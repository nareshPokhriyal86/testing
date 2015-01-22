package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IAlertEngineDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.MemcacheUtil;

public class AlertEngineDAO implements IAlertEngineDAO {
	
	private static final Logger log = Logger.getLogger(AlertEngineDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	@Override
	public void saveObject(Object obj) throws DataServiceException{	
    	obfy.save().entity(obj).now();
    	obfy.clear();
	}
	
	@Override
	public List<PerformanceMonitoringDTO> loadAllCampaignData(QueryDTO queryDTO, String orderIds){
		log.info("loadAllCampaignsData....");
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
			.append(" Where Order_ID IN("+orderIds+") ")
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
	public List<PerformanceMonitoringDTO> loadAllLineItemsForOrderId(String orderId,QueryDTO queryDTO, String alertDate){
		QueryResponse queryResponse = null;
		PerformanceMonitoringDTO dtoObj = new PerformanceMonitoringDTO();
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<PerformanceMonitoringDTO>();
		StringBuilder query = new StringBuilder();
		try{
			query.append("Select line_item_id,substr(Line_Item,27,Length(Line_Item)) as Line_Item,sum(ImpressionsDelivered) as Impression_Delivered,  Sum(Clicks) as Clicks,")
			.append(" ifnull(round((Sum(Clicks)/sum(ImpressionsDelivered))*100,4),0.0) as CTR,Date( Min(Line_item_Start_Date)) as Line_item_Start_Date,	 ")
			.append("Date(max(Line_item_End_date)) as Line_item_End_date	from ( SELECT  MAX(concat(FORMAT_UTC_USEC(Date), Line_Item)) as Line_Item,  Min( Line_item_start_time ) ")
			.append(" as Line_item_Start_Date, max( Line_Item_end_date ) as Line_item_End_date, line_item_id, Sum(total_impressions) as ImpressionsDelivered,  Sum(total_clicks) as Clicks,")
			.append(" from "+queryDTO.getQueryData())
			.append(" Where Order_id='"+orderId+"'");
			if(alertDate != null)
			{
				query.append(" and date<='"+alertDate+" 00:00:00'");
			}
			
			query.append(" group each by line_item_id ignore case )  Group By line_item_id,line_item ignore case");
			/*.append(" Group By Order,Order_Id,Advertiser ignore case "); */
			log.info("loadAllLineItemsForOrderId AlertEngineDAO :: Query  :"+query);

			
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
			    	  
			    	  dtoObj= new PerformanceMonitoringDTO(
			    				 (cellList.get(0).getV().toString()),
			    				 (cellList.get(1).getV().toString()),
			    				 (cellList.get(2).getV().toString()),
			    				 (cellList.get(3).getV().toString()), 
			    				 (cellList.get(4).getV().toString()), 
			    				 (cellList.get(5).getV().toString()), 
			    				 (cellList.get(6).getV().toString())
			    				);
			    	  
			    	  campaignList.add(dtoObj);
				 }
			    	  
	 }	
		}catch(Exception e){
			log.info("Exception in loadAllLineItemsForOrderId of AlertEngineDAO : "+e.getMessage());
			
		}

		return campaignList;
		
	}
	
