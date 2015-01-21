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

public class DFPTargetReportAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(DFPTargetReportAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
	String schemaName=LinMobileConstants.BQ_DFP_TARGET;


	public String dailyLinMediaReport(){		
		log.info("targetDailyLinMediaReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String orderId=request.getParameter("orderId");	
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
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); 
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseLinMediaReport(startDate, endDate,accountId,orderId);
	   }else{
		   reportsResponse="Invalid dates...";
		   log.info(reportsResponse);
	   }
	   return Action.SUCCESS;
	}	
	
	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyLinMediaNonFinaliseReport(){
		log.info("targetNonFinaliseLinMediaReport action starts...");
		
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
					LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);					
					reportsResponse=runNonFinaliseLinMediaReport(startDate, endDate,null,null);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
			}
			i++;
		}
		log.info(" action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
	}

	/*
	 * update monthly finalise table for LinMedia non-finalised data
	 */
	public String updateMonthwiseLinMediaFinaliseReport(){		
		log.info("targetFinaliseLinMediaReport action executes..");
		
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
		    String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
		    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		    String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile=LinMobileConstants.DFP_TARGET_PLATFORM_TABLE_SCHEMA;
			
				        
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
	    			
	    			String id=publisherId+":"+finaliseTableId;
    				int finalise=1;
    				
    				FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, finaliseTableId, finalise, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);	
    				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
    						projectId, schemaFile, finaliseTableId, dataSetId, schemaName);
    				
    				reportsResponse=service.uploadProcessFileAtBQ(reportObject, finalise, processedFilePath, bigQueryDTO,null);
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
	
	 private String runNonFinaliseLinMediaReport(String startDate,String endDate,String accountId,String orderId){
	    	String response="";
	    	try {	
	    		String networkCode=LinMobileConstants.DFP_NETWORK_CODE;
	    		String publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
	    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
	    		
				dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
							LinMobileConstants.DFP_APPLICATION_NAME);	
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
		        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		        IDFPReportService dfpReportService=new DFPReportService();
		        boolean isOrder=false;
		        List<String> orderIdList = null;
		        List<String> accountIdList=new ArrayList<String>();
		        if(accountId!=null){
		        	accountIdList.add(accountId);
		        }else if(orderId !=null){
			    	isOrder=true;
			    	orderIdList=new ArrayList<String>();
			    	orderIdList.add(orderId);
			    }else{
		        	/*accountIdList = service.getAllAccountId(networkCode,
							LinMobileConstants.DFP_ADVERTISER_USERNAME);*/
		        	accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
		        }
		        		        
		        String downloadUrl = null;
				if(isOrder){
					log.info("Get dfp report by orderIds...");
					downloadUrl=dfpReportService.getDFPTargetReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
				}else{
					log.info("Get dfp report by accountIds...");
				    downloadUrl = dfpReportService.getDFPTargetReportByAccountIds(dfpServices, dfpSession,
						startDate,endDate,accountIdList,null);
				}
		        
		        String csvData=dfpReportService.readCSVGZFile(downloadUrl);		       
		        
		    	if(csvData !=null){
	    		    
					String projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
					String serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
					String servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
					String bucketName=LinMobileVariables.LIN_MEDIA_CLOUD_STORAGE_BUCKET;
					
					String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
					
					String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
					String schemaFile=LinMobileConstants.DFP_TARGET_PLATFORM_TABLE_SCHEMA;
					
					QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
							projectId, schemaFile, tableId, dataSetId, schemaName);
					
		    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
			    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
		    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
		    		
		    	    String dirName=LinMobileConstants.REPORT_FOLDER+ "/"+
		    		               LinMobileConstants.DFP_TARGET_REPORT_BUCKET+		    	                  
		    	                   "/LinMedia/non_finalise/"+
		    	                   month;
		    	    
		    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
		    		log.info("Report saved :"+reportPathOnStorage);	    		
		    		
		    		response=service.generateDFPTargetReport(startDate, endDate,rawFileName,dirName,networkCode,null,null,bucketName);
		    		
		    		String processedFilePath=response;
		    		
	    			String id=publisherId+":"+tableId;	
		    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
		    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
		    				projectId, dataSetId, 0);			    		
		    		
		    		String bqResponse=service.uploadProcessFileAtBQ(reportObject, 0, response, bigQueryDTO,null);
		    		
	    			response=response+" and bigQuery response:: "+bqResponse;
		    		
		    		
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


