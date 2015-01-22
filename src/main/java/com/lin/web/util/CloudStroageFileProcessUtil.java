package com.lin.web.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.LockException;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.DFPTargetReportObj;
import com.lin.server.bean.LocationReportObj;
import com.lin.server.bean.ProductPerformanceReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;

@SuppressWarnings("deprecation")
public class CloudStroageFileProcessUtil {

	
	private static final Logger log = Logger.getLogger(CloudStroageFileProcessUtil.class.getName());	
  
    private static List<String> corePerformanceReportColsList=null;
    static final String [] corePerformanceReportCols={
    	"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME",
    	"Ad unit ID 1","Ad unit ID 2","Ad unit ID 3","Ad unit ID 4","Ad unit ID 5","Ad unit 1","Ad unit 2","Ad unit 3","Ad unit 4","Ad unit 5",
    	"Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.CREATIVE_ID","Dimension.CREATIVE_NAME","Dimension.CREATIVE_SIZE","Dimension.CREATIVE_TYPE","Dimension.SALESPERSON_NAME","Dimension.CREATIVE_TYPE_ID","Dimension.SALESPERSON_ID","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","DimensionAttribute.LINE_ITEM_COST_TYPE","DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS","DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.ORDER_LIFETIME_CLICKS","DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS","DimensionAttribute.ORDER_PO_NUMBER","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.ORDER_TRAFFICKER","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CTR","Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM","Column.AD_SERVER_CLICKS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS","Column.AD_SERVER_ALL_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR","Column.AD_SERVER_CPD_REVENUE","Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM","Column.AD_SERVER_DELIVERY_INDICATOR"
    };
    
    private static List<String> targetReportColsList=null;
    static final String [] targetReportCols={
		"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.GENERIC_CRITERION_NAME","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CLICKS","Column.AD_SERVER_CTR","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_SERVER_DELIVERY_INDICATOR","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM","Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"
     };

    private static List<String> locationReportColsList=null;
    static final String [] locationReportCols={
    	"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.COUNTRY_CRITERIA_ID","Dimension.REGION_CRITERIA_ID","Dimension.CITY_CRITERIA_ID","Dimension.COUNTRY_NAME","Dimension.REGION_NAME","Dimension.CITY_NAME","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","DimensionAttribute.LINE_ITEM_COST_TYPE","DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS","DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.ORDER_LIFETIME_CLICKS","DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS","DimensionAttribute.ORDER_PO_NUMBER","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.ORDER_TRAFFICKER","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CLICKS","Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM","Column.AD_SERVER_CTR","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_SERVER_CPD_REVENUE","Column.AD_SERVER_ALL_REVENUE","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM"
    };
    
    private static List<String> customEventReportColsList=null;
	static final String [] customEventReportCols={
		"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.CREATIVE_ID","Dimension.CREATIVE_NAME","Dimension.CREATIVE_SIZE","Dimension.CREATIVE_TYPE","Dimension.CUSTOM_EVENT_ID","Dimension.CUSTOM_EVENT_NAME","Dimension.CUSTOM_EVENT_TYPE","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","Column.RICH_MEDIA_CUSTOM_EVENT_COUNT","Column.RICH_MEDIA_CUSTOM_EVENT_TIME"
	};
    
	
	private static List<String> productPerformanceReportColsList=null;
    static final String [] productPerformanceReportCols={
    	"Ad unit ID 1","Ad unit ID 2","Ad unit ID 3","Ad unit ID 4","Ad unit ID 5","Ad unit 1","Ad unit 2","Ad unit 3","Ad unit 4","Ad unit 5","Dimension.COUNTRY_CRITERIA_ID","Dimension.REGION_CRITERIA_ID","Dimension.CITY_CRITERIA_ID","Dimension.COUNTRY_NAME","Dimension.REGION_NAME","Dimension.CITY_NAME","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CLICKS"

    };
    
