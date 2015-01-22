package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.ILinMobileDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserByMarketObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
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
import com.lin.web.dto.LeftMenuDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.dto.PublisherReallocationHeaderDTO;
import com.lin.web.dto.PublisherTrendAnalysisHeaderDTO;
import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.ReconciliationDataDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class LinMobileDAO implements ILinMobileDAO {

	private static final Logger log = Logger.getLogger(LinMobileDAO.class
			.getName());
	private Objectify obfy = OfyService.ofy();	

	public void saveObject(Object obj) throws DataServiceException {
		obfy.save().entity(obj);
	}

	public void deleteObject(Object obj) throws DataServiceException {

		obfy.delete().entity(obj);
		log.info("Object deleted successfully from datastore.");
	}

	public List<AdvertiserReportObj> loadPerformingLineItemsForAdvertiser(
			int size, String lowerDate, String upperDate)
			throws DataServiceException {
		log.info("loadPerformingLineItemsForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<AdvertiserReportObj> advertiserReportList = null;
		advertiserReportList = obfy.load().type(AdvertiserReportObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.order("-date")
				/*
				 * .order("-adServerCTR") .limit(size)
				 */
				.list();
		return advertiserReportList;
	}

	public List<AdvertiserReportObj> loadNonPerformingLineItemsForAdvertiser(
			int size, String lowerDate, String upperDate)
			throws DataServiceException {
		log.info("loadNonPerformingLineItemsForAdvertiser :lowerDate:"
				+ lowerDate + " and upperDate:" + upperDate);
		List<AdvertiserReportObj> advertiserReportList = null;
		double deliveryLimit = 100.00;
		advertiserReportList = obfy.load().type(AdvertiserReportObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				/*
				 * .filter("deliveryIndicator < ",deliveryLimit)
				 * .order("-deliveryIndicator") .limit(size)
				 */
				.list();
		return advertiserReportList;
	}

	public List<PublisherViewDTO> getPublisherView(int page)
			throws DataServiceException {
		int from = (page * 5) - 4;
		List<PublisherViewDTO> subList = obfy.load().type(PublisherViewDTO.class)
				.offset(from).limit(5).list();
		return subList;
	}

	public List<PublisherViewDTO> getPublisherViewDTO()
			throws DataServiceException {
		List<PublisherViewDTO> publisherViewList = obfy.load().type(
				PublisherViewDTO.class).list();
		return publisherViewList;
	}

	public List<LeftMenuDTO> getLeftMenuList() throws DataServiceException {
		List<LeftMenuDTO> leftMenuList = obfy.load().type(LeftMenuDTO.class).list();
		return leftMenuList;
	}

	public List<AgencyAdvertiserObj> loadAllAgencies()
			throws DataServiceException {
		log.info("DAO: load agencies..");
		List<AgencyAdvertiserObj> advertiserReportList = null;
		advertiserReportList = obfy.load().type(AgencyAdvertiserObj.class)
				.order("agencyName").list();
		if (advertiserReportList != null) {
			log.info("advertiserReportList:" + advertiserReportList.size());
		} else {
			log.info("advertiserReportList:Null");
		}
		return advertiserReportList;
	}

	public List<AgencyAdvertiserObj> loadAllAdvertisers(String agencyName)
			throws DataServiceException {
		log.info("DAO: load advertisers..");
		List<AgencyAdvertiserObj> advertiserReportList = null;
		if (agencyName != null) {
			advertiserReportList = obfy.load().type(AgencyAdvertiserObj.class)
					.filter("agencyName = ", agencyName)
					.order("advertiserName").list();
		} else {
			advertiserReportList = obfy.load().type(AgencyAdvertiserObj.class)
					.order("advertiserName").list();
		}
		if (advertiserReportList != null) {
			log.info("advertiserReportList:" + advertiserReportList.size());
		} else {
			log.info("advertiserReportList:Null");
		}
		return advertiserReportList;
	}

	public List<OrderLineItemObj> loadAllOrders() throws DataServiceException {
		List<OrderLineItemObj> resultList = null;
		resultList = obfy.load().type(OrderLineItemObj.class).order("orderName")
				.list();
		if (resultList != null) {
			log.info("resultList:" + resultList.size());
		} else {
			log.info("resultList :Null");
		}
		return resultList;
	}

	public List<OrderLineItemObj> loadAllLineItems(String orderName)
			throws DataServiceException {
		log.info("DAO: load advertisers..");
		List<OrderLineItemObj> resultList = null;
		if (orderName != null) {
			resultList = obfy.load().type(OrderLineItemObj.class)
					.filter("orderName = ", orderName).order("lineItemName")
					.list();
		} else {
			resultList = obfy.load().type(OrderLineItemObj.class)
					.order("lineItemName").list();
		}
		if (resultList != null) {
			log.info("resultList:" + resultList.size());
		} else {
			log.info("resultList :Null");
		}
		return resultList;
	}

	public List<OrderLineItemObj> loadAllLineItems(long orderId)
			throws DataServiceException {
		log.info("DAO: load All line items.................." + orderId);
		List<OrderLineItemObj> resultList = null;
		if (orderId > 0) {
			resultList = obfy.load().type(OrderLineItemObj.class)
					.filter("orderId = ", orderId).order("lineItemName").list();
		} else {
			resultList = obfy.load().type(OrderLineItemObj.class)
					.order("lineItemName").list();
		}
		if (resultList != null) {
			log.info("resultList:" + resultList.size());
		} else {
			log.info("resultList :Null");
		}
		return resultList;
	}

	public List<CustomLineItemObj> loadMostActiveLineItem(int size,
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadPerformingLineItemsForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<CustomLineItemObj> resultList = null;
		resultList = obfy.load().type(CustomLineItemObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				/* .limit(size) */
				.list();
		return resultList;
	}

	public List<AdvertiserByLocationObj> loadAdvertisersByLocationData(
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadAdvertisersByLocationData :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<AdvertiserByLocationObj> AdvertiserByLocationList = null;
		AdvertiserByLocationList = obfy.load().type(AdvertiserByLocationObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				/* .order("-date") */
				.list();
		return AdvertiserByLocationList;
	}

	public List<AdvertiserByMarketObj> loadAdvertisersBymarketData(
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadAdvertisersByLocationData :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<AdvertiserByMarketObj> advertiserBymarketList = null;
		advertiserBymarketList = obfy.load().type(AdvertiserByMarketObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				/* .order("-date") */
				.list();
		return advertiserBymarketList;
	}

	@Override
	public List<PerformanceMetricsObj> loadAdvertiserPerformanceMetrics(
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadAdvertiserPerformanceMetrics(String,String) :lowerDate:"
				+ lowerDate + " and upperDate:" + upperDate);
		List<PerformanceMetricsObj> performanceMetricsObjList = null;
		performanceMetricsObjList = obfy.load().type(PerformanceMetricsObj.class)
		// .filter("date >= ",lowerDate)
		// .filter("date <= ",upperDate)
				.list();
		return performanceMetricsObjList;
	}

	@Override
	public List<PerformanceMetricsObj> loadAdvertiserPerformanceMetrics(
			int counter, String lowerDate, String upperDate)
			throws DataServiceException {
		log.info("loadAdvertiserPerformanceMetrics(int,String,String) :lowerDate:"
				+ lowerDate + " and upperDate:" + upperDate);
		List<PerformanceMetricsObj> performanceMetricsObjList = null;
		int from = (counter * 10) - 9;
		performanceMetricsObjList = obfy.load().type(PerformanceMetricsObj.class)
		// .filter("date >= ",lowerDate)
		// .filter("date <= ",upperDate)
				.offset(from).limit(10).list();
		return performanceMetricsObjList;
	}

	public List<ReallocationDataObj> loadReallocationItemsForAdvertiser(
			int size, String lowerDate, String upperDate)
			throws DataServiceException {
		log.info("loadPerformingLineItemsForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<ReallocationDataObj> reallocationReportList = null;
		reallocationReportList = obfy.load().type(ReallocationDataObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.order("-date")
				/*
				 * .order("-adServerCTR") .limit(size)
				 */
				.list();
		return reallocationReportList;
	}
	
	public List<AgencyAdvertiserObj> loadAdvertiserById(long advertiserId)
			throws DataServiceException {
		log.info("DAO: loadAdvertiserById :advertiserId :" + advertiserId);
		List<AgencyAdvertiserObj> resultList = obfy.load().type(AgencyAdvertiserObj.class)
				.filter("advertiserId = ", advertiserId).list();
		return resultList;
	}

	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(
			int limit, String lowerDate, String upperDate) {
		log.info("loadPerformanceByPropertyList(int,String,String) :lowerDate:"
				+ lowerDate + " and upperDate:" + upperDate);
		List<PublisherPropertiesObj> performanceByPropertyObjList = null;
		// int from = (counter*10)-9;
		performanceByPropertyObjList = obfy.load().type(PublisherPropertiesObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				// .offset(from)
				.limit(limit).list();
		return performanceByPropertyObjList;
	}

	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(
			String lowerDate, String upperDate) {

		List<PublisherPropertiesObj> performanceByPropertyObjList = new ArrayList<PublisherPropertiesObj>();
		PublisherPropertiesObj publisherPropertiesObj;
		String QUERY = "Select a.site_name ,ifnull(ECPM,0) ECPM,ifnull((a.ECPM-b.Last_ECPM),0) Change,ifnull((((a.ECPM-b.Last_ECPM)*100)/ECPM),0) Change_in_Percentage,ifnull(a.Impressions_Delivered,0) as Impressions_Delivered, ifnull(a.Clicks,0) as Clicks, ifnull(a.Payout,0) as Payout From (Select site_name , (sum(total_revenue)/sum(total_impressions))*1000 as ECPM,sum(served) as Impressions_Delivered,sum(total_clicks) as clicks, sum(total_revenue) as Payout From ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"
				+ lowerDate
				+ " 00:00:00' and date <='"
				+ upperDate
				+ " 00:00:00' and channel_name = 'Mojiva' Group by site_name)a JOIN EACH (Select site_name , (sum(total_revenue)/sum(total_impressions))*1000 as Last_ECPM From ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] Where date >='"
				+ lowerDate
				+ " 00:00:00' and date <='"
				+ upperDate
				+ " 00:00:00' and channel_name = 'Mojiva' Group by site_name)b on a.site_name = b.site_name";
		
		log.info("Query loadPerformanceByPropertyList =  "+ QUERY);
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			for (TableRow row : rowList) {
				List<TableCell> cellList = row.getF();
				publisherPropertiesObj = new PublisherPropertiesObj(cellList
						.get(1).getV().toString(), cellList.get(0).getV()
						.toString(), new Double(cellList.get(1).getV()
						.toString()), new Double(cellList.get(2).getV()
						.toString()), new Double(cellList.get(3).getV()
						.toString()), new Long(cellList.get(4).getV()
						.toString()), new Long(cellList.get(5).getV()
						.toString()), new Double(cellList.get(6).getV()
						.toString()), null);

				performanceByPropertyObjList.add(publisherPropertiesObj);

			}

		}
		return performanceByPropertyObjList;

	}

	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(
			String lowerDate, String upperDate, String compareStartDate,
			String compareEndDate, String channel, String publisher, String channelAndDataSourceQuery)  throws Exception{
	
		List<PublisherPropertiesObj> performanceByPropertyObjList = new ArrayList<PublisherPropertiesObj>();
		PublisherPropertiesObj publisherPropertiesObj;
	     
		String QUERY = "Select"
+" case when REGEXP_REPLACE(ifnull(g.station_name,''),'null','')='' then f.site_name when g.station_name = 'NA' then 'US'"
+" else g.station_name end site_name,"
+" ifnull(f.ecpm,0.0) as ecpm,"
+" ifnull(f.change,0.0) as change,"   
+" ifnull(f.change_in_percentage,0.0)  as change_in_percentage,"
+" ifnull(f.impressions_delivered,0) as impressions_delivered,"  
+" ifnull(f.clicks,0) as clicks,"
+" ifnull(f.payout,0.0) as payout,"
+" ifnull(g.dfp_property_name,'') as dfp_property_name,"
+" ifnull(g.state_name,'') as state_name,"
+" ifnull(g.station_name,'') as station_name,"
+" ifnull(f.Last_Revenue,0.0) as Last_Revenue, "
+" ifnull(f.Last_Impression,0) as Last_Impression "
+" From ("
+"                 select d.site_name as site_name ,ifnull(d.ECPM,0) as ECPM  ,ifnull((d.ECPM-e.Last_ECPM),0) as Change ,"
+"                 ifnull((((d.ECPM-e.Last_ECPM)*100)/e.Last_ECPM),0) as Change_in_Percentage,"
+"                 ifnull(d.ImpressionsDelivered,0) as Impressions_Delivered, ifnull(d.Clicks,0) as Clicks, ifnull(d.Payout,0) as Payout, "
+" 				   ifnull(e.Last_Revenue,0.0) as Last_Revenue, ifnull(e.Last_Impression,0) as Last_Impression "
+"                 from"
+"                 ("
+"                 SELECT"
+"                 a.site_name ,"
+"                 ifnull((sum(a.total_revenue)/sum(a.total_impressions))*1000,0) as ECPM,"
+"                 ifnull(sum(a.total_impressions),0) as ImpressionsDelivered,"
+"                 ifnull(sum(a.total_clicks),0) as clicks,"
+"                 ifnull(sum(a.total_revenue),0) as Payout"
+"                 FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+"                 a"
+"                 join each"
+"                 ("
+"                 select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
+"                 from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+"                 where Publisher_name = '"+publisher+"'"; 
		QUERY = QUERY + channelAndDataSourceQuery;
                   QUERY = QUERY+" and channel_name = '"+channel+"'"
+"                 and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
+" 				   where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
+" 				   and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
+"                 group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
+"                 ignore case ) b"
+"                 on a.date = b.date"
+"                 and a.Publisher_name = b.Publisher_name"
+"                 and a.Channel_name = b.Channel_name"
+"                 and a.site_name = b.site_name"
+"                 and a.site_type = b.site_type"
+"                 and a.zone = b.zone"
+"                 and a.advertiser = b.advertiser"
+"                 and a.line_item_id = b.line_item_id"
+"                 and a.creative_id = b.creative_id"
+"                 and a.load_timestamp = b.load_timestamp"
+"                 group each by a.site_name"
+"                 ignore case )d"
+"                 left join"
+"                 ("
+"                 SELECT a.site_name, ifnull(sum(a.total_revenue),0.0) as Last_Revenue, ifnull(sum(a.total_impressions),0) as Last_Impression ,ifnull((sum(a.total_revenue)/sum(a.total_impressions))*1000,0) as Last_ECPM"
+"                 FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a"
+"                 join each"
+"                 ("
+"                 select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
+"                 from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+"                 where Publisher_name = '"+publisher+"'"; 
                   QUERY = QUERY + channelAndDataSourceQuery;
                   QUERY = QUERY+" and channel_name = '"+channel+"'"
+"                 and date >='"+compareStartDate+" 00:00:00' and date <='"+compareEndDate+" 00:00:00' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
+" 					where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
+" 					and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
+"                 group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
+"                 ignore case ) b"
+"                 on a.date = b.date"
+"                 and a.Publisher_name = b.Publisher_name"
+"                 and a.Channel_name = b.Channel_name"
+"                 and a.site_name = b.site_name"
+"                 and a.site_type = b.site_type"
+"                 and a.zone = b.zone"
+"                 and a.advertiser = b.advertiser"
+"                 and a.line_item_id = b.line_item_id"
+"                 and a.creative_id = b.creative_id"
+"                 and a.load_timestamp = b.load_timestamp"
+"                 group each by a.site_name"
+"                 )e on d.site_name =e.site_name"
+"    ignore case )f"
+" left join"
+"    (select dfp_property_name , station_name, state_name  from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Property_Name] ignore case)g"
+"   on f.site_name = g.dfp_property_name"
+" order by site_name"
+" ignore case";

		 log.info("PERFORMANCE BY PROPERTY QUERY = "+QUERY);
		 
		
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			for (TableRow row : rowList) {
				List<TableCell> cellList = row.getF();
				publisherPropertiesObj = new PublisherPropertiesObj(cellList
						.get(1).getV().toString(), cellList.get(0).getV()
						.toString(), new Double(cellList.get(1).getV()
						.toString()), new Double(cellList.get(2).getV()
						.toString()), new Double(cellList.get(3).getV()
						.toString()), new Long(cellList.get(4).getV()
						.toString()), new Long(cellList.get(5).getV()
						.toString()), new Double(cellList.get(6).getV()
						.toString()), "", cellList.get(7).getV().toString(),
						cellList.get(8).getV().toString(),
						cellList.get(9).getV().toString(),
						new Double(cellList.get(10).getV().toString()),
						new Long(cellList.get(11).getV().toString()));
				performanceByPropertyObjList.add(publisherPropertiesObj);

			}

		}

		return performanceByPropertyObjList;

	}

	public List<PublisherPropertiesObj> loadAllPerformanceByProperty(String lowerDate,String upperDate, String channelIds, String selectedDFPPropertiesQuery, QueryDTO queryDTO) throws Exception {
			log.info("within loadAllPerformanceByProperty() of LinMobileDao.java");
			
			List<PublisherPropertiesObj> performanceByPropertyObjList = new ArrayList<PublisherPropertiesObj>();
			PublisherPropertiesObj publisherPropertiesObj;
			try {
				StringBuilder QUERY = new StringBuilder();
				QUERY.append(" select c.channel_name, ifnull(c.site_name,'') as site_name, c.ECPM as ECPM, c.ImpressionsDelivered as ImpressionsDelivered, c.clicks as clicks , ")
						.append(" ifnull(d.total_impressions,0) as total_impressions, ifnull(d.Payout,0.0) as Payout, ")
						.append(" ifnull(c.state_name,'') as state_name, ifnull(c.site,'') as site,  c.payout as DFP_Payout, ifnull(c.latitude,37.82495120302931) as latitude, ifnull(c.longitude,-96.50708650000001) as longitude ")
						.append(" from ( ")
						.append(" select channel_name, ECPM, ImpressionsDelivered, clicks, Payout, ifnull(g.Market,'') as state_name, g.latitude as latitude, g.longitude as longitude, f.site_name as site ,  ")
						.append(" case when REGEXP_REPLACE(ifnull(g.station_name,''),'null','')=''  ")
						.append(" then f.site_name when g.station_name = 'NA'  then 'US'  else g.station_name end site_name  ")
						.append(" from ( ")
						.append(" SELECT channel_name, site_name, ")
						.append(" ifnull(round((sum(total_revenue)/sum(total_impressions))*1000,4),0.0) as ECPM, ")
						.append(" sum(total_impressions) as ImpressionsDelivered,  sum(total_clicks) as clicks,  ")
						.append(" round(sum(case  when channel_id in (2,7) then 0 else total_revenue end),4) as Payout ") //Pass Channelid
						.append(" from "+queryDTO.getQueryData())
						.append(" where  Passback = 1 ")  //Pass publisherlid	
						.append(selectedDFPPropertiesQuery)
						.append(" and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' ")
						.append(" and ((channel_id in ("+channelIds+") and direct_delivered = 1) or (channel_id in ("+channelIds+") and direct_delivered = 0))")   //Pass Channelid
						.append(" group each by site_name,channel_name  ignore case)F  ")
						.append(" left join  ")
						.append(" (select dfp_property_name , station_name, market, latitude, longitude from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Property_Name] ignore case)g ")
						.append(" on f.site_name = g.dfp_property_name ignore case)c ")
						.append(" left join ( ")
						.append(" SELECT  channel_name, sum(ifnull(total_impressions,0)) as total_impressions, ")
						.append(" ifnull(sum(total_revenue),0) as Payout   ")
						.append(" from "+queryDTO.getQueryData())
						.append(" where  Passback = 1 ")	//Pass publisherlid		
						.append(selectedDFPPropertiesQuery)
						.append(" and (channel_id in ("+channelIds+") and direct_delivered = 1) ") //Pass Channelid
						.append(" and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' ")
						.append(" group each by channel_name ignore case ")
						.append("  )d on c.channel_name = d.channel_name ")
						.append("  order by site_name ") ;
	
	
				log.info("ALL PERFORMANCE BY PROPERTY QUERY = "+QUERY);
				queryDTO.setQueryData(QUERY.toString());
				QueryResponse queryResponse = null;
				 int i=0;
				do{
				             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);;
				}
				catch(com.google.api.client.googleapis.json.GoogleJsonResponseException e)
				{
					queryResponse = new QueryResponse();
					queryResponse.setJobComplete(true);
				}
				catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
				}
				i++;
				}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));
	
				if (queryResponse.getRows() != null) {
					List<TableRow> rowList = queryResponse.getRows();
					for (TableRow row : rowList) {
						List<TableCell> cellList = row.getF();
						publisherPropertiesObj = new PublisherPropertiesObj(
						cellList.get(0).getV().toString(),
						cellList.get(1).getV().toString(),
						new Double(cellList.get(2).getV().toString()), 
						new Double(cellList.get(3).getV().toString()),
						new Long(cellList.get(4).getV().toString()),
						new Long(cellList.get(5).getV().toString()),
						new Double(cellList.get(6).getV().toString()),
						cellList.get(7).getV().toString(),
						cellList.get(8).getV().toString(),
						new Double(cellList.get(9).getV().toString())
						);
						publisherPropertiesObj.setLatitude(cellList.get(10).getV().toString());
						publisherPropertiesObj.setLongitude(cellList.get(11).getV().toString());
						performanceByPropertyObjList.add(publisherPropertiesObj);
					}
				}
			}
			catch (Exception e) {
			
			log.severe("Exception in  loadAllPerformanceByProperty in LinMobileDAO: "+e.getMessage());
			}
			log.info("All performanceByPropertyObjList.size() = "+performanceByPropertyObjList.size());
			return performanceByPropertyObjList;

			}

	public List<SellThroughDataObj> loadSellThroughDataList(String lowerDate, String upperDate, String selectedDFPPropertiesQuery, QueryDTO queryDTO) {
		log.info("within loadSellThroughDataList() of LinMobileDao.java ");
		List<SellThroughDataObj> sellThroughDataList = new ArrayList<SellThroughDataObj>();
		SellThroughDataObj sellThroughDataObj;

	try {
		String queryData = queryDTO.getQueryData();
		String dataSet = queryData.substring(0, queryData.indexOf("."));
		StringBuilder QUERY = new StringBuilder();
		QUERY.append(" select case when REGEXP_REPLACE(ifnull(y.station_name,''),'null','')='' ")
			.append(" then x.site_name ")
			.append(" when y.station_name = 'NA' then 'US' ")
			.append(" else y.station_name end site_name, ")
			.append(" Ifnull(creative_size,'') as creative_size, ")
			.append(" Ifnull(forecasted_impressions,0) as forecasted_impressions, ")
			.append(" Ifnull(available_impressions,0) as available_impressions, ")
			.append(" Ifnull(reserved_impressions,0) as reserved_impressions, ")
			.append(" Ifnull(sell_through_rate,0) as sell_through_rate from ( ")
			.append(" select c.site_name as site_name,c.available_impressions as available_impressions, ")
			.append(" c.creative_size as creative_size ,'' as date, c.forecasted_impressions as forecasted_impressions, ")
			.append(" '' as frequency , '' as load_timestamp, ") 
			.append(" c.reserved_impressions as reserved_impressions, c.sell_through_rate as sell_through_rate, '' as site_id  ")
			.append(" FROM "+queryData +" c ")
			.append(" Join each ")
			.append(" (SELECT  frequency, site_name, creative_size, start_date, end_date,pubisher_id ")
			.append(" ,data_source, max(load_timestamp) as load_timestamp ")
			.append(" FROM "+queryData)
			.append(" where start_date >= '"+lowerDate+" 00:00:00' and start_date <= '"+upperDate+" 00:00:00' ")
			.append(selectedDFPPropertiesQuery)
			.append(" and site_name not in ('LIN Passbacks (lin.pb)','lin.wlin')")
			.append(" group each by frequency, site_name, creative_size, start_date, end_date,pubisher_id,data_source ")
			.append(" ignore case)d ")
			.append(" on c.frequency = d.frequency and c.site_name = d.site_name and c.load_timestamp = d.load_timestamp ")
			.append(" and c.start_date = d.start_date and c.end_date = d.end_date and c.creative_size = d.creative_size ")
			.append(" and c.data_source = d.data_source order by site_name ignore case ) x left join ")
			.append(" (select dfp_property_name , station_name, state_name ")
			.append(" FROM "+dataSet+".Property_Name")
			.append(" ignore case)y on x.site_name = y.dfp_property_name ignore case");
			
			 log.info("LOAD SELL THROUGH DATA QUERY = "+QUERY);
			
			queryDTO.setQueryData(QUERY.toString());
			QueryResponse queryResponse = null;
			 int i=0;
				do{
		             try {
						queryResponse = BigQueryUtil.getBigQueryData(queryDTO);;
					}
		             catch(com.google.api.client.googleapis.json.GoogleJsonResponseException e)
		             {
							queryResponse = new QueryResponse();
							queryResponse.setJobComplete(true);
		             } 
		            catch (Exception e) {
						log.severe("Query Exception = " + e.getMessage());
						
					}
					i++;
				}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));
	
			if (queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
				long id = 1;
				for (TableRow row : rowList) {
					id++;
					List<TableCell> cellList = row.getF();
					sellThroughDataObj = new SellThroughDataObj(
							cellList.get(0).getV().toString(),
							cellList.get(1).getV().toString(),
							cellList.get(2).getV().toString(),
							cellList.get(3).getV().toString(),
							cellList.get(4).getV().toString(),
							cellList.get(5).getV().toString()
							);
	
					sellThroughDataList.add(sellThroughDataObj);
	
				}
	
			}
			if(sellThroughDataList != null) {
				log.info("sellThroughDataList size : "+sellThroughDataList.size());
			}
	} catch (Exception e) {
		log.severe("Exception in loadSellThroughDataList of LinMobileDAO = " + e.getMessage());
		
	}
		return sellThroughDataList;

	}

	public List<PublisherChannelObj> loadPublisherDataList() {
		log.info("loadPublisherDataList()");
		List<PublisherChannelObj> publisherList = obfy.load().type(
				PublisherChannelObj.class)
		// .filter("date >= ",lowerDate)
		// .filter("date <= ",upperDate)
				.list();
		return publisherList;
	}

	public List<PublisherChannelObj> loadChannels(String publisherName) {
		List<PublisherChannelObj> publisherList = obfy.load().type(PublisherChannelObj.class)
				.filter("publisherName = ", publisherName).list();
		return publisherList;
	}

	public List<PublisherChannelObj> loadChannelsByName(String channelName)
			throws DataServiceException {
		List<PublisherChannelObj> publisherList = obfy.load().type(PublisherChannelObj.class)
				.filter("channelName = ", channelName).limit(1).list();
		return publisherList;
	}

	public List<AdvertiserReportObj> loadLineItem(String lowerDate,
			String upperDate, long lineItemId) throws DataServiceException {
		log.info("DAO: loadLineItem :lineItemId :" + lineItemId);
		List<AdvertiserReportObj> resultList = obfy.load().type(AdvertiserReportObj.class)
				.filter("date >= ", lowerDate)
				.filter("date <= ", upperDate)
				.filter("lineItemId = ", lineItemId).order("-date").list();
		return resultList;
	}

	public List<CustomLineItemObj> loadLineItem(String lowerDate,
			String upperDate, String lineItemName) throws DataServiceException {
		log.info("DAO: loadLineItem :lineItemName :" + lineItemName);
		List<CustomLineItemObj> resultList = obfy.load().type(CustomLineItemObj.class)
				.filter("date >= ", lowerDate)
				.filter("date <= ", upperDate)
				.filter("lineItemName = ", lineItemName).order("-date").list();
		return resultList;
	}

	public List<PerformanceMetricsObj> loadLineItemForPerformanceMetrics(
			String lowerDate, String upperDate, String lineItemName)
			throws DataServiceException {
		log.info("DAO: loadLineItem :lineItemName :" + lineItemName);
		List<PerformanceMetricsObj> resultList = obfy.load().type(PerformanceMetricsObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.filter("lineItemName = ", lineItemName).order("-date").list();
		return resultList;
	}

	public List<ActualAdvertiserObj> loadActualDataForAdvertiser(int size,
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadActualDataForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<ActualAdvertiserObj> actualAdvertiserList = null;
		actualAdvertiserList = obfy.load().type(ActualAdvertiserObj.class)
				.filter("days >= ", lowerDate).filter("days <= ", upperDate)
				.order("-days")
				/*
				 * .order("-adServerCTR") .limit(size)
				 */
				.list();
		return actualAdvertiserList;
	}

	public List<ForcastedAdvertiserObj> loadForcastDataForAdvertiser(int size,
			String lowerDate, String upperDate) throws DataServiceException {
		log.info("loadForcastDataForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<ForcastedAdvertiserObj> forcastAdvertiserList = null;
		forcastAdvertiserList = obfy.load().type(ForcastedAdvertiserObj.class)
				.filter("days >= ", lowerDate).filter("days <= ", upperDate)
				.order("-days")
				.list();
		return forcastAdvertiserList;
	}
	
	public List<OrderLineItemObj> loadLineItem(long lineItemId)
			throws DataServiceException {
		log.info("DAO: loadLineItem :lineItemId :" + lineItemId);
		List<OrderLineItemObj> resultList = obfy.load().type(OrderLineItemObj.class)
				.filter("lineItemId = ", lineItemId).list();
		return resultList;
	}

	public PublisherChannelObj loadChannelPerformanceForPopUP(String lowerDate,
			String upperDate, String id) {

		PublisherChannelObj result = obfy.load().type(PublisherChannelObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.filter("id", id).first().now();
		return result;

	}

	public List<PublisherChannelObj> loadActualDataForPublisher(String lowerDate, String upperDate, String channelId, QueryDTO queryDTO) throws DataServiceException {
		log.info("loadActualDataForpublisher :lowerDate:" + lowerDate + " and upperDate:" + upperDate);
	
		List<PublisherChannelObj> actualPublisherList = new ArrayList<PublisherChannelObj>();
		PublisherChannelObj publisherChannelObj;
	  try {
	     StringBuilder QUERY = new StringBuilder();
		 QUERY.append(" SELECT date(date) as date, Channel_name, sum(requests) as requests, ")
		.append(" sum(total_impressions) as ImpressionsDelivered,  ")
		.append(" ifnull(round((sum(total_impressions)/sum(REQUESTS))*100,4),0.0) Fill_Rate, sum(total_clicks) as Clicks, ")
		.append(" ifnull(round(sum(total_clicks)/sum(total_impressions),4),0.0) as CTR, ")
		.append(" round(sum(total_revenue),4) Revenue,  ")
		.append(" ifnull(round((sum(total_revenue)/sum(total_impressions))*1000,4),0.0) ECPM, ")
		.append(" ifnull(round((sum(total_revenue)/sum(requests))*1000,4),0.0) as RPM  ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where direct_delivered = 1")  // Pass Pubisher id
		.append(" and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' and Passback = 1 ")
		.append(" and channel_id in ("+channelId+")  ")  // Pass channel id
		.append(" group each by date, Channel_name ")
		.append(" order by date,Channel_name ")
		.append(" ignore case ");
		
		log.info("publisher trends analysis Query :  "+QUERY );
		queryDTO.setQueryData(QUERY.toString());
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					publisherChannelObj = new PublisherChannelObj();
					List<TableCell> cellList = row.getF();

					publisherChannelObj = new PublisherChannelObj
							(cellList.get(0).getV().toString(), 
							 cellList.get(0).getV().toString(), 
							new Long(cellList.get(2).getV().toString()), 
							new Long(cellList.get(3).getV().toString()), 
							new Long(cellList.get(3).getV().toString()), 
							new Double(cellList.get(4).getV().toString()), 
							new Long(cellList.get(5).getV().toString()), 
							new Double(cellList.get(6).getV().toString()), 
							new Double(cellList.get(7).getV().toString()), 
							new Double(cellList.get(8).getV().toString()), 
							new Double(cellList.get(9).getV().toString()), 
							"", 
							cellList.get(1).getV().toString(), 
							0.0, 
							0.0, 
							0.0);

					actualPublisherList.add(publisherChannelObj);

				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}

		}
	  }
	  catch (Exception e) {
		
		log.severe("Exception in  loadActualDataForPublisher in LinMobileDAO : " + e.getMessage());
	 }
		return actualPublisherList;
	}

	public List<PublisherChannelObj> loadChannels(String lowerDate,
			String upperDate, String channelName) throws DataServiceException {
		List<PublisherChannelObj> actualPublisherList = null;
		actualPublisherList = obfy.load().type(PublisherChannelObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.filter("channelName = ", channelName).order("-date").list();
		return actualPublisherList;
	}

	public List<PublisherChannelObj> loadReallocationDataForPublisher(int size,
			String lowerDate, String upperDate, String publisherName)
			throws DataServiceException {
		log.info("loadActualDataForAdvertiser :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate);
		List<PublisherChannelObj> reallocationPublisherList = null;
		reallocationPublisherList = obfy.load().type(PublisherChannelObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.filter("publisherName = ", publisherName).order("-date")
				.list();
		return reallocationPublisherList;
	}

	public List<SellThroughDataObj> loadSellThroughDataByProperty(
			String lowerDate, String upperDate, String property)
			throws DataServiceException {
		log.info("loadSellThroughDataList :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate + " and property:" + property);
		List<SellThroughDataObj> sellThroughDataList = obfy.load().type(SellThroughDataObj.class)
				.filter("date >= ", lowerDate)
				.filter("date <= ", upperDate).filter("property = ", property)
				.order("-date").list();
		return sellThroughDataList;
	}

	public List<PublisherPropertiesObj> loadPerformanceByPropertyList(
			String lowerDate, String upperDate, String property) {
		log.info("loadPerformanceByPropertyList :lowerDate:" + lowerDate
				+ " and upperDate:" + upperDate + " and property:" + property);
		List<PublisherPropertiesObj> performanceByPropertyObjList = null;
		performanceByPropertyObjList = obfy.load().type(PublisherPropertiesObj.class)
				.filter("date >= ", lowerDate).filter("date <= ", upperDate)
				.filter("name = ", property).order("-date").list();
		return performanceByPropertyObjList;
	}

	public List<PublisherTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderData(String lowerDate, String upperDate, String publisherName,String channelName, String channelAndDataSourceQuery) {
		log.info("in loadTrendAnalysisHeaderData of linMobileDAO");
		PublisherTrendAnalysisHeaderDTO object = new PublisherTrendAnalysisHeaderDTO();
		List<PublisherTrendAnalysisHeaderDTO> trendAnalysisHeaderList = new ArrayList<PublisherTrendAnalysisHeaderDTO>();
	  try {
	     if(channelName == null) {
	    	 channelName = "";
	     }
		
		String QUERY = "SELECT a.Publisher_name, ifnull(SUM(REQUESTS),0) Requests, ifnull(SUM(total_impressions),0) as Delivered, " 
			+ "ifnull((sum(total_impressions)/sum(REQUESTS))*100,0) Fill_Rate, ifnull(sum(total_clicks),0) Clicks, " 
			+ "ifnull((sum(total_clicks)/sum(total_impressions))*100,0) CTR, "
		 	+ "ifnull(sum(total_revenue),0) Revenue, "
		 	+ "ifnull((sum(total_revenue)/sum(total_impressions))*1000,0) as ECPM, "
		 	+ "ifnull((sum(total_revenue)/sum(requests))*1000,0) RPM "
		 	+ "from "
		 	+ "(select * "
		 	+ "from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
	        + "a "
	        + "join each  "
	        + "( "
			+ "select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
			+ "from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
		    + "where Publisher_name = '" + publisherName + "'"; 
		QUERY =QUERY  + channelAndDataSourceQuery;
		QUERY =QUERY  + "And Channel_name in ('"+channelName+"') "
		    + "and date >='"+lowerDate+"' and date <='"+upperDate+"' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
			+" where date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+" and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
			+ "group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
			+ "ignore case) b "
			+ "on a.date = b.date "
			+ "and a.Publisher_name = b.Publisher_name "
			+ "and a.Channel_name = b.Channel_name " 
			+ "and a.site_name = b.site_name "
			+ "and a.site_type = b.site_type "
			+ "and a.zone = b.zone "
			+ "and a.advertiser = b.advertiser "
			+ "and a.line_item_id = b.line_item_id " 
			+ "and a.creative_id = b.creative_id "
			+ "and a.load_timestamp = b.load_timestamp "
			+ "ignore case) "
		    + "group each by a.publisher_name "
		    + "ignore case ";

        log.info("Query publisher trends analysis Header : "+QUERY);
		
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));

		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();

			TableRow row = rowList.get(0);
			List<TableCell> cellList = row.getF();
			object = new PublisherTrendAnalysisHeaderDTO(
					(cellList.get(1).getV().toString()), 
					(cellList.get(2).getV().toString()),
					(cellList.get(3).getV().toString()), 
					(cellList.get(4).getV().toString()),
					(cellList.get(5).getV().toString()),
					(cellList.get(7).getV().toString()),
					(cellList.get(8).getV().toString()),
					(cellList.get(6).getV().toString())

			);
			trendAnalysisHeaderList.add(object);
		}
	  }
	  catch (Exception e) {
			
			log.severe("Exception in  loadTrendAnalysisHeaderData in LinMobileDAO: "+e.getMessage());
	  }
		return trendAnalysisHeaderList;
	}

	public List<PublisherInventoryRevenueObj> loadInventoryRevenueHeaderData(
			String lowerDate, String upperDate, String publisherName,
			String ChannelsStr, String channelAndDataSourceQuery) {
		log.info("in loadInventoryRevenueHeaderData of linMobileDAO");
		PublisherInventoryRevenueObj object = new PublisherInventoryRevenueObj();
		List<PublisherInventoryRevenueObj> inventoryRevenueHeaderList = new ArrayList<PublisherInventoryRevenueObj>();
		try {
		
		String query = "select q.Publisher_name, q.Site, q.ImpressionsDelivered , q.Clicks , q.CTR, q.ECPM, q.RPM,"
+ " q.Payout, ((s.ExcludeHouse_ImpressionsDelivered)/s.ImpressionsDelivered)*100 as Fill_Percentage"
+ " from"
+ " ("
+ " SELECT a.Publisher_name as Publisher_name,"
+ " count(distinct site_id) as Site,"
+ " ifnull(sum(total_impressions),0) as ImpressionsDelivered,"
+ " ifnull(sum(total_clicks),0) as Clicks,"
+ " ifnull((sum(total_clicks)/sum(total_impressions))*100,0) CTR,"  
+ " ifnull((sum(total_revenue)/sum(total_impressions))*1000,0) as ECPM,"
+ " ifnull((sum(total_revenue)/sum(requests))*1000,0) RPM,"
+ " ifnull(sum(total_revenue),0) Payout, 1 as id"
+ " from"
+ " ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " a"
+ " join each"
+ " ("
+ " select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
+ " from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " Where Publisher_name = '"+publisherName+"' ";
query = query + channelAndDataSourceQuery;
query = query + " And "+ChannelsStr 
+ " and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
+" where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
+" and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
+ " GROUP EACH BY  date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
+ " ignore case) b"
+ " on a.date = b.date"
+ " and a.Publisher_name = b.Publisher_name"
+ " and a.Channel_name = b.Channel_name"
+ " and a.site_name = b.site_name"
+ " and a.site_type = b.site_type"
+ " and a.zone = b.zone"
+ " and a.advertiser = b.advertiser"
+ " and a.line_item_id = b.line_item_id"
+ " and a.creative_id = b.creative_id"
+ " and a.load_timestamp = b.load_timestamp"
+ " GROUP EACH BY publisher_name"
+ " ignore case"
+ " )q"
+ " Left Join"
+ " ("
+ " SELECT"
+ " ifnull(u.ImpressionsDelivered,0) as ImpressionsDelivered, u.id as id"
+ " , h.ExcludeHouse_ImpressionsDelivered as ExcludeHouse_ImpressionsDelivered"
+ " from"
+ " (select ifnull(sum(a.total_impressions),0) as ImpressionsDelivered, 1 as id"
+ " from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " a"
+ " join each"
+ " ("
+ " select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
+ " from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " Where Publisher_name = '"+publisherName+"'"; 
query = query + channelAndDataSourceQuery;
query= query+ " And date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
+ " where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
+ " and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
+ " GROUP EACH BY date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
+ " ignore case) b"
+ " on a.date = b.date"
+ " and a.Publisher_name = b.Publisher_name"
+ " and a.Channel_name = b.Channel_name"
+ " and a.site_name = b.site_name"
+ " and a.site_type = b.site_type"
+ " and a.zone = b.zone"
+ " and a.advertiser = b.advertiser"
+ " and a.line_item_id = b.line_item_id"
+ " and a.creative_id = b.creative_id"
+ " and a.load_timestamp = b.load_timestamp"
+ " GROUP BY id ignore case)u"
+ " left join ("
+ " SELECT"
+ " ifnull(sum(total_impressions),0) as ExcludeHouse_ImpressionsDelivered, 1 as id"
+ " from"
+ " ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " a"
+ " join each"
+ " ("
+ " select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
+ " from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]"
+ " Where Publisher_name = '"+publisherName+"'"; 
    
    query = query + channelAndDataSourceQuery;
query = query+ "  and data_source = '"+LinMobileConstants.DFP_DATA_SOURCE+"' and channel_name <> 'House' And "+ChannelsStr 
+ " and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
			+" where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
			+" and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House') "
+ " GROUP EACH BY date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
+ " ignore case) b"
+ " on a.date = b.date"
+ " and a.Publisher_name = b.Publisher_name"
+ " and a.Channel_name = b.Channel_name"
+ " and a.site_name = b.site_name"
+ " and a.site_type = b.site_type"
+ " and a.zone = b.zone"
+ " and a.advertiser = b.advertiser"
+ " and a.line_item_id = b.line_item_id"
+ " and a.creative_id = b.creative_id"
+ " and a.load_timestamp = b.load_timestamp"
+ " GROUP BY id"
+ " ignore case)h"
+ " on u.id = h.id"
+ " )s on s.id = q.id"
+ " ignore case";
		
		log.info("INVENTORY AND REVENUE HEADER QUERY = "+query);
		
		
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(query);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));
		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();

			TableRow row = rowList.get(0);
			List<TableCell> cellList = row.getF();
			object = new PublisherInventoryRevenueObj(("23"), (cellList.get(2)
					.getV().toString()), ("23"), ("23"), (cellList.get(3)
					.getV().toString()), (cellList.get(4).getV().toString()),
					(cellList.get(5).getV().toString()), (cellList.get(6)
							.getV().toString()),
					(cellList.get(7).getV().toString()), (cellList.get(8)
							.getV().toString()));
			inventoryRevenueHeaderList.add(object);

		}
	  }
	  catch (Exception e) {
		  
		  log.severe("Exception in  loadInventoryRevenueHeaderData in LinMobileDAO: "+e.getMessage());
	 }
		return inventoryRevenueHeaderList;
	}
	
	public List<PublisherSummaryObj> loadPublisherSummaryData(String startDate,String endDate, String selectedDFPPropertiesQuery, String channelIds, QueryDTO queryDTO) {
		log.info("within loadPublisherSummaryData() of LinMobileDAO");
		PublisherSummaryObj object = null;
		List<PublisherSummaryObj> publisherSummaryList = new ArrayList<PublisherSummaryObj>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("SELECT Channel_name,count(distinct site_id) as Site, ")
					.append(" sum(total_impressions) as ImpressionsDelivered, sum(total_clicks) as Clicks, sum(requests) as requests, ")
					.append(" ifnull(round(sum(total_clicks)/sum(total_impressions),4),0.0) as CTR, ")
					.append(" ifnull(round((sum(total_revenue)/sum(total_impressions))*1000,4),0.0) ECPM, ")
					.append(" ifnull(round((sum(total_revenue)/sum(requests))*1000,4),0.0) as RPM, ")
					.append(" round(sum(total_revenue),4) Payout ")
					.append(" from "+queryDTO.getQueryData())
					.append(" Where Passback = 1 ") // Pubisher_id
					.append(selectedDFPPropertiesQuery)
					.append(" and date >='"+startDate+" 00:00:00' and date <='"+endDate+" 00:00:00' ")
					.append(" and channel_id in ("+channelIds+") and direct_delivered = 1 ") // Pass Channel_id
					.append(" group by Channel_name order by Channel_name ")
					.append(" ignore case ");

			log.info("PUBLISHER SUMMARY DATA = " + query);

			QueryResponse queryResponse = null;
			int i = 0;
			do {
				try {
					queryDTO.setQueryData(query.toString());
					queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			} while (queryResponse == null
					|| (!queryResponse.getJobComplete() && i <= 3));
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();

				try {
					for (TableRow row : rowList) {
						List<TableCell> cellList = row.getF();

						object = new PublisherSummaryObj(cellList.get(0).getV() .toString(), 
								new Integer(cellList.get(1).getV().toString()),
								new Long(cellList.get(2).getV().toString()), 
								new Long(cellList.get(3).getV().toString()), 
								new Long(cellList.get(4).getV().toString()), 
								new Double(cellList.get(5).getV().toString()),
								new Double(cellList.get(6).getV().toString()),
								new Double(cellList.get(7).getV().toString()),
								new Double(cellList.get(8).getV().toString()));
						publisherSummaryList.add(object);
					}
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}

			}
		} catch (Exception e) {
			
			log.severe("Exception in  loadPublisherSummaryData in LinMobileDAO: "
					+ e.getMessage());
		}
		log.info("publisherSummaryList :"+publisherSummaryList.size());
		return publisherSummaryList;

	}
	
	

	public List<PublisherReallocationHeaderDTO> loadReallocationHeaderData(
			String lowerDate, String upperDate, String publisherName,
			List<String> ChannelList) {

		log.info("in loadReallocationHeaderData of linMobileDAO");
		PublisherReallocationHeaderDTO object = new PublisherReallocationHeaderDTO();
		List<PublisherReallocationHeaderDTO> reallocationHeaderList = new ArrayList<PublisherReallocationHeaderDTO>();
		String QUERY = "SELECT Publisher_name, SUM(REQUESTS) Requests,  SUM(SERVED) Served, SUM(SERVED) as Delivered,(sum(total_impressions)/sum(REQUESTS))*100 Fill_Rate, sum(total_clicks) Clicks,(sum(total_clicks)/sum(total_impressions))*100 CTR,sum(total_revenue) Revenue,(sum(total_revenue)/sum(total_impressions))*1000 as ECPM,(sum(total_revenue)/sum(requests))*1000 RPM FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] Where date >='"
				+ lowerDate
				+ "' and date <='"
				+ upperDate
				+ "' And Publisher_name = '" + publisherName + "' And ";
		if (ChannelList.size() != 0) {
			int channelListSize = ChannelList.size();
			int i = 0;
			if (channelListSize == 1) {
				QUERY = QUERY + "Channel_name = '" + ChannelList.get(i)
						+ "' GROUP EACH BY publisher_name";
			} else {
				QUERY = QUERY + "(";
				for (i = 0; i < channelListSize; i++) {
					QUERY = QUERY + "Channel_name = '" + ChannelList.get(i)
							+ "'";
					if (i != channelListSize - 1) {
						QUERY = QUERY + " OR ";
					}
				}
				QUERY = QUERY + " )GROUP EACH BY publisher_name";

			}

		}

		
		QueryResponse queryResponse = null;
		 int i=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				i++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=3));

		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();

			TableRow row = rowList.get(0);
			List<TableCell> cellList = row.getF();
			object = new PublisherReallocationHeaderDTO(
					(cellList.get(1).getV().toString()), (cellList.get(3)
							.getV().toString()),
					(cellList.get(4).getV().toString()), (cellList.get(5)
							.getV().toString()),
					(cellList.get(6).getV().toString()), (cellList.get(8)
							.getV().toString()),
					(cellList.get(9).getV().toString()), (cellList.get(7)
							.getV().toString()));
			reallocationHeaderList.add(object);

		}
		return reallocationHeaderList;
	}

	public List<PopUpDTO> loadChannelsPerformancePopUpGraphData(String startDate,String endDate,String channelId, String publisherId, QueryDTO queryDTO) throws Exception {
		log.info("within loadChannelsPerformancePopUpGraphData() of LinMobileDao class");
		List<PopUpDTO> resultList = new ArrayList<PopUpDTO>();
	     StringBuilder QUERY = new StringBuilder();
	     QUERY.append("select channel_name, date(date) as date, sum(total_impressions) as ImpressionsDelivered ")
			.append(" from "+queryDTO.getQueryData())
			.append(" where date >='"+startDate+" 00:00:00' and date <='"+endDate+" 00:00:00' ")
			.append(" and Passback = 1 ") // Pubisher_id
			.append(" And channel_id in ("+channelId+") and direct_delivered = 1 ") //Pass Channel Id
			.append(" group by date, Channel_name ")
			.append(" order by date ")
			.append(" ignore case");
		
		log.info("CHANNEL PERFORMANCE POP-UP GRAPH DATA QUERY = "+QUERY);
		queryDTO.setQueryData(QUERY.toString());
		QueryResponse queryResponse = null;
		
		 int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(queryDTO);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				j++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && j<=3));

		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			log.info("queryResponse.getRows():" + rowList.size());
			for (int i = 0; i < rowList.size(); i++) {
				PopUpDTO dataObject = new PopUpDTO();
				TableRow row = rowList.get(i);
				List<TableCell> cellList = row.getF();
				dataObject.setDate(cellList.get(1).getV().toString());
				dataObject.setImpressionDeliveredLifeTime(cellList.get(2)
						.getV().toString());
				resultList.add(dataObject);
			}

		}
		return resultList;
	}

	@Override
	public List<ReconciliationDataDTO> loadAllRecociliationData(String startDate, String endDate, String lineItemQuery, QueryDTO queryDTO) throws Exception {
		log.info("Inside loadAllRecociliationData of LinMobileDAO");
		List<ReconciliationDataDTO> reconciliationDataDTOList = new ArrayList<ReconciliationDataDTO>();
		startDate = startDate+" 00:00:00";
		endDate = endDate+" 00:00:00";
		try{
			StringBuilder QUERY = new StringBuilder();
			QUERY.append(" select * from ( ")
			.append(" SELECT date(date) as Date, Data_Source, Channel_Name, '' as line_item, ")
			.append(" sum(Total_Impressions) as Impressions_Delivered, ")
			.append(" sum(requests) as Requests  ")
			.append(" from "+queryDTO.getQueryData())
			.append(" Where date >='"+startDate+"' and date <='"+endDate+"' and passback =1 ")
			.append(" group by Date, Data_Source, Channel_Name, line_item)z ")
			.append(" ,(SELECT date(date) as Date, Data_Source, Channel_Name, " )
			.append(" line_item, sum(Total_Impressions) as Impressions_Delivered,  sum(requests) as Requests  ")
			.append(" from "+queryDTO.getQueryData())
			.append(" Where date >='"+startDate+"' and date <='"+endDate+"' ")
			.append(" and ("+lineItemQuery+") ")
			.append(" group by Date, Data_Source, Channel_Name, line_item ) ")
			.append(" order by Date ");
			
			log.info("actual AllRecociliationData Data query  :"+QUERY);
			
			queryDTO.setQueryData(QUERY.toString());
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(queryDTO);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				j++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && j<=3));
			
			if(queryResponse != null && !queryResponse.getJobComplete()) {
				log.severe("Query job incomplete.");
			}
			
			if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  ReconciliationDataDTO reconciliationDataDTO = new ReconciliationDataDTO();
					    		  reconciliationDataDTO.setFormattedDate(cellList.get(0).getV().toString());
					    		  reconciliationDataDTO.setDate(DateUtil.getDateYYYYMMDD(cellList.get(0).getV().toString()));
					    		  reconciliationDataDTO.setDataSource(cellList.get(1).getV().toString());
					    		  reconciliationDataDTO.setChannelName(cellList.get(2).getV().toString());
					    		  reconciliationDataDTO.setSiteType(cellList.get(3).getV().toString());
					    		  reconciliationDataDTO.setDelivered(new Long(cellList.get(4).getV().toString()));
					    		  reconciliationDataDTO.setRequests(new Long(cellList.get(5).getV().toString()));
			    		 
					    		  reconciliationDataDTOList.add(reconciliationDataDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }
		}catch(Exception e) {
			log.severe("Exception in loadAllRecociliationData of LinMobileDAO."+e.getMessage());
			
		}
		 return reconciliationDataDTOList;
	}

	/*public List<String> loadPublisherPropertyDropDownList(String publisherName,long userId,String term)
	                                                                                                            24.08.2013
	{
		System.out.println(publisherName+"=========="+userId+"=========="+term);
		List<PublisherPropertiesObj> propertyList1 = null;
		List<UserTeamAssocObj> userTeamList = new ArrayList<UserTeamAssocObj>();
		List<TeamPropertiesObj> teamPropertiesList = new ArrayList<TeamPropertiesObj>();
		List<String> propertyList = new ArrayList<String>();
		TeamPropertiesObj propertiesObj = new TeamPropertiesObj();
		UserTeamAssocObj assocObj = new UserTeamAssocObj();
		userTeamList = obfy.load().type(UserTeamAssocObj.class).filter("userId = ", userId).list();
		if(userTeamList!=null && userTeamList.size()>0){
			Iterator<UserTeamAssocObj> iterator = userTeamList.iterator();
			while(iterator.hasNext()){
				List<TeamPropertiesObj> tempList = new ArrayList<TeamPropertiesObj>();
				assocObj = iterator.next();
				String teamId = assocObj.getTeamName();
				tempList  = obfy.load().type(TeamPropertiesObj.class).filter("teamId = ", teamId).filter("publisherName = ", publisherName).list();
				teamPropertiesList.addAll(tempList);
			}
		}
		if(teamPropertiesList!=null && teamPropertiesList.size()>0){
			Iterator<TeamPropertiesObj> iterator = teamPropertiesList.iterator();
			while(iterator.hasNext()){
				propertiesObj = iterator.next();
				String propertyName = propertiesObj.getPropertyName();
				if(propertyList!=null && !propertyList.contains(propertiesObj.getPropertyName())){
					//if(propertyName.toLowerCase().contains(term.toLowerCase())){
						propertyList.add(propertiesObj.getDfp_propertyName());
					}
				}
			}
		//}
		
		propertyList1 = obfy.load().type(PublisherPropertiesObj.class).filter("userId =", userId)
				.filter("publisherName =", publisherName).filter("term =", term).list();

		return propertyList;
	
	}*/
	
	public String agencyAdvertiserCondition(String agency, String agencySQL, String advertiser, String advertiserSQL){
		String conditional = "";
		if (!agency.trim().equalsIgnoreCase("")){
			conditional = conditional + " and ((REGEXP_REPLACE(ifnull("+agencySQL+",''),'null','') in  ('"+agency+"')) )";
		}
		if (!advertiser.trim().equalsIgnoreCase("")){
			conditional = conditional + " and ((REGEXP_REPLACE(ifnull("+advertiserSQL+",''),'null','') in ('"+advertiser+"')))";
		}
		return conditional;
	}
	
	public List<AdvertiserPerformerDTO> loadCampainTotalDataPublisherViewList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency,String properties)
	{
		log.info("Inside loadAdvertiserTotalDataList of RichMediaAdvertiserDAO"+advertiser+" "+agency+" "+properties);
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
				String QUERY = "select k.order as order ,k.line_item_id as line_item_id, k.line_item as line_item,  " 
					+" k.Impression_Delivered as Impression_Delivered,  k.Clicks as Clicks,  k.CTR as CTR, " 
					+" k.revenue_Delivered as revenue_Delivered,  k.advertiser as advertiser, k.agency as agency,  " 
					+" k.creative_type as creative_type, s.market as market,  k.d as d,   k.booked_impression as booked_impression,   " 
					+" k.budget as budget  ,  k.Publisher_Name as Publisher_Name ,k.Site_Name as Site_Name  ,k.Line_item_end_time as Line_item_end_time  from( " 
				+" select REGEXP_REPLACE(ifnull(a.order,''),'null','') as order, "
				+" a.line_item_id as line_item_id, "
				+" REGEXP_REPLACE(ifnull(a.line_item,''),'null','') as line_item, "
				+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered, "
				+" ifnull(sum(a.total_clicks),0) Clicks, "
				+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR, "
				+" (sum(a.Total_revenue)) as revenue_Delivered, "
				+" a.advertiser as advertiser, a.agency as agency, "
				+" a.creative_type as creative_type ,  "
				+" (sum(Delivery_Indicator)/count(line_item_id)) as d, "
				//+" (sum(Delivery_Indicator)/(DATEDIFF(TIMESTAMP('"+upperDate+" 00:00:00'),TIMESTAMP('"+lowerDate+" 00:00:00'))+1)) "
				+"  case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end as booked_impression, "
				+" ((case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end )*a.rate)/1000 as budget  "
				+", a.Publisher_Name as Publisher_Name ,a.Site_Name as Site_Name  ,  Line_item_end_time "
				+" from ( "
				+" select a.order as order, a.line_item_id as line_item_id, a.line_item as line_item, "
				+" a.advertiser as advertiser, a.agency as agency, "
				+" a.total_impressions as Impression_Delivered, a.total_clicks as Clicks, "
				+" a.Total_revenue as revenue_Delivered, a.creative_type as creative_type, "
				+" a.Delivery_Indicator as Delivery_Indicator,  a.cost_type as  cost_type, a.goal_qty as goal_qty ,a.rate as rate ,a.Publisher_Name as Publisher_Name ,a.Site_Name as Site_Name, a.Line_item_end_time as Line_item_end_time     "
				//+" case when ifnull(a.Delivery_Indicator,0.0)=0.0 then 1000000.00 "
				//+" else a.Delivery_Indicator "
				//+" end as Delivery_Indicator "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  a "
				+" join each "
				+" ( "
				+" select date,  Channel_name, site_name, site_type,"
				+" zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
				+" Where Channel_name <> 'House' and advertiser <> 'LIN Mobile - Golfsmith' "
				+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
				+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  ";
//				+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser");
				if(properties != null && !properties.trim().equalsIgnoreCase("")) {
					String[] str = properties.split(",");
					for(int i=0;i<str.length;i++){
						if(str.length == 1){
							QUERY = QUERY +" and (site_name = '"+str[i]+"' )";
						}else{
							 if(i==0){
								QUERY = QUERY +" and (site_name = '"+str[i]+"'";
							}else if(i==str.length-1){
								QUERY = QUERY +" or site_name = '"+str[i]+"' )";
							}else{
								QUERY = QUERY +"  or site_name = '"+str[i]+"'";
							}
						}
						
					}
					//QUERY = QUERY +" and site_name in ('"+properties+"')";
				}
				QUERY = QUERY
		//		+" and Channel_name <> 'House' "
				+" and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
				+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' " 
				+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
				+" group each by date,  Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
				+" ignore case) b "
				+" on a.date = b.date "
				+" and a.Channel_name = b.Channel_name "
				+" and a.site_name = b.site_name "
				+" and a.site_type = b.site_type "
				+" and a.zone = b.zone "
				+" and a.advertiser = b.advertiser "
				+" and a.line_item_id = b.line_item_id " 
				+" and a.creative_id = b.creative_id "
				+" and a.load_timestamp = b.load_timestamp)z "
				+" group each by line_item, line_item_id, order, advertiser, agency, creative_type, booked_impression, budget,Publisher_Name ,Site_Name, Line_item_end_time "
				+" order by CTR desc, line_item "
				+" ) k left join ( select state_name as market, dfp_property_name from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Property_Name] ) s "
				+" on k.site_name = s.dfp_property_name ignore case";

			
			log.info("Rich Media Advertiser Total Data List Query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				j++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && j<=3));
			
			
			
			if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  
					    		  
					    		  String market=cellList.get(10).getV().toString();
					    		  if(market.contains("java.lang")){
					    			  market="NA";
					    		  }
					    		  String endDateTime=cellList.get(16).getV().toString();
					    		  if(endDateTime.contains("java.lang")){
					    			  endDateTime="";
					    		  }
					    		  advertiserPerformerDTO= new AdvertiserPerformerDTO(
			    				  cellList.get(0).getV().toString(),
			    				  new Long(cellList.get(1).getV().toString()),
			    				  cellList.get(2).getV().toString(),
			    				  new Long(cellList.get(3).getV().toString()),
			    				  new Long(cellList.get(4).getV().toString()),
			    				  new Double(cellList.get(5).getV().toString()),
			    				  new Double(cellList.get(6).getV().toString()),
			    				  cellList.get(7).getV().toString(),
			    				  cellList.get(8).getV().toString(),
			    				  cellList.get(9).getV().toString(),
			    				  market,
			    				  new Double(cellList.get(11).getV().toString()),
			    				  "",
			    				  new Long(cellList.get(12).getV().toString()),
			    				  new Double(cellList.get(13).getV().toString()),
			    				  cellList.get(14).getV().toString(),
			    				  cellList.get(15).getV().toString(),
			    				  endDateTime
			    				  );
					    		  
					    		  
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertiserTotalDataList of RichMediaAdvertiserDAO."+e.getMessage());
			
		}
		 return advertiserTotalDataList;
	}
	
	public List<AdvertiserPerformerDTO> loadCampainPerformanceDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{

		log.info("Inside loadDeliveryIndicatorData of RichMediaAdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "select a.line_item_id as line_item_id , "
				+" case when a.cost_type = 'CPM' then "
				+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100) " 
				+" else 0.0 end Life_Time_CTR , "
				+" case when a.cost_type = 'CPM' " 
				+" then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end " 
				+" booked_impression , "
				+" case when a.cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000 " 
				+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget "
				+" , case when ifnull(a.delivery_indicator,0.0)=0.0 then 10000.00 else a.delivery_indicator end as delivery_indicator  "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a " 
				+" join each "
				+" ( "
				+" select channel_name, r.line_item_id as line_item_id, r.date as date, "
				+" r.load_timestamp as load_timestamp , r.zone_id as zone_id, "
				+" r.site_type_id as site_type_id, max(r.creative_id) as creative_id "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r "
				+" join each ( "
				+" select s.line_item_id as line_item_id, s.date as date, "
				+" s.load_timestamp as load_timestamp , s.zone_id as zone_id, "
				+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s " 
				+" join each ( "
				+" select t.line_item_id as line_item_id, t.date as date, "
				+" t.load_timestamp as load_timestamp , "
				+" max(zone_id) as zone_id "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t "
				+" join each ( "
				+" select u.line_item_id as line_item_id, u.date as date, "
				+" max(load_timestamp) as load_timestamp "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u "
				+" join each "
				+" (select line_item_id, max(date) as date "
				+"  from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
				+" where not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
				+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06') and  "
				//		+" Publisher_name = '"+publisherName+"' " 
				+" Channel_name <> 'House'  and advertiser <> 'LIN Mobile - Golfsmith' ";
//				+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser");
				if(properties != null && !properties.trim().equalsIgnoreCase("")) {
					String[] str = properties.split(",");
					for(int i=0;i<str.length;i++){
						if(str.length == 1){
							QUERY = QUERY +" and (site_name = '"+str[i]+"' )";
						}else{
							 if(i==0){
								QUERY = QUERY +" and (site_name = '"+str[i]+"'";
							}else if(i==str.length-1){
								QUERY = QUERY +" or site_name = '"+str[i]+"' )";
							}else{
								QUERY = QUERY +"  or site_name = '"+str[i]+"'";
							}
						}
						
					}
					//QUERY = QUERY +" and site_name in ('"+properties+"')";
				}
				QUERY = QUERY
				
				+" And not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
				+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
				+" and line_item_id in (select line_item_id "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
				+" where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00') "
				+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
				+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
				+" group each by line_item_id  ignore case)v "
				+"  on u.line_item_id = v.line_item_id and u.date = v.date "
				+"  group each by line_item_id, date  ignore case)w "
				+"  on t.line_item_id = w.line_item_id and t.date = w.date " 
				+"  and t.load_timestamp = w.load_timestamp "
				+"  group each by line_item_id, date, load_timestamp  ignore case)x "
				+"  on s.line_item_id = x.line_item_id and s.date = x.date "
				+"  and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id "
				+"  group each by line_item_id, date, load_timestamp, zone_id "
				+"  ignore case)y "
				+"  on r.line_item_id = y.line_item_id and r.date = y.date " 
				+"  and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id "
				+"  and r.site_type_id = y.site_type_id "
				+"  group each by line_item_id, date, load_timestamp, zone_id, site_type_id "
				+"  ,  channel_name "
				+"  ignore case) b "
				+" on a.Channel_name = b.Channel_name "
				+" and a.site_type_id = b.site_type_id "
				+" and a.creative_id = b.creative_id "
				+" and a.line_item_id = b.line_item_id " 
				+" and a.load_timestamp = b.load_timestamp "
				+" and a.zone_id = b.zone_id "
				+" and a.date = b.date  "
				+" ignore case";
			
			log.info("Rich Media Delivery Indicator Data List Query : "+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				j++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && j<=3));
			
			
			
			if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  advertiserPerformerDTO= new AdvertiserPerformerDTO(
					    		  new Long(Long.valueOf(cellList.get(0).getV().toString())),
			    				  new Double(Double.parseDouble(cellList.get(1).getV().toString())),
			    				  new Long(Long.valueOf(cellList.get(2).getV().toString())),
			    				  new Double(Double.parseDouble(cellList.get(3).getV().toString()))
			    				  );
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadDeliveryIndicatorData of RichMediaAdvertiserDAO."+e.getMessage());
			
		}
		 return advertiserTotalDataList;
	
	}
	
	/*public List<AdvertiserPerformerDTO> loadCampainPerformanceDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{

		log.info("Inside loadDeliveryIndicatorData of RichMediaAdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			
			String QUERY =  " select line_item_id,	CTR,	goal_QTY as booked_impression	,Budget  from [LIN.Core_Performance] limit 100";
			String QUERY =  " select line_item_id,	ifnull(Life_Time_CTR,0.0) as Life_Time_CTR,	ifnull(booked_impression,0) as 	booked_impression, ifnull(Budget,0.0) as Budget, ifnull(Percentage_Time_Elapsed,0.0) as Percentage_Time_Elapsed "
				+" from (  " 
				+" select a.line_item_id as line_item_id , "
				+" case when a.cost_type = 'CPM' then "
				+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100) " 
				+" else 0 end Life_Time_CTR , "
				+" case when a.cost_type = 'CPM' " 
				+" then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end " 
				+" booked_impression , "
				+" case when a.cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000 " 
				+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget, "
+" case when Line_item_end_time>='"+lowerDate+" 00:00:00' then   "
+" datediff(CURRENT_TIMESTAMP(),Line_item_start_time)+1 > 0  "
+" else 0 end as Day_Passed,   "
+" datediff(Line_item_end_time,Line_item_start_time) as Day_Of_Campaign,  "
+" ((case when Line_item_end_time>='"+lowerDate+" 00:00:00' then  "
+" datediff(CURRENT_TIMESTAMP(),Line_item_start_time)+1 > 0  "
+" else 0 end)/(datediff(Line_item_end_time,Line_item_start_time)+1))  "
+" as Percentage_Time_Elapsed  "
+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a    "
+" join each (  select u.channel_name as channel_name, u.line_item_id as line_item_id, u.date as date,  max(load_timestamp) as load_timestamp     "
+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u     "
+" join each  (select line_item_id, max(date) as date   "
+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
+" where Publisher_name = '"+publisherName+"' and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')   "
+" and line_item_id not in (SELECT line_item_id FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]   "
+"  where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00'   "
+"  and site_name in ('LIN Passbacks (lin.pb)','lin.wlin') and Channel_name <> 'House')  "
+" and site_name not in ('INT2400 +AC0- Leaderboard 728x90 +IBM- ROS','INT2400 +AC0- Smartphone 320x50 +IBM- ROS')  "
+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser");
			if(properties != null && !properties.trim().equalsIgnoreCase("")) {
				String[] str = properties.split(",");
				for(int i=0;i<str.length;i++){
					if(str.length == 1){
						QUERY = QUERY +" and (site_name = '"+str[i]+"' )";
					}else{
						 if(i==0){
							QUERY = QUERY +" and (site_name = '"+str[i]+"'";
						}else if(i==str.length-1){
							QUERY = QUERY +" or site_name = '"+str[i]+"' )";
						}else{
							QUERY = QUERY +"  or site_name = '"+str[i]+"'";
						}
					}
					
				}
			}
			QUERY = QUERY
+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')     "
+" and  Channel_name <> 'House' and advertiser <> 'LIN Mobile - Golfsmith'     "
+" And not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'   "
+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')    "

+" group each by line_item_id  ignore case)v     "
+" on u.line_item_id = v.line_item_id and u.date = v.date  "
+" group each by line_item_id, Channel_name, date  ignore case)b    "
+" on a.Channel_name = b.Channel_name and a.line_item_id = b.line_item_id  and a.load_timestamp = b.load_timestamp  and a.date = b.date   "
+" where a.line_item_id in (select line_item_id  from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' group each by line_item_id )  "
+" order by line_item_id "
+" ignore case)z  "
+" group by line_item_id,	Life_Time_CTR,	booked_impression,	Budget,	Percentage_Time_Elapsed  ";
			
			log.info("publisher Delivery Indicator Data List Query : "+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);;
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					
				}
				j++;
			}while(queryResponse == null || (!queryResponse.getJobComplete() && i<=10));
			
			
			
			if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  advertiserPerformerDTO= new AdvertiserPerformerDTO(
					    		  new Long(cellList.get(0).getV().toString()),
			    				  new Double(cellList.get(1).getV().toString()),
			    				  new Long(cellList.get(2).getV().toString()),
			    				  new Double(cellList.get(3).getV().toString()),
			    				  new Double(cellList.get(4).getV().toString())
			    				  );
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadDeliveryIndicatorData of RichMediaAdvertiserDAO."+e.getMessage());
			
		}
		 return advertiserTotalDataList;
	
	}*/
}
