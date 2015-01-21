package com.lin.web.service;

import java.util.List;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.DailyDataProcessObj;
import com.lin.server.bean.DataCollectorReport;
import com.lin.server.bean.DataProcessorReport;
import com.lin.server.bean.DataUploaderReport;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.ProcessFileObj;
import com.lin.server.bean.TrackCronJobReport;
import com.lin.web.dto.QueryDTO;

public interface IReportService extends IBusinessService{

	public String generateNexageReport(String startDate,String endDate,String dimensions);
	//public String generateNewNexageReport(String startDate,String endDate,String dimensions);
	public String generateMojivaReport(String startDate,String endDate);
	public String generateMojivaReportByDay(String date);
	public String generateAdExReport(String reportsResponse,String startDate,String fileName, String dirName);
	
	public String generateDFPReport(String startDate,String endDate,String rawFileName,String dirName,
			String timestamp);
	
	public String generateDFPReport(String startDate,String endDate,String rawFileName,
			String dirName,String timestamp,String networkCode);	
	
	
	public String generateDFPReportByLocation(String startDate,String endDate,String rawFileName,String dirName,String timestamp);
	
	public String generateDFPReportByLocation(String rawFileName,String proccessFileName,
			String dirName,String timestamp);
	
	public List<String> generateDFPReportByLocationWithSplit(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName);
	
	public List<String> generateDFPReportByLocation(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName);
	
	
	public List<DataCollectorReport> loadDataCollectorReportToDownload(String downloadStatus);
	
	public void saveDataFrameworkReport(Object obj);
	
	
	public String generateUndertoneReport(String rawFileName,String procFileName,String dirName,String bucketName);
	public String generateSellThroughReport(String rawFileName,String procFileName,
			String dirName,String networkCode,String bucketName);
	
	public boolean processReports(int limit);
	public boolean uploadReportsOnBigQuery(int limit);
	
	public TrackCronJobReport getCronJobData(String reportType);
	public boolean saveCronJobData(TrackCronJobReport obj);
	
	public String generateLinOneRichMediaCustomEventReport(String rawFileName,String procFileName,
    		String dirName,String dataSource);
	
	public String generateLSNReport(String rawFileName,String procFileName,String dirName);
	
	public String generateRichMediaTraffickingReport(String rawFileName,String procFileName,String dirName,String dataSource);
	
	
	public String generateRichMediaCustomEventReport(String rawFileName,String procFileName,
			String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName);
	
	public String generateRichMediaVideoCampaignReport(String rawFileName,String procFileName,
			String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName);
	
	public String generateCeltraReport(String rawFileName,String procFileName,String dirName);
	
	public String generateXAdReport(String rawFileName,String procFileName,String dirName);
	
	public String generateTribuneReport(String rawFileName,String procFileName,String dirName);
	
	public String generateLinDigitalDFPReport(String startDate,String endDate,String rawFileName,
			String dirName);	
	
	public List<Long> getAllOrderIds(String networkCode);
	
	public String generateDFPTargetReport(String startDate,String endDate,String rawFileName,
				String dirName,String networkCode,String publisherId,String publisherName,String bucketName);
	public List<String> generateDFPTargetReport(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName);
			
	public String generateNewDFPReport(String startDate,
			 String endDate,String rawFileName,String dirName,String timestamp);
	 
	public List<String> getAllAccountByCompanyId(String companyId);
	 
	public String generateFinilisedDFPReport(String rawFileName,
				String dirName,String networkCode,String publisherId, String publisherName,String bucketName);
	 
	public String generateFinalisedNexageReport(String startDate,String endDate,String dimensions,
	    		String dirName,String rawFileName,String bucketName);
	
	public String generateFinalisedNexageReportNewAPI(String startDate,String endDate,String dimensions,
    		String dirName,String rawFileName,String bucketName);
	  
	public String generateFinaliseMojivaReportByDay(String date, String fileName,String dirName,String bucketName);
	public String generateFinaliseMojivaReport(String startDate,String endDate, String fileName,String dirName,String bucketName);
	 
	public boolean saveReportInDatastore(Object reportObject);
	 

	
	/*public String uploadProcessFileAtBQ(String tableId,String reportsResponse, String startDate,String endDate,
				String timestamp,String processedFilePath,String dataSource,int finalizeOrNonFinalize, 
				String publisherId,String publisherName, String serviceAccountEmail,String servicePrivateKey,
				String projectId,String nonFinaliseTableId);*/
	
	public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,int finalizeOrNonFinalize, 
				String reportsResponse, QueryDTO bqDTO,String nonFinaliseTableId);
	
	public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,int finalizeOrNonFinalize, 
			List<String> reportsResponseList, QueryDTO bqDTO,String nonFinaliseTableId);
	
	 public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,
			   String publisherId,List<String> reportsResponseList, QueryDTO bqDTO);
	 
	public FinalisedTableDetailsObj loadFinaliseNonFinaliseObject(String tableId,String publisherId);
	
	public String generateFinalisedAdExchangeReport(String reportsResponse,String startDate,String fileName,
			String dirName,String bucketName);
	
	public String generateFinaliseUndertoneReport(String rawFileName,String procFileName,String dirName,String bucketName);
	
	
	
	public List<String> getAllAccountIdByCompanyName(String companyName);
	
	
	public String generateClientDemoCorePerformanceReport(String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId, String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName);
	public List<String> generateClientDemoDFPReportByLocationWithSplit(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName);
	public String generateClientDemoDFPTargetReport(String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId,String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName);
	public String generateClientDemoRichMediaCustomEventReport(String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,
    		String orderName,String advertiserName,String siteName,String lineItemName);
	
    public boolean updateAllFinalisedTable(); 
    
    public List<String> generateDFPReportByProductPerformance(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String bucketName);
    
    public FinalisedTableDetailsObj loadFinaliseNonFinaliseObject(String id);
	void saveProcessFileObj(ProcessFileObj processFileObj)
			throws DataServiceException;
	List<ProcessFileObj> loadAllProcessObjList(String mapKey, int uploadState)
			throws Exception;
	void saveDailyDataProcessObj(DailyDataProcessObj dailyDataProcessObj, String key) throws DataServiceException;
	DailyDataProcessObj getDailyDataProcessObj(String processKey)throws Exception;
   
}
