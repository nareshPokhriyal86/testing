package com.lin.persistance.dao;

import java.io.IOException;
import java.util.List;

import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;

public interface IPerformanceMonitoringDAO extends IBaseDao {

	List<PerformanceMonitoringDTO> loadAllCampaigns(QueryDTO queryDTO);

	List<SmartCampaignObj> getRunningCampaignListSuperUser(String campaignStatus)
			throws Exception;

	List<SmartCampaignObj> getRunningCampaignList(String campaignStatus,
			String companyId) throws Exception;

	QueryResponse headerData(String orderId, String lineItemId, QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse clicksLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse impressionsLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse deliveryMetricsData(String orderId, String lineItemIds, QueryDTO queryDTO, boolean multipleOptionSelected) throws DataServiceException, IOException;

	QueryResponse ctrLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;
	
	QueryResponse locationTargetData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO, String stateStr, String cityStr, String dmaStr) throws DataServiceException, IOException;

	QueryResponse locationCompleteData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;
	
	QueryResponse locationTopCitiesData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse flightLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;
	
	QueryResponse richMediaLineChartData(String orderId, String lineItemId, QueryDTO queryDTO) throws DataServiceException, IOException;
	
	QueryResponse videoData(String orderId, String lineItemIds, QueryDTO queryDTO);

	QueryResponse loadCreativeChartData(String orderId,
			String lineItemId, boolean isNoise, double threshold,
			QueryDTO queryDTO) throws DataServiceException, IOException;

	
	QueryResponse loadOSChartData(String orderId,
			String lineItemId, boolean isNoise, double threshold,
			QueryDTO queryDTO) throws DataServiceException, IOException;
	
	QueryResponse loadDeviceChartData(String orderId,
			String lineItemId, boolean isNoise, double threshold,
			QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse loadOperatingSystemImpressionClicksChartDate(String orderId,
			String lineItemId, boolean isNoise, double threshold,QueryDTO queryDTO)throws DataServiceException,
			IOException;

	QueryResponse loadOperatingTotalGoal(String orderId, String lineItemId,
			String targetOS, QueryDTO queryDTO)throws DataServiceException,
			IOException;

	QueryResponse loadCreativeImpressionClicksChartDate(String orderId,
			String lineItemId, boolean isNoise, double threshold,
			QueryDTO queryDTO) throws DataServiceException, IOException;

	QueryResponse loadDeviceImpressionClicksChartDate(String orderId,
			String lineItemId,boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException,
			IOException;


	QueryResponse loadRMCustomEventList(String orderId, String lineItemId,
			QueryDTO queryDTO)throws DataServiceException,
			IOException;

	QueryResponse loadRMCustomEventCards(String orderId, String lineItemId,
			QueryDTO queryDTO)throws DataServiceException,
			IOException;


	void updateSmartCampaignData();
	
	QueryResponse getLocationRank(String mainPopulationColumn , int rankRatio, String rankBy,StringBuffer rankStmt, StringBuffer categoryStmt, StringBuffer subQueryCategoryStmt,QueryDTO queryDTO);

	QueryResponse getProductPerformance(String groupBy,String productTable,QueryDTO queryDTO, List<String> adUnits);
	
	//Added By Anup
	QueryResponse getCampaignDetailByPartner(String OrderID, List<Long> lineItemIDs, QueryDTO queryDTO);
}
