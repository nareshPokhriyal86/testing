package com.lin.web.util;

public class LinMobileVariablesProduction {

	public static final String REDIRECT_URL= "http://linmobile-backend.appspot.com";
	public static final String CUSTOM_REDIRECT_URL= "http://analytics.linone.com";
	
	public static final String CALLBACK_URL = REDIRECT_URL+"/oauth2callback";	
	
	public static final String SENDER_EMAIL_ADDRESS="noreply@linone.com";
	public static final String SENDER_NAME="ONE Analytics";
	
	public static final String AUTHORIZATION_URL = CUSTOM_REDIRECT_URL+"/authorizeEmail.lin?authorize=";	
	public static final String FORGET_PASSWORD_URL = CUSTOM_REDIRECT_URL+"/forgetPasswordEmail.lin?reset=";
	public static final String APPLICATION_URL =REDIRECT_URL;
	
	public static final String GOOGLE_LOGIN_CLIENT_ID = "15890596879-i9fvr59p30gil3uur0asg3llfrn8c7jn.apps.googleusercontent.com";
	public static final String GOOGLE_LOGIN_CLIENT_SECRET = "agTicP0lefgUtQTMVDTlbfrP";
	public static final String GOOGLE_LOGIN_REDIRECT_URI= REDIRECT_URL+"/googleLoginCallback";
	
	public static final String GOOGLE_BIGQUERY_DATASET_ID = "LIN";
	public static final String GOOGLE_BIGQUERY_RAW_DATASET_ID = "LIN_RAW";

	
	public static final String APPLICATION_BUCKET_NAME = "linmobile";
	public static final String LIN_DIGITAL_CLOUD_STORAGE_BUCKET = "lindigital_prod";
	public static final String LIN_MEDIA_CLOUD_STORAGE_BUCKET = "linmedia_prod";
	public static final String LIN_MOBILE_CLOUD_STORAGE_BUCKET = "prod_linmobile";
	public static final String TRIBUNE_CLOUD_STORAGE_BUCKET = "tribune_prod";
	
	public static final String AD_EXCHANGE_CALLBACK_URL = REDIRECT_URL+"/oauth2callback";	
    public static final String AD_EXCHANGE_CLIENT_ID = "15890596879-32ud3ferr9eo5oo5p0rpfsc26c8b8ftp.apps.googleusercontent.com";
	public static final String AD_EXCHANGE_CLIENT_SECRET = "AZvu35VUnTb2xeZi2l1rSty6";
	
	public static final String APPLICATION_TYPE = "SynergyProd";
	public static final String BACKEND_NAME="linbackend"; 
	
	public static final String FUSION_TABLE_CLIENT_ID="904092960638-5f266ghcmcag8irhgna3rsu25ilc5b2l.apps.googleusercontent.com" ;
	public static final String FUSION_TABLE_CLIENT_SECRET="dOnphK3jmh7JNdsvZMq7e63b" ;
	public static final String FUSION_TABLE_CALLBACK_URL=REDIRECT_URL+"/fusionCallback.lin";
	public static final String FUSION_POOL_MAP_MERGED_TABLE="1cTLtS6TAMB3N9Bv4cfq6BaBgjGBUtN6VR0nxMN8";
	public static final String FUSION_INVENTORY_FORECAST_TABLE="165G9r9SIPgWgZXDW3iecy_BtRzxMtJlvyJxsWao";
	
	public static final String SERVICE_ACCOUNT_KEY="linmobile-backend-11f89afed7aa.p12";
	public static final String SERVICE_ACCOUNT_EMAIL="84127901856-b3t8ooqte83lr03kj344hbkmqkp6fluc@developer.gserviceaccount.com";
	
	// Added by Anup for Geo-Census Module
	public static final String LINMEDIA_BIGQUERY_GEO_DATASET = "GEO";
	
}
