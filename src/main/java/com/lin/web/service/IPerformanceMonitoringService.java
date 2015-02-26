package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignFlightDTO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public interface IPerformanceMonitoringService extends IBusinessService {

	public JSONArray loadAllCampaigns(String campaignStatus, long userId, boolean superAdmin, int campaignPerPage, int pageNumber, String searchKeyword, String publisherIdInBQ);

	Map<String, String> getPlacementInformation(Long orderId, long userId, boolean isSuperAdmin);

	public QueryDTO getCampaignQueryDTO(String campaignId,String publisherIdInBQ,String bqSchema);
	
	JSONObject deliveryMetricsData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String orderInfo, String placementInfo, String lineItemPlacementName, String lineItemPlacementIds);

	JSONObject flightLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo, String lineItemPlacementIds, String lineItemPlacementName);

	JSONObject headerData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo);

	JSONObject ctrLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient,String userCompany);

	JSONObject impressionsLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient,String userCompany);

	JSONObject clicksLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo, boolean isClient,String userCompany);

	public JSONObject locationCompleteData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo);

	public JSONObject locationTopCitiesData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo);

	public JSONObject locationTargetData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo);

	public JSONObject richMediaLineChartData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient, String userCompany);

	public JSONArray richMediaDonutChartData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo);
	
	public JSONObject videoData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo);

	public Map<String, String> creativeBarChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient,String userCompany);

	public Map<String, String> deviceBarChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient,String userCompany);

	public Map<String, String> osChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo,boolean isClient,String userCompany);
	
	public void updateCampaignData();

	long dayDurationInFlights(List<SmartCampaignFlightDTO> flightObjList, String startDate, String endDate, String dateFormat);
	
	//Added by Anup for Geo-Census Integration
	public JSONArray getLocationRank(int rankRatio, String rankBy, JSONArray censusArry,String gender);

	public JSONArray getCampaignDetailByPartner(String orderID,String campaignID,String publisherIdInBQ, String partner);
	
	public JSONArray getRUNDspCampaignList();
	
	public JSONArray getRUNDspCampaignDetail(String campaingID, String groupBy, String start, String end);
}


