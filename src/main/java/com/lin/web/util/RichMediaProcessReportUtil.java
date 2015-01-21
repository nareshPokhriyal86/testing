package com.lin.web.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.google.api.ads.dfp.jaxws.v201403.Column;
import com.google.api.ads.dfp.jaxws.v201403.Dimension;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.LockException;
import com.lin.server.bean.RichMediaCommonReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;


/*
 * This is a part of Cloud Storage Util, which will read raw file (For Rich Media only) from cloud storage
 *  and process that file.
 *  
 *  @author Youdhveer Panwar
 */
@SuppressWarnings("unused")
public class RichMediaProcessReportUtil {

	private static final Logger log = Logger.getLogger(RichMediaProcessReportUtil.class.getName());
	
	private static List<String> customEventReportColsList=null;
	static final String [] customEventReportCols={
		"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.CREATIVE_ID","Dimension.CREATIVE_NAME","Dimension.CREATIVE_SIZE","Dimension.CREATIVE_TYPE","Dimension.CUSTOM_EVENT_ID","Dimension.CUSTOM_EVENT_NAME","Dimension.CUSTOM_EVENT_TYPE","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","Column.RICH_MEDIA_CUSTOM_EVENT_COUNT","Column.RICH_MEDIA_CUSTOM_EVENT_TIME"
	};
	
	private static List<String> videoCampaignReportColsList=null;
	static final String [] videoCampaignReportCols={
		"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.CREATIVE_ID","Dimension.CREATIVE_NAME","Dimension.CREATIVE_SIZE","Dimension.CREATIVE_TYPE",/*"Dimension.CUSTOM_EVENT_ID","Dimension.CUSTOM_EVENT_NAME","Dimension.CUSTOM_EVENT_TYPE",*/
		"DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT",
		"Column.RICH_MEDIA_BACKUP_IMAGES","Column.RICH_MEDIA_DISPLAY_TIME","Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME","Column.RICH_MEDIA_EXPANSIONS","Column.RICH_MEDIA_EXPANDING_TIME","Column.RICH_MEDIA_INTERACTION_TIME","Column.RICH_MEDIA_INTERACTION_COUNT","Column.RICH_MEDIA_INTERACTION_RATE","Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME","Column.RICH_MEDIA_INTERACTION_IMPRESSIONS","Column.RICH_MEDIA_MANUAL_CLOSES","Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS","Column.RICH_MEDIA_VIDEO_INTERACTIONS","Column.RICH_MEDIA_VIDEO_INTERACTION_RATE","Column.RICH_MEDIA_VIDEO_MUTES","Column.RICH_MEDIA_VIDEO_PAUSES","Column.RICH_MEDIA_VIDEO_PLAYES","Column.RICH_MEDIA_VIDEO_MIDPOINTS","Column.RICH_MEDIA_VIDEO_COMPLETES","Column.RICH_MEDIA_VIDEO_REPLAYS","Column.RICH_MEDIA_VIDEO_STOPS","Column.RICH_MEDIA_VIDEO_UNMUTES","Column.RICH_MEDIA_VIDEO_VIEW_RATE","Column.RICH_MEDIA_VIDEO_VIEW_TIME","Column.VIDEO_INTERACTION_START","Column.VIDEO_INTERACTION_FIRST_QUARTILE","Column.VIDEO_INTERACTION_MIDPOINT","Column.VIDEO_INTERACTION_THIRD_QUARTILE","Column.VIDEO_INTERACTION_COMPLETE","Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE","Column.VIDEO_INTERACTION_COMPLETION_RATE","Column.VIDEO_INTERACTION_ERROR_COUNT","Column.VIDEO_INTERACTION_VIDEO_LENGTH","Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN","Column.VIDEO_INTERACTION_ENGAGED_VIEW","Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE","Column.VIDEO_INTERACTION_PAUSE","Column.VIDEO_INTERACTION_RESUME","Column.VIDEO_INTERACTION_REWIND","Column.VIDEO_INTERACTION_MUTE","Column.VIDEO_INTERACTION_UNMUTE","Column.VIDEO_INTERACTION_COLLAPSE","Column.VIDEO_INTERACTION_EXPAND","Column.VIDEO_INTERACTION_FULL_SCREEN","Column.VIDEO_INTERACTION_VIDEO_SKIPS","Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE","Column.VIDEO_INTERACTION_VIEW_RATE"
	};
	    
