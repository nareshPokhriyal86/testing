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
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.PropertyChildObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

/*
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 * 
 * DFP action which have all method to download 
 * report from DFP API for CorePerformance
 */
public class DFPAction implements ServletRequestAware,ServletResponseAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(DFPAction.class.getName());
	
	private String reportsResponse;	

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		return Action.SUCCESS;
	}
	
	public String dailyLinMediaReport(){		
		log.info("dailyLinMediaReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
		String adUnitId=request.getParameter("adUnitId");
		String orderId=request.getParameter("orderId");		
		
		if(startDate !=null && !DateUtil.isDateFormatYYYYMMDD(startDate)){
			reportsResponse="Invalid startDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		if(endDate !=null && !DateUtil.isDateFormatYYYYMMDD(endDate)){
			reportsResponse="Invalid endDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		
		if(startDate ==null && endDate==null){			
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); //DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseReport(startDate, endDate,adUnitId,orderId);
	   }else{
		   reportsResponse="Invalid dates...";
		   log.info(reportsResponse);
	   }
	   return Action.SUCCESS;
	}	
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseReport(){
		log.info("dfpNonFinaliseLinMediaReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);					
					reportsResponse=runNonFinaliseReport(startDate, endDate,null,null);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				reportsResponse="This table id not found in datastore :"+nonFinaliseTableId;				
			}
			i++;
		}
		log.info("dfpNonFinaliseLinMediaReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}
	
    private String runNonFinaliseReport(String startDate,String endDate,String adUnitId,String orderId){
    	String response="";
    	try {
    		String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
	    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
	    	String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
	    	
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	    	
	    	String csvData=getDFPReportURL(startDate, endDate,adUnitId,orderId);
	    	
	    	
	    	if(csvData !=null){
	    		String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;		    	
		    	String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
		    	String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
	    		 
		    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv"; 
	    		
	    	    String dirName=LinMobileConstants.REPORT_FOLDER+"/"
	    	                  +LinMobileConstants.DFP_LIN_MEDIA_REPORT_BUCKET+"/non_finalise/"
	    	                  +month;
	    	    
	    	    String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+reportPathOnStorage);	    		
	    		
	    		String tableId=schemaName+"_"+dataSource+"_"+startDate.replaceAll("-", "_");
	    		QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
	    		
	    		response=service.generateFinilisedDFPReport(rawFileName,dirName,
	    				networkCode,null,null,bucketName);
	    		
	    		String processedFilePath=response;
	    		
    			String id=publisherId+":"+tableId;	
	    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
	    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
	    				projectId, dataSetId, 0);			    		
	    		
	    		String bqResponse=service.uploadProcessFileAtBQ(reportObject, 0, response, bigQueryDTO,null);
    			response=response+" and bigQuery response:: "+bqResponse;
	    		
	    	}else{
	    		log.warning("Failed to create report, please check log ....");
	    		response="Failed to create report";
	    	}	
		 } catch (ValidationException e) {
			log.severe("DFP session exception: ValidationException :"+e.getMessage());
			response=e.getMessage();
			e.printStackTrace();
		 } catch (Exception e) {
			log.severe("DFP report exception: Exception :"+e.getMessage());
			response=e.getMessage();
			e.printStackTrace();
		 }
    	return response;
	}
	

	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("updateMonthwiseFinaliseLinMediaReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
				
		if(startDate !=null && !DateUtil.isDateFormatYYYYMMDD(startDate)){
			reportsResponse="Invalid startDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		if(endDate !=null && !DateUtil.isDateFormatYYYYMMDD(endDate)){
			reportsResponse="Invalid endDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		
		if(startDate ==null && endDate==null){
			Date currentDate=new Date();
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");
			endDate=startDate;
		}else if(startDate !=null && endDate !=null){
			String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			long days=DateUtil.getDifferneceBetweenTwoDates(endDate,currentDate, "yyyy-MM-dd");
			if(days < LinMobileConstants.CHANGE_WINDOW_SIZE){
				dateCheck=false;
				reportsResponse="Invalid end date, you can not merge the data within change window days. Change Widow size:"+
				    LinMobileConstants.CHANGE_WINDOW_SIZE;
			}
			
		}		
		
		
	    if(dateCheck){
	      try {
	    	 
    	    String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
		    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			
				        
	        String readyToMergeTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
	        String finaliseTableId=schemaName+"_"+month.replaceAll("-", "_");
			
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
	        
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
    				
    				FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, finaliseTableId, finalise, startDate, endDate, 
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
			log.severe("DFP report exception: Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 }
	   }	
	   return Action.SUCCESS;
	}
	
	
	@SuppressWarnings("deprecation")
	private String getDFPReportURL(String startDate,String endDate,String adUnitId,String orderId) throws Exception{
		
    	log.info(" now going to build dfpSession ...");
    	String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
		dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
				LinMobileConstants.DFP_APPLICATION_NAME);
		log.info(" getting DfpServices instance from properties...");
		dfpServices = LinMobileProperties.getInstance().getDfpServices();
		
		
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<String> adUnitIds = new ArrayList<String>();
		List<String> orderIdList = new ArrayList<String>();
		boolean isOrder=false;
		if(adUnitId !=null ){
			adUnitIds.add(adUnitId);
		}else if(orderId !=null){
			orderIdList.add(orderId);
			isOrder=true;
		}else{
			List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
			if(propertyObjList == null || propertyObjList.size() == 0){
				reportsResponse="No sites found for Lin Media in datastore, please check AdUnitObj entity:";					
				throw new Exception(reportsResponse); 
			}else{
				log.info("propertyObjList : "+propertyObjList.size());
			}
			for (PropertyObj propertyObj : propertyObjList) {
				if(propertyObj != null && propertyObj.getChilds() != null) {
					List<PropertyChildObj> childObjList = propertyObj.getChilds();
					//log.info("childObjList : "+childObjList.size());
					for(PropertyChildObj childObj : childObjList) {
						if(!adUnitIds.contains(childObj.getChildId())) {
							adUnitIds.add(childObj.getChildId());
						}
					}
				}else{
					log.info("childObjList : Null : propertyObj: "+propertyObj.toString());
				}
			}
		}
		log.info("Finally : adUnitIds :"+adUnitIds);
		String downloadUrl=null;
		IDFPReportService dfpReportService=new DFPReportService();
		if(isOrder){
			downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
		}else{
			downloadUrl=dfpReportService.getDFPReportByAdUnitIds(dfpServices, dfpSession,startDate, 
					endDate, adUnitIds);
		}
				
		
		log.info("DFP report downloadUrl:"+downloadUrl);
    	String csvData=dfpReportService.readCSVGZFile(downloadUrl);
    	return csvData;
	}
		
	public String dailyUpdateAdUnits(){
		log.info("dailyUpdateAdUnits......");
		
    	log.info(" now going to build dfpSession ...");
		try {
			String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
					LinMobileConstants.DFP_APPLICATION_NAME);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IDFPReportService dfpReport=new DFPReportService();
			List<AdUnitHierarchy> adUnitHierarchyList= dfpReport.getRecentlyUpdatedAdUnitsHierarchy(dfpServices, dfpSession);
			if(adUnitHierarchyList !=null){
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				productService.saveAdUnitHierarchy(adUnitHierarchyList);
			}			
			
		} catch (ValidationException e) {
			log.severe("ValidationException :"+e.getMessage());
		} catch (GeneralSecurityException e) {
			log.severe("GeneralSecurityException :"+e.getMessage());
		} catch (IOException e) {
			log.severe("IOException :"+e.getMessage());
		} catch (Exception e) {
		   log.severe("ApiException_Exception :"+e.getMessage());			
		}
		
		return Action.SUCCESS;
	}
	
	public String authorizeUsingOauth2(){
    	try {
   	      Entity credentials = null;   	     
   	      DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
   	      Key creds_key = KeyFactory.createKey("Credentials", "DFPCredentials");
   	      credentials = ds.get(creds_key);
   	      
   	      log.info("found credential in datastore, going to adExReport..");
  	      try {
			 response.sendRedirect("/dfpReports.lin");
		  } catch (IOException e) {
			 log.severe("IOException: "+e.getMessage());
			 e.printStackTrace();
		  }
		
   	    } catch (EntityNotFoundException ex) {
   	      log.warning("No credentials availble, create new...");      
   	    
   	      GoogleAuthorizationCodeRequestUrl requestUrl = 
   	        new GoogleAuthorizationCodeRequestUrl(
   	        		LinMobileVariables.AD_EXCHANGE_CLIENT_ID, 
   	        		LinMobileVariables.AD_EXCHANGE_CALLBACK_URL, 
   	        		Arrays.asList(LinMobileConstants.DFP_API_SCOPE));
   	      
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
    	System.out.println("DFP Callback action called....");
		
		String code = request.getParameter("code");
		log.info("Callback action: authCode:" + code);
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// Exchange auth code for access and refresh tokens
		GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeFlow(
				transport, jsonFactory, LinMobileVariables.AD_EXCHANGE_CLIENT_ID,
				LinMobileVariables.AD_EXCHANGE_CLIENT_SECRET,
				Arrays.asList(LinMobileConstants.DFP_API_SCOPE))
		        .newTokenRequest(code)
				.setRedirectUri(LinMobileVariables.AD_EXCHANGE_CALLBACK_URL);

		GoogleTokenResponse tokens;
		try {
			tokens = tokenRequest.execute();

			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Entity credentials = new Entity("Credentials", "DFPCredentials");
			credentials.setProperty("accessToken", tokens.getAccessToken());
			credentials.setProperty("expiresIn", tokens.getExpiresInSeconds());
			credentials.setProperty("refreshToken", tokens.getRefreshToken());
			credentials.setProperty("clientId", LinMobileConstants.CLIENT_ID);
			credentials.setProperty("clientSecret",
					LinMobileConstants.CLIENT_SECRET);
			datastore.put(credentials);

			response.sendRedirect("/dfpReports.lin");

		} catch (IOException e) {
			log.severe("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
    }
   
	@Override
	public void setSession(Map session) {
		this.session=session;
		
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


}


