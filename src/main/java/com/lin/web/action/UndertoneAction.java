package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

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
import com.lin.web.util.UndertoneReportUtil;
import com.opensymphony.xwork2.Action;

public class UndertoneAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(UndertoneAction.class.getName());
	
	
	private HttpServletRequest request;	
	private Map session;
	
	private String reportsResponse;
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	private String startDate;
	private String endDate;
	private String finalise;
	
	private String error;
	
	String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		log.info("UnderoneReports action, linCSVFileName:"+linCSVFileName);	
		reportsResponse="None- blank action";
		return Action.SUCCESS;
	}
	
	public String uploadUndertoneReport(){		
		log.info("Undertone report action...");
		return Action.SUCCESS;
	}
	
	
	public String dailyReport(){		
		
		log.info("undertoneDailyLinMediaReport action starts.."+startDate+" and endDate:"+endDate+" and finalise:"+finalise);
		if(startDate ==null || endDate == null){			
			error="Invalid dates..please provide both dates in yyyy-MM-dd format only.";
		}else if(!DateUtil.isDateFormatYYYYMMDD(startDate)){
			error="Invalid start date..please provide both dates in yyyy-MM-dd format only.";
		}else if(!DateUtil.isDateFormatYYYYMMDD(endDate)){
			error="Invalid end date..please provide both dates in yyyy-MM-dd format only.";
		}else if(finalise ==null || finalise.equals("-1")){
			error="Please select your table status, finalise or nonfinalise.";			
		}else{							
			if(linCSVFileName !=null){
				if(finalise.equals("0")){
					reportsResponse=runNonFinaliseReport(linCSV, startDate, endDate);
				}else if(finalise.equals("1")){
					reportsResponse=updateMonthwiseFinaliseReport(linCSV, startDate, endDate);
				}else{
					error="Invalid finalise value :"+finalise;
					reportsResponse=error;
				}			    
			    request.setAttribute("status", reportsResponse);
			}else{
				log.info("No csv file found in request....");
				error="No csv file found to upload.";
				request.setAttribute("status", error);
			}
		}		
		if(error !=null){
			reportsResponse=reportsResponse+" \n error:"+error;
		}
		log.info("action ends, reportsResponse:"+reportsResponse);
		
		return Action.SUCCESS;
	}
			
	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(String csvFile, String startDateStr, String endDateStr){		
			   
	    String start=startDateStr;		
		String end=endDateStr;
		String response=null;
		log.info("Upload finalise data.. start:"+start+" and end:"+end);		
	   
        try {    	
        	String dataSource=LinMobileConstants.UNDERTONE_DATA_SOURCE;        	
        	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;		   
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			
				        
	        String readyToMergeTableId=schemaName+"_"+dataSource+"_"+startDate.replaceAll("-", "_");
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
			log.severe(" Exception :"+e.getMessage());
			response=e.getMessage();
			e.printStackTrace();
		 }       	
	     return response;
    }
	
	
 private String runNonFinaliseReport(String csvFileName,String startDate,String endDate){
    	
    	IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    	
    	String dataSource=LinMobileConstants.UNDERTONE_DATA_SOURCE;
    	
    	String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
    	String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
    	String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
    	String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
		String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
		String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;			    	
    	String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
    	String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
		 
    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		
		
    	String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.UNDERTONE_REPORTS_BUCKET+
    			   "/non-finalise/"+month;
    	
    	String rawFileName=timestamp+"_"+dataSource+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";
    	String procFileName=rawFileName.replace("_raw", "_proc");
    	String response=UndertoneReportUtil.uploadUndertoneRawCSVReportOnCloudStorage(csvFileName,rawFileName,
    			dirName,bucketName);
		log.info("Raw undertone report path:"+response);
		
		if(response !=null){			
			response=service.generateFinaliseUndertoneReport(rawFileName,procFileName, dirName,bucketName);
			log.info("Proccessed  undertone report path:"+response);			
			if(response !=null && response.trim().length()>0){				
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
					
		}else{
			response="Undertone report failed to upload at cloud storage. Please check application log.";			
		}
		
    	return response;
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

	public void setLinCSV(String linCSV) {
		this.linCSV = linCSV;
	}

	public String getLinCSV() {
		return linCSV;
	}

	public void setLinCSVContentType(String linCSVContentType) {
		this.linCSVContentType = linCSVContentType;
	}

	public String getLinCSVContentType() {
		return linCSVContentType;
	}

	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFinalise() {
		return finalise;
	}

	public void setFinalise(String finalise) {
		this.finalise = finalise;
	}
}
