package com.lin.web.layer.fusion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.FetchOptions.Builder;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class FusionTableUtil {
	private static final Logger log = Logger.getLogger(FusionTableUtil.class.getName());
	
	private static final String API_KEY="AIzaSyB1ObhtSzt15zQdOBjl5yq_eFD5qRfdRh8"; //"AIzaSyC80K_M0QJ84WS_k_OudC0DneY8i1PBMeI";
	private static final String FUSION_SERVICE_URL = "https://www.googleapis.com/fusiontables/v1/tables";
	
	private static final String MERGE_TABLE_ID= LinMobileVariables.FUSION_POOL_MAP_MERGED_TABLE;
	
	private static final String INVENTORY_FORECAST_TABLE_NAME="InventoryForecastData";
	private static final String INVENTORY_FORECAST_TABLE_ID=LinMobileVariables.FUSION_INVENTORY_FORECAST_TABLE;
	
	private static final String FUSION_SERVICE_UPLOAD_URL = "https://www.googleapis.com/upload/fusiontables/v1/tables";
	private static final String FUSION_QUERY_SERVICE_URL = "https://www.googleapis.com/fusiontables/v1/query";
	private static final String SPACE="%20";
	private static final String SINGLE_QUOTE="%27";
	
	public static String getFusionTable(String accessToken) {		
		
		String requestURL=FUSION_SERVICE_URL +"?"+"access_token="+accessToken;
		
		StringBuffer dataBuffer=getWebServiceResponse(requestURL);		
		return dataBuffer.toString();
   }
	
   public static String createFusionTable(String accessToken) throws Exception {
    	String requestURL=FUSION_SERVICE_URL +"?"+"access_token="+accessToken;
    	String jsonContent = "{'name': '"+INVENTORY_FORECAST_TABLE_NAME+"','columns': " +
    			"[{'name': 'Site_Name','type': 'STRING'},{'name': 'Creative_Size','type': 'STRING'}" +
    			",{'name': 'Start_Date','type': 'STRING'},{'name': 'End_Date','type': 'STRING'}" +
    			",{'name': 'Forecasted_Impressions','type': 'NUMBER'},{'name': 'Available_Impressions','type': 'NUMBER'}" +
    			",{'name': 'Reserved_Impressions','type': 'NUMBER'},{'name': 'Pubisher_ID','type': 'NUMBER'}" +
    			",{'name': 'Publisher_Name','type': 'STRING'},{'name': 'UID','type': 'STRING'}]" +
    			",'description': 'Record Inventory forecasting data','isExportable': true}";
    	String contentType="application/json";
    	String responseStr=sendPostRequestByContentType(requestURL, jsonContent,contentType);
    	return responseStr;
   }
	
   public static String createFusionTable(String accessToken,String jsonContent) throws Exception {
    	String requestURL=FUSION_SERVICE_URL +"?"+"access_token="+accessToken;
    	//String jsonContent = "{'name': 'newIndia','columns': [{'name': 'Species','type': 'STRING'}],'description': 'Insect Tracking Information.','isExportable': true}";
    	String contentType="application/json";
    	String responseStr=sendPostRequestByContentType(requestURL, jsonContent,contentType);
    	return responseStr;
   }
   
   public static String selectFusionTable(String accessToken,String startDate,String endDate) throws Exception {
   	String requestURL=FUSION_QUERY_SERVICE_URL +"?"+"access_token="+accessToken;
    String tableId=INVENTORY_FORECAST_TABLE_ID;
    String query="&sql="+
    		"SELECT"+SPACE+"ROWID"+SPACE+"FROM"+SPACE+tableId+SPACE+"WHERE"+SPACE+"Start_Date=%27"+
    		startDate+SINGLE_QUOTE+SPACE+"AND"+SPACE+"End_Date="+SINGLE_QUOTE+endDate+SINGLE_QUOTE;    
    //query=URLEncoder.encode(query, "UTF-8");
   // query=query.replace("TABLE_ID", tableId);
    requestURL=requestURL+query; 
   
    StringBuffer response=getWebServiceResponse(requestURL);
    log.info("response:"+response.toString());    
   	return response.toString();
  }
    
   public static String deleteFusionTable(String accessToken,String rowIds) throws Exception {	   
	   	String requestURL=FUSION_QUERY_SERVICE_URL +"?"+"access_token="+accessToken;
	    String tableId=INVENTORY_FORECAST_TABLE_ID;	     	
	   	String query="DELETE FROM "+tableId+" WHERE ROWID = '"+rowIds+"'";
    	String params="sql="+query;		
    	String responseStr=sendPlainPostRequest(requestURL, params);    	
	   	return responseStr;
  }
  
   public static String deleteAllRowsFusionTable(String accessToken,String tableId) throws Exception {	   
	   	String requestURL=FUSION_QUERY_SERVICE_URL +"?"+"access_token="+accessToken;	   
	   	String query="DELETE FROM "+tableId;
	   	String params="sql="+query;		
	   	String responseStr=sendPlainPostRequest(requestURL, params);    	
	   	return responseStr;
 }
	
   public static String insertFusionTable(String accessToken,String adUnitId,String adUnitName,
		   Map<String,ForcastDTO> inventryMap,String startDate,String endDate) 
		   throws Exception {
	   ForcastDTO forecastDTO=inventryMap.get(adUnitId);
	   String dateUID=startDate+":"+endDate;
	   String tableId=INVENTORY_FORECAST_TABLE_ID;
  	   String query="INSERT INTO "+tableId+" (Site_Name, Creative_Size, Start_Date, End_Date, " +
  	    		"Forecasted_Impressions, Available_Impressions, Reserved_Impressions, " +
  	    		"Pubisher_ID, Publisher_Name,DateUID ) VALUES ('"+adUnitName+"','All'" +
  	    		",'"+startDate+"','"+endDate+"'"+
  	    		","+forecastDTO.getPossibleImpressions()+","+forecastDTO.getAvailableImpressions()+
  	    		","+forecastDTO.getReservedImpressions()+",1,'Lin Media' " +
  	    		","+dateUID+")";
  	   
   	   String requestURL=FUSION_QUERY_SERVICE_URL+"?access_token="+accessToken;
   	   String params = "sql="+query;
	   String responseStr=sendPlainPostRequest(requestURL, params);
	   return responseStr;
	}	
	
   public static String getRowIds(String accessToken,String startDate,String endDate){
	   
	   StringBuffer strBuffer=new StringBuffer();
	   String rowIds=null;
	   try {
		  
		  String selectResponse=selectFusionTable(accessToken, startDate, endDate);
		  JSONObject jsonResponse = (JSONObject) JSONSerializer.toJSON(selectResponse);
		  JSONArray jsonArray=jsonResponse.getJSONArray("rows");
		  if(jsonArray !=null){
			  //System.out.println("jsonArray:"+jsonArray.size());
			  for(int i=0;i<jsonArray.size();i++){
				  if(i==0){
					  strBuffer.append(jsonArray.get(i).toString());					  
				  }else{
					  strBuffer.append(","+jsonArray.get(i).toString());
				  }				  
			  }
			  rowIds=strBuffer.toString();
			  //log.info("rowIdS:"+rowIds);			
			  rowIds = rowIds.replaceAll("\\[", "").replaceAll("\\]","");			 
			  rowIds=rowIds.replaceAll("\"", "");
			  //log.info("Row :Ids:"+rowIds);			 
			  
		  }
	   } catch (Exception e) {
		  log.severe("selectResponse:Exception :"+e.getMessage());
		  e.printStackTrace();
	   }
	   return rowIds;	   
   }
   
   public static String insertFusionTableByImport(String accessToken, List<ForcastDTO> inventoryList,
		   String startDate,String endDate,String publisherId,boolean dayWise) {
	   String responseStr=null;
	   String importData="";
	   if(dayWise){
		   importData=createDayWiseCSVDataForFusionTable(inventoryList, startDate, endDate, publisherId);
	   }else{
		   importData=createCSVDataForFusionTable(inventoryList, startDate, endDate, publisherId);
	   }
	   
	   log.info("===========importData=======:\n"+importData);
	   String tableId=INVENTORY_FORECAST_TABLE_ID;
  	   String requestURL=FUSION_SERVICE_UPLOAD_URL+"/"+tableId+"/import"+"?access_token="+accessToken;
   	   String contentType="application/octet-stream";
	   
	   int i=0;
	   while(i<3 && responseStr ==null){
		   log.info("send insert request :i:"+i);
		   responseStr= resendRequest(requestURL, importData, contentType);
		   i++;
	   }		
	   
	   return responseStr;
	}	
   
   public static String resendRequest(String requestURL, String importData,String contentType) {
	   String responseStr=null;
	   try {
		   responseStr = sendPostRequestByContentType(requestURL, importData,contentType);
	   } catch (Exception e) {
		   responseStr=null;
		   log.severe("Exception :"+e.getMessage());
		   e.printStackTrace();
	   }
	  return responseStr;
   }
   
    public static String createCSVDataForFusionTable(List<ForcastDTO> inventoryList,
 		   String startDate,String endDate,String publisherId){
    	String dateUID=startDate+":"+endDate;
    	String publisherName="";
    	if(publisherId.equalsIgnoreCase(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)){
    		publisherName=LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME;
    	}else if(publisherId.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)){
    		publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
    	}else if(publisherId.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)){
    		publisherName=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME;
    	}
    	
    	StringBuffer strBuffer=new StringBuffer();
    	for(int i=0;i<inventoryList.size();i++){
    		ForcastDTO forecastDTO=inventoryList.get(i);
    		strBuffer.append(forecastDTO.getAdUnitName()+",");    		
    		strBuffer.append("All"+",");
    		strBuffer.append(startDate+",");
    		strBuffer.append(endDate+",");
    		strBuffer.append(forecastDTO.getPossibleImpressions()+",");
    		strBuffer.append(forecastDTO.getAvailableImpressions()+",");
    		strBuffer.append(forecastDTO.getReservedImpressions()+",");
    		strBuffer.append(publisherId+","+publisherName);
    		strBuffer.append(","+dateUID);
    		strBuffer.append('\n');
    	} 
    	return strBuffer.toString();
    }
    
    public static String createDayWiseCSVDataForFusionTable(List<ForcastDTO> inventoryList,
  		   String startDate,String endDate,String publisherId){
     	String dateUID=startDate+":"+endDate;
     	String publisherName="";
     	if(publisherId.equalsIgnoreCase(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)){
     		publisherName=LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME;
     	}else if(publisherId.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)){
     		publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
     	}else if(publisherId.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)){
     		publisherName=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME;
     	}
     	
     	String tmp=DateUtil.getModifiedDateStringByDays(startDate, 1, "yyyy-MM-dd");
     	List<String> dataRangeList=new ArrayList<String>();
 		while(tmp !=null && !tmp.equals(endDate)){
 			tmp=DateUtil.getModifiedDateStringByDays(tmp, 1, "yyyy-MM-dd");
 			dataRangeList.add(tmp);
 		}
 		int size=dataRangeList.size();
     	StringBuffer strBuffer=new StringBuffer();
     	for(int i=0;i<inventoryList.size();i++){
     		ForcastDTO forecastDTO=inventoryList.get(i);
     		long avlImp=size==0?forecastDTO.getAvailableImpressions():
     			forecastDTO.getAvailableImpressions()/size;
     		long posImp=size==0?forecastDTO.getPossibleImpressions():
     			forecastDTO.getPossibleImpressions()/size;
     		long reserveImp=size==0?forecastDTO.getReservedImpressions():
     				forecastDTO.getReservedImpressions()/size;
     		for(String date:dataRangeList){
     			strBuffer.append(forecastDTO.getAdUnitName()+",");    		
         		strBuffer.append("All"+",");
         		strBuffer.append(date+",");
         		strBuffer.append(date+",");
         		strBuffer.append(posImp+",");
         		strBuffer.append(avlImp+",");
         		strBuffer.append(reserveImp+",");
         		strBuffer.append(publisherId+","+publisherName);
         		strBuffer.append(","+dateUID);
         		strBuffer.append('\n');
     		}
     		
     	} 
     	return strBuffer.toString();
     }
	public static String sendPlainPostRequest(String webServerURL,String params) throws Exception{
		
		String responseStr=null;
		URL url = new URL(webServerURL);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST, Builder.allowTruncate());		
		request.setPayload(params.getBytes());		
		URLFetchService service = URLFetchServiceFactory.getURLFetchService();
		HTTPResponse response = service.fetch(request);
		if (response != null) {
	            int responseCode = response.getResponseCode();	  
	            //System.out.println("responseCode:"+responseCode);
	            List<HTTPHeader> headers = response.getHeaders();
	            byte[] content = response.getContent();
	            responseStr=new String(content);
	    }		
		log.info("response code:"+response.getResponseCode());
		return responseStr;
	}
	
	public static String sendPostRequestByContentType(String postURL,String data,String contentType) throws Exception{
		String responseStr=null;
        URL url = new URL(postURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType);  //"application/json"
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
        connection.setUseCaches(false);

        OutputStreamWriter  writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(data);        
        writer.close();
        responseStr="Response code: "+connection.getResponseCode()+" and mesg:"+connection.getResponseMessage();
              
        InputStream response;                   

        // Check for error , if none store response
        if(connection.getResponseCode() == 200){
        	response = connection.getInputStream();
        }else{
        	response = connection.getErrorStream();
        }
        InputStreamReader isr = new InputStreamReader(response);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(isr);
        String read = br.readLine();
        while(read != null){
            sb.append(read);
            read = br.readLine();
        }   
        // Print the String     
        responseStr=sb.toString();
        
        connection.disconnect();
		return responseStr;
	}
	
	public static String sendGetRequest(String webServerURL) throws Exception{
		String response=null;		
		  
		URL url = new URL(webServerURL);
		URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();			
		HTTPResponse httpResponse =  urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));
		response=new String(httpResponse.getContent());
		log.info("resp:"+httpResponse.getResponseCode()+" \n content : "+response);
		
		return response;
	}
	
	public static StringBuffer getWebServiceResponse(String webServerURL) {
		log.info("Going to read webServerURL:"+webServerURL);
		StringBuffer dataBuffer = new StringBuffer();
		try {
			URL url = new URL(webServerURL);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				dataBuffer.append(line);
				line = reader.readLine();
			}
			log.info("Reading response in buffer done...");
			reader.close();

		} catch (MalformedURLException e) {
			log.severe("MalformedURLException :" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			log.severe("Exception:" + e.getMessage());
			e.printStackTrace();
		}
		return dataBuffer;
	}
	
	public static StringBuffer getWebServiceResponseInCSV(String webServerURL) {
		StringBuffer dataBuffer = new StringBuffer();		
		try {            
			    URL url = new URL(webServerURL);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	dataBuffer.append(line+"\n");	            	
	            }
	            //log.info("dataBuffer:"+dataBuffer.toString());
	            reader.close();

		} catch (MalformedURLException  e) {
			log.severe("MalformedURLException :" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			e.printStackTrace();
		} 
		return dataBuffer;
	}
	
	public static void main(String arg[]){
		
	}

}
