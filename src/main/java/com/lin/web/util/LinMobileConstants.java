package com.lin.web.util;




public class LinMobileConstants {
	
	public static final String EMAIL_ADDRESS="naresh.pokhriyal@mediaagility.com"; //mediaad2012sense@gmail.com
	public static final String EMAIL_PASSWORD="LinMobile8";//maadsense2012
	
	public static final String TO_EMAIL_ADDRESS="youdhveer.panwar@mediaagility.com"; //linmobile@mediaagility.com";
	public static final String CC_EMAIL_ADDRESS="youdhveer.panwar@mediaagility.com";
	public static final String TRAFFICKER_EMAIL="sahil.kapoor@mediaagility.com";
	public static final String APPLICATION_NAME ="ONE Analytics";
	public static final String APPLICATION_IMAGE_URL ="/img/oneAnalytics.png";
	
	public static final String REPORT_FOLDER = "inbox";
	
	public static final String DFP_NETWORK_CODE="5678"; // for MA: 12008447 , for: lin: 5678
	public static final String DFP_ADVERTISER_USERNAME="naresh.pokhriyal@mediaagility.com";
	public static final String DFP_APPLICATION_NAME="Lin";
	public static final String DFP_REPORT_BUCKET = "dfp_reports";
	public static final String DFP_API_SCOPE = "https://www.google.com/apis/ads/publisher";
	
	public static final String DFP_PERFORMANCE_BY_LOCATION_REPORT_BUCKET = "dfp_reports_by_location";
	
	public static final String DFP_TARGET_REPORT_BUCKET = "dfp_target_reports";
	public static final String DFP_TARGET_PLATFORM_TABLE_ID= "DFP_Target";
	public static final String DFP_TARGET_PLATFORM_TABLE_SCHEMA="DFPTargetSchema.csv"; 
	
	
	public static final String LIN_DIGITAL_DFP_NETWORK_CODE="4206"; 
	public static final String LIN_DIGITAL_DFP_APPLICATION_NAME="Lin Digital";
	public static final String LIN_DIGITAL_DFP_REPORT_BUCKET = "lin_digital_dfp_reports";
	public static final String LIN_DIGITAL_DFP_LOCATION_REPORTS_BUCKET="lin_digital_dfp_reports_by_location";
	public static final String LIN_DIGITAL_EMAIL_ADDRESS="harshal.limaye@linmedia.com"; 
	public static final String LIN_DIGITAL_EMAIL_PASSWORD="LinMobile!";
	public static final String LIN_DIGITAL_PUBLISHER_ID="2"; 
	public static final String LIN_DIGITAL_PUBLISHER_NAME="Lin Digital";
	public static final String DEFAULT_DATASOURE_NAME="DFP";
	public static final String CLOUD_STORAGE_DFP_REPORT_BUCKET = "dfp_reports";	
	
    public static final String CLIENT_ID = "15890596879-32ud3ferr9eo5oo5p0rpfsc26c8b8ftp.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "AZvu35VUnTb2xeZi2l1rSty6";
	
	public static final String MOJIVA_REST_SERVER_URL="http://www.mojiva.com/mocean_rest_server.php";
	public static final String MOJIVA_REST_SERVER_PRIVATE_KEY="51b9f49383b82";
	public static final String MOJIVA_SITE_METHOD="getSiteId";
	public static final String MOJIVA_SITE_BY_DATE_METHOD="getSiteInfoByDate";
	public static final String MOJIVA_SITE_BY_RANGE_METHOD="getSiteInfoByRange";
	public static final String MOJIVA_REPORTS_BUCKET="mojiva_reports";
	public static final String MOJIVA_CHANNEL_NAME="Mojiva";
	public static final String MOJIVA_CHANNEL_TYPE="Ad Network";
	public static final String MOJIVA_SALES_TYPE="Non-direct";
	
