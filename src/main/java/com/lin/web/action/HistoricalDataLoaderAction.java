package com.lin.web.action;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.DFPTaskEntity;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.DataUploaderDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.IReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.HistoricalReportService;
import com.lin.web.servlet.GCSUtil;
import com.lin.web.util.CloudStorageUtil;
import com.lin.web.util.DataLoaderUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileErrorCodes;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;


/**
 * 
 * @author Manish Mudgal
 * 
 * This class is used to automate the historical data loading process for DFP. 
 * There may be campaigns in datastore for which there is no data in bigquery. 
 * For that matter this action is expected to be a generic solution .
 * 
 * This action takes the order id which is fetched from datastore and verified. 
 * It also expects the user(caller) to pass the network code and publisher id. 
 * 
 *
 */
public class HistoricalDataLoaderAction implements ServletRequestAware,
		SessionAware {

	private static final Logger log = Logger
			.getLogger(HistoricalDataLoaderAction.class.getName());

	private String reportsResponse;
	private HttpServletRequest request;
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private Map session;
	private DfpSession dfpSession;
	private DfpServices dfpServices;

	public String execute() {

		return Action.SUCCESS;
	}

	/**
	 * @author Manish Mudgal 
	 *  This method expects 
	 * 	dfpNetworkCode 
	 * 	orderId
	 * 	loadType (Optional)
	 * 	as request parameters. <br />
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String loadHistoricalData() throws Exception {/*
		int taskCount = 0;
		try{
		ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator
				.locate(ISmartCampaignPlannerService.class);

		String dfpNetworkCode = request.getParameter("dfpNetworkCode");
		String orderId = request.getParameter("orderId");
		
		String publisherIdInBQ=request.getParameter("publisherIdInBQ");
		String companyId=request.getParameter("companyId");
		
		
		if (dfpNetworkCode == null || dfpNetworkCode.trim().length() == 0) {
			log.severe("Invalid dfp network code received");
			return Action.ERROR;
		}

		if (orderId == null || orderId.trim().length() == 0) {
			log.severe("Invalid campaignId received");
			return Action.ERROR;
		}
		
        
        
		SmartCampaignObj smartCampaign = campaignPlannerService.loadSmartCampaignByOrderId(orderId, dfpNetworkCode);
		String loadType  = request.getParameter("loadType");
		if (smartCampaign != null) {
			
			companyId=smartCampaign.getCompanyId();
			
			if(companyId !=null && publisherIdInBQ==null){
	        	IUserService service = (IUserService) BusinessServiceLocator.locate(IUserService.class);
	        	CompanyObj companyObj = service.getCompanyById(StringUtil.getLongValue(companyId), MemcacheUtil.getAllCompanyList());
	        	publisherIdInBQ=companyObj.getBqIdentifier()+"";
	        }
			log.info("Load historical data :: publisherIdInBQ : "+publisherIdInBQ+", companyId : "+companyId+", orderId : "+orderId+", dfpNetworkCode : "+dfpNetworkCode);
			
			Date startDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY(smartCampaign.getStartDate()));
			Date endDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY(smartCampaign.getEndDate()));
			if (startDate == null || endDate == null) {
				log.severe("Start or End date are not valid for campaignID: "
						+ smartCampaign.getCampaignId() + " startDate:["
						+ smartCampaign.getStartDate() + "] AND endDate : ["
						+ smartCampaign.getEndDate() + "]");
				return Action.ERROR;
			}
		
			// if today is 22Oct2014, this date will be 19Oct2014
			Date historicalCapDate = DateUtil.getDateBeforeAfterDays(-(LinMobileConstants.CHANGE_WINDOW_SIZE));

			// Load all finalised data for this campaign and merge them with
			// existing table.
			if (startDate.before(historicalCapDate) || startDate.equals(historicalCapDate)) {
				String[][] dateArr = DateUtil.getMonthlyStartEndDates(startDate, endDate, "yyyy-MM-dd");
				for (int i = 0; i < dateArr.length; i++) {
					String startParamDate = dateArr[i][0];
					String endParamDate = dateArr[i][1];
					boolean endFound = false;
					if (DateUtil.getMonthOfDate(historicalCapDate) == DateUtil.getMonthOfDate(DateUtil.getDateYYYYMMDD(endParamDate))) {
						endParamDate = DateUtil.getDateAsString(historicalCapDate, "yyyy-MM-dd");
						endFound = true;
					}
					loadHistoricalData(dfpNetworkCode, orderId, publisherIdInBQ, startParamDate, endParamDate, loadType, true);
					taskCount ++;
					if (endFound) {
						break;
					}
				}

			}

			// load last two dates as well (non finalised) if this is the case.
			if ((endDate.after(historicalCapDate) || startDate.after(historicalCapDate))) {
				String date = DateUtil.getDateAsString(DateUtil.getDateBeforeAfterDays(-1), "yyyy-MM-dd");
				loadHistoricalData(dfpNetworkCode, orderId, publisherIdInBQ,date, date, loadType, false);
				date = DateUtil.getDateAsString(DateUtil.getDateBeforeAfterDays(-2), "yyyy-MM-dd");
				loadHistoricalData(dfpNetworkCode, orderId, publisherIdInBQ, date, date, loadType, false);
				taskCount += 2;
			}

		}
		}catch(Exception e){
			log.severe("Error occurred while loading historical data. "+ e.getMessage()); 
		}
		reportsResponse = "Total number of tasks added: "+ taskCount;
	*/
	
	
//			
 
 		
		return Action.SUCCESS;
	}
	
	public void startFullHistoricalLoad(){
		TaskQueueUtil.addFullHistoricalLoad("/executeFullHistoricalLoad.lin");
		reportsResponse = "Task initiated. Sit back and relax.";
	}
	
	public void executeFullHistoricalLoad(){
		String breaker = request.getParameter("break");
		List<DataUploaderDTO> dtoList = DataLoaderUtil.getDataUploaderDTO(100);
		for(DataUploaderDTO dto : dtoList){
				for(String orderIdcsv :dto.getOrderIdList()){
					log.info("executeFullHistoricalLoad");		
			try {
				for(String orderId: orderIdcsv.split(",")){
					TaskQueueUtil.startTaskForDataLoading("/runSmartDataLoader.lin", orderId, dto.getPublisherBQId()+"", dto.getDfpNetworkCode(), "common-task-id");
				log.info("Started task for adding all sub tasks for order ["+orderId+"]"); 
				}
				log.info("Finished all tasks");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}
				if(breaker != null){ return;}
				}
		}
	}

	@Deprecated
	private void loadHistoricalData(String networkCode, String orderId, String publisherIdInBQ, 
			String start, String end, String loadType, boolean merge) throws Exception {
		// If loadType not defined, load for all types.
		ArrayList<String> loadTypeList = new ArrayList<String>();
		if(loadType == null || loadType.trim().length() == 0){
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_LOCATION);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_RICH_MEDIA);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_TARGET);
		}else{
			loadTypeList.add(loadType);
		}
		
		
		for(String loadTypeElem : loadTypeList){
			
			TaskQueueUtil.addTaskInForHistoricalData("/runHistoricalDataTask.lin", start, end, orderId, publisherIdInBQ, networkCode, loadTypeElem, merge);
		}
	}
	

	
	public String loadFinalizeNonFinalizeDataAsTask() {
		boolean merge = request.getParameter("merge") != null && request.getParameter("merge").trim().equals("true");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String orderId = request.getParameter("orderId");
		String publisherIdInBQ = request.getParameter("publisherIdInBQ");
		String networkCode = request.getParameter("networkCode"); 
		String loadTypeElem = request.getParameter("loadTypeElem");
		try {
			if(merge){
				//reportsResponse=runFinalisedReportForHistoricalData(start, end, orderId, publisherIdInBQ, networkCode, loadTypeElem);
				reportsResponse=runFinalisedReportForHistoricalData(start, end, orderId, publisherIdInBQ, networkCode, loadTypeElem);
			}
			// Data is for period after historicalDate cap
			else{
				reportsResponse=runNonFinaliseReportForHistoricalData(start, end , orderId, publisherIdInBQ, networkCode, loadTypeElem);
			}
		} catch (Exception e) {
			reportsResponse="Failed to upload data : exception :"+e.getMessage();
			
		}
		
		log.info("action ends : "+reportsResponse);
		return Action.SUCCESS;
	}
	

	private String runNonFinaliseReportForHistoricalData(String startDate, String endDate, String orderId, String publisherIdInBQ,
			String networkCode, String loadType)throws Exception {

		String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadType);
		String response = "";
		try {

			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();

			IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
			IDFPReportService dfpReportService = new DFPReportService();

			List<String> orderIdList = null;
			orderIdList = new ArrayList<String>();
			orderIdList.add(orderId);

			// Run report search tool on DFP and get the download URL of dfp
			// site.
			
			String downloadUrl = downloadDFPReportUsingSchema(dfpServices, dfpSession, dfpReportService,loadType,
								startDate, endDate, orderIdList);
			log.info("Download URL:" + downloadUrl);

			// Read file from URL, split it if size is more than
			// record-count-allowed and build a list of string(file content)
			List<String> csvDataList = dfpReportService.readCSVGZFileAndSplit(downloadUrl);

			if (csvDataList != null && csvDataList.size() > 0) {
				log.info("Going to process these raw files..");

				// Initialize default parameters based on network code and load
				// type
				CloudProjectDTO cloudProjectBQDTO=DataLoaderUtil.getCloudProjectDTO(publisherIdInBQ);
				if(cloudProjectBQDTO ==null){
					log.severe("Invalid bqIdentifier -- no cloud project configured for publisherIdInBQ :"+publisherIdInBQ);
					throw new Exception("Invalid bqIdentifier -- no cloud project configured for publisherIdInBQ :"+publisherIdInBQ);
				}
				log.info("Found cloudProjectBQDTO : "+cloudProjectBQDTO.toString());
				String projectId = cloudProjectBQDTO.getBigQueryProjectId();
				String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
				String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
				String bucketName = cloudProjectBQDTO.getCloudStorageBucket(); //DataLoaderUtil.getCloudStorageBucket(networkCode);
				String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				String tableId = schemaName + "_" + LinMobileConstants.DFP_DATA_SOURCE + "_" + startDate.replaceAll("-", "_");
				String schemaFile = DataLoaderUtil.getSchemaFileByLoadType(loadType);
				QueryDTO bigQueryDTO = new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId, schemaFile,
										tableId, dataSetId, schemaName);
				String timestamp = DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
				String month = DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
				String rawFileName = timestamp + "_" + schemaName + "_" + startDate + "_" + 
										endDate + "_" + orderId + "_" + "raw" + ".csv";
				String procFileName = rawFileName.replace("_raw", "_proc");
			
				
				String dirName = LinMobileConstants.REPORT_FOLDER  + "/historical/" + orderId+"/"+schemaName+ "/" + month;
				String reportPathOnStorage = "";
				String splitRawFileName = "";

				// This empty list will eventually hold processed file list.  DFP
				// gives us raw data while processing will give us processed
				// data as
				// per the big query schema.
				List<String> processedFilePathList = new ArrayList<>();

				for (int i = 0; i < csvDataList.size(); i++) {
					String csvData = csvDataList.get(i);
					if (csvDataList.size() > 1) {
						splitRawFileName = rawFileName.replace("_raw", "_raw_"+ i);
					} else {
						splitRawFileName = rawFileName;
					}
					procFileName = splitRawFileName.replace("_raw", "_proc");

					// Read content of each set of csv data and upload that to
					// cloud storage with a file name.
					// (appending 'i' to the file name). This is still raw.
					log.info("Upload raw file on cloud storage : splitRawFileName :"+splitRawFileName);
					reportPathOnStorage = ReportUtil.uploadReportOnCloudStorage(csvData,splitRawFileName, dirName, bucketName);
					log.info(" raw report saved on cloud storage :" + reportPathOnStorage);
					List<String> tempProcessedFilePathList = processRawFiles(loadType, service, splitRawFileName, 
							procFileName, dirName, networkCode, bucketName,publisherIdInBQ);
					log.info("tempProcessedFilePathList : "+tempProcessedFilePathList);
					processedFilePathList.addAll(tempProcessedFilePathList);
				}
				log.info("all processedFilePathList:" + processedFilePathList.size());

				// Convert processed file paths list to comma separated string.
				String processedFilePath = "";
				String bqResponse = "";
				String id = publisherIdInBQ + ":" + tableId;
				for (int i = 0; i < processedFilePathList.size(); i++) {
					response = processedFilePathList.get(i);
					if (i == 0) {
						processedFilePath = response;
					} else {
						processedFilePath = processedFilePath + "," + response;
					}
				}

				// Pass the csv file paths to bigquery and upload the data.
				FinalisedTableDetailsObj reportObject = new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
						timestamp, Integer.parseInt(publisherIdInBQ), LinMobileConstants.DFP_DATA_SOURCE, processedFilePath,
						projectId, LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID, 0);
				bqResponse = service.uploadProcessFileAtBQ(reportObject, 0,processedFilePathList, bigQueryDTO);
				response = processedFilePathList + " and bigQuery response:: " + bqResponse;

			} else {
				log.warning("Failed to create report, please check log ....");
				response = "Failed to create report";
			}
		} catch (IOException e) {
			log.severe("IOException :" + e.getMessage());
			response = response + " : " + e.getMessage();
		}
		return response;

	}

	
	private String runFinalisedReportForHistoricalData(String startDate, String endDate, String orderId,String publisherIdInBQ,
			String networkCode, String loadType) throws Exception {
		String response = "";
		IReportService reportService = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		
		String id=publisherIdInBQ+":"+DataLoaderUtil.getSchemaNameByLoadType(loadType)+":" + orderId+":"+
							startDate.replaceAll("-", "_")+":"+endDate.replaceAll("-", "_");
		FinalisedTableDetailsObj tableObj=reportService.loadFinaliseNonFinaliseObject(id);
		
		if(tableObj !=null && tableObj.getMergeStatus()==1){			
			log.warning("This table has already been merged to finalised table..");
			response="This table has already been merged with datastore id:"+id;
			return response;
		}
		// Get bigquery schema for this load type.
		String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadType);
		try {
			
			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
			IDFPReportService dfpReportService = new DFPReportService();

			List<String> orderIdList = null;
			orderIdList = new ArrayList<String>();
			orderIdList.add(orderId);

			// Run report search tool on DFP and get the download URL of dfp
			// site.
			String downloadUrl = downloadDFPReportUsingSchema(dfpServices, dfpSession, dfpReportService,loadType, startDate, endDate, orderIdList);
			
			log.info("Download URL:" + downloadUrl);

			
			// Initialize default parameters based on network code and load
			// type
			CloudProjectDTO cloudProjectBQDTO=DataLoaderUtil.getCloudProjectDTO(publisherIdInBQ);
			if(cloudProjectBQDTO ==null){
				throw new Exception("Invalid bqIdentifier -- no cloud project configured for publisherIdInBQ :"+publisherIdInBQ);
			}
			log.info("Found cloudProjectBQDTO : "+cloudProjectBQDTO.toString());
			String projectId = cloudProjectBQDTO.getBigQueryProjectId();
			String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
			String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
			String bucketName = cloudProjectBQDTO.getCloudStorageBucket(); //DataLoaderUtil.getCloudStorageBucket(networkCode);
			String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			String schemaFile = DataLoaderUtil.getSchemaFileByLoadType(loadType);
			String timestamp = DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
			String month = DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
			String rawFileName = timestamp + "_" + schemaName + "_" + startDate + "_" + endDate + "_" + orderId + "_" + "raw" + ".csv";
			String procFileName = rawFileName.replace("_raw", "_proc");
			String dirName = LinMobileConstants.REPORT_FOLDER 
								+ "/historical/"
								+ orderId+"/"+schemaName+ "/"
								+ month;
			
			// Read file from URL, split it if size is more than
			// record-count-allowed and build a list of string(file content)
			List<String> rawCsvFileList = ReportUtil.readCSVGZFileSplitAndUploadGCS(downloadUrl, rawFileName, dirName, bucketName);
			List<String> processedFilePathList = new ArrayList<>();
			if (rawCsvFileList != null && rawCsvFileList.size() > 0) {
				log.info("Going to process these raw files..");
				// This empty list will eventually hold processed file list. DFP
				// gives us raw data while processing will give us processed
				// data as per the big query schema.
				

				for (String splitRawFileName:rawCsvFileList) {					
					procFileName = splitRawFileName.replace("_raw", "_proc");					
					log.info("Process raw file -- "+splitRawFileName);				
					List<String> tempProcessedFilePathList = processRawFiles(loadType, service, splitRawFileName, 
							procFileName, dirName, networkCode, bucketName,publisherIdInBQ);
					log.info("tempProcessedFilePathList : "+tempProcessedFilePathList);
					processedFilePathList.addAll(tempProcessedFilePathList);
				}
				log.info("processedFilePathList:" + processedFilePathList.size());

				// Convert processed file paths list to comma separated string.
				String processedFilePath = "";
				for (int i = 0; i < processedFilePathList.size(); i++) {
					response = processedFilePathList.get(i);
					if (i == 0) {
						processedFilePath = response;
					} else {
						processedFilePath = processedFilePath + "," + response;
					}
				}				
				
				// find the name of the table in which the data will be appended.
				String finaliseTableId = schemaName + "_" + month.replaceAll("-", "_");
				//String id = publisherIdInBQ + ":" + finaliseTableId+":"+orderId+":"+startDate+":"+endDate;
				int finalise = 1;
				String dataSource = LinMobileConstants.DFP_DATA_SOURCE;
				FinalisedTableDetailsObj reportObject = new FinalisedTableDetailsObj(id, finaliseTableId, finalise, startDate, endDate,
														timestamp, Integer.parseInt(publisherIdInBQ), dataSource,
														processedFilePath, projectId, dataSetId, 0);
				QueryDTO bigQueryDTO = new QueryDTO(serviceAccountEmail,servicePrivateKey, projectId, schemaFile, finaliseTableId, dataSetId, schemaName);
				reportsResponse = service.uploadProcessFileAtBQ(reportObject, finalise, processedFilePathList, bigQueryDTO);
				log.info("uploaded processed file at bq table :reportsResponse:"+ reportsResponse);

				log.info("This table has been merged to finalised table successfully..: readyToMergeTableId:"+id);
				
			} else {
				log.warning("Failed to create report, please check log ....");
				response = "Failed to create report";
			}
		} catch (IOException e) {
			log.severe("IOException :" + e.getMessage());
			response = response + " : " + e.getMessage();
		}
		return response;

	}
	
	public String downloadDFPReportUsingSchema(DfpServices dfpServices,DfpSession dfpSession, IDFPReportService dfpReportService,
			String loadType, String startDate, String endDate, List<String> orderIdList) throws Exception{
		String downloadUrl = null;
		
		switch (loadType) {
			case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
				downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
				break;
			case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
				downloadUrl=dfpReportService.getRichMediaCustomEventReportByOrderId(dfpServices, dfpSession, 
						startDate, endDate, orderIdList);
				break;
			case LinMobileConstants.LOAD_TYPE_LOCATION:
				downloadUrl=dfpReportService.getDFPReportByLocationByOrderIds(dfpServices, dfpSession, startDate, 
						endDate, orderIdList);
				break;
			case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
				downloadUrl=dfpReportService.getRichMediaVideoCampaignReportByByOrderIds(dfpServices, dfpSession, 
						startDate, endDate, orderIdList);
				break;
			case LinMobileConstants.LOAD_TYPE_TARGET:
				downloadUrl=dfpReportService.getDFPTargetReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList);
				break;	
			default:
				break;
		}
		 return downloadUrl;
	}
	 
	private List<String> processRawFiles(String loadType,IHistoricalReportService service,String splitRawFileName,
			String procFileName, String dirName, String networkCode,String bucketName,String publisherIdInBQ){
		List<String> processedFilePathList = new ArrayList<>();
		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		String processFilePath=null;
		switch (loadType) {
			case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:				
				processFilePath=service.generateCorePerformanceReport(splitRawFileName,dirName,networkCode,
						publisherIdInBQ,DataLoaderUtil.getPublisherName(publisherIdInBQ),bucketName);
				if(processFilePath !=null){
					processedFilePathList.add(processFilePath);
				}	
				
				break;
			case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:				
				processFilePath=service.generateRichMediaCustomEventReport(splitRawFileName,procFileName,
	    				dirName,dataSource,networkCode,publisherIdInBQ,
	    				DataLoaderUtil.getPublisherName(publisherIdInBQ),bucketName);
				if(processFilePath !=null){
					processedFilePathList.add(processFilePath);
				}
				
				break;
			case LinMobileConstants.LOAD_TYPE_LOCATION:
				processedFilePathList = service.generateDFPReportByLocation(splitRawFileName,procFileName, dirName, networkCode,
						publisherIdInBQ,
						DataLoaderUtil.getPublisherName(publisherIdInBQ), bucketName);
				
				break;
			case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
				processFilePath=service.generateRichMediaVideoCampaignReport(splitRawFileName,procFileName,
	    				dirName,dataSource,networkCode,publisherIdInBQ,
	    				DataLoaderUtil.getPublisherName(publisherIdInBQ),bucketName);
				if(processFilePath !=null){
					processedFilePathList.add(processFilePath);
				}		
				
				break;
			case LinMobileConstants.LOAD_TYPE_TARGET:
				processedFilePathList = service.generateDFPTargetReport(splitRawFileName,procFileName, dirName, networkCode,
						publisherIdInBQ,
						DataLoaderUtil.getPublisherName(publisherIdInBQ), bucketName);
				break;	
			default:
				break;
		
			
		}
		
		return processedFilePathList;
	}
	
	
	public String loadHistoricalDataForDataStoreCampaigns() {
		try{
			ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator
					.locate(ISmartCampaignPlannerService.class);

			String dfpNetworkCode = request.getParameter("dfpNetworkCode");
			String startCount = request.getParameter("startCount");
			
			String endCount=request.getParameter("endCount");
			
			
			if (dfpNetworkCode == null || dfpNetworkCode.trim().length() == 0) {
				log.severe("Invalid dfp network code received");
				return Action.ERROR;
			}

			if (startCount == null || startCount.trim().length() == 0) {
				log.severe("Invalid campaignId received");
				return Action.ERROR;
			}
			
			if (endCount == null || endCount.trim().length() == 0) {
				log.severe("Invalid campaignId received");
				return Action.ERROR;
			}
			
			boolean status = campaignPlannerService.loadHistoricalDataForDataStoreCampaigns(dfpNetworkCode, startCount, endCount);
			
		}catch(Exception e){
			log.severe("Exception ::"+e.getMessage());
		}
		
		return Action.SUCCESS;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setSession(Map session) {
		this.session = session;

	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}

	public String getReportsResponse() {
		return reportsResponse;
	}
	
	
	
	

	 
	/**
	 * this method is only for loading historical data of a particular order
	 * @return
	 * @throws Exception
	 */
	public String loadSmartDataFromDFP()throws Exception {
//		public static List<DataUploaderDTO> getDataUploaderDTO(int orderListSize){
		String orderId = request.getParameter("orderId"), 
		publisherIdInBQ  = request.getParameter("publisherIdInBQ"), 
		dfpNetworkCode = request.getParameter("dfpNetworkCode") , 
		loadType = request.getParameter("loadType"),
		dfpTaskId = request.getParameter("dfpTaskId");
		
		boolean isHistorical = true;
		
		int taskCount = 0;
		try{
		ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
	
		
		String companyId=request.getParameter("companyId");
		
		
		if (dfpNetworkCode == null || dfpNetworkCode.trim().length() == 0) {
			log.severe("Invalid dfp network code received");
			return Action.ERROR;
		}

		if (orderId == null || orderId.trim().length() == 0) {
			log.severe("Invalid campaignId received");
			return Action.ERROR;
		}
//		String []  orders = orderIds.split(",");
	 	SmartCampaignObj smartCampaign = campaignPlannerService.loadSmartCampaignByOrderId(orderId, dfpNetworkCode);
		if (smartCampaign != null) {
			if(smartCampaign.isHistoryLoaded()){
				log.info("Historical Data is already loaded for this campaign ["+smartCampaign.getDfpOrderId()+"]");
				return Action.SUCCESS;
			}
			companyId=smartCampaign.getCompanyId();
			
			if(companyId !=null && publisherIdInBQ==null){
	        	IUserService service = (IUserService) BusinessServiceLocator.locate(IUserService.class);
	        	CompanyObj companyObj = service.getCompanyById(StringUtil.getLongValue(companyId), MemcacheUtil.getAllCompanyList());
	        	publisherIdInBQ=companyObj.getBqIdentifier()+"";
	        }
			log.info("Load historical data :: publisherIdInBQ : "+publisherIdInBQ+", companyId : "+companyId+", orderId : "+orderId+", dfpNetworkCode : "+dfpNetworkCode);
			
			Date startDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY(smartCampaign.getStartDate()));
			Date endDate = DateUtil.getAbsoluteDate(DateUtil.getDateMMDDYYYY(smartCampaign.getEndDate()));
			
			// To avoid making a large array if end date is too way far like 2099
			if(endDate.after(new Date())){
				endDate = DateUtil.getLastDateOfMonth(new Date());
			}
			if (startDate == null || endDate == null) {
				log.severe("Start or End date are not valid for campaignID: "
						+ smartCampaign.getCampaignId() + " startDate:["
						+ smartCampaign.getStartDate() + "] AND endDate : ["
						+ smartCampaign.getEndDate() + "]");
				return Action.ERROR;
			}
		
			// if today is 22Oct2014, this date will be 19Oct2014
			Date historicalCapDate = DateUtil.getDateBeforeAfterDays(-(LinMobileConstants.CHANGE_WINDOW_SIZE));

			// Load all finalised data for this campaign and merge them with
			// existing table.
			if (startDate.before(historicalCapDate) || startDate.equals(historicalCapDate)) {
				String[][] dateArr = DateUtil.getMonthlyStartEndDates(startDate, endDate, "yyyy-MM-dd");
				for (int i = 0; i < dateArr.length; i++) {
					String startParamDate = dateArr[i][0];
					String endParamDate = dateArr[i][1];
					boolean endFound = false;
					if (DateUtil.getYearOfDate(historicalCapDate) == DateUtil.getYearOfDate(DateUtil.getDateYYYYMMDD(endParamDate))
							&&
							DateUtil.getMonthOfDate(historicalCapDate) == DateUtil.getMonthOfDate(DateUtil.getDateYYYYMMDD(endParamDate))) {
						endParamDate = DateUtil.getDateAsString(historicalCapDate, "yyyy-MM-dd");
						endFound = true;
					}
					loadAdvanceHistoricalData(dfpNetworkCode, orderId, publisherIdInBQ, startParamDate, endParamDate, loadType, isHistorical);
					if (endFound) {
						break;
					}
				}

			}

			// load last two dates as well (non finalised) if this is the case.
			if ((endDate.after(historicalCapDate) || startDate.after(historicalCapDate))) {
				String date = DateUtil.getDateAsString(DateUtil.getDateBeforeAfterDays(-1), "yyyy-MM-dd");
				loadAdvanceHistoricalData(dfpNetworkCode, orderId, publisherIdInBQ,date, date, loadType, isHistorical);
				date = DateUtil.getDateAsString(DateUtil.getDateBeforeAfterDays(-2), "yyyy-MM-dd");
				loadAdvanceHistoricalData(dfpNetworkCode,  orderId , publisherIdInBQ, date, date, loadType, isHistorical);
			}
		}
		}catch(Exception e){
			log.severe("Error occurred while loading historical data. "+ e.getMessage()); 
		}
		
		
		reportsResponse = "Total number of tasks added: "+ taskCount;
		return Action.SUCCESS;
	
	}
	
	private void loadAdvanceHistoricalData(String networkCode, String orderId, String publisherIdInBQ,  String start, String end, String loadType, boolean historical) throws Exception {
		// If loadType not defined, load for all types.
		ArrayList<String> loadTypeList = new ArrayList<String>();
 		if(loadType == null || loadType.trim().length() == 0){
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_LOCATION);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_RICH_MEDIA);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_TARGET);
		}else{
			loadTypeList.add(loadType);
		}
		
		String dfpTaskkey = System.currentTimeMillis()+"_"+ new BigInteger(50, new SecureRandom()).toString(32);
		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
		
		for(String loadTypeElem : loadTypeList){
			DFPTaskEntity entity = service.saveInProgressTask("not-generated", networkCode, start, end, dfpTaskkey, loadTypeElem, orderId);
			String taskName = TaskQueueUtil.addSmartDataLoaderTask("/runSmartDataLoaderTask.lin", start, end, 
					orderId, publisherIdInBQ, networkCode, loadTypeElem, historical, dfpTaskkey, (entity.getId() == null ? 0 : entity.getId()));
			entity.setTaskName(taskName);
			service.saveOrUpdateTask(entity);
 		}
	}

	public void loadSmartDataFromDFPTask()throws Exception {
		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);		

		String startDate = request.getParameter("startDate"),
		endDate= request.getParameter("endDate"), 
 		orderIds= request.getParameter("orderIds"),
		publisherIdInBQ= request.getParameter("publisherIdInBQ"),
		networkCode= request.getParameter("networkCode"), 
		entityId = request.getParameter("entityId"), 
		loadType= request.getParameter("loadType"),
		dfpTaskKey = request.getParameter("dfpTaskKey");
		boolean merge =  request.getParameter("merge") != null && request.getParameter("merge").equals("true"); 
		log.info("starting data loading for parameters : startDate = ["+startDate+"] "
				+ "and endDate = ["+endDate+"] and "
				+ "orderIds = ["+orderIds +"] and publisherIdBQ = ["+publisherIdInBQ+"] and "
				+ "networkCode = ["+networkCode+"] and dfpTaskKey = ["+dfpTaskKey+"] and "
				+ "loadType = ["+loadType+"] and merge = ["+merge+"] and "
				+ "entityId = ["+entityId+"].");
		
		String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadType);
		log.info("schema name is : ["+schemaName+"]");
		try {

			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();

			IDFPReportService dfpReportService = new DFPReportService();
			List<DFPTaskEntity> taskEntitieList = new ArrayList<>();
			List<String> orderIdList = Arrays.asList(orderIds.split(","));

			CloudProjectDTO cloudProjectBQDTO=DataLoaderUtil.getCloudProjectDTO(publisherIdInBQ);
			String uploadedFileURL = null;
			try{
				log.info("Going to download report");
				
				String downloadUrl = downloadDFPReportUsingSchema(dfpServices, dfpSession, dfpReportService,loadType, startDate, endDate, orderIdList);
				log.info("report downloaded with URL ["+downloadUrl+"]");
				uploadedFileURL = CloudStorageUtil.downloadReportFileFromDFP(cloudProjectBQDTO, schemaName, startDate, publisherIdInBQ, endDate, loadType, downloadUrl);
				log.info("report gz file downloaded at location ["+uploadedFileURL+"]"); 
			}catch(Exception e){
				log.info("Exception occurred while downloading and saving gz report. ["+e.getMessage()+"]");
				throw new Exception ("Exception with error code ["+LinMobileErrorCodes.DFP_REPORT_DOWNLOAD_FAILED+"]");
			}	
			String rawTableId = null;
			try{
				rawTableId= createRawTable(cloudProjectBQDTO, loadType, uploadedFileURL, startDate, endDate, orderIds);
				DFPTaskEntity entity = service.getDFPTaskEntity(entityId);
				entity.setRawTableId(rawTableId);
				
				synchronized (HistoricalDataLoaderAction.class) {
					service.saveOrUpdateTask(entity);
					taskEntitieList = service.getDFPTaskEntityByTaskKey(dfpTaskKey);
				}
				 
			}catch(Exception e){
				log.severe(e.getMessage());
				throw new Exception("Exception with error code ["+LinMobileErrorCodes.RAW_TABLE_CREATE_FAILED+"]");
				
			}
			
			try{
				if(merge){
				String result = createProcTable(cloudProjectBQDTO, rawTableId, loadType, networkCode, publisherIdInBQ, DataLoaderUtil.getPublisherName(publisherIdInBQ), merge, startDate, entityId);
				log.info("Proc table creation result is ["+result  == null || result.length()==0 ? "success " : result+"]");
				//TaskQueueUtil.addBigQueryJobUpdateTask("/checkBigQueryJob.lin", entityId, publisherIdInBQ, jobId);
				}else{
					if(taskEntitieList!=null && taskEntitieList.size()>0){
						log.info("no of task found for dfpTaskKey "+dfpTaskKey+" is "+taskEntitieList.size());
						List<String> rawTableIdList = new ArrayList<>();
						for (DFPTaskEntity dfpTaskEntity : taskEntitieList) {
							log.info("dfpTaskEntity : "+dfpTaskEntity);
							if(dfpTaskEntity.getRawTableId()==null || dfpTaskEntity.getRawTableId().equals("") ){
								log.info("empty rawTableID for for taskName = "+dfpTaskEntity.getTaskName()+" entity id = ["+dfpTaskEntity.getId()+"] time = "+ new Date());
								return;
							}
						}
						log.info("AllTasksDoneForRaw");
						int mergeCount = 0;
						if(taskEntitieList!=null && taskEntitieList.size()>0){
							for (DFPTaskEntity dfpTaskEntity : taskEntitieList) {
								String result = createProcTableForDailyAndHistorical(cloudProjectBQDTO, dfpTaskEntity.getRawTableId(), loadType, networkCode, publisherIdInBQ, DataLoaderUtil.getPublisherName(publisherIdInBQ), mergeCount!=0, startDate, dfpTaskEntity.getId().toString(),mergeCount);
								log.info("Proc table creation result is ["+result  == null || result.length()==0 ? "success " : result+"]");
								mergeCount++;
							}
						}else{
							log.info("rawTableIdList found null or empty");
						}
						
					}
				}
			}catch(Exception e){
				log.severe(e.getMessage());
				throw new Exception("Exception with error code ["+LinMobileErrorCodes.RAW_TO_PROC_FAILED+"]");
			}	
		
		}catch(Exception e){
			log.severe("Exception in uploading report"+ e.getMessage());
			
			DFPTaskEntity entity = service.getDFPTaskEntity(entityId);
			entity.setStatus(DFPTaskEntity.STATUS_FAILED);
			service.saveOrUpdateTask(entity); 
		}
	}

	
	public String  createRawTable(CloudProjectDTO cloudProjectBQDTO, String loadType, String uploadedFileUrl, String startDate, String endDate, String orderIds) throws GeneralSecurityException, IOException{
	    String tableId = "_raw"+ ((orderIds != null && !orderIds.contains(",")) ? "_order_"+orderIds+"_" : "daily" ) +"_"+loadType+"_"+startDate.replace("-", "_")+"_"+endDate.replace("-", "_")+"_"+System.currentTimeMillis();
	    log.info("Going to insert in table with id ["+tableId+"]");
	    String projectId = cloudProjectBQDTO.getBigQueryProjectId();
		String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
		String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
		String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_RAW_DATASET_ID;
	    String queryResponse = BigQueryUtil.uploadRawGzipFileToBigQuery(uploadedFileUrl, tableId, loadType, serviceAccountEmail, servicePrivateKey, projectId, dataSetId);
	    log.info("Job finished at bigquery. Query Response is ["+queryResponse+"]");
	    return tableId;
	}
	
	
	public String createProcTable(CloudProjectDTO cloudProjectBQDTO,    String rawTableId,  String loadType,
			String networkCode,String  publisherId, String publisherName, boolean merge, String startDate, String taskId
			) throws Exception{ 
			return createProcTableForDailyAndHistorical(cloudProjectBQDTO, rawTableId, loadType, networkCode, publisherId, publisherName, merge, startDate, taskId, -1);
	}
	
	public String createProcTableForDailyAndHistorical(CloudProjectDTO cloudProjectBQDTO,    String rawTableId,  String loadType,
			String networkCode,String  publisherId, String publisherName, boolean merge, String startDate, String taskId, int count
			) throws Exception{ 
			
			String projectId = cloudProjectBQDTO.getBigQueryProjectId();
			String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
			String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
			String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_RAW_DATASET_ID;
			String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadType);
			String month = DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
			//String tableId  = schemaName + "_" + LinMobileConstants.DFP_DATA_SOURCE + "_" + startDate.replaceAll("-", "_");
			String tableId = schemaName + "_" + DFPReportService.getDFPDataSourceByDFPNetworkCode(networkCode) + "_" + startDate.replaceAll("-", "_");
			IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);		

			if(merge && count < 0){
			 tableId  = schemaName + "_" + month.replaceAll("-", "_");
			}
			String result = null;
			try {
				
				result = BigQueryUtil.processRawData(serviceAccountEmail, servicePrivateKey, rawTableId, tableId, loadType, projectId, 
				dataSetId, networkCode, publisherId, publisherName, merge);
			} catch (IOException | InterruptedException e) {
				 log.severe("error while creating raw to proc "+ e.getMessage());
			}
			try{
			DFPTaskEntity entity = service.getDFPTaskEntity(taskId);
			entity.setStatus(result != null && result.length() > 0 ? DFPTaskEntity.STATUS_FAILED : DFPTaskEntity.STATUS_COMPLETED);
			entity.setErrorDesc(result);
			
			service.saveOrUpdateTask(entity);
			}catch(Exception e){
				 log.severe("Error while updating task in checkBigQueryJob taskId is ["+taskId+"]. "+e.getMessage());
				 
				
			}
		return result;
		
	}
	
	public void checkBigQueryJob(){
		String publisherIdInBQ  = request.getParameter("publisherIdInBQ");
		String jobId = request.getParameter("jobId");
		String taskId = request.getParameter("taskId");
		CloudProjectDTO cloudProjectBQDTO=DataLoaderUtil.getCloudProjectDTO(publisherIdInBQ);
		String projectId = cloudProjectBQDTO.getBigQueryProjectId();
		String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
		String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
		String result = null;
		
		
	}
}
