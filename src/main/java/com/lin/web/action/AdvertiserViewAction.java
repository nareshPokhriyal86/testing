package com.lin.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.persistance.dao.IAdvertiserDAO;
import com.lin.persistance.dao.impl.AdvertiserDAO;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AgencyAdvertiserObj;
import com.lin.server.bean.ForcastedAdvertiserObj;
import com.lin.server.bean.OrderLineItemObj;
import com.lin.server.bean.ReallocationDataObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationHeaderDTO;
import com.lin.web.dto.AdvertiserReallocationTableDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IAdvertiserService;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;


public class AdvertiserViewAction implements ModelDriven<UserDetailsDTO>, ServletRequestAware, SessionAware{
	
	private static final Logger log = Logger.getLogger(AdvertiserViewAction.class.getName());
	private HttpServletRequest request;
	private Map session;
	private SessionObjectDTO sessionDTO;
	
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private Map<String,String> headerMap;
	private String status;
	public double threshold= LinMobileConstants.THRESHOLD;
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	private String errorStatus;
	
	private int total;
	//private List<AdvertiserReportObj> topPerformerLineItemList;
	private List<AdvertiserPerformerDTO> topPerformerLineItemList;
	//private List<AdvertiserReportObj> topNonPerformerLineItemList;
	private List<AdvertiserPerformerDTO> topNonPerformerLineItemList;
	//private List<CustomLineItemObj> mostActiveLineItemList;
	private List<AdvertiserPerformerDTO> mostActiveLineItemList;
	//private List<CustomLineItemObj> topGainersLineItemList;
	private List<AdvertiserPerformerDTO> topGainersLineItemList;
	//private List<CustomLineItemObj> topLosersLineItemList;
	private List<AdvertiserPerformerDTO> topLosersLineItemList;
	//private List<PerformanceMetricsObj> performanceMetricsList;
	private List<AdvertiserPerformerDTO> performanceMetricsList;
	private Map<String,String> advertiserByLocationDataMap;
	private List<String> agencyList;
	private List<AgencyAdvertiserObj> advertiserList;
	private List<String> ordersList;
	private List<OrderLineItemObj> lineItemNameList;
	private List<String> ordersTrandsList;
	private PopUpDTO popUpDTOObj;
	private List<OrderLineItemObj> lineItemNameTrandsList;
	private List<ReallocationDataObj> reallocationItemList;
	private List<ActualAdvertiserObj> actualAdvertiserDataList;
	private List<ForcastedAdvertiserObj> forcastAdvertiserDataList;
	private List<AdvertiserTrendAnalysisHeaderDTO> advertiserTrendAnalysisHeaderList;
 	private List<AdvertiserTrendAnalysisActualDTO> advertiserTrendAnalysisActualDatarList;
 	private List<AdvertiserTrendAnalysisForcastDTO> advertiserTrendAnalysisForcastlDatarList;
 	private List<AdvertiserPerformanceSummaryHeaderDTO> performanceSummaryHeaderDataList;
 	private List<AdvertiserTrendAnalysisActualDTO> advertiserTrendAnalysisActualDataGraphList;
 	private List<AdvertiserReallocationTableDTO> advertiserReallocationDataList;
 	private List<AgencyDTO> agencies;
 	private List<AdvertiserDTO> advertisers;
 	private List<OrderDTO> orders;
 	private List<LineItemDTO> lineitems;
 	private int performanceMetricsTotal;
 	private String clientLoginToken = null;
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    private AdvertiserReallocationHeaderDTO advertiserReallocationHeaderObj;
    private List<PropertyDropDownDTO> siteDropDownList;
    private ForcastLineItemDTO forcastLineItemDTO;
    private List<AdvertiserPerformerDTO> advertiserTotalDataListCurrent;
    private List<AdvertiserPerformerDTO> advertiserTotalDataListCompare;
    private List<AdvertiserPerformerDTO> advertiserTotalDataListMTD;
    private List<AdvertiserPerformerDTO> deliveryIndicatorDataList;
    
