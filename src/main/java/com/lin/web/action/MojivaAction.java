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
import org.apache.struts2.interceptor.SessionAware;

import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.opensymphony.xwork2.Action;

public class MojivaAction implements ServletRequestAware,ServletResponseAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(MojivaAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		log.info("MojivaAction action executes..");
	
		String date=request.getParameter("start");
		String siteId=request.getParameter("siteId");
		String endDate=request.getParameter("end");
		
		if(date ==null && endDate==null){
			Date currentDate=new Date();
			date=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			endDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		}
		
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		
		if(date !=null && endDate !=null ){
			log.info("generateMojivaReport By interval: start:"+date+" and end:"+endDate+" and siteId:"+siteId);
			reportsResponse=service.generateMojivaReport(date,endDate);	
		}else{
			if(date ==null){
				Date currentDate=new Date();
				date=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			}		
			log.info("generateMojivaReportByDay: start:"+date);
			reportsResponse=service.generateMojivaReportByDay(date);	
		}
		
		log.info("reportsResponse:"+reportsResponse);
		String schemaFile=LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA;
		String tableId=LinMobileConstants.CORE_PERFORMANCE_TABLE_ID;
		reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse, schemaFile, tableId);		
		
		return Action.SUCCESS;
	}
	
	
	public String dailyReport(){		
		log.info("MojivaDailyReport action executes..");
	
		String start=request.getParameter("start");
		String siteId=request.getParameter("siteId");
		String endDate=request.getParameter("end");
		
		if(start ==null || endDate==null){
			start=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			endDate=start;
		}
		
		log.info("generateMojivaReport By interval: start:"+start+" and end:"+endDate);	
		reportsResponse=runNonFinaliseReport(start, endDate);
		log.info("action ends, reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseReport(){
		log.info("mojivaNonFinaliseLinMediaReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.MOJIVA_CHANNEL_NAME+"_"+
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
					//String siteId=null;
					reportsResponse=runNonFinaliseReport(startDate, endDate);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
				log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);
				//String siteId=null;
				reportsResponse=runNonFinaliseReport(startDate, endDate);
				log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
			}
			i++;
		}
		log.info("mojivaNonFinaliseLinMediaReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}
	
	
	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("mojivaFinaliseLinMediaReport action executes..");	
	   
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
		    String dataSource=LinMobileConstants.MOJIVA_CHANNEL_NAME;
		    
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;		   
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			
				        
	        String readyToMergeTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+start.replaceAll("-", "_");
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
			log.severe(" Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			
		 }
	  	
         log.info("mojivaFinaliseLinMediaReport action ends..reportsResponse:"+reportsResponse);	
	     return Action.SUCCESS;
   }
	
	private String runNonFinaliseReport(String startDate,String endDate){
    	
    	IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    	String dataSource=LinMobileConstants.MOJIVA_CHANNEL_NAME;
    	
    	String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
    	String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
		String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
		String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;			    	
    	String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
    	String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
    	
    	String rawFileName=timestamp+"_"+dataSource+"_"+schemaName+"_"+
				startDate+"_"+endDate+"_raw"+".csv";  
    	// Raw and proc file are same for mojiva
    	String procFileName=timestamp+"_"+dataSource+"_"+schemaName+"_"+
    						startDate+"_"+endDate+"_proc"+".csv";   
    	String response="";
    			
    	String dirName=LinMobileConstants.REPORT_FOLDER+"/"
                +LinMobileConstants.MOJIVA_REPORTS_BUCKET+"/non-finalise/"
                +month;

    	if(startDate !=null && endDate !=null ){
			log.info("generateMojivaReport By interval: start:"+startDate+" and end:"+endDate);
			response=service.generateFinaliseMojivaReport(startDate,endDate,procFileName,dirName,bucketName);	
		}else{
			if(startDate ==null){
				Date currentDate=new Date();
				startDate=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			}		
			log.info("generateMojivaReportByDay: start:"+startDate);
			response=service.generateFinaliseMojivaReportByDay(startDate,procFileName,dirName,bucketName);	
		}    	
    		
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


