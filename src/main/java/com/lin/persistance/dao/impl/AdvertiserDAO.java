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
import com.lin.persistance.dao.IAdvertiserDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdvertiserDTO;
import com.lin.web.dto.AdvertiserPerformanceSummaryHeaderDTO;
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
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;


public class AdvertiserDAO implements IAdvertiserDAO {
	
	private static final Logger log = Logger.getLogger(AdvertiserDAO.class.getName());
	private Objectify obfy = OfyService.ofy();	

	public void saveObject(Object obj) throws DataServiceException {
		//obfy.put(obj);
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
		
		if(!lineItem.trim().equalsIgnoreCase("") && !campainOrder.trim().equalsIgnoreCase("") ){
		log.info("in loadTrendAnalysisHeaderDataAdvertiserVew of AdvertiserDAO");
		
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
			+" select date, Publisher_name, Channel_name, "
			+" site_name, site_type, zone, advertiser, line_item_id, "
			+" creative_id, MAX(load_timestamp) as load_timestamp "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+" and line_item = '"+lineItem+"' ";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
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
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date,  "
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
			+" where Publisher_name = '"+publisherName+"' and  not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" and line_item = '"+lineItem+"' ";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" And Publisher_name = '"+publisherName+"'   " 
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
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id, publisher_name, channel_name  "
			+" ignore case) b  "
			+" on a.Publisher_name = b.Publisher_name  "
			+" and a.Channel_name = b.Channel_name  "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" and a.date = b.date  "
			+" ignore case) group by column ignore case)  "
			+" ignore case  ";
	
		
	
       
		
		log.info("actual header trends  data advertiser view query  :"+QUERY);
		
		
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
		log.info("in loadTrendAnalysisHeaderDataAdvertiserVew of AdvertiserDAO");
		AdvertiserTrendAnalysisActualDTO object = new AdvertiserTrendAnalysisActualDTO();
		double totalRevenueDelivered = 0;
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
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'"
			+" and line_item in ('"+lineItem+"')";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" and Publisher_name = '"+publisherName+"'  "
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
			+" where line_item in ('"+lineItem+"')";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" and Publisher_name = '"+publisherName+"'  "
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
		log.info("actual trends and analysis ACTUAL  data advertiser view query  :"+QUERY);
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
		log.info("in loadTrendAnalysisForcastDataAdvertiserView of AdvertiserDAO");
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
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'"
			+" and line_item in ('"+lineItem+"')";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" and Publisher_name = '"+publisherName+"'  "
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
			+" where line_item in ('"+lineItem+"')";
			if(campainOrder != null && !campainOrder.trim().equalsIgnoreCase("")) {
				QUERY = QUERY +" and order in ('"+campainOrder+"')";
			}
			QUERY = QUERY
			+" and Publisher_name = '"+publisherName+"'  "
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

	@Override
	public List<AdvertiserPerformanceSummaryHeaderDTO> loadPerformanceSummaryHeaderData(String lowerDate, String advertiser, String upperDate, String agency,String publisherName,String properties) throws Exception {
		log.info("Inside loadPerformanceSummaryHeaderData of AdvertiserDAO");
		AdvertiserPerformanceSummaryHeaderDTO object = new AdvertiserPerformanceSummaryHeaderDTO();
		List<AdvertiserPerformanceSummaryHeaderDTO> performanceSummaryHeaderDataList = new ArrayList<AdvertiserPerformanceSummaryHeaderDTO>();
		String QUERY = "";
		QUERY = QUERY
		+" select * from ( "
		+" select 'date filter' as column ,  "
		+" integer(0) as Booked_impression,  "
		+" 0.0 as budget,  "
		+" sum(Total_clicks) as Clicks ,	 "
		+" ifnull((sum(Total_clicks)/sum(Total_Impressions))*100,0) CTR, "
		+" ifnull((sum(Total_Impressions)),0) as Impression_delivered, "
		+" ifnull((sum(Total_revenue)),0.0) as revenue_Delivered,  "
		+" 0.0 as  revenue_remaining "
		+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
		+" join each "
		+" ( "
		+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
		+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
		+" Where Publisher_name = '"+publisherName+"'  "
		+" and date >='"+lowerDate+"' and date <='"+upperDate+"' "
		+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
		+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
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
			//QUERY = QUERY +" and site_name in ('"+properties+"')";
		}
			QUERY = QUERY		
		+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
		+" ignore case) b  "
		+" on a.date = b.date  "
		+" and a.Publisher_name = b.Publisher_name "
		+" and a.Channel_name = b.Channel_name "
		+" and a.site_name = b.site_name "
		+" and a.site_type = b.site_type "
		+" and a.zone = b.zone "
		+" and a.advertiser = b.advertiser "
		+" and a.line_item_id = b.line_item_id "
		+" and a.creative_id = b.creative_id "
		+" and a.load_timestamp = b.load_timestamp "
		+" group by column ignore case), "
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
		+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget,  "
		+" a.line_item_id as line_item_id, a.line_item_lifetime_impressions as line_item_lifetime_impressions,  "
		+" case when a.cost_type = 'CPD'  "
		+" then (case when a.Line_Item_end_date >= '"+lowerDate+" ' and  "
		+" a.Line_Item_end_date < '"+upperDate+"'  "
		+" then (DATEDIFF(a.Line_Item_end_date,a.Line_Item_start_date)*a.rate)  "
		+" when a.Line_Item_end_date >= '"+lowerDate+"'  "
		+" and a.Line_Item_end_date >= '"+upperDate+" '  "
		+" then (DATEDIFF(timestamp('"+upperDate+" '),a.Line_Item_start_date)*a.rate)  "
		+" when a.Line_Item_end_date is null then  "
		+" (DATEDIFF(timestamp('"+upperDate+" '),a.Line_Item_start_date)*a.rate)  "
		+" end)  "
		+" else (a.line_item_lifetime_impressions*a.rate)/1000  "
		+" end Revenue_Delivered,  "
		+" a.line_item_lifetime_clicks as line_item_lifetime_clicks  "
		+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a  "
		+" join each  "
		+" (  "
		+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, "
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
		+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
		+" where Publisher_name = '"+publisherName+"'  "
		+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
		+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
		+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
		+" and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"+lowerDate+"' and date <='"+upperDate+"')";		
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
		+" group each by line_item_id ignore case)v "
		+" on u.line_item_id = v.line_item_id and u.date = v.date "
		+" group each by line_item_id, date ignore case)w "
		+" on t.line_item_id = w.line_item_id and t.date = w.date "
		+" and t.load_timestamp = w.load_timestamp "
		+" group each by line_item_id, date, load_timestamp ignore case)x "
		+" on s.line_item_id = x.line_item_id and s.date = x.date "
		+" and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id "
		+" group each by line_item_id, date, load_timestamp, zone_id "
		+"  ignore case)y "
		+" on r.line_item_id = y.line_item_id and r.date = y.date "
		+" and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id "
		+" and r.site_type_id = y.site_type_id "
		+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id	, publisher_name, channel_name "
		+"  ignore case) b  " 
		+" on a.Publisher_name = b.Publisher_name  "
		+" and a.Channel_name = b.Channel_name  "
		+" and a.site_type_id = b.site_type_id  "
		+" and a.creative_id = b.creative_id  "
		+" and a.line_item_id = b.line_item_id  "
		+" and a.load_timestamp = b.load_timestamp  "
		+" and a.zone_id = b.zone_id  "
		+" and a.date = b.date  "
		+" ignore case) group by column)  "
		+" ignore case  ";

		log.info("actual Performance Summary Header Data query  :"+QUERY);
		
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
				 if(rowList != null && rowList.size() > 1) {
					 TableRow row1 = rowList.get(0);
			    	 List<TableCell> cellList1 = row1.getF();
			    	 if(cellList1 != null && !cellList1.isEmpty()) {
			    		 object.setClicks(cellList1.get(3).getV().toString());
			    		 object.setCtr(cellList1.get(4).getV().toString());
			    		 object.setImpressionDelivered(cellList1.get(5).getV().toString());
			    		 object.setRevenueDelivered(cellList1.get(6).getV().toString());
			    	 }
				     TableRow row2 = rowList.get(1);
			    	 List<TableCell> cellList2 = row2.getF();
			    	 if(cellList2 != null && !cellList2.isEmpty()) {
			    		 object.setBookedImpressions(cellList2.get(1).getV().toString());
			    		 object.setBudget(cellList2.get(2).getV().toString());
			    		 object.setTotalClicks(cellList2.get(3).getV().toString());
			    		 object.setTotalCtr(cellList2.get(4).getV().toString());
			    		 object.setTotalImpressionDelivered(cellList2.get(5).getV().toString());
			    		 object.setTotalRevenueDelivered(cellList2.get(6).getV().toString());
			    		 object.setRevenueRemaining(cellList2.get(7).getV().toString());
			    	 }
				 }
				 else if(rowList != null && rowList.size() == 1 && rowList.get(0).getF().get(0).getV().toString().equalsIgnoreCase("date filter")) {
					 TableRow row1 = rowList.get(0);
			    	 List<TableCell> cellList1 = row1.getF();
			    	 if(cellList1 != null && !cellList1.isEmpty()) {
			    		 object.setClicks(cellList1.get(3).getV().toString());
			    		 object.setCtr(cellList1.get(4).getV().toString());
			    		 object.setImpressionDelivered(cellList1.get(5).getV().toString());
			    		 object.setRevenueDelivered(cellList1.get(6).getV().toString());
			    		 
			    		 object.setBookedImpressions("0");
			    		 object.setBudget("0");
			    		 object.setTotalClicks("0");
			    		 object.setTotalCtr("0");
			    		 object.setTotalRevenueDelivered("0");
			    		 object.setTotalImpressionDelivered("0");
			    		 object.setRevenueRemaining("0");
			    	 }
				 }
				 else if(rowList != null && rowList.size() == 1 && rowList.get(0).getF().get(0).getV().toString().equalsIgnoreCase("Total")) {
					 TableRow row2 = rowList.get(0);
			    	 List<TableCell> cellList2 = row2.getF();
			    	 if(cellList2 != null && !cellList2.isEmpty()) {
			    		 object.setBookedImpressions(cellList2.get(1).getV().toString());
			    		 object.setBudget(cellList2.get(2).getV().toString());
			    		 object.setTotalClicks(cellList2.get(3).getV().toString());
			    		 object.setTotalCtr(cellList2.get(4).getV().toString());
			    		 object.setTotalImpressionDelivered(cellList2.get(5).getV().toString());
			    		 object.setTotalRevenueDelivered(cellList2.get(6).getV().toString());
			    		 object.setRevenueRemaining(cellList2.get(7).getV().toString());
			    		 
			    		 object.setClicks("0");
			    		 object.setCtr("0");
			    		 object.setRevenueDelivered("0");
			    		 object.setImpressionDelivered("0");
			    	 }
				 }
			    performanceSummaryHeaderDataList.add(object);
			 }
		 }	
		 return performanceSummaryHeaderDataList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadTopPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String limit = "5";
		log.info("Inside loadTopPerformerLineItems of AdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> topPerformerLineItemsList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" SELECT  REGEXP_REPLACE(ifnull(line_item,''),'null','') line_item, " 
			+" ifnull(SUM(total_impressions),0) as Impression_Delivered, "
			+" ifnull(sum(total_clicks),0) Clicks, "
			+" ifnull((sum(total_clicks)/sum(total_impressions))*100,0) CTR  "
			+" from  "
			+" (select a.line_item_id as line_item_id, a.line_item as line_item, a.total_clicks as total_clicks,  "
			+" a.total_impressions as total_impressions, a.date as date, a.Channel_name as Channel_name, a.Publisher_name as Publisher_name, a.site_name as site_name, " 
			+" case when a.delivery_indicator = 0.0 then 10000.0 else a.delivery_indicator end as delivery_indicator "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" a "
			+" join each "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
			+" and Channel_name <> 'House' "
			+" And Publisher_name = '"+publisherName+"'  "
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
				//QUERY = QUERY +" and site_name in ('"+properties+"')";
			}
			QUERY = QUERY			
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+" ignore case) b  "
			+" on a.date = b.date  "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" ignore case)  "
			+" where delivery_indicator >= 100 "
			+" group each by line_item "
			+" order by ctr desc, line_item "
			+" limit "+limit
			+" ignore case";
			log.info("actual Top Performer LineItems Data query  :"+QUERY);
			
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
			    				  cellList.get(0).getV().toString(),
			    				  new Long(cellList.get(1).getV().toString()),
			    				  new Long(cellList.get(2).getV().toString()),
			    				  new Double(cellList.get(3).getV().toString())
			    				  );
			    		 
					    		  topPerformerLineItemsList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadTopPerformerLineItems of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return topPerformerLineItemsList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadNonPerformerLineItems(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String limit = "5";		
		log.info("Inside loadNonPerformerLineItems of AdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> topPerformerLineItemsList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select REGEXP_REPLACE(ifnull(z.line_item,''),'null','') line_item, "
			+" ifnull(x.goal_qty,0) as Booked_Impression, "
			+" ifnull(z.Impression_Delivered,0) Impression_Delivered, "
			+" ifnull(z.Clicks,0) Clicks,  "
			+" ifnull(z.CTR,0.0) as CTR, "
			+" ifnull(x.delivery_indicator,0.0) as Delivery_Indicator, z.line_item_id "
			+"  from ( "
			+" SELECT  ifnull(a.line_item,'') as line_item, a.line_item_id as line_item_id,ifnull(SUM(a.total_impressions),0) as Impression_Delivered," 
			+" ifnull(sum(a.total_clicks),0) Clicks, "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a "
			+" join each  "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"'  "
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
				//QUERY = QUERY +" and site_name in ('"+properties+"')";
			}
			QUERY = QUERY
			+" and Channel_name <> 'House' "
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id  "
			+" ignore case) b  "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name  "
			+" and a.site_name = b.site_name  "
			+" and a.site_type = b.site_type  "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" group each by line_item, line_item_id  "
			+" ignore case)z "
			+" join each(select ifnull(a.line_item_id,'') as line_item_id, case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))  else 0 end goal_qty, " 
			+" case when ifnull(a.delivery_indicator,0.0)=0.0 then 10000.00 else a.delivery_indicator end as delivery_indicator	 "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
			+" join each "
			+" ( "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, r.load_timestamp as load_timestamp ,  " 
			+" r.zone_id as zone_id, r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r   "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,	s.load_timestamp as load_timestamp , s.zone_id as zone_id,  "
			+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s   "
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,	t.load_timestamp as load_timestamp, max(zone_id) as zone_id  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,	max(load_timestamp) as load_timestamp  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"'  "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"+lowerDate+"' and date <='"+upperDate+"')"
			+" and Channel_name <> 'House' " ;
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
			+" and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id and r.site_type_id = y.site_type_id  " 
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id	, publisher_name, channel_name  "
			+" ignore case) b "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_type_id = b.site_type_id "
			+" and a.creative_id = b.creative_id "
			+" and a.line_item_id = b.line_item_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" and a.zone_id = b.zone_id "
			+" ignore case )x  "
			+" on z.line_item_id = x.line_item_id "
			+" where x.delivery_indicator < 100 "
			+" order by ctr,line_item "
			+" limit "+limit
			+" ignore case";
			log.info("actual Top Non Performer LineItems Data query  :"+QUERY);
			
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
			      for (TableRow row : rowList) {
			    	  List<TableCell> cellList = row.getF();
			    	  advertiserPerformerDTO= new AdvertiserPerformerDTO();
			    	  advertiserPerformerDTO.setCampaignLineItem(cellList.get(0).getV().toString());
			    	  advertiserPerformerDTO.setBookedImpressions(new Long(cellList.get(1).getV().toString()));
			    	  advertiserPerformerDTO.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
			    	  advertiserPerformerDTO.setClicks(new Long(cellList.get(3).getV().toString()));
			    	  advertiserPerformerDTO.setCTR(new Double(cellList.get(4).getV().toString()));
			    	  advertiserPerformerDTO.setDeliveryIndicator(new Double(cellList.get(5).getV().toString()));
			    	  advertiserPerformerDTO.setLineItemId(new Long(cellList.get(6).getV().toString()));
			    	  topPerformerLineItemsList.add(advertiserPerformerDTO);	 
					      
			      }
			    	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadNonPerformerLineItems of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return topPerformerLineItemsList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadMostActiveLineItems(String lowerDate, String upperDate, String compareStartDate,String compareEndDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		String limit = "5";
		log.info("Inside loadMostActiveLineItems of AdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> mostActiveLineItemList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select REGEXP_REPLACE(ifnull(g.line_item,''),'null','') line_item, "
			+" ifnull(g.Impression_Delivered,0) Impression_Delivered, "
			+" ifnull(g.Clicks,0) Clicks, "
			+" ifnull(g.CTR,0.0) as CTR, ifnull(g.Change_in_time_period,0.0) Change_in_Week,  "
			+" ifnull(x.delivery_indicator,0.0) as Delivery_Indicator,  "
			+" ifnull((g.CTR - x.Life_Time_CTR),0.0) as Change_Life_Time  "
			+" from ( "
			+" select z.line_item_id as line_item_id, z.line_item as line_item, z.Impression_Delivered as Impression_Delivered, z.Clicks as Clicks, z.CTR as CTR ,"
			+" ifnull(y.Last_Week_CTR,0) as Last_Week_CTR "
			+" ,(CTR-Last_Week_CTR) as Change_in_time_period  "
			+" from ( "
			+" SELECT  ifnull(a.line_item,'') as line_item, a.line_item_id as line_item_id, "
			+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered,  "
			+" ifnull(sum(a.total_clicks),0) Clicks,  "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a  "
			+" join each"
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and Channel_name <> 'House'  ";
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
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
			+" group each by line_item, line_item_id "
			+" ignore case)z  "
			+" Left Join  "
			+" (  "
			+" SELECT  a.line_item_id as line_item_id,  "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) Last_Week_CTR "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a  "
			+" join each "
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where date >='"+compareStartDate+"' and date <='"+compareEndDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and Channel_name <> 'House'  ";
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
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
			+" group each by line_item_id  "
			+" ignore case  "
			+" )y on z.line_item_id = y.line_item_id  "
			+" )g "
			+" join each (select ifnull(a.line_item_id,'') as line_item_id, case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end goal_qty, " 
			+" case when ifnull(a.delivery_indicator,0.0)=0.0 then 10000.00 else a.delivery_indicator end as delivery_indicator,  "
			+" case when a.cost_type = 'CPM' then "
			+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100) "
			+" else 0 end Life_Time_CTR 	  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
			+" join each "
			+" ( "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, r.load_timestamp as load_timestamp , r.zone_id as zone_id,  "
			+" r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r   "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,s.load_timestamp as load_timestamp , s.zone_id as zone_id, max(site_type_id) as site_type_id  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  " 
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,t.load_timestamp as load_timestamp ,max(zone_id) as zone_id   " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,max(load_timestamp) as load_timestamp  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"+lowerDate+"' and date <='"+upperDate+"')"
			+" and Channel_name <> 'House'  ";
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
			+" group each by line_item_id ignore case )v   "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case )w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case )x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case )y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id" 
			+" and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id, publisher_name, channel_name "
			+" ignore case) b  "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_type_id = b.site_type_id "
			+" and a.creative_id = b.creative_id "
			+" and a.line_item_id = b.line_item_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" and a.zone_id = b.zone_id "
			+" ignore case  "
			+"  )x  "
			+" on g.line_item_id = x.line_item_id "
			+" where Delivery_Indicator >=100 "
			+" order by Delivery_Indicator desc, line_item  "
			+" limit "+limit
			+" ignore case";
			log.info("actual Most Active LineItems Data query  :"+QUERY);
			
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
						    	  advertiserPerformerDTO= new AdvertiserPerformerDTO();
						    	  advertiserPerformerDTO.setCampaignLineItem(cellList.get(0).getV().toString());
						    	  advertiserPerformerDTO.setImpressionDelivered(new Long(cellList.get(1).getV().toString()));
						    	  advertiserPerformerDTO.setClicks(new Long(cellList.get(2).getV().toString()));
						    	  advertiserPerformerDTO.setCTR(new Double(cellList.get(3).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInTimePeriod(new Double(cellList.get(4).getV().toString()));
						    	  advertiserPerformerDTO.setDeliveryIndicator(new Double(cellList.get(5).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInLifeTime(new Double(cellList.get(6).getV().toString()));
						    		 
						    	  mostActiveLineItemList.add(advertiserPerformerDTO);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadMostActiveLineItems of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return mostActiveLineItemList;
	}

	@Override
	public List<AdvertiserPerformerDTO> loadTopGainers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception {
		String limit = "5";
		log.info("Inside loadTopGainers of AdvertiserDAO");
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> topGainersList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select REGEXP_REPLACE(ifnull(g.line_item,''),'null','') line_item,  "
			+"  ifnull(g.Impression_Delivered,0) as Impression_Delivered, ifnull(g.CTR,0) as CTR, ifnull(g.Change_in_Week,0) as  Change_in_Week,"
			+" ifnull((g.CTR - x.Life_Time_CTR),0) as Change_Life_Time "
			+" from (  "
			+" select z.line_item_id as line_item_id, z.line_item as line_item, z.Impression_Delivered as Impression_Delivered,  z.Clicks as Clicks, z.CTR as CTR ,"
			+" ifnull(y.Last_Week_CTR,0) as Last_Week_CTR  "
			+" ,(CTR-Last_Week_CTR) as Change_in_Week  "
			+" from (  "
			+" SELECT  ifnull(a.line_item,'') as line_item, a.line_item_id as line_item_id,  "
			+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered, "
			+" ifnull(sum(a.total_clicks),0) Clicks, "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" a "
			+" join each "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
			+" And Publisher_name = '"+publisherName+"'  "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id  "
			+" ) b "
			+" on a.date = b.date  "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" group each by line_item, line_item_id  "
			+" ignore case)z "
			+" Left Join "
			+" ( "
			+" SELECT  a.line_item_id as line_item_id, "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) Last_Week_CTR  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" a "
			+" join each "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where date >='"+compareStartDate+"' and date <='"+compareEndDate+"' "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
			+" And Publisher_name = '"+publisherName+"'  "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id  "
			+"  ignore case) b "
			+" on a.date = b.date  "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone "
			+" and a.advertiser = b.advertiser "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" group each by line_item_id "
			+" ignore case "
			+" )y on z.line_item_id = y.line_item_id "
			+"  ignore case)g  "
			+" join each(select ifnull(a.line_item_id,'') as line_item_id, "
			+" case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) else 0 end goal_qty,  "
			+" case when a.cost_type = 'CPM' then  "
			+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100)  "
			+" else 0 end Life_Time_CTR 	 "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
			+" join each "
			+" (  "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, r.load_timestamp as load_timestamp , r.zone_id as zone_id,  "
			+" r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r   "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date, s.load_timestamp as load_timestamp , s.zone_id as zone_id,  "
			+" max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s   "
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,t.load_timestamp as load_timestamp, max(zone_id) as zone_id  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,max(load_timestamp) as load_timestamp  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  "
			+" And Publisher_name = '"+publisherName+"'  "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+"and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"+lowerDate+"' and date <='"+upperDate+"')"
			+" and Channel_name <> 'House'  ";
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
			+" group each by line_item_id ignore case)v  " 
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id  "
			+" and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id , publisher_name, channel_name  "
			+" ignore case) b "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_type_id = b.site_type_id "
			+" and a.creative_id = b.creative_id "
			+" and a.line_item_id = b.line_item_id "
			+" and a.load_timestamp = b.load_timestamp "
			+" and a.zone_id = b.zone_id "
			+" ignore case "
			+"  )x "
			+" on g.line_item_id = x.line_item_id  "
			+" where Change_in_Week > 0 "
			+" order by Change_in_Week desc, line_item "
			+" limit "+limit
			+" ignore case";
			log.info("actual Top Gainers Data query  :"+QUERY);
			
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
						    	  advertiserPerformerDTO= new AdvertiserPerformerDTO();
						    	  advertiserPerformerDTO.setCampaignLineItem(cellList.get(0).getV().toString());
						    	  advertiserPerformerDTO.setImpressionDelivered(new Long(cellList.get(1).getV().toString()));
						    	  advertiserPerformerDTO.setCTR(new Double(cellList.get(2).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInTimePeriod(new Double(cellList.get(3).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInLifeTime(new Double(cellList.get(4).getV().toString()));
						    		 
						    	  topGainersList.add(advertiserPerformerDTO);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadTopGainers of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return topGainersList;
	}
	
	@Override
	public List<AdvertiserPerformerDTO> loadTopLosers(String lowerDate,String upperDate, String compareStartDate, String compareEndDate,String advertiser, String agency,String publisherName,String properties) throws Exception {
		log.info("Inside loadTopLosers of AdvertiserDAO");
		String limit = "5";
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> topLosersList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select REGEXP_REPLACE(ifnull(g.line_item,''),'null','') as line_item,  "
			+" ifnull(g.Impression_Delivered,0) as Impression_Delivered, ifnull(g.CTR,0) as CTR,  "
			+" ifnull(g.Change_in_Week,0) as Change_in_Week,  "
			+" ifnull((g.CTR - x.Life_Time_CTR),0) as Change_Life_Time  "
			+" from ( "
			+" select z.line_item_id as line_item_id, z.line_item as line_item, z.Impression_Delivered as Impression_Delivered, z.Clicks as Clicks, z.CTR as CTR ,"
			+" ifnull(y.Last_Week_CTR,0) as Last_Week_CTR "
			+" ,(CTR-Last_Week_CTR) as Change_in_Week "
			+" from ( "
			+" SELECT  ifnull(a.line_item,'') as line_item, a.line_item_id as line_item_id, "
			+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered,  "
			+" ifnull(sum(a.total_clicks),0) Clicks,  "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a  "
			+" join each "
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where date >='"+lowerDate+"' and date <='"+upperDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and Channel_name <> 'House'  ";
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
			+"  ignore case) b  "
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
			+" group each by line_item, line_item_id "
			+" ignore case)z  "
			+" Left Join  "
			+" (  "
			+" SELECT  a.line_item_id as line_item_id,  "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) Last_Week_CTR "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" a  "
			+" join each "
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]  "
			+" Where date >='"+compareStartDate+"' and date <='"+compareEndDate+"'  "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and Channel_name <> 'House'  ";
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id "
			+"  ignore case) b  "
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
			+" group each by line_item_id  "
			+" ignore case  "
			+" )y on z.line_item_id = y.line_item_id  "
			+"  ignore case)g "
			+" join each (select ifnull(a.line_item_id,'') as line_item_id, case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))  else 0 end goal_qty," 
			+" case when a.cost_type = 'CPM' then "
			+" ((ifnull(a.line_item_lifetime_clicks,0)/ifnull(a.line_item_lifetime_impressions,0))*100) "
			+" else 0 end Life_Time_CTR 	  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a "
			+" join each "
			+" (  "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, r.load_timestamp as load_timestamp , r.zone_id as zone_id,r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r   "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,s.load_timestamp as load_timestamp , s.zone_id as zone_id,max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  " 
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,t.load_timestamp as load_timestamp ,max(zone_id) as zone_id  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,max(load_timestamp) as load_timestamp  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'  "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" And Publisher_name = '"+publisherName+"' "
			+  agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where date >='"+lowerDate+"' and date <='"+upperDate+"')"
			+" and Channel_name <> 'House'  ";
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
			+" group each by line_item_id ignore case)v   "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id, publisher_name, channel_name  "
			+" ignore case) b  "
			+" on a.date = b.date  "
			+" and a.Publisher_name = b.Publisher_name  "
			+" and a.Channel_name = b.Channel_name  "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" ignore case  "
			+"  )x  "
			+" on g.line_item_id = x.line_item_id "
			+" where Change_in_Week < 0 "
			+" order by Change_in_Week , line_item  "
			+" limit "+limit
			+" ignore case";
			log.info("actual Top Losers Data query  :"+QUERY);
			
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
						    	  advertiserPerformerDTO= new AdvertiserPerformerDTO();
						    	  advertiserPerformerDTO.setCampaignLineItem(cellList.get(0).getV().toString());
						    	  advertiserPerformerDTO.setImpressionDelivered(new Long(cellList.get(1).getV().toString()));
						    	  advertiserPerformerDTO.setCTR(new Double(cellList.get(2).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInTimePeriod(new Double(cellList.get(3).getV().toString()));
						    	  advertiserPerformerDTO.setChangeInLifeTime(new Double(cellList.get(4).getV().toString()));
						    		 
						    	  topLosersList.add(advertiserPerformerDTO);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadTopLosers of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
			/**/
		}
		 return topLosersList;
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
	public List<AdvertiserPerformerDTO> loadPerformanceMetrics(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		log.info("Inside loadPerformanceMetrics of AdvertiserDAO");
		String limit = "243";
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> performanceMetricsList = new ArrayList<AdvertiserPerformerDTO>();
		
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select REGEXP_REPLACE(ifnull(z.line_item,''),'null','') as line_item,  z.order,                            "
			+" ifnull(z.Impression_Delivered,0) Impression_Delivered,   "
			+" ifnull(z.CTR,0.0) as CTR , "
			+" ifnull(z.Clicks,0) Clicks, ifnull(z.revenue_Delivered,0.0) revenue_Delivered, "
			+" (ifnull(x.Budget,0)-z.revenue_Delivered) as Balance,  "
			+" ifnull(z.revenue_Delivered,0.0) as Spent, "
			+" ifnull(x.booked_impression,0) as booked_Impression,   "
			+" ifnull(x.Budget,0.0) as Budget   "
			+"  from (  "
			+" SELECT REGEXP_REPLACE(ifnull(a.order,''),'null','') as  order, "
			+" REGEXP_REPLACE(ifnull(a.line_item,''),'null','') as line_item, "
			+" REGEXP_REPLACE(ifnull(a.line_item_id,''),'null','') as line_item_id, ifnull(SUM(a.total_impressions),0) as Impression_Delivered," 
			+" sum(a.total_clicks) Clicks,   "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR, "
			+" (sum(Total_revenue)) as revenue_Delivered "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" a  "
			+" join each   "
			+" (  "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id,  MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" Where Publisher_name = '"+publisherName+"' and Channel_name <> 'House'   "
			+" and date >='"+lowerDate+"' and date <='"+upperDate+"' "
			+ agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'   "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  ";
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+"  ignore case) b   "
			+" on a.date = b.date   "
			+" and a.Publisher_name = b.Publisher_name   "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_name = b.site_name "
			+" and a.site_type = b.site_type "
			+" and a.zone = b.zone  "
			+" and a.advertiser = b.advertiser  "
			+" and a.line_item_id = b.line_item_id "
			+" and a.creative_id = b.creative_id   "
			+" and a.load_timestamp = b.load_timestamp   "
			+" group each by order, line_item,line_item_id  "
			+" ignore case)z  "
			+" join each  "
			+" (select a.cost_type as cost_type,  a.line_item_id as line_item_id , case when cost_type = 'CPM' then (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate)/1000 "
			+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget, case when a.cost_type = 'CPM' then integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0')) " 
			+" else 0 end booked_impression , a.line_item_lifetime_impressions as line_item_lifetime_impressions, a.line_item_lifetime_clicks as line_item_lifetime_clicks  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] a   "
			+" join each  "
			+" (  "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, r.load_timestamp as load_timestamp , r.zone_id as zone_id,r.site_type_id as site_type_id, max(r.creative_id) as creative_id  "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]r   "
			+" join each (  "
			+" select s.line_item_id as line_item_id, s.date as date,s.load_timestamp as load_timestamp , s.zone_id as zone_id,max(site_type_id) as site_type_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]s  " 
			+" join each (  "
			+" select t.line_item_id as line_item_id, t.date as date,t.load_timestamp as load_timestamp ,max(zone_id) as zone_id  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]t  "
			+" join each (  "
			+" select u.line_item_id as line_item_id, u.date as date,max(load_timestamp) as load_timestamp  " 
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance]u  "
			+" join each  "
			+" (select line_item_id, max(date) as date "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "			
			+" where Publisher_name = '"+publisherName+"' and Channel_name <> 'House'   "
			+" and line_item_id in (select line_item_id from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where date >='"+lowerDate+"' and date <='"+upperDate+"')"
			+ agencyAdvertiserCondition(agency, "agency", advertiser, "advertiser")
			+" And not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0'   "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')  ";
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
			+" group each by line_item_id ignore case)v   "
			+" on u.line_item_id = v.line_item_id and u.date = v.date  "
			+" group each by line_item_id, date ignore case)w  "
			+" on t.line_item_id = w.line_item_id and t.date = w.date and t.load_timestamp = w.load_timestamp  "
			+" group each by line_item_id, date, load_timestamp ignore case)x  "
			+" on s.line_item_id = x.line_item_id and s.date = x.date and s.load_timestamp = x.load_timestamp and s.zone_id = x.zone_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id  "
			+" ignore case)y  "
			+" on r.line_item_id = y.line_item_id and r.date = y.date and r.load_timestamp = y.load_timestamp and r.zone_id = y.zone_id and r.site_type_id = y.site_type_id  "
			+" group each by line_item_id, date, load_timestamp, zone_id, site_type_id, publisher_name, channel_name  "
			+" ignore case) b  "
			+" on a.date = b.date  "
			+" and a.Publisher_name = b.Publisher_name  "
			+" and a.Channel_name = b.Channel_name  "
			+" and a.site_type_id = b.site_type_id  "
			+" and a.creative_id = b.creative_id  "
			+" and a.line_item_id = b.line_item_id  "
			+" and a.load_timestamp = b.load_timestamp  "
			+" and a.zone_id = b.zone_id  "
			+" ignore case )x "
			+" on z.line_item_id = x.line_item_id  "
			+" order by z.order, line_item "
			+" limit "+limit
			+" ignore case";
			
			log.info("actual Performance Metrics Data query  :"+QUERY);
			
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
						    	  advertiserPerformerDTO= new AdvertiserPerformerDTO();
						    	  
						    	  advertiserPerformerDTO.setCampaignLineItem(cellList.get(0).getV().toString());
						    	  advertiserPerformerDTO.setCampaignIO(cellList.get(1).getV().toString());
						    	 // advertiserPerformerDTO.setDate(cellList.get(2).getV().toString());
						    	  advertiserPerformerDTO.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
						    	  advertiserPerformerDTO.setCTR(new Double(cellList.get(3).getV().toString()));
						    	  advertiserPerformerDTO.setClicks(new Long(cellList.get(4).getV().toString()));
						    	  advertiserPerformerDTO.setRevenueDeliverd(new Double(cellList.get(5).getV().toString()));
						    	  advertiserPerformerDTO.setBalance(new Double(cellList.get(6).getV().toString()));
						    	  advertiserPerformerDTO.setSpent(new Double(cellList.get(7).getV().toString()));
						    	  advertiserPerformerDTO.setBookedImpressions(new Long(cellList.get(8).getV().toString()));
						    	  advertiserPerformerDTO.setBudget(new Double(cellList.get(9).getV().toString()));
						    		 
						    	  performanceMetricsList.add(advertiserPerformerDTO);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadPerformanceMetrics of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		if(performanceMetricsList != null) {
    		log.info("performanceMetricsList in DAO, size : "+performanceMetricsList.size());
    	}
		
		return performanceMetricsList;
	}

	@Override
	public List<AdvertiserByLocationObj> loadAdvertisersByLocationData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		log.info("Inside loadAdvertisersByLocationData of AdvertiserDAO");
		AdvertiserByLocationObj advertiserByLocationObj = new AdvertiserByLocationObj();
		List<AdvertiserByLocationObj> advertiserByLocationObjList = new ArrayList<AdvertiserByLocationObj>();
		try{
			String QUERY = "";
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
			+" ignore case";
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
						    	  
					    		  advertiserByLocationObj.setState(cellList.get(0).getV().toString());
					    		  advertiserByLocationObj.setImpression(new Long(cellList.get(1).getV().toString()));
					    		  advertiserByLocationObj.setCtrPercent(new Double(cellList.get(2).getV().toString()));
						    		 
						    	  advertiserByLocationObjList.add(advertiserByLocationObj);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertisersByLocationData of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserByLocationObjList;
	}

	@Override
	public List<AdvertiserByLocationObj> loadAdvertiserByMarketGraphData(String lowerDate, String upperDate, String advertiser, String agency,String publisherName,String properties) throws Exception {
		
		log.info("Inside loadAdvertiserByMarketGraphData of AdvertiserDAO");
		AdvertiserByLocationObj advertiserByLocationObj = new AdvertiserByLocationObj();
		List<AdvertiserByLocationObj> advertiserByLocationObjList = new ArrayList<AdvertiserByLocationObj>();
		try{
			String QUERY = "";
			QUERY = QUERY
			+" select                                                                                                       "
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
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp"
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where date >='"+lowerDate+"' and date <='"+upperDate+"' and "
			+" not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '')"
			+" And Publisher_name = '"+publisherName+"' "
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
			+" group each by date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id"
			+"  ignore case) b "
			+" on a.date = b.date "
			+" and a.Publisher_name = b.Publisher_name "
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
			+"  left join "
			+"  (select dfp_property_name , station_name, state_name  from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Property_Name])g "
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
					    		  advertiserByLocationObj.setImpression(new Long(cellList.get(3).getV().toString()));
						    		 
						    	  advertiserByLocationObjList.add(advertiserByLocationObj);
					    	  }
				    	  }				    
				      }
				 }
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertiserByMarketGraphData of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserByLocationObjList;
	}

	@Override
	public void getLineItemForPopUP(String lowerDate, String upperDate, String lineItemName, PopUpDTO popUpObj,String publisherName,String properties) throws Exception {
		log.info("Inside getLineItemForPopUP of AdvertiserDAO");
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
			+" and date >='"+lowerDate+"' and date <='"+upperDate+"'  "
			+" And Publisher_name = '"+publisherName+"' "
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
				+" Where Publisher_name = '"+publisherName+"' "
				+" and date >='"+lowerDate+"' and date <='"+upperDate+"'  "
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
				+" where  Publisher_name = '"+publisherName+"'  "
				+" and line_item = '"+lineItemName+"' "
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
			log.severe("Exception getLineItemForPopUP of AdvertiserDAO."+e.getMessage());
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
	
		public List<AgencyDTO> getAgencyDropDownList(String publisherName,
				String str) {
			List<AgencyDTO> agencies = new ArrayList<AgencyDTO>();

			// String QUERY =
			// "select agency from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"+publisherName+"' and agency is not null and agency <> 'null' and agency <> '-0' and agency <> '-' GROUP EACH BY agency ignore case";
			String QUERY = "select Agency_ID,agency from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"
					+ publisherName
					+ "' and agency is not null and agency <> 'null' and agency <> '-0' and agency <> '-' and agency contains '"
					+ str
					+ "' GROUP BY agency,Agency_ID order by agency limit 20 ignore case";
			// log.info("GET AGENCY DROP DOWN LIST   QUERY = "+QUERY);

			
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

		public List<AdvertiserDTO> getAdvertiserDropDownList(String publisherName,
				String agencyName, String str) {
			List<AdvertiserDTO> advertisers = new ArrayList<AdvertiserDTO>();
			String QUERY = null;
			if (agencyName == null || agencyName.trim().equals(""))
				QUERY = "select Advertiser_ID,Advertiser from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"
						+ publisherName
						+ "' and Advertiser is not null and Advertiser <> 'null' and Advertiser contains '"
						+ str
						+ "' GROUP  BY Advertiser,Advertiser_ID order by Advertiser ignore case;";
			else
				QUERY = "select Advertiser_ID,Advertiser from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"
						+ publisherName
						+ "' and Advertiser is not null and Advertiser <> 'null' and Advertiser contains '"
						+ str
						+ "' and agency = '"
						+ agencyName
						+ "' GROUP  BY Advertiser,Advertiser_ID order by Advertiser ignore case;";

			
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
				QUERY = "select Order_ID,order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"
						+ publisherName
						+ "' and order is not null and order <> 'null' and order contains '"
						+ str + "' GROUP  BY order,Order_ID order by order ignore case";
			else
				QUERY = "select Order_ID,order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"
						+ publisherName
						+ "' and order is not null and order <> 'null' and order contains '"
						+ str
						+ "' and (Advertiser = '"
						+ advertiserName
						+ "' ) GROUP  BY order,Order_ID order by order ignore case";

			log.info("getOrderDropDownList query : "+QUERY);
			
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

			            QUERY = "select Line_item_ID, line_item, order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"

			                    + publisherName
			                    + "' and line_item is not null and line_item <> 'null' and line_item contains '"
			                    + str
			                    + "' GROUP  BY line_item,Line_item_ID ,order order by line_item limit 20 ignore case";
			}
			        else
			{            QUERY = "select Line_item_ID,line_item, order from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] where publisher_name = '"

			                    + publisherName
			                    + "' and line_item is not null and line_item <> 'null' and line_item contains '"
			                    + str
			                    + "' and (order = '"
			                    + orderName
			                    + "' ) GROUP  BY line_item,Line_item_ID,order order by line_item limit 20 ignore case";
			}
			
			log.info("LINE ITEM QUERY = "+QUERY);

			
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
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			UserDetailsObj userDetailsObj = dao.getUserById(userId);
			if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherName != null && !publisherName.trim().equals("")) {
				CompanyObj publisherCompany = dao.getCompanyObjByNameAndCompanyType(publisherName, LinMobileConstants.COMPANY_TYPE[0], MemcacheUtil.getAllCompanyList());
				if(publisherCompany != null && publisherCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					String publisherId = publisherCompany.getId()+"";
					List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
					propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(publisherCompany.getId()+""));
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
			log.severe("Exception in getPropertyObjByName of AdvertiserDAO"+e.getMessage());
			e.printStackTrace();
		}*/
		return propertyObj;
	}
	
	public List<AdvertiserPerformerDTO> loadAdvertiserTotalDataList(String lowerDate,String upperDate,String publisherName,String advertiser, String agency,String properties)
	{

		log.info("Inside loadAdvertiserTotalDataList of AdvertiserDAO"+advertiser+" "+agency+" "+properties);
		AdvertiserPerformerDTO advertiserPerformerDTO = new AdvertiserPerformerDTO();
		List<AdvertiserPerformerDTO> advertiserTotalDataList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			String QUERY = "SELECT "
			+" REGEXP_REPLACE(ifnull(a.order,''),'null','') as  order, "
			+" a.line_item_id as line_item_id, "
			+" REGEXP_REPLACE(ifnull(a.line_item,''),'null','') as line_item, " 
			+" ifnull(SUM(a.total_impressions),0) as Impression_Delivered, "
			+" ifnull(sum(a.total_clicks),0) Clicks, "
			+" ifnull((sum(a.total_clicks)/sum(a.total_impressions))*100,0) CTR, "
			+" (sum(Total_revenue)) as revenue_Delivered ,a.advertiser as advertiser, a.agency as agency"
			+" from [LIN.Core_Performance] a "
			+" join each "
			+" ( "
			+" select date, Publisher_name, Channel_name, site_name, site_type, zone, advertiser, line_item_id, creative_id, MAX(load_timestamp) as load_timestamp "
			+" from [LIN.Core_Performance] "
			+" Where Publisher_name = '"+publisherName+"' "
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
				//QUERY = QUERY +" and site_name in ('"+properties+"')";
			}
			QUERY = QUERY
			+" and Channel_name <> 'House' "
			+" and date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00' "
			+" and not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' " 
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
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
			+" group each by line_item, line_item_id, order, advertiser, agency "
			+" order by CTR desc, line_item "
			+" ignore case";
			
			log.info("load Advertiser Total Data List Query  :"+QUERY);
			
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
			    				  cellList.get(0).getV().toString(),
			    				  new Long(cellList.get(1).getV().toString()),
			    				  cellList.get(2).getV().toString(),
			    				  new Long(cellList.get(3).getV().toString()),
			    				  new Long(cellList.get(4).getV().toString()),
			    				  new Double(cellList.get(5).getV().toString()),
			    				  new Double(cellList.get(6).getV().toString()),
			    				  cellList.get(7).getV().toString(),
			    				  cellList.get(8).getV().toString()
			    				  );
			    		 
					    		  advertiserTotalDataList.add(advertiserPerformerDTO);
					    	  }
				    	  }
				      }
			      } 	  
			 }	
		}catch(Exception e) {
			log.severe("Exception loadAdvertiserTotalDataList of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserTotalDataList;
	}
	
	public List<AdvertiserPerformerDTO> loadDeliveryIndicatorData(String lowerDate,String upperDate,String publisherName,String advertiser, String agency, String properties)
	{

		log.info("Inside loadDeliveryIndicatorData of AdvertiserDAO");
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
			+" else (integer(REGEXP_REPLACE(string(ifnull(a.goal_qty,0)),'-1','0'))*a.rate) end Budget, "
			+" case when ifnull(a.delivery_indicator,0.0)=0.0 then 10000.00 else a.delivery_indicator "
			+" end as delivery_indicator "
			+" from [LIN.Core_Performance] a " 
			+" join each "
			+" ( "
			+" select publisher_name, channel_name, r.line_item_id as line_item_id, r.date as date, "
			+" r.load_timestamp as load_timestamp , r.zone_id as zone_id, "
			+" r.site_type_id as site_type_id, max(r.creative_id) as creative_id "
			+" from [LIN.Core_Performance]r "
			+" join each ( "
			+" select s.line_item_id as line_item_id, s.date as date, "
			+" s.load_timestamp as load_timestamp , s.zone_id as zone_id, "
			+" max(site_type_id) as site_type_id from [LIN.Core_Performance]s " 
			+" join each ( "
			+" select t.line_item_id as line_item_id, t.date as date, "
			+" t.load_timestamp as load_timestamp , "
			+" max(zone_id) as zone_id "
			+" from [LIN.Core_Performance]t "
			+" join each ( "
			+" select u.line_item_id as line_item_id, u.date as date, "
			+" max(load_timestamp) as load_timestamp "
			+" from [LIN.Core_Performance]u "
			+" join each "
			+" (select line_item_id, max(date) as date "
			+"  from [LIN.Core_Performance] "
			+" where Publisher_name = '"+publisherName+"' and Channel_name <> 'House' "
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
				//QUERY = QUERY +" and site_name in ('"+properties+"')";
			}
			QUERY = QUERY
			
			+" And not(REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '0' "
			+" or REGEXP_REPLACE(ifnull(line_item_id,''),'null','') = '') "
			+" and line_item_id in (select line_item_id "
			+" from ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID +".Core_Performance] "
			+" where date >='"+lowerDate+" 00:00:00' and date <='"+upperDate+" 00:00:00')"		
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
			+"  , publisher_name, channel_name "
			+"  ignore case) b "
			+" on a.Publisher_name = b.Publisher_name "
			+" and a.Channel_name = b.Channel_name "
			+" and a.site_type_id = b.site_type_id "
			+" and a.creative_id = b.creative_id "
			+" and a.line_item_id = b.line_item_id " 
			+" and a.load_timestamp = b.load_timestamp "
			+" and a.zone_id = b.zone_id "
			+" and a.date = b.date order by delivery_indicator "
			+" ignore case";
			
			log.info("Delivery Indicator Data List Query : "+QUERY);
			
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
			log.severe("Exception loadDeliveryIndicatorData of AdvertiserDAO."+e.getMessage());
			e.printStackTrace();
		}
		 return advertiserTotalDataList;
	
	}
}