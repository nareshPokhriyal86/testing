package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IReportDAO;
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
import com.lin.web.service.impl.OfyService;


public class ReportDAO implements IReportDAO{

	private static final Logger log=Logger.getLogger(ReportDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	 private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);
	 
	public void saveObjectStrongObfy(Object obj) throws DataServiceException {
		strongObfy.save().entity(obj);		
	}
	public void saveObject(Object obj) throws DataServiceException {
		obfy.save().entity(obj);		
	}
	public void saveDFPTaskEntity(DFPTaskEntity obj) throws DataServiceException {
		strongObfy.save().entity(obj).now();
		obfy.clear();
	}
	
	 public DFPTaskEntity  loadDFPTaskEntity (String id) throws DataServiceException{
		 
		 DFPTaskEntity  obj=strongObfy.cache(false).load().type(DFPTaskEntity .class).id(new Long(id)).now();
			return obj;
		}
	 
	 public List<DFPTaskEntity>  loadDFPTaskEntityByTaskKey (String dfpTaskKey) throws DataServiceException{
		 
		 List<DFPTaskEntity>  taskEntitiesList =strongObfy.cache(false).load().type(DFPTaskEntity .class)
				 .filter("taskGroupKey = " , dfpTaskKey).list();
			return taskEntitiesList;
		}
	    
	 
	 
	public void deleteObject(Object obj) throws DataServiceException {
		obfy.delete().entity(obj);
	}

	public List<DataCollectorReport> loadDataCollectorReportByDownloadStatus(String downloadStatus) throws DataServiceException{
		List<DataCollectorReport> resultList=obfy.load().type(DataCollectorReport.class)		                         
		                         .filter("downloadStatus = ",downloadStatus)	    	                    
		                         .list();
		return resultList;
	}
	
    public List<DataCollectorReport> loadDataCollectorReportByProcessStatus(String downloadStatus,String processedStatus) 
         throws DataServiceException{		
		List<DataCollectorReport> resultList=obfy.load().type(DataCollectorReport.class)		                         
		                         .filter("downloadStatus = ",downloadStatus)
		                         .filter("processedStatus = ",processedStatus)		                       
		                         .list();
		return resultList;
	}
    
    public List<DataCollectorReport> loadDataCollectorReportByProcessStatus(String downloadStatus,String processedStatus,String dataSource) throws DataServiceException{
    	List<DataCollectorReport> resultList=obfy.load().type(DataCollectorReport.class)		                         
                 .filter("downloadStatus = ",downloadStatus)
                 .filter("processedStatus = ",processedStatus)		                       
                 .filter("dataSource = ",dataSource)
                 .list();
       return resultList;
    }
    
    public List<DataProcessorReport> loadDataUploaderReportByUploadStatus(String processedStatus,String uploadStatus,String dataSource) throws DataServiceException{
    	List<DataProcessorReport> resultList=obfy.load().type(DataProcessorReport.class)		                         
    			.filter("processedStatus = ",processedStatus)
    			.filter("uploadBQStatus = ",uploadStatus)		                       
    			.filter("dataSource = ",dataSource)
    			.list();
      return resultList;
    }
    
    public List<DataCollectorReport> loadDataCollectorReportByDownloadStatus(String downloadStatus,String dataSource) throws DataServiceException{
		List<DataCollectorReport> resultList=obfy.load().type(DataCollectorReport.class)		                         
        		.filter("downloadStatus = ",downloadStatus)	    	                    
        		.filter("dataSource = ",dataSource)
        		.list();
        return resultList;
    }
    
    public DataCollectorReport loadDataCollectorReportByDownloadURL(String downloadURL) throws DataServiceException{
    	DataCollectorReport obj=obfy.load().type(DataCollectorReport.class)		                         
		                         .filter("downloadURL = ",downloadURL)
		                         .first().now();
    	return obj;		
    }
    
