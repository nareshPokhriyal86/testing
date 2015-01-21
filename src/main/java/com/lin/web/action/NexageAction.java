package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.NexageReportUtil;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

public class NexageAction implements ServletRequestAware{

	
	private static final Logger log = Logger.getLogger(NexageAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		log.info("NexageReports action executes..");
		
		String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){
			Date currentDate=new Date();
			start=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			
		}
		if(end == null){
			end=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			//end=start;
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim); 
		
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		reportsResponse= service.generateNexageReport(start, end, dim);
		// example: http://storage.googleapis.com/linmobile/NexageRevenueReport_2013-06-13_2013-06-13.csv
		log.info("reportsResponse:"+reportsResponse);
		
		if(reportsResponse !=null ){
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA;
			String tableId=LinMobileConstants.CORE_PERFORMANCE_TABLE_ID;
			reportsResponse=uploadDataOnBigQuery(reportsResponse,false,schemaFile,tableId);
		}
		
		return Action.SUCCESS;
	}
	
/*	public String reportThroughNexageNewAPI(){		
		log.info("reportThroughNexageAPI action executes..");
		
		String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){
			Date currentDate = new Date();
			start=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			
		}
		if(end == null){
			end=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			//end=start;
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim); 
		
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		reportsResponse= service.generateNewNexageReport(start, end, dim);
		// example: http://storage.googleapis.com/linmobile/NexageRevenueReport_2013-06-13_2013-06-13.csv
		log.info("reportsResponse:"+reportsResponse);
		
		if(reportsResponse !=null ){
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA;
			String tableId=LinMobileConstants.CORE_PERFORMANCE_TABLE_ID;
			reportsResponse=uploadDataOnBigQuery(reportsResponse,false,schemaFile,tableId);
		}
		
		return Action.SUCCESS;
	}*/
	
	/*
	 * This action will run every hour to update current date's data for non-finalise
	 * from nexgae data source
	 */
	public String dailyReport(){		
		log.info("NexageDailyReports action executes..");
		
		String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){			
			start=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		}
		if(end == null){			
			end=start;			
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim); 
		
		reportsResponse=runNonFinaliseReport(start, end, dim);
		log.info("action ends, reportsResponse:"+reportsResponse);
		
		return Action.SUCCESS;
	}
	
