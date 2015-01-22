package com.lin.web.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.api.client.util.Lists;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.NumberValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.lin.persistance.dao.INewAdvertiserViewDAO;
import com.lin.persistance.dao.impl.NewAdvertiserViewDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.web.action.AdvertiserViewAction;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.dto.ChannelPerformancePopUpDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineChartDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.NonPerformerReportDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByLocationReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformanceBySiteReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SummaryReportDTO;
import com.lin.web.service.INewAdvertiserViewService;
import com.lin.web.service.IQueryService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.GoogleVisulizationUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;


public class NewAdvertiserViewService implements INewAdvertiserViewService {

	private static final Logger log = Logger.getLogger(AdvertiserViewAction.class.getName());
	
	public QueryDTO getQueryDTOForCampaignPerformance(String publisherIdInBQ, String schema) {
		log.info("schema.."+schema);
		IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
		String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		
		QueryDTO queryDTO = queryService.createQueryFromClause(publisherIdInBQ, "2013-01-01", currentDate, schema);
		if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
			log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
		}
		/*else {
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
			else if(schema.equals(LinMobileConstants.BQ_RICH_MEDIA)) {
				queryData.append("LIN_QA.RichMedia");
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
				  if(schema.equals(LinMobileConstants.BQ_RICH_MEDIA)) {
					  projectId=LinMobileConstants.GOOGLE_API_PROJECT_ID;
					  serviceAccountEmail=LinMobileConstants.GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
					  servicePrivateKey=LinMobileConstants.GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				  }
				  else {
					  projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
					  serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
					  servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
				  }
				  break;
			  default:
				  log.warning("There is no project configured for this publisherId :"+publisherIdInBQ);
				  return queryDTO;			  
			}	
			
			queryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, projectId,dataSetId, queryData.toString());
			log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
			//
		}*/
		return queryDTO;
	}
	
	public List<NewAdvertiserViewDTO> headerDate(String orderId,String campaignName,String accountName, String publisherId){
		log.info("In headerData() of NewAdvertiserViewService");
		List<NewAdvertiserViewDTO> headerDataList = new ArrayList<NewAdvertiserViewDTO>();
 		try{
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			headerDataList = MemcacheUtil.getAdvertiserHeaderDataList(orderId);
			if(headerDataList==null || headerDataList.size()<=0){
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					headerDataList = dao.headerData(orderId,campaignName, accountName, queryDTO);
					MemcacheUtil.setAdvertiserHeaderDataList(headerDataList, orderId);
				}
			}
			
		}catch(Exception e){
			log.severe("Exception in headerData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return headerDataList;
	}
	
	public String performarData(String orderId, String publisherId){
		log.info("In performarData() of NewAdvertiserViewService");
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		List<PerformerReportDTO> performerDataList = new ArrayList<>();
		List<PerformerReportDTO> performerDataModifiedList = new ArrayList<>();
		performerDataList = MemcacheUtil.getPerformerAdvertiserObj(orderId);
		if(performerDataList==null || performerDataList.size()>=0){
			QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				performerDataList = dao.performarData(orderId, queryDTO);
				MemcacheUtil.setPerformerAdvertiserObj(orderId, performerDataList);
			}
		}
		for (PerformerReportDTO performerReportDTO : performerDataList) {
			if(performerReportDTO!=null){
				PerformerReportDTO dto = new PerformerReportDTO();
				if(performerReportDTO.getS1Site()!=null){
					dto.setS1Site(performerReportDTO.getS1Site());
				}
				if(performerReportDTO.getS3Clicks()!=null){
					dto.setS3Clicks(performerReportDTO.getS3Clicks());
				}
				if(performerReportDTO.getS2ImpressionDelivered()!=null){
					dto.setS2ImpressionDelivered(performerReportDTO.getS2ImpressionDelivered());
				}
				if(performerReportDTO.getS4CTR()!=null){
					dto.setS4CTR(performerReportDTO.getS4CTR());
				}
				performerDataModifiedList.add(dto);
			}
		}
		String json = "";
		try {
			Gson gson = new Gson();
			json = gson.toJson(performerDataModifiedList); // convert java object to JSON format,
			// and returned as JSON formatted string
		} catch (Exception e) {
			log.severe("Exception in performarData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return json;
	}
	
	public String mostActiveData(String orderId, String publisherId){
		log.info("In mostActiveData() of NewAdvertiserViewService");
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		List<MostActiveReportDTO> mostActiveDataList = new ArrayList<>();
		List<MostActiveReportDTO> mostActiveDataModifiedList = new ArrayList<>();
		mostActiveDataList = MemcacheUtil.getMostActiveAdvertiserObj(orderId);
		if(mostActiveDataList==null || mostActiveDataList.size()<=0) {
			QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				mostActiveDataList = dao.mostActiveData(orderId, queryDTO);
				MemcacheUtil.setMostActiveAdvertiserObj(orderId, mostActiveDataList);
			}
		}
		for (MostActiveReportDTO mostActiveReportDTO : mostActiveDataList) {
			if(mostActiveReportDTO!=null){
				MostActiveReportDTO dto = new MostActiveReportDTO();
				if(mostActiveReportDTO.getC1lineItem()!=null){
					dto.setC1lineItem(mostActiveReportDTO.getC1lineItem());
				}
			    if(mostActiveReportDTO.getC2impressionBooked()!=null){
			    	dto.setC2impressionBooked(mostActiveReportDTO.getC2impressionBooked());
			    }
			    if(mostActiveReportDTO.getC3impressionDelivered()!=null){
			    	dto.setC3impressionDelivered(mostActiveReportDTO.getC3impressionDelivered());
			    }
			    if(mostActiveReportDTO.getC4pacing()!=null){
			    	dto.setC4pacing(mostActiveReportDTO.getC4pacing());
			    }
			    mostActiveDataModifiedList.add(dto);
			}
		}
		String json = "";
		try {
			Gson gson = new Gson();
			json = gson.toJson(mostActiveDataModifiedList); // convert java object to JSON format,
			// and returned as JSON formatted string
		} catch (Exception e) {
			log.severe("Exception in mostActiveData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return json;
	}
	
	public String deliveryMetricsData(String orderId, String publisherId){
		log.info("In deliveryMetrics() of NewAdvertiserViewService");
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		List<NewAdvertiserViewDTO> deliveryMetricsList = new ArrayList<NewAdvertiserViewDTO>();
		deliveryMetricsList = MemcacheUtil.getdeliveryMetricsData(orderId);
		if(deliveryMetricsList==null || deliveryMetricsList.size()<=0) {
			QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			   deliveryMetricsList = dao.deliveryMetricsData(orderId, queryDTO);
			   MemcacheUtil.setdeliveryMetricsDataList(deliveryMetricsList, orderId);
			}
		}
		String json = "";
		try {
			Gson gson = new Gson();
			json = gson.toJson(deliveryMetricsList); // convert java object to JSON format,
			
		} catch (Exception e) {
			log.severe("Exception in deliveryMetrics() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return json;
	}
	
	public DataTable performanceByLocationChartData(String orderId, boolean isNoise, double threshold, String publisherId){
		log.info("In performanceByLocationChartData() of NewAdvertiserViewService");
		QueryResponse queryResponse = null;
		List<String> list = new ArrayList<String>();
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		DataTable datatable = new DataTable();
		//DataTable datatable1 = new DataTable();
		QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION);
		if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			queryResponse = dao.performanceByLocationChartData(orderId, false, isNoise, threshold,queryDTO);
		}
		try {
			datatable = GoogleVisulizationUtil.buildColumns(queryResponse,list);
			if(queryResponse !=null && queryResponse.getRows()!=null){
				datatable = GoogleVisulizationUtil.buildRows(datatable, queryResponse);
			}else{
				log.warning("No data found for this campaign -"+orderId);
			}	
		} catch (SQLException e) {
			log.severe("Exception in performanceByLocationChartData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return datatable;
	}
	
	/*
	 * This method will return all orders for an advertiser or agency
	 * 
	 * @author Youdhveer Panwar 
	 * @param String advertiserId,String agencyId 
	 * @return Map<String,String>
	 */
	public Map<String,String> loadOrdersForAdvertiserOrAgency(String advertiserId,String agencyId, String publisherIdInBQ,long userId){
		Map<String,String> resultMap=new LinkedHashMap<String,String>();
		
		try {
			String memcacheKey=advertiserId+"_"+agencyId;
			if(userId>0){
				memcacheKey=memcacheKey+"_"+userId;
			}
			
			resultMap=MemcacheUtil.getDataMapFromCacheByKey(memcacheKey);
			if(resultMap == null || resultMap.size()==0){
				INewAdvertiserViewDAO newAdvertiserDAO=new NewAdvertiserViewDAO();
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					resultMap=newAdvertiserDAO.loadOrdersForAdvertiserOrAgency(advertiserId, agencyId, queryDTO);
					MemcacheUtil.setDataMapInCacheByKey(resultMap, memcacheKey);
				}
			}
			
		} catch (Exception e) {
			log.severe("Exception :"+e.getMessage());
			
		}
		return resultMap;
	}
	
	public String performanceByAdSizeChartData(String orderId, boolean isNoise, double threshold, String publisherId){		
		log.info("In performanceByAdSizeChartData() of NewAdvertiserViewService");
		List<PerformanceByAdSizeReportDTO>  performanceByAdSizeChartDataList = new ArrayList<>();
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		performanceByAdSizeChartDataList = MemcacheUtil.getPerformanceByAdSize(orderId, isNoise, threshold);
		if(performanceByAdSizeChartDataList==null || performanceByAdSizeChartDataList.size()<=0){
			QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				performanceByAdSizeChartDataList = dao.performanceByAdSizeChartData(orderId, isNoise, threshold, queryDTO);
				MemcacheUtil.setPerformanceByAdSize(orderId, isNoise, threshold, performanceByAdSizeChartDataList);
			}
		}
		String json = "";
		try {
			Gson gson = new Gson();
			json = gson.toJson(performanceByAdSizeChartDataList);
			
		} catch (Exception e) {
			log.severe("Exception in performanceByAdSizeChartData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return json;	
	}
	
	public DataTable impressionByOSChartData(String orderId, String publisherId){
		log.info("In impressionByOSChartData() of NewAdvertiserViewService");
		QueryResponse queryResponse = null;
		List<String> list = new ArrayList<String>();
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		DataTable datatable = new DataTable();
		QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
		if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			queryResponse = dao.impressionByOSChartData(orderId, queryDTO);
		}
		try {
			datatable = GoogleVisulizationUtil.buildColumns(queryResponse,list);
			if(queryResponse !=null && queryResponse.getRows()!=null){
				datatable = GoogleVisulizationUtil.buildRows(datatable, queryResponse);
			}else{
				log.warning("No data found for this campaign -"+orderId);
			}			
		} catch (SQLException e) {
			log.severe("Exception in impressionByOSChartData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return datatable;
	}
	
	public DataTable impressionByDeviceChartData(String orderId, String publisherId){
		log.info("In impressionByDeviceChartData() of NewAdvertiserViewService");
		QueryResponse queryResponse = null;
		List<String> list = new ArrayList<String>();
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		DataTable datatable = new DataTable();
		QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
		if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			queryResponse = dao.impressionByDeviceChartData(orderId, queryDTO);
		}
		try {
			datatable = GoogleVisulizationUtil.buildColumns(queryResponse,list);
			if(queryResponse !=null && queryResponse.getRows()!=null){
				datatable = GoogleVisulizationUtil.buildRows(datatable, queryResponse);
			}else{
				log.warning("No data found for this campaign -"+orderId);
			}	
		} catch (SQLException e) {
			log.severe("Exception in impressionByDeviceChartData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return datatable;
	}
	
	public DataTable impressionBymobileWebVsAppChartData(String orderId, String publisherId){
		log.info("In impressionBymobileWebVsAppChartData() of NewAdvertiserViewService");
		QueryResponse queryResponse = null;
		List<String> list = new ArrayList<String>();
		INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
		DataTable datatable = new DataTable();
		QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
		if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			queryResponse = dao.impressionBymobileWebVsAppChartData(orderId, queryDTO);
		}
		try {
			datatable = GoogleVisulizationUtil.buildColumns(queryResponse,list);
			if(queryResponse !=null && queryResponse.getRows()!=null){
				datatable = GoogleVisulizationUtil.buildRows(datatable, queryResponse);
			}else{
				log.warning("No data found for this campaign -"+orderId);
			}	
		} catch (SQLException e) {
			log.severe("Exception in impressionBymobileWebVsAppChartData() of NewAdvertiserViewService : "+e.getMessage());
			
		}
		return datatable;
	}
	
	/*
	 * This method will process line chart data for an order in json format
	 * 
	 * @author Youdhveer Panwar 
	 * @param String orderId
	 * @return Map<String,String> jsonChartMap
	 */
	
	public Map<String,String> processLineChartData(String orderId, boolean isNoise, double threshold, String publisherId){		
		Map<String,String> jsonChartMap=MemcacheUtil.getLineChartMapFromCache(orderId, isNoise, threshold);
		if(jsonChartMap ==null || jsonChartMap.size()==0){
			jsonChartMap=new HashMap<String,String>();
			List<LineChartDTO> resultList=new ArrayList<LineChartDTO>();
			INewAdvertiserViewDAO newAdvertiserDAO=new NewAdvertiserViewDAO();
			try {
				
				QueryResponse queryResponse = null;
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					queryResponse = newAdvertiserDAO.loadLineChartData(orderId, isNoise, threshold, queryDTO);
				}
				Map<String, Object> dateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> siteMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfDataMap = new LinkedHashMap<String, Object>();
				
				if (queryResponse != null && queryResponse.getRows() != null) {
					List<com.google.api.services.bigquery.model.TableRow> rowList = queryResponse.getRows();            
					for (com.google.api.services.bigquery.model.TableRow row : rowList) {
						List<TableCell> cellList = row.getF();
						String date=cellList.get(0).getV().toString();
						String siteName=cellList.get(1).getV().toString();
						String impressions=cellList.get(2).getV().toString();
						String clicks=cellList.get(3).getV().toString();
						String ctr=cellList.get(4).getV().toString();
						LineChartDTO lineChartDTO=new LineChartDTO(siteName, impressions, clicks, ctr, date);
						resultList.add(lineChartDTO);
						dateMap.put(date, null);
						siteMap.put(siteName, null);
						String key=date+"_"+siteName;
						perfDataMap.put(key, lineChartDTO);
				    }
					String ctrLineChartJson=createJsonResponseForLineChart(dateMap, siteMap, perfDataMap, "CTR");
					jsonChartMap.put("CTR", ctrLineChartJson);
					String clicksLineChartJson=createJsonResponseForLineChart(dateMap, siteMap, perfDataMap, "Clicks");
					jsonChartMap.put("Clicks", clicksLineChartJson);
					String impressionsLineChartJson=createJsonResponseForLineChart(dateMap, siteMap, perfDataMap, "Impressions");
					jsonChartMap.put("Impressions", impressionsLineChartJson);
					MemcacheUtil.setLineChartMapInCache(jsonChartMap, orderId, isNoise, threshold);
					
				}else{
					log.warning("Invalid query or no data found...");
				}		
						 
			} catch (Exception e) {
				log.severe("Exception :"+e.getMessage());
				
			}
		}else{
			log.info("Found line chart data in memcache..size:"+jsonChartMap.size());
		}		
		return jsonChartMap;
	}
	
	/*
	 * This method will create json string for line chart
	 */
	private String createJsonResponseForLineChart(Map<String, Object> dateMap,
			Map<String, Object> siteMap,Map<String, Object> perfDataMap,String chartType) throws TypeMismatchException{
		
		DataTable linechartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription("date", ValueType.DATE, "Date");
	    linechartTable.addColumn(col0);

	    for (Entry<String, Object> sites : siteMap.entrySet()) {
			  String site = sites.getKey();
			  ColumnDescription sitecol = new ColumnDescription(site, ValueType.NUMBER, site);
			  linechartTable.addColumn(sitecol);
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
			  
			  for (Entry<String, Object> sites : siteMap.entrySet()) {
				  String site = sites.getKey();
				  String key=date+"_"+site;					 
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
					  }else if(chartType.equalsIgnoreCase("averageInteractionRate")){
						  Double averageInteractionRate=StringUtil.getDoubleValue(perData.getAverage_Interaction_Rate(), 4);
						  row.addCell(new NumberValue(new Double(averageInteractionRate)));
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
	
	private String createJsonForVideoCampaignLineChart(Map<String, Object> dateMap, Map<String, Object> columnMap,
			Map<String, Object> dataMap) throws TypeMismatchException{
		
		DataTable linechartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription("date", ValueType.DATE, "Date");
	    linechartTable.addColumn(col0);

	    for (Entry<String, Object> columns : columnMap.entrySet()) {
			  String column = columns.getKey();
			  ColumnDescription cols = new ColumnDescription(column, ValueType.NUMBER, column);
			  linechartTable.addColumn(cols);
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
			  
			  for (String chartType : columnMap.keySet()) {
				  String key=date;				 
				  LineChartDTO perData = (LineChartDTO) dataMap.get(key);					 
				  if ( perData  != null){
					  if(chartType.equalsIgnoreCase("Average Interaction Rate")){
						  Double val=StringUtil.getDoubleValue(perData.getAverage_Interaction_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("Average View Rate")){
						  Double val=StringUtil.getDoubleValue(perData.getAverage_View_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("Completion Rate")){
						  Double val=StringUtil.getDoubleValue(perData.getCompletion_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("Start")){
						  row.addCell(new NumberValue(new Long(perData.getStart()))); 
					  }else if(chartType.equalsIgnoreCase("First Quartile")){
						  row.addCell(new NumberValue(new Long(perData.getFirstQuartile()))); 
					  }else if(chartType.equalsIgnoreCase("Midpoint")){
						  row.addCell(new NumberValue(new Long(perData.getMidpoint()))); 
					  }else if(chartType.equalsIgnoreCase("Third Quartile")){
						  row.addCell(new NumberValue(new Long(perData.getThird_Quartile()))); 
					  }else if(chartType.equalsIgnoreCase("Complete")){
						  row.addCell(new NumberValue(new Long(perData.getComplete()))); 
					  }else if(chartType.equalsIgnoreCase("Pause")){
						  row.addCell(new NumberValue(new Long(perData.getPause()))); 
					  }else if(chartType.equalsIgnoreCase("Resume")){
						  row.addCell(new NumberValue(new Long(perData.getResume()))); 
					  }else if(chartType.equalsIgnoreCase("Rewind")){
						  row.addCell(new NumberValue(new Long(perData.getRewind()))); 
					  }else if(chartType.equalsIgnoreCase("Mute")){
						  row.addCell(new NumberValue(new Long(perData.getMute()))); 
					  }else if(chartType.equalsIgnoreCase("Unmute")){
						  row.addCell(new NumberValue(new Long(perData.getUnmute()))); 
					  }else if(chartType.equalsIgnoreCase("FullScreen")){
						  row.addCell(new NumberValue(new Long(perData.getFullScreen()))); 
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
	
	/*private String createJsonForVideoCampaignLineChart(Map<String, Object> dateMap,
			Map<String, Object> dataMap,String chartType) throws TypeMismatchException{
		
		DataTable linechartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription("date", ValueType.DATE, "Date");
	    linechartTable.addColumn(col0);

	    ColumnDescription col1 = new ColumnDescription("", ValueType.NUMBER, "");
		linechartTable.addColumn(col1);
		  
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
			  
				  String key=date;				 
				  LineChartDTO perData = (LineChartDTO) dataMap.get(key);					 
				  if ( perData  != null){
					  if(chartType.equalsIgnoreCase("averageInteractionRate")){
						  Double val=StringUtil.getDoubleValue(perData.getAverage_Interaction_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("averageViewRate")){
						  Double val=StringUtil.getDoubleValue(perData.getAverage_View_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("completionRate")){
						  Double val=StringUtil.getDoubleValue(perData.getCompletion_Rate(), 4);
						  row.addCell(new NumberValue(new Double(val)));
					  }else if(chartType.equalsIgnoreCase("start")){
						  row.addCell(new NumberValue(new Long(perData.getStart()))); 
					  }else if(chartType.equalsIgnoreCase("firstQuartile")){
						  row.addCell(new NumberValue(new Long(perData.getFirstQuartile()))); 
					  }else if(chartType.equalsIgnoreCase("midpoint")){
						  row.addCell(new NumberValue(new Long(perData.getMidpoint()))); 
					  }else if(chartType.equalsIgnoreCase("thirdQuartile")){
						  row.addCell(new NumberValue(new Long(perData.getThird_Quartile()))); 
					  }else if(chartType.equalsIgnoreCase("complete")){
						  row.addCell(new NumberValue(new Long(perData.getComplete()))); 
					  }else if(chartType.equalsIgnoreCase("pause")){
						  row.addCell(new NumberValue(new Long(perData.getPause()))); 
					  }else if(chartType.equalsIgnoreCase("resume")){
						  row.addCell(new NumberValue(new Long(perData.getResume()))); 
					  }else if(chartType.equalsIgnoreCase("rewind")){
						  row.addCell(new NumberValue(new Long(perData.getRewind()))); 
					  }else if(chartType.equalsIgnoreCase("mute")){
						  row.addCell(new NumberValue(new Long(perData.getMute()))); 
					  }else if(chartType.equalsIgnoreCase("unmute")){
						  row.addCell(new NumberValue(new Long(perData.getUnmute()))); 
					  }else if(chartType.equalsIgnoreCase("fullScreen")){
						  row.addCell(new NumberValue(new Long(perData.getFullScreen()))); 
					  }
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  }
			  rows.add(row);				  
	  }
	  linechartTable.addRows(rows);
	  
	  java.lang.CharSequence jsonStr =	 JsonRenderer.renderDataTable(linechartTable, true, false);
	  return jsonStr.toString();
	}*/

	@Override
	public String richMediaEventGraph(List<CommonDTO> customEvents, String campaignId, String publisherId) throws Exception {
		List<String> tempCustomEventsList = new ArrayList<String>();
		  List<AdvertiserPerformerDTO> richMediaEventGraphList = null; 
		  List<CommonDTO> customEventsList = new ArrayList<CommonDTO>();
		  String customEventString = "";
		  String customizedEventValue = "";
		  String richMediaGraphTable = "";
		  INewAdvertiserViewDAO newAdvertiserDAO=new NewAdvertiserViewDAO();
			
		  richMediaEventGraphList = MemcacheUtil.getRichMediaEventGraphList(campaignId, publisherId);
		  if(richMediaEventGraphList == null || richMediaEventGraphList.size() == 0) {
			  QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CUSTOM_EVENT);
			  if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				  richMediaEventGraphList =  newAdvertiserDAO.richMediaEventGraph(campaignId, queryDTO);
				  if(richMediaEventGraphList != null && richMediaEventGraphList.size()> 0) {
					  MemcacheUtil.setRichMediaEventGraphList(richMediaEventGraphList, campaignId, publisherId);
				  }
				}
		  }

		  if(richMediaEventGraphList!=null && richMediaEventGraphList.size()>0){			  
			  for (AdvertiserPerformerDTO advertiserPerformerDTO : richMediaEventGraphList) {
				  if(advertiserPerformerDTO != null && advertiserPerformerDTO.getCustomEvent()!=null && !advertiserPerformerDTO.getCustomEvent().trim().equals("") && !advertiserPerformerDTO.getCustomEvent().trim().equalsIgnoreCase("null") && !tempCustomEventsList.contains(advertiserPerformerDTO.getCustomEvent().trim())) {
					  customizedEventValue = advertiserPerformerDTO.getCustomEvent().trim();
					  tempCustomEventsList.add(customizedEventValue);
					  if(customizedEventValue.startsWith("http")) {
		    			  customizedEventValue = "URL Clicked";
		    		  }
		    		  else if(customizedEventValue.trim().length() > 22) {
		    			  customizedEventValue = customizedEventValue.trim().substring(0, 20) +"..";
		    		  }
					  customEventString = customEventString + ",'"+customizedEventValue+"'";
					  customEventsList.add(new CommonDTO("richMediaEventGraph_"+customizedEventValue.replaceAll(" ", ""), customizedEventValue));
				  }
			  }
			  customEvents.addAll(customEventsList);
			  
			  richMediaGraphTable = richMediaGraphTable +  "['Market','Site','Campaign Category','Label','Count'"+customEventString+"],";
			  System.out.println(richMediaGraphTable);
			 for (AdvertiserPerformerDTO dto : richMediaEventGraphList) {
				 if(dto!=null){
					 customizedEventValue = dto.getCustomEvent().trim();
					  tempCustomEventsList.add(customizedEventValue);
					  if(customizedEventValue.startsWith("http")) {
		    			  customizedEventValue = "URL Clicked";
		    		  }
		    		  else if(customizedEventValue.trim().length() > 22) {
		    			  customizedEventValue = customizedEventValue.trim().substring(0, 20) +"..";
		    		  }
					  richMediaGraphTable = richMediaGraphTable + "[";
					  if(dto.getMarket()!=null && (dto.getMarket().trim().equals("DC") || dto.getMarket().trim().equals("SF"))){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getMarket().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getSiteName()!=null && !dto.getSiteName().trim().equals("") && !dto.getSiteName().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getSiteName().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getCampaignCategory()!=null && !dto.getCampaignCategory().trim().equals("") && !dto.getCampaignCategory().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+dto.getCampaignCategory().trim()+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(customizedEventValue!=null && !customizedEventValue.equals("") && !customizedEventValue.equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + "'"+customizedEventValue+"' , ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "'NA' , ";
					  }
					  if(dto.getValue()!=null && !dto.getValue().trim().equals("") && !dto.getValue().trim().equalsIgnoreCase("null")){
						  richMediaGraphTable = richMediaGraphTable + ""+dto.getValue().trim()+" ";
					  }else{
						  richMediaGraphTable = richMediaGraphTable + "0  ";
					  }
					 
				     richMediaGraphTable = richMediaGraphTable + customEventString;
					 
					  richMediaGraphTable = richMediaGraphTable + "],";
				  }
			}
			 log.info("Rich Media Graph Table :"+richMediaGraphTable);
		  }
		 
		  return richMediaGraphTable;
	}
	
	 
	 
	 public String performanceByOSBarChartData(String orderId, boolean isNoise, double threshold, String publisherId){
			log.info("In performanceByOSChartData() of NewAdvertiserViewService");
			List<PerformanceByOSReportDTO>  performanceByOSeChartDataList = new ArrayList<>();
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			performanceByOSeChartDataList = MemcacheUtil.getPerformanceByOS(orderId, isNoise, threshold);
			if(performanceByOSeChartDataList==null || performanceByOSeChartDataList.size()<=0){
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByOSeChartDataList = dao.performanceByOSBarChartData(orderId, isNoise, threshold, queryDTO);
					MemcacheUtil.setPerformanceByOS(orderId, isNoise, threshold, performanceByOSeChartDataList);
				}
			}
			
			String json = "";
			try {
				Gson gson = new Gson();
				json = gson.toJson(performanceByOSeChartDataList);
				
			} catch (Exception e) {
				log.severe("Exception in performanceByAdSizeChartData() of NewAdvertiserViewService : "+e.getMessage());
				
			}
			return json;	
		}
		
		public String performanceByDeviceBarChartData(String orderId, boolean isNoise, double threshold, String publisherId){
			log.info("In performanceByDeviceChartData() of NewAdvertiserViewService");
			List<PerformanceByDeviceReportDTO>  performanceByDeviceChartDataList = new ArrayList<>();
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			performanceByDeviceChartDataList = MemcacheUtil.getPerformanceByDevice(orderId, isNoise, threshold);
			if(performanceByDeviceChartDataList==null || performanceByDeviceChartDataList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByDeviceChartDataList = dao.performanceByDeviceBarChartData(orderId, isNoise, threshold, queryDTO);
					MemcacheUtil.setPerformanceByDevice(orderId, isNoise, threshold, performanceByDeviceChartDataList);
				}
			}
			
			String json = "";
			try {
				Gson gson = new Gson();
				json = gson.toJson(performanceByDeviceChartDataList);
				
			} catch (Exception e) {
				log.severe("Exception in performanceByAdSizeChartData() of NewAdvertiserViewService : "+e.getMessage());
				
			}
			return json;	
		}
		
		public String mobileWebVsAppBarChartData(String orderId, boolean isNoise, double threshold, String publisherId){
			log.info("In mobileWebVsAppChartData() of NewAdvertiserViewService");
			QueryResponse queryResponse = null;
			List<PerformanceByPlacementReportDTO>  performanceByPlacementDataList = new ArrayList<>();
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			performanceByPlacementDataList = MemcacheUtil.getPerformanceByPlacement(orderId, isNoise, threshold);
			if(performanceByPlacementDataList==null || performanceByPlacementDataList.size()<=0){
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByPlacementDataList = dao.mobileWebVsAppBarChartData(orderId, isNoise, threshold, queryDTO);
					MemcacheUtil.setPerformanceByPlacement(orderId, isNoise, threshold, performanceByPlacementDataList);
				}
			}
			
			String json = "";
			try {
				Gson gson = new Gson();
				json = gson.toJson(performanceByPlacementDataList);
				
			} catch (Exception e) {
				log.severe("Exception in performanceByAdSizeChartData() of NewAdvertiserViewService : "+e.getMessage());
				
			}
			return json;	
		}
		
		public List<NewAdvertiserViewDTO> deliveryMetricsReportObject(String orderId, String publisherId){
			log.info("In deliveryMetricsReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<NewAdvertiserViewDTO> deliveryMetricsList = new ArrayList<NewAdvertiserViewDTO>();
			deliveryMetricsList = MemcacheUtil.getdeliveryMetricsData(orderId);
			if(deliveryMetricsList==null || deliveryMetricsList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					deliveryMetricsList = dao.deliveryMetricsData(orderId, queryDTO);
					MemcacheUtil.setdeliveryMetricsDataList(deliveryMetricsList, orderId);
				}
			}
			return deliveryMetricsList;
		}
		
		public List<SummaryReportDTO> summaryReportObject(String orderId,String campaignName,String accountName, String publisherId){
			log.info("In summaryReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<NewAdvertiserViewDTO> list = new ArrayList<NewAdvertiserViewDTO>();
			List<SummaryReportDTO> summaryReportList = new ArrayList<SummaryReportDTO>();
			list = MemcacheUtil.getAdvertiserHeaderDataList(orderId);
			if(list==null || list.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					list = dao.headerData(orderId, campaignName, accountName, queryDTO);
					MemcacheUtil.setAdvertiserHeaderDataList(list, orderId);
				}
			}
			for (NewAdvertiserViewDTO newAdvertiserViewDTO : list) {
				SummaryReportDTO dto = new SummaryReportDTO();
				if(newAdvertiserViewDTO.getAccountName()!=null && !newAdvertiserViewDTO.getAccountName().equals("")){
					dto.setAccountName(newAdvertiserViewDTO.getAccountName());
				}else{
					dto.setAccountName("");
				}
				if(newAdvertiserViewDTO.getOrder()!=null && !newAdvertiserViewDTO.getOrder().equals("")){
					dto.setOrder(newAdvertiserViewDTO.getOrder());
				}else{
					dto.setOrder("");	
				}
				if(newAdvertiserViewDTO.getD1startDateTime()!=null && !newAdvertiserViewDTO.getD1startDateTime().equals("")){
					dto.setStartDateTime(DateUtil.getDateYYYYMMDD(newAdvertiserViewDTO.getD1startDateTime()));
				}
				if(newAdvertiserViewDTO.getD2endDateTime()!=null && !newAdvertiserViewDTO.getD2endDateTime().equals("")){
					dto.setEndDateTime(DateUtil.getDateYYYYMMDD(newAdvertiserViewDTO.getD2endDateTime()));
				}
				if(newAdvertiserViewDTO.getBudget()!=null){
					dto.setBudget(newAdvertiserViewDTO.getBudget());
				}else{
					dto.setBudget(0.0);
				}
				if(newAdvertiserViewDTO.getSpent()!=null){
					dto.setSpent(newAdvertiserViewDTO.getSpent());
				}else{
					dto.setSpent(0.0);
				}
				if(newAdvertiserViewDTO.getImpressionDelivered()!=null){
					dto.setImpressionDelivered(newAdvertiserViewDTO.getImpressionDelivered());
				}else{
					dto.setImpressionDelivered(0L);
				}
				if(newAdvertiserViewDTO.getClicks()!=null){
					dto.setClicks(newAdvertiserViewDTO.getClicks());
				}else{
					dto.setClicks(0L);
				}
				if(newAdvertiserViewDTO.getCTR()!=null){
					dto.setCTR(newAdvertiserViewDTO.getCTR());
				}else{
					dto.setCTR(0.0);
				}
				if(newAdvertiserViewDTO.getBalance()!=null){
					dto.setBalance(newAdvertiserViewDTO.getBalance());
				}else{
					dto.setBalance(0.0);
				}
				dto.setBenchCtr(0.1/100);
				summaryReportList.add(dto);
				
			}
			return summaryReportList;
		}
		
		public List<PerformanceBySiteReportDTO> performanceBySiteReportObject(String orderId, String publisherId) throws DataServiceException, IOException{
			log.info("In performanceBySiteReportObject() of NewAdvertiserViewService");
			List<PerformanceBySiteReportDTO> performanceBySiteReportList = new ArrayList<PerformanceBySiteReportDTO>();
			INewAdvertiserViewDAO newAdvertiserDAO=new NewAdvertiserViewDAO();
			try{
				QueryResponse queryResponse = null;
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					queryResponse = newAdvertiserDAO.loadLineChartData(orderId, false, 0.00, queryDTO);
				}
				if (queryResponse!=null && queryResponse.getRows() != null) {
					 List<TableRow> rowList = queryResponse.getRows();
					 for (TableRow row : rowList){
						 PerformanceBySiteReportDTO dtoObj = new PerformanceBySiteReportDTO();
						 List<TableCell> cellList = row.getF();
						 dtoObj.setStartDateTime(DateUtil.getDateYYYYMMDD(cellList.get(0).getV().toString()));
						 dtoObj.setSite((cellList.get(1).getV().toString()));
						 dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
						 dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
						 dtoObj.setCTR(new Double(cellList.get(4).getV().toString())/100);
						 performanceBySiteReportList.add(dtoObj);
					 }
		       } 
			}catch(Exception e){
				log.severe("Exception in performanceBySiteReportObject()  in NewAdvertiserViewService : "+e.getMessage());
				 
			}
				
			return performanceBySiteReportList;
		}
	
		public DataTable impressionsByAdSizeChartData(String orderId, String publisherId){
			log.info("In impressionsByAdSizeChartData() of NewAdvertiserViewService");
			QueryResponse queryResponse = null;
			List<String> list = new ArrayList<String>();
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			DataTable datatable = new DataTable();
			QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				queryResponse = dao.impressionsByAdSizeChartData(orderId, queryDTO);
			}
			try {
				datatable = GoogleVisulizationUtil.buildColumns(queryResponse,list);
				if(queryResponse !=null && queryResponse.getRows()!=null){
					datatable = GoogleVisulizationUtil.buildRows(datatable, queryResponse);
				}else{
					log.warning("No data found for this campaign -"+orderId);
				}			
			} catch (SQLException e) {
				log.severe("Exception in impressionsByAdSizeChartData() of NewAdvertiserViewService : "+e.getMessage());
				
			}
			return datatable;
		}
		
		public List<PerformanceByOSReportDTO> performanceByOSReportObject(String orderId, String publisherId){
			log.info("In performanceByOSReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformanceByOSReportDTO> performanceByOSList = new ArrayList<>();
			performanceByOSList = MemcacheUtil.getPerformanceByOS(orderId, false, 0.00);
			if(performanceByOSList==null || performanceByOSList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByOSList = dao.performanceByOSBarChartData(orderId, false, 0.00, queryDTO);
					MemcacheUtil.setPerformanceByOS(orderId, false, 0.00 , performanceByOSList);
				}
			}
			return performanceByOSList;
		}
		
		public List<PerformanceByDeviceReportDTO> performanceByDeviceReportObject(String orderId, String publisherId){
			log.info("In performanceByDeviceReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformanceByDeviceReportDTO> performanceByDeviceList = new ArrayList<>();
			performanceByDeviceList = MemcacheUtil.getPerformanceByDevice(orderId, false, 0.00);
			if(performanceByDeviceList==null || performanceByDeviceList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_DFP_TARGET);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByDeviceList = dao.performanceByDeviceBarChartData(orderId, false, 0.00, queryDTO);
					MemcacheUtil.setPerformanceByDevice(orderId, false, 0.00 , performanceByDeviceList);
				}
			}
			return performanceByDeviceList;
		}
		
		public List<PerformanceByPlacementReportDTO> performanceByPlacementReportObject(String orderId, String publisherId){
			log.info("In performanceByPlacementReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformanceByPlacementReportDTO> performanceByPlacementList = new ArrayList<>();
			performanceByPlacementList = MemcacheUtil.getPerformanceByPlacement(orderId, false, 0.00);
			if(performanceByPlacementList==null || performanceByPlacementList.size()<=0){
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByPlacementList = dao.mobileWebVsAppBarChartData(orderId, false, 0.00, queryDTO);
					MemcacheUtil.setPerformanceByPlacement(orderId, false, 0.00 , performanceByPlacementList);
				}
			}
			return performanceByPlacementList;
		}
		
		public List<PerformanceByAdSizeReportDTO> performanceByAdSizeReportObject(String orderId, String publisherId){
			log.info("In performanceByAdSizeReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformanceByAdSizeReportDTO> performanceByAdSizeList = new ArrayList<>();
			performanceByAdSizeList = MemcacheUtil.getPerformanceByAdSize(orderId, false, 0.00);
			if(performanceByAdSizeList==null || performanceByAdSizeList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performanceByAdSizeList = dao.performanceByAdSizeChartData(orderId, false, 0.00, queryDTO);
					MemcacheUtil.setPerformanceByAdSize(orderId, false, 0.00 , performanceByAdSizeList);
				}
			}
			return performanceByAdSizeList;
		}
		
		public List<MostActiveReportDTO> mostActiveReportObject(String orderId, String publisherId){
			log.info("In mostActiveReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<MostActiveReportDTO> mostActiveReportDataList = new ArrayList<>();
			mostActiveReportDataList = MemcacheUtil.getMostActiveAdvertiserObj(orderId);
			if(mostActiveReportDataList==null || mostActiveReportDataList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					mostActiveReportDataList = dao.mostActiveData(orderId, queryDTO);
					MemcacheUtil.setMostActiveAdvertiserObj(orderId, mostActiveReportDataList);
				}
			}
			return mostActiveReportDataList;
		}
		
		public List<PerformanceByLocationReportDTO> performanceByLocationReportobject(String orderId, String publisherId){
			log.info("In performanceByLocationReportobject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformanceByLocationReportDTO> performanceByLocationList = new ArrayList<>();
			QueryResponse queryResponse = null;
			performanceByLocationList = MemcacheUtil.getPerformanceByLocationAdvertiserObj(orderId, false,true, 0.00);
			if(performanceByLocationList==null || performanceByLocationList.size()<=0){
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					queryResponse = dao.performanceByLocationChartData(orderId, true, false, 0.00, queryDTO);
				}
				if (queryResponse!=null && queryResponse.getRows() != null) {
					 List<TableRow> rowList = queryResponse.getRows();
					 for (TableRow row : rowList){
						 PerformanceByLocationReportDTO dtoObj = new PerformanceByLocationReportDTO();
				    	  List<TableCell> cellList = row.getF();
				    	  dtoObj.setRegion(cellList.get(0).getV().toString());
				    	  dtoObj.setCTR(new Double(cellList.get(1).getV().toString())/100);
				    	 // dtoObj.setImpressionDelivered(new Long(cellList.get(2).getV().toString()));
				    	  dtoObj.setImpressionDelivered((new Double( cellList.get(2).getV().toString())).longValue());
				    	 // dtoObj.setClicks(new Long(cellList.get(3).getV().toString()));
				    	  dtoObj.setClicks((new Double( cellList.get(3).getV().toString())).longValue());
				    	  performanceByLocationList.add(dtoObj);
				      }
					 MemcacheUtil.setPerformanceByLocationAdvertiserObj(orderId, false, true, 0.00, performanceByLocationList);
				 }
			}
			return performanceByLocationList;
		}
		
		public List<PerformerReportDTO> PerformerReportObject(String orderId, String publisherId){
			log.info("In PerformerReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformerReportDTO> performerDataList = new ArrayList<>();
			List<PerformerReportDTO> performerReportDataList = new ArrayList<>();
			performerDataList = MemcacheUtil.getPerformerAdvertiserObj(orderId);
			if(performerDataList==null || performerDataList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					performerDataList = dao.performarData(orderId, queryDTO);
					MemcacheUtil.setPerformerAdvertiserObj(orderId, performerDataList);
				}
			}
			for (PerformerReportDTO performerReportDTO : performerDataList) {
				if(performerReportDTO!=null){
					performerReportDTO.setBenchCtr(0.1);
					if(performerReportDTO.getCTRReport()>= performerReportDTO.getBenchCtr() ){
						performerReportDataList.add(performerReportDTO);
					}
				}
			}
			return performerReportDataList;
		}
		
		public List<NonPerformerReportDTO> NonPerformerReportObject(String orderId, String publisherId){
			log.info("In NonPerformerReportObject() of NewAdvertiserViewService");
			INewAdvertiserViewDAO dao = new NewAdvertiserViewDAO();
			List<PerformerReportDTO> nonPerformerDataList = new ArrayList<>();
			List<NonPerformerReportDTO> nonPerformerReportDataList = new ArrayList<>();
			nonPerformerDataList = MemcacheUtil.getPerformerAdvertiserObj(orderId);
			if(nonPerformerDataList==null || nonPerformerDataList.size()<=0) {
				QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					nonPerformerDataList = dao.performarData(orderId, queryDTO);
					MemcacheUtil.setPerformerAdvertiserObj(orderId, nonPerformerDataList);
				}
			}
			for (PerformerReportDTO performerReportDTO : nonPerformerDataList) {
				if(performerReportDTO!=null){
					performerReportDTO.setBenchCtr(0.1);
					if(performerReportDTO.getCTRReport()< performerReportDTO.getBenchCtr() ){
						NonPerformerReportDTO dto = new NonPerformerReportDTO();
						if(performerReportDTO.getClicksReport()!=null){
							dto.setClicksReport(performerReportDTO.getClicksReport());
						}
						if(performerReportDTO.getCTRReport()!=null){
							dto.setCTRReport(performerReportDTO.getCTRReport());
						}
						if(performerReportDTO.getImpressionDeliveredReport()!=null){
							dto.setImpressionDeliveredReport(performerReportDTO.getImpressionDeliveredReport());
						}
							
						if(performerReportDTO.getS1Site()!=null){
							dto.setSite(performerReportDTO.getS1Site());
						}
						nonPerformerReportDataList.add(dto);
					}
				}
			}
			return nonPerformerReportDataList;
		}
		
		public Map<String, Object> videoCampaignData(String orderId, String publisherIdInBQ) {
			INewAdvertiserViewDAO newAdvertiserDAO = new NewAdvertiserViewDAO();
			Map<String,Object> jsonChartMap = null;
			Map<String, Object> dateMap = new LinkedHashMap<String, Object>();
			Map<String, Object> columnMap = new LinkedHashMap<String, Object>();
			Map<String, Object> chartDataMap = new LinkedHashMap<String, Object>();
			QueryResponse queryResponse = null;
			DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			try {
				jsonChartMap = MemcacheUtil.getVideoCampaignData(orderId);
				if(jsonChartMap ==null || jsonChartMap.size()==0){
					jsonChartMap = new LinkedHashMap<String,Object>();
					QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherIdInBQ, LinMobileConstants.BQ_RICH_MEDIA);
					if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
						queryResponse = newAdvertiserDAO.videoCampaignData(orderId, queryDTO);
						
						if (queryResponse != null && queryResponse.getRows() != null) {
							List<com.google.api.services.bigquery.model.TableRow> rowList = queryResponse.getRows(); 
							int count = 0;
							double average_Interaction_RateVal = 0;
							double average_View_RateVal = 0;
							double completion_RateVal =0;
							int startVal =0;
							int firstQuartileVal = 0;
							int midpointVal = 0;
							int third_QuartileVal = 0;
							int completeVal = 0;
							int pauseVal = 0;
							int resumeVal = 0;
							int rewindVal = 0;
							int muteVal = 0;
							int unmuteVal = 0;
							int fullScreenVal =0;
							
							for (com.google.api.services.bigquery.model.TableRow row : rowList) {
								count++;
								List<TableCell> cellList = row.getF();
								
								String date=cellList.get(0).getV().toString();
								
								average_Interaction_RateVal = average_Interaction_RateVal + Double.valueOf(cellList.get(1).getV().toString());
								String average_Interaction_Rate=cellList.get(1).getV().toString();
								
								average_View_RateVal = average_View_RateVal + Double.valueOf(cellList.get(2).getV().toString());
								String average_View_Rate=cellList.get(2).getV().toString();
								
								completion_RateVal = completion_RateVal + Double.valueOf(cellList.get(3).getV().toString());
								String completion_Rate=cellList.get(3).getV().toString();
								
								startVal = startVal + Integer.parseInt(cellList.get(4).getV().toString());
								String start=cellList.get(4).getV().toString();
								
								firstQuartileVal = firstQuartileVal + Integer.parseInt(cellList.get(5).getV().toString());
								String firstQuartile=cellList.get(5).getV().toString();
								
								midpointVal = midpointVal + Integer.parseInt(cellList.get(6).getV().toString());
								String midpoint=cellList.get(6).getV().toString();
								
								third_QuartileVal = third_QuartileVal + Integer.parseInt(cellList.get(7).getV().toString());
								String third_Quartile=cellList.get(7).getV().toString();
								
								completeVal = completeVal + Integer.parseInt(cellList.get(8).getV().toString());
								String complete=cellList.get(8).getV().toString();
								
								pauseVal = pauseVal + Integer.parseInt(cellList.get(9).getV().toString());
								String pause=cellList.get(9).getV().toString();
								
								resumeVal = resumeVal + Integer.parseInt(cellList.get(10).getV().toString());
								String resume=cellList.get(10).getV().toString();
								
								rewindVal = rewindVal + Integer.parseInt(cellList.get(11).getV().toString());
								String rewind=cellList.get(11).getV().toString();
								
								muteVal = muteVal + Integer.parseInt(cellList.get(12).getV().toString());
								String mute=cellList.get(12).getV().toString();
								
								unmuteVal = unmuteVal + Integer.parseInt(cellList.get(13).getV().toString());
								String unmute=cellList.get(13).getV().toString();
								
								fullScreenVal = fullScreenVal + Integer.parseInt(cellList.get(14).getV().toString());
								String fullScreen=cellList.get(14).getV().toString();
								
								LineChartDTO lineChartDTO=new LineChartDTO();
								lineChartDTO.setDate(date);
								lineChartDTO.setAverage_Interaction_Rate(average_Interaction_Rate);
								lineChartDTO.setAverage_View_Rate(average_View_Rate);
								lineChartDTO.setCompletion_Rate(completion_Rate);
								lineChartDTO.setStart(start);
								lineChartDTO.setFirstQuartile(firstQuartile);
								lineChartDTO.setMidpoint(midpoint);
								lineChartDTO.setThird_Quartile(third_Quartile);
								lineChartDTO.setComplete(complete);
								lineChartDTO.setPause(pause);
								lineChartDTO.setResume(resume);
								lineChartDTO.setRewind(rewind);
								lineChartDTO.setMute(mute);
								lineChartDTO.setUnmute(unmute);
								lineChartDTO.setFullScreen(fullScreen);
								
								dateMap.put(date, null);
								chartDataMap.put(date, lineChartDTO);
						    }
							
							/*String[] chartTypes = {"averageInteractionRate","averageViewRate","completionRate","start","firstQuartile","midpoint","thirdQuartile","complete",
							 * "pause","resume","rewind","mute","unmute","fullScreen"};
							String lineChartJson = "";
							for(String chartType : chartTypes) {
								lineChartJson=createJsonForVideoCampaignLineChart(dateMap, chartDataMap, chartType);
								jsonChartMap.put(chartType, lineChartJson);
							}*/
							
							
							String lineChartJson = "";
							columnMap.put("Average Interaction Rate", null);
							columnMap.put("Average View Rate", null);
							columnMap.put("Completion Rate", null);
							lineChartJson=createJsonForVideoCampaignLineChart(dateMap, columnMap, chartDataMap);
							jsonChartMap.put("byRate", lineChartJson);
							
							columnMap.clear();
							columnMap.put("First Quartile", null);
							columnMap.put("Midpoint", null);
							columnMap.put("Third Quartile", null);
							columnMap.put("Complete", null);
							lineChartJson=createJsonForVideoCampaignLineChart(dateMap, columnMap, chartDataMap);
							jsonChartMap.put("byLength", lineChartJson);
							
							columnMap.clear();
							columnMap.put("Start", null);
							columnMap.put("Pause", null);
							columnMap.put("Resume", null);
							columnMap.put("Rewind", null);
							columnMap.put("Mute", null);
							columnMap.put("Unmute", null);
							columnMap.put("FullScreen", null);
							lineChartJson=createJsonForVideoCampaignLineChart(dateMap, columnMap, chartDataMap);
							jsonChartMap.put("byEvent", lineChartJson);
							
							average_Interaction_RateVal = average_Interaction_RateVal/count;
							average_View_RateVal = average_View_RateVal/count;
							completion_RateVal =completion_RateVal/count;
							/*startVal =startVal/count;
							firstQuartileVal = firstQuartileVal/count;
							midpointVal = midpointVal/count;
							third_QuartileVal = third_QuartileVal/count;
							completeVal = completeVal/count;
							pauseVal = pauseVal/count;
							resumeVal = resumeVal/count;
							rewindVal = rewindVal/count;
							muteVal = muteVal/count;
							unmuteVal = unmuteVal/count;
							fullScreenVal =fullScreenVal/count;*/
							
							LineChartDTO lineChartDTO=new LineChartDTO();
							lineChartDTO.setAverage_Interaction_Rate(df.format(average_Interaction_RateVal)+" %");
							lineChartDTO.setAverage_View_Rate(df.format(average_View_RateVal)+" %");
							lineChartDTO.setCompletion_Rate(df.format(completion_RateVal)+" %");
							lineChartDTO.setStart(lf.format(startVal)+"");
							lineChartDTO.setFirstQuartile(lf.format(firstQuartileVal)+"");
							lineChartDTO.setMidpoint(lf.format(midpointVal)+"");
							lineChartDTO.setThird_Quartile(lf.format(third_QuartileVal)+"");
							lineChartDTO.setComplete(lf.format(completeVal)+"");
							lineChartDTO.setPause(lf.format(pauseVal)+"");
							lineChartDTO.setResume(lf.format(resumeVal)+"");
							lineChartDTO.setRewind(lf.format(rewindVal)+"");
							lineChartDTO.setMute(lf.format(muteVal)+"");
							lineChartDTO.setUnmute(lf.format(unmuteVal)+"");
							lineChartDTO.setFullScreen(lf.format(fullScreenVal)+"");
							
							jsonChartMap.put("videoCampaignDataSum", lineChartDTO);
							
							MemcacheUtil.setVideoCampaignData(orderId, jsonChartMap);
							
						}else{
							log.warning("Invalid query or no data found...");
						}
						
						//MemcacheUtil.setDataMapInCacheByKey(resultMap, memcacheKey);
					}
				}
				else {
					log.info("Video Campaign Data in memcache..size:"+jsonChartMap.size());
				}
			}catch (Exception e) {
				log.severe("Exception in videoCampaignData : "+e.getMessage());
				
				return null;
			}
			return jsonChartMap;
		}
		
		public JSONArray campaignGridData(boolean activeCampaign, int campaignsPerPage, int pageNumber, String publisherIdInBQ, String searchKeyword, String commaSeperatedAccountIds) {
			INewAdvertiserViewDAO newAdvertiserDAO = new NewAdvertiserViewDAO();
			JSONArray campaignGridArray= new JSONArray();
			try {
				List<ChannelPerformancePopUpDTO> campaignGridDataList = MemcacheUtil.getCampaignGridDataFromCache(activeCampaign, publisherIdInBQ);
				if(campaignGridDataList == null || campaignGridDataList.size() == 0) {
					QueryDTO queryDTO = getQueryDTOForCampaignPerformance(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
					if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
						campaignGridDataList = newAdvertiserDAO.campaignGridData(activeCampaign, commaSeperatedAccountIds, queryDTO);
						if(campaignGridDataList != null && campaignGridDataList.size() > 0) {
							MemcacheUtil.setCampaignGridDataInCache(activeCampaign, publisherIdInBQ, campaignGridDataList);
						}
					}
				}
				if(campaignGridDataList != null && campaignGridDataList.size() > 0) {
					log.info("loaded campaignGridDataList :"+campaignGridDataList.size());
					int start = 0;
					int end = 0;
					
					if(searchKeyword.length() > 0) {
						end = campaignGridDataList.size()-1;
					}
					else {
						start = (pageNumber - 1)*campaignsPerPage;
						end = (pageNumber*campaignsPerPage) - 1;
					}
					log.info("start : "+start+", end : "+end);
					for(int i=start;i<=end;i++) {
						if(campaignGridDataList.size() > i) {
							ChannelPerformancePopUpDTO obj = campaignGridDataList.get(i);
							if(searchKeyword.length() == 0 || (searchKeyword.length() > 0 && (obj.getCampaignName().toLowerCase()).contains(searchKeyword))) {
								JSONObject campaignGridObj = new JSONObject();
								campaignGridObj.put("advertiserId",obj.getAdvertiserId());
								campaignGridObj.put("id",obj.getCampaignId());
								campaignGridObj.put("name",obj.getCampaignName());
								campaignGridObj.put("impression",obj.getCampaignImpressions());
								campaignGridObj.put("click",obj.getCampaignClicks());
								campaignGridObj.put("ctr",obj.getCampaignCtr());
								campaignGridObj.put("spend",obj.getCampaignSpend());
								campaignGridObj.put("pacing",obj.getCampaignPacing());
								campaignGridArray.add(campaignGridObj);
							}
						}
					}
				}
				
			} catch (Exception e) {
				log.severe("Exception in campaignGridData() of NewAdvertiserViewService : "+e.getMessage());
				
			}
			return campaignGridArray;
		}
}