	@Override
	public List<PerformanceMonitoringDTO> loadAllLineItemsForFlights(String lineItemId,String startDate,String endDate,QueryDTO queryDTO){
		QueryResponse queryResponse = null;
		PerformanceMonitoringDTO dtoObj = new PerformanceMonitoringDTO();
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<PerformanceMonitoringDTO>();
		StringBuilder query = new StringBuilder();
		try{
			query.append("Select line_item_id,substr(Line_Item,27,Length(Line_Item)) as Line_Item,ImpressionsDelivered as Impression_Delivered,  Clicks as Clicks,")
			.append("ifnull(round(Clicks/ImpressionsDelivered*100,4),0.0) as CTR,Date( Line_item_Start_Date) as Line_item_Start_Date,	Date(Line_item_End_date) as Line_item_End_date	 ")
			.append("from ( SELECT   MAX(concat(FORMAT_UTC_USEC(Date), Line_Item)) as Line_Item,   Min( Line_item_start_time ) as Line_item_Start_Date, ")
			.append("max( Line_Item_end_date ) as Line_item_End_date, line_item_id, Sum(total_impressions) as ImpressionsDelivered,  Sum(total_clicks) as Clicks,")
			.append(" from "+queryDTO.getQueryData())
			.append(" Where line_item_id='"+lineItemId+"' and date>='"+startDate+" 00:00:00' and date<='"+endDate+" 00:00:00'")
			.append("group each by line_item_id ignore case )ignore case");
			/*.append(" Group By Order,Order_Id,Advertiser ignore case "); */
			log.info("loadAllLineItemsForFlights AlertEngineDAO :: Query  :"+query);
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
			    	  
			    	  dtoObj= new PerformanceMonitoringDTO(
			    				 (cellList.get(0).getV().toString()),
			    				 (cellList.get(1).getV().toString()),
			    				 (cellList.get(2).getV().toString()),
			    				 (cellList.get(3).getV().toString()), 
			    				 (cellList.get(4).getV().toString()), 
			    				 (cellList.get(5).getV().toString()), 
			    				 (cellList.get(6).getV().toString())
			    				);
			    	  
			    	  campaignList.add(dtoObj);
				 }
			    	  
	 }	
		}catch(Exception e){
			log.info("Exception in loadAllLineItemsForFlights of AlertEngineDAO : "+e.getMessage());
			
		}

		return campaignList;
		
	}
	
	@Override
	public  List<SmartCampaignObj>  getRunningCampaignList(String companyId) throws Exception{
		 log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		 obfy.clear();
		 List<SmartCampaignObj> campaignList = new ArrayList<>();
		 campaignList = obfy.load().type(SmartCampaignObj.class)
				 						.filter("companyId = ",companyId)
					 					.list();
		 return campaignList;
	}
	
	@Override
	public  List<AlertEngineObj>  getAllCampaigns() throws Exception{
		 log.info("Inside getSmartCampaignList of getAllCampaigns");
		 obfy.clear();
		 List<AlertEngineObj> alertEngineObjList = new ArrayList<AlertEngineObj>();
		 alertEngineObjList = obfy.load().type(AlertEngineObj.class)
					 					.list();
		 return alertEngineObjList;
	}
	
	@Override
	public  List<AlertEngineObj>  getAllCampaigns(Date date) throws Exception{
		log.info("Inside getSmartCampaignList of getAllCampaigns");
		log.info("date : "+date);
		 
		 obfy.clear();
		 List<AlertEngineObj> alertEngineObjList = new ArrayList<AlertEngineObj>();
		 alertEngineObjList = obfy.load().type(AlertEngineObj.class).filter("alertDate = ", date)
					 					.list();
		 log.info("alertEngineObjList : "+alertEngineObjList);
		 return alertEngineObjList;
	}
	
	@Override
	public List<AlertEngineObj> getAllCampaignAlert(String companyId) throws Exception{
		log.info("Inside getAllCampaignAlert of AlertEngineDAO");
		List<AlertEngineObj> alertList = new ArrayList<AlertEngineObj>();
		alertList = obfy.load().type(AlertEngineObj.class)
					.filter("companyId = ",companyId)
								//.filter("campaignId IN ", campaignIdList)
								.order("- alertDate").order("campaignName")
								.list();
		return alertList;
	}
	
	@Override
	public List<AlertEngineObj> getAllCampaignAlertSuperUser() throws Exception{
		log.info("Inside getAllCampaignAlertSuperUser of AlertEngineDAO");
		List<AlertEngineObj> alertList = new ArrayList<AlertEngineObj>();
		alertList = obfy.load().type(AlertEngineObj.class)
				//.filter("companyId = ",companyId)
								//.filter("campaignId IN ", campaignIdList)
								.order("- alertDate").order("campaignName")
								.list();
		return alertList;
	}

}
