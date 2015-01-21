package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CustomLineItemObj;
import com.lin.server.bean.ForcastedAdvertiserObj;
import com.lin.server.bean.OrderLineItemObj;
import com.lin.server.bean.PerformanceMetricsObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherInventoryRevenueObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.PublisherSummaryObj;
import com.lin.server.bean.ReallocationDataObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LeftMenuDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.PublisherReallocationHeaderDTO;
import com.lin.web.dto.PublisherReportChannelPerformanceDTO;
import com.lin.web.dto.PublisherReportHeaderDTO;
import com.lin.web.dto.PublisherTrendAnalysisHeaderDTO;
import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.dto.ReconciliationDataDTO;


public interface ILinMobileBusinessService extends IBusinessService{
	
	public List<LineItemDTO> getLineItems(long orderId);
	public List<AdvertiserReportObj> loadPerformingLineItems(String lowerDate,String upperDate);
	public List<AdvertiserReportObj> loadNonPerformingLineItems(String lowerDate,String upperDate);
	public void insertPublisherViewDTO(List<PublisherViewDTO> publisherViewList);
	public List<PublisherViewDTO>  getPublisherViewDTO();
	public List<PublisherViewDTO> getPublisherViewDTO(int pageNo);
	public List<String> loadAgencies();
	public List<AgencyAdvertiserObj> loadAdvertisers(String agencyName);
	
	public List<AgencyAdvertiserObj> loadAdvertiserDetails(long advertiserId);
	
	public void insertLeftMenuDTO(List<LeftMenuDTO> leftMenuItemList);
	public List<LeftMenuDTO> getLeftMenuList();

	public List<PerformanceMetricsObj> getAdvertiserPerformanceMetrics(int counter,String lowerDate,String upperDate);
	public List<PerformanceMetricsObj> getAdvertiserPerformanceMetrics(String lowerDate,String upperDate);
	public List<CustomLineItemObj> getMostActiveLineItems(String lowerDate,String upperDate);	
	public List<CustomLineItemObj> getTopGainersLineItems(String lowerDate,String upperDate);
	public List<CustomLineItemObj> getTopLosersLineItems(String lowerDate,String upperDate);
	
	public void loadAdvertisersByLocationData(String lowerDate,String upperDate,StringBuilder sbStr);
	
	public void loadAdvertisersByMarketData(String lowerDate,String upperDate,StringBuilder sbStr);
	
	/*public List<PublisherChannelObj> getChannelPerformanceList(String lowerDate, String upperDate,  String compareStartDate,  String compareEndDate, String allChannelName,String publisher);*/
	
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(String lowerDate,String upperDate);
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(int limit,String lowerDate,String upperDate);
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(String lowerDate,String upperDate, String compareStartDate, String compareEndDate, String channel,String publisher);
	
	public List<SellThroughDataObj> getSellThroughDataList(String lowerDate,String upperDate, String publisherName, long publisherId, long userId);
	//public List<SellThroughDataObj> getSellThroughDataList(int counter,String lowerDate,String upperDate);
	
	public List<PublisherChannelObj> getPublisherDataList(String publisherName);
	public List<String> getPublishers();
	
