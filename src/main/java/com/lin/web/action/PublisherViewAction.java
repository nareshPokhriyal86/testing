package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherInventoryRevenueObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.PublisherSummaryObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.ChannelPerformancePopUpDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.PublisherReportHeaderDTO;
import com.lin.web.dto.PublisherTrendAnalysisHeaderDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.DateUtil;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

public class PublisherViewAction implements ModelDriven<UserDetailsDTO>, ServletRequestAware, SessionAware{
	private static final Logger log = Logger.getLogger(AdvertiserViewAction.class.getName());
	
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private Map session;
	private SessionObjectDTO sessionDTO;
	
	private List<CommonDTO> timePeriodList;
	private List<PublisherChannelObj> channelPerformanceList;
	private List<PublisherPropertiesObj> performanceByPropertyList;
	private List<SellThroughDataObj> sellThroughDataList;
	private List<PublisherChannelObj> reallocationDataList;
	private List<PublisherChannelObj> actualPublisherDataList;
	private int channelPerformanceTotal;
	private int performanceByPropertyTotal;
	private int sellThroughDataTotal;
	private List<String> publisherList;
	private PublisherTrendAnalysisHeaderDTO publisherTrendAnalysisHeaderDTO;
	private List<PublisherTrendAnalysisHeaderDTO> publisherTrendAnalysisHeaderList;
	private Map<String,String> publisherMap; 	
    private List<PublisherInventoryRevenueObj> publisherInventoryRevenurList;
    private List<PublisherSummaryObj> publisherSummaryList;
    private List<PublisherPropertiesObj> allPerformanceByPropertyListCurrent;
    private Map<String,List<PublisherPropertiesObj>> allPerformanceByPropertyListCurrentMap;
    private List<PublisherPropertiesObj> allPerformanceByPropertyListCompare;
    private Map<String,List<PublisherPropertiesObj>> allPerformanceByPropertyListCompareMap;
    private Map<String,String> headerMap;
	private HttpServletRequest request;
	private ChannelPerformancePopUpDTO channelPopUpDTOObj;
	private PopUpDTO popUpDTO;
	private PublisherChannelObj channelPerformanceTotalObj;
	
	private List<CommonDTO> allPublishersList;
	private List<String> channelsNameList;
	private double aggrSellThroughRate;
	private PublisherPropertiesObj performanceByPropertyTotalObj;
	private List<PropertyDropDownDTO> propertyDropDownList;
	private List<String> siteDropDownList;
	

    private Map<String,List<AdvertiserPerformerDTO>>publisherCampainTotalDataListCurrentMap;
    private Map<String,List<AdvertiserPerformerDTO>> publisherCampainTotalDataListCompareMap;
    private Map<String,List<AdvertiserPerformerDTO>> publisherCampainTotalDataListMTDMap;
    private List<AdvertiserPerformerDTO> publisherCampainDeliveryIndicatorDataList;
    
    private InputStream inputStream;
	
	private List<ForcastLineItemDTO> forcastLineItemDTOList;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;

