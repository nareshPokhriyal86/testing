package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.ads.dfp.jaxws.v201403.ComputedStatus;
import com.google.api.ads.dfp.jaxws.v201403.Date;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemService;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IRichMediaAdvertiserDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.AdvertiserReallocationHeaderDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisActualDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisForcastDTO;
import com.lin.web.dto.AdvertiserTrendAnalysisHeaderDTO;
import com.lin.web.dto.AgencyDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.PopUpDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;


public class RichMediaAdvertiserDAO implements IRichMediaAdvertiserDAO {
	
	private static final Logger log = Logger.getLogger(RichMediaAdvertiserDAO.class.getName());
	private Objectify obfy = OfyService.ofy();	

	public void saveObject(Object obj) throws DataServiceException {
		obfy.save().entity(obj);
	}

	public void deleteObject(Object obj) throws DataServiceException {

		obfy.delete().entity(obj);
		log.info("Object deleted successfully from datastore.");
	}
	
	public List<AdvertiserTrendAnalysisHeaderDTO> loadTrendAnalysisHeaderDataAdvertiserView(String lowerDate,String upperDate,String campainOrder,String lineItem,String publisherName){		
		
		QueryResponse queryResponse = null;
		AdvertiserTrendAnalysisHeaderDTO object = new AdvertiserTrendAnalysisHeaderDTO();
		List<AdvertiserTrendAnalysisHeaderDTO> trendAnalysisHeaderList = new ArrayList<AdvertiserTrendAnalysisHeaderDTO>();
		log.info("in advertiser DAO loadTrendAnalysisHeaderDataAdvertiserView");
		if(!lineItem.trim().equalsIgnoreCase("") || !campainOrder.trim().equalsIgnoreCase("") ){
		log.info("in loadTrendAnalysisHeaderDataAdvertiserVew of RichMediaAdvertiserDAO");
		
		String QUERY = ""
			+" select * from (  "
			+" select 'date filter' as column , "
			+" integer(0) as Booked_impression, "
			+" sum(Total_clicks) as Clicks ,	  "
			+" ifnull((sum(Total_clicks)/sum(Impression_delivered))*100,0) CTR,   "
			+" ifnull(sum(Impression_delivered),0) as Impression_delivered, "
			+" ifnull(sum(revenue_Delivered),0.0) as revenue_Delivered, "
			+" 0.0 as  revenue_remaining, "
			+" 0.0 as budget  "
			+" from(select a.Total_clicks as Total_clicks,	  "
			+" ifnull((a.Total_Impressions),0) as Impression_delivered, "
			+" ifnull((a.Total_revenue),0.0) as revenue_Delivered,  "
			+" 0.0 as  revenue_remaining "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  a "
			+" join each( "
			+" select date, Channel_name, "
			+" site_name, site_type, zone, advertiser, line_item_id, "
			+" creative_id, MAX(load_timestamp) as load_timestamp "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  ";
			if(lineItem != null && !lineItem.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and line_item in( '"+lineItem+"' )";
			}
	//		+" And Publisher_name = '"+publisherName+"' "
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" group each by date, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
			+" ignore case) b "
			+" on a.date = b.date "
	//		+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp  ignore case)  "
			+" group by column ignore case)z, "
			+" (select 'Total' as column ,  "
			+" ifnull(sum(goal_qty),0) as Booked_impression,  "
			+" ifnull(sum(line_item_lifetime_clicks),0) as Clicks ,	  "
			+" ifnull((sum(line_item_lifetime_clicks)/sum(line_item_lifetime_impressions))*100,0.0) CTR,  "
			+" ifnull(sum(line_item_lifetime_impressions),0) as Impression_delivered, "
			+" ifnull(sum(Revenue_Delivered),0.0) as revenue_Delivered, "
			+" ifnull((sum(Budget)-(sum(Revenue_Delivered))),0.0) revenue_remaining,  "
			+" ifnull(sum(Budget),0.0) Budget "
			+" from(  "
			+" select a.cost_type,case when a.cost_type = 'CPM'  "
			+" then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end goal_qty, a.rate as rate,  "
			+" case when a.cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000  "
			+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*rate) end Budget,  "
			+" a.line_item_id as line_item_id, a.line_item_lifetime_impressions as line_item_lifetime_impressions,  "
			+" case when a.cost_type = 'CPD'  "
			+" then (case when a.Line_Item_end_date >= '"+lowerDate+"' and  "
			+" a.Line_Item_end_date < '"+upperDate+"'  "
			+" then (DATEDIFF(a.Line_Item_end_date,a.Line_Item_start_date)*a.rate)  "
			+" when a.Line_Item_end_date >= '"+lowerDate+"'  "
			+" and a.Line_Item_end_date >= '"+upperDate+"'  "
			+" then (DATEDIFF(timestamp('"+upperDate+"'),a.Line_Item_start_date)*a.rate)  "
			+" when a.Line_Item_end_date is null then  "
			+" (DATEDIFF(timestamp('"+upperDate+"'),a.Line_Item_start_date)*a.rate)  "
			+" end)  "
			+" else (a.line_item_lifetime_impressions*a.rate)/1000  "
			+" end Revenue_Delivered,  "
			+" a.line_item_lifetime_clicks as line_item_lifetime_clicks  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a  "
			+" join each  "
			+" (  "
			+" select channel_name, r.line_item_id as line_item_id, r.date as date,  "
			+" r.load_timestamp as load_timestamp , r.zone_id as zone_id,  "
			+" r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r  "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,  "
			+" s.load_timestamp as load_timestamp , s.zone_id as zone_id,  "
			+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  "
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date, t.load_timestamp as load_timestamp ,  "
			+" max(zone_id) as zone_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,  "
			+" max(load_timestamp) as load_timestamp  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" where "
	//		+" Publisher_name = '"+publisherName+"' and  "
			+" not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  ";
			if(lineItem != null && !lineItem.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and line_item in( '"+lineItem+"' )";
			}
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
	//		+" And Publisher_name = '"+publisherName+"'   " 
			+" group each by line_item_id ignore case)v  "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date  "
			+" and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date  "
			+" and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date  "
			+" and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id  "
			+" and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id, channel_name  "
			+" ignore case) b  "
	//		+" on a.Publisher_name = b.Publisher_name  "
			+" on a.Channel_name = b.Channel_name  "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" and a.date = b.date  "
			+" ignore case) group by column ignore case)  "
			+" ignore case  ";
       
		
		log.info("Rich Media Advertiser Trend and Analysis Header Query  :"+QUERY);
		
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		
		
		
		}
			
		
		if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
			 if(rowList != null && rowList.size() > 0) {
				 if(rowList != null && rowList.size() > 1) {
					 TableRow row1 = rowList.get(0);
			    	 List<TableCell> cellList1 = row1.getF();
			    	 if(cellList1 != null && !cellList1.isEmpty()) {
			    		 object.setClicks(cellList1.get(2).getV().toString());
			    		 object.setCTR(cellList1.get(3).getV().toString());
			    		 object.setImpressions(cellList1.get(4).getV().toString());
			    		 object.setRevenue(cellList1.get(5).getV().toString());
			    	 }
				     TableRow row2 = rowList.get(1);
			    	 List<TableCell> cellList2 = row2.getF();
			    	 if(cellList2 != null && !cellList2.isEmpty()) {
			    		 object.setBookedImpressions(cellList2.get(1).getV().toString());
			    		 object.setLifeTimeClicks(cellList2.get(2).getV().toString());
			    		 object.setTotalCTR(cellList2.get(3).getV().toString());
			    		 object.setLifeTimeImpresions(cellList2.get(4).getV().toString());
			    		 object.setRevenueDelivered(cellList2.get(5).getV().toString());
			    		 object.setRevenueRemaining(cellList2.get(6).getV().toString());
			    		 object.setBudget(cellList2.get(7).getV().toString());
			    	 }
				 }
				 else if(rowList != null && rowList.size() == 1 && rowList.get(0).getF().get(0).getV().toString().equalsIgnoreCase("date filter")) {
					 TableRow row1 = rowList.get(0);
			    	 List<TableCell> cellList1 = row1.getF();
			    	 if(cellList1 != null && !cellList1.isEmpty()) {
			    		 object.setClicks(cellList1.get(2).getV().toString());
			    		 object.setCTR(cellList1.get(3).getV().toString());
			    		 object.setImpressions(cellList1.get(4).getV().toString());
			    		 object.setRevenue(cellList1.get(5).getV().toString());
			    		 
			    		 object.setBookedImpressions("0");
			    		 object.setLifeTimeClicks("0");
			    		 object.setTotalCTR("0");
			    		 object.setLifeTimeImpresions("0");
			    		 object.setRevenueDelivered("0");
			    		 object.setRevenueRemaining("0");
			    		 object.setBudget("0");
			    	 }
				 }
				 else if(rowList != null && rowList.size() == 1 && rowList.get(0).getF().get(0).getV().toString().equalsIgnoreCase("Total")) {
					 TableRow row2 = rowList.get(0);
			    	 List<TableCell> cellList2 = row2.getF();
			    	 if(cellList2 != null && !cellList2.isEmpty()) {
			    		 object.setBookedImpressions(cellList2.get(1).getV().toString());
			    		 object.setLifeTimeClicks(cellList2.get(2).getV().toString());
			    		 object.setTotalCTR(cellList2.get(3).getV().toString());
			    		 object.setLifeTimeImpresions(cellList2.get(4).getV().toString());
			    		 object.setRevenueDelivered(cellList2.get(5).getV().toString());
			    		 object.setRevenueRemaining(cellList2.get(6).getV().toString());
			    		 object.setBudget(cellList2.get(7).getV().toString());
			    		 
			    		 object.setClicks("0");
			    		 object.setCTR("0");
			    		 object.setImpressions("0");
			    		 object.setRevenue("0");
			    	 }
				 }
				 trendAnalysisHeaderList.add(object);
			 }
		 }
		
		/* if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 TableRow row1 = rowList.get(0);
			 TableRow row2 = rowList.get(1);
		    	  List<TableCell> cellList1 = row1.getF();
		    	  List<TableCell> cellList2 = row2.getF();
		    	  object= new AdvertiserTrendAnalysisHeaderDTO(
		    				 (cellList2.get(2).getV().toString()),
		    				 (cellList1.get(6).getV().toString()),
		    				 (cellList2.get(6).getV().toString()),
		    				 (cellList1.get(3).getV().toString()), 
		    				 (cellList2.get(3).getV().toString()), 
		    				 (cellList1.get(8).getV().toString()), 
		    				 (cellList1.get(5).getV().toString()),
		    				 (cellList2.get(5).getV().toString()),
		    				 (cellList1.get(7).getV().toString()),
		    				 (cellList2.get(7).getV().toString()),
		    				 (cellList2.get(8).getV().toString())

		    				);
		    	  trendAnalysisHeaderList.add(object);
		    	  
}	*/
		 return trendAnalysisHeaderList;
		 }

	public List<AdvertiserTrendAnalysisActualDTO> loadTrendAnalysisActualDataAdvertiserView(String lowerDate,String upperDate,String campainOrder,String lineItem,String publisherName){
		log.info("in loadTrendAnalysisHeaderDataAdvertiserVew of RichMediaAdvertiserDAO");
		AdvertiserTrendAnalysisActualDTO object = new AdvertiserTrendAnalysisActualDTO();
		double totalRevenueDelivered = 0;
		String lineItemQuery = "";
		if(!lineItem.trim().equals("")) {
			lineItemQuery = " and line_item in ('"+lineItem+"') ";
		}
		List<AdvertiserTrendAnalysisActualDTO> trendAnalysisActualList = new ArrayList<AdvertiserTrendAnalysisActualDTO>();
		//String QUERY = "SELECT Publisher_name, SUM(REQUESTS) Requests,  SUM(SERVED) Served, SUM(SERVED) as Delivered,(sum(total_impressions)/sum(REQUESTS))*100 Fill_Rate, sum(total_clicks) Clicks,(sum(total_clicks)/sum(total_impressions))*100 CTR,sum(total_revenue) Revenue,(sum(total_revenue)/sum(total_impressions))*1000 as ECPM,(sum(total_revenue)/sum(requests))*1000 RPM FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] Where date >='"+lowerDate+"' and date <='"+upperDate+"' And Publisher_name = '"+publisherName+"' And ";
		String QUERY = "select z.line_item, z.order, z.date, ifnull(z.Impression_Delivered,0) as Impression_Delivered, ifnull(z.CTR,0.0) as CTR ,"
			+" ifnull(z.Clicks,0) as Clicks, ifnull(z.revenue_Delivered,0.0) as revenue_Delivered, x.Budget"
			+" from ("
			+" SELECT a.order as  order, a.line_item as line_item, a.line_item_id as line_item_id, date(a.date) date, ifnull(SUM(a.total_impressions),0) as Impression_Delivered," 
			+" sum(a.total_clicks) Clicks,"
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR,"
			+" (sum(Total_revenue)) as revenue_Delivered"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]" 
			+" a"
			+" JOIN EACH"
			+" ("
			+" select date, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'"
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
			+lineItemQuery;
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
	//		+" and Publisher_name = '"+publisherName+"'  "
			+" GROUP EACH BY date, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+" ignore case ) b"
			+" on a.date = b.date"
	//		+" and a.Publisher_name = b.Publisher_name"
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name"
			+" and a.site_type = b.site_type"
			+" and a.zone = b.zone"
			+" and a.advertiser = b.advertiser"
			+" and a.line_item_id = b.line_item_id" 
			+" and a.creative_id = b.creative_id"
			+" and a.load_timestamp = b.load_timestamp"
			+" GROUP EACH BY date, order, line_item,line_item_id ignore case )z "
			+" join each"
			+" (select a.line_item_id as line_item_id, " 
			+" case when cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000" 
			+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget "
			+" from  ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a"
			+" join each ("
			+" select channel_name, r.line_item_id as line_item_id, r.date as date,r.load_timestamp as load_timestamp , r.zone_id as zone_id, r.site_type_id as site_type_id, " 
			+" max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r  "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,  "
			+" s.load_timestamp as load_timestamp , s.zone_id as zone_id,  "
			+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  " 
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,  "
			+" t.load_timestamp as load_timestamp ,  "
			+" max(zone_id) as zone_id   "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,  "
			+" max(load_timestamp) as load_timestamp   "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
			+" where not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
			+lineItemQuery;
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
	//		+" and Publisher_name = '"+publisherName+"'  "
			+" group each by line_item_id ignore case)v   "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date  " 
			+" and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date  " 
			+" and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date  " 
			+" and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id  "
			+" and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id  "
			+" , channel_name  "
			+" ignore case) b  "
	//		+" on a.Publisher_name = b.Publisher_name  "
			+" on a.Channel_name = b.Channel_name   "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id   "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" and a.date = b.date  "
			+" ignore case)x  "
			+" on z.line_item_id = x.line_item_id  " 
			+" order by z.date, z.line_item "
			+" limit 1000 "
			+" ignore case ";
		log.info("Rich Media Advertiser Actual Data Query  :"+QUERY);
		//log.info(lowerDate+" 00:00:00 and "+ upperDate+" 00:00:00"  );
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
		    	  List<TableCell> cellList = row.getF();
		    	  
		    	  totalRevenueDelivered = totalRevenueDelivered + Double.parseDouble(cellList.get(6).getV().toString());
		    	  double budget = Double.parseDouble(cellList.get(7).getV().toString());
		    	  double remaining = budget - totalRevenueDelivered;
		    	  object= new AdvertiserTrendAnalysisActualDTO(
		    				 (cellList.get(2).getV().toString()),
		    				 (cellList.get(1).getV().toString()),
		    				 (cellList.get(0).getV().toString()),
		    				 (cellList.get(3).getV().toString()), 
		    				 (cellList.get(5).getV().toString()), 
		    				 (cellList.get(4).getV().toString()), 
		    				 (cellList.get(6).getV().toString()),
		    				 (String.valueOf(remaining))

		    				);
		    	 
		    	  trendAnalysisActualList.add(object);
			 }
		    	  
}	
		 return trendAnalysisActualList;
		 }
	
	public List<AdvertiserTrendAnalysisForcastDTO> loadTrendAnalysisForcastDataAdvertiserView(String lowerDate,String upperDate,String campainOrder,String lineItem,String publisherName){
		log.info("in loadTrendAnalysisForcastDataAdvertiserView of RichMediaAdvertiserDAO");
		AdvertiserTrendAnalysisForcastDTO object = new AdvertiserTrendAnalysisForcastDTO();
		List<AdvertiserTrendAnalysisForcastDTO> trendAnalysisForcastList = new ArrayList<AdvertiserTrendAnalysisForcastDTO>();
		//String QUERY = "SELECT Publisher_name, SUM(REQUESTS) Requests,  SUM(SERVED) Served, SUM(SERVED) as Delivered,(sum(total_impressions)/sum(REQUESTS))*100 Fill_Rate, sum(total_clicks) Clicks,(sum(total_clicks)/sum(total_impressions))*100 CTR,sum(total_revenue) Revenue,(sum(total_revenue)/sum(total_impressions))*1000 as ECPM,(sum(total_revenue)/sum(requests))*1000 RPM FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] Where date >='"+lowerDate+"' and date <='"+upperDate+"' And Publisher_name = '"+publisherName+"' And ";
		String QUERY = "select z.line_item, z.order, z.date, ifnull(z.Impression_Delivered,0) as Impression_Delivered, ifnull(z.CTR,0.0) as CTR ,"
			+" ifnull(z.Clicks,0) as Clicks, ifnull(z.revenue_Delivered,0.0) as revenue_Delivered, (ifnull(x.Budget,0)-z.revenue_Delivered) as Revenue_Remaing"
			+" from ("
			+" SELECT a.order as  order, a.line_item as line_item, a.line_item_id as line_item_id, date(a.date) date, ifnull(SUM(a.total_impressions),0) as Impression_Delivered," 
			+" sum(a.total_clicks) Clicks,"
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR,"
			+" (sum(Total_revenue)) as revenue_Delivered"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]" 
			+" a"
			+" JOIN EACH"
			+" ("
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'";
			if(lineItem != null && !lineItem.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and line_item in( '"+lineItem+"' )";
			}
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			//+" and Publisher_name = '"+publisherName+"'  "
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
			+" GROUP EACH BY date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+" ignore case ) b"
			+" on a.date = b.date"
			+" and a.Publisher_name = b.Publisher_name"
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name"
			+" and a.site_type = b.site_type"
			+" and a.zone = b.zone"
			+" and a.advertiser = b.advertiser"
			+" and a.line_item_id = b.line_item_id" 
			+" and a.creative_id = b.creative_id"
			+" and a.load_timestamp = b.load_timestamp"
			+" GROUP EACH BY date, order, line_item,line_item_id ignore case )z "
			+" join each"
			+" (select a.line_item_id as line_item_id, " 
			+" case when cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000" 
			+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget "
			+" from  ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a"
			+" join each ("
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date,r.load_timestamp as load_timestamp , r.zone_id as zone_id, r.site_type_id as site_type_id, " 
			+" max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r  "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,  "
			+" s.load_timestamp as load_timestamp , s.zone_id as zone_id,  "
			+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  " 
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,  "
			+" t.load_timestamp as load_timestamp ,  "
			+" max(zone_id) as zone_id   "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,  "
			+" max(load_timestamp) as load_timestamp   "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] " 
			+" where not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  ";
			if(lineItem != null && !lineItem.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and line_item in( '"+lineItem+"' )";
			}
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			//+" and Publisher_name = '"+publisherName+"'  "
			+" group each by line_item_id ignore case)v   "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date  " 
			+" and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date  " 
			+" and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date  " 
			+" and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id  "
			+" and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id  "
			+" , publisher_name, channel_name  "
			+" ignore case) b  "
			+" on a.Publisher_name = b.Publisher_name  "
			+" and a.Channel_name = b.Channel_name   "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id   "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" and a.date = b.date  "
			+" ignore case)x  "
			+" on z.line_item_id = x.line_item_id  " 
			+" order by z.date, z.line_item "
			+" limit 1000 "
			+" ignore case ";
		log.info("actual trends and analysis FORCAST  data advertiser view query   :"+QUERY);
		log.info(lowerDate+" 00:00:00 and "+ upperDate+" 00:00:00"  );
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 int index = 1;
			 for (TableRow row : rowList){
		    	  List<TableCell> cellList = row.getF();
		    	  String dateStr=DateUtil.getModifiedDateStringByDays(new java.util.Date(),index, "dd/MM/yyyy" );
		    	  String s1=cellList.get(3).getV().toString();
		    	  String s2=cellList.get(5).getV().toString();
		    	
		    	  int impression=(int) (Integer.parseInt(s1)* (Integer.parseInt(s2)*(0.2)+1));
		    	  
		    	  int click=(int) (Integer.parseInt(s2)* (Integer.parseInt(s1)*(0.0015)+1));
		    	  
		    	  object= new AdvertiserTrendAnalysisForcastDTO(
		    			     (dateStr),
		    				 (cellList.get(1).getV().toString()),
		    				 (cellList.get(0).getV().toString()),
		    				 (impression+"" ), 
		    				 ((click/impression)*100+""), 
		    				 (click+""), 
		    				 (cellList.get(6).getV().toString()),
		    				 (cellList.get(7).getV().toString())

		    				);
		    	  trendAnalysisForcastList.add(object);
			 }	  
}	
		 return trendAnalysisForcastList;

	}

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
	
	@Override
	public List<AdvertiserByLocationObj> loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		log.info("Inside loadAdvertisersByLocationData of RichMediaAdvertiserDAO");
		AdvertiserByLocationObj advertiserByLocationObj = new AdvertiserByLocationObj();
		List<AdvertiserByLocationObj> advertiserByLocationObjList = new ArrayList<AdvertiserByLocationObj>();
		try{
/*			String QUERY = "";
			QUERY = QUERY
			+" select a.region as region, ifnull(sum(integer(a.ad_server_impressions)),0) as Impression,   "
			+" ifnull((sum(a.ad_server_clicks)/sum(a.ad_server_impressions))*100,0) CTR "
			+" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Performance_By_Location] a  "
			+" join each (  "
			+" SELECT date, publisher_name,Line_item_ID, Agency_ID, advertiser_id, country_id,region_id,city_id, MAX(load_timestamp) as load_timestamp "
			+" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Performance_By_Location] "
			+" where Publisher_name = '"+publisherName+"' and region is not null and region <> 'null'  "
			+" and date >='"+lowerDate+"' and date <='"+upperDate+"'"
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+ agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" group each by date, publisher_name,advertiser_id, line_item_id , Agency_ID,country_id, region_id,city_id ignore case)b " 
			+" on a.date = b.date  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.Publisher_name = b.Publisher_name  "			
			+" and a.advertiser_id = b.advertiser_id  "
			+" and a.Agency_ID = b.Agency_ID  "
			+" and a.line_item_id = b.line_item_id  " 
			+" and a.country_id = b.country_id  "
			+" and a.city_id = b.city_id  "
			+" and a.region_id = b.region_id  " 
			+" GROUP BY region"
			+" ignore case";*/
			
			String QUERY = "";
			QUERY = QUERY
			+" select  "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) as CTR, Column3 as by_market, ifnull(SUM(a.total_impressions),0) as Impressions "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
			+" join each "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where Column3 <> 'null' and "
			+" date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  ";
	//		+" agency ='"+agency+"' and advertiser = '"+advertiser+"' "
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
			QUERY = QUERY + agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
			+" ignore case) b "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" group each by by_market "
			+" ignore case ";
			
			log.info("actual Advertisers By Location Data query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
			 if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  advertiserByLocationObj= new AdvertiserByLocationObj();
						    	  
					    		  advertiserByLocationObj.setCtrPercent(new Double(cellList.get(0).getV().toString()));
					    		  advertiserByLocationObj.setState(cellList.get(1).getV().toString());
					    		  advertiserByLocationObj.setImpression(new Long(cellList.get(2).getV().toString()));
					    		  
						    		 
						    	  advertiserByLocationObjList.add(advertiserByLocationObj);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertisersByLocationData of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserByLocationObjList;
	}

	@Override
	public List<AdvertiserByLocationObj> loadAdvertiserByMarketGraphData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		
		log.info("Inside loadAdvertiserByMarketGraphData of RichMediaAdvertiserDAO");
		AdvertiserByLocationObj advertiserByLocationObj = new AdvertiserByLocationObj();
		List<AdvertiserByLocationObj> advertiserByLocationObjList = new ArrayList<AdvertiserByLocationObj>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select "
			+" ifnull(f.ctr,0.0) CTR, "
			+" case when g.state_name is null then 'US' "
			+" when g.state_name = 'NA' then 'US' "
			+" else g.state_name  "
			+" end state_name, "
			+" case when g.station_name is null then site_name  "
			+" when g.station_name = 'NA' then site_name  "
			+" else g.station_name "
			+" end station_name,  "
			+" total_impressions  "
			+" from ( "
			+" SELECT a.site_name as site_name, sum(a.total_impressions) as total_impressions, (sum(a.total_clicks)/sum(a.total_impressions))*100 CTR" 
			+" from "
			+"  (select * "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" a  "
			+" join each "
			+" (  "
			+" select date, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where date >='"+lowerDate+"' and date <='"+upperDate+"' and "
			+" not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')"
			+ agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and Channel_name <> 'House' ";
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
			+" group each by date, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+"  ignore case) b "
			+" on a.date = b.date "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone  "
			+" and a.advertiser = b.advertiser  "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+"  ignore case)				  "
			+" group by site_name "
			+" ignore case)f "
			+" left join "
			+" (select dfp_property_name , station_name, state_name  from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Property_Name])g "
			+" on f.site_name = g.dfp_property_name "
			+" order by state_name "
			+" ignore case ";
			
			log.info("actual Advertisers By Market Data query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
			 if (queryResponse!=null && queryResponse.getRows() != null) {
				 List<TableRow> rowList = queryResponse.getRows();
				 if(rowList != null && rowList.size() > 0) {
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  advertiserByLocationObj= new AdvertiserByLocationObj();
						    	  
					    		  advertiserByLocationObj.setCtrPercent(new Double(cellList.get(0).getV().toString()));
					    		  advertiserByLocationObj.setState(cellList.get(1).getV().toString());
					    		  advertiserByLocationObj.setStation(cellList.get(2).getV().toString());
					    		  advertiserByLocationObj.setImpression(new Long(cellList.get(2).getV().toString()));
						    		 
						    	  advertiserByLocationObjList.add(advertiserByLocationObj);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertiserByMarketGraphData of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserByLocationObjList;
	}

	@Override
	public void getLineItemForPopUP(String lowerDate, String upperDate, String lineItemName, PopUpDTO popUpObj,String publisherName,String properties) throws Exception {
		log.info("Inside getLineItemForPopUP of RichMediaAdvertiserDAO");
		try{
			// graph data
			String QUERY1 = "" 
			+" select 	date(date) date, sum(Total_Impressions) as Impressions_Delivered "
			+" from(  "
			+" select a.date as date, a.Total_Impressions as Total_Impressions  "
			+" , a.line_item as line_item,a.Publisher_name as Publisher_name,a.line_item_id as line_item_id "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a  "
			+" JOIN EACH  "
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" and date >='"+lowerDate+"' and date <='"+upperDate+"'  and advertiser <> 'LIN Mobile - Golfsmith'  "
			+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
			+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
			//+" And Publisher_name = '"+publisherName+"' "
			+" and line_item = '"+lineItemName+"' "
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+" ignore case) b "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name  "
			+" and a.Channel_name = b.Channel_name  "
			+" and a.site_name = b.site_name  "
			+" and a.site_type = b.site_type  "
			+" and a.zone = b.zone  "
			+" and a.advertiser = b.advertiser  "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" ignore case) "
			+" GROUP EACH BY date "
			+" order by date";
			
			// pop up data
			String QUERY2 = ""
				+" select * from (  "
				+" select 'date filter' as column , "
				+" integer(0) as Booked_impression, "
				+" 0.0 as budget, "
				+" ifnull(sum(Total_clicks),0) as Clicks ,	  "
				+" ifnull((sum(Total_clicks)/sum(Total_Impressions))*100,0) CTR,  "
				+" ifnull((sum(Total_Impressions)),0) as Impression_delivered,  "
				+" ifnull((sum(Total_revenue)),0.0) as revenue_Delivered, "
				+" 0.0 as  revenue_remaining, "
				+" ifnull((sum(total_revenue)/sum(total_impressions))*1000,0) as CPM, cost_type, order "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a  "
				+" join each  "
				+" (  "
				+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
				+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'  and advertiser <> 'LIN Mobile - Golfsmith'  "
				+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
				+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
				//+" and Publisher_name = '"+publisherName+"' "
				+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
				+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
				+" and line_item = '"+lineItemName+"' "
				+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
				+" ignore case) b "
				+" on a.date = b.date "
				+" and a.Publisher_name = b.Publisher_name  "
				+" and a.Channel_name = b.Channel_name  "
				+" and a.site_name = b.site_name  "
				+" and a.site_type = b.site_type  "
				+" and a.zone = b.zone  "
				+" and a.advertiser = b.advertiser  "
				+" and a.line_item_id = b.line_item_id  "
				+" and a.creative_id = b.creative_id  "
				+" and a.load_timestamp = b.load_timestamp  "
				+" group by column, cost_type, order ignore case), "
				+" (select 'Total' as column ,  "
				+" ifnull(sum(goal_qty),0) as Booked_impression,  "
				+" ifnull(sum(Budget),0.0) Budget,  "
				+" ifnull(sum(line_item_lifetime_clicks),0) as Clicks ,	  "
				+" ifnull((sum(line_item_lifetime_clicks)/sum(line_item_lifetime_impressions))*100,0.0) CTR,  "
				+" ifnull(sum(line_item_lifetime_impressions),0) as Impression_delivered, "
				+" ifnull(sum(Revenue_Delivered),0.0) as revenue_Delivered, "
				+" ifnull((sum(Budget)-(sum(Revenue_Delivered))),0.0) revenue_remaining,  "
				+" 0.0 as CPM, cost_type, order "
				+" from(  "
				+" select cost_type,case when cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(goal_qty,0)),'-1','0')) else 0 end goal_qty, rate,  "
				+" case when cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(goal_qty,0)),'-1','0'))*rate)/1000   "
				+" else (integer(REGEXP_REPLACE(string(ifnull(goal_qty,0)),'-1','0'))*rate) end Budget, line_item_id, line_item_lifetime_impressions, "
				+" case when cost_type = 'CPD'  "
				+" then (case when Line_Item_end_date >= '"+lowerDate+"' and  "
				+" Line_Item_end_date < '"+upperDate+"'  "
				+" then (DATEDIFF(Line_Item_end_date,Line_Item_start_date)*rate) "
				+" when Line_Item_end_date >= '"+lowerDate+"'  "
				+" and Line_Item_end_date >= '"+upperDate+"'   "
				+" then (DATEDIFF(timestamp('"+upperDate+"'),Line_Item_start_date)*rate)  "
				+" when Line_Item_end_date is null then  "
				+" (DATEDIFF(timestamp('"+upperDate+"'),Line_Item_start_date)*rate) end) "
				+" else (line_item_lifetime_impressions*rate)/1000 end Revenue_Delivered," 
				+" line_item_lifetime_clicks, order, row_number() over(partition by line_item_id order by " 
				+" date desc, load_timestamp desc) as id "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
				+" where  line_item = '"+lineItemName+"'  and advertiser <> 'LIN Mobile - Golfsmith' "
				+" and not(channel_name = 'Celtra' and load_timestamp >='2013-09-05 14:01:25' and load_timestamp < '2013-09-10 00:16:17')  "
				+" and not(channel_name = 'XAd' and load_timestamp >='2013-09-05 14:23:44' and load_timestamp < '2013-09-10 17:40:06')  "
				//+" and Publisher_name = '"+publisherName+"'  "
				+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
				+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
				+" ignore case )z "
				+" where id = 1 "
				+" group by column, cost_type, order ignore case)  "
				+" ignore case  ";
			
			log.info("actual LineItem Graph For PopUP query  :"+QUERY1);
			log.info("actual LineItem For PopUP query  :"+QUERY2);
			
			QueryResponse queryResponse1 = null;
			QueryResponse queryResponse2 = null;
			
			
			int j=0;
			do{
	             try {
					queryResponse1 = BigQueryUtil.getBigQueryData(QUERY1);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse1.getJobComplete() && j<=3);
			
			j=0;
			do{
	             try {
					queryResponse2 = BigQueryUtil.getBigQueryData(QUERY2);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse2.getJobComplete() && j<=3);
			
			 if (queryResponse1!=null && queryResponse1.getRows() != null) {
				 List<TableRow> rowList = queryResponse1.getRows();
				 if(rowList != null && rowList.size() > 0) {
					 List<CommonDTO> list = new ArrayList<CommonDTO>();
				      for (TableRow row : rowList) {
				    	  if(row != null && !row.isEmpty()) {
					    	  List<TableCell> cellList = row.getF();
					    	  if(cellList != null && cellList.size() > 0) {
					    		  list.add(new CommonDTO("", "", cellList.get(0).getV().toString(), cellList.get(1).getV().toString()));
					    	  }
				    	  }				    
				      }
				      popUpObj.setPopUpGraphDataList(list);
				 }
			 }	
			 if (queryResponse2!=null && queryResponse2.getRows() != null) {
				 List<TableRow> rowList = queryResponse2.getRows();
				 if(rowList != null && rowList.size() > 0) {
					 TableRow row1 = rowList.get(0);
			    	 if(row1 != null && !row1.isEmpty()) {
				    	  List<TableCell> cellList = row1.getF();
				    	  if(cellList != null && cellList.size() > 0) {
				    		popUpObj.setClicksInSelectedTime(cellList.get(3).getV().toString());
				    		popUpObj.setCostType(cellList.get(9).getV().toString());
				    		popUpObj.setCtrInSelectedTime(cellList.get(4).getV().toString());
							popUpObj.setImpressionDeliveredInSelectedTime(cellList.get(5).getV().toString());
							popUpObj.setRevenueDeliveredInSelectedTime(cellList.get(6).getV().toString());
							popUpObj.setRevenueRemainingInSelectedTime(cellList.get(7).getV().toString());
							popUpObj.seteCPM(cellList.get(8).getV().toString());
							popUpObj.setOrder(cellList.get(10).getV().toString());
				    	  }
			    	  }
				     TableRow row2 = rowList.get(1);
				     if(row2 != null && !row2.isEmpty()) {
				    	  List<TableCell> cellList = row2.getF();
				    	  if(cellList != null && cellList.size() > 0) {
				    		popUpObj.setBookedImpression(cellList.get(1).getV().toString());
				    		popUpObj.setClicksLifeTime(cellList.get(3).getV().toString());
				    		popUpObj.setCtrLifeTime(cellList.get(4).getV().toString());
				    		popUpObj.setImpressionDeliveredLifeTime(cellList.get(5).getV().toString());
				    		popUpObj.setRevenueDeliveredLifeTime(cellList.get(6).getV().toString());
				    		popUpObj.setRevenueRemainingLifeTime(cellList.get(7).getV().toString());
				    		popUpObj.setOrder(cellList.get(10).getV().toString());
				    	  }
			    	  }
				 }
			 }
		}catch(Exception e) {
			log.severe("Exception getLineItemForPopUP of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
		public static String getReportForLineItem(String lineItemId) throws Exception{
			 //log.info("Get line item..."+lineItemId);
			 List<LineItem> lineItemList=new ArrayList<LineItem>();
			 LineItem line=new LineItem();
			 //line.
			 LineItemService lineItemService=new LineItemService();
			 LineItemServiceInterface lineItemServiceInterface=lineItemService.getLineItemServiceInterfacePort();
			 //LineItem lineItem=lineItemServiceInterface.getLineItem(Long.parseLong(lineItemId), requestHeader, null);
			 
			 String targetPlatform="MOBILE";
		     String costType="CPM";
		      //String endDate = "";
		      //String startDate = "";
		      Date startDate = new Date();
		      startDate.setMonth(3);
		      startDate.setDay(1);
		      startDate.setYear(2013);
		      Date endDate = new Date();
		      endDate.setDay(30);
		      endDate.setMonth(6);
		      endDate.setYear(2013);
		      
			 Long[] orderIdArray = { Long.parseLong("129822342"),
							Long.parseLong("134664942"), Long.parseLong("138517662"),
							Long.parseLong("141872742"), Long.parseLong("141877902"),
							Long.parseLong("142127862"), Long.parseLong("143171142"),
							Long.parseLong("143217822"), Long.parseLong("143655822"),
							Long.parseLong("144784902") };
			  
			 Statement filterStatement=new Statement();
			 Long orderId=null;
			 
			 for(int i=0;i<orderIdArray.length;i++){
				  
		    	  orderId=orderIdArray[i];
		    	  log.info("OrderId: "+orderId);
		    	  
		    	  String query="WHERE  orderId = '"+orderId+"' AND status = '"
		              +ComputedStatus.DELIVERING+"' AND targetPlatform = '"
		              +targetPlatform+"' AND costType= '"
		              +costType+"' AND endDateTime='"+endDate+"' AND startDateTime='"+startDate+"'";
		    	  log.info("query:"+query);
		    	  //String query= "WHERE orderId= '129822342' AND status = 'DELIVERING' ";
		          filterStatement.setQuery(query);
		         
		          LineItemPage page=lineItemServiceInterface.getLineItemsByStatement(filterStatement);
		          if(page.getResults()!=null){
		        	  for(LineItem lineItem : page.getResults()){
		        		  lineItemList.add(lineItem);
		        	  }
		        	  log.info("Found line Items:"+lineItemList.size());
		          }else{
		        	  log.warning("No line items found for query:"+query);
		          }
			 }
			 
			 String uploadedFileURL="";
			 return uploadedFileURL;
		}

		@Override
		public List<AdvertiserReallocationHeaderDTO> loadReallocationHeaderDataAdvertiserView(
				String lowerDate, String upperDate, String canpainOrder,
				String lineItem) {
			
			return null;
		}
	
		public List<AgencyDTO> getAgencyDropDownList(String publisherId, String str, String agencyIdString) throws Exception {
			List<AgencyDTO> agencies = new ArrayList<AgencyDTO>();

			/*String QUERY = 
				" SELECT  agency_id, agency  "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
				+" join each " 
				+" ( "
				+" select  data_source, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
				+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
				+" group each by data_source, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
				+" ignore case) b "
				+" on  a.Channel_name = b.Channel_name  "
				+" and a.data_source = b.data_source "
				+" and a.site_name = b.site_name "
				+" and a.site_type = b.site_type "
				+" and a.zone = b.zone "
				+" and a.advertiser = b.advertiser "
				+" and a.line_item_id = b.line_item_id  "
				+" and a.creative_id = b.creative_id "
				+" and a.load_timestamp = b.load_timestamp "
				+" where agency is not null and agency <> 'null' and agency <> '-0' and agency <> '-' and agency contains '" + str + "'"				
				+" group each by agency_id, agency "
				+" order by agency limit 20 ignore case ";*/
				
				StringBuilder QUERY = new StringBuilder();
				QUERY.append("select Agency_ID,agency from [")
				.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID )
				.append(".Core_Performance] where agency is not null ");
				if(!agencyIdString.contains(LinMobileConstants.ALL_AGENCIES)) {
					QUERY.append(" and Agency_ID in ('")
					.append(agencyIdString)
					.append("')");
				}
				/*QUERY.append(" and Pubisher_ID = '")
				.append(publisherId+"' ");*/
				QUERY.append(" and agency <> 'null' and agency <> '-0' and agency <> '-' and agency contains '")
				.append(str)
				.append("' GROUP BY agency,Agency_ID order by agency limit 20 ignore case");
			/*String QUERY1 ="select Agency_ID,agency from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
					+ "  agency is not null and agency <> 'null' and agency <> '-0' and agency <> '-' and agency contains '"
					+ str
					+ "' GROUP BY agency,Agency_ID order by agency limit 20 ignore case";*/
			
			
			
			 log.info("GET AGENCY DROP DOWN LIST   QUERY = "+QUERY);

			
			QueryResponse queryResponse = null;
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);

			if (queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
				try {
					for (TableRow row : rowList) {
						List<TableCell> cellList = row.getF();
						AgencyDTO agncyObj = new AgencyDTO();
						agncyObj.setAgencyId(new Long(cellList.get(0).getV()
								.toString()));
						agncyObj.setAgencyName(cellList.get(1).getV().toString());

						agencies.add(agncyObj);
					}
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}

			}
			return agencies;
		}

		public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherId ,String agencyName, String str, String advertiserIdString) throws Exception {
			log.info("inside getAdvertiserDropDownList() of RichMediaAdvertiserDao...");
			List<AdvertiserDTO> advertisers = new ArrayList<AdvertiserDTO>();
			/*String QUERY = null;
			if (agencyName == null || agencyName.trim().equals(""))
				QUERY = "select Advertiser_ID,Advertiser from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
						+ " Advertiser is not null  and advertiser <> 'LIN Mobile - Golfsmith' and Advertiser <> 'null' and Advertiser contains '"
						+ str
						+ "' GROUP  BY Advertiser,Advertiser_ID order by Advertiser ignore case;";
			else
				QUERY = "select Advertiser_ID,Advertiser from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
						+ " Advertiser is not null  and advertiser <> 'LIN Mobile - Golfsmith' and Advertiser <> 'null' and Advertiser contains '"
						+ str
						+ "' and agency = '"
						+ agencyName
						+ "' GROUP  BY Advertiser,Advertiser_ID order by Advertiser ignore case;";*/
			StringBuilder QUERY = new StringBuilder();
			
			QUERY.append("select Advertiser_ID,Advertiser from [")
				.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
				.append(".Core_Performance] where Advertiser is not null ");
				if(!advertiserIdString.contains(LinMobileConstants.ALL_ADVERTISERS)) {
					QUERY.append(" and Advertiser_ID in ('")
					.append(advertiserIdString)
					.append("')");
				}
			/*QUERY.append(" and Pubisher_ID = '")
			.append(publisherId+"' ");*/
				if (agencyName != null && !agencyName.trim().equals("")) {
					QUERY.append(" and agency = '")
					.append(agencyName)
					.append("' ");
				}
			QUERY.append("  and advertiser <> 'LIN Mobile - Golfsmith' and Advertiser <> 'null' and Advertiser contains '")
				.append(str)
				.append("' GROUP  BY Advertiser,Advertiser_ID order by Advertiser limit 20 ignore case");
			
/*			if (agencyName == null || agencyName.trim().equals(""))
				QUERY = "SELECT "
						+" advertiser, advertiser_id "
						+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  a "
						+" join each "
						+" ( "
						+" select  Channel_name, site_name, site_type, zone, agency, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
						+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
						+" group each by  Channel_name, site_name, site_type, zone, agency, line_item_id, creative_id "
						+" ignore case) b "
						+" on  a.Channel_name = b.Channel_name " 
						+" and a.site_name = b.site_name "
						+" and a.site_type = b.site_type "
						+" and a.zone = b.zone "
						+" and a.agency = b.agency "
						+" and a.line_item_id = b.line_item_id " 
						+" and a.creative_id = b.creative_id "
						+" and a.load_timestamp = b.load_timestamp "
						+" where a.Advertiser is not null and a.Advertiser <> 'null' and a.Advertiser contains  '"+str+"' "
						+" group each by Advertiser,advertiser_id "
						+" ignore case";
			else
				QUERY = "SELECT "
					+" advertiser, advertiser_id "
					+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  a "
					+" join each "
					+" ( "
					+" select  Channel_name, site_name, site_type, zone, agency, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
					+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
					+" group each by  Channel_name, site_name, site_type, zone, agency, line_item_id, creative_id "
					+" ignore case) b "
					+" on  a.Channel_name = b.Channel_name " 
					+" and a.site_name = b.site_name "
					+" and a.site_type = b.site_type "
					+" and a.zone = b.zone "
					+" and a.agency = b.agency "
					+" and a.line_item_id = b.line_item_id " 
					+" and a.creative_id = b.creative_id "
					+" and a.load_timestamp = b.load_timestamp "
					+" where a.Advertiser is not null and a.Advertiser <> 'null' and a.Advertiser contains  '"+str+"' " 
					+" and agency = '"
					+ agencyName
					+"' group each by Advertiser,advertiser_id "
					+" ignore case";*/
		

			log.info("Advertiser Drop Down List Query = "+QUERY.toString());
			
			QueryResponse queryResponse = null;
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);

			if (queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
				try {
					for (TableRow row : rowList) {
						AdvertiserDTO advObj = new AdvertiserDTO();
						List<TableCell> cellList = row.getF();
						advObj.setAdvertiserId(new Long(cellList.get(0).getV()
								.toString()));
						advObj.setAdvertiserName(cellList.get(1).getV().toString());

						advertisers.add(advObj);
					}
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}

			}
			return advertisers;
		}

		public List<OrderDTO> getOrderDropDownList(String publisherName,
				String agencyName, String advertiserName, String str) {
			List<OrderDTO> orders = new ArrayList<OrderDTO>();

			String QUERY = null;
			
			if (advertiserName == null || advertiserName.equals(""))
				QUERY = "select Order_ID,order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
						+ " order is not null and order <> 'null' and order contains '"
						+ str + "' GROUP  BY order,Order_ID order by order ignore case";
			else
				QUERY = "select Order_ID,order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
						+ " order is not null and order <> 'null' and order contains '"
						+ str
						+ "' and (Advertiser = '"
						+ advertiserName
						+ "' ) GROUP  BY order,Order_ID order by order ignore case";

			log.info("ORDER DROP DOWN QUERY : "+QUERY);
			
			QueryResponse queryResponse = null;
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);

			if (queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
				try {
					for (TableRow row : rowList) {
						OrderDTO odrObj = new OrderDTO();
						List<TableCell> cellList = row.getF();
						odrObj.setOrderId(new Long(cellList.get(0).getV()
								.toString()));
						odrObj.setOrderName(cellList.get(1).getV().toString());

						orders.add(odrObj);
					}
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}

			}
			return orders;
		}

		public List<LineItemDTO> getLineItemDropDownList(String publisherName,
				String agencyName, String advertiserName, String orderName,
				String str) {
			List<LineItemDTO> lineItems = new ArrayList<LineItemDTO>();

			String QUERY = null;
			if (orderName == null || orderName.equals(""))
			{

			            QUERY = "select Line_item_ID, line_item, order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
			                    + " line_item is not null and line_item <> 'null' and line_item contains '"
			                    + str
			                    + "' GROUP  BY line_item,Line_item_ID ,order order by line_item limit 20 ignore case";
			}
			        else
			{            QUERY = "select Line_item_ID,line_item, order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where "
			                    + " line_item is not null and line_item <> 'null' and line_item contains '"
			                    + str
			                    + "' and (order = '"
			                    + orderName
			                    + "' ) GROUP  BY line_item,Line_item_ID ,order order by line_item limit 20 ignore case";
			}
			
			log.info("LINE ITEM DROP DOWN QUERY = "+QUERY);

			
			QueryResponse queryResponse = null;
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);

			if (queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
				try {
					for (TableRow row : rowList) {
						LineItemDTO lineObj = new LineItemDTO();

						List<TableCell> cellList = row.getF();
						lineObj.setLineItemId(new Long(cellList.get(0).getV()
								.toString()));
						lineObj.setName(cellList.get(1).getV().toString());
						lineObj.setOrderName(cellList.get(2).getV().toString());
						
						lineItems.add(lineObj);
					}
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}

			}
			return lineItems;
		}
	
		public List<String> loadAdvertiserDFPPropertyList(String publisherName,long userId,String term) {
			List<String> siteList = new ArrayList<String>();
			List<String> propertyIdList = new ArrayList<String>();
			IUserDetailsDAO dao = new UserDetailsDAO();
			try {
				UserDetailsObj userDetailsObj = dao.getUserById(userId);
				if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherName != null && !publisherName.trim().equals("")) {
					CompanyObj publisherCompany = dao.getCompanyObjByNameAndCompanyType(publisherName, LinMobileConstants.COMPANY_TYPE[0], MemcacheUtil.getAllCompanyList());
					if(publisherCompany != null && publisherCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
						String publisherId = publisherCompany.getId()+"";
						List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
						propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(publisherCompany.getId()+""));
						/*List<CompanyObj> publisherCompanyList = new ArrayList<CompanyObj>();
				 		 publisherCompanyList.add(publisherCompany);
				 		 Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(publisherCompanyList);
				 		 if(map != null && !map.isEmpty()) {
				 			List<PropertyEntityObj> propertyObjList = new ArrayList<PropertyEntityObj>();
							for(String key : map.keySet()) {
								AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
								if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null) {
									propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerUsername()));
								}
							}*/
							List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
							 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
							if(roleName!= null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
								if(propertyObjList != null && propertyObjList.size() > 0) {
									for (PropertyObj propertyObj : propertyObjList) {
										if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
											siteList.add(propertyObj.getDFPPropertyName());
										}
									}
								}
							}
							else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {		 
								 List<String> teamIdList = userDetailsObj.getTeams();
								 if(teamIdList != null && teamIdList.size() > 0) {
									 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
									 for (String teamId : teamIdList) {
										 if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
											 siteList.clear();
											 break;
										 }
										 else if(teamId.startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
											 siteList.clear();
											 TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId, teamPropertiesObjList);
											 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(publisherId.trim())) {
												 if(propertyObjList != null && propertyObjList.size() > 0) {
													 for (PropertyObj propertyObj : propertyObjList) {
														 if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
															 siteList.add(propertyObj.getDFPPropertyName());
														}
													 }
												 }
											 }
											 break;
										 }
										 else {
											TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId, teamPropertiesObjList);
											if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(publisherId)) {
												List<String> tempList = teamPropertiesObj.getPropertyId();
												if(tempList != null && tempList.size() > 0 && tempList.contains(LinMobileConstants.ALL_PROPERTIES)) {
													siteList.clear();
													if(propertyObjList != null && propertyObjList.size() > 0) {
														for (PropertyObj propertyObj : propertyObjList) {
															if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
																siteList.add(propertyObj.getDFPPropertyName());
															}
														}
													}
													break;
												}
												else if(tempList != null && tempList.size() > 0) {
													for (String propertyId : tempList) {
														if(!propertyIdList.contains(propertyId.trim())) {
															propertyIdList.add(propertyId.trim());
															PropertyObj propertyObj = dao.getPropertyObjById(propertyId.trim(), propertyObjList);												
															if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
																siteList.add(propertyObj.getDFPPropertyName());
															}
														}
													}
												}
											}
										}
									 }
								 }							
							}
				 		//}
					}
				}
			}
			 catch (Exception e) {
				log.severe("Exception in loadAdvertiserDFPPropertyList in AdvertiserDAO"+e.getMessage());
				e.printStackTrace();
			}
		 	return siteList;
		
		}	
	
	@Override
	public PropertyObj getPropertyObjByName(String propertyName) {
		log.info("UserDetailsDAO : getPropertyObjByName..");
		PropertyObj propertyObj = null;
		/*try {
			List<PropertyObj> resultList = MemcacheUtil.getAllPropertiesList();		
			if(resultList !=null && resultList.size() > 0){
				for (PropertyObj obj : resultList) {
					if(obj != null && obj.getPropertyName().trim().equals(propertyName.trim())) {
						propertyObj = obj;
						break;
					}
				}
			}
		}catch (Exception e) {
			log.severe("Exception in getPropertyObjByName of RichMediaAdvertiserDAO"+e.getMessage());
			e.printStackTrace();
		}*/
		return propertyObj;
	}
	
	public List<AdvertiserPerformerDTO> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency,String properties)
	{
		log.info("Inside loadAdvertiserTotalDataList of RichMediaAdvertiserDAO"+advertiser+" "+agency+" "+properties);
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
				String QUERY = " select REGEXP_REPLACE(ifnull(a.order,''),'null','') as order, "
				+" a.line_item_id as line_item_id, "
				+" REGEXP_REPLACE(ifnull(a.line_item,''),'null','') as line_item, "
				+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered, "
				+" ifnull(sum(a.total_clicks),0) Clicks, "
				+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR, "
				+" (sum(a.Total_revenue)) as revenue_Delivered, "
				+" a.advertiser as advertiser, a.agency as agency, "
				+" a.creative_type as creative_type , a.column3 as market, "
				+" (sum(Delivery_Indicator)/count(line_item_id)) as d, "
				//+" (sum(Delivery_Indicator)/(DATEDIFF(TIMESTAMP('"+upperDate+" 00:00:00'),TIMESTAMP('"+lowerDate+" 00:00:00'))+1)) "
				+"  case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end as booked_impression, "
				+" ((case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end )*a.rate)/1000 as budget  "
				+", a.Publisher_Name as Publisher_Name ,a.Site_Name as Site_Name"
				+" from ( "
				+" select a.order as order, a.line_item_id as line_item_id, a.line_item as line_item, "
				+" a.advertiser as advertiser, a.agency as agency, "
				+" a.total_impressions as Impression_Delivered, a.total_clicks as Clicks, "
				+" a.Total_revenue as revenue_Delivered, a.creative_type as creative_type, "
				+"  a.column3 as market, a.Delivery_Indicator as Delivery_Indicator,  a.cost_type as  cost_type, a.goal_qty as goal_qty ,a.rate as rate ,a.Publisher_Name as Publisher_Name ,a.Site_Name as Site_Name  "
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
				+" group each by line_item, line_item_id, order, advertiser, agency, creative_type, market, booked_impression, budget,Publisher_Name ,Site_Name "
				+" order by CTR desc, line_item "
				+" ignore case";
			
			log.info("Rich Media Advertiser Total Data List Query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
			
			
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
			    				  ""
			    				  );
					    		  
					    		  
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertiserTotalDataList of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserTotalDataList;
	}
	
	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{

		log.info("Inside loadDeliveryIndicatorData of RichMediaAdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "select a.line_item_id as line_item_id , "
				+" case when a.cost_type = 'CPM' then "
				+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100) " 
				+" else 0 end Life_Time_CTR , "
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
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
			
			
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
			    				  new Double(cellList.get(3).getV().toString())
			    				  );
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadDeliveryIndicatorData of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserTotalDataList;
	
	}
	
	
	@Override
	public List<AdvertiserPerformerDTO> loadRichMediaEventPopup(String lowerDate, String upperDate, String lineItemName) throws Exception {
		log.info("Inside loadRichMediaEventPopup of RichMediaAdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> richMediaEventPopupList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "select a.market as market,  a.custom_event as custom_event, sum(ifnull(a.custom_count_value,0)) as value, a.site_name as site_name, a.campaign_category as campaign_category, " 
				+" from [LIN_DEV.Rich_Media_Custom_Event] a   "
				+" join each  "
				+" (  "
				+" select date, channel_name, sales_type, data_source, publisher_name, site_name,  " 
				+" advertiser, order, line_item, Max(load_timestamp) as load_timestamp   "
							+" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Rich_Media_Custom_Event] "
							+" where line_item = '"+lineItemName+"' " 
							+" and custom_event <> 'null' "
							+" and date >= '"+lowerDate+"' and date <='"+upperDate+"' "
							+" group each by date, channel_name, sales_type, data_source, publisher_name, site_name, advertiser, order, line_item "
							+" ignore case) b  "
							+" on a.data_source = b.data_source  "
							+" and a.Publisher_name = b.Publisher_name  "
							+" and a.Channel_name = b.Channel_name   "
							+" and a.site_name = b.site_name  "
							+" and a.advertiser = b.advertiser  "
							+" and a.order = b.order  "
							+" and a.line_item = b.line_item  " 
							+" and a.load_timestamp = b.load_timestamp  "
							+" and a.date = b.date  "
							+" group each by market, site_name, campaign_category, custom_event  "
							+" ignore case  ";
			
			log.info("loadRichMediaEventPopup Data query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
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
					    		  advertiserPerformerDTO= new AdvertiserPerformerDTO();
					    		  String customizedEventValue = cellList.get(1).getV().toString();
					    		  if(customizedEventValue.trim().startsWith("http")) {
					    			  customizedEventValue = "URL Clicked";
					    		  }
					    		  else if(customizedEventValue.trim().length() > 22) {
					    			  customizedEventValue = customizedEventValue.trim().substring(0, 20) +"..";
					    		  }
					    		  advertiserPerformerDTO.setCustomizedCustomEvent(customizedEventValue);
					    		  advertiserPerformerDTO.setCustomEvent(cellList.get(1).getV().toString());
					    		  advertiserPerformerDTO.setValue(cellList.get(2).getV().toString());
			    		 
					    		  richMediaEventPopupList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadRichMediaEventPopup of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return richMediaEventPopupList;
	}
	@Override
	public List<AdvertiserPerformerDTO> loadRichMediaEventGraph(String lowerDate, String upperDate,String advertiser, String agency) throws Exception {
		log.info("Inside loadRichMediaEventGraph of RichMediaAdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = null;
		List<AdvertiserPerformerDTO> richMediaEventGraphList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "SELECT ifnull(a.market,'NA') as market, ifnull(a.site_name,'NA') as site_name, ifnull(a.campaign_category,'NA')  as campaign_category, ifnull(a.custom_event,'NA') as custom_event, sum(ifnull(a.custom_count_value,0)) as value "
                +" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Rich_Media_Custom_Event] a " 
                +"  join each "
                +" ( "
                +" select date, channel_name, sales_type, data_source, site_name,  "
                +" advertiser, order, line_item, Max(load_timestamp) as load_timestamp  "
                +" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Rich_Media_Custom_Event] "
                +" where  date >= '"+lowerDate+"' and date <= '"+upperDate+"' "
                +  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
                +" group each by date, channel_name, sales_type, data_source, site_name, " 
                +" advertiser, order, line_item ignore case) b "
                +" on a.data_source = b.data_source "
                +" and a.Channel_name = b.Channel_name  "
                +" and a.site_name = b.site_name "
                +" and a.advertiser = b.advertiser "
                +" and a.order = b.order "
                +" and a.line_item = b.line_item " 
                +" and a.load_timestamp = b.load_timestamp "
                +" and a.date = b.date "
                +" group each by  market, campaign_category, custom_event, site_name "
                +" order by  custom_event"
                +" ignore case ";
			log.info("loadRichMediaEventGraph Data query  :"+QUERY);
			
			QueryResponse queryResponse = null;
			
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(QUERY);
				} catch (Exception e) {
					log.severe("Query Exception = " + e.getMessage());
					e.printStackTrace();
				}
				j++;
			}while(!queryResponse.getJobComplete() && j<=3);
			
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
					    		  advertiserPerformerDTO= new AdvertiserPerformerDTO();
					    		  advertiserPerformerDTO.setMarket(cellList.get(0).getV().toString());
					    		  advertiserPerformerDTO.setSiteName(cellList.get(1).getV().toString());
					    		  advertiserPerformerDTO.setCampaignCategory(cellList.get(2).getV().toString());
					    		  advertiserPerformerDTO.setCustomEvent(cellList.get(3).getV().toString());
					    		  advertiserPerformerDTO.setValue(cellList.get(4).getV().toString());
					    		  richMediaEventGraphList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadRichMediaEventGraph of RichMediaAdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		return richMediaEventGraphList;
		
	}
	
}



