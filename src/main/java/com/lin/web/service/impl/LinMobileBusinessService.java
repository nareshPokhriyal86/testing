package com.lin.web.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.jaxws.v201403.ComputedStatus;
import com.google.api.ads.dfp.jaxws.v201403.DateTime;
import com.google.api.ads.dfp.jaxws.v201403.Forecast;
import com.google.api.ads.dfp.jaxws.v201403.ForecastServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Money;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
import com.google.api.ads.dfp.jaxws.v201403.TargetPlatform;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.util.Lists;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.NumberValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.lin.persistance.dao.ILinMobileDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.LinMobileDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserByMarketObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CustomLineItemObj;
import com.lin.server.bean.ForcastedAdvertiserObj;
import com.lin.server.bean.OrderLineItemObj;
import com.lin.server.bean.PerformanceMetricsObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherInventoryRevenueObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.PublisherSummaryObj;
import com.lin.server.bean.ReallocationDataObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LeftMenuDTO;
import com.lin.web.dto.LineChartDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.PublisherReallocationHeaderDTO;
import com.lin.web.dto.PublisherReportChannelPerformanceDTO;
import com.lin.web.dto.PublisherReportComputedValuesDTO;
import com.lin.web.dto.PublisherReportHeaderDTO;
import com.lin.web.dto.PublisherReportPerformanceByPropertyDTO;
import com.lin.web.dto.PublisherTrendAnalysisHeaderDTO;
import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.ReconciliationDataDTO;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.IQueryService;
import com.lin.web.service.IUserService;
import com.lin.web.util.AdvertiserViewMostActiveComparator;
import com.lin.web.util.AdvertiserViewNonPerformerComparator;
import com.lin.web.util.AdvertiserViewPerformerComparator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EncriptionUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;

public class LinMobileBusinessService implements ILinMobileBusinessService{
	private static final Logger log = Logger.getLogger(LinMobileBusinessService.class.getName());	
	
	
	public List<LineItemDTO> getLineItems(long orderId){
		List<LineItemDTO> lineItemDTOList=null;
		return lineItemDTOList;
	}
	
	private String ChannelsInQString(List<String> allChannelName, StringBuilder memcacheKeySubstring) {

		String Channel = "";
		if (allChannelName.size() != 0) {
			int channelListSize = allChannelName.size();
			int i = 0;
			if (channelListSize == 1) {
				Channel = Channel + "channel_name = '"
						+ allChannelName.get(i) + "' ";
				memcacheKeySubstring.append(allChannelName.get(i));
			} else {
				Channel = Channel + "(";
				for (i = 0; i < channelListSize; i++) {
					Channel = Channel + "channel_name = '" + allChannelName.get(i) + "'";
					memcacheKeySubstring.append(allChannelName.get(i));
					if (i != channelListSize - 1) {
						Channel = Channel + " OR ";
					}
				}
				Channel = Channel + " ) ";

			}
		}
		log.info("Channels:"+Channel);
		return Channel;

	}
	
	@Override
	public String getPublisherBQId(String publisherName) {
		log.info("publisherName : "+publisherName);
		
		Map<String, String> publisherMap = new HashMap<>();
		String publisherIdInBQ = "";
		
		publisherMap.put(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME, LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
		publisherMap.put(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME, LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
		publisherMap.put(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME, LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID);
		publisherMap.put(LinMobileConstants.LIN_MOBILE_PUBLISHER_NAME, LinMobileConstants.LIN_MOBILE_PUBLISHER_ID);
		
		publisherIdInBQ = publisherMap.get(publisherName);
		
		log.info("publisherIdInBQ : "+publisherIdInBQ);
		return publisherIdInBQ;
	}
	
	public String getChannelsBQId(String channels) {
		log.info("channels : "+channels);
		
		Map<String, String> channelMap = new HashMap<>();
		StringBuilder channelIdString = new StringBuilder();
		
		channelMap.put("Google Ad exchange", "7");
		channelMap.put("House", "8");
		channelMap.put("LSN", "10");
		channelMap.put("Local Sales Direct", "4");
		channelMap.put("Mojiva", "3");
		channelMap.put("National Sales Direct", "5");
		channelMap.put("Nexage", "2");
		channelMap.put("Undertone", "6");
		
		if(channels != null && channels.trim().length() > 0) {
			String[] channelArray = channels.split(",");
			for(String channel : channelArray){
				channel = channel.replaceAll("'", "");
				channel = channelMap.get(channel.trim());
				if(channel.length() > 0) {
					if((channelIdString.toString()).length() > 0) {
						channelIdString.append(","+channel);
					}
					else {
						channelIdString.append(channel);
					}
				}
			}
		}
		log.info("channelIdString : "+channelIdString);
		return channelIdString.toString();
	}
	
	public QueryDTO getQueryDTO(String publisherIdInBQ, String startDate, String endDate, String schema) {
		log.info("publisherId : "+ publisherIdInBQ+", startDate : "+ startDate+", endDate : "+ endDate+", schema : "+ schema);
		QueryDTO queryDTO = null;
		
		if(publisherIdInBQ.length() > 0) {
			IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
			if(startDate.contains("00:00:00")) {
				startDate = startDate.replaceAll("00:00:00", "");
			}
			if(endDate.contains("00:00:00")) {
				endDate = endDate.replaceAll("00:00:00", "");
			}
			startDate = startDate.trim();
			endDate = endDate.trim();
			queryDTO = queryService.createQueryFromClause(publisherIdInBQ, startDate, endDate, schema);
		}
		if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
			log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
		}
		else {
			log.info("queryDTO : null or queryDTO.getQueryData() is empty");
			log.info("putting dummy data");
			String projectId=null;
			String serviceAccountEmail=null;
			String servicePrivateKey=null;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			StringBuffer queryData=new StringBuffer();
			
			if(schema.equals(LinMobileConstants.BQ_CORE_PERFORMANCE)) {
				queryData.append("LIN_QA.CorePerformance_2014_01,LIN_QA.CorePerformance_2014_02,LIN_QA.CorePerformance_2014_03");
			}
			else if(schema.equals(LinMobileConstants.BQ_DFP_TARGET)) {
				queryData.append("LIN_QA.DFPTarget_2014_01,LIN_QA.DFPTarget_2014_02,LIN_QA.DFPTarget_2014_03");
			}
			else if(schema.equals(LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION)) {
				queryData.append("LIN_QA.PerformanceByLocation_2014_01,LIN_QA.PerformanceByLocation_2014_02,LIN_QA.PerformanceByLocation_2014_03");
			}
			else if(schema.equals(LinMobileConstants.BQ_SELL_THROUGH)) {
				queryData.append("LIN.Sell_Through");
			}
			
			switch(publisherIdInBQ) {		
			  case "1":
				  projectId=LinMobileConstants.LIN_MEDIA_GOOGLE_API_PROJECT_ID;
				  serviceAccountEmail=LinMobileConstants.LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				  servicePrivateKey=LinMobileConstants.LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				  break;
			  case "2":
				  projectId=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_PROJECT_ID;
				  serviceAccountEmail=LinMobileConstants.LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				  servicePrivateKey=LinMobileConstants.LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				  break;
			  case "5":
				  projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
				  serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
				  servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				  break;
			  default:
				  log.warning("There is no project configured for this publisherId :"+publisherIdInBQ);
				  return queryDTO;			  
			}	
			
			queryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId,dataSetId, queryData.toString());
			log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
			//
		}
		return queryDTO;
	}
	
	public String getChannelAndDataSourceQuery(List<String> selectedChannelName, StringBuilder memcacheKeySubstring, boolean allChannels) throws Exception {
		log.info("Inside getChannelAndDataSourceQuery in LinMobileDAO");
		String QUERY = " and (( data_source = '' and channel_name = ''))";
		int length  = 0;
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(MemcacheUtil.getAllCompanyList());
		if(allChannels && demandPartnersList != null) {
			length  = demandPartnersList.size();
		}
		else if(!allChannels && selectedChannelName != null && selectedChannelName.size() > 0) {
			length  = selectedChannelName.size();
		}
		
		QUERY = "";
		for (int i=0; i<length; i++) {
			CompanyObj demandPartnerCompany = null;
			if(allChannels && demandPartnersList != null) {
				demandPartnerCompany = demandPartnersList.get(i);
			}
			else if(!allChannels && selectedChannelName != null && selectedChannelName.size() > 0) {
				demandPartnerCompany = userDetailsDAO.getCompanyByName(selectedChannelName.get(i).trim(), demandPartnersList);
			}
			if(demandPartnerCompany!=null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null && !demandPartnerCompany.getCompanyName().trim().equalsIgnoreCase("") && demandPartnerCompany.getDataSource() != null && !demandPartnerCompany.getDataSource().trim().equalsIgnoreCase("")){
				memcacheKeySubstring.append(demandPartnerCompany.getCompanyName().trim());
				if(i==0 && length == 1) {
		    		 QUERY = QUERY+" and (( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
		    	 }
				else if(i==0) {
		    		 QUERY = QUERY+" and (( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
		    	 }
	    		 else if(i == length-1) {
		    		 QUERY = QUERY+" or ( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
		    	 }
	    		 else {
		    		 QUERY = QUERY+" or ( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
		    	 } 
	    	 }
		}
		return QUERY;
	}
	
	
	/*public String getAllChannelAndDataSourceQuery(StringBuilder memcacheKeySubstring) throws Exception {
		log.info("Inside getAllChannelAndDataSourceQuery in LinMobileDAO");
		String QUERY = "";
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(MemcacheUtil.getAllCompanyList());
		if(demandPartnersList != null && demandPartnersList.size() > 0) {
			int length  = demandPartnersList.size();
			for (int i=0; i<length; i++) {
				CompanyObj demandPartnerCompany = demandPartnersList.get(i);
				if(demandPartnerCompany!=null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null && !demandPartnerCompany.getCompanyName().trim().equalsIgnoreCase("") && demandPartnerCompany.getDataSource() != null && !demandPartnerCompany.getDataSource().trim().equalsIgnoreCase("")){
					memcacheKeySubstring.append(demandPartnerCompany.getCompanyName().trim());
					if(i==0 && length == 1) {
			    		 QUERY = QUERY+" and (( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
			    	 }
					else if(i==0) {
			    		 QUERY = QUERY+" and (( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
			    	 }
		    		 else if(i == length-1) {
			    		 QUERY = QUERY+" or ( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
			    	 }
		    		 else {
			    		 QUERY = QUERY+" or ( data_source = '"+demandPartnerCompany.getDataSource().trim()+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
			    	 } 
		    	 }
			}
		}
		return QUERY;
	}*/
	/*public String getChannelWithDataSourceDFPQuery(StringBuilder memcacheKeySubstring) throws Exception {
		log.info("Inside getAllChannelAndDataSourceQuery in LinMobileDAO");
		String QUERY = "";
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(MemcacheUtil.getAllCompanyList());
		if(demandPartnersList != null && demandPartnersList.size() > 0) {
			int length  = demandPartnersList.size();
			for (int i=0; i<length; i++) {
				CompanyObj demandPartnerCompany = demandPartnersList.get(i);
				if(demandPartnerCompany!=null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null && !demandPartnerCompany.getCompanyName().trim().equalsIgnoreCase("") && demandPartnerCompany.getDataSource() != null && !demandPartnerCompany.getDataSource().trim().equalsIgnoreCase("")){
					memcacheKeySubstring.append(demandPartnerCompany.getCompanyName().trim());
					if(i==0 && length == 1) {
			    		 QUERY = QUERY+" and (( data_source = '"+LinMobileConstants.DEFAULT_DATASOURE_NAME+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
			    	 }
		    		 else if(i==0) {
			    		 QUERY = QUERY+" and (( data_source = '"+LinMobileConstants.DEFAULT_DATASOURE_NAME+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
			    	 }
		    		 else if(i == length-1) {
			    		 QUERY = QUERY+" or ( data_source = '"+LinMobileConstants.DEFAULT_DATASOURE_NAME+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"'))";
			    	 }
		    		 else {
			    		 QUERY = QUERY+" or ( data_source = '"+LinMobileConstants.DEFAULT_DATASOURE_NAME+"' and channel_name = '"+demandPartnerCompany.getCompanyName().trim()+"')";
			    	 } 
		    	 }
			}
		}
		return QUERY;
	}*/


	public String getSelectedDFPPropertiesQuery(long publisherId, long userId, StringBuilder memcacheKeySubstring) throws Exception {
		/*log.info("Inside getSelectedDFPPropertiesQuery in LinMobileDAO");
		StringBuilder QUERY = new StringBuilder();
		QUERY.append(" and ( site_name = '' )");
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<PropertyEntityObj> propertyObjList = userDetailsDAO.getSelectedPropertiesByUserIdAndPublisherId(userId, (Long.valueOf(publisherId)).toString().trim());
		if(propertyObjList != null && propertyObjList.size() > 0) {
			int length  = propertyObjList.size();
			if(length > 0) {
				QUERY = new StringBuilder();
				for (int i=0; i<length; i++) {
					PropertyEntityObj propertyObj = propertyObjList.get(i);
					if(propertyObj != null && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equalsIgnoreCase("")) {
						 memcacheKeySubstring.append(propertyObj.getDFPPropertyName().trim());
			    		 if(i==0) {
			    			 QUERY.append(" and ( site_name = '"+propertyObj.getDFPPropertyName().trim()+"' ");
				    	 }
			    		 else {
			    			 QUERY.append(" or  site_name = '"+propertyObj.getDFPPropertyName().trim()+"' ");
				    	 } 
			    	 }
				}
				QUERY.append(" ) ");
			}
		}
		return QUERY.toString();*/
		return "";
	}
	
