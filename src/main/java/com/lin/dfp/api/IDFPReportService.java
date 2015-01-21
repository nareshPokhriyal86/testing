package com.lin.dfp.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.web.dto.LineItemDTO;

public interface IDFPReportService {

	
	public String getDFPReport(DfpServices dfpServices, DfpSession session,String start,String end)
	   throws Exception;
	
	public  String getDFPReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds,String targetFilter) throws Exception;
	
	public  String getLinDigitalReport(DfpServices dfpServices, DfpSession session,
			String start,String end,long orderId) throws Exception;
	
	public  String readCSVGZFile(String url) throws IOException;
	public  List<String> readCSVGZFileAndSplit(String url);
	
	public  String getDFPReportByLocation(DfpServices dfpServices, DfpSession session,
			String start,String end,String targetPlatform) throws Exception;
	
	public  String getDFPReportByLocation(DfpServices dfpServices, DfpSession session,
			String start,String end,int lineItemTypeId) throws Exception;
	
	public  String getSellThroughReport(DfpServices dfpServices, DfpSession session) throws Exception;
	
	public  String getLinDigitalPerformanceByLocationReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception;
	
	public  String getLinOneDFPReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception;
	
	public  String getLinDigitalLinOneRichMediaReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception;
	
	public  String getLinDigitalRichMediaCustomEventReport(DfpServices dfpServices, DfpSession session,
			String start,String end,long orderId) throws Exception;
	
	public  void getForcastedData(DfpServices dfpServices, DfpSession session)
			throws Exception;
	
	public  String getAdExchangeDataFromDFPReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception;
	
	public  String getLinDigitalReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds)  throws Exception;
	
	public  String getRichMediaCustomEventReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception;
	
	public  List<Long> getAllActiveMobileLineItemsOrderIds(DfpServices dfpServices, DfpSession session)
	        throws Exception;

	public  String getDFPTargetReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds,String targetFilter) throws Exception;
	
	public  String getLinDigitalTargetReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception;
	
	public  String getDFPLinMediaReportForMobilePlatform(DfpServices dfpServices, 
			DfpSession session,String start,String end) throws Exception;	

	public  String getDFPTargetReportByAccountIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds,String targetFilter);
	
	
	public  String getDFPReportByAdUnitIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> adUnitIds) throws Exception;
	
	/*public  String getDFPReportByAccountIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> advertiserIds) throws Exception;*/
	public  String getDFPReportByAccountIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> accountIds) throws ApiException_Exception, InterruptedException ;
	
	public  String getDFPReportByLocationByAccountIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception;
	
	public  String getRichMediaCustomEventReportByAccountId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception;
	
	public  String getRichMediaVideoCampaignReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception;
	public  String getRichMediaVideoCampaignReportByByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception;

	public List<LineItemDTO> getAdUnitChilds(DfpServices dfpServices, DfpSession dfpSession, 
			String parentId, String parentName)  throws Exception;

	public boolean getTopLevelAdUnitsFromDFP(DfpServices dfpServices,
			DfpSession dfpSession, String adServerId, String adServerUsername,
			String prefixStr, List<LineItemDTO> siteList,
			StringBuilder stringBuilder)  throws Exception;

	List<AdUnitHierarchy> loadAllChildsInHierarchy(DfpServices dfpServices, DfpSession dfpSession, String adServerId, Map<String, String> parentIdMap, StringBuilder summary) throws Exception;
	
	/*public  String getDFPReportByOrderIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> orderIds) throws Exception;*/
	public  String getDFPReportByOrderIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> orderIds) throws ApiException_Exception, InterruptedException;
	public  String getDFPTargetReportByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds);
	public  String getDFPReportByLocationByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception;	
	public  String getRichMediaCustomEventReportByOrderId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception;
	public List<AdUnitHierarchy> getRecentlyUpdatedAdUnitsHierarchy(DfpServices dfpServices, DfpSession dfpSession)
			throws Exception;
	
	public  String getDFPReportByAdUnitsWithCity(DfpServices dfpServices, DfpSession session,List<String> adUnitIds,
			String start,String end) throws Exception;
	
	public String getDFPReportByOrderIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws ApiException_Exception, InterruptedException;
	
	public  String getDFPReportByLocationByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception;
	
	public  String getRichMediaCustomEventReportByOrderId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception;
	
	public  String getRichMediaVideoCampaignReportByByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception;
	
	public  String getDFPTargetReportByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception;
}
