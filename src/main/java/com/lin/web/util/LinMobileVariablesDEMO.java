
package com.lin.web.util;

public class LinMobileVariablesDEMO {

	public static final String REDIRECT_URL= "http://one-client-demo.appspot.com";
	public static final String CALLBACK_URL = REDIRECT_URL+"/oauth2callback";	
		
	public static final String SENDER_EMAIL_ADDRESS="naresh.pokhriyal@mediaagility.com";
	public static final String SENDER_NAME="Client Demo";
	
	public static final String AUTHORIZATION_URL = REDIRECT_URL+"/authorizeEmail.lin?authorize=";
	public static final String FORGET_PASSWORD_URL =REDIRECT_URL+"/forgetPasswordEmail.lin?reset=";
	public static final String APPLICATION_URL =REDIRECT_URL;
	
	public static final String GOOGLE_LOGIN_CLIENT_ID = "15890596879-f888ib85d7jd1fqugfkfki81pcs2o5eo.apps.googleusercontent.com";
	public static final String GOOGLE_LOGIN_CLIENT_SECRET = "S5q3DxprFSItMwGrLRwwjzel";
	public static final String GOOGLE_LOGIN_REDIRECT_URI= REDIRECT_URL+"/googleLoginCallback";
	
	public static final String GOOGLE_BIGQUERY_DATASET_ID = "LIN_DEMO";
	public static final String GOOGLE_BIGQUERY_RAW_DATASET_ID = "LIN_RAW";

	public static final String APPLICATION_BUCKET_NAME = "lin_demo";
	public static final String BACKEND_NAME="demobackend"; 
	
	public static final String LIN_DIGITAL_CLOUD_STORAGE_BUCKET = "lindigital_dev";
	public static final String LIN_MEDIA_CLOUD_STORAGE_BUCKET = "linmedia_dev";
	public static final String LIN_MOBILE_CLOUD_STORAGE_BUCKET = "dev_linmobile";
	public static final String TRIBUNE_CLOUD_STORAGE_BUCKET = "tribune_dev";
	
	public static final String AD_EXCHANGE_CALLBACK_URL = REDIRECT_URL+"/oauth2callback";		
    public static final String AD_EXCHANGE_CLIENT_ID = "15890596879-ne8u4c61787d1ekqfh9aj98onqq441pt.apps.googleusercontent.com" ;
	public static final String AD_EXCHANGE_CLIENT_SECRET = "XV0LmmjSnStC5t_vQveC89NZ"; 
	public static final String APPLICATION_TYPE = "SynergyDEMO";	
	
	public static final String FUSION_TABLE_CLIENT_ID="" ;
	public static final String FUSION_TABLE_CLIENT_SECRET="" ;
	public static final String FUSION_TABLE_CALLBACK_URL=REDIRECT_URL+"/fusionCallback.lin";
	public static final String FUSION_POOL_MAP_MERGED_TABLE="";
	public static final String FUSION_INVENTORY_FORECAST_TABLE="";	

	public static final String SERVICE_ACCOUNT_KEY="ClientDemo-6e3e2ca6a16c.p12";
	public static final String SERVICE_ACCOUNT_EMAIL="393383870106-hns448tnp463a55jcmfvu9eiuimq3i6a@developer.gserviceaccount.com";
	
	// Added by Anup for Geo-Census Module
	public static final String LINMEDIA_BIGQUERY_GEO_DATASET = "GEO";
}
