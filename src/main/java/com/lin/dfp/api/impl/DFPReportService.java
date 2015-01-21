package com.lin.dfp.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.joda.time.Duration;
import org.joda.time.Instant;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.utils.v201403.DateTimes;
import com.google.api.ads.dfp.jaxws.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.jaxws.v201403.AdUnit;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitPage;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitParent;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.Column;
import com.google.api.ads.dfp.jaxws.v201403.Company;
import com.google.api.ads.dfp.jaxws.v201403.CompanyPage;
import com.google.api.ads.dfp.jaxws.v201403.CompanyServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Date;
import com.google.api.ads.dfp.jaxws.v201403.DateRangeType;
import com.google.api.ads.dfp.jaxws.v201403.Dimension;
import com.google.api.ads.dfp.jaxws.v201403.DimensionAttribute;
import com.google.api.ads.dfp.jaxws.v201403.DimensionFilter;
import com.google.api.ads.dfp.jaxws.v201403.ExportFormat;
import com.google.api.ads.dfp.jaxws.v201403.Forecast;
import com.google.api.ads.dfp.jaxws.v201403.ForecastServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.InventoryServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItemSummary;
import com.google.api.ads.dfp.jaxws.v201403.NetworkServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Order;
import com.google.api.ads.dfp.jaxws.v201403.OrderPage;
import com.google.api.ads.dfp.jaxws.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.OrderStatus;
import com.google.api.ads.dfp.jaxws.v201403.ComputedStatus;
import com.google.api.ads.dfp.jaxws.v201403.ReportJob;
import com.google.api.ads.dfp.jaxws.v201403.ReportJobStatus;
import com.google.api.ads.dfp.jaxws.v201403.ReportQuery;
import com.google.api.ads.dfp.jaxws.v201403.ReportQueryAdUnitView;
import com.google.api.ads.dfp.jaxws.v201403.ReportServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
import com.google.api.ads.dfp.jaxws.v201403.UnitType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.common.collect.Lists;
import com.lin.dfp.api.IDFPReportService;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.web.dto.AccountDTO;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;


/*
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 */
public class DFPReportService implements IDFPReportService{
	private  final Logger log = Logger.getLogger(DFPReportService.class.getName());
	private static final int BUFFER_SIZE = 4* 1024 * 1024;
	private static final SimpleDateFormat DATE_TIME_FORMAT =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private static final SimpleDateFormat DATE_FORMAT =
		new SimpleDateFormat("yyyy-MM-dd'T'");
	
	private static final String advertiserForDFPQuery = "ADVERTISER";
	private static final String agencyForDFPQuery = "AGENCY";
	
	/*
	 * @author Naresh Pokhriyal
	 * get DFP network credentials in a Map with networkCode as key
	 * @return Map<String, AdServerCredentialsDTO> credentialsMap
	 */
	public static Map<String, AdServerCredentialsDTO> getNetWorkCredentials() {
		Map<String, AdServerCredentialsDTO> credentialsMap = new HashMap<>();
		// LIN DIGITAL
		AdServerCredentialsDTO adServerCredentialsDTO = new AdServerCredentialsDTO(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE, LinMobileConstants.LIN_DIGITAL_EMAIL_ADDRESS, LinMobileConstants.LIN_DIGITAL_EMAIL_PASSWORD);
		credentialsMap.put(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE, adServerCredentialsDTO);
		// TRIBUNE
		adServerCredentialsDTO = new AdServerCredentialsDTO(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE, LinMobileConstants.TRIBUNE_DFP_EMAIL_ADDRESS, LinMobileConstants.TRIBUNE_DFP_EMAIL_PASSWORD);
		credentialsMap.put(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE, adServerCredentialsDTO);
		// LIN_MOBILE
		adServerCredentialsDTO = new AdServerCredentialsDTO(LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE, LinMobileConstants.LIN_MOBILE_DFP_EMAIL_ADDRESS, LinMobileConstants.LIN_MOBILE_DFP_EMAIL_PASSWORD);
		credentialsMap.put(LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE, adServerCredentialsDTO);
		// LIN_MOBILE_NEW
		adServerCredentialsDTO = new AdServerCredentialsDTO(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE, LinMobileConstants.LIN_MOBILE_NEW_DFP_EMAIL_ADDRESS, LinMobileConstants.LIN_MOBILE_NEW_DFP_EMAIL_PASSWORD);
		credentialsMap.put(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE, adServerCredentialsDTO);
		// EXAMINER
		/*adServerCredentialsDTO = new AdServerCredentialsDTO(LinMobileConstants.EXAMINER_DFP_NETWORK_CODE, LinMobileConstants.EXAMINER_DFP_EMAIL_ADDRESS, LinMobileConstants.EXAMINER_DFP_EMAIL_PASSWORD);
		credentialsMap.put(LinMobileConstants.EXAMINER_DFP_NETWORK_CODE, adServerCredentialsDTO);*/

		return credentialsMap;
	}
		
	public static String getDFPDataSourceByDFPNetworkCode(String dfpNetworkCode){
		String dfpDataSource = "";
		switch (dfpNetworkCode) {
			case LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE:
				dfpDataSource = LinMobileConstants.DFP_DATA_SOURCE;
				break;
			case LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE:
				dfpDataSource = LinMobileConstants.DFP_DATA_SOURCE;
				break;
			case LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE:
				dfpDataSource = LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE;
				break;
			case LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE:
				dfpDataSource = LinMobileConstants.DFP_DATA_SOURCE;
				break;
			default:
				dfpDataSource = LinMobileConstants.DFP_DATA_SOURCE;
				break;
		}
		return dfpDataSource;
	}
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * Get DFP report using start date and end date
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate
	 * @return String downloadUrl
	 */
	public  String getDFPReport(DfpServices dfpServices, DfpSession session,String start,String end) throws Exception{
		 log.info("getDFPReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
	     
	    /* Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
    	 query.append(" WHERE AD_UNIT_ID IN (51919902, 51929502, 51944742, 51758982, 51920862, 51939462, 51942822, 51924702, 54776382, 54765222, 51923742, 51928542, 51926622, 51941862, 54774462, 51932502, 51931542, 54772422, 51921822, 51925662, 51918942, 51943782, 51935382, 54767742, 54769662, 51930462, 51933462, 51938502, 51940902, 51927582, 51936582, 51937542, 54780222, 54778302, 51776502, 51922782, 51934422, 51945702, 55960542)" );
    	 //query.append(" AND  CREATIVE_SIZE IN ('320 x 50', '728 x 90', '300 x 250') ");
    	 		   
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);*/
	     
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * Get DFP report using start date and end date
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds,String targetFilter
	 * @return String downloadUrl
	 */
	public  String getDFPReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds,String targetFilter) throws Exception{
		 log.info("getDFPReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=null;
	     if(targetFilter !=null && targetFilter.equalsIgnoreCase("MOBILE")){
	    	 dimFilterArray=new DimensionFilter[]{	    		 
		    		 DimensionFilter.MOBILE_LINE_ITEMS
		     };	    	 
	     }else{
	    	 dimFilterArray=new DimensionFilter[]{	 
		    		 DimensionFilter.ACTIVE_AD_UNITS
		     };
	     }
	     
	     
	     //where (line_item_target_platform in ('MOBILE') or order_id in (159122022));	    
	    
	     if(orderIds !=null && orderIds.size()>0){	 
	    	 Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     
	    	 query.append(" WHERE ORDER_ID IN ( ");
		     
		     for(int i=0;i<orderIds.size();i++){
		    	 if(i==0){
		    		 query.append(orderIds.get(i));	 
		    	 }else{
		    		 query.append(" , "+ orderIds.get(i));
		    	 }	    	 
		     }
		     query.append(" )");		   
		     log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		     
		     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderId found in datastore to add in reportQuery, loading report for all orderIds from dfp...");
	     }   
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital DFP report using start date and end date 
	 *      for Golfsmith: LINMobile-Golfsmith-Golfsmith2013-2013-07
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,String orderName
	 * @return String downloadUrl
	 */
	public  String getLinDigitalReport(DfpServices dfpServices, DfpSession session,
			String start,String end,String orderName) throws Exception{
		 log.info("getLinDigitalReport() by dates..start:"+start+" and end:"+end+" and orderName:"+orderName);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.WEB_LINE_ITEMS
	     };
	     
	     Statement statement=new Statement();
	     statement.setQuery(" WHERE ORDER_NAME = 'LINMobile-Golfsmith-Golfsmith2013-2013-07' ");
	     reportQuery.setStatement(statement);
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital DFP report using start date and end date 
	 *      for given orderId
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,long orderId
	 * @return String downloadUrl
	 */
	public  String getLinDigitalReport(DfpServices dfpServices, DfpSession session,
			String start,String end,long orderId) throws Exception{
		 log.info("getLinDigitalReport() by dates..start:"+start+" and end:"+end+" and orderId:"+orderId);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		 };	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.WEB_LINE_ITEMS
	     };*/
	     
	     Statement statement=new Statement();
	     String query="WHERE  ORDER_ID = "+orderId;
	     statement.setQuery(query);
	     //statement.setQuery(" WHERE ORDER_NAME = 'LINMobile-Golfsmith-Golfsmith2013-2013-07' ");
	     reportQuery.setStatement(statement);
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital DFP report using start date and end date 
	 *      for all given orderIds
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds
	 * @return String downloadUrl
	 */
	public  String getLinDigitalReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception{
		 log.info("getLinDigitalReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		 };	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.ACTIVE_AD_UNITS
	     };
	     
	     
	     
	     if(orderIds.size()>0){	 
	    	 Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     
	    	 query.append(" WHERE ORDER_ID IN ( ");
		     
		     for(int i=0;i<orderIds.size();i++){
		    	 if(i==0){
		    		 query.append(orderIds.get(i));	 
		    	 }else{
		    		 query.append(" , "+ orderIds.get(i));
		    	 }	    	 
		     }
		     query.append(" )");		   
		     log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		     
		     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderId found in datastore to add in reportQuery, loading report for all orderIds from dfp...");
	     }   
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get 'Rich Media' Custom Event report using start date and end date 
	 *      for given orderIds
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds
	 * @return String downloadUrl
	 */
	public  String getRichMediaCustomEventReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE,
					  Dimension.CUSTOM_EVENT_ID,
					  Dimension.CUSTOM_EVENT_NAME,
					  Dimension.CUSTOM_EVENT_TYPE
					  
		 };
		     
		 Column[] columnArray= new Column[] {
		    		 
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_COUNT,
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_TIME		    		  
		    		  
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				 
				 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT
		   		 
		 };
	     
		 DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
		 
		 if(orderIds.size()>0){
	    	 Statement statement=new Statement();
		     String query="WHERE  ORDER_ID IN ( ";
		     for(int i=0;i<orderIds.size();i++){
		    	 if(i==0){
		    		 query =query+orderIds.get(i);	 
		    	 }else{
		    		 query =query+ " , "+ orderIds.get(i);
		    	 }	    	 
		     }
		     query=query+" )";		    
		     log.info("Query : "+query);
		     statement.setQuery(query);
		     
		     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderIds given, loading report for all orderIds from dfp...apply target platform:mobile");
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }
        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	/*
	 * @author Naresh Pokhriyal(naresh.pokhriyal@mediaagility.com)
	 * 
	 * Get 'Rich Media' Video Campaign report using start date and end date 
	 *      for given accountsIds(advertisers)
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate, String endDate, List<String> accountIds
	 * @return String downloadUrl
	 */
	@Override
	public  String getRichMediaVideoCampaignReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
    		 Dimension.DATE, 
    		 Dimension.ADVERTISER_ID,
    		 Dimension.ADVERTISER_NAME,
    		 Dimension.ORDER_ID,
    		 Dimension.ORDER_NAME,
    		 Dimension.LINE_ITEM_ID,
    		 Dimension.LINE_ITEM_NAME,
    		 Dimension.LINE_ITEM_TYPE,
    		 Dimension.CREATIVE_ID,
    		 Dimension.CREATIVE_NAME,
    		 Dimension.CREATIVE_SIZE,
    		 Dimension.CREATIVE_TYPE
		 };
			
		 Column[] columnArray= new Column[] {
				 /*Rich Media viewership*/
				 Column.RICH_MEDIA_BACKUP_IMAGES,				//
				 Column.RICH_MEDIA_DISPLAY_TIME,				//
				 Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME,		//
				 
				 /* Rich Media interaction */
				 Column.RICH_MEDIA_EXPANSIONS,
				 Column.RICH_MEDIA_EXPANDING_TIME,
				 Column.RICH_MEDIA_INTERACTION_TIME,
				 Column.RICH_MEDIA_INTERACTION_COUNT,
				 Column.RICH_MEDIA_INTERACTION_RATE,			//
				 Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME,
				 Column.RICH_MEDIA_INTERACTION_IMPRESSIONS,
				 Column.RICH_MEDIA_MANUAL_CLOSES,
				 Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS,
				 
				 /* Rich Media video metrics */
				 Column.RICH_MEDIA_VIDEO_INTERACTIONS,
				 Column.RICH_MEDIA_VIDEO_INTERACTION_RATE,
				 Column.RICH_MEDIA_VIDEO_MUTES,
				 Column.RICH_MEDIA_VIDEO_PAUSES,
				 Column.RICH_MEDIA_VIDEO_PLAYES,
				 Column.RICH_MEDIA_VIDEO_MIDPOINTS,
				 Column.RICH_MEDIA_VIDEO_COMPLETES,
				 Column.RICH_MEDIA_VIDEO_REPLAYS,
				 Column.RICH_MEDIA_VIDEO_STOPS,
				 Column.RICH_MEDIA_VIDEO_UNMUTES,
				 Column.RICH_MEDIA_VIDEO_VIEW_RATE,
				 Column.RICH_MEDIA_VIDEO_VIEW_TIME,				//
				 
				 /* Video viewership */
				 Column.VIDEO_INTERACTION_START,
				 Column.VIDEO_INTERACTION_FIRST_QUARTILE,
				 Column.VIDEO_INTERACTION_MIDPOINT,
				 Column.VIDEO_INTERACTION_THIRD_QUARTILE,
				 Column.VIDEO_INTERACTION_COMPLETE,
				 Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE,
				 Column.VIDEO_INTERACTION_COMPLETION_RATE,
				 Column.VIDEO_INTERACTION_ERROR_COUNT,
				 Column.VIDEO_INTERACTION_VIDEO_LENGTH,
				 Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN,
				 Column.VIDEO_INTERACTION_ENGAGED_VIEW,
				 Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE,
				 
				 /* Video interaction */
				 Column.VIDEO_INTERACTION_PAUSE,
				 Column.VIDEO_INTERACTION_RESUME,
				 Column.VIDEO_INTERACTION_REWIND,
				 Column.VIDEO_INTERACTION_MUTE,
				 Column.VIDEO_INTERACTION_UNMUTE,
				 Column.VIDEO_INTERACTION_COLLAPSE,
				 Column.VIDEO_INTERACTION_EXPAND,
				 Column.VIDEO_INTERACTION_FULL_SCREEN,
				 Column.VIDEO_INTERACTION_VIDEO_SKIPS,
			     Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE,
			     Column.VIDEO_INTERACTION_VIEW_RATE,
		 };
		
		
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
			 DimensionAttribute.ORDER_START_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	   		 DimensionAttribute.ORDER_END_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
	   		 DimensionAttribute.ORDER_AGENCY,
	   		 DimensionAttribute.ORDER_AGENCY_ID,
	   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT	    		 
		 };
		 
