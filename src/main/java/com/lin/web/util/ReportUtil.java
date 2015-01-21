package com.lin.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.google.api.ads.dfp.jaxws.v201403.DeviceCapability;
import com.google.api.ads.dfp.jaxws.v201403.DeviceCapabilityTargeting;
import com.google.api.ads.dfp.jaxws.v201403.TechnologyTargeting;
import com.google.appengine.api.files.LockException;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.CorePerformanceTargetReportObj;
import com.lin.server.bean.DFPTargetReportObj;
import com.lin.server.bean.LinOneRichMediaReportObj;
import com.lin.server.bean.LocationReportObj;
import com.lin.server.bean.ProductPerformanceReportObj;
import com.lin.server.bean.RichMediaCommonReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;
import com.lin.server.bean.RichMediaReportObj;
import com.lin.server.bean.RichMediaReportTraffickingObj;
import com.lin.server.bean.SellThroughReportObj;
import com.lin.web.dto.CeltraDTO;
import com.lin.web.servlet.GCSUtil;
import com.lin.web.servlet.GCStorageUtil;

/*
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 */
public class ReportUtil {

	private static final Logger log = Logger.getLogger(ReportUtil.class.getName());
	private static final int BUFFER_SIZE = 4* 1024 * 1024;

	  public static final int TARGETING_MOBILE_APP_ONLY = 1;
	  public static final int TARGETING_MOBILE_WEB_ONLY = 2;
	  public static final int TARGETING_MOBILE_APP_AND_WEB = 3;
	  public static final int TARGETING_ANY = 0;

	  
	/*
	 * Convert micro money (returned form DFP)
	 * into USD upto 6 digits
	 * 
	 * @param  - String moneyStr
	 * @return - double money (USD) up 6 digits
	 */
	public static double convertMoney(String moneyStr){
		long moneyL=Long.parseLong(moneyStr);
	    double money=(((double)moneyL/1000000));
	    //money=Math.round((money*100.0))/100.0;
		return money;
	}
	
	public static double calculateCTR(long clicks,long impressions){
		double CTR=(((double)clicks/impressions)*100);
		CTR=Math.round((CTR*100.0))/100.0;
		/*DecimalFormat decimalFormat = new DecimalFormat("##.00");
		CTR=Double.parseDouble(decimalFormat.format(CTR));*/
		return CTR;
	}
	
	public static double calculateECPM(double revenue,long impressions){
		double eCPM=((revenue/impressions)*1000.0);
		eCPM=Math.round((eCPM*100.0))/100.0;
		return eCPM;
	}
	
	public static double calculateRPM(double revenue,long requests){
		double RPM=((revenue/requests)*1000.0);
		RPM=Math.round((RPM*100.0))/100.0;
		return RPM;
	}
	
	public static double calculateFillRate(long impressions,long requests){
		double fillRate=(((double)impressions/requests)*100.0);
		fillRate=Math.round((fillRate*100.0))/100.0;
		return fillRate;
	}
	
	public static double calculateBudget(long bookedImpressionOrGoalQty,double rate){
		double budget=0;
		if(rate>0){
			budget=(((double)bookedImpressionOrGoalQty*rate)/1000.0);
		}				
		return budget;
	}
	
	public static String getDoubleStringValue(String value){
		DecimalFormat decimalFormat = new DecimalFormat("#.########");
		return decimalFormat.format(value);
	}
	
	public static String getDoubleStringValue(double value){
		DecimalFormat decimalFormat = new DecimalFormat("#.########");
		return decimalFormat.format(value);
	}
	
