package com.lin.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.CommonDTO;

public class LinMobileProperties {
	private static final Logger log = Logger.getLogger(LinMobileProperties.class.getName());
		
	private static List<GoogleAuthorizationCodeFlow> googleAuthCodeFlow;
	private static GoogleCredential credentials;
	private static LinMobileProperties linMobileProperties=null;
	private final static Properties linChannelService;
	private static DfpServices dfpServices;
	private static Map<String,CloudProjectDTO> cloudProjectDTOMap;
	
	private List<CommonDTO> linChannelList;
 
	static {		
		linChannelService = new Properties();
		try {
			
			initCloudProjectMap();			
			linChannelService.load(LinMobileProperties.class.getResourceAsStream("/env/LinChannel.properties"));			
		} catch (IOException e) {
			log.severe(" LinChannel.properties not found- Exception :" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * This method will load all cloud projects into a DTOMap
	 * @author Youdhveer Panwar
	 */
	private static void initCloudProjectMap(){
		cloudProjectDTOMap=new HashMap<String, CloudProjectDTO>();
		CloudProjectDTO projectDTO=new CloudProjectDTO(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID, 
				LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID,
				LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, 
				LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY,
				LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET,
				LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		cloudProjectDTOMap.put(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID, projectDTO);
		
		projectDTO=new CloudProjectDTO(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID, 
				LinMobileConstants.LIN_DIGITAL_GOOGLE_API_PROJECT_ID,
				LinMobileConstants.LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, 
				LinMobileConstants.LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY,
				LinMobileVariables.LIN_DIGITAL_CLOUD_STORAGE_BUCKET,
				LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		cloudProjectDTOMap.put(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID, projectDTO);
		
		projectDTO=new CloudProjectDTO(LinMobileConstants.LIN_MOBILE_PUBLISHER_ID, 
				LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID,
				LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, 
				LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY,
				LinMobileVariables.LIN_MOBILE_CLOUD_STORAGE_BUCKET,
				LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		cloudProjectDTOMap.put(LinMobileConstants.LIN_MOBILE_PUBLISHER_ID, projectDTO);
		
		projectDTO=new CloudProjectDTO(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID, 
				LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID,
				LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, 
				LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY,
				LinMobileVariables.TRIBUNE_CLOUD_STORAGE_BUCKET,
				LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		cloudProjectDTOMap.put(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID, projectDTO);
		
		projectDTO=new CloudProjectDTO(LinMobileConstants.CLIENT_DEMO_COMPANY_ID, 
				LinMobileConstants.CLIENT_DEMO_GOOGLE_API_PROJECT_ID,
				LinMobileConstants.CLIENT_DEMO_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, 
				LinMobileConstants.CLIENT_DEMO_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY,
				LinMobileConstants.CLIENT_DEMO_CLOUD_STORAGE_BUCKET,
				LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
		cloudProjectDTOMap.put(LinMobileConstants.CLIENT_DEMO_COMPANY_ID, projectDTO);
		log.info("Cloud projects map has been loaded..cloudProjectDTOMap.size()::"+cloudProjectDTOMap.size());
		
	}
	
	@SuppressWarnings("rawtypes")
	private LinMobileProperties(){
		if(dfpServices==null){
			dfpServices = new DfpServices();
		}
		Set keySet = linChannelService.keySet();
		Iterator iterator = keySet.iterator();
		linChannelList = new ArrayList<CommonDTO>();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = linChannelService.getProperty(key);
			linChannelList.add(new CommonDTO(key,value));
		}
	}
	
	
	public static LinMobileProperties getInstance() {
		if (linMobileProperties == null) {
			linMobileProperties = new LinMobileProperties();
			log.info("LinMobileProperties:: instance loaded");
		}
		return linMobileProperties;
	}
	
	
	public static void setGoogleAuthCodeFlow(List<GoogleAuthorizationCodeFlow> googleAuthCodeFlow) {
		LinMobileProperties.googleAuthCodeFlow = googleAuthCodeFlow;
	}

	public static List<GoogleAuthorizationCodeFlow> getGoogleAuthCodeFlow() {
		return googleAuthCodeFlow;
	}


	public static void setCredentials(GoogleCredential credentials) {
		LinMobileProperties.credentials = credentials;
	}

	public static GoogleCredential getCredentials() {
		return credentials;
	}


	public void setLinChannelList(List<CommonDTO> linChannelList) {
		this.linChannelList = linChannelList;
	}


	public List<CommonDTO> getLinChannelList() {
		return linChannelList;
	}


	public DfpServices getDfpServices() {
		return dfpServices;
	}


	public void setDfpServices(DfpServices dfpServices) {
		LinMobileProperties.dfpServices = dfpServices;
	}


	public static Map<String,CloudProjectDTO> getCloudProjectDTOMap() {
		return cloudProjectDTOMap;
	}


	public static void setCloudProjectDTOMap(Map<String,CloudProjectDTO> cloudProjectDTOMap) {
		LinMobileProperties.cloudProjectDTOMap = cloudProjectDTOMap;
	}
	

}
