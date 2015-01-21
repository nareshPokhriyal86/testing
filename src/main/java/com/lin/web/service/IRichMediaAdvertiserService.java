package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationTableDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;

public interface IRichMediaAdvertiserService extends IBusinessService {

	/*public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String canpainOrder,String lineOrder,String publisherName,String properties);
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName,String properties);
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName,String properties);*/
	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String canpainOrder,String lineOrder,String publisherName);
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName);
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String Order,String lineOrder,String publisherName);

	public void loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception;
	
	public void loadAdvertiserByMarketGraphData(String lowerDate,String upperDate, String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception;
	
	public PopUpDTO getLineItemForPopUP(String lowerDate, String upperDate, String lineItemName,String publisherName,String properties) throws Exception;
	
	public Map loadReallocationHeaderDataAdvertiserView(DfpServices dfpServices,DfpSession session,String lowerDate, String upperDate, String campainOrder, String lineItem,long userId);
	
	public List<AgencyDTO> getAgencyDropDownList(String publisherId, String str, long userId)throws Exception;
	
	public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherId ,String agencyName,String str, long userId)throws Exception;
	
	public List<OrderDTO> getOrderDropDownList(String publisherName ,String agencyName, String advertiserName,String str)throws Exception;
	
	public List<LineItemDTO> getLineItemDropDownList(String publisherName ,String agencyName, String advertiserName, String orderName, String str)throws Exception;
	
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherName,long userId,String str);
	
	public List<String> getDFPPropertyName(String propertyName);
	
	public List<AdvertiserReallocationTableDTO> updateReallocationLineItem(DfpServices dfpServices,DfpSession session,String array,long userId,String lowerDate, String upperDate, String campainOrder, String lineItem);
	
    public Map<String,List<AdvertiserPerformerDTO>>  loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);

	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties);
	
	public ForcastLineItemDTO getLineItemForcast(DfpServices dfpServices,DfpSession session,String lineItemId);
	 
	public List<AdvertiserPerformerDTO> loadRichMediaEventPopup(String lowerDate, String upperDate, String lineItemName) throws Exception;
	
	public String loadRichMediaEventGraph(List<CommonDTO> customEvents, String lowerDate, String upperDate,String advertiser, String agency) throws Exception ;

	
}


