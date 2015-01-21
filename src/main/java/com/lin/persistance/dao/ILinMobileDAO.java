package com.lin.persistance.dao;

import java.util.List;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserByMarketObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
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
import com.lin.web.dto.LeftMenuDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PublisherReallocationHeaderDTO;
import com.lin.web.dto.PublisherTrendAnalysisHeaderDTO;
import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.ReconciliationDataDTO;


public interface ILinMobileDAO extends IBaseDao{

	
	public void saveObject(Object obj) throws DataServiceException;
	public void deleteObject(Object obj) throws DataServiceException;
	public List<AdvertiserReportObj> loadPerformingLineItemsForAdvertiser(int limit,String lowerDate,String upperDate) throws DataServiceException;
	public List<AdvertiserReportObj> loadNonPerformingLineItemsForAdvertiser(int limit,String lowerDate,String upperDate) throws DataServiceException;
	public List<AgencyAdvertiserObj> loadAllAgencies() throws DataServiceException;
	public List<AgencyAdvertiserObj> loadAllAdvertisers(String agencyName) throws DataServiceException;
	public List<PublisherViewDTO> getPublisherView(int page) throws DataServiceException;
	public List<PublisherViewDTO> getPublisherViewDTO()throws DataServiceException;
	public List<LeftMenuDTO> getLeftMenuList()throws DataServiceException;
	
	public List<OrderLineItemObj> loadAllOrders() throws DataServiceException;
	public List<OrderLineItemObj> loadAllLineItems(String orderName) throws DataServiceException;
	public List<OrderLineItemObj> loadAllLineItems(long orderId) throws DataServiceException;
	public List<AdvertiserByLocationObj> loadAdvertisersByLocationData(String lowerDate,String upperDate) throws DataServiceException;
	public List<AdvertiserByMarketObj> loadAdvertisersBymarketData(String lowerDate,String upperDate) throws DataServiceException;
	
	public List<PerformanceMetricsObj> loadAdvertiserPerformanceMetrics(int counter,String lowerDate,String upperDate)throws DataServiceException;
	public List<PerformanceMetricsObj> loadAdvertiserPerformanceMetrics(String lowerDate,String upperDate)throws DataServiceException;
	
	public List<CustomLineItemObj> loadMostActiveLineItem(int size,String lowerDate,String upperDate)throws DataServiceException;
	public List<ReallocationDataObj> loadReallocationItemsForAdvertiser(int size,String lowerDate,String upperDate) throws DataServiceException;
	
	public List<AgencyAdvertiserObj> loadAdvertiserById(long advertiserId) throws DataServiceException;
	
	//public List<PublisherChannelObj> loadChannelPerformanceList(String lowerDate, String upperDate,  String compareStartDate,  String compareEndDate, List<String> allChannelName, String publisher) throws Exception;

	
	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(String lowerDate,String upperDate)throws DataServiceException;
	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(int limit, String lowerDate,String upperDate)throws DataServiceException;
	
	public List<SellThroughDataObj> loadSellThroughDataList(String lowerDate, String upperDate, String selectedDFPPropertiesQuery, QueryDTO queryDTO) throws DataServiceException;
	
	public List<PublisherChannelObj> loadPublisherDataList() throws DataServiceException;
	public List<PublisherChannelObj> loadChannels(String publisherName) throws DataServiceException;
	public List<PublisherChannelObj> loadChannelsByName(String channelName) throws DataServiceException;
	
	public List<PublisherChannelObj> loadChannels(String startDate,String endDate,String channelName) throws DataServiceException;
	
	public List<AdvertiserReportObj> loadLineItem(String lowerDate,String upperDate,long lineItemId) throws DataServiceException;
	public List<CustomLineItemObj> loadLineItem(String lowerDate,String upperDate,String lineItemName) throws DataServiceException;
	
	public List<PerformanceMetricsObj> loadLineItemForPerformanceMetrics(String lowerDate,String upperDate,String lineItemName) throws DataServiceException;
	public List<ActualAdvertiserObj> loadActualDataForAdvertiser(int size,String lowerDate,String upperDate) throws DataServiceException;
	public List<ForcastedAdvertiserObj> loadForcastDataForAdvertiser(int size,String lowerDate,String upperDate) throws DataServiceException;
	
	public List<OrderLineItemObj> loadLineItem(long lineItemId) throws DataServiceException;
	
	public PublisherChannelObj loadChannelPerformanceForPopUP(String lowerDate, String upperDate, String id) throws DataServiceException;
	public List<PublisherChannelObj> loadActualDataForPublisher(String lowerDate,String upperDate,String channelId, QueryDTO queryDTO) throws Exception;
	public List<PublisherChannelObj> loadReallocationDataForPublisher(int size,String lowerDate,String upperDate,String publisherName) throws DataServiceException;
	
	public List<SellThroughDataObj> loadSellThroughDataByProperty(String lowerDate,String upperDate,String property) throws DataServiceException;
	
	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(String lowerDate,String upperDate,String property) throws DataServiceException;
	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(String lowerDate,String upperDate,String compareStartDate, String compareEndDate, String channel, String publisher, String channelAndDataSourceQuery) throws Exception;

	public List<PublisherTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderData(String lowerDate,String upperDate,String publisherName,String channelName, String channelAndDataSourceQuery);
	public List<PublisherInventoryRevenueObj> loadInventoryRevenueHeaderData(String lowerDate,String upperDate,String publisherName,String ChannelsStr, String channelAndDataSourceQuery);
	public List<PublisherSummaryObj> loadPublisherSummaryData(String lowerDate, String upperDate, String selectedDFPPropertiesQuery, String channelIds, QueryDTO queryDTO);
	public List<PublisherPropertiesObj> loadAllPerformanceByProperty(String lowerDate,String upperDate, String channelIds, String selectedDFPPropertiesQuery, QueryDTO queryDTO) throws Exception;
	public List<PublisherReallocationHeaderDTO> loadReallocationHeaderData(String lowerDate,String upperDate,String publisherName,List<String> ChannelList);
	/*public List<PublisherChannels> getAllPublisherChannels();*/
	public List<PopUpDTO> loadChannelsPerformancePopUpGraphData(String startDate,String endDate,String channelId, String publisherId, QueryDTO queryDTO) throws Exception;
	/*public List<String> loadPublisherPropertyDropDownList(String publisherName,long userId,String term);                   24.08.2013*/
	public List<ReconciliationDataDTO> loadAllRecociliationData(String startDate, String endDate, String lineItemQuery, QueryDTO queryDTO) throws Exception;
	public List<AdvertiserPerformerDTO> loadCampainTotalDataPublisherViewList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency,String properties);
	public List<AdvertiserPerformerDTO> loadCampainPerformanceDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);
	
}