	public static final String MOJIVA_CHANNEL_ID="3";
	public static final String MOJIVA_PUBLISHER_ID="1";
	public static final String MOJIVA_PUBLISHER_NAME="Lin Media";
	
	public static final String NEXAGE_COMPNAY_ID="8a809449013c3c26198049f2ea6c4686";
	public static final String NEXAGE_COMPNAY_ACCESS_KEY="8a809449013c3c26198049f2ea6c4686";
	public static final String NEXAGE_COMPNAY_SECRET_KEY="82F677CB-7127-A668-A8B8-DA940DC85872";
	public static final String NEXAGE_REPORTS_API_URL="https://reports.nexage.com/access/8a809449013c3c26198049f2ea6c4686/reports/";
	public static final String NEXAGE_NET_REVENUE_SUMMARY_REPORT_ID="sellernetrevenuesummary";
	public static final String NEXAGE_CHANNEL_NAME="Nexage";
	public static final String NEXAGE_CHANNEL_ID="2";
	public static final String NEXAGE_CHANNEL_TYPE="RTB/Mediation";
	public static final String NEXAGE_SALES_TYPE="Non-direct";
	public static final String NEXAGE_PUBLISHER_ID="1";
	public static final String NEXAGE_PUBLISHER_NAME="Lin Media";
	public static final String NEXAGE_REPORTS_BUCKET="nexage_reports";
	
	
	
	public static final String ALLOWED_IP_ADDRESS="127.0.0.1";		
	
	//public static final String PROXY_URL="10.10.10.1";  // For MA local
	public static final String PROXY_URL=null;
	
	public static final String[] ADMINS={"SuperAdmin", "Administrator"};
	public static final String[] USERS_ARRAY={ADMINS[1], "Non Admin"};
	public static final String[] STATUS_ARRAY={"Active", "Inactive"};
	public static final String[] COMPANY_TYPE={"Publisher Pool Partner", "Demand Partner", "Client"};
	
	public static final String[] APP_VIEWS={"1:PUBLISHER DASHBOARD", "2:PERFORMANCE AND MONITORING", "3:PLANNING", "4:NEWS AND RESEARCH", "5:ADMIN", "6:MY SETTINGS", "7:ONE AUDIENCE", "8:CAMPAIGN PERFORMANCE", "9:DEMO EXPLORER", "10:REPORT"};

	public static final String[] DEMAND_PARTNER_APP_VIEWS={};
	public static final String[] PUBLISHER_PARTNER_APP_VIEWS={APP_VIEWS[0], APP_VIEWS[1], APP_VIEWS[2], APP_VIEWS[3], APP_VIEWS[6], APP_VIEWS[7], APP_VIEWS[8], APP_VIEWS[9]};
	public static final String[] CLIENT_APP_VIEWS={APP_VIEWS[0], APP_VIEWS[1], APP_VIEWS[2], APP_VIEWS[3], APP_VIEWS[6], APP_VIEWS[7], APP_VIEWS[8], APP_VIEWS[9]};
	public static final String[] COMPANY_APP_VIEWS={APP_VIEWS[0], APP_VIEWS[1], APP_VIEWS[2], APP_VIEWS[3], APP_VIEWS[6], APP_VIEWS[7], APP_VIEWS[8], APP_VIEWS[9]};
	
	public static final String ARRAY_SPLITTER=":";
	public static final String[] DEFINED_TYPES = {"Built-In", "Custom"};
	public static final String ALL_PROPERTIES = "All Properties";
	public static final String ALL_ADVERTISERS = "All Advertisers";
	public static final String ALL_ORDERS = "All Orders";
	public static final String ALL_AGENCIES = "All Agencies";
	public static final String ALL_ACCOUNTS = "All Accounts";
	public static final String NO_RESTRICTIONS = "No Restrictions";
	public static final String AGENCY_ID_PREFIX = "Agency";
	public static final String ADVERTISER_ID_PREFIX = "Advertiser";
	public static final String[] DEMAND_PARTNER_TYPES = {"Direct", "Non-direct", "House"};
	public static final String TEAM_ALL_ENTITIE = "All Entities";
	public static final String TEAM_NO_ENTITIE = "NO Entity";
	public static final String COMPANY_LOGO_NAME_PREFIX = "SynergyMapImages";
	public static final String COMPANY_IMAGES_FOLDER = "CompanyLogo";
	public static final String DEFAULT_PASSWORD ="``````````";
	public static final String[] DROP_DOWN_VALUE = {"Campaign Type", "Campaign status", "Education", "Ethinicity"};
	
