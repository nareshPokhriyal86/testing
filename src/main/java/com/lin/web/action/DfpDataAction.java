package com.lin.web.action;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.web.documents.TextSearchDocument;
import com.lin.web.dto.QueryDTO;
import com.lin.web.service.IProductService;
import com.lin.web.service.IQueryService;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.CSVReaderUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;

public class DfpDataAction implements ServletRequestAware,SessionAware{
	
	private static final Logger log = Logger.getLogger(DfpDataAction.class.getName());
	private HttpServletRequest request;
	private Map session;
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	private String startCounter;
	private String endCounter;
	public static int counter=1;	
	private String status;
	
	private String adServerNetwork;
	
	private String entityType;
	
	public String execute(){		
		log.info("DfpDataAction action executes..");
		//ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		
		
		return Action.SUCCESS;
	}	
	
	public String loadAdvertiserReportFromCSV(){		
		log.info("loadAdvertiserReportFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForAdvertiserReports(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}
		Date date=new Date();
		
		return Action.SUCCESS;
	}	
	
	
	public String loadCSVForByLocation(){		
		log.info("loadAdvertiserReportFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		
		
		boolean status=CSVReaderUtil.readCSVForByLocation(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}
		Date date=new Date();
		return Action.SUCCESS;
	}
	
	
	public String loadCSVForByMarket(){		
		log.info("loadAdvertiserReportFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		
		
		boolean status=CSVReaderUtil.readCSVForByMarket(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}
		Date date=new Date();
		return Action.SUCCESS;
	}

	
	public String loadAdvertiserAgencyDataFromCSV(){		
		log.info("loadAdvertiserAgencyDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
			
		boolean status=CSVReaderUtil.readCSVForAdvertiserAgencyData(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}	
		
		return Action.SUCCESS;
	}	
	
	public String loadOrderLineItemDataFromCSV(){		
		log.info("loadOrderLineItemDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
			
		boolean status=CSVReaderUtil.readCSVForOrderLineItemData(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}	
		
		return Action.SUCCESS;
	}
	
	public String loadReallocationDataFromCSV(){		
		log.info("loadReallocationDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
			
		boolean status=CSVReaderUtil.readCSVForReallocationData(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}	
		
		return Action.SUCCESS;
	}
	
	public String UploadAdvPerformanceMetricsDataFromCSV(){		
		log.info("UploadAdvPerformanceMetricsDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
			
		boolean status=CSVReaderUtil.readCSVForAdvPerformanceMetrics(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}	
		
		return Action.SUCCESS;
	}
	
	
	public String loadMostActiveDataFromCSV(){		
		log.info("loadMostActiveDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForMostActiveLineItem(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}		
		return Action.SUCCESS;
	}	
	
	public String loadPerformanceMetricsDataFromCSV(){		
		log.info("loadPerformanceMetricsDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForAdvPerformanceMetrics(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}		
		return Action.SUCCESS;
	}	
	
	public String loadChannelPerformanceDataFromCSV(){		
		log.info("loadChannelPerformanceDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForChannelPerformance(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}		
		return Action.SUCCESS;
	}
	
	public String loadPerformanceByPropertyFromCSV(){		
		log.info("loadPerformanceByPropertyFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForPerformanceByProperty(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}		
		return Action.SUCCESS;
	}
	
	public String loadSellThroughDataFromCSV(){		
		log.info("loadSellThroughDataFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForSellThroughData(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}		
		return Action.SUCCESS;
	}
	
/*	                                 24.08.2013
	public String loadTeamsFromCSV(){		
		log.info("loadTeamsFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
			
		boolean status=CSVReaderUtil.readCSVForTeams(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}	
		
		return Action.SUCCESS;
	}*/
	
	/*public String loadPublisherChannelsFromCSV(){		
		log.info("loadPublisherChannelsFromCSV action executes.."+linCSVFileName+" (startCounter:"+startCounter+", endCounter:"+endCounter+")");
		boolean status=CSVReaderUtil.readCSVForPublisherChannels(linCSV, Integer.parseInt(startCounter), Integer.parseInt(endCounter));
		
		if(status){
			request.setAttribute("status", "Uploaded successfully");
		}else{
			request.setAttribute("status", "Failed to upload file, please check log");
		}
		
		return Action.SUCCESS;
	}*/

	/*
	 * @author Youdhveer Panwar
	 * uploadEntityData in datastore using csv reader 
	 * 
	 */
	public String uploadEntityData(){		
		log.info("csvFile:"+linCSVFileName);
		if(linCSVFileName !=null){
			if(entityType !=null &&  (!entityType.equals("-1"))){
				if(entityType.equalsIgnoreCase("Industry")){
					status=CSVReaderUtil.readCSVAndUploadIndustry(linCSV);
				}else if(entityType.equalsIgnoreCase("Campaign Type")) {
					status = CSVReaderUtil.readCSVAndUploadCampaignType(linCSV);
				}else if(entityType.equalsIgnoreCase("Campaign Status")) {
					status = CSVReaderUtil.readCSVAndUploadCampaignStatus(linCSV);
				}else if(entityType.equalsIgnoreCase("GeoTargets")){
					status=CSVReaderUtil.readCSVAndUploadGeoTargets(linCSV);
				}else if(entityType.equalsIgnoreCase("KPI")){
					status=CSVReaderUtil.readCSVAndUploadKPIs(linCSV);
				}else if(entityType.equalsIgnoreCase("DfpOrderIdsObj")){
					status=CSVReaderUtil.readCSVAndUploadOrderIds(linCSV);	
				}else if(entityType.equalsIgnoreCase("CountryObj")){
					status=CSVReaderUtil.readCSVAndUploadUSAData(linCSV, "CountryObj");
				}else if(entityType.equalsIgnoreCase("StateObj")){
					status=CSVReaderUtil.readCSVAndUploadUSAData(linCSV, "StateObj");
				}else if(entityType.equalsIgnoreCase("CityObj")){
					status=CSVReaderUtil.readCSVAndUploadUSAData(linCSV, "CityObj");		
				}else if(entityType.equalsIgnoreCase("CreativeObj")){
					status=CSVReaderUtil.readCSVAndUploadCreativeObjData(linCSV);		
				}else if(entityType.equalsIgnoreCase("DeviceObj")){
					status=CSVReaderUtil.readCSVAndUploadDeviceOrPlatformData(linCSV,"DeviceObj");		
				}else if(entityType.equalsIgnoreCase("PlatformObj")){
					status=CSVReaderUtil.readCSVAndUploadDeviceOrPlatformData(linCSV,"PlatformObj");		
				}else if(entityType.equalsIgnoreCase("AdUnitHierarchy")){
					if(adServerNetwork==null || adServerNetwork.equals("-1")){
						status="Invalid adserverNetwork :"+adServerNetwork;
					}else{
						log.info("adServerNetwork:"+adServerNetwork);
						status=CSVReaderUtil.readCSVAndUploadAdUnitHierarchy(linCSV,adServerNetwork);
					}
					
				}else if(entityType.equalsIgnoreCase("Education Type")){
					status=CSVReaderUtil.readCSVAndUploadEducationType(linCSV);		
				}else if(entityType.equalsIgnoreCase("Ethinicity Type")){
					status=CSVReaderUtil.readCSVAndUploadEthinicityType(linCSV);		
				}else if(entityType.equalsIgnoreCase("UpdateMigratedCampaign")){
					status=CSVReaderUtil.readCSVAndUpdateMigratedCampaign(linCSV);		
				}
			}else{
				status= "Failed to upload csv file , please select an entity type also:"+linCSVFileName;
			}
						
		}else{
			status= "Failed to upload csv file :"+linCSVFileName;
		}		
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}
	
	/*Author : Dheeraj Kumar
	 * to call taskQueue to get records of CityObj from data store and create index search
	 * CityObj
	 */
	public void uploadCityData(){
    	log.info("uploadCityData action starts..");
    	
    	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    	try{
	    	int cityCount = productService.getCityObjCount();
	    	log.info("Total No. of CityObj got from datastore = "+cityCount);
	    	for(int offset = 1, limit = 1000, counter=1; offset < cityCount ;)
	    	{
	    		if(counter <= 5){
	    		TaskQueueUtil.uploadCityDataDefaultQueue("/loadCityDataThroughTaskQueue.lin", offset+"", limit+"");
	    		offset = offset + 1000;
	    		}else{
	    			counter = 1;
	    			Thread.sleep(1000*60*5);
	    		}
	    		counter++;
	    	}
    	}catch(Exception e)
    	{
    		log.info("Exception found in uploadCityData = "+e.toString());
    	}
    	
    }
	
	/*Author : Dheeraj Kumar
	 * to create taskQueues to get records of CityObj from data store and create index search
	 */
	public void loadCityDataThroughTaskQueue(){
    	log.info("loadCityDataThroughTaskQueue action starts...");
    	
    	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    	try{
    		
    		String offset = request.getParameter("offset");
    		String limit = request.getParameter("limit");
    		log.info("offset = "+offset);
    		log.info("limit = "+limit);
    		productService.loadCityDataThroughTaskQueue(offset,limit);
    		
    	}catch(Exception e)
    	{
    		log.info("Exception found in loadCityDataThroughTaskQueue Action = "+ e.toString());
    	}
    }
	
	
	
	//AdUnitHierarchy
	/*Author : Dheeraj Kumar
	 * to call taskQueue to get records of AdUnitHierarchy from data store and create index search
	 */
	public void uploadAdUnitData(){
    	log.info("update documents for adUnits...");
    	
    	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    	try{
	    	int adUnitCount = productService.getAdUnitHierarchyCount();
	    	log.info("Total No. of adUnits got from datastore = "+adUnitCount);

	    	for(int offset = 1, limit = 1000, counter = 1  ; offset < adUnitCount ;){
	    		if(counter <= 5){
		    		TaskQueueUtil.uploadAdUnitInQueue("/loadAdUnitDataThroughTaskQueue.lin", offset+"", limit+"");
		    		offset = offset + 1000;
	    		}else{
	    			counter = 1;
	    			Thread.sleep(1000*60*2);
	    		}	    		
	    		counter++;
	    	}
    	}catch(Exception e){
    		log.info("Exception found in uploadAdUnitData = "+e.toString());
    	}
    	
    }
	
	/*Author : Dheeraj Kumar
	 * to create taskQueues to get records of AdUnitHierarchy from data store and create index search
	 */
	public void loadAdUnitDataThroughTaskQueue(){
    	log.info("action starts...");
    	
    	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    	try{
    		
    		String offset = request.getParameter("offset");
    		String limit = request.getParameter("limit");
    		log.info("offset = "+offset);
    		log.info("limit = "+limit);
    		productService.loadAdUnitDataThroughTaskQueue(offset,limit);
    		
    	}catch(Exception e)
    	{
    		log.info("Exception found in loadCityDataThroughTaskQueue Action = "+ e.toString());
    	}
    }
	
	
	
	
	public String queryTest(){
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String publisherName=request.getParameter("publisherName");
		String dataSource=request.getParameter("dataSource");
		String schemaType=request.getParameter("schemaType");
		log.info("startDate:"+startDate+" and endDate:"+endDate);
		IQueryService queryService=(IQueryService) BusinessServiceLocator.locate(IQueryService.class);
		QueryDTO queryDTO=queryService.createQueryFromClause(publisherName, startDate, endDate, schemaType);
		if(queryDTO !=null){
			String queryClause=queryDTO.getQueryData();
			/*String [] tableArray=queryDTO.getQueryData().split(",");
			queryClause="";
			for(String table:tableArray){
				if(table.contains(dataSource)){
					queryClause=queryClause+","+table;
				}
			}*/
			status=queryClause;			
		}else{					
			status="No query generated, please check log...";
		}		
		return Action.SUCCESS;
	}
	
	public String copyFinalisedData(){
		log.info("copyFinaliseEntity action...");
		IReportService reportService=(IReportService) BusinessServiceLocator.locate(IReportService.class);
		boolean tableCopied=reportService.updateAllFinalisedTable();
		status="All finalise table enties copied :"+tableCopied;
		return Action.SUCCESS;	
	}
	
	public String deleteAllDocuments(){
		String indexName=request.getParameter("indexName");
		if(indexName !=null){
			TextSearchDocument.deleteAllDocumentsFromIndex(indexName);
		}else{
			status="Invalid index :"+indexName;
		}
		
		return Action.SUCCESS;	
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	public String getLinCSV() {
		return linCSV;
	}

	public void setLinCSV(String linCSV) {
		this.linCSV = linCSV;
	}

	public String getLinCSVContentType() {
		return linCSVContentType;
	}

	public void setLinCSVContentType(String linCSVContentType) {
		this.linCSVContentType = linCSVContentType;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}

	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}

	public void setStartCounter(String startCounter) {
		this.startCounter = startCounter;
	}

	public String getStartCounter() {
		return startCounter;
	}

	public void setEndCounter(String endCounter) {
		this.endCounter = endCounter;
	}

	public String getEndCounter() {
		return endCounter;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityType() {
		return entityType;
	}

	public String getAdServerNetwork() {
		return adServerNetwork;
	}

	public void setAdServerNetwork(String adServerNetwork) {
		this.adServerNetwork = adServerNetwork;
	}
}

