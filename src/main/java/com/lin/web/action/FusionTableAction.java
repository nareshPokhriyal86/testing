package com.lin.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.lin.dfp.api.IForecastInventoryService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.ForecastInventoryService;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;


public class FusionTableAction implements ServletRequestAware,ServletResponseAware{

	
	private static final Logger log = Logger.getLogger(FusionTableAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String accessToken;
	private String error;
	private DfpSession dfpSession;
    private DfpServices dfpServices;
       
    public String uploadForecastInventoryByTaskQueue(){		
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory1.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory2.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory3.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory4.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory5.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory6.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory7.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory8.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory9.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory10.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory11.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadLinMediaInventory12.lin");
		log.info("Now add all jobs to upload data for Tribune..");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/loadTribuneInventory.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadTribuneInventoryQ1.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadTribuneInventoryQ2.lin");
		TaskQueueUtil.addInventoryUploadTaskInDefaultQueue("/uploadTribuneInventoryQ3.lin");
		reportsResponse="For uploading Lin Media and Tribune forecasting data, 16 tasks have been added successfully";
			
    	return Action.SUCCESS;
    }
    
    /*
     * Load LinMedia Inventory using forecast service
     *  and upload data in fusion table
     */
	public String execute(){
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String adUnitId=request.getParameter("adUnitId");
		String emptyTable=request.getParameter("emptyTable");
		String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
		
		if(startDate ==null || endDate == null){
			Date currentDate=new Date();
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			endDate=DateUtil.getEndDateOfMonthFromCurrent(0, "yyyy-MM-dd");			
		}
		
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		if(emptyTable !=null){
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);
		}else{
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,true);
		}
			
		
		return Action.SUCCESS;
	}
	
	 /*
     * Load Tribune Inventory using forecast service
     *  and upload data in fusion table
     */
	public String loadTribuneInventory(){
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String adUnitId=request.getParameter("adUnitId");
		String emptyTable=request.getParameter("emptyTable");
		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
		
		if(startDate ==null || endDate == null){
			Date currentDate=new Date();
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			endDate=DateUtil.getEndDateOfMonthFromCurrent(0, "yyyy-MM-dd");			
		}
		
		log.info("startDate:"+startDate+" and endDate:"+endDate);
		if(emptyTable !=null && emptyTable.equalsIgnoreCase("true")){
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,true);
		}else{
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);
		}			
		
		return Action.SUCCESS;
	}
	
    @SuppressWarnings("deprecation")
	private void fecthForecastDataAndUpdateDataStore(String startDate,String endDate,
    		String publisherId,boolean doEmptyTable){
    	
    	validateAccessToken();	
    	try {			
			
    		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			Map<String,String> siteMap = new LinkedHashMap<String, String>();
			
			if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)){
				List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
	            siteMap = userService.getAdunit1(propertyObjList);
			
				dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.DFP_NETWORK_CODE,
						LinMobileConstants.DFP_APPLICATION_NAME);
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)){
				List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID);
	            siteMap = userService.getAdunit1(propertyObjList);
				
				dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE,
						LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME);
				
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)){
				List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
	            siteMap = userService.getAdunit1(propertyObjList);
	            dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE,
						LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME);
					
			}else{
				throw new Exception("Invalid DFP account.....");
			}
						
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
           
			List<ForcastDTO> inventoryList=new ArrayList<ForcastDTO>();
			List<ForcastInventoryObj> forcastInventoryObjsList = new ArrayList<>();
            int i=0;
            
            if(siteMap == null || siteMap.size() == 0) {
            	throw new Exception("No Active sites for publisherId : "+publisherId);
            }
            log.info("Sites loaded :"+siteMap.size()+" for publisherId:"+publisherId);
            IForecastInventoryService forecastService=new ForecastInventoryService();
            
            for (Map.Entry<String, String> entry : siteMap.entrySet()) {
				
				String adUnitId= entry.getKey();
				String adUnitName=entry.getValue();
				
				ForcastDTO forecastDTO=forecastService.
						loadForecastDataByAdUnit(dfpServices,dfpSession, adUnitId,adUnitName,startDate, endDate);
				inventoryList.add(forecastDTO);
				
			}
			if(inventoryList!=null && inventoryList.size()>0){
				forcastInventoryObjsList = forecastService.deleteUpdateForcastInventoryObj(inventoryList, startDate, endDate, doEmptyTable);
			}
			
			/*if(doEmptyTable){
				reportsResponse=FusionTableUtil.deleteAllRowsFusionTable(accessToken,LinMobileVariables.FUSION_INVENTORY_FORECAST_TABLE);	
			}
			log.info("going to insert data into fusion table...via import: inventoryList:"+inventoryList.size());
			boolean dayWise=false;
			reportsResponse=FusionTableUtil.insertFusionTableByImport(accessToken, 
					inventoryList,startDate, endDate,publisherId,dayWise);
			log.info("Done : reportsResponse:"+reportsResponse);*/
			
		} catch (Exception e) {
			log.severe("Exception:"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		}
    }
    
   
    public String cronJob2(){	
    	
		String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;			
		String startDate=DateUtil.getStartDateOfMonthFromCurrent(1, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(1, "yyyy-MM-dd");
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		
		return Action.SUCCESS;
	}
    
    public String cronJob3(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(2, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(2, "yyyy-MM-dd");
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob4(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(3, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(3, "yyyy-MM-dd");			
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob5(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(4, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(4, "yyyy-MM-dd");		
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob6(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(5, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(5, "yyyy-MM-dd");	
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob7(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(6, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(6, "yyyy-MM-dd");		
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob8(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(7, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(7, "yyyy-MM-dd");	
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob9(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(8, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(8, "yyyy-MM-dd");			
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob10(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(9, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(9, "yyyy-MM-dd");		
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob11(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(10, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(10, "yyyy-MM-dd");		
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
    public String cronJob12(){
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String startDate=DateUtil.getStartDateOfMonthFromCurrent(11, "yyyy-MM-dd");
		String endDate=DateUtil.getEndDateOfMonthFromCurrent(11, "yyyy-MM-dd");	
		log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
		fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		return Action.SUCCESS;
	}
    
	
	
   public String uploadTribuneInventoryQ1(){
		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;		
		for(int i=1;i<5;i++){
			String startDate=DateUtil.getStartDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			String endDate=DateUtil.getEndDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		}
		return Action.SUCCESS;
	}
   
   public String uploadTribuneInventoryQ2(){
		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;		
		for(int i=5;i<9;i++){
			String startDate=DateUtil.getStartDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			String endDate=DateUtil.getEndDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		}
		return Action.SUCCESS;
	}

   public String uploadTribuneInventoryQ3(){
		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;		
		for(int i=9;i<12;i++){
			String startDate=DateUtil.getStartDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			String endDate=DateUtil.getEndDateOfMonthFromCurrent(i, "yyyy-MM-dd");
			log.info("fusionTableAccess action executes..startDate:"+startDate+" and endDate:"+endDate);
			fecthForecastDataAndUpdateDataStore(startDate, endDate, publisherId,false);	
		}
		return Action.SUCCESS;
   }
   
     
   private void validateAccessToken(){
		
		Entity credentials = null;
		try {
	    	log.info("Going to fetch credentials...");
	        DatastoreService datastore =DatastoreServiceFactory.getDatastoreService();
	        Key credsKey = KeyFactory.createKey("Credentials", "FusionTable");
	        credentials = datastore.get(credsKey);
	      
	        GoogleTokenResponse tokens = new GoogleTokenResponse();
		    tokens.setAccessToken((String) credentials.getProperty("accessToken"));
		    tokens.setExpiresInSeconds((Long) credentials.getProperty("expiresIn"));
		    tokens.setRefreshToken((String) credentials.getProperty("refreshToken"));
		    String clientId = (String) credentials.getProperty("clientId");
		    String clientSecret = (String) credentials.getProperty("clientSecret");
		    tokens.setScope(LinMobileConstants.FUSION_TABLE_SCOPE);
		    
		    HttpTransport httpTransport = new NetHttpTransport();
		    JsonFactory jsonFactory = new JacksonFactory();
		    
		    GoogleCredential credential=new GoogleCredential.Builder()			       
					.setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setClientSecrets(clientId,clientSecret)
					.addRefreshListener(new CredentialRefreshListener() {
				public void onTokenResponse(Credential credential,
						TokenResponse tokenResponse) {
					log.info("Credential was refreshed successfully: "+ tokenResponse.getAccessToken());
					accessToken=tokenResponse.getAccessToken();
					
				}
				public void onTokenErrorResponse(Credential credential,
						TokenErrorResponse tokenErrorResponse) {
					log.severe("Credential was not refreshed successfully. Redirect to error page or login screen.");
				}
			}).build();
		    credential.setFromTokenResponse(tokens);
			credential.refreshToken();
			
			if(accessToken !=null){
				tokens.setAccessToken(accessToken);
			}
			
	    } catch (EntityNotFoundException ex) {
	    	 log.severe("Please authorize using oauth2 first, EntityNotFoundException :"+ex.getMessage());
	    	 error=ex.getMessage();
	         ex.printStackTrace();	
	         authorizeUsingOauth2();
	    } catch (IOException e) {
			log.severe("IOException :"+e.getMessage());
			error=e.getMessage();
			e.printStackTrace();				
		} 
	    
	}
   
    public String authorizeUsingOauth2(){
    	try {
   	      Entity credentials = null;   	     
   	      DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
   	      Key creds_key = KeyFactory.createKey("Credentials", "FusionTable");
   	      credentials = ds.get(creds_key);
   	      
   	      log.info("found credential in datastore, going to adExReport..");
  	      try {
  	    	response.sendRedirect("/fusionTableAccess.lin");;
		  } catch (IOException e) {
			 log.severe("IOException: "+e.getMessage());
			 e.printStackTrace();
		  }
		
   	    } catch (EntityNotFoundException ex) {
   	      log.warning("No credentials availble, create new...");      
   	    
   	      GoogleAuthorizationCodeRequestUrl requestUrl = 
   	        new GoogleAuthorizationCodeRequestUrl(
   	        		LinMobileVariables.FUSION_TABLE_CLIENT_ID, 
   	        		LinMobileVariables.FUSION_TABLE_CALLBACK_URL, 
   	        		Arrays.asList(LinMobileConstants.FUSION_TABLE_SCOPE));
   	      
   	      requestUrl.setAccessType("offline");
   	      requestUrl.setApprovalPrompt("force");
   	      System.out.println("before sendRedirect:"+requestUrl.build());
   	      try {
			response.sendRedirect(requestUrl.build());
		  } catch (IOException e) {
			  log.severe("IOException: "+e.getMessage());
			e.printStackTrace();
		  }
   	      
   	    }
   	   
		return null;
    }
    
  
    
    public String oauth2Callback(){
    	System.out.println("Callback action called....");
		
		String code = request.getParameter("code");
		log.info("Callback action: authCode:" + code);
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// Exchange auth code for access and refresh tokens
		GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeFlow(
				transport, jsonFactory, LinMobileVariables.FUSION_TABLE_CLIENT_ID,
				LinMobileVariables.FUSION_TABLE_CLIENT_SECRET,
				Arrays.asList(LinMobileConstants.FUSION_TABLE_SCOPE))
		        .newTokenRequest(code)
				.setRedirectUri(LinMobileVariables.FUSION_TABLE_CALLBACK_URL);

		GoogleTokenResponse tokens;
		try {
			tokens = tokenRequest.execute();

			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Entity credentials = new Entity("Credentials", "FusionTable");
			credentials.setProperty("accessToken", tokens.getAccessToken());
			credentials.setProperty("expiresIn", tokens.getExpiresInSeconds());
			credentials.setProperty("refreshToken", tokens.getRefreshToken());
			credentials.setProperty("clientId", LinMobileVariables.FUSION_TABLE_CLIENT_ID);
			credentials.setProperty("clientSecret",
					LinMobileVariables.FUSION_TABLE_CLIENT_SECRET);
			datastore.put(credentials);

			response.sendRedirect("/fusionTableAccess.lin");

		} catch (IOException e) {
			log.severe("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
    }
    
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;		
	}

	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}



}