	public static final String GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY = "892d0834292bff62001a53a17624d1b22f1002a3-privatekey.p12";
	public static final String GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "15890596879-kp0i5r5lk77nlvmpl9bsnjufmeksqqrj@developer.gserviceaccount.com";
	public static final String GOOGLE_API_PROJECT_ID = "mediaagility.com:ma-maps";
	public static final String GOOGLE_BIGQUERY_SCOPE = "https://www.googleapis.com/auth/bigquery";
	
	public static final String BIGQUERY_SCHEMA_FOLDER = "bigQuerySchema";
	
	public static final String AD_EXCHANGE_WEBSERVICE_SCOPE="https://www.googleapis.com/auth/adexchange.seller";
    public static final String AD_EXCHANGE_CHANNEL_NAME="Google Ad exchange";
	public static final String AD_EXCHANGE_CHANNEL_ID="7";
	public static final String AD_EXCHANGE_CHANNEL_TYPE="RTB/Mediation";
	public static final String AD_EXCHANGE_SALES_TYPE="Non-direct";
	public static final String AD_EXCHANGE_PUBLISHER_ID="1";
	public static final String AD_EXCHANGE_PUBLISHER_NAME="Lin Media";
	public static final String AD_EXCHANGE_REPORTS_BUCKET="adexchange_reports";
	public static final String AD_EXCHANGE_TABLE_NAME="GoogleAdExchange";
	public static final String AD_EXCHANGE_DATA_SOURCE="Google AdExchange";
	
	public static final String DFP_LIN_MEDIA_CHANNEL_ID="4";
	public static final String LIN_MEDIA_PUBLISHER_ID="1";
	public static final String LIN_MEDIA_PUBLISHER_NAME="Lin Media";
	public static final String DFP_LIN_MEDIA_REPORT_BUCKET="lin_media_dfp_reports";
	
	public static final String CORE_PERFORMANCE_TABLE_ID= "Core_Performance";
	public static final String CORE_PERFORMANCE_TABLE_SCHEMA="CorePerformanceSchema2.csv"; //For old jobs
	
	//public static final String CORE_PERFORMANCE_FINILIZED_TABLE_ID= "Core_Performance_New"; //For Demo
	public static final String CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA="CorePerformanceSchema3.csv"; //For new jobs
	
	public static final String PERFORMANCE_BY_LOCATION_TABLE_SCHEMA="PerformanceByLocationSchema_2.csv";
	public static final String PERFORMANCE_BY_LOCATION_TABLE_ID="Performance_By_Location";
	
	public static final String BQ_UPLOADER_FAIL_SUBJECT="Data failed to upload in BigQuery";
	public static final String BQ_UPLOADER_FAIL_MSG="Please check the log, data failed to upload on BigQuery.";
	
	public static final String UNDERTONE_CHANNEL_NAME="Undertone";
	public static final String UNDERTONE_CHANNEL_ID="6";
	public static final String UNDERTONE_CHANNEL_TYPE="Ad Network";
	public static final String UNDERTONE_SALES_TYPE="Non-direct";
	public static final String UNDERTONE_PUBLISHER_ID="1";
	public static final String UNDERTONE_PUBLISHER_NAME="Lin Media";
	public static final String UNDERTONE_REPORTS_BUCKET="undertone_reports";
	public static final String UNDERTONE_DATA_SOURCE="Undertone";
	
