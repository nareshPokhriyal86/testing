package com.lin.web.action;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

public class XAdAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(XAdAction.class.getName());
	
	private String reportsResponse;	

	private HttpServletRequest request;
	private Map session;
	
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	
	
	private String status;
	
	public String execute(){
		log.info("XAd RichMedia action, linCSVFileName:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
	    	String rawFileName=timestamp+"_XAd_RichMedia_Trafficking_"+"raw"+".csv";
	    	String dirName=LinMobileConstants.REPORT_FOLDER+
                           "/"+
                            LinMobileConstants.RICH_MEDIA_REPORTS_BUCKET+
                            "/Trafficking/"+
                            LinMobileConstants.XAD_CHANNEL_NAME+"/"+
                            DateUtil.getCurrentTimeStamp("yyyy_MM");
    	  
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw RichMedia Trafficking report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String procFileName=rawFileName.replace("raw", "proc");
    			reportsResponse=service.generateRichMediaTraffickingReport(
		                   rawFileName,
		                   procFileName, 
		                   dirName,
		                   LinMobileConstants.XAD_CHANNEL_NAME);
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
		log.info("XAd RichMedia customEvent action, linCSVFileName:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
	    	String rawFileName=timestamp+"_XAd_RichMedia_CustomEvent_"+"raw"+".csv";
	    	String bucketName=LinMobileVariables.APPLICATION_BUCKET_NAME;
    	    String dirName=LinMobileConstants.REPORT_FOLDER+
            				"/"+
            				LinMobileConstants.RICH_MEDIA_REPORTS_BUCKET+            				
            				"/Custom_Events/"+
            				LinMobileConstants.XAD_CHANNEL_NAME+"/"+
            				DateUtil.getCurrentTimeStamp("yyyy_MM");
    	    
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw RichMedia custom event report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String procFileName=rawFileName.replace("raw", "proc");
    			reportsResponse=service.generateRichMediaCustomEventReport(rawFileName,procFileName, 
    					dirName,LinMobileConstants.XAD_CHANNEL_NAME,null,null,null,bucketName);
    			log.info("Proccessed  RichMedia custom event report path:"+reportsResponse);
    			if(reportsResponse !=null){
    				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse, 
    						LinMobileConstants.CUSTOM_EVENT_TABLE_SCHEMA,
    						LinMobileConstants.CUSTOM_EVENT_TABLE_ID);    				    				    			
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

	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}

}
