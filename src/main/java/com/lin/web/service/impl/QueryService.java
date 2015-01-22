package com.lin.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.lin.persistance.dao.IReportDAO;
import com.lin.persistance.dao.impl.ReportDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IQueryService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;

/*
 * @author Youdhveer Panwar
 */
public class QueryService implements IQueryService{

	private static final Logger log = Logger.getLogger(QueryService.class.getName());
	/*
	 * This method will create 'From' clause based on user parameters
	 * 
	 * @author Youdhveer Panwar
	 */
	public QueryDTO createQueryFromClause(String publisherId, String startDate, String endDate, String schemaName){
		StringBuffer queryData=new StringBuffer();
		String projectId=null;
		String serviceAccountEmail=null;
		String servicePrivateKey=null;
		String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;		
		QueryDTO queryDTO=null;
		
		switch(publisherId) {		
		  case "0":
			  projectId=LinMobileConstants.CLIENT_DEMO_GOOGLE_API_PROJECT_ID;
			  serviceAccountEmail=LinMobileConstants.CLIENT_DEMO_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			  servicePrivateKey=LinMobileConstants.CLIENT_DEMO_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			  break;
		  case "1":
			  projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
			  serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			  servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			  break;
		  case "2":
			  projectId=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_PROJECT_ID;
			  serviceAccountEmail=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			  servicePrivateKey=LinMobileConstants.LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			  break;
		  case "4":
			  projectId=LinMobileConstants.TRIBUNE_GOOGLE_API_PROJECT_ID;
			  serviceAccountEmail=LinMobileConstants.TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			  servicePrivateKey=LinMobileConstants.TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			  break;
		  case "5":
			  projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
			  serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			  servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			  break;
		  default:
			  log.warning("There is no project configured for this publisherId :"+publisherId);
			  return queryDTO;			  
		}		
		
		
		log.info("QuerySchema:"+schemaName);
		//Compare startDate and endDate with currentDate to select finalise and non-finalise tables
		String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		long diff=DateUtil.getDifferneceBetweenTwoDates(endDate, currentDate, "yyyy-MM-dd");
		if(diff < 0){
			 log.warning("There is no tables for this end date :"+endDate+", it should be less than or equal currentDate:"+currentDate);
			 if(schemaName !=null && schemaName.equals(LinMobileConstants.BQ_SELL_THROUGH)){
				 String table=dataSetId+"."+LinMobileConstants.BQ_SELL_THROUGH;
				 queryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId,dataSetId, table);				
			 }
		}else if(diff >=0 && diff < LinMobileConstants.CHANGE_WINDOW_SIZE){
			long diff2=DateUtil.getDifferneceBetweenTwoDates(startDate, currentDate, "yyyy-MM-dd");
			if(diff2 >= LinMobileConstants.CHANGE_WINDOW_SIZE){
				log.info("Need finalise and non-finalise both tables...");
				String finaliseEndDate=DateUtil.getModifiedDateStringByDays(currentDate, -LinMobileConstants.CHANGE_WINDOW_SIZE, "yyyy-MM-dd");
				
				queryDTO=getFinaliseTables(startDate, finaliseEndDate, publisherId, schemaName, serviceAccountEmail, 
						servicePrivateKey, projectId, dataSetId);
				if(queryDTO !=null){
					queryData.append(queryDTO.getQueryData());
				}				
				
				QueryDTO nonFinaliseQueryDTO=getNonFinaliseTables(startDate,endDate,publisherId, schemaName, serviceAccountEmail, 
						servicePrivateKey, projectId, dataSetId);
				if(nonFinaliseQueryDTO !=null){
					if(queryData.toString() !=null && queryData.toString().length()>0){
						queryData.append(","+nonFinaliseQueryDTO.getQueryData());
					}else{
						queryData.append(nonFinaliseQueryDTO.getQueryData());
					}
					queryDTO.setQueryData(queryData.toString());
					
				}			
				
			}else{
				log.info("Need only non-finalise tables...");
				queryDTO=getNonFinaliseTables(startDate,endDate,publisherId, schemaName, serviceAccountEmail, 
						servicePrivateKey, projectId, dataSetId);				
			}
			
		}else{
			log.info("Need only finalise tables...");
			queryDTO=getFinaliseTables(startDate, endDate, publisherId, schemaName, serviceAccountEmail, 
					servicePrivateKey, projectId, dataSetId);
			
		}	
		
