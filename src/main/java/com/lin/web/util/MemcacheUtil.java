package com.lin.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.ProposalObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AccountDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.ChannelPerformancePopUpDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByLocationReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.ReconciliationDataDTO;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;


@SuppressWarnings("unchecked")
public class MemcacheUtil {
    
	public static MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();    
	
	private static final Logger log = Logger.getLogger(MemcacheUtil.class.getName());
	private static final int expireInDay = 60*60*24;
	private static final int expiredInTwoHours = 60*60*2;
	private static final int expiredInAnHour = 60*60;
	public static final int MEMCACHE_KEY_LENGTH = 249;
	
    private static final String AUTH_KEY="authKey";
    private static final String LIN_DIGITAL_AUTH_KEY="lin_digital_authKey";
    
    private static final String ALL_USERS="all_users";
    private static final String ALL_TEAMS="all_teams";
    private static final String ALL_COMPANY="all_company";
    private static final String ALL_AUTHORISATION_TEXT="all_authorisation_text";
    private static final String ALL_ROLES_AND_AUTHENTICATION="all_roles_and_authentication";
    private static final String ALL_PROPERTIES="all_properties";
    private static final String ALL_ACCOUNTS="all_accounts";
    private static final String ALL_DROPDOWNS_OBJ = "all_DropdownDataObj";
    private static final String ACCOUNT_AUTH_KEY="accountAuthKey";
    
    private static final String SELL_THROUGH_DATA="sell_through_data";    
    private static final String ADVERTISER_LINEITEM_POPUP_DATA = "advertiser_lineItem_data";
    
    private static final String PUBLISHER_CHANNEL_POPUP_DATA="publisher_channel_data";
    private static final String PUBLISHER_CHANNEL_POPUP_GRAPH_DATA="publisher_channel_graph_data";
	private static final String PUBLISHER_CHANNEL_FINALIZE_DATA="publisherFinaliseData"; 
	private static final String TREND_ANALYSIS_ACTUAL_DATA_PUBLISHER = "trend_analysis_actual_data_publisher";
	private static final String ACTUAL_LINE_CHART_MAP_PUBLISHER = "actual_line_chart_map_publisher";
	
	private static final String TREND_ANALYSIS_HEADER_DATA_ADVERTISER = "TREND_ANALYSIS_HEADER_DATA_ADVERTISER";
	private static final String TREND_ANALYSIS_ACTUAL_DATA_ADVERTISER = "TREND_ANALYSIS_ACTUAL_DATA_ADVERTISER";
	private static final String PERFORMANCE_SUMMARY_HEADER_DATA = "PERFORMANCE_SUMMARY_HEADER_DATA";
	private static final String TREND_ANALYSIS_FORECASTED_DATA_ADVERTISER = "TREND_ANALYSIS_FORECASTED_DATA_ADVERTISER";
	private static final String TOP_PERFORMER_LINE_ITEMS = "TOP_PERFORMER_LINE_ITEMS";
	private static final String TOP_NONPERFORMER_LINE_ITEMS = "TOP_NONPERFORMER_LINE_ITEMS";
	private static final String MOST_ACTIVE_LINE_ITEMS = "MOST_ACTIVE_LINE_ITEMS";
	private static final String TOP_GAINER_LINE_ITEMS = "TOP_GAINER_LINE_ITEMS";
	private static final String TOP_LOOSER_LINE_ITEMS = "TOP_LOOSER_LINE_ITEMS";
	private static final String PERFORMANCE_METRICS = "PERFORMANCE_METRICS";
	private static final String ADVERTISER_BY_MARKET = "ADVERTISER_BY_MARKET";
	private static final String ADVERTISER_BY_LOCATION = "ADVERTISER_BY_LOCATION";
	private static final String ADVERTISER_TOTAL_DATA = "ADVERTISER_TOTAL_DATA";
	private static final String PUBLISHER_TOTAL_DATA = "publisher_total_data";
	private static final String PUBLISHER_PERFORMANCE_BY_PROPERTY = "publisher_performance_by_property";
	private static final String PUBLISHER_PERFORMANCE_BY_PROPERTY_CALCULATED = "publisher_performance_by_property_calculated";
	private static final String DELIVERY_INDICATOR = "DELIVERY_INDICATOR";
	private static final String RICH_MEDIA_ADVERTISER_TOTAL_DATA = "RICH_MEDIA_ADVERTISER_TOTAL_DATA";
	private static final String RICH_MEDIA_DELIVERY_INDICATOR = "RICH_MEDIA_DELIVERY_INDICATOR";
	private static final String RICH_MEDIA_ADVERTISER_BY_MARKET = "RICH_MEDIA_ADVERTISER_BY_MARKET";
	private static final String RICH_MEDIA_ADVERTISER_BY_LOCATION = "RICH_MEDIA_ADVERTISER_BY_LOCATION";
	
	private static final String RICH_MEDIA_EVENT_POPUP = "rich_media_popup";
	private static final String RECONCILIATION_DATA = "reconciliation_data";
	private static final String RICH_MEDIA_EVENT_GRAPH = "rich_media_graph";
	private static final String ALL_RICH_MEDIA_EVENTS = "all_rich_media_events";
	
	private static final String ALL_GEO_TARGETS_KEY="all_geo_targets";
	private static final String ALL_INDUSTRIES_KEY="all_industry";
	private static final String ALL_INDUSTRIES_LIST="all_industry_list";
	private static final String ALL_KPI_KEY="all_kpi";
	private static final String PROPOSAL_KEY="proposal";
	private static final String MEDIA_PLAN_ADVERTISERS_KEY="all_advertisers";
	private static final String MEDIA_PLAN_AGENCIES_KEY="all_agencies";
	private static final String ADVERTISERS_BQ_KEY="bq_advertisers";
	private static final String AGENCIES_BQ_KEY="bq_agencies";
	private static final String ACCOUNTS_BQ_KEY="bq_accounts";
	private static final String ACCOUNTS_DFP_KEY="dfp_accounts";
	private static final String PROPERTIES_DFP_KEY="dfp_properties";
	private static final String ALL_PROPERTIES_DFP_KEY="all_dfp_properties";
	private static final String MEDIA_PLAN_COMPANY_KEY="media_plan_all_companies";
	private static final String MEDIA_PLAN_ADSIZE_KEY="all_adSize";
	private static final String MEDIA_PLAN_ADFORMAT_KEY="all_adFormats";
	private static final String MEDIA_PLAN_SITE_FORECAST_DATA_KEY="site";
	private static final String MEDIA_PLAN_PLACEMENT_KEY="media_plan_placement_key";
	
	private static final String CAMPAIGN_TRAFFICKING_DATA = "CAMPAIGN_TRAFFICKING_DATA";
	private static final String CAMPAIGN_TRAFFICKING_FORECASTED_DATA = "CAMPAIGN_TRAFFICKING_FORECASTED_DATA";
	
	private static final String DELIVERING_CAMPAIGN_DATA = "DELIVERING_CAMPAIGN_DATA";
	private static final String READY_CAMPAIGN_DATA = "READY_CAMPAIGN_DATA";
	private static final String DRAFT_NEEDCREATIVES_CAMPAIGN_DATA = "DRAFT_NEEDCREATIVES_CAMPAIGN_DATA";
	private static final String PAUSED_INVENTORYRELEASED_CAMPAIGN_DATA = "PAUSED_INVENTORYRELEASED_CAMPAIGN_DATA";
	private static final String PENDING_APPROVAL_CAMPAIGN_DATA = "PENDING_APPROVAL_CAMPAIGN_DATA";
	
	private static final String DFP_ORDER_ID_KEY="dfp_orderIds";
	
	private static final String LINE_CHART_KEY="line_chart";
	private static final String DELIVERY_METRIC_DATA_KEY="delivery_metrics_data_key";
	private static final String ADVERTISER_HEADER_DATA_KEY="advertiser_header_data_key";
	private static final String PERFORMANCE_BY_OS_KEY = "performance_by_os_key";
	private static final String PERFORMANCE_BY_DEVICE_KEY = "performance_by_device_key";
	private static final String PERFORMANCE_BY_PLACEMENT_KEY = "performance_by_placement_key";
	private static final String PERFORMANCE_BY_ADSIZE_KEY = "performance_by_adsize_key";
	private static final String MOST_ACTIVE_ADVERTISER_KEY = "most_active_advertiser_key";
	private static final String PERFORMANCE_BY_LOCATION_KEY = "performance_by_location_key";
	private static final String PERFORMER_ADVERTISER_OBJECT_KEY = "performer_advertiser_object_key";
	private static final String DFP_SITES_WITH_DMA_OBJS_KEY = "DFP_Sites_With_DMA_Objs_key";
	private static final String ALL_DMA_WITH_INVENTORY_KEY = "All_DMA_With_Inventory_Key";
	private static final String ALL_ALLOCATE_INVENTORY_KEY = "All_Allocate_Inventory_key";
	
	private static final String VIDEO_CAMPAIGN_DATA = "video_campaign_data_key";
	private static final String CAMPAIGN_GRID_DATA = "campaign_grid_data_key";
	private static final String ALL_PRODUCT_DATA = "all_Product_data";
	private static final String DFP_CHILD_ADUNITS = "dfp_child_adUnits";
	
	public static final String ALL_BQ_TABLES_KEY = "all_tables_key";
	public static final String FINALISE_BQ_TABLES_KEY = "finalise_tables_key";
	public static final String NON_FINALISE_BQ_TABLES_KEY = "non_finalise_tables_key";
	
	public static final String CAMPAIGN_HISTORY_KEY = "campaign_history_key_";
	public static final String ALL_CAMPAIGN_LIST_KEY = "all_campaign_list_key_";
	public static final String BILLING_REPORT_KEY = "billing_report_key_";
	
	
	public static void setAuthToken(String authorizationToken){
		if(memcache !=null && memcache.contains(AUTH_KEY)){
			memcache.delete(AUTH_KEY);
		}
		log.info("set token in memcache:"+authorizationToken);
		memcache.put(AUTH_KEY, authorizationToken);
	}
	
	public static void setAccountAuthToken(String userName, String authorizationToken){
		String key = ACCOUNT_AUTH_KEY + userName;
		try {
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			log.info("set AccountAuthToken token in memcache:"+authorizationToken + "for key : "+key);
			memcache.put(key, authorizationToken);
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's setAccountAuthToken : "+e.getMessage());
			
		}
	}
	
	public static String getAccountAuthToken(String userName){
		String key = ACCOUNT_AUTH_KEY + userName;
		String authorizationToken=null;
		try {
			if(memcache !=null && memcache.contains(key)){
				authorizationToken=(String) memcache.get(key);
			}
			log.info("get getAccountAuthToken token from memcache : "+authorizationToken + " for key : "+key);
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAccountAuthToken : "+e.getMessage());
			
		}
		return authorizationToken;
	}
	
	public static String getLinDigitalAuthToken(){
		String authorizationToken=null;
		if(memcache !=null && memcache.contains(LIN_DIGITAL_AUTH_KEY)){
			authorizationToken=(String) memcache.get(LIN_DIGITAL_AUTH_KEY);
		}
		log.info("get token from memcache:"+authorizationToken);
		return authorizationToken;
	}
	
	public static void setLinDigitalAuthToken(String authorizationToken){
		if(memcache !=null && memcache.contains(LIN_DIGITAL_AUTH_KEY)){
			memcache.delete(LIN_DIGITAL_AUTH_KEY);
		}
		log.info("set token in memcache:"+authorizationToken);
		memcache.put(LIN_DIGITAL_AUTH_KEY, authorizationToken);
	}
	
	public static String getAuthToken(){
		String authorizationToken=null;
		if(memcache !=null && memcache.contains(AUTH_KEY)){
			authorizationToken=(String) memcache.get(AUTH_KEY);
		}
		log.info("get token from memcache:"+authorizationToken);
		return authorizationToken;
	}
	
	/*public static List<DFPAgencyObj> getAllDFPAgencyObj() {
		List<DFPAgencyObj> dfpAgencyObjList =new ArrayList<DFPAgencyObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_DFP_AGENCIES)){
				dfpAgencyObjList=(List<DFPAgencyObj>) memcache.get(ALL_DFP_AGENCIES);
				log.warning("From memcache... dfpAgencyObjList Size= "+dfpAgencyObjList.size());
				if(dfpAgencyObjList == null || dfpAgencyObjList.size()==0){
					dfpAgencyObjList=getAllDFPAgencyObjFromDB();
				}
				
			}else{
				dfpAgencyObjList=getAllDFPAgencyObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllDFPAgencyObj");
			dfpAgencyObjList=getAllDFPAgencyObjFromDB();
		}
		return dfpAgencyObjList;
	}
	
	public static List<DFPAgencyObj> getAllDFPAgencyObjFromDB(){
		List<DFPAgencyObj> dfpAgencyObjList=new ArrayList<DFPAgencyObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  dfpAgencyObjList=userDetailsDAO.getAllDFPAgencyObj();
				if(dfpAgencyObjList !=null){
					log.warning("dfpAgencyObjList Size: "+dfpAgencyObjList.size());
				}
				memcache.put(ALL_DFP_AGENCIES, dfpAgencyObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllDFPAgencyObjFromDB"+e.getMessage());
				
		  }
	    
	      return dfpAgencyObjList;
	}
	
    
	public static void updateMemcacheDFPAgencyObj(){
		List<DFPAgencyObj> dfpAgencyObjList=new ArrayList<DFPAgencyObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		try {
			dfpAgencyObjList=userDetailsDAO.getAllDFPAgencyObj();
			if(dfpAgencyObjList != null) {
				log.warning("DFPAgencyObj key deleted from memchache");
				memcache.delete(ALL_DFP_AGENCIES);
				memcache.put(ALL_DFP_AGENCIES, dfpAgencyObjList);
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's updateMemcacheDFPAgencyObj : "+e.getMessage());
			
		}
		log.warning("DFPAgencyObj List Size: "+dfpAgencyObjList.size());
       
	}
    
	public static List<DFPAdvertisersObj> getAllDFPAdvertisersObj() {
		List<DFPAdvertisersObj> dfpAdvertisersObjList =new ArrayList<DFPAdvertisersObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_DFPADVERTISERS)){
				dfpAdvertisersObjList=(List<DFPAdvertisersObj>) memcache.get(ALL_DFPADVERTISERS);
				log.warning("From memcache... dfpAdvertisersObjList Size= "+dfpAdvertisersObjList.size());
				if(dfpAdvertisersObjList == null || dfpAdvertisersObjList.size()==0){
					dfpAdvertisersObjList=getAllDFPAdvertisersObjFromDB();
				}
				
			}else{
				dfpAdvertisersObjList=getAllDFPAdvertisersObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllDFPAdvertisersObj");
			dfpAdvertisersObjList=getAllDFPAdvertisersObjFromDB();
		}
		return dfpAdvertisersObjList;
	}
	
	public static List<DFPAdvertisersObj> getAllDFPAdvertisersObjFromDB(){
		List<DFPAdvertisersObj> dfpAdvertisersObjList=new ArrayList<DFPAdvertisersObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  dfpAdvertisersObjList=userDetailsDAO.getAllDFPAdvertisersObj();
				if(dfpAdvertisersObjList !=null){
					log.warning("dfpAdvertisersObjList Size: "+dfpAdvertisersObjList.size());
				}
				memcache.put(ALL_DFPADVERTISERS, dfpAdvertisersObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllDFPAdvertisersObjFromDB : "+e.getMessage());
				
		  }
	    
	      return dfpAdvertisersObjList;
	}
	
    
	public static void updateMemcacheDFPAdvertisersObj(){
		List<DFPAdvertisersObj> dfpAgencyObjList=new ArrayList<DFPAdvertisersObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		try {
			dfpAgencyObjList=userDetailsDAO.getAllDFPAdvertisersObj();
			if(dfpAgencyObjList != null) {
				log.warning("DFPAdvertisersObj key deleted from memchache");
				memcache.delete(ALL_DFPADVERTISERS);
				memcache.put(ALL_DFPADVERTISERS, dfpAgencyObjList);
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's updateMemcacheDFPAdvertisersObj : "+e.getMessage());
			
		}
		log.warning("DFPAdvertisersObj List Size: "+dfpAgencyObjList.size());
       
	}
	
	public static List<OrdersObj> getAllOrdersObj() {
		List<OrdersObj> ordersObjList =new ArrayList<OrdersObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_ORDERS)){
				ordersObjList=(List<OrdersObj>) memcache.get(ALL_ORDERS);
				log.warning("From memcache... ordersObjList Size= "+ordersObjList.size());
				if(ordersObjList == null || ordersObjList.size()==0){
					ordersObjList=getAllOrdersObjFromDB();
				}
				
			}else{
				ordersObjList=getAllOrdersObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllOrdersObj");
			ordersObjList=getAllOrdersObjFromDB();
		}
		return ordersObjList;
	}
	
	public static List<OrdersObj> getAllOrdersObjFromDB(){
		List<OrdersObj> ordersObjList=new ArrayList<OrdersObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  ordersObjList=userDetailsDAO.getAllOrdersObj();
				if(ordersObjList !=null){
					log.warning("ordersObjList Size: "+ordersObjList.size());
				}
				memcache.put(ALL_ORDERS, ordersObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllOrdersObjFromDB : "+e.getMessage());
				
		  }
	    
	      return ordersObjList;
	}*/
	
