package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationTableDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;

public interface IAdvertiserService extends IBusinessService {

	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String canpainOrder,String lineOrder,String publisherName,String properties);
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName,String properties);
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName,String properties);

	public List<AdvertiserPerformanceSummaryHeaderDTO> loadPerformanceSummaryHeaderData(String lowerDate, String advertiser, String upperDate, String agency,String publisherName,String properties) throws Exception;

	public List<AdvertiserPerformerDTO> loadPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception;

	public List<AdvertiserPerformerDTO> loadNonPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadMostActiveLineItems(String lowerDate, String upperDate, String compareStartDate,String compareEndDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadTopGainers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadTopLosers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadPerformanceMetrics(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public void loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception;
	
	public void loadAdvertiserByMarketGraphData(String lowerDate,String upperDate, String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception;
	
	public PopUpDTO getLineItemForPopUP(String lowerDate, String upperDate, String lineItemName,String publisherName,String properties) throws Exception;
	
	public Map loadReallocationHeaderDataAdvertiserView(DfpServices dfpServices,DfpSession session,String lowerDate, String upperDate, String campainOrder, String lineItem,long userId);
	
	public List<AgencyDTO> getAgencyDropDownList(String publisherName, String str)throws Exception;
	
	public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherName ,String agencyName,String str)throws Exception;
	
	public List<OrderDTO> getOrderDropDownList(String publisherName ,String agencyName, String advertiserName,String str)throws Exception;
	
	public List<LineItemDTO> getLineItemDropDownList(String publisherName ,String agencyName, String advertiserName, String orderName, String str)throws Exception;
	
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherName,long userId,String str);
	
	public List<String> getDFPPropertyName(String propertyName);
	
	public List<AdvertiserReallocationTableDTO> updateReallocationLineItem(DfpServices dfpServices,DfpSession session,String array,long userId,String lowerDate, String upperDate, String campainOrder, String lineItem);
	
	public List<AdvertiserPerformerDTO> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);

	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);
	
}