	/*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed rich media custom event data
	  *  
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<RichMediaCustomEventReportObj>
	  * 
	  * @param String fileName,String dirName,String dataSource
	  */
	 public static List<RichMediaCustomEventReportObj> processRichMediaCustomEventRawCSVFromCloudStorage(
			 String fileName,String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName) { 
		    List<RichMediaCustomEventReportObj> dataList=new ArrayList<RichMediaCustomEventReportObj>();
		   
		    log.info("Reading from google cloud storage,fileName:"+fileName);
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + bucketName + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
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
							/*if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("DC")>=0){
								market="DC";
							}else if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("SF")>=0){
								market="SF";
							}	*/						
							
							rootObj.setMarket(market);
							rootObj.setCampaignCategory(campaignCategory);
							
                            rootObj.setDataSource(dataSource);	
                            
                            if(publisherId !=null && publisherName !=null){
                            	rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
    							rootObj.setPublisherName(publisherName);    							
    							rootObj.setChannelId(5);
    							rootObj.setChannelName("National Sales Direct");
    							rootObj.setChannelType("National Sales");
    							rootObj.setSalesType("Direct");	
                            }else{
                            	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);    							
        							rootObj.setChannelId(5);
        							rootObj.setChannelName("National Sales Direct");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
        							
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
        							rootObj.setChannelId(5);
        							rootObj.setChannelName("National Sales Direct");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
        							rootObj.setChannelId(4);
        							rootObj.setChannelName("Local Sales Direct");
        							rootObj.setChannelType("Local Sales");
        							rootObj.setSalesType("Direct");	
                                }
                            }
                            
														
							dataList.add(rootObj);    					    
						
					  }
					  count++;
				    }
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				e.printStackTrace();
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				e.printStackTrace();
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				e.printStackTrace();
				
			}
			return dataList;
	 }
	 
	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed rich media video campaign data
	  *  
	  * @author Naresh Pokhriyal(naresh.pokhriyal@mediaagility.com)
	  * 
	  * @return List<RichMediaCommonReportObj>
	  * 
	  * @param String fileName,String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName
	  */
	 public static List<RichMediaCommonReportObj> processRichMediaVideoCampaignRawCSVFromCloudStorage(
			 String fileName,String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName) { 
		    List<RichMediaCommonReportObj> dataList=new ArrayList<RichMediaCommonReportObj>();
		   
		    log.info("Reading from google cloud storage,fileName:"+fileName);
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + bucketName + "/"+dirName+"/"+ fileName;
		    log.info("Reading from google cloud storage: filename:"+filename);
		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    
		    String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				    
					CSVReader csvReader = new CSVReader(reader);
					if(videoCampaignReportColsList==null){
						videoCampaignReportColsList=new ArrayList<String>();
						videoCampaignReportColsList=Arrays.asList(videoCampaignReportCols);
					}
					
					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
					int count=0;	
					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Reading columns from first row...:");
							for(int i=0;i<line.length;i++){
								int index=videoCampaignReportColsList.indexOf(line[i]);
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
						    
						    /*long AD_SERVER_IMPRESSIONS=csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")==null?0:
								StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);*/				//
							 
							/*Rich Media viewership*/
						    long RICH_MEDIA_BACKUP_IMAGES=csvColumnMap.get("Column.RICH_MEDIA_BACKUP_IMAGES")==null?0:
								StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_BACKUP_IMAGES")]);				//
						    
						    double RICH_MEDIA_DISPLAY_TIME=csvColumnMap.get("Column.RICH_MEDIA_DISPLAY_TIME")==null?0.0:
								StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_DISPLAY_TIME")]);			//
							
							double RICH_MEDIA_AVERAGE_DISPLAY_TIME=csvColumnMap.get("Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME")]);	//
							
						    /* Rich Media interaction */
						    long RICH_MEDIA_EXPANSIONS=csvColumnMap.get("Column.RICH_MEDIA_EXPANSIONS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_EXPANSIONS")]);
						    
						    double RICH_MEDIA_EXPANDING_TIME=csvColumnMap.get("Column.RICH_MEDIA_EXPANDING_TIME")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_EXPANDING_TIME")]);
						    
						    double RICH_MEDIA_INTERACTION_TIME=csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_TIME")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_TIME")]);
						    
						    long RICH_MEDIA_INTERACTION_COUNT=csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_COUNT")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_COUNT")]);
						    
						    double RICH_MEDIA_INTERACTION_RATE=csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_RATE")==null?0.0:
							    StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_RATE")]);		//
						    
						    double RICH_MEDIA_AVERAGE_INTERACTION_TIME=csvColumnMap.get("Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME")]);
						    
						    long RICH_MEDIA_INTERACTION_IMPRESSIONS=csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_IMPRESSIONS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_INTERACTION_IMPRESSIONS")]);
						    
						    long RICH_MEDIA_MANUAL_CLOSES=csvColumnMap.get("Column.RICH_MEDIA_MANUAL_CLOSES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_MANUAL_CLOSES")]);
						    
						    long RICH_MEDIA_FULL_SCREEN_IMPRESSIONS=csvColumnMap.get("Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS")]);
						    
						    /* Rich Media video metrics */
						    long RICH_MEDIA_VIDEO_INTERACTIONS=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_INTERACTIONS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_INTERACTIONS")]);
						    
						    double RICH_MEDIA_VIDEO_INTERACTION_RATE=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_INTERACTION_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_INTERACTION_RATE")]);
						    
						    long RICH_MEDIA_VIDEO_MUTES=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_MUTES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_MUTES")]);
						    
						    long RICH_MEDIA_VIDEO_PAUSES=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_PAUSES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_PAUSES")]);
						    
						    long RICH_MEDIA_VIDEO_PLAYES=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_PLAYES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_PLAYES")]);
						    
						    long RICH_MEDIA_VIDEO_MIDPOINTS=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_MIDPOINTS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_MIDPOINTS")]);
						    
						    long RICH_MEDIA_VIDEO_COMPLETES=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_COMPLETES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_COMPLETES")]);
						    
						    long RICH_MEDIA_VIDEO_REPLAYS=csvColumnMap.get("RICH_MEDIA_VIDEO_REPLAYS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_REPLAYS")]);
						    
						    long RICH_MEDIA_VIDEO_STOPS=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_STOPS")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_STOPS")]);
						    
						    long RICH_MEDIA_VIDEO_UNMUTES=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_UNMUTES")==null?0:
						        StringUtil.getLongValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_UNMUTES")]);
						    
						    double RICH_MEDIA_VIDEO_VIEW_RATE=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_VIEW_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_VIEW_RATE")]);

						    double RICH_MEDIA_VIDEO_VIEW_TIME=csvColumnMap.get("Column.RICH_MEDIA_VIDEO_VIEW_TIME")==null?0.0:
					        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.RICH_MEDIA_VIDEO_VIEW_TIME")]);
						    
						    /* Video viewership */
						    long VIDEO_INTERACTION_START=csvColumnMap.get("Column.VIDEO_INTERACTION_START")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_START")]);
						    
						    long VIDEO_INTERACTION_FIRST_QUARTILE=csvColumnMap.get("Column.VIDEO_INTERACTION_FIRST_QUARTILE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_FIRST_QUARTILE")]);
						    
						    long VIDEO_INTERACTION_MIDPOINT=csvColumnMap.get("Column.VIDEO_INTERACTION_MIDPOINT")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_MIDPOINT")]);
						    
						    long VIDEO_INTERACTION_THIRD_QUARTILE=csvColumnMap.get("Column.VIDEO_INTERACTION_THIRD_QUARTILE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_THIRD_QUARTILE")]);
						    
						    long VIDEO_INTERACTION_COMPLETE=csvColumnMap.get("Column.VIDEO_INTERACTION_COMPLETE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_COMPLETE")]);
						    
						    double VIDEO_INTERACTION_AVERAGE_VIEW_RATE=csvColumnMap.get("Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE")]);
						    
						    double VIDEO_INTERACTION_COMPLETION_RATE=csvColumnMap.get("Column.VIDEO_INTERACTION_COMPLETION_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_COMPLETION_RATE")]);
						    
						    long VIDEO_INTERACTION_ERROR_COUNT=csvColumnMap.get("Column.VIDEO_INTERACTION_ERROR_COUNT")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_ERROR_COUNT")]);
						    
						    double VIDEO_INTERACTION_VIDEO_LENGTH=csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_LENGTH")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_LENGTH")]);
						    
						    long VIDEO_INTERACTION_VIDEO_SKIP_SHOWN=csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN")]);
						    
						    long VIDEO_INTERACTION_ENGAGED_VIEW=csvColumnMap.get("Column.VIDEO_INTERACTION_ENGAGED_VIEW")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_ENGAGED_VIEW")]);
						    
						    double VIDEO_INTERACTION_VIEW_THROUGH_RATE=csvColumnMap.get("Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE")]);
						    
						    /* Video interaction */
						    long VIDEO_INTERACTION_PAUSE=csvColumnMap.get("Column.VIDEO_INTERACTION_PAUSE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_PAUSE")]);
						    
						    long VIDEO_INTERACTION_RESUME=csvColumnMap.get("Column.VIDEO_INTERACTION_RESUME")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_RESUME")]);
						    
						    long VIDEO_INTERACTION_REWIND=csvColumnMap.get("Column.VIDEO_INTERACTION_REWIND")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_REWIND")]);
						    
						    long VIDEO_INTERACTION_MUTE=csvColumnMap.get("Column.VIDEO_INTERACTION_MUTE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_MUTE")]);
						    
						    long VIDEO_INTERACTION_UNMUTE=csvColumnMap.get("Column.VIDEO_INTERACTION_UNMUTE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_UNMUTE")]);
						    
						    long VIDEO_INTERACTION_COLLAPSE=csvColumnMap.get("Column.VIDEO_INTERACTION_COLLAPSE")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_COLLAPSE")]);
						    
						    long VIDEO_INTERACTION_EXPAND=csvColumnMap.get("Column.VIDEO_INTERACTION_EXPAND")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_EXPAND")]);
						    
						    long VIDEO_INTERACTION_FULL_SCREEN=csvColumnMap.get("Column.VIDEO_INTERACTION_FULL_SCREEN")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_FULL_SCREEN")]);
						    
						    long VIDEO_INTERACTION_VIDEO_SKIPS=csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_SKIPS")==null?0:
					    		StringUtil.getLongValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_VIDEO_SKIPS")]);
						    
						    double VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE=csvColumnMap.get("Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE")]);
						    
						    double VIDEO_INTERACTION_VIEW_RATE=csvColumnMap.get("Column.VIDEO_INTERACTION_VIEW_RATE")==null?0.0:
						        StringUtil.getDoubleValue(line[csvColumnMap.get("Column.VIDEO_INTERACTION_VIEW_RATE")]);
						    
						    
						    RichMediaCommonReportObj rootObj=new RichMediaCommonReportObj();	
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
							
							/*rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);*///
							 
							 /*Rich Media viewership*/
						    rootObj.setRichMediaBackupImages(RICH_MEDIA_BACKUP_IMAGES);//
							rootObj.setRichMediaDisplayTime(RICH_MEDIA_DISPLAY_TIME);//
							rootObj.setRichMediaAverageDisplayTime(RICH_MEDIA_AVERAGE_DISPLAY_TIME);//
							
							/* Rich Media interaction */
							rootObj.setRichMediaExpansions(RICH_MEDIA_EXPANSIONS);
							rootObj.setRichMediaExpandingTime(RICH_MEDIA_EXPANDING_TIME);
							rootObj.setRichMediaInteractionTime(RICH_MEDIA_INTERACTION_TIME);
							rootObj.setRichMediaInteractionCount(RICH_MEDIA_INTERACTION_COUNT);
							rootObj.setRichMediaInteractionRate(RICH_MEDIA_INTERACTION_RATE);//
							rootObj.setRichMediaAverageInteractionTime(RICH_MEDIA_AVERAGE_INTERACTION_TIME);
							rootObj.setRichMediaInteractionImpressions(RICH_MEDIA_INTERACTION_IMPRESSIONS);
							rootObj.setRichMediaManualCloses(RICH_MEDIA_MANUAL_CLOSES);
							rootObj.setRichMediaFullScreenImpressions(RICH_MEDIA_FULL_SCREEN_IMPRESSIONS);
							
							/* Rich Media video metrics */
							rootObj.setRichMediaVideoInteractions(RICH_MEDIA_VIDEO_INTERACTIONS);
							rootObj.setRichMediaVideoInteractionRate(RICH_MEDIA_VIDEO_INTERACTION_RATE);
							rootObj.setRichMediaVideoMutes(RICH_MEDIA_VIDEO_MUTES);
							rootObj.setRichMediaVideoPauses(RICH_MEDIA_VIDEO_PAUSES);
							rootObj.setRichMediaVideoPlayes(RICH_MEDIA_VIDEO_PLAYES);
							rootObj.setRichMediaVideoMidpoints(RICH_MEDIA_VIDEO_MIDPOINTS);
							rootObj.setRichMediaVideoCompletes(RICH_MEDIA_VIDEO_COMPLETES);
							rootObj.setRichMediaVideoReplays(RICH_MEDIA_VIDEO_REPLAYS);
							rootObj.setRichMediaVideoStops(RICH_MEDIA_VIDEO_STOPS);
							rootObj.setRichMediaVideoUnmutes(RICH_MEDIA_VIDEO_UNMUTES);
							rootObj.setRichMediaVideoViewRate(RICH_MEDIA_VIDEO_VIEW_RATE);
							rootObj.setRichMediaVideoViewTime(RICH_MEDIA_VIDEO_VIEW_TIME);
							
							/* Video viewership */
							rootObj.setStart(VIDEO_INTERACTION_START);
							rootObj.setFirstQuartile(VIDEO_INTERACTION_FIRST_QUARTILE);
							rootObj.setMidpoint(VIDEO_INTERACTION_MIDPOINT);
							rootObj.setThirdQuartile(VIDEO_INTERACTION_THIRD_QUARTILE);
							rootObj.setComplete(VIDEO_INTERACTION_COMPLETE);
							rootObj.setAverageViewRate(VIDEO_INTERACTION_AVERAGE_VIEW_RATE);
							rootObj.setCompletionRate(VIDEO_INTERACTION_COMPLETION_RATE);
							rootObj.setErrorCount(VIDEO_INTERACTION_ERROR_COUNT);
							rootObj.setVideoLength(VIDEO_INTERACTION_VIDEO_LENGTH);
							rootObj.setVideoSkipShown(VIDEO_INTERACTION_VIDEO_SKIP_SHOWN);
							rootObj.setEngageView(VIDEO_INTERACTION_ENGAGED_VIEW);
							rootObj.setViewThroughRate(VIDEO_INTERACTION_VIEW_THROUGH_RATE);
							
							/* Video interaction */	
							rootObj.setPause(VIDEO_INTERACTION_PAUSE);
							rootObj.setResume(VIDEO_INTERACTION_RESUME);
							rootObj.setRewind(VIDEO_INTERACTION_REWIND);
							rootObj.setMute(VIDEO_INTERACTION_MUTE);
							rootObj.setUnmute(VIDEO_INTERACTION_UNMUTE);
							rootObj.setCollapse(VIDEO_INTERACTION_COLLAPSE);
							rootObj.setExpand(VIDEO_INTERACTION_EXPAND);
							rootObj.setFullScreen(VIDEO_INTERACTION_FULL_SCREEN);
							rootObj.setVideoSkips(VIDEO_INTERACTION_VIDEO_SKIPS);
							rootObj.setAverageInteractionRate(VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE);
							rootObj.setViewRate(VIDEO_INTERACTION_VIEW_RATE);
							
							String market="";
							String campaignCategory="";
							/*if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("DC")>=0){
								market="DC";
							}else if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("SF")>=0){
								market="SF";
							}	*/						
							
							rootObj.setMarket(market);
							rootObj.setCampaignCategory(campaignCategory);
							
                            rootObj.setDataSource(dataSource);	
                            
                            if(publisherId !=null && publisherName !=null){
                            	rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
    							rootObj.setPublisherName(publisherName);    							
    							rootObj.setChannelId(5);
    							rootObj.setChannelName("National Sales Direct");
    							rootObj.setChannelType("National Sales");
    							rootObj.setSalesType("Direct");	
                            }else{
                            	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);    							
        							rootObj.setChannelId(5);
        							rootObj.setChannelName("National Sales Direct");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
        							
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
        							rootObj.setChannelId(5);
        							rootObj.setChannelName("National Sales Direct");
        							rootObj.setChannelType("National Sales");
        							rootObj.setSalesType("Direct");	
                                }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
                                	rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
        							rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
        							rootObj.setChannelId(4);
        							rootObj.setChannelName("Local Sales Direct");
        							rootObj.setChannelType("Local Sales");
        							rootObj.setSalesType("Direct");	
                                }
                            }
                            
														
							dataList.add(rootObj);    					    
						
					  }
					  count++;
				    }
				    readChannel.close();
				  
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				e.printStackTrace();
				
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				e.printStackTrace();
				
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				e.printStackTrace();
				
			}
			return dataList;
	 }
	 
	 /*
	  * Create RichMedia custom event csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name),String dataSource,String bucketName
	  * @return - List<RichMediaReportTraffingObj> (List of RichMediaReportTraffingObj)
	  */
	 public static List<RichMediaCustomEventReportObj> createRichMediaCeltraCustomEventCSVReportFromCloudStorage(
				String fileName, String dirName,String dataSource,String bucketName) {
			List<RichMediaCustomEventReportObj> reportList = new ArrayList<RichMediaCustomEventReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + bucketName
					+ "/" + dirName + "/" + fileName;
			
			AppEngineFile readableFile = new AppEngineFile(filename);
			FileReadChannel readChannel;
			String[] headerRow=null;
			try {
				readChannel = fileService.openReadChannel(readableFile, false);
				BufferedReader reader = new BufferedReader(Channels.newReader(
						readChannel, "UTF8"));

				CSVReader csvReader = new CSVReader(reader);

				List<String[]> allElements = csvReader.readAll();
				String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
				int count = 0;
				for (String[] line : allElements) {
					if (count == 0) {
						log.info("Skip first row...");
						headerRow=line;
					} else {
						if (headerRow.length >= 25) {
							
							String campaignId= line[0];
							String campaignName= line[1];
							
							String date= line[2];
							date=DateUtil.getFormatedDate(date, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");							
							String accountDate= line[3];
							accountDate=DateUtil.getFormatedDate(accountDate, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
							String creativeId=line[4];
							String creativeName= line[5];							
							String placementId = line[6];
							String placementName = line[7];
							String platform = line[8];
							String sdk = line[9];
							String unitName=line[10];
							String screenLocalId=line[11];
							String screenTitle=line[12];
							String label=line[13];
							
							long customEventOccurs=StringUtil.getLongValue(line[14]);
							long urlOpens=StringUtil.getLongValue(line[15]);
							long storeOpens=StringUtil.getLongValue(line[16]);
							long videoPlays=StringUtil.getLongValue(line[17]);							
							long phoneCalls = StringUtil.getLongValue(line[18]);
							long facebookShareAttempts = StringUtil.getLongValue(line[19]);
							long twitterProfileOpens = StringUtil.getLongValue(line[20]);
							long tweetPageOpens = StringUtil.getLongValue(line[21]);
							long formSubmissionAttempts = StringUtil.getLongValue(line[22]);
							long formSubmissionSuccesses = StringUtil.getLongValue(line[23]);
							long pinterestPinAttempts = StringUtil.getLongValue(line[24]);
							
							long customCount=customEventOccurs+urlOpens+videoPlays+phoneCalls+storeOpens+
										facebookShareAttempts+twitterProfileOpens+tweetPageOpens+
										formSubmissionAttempts+formSubmissionSuccesses+pinterestPinAttempts;
							
							RichMediaCustomEventReportObj rootObj=new RichMediaCustomEventReportObj();
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(accountDate);
							rootObj.setOrderId(campaignId);
							rootObj.setOrder(campaignName);
							rootObj.setLineItemId(creativeId);
							rootObj.setLineItem(creativeName);
							rootObj.setCreativeId(creativeId);
							rootObj.setCreative(creativeName);
							
							rootObj.setCreativeType("Rich Media");
							
							rootObj.setSiteName(placementName);
							rootObj.setSiteId(placementId);
							rootObj.setCustomEvent(label);
							rootObj.setCustomConversionCountValue(customCount);
							
							if(sdk !=null && sdk.equalsIgnoreCase("MobileWeb")){
								rootObj.setSiteType(sdk);
							}else{
								rootObj.setSiteType("App");
							}
							String market="";
							String campaignCategory="";
							if(creativeName !=null && creativeName.indexOf("DC") >=0){
								market="DC";
							}else if(creativeName !=null && creativeName.indexOf("SF") >=0){
								market="SF";
							}
							
						
							rootObj.setMarket(market);
							rootObj.setCampaignCategory(campaignCategory);
							rootObj.setAgency(LinMobileConstants.XAD_AGENCY_GOLFSMITH);
							rootObj.setAdvertiser(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER);
							rootObj.setAdvertiserId(Long.parseLong(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID));
							
							rootObj.setPublisherId(StringUtil
									.getLongValue(LinMobileConstants.CELTRA_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.CELTRA_PUBLISHER_NAME);
							rootObj.setChannelId(Integer
									.parseInt(LinMobileConstants.CELTRA_CHANNEL_ID));
							rootObj.setChannelName(LinMobileConstants.CELTRA_CHANNEL_NAME);
							rootObj.setChannelType(LinMobileConstants.CELTRA_CHANNEL_TYPE);
							rootObj.setSalesType(LinMobileConstants.CELTRA_SALES_TYPE);
							rootObj.setDataSource(dataSource);
							
							reportList.add(rootObj);
						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}
					}
					count++;
				}
				
				readChannel.close();
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:" + e.getMessage());
				e.printStackTrace();

			} catch (LockException e) {
				log.severe("LockException:" + e.getMessage());
				e.printStackTrace();

			} catch (IOException e) {
				log.severe("IOException:" + e.getMessage());
				e.printStackTrace();

			}
			return reportList;
	}
	 
	 
	 /*
	  * Create RichMedia custom event csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<RichMediaReportTraffingObj> (List of RichMediaReportTraffingObj)
	  */
	 public static List<RichMediaCustomEventReportObj> createRichMediaCustomEventXAdCSVReportFromCloudStorage(
				String fileName, String dirName,String bucketName) {
			List<RichMediaCustomEventReportObj> reportList = new ArrayList<RichMediaCustomEventReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + bucketName
					+ "/" + dirName + "/" + fileName;
			
			AppEngineFile readableFile = new AppEngineFile(filename);
			FileReadChannel readChannel;
			String[] headerRow=null;
			try {
				readChannel = fileService.openReadChannel(readableFile, false);
				BufferedReader reader = new BufferedReader(Channels.newReader(
						readChannel, "UTF8"));

				CSVReader csvReader = new CSVReader(reader);

				List<String[]> allElements = csvReader.readAll();
				String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
				int count = 0;
				for (String[] line : allElements) {
					if (count == 0) {
						log.info("Skip first row...");
						headerRow=line;
					} else {
                         if (headerRow.length >= 12) {							
							String date= line[0];							
							date=DateUtil.getFormatedDate(date, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							
							String adUnit= line[1];
							long impressions = StringUtil.getLongValue(line[2]);
							long totalClicks = StringUtil.getLongValue(line[3]);
							
							String clickRatePercent=line[4];
							if(clickRatePercent !=null && clickRatePercent.indexOf("%")>=0){
								clickRatePercent=clickRatePercent.replace("%", "");
							}
							double clickRate = StringUtil.getDoubleValue(clickRatePercent, 6);
							
							long totalCalls=StringUtil.getLongValue(line[5]);
							
							String callRatePercent=line[6];
							if(callRatePercent !=null && callRatePercent.indexOf("%")>=0){
								callRatePercent=callRatePercent.replace("%", "");
							}
							double callRate = StringUtil.getDoubleValue(callRatePercent, 6);
							
							String budgetStr=line[7];
							if(budgetStr !=null && budgetStr.indexOf("$")>=0){
								budgetStr=budgetStr.replace("$","");
							}
							double budget = StringUtil.getDoubleValue(budgetStr, 6);
							
							String secondaryClickRatePercent=line[8];
							if(secondaryClickRatePercent !=null && secondaryClickRatePercent.indexOf("%")>=0){
								secondaryClickRatePercent=secondaryClickRatePercent.replace("%", "");
							}
							double secondaryClickRate = StringUtil.getDoubleValue(secondaryClickRatePercent, 6);
							
							long map=StringUtil.getLongValue(line[9]);
							long direction=StringUtil.getLongValue(line[10]);
							long information=StringUtil.getLongValue(line[11]);
							
							String orderStartDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_START_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							String orderEndDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_END_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							double goalQty= 0;
							
							for(int i=0;i<4;i++){
								RichMediaCustomEventReportObj rootObj=new RichMediaCustomEventReportObj();
								
								rootObj.setLoadTimestamp(loadTimestamp);
								rootObj.setDate(date);
															
								rootObj.setAdFormat(LinMobileConstants.XAD_AD_FORMAT);
								
								rootObj.setOrderId(LinMobileConstants.LIN_DIGITAL_XAD_ORDER_NAME);
								rootObj.setOrder(LinMobileConstants.LIN_DIGITAL_XAD_ORDER_NAME);
								rootObj.setLineItemId(adUnit);
								rootObj.setLineItem(adUnit);
								rootObj.setCreativeId(adUnit);
								rootObj.setCreative(adUnit);
								
								rootObj.setSiteName("Location Based Targeting");
								rootObj.setCreativeType("Rich Media");
								
								rootObj.setGoalQuantity(goalQty);							
															
								
								rootObj.setOrderStartDate(orderStartDate);
								rootObj.setOrderEndDate(orderEndDate);
								rootObj.setLineItemStartDate(orderStartDate);
								rootObj.setLineItemEndDate(orderEndDate);
								
								String market="";
								String campaignCategory="";
								if(adUnit !=null && adUnit.indexOf("DC") >=0){
									market="DC";
								}else if(adUnit !=null && adUnit.indexOf("SF") >=0){
									market="SF";
								}
								
								
								rootObj.setMarket(market);
								rootObj.setCampaignCategory(campaignCategory);
								
								rootObj.setAgency(LinMobileConstants.XAD_AGENCY_GOLFSMITH);								
								rootObj.setServingPlatform(LinMobileConstants.XAD_CHANNEL_NAME);								
								rootObj.setAdvertiser(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER);
								rootObj.setAdvertiserId(Long.parseLong(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID));
								
								rootObj.setPublisherId(StringUtil
										.getLongValue(LinMobileConstants.XAD_PUBLISHER_ID));
								rootObj.setPublisherName(LinMobileConstants.XAD_PUBLISHER_NAME);
								rootObj.setChannelId(Integer
										.parseInt(LinMobileConstants.XAD_CHANNEL_ID));
								rootObj.setChannelName(LinMobileConstants.XAD_CHANNEL_NAME);
								rootObj.setChannelType(LinMobileConstants.XAD_CHANNEL_TYPE);
								rootObj.setSalesType(LinMobileConstants.XAD_SALES_TYPE);
								rootObj.setDataSource(LinMobileConstants.XAD_CHANNEL_NAME);
								
								rootObj.setCustomEvent(LinMobileConstants.XAD_CUSTOM_EVENT_ARRAY[i]);								
								if(LinMobileConstants.XAD_CUSTOM_EVENT_ARRAY[i].equals("Click to call")){
									rootObj.setCustomConversionCountValue(totalCalls);
								}else if(LinMobileConstants.XAD_CUSTOM_EVENT_ARRAY[i].equals("Map")){
									rootObj.setCustomConversionCountValue(map);
								}else if(LinMobileConstants.XAD_CUSTOM_EVENT_ARRAY[i].equals("Directions")){
									rootObj.setCustomConversionCountValue(direction);
								}else if(LinMobileConstants.XAD_CUSTOM_EVENT_ARRAY[i].equals("More Information")){
									rootObj.setCustomConversionCountValue(information);
								}								
								reportList.add(rootObj);
						    }
							
							
						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}						
					}
					count++;
				}
				
				readChannel.close();
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:" + e.getMessage());
				e.printStackTrace();

			} catch (LockException e) {
				log.severe("LockException:" + e.getMessage());
				e.printStackTrace();

			} catch (IOException e) {
				log.severe("IOException:" + e.getMessage());
				e.printStackTrace();

			}
			return reportList;
	}

}