		 /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };*/
		 
		 if(accountIds.size()>0){
			    Statement statement=new Statement();	    
			     StringBuffer query=new StringBuffer();
			     query.append(" WHERE ADVERTISER_ID IN ( ");
			     
			     for(int i=0;i<accountIds.size();i++){
			    	 if(i==0){
			    		 query.append(accountIds.get(i));
			    	 }else{
			    		 query.append(",");
			    		 query.append(accountIds.get(i));
			    	 }
			     }
			     query.append("23548662,24659502,25870542,26561262,26902062,26931702,27484902,28013982,28020822,28876182,29080302,29087742,29362902,29363022,29444622,29549382,29552022,29579502,29579622,29627742,29681982,30177102,30728142,31250982,31572822,31572942,32483382,32590782,32590902,32829702,33206142,33503142,33503262,33503382,34000182,34000302,35545782,35703102,36098502,36110862,36470382,36470502,36638142,36811782,37747182,38299062,38648022,39611142,40346142,41246382,41418582,41580342,41580702,42039462,42784062,42784902,43043982,43437342,43592742,44056182,44058702,44800542,45060342,45337782,45495342,45512982,45524022,45953382,46585662,47319582,47595222,47600622,48008982,48381702,48384702,48462582,49058022,49670622,49916742,50038422,50038542,50038902,50477622,50629422,51143022,51150702,51416982,51533742,52090542,52090662,52285422,52530822,52641222,52757742,52856022,52856142,53299542,53378382,53390982,54366222,54503382,54503502,56534862,57378462");
			     query.append(" )");
			     log.info("PQL Query : "+query.toString());
			     statement.setQuery(query.toString());		     
			     reportQuery.setStatement(statement);
	     }
		 /*else{
	    	 log.warning("no accountIds given, loading report for all accountIds from dfp..");
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }*/
        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar
	 * 
	 * Get 'Rich Media' Video Campaign report using start date and end date 
	 *      for given orderIds
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate, String endDate, List<String> orderIds
	 * @return String downloadUrl
	 */

	public  String getRichMediaVideoCampaignReportByByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
	
	     return getRichMediaVideoCampaignReportByByOrderIds(dfpServices, session, start, end, orderIds, null);
	}
	
	public  String getRichMediaVideoCampaignReportByByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
    		 Dimension.DATE, 
    		 Dimension.ADVERTISER_ID,
    		 Dimension.ADVERTISER_NAME,
    		 Dimension.ORDER_ID,
    		 Dimension.ORDER_NAME,
    		 Dimension.LINE_ITEM_ID,
    		 Dimension.LINE_ITEM_NAME,
    		 Dimension.LINE_ITEM_TYPE,
    		 Dimension.CREATIVE_ID,
    		 Dimension.CREATIVE_NAME,
    		 Dimension.CREATIVE_SIZE,
    		 Dimension.CREATIVE_TYPE
		 };
			
		 Column[] columnArray= new Column[] {
				 /*Rich Media viewership*/
				 Column.RICH_MEDIA_BACKUP_IMAGES,				//
				 Column.RICH_MEDIA_DISPLAY_TIME,				//
				 Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME,		//
				 
				 /* Rich Media interaction */
				 Column.RICH_MEDIA_EXPANSIONS,
				 Column.RICH_MEDIA_EXPANDING_TIME,
				 Column.RICH_MEDIA_INTERACTION_TIME,
				 Column.RICH_MEDIA_INTERACTION_COUNT,
				 Column.RICH_MEDIA_INTERACTION_RATE,			//
				 Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME,
				 Column.RICH_MEDIA_INTERACTION_IMPRESSIONS,
				 Column.RICH_MEDIA_MANUAL_CLOSES,
				 Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS,
				 
				 /* Rich Media video metrics */
				 Column.RICH_MEDIA_VIDEO_INTERACTIONS,
				 Column.RICH_MEDIA_VIDEO_INTERACTION_RATE,
				 Column.RICH_MEDIA_VIDEO_MUTES,
				 Column.RICH_MEDIA_VIDEO_PAUSES,
				 Column.RICH_MEDIA_VIDEO_PLAYES,
				 Column.RICH_MEDIA_VIDEO_MIDPOINTS,
				 Column.RICH_MEDIA_VIDEO_COMPLETES,
				 Column.RICH_MEDIA_VIDEO_REPLAYS,
				 Column.RICH_MEDIA_VIDEO_STOPS,
				 Column.RICH_MEDIA_VIDEO_UNMUTES,
				 Column.RICH_MEDIA_VIDEO_VIEW_RATE,
				 Column.RICH_MEDIA_VIDEO_VIEW_TIME,				//
				 
				 /* Video viewership */
				 Column.VIDEO_INTERACTION_START,
				 Column.VIDEO_INTERACTION_FIRST_QUARTILE,
				 Column.VIDEO_INTERACTION_MIDPOINT,
				 Column.VIDEO_INTERACTION_THIRD_QUARTILE,
				 Column.VIDEO_INTERACTION_COMPLETE,
				 Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE,
				 Column.VIDEO_INTERACTION_COMPLETION_RATE,
				 Column.VIDEO_INTERACTION_ERROR_COUNT,
				 Column.VIDEO_INTERACTION_VIDEO_LENGTH,
				 Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN,
				 Column.VIDEO_INTERACTION_ENGAGED_VIEW,
				 Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE,
				 
				 /* Video interaction */
				 Column.VIDEO_INTERACTION_PAUSE,
				 Column.VIDEO_INTERACTION_RESUME,
				 Column.VIDEO_INTERACTION_REWIND,
				 Column.VIDEO_INTERACTION_MUTE,
				 Column.VIDEO_INTERACTION_UNMUTE,
				 Column.VIDEO_INTERACTION_COLLAPSE,
				 Column.VIDEO_INTERACTION_EXPAND,
				 Column.VIDEO_INTERACTION_FULL_SCREEN,
				 Column.VIDEO_INTERACTION_VIDEO_SKIPS,
			     Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE,
			     Column.VIDEO_INTERACTION_VIEW_RATE,
		 };
		
		
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
			 DimensionAttribute.ORDER_START_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	   		 DimensionAttribute.ORDER_END_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
	   		 DimensionAttribute.ORDER_AGENCY,
	   		 DimensionAttribute.ORDER_AGENCY_ID,
	   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT	    		 
		 };
		 
		 /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };*/
		 
		 if(orderIds.size()>0){
			    Statement statement=new Statement();	    
			     StringBuffer query=new StringBuffer();
			     if(commaSepratedOrderIds!=null && commaSepratedOrderIds.trim().length()>0){
		 	    	 query.append(" WHERE ORDER_ID IN ("+orderIds.get(0)+")");
		 	     }else{
		 	    	 query.append(" WHERE ORDER_ID IN ( ");
		 	 	     
		 	 	     for(int i=0;i<orderIds.size();i++){
		 	 	    	 if(i==0){
		 	 	    		 query.append(orderIds.get(i));
		 	 	    	 }else{
		 	 	    		 query.append(",");
		 	 	    		 query.append(orderIds.get(i));
		 	 	    	 }
		 	 	     }
		 	 	     query.append(" )");    
		 	     }
			     log.info("PQL Query : "+query.toString());
			     statement.setQuery(query.toString());		     
			     reportQuery.setStatement(statement);
	     }        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Read CSV gzip file that DFP provides  
	 *      
	 * @param String fileURL
	 * 
	 * @return String downloadUrl
	 */
	public  String readCSVGZFile(String url) throws IOException {
		   log.info("readCSVGZFile.....");
		   StringBuffer dataBuffer =new StringBuffer();
		   InputStream gzipStream = new GZIPInputStream((new URL(url)).openStream(),BUFFER_SIZE);
		   Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
		   BufferedReader buffered = new BufferedReader(decoder);
		   int i=0;
		   String line=buffered.readLine();
		   while(line !=null){
			   if(i>0){
				   dataBuffer.append('\n');
			   }			   
			   dataBuffer.append(line);
			   line=buffered.readLine();
			   i++;
		   }		
		   log.info("Total lines in buffer: "+i);
		   return dataBuffer.toString();		   
	}
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Read CSV gzip file and split if it exceeds 10,000 rows	
	 *      
	 * @param String fileURL
	 * 
	 * @return List<String> dataList(List of split files)
	 */
	public  List<String> readCSVGZFileAndSplit(String url) {
	    log.info("readCSVGZFileAndSplit: split it if size is too big");
		StringBuffer dataBuffer =new StringBuffer();
		List<String> dataList=new ArrayList<String>();
		String header="";
		try {
			InputStream gzipStream = new GZIPInputStream(
					(new URL(url)).openStream(),BUFFER_SIZE);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			log.info("Before reading line.....");
			BufferedReader reader = new BufferedReader(decoder);
			int i = 0;
			String line = reader.readLine();
			int count = 1;
			while (line != null) {
				if(i==0){
					header=line; // Save header for splitted files
				}
				
				if (i > 0) {
					dataBuffer.append('\n');
				}
				
				if (i == (count * 10000 - 1)) {		
					if(count == 1){
						dataList.add(dataBuffer.toString());// First split file already have header
					}else{						
						dataList.add(header+"\n"+dataBuffer.toString()); // Add header for all split files except first
					}
					
					dataBuffer = new StringBuffer();
					count++;
					log.info("added file data in list :"+count+" : i:" + i);
				}
				dataBuffer.append(line);
				line = reader.readLine();
				i++;
			}
			log.info("Spliting done....i:"+i);
			if(count > 1){
				dataList.add(header+"\n"+dataBuffer.toString()); // add header in last split file
			}else{
				dataList.add(dataBuffer.toString());
			}
			
			log.info("Splitted dataList size:" + dataList.size());
			reader.close();
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			e.printStackTrace();
		}

		return dataList;
	}	

	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get PerformanceByLocation report from DFP using start date and end date 
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate
	 * @return String downloadUrl
	 */
	public  String getDFPReportByLocation(DfpServices dfpServices, DfpSession session
			,String start,String end,String targetPlatform) throws Exception{
		
	     String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);	     
	    	    
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  /*Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,*/
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME
				  /*Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME*/
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  //Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     if(targetPlatform !=null && targetPlatform.equalsIgnoreCase("MOBILE")){
	    	 DimensionFilter[] dimFilterArray=new DimensionFilter[]{
		    		 DimensionFilter.MOBILE_LINE_ITEMS	    		 
		     }; 
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }else{
	    	 DimensionFilter[] dimFilterArray=new DimensionFilter[]{
		    		 DimensionFilter.WEB_LINE_ITEMS	    		 
		     };
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }
	     
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
		
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get PerformanceByLocation report from DFP using LineItemType, startDate and endDate
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String start,String end,int lineItemTypeId
	 * @return String downloadUrl
	 */
	public  String getDFPReportByLocation(DfpServices dfpServices, DfpSession session, String start,String end,int lineItemTypeId) throws Exception{		

	     String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);	     
	     
		 String lineItemType=null;
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME				 
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	  
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.MOBILE_LINE_ITEMS	    		 
	     };
	     
	     switch(lineItemTypeId){
	        case 1:
	        	lineItemType="SPONSORSHIP";
	        	break;
	        case 2:
	        	lineItemType="STANDARD";
	        	break;
	        case 3:
	        	lineItemType="NETWORK";
	        	break;
	        case 4:
	        	lineItemType="HOUSE";
	        	break;
	        case 5:
	        	lineItemType="PRICE_PRIORITY";
	        	break;
	      /*  case 6:
	        	lineItemType="AD_EXCHANGE";
	        	break;
	        case 7:
	        	lineItemType="LEGACY_DFP";
	        	break;
	        case 8:
	        	lineItemType="CLICK_TRACKING";
	        	break;
	        case 9:
	        	lineItemType="ADSENSE";
	        	break;
	        case 10:
	        	lineItemType="BULK";
	        	break;
	        case 11:
	        	lineItemType="BUMPER";
	        	break;
	        case 12:
	        	lineItemType="UNKNOWN";
	        	break;*/
	        default: 
	        	lineItemType=null;
	            break;
	     }
	    
	     log.info("lineItemTypeId :"+lineItemTypeId+" , lineItemType:"+lineItemType);
	     if(lineItemType !=null){
	    	 StatementBuilder statementBuilder = new StatementBuilder()
		        .where(" LINE_ITEM_TYPE = :lineItemType")
		        //.orderBy("id ASC")
		        //.limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
		        //.withBindVariableValue("advertiserType",advertiserType)
		        .withBindVariableValue("lineItemType",lineItemType); 
	    	 reportQuery.setStatement(statementBuilder.toStatement());	    	
	     }else{
	    	 StatementBuilder statementBuilder = new StatementBuilder()
		        .where(" LINE_ITEM_TYPE IN ('AD_EXCHANGE','LEGACY_DFP','CLICK_TRACKING','ADSENSE','BULK','BUMPER')");
	    	 reportQuery.setStatement(statementBuilder.toStatement());	 
	     }
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     //reportQuery.setStatement(filterStatement);
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get SellThrough report from DFP for next neek
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 * 
	 * @return String downloadUrl
	 */
	public  String getSellThroughReport(DfpServices dfpServices, DfpSession session) throws Exception{
		 log.info("getSellThroughReport called...");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     
	     Date startDate=new Date();	     
	     startDate.setDay(1);
	     startDate.setMonth(7);
	     startDate.setYear(2013);
	     
	     Date endDate=new Date();
	     endDate.setDay(3);
	     endDate.setMonth(7);
	     endDate.setYear(2013);
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	    
	     Dimension[] dimensionArray=new Dimension[] {
				  Dimension.DATE
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.SELL_THROUGH_AVAILABLE_IMPRESSIONS,
		    		  Column.SELL_THROUGH_FORECASTED_IMPRESSIONS,
		    		  Column.SELL_THROUGH_RESERVED_IMPRESSIONS,
		    		  Column.SELL_THROUGH_SELL_THROUGH_RATE		    		 		    		
		 };
	  
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
	     
	     //reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));	     
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital Performance by Location report using start date and end date 
	 *      for LinDigital
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds
	 * @return String downloadUrl
	 */
	public  String getLinDigitalPerformanceByLocationReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception{
		 log.info("getLinDigitalReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.COUNTRY_CRITERIA_ID,
					  Dimension.REGION_CRITERIA_ID,
					  Dimension.CITY_CRITERIA_ID,
					  Dimension.COUNTRY_NAME,
					  Dimension.REGION_NAME,
					  Dimension.CITY_NAME				 
		 };
		     
		 Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	  
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
		   		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
		   		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
		   		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
		   		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
		   		 DimensionAttribute.ORDER_PO_NUMBER,
		   		 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_TRAFFICKER	    		 
		 };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.WEB_LINE_ITEMS
	     };
	     
	  
	     if(orderIds.size()>0){
	    	 Statement statement=new Statement();
	    	 String query="WHERE  ORDER_ID IN ( ";
	    	 for(int i=0;i<orderIds.size();i++){
	    		 if(i==0){
	    			 query =query+orderIds.get(i);	 
	    		 }else{
	    			 query =query+ " , "+ orderIds.get(i);
	    		 }	    	 
	    	 }
	    	 query=query+" )";		    
	    	 log.info("Query : "+query);
	    	 statement.setQuery(query);
     
	    	 reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderIds given, loading report for all orderIds from dfp...");
	     }
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital DFP report using start date and end date 
	 *      for LinOne
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,String orderName
	 * @return String downloadUrl
	 */
	public  String getLinOneDFPReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception{
		 log.info("getLinOneDFPReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	    	     
	     Statement statement=new Statement();
	     statement.setQuery(" WHERE ORDER_ID IN (141903979) ");
	     reportQuery.setStatement(statement);
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital 'Rich Media' report using start date and end date 
	 *      for LinOne 
	 *      
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,String orderName
	 * @return String downloadUrl
	 */
	public  String getLinDigitalLinOneRichMediaReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception{
		 log.info("getLinDigitalRichMediaReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE
					  
		 };
		     
		 Column[] columnArray= new Column[] {
		    		  	    		  
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.RICH_MEDIA_DISPLAY_TIME,		    		  
		    		  Column.RICH_MEDIA_EXPANSIONS,
		    		  Column.RICH_MEDIA_INTERACTION_IMPRESSIONS,
		    		  Column.RICH_MEDIA_EXPANDING_TIME,
		    		  Column.RICH_MEDIA_MANUAL_CLOSES,
		    		  Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME,
		    		  Column.RICH_MEDIA_BACKUP_IMAGES,	
		    		  Column.RICH_MEDIA_INTERACTION_TIME,
		    		  Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME,
		    		  Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS,
		    		  Column.RICH_MEDIA_INTERACTION_RATE,
		    		  Column.RICH_MEDIA_INTERACTION_COUNT		    		  
		    		
		    		 /* Column.RICH_MEDIA_CUSTOM_CONVERSION_COUNT_VALUE,
		    		  Column.RICH_MEDIA_CUSTOM_CONVERSION_TIME_VALUE,
		    		  Column.RICH_MEDIA_VIDEO_COMPLETES,
		    		  Column.RICH_MEDIA_VIDEO_INTERACTION_RATE,
		    		  Column.RICH_MEDIA_VIDEO_INTERACTIONS,
		    		  Column.RICH_MEDIA_VIDEO_MIDPOINTS,
		    		  Column.RICH_MEDIA_VIDEO_MUTES,
		    		  Column.RICH_MEDIA_VIDEO_PAUSES,
		    		  Column.RICH_MEDIA_VIDEO_PLAYES,
		    		  Column.RICH_MEDIA_VIDEO_REPLAYS,
		    		  Column.RICH_MEDIA_VIDEO_STOPS,
		    		  Column.RICH_MEDIA_VIDEO_UNMUTES,
		    		  Column.RICH_MEDIA_VIDEO_VIEW_RATE,
		    		  Column.RICH_MEDIA_VIDEO_VIEW_TIME,*/
		    		  
		    		  
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				 
				 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT
		   		 
		   	  /*
		   		 DimensionAttribute.LINE_ITEM_LABELS,
		   		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
		   		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
		   		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
		   		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
		   		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
		   		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
		   		 DimensionAttribute.ORDER_PO_NUMBER,		   		 
		   		 DimensionAttribute.ORDER_TRAFFICKER,
		   		 DimensionAttribute.ORDER_SECONDARY_TRAFFICKERS,
		   		 DimensionAttribute.ORDER_SALESPERSON,
		   		 DimensionAttribute.ORDER_SECONDARY_SALESPEOPLE,
		   		 DimensionAttribute.ORDER_LABELS,
		   		 DimensionAttribute.ADVERTISER_LABELS,
		   		 DimensionAttribute.CREATIVE_CLICK_THROUGH_URL,
		   		 DimensionAttribute.CREATIVE_OR_CREATIVE_SET 
		   	 */
		   		 
		   		 
		 };
	   
		  
	     /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.WEB_LINE_ITEMS
	     };*/
	      
	     Statement statement=new Statement();	     
	     //statement.setQuery(" WHERE ORDER_ID IN (139570339, 141903979) ");
	     statement.setQuery(" WHERE ORDER_ID IN (141903979) ");

         reportQuery.setStatement(statement);
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital 'Rich Media' Custom Event report using start date and end date 
	 *      for given orderId
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,String orderName
	 * @return String downloadUrl
	 */
	public  String getLinDigitalRichMediaCustomEventReport(DfpServices dfpServices, DfpSession session,
			String start,String end,long orderId) throws Exception{
		 log.info("getLinDigitalLinOneRichMediaCustomEventReport: by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE
					  
		 };
		     
		 Column[] columnArray= new Column[] {
		    		 
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_COUNT,
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_TIME		    		  
		    		  
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				 
				 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT
		   		 
		 };
	      
	     Statement statement=new Statement();	     
	     //statement.setQuery(" WHERE ORDER_ID IN (139570339, 141903979) ");
	     statement.setQuery(" WHERE ORDER_ID IN ("+orderId+") ");

         reportQuery.setStatement(statement);
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;	     
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get Forcasted report from DFP 
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 * 
	 * @return String downloadUrl
	 * 
	 * @note This report is not supported for our network by DFP API right now
	 *       ,will be used in future.
	 */
	public  void getForcastedData(DfpServices dfpServices, DfpSession session)
			throws Exception {
		long lineItemId=44607462;
		ForecastServiceInterface forecastService = dfpServices.get(session,
				ForecastServiceInterface.class);
		
		Forecast forecast = forecastService.getForecastById(lineItemId);
		
		long matched = forecast.getMatchedUnits();
		long available = forecast.getAvailableUnits();		
		long possible = forecast.getPossibleUnits();
		long delivered=forecast.getDeliveredUnits();
		long orderId=forecast.getOrderId();
		long reserved=forecast.getReservedUnits();
		String unitType = forecast.getUnitType().toString().toLowerCase();
		//ForcastDTO forcastDTO=new ForcastDTO();
		System.out.println("matched: "+matched+" \n available: "+available
				+"\n possible: "+possible
				+"\n delivered: "+delivered
				+"\n orderId: "+orderId
				+"\n reserved: "+reserved
				+"\n unitType: "+unitType
				);
		
		forecast.setUnitType(UnitType.CLICKS);
		forecast = forecastService.getForecastById(lineItemId);
		matched = forecast.getMatchedUnits();
		available = forecast.getAvailableUnits();		
		possible = forecast.getPossibleUnits();
		delivered=forecast.getDeliveredUnits();
		orderId=forecast.getOrderId();
		reserved=forecast.getReservedUnits();
		unitType = forecast.getUnitType().toString().toLowerCase();
		
		System.out.println("matched: "+matched+" \n available: "+available
				+"\n possible: "+possible
				+"\n delivered: "+delivered
				+"\n orderId: "+orderId
				+"\n reserved: "+reserved
				+"\n unitType: "+unitType
				);
		
	}

	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * Get DFP report for 
	 *   Advertiser   - WLIN | Google Ad Exchange - AdEx (Remnant)
	 *   AdvertiserId - 15599382
	 * using start date and end date
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate
	 * @return String downloadUrl
	 */
	public  String getAdExchangeDataFromDFPReport(DfpServices dfpServices, DfpSession session,String start,String end) throws Exception{
		 log.info("getDFPReport() by dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
		 // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
	     
	     Statement statement=new Statement();
	     statement.setQuery(" WHERE ADVERTISER_ID = 15599382 ");
	     reportQuery.setStatement(statement);
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * Get DFP target report using start date and end date
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds,String targetFilter
	 * @return String downloadUrl
	 */
	public  String getDFPTargetReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds,String targetFilter) throws Exception{
		 log.info("start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.GENERIC_CRITERION_NAME
		  };
		     
		  Column[] columnArray= new Column[] {
			    		  Column.AD_SERVER_IMPRESSIONS,
			    		  Column.AD_SERVER_CLICKS,
			    		  Column.AD_SERVER_CTR,
			    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,	
			    		  Column.AD_SERVER_DELIVERY_INDICATOR,
			    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		  };
			
		
	   
	     DimensionFilter[] dimFilterArray=null;
	     if(targetFilter !=null && targetFilter.equalsIgnoreCase("MOBILE")){
	    	 dimFilterArray=new DimensionFilter[]{	    		 
		    		 DimensionFilter.MOBILE_LINE_ITEMS
		     };
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }
	     
	     /*else{ 
		    
	    	 *//****** using statement code starts *******//*	   
		     Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     query.append(" WHERE AD_UNIT_ANCESTOR_AD_UNIT_ID IN (55958262,55958382,55960542,55960662,55960782,55960902,32578062,54699942,57908742,54774462,54775422,54915822,55276902,57730782,54767742,54767862,54821502,55267902,54778302,54779262,54914622,55288662,51758982,51776502,54780222,54781182,54962502,55272822,54772422,54773502,54917622,55277862,51918942,51919902,56926302,57081222,58775502,51920862,51921822,58447182,51922782,51923742,57037662,51924702,51925662,56926662,57227862,51926622,51927582,57075462,54765222,54765342,54933942,55288422,51928542,51929502,56980422,51930462,51931542,56960622,57486942,58600302,54776382,54777342,54960582,55288542,54769662,54769782,54917982,55123782,51932502,51933462,57038502,32656902,54381582,54381702,54459942,54639942,51934422,51935382,56876022,51936582,51937542,57067062,51938502,51939462,56976102,57245262,51944742,51945702,57086262,51940902,51941862,57291462,51942822,51943782,56972622,57331422)" );
			 log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		     
		     reportQuery.setStatement(statement);
		     *//****** using statement code ends *******//*	   
	    
	     }*/
	     
	   
	    if(orderIds !=null && orderIds.size()>0){	 
	    	 Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     
	    	 query.append(" WHERE ORDER_ID IN ( ");
		     
		     for(int i=0;i<orderIds.size();i++){
		    	 if(i==0){
		    		 query.append(orderIds.get(i));	 
		    	 }else{
		    		 query.append(" , "+ orderIds.get(i));
		    	 }	    	 
		     }
		     query.append(" )");		   
		     log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		     
		     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderId found in datastore to add in reportQuery, loading report for all orderIds from dfp...");
	    	
	    	
	     }
	    
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	
	/*
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * Get LinDigital DFP Target report using start date and end date 
	 *      for all given orderIds
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds
	 * @return String downloadUrl
	 */
	public  String getLinDigitalTargetReport(DfpServices dfpServices, DfpSession session,
			String start,String end,List<Long> orderIds) throws Exception{
		 log.info("Dates ...start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {		    		  
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  /*Dimension.AD_UNIT_ID,
					  Dimension.AD_UNIT_NAME,*/
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  /*Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE,
					  Dimension.SALESPERSON_NAME,*/
					  Dimension.GENERIC_CRITERION_NAME
		 };
		     
		 Column[] columnArray= new Column[] {
			    		  Column.AD_SERVER_IMPRESSIONS,
			    		  Column.AD_SERVER_CLICKS,
			    		  Column.AD_SERVER_CTR,
			    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
			    		  /*Column.AD_SERVER_CPD_REVENUE, 
			    		   Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
			    		  Column.AD_SERVER_ALL_REVENUE */	    		
			    		  Column.AD_SERVER_DELIVERY_INDICATOR,
			    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS/*,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
			    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE*/
		 };
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
	    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 //DimensionFilter.ACTIVE_AD_UNITS
	     };
	     
	     
	     
	     if(orderIds.size()>0){	 
	    	 Statement statement=new Statement();	    
		     StringBuffer query=new StringBuffer();
		     
	    	 query.append(" WHERE ORDER_ID IN ( ");
		     
		     for(int i=0;i<orderIds.size();i++){
		    	 if(i==0){
		    		 query.append(orderIds.get(i));	 
		    	 }else{
		    		 query.append(" , "+ orderIds.get(i));
		    	 }	    	 
		     }
		     query.append(" )");		   
		     log.info("PQL Query : "+query.toString());
		     statement.setQuery(query.toString());		     
		     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderId found in datastore to add in reportQuery, loading report for all orderIds from dfp...");
	     }   
	     
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     //reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	public String downloadReport(DfpServices dfpServices, DfpSession session,ReportQuery reportQuery) throws ApiException_Exception, InterruptedException {
		 log.info("download report....networkCode:"+session.getNetworkCode());
		 String downloadUrl=null;
		 ReportServiceInterface reportService = dfpServices.get(session, ReportServiceInterface.class);
		 log.info("Got report service....");
	     ReportJob reportJob = new ReportJob();
	     log.info("created report job...");
	     reportJob.setReportQuery(reportQuery); 
	     log.info("set report query and now going to run report.....");
	      // Run report job.
	      reportJob = reportService.runReportJob(reportJob);

	      do {
	    	log.info("Report with ID '" + reportJob.getId() + "' is still running.");
	        Thread.sleep(30000);
	        // Get report job.
	        reportJob = reportService.getReportJob(reportJob.getId());
	      } while (reportJob.getReportJobStatus() == ReportJobStatus.IN_PROGRESS);

	      if (reportJob.getReportJobStatus() == ReportJobStatus.FAILED) {
	    	  log.info("Report job with ID '" + reportJob.getId()
	            + "' failed to finish successfully.");
	      }else{
	    	  log.info("Report job with ID '" + reportJob.getId()+ "' completed successfully."); 
	          Long reportJobId = reportJob.getId();
	          ExportFormat exportFormat = ExportFormat.CSV_DUMP;
	          downloadUrl = reportService.getReportDownloadURL(reportJobId, exportFormat);
	          log.info("downloadUrl: "+downloadUrl);
	      }
	      return downloadUrl;
	}
	
	
	
	public  List<Long> getAllActiveMobileLineItemsOrderIds(DfpServices dfpServices, DfpSession session)
	        throws Exception{
		
		List<Long> orderIdList=new ArrayList<Long>();
		LineItemServiceInterface lineItemService=dfpServices.get(session, LineItemServiceInterface.class);
		
		LineItemPage page = new LineItemPage();
	    Statement filterStatement = new Statement();	    

	    filterStatement.setQuery("WHERE STATUS IN ('DELIVERING') " );

        page = lineItemService.getLineItemsByStatement(filterStatement);
        if (page.getResults() != null) {
          int i = page.getStartIndex();
          for (LineItemSummary lineItem : page.getResults()) {
        	  String targetPlatform=lineItem.getTargetPlatform().value();
        	  if(targetPlatform !=null && targetPlatform.equalsIgnoreCase("MOBILE")){
        		  if(orderIdList.indexOf(lineItem.getOrderId()) != -1){
	        		  orderIdList.add(lineItem.getOrderId()); 
	        	  }	 
        	  }else{
        		  System.out.println("Not mobile platform: targetPlatform:"+targetPlatform);
        	  }
        	        	  
              i++;
          }
        }
        
	   /* do {	       
	        

	        offset += 500;
	    } while (page.getResults() != null && page.getResults().size() == 500);*/

	    System.out.println("Number of results found: " + page.getTotalResultSetSize());
	      
		return orderIdList;
	}
	
		
/*
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 * Get DFP report using start date and end date
 * @param DfpServices dfpServices, DfpSession session
 *        String startDate,String endDate
 * @return String downloadUrl
 */
 public  String getDFPLinMediaReportForMobilePlatform(DfpServices dfpServices, DfpSession session,String start,String end) throws Exception{
	 log.info("dates..start:"+start+" and end:"+end);
	 String [] startArray=start.split("-");
	 String [] endArray=end.split("-");
	 
     // Create report query.
     ReportQuery reportQuery = new ReportQuery();
     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
    
     Date startDate=new Date();
     
     startDate.setDay(Integer.parseInt(startArray[2]));
     startDate.setMonth(Integer.parseInt(startArray[1]));
     startDate.setYear(Integer.parseInt(startArray[0]));
     
     Date endDate=new Date();
     endDate.setDay(Integer.parseInt(endArray[2]));
     endDate.setMonth(Integer.parseInt(endArray[1]));
     endDate.setYear(Integer.parseInt(endArray[0]));
     
     reportQuery.setStartDate(startDate);
     reportQuery.setEndDate(endDate);
     
     Dimension[] dimensionArray=new Dimension[] 
                          	    		    {
			  Dimension.DATE, 
			  Dimension.ADVERTISER_ID,
			  Dimension.ADVERTISER_NAME,
			  Dimension.AD_UNIT_ID,
			  Dimension.AD_UNIT_NAME,
			  Dimension.ORDER_ID,
			  Dimension.ORDER_NAME,
			  Dimension.LINE_ITEM_ID,
			  Dimension.LINE_ITEM_NAME,
			  Dimension.LINE_ITEM_TYPE,
			  Dimension.CREATIVE_ID,
			  Dimension.CREATIVE_NAME,
			  Dimension.CREATIVE_SIZE,
			  Dimension.CREATIVE_TYPE,
			  Dimension.SALESPERSON_NAME
	 };
     
     Column[] columnArray= new Column[] {
	    		  Column.AD_SERVER_IMPRESSIONS,
	    		  Column.AD_SERVER_CLICKS,
	    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
	    		  Column.AD_SERVER_CTR,
	    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
	    		  Column.AD_SERVER_CPD_REVENUE,
	    		  Column.AD_SERVER_ALL_REVENUE,	    		
	    		  Column.AD_SERVER_DELIVERY_INDICATOR,
	    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
	    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
	 };
     
     
     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
    		 DimensionAttribute.ORDER_AGENCY,
    		 DimensionAttribute.ORDER_AGENCY_ID,	    		 
    		 //DimensionAttribute.ORDER_EXTERNAL_ID,  // Not allowed
    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.ORDER_PO_NUMBER,
    		 DimensionAttribute.ORDER_START_DATE_TIME,
    		 DimensionAttribute.ORDER_END_DATE_TIME,
    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
     };
   
     /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{
    		 DimensionFilter.MOBILE_LINE_ITEMS
     };
     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));*/
     
     /****** using statement code starts *******/	   
     Statement statement=new Statement();	    
     StringBuffer query=new StringBuffer();
     query.append(" WHERE AD_UNIT_ANCESTOR_AD_UNIT_ID IN (55958262,55958382,55960542,55960662,55960782,55960902,32578062,54699942,57908742,54774462,54775422,54915822,55276902,57730782,54767742,54767862,54821502,55267902,54778302,54779262,54914622,55288662,51758982,51776502,54780222,54781182,54962502,55272822,54772422,54773502,54917622,55277862,51918942,51919902,56926302,57081222,58775502,51920862,51921822,58447182,51922782,51923742,57037662,51924702,51925662,56926662,57227862,51926622,51927582,57075462,54765222,54765342,54933942,55288422,51928542,51929502,56980422,51930462,51931542,56960622,57486942,58600302,54776382,54777342,54960582,55288542,54769662,54769782,54917982,55123782,51932502,51933462,57038502,32656902,54381582,54381702,54459942,54639942,51934422,51935382,56876022,51936582,51937542,57067062,51938502,51939462,56976102,57245262,51944742,51945702,57086262,51940902,51941862,57291462,51942822,51943782,56972622,57331422)" );
	 //query.append(" AND  CREATIVE_SIZE IN ('320 x 50', '728 x 90', '300 x 250') ");
     log.info("PQL Query : "+query.toString());
     statement.setQuery(query.toString());		     
     reportQuery.setStatement(statement);
     /****** using statement code ends *******/	   
     
     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
    
     
     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
     return downloadUrl;
 }
 
 public  String getDFPReportByAdUnitIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> adUnitIds) throws Exception{
	 
	 log.info("dates..start:"+start+" and end:"+end);
	 String [] startArray=start.split("-");
	 String [] endArray=end.split("-");
	 
     // Create report query.
     ReportQuery reportQuery = new ReportQuery();
     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
    
     Date startDate=new Date();
     
     startDate.setDay(Integer.parseInt(startArray[2]));
     startDate.setMonth(Integer.parseInt(startArray[1]));
     startDate.setYear(Integer.parseInt(startArray[0]));
     
     Date endDate=new Date();
     endDate.setDay(Integer.parseInt(endArray[2]));
     endDate.setMonth(Integer.parseInt(endArray[1]));
     endDate.setYear(Integer.parseInt(endArray[0]));
     
     reportQuery.setStartDate(startDate);
     reportQuery.setEndDate(endDate);
     
     Dimension[] dimensionArray=new Dimension[] 
                          	    		    {
			  Dimension.DATE, 
			  Dimension.ADVERTISER_ID,
			  Dimension.ADVERTISER_NAME,
			  Dimension.AD_UNIT_ID,
			  Dimension.AD_UNIT_NAME,
			  Dimension.ORDER_ID,
			  Dimension.ORDER_NAME,
			  Dimension.LINE_ITEM_ID,
			  Dimension.LINE_ITEM_NAME,
			  Dimension.LINE_ITEM_TYPE,
			  Dimension.CREATIVE_ID,
			  Dimension.CREATIVE_NAME,
			  Dimension.CREATIVE_SIZE,
			  Dimension.CREATIVE_TYPE,
			  Dimension.SALESPERSON_NAME
	 };
     
     Column[] columnArray= new Column[] {
	    		  Column.AD_SERVER_IMPRESSIONS,
	    		  Column.AD_SERVER_CLICKS,
	    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
	    		  Column.AD_SERVER_CTR,
	    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
	    		  Column.AD_SERVER_CPD_REVENUE,
	    		  Column.AD_SERVER_ALL_REVENUE,	    		
	    		  Column.AD_SERVER_DELIVERY_INDICATOR,
	    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
	    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
	 };
     
     
     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
    		 DimensionAttribute.ORDER_AGENCY,
    		 DimensionAttribute.ORDER_AGENCY_ID,	
    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.ORDER_PO_NUMBER,
    		 DimensionAttribute.ORDER_START_DATE_TIME,
    		 DimensionAttribute.ORDER_END_DATE_TIME,
    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
     };
   
     DimensionFilter[] dimFilterArray=
    		 new DimensionFilter[]{	
    		 DimensionFilter.MOBILE_LINE_ITEMS
    		 };
    //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
     
     /****** using statement code starts *******/	   
     Statement statement=new Statement();	    
     StringBuffer query=new StringBuffer();
     query.append(" WHERE AD_UNIT_ANCESTOR_AD_UNIT_ID IN ( ");
     
     for(int i=0;i<adUnitIds.size();i++){
    	 if(i==0){
    		 query.append(adUnitIds.get(i));
    	 }else{
    		 query.append(",");
    		 query.append(adUnitIds.get(i));
    	 }
     }
     query.append(" )");
    
     log.info("PQL Query : "+query.toString());
     statement.setQuery(query.toString());		     
     reportQuery.setStatement(statement);
     /****** using statement code ends *******/	   
     
     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
    
     
     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
     return downloadUrl;
 }
 
 public boolean updateAccounts(DfpServices dfpServices, DfpSession dfpSession, String adServerId, String adServerUserName, java.util.Date startDate, java.util.Date endDate, StringBuilder summary) throws Exception {
		log.info("Inside updateAccounts at : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
		IUserDetailsDAO dao = new UserDetailsDAO();
		String str = "";
		java.util.Date dateNow = DateUtil.getYesterdayDate("yyyy-MM-dd");
		boolean result = false;
		
		try {
			CompanyServiceInterface companyService = dfpServices.get(dfpSession, CompanyServiceInterface.class);
			Statement filterStatement = new Statement();
			CompanyPage companyPage = new CompanyPage();
			
			if(startDate == null || endDate == null) {
				summary.append("lastModifiedDateTime >= "+DATE_FORMAT.format(dateNow)+"\n ");
				filterStatement = new StatementBuilder()
								.where("WHERE type IN ('"+advertiserForDFPQuery+"','"+agencyForDFPQuery+"') AND lastModifiedDateTime >= :dateTimeString")
								.withBindVariableValue("dateTimeString", DATE_FORMAT.format(dateNow))
								.toStatement();
			}
			else {
				filterStatement = new StatementBuilder()
								.where("WHERE type IN ('"+advertiserForDFPQuery+"','"+agencyForDFPQuery+"') AND lastModifiedDateTime >= :startDateTimeString AND lastModifiedDateTime <= :endDateTimeString")
								.withBindVariableValue("startDateTimeString", DATE_TIME_FORMAT.format(startDate))
								.withBindVariableValue("endDateTimeString", DATE_TIME_FORMAT.format(endDate))
								.toStatement();
			}
			companyPage = companyService.getCompaniesByStatement(filterStatement);
			log.info("Agency/advertiser fetch query completed at : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			// add new or modified accounts
			if (companyPage.getResults() != null) {
			  String id = "";
			  String accountDfpId = null;
			  String dfpAccountName = "";
			  String accountType = "";
			  AccountsEntity accountsObj = null;
			  for (Company company : companyPage.getResults()) {
				  if(company != null && company.getId() != null && company.getName() != null && company.getType() != null) {
					  accountDfpId = String.valueOf(company.getId());
					  id = adServerId + "__" + adServerUserName + "__" + accountDfpId;
					  dfpAccountName = company.getName().trim();
					  accountType = company.getType().toString();
					  
					  accountsObj = dao.getAccountsObjById(id);
					  if(accountsObj != null && accountsObj.getId() != null) {
						  accountsObj.setId(id);
						  accountsObj.setAccountDfpId(accountDfpId);
						  if(accountsObj.getAccountName() == null || accountsObj.getAccountName().trim().length() == 0) {
							  accountsObj.setAccountName(dfpAccountName);
						  }
						  
						  if(accountType.equals(advertiserForDFPQuery)) {
							  accountsObj.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
						  }
						  else if(accountType.equals(agencyForDFPQuery)) {
							  accountsObj.setAccountType(LinMobileConstants.AGENCY_ID_PREFIX);
						  }
						  
						  if(accountsObj.getStatus() == null || accountsObj.getStatus().trim().length() == 0) {
							  accountsObj.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
						  }
						  accountsObj.setDfpAccountName(dfpAccountName);
						  accountsObj.setAdServerId(adServerId);
						  accountsObj.setAdServerUserName(adServerUserName);
						  
						  dao.saveObject(accountsObj);
						  str = "Account updated successfully, Account Name : "+dfpAccountName+", Account id : "+accountDfpId+", Account type : "+accountType+", Datastore Id : "+id;
						  log.info(str);
						  summary.append(str+"\n ");
					  }
					  else {
						  str = "Account does not exist in DataStore, Account Name : "+dfpAccountName+", Account id : "+accountDfpId+", Account type : "+accountType;
						  log.warning(str);
						  summary.append(str+"\n ");
					  }
				  }
			  }
			  str = "Number of Accounts found: " + companyPage.getTotalResultSetSize();
			  log.info(str);
			  summary.append(str+"<br>");
			}
			result = true;
		}
		catch (Exception e) {
			log.severe("Exception in updateAccounts of DFPReportService : " + e.getMessage()+" at : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			e.printStackTrace();
			throw e;
		}
		  
		return result;
	}

	/*public List<LineItemDTO> searchAccounts(DfpServices dfpServices, DfpSession dfpSession, String prefixStr) throws Exception {
		List<LineItemDTO> accountsList = new ArrayList<LineItemDTO>();
		try {
			CompanyServiceInterface companyService = dfpServices.get(dfpSession, CompanyServiceInterface.class);
			Statement filterStatement = new StatementBuilder()
							.where("WHERE type IN ('"+advertiserForDFPQuery+"','"+agencyForDFPQuery+"') AND name LIKE '%"+prefixStr+"%' ORDER BY name LIMIT 100")
							.toStatement();
			CompanyPage companyPage = companyService.getCompaniesByStatement(filterStatement);
			
			if (companyPage.getResults() != null) {
			  for (Company company : companyPage.getResults()) {
				  if(company != null && company.getId() != null && company.getName() != null && company.getType() != null) {
					  LineItemDTO obj = new LineItemDTO();
					  obj.setAccountId(String.valueOf(company.getId()));
					  obj.setAccountName(company.getName().trim());
					  if((company.getType().toString()).equals(advertiserForDFPQuery)) {
						  obj.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
					  }
					  else if((company.getType().toString()).equals(agencyForDFPQuery)) {
						  obj.setAccountType(LinMobileConstants.AGENCY_ID_PREFIX);
					  }
					  accountsList.add(obj);
				  }
			  }
			  log.info("Matching Accounts resultSet size : " + companyPage.getTotalResultSetSize());
			}
		}
		catch (Exception e) {
			log.severe("Exception in searchAccounts of DFPReportService : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return accountsList;
	}*/

public boolean getAccountsFromDFP(DfpServices dfpServices, DfpSession dfpSession, String adServerId, String adServerUsername, String prefixStr, List<AccountDTO> matchingAccountsList, StringBuilder summary) throws Exception {
	 	boolean result = false;
	 	String str = "";
		List<AccountDTO> accountsList = new ArrayList<AccountDTO>();
		try {
			log.info("Initialising CompanyService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			CompanyServiceInterface companyService = dfpServices.get(dfpSession, CompanyServiceInterface.class);
			String query = "where type IN ('"+advertiserForDFPQuery+"','"+agencyForDFPQuery+"') ORDER BY name";
			log.info("PQL query : "+query);
			/*Statement filterStatement = new StatementBuilder()
							.where(query)
							.toStatement();*/
			Statement filterStatement = new Statement();
			filterStatement.setQuery(query);
			log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			CompanyPage companyPage = companyService.getCompaniesByStatement(filterStatement);
			log.info("CompanyPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			if (companyPage.getResults() != null) {
				str = "Accounts resultSet size : " + companyPage.getResults().size();
				log.info(str);
				summary.append(str+"<br>");
				if(prefixStr == null || prefixStr.trim().length() == 0) {
					for (Company company : companyPage.getResults()) {
						if(company != null && company.getId() != null && company.getName() != null && company.getType() != null) {
							AccountDTO obj = new AccountDTO();
							obj.setId(String.valueOf(company.getId()));
							obj.setName(company.getName().trim());
							obj.setType(LinMobileConstants.AGENCY_ID_PREFIX);
							if((company.getType().toString()).equals(advertiserForDFPQuery)) {
								obj.setType(LinMobileConstants.ADVERTISER_ID_PREFIX);
							}
							accountsList.add(obj);
						}
					}
				}
				else {
					for (Company company : companyPage.getResults()) {
						if(company != null && company.getId() != null && company.getName() != null && company.getType() != null) {
							AccountDTO obj = new AccountDTO();
							obj.setId(String.valueOf(company.getId()));
							obj.setName(company.getName().trim());
							obj.setType(LinMobileConstants.AGENCY_ID_PREFIX);
							if((company.getType().toString()).equals(advertiserForDFPQuery)) {
								obj.setType(LinMobileConstants.ADVERTISER_ID_PREFIX);
							}
							accountsList.add(obj);
							if(matchingAccountsList.size() < 20 && obj.getName().toLowerCase().contains(prefixStr)) {
								matchingAccountsList.add(obj);
							}
						}
					}
					str = "MatchingAccountsList size : "+matchingAccountsList.size();
					log.info(str);
					summary.append(str+"<br>");
				}
				MemcacheUtil.setDFPAccountsInCache(accountsList, adServerId + adServerUsername);
				str = "Memcache created successfully, size : "+accountsList.size();
				log.info(str);
				summary.append(str+"<br>");
			}
			result = true;
		}
		catch (Exception e) {
			log.severe("Exception in getAccountsFromDFP of DFPReportService : " + e.getMessage());
			throw e;
		}
		return result;
	}
	
	public boolean getTopLevelAdUnitsFromDFP(DfpServices dfpServices, DfpSession dfpSession, String adServerId, String adServerUsername, String prefixStr, List<LineItemDTO> matchingPropertiesList, StringBuilder summary) throws Exception {
	 	boolean result = false;
	 	String str = "";
		List<LineItemDTO> propertiesList = new ArrayList<LineItemDTO>();
		try {
			log.info("Initialising InventoryService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			InventoryServiceInterface inventoryService = dfpServices.get(dfpSession, InventoryServiceInterface.class);
			
			log.info("Initialising NetworkServiceInterface, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
	        NetworkServiceInterface networkService = dfpServices.get(dfpSession, NetworkServiceInterface.class);
	        
	        log.info("Getting parentAdUnitId, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
	        String parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
	        log.info("parentAdUnitId : "+parentAdUnitId);
	        
	        StatementBuilder statementBuilder = new StatementBuilder()
	            .where("parentId = :parentId and status = 'ACTIVE' ")  
	            .orderBy("name")
	            .withBindVariableValue("parentId", parentAdUnitId);
	        
	        log.info("Initialising AdUnitPage, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
	        AdUnitPage adUnitPage = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());
	        log.info("AdUnitPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			if (adUnitPage.getResults() != null) {
				str = "AdUnit resultSet size : " + adUnitPage.getResults().size();
				log.info(str);
				summary.append(str+"<br>");
				
				if(prefixStr == null || prefixStr.trim().length() == 0) {
					for (AdUnit adUnit : adUnitPage.getResults()) {
						if(adUnit != null && adUnit.getId() != null && adUnit.getName() != null) {
							LineItemDTO obj = new LineItemDTO();
							obj.setSiteId(String.valueOf(adUnit.getId()));
							obj.setSiteName(adUnit.getName().trim());
							obj.setParentId(String.valueOf(adUnit.getParentId()));
							obj.setHasChildren(adUnit.isHasChildren());
							obj.setParentName(adUnit.getName().trim());
							propertiesList.add(obj);
							matchingPropertiesList.add(obj);
						}
					}
				}
				else {
					for (AdUnit adUnit : adUnitPage.getResults()) {
						if(adUnit != null && adUnit.getId() != null && adUnit.getName() != null) {
							LineItemDTO obj = new LineItemDTO();
							obj.setSiteId(String.valueOf(adUnit.getId()));
							obj.setSiteName(adUnit.getName().trim());
							propertiesList.add(obj);
							if(matchingPropertiesList.size() < 20 && obj.getSiteName().toLowerCase().contains(prefixStr)) {
								matchingPropertiesList.add(obj);
							}
						}
					}
					str = "MatchingPropertiesList size : "+matchingPropertiesList.size();
					log.info(str);
					summary.append(str+"<br>");
				}
				MemcacheUtil.setDFPPropertiesInCache(propertiesList, adServerId + adServerUsername);
				str = "MemcacheUtil created succesasfully for DFP Properties, size : "+propertiesList.size();
				log.info(str);
				summary.append(str+"<br>");
			}
			result = true;
		}
		catch (Exception e) {
			log.severe("Exception in getPropertiesFromDFP of DFPReportService : " + e.getMessage());
			throw e;
		}
		return result;
	}
 	
 	
 	public  String getDFPReportByAccountIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> accountIds) throws ApiException_Exception, InterruptedException {
	 
	 log.info("dates..start:"+start+" and end:"+end);
	 String [] startArray=start.split("-");
	 String [] endArray=end.split("-");
	 
     // Create report query.
     ReportQuery reportQuery = new ReportQuery();
     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
    
     Date startDate=new Date();
     
     startDate.setDay(Integer.parseInt(startArray[2]));
     startDate.setMonth(Integer.parseInt(startArray[1]));
     startDate.setYear(Integer.parseInt(startArray[0]));
     
     Date endDate=new Date();
     endDate.setDay(Integer.parseInt(endArray[2]));
     endDate.setMonth(Integer.parseInt(endArray[1]));
     endDate.setYear(Integer.parseInt(endArray[0]));
     
     reportQuery.setStartDate(startDate);
     reportQuery.setEndDate(endDate);
     
     Dimension[] dimensionArray=new Dimension[] 
                          	    		    {
			  Dimension.DATE, 
			  Dimension.ADVERTISER_ID,
			  Dimension.ADVERTISER_NAME,
			  Dimension.AD_UNIT_ID,
			  Dimension.AD_UNIT_NAME,
			  Dimension.ORDER_ID,
			  Dimension.ORDER_NAME,
			  Dimension.LINE_ITEM_ID,
			  Dimension.LINE_ITEM_NAME,
			  Dimension.LINE_ITEM_TYPE,
			  Dimension.CREATIVE_ID,
			  Dimension.CREATIVE_NAME,
			  Dimension.CREATIVE_SIZE,
			  Dimension.CREATIVE_TYPE,
			  Dimension.SALESPERSON_NAME
	 };
     
     Column[] columnArray= new Column[] {
	    		  Column.AD_SERVER_IMPRESSIONS,
	    		  Column.AD_SERVER_CLICKS,
	    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
	    		  Column.AD_SERVER_CTR,
	    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
	    		  Column.AD_SERVER_CPD_REVENUE,
	    		  Column.AD_SERVER_ALL_REVENUE,	    		
	    		  Column.AD_SERVER_DELIVERY_INDICATOR,
	    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
	    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
	    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
	 };
     
     
     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
    		 DimensionAttribute.ORDER_AGENCY,
    		 DimensionAttribute.ORDER_AGENCY_ID,	
    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
    		 DimensionAttribute.ORDER_PO_NUMBER,
    		 DimensionAttribute.ORDER_START_DATE_TIME,
    		 DimensionAttribute.ORDER_END_DATE_TIME,
    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
     };
   
  
     
     /****** using statement code starts *******/	   
     Statement statement=new Statement();	    
     StringBuffer query=new StringBuffer();
     query.append(" WHERE ADVERTISER_ID IN ( ");
     
     for(int i=0;i<accountIds.size();i++){
    	 if(i==0){
    		 query.append(accountIds.get(i));
    	 }else{
    		 query.append(",");
    		 query.append(accountIds.get(i));
    	 }
     }
     
     query.append(")");     
    
     log.info("PQL Query : "+query.toString());
     statement.setQuery(query.toString());		     
     reportQuery.setStatement(statement); 
     /****** using statement code ends *******/	   
     
     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
    
     
     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
     return downloadUrl;
 }
 
 	
 	public  String getDFPReportByOrderIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> orderIds) throws ApiException_Exception, InterruptedException {
	 
	 
     return getDFPReportByOrderIds(dfpServices, session, start, end, orderIds,null);
			
 }
 	
 	public String getDFPReportByOrderIds(DfpServices dfpServices, 
			DfpSession session,String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws ApiException_Exception, InterruptedException {
 		
 		log.info("dates..start:"+start+" and end:"+end);
 		 String [] startArray=start.split("-");
 		 String [] endArray=end.split("-");
 		 
 	     // Create report query.
 	     ReportQuery reportQuery = new ReportQuery();
 	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
 	     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
 	    
 	     Date startDate=new Date();
 	     
 	     startDate.setDay(Integer.parseInt(startArray[2]));
 	     startDate.setMonth(Integer.parseInt(startArray[1]));
 	     startDate.setYear(Integer.parseInt(startArray[0]));
 	     
 	     Date endDate=new Date();
 	     endDate.setDay(Integer.parseInt(endArray[2]));
 	     endDate.setMonth(Integer.parseInt(endArray[1]));
 	     endDate.setYear(Integer.parseInt(endArray[0]));
 	     
 	     reportQuery.setStartDate(startDate);
 	     reportQuery.setEndDate(endDate);
 	     
 	     Dimension[] dimensionArray=new Dimension[] 
 	                          	    		    {
 				  Dimension.DATE, 
 				  Dimension.ADVERTISER_ID,
 				  Dimension.ADVERTISER_NAME,
 				  Dimension.AD_UNIT_ID,
 				  Dimension.AD_UNIT_NAME,
 				  Dimension.ORDER_ID,
 				  Dimension.ORDER_NAME,
 				  Dimension.LINE_ITEM_ID,
 				  Dimension.LINE_ITEM_NAME,
 				  Dimension.LINE_ITEM_TYPE,
 				  Dimension.CREATIVE_ID,
 				  Dimension.CREATIVE_NAME,
 				  Dimension.CREATIVE_SIZE,
 				  Dimension.CREATIVE_TYPE,
 				  Dimension.SALESPERSON_NAME
 		 };
 	     
 	     Column[] columnArray= new Column[] {
 		    		  Column.AD_SERVER_IMPRESSIONS,
 		    		  Column.AD_SERVER_CLICKS,
 		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
 		    		  Column.AD_SERVER_CTR,
 		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
 		    		  Column.AD_SERVER_CPD_REVENUE,
 		    		  Column.AD_SERVER_ALL_REVENUE,	    		
 		    		  Column.AD_SERVER_DELIVERY_INDICATOR,
 		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
 		    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
 		    		  Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
 		 };
 	     
 	     
 	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
 	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
 	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
 	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
 	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
 	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
 	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
 	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
 	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
 	    		 DimensionAttribute.ORDER_AGENCY,
 	    		 DimensionAttribute.ORDER_AGENCY_ID,	
 	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
 	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
 	    		 DimensionAttribute.ORDER_PO_NUMBER,
 	    		 DimensionAttribute.ORDER_START_DATE_TIME,
 	    		 DimensionAttribute.ORDER_END_DATE_TIME,
 	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
 	     };
 	   
 	  
 	     
 	     /****** using statement code starts *******/	   
 	     Statement statement=new Statement();	    
 	     StringBuffer query=new StringBuffer();
 	     if(commaSepratedOrderIds!=null && commaSepratedOrderIds.trim().length()>0){
 	    	 query.append(" WHERE ORDER_ID IN ("+orderIds.get(0)+")");
 	     }else{
 	    	 query.append(" WHERE ORDER_ID IN ( ");
 	 	     
 	 	     for(int i=0;i<orderIds.size();i++){
 	 	    	 if(i==0){
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }else{
 	 	    		 query.append(",");
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }
 	 	     }
 	 	     query.append(" )");    
 	     }
 	    
 	    
 	    
 	     log.info("PQL Query : "+query.toString());
 	     statement.setQuery(query.toString());		     
 	     reportQuery.setStatement(statement);
 	     /****** using statement code ends *******/	   
 	     
 	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
 	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
 	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
 	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
 	    
 	     
 	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
 	    return downloadUrl;
 	}
	
 	
	/*
	 * @author Shubham Goel(shubham.goel@mediaagility.com)
	 * 
	 * Get LinMedia PerformanceByLocation report from DFP on the basis of accountIds 
	 
	 */
	public  String getDFPReportByLocationByAccountIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception{		

	     String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);	     
	    	    
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  /*Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,*/
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME
				  /*Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME*/
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  //Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.ACTIVE_AD_UNITS	    		 
	     };
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     query.append(" WHERE ADVERTISER_ID IN ( ");
	     
	     for(int i=0;i<accountIds.size();i++){
	    	 if(i==0){
	    		 query.append(accountIds.get(i));
	    	 }else{
	    		 query.append(",");
	    		 query.append(accountIds.get(i));
	    	 }
	     }
	     query.append(" )"); 
	     log.info("PQL Query : "+query.toString());
	    /* statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);*/
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	public  String getDFPReportByLocationByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception{		

	    
	     return getDFPReportByLocationByOrderIds(dfpServices, session, start, end, orderIds, null);
	}
	
	public  String getDFPReportByLocationByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception{		

	     String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);	     
	    	    
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  /*Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,*/
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME
				  /*Dimension.CREATIVE_ID,
				  Dimension.CREATIVE_NAME,
				  Dimension.CREATIVE_SIZE,
				  Dimension.CREATIVE_TYPE,
				  Dimension.SALESPERSON_NAME*/
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS,
		    		  Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
		    		  Column.AD_SERVER_CTR,
		    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,
		    		  Column.AD_SERVER_CPD_REVENUE,
		    		  Column.AD_SERVER_ALL_REVENUE,	    		
		    		  //Column.AD_SERVER_DELIVERY_INDICATOR,
		    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
	    		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
	    		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
	    		 DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
	    		 DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	    		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
	    		 DimensionAttribute.ORDER_AGENCY,
	    		 DimensionAttribute.ORDER_AGENCY_ID,
	    		 DimensionAttribute.ORDER_LIFETIME_CLICKS,
	    		 DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
	    		 DimensionAttribute.ORDER_PO_NUMBER,
	    		 DimensionAttribute.ORDER_START_DATE_TIME,
	    		 DimensionAttribute.ORDER_END_DATE_TIME,
	    		 DimensionAttribute.ORDER_TRAFFICKER	    		 
	     };
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.ACTIVE_AD_UNITS	    		 
	     };
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     if(commaSepratedOrderIds!=null && commaSepratedOrderIds.trim().length()>0){
 	    	 query.append(" WHERE ORDER_ID IN ("+orderIds.get(0)+")");
 	     }else{
 	    	 query.append(" WHERE ORDER_ID IN ( ");
 	 	     
 	 	     for(int i=0;i<orderIds.size();i++){
 	 	    	 if(i==0){
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }else{
 	 	    		 query.append(",");
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }
 	 	     }
 	 	     query.append(" )");    
 	     } 
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	/*
	 *  @author Shubham Goel(shubham.goel@mediaagility.com)
	 * Get DFP target report using start date and end date
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<String> accountIds,String targetFilter
	 * @return String downloadUrl
	 */
	public  String getDFPTargetReportByAccountIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds,String targetFilter){
		 log.info("start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.GENERIC_CRITERION_NAME
		  };
		     
		  Column[] columnArray= new Column[] {
			    		  Column.AD_SERVER_IMPRESSIONS,
			    		  Column.AD_SERVER_CLICKS,
			    		  Column.AD_SERVER_CTR,
			    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,	
			    		  Column.AD_SERVER_DELIVERY_INDICATOR,
			    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		  }; 
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     query.append(" WHERE ADVERTISER_ID IN ( ");
	     
	     for(int i=0;i<accountIds.size();i++){
	    	 if(i==0){
	    		 query.append(accountIds.get(i));
	    	 }else{
	    		 query.append(",");
	    		 query.append(accountIds.get(i));
	    	 }
	     }
	     query.append(" )");
	     
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	    
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));	     
	     String downloadUrl=null;
		try {
			downloadUrl = downloadReport(dfpServices, session, reportQuery);
		} catch (ApiException_Exception | InterruptedException e) {
			log.severe("Download report exception -- "+e.getMessage());
			e.printStackTrace();
		}
	     return downloadUrl;
	}
	
	public  String getDFPTargetReportByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds){
		 log.info("start:"+start+" and end:"+end);
	
	     return getDFPTargetReportByOrderIds(dfpServices, session, start, end, orderIds, null);
	}
	
	public  String getDFPTargetReportByOrderIds(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds){
		 log.info("start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.GENERIC_CRITERION_NAME
		  };
		     
		  Column[] columnArray= new Column[] {
			    		  Column.AD_SERVER_IMPRESSIONS,
			    		  Column.AD_SERVER_CLICKS,
			    		  Column.AD_SERVER_CTR,
			    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,	
			    		  Column.AD_SERVER_DELIVERY_INDICATOR,
			    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		  }; 
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     if(commaSepratedOrderIds!=null && commaSepratedOrderIds.trim().length()>0){
 	    	 query.append(" WHERE ORDER_ID IN ("+orderIds.get(0)+")");
 	     }else{
 	    	 query.append(" WHERE ORDER_ID IN ( ");
 	 	     
 	 	     for(int i=0;i<orderIds.size();i++){
 	 	    	 if(i==0){
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }else{
 	 	    		 query.append(",");
 	 	    		 query.append(orderIds.get(i));
 	 	    	 }
 	 	     }
 	 	     query.append(" )");    
 	     }
	     
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	    
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));	     
	     String downloadUrl=null;
		try {
			downloadUrl = downloadReport(dfpServices, session, reportQuery);
		} catch (ApiException_Exception | InterruptedException e) {
			log.severe("Download report exception -- "+e.getMessage());
			e.printStackTrace();
		}
	     return downloadUrl;
	}
	/*
	 * @author Shubham Goel(shubham.goel@mediaagility.com)
	 * 
	 * Get 'Rich Media' Custom Event report using start date and end date 
	 *      for given orderIds
	 *      
	 * @param DfpServices dfpServices, DfpSession session
	 *        String startDate,String endDate,List<Long> orderIds
	 * @return String downloadUrl
	 */
	public  String getRichMediaCustomEventReportByAccountId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> accountIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE,
					  Dimension.CUSTOM_EVENT_ID,
					  Dimension.CUSTOM_EVENT_NAME,
					  Dimension.CUSTOM_EVENT_TYPE
					  
		 };
		     
		 Column[] columnArray= new Column[] {
		    		 
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_COUNT,
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_TIME		    		  
		    		  
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				 
				 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT
		   		 
		 };
	     
		 DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
		 
		 if(accountIds.size()>0){
			    Statement statement=new Statement();	    
			     StringBuffer query=new StringBuffer();
			     query.append(" WHERE ADVERTISER_ID IN ( ");
			     
			     for(int i=0;i<accountIds.size();i++){
			    	 if(i==0){
			    		 query.append(accountIds.get(i));
			    	 }else{
			    		 query.append(",");
			    		 query.append(accountIds.get(i));
			    	 }
			     }
			     query.append(" )");	    
			     log.info("PQL Query : "+query.toString());
			     statement.setQuery(query.toString());		     
			     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderIds given, loading report for all orderIds from dfp...apply target platform:mobile");
	    	 reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }
        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	public  String getRichMediaCustomEventReportByOrderId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 
		 return getRichMediaCustomEventReportByOrderId(dfpServices, session, start, end, orderIds, null);
	}
	
	
	public  String getRichMediaCustomEventReportByOrderId(DfpServices dfpServices, DfpSession session,
			String start,String end,List<String> orderIds, String commaSepratedOrderIds) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] 
		                          	    		    {
					  Dimension.DATE, 
					  Dimension.ADVERTISER_ID,
					  Dimension.ADVERTISER_NAME,
					  Dimension.ORDER_ID,
					  Dimension.ORDER_NAME,
					  Dimension.LINE_ITEM_ID,
					  Dimension.LINE_ITEM_NAME,
					  Dimension.LINE_ITEM_TYPE,
					  Dimension.CREATIVE_ID,
					  Dimension.CREATIVE_NAME,
					  Dimension.CREATIVE_SIZE,
					  Dimension.CREATIVE_TYPE,
					  Dimension.CUSTOM_EVENT_ID,
					  Dimension.CUSTOM_EVENT_NAME,
					  Dimension.CUSTOM_EVENT_TYPE
					  
		 };
		     
		 Column[] columnArray= new Column[] {
		    		 
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_COUNT,
		    		  Column.RICH_MEDIA_CUSTOM_EVENT_TIME		    		  
		    		  
		 };
		     
		     
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				 
				 DimensionAttribute.ORDER_START_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
		   		 DimensionAttribute.ORDER_END_DATE_TIME,
		   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
		   		 DimensionAttribute.ORDER_AGENCY,
		   		 DimensionAttribute.ORDER_AGENCY_ID,
		   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
		   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT
		   		 
		 };
	     
		 DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
		 
		 if(orderIds.size()>0){
			    Statement statement=new Statement();	    
			     StringBuffer query=new StringBuffer();
			     if(commaSepratedOrderIds!=null && commaSepratedOrderIds.trim().length()>0){
		 	    	 query.append(" WHERE ORDER_ID IN ("+orderIds.get(0)+")");
		 	     }else{
		 	    	 query.append(" WHERE ORDER_ID IN ( ");
		 	 	     
		 	 	     for(int i=0;i<orderIds.size();i++){
		 	 	    	 if(i==0){
		 	 	    		 query.append(orderIds.get(i));
		 	 	    	 }else{
		 	 	    		 query.append(",");
		 	 	    		 query.append(orderIds.get(i));
		 	 	    	 }
		 	 	     }
		 	 	     query.append(" )");    
		 	     }
			     log.info("PQL Query : "+query.toString());
			     statement.setQuery(query.toString());		     
			     reportQuery.setStatement(statement);
	     }else{
	    	 log.warning("no orderIds given, loading report for all orderIds from dfp..");
	    	 //reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     }
        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	public List<LineItemDTO> loadAllAdUnits(DfpServices dfpServices, DfpSession dfpSession) throws Exception {
		List<LineItemDTO> siteList = new ArrayList<LineItemDTO>();
		try {
			log.info("Initialising InventoryService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			InventoryServiceInterface inventoryService = dfpServices.get(dfpSession, InventoryServiceInterface.class);
			Statement filterStatement = new StatementBuilder().toStatement();
			log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			AdUnitPage adUnitPage = inventoryService.getAdUnitsByStatement(filterStatement);
			log.info("AdUnitPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			if (adUnitPage.getResults() != null) {
			  for (AdUnit adUnit : adUnitPage.getResults()) {
				  if(adUnit != null && adUnit.getId() != null && adUnit.getName() != null) {
					  LineItemDTO obj = new LineItemDTO();
					  obj.setSiteId(String.valueOf(adUnit.getId()));
					  obj.setSiteName(adUnit.getName().trim());
					  obj.setParentId(String.valueOf(adUnit.getParentId()));
					  siteList.add(obj);
				  }
			  }
			  log.info("AdUnit resultSet size : " + siteList.size());
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadAllAdUnits of DFPReportService : " + e.getMessage());
		}
		return siteList;
	}
	
	public List<LineItemDTO> getAdUnitChilds(DfpServices dfpServices, DfpSession dfpSession, String adUnitId, String parentName) throws Exception {
		List<LineItemDTO> siteList = new ArrayList<LineItemDTO>();
		try {
			log.info("Initialising InventoryService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			InventoryServiceInterface inventoryService = dfpServices.get(dfpSession, InventoryServiceInterface.class);
			String query = "parentId = "+adUnitId+" and status = 'ACTIVE' ";
			log.info("PQL query : "+query);
			Statement filterStatement = new StatementBuilder()
							.where(query)
							.toStatement();
			log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			AdUnitPage adUnitPage = inventoryService.getAdUnitsByStatement(filterStatement);
			log.info("AdUnitPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			if (adUnitPage.getResults() != null) {
			  for (AdUnit adUnit : adUnitPage.getResults()) {
				  if(adUnit != null && adUnit.getId() != null && adUnit.getName() != null) {
					  LineItemDTO obj = new LineItemDTO();
					  obj.setSiteId(String.valueOf(adUnit.getId()));
					  obj.setSiteName(adUnit.getName().trim());
					  if(parentName == null || parentName.trim().length() == 0) {
						  obj.setParentName(adUnit.getName().trim());
					  }
					  else {
						  obj.setParentName(parentName.trim() + " - "+adUnit.getName().trim());
					  }
					  obj.setParentId(String.valueOf(adUnit.getParentId()));
					  obj.setHasChildren(adUnit.isHasChildren());
					  siteList.add(obj);
				  }
			  }
			  log.info("Number of matching AdUnit resultSet size : " + siteList.size());
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadAdUnitChilds of DFPReportService : " + e.getMessage());
		}
		return siteList;
	}


	@Override
	public List<AdUnitHierarchy> loadAllChildsInHierarchy(DfpServices dfpServices, DfpSession dfpSession, String adServerId, Map<String, String> parentIdMap, StringBuilder summary) throws Exception {
		List<AdUnitHierarchy> siteChildList = new ArrayList<>();
		String message = "";
		try {
			log.info("Initialising InventoryService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			InventoryServiceInterface inventoryService = dfpServices.get(dfpSession, InventoryServiceInterface.class);
			/*do {
				String commaSeperatedParenIds = "";
				if(parentIdMap != null && parentIdList.size() > 0) {
					for(int i=0; i<3; i++) {
						if(parentIdList.size() > 0) {
							commaSeperatedParenIds = commaSeperatedParenIds + parentIdList.get(0)+", ";
							parentIdList.remove(0);
						}
					}
					if(commaSeperatedParenIds.lastIndexOf(",") != -1) {
						commaSeperatedParenIds = commaSeperatedParenIds.substring(0, commaSeperatedParenIds.lastIndexOf(","));
					}
				}
				if(commaSeperatedParenIds.length() > 0) {
					String query = "parentId IN ("+commaSeperatedParenIds+") and status = 'ACTIVE' ";
					log.info("PQL query : "+query);
					Statement filterStatement = new StatementBuilder()
									.where(query)
									.toStatement();
					log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					AdUnitPage adUnitPage = inventoryService.getAdUnitsByStatement(filterStatement);
					log.info("AdUnitPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					
					if (adUnitPage.getResults() != null) {
						for (AdUnit adUnit : adUnitPage.getResults()) {
							String adUnitId = adUnit.getId();
							if(adUnit != null && adUnitId != null && adUnit.getName() != null) {
								AdUnitHierarchy adUnitHierarchy = new AdUnitHierarchy();
								adUnitHierarchy.setId(adServerId+"_"+adUnitId);
								adUnitHierarchy.setAdServerId(adServerId);
								adUnitHierarchy.setAdUnitId(adUnitId);
								adUnitHierarchy.setAdUnitName(adUnit.getName().trim());
								adUnitHierarchy.setPid(adUnit.getParentId());
								if(!siteChildList.contains(adUnitHierarchy)) {
									siteChildList.add(adUnitHierarchy);
									parentIdList.add(adUnitId);
								}
							}
						}
					}
				}
				log.info("parentIdList size : "+parentIdList.size());
				log.info("......................................");
			} while(parentIdList != null && parentIdList.size() > 0);*/
			log.info("Child AdUnitHierarchy list size : " + siteChildList.size());
		}
		catch (Exception e) {
			message = "Exception in loadAllChildsInHierarchy of DFPReportService : " + e.getMessage();
			log.severe(message);
			summary.append(message+"<br>");
		}
		return siteChildList;
	}
	
	public List<AdUnitHierarchy> getRecentlyUpdatedAdUnitsHierarchy(DfpServices dfpServices, DfpSession dfpSession) throws Exception{
		
		List<AdUnit> adUnits = Lists.newArrayList();

		String adServerId=dfpSession.getNetworkCode();
		List<AdUnitHierarchy> adUnitHierarchyList=new ArrayList<AdUnitHierarchy>();
		
	    // Get the InventoryService.
	    InventoryServiceInterface inventoryService = dfpServices.get(dfpSession, InventoryServiceInterface.class);

	    // Create a statement to only fetch ad units updated or created since
	    // yesterday.

	    StatementBuilder statementBuilder = new StatementBuilder()
	        .where("lastModifiedDateTime >= :lastModifiedDateTime")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("lastModifiedDateTime",
	            DateTimes.toDateTime(Instant.now().minus(Duration.standardDays(1L)),
	                "America/New_York"));
	    
	    // Default for total result set size.
	    int totalResultSetSize = 0;
	    try {
	    	do {
		      // Get ad units by statement.
		      AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());
			  if (page.getResults() != null) {
			        totalResultSetSize = page.getTotalResultSetSize();
			        log.info("totalResultSetSize:"+totalResultSetSize);
			        int i = page.getStartIndex();
			        for (AdUnit adUnit : page.getResults()) {
			          //System.out.println("AdUnit Parent Path:"+adUnit.getParentPath().size()+" and active status:"+adUnit.getStatus().value());
			          List<AdUnitParent> adUnitparents=adUnit.getParentPath();
			          if(adUnitparents !=null){	        	  
			        	  //System.out.println("adUnitparents size :"+adUnitparents.size());
			        	  String adUnitId="";
						  String adUnitName="";
						  String canonicalPath="";
						  String parentId="";
						  AdUnitHierarchy adUnitObj=null;
						  
						  if(adUnitparents.size()==5){
							String adUnit1=adUnitparents.get(1).getName();	   						
							String adUnit2=adUnitparents.get(2).getName();
							String adUnit3=adUnitparents.get(3).getName();
							String adUnit4=adUnitparents.get(4).getName();
							//String adUnitId4=adUnitparents[4].getId();
							String adUnit5=adUnit.getName();
							String adUnitId5=adUnit.getId();
							
							adUnitId=adUnitId5;
			 					parentId=adUnit.getParentId();
			 					adUnitName=adUnit5;
			 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4+" > "+adUnit5;
			 					String id=adServerId+"_"+adUnitId;
			 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
							
						  }else if(adUnitparents.size()==4){
							String adUnit1=adUnitparents.get(1).getName();	   						
							String adUnit2=adUnitparents.get(2).getName();
							String adUnit3=adUnitparents.get(3).getName();
							String adUnit4=adUnit.getName();
							String adUnitId4=adUnit.getId();
							
							adUnitId=adUnitId4;
			 					parentId=adUnit.getParentId();
			 					adUnitName=adUnit4;
			 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4;
			 					String id=adServerId+"_"+adUnitId;
			 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
						  }else if(adUnitparents.size()==3){
							String adUnit1=adUnitparents.get(1).getName();	   						
							String adUnit2=adUnitparents.get(2).getName();
							String adUnit3=adUnit.getName();
							String adUnitId3=adUnit.getId();
							
							adUnitId=adUnitId3;
			 					parentId=adUnit.getParentId();
			 					adUnitName=adUnit3;
			 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3;
			 					String id=adServerId+"_"+adUnitId;
			 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath); 
						  }else if(adUnitparents.size()==2){
							String adUnit1=adUnitparents.get(1).getName();	
							String adUnit2=adUnit.getName();
							String adUnitId2=adUnit.getId();
							
							adUnitId=adUnitId2;
			 					parentId=adUnit.getParentId();
			 					adUnitName=adUnit2;
			 					canonicalPath=adUnit1+" > "+adUnit2;
			 					String id=adServerId+"_"+adUnitId;
			 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
						  }else if(adUnitparents.size()==1){
							
							String adUnit1=adUnit.getName();
							String adUnitId1=adUnit.getId();	   						
							adUnitId=adUnitId1;
			 					parentId=adUnit.getParentId();
			 					adUnitName=adUnit1;
			 					canonicalPath=adUnit1;
			 					String id=adServerId+"_"+adUnitId;
			 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
						  }else{
							  log.warning("=========================================Unhandled adUnit=======================");
						  }
						
						  if(adUnitObj !=null){
							  log.info("added : "+adUnitObj.toString());
							  adUnitHierarchyList.add(adUnitObj);
						  }   				
			          }
			        
			          i++;
			        }
			        adUnits.addAll(Lists.<AdUnit>newArrayList(page.getResults()));
			      }

			     statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
			    } while (statementBuilder.getOffset() < totalResultSetSize);

			    log.info("Number of results found: "+ totalResultSetSize);
		} catch (ApiException_Exception e) {
			log.severe("ApiException_Exception : "+e.getMessage());
			throw new Exception("ApiException_Exception : "+e.getMessage());
		}
	    
	    return adUnitHierarchyList;
	}
	
	public boolean updateCampaignDetailFromDFP(DfpServices dfpServices, DfpSession dfpSession, String commaSeperatedOrderIds, StringBuilder summary, String adServerCode) throws Exception {
	 	boolean result = false;
	 	String str = "OrderIds : " + commaSeperatedOrderIds;
		log.info(str);
		summary.append(str+"<br>");
		try {
			log.info("Initialising orderService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			OrderServiceInterface orderService = dfpServices.get(dfpSession, OrderServiceInterface.class);
			
			String query = " id IN ("+commaSeperatedOrderIds+") ";
			log.info("PQL query : "+query);
			Statement filterStatement = new StatementBuilder().where(query).toStatement();
			
			log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			OrderPage orderPage = orderService.getOrdersByStatement(filterStatement);
			log.info("OrderPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			if (orderPage.getResults() != null) {
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				str = "Order resultSet size : " + orderPage.getResults().size();
				log.info(str);
				summary.append(str+"<br>");
				
				for (Order order : orderPage.getResults()) {
					if(order != null && order.getId() != null && order.getId() > 0 && order.getStatus() != null) {
						SmartCampaignObj obj = smartCampaignPlannerDAO.getCampaignByDFPOrderId(order.getId(), adServerCode);
						if(obj != null) {
							obj.setDfpStatus(order.getStatus().value());
							if(obj.getCampaignStatus()!=null && !obj.getCampaignStatus().equals(CampaignStatusEnum.Paused.ordinal()+"")
									&& !obj.getCampaignStatus().equals(CampaignStatusEnum.Canceled.ordinal()+"")){
								obj.setCampaignStatus(convertDFPStatus(order.getStatus(),obj,order.isIsArchived()));
							}
							smartCampaignPlannerDAO.saveObject(obj);
							//log.info("DFP Id : "+order.getId()+", Name : "+order.getName()+", DFP Status : "+order.getStatus().value());
						}
					}
				}
			}
			result = true;
		}
		catch (Exception e) {
			str = "Exception in updateCampaignDetailFromDFP of DFPReportService : " + e.getMessage();
			log.severe(str);
			e.printStackTrace();
			summary.append(str+"<br>");
		}
		return result;
	}
	
	/*
	 * @author Shubham Goel
	 * convert DFP orderStatus to smartCampaignObj status and update the same 
	 *      
	 * @param OrderStatus dfpStatus,SmartCampaignObj obj,boolean isArchived
	 * @return String campaignStatus
	 */
	private String convertDFPStatus(OrderStatus dfpStatus,SmartCampaignObj obj,boolean isArchived) {
		String campaignStatus = "";
		try{
			if(dfpStatus != null && obj!=null) {
				
				List<String> lineItemStatusList = new ArrayList<>();
				if(isArchived){
					campaignStatus = CampaignStatusEnum.Archived.ordinal()+"";
				}else if(dfpStatus.equals(OrderStatus.DRAFT)){
					lineItemStatusList = getLineItemStatus(obj.getCampaignId());
					campaignStatus = campaignStatus(dfpStatus,lineItemStatusList);
				}else if(dfpStatus.equals(OrderStatus.PENDING_APPROVAL)){
					lineItemStatusList = getLineItemStatus(obj.getCampaignId());
					campaignStatus = campaignStatus(dfpStatus,lineItemStatusList);
				}else if(dfpStatus.equals(OrderStatus.APPROVED)){
					lineItemStatusList = getLineItemStatus(obj.getCampaignId());
					campaignStatus = campaignStatus(dfpStatus,lineItemStatusList);
				}else if(dfpStatus.equals(OrderStatus.PAUSED)){
					lineItemStatusList = getLineItemStatus(obj.getCampaignId());
					campaignStatus = campaignStatus(dfpStatus,lineItemStatusList);
				}else if(dfpStatus.equals(OrderStatus.CANCELED)){
					lineItemStatusList = getLineItemStatus(obj.getCampaignId());
					campaignStatus = campaignStatus(dfpStatus,lineItemStatusList);
				}
			}
			
			if(campaignStatus!=null && !campaignStatus.equals("")){
				return campaignStatus;
			}else{
				campaignStatus =  obj.getCampaignStatus();
			}
			
		}catch(Exception e){
			log.severe("Exception in convertDFPStatus in DFPReportService"+e.toString());
			e.printStackTrace();
		}
		return campaignStatus;
	}
	
	/*
	 * @author Shubham Goel
	 * convert DFP orderStatus to smartCampaignObj status using lineItem list, and update the same 
	 *      
	 * @param OrderStatus dfpStatus,List<String> lineItemStatusList
	 * @return String campaignStatus
	 */
	public String campaignStatus(OrderStatus dfpStatus,List<String> lineItemStatusList){
		String campaignStatus = "";
		try{
			if(dfpStatus!=null && lineItemStatusList!=null && lineItemStatusList.size()>0){
				if(dfpStatus.equals(OrderStatus.DRAFT) && lineItemStatusList.size()==1){
					if(lineItemStatusList.get(0).equals(ComputedStatus.DRAFT.value()) || lineItemStatusList.get(0).equals(ComputedStatus.NEEDS_CREATIVES.value())){
						campaignStatus =  CampaignStatusEnum.Ready.ordinal()+"";
					}
				}else if(dfpStatus.equals(OrderStatus.DRAFT) && lineItemStatusList.size()>1){
					for (int i=0;i<lineItemStatusList.size();i++) {
						if(lineItemStatusList.get(i).equals(ComputedStatus.DRAFT.value()) || lineItemStatusList.get(i).equals(ComputedStatus.NEEDS_CREATIVES.value())){
							campaignStatus =  CampaignStatusEnum.Ready.ordinal()+"";
						}else{
							campaignStatus = "";
							break;
						}
					}
				}
				
				if(dfpStatus.equals(OrderStatus.PENDING_APPROVAL) && lineItemStatusList.size()==1){
					if(lineItemStatusList.get(0).equals(ComputedStatus.READY.value())){
						campaignStatus =  CampaignStatusEnum.Ready.ordinal()+"";
					}
				}else if(dfpStatus.equals(OrderStatus.PENDING_APPROVAL) && lineItemStatusList.size()>1){
					for (int i=0;i<lineItemStatusList.size();i++) {
						if(lineItemStatusList.get(i).equals(ComputedStatus.READY.value())){
							campaignStatus =  CampaignStatusEnum.Ready.ordinal()+"";
						}else{
							campaignStatus = "";
							break;
						}
					}
				}
				
				if(dfpStatus.equals(OrderStatus.APPROVED) && lineItemStatusList.size()==1){
					if(lineItemStatusList.get(0).equals(ComputedStatus.DELIVERING.value())){
						campaignStatus =  CampaignStatusEnum.Running.ordinal()+"";
					}else if(lineItemStatusList.get(0).equals(ComputedStatus.COMPLETED.value())){
						campaignStatus =  CampaignStatusEnum.Completed.ordinal()+"";
					}
				}else if(dfpStatus.equals(OrderStatus.APPROVED) && lineItemStatusList.size()>1){
					for (int i=0;i<lineItemStatusList.size();i++) {
						if(lineItemStatusList.get(i).equals(ComputedStatus.DELIVERING.value())){
							campaignStatus =  CampaignStatusEnum.Running.ordinal()+"";
						}else if(lineItemStatusList.get(i).equals(ComputedStatus.COMPLETED.value())){
							campaignStatus =  CampaignStatusEnum.Completed.ordinal()+"";
						}else{
							campaignStatus =  CampaignStatusEnum.Running.ordinal()+"";
						}
					}
				}
				
				if(dfpStatus.equals(OrderStatus.PAUSED) && lineItemStatusList.size()==1){
					if(lineItemStatusList.get(0).equals(ComputedStatus.PAUSED.value())){
						campaignStatus =  CampaignStatusEnum.Paused.ordinal()+"";
					}
				}else if(dfpStatus.equals(OrderStatus.PAUSED) && lineItemStatusList.size()>1){
					for (int i=0;i<lineItemStatusList.size();i++) {
						if(lineItemStatusList.get(i).equals(ComputedStatus.PAUSED.value())){
							campaignStatus =  CampaignStatusEnum.Paused.ordinal()+"";
						}else{
							campaignStatus = "";
							break;
						}
					}
				}
				
				if(dfpStatus.equals(OrderStatus.CANCELED) && lineItemStatusList.size()==1){
					if(lineItemStatusList.get(0).equals(ComputedStatus.CANCELED.value())){
						campaignStatus =  CampaignStatusEnum.Canceled.ordinal()+"";
					}
				}else if(dfpStatus.equals(OrderStatus.CANCELED) && lineItemStatusList.size()>1){
					for (int i=0;i<lineItemStatusList.size();i++) {
						if(lineItemStatusList.get(i).equals(ComputedStatus.CANCELED.value())){
							campaignStatus =  CampaignStatusEnum.Canceled.ordinal()+"";
						}/*else{
							campaignStatus = "";
							break;
						}*/
					}
				}
			}
		}catch(Exception e){
			log.severe("Exception in campaignStatus in DFPReportService"+e.toString());
			e.printStackTrace();
		}
		return campaignStatus;
		
	}
	
	/*
	 * @author Shubham Goel
	 * Get LineItems for given Campaign Ids from Datastore  SmartCampaignObj
	 *      
	 * @param SmartCampaignObj campaignId 
	 * @return List<String> lineItemStatusList
	 */
	public List<String> getLineItemStatus(long campaignId){
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		Map<String, Long> lineItemStatusCountMap = new TreeMap<>();
		List<String> lineItemStatusList = new ArrayList<>();

		try {
			List<SmartCampaignPlacementObj> smartCampaignPlacementList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(campaignId);
			if(smartCampaignPlacementList != null && smartCampaignPlacementList.size() > 0) {
				for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementList) {
					if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getDfpLineItemList() != null && smartCampaignPlacementObj.getDfpLineItemList().size() > 0) {
						for (DFPLineItemDTO dfpLineItemDTO : smartCampaignPlacementObj.getDfpLineItemList()) {
							if(dfpLineItemDTO != null && dfpLineItemDTO.getLineItemId() > 0 && dfpLineItemDTO.getDfpStatus() != null && dfpLineItemDTO.getDfpStatus().trim().length() > 0) {
								String status = dfpLineItemDTO.getDfpStatus().trim();
								if(lineItemStatusCountMap.containsKey(status)) {
									long count = lineItemStatusCountMap.get(status);
									lineItemStatusCountMap.put(status, (count + 1));
								}
								else {
									lineItemStatusCountMap.put(status, 1L);
								}
							}
						}
					}
				}
	        }
			if(lineItemStatusCountMap.size() > 0) {
					for(String status : lineItemStatusCountMap.keySet()) {
					lineItemStatusList.add(status);
				}
			}
		}catch(Exception e){
			log.severe("Exception in getLineItemStatus of DFPReportService : "+e.getMessage());
		}
		return lineItemStatusList;
	}
	
	

	/*
	 * @author Naresh Pokhriyal
	 * Get LineItem for given LineItem Ids from DFP and updates LineItem status in SmartCampaignPlacementObj
	 *      
	 * @param DfpServices dfpServices, DfpSession dfpSession, String commaSeperatedLineItemIds,
	 * 				 StringBuilder summary, Map<Long,SmartCampaignPlacementObj> lineItemPlacementMap, String adServerCode
	 * @return boolean result
	 */
	public boolean updatePlacementDetailFromDFP(DfpServices dfpServices, DfpSession dfpSession, String commaSeperatedLineItemIds, StringBuilder summary, Map<Long,SmartCampaignPlacementObj> lineItemPlacementMap, String adServerCode) throws Exception {
	 	boolean result = false;
	 	String str = "LineItemIds : " + commaSeperatedLineItemIds;
		log.info(str);
		summary.append(str+"<br>");
		try {
			log.info("Initialising orderService, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			LineItemServiceInterface lineItemService = dfpServices.get(dfpSession, LineItemServiceInterface.class);
			
			String query = " id IN ("+commaSeperatedLineItemIds+") ";
			log.info("PQL query : "+query);
			Statement filterStatement = new StatementBuilder().where(query).toStatement();
			
			log.info("Executing Query, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			LineItemPage lineItemPage = lineItemService.getLineItemsByStatement(filterStatement);
			log.info("LineItemPage initialised, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
			
			if (lineItemPage.getResults() != null) {
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				str = "LineItem resultSet size : " + lineItemPage.getResults().size();
				log.info(str);
				summary.append(str+"<br>");
				
				for (LineItem lineItem : lineItemPage.getResults()) {
					if(lineItem != null && lineItem.getId() != null && lineItem.getId() > 0 && lineItem.getStatus() != null) {
						Long lineItemId = lineItem.getId();
						if(lineItemPlacementMap.containsKey(lineItemId)) {
							SmartCampaignPlacementObj placement = lineItemPlacementMap.get(lineItemId);
							if(placement != null && placement.getDfpLineItemList() != null && placement.getDfpLineItemList().size() > 0) {
								for(DFPLineItemDTO dfpLineItemDTO : placement.getDfpLineItemList()) {
									if(dfpLineItemDTO != null && dfpLineItemDTO.getLineItemId() == lineItemId) {
										dfpLineItemDTO.setDfpStatus(lineItem.getStatus().value());
										smartCampaignPlannerDAO.saveObject(placement);
										//log.info("Placement Id : "+placement.getPlacementId()+", LineItemId : "+lineItemId+", LineItem Status : "+lineItem.getStatus().value());
										break;
									}
								}
							}
						}
					}
				}
			}
			result = true;
		}
		catch (Exception e) {
			str = "Exception in updatePlacementDetailFromDFP of DFPReportService : " + e.getMessage();
			log.severe(str);
			//e.printStackTrace();
			summary.append(str+"<br>");
		}
		return result;
	}
	
	
	/*
	 * Load adUnit data for USA only
	 */
	public  String getDFPReportByAdUnitsWithCity(DfpServices dfpServices, DfpSession session,List<String> adUnitIds,
			String start,String end) throws Exception{		
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	    
	     if(start !=null && end !=null){
	    	 reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    	 String [] startArray=start.split("-");
			 String [] endArray=end.split("-"); 
		     Date startDate=new Date();
		     
		     startDate.setDay(Integer.parseInt(startArray[2]));
		     startDate.setMonth(Integer.parseInt(startArray[1]));
		     startDate.setYear(Integer.parseInt(startArray[0]));
		     
		     Date endDate=new Date();
		     endDate.setDay(Integer.parseInt(endArray[2]));
		     endDate.setMonth(Integer.parseInt(endArray[1]));
		     endDate.setYear(Integer.parseInt(endArray[0]));
		     
		     reportQuery.setStartDate(startDate);
		     reportQuery.setEndDate(endDate);	   
	     }else{
	    	 reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	     }
	    	     
	    	    
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {				  
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,				
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME
				 
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS		    		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{	    			 
	     };	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.ACTIVE_AD_UNITS,	    		 
	     };
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     query.append(" where AD_UNIT_ANCESTOR_AD_UNIT_ID in ( ");
	     
	     for(int i=0;i<adUnitIds.size();i++){
	    	 if(i==0){
	    		 query.append(adUnitIds.get(i));
	    	 }else{
	    		 query.append(",");
	    		 query.append(adUnitIds.get(i));
	    	 }
	     }
	     query.append(" ) and country_criteria_id in (2840) ");  //Load only for USA right now
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.getDimensions().addAll(Arrays.asList(dimensionArray));
	     reportQuery.getColumns().addAll(Arrays.asList(columnArray));
	     //reportQuery.getDimensionAttributes().addAll(Arrays.asList(dimAttributeArray));
	     reportQuery.getDimensionFilters().addAll(Arrays.asList(dimFilterArray));
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	

}
