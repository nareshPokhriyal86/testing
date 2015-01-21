package com.lin.web.action;

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
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

/*
 * @author - Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
 * 
 * TribunePerformanceByLocationAction will fetch performance by location raw data from DFP
 */

public class TribunePerformanceByLocationAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(TribunePerformanceByLocationAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION;
    
	public String execute() {
		return Action.SUCCESS;
	}	
		
	public String dailyReport(){		
		log.info("locationDailyTribuneReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
		String accountId=request.getParameter("accountId");
		
		if(startDate !=null && !DateUtil.isDateFormatYYYYMMDD(startDate)){
			reportsResponse="Invalid startDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		if(endDate !=null && !DateUtil.isDateFormatYYYYMMDD(endDate)){
			reportsResponse="Invalid endDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		
		if(startDate ==null && endDate==null){			
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); //DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseReport(startDate, endDate,accountId);
	   }else{
		   reportsResponse="Invalid dates...";
		   log.info(reportsResponse);
	   }
	   return Action.SUCCESS;
	}	
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseReport(){
		log.info("locationNonFinaliseTribuneReport action starts...");
		
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);			
			String nonFinaliseTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);					
					reportsResponse=runNonFinaliseReport(startDate, endDate,null);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
			}
			i++;
		}
		log.info("locationNonFinaliseTribuneReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}

	/*
	 * update monthly finalise table for Tribune non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("locationFinaliseTribuneReport action executes..");
		
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
			startDate=DateUtil.getModifiedDateStringByDays(currentDate,-LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");
			endDate=startDate;
		}else if(startDate !=null && endDate !=null){
			String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			long days=DateUtil.getDifferneceBetweenTwoDates(endDate,currentDate, "yyyy-MM-dd");
			if(days < LinMobileConstants.CHANGE_WINDOW_SIZE){
				dateCheck=false;
				reportsResponse="Invalid end date, you can not merge the data within change window days. Change Widow size:"+
				    LinMobileConstants.CHANGE_WINDOW_SIZE;
			}
			
		}		
		
		
	    if(dateCheck){
	      try {
	    		
	    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
		    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		    String projectId=LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_SCHEMA;
			
				        
	        String readyToMergeTableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
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
	    			
	    			String [] processFilePathArray=processedFilePath.split(",");
	    			List<String> processFileList=new ArrayList<String>();
	    			String id=publisherId+":"+finaliseTableId;
	    			
	    			for(int i=0;i<processFilePathArray.length;i++){	    				
	    				processFileList.add(processFilePathArray[i]);	    				
	    			}
	    			int finalise=1;    				
    				FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, finaliseTableId, finalise, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);	
    				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
    						projectId, schemaFile, finaliseTableId, dataSetId, schemaName);
    				
    				reportsResponse=service.uploadProcessFileAtBQ(reportObject, finalise, processFileList, bigQueryDTO,null);
    				log.info("uploaded file at bq table :reportsResponse:"+reportsResponse);
    				
	    			log.info("This table has been merged to finalised table successfully..: readyToMergeTableId:"+readyToMergeTableId);
		    		
	    		}	    		
	    		
	    	}else{
	    		log.warning("Failed to find table for merging.."+readyToMergeTableId);
	    		reportsResponse="Failed to find table for merging..readyToMergeTableId:"+readyToMergeTableId;
	    	}	
		 } catch (Exception e) {
			log.severe("DFP report exception: Exception :"+e.getMessage());
			reportsResponse=e.getMessage();
			e.printStackTrace();
		 }
	   }	
	   return Action.SUCCESS;
	}
	
	 private String runNonFinaliseReport(String startDate,String endDate, String accountId){
	    	String response="";
	    	try {	
	    		String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
	    		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
	    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
	    		dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
						LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME);	
			    
				log.info(" getting DfpServices instance from properties...");
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
		        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		        IDFPReportService dfpReportService=new DFPReportService();
		       
		        List<String> accountIdList=new ArrayList<String>();
		        if(accountId !=null){
		        	accountIdList.add(accountId);
		        }else{		        	
		        	accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
		        }
		        		       
		        String downloadUrl = dfpReportService.getDFPReportByLocationByAccountIds(dfpServices, dfpSession,
						startDate,endDate,accountIdList);		       
		        
		        List<String> csvDataList=dfpReportService.readCSVGZFileAndSplit(downloadUrl);		       
		        
		    	if(csvDataList !=null && csvDataList.size()>0){
	    		    
					String projectId=LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID;
					String serviceAccountEmail=LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
					String servicePrivateKey=LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
					String bucketName=LinMobileVariables.TRIBUNE_CLOUD_STORAGE_BUCKET;
					
					String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
					
					String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
					String schemaFile=LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_SCHEMA;
					
					QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
							projectId, schemaFile, tableId, dataSetId, schemaName);
					
		    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
			    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    		String rawFileName=timestamp+"_PerformanceByLocation_"+startDate+"_"+endDate+"_raw"+".csv";    		
		    		String procFileName=rawFileName.replace("_raw", "_proc");
		    	    String dirName=LinMobileConstants.REPORT_FOLDER+		    	                  
		    	                  "/dfp_reports_by_location/Tribune/non_finalise/"
		    	                  +month;
		    	    String reportPathOnStorage="";
		    	    
		    	    String splitRawFileName="";
		    	    List<String> processedFilePathList=new ArrayList<>();
		    	    for(int i=0;i<csvDataList.size();i++){
		    	    	String csvData=csvDataList.get(i);
		    	    	if(csvDataList.size()> 1){
		    	    		splitRawFileName=rawFileName.replace("_raw", "_raw_"+i);		    	    		
		    	    	}else{
		    	    		splitRawFileName=rawFileName;
		    	    	}
		    	    	procFileName=splitRawFileName.replace("_raw", "_proc");
		    	    	
		    	    	reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, splitRawFileName,
		    	    			dirName, bucketName);
			    		log.info(" raw report saved on cloud storage :"+reportPathOnStorage);	  
			    		List <String> tempProcessedFilePathList=service.generateDFPReportByLocation(splitRawFileName, 
			    				procFileName, dirName,
			    				networkCode,LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID,
			    				LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME,bucketName);
			    		processedFilePathList.addAll(tempProcessedFilePathList);
		    	    }
		    		
		    		
		    		/*processedFilePathList=service.generateDFPReportByLocationWithSplit(rawFileName,procFileName, 
		    				dirName,networkCode,LinMobileConstants.TRIBUNE_PUBLISHER_ID,LinMobileConstants.TRIBUNE_PUBLISHER_NAME);*/
		    		log.info("processedFilePathList:"+processedFilePathList.size());
		    		
		    		String processedFilePath="";
		    		String bqResponse="";
		    		String id=publisherId+":"+tableId;
		    		for(int i=0;i<processedFilePathList.size();i++){		    				
		    			response=processedFilePathList.get(i);
		    			if(i==0){
		    				processedFilePath=response;
		    			}else{
		    				processedFilePath=processedFilePath+","+response;
		    			}			    			
		    		}  
		    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);			    		
		    		
		    		bqResponse= service.uploadProcessFileAtBQ(reportObject, 0, processedFilePathList, bigQueryDTO,null);
		    		
		    		response=processedFilePathList+" and bigQuery response:: "+bqResponse;		    		
		    		
		    	}else{
		    		log.warning("Failed to create report, please check log ....");
		    		response="Failed to create report";
		    	}	
			 } catch (ValidationException e) {
				log.severe("DFP session exception: ValidationException :"+e.getMessage());
				response=e.getMessage();
				e.printStackTrace();
			 } catch (Exception e) {
				log.severe("DFP report exception: Exception :"+e.getMessage());
				response=e.getMessage();
				e.printStackTrace();
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

}