	public static final String SELL_THROUGH_REPORTS_BUCKET="sell_through_reports";
	public static final String SELL_THROUGH_PUBLISHER_ID="1";
	public static final String SELL_THROUGH_PUBLISHER_NAME="Lin Media";
	public static final String SELL_THROUGH_TABLE_ID= "Sell_Through";
	public static final String SELL_THROUGH_TABLE_SCHEMA="SellThroughSchema.csv";
	
	public static final String REPORT_TYPE_DFP_CORE_PERFORMANCE="CorePerformance";
	public static final String REPORT_TYPE_DFP_PERFORMANCE_BY_LOCATION="PerformanceByLocation";
	public static final String REPORT_TYPE_DFP_SELL_THROUGH="SellThrough";
	
	public static final String LIN_DIGITAL_GOLFSMITH_ORDER_NAME="LINMobile-Golfsmith-Golfsmith2013-2013-07";
	public static final String LIN_DIGITAL_GOLFSMITH_ADVERTISER="Golfsmith";
	public static final String LIN_DIGITAL_GOLFSMITH_ADVERTISER_ID="22491019";
	public static final String LIN_DIGITAL_XAD_ORDER_NAME="Golfsmith SF/DC Test";
	
	public static final int CHANNEL_DATA_EXPIRATION_TIME = (60*60*12);
	
	public static final String LSN_CHANNEL_ID="10";
	public static final String LSN_CHANNEL_NAME="LSN";
	public static final String LSN_CHANNEL_TYPE="Ad Network";
	public static final String LSN_SALES_TYPE="Non-direct";
	public static final String LSN_PUBLISHER_ID="1";
	public static final String LSN_PUBLISHER_NAME="Lin Media";
	
	public static final String LSN_REPORTS_BUCKET="lsn_reports";
	
	public static final String RICH_MEDIA_REPORTS_BUCKET="rich_media_reports";	
	public static final String RICH_MEDIA_TRAFFICKING_TABLE_ID= "Rich_Media_Trafficking";
	public static final String RICH_MEDIA_TRAFFICKING_TABLE_SCHEMA="RichMediaTraffickingSchema.csv";
	
	public static final String RICH_MEDIA_TABLE_ID= "Rich_Media";
	public static final String RICH_MEDIA_TABLE_SCHEMA="RichMediaSchema.csv";    // USED FOR VIDEO CAMPAIGNS
	public static final String RICH_MEDIA_EVENT_GRAPH_CLICK_TO_CALLS="Click to Calls";
	public static final String RICH_MEDIA_EVENT_GRAPH_URL="URL";
	public static final String RICH_MEDIA_EVENT_GRAPH_COUPONS_DOWNLOADS="Coupons Downloads";
	public static final String RICH_MEDIA_EVENT_GRAPH_FIND_STORE="Find Store";
	public static final String CUSTOM_EVENT_TABLE_ID= "CustomEvent";
	public static final String CUSTOM_EVENT_TABLE_SCHEMA="RichMediaCustomEventSchema.csv";
	
	
	public static final String CELTRA_CHANNEL_NAME="Celtra";
	public static final String CELTRA_CHANNEL_ID="11";
	public static final String CELTRA_CHANNEL_TYPE="Ad Network";
	public static final String CELTRA_SALES_TYPE="National sales direct";
	public static final String CELTRA_PUBLISHER_ID="2";
	public static final String CELTRA_PUBLISHER_NAME="Lin Digital";
	public static final String CELTRA_REPORTS_BUCKET="celtra_reports";
	
	public static final String DFP_DATA_SOURCE="DFP";
	
	public static final String DFP_LIN_MEDIA_TABLE_NAME="DFP_Lin_Media";
	public static final String DFP_LIN_DIGITAL_TABLE_NAME="DFP_Lin_Digital";	
	public static final String DFP_LIN_MEDIA_BY_ORDER_ID_TABLE_NAME="DFP_Lin_Media_ByOrderId";
	public static final String DFP_LIN_DIGITAL_BY_ORDER_ID_TABLE_NAME="DFP_Lin_Digital_ByOrderId";
	
