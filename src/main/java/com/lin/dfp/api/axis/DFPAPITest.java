package com.lin.dfp.api.axis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201403.DateTimes;
import com.google.api.ads.dfp.axis.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.axis.v201403.AdUnit;
import com.google.api.ads.dfp.axis.v201403.AdUnitPage;
import com.google.api.ads.dfp.axis.v201403.AdUnitParent;
import com.google.api.ads.dfp.axis.v201403.AdUnitTargeting;
import com.google.api.ads.dfp.axis.v201403.Column;
import com.google.api.ads.dfp.axis.v201403.Company;
import com.google.api.ads.dfp.axis.v201403.CompanyPage;
import com.google.api.ads.dfp.axis.v201403.CompanyServiceInterface;
import com.google.api.ads.dfp.axis.v201403.CompanyType;
import com.google.api.ads.dfp.axis.v201403.ComputedStatus;
import com.google.api.ads.dfp.axis.v201403.CostType;
import com.google.api.ads.dfp.axis.v201403.Creative;
import com.google.api.ads.dfp.axis.v201403.CreativePage;
import com.google.api.ads.dfp.axis.v201403.CreativePlaceholder;
import com.google.api.ads.dfp.axis.v201403.CreativeRotationType;
import com.google.api.ads.dfp.axis.v201403.CreativeServiceInterface;
import com.google.api.ads.dfp.axis.v201403.Date;
import com.google.api.ads.dfp.axis.v201403.DateRangeType;
import com.google.api.ads.dfp.axis.v201403.Dimension;
import com.google.api.ads.dfp.axis.v201403.DimensionAttribute;
import com.google.api.ads.dfp.axis.v201403.DimensionFilter;
import com.google.api.ads.dfp.axis.v201403.ExportFormat;
import com.google.api.ads.dfp.axis.v201403.Forecast;
import com.google.api.ads.dfp.axis.v201403.ForecastServiceInterface;
import com.google.api.ads.dfp.axis.v201403.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201403.InventoryTargeting;
import com.google.api.ads.dfp.axis.v201403.LineItem;
import com.google.api.ads.dfp.axis.v201403.LineItemCreativeAssociation;
import com.google.api.ads.dfp.axis.v201403.LineItemCreativeAssociationPage;
import com.google.api.ads.dfp.axis.v201403.LineItemCreativeAssociationServiceInterface;
import com.google.api.ads.dfp.axis.v201403.LineItemPage;
import com.google.api.ads.dfp.axis.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.axis.v201403.LineItemType;
import com.google.api.ads.dfp.axis.v201403.MobilePlatform;
import com.google.api.ads.dfp.axis.v201403.NetworkServiceInterface;
import com.google.api.ads.dfp.axis.v201403.Order;
import com.google.api.ads.dfp.axis.v201403.OrderPage;
import com.google.api.ads.dfp.axis.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.axis.v201403.ReportJob;
import com.google.api.ads.dfp.axis.v201403.ReportJobStatus;
import com.google.api.ads.dfp.axis.v201403.ReportQuery;
import com.google.api.ads.dfp.axis.v201403.ReportQueryAdUnitView;
import com.google.api.ads.dfp.axis.v201403.ReportServiceInterface;
import com.google.api.ads.dfp.axis.v201403.RoadblockingType;
import com.google.api.ads.dfp.axis.v201403.Size;
import com.google.api.ads.dfp.axis.v201403.Statement;
import com.google.api.ads.dfp.axis.v201403.TargetPlatform;
import com.google.api.ads.dfp.axis.v201403.Targeting;
import com.google.api.ads.dfp.axis.v201403.UnitType;
import com.google.api.ads.dfp.axis.v201403.UpdateResult;
import com.google.api.ads.dfp.axis.v201403.User;
import com.google.api.ads.dfp.axis.v201403.UserPage;
import com.google.api.ads.dfp.axis.v201403.UserServiceInterface;
import com.google.api.ads.dfp.axis.v201403.Network;
import com.google.api.ads.dfp.axis.v201403.StartDateTimeType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPCampaignSetupService;
import com.lin.dfp.api.impl.DFPCompanyEnum;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.StringUtil;


public class DFPAPITest {
	
  static final Logger log = Logger.getLogger(DFPAPITest.class.getName());
  private static final String [] creativeSizeArray={"320x50", "300x50", "300x250", "728x90", "768x1024"};
 
  private static final String [] adUnitIdsArray={"58316262","54699942","57908742","54774462","54775422","54915822","55276902","57730782","54767742","54767862","54821502","55267902","54778302","54779262","54914622","55288662","58608702","51758982","51776142","51776502","58654902","54780222","54781182","54962502","55272822","58723902","54772422","54773502","54917622","55277862","51918942","51919902","56926302","57081222","51920862","51921822","58447182","51922782","51923742","57037662","51924702","51925662","56926662","57227862","51926622","51927582","57075462","54765222","54765342","54933942","55288422","51928542","51929502","56980422","58731942","51930462","51931542","56960622","57486942","58600302","54776382","54777342","54960582","55288542","54769662","54769782","54917982","55123782","51932502","51933462","57038502","32656902","54381582","54381702","54459942","54639942","51934422","51935382","56876022","58724862","51936582","51937542","57067062","54699462","51938502","51939462","56976102","57245262","51944742","51945702","57086262","51940902","51941862","57291462","51942822","51943782","56972622","57331422","54503622","54503862","54507822","54507942","54516702","54516822","54508542","54503742","54503982"};
  private static final int BUFFER_SIZE = 4* 1024 * 1024;
  
  private static final String DFP_AUTH_SCOPE="https://www.googleapis.com/auth/dfp";
  
  @SuppressWarnings("deprecation")
public static void main(String[] args) throws Exception {
	  String dfpApplicationName="Lin Digital";
	  String dfpNetworkCode="9331149";
	  /*GoogleCredential credential = new GoogleCredential.Builder()
      .setTransport(new NetHttpTransport())
      .setJsonFactory(new GsonFactory())
      .setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
      .setServiceAccountScopes(ImmutableList.of(DFP_AUTH_SCOPE))
      .setServiceAccountPrivateKeyFromP12File(new File("src/main/resources/env/keys/"+LinMobileVariables.SERVICE_ACCOUNT_KEY))
      .build();
  
	  credential.refreshToken();*/
  
	  log.info(" now going to build dfpSession ...");
	  // Construct a DfpSession.
	  /*DfpSession session = new DfpSession.Builder()
	      .withNetworkCode(dfpNetworkCode)
	      .withApplicationName(dfpApplicationName)
	      .withOAuth2Credential(credential)
	      .build();*/
	  DfpSession session = DFPAuthenticationUtil.getDFPSession(dfpNetworkCode,LinMobileConstants.DFP_APPLICATION_NAME);
		log.info(" getting DfpServices instance from properties...");
  
	  /*Credential oAuth2Credential = new OfflineCredentials.Builder()
		  .forApi(Api.DFP)
		  .fromFile()
		  .build()
		  .generateCredential();
	  DfpSession session = new DfpSession.Builder()
		  .fromFile()
		  .withOAuth2Credential(oAuth2Credential)
		  .build();*/

    System.out.println("network code:"+session.getNetworkCode());
    DfpServices dfpServices = new DfpServices();
    
    
    String downloadReportURL="";
    System.out.println("download report from dfp...");
    String start="2014-11-15";
    String end="2014-11-15";

    long orderId=218389949;
   // getLastUpdatedOrders(dfpServices, session);
   // getLineItemForOrder(dfpServices, session);
   	//getCreative(dfpServices, session);
   // //downloadReportURL=getDFPReportByAdUnitsWithCity(dfpServices, session, start, end);
    //getLastUpdatedOrders(dfpServices, session);
    //getLineItemForOrder(dfpServices, session);
    //downloadReportURL=getDFPReportByAdUnitsWithCity(dfpServices, session, start, end);
    //downloadReportURL=downloadReport(dfpServices, session);
    
    
    //downloadReportURL=getDFPLocationReport(dfpServices, session, start, end) ;  
    // getRecentlyUpdatedAdUnits(dfpServices, session);
   // approveOrder(dfpServices, session, orderId);
    //getLineItemsDelivering(dfpServices, session, orderId);
    //getAllLineItems(dfpServices, session, orderId);
   // getOrderStatus(dfpServices, session, orderId);
    //createUser(dfpServices, session, "gaurav.bhatia@mediaagility.com", "Gaurav Bhatia"); //117281789
    //getAllUsers(dfpServices, session);
    //checkAdUnit(dfpServices, session, "test",null); //"64174229"
    
   // fetchAdUnits(dfpServices, session);
  
    // createAdvertiser(dfpServices, session, "Advertiser :#test");
    //getLineItemNeedsCreative(dfpServices, session);
   
    //long lineItemId=34178622;
    //getLineItemCreativeAssosiation(dfpServices, session,lineItemId);
    
    
    
   // getForecastingData(dfpServices, session);
    
    //getSellThroughReport(dfpServices, session);
    
    //getAllAdvertisers(dfpServices, session);
  
   // getAllCompany(dfpServices, session);
    
   // getTopLevelAdUnits(dfpServices, session);
    
  
    //downloadReportURL=getDFPReportByAdUnitIds(dfpServices, session, start, end);
    //downloadReportURL=getDFPReportByAccounts(dfpServices, session, start, end);
    //downloadReportURL= getDFPTargetReport(dfpServices, session, start, end);
   // downloadReportURL= getRichMediaVideoCampaignReport(dfpServices, session, start, end);
    //downloadReportURL=getDFPLocationReport(dfpServices, session, start, end);
    
/*
    IDFPReportService reportService=new DFPReportService();
    List<String> fileDataList=reportService.readCSVGZFileAndSplit(downloadReportURL);
    System.out.println("total files:"+fileDataList.size());*/
   
    //String data=readCSVGZFile(downloadReportURL);
    
    NetworkServiceInterface networkService = dfpServices.get(session,NetworkServiceInterface.class);
    Network currentNetwork = networkService.getCurrentNetwork();
    
    LineItem lineItem = new LineItem();
    Targeting targeting = new Targeting();
	lineItem.setName("");
	lineItem.setOrderId(orderId);
	lineItem.setTargeting(targeting);
	lineItem.setLineItemType(LineItemType.SPONSORSHIP); //LineItemType.SPONSORSHIP OR STANDARD
	lineItem.setAllowOverbook(true);
	lineItem.setTargetPlatform(TargetPlatform.ANY);
    lineItem.setAllowOverbook(true);
	lineItem.setTargetPlatform(TargetPlatform.ANY);
	lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
	lineItem.setEndDateTime(DateTimes.toDateTime("03-29-2015", currentNetwork.getTimeZone()));
    
    System.out.println("downloadReportURL:"+downloadReportURL);
    
  }
  