	/*
	 * @author Youdhveer Panwar
	 * @param CorePerformanceReportObj
	 * @return integer (0 or 1 as PASSBACK)
	 */
	public static int getPassback(CorePerformanceReportObj rootObj){
		int PASSBACK=1;
		if(rootObj.getSiteName() !=null 
				&&( rootObj.getSiteName().equalsIgnoreCase("LIN Passbacks (lin.pb)")
				   || rootObj.getSiteName().equalsIgnoreCase("lin.wlin"))
				){
			
			if(rootObj.getChannelName()!=null
				&& (!rootObj.getChannelName().equalsIgnoreCase("House"))								    
				){
				PASSBACK=0;
			}
		}
		return PASSBACK;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * @param CorePerformanceReportObj
	 * @return integer (0 or 1 as DIRECT_DELIVERED)
	 */
	public static int getDirectDelivered(CorePerformanceReportObj rootObj){
		int DIRECT_DELIVERED=0;
		
		if(rootObj.getDataSource() !=null && rootObj.getChannelName()!=null){
			if(rootObj.getDataSource().equalsIgnoreCase(LinMobileConstants.DFP_DATA_SOURCE)){
				
				if( rootObj.getChannelName().equalsIgnoreCase("Local Sales Direct")
						|| rootObj.getChannelName().equalsIgnoreCase("National Sales Direct")
						|| rootObj.getChannelName().equalsIgnoreCase("House")
				   ){
					DIRECT_DELIVERED=1;
				}
				
			}else if(rootObj.getDataSource().equalsIgnoreCase("Nexage")				
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.NEXAGE_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("Mojiva")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.MOJIVA_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("Google AdExchange")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.AD_EXCHANGE_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("Undertone")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.UNDERTONE_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("XAd")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.XAD_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("Celtra")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.CELTRA_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("Tribune")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.TRIBUNE_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}else if(rootObj.getDataSource().equalsIgnoreCase("LSN")
					&& rootObj.getChannelName().equalsIgnoreCase(LinMobileConstants.LSN_CHANNEL_NAME)){
				DIRECT_DELIVERED=1;
			}
		}
		
		return DIRECT_DELIVERED;
	}
	
	
	/*
	 * uploadReportOnCloudStorage
	 * @param : String csvDate
	 *           String fileName 
	 *           String dirName (directory under a bucket on cloud storage)
	 * @return  : UploadedFileURL
	 */
	public static String uploadReportOnCloudStorage(String csvDate,
			String fileName, String dirName)  throws IOException{
		String uploadedFileURL =null;
		InputStream input = new ByteArrayInputStream(csvDate.getBytes());
		BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
		GCStorageUtil storage = new GCStorageUtil();
		try {
			log.info("0) Going to upload file on cloud storage: fileName:"
					+ fileName+" :location dirName:"+dirName);
			storage.init(dirName, fileName, "application/CSV");

			
			if(dirName !=null){
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream,dirName);
			}else{
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream);
			}
			
			
		} catch (FileNotFoundException e) {
			log.severe(":FileNotFoundException::" + e.getMessage());
		} catch (Exception e) {
			log.severe(":Exception::" + e.getMessage());
		}finally{
			log.info("uploadedFileURL : "+uploadedFileURL);
			if(input!=null){
				input.close();	
			}
			
		}
		return uploadedFileURL;
   }
	
	
	/*
	 * uploadReportOnCloudStorage
	 * @param : String csvDate
	 *           String fileName 
	 *           String dirName (directory under a bucket on cloud storage)
	 *           String bucketName
	 * @return  : UploadedFileURL
	 */
	public static String uploadReportOnCloudStorage(String csvData,
			String fileName, String dirName,String bucketName)  throws IOException{
		
		log.info("1) Going to upload file on cloud storage: fileName:"
				+ fileName+" :location dirName:"+dirName+" and bucketName:"+bucketName);
		//New GCS Util
		String uploadedFileURL =null;
		GCSUtil gcsUtil=new GCSUtil();
		uploadedFileURL=gcsUtil.uploadFileUsingGCSClient(csvData, fileName, dirName,bucketName);
    	
		/* This is old way to upload file at cloud storage
		InputStream input = new ByteArrayInputStream(csvData.getBytes());
		BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
		GCStorageUtil storage = new GCStorageUtil();
		try {
			log.info("1) Going to upload file on cloud storage: fileName:"
					+ fileName+" :location dirName:"+dirName+" and bucketName:"+bucketName);
			storage.init(dirName, fileName, "application/CSV",bucketName);

			if(dirName !=null){
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream,dirName,bucketName);
			}else{
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream);
			}			
		} catch (FileNotFoundException e) {
			log.severe(":FileNotFoundException::" + e.getMessage());			
		} catch (Exception e) {
			log.severe(":Exception::" + e.getMessage());			
		}finally{
			log.info("uploadedFileURL : "+uploadedFileURL);
			if(input!=null){
				input.close();	
			}
		}*/
		return uploadedFileURL;
   }
	
	/*
	 * This method will read URL and split files based on 10,000 lines and upload at Google Cloud Storage 
	 * 	one by one at the same time (during reading)
	 * 
	 * @author Youdhveer Panwar
	 * 
	 */
	public static List<String> readCSVGZFileSplitAndUploadGCS(String url,String rawFileName,String dirName,String bucketName) {
	    log.info("split it if size is too big...");
		StringBuilder dataBuffer =new StringBuilder();
		List<String> dataList=new ArrayList<String>();
		String header="";
		String csvFileData="";
		
		try {
			InputStream gzipStream = new GZIPInputStream(
					(new URL(url)).openStream(),BUFFER_SIZE);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			log.info("Before reading line.....");
			BufferedReader reader = new BufferedReader(decoder);
			int i = 0;
			String line = reader.readLine();
			int count = 1;
			while (line != null) {
				if(i==0){
					header=line; // Save header for split files
				}
				
				if (i > 0) {
					dataBuffer.append('\n');
				}
				
				if (i == (count * 10000 - 1)) {	
					String tempRawFile=rawFileName.replace("_raw", "_raw_"+count);
					dataList.add(tempRawFile);
					
					if(count == 1){
						csvFileData=dataBuffer.toString();// First split file already have header
					}else{						
						csvFileData=header+"\n"+dataBuffer.toString(); // Add header for all split files except first
					}
					
					String reportPathOnStorage = uploadReportOnCloudStorage(csvFileData, tempRawFile, dirName, bucketName);
					log.info("Raw file report saved on cloud storage :" + reportPathOnStorage);
					
					dataBuffer = new StringBuilder();
					count++;
					log.info("added file data in list :"+count+" : i:" + i);
				}
				dataBuffer.append(line);
				line = reader.readLine();
				i++;
			}
			log.info("Spliting done....i:"+i);
			
			if(count > 1){				
				csvFileData=header+"\n"+dataBuffer.toString();// add header in last split file 
			}else{
				csvFileData=dataBuffer.toString();
			}			
			String tempRawFile=rawFileName.replace("_raw", "_raw_"+count);
			dataList.add(tempRawFile);
			
			String reportPathOnStorage = uploadReportOnCloudStorage(csvFileData, tempRawFile, dirName, bucketName);
			log.info(" Last raw file saved on cloud storage :" + reportPathOnStorage);
			
			log.info("Splitted dataList :" + dataList);
			reader.close();
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException:"+e.getMessage());
			e.printStackTrace();
		}

		return dataList;
	}	
	
	public static String uploadReportOnCloudStorageByURL(String url,
			String fileName, String dirName)  {
		String uploadedFileURL = null;
		
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					new URL(url).openStream());
			GCStorageUtil storage = new GCStorageUtil();
			log.info("2) Going to upload file on cloud storage: fileName:"
					+ fileName);
			storage.init(fileName, "application/CSV");

			uploadedFileURL = storage.writeFile(fileName, bufferedInputStream, dirName);
			

		} catch (FileNotFoundException e) {
			log.severe(":FileNotFoundException::" + e.getMessage());
			uploadedFileURL = null;
			
		} catch (Exception e) {
			log.severe(":Exception::" + e.getMessage());
			uploadedFileURL = null;
			
		}finally{
			log.info("uploadedFileURL : "+uploadedFileURL);			
		}
		return uploadedFileURL;
	}
	
	/*
	 * upload GZip ReportOnCloudStorage
	 * @param   : String url
	 *            String fileName 
	 *            String dirName (directory under a bucket on cloud storage)
	 * @return  : String UploadedFileURL
	 */
	public static String uploadGZipReportOnCloudStorageByURL(String url,
			String fileName, String dirName) throws IOException  {
		String uploadedFileURL = null;
		InputStream gzipStream =null;
		try {
			gzipStream = new GZIPInputStream((new URL(url)).openStream());		   
			BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipStream);
			GCStorageUtil storage = new GCStorageUtil();
			
			log.info("3) Going to upload file on cloud storage: fileName:"
					+ fileName);
			storage.init(fileName, "application/x-gzip");

			uploadedFileURL = storage.writeFile(fileName, bufferedInputStream,
					dirName);
			

		} catch (FileNotFoundException e) {
			log.severe(":FileNotFoundException::" + e.getMessage());
			uploadedFileURL = null;
			
		} catch (Exception e) {
			log.severe(":Exception::" + e.getMessage());
			uploadedFileURL = null;
			
		}finally{
			log.info("uploadedFileURL::" + uploadedFileURL);
			if(gzipStream !=null){
				gzipStream.close();
			}			
		}
		return uploadedFileURL;
	}	
	
	
	/*
	 * Create CorePerformance CSV file
	 * @param   : List<CorePerformanceReportObj> dataList(List of Core Schema objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createCorePerformanceCSVReport (List<CorePerformanceReportObj> dataList) {		
    	
		StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Id");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Name");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Type");
		  strBuffer.append(",");
		  strBuffer.append("Sales_Type");
		  strBuffer.append(",");
		  strBuffer.append("Pubisher_Id");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Advertiser");
		  strBuffer.append(",");
		  strBuffer.append("Order");
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Type");
		  strBuffer.append(",");
		  strBuffer.append("LineItem");
		  strBuffer.append(",");
		  strBuffer.append("Creative");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Size");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Type");
		  strBuffer.append(",");		  
		  strBuffer.append("Site_Id");
		  strBuffer.append(",");
		  strBuffer.append("Site_Name");
		  strBuffer.append(",");		 
		  strBuffer.append("Site_Type_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Site_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone");			 
		  strBuffer.append(",");
		  strBuffer.append("Advertiser_Id");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Id");	 
		  strBuffer.append(",");
		  strBuffer.append("Creative_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_End_Time");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_PO_Number");			 
	      strBuffer.append(",");		     
	      strBuffer.append("Agency");			 
		  strBuffer.append(",");
		  strBuffer.append("Agency_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Trafficker");			 
		  strBuffer.append(",");
		  strBuffer.append("Sales_Person");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Date");
		  strBuffer.append(",");		  
		  strBuffer.append("Cost_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Rate");			 
		  strBuffer.append(",");
		  strBuffer.append("Goal_Qty");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Priority");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Orde_Lifetime_Clicks");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Clicks");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Requests");
		  strBuffer.append(",");
		  strBuffer.append("Served");
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Impressions_CPD");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Total_Clicks");
		  strBuffer.append(",");			  
		  strBuffer.append("Clicks_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPC");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPD");			 
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Adserver_CPM_And_CPC_Revenue");			 
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPD");
		  strBuffer.append(",");		  
		  strBuffer.append("ECPM");
		  strBuffer.append(",");
		  strBuffer.append("RPM");
		  strBuffer.append(",");
		  strBuffer.append("Fill_Rate");
		  strBuffer.append(",");
		  strBuffer.append("CTR");		 
		  strBuffer.append(",");	
		  strBuffer.append("Contracted_Qty");
		  strBuffer.append(",");
		  strBuffer.append("Delivery_Indicator_Percentage");			 
		  strBuffer.append(",");		 
		  strBuffer.append("Budget");			 
		  strBuffer.append(",");
		  strBuffer.append("Spent_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Balance_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Ad_Source");
		  strBuffer.append(",");
		  strBuffer.append("Data_Source");
		  strBuffer.append(",");
		  strBuffer.append("Column1");
		  strBuffer.append(",");
		  strBuffer.append("Column2");
		  strBuffer.append(",");
		  strBuffer.append("Column3");
		  strBuffer.append(",");
		  strBuffer.append("Column4");
		  strBuffer.append(",");
		  strBuffer.append("Column5");
		  strBuffer.append(",");
		  strBuffer.append("Column6");
		  strBuffer.append(",");
		  strBuffer.append("Column7");
		  strBuffer.append(",");
		  strBuffer.append("Column8");
		  strBuffer.append(",");
		  strBuffer.append("Column9");
		  strBuffer.append(",");
		  strBuffer.append("Column10");
		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
	      for(CorePerformanceReportObj obj:dataList){
	    	  //strBuffer.append(obj.getLoadTimestamp());
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getAdvertiser());
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrder());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItem());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreative());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeSize());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeType());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getSiteId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteTypeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZoneId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZone());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdvertiserId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemId());	 
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartTime()==null?"":obj.getOrderStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartTime()==null?"":obj.getLineItemStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndTime()==null?"":obj.getOrderEndTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemEndTime()==null?"":obj.getLineitemEndTime());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getOrderPONumber());			 
		      strBuffer.append(",");		     
		      strBuffer.append(obj.getAgency());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAgencyId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getTrafficker());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesPerson());			 
			  strBuffer.append(",");			
			  strBuffer.append(obj.getDate()==null?"":obj.getDate());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getCostType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getRate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getGoalQty());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemPriority());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrdeLifetimeClicks());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeClicks());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getRequests());
			  strBuffer.append(",");
			  strBuffer.append(obj.getServed());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPC());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getImpressionsCPD());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getClicksCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPD());			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getTotalRevenue()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPM()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPC()));
			  strBuffer.append(",");			  
			  strBuffer.append(getDoubleStringValue(obj.getAdserverCPMAndCPCRevenue()));			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPD()));
			  strBuffer.append(",");
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRPM());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getFillRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getContractedQty());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDeliveryIndicator());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getBudget());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSpentLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getBalanceLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn1());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn2());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn3());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn4());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn5());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn6());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn7());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn8());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn9());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn10());	
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
	/*
	 * Create PerformanceByLocation CSV file
	 * @param   : List<LocationReportObj> dataList(List of Location objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createLocationSchemaCSVReport (List<LocationReportObj> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");		 
		  strBuffer.append("Advertiser");
		  strBuffer.append(",");
		  strBuffer.append("Order");
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Type");
		  strBuffer.append(",");
		  strBuffer.append("LineItem");
		  strBuffer.append(",");
		  strBuffer.append("Country");
		  strBuffer.append(",");
		  strBuffer.append("Region");
		  strBuffer.append(",");
		  strBuffer.append("City");
		  strBuffer.append(",");		  
		  strBuffer.append("Advertiser_ID");
		  strBuffer.append(",");
		  strBuffer.append("Order_ID");
		  strBuffer.append(",");		 
		  strBuffer.append("Line_item_ID");			 
		  strBuffer.append(",");
		  strBuffer.append("Country_ID");			 
		  strBuffer.append(",");
		  strBuffer.append("Region_ID");			 
		  strBuffer.append(",");
		  strBuffer.append("City_ID");			 
		  strBuffer.append(",");		 
		  strBuffer.append("Order_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_End_Time");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_PO_Number");			 
	      strBuffer.append(",");		     
	      strBuffer.append("Agency");			 
		  strBuffer.append(",");
		  strBuffer.append("Agency_Id");
		  strBuffer.append(",");		  
		  strBuffer.append("Date");
		  strBuffer.append(",");		  
		  strBuffer.append("Cost_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Rate");			 
		  strBuffer.append(",");
		  strBuffer.append("Goal_Qty");			 
		  strBuffer.append(",");		 
		  strBuffer.append("Order_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Orde_Lifetime_Clicks");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Clicks");			 
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");		 			 
		  strBuffer.append(",");			  
		  strBuffer.append("Total_Clicks");
		  strBuffer.append(",");			  
		  strBuffer.append("ECPM");
		  strBuffer.append(",");
		  strBuffer.append("CTR");
		  strBuffer.append(",");		  
		 /* strBuffer.append("Adserver_CPM_And_CPC_Revenue");			 
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPD");
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");*/
		  strBuffer.append("Pubisher_ID");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Data_Source");
		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");	
	      for(LocationReportObj obj:dataList){
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");			 	  
			  strBuffer.append(obj.getAdvertiser());
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrder());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItem());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCountryName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRegionName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCityName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdvertiserId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemId());	 
			  strBuffer.append(",");
			  strBuffer.append(obj.getCountryCriteriaId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRegionCriteriaId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getCityCriteriaId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartTime()==null?"":obj.getOrderStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartTime()==null?"":obj.getLineItemStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndTime()==null?"":obj.getOrderEndTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemEndTime()==null?"":obj.getLineitemEndTime());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getOrderPONumber());			 
		      strBuffer.append(",");		     
		      strBuffer.append(obj.getAgency());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAgencyId());			 
			  strBuffer.append(",");			 		
			  strBuffer.append(obj.getDate()==null?"":DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getCostType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getRate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getGoalQty());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrdeLifetimeClicks());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeClicks());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");
			  /*strBuffer.append(obj.getAdserverCPMAndCPCRevenue());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getRevenueCPD());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalRevenue());
			  strBuffer.append(",");*/
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());
			  
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}

	/*
	 * upload raw csv file on CloudStorage
	 * @param   : String file (data)
	 *            String fileName 
	 *            String dirName (directory under a bucket on cloud storage)
	 * @return  : String reportPath
	 */
	public static String uploadRawCSVReportOnCloudStorage(String file,
			String fileName, String dirName) {
		
        String reportPath=null;
		InputStream inputStream;
		StringBuffer strBuffer = new StringBuffer();
		try {
			inputStream = IOUtils.toInputStream(file, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			int count=0;
			while ((line = reader.readLine()) != null) {
				if(count !=0){
					strBuffer.append('\n');
				}
				strBuffer.append(line);
				count++;
			}			
			reader.close();
			if(strBuffer.toString() !=null && strBuffer.toString().length()>0){
				reportPath=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
			}else{
				log.warning("No data found in csv file:");
			}
			
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		
		return reportPath;
	}
	
	/*
	 * upload raw csv file on CloudStorage
	 * @param   : String file (data)
	 *            String fileName 
	 *            String dirName (directory under a bucket on cloud storage)
	 * @return  : String reportPath
	 */
	public static String uploadRawCSVReportOnCloudStorage(String file,
			String fileName, String dirName,String bucketName) {
		
        String reportPath=null;
		InputStream inputStream;
		StringBuffer strBuffer = new StringBuffer();
		try {
			inputStream = IOUtils.toInputStream(file, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			int count=0;
			while ((line = reader.readLine()) != null) {
				if(count !=0){
					strBuffer.append('\n');
				}
				strBuffer.append(line);
				count++;
			}			
			reader.close();
			if(strBuffer.toString() !=null && strBuffer.toString().length()>0){
				reportPath=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName,bucketName);
			}else{
				log.warning("No data found in csv file:");
			}
			
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		
		return reportPath;
	}
	
	/*
	 * Split raw csv file if records exceeds 10,000
	 * @param   : String file (data)
	 *            
	 * @return  : List<String> dataList
	 */
	public static List<String> splitRawCSVFile(String file) {
		
		List<String> dataList=new ArrayList<String>();;
		InputStream inputStream;
		StringBuffer dataBuffer = new StringBuffer();
		try {			
			inputStream = IOUtils.toInputStream(file, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
		
			int i = 0;
			String line = reader.readLine();
			int count = 1;
			while (line != null) {
				if (i > 0) {
					dataBuffer.append('\n');
				}
				if (i == (count * 10000 + 1)) {
					dataList.add(dataBuffer.toString());
					dataBuffer = new StringBuffer();
					count++;
				}
				dataBuffer.append(line);
				line = reader.readLine();
				i++;
			}
			if(count <=10000 && dataList.size()==0){
				dataList.add(dataBuffer.toString());
			}
			log.info("Splitted dataList size:" + dataList.size());
			reader.close();

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		
		return dataList;
	}
	
	/*
	 * Create Sell Through csv report
	 * 
	 * @param   : List<SellThroughReportObj> dataList           
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createSellThroughSchemaCSVReport (List<SellThroughReportObj> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");		 
		  strBuffer.append("Frequency");
		  strBuffer.append(",");
		  strBuffer.append("Site_Name");
		  strBuffer.append(",");
		  strBuffer.append("Site_ID");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Size");
		  strBuffer.append(",");
		  strBuffer.append("Start_Date");
		  strBuffer.append(",");
		  strBuffer.append("End_Date");
		  strBuffer.append(",");
		  strBuffer.append("Forecasted_Impressions");
		  strBuffer.append(",");		  
		  strBuffer.append("Available_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Reserved_Impressions");
		  strBuffer.append(",");		 
		  strBuffer.append("Sell_Through_Rate");
		  strBuffer.append(",");		 
		  strBuffer.append("Pubisher_ID");	
		  strBuffer.append(",");		 
		  strBuffer.append("Publisher_Name");	
		  strBuffer.append(",");		 
		  strBuffer.append("Data_Source");	
		  strBuffer.append('\n');
		  
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");		
	      for(SellThroughReportObj obj:dataList){
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");			 	  
			  strBuffer.append(obj.getFrequency());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteID());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeSize());
			  strBuffer.append(",");
			  strBuffer.append(obj.getStartDate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getEndDate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getForecastedImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAvailableImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getReservedImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSellThroughRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
/*	
	 * @ Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
	 * 
	 * This method generate authToken using Google ClientAuthentication
	 * which will be used to authenticate DFP API request.
	 * First it will search token from memcache, if not found then it will create new
	 * and put in memcache for further requests.
	 
	public static String regenerateAuthToken() {
		String clientLoginToken=regenerateClientAuthToken(
				LinMobileConstants.LIN_MEDIA_PUBLISHER_ID,
				LinMobileConstants.EMAIL_ADDRESS, 
				LinMobileConstants.EMAIL_PASSWORD);
		 String clientLoginToken=MemcacheUtil.getAuthToken();
		 if(clientLoginToken ==null){
			 ClientLoginAuth clientLoginAuth = new ClientLoginAuth(
					 LinMobileConstants.EMAIL_ADDRESS, 
					 LinMobileConstants.EMAIL_PASSWORD);
			 try {
				clientLoginToken =clientLoginAuth.getAuthToken();
				MemcacheUtil.setAuthToken(clientLoginToken);
			} catch (Exception e) {
				log.severe(" Exception in getting authToken: "+e.getMessage());
				
			}
		 }	    
	    return clientLoginToken;
	}
	
	
	
	 * @ Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
	 * 
	 * This method generate authToken using Google ClientAuthentication
	 * which will be used to authenticate DFP API request for LinDigital.
	 * First it will search token from memcache, if not found then it will create new
	 * and put in memcache for further requests.
	 
	public static String regenerateLinDigitalAuthToken() {
		String clientLoginToken=regenerateClientAuthToken(
				LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID,
				LinMobileConstants.LIN_DIGITAL_EMAIL_ADDRESS, 
				LinMobileConstants.LIN_DIGITAL_EMAIL_PASSWORD);
		
		 String clientLoginToken=MemcacheUtil.getLinDigitalAuthToken();
		 if(clientLoginToken ==null){
			 ClientLoginAuth clientLoginAuth = new ClientLoginAuth(
					 LinMobileConstants.LIN_DIGITAL_EMAIL_ADDRESS,
					 LinMobileConstants.LIN_DIGITAL_EMAIL_PASSWORD);
			 try {
				clientLoginToken =clientLoginAuth.getAuthToken();
				MemcacheUtil.setLinDigitalAuthToken(clientLoginToken);
			} catch (Exception e) {
				log.severe(" Exception in getting authToken: "+e.getMessage());
				
			}			 
		 }	    
	    return clientLoginToken;
	}
	
	
	 
 */
	
	
	/*
	 * Generate client auth token for dfp session
	 * and put in memcache
	 * 
	 * @parameter String dfpPublisher,String emailAddress,String password
	 * @return String clientLoginToken
	*/ 
	public static String regenerateClientAuthToken(String dfpPublisher,String emailAddress,String password) {
		 String key=dfpPublisher+"_client_auth_token";
		 String clientLoginToken=MemcacheUtil.getClientAuthToken(key);
		 if(clientLoginToken ==null){
			 ClientLoginAuth clientLoginAuth = new ClientLoginAuth(emailAddress,password);
			 try {
				clientLoginToken =clientLoginAuth.getAuthToken();
				MemcacheUtil.setClientAuthToken(key, clientLoginToken);
			} catch (Exception e) {
				log.severe(" Exception in getting authToken: "+e.getMessage());
				
			}
		 }	    
	    return clientLoginToken;
	}
	
	/*
	 * Create Rich Media trafficking CSV report
	 * 
	 * @author Youdhveer Panwar
	 * @param List<RichMediaReportTraffickingObj>
	 * @return StringBuffer
	 */	
	public static StringBuffer createRichMediaTraffickingCSVReport(List<RichMediaReportTraffickingObj> dataList){
		StringBuffer strBuffer = new StringBuffer();	
		
		strBuffer.append("LoadTimestamp");
		strBuffer.append(",");
		strBuffer.append("ChannelId");
		strBuffer.append(",");
		strBuffer.append("ChannelName");
		strBuffer.append(",");
		strBuffer.append("ChannelType");
		strBuffer.append(",");
		strBuffer.append("SalesType");
		strBuffer.append(",");
		strBuffer.append("PublisherId");
		strBuffer.append(",");
		strBuffer.append("PublisherName");
		strBuffer.append(",");
		strBuffer.append("DataSource");
		strBuffer.append(",");
		strBuffer.append("CampaignId");
		strBuffer.append(",");
		strBuffer.append("CampaignName");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		strBuffer.append("AccountDate");
		strBuffer.append(",");
		strBuffer.append("CreativeId");
		strBuffer.append(",");
		strBuffer.append("CreativeName");
		strBuffer.append(",");
		strBuffer.append("Format");
		strBuffer.append(",");
		strBuffer.append("PlacementId");
		strBuffer.append(",");
		strBuffer.append("PlacementName");
		strBuffer.append(",");
		strBuffer.append("Platform");
		strBuffer.append(",");
		strBuffer.append("Sdk");
		strBuffer.append(",");
		strBuffer.append("Impressions");
		strBuffer.append(",");
		strBuffer.append("ShownImpressions");
		strBuffer.append(",");
		strBuffer.append("Interactions");
		strBuffer.append(",");
		strBuffer.append("FirstClickThroughs");
		strBuffer.append(",");
		strBuffer.append("InteractionRate");
		strBuffer.append(",");
		strBuffer.append("FirstClickThroughRate");
		strBuffer.append('\n');
		
        String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(RichMediaReportTraffickingObj obj:dataList){
			strBuffer.append(loadTimestamp);			
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getCampaignId());
			strBuffer.append(",");
			strBuffer.append(obj.getCampaignName());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			strBuffer.append(obj.getAccountDate());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeName());
			strBuffer.append(",");
			strBuffer.append(obj.getFormat());
			strBuffer.append(",");
			strBuffer.append(obj.getPlacementId());
			strBuffer.append(",");
			strBuffer.append(obj.getPlacementName());
			strBuffer.append(",");
			strBuffer.append(obj.getPlatform());
			strBuffer.append(",");
			strBuffer.append(obj.getSdk());
			strBuffer.append(",");
			strBuffer.append(obj.getImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getShownImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstClickThroughs());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstClickThroughRate());
			strBuffer.append('\n');
		}
		
		return strBuffer;
	}
	
	
	/*
	 * Create Rich Media custom event CSV report
	 * 
	 * @author Youdhveer Panwar
	 * @param List<RichMediaReportTraffingObj>
	 * @return StringBuffer
	 */	
	public static StringBuffer createRichMediaCustomEventCSVReport(List<RichMediaCustomEventReportObj> dataList){
		StringBuffer strBuffer = new StringBuffer();	
		
		strBuffer.append("Load_Timestamp");
		strBuffer.append(",");
		strBuffer.append("Channel_Id");
		strBuffer.append(",");
		strBuffer.append("Channel_Name");
		strBuffer.append(",");
		strBuffer.append("Channel_Type");
		strBuffer.append(",");
		strBuffer.append("Sales_Type");
		strBuffer.append(",");
		strBuffer.append("Pubisher_Id");
		strBuffer.append(",");
		strBuffer.append("Publisher_Name");
		strBuffer.append(",");
		strBuffer.append("Data_Source");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		strBuffer.append("Site_Id");
		strBuffer.append(",");
		strBuffer.append("Site_Name");
		strBuffer.append(",");
		strBuffer.append("Site_Type");
		strBuffer.append(",");
		strBuffer.append("Zone_Id");
		strBuffer.append(",");
		strBuffer.append("Zone_Type");
		strBuffer.append(",");
		strBuffer.append("Ad_Format");
		strBuffer.append(",");
		strBuffer.append("Serving_Platform");
		strBuffer.append(",");
		strBuffer.append("Advertiser_Id");
		strBuffer.append(",");
		strBuffer.append("Advertiser");
		strBuffer.append(",");
		strBuffer.append("Order_Id");
		strBuffer.append(",");
		strBuffer.append("Order");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Id");
		strBuffer.append(",");
		strBuffer.append("Line_Item");
		strBuffer.append(",");
		strBuffer.append("Line_Item_type");
		strBuffer.append(",");
		strBuffer.append("Creative_Id");
		strBuffer.append(",");
		strBuffer.append("Creative");
		strBuffer.append(",");
		strBuffer.append("Creative_Size");
		strBuffer.append(",");
		strBuffer.append("Creative_type");
		strBuffer.append(",");
		strBuffer.append("Order_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Order_End_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_End_Date");
		strBuffer.append(",");
		strBuffer.append("Agency_Id");
		strBuffer.append(",");
		strBuffer.append("Agency");
		strBuffer.append(",");
		strBuffer.append("Goal_Quantity");
		strBuffer.append(",");
		strBuffer.append("Rate");
		strBuffer.append(",");
		strBuffer.append("Market");
		strBuffer.append(",");
		strBuffer.append("Campaign_Category");
		strBuffer.append(",");
		strBuffer.append("Custom_Event");
		strBuffer.append(",");
		strBuffer.append("Custom_Event_Type");
		strBuffer.append(",");
		strBuffer.append("Custom_Event_Id");
		strBuffer.append(",");
		strBuffer.append("Custom_Count_Value");
		strBuffer.append(",");
		strBuffer.append("Custom_Time_Value");
		strBuffer.append('\n');
		
        String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(RichMediaCustomEventReportObj obj:dataList){
			strBuffer.append(loadTimestamp);			
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			
			strBuffer.append(obj.getSiteId());
			strBuffer.append(",");
			strBuffer.append(obj.getSiteName());
			strBuffer.append(",");
			strBuffer.append(obj.getSiteType());
			strBuffer.append(",");
			strBuffer.append(obj.getZoneId());
			strBuffer.append(",");
			strBuffer.append(obj.getZoneType());
			strBuffer.append(",");
			strBuffer.append(obj.getAdFormat());
			strBuffer.append(",");
			strBuffer.append(obj.getServingPlatform());			
			strBuffer.append(",");
			
			strBuffer.append(obj.getAdvertiserId());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiser());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrder());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItem());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemType());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreative());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeSize());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeType());
			strBuffer.append(",");
			
			strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			strBuffer.append(",");			
			strBuffer.append(obj.getAgencyId());
			strBuffer.append(",");
			strBuffer.append(obj.getAgency());
			strBuffer.append(",");
			strBuffer.append(obj.getGoalQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getRate());
			strBuffer.append(",");
			strBuffer.append(obj.getMarket());
			strBuffer.append(",");		
			strBuffer.append(obj.getCampaignCategory());
			strBuffer.append(",");		
			strBuffer.append(obj.getCustomEvent());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventType());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventId());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomConversionCountValue());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomConversionTimeValue());
			
			strBuffer.append('\n');
		}
		
		return strBuffer;
	}
	
	/*
	 * Create Rich Media video campaign CSV report
	 * 
	 * @author Naresh Pokhriyal
	 * @param List<RichMediaVideoCampaignReportObj>
	 * @return StringBuffer
	 */	
	public static StringBuffer createRichMediaVideoCampaignCSVReport(List<RichMediaCommonReportObj> dataList){
		StringBuffer strBuffer = new StringBuffer();	
		
		strBuffer.append("Load_Timestamp");
		strBuffer.append(",");
		strBuffer.append("Channel_Id");
		strBuffer.append(",");
		strBuffer.append("Channel_Name");
		strBuffer.append(",");
		strBuffer.append("Channel_Type");
		strBuffer.append(",");
		strBuffer.append("Sales_Type");
		strBuffer.append(",");
		strBuffer.append("Pubisher_Id");
		strBuffer.append(",");
		strBuffer.append("Publisher_Name");
		strBuffer.append(",");
		strBuffer.append("Data_Source");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		
		strBuffer.append("Advertiser_Id");
		strBuffer.append(",");
		strBuffer.append("Advertiser");
		strBuffer.append(",");
		strBuffer.append("Order_Id");
		strBuffer.append(",");
		strBuffer.append("Order");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Id");
		strBuffer.append(",");
		strBuffer.append("Line_Item");
		strBuffer.append(",");
		strBuffer.append("Line_Item_type");
		strBuffer.append(",");
		strBuffer.append("Creative_Id");
		strBuffer.append(",");
		strBuffer.append("Creative");
		strBuffer.append(",");
		strBuffer.append("Creative_Size");
		strBuffer.append(",");
		strBuffer.append("Creative_Type");
		strBuffer.append(",");
		
		strBuffer.append("Order_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Order_End_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_End_Date");
		strBuffer.append(",");
		strBuffer.append("Agency");
		strBuffer.append(",");
		strBuffer.append("Agency_Id");
		strBuffer.append(",");
		strBuffer.append("Goal_Quantity");
		strBuffer.append(",");
		strBuffer.append("Rate");
		strBuffer.append(",");
		
		strBuffer.append("Market");
		strBuffer.append(",");
		strBuffer.append("Campaign_Category");
		strBuffer.append(",");
		
		/*strBuffer.append("AD_SERVER_IMPRESSIONS");
		strBuffer.append(",");*/
		
		/*Rich Media viewership*/
		strBuffer.append("RICH_MEDIA_BACKUP_IMAGES");
		strBuffer.append(",");
		strBuffer.append("RICH_MEDIA_DISPLAY_TIME");
		strBuffer.append(",");
		strBuffer.append("RICH_MEDIA_AVERAGE_DISPLAY_TIME");
		strBuffer.append(",");
		
		/* Rich Media interaction */
		strBuffer.append("Rich_Media_Expansions");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Expanding_Time");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Interaction_Time");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Interaction_Count");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Interaction_Rate");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Average_Interaction_Time");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Interaction_Impressions");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Manual_Closes");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_FullScreen_Impressions");
		strBuffer.append(",");
		
		/* Rich Media video metrics */
		strBuffer.append("Rich_Media_Video_Interactions");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Interaction_Rate");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Mutes");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Pauses");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Playes");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Midpoints");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Completes");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Replays");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Stops");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_Unmutes");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_View_Rate");
		strBuffer.append(",");
		strBuffer.append("Rich_Media_Video_View_Time");
		strBuffer.append(",");
		
		/* Video viewership */
		strBuffer.append("Start");
		strBuffer.append(",");
		strBuffer.append("First_Quartile");
		strBuffer.append(",");
		strBuffer.append("Midpoint");
		strBuffer.append(",");
		strBuffer.append("Third_Quartile");
		strBuffer.append(",");
		strBuffer.append("Complete");
		strBuffer.append(",");
		strBuffer.append("Average_View_Time");
		strBuffer.append(",");
		strBuffer.append("Completion_Rate");
		strBuffer.append(",");
		strBuffer.append("Error_Count");
		strBuffer.append(",");
		strBuffer.append("Video_Length");
		strBuffer.append(",");
		strBuffer.append("Video_Skip_Shown");
		strBuffer.append(",");
		strBuffer.append("Engage_View");
		strBuffer.append(",");
		strBuffer.append("View_Through_Rate");
		strBuffer.append(',');
		
		/* Video interaction */	
		strBuffer.append("Pause");
		strBuffer.append(",");
		strBuffer.append("Resume");
		strBuffer.append(",");
		strBuffer.append("Rewind");
		strBuffer.append(",");
		strBuffer.append("Mute");
		strBuffer.append(",");
		strBuffer.append("Unmute");
		strBuffer.append(",");
		strBuffer.append("Collapse");
		strBuffer.append(",");
		strBuffer.append("Expand");
		strBuffer.append(",");
		strBuffer.append("FullScreen");
		strBuffer.append(",");
		strBuffer.append("Video_Skips");
		strBuffer.append(",");
		strBuffer.append("Average_Interaction_Rate");
		strBuffer.append(",");
		strBuffer.append("Average_View_Rate");
		strBuffer.append("\n");
		
        String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(RichMediaCommonReportObj obj:dataList){
			strBuffer.append(loadTimestamp);			
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			
			strBuffer.append(obj.getAdvertiserId());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiser());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrder());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItem());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemType());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreative());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeSize());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeType());
			strBuffer.append(",");
			
			strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			strBuffer.append(",");	
			strBuffer.append(obj.getAgency());
			strBuffer.append(",");
			strBuffer.append(obj.getAgencyId());
			strBuffer.append(",");
			strBuffer.append(obj.getGoalQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getRate());
			strBuffer.append(",");
			
			strBuffer.append(obj.getMarket());
			strBuffer.append(",");		
			strBuffer.append(obj.getCampaignCategory());
			strBuffer.append(",");
			
			/*strBuffer.append(obj.getTotalImpressions());
			strBuffer.append(",");*/
			
			/*Rich Media viewership*/
			strBuffer.append(obj.getRichMediaBackupImages());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaDisplayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaAverageDisplayTime());
			strBuffer.append(",");
			
			/* Rich Media interaction */
			strBuffer.append(obj.getRichMediaExpansions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaExpandingTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionCount());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaAverageInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaManualCloses());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaFullScreenImpressions());
			strBuffer.append(",");
			
			/* Rich Media video metrics */
			strBuffer.append(obj.getRichMediaVideoInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoMutes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoPauses());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoPlayes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoMidpoints());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoCompletes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoReplays());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoStops());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoUnmutes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoViewRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoViewTime());
			strBuffer.append(",");
			
			/* Video viewership */
			strBuffer.append(obj.getStart());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstQuartile());
			strBuffer.append(",");
			strBuffer.append(obj.getMidpoint());
			strBuffer.append(",");
			strBuffer.append(obj.getThirdQuartile());
			strBuffer.append(",");
			strBuffer.append(obj.getComplete());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageViewRate());
			strBuffer.append(",");
			strBuffer.append(obj.getCompletionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getErrorCount());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoLength());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoSkipShown());
			strBuffer.append(",");
			strBuffer.append(obj.getEngageView());
			strBuffer.append(",");
			strBuffer.append(obj.getViewThroughRate());
			strBuffer.append(",");
			
			/* Video interaction */	
			strBuffer.append(obj.getPause());
			strBuffer.append(",");
			strBuffer.append(obj.getResume());
			strBuffer.append(",");
			strBuffer.append(obj.getRewind());
			strBuffer.append(",");
			strBuffer.append(obj.getMute());
			strBuffer.append(",");
			strBuffer.append(obj.getUnmute());
			strBuffer.append(",");
			strBuffer.append(obj.getCollapse());
			strBuffer.append(",");
			strBuffer.append(obj.getExpand());
			strBuffer.append(",");
			strBuffer.append(obj.getFullScreen());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoSkips());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getViewRate());
			strBuffer.append('\n');
		}
		
		return strBuffer;
	}
	
	
	/*
	 * Create LinOne Rich Media CSV report
	 * 
	 * @author Youdhveer Panwar
	 * @param List<LinOneRichMediaReportObj>
	 * @return StringBuffer
	 */	
	public static StringBuffer createLinOneRichMediaCSVReport(List<LinOneRichMediaReportObj> dataList){
		StringBuffer strBuffer = new StringBuffer();		
		
		strBuffer.append("LoadTimestamp");
		strBuffer.append(",");
		strBuffer.append("ChannelId");
		strBuffer.append(",");
		strBuffer.append("ChannelName");
		strBuffer.append(",");
		strBuffer.append("ChannelType");
		strBuffer.append(",");
		strBuffer.append("SalesType");
		strBuffer.append(",");
		strBuffer.append("PublisherId");
		strBuffer.append(",");
		strBuffer.append("PublisherName");
		strBuffer.append(",");
		strBuffer.append("DataSource");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		strBuffer.append("AdvertiserId");
		strBuffer.append(",");
		strBuffer.append("Advertiser");
		strBuffer.append(",");
		strBuffer.append("OrderId");
		strBuffer.append(",");
		strBuffer.append("Order");
		strBuffer.append(",");
		strBuffer.append("LineItemId");
		strBuffer.append(",");
		strBuffer.append("LineItem");
		strBuffer.append(",");
		strBuffer.append("LineItemType");
		strBuffer.append(",");
		strBuffer.append("CreativeId");
		strBuffer.append(",");
		strBuffer.append("Creative");
		strBuffer.append(",");
		strBuffer.append("CreativeSize");
		strBuffer.append(",");
		strBuffer.append("CreativeType");
		strBuffer.append(",");
		strBuffer.append("CreativeTypeId");
		strBuffer.append(",");
		strBuffer.append("LineItemLabels");
		strBuffer.append(",");	
		strBuffer.append("OrderStartDate");
		strBuffer.append(",");
		strBuffer.append("LineItemStartDate");
		strBuffer.append(",");
		strBuffer.append("OrderEndDate");
		strBuffer.append(",");
		strBuffer.append("LineItemEndDate");
		strBuffer.append(",");
		strBuffer.append("OrderStartTime");
		strBuffer.append(",");
		strBuffer.append("LineItemStartTime");
		strBuffer.append(",");
		strBuffer.append("OrderEndTime");
		strBuffer.append(",");
		strBuffer.append("LineItemEndTime");
		strBuffer.append(",");
		strBuffer.append("OrderPONumber");
		strBuffer.append(",");
		strBuffer.append("Agency");
		strBuffer.append(",");
		strBuffer.append("AgencyId");
		strBuffer.append(",");
		strBuffer.append("BookedCPM");
		strBuffer.append(",");
		strBuffer.append("BookedCPC");
		strBuffer.append(",");
		strBuffer.append("AdvertiserLabels");
		strBuffer.append(",");
		strBuffer.append("OrderLabels");
		strBuffer.append(",");
		strBuffer.append("GoalQuantity");
		strBuffer.append(",");	
		strBuffer.append("Trafficker");
		strBuffer.append(",");
		strBuffer.append("SecondaryTraffickers");
		strBuffer.append(",");
		strBuffer.append("SalesPerson");
		strBuffer.append(",");
		strBuffer.append("SecondarySalesPeople");
		strBuffer.append(",");
		strBuffer.append("CostType");
		strBuffer.append(",");
		strBuffer.append("Rate");
		strBuffer.append(",");
		strBuffer.append("BookedRevenueExcludeCPD");
		strBuffer.append(",");
		strBuffer.append("DeliveryPacing");
		strBuffer.append(",");	
		strBuffer.append("SponsorshipGoal");
		strBuffer.append(",");
		strBuffer.append("LineItemPriority");
		strBuffer.append(",");
		strBuffer.append("FrequencyCap");
		strBuffer.append(",");
		strBuffer.append("ContractedQuantity");
		strBuffer.append(",");
		strBuffer.append("Discount");
		strBuffer.append(",");
		strBuffer.append("OrderLifetimeImpressions");
		strBuffer.append(",");
		strBuffer.append("LineItemLifetimeImpressions");
		strBuffer.append(",");
		strBuffer.append("OrderLifetimeClicks");
		strBuffer.append(",");
		strBuffer.append("LineItemLifetimeClicks");
		strBuffer.append(",");
		strBuffer.append("ClickThroughURL");
		strBuffer.append(",");
		strBuffer.append("CreativeStartDate");
		strBuffer.append(",");
		strBuffer.append("CreativeEndDate");
		strBuffer.append(",");	
		strBuffer.append("RichMediaVideoCompletes");
		strBuffer.append(",");
		strBuffer.append("RichMediaDisplayTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaExpansions");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoViewRate");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoMidPoints");
		strBuffer.append(",");
		strBuffer.append("RichMediaInteractionImpressions");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoPauses");
		strBuffer.append(",");
		strBuffer.append("RichMediaCustomConversionCountValue");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoReplays");
		strBuffer.append(",");
		strBuffer.append("RichMediaExpandingTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaManualCloses");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoUnMutes");
		strBuffer.append(",");
		strBuffer.append("RichMediaAverageInteractionTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaBackupImages");
		strBuffer.append(",");
		strBuffer.append("RichMediaInteractionCount");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoInteractionRate");
		strBuffer.append(",");
		strBuffer.append("RichMediaInteractionTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoViewTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoInteraction");
		strBuffer.append(",");
		strBuffer.append("RichMediaAverageDisplayTime");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoStops");
		strBuffer.append(",");
		strBuffer.append("RichMediaFullScreenImpressions");
		strBuffer.append(",");
		strBuffer.append("RichMediaInteractionRate");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoMutes");
		strBuffer.append(",");
		strBuffer.append("RichMediaCustomConversionTimeValue");
		strBuffer.append(",");
		strBuffer.append("RichMediaVideoPlays");
		strBuffer.append(",");
		strBuffer.append("CustomEvent");
		strBuffer.append(",");
		strBuffer.append("CustomEventType");
		strBuffer.append(",");
		strBuffer.append("CustomEventId");
		strBuffer.append(",");		
		strBuffer.append("TotalInteractions");
		strBuffer.append('\n');
		
        String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(LinOneRichMediaReportObj obj:dataList){
			strBuffer.append(loadTimestamp);			
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiserId());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiser());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrder());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItem());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemType());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreative());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeSize());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeType());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeTypeId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLabels());
			strBuffer.append(",");	
			strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderStartTime()==null?"":obj.getOrderStartTime());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartTime()==null?"":obj.getLineItemStartTime());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndTime()==null?"":obj.getOrderEndTime());			 
		    strBuffer.append(",");
		    strBuffer.append(obj.getLineItemEndTime()==null?"":obj.getLineItemEndTime());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderPONumber());
			strBuffer.append(",");
			strBuffer.append(obj.getAgency());
			strBuffer.append(",");
			strBuffer.append(obj.getAgencyId());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedCPM());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedCPC());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiserLabels());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLabels());
			strBuffer.append(",");
			strBuffer.append(obj.getGoalQuantity());
			strBuffer.append(",");	
			strBuffer.append(obj.getTrafficker());
			strBuffer.append(",");
			strBuffer.append(obj.getSecondaryTraffickers());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesPerson());
			strBuffer.append(",");
			strBuffer.append(obj.getSecondarySalesPeople());
			strBuffer.append(",");
			strBuffer.append(obj.getCostType());
			strBuffer.append(",");
			strBuffer.append(obj.getRate());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedRevenueExcludeCPD());
			strBuffer.append(",");
			strBuffer.append(obj.getDeliveryPacing());
			strBuffer.append(",");	
			strBuffer.append(obj.getSponsorshipGoal());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemPriority());
			strBuffer.append(",");
			strBuffer.append(obj.getFrequencyCap());
			strBuffer.append(",");
			strBuffer.append(obj.getContractedQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getDiscount());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLifetimeImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLifetimeImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLifetimeClicks());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLifetimeClicks());
			strBuffer.append(",");
			strBuffer.append(obj.getClickThroughURL());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeStartDate()==null?"":obj.getCreativeStartDate());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeEndDate()==null ? "":obj.getCreativeEndDate());
			strBuffer.append(",");	
			strBuffer.append(obj.getRichMediaVideoCompletes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaDisplayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaExpansions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoViewRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoMidPoints());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoPauses());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaCustomConversionCountValue());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoReplays());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaExpandingTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaManualCloses());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoUnMutes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaAverageInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaBackupImages());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionCount());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoViewTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoInteraction());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaAverageDisplayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoStops());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaFullScreenImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoMutes());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaCustomConversionTimeValue());
			strBuffer.append(",");
			strBuffer.append(obj.getRichMediaVideoPlays());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEvent());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventType());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventId());
			strBuffer.append(",");	
			strBuffer.append(obj.getTotalInteractions());
			
			strBuffer.append('\n');
		}
		
		return strBuffer;
	}
	
	
	/*
	 * Create Golfsmith 'CommonRichMediaSchema' CSV report
	 * 
	 * @author Youdhveer Panwar
	 * @param List<RichMediaCommonReportObj>
	 * @return StringBuffer
	 */	
	public static StringBuffer createGolfsmithCommonRichMediaCSVReport(List<RichMediaCommonReportObj> dataList){
		StringBuffer strBuffer = new StringBuffer();		
		
		strBuffer.append("Load_Timestamp");
		strBuffer.append(",");
		strBuffer.append("Channel_Id");
		strBuffer.append(",");
		strBuffer.append("Channel_Name");
		strBuffer.append(",");
		strBuffer.append("Channel_Type");
		strBuffer.append(",");
		strBuffer.append("Sales_Type");
		strBuffer.append(",");
		strBuffer.append("Pubisher_Id");
		strBuffer.append(",");
		strBuffer.append("Publisher_Name");
		strBuffer.append(",");
		strBuffer.append("Data_Source");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		strBuffer.append("Site_Id");
		strBuffer.append(",");
		strBuffer.append("Site_Name");
		strBuffer.append(",");
		strBuffer.append("Site_Type");
		strBuffer.append(",");
		strBuffer.append("Zone_Id");
		strBuffer.append(",");
		strBuffer.append("Zone_Type");
		strBuffer.append(",");
		strBuffer.append("Ad_Format");
		strBuffer.append(",");
		strBuffer.append("Serving_Platform");
		strBuffer.append(",");
		strBuffer.append("Advertiser_Id");
		strBuffer.append(",");
		strBuffer.append("Advertiser");
		strBuffer.append(",");
		strBuffer.append("Order_Id");
		strBuffer.append(",");
		strBuffer.append("Order");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Id");
		strBuffer.append(",");
		strBuffer.append("Line_Item");
		strBuffer.append(",");
		strBuffer.append("Line_Item_type");
		strBuffer.append(",");
		strBuffer.append("Creative_Id");
		strBuffer.append(",");
		strBuffer.append("Creative");
		strBuffer.append(",");
		strBuffer.append("Creative_Size");
		strBuffer.append(",");
		strBuffer.append("Creative_type");
		strBuffer.append(",");
		strBuffer.append("Order_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_Start_Date");
		strBuffer.append(",");
		strBuffer.append("Order_End_Date");
		strBuffer.append(",");
		strBuffer.append("Line_Item_End_Date");
		strBuffer.append(",");
		strBuffer.append("Agency_Id");
		strBuffer.append(",");
		strBuffer.append("Agency");
		strBuffer.append(",");
		strBuffer.append("Goal_Quantity");
		strBuffer.append(",");
		strBuffer.append("Rate");
		strBuffer.append(",");
		strBuffer.append("Total_Clicks");
		strBuffer.append(",");
		strBuffer.append("CTR");
		strBuffer.append(",");
		strBuffer.append("Total_Impressions");
		strBuffer.append(",");
		strBuffer.append("Shown_Impressions");
		strBuffer.append(",");
		strBuffer.append("Total_Interactions");
		strBuffer.append(",");
		strBuffer.append("Display_Time");
		strBuffer.append(",");
		strBuffer.append("Expansions");
		strBuffer.append(",");
		strBuffer.append("Interaction_Time");
		strBuffer.append(",");
		strBuffer.append("Interaction_Impressions");
		strBuffer.append(",");
		strBuffer.append("Average_Display_Time");
		strBuffer.append(",");
		strBuffer.append("Full_Screen_Impressions");
		strBuffer.append(",");
		strBuffer.append("Expanding_Time");
		strBuffer.append(",");
		strBuffer.append("Manual_Closes");
		strBuffer.append(",");
		strBuffer.append("Average_Interaction_Time");
		strBuffer.append(",");
		strBuffer.append("Interaction_Rate");
		strBuffer.append(",");
		strBuffer.append("Backup_Images");
		strBuffer.append('\n');
		
        String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(RichMediaCommonReportObj obj:dataList){
			strBuffer.append(loadTimestamp);			
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			
			strBuffer.append(obj.getSiteId());
			strBuffer.append(",");
			strBuffer.append(obj.getSiteName());
			strBuffer.append(",");
			strBuffer.append(obj.getSiteType());
			strBuffer.append(",");
			strBuffer.append(obj.getZoneId());
			strBuffer.append(",");
			strBuffer.append(obj.getZoneType());
			strBuffer.append(",");
			strBuffer.append(obj.getAdFormat());
			strBuffer.append(",");
			strBuffer.append(obj.getServingPlatform());			
			strBuffer.append(",");
			
			strBuffer.append(obj.getAdvertiserId());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiser());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrder());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItem());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemType());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreative());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeSize());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeType());
			strBuffer.append(",");
			
			strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			strBuffer.append(",");			
			strBuffer.append(obj.getAgencyId());
			strBuffer.append(",");
			strBuffer.append(obj.getAgency());
			strBuffer.append(",");
			strBuffer.append(obj.getGoalQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getRate());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalClicks());
			strBuffer.append(",");
			strBuffer.append(obj.getCTR());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getShownImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getDisplayTime());
			strBuffer.append(",");			
			strBuffer.append(obj.getExpansions());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractionTime());
			strBuffer.append(",");			
			strBuffer.append(obj.getInteractionImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageDisplayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getFullScreenImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getExpandingTime());
			strBuffer.append(",");
			strBuffer.append(obj.getManualCloses());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getBackupImages());
			
			strBuffer.append('\n');
		}		
		return strBuffer;
	}
		
	
	/*
	 * Create Rich Media CSV report
	 * 
	 * @author Youdhveer Panwar
	 * @param
	 * @return StringBuffer
	 */
	
	public static StringBuffer createRichMediaCSVReport(List<RichMediaReportObj> dataList){
		StringBuffer strBuffer = new StringBuffer();	
			
		strBuffer.append("LoadTimestamp");
		strBuffer.append(",");
		strBuffer.append("ChannelId");
		strBuffer.append(",");
		strBuffer.append("ChannelName");
		strBuffer.append(",");
		strBuffer.append("ChannelType");
		strBuffer.append(",");
		strBuffer.append("SalesType");
		strBuffer.append(",");
		strBuffer.append("PubisherId");
		strBuffer.append(",");
		strBuffer.append("PublisherName");
		strBuffer.append(",");
		strBuffer.append("DataSource");
		strBuffer.append(",");
		strBuffer.append("Date");
		strBuffer.append(",");
		strBuffer.append("Advertiser");
		strBuffer.append(",");
		strBuffer.append("Order");
		strBuffer.append(",");
		strBuffer.append("LineItemType");
		strBuffer.append(",");
		strBuffer.append("LineItem");
		strBuffer.append(",");
		strBuffer.append("Creative");
		strBuffer.append(",");
		strBuffer.append("CreativeSize");
		strBuffer.append(",");
		strBuffer.append("CreativeType");
		strBuffer.append(",");
		strBuffer.append("CustomEvent");
		strBuffer.append(",");
		strBuffer.append("CustomEventType");
		strBuffer.append(",");
		strBuffer.append("CustomEventCount");
		strBuffer.append(",");
		strBuffer.append("CustomEventTime");
		strBuffer.append(",");
		strBuffer.append("AdvertiserId");
		strBuffer.append(",");
		strBuffer.append("OrderId");
		strBuffer.append(",");
		strBuffer.append("LineItemId");
		strBuffer.append(",");
		strBuffer.append("CreativeId");
		strBuffer.append(",");
		strBuffer.append("CustomEventId");
		strBuffer.append(",");
		strBuffer.append("OrderStartDate");
		strBuffer.append(",");
		strBuffer.append("LineItemStartDate");
		strBuffer.append(",");
		strBuffer.append("OrderEndDate");
		strBuffer.append(",");
		strBuffer.append("LineItemEndDate");
		strBuffer.append(",");
		strBuffer.append("OrderStartTime");
		strBuffer.append(",");
		strBuffer.append("LineItemStartTime");
		strBuffer.append(",");
		strBuffer.append("OrderEndTime");
		strBuffer.append(",");
		strBuffer.append("LineItemEndTime");
		strBuffer.append(",");
		strBuffer.append("OrderPONumber");
		strBuffer.append(",");
		strBuffer.append("Agency");
		strBuffer.append(",");
		strBuffer.append("AgencyId");
		strBuffer.append(",");
		strBuffer.append("BookedCPM");
		strBuffer.append(",");
		strBuffer.append("BookedCPC");
		strBuffer.append(",");
		strBuffer.append("AdvertiserLabels");
		strBuffer.append(",");
		strBuffer.append("OrderLabels");
		strBuffer.append(",");
		strBuffer.append("LineItemLabels");
		strBuffer.append(",");
		strBuffer.append("Trafficker");
		strBuffer.append(",");
		strBuffer.append("SecondaryTraffickers");
		strBuffer.append(",");
		strBuffer.append("SalesPerson");
		strBuffer.append(",");
		strBuffer.append("SecondarySalesPeople");
		strBuffer.append(",");
		strBuffer.append("CostType");
		strBuffer.append(",");
		strBuffer.append("Rate");
		strBuffer.append(",");
		strBuffer.append("BookedRevenueExcludeCPD");
		strBuffer.append(",");
		strBuffer.append("DeliveryPacing");
		strBuffer.append(",");
		strBuffer.append("GoalQuantity");
		strBuffer.append(",");
		strBuffer.append("SponsorshipGoal");
		strBuffer.append(",");
		strBuffer.append("LineItemPriority");
		strBuffer.append(",");
		strBuffer.append("FrequencyCap");
		strBuffer.append(",");
		strBuffer.append("ContractedQuantity");
		strBuffer.append(",");
		strBuffer.append("Discount");
		strBuffer.append(",");
		strBuffer.append("OrderLifetimeImpressions");
		strBuffer.append(",");
		strBuffer.append("LineItemLifetimeImpressions");
		strBuffer.append(",");
		strBuffer.append("OrderLifetimeClicks");
		strBuffer.append(",");
		strBuffer.append("LineItemLifetimeClicks");
		strBuffer.append(",");
		strBuffer.append("ClickThroughURL");
		strBuffer.append(",");
		strBuffer.append("CreativeStartDate");
		strBuffer.append(",");
		strBuffer.append("CreativeEndDate");
		strBuffer.append(",");
		strBuffer.append("BackupImageImpressions");
		strBuffer.append(",");
		strBuffer.append("TotalDisplayTime");
		strBuffer.append(",");
		strBuffer.append("TotalExpansions");
		strBuffer.append(",");
		strBuffer.append("AverageExpandingTime");
		strBuffer.append(",");
		strBuffer.append("InteractionTime");
		strBuffer.append(",");
		strBuffer.append("totalInteractions");
		strBuffer.append(",");
		strBuffer.append("AverageInteractionTime");
		strBuffer.append(",");
		strBuffer.append("InteractiveImpressions");
		strBuffer.append(",");
		strBuffer.append("ManualCloses");
		strBuffer.append(",");
		strBuffer.append("FullScreenImpressions");
		strBuffer.append(",");
		strBuffer.append("TotalVideoInteractions");
		strBuffer.append(",");
		strBuffer.append("VideoInteractionRate");
		strBuffer.append(",");
		strBuffer.append("Mute");
		strBuffer.append(",");
		strBuffer.append("Pause");
		strBuffer.append(",");
		strBuffer.append("Plays");
		strBuffer.append(",");
		strBuffer.append("MidPoint");
		strBuffer.append(",");
		strBuffer.append("Complete");
		strBuffer.append(",");
		strBuffer.append("Replays");
		strBuffer.append(",");
		strBuffer.append("Stops");
		strBuffer.append(",");
		strBuffer.append("UnMute");
		strBuffer.append(",");
		strBuffer.append("AverageViewTime");
		strBuffer.append(",");
		strBuffer.append("ViewRate");
		strBuffer.append(",");
		strBuffer.append("CampaignId");
		strBuffer.append(",");
		strBuffer.append("CampaignName");
		strBuffer.append(",");	
		strBuffer.append("CreativeName");
		strBuffer.append(",");
		strBuffer.append("Format");
		strBuffer.append(",");	
		strBuffer.append("ShownImpressions");
		strBuffer.append(",");
		strBuffer.append("FirstInteractions");
		strBuffer.append(",");
		strBuffer.append("TotalPageViews");
		strBuffer.append(",");
		strBuffer.append("CouponBtnClicked");
		strBuffer.append(",");
		strBuffer.append("BannerPageViews");
		strBuffer.append(",");
		strBuffer.append("ExpandedPageViews");
		strBuffer.append(",");
		strBuffer.append("OrientationCheck");
		strBuffer.append(",");
		strBuffer.append("PStart");
		strBuffer.append(",");
		strBuffer.append("PStartOrCouponBtnClicked");
		strBuffer.append(",");
		strBuffer.append("AdExpansions");
		strBuffer.append(",");
		strBuffer.append("AdExpansionRate");
		strBuffer.append(",");
		strBuffer.append("AdEngagementRate");
		strBuffer.append(",");
		strBuffer.append("AdEngagements");
		strBuffer.append(",");
		strBuffer.append("TimeOnAdUnit");
		strBuffer.append(",");
		strBuffer.append("ClickThroughs");
		strBuffer.append(",");
		strBuffer.append("FeatureEngagements");
		strBuffer.append(",");
		strBuffer.append("FeatureEngagementRate");
		strBuffer.append(",");	
		strBuffer.append("VideoPlayRate");
		strBuffer.append(",");
		strBuffer.append("VideoStarts");
		strBuffer.append(",");
		strBuffer.append("VideoCompletions");
		strBuffer.append(",");
		strBuffer.append("VideoCompletionRate");
		strBuffer.append(",");
		strBuffer.append("VideoPlayTime");
		strBuffer.append(",");
		strBuffer.append("TotalVideoPlayTime");
		strBuffer.append(",");
		strBuffer.append("AverageVideoPlayTime");
		strBuffer.append(",");
		strBuffer.append("VideoAttentionSpan");
		strBuffer.append(",");
		strBuffer.append("Impressions");
		strBuffer.append(",");
		strBuffer.append("PlacementId");
		strBuffer.append(",");
		strBuffer.append("PlacementName");
		strBuffer.append(",");
		strBuffer.append("Platform");
		strBuffer.append(",");
		strBuffer.append("Sdk");
		strBuffer.append(",");
		strBuffer.append("UnitName");
		strBuffer.append(",");
		strBuffer.append("ScreenLocalId");
		strBuffer.append(",");
		strBuffer.append("ScreenTitle");
		strBuffer.append(",");
		strBuffer.append("ScreenViews");
		strBuffer.append(",");
		strBuffer.append("UniqueScreenViews");
		strBuffer.append(",");
		strBuffer.append("TimeOnScreen");
		strBuffer.append(",");
		strBuffer.append("Label");
		strBuffer.append(",");
		strBuffer.append("CustomEventOccurs");
		strBuffer.append(",");
		strBuffer.append("UrlOpens");
		strBuffer.append(",");
		strBuffer.append("StoreOpens");
		strBuffer.append(",");
		strBuffer.append("VideoPlays");
		strBuffer.append(",");
		strBuffer.append("PhoneCalls");
		strBuffer.append(",");
		strBuffer.append("FacebookShareAttempts");
		strBuffer.append(",");
		strBuffer.append("TwitterProfileOpens");
		strBuffer.append(",");
		strBuffer.append("TweetPageOpens");
		strBuffer.append(",");
		strBuffer.append("FormSubmissionAttempts");
		strBuffer.append(",");
		strBuffer.append("FormSubmissionSuccesses");
		strBuffer.append(",");
		strBuffer.append("PinterestPinAttempts");
		strBuffer.append(",");
		strBuffer.append("Sessions");
		strBuffer.append(",");
		strBuffer.append("CreativeViews");
		strBuffer.append(",");
		strBuffer.append("Interactions");
		strBuffer.append(",");
		strBuffer.append("FirstClickThroughs");
		strBuffer.append(",");
		strBuffer.append("InteractionRate");
		strBuffer.append(",");
		strBuffer.append("FirstClickThroughRate");
		strBuffer.append(",");
		strBuffer.append("Column1");
		strBuffer.append(",");
		strBuffer.append("Column2");
		strBuffer.append(",");
		strBuffer.append("Column3");
		strBuffer.append(",");
		strBuffer.append("Column4");
		strBuffer.append(",");
		strBuffer.append("Column5");
		strBuffer.append(",");
		strBuffer.append("Column6");
		strBuffer.append(",");
		strBuffer.append("Column7");
		strBuffer.append(",");
		strBuffer.append("Column8");
		strBuffer.append(",");
		strBuffer.append("Column9");
		strBuffer.append(",");
		strBuffer.append("Column10");
		strBuffer.append('\n');
		
		String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		
		for(RichMediaReportObj obj:dataList){
			strBuffer.append(loadTimestamp);
			strBuffer.append(",");
			strBuffer.append(obj.getChannelId());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelName());
			strBuffer.append(",");
			strBuffer.append(obj.getChannelType());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesType());
			strBuffer.append(",");
			strBuffer.append(obj.getPubisherId());
			strBuffer.append(",");
			strBuffer.append(obj.getPublisherName());
			strBuffer.append(",");
			strBuffer.append(obj.getDataSource());
			strBuffer.append(",");
			strBuffer.append(obj.getDate());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiser());
			strBuffer.append(",");
			strBuffer.append(obj.getOrder());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemType());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItem());
			strBuffer.append(",");
			strBuffer.append(obj.getCreative());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeSize());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeType());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEvent());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventType());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventCount());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventTime());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiserId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderId());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemId());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeId());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventId());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderStartDate());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartDate());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndDate());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndDate());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderStartTime());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemStartTime());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderEndTime());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemEndTime());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderPONumber());
			strBuffer.append(",");
			strBuffer.append(obj.getAgency());
			strBuffer.append(",");
			strBuffer.append(obj.getAgencyId());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedCPM());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedCPC());
			strBuffer.append(",");
			strBuffer.append(obj.getAdvertiserLabels());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLabels());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLabels());
			strBuffer.append(",");
			strBuffer.append(obj.getTrafficker());
			strBuffer.append(",");
			strBuffer.append(obj.getSecondaryTraffickers());
			strBuffer.append(",");
			strBuffer.append(obj.getSalesPerson());
			strBuffer.append(",");
			strBuffer.append(obj.getSecondarySalesPeople());
			strBuffer.append(",");
			strBuffer.append(obj.getCostType());
			strBuffer.append(",");
			strBuffer.append(obj.getRate());
			strBuffer.append(",");
			strBuffer.append(obj.getBookedRevenueExcludeCPD());
			strBuffer.append(",");
			strBuffer.append(obj.getDeliveryPacing());
			strBuffer.append(",");
			strBuffer.append(obj.getGoalQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getSponsorshipGoal());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemPriority());
			strBuffer.append(",");
			strBuffer.append(obj.getFrequencyCap());
			strBuffer.append(",");
			strBuffer.append(obj.getContractedQuantity());
			strBuffer.append(",");
			strBuffer.append(obj.getDiscount());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLifetimeImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLifetimeImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getOrderLifetimeClicks());
			strBuffer.append(",");
			strBuffer.append(obj.getLineItemLifetimeClicks());
			strBuffer.append(",");
			strBuffer.append(obj.getClickThroughURL());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeStartDate());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeEndDate());
			strBuffer.append(",");
			strBuffer.append(obj.getBackupImageImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalDisplayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalExpansions());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageExpandingTime());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageInteractionTime());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractiveImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getManualCloses());
			strBuffer.append(",");
			strBuffer.append(obj.getFullScreenImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalVideoInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getMute());
			strBuffer.append(",");
			strBuffer.append(obj.getPause());
			strBuffer.append(",");
			strBuffer.append(obj.getPlays());
			strBuffer.append(",");
			strBuffer.append(obj.getMidPoint());
			strBuffer.append(",");
			strBuffer.append(obj.getComplete());
			strBuffer.append(",");
			strBuffer.append(obj.getReplays());
			strBuffer.append(",");
			strBuffer.append(obj.getStops());
			strBuffer.append(",");
			strBuffer.append(obj.getUnMute());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageViewTime());
			strBuffer.append(",");
			strBuffer.append(obj.getViewRate());
			strBuffer.append(",");
			strBuffer.append(obj.getCampaignId());
			strBuffer.append(",");
			strBuffer.append(obj.getCampaignName());
			strBuffer.append(",");	
			strBuffer.append(obj.getCreativeName());
			strBuffer.append(",");
			strBuffer.append(obj.getFormat());
			strBuffer.append(",");	
			strBuffer.append(obj.getShownImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalPageViews());
			strBuffer.append(",");
			strBuffer.append(obj.getCouponBtnClicked());
			strBuffer.append(",");
			strBuffer.append(obj.getBannerPageViews());
			strBuffer.append(",");
			strBuffer.append(obj.getExpandedPageViews());
			strBuffer.append(",");
			strBuffer.append(obj.getOrientationCheck());
			strBuffer.append(",");
			strBuffer.append(obj.getPStart());
			strBuffer.append(",");
			strBuffer.append(obj.getPStartOrCouponBtnClicked());
			strBuffer.append(",");
			strBuffer.append(obj.getAdExpansions());
			strBuffer.append(",");
			strBuffer.append(obj.getAdExpansionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getAdEngagementRate());
			strBuffer.append(",");
			strBuffer.append(obj.getAdEngagements());
			strBuffer.append(",");
			strBuffer.append(obj.getTimeOnAdUnit());
			strBuffer.append(",");
			strBuffer.append(obj.getClickThroughs());
			strBuffer.append(",");
			strBuffer.append(obj.getFeatureEngagements());
			strBuffer.append(",");
			strBuffer.append(obj.getFeatureEngagementRate());
			strBuffer.append(",");	
			strBuffer.append(obj.getVideoPlayRate());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoStarts());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoCompletions());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoCompletionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoPlayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getTotalVideoPlayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getAverageVideoPlayTime());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoAttentionSpan());
			strBuffer.append(",");
			strBuffer.append(obj.getImpressions());
			strBuffer.append(",");
			strBuffer.append(obj.getPlacementId());
			strBuffer.append(",");
			strBuffer.append(obj.getPlacementName());
			strBuffer.append(",");
			strBuffer.append(obj.getPlatform());
			strBuffer.append(",");
			strBuffer.append(obj.getSdk());
			strBuffer.append(",");
			strBuffer.append(obj.getUnitName());
			strBuffer.append(",");
			strBuffer.append(obj.getScreenLocalId());
			strBuffer.append(",");
			strBuffer.append(obj.getScreenTitle());
			strBuffer.append(",");
			strBuffer.append(obj.getScreenViews());
			strBuffer.append(",");
			strBuffer.append(obj.getUniqueScreenViews());
			strBuffer.append(",");
			strBuffer.append(obj.getTimeOnScreen());
			strBuffer.append(",");
			strBuffer.append(obj.getLabel());
			strBuffer.append(",");
			strBuffer.append(obj.getCustomEventOccurs());
			strBuffer.append(",");
			strBuffer.append(obj.getUrlOpens());
			strBuffer.append(",");
			strBuffer.append(obj.getStoreOpens());
			strBuffer.append(",");
			strBuffer.append(obj.getVideoPlays());
			strBuffer.append(",");
			strBuffer.append(obj.getPhoneCalls());
			strBuffer.append(",");
			strBuffer.append(obj.getFacebookShareAttempts());
			strBuffer.append(",");
			strBuffer.append(obj.getTwitterProfileOpens());
			strBuffer.append(",");
			strBuffer.append(obj.getTweetPageOpens());
			strBuffer.append(",");
			strBuffer.append(obj.getFormSubmissionAttempts());
			strBuffer.append(",");
			strBuffer.append(obj.getFormSubmissionSuccesses());
			strBuffer.append(",");
			strBuffer.append(obj.getPinterestPinAttempts());
			strBuffer.append(",");
			strBuffer.append(obj.getSessions());
			strBuffer.append(",");
			strBuffer.append(obj.getCreativeViews());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractions());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstClickThroughs());
			strBuffer.append(",");
			strBuffer.append(obj.getInteractionRate());
			strBuffer.append(",");
			strBuffer.append(obj.getFirstClickThroughRate());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn1());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn2());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn3());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn4());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn5());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn6());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn7());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn8());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn9());
			strBuffer.append(",");
			strBuffer.append(obj.getColumn10());
			strBuffer.append('\n');
		}
		return strBuffer;
	}
	
		
	/*
	 * Create CorePerformanceTarget CSV file
	 * @param   : List<CorePerformanceTargetReportObj> dataList(List of Core Schema objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createCorePerformanceTargetCSVReport (List<CorePerformanceTargetReportObj> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Id");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Name");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Type");
		  strBuffer.append(",");
		  strBuffer.append("Sales_Type");
		  strBuffer.append(",");
		  strBuffer.append("Pubisher_Id");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Advertiser");
		  strBuffer.append(",");
		  strBuffer.append("Order");
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Type");
		  strBuffer.append(",");
		  strBuffer.append("LineItem");
		  strBuffer.append(",");
		  strBuffer.append("Creative");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Size");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Type");
		  strBuffer.append(",");		  
		  strBuffer.append("Site_Id");
		  strBuffer.append(",");
		  strBuffer.append("Site_Name");
		  strBuffer.append(",");		 
		  strBuffer.append("Site_Type_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Site_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone");			 
		  strBuffer.append(",");
		  strBuffer.append("Advertiser_Id");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Id");	 
		  strBuffer.append(",");
		  strBuffer.append("Creative_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_End_Time");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_PO_Number");			 
	      strBuffer.append(",");		     
	      strBuffer.append("Agency");			 
		  strBuffer.append(",");
		  strBuffer.append("Agency_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Trafficker");			 
		  strBuffer.append(",");
		  strBuffer.append("Sales_Person");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Date");
		  strBuffer.append(",");		  
		  strBuffer.append("Cost_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Rate");			 
		  strBuffer.append(",");
		  strBuffer.append("Goal_Qty");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Priority");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Orde_Lifetime_Clicks");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Clicks");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Requests");
		  strBuffer.append(",");
		  strBuffer.append("Served");
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Impressions_CPD");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Total_Clicks");
		  strBuffer.append(",");			  
		  strBuffer.append("Clicks_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPC");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPD");			 
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Adserver_CPM_And_CPC_Revenue");			 
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPD");
		  strBuffer.append(",");		  
		  strBuffer.append("ECPM");
		  strBuffer.append(",");
		  strBuffer.append("RPM");
		  strBuffer.append(",");
		  strBuffer.append("Fill_Rate");
		  strBuffer.append(",");
		  strBuffer.append("CTR");		 
		  strBuffer.append(",");	
		  strBuffer.append("Contracted_Qty");
		  strBuffer.append(",");
		  strBuffer.append("Delivery_Indicator_Percentage");			 
		  strBuffer.append(",");		 
		  strBuffer.append("Budget");			 
		  strBuffer.append(",");
		  strBuffer.append("Spent_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Balance_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Ad_Source");
		  strBuffer.append(",");
		  strBuffer.append("Data_Source");
		  strBuffer.append(",");
		  strBuffer.append("Order_Budget");
		  strBuffer.append(",");
		  strBuffer.append("Passback");
		  strBuffer.append(",");
		  strBuffer.append("Direct_Delivered");
		  strBuffer.append(",");
		  strBuffer.append("Target_Category");
		  strBuffer.append(",");
		  strBuffer.append("Target_Value");
		  strBuffer.append(",");
		  strBuffer.append("Column1");
		  strBuffer.append(",");
		  strBuffer.append("Column2");
		  strBuffer.append(",");
		  strBuffer.append("Column3");
		  strBuffer.append(",");
		  strBuffer.append("Column4");
		  strBuffer.append(",");
		  strBuffer.append("Column5");
		  strBuffer.append(",");
		  strBuffer.append("Column6");
		  strBuffer.append(",");
		  strBuffer.append("Column7");
		  strBuffer.append(",");
		  strBuffer.append("Column8");
		  strBuffer.append(",");
		  strBuffer.append("Column9");
		  strBuffer.append(",");
		  strBuffer.append("Column10");
		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
	      for(CorePerformanceTargetReportObj obj:dataList){
	    	  //strBuffer.append(obj.getLoadTimestamp());
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getAdvertiser());
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrder());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItem());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreative());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeSize());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeType());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getSiteId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteTypeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZoneId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZone());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdvertiserId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemId());	 
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartTime()==null?"":obj.getOrderStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartTime()==null?"":obj.getLineItemStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndTime()==null?"":obj.getOrderEndTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemEndTime()==null?"":obj.getLineitemEndTime());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getOrderPONumber());			 
		      strBuffer.append(",");		     
		      strBuffer.append(obj.getAgency());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAgencyId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getTrafficker());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesPerson());			 
			  strBuffer.append(",");			
			  strBuffer.append(obj.getDate()==null?"":obj.getDate());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getCostType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getRate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getGoalQty());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemPriority());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrdeLifetimeClicks());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeClicks());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getRequests());
			  strBuffer.append(",");
			  strBuffer.append(obj.getServed());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPC());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getImpressionsCPD());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getClicksCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPD());			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getTotalRevenue()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPM()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPC()));
			  strBuffer.append(",");			  
			  strBuffer.append(getDoubleStringValue(obj.getAdserverCPMAndCPCRevenue()));			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPD()));
			  strBuffer.append(",");
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRPM());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getFillRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getContractedQty());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDeliveryIndicator());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getBudget());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSpentLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getBalanceLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderBudget());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getPassback());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getDirectDelivered());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getTargetCategory());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getTargetValue());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn1());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn2());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn3());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn4());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn5());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn6());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn7());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn8());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn9());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn10());	
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
	/*
	 * Create DFPTargetReportObj CSV file
	 * @param   : List<DFPTargetReportObj> dataList(List of Core Schema objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createDFPTargetCSVReport (List<DFPTargetReportObj> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");
		  strBuffer.append("Pubisher_Id");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Advertiser");
		  strBuffer.append(",");
		  strBuffer.append("Order");
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Type");
		  strBuffer.append(",");
		  strBuffer.append("LineItem");
		  strBuffer.append(",");
		  strBuffer.append("Advertiser_Id");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Id");	 
		  strBuffer.append(",");
		  strBuffer.append("Date");
		  strBuffer.append(",");	
		  strBuffer.append("Requests");
		  strBuffer.append(",");
		  strBuffer.append("Served");
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Total_Clicks");		 
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");			  
		  strBuffer.append("Adserver_CPM_And_CPC_Revenue");			 
		  strBuffer.append(",");
		  strBuffer.append("ECPM");
		  strBuffer.append(",");
		  strBuffer.append("Fill_Rate");
		  strBuffer.append(",");
		  strBuffer.append("CTR");	
		  strBuffer.append(",");
		  strBuffer.append("Delivery_Indicator_Percentage");			 
		  strBuffer.append(",");
		  strBuffer.append("Data_Source");
		  strBuffer.append(",");
		  strBuffer.append("Target_Category");
		  strBuffer.append(",");
		  strBuffer.append("Target_Value");
		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
	      for(DFPTargetReportObj obj:dataList){
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getAdvertiser());
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrder());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItem());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdvertiserId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemId());	 
			  strBuffer.append(",");			 			
			  strBuffer.append(obj.getDate()==null?"":obj.getDate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRequests());
			  strBuffer.append(",");
			  strBuffer.append(obj.getServed());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getTotalRevenue()));
			  strBuffer.append(",");	  
			  strBuffer.append(getDoubleStringValue(obj.getAdserverCPMAndCPCRevenue()));			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getFillRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getDeliveryIndicator());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getTargetCategory());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getTargetValue());
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
	/*
	 * Create CorePerformance Finalise CSV file
	 * @param   : List<CorePerformanceReportObj> dataList(List of Core Schema objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createCorePerformanceFinaliseCSVReport (List<CorePerformanceReportObj> dataList) {		
    	
		StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Id");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Name");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Type");
		  strBuffer.append(",");
		  strBuffer.append("Sales_Type");
		  strBuffer.append(",");
		  strBuffer.append("Pubisher_Id");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Advertiser");
		  strBuffer.append(",");
		  strBuffer.append("Order");
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Type");
		  strBuffer.append(",");
		  strBuffer.append("LineItem");
		  strBuffer.append(",");
		  strBuffer.append("Creative");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Size");
		  strBuffer.append(",");
		  strBuffer.append("Creative_Type");
		  strBuffer.append(",");		  
		  strBuffer.append("Site_Id");
		  strBuffer.append(",");
		  strBuffer.append("Site_Name");
		  strBuffer.append(",");		 
		  strBuffer.append("Site_Type_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Site_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Zone");			 
		  strBuffer.append(",");
		  strBuffer.append("Advertiser_Id");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Id");	 
		  strBuffer.append(",");
		  strBuffer.append("Creative_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_End_Date");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Start_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_End_Time");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_End_Time");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Order_PO_Number");			 
	      strBuffer.append(",");		     
	      strBuffer.append("Agency");			 
		  strBuffer.append(",");
		  strBuffer.append("Agency_Id");			 
		  strBuffer.append(",");
		  strBuffer.append("Trafficker");			 
		  strBuffer.append(",");
		  strBuffer.append("Sales_Person");			 
		  strBuffer.append(",");		  
		  strBuffer.append("Date");
		  strBuffer.append(",");		  
		  strBuffer.append("Cost_Type");			 
		  strBuffer.append(",");
		  strBuffer.append("Rate");			 
		  strBuffer.append(",");
		  strBuffer.append("Goal_Qty");			 
		  strBuffer.append(",");
		  strBuffer.append("LineItem_Priority");			 
		  strBuffer.append(",");
		  strBuffer.append("Order_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Orde_Lifetime_Clicks");			 
		  strBuffer.append(",");
		  strBuffer.append("Lineitem_Lifetime_Clicks");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Requests");
		  strBuffer.append(",");
		  strBuffer.append("Served");
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Impressions_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Impressions_CPD");			 
		  strBuffer.append(",");			  
		  strBuffer.append("Total_Clicks");
		  strBuffer.append(",");			  
		  strBuffer.append("Clicks_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPC");
		  strBuffer.append(",");
		  strBuffer.append("Clicks_CPD");			 
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPM");
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPC");
		  strBuffer.append(",");			  
		  strBuffer.append("Adserver_CPM_And_CPC_Revenue");			 
		  strBuffer.append(",");
		  strBuffer.append("Revenue_CPD");
		  strBuffer.append(",");		  
		  strBuffer.append("ECPM");
		  strBuffer.append(",");
		  strBuffer.append("RPM");
		  strBuffer.append(",");
		  strBuffer.append("Fill_Rate");
		  strBuffer.append(",");
		  strBuffer.append("CTR");		 
		  strBuffer.append(",");	
		  strBuffer.append("Contracted_Qty");
		  strBuffer.append(",");
		  strBuffer.append("Delivery_Indicator_Percentage");			 
		  strBuffer.append(",");		 
		  strBuffer.append("Budget");			 
		  strBuffer.append(",");
		  strBuffer.append("Spent_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Balance_Lifetime");			 
		  strBuffer.append(",");
		  strBuffer.append("Ad_Source");
		  strBuffer.append(",");
		  strBuffer.append("Data_Source");
		  strBuffer.append(",");
		  strBuffer.append("Order_Budget");
		  strBuffer.append(",");
		  strBuffer.append("Passback");
		  strBuffer.append(",");
		  strBuffer.append("Direct_Delivered");
		  strBuffer.append(",");
		  strBuffer.append("Column1");
		  strBuffer.append(",");
		  strBuffer.append("Column2");
		  strBuffer.append(",");
		  strBuffer.append("Column3");
		  strBuffer.append(",");
		  strBuffer.append("Column4");
		  strBuffer.append(",");
		  strBuffer.append("Column5");
		  strBuffer.append(",");
		  strBuffer.append("Column6");
		  strBuffer.append(",");
		  strBuffer.append("Column7");
		  strBuffer.append(",");
		  strBuffer.append("Column8");
		  strBuffer.append(",");
		  strBuffer.append("Column9");
		  strBuffer.append(",");
		  strBuffer.append("Column10");
		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
	      for(CorePerformanceReportObj obj:dataList){
	    	  //strBuffer.append(obj.getLoadTimestamp());
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getAdvertiser());
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrder());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItem());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreative());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeSize());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeType());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getSiteId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteTypeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZoneId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getZone());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdvertiserId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemId());	 
			  strBuffer.append(",");
			  strBuffer.append(obj.getCreativeId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartDate()==null?"":obj.getOrderStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartDate()==null?"":obj.getLineItemStartDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndDate()==null?"":obj.getOrderEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemEndDate()==null?"":obj.getLineItemEndDate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderStartTime()==null?"":obj.getOrderStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemStartTime()==null?"":obj.getLineItemStartTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderEndTime()==null?"":obj.getOrderEndTime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemEndTime()==null?"":obj.getLineitemEndTime());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getOrderPONumber());			 
		      strBuffer.append(",");		     
		      strBuffer.append(obj.getAgency());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAgencyId());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getTrafficker());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesPerson());			 
			  strBuffer.append(",");			
			  strBuffer.append(obj.getDate()==null?"":obj.getDate());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getCostType());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getRate());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getGoalQty());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineItemPriority());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeImpressions());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrdeLifetimeClicks());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getLineitemLifetimeClicks());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getRequests());
			  strBuffer.append(",");
			  strBuffer.append(obj.getServed());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPC());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getImpressionsCPD());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getClicksCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPD());			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getTotalRevenue()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPM()));
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPC()));
			  strBuffer.append(",");			  
			  strBuffer.append(getDoubleStringValue(obj.getAdserverCPMAndCPCRevenue()));			 
			  strBuffer.append(",");
			  strBuffer.append(getDoubleStringValue(obj.getRevenueCPD()));
			  strBuffer.append(",");
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRPM());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getFillRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getContractedQty());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDeliveryIndicator());			 
			  strBuffer.append(",");			  
			  strBuffer.append(obj.getBudget());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getSpentLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getBalanceLifetime());			 
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getDataSource());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getOrderBudget());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getPassback());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getDirectDelivered());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn1());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn2());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn3());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn4());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn5());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn6());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn7());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn8());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn9());	
			  strBuffer.append(",");
			  strBuffer.append(obj.getColumn10());	
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
	
	/*
	 * @author Youdhveer Panwar
	 * Create ProductPerformanceReportObj CSV file
	 * @param   : List<ProductPerformanceReportObj> dataList(List of ProductPerformanceReportObj objects)
	 *        
	 * @return  : StringBuffer (csv data)
	 */
	public static StringBuffer createProductPerformanceSchemaCSVReport (List<ProductPerformanceReportObj> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("LoadTimestamp");
		  strBuffer.append(",");		 
		  strBuffer.append("NetworkCode");
		  strBuffer.append(",");
		  strBuffer.append("AdUnitId1");
		  strBuffer.append(",");
		  strBuffer.append("AdUnitId2");
		  strBuffer.append(",");
		  strBuffer.append("AdUnitId3");
		  strBuffer.append(",");
		  strBuffer.append("AdUnitId4");
		  strBuffer.append(",");
		  strBuffer.append("AdUnitId5");
		  strBuffer.append(",");
		  strBuffer.append("AdUnit1");
		  strBuffer.append(",");
		  strBuffer.append("AdUnit2");
		  strBuffer.append(",");
		  strBuffer.append("AdUnit3");
		  strBuffer.append(",");
		  strBuffer.append("AdUnit4");
		  strBuffer.append(",");
		  strBuffer.append("AdUnit5");
		  strBuffer.append(",");		  
		  strBuffer.append("Country");
		  strBuffer.append(",");
		  strBuffer.append("CountryId");
		  strBuffer.append(",");
		  strBuffer.append("Region");
		  strBuffer.append(",");
		  strBuffer.append("RegionId");
		  strBuffer.append(",");		  
		  strBuffer.append("City");
		  strBuffer.append(",");
		  strBuffer.append("CityId");
		  strBuffer.append(",");		 
		  strBuffer.append("Impressions");			 
		  strBuffer.append(",");
		  strBuffer.append("Clicks");		  
		  strBuffer.append('\n');
		  String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");	
	      for(ProductPerformanceReportObj obj:dataList){
	    	  strBuffer.append(loadTimestamp);
			  strBuffer.append(",");			 	  
			  strBuffer.append(obj.getNetworkCode());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdUnitId1());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getAdUnitId2());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getAdUnitId3());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getAdUnitId4());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getAdUnitId5());
			  strBuffer.append(",");	
			  strBuffer.append(obj.getAdUnit1());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdUnit2());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdUnit3());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdUnit4());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdUnit5());
			  strBuffer.append(",");			 		
			  strBuffer.append(obj.getCountryName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCountryId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRegionName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRegionId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCityName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCityId());
			  strBuffer.append(",");			 
			  strBuffer.append(obj.getImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicks());
			  
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}

	   public static void setDeviceCapability(TechnologyTargeting targeting, Integer capabilityType){
		   if(capabilityType  == null || capabilityType == TARGETING_MOBILE_APP_AND_WEB){
			   log.info("Device capability is either null or mobile and web. capability type receieved is ["+ capabilityType+"]");
			   return;
		   }
		   log.info("Adding device capability for type: ["+ capabilityType+"]");
		   DeviceCapabilityTargeting deviceCapTar = new DeviceCapabilityTargeting();
		   DeviceCapability mobileAppCapability = new DeviceCapability();
		   mobileAppCapability.setId(5005L); // 5005  = Mobile Apps
		   
		   if(capabilityType == TARGETING_MOBILE_APP_ONLY){
			   deviceCapTar.getTargetedDeviceCapabilities().add(mobileAppCapability);
			   log.info("Added mobile category to targeted device capability.");
		   }else if(capabilityType == TARGETING_MOBILE_WEB_ONLY){
			   deviceCapTar.getExcludedDeviceCapabilities().add(mobileAppCapability);
			   log.info("Added mobile capability to excluded devices.");
		   }else{
			   log.info("No valid capability found. Returning.");
			   return;
		   }
		   
		   targeting.setDeviceCapabilityTargeting(deviceCapTar);
		   log.info("Added device capability targeting successfully.");
	   }
	   
	public static void main(String [] args){		
		double CTR=calculateCTR(2,4400);
		double eCPM=calculateECPM(11000000,4400);
		double RPM=calculateRPM(62.86,2593977);
		double fillRate=calculateFillRate(191127,541110);
		System.out.println(" CTR:"+CTR+"\n eCPM:"+eCPM+"\n RPM:"+RPM+"\nfillRate:"+fillRate);
	}
}