/*	public String dailyReportNewAPI(){		
		log.info("NexageDailyReports action executes..");
		
		String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){			
			start=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		}
		if(end == null){			
			end=start;			
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim); 
		
		reportsResponse=runNonFinaliseReportNewAPI(start, end, dim);
		log.info("action ends, reportsResponse:"+reportsResponse);
		
		return Action.SUCCESS;
	}*/
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseReport(){
		log.info("nexageNonFinaliseLinMediaReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_"+
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
					String dim=null;
					reportsResponse=runNonFinaliseReport(startDate, endDate, dim);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
				log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);
				String dim=null;
				reportsResponse=runNonFinaliseReport(startDate, endDate, dim);
				log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
			}
			i++;
		}
		log.info("nexageNonFinaliseLinMediaReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}

	/*public String updateDailyNonFinaliseReportNewAPI(){
		log.info("nexageNonFinaliseLinMediaReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_"+
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
					String dim=null;
					reportsResponse=runNonFinaliseReportNewAPI(startDate, endDate, dim);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
				log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);
				String dim=null;
				reportsResponse=runNonFinaliseReportNewAPI(startDate, endDate, dim);
				log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
			}
			i++;
		}
		log.info("nexageNonFinaliseLinMediaReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}*/
	
	
	private String runNonFinaliseReport(String startDate,String endDate, String dim){
    	    	
    	IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    	String rawFileName=timestamp+"_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_"+schemaName+"_"+
    						startDate+"_"+endDate+"_raw"+".csv";   
    	
    	String dataSource=LinMobileConstants.NEXAGE_CHANNEL_NAME;
    	String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
    	String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
		String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
		String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;			    	
    	String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
    	String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
    	
    	
    	String dirName=LinMobileConstants.REPORT_FOLDER+"/"
                +LinMobileConstants.NEXAGE_REPORTS_BUCKET+"/non-finalise/"
                +month;

    	String response= service.generateFinalisedNexageReport(startDate, endDate, dim,dirName,rawFileName,bucketName);		
		log.info("response:"+response);
		if(response !=null){
			String tableId=schemaName+"_"+dataSource+"_"+startDate.replaceAll("-", "_");		
			
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
    	return response;
	}
	
	/*private String runNonFinaliseReportNewAPI(String startDate,String endDate, String dim){
    	
    	IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    	String rawFileName=timestamp+"_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_CorePerformance_"+
    						startDate+"_"+endDate+"_raw"+".csv";   
    	String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
    	
    	String dirName=LinMobileConstants.REPORT_FOLDER+"/"
                +LinMobileConstants.NEXAGE_REPORTS_BUCKET+"/non-finalise/"
                +month;

    	String response= service.generateFinalisedNexageReportNewAPI(startDate, endDate, dim,dirName,rawFileName,bucketName);		
		log.info("response:"+response);
		if(response !=null){
					
			String tableId="CorePerformance_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_"+startDate.replaceAll("-", "_");
			 
			String processedFilePath=response;
			String bqResponse=service.uploadProcessFileAtBQ(tableId, response, startDate, endDate, timestamp, 
					processedFilePath,
					LinMobileConstants.NEXAGE_CHANNEL_NAME, 0, 
					LinMobileConstants.LIN_MEDIA_PUBLISHER_ID, LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME,
					LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS,
					LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY, 
					LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID,null);	    			    		
			
			response=response+" and bigQuery response:"+bqResponse;
		}else{			
			response="No processed report found to upload at bigquery. response:"+response;			
		}
    	return response;
	}*/
	
	
	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("nexageFinaliseLinMediaReport action executes..");	
	   
	    String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){
			Date currentDate=new Date();
			start=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");			
		}		
		if(end == null){
			end=start;
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim);		
	   
        try {   
        	
        	String dataSource=LinMobileConstants.NEXAGE_CHANNEL_NAME;        	
        	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(start, "yyyy-MM-dd", "yyyy_MM");
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;		   
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			
				        
	        String readyToMergeTableId=schemaName+"_"+dataSource+"_"+start.replaceAll("-", "_");
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
			log.severe("Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 }
	  	
         log.info("nexageFinaliseLinMediaReport action ends..reportsResponse:"+reportsResponse);	
	     return Action.SUCCESS;
  }
	
	/*public String updateMonthwiseFinaliseReportNewAPI(){		
		log.info("nexageFinaliseLinMediaReport action executes..");	
	   
	    String start=request.getParameter("start");		
		String end=request.getParameter("end");
		String dim=request.getParameter("dim");
		
		if(start ==null){
			Date currentDate=new Date();
			start=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");			
		}		
		if(end == null){
			end=start;
		}
		log.info("start:"+start+" and end:"+end+" and dim:"+dim);		
	   
        try {    	  
	    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(start, "yyyy-MM-dd", "yyyy_MM");
		    	
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	        
	        String readyToMergeTableId="CorePerformance_"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"_"+start.replaceAll("-", "_");
	        String finaliseTableId="CorePerformance_"+month.replaceAll("-", "_");
	        
	        FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(readyToMergeTableId,
	        		LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);	   
	        
	        if(tableObj !=null){
	        	log.info("Loaded data : "+tableObj.toString());
	        	String processedFilePath= tableObj.getProcessedFilePath();	        
	    		if(tableObj.getMergeStatus()==1){
	    			log.warning("This table has already been merged to finalised table..");
		    		reportsResponse="This table has already been merged to finalised table..readyToMergeTableId:"+readyToMergeTableId;
	    		}else{
	    			log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
		    		reportsResponse=service.uploadProcessFileAtBQ(finaliseTableId,processedFilePath, start, end, timestamp, 
		    				processedFilePath, LinMobileConstants.NEXAGE_CHANNEL_NAME, 1, 
		    				LinMobileConstants.LIN_MEDIA_PUBLISHER_ID, LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME,
		    				LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS,
		    				LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY, 
		    				LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID,null);
		    		
		    		log.info("This table has been merged to finalised table successfully..");
	    		}	    		
	    		
	    	}else{
	    		log.warning("Failed to find table for merging.."+readyToMergeTableId);
	    		reportsResponse="Failed to find table for merging..readyToMergeTableId:"+readyToMergeTableId;
	    	}	
		 } catch (Exception e) {
			log.severe("Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 }
	  	
         log.info("nexageFinaliseLinMediaReport action ends..reportsResponse:"+reportsResponse);	
	     return Action.SUCCESS;
  }*/
	
	private String uploadDataOnBigQuery(String responseURL,boolean dailyReport,String schemaFile,String tableId){
		String bigQueryResponse=null;
		String message="";
		String subject="";
		if(responseURL !=null && responseURL.contains(".csv")){
			try {				
				log.info("Before saving data in bigquery- tableId:"+tableId);
				if(dailyReport){					
					bigQueryResponse=BigQueryUtil.truncateAndSaveData(responseURL, schemaFile, tableId);
				}else{
					bigQueryResponse=BigQueryUtil.saveData(responseURL, schemaFile, tableId);
				}
				
				log.info("bigQueryResponse:"+bigQueryResponse);
				responseURL="Cloud Storage path:"+responseURL+" And BigQueryResponse:"+bigQueryResponse;
				if(bigQueryResponse!=null && bigQueryResponse.equals("Success")){
					message="Nexage data has been uploaded in BigQuery.\n Please see Response:"+responseURL;
					subject="Nexage data uploaded in BigQuery";
					LinMobileUtil.sendMailOnGAE(
							LinMobileVariables.SENDER_EMAIL_ADDRESS, 
							LinMobileConstants.TO_EMAIL_ADDRESS,						
							subject,message);
				}else{
					message="Nexage data failed to upload in BigQuery.\n Please see Response:"+responseURL;
					subject="Nexage data failed to upload in BigQuery";
					LinMobileUtil.sendMailOnGAE(
							LinMobileVariables.SENDER_EMAIL_ADDRESS, 
							LinMobileConstants.TO_EMAIL_ADDRESS,
							LinMobileConstants.CC_EMAIL_ADDRESS, 
							subject,message);
				}				
				
			} catch (GeneralSecurityException  e) {
				log.severe("uploadDataOnBigQuery exception:"+e.getMessage());
				responseURL=responseURL+" And BigQueryResponse:"+e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				log.severe("uploadDataOnBigQuery exception:"+e.getMessage());
				responseURL=responseURL+" And BigQueryResponse:"+e.getMessage();
				e.printStackTrace();
			}
		}else{
			log.severe("Cron job failed : Sending mail...");
			message="Please check the log, NexageReport cron job has been falied to upload data in BigQuery";
			subject="NexageReport Cron job falied";
			LinMobileUtil.sendMailOnGAE(
					LinMobileVariables.SENDER_EMAIL_ADDRESS, 
					LinMobileConstants.TO_EMAIL_ADDRESS,
					LinMobileConstants.CC_EMAIL_ADDRESS, 
					subject,message);
		}	
		LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS,
				LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
		
		
		return responseURL;
	}

	private String restartCronOnFailure(String start,String end,String dim){
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		reportsResponse= service.generateNexageReport(start, end, dim);
		return reportsResponse;
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

