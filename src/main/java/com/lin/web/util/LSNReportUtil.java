package com.lin.web.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.files.LockException;
import com.lin.server.bean.CorePerformanceReportObj;

/*
 *  LSN channel util file
 *  @author Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
 */
public class LSNReportUtil {

	static final Logger log = Logger.getLogger(LSNReportUtil.class.getName());
    static String [] corePerformanceHeaderArray={
    	"Load_Timestamp","Channel_Id","Channel_Name","Channel_Type",
    	"Sales_Type","Pubisher_Id","Publisher_Name","Advertiser","Order","LineItem_Type","LineItem",
    	"Creative","Creative_Size","Creative_Type","Site_Id","Site_Name", "Site_Type_Id", "Site_Type",
    	"Zone_Id",	 "Zone", "Advertiser_Id", "Order_Id", "Lineitem_Id", "Creative_Id",	"Order_Start_Date",	
    	"LineItem_Start_Date", "Order_End_Date", "LineItem_End_Date", "Order_Start_Time", "LineItem_Start_Time",
    	"Order_End_Time","Lineitem_End_Time", "Order_PO_Number","Agency", "Agency_Id", "Trafficker","Sales_Person",
    	"Date",	"Cost_Type", "Rate", "Goal_Qty", "LineItem_Priority", "Order_Lifetime_Impressions",	"Lineitem_Lifetime_Impressions",
    	"Orde_Lifetime_Clicks",	 "Lineitem_Lifetime_Clicks", "Requests","Served","Total_Impressions","Impressions_CPM",
    	"Impressions_CPC",	 "Impressions_CPD",	 "Total_Clicks", "Clicks_CPM","Clicks_CPC","Clicks_CPD", "Total_Revenue",
    	"Revenue_CPM","Revenue_CPC", "Adserver_CPM_And_CPC_Revenue","Revenue_CPD",	"ECPM","RPM","Fill_Rate","CTR",
    	"Contracted_Qty","Delivery_Indicator_Percentage","Budget", "Spent_Lifetime", "Balance_Lifetime", "Ad_Source",
    	"Data_Source","Column1","Column2","Column3","Column4","Column5","Column6","Column7","Column8","Column9","Column10"
     };
    
