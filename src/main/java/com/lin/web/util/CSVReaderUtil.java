package com.lin.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.googlecode.objectify.Key;
import com.lin.persistance.dao.ILinMobileDAO;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.IProductDAO;
import com.lin.persistance.dao.IReportDAO;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.LinMobileDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.ProductDAO;
import com.lin.persistance.dao.impl.ReportDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdUnitDataObj;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserByMarketObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.CustomLineItemObj;
import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.DfpOrderIdsObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.ForcastedAdvertiserObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.OrderLineItemObj;
import com.lin.server.bean.PerformanceMetricsObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.PropertyChildObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.ReallocationDataObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.StateObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.documents.TextSearchDocument;
import com.lin.web.dto.DFPLineItemDTO;



public class CSVReaderUtil {
	
	private static final Logger log = Logger.getLogger(CSVReaderUtil.class.getName());
	
	 private static List<String> adUnitReportColsList=null;
	 static final String [] adUnitReportCols={
		 "Ad unit 1","Ad unit 2","Ad unit 3","Ad unit 4","Ad unit 5","Ad unit ID 1","Ad unit ID 2","Ad unit ID 3","Ad unit ID 4","Ad unit ID 5"
	 };
	    
	public static boolean readCSVForAdvertiserReports(String fileName,int startCounter,int endCounter){
		
		
		InputStream inputStream;
		try {
			log.info("========readCSVForAdvertiserReports==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForAdvertiserReports==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){		
				
				if(line.length >= 32){
					
					String advertiserName=line[0];
					String orderName=line[1];
					String lineItemName=line[2];
					String creativeSize=line[3];
					String date=line[4];
					date=DateUtil.getFormatedDate(date, "MM/dd/yy", "yyyy-MM-dd");
					long advertiserId=Long.parseLong(line[5]);
					long orderId=Long.parseLong(line[6]);
					long lineItemId=Long.parseLong(line[7]);
					
					String advertiserLabel=line[8];
					String orderStartDate=line[9];
					orderStartDate=DateUtil.getFormatedDate(orderStartDate, "MM/dd/yy", "yyyy-MM-dd");
					String orderEndDate=line[10];
					orderEndDate=DateUtil.getFormatedDate(orderEndDate, "MM/dd/yy", "yyyy-MM-dd");
					String orderStartTime=line[11];
					String orderEndTime=line[12];
					
					long orderPONumber=0;
					if(LinMobileUtil.isNumeric(line[13])){
						orderPONumber=Long.parseLong(line[13]);
					}
					
					long orderLifeTimeClicks=0;
					if(LinMobileUtil.isNumeric(line[14])){
						orderLifeTimeClicks=Long.parseLong(line[14]);
					}
					
					long orderLifeTimeImpressions=0;
					if(LinMobileUtil.isNumeric(line[15])){
						orderLifeTimeImpressions=Long.parseLong(line[15]);
					}
					String agencyName=line[16];
					String lineItemStartDate=line[17];
					lineItemStartDate=DateUtil.getFormatedDate(lineItemStartDate, "MM/dd/yy", "yyyy-MM-dd");
					String lineItemEndDate=line[18];
					lineItemEndDate=DateUtil.getFormatedDate(lineItemEndDate, "MM/dd/yy", "yyyy-MM-dd");
					String lineItemStartTime=line[19];
					String lineItemEndTime=line[20];
					String costType=line[21];	
					double rate=0;
					if(LinMobileUtil.isNumeric(line[22])){
						rate=Double.parseDouble(line[22]);
					}
					
					long goalQuantity=0;
					if(LinMobileUtil.isNumeric(line[23])){
						goalQuantity=Long.parseLong(line[23]);
					}
					
					long lineItemLifeTimeClicks=0;
					if(LinMobileUtil.isNumeric(line[24])){
						lineItemLifeTimeClicks=Long.parseLong(line[24]);
					}
					
					long lineItemLifeItemImpressions=0;
					if(LinMobileUtil.isNumeric(line[25])){
						lineItemLifeItemImpressions=Long.parseLong(line[25]);
					}
					
					String contractedQuantity=line[26];
					
					long adServerImpressions=0;
					if(LinMobileUtil.isNumeric(line[27])){
						adServerImpressions=Long.parseLong(line[27]);
					}
					
					
					String adServerCTRStr=line[28];
					if(adServerCTRStr !=null && adServerCTRStr.equals("N/A")){
						adServerCTRStr="0";
					}else if(adServerCTRStr !=null && adServerCTRStr.contains("%")){
						adServerCTRStr=adServerCTRStr.replace("%","");
					}
					double adServerCTR=0;
					if(LinMobileUtil.isNumeric(adServerCTRStr)){
						adServerCTR=Double.parseDouble(adServerCTRStr);
					}
					
					
					long adServerClicks=0;
					if(LinMobileUtil.isNumeric(line[29])){
						adServerClicks=Long.parseLong(line[29]);
					}
					
					String deliveryIndicatorStr=line[30];
					double deliveryIndicator=0;
					if(deliveryIndicatorStr !=null && deliveryIndicatorStr.equals("N/A")){
						deliveryIndicatorStr="0.0";
					}else if(deliveryIndicatorStr !=null && deliveryIndicatorStr.contains("%")){
						deliveryIndicatorStr=deliveryIndicatorStr.replace("%","");
					}					
					if(LinMobileUtil.isNumeric(deliveryIndicatorStr)){
						deliveryIndicator=Double.parseDouble(deliveryIndicatorStr);
					}
					
					double adServerECPM=Double.parseDouble(line[31]);
					
					String id=date+":"+creativeSize;
					
					AdvertiserReportObj obj=new AdvertiserReportObj(id, advertiserName, orderName, lineItemName, creativeSize, date, advertiserId, orderId, lineItemId, advertiserLabel, orderStartDate, orderEndDate, orderStartTime, orderEndTime, orderPONumber, orderLifeTimeClicks, orderLifeTimeImpressions, agencyName, lineItemStartDate, lineItemEndDate, lineItemStartTime, lineItemEndTime, costType, rate, goalQuantity, lineItemLifeTimeClicks, lineItemLifeItemImpressions, contractedQuantity, adServerImpressions, adServerCTR, adServerClicks, deliveryIndicator, adServerECPM);
				   
				    if(count >= startCounter && count < endCounter){
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				}
				count++;
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}
	
 public static boolean readCSVForAdvertiserAgencyData(String fileName,int startCounter,int endCounter){		
		InputStream inputStream;
		try {
			log.info("========readCSVForAdvertiserAgencyData==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForAdvertiserAgencyData==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){			
				count++;
				if(line.length >= 3){
					long agencyId=(long) count;
					String agencyName=line[0];
					long advertiserId=Long.parseLong(line[1]);
					String advertiserName=line[2];
					double budget=Double.parseDouble(line[3]);
					double revenueDelivered=Double.parseDouble(line[4]);
					double revenueRemaining=Double.parseDouble(line[5]);
					long impressionsBooked=Long.parseLong(line[6]);
					double clicks=Double.parseDouble(line[7]);
					double CTR=Double.parseDouble(line[8]);
					String id=""+advertiserId;
					AgencyAdvertiserObj obj=new AgencyAdvertiserObj(id, agencyId, agencyName, advertiserId, advertiserName, budget, revenueDelivered, revenueRemaining, impressionsBooked, clicks, CTR);
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){
				    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				}				
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}
 
 public static boolean readCSVForOrderLineItemData(String fileName,int startCounter,int endCounter){
		
		InputStream inputStream;
		try {
			log.info("========readCSVForOrderLineItemData==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForOrderLineItemData==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){					
				if(line.length >= 3){
					
					String orderName=line[0];
					String lineItemName=line[1];
					long orderId=Long.parseLong(line[2]);
					long lineItemId=Long.parseLong(line[3]);
					String id=orderId+":"+lineItemId;
					double totalBudget=Double.parseDouble(line[4]);
					double revenueDelivered=Double.parseDouble(line[5]);
					double revenueRemaining=Double.parseDouble(line[6]);
					long impressionsBooked=Long.parseLong(line[7]);
					long clicks=Long.parseLong(line[8]);
					double CTR=Double.parseDouble(line[9]);
					double CPM=Double.parseDouble(line[10]);
					long impressionsLifeTime=Long.parseLong(line[11]);
					long clicksLifeTimes=Long.parseLong(line[12]);
					String startDate=line[13];
					String endDate=line[14];
					OrderLineItemObj obj=new OrderLineItemObj(id, orderId, orderName, lineItemId, lineItemName, impressionsLifeTime, clicksLifeTimes, CPM,CTR, revenueDelivered, revenueRemaining, impressionsBooked, clicks, totalBudget);
					obj.setStartDate(startDate);
					obj.setEndDate(endDate);
					
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){				    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				}
				count++;
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}
 
   public static boolean readCSVForMostActiveLineItem(String fileName,int startCounter,int endCounter){
		
		InputStream inputStream;
		try {
			log.info("========readCSVForMostActiveLineItem==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForMostActiveLineItem==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){			
					String lineItemName=line[0];
					double CTR=Double.parseDouble(line[1]);
					double changeInSelectedTime=Double.parseDouble(line[2]);
					double changeLifeTime=Double.parseDouble(line[3]);
					long deliveredImpressions=Long.parseLong(line[4]);
					double deliveryIndicator=Double.parseDouble(line[5]);
					String date=line[6];
					CustomLineItemObj obj=new CustomLineItemObj(lineItemName, CTR, changeInSelectedTime, changeLifeTime, deliveredImpressions, deliveryIndicator,date);
					
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){				    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				
				count++;
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}
    
 public static boolean readCSVForAdvPerformanceMetrics(String fileName,int startCounter,int endCounter){
		
		InputStream inputStream;
		try {
			log.info("========readCSVForAdvPerformanceMetrics=========="+fileName);
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			 ILinMobileDAO linDAO=new LinMobileDAO();
			CSVReader csvReader = new CSVReader(reader);
			
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForAdvPerformanceMetrics==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){			
				
				String date=line[0];
				String advertiserName=line[1];
				String lineItemName=line[2];
				String startDate=line[3];
				String endDate=line[4];
				long impressionsBooked=Long.parseLong(line[5]);
				long impressionsDelivered=Long.parseLong(line[6]);
				long clicks=Long.parseLong(line[7]);
				double CTR =Double.parseDouble(line[8]);
				double budget=Double.parseDouble(line[9]);
				double revenueRecoByDay=Double.parseDouble(line[10]);
				double revenueLeftByDay=Double.parseDouble(line[11]);
				String id=lineItemName+":"+date;
				PerformanceMetricsObj obj=new PerformanceMetricsObj(id, date, advertiserName, lineItemName, startDate, endDate, impressionsBooked, impressionsDelivered, clicks, CTR, budget, revenueRecoByDay, revenueLeftByDay);
					
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){			    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				count++;
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}
 
 public static boolean readCSVForByLocation(String fileName,int startCounter,int endCounter){
		
		InputStream inputStream;
		try {
			log.info("========readCSVForByLocation==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForByLocation==========allElements:"+allElements.size());
			int count=0;
			for(String[] line:allElements){			
				    count++;
				   
					String state=line[0];
					long impressions=Long.parseLong(line[1]);
					double ctrPercent=Double.parseDouble(line[2]);
					String date=line[3];
					String id=state+":"+date;
					 
					AdvertiserByLocationObj obj=new AdvertiserByLocationObj(id,state, impressions, ctrPercent, date);
					
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){				    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}

public static boolean readCSVForByMarket(String fileName,int startCounter,int endCounter){
		
		InputStream inputStream;
		try {
			log.info("========readCSVForOrderLineItemData==========");
			inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			
			CSVReader csvReader = new CSVReader(reader);
			 ILinMobileDAO linDAO=new LinMobileDAO();
			List<String[]> allElements = csvReader.readAll();
			log.info("========readCSVForOrderLineItemData==========allElements:"+allElements.size());
			int count=1;
			for(String[] line:allElements){
				  
					String state=line[0];
					String linProperty=line[1];
					double ctrPercent=Double.parseDouble(line[2]);
					String date=line[3];
					String id=state+":"+date;
					AdvertiserByMarketObj obj=new AdvertiserByMarketObj(id,state, linProperty, ctrPercent, date);
					
					log.info("Going to save this object...");
				    if(count >= startCounter && count < endCounter){				    	
				    	
				    	 linDAO.saveObject(obj);
				    	 log.info("Object saved successfully in datastore :"+count);
				    }else if(count>endCounter){				    	
				    	break;
				    }else{
				    	log.info("Already imported...."+count);
				    }				    
				
				count++;
			}
			csvReader.close();
	 		reader.close();
			return true;
		}catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			return false;
		} catch (Exception e) {		
			log.severe("Exception:"+e.getMessage());
			return false;
		}
		
	}

public static boolean readCSVForChannelPerformance(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForChannelPerformance=========="+fileName);
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		
		CSVReader csvReader = new CSVReader(reader);
		 ILinMobileDAO linDAO=new LinMobileDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForChannelPerformance==========allElements:"+allElements.size());
		int count=0;
		for(String[] line:allElements){			
			
			
			String date=line[0];
			long requests=Long.parseLong(line[1]);
			long served=Long.parseLong(line[2]);
			long impressionsDelivered=Long.parseLong(line[3]);
			double fillRate=Double.parseDouble(line[4]);
			long clicks=Long.parseLong(line[5]);
			double CTR=Double.parseDouble(line[6]);
			double revenue=Double.parseDouble(line[7]);
			double eCPM=Double.parseDouble(line[8]);
			double RPM=Double.parseDouble(line[9]);
			String puiblisherName=line[10];
			String channelName=line[11];
			double CHG=Double.parseDouble(line[12]);
			double percentageCHG=Double.parseDouble(line[13]);	
			double payout=Double.parseDouble(line[14]);
			String id=channelName+":"+date;
			
			PublisherChannelObj obj=new PublisherChannelObj(id, date, requests, served, impressionsDelivered, fillRate, clicks, CTR, revenue, eCPM, RPM, puiblisherName, channelName, CHG, percentageCHG, payout);
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }				    
			
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
}
 
public static boolean readCSVForPerformanceByProperty(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForPerformanceByProperty==========");
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		
		CSVReader csvReader = new CSVReader(reader);
		ILinMobileDAO linDAO=new LinMobileDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForPerformanceByProperty==========allElements:"+allElements.size());
		int count=0;
		for(String[] line:allElements){				
			String name=line[0];
			double eCPM=Double.parseDouble(line[1]);
			double CHG=Double.parseDouble(line[2]);
			double percentageCHG=Double.parseDouble(line[3]);
			long impressionsDelivered=Long.parseLong(line[4]);
			long clicks=Long.parseLong(line[5]);
			double payout=Double.parseDouble(line[6]);
			String date=line[7];
			String id=name+":"+date;				
			PublisherPropertiesObj obj=new PublisherPropertiesObj(id,name,eCPM,CHG,percentageCHG,impressionsDelivered,clicks,payout,date);
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	 
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }				    
			
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
}

public static boolean readCSVForSellThroughData(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForSellThroughData==========");
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		ILinMobileDAO linDAO=new LinMobileDAO();
		CSVReader csvReader = new CSVReader(reader);
		
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForSellThroughData==========allElements:"+allElements.size());
		int count=0;
		for(String[] line:allElements){			    
				String property=line[0];
				String adUnit=line[1];
				String forecastedImpressions=line[2];
				String availableImpressions=line[3];
				String reservedImpressions=line[4];
				String sellThroughRate=line[5];
				String date=line[6];
				String id=property+":"+date;
				
				SellThroughDataObj obj=new SellThroughDataObj(id,property,adUnit,forecastedImpressions,availableImpressions,reservedImpressions,sellThroughRate,date);
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	 
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }				    
			
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
  }

public static boolean readCSVForReallocationData(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForReallocationData==========");
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		
		CSVReader csvReader = new CSVReader(reader);
		 ILinMobileDAO linDAO=new LinMobileDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForReallocationData==========allElements:"+allElements.size());
		int count=0;
		for(String[] line:allElements){			
			count++;
			
				String date = line[0];
				String lineItem=line[1];
				String size=line[2];
				float ecpm=Float.parseFloat(line[3]);
				float budget=Float.parseFloat(line[4]);
				long bookedImp=Long.parseLong(line[5]);
				long delivery=Long.parseLong(line[6]);
				int clicks=Integer.parseInt(line[7]);
				long overUnder=Long.parseLong(line[8]);
				float ctr= Float.parseFloat(line[9]);
				float revenueDelivered=Float.parseFloat(line[10]);
				float revenueRemaining=Float.parseFloat(line[11]);
				float revisedBudget=Float.parseFloat(line[12]);
				long revisedBookedImp=Long.parseLong(line[13]);
				float revenueToBeDelivered=Float.parseFloat(line[14]);
				String id = lineItem+":"+date;
				ReallocationDataObj obj = new ReallocationDataObj(id, date, lineItem, size, ecpm, budget, bookedImp, delivery, clicks, overUnder, ctr, revenueDelivered, revenueRemaining, revisedBudget, revisedBookedImp, revenueToBeDelivered);
				
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }			
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
}
public static boolean readCSVForActiveAdvertiserData(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForOrderLineItemData==========");
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		
		CSVReader csvReader = new CSVReader(reader);
		ILinMobileDAO linDAO=new LinMobileDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForOrderLineItemData==========allElements:"+allElements.size());
		int count=0;
		for(String[] line:allElements){			
			count++;
			if(line.length >= 7){
				String days = line[0];
				String startDate = line[1];
				String endDate = line[2];
				float deliveredImpressions = Float.parseFloat(line[3]);
				int clicks=Integer.parseInt(line[4]);
				float ctr= Float.parseFloat(line[5]);
				float revenueDelivered=Float.parseFloat(line[6]);
				float revenueRemaining=Float.parseFloat(line[7]);
							
				String id = count+"";
				ActualAdvertiserObj obj = new ActualAdvertiserObj(id, days, startDate, endDate, deliveredImpressions, clicks, ctr, revenueDelivered, revenueRemaining);
				
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	 
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }				    
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
}
public static boolean readCSVForForcastAdvertiserData(String fileName,int startCounter,int endCounter){
	
	InputStream inputStream;
	try {
		log.info("========readCSVForOrderLineItemData==========");
		inputStream = IOUtils.toInputStream(fileName,"ISO-8859-1");
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		
		CSVReader csvReader = new CSVReader(reader);
		
		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForOrderLineItemData==========allElements:"+allElements.size());
		int count=0;
		 ILinMobileDAO linDAO=new LinMobileDAO();
		for(String[] line:allElements){			
			count++;
			if(line.length >= 7){
				String days = line[0];
				String startDate = line[1];
				String endDate = line[2];
				float deliveredImpressions = Float.parseFloat(line[3]);
				int clicks=Integer.parseInt(line[4]);
				float ctr= Float.parseFloat(line[5]);
				float revenueDelivered=Float.parseFloat(line[6]);
				float revenueRemaining=Float.parseFloat(line[7]);
							
				String id = count+"";
				ForcastedAdvertiserObj obj = new ForcastedAdvertiserObj(id, days, deliveredImpressions, startDate, endDate, clicks, ctr, revenueDelivered, revenueRemaining);
				
				
				log.info("Going to save this object...");
			    if(count >= startCounter && count < endCounter){				    	
			    	
			    	 linDAO.saveObject(obj);
			    	 log.info("Object saved successfully in datastore :"+count);
			    }else if(count>endCounter){				    	
			    	break;
			    }else{
			    	log.info("Already imported...."+count);
			    }				    
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	}catch (IOException e) {
		log.severe("IOException:"+e.getMessage());
		return false;
	} catch (Exception e) {		
		log.severe("Exception:"+e.getMessage());
		return false;
	}
	
}

/*                    22.08.2013
 * public static boolean readCSVForTeams(String fileName, int startCounter, int endCounter) {
	InputStream inputStream;
	try {
		log.info("======== readCSVForTeams ==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("======== readCSVForTeams ==========allElements:" + allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 1) {

				String teamName = line[0];
				String id = teamName;

				TeamsObj obj = new TeamsObj(id, teamName);

				log.info("Going to save this object...");
				if (count >= startCounter && count < endCounter) {
					IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
					userDetailsDAO.saveObject(obj);
					log.info("Object saved successfully in datastore :"
							+ count);
				} else if (count > endCounter) {
					break;
				} else {
					log.info("Already imported...." + count);
				}
			}
			count++;
		}
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		return false;
	}
 }*/

/*public static boolean readCSVForPublisherChannels(String fileName, int startCounter, int endCounter) {
	InputStream inputStream;
	try {
		log.info("======== readCSVForPublisherChannels ==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("======== readCSVForPublisherChannels ==========allElements:" + allElements.size());
		int count = 0;
		
		for (String[] line : allElements) {			
			if (line.length >= 1) {
				long id = count+1;
				String publisherName = line[0];
				String channelsName = line[1];
				String channelViewOrder = line[2];
				String dataSource = line[3];
				String companyName = line[4];

				PublisherChannels obj = new PublisherChannels(id, publisherName, channelsName, channelViewOrder, dataSource, companyName);

				log.info("Going to save this object...");
				if (count >= startCounter && count < endCounter) {
					ILinMobileDAO linDAO=new LinMobileDAO();
			    	 linDAO.saveObject(obj);
					log.info("Object saved successfully in datastore :"
							+ count);
				} else if (count > endCounter) {
					break;
				} else {
					log.info("Already imported...." + count);
				}
			}
			count++;
		}
		
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		return false;
	}
 }*/

public static boolean readCSVForRolesAndAuthorisation(String fileName, int startCounter, int endCounter) {
	InputStream inputStream;
	try {
		log.info("======== readCSVForRolesAndAuthorisation ==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("======== readCSVForRolesAndAuthorisation ==========allElements:" + allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 4) {

				RolesAndAuthorisation obj = new RolesAndAuthorisation();
				obj.setId(Long.valueOf(line[0].trim()));
				obj.setRoleName(line[1].trim());
				obj.setRoleStatus(line[2].trim());
				obj.setRoleDescription(line[3].trim());
				obj.setRoleType(LinMobileConstants.DEFINED_TYPES[0]);
				try {
					obj.setA1(line[4].trim());
					obj.setA2(line[5].trim());
					obj.setA3(line[6].trim());
					obj.setA4(line[7].trim());
					obj.setA5(line[8].trim());
					obj.setA6(line[9].trim());
					obj.setA7(line[10].trim());
					obj.setA8(line[11].trim());
					obj.setA9(line[12].trim());
					obj.setA10(line[13].trim());
					obj.setA11(line[14].trim());
					obj.setA12(line[15].trim());
					obj.setA13(line[16].trim());
					obj.setA14(line[17].trim());
					obj.setA15(line[18].trim());
					obj.setA16(line[19].trim());
					obj.setA17(line[20].trim());
					obj.setA18(line[21].trim());
					obj.setA19(line[22].trim());
					obj.setA20(line[23].trim());
					obj.setA21(line[24].trim());
					obj.setA22(line[25].trim());
					obj.setA23(line[26].trim());
					obj.setA24(line[27].trim());
					obj.setA25(line[28].trim());
					obj.setA26(line[29].trim());
					obj.setA27(line[30].trim());
					obj.setA28(line[31].trim());
					obj.setA29(line[32].trim());
					obj.setA30(line[33].trim());
					obj.setA31(line[34].trim());
					obj.setA32(line[35].trim());
					obj.setA33(line[36].trim());
					obj.setA34(line[37].trim());
					obj.setA35(line[38].trim());
					obj.setA36(line[39].trim());
					obj.setA37(line[40].trim());
					obj.setA38(line[41].trim());
					obj.setA39(line[42].trim());
					obj.setA40(line[43].trim());
					obj.setA41(line[44].trim());
					obj.setA42(line[45].trim());
					obj.setA43(line[46].trim());
					obj.setA44(line[47].trim());
					obj.setA45(line[48].trim());
					obj.setA46(line[49].trim());
					obj.setA47(line[50].trim());
					obj.setA48(line[51].trim());
					obj.setA49(line[52].trim());
					obj.setA50(line[53].trim());
					obj.setA51(line[54].trim());
					obj.setA52(line[55].trim());
					obj.setA53(line[56].trim());
					obj.setA54(line[57].trim());
					obj.setA55(line[58].trim());
					obj.setA56(line[59].trim());
					obj.setA57(line[60].trim());
					obj.setA58(line[61].trim());
					obj.setA59(line[62].trim());
					obj.setA60(line[63].trim());
					obj.setA61(line[64].trim());
					obj.setA62(line[65].trim());
					obj.setA63(line[66].trim());
					obj.setA64(line[67].trim());
					obj.setA65(line[68].trim());
					obj.setA66(line[69].trim());
					obj.setA67(line[70].trim());
					obj.setA68(line[71].trim());
					obj.setA69(line[72].trim());
					obj.setA70(line[73].trim());
					obj.setA71(line[74].trim());
					obj.setA72(line[75].trim());
					obj.setA73(line[76].trim());
					obj.setA74(line[77].trim());
					obj.setA75(line[78].trim());
					obj.setA76(line[79].trim());
					obj.setA77(line[80].trim());
					obj.setA78(line[81].trim());
					obj.setA79(line[82].trim());
					obj.setA80(line[83].trim());
					obj.setA81(line[84].trim());
					obj.setA82(line[85].trim());
					obj.setA83(line[86].trim());
					obj.setA84(line[87].trim());
					obj.setA85(line[88].trim());
					obj.setA86(line[89].trim());
					obj.setA87(line[90].trim());
					obj.setA88(line[91].trim());
					obj.setA89(line[92].trim());
					obj.setA90(line[93].trim());
					obj.setA91(line[94].trim());
					obj.setA92(line[95].trim());
					obj.setA93(line[96].trim());
					obj.setA94(line[97].trim());
					obj.setA95(line[98].trim());
					obj.setA96(line[99].trim());
					obj.setA97(line[100].trim());
					obj.setA98(line[101].trim());
					obj.setA99(line[102].trim());
					obj.setA100(line[103].trim());
				}catch (ArrayIndexOutOfBoundsException e) {
					log.severe("ArrayIndexOutOfBoundsException in readCSVForRolesAndAuthorisation of CSVReaderUtil. : "+e.getMessage());
				}
				catch (Exception e) {
					log.severe("Exception in readCSVForRolesAndAuthorisation of CSVReaderUtil. : "+e.getMessage());
				}

				log.info("Going to save this object...");
				if (count >= startCounter && count < endCounter) {
				
					userDetailsDAO.saveObject(obj);
					log.info("Object saved successfully in datastore :"
							+ count);
				} else if (count > endCounter) {
					break;
				} else {
					log.info("Already imported...." + count);
				}
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
}

public static boolean readCSVForAuthorisationText(String fileName, int startCounter, int endCounter) {
	InputStream inputStream;
	try {
		log.info("======== readCSVForAuthorisationText ==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("======== readCSVForAuthorisationText ==========allElements:" + allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 5) {

				AuthorisationTextObj obj = new AuthorisationTextObj();
				obj.setRolesAndAuthorisationColumnName(line[0].trim());
				obj.setAuthorisationText(line[1].trim());
				obj.setAuthorisationTextKeyword(line[2].trim());
				obj.setAuthorisationTextStatus(line[3].trim());
				obj.setAuthorisationForPage(line[4].trim());

				log.info("Going to save this object...");
				if (count >= startCounter && count < endCounter) {
					
					userDetailsDAO.saveObject(obj);
					log.info("Object saved successfully in datastore :"
							+ count);
				} else if (count > endCounter) {
					break;
				} else {
					log.info("Already imported...." + count);
				}
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
}

public static boolean uploadCompany(String fileName) {
	InputStream inputStream;
	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	try {
		log.info("========uploadCompany==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("========uploadCompany==========allElements:"
				+ allElements.size());
		for (String[] line : allElements) {			
			if (line.length >= 14) {
				
				CompanyObj obj = new CompanyObj();
				
				long id = Long.valueOf(line[0].trim());
				String companyName = line[1].trim();
				String status = line[2].trim();
				String companyType = line[3].trim();
				String adServerInfo = line[4].trim();
				int bqIdentifier = Integer.parseInt(line[5].trim());

				obj.setId(id);
				obj.setCompanyName(companyName);
				obj.setStatus(status);
				obj.setCompanyType(companyType);
				obj.setAdServerInfo(adServerInfo);
				obj.setBqIdentifier(bqIdentifier);
				obj.setAdServerId(getListFromDelimitedString(line[6].trim(),"<SEP>"));
				obj.setAdServerUsername(getListFromDelimitedString(line[7].trim(),"<SEP>"));
				obj.setAdServerPassword(getListFromDelimitedString(line[8].trim(),"<SEP>"));
				obj.setAppViews(getListFromDelimitedString(line[9].trim(),"<SEP>"));
				obj.setDemandPartnerId(getListFromDelimitedString(line[10].trim(),"<SEP>"));
				obj.setDataSource(line[11].trim());
				obj.setDemandPartnerType(line[12].trim());
				obj.setPassback_Site_type(getListFromDelimitedString(line[13].trim(),"<SEP>"));
				
				if(companyType.equals(LinMobileConstants.COMPANY_TYPE[0])) {		// publisher
					obj.setAccessToAccounts(true);
					obj.setAccessToProperties(true);
				}
				else if(companyType.equals(LinMobileConstants.COMPANY_TYPE[1])) {	// Demand Partner
					obj.setAccessToAccounts(true);
					obj.setAccessToProperties(false);
				}
				else if(companyType.equals(LinMobileConstants.COMPANY_TYPE[2])) {	// client
					obj.setAccessToAccounts(true);
					obj.setAccessToProperties(false);
				}
				
				obj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
				obj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				
				log.info("Going to save company...");
				userDetailsDAO.saveObject(obj);
				log.info("Saved");
				
				log.info("creating default AllEntity team for id : "+id+LinMobileConstants.TEAM_ALL_ENTITIE+" companyType : "+companyType);
				TeamPropertiesObj teamPropertiesObj = new TeamPropertiesObj();
				
				// create All Entity
				teamPropertiesObj.setCompanyId(id+"");
				teamPropertiesObj.setTeamType(LinMobileConstants.DEFINED_TYPES[0]);
				teamPropertiesObj.setTeamStatus(status);
				teamPropertiesObj.setTeamName(LinMobileConstants.TEAM_ALL_ENTITIE+" ("+companyName+")");		
				teamPropertiesObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj.setLastModifiedByUserId(0);
				teamPropertiesObj.setCreatedByUserId(0);
				
				List<String> tempList = new ArrayList<String>();
				tempList.addAll(obj.getAppViews());
				teamPropertiesObj.setAppViews(tempList);
				
				tempList = new ArrayList<String>();
				if(obj.isAccessToProperties()) {
					tempList.add(LinMobileConstants.ALL_PROPERTIES);
				}
				teamPropertiesObj.setPropertyId(tempList);
				
				tempList = new ArrayList<String>();
				if(obj.isAccessToAccounts()) {
					tempList.add(LinMobileConstants.NO_RESTRICTIONS);
					teamPropertiesObj.setAgencyId(tempList);
					teamPropertiesObj.setAdvertiserId(tempList);
				}
				else {
					teamPropertiesObj.setAgencyId(tempList);
					teamPropertiesObj.setAdvertiserId(tempList);
				}
				
				String uniqueNumber = LinMobileConstants.TEAM_ALL_ENTITIE+""+id;
				teamPropertiesObj.setId(uniqueNumber);
				userDetailsDAO.saveObject(teamPropertiesObj);
				log.info("default AllEntity team created successfully");
				
				log.info("creating default NoEntity team for id : "+id+LinMobileConstants.TEAM_NO_ENTITIE +" companyType : "+companyType);
				TeamPropertiesObj teamPropertiesObj2 = new TeamPropertiesObj();
				
				// create No Entity
				teamPropertiesObj2.setCompanyId(id+"");
				teamPropertiesObj2.setTeamType(LinMobileConstants.DEFINED_TYPES[0]);
				teamPropertiesObj2.setTeamStatus(status);
				teamPropertiesObj2.setTeamName(LinMobileConstants.TEAM_NO_ENTITIE+" ("+companyName+")");		
				teamPropertiesObj2.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj2.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj2.setLastModifiedByUserId(0);
				teamPropertiesObj2.setCreatedByUserId(0);
				
				teamPropertiesObj2.setAppViews(new ArrayList<String>());
				
				uniqueNumber = LinMobileConstants.TEAM_NO_ENTITIE+""+id;
				teamPropertiesObj2.setId(uniqueNumber);
				userDetailsDAO.saveObject(teamPropertiesObj2);
			}
		}
		csvReader.close();
 		reader.close();
		
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
}

public static List<String> getListFromDelimitedString(String delimitedStr, String delimiter) {
	List<String> list = new ArrayList<String>();
	if(delimitedStr != null && !delimitedStr.trim().equals("") && delimiter != null) {
		String[] strArray = delimitedStr.split(delimiter);
		if(strArray.length > 0) {
			for (String str : strArray) {
				if(str != null) {
					list.add(str.trim());
				}
			}
		}
	}
	return list; 
}

public static boolean uploadProperties(String fileName) {
	InputStream inputStream;
	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	try {
		log.info("========uploadProperties==========");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("========uploadProperties==========allElements:"
				+ allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 7) {
				if(count==0){
					log.info("Skip first header row...");
				}
				else {
					List<PropertyChildObj> childList = new ArrayList<PropertyChildObj>();
					String propertyName = line[0].trim();
					String DFPPropertyName = line[1].trim();
					String DFPPropertyId = line[2].trim();
					String adserverId = line[3].trim();
					String adServerUserName = line[4].trim();
					String status = line[5].trim(); 
					String companyId = line[6].trim();
					
					if(line.length > 7) {
						for(int i=7; i<=line.length-1; i++) {
							String[] childInfo = line[i].trim().split("<SEP>");
							if(childInfo.length == 2 && childInfo[0] != null &&  childInfo[1] != null && childInfo[0].trim().length() > 0) {
								PropertyChildObj childObj = new PropertyChildObj(childInfo[0].trim(), childInfo[1].trim());
								if(!childList.contains(childObj))
								childList.add(childObj);
							}
						}
					}
					
					String id = adserverId + "__" + adServerUserName + "__" + DFPPropertyId + "__" + companyId;
	
					PropertyObj obj = new PropertyObj();
					obj.setId(id);
					obj.setPropertyName(propertyName);
					obj.setAdServerUserName(adServerUserName);
					obj.setDFPPropertyName(DFPPropertyName);
					obj.setDFPPropertyId(DFPPropertyId);
					obj.setAdServerId(adserverId);
					obj.setChilds(childList);
					obj.setCompanyId(companyId);
					obj.setStatus(status);
	
					log.info("Going to save this property");
					userDetailsDAO.saveObject(obj);
				}
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
}

public static boolean uploadAccounts(String fileName) {
	InputStream inputStream;
	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	try {
		log.info("upload Accounts starts");
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("allElements:" + allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 8) {
				if(count==0){
					log.info("Skip first header row...");
				}
				else {
					String accountDfpId = line[0].trim();
					String accountName = line[1].trim();
					String accountType = line[2].trim();
					String adServerId = line[3].trim();
					String adServerUserName = line[4].trim();
					String companyId = line[5].trim();
					String dfpAccountName = line[6].trim();
					String status = line[7].trim();
					String id = adServerId + "__" + adServerUserName + "__" + accountDfpId + "__" + companyId;
					
					accountName = accountName.replace("("+accountType+")", "");
					accountName = accountName.trim();
					
					AccountsEntity obj = new AccountsEntity();
					obj.setId(id);
					obj.setAccountDfpId(accountDfpId);
					obj.setAccountName(accountName + " ("+accountType+")");
					obj.setAccountType(accountType);
					obj.setAdServerId(adServerId);
					obj.setAdServerUserName(adServerUserName);
					obj.setCompanyId(companyId);
					obj.setDfpAccountName(dfpAccountName);
					obj.setStatus(status);
	
					log.info("Going to save this account");
					userDetailsDAO.saveObject(obj);
				}
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
}
 
public static boolean readCSVForUserDetails(String fileName) {
	InputStream inputStream;
	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	try {
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		CSVReader csvReader = new CSVReader(reader);

		List<String[]> allElements = csvReader.readAll();
		log.info("========readCSVForUserDetails==========allElements : "+ allElements.size());
		for (String[] line : allElements) {			
			if (line.length >= 6) {
				long id = Long.valueOf(line[0].trim());
				String emailId = line[1].trim();
				String password = line[2].trim();
				String userName = line[3].trim();
				String role = line[4].trim();
				String status = line[5].trim();

				UserDetailsObj obj = new UserDetailsObj(id, emailId,
						password, userName, role, status);
				if (line.length > 6) {
					List<String> teamIDList = new ArrayList<String>();
					for(int i=6; i<line.length; i++) {
						teamIDList.add(line[i].trim());
					}
					obj.setTeams(teamIDList);
				}
				log.info("Going to save this user");
				userDetailsDAO.saveObject(obj);
				log.info("User saved successfully in datastore");
			}
		}
		csvReader.close();
 		reader.close();
		return true;
	} catch (IOException e) {
		log.severe("IOException:" + e.getMessage());
		
		return false;
	} catch (Exception e) {
		log.severe("Exception:" + e.getMessage());
		
		return false;
	}
 }
  

/*
 * @author Youdhveer Panwar
 * Read the csv file and save data into datastore
 * @return String - response
 * @param String - file
 */
 public static String readCSVAndUploadGeoTargets(String fileName) {
	String response="Uploaded";
	InputStream inputStream;
	try {
		log.info("uploadGeoTargets.."+fileName);
		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		CSVReader csvReader = new CSVReader(reader);
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
		List<String[]> allElements = csvReader.readAll();
		log.info("total lines :"+ allElements.size());
		int count = 0;
		for (String[] line : allElements) {			
			if (line.length >= 2) {	
				if(count==0){
					log.info("Skip first header row...");
				}else{
					long id = StringUtil.getLongValue((line[0]).trim());
					String geoName = line[1].trim();
					GeoTargetsObj obj=new GeoTargetsObj(id, geoName);
					
					mediaPlanDAO.saveObject(obj);
					log.info("Object saved successfully in datastore :"+ count);
				}				
			}else{
				log.warning("Invalid csv file, please provide two columns only..");
				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
			}
			count++;
		}
		csvReader.close();
 		reader.close();
		
	} catch (IOException e) {
  		log.severe("IOException:" + e.getMessage());
  		
  		response="IOException:" + e.getMessage();
  	} catch (Exception e) {
  		log.severe("Exception:" + e.getMessage());
  		response=" Exception:" + e.getMessage();
  				
  	}
  	return response;
   }

 
 /*
  * @author Youdhveer Panwar
  * Read the industry csv file and save data into datastore
  * @return String - response
  * @param String - file
  */
  public static String readCSVAndUploadIndustry(String fileName) {
 	String response="Uploaded";
 	InputStream inputStream;
 	try {
 		log.info("uploadIndustry.."+fileName);
 		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
 		BufferedReader reader = new BufferedReader(new InputStreamReader(
 				inputStream));

 		CSVReader csvReader = new CSVReader(reader);
 		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
 		List<String[]> allElements = csvReader.readAll();
 		log.info("total lines :"+ allElements.size());
 		int count = 0;
 		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");		
 		for (String[] line : allElements) {			
 			if (line.length >= 2) {	
 				if(count==0){
 					log.info("Skip first header row...");
 				}else{
 					long id = StringUtil.getLongValue((line[0]));
 					String name = line[1];
 					IndustryObj obj=new IndustryObj(id, name,"1",createdOn);
 					
 					mediaPlanDAO.saveObject(obj);
 					log.info("Object saved successfully in datastore :"+ count);
 				}				
 			}else{
 				log.warning("Invalid csv file, please provide two columns only..");
 				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
 			}
 			count++;
 		}
 		csvReader.close();
 		reader.close();
 		
 	} catch (IOException e) {
  		log.severe("IOException:" + e.getMessage());
  		
  		response="IOException:" + e.getMessage();
  	} catch (Exception e) {
  		log.severe("Exception:" + e.getMessage());
  		response=" Exception:" + e.getMessage();
  				
  	}
  	return response;
   }
  
  /*
   * @author Youdhveer Panwar
   * Read the KPIs csv file and save data into datastore
   * @return String - response
   * @param String - file
   */
   public static String readCSVAndUploadKPIs(String fileName) {
  	String response="Uploaded";
  	InputStream inputStream;
  	try {
  		log.info("uploadKPIs.."+fileName);
  		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
  		BufferedReader reader = new BufferedReader(new InputStreamReader(
  				inputStream));

  		CSVReader csvReader = new CSVReader(reader);
  		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
  		List<String[]> allElements = csvReader.readAll();
  		log.info("total lines :"+ allElements.size());
  		int count = 0;
  		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");		
  		for (String[] line : allElements) {			
  			if (line.length >= 2) {	
  				if(count==0){
  					log.info("Skip first header row...");
  				}else{
  					long id = StringUtil.getLongValue((line[0]));
  					String name = line[1];
  					KPIObj obj=new KPIObj(id, name,"1",createdOn);
  					
  					mediaPlanDAO.saveObject(obj);
  					log.info("Object saved successfully in datastore :"+ count);
  				}				
  			}else{
  				log.warning("Invalid csv file, please provide two columns only..");
  				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
  			}
  			count++;
  		}
  		csvReader.close();
 		reader.close();
  		
  	} catch (IOException e) {
  		log.severe("IOException:" + e.getMessage());
  		
  		response="IOException:" + e.getMessage();
  	} catch (Exception e) {
  		log.severe("Exception:" + e.getMessage());
  		response=" Exception:" + e.getMessage();
  				
  	}
  	return response;
   }
   
   
 /*
  * @author Youdhveer Panwar
  * Read the csv file and save data into datastore
  * @return String - response
  * @param String - file
  */
  public static String readCSVAndUploadOrderIds(String fileName) {
 	String response="Uploaded";
 	InputStream inputStream;
 	try {
 		log.info("uploadOrderIds.."+fileName);
 		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
 		BufferedReader reader = new BufferedReader(new InputStreamReader(
 				inputStream));

 		CSVReader csvReader = new CSVReader(reader);
 		IReportDAO reportDAO = new ReportDAO();
 		List<String[]> allElements = csvReader.readAll();
 		log.info("total lines :"+ allElements.size());
 		int count = 0;
 		List<String> networkCodeList=new ArrayList<String>();
 		for (String[] line : allElements) {			
 			if (line.length >= 3) {	
 				if(count==0){
 					log.info("Skip first header row...");
 				}else{
 					long orderId = StringUtil.getLongValue((line[0]));
 					String dfpNetworkCode = line[1];
 					String dfpNetworkName = line[2];
 					String id=orderId+":"+dfpNetworkCode;
 					DfpOrderIdsObj obj=new DfpOrderIdsObj(id, orderId, dfpNetworkCode, dfpNetworkName);
 					
 					reportDAO.saveObject(obj);
 					if(!networkCodeList.contains(dfpNetworkCode)){
 						networkCodeList.add(dfpNetworkCode);
 					}
 					log.info("Object saved successfully in datastore :"+ count);
 				}
 			}else{
 				log.warning("Invalid csv file, please provide two columns only..");
 				response="Invalid csv file, please provide 'OrderId,NetworkCode,NetworkName' columns respectively..error@ line:"+count;				
 			}
 			count++;
 		}
 		
 		for(String networkCode:networkCodeList){
 			MemcacheUtil.setOrderIdsInCache(null, networkCode);
 		}
 		csvReader.close();
 		reader.close();
 		
 	} catch (IOException e) {
 		log.severe("readCSVAndUploadGeoTargets:IOException:" + e.getMessage());
 		
 		response="readCSVAndUploadGeoTargets: IOException:" + e.getMessage();
 	} catch (Exception e) {
 		log.severe("readCSVAndUploadGeoTargets :Exception:" + e.getMessage());
 		response="readCSVAndUploadGeoTargets: Exception:" + e.getMessage();
 				
 	}
 	return response;
  }

  
  /*
   * @author Youdhveer Panwar
   * Read the adSize csv file and save data into datastore
   * @return String - response
   * @param String - file
   */
   public static String readCSVAndUploadAdSize(String fileName) {
  	String response="Uploaded";
  	InputStream inputStream;
  	try {
  		log.info("upload adsize..file:"+fileName);
  		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
  		BufferedReader reader = new BufferedReader(new InputStreamReader(
  				inputStream));

  		CSVReader csvReader = new CSVReader(reader);
  		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
  		List<String[]> allElements = csvReader.readAll();
  		log.info("total lines :"+ allElements.size());
  		int count = 0;
  		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");		
  		for (String[] line : allElements) {			
  			if (line.length >= 2) {	
  				if(count==0){
  					log.info("Skip first header row...");
  				}else{
  					long id = StringUtil.getLongValue((line[0]));
  					String name = line[1];
  					AdSizeObj obj=new AdSizeObj(id, name,"1",createdOn);
  					
  					mediaPlanDAO.saveObject(obj);
  					log.info("Object saved successfully in datastore :"+ count);
  				}				
  			}else{
  				log.warning("Invalid csv file, please provide two columns only..");
  				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
  			}
  			count++;
  		}
  		csvReader.close();
 		reader.close();
  		
  	} catch (IOException e) {
  		log.severe("IOException:" + e.getMessage());
  		
  		response="IOException:" + e.getMessage();
  	} catch (Exception e) {
  		log.severe("Exception:" + e.getMessage());
  		response=" Exception:" + e.getMessage();
  				
  	}
  	return response;
   }
   
   
   /*
    * @author Youdhveer Panwar
    * Read the adFormats csv file and save data into datastore
    * @return String - response
    * @param String - file
    */
    public static String readCSVAndUploadAdFormat(String fileName) {
   	String response="Uploaded";
   	InputStream inputStream;
   	try {
   		log.info("upload adFormats..file:"+fileName);
   		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
   		BufferedReader reader = new BufferedReader(new InputStreamReader(
   				inputStream));

   		CSVReader csvReader = new CSVReader(reader);
   		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
   		List<String[]> allElements = csvReader.readAll();
   		log.info("total lines :"+ allElements.size());
   		int count = 0;
   		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");		
   		for (String[] line : allElements) {			
   			if (line.length >= 2) {	
   				if(count==0){
   					log.info("Skip first header row...");
   				}else{
   					long id = StringUtil.getLongValue((line[0]));
   					String name = line[1];
   					AdFormatObj obj=new AdFormatObj(id, name,"1",createdOn);
   					
   					mediaPlanDAO.saveObject(obj);
   					log.info("Object saved successfully in datastore :"+ count);
   				}				
   			}else{
   				log.warning("Invalid csv file, please provide two columns only..");
   				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
   			}
   			count++;
   		}
   		csvReader.close();
 		reader.close();
   		
   	} catch (IOException e) {
   		log.severe("IOException:" + e.getMessage());
   		
   		response="IOException:" + e.getMessage();
   	} catch (Exception e) {
   		log.severe("Exception:" + e.getMessage());
   		response=" Exception:" + e.getMessage();
   				
   	}
   	return response;
    }
    
    /*
     * @author Naresh Pokhriyal
     * Read the campaign types csv file and save data into datastore
     * @return String - response
     * @param String - file
     */
    public static String readCSVAndUploadCampaignType(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	try {
     		log.info("upload Campaign Types.."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
     		CSVReader csvReader = new CSVReader(reader);
     		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		int count = 0;
     		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
     		for (String[] line : allElements) {			
     			if (line.length >= 2) {	
     				long objectId = Long.valueOf(line[0].trim());
 					String value = line[1];
 					DropdownDataObj obj = new DropdownDataObj(objectId, LinMobileConstants.DROP_DOWN_VALUE[0], value, "1", createdOn);
 					
 					mediaPlanDAO.saveObject(obj);
 					log.info("Object saved successfully in datastore :"+ count);				
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
      		log.severe("IOException:" + e.getMessage());
      		
      		response="IOException:" + e.getMessage();
      	} catch (Exception e) {
      		log.severe("Exception:" + e.getMessage());
      		response=" Exception:" + e.getMessage();
      				
      	}
      	return response;
    }
    
    /*
     * @author Naresh Pokhriyal
     * Read the campaign status csv file and save data into datastore
     * @return String - response
     * @param String - file
     */
    public static String readCSVAndUploadCampaignStatus(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
     	try {
     		log.info("upload Campaign Status.."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
     		CSVReader csvReader = new CSVReader(reader);

     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		int count = 0;
     		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
     		for (String[] line : allElements) {			
     			if (line.length >= 2) {	
     				long objectId = Long.valueOf(line[0].trim());
 					String value = line[1];
 					DropdownDataObj obj = new DropdownDataObj(objectId, LinMobileConstants.DROP_DOWN_VALUE[1], value, "1", createdOn);
 					
 					mediaPlanDAO.saveObject(obj);
 					log.info("Object saved successfully in datastore :"+ count);				
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
      		log.severe("IOException:" + e.getMessage());
      		
      		response="IOException:" + e.getMessage();
      	} catch (Exception e) {
      		log.severe("Exception:" + e.getMessage());
      		response=" Exception:" + e.getMessage();
      				
      	}
      	return response;
    }
    
    
    /*
     * @author Youdhveer Panwar
     * Read the adUnitData csv file and save data into datastore
     * @return String - response
     * @param String - file
     */
     public static String readCSVAndUploadAdUnitData(String fileName) {
    	String response="Uploaded";
    	InputStream inputStream;
    	try {
    		log.info("upload file:"+fileName);
    		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(
    				inputStream));

    		CSVReader csvReader = new CSVReader(reader);
    		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
    		List<String[]> allElements = csvReader.readAll();
    		log.info("total lines :"+ allElements.size());
    		int count = 0;    			
    		for (String[] line : allElements) {			
    			if (line.length >= 7) {	
    				if(count==0){
    					log.info("Skip first header row...");
    				}else{
    					
    					String adUnit1=line[0];	
    					String adUnit2=line[1];
    					String adUnitId1=line[2];	
    					String adUnitId2=line[3];
    					String adUnitCode=line[4];	
    					String publisher=line[5];
    					String publisherId=line[6];
    					String id=publisherId+"_"+adUnitId1+"_"+adUnitId2;
    					AdUnitDataObj obj=new AdUnitDataObj(adUnit1, adUnit2, adUnitId1, adUnitId2, adUnitCode, publisher, publisherId);
    					obj.setId(id);
    					
    					
    					mediaPlanDAO.saveObject(obj);
    					log.info("Object saved successfully in datastore :"+ count);
    				}				
    			}else{
    				log.warning("Invalid csv file, please provide 7 columns only..");
    				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
    			}
    			count++;
    		}
    		csvReader.close();
	 		reader.close();
    		
    	} catch (IOException e) {
    		log.severe("IOException:" + e.getMessage());
    		
    		response="IOException:" + e.getMessage();
    	} catch (Exception e) {
    		log.severe("Exception:" + e.getMessage());
    		response=" Exception:" + e.getMessage();
    				
    	}
    	return response;
     }
     
     /*
      * @author Shubham Goel
      * Read the csv file and save data into datastore
      * @return String - response
      * @param String - file
      */
      public static String readCSVAndUploadDFPSitesWithDMA(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	try {
     		log.info("DFP sites With DMA..."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(
     				inputStream));

     		CSVReader csvReader = new CSVReader(reader);
     		IReportDAO reportDAO = new ReportDAO();
     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		int count = 0;
     		for (String[] line : allElements) {			
     			if (line.length >= 3) {	
     				if(count==0){
     					log.info("Skip first header row...");
     				}else{
     					long code = StringUtil.getLongValue((line[0]));
     					String DMAId = line[1];
     					String propertyName = line[2];
     					String publisherId = line[3];
     					String DFPPropertyName = line[4];
     					String address = line[5];
     					String zipCode = line[6];
     					String state = line[7];
     					String name = line[8];
     					String publisherName = line[9];
     					String id=code+":"+propertyName+":"+publisherId;
     					DFPSitesWithDMAObj obj = new DFPSitesWithDMAObj(id, code, DMAId, propertyName, publisherId, DFPPropertyName, address, zipCode, state, name, publisherName);
     					
     					reportDAO.saveObject(obj);
     					log.info("Object saved successfully in datastore :"+ count);
     				}
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide 'code,DMAId,propertyName,publisherId,DFPPropertyName,address,zipCode,state,name'"
     						+ " columns respectively..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
     		log.severe("readCSVAndUploadGeoTargets:IOException:" + e.getMessage());
     		
     		response="readCSVAndUploadGeoTargets: IOException:" + e.getMessage();
     	} catch (Exception e) {
     		log.severe("readCSVAndUploadGeoTargets :Exception:" + e.getMessage());
     		response="readCSVAndUploadGeoTargets: Exception:" + e.getMessage();
     				
     	}
     	return response;
      }

	public static String uploadContextualCategories(String fileName) {
		String response="Uploaded";
     	InputStream inputStream;
     	try {
     		log.info("DFP sites With DMA..."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

     		CSVReader csvReader = new CSVReader(reader);

     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		long count = 0;
     		IProductDAO dao = new ProductDAO();
     		for (String[] line : allElements) {			
     			if (line.length >= 2) {	
     				if(count==0){
     					log.info("Skip first header row...");
     				}else{
     					String group = line[0];
     					String subGroup = line[1];
     					IABContextObj obj = new IABContextObj(count, group, subGroup);     					
     					dao.saveObject(obj);
     					log.info("Object saved successfully in datastore :"+ count);
     				}
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide 'group, subGroup'"
     						+ " columns respectively..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
     		log.severe("uploadContextualCategories:IOException:" + e.getMessage());
     		
     		response="uploadContextualCategories: IOException:" + e.getMessage();
     	} catch (Exception e) {
     		log.severe("uploadContextualCategories :Exception:" + e.getMessage());
     		response="uploadContextualCategories: Exception:" + e.getMessage();
     				
     	}
     	return response;
	}

    
	  public static String readCSVAndUploadUSAData(String fileName,String entityType) {
		 	String response="Uploaded";
		 	try {
		 		//log.info("upload entity.."+fileName);
		 		InputStream inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		 		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		 		CSVReader csvReader = new CSVReader(reader);

		 		List<String[]> allElements = csvReader.readAll();
		 		log.info("total lines :"+ allElements.size());
		 		int count = 0;
		 	
		 		String stateCacheKey="usa_state_code_id_key";		 		
		 		Map<String,String> stateMap=MemcacheUtil.getDataMapFromCacheByKey(stateCacheKey);
		 		if(stateMap==null || stateMap.size()==0){
		 			stateMap=new HashMap<String, String>();
		 		}else{
		 			log.info("Found state map in cache--"+stateMap.size());
		 		}
		 		
		 		IProductDAO dao = new ProductDAO();
		 		CountryObj countryObj=null;
		 		StateObj stateObj=null;
		 		CityObj cityObj=null;
		 		int logCounter=1;
		 		for (String[] line : allElements) {	
		 			if(count==0){
     					log.info("Skip first header row...");
     				}else{
     					if(line.length >=7){
    		 				String id=line[0];
    		 				String name=line[1];
    		 				String parentId=line[3];
    		 				String code=line[4];
    		 				
    		 				if(entityType !=null && entityType.equals("CountryObj")){
    		 					countryObj=new CountryObj(Long.parseLong(id), code, name);
    		 					dao.saveObject(countryObj);
    			 			}else if(entityType !=null && entityType.equals("StateObj")){
    			 				stateObj=new StateObj();
    			 				stateObj.setId(Long.parseLong(id));
    			 				stateObj.setText(name);
    			 				stateObj.setCode(code);
    			 				if(parentId !=null && parentId.trim().length()>=0){
    			 					Key<CountryObj> countryKey=Key.create(CountryObj.class, Long.parseLong(parentId));
    			 					stateObj.setCountry(countryKey);
    			 					dao.saveObject(stateObj);
    			 					stateMap.put(code, id);			 					
    			 				}else{
    			 					log.warning("Invalid state, without parentId (country id)--"+name);
    			 				}			 				
    			 			}
    	 				}else if(line.length>=5){
    	 					String zipCode=line[0];
    		 				String cityName=line[1];
    		 				String stateCode=line[2];
    		 				
    		 				if(stateMap !=null && stateMap.size()>0){	
    		 					cityObj=new CityObj();
    		 					cityObj.setId(Long.parseLong(zipCode));
    		 					cityObj.setText(cityName);
    		 					cityObj.setZip(zipCode);
    		 					String parentId=stateMap.get(stateCode);		 					
    		 					if(parentId !=null && parentId.trim().length()>=0){
    		 						Key<StateObj> stateKey=Key.create(StateObj.class, Long.parseLong(parentId));
    		 						cityObj.setState(stateKey);
    		 						dao.saveObject(cityObj);
    		 						addCityInSearchIndex(cityObj);    		 						
    		 					}else{
    			 					log.warning("Invalid zip - city, without parentId (state id)--"+cityName+" and zipCode:"+zipCode);
    			 				}		 					
    		 							 					
    		 				}else{
    		 					response="Invalid operation, please upload state data first.";
    		 					log.warning(response);
    		 					break;
    		 				}		 				
    	 				}else{		 				
    		 				response="Invalid csv file...didn't found required columns..."+count;
    		 				log.warning(response);
    		 			}
     					if(count==logCounter*500){
  		 					logCounter=logCounter+1;
  		 					log.info("Total lines (rows) read --"+count);
  		 				}
	   				}		 			
		 			count++;		 			
		 		}
		 		log.info("Total rows read.."+count);      
		 		MemcacheUtil.setDataMapInCacheByKey(stateMap, stateCacheKey);		 		
		 		log.info("Total data uploaded.."+count);
		 		csvReader.close();
		 		reader.close();
		 		
		 	} catch (IOException e) {
		 		response=response+ " : readCSVAndUploadUSAData: IOException:" + e.getMessage();
		 		log.severe(response); 			 		
		 	} catch (Exception e) {
		 		response=response+ " : readCSVAndUploadUSAData: Exception:" + e.getMessage();
		 		log.severe(response); 		 			
		 	}
		 	return response;
	 }
	  
	  public static String readCSVAndUploadCreativeObjData(String fileName) {
		 	String response="Uploaded";
		 	try {
		 		log.info("upload entity.."+fileName);
		 		InputStream inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		 		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		 		CSVReader csvReader = new CSVReader(reader);
		 		List<String[]> allElements = csvReader.readAll();
		 		log.info("total lines :"+ allElements.size());
		 		int count = 0;
		 		IProductDAO dao = new ProductDAO();
		 	    
		 		for (String[] line : allElements) {	
		 			if(count==0){
   					   log.info("Skip first header row...");
	   				}else{
	   					if(line.length >=3){
	  		 				String id=line[0];
	  		 				String format=line[1];
	  		 				String size=line[2];	  		 					  		 							
	  		 				CreativeObj creative=new CreativeObj(Long.parseLong(id), format, size);	
	  		 				dao.saveObject(creative);
	  	 				}else{		 				
	  		 				response="Invalid csv file...didn't found required columns..."+count;
	  		 				log.warning(response);
	  		 			}
	   				}		 			
		 			count++;
		 		}
		 		csvReader.close();
		 		reader.close();
		 	} catch (IOException e) {
		 		response=response+ " : readCSVAndUploadUSAData: IOException:" + e.getMessage();
		 		log.severe(response); 			 		
		 	} catch (Exception e) {
		 		response=response+ " : readCSVAndUploadUSAData: Exception:" + e.getMessage();
		 		log.severe(response); 		 			
		 	}
		 	return response;
	 }
	 
	  public static String readCSVAndUploadDeviceOrPlatformData(String fileName,String entity) {
		 	String response="Uploaded";
		 	try {
		 		log.info("upload entity.."+fileName);
		 		InputStream inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		 		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		 		CSVReader csvReader = new CSVReader(reader);
		 		List<String[]> allElements = csvReader.readAll();
		 		log.info("total lines :"+ allElements.size());
		 		int count = 0;
		 		IProductDAO dao = new ProductDAO();
		 	    PlatformObj platform=null;
		 	    DeviceObj device=null;
		 	    
		 		for (String[] line : allElements) {	
		 			if(count==0){
 					   log.info("Skip first header row...");
	   				}else{
	   					if(line.length >=2){
	  		 				String id=line[0];
	  		 				String text=line[1];
	  		 				if(entity !=null && entity.equals("DeviceObj")){
	  		 					device=new DeviceObj(Long.parseLong(id), text);
	  		 					dao.saveObject(device);
	  		 				}else if(entity !=null && entity.equals("PlatformObj")){
	  		 					platform=new PlatformObj(Long.parseLong(id), text);
	  		 					dao.saveObject(platform);
	  		 				}
	  		 				
	  	 				}else{		 				
	  		 				response="Invalid csv file...didn't found required columns..."+count;
	  		 				log.warning(response);
	  		 			}
	   				}		 			
		 			count++;
		 		}
		 		csvReader.close();
		 		reader.close();
		 	} catch (IOException e) {
		 		response=response+ " : readCSVAndUploadUSAData: IOException:" + e.getMessage();
		 		log.severe(response); 			 		
		 	} catch (Exception e) {
		 		response=response+ " : readCSVAndUploadUSAData: Exception:" + e.getMessage();
		 		log.severe(response); 		 			
		 	}
		 	return response;
	}
	  /*
	      *Readind csv file and uploading the adunithierarchy
	     * @return String - response
	     * @param String - file
	     * @param String - Add server id
	     */
	  /*Readind csv file and uploading the adunithierarchy*/
	  public static String readCSVAndUploadAdUnitHierarchy(String fileName,String adServerId) {
		 	String response="Uploaded";
		 	try {
		 		//log.info("upload entity.."+fileName);
		 		InputStream inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
		 		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		 		CSVReader csvReader = new CSVReader(reader);
		 		
		 		if(adUnitReportColsList==null){
		 			adUnitReportColsList=new ArrayList<String>();
		 			adUnitReportColsList=Arrays.asList(adUnitReportCols);
				}
		 		
		 		List<String[]> allElements = csvReader.readAll();
		 		Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
		 		
		 		log.info("total lines :"+ allElements.size());
		 		int count = 0;
		 		IProductDAO dao = new ProductDAO();
		 	    AdUnitHierarchy adUnitObj=null;
		 	    int logCounter=1;
		 	    int csvColumns=1;
		 	    boolean addData=false;
		 		for (String[] line : allElements) {	
		 			if(count==0){
					   log.info("Skip first header row...");
					   for(int i=0;i<line.length;i++){
							int index=adUnitReportColsList.indexOf(line[i]);
							if(index >=0){
								csvColumnMap.put(line[i], i);
							}else{
								log.warning("This column not found in list :"+line[i]+" : Please update column list");
							}
						}
						log.info("Found cloumns in csv:"+csvColumnMap.size());
						
	   				}else{
	   					if(line.length >=csvColumns){	   						
	   						String id="";
	   						String adUnitId="";
	   						String adUnitName="";
	   						String canonicalPath="";
	   						String parentId="";
	   						
	  		 				String adUnit1=csvColumnMap.get("Ad unit 1")==null?null:line[csvColumnMap.get("Ad unit 1")];
	  		 				String adUnit2=csvColumnMap.get("Ad unit 2")==null?null:line[csvColumnMap.get("Ad unit 2")];
	  		 				String adUnit3=csvColumnMap.get("Ad unit 3")==null?null:line[csvColumnMap.get("Ad unit 3")];
	  		 				String adUnit4=csvColumnMap.get("Ad unit 4")==null?null:line[csvColumnMap.get("Ad unit 4")];
	  		 				String adUnit5=csvColumnMap.get("Ad unit 5")==null?null:line[csvColumnMap.get("Ad unit 5")];
	  		 				String adUnitId1=csvColumnMap.get("Ad unit ID 1")==null?null:line[csvColumnMap.get("Ad unit ID 1")];
	  		 				String adUnitId2=csvColumnMap.get("Ad unit ID 2")==null?null:line[csvColumnMap.get("Ad unit ID 2")];
	  		 				String adUnitId3=csvColumnMap.get("Ad unit ID 3")==null?null:line[csvColumnMap.get("Ad unit ID 3")];
	  		 				String adUnitId4=csvColumnMap.get("Ad unit ID 4")==null?null:line[csvColumnMap.get("Ad unit ID 4")];
	  		 				String adUnitId5=csvColumnMap.get("Ad unit ID 5")==null?null:line[csvColumnMap.get("Ad unit ID 5")];
	  		 				
	  		 				if(adUnitId5 !=null &&  !(adUnitId5.equals("-0") || adUnitId5.equals("0"))){
	  		 					adUnitId=adUnitId5;
	  		 					parentId=adUnitId4;
	  		 					adUnitName=adUnit5;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4+" > "+adUnit5;
	  		 					addData=true;
	  		 				}else if(adUnitId4 !=null &&  !(adUnitId4.equals("-0") || adUnitId4.equals("0"))){
	  		 					adUnitId=adUnitId4;
	  		 					parentId=adUnitId3;
	  		 					adUnitName=adUnit4;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4;
	  		 					addData=true;
	  		 				}else if(adUnitId3 !=null &&  !(adUnitId3.equals("-0") || adUnitId3.equals("0"))){
	  		 					adUnitId=adUnitId3;
	  		 					adUnitName=adUnit3;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3;
	  		 					parentId=adUnitId2;
	  		 					addData=true;
	  		 				}else if(adUnitId2 !=null &&  !(adUnitId2.equals("-0") || adUnitId2.equals("0"))){
	  		 					adUnitId=adUnitId2;
	  		 					adUnitName=adUnit2;
	  		 					canonicalPath=adUnit1+" > "+adUnit2;
	  		 					parentId=adUnitId1;
	  		 					addData=true;
	  		 				}else if(adUnitId1 !=null &&  !(adUnitId1.equals("-0") || adUnitId1.equals("0"))){
	  		 					adUnitId=adUnitId1;
	  		 					adUnitName=adUnit1;
	  		 					canonicalPath=adUnit1;	  		 					
	  		 					addData=true;
	  		 				}else{
	  		 					log.warning("Invalid rows ..."+line+" || rowCount:"+count);
	  		 					addData=false;
	  		 				}
	  		 				if(addData){	  		 					
	  		 					id=adServerId+"_"+adUnitId;
	  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
		  		 				dao.saveObject(adUnitObj);
		  		 				TextSearchDocument.createDocument(adUnitObj);	
	  		 				}	  		 				
	  		 				
	  	 				}else{		 				
	  		 				response="Invalid csv file...didn't found required columns"+csvColumns + " on row number-"+count;
	  		 				log.warning(response);
	  		 			}
	   					if(count==logCounter*500){
  		 					logCounter=logCounter+1;
  		 					log.info("Total lines (rows) read --"+count);
  		 				}
	   				}		 			
		 			count++;		 			
		 		}
		 		log.info("Total rows read.."+count);
		 		csvReader.close();
		 		reader.close();
		 	} catch (IOException e) {
		 		response=response+ " : readCSVAndUploadUSAData: IOException:" + e.getMessage();
		 		log.severe(response); 			 		
		 	} catch (Exception e) {
		 		response=response+ " : readCSVAndUploadUSAData: Exception:" + e.getMessage();
		 		log.severe(response); 		 			
		 	}
		 	return response;
	}
	public static void addCityInSearchIndex(CityObj city){
			TextSearchDocument.createDocument(city);
			
	}
	
	/*
     * @author Shubham Goel
     * Read the Education types csv file and save data into datastore
     * @return String - response
     * @param String - file
     */
    public static String readCSVAndUploadEducationType(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	try {
     		log.info("upload Education Types.."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
     		CSVReader csvReader = new CSVReader(reader);
     		ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		int count = 0;
     		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
     		for (String[] line : allElements) {			
     			if (line.length >= 2) {	
     				long objectId = Long.valueOf(line[0].trim());
 					String value = line[1];
 					DropdownDataObj obj = new DropdownDataObj(objectId, LinMobileConstants.DROP_DOWN_VALUE[2], value, "1", createdOn);
 					
 					dao.saveObject(obj);
 					log.info("Object saved successfully in datastore :"+ count);				
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
      		log.severe("IOException:" + e.getMessage());
      		
      		response="IOException:" + e.getMessage();
      	} catch (Exception e) {
      		log.severe("Exception:" + e.getMessage());
      		response=" Exception:" + e.getMessage();
      				
      	}
      	return response;
    }
    
	/*
     * @author Shubham Goel
     * Read the Ethinicity types csv file and save data into datastore
     * @return String - response
     * @param String - file
     */
    public static String readCSVAndUploadEthinicityType(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	try {
     		log.info("upload Education Types.."+fileName);
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
     		CSVReader csvReader = new CSVReader(reader);
     		ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());
     		int count = 0;
     		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
     		for (String[] line : allElements) {			
     			if (line.length >= 2) {	
     				long objectId = Long.valueOf(line[0].trim());
 					String value = line[1];
 					DropdownDataObj obj = new DropdownDataObj(objectId, LinMobileConstants.DROP_DOWN_VALUE[3], value, "1", createdOn);
 					
 					dao.saveObject(obj);
 					log.info("Object saved successfully in datastore :"+ count);				
     			}else{
     				log.warning("Invalid csv file, please provide two columns only..");
     				response="Invalid csv file, please provide two columns only..error@ line:"+count;				
     			}
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
      		log.severe("IOException:" + e.getMessage());
      		
      		response="IOException:" + e.getMessage();
      	} catch (Exception e) {
      		log.severe("Exception:" + e.getMessage());
      		response=" Exception:" + e.getMessage();
      				
      	}
      	return response;
    }
    
    /*
     * @author Youdhveer Panwar
     * Read the MigratedCampaign data from csv file and update data into datastore
     * @return String - response
     * @param String - file
     */
    public static String readCSVAndUpdateMigratedCampaign(String fileName) {
     	String response="Uploaded";
     	InputStream inputStream;
     	int count = 0;
     	try {
     		log.info("readCSVAndUpdateMigratedCampaign..");
     		inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
     		CSVReader csvReader = new CSVReader(reader);
     		ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
     		List<String[]> allElements = csvReader.readAll();
     		log.info("total lines :"+ allElements.size());     		
     		
     		for (String[] line : allElements) {	
     			if(count ==0){
 					log.info("Reading header...");
 				}else{
 					if (line.length >= 4 ) {	
 	     				
 	     				String campaignName=line[0];
 	     				String partnerName=line[1];
 	     				String orderId=line[2];
 	     				String dfpNetworkCode=line[3];
 	     				long dfpOrderId=StringUtil.getLongValue(orderId);
 	     				
 	     				if(dfpOrderId >0 && LinMobileUtil.isNumeric(dfpNetworkCode)){
 	     					log.info("Update campaign -dfpOrderId :"+dfpOrderId);
 	     					SmartCampaignObj migratedCampaign=dao.getCampaignByDFPOrderId(dfpOrderId, dfpNetworkCode);
 	     					if(migratedCampaign !=null){
 	     						//migratedCampaign.setPublisherName(partnerName);
 	         					//migratedCampaign.setPublisherId("0");
 	         					if(campaignName !=null){
 	         						migratedCampaign.setName(campaignName);
 	         						migratedCampaign.setDfpOrderName(campaignName);
 	             					dao.saveObjectWithStrongConsistancy(migratedCampaign);
 	         					}     					
 	         					log.info("Update placements for campaign  -dfpOrderId :"+dfpOrderId);
 	         					//Key<SmartCampaignObj> campaignKey = Key.create(SmartCampaignObj.class, migratedCampaign.getCampaignId());
 	         					List<SmartCampaignPlacementObj> placements=dao.getAllPlacementOfCampaign(migratedCampaign.getCampaignId());
 	         					for(SmartCampaignPlacementObj placement:placements){
 	         						List<DFPLineItemDTO> lineItemsDTOList=placement.getDfpLineItemList();
 	         						if(lineItemsDTOList !=null && lineItemsDTOList.size()>0){
 	         							Iterator<DFPLineItemDTO> itr=lineItemsDTOList.iterator();
 	             						while(itr.hasNext()){
 	             							DFPLineItemDTO lineItemDTO=itr.next();
 	             							lineItemDTO.setPartner(partnerName); //set partner for each lineItem
 	             						}
 	             						placement.setDfpLineItemList(lineItemsDTOList);
 	         						}
 	         						log.info("Update placement in datastore --id -"+placement.getId());
 	         						dao.saveObjectWithStrongConsistancy(placement);
 	         					}
 	         					log.info("Campaign updated successfully in datastore :"+ dfpOrderId+" , row count -"+count);
 	     					}else{
 	     						log.warning("This campaign not found in datastore -- "+dfpOrderId);
 	     					}
 	     					
 	     					
 	     				}else{
 	     					log.warning("Invalid csv row --- dfpNetworkCode:"+dfpNetworkCode+" and orderId - "+orderId);
 	     				}
 	     				
 	 					
 	 									
 	     			}else{
 	     				log.warning("Invalid csv file, please provide 4 columns only..");
 	     				response="Invalid csv file, please provide 4 columns only..error@ line:"+count;				
 	     			}
 				}
     	
     			count++;
     		}
     		csvReader.close();
	 		reader.close();
     		
     	} catch (IOException e) {
      		log.severe("IOException:" + e.getMessage());
      		
      		response="IOException:" + e.getMessage();
      	} catch (Exception e) {
      		log.severe("Exception:" + e.getMessage());
      		response=" Exception:" + e.getMessage();
      				
      	}finally{
      		log.info("Row count :"+ count);
      	}
      	return response;
    }
    
}