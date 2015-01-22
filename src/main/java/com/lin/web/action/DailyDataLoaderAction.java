package com.lin.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.DFPTaskEntity;
import com.lin.server.bean.DailyDataProcessObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.ProcessFileObj;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.DataUploaderDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DataLoaderUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;

public class DailyDataLoaderAction implements ServletRequestAware,
SessionAware {
	
	private static final Logger log = Logger
			.getLogger(HistoricalDataLoaderAction.class.getName());
	
	public enum ProcessFileStatusEnum {
		Process, Error, Uploaded
	}
	
	private String reportsResponse;
	private HttpServletRequest request;
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private Map session;
	private DfpSession dfpSession;
	private DfpServices dfpServices;

//	public static Map<String,Integer> processDataKeyFileMap = new HashMap<String, Integer>();
	

	public String execute() {

		return Action.SUCCESS;
	}
	
	public String loadDailyData() throws Exception{
		
		log.info("DailyDataLoaderAction action executes..");	
		String taskURL = "/addDailyDataTask.lin";
	    boolean dateCheck=true;
		String startDate=request.getParameter("start");		
		String endDate=request.getParameter("end");		
		String loadType  = request.getParameter("loadType");
		String taskType = LinMobileConstants.DAILY_TASK_TYPE;
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
			 if(loadType==null){
				 loadType = "";
			 }
		    TaskQueueUtil.addDailyDataTask(taskURL, startDate, endDate, loadType,taskType);
		   }else{
			   reportsResponse="Invalid dates...";
			   log.info(reportsResponse);
		   }
		   return Action.SUCCESS;
		
	}

	/*
	 * This action will reload all non-Finalise tables again once in a day
	 */
	public String updateDailyNonFinaliseData(){
		log.info("updateDailyNonFinaliseData action executes..");	
		String taskURL = "/addDailyDataTask.lin";
		String taskType = LinMobileConstants.NON_FINALISE_TASK_TYPE;
		Date currentDate=new Date();
		int i=1;
		String loadType  = request.getParameter("loadType");
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String startDate=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy-MM-dd");
			String endDate=startDate;
			log.info("startDate:"+startDate+" and endDate:"+endDate);
			if(loadType==null){
				 loadType = "";
			 }
			TaskQueueUtil.addDailyDataTask(taskURL, startDate, endDate, loadType,taskType);
			i++;
		}
		return Action.SUCCESS;
	}
	