    public static  List<String> corePerformanceHeaderList=new ArrayList<String>(Arrays.asList(corePerformanceHeaderArray));
	
	
	/*
	 *  Create LSN report from raw csv file to CorePerformance Schema
	 *  
	 *  @param String fileName
	 *  @return  List<CorePerformanceReportObj> reportList - List of @CorePerformanceReportObj objects
	 */
	public static List<CorePerformanceReportObj> createLSNCorePerformanceCSVReport(String fileName) {
		List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

		InputStream inputStream;
        String[] headerRow=null;
		try {
			inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			CSVReader csvReader = new CSVReader(reader);

			List<String[]> allElements = csvReader.readAll();
			String currentTime = DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			int count = 0;
			for (String[] line : allElements) {
				if (count == 0) {
					headerRow=line;
					log.info("Skip first row...");
				} else {
					if (headerRow.length > 0) {
						String date = line[0];
						date=DateUtil.getFormatedDate(date, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
						
						String siteName = line[1];
						if(siteName !=null && siteName.indexOf("(LIN - MRO)") >=0){
							siteName=siteName.replace("(LIN - MRO)","");
						}
						long  requests = StringUtil.getLongValue(line[2]);
						long  impressions = StringUtil.getLongValue(line[3]);
						double fillRate=StringUtil.getDoubleValue(line[4]);
						long  clicks = StringUtil.getLongValue(line[5]);
						double CTR=StringUtil.getDoubleValue(line[6]);
						double ECPM=StringUtil.getDoubleValue(line[7]);
						double revenue=StringUtil.getDoubleValue(line[8]);
						
						long  backFilledImpressions = StringUtil.getLongValue(line[9]);
						long  lsnFilledImpressions = StringUtil.getLongValue(line[10]);
						
						CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
						rootObj.setLoadTimestamp(currentTime);
						
						rootObj.setDate(date);

						rootObj.setTotalImpressions(impressions);
						rootObj.setTotalRevenue(revenue);
						rootObj.setTotalClicks(clicks);
						rootObj.setCTR(CTR);
						rootObj.setRequests(requests);
						rootObj.setFillRate(fillRate);
						rootObj.setECPM(ECPM);
						rootObj.setSiteName(siteName);
						rootObj.setSiteId(siteName);

						rootObj.setColumn1(String.valueOf(backFilledImpressions));
						rootObj.setColumn2(String.valueOf(lsnFilledImpressions));
						
						rootObj.setPublisherId(StringUtil
								.getLongValue(LinMobileConstants.LSN_PUBLISHER_ID));
						rootObj.setPublisherName(LinMobileConstants.LSN_PUBLISHER_NAME);
						rootObj.setChannelId(Integer
								.parseInt(LinMobileConstants.LSN_CHANNEL_ID));
						rootObj.setChannelName(LinMobileConstants.LSN_CHANNEL_NAME);
						rootObj.setChannelType(LinMobileConstants.LSN_CHANNEL_TYPE);
						rootObj.setSalesType(LinMobileConstants.LSN_SALES_TYPE);
						rootObj.setDataSource("LSN");
						reportList.add(rootObj);
					}
				}
				count++;
			}		
			log.info("Total line processed in this csv :"+count);
			reader.close();
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			

		}
		return reportList;
	}
	
	
	/*
	 *  Create dynamic report from raw csv file to CorePerformance Schema
	 *  
	 *  @param String fileName
	 *  @return  List<CorePerformanceReportObj> reportList - List of @CorePerformanceReportObj objects
	 */
	public static List<CorePerformanceReportObj> generateDynamicCorePerformanceCSVReport(String fileName) {
		
		List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();
        List<Integer> columnIndexList=new ArrayList<Integer>();
        
		InputStream inputStream;
        String[] headerRow=null;
		try {
			inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			CSVReader csvReader = new CSVReader(reader);
			List<String[]> allElements = csvReader.readAll();			
			int count = 0;
			for (String[] line : allElements) {
				if (count == 0) {
					log.info("Reading headers from first row...");
					headerRow=line;
					columnIndexList=mapHeaders(headerRow);
					log.info("Mapped columnIndexList:"+columnIndexList.size());
					if(columnIndexList.size() != headerRow.length){
						log.warning("Some column is not mapped, please check your report.");
						break;
					}
					
				} else {
					CorePerformanceReportObj rootObj=mapRows(line,columnIndexList);
					reportList.add(rootObj);
				}
				count++;
			}		
			log.info("Total line processed in this csv :"+count);
			reader.close();
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			

		}
		return reportList;
	}
	
	/*
	 * Map headers to actual CorePerformance report headers
	 * @param  - String [] headers
	 * @return - List<Integer> : List of mapped header indices
	 */
	public static List<Integer> mapHeaders(String [] headers){
		List<Integer> columnIndexList=new ArrayList<Integer>();
		log.info("Total headers in CorePerformance :"+corePerformanceHeaderList.size());
		for(String header:headers){
			int index=corePerformanceHeaderList.indexOf(header);
			if(index != -1){
				columnIndexList.add(index);
			}else{
				log.info("Not found this in header list :"+header+" , please check your report header, all should match to standard headers");
				
			}
		}
		return columnIndexList;
	}
	
	/*
	 * Read each row of csv and return report object
	 * 
	 * @param  - String [] headers,
	 *           List<Integer> columnIndexList (Mapped header index list)
	 *           
	 * @return - CorePerformanceReportObj  
	 */
	public static CorePerformanceReportObj mapRows(String [] line,List<Integer> columnIndexList){
	   CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
	   for(int i=0;i<line.length;i++){
		   int index=columnIndexList.get(i);
		   String value=line[i];
		   
		   switch(index){
		   		case 1:
		   			rootObj.setLoadTimestamp(value);
		   			break;
		   		case 2:
		   			rootObj.setChannelId(StringUtil.getLongValue(value));
		   			break;
		   		case 3:
		   			rootObj.setChannelName(value);
		   			break;
		   		case 4:
		   			rootObj.setSalesType(value);
		   			break;
		   		case 5:
		   			rootObj.setPublisherId(StringUtil.getLongValue(value));
		   			break;
		   		case 6:
		   			rootObj.setPublisherName(value);
		   			break;
		   		case 7:
		   			rootObj.setAdvertiser(value);
		   			break;
		   		case 8:
		   			rootObj.setOrder(value);
		   			break;
		   		case 9:
		   			rootObj.setLineItemType(value);
		   			break;
		   		case 10:
		   			rootObj.setLineItem(value);
		   			break;
		   		case 11:
		   			rootObj.setCreative(value);
		   			break;
		   		case 12:
		   			rootObj.setCreativeSize(value);
		   			break;
		   		case 13:
		   			rootObj.setCreativeType(value);
		   			break;
		   		case 14:
		   			rootObj.setSiteId(value);
		   			break;
		   		case 15:
		   			rootObj.setSiteName(value);
		   			break;
		   		case 16:
		   			rootObj.setSiteTypeId(value);
		   			break;
		   		case 17:
		   			rootObj.setSiteType(value);
		   			break;
		   		case 18:
		   			rootObj.setZoneId(StringUtil.getLongValue(value));
		   			break;
		   		case 19:
		   			rootObj.setZone(value);
		   			break;
		   		case 20:
		   			rootObj.setAdvertiserId(StringUtil.getLongValue(value));
		   			break;
		   		case 21:
		   			rootObj.setOrderId(StringUtil.getLongValue(value));
		   			break;
		   		case 22:
		   			rootObj.setLineitemId(StringUtil.getLongValue(value));
		   			break;
		   		case 23:
		   			rootObj.setCreativeId(StringUtil.getLongValue(value));
		   			break;
		   		case 24:		   			
		   			rootObj.setOrderStartDate(value);
		   			break;
		   		case 25:		   			
		   			rootObj.setLineItemStartDate(value);
		   			break;
		   		case 26:		   			
		   			rootObj.setOrderEndDate(value);
		   			break;
		   		case 27:		   			
		   			rootObj.setLineItemEndDate(value);
		   			break;
		   		case 28:		   			
		   			rootObj.setOrderStartTime(value);
		   			break;
		   		case 29:		   			
		   			rootObj.setLineItemStartTime(value);
		   			break;
		   		case 30:		   			
		   			rootObj.setOrderEndTime(value);
		   			break;
		   		case 31:		   			
		   			rootObj.setLineitemEndTime(value);
		   			break;
		   		case 32:
		   			rootObj.setOrderPONumber(value);
		   			break;
		   		case 33:
		   			rootObj.setAgency(value);
		   			break;
		   		case 34:
		   			rootObj.setAgencyId(StringUtil.getLongValue(value));
		   			break;
		   		case 35:
		   			rootObj.setTrafficker(value);
		   			break;
		   		case 36:
		   			rootObj.setSalesPerson(value);
		   			break;
		   		case 37:		   			
		   			rootObj.setDate(value);
		   			break;
		   		case 38:
		   			rootObj.setCostType(value);
		   			break;
		   		case 39:
		   			rootObj.setRate(StringUtil.getDoubleValue(value));
		   			break;
		   		case 40:
		   			rootObj.setGoalQty(StringUtil.getLongValue(value));
		   			break;
		   		case 41:
		   			if(!LinMobileUtil.isNumeric(value)){
		   				value="0";
		   			}
		   			rootObj.setLineItemPriority(Integer.parseInt(value));
		   			break;
		   		case 42:
		   			rootObj.setOrderLifetimeImpressions(StringUtil.getLongValue(value));
		   			break;
		   		case 43:
		   			rootObj.setLineitemLifetimeImpressions(StringUtil.getLongValue(value));
		   			break;
		   		case 44:
		   			rootObj.setOrdeLifetimeClicks(StringUtil.getLongValue(value));
		   			break;
		   		case 45:
		   			rootObj.setLineitemLifetimeClicks(StringUtil.getLongValue(value));
		   			break;
		   		case 46:
		   			rootObj.setRequests(StringUtil.getLongValue(value));
		   			break;
		   		case 47:
		   			rootObj.setServed(StringUtil.getLongValue(value));
		   			break;
		   		case 48:
		   			rootObj.setTotalImpressions(StringUtil.getLongValue(value));
		   			break;
		   		case 49:
		   			rootObj.setImpressionsCPM(StringUtil.getLongValue(value));
		   			break;
		   		case 50:
		   			rootObj.setImpressionsCPC(StringUtil.getLongValue(value));
		   			break;
		   		case 51:
		   			rootObj.setImpressionsCPD(StringUtil.getLongValue(value));
		   			break;
		   		case 52:
		   			rootObj.setTotalClicks(StringUtil.getLongValue(value));
		   			break;
		   		case 53:
		   			rootObj.setClicksCPM(StringUtil.getLongValue(value));
		   			break;
		   		case 54:
		   			rootObj.setClicksCPC(StringUtil.getLongValue(value));
		   			break;
		   		case 55:
		   			rootObj.setClicksCPD(StringUtil.getLongValue(value));
		   			break;
		   		case 56:
		   			rootObj.setTotalRevenue(StringUtil.getLongValue(value));
		   			break;
		   		case 57:
		   			rootObj.setRevenueCPM(StringUtil.getDoubleValue(value));
		   			break;
		   		case 58:
		   			rootObj.setRevenueCPC(StringUtil.getDoubleValue(value));
		   			break;
		   		case 59:
		   			rootObj.setAdserverCPMAndCPCRevenue(StringUtil.getDoubleValue(value));
		   			break;
		   		case 60:
		   			rootObj.setRevenueCPD(StringUtil.getDoubleValue(value));
		   			break;
		   		case 61:
		   			rootObj.setECPM(StringUtil.getDoubleValue(value));
		   			break;
		   		case 62:
		   			rootObj.setRPM(StringUtil.getDoubleValue(value));
		   			break;
		   		case 63:
		   			rootObj.setFillRate(StringUtil.getDoubleValue(value));
		   			break;
		   		case 64:
		   			rootObj.setCTR(StringUtil.getDoubleValue(value));
		   			break;
		   		case 65:
		   			rootObj.setContractedQty(StringUtil.getDoubleValue(value));
		   			break;
		   		case 66:
		   			rootObj.setDeliveryIndicator(StringUtil.getDoubleValue(value));
		   			break;
		   		case 67:
		   			rootObj.setBudget(StringUtil.getDoubleValue(value));
		   			break;
		   		case 68:
		   			rootObj.setSpentLifetime(StringUtil.getDoubleValue(value));
		   			break;
		   		case 69:
		   			rootObj.setBalanceLifetime(StringUtil.getDoubleValue(value));
		   			break;
		   		case 70:
		   			rootObj.setAdSource(value);
		   			break;
		   		case 71:
		   			rootObj.setDataSource(value);
		   			break;
		   		case 72:
		   			rootObj.setColumn1(value);
		   			break;
		   		case 73:
		   			rootObj.setColumn2(value);
		   			break;
		   		case 74:
		   			rootObj.setColumn3(value);
		   			break;
		   		case 75:
		   			rootObj.setColumn4(value);
		   			break;
		   		case 76:
		   			rootObj.setColumn5(value);
		   			break;
		   		case 77:
		   			rootObj.setColumn6(value);
		   			break;
		   		case 78:
		   			rootObj.setColumn7(value);
		   			break;
		   		case 79:
		   			rootObj.setColumn8(value);
		   			break;
		   		case 80:
		   			rootObj.setColumn9(value);
		   			break;
		   		case 81:
		   			rootObj.setColumn10(value);
		   			break;
		   		default:
		   			log.warning("Invalid  value (No header for this column value) :"+value);
		   			break;
		   }
	   }
	   return rootObj;
	}
}
