package com.lin.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.server.bean.DataCollectorReport;
import com.lin.server.bean.DataProcessorReport;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

/*
 * @author - Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
 * 
 * DataCorrectorReportAction will help to upload failed report after correcting them
 */
public class DataCorrectorReportAction implements ServletRequestAware,SessionAware{

	
	static final Logger log = Logger.getLogger(DataCorrectorReportAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	
	private String reportType;
	private String level;
	private String dataSource;
	
	private String status;
	private String startDate;
	private String endDate;
	
	/*
	 *  This action method will upload or precess file
	 *  as per user imputs
	 */
	public String execute(){
		log.info("linCSVFileName:"+linCSVFileName);
		log.info("reportType:"+reportType+", level:"+level+" ,dataSource:"+dataSource);
		boolean dateCheck=true;
		if (startDate != null && !DateUtil.isDateFormatYYYYMMDD(startDate)) {
			reportsResponse = "Invalid startDate :" + startDate
					+ ", Please provide yyyy-MM-dd format.";
			dateCheck = false;
			status=reportsResponse;
		}
		if (endDate != null && !DateUtil.isDateFormatYYYYMMDD(endDate)) {
			reportsResponse = "Invalid endDate :" + startDate
					+ ", Please provide yyyy-MM-dd format.";
			dateCheck = false;
			status=reportsResponse;
		}
		
		if(startDate ==null || endDate==null){
			reportsResponse = "Date can not be blank";
	        dateCheck = false;
	        status=reportsResponse;
		}
		if(dateCheck){
			if( (reportType!=null && level!=null  && dataSource!=null) 
					&& !( reportType.equals("-1") ||  level.equals("-1") || dataSource.equals("-1"))){
				if(level !=null && level.equals("Process")){
					status=updateDataCollector(linCSV,reportType,dataSource,startDate,endDate);
				}else if(level !=null && level.equals("Upload")){
					status=updateDataProcessor(linCSV,reportType,dataSource,startDate,endDate);
				}else{
					status="Error: Invalid level.";
				}
				
			}else{
				status="Error: reportType, level and dataSource all three fields required.";				
			}
			reportsResponse=status;
			
		}
		
		
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}
	
	/*
	 *  This action method will upload raw file to cloud stoarge
	 *  and save info in DataCollectorReport entity in datastore
	 *  so that DataProcessor can process it automatically 
	 */
	public String updateDataCollector(String csvFile,String reportType,String dataSource,
			String startDate,String endDate){
		String response=null;
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		DataCollectorReport collectorObj;
		
		String rawFileName=null;
		String dirName=LinMobileConstants.REPORT_FOLDER;
		String id=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String reportId=id;
    	String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
    	String currentMonth=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		if(reportType.equals("CorePerformance")){
			if(linCSVFileName !=null && linCSVFileName.contains("raw")){
				rawFileName=linCSVFileName;
			}else{
				rawFileName=timestamp+"_CorePerformance_"+startDate+"_"+endDate+"_raw"+".csv";
			}			
			if(dataSource.equals("Undertone")){
				dirName=dirName+"/"+LinMobileConstants.UNDERTONE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("DFP")){
				dirName=dirName+"/"+LinMobileConstants.DFP_LIN_MEDIA_REPORT_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Mojiva")){
				dirName=dirName+"/"+LinMobileConstants.MOJIVA_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Nexage")){
				dirName=dirName+"/"+LinMobileConstants.NEXAGE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Google AdExchange")){
				dirName=dirName+"/"+LinMobileConstants.AD_EXCHANGE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("LSN")){
				dirName=dirName+"/"+LinMobileConstants.LSN_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Celtra")){
				dirName=dirName+"/"+LinMobileConstants.CELTRA_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("XAd")){
				dirName=dirName+"/"+LinMobileConstants.XAD_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Tribune")){
				dirName=dirName+"/"+LinMobileConstants.TRIBUNE_DFP_REPORT_BUCKET+"/"+currentMonth;	
			}else{
				dirName=dirName+"/"+"misc_reports"+"/"+currentMonth;	
			}
			
		}else if(reportType.equals("PerformanceByLocation")){
			if(linCSVFileName !=null && linCSVFileName.contains("raw")){
				rawFileName=linCSVFileName;
			}else{
				rawFileName=timestamp+"_PerformanceByLocation_"+startDate+"_"+endDate+"_raw"+".csv";
			}			
			dirName=dirName+"/"+"dfp_reports_by_location"+"/"+currentMonth;
		}else if(reportType.equals("SellThrough")){
			rawFileName=timestamp+"_SellThrough_"+startDate+"_"+endDate+"_raw"+".csv";
			dirName=dirName+"/"+LinMobileConstants.SELL_THROUGH_REPORTS_BUCKET+"/"+currentMonth;
		}else{
			rawFileName=timestamp+"_"+reportType+"_"+startDate+"_"+endDate+"_raw"+".csv";
			dirName=dirName+"/"+"misc_reports"+"/"+currentMonth;
		}
		
		List<String> dataList=ReportUtil.splitRawCSVFile(csvFile);
		int total=dataList.size();
		if(total==0){
			response="Failed to split file, invalid csv file. Check application log...";
		}
		int count=0;
		for(String splitCSVFile:dataList){
			String splitRawFileName=null;
			if(total <=1){
				splitRawFileName=rawFileName;
			}else{
				splitRawFileName=rawFileName.replace("raw", "raw_"+count);
			}
			
			try {
				reportsResponse=ReportUtil.uploadReportOnCloudStorage(splitCSVFile, splitRawFileName, dirName);
			} catch (IOException e) {
				log.severe("Failed to upload file at cloud storage. IOException: "+e.getMessage());
				reportsResponse=null;
				e.printStackTrace();
			}
			if(reportsResponse !=null){
				collectorObj=new DataCollectorReport(id, reportId, splitRawFileName,
						dataSource, reportsResponse, createdOn, "1", null, createdOn, "0",
						reportType, startDate, endDate, dirName);
	            collectorObj.setStatus("0");
	            response=response+"\n Upload status: "+splitRawFileName+": Success";
			}else{
				collectorObj=new DataCollectorReport(id, reportId, splitRawFileName,
						dataSource, reportsResponse, createdOn, "1", null, createdOn, "0",
						reportType,startDate, endDate, dirName);
	            collectorObj.setStatus("2");
	            collectorObj.setErrorLog("Failed to upload at cloud storage");    
	            response=response+"\n Upload status: "+splitRawFileName+": Failed";
			}
			
			log.info("reportsResponse:"+reportsResponse);
			IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
			service.saveDataFrameworkReport(collectorObj);
			count++;
		}
		return response;
	}
	
	/*
	 *  This action method will upload process file to cloud stoarge
	 *  and save info in DataProcessorReport entity in datastore
	 *  so that DataUploader can upload it on BigQuery automatically 
	 */
    public String updateDataProcessor(String csvFile,String reportType,String dataSource,
    		String startDate,String endDate){
		String response=null;
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
				
		String processFileName=null;
		String dirName=LinMobileConstants.REPORT_FOLDER;
		String id=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
    	String reportId=id;
    	String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
    	String currentMonth=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    	
		if(reportType.equals("CorePerformance")){
			
			if(linCSVFileName !=null && linCSVFileName.contains("raw")){
				processFileName=linCSVFileName;
			}else{
				processFileName=timestamp+"_CorePerformance_"+startDate+"_"+endDate+"_proc"+".csv";
			}	
			if(dataSource.equals("Undertone")){
				dirName=dirName+"/"+LinMobileConstants.UNDERTONE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("DFP")){
				dirName=dirName+"/"+LinMobileConstants.DFP_LIN_MEDIA_REPORT_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Mojiva")){
				dirName=dirName+"/"+LinMobileConstants.MOJIVA_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Nexage")){
				dirName=dirName+"/"+LinMobileConstants.NEXAGE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Google AdExchange")){
				dirName=dirName+"/"+LinMobileConstants.AD_EXCHANGE_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("LSN")){
				dirName=dirName+"/"+LinMobileConstants.LSN_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Celtra")){
				dirName=dirName+"/"+LinMobileConstants.CELTRA_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("XAd")){
				dirName=dirName+"/"+LinMobileConstants.XAD_REPORTS_BUCKET+"/"+currentMonth;	
			}else if(dataSource.equals("Tribune")){
				dirName=dirName+"/"+LinMobileConstants.TRIBUNE_DFP_REPORT_BUCKET+"/"+currentMonth;	
			}else{
				dirName=dirName+"/"+"misc_reports"+"/"+currentMonth;	
			}
			
		}else if(reportType.equals("PerformanceByLocation")){
			if(linCSVFileName !=null && linCSVFileName.contains("raw")){
				processFileName=linCSVFileName;
			}else{
				processFileName=timestamp+"_PerformanceByLocation_"+startDate+"_"+endDate+"_proc"+".csv";
			}
			dirName=dirName+"/"+"dfp_reports_by_location"+"/"+currentMonth;
		}else if(reportType.equals("SellThrough")){
			processFileName=timestamp+"_SellThrough_"+startDate+"_"+endDate+"_proc"+".csv";
			dirName=dirName+"/"+LinMobileConstants.SELL_THROUGH_REPORTS_BUCKET+"/"+currentMonth;
		}else{
			processFileName=timestamp+"_"+reportType+"_"+startDate+"_"+endDate+"_proc"+".csv";
			dirName=dirName+"/"+"misc_reports"+"/"+currentMonth;
		}
		
		DataProcessorReport processorObj=new DataProcessorReport();
		processorObj.setReportName(processFileName);
    	processorObj.setUpdatedOn(createdOn);
    	processorObj.setReportId(reportId);
    	processorObj.setId(id);
    	processorObj.setDataSource(dataSource);
    	processorObj.setDirName(dirName);
    	processorObj.setReportType(reportType);
    	processorObj.setUploadBQStatus("0");
    	processorObj.setStartDate(startDate);
    	processorObj.setEndDate(endDate);
		
		
		List<String> dataList=ReportUtil.splitRawCSVFile(csvFile);
		int total=dataList.size();
		if(total==0){
			response="Failed to split file, invalid csv file. Check application log...";
		}
		int count=0;
		for(String splitCSVFile:dataList){
			String splitProcessFileName=null;
			
			if(total <=1){
				splitProcessFileName=processFileName;
			}else{
				splitProcessFileName=processFileName.replace("proc", "proc_"+count);
			}
			
			try {
				reportsResponse=ReportUtil.uploadReportOnCloudStorage(splitCSVFile, splitProcessFileName, dirName);
			} catch (IOException e) {
				log.severe("Failed to upload file at cloud storage. IOException: "+e.getMessage());
				reportsResponse=null;
				e.printStackTrace();
			}
			if(reportsResponse !=null){			
				processorObj.setReportPath(reportsResponse);
				processorObj.setProcessedStatus("1");
				response=response+"\n Upload status: "+splitProcessFileName+": Success";
			}else{
				processorObj.setProcessedStatus("0");
				processorObj.setStatus("2");
				processorObj.setErrorLog("Failed to upload file on cloud stoarge.");
				response=response+"\n Upload status: "+splitProcessFileName+": Failed";
			}
			processorObj.setReportName(splitProcessFileName);
			
			log.info("reportsResponse:"+reportsResponse);
			IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
			service.saveDataFrameworkReport(processorObj);
			count++;
		}
		return response;
	}
	
    
    public String uploadLinOneData(){
		log.info("uploadLinOneData:"+linCSVFileName);
		if(linCSVFileName !=null){
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");	
			String rawFileName=timestamp+"_DFP_LinOne_RichMedia_CustomEvent"+"_raw"+".csv";
			String dirName=LinMobileConstants.REPORT_FOLDER+
            			"/"+
            			LinMobileConstants.RICH_MEDIA_REPORTS_BUCKET+
            			"/Custom_Events/LinOne/"+
            			LinMobileConstants.DFP_DATA_SOURCE+"/"+
            			DateUtil.getCurrentTimeStamp("yyyy_MM");
			
    		reportsResponse=ReportUtil.uploadRawCSVReportOnCloudStorage(linCSV,rawFileName,dirName);
    		log.info("Raw LinOne RichMedia Custom Event report path:"+reportsResponse);
    		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
    		if(reportsResponse !=null){
    			String processFile=rawFileName.replace("raw", "proc");
    			reportsResponse=service.generateLinOneRichMediaCustomEventReport(rawFileName,processFile,
	    				dirName,LinMobileConstants.DFP_DATA_SOURCE);
    			log.info("Proccessed  LinOne RichMedia Custom Event report path:"+reportsResponse);
    			
    			if(reportsResponse !=null && reportsResponse.contains(".csv")){
	    			log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
		    		BigQueryUtil.uploadDataOnBigQuery(reportsResponse,
		    				LinMobileConstants.CUSTOM_EVENT_TABLE_SCHEMA, 
		    				LinMobileConstants.BQ_CUSTOM_EVENT);
	    		}else{
    				String message="Please check the log, LinOne RichMedia Custom Event report failed to generate.";
        			String subject="LinOne RichMedia Custom Event report failed to generate";        			
    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    			}    			
    		}else{
    			String message="LinOne RichMedia Custom Event failed to upload at cloud storage. Please check application log.";
    			String subject="LinOne RichMedia Custom Event report failed to upload at cloud storage.";        			
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);	
    		}
    		request.setAttribute("status", reportsResponse);
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
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

	
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}



	public String getDataSource() {
		return dataSource;
	}



	public void setReportType(String reportType) {
		this.reportType = reportType;
	}



	public String getReportType() {
		return reportType;
	}



	public void setLevel(String level) {
		this.level = level;
	}



	public String getLevel() {
		return level;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getEndDate() {
		return endDate;
	}

}