		if(queryDTO !=null){
			log.info(">>Query content :"+queryDTO.getQueryData());	
		}else{
			log.info(">>Query content :"+queryData.toString());	
		}
		return queryDTO;
		
	}
	
	public QueryDTO getFinaliseTables(String startDate, String endDate, String publisherId, String schemaName,
			String serviceAccountEmail,	String servicePrivateKey,String projectId,String dataSetId){
		StringBuffer queryData=new StringBuffer();
		QueryDTO queryDTO=null;
		String tableId;
		
		List<String> monthList=getFinaliseMonthlyDate(startDate, endDate);
 		List<String> bigQueryTableList=getFinaliseNonFinaliseTablesFromBigQuery(serviceAccountEmail, servicePrivateKey, publisherId,
				projectId, dataSetId, schemaName, true);
		if(bigQueryTableList!=null && bigQueryTableList.size()>0){
			int matchCount=0;
			for(String dateStr:monthList){
				//tableId=schemaName+"_"+dateStr;
				tableId=dataSetId+"."+schemaName+"_"+dateStr;
				if(bigQueryTableList.contains(tableId)){
					if(matchCount==0){
						//queryData.append(dataSetId+"."+tableId);
						queryData.append(tableId);	
					}else{
						queryData.append(",");		
						//queryData.append(dataSetId+"."+tableId);
						queryData.append(tableId);	
					}
					matchCount++;				
				}
				else{
					log.info("This table is not found ::"+tableId);
				}
				
			}
			log.info("Query content :"+queryData.toString());
			queryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId,dataSetId, queryData.toString());
		}else{
			log.info("No tables found for this schema::"+schemaName+" and projectId:"+projectId+
					" and dataSetId:"+dataSetId+" and publisherId:"+publisherId);
			queryDTO=null;
		}
	
		return queryDTO;
	}
	
	public QueryDTO getNonFinaliseTables(String startDate, String endDate,String publisherId, String schemaName,
			String serviceAccountEmail,	String servicePrivateKey,String projectId,String dataSetId){
		StringBuffer queryData=new StringBuffer();
		QueryDTO queryDTO=null;
		String tableId="";
		
		//List<String> nonFinaliseDateList=getNonFinaliseMonthlyDate();
		List<String> nonFinaliseDateList=getNonFinaliseMonthlyDate(startDate,endDate);
		
		//List<String> datastoreList=getNonFinaliseTablesFromDataStore(publisherId, projectId, dataSetId, schemaName);
		List<String> bigQueryTableList=getFinaliseNonFinaliseTablesFromBigQuery(serviceAccountEmail, servicePrivateKey, publisherId,
				projectId, dataSetId, schemaName, false);
		
		if(bigQueryTableList!=null && bigQueryTableList.size()>0){
			int matchCount=0;
			for(String nonFinaliseTableId:bigQueryTableList){
				boolean match=false;				
				for(String dayStr:nonFinaliseDateList){				
					if(nonFinaliseTableId.contains(dayStr)){
						match=true;
						tableId=nonFinaliseTableId;
						break;
					}
				}
				if(match){
					if(matchCount==0){
						//queryData.append(dataSetId+"."+tableId);
						queryData.append(tableId);	
					}else{
						queryData.append(",");		
						//queryData.append(dataSetId+"."+tableId);
						queryData.append(tableId);	
					}
					matchCount++;
									
				}
				/*else{
					log.info("This table is not required in query:::"+nonFinaliseTableId);
				}*/
				
			}
			log.info("Query content :"+queryData.toString());
			queryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId,dataSetId, queryData.toString());
		}else{
			log.warning("No tables found in dataStore for this schema::"+schemaName+" and projectId:"+projectId+
					" and dataSetId:"+dataSetId+" and publisherId:"+publisherId);
			queryDTO=null;
		}
	
		return queryDTO;
	}
	
	public List<String> getFinaliseMonthlyDate(String startDate, String endDate){
		List<String> monthList=new LinkedList<String>();
		startDate=startDate.replaceAll("-","_");
		endDate=endDate.replaceAll("-","_");
		
		String startMonth=startDate.substring(0,7);
		String endMonth=endDate.substring(0,7);
		if(startMonth.equals(endMonth)){
			monthList.add(startMonth);
		}else{
			monthList.add(startMonth);
			String nextMonthDate;
			try {
				nextMonthDate = DateUtil.getModifiedDateByMonths(startMonth, 1, "yyyy_MM");
				while(!nextMonthDate.equals(endMonth) ){
					if(!monthList.contains(nextMonthDate)){
						monthList.add(nextMonthDate);
					}
					nextMonthDate=DateUtil.getModifiedDateByMonths(nextMonthDate, 1, "yyyy_MM");				
				}
				if(!monthList.contains(endMonth)){
					monthList.add(endMonth);
				}
				
			} catch (Exception e) {
				log.severe("Invalid date : it should be only in format : yyyy-MM-dd");	
				monthList=null;
			}		
		}		
		
		return monthList;
		
	}
	
	public List<String> getNonFinaliseMonthlyDate(){
		List<String> nonFinaliseDateList=new ArrayList<String>();
		
		String currentDate=DateUtil.getCurrentTimeStamp("yyyy_MM_dd");
		nonFinaliseDateList.add(currentDate);
		int i=1;
		while(i<LinMobileConstants.CHANGE_WINDOW_SIZE){
			String preDateStr=DateUtil.getModifiedDateStringByDays(currentDate,-i, "yyyy_MM_dd");
			nonFinaliseDateList.add(preDateStr);
			i++;
		}		
		return nonFinaliseDateList;		
	}
	
	public List<String> getNonFinaliseMonthlyDate(String startDate,String endDate){
		List<String> nonFinaliseDateList=new ArrayList<String>();		
		startDate=startDate.replaceAll("-","_");
		endDate=endDate.replaceAll("-","_");
		if(startDate.equals(endDate)){
			nonFinaliseDateList.add(startDate);	
		}else{
			String currentDate=DateUtil.getCurrentTimeStamp("yyyy_MM_dd");
			
			try {
				String startDateStr=DateUtil.getModifiedDateStringByDays(currentDate,-(LinMobileConstants.CHANGE_WINDOW_SIZE-1),
						"yyyy_MM_dd");				
				
				while(!startDateStr.equals(endDate) ){
					if(!nonFinaliseDateList.contains(startDateStr)){
						nonFinaliseDateList.add(startDateStr);
					}
					startDateStr=DateUtil.getModifiedDateStringByDays(startDateStr, 1, "yyyy_MM_dd");				
				}
				if(!nonFinaliseDateList.contains(endDate)){
					nonFinaliseDateList.add(endDate);
				}
				
			} catch (Exception e) {
				log.severe("Invalid date : it should be only in format : yyyy-MM-dd");	
				nonFinaliseDateList=null;
			}			
		}
		
		return nonFinaliseDateList;		
	}
	
	public List<String> getFinaliseTablesFromDataStore(String publisherId,String projectId,String dataSetId, String schemaName){
		IReportDAO reportDAO=new ReportDAO();
		List<String> tableList=new ArrayList<String>();
		try {
			List<FinalisedTableDetailsObj> finaliseTableObjList=reportDAO.loadFinaliseTables(Integer.parseInt(publisherId), 
					projectId, dataSetId);
			if(finaliseTableObjList !=null){
				log.info("finaliseTableObjList:"+finaliseTableObjList.size());
			}else{
				log.info("finaliseTableObjList: 0");
			}
			for(FinalisedTableDetailsObj obj:finaliseTableObjList){
				String tableId=obj.getTableId();
				if(tableId !=null && tableId.contains(schemaName)){
					tableList.add(tableId);
				}
			}
		}catch (DataServiceException e) {
			log.severe("Failed to load finalise table list from datastore..."+e.getMessage());			
		}
		return tableList;
	}
	
	
	public List<String> getNonFinaliseTablesFromDataStore(String publisherId,String projectId,String dataSetId, 
			String schemaName){
		IReportDAO reportDAO=new ReportDAO();
		List<String> tableList=new ArrayList<String>();
		try {
			List<FinalisedTableDetailsObj> nonFinaliseTableObjList=reportDAO.loadNonFinaliseTables(Integer.parseInt(publisherId), 
					projectId, dataSetId);
			if(nonFinaliseTableObjList !=null){
				log.info("nonFinaliseTableObjList:"+nonFinaliseTableObjList.size());
			}else{
				log.info("nonFinaliseTableObjList: 0");
			}
			for(FinalisedTableDetailsObj obj:nonFinaliseTableObjList){
				String tableId=obj.getTableId();
				if(tableId !=null && tableId.contains(schemaName)){
					tableList.add(tableId);
				}
			}
		}catch (DataServiceException e) {
			log.severe("Failed to load finalise table list from datastore..."+e.getMessage());			
		}
		return tableList;
	}
	
	/*
	 * This method returns list of finalised or non-finalised table names from BigQuery 
	 * @see com.lin.web.service.IQueryService#getFinaliseNonFinaliseTablesFromBigQuery(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public List<String> getFinaliseNonFinaliseTablesFromBigQuery(String serviceAccountEmail,String servicePrivateKey,
			String publisherId,String projectId,String dataSetId, String schemaName,boolean finalise){
		
		String finaliseMemcacheKey=projectId+"_"+MemcacheUtil.FINALISE_BQ_TABLES_KEY;
		String nonFinaliseMemcacheKey=projectId+"_"+MemcacheUtil.NON_FINALISE_BQ_TABLES_KEY;
		
		List<String> bqTableList=getAllTablesFromBigQuery(serviceAccountEmail, servicePrivateKey,projectId, dataSetId);	
		List<String> finalisedTableList=MemcacheUtil.getDataListFromCacheByKey(finaliseMemcacheKey);
		List<String> nonFinalisedTableList=MemcacheUtil.getDataListFromCacheByKey(nonFinaliseMemcacheKey);
		
		if( (finalisedTableList ==null || nonFinalisedTableList==null) ){
			log.info("Load both finalise and non finalise tables for this BigQuery Project:"+projectId+" under dataSetId:"+dataSetId);
			finalisedTableList=new ArrayList<>();
			nonFinalisedTableList=new ArrayList<>();
			
			//assert(bqTableList!=null);
			if(bqTableList!=null && bqTableList.size()>0){
				log.info("Tables from BigQuery:"+bqTableList.size());
				for(String tableName:bqTableList){	
					//System.out.println("tableName :"+tableName+" and schemaName : "+schemaName);
					if(tableName.contains(schemaName)){
						if(tableName.contains(":")){
							tableName=tableName.substring(tableName.indexOf(":")+1);
						}					
						if(tableName.contains("_"+LinMobileConstants.DFP_DATA_SOURCE+"_") ||
								tableName.contains(LinMobileConstants.AD_EXCHANGE_TABLE_NAME) ||
								tableName.contains(LinMobileConstants.NEXAGE_CHANNEL_NAME) ||
								tableName.contains(LinMobileConstants.MOJIVA_CHANNEL_NAME) ||
								tableName.contains(LinMobileConstants.UNDERTONE_CHANNEL_NAME) ||
								tableName.contains(LinMobileConstants.AD_EXCHANGE_TABLE_NAME) ){
							nonFinalisedTableList.add(tableName);
						}else{
							finalisedTableList.add(tableName);
						}
						
					}
				}
				
				String memcacheKey=projectId+"_"+schemaName+"_"+MemcacheUtil.FINALISE_BQ_TABLES_KEY;
				MemcacheUtil.setDataListInCacheByKey(finalisedTableList, memcacheKey);	
				
				String memcacheKey2=projectId+"_"+schemaName+"_"+MemcacheUtil.NON_FINALISE_BQ_TABLES_KEY;
				MemcacheUtil.setDataListInCacheByKey(nonFinalisedTableList, memcacheKey2);				
				
			}else{
				log.info("There is no table for this BigQuery Project:"+projectId+" under dataSetId:"+dataSetId);
			}
			
		}
		if(finalise){
			log.info("finalisedTableList:"+finalisedTableList.toString()+" under project:"+projectId+
					" and dataSetId:"+dataSetId);	
			return finalisedTableList;
		}else{
			log.info("nonFinalisedTableList:"+nonFinalisedTableList.toString()+" under project:"+projectId+
					" and dataSetId:"+dataSetId);
			return nonFinalisedTableList;
		}	
			
		
	}
	
	/*
	 * Get all tables from BigQuery for selected project
	 * @see com.lin.web.service.IQueryService#getAllTablesFromBigQuery(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<String> getAllTablesFromBigQuery(String serviceAccountEmail,String servicePrivateKey,
			String projectId,String dataSetId){
		String memcacheKey=projectId+"_"+MemcacheUtil.ALL_BQ_TABLES_KEY;
		List<String> bqTableList=MemcacheUtil.getDataListFromCacheByKey(memcacheKey);
		if(bqTableList==null || bqTableList.size()==0){
			bqTableList=BigQueryUtil.getTables(serviceAccountEmail, servicePrivateKey, projectId, dataSetId);
			MemcacheUtil.setDataListInCacheByKey(bqTableList, memcacheKey);
		}
		return bqTableList;
	}	
	
}
