package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

public class LinMobileDFPAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(LinMobileDFPAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		log.info("LinMobileDFP action executes..");			
	    return Action.SUCCESS;
	}
	
	/*
	 * DailyReoport for LinMobile non-finalised data
	 */
	public String dailyReport(){		
		log.info("dailyLinMobileReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String accountId=request.getParameter("accountId");
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
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseReport(startDate, endDate,accountId,orderId);	     
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
		log.info("dfpNonFinaliseLinMobileReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.LIN_MOBILE_PUBLISHER_ID);
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
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
			}
			i++;
		}
		log.info("action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
  }
	
	/*
	 * update monthly finalise table for LinMobile non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("dfpFinaliseLinMobileReport action executes..");		
	    
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
			    String publisherId=LinMobileConstants.LIN_MOBILE_PUBLISHER_ID;
			    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
			    String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
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
	    
		  log.info("action ends..");	
	      return Action.SUCCESS;
	}
	
	private String runNonFinaliseReport(String startDate,String endDate,String accountId,String orderId){
    	String response="";
    	try {
    		String networkCode=LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE;
	    	String publisherId=LinMobileConstants.LIN_MOBILE_PUBLISHER_ID;
	    	String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
	    	
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	    	
	    	String csvData=getDFPReportURL(service,startDate, endDate,accountId,orderId);	    		    	
	    	
	    	
	    	if(csvData !=null){
	    		
	    		String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.LIN_MOBILE_CLOUD_STORAGE_BUCKET;
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				
				String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
				
				
				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
				
	    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"
			   	                  +LinMobileConstants.LIN_MOBILE_DFP_REPORT_BUCKET+"/non_finalise/"
			   	                  +month;
	    		
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+reportPathOnStorage);	    		
	    		
	    		response=service.generateFinilisedDFPReport(rawFileName,dirName,networkCode,
	    				publisherId,LinMobileConstants.LIN_MOBILE_PUBLISHER_NAME,bucketName);
	    		
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
	
	@SuppressWarnings("deprecation")
	private String getDFPReportURL(IReportService service,String startDate,String endDate,String accountId, 
			String orderId) throws Exception{
		
    	log.info(" now going to build dfpSession ...");
		dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE,
											LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME);
		
		log.info(" getting DfpServices instance from properties...");
		dfpServices = LinMobileProperties.getInstance().getDfpServices();
	    
		boolean isOrder=false;
		
		List<String> accountIdList = new ArrayList<String>();
		List<String> orderIdList = null;
	    if(accountId !=null){
	    	accountIdList.add(accountId);
	    }else if(orderId !=null){
	    	isOrder=true;
	    	orderIdList=new ArrayList<String>();
	    	orderIdList.add(orderId);
	    }else{
	    	accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.LIN_MOBILE_PUBLISHER_NAME);
	    }
		IDFPReportService dfpReportService=new DFPReportService();
		
		String downloadUrl = null;
		if(isOrder){
			log.info("Get dfp report by orderIds...");
			downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
		}else{
			log.info("Get dfp report by accountIds...");
			downloadUrl=dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession, startDate, endDate, accountIdList);
		}	
			
		log.info("DFP report downloadUrl:"+downloadUrl);
    	String csvData=dfpReportService.readCSVGZFile(downloadUrl);
    	return csvData;
	}
	
	public String dailyUpdateAdUnits(){
		
    	log.info("Going to build dfpSession ...");
		try {
			dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE,
					LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IDFPReportService dfpReport=new DFPReportService();
			List<AdUnitHierarchy> adUnitHierarchyList= dfpReport.getRecentlyUpdatedAdUnitsHierarchy(dfpServices, dfpSession);
			if(adUnitHierarchyList !=null){
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				productService.saveAdUnitHierarchy(adUnitHierarchyList);
			}			
			
		}  catch (ValidationException e) {
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
	
	
	
	/*  ******************LIN Mobile new DFP data uploader jobs ******************************* */
	
	/*
	 * LinMobile instance daily dfp report action
	 */
	public String dailyLinMobileNewReport(){		
		log.info("dailyLinMobileReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String accountId=request.getParameter("accountId");
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
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseLinMobileNewReport(startDate, endDate,accountId,orderId);	     
	    }else{
			   reportsResponse="Invalid dates...";
			   log.info(reportsResponse);
		}	
	    return Action.SUCCESS;
	}	
	
	
	/*
	 * This action will reload all non-Finalise tables again once in a day for LinMobile instance
	 */
	public String updateDailyNonFinaliseLinMobileNewReport(){
		log.info("dfpNonFinaliseLinMobileReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		String networkCode=LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE;  
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+networkCode+"_"+startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.LIN_MOBILE_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);					
					reportsResponse=runNonFinaliseLinMobileNewReport(startDate, endDate,null,null);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
			}
			i++;
		}
		log.info("action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
  }
	
	/*
	 * update monthly finalise table for LinMobile instance non-finalised data
	 */
	public String updateMonthwiseFinaliseLinMobileNewReport(){		
		log.info("dfpFinaliseLinMobileReport action executes..");		
	    
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
		    	String networkCode=LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE;  
		    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
			    String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
			    String publisherId=LinMobileConstants.LIN_MOBILE_PUBLISHER_ID;
			    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
			    String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;				
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				
				String readyToMergeTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+networkCode+"_"+startDate.replaceAll("-", "_");
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
	    						bigQueryDTO,readyToMergeTableId);
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
	    
		  log.info("action ends..");	
	      return Action.SUCCESS;
	}
	
	private String runNonFinaliseLinMobileNewReport(String startDate,String endDate,String accountId,String orderId){
    	String response="";
    	try {
	    	  
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	    	
	    	String csvData=makeDFPReportURL(service,startDate, endDate,accountId,orderId);	    		    	
	    	String networkCode=LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE;
	    	String publisherId=LinMobileConstants.LIN_MOBILE_PUBLISHER_ID;
	    	String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
	    	
	    	if(csvData !=null){
	    		
	    		String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.LIN_MOBILE_CLOUD_STORAGE_BUCKET;
				
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				
				
				String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+networkCode+"_"+startDate.replaceAll("-", "_");
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				
				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
				
	    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"
   	                  +LinMobileConstants.LIN_MOBILE_DFP_REPORT_BUCKET+"/non_finalise_"+networkCode+"/"
   	                  +month;
	    		
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+reportPathOnStorage);	    		
	    		
	    		response=service.generateFinilisedDFPReport(rawFileName,dirName,networkCode,
	    				publisherId,LinMobileConstants.LIN_MOBILE_PUBLISHER_NAME,bucketName);
	    		
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
		
	private String makeDFPReportURL(IReportService service,String startDate,String endDate,String accountId, 
			String orderId) throws GeneralSecurityException, IOException, ValidationException, Exception, InterruptedException {
		String linMobileNewDFPNetworkCode=LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE;
		        
        log.info(" now going to build dfpSession ...");
        // Construct a DfpSession.
        dfpSession =DFPAuthenticationUtil.getDFPSession(linMobileNewDFPNetworkCode, 
        		LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME);
        //DfpServices dfpServices = new DfpServices();
 
		log.info(" getting DfpServices instance from properties...");
		dfpServices = LinMobileProperties.getInstance().getDfpServices();
	    
		boolean isOrder=false;
		
		List<String> accountIdList = new ArrayList<String>();
		List<String> orderIdList = null;
	    if(accountId !=null){
	    	accountIdList.add(accountId);
	    }else if(orderId !=null){
	    	isOrder=true;
	    	orderIdList=new ArrayList<String>();
	    	orderIdList.add(orderId);
	    }else{
	    	isOrder=true;
	    	log.info("Load orders from datastore..");
	    	ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
	    	orderIdList=campaignPlannerService.loadAllOrderIdsFromDatastore(linMobileNewDFPNetworkCode);
	    }
		IDFPReportService dfpReportService=new DFPReportService();
		
		String downloadUrl = null;
		if(isOrder){
			log.info("Get dfp report by orderIds...");
			downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
		}else{
			log.info("Get dfp report by accountIds...");
			downloadUrl=dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession, startDate, endDate, accountIdList);
		}	
			
		log.info("DFP report downloadUrl:"+downloadUrl);
    	String csvData=dfpReportService.readCSVGZFile(downloadUrl);
    	return csvData;
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}



}


