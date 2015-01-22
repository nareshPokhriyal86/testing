package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

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
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.AdExchangeReportUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;


public class AdExchangeAction implements ServletRequestAware,ServletResponseAware{

	
	private static final Logger log = Logger.getLogger(AdExchangeAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String accessToken;
	private String error;
	String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		int count=1;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		log.info("AdExchangeAction action executes..startDate:"+startDate+" and endDate:"+endDate);
		if(startDate ==null){
			Date currentDate=new Date();
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
		}
		if(endDate == null){			
			//endDate=startDate;
			endDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		}
		
		validateAccessToken();
		
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		String fileName=timestamp+"_AdExchange_CorePerformance_"+startDate+"_"+endDate+"_proc"+".csv";
		String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.AD_EXCHANGE_REPORTS_BUCKET+"/"
	              +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		
		String reportsResponseAdEx=AdExchangeReportUtil.getAdExRepors(startDate,endDate,accessToken);
		
		 while( (reportsResponseAdEx ==null || reportsResponseAdEx.trim().length()==0) && count <=3){
		    	log.info("Cron failed to fetch data, restart.."+count);
		    	reportsResponseAdEx=AdExchangeReportUtil.getAdExRepors(startDate,endDate,accessToken);
		    	count++;
		 }
		    
		 if((reportsResponseAdEx ==null || reportsResponseAdEx.trim().length()==0) && count > 3){
		    	sendMail();
		  }else{
			  IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
			  reportsResponse=service.generateAdExReport(reportsResponseAdEx, startDate, fileName,dirName);			  
			  if(reportsResponse !=null && reportsResponse.trim().length()>0){
				  log.info("Going to save data in bigquery from file: reportsResponse:"+reportsResponse);
				  String schemaFile=LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA;
				  String tableId=LinMobileConstants.CORE_PERFORMANCE_TABLE_ID;
				  reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse,schemaFile,tableId);
			  }else{
				  log.info("Nothing to upload on bigquery: reportsResponse:"+reportsResponse);
				  reportsResponse="No data found for this date interval";
				  error="none";
			  }
		  }
		 return Action.SUCCESS;
	}
	
	
	public String dailyReport(){		
	
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		
		if(startDate ==null || endDate == null){			
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			endDate=startDate;
		}
		
		log.info("adExchangeDailyReport action executes..startDate:"+startDate+" and endDate:"+endDate);				
		
		reportsResponse=runNonFinaliseReport(startDate, endDate);
		log.info("action ends, reportsResponse:"+reportsResponse);
		
		return Action.SUCCESS;
	}
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseReport(){
		log.info("adExNonFinaliseLinMediaReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.AD_EXCHANGE_TABLE_NAME+"_"+
			                           startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);
					reportsResponse=runNonFinaliseReport(startDate, endDate);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
				log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);
				reportsResponse=runNonFinaliseReport(startDate, endDate);
				log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
			}
			i++;
		}
		log.info("adExNonFinaliseLinMediaReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}
	
	
	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("adExFinaliseLinMediaReport action executes..");	
	   
	    String start=request.getParameter("start");		
		String end=request.getParameter("end");
				
		if(start ==null){
			Date currentDate=new Date();
			start=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");			
		}		
		if(end == null){
			end=start;
		}
		log.info("start:"+start+" and end:"+end);		
	   
        try {    	  
	    	
		    String dataSource=LinMobileConstants.AD_EXCHANGE_DATA_SOURCE;
		    
		    String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(start, "yyyy-MM-dd", "yyyy_MM");
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;		   
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
	        
	        String readyToMergeTableId=schemaName+"_"+LinMobileConstants.AD_EXCHANGE_TABLE_NAME+"_"+start.replaceAll("-", "_");
	        String finaliseTableId=schemaName+"_"+month.replaceAll("-", "_");
	     	        
	        FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(readyToMergeTableId,
	        		publisherId);	    
	        
	        if(tableObj !=null){
	        	
	        	log.info("Loaded data : "+tableObj.toString());
	        	String processedFilePath= tableObj.getProcessedFilePath();	        
	    		if(tableObj.getMergeStatus()==1){
	    			log.warning("This table has already been merged to finalised table..");
		    		reportsResponse="This table has already been merged to finalised table..readyToMergeTableId:"+readyToMergeTableId;
	    		}else{
	    			log.info("Going to save this finalise data on bigquery: processedFilePaths:"+processedFilePath);
	    			
	    			String id=publisherId+":"+finaliseTableId;
    				int finalise=1;
    				
    				FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, finaliseTableId, finalise, start, end, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);	
    				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
    						projectId, schemaFile, finaliseTableId, dataSetId, schemaName);
    				
    				reportsResponse=service.uploadProcessFileAtBQ(reportObject, finalise, processedFilePath, 
    						bigQueryDTO,null);
    				log.info("This table has been merged to finalised table successfully..: readyToMergeTableId:"+readyToMergeTableId);
		    		
	    		}
	    		
	    	}else{
	    		log.warning("Failed to find table for merging.."+readyToMergeTableId);
	    		reportsResponse="Failed to find table for merging..readyToMergeTableId:"+readyToMergeTableId;
	    	}	
		 } catch (Exception e) {
			log.severe(" Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			
		 }
	  	
         log.info("adExFinaliseLinMediaReport action ends..reportsResponse:"+reportsResponse);	
	     return Action.SUCCESS;
    }
	
	
	private void validateAccessToken(){
		
		Entity credentials = null;
		try {
	    	log.info("Going to fetch credentials...");
	        DatastoreService datastore =DatastoreServiceFactory.getDatastoreService();
	        Key credsKey = KeyFactory.createKey("DFPCredentials", "DFPCredentials");
	        credentials = datastore.get(credsKey);
	      
	        GoogleTokenResponse tokens = new GoogleTokenResponse();
		    tokens.setAccessToken((String) credentials.getProperty("accessToken"));
		    tokens.setExpiresInSeconds((Long) credentials.getProperty("expiresIn"));
		    tokens.setRefreshToken((String) credentials.getProperty("refreshToken"));
		    String clientId = (String) credentials.getProperty("clientId");
		    String clientSecret = (String) credentials.getProperty("clientSecret");
		    tokens.setScope(LinMobileConstants.AD_EXCHANGE_WEBSERVICE_SCOPE);
		    
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
							
		} 
	    
	}
	
		
   private String runNonFinaliseReport(String startDate,String endDate){
    	
    	IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	
    	String dataSource=LinMobileConstants.AD_EXCHANGE_DATA_SOURCE;
    	
    	String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
    	String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
		String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
		String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;			    	
    	String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
    	String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
    	
    	String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.AD_EXCHANGE_REPORTS_BUCKET+"/"
	              +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    	
    	String rawFileName=timestamp+"_"+LinMobileConstants.AD_EXCHANGE_TABLE_NAME+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";
    	String procFileName=rawFileName.replace("_raw", "_proc");
    	
    	
    	String response="";
    	
    	validateAccessToken();
    	
    	String reportsResponseAdEx=AdExchangeReportUtil.getAdExRepors(startDate,endDate,accessToken);
		int count=1;
		while( (reportsResponseAdEx ==null || reportsResponseAdEx.trim().length()==0) && count <=3){
			    count++;
		    	log.info("Cron failed to fetch data, restart.."+count);
		    	reportsResponseAdEx=AdExchangeReportUtil.getAdExRepors(startDate,endDate,accessToken);		    	
		 }
		    
		 if((reportsResponseAdEx ==null || reportsResponseAdEx.trim().length()==0) && count > 3){
		    	sendMail();
		  }else{
			  response=service.generateFinalisedAdExchangeReport(reportsResponseAdEx, startDate, procFileName, 
					  dirName, bucketName);
			  
			  log.info("response:"+response);
			  
			  if(response !=null && response.trim().length()>0){					
					String tableId=schemaName+"_"+LinMobileConstants.AD_EXCHANGE_TABLE_NAME+"_"+startDate.replaceAll("-", "_");
					//String tableId=schemaName+"_"+dataSource+"_"+startDate.replaceAll("-", "_"); 
					// dataSource is not same as AD_EXCHANGE_TABLE_NAME
					
					QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
							projectId, schemaFile, tableId, dataSetId, schemaName);
		    		
		    		String processedFilePath=response;
		    		
	    			String id=publisherId+":"+tableId;	
		    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);			    		
		    		
		    		String bqResponse=service.uploadProcessFileAtBQ(reportObject, 0, response, bigQueryDTO,null);
	    			response=response+" and bigQuery response:: "+bqResponse;
				  			  
			  }else{
				  response="No processed report found to upload at bigquery. response:"+response;
			  }
		}	
    	return response;
	}

		
		
	public void sendMail(){
		String message="Please check the log, AdExchange cron job has been falied.";
		LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "Cron job falied", message);
	}
	
    public String authorizeUsingOauth2(){
    	try {
   	      Entity credentials = null;   	     
   	      DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
   	      Key creds_key = KeyFactory.createKey("DFPCredentials", "DFPCredentials");
   	      credentials = ds.get(creds_key);
   	      
   	      log.info("found credential in datastore, going to adExReport..");
  	      try {
			 response.sendRedirect("/adExReport.lin");
		  } catch (IOException e) {
			 log.severe("IOException: "+e.getMessage());
			 
		  }
		
   	    } catch (EntityNotFoundException ex) {
   	      log.warning("No credentials availble, create new...");      
   	    
   	      GoogleAuthorizationCodeRequestUrl requestUrl = 
   	        new GoogleAuthorizationCodeRequestUrl(
   	        		LinMobileVariables.AD_EXCHANGE_CLIENT_ID, 
   	        		LinMobileVariables.AD_EXCHANGE_CALLBACK_URL, 
   	        		Arrays.asList(LinMobileConstants.DFP_API_SCOPE,LinMobileConstants.AD_EXCHANGE_WEBSERVICE_SCOPE));
   	      
   	      requestUrl.setAccessType("offline");
   	      requestUrl.setApprovalPrompt("force");
   	      System.out.println("before sendRedirect:"+requestUrl.build());
   	      try {
			response.sendRedirect(requestUrl.build());
		  } catch (IOException e) {
			  log.severe("IOException: "+e.getMessage());
			
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
				transport, jsonFactory, LinMobileVariables.AD_EXCHANGE_CLIENT_ID,
				LinMobileVariables.AD_EXCHANGE_CLIENT_SECRET,
				Arrays.asList(LinMobileConstants.DFP_API_SCOPE,LinMobileConstants.AD_EXCHANGE_WEBSERVICE_SCOPE))
		        .newTokenRequest(code)
				.setRedirectUri(LinMobileVariables.AD_EXCHANGE_CALLBACK_URL);

		GoogleTokenResponse tokens;
		try {
			tokens = tokenRequest.execute();

			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Entity credentials = new Entity("DFPCredentials", "DFPCredentials");
			credentials.setProperty("accessToken", tokens.getAccessToken());
			credentials.setProperty("expiresIn", tokens.getExpiresInSeconds());
			credentials.setProperty("refreshToken", tokens.getRefreshToken());
			credentials.setProperty("clientId", LinMobileConstants.CLIENT_ID);
			credentials.setProperty("clientSecret",
					LinMobileConstants.CLIENT_SECRET);
			datastore.put(credentials);

			response.sendRedirect("/adExReport.lin");

		} catch (IOException e) {
			log.severe("IOException: " + e.getMessage());
			
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



