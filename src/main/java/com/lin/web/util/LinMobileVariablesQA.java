package com.lin.web.util;

public class LinMobileVariablesQA {
	
	public static final String REDIRECT_URL= "http://qa-linmobile.appspot.com";
	public static final String CALLBACK_URL = REDIRECT_URL+"/oauth2callback";	
		
	public static final String SENDER_EMAIL_ADDRESS="noreply@linone.com";
	public static final String SENDER_NAME="ONE Analytics";
	
	public static final String AUTHORIZATION_URL = REDIRECT_URL+"/authorizeEmail.lin?authorize=";
	public static final String FORGET_PASSWORD_URL =REDIRECT_URL+"/forgetPasswordEmail.lin?reset=";
	public static final String APPLICATION_URL =REDIRECT_URL;
	
	public static final String GOOGLE_LOGIN_CLIENT_ID = "15890596879-fmsqjafjdb1rio69l4m3tg6h9vs12qtr.apps.googleusercontent.com";
	public static final String GOOGLE_LOGIN_CLIENT_SECRET = "cTN2aQ1G5T0tI0OLnQDsVgFA";
	public static final String GOOGLE_LOGIN_REDIRECT_URI= REDIRECT_URL+"/googleLoginCallback";
	
	public static final String GOOGLE_BIGQUERY_DATASET_ID = "LIN_QA";
	public static final String GOOGLE_BIGQUERY_RAW_DATASET_ID = "LIN_RAW";

	
	public static final String APPLICATION_BUCKET_NAME = "linmobile_qa";
	public static final String LIN_DIGITAL_CLOUD_STORAGE_BUCKET = "lindigital_qa";
	public static final String LIN_MEDIA_CLOUD_STORAGE_BUCKET = "linmedia_qa";
	public static final String LIN_MOBILE_CLOUD_STORAGE_BUCKET = "qa_linmobile";
	public static final String TRIBUNE_CLOUD_STORAGE_BUCKET = "tribune_qa";
	
	
	public static final String AD_EXCHANGE_CALLBACK_URL = REDIRECT_URL+"/oauth2callback";	
    public static final String AD_EXCHANGE_CLIENT_ID = "15890596879-mak2r1okfp91qd7t4fqsbt0nuc795krk.apps.googleusercontent.com";
	public static final String AD_EXCHANGE_CLIENT_SECRET = "vXp19fQhKlx70FOu3APpldem";
	public static final String APPLICATION_TYPE = "SynergyQA";
	public static final String BACKEND_NAME="linbackend"; 
	
	public static final String FUSION_TABLE_CLIENT_ID="904092960638-uh6fr6jq72vp2blsm34t437tppd2v11a.apps.googleusercontent.com" ;
	public static final String FUSION_TABLE_CLIENT_SECRET="Mp3FjQoDbqNwFWhX5cIw_eMX" ;
	public static final String FUSION_TABLE_CALLBACK_URL=REDIRECT_URL+"/fusionCallback.lin";
	public static final String FUSION_POOL_MAP_MERGED_TABLE="1cTLtS6TAMB3N9Bv4cfq6BaBgjGBUtN6VR0nxMN8";
	public static final String FUSION_INVENTORY_FORECAST_TABLE="165G9r9SIPgWgZXDW3iecy_BtRzxMtJlvyJxsWao";
	
	public static final String SERVICE_ACCOUNT_KEY="qa-linmobile-e4b4d95cc084.p12";
	public static final String SERVICE_ACCOUNT_EMAIL="519435726072-ivlpk6aljm5g3tq3hgqg1n9rhhtb8b02@developer.gserviceaccount.com";
	
	// Added by Anup for Geo-Census Module
	public static final String LINMEDIA_BIGQUERY_GEO_DATASET = "GEO";
	
}
