package com.lin.web.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.files.LockException;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.CorePerformanceTargetReportObj;
import com.lin.server.bean.DFPTargetReportObj;
import com.lin.server.bean.LinOneRichMediaReportObj;
import com.lin.server.bean.LocationReportObj;
import com.lin.server.bean.RichMediaCommonReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;
import com.lin.server.bean.SellThroughReportObj;
import com.lin.web.dto.CeltraDTO;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.ReadBigQuerySchemaDTO;
import com.lin.web.servlet.GCStorageUtil;


/**
 * This util helps to read/write csv file on cloud storage
 *
 * Tags: CloudStorageUtil
 *
 * @author youdhveer.panwar@mediaagility.com (Youdhveer Panwar)
 */
public class CloudStorageUtil {

	private static final int BUFFER_SIZE = 1024 * 1024;
	private static final Logger log = Logger.getLogger(CloudStorageUtil.class.getName());

    private static List<String> targetReportColsList=null;
    static final String [] targetReportCols={
		"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.GENERIC_CRITERION_NAME","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CLICKS","Column.AD_SERVER_CTR","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_SERVER_DELIVERY_INDICATOR","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM","Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"
     };

    private static List<String> locationReportColsList=null;
    static final String [] locationReportCols={
    	"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME","Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.COUNTRY_CRITERIA_ID","Dimension.REGION_CRITERIA_ID","Dimension.CITY_CRITERIA_ID","Dimension.COUNTRY_NAME","Dimension.REGION_NAME","Dimension.CITY_NAME","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","DimensionAttribute.LINE_ITEM_COST_TYPE","DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS","DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.ORDER_LIFETIME_CLICKS","DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS","DimensionAttribute.ORDER_PO_NUMBER","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.ORDER_TRAFFICKER","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CLICKS","Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM","Column.AD_SERVER_CTR","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_SERVER_CPD_REVENUE","Column.AD_SERVER_ALL_REVENUE","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM"
     };
    
    private static List<String> corePerformanceReportColsList=null;
    static final String [] corePerformanceReportCols={
    	"Dimension.DATE","Dimension.ADVERTISER_ID","Dimension.ADVERTISER_NAME",
    	"Ad unit ID 1","Ad unit ID 2","Ad unit ID 3","Ad unit ID 4","Ad unit 1","Ad unit 2","Ad unit 3","Ad unit 4",
    	"Dimension.ORDER_ID","Dimension.ORDER_NAME","Dimension.LINE_ITEM_ID","Dimension.LINE_ITEM_NAME","Dimension.LINE_ITEM_TYPE","Dimension.CREATIVE_ID","Dimension.CREATIVE_NAME","Dimension.CREATIVE_SIZE","Dimension.CREATIVE_TYPE","Dimension.SALESPERSON_NAME","Dimension.CREATIVE_TYPE_ID","Dimension.SALESPERSON_ID","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY","DimensionAttribute.LINE_ITEM_COST_PER_UNIT","DimensionAttribute.LINE_ITEM_COST_TYPE","DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS","DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS","DimensionAttribute.LINE_ITEM_START_DATE_TIME","DimensionAttribute.LINE_ITEM_END_DATE_TIME","DimensionAttribute.ORDER_AGENCY","DimensionAttribute.ORDER_AGENCY_ID","DimensionAttribute.ORDER_LIFETIME_CLICKS","DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS","DimensionAttribute.ORDER_PO_NUMBER","DimensionAttribute.ORDER_START_DATE_TIME","DimensionAttribute.ORDER_END_DATE_TIME","DimensionAttribute.ORDER_TRAFFICKER","Column.AD_SERVER_IMPRESSIONS","Column.AD_SERVER_CTR","Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM","Column.AD_SERVER_CLICKS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS","Column.AD_SERVER_ALL_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR","Column.AD_SERVER_CPD_REVENUE","Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM","Column.AD_SERVER_CPM_AND_CPC_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE","Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM","Column.AD_SERVER_DELIVERY_INDICATOR"
    };
    
    private static List<String> sellThroughReportColsList=null;
    static final String [] sellThroughReportCols={
    	"Ad unit","Ad request sizes","Week","Ad unit ID","Forecasted impressions","Available impressions","Reserved impressions","Sell-through rate"
     };
    
	private FileWriteChannel writeChannel = null;
	FileService fileService = FileServiceFactory.getFileService();
	private OutputStream os = null;

	public void init(String fileName, String mime) throws Exception {	   
	   log.info("Storage service:init() method:  file name:"+fileName+" and mime:"+mime);

	    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
	            .setAcl("public_read")
	            .setBucket(LinMobileVariables.APPLICATION_BUCKET_NAME)
	            .setKey(fileName)
	            .setMimeType(mime);
	    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
	    boolean lock = true;
	    writeChannel = fileService.openWriteChannel(writableFile, lock);
	    os = Channels.newOutputStream(writeChannel);
	}

	public void init(String fileName, String mime,String dirName) throws Exception {		  
		   log.info("Storage service:init() method:  file name:"+fileName+" and mime:"+mime);

		    GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
		            .setAcl("public_read")
		            .setBucket(LinMobileVariables.APPLICATION_BUCKET_NAME+"/"+dirName)
		            .setKey(fileName)
		            .setMimeType(mime);
		    AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
		    boolean lock = true;
		    writeChannel = fileService.openWriteChannel(writableFile, lock);
		    os = Channels.newOutputStream(writeChannel);
	}

	public void storeFile(byte[] b, int readSize) throws Exception {
	    os.write(b, 0, readSize);
	    os.flush();
	}

	/*
	 * Write file on cloud storage
	 * 
	 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	 * 
	 * @param  - String file,
	 *           String fileName, 
	 *           String dirName,
	 *           String mime
	 * @return - String uploadedFileURL         
	 */
	public String writeFileOnCloudStorage(String file,String fileName, String dirName,String mime) {
		GCStorageUtil storage = new GCStorageUtil();
		try {
			log.info(":Going to upload file on cloud storage: fileName:"+fileName);
            if(dirName ==null){
            	storage.init(fileName, mime);
            }else{
            	storage.init(dirName, fileName, mime);
            }

			InputStream inputStream = IOUtils.toInputStream(file,"ISO-8859-1");
			String uploadedFileURL = storage.writeFile(fileName, inputStream);
			log.info("uploadedFileURL::" +  uploadedFileURL);			
			return uploadedFileURL;

		} catch (FileNotFoundException e) {
			log.severe(this.getClass() + ":FileNotFoundException::"+ e.getMessage());
			return null;
		} catch (Exception e) {
			log.severe(this.getClass() + ":Exception::" + e.getMessage());
			return null;
		}

	}	

	/*
	 * ReadBigQuerySchemaCSVFromCloudStoragee
	 * 
	 * @author Shubham Goel
	 * 
	 * @param  - String fileName,
	 *           String bucketName (cloud storage bucket name)
	 * @return - List<ReadBigQuerySchemaDTO>        
	 */
	public static List<ReadBigQuerySchemaDTO> readBigQuerySchemaCSVFromCloudStorage(String fileName , String bucketName) { 

		    log.info("Reading from google cloud storage,fileName:"+fileName);
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename ="/gs/"+bucketName+"/"+LinMobileConstants.BIGQUERY_SCHEMA_FOLDER+"/"+fileName;

		    log.info("Reading from google cloud storag: filename:"+filename);

		    AppEngineFile readableFile = new AppEngineFile(filename);
		    FileReadChannel readChannel;
		    List<ReadBigQuerySchemaDTO> schemaDTO = new ArrayList<ReadBigQuerySchemaDTO>();
			try {
					readChannel = fileService.openReadChannel(readableFile, false);
					BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));

					CSVReader csvReader = new CSVReader(reader);
					List<String[]> allElements = csvReader.readAll();