	public static final String XAD_CHANNEL_NAME="XAd";
	public static final String XAD_CHANNEL_ID="12";
	public static final String XAD_CHANNEL_TYPE=""; 
	public static final String XAD_SALES_TYPE="National sales direct";
	public static final String XAD_PUBLISHER_ID="3"; 
	public static final String XAD_PUBLISHER_NAME="XAd"; 
	public static final String XAD_REPORTS_BUCKET="xad_reports";
	public static final String XAD_AD_FORMAT="Interstitial";
	
	public static final String [] XAD_CUSTOM_EVENT_ARRAY={"Click to call","Map","Directions","More Information"};
	public static final String XAD_CUSTOM_EVENT_CLICK_TO_CALL="Click to call";
	public static final String XAD_CUSTOM_EVENT_MAP="Map";
	public static final String XAD_CUSTOM_EVENT_DIRECTION="Directions";
	public static final String XAD_CUSTOM_EVENT_MORE_INFORMATION="More Information";
	
	public static final String XAD_AGENCY_GOLFSMITH="Greatest Common Factory";
	public static final String XAD_ORDER_START_DATE="07/19/2013";
	public static final String XAD_ORDER_END_DATE="08/30/2013";
		
	
/*	public static final String GOLFSMITH_CAMPAIGN_CATEGORY_LOW_PRICE="15% off Coupon";
	public static final String GOLFSMITH_CAMPAIGN_CATEGORY_CUSTOM_FIT="Custom Fit Coupon";
	public static final double GOLFSMITH_RATE=4.0;
	public static final long GOLFSMITH_GOAL_QTY= (1022370/4);	
	public static final String LIN_DIGITAL_ECONOMIST_ORDER_NAME="The Economist Chicago Blitz";
	public static final String LIN_DIGITAL_ECONOMIST_ORDER_ID="149356219";
	public static final String LIN_DIGITAL_ECONOMIST_ADVERTISER="The Economist";
	public static final String LIN_DIGITAL_ECONOMIST_AGENCY="M&C Saatchi Mobile";
	public static final double LIN_DIGITAL_ECONOMIST_BUDGET=21250.00;
	public static final double LIN_DIGITAL_ECONOMIST_RATE=5.35;*/
	
	public static final String LIN_DIGITAL_LIN_ONE_ORDER_ID="141903979";
		
    public static final long [] LIN_DIGITAL_ORDER_IDS={149356219,139570339, 141903979,136688419};
    
    
    public static final String TRIBUNE_CHANNEL_NAME="Tribune";
	public static final String TRIBUNE_CHANNEL_ID="13";
	public static final String TRIBUNE_CHANNEL_TYPE="";
	public static final String TRIBUNE_SALES_TYPE="National sales direct";
	//public static final String TRIBUNE_PUBLISHER_ID="4";
	//public static final String TRIBUNE_PUBLISHER_NAME="Lin Digital";
	//public static final String TRIBUNE_REPORTS_BUCKET="tribune_reports";
	
    public static final String TRIBUNE_DFP_NETWORK_CODE="45604844"; 
	public static final String TRIBUNE_DFP_APPLICATION_NAME="Tribune Broadcast";
	public static final String TRIBUNE_DFP_REPORT_BUCKET = "tribune_dfp_reports";
	public static final String TRIBUNE_DFP_LOCATION_REPORTS_BUCKET="tribune_dfp_reports_by_location";
	public static final String TRIBUNE_DFP_EMAIL_ADDRESS="harshal.limaye@linmedia.com"; 
	public static final String TRIBUNE_DFP_EMAIL_PASSWORD="LinMobile!";
	public static final String TRIBUNE_DFP_PUBLISHER_ID="4"; 
	public static final String TRIBUNE_DFP_PUBLISHER_NAME="Tribune";
	public static final String TRIBUNE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY ="a3a60214c8e6e20038146d67939652454f9689ee-privatekey.p12";
	public static final String TRIBUNE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "957746284653-olvqlr08j8l47m42r554tthfrnbb5h4f@developer.gserviceaccount.com";
	public static final String TRIBUNE_GOOGLE_API_PROJECT_ID = "one-tribune";
	
	
	public static final String FUSION_TABLE_SCOPE="https://www.googleapis.com/auth/fusiontables";
	