	public List<PublisherChannelObj> loadChannelsByPublisher(String publisherName);
	public List<PublisherChannelObj> loadChannelsByName(String channelName);

	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,long lineItemId);
	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,String lineItemName);
	public PopUpDTO getPerformanceMetricLineItemForPopUP(String lowerDate,String upperDate,String lineItemName);
	
	public List<String> loadOrders();	
	public List<OrderLineItemObj> loadLineItemName(String orderName);
	public List<OrderLineItemObj> loadLineItemName(long orderId);
	public List<OrderLineItemObj> loadLineItem(long lineItemId);
	public List<ReallocationDataObj> loadReallocationItems(String lowerDate,String upperDate);
	public List<ActualAdvertiserObj> loadActualAdvertiserData(String lowerDate,String upperDate);
	public List<ForcastedAdvertiserObj> loadForcastAdvertiserData(String lowerDate,String upperDate);
	
	
	public List<PublisherChannelObj> loadActualPublisherData(String lowerDate,String upperDate,String publisherName,String channelList);
	
	public List<PublisherChannelObj> loadActualPublisherData(String lowerDate,String upperDate,String channelName);
	
	public PopUpDTO getChannelPerformancePopUP(String lowerDate,String upperDate,String channelName, String publisher);
	public List<PublisherChannelObj> loadReallocationPublisherData(String lowerDate,String upperDate,String publisherName);
	public List<PublisherChannelObj> loadReallocationGraphPublisherData(String lowerDate,String upperDate,String publisherName);
	
	public PopUpDTO getSellThroughPopUP(String lowerDate,String upperDate,String propertyName);
	
	public PopUpDTO getPropertyPopup(String lowerDate,String upperDate,String propertyName);
	public List<PublisherTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderData(String lowerDate,String upperDate,String publisherName,String channelName);
	public List<PublisherInventoryRevenueObj> loadInventoryRevenueHeaderData(String lowerDate,String upperDate,String publisherName,String channelName);
	public List<PublisherSummaryObj> loadPublisherSummaryData(String lowerDate,String upperDate,String publisherName, long publisherId, long userId, String channelNames);
	public Map<String, List<PublisherPropertiesObj>> loadAllPerformanceByProperty(String lowerDate,String upperDate,String publisherName, long publisherId, long userId,String channels,String applyFlag) throws Exception;
	public List<PublisherReallocationHeaderDTO> loadReallocationHeaderData(String lowerDate,String upperDate,String publisherName,String channelName);
	public List<CommonDTO> getAllPublishersByPublisherIdAndUserId(String publisherName, List<String> channelsNameList, long userId);
	/*public List<String> getChannelsTypeListByPublisherName(String publisherName);*/
	/*public String getCommaSeperatedChannelsName(String publisherName);*/
	public String loadPerformanceChannelPopUpGraphDataFromBigQuery(String lowerDate,String upperDate,String channelName, String publisher);
	public List<PropertyDropDownDTO> loadPublisherPropertyDropDownList(String publisherName,long userId,String term);
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherName,long userId,String str);
	public String getCommaSeperatedChannelsNameByPublisherIdAndUserId(String publisherId, long userId, List<CompanyObj> companyObjList) throws Exception;
	public String getCommaSeperatedChannelsNameByChannelIdList(List<String> channelIdList) throws Exception;
	public String getCommaSeperatedChannelsName(String publisherName) throws Exception;
	public void loadAllReconciliationData(String startDate, String endDate, String publisherId, long userId, List<ReconciliationDataDTO> reconciliationSummaryDataList, List<ReconciliationDataDTO> reconciliationDetailsDataList) throws Exception;
	public List<LineItemDTO> loadCampaignTraffickingData(DfpServices dfpServices,DfpSession session,String lowerDate, String upperDate,long userId)throws Exception;
	public List<ForcastLineItemDTO> getLineItemForcasts(DfpServices dfpServices,DfpSession session,String[] lineItemIds) throws Exception;
	public Map<String,List<AdvertiserPerformerDTO>> loadCampainTotalDataPublisherViewList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);
	public List<AdvertiserPerformerDTO> loadCampainPerformanceDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);
	public PublisherReportHeaderDTO getHeaderData(String allChannelName, List<PublisherSummaryObj> currentDateSummaryList, List<PublisherSummaryObj> compareDateSummaryList);
	public Map getChannelPerformanceData(String allChannelName, List<PublisherSummaryObj> currentDateSummaryList, List<PublisherSummaryObj> compareDateSummaryList);
	public Map getPerformanceByPropertyData(String channelName, List<PublisherPropertiesObj> performanceByPropertyCurrentList, List<PublisherPropertiesObj> performanceByPropertyCompareList);
	public String getPublisherBQId(String publisherName);
	public Map<String,String> processPublisherLineChartData(String lowerDate,String upperDate,String publisherName,String channelName);
}