					int count=0;
					log.info("Going to read all lines.........allElements:"+allElements.size());					
					for(String[] line:allElements){			


							String columnName=line[0];
							String columnType=line[1];
						    ReadBigQuerySchemaDTO obj = new ReadBigQuerySchemaDTO(columnName, columnType);
						    schemaDTO.add(obj);

					}				   
				    readChannel.close();
				    return schemaDTO;
			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:"+e.getMessage());
				return null;
			} catch (LockException e) {
				log.severe("LockException:"+e.getMessage());
				return null;
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				return null;
			} 
	}	

	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list and rejected data list (if faulted data found)
	  * for Lin Media (DFP account)
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readCorePerformanceDFPRawCSVFromCloudStorage(String fileName,String dirName) { 
		    List<CorePerformanceReportObj> correctDataList=new ArrayList<CorePerformanceReportObj>();
		    List<CorePerformanceReportObj> faultedDataList=new ArrayList<CorePerformanceReportObj>();
		    boolean faultedData=false;

		    Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();

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

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();

					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
						}else{
						if(line.length >= 53){	

							String DATE=line[0];
							long ADVERTISER_ID=StringUtil.getLongValue(line[1]);
							String ADVERTISER_NAME=line[2];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}
							String AD_UNIT_ID_1=line[3];
							String AD_UNIT_ID_2=line[4];
							String AD_UNIT_ID_3=line[5];
							String AD_UNIT_NAME_1=line[6];
							if(AD_UNIT_NAME_1!=null ){
								AD_UNIT_NAME_1=AD_UNIT_NAME_1.replaceAll(","," ");
							}
							String AD_UNIT_NAME_2=line[7];
							if(AD_UNIT_NAME_2!=null ){
								AD_UNIT_NAME_2=AD_UNIT_NAME_2.replaceAll(","," ");
							}
							String AD_UNIT_NAME_3=line[8];
							if(AD_UNIT_NAME_3!=null ){
								AD_UNIT_NAME_3=AD_UNIT_NAME_3.replaceAll(","," ");
							}
							long ORDER_ID=StringUtil.getLongValue(line[9]);
							String ORDER_NAME=line[10];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(","," ");
							}

							long LINE_ITEM_ID=StringUtil.getLongValue(line[11]);
							String LINE_ITEM_NAME=line[12];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
							}
							String LINE_ITEM_TYPE=line[13];
							long CREATIVE_ID=StringUtil.getLongValue(line[14]);
							String CREATIVE_NAME=line[15];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[16];
							String CREATIVE_TYPE=line[17];
							String SALESPERSON_NAME=line[18];
							if(SALESPERSON_NAME!=null ){
								SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
							}
							long CREATIVE_TYPE_ID=StringUtil.getLongValue(line[19]);
							long SALESPERSON_ID=StringUtil.getLongValue(line[20]);

							String goalQty=line[21];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

						    long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[22]);

						    String LINE_ITEM_COST_PER_UNIT=line[23];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

						    String LINE_ITEM_COST_TYPE=line[24];
						    long LINE_ITEM_LIFETIME_CLICKS=StringUtil.getLongValue(line[25]);
						    long LINE_ITEM_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[26]);

						    String LINE_ITEM_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(line[27],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    //String LINE_ITEM_START_DATE=DateUtil.getFormatedDateUsingJodaLib(line[27],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd");
						    String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;

						    String lineItemEndDate=line[28];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							    //LINE_ITEM_END_DATE=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd");
						    	LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							 }

						    String ORDER_AGENCY=line[29];
						    long ORDER_AGENCY_ID=StringUtil.getLongValue(line[30]);
						    long ORDER_LIFETIME_CLICKS=StringUtil.getLongValue(line[31]);
						    long ORDER_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[32]);
						    String ORDER_PO_NUMBER=line[33];

						    String orderStartDate=line[34];
						    String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    //String ORDER_START_DATE=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd");
						    String ORDER_START_DATE=ORDER_START_DATE_TIME;

						    String orderEndDate=line[35];
						    String ORDER_END_DATE_TIME="";
						    String ORDER_END_DATE="";
						    if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
						    	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
								 //ORDER_END_DATE=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd");
						    	 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}

						    String ORDER_TRAFFICKER=line[36];


							long AD_SERVER_IMPRESSIONS=StringUtil.getLongValue(line[37]);							
							double AD_SERVER_CTR=0;
							if(LinMobileUtil.isNumeric(line[38])){
								AD_SERVER_CTR=(((double)Double.parseDouble(line[38])*100));
							}							
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;

							double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							if(LinMobileUtil.isNumeric(line[39])){
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(line[39])*100));
							}
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[40]);
							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[41]);

							long AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS=StringUtil.getLongValue(line[42]);
							double AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							if(LinMobileUtil.isNumeric(line[43])){
								AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(line[43])*100));
							}
							long AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS=StringUtil.getLongValue(line[44]);	
							double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[45]);

							double AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=0;
							if(LinMobileUtil.isNumeric(line[46])){
								AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=(((double)Double.parseDouble(line[46])*100));
							}							
							AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=Math.round((AD_EXCHANGE_LINE_ITEM_LEVEL_CTR*100.0))/100.0;

							double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[47]);							
							double AD_SERVER_WITH_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[48]);
							double AD_SERVER_CPM_AND_CPC_REVENUE=ReportUtil.convertMoney(line[49]);
							double AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE=ReportUtil.convertMoney(line[50]);
							double AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM=ReportUtil.convertMoney(line[51]);

							double AD_SERVER_DELIVERY_INDICATOR=0.0;
							if(LinMobileUtil.isNumeric(line[52])){								
								AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(line[52])*100));
								AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
							}


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

							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);

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
									//rootObj.setChannelName("Lin Media Direct");
									rootObj.setChannelName("Local Sales Direct");
									rootObj.setChannelType("Local Sales");
									rootObj.setSalesType("Direct");									
								}else{
									faultedData=true;
								}
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

						}else{
							log.warning("Invalid CSV file : Please provide 53 columns csv file..");
						}
					  }
					  count++;
				    }

					allDataMap.put("CorrectData", correctDataList);
					allDataMap.put("InCorrectData", faultedDataList);
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
	  * creates processed data list and rejected data list (if faulted data found)
	  * for LinDigital
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readLinDigitalDFPRawCSVFromCloudStorage(
			 String fileName,String dirName) {

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
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}

							String AD_UNIT_ID_1=csvColumnMap.get("Ad unit ID 1")==null?"":line[csvColumnMap.get("Ad unit ID 1")];
							String AD_UNIT_ID_2=csvColumnMap.get("Ad unit ID 2")==null?"":line[csvColumnMap.get("Ad unit ID 2")];
							String AD_UNIT_ID_3=csvColumnMap.get("Ad unit ID 3")==null?"":line[csvColumnMap.get("Ad unit ID 3")];
							String AD_UNIT_ID_4=csvColumnMap.get("Ad unit ID 4")==null?"":line[csvColumnMap.get("Ad unit ID 4")];

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
							long CREATIVE_ID=
									StringUtil.getLongValue(line[csvColumnMap.get("Dimension.CREATIVE_ID")]);
							String CREATIVE_NAME=line[csvColumnMap.get("Dimension.CREATIVE_NAME")];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[csvColumnMap.get("Dimension.CREATIVE_SIZE")];
							String CREATIVE_TYPE=line[csvColumnMap.get("Dimension.CREATIVE_TYPE")];
							String SALESPERSON_NAME=line[csvColumnMap.get("Dimension.SALESPERSON_NAME")];
							if(SALESPERSON_NAME!=null ){
								SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
							}
							long SALESPERSON_ID=
									csvColumnMap.get("Dimension.SALESPERSON_ID")==null?0:StringUtil.getLongValue(line[csvColumnMap.get("Dimension.SALESPERSON_ID")]);

							String goalQty=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

							long LINE_ITEM_CONTRACTED_QUANTITY=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY")]);

							String LINE_ITEM_COST_PER_UNIT=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")];
							if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
								LINE_ITEM_COST_PER_UNIT="0";
							}
							double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

							String LINE_ITEM_COST_TYPE=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE")];
							long LINE_ITEM_LIFETIME_CLICKS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS")]);
							long LINE_ITEM_LIFETIME_IMPRESSIONS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS")]);


							String LINE_ITEM_START_DATE_TIME=
							 DateUtil.getFormatedDateUsingJodaLib(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;
                           
							String lineItemEndDate=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")];

							String LINE_ITEM_END_DATE_TIME="";
							String LINE_ITEM_END_DATE="";
							if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
								LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							 }

							String ORDER_AGENCY=line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")];
							if(ORDER_AGENCY !=null && ORDER_AGENCY.indexOf(",")>=0){
								ORDER_AGENCY=ORDER_AGENCY.replaceAll(",", " ");
							}
							long ORDER_AGENCY_ID=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")]);
							long ORDER_LIFETIME_CLICKS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_CLICKS")]);
							long ORDER_LIFETIME_IMPRESSIONS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS")]);
							String ORDER_PO_NUMBER=line[csvColumnMap.get("DimensionAttribute.ORDER_PO_NUMBER")];

							String orderStartDate=line[csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")];
							String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String ORDER_START_DATE=ORDER_START_DATE_TIME;

							String orderEndDate=line[csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")];
							String ORDER_END_DATE_TIME="";
							String ORDER_END_DATE="";
							if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
								 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
								 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}					   
							String ORDER_TRAFFICKER=line[csvColumnMap.get("DimensionAttribute.ORDER_TRAFFICKER")];


							long AD_SERVER_IMPRESSIONS=
							   StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);	
							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_CLICKS")]);
							double AD_SERVER_WITH_CPD_AVERAGE_ECPM=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")]);
							double AD_SERVER_CTR=0;
							String ctr=line[csvColumnMap.get("Column.AD_SERVER_CTR")];
							if(LinMobileUtil.isNumeric(ctr)){
								AD_SERVER_CTR=(((double)Double.parseDouble(ctr)*100));
							}	
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
							double AD_SERVER_CPM_AND_CPC_REVENUE=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")]);
							double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPD_REVENUE")]);
							double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_ALL_REVENUE")]);
							double AD_SERVER_DELIVERY_INDICATOR=0.0;
							String deliveryIndicator=line[csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")];
							if(LinMobileUtil.isNumeric(deliveryIndicator)){		
								AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(deliveryIndicator)*100));
								AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
							}
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")]);
							double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							String lineItemLvlImp=line[csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
							if(LinMobileUtil.isNumeric(lineItemLvlImp)){
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(lineItemLvlImp)*100));
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

							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);

							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
							rootObj.setChannelId(5);
							rootObj.setChannelName("National Sales Direct");
							rootObj.setChannelType("National Sales");
							rootObj.setSalesType("Direct");	


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
	  * creates processed data list and rejected data list (if faulted data found)
	  * for LinDigital
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readTribuneDFPRawCSVFromCloudStorage(
			 String fileName,String dirName) {

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
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}

							String AD_UNIT_ID_1=csvColumnMap.get("Ad unit ID 1")==null?"":line[csvColumnMap.get("Ad unit ID 1")];
							String AD_UNIT_ID_2=csvColumnMap.get("Ad unit ID 2")==null?"":line[csvColumnMap.get("Ad unit ID 2")];
							String AD_UNIT_ID_3=csvColumnMap.get("Ad unit ID 3")==null?"":line[csvColumnMap.get("Ad unit ID 3")];
							String AD_UNIT_ID_4=csvColumnMap.get("Ad unit ID 4")==null?"":line[csvColumnMap.get("Ad unit ID 4")];

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
							long CREATIVE_ID=
									StringUtil.getLongValue(line[csvColumnMap.get("Dimension.CREATIVE_ID")]);
							String CREATIVE_NAME=line[csvColumnMap.get("Dimension.CREATIVE_NAME")];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[csvColumnMap.get("Dimension.CREATIVE_SIZE")];
							String CREATIVE_TYPE=line[csvColumnMap.get("Dimension.CREATIVE_TYPE")];
							String SALESPERSON_NAME=line[csvColumnMap.get("Dimension.SALESPERSON_NAME")];
							if(SALESPERSON_NAME!=null ){
								SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
							}
							long SALESPERSON_ID=
									csvColumnMap.get("Dimension.SALESPERSON_ID")==null?0:StringUtil.getLongValue(line[csvColumnMap.get("Dimension.SALESPERSON_ID")]);

							String goalQty=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

							long LINE_ITEM_CONTRACTED_QUANTITY=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY")]);

							String LINE_ITEM_COST_PER_UNIT=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")];
							if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
								LINE_ITEM_COST_PER_UNIT="0";
							}
							double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

							String LINE_ITEM_COST_TYPE=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE")];
							long LINE_ITEM_LIFETIME_CLICKS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS")]);
							long LINE_ITEM_LIFETIME_IMPRESSIONS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS")]);


							String LINE_ITEM_START_DATE_TIME=
							 DateUtil.getFormatedDateUsingJodaLib(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;
                           
							String lineItemEndDate=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")];

							String LINE_ITEM_END_DATE_TIME="";
							String LINE_ITEM_END_DATE="";
							if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
								LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							 }

							String ORDER_AGENCY=line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")];
							if(ORDER_AGENCY !=null && ORDER_AGENCY.indexOf(",")>=0){
								ORDER_AGENCY=ORDER_AGENCY.replaceAll(",", " ");
							}
							long ORDER_AGENCY_ID=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")]);
							long ORDER_LIFETIME_CLICKS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_CLICKS")]);
							long ORDER_LIFETIME_IMPRESSIONS=
							  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS")]);
							String ORDER_PO_NUMBER=line[csvColumnMap.get("DimensionAttribute.ORDER_PO_NUMBER")];

							String orderStartDate=line[csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")];
							String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String ORDER_START_DATE=ORDER_START_DATE_TIME;

							String orderEndDate=line[csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")];
							String ORDER_END_DATE_TIME="";
							String ORDER_END_DATE="";
							if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
								 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
								 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}					   
							String ORDER_TRAFFICKER=line[csvColumnMap.get("DimensionAttribute.ORDER_TRAFFICKER")];


							long AD_SERVER_IMPRESSIONS=
							   StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);	
							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_CLICKS")]);
							double AD_SERVER_WITH_CPD_AVERAGE_ECPM=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")]);
							double AD_SERVER_CTR=0;
							String ctr=line[csvColumnMap.get("Column.AD_SERVER_CTR")];
							if(LinMobileUtil.isNumeric(ctr)){
								AD_SERVER_CTR=(((double)Double.parseDouble(ctr)*100));
							}	
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
							double AD_SERVER_CPM_AND_CPC_REVENUE=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")]);
							double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPD_REVENUE")]);
							double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_ALL_REVENUE")]);
							double AD_SERVER_DELIVERY_INDICATOR=0.0;
							String deliveryIndicator=line[csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")];
							if(LinMobileUtil.isNumeric(deliveryIndicator)){		
								AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(deliveryIndicator)*100));
								AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
							}
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=
							  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")]);
							double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							String lineItemLvlImp=line[csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
							if(LinMobileUtil.isNumeric(lineItemLvlImp)){
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(lineItemLvlImp)*100));
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
							rootObj.setCostType(LINE_ITEM_COST_TYPE);
							rootObj.setGoalQty(LINE_ITEM_GOAL_QUANTITY);							

							double calculatedBudget=ReportUtil.calculateBudget(LINE_ITEM_GOAL_QUANTITY,rate);
							rootObj.setBudget(calculatedBudget);


							rootObj.setRate(rate);

							rootObj.setDeliveryIndicator(AD_SERVER_DELIVERY_INDICATOR);

							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);

							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
							//rootObj.setChannelId(Integer.parseInt(LinMobileConstants.TRIBUNE_CHANNEL_ID));
							//rootObj.setChannelName(LinMobileConstants.TRIBUNE_CHANNEL_NAME);
							rootObj.setChannelId(4);							
							rootObj.setChannelName("Local Sales Direct");
							rootObj.setChannelType("Local Sales");
							rootObj.setSalesType("Direct");	


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
	  * creates a Map containing processed data list and faulted data list(if exist)
	  * on the basis of DFP network code
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceReportObj>>
	  * 
	  * @param String fileName,String dirName,String networkCode
	  */
	 public static Map<String,List<CorePerformanceReportObj>> readCorePerformanceDFPRawCSVFromCloudStorage(String fileName,String dirName,String networkCode) { 

		    Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		    if(networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
		    	allDataMap=readCorePerformanceDFPRawCSVFromCloudStorage(fileName, dirName);
		    }else if(networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
		    	allDataMap=readLinDigitalDFPRawCSVFromCloudStorage(fileName, dirName);
		    }else if(networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
		    	allDataMap=readTribuneDFPRawCSVFromCloudStorage(fileName, dirName);
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
	 public static List<LocationReportObj> readDFPReportByLocationCSVFromCloudStorage(String fileName,String dirName,
			 String networkCode,String publisherId,String publisherName,String bucketName) { 
		    List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		    log.info(" Creating report for Network code:"+networkCode);
		    if(publisherId ==null && publisherName == null){
		        publisherId="0";
			    publisherName=null;
			    if(networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			    	publisherId=LinMobileConstants.LIN_MEDIA_PUBLISHER_ID;
			    	publisherName=LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME;				
				}else if(networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
					publisherId=LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID;
			    	publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;				
				}else if(networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
					publisherId=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID;
			    	publisherName=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME;				
				}
		    }
		    
		    FileService fileService = FileServiceFactory.getFileService();
		    String filename = "/gs/" + bucketName + "/"+dirName+"/"+ fileName;
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

						    long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[15]);

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

						    String ORDER_TRAFFICKER=line[29];


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
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[37]);							



							LocationReportObj rootObj=new LocationReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(DATE);

							rootObj.setAdvertiserId(ADVERTISER_ID);
							rootObj.setAdvertiser(ADVERTISER_NAME);

							rootObj.setCountryCriteriaId(COUNTRY_ID);
							rootObj.setRegionCriteriaId(REGION_ID);
							rootObj.setCityCriteriaId(CITY_ID);
							rootObj.setCountryName(COUNTRY_NAME);
							rootObj.setRegionName(REGION_NAME);
							rootObj.setCityName(CITY_NAME);

							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
							/*if(LINE_ITEM_COST_TYPE !=null && LINE_ITEM_COST_TYPE.equalsIgnoreCase("CPD")){
								rootObj.setClicksCPD(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPD(AD_SERVER_IMPRESSIONS);
							}else{
								rootObj.setClicksCPM(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPM(AD_SERVER_IMPRESSIONS);
							}*/

							rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
							rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);
							rootObj.setRevenueCPD(AD_SERVER_CPD_REVENUE);

							rootObj.setCTR(AD_SERVER_CTR);							
							rootObj.setECPM(AD_SERVER_WITH_CPD_AVERAGE_ECPM);



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


							rootObj.setLineItem(LINE_ITEM_NAME);
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
							reportList.add(rootObj);													    					    
						}						
					  }
					  count++;
				    }		
					csvReader.close();
					reader.close();
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
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list for PerformanceByLocation report
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @return List<LocationReportObj>	  * 
	  * @param String fileName,String dirName
	  */
	 public static List<LocationReportObj> readDFPReportByLocationCSVFromCloudStorage(String fileName
			 ,String dirName,int start,int limit) { 
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

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();
					log.info("Total rows in csv file :"+allElements.size());
					String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");

					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
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

						    long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[15]);

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

						    String ORDER_TRAFFICKER=line[29];

							double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[30]);
							long AD_SERVER_IMPRESSIONS=StringUtil.getLongValue(line[31]);							

							double AD_SERVER_CTR=0;
							if(LinMobileUtil.isNumeric(line[32])){
								AD_SERVER_CTR=(((double)Double.parseDouble(line[32])*100));
							}
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;

							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[33]);

							double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[34]);							
							double AD_SERVER_WITH_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[35]);
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[36]);							
							double AD_SERVER_CPM_AND_CPC_REVENUE=ReportUtil.convertMoney(line[37]);


							LocationReportObj rootObj=new LocationReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(DATE);

							rootObj.setAdvertiserId(ADVERTISER_ID);
							rootObj.setAdvertiser(ADVERTISER_NAME);

							rootObj.setCountryCriteriaId(COUNTRY_ID);
							rootObj.setRegionCriteriaId(REGION_ID);
							rootObj.setCityCriteriaId(CITY_ID);
							rootObj.setCountryName(COUNTRY_NAME);
							rootObj.setRegionName(REGION_NAME);
							rootObj.setCityName(CITY_NAME);

							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
							/*if(LINE_ITEM_COST_TYPE !=null && LINE_ITEM_COST_TYPE.equalsIgnoreCase("CPD")){
								rootObj.setClicksCPD(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPD(AD_SERVER_IMPRESSIONS);
							}else{
								rootObj.setClicksCPM(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPM(AD_SERVER_IMPRESSIONS);
							}*/

							rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
							rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);
							rootObj.setRevenueCPD(AD_SERVER_CPD_REVENUE);

							rootObj.setCTR(AD_SERVER_CTR);							
							rootObj.setECPM(AD_SERVER_WITH_CPD_AVERAGE_ECPM);



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


							rootObj.setLineItem(LINE_ITEM_NAME);
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

							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
							rootObj.setDataSource("DFP");
							reportList.add(rootObj);													    					    
						}						
					  }
					  count++;
				    }					
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
	  * Create Undertone Core Performance csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name), String bucketName
	  * @return - List<CorePerformanceReportObj> (List of CorePerformanceReportObj)
	  */
	 public static List<CorePerformanceReportObj> createUndertoneCSVReportFromCloudStorage(
				String fileName, String dirName,String bucketName) {
			List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + bucketName+ "/" + dirName + "/" + fileName;
			log.info("Reading from google cloud storage: filename:" + filename);
			AppEngineFile readableFile = new AppEngineFile(filename);
			FileReadChannel readChannel;

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
					} else {
						if (line.length >= 6) {
							String siteName = line[0];
							if(siteName!=null ){
								siteName=siteName.replaceAll(",","|");
								siteName=siteName.replaceAll("\"", "");
							}
							String costType = line[1];
							String rate = line[2];
							String date = line[3];
							date = DateUtil.getFormatedDate(date, "MM/dd/yyyy",
									"yyyy-MM-dd 00:00:00");
							String impressions = line[4];
							String revenue = line[5];

							CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setCostType(costType);
							rootObj.setDate(date);

							rootObj.setTotalImpressions(StringUtil.getLongValue(impressions));
							rootObj.setTotalRevenue(StringUtil.getDoubleValue(revenue));

							if (costType != null && costType.equalsIgnoreCase("CPD")) {
								rootObj.setImpressionsCPD(StringUtil.getLongValue(impressions));
								rootObj.setRevenueCPD(StringUtil.getDoubleValue(revenue));
							} else {
								rootObj.setImpressionsCPM(StringUtil.getLongValue(impressions));
								rootObj.setRevenueCPM(StringUtil.getDoubleValue(revenue));
							}
							rootObj.setRate(StringUtil.getDoubleValue(rate));
							rootObj.setECPM(StringUtil.getDoubleValue(rate));
							rootObj.setSiteName(siteName);

							rootObj.setPublisherId(StringUtil
									.getLongValue(LinMobileConstants.UNDERTONE_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.UNDERTONE_PUBLISHER_NAME);
							rootObj.setChannelId(Integer
									.parseInt(LinMobileConstants.UNDERTONE_CHANNEL_ID));
							rootObj.setChannelName(LinMobileConstants.UNDERTONE_CHANNEL_NAME);
							rootObj.setChannelType(LinMobileConstants.UNDERTONE_CHANNEL_TYPE);
							rootObj.setSalesType(LinMobileConstants.UNDERTONE_SALES_TYPE);
							rootObj.setDataSource(LinMobileConstants.UNDERTONE_DATA_SOURCE);

							rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));

							reportList.add(rootObj);
						}
					}
					count++;
				}

				readChannel.close();
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
	  * Create Sell Through csv report after reading raw file
	  * from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<SellThroughReportObj> (List of sell through objects)
	  */
	 public static List<SellThroughReportObj> createSellThroughCSVReportFromCloudStorage(
				String fileName, String dirName,String networkCode,String bucketName) {
			List<SellThroughReportObj> reportList = new ArrayList<SellThroughReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + bucketName
					+ "/" + dirName + "/" + fileName;
			log.info("Reading from google cloud storage: filename:" + filename);
			AppEngineFile readableFile = new AppEngineFile(filename);
			FileReadChannel readChannel;
			try {
				readChannel = fileService.openReadChannel(readableFile, false);
				BufferedReader reader = new BufferedReader(Channels.newReader(
						readChannel, "UTF8"));

				CSVReader csvReader = new CSVReader(reader);
				
				if(sellThroughReportColsList==null){
					sellThroughReportColsList=new ArrayList<String>();
					sellThroughReportColsList=Arrays.asList(sellThroughReportCols);
				}

				List<String[]> allElements = csvReader.readAll();
				Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();

				String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
				
				int count=0;					
				for(String[] line:allElements){			
					if(count==0){
						log.info("Reading columns from first row...sellThroughReportColsList:"+sellThroughReportColsList.size());
						for(int i=0;i<line.length;i++){
							int index=sellThroughReportColsList.indexOf(line[i]);
							if(index >=0){
								csvColumnMap.put(line[i], i);									
							}else{
								log.warning("This column not found in list :"+line[i]+" : Please update column list");
							}
						}
						log.info("Found cloumns in csv:"+csvColumnMap.size());
					}else{
						String siteName = line[csvColumnMap.get("Ad unit")];
						if(siteName!=null ){
							siteName=siteName.replaceAll(",","|");
							siteName=siteName.replaceAll("\"", "");
						}
						String siteSize = line[csvColumnMap.get("Ad request sizes")];
						if(siteSize!=null ){
							siteSize=siteSize.replaceAll(",","|");
							siteSize=siteSize.replaceAll("\"", "");
						}
						String date = csvColumnMap.get("Week")==null?null:line[csvColumnMap.get("Week")];							
						String siteId = csvColumnMap.get("Ad unit ID")==null?"":line[csvColumnMap.get("Ad unit ID")];
						String forecastedImpressions = csvColumnMap.get("Forecasted impressions")==null?"0":line[csvColumnMap.get("Forecasted impressions")];
						String availableImpressions = csvColumnMap.get("Available impressions")==null?"0":line[csvColumnMap.get("Available impressions")];
						String reservedImpressions = csvColumnMap.get("Reserved impressions")==null?"0":line[csvColumnMap.get("Reserved impressions")];
						String sellThroughRate =csvColumnMap.get("Sell-through rate")==null?"0":line[csvColumnMap.get("Sell-through rate")];

						SellThroughReportObj rootObj = new SellThroughReportObj();

						if(sellThroughRate!=null && (sellThroughRate.indexOf("%") != -1) ){
							sellThroughRate=sellThroughRate.replace("%", "");
						}
						String [] dateArray;
						if(date !=null && (date.indexOf("-") != -1)){
							dateArray=date.split("-");
							if(dateArray.length>=2){
								String startDate=DateUtil.getFormatedDate(dateArray[0].trim(), "MM/dd/yy","yyyy-MM-dd 00:00:00");
								String endDate=DateUtil.getFormatedDate(dateArray[1].trim(), "MM/dd/yy","yyyy-MM-dd 00:00:00");
								rootObj.setStartDate(startDate);
								rootObj.setEndDate(endDate);
								rootObj.setFrequency("Weekly");
							}else{
								log.warning("Wrong date coulmn, please check..date:"+date);
							}
						}else{
							date = DateUtil.getFormatedDate(date, "MM/dd/yy","yyyy-MM-dd 00:00:00");
							rootObj.setStartDate(date);
							rootObj.setEndDate(date);
							rootObj.setFrequency("Daily");
						}

						rootObj.setLoadTimestamp(loadTimestamp);

						rootObj.setAvailableImpressions(StringUtil.getLongValue(availableImpressions));
						rootObj.setForecastedImpressions(StringUtil.getLongValue(forecastedImpressions));
						rootObj.setReservedImpressions(StringUtil.getLongValue(reservedImpressions));
						rootObj.setSiteName(siteName);
						rootObj.setSiteID(siteId);
						rootObj.setCreativeSize(siteSize);
						rootObj.setSellThroughRate(StringUtil.getDoubleValue(sellThroughRate));

						if(networkCode !=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
							rootObj.setPublisherId(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID);
							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
						}else if(networkCode !=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
							rootObj.setPublisherId(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID);
							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
						}else if(networkCode !=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
							rootObj.setPublisherId(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID);
							rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
						}

						rootObj.setDataSource("DFP");

						reportList.add(rootObj);
					}
					count++;
				}

				readChannel.close();
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
	  * Create LSN Core Performance csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<CorePerformanceReportObj> (List of CorePerformanceReportObj)
	  */
	 public static List<CorePerformanceReportObj> createLSNCSVReportFromCloudStorage(
				String fileName, String dirName) {
			List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME
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
						if (headerRow.length >= 11) {
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
							rootObj.setLoadTimestamp(loadTimestamp);

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

							rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));

							reportList.add(rootObj);
						}
					}
					count++;
				}

				readChannel.close();
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
	  * Create Celtra Core Performance csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<CorePerformanceReportObj> (List of CorePerformanceReportObj)
	  */
	 
	 /*public static List<CorePerformanceReportObj> createCeltraCSVReportFromCloudStorage(
				String fileName, String dirName) {
			List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME
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
				Map<String,CeltraDTO> objectMap=new LinkedHashMap<String, CeltraDTO>();

				Map<String,Long> lineItemImpressionMap=new LinkedHashMap<String, Long>();
				Map<String,Long> lineItemClickMap=new LinkedHashMap<String, Long>();
				Map<String,Long> lineItemQtyMap=new LinkedHashMap<String, Long>();
				lineItemQtyMap.put("300-GolfsmithUSA-Mobile_13-0626_DC_15%", (long) 5000);
				lineItemQtyMap.put("300-GolfsmithUSA-Mobile_13-0626_DC_CF", (long) 6000);
				lineItemQtyMap.put("300-GolfsmithUSA-Mobile_13-0626_SF_15%", (long) 15000);
				lineItemQtyMap.put("300-GolfsmithUSA-Mobile_13-0626_SF_CF", (long) 35000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_DC_15%", (long) 75000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_DC_ANDROID_15%", (long) 15000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_DC_ANDROID_CF", (long) 30000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_DC_CF", (long) 25000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_SF_15%", (long) 60000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_SF_ANDROID_15%", (long) 12000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_SF_ANDROID_CF", (long) 30000);
				lineItemQtyMap.put("GolfsmithUSA-Mobile_13-0626_SF_CF", (long) 20000);

				Map<String,Long> lineItemIdMap=new LinkedHashMap<String, Long>();
				lineItemIdMap.put("300-GolfsmithUSA-Mobile_13-0626_DC_15%", (long) 13062601);
				lineItemIdMap.put("300-GolfsmithUSA-Mobile_13-0626_DC_CF", (long) 13062602);
				lineItemIdMap.put("300-GolfsmithUSA-Mobile_13-0626_SF_15%", (long) 13062603);
				lineItemIdMap.put("300-GolfsmithUSA-Mobile_13-0626_SF_CF", (long) 13062604);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_DC_15%", (long) 13062605);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_DC_ANDROID_15%", (long) 13062606);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_DC_ANDROID_CF", (long) 13062607);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_DC_CF", (long) 13062608);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_SF_15%", (long) 13062609);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_SF_ANDROID_15%", (long) 13062610);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_SF_ANDROID_CF", (long) 13062611);
				lineItemIdMap.put("GolfsmithUSA-Mobile_13-0626_SF_CF", (long) 13062612);

				for (String[] line : allElements) {
					if (count == 0) {
						log.info("Skip first row...");
						headerRow=line;
					} else {
						if (headerRow.length >= 17) {

							String campaignId= line[0];
							String campaignName= line[1];

							String date= line[2];
							date=DateUtil.getFormatedDate(date, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");

							String accountDate= line[3];
							accountDate=DateUtil.getFormatedDate(accountDate, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");

							String creativeId= line[4];
							String creativeName= line[5];
							String format= line[6];
							String placementId = line[7];
							String placementName = line[8];
							String platform = line[9];
							String sdk = line[10];
							long impressions = StringUtil.getLongValue(line[11]);      // session
							long shownImpressions = StringUtil.getLongValue(line[12]); // creativeViews
							long interactions = StringUtil.getLongValue(line[13]);
							long firstClickThroughs = StringUtil.getLongValue(line[14]);
							String rate=line[15];
							if(rate !=null && rate.indexOf("%")>=0){
								rate=rate.replace("%", "");
							}
							double interactionRate = StringUtil.getDoubleValue(rate, 6);

							String firstClickThroughRateStr=line[16];
							if(firstClickThroughRateStr !=null && firstClickThroughRateStr.indexOf("%")>=0){
								firstClickThroughRateStr=firstClickThroughRateStr.replace("%", "");
							}
							double firstClickThroughRate = StringUtil.getDoubleValue(firstClickThroughRateStr, 6);

							String key=accountDate+"-"+creativeName;
							if(objectMap.containsKey(key) ){
								CeltraDTO celtraDTO=objectMap.get(key);
								impressions=impressions+celtraDTO.getSessions();
								shownImpressions=shownImpressions+celtraDTO.getCreativeViews();
								interactions=interactions+celtraDTO.getInteractions();
								firstClickThroughs=firstClickThroughs+celtraDTO.getFirstClickThroughs();
								interactionRate=interactionRate+celtraDTO.getInteractionRate();
								firstClickThroughRate=firstClickThroughRate+celtraDTO.getFirstClickThroughRate();
								celtraDTO.setSessions(impressions);
								celtraDTO.setCreativeViews(shownImpressions);
								celtraDTO.setInteractions(interactions);
								celtraDTO.setFirstClickThroughs(firstClickThroughs);
								celtraDTO.setInteractionRate(interactionRate);
								celtraDTO.setFirstClickThroughRate(firstClickThroughRate);

								objectMap.put(key, celtraDTO);

							}else{
								CeltraDTO celtraDTO=new CeltraDTO(campaignId, campaignName, 
										date, accountDate, creativeId, 
										creativeName, format, placementId, 
										placementName, platform, sdk, 
										impressions, shownImpressions, 
										interactions, firstClickThroughs, 
										interactionRate, firstClickThroughRate);
								objectMap.put(key, celtraDTO);
							}


						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}

					}

					count++;
				}				
				reader.close();
				log.info("Total objects after grouped by creativeName:"+objectMap.size());
				for(String key:objectMap.keySet()){

					CeltraDTO celtraDTO=objectMap.get(key);
					String accountDate=celtraDTO.getAccountDate();
					long sessions=celtraDTO.getSessions();
					long creativeViews=celtraDTO.getCreativeViews();
					long impressions=creativeViews;

					String creativeName=celtraDTO.getCreativeName();

					long firstClickThroughs=celtraDTO.getFirstClickThroughs();

					CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
					rootObj.setLoadTimestamp(loadTimestamp);
					rootObj.setDate(accountDate);

					rootObj.setTotalImpressions(creativeViews);
					rootObj.setImpressionsCPM(impressions);
					rootObj.setRequests(sessions);
					rootObj.setServed(impressions);

					//rootObj.setOrderId(campaignId);
					rootObj.setOrder(celtraDTO.getCampaignName());
					//rootObj.setLineItemId(creativeId);
					if(lineItemIdMap!=null && lineItemIdMap.containsKey(creativeName)){					
						rootObj.setLineitemId(lineItemIdMap.get(creativeName));
					}
					rootObj.setLineItem(creativeName);
					//rootObj.setCreativeId(creativeId);					
					rootObj.setCreative(creativeName);

					rootObj.setSiteName(celtraDTO.getPlacementName());
					rootObj.setSiteId(celtraDTO.getPlacementId());

					rootObj.setTotalClicks(firstClickThroughs);
					rootObj.setCTR(celtraDTO.getFirstClickThroughRate());

					rootObj.setAdvertiser(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER);
					rootObj.setAdvertiserId(Long.parseLong(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID));
					rootObj.setAgency(LinMobileConstants.XAD_AGENCY_GOLFSMITH);

					String sdk=celtraDTO.getSdk();
					if(sdk !=null && sdk.equalsIgnoreCase("MobileWeb")){
						rootObj.setSiteType(sdk);
					}else{
						rootObj.setSiteType("App");
					}					

					rootObj.setCreativeType("Rich Media");
					rootObj.setCostType("CPM");

					if(lineItemImpressionMap.containsKey(creativeName)){
						long cumulativeImpressions=lineItemImpressionMap.get(creativeName)+impressions;
						lineItemImpressionMap.put(creativeName, cumulativeImpressions);
						rootObj.setLineitemLifetimeImpressions(cumulativeImpressions);
					}else{
						lineItemImpressionMap.put(creativeName, impressions);
						rootObj.setLineitemLifetimeImpressions(impressions);
					}

					if(lineItemClickMap.containsKey(creativeName)){
						long cumulativeClicks=lineItemClickMap.get(creativeName)+firstClickThroughs;
						lineItemClickMap.put(creativeName, cumulativeClicks);
						rootObj.setLineitemLifetimeClicks(cumulativeClicks);
					}else{
						lineItemClickMap.put(creativeName, firstClickThroughs);
						rootObj.setLineitemLifetimeImpressions(impressions);
					}

					String market="";
					String campaignCategory="";
					if(creativeName !=null && creativeName.indexOf("DC") >=0){
						market="DC";
					}else if(creativeName !=null && creativeName.indexOf("SF") >=0){
						market="SF";
					}

					if(creativeName !=null && creativeName.indexOf("15%") >=0){
						campaignCategory=LinMobileConstants.GOLFSMITH_CAMPAIGN_CATEGORY_LOW_PRICE;
					}else if(creativeName !=null && ( creativeName.indexOf("CF") >=0 
							|| creativeName.indexOf("Custom Fitting") >=0 )){
						campaignCategory=LinMobileConstants.GOLFSMITH_CAMPAIGN_CATEGORY_CUSTOM_FIT;
					}
					rootObj.setColumn3(market);
					rootObj.setColumn4(campaignCategory);							

					double actualRate=0;
					if(creativeName !=null && creativeName.indexOf("300-") >=0){
						actualRate=8.4;;
					}else{
						actualRate=9.0;
					}	
					long goalQty= lineItemQtyMap.get(creativeName);
					rootObj.setGoalQty(goalQty);

					rootObj.setRate(actualRate);							
					rootObj.setECPM(actualRate);

					double calculatedBudget=ReportUtil.calculateBudget(goalQty,actualRate);
					rootObj.setBudget(calculatedBudget);



					String orderStartDate=DateUtil.getFormatedDate(
							LinMobileConstants.XAD_ORDER_START_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
					String orderEndDate=DateUtil.getFormatedDate(
							LinMobileConstants.XAD_ORDER_END_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
					rootObj.setOrderStartDate(orderStartDate);
					rootObj.setOrderEndDate(orderEndDate);
					rootObj.setLineItemStartDate(orderStartDate);
					rootObj.setLineItemEndDate(orderEndDate);

					int days=(int) DateUtil.getDifferneceBetweenTwoDates(LinMobileConstants.XAD_ORDER_START_DATE, 
							LinMobileConstants.XAD_ORDER_END_DATE, "MM/dd/yyyy");

					long impressionTargetPerDay= goalQty/days;							
					double deliveryIndicator=((double)impressions / impressionTargetPerDay)*100;
					rootObj.setDeliveryIndicator(Math.round((deliveryIndicator*100.0))/100.0);


					rootObj.setPublisherId(StringUtil
							.getLongValue(LinMobileConstants.CELTRA_PUBLISHER_ID));
					rootObj.setPublisherName(LinMobileConstants.CELTRA_PUBLISHER_NAME);
					rootObj.setChannelId(Integer
							.parseInt(LinMobileConstants.CELTRA_CHANNEL_ID));
					rootObj.setChannelName(LinMobileConstants.CELTRA_CHANNEL_NAME);
					rootObj.setChannelType(LinMobileConstants.CELTRA_CHANNEL_TYPE);
					rootObj.setSalesType(LinMobileConstants.CELTRA_SALES_TYPE);
					rootObj.setDataSource(LinMobileConstants.CELTRA_CHANNEL_NAME);


					rootObj.setPassback(ReportUtil.getPassback(rootObj));
					rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));

				    reportList.add(rootObj);
				}
				log.info("reportList:"+reportList.size());

			} catch (FileNotFoundException e) {
				log.severe("FileNotFoundException:" + e.getMessage()+" and fileURL:"+fileName);

			} catch (LockException e) {
				log.severe("LockException:" + e.getMessage());

			} catch (IOException e) {
				log.severe("IOException:" + e.getMessage());

			}
			return reportList;
	 }*/


	 
	 
	 /*
	  * Create Tribune Core Performance csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<CorePerformanceReportObj> (List of CorePerformanceReportObj)
	  */
	 public static List<CorePerformanceReportObj> createTribuneCSVReportFromCloudStorage(
				String fileName, String dirName) {
			List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME
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
				Map<String,Long> lineItemImpressionMap=new LinkedHashMap<String, Long>();
				Map<String,Long> lineItemClickMap=new LinkedHashMap<String, Long>();

				Map<String,Double> lineItemRateMap=new LinkedHashMap<String, Double>();
				lineItemRateMap.put("RON ROS Multisize Leaderboard 728x90 & Mobile Banner 320x50 (DC)", (double)3.0);
				lineItemRateMap.put("RON Sports Multisize Leaderboard 728x90 & Mobile Banner 320x50 (DC)", (double)5.0);
				lineItemRateMap.put("RON ROS Multisize Leaderboard 728x90 & Mobile Banner 320x50 (SF)", (double)3.0);
				lineItemRateMap.put("RON Sports Multisize Leaderboard 728x90 & Mobile Banner 320x50 (SF)", (double)5.0);

				Map<String,Long> lineItemQtyMap=new LinkedHashMap<String, Long>();
				lineItemQtyMap.put("RON ROS Multisize Leaderboard 728x90 & Mobile Banner 320x50 (DC)", (long) 114000);
				lineItemQtyMap.put("RON Sports Multisize Leaderboard 728x90 & Mobile Banner 320x50 (DC)", (long) 6750);
				lineItemQtyMap.put("RON ROS Multisize Leaderboard 728x90 & Mobile Banner 320x50 (SF)", (long) 115000);
				lineItemQtyMap.put("RON Sports Multisize Leaderboard 728x90 & Mobile Banner 320x50 (SF)", (long) 6000);

				for (String[] line : allElements) {
					if (count == 0) {
						log.info("Skip first row...");
						headerRow=line;
					} else {
						if (headerRow.length >= 10) {
							String lineItem=line[0];
							String creative=line[1];
							String creativeSize=line[2];
							String date = line[3];
							date=DateUtil.getFormatedDate(date, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							String lineItemId=line[4];
							String creativeId=line[5];
							String deliveryIndicatorStr=line[6];
							if(deliveryIndicatorStr !=null ){
								deliveryIndicatorStr.replaceAll("%", "");
							}
							double  deliveryIndicator = StringUtil.getDoubleValue(deliveryIndicatorStr);
							long  impressions = StringUtil.getLongValue(line[7]);
							long  clicks = StringUtil.getLongValue(line[8]);
							double CTR=StringUtil.getDoubleValue(line[9]);


							CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
							rootObj.setLoadTimestamp(loadTimestamp);

							rootObj.setDate(date);

							rootObj.setTotalImpressions(impressions);
							rootObj.setImpressionsCPM(impressions);
							rootObj.setRequests(impressions);
							rootObj.setServed(impressions);

							rootObj.setTotalClicks(clicks);
							rootObj.setCTR(CTR);							
							rootObj.setDeliveryIndicator(deliveryIndicator);

							rootObj.setLineItem(lineItem);
							rootObj.setLineitemId(StringUtil.getLongValue(lineItemId));							
							rootObj.setCreative(creative);
							rootObj.setCreativeId(StringUtil.getLongValue(creativeId));
							rootObj.setCreativeSize(creativeSize);
							rootObj.setCreativeType("Rich Media");		
							rootObj.setSiteName("Tribune");
							rootObj.setCostType("CPM");

							if(lineItemImpressionMap.containsKey(lineItem)){
								long cumulativeImpressions=lineItemImpressionMap.get(lineItem)+impressions;
								lineItemImpressionMap.put(lineItem, cumulativeImpressions);
								rootObj.setLineitemLifetimeImpressions(cumulativeImpressions);
							}else{
								lineItemImpressionMap.put(lineItem, impressions);
								rootObj.setLineitemLifetimeImpressions(impressions);
							}

							if(lineItemClickMap.containsKey(lineItem)){
								long cumulativeClicks=lineItemClickMap.get(lineItem)+clicks;
								lineItemClickMap.put(lineItem, cumulativeClicks);
								rootObj.setLineitemLifetimeClicks(cumulativeClicks);
							}else{
								lineItemClickMap.put(lineItem, clicks);
								rootObj.setLineitemLifetimeImpressions(impressions);
							}

							double rate=0;
							if(lineItem!=null ){
								rate=lineItemRateMap.get(lineItem);
							}
							rootObj.setRate(rate);
							rootObj.setECPM(rate);

							long goalQty=0;
							if(lineItem!=null ){
								goalQty=lineItemQtyMap.get(lineItem);
							}
							rootObj.setGoalQty(goalQty);

							double calculatedBudget=ReportUtil.calculateBudget(goalQty,rate);
							rootObj.setBudget(calculatedBudget);

							String market="";
							String campaignCategory="";
							if(lineItem !=null && lineItem.indexOf("DC") >=0){
								market="DC";
							}else if(lineItem !=null && lineItem.indexOf("SF") >=0){
								market="SF";
							}

							
							rootObj.setColumn3(market);
							rootObj.setColumn4(campaignCategory);	

							String orderStartDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_START_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							String orderEndDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_END_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							rootObj.setOrderStartDate(orderStartDate);
							rootObj.setOrderEndDate(orderEndDate);
							rootObj.setLineItemStartDate(orderStartDate);
							rootObj.setLineItemEndDate(orderEndDate);

							rootObj.setAgency(LinMobileConstants.XAD_AGENCY_GOLFSMITH);
							rootObj.setOrder(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ORDER_NAME);
							rootObj.setAdvertiser(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER);
							rootObj.setAdvertiserId(Long.parseLong(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID));


							rootObj.setPublisherId(StringUtil
									.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
							rootObj.setChannelId(Integer
									.parseInt(LinMobileConstants.TRIBUNE_CHANNEL_ID));
							rootObj.setChannelName(LinMobileConstants.TRIBUNE_CHANNEL_NAME);
							rootObj.setChannelType(LinMobileConstants.TRIBUNE_CHANNEL_TYPE);
							rootObj.setSalesType(LinMobileConstants.TRIBUNE_SALES_TYPE);
							rootObj.setDataSource("Tribune");

							rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));

							reportList.add(rootObj);
						}
					}
					count++;
				}

				readChannel.close();
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
	  * Create Celtra RichMedia trafficking csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<RichMediaCommonReportObj> (List of RichMediaCommonReportObj)
	  */
	 public static List<RichMediaCommonReportObj> createRichMediaCeltraTraffickingCSVReportFromCloudStorage(
				String fileName, String dirName) {
			//List<RichMediaReportTraffickingObj> reportList = new ArrayList<RichMediaReportTraffickingObj>();
		    List<RichMediaCommonReportObj> reportList = new ArrayList<RichMediaCommonReportObj>();
			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME
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
						if (headerRow.length >= 17) {

							String campaignId= line[0];
							String campaignName= line[1];

							String date= line[2];
							date=DateUtil.getFormatedDate(date, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");

							String accountDate= line[3];
							accountDate=DateUtil.getFormatedDate(accountDate, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");

							String creativeId= line[4];
							String creativeName= line[5];
							String format= line[6];
							String placementId = line[7];
							String placementName = line[8];
							String platform = line[9];
							String sdk = line[10];
							long impressions = StringUtil.getLongValue(line[11]);
							long shownImpressions = StringUtil.getLongValue(line[12]);
							long interactions = StringUtil.getLongValue(line[13]);
							long firstClickThroughs = StringUtil.getLongValue(line[14]);
							String rate=line[15];
							if(rate !=null && rate.indexOf("%")>=0){
								rate=rate.replace("%", "");
							}
							double interactionRate = StringUtil.getDoubleValue(rate, 6);

							String firstClickThroughRateStr=line[16];
							if(firstClickThroughRateStr !=null && firstClickThroughRateStr.indexOf("%")>=0){
								firstClickThroughRateStr=firstClickThroughRateStr.replace("%", "");
							}
							double firstClickThroughRate = StringUtil.getDoubleValue(firstClickThroughRateStr, 6);


							RichMediaCommonReportObj rootObj=new RichMediaCommonReportObj();
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(accountDate);
							rootObj.setTotalImpressions(impressions);
							rootObj.setAdFormat(format);
							rootObj.setShownImpressions(shownImpressions);
							rootObj.setOrderId(campaignId);
							rootObj.setOrder(campaignName);
							rootObj.setLineItemId(creativeId);
							rootObj.setLineItem(creativeName);
							rootObj.setCreativeId(creativeId);
							rootObj.setCreative(creativeName);
							rootObj.setSiteName(placementName);
							rootObj.setSiteId(placementId);
							rootObj.setTotalInteractions(interactions);
							rootObj.setInteractionRate(interactionRate);
							rootObj.setTotalClicks(firstClickThroughs);
							rootObj.setCTR(firstClickThroughRate);

							rootObj.setAdvertiser(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER);
							rootObj.setAdvertiserId(Long.parseLong(LinMobileConstants.LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID));


							if(sdk !=null && sdk.equalsIgnoreCase("MobileWeb")){
								rootObj.setSiteType(sdk);
							}else{
								rootObj.setSiteType("App");
							}
							/*RichMediaReportTraffickingObj rootObj = new RichMediaReportTraffickingObj(campaignId, campaignName, date, 
									  accountDate, creativeId, creativeName, format, placementId, placementName, 
									  platform, sdk, impressions, shownImpressions, interactions, firstClickThroughs, 
									  interactionRate, firstClickThroughRate);*/

							rootObj.setPublisherId(StringUtil
									.getLongValue(LinMobileConstants.CELTRA_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.CELTRA_PUBLISHER_NAME);
							rootObj.setChannelId(Integer
									.parseInt(LinMobileConstants.CELTRA_CHANNEL_ID));
							rootObj.setChannelName(LinMobileConstants.CELTRA_CHANNEL_NAME);
							rootObj.setChannelType(LinMobileConstants.CELTRA_CHANNEL_TYPE);
							rootObj.setSalesType(LinMobileConstants.CELTRA_SALES_TYPE);
							rootObj.setDataSource(LinMobileConstants.CELTRA_CHANNEL_NAME);

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

			} catch (LockException e) {
				log.severe("LockException:" + e.getMessage());

			} catch (IOException e) {
				log.severe("IOException:" + e.getMessage());

			}
			return reportList;
		}



	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list for LinOne test Order (Lin Digital)
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<LinOneRichMediaReportObj>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static List<LinOneRichMediaReportObj> readRichMediaLinOneRawCSVFromCloudStorage(String fileName,String dirName) { 
		    List<LinOneRichMediaReportObj> dataList=new ArrayList<LinOneRichMediaReportObj>();

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

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();

					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
						}else{
						if(line.length >= 64){

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
							String CREATIVE_ID=line[8];
							String CREATIVE_NAME=line[9];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[10];
							String CREATIVE_TYPE=line[11];
							String CREATIVE_TYPE_ID=line[12];
							String LINE_ITEM_LABELS=line[13];				
							String goalQty=line[14];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

						    long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[15]);

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

						    String ORDER_TRAFFICKER=line[29];

						    String ORDER_SECONDARY_TRAFFICKERS=line[30];
						    String ORDER_SALESPERSON=line[31];
						    String ORDER_SECONDARY_SALESPEOPLE=line[32];
						    String ORDER_LABELS=line[33];
						    String ADVERTISER_LABELS=line[34];
						    String CREATIVE_CLICK_THROUGH_URL=line[35];
						    String CREATIVE_OR_CREATIVE_SET=line[36];

						    long RICH_MEDIA_VIDEO_COMPLETES=StringUtil.getLongValue(line[37]);
						    long RICH_MEDIA_DISPLAY_TIME=StringUtil.getLongValue(line[38]);
						    long RICH_MEDIA_EXPANSIONS=StringUtil.getLongValue(line[39]);
						    long RICH_MEDIA_VIDEO_VIEW_RATE=StringUtil.getLongValue(line[40]);
						    long RICH_MEDIA_VIDEO_MIDPOINTS=StringUtil.getLongValue(line[41]);
						    long RICH_MEDIA_INTERACTION_IMPRESSIONS=StringUtil.getLongValue(line[42]);
						    long RICH_MEDIA_VIDEO_PAUSES=StringUtil.getLongValue(line[43]);
						    long RICH_MEDIA_CUSTOM_CONVERSION_COUNT_VALUE=StringUtil.getLongValue(line[44]);
						    long RICH_MEDIA_VIDEO_REPLAYS=StringUtil.getLongValue(line[45]);
						    long RICH_MEDIA_EXPANDING_TIME=StringUtil.getLongValue(line[46]);
						    long RICH_MEDIA_MANUAL_CLOSES=StringUtil.getLongValue(line[47]);
						    long RICH_MEDIA_VIDEO_UNMUTES=StringUtil.getLongValue(line[48]);
						    double RICH_MEDIA_AVERAGE_INTERACTION_TIME=StringUtil.getDoubleValue(line[49],6);
						    long RICH_MEDIA_BACKUP_IMAGES=StringUtil.getLongValue(line[50]);
						    long RICH_MEDIA_INTERACTION_COUNT=StringUtil.getLongValue(line[51]);
						    double RICH_MEDIA_VIDEO_INTERACTION_RATE=StringUtil.getDoubleValue(line[52],6);
						    long RICH_MEDIA_INTERACTION_TIME=StringUtil.getLongValue(line[53]);
						    long RICH_MEDIA_VIDEO_VIEW_TIME=StringUtil.getLongValue(line[54]);
						    long RICH_MEDIA_VIDEO_INTERACTIONS=StringUtil.getLongValue(line[55]);
						    double RICH_MEDIA_AVERAGE_DISPLAY_TIME=StringUtil.getDoubleValue(line[56],6);

						    double AD_SERVER_CPM_AND_CPC_REVENUE=ReportUtil.convertMoney(line[57]);		

						    long RICH_MEDIA_VIDEO_STOPS=StringUtil.getLongValue(line[58]);
						    long RICH_MEDIA_FULL_SCREEN_IMPRESSIONS=StringUtil.getLongValue(line[59]);
						    double RICH_MEDIA_INTERACTION_RATE=StringUtil.getDoubleValue(line[60],6);
						    long RICH_MEDIA_VIDEO_MUTES=StringUtil.getLongValue(line[61]);
						    long RICH_MEDIA_CUSTOM_CONVERSION_TIME_VALUE=StringUtil.getLongValue(line[62]);
						    long RICH_MEDIA_VIDEO_PLAYES=StringUtil.getLongValue(line[63]);

							LinOneRichMediaReportObj rootObj=new LinOneRichMediaReportObj();	
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
							rootObj.setSalesPerson(ORDER_SALESPERSON);
							rootObj.setOrderId(ORDER_ID);
							rootObj.setOrder(ORDER_NAME);
							rootObj.setOrderLifetimeClicks(ORDER_LIFETIME_CLICKS);
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
							rootObj.setLineItemId(LINE_ITEM_ID);
							rootObj.setLineItemType(LINE_ITEM_TYPE);
							rootObj.setLineItemEndDate(LINE_ITEM_END_DATE);
							rootObj.setLineItemEndTime(LINE_ITEM_END_DATE_TIME);
							rootObj.setLineItemStartDate(LINE_ITEM_START_DATE);
							rootObj.setLineItemStartTime(LINE_ITEM_START_DATE_TIME);
							rootObj.setLineItemLifetimeClicks(LINE_ITEM_LIFETIME_CLICKS);
							rootObj.setLineItemLifetimeImpressions(LINE_ITEM_LIFETIME_IMPRESSIONS);
							rootObj.setContractedQuantity(LINE_ITEM_CONTRACTED_QUANTITY);
							rootObj.setGoalQuantity(LINE_ITEM_GOAL_QUANTITY);							
							rootObj.setCostType(LINE_ITEM_COST_TYPE);
							rootObj.setRate(rate);

							rootObj.setOrderLabels(ORDER_LABELS);
							rootObj.setAdvertiserLabels(ADVERTISER_LABELS);
							rootObj.setSecondarySalesPeople(ORDER_SECONDARY_SALESPEOPLE);
							rootObj.setSecondaryTraffickers(ORDER_SECONDARY_TRAFFICKERS);
							rootObj.setClickThroughURL(CREATIVE_CLICK_THROUGH_URL);


							rootObj.setRichMediaAverageDisplayTime(RICH_MEDIA_AVERAGE_DISPLAY_TIME);
							rootObj.setRichMediaAverageInteractionTime(RICH_MEDIA_AVERAGE_INTERACTION_TIME);
							rootObj.setRichMediaBackupImages(RICH_MEDIA_BACKUP_IMAGES);
							rootObj.setRichMediaCustomConversionCountValue(RICH_MEDIA_CUSTOM_CONVERSION_COUNT_VALUE);
							rootObj.setRichMediaCustomConversionTimeValue(RICH_MEDIA_CUSTOM_CONVERSION_TIME_VALUE);
							rootObj.setRichMediaDisplayTime(RICH_MEDIA_DISPLAY_TIME);
							rootObj.setRichMediaExpandingTime(RICH_MEDIA_EXPANDING_TIME);
							rootObj.setRichMediaExpansions(RICH_MEDIA_EXPANSIONS);
							rootObj.setRichMediaFullScreenImpressions(RICH_MEDIA_FULL_SCREEN_IMPRESSIONS);
							rootObj.setRichMediaInteractionCount(RICH_MEDIA_INTERACTION_COUNT);
							rootObj.setRichMediaInteractionImpressions(RICH_MEDIA_INTERACTION_IMPRESSIONS);
							rootObj.setRichMediaInteractionRate(RICH_MEDIA_INTERACTION_RATE);
							rootObj.setRichMediaInteractionTime(RICH_MEDIA_INTERACTION_TIME);
							rootObj.setRichMediaManualCloses(RICH_MEDIA_MANUAL_CLOSES);
							rootObj.setRichMediaVideoCompletes(RICH_MEDIA_VIDEO_COMPLETES);
							rootObj.setRichMediaVideoInteraction(RICH_MEDIA_VIDEO_INTERACTIONS);
							rootObj.setRichMediaVideoInteractionRate(RICH_MEDIA_VIDEO_INTERACTION_RATE);
							rootObj.setRichMediaVideoMidPoints(RICH_MEDIA_VIDEO_MIDPOINTS);
							rootObj.setRichMediaVideoMutes(RICH_MEDIA_VIDEO_MUTES);
							rootObj.setRichMediaVideoPauses(RICH_MEDIA_VIDEO_PAUSES);
							rootObj.setRichMediaVideoPlays(RICH_MEDIA_VIDEO_PLAYES);
							rootObj.setRichMediaVideoReplays(RICH_MEDIA_VIDEO_REPLAYS);
							rootObj.setRichMediaVideoStops(RICH_MEDIA_VIDEO_STOPS);
							rootObj.setRichMediaVideoUnMutes(RICH_MEDIA_VIDEO_UNMUTES);
							rootObj.setRichMediaVideoViewRate(RICH_MEDIA_VIDEO_VIEW_RATE);
							rootObj.setRichMediaVideoViewTime(RICH_MEDIA_VIDEO_VIEW_TIME);

							rootObj.setBookedRevenueExcludeCPD(AD_SERVER_CPM_AND_CPC_REVENUE);

                            rootObj.setDataSource("DFP");							
							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
							rootObj.setChannelId(5);
							rootObj.setChannelName("National Sales Direct");
							rootObj.setChannelType("National Sales");
							rootObj.setSalesType("Direct");	

							dataList.add(rootObj);

						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}
					  }
					  count++;
				    }
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
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list for LinOne
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<RichMediaCommonReportObj>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static List<RichMediaCommonReportObj> createRichMediaLinOneCSVReportFromCloudStorage(String fileName,String dirName) { 
		    List<RichMediaCommonReportObj> dataList=new ArrayList<RichMediaCommonReportObj>();

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

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();

					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
						}else{
						if(line.length >= 35){

							String DATE=line[0];
							long ADVERTISER_ID=StringUtil.getLongValue(line[1]);
							String ADVERTISER_NAME=line[2];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}							
							//long ORDER_ID=StringUtil.getLongValue(line[3]);
							String ORDER_ID=line[3];
							String ORDER_NAME=line[4];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(","," ");
							}							
							//long LINE_ITEM_ID=StringUtil.getLongValue(line[5]);
							String LINE_ITEM_ID=line[5];
							String LINE_ITEM_NAME=line[6];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
							}
							String LINE_ITEM_TYPE=line[7];
							String CREATIVE_ID=line[8];
							String CREATIVE_NAME=line[9];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[10];
							String CREATIVE_TYPE=line[11];
							String CREATIVE_TYPE_ID=line[12];

						    String orderStartDate=line[13];
							String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						    
							String ORDER_START_DATE=ORDER_START_DATE_TIME;

							String LINE_ITEM_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(line[14],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;

							String orderEndDate=line[15];
							String ORDER_END_DATE_TIME="";
							String ORDER_END_DATE="";
							if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
							 	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						
							  	 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}

							String lineItemEndDate=line[16];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");							    
						    	LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							}

						    String ORDER_AGENCY=line[17];
						    long ORDER_AGENCY_ID=StringUtil.getLongValue(line[18]);

							String goalQty=line[19];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);				   

						    String LINE_ITEM_COST_PER_UNIT=line[20];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

						    long AD_SERVER_IMPRESSIONS=StringUtil.getLongValue(line[21]);							   


						    long INTERACTION_COUNT=StringUtil.getLongValue(line[22]);
						    long DISPLAY_TIME=StringUtil.getLongValue(line[23]);

						    double AD_SERVER_CTR=0;
							if(LinMobileUtil.isNumeric(line[24])){
								AD_SERVER_CTR=(((double)Double.parseDouble(line[24])*100));
							}						    

						    long EXPANSIONS=StringUtil.getLongValue(line[25]);
						    long INTERACTION_TIME=StringUtil.getLongValue(line[26]);						    
						    long INTERACTION_IMPRESSIONS=StringUtil.getLongValue(line[27]);
						    double AVERAGE_DISPLAY_TIME=StringUtil.getDoubleValue(line[28],6);
						    long FULL_SCREEN_IMPRESSIONS=StringUtil.getLongValue(line[29]);						    
						    long EXPANDING_TIME=StringUtil.getLongValue(line[30]);

						    long AD_SERVER_CLICKS=StringUtil.getLongValue(line[31]);

						    long MANUAL_CLOSES=StringUtil.getLongValue(line[32]);						    
						    double AVERAGE_INTERACTION_TIME=StringUtil.getDoubleValue(line[33],6);
						    double INTERACTION_RATE=StringUtil.getDoubleValue(line[34],6);
						    long BACKUP_IMAGES=StringUtil.getLongValue(line[35]);


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
							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
							rootObj.setCTR(AD_SERVER_CTR);							

							rootObj.setAverageDisplayTime(AVERAGE_DISPLAY_TIME);
							rootObj.setAverageInteractionTime(AVERAGE_INTERACTION_TIME);
							rootObj.setBackupImages(BACKUP_IMAGES);					

							rootObj.setDisplayTime(DISPLAY_TIME);
							rootObj.setExpandingTime(EXPANDING_TIME);
							rootObj.setExpansions(EXPANSIONS);
							rootObj.setFullScreenImpressions(FULL_SCREEN_IMPRESSIONS);
							rootObj.setTotalInteractions(INTERACTION_COUNT);
							rootObj.setInteractionImpressions(INTERACTION_IMPRESSIONS);
							rootObj.setInteractionRate(INTERACTION_RATE);
							rootObj.setInteractionTime(INTERACTION_TIME);
							rootObj.setManualCloses(MANUAL_CLOSES);

                            rootObj.setDataSource("DFP");							
							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
							rootObj.setChannelId(5);
							rootObj.setChannelName("National Sales Direct");
							rootObj.setChannelType("National Sales");
							rootObj.setSalesType("Direct");	

							dataList.add(rootObj);

						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}
					  }
					  count++;
				    }
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
	  * This method read the raw file (generated from DFP dashboard) 
	  *  from specified cloud storage directory and 
	  * creates processed rich media custom event data list for LinOne 
	  *  
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<RichMediaCustomEventReportObj>
	  * 
	  * @param String fileName,String dirName,String dataSource
	  */
	 public static List<RichMediaCustomEventReportObj> readRichMediaLinOneCustomEventRawCSVFromCloudStorage(
			 String fileName,String dirName,String dataSource) { 
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

					CSVReader csvReader = new CSVReader(reader);

					List<String[]> allElements = csvReader.readAll();

					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
						}else{
						if(line.length >= 26){

							String DATE=line[0];	
							if(DATE!=null){
								DATE=DateUtil.getFormatedDate(DATE, "MM/dd/yy", "yyyy-MM-dd HH:mm:ss");
							}	

							String ADVERTISER_NAME=line[1];
							if(ADVERTISER_NAME!=null ){
								ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
							}	
							String ORDER_NAME=line[2];
							if(ORDER_NAME!=null ){
								ORDER_NAME=ORDER_NAME.replaceAll(","," ");
							}	
							String LINE_ITEM_TYPE=line[3];
							String LINE_ITEM_NAME=line[4];
							if(LINE_ITEM_NAME!=null ){
								LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
							}
							String CREATIVE_NAME=line[5];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[6];
							String CREATIVE_TYPE=line[7];
							String customEvent=line[8];
							String customEventType=line[9];

							long ADVERTISER_ID=StringUtil.getLongValue(line[10]);							
							String ORDER_ID=line[11];							
							String LINE_ITEM_ID=line[12];
							String CREATIVE_ID=line[13];
							String creativeTypeId=line[14];
							long customEventId=StringUtil.getLongValue(line[15]);

						    String orderStartDate=line[16];
							String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						    
							String ORDER_START_DATE=ORDER_START_DATE_TIME;

							String LINE_ITEM_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(line[17],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;

							String orderEndDate=line[18];
							String ORDER_END_DATE_TIME="";
							String ORDER_END_DATE="";
							if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
							 	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						
							  	 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}

							String lineItemEndDate=line[19];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");							    
						    	LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							}

						    String ORDER_AGENCY=line[20];
						    long ORDER_AGENCY_ID=StringUtil.getLongValue(line[21]);

						    String LINE_ITEM_COST_PER_UNIT=line[22];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);		

							String goalQty=line[23];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);						   				    

						    long CUSTOM_CONVERSION_COUNT_VALUE=StringUtil.getLongValue(line[24]);
						    long CUSTOM_CONVERSION_TIME_VALUE=StringUtil.getLongValue(line[25]);


						    RichMediaCustomEventReportObj rootObj=new RichMediaCustomEventReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);

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
							rootObj.setCustomEvent(customEvent);
							rootObj.setCustomEventId(customEventId);
							rootObj.setCustomEventType(customEventType);
							rootObj.setCustomConversionCountValue(CUSTOM_CONVERSION_COUNT_VALUE);
							rootObj.setCustomConversionTimeValue(CUSTOM_CONVERSION_TIME_VALUE);

							/*String market="";
							String campaignCategory="";
							if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("DC")>=0){
								market="DC";
							}else if(CREATIVE_NAME !=null && CREATIVE_NAME.indexOf("SF")>=0){
								market="SF";
							}							
							if(CREATIVE_NAME !=null && (CREATIVE_NAME.indexOf("fitting")>=0 || 
									CREATIVE_NAME.indexOf("fitting_coupon")>=0)){
								campaignCategory=LinMobileConstants.GOLFSMITH_CAMPAIGN_CATEGORY_CUSTOM_FIT;
							}else if(CREATIVE_NAME !=null && (CREATIVE_NAME.indexOf("lowprice")>=0 || 
									CREATIVE_NAME.indexOf("lowprice_coupon")>=0)){
								campaignCategory=LinMobileConstants.GOLFSMITH_CAMPAIGN_CATEGORY_LOW_PRICE;
							}
							rootObj.setMarket(market);
							rootObj.setCampaignCategory(campaignCategory);*/

                            rootObj.setDataSource(dataSource);		//dataSource=""DFP";					
							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
							rootObj.setChannelId(5);
							rootObj.setChannelName("National Sales Direct");
							rootObj.setChannelType("National Sales");
							rootObj.setSalesType("Direct");	

							dataList.add(rootObj);

						}else{
							log.warning("Invalid CSV file with column : "+line.length);
							break;
						}
					  }
					  count++;
				    }
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
	  * Create XAd RichMedia trafficking csv report after reading raw file
	  *  from cloud storage
	  *   
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * @param  - String fileName  
	  *           String dirName (Cloud storage folder name)
	  * @return - List<RichMediaCommonReportObj> (List of RichMediaCommonReportObj)
	  */
	 public static List<RichMediaCommonReportObj> createRichMediaXAdTraffickingCSVReportFromCloudStorage(
				String fileName, String dirName) {
			//List<RichMediaReportTraffickingObj> reportList = new ArrayList<RichMediaReportTraffickingObj>();
		    List<RichMediaCommonReportObj> reportList = new ArrayList<RichMediaCommonReportObj>();
			log.info("Reading from google cloud storage,fileName:" + fileName);
			FileService fileService = FileServiceFactory.getFileService();
			String filename = "/gs/" + LinMobileVariables.APPLICATION_BUCKET_NAME
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



							RichMediaCommonReportObj rootObj=new RichMediaCommonReportObj();
							rootObj.setLoadTimestamp(loadTimestamp);
							rootObj.setDate(date);
							rootObj.setTotalImpressions(impressions);
							rootObj.setShownImpressions(impressions);
							rootObj.setAdFormat(LinMobileConstants.XAD_AD_FORMAT);

							rootObj.setOrderId(LinMobileConstants.LIN_DIGITAL_XAD_ORDER_NAME);
							rootObj.setOrder(LinMobileConstants.LIN_DIGITAL_XAD_ORDER_NAME);
							rootObj.setLineItemId(adUnit);
							rootObj.setLineItem(adUnit);
							rootObj.setCreativeId(adUnit);
							rootObj.setCreative(adUnit);							

							rootObj.setInteractionRate(clickRate);
							rootObj.setTotalClicks(totalClicks);
							rootObj.setExpansions(totalClicks);
							double goalQty= 0; //LinMobileConstants.GOLFSMITH_GOAL_QTY;
							rootObj.setGoalQuantity(goalQty);							
							//rootObj.setRate(LinMobileConstants.GOLFSMITH_RATE);

							String orderStartDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_START_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							String orderEndDate=DateUtil.getFormatedDate(
									LinMobileConstants.XAD_ORDER_END_DATE, "MM/dd/yyyy", "yyyy-MM-dd HH:mm:ss");
							rootObj.setOrderStartDate(orderStartDate);
							rootObj.setOrderEndDate(orderEndDate);
							rootObj.setLineItemStartDate(orderStartDate);
							rootObj.setLineItemEndDate(orderEndDate);

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

			} catch (LockException e) {
				log.severe("LockException:" + e.getMessage());

			} catch (IOException e) {
				log.severe("IOException:" + e.getMessage());

			}
			return reportList;
	}	 
    


	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates a Map containing processed data list and faulted data list(if exist)
	  * on the basis of DFP network code
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceTargetReportObj>>
	  * 
	  * @param String fileName,String dirName,String networkCode
	  */
	 public static Map<String,List<CorePerformanceTargetReportObj>> readCorePerformanceTargetDFPRawCSVFromCloudStorage(String fileName,String dirName,String networkCode) { 

		    Map<String,List<CorePerformanceTargetReportObj>> allDataMap=new HashMap <String,List<CorePerformanceTargetReportObj>>();
		    if(networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
		    	allDataMap=readCorePerformanceTargetDFPRawCSVFromCloudStorage(fileName, dirName);
		    }else if(networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){

		    }else if(networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){

		    }
			return allDataMap;
	 }

	 /*
	  * This method read a file from specified cloud storage directory and 
	  * creates processed data list and rejected data list (if faulted data found)
	  * for Lin Media (DFP account)
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return Map<String,List<CorePerformanceTargetReportObj>>
	  * 
	  * @param String fileName,String dirName
	  */
	 public static Map<String,List<CorePerformanceTargetReportObj>> readCorePerformanceTargetDFPRawCSVFromCloudStorage(String fileName,String dirName) { 
		    List<CorePerformanceTargetReportObj> correctDataList=new ArrayList<CorePerformanceTargetReportObj>();
		    List<CorePerformanceTargetReportObj> faultedDataList=new ArrayList<CorePerformanceTargetReportObj>();
		    boolean faultedData=false;

		    Map<String,List<CorePerformanceTargetReportObj>> allDataMap=new HashMap <String,List<CorePerformanceTargetReportObj>>();

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

					List<String[]> allElements = csvReader.readAll();

					int count=0;					
					for(String[] line:allElements){			
						if(count==0){
							log.info("Skip first row...");
						}else{
						if(line.length >= 45){	

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
							long CREATIVE_ID=StringUtil.getLongValue(line[8]);
							String CREATIVE_NAME=line[9];
							if(CREATIVE_NAME!=null ){
								CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
							}
							String CREATIVE_SIZE=line[10];
							String CREATIVE_TYPE=line[11];
							String SALESPERSON_NAME=line[12];
							if(SALESPERSON_NAME!=null ){
								SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
							}

							String GENERIC_CRITERION_NAME=line[13];


							long CREATIVE_TYPE_ID=StringUtil.getLongValue(line[14]);
							long SALESPERSON_ID=StringUtil.getLongValue(line[15]);
							long CRITERION_ID=StringUtil.getLongValue(line[16]);

							String goalQty=line[17];
							if(!LinMobileUtil.isNumeric(goalQty)){
								goalQty="-1";
							}
							long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

						    long LINE_ITEM_CONTRACTED_QUANTITY=StringUtil.getLongValue(line[18]);

						    String LINE_ITEM_COST_PER_UNIT=line[19];
						    if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
						    	LINE_ITEM_COST_PER_UNIT="0";
						    }
						    double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

						    String LINE_ITEM_COST_TYPE=line[20];
						    long LINE_ITEM_LIFETIME_CLICKS=StringUtil.getLongValue(line[21]);
						    long LINE_ITEM_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[22]);

						    String LINE_ITEM_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(line[23],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");						    
						    String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;

						    String lineItemEndDate=line[24];
						    String LINE_ITEM_END_DATE_TIME="";
						    String LINE_ITEM_END_DATE="";
						    if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
						    	LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
							    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
							 }

						    String ORDER_AGENCY=line[25];
						    long ORDER_AGENCY_ID=StringUtil.getLongValue(line[26]);
						    long ORDER_LIFETIME_CLICKS=StringUtil.getLongValue(line[27]);
						    long ORDER_LIFETIME_IMPRESSIONS=StringUtil.getLongValue(line[28]);
						    String ORDER_PO_NUMBER=line[29];

						    String orderStartDate=line[30];
						    String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    String ORDER_START_DATE=ORDER_START_DATE_TIME;

						    String orderEndDate=line[31];
						    String ORDER_END_DATE_TIME="";
						    String ORDER_END_DATE="";
						    if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
						    	 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
								 ORDER_END_DATE=ORDER_END_DATE_TIME;
							}

						    String ORDER_TRAFFICKER=line[32];							
							long AD_SERVER_IMPRESSIONS=StringUtil.getLongValue(line[33]);

							double AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=0;
							if(LinMobileUtil.isNumeric(line[34])){
								AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=(((double)Double.parseDouble(line[34])*100));
							}							
							AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=Math.round((AD_EXCHANGE_LINE_ITEM_LEVEL_CTR*100.0))/100.0;

							double AD_SERVER_CTR=0;
							if(LinMobileUtil.isNumeric(line[35])){
								AD_SERVER_CTR=(((double)Double.parseDouble(line[35])*100));
							}							
							AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;

							double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							if(LinMobileUtil.isNumeric(line[36])){
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(line[36])*100));
							}
							double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=ReportUtil.convertMoney(line[37]);
							double AD_SERVER_CPM_AND_CPC_REVENUE=ReportUtil.convertMoney(line[38]);
							long AD_SERVER_CLICKS=StringUtil.getLongValue(line[39]);
							double AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE=ReportUtil.convertMoney(line[40]);
							long AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS=StringUtil.getLongValue(line[41]);

							double AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
							if(LinMobileUtil.isNumeric(line[42])){
								AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(line[42])*100));
							}
							long AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS=StringUtil.getLongValue(line[43]);	
							double AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM=ReportUtil.convertMoney(line[44]);
							double AD_SERVER_DELIVERY_INDICATOR=0.0;
							if(LinMobileUtil.isNumeric(line[45])){								
								AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(line[45])*100));
								AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
							}


							double AD_SERVER_ALL_REVENUE=AD_SERVER_CPM_AND_CPC_REVENUE;	

							if(ADVERTISER_NAME !=null && ADVERTISER_NAME.equalsIgnoreCase("WLIN | Google Ad Exchange - AdEx (Remnant)")){
								AD_SERVER_CLICKS=AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS;
								AD_SERVER_IMPRESSIONS=AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS;
								AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS;
								AD_SERVER_CTR=AD_EXCHANGE_LINE_ITEM_LEVEL_CTR;
								AD_SERVER_ALL_REVENUE=AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE;								
							}

							long requests=(long) ((AD_SERVER_IMPRESSIONS*100) /AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS);
							double fillRate=ReportUtil.calculateFillRate(AD_SERVER_IMPRESSIONS, requests);


							CorePerformanceTargetReportObj rootObj=new CorePerformanceTargetReportObj();	
							rootObj.setLoadTimestamp(loadTimestamp);
							if(DATE!=null){
								DATE=DateUtil.getFormatedDate(DATE, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");								
							}							
							rootObj.setDate(DATE);

							rootObj.setAdvertiserId(ADVERTISER_ID);
							rootObj.setAdvertiser(ADVERTISER_NAME);


							rootObj.setTotalClicks(AD_SERVER_CLICKS);
							rootObj.setTotalImpressions(AD_SERVER_IMPRESSIONS);
							if(LINE_ITEM_COST_TYPE !=null && LINE_ITEM_COST_TYPE.equalsIgnoreCase("CPD")){
								rootObj.setClicksCPD(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPD(AD_SERVER_IMPRESSIONS);
							}else{
								rootObj.setClicksCPM(AD_SERVER_CLICKS);
								rootObj.setImpressionsCPM(AD_SERVER_IMPRESSIONS);
							}
							if(GENERIC_CRITERION_NAME !=null){
								String [] target=GENERIC_CRITERION_NAME.split("=");
								if(target !=null && target.length==2){
									rootObj.setTargetCategory(target[0]);
									rootObj.setTargetValue(target[1]);
								}								
							}
							rootObj.setRequests(requests);
							rootObj.setFillRate(fillRate);

							rootObj.setAdserverCPMAndCPCRevenue(AD_SERVER_CPM_AND_CPC_REVENUE);
							rootObj.setTotalRevenue(AD_SERVER_ALL_REVENUE);


							rootObj.setCTR(AD_SERVER_CTR);							
							rootObj.setECPM(AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM); //AD_SERVER_WITH_CPD_AVERAGE_ECPM

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

							rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
							rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
							rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);

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
									//rootObj.setChannelName("Lin Media Direct");
									rootObj.setChannelName("Local Sales Direct");
									rootObj.setChannelType("Local Sales");
									rootObj.setSalesType("Direct");									
								}else{
									faultedData=true;
								}
							}

							rootObj.setPassback(1);
							rootObj.setDirectDelivered(0);


							if(faultedData){
								faultedDataList.add(rootObj);
								faultedData=false;  // reset it to false after add in list
							}else{
								faultedData=false;
								correctDataList.add(rootObj);
							}

						}else{
							log.warning("Invalid CSV file : columns are not matching..");
						}
					  }
					  count++;
				    }

					allDataMap.put("CorrectData", correctDataList);
					allDataMap.put("InCorrectData", faultedDataList);
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
	  * This method read a target dfp csv file from specified cloud storage directory 
	  * on the basis of DFP network code
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return List<DFPTargetReportObj>
	  * 
	  * @param String fileName,String dirName,String networkCode,String publisherId,String publisherName
	  */
	 public static List<DFPTargetReportObj> readDFPTargetDFPRawCSVFromCloudStorage(String fileName,String dirName,
			 String networkCode,String publisherId,String publisherName,String bucketName) { 

		    List<DFPTargetReportObj> resultList=new ArrayList<DFPTargetReportObj>();
		    resultList=readDFPTargetRawCSV(fileName, dirName,networkCode,publisherId,publisherName,bucketName);
		    
		  /*  if(networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)
		    		|| networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)
		    		|| networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)
		    		|| networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)
		    		){
		    	resultList=readDFPTargetRawCSV(fileName, dirName,networkCode,publisherId,publisherName,bucketName);
		    }else {
		    	log.severe("No data processor has been made for this networkCode:"+networkCode);
		    }*/
			return resultList;
	 }




	 /*
	  * This method read LinDigital target data file from specified cloud storage directory and 
	  * creates processed data list
	  * for given networkCode (ex, Lin Media (DFP account), Lin Digital etc...)
	  * 
	  * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
	  * 
	  * @return  List<DFPTargetReportObj>
	  * 
	  * @param String fileName,String dirName,String networkCode,String publisherId,String publisherName
	  */
	 public static List<DFPTargetReportObj> readDFPTargetRawCSV(String fileName,
			 String dirName,String networkCode,String publisherId,String publisherName,String bucketName) { 
		    List<DFPTargetReportObj> correctDataList=new ArrayList<DFPTargetReportObj>();

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
					if(targetReportColsList==null){
						targetReportColsList=new ArrayList<String>();
						targetReportColsList=Arrays.asList(targetReportCols);
					}

					List<String[]> allElements = csvReader.readAll();
					Map<String,Integer> csvColumnMap=new HashMap<String,Integer>();
					int count=0;	

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
								csvColumnMap.get("Dimension.GENERIC_CRITERION_NAME")==null?"":line[csvColumnMap.get("Dimension.GENERIC_CRITERION_NAME")];
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
							if(publisherId !=null && publisherName !=null){
								rootObj.setPublisherId(StringUtil.getLongValue(publisherId));
							    rootObj.setPublisherName(publisherName);
							}else{
								if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){								
									rootObj.setPublisherId(StringUtil.getLongValue(
											LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
								    rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
								}else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
									rootObj.setPublisherId(StringUtil.getLongValue(
											LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID));
								    rootObj.setPublisherName(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME);
								}else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
									rootObj.setPublisherId(StringUtil.getLongValue(
											LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID));
								    rootObj.setPublisherName(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME);
								}
							}														
							correctDataList.add(rootObj);													    					    

					  }
					  count++;
				    }					
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
 * creates processed data list and rejected data list (if faulted data found)
 * for Lin Media (DFP account)
 * 
 * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
 * 
 * @return Map<String,List<CorePerformanceReportObj>>
 * 
 * @param String fileName,String dirName
 */