    /*
	  * This method read a dfp raw file from specified cloud storage directory and 
	  * creates processed data list and rejected data list (if faulted data found)
	  * for given networkCode
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName,String networkCode,String bucketName
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readDFPRawCSVFromCloudStorage(
			 String fileName,String dirName,String networkCode,String publisherId,String publisherName, String bucketName) {
		 
		    List<CorePerformanceReportObj> correctDataList=new ArrayList<CorePerformanceReportObj>();
		    List<CorePerformanceReportObj> faultedDataList=new ArrayList<CorePerformanceReportObj>();
		    boolean faultedData=false;
		    
		    Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		    
		    FileService fileService = FileServiceFactory.getFileService();
		    String cloudSrorageFilePath = "/gs/" + bucketName + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+cloudSrorageFilePath);
		    AppEngineFile readableFile = new AppEngineFile(cloudSrorageFilePath);
		    FileReadChannel readChannel;
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
					CSVReader csvReader = new CSVReader(reader);
					
					if(corePerformanceReportColsList==null){
						corePerformanceReportColsList=new ArrayList<String>();
						corePerformanceReportColsList=Arrays.asList(corePerformanceReportCols);
					}
					
					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();					
					
					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...corePerformanceReportColsList:"+corePerformanceReportColsList.size());
							for(int i=0;i<line.length;i++){
								int index=corePerformanceReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
									//System.out.println(i+":"+line[i]);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
						}else{							
							
							// Get a single object from current csv row.
							CorePerformanceReportObj rootObj=getProcessedCorePerformanceRow(csvColumnMap, line, loadTimestamp);	
							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
							rootObj.setColumn7(networkCode);
							if(publisherId !=null ){
								 rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
							}
							if(publisherName!=null){
								 rootObj.setPublisherName(publisherName);
							}
							 
							if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
								rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
								rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
								rootObj.setChannelId(5);
								rootObj.setChannelName("National Sales Direct");
								rootObj.setChannelType("National Sales");
								rootObj.setSalesType("Direct");	
								
							}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
								
								 if(publisherId !=null ){
									 rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
								 }else{
									 rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
								 }
								 if(publisherName!=null){
									 rootObj.setPublisherName(publisherName);
								 }else{
									 rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
								 }
								
								 
								
								 String ORDER_PO_NUMBER=rootObj.getOrderPONumber();
								 String ADVERTISER_NAME=rootObj.getAdvertiser();
								 String LINE_ITEM_TYPE=rootObj.getLineItemType();
								 
								 if(ORDER_PO_NUMBER !=null 
											&& ( ORDER_PO_NUMBER.equalsIgnoreCase("Lin Mobile Partnership") 
											|| (ORDER_PO_NUMBER.equalsIgnoreCase("LIN Mobile Partnership") )
											|| (ORDER_PO_NUMBER.equalsIgnoreCase("Partnership") )
										)){
										if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Nexage")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.NEXAGE_CHANNEL_ID));
											rootObj.setChannelName(LinMobileConstants.NEXAGE_CHANNEL_NAME);
											rootObj.setChannelType(LinMobileConstants.NEXAGE_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.NEXAGE_SALES_TYPE);
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Mojiva")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.MOJIVA_CHANNEL_ID));
											rootObj.setChannelName(LinMobileConstants.MOJIVA_CHANNEL_NAME);
											rootObj.setChannelType(LinMobileConstants.MOJIVA_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.MOJIVA_SALES_TYPE);
										}else if( ADVERTISER_NAME !=null 
												&& ( ADVERTISER_NAME.equalsIgnoreCase("LIN | Undertone") 
														|| ADVERTISER_NAME.equalsIgnoreCase("LIN | Undertone (Ad network") ) ){
											rootObj.setChannelId(6);
											rootObj.setChannelName("Undertone");
											rootObj.setChannelType("Ad Network");
											rootObj.setSalesType("Non-direct");
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Google Ad Exchange - AdEx (Remnant)")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.AD_EXCHANGE_CHANNEL_ID));
											rootObj.setChannelName(LinMobileConstants.AD_EXCHANGE_CHANNEL_NAME);
											rootObj.setChannelType(LinMobileConstants.AD_EXCHANGE_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.AD_EXCHANGE_SALES_TYPE);
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | LSN")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.LSN_CHANNEL_ID));
											rootObj.setChannelName(LinMobileConstants.LSN_CHANNEL_NAME);
											rootObj.setChannelType(LinMobileConstants.LSN_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.LSN_SALES_TYPE);
										}else{
											faultedData=true;
										}
										
								}else if(ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("Lin Mobile Direct")){
									rootObj.setChannelId(5);
									//rootObj.setChannelName("Lin Mobile Direct");
									rootObj.setChannelName("National Sales Direct");
									rootObj.setChannelType("National Sales");
									rootObj.setSalesType("Direct");
								}else if(ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("TEST Ads")){
									rootObj.setChannelId(9);
									rootObj.setChannelName("TEST Ads");
									rootObj.setChannelType("TEST Ads");
									rootObj.setSalesType("N/A");
								}else{
									if( (LINE_ITEM_TYPE !=null && (LINE_ITEM_TYPE.equalsIgnoreCase("HOUSE")) 
											|| ( ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("HOUSE") ) ) ){
										rootObj.setChannelId(8);
										rootObj.setChannelName("House");
										rootObj.setChannelType("House Ads");
										rootObj.setSalesType("In House");
									}else if(LINE_ITEM_TYPE !=null && (LINE_ITEM_TYPE.equalsIgnoreCase("STANDARD") || LINE_ITEM_TYPE.equalsIgnoreCase("SPONSORSHIP"))){
										rootObj.setChannelId(4);
										rootObj.setChannelName("Local Sales Direct");
										rootObj.setChannelType("Local Sales");
										rootObj.setSalesType("Direct");									
									}else{
										faultedData=true;
									}
								}
												    
							}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
								rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
								rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
								rootObj.setChannelId(4);							
								rootObj.setChannelName("Local Sales Direct");
								rootObj.setChannelType("Local Sales");
								rootObj.setSalesType("Direct");	
							}else if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)){
								rootObj.setChannelId(4);							
								rootObj.setChannelName("Local Sales Direct");
								rootObj.setChannelType("Local Sales");
								rootObj.setSalesType("Direct");	
							}
							
							rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
							
							if(faultedData){
								faultedDataList.add(rootObj);
								faultedData=false;  // reset it to false after add in list
							}else{
								faultedData=false;
								correctDataList.add(rootObj);
							}
													    					    
						
					  }
					  count++;
				    }
					
					allDataMap.put("CorrectData", correctDataList);
					allDataMap.put("InCorrectData", faultedDataList);
					csvReader.close();
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
				
			}
			return allDataMap;
	 }
	 
	
	 public static CorePerformanceReportObj getProcessedCorePerformanceRow(Map<String,Integer> csvColumnMap, 
			 String[] line,String loadTimestamp){
		    
		    String DATE=csvColumnMap.get("Dimension.DATE") ==null?"":line[csvColumnMap.get("Dimension.DATE")];
			long ADVERTISER_ID=
					csvColumnMap.get("Dimension.ADVERTISER_ID")==null?0:
						StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ADVERTISER_ID")]);
			String ADVERTISER_NAME=
					csvColumnMap.get("Dimension.ADVERTISER_NAME")==null?"":line[csvColumnMap.get("Dimension.ADVERTISER_NAME")];
			if(ADVERTISER_NAME!=null ){
				ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
			}
			
			String AD_UNIT_ID_1=csvColumnMap.get("Ad unit ID 1")==null?"":line[csvColumnMap.get("Ad unit ID 1")];
			String AD_UNIT_ID_2=csvColumnMap.get("Ad unit ID 2")==null?"":line[csvColumnMap.get("Ad unit ID 2")];
			String AD_UNIT_ID_3=csvColumnMap.get("Ad unit ID 3")==null?"":line[csvColumnMap.get("Ad unit ID 3")];
			String AD_UNIT_ID_4=csvColumnMap.get("Ad unit ID 4")==null?"":line[csvColumnMap.get("Ad unit ID 4")];
			String AD_UNIT_ID_5=csvColumnMap.get("Ad unit ID 5")==null?"":line[csvColumnMap.get("Ad unit ID 5")];

			String AD_UNIT_NAME_1=csvColumnMap.get("Ad unit 1")==null?"":line[csvColumnMap.get("Ad unit 1")];
			if(AD_UNIT_NAME_1!=null ){
				AD_UNIT_NAME_1=AD_UNIT_NAME_1.replaceAll(","," ");
			}
			String AD_UNIT_NAME_2=csvColumnMap.get("Ad unit 2")==null?"":line[csvColumnMap.get("Ad unit 2")];
			if(AD_UNIT_NAME_2!=null ){
				AD_UNIT_NAME_2=AD_UNIT_NAME_2.replaceAll(","," ");
			}
			String AD_UNIT_NAME_3=csvColumnMap.get("Ad unit 3")==null?"":line[csvColumnMap.get("Ad unit 3")];
			if(AD_UNIT_NAME_3!=null ){
				AD_UNIT_NAME_3=AD_UNIT_NAME_3.replaceAll(","," ");
			}
			String AD_UNIT_NAME_4=csvColumnMap.get("Ad unit 4")==null?"":line[csvColumnMap.get("Ad unit 4")];
			if(AD_UNIT_NAME_4!=null ){
				AD_UNIT_NAME_4=AD_UNIT_NAME_4.replaceAll(","," ");
			}
			String AD_UNIT_NAME_5=csvColumnMap.get("Ad unit 5")==null?"":line[csvColumnMap.get("Ad unit 5")];
			if(AD_UNIT_NAME_5!=null ){
				AD_UNIT_NAME_5=AD_UNIT_NAME_5.replaceAll(","," ");
			}

			long ORDER_ID = csvColumnMap.get("Dimension.ORDER_ID")==null? 0 : StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ORDER_ID")]);
			
			String ORDER_NAME=csvColumnMap.get("Dimension.ORDER_NAME")==null ? "" : line[csvColumnMap.get("Dimension.ORDER_NAME")];
			if(ORDER_NAME!=null ){
				ORDER_NAME=ORDER_NAME.replaceAll(","," ");
			}

			long LINE_ITEM_ID= csvColumnMap.get("Dimension.LINE_ITEM_ID")==null ? 0 : StringUtil.getLongValue(line[csvColumnMap.get("Dimension.LINE_ITEM_ID")]);
			String LINE_ITEM_NAME=csvColumnMap.get("Dimension.LINE_ITEM_NAME")==null ? "" : line[csvColumnMap.get("Dimension.LINE_ITEM_NAME")];
			if(LINE_ITEM_NAME!=null ){
				LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
			}
			
			String LINE_ITEM_TYPE=csvColumnMap.get("Dimension.LINE_ITEM_TYPE")==null ? "" : line[csvColumnMap.get("Dimension.LINE_ITEM_TYPE")];
			long CREATIVE_ID=csvColumnMap.get("Dimension.CREATIVE_ID")==null?0: StringUtil.getLongValue(line[csvColumnMap.get("Dimension.CREATIVE_ID")]);
			String CREATIVE_NAME=csvColumnMap.get("Dimension.CREATIVE_NAME")==null?"": line[csvColumnMap.get("Dimension.CREATIVE_NAME")];
			if(CREATIVE_NAME!=null ){
				CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
			}
			
			String CREATIVE_SIZE=csvColumnMap.get("Dimension.CREATIVE_SIZE")==null?"":
				line[csvColumnMap.get("Dimension.CREATIVE_SIZE")];
			String CREATIVE_TYPE=csvColumnMap.get("Dimension.CREATIVE_TYPE")==null?"":
				line[csvColumnMap.get("Dimension.CREATIVE_TYPE")];
			String SALESPERSON_NAME=csvColumnMap.get("Dimension.SALESPERSON_NAME")==null?"":
				line[csvColumnMap.get("Dimension.SALESPERSON_NAME")];
			if(SALESPERSON_NAME!=null ){
				SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
			}
			//long SALESPERSON_ID=csvColumnMap.get("Dimension.SALESPERSON_ID")==null?0:StringUtil.getLongValue(line[csvColumnMap.get("Dimension.SALESPERSON_ID")]);

			String goalQty=csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")==null?"":
				line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")];
			if(!LinMobileUtil.isNumeric(goalQty)){
				goalQty="-1";
			}
			long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

			long LINE_ITEM_CONTRACTED_QUANTITY=csvColumnMap.get("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY")]);

			String LINE_ITEM_COST_PER_UNIT=csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")==null?"":
				line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")];
			if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
				LINE_ITEM_COST_PER_UNIT="0";
			}
			double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);
			   
			String LINE_ITEM_COST_TYPE=csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE")==null?"":
				line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE")];
			long LINE_ITEM_LIFETIME_CLICKS=csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS")]);
			long LINE_ITEM_LIFETIME_IMPRESSIONS=csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS")]);
			
			
			String LINE_ITEM_START_DATE_TIME=csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")==null?"":
			 DateUtil.getFormatedDateUsingJodaLib(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
			String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;
       
			String lineItemEndDate=csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")==null?"":
				line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")];
			
			String LINE_ITEM_END_DATE_TIME="";
			String LINE_ITEM_END_DATE="";
			if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
				LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
			    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
			 }

			String ORDER_AGENCY=csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")==null?"":
				line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")];
			if(ORDER_AGENCY !=null && ORDER_AGENCY.indexOf(",")>=0){
				ORDER_AGENCY=ORDER_AGENCY.replaceAll(",", " ");
			}
			long ORDER_AGENCY_ID=csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")]);
			
			long ORDER_LIFETIME_CLICKS=csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_CLICKS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_CLICKS")]);
			
			long ORDER_LIFETIME_IMPRESSIONS= csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS")]);
			
			String ORDER_PO_NUMBER=csvColumnMap.get("DimensionAttribute.ORDER_PO_NUMBER")==null?"":
				line[csvColumnMap.get("DimensionAttribute.ORDER_PO_NUMBER")];

			String orderStartDate=csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")==null?"":
				line[csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")];
			String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
			String ORDER_START_DATE=ORDER_START_DATE_TIME;

			String orderEndDate=csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")==null?"":
				line[csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")];
			String ORDER_END_DATE_TIME="";
			String ORDER_END_DATE="";
			if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
				 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
				 ORDER_END_DATE=ORDER_END_DATE_TIME;
			}					   
			String ORDER_TRAFFICKER=csvColumnMap.get("DimensionAttribute.ORDER_TRAFFICKER")==null?"":
				line[csvColumnMap.get("DimensionAttribute.ORDER_TRAFFICKER")];


			long AD_SERVER_IMPRESSIONS=csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")==null?0:
			   StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);	
			
			long AD_SERVER_CLICKS=csvColumnMap.get("Column.AD_SERVER_CLICKS")==null?0:
				StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_CLICKS")]);
			
			double AD_SERVER_WITH_CPD_AVERAGE_ECPM=csvColumnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")==null?0:
			  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")]);
			
			double AD_SERVER_CTR=0;
			String ctr=csvColumnMap.get("Column.AD_SERVER_CTR")==null?"":
				line[csvColumnMap.get("Column.AD_SERVER_CTR")];
			if(LinMobileUtil.isNumeric(ctr)){
				AD_SERVER_CTR=(((double)Double.parseDouble(ctr)*100));
			}	
			AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
			
			double AD_SERVER_CPM_AND_CPC_REVENUE=csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")==null?0:
			  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")]);
			
			double AD_SERVER_CPD_REVENUE=csvColumnMap.get("Column.AD_SERVER_CPD_REVENUE")==null?0:
				ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPD_REVENUE")]);
			
			double AD_SERVER_ALL_REVENUE=csvColumnMap.get("Column.AD_SERVER_ALL_REVENUE")==null?0:
				ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_ALL_REVENUE")]);
			
			double AD_SERVER_DELIVERY_INDICATOR=0.0;
			String deliveryIndicator=csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")==null?"":
				line[csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")];
			if(LinMobileUtil.isNumeric(deliveryIndicator)){		
				AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(deliveryIndicator)*100));
				AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
			}
			
			//double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM= csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")==null?0: ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")]);
			
			double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
			String lineItemLvlImp=csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")==null?"":
				 line[csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
			if(LinMobileUtil.isNumeric(lineItemLvlImp)){
				AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(lineItemLvlImp)*100));
			}
			
			
			double AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM=csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM")==null?0:
					  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM")]);
			
			long AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS=csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")]);
			
			double AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=0;
			String adExchangeLineItemLvlCtr=csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR")==null?"":
				line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR")];
			
			if(LinMobileUtil.isNumeric(adExchangeLineItemLvlCtr)){
				AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=(((double)Double.parseDouble(adExchangeLineItemLvlCtr)*100));
			}	
			AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=Math.round((AD_EXCHANGE_LINE_ITEM_LEVEL_CTR*100.0))/100.0;
			long AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS=csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")==null?0:
			  StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")]);	
			
			double AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
			String adExchangeLineItemLvlPercentImp=csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")==null?"":
				line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
			
			if(LinMobileUtil.isNumeric(adExchangeLineItemLvlPercentImp)){
				AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(adExchangeLineItemLvlPercentImp)*100));
			}
			
			double AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE= csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE")==null?0:
			  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE")]);

										
		   if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Google Ad Exchange - AdEx (Remnant)")){
				AD_SERVER_CLICKS=AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS;
				AD_SERVER_IMPRESSIONS=AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS;
				AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS;
				AD_SERVER_CTR=AD_EXCHANGE_LINE_ITEM_LEVEL_CTR;
				AD_SERVER_WITH_CPD_AVERAGE_ECPM=AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM;
				AD_SERVER_ALL_REVENUE=AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE;								
			}

				   
			long requests=(long) ((AD_SERVER_IMPRESSIONS*100) /AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS);
			double fillRate=ReportUtil.calculateFillRate(AD_SERVER_IMPRESSIONS, requests);
			
									
			CorePerformanceReportObj rootObj=new CorePerformanceReportObj();	
			rootObj.setLoadTimestamp(loadTimestamp);
			if(DATE!=null){
				DATE=DateUtil.getFormatedDate(DATE, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");								
			}							
			rootObj.setDate(DATE);
			
			rootObj.setAdvertiserId(ADVERTISER_ID);
			rootObj.setAdvertiser(ADVERTISER_NAME);
			rootObj.setSiteId(AD_UNIT_ID_1);
			rootObj.setSiteName(AD_UNIT_NAME_1);
			rootObj.setSiteTypeId(AD_UNIT_ID_2);
			rootObj.setSiteType(AD_UNIT_NAME_2);
			rootObj.setZoneId(StringUtil.getLongValue(AD_UNIT_ID_3));
			rootObj.setZone(AD_UNIT_NAME_3);
			rootObj.setColumn5(AD_UNIT_ID_4);
			rootObj.setColumn6(AD_UNIT_NAME_4);
			
			rootObj.setTotalClicks(AD_SERVER_CLICKS);
			rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
			if(LINE_ITEM_COST_TYPE !=null && LINE_ITEM_COST_TYPE.equalsIgnoreCase("CPD")){
				rootObj.setClicksCPD(AD_SERVER_CLICKS);
				rootObj.setImpressionsCPD(AD_SERVER_IMPRESSIONS);
			}else{
				rootObj.setClicksCPM(AD_SERVER_CLICKS);
				rootObj.setImpressionsCPM(AD_SERVER_IMPRESSIONS);
			}
			rootObj.setRequests(requests);
			rootObj.setFillRate(fillRate);
			
			rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
			rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);
			rootObj.setRevenueCPD(AD_SERVER_CPD_REVENUE);
			
			rootObj.setCTR(AD_SERVER_CTR);							
			rootObj.setECPM(AD_SERVER_WITH_CPD_AVERAGE_ECPM);
			
			rootObj.setCreativeId(CREATIVE_ID);
			rootObj.setCreative(CREATIVE_NAME);
			rootObj.setCreativeType(CREATIVE_TYPE);
			rootObj.setCreativeSize(CREATIVE_SIZE);
			
			rootObj.setSalesPerson(SALESPERSON_NAME);
			
			rootObj.setOrderId(ORDER_ID);
			rootObj.setOrder(ORDER_NAME);
			rootObj.setOrdeLifetimeClicks(ORDER_LIFETIME_CLICKS);
			rootObj.setOrderEndDate(ORDER_END_DATE);
			rootObj.setOrderEndTime(ORDER_END_DATE_TIME);
			rootObj.setOrderLifetimeImpressions(ORDER_LIFETIME_IMPRESSIONS);
			rootObj.setOrderPONumber(ORDER_PO_NUMBER);
			rootObj.setOrderStartDate(ORDER_START_DATE);
			rootObj.setOrderStartTime(ORDER_START_DATE_TIME);
			rootObj.setAgency(ORDER_AGENCY);
			rootObj.setAgencyId(ORDER_AGENCY_ID);
			rootObj.setTrafficker(ORDER_TRAFFICKER);
			
			rootObj.setLineItem(LINE_ITEM_NAME);
			rootObj.setLineitemId(LINE_ITEM_ID);
			rootObj.setLineItemType(LINE_ITEM_TYPE);
			rootObj.setLineItemEndDate(LINE_ITEM_END_DATE);
			rootObj.setLineitemEndTime(LINE_ITEM_END_DATE_TIME);
			rootObj.setLineItemStartDate(LINE_ITEM_START_DATE);
			rootObj.setLineItemStartTime(LINE_ITEM_START_DATE_TIME);
			rootObj.setLineitemLifetimeClicks(LINE_ITEM_LIFETIME_CLICKS);
			rootObj.setLineitemLifetimeImpressions(LINE_ITEM_LIFETIME_IMPRESSIONS);
			
			rootObj.setContractedQty(LINE_ITEM_CONTRACTED_QUANTITY);
			rootObj.setGoalQty(LINE_ITEM_GOAL_QUANTITY);							
			rootObj.setCostType(LINE_ITEM_COST_TYPE);
			
			
			rootObj.setRate(rate);
			rootObj.setDeliveryIndicator(AD_SERVER_DELIVERY_INDICATOR);
			
			double calculatedBudget=ReportUtil.calculateBudget(LINE_ITEM_GOAL_QUANTITY,rate);
			rootObj.setBudget(calculatedBudget);
			
			return rootObj;
	 }
	 
    /*
	  * This method read a dfp raw file from specified cloud storage directory and 
	  * creates processed data list for 'CLIENT DEMO' and rejected data list (if faulted data found)
	  * for given networkCode
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName,String networkCode
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readClientDemoDFPRawCSVFromCloudStorage(
			 String fileName,String dirName,String networkCode,String publisherId,String publisherName,
			 String orderName, String advertiserName,String siteName, String lineItemName) {
		 
		    List<CorePerformanceReportObj> correctDataList=new ArrayList<CorePerformanceReportObj>();
		    List<CorePerformanceReportObj> faultedDataList=new ArrayList<CorePerformanceReportObj>();
		    boolean faultedData=false;
		    
		    Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		    
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
					CSVReader csvReader = new CSVReader(reader);
					List<String> lineItemList=new ArrayList<String>();
					if(corePerformanceReportColsList==null){
						corePerformanceReportColsList=new ArrayList<String>();
						corePerformanceReportColsList=Arrays.asList(corePerformanceReportCols);
					}
					
					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();					
					
					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...corePerformanceReportColsList:"+corePerformanceReportColsList.size());
							for(int i=0;i<line.length;i++){
								int index=corePerformanceReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
									//System.out.println(i+":"+line[i]);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
						}else{							
							CorePerformanceReportObj rootObj=getProcessedCorePerformanceRow(csvColumnMap, line, loadTimestamp);	
							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
							String lineItem=rootObj.getLineItem();							
							if(lineItem !=null && lineItemList.indexOf(lineItem) <0){
								lineItemList.add(lineItem);	
							}
							rootObj.setLineItem(lineItemName+" "+lineItemList.size());
							rootObj.setCreative(rootObj.getLineItem()+" | "+rootObj.getCreativeSize());
							
							rootObj.setPublisherId(Long.parseLong(publisherId));
							rootObj.setPublisherName(publisherName);
							rootObj.setAdvertiser(advertiserName);
							rootObj.setSiteName(siteName);
							rootObj.setOrder(orderName);
							rootObj.setAgency("Demo");
							
							if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){								
								rootObj.setChannelId(5);
								rootObj.setChannelName("Channel5");
								rootObj.setChannelType("National Sales");
								rootObj.setSalesType("Direct");	
								
							}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
								
								 String ORDER_PO_NUMBER=rootObj.getOrderPONumber();
								 String ADVERTISER_NAME=rootObj.getAdvertiser();
								 String LINE_ITEM_TYPE=rootObj.getLineItemType();
								 
								 if(ORDER_PO_NUMBER !=null 
											&& ( ORDER_PO_NUMBER.equalsIgnoreCase("Lin Mobile Partnership") 
											|| (ORDER_PO_NUMBER.equalsIgnoreCase("LIN Mobile Partnership") )
											|| (ORDER_PO_NUMBER.equalsIgnoreCase("Partnership") )
										)){
										if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Nexage")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.NEXAGE_CHANNEL_ID));
											rootObj.setChannelName("Channel2");
											rootObj.setChannelType(LinMobileConstants.NEXAGE_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.NEXAGE_SALES_TYPE);
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Mojiva")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.MOJIVA_CHANNEL_ID));
											rootObj.setChannelName("Channel3");
											rootObj.setChannelType(LinMobileConstants.MOJIVA_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.MOJIVA_SALES_TYPE);
										}else if( ADVERTISER_NAME !=null 
												&& ( ADVERTISER_NAME.equalsIgnoreCase("LIN | Undertone") 
														|| ADVERTISER_NAME.equalsIgnoreCase("LIN | Undertone (Ad network") ) ){
											rootObj.setChannelId(6);
											rootObj.setChannelName("Channel6");
											rootObj.setChannelType("Ad Network");
											rootObj.setSalesType("Non-direct");
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Google Ad Exchange - AdEx (Remnant)")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.AD_EXCHANGE_CHANNEL_ID));
											rootObj.setChannelName("Channel7");
											rootObj.setChannelType(LinMobileConstants.AD_EXCHANGE_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.AD_EXCHANGE_SALES_TYPE);
										}else if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | LSN")){
											rootObj.setChannelId(StringUtil.getLongValue(LinMobileConstants.LSN_CHANNEL_ID));
											rootObj.setChannelName("Channel10");
											rootObj.setChannelType(LinMobileConstants.LSN_CHANNEL_TYPE);
											rootObj.setSalesType(LinMobileConstants.LSN_SALES_TYPE);
										}else{
											faultedData=true;
										}
										
								}else if(ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("Lin Mobile Direct")){
									rootObj.setChannelId(5);									
									rootObj.setChannelName("Channel5");
									rootObj.setChannelType("National Sales");
									rootObj.setSalesType("Direct");
								}else if(ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("TEST Ads")){
									rootObj.setChannelId(9);
									rootObj.setChannelName("Channel9");
									rootObj.setChannelType("TEST Ads");
									rootObj.setSalesType("N/A");
								}else{
									if( (LINE_ITEM_TYPE !=null && (LINE_ITEM_TYPE.equalsIgnoreCase("HOUSE")) 
											|| ( ORDER_PO_NUMBER !=null && ORDER_PO_NUMBER.equalsIgnoreCase("HOUSE") ) ) ){
										rootObj.setChannelId(8);
										rootObj.setChannelName("Channel8");
										rootObj.setChannelType("House Ads");
										rootObj.setSalesType("In House");
									}else if(LINE_ITEM_TYPE !=null && (LINE_ITEM_TYPE.equalsIgnoreCase("STANDARD") || LINE_ITEM_TYPE.equalsIgnoreCase("SPONSORSHIP"))){
										rootObj.setChannelId(4);
										rootObj.setChannelName("Channel4");
										rootObj.setChannelType("Local Sales");
										rootObj.setSalesType("Direct");									
									}else{
										faultedData=true;
									}
								}
												    
							}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
								rootObj.setChannelId(4);							
								rootObj.setChannelName("Channel4");
								rootObj.setChannelType("Local Sales");
								rootObj.setSalesType("Direct");	
							}
							
							rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
							
							if(faultedData){
								faultedDataList.add(rootObj);
								faultedData=false;  // reset it to false after add in list
							}else{
								faultedData=false;
								correctDataList.add(rootObj);
							}
													    					    
						
					  }
					  count++;
				    }
					
					allDataMap.put("CorrectData", correctDataList);
					allDataMap.put("InCorrectData", faultedDataList);
					csvReader.close();
				    readChannel.close();
				    
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
				
			}
			return allDataMap;
	 }
	 
	 
	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list for PerformanceByLocation report
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<LocationReportObj> 
	  * @param String fileName,String dirName,String networkCode
	  */
	 public static List<LocationReportObj> readClientDemoDFPReportByLocationCSVFromCloudStorage(String fileName,
			 String dirName,String networkCode,String publisherId,String publisherName,
			 String orderName, String advertiserName,String siteName, String lineItemName) { 
		    List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		   
		    log.info("Reading from google cloud storage,fileName:"+fileName);
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    int count=0; 
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
					if(locationReportColsList==null){
						locationReportColsList=new ArrayList<String>();
						locationReportColsList=Arrays.asList(locationReportCols);
					}
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
					
					CSVReader csvReader = new CSVReader(reader);
					List<String> lineItemList=new ArrayList<String>();
					
					List<String[]> allElements = csvReader.readAll();
					log.info("Total rows in csv file :"+allElements.size());
					String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
										
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...locationReportColsList:"+locationReportColsList.size());
							for(int i=0;i<line.length;i++){
								int index=locationReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
						}else{
						if(line.length >= 38){	
							
							String DATE=line[0];
							long ADVERTISER_ID=StringUtil.getLongValue(line[1]);
							String ADVERTISER_NAME=line[2];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}
							
							long ORDER_ID=StringUtil.getLongValue(line[3]);
							String ORDER_NAME=line[4];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(","," ");
							}
							
							long LINE_ITEM_ID=StringUtil.getLongValue(line[5]);
							String LINE_ITEM_NAME=line[6];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
							}
							String LINE_ITEM_TYPE=line[7];
							long COUNTRY_ID=StringUtil.getLongValue(line[8]);
							long REGION_ID=StringUtil.getLongValue(line[9]);
							long CITY_ID=StringUtil.getLongValue(line[10]);
							String COUNTRY_NAME=line[11];
							if(COUNTRY_NAME!=null ){
								COUNTRY_NAME=COUNTRY_NAME.replaceAll(","," ");
							}
							String REGION_NAME=line[12];
							if(REGION_NAME!=null ){
								REGION_NAME=REGION_NAME.replaceAll(","," ");
							}
							String CITY_NAME=line[13];
							if(CITY_NAME!=null ){
								CITY_NAME=CITY_NAME.replaceAll(","," ");
							}
							
