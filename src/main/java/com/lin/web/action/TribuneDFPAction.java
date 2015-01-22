package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.opensymphony.xwork2.Action;

public class TribuneDFPAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(TribuneDFPAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    String schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
	
	public String execute(){		
		log.info("TribuneDFP action executes..");			
	    return Action.SUCCESS;
	}
	
	/*
	 * DailyReoport for Tribune non-finalised data
	 */
	public String dailyReport(){		
		log.info("dailyTribuneReport action executes..");		
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");
		String adUnitId=request.getParameter("adUnitId");
		
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
	    	reportsResponse=runNonFinaliseReport(startDate, endDate,adUnitId);	     
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
		log.info("dfpNonFinaliseTribuneReport action starts...");
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
		log.info("action ends...reportsResponse:"+reportsResponse);
		return Action.SUCCESS;
  }
	
	/*
	 * update monthly finalise table for LinDigital non-finalised data
	 */
	public String updateMonthwiseFinaliseReport(){		
		log.info("dfpFinaliseTribuneReport action executes..");		
	    
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
				
			 }
		  }	
	    
		  log.info("action ends..");	
	      return Action.SUCCESS;
	}
	
	private String runNonFinaliseReport(String startDate,String endDate,String adUnitId){
    	String response="";
    	try {
    		String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
    		String publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
    		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
    		
	        IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);	    	
	    	String csvData=getDFPReportURL(service,startDate, endDate,adUnitId);
	    	if(csvData !=null){
	    		
				String projectId=LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID;
				String serviceAccountEmail=LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				String servicePrivateKey=LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				String bucketName=LinMobileVariables.TRIBUNE_CLOUD_STORAGE_BUCKET;
				String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				
				String tableId=schemaName+"_"+LinMobileConstants.DFP_DATA_SOURCE+"_"+startDate.replaceAll("-", "_");
				String schemaFile=LinMobileConstants.DFP_TARGET_PLATFORM_TABLE_SCHEMA;
				
				QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
						projectId, schemaFile, tableId, dataSetId, schemaName);
				
	    		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		    	String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		String rawFileName=timestamp+"_"+schemaName+"_"+startDate+"_"+endDate+"_raw"+".csv";    		
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"
    	                  +LinMobileConstants.TRIBUNE_DFP_REPORT_BUCKET+"/non_finalise/"
    	                  +month;
	    	    
	    		String reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, rawFileName,dirName,bucketName);
	    		log.info("Report saved :"+reportPathOnStorage);	    		
	    			    		
	    		response=service.generateFinilisedDFPReport(rawFileName,dirName,networkCode,
	    				publisherId,LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME,bucketName);
	    		
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
			
		 } catch (Exception e) {
			log.severe("DFP report exception: Exception :"+e.getMessage());
			response=e.getMessage();
			
		 }
    	return response;
	}
	
	@SuppressWarnings("deprecation")
	private String getDFPReportURL(IReportService service,String startDate, String endDate, String adUnitId)
			throws Exception{
		String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
		dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
				LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME);	
		log.info(" getting DfpServices instance from properties...");
		dfpServices = LinMobileProperties.getInstance().getDfpServices();
	
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<String> adUnitIdList = new ArrayList<String>();
	    if(adUnitId !=null){
	    	adUnitIdList.add(adUnitId);
	    }else{
	    	List<PropertyObj> propertyObjList = userService.getSiteIdsByBqIdentifier(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
			if(propertyObjList == null || propertyObjList.size() == 0){
				reportsResponse="No sites found for Tribune in datastore, please check AdUnitObj entity:";					
				throw new Exception(reportsResponse); 
			}
			for (PropertyObj propertyObj : propertyObjList) {
				String adUnitIdStr=propertyObj.getDFPPropertyId();
				if(!adUnitIdList.contains(adUnitIdStr)) {
					adUnitIdList.add(adUnitIdStr);
				}
			}
	    }
	    
		IDFPReportService dfpReportService=new DFPReportService();		
		String downloadUrl=dfpReportService.getDFPReportByAdUnitIds(dfpServices, dfpSession,startDate, 
				endDate, adUnitIdList);			
		log.info("DFP report downloadUrl:"+downloadUrl);
    	String csvData=dfpReportService.readCSVGZFile(downloadUrl);
    	return csvData;
	}
	
	public String dailyUpdateAdUnits(){
		log.info("dailyUpdateAdUnits......");
		String networkCode=LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE;
    	log.info(" now going to build dfpSession ...");
		try {
			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode,
					LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME);	
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IDFPReportService dfpReport=new DFPReportService();
			List<AdUnitHierarchy> adUnitHierarchyList= dfpReport.getRecentlyUpdatedAdUnitsHierarchy(dfpServices, dfpSession);
			if(adUnitHierarchyList !=null){
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				productService.saveAdUnitHierarchy(adUnitHierarchyList);
			}			
			
		} catch (ValidationException e) {
			log.severe("ValidationException :"+e.getMessage());
		} catch (GeneralSecurityException e) {
			log.severe("GeneralSecurityException :"+e.getMessage());
		} catch (IOException e) {
			log.severe("IOException :"+e.getMessage());
		} catch (Exception e) {
		   log.severe("ApiException_Exception :"+e.getMessage());			
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


