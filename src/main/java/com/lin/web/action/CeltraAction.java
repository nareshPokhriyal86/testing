package com.lin.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

public class CeltraAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(CeltraAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	
	private HttpServletRequest request;
	
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	
	
	private String status;
	
	public String execute(){
		log.info("linCSVFileName:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
	    	String rawFileName=timestamp+"_Celtra_RichMedia_Trafficking_"+"raw"+".csv";
	    	String dirName=LinMobileConstants.REPORT_FOLDER+
                           "/"+
                            LinMobileConstants.RICH_MEDIA_REPORTS_BUCKET+
                            "/Trafficking/"+
                            LinMobileConstants.CELTRA_CHANNEL_NAME+"/"+
                            DateUtil.getCurrentTimeStamp("yyyy_MM");
    	  
	    	/*List<CorePerformanceReportObj> reportList=ReportUtil.createCeltraCorePerformanceProcessCSVReport(linCSV);
	    	StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);
	    	System.out.println("strBuffer:"+strBuffer.toString());*/
	    	
	    	
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw RichMedia Trafficking report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String procFileName=rawFileName.replace("raw", "proc");
    			reportsResponse=service.generateRichMediaTraffickingReport(
    					                   rawFileName,
    					                   procFileName, 
    					                   dirName,
    					                   LinMobileConstants.CELTRA_CHANNEL_NAME);
    			log.info("Proccessed  RichMedia Trafficking report path:"+reportsResponse);
    			if(reportsResponse !=null){
    				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse,
		    						LinMobileConstants.RICH_MEDIA_TABLE_SCHEMA, 
		    						LinMobileConstants.RICH_MEDIA_TABLE_ID);
    			}else{
    				String message="Please check the log, RichMedia Trafficking report failed to generate.";
        			String subject="RichMedia Trafficking report failed to generate";        			
    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    			}    			
    		}else{
    			String message="RichMedia Trafficking report failed to upload at cloud storage. Please check application log.";
    			String subject="RichMedia Trafficking report failed to upload at cloud storage.";        			
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    		}
    		request.setAttribute("status", reportsResponse);
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
		}
		return Action.SUCCESS;
	}
	
	public String customEvent(){
		log.info("linCSVFileName:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
	    	String rawFileName=timestamp+"_Celtra_RichMedia_CustomEvent_"+"raw"+".csv";
    	  
    	    String dirName=LinMobileConstants.REPORT_FOLDER+
            				"/"+
            				LinMobileConstants.RICH_MEDIA_REPORTS_BUCKET+            				
            				"/Custom_Events/"+
            				LinMobileConstants.CELTRA_CHANNEL_NAME+"/"+
            				DateUtil.getCurrentTimeStamp("yyyy_MM");
    	    
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw RichMedia custom event report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String procFileName=rawFileName.replace("raw", "proc");
    			reportsResponse=service.generateRichMediaCustomEventReport(rawFileName,procFileName,
    					dirName,LinMobileConstants.CELTRA_CHANNEL_NAME,null,null,null,
    					LinMobileVariables.APPLICATION_BUCKET_NAME);
    			log.info("Proccessed  RichMedia custom event report path:"+reportsResponse);
    			if(reportsResponse !=null){
    				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse, 
    						LinMobileConstants.CUSTOM_EVENT_TABLE_SCHEMA,
    						LinMobileConstants.BQ_CUSTOM_EVENT);    				    				    			
    			}else{
    				String message="Please check the log, RichMedia custom event report failed to generate.";
        			String subject="RichMedia custom event report failed to generate";        			
    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    			}    			
    		}else{
    			String message="RichMedia custom event report failed to upload at cloud storage. Please check application log.";
    			String subject="RichMedia custom event report failed to upload at cloud storage.";        			
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    		}
    		request.setAttribute("status", reportsResponse);
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
		}
		return Action.SUCCESS;
	}
	
	
	public String uploadCeltra(){
		log.info("linCSVFileName:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
	    	String rawFileName=timestamp+"_Celtra_CorePerformance_RichMediaTrafficking_"+"raw"+".csv";
	    	String dirName=LinMobileConstants.REPORT_FOLDER+
                           "/"+
                            LinMobileConstants.CELTRA_REPORTS_BUCKET+
                            "/"+
                            DateUtil.getCurrentTimeStamp("yyyy_MM");
	    	reportsResponse="No file processor available, please create a fileprocessor first.";
	    	//reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV, rawFileName, dirName);
	    	List<CorePerformanceReportObj> reportList=null;
	    	//ReportUtil.createCeltraCorePerformanceProcessCSVReport(linCSV);
	    	//StringBuffer csvData=ReportUtil.createCorePerformanceCSVReport(reportList);
	    	String procFileName=rawFileName.replace("raw", "proc");
	    /*	try {
				reportsResponse=ReportUtil.uploadReportOnCloudStorage(csvData.toString(), procFileName, dirName);
				if(reportsResponse !=null){
	    			log.info("Proccessed  RichMedia Trafficking report path:"+reportsResponse);
	    			if(reportsResponse !=null){
	    				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse,
			    						LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA, 
			    						LinMobileConstants.CORE_PERFORMANCE_TABLE_ID
			    						//"Core_Performance_Test"
			    						);
	    			}else{
	    				String message="Please check the log, RichMedia Trafficking report failed to generate.";
	        			String subject="RichMedia Trafficking report failed to generate";        			
	    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
	    			}    			
	    		}else{
	    			String message="RichMedia Trafficking report failed to upload at cloud storage. Please check application log.";
	    			String subject="RichMedia Trafficking report failed to upload at cloud storage.";        			
					LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
	    		}
	    		request.setAttribute("status", reportsResponse);
			} catch (IOException e) {
				log.severe("IO Exception : failed to upload file at cloud storage:"+e.getMessage());
				e.printStackTrace();
			}*/
    		
    		
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
		}
		request.setAttribute("status", reportsResponse);
		return Action.SUCCESS;
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
	
	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
	}

}