	public static final String DEPLOYMENT_VERSION = DateUtil.getCurrentTimeStamp("yyyyMMddHHmmssSSS");
	public static final String DMA_KML_CLOUD_STORAGE_PATH="http://commondatastorage.googleapis.com/linmobile_dev/kml/nielsen_dma.kml";
	
	
	public static final String LIN_MEDIA_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY ="1aed57b0d19553bd2254ccad779382fa2cbe0536-privatekey.p12";
	public static final String LIN_MEDIA_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "252848309247-fbte1hb7llcgpc5plg6v43nod8phvc5a@developer.gserviceaccount.com";
	public static final String LIN_MEDIA_GOOGLE_API_PROJECT_ID = "lin-media";
	
	public static final String LIN_DIGITAL_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY = "40ecebfb001f592ddf673e508962fecc1ad5bf46-privatekey.p12";
	public static final String LIN_DIGITAL_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "27673172330-s46hn85ibv3cmvu17p9nn2uetru782h4@developer.gserviceaccount.com";
	public static final String LIN_DIGITAL_GOOGLE_API_PROJECT_ID = "lin-digital";
		
	
	public static final String LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY ="ca759c6a1b8769003620533fcb935fdb767bb860-privatekey.p12";
	public static final String LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "60688247237-08jb4td5ns1l1l3ao52jdibitinhqb7p@developer.gserviceaccount.com";
	public static final String LIN_MOBILE_GOOGLE_API_PROJECT_ID = "one-lin-mobile";
	public static final String LIN_MOBILE_PUBLISHER_ID="5"; //company id
	public static final String LIN_MOBILE_PUBLISHER_NAME="LIN Mobile";  //company name
	public static final String LIN_MOBILE_DFP_REPORT_BUCKET="lin_mobile_dfp_reports";
	public static final String LIN_MOBILE_DFP_NETWORK_CODE="5678";
	public static final String LIN_MOBILE_DFP_APPLICATION_NAME="Lin Mobile";
	public static final String LIN_MOBILE_DFP_EMAIL_ADDRESS="naresh.pokhriyal@mediaagility.com"; 
	public static final String LIN_MOBILE_DFP_EMAIL_PASSWORD="LinMobile8";
	public static final String LIN_MOBILE_AD_SDK_BQ_TABLE_ID = "Lin_Mobile_Ad_Sdk_Data";
	
	public static final String LIN_MOBILE_NEW_DFP_REPORT_BUCKET="lin_mobile_new_dfp_reports";
	public static final String LIN_MOBILE_NEW_DFP_NETWORK_CODE="9331149";
	public static final String LIN_MOBILE_NEW_DFP_EMAIL_ADDRESS="harshal.limaye@linmedia.com"; 
	public static final String LIN_MOBILE_NEW_DFP_EMAIL_PASSWORD="LinMobile!";
	
	public static final int CHANGE_WINDOW_SIZE=3;
	
	public static final double THRESHOLD=0.001; 
	
	public static final String BQ_CORE_PERFORMANCE="CorePerformance";
	public static final String BQ_PERFORMANCE_BY_LOCATION="PerformanceByLocation";
	public static final String BQ_DFP_TARGET="DFPTarget";
	public static final String BQ_CUSTOM_EVENT="CustomEvent";
	public static final String BQ_RICH_MEDIA="RichMedia";
	public static final String BQ_SELL_THROUGH="Sell_Through";
	public static final String BQ_PRODUCT_PERFORMANCE="Product_Performance";
	
