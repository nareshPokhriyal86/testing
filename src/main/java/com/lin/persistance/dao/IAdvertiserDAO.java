package com.lin.persistance.dao;

import java.util.List;

import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationHeaderDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.service.IBusinessService;

public interface IAdvertiserDAO extends IBusinessService {
	
	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String canpainOrder,String lineItem,String publisherName)throws Exception;
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String campainOrder,String lineItem,String publisherName);
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String canpainOrder,String lineItem,String publisherName);
	
	public List<AdvertiserPerformanceSummaryHeaderDTO> loadPerformanceSummaryHeaderData(String lowerDate, String advertiser, String upperDate, String agency,String publisherName, String Properties) throws Exception;

	public List<AdvertiserPerformerDTO> loadTopPerformerLineItems( String lowerDate, String upperDate, String advertiser, String agency,String publisherName, String properties) throws Exception;

	public List<AdvertiserPerformerDTO> loadNonPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName, String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadMostActiveLineItems(String lowerDate, String upperDate, String compareStartDate,String compareEndDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadTopGainers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName, String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadTopLosers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserPerformerDTO> loadPerformanceMetrics(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserByLocationObj> loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName, String properties) throws Exception;
	
	public List<AdvertiserByLocationObj> loadAdvertiserByMarketGraphData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception;
	
	public void getLineItemForPopUP(String lowerDate, String upperDate, String lineItemName, PopUpDTO popUpObj,String publisherName,String properties) throws Exception;
	
	public List<AdvertiserReallocationHeaderDTO> loadReallocationHeaderDataAdvertiserView(String lowerDate, String upperDate, String canpainOrder, String lineItem);

	public List<AgencyDTO> getAgencyDropDownList(String publisherId, String str);
	
	public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherId ,String agencyId, String str);
	
	public List<OrderDTO> getOrderDropDownList(String publisherId ,String agencyId, String advertiserId,String str);
	
	public List<LineItemDTO> getLineItemDropDownList(String publisherId ,String agencyId, String advertiserId, String orderId, String str);
	
	/*public List<String> loadAdvertiserPropertyDropDownList(String publisherName,long userId,String term);                          24.08.2013*/
	
	public List<String> loadAdvertiserDFPPropertyList(String publisherName,long userId,String term);
	
	/*public List<String> getDFPPropertyName(String[] propertyName);                           24.08.2013*/
	
	public PropertyObj getPropertyObjByName(String propertyName);
	
//	public List<AdvertiserPerformerDTO> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName);
	
	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName, String advertiser, String agency,String properties);
}