	public static List<DropdownDataObj> getAllDropdownDataObjList() {
		List<DropdownDataObj> dropdownDataObjList =new ArrayList<DropdownDataObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_DROPDOWNS_OBJ)){
				dropdownDataObjList=(List<DropdownDataObj>) memcache.get(ALL_DROPDOWNS_OBJ);
				log.warning("From memcache... dropdownDataObjList Size= "+dropdownDataObjList.size());
				if(dropdownDataObjList == null || dropdownDataObjList.size()==0){
					dropdownDataObjList=getAllDropdownDataObjFromDB();
				}
				
			}else{
				dropdownDataObjList=getAllDropdownDataObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAdServerToPublisherInBigQueryObj");
			dropdownDataObjList=getAllDropdownDataObjFromDB();
		}
		return dropdownDataObjList;
	}
	
	public static List<DropdownDataObj> getAllDropdownDataObjFromDB(){
		List<DropdownDataObj> dropdownDataObjList =new ArrayList<DropdownDataObj>();
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
	      try {
	    	  dropdownDataObjList=mediaPlanDAO.getDropdownDataObj();
				if(dropdownDataObjList !=null){
					log.warning("dropdownDataObjList Size: "+dropdownDataObjList.size());
				}
				memcache.put(ALL_DROPDOWNS_OBJ, dropdownDataObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllDropdownDataObjFromDB : "+e.getMessage());
				
		  }
	    
	      return dropdownDataObjList;
	}
	
	
	/*public static List<DfpBQMappingObj> getAllAdServerToPublisherInBigQueryObj() {
		List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList =new ArrayList<DfpBQMappingObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_ADSERVER_TO_PUBLISHER_IN_BIGQUERY)){
				adServerToPublisherInBigQueryObjList=(List<DfpBQMappingObj>) memcache.get(ALL_ADSERVER_TO_PUBLISHER_IN_BIGQUERY);
				log.warning("From memcache... adServerToPublisherInBigQueryObjList Size= "+adServerToPublisherInBigQueryObjList.size());
				if(adServerToPublisherInBigQueryObjList == null || adServerToPublisherInBigQueryObjList.size()==0){
					adServerToPublisherInBigQueryObjList=getAllAdServerToPublisherInBigQueryObjFromDB();
				}
				
			}else{
				adServerToPublisherInBigQueryObjList=getAllAdServerToPublisherInBigQueryObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAdServerToPublisherInBigQueryObj");
			adServerToPublisherInBigQueryObjList=getAllAdServerToPublisherInBigQueryObjFromDB();
		}
		return adServerToPublisherInBigQueryObjList;
	}
	
	public static List<DfpBQMappingObj> getAllAdServerToPublisherInBigQueryObjFromDB(){
		List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList =new ArrayList<DfpBQMappingObj>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  adServerToPublisherInBigQueryObjList=userDetailsDAO.getAdServerToPublisherInBigQueryObj();
				if(adServerToPublisherInBigQueryObjList !=null){
					log.warning("adServerToPublisherInBigQueryObjList Size: "+adServerToPublisherInBigQueryObjList.size());
				}
				memcache.put(ALL_ADSERVER_TO_PUBLISHER_IN_BIGQUERY, adServerToPublisherInBigQueryObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllAdServerToPublisherInBigQueryObjFromDB : "+e.getMessage());
				
		  }
	    
	      return adServerToPublisherInBigQueryObjList;
	}*/
	
	public static List<IndustryObj> getAllIndustryObjList() {
		List<IndustryObj> industryObjList =new ArrayList<IndustryObj>();
		try {
			if(memcache !=null && memcache.contains(ALL_INDUSTRIES_LIST)){
				industryObjList=(List<IndustryObj>) memcache.get(ALL_INDUSTRIES_LIST);
				log.warning("From memcache... industryObjList Size= "+industryObjList.size());
				if(industryObjList == null || industryObjList.size()==0){
					industryObjList = getAllIndustryObjFromDB();
				}
				
			}else{
				industryObjList=getAllIndustryObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAdServerToPublisherInBigQueryObj");
			industryObjList=getAllIndustryObjFromDB();
		}
		return industryObjList;
	}
	
	public static List<IndustryObj> getAllIndustryObjFromDB(){
		List<IndustryObj> industryObjList =new ArrayList<IndustryObj>();
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
	      try {
	    	  industryObjList = mediaPlanDAO.loadAllIndustries();
				if(industryObjList !=null){
					log.warning("industryObjList Size: "+industryObjList.size());
				}
				memcache.delete(ALL_INDUSTRIES_LIST);
				memcache.put(ALL_INDUSTRIES_LIST, industryObjList);
	      }catch (Exception e) {	
	    	    log.severe("Exception in MemcacheUtil's getAllIndustryObjFromDB : "+e.getMessage());
				
		  }
	    
	      return industryObjList;
	}
	
    public static List<PopUpDTO> getPublisherChannelPopUpData(String startDate,String endDate,String channel, String publisher){
    	List<PopUpDTO> resultList=null;
    	String key=PUBLISHER_CHANNEL_POPUP_DATA+"_"+startDate+"_"+endDate+"_"+channel+"_"+publisher;
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<PopUpDTO>) memcache.get(key);
    	}
    	return resultList;
	}
	
    public static void  setPublisherChannelPopUpData(List<PopUpDTO> dataList,String startDate,String endDate,String channel, String publisher){
    	String key=PUBLISHER_CHANNEL_POPUP_DATA+"_"+startDate+"_"+endDate+"_"+channel+"_"+publisher;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList);
	}
    
    public static PopUpDTO getAdvertiserLineItemPopUpData(String lowerDate,String upperDate,String lineItemName,String publisherName){
    	PopUpDTO popUpDTO = null;
    	String key=ADVERTISER_LINEITEM_POPUP_DATA+"_"+lowerDate+"_"+upperDate+"_"+lineItemName+"_"+publisherName;
    	if(memcache !=null && memcache.contains(key)){
    		popUpDTO=(PopUpDTO) memcache.get(key);
    	}
    	return popUpDTO;
	}
	
    public static void  setAdvertiserLineItemPopUpData(PopUpDTO popUpDTO, String lowerDate, String upperDate, String lineItemName, String publisherName){
    	String key=ADVERTISER_LINEITEM_POPUP_DATA+"_"+lowerDate+"_"+upperDate+"_"+lineItemName+"_"+publisherName;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, popUpDTO);
	}
	
    public static String getPublisherChannelPopUpGraphData(String startDate,String endDate,String channelId, String publisherId){
    	String chartData=null;
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+channelId+publisherId);
        String key=PUBLISHER_CHANNEL_POPUP_GRAPH_DATA+"_"+hashedKeyPart;
    	if(memcache !=null && memcache.contains(key)){
    		chartData=(String) memcache.get(key);
    	}
    	return chartData;
	}
	
    public static void  setPublisherChannelPopUpGraphData(String chartData,String startDate,String endDate,String channelId, String publisherId){
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+channelId+publisherId);
        String key=PUBLISHER_CHANNEL_POPUP_GRAPH_DATA+"_"+hashedKeyPart;
        log.warning("key value : "+key+", key length : "+key.length());
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, chartData);
	}
    
    public static List<UserDetailsObj> getAllUsersList() {
		List<UserDetailsObj> userDetailsObjList =new ArrayList<UserDetailsObj>(0);
		try {
			if(memcache !=null && memcache.contains(ALL_USERS)){
				userDetailsObjList=(List<UserDetailsObj>) memcache.get(ALL_USERS);
				log.warning("From memcache... userDetailsObjList Size= "+userDetailsObjList.size());
				if(userDetailsObjList == null || userDetailsObjList.size()==0){
					userDetailsObjList=getAllUsersFromDB();
				}
				
			}else{
				userDetailsObjList=getAllUsersFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllUsersList");
			userDetailsObjList=getAllUsersFromDB();
		}
		return userDetailsObjList;
	}
	
	public static List<UserDetailsObj> getAllUsersFromDB(){
		List<UserDetailsObj> userDetailsObjList=new ArrayList<UserDetailsObj>(0);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  userDetailsObjList=userDetailsDAO.getAllUsers();
				if(userDetailsObjList !=null){
					log.warning("UserList Size: "+userDetailsObjList.size());
				}
				memcache.put(ALL_USERS, userDetailsObjList);
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return userDetailsObjList;
	}
	
    
	/*public static void updateMemcacheUserDetails(){
		List<UserDetailsObj> userDetailsObjList=new ArrayList<UserDetailsObj>(0);
		memcache.delete(ALL_USERS);
		log.warning("UserDetailsObj key deleted from memchache");
		log.warning("Going to LinMobileDAO to get UserDetailsObj List");
		userDetailsObjList=getAllUsersFromDB();
		log.warning("UserDetailsObj List Size: "+userDetailsObjList.size());
       
	}*/
	
	public static boolean selfUpdateMemcacheUserDetails(UserDetailsObj userDetailsObj, boolean addNewObject, boolean isDeleteObject){
		List<UserDetailsObj> objList = new ArrayList<UserDetailsObj>();
		boolean result = false;
		try {
			if(userDetailsObj != null) {
				objList = getAllUsersList();
				if(addNewObject && !isDeleteObject) {			// save new object
					if(objList == null) {
						objList = new ArrayList<UserDetailsObj>();
					}
					objList.add(userDetailsObj);
					result = true;
				}
				else {											// update or delete existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (UserDetailsObj obj : objList) {
							if(obj.getId() == userDetailsObj.getId()) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							if(isDeleteObject) {				// delete existing object
								objList.remove(count);
							}else {								// update existing object
								objList.add(count, userDetailsObj);
								objList.remove(count + 1);
							}
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(ALL_USERS)){
						memcache.delete(ALL_USERS);
					}
					memcache.put(ALL_USERS, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheUserDetails of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<SellThroughDataObj> getSellThroughData(String startDate,String endDate, String publisherBQId, String DFP_Properties){
		log.info("within getSellThroughData() of MemcacheUtil class");
    	List<SellThroughDataObj> resultList=null;
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherBQId+DFP_Properties);
        String key=SELL_THROUGH_DATA+"_"+hashedKeyPart;
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getSellThroughData() of MemcacheUtil class key = "+key);
    		resultList=(List<SellThroughDataObj>) memcache.get(key);
    		log.info("within getSellThroughData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setSellThroughData(List<SellThroughDataObj> dataList,String startDate,String endDate, String publisherBQId, String DFP_Properties){
    	log.info("within setSellThroughData() of MemcacheUtil class");
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherBQId+DFP_Properties);
        String key=SELL_THROUGH_DATA+"_"+hashedKeyPart;
        log.warning("key value : "+key+", key length : "+key.length());
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setSellThroughData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setSellThroughData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static void loadAllFinalizeData(Map<String, List<PublisherChannelObj>> channelsDataMap,List<String> channelArrList,String publisherName){
		if(channelArrList!=null && channelArrList.size()>0){
			Iterator<String> iterator = channelArrList.iterator();
			while(iterator.hasNext()){
				String object = iterator.next();
				if(channelsDataMap.containsKey(object)){
					List<PublisherChannelObj> channelDataList = channelsDataMap.get(object);
					String key = PUBLISHER_CHANNEL_FINALIZE_DATA+"_"+publisherName+"_"+object;
					if(memcache!=null && !memcache.contains(key)){
						memcache.put(key, channelDataList,Expiration.byDeltaSeconds(LinMobileConstants.CHANNEL_DATA_EXPIRATION_TIME));
					}
				}
			}
		}
	}
	public static HashMap<String,List<PublisherChannelObj>>  getAllFinalizeData(List<String> channelArrList,String publisherName){
		HashMap<String,List<PublisherChannelObj>> channelsDataMap = new HashMap<String, List<PublisherChannelObj>>();
		new ArrayList<PublisherChannelObj>();
		if(channelArrList!=null && channelArrList.size()>0){
			Iterator<String> iterator = channelArrList.iterator();
			while(iterator.hasNext()){
				String object = iterator.next();
				//channelsDataMap.put(object, channelDataList);
			    String key = PUBLISHER_CHANNEL_FINALIZE_DATA+"_"+publisherName+"_"+object;
			    if(memcache!=null && memcache.contains(key)){
			    	
			    		List<PublisherChannelObj> tempList = (List<PublisherChannelObj>) memcache.get(key);
			    		channelsDataMap.put(object, tempList);
			    		tempList = null;
			    }
			}
		}
		return channelsDataMap;
		
	}
	public static void flushOneYearData(List<String> channelArrList,String publisherName){
		if(channelArrList!=null && channelArrList.size()>0){
			Iterator<String> iterator = channelArrList.iterator();
			while(iterator.hasNext()){
				String object = iterator.next();
				String key = PUBLISHER_CHANNEL_FINALIZE_DATA+"_"+publisherName+"_"+object;
					if(memcache!=null && memcache.contains(key)){
						memcache.delete(key);
					}
			}
		}
	}
	
	public static List<AuthorisationTextObj> getAllAuthorisationTextList() {
		List<AuthorisationTextObj> authorisationTextList =new ArrayList<AuthorisationTextObj>(0);
		try {
			if(memcache !=null && memcache.contains(ALL_AUTHORISATION_TEXT)){
				authorisationTextList=(List<AuthorisationTextObj>) memcache.get(ALL_AUTHORISATION_TEXT);
				log.warning("From memcache... authorisationTextList Size= "+authorisationTextList.size());
				if(authorisationTextList == null || authorisationTextList.size()==0){
					authorisationTextList=getAllAuthorisationTextFromDB();
				}
			}else{
				authorisationTextList=getAllAuthorisationTextFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAuthorisationTextList");
			authorisationTextList=getAllAuthorisationTextFromDB();
		}
		return authorisationTextList;
	}
	
	public static List<AuthorisationTextObj> getAllAuthorisationTextFromDB(){
		List<AuthorisationTextObj> authorisationTextList=new ArrayList<AuthorisationTextObj>(0);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  authorisationTextList=userDetailsDAO.getAllAuthorisationText();
				if(authorisationTextList !=null){
					log.warning("AuthorisationText List Size: "+authorisationTextList.size());
				}
				memcache.put(ALL_AUTHORISATION_TEXT, authorisationTextList);
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return authorisationTextList;
	}
	
	public static void updateMemcacheAuthorisationText(){
		List<AuthorisationTextObj> authorisationTextList=new ArrayList<AuthorisationTextObj>(0);
		memcache.delete(ALL_AUTHORISATION_TEXT);
		log.warning("AuthorisationText key deleted from memchache");
		log.warning("Going to UserDetailsDAO to get AuthorisationText List");
		authorisationTextList=getAllAuthorisationTextFromDB();
		log.warning("AuthorisationText List Size: "+authorisationTextList.size());
	}
	
	public static List<RolesAndAuthorisation> getAllRolesAndAuthorisationList() {
		List<RolesAndAuthorisation> rolesAndAuthorisationsList =new ArrayList<RolesAndAuthorisation>(0);
		try {
			if(memcache !=null && memcache.contains(ALL_ROLES_AND_AUTHENTICATION)){
				rolesAndAuthorisationsList=(List<RolesAndAuthorisation>) memcache.get(ALL_ROLES_AND_AUTHENTICATION);
				log.warning("From memcache... RolesAndAuthorisation List Size= "+rolesAndAuthorisationsList.size());
				if(rolesAndAuthorisationsList == null || rolesAndAuthorisationsList.size()==0){
					rolesAndAuthorisationsList=getAllRolesAndAuthorisationFromDB();
				}
			}else{
				rolesAndAuthorisationsList=getAllRolesAndAuthorisationFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllRolesAndAuthorisationList");
			rolesAndAuthorisationsList=getAllRolesAndAuthorisationFromDB();
		}
		return rolesAndAuthorisationsList;
	}
	
	public static List<RolesAndAuthorisation> getAllRolesAndAuthorisationFromDB(){
		List<RolesAndAuthorisation> rolesAndAuthorisationsList=new ArrayList<RolesAndAuthorisation>(0);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  rolesAndAuthorisationsList=userDetailsDAO.getAllRolesAndAuthorisation();
				if(rolesAndAuthorisationsList !=null){
					memcache.put(ALL_ROLES_AND_AUTHENTICATION, rolesAndAuthorisationsList);
					log.warning("RolesAndAuthorisation List Size: "+rolesAndAuthorisationsList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return rolesAndAuthorisationsList;
	}
	
	/*public static void updateMemcacheRolesAndAuthorisation(){
		List<RolesAndAuthorisation> rolesAndAuthorisationList=new ArrayList<RolesAndAuthorisation>(0);
		memcache.delete(ALL_ROLES_AND_AUTHENTICATION);
		log.warning("Going to UserDetailsDAO to get RolesAndAuthorisation List");
		rolesAndAuthorisationList=getAllRolesAndAuthorisationFromDB();
		log.warning("RolesAndAuthorisation List Size: "+rolesAndAuthorisationList.size());
	}*/
	
	public static boolean selfUpdateMemcacheRolesAndAuthorisation(RolesAndAuthorisation rolesAndAuthorisation, boolean addNewObject){
		List<RolesAndAuthorisation> objList = new ArrayList<RolesAndAuthorisation>();
		boolean result = false;
		try {
			if(rolesAndAuthorisation != null) {
				objList = getAllRolesAndAuthorisationList();
				if(addNewObject) {			// save new object
					if(objList == null) {
						objList = new ArrayList<RolesAndAuthorisation>();
					}
					objList.add(rolesAndAuthorisation);
					result = true;
				}
				else {						// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (RolesAndAuthorisation obj : objList) {
							if(obj.getId() == rolesAndAuthorisation.getId()) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, rolesAndAuthorisation);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(ALL_ROLES_AND_AUTHENTICATION)){
						memcache.delete(ALL_ROLES_AND_AUTHENTICATION);
					}
					memcache.put(ALL_ROLES_AND_AUTHENTICATION, objList);
					log.info("Memcache upadetd successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheRolesAndAuthorisation of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	public static List<TeamPropertiesObj> getAllTeamPropertiesObjList() {
		List<TeamPropertiesObj> teamPropertiesObjList =new ArrayList<TeamPropertiesObj>(0);
		try {
			if(memcache !=null && memcache.contains(ALL_TEAMS)){
				teamPropertiesObjList=(List<TeamPropertiesObj>) memcache.get(ALL_TEAMS);
				log.warning("From memcache... TeamPropertiesObj List Size= "+teamPropertiesObjList.size());
				if(teamPropertiesObjList == null || teamPropertiesObjList.size()==0){
					teamPropertiesObjList=getAllTeamPropertiesObjFromDB();
				}
			}else{
				teamPropertiesObjList=getAllTeamPropertiesObjFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllTeamPropertiesObjList");
			teamPropertiesObjList=getAllTeamPropertiesObjFromDB();
		}
		return teamPropertiesObjList;
	}
	
	public static List<TeamPropertiesObj> getAllTeamPropertiesObjFromDB(){
		List<TeamPropertiesObj> teamPropertiesObjList=new ArrayList<TeamPropertiesObj>(0);
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
	      try {
	    	  IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	    	  teamPropertiesObjList=userDetailsDAO.getAllTeamPropertiesObj();
				if(teamPropertiesObjList !=null){
					memcache.put(ALL_TEAMS, teamPropertiesObjList);
					log.warning("TeamPropertiesObj List Size: "+teamPropertiesObjList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return teamPropertiesObjList;
	}
	
	/*public static void updateMemcacheTeamPropertiesObj(){
		List<TeamPropertiesObj> teamPropertiesObjList=new ArrayList<TeamPropertiesObj>(0);
		memcache.delete(ALL_TEAMS);
		log.warning("Going to UserDetailsDAO to get TeamPropertiesObj List");
		teamPropertiesObjList=getAllTeamPropertiesObjFromDB();
		log.warning("TeamPropertiesObj List Size: "+teamPropertiesObjList.size());
	}*/
	
	public static boolean selfUpdateMemcacheTeamPropertiesObj(TeamPropertiesObj teamPropertiesObj, boolean addNewObject){
		List<TeamPropertiesObj> objList = new ArrayList<TeamPropertiesObj>();
		boolean result = false;
		try {
			if(teamPropertiesObj != null) {
				objList = getAllTeamPropertiesObjList();
				if(addNewObject) {			// save new object
					if(objList == null) {
						objList = new ArrayList<TeamPropertiesObj>();
					}
					objList.add(teamPropertiesObj);
					result = true;
				}
				else {						// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (TeamPropertiesObj obj : objList) {
							if(obj.getId().equals(teamPropertiesObj.getId())) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, teamPropertiesObj);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(ALL_TEAMS)){
						memcache.delete(ALL_TEAMS);
					}
					memcache.put(ALL_TEAMS, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheTeamPropertiesObj of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	
	public static List<CompanyObj> getAllCompanyList() {
		List<CompanyObj> companyList =new ArrayList<CompanyObj>(0);
		try {
			if(memcache !=null && memcache.contains(ALL_COMPANY)){
				companyList=(List<CompanyObj>) memcache.get(ALL_COMPANY);
				log.warning("From memcache... getAllCompanyList List Size= "+companyList.size());
				if(companyList == null || companyList.size()==0){
					companyList=getAllCompanyFromDB();
				}
			}else{
				companyList=getAllCompanyFromDB();
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllCompanyList");
			companyList=getAllCompanyFromDB();
		}
		return companyList;
	}
	
	public static List<CompanyObj> getAllCompanyFromDB(){
		List<CompanyObj> companyList=new ArrayList<CompanyObj>(0);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	      try {
	    	  companyList=userDetailsDAO.getAllCompany();
				if(companyList !=null){
					memcache.put(ALL_COMPANY, companyList);
					log.warning("companyList List Size: "+companyList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getAllCompanyFromDB :"+e.getMessage());
				
		  }
	    
	      return companyList;
	}
	
	/*public static void updateMemcacheCompany(){
		List<CompanyObj> companyList=new ArrayList<CompanyObj>(0);
		memcache.delete(ALL_COMPANY);
		log.warning("Going to UserDetailsDAO to get Company List");
		companyList=getAllCompanyFromDB();
		log.warning("companyList List Size: "+companyList.size());
	}*/
	
	public static boolean selfUpdateMemcacheCompany(CompanyObj companyObj, boolean addNewObject){
		List<CompanyObj> objList = new ArrayList<CompanyObj>();
		boolean result = false;
		try {
			if(companyObj != null) {
				objList = getAllCompanyList();
				if(addNewObject) {			// save new object
					if(objList == null) {
						objList = new ArrayList<CompanyObj>();
					}
					objList.add(companyObj);
					result = true;
				}
				else {						// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (CompanyObj obj : objList) {
							if(obj.getId() == companyObj.getId()) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, companyObj);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(ALL_COMPANY)){
						memcache.delete(ALL_COMPANY);
					}
					memcache.put(ALL_COMPANY, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheCompany of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	/*public static List<AccountsObj> getAllAccountsObjList(String adServerId, String adserverUsername) {
		List<AccountsObj> accountsObjList =new ArrayList<AccountsObj>(0);
		String key = ALL_ACCOUNTS + adServerId+adserverUsername;
		log.info("ket : "+key);
		try {
			if(memcache !=null && memcache.contains(key)){
				accountsObjList=(List<AccountsObj>) memcache.get(key);
				log.warning("From memcache... AccountsObj List Size= "+accountsObjList.size());
				if(accountsObjList == null || accountsObjList.size()==0){
					accountsObjList=getAllAccountsObjFromDB(adServerId, adserverUsername);
				}
			}else{
				accountsObjList=getAllAccountsObjFromDB(adServerId, adserverUsername);
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAccountsObjList");
			accountsObjList=getAllAccountsObjFromDB(adServerId, adserverUsername);
		}
		return accountsObjList;
	}
	
	public static List<AccountsObj> getAllAccountsObjFromDB(String adServerId, String adserverUsername) {
		String key = ALL_ACCOUNTS + adServerId+adserverUsername;
		List<AccountsObj> accountsObjList=new ArrayList<AccountsObj>(0);
	      try {
	    	  IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	    	  accountsObjList = userDetailsDAO.getAccountsObjByAdServerIdUsername(adServerId, adserverUsername);
				if(accountsObjList !=null){
					memcache.put(key, accountsObjList);
					log.warning("AccountsObj List Size: "+accountsObjList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return accountsObjList;
	}
	
	public static boolean selfUpdateMemcacheAccountsObj(AccountsObj accountsObj, boolean addNewObject, String networkId, String networkUserName, String previousNetworkId, String prevoiusNetworkUsername){
		List<AccountsObj> objList = null;
		List<AccountsObj> previousCompanyObjList = null;
		String key = ALL_ACCOUNTS + networkId + networkUserName;
		String previousCompanykey = ALL_ACCOUNTS + previousNetworkId + prevoiusNetworkUsername;
		log.info("key : "+key);
		boolean result = false;
		boolean previousCompanyMemcacheUpdate = false;
		try {
			if(accountsObj != null) {
				objList = getAllAccountsObjList(networkId, networkUserName);
				if(addNewObject) {			// save new object
					if(objList == null) {
						objList = new ArrayList<AccountsObj>();
					}
					objList.add(accountsObj);
					result = true;
				}
				else {						// update existing object
					if(objList != null) {
						int count = 0;
						boolean found = false;
						if(!(networkId + networkUserName).equals(previousNetworkId + prevoiusNetworkUsername)) {
							previousCompanyObjList = getAllAccountsObjList(previousNetworkId, prevoiusNetworkUsername);
							for (AccountsObj obj : previousCompanyObjList) {
								if(obj.getId().equals(accountsObj.getId())) {
									found = true;
									break;
								}
								count++;
							}
							if(found) {
								previousCompanyObjList.remove(count);
								previousCompanyMemcacheUpdate = true;
							}
							objList.add(accountsObj);
							result = true;
						}
						else {
							for (AccountsObj obj : objList) {
								if(obj.getId().equals(accountsObj.getId())) {
									found = true;
									break;
								}
								count++;
							}
							if(found) {
								objList.add(count, accountsObj);
								objList.remove(count + 1);
								result = true;
							}
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(key)){
						memcache.delete(key);
					}
					memcache.put(key, objList);
					log.info("Memcache for key '"+key+"' updated successfully, size : "+objList.size());
					
					if(previousCompanyMemcacheUpdate) {
						if(memcache !=null && memcache.contains(previousCompanykey)){
							memcache.delete(previousCompanykey);
						}
						memcache.put(previousCompanykey, previousCompanyObjList);
						log.info("Memcache for key '"+previousCompanykey+"' updated successfully, size : "+previousCompanyObjList.size());
					}
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheAccountsObj of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}*/
	
	 public static List<AccountsEntity> getAllAccountsObjList(String companyId) {
		List<AccountsEntity> accountsObjList =new ArrayList<AccountsEntity>(0);
		String key = ALL_ACCOUNTS + companyId;
		log.info("key : "+key);
		try {
			if(memcache !=null && memcache.contains(key)){
				accountsObjList=(List<AccountsEntity>) memcache.get(key);
				log.warning("From memcache... AccountsObj List Size= "+accountsObjList.size());
				if(accountsObjList == null || accountsObjList.size()==0){
					accountsObjList=getAllAccountsObjFromDB(companyId);
				}
			}else{
				accountsObjList=getAllAccountsObjFromDB(companyId);
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllAccountsObjList"+e.getMessage());
			
			accountsObjList=getAllAccountsObjFromDB(companyId);
		}
		return accountsObjList;
	}
	
	public static List<AccountsEntity> getAllAccountsObjFromDB(String companyId) {
		String key = ALL_ACCOUNTS + companyId;
		List<AccountsEntity> accountsObjList=new ArrayList<AccountsEntity>(0);
	      try {
	    	  IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
	    	  accountsObjList = userDetailsDAO.getAccountsObjByCompanyId(companyId);
				if(accountsObjList !=null){
					memcache.put(key, accountsObjList);
					log.warning("AccountsObj List Size: "+accountsObjList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getPostFromDB:DataServiceException:"+e.getMessage());
				
		  }
	    
	      return accountsObjList;
	}
	
	public static boolean selfUpdateMemcacheAccountsObj(AccountsEntity accountsObj, boolean addNewObject) {
		List<AccountsEntity> objList = new ArrayList<AccountsEntity>();
		boolean result = false;
		try {
			if(accountsObj != null) {
				String key = ALL_ACCOUNTS + accountsObj.getCompanyId();
				objList = getAllAccountsObjList(accountsObj.getCompanyId());
				if(addNewObject) {			// save new object
					if(objList == null) {	// if Datastore is empty
						objList = new ArrayList<AccountsEntity>();
					}
					objList.add(accountsObj);
					result = true;
				}
				else {						// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (AccountsEntity obj : objList) {
							if(obj.getId().equals(accountsObj.getId())) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, accountsObj);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(key)){
						memcache.delete(key);
					}
					memcache.put(key, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcacheAccountsObj of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	public static void  setAccountsObj(List<AccountsEntity> dataList, String companyId){
		String key = ALL_ACCOUNTS + companyId;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList);
	}
	
	public static List<AdvertiserPerformanceSummaryHeaderDTO> getPerformanceSummaryHeaderData(String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getPerformanceSummaryHeaderData() of MemcacheUtil class");
    	List<AdvertiserPerformanceSummaryHeaderDTO> resultList=null;
    	String key=PERFORMANCE_SUMMARY_HEADER_DATA+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Performance Summary key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getPerformanceSummaryHeaderData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformanceSummaryHeaderDTO>) memcache.get(key);
    		log.info("within getPerformanceSummaryHeaderData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setPerformanceSummaryHeaderData(List<AdvertiserPerformanceSummaryHeaderDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setPerformanceSummaryHeaderData() of MemcacheUtil class");
    	String key=PERFORMANCE_SUMMARY_HEADER_DATA+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("performance Summary key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setPerformanceSummaryHeaderData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setPerformanceSummaryHeaderData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getTopPerformerLineItemsList(String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getTopPerformerLineItemsList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=TOP_PERFORMER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Performer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTopPerformerLineItemsList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getTopPerformerLineItemsList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTopPerformerLineItemsList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setTopPerformerLineItemsList() of MemcacheUtil class");
    	String key=TOP_PERFORMER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Performer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTopPerformerLineItemsList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTopPerformerLineItemsList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getTopNonPerformerLineItemsList(String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getTopNonPerformerLineItemsList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=TOP_NONPERFORMER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top NonPerformer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTopNonPerformerLineItemsList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getTopNonPerformerLineItemsList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTopNonPerformerLineItemsList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setTopNonPerformerLineItemsList() of MemcacheUtil class");
    	String key=TOP_NONPERFORMER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top NonPerformer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTopNonPerformerLineItemsList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTopNonPerformerLineItemsList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getMostActiveLineItemList(String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getMostActiveLineItemList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=MOST_ACTIVE_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Most Active Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getMostActiveLineItemList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getMostActiveLineItemList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setMostActiveLineItemList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setMostActiveLineItemList() of MemcacheUtil class");
    	String key=MOST_ACTIVE_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Most Active Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setMostActiveLineItemList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setMostActiveLineItemList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getTopGainersLineItemList(String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getTopGainersLineItemList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=TOP_GAINER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Gainer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTopGainersLineItemList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getTopGainersLineItemList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTopGainersLineItemList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setTopGainersLineItemList() of MemcacheUtil class");
    	String key=TOP_GAINER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Gainer Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTopGainersLineItemList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTopGainersLineItemList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getTopLosersLineItemList(String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getTopLosersLineItemList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=TOP_LOOSER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Looser Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTopLosersLineItemList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getTopLosersLineItemList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTopLosersLineItemList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String compareStartDate,String compareEndDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setTopLosersLineItemList() of MemcacheUtil class");
    	String key=TOP_LOOSER_LINE_ITEMS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+compareStartDate+"_"+compareEndDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Top Looser Line Item key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTopLosersLineItemList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTopLosersLineItemList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserPerformerDTO> getPerformanceMetricsList(String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getPerformanceMetricsList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=PERFORMANCE_METRICS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Performance Metrics key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getPerformanceMetricsList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getPerformanceMetricsList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setPerformanceMetricsList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setPerformanceMetricsList() of MemcacheUtil class");
    	String key=PERFORMANCE_METRICS+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Performance Metrics key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setPerformanceMetricsList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setPerformanceMetricsList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserTrendAnalysisHeaderDTO> getTrendAnalysisHeaderDataAdvertiserView(String startDate,String endDate,String publisherName,String order,String lineOrder){
		log.info("within getTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class");
    	List<AdvertiserTrendAnalysisHeaderDTO> resultList=null;
    	String key=TREND_ANALYSIS_HEADER_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineOrder;
    	log.warning("TrendAnalysis Header Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserTrendAnalysisHeaderDTO>) memcache.get(key);
    		log.info("within getTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTrendAnalysisHeaderDataAdvertiserView(List<AdvertiserTrendAnalysisHeaderDTO> dataList,String startDate,String endDate,String publisherName,String order,String lineOrder){
    	log.info("within setTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class");
    	String key=TREND_ANALYSIS_HEADER_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineOrder;
    	log.warning("TrendAnalysis Header Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTrendAnalysisHeaderDataAdvertiserView() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    
	public static List<AdvertiserTrendAnalysisActualDTO> getTrendAnalysisActualDataAdvertiserView(String startDate,String endDate,String publisherName,String order,String lineItem){
		log.info("within getTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class");
    	List<AdvertiserTrendAnalysisActualDTO> resultList=null;
    	String key=TREND_ANALYSIS_ACTUAL_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineItem;
    	log.warning("TrendAnalysis Actual Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserTrendAnalysisActualDTO>) memcache.get(key);
    		log.info("within getTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTrendAnalysisActualDataAdvertiserView(List<AdvertiserTrendAnalysisActualDTO> dataList,String startDate,String endDate,String publisherName,String order,String lineItem){
    	log.info("within setTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class");
    	String key=TREND_ANALYSIS_ACTUAL_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineItem;
    	log.warning("TrendAnalysis Actual Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTrendAnalysisActualDataAdvertiserView() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserTrendAnalysisForcastDTO> getTrendAnalysisForcastDataAdvertiserView(String startDate,String endDate,String publisherName,String order,String lineItem){
		log.info("within getTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class");
    	List<AdvertiserTrendAnalysisForcastDTO> resultList=null;
    	String key=TREND_ANALYSIS_FORECASTED_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineItem;
    	log.warning("TrendAnalysis Forcasted Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserTrendAnalysisForcastDTO>) memcache.get(key);
    		log.info("within getTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setTrendAnalysisForcastDataAdvertiserView(List<AdvertiserTrendAnalysisForcastDTO> dataList,String startDate,String endDate,String publisherName,String order,String lineItem){
    	log.info("within setTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class");
    	String key=TREND_ANALYSIS_FORECASTED_DATA_ADVERTISER+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+order+"_"+lineItem;
    	log.warning("TrendAnalysis Forecasted Data AdvertiserView key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setTrendAnalysisForcastDataAdvertiserView() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<AdvertiserByLocationObj> getAdvertiserByMarketGraphData(String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
		log.info("within getAdvertiserByMarketGraphData() of MemcacheUtil class");
    	List<AdvertiserByLocationObj> resultList=null;
    	
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherName+advertiser+agency+properties);
        String key=ADVERTISER_BY_MARKET+"_"+hashedKeyPart;
        log.warning("Advetiser By Market Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getAdvertiserByMarketGraphData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserByLocationObj>) memcache.get(key);
    		log.info("within getAdvertiserByMarketGraphData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setAdvertiserByMarketGraphData(List<AdvertiserByLocationObj> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setAdvertiserByMarketGraphData() of MemcacheUtil class");
    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherName+advertiser+agency+properties);
        String key=ADVERTISER_BY_MARKET+"_"+hashedKeyPart;
        log.warning("key value : "+key+", key length : "+key.length());
        
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setAdvertiserByMarketGraphData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setAdvertiserByMarketGraphData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    public static List<AdvertiserByLocationObj> getRichMediaAdvertiserByMarketGraphData(String startDate,String endDate,String advertiser,String agency,String properties){
		log.info("within getRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class");
    	List<AdvertiserByLocationObj> resultList=null;
    	String key=RICH_MEDIA_ADVERTISER_BY_MARKET+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Advetiser By Market Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserByLocationObj>) memcache.get(key);
    		log.info("within getRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaAdvertiserByMarketGraphData(List<AdvertiserByLocationObj> dataList,String startDate,String endDate,String advertiser,String agency,String properties){
    	log.info("within setRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class");
    	String key=RICH_MEDIA_ADVERTISER_BY_MARKET+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Advetiser By Market Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setRichMediaAdvertiserByMarketGraphData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    
    
	public static List<AdvertiserByLocationObj> getAdvertisersByLocationData(String startDate, String endDate, String advertiser,String  agency,String  publisherName){
		log.info("within getAdvertisersByLocationData() of MemcacheUtil class");
    	List<AdvertiserByLocationObj> resultList=null;
    	String key=ADVERTISER_BY_LOCATION+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency;
    	log.warning("Advetiser By Location Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getAdvertisersByLocationData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserByLocationObj>) memcache.get(key);
    		log.info("within getAdvertisersByLocationData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setAdvertisersByLocationData(List<AdvertiserByLocationObj> dataList,String startDate,String endDate,String advertiser,String  agency,String  publisherName){
    	log.info("within setAdvertisersByLocationData() of MemcacheUtil class");
    	String key=ADVERTISER_BY_LOCATION+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency;
    	//log.warning("Advertiser By Location key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setAdvertisersByLocationData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setAdvertisersByLocationData() of MemcacheUtil class, memcache.put() called..."+key);
	}
	
	public static List<AdvertiserByLocationObj> getRichMediaAdvertisersByLocationData(String startDate, String endDate, String advertiser,String  agency,String  properties){
		log.info("within getRichMediaAdvertisersByLocationData() of MemcacheUtil class");
    	List<AdvertiserByLocationObj> resultList=null;
    	String key=RICH_MEDIA_ADVERTISER_BY_LOCATION+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Advetiser By Location Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getRichMediaAdvertisersByLocationData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserByLocationObj>) memcache.get(key);
    		log.info("within getRichMediaAdvertisersByLocationData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaAdvertisersByLocationData(List<AdvertiserByLocationObj> dataList,String startDate,String endDate,String advertiser,String  agency,String  properties){
    	log.info("within setRichMediaAdvertisersByLocationData() of MemcacheUtil class");
    	String key=RICH_MEDIA_ADVERTISER_BY_LOCATION+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Advertiser By Location key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setRichMediaAdvertisersByLocationData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setRichMediaAdvertisersByLocationData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    public static List<PropertyObj> getAllPropertiesList(String companyId) {
		List<PropertyObj> propertyObjList =new ArrayList<PropertyObj>(0);
		String key = ALL_PROPERTIES+companyId;
		try {
			if(memcache !=null && memcache.contains(key)){
				propertyObjList=(List<PropertyObj>) memcache.get(key);
				log.warning("From memcache... PropertyObjList Size= "+propertyObjList.size());
				if(propertyObjList == null || propertyObjList.size()==0){
					propertyObjList=getAllPropertiesFromDB(companyId);
				}
			}else{
				propertyObjList=getAllPropertiesFromDB(companyId);
			}
		}
		catch (Exception e) {
			log.severe("Exception in MemcacheUtil's getAllPropertiesList");
			propertyObjList=getAllPropertiesFromDB(companyId);
		}
		return propertyObjList;
	}
	
	public static List<PropertyObj> getAllPropertiesFromDB(String companyId){
		List<PropertyObj> propertyObjList=new ArrayList<PropertyObj>(0);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		String key = ALL_PROPERTIES+companyId;
	      try {
	    	  propertyObjList=userDetailsDAO.getAllPropertyObjByCompanyId(companyId);
				if(propertyObjList !=null){
					memcache.put(key, propertyObjList);
					log.warning("PropertyObj List Size: "+propertyObjList.size());
				}
	      }catch (Exception e) {	
	    	    log.severe("getAllCompanyFromDB :"+e.getMessage());
				
		  }
	    
	      return propertyObjList;
	}
	
	public static boolean selfUpdateMemcachePropertyObj(PropertyObj propertyObj, boolean addNewObject){
		List<PropertyObj> objList = new ArrayList<PropertyObj>();
		boolean result = false;
		try {
			if(propertyObj != null) {
				String key = ALL_PROPERTIES + propertyObj.getCompanyId();
				objList = getAllPropertiesList(propertyObj.getCompanyId());
				if(addNewObject) {			// save new object
					if(objList == null) {	// if Datastore is empty
						objList = new ArrayList<PropertyObj>();
					}
					objList.add(propertyObj);
					result = true;
				}
				else {						// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (PropertyObj obj : objList) {
							if(obj.getId().equals(propertyObj.getId())) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, propertyObj);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				if(result) {
					Collections.sort(objList);
					if(memcache !=null && memcache.contains(key)){
						memcache.delete(key);
					}
					memcache.put(key, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in selfUpdateMemcachePropertyObj of MemcacheUtil: "+e.getMessage());
			
			result = false;
		}
		return result;
	}
	
	
	public static List<AdvertiserPerformerDTO> getAdvertiserTotalDataList(String startDate,String endDate, String publisherName,String advertiser,String agency,String properties){
		log.info("within getAdvertiserTotalDataList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=ADVERTISER_TOTAL_DATA+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Total Advertiser Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getAdvertiserTotalDataList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getAdvertiserTotalDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setAdvertiserTotalDataList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setAdvertiserTotalDataList() of MemcacheUtil class");
    	String key=ADVERTISER_TOTAL_DATA+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Total Advertiser Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setAdvertiserTotalDataList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setAdvertiserTotalDataList() of MemcacheUtil class, memcache.put() called..."+key);
	}
  
	public static List<AdvertiserPerformerDTO> getDeliveryIndicatorData(String startDate,String endDate, String publisherName,String advertiser,String agency,String properties){
		log.info("within getDeliveryIndicatorData() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=DELIVERY_INDICATOR+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Delivery Indicator Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getDeliveryIndicatorData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getDeliveryIndicatorData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setDeliveryIndicatorData(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
    	log.info("within setDeliveryIndicatorData() of MemcacheUtil class");
    	String key=DELIVERY_INDICATOR+"_"+publisherName+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Delivery Indicator Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setDeliveryIndicatorData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setDeliveryIndicatorData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    public static List<AdvertiserPerformerDTO> getRichMediaAdvertiserTotalDataList(String startDate,String endDate){
		log.info("within getRichMediaAdvertiserTotalDataList() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=RICH_MEDIA_ADVERTISER_TOTAL_DATA+"_"+startDate+"_"+endDate;
    	log.warning("Rich Media Total Advertiser Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getRichMediaAdvertiserTotalDataList() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getRichMediaAdvertiserTotalDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaAdvertiserTotalDataList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate){
    	log.info("within setRichMediaAdvertiserTotalDataList() of MemcacheUtil class");
    	String key=RICH_MEDIA_ADVERTISER_TOTAL_DATA+"_"+startDate+"_"+endDate;
    	log.warning("Rich Media Total Advertiser Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setRichMediaAdvertiserTotalDataList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setRichMediaAdvertiserTotalDataList() of MemcacheUtil class, memcache.put() called..."+key);
	}
  
	public static List<AdvertiserPerformerDTO> getRichMediaDeliveryIndicatorData(String startDate,String endDate,String advertiser,String agency,String properties){
		log.info("within getRichMediaDeliveryIndicatorData() of MemcacheUtil class");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=RICH_MEDIA_DELIVERY_INDICATOR+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Delivery Indicator Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getRichMediaDeliveryIndicatorData() of MemcacheUtil class key = "+key);
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("within getRichMediaDeliveryIndicatorData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaDeliveryIndicatorData(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String advertiser,String agency,String properties){
    	log.info("within setRichMediaDeliveryIndicatorData() of MemcacheUtil class");
    	String key=RICH_MEDIA_DELIVERY_INDICATOR+"_"+startDate+"_"+endDate+"_"+advertiser+"_"+agency+"_"+properties;
    	log.warning("Rich Media Delivery Indicator Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setRichMediaDeliveryIndicatorData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
    	log.info("within setRichMediaDeliveryIndicatorData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    public static List<AdvertiserPerformerDTO> getRichMediaEventPopupList(String startDate,String endDate, String lineItemName){
		log.info("In MemcacheUtil getRichMediaEventPopupList()");
    	List<AdvertiserPerformerDTO> resultList=null;
    	String key=RICH_MEDIA_EVENT_POPUP+"_"+startDate+"_"+endDate+"_"+lineItemName;
    	log.warning("Rich Media Event Popup key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("Rich Media Event Popup resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaEventPopupList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate, String lineItemName){
    	log.info("In MemcacheUtil setRichMediaEventPopupList()");
    	String key=RICH_MEDIA_EVENT_POPUP+"_"+startDate+"_"+endDate+"_"+lineItemName;
    	log.warning("Rich Media Popup key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
	}
    
    public static List<ReconciliationDataDTO> getReconciliationDataList(String startDate,String endDate, String publisherBQId, String hashedPassbackValueString){
		log.info("In MemcacheUtil getReconciliationDataList");
    	List<ReconciliationDataDTO> resultList=null;
    	String key=RECONCILIATION_DATA+"_"+startDate+"_"+endDate+"_"+publisherBQId+"_"+hashedPassbackValueString;
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<ReconciliationDataDTO>) memcache.get(key);
    		log.info("Memcache getReconciliationDataList size = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setReconciliationDataList(List<ReconciliationDataDTO> dataList,String startDate,String endDate, String publisherBQId, String hashedPassbackValueString){
    	log.info("In MemcacheUtil setReconciliationDataList");
    	String key=RECONCILIATION_DATA+"_"+startDate+"_"+endDate+"_"+publisherBQId+"_"+hashedPassbackValueString;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
	}
    
    public static List<AdvertiserPerformerDTO> getRichMediaEventGraphList(String lowerDate, String upperDate,String advertiser, String agency){
		log.info("In MemcacheUtil getRichMediaEventGraphList");
		List<AdvertiserPerformerDTO> resultList=null;
    	String key=RICH_MEDIA_EVENT_GRAPH+"_"+lowerDate+"_"+upperDate+"_"+advertiser+"_"+agency;
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("Memcache getRichMediaEventGraphList size = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setRichMediaEventGraphList(List<AdvertiserPerformerDTO> dataList,String lowerDate, String upperDate,String advertiser, String agency){
    	log.info("In MemcacheUtil setRichMediaEventGraphList");
    	String key=RICH_MEDIA_EVENT_GRAPH+"_"+lowerDate+"_"+upperDate+"_"+advertiser+"_"+agency;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
	}
    
    public static List<AdvertiserPerformerDTO> getRichMediaEventGraphList(String campaignId, String publisherId){
		List<AdvertiserPerformerDTO> resultList=null;
    	String key=RICH_MEDIA_EVENT_GRAPH+"-"+campaignId+publisherId;
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
    		log.info("Memcache getRichMediaEventGraphList size = "+resultList.size());
    	}
    	return resultList;
	}
    
    public static void  setRichMediaEventGraphList(List<AdvertiserPerformerDTO> dataList,String campaignId, String publisherId){
    	String key=RICH_MEDIA_EVENT_GRAPH+"-"+campaignId+publisherId;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
	}
    
    
    /*
     * Get all geo targets(DMA from memcache)
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getGeoTargetsFromCache(){
		Map<String,String> dataMap=null;    	
    	if(memcache !=null && memcache.contains(ALL_GEO_TARGETS_KEY)){
    		dataMap=(Map<String,String>) memcache.get(ALL_GEO_TARGETS_KEY);
    		log.info("Memcache getGeoTargetsFromCache size = "+dataMap.size());
    	}    	
    	return dataMap;
	}
    
    /*
     * Set all geo targets(DMA ) in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setGeoTargetsInCache(Map<String,String> dataMap){    	
    	if(memcache !=null && memcache.contains(ALL_GEO_TARGETS_KEY)){
    		memcache.delete(ALL_GEO_TARGETS_KEY);
    	}
    	memcache.put(ALL_GEO_TARGETS_KEY, dataMap);
	}
    
    /*
     * Get all KPIs from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getKPIsFromCache(){
		Map<String,String> dataMap=null;
    	
    	if(memcache !=null && memcache.contains(ALL_KPI_KEY)){
    		dataMap=(Map<String,String>) memcache.get(ALL_KPI_KEY);
    		log.info("Memcache getKPIsFromCache size = "+dataMap.size());
    	}    	
    	return dataMap;
	}
    
    /*
     * Set all KPIs in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setKPIsInCache(Map<String,String> dataMap){    	
    	if(memcache !=null && memcache.contains(ALL_KPI_KEY)){
    		memcache.delete(ALL_KPI_KEY);
    	}
    	memcache.put(ALL_KPI_KEY, dataMap);
	}
    
    
    /*
     * Get all industries from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getIndustriesFromCache(){
		Map<String,String> dataMap=null;
    	
    	if(memcache !=null && memcache.contains(ALL_INDUSTRIES_KEY)){
    		dataMap=(Map<String,String>) memcache.get(ALL_INDUSTRIES_KEY);
    		log.info("Memcache getIndustriesFromCache size = "+dataMap.size());
    	}    	
    	return dataMap;
	}
    
    /*
     * Set all industries in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setIndustriesInCache(Map<String,String> dataMap){    	
    	if(memcache !=null && memcache.contains(ALL_INDUSTRIES_KEY)){
    		memcache.delete(ALL_INDUSTRIES_KEY);
    	}
    	memcache.put(ALL_INDUSTRIES_KEY, dataMap);
	}
    
    
    
    public static List<CommonDTO> getAllRichMediaEvents(String date){
		log.info("In MemcacheUtil getAllRichMediaEvents");
		List<CommonDTO> resultList=null;
    	String key=ALL_RICH_MEDIA_EVENTS+"_"+date;
    	if(memcache !=null && memcache.contains(key)){
    		resultList=(List<CommonDTO>) memcache.get(key);
    		log.info("Memcache getAllRichMediaEvents size = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setAllRichMediaEvents(List<CommonDTO> dataList,String date){
    	log.info("In MemcacheUtil setAllRichMediaEvents");
    	String key=ALL_RICH_MEDIA_EVENTS+"_"+date;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
	}


    /*
     * Get a proposal from memcache by id
     * @author Youdhveer Panwar
     * @param String - (proposalId)
     * @return ProposalDTO
     */
    public static ProposalDTO getProposalFromCache(String proposalId){
		log.info("In MemcacheUtil getProposalFromCache");
		ProposalDTO proposalDTO=null;
    	String proposalKey=PROPOSAL_KEY+"_"+proposalId;
    	if(memcache !=null && memcache.contains(proposalKey)){
    		proposalDTO=(ProposalDTO) memcache.get(proposalKey);
    	}    	
    	return proposalDTO;
	}
    
    /*
     * Set a proposal in memcache by id
     * @author Youdhveer Panwar
     * @param ProposalDTO
     */
    public static void setProposalInCache(ProposalDTO proposalDTO){
    	String proposalKey=PROPOSAL_KEY+"_"+proposalDTO.getProposalId();
		log.info("set proposal in memcache : memcache key:"+proposalKey);
			
		if(memcache !=null && memcache.contains(proposalKey)){
    		memcache.delete(proposalKey);
    	}
    	memcache.put(proposalKey, proposalDTO);
	}
    
    
    /*
     * Get all proposals from memcache by userId
     * @author Youdhveer Panwar
     * @param String - (proposalId)
     * 
     * @return List<ProposalObj> proposalDTOList
     */
    public static List<ProposalObj> getAllProposalsFromCache(String userId){
		
		List<ProposalObj> proposalList=null;
		String proposalKey=PROPOSAL_KEY+"_all_user_"+userId;
    	if(memcache !=null && memcache.contains(proposalKey)){
    		proposalList=(List<ProposalObj>) memcache.get(proposalKey);
    	}   
    	log.info("In MemcacheUtil getAllProposalsFromCache ::"+proposalKey);
    	return proposalList;
	}
    
    /*
     * Set all proposals in memcache by userId
     * @author Youdhveer Panwar
     * 
     * @param List<ProposalObj> proposalDTOList,
     *        String - (proposalId)
     */
    public static void setAllProposalsInCache(List<ProposalObj> proposalList,String userId){
    	String proposalKey=PROPOSAL_KEY+"_all_user_"+userId;
		log.info("set all proposals in memcache : memcache key:"+proposalKey);			
		if(memcache !=null && memcache.contains(proposalKey)){
    		memcache.delete(proposalKey);
    	}
    	memcache.put(proposalKey, proposalList);
	}
    
    
    /*
     * Get all advertisers from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getAdvertisersFromCache(){
		log.info("getAdvertisersFromCache..");
		Map<String,String> advertiserMap=null;    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_ADVERTISERS_KEY)){
    		advertiserMap=(Map<String,String>) memcache.get(MEDIA_PLAN_ADVERTISERS_KEY);
    	}    	
    	return advertiserMap;
	}
    
    /*
     * Set all advertisers in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void setAdvertisersInCache(Map<String,String> advertiserMap){    	
		log.info("setAdvertisersInCache : memcache key:"+MEDIA_PLAN_ADVERTISERS_KEY);			
		if(memcache !=null && memcache.contains(MEDIA_PLAN_ADVERTISERS_KEY)){
    		memcache.delete(MEDIA_PLAN_ADVERTISERS_KEY);
    	}
    	memcache.put(MEDIA_PLAN_ADVERTISERS_KEY, advertiserMap);
	}
    
    /*
     * Get all agencies from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getAgenciesFromCache(){
		log.info("getAgenciesFromCache..");
		Map<String,String> agencyMap=null;    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_AGENCIES_KEY)){
    		agencyMap=(Map<String,String>) memcache.get(MEDIA_PLAN_AGENCIES_KEY);
    	}    	
    	return agencyMap;
	}
    
    /*
     * Set all agencies in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void setAgenciesInCache(Map<String,String> agencyMap){    	
		log.info("setAgenciesInCache : memcache key:"+MEDIA_PLAN_AGENCIES_KEY);			
		if(memcache !=null && memcache.contains(MEDIA_PLAN_AGENCIES_KEY)){
    		memcache.delete(MEDIA_PLAN_AGENCIES_KEY);
    	}
    	memcache.put(MEDIA_PLAN_AGENCIES_KEY, agencyMap);
	}
    
    
    /*
     * Get all orderIds from memcache on the basis of networkCode
     * @author Youdhveer Panwar
     * 
     * @return List<Long> -- orderIdList
     * @param String - networkCode
     */
    public static List<Long> getOrderIdsFromCache(String networkCode){		
		String key=DFP_ORDER_ID_KEY+"_"+networkCode;
		log.info("getOrderIdsFromCache..key:"+key);
		List<Long> orderIdList=null;    	
    	if(memcache !=null && memcache.contains(key)){
    		orderIdList=(List<Long>) memcache.get(key);
    	}    	
    	return orderIdList;
	}
    
    /*
     * Set all orderIds in memcache
     * @author Youdhveer Panwar
     * 
     * @param List<Long> -- orderIdList
     *        String -- networkCode
     */
    public static void setOrderIdsInCache(List<Long> orderIdList,String networkCode){    	
		
		String key=DFP_ORDER_ID_KEY+"_"+networkCode;
		log.info("setOrderIdsInCache : memcache key:"+key);	
		if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, orderIdList);
	}
    
    
    /*
     * Get all Companies from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
   /* public static Map<String,String> getCompaniesFromCache(){
		log.info("getCompaniesFromCache..");
		Map<String,String> companyMap=null;    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_COMPANY_KEY)){
    		companyMap=(Map<String,String>) memcache.get(MEDIA_PLAN_COMPANY_KEY);
    	}    	
    	return companyMap;
	}*/
    
    /*
     * Set all Companies in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    /*public static void setCompaniesInCache(Map<String,String> companyMap){    	
		log.info("setCompaniesInCache : memcache key:"+MEDIA_PLAN_COMPANY_KEY);			
		if(memcache !=null && memcache.contains(MEDIA_PLAN_COMPANY_KEY)){
    		memcache.delete(MEDIA_PLAN_COMPANY_KEY);
    	}
    	memcache.put(MEDIA_PLAN_COMPANY_KEY, companyMap);
	}*/
    
    
    /*
     * Get all SiteForecastedData from memcache
     * @author Youdhveer Panwar
     * @param String - siteKey(name_startDate_endDate)
     * @return Map<String,String>
     */
    public static Map<String,String> getSiteForecastedDataFromCache(String siteKey){		
		String key=MEDIA_PLAN_SITE_FORECAST_DATA_KEY+"_"+siteKey;
		log.info("getSiteForecastedDataInCache..key:"+key);
		Map<String,String> forecastedMap=null;    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_COMPANY_KEY)){
    		forecastedMap=(Map<String,String>) memcache.get(MEDIA_PLAN_COMPANY_KEY);
    	}    	
    	return forecastedMap;
	}
    
    /*
     * Set SiteForecastedData in memcache
     * @author Youdhveer Panwar
     * @param String - siteKey(name_startDate_endDate)
     * @param Map<String,String>
     */
    public static void setSiteForecastedDataInCache(Map<String,String> forecastedMap,String siteKey){
    	String key=MEDIA_PLAN_SITE_FORECAST_DATA_KEY+"_"+siteKey;
		log.info("setSiteForecastedDataInCache : memcache key:"+key);			
		if(memcache !=null && memcache.contains(MEDIA_PLAN_COMPANY_KEY)){
    		memcache.delete(MEDIA_PLAN_COMPANY_KEY);
    	}
    	memcache.put(MEDIA_PLAN_COMPANY_KEY, forecastedMap);
	}
    
	public static List<LineItemDTO> getCampaignTraffickingData(){
		log.info("within getCampaignTraffickingData() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=CAMPAIGN_TRAFFICKING_DATA;
    	log.warning("Campaign Trafficking Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getCampaignTraffickingData() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		log.info("within getCampaignTraffickingData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setCampaignTraffickingData(List<LineItemDTO> campaignTraffickingDataList){
    	log.info("within setCampaignTraffickingData() of MemcacheUtil class");
    	String key=CAMPAIGN_TRAFFICKING_DATA;
    	log.warning("Campaign Trafficking Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setCampaignTraffickingData() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setCampaignTraffickingData() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<ForcastLineItemDTO> getLineItemForcasts(String IdsList){
		log.info("within getLineItemForcasts() of MemcacheUtil class");
    	List<ForcastLineItemDTO> resultList=null;
    	String key=CAMPAIGN_TRAFFICKING_FORECASTED_DATA+"_"+IdsList;
    	log.warning("Campaign Trafficking Forecasted Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getLineItemForcasts() of MemcacheUtil class key = "+key);
    		resultList=(List<ForcastLineItemDTO>) memcache.get(key);
    		log.info("within getLineItemForcasts() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void setLineItemForcasts(List<ForcastLineItemDTO> forcastLineItemDTOList,String IdsList){
    	log.info("within setLineItemForcasts() of MemcacheUtil class");
    	String key=CAMPAIGN_TRAFFICKING_FORECASTED_DATA+"_"+IdsList;
    	log.warning("Campaign Trafficking Forecasted Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setLineItemForcasts() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, forcastLineItemDTOList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setLineItemForcasts() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    
    /*
     * Get all AdSize from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getAdSizeFromCache(){
		Map<String,String> dataMap=null;    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_ADSIZE_KEY)){
    		dataMap=(Map<String,String>) memcache.get(MEDIA_PLAN_ADSIZE_KEY);
    	}    	
    	return dataMap;
	}
    
    /*
     * Set all AdSize in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setAdSizeInCache(Map<String,String> dataMap){    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_ADSIZE_KEY)){
    		memcache.delete(MEDIA_PLAN_ADSIZE_KEY);
    	}
    	memcache.put(MEDIA_PLAN_ADSIZE_KEY, dataMap);
	}
    
    /*
     * Get all AdFormat from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getAdFormatFromCache(){
		Map<String,String> dataMap=null;
    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_ADFORMAT_KEY)){
    		dataMap=(Map<String,String>) memcache.get(MEDIA_PLAN_ADFORMAT_KEY);
    	}    	
    	return dataMap;
	}
    
    /*
     * Set all AdFormat in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setAdFormatInCache(Map<String,String> dataMap){    	
    	if(memcache !=null && memcache.contains(MEDIA_PLAN_ADFORMAT_KEY)){
    		memcache.delete(MEDIA_PLAN_ADFORMAT_KEY);
    	}
    	memcache.put(MEDIA_PLAN_ADFORMAT_KEY, dataMap);
	}
    
    
    /*
     * Get line chart map by order id from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getLineChartMapFromCache(String orderId, boolean isNoise, double threshold){
		Map<String,String> dataMap=null;
		String key=LINE_CHART_KEY+"_"+orderId+isNoise+threshold;
    	if(memcache !=null && memcache.contains(key)){
    		dataMap=(Map<String,String>) memcache.get(key);
    	}    	
    	return dataMap;
	}
    
    /*
     * Set line chart data by order id in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setLineChartMapInCache(Map<String,String> dataMap,String orderId, boolean isNoise, double threshold){
    	String key=LINE_CHART_KEY+"_"+orderId+isNoise+threshold;
    	if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, dataMap,Expiration.byDeltaSeconds(3600));
	}
    
    public static List<AdvertiserPerformerDTO> getPublisherCampaignTotalDataList(String startDate,String endDate, String publisherName,String advertiser,String agency,String properties){
        log.info("within getAdvertiserTotalDataList() of MemcacheUtil class");
        List<AdvertiserPerformerDTO> resultList=null;
        String key=PUBLISHER_TOTAL_DATA+"_"+startDate+"_"+endDate;
        //log.warning("Total Advertiser Data key :"+key);
        if(memcache !=null && memcache.contains(key)){
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class key = "+key);
        resultList=(List<AdvertiserPerformerDTO>) memcache.get(key);
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
        }

        return resultList;
    }

    public static void  setPublisherCampaignTotalDataList(List<AdvertiserPerformerDTO> dataList,String startDate,String endDate,String publisherName,String advertiser,String agency,String properties){
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class");
        String key=PUBLISHER_TOTAL_DATA+"_"+startDate+"_"+endDate;
        //log.warning("Total Advertiser Data key :"+key);
        if(memcache !=null && memcache.contains(key)){
        log.info("within setPublisherCampaignTotalDataList of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
        memcache.delete(key);
        }
        memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class, memcache.put() called..."+key);
    }
    
    public static List<PublisherPropertiesObj> getAllPerformanceByPropertyCalculatedList(String startDate,String endDate,String publisherId, String channelIds, String DFP_Properties){
        log.info("within getAllPerformanceByPropertyList() of MemcacheUtil class");
        List<PublisherPropertiesObj> resultList=null;
        String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherId+channelIds+DFP_Properties);
        String key=PUBLISHER_PERFORMANCE_BY_PROPERTY_CALCULATED+"_"+hashedKeyPart;
        if(memcache !=null && memcache.contains(key)){
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class key = "+key);
        resultList=(List<PublisherPropertiesObj>) memcache.get(key);
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
        }

        return resultList;
    }

    public static void  setAllPerformanceByPropertyCalculatedList(List<PublisherPropertiesObj> dataList,String startDate,String endDate,String publisherId, String channelIds, String DFP_Properties){
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class");
        String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherId+channelIds+DFP_Properties);
        String key=PUBLISHER_PERFORMANCE_BY_PROPERTY_CALCULATED+"_"+hashedKeyPart;
        log.warning("key value : "+key+", key length : "+key.length());
        //log.warning("Total Advertiser Data key :"+key);
        if(memcache !=null && memcache.contains(key)){
        log.info("within setPublisherCampaignTotalDataList of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
        memcache.delete(key);
        }
        memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class, memcache.put() called..."+key);
    }
    
    public static List<PublisherPropertiesObj> getAllPerformanceByPropertyList(String startDate,String endDate,String publisherId, String channelIds, String DFP_Properties){
        log.info("within getAllPerformanceByPropertyList() of MemcacheUtil class");
        List<PublisherPropertiesObj> resultList=null;
        String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherId+channelIds+DFP_Properties);
        String key=PUBLISHER_PERFORMANCE_BY_PROPERTY+"_"+hashedKeyPart;
        if(memcache !=null && memcache.contains(key)){
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class key = "+key);
        resultList=(List<PublisherPropertiesObj>) memcache.get(key);
        //log.info("within getAdvertiserTotalDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
        }

        return resultList;
    }

    public static void  setAllPerformanceByPropertyList(List<PublisherPropertiesObj> dataList,String startDate,String endDate,String publisherId, String channelIds, String DFP_Properties){
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class");
        String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+publisherId+channelIds+DFP_Properties);
        String key=PUBLISHER_PERFORMANCE_BY_PROPERTY+"_"+hashedKeyPart;
        log.warning("key value : "+key+", key length : "+key.length());
        if(memcache !=null && memcache.contains(key)){
        log.info("within setPublisherCampaignTotalDataList of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
        memcache.delete(key);
        }
        memcache.put(key, dataList, Expiration.byDeltaSeconds(expireInDay));
        log.info("within setAdvertiserTotalDataList() of MemcacheUtil class, memcache.put() called..."+key);
    }

	public static List<LineItemDTO> getDeliveringCampaignList(String startDate,String endDate){
		log.info("within getDeliveringCampaignList() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=DELIVERING_CAMPAIGN_DATA+startDate+endDate;
    	log.warning("Delivering Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getDeliveringCampaignList() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getDeliveringCampaignList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setDeliveringCampaignList(List<LineItemDTO> campaignTraffickingDataList,String startDate,String endDate){
    	log.info("within setDeliveringCampaignList() of MemcacheUtil class");
    	String key=DELIVERING_CAMPAIGN_DATA+startDate+endDate;
    	log.warning("Delivering Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setDeliveringCampaignList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setDeliveringCampaignList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<LineItemDTO> getReadyCampaignList(){
		log.info("within getReadyCampaignList() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=READY_CAMPAIGN_DATA;
    	log.warning("Ready Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getReadyCampaignList() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getReadyCampaignList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setReadyCampaignList(List<LineItemDTO> campaignTraffickingDataList){
    	log.info("within setReadyCampaignList() of MemcacheUtil class");
    	String key=READY_CAMPAIGN_DATA;
    	log.warning("Ready Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setReadyCampaignList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setReadyCampaignList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<LineItemDTO> getDraftAndNeedCreativesCampaignList(){
		log.info("within getDraftAndNeedCreativesCampaignList() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=DRAFT_NEEDCREATIVES_CAMPAIGN_DATA;
    	log.warning("Draft and Need Creatives Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getDraftAndNeedCreativesCampaignList() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getDraftAndNeedCreativesCampaignList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setDraftAndNeedCreativesCampaignList(List<LineItemDTO> campaignTraffickingDataList){
    	log.info("within setReadyCampaignList() of MemcacheUtil class");
    	String key=DRAFT_NEEDCREATIVES_CAMPAIGN_DATA;
    	log.warning("Draft and Need Creatives Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setDraftAndNeedCreativesCampaignList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setDraftAndNeedCreativesCampaignList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<LineItemDTO> getPausedAndInventoryReleasedCampaignList(){
		log.info("within getPausedAndInventoryReleasedCampaignList() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=PAUSED_INVENTORYRELEASED_CAMPAIGN_DATA;
    	log.warning("Paused And Inventory Released Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getPausedAndInventoryReleasedCampaignList() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getPausedAndInventoryReleasedCampaignList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setPausedAndInventoryReleasedCampaignList(List<LineItemDTO> campaignTraffickingDataList){
    	log.info("within setPausedAndInventoryReleasedCampaignList() of MemcacheUtil class");
    	String key=PAUSED_INVENTORYRELEASED_CAMPAIGN_DATA;
    	log.warning("Paused And Inventory Released Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setPausedAndInventoryReleasedCampaignList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
    	log.info("within setPausedAndInventoryReleasedCampaignList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
	public static List<LineItemDTO> getPendingApprovalCampaignList(){
		log.info("within getPendingApprovalCampaignList() of MemcacheUtil class");
    	List<LineItemDTO> resultList=null;
    	String key=PENDING_APPROVAL_CAMPAIGN_DATA;
    	log.warning("Pending Approval Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getPendingApprovalCampaignList() of MemcacheUtil class key = "+key);
    		resultList=(List<LineItemDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getPendingApprovalCampaignList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setPendingApprovalCampaignList(List<LineItemDTO> campaignTraffickingDataList){
    	log.info("within setPausedAndInventoryReleasedCampaignList() of MemcacheUtil class");
    	String key=PENDING_APPROVAL_CAMPAIGN_DATA;
    	log.warning("Pending Approval Campaign Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setPendingApprovalCampaignList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, campaignTraffickingDataList, Expiration.byDeltaSeconds(expiredInTwoHours));
	}
	
    /*
     * Get data map by key from memcache
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public static Map<String,String> getDataMapFromCacheByKey(String memcacheKey){
		Map<String,String> dataMap=null;
    	if(memcache !=null && memcache.contains(memcacheKey)){
    		dataMap=(Map<String,String>) memcache.get(memcacheKey);
    	}    	
    	return dataMap;
	}
    
    /*
     * Set data map by key in memcache
     * @author Youdhveer Panwar
     * @param Map<String,String>
     */
    public static void  setDataMapInCacheByKey(Map<String,String> dataMap,String memcacheKey){
    	if(memcache !=null && memcache.contains(memcacheKey)){
    		memcache.delete(memcacheKey);
    	}
    	memcache.put(memcacheKey, dataMap,Expiration.byDeltaSeconds(3600));
	}
    
	/*
     * Get data list by key from memcache
     * @author Youdhveer Panwar
     * @return List<String> 
     */
    public static List<String> getDataListFromCacheByKey(String memcacheKey){
    	List<String> dataList=null;
    	if(memcache !=null && memcache.contains(memcacheKey)){
    		dataList=(List<String>) memcache.get(memcacheKey);
    	}    	
    	return dataList;
	}
    
    /*
     * Set data list by key in memcache
     * @author Youdhveer Panwar
     * @param List<String> 
     */
    public static void  setDataListInCacheByKey(List<String> dataList,String memcacheKey){
    	if(memcache !=null && memcache.contains(memcacheKey)){
    		memcache.delete(memcacheKey);
    	}
    	if(dataList!=null){
    		memcache.put(memcacheKey, dataList,Expiration.byDeltaSeconds(3600));
    	}
    	
	}
    
    public static List<NewAdvertiserViewDTO> getdeliveryMetricsData(String orderId){
		log.info("within getdeliveryMetricsData() of MemcacheUtil class");
    	List<NewAdvertiserViewDTO> resultList=null;
    	String key=DELIVERY_METRIC_DATA_KEY+"_"+orderId;
    	log.info("Delivery Metrics Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getdeliveryMetricsData() of MemcacheUtil class key = "+key);
    		resultList=(List<NewAdvertiserViewDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getdeliveryMetricsData() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setdeliveryMetricsDataList(List<NewAdvertiserViewDTO> deliveryMetricsDataList,String orderId){
    	log.info("within setdeliveryMetricsDataList() of MemcacheUtil class");
    	String key=DELIVERY_METRIC_DATA_KEY+"_"+orderId;
    	log.info("Delivery Metrics Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setdeliveryMetricsDataList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, deliveryMetricsDataList, Expiration.byDeltaSeconds(60*60));
    	log.info("within setdeliveryMetricsDataList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    public static List<NewAdvertiserViewDTO> getAdvertiserHeaderDataList(String orderId){
		log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class");
    	List<NewAdvertiserViewDTO> resultList=null;
    	String key=ADVERTISER_HEADER_DATA_KEY+"_"+orderId;
    	log.info("Hader Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class key = "+key);
    		resultList=(List<NewAdvertiserViewDTO>) memcache.get(key);
    		if(resultList != null)
    		log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
    	}
    	
    	return resultList;
	}
	
    public static void  setAdvertiserHeaderDataList(List<NewAdvertiserViewDTO> headerDataList,String orderId){
    	log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class");
    	String key=ADVERTISER_HEADER_DATA_KEY+"_"+orderId;
    	log.info("Hader Data key :"+key);
    	if(memcache !=null && memcache.contains(key)){
    		log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
    		memcache.delete(key);
    	}
    	memcache.put(key, headerDataList, Expiration.byDeltaSeconds(60*60));
    	log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class, memcache.put() called..."+key);
	}
    
    /*
     * Get all BQ advertisers from memcache
     * @author Naresh Pokhriyal
     * @return Map<String,String>
     */
    public static Map<String,String> getBQAdvertisersFromCache(String publisherIdsInBQ, String advertiserIds){
		Map<String,String> advertiserDFPMap=null;
		String hashedKeyPart = StringUtil.getHashedValue(publisherIdsInBQ+"_"+advertiserIds);
		String key = ADVERTISERS_BQ_KEY+"-"+hashedKeyPart;
    	if(memcache !=null && memcache.contains(key)){
    		advertiserDFPMap=(Map<String,String>) memcache.get(key);
    	}    	
    	return advertiserDFPMap;
	}
    
    /*
     * Set all BQ advertisers in memcache
     * @author Naresh Pokhriyal
     * @param Map<String,String>
     */
    public static void setBQAdvertisersInCache(Map<String,String> advertiserDFPMap, String publisherIdsInBQ, String advertiserIds){ 
    	String hashedKeyPart = StringUtil.getHashedValue(publisherIdsInBQ+"_"+advertiserIds);
		String key = ADVERTISERS_BQ_KEY+"-"+hashedKeyPart;
		log.info("setBQAdvertisersInCache : memcache key:"+key);			
		if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, advertiserDFPMap, Expiration.byDeltaSeconds(expiredInTwoHours));
	}
    
    /*
     * Get all BQ agencies from memcache
     * @author Naresh Pokhriyal
     * @return Map<String,String>
     */
    public static Map<String,String> getBQAgenciesFromCache(String publisherIdsInBQ, String agencyId){
		log.info("getBQAgenciesFromCache..");
		Map<String,String> agencyDFPMap=null;
		String hashedKeyPart = StringUtil.getHashedValue(publisherIdsInBQ+"_"+agencyId);
		String key = AGENCIES_BQ_KEY+"-"+hashedKeyPart;
    	if(memcache !=null && memcache.contains(key)){
    		agencyDFPMap=(Map<String,String>) memcache.get(key);
    	}    	
    	return agencyDFPMap;
	}
    
    /*
     * Set all BQ agencies in memcache
     * @author Naresh Pokhriyal
     * @param Map<String,String>
     */
    public static void setBQAgenciesInCache(Map<String,String> agencyDFPMap, String publisherIdsInBQ, String agencyId){
    	String hashedKeyPart = StringUtil.getHashedValue(publisherIdsInBQ+"_"+agencyId);
		String key = AGENCIES_BQ_KEY+"-"+hashedKeyPart;
		log.info("setBQAgenciesInCache : memcache key:"+key);			
		if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, agencyDFPMap, Expiration.byDeltaSeconds(expiredInTwoHours));
	}

	public static void setPlacementsInCache(List<PlacementObj> placementObjList, String proposalId) {
		String key = MEDIA_PLAN_PLACEMENT_KEY+"-"+proposalId;
		log.info("setPlacements : memcache key:"+key);			
		if(memcache !=null && memcache.contains(key)){
    		memcache.delete(key);
    	}
    	memcache.put(key, placementObjList);
	}
	
	public static List<PlacementObj> getPlacementsFromCache(String proposalId) {
		String key = MEDIA_PLAN_PLACEMENT_KEY+"-"+proposalId;
		List<PlacementObj> placementObjList = null;
    	if(memcache !=null && memcache.contains(key)){
    		placementObjList=(List<PlacementObj>) memcache.get(key);
    	}    	
    	return placementObjList;
	}
    
	 public static List<NewAdvertiserViewDTO> getAdvertiserSummaryReportDataList(String orderId){
			log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class");
	    	List<NewAdvertiserViewDTO> resultList=null;
	    	String key=ADVERTISER_HEADER_DATA_KEY+"_"+orderId;
	    	log.info("Hader Data key :"+key);
	    	if(memcache !=null && memcache.contains(key)){
	    		log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class key = "+key);
	    		resultList=(List<NewAdvertiserViewDTO>) memcache.get(key);
	    		if(resultList != null)
	    		log.info("within getAdvertiserHeaderDataList() of MemcacheUtil class, resultList.size() = "+resultList.size());
	    	}
	    	
	    	return resultList;
		}
		
	    public static void  setAdvertiserSummaryReportDataList(List<NewAdvertiserViewDTO> headerDataList,String orderId){
	    	log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class");
	    	String key=ADVERTISER_HEADER_DATA_KEY+"_"+orderId;
	    	log.info("Hader Data key :"+key);
	    	if(memcache !=null && memcache.contains(key)){
	    		log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class, memcache !=null && memcache.contains(key) "+key);
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, headerDataList, Expiration.byDeltaSeconds(60*60));
	    	log.info("within setAdvertiserHeaderDataList() of MemcacheUtil class, memcache.put() called..."+key);
		}
	   
	    public static String getClientAuthToken(String key){
			String authToken=null;		
	    	if(memcache !=null && memcache.contains(key)){
	    		authToken= (String) memcache.get(key);
	    	}    	
	    	return authToken;
		}
		
		public static void setClientAuthToken(String key,String clientAuthToken){			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, clientAuthToken,Expiration.byDeltaSeconds(60*60)); 
		} 
		
		/*
	     * Get all BQ Accounts from memcache
	     * @author Naresh Pokhriyal
	     * @return Map<String,String>
	     */
	    public static Map<String,String> getBQAccountsFromCache(String key){
			Map<String,String> accountDFPMap=null;
			key = ACCOUNTS_BQ_KEY+"-"+StringUtil.getHashedValue(key);
	    	if(memcache !=null && memcache.contains(key)){
	    		accountDFPMap=(Map<String,String>) memcache.get(key);
	    	}    	
	    	return accountDFPMap;
		}
	    
	    /*
	     * Set all BQ Accounts in memcache
	     * @author Naresh Pokhriyal
	     * @param Map<String,String>
	     */
	    public static void setBQAccountsInCache(Map<String,String> accountDFPMap, String key){ 
	    	key = ACCOUNTS_BQ_KEY+"-"+StringUtil.getHashedValue(key);
			log.info("setBQAccountsInCache : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, accountDFPMap, Expiration.byDeltaSeconds(expiredInTwoHours));
		}
	    
	    /*
	     * Get all DFP Accounts for particular network cerdentials from memcache
	     * @author Naresh Pokhriyal
	     * @return List<AccountDTO>
	     */
	    public static List<AccountDTO> getDFPAccountsFromCache(String key) {
	    	List<AccountDTO> accountDFPList = null;
			key = ACCOUNTS_DFP_KEY+"-"+key;
	    	if(memcache !=null && memcache.contains(key)){
	    		accountDFPList = (List<AccountDTO>) memcache.get(key);
	    	}    	
	    	return accountDFPList;
		}
	    
	    /*
	     * Set all DFP Accounts for particular network cerdentials in memcache
	     * @author Naresh Pokhriyal
	     * @param List<AccountDTO>
	     */
	    public static void setDFPAccountsInCache(List<AccountDTO> accountDFPList, String key){ 
	    	key = ACCOUNTS_DFP_KEY+"-"+key;
			log.info("setDFPAccountsInCache : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, accountDFPList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get all DFP Properties for particular network cerdentials from memcache
	     * @author Naresh Pokhriyal
	     * @return List<AccountDTO>
	     */
	    public static List<LineItemDTO> getDFPPropertiesFromCache(String key) {
	    	List<LineItemDTO> propertiesDFPList = null;
			key = PROPERTIES_DFP_KEY+"-"+key;
	    	if(memcache !=null && memcache.contains(key)){
	    		propertiesDFPList = (List<LineItemDTO>) memcache.get(key);
	    	}    	
	    	return propertiesDFPList;
		}
	    
	    /*
	     * Set all DFP Properties for particular network cerdentials in memcache
	     * @author Naresh Pokhriyal
	     * @param List<AccountDTO>
	     */
	    public static void setDFPPropertiesInCache(List<LineItemDTO> propertiesList, String key){ 
	    	key = PROPERTIES_DFP_KEY+"-"+key;
			log.info("setDFPPropertiesInCache : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, propertiesList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get all DFP Properties for particular network cerdentials from memcache
	     * @author Naresh Pokhriyal
	     * @return List<AccountDTO>
	     */
	    public static List<LineItemDTO> getAllDFPPropertiesFromCache(String key) {
	    	List<LineItemDTO> propertiesDFPList = null;
			key = ALL_PROPERTIES_DFP_KEY+"-"+key;
	    	if(memcache !=null && memcache.contains(key)){
	    		propertiesDFPList = (List<LineItemDTO>) memcache.get(key);
	    	}    	
	    	return propertiesDFPList;
		}
	    
	    /*
	     * Set all DFP Properties for particular network cerdentials in memcache
	     * @author Naresh Pokhriyal
	     * @param List<AccountDTO>
	     */
	    public static void setAllDFPPropertiesInCache(List<LineItemDTO> propertiesList, String key){ 
	    	key = ALL_PROPERTIES_DFP_KEY+"-"+key;
			log.info("setDFPPropertiesInCache : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, propertiesList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get all DFP Sites with DMA objects from memcache
	     * @author Shubham Goel
	     * @return List<DFPSitesWithDMAObj>
	     */
	    
	    public static List<DFPSitesWithDMAObj> getDFPSitesWithDMAObjs() {
	    	List<DFPSitesWithDMAObj> DFPSitesWithDMAObjList = new ArrayList<>();
	    	String key = DFP_SITES_WITH_DMA_OBJS_KEY;
	    	if(memcache !=null && memcache.contains(key)){
	    		DFPSitesWithDMAObjList = (List<DFPSitesWithDMAObj>) memcache.get(key);
	    		
	    	}
			return DFPSitesWithDMAObjList;
			
		}
	    
	    /*
	     * Set all all DFP Sites with DMA objects in memcache
	     * @author Shubham Goel
	     * @param List<DFPSitesWithDMAObj>
	     */
	    public static void setDFPSitesWithDMAObjs(List<DFPSitesWithDMAObj> DFPSitesWithDMAObjList){ 
	    	String key = DFP_SITES_WITH_DMA_OBJS_KEY;
			log.info("setDFPSitesWithDMAObjs : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, DFPSitesWithDMAObjList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get all  DMA with Inventery objects from memcache
	     * @author Shubham Goel
	     * @return List<DFPSitesWithDMAObj>
	     */
	    
	    public static List<ForcastInventoryObj> getAllDMAsWithInventory(String startDate, String endDate){ 
	    	List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
	    	String key = ALL_DMA_WITH_INVENTORY_KEY+startDate+endDate;
	    	if(memcache !=null && memcache.contains(key)){
	    		forcastInventoryObjList = (List<ForcastInventoryObj>) memcache.get(key);
	    	}
			return forcastInventoryObjList;
			
		}
	    
	    /*
	     * Set all  DMA with Inventery objects in memcache
	     * @author Shubham Goel
	     * @param List<DFPSitesWithDMAObj>
	     */
	    public static void setAllDMAsWithInventory(String startDate, String endDate, List<ForcastInventoryObj> forcastInventoryObjList){ 
	    	String key = ALL_DMA_WITH_INVENTORY_KEY+startDate+endDate;
			log.info("setAllDMAsWithInventory : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, forcastInventoryObjList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get all Allocated Inventery objects from memcache
	     * @author Shubham Goel
	     * @return List<ForcastInventoryObj>
	     */
	    
	    public static List<ForcastInventoryObj> getAllocateInventry(String startDate, String endDate ,String codeListstr){ 
	    	List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
	    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+codeListstr);
	    	String key=ALL_ALLOCATE_INVENTORY_KEY+"_"+hashedKeyPart;
	    	if(memcache !=null && memcache.contains(key)){
	    		forcastInventoryObjList = (List<ForcastInventoryObj>) memcache.get(key);
	    	}
			return forcastInventoryObjList;
			
		}
	    
	    /*
	     * Set all Allocated Inventery objects from memcache
	     * @author Shubham Goel
	     * @param List<ForcastInventoryObj>
	     */
	    public static void setAllocateInventry(String startDate, String endDate, String codeListstr, List<ForcastInventoryObj> forcastInventoryObjList){ 
	    	String hashedKeyPart = StringUtil.getHashedValue(startDate+endDate+codeListstr);
	    	String key=ALL_ALLOCATE_INVENTORY_KEY+"_"+hashedKeyPart;
			log.info("setAllocateInventry : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, forcastInventoryObjList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    
	    /*
	     * Get performance by os from memcache
	     * @author Shubham Goel
	     * @return List<NewAdvertiserViewDTO>
	     */
	    
	    public static List<PerformanceByOSReportDTO> getPerformanceByOS(String orderId, boolean isNoise, double threshold){ 
	    	List<PerformanceByOSReportDTO> performanceByOSList = new ArrayList<>();
	    	String key = PERFORMANCE_BY_OS_KEY+orderId+isNoise+threshold;
	    	if(memcache !=null && memcache.contains(key)){
	    		performanceByOSList = (List<PerformanceByOSReportDTO>) memcache.get(key);
	    	}
			return performanceByOSList;
			
		}
	    
	    /*
	     * Set all  performance by os objects in memcache
	     * @author Shubham Goel
	     * @param List<NewAdvertiserViewDTO>
	     */
	    public static void setPerformanceByOS(String orderId, boolean isNoise, double threshold, List<PerformanceByOSReportDTO> performanceByOSList){ 
	    	String key = PERFORMANCE_BY_OS_KEY+orderId+isNoise+threshold;
			log.info("setPerformanceByOS : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performanceByOSList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get performance by Device from memcache
	     * @author Shubham Goel
	     * @return List<NewAdvertiserViewDTO>
	     */
	    
	    public static List<PerformanceByDeviceReportDTO> getPerformanceByDevice(String orderId, boolean isNoise, double threshold){ 
	    	List<PerformanceByDeviceReportDTO> performanceByOSList = new ArrayList<>();
	    	String key = PERFORMANCE_BY_DEVICE_KEY+orderId+isNoise+threshold;
	    	if(memcache !=null && memcache.contains(key)){
	    		performanceByOSList = (List<PerformanceByDeviceReportDTO>) memcache.get(key);
	    	}
			return performanceByOSList;
			
		}
	    
	    /*
	     * Set all  performance by Device objects in memcache
	     * @author Shubham Goel
	     * @param List<NewAdvertiserViewDTO>
	     */
	    public static void setPerformanceByDevice(String orderId, boolean isNoise, double threshold, List<PerformanceByDeviceReportDTO> performanceByDeviceList){ 
	    	String key = PERFORMANCE_BY_DEVICE_KEY+orderId+isNoise+threshold;
			log.info("setPerformanceByOS : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performanceByDeviceList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get performance by Placement from memcache
	     * @author Shubham Goel
	     * @return List<NewAdvertiserViewDTO>
	     */
	    
	    public static List<PerformanceByPlacementReportDTO> getPerformanceByPlacement(String orderId, boolean isNoise, double threshold){ 
	    	List<PerformanceByPlacementReportDTO> performanceByPlacementList = new ArrayList<>();
	    	String key = PERFORMANCE_BY_PLACEMENT_KEY+orderId+isNoise;
	    	if(memcache !=null && memcache.contains(key)){
	    		performanceByPlacementList = (List<PerformanceByPlacementReportDTO>) memcache.get(key);
	    	}
			return performanceByPlacementList;
			
		}
	    
	    /*
	     * Set all  performance by Placement objects in memcache
	     * @author Shubham Goel
	     * @param List<NewAdvertiserViewDTO>
	     */
	    public static void setPerformanceByPlacement(String orderId, boolean isNoise,double threshold, List<PerformanceByPlacementReportDTO> performanceByPlacementList){ 
	    	String key = PERFORMANCE_BY_PLACEMENT_KEY+orderId+isNoise;
			log.info("setPerformanceByPlacement : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performanceByPlacementList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get performance by AdSize from memcache
	     * @author Shubham Goel
	     * @return List<NewAdvertiserViewDTO>
	     */
	    
	    public static List<PerformanceByAdSizeReportDTO> getPerformanceByAdSize(String orderId, boolean isNoise, double threshold){ 
	    	List<PerformanceByAdSizeReportDTO> performanceByAdSizeList = new ArrayList<>();
	    	String key = PERFORMANCE_BY_ADSIZE_KEY+orderId+isNoise+threshold;
	    	if(memcache !=null && memcache.contains(key)){
	    		performanceByAdSizeList = (List<PerformanceByAdSizeReportDTO>) memcache.get(key);
	    	}
			return performanceByAdSizeList;
			
		}
	    
	    /*
	     * Set all  performance by AdSize objects in memcache
	     * @author Shubham Goel
	     * @param List<NewAdvertiserViewDTO>
	     */
	    public static void setPerformanceByAdSize(String orderId, boolean isNoise, double threshold, List<PerformanceByAdSizeReportDTO> performanceByAdSizeList){ 
	    	String key = PERFORMANCE_BY_ADSIZE_KEY+orderId+isNoise+threshold;
			log.info("setPerformanceByAdSize : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performanceByAdSizeList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get mostActive advertiser object from memcache
	     * @author Shubham Goel
	     * @return List<NewAdvertiserViewDTO>
	     */
	    
	    public static List<MostActiveReportDTO> getMostActiveAdvertiserObj(String orderId){ 
	    	List<MostActiveReportDTO> mostActiveDataList = new ArrayList<>();
	    	String key = MOST_ACTIVE_ADVERTISER_KEY+orderId;
	    	if(memcache !=null && memcache.contains(key)){
	    		mostActiveDataList = (List<MostActiveReportDTO>) memcache.get(key);
	    	}
			return mostActiveDataList;
			
		}
	    
	    /*
	     * Set  mostActive advertiser  objects in memcache
	     * @author Shubham Goel
	     * @param List<NewAdvertiserViewDTO>
	     */
	    public static void setMostActiveAdvertiserObj(String orderId,List<MostActiveReportDTO> mostActiveDataList){ 
	    	String key = MOST_ACTIVE_ADVERTISER_KEY+orderId;
			log.info("setMostActiveAdvertiserObj : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, mostActiveDataList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get performancebyLocation advertiser object from memcache
	     * @author Shubham Goel
	     * @return List<PerformanceByLocationReportDTO>
	     */
	    
	    public static List<PerformanceByLocationReportDTO> getPerformanceByLocationAdvertiserObj(String orderId, boolean isNoise, boolean isReport, double threshold){ 
	    	List<PerformanceByLocationReportDTO> performanceByLocationList = new ArrayList<>();
	    	String key = PERFORMANCE_BY_LOCATION_KEY+orderId+isNoise+isReport+threshold;
	    	if(memcache !=null && memcache.contains(key)){
	    		performanceByLocationList = (List<PerformanceByLocationReportDTO>) memcache.get(key);
	    	}
			return performanceByLocationList;
			
		}
	    
	    /*
	     * Set  performancebyLocation advertiser  objects in memcache
	     * @author Shubham Goel
	     * @param List<PerformanceByLocationReportDTO>
	     */
	    public static void setPerformanceByLocationAdvertiserObj(String orderId, boolean isNoise, boolean isReport, double threshold, List<PerformanceByLocationReportDTO> performanceByLocationList){ 
	    	String key = PERFORMANCE_BY_LOCATION_KEY+orderId+isNoise+isReport+threshold;
			log.info("setPerformanceByLocationAdvertiserObj : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performanceByLocationList, Expiration.byDeltaSeconds(expireInDay));
		}
	    
	    /*
	     * Get performer advertiser object from memcache
	     * @author Shubham Goel
	     * @return List<PerformerReportDTO>
	     */
	    
	    public static List<PerformerReportDTO> getPerformerAdvertiserObj(String orderId){ 
	    	List<PerformerReportDTO> performerDataList = new ArrayList<>();
	    	String key = PERFORMER_ADVERTISER_OBJECT_KEY+orderId;
	    	if(memcache !=null && memcache.contains(key)){
	    		performerDataList = (List<PerformerReportDTO>) memcache.get(key);
	    	}
			return performerDataList;
			
		}
	    
	    /*
	     * Set  performer advertiser  objects in memcache
	     * @author Shubham Goel
	     * @param List<PerformerReportDTO>
	     */
	    public static void setPerformerAdvertiserObj(String orderId,List<PerformerReportDTO> performerDataList){ 
	    	String key = PERFORMER_ADVERTISER_OBJECT_KEY+orderId;
			log.info("setPerformerAdvertiserObj : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, performerDataList, Expiration.byDeltaSeconds(expireInDay));
		}
	   
	    /*
	     * Get trends and analysis actual data object for selected date range
	     * @author Shubham Goel
	     * @return List<PublisherChannelObj>
	     */
	    
	    public static List<PublisherChannelObj> getTrendsAnalysisActualDataPublisher(String lowerDate, String upperDate, String publisher, String channelName){ 
	    	List<PublisherChannelObj> actualDataPublisherList = new ArrayList<>();
	    	String hashedKeyPart = StringUtil.getHashedValue(lowerDate+upperDate+publisher+channelName);
	        String key=TREND_ANALYSIS_ACTUAL_DATA_PUBLISHER+"_"+hashedKeyPart;
	    	if(memcache !=null && memcache.contains(key)){
	    		actualDataPublisherList = (List<PublisherChannelObj>) memcache.get(key);
	    	}
			return actualDataPublisherList;
			
		}
	    
	    /*
	     * Set  trends and analysis actual data object for selected date range
	     * @author Shubham Goel
	     * @param List<PublisherChannelObj>
	     */
	    public static void setTrendsAnalysisActualDataPublisher(String lowerDate, String upperDate, String publisherId, String channelId,List<PublisherChannelObj> actualDataPublisherList){ 
	    	String hashedKeyPart = StringUtil.getHashedValue(lowerDate+upperDate+publisherId+channelId);
	        String key=TREND_ANALYSIS_ACTUAL_DATA_PUBLISHER+"_"+hashedKeyPart;
			log.info("setTrendsAnalysisActualDataPublisher : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, actualDataPublisherList, Expiration.byDeltaSeconds(expireInDay));
		}
	   
	    /*
	     * Get actual line chart map memcache
	     * @author Shubham Goel
	     * @return Map<String,String>
	     */
	    public static Map<String,String> getLineChartMapFromCachePublisher(String lowerDate, String upperDate, String publisher, String channelName){
			Map<String,String> dataMap=null;
			String hashedKeyPart = StringUtil.getHashedValue(lowerDate+upperDate+publisher+channelName);
			String key=ACTUAL_LINE_CHART_MAP_PUBLISHER+"_"+hashedKeyPart;
	    	if(memcache !=null && memcache.contains(key)){
	    		dataMap=(Map<String,String>) memcache.get(key);
	    	}    	
	    	return dataMap;
		}
	    
	    /*
	     * Set line chart data by in memcache
	     * @author  Shubham Goel
	     * @param Map<String,String>
	     */
	    public static void  setLineChartMapInCachePublisher(Map<String,String> dataMap, String lowerDate, String upperDate, String publisher, String channelName){
	    	String hashedKeyPart = StringUtil.getHashedValue(lowerDate+upperDate+publisher+channelName);
	    	String key=ACTUAL_LINE_CHART_MAP_PUBLISHER+"_"+hashedKeyPart;
	    	if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, dataMap,Expiration.byDeltaSeconds(3600));
		}
	    
	    public static void updateAllFinaliseNonFinaliseTables(String projectId){
	    	String finaliseMemcacheKey=projectId+"_"+FINALISE_BQ_TABLES_KEY;
			log.info("Update all finalise tables in memcache also..finaliseMemcacheKey:"+finaliseMemcacheKey);
			MemcacheUtil.setDataListInCacheByKey(null, finaliseMemcacheKey);
			String nonFinaliseMemcacheKey=projectId+"_"+NON_FINALISE_BQ_TABLES_KEY;
			log.info("Update all non finalise tables in memcache also..nonFinaliseMemcacheKey:"+nonFinaliseMemcacheKey);
			MemcacheUtil.setDataListInCacheByKey(null, nonFinaliseMemcacheKey);
	    }
	    
	    public static Map<String,Object> getVideoCampaignData(String orderId){ 
	    	Map<String,Object> videoCampaignData = new LinkedHashMap<>();
	    	String key = VIDEO_CAMPAIGN_DATA+orderId;
	    	if(memcache !=null && memcache.contains(key)){
	    		videoCampaignData = (Map<String,Object>) memcache.get(key);
	    	}
			return videoCampaignData;
			
		}
	    
	    public static void setVideoCampaignData(String orderId,Map<String,Object> videoCampaignData){ 
	    	String key = VIDEO_CAMPAIGN_DATA+orderId;
			log.info("setPerformerAdvertiserObj : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, videoCampaignData, Expiration.byDeltaSeconds(expiredInAnHour));
		}

		public static List<ChannelPerformancePopUpDTO> getCampaignGridDataFromCache(boolean activeCampaign, String publisherIdInBQ) {
			List<ChannelPerformancePopUpDTO> campaignGridDataList = null;
			String key = CAMPAIGN_GRID_DATA+"_"+activeCampaign+"_"+publisherIdInBQ;
	    	if(memcache !=null && memcache.contains(key)){
	    		campaignGridDataList = (List<ChannelPerformancePopUpDTO>) memcache.get(key);
	    	}
			return campaignGridDataList;
		}
		
		public static void setCampaignGridDataInCache(boolean activeCampaign, String publisherIdInBQ, List<ChannelPerformancePopUpDTO> campaignGridDataList){ 
			String key = CAMPAIGN_GRID_DATA+"_"+activeCampaign+"_"+publisherIdInBQ;
			log.info("setCampaignGridDataInCache : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, campaignGridDataList, Expiration.byDeltaSeconds(expiredInAnHour));
		}
		
		
		public static List<ProductsObj> getProductObj(String partnerId){ 
			List<ProductsObj> productData = new ArrayList<>();
	    	String key = ALL_PRODUCT_DATA+partnerId;
	    	log.info("getProductObj : memcache key:"+key);
	    	if(memcache !=null && memcache.contains(key)){
	    		productData = (List<ProductsObj>) memcache.get(key);
	    	}
			return productData;
			
		}
	    
	    public static void setProductObj(String partnerId, List<ProductsObj> productData){ 
	    	String key = ALL_PRODUCT_DATA+partnerId;
			log.info("setProductObj : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, productData, Expiration.byDeltaSeconds(expireInDay));
		}

		public static List<LineItemDTO> getAdUnitsChildsFromDFP(String parentId, String parentFullPath, String adServerId, String adServerUsername) {
			List<LineItemDTO> siteData = new ArrayList<>();
	    	String key = DFP_CHILD_ADUNITS + parentId + parentFullPath + adServerId + adServerUsername;
	    	if(memcache !=null && memcache.contains(key)){
	    		siteData = (List<LineItemDTO>) memcache.get(key);
	    	}
			return siteData;
		}

		public static void setAdUnitsChildsFromDFP(List<LineItemDTO> siteList, String parentId, String parentFullPath, String adServerId, String adServerUsername) {
			String key = DFP_CHILD_ADUNITS + parentId + parentFullPath + adServerId + adServerUsername;
			log.info("setAdUnitsChildsFromDFP : memcache key:"+key);			
			if(memcache !=null && memcache.contains(key)){
	    		memcache.delete(key);
	    	}
	    	memcache.put(key, siteList, Expiration.byDeltaSeconds(60*60));
		}
		
		public static void setObjectsListInCache(List<? extends Object> objectList, String key){
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			memcache.put(key, objectList, Expiration.byDeltaSeconds(2*60*60));
		}
		public static boolean setObjectsListInCacheWithExpirationErrorFree(List<? extends Object> objectList, String key,int expirationTime){
			try{
				setObjectsListInCacheWithExpiration(objectList, key, expirationTime); 
				return true;
			}catch(MemcacheServiceException mse){
				return false;
			}
		}
		public static void setObjectsListInCacheWithExpiration(List<? extends Object> objectList, String key,int expirationTime){
			
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			if(expirationTime!=0){
				memcache.put(key, objectList, Expiration.byDeltaSeconds(expirationTime));
			}else{
				memcache.put(key, objectList, Expiration.byDeltaSeconds(2*60*60));
			}
			
		}
		
		public static List<? extends Object> getObjectsListFromCache(String key){
			List<? extends Object> objectList=null;			
			if(memcache !=null && memcache.contains(key)){
				objectList=(List<? extends Object>) memcache.get(key);
			}
			return objectList;
		
		}	
		
		
		public static void setObjectInCache(String key,Object object){
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			memcache.put(key, object, Expiration.byDeltaSeconds(12*60*60));
		}
		
		public static void setObjectInCache(String key,Object object, int expirationTime){
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			memcache.put(key, object, Expiration.byDeltaSeconds(expirationTime));
		}
		
		public static Object getObjectFromCache(String key){
			Object object=null;			
			if(memcache !=null && memcache.contains(key)){
				object= memcache.get(key);
			}
			return object;		
		}
		
		
		public static void setObjectsMapInCache(Map<String,? extends Object> objectMap, String key){
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
			memcache.put(key, objectMap, Expiration.byDeltaSeconds(2*60*60));
		}
		
		public static Map<String,? extends Object> getObjectsMapFromCache(String key){
			Map<String,? extends Object> objectMap=null;			
			if(memcache !=null && memcache.contains(key)){
				objectMap=(Map<String,? extends Object>) memcache.get(key);
			}
			return objectMap;
		
		}	
		
		public static void flushCache(String key){
			if(memcache !=null && memcache.contains(key)){
				memcache.delete(key);
			}
		}
}