	public static final String PRODUCT_PERFORMANCE_TABLE_SCHEMA="ProductPerformanceSchema.csv";
	
	public static final String CLIENT_DEMO_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY ="ceb06473c4b08b6da76f3a7eb827fbb6d783c1df-privatekey.p12";
	public static final String CLIENT_DEMO_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS = "393383870106-6ip8c57lbvlm18adg079l7q45nmutprf@developer.gserviceaccount.com";
	public static final String CLIENT_DEMO_GOOGLE_API_PROJECT_ID = "one-client-demo";
	public static final String CLIENT_DEMO_COMPANY_ID="0"; //company id
	public static final String CLIENT_DEMO_COMPANY_NAME="Client Demo";  //company name
	public static final String CLIENT_DEMO_CLOUD_STORAGE_BUCKET="lin_demo";
	
/*	public static final String[] CAMPAIGN_STATUS={"0:All","1:Active", "2:Running", "3:Paused", "4:Draft",
													"5:Completed", "6:Archived","7:Approve","8:Ready","9:Inactive"};*/
	
	public static final String OVER_FLOW_PARTNER_NAME="Overflow Impressions";
	public static final String OVER_FLOW_PRODUCT_NAME="Run DSP";
	
	public static final String EXAMINER_DFP_PUBLISHER_ID="6";
	public static final String EXAMINER_DFP_PUBLISHER_NAME="Examiner";
	public static final String EXAMINER_DFP_NETWORK_CODE="5578"; 
	public static final String EXAMINER_DFP_APPLICATION_NAME="Examiner.com";
	public static final String EXAMINER_DFP_EMAIL_ADDRESS="harshal.limaye@linmedia.com"; 
	public static final String EXAMINER_DFP_EMAIL_PASSWORD="LinMobile!";
	
	public static final String TOPIX_DFP_PUBLISHER_ID="7";
	public static final String TOPIX_DFP_PUBLISHER_NAME="Topix";
	public static final String TOPIX_DFP_NETWORK_CODE="5578"; 
	public static final String TOPIX_DFP_APPLICATION_NAME="Topix_DFP SB_Mobile";
	public static final String TOPIX_DFP_EMAIL_ADDRESS="harshal.limaye@linmedia.com"; 
	public static final String TOPIX_DFP_EMAIL_PASSWORD="LinMobile!";
	
	public static final String  NEW_US_STATE_PROD_TABLEID = "12180835279485187370-16047606221156071538"; // table id for GME vector table new_us_state_prod
	
	
	/* ******  Define type of data loading ***/
	public static final String LOAD_TYPE_COMMON = "common";
	public static final String LOAD_TYPE_CORE_PERFORMANCE = "coreperformance";
	public static final String LOAD_TYPE_LOCATION = "location";
	public static final String LOAD_TYPE_TARGET  = "target";
	public static final String LOAD_TYPE_CUSTOM_EVENT = "customevent";
	public static final String LOAD_TYPE_RICH_MEDIA = "richmedia";
	public static final String LOAD_TYPE_PRODUCT_PERFORMANCE = "productperformance";
	public static final String LOAD_TYPE_SELL_THROUGH = "sellthrough";
	
	/* ******  No of order IDs for define type of data loading ***/
	
	public static final int LOAD_TYPE_CORE_PERFORMANCE_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_LOCATION_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_TARGET_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_CUSTOM_EVENT_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_RICH_MEDIA_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_PRODUCT_PERFORMANCE_ORDER_COUNT = 50;
	public static final int LOAD_TYPE_SELL_THROUGH_ORDER_COUNT = 50;
	public static final int DAILY_REPORT_ORDER_COUNT = 36;
	
	public static final int[] PRODUCT_FORCAST_DAY = {30};
	
}