  public static String readCSVGZFile(String url) throws IOException {		 
		   log.info("readCSVGZFile.....");
		   StringBuffer dataBuffer =new StringBuffer();
		   InputStream gzipStream = new GZIPInputStream((new URL(url)).openStream(),BUFFER_SIZE);
		   Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
		   BufferedReader buffered = new BufferedReader(decoder);
		   int i=0;
		   String line=buffered.readLine();
		   while(line !=null){
			   if(i>0){
				   dataBuffer.append('\n');
			   }			   
			   dataBuffer.append(line);
			   line=buffered.readLine();
			   i++;
		   }		
		   log.info("Total lines in buffer: "+i);
		   return dataBuffer.toString();		   
	}
  
  public static List<AdUnit> fetchAdUnits(DfpServices dfpServices, DfpSession session)
	      throws Exception {
		  log.info("fetchAdUnits...........");
	    List<AdUnit> adUnits = Lists.newArrayList();

	    // Get the InventoryService.
	    InventoryServiceInterface inventoryService =
	        dfpServices.get(session, InventoryServiceInterface.class);

	    // Create a statement to select all ad units.
	    StatementBuilder statementBuilder = new StatementBuilder()
	    		.where("status = 'ACTIVE' ")
	    		.offset(100)
	            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    

	   
	    // Default for total result set size.
	    int totalResultSetSize = 0;

	    do {
	      // Get ad units by statement.
	      AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        System.out.println("totalResultSetSize:"+totalResultSetSize);
	        int i = page.getStartIndex();
	        for (AdUnit adUnit : page.getResults()) {
	          System.out.printf(
	              "%s) Ad unit with ID \"%s\" and name \"%s\" was found.\n", i,
	              adUnit.getId(), adUnit.getName());
	          i++;
	        }
	        adUnits.addAll(Lists.<AdUnit>newArrayList(page.getResults()));
	      }

	     statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);

