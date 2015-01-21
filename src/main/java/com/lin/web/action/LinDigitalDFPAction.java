package com.lin.web.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.ads.dfp.lib.conf.DfpConfigurationModule;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.SmartCampaignPlannerService;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;

public class LinDigitalDFPAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(LinDigitalDFPAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
    
	public String execute(){		
		log.info("LinDigitalDFP action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
		String accountId=request.getParameter("accountId");
		String orderId=request.getParameter("orderId");
		
		log.info("startDate : "+startDate+", endDate : "+endDate+", accountId : "+accountId+", orderId:"+orderId);
		
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
			String networkCode=LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE;
			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
					LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME);	
			log.info(" getting DfpServices instance from properties...");
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
			IDFPReportService dfpReportService=new DFPReportService();
			
			 boolean isOrder=false;
		        List<String> orderIdList = null;
		        
		        List<String> accountIdList=new ArrayList<String>();
		        if(accountId !=null){
		        	accountIdList.add(accountId);
		        }else if(orderId !=null){
			    	isOrder=true;
			    	orderIdList=new ArrayList<String>();
			    	orderIdList.add(orderId);
			    }else{		        	
		        //	accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
			    	isOrder=true;
			    	log.info("Load orders from datastore..");
			    	ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			    	orderIdList=campaignPlannerService.loadAllOrderIdsFromDatastore(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE);
		        }
		        		       
		        String downloadUrl = null;
				if(isOrder){
					log.info("Get dfp report by orderIds...");
					downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, 
							endDate, orderIdList);
				}else{
					log.info("Get dfp report by accountIds...");
					downloadUrl = dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession,
						startDate,endDate,accountIdList);		       
				}
				
			/*List<String> accountIdList=new ArrayList<String>();
			if(accountId !=null ){
				if(LinMobileUtil.isNumeric(accountId)){
					accountIdList.add(accountId);
				}else{
					reportsResponse="Invalid accountId :"+accountId;
					accountId=null;
					throw new Exception(reportsResponse);
				}
			}else{
				accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
			}
			if(accountIdList == null || accountIdList.size() == 0) {
				reportsResponse="No accounts for the company";
				throw new Exception(reportsResponse);
			}
			downloadUrl = dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession, startDate, endDate, accountIdList);*/
			
			log.info("report downloadUrl:"+downloadUrl);
			reportsResponse=downloadUrl;
			
			String csvData=dfpReportService.readCSVGZFile(downloadUrl);
	    	
	    	String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
	    	if(csvData !=null){
	    		String rawFileName=timestamp+"_DFP_LinDigital_CorePerformance_"+startDate+"_"+endDate+"_raw"+".csv";
	    	    String dirName=LinMobileConstants.REPORT_FOLDER+"/"
	    	                  +LinMobileConstants.LIN_DIGITAL_DFP_REPORT_BUCKET+"/"
	    	                  +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName);
	    		log.info("Report saved :"+reportPathOnStorage);
	    		
	    		
	    		reportsResponse=service.generateLinDigitalDFPReport(startDate, endDate,rawFileName,dirName);
	    		log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
	    		if(reportsResponse !=null){
    				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponse,
		    						LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA, 
		    						LinMobileConstants.CORE_PERFORMANCE_TABLE_ID);
    			}else{
    				String message="Please check the log, LinDigital CorePerformance report failed to generate.";
        			String subject="LinDigital CorePerformance report failed to generate";        			
    				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, 
    						LinMobileConstants.TO_EMAIL_ADDRESS, 
    						LinMobileConstants.CC_EMAIL_ADDRESS,
    						subject, message);
    			}    	
	    		
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
	
	
	/*
	 * DailyReoport for LinDigital non-finalised data
	 */
	public String dailyReport(){		
		log.info("dailyLinDigitalReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");	
		String accountId=request.getParameter("accountId");
		String orderId=request.getParameter("orderId");		
		
		if(startDate !=null && !DateUtil.isDateFormatYYYYMMDD(startDate)){
			reportsResponse="Invalid startDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		if(endDate !=null && !DateUtil.isDateFormatYYYYMMDD(endDate)){
			reportsResponse="Invalid endDate :"+startDate+", Please provide yyyy-MM-dd format.";
			dateCheck=false;
		}
		
		if(startDate ==null && endDate==null){
			//Date currentDate=new Date();
			startDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd"); //DateUtil.getModifiedDateStringByDays(currentDate,-1, "yyyy-MM-dd");
			endDate=startDate;
		}
		
	    if(dateCheck){
	    	reportsResponse=runNonFinaliseReport(startDate, endDate,accountId,orderId);	     
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
		log.info("dfpNonFinaliseLinDigitalReport action starts...");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Date currentDate=new Date();		
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			String nonFinaliseTimestamp=DateUtil.getCurrentTimeStamp("yyyyMMdd");
			log.info("startDate:"+startDate+" and endDate:"+endDate+" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
			String nonFinaliseTableId="CorePerformance_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
			FinalisedTableDetailsObj tableObj=service.loadFinaliseNonFinaliseObject(nonFinaliseTableId,
					LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID);
			if(tableObj !=null ){
				log.info(" Object found with tableId:"+nonFinaliseTableId+" and lastUpdatedOn:"+tableObj.getLastUpdatedOn()+
						" and nonFinaliseTimestamp:"+nonFinaliseTimestamp);
				if(tableObj.getLastUpdatedOn().contains(nonFinaliseTimestamp)){
					log.info("This tableId has already updated today.."+nonFinaliseTableId);
				}else{
					log.info("Going to relaod this non-finalised table with tableId.."+nonFinaliseTableId);					
					reportsResponse=runNonFinaliseReport(startDate, endDate,null,null);
					log.info("Relaod this non-finalised table successfully. Exit this job as only one table allowed at a time.");
					break;
				}
			}else{
				log.info("This table id not found in datastore :"+nonFinaliseTableId);
			}
			i++;
		}
		log.info("dfpNonFinaliseLinDigitalReport action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
  }
	
	/*
	 * update monthly finalise table for LinDigital non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("dfpFinaliseLinDigitalReport action executes..");		
	    
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
			    String publisherId=LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID;
			    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
			    String projectId=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				
					        
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
	    				FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, finaliseTableId, finalise, 
	    						startDate, endDate, timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
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
	    
		  log.info("dfpFinaliseLinDigitalReport action ends..");	
	      return Action.SUCCESS;
	}
	
	private String runNonFinaliseReport(String startDate,String endDate,String accountId,String orderId){
    	String response="";
    	try {
    		String networkCode=LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE;
    		String publisherId=LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID;
    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;  
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	    	
	        String csvData=getDFPReportURL(service,startDate, endDate,accountId,orderId);
	    	
	        if(csvData !=null ){
    		    
				String projectId=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.LIN_DIGITAL_CLOUD_STORAGE_BUCKET;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
				
				String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
								
				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
				
	    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"
			   	                  +LinMobileConstants.LIN_DIGITAL_DFP_REPORT_BUCKET+"/non_finalise/"
			   	                  +month;
	    		
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+reportPathOnStorage);	    		
	    		
	    		response=service.generateFinilisedDFPReport(rawFileName,dirName,networkCode,
	    				publisherId,LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME,bucketName);
	    		
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
	
	@SuppressWarnings("deprecation")
	private String getDFPReportURL(IReportService service,String startDate,String endDate,String accountId,String orderId) throws Exception{
		
    	log.info(" now going to build dfpSession ...");
		String networkCode=LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE;
		dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
				LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME);	
		log.info(" getting DfpServices instance from properties...");
		dfpServices = LinMobileProperties.getInstance().getDfpServices();
		IDFPReportService dfpReportService=new DFPReportService();
		
		   boolean isOrder=false;
	       List<String> orderIdList = null;
	        
	        List<String> accountIdList=new ArrayList<String>();
	        if(accountId !=null){
	        	accountIdList.add(accountId);
	        }else if(orderId !=null){
		    	isOrder=true;
		    	orderIdList=new ArrayList<String>();
		    	orderIdList.add(orderId);
		    }else{		        	
	        	accountIdList = service.getAllAccountIdByCompanyName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
	        }
	        		       
	        String downloadUrl = null;
			if(isOrder){
				log.info("Get dfp report by orderIds...");
				downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
			}else{
				log.info("Get dfp report by accountIds...");
				downloadUrl=dfpReportService.getDFPReportByAccountIds(dfpServices, dfpSession, startDate, endDate, accountIdList);
			}	
			
		log.info("LinDigital DFP report downloadUrl:"+downloadUrl);
		String csvData=dfpReportService.readCSVGZFile(downloadUrl);	
    	return csvData;
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