    /*
     * load oldest report pending to be processed
     * @see com.lin.persistance.dao.IReportDAO#loadReportsToProcess(int)
     */
    public List<DataCollectorReport> loadReportsToProcess(int limit) throws DataServiceException{
    	List<DataCollectorReport> resultList=obfy.load().type(DataCollectorReport.class)		                         
		      .filter("downloadStatus = ","1")	    	                    
		      .filter("processedStatus = ","0")
		      .limit(limit)
		      .order("updatedOn")
		      .list();
    	log.info("loadReportsToProcess: resultList:"+resultList.size());
        return resultList;
    }
    
    
   /*
    * load oldest report pending to be uploaded on BigQuery
    * @see com.lin.persistance.dao.IReportDAO#loadReportsToUploadBQ(int)
    */
    public List<DataProcessorReport> loadReportsToUploadBQ(int limit) throws DataServiceException{
    	List<DataProcessorReport> resultList=obfy.load().type(DataProcessorReport.class)		                         
			.filter("processedStatus = ","1")
			.filter("uploadBQStatus = ","0")		                       
			.limit(limit)
			.order("updatedOn")
			.list();
      return resultList;
   }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will load cron startDate and cron endDate
     * @param String reportType
     * @return TrackCronJobReport obj
     */
    public List<TrackCronJobReport> loadCronJobData(String reportType) throws DataServiceException{
    	List<TrackCronJobReport> objList=obfy.load().type(TrackCronJobReport.class)
    	                           .filter("reportType = ",reportType)		  
		                           .list();
    	return objList;		
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will load all orderIds on the basis of networkCode
     * @param String dfpNetworkCode
     * @return TrackCronJobReport obj
     */
    public List<DfpOrderIdsObj> loadAllOrderIds(String dfpNetworkCode) throws DataServiceException{
    	List<DfpOrderIdsObj> objList=obfy.load().type(DfpOrderIdsObj.class)
    	                           .filter("dfpNetworkCode = ",dfpNetworkCode)		  
		                           .list();
    	return objList;		
    }
    
    
    public FinalisedTableDetailsObj loadFinaliseOrNonFinaliseTable(String id) throws DataServiceException{
		FinalisedTableDetailsObj obj=obfy.load().type(FinalisedTableDetailsObj.class)		                         
		                         .id(id).now();
		return obj;
	}
    
    public FinalisedTableDetailsObj loadFinaliseOrNonFinaliseTable(String tableId, String publisherId) throws DataServiceException{
		FinalisedTableDetailsObj obj=obfy.load().type(FinalisedTableDetailsObj.class)		                         
		                         .filter("tableId = ",tableId)
		                         .filter("publisherId = ",Integer.parseInt(publisherId))
		                         .first().now();
		return obj;
	}
    
    public void deleteFinaliseOrNonFinaliseTable(String tableId,String publisherId) throws DataServiceException{
    	log.info("delete tableId:"+tableId+" for publisherId:"+publisherId);
		FinalisedTableDetailsObj obj=loadFinaliseOrNonFinaliseTable(tableId, publisherId);
		obfy.delete().entity(obj);
		
	}
   
    public List<FinalisedTableDetailsObj> loadFinaliseTables(int publisherId,
    		String bigQueryProjectId,String bigQueryDataSet)	throws DataServiceException{
		log.info("publisherId:"+publisherId+" bigQueryProjectId:"+bigQueryProjectId+" and bigQueryDataSet:"+bigQueryDataSet);
    	List<FinalisedTableDetailsObj> resultList=obfy.load().type(FinalisedTableDetailsObj.class)		                         
		                         .filter("tableType = ",1)
		                         .filter("publisherId = ",publisherId)
		                         .filter("bigQueryProjectId = ", bigQueryProjectId)
		                         .filter("bigQueryDataSet = ", bigQueryDataSet)
		                         .list();
		return resultList;
	}
    
    public List<FinalisedTableDetailsObj> loadNonFinaliseTables(int publisherId,
    		String bigQueryProjectId,String bigQueryDataSet)	throws DataServiceException{
    	log.info("publisherId:"+publisherId+" bigQueryProjectId:"+bigQueryProjectId+" and bigQueryDataSet:"+bigQueryDataSet);
    	List<FinalisedTableDetailsObj> resultList=obfy.load().type(FinalisedTableDetailsObj.class)		                         
		                         .filter("tableType = ",0)
		                         .filter("mergeStatus = ",0)
		                         .filter("publisherId = ",publisherId)
		                         .filter("bigQueryProjectId = ", bigQueryProjectId)
		                         .filter("bigQueryDataSet = ", bigQueryDataSet)
		                         .list();
		return resultList;

    }
    
    public List<FinalisedTableDetailsObj> loadAllNonFinaliseTables() throws DataServiceException{
    	List<FinalisedTableDetailsObj> resultList=obfy.load().type(FinalisedTableDetailsObj.class).list();
       return resultList;
    }
    
    @Override
    public List<ProcessFileObj> loadAllProcessObjList(String mapKey, int uploadState){
    	List<ProcessFileObj> processFileObjList = new ArrayList<>();
    	processFileObjList = obfy.load().type(ProcessFileObj.class)
    			.filter("mapKey =", mapKey)
    			.filter("uploadState =", uploadState).list();
		return processFileObjList;
    }
    
    @Override 
    public DailyDataProcessObj getDailyDataProcessObj(String processKey) throws Exception{
    	DailyDataProcessObj dailyDataProcessObj = obfy.load().type(DailyDataProcessObj.class)
    			.filter("processKey = ", processKey).first().now();
    	return dailyDataProcessObj;
    } 
}
