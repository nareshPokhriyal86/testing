package com.lin.web.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mapsengine.MapsEngine;
import com.google.api.services.mapsengine.MapsEngineScopes;
import com.google.api.services.mapsengine.model.Feature;
import com.google.api.services.mapsengine.model.FeaturesBatchInsertRequest;
import com.google.api.services.mapsengine.model.FeaturesBatchPatchRequest;
import com.google.api.services.mapsengine.model.GeoJsonPoint;
import com.google.api.services.mapsengine.model.Schema;
import com.google.api.services.mapsengine.model.Table;
import com.google.api.services.mapsengine.model.TableColumn;
import com.lin.persistance.dao.IMapEngineDAO;
import com.lin.persistance.dao.impl.MapEngineDAO;
import com.lin.web.dto.MapEngineDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IMapEngineService;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class MapEngineService implements IMapEngineService {

	private static final Logger log = Logger.getLogger(MapEngineService.class.getName());
	
	private static final String APPLICATION_NAME = "Google/MapsEngineCsvUpload-1.0";
	private static final Collection<String> SCOPES = Arrays.asList(MapsEngineScopes.MAPSENGINE);
	//private final List<Feature> tableData = new ArrayList<Feature>();
	private MapsEngine engine;
	
	  /** Global instance of the HTTP transport. */
	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	  
	//  private static final String PROJECT_ID="12180835279485187370";//"10480020710656778579"
	 // String fileName = "SampleCsv.csv";

	/*
	 * @author Shubham Goel 
	 * Authorizes the installed application to access user's protected data.
	 */
	  public static Credential authorize() throws Exception {
	    GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
	      .setJsonFactory(JSON_FACTORY)
	      .setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
	      .setServiceAccountScopes(SCOPES)
	      //.setServiceAccountPrivateKeyFromP12File(new File("src/main/resources/env/keys/"+SERVICE_KEY))
	      .setServiceAccountPrivateKeyFromP12File(new File("keys/"+LinMobileVariables.SERVICE_ACCOUNT_KEY))
	      .build();
	    credential.refreshToken();
	    String refreshToken=credential.getRefreshToken();
	    System.out.println("refreshToken:"+refreshToken);
	    String accessToken=credential.getAccessToken();
	    System.out.println("accessToken : "+accessToken);
	    return credential;
	  }
	
	  /*
		 * @author Shubham Goel 
		 * update the data on GME vector tables by using tableId.
	 */
	@Override
	public boolean updateProductDataGME(String publisherIdInBQ){
		log.info("in updateProductDataGME of MapEngineService");
		IMapEngineDAO mapDAO = new MapEngineDAO();
		QueryDTO queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_PRODUCT_PERFORMANCE);
		List<MapEngineDTO> mapEngineDTOList = new ArrayList<>();
		int tableCount = 0;
		
		//String projectId = PROJECT_ID;
		try{
			Credential credential = authorize();//Authenticate service account
		    System.out.println("Authorization successful!");
			// The MapsEngine object will be used to perform the requests.
		    engine =  new MapsEngine.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
		    						.setApplicationName(APPLICATION_NAME).build();
		    
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				String[] queryData = queryDTO.getQueryData().split(",");
				tableCount= queryData.length;
				mapEngineDTOList = mapDAO.getProductDataGMEByStateId(queryDTO);
				if(mapEngineDTOList!=null && mapEngineDTOList.size()>0){
					updateProductDataByState(mapEngineDTOList, tableCount);// update data on GME state wise
				}
			}
			
			/*queryDTO = new QueryDTO();
			queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_PRODUCT_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				String[] queryData = queryDTO.getQueryData().split(",");
				tableCount= queryData.length;
				mapEngineDTOList = new ArrayList<>();
				mapEngineDTOList = mapDAO.getProductDataGMEByCityId(queryDTO);
				if(mapEngineDTOList!=null && mapEngineDTOList.size()>0){
					updateProductDataByState(mapEngineDTOList, tableCount);// update data on GME state wise
				}
			}*/
			
		}catch(Exception e){
			log.severe("Exception updateProductDataGME of MapEngineService."+e.getMessage());
			
		}
		return true;
		
	}
	
	 /*
	 * @author Shubham Goel 
	 * Performs a batch update of data into the table.
	 */

	  private void updateMapProductData(String tableId, List<Feature> features) throws IOException {
		  FeaturesBatchPatchRequest patchRequest = new FeaturesBatchPatchRequest()
          .setFeatures(features);

      engine.tables().features().batchPatch(tableId, patchRequest).execute();
	  }
	  
	  
	  public void updateProductDataByState(List<MapEngineDTO> mapEngineDTOList, int tableCount) throws IOException{
		  List<Feature> tableData = new ArrayList<Feature>();
		  String tableId = LinMobileConstants.NEW_US_STATE_PROD_TABLEID;
		  for (MapEngineDTO mapEngineDTOObj : mapEngineDTOList) {
				if(mapEngineDTOObj!=null){
					Map<String, Object> properties = new HashMap<String, Object>();
					if(mapEngineDTOObj.getStateId()!=0){
						properties.put("state_id", mapEngineDTOObj.getStateId()+"");
						properties.put("state_name", mapEngineDTOObj.getStateName());
						if(tableCount!=0){
							properties.put("impression", (mapEngineDTOObj.getImpressions()/tableCount)+"");
							properties.put("click", (mapEngineDTOObj.getClicks()/tableCount)+"");
						}else{
							properties.put("impression", mapEngineDTOObj.getImpressions()+"");
							properties.put("click", mapEngineDTOObj.getClicks()+"");
						}
						
						Feature feature=new Feature();
						feature.setProperties(properties);
						tableData.add(feature);
						if(tableData.size()%50 == 0){
							updateMapProductData(tableId, tableData); // update vector table on GME for first 50 features, as GME API supports only 50 features to update in a batch.
							tableData = new ArrayList<Feature>();
							
						}
					}
			
				}
			}
			updateMapProductData(tableId, tableData);// update vector table on GME for remaining features.
		  
	  }
	  
	  public void updateProductDataByCity(List<MapEngineDTO> mapEngineDTOList, int tableCount) throws IOException{
		  List<Feature> tableData = new ArrayList<Feature>();
		  String tableId = LinMobileConstants.NEW_US_STATE_PROD_TABLEID;
		  for (MapEngineDTO mapEngineDTOObj : mapEngineDTOList) {
				if(mapEngineDTOObj!=null){
					Map<String, Object> properties = new HashMap<String, Object>();
					if(mapEngineDTOObj.getStateId()!=0){
						properties.put("city_id", mapEngineDTOObj.getCityId()+"");
						properties.put("city_name", mapEngineDTOObj.getCityName());
						if(tableCount!=0){
							properties.put("impression", (mapEngineDTOObj.getImpressions()/tableCount)+"");
							properties.put("click", (mapEngineDTOObj.getClicks()/tableCount)+"");
						}else{
							properties.put("impression", mapEngineDTOObj.getImpressions()+"");
							properties.put("click", mapEngineDTOObj.getClicks()+"");
						}
						
						Feature feature=new Feature();
						feature.setProperties(properties);
						tableData.add(feature);
						if(tableData.size()%50 == 0){
							updateMapProductData(tableId, tableData); // update vector table on GME for first 50 features, as GME API supports only 50 features to update in a batch.
							tableData = new ArrayList<Feature>();
							
						}
					}
			
				}
			}
			updateMapProductData(tableId, tableData);// update vector table on GME for remaining features.
		  
	  }


}
