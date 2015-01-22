package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.UndertoneReportUtil;
import com.opensymphony.xwork2.Action;

public class SellThroughReportAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(SellThroughReportAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	private String publisherId;
	private String error;	
	private String finalise;
	
	String schemaName="Sell_Through";
	
	public String execute(){
		log.info("linCSVFileName:"+linCSVFileName);
		String message="";
		String subject="";
		String networkCode=null;
		boolean uploadReport=false;
		if(linCSVFileName !=null){
			String publisher="";
			String bucketName="";
			if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)){
				networkCode=LinMobileConstants.DFP_NETWORK_CODE;
				bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
				uploadReport=true;
				publisher="LinMedia";
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)){
				networkCode=LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE;
				bucketName=LinMobileVariables.LIN_DIGITAL_CLOUD_STORAGE_BUCKET;
				uploadReport=true;
				publisher="LinDigital";
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)){
				networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
				bucketName=LinMobileVariables.TRIBUNE_CLOUD_STORAGE_BUCKET;
				uploadReport=true;
				publisher="Tribune";
			}else{
				error="Invalid publisher.";
				request.setAttribute("status", error);
			}
			
			if(uploadReport){
				String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
		    	String rawFileName=timestamp+"_"+publisher+"_SellThroughReport_"+"raw"+".csv";
		    	
	    	    String dirName=LinMobileConstants.REPORT_FOLDER+"/"+
		    	        LinMobileConstants.SELL_THROUGH_REPORTS_BUCKET+"/"+publisher+"/"+
	    	    		DateUtil.getCurrentTimeStamp("yyyy_MM");
	    	    
	    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName,bucketName);
	    		log.info("Raw SellThrough report path:"+reportsResponse);
	    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
	    		if(reportsResponse !=null){
	    			String procFileName=rawFileName.replace("_raw", "_proc");
	    			reportsResponse=service.generateSellThroughReport(rawFileName,procFileName, dirName,networkCode,bucketName);
	    			
	    			log.info("Proccessed  SellThrough report path:"+reportsResponse);
	    		
	    			if(reportsResponse !=null){
	    				//String bigQueryResponse=uploadDataOnBigQuery(reportsResponse);
	    				String schemaFile=LinMobileConstants.SELL_THROUGH_TABLE_SCHEMA;
	    				String tableId=LinMobileConstants.SELL_THROUGH_TABLE_ID;
	    				String bigQueryResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse, schemaFile, tableId);
	    				reportsResponse="Cloud Storage path:"+reportsResponse+" And bigQueryResponse:"+bigQueryResponse;
	    										    			
	    			}    			
	    		}else{	    			
	    			reportsResponse="SellThrough report failed to upload at cloud storage.\nPlease see the response:"+reportsResponse;    		
	    		}	    		
	    		request.setAttribute("status", reportsResponse);
			}
			
		}else{
			error="No csv file found to upload.";
			request.setAttribute("status", error);
		}
		return Action.SUCCESS;
	}
	
		
	public String finaliseOrNonFinalise(){
		log.info("linCSVFileName:"+linCSVFileName+" and finalise:"+finalise);
		String publisher="";
		if(linCSVFileName !=null){
			
			
			if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)){				
				publisher="LinMedia";
				dailyLinMediaReport();
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)){				
				publisher="LinDigital";
				request.setAttribute("status", "This action is not configured for publisher:"+publisher);
			}else if(publisherId !=null && publisherId.equals(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)){				
				publisher="Tribune";
				dailyTribuneReport();				
			}else{
				error="Invalid publisher.";
				request.setAttribute("status", error);
			}			
			
		}else{			
			error="No csv file found to upload.";
			request.setAttribute("status", error);
		}
		return Action.SUCCESS;
	}
	
	public String dailyLinMediaReport(){	
	    reportsResponse=runNonFinaliseLinMediaReport();
	    return reportsResponse;
	}
	
	public String dailyTribuneReport(){	
	    reportsResponse=runNonFinaliseTribuneReport();
	    return reportsResponse;
	}
	
	
	private String runNonFinaliseLinMediaReport(){
		    String startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); 
			String endDate=startDate;
			
	    	String response="";
	    	try {	
	    		String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
	    		String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
	    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;	    			
	    			
		    	if(linCSV !=null){
	    		    
					String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
					String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
					String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
					String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
					String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
					
					String tableId=schemaName ;//+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
					String schemaFile=LinMobileConstants.SELL_THROUGH_TABLE_SCHEMA;
					
					QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
							projectId, schemaFile, tableId, dataSetId, schemaName);
					
		    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
			    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
		    		String processFile=rawFileName.replace("_raw", "_proc");
		    		
		    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"+
				    	        LinMobileConstants.SELL_THROUGH_REPORTS_BUCKET+
				    	        "/LinMedia/non_finalise/"+ month;
		    		
		    		//String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(linCSV, rawFileName,dirName);
		    		response=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName,bucketName);
		    		log.info("Report saved :"+response);	 
		    		
		    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		    		if(response !=null){		    			
		    			response=service.generateSellThroughReport(rawFileName,processFile, dirName,networkCode,bucketName);
		    			log.info("Proccessed  SellThrough report path:"+reportsResponse);
			    		
			    		String processedFilePath=response;
			    		
		    			String id=publisherId+":"+tableId;	
			    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 1, startDate, endDate, 
			    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
			    				projectId, dataSetId, 1);			    		
			    		
			    		String bqResponse=service.uploadProcessFileAtBQ(reportObject, 1, response, bigQueryDTO,null);
			    		
		    			response=response+" and bigQuery response:: "+bqResponse;						    			
		    		}else{		    			
			    		response="Failed to upload raw report on cloud storage";
			    		log.warning(response);
		    		}
		    					    		
		    	}else{
		    		log.warning("Failed to create report, please check log ....");
		    		response="Failed to create report";
		    	}	
			} catch (Exception e) {
				log.severe("DFP report exception: Exception :"+e.getMessage());
				response=e.getMessage();
				
			 }
	    	return response;
	}
	 
	private String runNonFinaliseTribuneReport(){
	    String startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); 
		String endDate=startDate;
		
    	String response="";
    	try {	
    		String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
    		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;	    			
    			
	    	if(linCSV !=null){
    		    
				String projectId=LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.TRIBUNE_CLOUD_STORAGE_BUCKET;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				
				String tableId=schemaName; //+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
				String schemaFile=LinMobileConstants.SELL_THROUGH_TABLE_SCHEMA;
				
				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
				
	    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
	    		String processFile=rawFileName.replace("_raw", "_proc");
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"+
			    	        LinMobileConstants.SELL_THROUGH_REPORTS_BUCKET+
			    	        "/Tribune/non_finalise/"+ month;
	    			    		
	    		response=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+response);	 
	    		
	    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
	    		if(response !=null){		    			
	    			response=service.generateSellThroughReport(rawFileName,processFile, dirName,networkCode,bucketName);
	    			log.info("Proccessed  SellThrough report path:"+reportsResponse);
		    		
		    		String processedFilePath=response;
		    		
	    			String id=publisherId+":"+tableId;	
		    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 1, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 1);			    		
		    		
		    		String bqResponse=service.uploadProcessFileAtBQ(reportObject, 1, response, bigQueryDTO,null);
		    		
	    			response=response+" and bigQuery response:: "+bqResponse;						    			
	    		}else{		    			
		    		response="Failed to upload raw report on cloud storage";
		    		log.warning(response);
	    		}
	    					    		
	    	}else{
	    		log.warning("Failed to create report, please check log ....");
	    		response="Failed to create report";
	    	}	
		} catch (Exception e) {
			log.severe("DFP report exception: Exception :"+e.getMessage());
			response=e.getMessage();
			
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



	public String getPublisherId() {
		return publisherId;
	}



	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}


	public String getFinalise() {
		return finalise;
	}


	public void setFinalise(String finalise) {
		this.finalise = finalise;
	}

}
