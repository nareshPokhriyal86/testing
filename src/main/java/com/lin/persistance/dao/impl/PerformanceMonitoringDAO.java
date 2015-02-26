package com.lin.persistance.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IPerformanceMonitoringDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.LinMobileVariables;


public class PerformanceMonitoringDAO implements IPerformanceMonitoringDAO {
	
	private static final Logger log = Logger.getLogger(PerformanceMonitoringDAO.class.getName());
	private Objectify obfy = OfyService.ofy();	
	//private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);

	@Override
	public QueryResponse headerData(String orderId, String lineItemId, QueryDTO queryDTO) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select sum(ImpressionsDelivered) as Impression_Delivered, ")
		.append(" Sum(Clicks) as Clicks, ifnull(round((Sum(Clicks)/sum(ImpressionsDelivered))*100,4),0.0) as CTR, Order_id ")
		.append(" from ( SELECT Order_id, ")
		.append("line_item_id, Sum(total_impressions) as ImpressionsDelivered,  ")
		.append("Sum(total_clicks) as Clicks, ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"')" );
		}
		query.append(" group each by Order_id, line_item_id ignore case ) ")
		.append(" Group By Order_id ignore case "); 
		log.info("headerData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid headerData query or no data found...");
		}
		return queryResponse;
	}

	/*@Override
	public QueryResponse performanceAllLineItems(String orderId, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select Line_Item_id, substr(dateLine,27,Length(dateLine)) as line ")
		.append(" from ( SELECT Line_Item_id, MAX(concat(FORMAT_UTC_USEC(Date), Line_Item)) as dateLine ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'")
		.append(" group each by line_item_id ignore case )z ")
		.append(" Group By line,Line_Item_id ignore case "); 
		log.info("performanceAllLineItems Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid performanceAllLineItems query or no data found...");
		}
		return queryResponse;
	}*/

	@Override
	public QueryResponse clicksLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient,String userCompany) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select * from ( ")
		.append(" SELECT  date(date) date, ");
		
		if(!isClient)
			query.append(" line_item_id, ");
		else
			query.append(" '"+ userCompany +"', ");
		
		query.append(" sum(total_clicks) Clicks, ")
		.append(" SUM(total_impressions) as Impression, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id = '"+orderId+"' ");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by date ");
		
		if(!isClient)
			query.append(", line_item_id ");
		
		query.append(" ignore case ) ");
		
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		query.append(" order by date ");
		log.info("clicksLineChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid clicksLineChartData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse impressionsLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient,String userCompany) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select * from ( ")
		.append(" SELECT  date(date) date, ");
		
		if(isClient)
			query.append(" '"+userCompany+"',");
		else
			query.append(" line_item_id,  ");
		
		query.append(" SUM(total_impressions) as Impression, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id = '"+orderId+"' ");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by date");
		
		if(!isClient)
			query.append(" , line_item_id  ");
		
		query.append(" ignore case )   ");
		
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		query.append(" order by date ");
		log.info("impressionsLineChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid impressionsLineChartData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public List<PerformanceMonitoringDTO> loadAllCampaigns(QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		PerformanceMonitoringDTO dtoObj = new PerformanceMonitoringDTO();
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<PerformanceMonitoringDTO>();
		StringBuilder query = new StringBuilder();
		try{
			query.append(" Select Order_ID,substr(dateorder,27,Length(dateorder)) as Order, sum(ImpressionsDelivered) as Impression_Delivered, ")
			.append(" Sum(Clicks) as Clicks, ifnull(round((Sum(Clicks)/sum(ImpressionsDelivered))*100,4),0.0) as CTR, ")
			.append(" Date( Min(Order_Start_Date)) as order_start_date,	Date(max(Order_End_date)) as order_end_date,Advertiser ")
			.append(" from ( SELECT Order_id, MAX(concat(FORMAT_UTC_USEC(Date), Order)) as dateorder, ")
			.append(" Min(order_start_date) as Order_Start_Date, max(order_end_date) as Order_End_date, ")
			.append("line_item_id, Sum(total_impressions) as ImpressionsDelivered,  ")
			.append("Sum(total_clicks) as Clicks,Advertiser, ")
			.append(" from "+queryDTO.getQueryData())
			//.append(" where Order_id in ('"+orderIds+"')")
			.append(" group each by Order_id, line_item_id,Advertiser ignore case ) ")
			.append(" Group By Order,Order_Id,Advertiser ignore case "); 
			log.info("loadAllCampaigns PerformanceMonitoringDAO :: Query  :"+query);
			
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
			    /*	  String endDate=cellList.get(8).getV().toString();
			    	  if(endDate !=null && endDate.indexOf("java")>=0){
			    		  endDate="None";
			    	  }*/
			    	  
			    	  dtoObj= new PerformanceMonitoringDTO(
			    				 (cellList.get(0).getV().toString()),
			    				 (cellList.get(1).getV().toString()),
			    				 (cellList.get(2).getV().toString()),
			    				 (cellList.get(3).getV().toString()), 
			    				 (cellList.get(4).getV().toString()), 
			    				 (cellList.get(5).getV().toString()), 
			    				 (cellList.get(6).getV().toString()),
			    				 (cellList.get(7).getV().toString())
			    				);
			    	  
			    	  campaignList.add(dtoObj);
				 }
			    	  
	 }	
		}catch(Exception e){
			log.info("Exception in loadAllCampaigns of PerformanceMonitoringDAO : "+e.getMessage());
			
		}

		return campaignList;
		
	}
	
	@Override
	public  List<SmartCampaignObj>  getRunningCampaignListSuperUser(String campaignStatus) throws Exception{
		 log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		 obfy.clear();
		 List<SmartCampaignObj> cmapaignList = new ArrayList<>();
		 if(campaignStatus.equals("1")){
			 log.info("inside superadmin status 1");
			 cmapaignList = obfy.load().type(SmartCampaignObj.class)
					// .filter("campaignStatus != ",	"4")
					 /* Added by Anup | to sort campaigns by start date */
					 .order("-sDate")
					 .order("-eDate") 
                     .list();
		 } 
		 else{
			 log.info("inside superadmin status !1");
			  cmapaignList = obfy.load().type(SmartCampaignObj.class)
					   .filter("campaignStatus = ",	campaignStatus)
					   /* Added by Anup | to sort campaigns by start date */
					   .order("-sDate")
					   .order("-eDate") 
                   .list();
		 }
		 return cmapaignList;
	}
	
	@Override
	public  List<SmartCampaignObj>  getRunningCampaignList(String campaignStatus, String companyId) throws Exception{
		 log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		 obfy.clear();
		 List<SmartCampaignObj> cmapaignList = new ArrayList<>();
	if(campaignStatus.equals("1")){
		log.info("inside user status 1");
			 cmapaignList = obfy.load().type(SmartCampaignObj.class)
					 .filter("companyId = ",companyId)
					//  .filter("campaignStatus != ",	"4")
					 /* Added by Anup | to sort campaigns by start date */
					 .order("-sDate") 
					 .order("eDate")
                     .list();
		 } 
		 else{
			 log.info("inside user status !1");
			  cmapaignList = obfy.load().type(SmartCampaignObj.class)
					  .filter("companyId = ",companyId)
					   .filter("campaignStatus = ",	campaignStatus)
					   /* Added by Anup | to sort campaigns by start date */
					   .order("-sDate")
					   .order("eDate")
					   .list();
		 }
		 return cmapaignList;
	}

	@Override
	public QueryResponse deliveryMetricsData(String orderId, String lineItemIds, QueryDTO queryDTO, boolean multipleOptionSelected) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		if(multipleOptionSelected) {
			query.append("Select * from (SELECT  date(date) date, sum(total_clicks) Clicks, ")
			.append(" SUM(total_impressions) as Impression, line_item_id from ")
			.append(queryDTO.getQueryData())
			.append(" Where Order_id = '"+orderId+"' ");
			if(!lineItemIds.equalsIgnoreCase("All")) {
				query.append(" and line_item_id in ('"+lineItemIds+"') ");
			}
			query.append(" group each by date, line_item_id ignore case ) ")
			.append(" order by date, line_item_id");
		}else {
			query.append(" SELECT  date(date) date, sum(total_clicks) Clicks, ")
			.append(" SUM(total_impressions) as Impression from ")
			.append(queryDTO.getQueryData())
			.append(" Where Order_id = '"+orderId+"' ");
			if(!lineItemIds.equalsIgnoreCase("All")) {
				query.append(" and line_item_id in ('"+lineItemIds+"') ");
			}
			query.append(" group each by date order by date ignore case ");
		}
		log.info("deliveryMetricsData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid deliveryMetricsData query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse ctrLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient,String userCompany) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select * from ( ")
		.append(" SELECT  date(date) date, ");
		
		if(!isClient)
			query.append(" line_item_id, ");
		else
			query.append(" '"+ userCompany +"', ");
		
		query.append(" sum(total_clicks) Clicks, ")
		.append(" SUM(total_impressions) as Impression, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id = '"+orderId+"' ");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by date ");
		
		if(!isClient)
			query.append(" , line_item_id  ");
		
		query.append("ignore case ) ");
		
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		query.append(" order by date ");
		
		if(!isClient)
			query.append(" , line_item_id  ");
		
		log.info("ctrLineChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid ctrLineChartData query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse flightLineChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select * from ( ")
		.append(" SELECT  date(date) date, line_item_id, ")
		.append(" SUM(total_impressions) as Impression, ")
		.append(" sum(total_clicks) Clicks, ")
		.append("RATIO_TO_REPORT(Impression) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id = '"+orderId+"' ");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by date, line_item_id ignore case ) ");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		query.append(" order by date ");
		log.info("flightLineChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid flightLineChartData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse locationTargetData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO, String stateStr, String cityStr, String dmaStr) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" select sum(Impression) from (select region,round((sum(total_clicks)/sum(Impression))*100,4) CTR, sum(Impression) as Impression, ")
		.append(" RATIO_TO_REPORT(Impression) OVER() as Share from ( select region, City_ID, City, Region_ID , sum(ad_server_clicks) as total_clicks, ")
		.append(" sum ( ad_server_impressions ) as Impression ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" and region is not null and region <> 'null' and region <> 'N/A' and country = 'United States' ")
		.append(" GROUP EACH BY region, City_ID, City, Region_ID ignore case) as T1 ");
		if(dmaStr != null && dmaStr.trim().length() > 0) {
			query.append(" left join ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID+".citiesDma] as T2 on T2.criteriaid = T1.City_ID ");
		}
		if(stateStr != null && stateStr.trim().length() > 0) {
			query.append(" left join ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID+".location] as T3 on T3.criteriaid = T1.Region_ID ");
		}
		query.append(" where Impression <> 0 ");
		if((cityStr != null && cityStr.trim().length() > 0) || (dmaStr != null && dmaStr.trim().length() > 0) || (stateStr != null && stateStr.trim().length() > 0)) {
			query.append(" and ( ");
			if(cityStr != null && cityStr.trim().length() > 0) {
				query.append(" T1.City IN ('"+cityStr.trim()+"') ");
			}
			if(dmaStr != null && dmaStr.trim().length() > 0) {
				if(cityStr != null && cityStr.trim().length() > 0) {
					query.append(" or ");
				}
				query.append(" T2.dmaregioncode IN ('"+dmaStr+"') ");
			}
			if(stateStr != null && stateStr.trim().length() > 0) {
				if((cityStr != null && cityStr.trim().length() > 0) || (dmaStr != null && dmaStr.trim().length() > 0)) {
					query.append(" or ");
				}
				query.append(" T3.name IN ('"+stateStr+"') and T3.targettype = 'State' ");
			}
			if((cityStr != null && cityStr.trim().length() > 0) || (dmaStr != null && dmaStr.trim().length() > 0) || (stateStr != null && stateStr.trim().length() > 0)) {
				query.append(" ) ");
			}
		}
		
		query.append(" group by region ignore case )");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("locationTargetData Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid locationTargetData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse locationCompleteData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" select region, CTR, Impression from (select region,round((sum(total_clicks)/sum(Impression))*100,4) CTR, sum(Impression) as Impression, ")
		.append(" RATIO_TO_REPORT(Impression) OVER() as Share from ( select region, Region_ID , sum(ad_server_clicks) as total_clicks, ")
		.append(" sum ( ad_server_impressions ) as Impression ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" and region is not null and region <> 'null' and region <> 'N/A' and country = 'United States' ")
		.append(" GROUP EACH BY region, Region_ID ignore case) as T1 ")
		.append(" where Impression <> 0 ")
		.append(" group by region ignore case )");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		
		log.info("locationCompleteData Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid locationCompleteData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse locationTopCitiesData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" select city_name, Impression, CTR, region, dmaregioncode from (select T1.city as city_name, sum(Impression) as Impression, round((sum(total_clicks)/sum(Impression))*100,4) CTR, region, ")
		.append(" T2.dmaregioncode, sum(total_clicks) as Clicks, RATIO_TO_REPORT(Impression) OVER() as Share from ( select City, City_ID, region, Region_ID , sum(ad_server_clicks) as total_clicks, ")
		.append(" sum ( ad_server_impressions ) as Impression ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" and region is not null and region <> 'null' and region <> 'N/A' and City is not null and City <> 'null' and City <> 'N/A' ")
		.append(" and country = 'United States' GROUP EACH BY region, City, City_ID, Region_ID ignore case) as T1 ")
		.append(" left join ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID+".citiesDma] as T2 on T2.criteriaid = T1.City_ID ")
		.append(" where Impression <> 0 ")
		.append(" group by region, city_name, T2.dmaregioncode order by Impression desc ignore case )");
		if(isNoise) {
			query.append(" where Share > "+threshold);
		}
		query.append(" limit 5");
		
		log.info("locationTopCitiesData Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid locationTopCitiesData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse richMediaLineChartData(String orderId, String lineItemId, QueryDTO queryDTO, boolean isClient, String userCompany) throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" SELECT date(Date) as date, ");
		if(isClient) {
			query.append(" '"+userCompany+"' as line_item_id ,");
		} else {
			query.append(" line_item_id ,");
		}
		query.append(" Custom_Event, if((Custom_Event_Type = 'Timer'),sum(Custom_Time_Value),sum(Custom_Count_Value)) as countValue, Custom_Event_Type ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group by date ");
		if(!isClient) {
			query.append(" ,line_item_id ");
		}
		query.append(" , Custom_Event, Custom_Event_Type ")
		.append(" order by date, Custom_Event  ignore case");
		
		log.info("richMediaLineChartData Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("richMediaLineChartData Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid richMediaLineChartData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse videoData(String orderId, String lineItemIds, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select order_id, Line_Item_Id, date, Creative_Type, sum(Average_View_Time) as averageViewTime, sum(Video_Length) as videoLength, ")
		.append(" sum(start) as start, sum(pause) as pause, sum(resume) as resume, sum(rewind) as rewind, sum(mute) as Mute, Sum(unmute) as unmute, sum(fullScreen) as fullScreen, ")
		.append(" sum(first_Quartile)as firstQuartile, sum(midpoint) as midpoint, sum(third_Quartile) as thirdQuartile, sum(complete) as complete, ")
		.append(" sum(Rich_Media_Interaction_Time) as rmInteractionTime, sum(Rich_Media_Interaction_Count) as rmInteractionCount, ")
		.append(" sum(Rich_Media_Interaction_Impressions) as rmInteractionImpressions, sum(RICH_MEDIA_DISPLAY_TIME) as rmDisplayTime ")
		
		/*.append(" sum(Rich_Media_Expansions) as rmExpansions, sum(Rich_Media_Expanding_Time) as rmExpandingTime, sum(Rich_Media_Interaction_Time) as rmInteractionTime, ")
		.append(" sum(Rich_Media_Interaction_Count) as rmInteractionCount, sum(Rich_Media_Interaction_Rate) as rmInteractionRate, sum(Rich_Media_Average_Interaction_Time) as rmAvgInteractionTime, ")
		.append(" sum(Rich_Media_Interaction_Impressions) as rmInteractionImpressions, sum(Rich_Media_Manual_Closes) as rmManualCloses, sum(Rich_Media_FullScreen_Impressions) as rmFullScreenImps, ")
		.append(" sum(Rich_Media_Video_Interactions) as rmVideoInteractions, sum(Rich_Media_Video_Interaction_Rate) as rmVideoInteractionRate, sum(Rich_Media_Video_Mutes) as rmVideoMutes, ")
		.append(" sum(Rich_Media_Video_Pauses) as rmPauses, sum(Rich_Media_Video_Playes) as rmPlayes, sum(Rich_Media_Video_Midpoints) as rmMidpoints, ")
		.append(" sum(Rich_Media_Video_Completes) as rmCompletes, sum(Rich_Media_Video_Replays) as rmReplays, sum(Rich_Media_Video_Stops) as rmStops, ")
		.append(" sum(Rich_Media_Video_Unmutes) as rmUnmutes, sum(Rich_Media_Average_Display_Time) as rmAverageDisplayTime, sum(Rich_Media_Video_View_Rate) as rmVideoViewRate, ")
		.append(" sum(Rich_Media_Video_View_Time) as rmVideoViewTime ")*/
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"' ");
		//.append(" Where Order_id='173940102' ");
		if(!lineItemIds.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemIds+"') ");
		}
		query.append(" Group By order_id, Line_Item_Id, date, Creative_Type order by date Ignore Case ");
		
		log.info("videoData Query  :"+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("videoData Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid videoData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse loadCreativeChartData(String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient , String userCompany) throws DataServiceException, IOException{
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" select Impression_Delivered,Clicks,CTR,line_item_id,creative_size from ( Select sum(total_impressions) as Impression_Delivered ,sum(total_clicks) as Clicks, ")
		.append(" ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) CTR, ");
		
		if(isClient)
			query.append(" '"+userCompany+"' as line_item_id,");
		else
			query.append(" line_item_id,");
		
		
		query.append("creative_size, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		
		query.append(" group each by  creative_size ");
		
		if(!isClient)
			query.append(" ,line_item_id ");
		
		query.append(" ignore case ) ");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("loadCreativeChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadCreativeChartData query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse loadCreativeImpressionClicksChartDate(
			String orderId, String lineItemId, boolean isNoise, double threshold, QueryDTO queryDTO)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("select creative_size,Impression_Delivered,Clicks from ( select creative_size,sum(total_impressions) as Impression_Delivered, sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		
	/*	if(isNoise) {
			query.append("  where Share > "+threshold);
		}*/
		query.append(" group each by  creative_size order by Impression_Delivered desc , Clicks desc ignore case )");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("loadCreativeImpressionClicksChartDate Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
				}
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadCreativeImpressionClicksChartDate query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse loadOSChartData(String orderId, String lineItemId,
			boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient, String userCompany)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("select Impression_Delivered,Clicks,CTR,line_item_id,Target_Value from ( select sum(total_impressions) as Impression_Delivered ,sum(total_clicks) as Clicks,")
		.append("ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR, ");
		
		if(isClient)
			query.append(" '"+userCompany+"' as line_item_id ,");
		else
			query.append(" line_item_id ,");
		
		query.append("  case when Target_Value like '%Windows%' then 'Windows' when Target_Value like 'Android%' then 'Android' when Target_Value like '%iOS%' then 'iOS' else 'Other OS' End as Target_Value, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"' and Target_Category = 'OperatingSystem' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by  Target_Value");
		
		if(!isClient)
			query.append(" , line_item_id ");
		
		query.append(" ignore case ) ");
		
		
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("OSChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
				try{
					Thread.sleep(1000);
				}catch(Exception e1){
				}
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadOSChartData query or no data found...");
		}
		return queryResponse;
	}
	
	
	@Override
	public QueryResponse loadDeviceChartData(String orderId, String lineItemId,
			boolean isNoise, double threshold, QueryDTO queryDTO,boolean isClient, String userCompany)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("select * from ( select sum(total_impressions) as Impression_Delivered ,sum(total_clicks) as Clicks,")
		.append("ifnull(round((sum(total_clicks)/sum(total_impressions))*100,4),0.0) as CTR,  ");
		
		if(isClient)
			query.append(" '"+userCompany+"' as line_item_id , ");
		else
			query.append(" line_item_id , ");
		
		query.append(" case when Target_Value = 'MidRangeMobile' then 'Smart Phone' when  Target_Value = 'HighEndMobile' then 'Smart Phone' Else Target_Value End as Target_Value,")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"' and Target_Category = 'Platform' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by  Target_Value ");
		
		if(!isClient)
			query.append(" , line_item_id  ");
		
		query.append(" ignore case ) ");
		
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		
		log.info("loadDeviceChartData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadOSChartData query or no data found...");
		}
		return queryResponse;
	}
		
	@Override
	public QueryResponse loadDeviceImpressionClicksChartDate(
			String orderId, String lineItemId,boolean isNoise, double threshold, QueryDTO queryDTO)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("select case when Target_Value = 'MidRangeMobile' then 'Smart Phone' when  Target_Value = 'HighEndMobile' then 'Smart Phone' Else Target_Value End as Target_Value,Impression_Delivered,Clicks from ( select Target_Value,sum(total_impressions) as Impression_Delivered, sum(total_clicks) as Clicks,")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"'and Target_Category = 'Platform' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by  Target_Value order by Impression_Delivered desc , Clicks desc ignore case )");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("loadDeviceImpressionClicksChartDate Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadDeviceImpressionClicksChartDate query or no data found...");
		}
		return queryResponse;
	}
	
	@Override
	public QueryResponse loadOperatingSystemImpressionClicksChartDate(
			String orderId, String lineItemId,boolean isNoise, double threshold, QueryDTO queryDTO)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("select Target_Value,Impression_Delivered,Clicks from ( select  ")
		
		.append("  case when Target_Value like '%Windows%' then 'Windows' when Target_Value like 'Android%' then 'Android' when Target_Value like '%iOS%' then 'iOS' else 'Other OS' End as Target_Value, ")

		.append(" sum(total_impressions) as Impression_Delivered, sum(total_clicks) as Clicks, ")
		.append("RATIO_TO_REPORT(Impression_Delivered) OVER() as Share ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"' and Target_Category = 'OperatingSystem' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		query.append(" group each by  Target_Value order by Impression_Delivered desc , Clicks desc ignore case )");
		if(isNoise) {
			query.append("  where Share > "+threshold);
		}
		log.info("loadOperatingSystemImpressionClicksChartDate Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadOperatingSystemImpressionClicksChartDate query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse loadOperatingTotalGoal(String orderId,
			String lineItemId, String targetOS, QueryDTO queryDTO)
			throws DataServiceException, IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		String target = "";
		
		query.append("select sum(total_impressions) as Imps, sum(total_clicks) as Clicks")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where Order_id='"+orderId+"' and Target_Category = 'OperatingSystem' ");
		
		targetOS = targetOS.trim();
		// Checked if Target OS is selected or not
		if( targetOS != "" && targetOS != null && targetOS.length() > 0){
			String[] tempTarget = targetOS.split(",");
			
			for (String temp : tempTarget) {
				if(temp != "" && !temp.equalsIgnoreCase("ALL")){ // ALL is for datastore reference not valid for bigquery.
					if(target == "")
						target = " target_value like '%"+temp+"%' ";
					else
						target += " or target_value like '%"+temp+"%' ";
				}
			}
			if(target!=null && !target.trim().equals("") && !target.isEmpty() ){
				query.append(" and ( "+ target +" )");
			}
			
		}
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		
		
		query.append("ignore case ");
		log.info("loadOperatingTotalGoal Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadOperatingTotalGoal query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse loadRMCustomEventList(String orderId,
			String lineItemId, QueryDTO queryDTO) throws DataServiceException,
			IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT Custom_Event, Custom_Event_Type from ")
		.append(queryDTO.getQueryData())
		.append(" where Order_id = '"+orderId+"' and Custom_Event_Id !='23596902' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		
		query.append(" group by Custom_Event, Custom_Event_Type order by Custom_Event ignore case ");
		log.info("loadRMCustomEventList Query : "+query);
				
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadRMCustomEventList query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public QueryResponse loadRMCustomEventCards(String orderId,
			String lineItemId, QueryDTO queryDTO) throws DataServiceException,IOException {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		//query.append("SELECT Custom_Event, Custom_Event_Type , Creative_Size, sum(Custom_Count_Value) as counter , sum(Custom_Time_Value) as timer from ")
		query.append("SELECT Custom_Event, Custom_Event_Type , Creative_Size, if((Custom_Event_Type = 'Timer'),sum(Custom_Time_Value),sum(Custom_Count_Value)) as CountOrTimeValue from ")
			 .append(queryDTO.getQueryData())
			 .append(" where Order_id = '"+orderId+"'");
		
		//query.append(" and Custom_Event_Id !='23596902' ");
		
		if(!lineItemId.equalsIgnoreCase("All")) {
			query.append(" and line_item_id in ('"+lineItemId+"') ");
		}
		
		query.append(" group by Custom_Event, Creative_Size  ,Custom_Event_Type order by CountOrTimeValue desc, Custom_Event, Creative_Size ignore case ");
		log.info("loadRMCustomEventCards Query : "+query);
				
		queryDTO.setQueryData(query.toString());
		int j=0;
		do{
            try {
            	 Thread.sleep(15000);
				 queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				 log.severe("Query Exception = " + e.getMessage());
				 
			}
			j++;
		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
		
		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
			log.warning("Invalid loadRMCustomEventCards query or no data found...");
		}
		return queryResponse;
	}

	@Override
	public void updateSmartCampaignData() {
		log.info("Inside updateCapaignData @ PerformanceMontoringDAO");
		 obfy.clear();
		 List<SmartCampaignObj> campaignList = new ArrayList<>();
		 campaignList = obfy.load().type(SmartCampaignObj.class).list();
		 log.info("Total Campign found : " + campaignList.size());
		 int i=0;
		 for (Iterator iterator = campaignList.iterator(); iterator.hasNext();) {
			SmartCampaignObj smartCampaignObj = (SmartCampaignObj) iterator.next();
			smartCampaignObj.setsDate(smartCampaignObj.getStartDate());
			smartCampaignObj.seteDate(smartCampaignObj.getEndDate());
			
			obfy.save().entity(smartCampaignObj).now();
			log.info("update : " + ++i + " records");
		}
	}

	/*
	 * @author Anup Dutta
	 * @description This method takes rankRatio, Rank Group (zip,Dma,state) and Census columns
	 * @return rank for location  
	 **/
	@Override
	public QueryResponse getLocationRank(String mainPopulationColumn,int rankRatio, String rankBy,StringBuffer rankStmt,StringBuffer categoryStmt,StringBuffer subQueryCategoryStmt, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		if(rankBy.equalsIgnoreCase("zip")){
			query.append("select zip, "+mainPopulationColumn+" , " + rankStmt + ", PERCENT_RANK() OVER (order by avg_pop) as zip_rank "+ subQueryCategoryStmt)
			.append(" from " + queryDTO.getQueryData());
			queryDTO.setQueryData(query.toString());
		}else if(rankBy.equalsIgnoreCase("state")){
			
			query.append(" SELECT stateid,state,sum( "+mainPopulationColumn+" ) as total_pop, sum(avg_pop) as av_pop,PERCENT_RANK() OVER (order by av_pop) as state_rank "+ categoryStmt+" from ( ")
			.append(" SELECT stateid , state ,  "+mainPopulationColumn+"  ,  " + rankStmt + subQueryCategoryStmt + " from GEO.census as a left join GEO.zip_state as b on b.zip = a.zip where b.code is not null") 
			.append(" ) group by stateid,state");
			
			queryDTO.setQueryData(query.toString());
		}else if(rankBy.equalsIgnoreCase("dma")){
			query.append("SELECT DMA_Code, DMA_Description, sum( "+mainPopulationColumn+" ) as total_pop, sum(avg_pop) as av_pop,PERCENT_RANK() OVER (order by av_pop) as dma_rank "+ categoryStmt+" from ( ")
			.append("SELECT DMA_Code , DMA_Description,  "+mainPopulationColumn+"  , " + rankStmt + subQueryCategoryStmt +" from GEO.census as a left join GEO.zip_dma as b on b.Zip_Code = a.zip where b.DMA_Code is not null")
			.append(" ) group by DMA_Code, DMA_Description");
			queryDTO.setQueryData(query.toString());
		}
		
		log.info(query.toString());
		try{
			 queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
		}catch(Exception e){
			log.info("Inside getLocationRank : " + e.getMessage());
		}
		return queryResponse;
	}

	/*
	 * @author Anup Dutta
	 * @description This method takes Group (Zip,dma,state) and AdUnits
	 * @return performance of product based on group and adUnit based on Filter criteria 
	 **/
	@Override
	public QueryResponse getProductPerformance(String groupBy,
			String productTable, QueryDTO queryDTO,List<String> adUnits) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		StringBuffer adUnitClause = new StringBuffer();
		if(adUnits.size() > 0){
			adUnitClause.append(" where AdUnitId1 in ( ");
			for(int i=0;i<adUnits.size();i++){
				if(i==0){
					adUnitClause.append(adUnits.get(i));
				}else{
					adUnitClause.append(" , "+adUnits.get(i));
				}
			}
			adUnitClause.append(" ) ");
		}
		
		if(groupBy.equalsIgnoreCase("dma")){
			query.append("SELECT b.dmaregioncode as DMACode ,round(avg(a.Impressions)) as Impressions, round(avg(a.Clicks)) as Clicks FROM ")
			.append("( select * from " + productTable + adUnitClause + " ) as a ")
			.append(" left join GEO.Cities_DMA as b on a.cityid = b.criteriaid ")
			.append(" where b.criteriaid is not null group by DMACode order by Impressions desc ,Clicks desc");
			queryDTO.setQueryData(query.toString());
		}else if(groupBy.equalsIgnoreCase("state")){
			
			query.append("SELECT c.criteriaid as stateid,round(avg(a.Impressions)) as Impressions, round(avg(a.Clicks)) as Clicks FROM ")
			.append("( select * from " + productTable + adUnitClause + " ) as a ")
			.append("left join GEO.location as b on a.cityid = b.criteriaid ")
			.append("left join GEO.location as c on b.parentid = c.criteriaid ")
			.append("where c.criteriaid is not null group by stateid order by Impressions desc ,Clicks desc ");
			
			queryDTO.setQueryData(query.toString());
		}else if(groupBy.equalsIgnoreCase("city")){
			
			query.append("SELECT b.criteriaid as cityid,round(avg(a.Impressions)) as Impressions, round(avg(a.Clicks)) as Clicks FROM ")
			.append("( select * from " + productTable + adUnitClause + " ) as a ")
			.append(" left join GEO.location as b on a.cityid = b.criteriaid ")
			.append(" where b.criteriaid is not null group by cityid order by Impressions desc ,Clicks desc ");
			
			queryDTO.setQueryData(query.toString());
		}
		
		log.info("getProductPerf : " + query);
		try{
			 queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
		}catch(Exception e){
			log.info("Inside getProductPerformance : " + e.getMessage());
		}
		return queryResponse;
	}

	@Override
	public QueryResponse getCampaignDetailByPartner(String OrderID, List<Long> lineItemIDs, QueryDTO queryDTO) {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		
		String lineItem = "";
		
		for (Long val : lineItemIDs) {
			if(lineItem == "")
				lineItem = "'"+val+"'";
			else
				lineItem += " , '"+val+"'";
		}
	
		query.append("SELECT Date , Date(Date) as oDate, sum(Total_Impressions), sum (Total_clicks) FROM  ")
		.append(queryDTO.getQueryData())
		.append(" where Order_ID = '" + OrderID + "' ")
		.append(" and Line_item_ID in (" + lineItem + ")")
		.append(" group by Date,oDate order by Date ");
		
		System.out.println(query);
		
		queryDTO.setQueryData(query.toString());
		try{
			 queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
		}catch(Exception e){
			log.info("Inside getProductPerformance : " + e.getMessage());
		}
		
		return queryResponse;
	}


}