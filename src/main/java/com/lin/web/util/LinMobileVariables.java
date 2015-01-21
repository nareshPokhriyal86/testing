package com.lin.web.util;

public class LinMobileVariables {

	public static final String REDIRECT_URL= "http://dev-linmobile.appspot.com";
	public static final String CALLBACK_URL = REDIRECT_URL+"/oauth2callback";
	//public static final String CALLBACK_URL = "http://localhost:8888/oauth2callback";
	
	public static final String SENDER_EMAIL_ADDRESS="naresh.pokhriyal@mediaagility.com";
	public static final String SENDER_NAME="ONE Analytics";
	
	public static final String AUTHORIZATION_URL = REDIRECT_URL+"/authorizeEmail.lin?authorize=";
	public static final String FORGET_PASSWORD_URL = REDIRECT_URL+"/forgetPasswordEmail.lin?reset=";
	public static final String APPLICATION_URL =REDIRECT_URL;
	
	public static final String GOOGLE_LOGIN_CLIENT_ID = "15890596879-46c4fqt0vo67sssq5njh020jvu622v1e.apps.googleusercontent.com";
	public static final String GOOGLE_LOGIN_CLIENT_SECRET = "rAz2fsrTm7LwzCGW3o3F_SMz";
	public static final String GOOGLE_LOGIN_REDIRECT_URI= REDIRECT_URL+"/googleLoginCallback";
	
	public static final String GOOGLE_BIGQUERY_DATASET_ID = "LIN_DEV";
	public static final String GOOGLE_BIGQUERY_RAW_DATASET_ID = "LIN_RAW";
	public static final String APPLICATION_BUCKET_NAME = "linmobile_dev";
	public static final String LIN_DIGITAL_CLOUD_STORAGE_BUCKET = "lindigital_dev";
	public static final String LIN_MEDIA_CLOUD_STORAGE_BUCKET = "linmedia_dev";
	public static final String LIN_MOBILE_CLOUD_STORAGE_BUCKET = "dev_linmobile";
	public static final String TRIBUNE_CLOUD_STORAGE_BUCKET = "tribune_dev";
	
	public static final String AD_EXCHANGE_CALLBACK_URL = REDIRECT_URL+"/oauth2callback"; //"http://localhost:8888/oauth2callback";	
    public static final String AD_EXCHANGE_CLIENT_ID = "15890596879-bredjp941m44mdiot25heval203l0m4d.apps.googleusercontent.com" ;//FOR LOCAL
	public static final String AD_EXCHANGE_CLIENT_SECRET = "5l-1QXy_I-mxYPl8qvE7tEIi"; //"4DD2aiKUh-5N8HJhDps2Tugq"  //FOR LOCAL
	public static final String APPLICATION_TYPE = "SynergyDev";		
	public static final String BACKEND_NAME="linbackend"; 
	
	public static final String FUSION_TABLE_CLIENT_ID="904092960638-vneh35des39q218jk65g8mkr1ctbs9bi.apps.googleusercontent.com" ;
	public static final String FUSION_TABLE_CLIENT_SECRET="ymp5AwsJA7Zpf9r__G7pB3a-" ;
	public static final String FUSION_TABLE_CALLBACK_URL=REDIRECT_URL+"/fusionCallback.lin";
	public static final String FUSION_POOL_MAP_MERGED_TABLE="1E7DIhLUrpSoQQOtb6ukIyqYBBxEAg6c0VN3C-mQ";
	public static final String FUSION_INVENTORY_FORECAST_TABLE="1tkbBLzvGq89lKMpdwfJXpgofZerLj9L0bvSlh8g";
	
	public static final String SERVICE_ACCOUNT_KEY= "dev-linmobile-434b93f8d516.p12"; // Earlier it was: "dev-linmobile-890a4e0854bc.p12";
	public static final String SERVICE_ACCOUNT_EMAIL="615421841086-a6a45sbvvc9edot88pi63ureo0tp8vjp@developer.gserviceaccount.com"; // Eearlier it was: "615421841086-7daokr9p00sghao5fsvulubo85ejc0gg@developer.gserviceaccount.com";
	
	// Added by Anup for Geo-Census Module
	public static final String LINMEDIA_BIGQUERY_GEO_DATASET = "GEO";
}
