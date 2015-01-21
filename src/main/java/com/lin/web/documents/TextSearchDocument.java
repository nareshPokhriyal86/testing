package com.lin.web.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.Objectify;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.LinMobileUtil;

public class TextSearchDocument {
	 private static final Logger log = Logger.getLogger(
			TextSearchDocument.class.getName());

	 private static final Index INDEX = SearchServiceFactory.getSearchService()
          .getIndex(IndexSpec.newBuilder().setName("city_index"));
	 
	 private static final String ADUNIT_INDEX_NAME="adUnit_index";
	 private static final String CITY_OR_ZIP_INDEX="city_index";
	 
	enum Action {
	    ADD, REMOVE, DEFAULT;
	}
	
	public static void indexADocument(String indexName, Document document) {
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
	    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	    
	    try {
	        index.put(document);
	    } catch (PutException e) {
	    	log.severe("PutException :"+e.getMessage()+" : index-"+indexName+" and documentId:"+document.getId());
	        if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
	            // retry putting the document
	        }
	    }
	}
	
	public static Document createDocument(CityObj city){
		String docId=city.getId()+"";  //doc_id		
	    String zip=city.getZip();
	    String name=city.getText();
	    String stateId=city.getState().getId()+"";
		
		 String token = getAllSubstrings(city.getText());
		 String zipToten = getAllSubstrings(city.getZip());
			 
		 Document document = Document.newBuilder()
					.setId(docId)	    
				    .addField(Field.newBuilder().setName("token").setText(token))
				    .addField(Field.newBuilder().setName("name").setText(name))
				    .addField(Field.newBuilder().setName("stateId").setText(stateId))	    
				    .addField(Field.newBuilder().setName("zip").setText(zip))
				    .addField(Field.newBuilder().setName("zipToken").setText(zipToten))
				    .build();
					
					addDocumentInIndex(document);
		
		//log.info("document created:Id:"+docId+" & added in Index:"+added);
		return document;
	}
	
	private static String getAllSubstrings(String string) {
		String sub;
	      int i, c, length;
	      StringBuilder tokens = null;
	      
	      if(string != null && string.length() > 0){
	      tokens = new StringBuilder(string);
	      String[] allKeywords = string.split(" ");
	      for(String str : allKeywords) {
	    	  length = str.length();
		      for( c = 0 ; c < length ; c++ )
		      {
		         for( i = 1 ; i <= length - c ; i++ )
		         {
		            sub = str.substring(c, c+i);
		            tokens.append(" "+sub);
		         }
		      }
	      	}
	     }
	      return tokens.toString();
	}
	
    
	public static Document createDocument(AdUnitHierarchy adUnit){
		String docId=adUnit.getId();  //doc_id		
	    String adUnitName=adUnit.getAdUnitName();
	    if(adUnitName !=null && adUnitName.equalsIgnoreCase("null")){
	    	adUnitName="N/A";
	    }
	    String canonicalPath=adUnit.getCanonicalPath();
	    if(canonicalPath!=null && canonicalPath.indexOf("null")>=0){
	    	canonicalPath=canonicalPath.replaceAll("null","N/A");
	    }
	    String parentId=adUnit.getPid();
	    String adServerId=adUnit.getAdServerId();
	    String adUnitId=adUnit.getAdUnitId();
	    
	    String canonicalPathTokens = getAllSubstrings(canonicalPath);
	    String adUnitTokens= getAllSubstrings(adUnit.getAdUnitId());
	    
		Document document = Document.newBuilder()
			.setId(docId)	    
			.addField(Field.newBuilder().setName("adUnitId").setText(adUnitId))
		    .addField(Field.newBuilder().setName("name").setText(adUnitName))
		    .addField(Field.newBuilder().setName("parentId").setText(parentId))	    
		    .addField(Field.newBuilder().setName("canonicalPath").setText(canonicalPath))
		    .addField(Field.newBuilder().setName("adServerId").setText(adServerId))
		    .addField(Field.newBuilder().setName("canonicalPathTokens").setText(canonicalPathTokens))
		    .addField(Field.newBuilder().setName("adUnitTokens").setText(adUnitTokens))
		    .build();
		
		indexADocument(ADUNIT_INDEX_NAME,document);
		//log.info("document created:Id:"+docId+" & added in Index:"+added);
		return document;
	}
	
	public static Index getIndex(){		
	    return INDEX;
	}
	
	public static Index getIndex(String indexName){		
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	    return index;
	}
	
    public static String addDocumentInIndex(Document doc){
    	 try {
    	      INDEX.put(doc);
    	      return "Success";
    	 } catch (RuntimeException e) {
    	      log.severe("Failed to add " + e.getMessage());
    	      return "Failed-" + e.getMessage();
    	 }
    }
    
    public static String addDocumentInIndex(Document doc,String indexName){
    	 try {
    	      INDEX.put(doc);
    	      return "Success";
    	 } catch (RuntimeException e) {
    	      log.severe("Failed to add " + e.getMessage());
    	      return "Failed-" + e.getMessage();
    	 }
    }
    public static void deleteAllDocumentsFromIndex(){
    	try {
    	    while (true) {
    	        List<String> docIds = new ArrayList<String>();
    	        GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
    	        GetResponse<Document> response = getIndex().getRange(request);
    	        if (response.getResults().isEmpty()) {
    	            break;
    	        }
    	        for (Document doc : response) {
    	            docIds.add(doc.getId());
    	        }
    	        getIndex().delete(docIds);
    	    }
    	} catch (RuntimeException e) {
    	    log.severe("Failed to delete documents"+ e.getMessage());
    	}
    }
    
    public static void deleteAllDocumentsFromIndex(String indexName){
    	try {
    		int count=0;
    	    while (true) {
    	        List<String> docIds = new ArrayList<String>();
    	        GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
    	        GetResponse<Document> response = getIndex(indexName).getRange(request);
    	        if (response.getResults().isEmpty()) {
    	            break;
    	        }
    	        for (Document doc : response) {
    	            docIds.add(doc.getId());
    	        }
    	        getIndex(indexName).delete(docIds);
    	        count++;
    	    }
    	    log.info("Total documents deleted.."+count);
    	} catch (RuntimeException e) {
    	    log.severe("Failed to delete documents"+ e.getMessage());
    	}
    }
    
    
    public static void deleteDocumentFromIndex(Document doc){
    	try {    	   
    	    getIndex().delete(doc.getId());
    	    log.info("Document deleted successfully.."+doc.getId());
    	} catch (RuntimeException e) {
    	    log.severe("Failed to delete document"+ e.getMessage());
    	}
    }
	
    public static void deleteDocumentFromIndex(Document doc,String indexName){
    	try {   
    		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build(); 
    		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    		index.delete(doc.getId());
    	    log.info("Document deleted successfully.."+doc.getId());
    	} catch (RuntimeException e) {
    	    log.severe("Failed to delete document"+ e.getMessage());
    	}
    }

      
    public static Results<ScoredDocument> findAdUnitDocuments(String queryString, int offSet,int limit) {
        try {
            SortOptions sortOptions = SortOptions.newBuilder()
                .addSortExpression(SortExpression.newBuilder()
                    .setExpression("canonicalPath")
                    .setDirection(SortExpression.SortDirection.DESCENDING)
                    .setDefaultValue(""))
                .setLimit(1000)
                .build();
            QueryOptions options = QueryOptions.newBuilder()
                .setOffset(offSet)
                .setLimit(limit)
                .setFieldsToSnippet("canonicalPath", "adServerId")
                .setFieldsToReturn("name","canonicalPath","parentId","adUnitId")
                .setSortOptions(sortOptions)              
                .build();
            Query query = Query.newBuilder().setOptions(options).build(queryString);
            return getIndex(ADUNIT_INDEX_NAME).search(query);
        } catch (SearchException e) {
            log.severe("Search request with query " + queryString + " failed"+ e.getMessage());
            return null;
        }
    }
    
    public static Results<ScoredDocument> findCityOrZipDocuments(String queryString, int offSet,int limit) {
        try {
            SortOptions sortOptions = SortOptions.newBuilder()
                .addSortExpression(SortExpression.newBuilder()
                    .setExpression("zip")
                    .setDirection(SortExpression.SortDirection.DESCENDING)
                    .setDefaultValue(""))
                .setLimit(1000)
                .build();
            QueryOptions options = QueryOptions.newBuilder()
                .setOffset(offSet)
                .setLimit(limit)
                .setFieldsToSnippet("zip", "name", "stateId")
                .setFieldsToReturn("zip","name","stateId")
                .setSortOptions(sortOptions)              
                .build();
            Query query = Query.newBuilder().setOptions(options).build(queryString);
            return getIndex().search(query);
        } catch (SearchException e) {
            log.severe("Search request with query " + queryString + " failed"+ e.getMessage());
            return null;
        }
    }
    
    /*public static List<CityDTO> searchCityOrZip(String queryString,int start,int limit){
    	List<CityDTO> searchedDTOList=new ArrayList<CityDTO>(0);
    	int startCounter=start*50;
    	int endCounter=startCounter+50;
    	int count=0;
    	
    	CityDTO cityDTO; 
    	Results<ScoredDocument> results =findCityOrZipDocuments(queryString, 0,50);

		if (results != null) {
		  log.info("Searched results:"+results.getNumberFound()+" and startCounter:"+startCounter+" and endCounter:"+endCounter);
		  for (ScoredDocument scoredDocument : results) {			  
			  cityDTO=new CityDTO();
			  String cityId=scoredDocument.getId();
			  cityDTO.setId(Long.parseLong(cityId));
			  			  
			  String zip=scoredDocument.getOnlyField("zip").getText();
			  cityDTO.setZip(zip);
			  
			  String name=scoredDocument.getOnlyField("name").getText();
			  cityDTO.setText(name +" - "+zip);
			  
			  String stateId=scoredDocument.getOnlyField("stateId").getText();
			  cityDTO.setStateId(Long.parseLong(stateId));
			
			  if(count < endCounter && count >= startCounter){
				  searchedDTOList.add(cityDTO);	
			  }		  		  
			  count++;
		  }
		}
		log.info("Searched docs:"+searchedDTOList.size());
		return searchedDTOList;
    }*/
    
    public static List<CityDTO> searchCity(String queryString,int start,int limit){
    	List<CityDTO> searchedDTOList=new ArrayList<CityDTO>(0);
    	int startCounter=start*50;
    	int endCounter=startCounter+50;
    	int count=0;
    	CityDTO cityDTO;
	    
    	Results<ScoredDocument> results =findCityOrZipDocuments(queryString, 0,50);

		if (results != null) {
		  log.info("Searched results:"+results.getNumberFound()+" and startCounter:"+startCounter+" and endCounter:"+endCounter);
		  for (ScoredDocument scoredDocument : results) {			  
			  cityDTO=new CityDTO();
			  String cityId=scoredDocument.getId();
			  cityDTO.setId(Long.parseLong(cityId));
			  
			  String name=scoredDocument.getOnlyField("name").getText();
			  cityDTO.setText(name);
			  
			  String stateId=scoredDocument.getOnlyField("stateId").getText();
			  cityDTO.setStateId(Long.parseLong(stateId));
			
			  if(count < endCounter && count >= startCounter && !searchedDTOList.contains(cityDTO)){
				  searchedDTOList.add(cityDTO);	
			  }		  		  
			  count++;
		  }
		}
		log.info("Searched docs:"+searchedDTOList.size());
		return searchedDTOList;
    }
    
    public static List<ZipDTO> searchZip(String queryString,int start,int limit){
    	List<ZipDTO> searchedDTOList=new ArrayList<ZipDTO>(0);
    	int startCounter=start*50;
    	int endCounter=startCounter+50;
    	int count=0;
    	
    	ZipDTO zipDTO; 
    	Results<ScoredDocument> results =findCityOrZipDocuments(queryString, 0,50);

		if (results != null) {
		  log.info("Searched results:"+results.getNumberFound()+" and startCounter:"+startCounter+" and endCounter:"+endCounter);
		  for (ScoredDocument scoredDocument : results) {			  
			  zipDTO=new ZipDTO();
			  String cityId=scoredDocument.getId();
			  zipDTO.setCityId(cityId);
			  			  
			  String cityName=scoredDocument.getOnlyField("name").getText();
			  zipDTO.setCityName(cityName);
			  
			  String zip=scoredDocument.getOnlyField("zip").getText();
			  //zipDTO.setText(zip +" - "+cityName);
			  zipDTO.setText(zip);
			  
			  String stateId=scoredDocument.getOnlyField("stateId").getText();
			  zipDTO.setStateId(Long.parseLong(stateId));
			
			  if(count < endCounter && count >= startCounter){
				  searchedDTOList.add(zipDTO);	
			  }		  		  
			  count++;
		  }
		}
		log.info("Searched docs:"+searchedDTOList.size());
		return searchedDTOList;
    }
    
    public static List<AdUnitDTO> searchAdUnits(String queryString,int start,int limit){
    	List<AdUnitDTO> searchedDTOList=new ArrayList<AdUnitDTO>(0);
    	int startCounter=start*50;
    	int endCounter=startCounter+200;
    	int count=0;
    	
    	AdUnitDTO adUnitDTO; 
    	Results<ScoredDocument> results =findAdUnitDocuments(queryString,start,limit);

		if (results != null) {
		  log.info("Searched results:"+results.getNumberFound()+" and startCounter:"+startCounter+" and endCounter:"+endCounter);
		  for (ScoredDocument scoredDocument : results) {			  
			  adUnitDTO=new AdUnitDTO();
			  String adUnitId=scoredDocument.getOnlyField("adUnitId").getText();
			  if(adUnitId ==null || !LinMobileUtil.isNumeric(adUnitId)){
				  adUnitId="0";
			  }
			  adUnitDTO.setId(Long.parseLong(adUnitId));
			  			  
			  String name=scoredDocument.getOnlyField("name").getText();
			  if(name !=null && name.equalsIgnoreCase("null")){
				  name="N/A";
			  }
			  adUnitDTO.setName(name);
			  
			  String canonicalPath=scoredDocument.getOnlyField("canonicalPath").getText();
			  if(canonicalPath !=null && canonicalPath.indexOf("null")>=0){
				  canonicalPath=canonicalPath.replaceAll("null","N/A");
			  }
			  adUnitDTO.setCanonicalPath(canonicalPath);
			  
			  String parentId=scoredDocument.getOnlyField("parentId").getText();
			  if(parentId ==null || !LinMobileUtil.isNumeric(parentId)){
				  parentId="0";
			  }
			  adUnitDTO.setPid(Long.parseLong(parentId));
			
			  if(count < endCounter && count >= startCounter){
				  searchedDTOList.add(adUnitDTO);	
			  }		  		  
			  count++;
		  }
		}
		log.info("Searched docs:"+searchedDTOList.size());
		return searchedDTOList;
    } 
    
	public static int getCityObjCount()throws DataServiceException {
		Objectify obfy = OfyService.ofy();
		return obfy.load().type(CityObj.class).count();
	}
	
	public static int getAdUnitHierarchyCount()throws DataServiceException {
		Objectify obfy = OfyService.ofy();
		return obfy.load().type(AdUnitHierarchy.class).count();
	}
   
}