							String goalQty=line[14];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);
							
						    //long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[15]);
						    
						    String LINE_ITEM_COST_PER_UNIT=line[16];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);
						   
						    String LINE_ITEM_COST_TYPE=line[17];
						    long LINE_ITEM_LIFETIME_CLICKS=StringUtil.getLongValue(line[18]);
						    long LINE_ITEM_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[19]);
						    
						    String LINE_ITEM_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(line[20],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;
						    
						    String lineItemEndDate=line[21];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							 }
						    
						    String ORDER_AGENCY=line[22];
						    long ORDER_AGENCY_ID=StringUtil.getLongValue(line[23]);
						    long ORDER_LIFETIME_CLICKS=StringUtil.getLongValue(line[24]);
						    long ORDER_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[25]);
						    String ORDER_PO_NUMBER=line[26];
						    
						    String orderStartDate=line[27];
						    String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    String ORDER_START_DATE=ORDER_START_DATE_TIME;
						    
						    String orderEndDate=line[28];
						    String ORDER_END_DATE_TIME="";
						    String ORDER_END_DATE="";
						    if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
						    	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
								 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}
						   
						   // String ORDER_TRAFFICKER=line[29];
						    
							
							long AD_SERVER_IMPRESSIONS=StringUtil.getLongValue(line[30]);
							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[31]);
							double AD_SERVER_WITH_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[32]);
							double AD_SERVER_CTR=0;
							String strStr=line[33];
							if(LinMobileUtil.isNumeric(strStr)){
								AD_SERVER_CTR=(((double)Double.parseDouble(strStr)*100));
							}
							
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
							double AD_SERVER_CPM_AND_CPC_REVENUE=ReportUtil.convertMoney(line[34]);	
							double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[35]);
							double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[36]);							
							//double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[37]);							
							
							
							
							LocationReportObj rootObj=new LocationReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(DATE);
							
							rootObj.setAdvertiserId(ADVERTISER_ID);
														
							rootObj.setCountryCriteriaId(COUNTRY_ID);
							rootObj.setRegionCriteriaId(REGION_ID);
							rootObj.setCityCriteriaId(CITY_ID);
							rootObj.setCountryName(COUNTRY_NAME);
							rootObj.setRegionName(REGION_NAME);
							rootObj.setCityName(CITY_NAME);
							
							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
														
							rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
							rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);
							rootObj.setRevenueCPD(AD_SERVER_CPD_REVENUE);
							
							rootObj.setCTR(AD_SERVER_CTR);							
							rootObj.setECPM(AD_SERVER_WITH_CPD_AVERAGE_ECPM);
							
							
							
							rootObj.setOrderId(ORDER_ID);							
							rootObj.setOrdeLifetimeClicks(ORDER_LIFETIME_CLICKS);
							rootObj.setOrderEndDate(ORDER_END_DATE);
							rootObj.setOrderEndTime(ORDER_END_DATE_TIME);
							rootObj.setOrderLifetimeImpressions(ORDER_LIFETIME_IMPRESSIONS);
							rootObj.setOrderPONumber(ORDER_PO_NUMBER);
							rootObj.setOrderStartDate(ORDER_START_DATE);
							rootObj.setOrderStartTime(ORDER_START_DATE_TIME);
							
							rootObj.setAgencyId(ORDER_AGENCY_ID);
							
							rootObj.setAdvertiser(ADVERTISER_NAME);
							rootObj.setOrder(ORDER_NAME);
							rootObj.setLineItem(LINE_ITEM_NAME);
							rootObj.setAgency(ORDER_AGENCY);
							
							rootObj.setLineitemId(LINE_ITEM_ID);
							rootObj.setLineItemType(LINE_ITEM_TYPE);
							rootObj.setLineItemEndDate(LINE_ITEM_END_DATE);
							rootObj.setLineitemEndTime(LINE_ITEM_END_DATE_TIME);
							rootObj.setLineItemStartDate(LINE_ITEM_START_DATE);
							rootObj.setLineItemStartTime(LINE_ITEM_START_DATE_TIME);
							rootObj.setLineitemLifetimeClicks(LINE_ITEM_LIFETIME_CLICKS);
							rootObj.setLineitemLifetimeImpressions(LINE_ITEM_LIFETIME_IMPRESSIONS);
							
							rootObj.setGoalQty(LINE_ITEM_GOAL_QUANTITY);							
							rootObj.setCostType(LINE_ITEM_COST_TYPE);
							
							rootObj.setRate(rate);										
							
							rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
							rootObj.setPublisherName(publisherName);
							rootObj.setDataSource("DFP");
							
							// apply client demo settings...
							String lineItem=rootObj.getLineItem();							
							if(lineItem !=null && lineItemList.indexOf(lineItem) <0){
								lineItemList.add(lineItem);	
							}
							rootObj.setLineItem(lineItemName+" "+lineItemList.size());
							
							rootObj.setPublisherId(Long.parseLong(publisherId));
							rootObj.setPublisherName(publisherName);
							rootObj.setAdvertiser(advertiserName);
							rootObj.setAgency("Demo");
							//rootObj.setSiteName(siteName);
							rootObj.setOrder(orderName);
							// ended client demo settings...
							
							reportList.add(rootObj);													    					    
						}						
					  }
					  count++;
				    }					
					csvReader.close();
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage()+" : line count:"+count);
				
				
			}
			return reportList;
	 }
	 
	 /*
	  * This method read CLIENT DEMO target data file from specified cloud storage directory and 
	  * creates processed data list
	  * for given networkCode (ex, Lin Media (DFP account), Lin Digital etc...)
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return  List<DFPTargetReportObj>
	  * 
	  * @param String fileName,String dirName,String networkCode,String publisherId,String publisherName
	  */
	 public static List<DFPTargetReportObj> readClientDemoDFPTargetRawCSV(String fileName,
			 String dirName,String networkCode,String publisherId,String publisherName,
			 String orderName, String advertiserName,String siteName, String lineItemName) {
		 
		    List<DFPTargetReportObj> correctDataList=new ArrayList<DFPTargetReportObj>();
		    
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    		    
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
					List<String> lineItemList=new ArrayList<String>();
					
					CSVReader csvReader = new CSVReader(reader);
					if(targetReportColsList==null){
						targetReportColsList=new ArrayList<String>();
						targetReportColsList=Arrays.asList(targetReportCols);
					}
					
					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
					int count=0;	
					int maxCSVCol=16;
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...targetReportColsList:"+targetReportColsList.size());
							for(int i=0;i<line.length;i++){
								int index=targetReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
							
							
						}else{
						if(line.length >= maxCSVCol){
							String DATE=line[csvColumnMap.get("Dimension.DATE")];
							long ADVERTISER_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ADVERTISER_ID")]);
							String ADVERTISER_NAME=line[csvColumnMap.get("Dimension.ADVERTISER_NAME")];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}
							
							long ORDER_ID=
									StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ORDER_ID")]);
							String ORDER_NAME=line[csvColumnMap.get("Dimension.ORDER_NAME")];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(","," ");
							}
							
							long LINE_ITEM_ID=
									StringUtil.getLongValue(line[csvColumnMap.get("Dimension.LINE_ITEM_ID")]);
							String LINE_ITEM_NAME=line[csvColumnMap.get("Dimension.LINE_ITEM_NAME")];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
							}
							String LINE_ITEM_TYPE=line[csvColumnMap.get("Dimension.LINE_ITEM_TYPE")];							
							String GENERIC_CRITERION_NAME=
									line[csvColumnMap.get("Dimension.GENERIC_CRITERION_NAME")];
							//long CRITERION_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.CRITERION_ID")]);
												
							long AD_SERVER_IMPRESSIONS=
									StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);
							long AD_SERVER_CLICKS=
									StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_CLICKS")]);
							double AD_SERVER_CTR=0;
							String ctrStr=line[csvColumnMap.get("Column.AD_SERVER_CTR")];
							if(LinMobileUtil.isNumeric(ctrStr)){
								AD_SERVER_CTR=(((double)Double.parseDouble(ctrStr)*100));
							}							
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
							
							double AD_SERVER_CPM_AND_CPC_REVENUE=
									ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")]);
							double AD_SERVER_ALL_REVENUE=AD_SERVER_CPM_AND_CPC_REVENUE;	
							
							double AD_SERVER_DELIVERY_INDICATOR=0.0;
							String deliveryIndicatorStr=
									line[csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")];
							if(LinMobileUtil.isNumeric(deliveryIndicatorStr)){								
								AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(deliveryIndicatorStr)*100));
								AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
							}
							double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							String lineItemLevelPercentImpStr=
									line[csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
							if(LinMobileUtil.isNumeric(lineItemLevelPercentImpStr)){
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(lineItemLevelPercentImpStr)*100));
							}
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=
									ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")]);
							
							
							
							long requests=(long) ((AD_SERVER_IMPRESSIONS*100) /AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS);
							double fillRate=ReportUtil.calculateFillRate(AD_SERVER_IMPRESSIONS, requests);
							
													
							DFPTargetReportObj rootObj=new DFPTargetReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							if(DATE!=null){
								DATE=DateUtil.getFormatedDate(DATE, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");								
							}							
							rootObj.setDate(DATE);							
							rootObj.setAdvertiserId(ADVERTISER_ID);
							rootObj.setAdvertiser(ADVERTISER_NAME);
							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);							
							
							if(GENERIC_CRITERION_NAME !=null){
								String [] target=GENERIC_CRITERION_NAME.split("=");
								if(target !=null && target.length==2){
									rootObj.setTargetCategory(target[0]);
									if(target[1] !=null){
										target[1]=target[1].replaceAll(",", "_");
									}
									rootObj.setTargetValue(target[1]);
								}								
							}
							rootObj.setRequests(requests);
							rootObj.setFillRate(fillRate);
							rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
							rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);
							rootObj.setCTR(AD_SERVER_CTR);							
							rootObj.setECPM(AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM); //AD_SERVER_WITH_CPD_AVERAGE_ECPM
							rootObj.setOrderId(ORDER_ID);
							rootObj.setOrder(ORDER_NAME);
							rootObj.setLineItem(LINE_ITEM_NAME);
							rootObj.setLineItemId(LINE_ITEM_ID);
							rootObj.setLineItemType(LINE_ITEM_TYPE);							
							rootObj.setDeliveryIndicator(AD_SERVER_DELIVERY_INDICATOR);
														
							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
							
							
							// apply client demo settings...
							String lineItem=rootObj.getLineItem();							
							if(lineItem !=null && lineItemList.indexOf(lineItem) <0){
								lineItemList.add(lineItem);	
							}
							rootObj.setLineItem(lineItemName+" "+lineItemList.size());
							
							
							rootObj.setPublisherId(Long.parseLong(publisherId));
							rootObj.setPublisherName(publisherName);
							rootObj.setAdvertiser(advertiserName);							
							//rootObj.setSiteName(siteName);
							rootObj.setOrder(orderName);
							// ended client demo settings...
							
							correctDataList.add(rootObj);
							
													    					    
						}else{
							log.warning("Invalid CSV file, columns:"+line.length+", please provide "+maxCSVCol+" columns csv file.");
						}
					  }
					  count++;
				    }					
					csvReader.close();
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
				
			}
			return correctDataList;
	 }
	 
	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed rich media custom event data for CLIENT DEMO
	  *  
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<RichMediaCustomEventReportObj>
	  * 
	  * @param String fileName,String dirName,String dataSource,String networkCode,
			 String orderName, String advertiserName,String siteName, String lineItemName
	  */
	 public static List<RichMediaCustomEventReportObj> processClientDemoRichMediaCustomEventRawCSVFromCloudStorage(
			 String fileName,String dirName,String dataSource,String networkCode,String publisherId,String publisherName,
			 String orderName, String advertiserName,String siteName, String lineItemName) { 
		    List<RichMediaCustomEventReportObj> dataList=new ArrayList<RichMediaCustomEventReportObj>();
		   
		    log.info("Reading from google cloud storage,fileName:"+fileName);
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
					List<String> lineItemList=new ArrayList<String>();
					CSVReader csvReader = new CSVReader(reader);
					if(customEventReportColsList==null){
						customEventReportColsList=new ArrayList<String>();
						customEventReportColsList=Arrays.asList(customEventReportCols);
					}
					
					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
					int count=0;	
					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...:");
							for(int i=0;i<line.length;i++){
								int index=customEventReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
							
							
						}else{							
							String DATE=line[csvColumnMap.get("Dimension.DATE")];
							long ADVERTISER_ID=
									StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ADVERTISER_ID")]);
							String ADVERTISER_NAME=line[csvColumnMap.get("Dimension.ADVERTISER_NAME")];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(",","|");
							}							
						
							String ORDER_ID=line[csvColumnMap.get("Dimension.ORDER_ID")];
							String ORDER_NAME=line[csvColumnMap.get("Dimension.ORDER_NAME")];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(",","|");
							}							
							
							String LINE_ITEM_ID=line[csvColumnMap.get("Dimension.LINE_ITEM_ID")];
							String LINE_ITEM_NAME=line[csvColumnMap.get("Dimension.LINE_ITEM_NAME")];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(",","|");
							}
							String LINE_ITEM_TYPE=line[csvColumnMap.get("Dimension.LINE_ITEM_TYPE")];
							String CREATIVE_ID=line[csvColumnMap.get("Dimension.CREATIVE_ID")];
							String CREATIVE_NAME=line[csvColumnMap.get("Dimension.CREATIVE_NAME")];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(",","|");
							}
							String CREATIVE_SIZE=line[csvColumnMap.get("Dimension.CREATIVE_SIZE")];
							String CREATIVE_TYPE=line[csvColumnMap.get("Dimension.CREATIVE_TYPE")];
							
							String CUSTOM_EVENT_ID=csvColumnMap.get("Dimension.CUSTOM_EVENT_ID")==null?"":line[csvColumnMap.get("Dimension.CUSTOM_EVENT_ID")];
							String CUSTOM_EVENT_NAME=csvColumnMap.get("Dimension.CUSTOM_EVENT_NAME")==null?"":line[csvColumnMap.get("Dimension.CUSTOM_EVENT_NAME")];
							String CUSTOM_EVENT_TYPE=csvColumnMap.get("Dimension.CUSTOM_EVENT_TYPE")==null?"":line[csvColumnMap.get("Dimension.CUSTOM_EVENT_TYPE")];
							
													
						    String orderStartDate=line[csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")];
							String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						    
							String ORDER_START_DATE=ORDER_START_DATE_TIME;
							
							String LINE_ITEM_START_DATE_TIME=
									DateUtil.getFormatedDateUsingJodaLib(
											line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")],
											"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;
							    
							String orderEndDate=line[csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")];
							String ORDER_END_DATE_TIME="";
							String ORDER_END_DATE="";
							if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
							 	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						
							  	 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}
							
							String lineItemEndDate=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");							    
						    	LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							}
						    
						    String ORDER_AGENCY=line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")];
						    long ORDER_AGENCY_ID=csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")==null?0:
						    		StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")]);
						    
							String goalQty=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);				   
						    
						    String LINE_ITEM_COST_PER_UNIT=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);						    
						    						    
						   
						    long RICH_MEDIA_CUSTOM_EVENT_COUNT=csvColumnMap.get("Column.RICH_MEDIA_CUSTOM_EVENT_COUNT")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_CUSTOM_EVENT_COUNT")]);
						    long RICH_MEDIA_CUSTOM_EVENT_TIME=csvColumnMap.get("Column.RICH_MEDIA_CUSTOM_EVENT_TIME")==null?0:
						    		StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_CUSTOM_EVENT_TIME")]);
						    
						    
						    RichMediaCustomEventReportObj rootObj=new RichMediaCustomEventReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							if(DATE!=null){
								DATE=DateUtil.getFormatedDate(DATE, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");								
							}							
							rootObj.setDate(DATE);							
							rootObj.setAdvertiserId(ADVERTISER_ID);
							rootObj.setAdvertiser(ADVERTISER_NAME);
							
							rootObj.setCreativeId(CREATIVE_ID);
							rootObj.setCreative(CREATIVE_NAME);
							rootObj.setCreativeType(CREATIVE_TYPE);
							rootObj.setCreativeSize(CREATIVE_SIZE);
							rootObj.setOrderId(ORDER_ID);
							rootObj.setOrder(ORDER_NAME);
							
							rootObj.setOrderEndDate(ORDER_END_DATE);
							rootObj.setOrderStartDate(ORDER_START_DATE);							
							rootObj.setAgency(ORDER_AGENCY);
							rootObj.setAgencyId(ORDER_AGENCY_ID);
							rootObj.setLineItem(LINE_ITEM_NAME);
							rootObj.setLineItemId(LINE_ITEM_ID);
							rootObj.setLineItemType(LINE_ITEM_TYPE);
							rootObj.setLineItemEndDate(LINE_ITEM_END_DATE);							
							rootObj.setLineItemStartDate(LINE_ITEM_START_DATE);							
							rootObj.setGoalQuantity(LINE_ITEM_GOAL_QUANTITY);
							rootObj.setRate(rate);
							rootObj.setCustomConversionCountValue(RICH_MEDIA_CUSTOM_EVENT_COUNT);
							rootObj.setCustomConversionTimeValue(RICH_MEDIA_CUSTOM_EVENT_TIME);
							
							rootObj.setCustomEventId(StringUtil.getLongValue(CUSTOM_EVENT_ID));
							rootObj.setCustomEvent(CUSTOM_EVENT_NAME);
							rootObj.setCustomEventType(CUSTOM_EVENT_TYPE);
							
							String market="";
							String campaignCategory="";
							
							rootObj.setMarket(market);
							rootObj.setCampaignCategory(campaignCategory);
							
                            rootObj.setDataSource(dataSource);	
                            
                            if(publisherId !=null && publisherName !=null){
                            	rootObj.setChannelId(5);
                            	rootObj.setChannelName("Channel5");
    							rootObj.setChannelType("National Sales");
    							rootObj.setSalesType("Direct");	
                            }else{
                            	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
                                	rootObj.setChannelId(5);
        							rootObj.setChannelName("Channel5");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
        							
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
                                	rootObj.setChannelId(5);
                                	rootObj.setChannelName("Channel5");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
                                	rootObj.setChannelId(4);
        							rootObj.setChannelName("Channel4");
        							rootObj.setChannelType("Local Sales");
        							rootObj.setSalesType("Direct");	
                                }
                            }
                            

							// apply client demo settings...
							String lineItem=rootObj.getLineItem();							
							if(lineItem !=null && lineItemList.indexOf(lineItem) <0){
								lineItemList.add(lineItem);	
							}
							rootObj.setLineItem(lineItemName+" "+lineItemList.size());
							rootObj.setCreative(rootObj.getLineItem()+" | "+CREATIVE_SIZE);
							
							rootObj.setPublisherId(Long.parseLong(publisherId));
							rootObj.setPublisherName(publisherName);
							rootObj.setAdvertiser(advertiserName);	
							rootObj.setAgency("Demo");
							rootObj.setSiteName(siteName);
							rootObj.setOrder(orderName);
							// ended client demo settings...
							
														
							dataList.add(rootObj);    					    
						
					  }
					  count++;
				    }
					csvReader.close();
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
				
			}
			return dataList;
	 }
	 
	 
	 /*
	  *  
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * 
	  */
	 public static List<ProductPerformanceReportObj> readProductPerformanceCSVFromCloudStorage(String fileName,String dirName,
			 String networkCode,String bucketName) { 
		    List<ProductPerformanceReportObj> reportList=new ArrayList<ProductPerformanceReportObj>();
		    log.info("Reading raw file and creating process report (productperformance) for Network code:"+networkCode);

		    
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + bucketName + "/"+dirName+"/"+ fileName;
		   
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    int count=0; 
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));

					if(productPerformanceReportColsList==null){
						productPerformanceReportColsList=new ArrayList<String>();
						productPerformanceReportColsList=Arrays.asList(productPerformanceReportCols);
					}
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();
					log.info("Total rows in csv file :"+allElements.size());
					String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");

					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row, productPerformanceReportColsList:"+productPerformanceReportColsList.size());
							for(int i=0;i<line.length;i++){
								int index=productPerformanceReportColsList.indexOf(line[i]);
								if(index >=0){
									csvColumnMap.put(line[i], i);
								}else{
									log.warning("This column not found in list :"+line[i]+" : Please update column list");
								}
							}
							log.info("Found cloumns in csv:"+csvColumnMap.size());
						}else{
						
							String adUnitId1=csvColumnMap.get("Ad unit ID 1")==null?"0":line[csvColumnMap.get("Ad unit ID 1")];
							String adUnitId2=csvColumnMap.get("Ad unit ID 2")==null?"0":line[csvColumnMap.get("Ad unit ID 2")];
							String adUnitId3=csvColumnMap.get("Ad unit ID 3")==null?"0":line[csvColumnMap.get("Ad unit ID 3")];
							String adUnitId4=csvColumnMap.get("Ad unit ID 4")==null?"0":line[csvColumnMap.get("Ad unit ID 4")];
							String adUnitId5=csvColumnMap.get("Ad unit ID 5")==null?"0":line[csvColumnMap.get("Ad unit ID 5")];
							String adUnit1=csvColumnMap.get("Ad unit 1")==null?"N/A":line[csvColumnMap.get("Ad unit 1")];							
							String adUnit2=csvColumnMap.get("Ad unit 2")==null?"N/A":line[csvColumnMap.get("Ad unit 2")];
							String adUnit3=csvColumnMap.get("Ad unit 3")==null?"N/A":line[csvColumnMap.get("Ad unit 3")];
							String adUnit4=csvColumnMap.get("Ad unit 4")==null?"N/A":line[csvColumnMap.get("Ad unit 4")];
							String adUnit5=csvColumnMap.get("Ad unit 5")==null?"N/A":line[csvColumnMap.get("Ad unit 5")];
							
							String COUNTRY_CRITERIA_ID=csvColumnMap.get("Dimension.COUNTRY_CRITERIA_ID")==null?"0":line[csvColumnMap.get("Dimension.COUNTRY_CRITERIA_ID")];
							String REGION_CRITERIA_ID =csvColumnMap.get("Dimension.REGION_CRITERIA_ID")==null?"0":line[csvColumnMap.get("Dimension.REGION_CRITERIA_ID")];
							String CITY_CRITERIA_ID=csvColumnMap.get("Dimension.CITY_CRITERIA_ID")==null?"0":line[csvColumnMap.get("Dimension.CITY_CRITERIA_ID")];
							String COUNTRY_NAME=csvColumnMap.get("Dimension.COUNTRY_NAME")==null?"":line[csvColumnMap.get("Dimension.COUNTRY_NAME")];
							String REGION_NAME=csvColumnMap.get("Dimension.REGION_NAME")==null?"":line[csvColumnMap.get("Dimension.REGION_NAME")];
							String CITY_NAME=csvColumnMap.get("Dimension.CITY_NAME")==null?"":line[csvColumnMap.get("Dimension.CITY_NAME")];
							String adServerImpressions=csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")==null?"0":line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")];
							String adServerClicks=csvColumnMap.get("Column.AD_SERVER_CLICKS")==null?"0":line[csvColumnMap.get("Column.AD_SERVER_CLICKS")];
							
							
						
							long COUNTRY_ID=StringUtil.getLongValue(COUNTRY_CRITERIA_ID);
							long REGION_ID=StringUtil.getLongValue(REGION_CRITERIA_ID);
							long CITY_ID=StringUtil.getLongValue(CITY_CRITERIA_ID);
							
							if(COUNTRY_NAME!=null ){
								COUNTRY_NAME=COUNTRY_NAME.replaceAll(","," ");
							}
							
							if(REGION_NAME!=null ){
								REGION_NAME=REGION_NAME.replaceAll(","," ");
							}
							
							if(CITY_NAME!=null ){
								CITY_NAME=CITY_NAME.replaceAll(","," ");
							}
						/*	String canonicalPath="";
							String adUnitId="0";
							String adUnitName="";
							
	  		 				if(adUnitId5 !=null &&  !(adUnitId5.equals("-0") || adUnitId5.equals("0") || adUnitId5.equals("-"))){
	  		 					adUnitId=adUnitId5;
	  		 					adUnitName=adUnit5;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4+" > "+adUnit5;	  		 					
	  		 				}else if(adUnitId4 !=null &&  !(adUnitId4.equals("-0") || adUnitId4.equals("0") || adUnitId4.equals("-"))){
	  		 					adUnitId=adUnitId4;
	  		 					adUnitName=adUnit4;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4;
	  		 				}else if(adUnitId3 !=null &&  !(adUnitId3.equals("-0") || adUnitId3.equals("0")|| adUnitId3.equals("-"))){
	  		 					adUnitId=adUnitId3;
	  		 					adUnitName=adUnit3;
	  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3;
	  		 				}else if(adUnitId2 !=null &&  !(adUnitId2.equals("-0") || adUnitId2.equals("0") || adUnitId2.equals("-"))){
	  		 					adUnitId=adUnitId2;
	  		 					adUnitName=adUnit2;
	  		 					canonicalPath=adUnit1+" > "+adUnit2;
	  		 				}else if(adUnitId1 !=null &&  !(adUnitId1.equals("-0") || adUnitId1.equals("0") || adUnitId1.equals("-"))){
	  		 					adUnitId=adUnitId1;
	  		 					adUnitName=adUnit1;
	  		 					canonicalPath=adUnit1;	
	  		 				}*/
	  		 				
							ProductPerformanceReportObj rootObj=new ProductPerformanceReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							
							rootObj.setCountryId(COUNTRY_ID);
							rootObj.setRegionId(REGION_ID);
							rootObj.setCityId(CITY_ID);
							rootObj.setCountryName(COUNTRY_NAME);
							rootObj.setRegionName(REGION_NAME);
							rootObj.setCityName(CITY_NAME);						
							rootObj.setNetworkCode(networkCode);
							rootObj.setAdUnitId1(StringUtil.getLongValue(adUnitId1));
							rootObj.setAdUnitId2(StringUtil.getLongValue(adUnitId2));
							rootObj.setAdUnitId3(StringUtil.getLongValue(adUnitId3));
							rootObj.setAdUnitId4(StringUtil.getLongValue(adUnitId4));
							rootObj.setAdUnitId5(StringUtil.getLongValue(adUnitId5));
							rootObj.setAdUnit1(adUnit1);
							rootObj.setAdUnit2(adUnit2);
							rootObj.setAdUnit3(adUnit3);
							rootObj.setAdUnit4(adUnit4);
							rootObj.setAdUnit5(adUnit5);							
							rootObj.setImpressions(StringUtil.getLongValue(adServerImpressions));
							rootObj.setClicks(StringUtil.getLongValue(adServerClicks));
							reportList.add(rootObj);	
					  }
					  count++;
				    }		
					csvReader.close();
				    readChannel.close();

			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				

			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				

			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage()+" : line count:"+count);
				

			}
			return reportList;
	 }

}