	public List<AdvertiserReportObj> loadPerformingLineItems(String lowerDate,String upperDate){
		List<AdvertiserReportObj> advertiserReportList=null;
		List<AdvertiserReportObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			advertiserReportList=linDAO.loadPerformingLineItemsForAdvertiser(5,lowerDate,upperDate);
			if(advertiserReportList !=null){
				log.info("Found objects from datastore :"+advertiserReportList.size()+" , going to sort by CTR using comperator.");
				AdvertiserViewPerformerComparator performerComperator= new AdvertiserViewPerformerComparator();
				Collections.sort(advertiserReportList, performerComperator);
				if(advertiserReportList.size()>5){
					subList=advertiserReportList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from advertiserReportList.");
					return subList;
				}else{
					
					return advertiserReportList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return advertiserReportList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<AdvertiserReportObj> loadNonPerformingLineItems(String lowerDate,String upperDate){
		List<AdvertiserReportObj> advertiserReportList=null;
		List<AdvertiserReportObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			advertiserReportList=linDAO.loadNonPerformingLineItemsForAdvertiser(5,lowerDate,upperDate);
			if(advertiserReportList !=null){
				log.info("Found objects from datastore :"+advertiserReportList.size()+" , going to sort by delivery indicator using comparator.");
				
				double upperCap=100;
				Iterator itr=advertiserReportList.iterator();
				AdvertiserReportObj obj=null;
				
				while(itr.hasNext()){
					obj=(AdvertiserReportObj) itr.next();
					//log.info("obj.getDeliveryIndicator():"+obj.getDeliveryIndicator());
					if(obj.getDeliveryIndicator() >= upperCap){
						//log.info("Removing lineitem with delivery indicator ::"+obj.getDeliveryIndicator());
						itr.remove();
					}
				}				
				
				AdvertiserViewNonPerformerComparator nonPerformerComperator= new AdvertiserViewNonPerformerComparator();
				Collections.sort(advertiserReportList, nonPerformerComperator);
				
				if(advertiserReportList !=null && advertiserReportList.size()>5){
					subList=advertiserReportList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from advertiserReportList.");
					return subList;
				}else{					
					return advertiserReportList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return advertiserReportList;
			}
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
	}
	
	public List<String> loadAgencies(){
		log.info("Service :loading agencies...");
		List<AgencyAdvertiserObj> agencyAdvertiserList=null;
		List<String> agencyList=new ArrayList<String>();		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {			
			agencyAdvertiserList=linDAO.loadAllAgencies();			
			for(AgencyAdvertiserObj obj:agencyAdvertiserList){
				String agencyName=obj.getAgencyName();
				if(agencyName!=null && (!agencyName.equals("0")) && !agencyList.contains(agencyName)){
					agencyList.add(agencyName);
				}else{
					//log.info("already added.."+agencyList);
				}
			}
		} catch (DataServiceException e) {
			log.severe("Falied to load all agencies: DataServiceException:"+e.getMessage());
			
		}
		return agencyList;
	}
	
	public List<AgencyAdvertiserObj> loadAdvertisers(String agencyName){       
        List<AgencyAdvertiserObj> agencyAdvertiserList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			agencyAdvertiserList=linDAO.loadAllAdvertisers(agencyName);			
		} catch (DataServiceException e) {
			log.severe("Falied to load all advertisers: DataServiceException:"+e.getMessage());
			
		}
		return agencyAdvertiserList;
	}
	
	
	public List<CustomLineItemObj> getMostActiveLineItems(String lowerDate,String upperDate){
		List<CustomLineItemObj> resultList=null;
		List<CustomLineItemObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadMostActiveLineItem(5,lowerDate,upperDate);
			if(resultList !=null){
				log.info("Found objects from datastore :"+resultList.size()+" , going to sort by delivery indicator using comperator.");
				AdvertiserViewMostActiveComparator mostActiveComparator= new AdvertiserViewMostActiveComparator();
				Collections.sort(resultList, mostActiveComparator);
				if(resultList.size()>5){
					subList=resultList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from mostActiveList.");
					return subList;
				}else{
					
					return resultList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return resultList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<CustomLineItemObj> getTopGainersLineItems(String lowerDate,String upperDate){
		List<CustomLineItemObj> resultList=null;
		List<CustomLineItemObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadMostActiveLineItem(5,lowerDate,upperDate);
			if(resultList !=null){
				log.info("Found objects from datastore :"+resultList.size()+" , going to sort by delivery indicator using comperator.");
				AdvertiserViewMostActiveComparator mostActiveComparator= new AdvertiserViewMostActiveComparator();
				Collections.sort(resultList, mostActiveComparator);
				if(resultList.size()>5){
					subList=resultList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from mostActiveList.");
					return subList;
				}else{
					
					return resultList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return resultList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
		
	}
	
	public List<CustomLineItemObj> getTopLosersLineItems(String lowerDate,String upperDate){		
		List<CustomLineItemObj> resultList=null;
		List<CustomLineItemObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadMostActiveLineItem(5,lowerDate,upperDate);
			if(resultList !=null){
				log.info("Found objects from datastore :"+resultList.size()+" , going to sort by delivery indicator using comperator.");
				AdvertiserViewMostActiveComparator mostActiveComparator= new AdvertiserViewMostActiveComparator();
				Collections.sort(resultList, mostActiveComparator);
				if(resultList.size()>5){
					subList=resultList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from mostActiveList.");
					return subList;
				}else{
					
					return resultList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return resultList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public void insertPublisherViewDTO(List<PublisherViewDTO> publisherViewList)
	{
		LinMobileDAO linDao = new LinMobileDAO();
		for(PublisherViewDTO publisherView  : publisherViewList)
		{
			try {
				linDao.saveObject(publisherView);
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				
			}
		}
		
	}
	
	public List<PublisherViewDTO>  getPublisherViewDTO()
	{
		LinMobileDAO linDao = new LinMobileDAO();
		try {
			return linDao.getPublisherViewDTO();
		} catch (DataServiceException e) {			
			
			log.info(":exception: "+e.getMessage());
			return null;
		}
	}
	
	
	public List<PublisherViewDTO>  getPublisherViewDTO(int pageNo)
	{
		LinMobileDAO linDao = new LinMobileDAO();
		try {
			return linDao.getPublisherView(pageNo);
		} catch (DataServiceException e) {
			log.info(":exception: "+e.getMessage());
			
			return null;
		}
	}
	
	public void insertLeftMenuDTO(List<LeftMenuDTO> leftMenuItemList)
	{
		LinMobileDAO linDao = new LinMobileDAO();
		for(LeftMenuDTO leftMenuDTO  : leftMenuItemList)
		{
			try {
				linDao.saveObject(leftMenuDTO);
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				
			}
		}
	}
	
	
	public List<LeftMenuDTO> getLeftMenuList()
	{
		LinMobileDAO linDao = new LinMobileDAO();
		try {
			return linDao.getLeftMenuList();
		} catch (DataServiceException e) {
			log.info(":exception: "+e.getMessage());
			
			return null;
		}
	}
	
	public List<PerformanceMetricsObj> getAdvertiserPerformanceMetrics(int counter,String lowerDate,String upperDate){		
		List<PerformanceMetricsObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadAdvertiserPerformanceMetrics(counter,lowerDate,upperDate);
			return resultList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<PerformanceMetricsObj> getAdvertiserPerformanceMetrics(String lowerDate,String upperDate){		
		List<PerformanceMetricsObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadAdvertiserPerformanceMetrics(lowerDate,upperDate);
			return resultList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	
	public void loadAdvertisersByLocationData(String lowerDate,String upperDate,StringBuilder sbStr ){
	    try{
			List<AdvertiserByLocationObj>  advertiserByLocationObjList =null;
			LinMobileDAO linDao = new LinMobileDAO();
			
			advertiserByLocationObjList = linDao.loadAdvertisersByLocationData(lowerDate, upperDate);
			
			sbStr.append("[['State',   'Impression', 'CTR(%)']");
			for (AdvertiserByLocationObj object : advertiserByLocationObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState()+"',"+object.getImpression()+","+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");
	    }catch(Exception e){
	    	
	    	log.severe("Exception :"+e.getMessage());
	    }	
	}
		
	public void loadAdvertisersByMarketData(String lowerDate,String upperDate,StringBuilder sbStr){
		try{
			List<AdvertiserByMarketObj>  advertiserBymarketObjList =null;
			LinMobileDAO linDao = new LinMobileDAO();
			
			advertiserBymarketObjList = linDao.loadAdvertisersBymarketData(lowerDate, upperDate);
			
			sbStr.append("[['State',   'lin-property', 'CTR(%)']");
			for (AdvertiserByMarketObj object : advertiserBymarketObjList) {
				sbStr.append(",[");
				sbStr.append("'"+object.getState()+"','"+object.getLinProperty()+"',"+object.getCtrPercent());
				sbStr.append("]");
			}
			sbStr.append("]");
	    }catch(Exception e){
	    	
	    	log.severe("Exception :"+e.getMessage());
	    }	
	}
	
	
	/*
	 * Load advertiser details by advertiserId
	 * @see com.lin.web.service.ILinMobileBusinessService#loadAdvertiserDetails(long)
	 */
	public List<AgencyAdvertiserObj> loadAdvertiserDetails(long advertiserId){
		  List<AgencyAdvertiserObj> agencyAdvertiserList=null;		
			ILinMobileDAO linDAO=new LinMobileDAO();
			try {
				agencyAdvertiserList=linDAO.loadAdvertiserById(advertiserId);			
			} catch (DataServiceException e) {
				log.severe("Falied to load all advertisers: DataServiceException:"+e.getMessage());
				
			}
			return agencyAdvertiserList;
	}
	
	
	

	
/*	public List<PublisherChannelObj> getChannelPerformanceList(String lowerDate, String upperDate,  String compareStartDate,  String compareEndDate, String allChannelName, String publisher){
		List<PublisherChannelObj> resultList=null;
		List<PublisherChannelObj> publisherList=new ArrayList<PublisherChannelObj>();	
		List<String> channelNameList=new ArrayList<String>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		
		List<String> channelArrList = new ArrayList<String>();
		String[] str = allChannelName.split(",");
		if(str!=null){
			for (String channel : str) {
				channelArrList.add(channel.trim());
			}
		}
		
		
		
		try {
			resultList=linDAO.loadChannelPerformanceList(lowerDate,upperDate,compareStartDate,compareEndDate, channelArrList,publisher );
			if(resultList != null && resultList.size() > 0) {
				for(PublisherChannelObj obj:resultList){
					if(!channelNameList.contains(obj.getChannelName())){
						channelNameList.add(obj.getChannelName());
						publisherList.add(obj);
					}
				}
			}
			return publisherList;
		} catch (Exception e) {
			log.severe("Exception in getChannelPerformanceList in LinMobileBusinessService :"+e.getMessage());
			
			return publisherList;
		}
	}*/
	
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(int limit,String lowerDate,String upperDate)
	{
		List<PublisherPropertiesObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadPerformanceByPropertyList(limit,lowerDate,upperDate);
			return resultList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(String lowerDate,String upperDate){
		List<PublisherPropertiesObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadPerformanceByPropertyList(lowerDate,upperDate);
			return resultList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<PublisherPropertiesObj> getPerformanceByPropertyList(String lowerDate,String upperDate, String compareStartDate, String compareEndDate, String channel, String publisher){
		List<PublisherPropertiesObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			StringBuilder memcacheKeysubstring = new StringBuilder();
			String channelAndDataSourceQuery = getChannelAndDataSourceQuery(userService.CommaSeperatedStringToList(channel), memcacheKeysubstring, false);
			resultList=linDAO.loadPerformanceByPropertyList(lowerDate,upperDate,compareStartDate,compareEndDate, channel, publisher, channelAndDataSourceQuery);
			return resultList;
		} catch (Exception e) {
			log.severe("Exception :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<SellThroughDataObj> getSellThroughDataList(String lowerDate,String upperDate, String publisherName, long publisherId, long userId)
	{
		List<SellThroughDataObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			String publisherBQId = getPublisherBQId(publisherName);
			StringBuilder DFP_Properties_MemcacheKeyPart = new StringBuilder();
			String selectedDFPPropertiesQuery = getSelectedDFPPropertiesQuery(publisherId, userId, DFP_Properties_MemcacheKeyPart);
			resultList = MemcacheUtil.getSellThroughData(lowerDate, upperDate, publisherBQId, DFP_Properties_MemcacheKeyPart.toString());
			if(resultList == null || resultList.size() <= 0) {
				QueryDTO queryDTO = getQueryDTO(publisherBQId, lowerDate, upperDate, LinMobileConstants.BQ_SELL_THROUGH);
				log.info("Sell through data not found in memcache, get from bigquery..");
				resultList=linDAO.loadSellThroughDataList(lowerDate,upperDate, selectedDFPPropertiesQuery, queryDTO);
				if(resultList != null && resultList.size()>0) {
					MemcacheUtil.setSellThroughData(resultList, lowerDate, upperDate, publisherBQId, DFP_Properties_MemcacheKeyPart.toString());
				}
			}
			return resultList;
		} catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
			
			return null;
		}
	}
	
	/*
	 * Get LineItem by id
	 * @Param - String lowerDate,String upperDate,long lineItemId
	 * @Return - PopUpDTO popUpObj
	 */
	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,long lineItemId){
		List<AdvertiserReportObj> resultList=null;
		
		PopUpDTO popUpObj=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			popUpObj=new PopUpDTO();
			resultList=linDAO.loadLineItem(lowerDate,upperDate,lineItemId);
			popUpObj.setId(String.valueOf(lineItemId));
			if(resultList !=null && resultList.size()>0){
				log.info("Got lineItems:"+resultList.size());
				AdvertiserReportObj obj=resultList.get(0);
				popUpObj.setBookedImpression(String.valueOf(obj.getGoalQuantity()));
				popUpObj.setImpressionDeliveredLifeTime(String.valueOf(obj.getLineItemLifeItemImpressions()));
				popUpObj.setImpressionDeliveredInSelectedTime(String.valueOf(obj.getAdServerImpressions()));
				popUpObj.setClicksInSelectedTime(String.valueOf(obj.getAdServerClicks()));
				popUpObj.setClicksLifeTime(String.valueOf(obj.getLineItemLifeTimeClicks()));
				popUpObj.setCtrLifeTime(String.valueOf(obj.getAdServerCTR()));
				popUpObj.setCtrInSelectedTime(String.valueOf(obj.getAdServerCTR()));
				popUpObj.seteCPM(String.valueOf(obj.getAdServerECPM()));
				popUpObj.setTitle(obj.getLineItemName());
        
				StringBuffer strBuffer=new StringBuffer();
				
				strBuffer.append("[['Days',   'Impression']");
				for (AdvertiserReportObj object:resultList) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(object.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+object.getAdServerImpressions());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				log.info("strBuffer.toString():"+strBuffer.toString());
				popUpObj.setChartData(strBuffer.toString());
		
			}else{
				log.info("No lineitem found for lineItemId:"+lineItemId+" and time: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			}
			
		} catch (DataServiceException e) {
			log.severe("Exception in loading lineItem with id:"+lineItemId+" : "+e.getMessage());
			
		}
		return popUpObj;
	}
	
	public List<String> getPublishers(){
		List<PublisherChannelObj> resultList=null;
		List<String> publisherList=new ArrayList<String>();
				
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadPublisherDataList();
			for(PublisherChannelObj obj:resultList){
				if(!publisherList.contains(obj.getPublisherName())){
					publisherList.add(obj.getPublisherName());
				}
			}
			return publisherList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<PublisherChannelObj> getPublisherDataList(String publisherName){
		List<PublisherChannelObj> resultList=null;
		List<String> publisherList=new ArrayList<String>();
		List<PublisherChannelObj> publisherObjList=new ArrayList<PublisherChannelObj>();
				
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadPublisherDataList();
			for(PublisherChannelObj obj:resultList){
				if( (!publisherList.contains(obj.getPublisherName())) && (publisherName.equalsIgnoreCase(obj.getPublisherName())) ){
					publisherList.add(obj.getPublisherName());
					publisherObjList.add(obj);
					break;
				}
			}
			return publisherObjList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	
	public List<PublisherChannelObj> loadChannelsByPublisher(String publisherName){
		List<PublisherChannelObj> resultList=null;
		List<PublisherChannelObj> channelObjList=new ArrayList<PublisherChannelObj>();
		List<String> channelList=new ArrayList<String>();
				
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadChannels(publisherName);
			for(PublisherChannelObj obj:resultList){
				if(!channelList.contains(obj.getChannelName())){
					channelList.add(obj.getChannelName());
					channelObjList.add(obj);
				}
			}
			return channelObjList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	public List<PublisherChannelObj> loadChannelsByName(String channelName){
		List<PublisherChannelObj> resultList=null;
		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			resultList=linDAO.loadChannelsByName(channelName);			
			return resultList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}
	}
	
	/*
	 * Get LineItem by id
	 * @Param - String lowerDate,String upperDate,String lineItemName
	 * @Return - PopUpDTO popUpObj
	 */
	public PopUpDTO getLineItemForPopUP(String lowerDate,String upperDate,String lineItemName){
		List<CustomLineItemObj> resultList=null;
		
		PopUpDTO popUpObj=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			popUpObj=new PopUpDTO();
			resultList=linDAO.loadLineItem(lowerDate,upperDate,lineItemName);
			
			if(resultList !=null && resultList.size()>0){
				log.info("Got lineItems:"+resultList.size());
				CustomLineItemObj obj=resultList.get(0);
				String bookedImpressions=(obj.getDeliveredImpressions()*2)+"";
				popUpObj.setBookedImpression(bookedImpressions);
				popUpObj.setImpressionDeliveredLifeTime(String.valueOf(obj.getDeliveredImpressions()*3/2));
				popUpObj.setImpressionDeliveredInSelectedTime(String.valueOf(obj.getDeliveredImpressions()));
				String clicks=(Math.round(obj.getDeliveredImpressions()/5))+"";
				String clicksLifeTime=(Math.round((obj.getDeliveredImpressions()/5)))+"";
				popUpObj.setClicksInSelectedTime(String.valueOf(clicks));
				popUpObj.setClicksLifeTime(clicksLifeTime);
				popUpObj.setCtrLifeTime(String.valueOf(obj.getCTR()*2));
				popUpObj.setCtrInSelectedTime(String.valueOf(obj.getCTR()));
				String cpm=String.valueOf(obj.getCTR()*20);
				if(cpm.indexOf("-")>=0){
					cpm=cpm.replace("-", "");	
				}
				popUpObj.seteCPM(cpm);
				popUpObj.setTitle(obj.getLineItemName());	
				String revenueDelivered=(Math.round(obj.getDeliveryIndicator()*1500)/100)+""; //dummy data
				String revenueRemaining=(Math.round(obj.getDeliveryIndicator()*1000)/100)+""; //dummy data
				String revenueDeliveredLifeTime=(Math.round(obj.getDeliveryIndicator()*2000)/100)+""; //dummy data
				String revenueRemainingLifeTime=(Math.round((obj.getDeliveryIndicator()*1500)/100))+""; //dummy data
				popUpObj.setRevenueDeliveredInSelectedTime(revenueDelivered);
				popUpObj.setRevenueDeliveredLifeTime(revenueDeliveredLifeTime);
				popUpObj.setRevenueRemainingInSelectedTime(revenueRemaining);
				popUpObj.setRevenueRemainingLifeTime(revenueRemainingLifeTime);				
				
                StringBuffer strBuffer=new StringBuffer();
				
				strBuffer.append("[['Days',   'Impression']");
				for (CustomLineItemObj object:resultList) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(object.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+object.getDeliveredImpressions());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				popUpObj.setChartData(strBuffer.toString());
				log.info("strBuffer.toString():"+strBuffer.toString());
				
			}else{
				log.info("No lineitem found for lineItemName:"+lineItemName+" and time: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			}
			
		} catch (DataServiceException e) {
			log.severe("Exception in loading lineItem with lineItemName:"+lineItemName+" : "+e.getMessage());
			
		}
		return popUpObj;
	}

	/*
	 * getPerformanceMetricLineItemForPopUP
	 * @Param - String lowerDate,String upperDate,String lineItemName
	 * @Return - PopUpDTO popUpObj
	 */
	public PopUpDTO getPerformanceMetricLineItemForPopUP(String lowerDate,String upperDate,String lineItemName){
		List<PerformanceMetricsObj> resultList=null;
		
		PopUpDTO popUpObj=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			popUpObj=new PopUpDTO();
			resultList=linDAO.loadLineItemForPerformanceMetrics(lowerDate,upperDate,lineItemName);
			
			if(resultList!=null && resultList.size()>0){
				log.info("Got lineItems:"+resultList.size());
				PerformanceMetricsObj obj=resultList.get(0);
				String bookedImpressions=(obj.getImpressionsBooked())+"";
				popUpObj.setBookedImpression(bookedImpressions);
				popUpObj.setImpressionDeliveredLifeTime(String.valueOf(obj.getImpressionsDelivered()*3/2));
				popUpObj.setImpressionDeliveredInSelectedTime(String.valueOf(obj.getImpressionsDelivered()));
				String clicks=(Math.round(obj.getClicks()/5))+"";
				String clicksLifeTime=(obj.getClicks())+"";
				popUpObj.setClicksInSelectedTime(String.valueOf(clicks));
				popUpObj.setClicksLifeTime(clicksLifeTime);
				popUpObj.setCtrLifeTime(String.valueOf(obj.getCTR()));
				popUpObj.setCtrInSelectedTime(String.valueOf(obj.getCTR()));
				String cpm=String.valueOf(Math.round(obj.getCTR()*2000)/100);
				if(cpm.indexOf("-")>=0){
					cpm=cpm.replace("-", "");	
				}
				popUpObj.seteCPM(cpm);
				popUpObj.setTitle(obj.getLineItemName());	
				String revenueDelivered=(Math.round(obj.getRevenueRecoByDay()*100)/100)+""; //dummy data
				String revenueRemaining=(Math.round(obj.getRevenueLeftByDay()*100)/100)+""; //dummy data
				String revenueDeliveredLifeTime=(Math.round(obj.getRevenueRecoByDay()*200)/100)+""; //dummy data
				String revenueRemainingLifeTime=(Math.round((obj.getRevenueLeftByDay()*150)/100))+""; //dummy data
				popUpObj.setRevenueDeliveredInSelectedTime(revenueDelivered);
				popUpObj.setRevenueDeliveredLifeTime(revenueDeliveredLifeTime);
				popUpObj.setRevenueRemainingInSelectedTime(revenueRemaining);
				popUpObj.setRevenueRemainingLifeTime(revenueRemainingLifeTime);				
				
				StringBuffer strBuffer=new StringBuffer();
				
				strBuffer.append("[['Days',   'Impression']");
				for (PerformanceMetricsObj metricsObj:resultList) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(metricsObj.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+metricsObj.getImpressionsDelivered());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				popUpObj.setChartData(strBuffer.toString());
				log.info("strBuffer.toString():"+strBuffer.toString());
				
				
			}else{
				log.info("No lineitem found for lineItemName:"+lineItemName+" and time: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			}
			
			
		} catch (DataServiceException e) {
			log.severe("Exception in loading lineItem with lineItemName:"+lineItemName+" : "+e.getMessage());
			
		}
		return popUpObj;
	}
	
	public List<String> loadOrders(){
		List<OrderLineItemObj> orderNameList=null;
		List<String> orderList=new ArrayList<String>();		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			orderNameList=linDAO.loadAllOrders();
			for(OrderLineItemObj obj:orderNameList){
				String orderName=obj.getOrderName();
				if(orderName!=null && (!orderName.equals("0")) && !orderList.contains(orderName)){
					orderList.add(orderName);
					log.info("order list....."+orderList.size());
				}
			}
		} catch (DataServiceException e) {
			log.severe("Falied to load all agencies: DataServiceException:"+e.getMessage());
			
		}
		return orderList;
	}
	
	
	
	public List<OrderLineItemObj> loadLineItemName(String orderName){       
        List<OrderLineItemObj> lineItemList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			lineItemList=linDAO.loadAllLineItems(orderName);			
		} catch (DataServiceException e) {
			log.severe("Falied to load all advertisers: DataServiceException:"+e.getMessage());
			
		}
		return lineItemList;
	}
	
	public List<OrderLineItemObj> loadLineItemName(long orderId){
		 List<OrderLineItemObj> lineItemList=null;		
			ILinMobileDAO linDAO=new LinMobileDAO();
			try {
				lineItemList=linDAO.loadAllLineItems(orderId);			
			} catch (DataServiceException e) {
				log.severe("Falied to load all advertisers: DataServiceException:"+e.getMessage());
				
			}
			return lineItemList;
	}
	
	public List<OrderLineItemObj> loadLineItem(long lineItemId){
		 List<OrderLineItemObj> lineItemList=null;		
			ILinMobileDAO linDAO=new LinMobileDAO();
			try {
				lineItemList=linDAO.loadLineItem(lineItemId);			
			} catch (DataServiceException e) {
				log.severe("Falied to load all advertisers: DataServiceException:"+e.getMessage());
				
			}
			return lineItemList;
	}
	
	public List<ReallocationDataObj> loadReallocationItems(String lowerDate,String upperDate){
		List<ReallocationDataObj> reallocationReportList=null;
		List<ReallocationDataObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			reallocationReportList=linDAO.loadReallocationItemsForAdvertiser(5,lowerDate,upperDate);
			if(reallocationReportList !=null){
				log.info("Found objects from datastore :"+reallocationReportList.size()+" , going to sort by CTR using comperator.");
				/*AdvertiserViewPerformerComparator performerComperator= new AdvertiserViewPerformerComparator();
				Collections.sort(reallocationReportList, performerComperator);*/
				if(reallocationReportList.size()>5){
					subList=reallocationReportList.subList(0, 5);
					log.info("fetched sorted subList with 5 objects from advertiserReportList.");
					return subList;
				}else{
					
					return reallocationReportList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return reallocationReportList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<ActualAdvertiserObj> loadActualAdvertiserData(String lowerDate,String upperDate){
		List<ActualAdvertiserObj> actualAdvertiserList=null;
		List<ActualAdvertiserObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			actualAdvertiserList=linDAO.loadActualDataForAdvertiser(5,lowerDate,upperDate);
			if(actualAdvertiserList !=null){
				log.info("Found objects from datastore :"+actualAdvertiserList.size()+" , going to sort by CTR using comperator.");
				/*AdvertiserViewPerformerComparator performerComperator= new AdvertiserViewPerformerComparator();
				Collections.sort(reallocationReportList, performerComperator);*/
				if(actualAdvertiserList.size()>15){
					subList=actualAdvertiserList.subList(0, 15);
					log.info("fetched sorted subList with 5 objects from actualAdvertiserList.");
					return subList;
				}else{
					
					return actualAdvertiserList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return actualAdvertiserList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<ForcastedAdvertiserObj> loadForcastAdvertiserData(String lowerDate,String upperDate){
		List<ForcastedAdvertiserObj> forcastAdvertiserList=null;
		List<ForcastedAdvertiserObj> subList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			forcastAdvertiserList=linDAO.loadForcastDataForAdvertiser(5,lowerDate,upperDate);
			if(forcastAdvertiserList !=null){
				log.info("Found objects from datastore :"+forcastAdvertiserList.size()+" , going to sort by CTR using comperator.");
				/*AdvertiserViewPerformerComparator performerComperator= new AdvertiserViewPerformerComparator();
				Collections.sort(reallocationReportList, performerComperator);*/
				if(forcastAdvertiserList.size()>15){
					subList=forcastAdvertiserList.subList(0, 15);
					log.info("fetched sorted subList with 5 objects from actualAdvertiserList.");
					return subList;
				}else{
					
					return forcastAdvertiserList;
				}
				
			}else{
				log.info("Found objects from datastore :null");
				return forcastAdvertiserList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<PublisherChannelObj> loadActualPublisherData(String lowerDate,String upperDate,String publisherName,String channelName){
		List<PublisherChannelObj> actualPublisherList=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		//String channelAndDataSourceQuery = "";
		//IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		//List<String> channelArrList = new ArrayList<String>();
		try {
			
			String replaceStr = "\\\\'";
			
			if(channelName!=null){
				//StringBuilder memcacheKeySubstring = new StringBuilder();
				//channelAndDataSourceQuery = getChannelAndDataSourceQuery(userService.CommaSeperatedStringToList(channelName), memcacheKeySubstring, false);
				channelName = channelName.replaceAll("'", replaceStr);
				channelName = channelName.replaceAll(",", "','");
			}else{
				channelName = "";
			}
			String channelId = getChannelsBQId(channelName);
			String publisherId = getPublisherBQId(publisherName);
			actualPublisherList = MemcacheUtil.getTrendsAnalysisActualDataPublisher(lowerDate, upperDate, publisherId, channelId);
			if(actualPublisherList==null || actualPublisherList.size()<=0) {
				QueryDTO queryDTO = getQueryDTO(publisherId, lowerDate, upperDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					actualPublisherList=linDAO.loadActualDataForPublisher(lowerDate,upperDate,channelId, queryDTO);
					MemcacheUtil.setTrendsAnalysisActualDataPublisher(lowerDate, upperDate, publisherId, channelId, actualPublisherList);
				}
			}

				return actualPublisherList;

		} catch (Exception e) {
			log.severe("Exception :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<PublisherChannelObj> loadReallocationPublisherData(String lowerDate,String upperDate,String publisherName){
		List<PublisherChannelObj> reallocationPublisherList=null;
		List<PublisherChannelObj> reallocationPublisherAlteredList= new ArrayList<PublisherChannelObj>();
		List<String> networkList=new ArrayList<String>();
		
		List<PublisherChannelObj> subList=null;
		PublisherChannelObj channelObj = new PublisherChannelObj();
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			reallocationPublisherList=linDAO.loadReallocationDataForPublisher(5,lowerDate,upperDate,publisherName);
			
			if(reallocationPublisherList !=null){
				Iterator<PublisherChannelObj> iterator = reallocationPublisherList.iterator();
				while(iterator.hasNext()){
					channelObj = iterator.next();
					double ecpm = channelObj.geteCPM();
					double ctr = channelObj.getCTR();
					double cpc = ecpm/(1000*ctr);
					double floorCPM = 1000 * ctr * cpc;
					channelObj.setCPC(cpc);
					channelObj.setFloorCPM(floorCPM);
					if(!networkList.contains(channelObj.getChannelName())){
						reallocationPublisherAlteredList.add(channelObj);
						networkList.add(channelObj.getChannelName());
					}
					
				  }
				log.info("Networks: "+networkList);
				log.info("Found objects from datastore :"+reallocationPublisherAlteredList.size());
				return reallocationPublisherAlteredList;
			}else{
				log.info("Found objects from datastore :null");
				return reallocationPublisherAlteredList;
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	public List<PublisherChannelObj> loadReallocationGraphPublisherData(String lowerDate,String upperDate,String publisherName){
		
		List<PublisherChannelObj> reallocationPublisherList= null;
		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			reallocationPublisherList=linDAO.loadReallocationDataForPublisher(5,lowerDate,upperDate,publisherName);
			return reallocationPublisherList;
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return null;
		}		
		
	}
	
	/*
	 * Get ChannelPerformancePopUP
	 * @Param - String lowerDate,String upperDate,String channelName
	 * @Return - PopUpDTO popUpObj
	 */
	public PopUpDTO getChannelPerformancePopUP(String lowerDate,String upperDate,String channelName, String publisher){
		
		String preUpperDate=DateUtil.getModifiedDateStringByDays(lowerDate, -1, "yyyy-MM-dd");
		int days=(int)DateUtil.getDifferneceBetweenTwoDates(lowerDate, upperDate, "yyyy-MM-dd")+1;
		String preLowerDate=DateUtil.getModifiedDateStringByDays(preUpperDate, -days, "yyyy-MM-dd");
		
		
		String monthToDateEnd=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		String[] dayArray=monthToDateEnd.split("-");
		int year=Integer.parseInt(dayArray[0]);
		int month=Integer.parseInt(dayArray[1])-1; // subtract 1 from day String as day starts from 0 to 11 in Calender class
		String monthToDateStart=DateUtil.getDateByYearMonthDays(year,month,1,"yyyy-MM-dd");;
		
		log.info("Service: (startDate:"+lowerDate+" and endDate:"+upperDate+") and (preLowerDate="+preLowerDate+", preUpperDate="+preUpperDate+") and (monthToDateStart="+monthToDateStart+", monthToDateEnd="+monthToDateEnd+")");
		
		PopUpDTO popUpObj = null;
		String chartData = null;
		try{
			popUpObj=new PopUpDTO();
			chartData=loadPerformanceChannelPopUpGraphDataFromBigQuery(lowerDate, upperDate, channelName, publisher);
			popUpObj.setChartData(chartData);
			
		}catch (Exception e) {
			log.severe("Exception in loading channel:"+channelName+" : "+e.getMessage());
			
		}
		return popUpObj;
	}
	
	public String loadPerformanceChannelPopUpGraphDataFromBigQuery(String lowerDate,String upperDate,String channelName, String publisher){
		List<PopUpDTO> resultList=null;
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		StringBuilder memcacheKeysubstring = new StringBuilder();
		String chartData = "";
		try {
			String channelId = getChannelsBQId(channelName);
			String publisherId = getPublisherBQId(publisher);
			chartData=MemcacheUtil.getPublisherChannelPopUpGraphData(lowerDate, upperDate, channelId, publisherId);
			if(chartData == null ){
				log.info("Chart data not found in memcache, going to fetch from bigquery..");
				ILinMobileDAO linDAO=new LinMobileDAO();
				try {
					QueryDTO queryDTO = getQueryDTO(publisherId, lowerDate, upperDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
					if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
						resultList=linDAO.loadChannelsPerformancePopUpGraphData(lowerDate, upperDate, channelId, publisherId, queryDTO);
						StringBuffer strBuffer=new StringBuffer();				
						strBuffer.append("[['Days',   'Impression']");
						for (PopUpDTO object:resultList) {
							strBuffer.append(",[");
							strBuffer.append("'"+DateUtil.getFormatedDate(object.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+object.getImpressionDeliveredLifeTime());
							strBuffer.append("]");
						}
						strBuffer.append("]");
						chartData=strBuffer.toString();
						log.info("Going to set this in memcache, chartData:"+chartData);
						MemcacheUtil.setPublisherChannelPopUpGraphData(chartData,lowerDate, upperDate, channelId, publisherId);
					}
				} catch (Exception e) {
					log.severe("Exception :"+e.getMessage());
					
				}			
			}else{
				log.info("Chart data found from memcache:"+chartData);
			}
		}catch (Exception e) {
			log.severe("Exception in loadPerformanceChannelPopUpGraphDataFromBigQuery : "+e.getMessage());
			
		}
		return chartData;
	}
	
	/*
	 * loadActualPublisherData by channel name, selected time interval
	 * @see com.lin.web.service.ILinMobileBusinessService#loadActualPublisherData(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<PublisherChannelObj> loadActualPublisherData(String lowerDate,String upperDate,String channelName){
        List<PublisherChannelObj> resultList=null;		
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {			
			resultList=linDAO.loadChannels(lowerDate, upperDate, channelName);
		}catch (DataServiceException e) {
			log.severe("Exception in loadActualPublisherData :"+channelName+" : "+e.getMessage());
			
		}
		return resultList;
	}
	
	/*
	 * Get getSellThroughPopUP
	 * @Param - String lowerDate,String upperDate,String channelName
	 * @Return - PopUpDTO popUpObj
	 */
	public PopUpDTO getSellThroughPopUP(String lowerDate,String upperDate,String propertyName){
		List<SellThroughDataObj> resultList=null;
		
		PopUpDTO popUpObj=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			popUpObj=new PopUpDTO();
			resultList=linDAO.loadSellThroughDataByProperty(lowerDate, upperDate, propertyName);
			
			if(resultList !=null && resultList.size()>0){
				log.info("Got property :"+resultList.size());
				SellThroughDataObj obj=resultList.get(0);				
				popUpObj.setImpressionReserved(String.valueOf((obj.getReservedImpressions())));
				popUpObj.setImpressionAvailable(String.valueOf((obj.getAvailableImpressions())) );
				popUpObj.setImpressionForcasted(String.valueOf((obj.getForecastedImpressions())));
				popUpObj.setSellThroughRate(String.valueOf((obj.getSellThroughRate())));
        
				StringBuffer strBuffer=new StringBuffer();
				
				strBuffer.append("[['Days',   'Impression']");
				for (SellThroughDataObj object:resultList) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(object.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+object.getAvailableImpressions());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				log.info("strBuffer.toString():"+strBuffer.toString());
				popUpObj.setChartData(strBuffer.toString());
		
			}else{
				log.info("No property found:"+propertyName+" and time: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			}
			
		} catch (DataServiceException e) {
			log.severe("Exception in loading property:"+propertyName+" : "+e.getMessage());
			
		}
		return popUpObj;
	}
	
	public PopUpDTO getPropertyPopup(String lowerDate,String upperDate,String propertyName){
		List<PublisherPropertiesObj> resultList=null;
		PopUpDTO popUpObj=null;
		ILinMobileDAO linDAO=new LinMobileDAO();
		try {
			popUpObj=new PopUpDTO();
			resultList=linDAO.loadPerformanceByPropertyList(lowerDate, upperDate, propertyName);
			
			if(resultList !=null && resultList.size()>0){
				log.info("Got property :"+resultList.size());
				PublisherPropertiesObj obj=resultList.get(0);				
				popUpObj.seteCPM(String.valueOf(obj.geteCPM()));
				popUpObj.setClicksInSelectedTime(String.valueOf(obj.getClicks()));
				popUpObj.setImpressionDeliveredInSelectedTime(String.valueOf(obj.getImpressionsDelivered()));
				popUpObj.setPayout(String.valueOf(obj.getPayout()));
				popUpObj.setChangeCTRInSelectedTime(String.valueOf(obj.getCHG()));
				
				StringBuffer strBuffer=new StringBuffer();
				
				strBuffer.append("[['Days',   'Impression']");
				for (PublisherPropertiesObj object:resultList) {
					strBuffer.append(",[");
					strBuffer.append("'"+DateUtil.getFormatedDate(object.getDate(), "yyyy-MM-dd", "MM/dd")+"',"+object.getImpressionsDelivered());
					strBuffer.append("]");
				}
				strBuffer.append("]");
				log.info("strBuffer.toString():"+strBuffer.toString());
				popUpObj.setChartData(strBuffer.toString());
		
			}else{
				log.info("No property found:"+propertyName+" and time: lowerDate:"+lowerDate+" and upperDate:"+upperDate);
			}
			
		} catch (DataServiceException e) {
			log.severe("Exception in loading property:"+propertyName+" : "+e.getMessage());
			
		}
		return popUpObj;
	   
	}
	
	public List<PublisherTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderData(String lowerDate,String upperDate,String publisherName,String channelName){
		List<PublisherTrendAnalysisHeaderDTO> list = new ArrayList<PublisherTrendAnalysisHeaderDTO>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
        String replaceStr = "\\\\'";
        String channelAndDataSourceQuery = "";
		try {
			if(channelName!=null){
				StringBuilder memcacheKeysubstring = new StringBuilder();
				channelAndDataSourceQuery = getChannelAndDataSourceQuery(userService.CommaSeperatedStringToList(channelName), memcacheKeysubstring, false);
				channelName = channelName.replaceAll("'", replaceStr);
				channelName = channelName.replaceAll(",", "','");
			}else{
				channelName = "";
			}
			
			list = linDAO.loadTrendAnalysisHeaderData(lowerDate, upperDate, publisherName, channelName, channelAndDataSourceQuery);
		} catch (Exception e) {
			log.severe("Exception in loadTrendAnalysisHeaderData of  LinMobileBusinessService : "+e.getMessage());
			
		}
		return list;
		
	}
	
	public List<PublisherInventoryRevenueObj> loadInventoryRevenueHeaderData(String lowerDate,String upperDate,String publisherName,String channelName){
		List<PublisherInventoryRevenueObj> list = new ArrayList<PublisherInventoryRevenueObj>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<String> channelArrList = null;
		try {
			channelArrList = userService.CommaSeperatedStringToList(channelName);
			StringBuilder memcacheKeysubstring = new StringBuilder();
			String channelAndDataSourceQuery = getChannelAndDataSourceQuery(channelArrList, memcacheKeysubstring, false);
			String ChannelsStr = ChannelsInQString(channelArrList, memcacheKeysubstring);
			list = linDAO.loadInventoryRevenueHeaderData(lowerDate, upperDate, publisherName, ChannelsStr, channelAndDataSourceQuery);
		} catch (Exception e) {
			log.severe("Exception in loadInventoryRevenueHeaderData of  LinMobileBusinessService : "+e.getMessage());
			
		}
		return list;
		
	}

	public List<PublisherSummaryObj> loadPublisherSummaryData(String lowerDate,String upperDate,String publisherName, long dataStorePublisherId, long userId, String channelNames)
	{
		List<PublisherSummaryObj> list = new ArrayList<PublisherSummaryObj>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		//IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			if(lowerDate == null && upperDate== null)
			{	// get MTD data
				String monthToDateEnd=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
				String[] dayArray=monthToDateEnd.split("-");
				int year=Integer.parseInt(dayArray[0]);
				int month=Integer.parseInt(dayArray[1])-1; // subtract 1 from day String as day starts from 0 to 11 in Calender class
				String monthToDateStart=DateUtil.getDateByYearMonthDays(year,month,1,"yyyy-MM-dd");
				
				lowerDate = monthToDateStart;
				upperDate = monthToDateEnd;
			}
			String channelIds = getChannelsBQId(channelNames);
			String publisherId = getPublisherBQId(publisherName);
			QueryDTO queryDTO = getQueryDTO(publisherId, lowerDate, upperDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				//StringBuilder allChannelAndDataSourceDFP_MemcacheKeyPart = new StringBuilder();
				StringBuilder DFP_Properties_MemcacheKeyPart = new StringBuilder(); 
				String selectedDFPPropertiesQuery = getSelectedDFPPropertiesQuery(dataStorePublisherId, userId, DFP_Properties_MemcacheKeyPart);
				//String selectedDFPPropertiesQuery = "";
				//String allChannelAndDataSourceQuery = getChannelAndDataSourceQuery(userService.CommaSeperatedStringToList(channelNames), allChannelAndDataSourceDFP_MemcacheKeyPart, true);
				list = linDAO.loadPublisherSummaryData(lowerDate, upperDate, selectedDFPPropertiesQuery, channelIds, queryDTO);
			}
		} catch (Exception e) {
			log.severe("Exception in loadPublisherSummaryData of  LinMobileBusinessService : "+e.getMessage());
			
		}
		return list;
		
	
	}
	
	public Map<String,List<PublisherPropertiesObj>> loadAllPerformanceByProperty(String lowerDate,String upperDate,String publisherName, long dataStorePublisherId, long userId,String channels,String applyFlag) throws Exception
	{

		List<PublisherPropertiesObj> list = new ArrayList<PublisherPropertiesObj>();
		List<PublisherPropertiesObj> allPerformanceByPropertyTempList = new ArrayList<PublisherPropertiesObj>();
		List<PublisherPropertiesObj> allPerformanceByPropertyList = new ArrayList<PublisherPropertiesObj>();
		List<PublisherPropertiesObj> allPerformanceByPropertyBySiteNameList = new ArrayList<PublisherPropertiesObj>();
		List<PublisherPropertiesObj> performanceByPropertyDFPObjectList = new ArrayList<PublisherPropertiesObj>();
		List<PublisherPropertiesObj> performanceByPropertyNonDFPObjectList = new ArrayList<PublisherPropertiesObj>();
		
		ILinMobileDAO linDAO=new LinMobileDAO();
		Map<String,Double> totalImpByChannelMap = new HashMap<String,Double>();
		Map<String,PublisherPropertiesObj> dataBySiteMap = new HashMap<String,PublisherPropertiesObj>();
		Map<String,List<PublisherPropertiesObj>> performanceByPropertyMap = new HashMap<String,List<PublisherPropertiesObj>>();
		Map<String,PublisherPropertiesObj> DFPObjectMap = new HashMap<String,PublisherPropertiesObj>();
		Map<String,PublisherPropertiesObj> nonDFPObjectMap = new HashMap<String,PublisherPropertiesObj>();
		double totalImpDelByChannel = 0;
		
		//StringBuilder allChannelAndDataSource_MemcacheKeyPart = new StringBuilder();
		StringBuilder DFP_Properties_MemcacheKeyPart = new StringBuilder(); 
		//StringBuilder channelWithDataSourceDFP_MemcacheKeyPart = new StringBuilder(); 
		//String allChannelAndDataSourceQuery = getChannelAndDataSourceQuery(null, allChannelAndDataSource_MemcacheKeyPart, true);
		String selectedDFPPropertiesQuery = getSelectedDFPPropertiesQuery(dataStorePublisherId, userId, DFP_Properties_MemcacheKeyPart);
		//String selectedDFPSource = getChannelWithDataSourceDFPQuery(channelWithDataSourceDFP_MemcacheKeyPart);
		
		String channelIds = getChannelsBQId(channels);
		String publisherId = getPublisherBQId(publisherName);
		list = MemcacheUtil.getAllPerformanceByPropertyList(lowerDate, upperDate,publisherId, channelIds, DFP_Properties_MemcacheKeyPart.toString());
		if(list==null || list.size()<=0){
			QueryDTO queryDTO = getQueryDTO(publisherId, lowerDate, upperDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				list = linDAO.loadAllPerformanceByProperty(lowerDate, upperDate, channelIds, selectedDFPPropertiesQuery, queryDTO);
				MemcacheUtil.setAllPerformanceByPropertyList(list, lowerDate, upperDate,publisherId, channelIds, DFP_Properties_MemcacheKeyPart.toString());
			}
		}
		if(list!=null && list.size()>0){
			IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
			List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(MemcacheUtil.getAllCompanyList());
			for (PublisherPropertiesObj publisherPropertiesObj : list) {
				if(publisherPropertiesObj!=null){
					CompanyObj demandPartnerCompany = userDetailsDAO.getCompanyByName(publisherPropertiesObj.getChannelName().trim(), demandPartnersList);
					if(demandPartnerCompany!=null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null && !demandPartnerCompany.getCompanyName().trim().equalsIgnoreCase("") && demandPartnerCompany.getDataSource() != null && !demandPartnerCompany.getDataSource().trim().equalsIgnoreCase("")){
						publisherPropertiesObj.setDataSource(demandPartnerCompany.getDataSource().trim());
					}
					if( !totalImpByChannelMap.containsKey(publisherPropertiesObj.getChannelName()) && !publisherPropertiesObj.getDataSource().equals(LinMobileConstants.DEFAULT_DATASOURE_NAME)){
						totalImpByChannelMap.put(publisherPropertiesObj.getChannelName(), Math.round(publisherPropertiesObj.getImpressionsDelivered())+0.0);
					}else if(totalImpByChannelMap.size()!=0 && totalImpByChannelMap.containsKey(publisherPropertiesObj.getChannelName())){
						totalImpDelByChannel =  totalImpByChannelMap.get(publisherPropertiesObj.getChannelName())+ Math.round(publisherPropertiesObj.getImpressionsDelivered());
						totalImpByChannelMap.put(publisherPropertiesObj.getChannelName(), totalImpDelByChannel);
					}
				}
				allPerformanceByPropertyTempList.add(publisherPropertiesObj);
			}
		}
		
		if(allPerformanceByPropertyTempList!=null && allPerformanceByPropertyTempList.size()>0){
				for (PublisherPropertiesObj publisherPropertiesObj : allPerformanceByPropertyTempList) {
					if(publisherPropertiesObj!=null){
						if(totalImpByChannelMap!=null && totalImpByChannelMap.containsKey(publisherPropertiesObj.getChannelName())){
							publisherPropertiesObj.setTotalImpressionsDeliveredByChannelName(totalImpByChannelMap.get(publisherPropertiesObj.getChannelName()));
						}
					}
					
					allPerformanceByPropertyList.add(publisherPropertiesObj);
				}
				performanceByPropertyMap.put("allData", allPerformanceByPropertyList);
		}
		MemcacheUtil.setAllPerformanceByPropertyCalculatedList(allPerformanceByPropertyList,lowerDate,upperDate,publisherId, channelIds, DFP_Properties_MemcacheKeyPart.toString());
		
		if(allPerformanceByPropertyList!=null && allPerformanceByPropertyList.size()>0){
			List<String> channelArrList = new ArrayList<String>();
			String[] str = channels.split(",");
			if(str!=null){
				for (String channel : str) {
					channelArrList.add(channel);
				}
			}
			for (PublisherPropertiesObj publisherPropertiesObjBySiteName : allPerformanceByPropertyList) {
				if( channelArrList!=null &&  channelArrList.contains(publisherPropertiesObjBySiteName.getChannelName())){
				if(publisherPropertiesObjBySiteName!=null && publisherPropertiesObjBySiteName.getDataSource().equals(LinMobileConstants.DEFAULT_DATASOURE_NAME)){
					performanceByPropertyDFPObjectList.add(publisherPropertiesObjBySiteName);
				}else{
					performanceByPropertyNonDFPObjectList.add(publisherPropertiesObjBySiteName);
				}
			  }
			}
		}
		
		if(performanceByPropertyDFPObjectList!=null && performanceByPropertyDFPObjectList.size()>0){
			for (PublisherPropertiesObj publisherPropertiesObj : performanceByPropertyDFPObjectList) {
				if(!DFPObjectMap.containsKey(publisherPropertiesObj.getSite())){
					DFPObjectMap.put(publisherPropertiesObj.getSite(), publisherPropertiesObj);
					
				}else{
					PublisherPropertiesObj propertiesObj = copyObject(DFPObjectMap.get(publisherPropertiesObj.getSite()));
					double impressions = publisherPropertiesObj.getImpressionsDelivered();
					double payout = publisherPropertiesObj.getDFPPayout();
				    long clicks = publisherPropertiesObj.getClicks();
					propertiesObj.setImpressionsDelivered(impressions + propertiesObj.getImpressionsDelivered());
					propertiesObj.setClicks(clicks + propertiesObj.getClicks());
					propertiesObj.setDFPPayout(payout + propertiesObj.getDFPPayout());
					if(propertiesObj.getImpressionsDelivered()!=0){
						double ecpm = (propertiesObj.getDFPPayout()/propertiesObj.getImpressionsDelivered())*1000;	
						propertiesObj.seteCPM(ecpm);
					}
					DFPObjectMap.put(propertiesObj.getSite(), propertiesObj);
				}
				
			}
			 Iterator iterator = DFPObjectMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) iterator.next();
					allPerformanceByPropertyBySiteNameList.add((PublisherPropertiesObj) mapEntry.getValue());
			}
		}
		
		if(performanceByPropertyNonDFPObjectList!=null && performanceByPropertyNonDFPObjectList.size()>0)
		{
			for (PublisherPropertiesObj publisherPropertiesObj : performanceByPropertyNonDFPObjectList) {
				//PublisherPropertiesObj propertiesObj = copyObject(dataBySiteMap.get(publisherPropertiesObj.getSite()));
				if(!nonDFPObjectMap.containsKey(publisherPropertiesObj.getSite())){
					nonDFPObjectMap.put(publisherPropertiesObj.getSite(), publisherPropertiesObj);
					double requestWeightage = 0.0;
					double impressions = 0.0;
					double payout = 0.0;
					
					PublisherPropertiesObj propertiesObj = copyObject(nonDFPObjectMap.get(publisherPropertiesObj.getSite()));
					if(propertiesObj.getTotalImpressionsDeliveredByChannelName()!=0){
						requestWeightage = propertiesObj.getImpressionsDelivered()/propertiesObj.getTotalImpressionsDeliveredByChannelName();
					}
					long clicks = publisherPropertiesObj.getClicks();
					 impressions =  (requestWeightage * propertiesObj.getTotalImpressionsDeliveredBySiteName());
					 payout = requestWeightage * propertiesObj.getPayout();
					propertiesObj.setImpressionsDelivered((impressions));
					propertiesObj.setPayout(payout);
					propertiesObj.setClicks(clicks + propertiesObj.getClicks());
					if(propertiesObj.getImpressionsDelivered()!=0){
						double ecpm = (propertiesObj.getPayout()/propertiesObj.getImpressionsDelivered())*1000;	
						propertiesObj.seteCPM(ecpm);
					}
					nonDFPObjectMap.put(propertiesObj.getSite(), propertiesObj);
				}else{
					double requestWeightage = 0.0;
					double impressions = 0.0;
					double payout = 0.0;
					PublisherPropertiesObj propertiesObj = copyObject(nonDFPObjectMap.get(publisherPropertiesObj.getSite()));
					if(publisherPropertiesObj.getTotalImpressionsDeliveredByChannelName()!=0){
						 requestWeightage = publisherPropertiesObj.getImpressionsDelivered()/publisherPropertiesObj.getTotalImpressionsDeliveredByChannelName();
					}
					long clicks = publisherPropertiesObj.getClicks();
					 impressions =  (requestWeightage * publisherPropertiesObj.getTotalImpressionsDeliveredBySiteName());
					 payout = requestWeightage * publisherPropertiesObj.getPayout();
					propertiesObj.setImpressionsDelivered(impressions+propertiesObj.getImpressionsDelivered());
					propertiesObj.setPayout(payout+propertiesObj.getPayout());
					propertiesObj.setClicks(clicks + propertiesObj.getClicks());
					if(propertiesObj.getImpressionsDelivered()!=0){
						double ecpm = (propertiesObj.getPayout()/propertiesObj.getImpressionsDelivered())*1000;	
						propertiesObj.seteCPM(ecpm);
					}
					nonDFPObjectMap.put(propertiesObj.getSite(), propertiesObj);
				}
			}
			 Iterator iterator = nonDFPObjectMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				allPerformanceByPropertyBySiteNameList.add((PublisherPropertiesObj) mapEntry.getValue());
		     }
		}
		
		if(allPerformanceByPropertyBySiteNameList!=null && allPerformanceByPropertyBySiteNameList.size()>0){
			for (PublisherPropertiesObj publisherPropertiesObj : allPerformanceByPropertyBySiteNameList) {
				if(!dataBySiteMap.containsKey(publisherPropertiesObj.getSite())){
					dataBySiteMap.put(publisherPropertiesObj.getSite(), publisherPropertiesObj);
					PublisherPropertiesObj propertiesObj = copyObject(dataBySiteMap.get(publisherPropertiesObj.getSite()));
					if(propertiesObj.getDataSource().equals(LinMobileConstants.DEFAULT_DATASOURE_NAME)){
						double payout = propertiesObj.getDFPPayout();
						propertiesObj.setPayout(payout);
						dataBySiteMap.put(publisherPropertiesObj.getSite(), propertiesObj);
					}
					
				}else{
					double payout = 0.0;
					double previousPayout = 0.0;
					PublisherPropertiesObj propertiesObj = copyObject(dataBySiteMap.get(publisherPropertiesObj.getSite()));
					long clicks = publisherPropertiesObj.getClicks();
					double impressions = Math.round(publisherPropertiesObj.getImpressionsDelivered()) + 0.0;
					if(!publisherPropertiesObj.getDataSource().equals(LinMobileConstants.DEFAULT_DATASOURE_NAME)){
						 payout = publisherPropertiesObj.getPayout();
					}else{
						 payout = publisherPropertiesObj.getDFPPayout();
					}
					if(!propertiesObj.getDataSource().equals(LinMobileConstants.DEFAULT_DATASOURE_NAME)){
						previousPayout = propertiesObj.getPayout();
					}else{
						previousPayout = propertiesObj.getDFPPayout();
					}
					propertiesObj.setImpressionsDelivered(impressions + Math.round(propertiesObj.getImpressionsDelivered())+0.0);
					propertiesObj.setPayout(payout + previousPayout);
					propertiesObj.setClicks(clicks + propertiesObj.getClicks());
					if(propertiesObj.getImpressionsDelivered()!=0){
						double ecpm = (propertiesObj.getPayout()/propertiesObj.getImpressionsDelivered())*1000;	
						propertiesObj.seteCPM(ecpm);
					}
					dataBySiteMap.put(propertiesObj.getSite(), propertiesObj);
				}
			}
			 Iterator iterator = dataBySiteMap.entrySet().iterator();
			 allPerformanceByPropertyBySiteNameList = new ArrayList<PublisherPropertiesObj>();
				while (iterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) iterator.next();
					allPerformanceByPropertyBySiteNameList.add((PublisherPropertiesObj) mapEntry.getValue());
			}
				performanceByPropertyMap.put("allDataBySiteName", allPerformanceByPropertyBySiteNameList);
		}
	
		return performanceByPropertyMap;
	}
	
	
	public List<PublisherReallocationHeaderDTO> loadReallocationHeaderData(String lowerDate,String upperDate,String publisherName,String channelName)
	{

		List<PublisherReallocationHeaderDTO> list = new ArrayList<PublisherReallocationHeaderDTO>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		List<String> channelArrList = new ArrayList<String>();
		String[] str = channelName.split(",");
		if(str!=null){
			for (String channel : str) {
				channelArrList.add(channel);
			}
		}
		
		list = linDAO.loadReallocationHeaderData(lowerDate, upperDate, publisherName, channelArrList);
		
		return list;
		
	
	}
	
	
	
	public List<PropertyDropDownDTO> loadPublisherPropertyDropDownList(String publisherName,long userId,String str)
	{
		List<PropertyDropDownDTO> propertyList = new ArrayList<PropertyDropDownDTO>();
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		if(publisherName!=null){
			publisherName = publisherName.replaceAll("'", replaceStr);
		}
		
		/*ILinMobileDAO linDAO=new LinMobileDAO();                                                  24.08.2013
		propertyList = linDAO.loadPublisherPropertyDropDownList(publisherName,userId,str);*/
		IUserDetailsDAO dao = new UserDetailsDAO();
		propertyList = dao.loadPropertyDropDownList(publisherName,userId,str);
		if(propertyList!=null && propertyList.size()>0){
			return propertyList;
		}
		return new ArrayList<PropertyDropDownDTO>();
	}	

	@Override
	public List<CommonDTO> getAllPublishersByPublisherIdAndUserId(String selectedPublisherId, List<String> channelsNameList, long userId) {
		log.info("Inside getAllPublishersByPublisherIdAndUserId in LinMobileBusinessService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> publisherIdList = new ArrayList<String>();
		List<CommonDTO> allPublishersList = new ArrayList<CommonDTO>(0);
		try {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			List<CompanyObj> publisherList = dao.getAllPublishers(companyObjList);
			List<CompanyObj> selectedPublishersList = dao.getSelectedPublishersByUserId(userId, publisherIdList, publisherList);
			if(!selectedPublisherId.trim().equalsIgnoreCase("")) {
				CompanyObj publisherCompany = dao.getCompanyById(Long.valueOf(selectedPublisherId), publisherList);
				if(publisherCompany != null && publisherIdList.contains(String.valueOf(publisherCompany.getId())) && publisherCompany.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyName() != null) {
					allPublishersList.add(new CommonDTO(selectedPublisherId.trim(), publisherCompany.getCompanyName().trim()));
				}
			}
			
			if(selectedPublishersList != null && selectedPublishersList.size() > 0) {
				for (CompanyObj publisherCompany : selectedPublishersList) {
					if(publisherCompany != null && publisherCompany.getStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyName() != null && !publisherCompany.getCompanyName().trim().equalsIgnoreCase("")) {
						if(!selectedPublisherId.trim().equalsIgnoreCase("") && selectedPublisherId.trim().equals(String.valueOf(publisherCompany.getId()))) {
							//do nothing
						}
						else {
							allPublishersList.add(new CommonDTO(String.valueOf(publisherCompany.getId()),publisherCompany.getCompanyName().trim()));
						}
					}
				}
			}
			if(allPublishersList != null && allPublishersList.size() > 0) {
				selectedPublisherId = allPublishersList.get(0).getId();
				String commaSeperatedChannelsName = getCommaSeperatedChannelsNameByPublisherIdAndUserId(selectedPublisherId, userId, companyObjList);
				if(commaSeperatedChannelsName != null && !commaSeperatedChannelsName.trim().equalsIgnoreCase("")) {
					channelsNameList.add(commaSeperatedChannelsName);
				}
			}
		} catch (Exception e) {
			log.severe("Exception in getAllPublishersByPublisherIdAndUserId of LinMobileBusinessService"+e.getMessage());
			
		}
		return allPublishersList;
	}

	@Override
	public String getCommaSeperatedChannelsNameByPublisherIdAndUserId(String publisherId, long userId, List<CompanyObj> companyObjList) throws Exception {
		log.info("Inside getCommaSeperatedChannelsNameByPublisherIdAndUserId in LinMobileBusinessService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> commaSeperatedChannelsNameList = new ArrayList<String>(0);
		String commaSeperatedChannelsName = "";
		List<CompanyObj> demandPartnersList = dao.getActiveDemandPartnersByPublisherCompanyId(publisherId);
		if(demandPartnersList != null && demandPartnersList.size() > 0) {
			log.info("demandPartnersIdList.size : "+demandPartnersList.size());
			for (CompanyObj demandPartner : demandPartnersList) {
				if(demandPartner != null && !commaSeperatedChannelsNameList.contains(demandPartner.getCompanyName().trim())) {
					commaSeperatedChannelsNameList.add(demandPartner.getCompanyName().trim());
					commaSeperatedChannelsName = commaSeperatedChannelsName + demandPartner.getCompanyName().trim() + ",";
				}
			}
			if(commaSeperatedChannelsName.lastIndexOf(",") != -1) {
				commaSeperatedChannelsName = commaSeperatedChannelsName.substring(0, commaSeperatedChannelsName.lastIndexOf(","));
			}
		}
		return commaSeperatedChannelsName;
	}
	
	public List<PropertyDropDownDTO> loadAdvertiserPropertyDropDownList(String publisherId,long userId,String str)
	{
		List<PropertyDropDownDTO> propertyList = new ArrayList<PropertyDropDownDTO>();
		String replaceStr = "\\\\'";
		if(str!=null){
			str = str.replaceAll("'", replaceStr);
		}
		IUserDetailsDAO dao = new UserDetailsDAO();
		propertyList = dao.loadPropertyDropDownList(publisherId,userId,str);
		if(propertyList!=null && propertyList.size()>0){
			return propertyList;
		}
		return new ArrayList<PropertyDropDownDTO>();
	}

	@Override
	public String getCommaSeperatedChannelsNameByChannelIdList(List<String> channelIdList) throws Exception {
		log.info("Inside getCommaSeperatedChannelsNameByChannelIdList of LinMobileBussinessService");
		String commaSeperatedChannelNames = "";
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(MemcacheUtil.getAllCompanyList());
		if(channelIdList != null && channelIdList.size() > 0) {
			for (String channelId : channelIdList) {
				CompanyObj demandPartnerCompany = userDetailsDAO.getCompanyById(Long.valueOf(channelId), demandPartnersList);
				if(demandPartnerCompany != null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null) {
					commaSeperatedChannelNames = commaSeperatedChannelNames + demandPartnerCompany.getCompanyName().trim() + ",";
				}
			}
		}
		if(commaSeperatedChannelNames.lastIndexOf(",") != -1) {
			commaSeperatedChannelNames = commaSeperatedChannelNames.substring(0, commaSeperatedChannelNames.lastIndexOf(","));
		}
		return commaSeperatedChannelNames;
	}

	@Override
	public String getCommaSeperatedChannelsName(String publisherName) throws Exception {
		log.info("Inside getCommaSeperatedChannelsName of LinMobileBussinessService");
		String commaSeperatedChannelNames = "";
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		List<CompanyObj> demandPartnersList = userDetailsDAO.getAllDemandPartners(companyObjList);
		CompanyObj publisherCompany = userDetailsDAO.getCompanyObjByNameAndCompanyType(publisherName, LinMobileConstants.COMPANY_TYPE[0], companyObjList);
		if(publisherCompany != null && publisherCompany.getStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getDemandPartnerId() != null) {
			List<String> channelIdList = publisherCompany.getDemandPartnerId();
			if(channelIdList != null && channelIdList.size() > 0) {
				for (String channelId : channelIdList) {
					CompanyObj demandPartnerCompany = userDetailsDAO.getCompanyById(Long.valueOf(channelId), demandPartnersList);
					if(demandPartnerCompany != null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null) {
						commaSeperatedChannelNames = commaSeperatedChannelNames + demandPartnerCompany.getCompanyName().trim() + ",";
					}
				}
			}
		}
		if(commaSeperatedChannelNames.lastIndexOf(",") != -1) {
			commaSeperatedChannelNames = commaSeperatedChannelNames.substring(0, commaSeperatedChannelNames.lastIndexOf(","));
		}
		return commaSeperatedChannelNames;
	}
	
	public List<ReconciliationDataDTO> createSummaryData(List<ReconciliationDataDTO> reconciliationDataDTOList, String channelName, List<String> passBackList) {
		log.info("Inside createSummaryData of LinMobileBussinessService");
		List<ReconciliationDataDTO> tempReconciliationSummaryDataList = new ArrayList<ReconciliationDataDTO>();
		long DFP_requests = 0;
		long DFP_delivered = 0;
		long DFP_passback = 0;
		long demandPartnerRequests = 0;
		long demandPartnerDelivered = 0;
		long demandPartnerPassbacks = 0;
		double varianceRequests = 0;
		double varianceDelivered = 0;
		double variancePassbacks = 0;
		
		if(reconciliationDataDTOList != null && reconciliationDataDTOList.size() > 0) {
			for (ReconciliationDataDTO reconciliationDataDTO : reconciliationDataDTOList) {
				if(reconciliationDataDTO != null) {
					DFP_requests = DFP_requests +reconciliationDataDTO.getDFP_requests();
					DFP_delivered = DFP_delivered +reconciliationDataDTO.getDFP_delivered();
					DFP_passback = DFP_passback +reconciliationDataDTO.getDFP_passback();
					demandPartnerRequests = demandPartnerRequests +reconciliationDataDTO.getDemandPartnerRequests();
					demandPartnerDelivered = demandPartnerDelivered +reconciliationDataDTO.getDemandPartnerDelivered();
					demandPartnerPassbacks = demandPartnerPassbacks +reconciliationDataDTO.getDemandPartnerPassbacks();
				}
			}
			if(DFP_requests != 0) {
				varianceRequests = Double.valueOf((demandPartnerRequests - DFP_requests)*100)/DFP_requests;
			}
			if(DFP_delivered != 0) {
				varianceDelivered = Double.valueOf((demandPartnerDelivered - DFP_delivered)*100)/DFP_delivered;
			}
			if(DFP_passback != 0) {
				variancePassbacks = Double.valueOf((demandPartnerPassbacks - DFP_passback)*100)/DFP_passback;
			}
			
			if(DFP_requests == 0) {
				DFP_delivered = 0;
				varianceRequests = 0;
				varianceDelivered = 0;
			}
			if(demandPartnerRequests == 0) {
				demandPartnerPassbacks = 0;
				varianceRequests = 0;
				variancePassbacks = 0;
			}
			
			ReconciliationDataDTO dto = new ReconciliationDataDTO();
			if(passBackList == null || passBackList.size() == 0) {
				dto.setSiteType("NA");
			}
			dto.setChannelName(channelName);
			dto.setDFP_requests(DFP_requests);
			dto.setDFP_delivered(DFP_delivered);
			dto.setDFP_passback(DFP_passback);
			dto.setDemandPartnerRequests(demandPartnerRequests);
			dto.setDemandPartnerDelivered(demandPartnerDelivered);
			dto.setDemandPartnerPassbacks(demandPartnerPassbacks);
			dto.setVarianceRequests(varianceRequests);
			dto.setVarianceDelivered(varianceDelivered);
			dto.setVariancePassbacks(variancePassbacks);
			tempReconciliationSummaryDataList.add(dto);
			
		}
		
		return tempReconciliationSummaryDataList;
	}

	@Override
	public void loadAllReconciliationData(String startDate, String endDate, String publisherId, long userId, List<ReconciliationDataDTO> reconciliationSummaryDataList, List<ReconciliationDataDTO> reconciliationDetailsDataList) throws Exception {
		log.info("Inside loadAllReconciliationData of LinMobileBussinessService");
		ILinMobileDAO linDAO=new LinMobileDAO();
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<ReconciliationDataDTO> tempSubList = null;
		List<Date> dateList = new ArrayList<Date>();
		List<ReconciliationDataDTO> reconciliationDataDTOList = null;
		List<ReconciliationDataDTO> resultList = null;
		List<CommonDTO> channelsInfo = new ArrayList<CommonDTO>();
		HashMap<String, List<String>> channelPassbackSiteTypeMap = new HashMap<String, List<String>>();
		String publisherName = "";
		int tempCount = 0;
		
		List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		CompanyObj publisherCompany = userDetailsDAO.getCompanyObjByIdAndCompanyType(publisherId, LinMobileConstants.COMPANY_TYPE[0], companyObjList);
		if(publisherCompany != null && publisherCompany.getStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyName() != null && !publisherCompany.getCompanyName().equalsIgnoreCase("")) {
			List<CompanyObj> demandPartnersList = userDetailsDAO.getActiveDemandPartnersByPublisherCompanyId(publisherId);
			if(demandPartnersList != null && demandPartnersList.size() > 0) {
				List<String> passbackValues = userDetailsDAO.getPassbackValues(demandPartnersList);
				String hashedPassbackValueString = "";
				String lineItemQuery = "line_item =''";
				if(passbackValues != null && passbackValues.size() > 0) {
					lineItemQuery = "";
					for(String passbackValue : passbackValues) {
						hashedPassbackValueString = hashedPassbackValueString + passbackValue.trim();
						lineItemQuery = lineItemQuery + "line_item ='"+passbackValue.trim()+"' or ";
					}
					if(lineItemQuery.lastIndexOf("or") != -1) {
						lineItemQuery = lineItemQuery.substring(0, lineItemQuery.lastIndexOf("or"));
					}
					hashedPassbackValueString = EncriptionUtil.getEncriptedStrMD5(hashedPassbackValueString);
				}
				publisherName = publisherCompany.getCompanyName();
				
				String publisherBQId = getPublisherBQId(publisherName);
				QueryDTO queryDTO = getQueryDTO(publisherBQId, startDate, endDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
				
				resultList = MemcacheUtil.getReconciliationDataList(startDate, endDate, publisherBQId, hashedPassbackValueString);
				if(resultList != null && resultList.size()> 0) {
					reconciliationDataDTOList =resultList;
				}
				else {
					resultList = linDAO.loadAllRecociliationData(startDate, endDate, lineItemQuery, queryDTO);
					if(resultList != null && resultList.size()> 0) {
						MemcacheUtil.setReconciliationDataList(resultList, startDate, endDate, publisherBQId, hashedPassbackValueString);
						reconciliationDataDTOList =resultList;
					}
				}
				
				if(reconciliationDataDTOList != null && reconciliationDataDTOList.size() > 0) {
					for (CompanyObj demandPartner : demandPartnersList) {
						if(demandPartner != null && !demandPartner.getDataSource().trim().equals(LinMobileConstants.DFP_DATA_SOURCE)) {
							if(demandPartner.getPassback_Site_type() != null && demandPartner.getPassback_Site_type().size() > 0) {
								channelPassbackSiteTypeMap.put(demandPartner.getCompanyName().trim(), demandPartner.getPassback_Site_type());
							}
							else {
								channelPassbackSiteTypeMap.put(demandPartner.getCompanyName().trim(), new ArrayList<String>());
							}
							CommonDTO commonDTO = new CommonDTO();
							commonDTO.setChannelName(demandPartner.getCompanyName().trim());
							commonDTO.setChannelDataSource(demandPartner.getDataSource().trim());
							channelsInfo.add(commonDTO);
						}
					}
					
					if(channelsInfo != null && channelsInfo.size() > 0) {
						List<String> tempDateList = new ArrayList<String>();
						for (ReconciliationDataDTO reconciliationDataDTO : reconciliationDataDTOList) {
							if(reconciliationDataDTO != null && reconciliationDataDTO.getDate() != null && !tempDateList.contains(reconciliationDataDTO.getFormattedDate().trim())) {
								tempDateList.add(reconciliationDataDTO.getFormattedDate().trim());
								dateList.add(reconciliationDataDTO.getDate());
							}
						}
						if(dateList != null && dateList.size() > 0) {
							for(CommonDTO channelInfo : channelsInfo) {
								List<ReconciliationDataDTO> tempReconciliationDetailsDataList = new ArrayList<ReconciliationDataDTO>(); 
								String channel = channelInfo.getChannelName().trim();
								String channelDataSource = channelInfo.getChannelDataSource().trim();
								List<String> passBackList = channelPassbackSiteTypeMap.get(channel);
								tempSubList = reconciliationDataDTOList.subList(0, reconciliationDataDTOList.size()-1);
								tempCount = 0;
								for(Date date : dateList) {
									long DFP_requests = 0;
									long DFP_delivered = 0;
									long DFP_passback = 0;
									long demandPartnerRequests = 0;
									long demandPartnerDelivered = 0;
									long demandPartnerPassbacks = 0;
									double varianceRequests = 0;
									double varianceDelivered = 0;
									double variancePassbacks = 0;
									for (ReconciliationDataDTO reconciliationDataDTO : tempSubList) {
										if(reconciliationDataDTO != null && reconciliationDataDTO.getDate() != null) {
											if(reconciliationDataDTO.getDate().compareTo(date) > 0) {
												// create detail data
												
												if(passBackList == null || passBackList.size() == 0) {
													DFP_requests = 0;
													DFP_delivered = 0;
													DFP_passback = 0;
													demandPartnerRequests = 0;
													demandPartnerDelivered = 0;
													demandPartnerPassbacks = 0;
													varianceRequests = 0;
													varianceDelivered = 0;
													variancePassbacks = 0;
												}
												
												DFP_delivered = DFP_requests - DFP_passback;
												demandPartnerPassbacks = demandPartnerRequests - demandPartnerDelivered;
												if(DFP_requests != 0) {
													varianceRequests = Double.valueOf((demandPartnerRequests - DFP_requests)*100)/DFP_requests;
												}
												if(DFP_delivered != 0) {
													varianceDelivered = Double.valueOf((demandPartnerDelivered - DFP_delivered)*100)/DFP_delivered;
												}
												if(DFP_passback != 0) {
													variancePassbacks = Double.valueOf((demandPartnerPassbacks - DFP_passback)*100)/DFP_passback;
												}
												
												if(DFP_requests == 0) {
													DFP_delivered = 0;
													varianceRequests = 0;
													varianceDelivered = 0;
												}
												if(demandPartnerRequests == 0) {
													demandPartnerPassbacks = 0;
													varianceRequests = 0;
													variancePassbacks = 0;
												}
												
												ReconciliationDataDTO dto = new ReconciliationDataDTO();
												dto.setChannelName(channel);
												dto.setFormattedDate(DateUtil.getFormatedStringDateYYYYMMDD(date));
												dto.setDFP_requests(DFP_requests);
												dto.setDFP_delivered(DFP_delivered);
												dto.setDFP_passback(DFP_passback);
												dto.setDemandPartnerRequests(demandPartnerRequests);
												dto.setDemandPartnerDelivered(demandPartnerDelivered);
												dto.setDemandPartnerPassbacks(demandPartnerPassbacks);
												dto.setVarianceRequests(varianceRequests);
												dto.setVarianceDelivered(varianceDelivered);
												dto.setVariancePassbacks(variancePassbacks);
												tempReconciliationDetailsDataList.add(dto);
												
												tempSubList = reconciliationDataDTOList.subList(tempCount, reconciliationDataDTOList.size()-1);
												break;
											}
											tempCount++;
											// calculate Data
											
											if(reconciliationDataDTO.getDataSource().trim().equalsIgnoreCase(LinMobileConstants.DFP_DATA_SOURCE) &&  reconciliationDataDTO.getChannelName().trim().equals(channel)) {
												DFP_requests = DFP_requests + reconciliationDataDTO.getRequests();
											}
											if(reconciliationDataDTO.getDataSource().trim().equalsIgnoreCase(LinMobileConstants.DFP_DATA_SOURCE) &&  passBackList.contains(reconciliationDataDTO.getSiteType().trim())) {
												DFP_passback = DFP_passback + reconciliationDataDTO.getDelivered();
											}
											if(reconciliationDataDTO.getDataSource().trim().equals(channelDataSource) && reconciliationDataDTO.getChannelName().trim().equals(channel)) {
												demandPartnerRequests = demandPartnerRequests + reconciliationDataDTO.getRequests();
												demandPartnerDelivered = demandPartnerDelivered + reconciliationDataDTO.getDelivered();
											}
											if(tempCount == reconciliationDataDTOList.size()-1) {
												
												if(passBackList == null || passBackList.size() == 0) {
													DFP_requests = 0;
													DFP_delivered = 0;
													DFP_passback = 0;
													demandPartnerRequests = 0;
													demandPartnerDelivered = 0;
													demandPartnerPassbacks = 0;
													varianceRequests = 0;
													varianceDelivered = 0;
													variancePassbacks = 0;
												}
												
												DFP_delivered = DFP_requests - DFP_passback;
												demandPartnerPassbacks = demandPartnerRequests - demandPartnerDelivered;
												if(DFP_requests != 0) {
													varianceRequests = Double.valueOf((demandPartnerRequests - DFP_requests)*100)/DFP_requests;
												}
												if(DFP_delivered != 0) {
													varianceDelivered = Double.valueOf((demandPartnerDelivered - DFP_delivered)*100)/DFP_delivered;
												}
												if(DFP_passback != 0) {
													variancePassbacks = Double.valueOf((demandPartnerPassbacks - DFP_passback)*100)/DFP_passback;
												}
												
												if(DFP_requests == 0) {
													DFP_delivered = 0;
													varianceRequests = 0;
													varianceDelivered = 0;
												}
												if(demandPartnerRequests == 0) {
													demandPartnerPassbacks = 0;
													varianceRequests = 0;
													variancePassbacks = 0;
												}
												
												ReconciliationDataDTO dto = new ReconciliationDataDTO();
												dto.setChannelName(channel);
												dto.setFormattedDate(DateUtil.getFormatedStringDateYYYYMMDD(date));
												dto.setDFP_requests(DFP_requests);
												dto.setDFP_delivered(DFP_delivered);
												dto.setDFP_passback(DFP_passback);
												dto.setDemandPartnerRequests(demandPartnerRequests);
												dto.setDemandPartnerDelivered(demandPartnerDelivered);
												dto.setDemandPartnerPassbacks(demandPartnerPassbacks);
												dto.setVarianceRequests(varianceRequests);
												dto.setVarianceDelivered(varianceDelivered);
												dto.setVariancePassbacks(variancePassbacks);
												tempReconciliationDetailsDataList.add(dto);
												
												tempSubList = reconciliationDataDTOList.subList(tempCount, reconciliationDataDTOList.size()-1);
											}
											
										}
									}
								}
								reconciliationDetailsDataList.addAll(tempReconciliationDetailsDataList);
								
								// create summary data
								
								List<ReconciliationDataDTO> tempReconciliationSummaryDataList = createSummaryData(tempReconciliationDetailsDataList, channel, passBackList);
								if(tempReconciliationSummaryDataList !=null && tempReconciliationSummaryDataList.size() > 0) {
									reconciliationSummaryDataList.addAll(tempReconciliationSummaryDataList);
								}
							}
						}
					}
				}
			}
		}
	}
	
	  public List<LineItemDTO> loadCampaignTraffickingData(DfpServices dfpServices,DfpSession session,String lowerDate, String upperDate,long userId)throws Exception
	  {
		  log.info("inside loadCampaignTraffickingData() of LinMobileBusinessService class");
		  LineItemServiceInterface lineItemService = dfpServices.get(session, LineItemServiceInterface.class);
		  List<LineItemDTO> totalLineItemDTOList=new ArrayList<LineItemDTO>();
		  List<LineItem> lineItemList=new ArrayList<LineItem>();
		  Statement lineItemStatement=new Statement();
		  LineItemPage page = null;
		  String query = "";
		 /*Getting DELIVERING lineitems*/
		  try
		  {
			  List<LineItemDTO> deliveringCampaignList = MemcacheUtil.getDeliveringCampaignList(lowerDate,upperDate);
			 if(deliveringCampaignList == null || deliveringCampaignList.size() <= 0)
			 {
			   query="WHERE status = '"+ComputedStatus.DELIVERING+"' AND targetPlatform = '"+TargetPlatform.MOBILE+"' AND startDateTime = '"+lowerDate+"' AND endDateTime = '"+upperDate+"'"; 
			   log.info("loadCampaignTraffickingData Query 1 (DELIVERING): "+query); 
			  
			   lineItemStatement.setQuery(query);
			  
			   int j=0;
			   do{
			          page=lineItemService.getLineItemsByStatement(lineItemStatement);
			          List<LineItem> result = page.getResults();
			          if(result !=null && result.size()>0)
			          {
			        	  deliveringCampaignList = getLineItemDTOList(result);
			        	  log.info("Delivering Campaigns  = "+deliveringCampaignList.size());
			          }
			          else
			          {
			        	  log.warning("No line items found for query (DELIVERING):"+query);
			          }
			   	}while(page.getResults()== null && j<=3);
			   
			   MemcacheUtil.setDeliveringCampaignList(deliveringCampaignList,lowerDate,upperDate);
		  
		  }
			 
		 if(deliveringCampaignList != null && deliveringCampaignList.size() > 0)
		 {
	    	totalLineItemDTOList.addAll(deliveringCampaignList);
		 }
	  }catch(Exception e)
	  {
				 log.severe("exception in loadCampaignTraffickingData LinMobileBusinessService (DELIVERING): "+e.getMessage());
				 
	  } 
          
		  
		  /*Getting READY lineitems*/
		  try
		  {
			  List<LineItemDTO> readyCampaignList = MemcacheUtil.getReadyCampaignList();
			  if(readyCampaignList == null || readyCampaignList.size() <= 0)
			  {
		        query="WHERE status = '"+ComputedStatus.READY+"' AND targetPlatform = '"+TargetPlatform.MOBILE+"'"; 
				log.info("loadCampaignTraffickingData Query 2 (READY): "+query); 
				lineItemStatement.setQuery(query);
				
				int j=0;
			do{
				page=lineItemService.getLineItemsByStatement(lineItemStatement);
				List<LineItem> result = page.getResults();
		        if(result !=null && result.size()>0)
		        {
		        	readyCampaignList = getLineItemDTOList(result);
		        	log.info("Ready Campaigns  = "+readyCampaignList.size());
		        }
		        else
		        {
		      	  	log.warning("No line items found for quer (READY): "+query);
		        }
			}while(page.getResults()== null && j<=3);
			
			MemcacheUtil.setReadyCampaignList(readyCampaignList);
			}
		   
		  if(readyCampaignList != null && readyCampaignList.size() > 0)
		  {
	    		totalLineItemDTOList.addAll(readyCampaignList);
		  }
			  
		}catch(Exception e)
		{
			 log.severe("exception in loadCampaignTraffickingData LinMobileBusinessService (READY): "+e.getMessage());
			 
		} 
        
		
		/*Getting DRAFTS & NEEDS_CREATIVES lineitems*/
  	  try
	  {
  		List<LineItemDTO> draftAndNeedCreativesCampaignList = MemcacheUtil.getDraftAndNeedCreativesCampaignList();
		  if(draftAndNeedCreativesCampaignList == null || draftAndNeedCreativesCampaignList.size() <= 0)
		  {
           query="WHERE ( status = '"+ComputedStatus.DRAFT+"' " +
			"or status = '"+ComputedStatus.NEEDS_CREATIVES+"'" +
			")" +
			" AND targetPlatform = '"+TargetPlatform.MOBILE+"'"; 
		  log.info("loadCampaignTraffickingData Query 3 (DRAFT & NEEDS_CREATIVES):"+query);
		 lineItemStatement.setQuery(query);
		 
		int j=0;
		do{
	         page=lineItemService.getLineItemsByStatement(lineItemStatement);
	         List<LineItem> result = page.getResults();
	        if(result !=null && result.size()>0)
	        {
	        	draftAndNeedCreativesCampaignList = getLineItemDTOList(result);
	        	log.info("Draft and Need Creatives Campaigns  = "+draftAndNeedCreativesCampaignList.size());
	        }
	        else
	        {
	      	  log.warning("No line items found for query (DRAFT & NEEDS_CREATIVES): "+query);
	        }
	
		}while(page.getResults()== null && j<=3);  
		
		MemcacheUtil.setDraftAndNeedCreativesCampaignList(draftAndNeedCreativesCampaignList);
		}
		  
	  if(draftAndNeedCreativesCampaignList != null && draftAndNeedCreativesCampaignList.size() > 0)
	  {
    		totalLineItemDTOList.addAll(draftAndNeedCreativesCampaignList);
	  }
	  }catch(Exception e)
	  {
				 log.severe("exception in loadCampaignTraffickingData RichMediaAdvertiserService (DRAFT & NEEDS_CREATIVES): "+e.getMessage());
				 
	  } 
      
	  
	  
	  /*Getting PAUSED & PAUSED_INVENTORY_RELEASED lineitems*/
  	  try
	  { 
  		List<LineItemDTO> pausedAndInventoryReleasedCampaignList = MemcacheUtil.getPausedAndInventoryReleasedCampaignList();
		if(pausedAndInventoryReleasedCampaignList == null || pausedAndInventoryReleasedCampaignList.size() <= 0)
		{
		   query="WHERE (" +
			"status = '"+ComputedStatus.PAUSED+"' " +
			"or status = '"+ComputedStatus.PAUSED_INVENTORY_RELEASED+"' " +
			") " +
			"AND targetPlatform = '"+TargetPlatform.MOBILE+"'"; 
		  log.info("loadCampaignTraffickingData Query 4 (PAUSED & PAUSED_INVENTORY_RELEASED): "+query); 
		 lineItemStatement.setQuery(query);
		 int j=0;
		  do{
	          page=lineItemService.getLineItemsByStatement(lineItemStatement);
	          List<LineItem> result = page.getResults();
	          if(result != null && result.size() > 0)
	          {
	        	  pausedAndInventoryReleasedCampaignList = getLineItemDTOList(result);
	        	  log.info("Paused And Inventory Released Campaigns  = "+pausedAndInventoryReleasedCampaignList.size());
	          }
	          else
	          {
	        	  log.warning("No line items found for query 5 (PAUSED & PAUSED_INVENTORY_RELEASED):"+query);
	          }
		  }while(page.getResults()== null && j<=3);
		  
		MemcacheUtil.setPausedAndInventoryReleasedCampaignList(pausedAndInventoryReleasedCampaignList);
		}
		
		if(pausedAndInventoryReleasedCampaignList != null && pausedAndInventoryReleasedCampaignList.size() > 0)
    	{
    		totalLineItemDTOList.addAll(pausedAndInventoryReleasedCampaignList);
    	}
	  }catch(Exception e)
	  {
				 log.severe("exception in loadCampaignTraffickingData LinMobileBusinessService (PAUSED & PAUSED_INVENTORY_RELEASED): "+e.getMessage());
				 
	  }

	  
	  
	  /*Getting PENDING_APPROVAL lineitems*/
    	  try
		  {  
    		  List<LineItemDTO> pendingApprovalCampaignList = MemcacheUtil.getPendingApprovalCampaignList();
    		  if(pendingApprovalCampaignList == null || pendingApprovalCampaignList.size() <= 0)
    		  {
          query="WHERE status = '"+ComputedStatus.PENDING_APPROVAL+"' AND targetPlatform = '"+TargetPlatform.MOBILE+"'"; 

		  log.info("loadCampaignTraffickingData Query 6 (PENDING_APPROVAL): "+query); 
		 lineItemStatement.setQuery(query);
		int j=0;
		do{          
	        page=lineItemService.getLineItemsByStatement(lineItemStatement);
	        List<LineItem> result = page.getResults();
	        if(result !=null && result.size()>0)
	        {
	        	pendingApprovalCampaignList = getLineItemDTOList(result);
	        	log.info("Pending Approval Campaigns  = "+pendingApprovalCampaignList.size());
	        }
	        else
	        {
	      	  log.warning("No line items found for query (PENDING_APPROVAL) :"+query);
	        }
        }while(page.getResults()== null && j<=3);
		MemcacheUtil.setPendingApprovalCampaignList(pendingApprovalCampaignList);
    	}
    		  
    	if(pendingApprovalCampaignList != null && pendingApprovalCampaignList.size() > 0)
    	{
    		totalLineItemDTOList.addAll(pendingApprovalCampaignList);
    	}
		}catch(Exception e)
		{
			 log.severe("exception in loadCampaignTraffickingData LinMobileBusinessService (PENDING_APPROVAL): "+e.getMessage());
			 
		} 

		return totalLineItemDTOList;
	  }
	  
	  private List<LineItemDTO> getLineItemDTOList(List<LineItem> lineItemList)
	  {
		  List<LineItemDTO> list = new ArrayList<LineItemDTO>();
		  try{
	          
	          if(lineItemList!=null && lineItemList.size()>0){

		          Iterator<LineItem> iterator = lineItemList.iterator();
		          while(iterator.hasNext())
		          {
			          LineItem lineItemObj = iterator.next();
			          if(!lineItemObj.isIsArchived()){
			          LineItemDTO lineItem= new LineItemDTO();
			          lineItem.setLineItemId(lineItemObj.getId());
			          lineItem.setStatus(lineItemObj.getStatus().toString());
			          lineItem.setName(lineItemObj.getName());
			          lineItem.setGoalQuantity(lineItemObj.getUnitsBought());
			          
			          Money money = lineItemObj.getCostPerUnit();
			          lineItem.setCpm(money.getMicroAmount() / 1000000);
			          
			          if (lineItemObj.getStartDateTime()!= null){
				          com.google.api.ads.dfp.jaxws.v201403.Date startDateTime = lineItemObj.getStartDateTime().getDate();
				          lineItem.setStartDateTime(startDateTime.getMonth()+"/"+startDateTime.getDay()+"/"+startDateTime.getYear());
			          }
			          else{
			        	  
			        	  lineItem.setStartDateTime(null);
			          }
			          
			          
			          
			          if (lineItemObj.getEndDateTime()!= null){
				          com.google.api.ads.dfp.jaxws.v201403.Date endDateTime = lineItemObj.getEndDateTime().getDate();
				          lineItem.setEndDateTime(endDateTime.getMonth()+"/"+endDateTime.getDay()+"/"+endDateTime.getYear());
			          }
			          else{
			        	  
			        	  lineItem.setEndDateTime(null);
			          }
			          
			          list.add(lineItem);
			          }//end of if
		          }//end of while
		          
	          }
	          
			}catch(Exception e)
			{
					 log.severe("exception in loadCampaignTraffickingData LinMobileBusinessService : "+e.getMessage());
					 
			}
			  
			return list;
	  }
	  
	  
	  public List<ForcastLineItemDTO> getLineItemForcasts(DfpServices dfpServices,DfpSession session,String[] lineItemIds){
	    	log.info("inside getLineItemForcasts of LinMobile Business Service");
	    	List<ForcastLineItemDTO> forcastLineItemDTOList = new ArrayList<ForcastLineItemDTO>();
	    	long deliveredImpressions = 0;
	    	long bookedImpressions = 0;
	    	long impressionsToBeDelivered = 0;
	    	LineItemServiceInterface lineItemService = null;
	    	ForecastServiceInterface forecastService = null;
	    	
	    	try
	    	{
	    		int j=0;
	    		do{
	    			lineItemService = dfpServices.get(session, LineItemServiceInterface.class);
	    		}while(lineItemService == null && j < 3);
	    	}catch(Exception e)
	    	{
	    		
	    	}
	    	try{
	    		int k=0;
	    		do{
	    			forecastService = dfpServices.get(session, ForecastServiceInterface.class);
	    		}while(forecastService == null && k < 3);
	    	}catch(Exception e){
	    		
	    	}
	    	
	    	
	    		for(String id : lineItemIds){
		    		log.info("IDs = "+id);
		    		ForcastLineItemDTO forcastLineItemDTO = new ForcastLineItemDTO();
		    		
		    		LineItem lineItem =null;
		    		try{
		    			
		    		    StatementBuilder statementBuilder = new StatementBuilder()
		    			        .where(" id = :id")		    			       
		    			        .withBindVariableValue("id",Long.valueOf(id)); 
		    		    
			    		LineItemPage lineItemPage = lineItemService.getLineItemsByStatement(statementBuilder.toStatement());
			    		
			    		if(lineItemPage != null && lineItemPage.getResults().size()>0){
			    			List<LineItem> lineItemList=lineItemPage.getResults();
			    			lineItem=lineItemList.get(0);
			    			
				    		if(lineItem.getStats()!=null){
				    			if(lineItem.getStats().getImpressionsDelivered().toString()!=null){
				    				deliveredImpressions = lineItem.getStats().getImpressionsDelivered();
				    			}
				        	 }
				    		if(lineItem.getUnitsBought()!=0){
				    			bookedImpressions = lineItem.getUnitsBought();
				    			forcastLineItemDTO.setBookedImpressions(String.valueOf(lineItem.getUnitsBought()));
				        	  }
				    		
				    		if(deliveredImpressions>=0 && bookedImpressions>=0 ){
				    			impressionsToBeDelivered = bookedImpressions - deliveredImpressions;
				    		}
				    		 if(lineItem.getCostPerUnit().toString()!=null){
				        		  double costPerUnit = ((double) lineItem.getCostPerUnit()
				  						.getMicroAmount() / 1000000);
				        		forcastLineItemDTO.setECPM(String.valueOf(costPerUnit));
				        	 }
				    		 
				    		 if(lineItem.getName()!=null && !lineItem.getName().equals("")){
				    			 forcastLineItemDTO.setLineItem(lineItem.getName());
				    		 }
				    		 
				    		 if(lineItem!=null && lineItem.getStartDateTime().toString()!=null){
				    			 forcastLineItemDTO.setStartDate(getCustomDate(lineItem.getStartDateTime()));
					         }
				    		 
				    		 if(lineItem!=null && lineItem.getEndDateTime().toString()!=null){
				    			 forcastLineItemDTO.setEndDate(getCustomDate(lineItem.getEndDateTime()));
					         }
			            }
			    		else
			    		{
			    			log.info("FOUND NULL from lineItemService.getLineItem(Long.valueOf(id))");
			    		}
		    		}catch(Exception e)
		    		{
		    			log.info("1. UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED Exception found");
		    		}
		    		
		            log.info("Before getting lineitem forcast data");
		            
		            Forecast forecast = null;
		            
		            try{
		    			forecast =  forecastService.getForecastById(Long.valueOf(id));
			    	    
			              if(forecast!=null)
			              {
			            	  if(forecast.getId()!=null)
			            	  {
			            		  forcastLineItemDTO.setLineItemId(forecast.getId());
			            		  log.info(forecast.getId().toString());
			            	  }
			            	  if(forecast.getAvailableUnits()!=null)
			            	  {
			            		  forcastLineItemDTO.setAvailableUnit(forecast.getAvailableUnits());
			            		  log.info("AvailableUnits : "+forecast.getAvailableUnits().toString());
			            	  }
			            	  if(forecast.getDeliveredUnits()!=null)
			            	  {
			            		  forcastLineItemDTO.setDeliveredUnit((forecast.getDeliveredUnits()));
			            		  log.info("DeliveredUnits : "+forecast.getDeliveredUnits().toString());
			            	  }
			            	  if(forecast.getMatchedUnits()!=null)
			            	  {
			            		  forcastLineItemDTO.setMatchedUnit(forecast.getMatchedUnits());
			            		  log.info("MatchedUnits : "+forecast.getMatchedUnits().toString());
			            	  }
			            	  if(forecast.getPossibleUnits()!=null)
			            	  {
			            		  forcastLineItemDTO.setPossibleUnit(forecast.getPossibleUnits());
			            		  log.info("PossibleUnits : "+forecast.getPossibleUnits().toString());
			            	  }
			            	  if(forcastLineItemDTO.getAvailableUnit()!=null && impressionsToBeDelivered>=0 )
			            	  {
			            		  if(forcastLineItemDTO.getAvailableUnit()>=impressionsToBeDelivered)
			            		  {
			            			  forcastLineItemDTO.setStatus(true);
			            		  }
			            	  }
			            	  else
			            	  {
			            		  forcastLineItemDTO.setStatus(false);
			            	  }
			            	  forcastLineItemDTO.setArchived("no"); 
			              }
			              else
			              {
				    			log.info("FOUND NULL from forecastService.getForecastById(Long.valueOf(id))");
			              }
		            }catch(Exception e)
		            {
		            	forcastLineItemDTO.setDeliveredUnit(impressionsToBeDelivered);
		            	forcastLineItemDTO.setArchived("yes");
		            	log.info("2. UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED Exception found");
		            }
			        
			              forcastLineItemDTOList.add(forcastLineItemDTO);
	    		}
	    		
	    		lineItemService = null;
	    		forecastService = null;
	    	
	    	log.info("forcastLineItemDTOList.size() = "+forcastLineItemDTOList.size());
	    	
			return forcastLineItemDTOList;
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
		
		public PublisherPropertiesObj copyObject(PublisherPropertiesObj sourceObj){
			PublisherPropertiesObj destinationObj=new PublisherPropertiesObj(sourceObj.getChannelName(),
					sourceObj.getName(), sourceObj.geteCPM(), sourceObj.getImpressionsDelivered(),
					sourceObj.getClicks(),sourceObj.getTotalImpressionsDeliveredBySiteName(),
					sourceObj.getPayout(), sourceObj.getStateName(), sourceObj.getSite(),
					sourceObj.getDFPPayout());
			destinationObj.setDataSource(sourceObj.getDataSource());
			destinationObj.setTotalImpressionsDeliveredByChannelName(sourceObj.getTotalImpressionsDeliveredByChannelName());
			destinationObj.setLatitude(sourceObj.getLatitude());
			destinationObj.setLongitude(sourceObj.getLongitude());
			destinationObj.setClicks(sourceObj.getClicks());
			return destinationObj;
		}
		
		public Map<String,List<AdvertiserPerformerDTO>> loadCampainTotalDataPublisherViewList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
		{
			Map<String,List<AdvertiserPerformerDTO>> campainTotalDataPublisherMap = new HashMap<String,List<AdvertiserPerformerDTO>>();
			Map<String,AdvertiserPerformerDTO> calculatedLineItemMap = new HashMap<String,AdvertiserPerformerDTO>();
			//Map<String,AdvertiserPerformerDTO> nonDFPObjectMap = new HashMap<String,AdvertiserPerformerDTO>();
			List<AdvertiserPerformerDTO> list = new ArrayList<AdvertiserPerformerDTO>();
			List<AdvertiserPerformerDTO> lineItemCalculatedList = new ArrayList<AdvertiserPerformerDTO>();
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
			
			
			list = MemcacheUtil.getPublisherCampaignTotalDataList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
			if(list == null || list.size() <= 0)
			{
				try
				{
				 LinMobileDAO pubDAO=new LinMobileDAO();
				 list = pubDAO.loadCampainTotalDataPublisherViewList(lowerDate, upperDate, publisherName,advertiser,agency,properties);
				 MemcacheUtil.setPublisherCampaignTotalDataList(lineItemCalculatedList,lowerDate, upperDate, publisherName,advertiser,agency,properties);
				}catch (Exception e) {
					log.severe("DataServiceException :"+e.getMessage());
					
				}			
			}else{
				log.info("Advertiser Total Data found from memcache:");
		    }
			try{
				if(list!=null && list.size()>0){
					campainTotalDataPublisherMap.put("campainTotal",list);
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
						double revenue = 0.0;
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
						 
						revenue = advertiserPerformerDTO.getRevenueDeliverd()+ advertiserObj.getRevenueDeliverd();
						advertiserObj.setImpressionDelivered(impressions);
						advertiserObj.setClicks(clicks);
						advertiserObj.setCTR(ctr);
						advertiserObj.setBookedImpressions(bookedImpressions);
						advertiserObj.setDeliveryIndicator(deliveryIndicator);
						advertiserObj.setRevenueDeliverd(revenue);
						calculatedLineItemMap.put(advertiserObj.getCampaignLineItem(), advertiserObj);
						
					}
				  }
				}
				
				Iterator iterator = calculatedLineItemMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) iterator.next();
					lineItemCalculatedList.add((AdvertiserPerformerDTO) mapEntry.getValue());
			}
				campainTotalDataPublisherMap.put("lineItemCalculated",lineItemCalculatedList);	
				
			}catch(Exception e){
				
			}
			
			return campainTotalDataPublisherMap;
		}
		
		public List<AdvertiserPerformerDTO> loadCampainPerformanceDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
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
			
			//list = MemcacheUtil.getDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
			if(list == null || list.size() <= 0)
			{
				try
				{
					LinMobileDAO pubDAO=new LinMobileDAO();
				list = pubDAO.loadCampainPerformanceDeliveryIndicatorData(lowerDate, upperDate, publisherName, advertiser, agency, properties);
				//MemcacheUtil.setDeliveryIndicatorData(list,lowerDate, upperDate, publisherName,advertiser,agency,properties);
				
				}catch (Exception e) {
					log.severe("DataServiceException :"+e.getMessage());
					
				}			
			}else{
				log.info("Delivery Indicator Data found from memcache:");
		    }
			
			
			return list;
		}
		
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
			destinationObj.setRevenueDeliverd((sourceObj.getRevenueDeliverd()));
			return destinationObj;
		}
		
	@Override
	public PublisherReportHeaderDTO getHeaderData(String allChannelName, List<PublisherSummaryObj> currentDateSummaryList, List<PublisherSummaryObj> compareDateSummaryList) {
		int maxSite = 0;
		double totalECPM = 0.0;
		long totalEmpDelivered = 0;
		long totalClicks = 0;
		double totalCTR = 0.0;
		double totalRPM = 0.0;
		double totalPayouts = 0.0;
		long totalRequests = 0;
		long houseImpression = 0;
		int newFlag = 0;
		double fillPercentage = 0.0;
		long allChannelsTotalEmpDelivered = 0;
		PublisherReportHeaderDTO publisherReportHeaderDTO = new PublisherReportHeaderDTO();
		
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			List<String> channelList = service.CommaSeperatedStringToList(allChannelName);
			
			if(channelList != null && channelList.size() > 0 && currentDateSummaryList != null && compareDateSummaryList != null) {
				List<String> totalEmpDeliveredCheckList = new ArrayList<String>();
				  
			   for (PublisherSummaryObj dtoObject : currentDateSummaryList) {
				   newFlag++;
				   if(channelList.contains(dtoObject.getChannelName().trim())) {
					   if(dtoObject.getChannelName().equals("House")) {
						   houseImpression =  dtoObject.getImpressionsDelivered();
					   }
					   if(dtoObject.getSite() > maxSite) {
						   maxSite = dtoObject.getSite();
					   }
					   totalEmpDelivered = totalEmpDelivered + dtoObject.getImpressionsDelivered();
					   totalClicks = totalClicks + dtoObject.getClicks();
					   totalPayouts = totalPayouts + dtoObject.getPayOuts();
					   totalRequests = totalRequests + dtoObject.getRequests();
				   }
				   if(!totalEmpDeliveredCheckList.contains(dtoObject.getChannelName().trim())){
					   totalEmpDeliveredCheckList.add(dtoObject.getChannelName().trim());
					   allChannelsTotalEmpDelivered = allChannelsTotalEmpDelivered + dtoObject.getImpressionsDelivered();  
				   }
			   }
				
			   totalCTR =  ((double)totalClicks / totalEmpDelivered) * 100;
			   totalECPM = (totalPayouts / totalEmpDelivered) * 1000;
			   
			   if(totalRequests != 0) {
				   totalRPM = (totalPayouts / totalRequests) * 1000;
			   }
			   
			   fillPercentage = ((double)((totalEmpDelivered - houseImpression)* 100) / allChannelsTotalEmpDelivered);
			   
			   if(newFlag > 0) {
				   maxSite = 23;
			   }	
			   
			   publisherReportHeaderDTO.setSite(maxSite);
			   publisherReportHeaderDTO.setImpressionsDelivered(totalEmpDelivered);
			   publisherReportHeaderDTO.setClicks(totalClicks);
			   publisherReportHeaderDTO.setCtrPercentage(totalCTR/100);
			   publisherReportHeaderDTO.setEcpm(totalECPM);
			   publisherReportHeaderDTO.setRpm(totalRPM);
			   publisherReportHeaderDTO.setPayouts(totalPayouts);
			   publisherReportHeaderDTO.setFillPercentage(fillPercentage/100);
					  
			}
		}catch (Exception e) {
			log.severe("Exception in getHeaderData of LinMobileService : "+e.getMessage());
			
		}
		return publisherReportHeaderDTO;
	}
	
	@Override
	public Map getChannelPerformanceData(String allChannelName, List<PublisherSummaryObj> currentDateSummaryList, List<PublisherSummaryObj> compareDateSummaryList) {
		double totalECPMLast = 0.0;
		double totalPayoutsLast = 0.0;
		long totalEmpDeliveredLast = 0;
		double totalECPM = 0.0;
		long totalEmpDelivered = 0;
		long totalClicks = 0;
		double totalCTR = 0.0;
		double totalPayouts = 0.0;
		double totalChange = 0.0;
		double totalChangePercentage = 0.0;
		//int flg = 0;
		Map channelperformanceMap = new HashMap();
		List<PublisherReportChannelPerformanceDTO> pubReportChannelPerformanceDTOList = new ArrayList<PublisherReportChannelPerformanceDTO>();
		List<PublisherReportComputedValuesDTO> publisherReportComputedValuesDTOList = new ArrayList<PublisherReportComputedValuesDTO>();
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			List<String> channelList = service.CommaSeperatedStringToList(allChannelName);
			
			if(channelList != null && channelList.size() > 0 && currentDateSummaryList != null && currentDateSummaryList.size() > 0) {
				
				for(PublisherSummaryObj dtoObjectCurrent : currentDateSummaryList) {
					if(channelList.contains(dtoObjectCurrent.getChannelName().trim())) {
						int matchflag = 0;
						if(compareDateSummaryList.size() > 0) {
							int compareDateSummaryLength = compareDateSummaryList.size();
							int compareCount = 0;
							for (PublisherSummaryObj dtoObjectCompare : compareDateSummaryList) {
								if(channelList.contains(dtoObjectCompare.getChannelName().trim())) {
									PublisherReportChannelPerformanceDTO publisherReportChannelPerformanceDTO = new PublisherReportChannelPerformanceDTO();
									if(dtoObjectCurrent.getChannelName().equals(dtoObjectCompare.getChannelName())) {     
										//flg ++;
										double CHG = 0;
										double percentageCHG = 0.0;
										matchflag = 1;
										CHG = dtoObjectCurrent.geteCPM() - dtoObjectCompare.geteCPM();
										if(dtoObjectCompare.geteCPM() == 0) {
											percentageCHG = 0.0;
										}
										else {
											percentageCHG = (CHG / dtoObjectCompare.geteCPM()) * 100;
										}
										
										publisherReportChannelPerformanceDTO.setSalesChannel(dtoObjectCurrent.getChannelName());
										publisherReportChannelPerformanceDTO.setEcpm(dtoObjectCurrent.geteCPM());
										publisherReportChannelPerformanceDTO.setChange(CHG);
										publisherReportChannelPerformanceDTO.setChangePercentage(percentageCHG/100);
										publisherReportChannelPerformanceDTO.setImpressionsDelivered(dtoObjectCurrent.getImpressionsDelivered());
										publisherReportChannelPerformanceDTO.setClicks(dtoObjectCurrent.getClicks());
										publisherReportChannelPerformanceDTO.setCtrPercentage(dtoObjectCurrent.getCTR()/100);
										publisherReportChannelPerformanceDTO.setPayouts(dtoObjectCurrent.getPayOuts());
										
										totalPayoutsLast = totalPayoutsLast + dtoObjectCompare.getPayOuts();
										totalEmpDeliveredLast = totalEmpDeliveredLast + dtoObjectCompare.getImpressionsDelivered();
										totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.getImpressionsDelivered();
										totalClicks = totalClicks + dtoObjectCurrent.getClicks();
										totalPayouts = totalPayouts + dtoObjectCurrent.getPayOuts();
										
										pubReportChannelPerformanceDTOList.add(publisherReportChannelPerformanceDTO);
									
									}else if(compareDateSummaryLength-1 == compareCount && matchflag == 0) {
										//flg ++;
										double CHG = 0;
										double percentageCHG = 0.0;
										matchflag = 1;
										CHG = dtoObjectCurrent.geteCPM();
										percentageCHG = 0.0;
										
										publisherReportChannelPerformanceDTO.setSalesChannel(dtoObjectCurrent.getChannelName());
										publisherReportChannelPerformanceDTO.setEcpm(dtoObjectCurrent.geteCPM());
										publisherReportChannelPerformanceDTO.setChange(CHG);
										publisherReportChannelPerformanceDTO.setChangePercentage(percentageCHG/100);
										publisherReportChannelPerformanceDTO.setImpressionsDelivered(dtoObjectCurrent.getImpressionsDelivered());
										publisherReportChannelPerformanceDTO.setClicks(dtoObjectCurrent.getClicks());
										publisherReportChannelPerformanceDTO.setCtrPercentage(dtoObjectCurrent.getCTR()/100);
										publisherReportChannelPerformanceDTO.setPayouts(dtoObjectCurrent.getPayOuts());
										
										totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.getImpressionsDelivered();
										totalClicks = totalClicks + dtoObjectCurrent.getClicks();
										totalPayouts = totalPayouts + dtoObjectCurrent.getPayOuts();
										
										pubReportChannelPerformanceDTOList.add(publisherReportChannelPerformanceDTO);
										
									}
									compareCount++;
								}
							}
						}else {
							PublisherReportChannelPerformanceDTO publisherReportChannelPerformanceDTO = new PublisherReportChannelPerformanceDTO();
							//flg ++;
							double CHG = 0;
							double percentageCHG = 0.0;
							matchflag = 1;
							CHG = dtoObjectCurrent.geteCPM();
							percentageCHG = 0.0;
							  
							publisherReportChannelPerformanceDTO.setSalesChannel(dtoObjectCurrent.getChannelName());
							publisherReportChannelPerformanceDTO.setEcpm(dtoObjectCurrent.geteCPM());
							publisherReportChannelPerformanceDTO.setChange(CHG);
							publisherReportChannelPerformanceDTO.setChangePercentage(percentageCHG/100);
							publisherReportChannelPerformanceDTO.setImpressionsDelivered(dtoObjectCurrent.getImpressionsDelivered());
							publisherReportChannelPerformanceDTO.setClicks(dtoObjectCurrent.getClicks());
							publisherReportChannelPerformanceDTO.setCtrPercentage(dtoObjectCurrent.getCTR()/100);
							publisherReportChannelPerformanceDTO.setPayouts(dtoObjectCurrent.getPayOuts());
							
							pubReportChannelPerformanceDTOList.add(publisherReportChannelPerformanceDTO);
			 	 		        		
							totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.getImpressionsDelivered();
							totalClicks = totalClicks + dtoObjectCurrent.getClicks();
							totalPayouts = totalPayouts + dtoObjectCurrent.getPayOuts();				  
						}
					}
				}
	
				totalCTR =  (totalClicks / totalEmpDelivered) * 100;
				totalECPM = (totalPayouts / totalEmpDelivered) * 1000;
				totalECPMLast = (totalPayoutsLast / totalEmpDeliveredLast) * 1000;
				totalChange = totalECPM - totalECPMLast;
				totalChangePercentage = (totalChange / totalECPMLast) * 100;
				PublisherReportComputedValuesDTO publisherReportComputedValuesDTO = new PublisherReportComputedValuesDTO();
				publisherReportComputedValuesDTO.setChange(totalChange);
				publisherReportComputedValuesDTO.setChangePercentage(totalChangePercentage/100);
				publisherReportComputedValuesDTOList.add(publisherReportComputedValuesDTO);
				
				for(PublisherReportChannelPerformanceDTO publisherReportChannelPerformanceDTO : pubReportChannelPerformanceDTOList) {
					publisherReportChannelPerformanceDTO.setFillRate((double)publisherReportChannelPerformanceDTO.getImpressionsDelivered()/totalEmpDelivered);
				}
			}
		}catch (Exception e) {
			log.severe("Exception in getChannelPerformanceDate of LinMobileService : "+e.getMessage());
			
		}
		channelperformanceMap.put("PublisherReportChannelPerformanceDTO", pubReportChannelPerformanceDTOList);
		channelperformanceMap.put("PublisherReportComputedValuesDTO", publisherReportComputedValuesDTOList);
		return channelperformanceMap;
	}
	
	public Map getPerformanceByPropertyData(String channelName, List<PublisherPropertiesObj> performanceByPropertyCurrentDataBySiteName, List<PublisherPropertiesObj> performanceByPropertyCompareDataBySiteName) {
			double payoutsTotalCompare = 0.0;
			double impDeliveredTotalCompare = 0.0;
			double clicksTotalCurrent = 0;
			double payoutsTotalCurrent = 0.0;
			double impDeliveredTotalCurrent = 0.0;
			double eCPMTotalCompare = 0.0;
			double eCPMTotalCurrent = 0.0;
			double changeTotal = 0.0;
			double percentageCHGTotal = 0.0;
			double imprs =0.0;
			long payout = 0;
			int flg = 0;
			
			Map performanceByPropertyMap = new HashMap();
			List<PublisherReportPerformanceByPropertyDTO> publisherReportPerformanceByPropertyDTOList = new ArrayList<PublisherReportPerformanceByPropertyDTO>();
			List<PublisherReportComputedValuesDTO> publisherReportComputedValuesDTOList = new ArrayList<PublisherReportComputedValuesDTO>();
	
		try {
			if(performanceByPropertyCurrentDataBySiteName != null && performanceByPropertyCurrentDataBySiteName.size() > 0) {
				for(PublisherPropertiesObj objectCurrent : performanceByPropertyCurrentDataBySiteName) {
					int matchflag = 0;
					if(performanceByPropertyCompareDataBySiteName != null && performanceByPropertyCompareDataBySiteName.size() > 0) {
						int compareCount = 0;
						for(PublisherPropertiesObj objectCompare : performanceByPropertyCompareDataBySiteName) {
							PublisherReportPerformanceByPropertyDTO publisherReportPerformanceByPropertyDTO = new PublisherReportPerformanceByPropertyDTO();
							if(objectCurrent.getSite() != null && objectCompare.getSite() != null && objectCurrent.getSite().equals(objectCompare.getSite()) && objectCurrent.getChannelName().equals(objectCompare.getChannelName())) {	
								imprs = 0;
								long clks = objectCurrent.getClicks();
								imprs = objectCurrent.getImpressionsDelivered();
								double ctr = (clks / imprs) * 100;
			
								flg++;
								matchflag = 1;	
								double eCPM = objectCurrent.geteCPM();
								double CHG = 0.0;
								double percentageCHG = 0.0;
								if(objectCompare.geteCPM() == 0.0) {
									CHG = objectCurrent.geteCPM();
									percentageCHG = 0.0;
								}
								else {
									CHG = objectCurrent.geteCPM() - objectCompare.geteCPM();
									percentageCHG = (CHG / objectCompare.geteCPM()) * 100;
								}
								
								publisherReportPerformanceByPropertyDTO.setProperty(objectCurrent.getName());
								publisherReportPerformanceByPropertyDTO.setEcpm(objectCurrent.geteCPM());
								publisherReportPerformanceByPropertyDTO.setChange(CHG);
								publisherReportPerformanceByPropertyDTO.setChangePercentage(percentageCHG/100);
								publisherReportPerformanceByPropertyDTO.setImpressionsDelivered(Math.round(objectCurrent.getImpressionsDelivered()));
								publisherReportPerformanceByPropertyDTO.setClicks(objectCurrent.getClicks());
								publisherReportPerformanceByPropertyDTO.setPayouts(objectCurrent.getPayout());
								
								publisherReportPerformanceByPropertyDTOList.add(publisherReportPerformanceByPropertyDTO);
			
								payoutsTotalCompare = payoutsTotalCompare + objectCompare.getPayout();
								impDeliveredTotalCompare = impDeliveredTotalCompare + objectCompare.getImpressionsDelivered();
								clicksTotalCurrent = clicksTotalCurrent + objectCurrent.getClicks();
								payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.getPayout();
								impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.getImpressionsDelivered());
			
							}
							else if(performanceByPropertyCompareDataBySiteName.size()-1 == compareCount && matchflag == 0 ) {
								imprs = 0;
								long clks = objectCurrent.getClicks();
								imprs = objectCurrent.getImpressionsDelivered();
								double ctr = (clks / imprs) * 100;
								
								flg++;
								matchflag = 1;	
								double eCPM = objectCurrent.geteCPM();
								double CHG = 0.0;
								double percentageCHG = 0.0;
								CHG = objectCurrent.geteCPM();
								percentageCHG = 0.0;
								
								publisherReportPerformanceByPropertyDTO.setProperty(objectCurrent.getName());
								publisherReportPerformanceByPropertyDTO.setEcpm(objectCurrent.geteCPM());
								publisherReportPerformanceByPropertyDTO.setChange(CHG);
								publisherReportPerformanceByPropertyDTO.setChangePercentage(percentageCHG/100);
								publisherReportPerformanceByPropertyDTO.setImpressionsDelivered(Math.round(objectCurrent.getImpressionsDelivered()));
								publisherReportPerformanceByPropertyDTO.setClicks(objectCurrent.getClicks());
								publisherReportPerformanceByPropertyDTO.setPayouts(objectCurrent.getPayout());
								
								publisherReportPerformanceByPropertyDTOList.add(publisherReportPerformanceByPropertyDTO);
					
								clicksTotalCurrent = clicksTotalCurrent + objectCurrent.getClicks();
							    payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.getPayout();
							    impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.getImpressionsDelivered());	
							}
							compareCount++;
						}
					}
					else {
						PublisherReportPerformanceByPropertyDTO publisherReportPerformanceByPropertyDTO = new PublisherReportPerformanceByPropertyDTO();
						imprs = 0;
						long clks = objectCurrent.getClicks();
						imprs = objectCurrent.getImpressionsDelivered();
						double ctr = (clks / imprs) * 100;
						
						flg++;
						matchflag = 1;	
						double eCPM = objectCurrent.geteCPM();
						double CHG = 0.0;
						double percentageCHG = 0.0;
						CHG = objectCurrent.geteCPM();
						percentageCHG = 0.0;
						
						publisherReportPerformanceByPropertyDTO.setProperty(objectCurrent.getName());
						publisherReportPerformanceByPropertyDTO.setEcpm(objectCurrent.geteCPM());
						publisherReportPerformanceByPropertyDTO.setChange(CHG);
						publisherReportPerformanceByPropertyDTO.setChangePercentage(percentageCHG/100);
						publisherReportPerformanceByPropertyDTO.setImpressionsDelivered(Math.round(objectCurrent.getImpressionsDelivered()));
						publisherReportPerformanceByPropertyDTO.setClicks(objectCurrent.getClicks());
						publisherReportPerformanceByPropertyDTO.setPayouts(objectCurrent.getPayout());
						
						publisherReportPerformanceByPropertyDTOList.add(publisherReportPerformanceByPropertyDTO);
		
			       	  	clicksTotalCurrent = clicksTotalCurrent + objectCurrent.getClicks();
			       	  	payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.getPayout();
			       	  	impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.getImpressionsDelivered());	
					}
				}	
							
				if(flg > 0) {
					eCPMTotalCurrent = (payoutsTotalCurrent / impDeliveredTotalCurrent) * 1000;
					eCPMTotalCompare = (payoutsTotalCompare / impDeliveredTotalCompare) * 1000;
					changeTotal = (eCPMTotalCurrent - eCPMTotalCompare);
					percentageCHGTotal = (changeTotal / eCPMTotalCompare) * 100;
				}
				PublisherReportComputedValuesDTO publisherReportComputedValuesDTO = new PublisherReportComputedValuesDTO();
				publisherReportComputedValuesDTO.setChange(changeTotal);
				publisherReportComputedValuesDTO.setChangePercentage(percentageCHGTotal/100);
				publisherReportComputedValuesDTOList.add(publisherReportComputedValuesDTO);
			}
		}catch (Exception e) {
			log.severe("Exception in getPerformanceByPropertyData of LinMobileService : "+e.getMessage());
			
		}	
		performanceByPropertyMap.put("PublisherReportPerformanceByPropertyDTO", publisherReportPerformanceByPropertyDTOList);
		performanceByPropertyMap.put("PublisherReportPerformanceComputedValuesDTO", publisherReportComputedValuesDTOList);
		return performanceByPropertyMap;
	}
	
	/*
	 * This method will create json string for line chart
	 */
	@SuppressWarnings("unused")
	private String createJsonResponseForLineChart(Map<String, Object> dateMap,
			Map<String, Object> channelMap,Map<String, Object> perfDataMap,String chartType) throws TypeMismatchException{
		
		DataTable linechartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription("date", ValueType.DATE, "Date");
	    //ColumnDescription col0 = new ColumnDescription("date", ValueType.TEXT, "Date");
	    linechartTable.addColumn(col0);

	    for (Entry<String, Object> channel : channelMap.entrySet()) {
			  String channelName = channel.getKey();
			  ColumnDescription channelcol = new ColumnDescription(channelName, ValueType.NUMBER, channelName);
			  linechartTable.addColumn(channelcol);
		}
	    
	    rows = Lists.newArrayList();	
	    int year, month, day = 0;	
	    for (Entry<String, Object> dates : dateMap.entrySet()) {
			  String date = dates.getKey();
			  String [] dtArray=date.split("-");
			  com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
			  
			  year = Integer.parseInt(dtArray[0]);
			  month = Integer.parseInt(dtArray[1])-1;
			  day = Integer.parseInt(dtArray[2]);
			  DateValue dateValue= new DateValue(year,month,day);
			  
			  row.addCell(new com.google.visualization.datasource.datatable.TableCell(dateValue));
			  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(date));
			  
			  for (Entry<String, Object> channel : channelMap.entrySet()) {
				  String channelName = channel.getKey();
				  String key=date+"_"+channelName;					 
				  LineChartDTO perData = (LineChartDTO) perfDataMap.get(key);					 
				  if ( perData  != null){
					  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(perData.getCtr()).);						  
					  if(chartType.equalsIgnoreCase("CTR")){
						  Double ctr=StringUtil.getDoubleValue(perData.getCtr(), 4);
						  row.addCell(new NumberValue(new Double(ctr)));  
					  }else if(chartType.equalsIgnoreCase("Clicks")){
						  row.addCell(new NumberValue(new Long(perData.getClicks()))); 
					  }else if(chartType.equalsIgnoreCase("Impressions")){
						  row.addCell(new NumberValue(new Long(perData.getImpressions()))); 
					  }else if(chartType.equalsIgnoreCase("Revenue")){
						  Double revenue = StringUtil.getDoubleValue(perData.getRevenue(), 4);
						  row.addCell(new NumberValue(new Double(revenue))); 
					  }else if(chartType.equalsIgnoreCase("Ecpm")){
						  Double ecpm=StringUtil.getDoubleValue(perData.geteCPM(), 4);
						  row.addCell(new NumberValue(new Double(ecpm)));  
					  }else if(chartType.equalsIgnoreCase("FillRate")){
						  Double fillRate=StringUtil.getDoubleValue(perData.getFillRate(), 4);
						  row.addCell(new NumberValue(new Double(fillRate)));  
					  }
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  }
				  
			  }
			  rows.add(row);				  
	  }
	  linechartTable.addRows(rows);
	  
	  java.lang.CharSequence jsonStr =	 JsonRenderer.renderDataTable(linechartTable, true, false);
	  //System.out.println("LineChart Table JSON Data::" + jsonStr.toString());
	  return jsonStr.toString();
	}
	
	public Map<String,String> processPublisherLineChartData(String lowerDate,String upperDate,String publisherName,String channelName){
		List<PublisherChannelObj> actualPublisherList=null;
		Map<String, Object> dateMap = new LinkedHashMap<String, Object>();
		Map<String, Object> perfDataMap = new LinkedHashMap<String, Object>();
		Map<String, Object> channelMap = new LinkedHashMap<String, Object>();
		ILinMobileDAO linDAO=new LinMobileDAO();
		Map<String,String> jsonChartMap = null;
		try {
			String channelId = getChannelsBQId(channelName);
			String publisherId = getPublisherBQId(publisherName);
			jsonChartMap = MemcacheUtil.getLineChartMapFromCachePublisher(lowerDate, upperDate, publisherId, channelId);
			if(jsonChartMap ==null || jsonChartMap.size()==0){
				jsonChartMap = new LinkedHashMap<String,String>();
				 actualPublisherList = MemcacheUtil.getTrendsAnalysisActualDataPublisher(lowerDate, upperDate, publisherName, channelName);
				   if(actualPublisherList==null || actualPublisherList.size()<=0){
					   String replaceStr = "\\\\'";
						
						if(channelName!=null){
							channelName = channelName.replaceAll("'", replaceStr);
							channelName = channelName.replaceAll(",", "','");
						}else{
							channelName = "";
						}
						QueryDTO queryDTO = getQueryDTO(publisherId, lowerDate, upperDate, LinMobileConstants.BQ_CORE_PERFORMANCE);
						if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
							actualPublisherList=linDAO.loadActualDataForPublisher(lowerDate,upperDate,channelId, queryDTO);
							MemcacheUtil.setTrendsAnalysisActualDataPublisher(lowerDate, upperDate, publisherId, channelId, actualPublisherList);
						}
						
					   for (PublisherChannelObj publisherChannelObj : actualPublisherList) {
						String date = publisherChannelObj.getDate();
						String channelNamekey = publisherChannelObj.getChannelName();
						String revenue = publisherChannelObj.getRevenue()+"";
						String impressions = publisherChannelObj.getImpressionsDelivered()+"";
						String clicks = publisherChannelObj.getClicks()+"";
						String ctr = publisherChannelObj.getCTR()+"";
						String fillRate = publisherChannelObj.getFillRate()+"";
						String ecpm = publisherChannelObj.geteCPM()+"";
						LineChartDTO lineChartDTO = new LineChartDTO(impressions, clicks, ctr, date, fillRate, ecpm, revenue);
						dateMap.put(date, null);
						channelMap.put(channelNamekey, null);
						String key=date+"_"+channelNamekey;
						perfDataMap.put(key, lineChartDTO);
					}
					    String ctrLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "CTR");
						jsonChartMap.put("CTR", ctrLineChartJson);
						String clicksLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "Clicks");
						jsonChartMap.put("Clicks", clicksLineChartJson);
						String impressionsLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "Impressions");
						jsonChartMap.put("Impressions", impressionsLineChartJson);
						String revenueLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "Revenue");
						jsonChartMap.put("Revenue", revenueLineChartJson);
						String ecpmLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "Ecpm");
						jsonChartMap.put("Ecpm", ecpmLineChartJson);
						String fillRateLineChartJson=createJsonResponseForLineChart(dateMap, channelMap, perfDataMap, "FillRate");
						jsonChartMap.put("FillRate", fillRateLineChartJson);
					    MemcacheUtil.setLineChartMapInCachePublisher(jsonChartMap, lowerDate, upperDate, publisherId, channelId);
				}
			}else{
				log.info("Found line chart data in memcache..size:"+jsonChartMap.size());
	        }

	}catch (Exception e) {
		log.severe("Exception :"+e.getMessage());
		
		return null;
	}
	  return jsonChartMap;
	}
  }