public static Map<String,List<CorePerformanceReportObj>> readNewCorePerformanceDFPRawCSVFromCloudStorage(String fileName,String dirName) { 
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
							}else{
								log.warning("This column not found in list :"+line[i]+" : Please update column list");
							}
						}
						log.info("Found cloumns in csv:"+csvColumnMap.size());
					}else{

						String DATE=line[csvColumnMap.get("Dimension.DATE")];
						long ADVERTISER_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ADVERTISER_ID")]);
						String ADVERTISER_NAME=line[csvColumnMap.get("Dimension.ADVERTISER_NAME")];
						if(ADVERTISER_NAME!=null ){
							ADVERTISER_NAME=ADVERTISER_NAME.replaceAll(","," ");
						}
						String AD_UNIT_ID_1=csvColumnMap.get("Ad unit ID 1")==null?"":line[csvColumnMap.get("Ad unit ID 1")];
						String AD_UNIT_ID_2=csvColumnMap.get("Ad unit ID 2")==null?"":line[csvColumnMap.get("Ad unit ID 2")];
						String AD_UNIT_ID_3=csvColumnMap.get("Ad unit ID 3")==null?"":line[csvColumnMap.get("Ad unit ID 3")];
						String AD_UNIT_ID_4=csvColumnMap.get("Ad unit ID 4")==null?"":line[csvColumnMap.get("Ad unit ID 4")];

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

						long ORDER_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.ORDER_ID")]);
						String ORDER_NAME=line[csvColumnMap.get("Dimension.ORDER_NAME")];
						if(ORDER_NAME!=null ){
							ORDER_NAME=ORDER_NAME.replaceAll(","," ");
						}

						long LINE_ITEM_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.LINE_ITEM_ID")]);
						String LINE_ITEM_NAME=line[csvColumnMap.get("Dimension.LINE_ITEM_NAME")];
						if(LINE_ITEM_NAME!=null ){
							LINE_ITEM_NAME=LINE_ITEM_NAME.replaceAll(","," ");
						}
						String LINE_ITEM_TYPE=line[csvColumnMap.get("Dimension.LINE_ITEM_TYPE")];
						long CREATIVE_ID=StringUtil.getLongValue(line[csvColumnMap.get("Dimension.CREATIVE_ID")]);
						String CREATIVE_NAME=line[csvColumnMap.get("Dimension.CREATIVE_NAME")];
						if(CREATIVE_NAME!=null ){
							CREATIVE_NAME=CREATIVE_NAME.replaceAll(","," ");
						}
						String CREATIVE_SIZE=line[csvColumnMap.get("Dimension.CREATIVE_SIZE")];
						String CREATIVE_TYPE=line[csvColumnMap.get("Dimension.CREATIVE_TYPE")];
						String SALESPERSON_NAME=line[csvColumnMap.get("Dimension.SALESPERSON_NAME")];
						if(SALESPERSON_NAME!=null ){
							SALESPERSON_NAME=SALESPERSON_NAME.replaceAll(","," ");
						}
						long SALESPERSON_ID=
								csvColumnMap.get("Dimension.SALESPERSON_ID")==null?0:StringUtil.getLongValue(line[csvColumnMap.get("Dimension.SALESPERSON_ID")]);

						String goalQty=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY")];
						if(!LinMobileUtil.isNumeric(goalQty)){
							goalQty="-1";
						}
						long LINE_ITEM_GOAL_QUANTITY=StringUtil.getLongValue(goalQty);

						long LINE_ITEM_CONTRACTED_QUANTITY=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY")]);

						String LINE_ITEM_COST_PER_UNIT=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT")];
						if(LINE_ITEM_COST_PER_UNIT !=null && (LINE_ITEM_COST_PER_UNIT.equals("-0"))){
							LINE_ITEM_COST_PER_UNIT="0";
						}
						double rate=ReportUtil.convertMoney(LINE_ITEM_COST_PER_UNIT);

						String LINE_ITEM_COST_TYPE=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE")];
						long LINE_ITEM_LIFETIME_CLICKS=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS")]);
						long LINE_ITEM_LIFETIME_IMPRESSIONS=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS")]);

						String LINE_ITEM_START_DATE_TIME=
						 DateUtil.getFormatedDateUsingJodaLib(line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_START_DATE_TIME")],"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						String LINE_ITEM_START_DATE=LINE_ITEM_START_DATE_TIME;

						String lineItemEndDate=line[csvColumnMap.get("DimensionAttribute.LINE_ITEM_END_DATE_TIME")];
						String LINE_ITEM_END_DATE_TIME="";
						String LINE_ITEM_END_DATE="";
						if(lineItemEndDate!=null && (!lineItemEndDate.equalsIgnoreCase("Unlimited"))){
							LINE_ITEM_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(lineItemEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						    LINE_ITEM_END_DATE=LINE_ITEM_END_DATE_TIME;
						 }

						String ORDER_AGENCY=line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY")];
						if(ORDER_AGENCY !=null && ORDER_AGENCY.indexOf(",")>=0){
							ORDER_AGENCY=ORDER_AGENCY.replaceAll(",", " ");
						}
						long ORDER_AGENCY_ID=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_AGENCY_ID")]);
						long ORDER_LIFETIME_CLICKS=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_CLICKS")]);
						long ORDER_LIFETIME_IMPRESSIONS=
						  StringUtil.getLongValue(line[csvColumnMap.get("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS")]);
						String ORDER_PO_NUMBER=line[csvColumnMap.get("DimensionAttribute.ORDER_PO_NUMBER")];

						String orderStartDate=line[csvColumnMap.get("DimensionAttribute.ORDER_START_DATE_TIME")];
						String ORDER_START_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderStartDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");
						String ORDER_START_DATE=ORDER_START_DATE_TIME;

						String orderEndDate=line[csvColumnMap.get("DimensionAttribute.ORDER_END_DATE_TIME")];
						String ORDER_END_DATE_TIME="";
						String ORDER_END_DATE="";
						if(orderEndDate!=null && (!orderEndDate.equalsIgnoreCase("Unlimited"))){
							 ORDER_END_DATE_TIME=DateUtil.getFormatedDateUsingJodaLib(orderEndDate,"yyyy-MM-dd'T'HH:mm:ssZZ","yyyy-MM-dd HH:mm:ss");	
							 ORDER_END_DATE=ORDER_END_DATE_TIME;
						}					   
						String ORDER_TRAFFICKER=line[csvColumnMap.get("DimensionAttribute.ORDER_TRAFFICKER")];


						long AD_SERVER_IMPRESSIONS=
						   StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_IMPRESSIONS")]);	
						long AD_SERVER_CLICKS=StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_SERVER_CLICKS")]);
						double AD_SERVER_WITH_CPD_AVERAGE_ECPM=
						  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")]);
						double AD_SERVER_CTR=0;
						String ctr=line[csvColumnMap.get("Column.AD_SERVER_CTR")];
						if(LinMobileUtil.isNumeric(ctr)){
							AD_SERVER_CTR=(((double)Double.parseDouble(ctr)*100));
						}	
						AD_SERVER_CTR=Math.round((AD_SERVER_CTR*100.0))/100.0;
						double AD_SERVER_CPM_AND_CPC_REVENUE=
						  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE")]);
						double AD_SERVER_CPD_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_CPD_REVENUE")]);
						double AD_SERVER_ALL_REVENUE=ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_ALL_REVENUE")]);
						double AD_SERVER_DELIVERY_INDICATOR=0.0;
						String deliveryIndicator=line[csvColumnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR")];
						if(LinMobileUtil.isNumeric(deliveryIndicator)){		
							AD_SERVER_DELIVERY_INDICATOR=(((double)Double.parseDouble(deliveryIndicator)*100));
							AD_SERVER_DELIVERY_INDICATOR=Math.round((AD_SERVER_DELIVERY_INDICATOR*100.0))/100.0;;
						}
						double AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM=
						  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM")]);
						double AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
						String lineItemLvlImp=line[csvColumnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
						if(LinMobileUtil.isNumeric(lineItemLvlImp)){
							AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(lineItemLvlImp)*100));
						}
						double AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM=
						  ReportUtil.convertMoney(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM")]);
						long AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS=
						  StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")]);
						double AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=0;
						String adExchangeLineItemLvlCtr=line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR")];
						if(LinMobileUtil.isNumeric(adExchangeLineItemLvlCtr)){
							AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=(((double)Double.parseDouble(adExchangeLineItemLvlCtr)*100));
						}	
						AD_EXCHANGE_LINE_ITEM_LEVEL_CTR=Math.round((AD_EXCHANGE_LINE_ITEM_LEVEL_CTR*100.0))/100.0;
						long AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS=
						  StringUtil.getLongValue(line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")]);	
						double AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=0;
						String adExchangeLineItemLvlPercentImp=line[csvColumnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS")];
						if(LinMobileUtil.isNumeric(adExchangeLineItemLvlPercentImp)){
							AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS=(((double)Double.parseDouble(adExchangeLineItemLvlPercentImp)*100));
						}
						double AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE=
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

						rootObj.setDataSource(LinMobileConstants.DFP_DATA_SOURCE);
						rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
						rootObj.setPublisherName(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);

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
								//rootObj.setChannelName("Lin Media Direct");
								rootObj.setChannelName("Local Sales Direct");
								rootObj.setChannelType("Local Sales");
								rootObj.setSalesType("Direct");									
							}else{
								faultedData=true;
							}
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
public static String downloadReportFileFromDFP(CloudProjectDTO cloudProjectBQDTO, String schemaName, String startDate, String publisherIdInBQ, String endDate, String loadType, String downloadUrl) throws Exception{ 
	String month = DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	String bucketName = cloudProjectBQDTO.getCloudStorageBucket(); //DataLoaderUtil.getCloudStorageBucket(networkCode);
	String dirName = LinMobileConstants.REPORT_FOLDER+ "/advanced/"+ month+"/"+loadType;
	String fileName = schemaName+"_"+startDate.replace("-", "_")+"_"+endDate.replace("-", "_")+"_"+ new Date().getTime()+".gz";
	log.info("File name for gz report download at cloud is ["+fileName+"]");
	 GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	 GcsFilename filename = new GcsFilename(bucketName+"/"+dirName, fileName);
	 GcsFileOptions fileOptions = new GcsFileOptions.Builder().mimeType("application/x-gzip").acl("public-read").build();
	  
	    GcsOutputChannel outputChannel = gcsService.createOrReplace(filename, fileOptions);
	    BufferedOutputStream outStream = new BufferedOutputStream(Channels.newOutputStream(outputChannel));
	    
	    
	    URL url = new URL(downloadUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
           
 
            log.info("Content-Type = " + contentType);
            log.info("Content-Disposition = " + disposition);
            log.info("Content-Length = " + contentLength);
            log.info("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
            	outStream.write(buffer, 0, bytesRead);
            }
 
            outStream.close();
            outputChannel.close();
            inputStream.close();
 
            log.info("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
		String uploadedFileURL="gs://"+bucketName+"/"+dirName+"/"+fileName;
		    log.info("uploadedFileURL : "+uploadedFileURL);
		    return uploadedFileURL;
}
}