/*	public void loadDailyDataTasks() throws Exception {
		log.info("inside loadDailyDataTasks");
		String start=request.getParameter("startDate");		
		String end=request.getParameter("endDate");		
		String loadType  = request.getParameter("loadType");
		
		ArrayList<String> loadTypeList = new ArrayList<String>();
		if(loadType == null || loadType.trim().length() == 0 || loadType.equals("")){
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_LOCATION);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_RICH_MEDIA);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_TARGET);
		}else{
			loadTypeList.add(loadType);
		}
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		Integer  progressTaskCount = 0;
		List<DataUploaderDTO> DataUploaderDTOList = DataLoaderUtil.getDataUploaderDTO(LinMobileConstants.DAILY_REPORT_ORDER_COUNT);
		if(DataUploaderDTOList!=null && DataUploaderDTOList.size()>0){
			for(DataUploaderDTO dataUploaderDTO : DataUploaderDTOList){
				for(String loadTypeElem : loadTypeList){
					if(dataUploaderDTO != null  && dataUploaderDTO.getOrderIdList() != null && dataUploaderDTO.getOrderIdList().size() > 0){
						String processKey = loadTypeElem+"_"+start+"_"+end+"_"+dataUploaderDTO.getDfpNetworkCode()+"_"+dataUploaderDTO.getPublisherBQId();
						DailyDataProcessObj dailyDataProcessObj = new DailyDataProcessObj();
						dailyDataProcessObj = service.getDailyDataProcessObj(processKey);
						if(dailyDataProcessObj!=null){
							progressTaskCount = dailyDataProcessObj.getTaskCount();
						}
						if(progressTaskCount == null || progressTaskCount<=0){
							 dailyDataProcessObj = new DailyDataProcessObj(processKey, processKey, dataUploaderDTO.getOrderIdList().size());
							service.saveDailyDataProcessObj(dailyDataProcessObj,processKey);
							for(String orderIds : dataUploaderDTO.getOrderIdList() ){
								TaskQueueUtil.dailyDataUpload("/runDailyDataTask.lin", start, end, orderIds, loadTypeElem, 
											dataUploaderDTO.getDfpNetworkCode(), dataUploaderDTO.getPublisherBQId()+"", dataUploaderDTO.getPublisherName(),processKey);
								log.info("Task added for orderIDs : "+orderIds+ " for loadtype : "+loadTypeElem+", processKey : "+processKey+", taskCount : "+progressTaskCount+", processDataKeyFileMap : "+processDataKeyFileMap);
								//Thread.sleep(2000);
							}
						}else{
							log.warning("tasks already in running for key : "+processKey);
						}
					}else {
						log.info("getOrderIdList is Empty : "+dataUploaderDTO);
					}
				}
			}
		}

	}*/
	
	public void loadDailyDataTasks() throws Exception {
		log.info("inside loadDailyDataTasks");
		String start=request.getParameter("startDate");		
		String end=request.getParameter("endDate");		
		String loadType  = request.getParameter("loadType");
		String taskType = request.getParameter("taskType");
		boolean historical = false;
		ArrayList<String> loadTypeList = new ArrayList<String>();
		if(loadType == null || loadType.trim().length() == 0 || loadType.equals("")){
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_LOCATION);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_RICH_MEDIA);
			loadTypeList.add(LinMobileConstants.LOAD_TYPE_TARGET);
		}else{
			loadTypeList.add(loadType);
		}
		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
		List<DataUploaderDTO> DataUploaderDTOList = DataLoaderUtil.getDataUploaderDTO(LinMobileConstants.DAILY_REPORT_ORDER_COUNT);
		if(DataUploaderDTOList!=null && DataUploaderDTOList.size()>0){
			for(DataUploaderDTO dataUploaderDTO : DataUploaderDTOList){
				for(String loadTypeElem : loadTypeList){
					if(dataUploaderDTO != null  && dataUploaderDTO.getOrderIdList() != null && dataUploaderDTO.getOrderIdList().size() > 0){
						String dfpTaskkey = System.currentTimeMillis()+"_"+loadTypeElem+"_"+dataUploaderDTO.getDfpNetworkCode()+"_"+dataUploaderDTO.getPublisherBQId();
						for(String orderIds : dataUploaderDTO.getOrderIdList() ){
							DFPTaskEntity entity = service.saveInProgressTask("not-generated", dataUploaderDTO.getDfpNetworkCode(), start, end, dfpTaskkey, loadTypeElem, orderIds);
							String taskName = "";
							if(taskType!=null && taskType.equals(LinMobileConstants.DAILY_TASK_TYPE)){
								 taskName = TaskQueueUtil.dailyDataUpload("/runDailyDataTask.lin", start, end, orderIds, dataUploaderDTO.getDfpNetworkCode(),
										dataUploaderDTO.getPublisherBQId()+"",loadTypeElem, historical, dfpTaskkey, (entity.getId() == null ? 0 : entity.getId()), taskType);
							}else if(taskType!=null && taskType.equals(LinMobileConstants.NON_FINALISE_TASK_TYPE)){
								 taskName = TaskQueueUtil.dailyDataUpload("/runDailyDataTask.lin", start, end, orderIds, dataUploaderDTO.getDfpNetworkCode(),
										dataUploaderDTO.getPublisherBQId()+"",loadTypeElem, historical, dfpTaskkey, (entity.getId() == null ? 0 : entity.getId()), taskType);
							}
							
							entity.setTaskName(taskName);
							service.saveOrUpdateTask(entity);
							}
			
						}
					}
				}
			}
		}
	
	
	private String downloadDFPReportUsingSchema(DfpServices dfpServices,DfpSession dfpSession, IDFPReportService dfpReportService,
			String loadType, String startDate, String endDate, List<String> orderIdList, String commaSepratedOrderIds) throws Exception{
		String downloadUrl = null;
		
		switch (loadType) {
			case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
				downloadUrl=dfpReportService.getDFPReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList, commaSepratedOrderIds);
				break;
			case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
				downloadUrl=dfpReportService.getRichMediaCustomEventReportByOrderId(dfpServices, dfpSession, 
						startDate, endDate, orderIdList, commaSepratedOrderIds);
				break;
			case LinMobileConstants.LOAD_TYPE_LOCATION:
				downloadUrl=dfpReportService.getDFPReportByLocationByOrderIds(dfpServices, dfpSession, startDate, 
						endDate, orderIdList, commaSepratedOrderIds);
				break;
			case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
				downloadUrl=dfpReportService.getRichMediaVideoCampaignReportByByOrderIds(dfpServices, dfpSession, 
						startDate, endDate, orderIdList, commaSepratedOrderIds);
				break;
			case LinMobileConstants.LOAD_TYPE_TARGET:
				downloadUrl=dfpReportService.getDFPTargetReportByOrderIds(dfpServices, dfpSession, startDate, endDate, orderIdList, commaSepratedOrderIds);
				break;	
			default:
				break;
		}
		 return downloadUrl;
	}
	
	private List<String> processRawFiles(String loadType,String splitRawFileName,
			String procFileName, String dirName, String networkCode,String bucketName,String publisherIdInBQ){
		List<String> processedFilePathList = new ArrayList<>();
		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		IHistoricalReportService service = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
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
	
	
	public String runNonFinaliseDailyReport() throws Exception{
		log.info("inside runNonFinaliseDailyReport of DailyDataLoaderAction");
		
		String startDate=request.getParameter("startDate");		
		String endDate=request.getParameter("endDate");		
		String loadType  = request.getParameter("loadTypeElem");
		String commaSepratedOrderIds = request.getParameter("orderIds");
		String dfpNetworkCode = request.getParameter("dfpNetworkCode");
		String publisherIdInBQ = request.getParameter("publisherIdInBQ");
		String publisherName=request.getParameter("publisherName");
		String mapKey = request.getParameter("mapKey");
		
		String schemaName = DataLoaderUtil.getSchemaNameByLoadType(loadType);
		String response = "";
		int taskCount = 0;
		DailyDataProcessObj dailyDataProcessObj = new DailyDataProcessObj();
		int updatedTaskCount = 0;
		String id = "";
		String tableId = "";
		String processedFilePath="";
		String projectId = "";
		String timestamp = DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		QueryDTO bigQueryDTO = new QueryDTO();
		try {

			dfpSession = DFPAuthenticationUtil.getDFPSession(dfpNetworkCode);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();

			IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
			IDFPReportService dfpReportService = new DFPReportService();
			List<String> orderIdList = new ArrayList<String>();
			orderIdList.add(commaSepratedOrderIds);

			// Run report search tool on DFP and get the download URL of dfp
			// site.
			
			String downloadUrl = downloadDFPReportUsingSchema(dfpServices, dfpSession, dfpReportService,loadType,
								startDate, endDate, orderIdList, commaSepratedOrderIds);
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
				projectId = cloudProjectBQDTO.getBigQueryProjectId();
				String serviceAccountEmail = cloudProjectBQDTO.getBigQueryServiceAccountEmail();
				String servicePrivateKey = cloudProjectBQDTO.getBigQueryServicePrivateKey();
				String bucketName = cloudProjectBQDTO.getCloudStorageBucket(); //DataLoaderUtil.getCloudStorageBucket(networkCode);
				String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
				String dfpDataSource = DFPReportService.getDFPDataSourceByDFPNetworkCode(dfpNetworkCode);
				tableId = schemaName + "_" + dfpDataSource + "_" + startDate.replaceAll("-", "_");
				String schemaFile = DataLoaderUtil.getSchemaFileByLoadType(loadType);
				bigQueryDTO = new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId, schemaFile,
										tableId, dataSetId, schemaName);
				String month = DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
				String rawFileName = timestamp + "_" + schemaName + "_" + startDate + "_" + 
										endDate + "_" + "raw" + ".csv";
				String procFileName = rawFileName.replace("_raw", "_proc");
			
				
				String dirName = LinMobileConstants.REPORT_FOLDER 
									+ "/"+dfpNetworkCode
									+ "/"+schemaName+ "/"
									+ month;
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
					List<String> tempProcessedFilePathList = processRawFiles(loadType, splitRawFileName, 
							procFileName, dirName, dfpNetworkCode, bucketName,String.valueOf(publisherIdInBQ));
					log.info("tempProcessedFilePathList : "+tempProcessedFilePathList);
					processedFilePathList.addAll(tempProcessedFilePathList);
				}
				log.info("all processedFilePathList:" + processedFilePathList.size());
				id=publisherIdInBQ+":"+tableId;
	    		for(int i=0;i<processedFilePathList.size();i++){		    				
	    			response=processedFilePathList.get(i);
	    			if(i==0){
	    				processedFilePath=response;
	    			}else{
	    				processedFilePath=processedFilePath+","+response;
	    			}			    			
	    		}  
		
	    		ProcessFileObj processFileObj = new ProcessFileObj(processedFilePathList, publisherIdInBQ, ProcessFileStatusEnum.Process.ordinal(),
	    				DateUtil.getDateYYYYMMDD(), DateUtil.getDateYYYYMMDD(), mapKey);
	    		log.info("Going to save ProcessFileObj to datastore");
	    		if(processFileObj.getCloudStoragFilePathList()!=null && processFileObj.getCloudStoragFilePathList().size()>0){
		    		service.saveProcessFileObj(processFileObj);
	    		}else{
	    			log.info("invalid processedFilePathList ");
	    		}
	    		
	    		dailyDataProcessObj = checkTaskCount(mapKey);
	    		if(dailyDataProcessObj!=null){
	    			taskCount = dailyDataProcessObj.getTaskCount();
	    			log.info("taskCount found : "+taskCount);		
	    			if(taskCount>0){
	    				 updatedTaskCount = updateTaskCount(mapKey, taskCount, dailyDataProcessObj);
	    				if(updatedTaskCount==0){
	    					Thread.sleep(2000);
	    					response = uploadDataToBQ(mapKey, id, tableId, startDate, endDate, timestamp, publisherIdInBQ,
	    							processedFilePath, projectId, bigQueryDTO, response);
		    			}
	    			} 
	    		}
			}
		}catch(Exception e){
			log.severe("Exception :"+e.getMessage());
			log.info("Going to check task count : "+mapKey);
			dailyDataProcessObj = checkTaskCount(mapKey);
    		if(dailyDataProcessObj!=null){	
    			taskCount = dailyDataProcessObj.getTaskCount();
    			log.info("taskCount found : "+taskCount);
    			if(taskCount>0){
    				updatedTaskCount = updateTaskCount(mapKey, taskCount, dailyDataProcessObj);
    				if(updatedTaskCount==0){
    					response = uploadDataToBQ(mapKey, id, tableId, startDate, endDate, timestamp, publisherIdInBQ,
							processedFilePath, projectId, bigQueryDTO, response);
    				}
    			} 
    		}
		}
		return response;
	}
	
	public DailyDataProcessObj checkTaskCount(String mapKey) throws  Exception{
		log.info("Task count check, mapkey : "+mapKey);
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		DailyDataProcessObj dailyDataProcessObj = new DailyDataProcessObj();
		dailyDataProcessObj = service.getDailyDataProcessObj(mapKey);
		return dailyDataProcessObj;
	}
	
	public int updateTaskCount(String mapKey, int taskCount,DailyDataProcessObj dailyDataProcessObj) throws Exception{
		log.info("No of task still remaining = "+taskCount);
		log.info("going to decrement the task value");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		dailyDataProcessObj.setTaskCount(--taskCount);
		service.saveDailyDataProcessObj(dailyDataProcessObj,mapKey);
		log.info("task remaing after decrement  = "+dailyDataProcessObj.getTaskCount());
		return taskCount;
	}
		
	public String uploadDataToBQ(String mapKey,String id,String tableId,String startDate,String endDate,
			String timestamp, String publisherIdInBQ,String processedFilePath,String projectId,QueryDTO bigQueryDTO,String response) throws Exception{
		log.info("Going to save data to bigquery");
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
		String dataSetId = LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
		String bqResponse="";
		List<ProcessFileObj> processFileObjList = new ArrayList<>();
		processFileObjList = service.loadAllProcessObjList(mapKey,  ProcessFileStatusEnum.Process.ordinal());
		if(processFileObjList!=null && processFileObjList.size()>0){
			List<String> processFileList = new ArrayList<>();
			for (ProcessFileObj processObj : processFileObjList) {
				if(processObj!=null && processObj.getCloudStoragFilePathList()!=null && processObj.getCloudStoragFilePathList().size()>0){
					processFileList.addAll(processObj.getCloudStoragFilePathList());
					processObj.setUploadState(ProcessFileStatusEnum.Uploaded.ordinal());
					service.saveProcessFileObj(processObj);
				}
			}
			
			FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 0, startDate, endDate, 
    				timestamp, Integer.parseInt(publisherIdInBQ), dataSource, processedFilePath, 
    				projectId, dataSetId, 0);			    		
    		
    		bqResponse= service.uploadProcessFileAtBQ(reportObject, 0, processFileList, bigQueryDTO,null);	
    		processFileObjList = new ArrayList<>();
    		processFileObjList = service.loadAllProcessObjList(mapKey,  ProcessFileStatusEnum.Process.ordinal());
    		if(processFileObjList!=null && processFileObjList.size()>0){
    			for (ProcessFileObj processObj : processFileObjList) {
    				if(processObj!=null){
    					processObj.setUploadState(ProcessFileStatusEnum.Error.ordinal());
    					service.saveProcessFileObj(processObj);
    				}
    			}
    		}
    		response=processFileList+" and bigQuery response:: "+bqResponse;		
		}
		return response;
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
	/*public static Map<String, Integer> getProcessDataKeyFileMap() {
		return processDataKeyFileMap;
	}

	public static void setProcessDataKeyFileMap(
			Map<String, Integer> processDataKeyFileMap) {
		DailyDataLoaderAction.processDataKeyFileMap = processDataKeyFileMap;
	}*/

	public static void main(String[] args) {
		int taskCount = 1;
		--taskCount;
		System.out.println(taskCount); 
	}
}
