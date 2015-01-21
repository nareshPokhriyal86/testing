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
import com.google.api.ads.dfp.jaxws.v201403.Forecast;
import com.google.api.ads.dfp.jaxws.v201403.ForecastServiceInterface;
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
import com.lin.persistance.dao.IRichMediaAdvertiserDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.RichMediaAdvertiserDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.PropertyObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationHeaderDTO;
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
import com.lin.web.service.IRichMediaAdvertiserService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;



public class RichMediaAdvertiserService implements IRichMediaAdvertiserService{
	private static final Logger log = Logger.getLogger(RichMediaAdvertiserService.class.getName());
	
	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String order,String lineOrder,String publisherName){
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
			if(lineOrder.startsWith(",") && lineOrder.length() >= 2) {
				lineOrder = lineOrder.substring(1);
			}
			lineOrder = lineOrder.replaceAll("'", replaceStr);
			lineOrder = lineOrder.replaceAll(",", "','");
		}else {
			lineOrder = "";
		}
	
		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
/*		list = advertiserDAO.loadTrendAnalysisHeaderDataAdvertiserView(lowerDate, upperDate, order, lineOrder, publisherName,properties);
		if(list!=null && list.size()!=0){
			log.info("list size from advertiser DAO : "+list.size());
		}*/
		
		list = MemcacheUtil.getTrendAnalysisHeaderDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineOrder);
		if(list == null || list.size() <= 0)
		{
			try
			{
			//IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
				log.info("in try block of loadTrendAnalysisHeaderDataAdvertiserView");
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
	
	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String order,String lineItem,String publisherName){
		log.info("inside loadTrendAnalysisActualDataAdvertiserView of RichMediaAdvertiserService");
		
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
//		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
//		list = advertiserDAO.loadTrendAnalysisActualDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName,properties);
		
		list = MemcacheUtil.getTrendAnalysisActualDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineItem);
		if(list == null || list.size() <= 0)
		{
			try
			{
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
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

	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String order,String lineItem,String publisherName){
		log.info("inside loadTrendAnalysisForcastDataAdvertiserView of RichMediaAdvertiserService");
		
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
//		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
//		list = advertiserDAO.loadTrendAnalysisForcastDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName,properties);
		
		list = MemcacheUtil.getTrendAnalysisForcastDataAdvertiserView(lowerDate,upperDate,publisherName,order,lineItem);
		if(list == null || list.size() <= 0)
		{
			try
			{
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
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
	public void loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency, StringBuilder sbStr ,String publisherName,String properties) throws Exception {
		log.info("inside loadAdvertisersByLocationData() of RichMediaAdvertiserService");
		String replaceStr = "\\\\'";
		List<AdvertiserByLocationObj>  advertiserByLocationObjList =null;
//		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
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
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
			advertiserByLocationObjList = advertiserDAO.loadAdvertisersByLocationData(lowerDate, upperDate, advertiser, agency, publisherName, properties);
			MemcacheUtil.setAdvertisersByLocationData(advertiserByLocationObjList,lowerDate,upperDate,advertiser, agency, publisherName);
			
			}catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Advertiser By Location Data found from memcache:");
	    }
		
		/*if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State',  'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState().replaceAll("'", "")+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");	
		}*/
		
		if(advertiserByLocationObjList != null && advertiserByLocationObjList.size() > 0) {
			sbStr.append("[['State',  'CTR(%)', 'Impression']");
		
			/*double totalImpression = 0;
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				
				totalImpression = totalImpression + object.getImpression();
				
			}*/
			
		for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState().replaceAll("'", "")+"',"+object.getCtrPercent()+","+object.getImpression());
				sbStr.append("]");
			}
			sbStr.append("]");
		}
	}

	@Override
	public void loadAdvertiserByMarketGraphData(String lowerDate, String upperDate,String advertiser, String agency, StringBuilder sbStr,String publisherName,String properties) throws Exception {
		log.info("inside loadAdvertiserByMarketGraphData() of RichMediaAdvertiserService");
		String replaceStr = "\\\\'";
		List<AdvertiserByLocationObj>  advertiserByLocationObjList =null;
//		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
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
		
		advertiserByLocationObjList = MemcacheUtil.getRichMediaAdvertiserByMarketGraphData(lowerDate,upperDate,advertiser,agency,properties);
		if(advertiserByLocationObjList == null || advertiserByLocationObjList.size() <= 0)
		{
			try
			{
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
			advertiserByLocationObjList = advertiserDAO.loadAdvertiserByMarketGraphData(lowerDate, upperDate,advertiser,agency, publisherName, properties);
			MemcacheUtil.setRichMediaAdvertiserByMarketGraphData(advertiserByLocationObjList,lowerDate,upperDate,advertiser,agency,properties);
			
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
		
	}
	
	@Override
	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,String lineItemName,String publisherName,String properties) throws Exception {
		log.info("inside getLineItemForPopUP of RichMediaAdvertiserService");
		String replaceStr = "\\\\'";
		
		if(lineItemName != null && !lineItemName.trim().equalsIgnoreCase("")) {
			lineItemName = lineItemName.replaceAll("'", replaceStr);
			lineItemName = lineItemName.replaceAll("&amp;", "&");
		}
		
		
		PopUpDTO popUpObj = null;
		
		popUpObj = MemcacheUtil.getAdvertiserLineItemPopUpData(lowerDate, upperDate, lineItemName, publisherName);
		if(popUpObj == null) {
			popUpObj = new PopUpDTO();
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
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
	
	
	public List<AgencyDTO> getAgencyDropDownList(String publisherId, String str, long userId)
	{
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		IRichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
		List<AgencyDTO> agencyDTOList = new ArrayList<AgencyDTO>(); 
		String agencyIdString = "";
		String replaceStr = "\\\\'";
		try {
			/*if(str!=null){
				str = str.replaceAll("'", replaceStr);
			}
			List<String> agencyList = userDetailsDAO.getSelectedAgenciesByUserId(userId);
			if(agencyList != null && agencyList.size() > 0 && agencyList.contains(LinMobileConstants.ALL_AGENCIES)) {
				agencyIdString = LinMobileConstants.ALL_AGENCIES;
			}
			else if(agencyList != null && agencyList.size() > 0) {
				agencyIdString = UserService.createCommaSeperatedStringForBigQuery(agencyList);
			}
			if(agencyIdString != null && !agencyIdString.trim().equals("")) {
				List<AgencyDTO> list = advDAO.getAgencyDropDownList(publisherId, str, agencyIdString);
				if(list != null) {
					agencyDTOList = list;
				}
			}*/
		}
		catch (Exception e) {
			log.severe("Exception in getAgencyDropDownList of RichMediaAdvertiserService : "+e.getMessage());
			e.printStackTrace();
		}
		return agencyDTOList;
	}
	
	public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherId ,String agencyName, String str, long userId)
	{
		log.info("inside getAdvertiserDropDownList of RichMediaAdvertiserService...");
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		IRichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
		List<AdvertiserDTO> advertiserDTOList = new ArrayList<AdvertiserDTO>(); 
		String advertiserIdString = "";
		String replaceStr = "\\\\'";
		
		try {
			/*if(str!=null){
				str = str.replaceAll("'", replaceStr);
			}
			if(agencyName!=null){
				agencyName = agencyName.replaceAll("'", replaceStr);
			}
			List<String> advertiserList = userDetailsDAO.getSelectedAdvertisersByUserId(userId);
			if(advertiserList != null && advertiserList.size() > 0 && advertiserList.contains(LinMobileConstants.ALL_ADVERTISERS)) {
				advertiserIdString = LinMobileConstants.ALL_ADVERTISERS;
			}
			else if(advertiserList != null && advertiserList.size() > 0) {
				advertiserIdString = UserService.createCommaSeperatedStringForBigQuery(advertiserList);
			}
			if(advertiserIdString != null && !advertiserIdString.trim().equals("")) {
				List<AdvertiserDTO> list = advDAO.getAdvertiserDropDownList(publisherId, agencyName, str, advertiserIdString);
				if(list != null) {
					advertiserDTOList = list;
				}
			}*/
		}
		catch (Exception e) {
			log.severe("Exception in getAdvertiserDropDownList of RichMediaAdvertiserService : "+e.getMessage());
			e.printStackTrace();
		}
		return advertiserDTOList;
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
		IRichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
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
		
		IRichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
		return advDAO.getLineItemDropDownList(publisherName, agencyName, advertiserName, orderName, str);
	}
	
	/*public List<AdvertiserReallocationHeaderDTO> loadReallocationHeaderDataAdvertiserView(String lowerDate, String upperDate, String canpainOrder, String lineItem){
		String replaceStr = "\\\\'";
		List<AdvertiserReallocationHeaderDTO> reallocationHeaderList = new ArrayList<AdvertiserReallocationHeaderDTO>();
		if(lineItem != null) {
			log.info("lineItem : "+lineItem);
			lineItem = lineItem.replaceAll("'", replaceStr);
			log.info("lineItem : "+lineItem);
			lineItem = lineItem.replaceAll(",", "','");		
		}
		
		IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
		reallocationHeaderList = advertiserDAO.loadReallocationHeaderDataAdvertiserView(lowerDate,upperDate,canpainOrder,lineItem);
		return reallocationHeaderList;
		
	}*/
	
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
	   
	      lowerDate="2012-01-01T00:00:00";
		  upperDate="2014-12-31T00:00:00";
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
			 log.severe("exception in loadReallocationHeaderDataAdvertiserView RichMediaAdvertiserService : "+e.getMessage());
		 }
	
		 return reallocationObjectsMap;
	}
	
	public List<AdvertiserReallocationTableDTO> updateReallocationLineItem(DfpServices dfpServices,DfpSession session,String array,long userId,String lowerDate, String upperDate, String campainOrder, String lineItem){
		log.info("inside updateReallocationLineItem in RichMediaAdvertiserService");
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
			log.severe("Exception in updateReallocationLineItem in RichMediaAdvertiserService"+e.getMessage());
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
			//calendar.set(Calendar.HOUR, dfpDateTime.getHour());
			//calendar.set(Calendar.MINUTE, dfpDateTime.getMinute());
			//calendar.set(Calendar.SECOND, dfpDateTime.getSecond());
			//calendar.setTimeZone(TimeZone.getTimeZone(dfpDateTime
			//		.getTimeZoneID()));
			java.util.Date customDate = calendar.getTime();
			customDateStr = DateUtil.getFormatedDate(customDate,
					"yyyy-MM-dd");
		}else{
			customDateStr="NULL";
		}
		return customDateStr;
	}
	
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherId,long userId,String str)
	{
		List<PropertyDropDownDTO> propertyList = new ArrayList<PropertyDropDownDTO>();
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		
		/*IRichMediaAdvertiserDAO advertiserDAO = new RichMediaAdvertiserDAO();                                               24.08.2013*/
		IUserDetailsDAO dao = new UserDetailsDAO();
		propertyList = dao.loadPropertyDropDownList(publisherId,userId,str);
		if(propertyList!=null && propertyList.size()>0){
			return propertyList;
		}
		return new ArrayList<PropertyDropDownDTO>();
	}
	
	public List<String> getDFPPropertyName(String propertyNames)
	{
		List<String> propertyNameList = new ArrayList<String>();
		IRichMediaAdvertiserDAO advertiserDAO = new RichMediaAdvertiserDAO();
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
		
	public Map<String,List<AdvertiserPerformerDTO>> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{
		Map<String,List<AdvertiserPerformerDTO>> AdvertiserTotalDataMap = new HashMap<String,List<AdvertiserPerformerDTO>>();
		Map<String,AdvertiserPerformerDTO> calculatedLineItemMap = new HashMap<String,AdvertiserPerformerDTO>();
		//Map<String,AdvertiserPerformerDTO> nonDFPObjectMap = new HashMap<String,AdvertiserPerformerDTO>();
		List<AdvertiserPerformerDTO> list = new ArrayList<AdvertiserPerformerDTO>();
		List<AdvertiserPerformerDTO> lineItemCalculatedList = new ArrayList<AdvertiserPerformerDTO>();
		
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
		
		
		list = MemcacheUtil.getRichMediaAdvertiserTotalDataList(lowerDate, upperDate);
		if(list == null || list.size() <= 0)
		{
			try
			{
			RichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
			list = advDAO.loadAdvertiserTotalDataList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
			MemcacheUtil.setRichMediaAdvertiserTotalDataList(list,lowerDate, upperDate);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Advertiser Total Data found from memcache:");
	    }
		
		try{
			if(list!=null && list.size()>0){
				AdvertiserTotalDataMap.put("campainTotal",list);
				for (AdvertiserPerformerDTO advertiserPerformerDTO : list) {
					if(!calculatedLineItemMap.containsKey(advertiserPerformerDTO.getCampaignLineItem())){
						advertiserPerformerDTO.setLineItemCountFlag(1);
						calculatedLineItemMap.put(advertiserPerformerDTO.getCampaignLineItem(), advertiserPerformerDTO);
						
				}else{
					long impressions = 0;
					long clicks = 0;
					long bookedImpressions = 0;
					double ctr = 0.0;
					int lineItemCountFlag = 0;
					double deliveryIndicator = 0.0;
					AdvertiserPerformerDTO advertiserObj = copyObject(calculatedLineItemMap.get(advertiserPerformerDTO.getCampaignLineItem()));
					lineItemCountFlag = advertiserObj.getLineItemCountFlag() + 1;
					if(lineItemCountFlag!=0){
						deliveryIndicator = (advertiserPerformerDTO.getDeliveryIndicator()+advertiserObj.getDeliveryIndicator())/lineItemCountFlag;
					}
					advertiserObj.setLineItemCountFlag(lineItemCountFlag);
					impressions = advertiserPerformerDTO.getImpressionDelivered() + advertiserObj.getImpressionDelivered();
					clicks = advertiserPerformerDTO.getClicks() + advertiserObj.getClicks();
					bookedImpressions = advertiserPerformerDTO.getBookedImpressions() + advertiserObj.getBookedImpressions();
					if(impressions!=0){
						ctr = Double.valueOf(clicks*100)/impressions;
					}
					 
					advertiserObj.setImpressionDelivered(impressions);
					advertiserObj.setClicks(clicks);
					advertiserObj.setCTR(ctr);
					advertiserObj.setBookedImpressions(bookedImpressions);
					advertiserObj.setDeliveryIndicator(deliveryIndicator);
					calculatedLineItemMap.put(advertiserObj.getCampaignLineItem(), advertiserObj);
					
				}//end of else
			  }//end of for loop
			}
			
			Iterator iterator = calculatedLineItemMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				lineItemCalculatedList.add((AdvertiserPerformerDTO) mapEntry.getValue());
		}
			AdvertiserTotalDataMap.put("lineItemCalculated",lineItemCalculatedList);	
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return AdvertiserTotalDataMap;
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
		
		list = MemcacheUtil.getRichMediaDeliveryIndicatorData(lowerDate, upperDate, advertiser, agency, properties);
		if(list == null || list.size() <= 0)
		{
			try
			{
			RichMediaAdvertiserDAO advDAO=new RichMediaAdvertiserDAO();
			list = advDAO.loadDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
			MemcacheUtil.setRichMediaDeliveryIndicatorData(list,lowerDate, upperDate, advertiser,agency,properties);
			
			}catch (Exception e) {
				log.severe("DataServiceException :"+e.getMessage());
				e.printStackTrace();
			}			
		}else{
			log.info("Delivery Indicator Data found from memcache:");
	    }
		
		
		return list;
	}
	
	  public ForcastLineItemDTO getLineItemForcast(DfpServices dfpServices,DfpSession session,String lineItemId){
	    	log.info("inside getLineItemForcast of advertiser service");
	    	ForcastLineItemDTO forcastLineItemDTO = new ForcastLineItemDTO();
	    	Forecast forecast = null;
	    	try{
	    	ForecastServiceInterface forecastService = dfpServices.get(session, ForecastServiceInterface.class);
	        forecast =  forecastService.getForecastById(Long.valueOf(lineItemId));
	        if(forecast!=null){
	        	if(forecast.getId()!=null){
	        		forcastLineItemDTO.setLineItemId(forecast.getId());
	        	}
	        	if(forecast.getAvailableUnits()!=null){
	        		forcastLineItemDTO.setAvailableUnit(forecast.getAvailableUnits());
	        	}
	        	if(forecast.getDeliveredUnits()!=null){
	        		forcastLineItemDTO.setDeliveredUnit((forecast.getDeliveredUnits()));
	        	}
	        	if(forecast.getMatchedUnits()!=null){
	        		forcastLineItemDTO.setMatchedUnit(forecast.getMatchedUnits());
	        	}
	        }
	     
	        
	    	}catch(Exception e){
	    		log.severe("Exception in getLineItemForcast of advertiser service"+e.getMessage());
	    		e.printStackTrace();
	    	}
			return forcastLineItemDTO;
	        
      }
	  
	  @Override
	  public List<AdvertiserPerformerDTO> loadRichMediaEventPopup(String lowerDate, String upperDate, String lineItemName) throws Exception {
			String replaceStr = "\\\\'";
			List<AdvertiserPerformerDTO> richMediaEventPopupList = null;
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
			
			if(lineItemName != null && !lineItemName.trim().equalsIgnoreCase("")) {
				lineItemName = lineItemName.replaceAll("'", replaceStr);
			}else {
				lineItemName = "";
			}
			
			richMediaEventPopupList = MemcacheUtil.getRichMediaEventPopupList(lowerDate,upperDate,lineItemName);
			if(richMediaEventPopupList == null || richMediaEventPopupList.size() <= 0) {
				richMediaEventPopupList = advertiserDAO.loadRichMediaEventPopup(lowerDate, upperDate, lineItemName);
				if(richMediaEventPopupList != null && richMediaEventPopupList.size() > 0) {
					MemcacheUtil.setRichMediaEventPopupList(richMediaEventPopupList,lowerDate,upperDate,lineItemName);
				}			
			}else{
				log.info("Rich Media Event Popup data found in memcache:");
			}
			
			return richMediaEventPopupList;
		}
	  
	  @Override
	  public String loadRichMediaEventGraph(List<CommonDTO> customEvents, String lowerDate, String upperDate,String advertiser, String agency) throws Exception {
		  List<String> tempCustomEventsList = new ArrayList<String>();
		  List<AdvertiserPerformerDTO> richMediaEventGraphList = null; 
		  List<CommonDTO> customEventsList = new ArrayList<CommonDTO>();
		  String customEventString = "";
		  String customizedEventValue = "";
		  List<AdvertiserPerformerDTO> resultList = null;
		  String richMediaGraphTable = "";
		  lowerDate = lowerDate+" 00:00:00";
		  upperDate = upperDate+" 00:00:00";
			
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
			
			resultList = MemcacheUtil.getRichMediaEventGraphList(lowerDate, upperDate, advertiser, agency);
			if(resultList != null && resultList.size()> 0) {
				richMediaEventGraphList =resultList;
			}
			else {
				resultList =  advertiserDAO.loadRichMediaEventGraph(lowerDate, upperDate,advertiser,agency);
				if(resultList != null && resultList.size()> 0) {
					MemcacheUtil.setRichMediaEventGraphList(resultList, lowerDate, upperDate, advertiser, agency);
					richMediaEventGraphList =resultList;
				}
			}

		  if(richMediaEventGraphList!=null && richMediaEventGraphList.size()>0){			  
			  for (AdvertiserPerformerDTO advertiserPerformerDTO : richMediaEventGraphList) {
				  if(advertiserPerformerDTO != null && advertiserPerformerDTO.getCustomEvent()!=null && !advertiserPerformerDTO.getCustomEvent().trim().equals("") && !advertiserPerformerDTO.getCustomEvent().trim().equalsIgnoreCase("null") && !tempCustomEventsList.contains(advertiserPerformerDTO.getCustomEvent().trim())) {
					  customizedEventValue = advertiserPerformerDTO.getCustomEvent().trim();
					  tempCustomEventsList.add(customizedEventValue);
					  if(customizedEventValue.startsWith("http")) {
		    			  customizedEventValue = "URL Clicked";
		    		  }
		    		  else if(customizedEventValue.trim().length() > 22) {
		    			  customizedEventValue = customizedEventValue.trim().substring(0, 20) +"..";
		    		  }
					  customEventString = customEventString + ",'"+customizedEventValue+"'";
					  customEventsList.add(new CommonDTO("richMediaEventGraph_"+customizedEventValue.replaceAll(" ", ""), customizedEventValue));
				  }
			  }
			  customEvents.addAll(customEventsList);
			  
			  richMediaGraphTable = richMediaGraphTable +  "['Market','Site','Campaign Category','Label','Count'"+customEventString+"],";
			  System.out.println(richMediaGraphTable);
			 for (AdvertiserPerformerDTO dto : richMediaEventGraphList) {
				 if(dto!=null){
					 customizedEventValue = dto.getCustomEvent().trim();
					  tempCustomEventsList.add(customizedEventValue);
					  if(customizedEventValue.startsWith("http")) {
		    			  customizedEventValue = "URL Clicked";
		    		  }
		    		  else if(customizedEventValue.trim().length() > 22) {
		    			  customizedEventValue = customizedEventValue.trim().substring(0, 20) +"..";
		    		  }
					  richMediaGraphTable = richMediaGraphTable + "[";
					  if(dto.getMarket()!=null && (dto.getMarket().trim().equals("DC") || dto.getMarket().trim().equals("SF"))){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getMarket().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getSiteName()!=null && !dto.getSiteName().trim().equals("") && !dto.getSiteName().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getSiteName().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getCampaignCategory()!=null && !dto.getCampaignCategory().trim().equals("") && !dto.getCampaignCategory().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getCampaignCategory().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(customizedEventValue!=null && !customizedEventValue.equals("") && !customizedEventValue.equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+customizedEventValue+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getValue()!=null && !dto.getValue().trim().equals("") && !dto.getValue().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + ""+dto.getValue().trim()+" ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "0  ";
					  }
					 
				     richMediaGraphTable = richMediaGraphTable + customEventString;
					 
					  richMediaGraphTable = richMediaGraphTable + "],";
				  }
			}
			 System.out.println(richMediaGraphTable);
			 log.info("Rich Media Graph Table :"+richMediaGraphTable);
		  }
		 
		  return richMediaGraphTable;
		  
	  }
	  
	  
	  /*@Override
	  public String loadRichMediaEventGraph(StringBuilder customEventString, String lowerDate, String upperDate,String advertiser, String agency) throws Exception {
		  List<AdvertiserPerformerDTO> richMediaEventGraphList = null; 
		  List<AdvertiserPerformerDTO> resultList = null;
		  String richMediaGraphTable = "";
		  lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			
			AdvertiserPerformerDTO dto = new AdvertiserPerformerDTO();
			IRichMediaAdvertiserDAO advertiserDAO=new RichMediaAdvertiserDAO();
			
			resultList = MemcacheUtil.getRichMediaEventGraphList(lowerDate, upperDate, advertiser, agency);
			if(resultList != null && resultList.size()> 0) {
				richMediaEventGraphList =resultList;
			}
			else {
				resultList =  advertiserDAO.loadRichMediaEventGraph(lowerDate, upperDate,advertiser,agency);
				if(resultList != null && resultList.size()> 0) {
					MemcacheUtil.setRichMediaEventGraphList(resultList, lowerDate, upperDate, advertiser, agency);
					richMediaEventGraphList =resultList;
				}
			}

		  if(richMediaEventGraphList!=null && richMediaEventGraphList.size()>0){
			  richMediaGraphTable = richMediaGraphTable +  "['Market','Site','Campaign Category','Label','Count','Coupons Downloads','Find Store','Click to Calls','URL'],";
			 for (AdvertiserPerformerDTO advertiserPerformerDTO : richMediaEventGraphList) {
				dto = advertiserPerformerDTO;
				 if(dto!=null){
					  richMediaGraphTable = richMediaGraphTable + "[";
					  if(dto.getMarket()!=null && (dto.getMarket().equals("DC") || dto.getMarket().equals("SF"))){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getMarket()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getSiteName()!=null && !dto.getSiteName().equals("")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getSiteName()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getCampaignCategory()!=null && !dto.getCampaignCategory().equals("")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getCampaignCategory()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getCustomEvent()!=null && !dto.getCustomEvent().equals("")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getCustomEvent()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getValue()!=null && !dto.getValue().equals("")){
						  richMediaGraphTable = richMediaGraphTable + ""+dto.getValue()+" , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "0 , ";
					  }
					 
				     richMediaGraphTable = richMediaGraphTable + "'"+LinMobileConstants.RICH_MEDIA_EVENT_GRAPH_COUPONS_DOWNLOADS+"' , ";
					 richMediaGraphTable = richMediaGraphTable + "'"+LinMobileConstants.RICH_MEDIA_EVENT_GRAPH_FIND_STORE+"' , ";
					 richMediaGraphTable = richMediaGraphTable + "'"+LinMobileConstants.RICH_MEDIA_EVENT_GRAPH_CLICK_TO_CALLS+"' , ";
					 richMediaGraphTable = richMediaGraphTable + "'"+LinMobileConstants.RICH_MEDIA_EVENT_GRAPH_URL+"'";
					 
					  richMediaGraphTable = richMediaGraphTable + "],";
				  }
			}
			 System.out.println("table :"+richMediaGraphTable);
		  }
		 
		  return richMediaGraphTable;
		  
	  }*/
	  
	  public AdvertiserPerformerDTO copyObject(AdvertiserPerformerDTO sourceObj){
			AdvertiserPerformerDTO destinationObj=new AdvertiserPerformerDTO(sourceObj.getCampaignIO(),
					sourceObj.getLineItemId(), sourceObj.getCampaignLineItem(), sourceObj.getImpressionDelivered(),
					sourceObj.getClicks(),sourceObj.getCTR(),sourceObj.getRevenueDeliverd(),sourceObj.getAgency(),
					sourceObj.getAdvertiser(), sourceObj.getCreativeType(), sourceObj.getMarket(),
					sourceObj.getDeliveryIndicator(),sourceObj.getSite(),sourceObj.getBookedImpressions(),sourceObj.getBudget(),sourceObj.getPublisherName(),sourceObj.getSiteName(),"");
			
			destinationObj.setCampaignLineItem(sourceObj.getCampaignLineItem());
			destinationObj.setImpressionDelivered(sourceObj.getImpressionDelivered());
			destinationObj.setClicks(sourceObj.getClicks());
			destinationObj.setCTR(sourceObj.getCTR());
			destinationObj.setBookedImpressions(sourceObj.getBookedImpressions());
			destinationObj.setLineItemCountFlag(sourceObj.getLineItemCountFlag());
			
			return destinationObj;
		}
}
