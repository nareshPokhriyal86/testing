package com.lin.persistance.dao;

import java.io.IOException;
import java.util.List;

import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.DFPTaskEntity;
import com.lin.server.bean.DailyDataProcessObj;
import com.lin.server.bean.DataCollectorReport;
import com.lin.server.bean.DataProcessorReport;
import com.lin.server.bean.DataUploaderReport;
import com.lin.server.bean.DfpOrderIdsObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.ProcessFileObj;
import com.lin.server.bean.TrackCronJobReport;
import com.lin.web.dto.QueryDTO;


public interface IReportDAO extends IBaseDao{
	
	public void saveObject(Object obj) throws DataServiceException;
	public void saveObjectStrongObfy(Object obj) throws DataServiceException;
	public void deleteObject(Object obj) throws DataServiceException;
	
	public List<DataCollectorReport> loadDataCollectorReportByDownloadStatus(String downloadStatus) throws DataServiceException;
	public List<DataCollectorReport> loadDataCollectorReportByProcessStatus(String downloadStatus,String processedStatus) throws DataServiceException;
	
	public List<DataCollectorReport> loadDataCollectorReportByDownloadStatus(String downloadStatus,String dataSource) throws DataServiceException;
	public List<DataCollectorReport> loadDataCollectorReportByProcessStatus(String downloadStatus,String processedStatus,String dataSource) throws DataServiceException;
	public List<DataProcessorReport> loadDataUploaderReportByUploadStatus(String processedStatus,String uploadStatus,String dataSource) throws DataServiceException;
	
	public DataCollectorReport loadDataCollectorReportByDownloadURL(String downloadURL) throws DataServiceException;
	
	public List<DataCollectorReport> loadReportsToProcess(int limit) throws DataServiceException;
	
	public List<DataProcessorReport> loadReportsToUploadBQ(int limit) throws DataServiceException;
	
	public List<TrackCronJobReport> loadCronJobData(String reportType) throws DataServiceException;
	public List<DfpOrderIdsObj> loadAllOrderIds(String dfpNetworkCode) throws DataServiceException;
	
	public FinalisedTableDetailsObj loadFinaliseOrNonFinaliseTable(String tableId,String publisherId) throws DataServiceException;
	public void deleteFinaliseOrNonFinaliseTable(String tableId,String publisherId) throws DataServiceException;
	
	public List<FinalisedTableDetailsObj> loadFinaliseTables(int publisherId,
    		String bigQueryProjectId,String bigQueryDataSet)	throws DataServiceException;
	
	public List<FinalisedTableDetailsObj> loadNonFinaliseTables(int publisherId,
    		String bigQueryProjectId,String bigQueryDataSet)	throws DataServiceException;
	
	public List<FinalisedTableDetailsObj> loadAllNonFinaliseTables() throws DataServiceException;
	public FinalisedTableDetailsObj loadFinaliseOrNonFinaliseTable(String id) throws DataServiceException;
	List<ProcessFileObj> loadAllProcessObjList(String mapKey, int uploadState);
	DailyDataProcessObj getDailyDataProcessObj(String processKey)
			throws Exception;
	public void saveDFPTaskEntity(DFPTaskEntity obj) throws DataServiceException ;
	DFPTaskEntity loadDFPTaskEntity(String id) throws DataServiceException;
	public List<DFPTaskEntity>  loadDFPTaskEntityByTaskKey (String dfpTaskKey) throws DataServiceException;
	QueryResponse getAllOrderIdsForNonFinaliseData(QueryDTO queryDTO)
			throws DataServiceException, IOException;
}