	public String execute(){
    	log.info("PublisherViewAction action executes..");
    	IUserService userService = new UserService();
    	try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[0]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[0]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return Action.SUCCESS;
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in PublisherViewAction : "+e.getMessage());
    		e.printStackTrace();
		}
    	return "unAuthorisedAccess";
    }

	public String publisherRelocationData(){
		
		return Action.SUCCESS;
	}
	
	
	public String changeCallPriority(){
		return Action.SUCCESS;
	}
	
	public String relocationDataTableSaveOrUpdate(){
		return Action.SUCCESS;
	}
	
	
		
	
	public String getTimePeriod(){		
		timePeriodList= new ArrayList<CommonDTO>();
		SimpleDateFormat format = new SimpleDateFormat("MMM dd");
		
		Date today = new Date();
		Date thisweekStartOn = new Date(today.getTime() - 7 * 24 * 3600 * 1000 );
		Date lastweekStartOn = new Date(thisweekStartOn.getTime() - 7 * 24 * 3600 * 1000 );
		Date secondLastWeekStartOn =  new Date(lastweekStartOn.getTime() - 7 * 24 * 3600 * 1000 );
		
		Date secondLastWeekStartOnPlusOne =  new Date(lastweekStartOn.getTime() - 6 * 24 * 3600 * 1000 );
		
		new Date(secondLastWeekStartOn.getTime() - 7 * 24 * 3600 * 1000 );
		
		Date thirdLastWeekStartOnPlusOne =  new Date(secondLastWeekStartOn.getTime() - 6 * 24 * 3600 * 1000 );
		
		format.format(thisweekStartOn);
		
		timePeriodList.add(new CommonDTO("ThisWeek", "This Week"));
		timePeriodList.add(new CommonDTO("LastWeek", "Last Week"));
		timePeriodList.add(new CommonDTO("ThirdWeek", format.format(secondLastWeekStartOnPlusOne) +" "+format.format(lastweekStartOn)));
		timePeriodList.add(new CommonDTO("ForthWeek", format.format(thirdLastWeekStartOnPlusOne)+" "+format.format(secondLastWeekStartOn)));
		timePeriodList.add(new CommonDTO("ThisMonth", "This Month"));
		timePeriodList.add(new CommonDTO("LastMonth", "Last Month"));
		
		return Action.SUCCESS;
	}
	
	
	public String loadPerformanceByProperty(){
		log.info("inside loadPerformanceByProperty of PublisherViewAction class");
		try{
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");
		String channel=request.getParameter("channel");
		String compareStartDate=request.getParameter("compareStartDate");
		String compareEndDate=request.getParameter("compareEndDate");
		String counter=request.getParameter("counter");		
		String property=request.getParameter("property");
		String publisher = request.getParameter("selectedPublisher");
		
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		
		if(lowerDate != null && upperDate != null) {
			if(property !=null){
				log.info("Loading popup data for property :"+property);
				popUpDTO=service.getPropertyPopup(lowerDate, upperDate, property);
			}else{
				log.info("Loading properties by performance data ");
				if(channel!= null){
					performanceByPropertyList=service.getPerformanceByPropertyList(lowerDate, upperDate, compareStartDate, compareEndDate,channel,publisher);
				}
				else if(counter==null){
					performanceByPropertyList=service.getPerformanceByPropertyList(lowerDate, upperDate);
				}
				else{
					performanceByPropertyList=service.getPerformanceByPropertyList(Integer.parseInt(counter), lowerDate, upperDate);
				}		
				performanceByPropertyTotal = performanceByPropertyList.size();
				
				double eCPMTotal = 0.0;
				double lastECPMTotal = 0.0;
				double CHGTotal = 0.0;
				double percentageCHGTotal = 0.0;
				double impressionsDeliveredTotal = 0;
				long clicksTotal = 0;
				double payoutTotal = 0.0;
				long lastImpressionsDeliveredTotal = 0;
				double lastPayoutTotal = 0.0;
				
				for(PublisherPropertiesObj obj : performanceByPropertyList){
					//eCPMTotal = eCPMTotal + obj.geteCPM();
					//CHGTotal = CHGTotal + obj.getCHG();
					//percentageCHGTotal = percentageCHGTotal + obj.getPercentageCHG();
					impressionsDeliveredTotal = impressionsDeliveredTotal + obj.getImpressionsDelivered();
					clicksTotal = clicksTotal + obj.getClicks();
					payoutTotal = payoutTotal + obj.getPayout();
					lastImpressionsDeliveredTotal = lastImpressionsDeliveredTotal + obj.getLastImpressionsDelivered();
					lastPayoutTotal = lastPayoutTotal + obj.getLastPayout();
				}
				eCPMTotal = (payoutTotal / impressionsDeliveredTotal) * 1000;
				lastECPMTotal = (lastPayoutTotal / lastImpressionsDeliveredTotal) * 1000;
				
				CHGTotal = eCPMTotal - lastECPMTotal;
				percentageCHGTotal = (CHGTotal / lastECPMTotal) * 100;
				
				performanceByPropertyTotalObj = new PublisherPropertiesObj();
				performanceByPropertyTotalObj.seteCPM(eCPMTotal);
				performanceByPropertyTotalObj.setCHG(CHGTotal);
				performanceByPropertyTotalObj.setPercentageCHG(percentageCHGTotal);
				performanceByPropertyTotalObj.setImpressionsDelivered(impressionsDeliveredTotal);
				performanceByPropertyTotalObj.setClicks(clicksTotal);
				performanceByPropertyTotalObj.setPayout(payoutTotal);
				
				StringBuilder sbStr = new StringBuilder();
				sbStr.append("[['State','Property', 'CTR(%)']");
				for (PublisherPropertiesObj object : performanceByPropertyList) {
					double clks = object.getClicks();
					double imprs = object.getImpressionsDelivered();
					double ctr = (clks / imprs) * 100;
					ctr = trimToFourDecimalPlace(ctr);
					sbStr.append(",[");
					sbStr.append("'"+object.getStateName()+"',"+"'" + object.getStationName() + "'," + ctr);
					sbStr.append("]");
				}
				sbStr.append("]");
				publisherMap = new HashMap<String, String>();
				publisherMap.put("datastr", sbStr.toString());
			}
		}
		}catch(Exception e){
    		log.severe("exception in loadPerformanceByProperty of PublisherViewAction class"+e.getMessage());
    		e.printStackTrace();
    	}
		return Action.SUCCESS;
	}
	
	
	public String loadSellThroughData(){    	
		log.info("within loadSellThroughData of PublisherViewAction class");
		try{
		Date currentDate=new Date();
		String selectedPublisherId=request.getParameter("selectedPublisherId");
		String lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,0, "yyyy-MM-dd");
		String upperDate=DateUtil.getModifiedDateStringByDays(currentDate,7, "yyyy-MM-dd");
		log.info("SELL THROUGH ACTION ="+lowerDate+" "+upperDate);
		
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
		CompanyObj publisherCompany = userDetailsDAO.getCompanyById(StringUtil.getLongValue(selectedPublisherId), publisherList);
		if(publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			sellThroughDataList = service.getSellThroughDataList(lowerDate, upperDate, publisherCompany.getCompanyName().trim(), publisherCompany.getId(), sessionDTO.getUserId());
		}
		
		sellThroughDataTotal = sellThroughDataList.size();
		double reservedImpTotal = 0;
		double forcastedImpTotal = 0;
		for(SellThroughDataObj obj: sellThroughDataList){
			reservedImpTotal = reservedImpTotal + Integer.parseInt(obj.getReservedImpressions());
			forcastedImpTotal = forcastedImpTotal + Integer.parseInt(obj.getForecastedImpressions());
		}
		
		aggrSellThroughRate = (reservedImpTotal / forcastedImpTotal) * 100;
		/*if(property != null){
			popUpDTO=service.getSellThroughPopUP(lowerDate, upperDate, property);
			log.info(" returing popup data for sell through rate: property:"+property);
		}*/
		}catch(Exception e){
    		log.severe("exception in loadSellThroughData of PublisherViewAction class : "+e.getMessage());
    		e.printStackTrace();
    	}
		
		return Action.SUCCESS;
	}
	
	public String loadPublisherData(){
		log.info("inside loadPublisherData of PublisherViewAction class");
		try{
		String publisherName=request.getParameter("publisherName");
		log.info("publisherName:"+publisherName);
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		if(publisherName !=null){
			channelPerformanceList=service.getPublisherDataList(publisherName);
			log.info("Load publisher details with publisherName:"+publisherName+" : size:"+channelPerformanceList.size());
		}else{
			log.info("Load all publisher name..");
			publisherList=service.getPublishers();
			log.info("Loaded all publisher name..:"+publisherList);
		}
		}catch(Exception e){
    		log.severe("exception in loadPublisherData of PublisherViewAction class"+e.getMessage());
    		e.printStackTrace();
    	}
		return Action.SUCCESS;
	}
	
	
	public String loadChannels(){
		try {
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
				String [] dateArrayU=upperDate.split("-");
				upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
				upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2]));						
			}else{
				Date currentDate=new Date();
				lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
				upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
			}
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			
			String publisherName=request.getParameter("publisherName");
			String channelName=request.getParameter("channelName");
			log.info("loadChannels by publisherName:"+publisherName+" or by channelName:"+channelName);
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			if(publisherName !=null){
				channelPerformanceList=service.loadChannelsByPublisher(publisherName);
			}else if(channelName !=null){
				channelPerformanceList=service.loadChannelsByName(channelName);
			}else{
				log.info("publisherName or channelName null..");
			}
			actualPublisherDataList=service.loadActualPublisherData(lowerDate, upperDate, channelName);
			headerMap=createAllLineGraph(actualPublisherDataList);	
		}
		catch (Exception e) {
			log.severe("exception in loadChannels of PublisherViewAction"+e.getMessage());
    		e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
    public String loadActualPublisherData(){
    	try{
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String publisher = request.getParameter("selectedPublisher");
			String channelName = request.getParameter("channelName");
			log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate+", publisher : "+publisher+" and channelName : "+channelName);
			
			//actualPublisherDataCalculation(lowerDate, upperDate, publisher, channelName);
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			actualPublisherDataList = 	service.loadActualPublisherData(lowerDate, upperDate, publisher, channelName);
    	}catch(Exception e){
    		log.severe("exception in loadActualPublisherData"+e.getMessage());
    		e.printStackTrace();
    	}
		 return Action.SUCCESS;
	}
    
    public void actualPublisherDataCalculation(String lowerDate, String upperDate, String publisher, String channelName) {
    	Map<String, List<PublisherChannelObj>> channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		 List<String> channelArrList = new ArrayList<String>();
			if(channelName!=null){
	 			
	 			String[] str = channelName.split(",");
	 			if(str!=null){
	 				for (String channel : str) {
	 					channelArrList.add(channel);
	 					
	 				}
	 			}
	 		}
		 channelsDataMap = MemcacheUtil.getAllFinalizeData(channelArrList, publisher);
		 if(channelsDataMap.isEmpty() && channelsDataMap.size()<=0){
			 loadOneYearTrendsAnalysisData(publisher);
			 channelsDataMap = MemcacheUtil.getAllFinalizeData(channelArrList, publisher);
		 }
		 if(channelsDataMap!=null && !channelsDataMap.isEmpty() && channelsDataMap.size()>=0){
			 Iterator<String> iterator = channelArrList.iterator();
			 actualPublisherDataList =  new ArrayList<PublisherChannelObj>();
			 while(iterator.hasNext()){
				 PublisherChannelObj obj = new PublisherChannelObj();
				 String channel = iterator.next();
				 List<PublisherChannelObj> tempList = channelsDataMap.get(channel);
				 List<PublisherChannelObj> tempList2 = new ArrayList<PublisherChannelObj>();
				 if(tempList!=null && tempList.size()>=0){
				 Iterator<PublisherChannelObj> iterator2 = tempList.iterator();
				 while(iterator2.hasNext()){
					 obj = iterator2.next();
					 if(obj!=null){
					 long date = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd", "yyyyMMdd"));
					 long uDate = StringUtil.getLongValue(DateUtil.getFormatedDate(upperDate, "yyyy-MM-dd", "yyyyMMdd"));
	 				 long lDate = StringUtil.getLongValue(DateUtil.getFormatedDate(lowerDate, "yyyy-MM-dd", "yyyyMMdd"));
					 if(date>=lDate && date<=uDate){
						 tempList2.add(obj); 
					 }
				 }
				 }
				 actualPublisherDataList.addAll( tempList2);
				 log.info("actualPublisherDataList size ****************************************** : "+actualPublisherDataList.size());
				 
			 }
				 }
			 lowerDate = lowerDate+" 00:00:00";
			 upperDate = upperDate+" 00:00:00";
			 log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
	    				 
		 }
    }
    
    public String actualLineGraphPublisher() { 
		try{
			 log.info("actualLineGraphPublisher executes ......");
			 String lowerDate=request.getParameter("startDate");
			 String upperDate=request.getParameter("endDate");	
			 String publisher = request.getParameter("selectedPublisher");
			 String channelName = request.getParameter("allChannelName");
			 
			 log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate+" and publisher : "+publisher+" and channelName : "+channelName);
			 new ArrayList<PublisherChannelObj>();
			 ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			 headerMap = service.processPublisherLineChartData(lowerDate, upperDate, publisher, channelName);
			// headerMap=createAllLineGraph(actualDataList);	
			/* Map<String, List<PublisherChannelObj>> channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
			 List<String> channelArrList = new ArrayList<String>();
		     List<PublisherChannelObj> actualDataList = new ArrayList<PublisherChannelObj>();
		     channelsDataMap = MemcacheUtil.getAllFinalizeData(channelArrList, publisher);
				if(channelName!=null){
		 			
		 			String[] str = channelName.split(",");
		 			if(str!=null){
		 				for (String channel : str) {
		 					channelArrList.add(channel);
		 				
		 					
		 				}
		 			}
		 			Collections.sort(channelArrList);	
		 		}
			 if(channelsDataMap == null || channelsDataMap.isEmpty() || channelsDataMap.size()<=0){
				 loadOneYearTrendsAnalysisData(publisher);
				 channelsDataMap = MemcacheUtil.getAllFinalizeData(channelArrList, publisher);
			 }
			 if(channelsDataMap != null && !channelsDataMap.isEmpty() && channelsDataMap.size()>=0){
				 Iterator<String> iterator = channelArrList.iterator();
				 while(iterator.hasNext()){
					 PublisherChannelObj obj = new PublisherChannelObj();
					 String channel = iterator.next();
					 List<PublisherChannelObj> tempList = channelsDataMap.get(channel);
					 List<PublisherChannelObj> tempList2 = new ArrayList<PublisherChannelObj>();
					 log.info("tempList : "+tempList);
					 log.info("tempList.size() : "+tempList.size());
					 Iterator<PublisherChannelObj> iterator2 = tempList.iterator();
					 while(iterator2.hasNext()){
						 obj = iterator2.next();
						 long date = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd", "yyyyMMdd"));
						 long uDate = StringUtil.getLongValue(DateUtil.getFormatedDate(upperDate, "yyyy-MM-dd", "yyyyMMdd"));
		 				 long lDate = StringUtil.getLongValue(DateUtil.getFormatedDate(lowerDate, "yyyy-MM-dd", "yyyyMMdd"));
						 if(date>=lDate && date<=uDate){
							 tempList2.add(obj); 
						 }
					 }
					 actualDataList.addAll(tempList2);
				 }
				 if(channelArrList != null && channelArrList.size() > 0) { 
					 for(String channel : channelArrList){
						 List<PublisherChannelObj> tempList = channelsDataMap.get(channel);
						 if(tempList != null && tempList.size() > 0) {
							 for(PublisherChannelObj obj : tempList){
								 long date = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd", "yyyyMMdd"));
								 long uDate = StringUtil.getLongValue(DateUtil.getFormatedDate(upperDate, "yyyy-MM-dd", "yyyyMMdd"));
				 				 long lDate = StringUtil.getLongValue(DateUtil.getFormatedDate(lowerDate, "yyyy-MM-dd", "yyyyMMdd"));
								 if(date>=lDate && date<=uDate){
									 actualDataList.add(obj); 
								 }
							 }
						 }
					 }
				 }
				 ActualPublisherComparatorDate comparatorDate = new ActualPublisherComparatorDate();
				 Collections.sort(actualDataList,comparatorDate );
				 ActualPublisherComparatorChannelName comparatorChannelName = new ActualPublisherComparatorChannelName();
				 Collections.sort(actualDataList,comparatorChannelName );*/
		    	// ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		    	// actualPublisherDataList=service.loadActualPublisherData(lowerDate,upperDate,publisher,channelName);
		        
				
			 
		}catch(Exception e){
			log.severe("Exception in actualLineGraphPublisher  : " + e.getMessage() );
			e.printStackTrace();
		}
		 return Action.SUCCESS;
    }
		 
 
    public String channelPerformancePopup(){
    	log.info("inside channelPerformancePopup of PublisherViewAction");
    	try{
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");
		String publisher = request.getParameter("selectedPublisher");
		
		log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		Date currentDate=new Date();
		if(lowerDate ==null && upperDate==null){
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		
		String channelName=request.getParameter("channelName");		
        ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
        if(channelName !=null){
        	popUpDTO=service.getChannelPerformancePopUP(lowerDate, upperDate, channelName, publisher);
        }else{
        	log.info("channelName is null");
        }
    	}catch(Exception e){
    		log.severe("exception in channelPerformancePopup of PublisherViewAction class"+e.getMessage());
    		e.printStackTrace();
    	}
        
    	return Action.SUCCESS;
    }
    
   /* public String loadReallocationPublisherData(){
    	log.info("inside loadReallocationPublisherData action");
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String publisherName=request.getParameter("publisherName");
		log.info("Request param: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
		if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
			String [] dateArrayU=upperDate.split("-");
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2]));						
		}else{
			Date currentDate=new Date();
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		
		log.info("lowerDate:"+lowerDate+" and upperDate:"+upperDate);
        ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
        reallocationDataList=service.loadReallocationPublisherData(lowerDate,upperDate,publisherName);
        //headerMap=createReAllocationGraph(service.loadReallocationGraphPublisherData(lowerDate, upperDate, publisherName));
		log.info("reallocationDataList size: "+reallocationDataList.size());
		return Action.SUCCESS;
	}*/
    
  
    
    public  Map<String,String> createAllLineGraph(List<PublisherChannelObj> publisherChannelDataList) throws Exception{
    	Map<String,String> graphMap = new HashMap<String, String>();
    	StringBuilder ecpmStr = new StringBuilder();
		StringBuilder fillRateStr = new StringBuilder();
		StringBuilder revenueStr = new StringBuilder();
		StringBuilder impressionStr = new StringBuilder();
		StringBuilder clicksStr = new StringBuilder();
		StringBuilder ctrStr = new StringBuilder();
		new ArrayList<StringBuilder>();
		List<String> channelArrList = new ArrayList<String>();
	    String formatedECPM = "";
	    String formatedFillRate = "";
		String formatedRevenue = "";
		String formatedImpression = "";
		String formatedClicks = "";
		String formatedCTR = "";
    	try{
			String channelList = request.getParameter("channelName");
			if(channelList!=null){
				
				String[] str = channelList.split(",");
				if(str!=null && str.length > 0){
					for (String channel : str) {
						channelArrList.add(channel);
					}
				}
				Collections.sort(channelArrList);	
			}
			if(channelArrList!=null&& channelArrList.size() > 0){
				
			if (publisherChannelDataList != null && publisherChannelDataList.size() > 0) {
						int flagFirst = 0 ;
						int channelsCount=0;
						int iteration=0;
						int count3=0;
						int notused = 0;
						int rowCount = 0;
	                	String date= null;
				    	StringBuilder struStr = new StringBuilder();		
				    	String[] arrSChannel = new String[channelArrList.size()];
						fillRateStr.append("[['Date' ");			    	
						struStr.append("[['Date' ");
						revenueStr.append("[['Date' ");			    	
						impressionStr.append("[['Date' ");
						clicksStr.append("[['Date' ");			    	
						ctrStr.append("[['Date' ");
						int z=0;
						for (String str : channelArrList) {
							struStr.append(", '"+str+"'");
							fillRateStr.append(", '"+str+"'");
							revenueStr.append(", '"+str+"'");
							impressionStr.append(", '"+str+"'");
							clicksStr.append(", '"+str+"'");
							ctrStr.append(", '"+str+"'");
							arrSChannel[z]= str.toString();
							z++;
						}	
						struStr.append("]");
						fillRateStr.append("]");
						revenueStr.append("]");
						impressionStr.append("]");
						clicksStr.append("]");
						ctrStr.append("]");
						channelsCount = channelArrList.size();
						
						for (PublisherChannelObj object : publisherChannelDataList) {
							
							rowCount++;						
							if (count3 >= channelsCount ){
									count3 = 0;
							}
							notused = 0;
							formatedECPM = String.valueOf(object.geteCPM())+"00";
							formatedECPM = formatedECPM.substring(0,formatedECPM.lastIndexOf(".")+3);
							formatedFillRate = String.valueOf(object.getFillRate())+"0000";
							formatedFillRate = formatedFillRate.substring(0,formatedFillRate.lastIndexOf(".")+5);
							formatedRevenue = String.valueOf(object.getRevenue())+"00";
							formatedRevenue = formatedRevenue.substring(0,formatedRevenue.lastIndexOf(".")+3);
							formatedImpression = String.valueOf(object.getImpressionsDelivered());
							formatedClicks = String.valueOf(object.getClicks());
							formatedCTR = String.valueOf(object.getCTR())+"0000";
							formatedCTR = formatedCTR.substring(0,formatedCTR.lastIndexOf(".")+5);
							
		                     for (iteration =count3 ; iteration < channelsCount ; iteration++ ){
		                      if (arrSChannel[iteration].equalsIgnoreCase(object.getChannelName()))
									{
										if ( (!object.getDate().equalsIgnoreCase(date)) && iteration == 0 )
										{// first iteration
											if (flagFirst != 0){
												struStr.append("]");
												fillRateStr.append("]");
												revenueStr.append("]");
												impressionStr.append("]");
												clicksStr.append("]");
												ctrStr.append("]");
											}
											flagFirst =1;							
											struStr.append(",[");
											fillRateStr.append(",[");
											revenueStr.append(",[");
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											struStr.append("'" + subStr + "'," + formatedECPM);
											fillRateStr.append("'" + subStr + "'," + formatedFillRate);
											revenueStr.append("'" + subStr + "'," + formatedRevenue);
											impressionStr.append("'" + subStr + "'," + formatedImpression);
											clicksStr.append("'" + subStr + "'," + formatedClicks);
											ctrStr.append("'" + subStr + "'," + formatedCTR);
											date = object.getDate();
											
										}
										else if ( (!object.getDate().equalsIgnoreCase(date)) && iteration == channelsCount-1  )
										{ // last iteration
											struStr.append("," +formatedECPM);
											fillRateStr.append("," +formatedFillRate);
											revenueStr.append("," +formatedRevenue);
											impressionStr.append("," +formatedImpression);
											clicksStr.append("," +formatedClicks);
											ctrStr.append("," +formatedCTR);
											if (flagFirst != 0){
												struStr.append("]");
												fillRateStr.append("]");
												revenueStr.append("]");
												impressionStr.append("]");
												clicksStr.append("]");
												ctrStr.append("]");
											}
											flagFirst =1;							
											struStr.append(",[");
											fillRateStr.append(",[");
											revenueStr.append(",[");
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											struStr.append("'" + subStr + "'," + formatedECPM);
											fillRateStr.append("'" + subStr + "'," + formatedFillRate);
											revenueStr.append("'" + subStr + "'," + formatedRevenue);
											impressionStr.append("'" + subStr + "'," + formatedImpression);
											clicksStr.append("'" + subStr + "'," + formatedClicks);
											ctrStr.append("'" + subStr + "'," + formatedCTR);
											date = object.getDate();
										}
										else if (object.getDate().equalsIgnoreCase(date)){
											struStr.append("," +formatedECPM);
											fillRateStr.append("," +formatedFillRate);
											revenueStr.append("," +formatedRevenue);
											impressionStr.append("," +formatedImpression);
											clicksStr.append("," +formatedClicks);
											ctrStr.append("," +formatedCTR);
										}
										count3 = iteration+1;
										notused = 1;
											if (rowCount != publisherChannelDataList.size() ){
												break;
											}
									}else{
										if ( (!object.getDate().equalsIgnoreCase(date)) && iteration==0 )
										{
											if (flagFirst != 0){
												struStr.append("]");
												fillRateStr.append("]");
												revenueStr.append("]");
												impressionStr.append("]");
												clicksStr.append("]");
												ctrStr.append("]");
											}
											flagFirst =1;							
											struStr.append(",[");
											fillRateStr.append(",[");
											revenueStr.append(",[");
											impressionStr.append(",[");
											clicksStr.append(",[");
											ctrStr.append(",[");
											String str = object.getDate();
											String subStr = str.substring(8, 10);
											struStr.append("'" + subStr + "',0.0");
											fillRateStr.append("'" + subStr + "',0.0");
											revenueStr.append("'" + subStr + "',0.0");
											impressionStr.append("'" + subStr + "',0.0");
											clicksStr.append("'" + subStr + "',0.0");
											ctrStr.append("'" + subStr + "',0.0");
											date = object.getDate();
										}
										else if ( (!object.getDate().equalsIgnoreCase(date)) && iteration == channelsCount-1 )
										{
											struStr.append(",0.0");
											fillRateStr.append(",0.0");
											revenueStr.append(",0.0");
											impressionStr.append(",0.0");
											clicksStr.append(",0.0");
											ctrStr.append(",0.0");
											if (flagFirst != 0){
												struStr.append("]");
												fillRateStr.append("]");
												revenueStr.append("]");
												impressionStr.append("]");
												clicksStr.append("]");
												ctrStr.append("]");
											}
											flagFirst =1;	
											if (notused == 0 && arrSChannel[0].equalsIgnoreCase(object.getChannelName())){
												struStr.append(",[");
												fillRateStr.append(",[");
												revenueStr.append(",[");
												impressionStr.append(",[");
												clicksStr.append(",[");
												ctrStr.append(",[");
												String str = object.getDate();
												String subStr = str.substring(8, 10);
												struStr.append("'" + subStr + "'," + formatedECPM);
												fillRateStr.append("'" + subStr + "'," + formatedFillRate);
												revenueStr.append("'" + subStr + "'," + formatedRevenue);
												impressionStr.append("'" + subStr + "'," +formatedImpression);
												clicksStr.append("'" + subStr + "'," + formatedClicks);
												ctrStr.append("'" + subStr + "'," + formatedCTR);
												date = object.getDate();
												count3 = 1;
												notused = 1;
												break;
											}else{
																		
												struStr.append(",[");
												fillRateStr.append(",[");
												revenueStr.append(",[");
												impressionStr.append(",[");
												clicksStr.append(",[");
												ctrStr.append(",[");
												String str = object.getDate();
												String subStr = str.substring(8, 10);
												struStr.append("'" + subStr + "',0.0");
												fillRateStr.append("'" + subStr + "',0.0");
												revenueStr.append("'" + subStr + "',0.0");
												impressionStr.append("'" + subStr + "',0.0");
												clicksStr.append("'" + subStr + "',0.0");
												ctrStr.append("'" + subStr + "',0.0");
												date = object.getDate();
												iteration = 0;
											}
									}else {
											struStr.append(",0.0");
											fillRateStr.append(",0.0");
											revenueStr.append(",0.0");
											impressionStr.append(",0.0");
											clicksStr.append(",0.0");
											ctrStr.append(",0.0");
										}
									}
									
		                     	}
							}
						struStr.append("]]");
						fillRateStr.append("]]");
						revenueStr.append("]]");
						impressionStr.append("]]");
						clicksStr.append("]]");
						ctrStr.append("]]");
	
					    
						ecpmStr = struStr;
	        			} 
			            else {
						log.info("No chart data found, show default...");
	
						/*ecpmStr.append("[['Date',   'eCPM']");
						ecpmStr.append(",['31',131.0]");
						ecpmStr.append("]");
	
						fillRateStr.append("[['Date',   'FILL RATE(%)']");
						fillRateStr.append(",['31',131.0]");
						fillRateStr.append("]");
	
						revenueStr.append("[['Date',   'REVENUE']");
						revenueStr.append(",['31',131.0]");
						revenueStr.append("]");
	
						impressionStr.append("[['Date',   'Impressions']");
						impressionStr.append(",['31',131.0]");
						impressionStr.append("]");
	
						clicksStr.append("[['Date',  'Clicks']");
						clicksStr.append(",['31',5]");
						clicksStr.append("]");
	
						ctrStr.append("[['Date',   'CTR']");
						ctrStr.append(",['31',3.10]");
						ctrStr.append("]");*/
					}
			}
			graphMap.put("title", "title");
			graphMap.put("eCPM", ecpmStr.toString());
			graphMap.put("fillRate", fillRateStr.toString());
			graphMap.put("revenue", revenueStr.toString());
			graphMap.put("impression", impressionStr.toString());
			graphMap.put("click", clicksStr.toString());
			graphMap.put("ctr", ctrStr.toString());
    	}catch(Exception e){
    		log.severe("Exception in createAllLineGraph : "+e.getMessage());
    		e.printStackTrace();
    		throw e;
    	}
    	log.info("eCPM : "+ecpmStr.toString());
    	log.info("fillRate : "+fillRateStr.toString());
    	log.info("revenue : "+revenueStr.toString());
    	log.info("impression : "+impressionStr.toString());
    	log.info("click : "+clicksStr.toString());
    	log.info("ctr : "+ctrStr.toString());
    	return graphMap;
    }
    
    private Map<String,String> createReAllocationGraph(List<PublisherChannelObj> publisherList){
    	Map<String,String> graphMap=new HashMap<String, String>();
    	List<String> networkList=new ArrayList<String>();
    	StringBuilder ecpmStr = new StringBuilder();
        StringBuilder fillRateStr = new StringBuilder();
        
        ecpmStr.append("[['Date',");
        
        for(PublisherChannelObj object : publisherList){
        	if(!networkList.contains(object.getChannelName())){
        		networkList.add(object.getChannelName());
        		ecpmStr.append(" '"+object.getChannelName()+"']");
        	}
        }
        int networkSize=networkList.size();
        int count=0;
       
        for (int i=0;i< publisherList.size();i++) {
        	 while(count<networkSize){
             	ecpmStr.append(",[");
          
             	ecpmStr.append("]");
             	
            	count++;
        	 }
				PublisherChannelObj object=publisherList.get(i);
				String str = object.getDate();
				String subStr = str.substring(8, 10);
				if(i==0){
					ecpmStr.append("'"+subStr);
				}
				ecpmStr.append("',"+object.geteCPM());
		}
        	
       
        if(publisherList !=null && publisherList.size()>0){
        	
        	
			
			for (PublisherChannelObj object : publisherList) {				
				ecpmStr.append(",[");
				String str = object.getDate();
				String subStr = str.substring(8, 10);
				ecpmStr.append("'"+subStr+"',"+object.geteCPM());
				ecpmStr.append("]");
			}
			ecpmStr.append("]");
			
			fillRateStr.append("[['Date',   'FILL RATE(%)']");
			for (PublisherChannelObj object : publisherList) {
				fillRateStr.append(",[");
				String str = object.getDate();
				String subStr = str.substring(8, 10);
				fillRateStr.append("'"+subStr+"',"+object.getFillRate());
				fillRateStr.append("]");
			}
			fillRateStr.append("]");
        }
    	return graphMap;
    }
    
    public String loadTrendAnalysisHeaderData(){    	
    	log.info("inside loadTrendAnalysisHeaderData in publisherVierAction");
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String publisher = request.getParameter("selectedPublisher");
		String channelName = request.getParameter("channelName").trim();
		lowerDate = lowerDate+" 00:00:00";
		upperDate = upperDate+" 00:00:00";
//		publisherName = "Lin Media";
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		publisherTrendAnalysisHeaderList = service.loadTrendAnalysisHeaderData(lowerDate, upperDate, publisher, channelName);
		
	    return Action.SUCCESS;
    }
    
    public String loadInventoryRevenueHeaderData(){
    	log.info("inside loadInventoryRevenueHeaderData in publisherViewAction....");
    	try{
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");	
		String channelName = request.getParameter("channelName");
		String publisher = request.getParameter("selectedPublisher");

		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		publisherInventoryRevenurList = service.loadInventoryRevenueHeaderData(lowerDate, upperDate, publisher,channelName);
    	}catch (Exception e) {
    		log.severe("Exception in loadInventoryRevenueHeaderData in publisherViewAction");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadPublisherSummaryList(){
    	log.info("inside loadPublisherSummaryList in publisherViewAction class");
    	try{
	    	String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String channelName = request.getParameter("channelName");
			String publisher = request.getParameter("selectedPublisher");
			
			IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
			CompanyObj publisherCompany = userDetailsDAO.getCompanyByName(publisher, publisherList);
			if(publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				publisherSummaryList = service.loadPublisherSummaryData(lowerDate, upperDate, publisher, publisherCompany.getId(), sessionDTO.getUserId(), channelName);
			}
    	}catch (Exception e) {
    		log.severe("Exception in loadPublisherSummaryList in publisherViewAction");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadAllPerformanceByPropertyCurrent(){
    	log.info("inside loadAllPerformanceByProperty in publisherViewAction class");
    	try{
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");
		String publisher = request.getParameter("selectedPublisher");
		String channels = request.getParameter("allChannelName");
		String applyFlag = request.getParameter("defaultExecutionFlag");
		
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
		CompanyObj publisherCompany = userDetailsDAO.getCompanyByName(publisher, publisherList);
		if(lowerDate != null && upperDate != null && publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			allPerformanceByPropertyListCurrentMap = service.loadAllPerformanceByProperty(lowerDate, upperDate,publisher, publisherCompany.getId(), sessionDTO.getUserId(),channels,applyFlag);
		}
    	}catch (Exception e) {
    		log.severe("Exception in loadAllPerformanceByProperty in publisherViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadAllPerformanceByPropertyCompare(){
    	log.info("inside loadAllPerformanceByProperty in publisherViewAction class");
    	try{
    	String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");
		String publisher = request.getParameter("selectedPublisher");
		String channels = request.getParameter("allChannelName");
		String applyFlag = request.getParameter("defaultExecutionFlag");
		
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
		CompanyObj publisherCompany = userDetailsDAO.getCompanyByName(publisher, publisherList);
		if(lowerDate != null && upperDate != null && publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			allPerformanceByPropertyListCompareMap = service.loadAllPerformanceByProperty(lowerDate.trim(), upperDate.trim(),publisher, publisherCompany.getId(), sessionDTO.getUserId(),channels,applyFlag);
		}
    	}catch (Exception e) {
    		log.severe("Exception in loadAllPerformanceByProperty in publisherViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadInvAndRevFilterData(){
    	log.info("inside loadFilterData in publisherViewAction");
    	try {
    		channelsNameList = new ArrayList<String>();
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
	    	allPublishersList = service.getAllPublishersByPublisherIdAndUserId("", channelsNameList, sessionDTO.getUserId());
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadFilterData in publisherViewAction");
    		e.printStackTrace();
		}
    	return Action.SUCCESS;
    }
    
    public String loadInvAndRevFilterDataByPublisherName(){
    	log.info("inside loadFilterData in publisherViewAction");
    	try {
    		String publisherId = request.getParameter("publisherId");
    		channelsNameList = new ArrayList<String>();
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
	    	if(publisherId != null) {
	    		allPublishersList = service.getAllPublishersByPublisherIdAndUserId(publisherId, channelsNameList, sessionDTO.getUserId());
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadFilterData in publisherViewAction");
    		e.printStackTrace();
		}
    	return Action.SUCCESS;
    }
    
    private Double trimToFourDecimalPlace(Double val){
    	if(val==null){
    	return null;
    	}
    	val = (Math.floor(Math.round(val*10000))/10000);
    	return val;
    	}

    public String loadOneYearTrendsAnalysisData(){
    	String publisherName  = "";
    	String channelName = null;
    	try{
    		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	//List<String> publisherNameList = new ArrayList<String>();
	    	
	    	//publisherNameList = service.getAllPublishers("");
    		List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
	    	HashMap<String, List<PublisherChannelObj>> channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
	    	PublisherChannelObj channelObj;
	    	List<String> channelArrList = new ArrayList<String>();
	    	if(publisherList!=null && publisherList.size()>0){
	    		for (CompanyObj publisherCompany : publisherList) {
	    			if(publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyName() != null && publisherCompany.getDemandPartnerId() != null) {
		    			List<String> channelIdList = publisherCompany.getDemandPartnerId();
		        		publisherName = publisherCompany.getCompanyName().trim();
		        		channelName = service.getCommaSeperatedChannelsNameByChannelIdList(channelIdList);
		        		channelArrList = new ArrayList<String>() ;
		        		channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
		        		if(channelName!=null && !channelName.trim().equalsIgnoreCase("")){
		         			
		         			String[] str = channelName.split(",");
		         			if(str!=null){
		         				for (String channel : str) {
		         					channelArrList.add(channel);
		         					List<PublisherChannelObj> list = new ArrayList<PublisherChannelObj>();
		         					channelsDataMap.put(channel.trim(), list);
		         				}
		         			}
		         		}
		                    MemcacheUtil.flushOneYearData(channelArrList,publisherName);
		                	Date currentDate = new Date();
		                 	String upperDate=DateUtil.getModifiedDateStringByDays(currentDate,0, "yyyy-MM-dd");
		                 	String lowerDate = DateUtil.getModifiedDateByYears(currentDate,-1, "yyyy-MM-dd");
		                 	lowerDate = lowerDate+" 00:00:00";
		        			upperDate = upperDate+" 00:00:00";
		        			Iterator<String> iterator1 = channelArrList.iterator();
		        			while(iterator1.hasNext()){
		        				String channel = iterator1.next();
		        				List<PublisherChannelObj> list = new ArrayList<PublisherChannelObj>();
		        				channelsDataMap.put(channel, list);
		        			}
		                 	
		                 	actualPublisherDataList=service.loadActualPublisherData(lowerDate,upperDate,publisherName,channelName);
		                 	if(actualPublisherDataList!=null && actualPublisherDataList.size()>0){
		                 		Iterator<PublisherChannelObj> dataIterator = actualPublisherDataList.iterator();
		                 		while(dataIterator.hasNext()){
		                 			channelObj = dataIterator.next();
		                 			String key=channelObj.getChannelName();
		                 			if(channelsDataMap.containsKey(key) ){
		                 			   List<PublisherChannelObj> tempList = channelsDataMap.get(key);
		                 			   tempList.add(channelObj);
		                 			   channelsDataMap.put(key, tempList);
		                 			   
		                 			}else{
		                 				log.info("Key not found:"+key);
		                 			}
		                 		}
		                 	}
		                 	MemcacheUtil.loadAllFinalizeData(channelsDataMap,channelArrList,publisherName);
	    			}
	        	}
	    	}
    	
    	}catch(Exception e){
    		log.severe("Exception in loadOneYearTrendsAnalysisData :  "+ e.getMessage());
    		e.printStackTrace();
    	}
    	return Action.SUCCESS;
    }
    
    public String loadOneYearTrendsAnalysisData(String publisherName){
    	//PublisherChannels channels = new PublisherChannels();
    	//String publisherName  = request.getParameter("publisher");
    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    	HashMap<String, List<PublisherChannelObj>> channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
    	PublisherChannelObj channelObj;
    	List<String> channelArrList = new ArrayList<String>();
    	List<PublisherChannelObj> channelDataList = new ArrayList<PublisherChannelObj>();
    	try {
	    	String channelName = service.getCommaSeperatedChannelsName(publisherName);
	    	
	    	if(channelName!=null && !channelName.trim().equalsIgnoreCase("")){ 			
	 			String[] str = channelName.split(",");
	 			if(str!=null){
	 				for (String channel : str) {
	 					channelArrList.add(channel);
	 					channelsDataMap.put(channel, channelDataList);
	 				}
	 			}
	 		}
	    	channelsDataMap = MemcacheUtil.getAllFinalizeData(channelArrList,publisherName);
	    	
	    	if(channelsDataMap != null && channelsDataMap.isEmpty() && channelsDataMap.size()<=0){
	    		Iterator<String> iterator = channelArrList.iterator();
				while(iterator.hasNext()){
					String channel = iterator.next();
					List<PublisherChannelObj> list = new ArrayList<PublisherChannelObj>();
					channelsDataMap.put(channel.trim(), list);
				}
	        	 Date currentDate = new Date();
	         	String upperDate=DateUtil.getModifiedDateStringByDays(currentDate,0, "yyyy-MM-dd");
	         	String lowerDate = DateUtil.getModifiedDateByYears(currentDate,-1, "yyyy-MM-dd");
	         	/*lowerDate = lowerDate+" 00:00:00";
				upperDate = upperDate+" 00:00:00";*/
	         	actualPublisherDataList=service.loadActualPublisherData(lowerDate,upperDate,publisherName,channelName);
	         	if(actualPublisherDataList!=null && actualPublisherDataList.size()>0){
	         		Iterator<PublisherChannelObj> dataIterator = actualPublisherDataList.iterator();
	         		while(dataIterator.hasNext()){
	         			channelObj = dataIterator.next();
	         			if(channelsDataMap.containsKey(channelObj.getChannelName()) ){
	         				List<PublisherChannelObj> tempList = channelsDataMap.get(channelObj.getChannelName());
	         				tempList.add(channelObj);
	         				channelsDataMap.put(channelObj.getChannelName(), tempList);
	         				tempList=null;
	         			}
	         		}
	         	}
	         	MemcacheUtil.loadAllFinalizeData(channelsDataMap,channelArrList,publisherName);
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadOneYearTrendsAnalysisData in PublisherViewAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadPublisherPropertyDropDownList()
    {

    	log.info("Inside loadPublisherPropertyDropDownList in PublisherViewAction");
    	try {
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
    		String publisherId = request.getParameter("publisherName");
/*    		System.out.println("PUBLISHER NAME " +publisherName);
    		System.out.println("TERM "+request.getParameter("term"));
    		System.out.println("USER ID "+sessionDTO.getUserId());*/
    		long userId = sessionDTO.getUserId();
    	//	String agencyId = request.getParameter("agencyId");
    	//	String advertiserId = request.getParameter("advertiserId");
    	//	String orderId = request.getParameter("orderId");
    		String term = request.getParameter("term");
    		
    		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
    		propertyDropDownList = service.loadPublisherPropertyDropDownList(publisherId,userId,term);
			log.info("propertyDropDownList : "+propertyDropDownList.size());
	    }
		catch (Exception e) {
			log.severe("Exception in loadPublisherPropertyDropDownList in PublisherViewAction"+e.getMessage());
			e.printStackTrace();
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
    
    public String loadCampainTotalDataPublisherViewListCurrent(){
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
			
			if(lowerDate != null && upperDate != null) {
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				publisherCampainTotalDataListCurrentMap = service.loadCampainTotalDataPublisherViewList(lowerDate, upperDate,publisher,advertiser,agency,properties);
			}
    	}catch (Exception e) {
    		log.severe("Exception in loadAdvertiserTotalDataListCurrent in AdvertiserViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadCampainTotalDataPublisherViewListCompare(){
    	log.info("inside loadAdvertiserTotalDataListCompare in RichMediaAdvertiserViewAction class");
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
			
			if(lowerDate != null && upperDate != null) {
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				publisherCampainTotalDataListCompareMap = service.loadCampainTotalDataPublisherViewList(lowerDate, upperDate,publisher,advertiser,agency,properties);
			}
    	}catch (Exception e) {
    		log.severe("Exception in loadAdvertiserTotalDataListCompare in RichMediaAdvertiserViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadCampainTotalDataPublisherViewListMTD(){
    	log.info("inside loadAdvertiserTotalDataListMTD in RichMediaAdvertiserViewAction class");
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
		
		if(lowerDate != null && upperDate != null) {
			ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			publisherCampainTotalDataListMTDMap = service.loadCampainTotalDataPublisherViewList(lowerDate, upperDate,publisher,advertiser,agency,properties);
		}
    	}catch (Exception e) {
    		log.severe("Exception in loadAdvertiserTotalDataListMTD in RichMediaAdvertiserViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String loadCampainPerformanceDeliveryIndicatorData(){
    	log.info("inside loadDeliveryIndicatorData in RichMediaAdvertiserViewAction class");
    	try{
			String lowerDate=request.getParameter("startDate");
			String upperDate=request.getParameter("endDate");
			String advertiser=request.getParameter("advertiser");
			String agency=request.getParameter("agency");	
			String publisher = request.getParameter("publisherName");
			String properties = request.getParameter("properties");
			
			if(lowerDate != null && upperDate != null) {
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				publisherCampainDeliveryIndicatorDataList = service.loadCampainPerformanceDeliveryIndicatorData(lowerDate, upperDate, publisher, advertiser, agency, properties);
			}
    	}catch (Exception e) {
    		log.severe("Exception in loadDeliveryIndicatorData in RichMediaAdvertiserViewAction class");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    public String downloadPublisherReport(){
    	log.info("inside downloadPublisherReport in PublisherViewAction");
    	try{
			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String channelName = request.getParameter("channelName");
			String publisherName = request.getParameter("selectedPublisher");
			String compareStartDate=request.getParameter("compareStartDate");
			String compareEndDate=request.getParameter("compareEndDate");
			String applyFlag = request.getParameter("defaultExecutionFlag");
			
			log.info("startDate : "+startDate+", endDate : "+endDate+", channelName : "+channelName+", publisherName : "+publisherName+", compareStartDate : "+compareStartDate+", compareEndDate : "+compareEndDate+", applyFlag : "+applyFlag);
			
			if(startDate != null && endDate != null && channelName != null && publisherName != null) {
				IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
				sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
				List<CompanyObj> publisherList = userDetailsDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
				CompanyObj publisherCompany = userDetailsDAO.getCompanyByName(publisherName, publisherList);
				if(publisherCompany != null && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					/*String backendUrl = "";
					//backendUrl = LinMobileVariables.REDIRECT_URL;
					backendUrl = "http://localhost:8888";
					if(backendUrl.startsWith("http://")) {
						//backendUrl = backendUrl.replace("http://", "http://"+LinMobileConstants.BACKEND_NAME+".");
					}
					else {
						//backendUrl = LinMobileConstants.BACKEND_NAME+"." + backendUrl;
					}
					backendUrl = backendUrl + "/downloadPublisherReportFromBackend.lin?startDate="+startDate+"&endDate="+endDate+"&channelName="+channelName+"&publisherName="+publisherName+"&compareStartDate="+compareStartDate+"&compareEndDate="+compareEndDate+"&publisherId="+publisherCompany.getId()+"&userId="+sessionDTO.getUserId()+"&applyFlag="+applyFlag;
					backendUrl = backendUrl.replace(" ", "%20");
					log.info("URL : "+backendUrl);
					URL url = new URL(backendUrl);
				    URLConnection urlConnection = url.openConnection();
				    urlConnection.getInputStream();*/
					if(startDate != null && endDate != null && channelName != null && publisherName != null) {
						List<PublisherChannelObj> publisherReportByDateList = new  ArrayList<PublisherChannelObj>();
						String publisherId=publisherCompany.getId()+"";
						String userId=sessionDTO.getUserId()+"";
						ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
						List<PublisherSummaryObj> currentDateSummaryList = service.loadPublisherSummaryData(startDate, endDate, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
						List<PublisherSummaryObj> compareDateSummaryList = service.loadPublisherSummaryData(compareStartDate, compareEndDate, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
						Map<String, List<PublisherPropertiesObj>> allPerformanceByPropertyCurrentMap = service.loadAllPerformanceByProperty(startDate, endDate,publisherName, Long.valueOf(publisherId), Long.valueOf(userId),channelName,applyFlag);
						Map<String, List<PublisherPropertiesObj>> allPerformanceByPropertyCompareMap = service.loadAllPerformanceByProperty(compareStartDate, compareEndDate,publisherName, Long.valueOf(publisherId), Long.valueOf(userId),channelName,applyFlag);
						actualPublisherDataList = service.loadActualPublisherData(startDate, endDate, publisherName, channelName);
						if(actualPublisherDataList != null && actualPublisherDataList.size() > 0) {
							for (PublisherChannelObj publisherChannelObj : actualPublisherDataList) {
								publisherChannelObj.setFillRate(publisherChannelObj.getFillRate()/100);
								publisherChannelObj.setCTR(publisherChannelObj.getCTR()/100);
								publisherChannelObj.setReportDate(DateUtil.getRequiredFormatDate(publisherChannelObj.getDate(), "yyyy-MM-dd", "MM/dd/yyyy"));
								publisherReportByDateList.add(publisherChannelObj);
							}
						}
						//List<PublisherSummaryObj> mtDateSummaryList = service.loadPublisherSummaryData(null, null, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
						if((currentDateSummaryList != null && currentDateSummaryList .size() > 0) || (compareDateSummaryList != null && compareDateSummaryList .size() > 0) || (allPerformanceByPropertyCurrentMap != null && allPerformanceByPropertyCurrentMap .size() > 0) || (allPerformanceByPropertyCompareMap != null && allPerformanceByPropertyCompareMap .size() > 0) || (actualPublisherDataList != null && actualPublisherDataList.size() > 0)) {
							Map map = new HashMap();
							PublisherReportHeaderDTO publisherReportHeaderDTO = service.getHeaderData(channelName, currentDateSummaryList, compareDateSummaryList);
							publisherReportHeaderDTO.setPublisherName(publisherName);
							publisherReportHeaderDTO.setChannels(channelName.replaceAll(",", ", "));
							publisherReportHeaderDTO.setStartDate(DateUtil.getRequiredFormatDate(startDate, "yyyy-MM-dd", "MM/dd/yyyy"));
							publisherReportHeaderDTO.setEndDate(DateUtil.getRequiredFormatDate(endDate, "yyyy-MM-dd", "MM/dd/yyyy"));
							publisherReportHeaderDTO.setCompareStartDate(DateUtil.getRequiredFormatDate(compareStartDate, "yyyy-MM-dd", "MM/dd/yyyy"));
							publisherReportHeaderDTO.setCompareEndDate(DateUtil.getRequiredFormatDate(compareEndDate, "yyyy-MM-dd", "MM/dd/yyyy"));
							
							Map channelPerformanceMap = service.getChannelPerformanceData(channelName, currentDateSummaryList, compareDateSummaryList);
							
							Map performanceByPropertyMap = service.getPerformanceByPropertyData(channelName, allPerformanceByPropertyCurrentMap.get("allDataBySiteName"), allPerformanceByPropertyCompareMap.get("allDataBySiteName"));
							
							List<PublisherReportHeaderDTO> publisherReportHeaderDTOList = new ArrayList<PublisherReportHeaderDTO>();
							publisherReportHeaderDTOList.add(publisherReportHeaderDTO);
							map.put("PublisherReportHeaderDTO",publisherReportHeaderDTOList);
							map.putAll(channelPerformanceMap);
							map.putAll(performanceByPropertyMap);
							map.put("publisherReportByDate",publisherReportByDateList);
					    	
					    	ExcelReportGenerator erGen = new ExcelReportGenerator();
					        byte[] excelbytes = null;
					        try {
					  			excelbytes = erGen.advertiserReportGenerate("PublisherReport.xls",map);
					  			setInputStream(new ByteArrayInputStream(excelbytes));
					  		}catch (Exception e) {
					  			e.printStackTrace();
					  		}
						}
					}
				}
			}
    	}catch (SocketTimeoutException ste) {
		}
    	catch (Exception e) {
    		log.severe("Exception in downloadPublisherReport in PublisherViewAction");
    		e.printStackTrace();
		}
		return Action.SUCCESS;
    }
    
    
    public String downloadPublisherReportFromBackend(){
    	log.info("inside downloadPublisherReportFromBackend in PublisherViewAction");
    	/*try{
			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String channelName = request.getParameter("channelName");
			String publisherName = request.getParameter("publisherName");
			String compareStartDate=request.getParameter("compareStartDate");
			String compareEndDate=request.getParameter("compareEndDate");
			String publisherId=request.getParameter("publisherId");
			String userId=request.getParameter("userId");
			String applyFlag = request.getParameter("applyFlag");
			
			log.info("startDate : "+startDate+", endDate : "+endDate+", channelName : "+channelName+", publisherName : "+publisherName+", applyFlag : "+applyFlag+", compareStartDate : "+compareStartDate+", compareEndDate : "+compareEndDate+", publisherId : "+publisherId+", userId : "+userId);
			
			if(startDate != null && endDate != null && channelName != null && publisherName != null) {
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				List<PublisherSummaryObj> currentDateSummaryList = service.loadPublisherSummaryData(startDate, endDate, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
				List<PublisherSummaryObj> compareDateSummaryList = service.loadPublisherSummaryData(compareStartDate, compareEndDate, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
				Map<String, List<PublisherPropertiesObj>> allPerformanceByPropertyCurrentMap = service.loadAllPerformanceByProperty(startDate, endDate,publisherName, Long.valueOf(publisherId), Long.valueOf(userId),channelName,applyFlag);
				Map<String, List<PublisherPropertiesObj>> allPerformanceByPropertyCompareMap = service.loadAllPerformanceByProperty(compareStartDate, compareEndDate,publisherName, Long.valueOf(publisherId), Long.valueOf(userId),channelName,applyFlag);
				//List<PublisherSummaryObj> mtDateSummaryList = service.loadPublisherSummaryData(null, null, publisherName, Long.valueOf(publisherId), Long.valueOf(userId), channelName);
				if((currentDateSummaryList != null && currentDateSummaryList .size() > 0) || (compareDateSummaryList != null && compareDateSummaryList .size() > 0) || (allPerformanceByPropertyCurrentMap != null && allPerformanceByPropertyCurrentMap .size() > 0) || (allPerformanceByPropertyCompareMap != null && allPerformanceByPropertyCompareMap .size() > 0)) {
					Map map = new HashMap();
					PublisherReportHeaderDTO publisherReportHeaderDTO = service.getHeaderData(channelName, currentDateSummaryList, compareDateSummaryList);
					List<PublisherReportChannelPerformanceDTO> pubReportChannelPerformanceDTOList = service.getChannelPerformanceDate(channelName, currentDateSummaryList, compareDateSummaryList);
					
					List<PublisherReportHeaderDTO> publisherReportHeaderDTOList = new ArrayList<PublisherReportHeaderDTO>();
					publisherReportHeaderDTOList.add(publisherReportHeaderDTO);
					map.put("PublisherReportHeaderDTO",publisherReportHeaderDTOList);
			    	map.put("PublisherReportChannelPerformanceDTO",pubReportChannelPerformanceDTOList);
			    	
			    	ExcelReportGenerator erGen = new ExcelReportGenerator();
			        byte[] excelbytes = null;
			        try {
			  			excelbytes = erGen.advertiserReportGenerate("PublisherReport.xls",map);
			  			setInputStream(new ByteArrayInputStream(excelbytes));
			  		}catch (Exception e) {
			  			e.printStackTrace();
			  		}
				}
			}
    	}catch (Exception e) {
    		log.severe("Exception in downloadPublisherReportFromBackend in PublisherViewAction");
    		e.printStackTrace();
		}*/
		return Action.SUCCESS;
    }
    
    @Override
	public UserDetailsDTO getModel() {	
		return userDetailsDTO;
	}
    
	public List<CommonDTO> getTimePeriodList() {
		return timePeriodList;
	}

	public void setTimePeriodList(List<CommonDTO> timePeriodList) {
		this.timePeriodList = timePeriodList;
	}

	
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}



	public List<PublisherChannelObj> getChannelPerformanceList() {
		return channelPerformanceList;
	}



	public void setChannelPerformanceList(
			List<PublisherChannelObj> channelPerformanceList) {
		this.channelPerformanceList = channelPerformanceList;
	}



	public List<PublisherPropertiesObj> getPerformanceByPropertyList() {
		return performanceByPropertyList;
	}



	public void setPerformanceByPropertyList(
			List<PublisherPropertiesObj> performanceByPropertyList) {
		this.performanceByPropertyList = performanceByPropertyList;
	}



	public List<SellThroughDataObj> getSellThroughDataList() {
		return sellThroughDataList;
	}



	public void setSellThroughDataList(List<SellThroughDataObj> sellThroughDataList) {
		this.sellThroughDataList = sellThroughDataList;
	}



	public int getChannelPerformanceTotal() {
		return channelPerformanceTotal;
	}



	public void setChannelPerformanceTotal(int channelPerformanceTotal) {
		this.channelPerformanceTotal = channelPerformanceTotal;
	}



	public int getPerformanceByPropertyTotal() {
		return performanceByPropertyTotal;
	}



	public void setPerformanceByPropertyTotal(int performanceByPropertyTotal) {
		this.performanceByPropertyTotal = performanceByPropertyTotal;
	}



	public int getSellThroughDataTotal() {
		return sellThroughDataTotal;
	}



	public void setSellThroughDataTotal(int sellThroughDataTotal) {
		this.sellThroughDataTotal = sellThroughDataTotal;
	}

	
	public ChannelPerformancePopUpDTO getChannelPopUpDTOObj() {
		return channelPopUpDTOObj;
	}



	public void setChannelPopUpDTOObj(ChannelPerformancePopUpDTO channelPopUpDTOObj) {
		this.channelPopUpDTOObj = channelPopUpDTOObj;
	}



	public void setPublisherList(List<String> publisherList) {
		this.publisherList = publisherList;
	}



	public List<String> getPublisherList() {
		return publisherList;
	}



	public Map<String, String> getPublisherMap() {
		return publisherMap;
	}



	public void setPublisherMap(Map<String, String> publisherMap) {
		this.publisherMap = publisherMap;
	}

	


	public void setActualPublisherDataList(List<PublisherChannelObj> actualPublisherDataList) {
		this.actualPublisherDataList = actualPublisherDataList;
	}



	public List<PublisherChannelObj> getActualPublisherDataList() {
		return actualPublisherDataList;
	}
	
	public void setHeaderMap(Map<String,String> headerMap) {
		this.headerMap = headerMap;
	}
	

	public Map<String,String> getHeaderMap() {
		return headerMap;
	}



	public void setPopUpDTO(PopUpDTO popUpDTO) {
		this.popUpDTO = popUpDTO;
	}



	public PopUpDTO getPopUpDTO() {
		return popUpDTO;
	}
	
	public List<PublisherChannelObj> getReallocationDataList() {
		return reallocationDataList;
	}



	public void setReallocationDataList(
			List<PublisherChannelObj> reallocationDataList) {
		this.reallocationDataList = reallocationDataList;
	}



	public void setPublisherTrendAnalysisHeaderDTO(
			PublisherTrendAnalysisHeaderDTO publisherTrendAnalysisHeaderDTO) {
		this.publisherTrendAnalysisHeaderDTO = publisherTrendAnalysisHeaderDTO;
	}



	public PublisherTrendAnalysisHeaderDTO getPublisherTrendAnalysisHeaderDTO() {
		return publisherTrendAnalysisHeaderDTO;
	}



	public void setPublisherTrendAnalysisHeaderList(
			List<PublisherTrendAnalysisHeaderDTO> publisherTrendAnalysisHeaderList) {
		this.publisherTrendAnalysisHeaderList = publisherTrendAnalysisHeaderList;
	}



	public List<PublisherTrendAnalysisHeaderDTO> getPublisherTrendAnalysisHeaderList() {
		return publisherTrendAnalysisHeaderList;
	}


	public void setPublisherInventoryRevenurList(
			List<PublisherInventoryRevenueObj> publisherInventoryRevenurList) {
		this.publisherInventoryRevenurList = publisherInventoryRevenurList;
	}



	public List<PublisherInventoryRevenueObj> getPublisherInventoryRevenurList() {
		return publisherInventoryRevenurList;
	}



	public void setAllPublishersList(List<CommonDTO> allPublishersList) {
		this.allPublishersList = allPublishersList;
	}



	public List<CommonDTO> getAllPublishersList() {
		return allPublishersList;
	}



	public PublisherChannelObj getChannelPerformanceTotalObj() {
		return channelPerformanceTotalObj;
	}



	public void setChannelPerformanceTotalObj(
			PublisherChannelObj channelPerformanceTotalObj) {
		this.channelPerformanceTotalObj = channelPerformanceTotalObj;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
		}

	public Map<String, Object> getSession() {
		return session;
	}



	public double getAggrSellThroughRate() {
		return aggrSellThroughRate;
	}



	public void setAggrSellThroughRate(double aggrSellThroughRate) {
		this.aggrSellThroughRate = aggrSellThroughRate;
	}



	public PublisherPropertiesObj getPerformanceByPropertyTotalObj() {
		return performanceByPropertyTotalObj;
	}



	public void setPerformanceByPropertyTotalObj(
			PublisherPropertiesObj performanceByPropertyTotalObj) {
		this.performanceByPropertyTotalObj = performanceByPropertyTotalObj;
	}

	public void setChannelsNameList(List<String> channelsNameList) {
		this.channelsNameList = channelsNameList;
	}

	public List<String> getChannelsNameList() {
		return channelsNameList;
	}

	public List<PublisherSummaryObj> getPublisherSummaryList() {
		return publisherSummaryList;
	}

	public void setPublisherSummaryList(
			List<PublisherSummaryObj> publisherSummaryList) {
		this.publisherSummaryList = publisherSummaryList;
	}

	public List<PublisherPropertiesObj> getAllPerformanceByPropertyListCurrent() {
		return allPerformanceByPropertyListCurrent;
	}

	public void setAllPerformanceByPropertyListCurrent(
			List<PublisherPropertiesObj> allPerformanceByPropertyListCurrent) {
		this.allPerformanceByPropertyListCurrent = allPerformanceByPropertyListCurrent;
	}

	public List<PublisherPropertiesObj> getAllPerformanceByPropertyListCompare() {
		return allPerformanceByPropertyListCompare;
	}

	public void setAllPerformanceByPropertyListCompare(
			List<PublisherPropertiesObj> allPerformanceByPropertyListCompare) {
		this.allPerformanceByPropertyListCompare = allPerformanceByPropertyListCompare;
	}

	
	 public List<PropertyDropDownDTO> getPropertyDropDownList() {
			return propertyDropDownList;
		}

		public void setPropertyDropDownList(List<PropertyDropDownDTO> propertyDropDownList) {
			this.propertyDropDownList = propertyDropDownList;
		}
		
		public List<String> getSiteDropDownList() {
			return siteDropDownList;
		}

		public void setSiteDropDownList(List<String> siteDropDownList) {
			this.siteDropDownList = siteDropDownList;
		}
/*	public List<PublisherPropertiesObj> getAllPerformanceByPropertyList() {
		return allPerformanceByPropertyList;
	}

	public void setAllPerformanceByPropertyList(
			List<PublisherPropertiesObj> allPerformanceByPropertyList) {
		this.allPerformanceByPropertyList = allPerformanceByPropertyList;
	}*/

		public List<ForcastLineItemDTO> getForcastLineItemDTOList() {
			return forcastLineItemDTOList;
		}

		public void setForcastLineItemDTOList(
				List<ForcastLineItemDTO> forcastLineItemDTOList) {
			this.forcastLineItemDTOList = forcastLineItemDTOList;
		}
		
		public void setAllPerformanceByPropertyListCurrentMap(
				Map<String,List<PublisherPropertiesObj>> allPerformanceByPropertyListCurrentMap) {
			this.allPerformanceByPropertyListCurrentMap = allPerformanceByPropertyListCurrentMap;
		}

		public Map<String,List<PublisherPropertiesObj>> getAllPerformanceByPropertyListCurrentMap() {
			return allPerformanceByPropertyListCurrentMap;
		}

		public void setAllPerformanceByPropertyListCompareMap(
				Map<String,List<PublisherPropertiesObj>> allPerformanceByPropertyListCompareMap) {
			this.allPerformanceByPropertyListCompareMap = allPerformanceByPropertyListCompareMap;
		}

		public Map<String,List<PublisherPropertiesObj>> getAllPerformanceByPropertyListCompareMap() {
			return allPerformanceByPropertyListCompareMap;
		}

		public void setPublisherCampainDeliveryIndicatorDataList(
				List<AdvertiserPerformerDTO> publisherCampainDeliveryIndicatorDataList) {
			this.publisherCampainDeliveryIndicatorDataList = publisherCampainDeliveryIndicatorDataList;
		}

		public List<AdvertiserPerformerDTO> getPublisherCampainDeliveryIndicatorDataList() {
			return publisherCampainDeliveryIndicatorDataList;
		}

		public void setPublisherCampainTotalDataListCurrentMap(
				Map<String,List<AdvertiserPerformerDTO>> publisherCampainTotalDataListCurrentMap) {
			this.publisherCampainTotalDataListCurrentMap = publisherCampainTotalDataListCurrentMap;
		}

		public Map<String,List<AdvertiserPerformerDTO>> getPublisherCampainTotalDataListCurrentMap() {
			return publisherCampainTotalDataListCurrentMap;
		}

		public void setPublisherCampainTotalDataListCompareMap(
				Map<String,List<AdvertiserPerformerDTO>> publisherCampainTotalDataListCompareMap) {
			this.publisherCampainTotalDataListCompareMap = publisherCampainTotalDataListCompareMap;
		}

		public Map<String,List<AdvertiserPerformerDTO>> getPublisherCampainTotalDataListCompareMap() {
			return publisherCampainTotalDataListCompareMap;
		}

		public void setPublisherCampainTotalDataListMTDMap(
				Map<String,List<AdvertiserPerformerDTO>> publisherCampainTotalDataListMTDMap) {
			this.publisherCampainTotalDataListMTDMap = publisherCampainTotalDataListMTDMap;
		}

		public Map<String,List<AdvertiserPerformerDTO>> getPublisherCampainTotalDataListMTDMap() {
			return publisherCampainTotalDataListMTDMap;
		}


		public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
			this.userDetailsDTO = userDetailsDTO;
		}


		public UserDetailsDTO getUserDetailsDTO() {
			return userDetailsDTO;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

	
}
