package com.lin.web.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ComputedStatus;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.DateTime;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Money;
import com.google.api.ads.dfp.jaxws.v201403.Order;
import com.google.api.ads.dfp.jaxws.v201403.OrderPage;
import com.google.api.ads.dfp.jaxws.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
import com.google.api.ads.dfp.jaxws.v201403.TargetPlatform;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.persistance.dao.IAdvertiserDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.AdvertiserDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationHeaderDTO;
import com.lin.web.dto.AdvertiserReallocationTableDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.service.IAdvertiserService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;



public class AdvertiserService implements IAdvertiserService{
	private static final Logger log = Logger.getLogger(AdvertiserService.class.getName());
	
	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String order,String lineOrder,String publisherName,String properties){
		log.info("in advertiser service loadTrendAnalysisHeaderDataAdvertiserView");
		List<AdvertiserTrendAnalysisHeaderDTO> list = new ArrayList<AdvertiserTrendAnalysisHeaderDTO>();
		String replaceStr = "\\\\'";
		
		if(order!=null){
			order = order.replaceAll("'", replaceStr);
			order = order.replaceAll(",", "','");
		}else {
			order = "";
		}
		if(lineOrder!=null){
			lineOrder = lineOrder.replaceAll("'", replaceStr);
			lineOrder = lineOrder.replaceAll(",", "','");
		}else {
			lineOrder = "";
		}
	
		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
/*		list = advertiserDAO.loadTrendAnalysisHeaderDataAdvertiserView(lowerDate, upperDate, order, lineOrder, publisherName,properties);
		if(list!=null && list.size()!=0){
			log.info("list size from advertiser DAO : "+list.size());
		}*/
		
		list = MemcacheUtil.getTrendAnalysisHeaderDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineOrder);
		if(list == null || list.size() <= 0)
		{
			try
			{
			//IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			list = advertiserDAO.loadTrendAnalysisHeaderDataAdvertiserView(lowerDate, upperDate, order, lineOrder, publisherName);
			MemcacheUtil.setTrendAnalysisHeaderDataAdvertiserView(list,lowerDate,upperDate,publisherName,order,lineOrder);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("TrendAnalysis Header Data for Advertiser View found from memcache:");
		}
		
		
		return list;
		
	}
	
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String order,String lineItem,String publisherName,String properties){
		log.info("inside loadTrendAnalysisActualDataAdvertiserView of advertiserService");
		
		List<AdvertiserTrendAnalysisActualDTO> list = new ArrayList<AdvertiserTrendAnalysisActualDTO>();
		String replaceStr = "\\\\'";
		if(order!=null){
			order = order.replaceAll("'", replaceStr);
			order = order.replaceAll(",", "','");
		}else {
			order = "";
		}
		if(lineItem!=null){
			lineItem = lineItem.replaceAll("'", replaceStr);
			lineItem = lineItem.replaceAll(",", "','");
		}else {
			lineItem = "";
		}
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
//		list = advertiserDAO.loadTrendAnalysisActualDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName,properties);
		
		list = MemcacheUtil.getTrendAnalysisActualDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineItem);
		if(list == null || list.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			list = advertiserDAO.loadTrendAnalysisActualDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName);
			MemcacheUtil.setTrendAnalysisActualDataAdvertiserView(list,lowerDate,upperDate,publisherName,order,lineItem);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("TrendAnalysis Actual Data for Advertiser View found from memcache:");
		}
		
		return list;
		
	}

	@Override
	public List<AdvertiserPerformanceSummaryHeaderDTO> loadPerformanceSummaryHeaderData(
					String lowerDate, String advertiser, String upperDate, String agency,String publisherName,String properties) throws Exception {
		
		String replaceStr = "\\\\'";
//		List<AdvertiserPerformanceSummaryHeaderDTO> performanceSummaryHeaderDataList = new ArrayList<AdvertiserPerformanceSummaryHeaderDTO>();
		List<AdvertiserPerformanceSummaryHeaderDTO> performanceSummaryHeaderDataList = null;
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		performanceSummaryHeaderDataList = MemcacheUtil.getPerformanceSummaryHeaderData(lowerDate,upperDate,publisherName,advertiser,agency,properties);
		if(performanceSummaryHeaderDataList == null || performanceSummaryHeaderDataList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			performanceSummaryHeaderDataList = advertiserDAO.loadPerformanceSummaryHeaderData(lowerDate,advertiser,upperDate,agency, publisherName,properties);
			MemcacheUtil.setPerformanceSummaryHeaderData(performanceSummaryHeaderDataList,lowerDate,upperDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Performance Summary Header Data found from memcache:");
		}
		
		return performanceSummaryHeaderDataList;
	}
	
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String order,String lineItem,String publisherName,String properties){
		log.info("inside loadTrendAnalysisForcastDataAdvertiserView of advertiserService");
		
//		List<AdvertiserTrendAnalysisForcastDTO> list = new ArrayList<AdvertiserTrendAnalysisForcastDTO>();
		List<AdvertiserTrendAnalysisForcastDTO> list = null;
		String replaceStr = "\\\\'";
		if(order!=null){
			order = order.replaceAll("'", replaceStr);
			order = order.replaceAll(",", "','");
		}else {
			order = "";
		}
		if(lineItem!=null){
			lineItem = lineItem.replaceAll("'", replaceStr);
			lineItem = lineItem.replaceAll(",", "','");
		}else {
			lineItem = "";
		}
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
//		list = advertiserDAO.loadTrendAnalysisForcastDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName,properties);
		
		list = MemcacheUtil.getTrendAnalysisForcastDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineItem);
		if(list == null || list.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			list = advertiserDAO.loadTrendAnalysisForcastDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName);
			MemcacheUtil.setTrendAnalysisForcastDataAdvertiserView(list,lowerDate,upperDate,publisherName,order,lineItem);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Trend Analysis Forcasted Data found from memcache:");
		}
		
		return list;
		
	}
	
	@Override
	public List<AdvertiserPerformerDTO> loadPerformerLineItems(String lowerDate,String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception{
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> topPerformerLineItemsList=null;
		//IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		//topPerformerLineItemsList=advertiserDAO.loadTopPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
		
		topPerformerLineItemsList = MemcacheUtil.getTopPerformerLineItemsList(lowerDate,upperDate,publisherName,advertiser,agency,properties);
		if(topPerformerLineItemsList == null || topPerformerLineItemsList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			topPerformerLineItemsList = advertiserDAO.loadTopPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
			MemcacheUtil.setTopPerformerLineItemsList(topPerformerLineItemsList,lowerDate,upperDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Top Performer Line Items found from memcache:");
		}
		
		return topPerformerLineItemsList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadNonPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> topNonPerformerLineItemsList=null;
		//IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		//topNonPerformerLineItemsList=advertiserDAO.loadNonPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
		
		topNonPerformerLineItemsList = MemcacheUtil.getTopNonPerformerLineItemsList(lowerDate,upperDate,publisherName,advertiser,agency,properties);
		if(topNonPerformerLineItemsList == null || topNonPerformerLineItemsList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			topNonPerformerLineItemsList=advertiserDAO.loadNonPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
			MemcacheUtil.setTopNonPerformerLineItemsList(topNonPerformerLineItemsList,lowerDate,upperDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Top Non Performer Line Items found from memcache:");
		}
		
		return topNonPerformerLineItemsList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadMostActiveLineItems(String lowerDate, String upperDate, String compareStartDate,String compareEndDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> mostActiveLineItemList=null;
		//IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		
		mostActiveLineItemList = MemcacheUtil.getMostActiveLineItemList(lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
		if(mostActiveLineItemList == null || mostActiveLineItemList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			mostActiveLineItemList=advertiserDAO.loadMostActiveLineItems(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
			MemcacheUtil.setMostActiveLineItemList(mostActiveLineItemList,lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Top Most Active Line Items found from memcache:");
	    }
		
		return mostActiveLineItemList;
	}
	
	@Override
	public List<AdvertiserPerformerDTO> loadTopGainers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> topGainersLineItemList=null;
	//	IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		
		//topGainersLineItemList=advertiserDAO.loadTopGainers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
		
		topGainersLineItemList = MemcacheUtil.getTopGainersLineItemList(lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
		if(topGainersLineItemList == null || topGainersLineItemList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			topGainersLineItemList=advertiserDAO.loadTopGainers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
			MemcacheUtil.setTopGainersLineItemList(topGainersLineItemList,lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Top Gainer Line Items found from memcache:");
	    }
		
		return topGainersLineItemList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadTopLosers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> topLosersLineItemList=null;
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		//topLosersLineItemList=advertiserDAO.loadTopLosers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName, properties);
		
		topLosersLineItemList = MemcacheUtil.getTopLosersLineItemList(lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
		if(topLosersLineItemList == null || topLosersLineItemList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			topLosersLineItemList=advertiserDAO.loadTopLosers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName, properties);
			MemcacheUtil.setTopLosersLineItemList(topLosersLineItemList,lowerDate,upperDate,compareStartDate,compareEndDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Top Looser Line Items found from memcache:");
	    }
		
		return topLosersLineItemList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadPerformanceMetrics(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserPerformerDTO> performanceMetricsList=null;
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
//		performanceMetricsList=advertiserDAO.loadPerformanceMetrics(lowerDate,upperDate,advertiser,agency, publisherName, properties);
		
		performanceMetricsList = MemcacheUtil.getPerformanceMetricsList(lowerDate,upperDate,publisherName,advertiser,agency,properties);
		if(performanceMetricsList == null || performanceMetricsList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			performanceMetricsList=advertiserDAO.loadPerformanceMetrics(lowerDate,upperDate,advertiser,agency, publisherName, properties);
			MemcacheUtil.setPerformanceMetricsList(performanceMetricsList,lowerDate,upperDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Performance Metrics found from memcache:");
	    }
		
		return performanceMetricsList;
	}
	
	@Override
	public void loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency, StringBuilder sbStr ,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserByLocationObj>  advertiserByLocationObjList =null;
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
/*		advertiserByLocationObjList = advertiserDAO.loadAdvertisersByLocationData(lowerDate, upperDate, advertiser, agency, publisherName, properties);
		
		if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State',  'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState().replaceAll("'", "")+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");	
		}*/
		
		advertiserByLocationObjList = MemcacheUtil.getAdvertisersByLocationData(lowerDate, upperDate, advertiser, agency, publisherName);
		if(advertiserByLocationObjList == null || advertiserByLocationObjList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			advertiserByLocationObjList = advertiserDAO.loadAdvertisersByLocationData(lowerDate, upperDate, advertiser, agency, publisherName, properties);
			MemcacheUtil.setAdvertisersByLocationData(advertiserByLocationObjList,lowerDate,upperDate,advertiser, agency, publisherName);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Advertiser By Location Data found from memcache:");
	    }
		
		if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State',  'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState().replaceAll("'", "")+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");	
		}
	}

	@Override
	public void loadAdvertiserByMarketGraphData(String lowerDate, String upperDate,String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception {
		String replaceStr = "\\\\'";
		List<AdvertiserByLocationObj>  advertiserByLocationObjList =null;
//		IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
/*		advertiserByLocationObjList = advertiserDAO.loadAdvertiserByMarketGraphData(lowerDate, upperDate,advertiser,agency, publisherName, properties);
		
		if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State', 'Station', 'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState()+"','"+object.getState()+" : "+object.getStation()+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");
		}*/
		
		advertiserByLocationObjList = MemcacheUtil.getAdvertiserByMarketGraphData(lowerDate,upperDate,publisherName,advertiser,agency,properties);
		if(advertiserByLocationObjList == null || advertiserByLocationObjList.size() <= 0)
		{
			try
			{
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			advertiserByLocationObjList = advertiserDAO.loadAdvertiserByMarketGraphData(lowerDate, upperDate,advertiser,agency, publisherName, properties);
			MemcacheUtil.setAdvertiserByMarketGraphData(advertiserByLocationObjList,lowerDate,upperDate,publisherName,advertiser,agency,properties);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Advertiser By Market Data found from memcache:");
	    }
		
		if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State', 'Station', 'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState()+"','"+object.getState()+" : "+object.getStation()+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");
		}
		System.out.println("Array : "+sbStr);
		
	}
	
	@Override
	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,String lineItemName,String publisherName,String properties) throws Exception {
		log.info("inside getLineItemForPopUP of AdvertiserService");
		String replaceStr = "\\\\'";
		
		if(lineItemName != null && !lineItemName.trim().equalsIgnoreCase("")) {
			lineItemName = lineItemName.replaceAll("'", replaceStr);
			lineItemName = lineItemName.replaceAll("&amp;", "&");
		}
		
		
		PopUpDTO popUpObj = null;
		
		popUpObj = MemcacheUtil.getAdvertiserLineItemPopUpData(lowerDate, upperDate, lineItemName, publisherName);
		if(popUpObj == null) {
			popUpObj = new PopUpDTO();
			IAdvertiserDAO advertiserDAO=new AdvertiserDAO();
			advertiserDAO.getLineItemForPopUP(lowerDate,upperDate,lineItemName,popUpObj, publisherName, properties);
			
	        StringBuffer strBuffer = new StringBuffer();
			
	        if(popUpObj != null && popUpObj.getPopUpGraphDataList() != null && popUpObj.getPopUpGraphDataList().size() > 0) {
				strBuffer.append("[['Days',   'Impression']");
				for (CommonDTO commonDTO : popUpObj.getPopUpGraphDataList()) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(commonDTO.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+commonDTO.getImpressionsDelivered());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				popUpObj.setChartData(strBuffer.toString());
				
				log.info("strBuffer.toString():"+strBuffer.toString());
				
				MemcacheUtil.setAdvertiserLineItemPopUpData(popUpObj, lowerDate, upperDate, lineItemName, publisherName);
	        }
		}
		return popUpObj;
	}
	
	
	public List<AgencyDTO> getAgencyDropDownList(String publisherName, String str)
	{
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		if(publisherName!=null){
			publisherName = publisherName.replaceAll("'",replaceStr);
		}
		
		IAdvertiserDAO advDAO=new AdvertiserDAO();
		return advDAO.getAgencyDropDownList(publisherName,str);
	}
	
	public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherName ,String agencyName, String str)
	{
		String replaceStr = "\\\\'";
		
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		if(publisherName!=null){
			publisherName = publisherName.replaceAll("'", replaceStr);
		}
		if(agencyName!=null){
			agencyName = agencyName.replaceAll("'", replaceStr);
		}
		IAdvertiserDAO advDAO=new AdvertiserDAO();
		return advDAO.getAdvertiserDropDownList(publisherName, agencyName, str);
	}
	
	public List<OrderDTO> getOrderDropDownList(String publisherName ,String agencyName, String advertiserName, String str)
	{
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		if(publisherName!=null){
			publisherName = publisherName.replaceAll("'", replaceStr);
		}
		if(agencyName!=null){
			agencyName = agencyName.replaceAll("'", replaceStr);
		}
		if(advertiserName!=null){
		advertiserName = advertiserName.replaceAll("'", replaceStr);
		}
		IAdvertiserDAO advDAO=new AdvertiserDAO();
		return advDAO.getOrderDropDownList(publisherName, agencyName, advertiserName, str);
	}
	
	public List<LineItemDTO> getLineItemDropDownList(String publisherName ,String agencyName, String advertiserName, String orderName, String str)
	{
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		if(publisherName!=null){
			publisherName = publisherName.replaceAll("'", replaceStr);
		}
		if(agencyName!=null){
			agencyName = agencyName.replaceAll("'", replaceStr);
		}
		if(advertiserName!=null){
		advertiserName = advertiserName.replaceAll("'", replaceStr);
		}
		if(orderName!=null){
			orderName = orderName.replaceAll("'", replaceStr);
		}
		
		IAdvertiserDAO advDAO=new AdvertiserDAO();
		return advDAO.getLineItemDropDownList(publisherName, agencyName, advertiserName, orderName, str);
	}
	
	
	public Map loadReallocationHeaderDataAdvertiserView(DfpServices dfpServices,DfpSession session,String lowerDate, String upperDate, String campainOrder, String lineItem,long userId){
			
		 List<LineItem> lineItemList=new ArrayList<LineItem>();	
		 List<Order> orderList = new ArrayList<Order>();
		 Order orderObj =null;
		 Map reallocationObjectsMap  = new  HashMap();
		 // Map <String,List<LineItem>> testMap =new HashMap<String, List<LineItem>>();
		 List<AdvertiserReallocationHeaderDTO> reallocationHeaderList = new ArrayList<AdvertiserReallocationHeaderDTO>();
		 List<AdvertiserReallocationTableDTO> advertiserReallocationTableList = new ArrayList<AdvertiserReallocationTableDTO>();
		 OrderServiceInterface orderService = dfpServices.get(session, OrderServiceInterface.class);
		 LineItem lineItemObj;
		 AdvertiserReallocationHeaderDTO reallocationHeaderDTOObj  = new AdvertiserReallocationHeaderDTO();
		 AdvertiserReallocationTableDTO reallocationTableDTOObj = new AdvertiserReallocationTableDTO();
		 long totalClicks = 0; 
		 double totalECPM = 0;
		 double totalBudget = 0;
		 long totalBookedImpressions = 0;
		 long totalDeliveredImpressions = 0;
		 long totalOverUnder = 0;
		 double totalCTR = 0;
		 double totalRevenueDelivered = 0;
		 double totalRevenueRemaining = 0;
		 long totalRevisedBudget = 0;
		 long totalRevisedBookedImpressions = 0;
		 double totalRevenueToBeDelivered = 0;
		 Date date = new Date();
		 String dateStr = DateUtil.getFormatedDate(date, "yyyy-MM-dd");
		 //String targetPlatform="WEB";
	   //  String costType="CPM";
	     Double revenueDelivered = 0.0;
	   
	      lowerDate="2013-03-18T00:00:00";
		  upperDate="2013-12-31T00:00:00";
		 try{
			 String replaceStr = "\\\\'";
				if(campainOrder!=null){
					campainOrder = campainOrder.replaceAll("'", replaceStr);
				}
				  campainOrder = "131407327";
	   Statement orderStatement=new Statement();
	    String query = "WHERE  id = '"+campainOrder +"' AND endDateTime <='"+upperDate+"' AND startDateTime >='"+lowerDate+"'";
	    /* String query="WHERE  orderName = '"+campainOrder+"' AND status = '"
	              +ComputedStatus.DELIVERING+"' AND targetPlatform = '"
	              +targetPlatform
	             +"' AND costType= '" +costType
	             +"' AND endDateTime <='"+upperDate+"' AND startDateTime >='"+lowerDate+"'";*/
	    	  log.info("query:"+query);
	    	  
	    	 // DateTimes.toDateTime(endDateTime);
	    	 orderStatement.setQuery(query);
	         OrderPage orderPage = orderService.getOrdersByStatement(orderStatement);
	         if(orderPage.getResults()!=null && orderPage.getResults().size()>0){
	        	 orderList.add(orderPage.getResults().get(0));
	         }
	         if(orderList!=null && orderList.size()>0){
	        	 orderObj = orderList.get(0); 
	         }
	         if(orderObj!=null && orderObj.getTotalClicksDelivered().toString()!=null){
	        	 reallocationHeaderDTOObj.setClicks(orderObj.getTotalClicksDelivered().toString());
	         }
	         if(orderObj!=null && orderObj.getTotalImpressionsDelivered().toString()!=null){
	        	 reallocationHeaderDTOObj.setImpressions(orderObj.getTotalImpressionsDelivered().toString());
	         }
	         if(orderObj!=null && orderObj.getTotalBudget().toString()!=null){
	        	 double budget =  ((double) orderObj.getTotalBudget().getMicroAmount()) / 1000000;
	        	 reallocationHeaderDTOObj.setTotalBudget(String.valueOf(budget));
	         }
	         if(orderObj!=null && orderObj.getStartDateTime().toString()!=null){
	        	 reallocationHeaderDTOObj.setStartDate(getCustomDate(orderObj.getStartDateTime()));
	         }
	         if(orderObj!=null && orderObj.getEndDateTime().toString()!=null){
	        	 reallocationHeaderDTOObj.setEndDate(getCustomDate(orderObj.getEndDateTime()));
	         }
	         if(orderObj!=null && orderObj.getTotalClicksDelivered().toString()!=null && orderObj.getTotalImpressionsDelivered().toString()!=null){
	        	 double ctr =((double)orderObj.getTotalClicksDelivered() /(double)orderObj.getTotalImpressionsDelivered())*100;
       		  reallocationHeaderDTOObj.setCTR(ctr+"");
	         }
	         reallocationHeaderDTOObj.setDate(dateStr);
	        // System.out.println(dateStr);
	       
	         reallocationObjectsMap.put("HeaderObject", reallocationHeaderDTOObj);
	         
	         LineItemServiceInterface lineItemService=dfpServices.get(session, LineItemServiceInterface.class);
	         
	         query="WHERE  orderId = '"+campainOrder+"' AND status = '"
             +ComputedStatus.READY+"' AND (targetPlatform = '"
             +TargetPlatform.WEB + "'or targetPlatform = '"+TargetPlatform.MOBILE+"' )" 
             +" AND costType= '" +CostType.CPM
             +"' AND endDateTime <='"+upperDate+"' AND startDateTime >='"+lowerDate+"'";
   	         log.info("query:"+query); 
	         Statement lineItemStatement=new Statement();
	         lineItemStatement.setQuery(query);
	         LineItemPage page=lineItemService.getLineItemsByStatement(lineItemStatement);
	         
	        
	         // InventoryServiceInterface inventoryService = dfpServices.get(session, InventoryServiceInterface.class);
	         // Statement invntoryStatement = new Statement();
	         
	       if(page.getResults()!=null && page.getResults().size()>0){
	        	  for(LineItem lineItem1 : page.getResults()){
	        		  lineItemList.add(lineItem1);
	        		  //itemExtendList.add((LineItemExtend) lineItem1);
	        	  }
	        	 // testMap.put("test", lineItemList);
	        	  log.info("Found line Items:"+lineItemList.size());
	          }else{
	        	  log.warning("No line items found for query:"+query);
	          }
	          
         
	          if(lineItemList!=null && lineItemList.size()>0){
	        	  
	        	   //  MemcacheUtil.setReallocationDataLastQueryByUser(userId, testMap);
	        	     Iterator<LineItem> iterator = lineItemList.iterator();
	   	              while(iterator.hasNext()){
	   	        	  lineItemObj = iterator.next();
	   	        	/*   List<AdUnitTargeting> targetList=lineItemObj.getTargeting().getInventoryTargeting().getTargetedAdUnits();
	   	        	   System.out.println("targetList:"+targetList.size());
	   	        	   for(AdUnitTargeting adUnit:targetList){
	   	        		  System.out.println("AdUnitId:" + adUnit.getAdUnitId());
	   	        		  query = "WHERE  id = '"+adUnit.getAdUnitId()+"'";
	   	 	              log.info("query:"+query); 
	   	 	              invntoryStatement.setQuery(query);
	   		              AdUnitPage InventoryPage= inventoryService.getAdUnitsByStatement(invntoryStatement);
	   		              System.out.println();
	   		           
	   	        	   }*/
	   	        	  if(lineItemObj.getStats().getClicksDelivered().toString()!=null){
	   	        		  reallocationTableDTOObj.setClicks(lineItemObj.getStats().getClicksDelivered().toString());
	   	        		  totalClicks = totalClicks+Long.valueOf(lineItemObj.getStats().getClicksDelivered().toString());
	   	        	  }
	   	        	  if(lineItemObj.getStats().getImpressionsDelivered().toString()!=null){
	   	        		  reallocationTableDTOObj.setDeliveredImpressions(lineItemObj.getStats().getImpressionsDelivered().toString());
	   	        		  totalDeliveredImpressions = totalDeliveredImpressions+Long.valueOf(lineItemObj.getStats().getImpressionsDelivered().toString());
	   	        	  }
	   	        	  if(lineItemObj.getBudget().toString()!=null){
	   	        		  double budget =  ((double) lineItemObj.getBudget().getMicroAmount()) / 1000000;
	   	        		  reallocationTableDTOObj.setBudget(String.valueOf(budget));
	   	        		  totalBudget = totalBudget+Double.valueOf(String.valueOf(budget));
	   	        	  }
	   	        	  if(lineItemObj.getStats().getImpressionsDelivered().toString()!=null && lineItemObj.getStats().getClicksDelivered().toString()!=null){
	   	        		  double ctr =((double)lineItemObj.getStats().getClicksDelivered() /(double)lineItemObj.getStats().getImpressionsDelivered())*100;
	   	        		if (Double.isNaN(ctr)) {
	   	        			ctr = 0.0;
	   	        		}
	   	        		  reallocationTableDTOObj.setCTR(String.valueOf(ctr));
	   	        		  
	   	        	  }
	   	        	  if(lineItemObj.getCostPerUnit().toString()!=null){
	   	        		  double costPerUnit = ((double) lineItemObj.getCostPerUnit()
	   	  						.getMicroAmount() / 1000000);
	   	        		  reallocationTableDTOObj.setECPM(String.valueOf(costPerUnit));
	   	        		  totalECPM = totalECPM+Double.valueOf(String.valueOf(costPerUnit));
	   	        	  }
	   	        	  if(lineItemObj.getName()!=null && !lineItemObj.getName().trim().equalsIgnoreCase("")){
	   	        		  reallocationTableDTOObj.setLineItem(lineItemObj.getName());
	   	        	  }
	   	        	  if(lineItemObj.getUnitsBought()!=0){
	   	        		  reallocationTableDTOObj.setBookedImpressions(lineItemObj.getUnitsBought().toString());
	   	        		  totalBookedImpressions = totalBookedImpressions + Long.valueOf(lineItemObj.getUnitsBought().toString());
	   	        	  }
	   	        	  if(lineItemObj.getUnitsBought()!=0 && lineItemObj.getStats().getImpressionsDelivered()!=0){
	   	        		  long overUnder = lineItemObj.getUnitsBought()-lineItemObj.getStats().getImpressionsDelivered();
	   	        		  reallocationTableDTOObj.setOverUnder(String.valueOf(overUnder));
	   	        		  totalOverUnder = totalOverUnder + Long.valueOf(String.valueOf(overUnder));
	   	        	  }
	   	        	  if(lineItemObj.getCostPerUnit().toString()!=null && lineItemObj.getStats().getImpressionsDelivered()!=null){
	   	        		  double costPerUnit = ((double) lineItemObj.getCostPerUnit()
	   		  						.getMicroAmount() / 1000000);
	   	        		   revenueDelivered = (costPerUnit * (double)lineItemObj.getStats().getImpressionsDelivered())/1000;
	   	        		  reallocationTableDTOObj.setRevenueDelivered(String.valueOf(revenueDelivered));
	   	        		  totalRevenueDelivered = totalRevenueDelivered + Double.valueOf(String.valueOf(revenueDelivered));
	   	        	  }
	   	        	  if(lineItemObj.getBudget()!=null && revenueDelivered!=null){
	   	        		  double budget =  ((double) lineItemObj.getBudget().getMicroAmount()) / 1000000;
	   	        		  Double revenueRemaining = budget - revenueDelivered;
	   	        		  reallocationTableDTOObj.setRevenueRemaining(String.valueOf(revenueRemaining));
	   	        		  totalRevenueRemaining = totalRevenueRemaining + Double.valueOf(String.valueOf(revenueRemaining));
	   	        	  }
	   	        	  if(reallocationTableDTOObj.getBudget()!=null && reallocationTableDTOObj.getRevenueDelivered()!=null){
	   	        		double budget = Double.valueOf(reallocationTableDTOObj.getBudget());
	   	        		double revenueDel = Double.valueOf(reallocationTableDTOObj.getRevenueDelivered());
	   	        		double revenueToDel = budget - revenueDel;
	   	        		  reallocationTableDTOObj.setRevenueToBeDelivered(String.valueOf(revenueToDel));
	   	        	  }
	   	        	  
	   	        	 if(lineItemObj.getId()!=null ){
	   	        		  reallocationTableDTOObj.setLineItemId(String.valueOf(lineItemObj.getId()));
	   	        	  }
	   	        	 if(totalClicks>=0){
	   	        		reallocationTableDTOObj.setTotalClicks(String.valueOf(totalClicks));
	   	        	 }
	   	        	 if(totalDeliveredImpressions>=0){
		   	        		reallocationTableDTOObj.setTotalDeliveredImpressions(String.valueOf(totalDeliveredImpressions));
		   	         }
	   	        	 if(totalBudget>=0){
		   	        		reallocationTableDTOObj.setTotalBudget(String.valueOf(totalBudget));
		   	         }
	   	        	 if(totalCTR>=0 ){
	   	        		 if(totalDeliveredImpressions!=0 ){
	   	        			totalCTR =( totalClicks/totalDeliveredImpressions)*100;
	   	        			reallocationTableDTOObj.setTotalCTR(String.valueOf(totalCTR));
	   	        		 }else{
	   	        			reallocationTableDTOObj.setTotalCTR("0");
	   	        		 }
		   	         }
	   	        	 if(totalECPM>=0){
		   	        		reallocationTableDTOObj.setTotalECPM(String.valueOf(totalECPM));
		   	         }
	   	        	 if(totalBookedImpressions>=0){
		   	        		reallocationTableDTOObj.setTotalBookedImpressions(String.valueOf(totalBookedImpressions));
		   	         }
	   	        	 if(totalOverUnder>=0){
		   	        		reallocationTableDTOObj.setTotalOverUnder(String.valueOf(totalOverUnder));
		   	         }
	   	        	 if(totalRevenueDelivered>=0){
		   	        		reallocationTableDTOObj.setTotalRevenueDelivered(String.valueOf(totalRevenueDelivered));
		   	         }
	   	        	 if(totalRevenueRemaining>=0){
		   	        		reallocationTableDTOObj.setTotalRevenueRemaining(String.valueOf(totalRevenueRemaining));
		   	         }
	   	        	 
	   	        	 if(totalRevenueDelivered>=0 && totalBudget>=0){
			   	       	    totalRevenueToBeDelivered = totalBudget - totalRevenueDelivered;
			   	        	reallocationTableDTOObj.setTotalRevenueToBeDelivered(String.valueOf(totalRevenueToBeDelivered));
			   	       	 }
	   	        	 
	   	        	  advertiserReallocationTableList.add(reallocationTableDTOObj);
	   	        	reallocationTableDTOObj = new AdvertiserReallocationTableDTO();
	   	          } 
	          }
	          reallocationObjectsMap.put("TableObject",advertiserReallocationTableList);
	          
		 }catch(Exception e){
			 log.severe("exception in loadReallocationHeaderDataAdvertiserView advertiserService : "+e.getMessage());
		 }
	
		 return reallocationObjectsMap;
	}
	
	public List<AdvertiserReallocationTableDTO> updateReallocationLineItem(DfpServices dfpServices,DfpSession session,String array,long userId,String lowerDate, String upperDate, String campainOrder, String lineItem){
		log.info("inside updateReallocationLineItem in advertiserservice");
		List<LineItem> reallocationDataList = new ArrayList<LineItem>();
		List<LineItem> updatedReallocationDataList = new ArrayList<LineItem>();
		LineItem obj;
		//LineItem objTemp;
		String []arr = null;
		String query=null;
		long totalClicks = 0; 
		double totalECPM = 0;
		double totalBudget = 0;
		long totalBookedImpressions = 0;
		long totalDeliveredImpressions = 0;
		long totalOverUnder = 0;
		double totalCTR = 0;
		double totalRevenueDelivered = 0;
		double totalRevenueRemaining = 0;
		long totalRevisedBudget = 0;
		long totalRevisedBookedImpressions = 0;
		double totalRevenueToBeDelivered = 0;
		//String targetPlatform="WEB";
	    //String costType="CPM";
	    campainOrder = "131407327";
	    lowerDate="2012-01-01T00:00:00";
		upperDate="2014-12-31T00:00:00";
		List<AdvertiserReallocationTableDTO> advertiserReallocationTableList = new ArrayList<AdvertiserReallocationTableDTO>();
		Map<String,String> reallocationMap = new HashMap<String, String>();
		AdvertiserReallocationTableDTO reallocationTableDTOObj = new AdvertiserReallocationTableDTO();
		Double revenueDelivered = 0.0;
		try{
			if(array!=null && !array.equals("")){
        		arr = array.split(",");
        	}
			if(arr!=null&& arr.length>0)
    		{
    			for(int i=0;i<arr.length;i++){
    				String [] tempArr = arr[i].split(":");
    				reallocationMap.put(tempArr[0].trim(),tempArr[1].trim() );
    			}
    		}
             LineItemServiceInterface lineItemService=dfpServices.get(session, LineItemServiceInterface.class);
             query="WHERE  orderId = '"+campainOrder+"' AND status = '"
             +ComputedStatus.READY+"' AND (targetPlatform = '"
             +TargetPlatform.WEB + "'or targetPlatform = '"+TargetPlatform.MOBILE+"' )" 
             +" AND costType= '" +CostType.CPM
              +"' AND endDateTime <='"+upperDate+"' AND startDateTime >='"+lowerDate+"'";
   	         log.info("query:"+query); 
	         Statement lineItemStatement=new Statement();
	         lineItemStatement.setQuery(query);
	          LineItemPage page=lineItemService.getLineItemsByStatement(lineItemStatement);
	          
	          if(page.getResults()!=null && page.getResults().size()>0){
	        	  for(LineItem lineItem1 : page.getResults()){
	        		  reallocationDataList.add(lineItem1);
	        		  //itemExtendList.add((LineItemExtend) lineItem1);
	        	  }
			
			//reallocationDataList = MemcacheUtil.getReallocationDataLastQueryByUser(userId);
    		if(reallocationDataList!=null && reallocationDataList.size()>0){
    			for(int i=0;i<reallocationDataList.size();i++){
    				obj=reallocationDataList.get(i);
    				if(reallocationMap!=null && reallocationMap.size()>0 && reallocationMap.containsKey(obj.getId().toString())){
    					String temp =  reallocationMap.get(String.valueOf(obj.getId()));
    					String []tempArr = temp.split(";");
    					Money money = new Money();
    					 money.setMicroAmount(Long.valueOf(tempArr[0])*1000000);
    					 money.setCurrencyCode("USD");
    					 obj.setBudget(money);
    					 obj.setAllowOverbook(true);
    					 obj.setUnitsBought(Long.valueOf(tempArr[1]));
    					 updatedReallocationDataList.add(obj);
    					// objTemp = lineItemService.updateLineItem(obj);
    					 System.out.println("object updated");
    				}
    			}
    		}
    		reallocationDataList = null;
    		reallocationDataList =  lineItemService.updateLineItems(updatedReallocationDataList);
    		if(reallocationDataList!=null && reallocationDataList.size()>0){

	        	  
	        	   //  MemcacheUtil.setReallocationDataLastQueryByUser(userId, testMap);
	        	     Iterator<LineItem> iterator = reallocationDataList.iterator();
	   	              while(iterator.hasNext()){
	   	            	obj = iterator.next();
	   	        	  if(obj.getStats().getClicksDelivered().toString()!=null){
	   	        		  reallocationTableDTOObj.setClicks(obj.getStats().getClicksDelivered().toString());
	   	        		  totalClicks = totalClicks+Long.valueOf(obj.getStats().getClicksDelivered().toString()); 
	   	        	  }
	   	        	  if(obj.getStats().getImpressionsDelivered().toString()!=null){
	   	        		  reallocationTableDTOObj.setDeliveredImpressions(obj.getStats().getImpressionsDelivered().toString());
	   	        		  totalDeliveredImpressions = totalDeliveredImpressions+Long.valueOf(obj.getStats().getImpressionsDelivered().toString());
	   	        	  }
	   	        	  if(obj.getBudget().toString()!=null){
	   	        		  double budget =  ((double) obj.getBudget().getMicroAmount()) / 1000000;
	   	        		  reallocationTableDTOObj.setBudget(String.valueOf(budget));
	   	        		  totalBudget = totalBudget+Double.valueOf(String.valueOf(budget));
	   	        	  }
	   	        	  if(obj.getStats().getImpressionsDelivered().toString()!=null && obj.getStats().getClicksDelivered().toString()!=null){
	   	        		  double ctr =((double)obj.getStats().getClicksDelivered() /(double)obj.getStats().getImpressionsDelivered())*100;
	   	        		if (Double.isNaN(ctr)) {
	   	        			ctr = 0.0;
	   	        		}
	   	        		  reallocationTableDTOObj.setCTR(String.valueOf(ctr));
	   	        		  totalCTR = totalCTR+Double.valueOf(String.valueOf(ctr));
	   	        	  
	   	        	  }
	   	        	  if(obj.getCostPerUnit().toString()!=null){
	   	        		  double costPerUnit = ((double) obj.getCostPerUnit()
	   	  						.getMicroAmount() / 1000000);
	   	        		  reallocationTableDTOObj.setECPM(String.valueOf(costPerUnit));
	   	        		  totalECPM = totalECPM+Double.valueOf(String.valueOf(costPerUnit));
	   	        	  }
	   	        	  if(obj.getName()!=null && !obj.getName().trim().equalsIgnoreCase("")){
	   	        		  reallocationTableDTOObj.setLineItem(obj.getName());
	   	        	  }
	   	        	  if(obj.getUnitsBought()!=0){
	   	        		  reallocationTableDTOObj.setBookedImpressions(obj.getUnitsBought().toString());
	   	        		  totalBookedImpressions = totalBookedImpressions + Long.valueOf(obj.getUnitsBought().toString());
	   	        	  }
	   	        	  if(obj.getUnitsBought()!=0 && obj.getStats().getImpressionsDelivered()!=0){
	   	        		  long overUnder = obj.getUnitsBought()-obj.getStats().getImpressionsDelivered();
	   	        		  reallocationTableDTOObj.setOverUnder(String.valueOf(overUnder));
	   	        		  totalOverUnder = totalOverUnder + Long.valueOf(String.valueOf(overUnder));
	   	        	  }
	   	        	  if(obj.getCostPerUnit().toString()!=null && obj.getStats().getImpressionsDelivered()!=null){
	   	        		  double costPerUnit = ((double) obj.getCostPerUnit()
	   		  						.getMicroAmount() / 1000000);
	   	        		   revenueDelivered = (costPerUnit * (double)obj.getStats().getImpressionsDelivered())/1000;
	   	        		   
	   	        		  reallocationTableDTOObj.setRevenueDelivered(String.valueOf(revenueDelivered));
	   	        		  totalRevenueDelivered = totalRevenueDelivered + Double.valueOf(String.valueOf(revenueDelivered));
	   	        	  }
	   	        	  if(obj.getBudget()!=null && revenueDelivered!=null){
	   	        		  double budget =  ((double) obj.getBudget().getMicroAmount()) / 1000000;
	   	        		  Double revenueRemaining = budget - revenueDelivered;
	   	        		  reallocationTableDTOObj.setRevenueRemaining(String.valueOf(revenueRemaining));
	   	        		  totalRevenueRemaining = totalRevenueRemaining + Double.valueOf(String.valueOf(revenueRemaining));
	   	        	  }
	   	        	  
	   	        	 if(reallocationTableDTOObj.getBudget()!=null && reallocationTableDTOObj.getRevenueDelivered()!=null){
		   	        		double budget = Double.valueOf(reallocationTableDTOObj.getBudget());
		   	        		double revenueDel = Double.valueOf(reallocationTableDTOObj.getRevenueDelivered());
		   	        		double revenueToDel = budget - revenueDel;
		   	        		  reallocationTableDTOObj.setRevenueToBeDelivered(String.valueOf(revenueToDel));
		   	        	  }
	   	        	  
	   	        	 if(obj.getId()!=null ){
	   	        		  reallocationTableDTOObj.setLineItemId(String.valueOf(obj.getId()));
	   	        	  }
	   	        	 
	   	        	 if(totalClicks>=0){
		   	        		reallocationTableDTOObj.setTotalClicks(String.valueOf(totalClicks));
		   	        	 }
		   	         if(totalDeliveredImpressions>=0){
			   	       		reallocationTableDTOObj.setTotalDeliveredImpressions(String.valueOf(totalDeliveredImpressions));
       		   	        }
		   	       	 if(totalBudget>=0){
			        		reallocationTableDTOObj.setTotalBudget(String.valueOf(totalBudget));
		    	         }
		   	        if(totalCTR>=0 ){
	   	        		 if(totalDeliveredImpressions!=0 ){
	   	        			totalCTR =( totalClicks/totalDeliveredImpressions)*100;
	   	        			reallocationTableDTOObj.setTotalCTR(String.valueOf(totalCTR));
	   	        		 }else{
	   	        			reallocationTableDTOObj.setTotalCTR("0");
	   	        		 }
		   	         }
	  	        	 if(totalECPM>=0){
		   	        		reallocationTableDTOObj.setTotalECPM(String.valueOf(totalECPM));
			   	         }
	  	        	 if(totalBookedImpressions>=0){
			   	       		reallocationTableDTOObj.setTotalBookedImpressions(String.valueOf(totalBookedImpressions));
			   	         }
		   	       	 if(totalOverUnder>=0){
			   	       		reallocationTableDTOObj.setTotalOverUnder(String.valueOf(totalOverUnder));
			   	        }
		   	       	 if(totalRevenueDelivered>=0){
			           		reallocationTableDTOObj.setTotalRevenueDelivered(String.valueOf(totalRevenueDelivered));
			  	         }
		   	       	 if(totalRevenueRemaining>=0){
			  	        		reallocationTableDTOObj.setTotalRevenueRemaining(String.valueOf(totalRevenueRemaining));
		   	         }
		   	       	 
		   	       	 if(totalRevenueDelivered>=0 && totalBudget>=0){
		   	       	    totalRevenueToBeDelivered = totalBudget - totalRevenueDelivered;
		   	       	    reallocationTableDTOObj.setTotalRevenueToBeDelivered(String.valueOf(totalRevenueToBeDelivered));
		   	       	 }
	            	  advertiserReallocationTableList.add(reallocationTableDTOObj);
	   	        	reallocationTableDTOObj = new AdvertiserReallocationTableDTO();
	   	          } 
	          
    		}
 	           
	         }
    		  
		}catch(Exception e){
			log.severe("Exception in updateReallocationLineItem in advertiserService"+e.getMessage());
			e.printStackTrace();
		}
		return advertiserReallocationTableList;
		
	}
	public static String getCustomDate(DateTime dfpDateTime) {
		String customDateStr = "";
		if (dfpDateTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.YEAR, dfpDateTime.getDate().getYear());
			calendar.set(Calendar.MONTH, dfpDateTime.getDate().getMonth() - 1);
			calendar.set(Calendar.DAY_OF_MONTH, dfpDateTime.getDate().getDay());
			java.util.Date customDate = calendar.getTime();
			customDateStr = DateUtil.getFormatedDate(customDate,
					"yyyy-MM-dd");
		}else{
			customDateStr="NULL";
		}
		return customDateStr;
	}
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherName,long userId,String str)
	{
		List<PropertyDropDownDTO> propertyList = new ArrayList<PropertyDropDownDTO>();
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		
		/*IAdvertiserDAO advertiserDAO = new AdvertiserDAO();                                               24.08.2013*/
		IUserDetailsDAO dao = new UserDetailsDAO();
		propertyList = dao.loadPropertyDropDownList(publisherName,userId,str);
		if(propertyList!=null && propertyList.size()>0){
			return propertyList;
		}
		return new ArrayList<PropertyDropDownDTO>();
	}
	
	public List<String> getDFPPropertyName(String propertyNames)
	{
		List<String> propertyNameList = new ArrayList<String>();
		IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
		/*if(propertyName!=null && !propertyName.equals("")){                                24.08.2013
			String []propertyArr = propertyName.split(",");
			propertyNameList = advertiserDAO.getDFPPropertyName(propertyArr);
		}
		if(propertyNameList!=null && propertyNameList.size()>0){
			return propertyNameList;
		}
		return new ArrayList<String>();*/

		if(propertyNames!=null && !propertyNames.equals("")){
			String []propertyArr = propertyNames.split(",");
			for(String propertyName : propertyArr) {
				 PropertyObj propertyObj = advertiserDAO.getPropertyObjByName(propertyName);
				 if(propertyObj != null && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null) {
					 propertyNameList.add(propertyObj.getDFPPropertyName());
				 }
			}
		}
		return propertyNameList;
	}
	
	public List<AdvertiserPerformerDTO> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{

		List<AdvertiserPerformerDTO> list = new ArrayList<AdvertiserPerformerDTO>();
		//AdvertiserDAO advDAO=new AdvertiserDAO();
		
		String replaceStr = "\\\\'";
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		
		if(lowerDate == null && upperDate== null)
		{
		String monthToDateEnd=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		String[] dayArray=monthToDateEnd.split("-");
		int year=Integer.parseInt(dayArray[0]);
		int month=Integer.parseInt(dayArray[1])-1; // subtract 1 from day String as day starts from 0 to 11 in Calender class
		String monthToDateStart=DateUtil.getDateByYearMonthDays(year,month,1,"yyyy-MM-dd");
		
		lowerDate = monthToDateStart;
		upperDate = monthToDateEnd;
		}
		
		//list = advDAO.loadAdvertiserTotalDataList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
		
		
		list = MemcacheUtil.getAdvertiserTotalDataList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
		if(list == null || list.size() <= 0)
		{
			try
			{
			AdvertiserDAO advDAO=new AdvertiserDAO();
			list = advDAO.loadAdvertiserTotalDataList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
			MemcacheUtil.setAdvertiserTotalDataList(list,lowerDate, upperDate, publisherName,advertiser,agency,properties);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Advertiser Total Data found from memcache:");
	    }
		
		return list;
	}
	
	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{
		List<AdvertiserPerformerDTO> list = new ArrayList<AdvertiserPerformerDTO>();
		String replaceStr = "\\\\'";
		if(advertiser != null && !advertiser.trim().equalsIgnoreCase("")) {
			advertiser = advertiser.replaceAll("'", replaceStr);
		}else {
			advertiser = "";
		}
		if(agency != null && !agency.trim().equalsIgnoreCase("")) {
			agency = agency.replaceAll("'", replaceStr);		
		}else {
			agency = "";
		}
		
		//list = advDAO.loadDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
		
		list = MemcacheUtil.getDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
		if(list == null || list.size() <= 0)
		{
			try
			{
			AdvertiserDAO advDAO=new AdvertiserDAO();
			list = advDAO.loadDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
			MemcacheUtil.setDeliveryIndicatorData(list,lowerDate, upperDate, publisherName,advertiser,agency,properties);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Delivery Indicator Data found from memcache:");
	    }
		
		
		return list;
	}
	
	
	
}
