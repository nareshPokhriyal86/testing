package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.lin.web.dto.QueryDTO;

public interface IQueryService extends IBusinessService{
	
	public QueryDTO createQueryFromClause(String publisherId, String startDate, String endDate, String schemaName);
	
	public QueryDTO getFinaliseTables(String startDate, String endDate, String publisherId, String schemaName,
			String serviceAccountEmail,	String servicePrivateKey,String projectId,String dataSetId);
	public QueryDTO getNonFinaliseTables(String startDate, String endDate, String publisherId, String schemaName,
			String serviceAccountEmail,	String servicePrivateKey,String projectId,String dataSetId);
	
	public List<String> getFinaliseMonthlyDate(String startDate, String endDate);
	public List<String> getNonFinaliseMonthlyDate();
	public List<String> getNonFinaliseMonthlyDate(String startDate, String endDate);
	public List<String> getFinaliseTablesFromDataStore(String publisherId,String projectId,
			String dataSetId, String schemaName);
	public List<String> getNonFinaliseTablesFromDataStore(String publisherId,String projectId,String dataSetId, 
			String schemaName);	
	
	public List<String> getAllTablesFromBigQuery(String serviceAccountEmail,String servicePrivateKey,
			String projectId,String dataSetId);
	public List<String> getFinaliseNonFinaliseTablesFromBigQuery(String serviceAccountEmail,String servicePrivateKey,
			String publisherId,String projectId,String dataSetId, String schemaName,boolean finalise); 
}
