package com.lin.persistance.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.server.Exception.DataServiceException;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.ChannelPerformancePopUpDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IBusinessService;

public interface INewAdvertiserViewDAO extends IBusinessService {
	
	public List<NewAdvertiserViewDTO> headerData(String orderId,String campaignName,String accountName, QueryDTO queryDTO);
	public List<PerformerReportDTO> performarData(String orderId, QueryDTO queryDTO);
	public List<MostActiveReportDTO> mostActiveData(String orderId, QueryDTO queryDTO);
	public List<NewAdvertiserViewDTO> deliveryMetricsData(String orderId, QueryDTO queryDTO);
	public QueryResponse performanceByLocationChartData(String orderId, boolean isReport, boolean isNoise, double threshold, QueryDTO queryDTO);
	
	/*public Map<String,String> loadAdvertiserAndAgencyForUser(String companyIds,String advertiserIds,String agencyIds)
			throws DataServiceException, IOException;*/
	
	//public QueryResponse performanceByAdSizeChartData(String orderId);
	public List<PerformanceByAdSizeReportDTO>  performanceByAdSizeChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO);
	
	public QueryResponse impressionByOSChartData(String orderId, QueryDTO queryDTO);
	public QueryResponse impressionByDeviceChartData(String orderId, QueryDTO queryDTO);
	public QueryResponse impressionBymobileWebVsAppChartData(String orderId, QueryDTO queryDTO);
	public Map<String,String> loadOrdersForAdvertiserOrAgency(String advertiserId,String agencyId, QueryDTO queryDTO)
			throws DataServiceException, IOException;
	public QueryResponse loadLineChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException;
	public List<AdvertiserPerformerDTO> richMediaEventGraph(String campaignI, QueryDTO queryDTO) throws DataServiceException, IOException;
	public List<PerformanceByOSReportDTO> performanceByOSBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO);
	public List<PerformanceByDeviceReportDTO> performanceByDeviceBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO);
	public List<PerformanceByPlacementReportDTO> mobileWebVsAppBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO);
	public QueryResponse impressionsByAdSizeChartData(String orderId, QueryDTO queryDTO);
	public QueryResponse videoCampaignData(String orderId, QueryDTO queryDTO);
	public List<ChannelPerformancePopUpDTO> campaignGridData(boolean activeCampaign, String commaSeperatedAccountIds, QueryDTO queryDTO);
}
