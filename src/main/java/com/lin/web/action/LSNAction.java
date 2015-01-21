package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
import com.lin.web.util.LSNReportUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.UndertoneReportUtil;
import com.opensymphony.xwork2.Action;

public class LSNAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(LSNAction.class.getName());
	
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
	    	String rawFileName=timestamp+"_LSN_CorePerformance_"+"raw"+".csv";
    	    String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.LSN_REPORTS_BUCKET+"/"+DateUtil.getCurrentTimeStamp("yyyy_MM");
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw LSN report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String procFileName=timestamp+"_LSN_CorePerformance_"+"proc"+".csv";
    			reportsResponse=service.generateLSNReport(rawFileName,procFileName, dirName);
    			log.info("Proccessed  LSN report path:"+reportsResponse);
    			if(reportsResponse !=null){
    				reportsResponse=uploadDataOnBigQuery(reportsResponse);    				    				    			
    			}else{
    				String message="Please check the log, LSN report failed to generate.";
        			String subject="LSN report failed to generate";        			
    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    			}    			
    		}else{
    			String message="LSN report failed to upload at cloud storage. Please check application log.";
    			String subject="LSN report failed to upload at cloud storage.";        			
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    		}
    		request.setAttribute("status", reportsResponse);
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
		}
		return Action.SUCCESS;
	}
	
	private String uploadDataOnBigQuery(String responseURL){
		String subject="";
		String message="";
		String bigQueryResponse=null;
		if(responseURL !=null && responseURL.contains(".csv")){
			try {
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA;
				String tableId=LinMobileConstants.CORE_PERFORMANCE_TABLE_ID;
				log.info("Before saving data in bigquery");
				bigQueryResponse=BigQueryUtil.saveData(responseURL, schemaFile, tableId);
				log.info("bigQueryResponse:"+bigQueryResponse);
				responseURL="Cloud Storage path:"+responseURL+" And BigQueryResponse:"+bigQueryResponse;
				if(bigQueryResponse !=null && bigQueryResponse.equals("Success")){    				
    				subject="LSN data uploaded on BigQuery successfully";
    				message="LSN data has been uploaded on BigQuery successfully. \nPlease see response: "+responseURL;    				    			
    			}else{
    				subject="LSN data failed to upload on BigQuery";
    				message="LSN data failed to upload on BigQuery successfully. \nPlease see response: "+responseURL;    				    				
    			}
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				log.severe("Exception in saving data in bigquery: GeneralSecurityException:"+e.getMessage());
				e.printStackTrace();
			}
		}else{
			log.warning("Cron job failed : Sending mail...");
			message="Please check the log, LSN cron job has been falied.";
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "Cron job falied", message);
		}	
		return responseURL;
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