 	public String execute(){
		log.info("AdvertiserViewAction action executes..");
		IUserService userService = new UserService();
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return Action.SUCCESS;
	    	}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of AdvertiserViewAction : " + e.getMessage());
		}
	    return "unAuthorisedAccess";
}

	
	
	public String loadAllAgencies(){
		log.info("Going to load all agencies..");
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		agencyList=service.loadAgencies();
		if(agencyList !=null){
			log.info(" loaded agencies.."+agencyList.size());
		}else{
			log.info(" loaded agencies..null");
		}
		
		return Action.SUCCESS;
	}
	
	
    public String loadAllAdvertisers(){    	
    	String agencyName=request.getParameter("agencyName");
    	log.info("Going to load all advertisers..agencyName:"+agencyName);
    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    	advertiserList=service.loadAdvertisers(agencyName);
		return Action.SUCCESS;
	}
    
    public String loadAllOrders(){
		log.info("Going to load all Orders..");
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		ordersList=service.loadOrders();
		ordersTrandsList = ordersList;
        log.info("in load all order method orderlist=  "+ordersList.size());
		return Action.SUCCESS;
	}
	
	
    public String loadAllLineItemName(){    
    	log.info("Inside loadAllLineItemName of AdvertiserViewAction.");
    	log.info("Going to load all line item name ");
    	String orderName=request.getParameter("orderName");
    	String orderId=request.getParameter("orderId");
    	log.info("orderName : "+orderName+"  ,orderId : "+orderId);
    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    	if(orderName !=null ){
    		lineItemNameList= service.loadLineItemName(orderName);
    	}else if(orderId !=null){
    		lineItemNameList= service.loadLineItemName(Long.parseLong(orderId));
    	}else{
    		log.info("orderName or oderId is null.");
    	}
    	
    	lineItemNameTrandsList = lineItemNameList;
		return Action.SUCCESS;
	}
    
    public String lineItemById(){    	
    	String lineItemId=request.getParameter("lineItemId");
    	log.info("Going to load lineitem, lineItemId:"+lineItemId);
    	if(lineItemId !=null && lineItemId.trim().length()>=0){    		
    		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    		lineItemNameList=service.loadLineItem(Long.parseLong(lineItemId));        	
    	}else{
    		log.info("lineItemId is null");
    	}
		return Action.SUCCESS;
	}
    
    public String reallocationItem(){
    	log.info("inside reallocationItem action");
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");		
		log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
			String [] dateArrayU=upperDate.split("-");
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2])+1);						
		}else{
			Date currentDate=new Date();
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		//lowerDate = "2013-05-07";
		//upperDate = "2013-05-16";
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
        ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
        reallocationItemList=service.loadReallocationItems(lowerDate,upperDate);
		
		return Action.SUCCESS;
	}
    
    public String forcastAdvertiserData(){
    	log.info("inside forcastAdvertiserData action...........................................");
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");		
		log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
			String [] dateArrayU=upperDate.split("-");
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2])+1);						
		}else{
			Date currentDate=new Date();
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		//lowerDate = "2013-05-07";
		//upperDate = "2013-05-16";
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
        ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
        forcastAdvertiserDataList=service.loadForcastAdvertiserData(lowerDate,upperDate);		
		return Action.SUCCESS;
	}

   
	
    
	public String forcastLineGraph() { 
    	log.info("forcastLineGraph action...........................................");
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");		
		log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
			String [] dateArrayU=upperDate.split("-");
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2])+1);						
		}else{
			Date currentDate=new Date();
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
    	 String title = "title";
    	 ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
         forcastAdvertiserDataList=service.loadForcastAdvertiserData(lowerDate,upperDate);
		 
	    	
	    	StringBuilder impressionStr = new StringBuilder();
	    	StringBuilder clicksStr = new StringBuilder();
	    	StringBuilder ctrStr = new StringBuilder();
	    	if(forcastAdvertiserDataList !=null && forcastAdvertiserDataList.size()>0){
	    		impressionStr.append("[['Date',   'Impressions']");
	    		for (ForcastedAdvertiserObj object : forcastAdvertiserDataList) {
					impressionStr.append(",[");
					String str = object.getDays();
					String subStr = str.substring(8, 10);
					impressionStr.append("'"+subStr+"',"+object.getDeliveredImpressions());
					impressionStr.append("]");
				}
				impressionStr.append("]");
				
				clicksStr.append("[['Date',   'Clicks']");
				for (ForcastedAdvertiserObj object : forcastAdvertiserDataList) {
					clicksStr.append(",[");
					String str = object.getDays();
					String subStr = str.substring(8, 10);
					clicksStr.append("'"+subStr+"',"+object.getClicks());
					clicksStr.append("]");
				}
				clicksStr.append("]");
				
				ctrStr.append("[['Date',   'CTR']");
				for (ForcastedAdvertiserObj object : forcastAdvertiserDataList) {
					ctrStr.append(",[");
					String str = object.getDays();
					String subStr = str.substring(8, 10);
					ctrStr.append("'"+subStr+"',"+object.getCtr());
					ctrStr.append("]");
				}
				ctrStr.append("]");
	    	}else{
	    		log.info("No chart data found, show default...");
	    		impressionStr.append("[['Date',   'Impressions']");
	    		impressionStr.append(",['31',129.0]");
	    		impressionStr.append("]");
	    		
	    		clicksStr.append("[['Date',  'Clicks']");
	    		clicksStr.append(",['31',3]");
	    		clicksStr.append("]");
	    		
	    		ctrStr.append("[['Date',   'CTR']");
	    		ctrStr.append(",['31',2.33]");
	    		ctrStr.append("]");
	    	}
			
			headerMap=new HashMap<String, String>();
			headerMap.put("title", title);
			headerMap.put("impression", impressionStr.toString());
			headerMap.put("click", clicksStr.toString());
			headerMap.put("ctr", ctrStr.toString());
			
		 return Action.SUCCESS;
	 }
    
    public String advertiserById(){    	
    	String advertiserId=request.getParameter("advertiserId");
    	log.info("Going to load advertisers..advertiserId:"+advertiserId);
    	if(advertiserId !=null && advertiserId.trim().length()>=0){    		
    		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
        	advertiserList=service.loadAdvertiserDetails(Long.parseLong(advertiserId));        	
    	}else{
    		log.info("advertiserId is null");
    	}
		return Action.SUCCESS;
	}
    
    public String loadTrendAnalysisHeaderDataAdvertiserView() throws Exception{
    	log.info("inside loadTrendAnalysisHeaderData in advertiserVierAction");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String canpainOrder=request.getParameter("order");
		String lineOrder = request.getParameter("lineItem");
		String publisherName = request.getParameter("publisherName");
		String properties = request.getParameter("properties");
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		long userId = sessionDTO.getUserId();
		String term = request.getParameter("term");
		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
		if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
			IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
			List<String> list = new ArrayList<String>();
			list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
			properties = "";
			for(int i=0;i<list.size();i++){
				if(i==0){
					properties = properties+ list.get(i);
				}else{
					properties = properties+ ","+list.get(i);
				}
			}
			
		}else if(properties!=null && !properties.equals("")){
			List<String> list = new ArrayList<String>();
			list = service.getDFPPropertyName(properties);
			properties = "";
			for(int i=0;i<list.size();i++){
				if(i==0){
					properties = properties+ list.get(i);
				}else{
					properties = properties+ ","+list.get(i);
				}
			}
		}
		lowerDate = lowerDate+" 00:00:00";
		upperDate = upperDate+" 00:00:00";
		advertiserTrendAnalysisHeaderList = service.loadTrendAnalysisHeaderDataAdvertiserView(lowerDate, upperDate, canpainOrder, lineOrder, publisherName,properties);
	    if(advertiserTrendAnalysisHeaderList!=null && advertiserTrendAnalysisHeaderList.size()!=0){
	    	log.info("advertiserTrendAnalysisHeaderList :" +  advertiserTrendAnalysisHeaderList.size());
	    }
	    else {
	    	log.info("advertiserTrendAnalysisHeaderList : null");
	    }
	    return Action.SUCCESS;
    }
    
    public String loadTrendAnalysisActualDataAdvertiserView(){
    	log.info("inside loadTrendAnalysisActualData in advertiserViewAction ");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String order=request.getParameter("order");
		String lineItem = request.getParameter("lineItem");
		String publisherName = request.getParameter("publisherName");
		
		String properties = request.getParameter("properties");
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		long userId = sessionDTO.getUserId();
		String term = request.getParameter("term");
		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
		if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
			IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
			List<String> list = new ArrayList<String>();
			list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
			properties = "";
			for(int i=0;i<list.size();i++){
				if(i==0){
					properties = properties+ list.get(i);
				}else{
					properties = properties+ ","+list.get(i);
				}
			}
			
		}else if(properties!=null && !properties.equals("")){
			List<String> list = new ArrayList<String>();
			list = service.getDFPPropertyName(properties);
			properties = "";
			for(int i=0;i<list.size();i++){
				if(i==0){
					properties = properties+ list.get(i);
				}else{
					properties = properties+ ","+list.get(i);
				}
			}
		}
		lowerDate = lowerDate+" 00:00:00";
		upperDate = upperDate+" 00:00:00";
		
		advertiserTrendAnalysisActualDatarList = service.loadTrendAnalysisActualDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName,properties);
		if(advertiserTrendAnalysisActualDatarList!=null && advertiserTrendAnalysisActualDatarList.size()!=0){
			log.info("advertiserTrendAnalysisActualDatarList :" +  advertiserTrendAnalysisActualDatarList.size());
		}
		else {
			log.info("advertiserTrendAnalysisActualDatarList : null");
		}
		return Action.SUCCESS;
    	
    }
    
    public String loadTrendAnalysisForcastDataAdvertiserView(){
    	log.info("inside loadTrendAnalysisActualData in advertiserViewAction ");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String order=request.getParameter("order");
		String lineItem = request.getParameter("lineItem");
		String publisherName = request.getParameter("publisherName");
		
		String properties = request.getParameter("properties");
		
		lowerDate = lowerDate+" 00:00:00";
		upperDate = upperDate+" 00:00:00";
		
		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
		advertiserTrendAnalysisForcastlDatarList = service.loadTrendAnalysisForcastDataAdvertiserView(lowerDate, upperDate, order, lineItem, publisherName, properties);
		if(advertiserTrendAnalysisForcastlDatarList!=null && advertiserTrendAnalysisForcastlDatarList.size()!=0){
			log.info("advertiserTrendAnalysisForcastlDatarList :" +  advertiserTrendAnalysisForcastlDatarList.size());
		}
		else {
			log.info("advertiserTrendAnalysisForcastlDatarList : null");
		}
		return Action.SUCCESS;
    	
    }
    
    public String loadPerformerLineItems(){
    	
    	try {
    		String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");	
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			if(lowerDate == null || upperDate == null){
				Date currentDate=new Date();
				lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
				upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			topPerformerLineItemList = service.loadPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
			log.info("topPerformerLineItemList.size()"+topPerformerLineItemList.size());
	    }
		catch (Exception e) {
			log.severe("Exception in loadPerformerLineItems in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    	
    }
  
    public String loadNonPerformerLineItems(){
    	log.info("Inside loadNonPerformerLineItems in AdvertiserViewAction");
    	try {
    		String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			if(lowerDate == null || upperDate == null){
				Date currentDate=new Date();
				lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
				upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			topNonPerformerLineItemList = service.loadNonPerformerLineItems(lowerDate,upperDate,advertiser,agency, publisherName,properties);
			
	    }
		catch (Exception e) {
			log.severe("Exception in loadNonPerformerLineItems in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    	
    }
    
    public String loadPerformanceSummaryHeaderData(){
    	log.info("Inside loadPerformanceSummaryHeaderData in AdvertiserViewAction");
    	try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			log.info("advertiser:"+advertiser+" and agency:"+agency);
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			performanceSummaryHeaderDataList=service.loadPerformanceSummaryHeaderData(lowerDate,advertiser,upperDate,agency, publisherName,properties);
    	}
    	catch (Exception e) {
			log.severe("Exception in loadPerformanceSummaryHeaderData in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
    
    public String loadMostActiveLineItems(){
    	log.info("Inside loadMostActiveLineItems in AdvertiserViewAction");
    	try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String compareStartDate=request.getParameter("compareStartDate");
			String compareEndDate=request.getParameter("compareEndDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			compareStartDate = compareStartDate+" 00:00:00";
			compareEndDate = compareEndDate+" 00:00:00";
			
			log.info("advertiser:"+advertiser+" and agency:"+agency);
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			log.info("compareStartDate:"+compareStartDate+" and compareEndDate:"+compareEndDate);
			mostActiveLineItemList=service.loadMostActiveLineItems(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
    	}
    	catch (Exception e) {
			log.severe("Exception in loadMostActiveLineItems in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
    
    public String loadTopGainers(){
    	log.info("Inside loadTopGainers in AdvertiserViewAction");
    	try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String compareStartDate=request.getParameter("compareStartDate");
			String compareEndDate=request.getParameter("compareEndDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			compareStartDate = compareStartDate+" 00:00:00";
			compareEndDate = compareEndDate+" 00:00:00";
			
			log.info("advertiser:"+advertiser+" and agency:"+agency);
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			log.info("compareStartDate:"+compareStartDate+" and compareEndDate:"+compareEndDate);
			topGainersLineItemList=service.loadTopGainers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
    	}
    	catch (Exception e) {
			log.severe("Exception in loadTopGainers in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
    
    public String loadTopLosers(){
    	log.info("Inside loadTopLosers in AdvertiserViewAction");
    	try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String compareStartDate=request.getParameter("compareStartDate");
			String compareEndDate=request.getParameter("compareEndDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			compareStartDate = compareStartDate+" 00:00:00";
			compareEndDate = compareEndDate+" 00:00:00";
			
			log.info("advertiser:"+advertiser+" and agency:"+agency);
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			log.info("compareStartDate:"+compareStartDate+" and compareEndDate:"+compareEndDate);
			topLosersLineItemList=service.loadTopLosers(lowerDate,upperDate,compareStartDate,compareEndDate,advertiser,agency, publisherName,properties);
    	}
    	catch (Exception e) {
			log.severe("Exception in loadTopLosers in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
    
    public String loadPerformanceMetrics(){
    	log.info("Inside loadPerformanceMetrics in AdvertiserViewAction");
    	try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			
			log.info("advertiser:"+advertiser+" and agency:"+agency);
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			performanceMetricsList = service.loadPerformanceMetrics(lowerDate,upperDate,advertiser,agency, publisherName,properties);
    	}
    	catch (Exception e) {
			log.severe("Exception in loadPerformanceMetrics in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
    
    public String loadAdvertiserBylocationGraphData(){ 
    	log.info("Inside loadAdvertiserBylocationGraphData in AdvertiserViewAction");
    	try {
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}/*else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}*/
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";

			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			advertiserByLocationDataMap=new HashMap<String, String>();
	    	StringBuilder sbStr = new StringBuilder();
	    	service.loadAdvertisersByLocationData(lowerDate,upperDate,advertiser,agency,sbStr, publisherName,properties);
	    	if(sbStr != null && !sbStr.toString().trim().equalsIgnoreCase("")) {
	    		advertiserByLocationDataMap.put("JsonData", sbStr.toString());
	    		advertiserByLocationDataMap.put("isEmpty", "No");
	    	}
	    	else {
	    		advertiserByLocationDataMap.put("isEmpty", "Yes");
	    	}
    	}
    	catch (Exception e) {
			log.severe("Exception in loadAdvertiserBylocationGraphData in AdvertiserViewAction"+e.getMessage());
			
		}
	    return Action.SUCCESS;
    }
    
    public String loadAdvertiserByMarketGraphData(){ 
    	log.info("Inside loadAdvertiserByMarketGraphData in AdvertiserViewAction");
    	try {
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}/*else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}*/
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";

			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			advertiserByLocationDataMap=new HashMap<String, String>();
	    	StringBuilder sbStr = new StringBuilder();
	    	service.loadAdvertiserByMarketGraphData(lowerDate,upperDate,advertiser,agency,sbStr, publisherName,properties);
	    	if(sbStr != null && !sbStr.toString().trim().equalsIgnoreCase("")) {
	    		advertiserByLocationDataMap.put("JsonData", sbStr.toString());
	    		advertiserByLocationDataMap.put("isEmpty", "No");
	    	}
	    	else {
	    		advertiserByLocationDataMap.put("isEmpty", "Yes");
	    	}
    	}
    	catch (Exception e) {
			log.severe("Exception in loadAdvertiserByMarketGraphData in AdvertiserViewAction"+e.getMessage());
			
		}
	    return Action.SUCCESS;
    }
    
    public String loadLineItemForPopup(){
    	log.info("Inside loadLineItemForPopup in AdvertiserViewAction");
    	try {
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");		
			String lineItemName=request.getParameter("lineItemName");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
	
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate+" and lineItemName : "+lineItemName);
			if(lineItemName !=null && !lineItemName.trim().equalsIgnoreCase("")){			
				popUpDTOObj=service.getLineItemForPopUP(lowerDate, upperDate, lineItemName, publisherName,properties);
			}else{
				log.info("Invalid lineItemName:"+lineItemName);
			}
    	} catch (Exception e) {
    		log.severe("Exception in loadLineItemForPopup in AdvertiserViewAction"+e.getMessage());
			
		}
    	return Action.SUCCESS;
    }
    
 
    public String actualLineGraphAdvertiser() { 
    	try {
	    	log.info("inside actualLineGraph action...........................................");
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");	
			String lineOrderArr = request.getParameter("lineItem");
			String orderArr = request.getParameter("order");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long userId = sessionDTO.getUserId();
			String term = request.getParameter("term");
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			if(properties!=null &&( properties.equalsIgnoreCase("All Properties")||properties.equals(""))){
				IAdvertiserDAO advertiserDAO = new AdvertiserDAO();
				List<String> list = new ArrayList<String>();
				list = advertiserDAO.loadAdvertiserDFPPropertyList(publisherName, userId, term);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
				
			}else if(properties!=null && !properties.equals("")){
				List<String> list = new ArrayList<String>();
				list = service.getDFPPropertyName(properties);
				properties = "";
				for(int i=0;i<list.size();i++){
					if(i==0){
						properties = properties+ list.get(i);
					}else{
						properties = properties+ ","+list.get(i);
					}
				}
			}
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			
			advertiserTrendAnalysisActualDataGraphList = service.loadTrendAnalysisActualDataAdvertiserView(lowerDate, upperDate, orderArr, lineOrderArr, publisherName,properties);
		
			log.info("advertiserTrendAnalysisActualDataGraphList:"+advertiserTrendAnalysisActualDataGraphList.size());			
			 headerMap=createActualLineGraph(advertiserTrendAnalysisActualDataGraphList);
    	}catch (Exception e) {
    		log.severe("Exception in actualLineGraphAdvertiser in AdvertiserViewAction"+e.getMessage());
			
		}
		 return Action.SUCCESS;
	 }
    
    public  Map<String,String> createActualLineGraph(List<AdvertiserTrendAnalysisActualDTO> advertiserTrendAnalysisActualDataList){
    	log.info("Inside createActualLineGraph in AdvertiserViewAction");
    	
    	StringBuilder impressionStr = new StringBuilder();
		StringBuilder clicksStr = new StringBuilder();
		StringBuilder ctrStr = new StringBuilder();
		List<String> lineOrderList = new ArrayList<String>();
		String lineOrderArr = request.getParameter("lineItem");
		String orderArr = request.getParameter("order");
		String formatedImpression = "";
		String formatedClicks = "";
		String formatedCTR = "";
		
		
		if(lineOrderArr!=null){
			
			String[] str = lineOrderArr.split(",");
			if(str!=null){
				for (String lineOrder : str) {
					lineOrderList.add(lineOrder);
				}
			}
			
			Collections.sort(lineOrderList);	
		}
		
		if(lineOrderList!=null&& lineOrderList.size()!=0){
			
		if (advertiserTrendAnalysisActualDataList != null && advertiserTrendAnalysisActualDataList.size() > 0) {
					
					int flagFirst = 0 ;
					int lineOrdersCount=0;
					int count2=0;
					int count3=0;
					int notused = 0;
					int rowCount = 0;
                	String date= null;
			    	String[] arrSChannel = new String[lineOrderList.size()];
					impressionStr.append("[['Date' ");
					clicksStr.append("[['Date' ");			    	
					ctrStr.append("[['Date' ");
					int z=0;
					for (String str : lineOrderList) {
						impressionStr.append(", '"+str+"'");
						clicksStr.append(", '"+str+"'");
						ctrStr.append(", '"+str+"'");
						arrSChannel[z]= str.toString();
						z++;
					}	
					impressionStr.append("]");
					clicksStr.append("]");
					ctrStr.append("]");
					lineOrdersCount = lineOrderList.size();
					for(int j = 0;j<arrSChannel.length;j++){
					}
					
					for (AdvertiserTrendAnalysisActualDTO object : advertiserTrendAnalysisActualDataList) {
						formatedImpression = String.valueOf(object.getDeliveredImpression());
						formatedClicks = String.valueOf(object.getClicks());
						formatedCTR = String.valueOf(object.getCTR()+"0000");
						formatedCTR = formatedCTR.substring(0,formatedCTR.lastIndexOf(".")+5);
						rowCount++;
						//log.info("inside first for loop : "+object.getLineItem());
						if (count3 >= lineOrdersCount ){
								count3 = 0;
						}
						
						notused = 0;
						
	                     for (count2 =count3 ; (count2 < lineOrdersCount ); count2++ ){
	                    	
					   	 if (arrSChannel[count2].equalsIgnoreCase(object.getLineItem()))
								{
									
									if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == 0 )
									{
										log.info("!object.getDate().equals(date)) && count2 == 0");
										if (flagFirst != 0){
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
										impressionStr.append("'" + subStr + "'," + formatedImpression);
										clicksStr.append("'" + subStr + "'," +formatedClicks);
										ctrStr.append("'" + subStr + "'," +formatedCTR);
										date = object.getDate();
										
									}
									else if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == lineOrdersCount-1  )
									{
										
										impressionStr.append("," +formatedImpression);
										clicksStr.append("," +formatedClicks);
										ctrStr.append("," +formatedCTR);
										
										if (flagFirst != 0){
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
										impressionStr.append("'" + subStr + "'," + formatedImpression);
										clicksStr.append("'" + subStr + "'," + formatedClicks);
										ctrStr.append("'" + subStr + "'," + formatedCTR);
										date = object.getDate();
										
									}
									else if (object.getDate().equalsIgnoreCase(date)){
										impressionStr.append("," +formatedImpression);
										clicksStr.append("," +formatedClicks);
										ctrStr.append("," +formatedCTR);
									}
									count3 = count2+1;
									notused = 1;
										if (rowCount != advertiserTrendAnalysisActualDataList.size() ){
											break;
										}
								}else{
									log.info("inside first  else block : " +arrSChannel[count2]);
									if ( (!object.getDate().equalsIgnoreCase(date)) && count2==0 )
									{
										if (flagFirst != 0){
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
										impressionStr.append("'" + subStr + "',0.0");
										clicksStr.append("'" + subStr + "',0.0");
										ctrStr.append("'" + subStr + "',0.0");
										date = object.getDate();
										
									}
									else if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == lineOrdersCount-1 )
									{
										
										impressionStr.append(",0.0");
										clicksStr.append(",0.0");
										ctrStr.append(",0.0");
										
										if (flagFirst != 0){
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										
										flagFirst =1;	
										
										if (notused == 0){
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											impressionStr.append("'" + subStr + "'," + formatedImpression);
											clicksStr.append("'" + subStr + "'," + formatedClicks);
											ctrStr.append("'" + subStr + "'," + formatedCTR);
											date = object.getDate();
											count3 = 1;
											notused = 1;
											break;
											
											
										}else{
																	
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											impressionStr.append("'" + subStr + "',0.0");
											clicksStr.append("'" + subStr + "',0.0");
											ctrStr.append("'" + subStr + "',0.0");
											date = object.getDate();
											
											
										}
								}							
									else {
									
										impressionStr.append(",0.0");
										clicksStr.append(",0.0");
										ctrStr.append(",0.0");
									}
									
								}
								
	                     	}
	                     
					
						}
							
					impressionStr.append("]]");
					clicksStr.append("]]");
					ctrStr.append("]]");

        			} 
		            else {
					log.info("No chart data found, show default...");


					impressionStr.append("[['Date',   'Impressions']");
					impressionStr.append(",['31',131.0]");
					impressionStr.append("]");

					clicksStr.append("[['Date',  'Clicks']");
					clicksStr.append(",['31',5]");
					clicksStr.append("]");

					ctrStr.append("[['Date',   'CTR']");
					ctrStr.append(",['31',3.10]");
					ctrStr.append("]");
				}

			
		}
		//log.info("list size = "+advertiserTrendAnalysisActualDataList.size());
		
		Map<String,String> graphMap = new HashMap<String, String>();
		graphMap.put("title", "title");
		/*graphMap.put("eCPM", ecpmStr.toString());
		graphMap.put("fillRate", fillRateStr.toString());
		graphMap.put("revenue", revenueStr.toString());*/
		graphMap.put("impression", impressionStr.toString());
		graphMap.put("click", clicksStr.toString());
		graphMap.put("ctr", ctrStr.toString());
		
		return graphMap;
    }
    
    public String forcastLineGraphAdvertiser() { 
    	log.info("inside actualLineGraph action...........................................");
    	try {
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");	
			String lineOrderArr = request.getParameter("lineItem");
			String orderArr = request.getParameter("order");
			String publisherName = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			
			lowerDate = lowerDate+" 00:00:00";
			upperDate = upperDate+" 00:00:00";
			//orderArr = "KRQE | Academy Plumbing | Ask A Pro Q2 2013";
			//lineOrderArr = "Academy Plumbing | Ask A Pro Q2 2013 | ROS - newsTouch";
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			advertiserTrendAnalysisForcastlDatarList = service.loadTrendAnalysisForcastDataAdvertiserView(lowerDate, upperDate, orderArr, lineOrderArr, publisherName,properties);
		
	//         log.info("advertiserTrendAnalysisActualDataGraphList:"+advertiserTrendAnalysisActualDataGraphList.size());			
			 headerMap=createForcastLineGraph(advertiserTrendAnalysisForcastlDatarList);
    	}catch (Exception e) {
    		log.severe("Exception in actualLineGraphAdvertiser in AdvertiserViewAction"+e.getMessage());
			
		}
		 return Action.SUCCESS;
	 }
    
    public  Map<String,String> createForcastLineGraph(List<AdvertiserTrendAnalysisForcastDTO> advertiserTrendAnalysisForcastDataList){
    	StringBuilder impressionStr = new StringBuilder();
		StringBuilder clicksStr = new StringBuilder();
		StringBuilder ctrStr = new StringBuilder();
		List<String> lineOrderList = new ArrayList<String>();
		String lineOrderArr = request.getParameter("lineItem");
		String orderArr = request.getParameter("order");
		//orderArr = "KRQE | Academy Plumbing | Ask A Pro Q2 2013";
		//lineOrderArr = "Academy Plumbing | Ask A Pro Q2 2013 | ROS - newsTouch";
		
		if(lineOrderArr!=null){
			
			String[] str = lineOrderArr.split(",");
			if(str!=null){
				for (String lineOrder : str) {
					lineOrderList.add(lineOrder);
				}
			}
			
			Collections.sort(lineOrderList);	
		}
		//log.info( "channelArrList " +channelArrList);
		if(lineOrderList!=null&& lineOrderList.size()!=0){
			
		if (advertiserTrendAnalysisForcastDataList != null && advertiserTrendAnalysisForcastDataList.size() > 0) {
					
					int flagFirst = 0 ;
					int count1=0;
					int count2=0;
					int count3=0;
					int notused = 0;
					int rowCount = 0;
                	String date= null;
			    	//StringBuilder struStr = new StringBuilder();		
			    	String[] arrSChannel = new String[lineOrderList.size()];
					//fillRateStr.append("[['Date' ");			    	
					//struStr.append("[['Date' ");
					//revenueStr.append("[['Date' ");			    	
					impressionStr.append("[['Date' ");
					clicksStr.append("[['Date' ");			    	
					ctrStr.append("[['Date' ");
					int z=0;
					for (String str : lineOrderList) {
						
						//log.info("channelArrList :"+str);
						//struStr.append(", '"+str+"'");
						//fillRateStr.append(", '"+str+"'");
						//revenueStr.append(", '"+str+"'");
						impressionStr.append(", '"+str+"'");
						clicksStr.append(", '"+str+"'");
						ctrStr.append(", '"+str+"'");
						arrSChannel[z]= str.toString();
						z++;
					}	
					//struStr.append("]");
					//fillRateStr.append("]");
					//revenueStr.append("]");
					impressionStr.append("]");
					clicksStr.append("]");
					ctrStr.append("]");
					count1 = lineOrderList.size();
					log.info("count1 " +count1);
					
					for(int j = 0;j<arrSChannel.length;j++){
						log.info("arrSChannel : "+arrSChannel[j]);
					}
					
					for (AdvertiserTrendAnalysisForcastDTO object : advertiserTrendAnalysisForcastDataList) {
						rowCount++;
						log.info("inside first for loop : "+object.getLineOrder());
						if (count3 >= count1 ){
								count3 = 0;
						}
						
						notused = 0;
						
	                     for (count2 =count3 ; (count2 < count1 ); count2++ ){
	                    	// log.info("inside Second for loop : " +count2);
	                    	// log.info("inside befor first if block  arrSChannel[count2]  : " +arrSChannel[count2]);
	                    	// log.info("inside befor first if block getChannelName : " +object.getChannelName());
					   	 if (arrSChannel[count2].equalsIgnoreCase(object.getLineOrder()))
								{
									//log.info("inside first if block : " +object.getChannelName());
									if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == 0 )
									{
										log.info("!object.getDate().equals(date)) && count2 == 0");
										if (flagFirst != 0){
											//struStr.append("]");
											//fillRateStr.append("]");
											//revenueStr.append("]");
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										//struStr.append(",[");
										//fillRateStr.append(",[");
										//revenueStr.append(",[");
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
										//struStr.append("'" + subStr + "'," + object.geteCPM());
										//fillRateStr.append("'" + subStr + "'," + object.getFillRate());
										//revenueStr.append("'" + subStr + "'," + object.getRevenue());
										impressionStr.append("'" + subStr + "'," + object.getDeliveredImpression());
										clicksStr.append("'" + subStr + "'," + object.getClicks());
										ctrStr.append("'" + subStr + "'," + object.getCTR());
										date = object.getDate();
										
									}
									else if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == count1-1  )
									{
										
										//struStr.append("," +object.geteCPM());
										//fillRateStr.append("," +object.getFillRate());
										//revenueStr.append("," +object.getRevenue());
										impressionStr.append("," +object.getDeliveredImpression());
										clicksStr.append("," +object.getClicks());
										ctrStr.append("," +object.getCTR());
										
										if (flagFirst != 0){
											//struStr.append("]");
											//fillRateStr.append("]");
											//revenueStr.append("]");
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										//struStr.append(",[");
										//fillRateStr.append(",[");
										//revenueStr.append(",[");
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
										//struStr.append("'" + subStr + "'," + object.geteCPM());
										//fillRateStr.append("'" + subStr + "'," + object.getFillRate());
										//revenueStr.append("'" + subStr + "'," + object.getRevenue());
										impressionStr.append("'" + subStr + "'," + object.getDeliveredImpression());
										clicksStr.append("'" + subStr + "'," + object.getClicks());
										ctrStr.append("'" + subStr + "'," + object.getCTR());
										date = object.getDate();
										
									}
									else if (object.getDate().equalsIgnoreCase(date)){
										//struStr.append("," +object.geteCPM());
										//fillRateStr.append("," +object.getFillRate());
										//revenueStr.append("," +object.getRevenue());
										impressionStr.append("," +object.getDeliveredImpression());
										clicksStr.append("," +object.getClicks());
										ctrStr.append("," +object.getCTR());
									}
									//log.info("struStr inside if : " + struStr );
									count3 = count2+1;
									notused = 1;
										if (rowCount != advertiserTrendAnalysisForcastDataList.size() ){
											break;
										}
								}else{
									log.info("inside first  else block : " +arrSChannel[count2]);
									if ( (!object.getDate().equalsIgnoreCase(date)) && count2==0 )
									{
										if (flagFirst != 0){
											//struStr.append("]");
										//	fillRateStr.append("]");
											//revenueStr.append("]");
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										flagFirst =1;							
										//struStr.append(",[");
									//	fillRateStr.append(",[");
									//	revenueStr.append(",[");
										impressionStr.append(",[");
										clicksStr.append(",[");
										ctrStr.append(",[");
										String str = object.getDate();
										String subStr = str.substring(8, 10);
									//	struStr.append("'" + subStr + "',0.0");
									//	fillRateStr.append("'" + subStr + "',0.0");
									//	revenueStr.append("'" + subStr + "',0.0");
										impressionStr.append("'" + subStr + "',0.0");
										clicksStr.append("'" + subStr + "',0.0");
										ctrStr.append("'" + subStr + "',0.0");
										date = object.getDate();
										
									}
									else if ( (!object.getDate().equalsIgnoreCase(date)) && count2 == count1-1 )
									{
										
									//	struStr.append(",0.0");
									//	fillRateStr.append(",0.0");
									//	revenueStr.append(",0.0");
										impressionStr.append(",0.0");
										clicksStr.append(",0.0");
										ctrStr.append(",0.0");
										
										if (flagFirst != 0){
										//	struStr.append("]");
										//	fillRateStr.append("]");
										//	revenueStr.append("]");
											impressionStr.append("]");
											clicksStr.append("]");
											ctrStr.append("]");
										}
										
										flagFirst =1;	
										
										if (notused == 0){
											//struStr.append(",[");
										//	fillRateStr.append(",[");
										//	revenueStr.append(",[");
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											//struStr.append("'" + subStr + "'," + object.geteCPM());
											//fillRateStr.append("'" + subStr + "'," + object.getFillRate());
										//	revenueStr.append("'" + subStr + "'," + object.getRevenue());
											impressionStr.append("'" + subStr + "'," + object.getDeliveredImpression());
											clicksStr.append("'" + subStr + "'," + object.getClicks());
											ctrStr.append("'" + subStr + "'," + object.getCTR());
											date = object.getDate();
											count3 = 1;
											notused = 1;
											break;
											
											
										}else{
																	
											//struStr.append(",[");
											//fillRateStr.append(",[");
											//revenueStr.append(",[");
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											//struStr.append("'" + subStr + "',0.0");
										//	fillRateStr.append("'" + subStr + "',0.0");
										//	revenueStr.append("'" + subStr + "',0.0");
											impressionStr.append("'" + subStr + "',0.0");
											clicksStr.append("'" + subStr + "',0.0");
											ctrStr.append("'" + subStr + "',0.0");
											date = object.getDate();
											
											
										}
								}							
									else {
									
										//struStr.append(",0.0");
										//fillRateStr.append(",0.0");
										//revenueStr.append(",0.0");
										impressionStr.append(",0.0");
										clicksStr.append(",0.0");
										ctrStr.append(",0.0");
									}
								//	log.info("struStr inside Else : " + struStr );
									
								}
								
	                     	}
	                     
	                   //  log.info("struStr inside For : " + struStr );
					
						}
							
					
					//struStr.append("]]");
					//fillRateStr.append("]]");
				//	revenueStr.append("]]");
					impressionStr.append("]]");
					clicksStr.append("]]");
					ctrStr.append("]]");

				    //log.info("struStr :"+ struStr.toString());
				    //log.info("fillRateStr :"+ fillRateStr.toString());
					
				//	ecpmStr = struStr;
        			} 
		            else {
					log.info("No chart data found, show default...");

					//ecpmStr.append("[['Date',   'eCPM']");
					//ecpmStr.append(",['31',131.0]");
				//	ecpmStr.append("]");

				//	fillRateStr.append("[['Date',   'FILL RATE(%)']");
				//	fillRateStr.append(",['31',131.0]");
				//	fillRateStr.append("]");

				//	revenueStr.append("[['Date',   'REVENUE']");
				//	revenueStr.append(",['31',131.0]");
				//	revenueStr.append("]");

					impressionStr.append("[['Date',   'Impressions']");
					impressionStr.append(",['31',131.0]");
					impressionStr.append("]");

					clicksStr.append("[['Date',  'Clicks']");
					clicksStr.append(",['31',5]");
					clicksStr.append("]");

					ctrStr.append("[['Date',   'CTR']");
					ctrStr.append(",['31',3.10]");
					ctrStr.append("]");
				}

			
		}
		log.info("list size = "+advertiserTrendAnalysisForcastDataList.size());
		
		Map<String,String> graphMap = new HashMap<String, String>();
		graphMap.put("title", "title");
		/*graphMap.put("eCPM", ecpmStr.toString());
		graphMap.put("fillRate", fillRateStr.toString());
		graphMap.put("revenue", revenueStr.toString());*/
		graphMap.put("impression", impressionStr.toString());
		graphMap.put("click", clicksStr.toString());
		graphMap.put("ctr", ctrStr.toString());
		
		return graphMap;
    }
    
    public String loadAgencyDropDownList()
    {
    	log.info("Inside loadAgencyDropDownList in AdvertiserViewAction");
    	try {
    		//String publisherId = request.getParameter("publisherId");
    		String publisherName = request.getParameter("publisherName");
    		String term = request.getParameter("term");
    		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			agencies = service.getAgencyDropDownList(publisherName,term);
	    }
		catch (Exception e) {
			log.severe("Exception in getAgencyDropDownList in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    	
    
    }
    
    public String loadAdvertiserDropDownList()
    {

    	log.info("Inside loadAdvertiserDropDownList in AdvertiserViewAction");
    	try {
    		//String publisherId = request.getParameter("publisherId");
    		String publisherName = request.getParameter("publisherName");
    		String agencyId = request.getParameter("agencyId");
    		String term = request.getParameter("term");
    		log.info("INSIDE GET ADVERTISER DROP DOWN LIST() "+publisherName+" "+agencyId+" "+term);
    		
    		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			advertisers = service.getAdvertiserDropDownList(publisherName,agencyId,term);
	    }
		catch (Exception e) {
			log.severe("Exception in loadAdvertiserDropDownList in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    
    }
    
   
    public String loadOrderDropDownList()
    {

    	log.info("Inside loadOrderDropDownList in AdvertiserViewAction");
    	try {
    		//String publisherId = request.getParameter("publisherId");
    		String publisherName = request.getParameter("publisherName");
    		String agencyId = request.getParameter("agencyId");
    		String advertiserId = request.getParameter("advertiserId");
    		String term = request.getParameter("term");
    		
    		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			orders = service.getOrderDropDownList(publisherName,agencyId,advertiserId,term);
	    }
		catch (Exception e) {
			log.severe("Exception in loadOrderDropDownList in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    
    }
    
    public String loadLineItemDropDownList()
    {

    	log.info("Inside loadLineItemDropDownList in AdvertiserViewAction");
    	try {
    		String publisherName = request.getParameter("publisherName");
    		String agencyId = request.getParameter("agencyId");
    		String advertiserId = request.getParameter("advertiserId");
    		String orderId = request.getParameter("orderId");
    		String term = request.getParameter("term");
    		
    		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			lineitems = service.getLineItemDropDownList(publisherName,agencyId,advertiserId,orderId,term);
			log.info("lineItemName : "+lineitems.size()+"************************************");
	    }
		catch (Exception e) {
			log.severe("Exception in loadNonPerformerLineItems in AdvertiserViewAction"+e.getMessage());
			
		}
		return Action.SUCCESS;
    
    }
    
    public String loadReallocationHeaderDataAdvertiserView(){
    	log.info("inside loadReallocationHeaderDataAdvertiserView in advertiserVierAction");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String canpainOrder=request.getParameter("orderIdReallocation");
		String lineItem = request.getParameter("lineItem");
		Map reallocationObjectsMap = new HashMap();
		  try {
			  sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    		long userId = sessionDTO.getUserId();
			  clientLoginToken = regenerateAuthToken();
		      log.info(" clientLoginToken : "+clientLoginToken);
		      log.info(" now going to build dfpSession ...");
		      dfpSession = new DfpSession.Builder()
		      				.withClientLoginToken(clientLoginToken)					
		      				.withNetworkCode(LinMobileConstants.DFP_NETWORK_CODE)
		      				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
		      				.build();
		      
		      log.info(" now going to make DfpServices instance...");
			  dfpServices = LinMobileProperties.getInstance().getDfpServices();
	      
		lowerDate = lowerDate+"T00:00:00";
		upperDate = upperDate+"T00:00:00";
		//canpainOrder = "KRQE | Academy Plumbing | Ask A Pro Q2 2013";
		//lineOrder = "Academy Plumbing | Ask A Pro Q2 2013 | ROS - newsTouch,sdfsdfsdfsd";
		/*OrderServiceInterface orderService = dfpServices.get(dfpSession, OrderServiceInterface.class);
		LineItemServiceInterface lineItemService=dfpServices.get(dfpSession, LineItemServiceInterface.class);*/
		
		log.info("before creating service object ");
		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
		reallocationObjectsMap = service.loadReallocationHeaderDataAdvertiserView(dfpServices,dfpSession,lowerDate, upperDate, canpainOrder, lineItem,userId);
	    if(reallocationObjectsMap!=null && reallocationObjectsMap.size()!=0){
	    	advertiserReallocationDataList = (List<AdvertiserReallocationTableDTO>) reallocationObjectsMap.get("TableObject");
	    	advertiserReallocationHeaderObj = (AdvertiserReallocationHeaderDTO) reallocationObjectsMap.get("HeaderObject");
	    	return Action.SUCCESS;
	    }
		} catch (ValidationException e) {
				log.severe("DFP session exception: ValidationException :"+e.getMessage());				
				
				setErrorStatus("validationError");
				 return Action.SUCCESS;
		} catch (Exception e) {
				log.severe(" Exception :"+e.getMessage());
				
				setErrorStatus("DFPAPIError");
				 return Action.SUCCESS;
	   }
		 return Action.SUCCESS;
    }
    
    public String updateReallocationLineItem(){
    	log.info("inside updateReallocationLineItem in advertiserVierAction");
    	String array = request.getParameter("array");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String canpainOrder=request.getParameter("orderIdReallocation");
		String lineItem = request.getParameter("lineItem");
    	AdvertiserReallocationTableDTO obj;
        
		lowerDate = lowerDate+"T00:00:00";
		upperDate = upperDate+"T00:00:00";
    	try{
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
    		long userId = sessionDTO.getUserId();
    		clientLoginToken = regenerateAuthToken();
		      log.info(" clientLoginToken : "+clientLoginToken);
		      log.info(" now going to build dfpSession ...");
		      dfpSession = new DfpSession.Builder()
		      				.withClientLoginToken(clientLoginToken)					
		      				.withNetworkCode(LinMobileConstants.DFP_NETWORK_CODE)
		      				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
		      				.build();
		      
		      log.info(" now going to make DfpServices instance...");
			  dfpServices = LinMobileProperties.getInstance().getDfpServices();
			  IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			  advertiserReallocationDataList = service.updateReallocationLineItem(dfpServices, dfpSession, array, userId,lowerDate, upperDate, canpainOrder, lineItem);
    		
    	}catch (ValidationException e) {
			log.severe("DFP session exception: ValidationException :"+e.getMessage());				
			
			setErrorStatus("validationError");
			 
	   }catch(Exception e){
			log.severe("Exception in updateReallocationLineItem in AdvertiserViewAction"+e.getMessage());
			
			setErrorStatus("DFPAPIError");
		  
    	}
    	return Action.SUCCESS;
    	
    }
    public String regenerateAuthToken() throws Exception {
		 String clientLoginToken=MemcacheUtil.getAuthToken();
		 if(clientLoginToken ==null){
			 ClientLoginAuth clientLoginAuth = new ClientLoginAuth(LinMobileConstants.EMAIL_ADDRESS, LinMobileConstants.EMAIL_PASSWORD);
			 clientLoginToken =clientLoginAuth.getAuthToken();
			 MemcacheUtil.setAuthToken(clientLoginToken);
		 }	    
	    return clientLoginToken;
	}
	
	  public String loadAdvertiserPropertyDropDownList()
	    {
	    	log.info("Inside loadPublisherPropertyDropDownList in PublisherViewAction");
	    	try {
	    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    		String publisherName = request.getParameter("publisherName");
	    		long userId = sessionDTO.getUserId();
	    		String term = request.getParameter("term");
	    		IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
	    		siteDropDownList = service.loadAdvertiserPropertyDropDownList(publisherName,userId,term);
				log.info("propertyDropDownList : "+ siteDropDownList.size());
		    }
			catch (Exception e) {
				log.severe("Exception in loadPublisherPropertyDropDownList in PublisherViewAction"+e.getMessage());
				
			}
			return Action.SUCCESS;
	    }
	  
	    public String loadAdvertiserTotalDataListCurrent(){
	    	log.info("inside loadAdvertiserTotalDataListCurrent in AdvertiserViewAction class");
	    	try{
	    	//String lowerDate=request.getParameter("startDate");
			//String upperDate=request.getParameter("endDate");
			//String publisher = request.getParameter("publisherName");
			
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");	
			String publisher = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			advertiserTotalDataListCurrent = service.loadAdvertiserTotalDataList(lowerDate, upperDate,publisher,advertiser,agency,properties);
	    	}catch (Exception e) {
	    		log.severe("Exception in loadAdvertiserTotalDataListCurrent in AdvertiserViewAction class");
	    		
			}
			return Action.SUCCESS;
	    }
	    
	    public String loadAdvertiserTotalDataListCompare(){
	    	log.info("inside loadAdvertiserTotalDataListCompare in AdvertiserViewAction class");
	    	try{
	    	//String lowerDate=request.getParameter("startDate");
			//String upperDate=request.getParameter("endDate");
			//String publisher = request.getParameter("publisherName");
			
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");	
			String publisher = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			advertiserTotalDataListCompare = service.loadAdvertiserTotalDataList(lowerDate, upperDate,publisher,advertiser,agency,properties);
	    	}catch (Exception e) {
	    		log.severe("Exception in loadAdvertiserTotalDataListCompare in AdvertiserViewAction class");
	    		
			}
			return Action.SUCCESS;
	    }
	    
	    public String loadAdvertiserTotalDataListMTD(){
	    	log.info("inside loadAdvertiserTotalDataListMTD in AdvertiserViewAction class");
	    	try{
	    	//String lowerDate=request.getParameter("startDate");
			//String upperDate=request.getParameter("endDate");
			//String publisher = request.getParameter("publisherName");
			
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");	
			String publisher = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			
			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			advertiserTotalDataListMTD = service.loadAdvertiserTotalDataList(lowerDate, upperDate,publisher,advertiser,agency,properties);
	    	}catch (Exception e) {
	    		log.severe("Exception in loadAdvertiserTotalDataListMTD in AdvertiserViewAction class");
	    		
			}
			return Action.SUCCESS;
	    }
	    
	    public String loadDeliveryIndicatorData(){
	    	log.info("inside loadDeliveryIndicatorData in AdvertiserViewAction class");
	    	try{
				String lowerDate=request.getParameter("startDate");
				String upperDate=request.getParameter("endDate");
				String advertiser=request.getParameter("advertiser");
				String agency=request.getParameter("agency");	
				String publisher = request.getParameter("publisherName");
				String properties = request.getParameter("properties");

			IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
			deliveryIndicatorDataList = service.loadDeliveryIndicatorData(lowerDate, upperDate, publisher, advertiser, agency, properties);
	    	}catch (Exception e) {
	    		log.severe("Exception in loadDeliveryIndicatorData in AdvertiserViewAction class");
	    		
			}
			return Action.SUCCESS;
	    }
	    
	    public String loadLineItemForcast(){
	    	System.out.println("Inside loadLineItemForcast @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	    	try{
	    		String lineItemId = request.getParameter("lineItemId");
	    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    		dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.DFP_NETWORK_CODE, LinMobileConstants.DFP_APPLICATION_NAME);
	    		
			      
			      log.info(" now going to make DfpServices instance...");
				  dfpServices = LinMobileProperties.getInstance().getDfpServices();
				  IAdvertiserService service = (IAdvertiserService) BusinessServiceLocator.locate(IAdvertiserService.class);
				  setErrorStatus("This view is not used now");
				 // forcastLineItemDTO = service.getLineItemForcast(dfpServices, dfpSession, lineItemId);
	    		
	    	}catch (ValidationException e) {
				log.severe("DFP session exception: ValidationException :"+e.getMessage());				
				
				setErrorStatus("validationError");
				 
		   }catch(Exception e){
				log.severe("Exception in updateReallocationLineItem in AdvertiserViewAction"+e.getMessage());
				
				setErrorStatus("DFPAPIError");
			  
	    	}
	    	return Action.SUCCESS;
	}
	    
	/*
	 * @author Youdhveer Panwar
	 */
	    public String newAdvertiserView(){
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			
			IUserService userService = new UserService();
			try {
				sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
				userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
		    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
		    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
		    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
		    		/*if(userDetailsDTO.getAccounts() != null && userDetailsDTO.getAccounts().trim().length() > 0 && userDetailsDTO.getOrders() != null && userDetailsDTO.getOrders().trim().length() > 0) {
		    			return "performanceSummary";
		    		}
		    		else {
		    			return "campaignSummary";
		    		}*/
		    		return "performanceSummary";
		    	}
		 	}
			catch (Exception e) {
				
				log.severe("Exception in execution of AdvertiserViewAction : " + e.getMessage());
			}
		    return "unAuthorisedAccess";	    
			
		}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public List<AdvertiserPerformerDTO> getMostActiveLineItemList() {
		return mostActiveLineItemList;
	}

	public void setMostActiveLineItemList(
			List<AdvertiserPerformerDTO> mostActiveLineItemList) {
		this.mostActiveLineItemList = mostActiveLineItemList;
	}

	public List<AdvertiserPerformerDTO> getTopGainersLineItemList() {
		return topGainersLineItemList;
	}

	public void setTopGainersLineItemList(
			List<AdvertiserPerformerDTO> topGainersLineItemList) {
		this.topGainersLineItemList = topGainersLineItemList;
	}

	public List<AdvertiserPerformerDTO> getTopLosersLineItemList() {
		return topLosersLineItemList;
	}

	public void setTopLosersLineItemList(
			List<AdvertiserPerformerDTO> topLosersLineItemList) {
		this.topLosersLineItemList = topLosersLineItemList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<String> getAgencyList() {
		return agencyList;
	}

	public void setAgencyList(List<String> agencyList) {
		this.agencyList = agencyList;
	}

	public List<AgencyAdvertiserObj> getAdvertiserList() {
		return advertiserList;
	}

	public void setAdvertiserList(List<AgencyAdvertiserObj> advertiserList) {
		this.advertiserList = advertiserList;
	}

	public List<String> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(List<String> ordersList) {
		this.ordersList = ordersList;
	}

	public List<OrderLineItemObj> getLineItemNameList() {
		return lineItemNameList;
	}

	public void setLineItemNameList(List<OrderLineItemObj> lineItemNameList) {
		this.lineItemNameList = lineItemNameList;
	}

	public List<String> getOrdersTrandsList() {
		return ordersTrandsList;
	}

	public void setOrdersTrandsList(List<String> ordersTrandsList) {
		this.ordersTrandsList = ordersTrandsList;
	}

	public PopUpDTO getPopUpDTOObj() {
		return popUpDTOObj;
	}

	public void setPopUpDTOObj(PopUpDTO popUpDTOObj) {
		this.popUpDTOObj = popUpDTOObj;
	}

	public List<OrderLineItemObj> getLineItemNameTrandsList() {
		return lineItemNameTrandsList;
	}

	public void setLineItemNameTrandsList(
			List<OrderLineItemObj> lineItemNameTrandsList) {
		this.lineItemNameTrandsList = lineItemNameTrandsList;
	}

	public List<AdvertiserPerformerDTO> getPerformanceMetricsList() {
		return performanceMetricsList;
	}

	public void setPerformanceMetricsList(
			List<AdvertiserPerformerDTO> performanceMetricsList) {
		this.performanceMetricsList = performanceMetricsList;
	}

	public List<ReallocationDataObj> getReallocationItemList() {
		return reallocationItemList;
	}

	public void setReallocationItemList(
			List<ReallocationDataObj> reallocationItemList) {
		this.reallocationItemList = reallocationItemList;
	}

	public List<ActualAdvertiserObj> getActualAdvertiserDataList() {
		return actualAdvertiserDataList;
	}

	public void setActualAdvertiserDataList(
			List<ActualAdvertiserObj> actualAdvertiserDataList) {
		this.actualAdvertiserDataList = actualAdvertiserDataList;
	}

	public List<ForcastedAdvertiserObj> getForcastAdvertiserDataList() {
		return forcastAdvertiserDataList;
	}

	public void setForcastAdvertiserDataList(
			List<ForcastedAdvertiserObj> forcastAdvertiserDataList) {
		this.forcastAdvertiserDataList = forcastAdvertiserDataList;
	}

	public Map<String,String> getAdvertiserByLocationDataMap() {
		return advertiserByLocationDataMap;
	}

	public void setAdvertiserByLocationDataMap(Map<String,String> advertiserByLocationDataMap) {
		this.advertiserByLocationDataMap = advertiserByLocationDataMap;
	}

	public void setHeaderMap(Map<String,String> headerMap) {
		this.headerMap = headerMap;
	}

	public Map<String,String> getHeaderMap() {
		return headerMap;
	}

	public void setAdvertiserTrendAnalysisHeaderList(
			List<AdvertiserTrendAnalysisHeaderDTO> advertiserTrendAnalysisHeaderList) {
		this.advertiserTrendAnalysisHeaderList = advertiserTrendAnalysisHeaderList;
	}

	public List<AdvertiserTrendAnalysisHeaderDTO> getAdvertiserTrendAnalysisHeaderList() {
		return advertiserTrendAnalysisHeaderList;
	}

	


	public List<AdvertiserTrendAnalysisActualDTO> getAdvertiserTrendAnalysisActualDatarList() {
		return advertiserTrendAnalysisActualDatarList;
	}



	public void setAdvertiserTrendAnalysisActualDatarList(
			List<AdvertiserTrendAnalysisActualDTO> advertiserTrendAnalysisActualDatarList) {
		this.advertiserTrendAnalysisActualDatarList = advertiserTrendAnalysisActualDatarList;
	}



	public List<AdvertiserTrendAnalysisActualDTO> getAdvertiserTrendAnalysisActualDataGraphList() {
		return advertiserTrendAnalysisActualDataGraphList;
	}



	public void setAdvertiserTrendAnalysisActualDataGraphList(
			List<AdvertiserTrendAnalysisActualDTO> advertiserTrendAnalysisActualDataGraphList) {
		this.advertiserTrendAnalysisActualDataGraphList = advertiserTrendAnalysisActualDataGraphList;
	}



	public void setAdvertiserTrendAnalysisForcastlDatarList(
			List<AdvertiserTrendAnalysisForcastDTO> advertiserTrendAnalysisForcastlDatarList) {
		this.advertiserTrendAnalysisForcastlDatarList = advertiserTrendAnalysisForcastlDatarList;
	}



	public List<AdvertiserTrendAnalysisForcastDTO> getAdvertiserTrendAnalysisForcastlDatarList() {
		return advertiserTrendAnalysisForcastlDatarList;
	}


	public void setSession(Map<String, Object> session) {
		this.session = session;
		}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setPerformanceSummaryHeaderDataList(
			List<AdvertiserPerformanceSummaryHeaderDTO> performanceSummaryHeaderDataList) {
		this.performanceSummaryHeaderDataList = performanceSummaryHeaderDataList;
	}

	public List<AdvertiserPerformanceSummaryHeaderDTO> getPerformanceSummaryHeaderDataList() {
		return performanceSummaryHeaderDataList;
	}

	public void setTopPerformerLineItemList(List<AdvertiserPerformerDTO> topPerformerLineItemList) {
		this.topPerformerLineItemList = topPerformerLineItemList;
	}

	public List<AdvertiserPerformerDTO> getTopPerformerLineItemList() {
		return topPerformerLineItemList;
	}

	public void setTopNonPerformerLineItemList(
			List<AdvertiserPerformerDTO> topNonPerformerLineItemList) {
		this.topNonPerformerLineItemList = topNonPerformerLineItemList;
	}

	public List<AdvertiserPerformerDTO> getTopNonPerformerLineItemList() {
		return topNonPerformerLineItemList;
	}



	public List<AgencyDTO> getAgencies() {
		return agencies;
	}



	public void setAgencies(List<AgencyDTO> agencies) {
		this.agencies = agencies;
	}



	public List<AdvertiserDTO> getAdvertisers() {
		return advertisers;
	}



	public void setAdvertisers(List<AdvertiserDTO> advertisers) {
		this.advertisers = advertisers;
	}



	public List<OrderDTO> getOrders() {
		return orders;
	}



	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}



	public List<LineItemDTO> getLineitems() {
		return lineitems;
	}



	public void setLineitems(List<LineItemDTO> lineitems) {
		this.lineitems = lineitems;
	}



	


	public int getPerformanceMetricsTotal() {
		return performanceMetricsTotal;
	}



	public void setPerformanceMetricsTotal(int performanceMetricsTotal) {
		this.performanceMetricsTotal = performanceMetricsTotal;
	}


	public void setAdvertiserReallocationHeaderObj(
			AdvertiserReallocationHeaderDTO advertiserReallocationHeaderObj) {
		this.advertiserReallocationHeaderObj = advertiserReallocationHeaderObj;
	}



	public AdvertiserReallocationHeaderDTO getAdvertiserReallocationHeaderObj() {
		return advertiserReallocationHeaderObj;
	}



	public void setAdvertiserReallocationDataList(
			List<AdvertiserReallocationTableDTO> advertiserReallocationDataList) {
		this.advertiserReallocationDataList = advertiserReallocationDataList;
	}



	public List<AdvertiserReallocationTableDTO> getAdvertiserReallocationDataList() {
		return advertiserReallocationDataList;
	}



	public void setSiteDropDownList(List<PropertyDropDownDTO> siteDropDownList) {
		this.siteDropDownList = siteDropDownList;
	}



	public List<PropertyDropDownDTO> getSiteDropDownList() {
		return siteDropDownList;
	}



	


	public List<AdvertiserPerformerDTO> getAdvertiserTotalDataListCurrent() {
		return advertiserTotalDataListCurrent;
	}



	public void setAdvertiserTotalDataListCurrent(
			List<AdvertiserPerformerDTO> advertiserTotalDataListCurrent) {
		this.advertiserTotalDataListCurrent = advertiserTotalDataListCurrent;
	}



	public List<AdvertiserPerformerDTO> getAdvertiserTotalDataListCompare() {
		return advertiserTotalDataListCompare;
	}



	public void setAdvertiserTotalDataListCompare(
			List<AdvertiserPerformerDTO> advertiserTotalDataListCompare) {
		this.advertiserTotalDataListCompare = advertiserTotalDataListCompare;
	}



	public List<AdvertiserPerformerDTO> getAdvertiserTotalDataListMTD() {
		return advertiserTotalDataListMTD;
	}



	public void setAdvertiserTotalDataListMTD(
			List<AdvertiserPerformerDTO> advertiserTotalDataListMTD) {
		this.advertiserTotalDataListMTD = advertiserTotalDataListMTD;
	}



	public List<AdvertiserPerformerDTO> getDeliveryIndicatorDataList() {
		return deliveryIndicatorDataList;
	}



	public void setDeliveryIndicatorDataList(
			List<AdvertiserPerformerDTO> deliveryIndicatorDataList) {
		this.deliveryIndicatorDataList = deliveryIndicatorDataList;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}



	public String getErrorStatus() {
		return errorStatus;
	}



	public void setForcastLineItemDTO(ForcastLineItemDTO forcastLineItemDTO) {
		this.forcastLineItemDTO = forcastLineItemDTO;
	}



	public ForcastLineItemDTO getForcastLineItemDTO() {
		return forcastLineItemDTO;
	}
	
	@Override
	public UserDetailsDTO getModel() {
		return userDetailsDTO;
	}


	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}


	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	
   
}
