package com.lin.persistance.dao.impl;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.INewAdvertiserViewDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.ChannelPerformancePopUpDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;

public class NewAdvertiserViewDAO implements INewAdvertiserViewDAO {

	private static final Logger log = Logger.getLogger(AdvertiserDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	public List<NewAdvertiserViewDTO> headerData(String orderId,String campaignName,String accountName, QueryDTO queryDTO){
		
		//Make this code upto  method in service to reusable in future
		/* Code blocks starts */
		if(accountName == null){
			accountName="";
			
			SmartCampaignPlannerDAO dao =  new SmartCampaignPlannerDAO();
			SmartCampaignObj smartCampaignObj = null;
			try{
				smartCampaignObj = dao.getCampaignByDFPOrderId(Long.parseLong(orderId));
			}catch(Exception e){
				log.info("Exception in headerData() of NewAdvertiserViewDao "+e);
			}
			
			if(smartCampaignObj != null){
				log.info("smartCampaignObj is: "+smartCampaignObj);
				
				AdvertiserObj advertiserObj = null;
				try{
					advertiserObj = dao.getAdvertiserById(Long.parseLong(smartCampaignObj.getAdvertiserId()));
				}catch(Exception e){
					log.info("Exception in headerData() of NewAdvertiserViewDao "+e);
				}
				
				if(advertiserObj != null){
					log.info("advertiserObj is: "+advertiserObj);
					accountName = advertiserObj.getName();
				}
			}
		}
		/* Code blocks ends */
		
		QueryResponse queryResponse = null;
		NewAdvertiserViewDTO dtoObj = new NewAdvertiserViewDTO();
		List<NewAdvertiserViewDTO> headerDataList = new ArrayList<NewAdvertiserViewDTO>();
		StringBuilder query = new StringBuilder();
		query.append(" Select substr(dateorder,27,Length(dateorder)) as Order, sum(ImpressionsDelivered) as Impression_Delivered, ")
		.append(" Sum(Clicks) as Clicks, ifnull(round((Sum(Clicks)/sum(ImpressionsDelivered))*100,4),0.0) as CTR, ")
		.append(" Sum(LineBudget) as Budget, round(sum(Spent),4) as Spent, round(Sum(LineBudget)-sum(Spent),4) as Balance, ")
		.append(" Date( Min(Order_Start_Date)) as order_start_date,	Date(max(Order_End_date)) as order_end_date ")
		.append(" from ( SELECT Order_id, MAX(concat(FORMAT_UTC_USEC(Date), Order)) as dateorder, ")
		.append(" Min(order_start_date) as Order_Start_Date, max(order_end_date) as Order_End_date, ")
		.append("line_item_id, Sum(total_impressions) as ImpressionsDelivered,  ")
		.append("Sum(total_clicks) as Clicks, ")
		.append("(max(case when cost_type = 'CPM' then ifnull(goal_qty,0) else 0 end)*max(rate))/1000 as LineBudget, ")
		.append("Sum(total_revenue) Spent ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by Order_id, line_item_id ignore case ) ")
		.append(" Group By Order ignore case "); 
		log.info("headerData newAdvertiserView :: Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
		    	  List<TableCell> cellList = row.getF();
		    	  String endDate=cellList.get(8).getV().toString();
		    	  if(endDate !=null && endDate.indexOf("java")>=0){
		    		  endDate="None";
		    	  }
		    	  
		    	  dtoObj= new NewAdvertiserViewDTO(
		    				 (cellList.get(0).getV().toString()),
		    				 (cellList.get(1).getV().toString()),
		    				 (cellList.get(2).getV().toString()),
		    				 (cellList.get(3).getV().toString()), 
		    				 (cellList.get(4).getV().toString()), 
		    				 (cellList.get(5).getV().toString()), 
		    				 (cellList.get(6).getV().toString()), 
		    				 (cellList.get(7).getV().toString()),
		    				 (endDate),
		    				 (accountName),
		    				 (new Double(cellList.get(5).getV().toString())),
		    				 (new Double(cellList.get(3).getV().toString())),
		    				 (new Double(cellList.get(6).getV().toString())),
		    				 (new Long(cellList.get(2).getV().toString())),
		    				 (new Long(cellList.get(1).getV().toString())),
		    				 (new Double(cellList.get(4).getV().toString()))
		    				);
		    	  
		    	  headerDataList.add(dtoObj);
			 }
		    	  
 }	
		return headerDataList;
		
	}
	
	
	public List<PerformerReportDTO> performarData(String orderId, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		
		List<PerformerReportDTO> performerDataList = new ArrayList<PerformerReportDTO>();
		StringBuilder query = new StringBuilder();
		NumberFormat numberFormat = NumberFormat.getInstance();
		query.append("SELECT  site_name, ")
		.append(" SUM(total_impressions) as Impression_Delivered, ")
		.append(" sum(total_clicks) as Clicks, ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by site_name ")
		.append(" ignore case ");
		
		log.info("performarData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
		    	  List<TableCell> cellList = row.getF();
		    	  PerformerReportDTO dtoObj = new PerformerReportDTO();
		    	  dtoObj.setS1Site(cellList.get(0).getV().toString());
		    	  dtoObj.setImpressionDeliveredReport(new Long(cellList.get(1).getV().toString()));
		    	  dtoObj.setS2ImpressionDelivered(numberFormat.format(new Long(cellList.get(1).getV().toString())));
		    	  dtoObj.setClicksReport(new Long(cellList.get(2).getV().toString()));
		    	  dtoObj.setS3Clicks(numberFormat.format(new Long(cellList.get(2).getV().toString())));
		    	  dtoObj.setCTRReport(new Double(cellList.get(3).getV().toString())/4);
		    	  dtoObj.setS4CTR(numberFormat.format(new Double(cellList.get(3).getV().toString())));
		    	  performerDataList.add(dtoObj);
			 }
		    	  
}
		return performerDataList;
	}
	
	public List<MostActiveReportDTO> mostActiveData(String orderId, QueryDTO queryDTO){		
		QueryResponse queryResponse = null;
		NumberFormat numberFormat = NumberFormat.getInstance();
		List<MostActiveReportDTO> mostActiveDataList = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		query.append(" SELECT Line_Item, max(case when cost_type = 'CPD' then 0 else goal_qty end)as Booked_Impression, ") 
		.append(" SUM(total_impressions) as ImpressionsDelivered, ")
		.append(" ifnull(round(sum(case when cost_type = 'CPD' then 100.0 else delivery_indicator end)/count(site_id),4),0.0) Pacing ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by line_item ")
		.append(" Order By ImpressionsDelivered desc LIMIT 10")
		.append(" ignore case  ");
		
		log.info("mostActiveData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
				  MostActiveReportDTO dtoObj = new MostActiveReportDTO();
		    	  List<TableCell> cellList = row.getF();
		    	  dtoObj.setC1lineItem(cellList.get(0).getV().toString());
		    	  dtoObj.setC2impressionBooked (numberFormat.format(new Long(cellList.get(1).getV().toString())));
		    	  dtoObj.setC3impressionDelivered (numberFormat.format(new Long(cellList.get(2).getV().toString())));
		    	  dtoObj.setC4pacing(cellList.get(3).getV().toString());
		    	  dtoObj.setImpressionBookedReport(new Long(cellList.get(1).getV().toString()));
		    	  dtoObj.setImpressionDeliveredReport(new Long(cellList.get(2).getV().toString()));
		    	  dtoObj.setPacingReport(new Double(cellList.get(3).getV().toString())/100);
		    	  mostActiveDataList.add(dtoObj);
			 }
		    	  
        } 
		return mostActiveDataList;
	}
	
	public List<NewAdvertiserViewDTO> deliveryMetricsData(String orderId, QueryDTO queryDTO){		
		QueryResponse queryResponse = null;
		NumberFormat numberFormat = NumberFormat.getInstance();
		List<NewAdvertiserViewDTO> deliveryMetricsList = new ArrayList<NewAdvertiserViewDTO>();
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  Line_Item, max(case when cost_type = 'CPM' then goal_qty else 0 end) as Booked_Impression, ")
		.append(" sum(total_impressions) as ImpressionsDelivered, sum(total_clicks) as Clicks, ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) CTR,  ")
		.append(" (max(case when cost_type = 'CPM' then ifnull(goal_qty,0) else 0 end)*max(rate))/1000 as Budget, ")
		.append(" round(sum(total_revenue),4) Spent,   ")
		.append(" (max(case when cost_type = 'CPM' then ifnull(goal_qty,0) else 0 end)*max(rate))/1000 - sum(total_revenue) as Balance, ")
		.append("  ifnull(round(sum(case when cost_type = 'CPD' then 100.0 else delivery_indicator end)/count(site_id),4),0.0) Pacing,  ")
		.append(" date(order_start_date) as Start_Date, date(order_end_date) as End_Date, Line_item_id as Line_item_id ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by Line_item,start_date, end_date, Line_item_id ")
		.append(" Order By Line_item desc ")
		.append(" ignore case ");
		
		log.info("deliveryMetrics newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
				  NewAdvertiserViewDTO dtoObj = new NewAdvertiserViewDTO();
		    	  List<TableCell> cellList = row.getF();
		    	  dtoObj.setC1lineItem(cellList.get(0).getV().toString());
		    	  dtoObj.setC2impressionBooked(numberFormat.format(new Long(cellList.get(1).getV().toString())));
		    	  dtoObj.setC4impressionDelivered(numberFormat.format(new Long(cellList.get(2).getV().toString())));
		    	  dtoObj.setC5clicks(numberFormat.format(new Long(cellList.get(3).getV().toString())));
		    	  dtoObj.setC6CTR(cellList.get(4).getV().toString());
		    	  dtoObj.setC7budget(numberFormat.format(new Double(cellList.get(5).getV().toString())));
		    	  dtoObj.setC8spent(numberFormat.format(new Double(cellList.get(6).getV().toString())));
		    	  dtoObj.setC9balance(numberFormat.format(new Double(cellList.get(7).getV().toString())));
		    	  dtoObj.setPacing(numberFormat.format(new Double(cellList.get(8).getV().toString())));
		    	  dtoObj.setD1startDateTime(cellList.get(9).getV().toString());
		    	  dtoObj.setD2endDateTime(cellList.get(10).getV().toString());
		    	  dtoObj.setLineItemId(cellList.get(11).getV().toString());
		    	  
		    	  dtoObj.setImpressionBooked(new Long(cellList.get(1).getV().toString()));
		    	  dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
		    	  dtoObj.setClicks (new Long(cellList.get(3).getV().toString()));
		    	  dtoObj.setCTR(new Double (cellList.get(4).getV().toString())/100);
		    	  dtoObj.setBudget(new Double(cellList.get(5).getV().toString()));
		    	  dtoObj.setSpent(new Double(cellList.get(6).getV().toString()));
		    	  dtoObj.setBalance(new Double(cellList.get(7).getV().toString()));
		    	  dtoObj.setPacingDouble(new Double(cellList.get(8).getV().toString()));
		    	  dtoObj.setStartDateTime( DateUtil.getDateYYYYMMDD(cellList.get(9).getV().toString()));
		    	  dtoObj.setEndDateTime(DateUtil.getDateYYYYMMDD(cellList.get(10).getV().toString()));
		    	  
		    	  deliveryMetricsList.add(dtoObj);
			 }
		    	  
        } 
		return deliveryMetricsList;
	}
	
	public QueryResponse performanceByLocationChartData(String orderId, boolean isReport, boolean isNoise, double threshold, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		 String parameter ="region,CTR,Impression"; //use to create location graph on campaign performance 
		if(isReport) {
			parameter = "*"; // use to create performanceByLocation report sheet on campaign performance 
		}
		query.append(" select "+parameter+" from ( select region , round((sum(ad_server_clicks)/sum(ad_server_impressions))*100,4) CTR,  ")
		.append(" sum(ad_server_impressions) as Impression, sum(ad_server_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		// to match by location data with core performance data in advertiser reports.
		// Requested by : Harshal 
		// change on: 11th March 2014
		// change by Shubham Goel 
		if(!isReport) {  
			query.append(" and region is not null and region <> 'null' and region <> 'N/A' and country = 'United States' ");
		}
		query.append(" GROUP EACH BY region ignore case) where Impression <> 0 ")
		.append(" ignore case ");
		
		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append("Select * from (")
				 .append(tempQuery)
				 .append(")  where Share > "+threshold);
		}
		log.info("performanceByLocationChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}
	
	public List<PerformanceByAdSizeReportDTO>  performanceByAdSizeChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		List<PerformanceByAdSizeReportDTO>  performanceByAdSizeChartDataList = new ArrayList<PerformanceByAdSizeReportDTO>();
		query.append(" Select creative_size, ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) CTR,  ") 
		.append(" sum(total_impressions) as Impression_Delivered , ")
		.append(" sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by creative_size ")
		.append(" ignore case ");

		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append("Select * from (")
				 .append(tempQuery)
				 .append(")  where Share > "+threshold);
		}
		
		log.info("performanceByAdSizeChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
				  PerformanceByAdSizeReportDTO dtoObj = new PerformanceByAdSizeReportDTO();
		    	  List<TableCell> cellList = row.getF();
		    	  dtoObj.setCreativeSize(cellList.get(0).getV().toString());
		    	  dtoObj.setCTR(new Double(cellList.get(1).getV().toString()));
		    	  dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
		    	  dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
		    	  dtoObj.setReportCTR(new Double(cellList.get(1).getV().toString()));
		    	  dtoObj.setBenchCtr("0.1");
		    	  performanceByAdSizeChartDataList.add(dtoObj);
			 }
		    	  
        } 
		return performanceByAdSizeChartDataList;
	}
	
		
	public QueryResponse impressionByOSChartData(String orderId, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select ")
		.append(" Target_Value , ")
		.append(" sum(total_impressions) as Impression_Delivered ")		
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" and Target_Category = 'OperatingSystem' ")		
		.append(" group each by Target_Value ignore case ");
		
		log.info("performanceByOSChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}
	
	public QueryResponse impressionByDeviceChartData(String orderId, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select ") 
		.append(" Target_Value , sum(total_impressions) as Impression_Delivered ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" and Target_Category = 'Platform' ")		
		.append(" group each by Target_Value ignore case ");
		
		log.info("performanceByDeviceChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}
	
	public QueryResponse impressionBymobileWebVsAppChartData(String orderId, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select site_type, SUM(total_impressions) as Impression_Delivered ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" Group Each By site_type ignore case ");
		
		log.info("mobileWebVsAppChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}
	
	/*
	 * This method will fetch all orders with ids
	 * for a given agency Id or advertiserId
	 * 
	 * @author Youdhveer Panwar
	 * @param String advertiserId,String agencyId
	 * @return Map<String,String>
	 */
	public Map<String,String> loadOrdersForAdvertiserOrAgency(String advertiserId,String agencyId, QueryDTO queryDTO) throws DataServiceException, IOException{
		if(advertiserId ==null){
			advertiserId="";
		}
		if(agencyId == null){
			agencyId="";
		}
		Map<String,String> resultMap=new LinkedHashMap<String,String>();		
		StringBuilder query=new StringBuilder();		
		query.append(" Select Order_id, Order From ")
			.append(queryDTO.getQueryData())
			.append(" Where  (Advertiser_id in ('")
			.append(advertiserId)
			.append("') or Agency_id in ('")
			.append(agencyId)
			.append("')) AND Order_id <> '0' Group By Order_id, Order order by Order,Order_id");		
		log.info("loadOrdersForAdvertiserOrAgency: Query:"+query.toString());
		
		QueryResponse queryResponse = null;
		queryDTO.setQueryData(query.toString());
		
		int j = 0;
		do {
			//log.info("Going to fetch advertisers and agencies : query attempts:"+(j+1)+" out of max attempts: 3");
			queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			//log.info("queryResponse.getJobComplete() : "+queryResponse.getJobComplete());			
			j++;
		} while (!queryResponse.getJobComplete() && j <= 3);

		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
            log.info("total row results found :"+rowList.size());
			for (TableRow row : rowList) {
				List<TableCell> cellList = row.getF();
				String id="_"+cellList.get(0).getV().toString();
				String value=cellList.get(1).getV().toString();
				resultMap.put(id, value);				
			}
		}
		return resultMap;		
	}
	
	/*
	 * This method will fetch LineChart data from bigquery for an order 
	 * 
	 *  
	 * @author Youdhveer Panwar
	 * @param String orderId
	 * @return QueryResponse
	 * @throws DataServiceException,IOException
	 */
	public QueryResponse loadLineChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException{				
		StringBuilder query=new StringBuilder();	
		query.append(" Select * from ( ")
		.append(" SELECT  date(date) date, site_name, ")
		.append(" SUM(total_impressions) as Impression, ")
		.append(" sum(total_clicks) Clicks, ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) CTR, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id = '"+orderId+"' ")
		.append(" group each by date, site_name ")
		.append(" ignore case ) ");
		
		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append(tempQuery)
				 .append("  where Share > "+threshold);
		}
		query.append(" order by date ");
		
		log.info("loadLineChartData : Query:"+query.toString());
		
		
		QueryResponse queryResponse = null;
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
             } catch (Exception e) {
 				log.severe("Query Exception = " + e.getMessage());
 				
 			}
			j++;
		} while (!queryResponse.getJobComplete() && j <= 3);

		
		return queryResponse;		
	}


	@Override
	public List<AdvertiserPerformerDTO> richMediaEventGraph(String orderId, QueryDTO queryDTO) throws DataServiceException, IOException {
		AdvertiserPerformerDTO advertiserPerformerDTO = null;
		List<AdvertiserPerformerDTO> richMediaEventGraphList = new ArrayList<AdvertiserPerformerDTO>();
		try{
			StringBuilder query=new StringBuilder();
			query.append("SELECT ifnull(market,'NA') as market, ifnull(site_name,'NA') as site_name, ")
				.append(" ifnull(campaign_category,'NA')  as campaign_category, ")
				.append(" ifnull(custom_event,'NA') as custom_event, sum(custom_count_value) as value ")
				.append(" from "+queryDTO.getQueryData())
				.append(" Where Order_id = '"+orderId+"' ")
				.append(" group each by  market, campaign_category, custom_event, site_name ")
				.append(" order by  custom_event ignore case");
			
			log.info("richMediaEventGraph query  :"+query.toString());
			
			QueryResponse queryResponse = null;
			queryDTO.setQueryData(query.toString());
			
			int j = 0;
			do {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
				j++;
			} while (!queryResponse.getJobComplete() && j <= 3);
			
			if(queryResponse == null || !queryResponse.getJobComplete()) {
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
			log.severe("Exception richMediaEventGraph of RichMediaAdvertiserDAO."+e.getMessage());
			
		}
		return richMediaEventGraphList;
	}
	
	public List<PerformanceByOSReportDTO> performanceByOSBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		List<PerformanceByOSReportDTO>  performanceByOSBarChartDataList = new ArrayList<PerformanceByOSReportDTO>();
		StringBuilder query = new StringBuilder();
		query.append(" Select ")
		.append(" Target_Value , ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR,  ")
		.append(" sum(total_impressions) as Impression_Delivered , ")
		.append(" sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" and Target_Category = 'OperatingSystem' ")		
		.append(" group each by Target_Value ignore case ");
		
		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append("Select  * from (")
				 .append(tempQuery)
				 .append(")  where Share > "+threshold);
		}
		
		log.info("performanceByOSChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		 for (TableRow row : rowList){
			 PerformanceByOSReportDTO dtoObj = new PerformanceByOSReportDTO();
	    	  List<TableCell> cellList = row.getF();
	    	  dtoObj.setTargetValue(cellList.get(0).getV().toString());
	    	  dtoObj.setCTR(new Double(cellList.get(1).getV().toString()));
	    	  dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
	    	  dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
	    	  dtoObj.setReportCTR(new Double(cellList.get(1).getV().toString())/100);
	    	  dtoObj.setBenchCtr("0.1");
	    	  performanceByOSBarChartDataList.add(dtoObj);
		 }
		}
		return performanceByOSBarChartDataList;
	}
	
	public List<PerformanceByDeviceReportDTO> performanceByDeviceBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		List<PerformanceByDeviceReportDTO>  performanceByDeviceBarChartDataList = new ArrayList<PerformanceByDeviceReportDTO>();
		StringBuilder query = new StringBuilder();
		query.append(" Select ") 
		.append(" Target_Value, ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR,  ")
		.append(" sum(total_impressions) as Impression_Delivered, ")
		.append(" sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" and Target_Category = 'Platform' ")		
		.append(" group each by Target_Value ignore case ");
		
		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append("Select * from (")
				 .append(tempQuery)
				 .append(")  where Share > "+threshold);
		}
		
		log.info("performanceByDeviceChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		 for (TableRow row : rowList){
			 PerformanceByDeviceReportDTO dtoObj = new PerformanceByDeviceReportDTO();
	    	  List<TableCell> cellList = row.getF();
	    	  dtoObj.setTargetValue(cellList.get(0).getV().toString());
	    	  dtoObj.setCTR(new Double(cellList.get(1).getV().toString()));
	    	  dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
	    	  dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
	    	  dtoObj.setReportCTR(new Double(cellList.get(1).getV().toString())/100);
	    	  dtoObj.setBenchCtr("0.1");
	    	  performanceByDeviceBarChartDataList.add(dtoObj);
		 }
		}
		return performanceByDeviceBarChartDataList;
	}
	
	public List<PerformanceByPlacementReportDTO> mobileWebVsAppBarChartData(String orderId, boolean isNoise, double threshold, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		List<PerformanceByPlacementReportDTO>  mobileWebVsAppBarChartDataList = new ArrayList<PerformanceByPlacementReportDTO>();
		StringBuilder query = new StringBuilder();
		query.append(" Select site_type, ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR, ")
		.append(" sum(total_impressions) as Impression_Delivered , ")
		.append(" sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by site_type ignore case ");
		
		if(isNoise) {
			String tempQuery = query.toString();
			query = new StringBuilder();
			query.append("Select * from (")
				 .append(tempQuery)
				 .append(")  where Share > "+threshold);
		}
		
		log.info("mobileWebVsAppChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
				 PerformanceByPlacementReportDTO dtoObj = new PerformanceByPlacementReportDTO();
		    	  List<TableCell> cellList = row.getF();
		    	  dtoObj.setSite(cellList.get(0).getV().toString());
		    	  dtoObj.setCTR(new Double(cellList.get(1).getV().toString()));
		    	  dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
		    	  dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
		    	  dtoObj.setReportCTR(new Double(cellList.get(1).getV().toString())/100);
		    	  dtoObj.setBenchCtr("0.1");
		    	  mobileWebVsAppBarChartDataList.add(dtoObj);
			 }
		    	  
       } 
		return mobileWebVsAppBarChartDataList;
	}

	@Override
	public QueryResponse impressionsByAdSizeChartData(String orderId, QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select creative_size, ")
		.append(" sum(total_impressions) as impression_delivered ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by creative_size ")
		.append(" ignore case ");
		
		log.info("impressionsByAdSizeChartData newAdvertiserView Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}
	
	public QueryResponse videoCampaignData(String orderId, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select date(date) as date, ")
		.append(" round(sum(averageInteractionRate)/count(date),4) as averageInteractionRate,")
		.append(" round(sum(averageViewRate)/count(date),4) as averageViewRate,")
		.append(" sum(completionRate) as completionRate, sum(start) as start,sum(firstQ)FirstQ,")
		.append(" sum(mip) as MIP,sum(thirdQ)ThirdQ,sum(complete) as complete, sum(pause) as pause,")
		.append(" sum(resum) as resum,sum(rewind) as rewind, sum(mut) as Mut, Sum(unmut) as unmut, ")
		.append(" sum(fullS) as FullS, order_id")
		.append(" From ( ")
		.append(" Select order_id, Line_Item_Id, Line_Item, date, count(date), ")
		.append(" round(sum(average_Interaction_Rate)/count(date),4) as averageInteractionRate,")
		.append(" round(sum(average_View_Rate)/count(date),4) as averageViewRate,")
		.append(" sum(completion_Rate) as completionRate, sum(start) as start,sum(firstQuartile)FirstQ,")
		.append(" sum(midpoint) as MIP,sum(third_Quartile)ThirdQ,sum(complete) as complete, sum(pause) as pause, ")
		.append(" sum(resume) as resum,sum(rewind) as rewind, sum(mute) as Mut, Sum(unmute) as unmut,")
		.append(" sum(fullScreen) as FullS ")
		.append(" from "+queryDTO.getQueryData())
		.append(" where order_id = '"+orderId+"' ")
		//.append(" where order_id = '147223902' ")
		.append(" Group By order_id, Line_Item_Id, Line_Item, date)")
		.append(" Group By order_id, date Order By date Ignore Case");
		
		log.info("videoCampaignData Query  : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		return queryResponse;
	}


	@Override
	public List<ChannelPerformancePopUpDTO> campaignGridData(boolean activeCampaign, String commaSeperatedAccountIds, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		/*
		*
		*
		*
		*
		*
		*
		*
		*
		*
		*/
		List<ChannelPerformancePopUpDTO> channelPerformancePopUpDTOList = new ArrayList<ChannelPerformancePopUpDTO>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT Advertiser_ID, order_id, order as name, sum(total_impressions) as ImpressionsDelivered, ")
			.append(" sum(total_clicks) as Clicks, ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) CTR, ")  
			.append(" round(sum(total_revenue),4) Spent, ") 
			.append(" ifnull(round(sum(case when cost_type = 'CPD' then 100.0 else delivery_indicator end)/count(site_id),4),0.0) Pacing, ")
			.append(" from "+queryDTO.getQueryData());
			if(activeCampaign) {
				query.append(" where date(order_end_date) >= date(current_date()) ");
			}
			query.append(" group each by Advertiser_ID, order_id, name Order By order_id asc ignore case");
		
		log.info("campaignGridData Query  : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);
		
		 if (queryResponse!=null && queryResponse.getRows() != null) {
			 List<TableRow> rowList = queryResponse.getRows();
		     
			 for (TableRow row : rowList){
				 ChannelPerformancePopUpDTO dtoObj = new ChannelPerformancePopUpDTO();
		    	  List<TableCell> cellList = row.getF();
		    	  dtoObj.setAdvertiserId(new Long(cellList.get(0).getV().toString()));
		    	  dtoObj.setCampaignId(new Long(cellList.get(1).getV().toString()));
		    	  dtoObj.setCampaignName(cellList.get(2).getV().toString());
		    	  dtoObj.setCampaignImpressions(new Long(cellList.get(3).getV().toString()));
		    	  dtoObj.setCampaignClicks(new Long(cellList.get(4).getV().toString()));
		    	  dtoObj.setCampaignCtr(new Double(cellList.get(5).getV().toString()));
		    	  dtoObj.setCampaignSpend(new Double(cellList.get(6).getV().toString()));
		    	  dtoObj.setCampaignPacing(new Double(cellList.get(7).getV().toString()));
		    	  
		    	  channelPerformancePopUpDTOList.add(dtoObj);
			 }
		    	  
        } 
		return channelPerformancePopUpDTOList;
	}
	
}
