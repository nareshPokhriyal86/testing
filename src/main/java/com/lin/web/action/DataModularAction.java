package com.lin.web.action;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.opensymphony.xwork2.Action;

/*
 *  @author - Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
 *  
 *  This action is useful to process raw files and upload them on BigQuery 
 */

public class DataModularAction implements ServletRequestAware,SessionAware{
	
	static final Logger log = Logger.getLogger(DataModularAction.class.getName());
	private Map session;
	private String reportsResponse;	
	private HttpServletRequest request;		
	
	public String execute(){		
		log.info("DataModularAction action executes..");
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
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			endDate=startDate;
		}
		
	    return Action.SUCCESS;
	}	
	
	/*
	 * This action method will process the raw file from datastore
	 */
	public String processReport(){
		log.info("Data processor action...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		int noOfReportsToProcess=1;
		boolean reportProcessed=service.processReports(noOfReportsToProcess);
		log.info("reportProcessed:"+reportProcessed);
		if(reportProcessed){
			reportsResponse="Success";
		}else{
			reportsResponse="Failed";
		}
		return Action.SUCCESS;
	}

	/*
	 *  This action method will upload the processed file from datastore
	 */
	public String uploadReportOnBQ(){
		log.info("Data uploader action...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		int uploadReportLimit=1;
		boolean reportUploaded=service.uploadReportsOnBigQuery(uploadReportLimit);
		log.info("reportUploaded:"+reportUploaded);
		if(reportUploaded){
			reportsResponse="Success";
		}else{
			reportsResponse="Failed";
		}
		
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



}



