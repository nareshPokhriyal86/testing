package com.lin.web.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.google.visualization.datasource.datatable.DataTable;
import com.lin.server.Exception.DataServiceException;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.NonPerformerReportDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByLocationReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformanceBySiteReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.SummaryReportDTO;

public interface INewAdvertiserViewService extends IBusinessService {
	
	public List<NewAdvertiserViewDTO> headerDate(String orderId,String campaignName,String accountName, String publisherIdInBQ);
	public String performarData(String orderId, String publisherIdInBQ);
	public String mostActiveData(String orderId, String publisherIdInBQ);
	public String deliveryMetricsData(String orderId, String publisherIdInBQ);
	public DataTable performanceByLocationChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public List<SummaryReportDTO> summaryReportObject(String orderId,String campaignName,String accountName, String publisherIdInBQ) throws Exception;
	
	public Map<String,String> loadOrdersForAdvertiserOrAgency(String advertiserId,String agencyId, String publisherIdInBQ,long userId);
	
	//public DataTable performanceByAdSizeChartData(String orderId);
	public String performanceByAdSizeChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public List deliveryMetricsReportObject(String orderId, String publisherIdInBQ);
	public DataTable impressionByOSChartData(String orderId, String publisherIdInBQ);
	public DataTable impressionByDeviceChartData(String orderId, String publisherIdInBQ);
	public DataTable impressionBymobileWebVsAppChartData(String orderId, String publisherIdInBQ);
	public Map<String,String> processLineChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public String richMediaEventGraph(List<CommonDTO> customEvents, String campaignId, String publisherId) throws Exception;
	
	public String performanceByOSBarChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public String performanceByDeviceBarChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public String mobileWebVsAppBarChartData(String orderId, boolean isNoise, double threshold, String publisherIdInBQ);
	public List<PerformanceBySiteReportDTO> performanceBySiteReportObject(String orderId, String publisherIdInBQ) throws DataServiceException, IOException;
	public DataTable impressionsByAdSizeChartData(String orderId, String publisherIdInBQ);
	public List<PerformanceByOSReportDTO> performanceByOSReportObject(String orderId, String publisherIdInBQ);
	public List<PerformanceByDeviceReportDTO> performanceByDeviceReportObject(String orderId, String publisherIdInBQ);
	public List<PerformanceByPlacementReportDTO> performanceByPlacementReportObject(String orderId, String publisherIdInBQ);
	public List<PerformanceByAdSizeReportDTO> performanceByAdSizeReportObject(String orderId, String publisherId);
	public List<MostActiveReportDTO> mostActiveReportObject(String orderId, String publisherIdInBQ);
	public List<PerformanceByLocationReportDTO> performanceByLocationReportobject(String orderId, String publisherIdInBQ);
	public List<PerformerReportDTO> PerformerReportObject(String orderId, String publisherIdInBQ);
	public List<NonPerformerReportDTO> NonPerformerReportObject(String orderId, String publisherIdInBQ);
	public Map<String, Object> videoCampaignData(String orderId, String publisherIdInBQ);
	public JSONArray campaignGridData(boolean activeCampaign, int campaignsPerPage, int pageNumber, String publisherIdInBQ, String searchKeyword, String commaSeperatedAccountIds);
}
