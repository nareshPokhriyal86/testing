package com.lin.web.action;

import java.io.IOException;
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
import com.opensymphony.xwork2.Action;

public class ClientDemoDataAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(ClientDemoDataAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
	
	public String execute(){	   
	    String orderId=request.getParameter("orderId");
	    String orderName=request.getParameter("orderName");
	    String advertiserName=request.getParameter("advertiserName");
	    String siteName=request.getParameter("siteName");
	    String lineItemName=request.getParameter("lineItemName");
	    String networkCode=request.getParameter("networkCode");
	    String schemaName=request.getParameter("schemaName");
	    String month=request.getParameter("month");
	    log.info("clientDemoDataUploader action executes..orderId:"+orderId+", orderName:"+orderName+", advertiserName:"+advertiserName
	    		+", siteName:"+siteName+", lineItemName:"+lineItemName+", networkCode:"+networkCode+", schemaName:"+schemaName+", month:"+month);	
	    
	    boolean dataCheck=validateData(orderId, orderName, advertiserName, siteName, lineItemName, networkCode,
	    		schemaName, month);
		
	    if(dataCheck){
	    	if(orderName ==null){
	    		orderName="order 1";	    		
	    	}else{
	    		orderName=orderName.replaceAll("\"", "");
	    	}
	    	if(advertiserName==null){
	    		advertiserName="brand 1";
	    	}else{
	    		advertiserName=advertiserName.replaceAll("\"", "");
	    	}
	    	if(siteName==null){
	    		siteName="site 1";
	    	}else{
	    		siteName=siteName.replaceAll("\"", "");
	    	}
	    	if(lineItemName==null){
	    		lineItemName="line item 1";
	    	}else{
	    		lineItemName=lineItemName.replaceAll("\"", "");
	    	}
	    	
	      try {
	    	
	    	String startDate=month+"-01";		
	  		String endDate=DateUtil.getEndDateOfMonth(startDate, "yyyy-MM-dd");
	  	 	  		
	    	log.info(" now going to build dfpSession ...");
	    	dfpSession=DFPAuthenticationUtil.getDFPSession(networkCode);
			log.info(" getting DfpServices instance from properties...");
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			String downloadUrl= null;
			
			IDFPReportService dfpReportService=new DFPReportService();
			
			List<Long> orderIdList=new ArrayList<Long>();
			List<String> orderIds=null;
			orderIdList.add(Long.parseLong(orderId));
			String csvData=null;
			if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CORE_PERFORMANCE)){
				downloadUrl = dfpReportService.getDFPReport(dfpServices, dfpSession, startDate, endDate, orderIdList, null);
				log.info("report downloadUrl:"+downloadUrl);
				reportsResponse=downloadUrl;
				csvData=dfpReportService.readCSVGZFile(downloadUrl);				
			}else if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION)){
				orderIds=new ArrayList<String>();
				orderIds.add(orderId);
				downloadUrl = dfpReportService.getDFPReportByLocationByOrderIds(dfpServices, dfpSession, startDate,
						endDate, orderIds);
				log.info("report downloadUrl:"+downloadUrl);
				reportsResponse=downloadUrl;
				csvData=dfpReportService.readCSVGZFile(downloadUrl);				
			}else if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_DFP_TARGET)){
				downloadUrl = dfpReportService.getDFPTargetReport(dfpServices, dfpSession, startDate, endDate,
						orderIdList, null);
				log.info("report downloadUrl:"+downloadUrl);
				reportsResponse=downloadUrl;
				csvData=dfpReportService.readCSVGZFile(downloadUrl);				
			}else if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CUSTOM_EVENT)){
				orderIds=new ArrayList<String>();
				orderIds.add(orderId);
				downloadUrl = dfpReportService.getRichMediaCustomEventReportByOrderId(dfpServices, dfpSession, startDate,
						endDate, orderIds);
				log.info("report downloadUrl:"+downloadUrl);
				reportsResponse=downloadUrl;
				csvData=dfpReportService.readCSVGZFile(downloadUrl);				
			}			
	    	
			reportsResponse=processAndUploadReport(startDate, endDate, csvData, schemaName, networkCode, orderName,
					advertiserName, lineItemName, siteName);
			
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
	
	
	private String processAndUploadReport(String startDate,String endDate, String csvData,String schemaName,
			String networkCode, String orderName, String advertiserName, String lineItemName, String siteName)
					throws IOException{
    	String response="";
    	if(csvData !=null){
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
	    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
    		String proccessFileName=rawFileName.replaceAll("_raw", "_proc");
    	    String dirName=LinMobileConstants.REPORT_FOLDER+       
    	                   "/ClientDemo/finalise/"+
    	                   month;
    	    
    	    String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName);
    		log.info("Report saved :"+reportPathOnStorage);	    		
    		
    		String projectId=LinMobileConstants.CLIENT_DEMO_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.CLIENT_DEMO_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.CLIENT_DEMO_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
					
			String tableId=schemaName+"_"+month;
			String publisherId=LinMobileConstants.CLIENT_DEMO_COMPANY_ID;
			String publisherName=LinMobileConstants.CLIENT_DEMO_COMPANY_NAME;   
			String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
			
			String schemaFile="";
			String processedFilePath="";
			List<String> processedFilePathList=null;
			
			if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CORE_PERFORMANCE)){
				schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				response=service.generateClientDemoCorePerformanceReport(startDate, endDate, rawFileName, dirName,
						networkCode, publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
				processedFilePath=response;
			}else if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION)){
				schemaFile=LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_SCHEMA;
				processedFilePathList=service.generateClientDemoDFPReportByLocationWithSplit(rawFileName, proccessFileName, dirName,
						networkCode, publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
				log.info("processedFilePathList:"+processedFilePathList.size());	    		
	    		for(int i=0;i<processedFilePathList.size();i++){		    				
	    			response=processedFilePathList.get(i);
	    			if(i==0){
	    				processedFilePath=response;
	    			}else{
	    				processedFilePath=processedFilePath+","+response;
	    			}			    			
	    		}  
			}else if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_DFP_TARGET)){
				schemaFile=LinMobileConstants.DFP_TARGET_PLATFORM_TABLE_SCHEMA;
				response=service.generateClientDemoDFPTargetReport(startDate, endDate, rawFileName, dirName, networkCode,
						publisherId,publisherName, orderName, advertiserName, siteName, lineItemName);
				processedFilePath=response;
			}else if( schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CUSTOM_EVENT)){
				schemaFile=LinMobileConstants.CUSTOM_EVENT_TABLE_SCHEMA;
				response=service.generateClientDemoRichMediaCustomEventReport(rawFileName, proccessFileName, dirName, dataSource, 
						networkCode, publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
				processedFilePath=response;
			}else{
				response="Invalid schema, no service found for this schema - "+schemaName;
				log.severe(response);
			}
			
			QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
					projectId, schemaFile, tableId, dataSetId, schemaName);  
    		
			String id=publisherId+":"+tableId;	
    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 1, startDate, endDate, 
    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
    				projectId, dataSetId, 0);			    		
    		
    		String bqResponse="";
    		if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION)){
    			bqResponse=service.uploadProcessFileAtBQ(reportObject, 1,processedFilePathList, bigQueryDTO,null);
    		}else{
    			bqResponse=service.uploadProcessFileAtBQ(reportObject, 1,  response, bigQueryDTO,null);
    		}
			response=response+" and bigQuery response:: "+bqResponse;
    		
    		
    	}else{
    		log.warning("Failed to create report, please check log ....");
    		response="Failed to create report";
    	}	
    	return response;
    }
		
	
	private boolean validateData(String orderId,String orderName,String advertiserName,String siteName,String lineItemName,
			String networkCode, String schemaName,String month){
		boolean CheckData=true;
		if(orderId ==null || month==null || schemaName==null || networkCode==null){
			CheckData=false;
			reportsResponse="These parameters are mandatory and can not be blank : orderId, month, schemaName, networkCode.";
		}else {
			switch(networkCode){
			   case "5678":
				   CheckData=true;
				   break;
			   case "4206":
				   CheckData=true;
				   break;
			   case "45604844":
				   CheckData=true;
				   break;
			   default:
				   CheckData=false;
				   reportsResponse="Invalid networkCode :"+networkCode+", Only allowed codes are <5678 ,4206, 45604844> ";
				   break;
			}
		}
		
		
		if(CheckData){
			
			if(!LinMobileUtil.isNumeric(orderId)){
		    	reportsResponse="Invalid orderId, it should be numeric.";
		    	CheckData=false;		    	
		    }else if(month !=null ){
		    	String [] monthArray=month.split("-");
		    	if(monthArray.length==2){
		    		int year=Integer.parseInt(monthArray[0]);
		    		int monthValue=Integer.parseInt(monthArray[1]);
		    		if(year <=0 || monthValue <=0 || monthValue >12){
		    			reportsResponse="Invalid month :"+month+" , it should be yyyy-MM format only.";
				    	CheckData=false;
		    		}else{
		    			CheckData=true;
		    		}
		    	}else{
		    		reportsResponse="Invalid month :"+month+" , it should be yyyy-MM format only.";
		    		CheckData=false;
		    		return CheckData;
		    	}
		    }else if(schemaName!=null){
		    	schemaName=schemaName.replaceAll("\"", "");
		    	if(schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CORE_PERFORMANCE) ||
		    	   schemaName.equalsIgnoreCase(LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION) ||
		    	   schemaName.equalsIgnoreCase(LinMobileConstants.BQ_DFP_TARGET) ||
		    	   schemaName.equalsIgnoreCase(LinMobileConstants.BQ_CUSTOM_EVENT)){
		    		CheckData=true;
		    	}else{
		    		reportsResponse="Invalid schemaName :"+schemaName+" , it should be any one out of these < "+LinMobileConstants.BQ_CORE_PERFORMANCE+
		    				", "+LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION+", "+LinMobileConstants.BQ_DFP_TARGET
		    				+", "+LinMobileConstants.BQ_CUSTOM_EVENT+" >.";
		    		CheckData=false;
		    	}
		    }else{
		    	CheckData=true;
		    }
		}		
		return CheckData;
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


