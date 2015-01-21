package com.lin.web.action;

import java.util.ArrayList;
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
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.PropertyObj;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

/*
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 * 
 * Tribune  action -- Not used now, Has been replaced by TribuneDFPAction
 */
public class TribuneAction implements ServletRequestAware,ServletResponseAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(TribuneAction.class.getName());
	
	private String reportsResponse;	

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
		
	/*
	 *  Tribune (for DFP account) action will download report from DFP 
	 *  and upload it on cloud storage and BigQuery
	 * 
	 */
	public String execute(){		
		log.info("TribuneDFP action executes..");
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
		String accountId=request.getParameter("accountId");
		String adUnitId=request.getParameter("adUnitId");
		
		log.info("startDate : "+startDate+", endDate : "+endDate+", accountId : "+accountId+", adUnitId : "+adUnitId);
		
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
			endDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		}
		
	    if(dateCheck){
	      try {
	    	log.info(" now going to build dfpSession ...");
	    	String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
	    	dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
					LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME);	
			log.info(" getting DfpServices instance from properties...");
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);			
			IDFPReportService dfpReportService=new DFPReportService();
			String downloadUrl=null;
			List<String> accountIds=new ArrayList<String>();
			if(accountId !=null ){
				if(LinMobileUtil.isNumeric(accountId)){
					accountIds.add(accountId);
					downloadUrl = dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession, startDate, endDate, accountIds);
				}else{
					reportsResponse="Invalid accountId :"+accountId;
					accountId=null;
					throw new Exception(reportsResponse);
				}
			}else if(adUnitId !=null ){
				if(LinMobileUtil.isNumeric(adUnitId)){
					List<String> adUnitIds=new ArrayList<String>();
					adUnitIds.add(adUnitId);
					downloadUrl=dfpReportService.getDFPReportByAdUnitIds(dfpServices, dfpSession,startDate, 
							endDate, adUnitIds);
				}else{
					reportsResponse="Invalid adUnitId :"+adUnitId;
					adUnitId=null;
					throw new Exception(reportsResponse);
				}
			}else{
				IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				List<String> adUnitIds = new ArrayList<String>();
				List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
				if(propertyObjList == null || propertyObjList.size() == 0){
					reportsResponse="No sites found for tribune in datastore, please check AdUnitObj entity:";							
					throw new Exception(reportsResponse); 
				}
				for (PropertyObj propertyObj : propertyObjList) {
					if(!adUnitIds.contains(propertyObj.getDFPPropertyId())) {
						adUnitIds.add(propertyObj.getDFPPropertyId());
					}
				}
				downloadUrl=dfpReportService.getDFPReportByAdUnitIds(dfpServices, dfpSession,startDate, 
						endDate, adUnitIds);
			}			
			
			//reportsResponse=downloadUrl;
			log.info("Tribune DFP report downloadUrl:"+downloadUrl);
			
	    	String csvData=dfpReportService.readCSVGZFile(downloadUrl);
	    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
	    	if(csvData !=null){
	    		String rawFileName=timestamp+"_Tribune_DFP_CorePerformance_"+startDate+"_"+endDate+"_raw"+".csv";
	    	    String dirName= LinMobileConstants.REPORT_FOLDER+"/"+
	    	    				LinMobileConstants.TRIBUNE_DFP_REPORT_BUCKET+"/"+
	    	    				DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName);
	    		log.info("Report saved :"+reportPathOnStorage);
	    		
	    		
	    		reportsResponse=service.generateDFPReport(startDate, endDate,rawFileName,dirName,timestamp,
	    				LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE);
	    		log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
	    		
	    		reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse,
	    				LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA, 
	    				LinMobileConstants.CORE_PERFORMANCE_TABLE_ID);
	    		
	    		log.info("After :save this data on bigquery: reportsResponse:"+reportsResponse);
	    		
	    	}else{
	    		log.warning("Failed to create report, please check log ....");
	    		reportsResponse="Failed to create report";
	    	}			
			
		 } catch (ValidationException e) {
			log.severe("DFP session exception: ValidationException :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 } catch (Exception e) {
			log.severe("DFP report exception: Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 }
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