	    log.info("Number of results found: "+ totalResultSetSize);
	    return adUnits;
	  }
  
  public static void getLineItemNeedsCreative(DfpServices dfpServices, DfpSession session) throws Exception {
	    // Get the LineItemService.
	    LineItemServiceInterface lineItemService =
	        dfpServices.get(session, LineItemServiceInterface.class);

	    // Create a statement to only select line items that need creatives.
	    StatementBuilder statementBuilder = new StatementBuilder()
	        .where("status = :status")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("status", ComputedStatus.NEEDS_CREATIVES.toString());

	    // Default for total result set size.
	    int totalResultSetSize = 0;

	    do {
	      // Get line items by statement.
	      LineItemPage page =
	          lineItemService.getLineItemsByStatement(statementBuilder.toStatement());

	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        int i = page.getStartIndex();
	        for (LineItem lineItem : page.getResults()) {
	          System.out.printf(
	              "%d) Line item with ID \"%d\" and name \"%s\" was found.\n", i++,
	              lineItem.getId(), lineItem.getName());
	          
	        }
	      }

	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);

	    System.out.printf("Number of results found: %d\n", totalResultSetSize);
	  }

  
  public static void getAllCreatives(DfpServices dfpServices, DfpSession session) throws Exception {
	    // Get the CreativeService.
	    CreativeServiceInterface creativeService =
	        dfpServices.get(session, CreativeServiceInterface.class);

	    // Create a statement to get all creatives.
	    StatementBuilder statementBuilder = new StatementBuilder()
	        .where(" L")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

	    // Default for total result set size.
	    int totalResultSetSize = 0;

	    do {
	      // Get creatives by statement.
	      CreativePage page = creativeService.getCreativesByStatement(statementBuilder.toStatement());

	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        int i = page.getStartIndex();
	        for (Creative creative : page.getResults()) {
	          System.out.printf(
	              "%d) Creative with ID \"%d\" and name \"%s\" was found.\n", i++,
	              creative.getId(), creative.getName());
	        }
	      }

	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);

	    System.out.printf("Number of results found: %d\n", totalResultSetSize);
	  }
  
  public static void getLineItemCreativeAssosiation(DfpServices dfpServices, DfpSession session, long lineItemId)
	      throws Exception {
	  
	    // Get the LineItemCreativeAssociationService.
	    LineItemCreativeAssociationServiceInterface licaService =
		        dfpServices.get(session, LineItemCreativeAssociationServiceInterface.class);

	    // Create a statement to all LICAs for a line item.
	    StatementBuilder statementBuilder = new StatementBuilder()
	        .where("WHERE lineItemId = :lineItemId ")
	        .orderBy("lineItemId ASC, creativeId ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("lineItemId", lineItemId);

	    // Default for total result set size.
	    int totalResultSetSize = 0;

	    do {
	      // Get LICAs by statement.
	      LineItemCreativeAssociationPage page =
	          licaService.getLineItemCreativeAssociationsByStatement(statementBuilder.toStatement());

	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        int i = page.getStartIndex();
	        for (LineItemCreativeAssociation lica : page.getResults()) {
	          if (lica.getCreativeSetId() != null) {
	            System.out.printf(
	                "%d) LICA with line item ID \"%d\" and creative set ID \"%d\" was found.\n", i++,
	                lica.getLineItemId(), lica.getCreativeSetId());
	          } else {
	            System.out.printf(
	                "%d) LICA with line item ID \"%d\" and creative ID \"%d\" and destination URL \"%d\" was found.\n", i++,
	                lica.getLineItemId(), lica.getCreativeId(),lica.getDestinationUrl());
	            getCreativePreviewURL(dfpServices,session,lica.getCreativeId());
	          }
	          
	        }
	      }

	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);

	    System.out.printf("Number of results found: %d\n", totalResultSetSize);
	    
	  }

  public static String getCreativePreviewURL(DfpServices dfpServices, DfpSession session,long creativeId)
	      throws Exception {
		
	  	//log.info("getPreviewURL...........creativeId:"+creativeId);
               
		CreativeServiceInterface creativeService = dfpServices.get(session, CreativeServiceInterface.class);
		StatementBuilder statementBuilder = new StatementBuilder()
				        .where("WHERE id = :creativeId ")
				        .orderBy("id ASC")				       
				        .withBindVariableValue("creativeId", creativeId);
		String previewURL=null;
		CreativePage creativePage = creativeService.getCreativesByStatement(statementBuilder.toStatement());
		if(creativePage !=null && creativePage.getResults() !=null){
			for(Creative creative : creativePage.getResults()){
				previewURL = creative.getPreviewUrl();			    
			    System.out.println(" creative previewURL : " + previewURL);
			}
		}
	    
	    return previewURL;
	    
  }
  
  public static String getDFPReportByAdUnitIds(DfpServices dfpServices, DfpSession session,String start,String end) 
		  throws Exception{
		 log.info(" dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     com.google.api.ads.dfp.axis.v201403.Date startDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     com.google.api.ads.dfp.axis.v201403.Date endDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);

	     Dimension[] dimensionArray=new Dimension[] 
	    		    {
			Dimension.DATE, 
			Dimension.ADVERTISER_ID,
			Dimension.ADVERTISER_NAME,
			Dimension.AD_UNIT_ID,
			Dimension.AD_UNIT_NAME,
			Dimension.ORDER_ID,
			Dimension.ORDER_NAME,
			Dimension.LINE_ITEM_ID,
			Dimension.LINE_ITEM_NAME,
			Dimension.LINE_ITEM_TYPE,
			Dimension.CREATIVE_ID,
			Dimension.CREATIVE_NAME,
			Dimension.CREATIVE_SIZE,
			Dimension.CREATIVE_TYPE,
			Dimension.SALESPERSON_NAME
			};
			
			Column[] columnArray= new Column[] {
			Column.AD_SERVER_IMPRESSIONS,
			Column.AD_SERVER_CLICKS,
			Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
			Column.AD_SERVER_CTR,
			Column.AD_SERVER_CPM_AND_CPC_REVENUE,
			Column.AD_SERVER_CPD_REVENUE,
			Column.AD_SERVER_ALL_REVENUE,	    		
			Column.AD_SERVER_DELIVERY_INDICATOR,
			Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
			};
			
			
			DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
			DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
			DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
			DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
			DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
			DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
			DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
			DimensionAttribute.LINE_ITEM_START_DATE_TIME,
			DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
			DimensionAttribute.ORDER_AGENCY,
			DimensionAttribute.ORDER_AGENCY_ID,	
			DimensionAttribute.ORDER_LIFETIME_CLICKS,
			DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
			DimensionAttribute.ORDER_PO_NUMBER,
			DimensionAttribute.ORDER_START_DATE_TIME,
			DimensionAttribute.ORDER_END_DATE_TIME,
			DimensionAttribute.ORDER_TRAFFICKER	    		 
			};

	   	
				
			DimensionFilter[] dimFilterArray=new DimensionFilter[]{
					 DimensionFilter.ACTIVE_AD_UNITS
					 //DimensionFilter.MOBILE_INVENTORY_UNITS
			};
			reportQuery.setDimensionFilters(dimFilterArray);

	   	    Statement statement=new Statement();	    
		    StringBuffer query=new StringBuffer();
		     
	     
	      //query.append("  where ad_unit_ancestor_ad_unit_id in ( 32578902,32578902,32578062,32577222,54540462,54536382,54545982,32577102,54547302,54539022,32575062,58775502,32575182,32576022,32574822,32575662,54381822,32574462,32575542,54544662,54535062,32575902,32577582,54440262,32575302,32574702,32574942,32575782,32574582,32575422 )");
	      //query.append("  and creative_size in ('300 x 250', '300 x 50', '728 x 90', '768 x 1024')"); // API documentation says, can't use this as a filter
	      query.append("  where ad_unit_ancestor_ad_unit_id in (44760964,44736364,44754004,44724244,59115364,44729524,44722444,44741884,44771764,44733124,59345644,44766724,44778244,44750644,59115244)"); 
	   
	      log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);    
	   
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray); 
	     reportQuery.setDimensionAttributes(dimAttributeArray);
	   
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
  
  public static String getDFPLocationReport(DfpServices dfpServices, DfpSession session,String start,String end) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     //reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	    
	     com.google.api.ads.dfp.axis.v201403.Date startDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     com.google.api.ads.dfp.axis.v201403.Date endDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[]  {
				Dimension.DATE, 
				Dimension.ADVERTISER_ID,
				Dimension.ADVERTISER_NAME,
				/*Dimension.AD_UNIT_ID,
				Dimension.AD_UNIT_NAME,*/
				Dimension.ORDER_ID,
				Dimension.ORDER_NAME,
				Dimension.LINE_ITEM_ID,
				Dimension.LINE_ITEM_NAME,
				Dimension.LINE_ITEM_TYPE,
				Dimension.COUNTRY_CRITERIA_ID,
				Dimension.REGION_CRITERIA_ID,
				Dimension.CITY_CRITERIA_ID,
				Dimension.COUNTRY_NAME,
				Dimension.REGION_NAME,
				Dimension.CITY_NAME
				/*Dimension.CREATIVE_ID,
				Dimension.CREATIVE_NAME,
				Dimension.CREATIVE_SIZE,
				Dimension.CREATIVE_TYPE,
				Dimension.SALESPERSON_NAME*/
				};
				
				Column[] columnArray= new Column[] {
				Column.AD_SERVER_IMPRESSIONS,
				Column.AD_SERVER_CLICKS,
				Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
				Column.AD_SERVER_CTR,
				Column.AD_SERVER_CPM_AND_CPC_REVENUE,
				Column.AD_SERVER_CPD_REVENUE,
				Column.AD_SERVER_ALL_REVENUE,	    		
				//Column.AD_SERVER_DELIVERY_INDICATOR,
				Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM		    		
			};
				
				
			DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
				DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
				DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
				DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
				DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
				DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
				DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
				DimensionAttribute.LINE_ITEM_START_DATE_TIME,
				DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
				DimensionAttribute.ORDER_AGENCY,
				DimensionAttribute.ORDER_AGENCY_ID,
				DimensionAttribute.ORDER_LIFETIME_CLICKS,
				DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
				DimensionAttribute.ORDER_PO_NUMBER,
				DimensionAttribute.ORDER_START_DATE_TIME,
				DimensionAttribute.ORDER_END_DATE_TIME,
				DimensionAttribute.ORDER_TRAFFICKER	    		 
			 };
			 
			 DimensionFilter[] dimFilterArray=new DimensionFilter[]{
				  DimensionFilter.ACTIVE_ADVERTISERS	    		 
			  };
			//reportQuery.setDimensionFilters(dimFilterArray);
			
			Statement statement=new Statement();	    
		    StringBuffer query=new StringBuffer();
		   
		    //LinMobile accounts
		   // query.append("  WHERE ADVERTISER_ID IN ( 23548662,24659502,25870542,26561262,26902062,26931702,27484902,28013982,28020822,28876182,29080302,29087742,29362902,29363022,29444622,29549382,29552022,29579502,29579622,29627742,29681982,30177102,30728142,31250982,31572822,31572942,32483382,32590782,32590902,32829702,33206142,33503142,33503262,33503382,34000182,34000302,35545782,35703102,36098502,36110862,36470382,36470502,36638142,36811782,37747182,38299062,38648022,39611142,40346142,41246382,41418582,41580342,41580702,42039462,42784062,42784902,43043982,43437342,43592742,44056182,44058702,44800542,45060342,45337782,45495342,45512982,45524022,45953382,46585662,47319582,47595222,47600622,48008982,48381702,48384702,48462582,49058022,49670622,49916742,50038422,50038542,50038902,50477622,50629422,51143022,51150702,51416982,51533742,52090542,52090662,52285422,52530822,52641222,52757742,52856022,52856142,53299542,53378382,53390982,54366222,54503382,54503502,56534862,57378462 )");
		    
		    //LinDigital orders
		    query.append(" WHERE ORDER_ID IN ( 154464139,160368859,163313899,163405699,163411939,163412899,163415899,164313739,164316139,165164299,171161179,171197059,172597579,190149979,214471819,141903979,188654419,163403539,180298939,191719579,214327579,163572979,163572859,203735779,211557619,172722979,214309699,188654899,146131579,193442899,146122819,190281259,161902339,211555939,146229619,146116579,146378059,146228899,172136779,205549459,163558699,146230219,206448499,163573099,163548259,163572739,203735899,206450659,164092459,146132419,193442779,214483819,146122699,163550659,164092219,191724859,198265459,165414259,151355179,146387419,163558579,181134859,192091819,194748859,198264619,202782859,205183099,205350259,206257339,206576779,206603779,206605579,207553219,207971899,209249659,209341699,209865979,210998899,214482859,216197419,216714739,218568859,218759539,218776099,218779219,219032659,219184699,219444979,219689899,220678339,220678459,221733379,221767339,221967619,222630139,222641779,222646099,222648379,223251499,223290499,223463299,223626739,223709539,223727659,223727779,223875019,223889659,223891579,223892659,223893259,223895779,223895899,223896019,224113939,224114059,224117179,224126419,225073219,225078019,225083419,225094459,225097219,225613219,225616219,225616339,225616459,225658459,225813259,225974779,225975019,226018099,226018219,226114459,226141459,226150819,226162459,226166179,226179019,226179139,226253059,226328539,226328899,226329379,226386259,226390699,226397899,226413379,226447579,226453459,226569499,226572379,226679779,226844539,226844659,226986739,226989619,227006059,227007979,227008339,227010499,227023699,227023819,227024059,227024179,227024299,227024419,227025859,227027179,227027659,227167939,227168059,227169019,227169979,227170819,227172619,227193499,227221579,227396659,227428819,227429779,227430019,227438899,227541259,227563459,227575699,227707579,227978419,227996659,227997259,227997379,227997499,227997859,227998219,227998339,227999059,227999179,228001579,228001819,228004699,228005299,228005419,228009619,228009859,228010819,228107179,228132499,228141139,228153619,228156139 )");
		    
		    log.info("PQL Query : "+query.toString());
		    statement.setQuery(query.toString());		     
		    reportQuery.setStatement(statement);			
		    //reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
			
		    reportQuery.setDimensions(dimensionArray);
		    reportQuery.setColumns(columnArray);
		    reportQuery.setDimensionAttributes(dimAttributeArray);
	     
	        String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	        return downloadUrl;
}

  
  public static String getDFPReportByAccounts(DfpServices dfpServices, DfpSession session,String start,String end) 
		  throws Exception{
		 log.info(" dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     com.google.api.ads.dfp.axis.v201403.Date startDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     com.google.api.ads.dfp.axis.v201403.Date endDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);

	     Dimension[] dimensionArray=new Dimension[] 
	    		    {
			Dimension.DATE, 
			Dimension.ADVERTISER_ID,
			Dimension.ADVERTISER_NAME,
			Dimension.AD_UNIT_ID,
			Dimension.AD_UNIT_NAME,
			Dimension.ORDER_ID,
			Dimension.ORDER_NAME,
			Dimension.LINE_ITEM_ID,
			Dimension.LINE_ITEM_NAME,
			Dimension.LINE_ITEM_TYPE,
			Dimension.CREATIVE_ID,
			Dimension.CREATIVE_NAME,
			Dimension.CREATIVE_SIZE,
			Dimension.CREATIVE_TYPE,
			Dimension.SALESPERSON_NAME
			};
			
			Column[] columnArray= new Column[] {
			Column.AD_SERVER_IMPRESSIONS,
			Column.AD_SERVER_CLICKS,
			Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM,
			Column.AD_SERVER_CTR,
			Column.AD_SERVER_CPM_AND_CPC_REVENUE,
			Column.AD_SERVER_CPD_REVENUE,
			Column.AD_SERVER_ALL_REVENUE,	    		
			Column.AD_SERVER_DELIVERY_INDICATOR,
			Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS,
			Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE
			};
			
			
			DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
			DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
			DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY,
			DimensionAttribute.LINE_ITEM_COST_PER_UNIT,
			DimensionAttribute.LINE_ITEM_COST_TYPE,	    		 
			DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS,
			DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS,
			DimensionAttribute.LINE_ITEM_START_DATE_TIME,
			DimensionAttribute.LINE_ITEM_END_DATE_TIME,	    		
			DimensionAttribute.ORDER_AGENCY,
			DimensionAttribute.ORDER_AGENCY_ID,	
			DimensionAttribute.ORDER_LIFETIME_CLICKS,
			DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS,
			DimensionAttribute.ORDER_PO_NUMBER,
			DimensionAttribute.ORDER_START_DATE_TIME,
			DimensionAttribute.ORDER_END_DATE_TIME,
			DimensionAttribute.ORDER_TRAFFICKER	    		 
			};

	   	
				
			DimensionFilter[] dimFilterArray=new DimensionFilter[]{
					 DimensionFilter.ACTIVE_AD_UNITS
					 //DimensionFilter.MOBILE_INVENTORY_UNITS
			};
			reportQuery.setDimensionFilters(dimFilterArray);

	   	    Statement statement=new Statement();	    
		    StringBuffer query=new StringBuffer();
		     
	     
	      //query.append("  where ad_unit_ancestor_ad_unit_id in ( 32578902,32578902,32578062,32577222,54540462,54536382,54545982,32577102,54547302,54539022,32575062,58775502,32575182,32576022,32574822,32575662,54381822,32574462,32575542,54544662,54535062,32575902,32577582,54440262,32575302,32574702,32574942,32575782,32574582,32575422 )");
	      //query.append("  and creative_size in ('300 x 250', '300 x 50', '728 x 90', '768 x 1024')"); // API documentation says, can't use this as a filter
	    
		   //For Lin Mobile
		  //  query.append(" WHERE ADVERTISER_ID IN ( 24659502,25870542,26561262,26902062,26931702,27484902,28013982,28020822,28876182,29080302,29087742,29362902,29363022,29444622,29549382,29552022,29579502,29579622,29627742,29681982,30177102,30728142,31250982,31572822,31572942,32483382,32590902,32829702,33206142,33503142,33503262,33503382,34000182,34000302,35545782,35703102,36098502,36110862,36470382,36470502,36638142,36811782 )");
		    
		  //LinDigital accounts
		  query.append("  WHERE ADVERTISER_ID IN ( 24866659,24073939,17202019,25236379,26309299,20560219,16209139,25268899,26315299,22252699,26240059,26524699,18682099,16577779,17033059,25131499,16429099,16048579,15941899,25131019,16575379,17058859,16810819,23917819,16846819,23961499,16308499,25258699,18882979,16421179,23997379,16911739,26559619,24827179,26761699,16863379,16366699)");
		   
		    
	      log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);    
	   
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray); 
	     reportQuery.setDimensionAttributes(dimAttributeArray);
	   
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
  
  
  public static String getDFPTargetReport(DfpServices dfpServices, DfpSession session,String start,String end) 
		  throws Exception{
		 log.info(" dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	     com.google.api.ads.dfp.axis.v201403.Date startDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     com.google.api.ads.dfp.axis.v201403.Date endDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     Dimension[] dimensionArray=new Dimension[] {
				  Dimension.DATE, 
				  Dimension.ADVERTISER_ID,
				  Dimension.ADVERTISER_NAME,
				  Dimension.ORDER_ID,
				  Dimension.ORDER_NAME,
				  Dimension.LINE_ITEM_ID,
				  Dimension.LINE_ITEM_NAME,
				  Dimension.LINE_ITEM_TYPE,
				  Dimension.GENERIC_CRITERION_NAME
		  };
		     
		  Column[] columnArray= new Column[] {
			    		  Column.AD_SERVER_IMPRESSIONS,
			    		  Column.AD_SERVER_CLICKS,
			    		  Column.AD_SERVER_CTR,
			    		  Column.AD_SERVER_CPM_AND_CPC_REVENUE,	
			    		  Column.AD_SERVER_DELIVERY_INDICATOR,
			    		  Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM,	
			    		  Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS
		  }; 
				
		/*DimensionFilter[] dimFilterArray=new DimensionFilter[]{
					 DimensionFilter.ACTIVE_AD_UNITS
					 //DimensionFilter.MOBILE_INVENTORY_UNITS
		};*/
		//reportQuery.setDimensionFilters(dimFilterArray);

	   	    Statement statement=new Statement();	    
		    StringBuffer query=new StringBuffer();
		     
	      //For Lin Mobile
		  //  query.append(" WHERE ADVERTISER_ID IN ( 24659502,25870542,26561262,26902062,26931702,27484902,28013982,28020822,28876182,29080302,29087742,29362902,29363022,29444622,29549382,29552022,29579502,29579622,29627742,29681982,30177102,30728142,31250982,31572822,31572942,32483382,32590902,32829702,33206142,33503142,33503262,33503382,34000182,34000302,35545782,35703102,36098502,36110862,36470382,36470502,36638142,36811782 )");
		    
		  //LinDigital accounts
		  query.append("  WHERE ORDER_ID IN ( 226432349 )");
		   
		    
	      log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);    
	   
	    // reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray); 
	   	   
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
  
  public static String downloadReport(DfpServices dfpServices, DfpSession session,ReportQuery reportQuery) throws Exception{
		 String downloadUrl=null;
		 ReportServiceInterface reportService = dfpServices.get(session, ReportServiceInterface.class);
	     ReportJob reportJob = new ReportJob();
	     reportJob.setReportQuery(reportQuery);
	      
	      // Run report job.
	      reportJob = reportService.runReportJob(reportJob);

	      do {
	    	log.info("Report with ID '" + reportJob.getId() + "' is still running.");
	        Thread.sleep(30000);
	        // Get report job.
	        reportJob = reportService.getReportJob(reportJob.getId());
	      } while (reportJob.getReportJobStatus() == ReportJobStatus.IN_PROGRESS);

	      if (reportJob.getReportJobStatus() == ReportJobStatus.FAILED) {
	    	  log.info("Report job with ID '" + reportJob.getId()
	            + "' failed to finish successfully.");
	      }else{
	    	  log.info("Report job with ID '" + reportJob.getId()+ "' completed successfully."); 
	          Long reportJobId = reportJob.getId();
	          ExportFormat exportFormat = ExportFormat.CSV_DUMP;
	          downloadUrl = reportService.getReportDownloadURL(reportJobId, exportFormat);
	          log.info("downloadUrl: "+downloadUrl);
	      }
	      return downloadUrl;
	}
	
  
  public static String downloadReport(DfpServices dfpServices, DfpSession session) throws Exception{
		 String downloadUrl=null;
		 ReportServiceInterface reportService = dfpServices.get(session, ReportServiceInterface.class);
	    
	          ExportFormat exportFormat = ExportFormat.CSV_DUMP;
	          Long reportJobId=(long) 2003069622;
	          downloadUrl = reportService.getReportDownloadURL(reportJobId, exportFormat);
	          log.info("downloadUrl: "+downloadUrl);
	   
	      return downloadUrl;
	}
	
  
  public static void getForecastingData(DfpServices dfpServices, DfpSession session)
	      throws Exception {
		    // Get the ForecastService.
		    ForecastServiceInterface forecastService =
		        dfpServices.get(session, ForecastServiceInterface.class);

		    // Get the NetworkService.
		    NetworkServiceInterface networkService =
		        dfpServices.get(session, NetworkServiceInterface.class);

		    // Get the root ad unit ID used to target the whole site.
		    String rootAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();

		    //List<AdUnitTargeting> adUnitTargetList=loadAdUnitTargeting();
		    //inventoryTargeting.setTargetedAdUnits(adUnitTargetList.toArray(new AdUnitTargeting[adUnitTargetList.size()]));
		    
		   
		    
		    List<Forecast> resultList=new ArrayList<Forecast>();
		    
		    List<Size> sizeList=loadCreativeSizes();
		    for(Size size:sizeList){
		    	Forecast forecast=loadForecastDataBySize(forecastService, size);
		    	resultList.add(forecast);
		    	System.out.println("added forecast data in list:"+resultList.size());
		    }	
		   
		    
	}	  
  
    public static Forecast loadForecastDataBySize(ForecastServiceInterface forecastService,
    		Size size) throws Exception{    	    	
    	
    	// Create inventory targeting.
        InventoryTargeting inventoryTargeting = new InventoryTargeting();

        // Create ad unit targeting for the root ad unit.
        AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
        String adUnitId="32577222"; //"LIN.KASA";
	    adUnitTargeting.setAdUnitId(adUnitId);	 
        adUnitTargeting.setIncludeDescendants(true);

        inventoryTargeting.setTargetedAdUnits(new AdUnitTargeting[] {adUnitTargeting});

        // Create targeting.
        Targeting targeting = new Targeting();
        targeting.setInventoryTargeting(inventoryTargeting);

        // Create a line item.
        LineItem lineItem = new LineItem();
        lineItem.setTargeting(targeting);
        lineItem.setLineItemType(LineItemType.STANDARD);

        // Set the roadblocking type.
        lineItem.setRoadblockingType(RoadblockingType.ONE_OR_MORE);

        // Set the creative rotation type.
        lineItem.setCreativeRotationType(CreativeRotationType.OPTIMIZED);
        
        // Create the creative placeholder.
        CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
        creativePlaceholder.setSize(size);

        // Set the size of creatives that can be associated with this line item.
        lineItem.setCreativePlaceholders(new CreativePlaceholder[] {creativePlaceholder});

        DateTime startDate=new DateTime(2014,1,1,0,0);
	    DateTime endDate=new DateTime(2014,1,31,23,59);
	    
	   
	    // Set the length of the line item to run.
	    //lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
	    //lineItem.setEndDateTime(DateTimes.toDateTime(Instant.now().plus(Duration.standardDays(30L)), "America/New_York"));
	  	    
	    lineItem.setStartDateTime(DateTimes.toDateTime(startDate));
	    lineItem.setEndDateTime(DateTimes.toDateTime(endDate));
	   
        // Set the cost type.
        lineItem.setCostType(CostType.CPM);

        // Set the line item to use 50% of the impressions.
        lineItem.setUnitType(UnitType.IMPRESSIONS);
        lineItem.setUnitsBought(50L);

	    // Get forecast for line item.
	    Forecast forecast = forecastService.getForecast(lineItem);
	    
	    System.out.println("******************************************");
	    System.out.println("Creative size::"+size.getWidth()+" x "+size.getHeight());
    	long matched = forecast.getMatchedUnits();
	    double availablePercent = (forecast.getAvailableUnits() / (matched * 1.0)) * 100;
	    String unitType = forecast.getUnitType().toString().toLowerCase();

	    System.out.printf("%d %s matched.\n", matched, unitType);
	    System.out.printf("%.2f%% %s available.\n", availablePercent, unitType);

	    if (forecast.getPossibleUnits() != null) {
	      double possiblePercent = (forecast.getPossibleUnits() / (matched * 1.0)) * 100;
	      System.out.printf("%.2f%% %s possible.\n", possiblePercent, unitType);
	    }
	    
	    long reserved = forecast.getReservedUnits();
	    long delivered = forecast.getDeliveredUnits();
	    long available = forecast.getAvailableUnits();
	    long possible = forecast.getPossibleUnits();
	    System.out.println("reserved :"+reserved);
	    System.out.println("delivered :"+delivered);
	    System.out.println("available :"+available);	
	    System.out.println("possible :"+possible);
	    
    	return forecast;
    }
    
    public static List<Size> loadCreativeSizes(){
	  List<Size> sizeList=new ArrayList<Size>();		 
	  for(int i=0;i<creativeSizeArray.length;i++){
		  String [] dim=creativeSizeArray[i].split("x");
		  Size size=new Size();
		  size.setWidth(Integer.parseInt(dim[0]));
		  size.setHeight(Integer.parseInt(dim[1]));
		  size.setIsAspectRatio(false);
		  sizeList.add(size);
	  }
	  
	  return sizeList;
  }
  
    public static List<AdUnitTargeting> loadAdUnitTargeting(){
  	  List<AdUnitTargeting> adUnitTargetList=new ArrayList<AdUnitTargeting>();		 
  	  for(int i=0;i<adUnitIdsArray.length;i++){
	  		AdUnitTargeting adUnitTargeting = new AdUnitTargeting();		    
		    String adUnitId=adUnitIdsArray[i];
		    adUnitTargeting.setAdUnitId(adUnitId);		   
		    adUnitTargeting.setIncludeDescendants(true);	
  		    adUnitTargetList.add(adUnitTargeting);
  	  }  	  
  	  return adUnitTargetList;
    }
    
    public static String getSellThroughReport(DfpServices dfpServices, DfpSession session) throws Exception{
		 log.info("getSellThroughReport called...");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     
	     com.google.api.ads.dfp.axis.v201403.Date startDate=new com.google.api.ads.dfp.axis.v201403.Date();	     
	     startDate.setDay(1);
	     startDate.setMonth(1);
	     startDate.setYear(2014);
	     
	     com.google.api.ads.dfp.axis.v201403.Date endDate=new com.google.api.ads.dfp.axis.v201403.Date();
	     endDate.setDay(31);
	     endDate.setMonth(1);
	     endDate.setYear(2014);
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	    
	     Dimension[] dimensionArray=new Dimension[] {
				  Dimension.DATE,
				  Dimension.AD_REQUEST_AD_UNIT_SIZES
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.SELL_THROUGH_AVAILABLE_IMPRESSIONS,
		    		  Column.SELL_THROUGH_FORECASTED_IMPRESSIONS,
		    		  Column.SELL_THROUGH_RESERVED_IMPRESSIONS,
		    		  Column.SELL_THROUGH_SELL_THROUGH_RATE		    		 		    		
		 };
	  
	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };
	     
	     //reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray);	     
	     reportQuery.setDimensionFilters(dimFilterArray);	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
    
    public static void getAllAdvertisers(DfpServices dfpServices, DfpSession session) throws Exception {
        // Get the CompanyService.
        CompanyServiceInterface companyService =
            dfpServices.get(session, CompanyServiceInterface.class);

        // Create a statement to only select companies that are advertisers.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("type = :type")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("type", CompanyType.ADVERTISER.toString())
            .withBindVariableValue("lastModifiedDateTime",
            DateTimes.toDateTime(Instant.now().minus(Duration.standardDays(1L)),
                "America/New_York"));

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get companies by statement.
          CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (Company company : page.getResults()) {
              System.out.printf(
                  "%d) Company with ID \"%d\", name \"%s\", and type \"%s\" was found.\n", i++,
                  company.getId(), company.getName(), company.getType());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
    }
    
    public static void getAllCompany(DfpServices dfpServices, DfpSession session) throws Exception {
        // Get the CompanyService.
        CompanyServiceInterface companyService =
            dfpServices.get(session, CompanyServiceInterface.class);

        // Create a statement to get all companies.
        StatementBuilder statementBuilder = new StatementBuilder()
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get companies by statement.
          CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (Company company : page.getResults()) {
              System.out.printf(
                  "%d) Company: ID: \"%d\", name: \"%s\", type: \"%s\" \n", i++,
                  company.getId(), company.getName(), company.getType());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
      }
    
   
    public static void getTopLevelAdUnits(DfpServices dfpServices, DfpSession session) throws Exception {
        // Get the InventoryService.
        InventoryServiceInterface inventoryService =
            dfpServices.get(session, InventoryServiceInterface.class);

        // Get the NetworkService.
        NetworkServiceInterface networkService =
            dfpServices.get(session, NetworkServiceInterface.class);

        // Set the parent ad unit's ID for all children ad units to be fetched from.
        String parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
       
        System.out.println("parentAdUnitId:"+parentAdUnitId);
        //  parentAdUnitId= "58775502";  //lin.mobile
        //System.out.println("parentAdUnitId:"+parentAdUnitId+" : lin.mobile");
        
        // Create a statement to select ad units under the parent ad unit.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("parentId = :parentId ")  
            //.where("parentId = :parentId AND targetPlatform='MOBILE' ")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("parentId", parentAdUnitId);

        // Default for total result set size.
        int totalResultSetSize = 0;
       
        do {
          // Get ad units by statement.
          AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            System.out.println("totalResultSetSize");
            int i = page.getStartIndex();
            System.out.println("Name, TargetPlatform, MobilePlatform");
            for (AdUnit adUnit : page.getResults()) {
            	String name=adUnit.getName();
            	TargetPlatform targetPlatform=adUnit.getTargetPlatform();
            	String target=targetPlatform.toString();
            	MobilePlatform mobile=adUnit.getMobilePlatform();
            	String mobileStr=mobile.toString();
            	
            	System.out.println(adUnit.getId()+","+name+","+target+","+mobileStr);
            	/*System.out.printf(
                        "%d) Ad unit with ID 2 \"%s\" and name \"%s\" was found. targetPlatform: \"%s\" and mobilePlatform: \"%s\" \n", i++,
                        adUnit.getId(), adUnit.getName(),target,mobileStr);*/
            	            	 
             
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
      }
    
    public static void createAdvertiser(DfpServices dfpServices, DfpSession session, String advertiserName) throws Exception {
        // Get the CompanyService.
        CompanyServiceInterface companyService =
            dfpServices.get(session, CompanyServiceInterface.class);

        StatementBuilder statementBuilder = new StatementBuilder()
	        .where("type = :type AND name = :name")	       
	        .withBindVariableValue("name", advertiserName)
	        .withBindVariableValue("type", CompanyType.ADVERTISER.toString());

        boolean companyExist=false;
	    // Default for total result set size.
	    int totalResultSetSize = 0;
	
	    do {
	      // Get companies by statement.
	      CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());
	
	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        System.out.println("totalResultSetSize:"+totalResultSetSize);
	        companyExist=true;
	        int i = page.getStartIndex();
	        for (Company company : page.getResults()) {
	          System.out.printf(
	              "%d) Company with ID \"%d\", name \"%s\", and type \"%s\" was found.\n", i++,
	              company.getId(), company.getName(), company.getType());
	        }
	      }else{
	    	  System.out.println("There is no advertiser....");
	      }
	
	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);
    
	    DFPCompanyEnum companyEnum=DFPCompanyEnum.ADVERTISER;
	    
	    System.out.println("CompanyType.ADVERTISER:"+CompanyType.ADVERTISER);
	    System.out.println("and dfpcompanyEnum:"+companyEnum);
	    
	    if(!companyExist){
	    	// Create an advertiser.
	        Company advertiser = new Company();
	        advertiser.setName(advertiserName);
	        advertiser.setType(CompanyType.ADVERTISER);
            Company [] companies=new Company []{
            		advertiser
            };
	        
	        // Create the companies on the server.
	        Company [] createdCompanyArray = companyService.createCompanies(companies);
            for(Company createdCompany:createdCompanyArray){
            	 System.out.printf("A company with ID \"%d\", name \"%s\", and type \"%s\" was created.\n",
       	              createdCompany.getId(), createdCompany.getName(), createdCompany.getType());
            }
	       
	    }
        
       
      }
    
 
    public static void createUser(DfpServices dfpServices, DfpSession session, String emailAddress,
    	      String name) throws Exception {
    		
    	    // Get the UserService.
    	    UserServiceInterface userService = dfpServices.get(session, UserServiceInterface.class);

    	    // Create a user.
    	    User traffickerUser = new User();
    	    traffickerUser.setEmail(emailAddress);
    	    traffickerUser.setName(name);
    	    traffickerUser.setPreferredLocale("en_US");

    	    // Set the system defined ID of the trafficker role.
    	    // To determine what other roles exist, run GetAllRoles.java.
    	    traffickerUser.setRoleId(-7L);

    	    // Create the user on the server.
    	    User[] users = userService.createUsers(new User[] {traffickerUser});

    	    for (User createdUser : users) {
    	      System.out.printf("A user with ID \"%d\" and name \"%s\" was created.\n",
    	          createdUser.getId(), createdUser.getName());
    	    }
   }
    
    public static void getAllUsers(DfpServices dfpServices, DfpSession session) throws Exception {
        // Get the UserService.
        UserServiceInterface userService = dfpServices.get(session, UserServiceInterface.class);

        // Create a statement to get all users.
        StatementBuilder statementBuilder = new StatementBuilder()
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get users by statement.
          UserPage page = userService.getUsersByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (User user : page.getResults()) {
              System.out.printf("%d) User with ID \"%d\" and name \"%s\" was found.\n", i++,
                  user.getId(), user.getName());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
    }
    
    public static void checkAdUnit(DfpServices dfpServices, DfpSession session,String adUnitName,String parentAdUnitId) 
    		throws Exception {
	
		
			InventoryServiceInterface inventoryService =
		            dfpServices.get(session, InventoryServiceInterface.class);
			
			StatementBuilder statementBuilder=null;
			
			if(parentAdUnitId ==null){
				NetworkServiceInterface networkService =
				            dfpServices.get(session, NetworkServiceInterface.class);
				 parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
				 System.out.println("parentAdUnitId:"+parentAdUnitId);
				 
				 statementBuilder=new StatementBuilder();
			}else{
				statementBuilder=new StatementBuilder()
			        .where("name = :name AND parentId = :parentId")	       
			        .withBindVariableValue("name", adUnitName)
			        .withBindVariableValue("parentId", parentAdUnitId);
			}
			/*statementBuilder=new StatementBuilder()
		        .where("name = :name AND parentId = :parentId")	       
		        .withBindVariableValue("name", adUnitName)
		        .withBindVariableValue("parentId", parentAdUnitId);*/
			
			
			int totalResultSetSize = 0;			
			do {
		          // Get ad units by statement.
		          AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

		          if (page.getResults() != null) {
		            totalResultSetSize = page.getTotalResultSetSize();
		            int i = page.getStartIndex();
		            System.out.println("Name, TargetPlatform, MobilePlatform");
		            for (AdUnit adUnit : page.getResults()) {
		            	String name=adUnit.getName();
		            	TargetPlatform targetPlatform=adUnit.getTargetPlatform();
		            	String target=targetPlatform.toString();
		            	MobilePlatform mobile=adUnit.getMobilePlatform();
		            	String mobileStr=mobile.toString();
		            	
		            	System.out.println(adUnit.getId()+","+name+","+target+","+mobileStr);
		            	/*System.out.printf(
		                        "%d) Ad unit with ID 2 \"%s\" and name \"%s\" was found. targetPlatform: \"%s\" and mobilePlatform: \"%s\" \n", i++,
		                        adUnit.getId(), adUnit.getName(),target,mobileStr);*/
		            	            	 
		             
		            }
		          }

		          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		        } while (statementBuilder.getOffset() < totalResultSetSize);
		
		
	}
    
    public static  List<AdUnit> getRecentlyUpdatedAdUnits(DfpServices dfpServices, DfpSession session) 
  		  throws Exception{
                String adServerId=session.getNetworkCode();
    		
    		    List<AdUnit> adUnits = Lists.newArrayList();
                List<AdUnitHierarchy> adUnitHierarchyList=new ArrayList<AdUnitHierarchy>();
    		    // Get the InventoryService.
    		    InventoryServiceInterface inventoryService =
    		        dfpServices.get(session, InventoryServiceInterface.class);

    		    // Create a statement to only fetch ad units updated or created since
    		    // yesterday.

    		    StatementBuilder statementBuilder = new StatementBuilder()
	    	        .where("lastModifiedDateTime >= :lastModifiedDateTime")
	    	        .orderBy("id ASC")
	    	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	    	        .withBindVariableValue("lastModifiedDateTime",
	    	            DateTimes.toDateTime(Instant.now().minus(Duration.standardDays(1L)),
	    	                "America/New_York"));
    		    
    		    // Default for total result set size.
    		    int totalResultSetSize = 0;

    		    do {
    		      // Get ad units by statement.
    		      AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

    		      if (page.getResults() != null) {
    		        totalResultSetSize = page.getTotalResultSetSize();
    		        System.out.println("totalResultSetSize:"+totalResultSetSize);
    		        int i = page.getStartIndex();
    		        for (AdUnit adUnit : page.getResults()) {
    		          System.out.printf(
    		              "%s) Ad unit with ID \"%s\" and name \"%s\" was found.\n", i,
    		              adUnit.getId(), adUnit.getName());
    		          //System.out.println("AdUnit Parent Path:"+adUnit.getParentPath().length+" and active status:"+adUnit.getStatus().getValue());
    		          AdUnitParent [] adUnitparents=adUnit.getParentPath();
    		          if(adUnitparents !=null){
    		        	  System.out.println("adUnitparents length :"+adUnitparents.length);
    		        	  String adUnitId="";
	   					  String adUnitName="";
	   					  String canonicalPath="";
	   					  String parentId="";
	   					  AdUnitHierarchy adUnitObj=null;
	   					  
	   					  if(adUnitparents.length==5){
	   						String adUnit1=adUnitparents[1].getName();	   						
	   						String adUnit2=adUnitparents[2].getName();
	   						String adUnit3=adUnitparents[3].getName();
	   						String adUnit4=adUnitparents[4].getName();
	   						//String adUnitId4=adUnitparents[4].getId();
	   						String adUnit5=adUnit.getName();
	   						String adUnitId5=adUnit.getId();
	   						
	   						adUnitId=adUnitId5;
  		 					parentId=adUnit.getParentId();
  		 					adUnitName=adUnit5;
  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4+" > "+adUnit5;
  		 					String id=adServerId+"_"+adUnitId;
  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
	   						
	   					  }else if(adUnitparents.length==4){
	   						String adUnit1=adUnitparents[1].getName();	   						
	   						String adUnit2=adUnitparents[2].getName();
	   						String adUnit3=adUnitparents[3].getName();
	   						String adUnit4=adUnit.getName();
	   						String adUnitId4=adUnit.getId();
	   						
	   						adUnitId=adUnitId4;
  		 					parentId=adUnit.getParentId();
  		 					adUnitName=adUnit4;
  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3+" > "+adUnit4;
  		 					String id=adServerId+"_"+adUnitId;
  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
	   					  }else if(adUnitparents.length==3){
	   						String adUnit1=adUnitparents[1].getName();	   						
	   						String adUnit2=adUnitparents[2].getName();
	   						String adUnit3=adUnit.getName();
	   						String adUnitId3=adUnit.getId();
	   						
	   						adUnitId=adUnitId3;
  		 					parentId=adUnit.getParentId();
  		 					adUnitName=adUnit3;
  		 					canonicalPath=adUnit1+" > "+adUnit2+" > "+adUnit3;
  		 					String id=adServerId+"_"+adUnitId;
  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath); 
	   					  }else if(adUnitparents.length==2){
	   						String adUnit1=adUnitparents[1].getName();	
	   						String adUnit2=adUnit.getName();
	   						String adUnitId2=adUnit.getId();
	   						
	   						adUnitId=adUnitId2;
  		 					parentId=adUnit.getParentId();
  		 					adUnitName=adUnit2;
  		 					canonicalPath=adUnit1+" > "+adUnit2;
  		 					String id=adServerId+"_"+adUnitId;
  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
	   					  }else if(adUnitparents.length==1){
	   						
	   						String adUnit1=adUnit.getName();
	   						String adUnitId1=adUnit.getId();	   						
	   						adUnitId=adUnitId1;
  		 					parentId=adUnit.getParentId();
  		 					adUnitName=adUnit1;
  		 					canonicalPath=adUnit1;
  		 					String id=adServerId+"_"+adUnitId;
  		 					adUnitObj=new AdUnitHierarchy(id, adServerId, adUnitId, adUnitName, parentId, canonicalPath);
	   					  }else{
	   						  System.out.println("=========================================Unhandled adUnit=======================");
	   					  }
	   					
	   					  if(adUnitObj !=null){
	   						  System.out.println("added : "+adUnitObj.toString());
	   						  adUnitHierarchyList.add(adUnitObj);
	   					  }
    		        	 /* for(AdUnitParent adUnitparent:adUnitparents){
        		        	  System.out.println("Parent : "+adUnitparent.getName());
        		          }*/
    		          }
    		        
    		          i++;
    		        }
    		        adUnits.addAll(Lists.<AdUnit>newArrayList(page.getResults()));
    		      }

    		     statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
    		    } while (statementBuilder.getOffset() < totalResultSetSize);

    		    log.info("Number of results found: "+ totalResultSetSize);
    		    System.out.println("===================total adUnits objects====="+adUnitHierarchyList.size());
    		    return adUnits;
  	}
    
    public static void approveOrder(DfpServices dfpServices, DfpSession session, long orderId)
    	      throws Exception {
    	    // Get the OrderService.
    	    OrderServiceInterface orderService =
    	        dfpServices.get(session, OrderServiceInterface.class);

    	    // Create a statement to select an order.
    	    StatementBuilder statementBuilder = new StatementBuilder()
    	        .where("WHERE id = :id")
    	        .orderBy("id ASC")
    	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
    	        .withBindVariableValue("id", orderId);

    	    // Default for total result set size.
    	    int totalResultSetSize = 0;

    	    do {
    	      // Get orders by statement.
    	      OrderPage page = orderService.getOrdersByStatement(statementBuilder.toStatement());

    	      if (page.getResults() != null) {
    	        totalResultSetSize = page.getTotalResultSetSize();
    	        int i = page.getStartIndex();
    	        for (Order order : page.getResults()) {
    	          System.out.printf("%d) Order with ID \"%d\" will be approved.\n", i++, order.getId());
    	        }
    	      }

    	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
    	    } while (statementBuilder.getOffset() < totalResultSetSize);

    	    System.out.printf("Number of orders to be approved: %d\n", totalResultSetSize);

    	    if (totalResultSetSize > 0) {
    	      // Remove limit and offset from statement.
    	      statementBuilder.removeLimitAndOffset();

    	      // Create action.
    	      com.google.api.ads.dfp.axis.v201403.ApproveOrders action =
    	          new com.google.api.ads.dfp.axis.v201403.ApproveOrders();
    	      action.setSkipInventoryCheck(true);
    	      
    	      com.google.api.ads.dfp.axis.v201403.ApproveAndOverbookOrders overbookAction=
    	    		  new com.google.api.ads.dfp.axis.v201403.ApproveAndOverbookOrders();
    	      
    	      // Perform action.
    	      UpdateResult result =
    	          orderService.performOrderAction(overbookAction, statementBuilder.toStatement());

    	      if (result != null && result.getNumChanges() > 0) {
    	        System.out.printf("Number of orders approved: %d\n", result.getNumChanges());
    	      } else {
    	        System.out.println("No orders were approved.");
    	      }
    	    }
    }
    
    public static void getLineItemsDelivering(DfpServices dfpServices, DfpSession session,long orderId) throws Exception {
        // Get the LineItemService.
        LineItemServiceInterface lineItemService =
            dfpServices.get(session, LineItemServiceInterface.class);

        // Create a statement to only select line items that need creatives.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("status = :status AND orderId = :orderId")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("status", ComputedStatus.NEEDS_CREATIVES.toString())
            .withBindVariableValue("orderId", orderId);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get line items by statement.
          LineItemPage page =
              lineItemService.getLineItemsByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (LineItem lineItem : page.getResults()) {
              System.out.printf(
                  "%d) Line item with ID \"%d\" and name \"%s\" was found.\n", i++,
                  lineItem.getId(), lineItem.getName());
              System.out.println("status:"+lineItem.getStatus().getValue());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
   }
    
    public static void getOrderStatus(DfpServices dfpServices, DfpSession session,long orderId) throws Exception {
        // Get the OrderService.
        OrderServiceInterface orderService =
            dfpServices.get(session, OrderServiceInterface.class);

        // Create a statement to select all orders.
        StatementBuilder statementBuilder = new StatementBuilder()
             .where("id = :id")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("id", orderId);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get orders by statement.
          OrderPage page =
              orderService.getOrdersByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (Order order : page.getResults()) {
              System.out.printf(
                  "%d) Order with ID \"%d\" and name \"%s\" was found.\n", i++,
                  order.getId(), order.getName());
              System.out.println("Status --"+order.getStatus().getValue());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
    }
    
    
    public static void getAllLineItems(DfpServices dfpServices, DfpSession session,long orderId) throws Exception {
        // Get the LineItemService.
        LineItemServiceInterface lineItemService =
            dfpServices.get(session, LineItemServiceInterface.class);

        // Create a statement to only select line items that need creatives.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("orderId = :orderId")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)           
            .withBindVariableValue("orderId", orderId);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get line items by statement.
          LineItemPage page =
              lineItemService.getLineItemsByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
            totalResultSetSize = page.getTotalResultSetSize();
            int i = page.getStartIndex();
            for (LineItem lineItem : page.getResults()) {
              System.out.printf(
                  "%d) Line item with ID \"%d\" and name \"%s\" was found.\n", i++,
                  lineItem.getId(), lineItem.getName());
              System.out.println("status:"+lineItem.getStatus().getValue());
            }
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

        System.out.printf("Number of results found: %d\n", totalResultSetSize);
   }
    
    
	/*
	 * Load adUnit data for USA only
	 */
	public static String getDFPReportByAdUnitsWithCity(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception{		
		 
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	    
	     if(start !=null && end !=null){
	    	 reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    	 String [] startArray=start.split("-");
			 String [] endArray=end.split("-"); 
			 com.google.api.ads.dfp.axis.v201403.Date startDate=new  com.google.api.ads.dfp.axis.v201403.Date();
		     
		     startDate.setDay(Integer.parseInt(startArray[2]));
		     startDate.setMonth(Integer.parseInt(startArray[1]));
		     startDate.setYear(Integer.parseInt(startArray[0]));
		     
		     com.google.api.ads.dfp.axis.v201403.Date endDate=new  com.google.api.ads.dfp.axis.v201403.Date();
		     endDate.setDay(Integer.parseInt(endArray[2]));
		     endDate.setMonth(Integer.parseInt(endArray[1]));
		     endDate.setYear(Integer.parseInt(endArray[0]));
		     
		     reportQuery.setStartDate(startDate);
		     reportQuery.setEndDate(endDate);	   
	     }else{
	    	 reportQuery.setDateRangeType(DateRangeType.LAST_MONTH);
	     }
	    	     
	    	    
	     Dimension[] dimensionArray=new Dimension[] 
	                          	    		    {				  
				  Dimension.AD_UNIT_ID,
				  Dimension.AD_UNIT_NAME,				
				  Dimension.COUNTRY_CRITERIA_ID,
				  Dimension.REGION_CRITERIA_ID,
				  Dimension.CITY_CRITERIA_ID,
				  Dimension.COUNTRY_NAME,
				  Dimension.REGION_NAME,
				  Dimension.CITY_NAME
				 
		 };
	     
	     Column[] columnArray= new Column[] {
		    		  Column.AD_SERVER_IMPRESSIONS,
		    		  Column.AD_SERVER_CLICKS		    		    		
		 };
	     
	     
	     DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{	    			 
	     };	   
	     DimensionFilter[] dimFilterArray=new DimensionFilter[]{
	    		 DimensionFilter.ACTIVE_AD_UNITS,	    		 
	     };
	     
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     query.append(" where AD_UNIT_ANCESTOR_AD_UNIT_ID in ( 56259342 ");
	     //query.append(" where AD_UNIT_ANCESTOR_AD_UNIT_ID in ( 32575062, 56259342");  //56259342 -- level -3 adunitid
	     
	    /* for(int i=0;i<adUnitIds.size();i++){
	    	 if(i==0){
	    		 query.append(adUnitIds.get(i));
	    	 }else{
	    		 query.append(",");
	    		 query.append(adUnitIds.get(i));
	    	 }
	     }*/
	     query.append(" ) and country_criteria_id in (2840) ");  //Load only for USA right now
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray);	
	     reportQuery.setDimensionFilters(dimFilterArray);		     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	public static String getRichMediaVideoCampaignReport(DfpServices dfpServices, DfpSession session,
			String start,String end) throws Exception{
		 log.info("dates..start:"+start+" and end:"+end);
		 String [] startArray=start.split("-");
		 String [] endArray=end.split("-");
		
	     // Create report query.
	     ReportQuery reportQuery = new ReportQuery();
	     reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
	    
	     Date startDate=new Date();
	     
	     startDate.setDay(Integer.parseInt(startArray[2]));
	     startDate.setMonth(Integer.parseInt(startArray[1]));
	     startDate.setYear(Integer.parseInt(startArray[0]));
	     
	     Date endDate=new Date();
	     endDate.setDay(Integer.parseInt(endArray[2]));
	     endDate.setMonth(Integer.parseInt(endArray[1]));
	     endDate.setYear(Integer.parseInt(endArray[0]));
	     
	     reportQuery.setStartDate(startDate);
	     reportQuery.setEndDate(endDate);
	     
	     Dimension[] dimensionArray=new Dimension[] {
    		 Dimension.DATE, 
    		 Dimension.ADVERTISER_ID,
    		 Dimension.ADVERTISER_NAME,
    		 Dimension.ORDER_ID,
    		 Dimension.ORDER_NAME,
    		 Dimension.LINE_ITEM_ID,
    		 Dimension.LINE_ITEM_NAME,
    		 Dimension.LINE_ITEM_TYPE,
    		 Dimension.CREATIVE_ID,
    		 Dimension.CREATIVE_NAME,
    		 Dimension.CREATIVE_SIZE,
    		 Dimension.CREATIVE_TYPE
		 };
			
		 Column[] columnArray= new Column[] {
			 /*Column.AD_SERVER_CLICKS,*/
			
			 Column.AD_SERVER_IMPRESSIONS,
				 
			 /*Rich Media viewership*/
			 Column.RICH_MEDIA_BACKUP_IMAGES,				//
			 Column.RICH_MEDIA_DISPLAY_TIME,				//
			 Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME,		//
			 
			 /* Rich Media interaction */
			 Column.RICH_MEDIA_EXPANSIONS,
			 Column.RICH_MEDIA_EXPANDING_TIME,
			 Column.RICH_MEDIA_INTERACTION_TIME,
			 Column.RICH_MEDIA_INTERACTION_COUNT,
			 Column.RICH_MEDIA_INTERACTION_RATE,			//
			 Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME,
			 Column.RICH_MEDIA_INTERACTION_IMPRESSIONS,
			 Column.RICH_MEDIA_MANUAL_CLOSES,
			 Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS,
			 
			 /* Rich Media video metrics */
			 Column.RICH_MEDIA_VIDEO_INTERACTIONS,
			 Column.RICH_MEDIA_VIDEO_INTERACTION_RATE,
			 Column.RICH_MEDIA_VIDEO_MUTES,
			 Column.RICH_MEDIA_VIDEO_PAUSES,
			 Column.RICH_MEDIA_VIDEO_PLAYES,
			 Column.RICH_MEDIA_VIDEO_MIDPOINTS,
			 Column.RICH_MEDIA_VIDEO_COMPLETES,
			 Column.RICH_MEDIA_VIDEO_REPLAYS,
			 Column.RICH_MEDIA_VIDEO_STOPS,
			 Column.RICH_MEDIA_VIDEO_UNMUTES,
			 Column.RICH_MEDIA_VIDEO_VIEW_RATE,
			 Column.RICH_MEDIA_VIDEO_VIEW_TIME,				//
			 
			 /* Video viewership */
			 Column.VIDEO_INTERACTION_START,
			 Column.VIDEO_INTERACTION_FIRST_QUARTILE,
			 Column.VIDEO_INTERACTION_MIDPOINT,
			 Column.VIDEO_INTERACTION_THIRD_QUARTILE,
			 Column.VIDEO_INTERACTION_COMPLETE,
			 Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE,
			 Column.VIDEO_INTERACTION_COMPLETION_RATE,
			 Column.VIDEO_INTERACTION_ERROR_COUNT,
			 Column.VIDEO_INTERACTION_VIDEO_LENGTH,
			 Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN,
			 Column.VIDEO_INTERACTION_ENGAGED_VIEW,
			 Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE,
			 
			 /* Video interaction */
			 Column.VIDEO_INTERACTION_PAUSE,
			 Column.VIDEO_INTERACTION_RESUME,
			 Column.VIDEO_INTERACTION_REWIND,
			 Column.VIDEO_INTERACTION_MUTE,
			 Column.VIDEO_INTERACTION_UNMUTE,
			 Column.VIDEO_INTERACTION_COLLAPSE,
			 Column.VIDEO_INTERACTION_EXPAND,
			 Column.VIDEO_INTERACTION_FULL_SCREEN,
			 Column.VIDEO_INTERACTION_VIDEO_SKIPS,
		     Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE,
		     Column.VIDEO_INTERACTION_VIEW_RATE,
		 };
		
		
		 DimensionAttribute [] dimAttributeArray=new DimensionAttribute []{
			 DimensionAttribute.ORDER_START_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_START_DATE_TIME,
	   		 DimensionAttribute.ORDER_END_DATE_TIME,
	   		 DimensionAttribute.LINE_ITEM_END_DATE_TIME,
	   		 DimensionAttribute.ORDER_AGENCY,
	   		 DimensionAttribute.ORDER_AGENCY_ID,
	   		 DimensionAttribute.LINE_ITEM_GOAL_QUANTITY,
	   		 DimensionAttribute.LINE_ITEM_COST_PER_UNIT	    		 
		 };
		 
		 /*DimensionFilter[] dimFilterArray=new DimensionFilter[]{	    		 
	    		 DimensionFilter.MOBILE_LINE_ITEMS
	     };*/
		 
	     Statement statement=new Statement();	    
	     StringBuffer query=new StringBuffer();
	     query.append(" WHERE ADVERTISER_ID IN ( ");
	     query.append("24659502,25870542,26561262,26902062,26931702,27484902,28013982,28020822,28876182,29080302,29087742,29362902,29363022,29444622,29549382,29552022,29579502,29579622,29627742,29681982,30177102,30728142,31250982,31572822,31572942,32483382,32590782,32590902,32829702,33206142,33503142,33503262,33503382,34000182,34000302,35545782,35703102,36098502,36110862,36470382,36470502,36638142,36811782,37747182,38299062,38648022,39611142,40346142,41246382,41418582,41580342,41580702,42039462,42784062,42784902,43043982,43437342,43592742,44056182,44058702,44800542,45060342,45337782,45495342,45512982,45524022,45953382,46585662,47319582,47595222,47600622,48008982,48381702,48384702,48462582,49058022,49670622,49916742,50038422,50038542,50038902,50477622,50629422,51143022,51150702,51416982,51533742,52090542,52090662,52285422,52530822,52641222,52757742,52856022,52856142,53299542,53378382,53390982,54006342,54006462,54366222,54503382,54503502,55187862,55451262,56067462,56214702,56363382,56534862,57378462,57803262");
	     query.append(" )");
	     log.info("PQL Query : "+query.toString());
	     statement.setQuery(query.toString());		     
	     reportQuery.setStatement(statement);
	     
        
	     reportQuery.setAdUnitView(ReportQueryAdUnitView.HIERARCHICAL);
	     
	     reportQuery.setDimensions(dimensionArray);
	     reportQuery.setColumns(columnArray);
	     reportQuery.setDimensionAttributes(dimAttributeArray);
	     
	     
	     String downloadUrl=downloadReport(dfpServices, session, reportQuery);
	     return downloadUrl;
	}
	
	 public static void getLastUpdatedOrders(DfpServices dfpServices, DfpSession session) throws Exception {
		    // Get the LineItemService.
		    OrderServiceInterface orderService =
		        dfpServices.get(session, OrderServiceInterface.class);

		    // Create a statement to only select line items updated or created since
		    // yesterday.
		    StatementBuilder statementBuilder = new StatementBuilder()
		        .where("lastModifiedDateTime >= :lastModifiedDateTime")
		        .orderBy("id ASC")
		        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
		        .withBindVariableValue("lastModifiedDateTime",
		            DateTimes.toDateTime(Instant.now().minus(Duration.standardHours(24L)),
		                "America/New_York"));

		    // Default for total result set size.
		    int totalResultSetSize = 0;

		    do {
		      // Get line items by statement.
		      OrderPage page =
		    		  orderService.getOrdersByStatement(statementBuilder.toStatement());

		      if (page.getResults() != null) {
		        totalResultSetSize = page.getTotalResultSetSize();
		        int i = page.getStartIndex();
		        for (Order order : page.getResults()) {
		          System.out.printf(
		              "%d) Order with ID \"%d\" and name \"%s\" was found.\n", i++,
		              order.getId(), order.getName());
		          System.out.println("Start Date : "+order.getStartDateTime().getDate().getDay()+" End Date : "+order.getEndDateTime()+" orderstatus: "+order.getAdvertiserId());
		        }
		      }

		      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		    } while (statementBuilder.getOffset() < totalResultSetSize);

		    System.out.printf("Number of results found: %d\n", totalResultSetSize);
		  }
	 
	 public static void getLineItemForOrder(DfpServices dfpServices, DfpSession session) throws Exception {
		    // Get the LineItemService.
		    LineItemServiceInterface lineItemService =
		        dfpServices.get(session, LineItemServiceInterface.class);

		    // Create a statement to only select line items updated or created since
		    // yesterday.
		    long orderId = 123922782;
		    StatementBuilder statementBuilder = new StatementBuilder()
		    .where("WHERE orderId = :id")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("id", orderId);
		       /* .where("orderId = 123922782 ")
		        .orderBy("id ASC")
		        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
		        .withBindVariableValue("lastModifiedDateTime",
		            DateTimes.toDateTime(Instant.now().minus(Duration.standardDays(1L)),
		                "America/New_York"));*/

		    // Default for total result set size.
		    int totalResultSetSize = 0;

		    do {
		      // Get line items by statement.
		      LineItemPage page =
		          lineItemService.getLineItemsByStatement(statementBuilder.toStatement());

		      if (page.getResults() != null) {
		        totalResultSetSize = page.getTotalResultSetSize();
		        int i = page.getStartIndex();
		        for (LineItem lineItem : page.getResults()) {
		          System.out.printf(
		              "%d) Line item with ID \"%d\" and name \"%s\" was found.\n", i++,
		              lineItem.getId(), lineItem.getName());
		          System.out.println("creative Size :"+lineItem.getCreativePlaceholders().length);
		          System.out.println("creative Size :"+lineItem.getCreativePlaceholders()[0].getSize().getHeight());
		          System.out.println("creative Size :"+lineItem.getCreativePlaceholders()[0].getSize().getWidth());
		        }
		      }

		      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		    } while (statementBuilder.getOffset() < totalResultSetSize);

		    System.out.printf("Number of results found: %d\n", totalResultSetSize);
		  }
	 
	  public static void getCreative(DfpServices dfpServices, DfpSession session) throws Exception {
		    // Get the CreativeService.
		    CreativeServiceInterface creativeService =
		        dfpServices.get(session, CreativeServiceInterface.class);

		    // Create a statement to get all creatives.
		    StatementBuilder statementBuilder = new StatementBuilder()
		        .orderBy("id ASC")
		        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

		    // Default for total result set size.
		    int totalResultSetSize = 0;
	          Set<String> creativeSet = new HashSet<>();

		    do {
		      // Get creatives by statement.
		      CreativePage page = creativeService.getCreativesByStatement(statementBuilder.toStatement());

		      if (page.getResults() != null) {
		        totalResultSetSize = page.getTotalResultSetSize();
		        int i = page.getStartIndex();
		        for (Creative creative : page.getResults()) {
		          System.out.printf(
		              "%d) Creative with ID \"%d\" and name \"%s\" was found.\n", i++,
		              creative.getId(), creative.getName());
		          System.out.println(creative.getCreativeType());
		          creativeSet.add(creative.getCreativeType());
		          
		      
		        }
		      }
		      for (String s : creativeSet) {
	        	    System.out.println(s);
	        	}
		      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		    } while (statementBuilder.getOffset() < totalResultSetSize);
		    
		    System.out.printf("Number of results found: %d\n", totalResultSetSize);
		  }

}