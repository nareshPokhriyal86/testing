package com.lin.dfp.api.impl;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.collect.ImmutableList;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;

public class DFPAuthenticationUtil{
	
	private static final Logger log = Logger.getLogger(DFPAuthenticationUtil.class.getName());
	private static final String DFP_AUTH_SCOPE="https://www.googleapis.com/auth/dfp";
	
	
	public static DfpSession getDFPSession(String dfpNetworkCode,String dfpApplicationName) 
			throws GeneralSecurityException, IOException, ValidationException{
		if(dfpApplicationName==null){
			dfpApplicationName="None";
		}
		log.info("Authorising service account...");
		// Create service account credential.
        GoogleCredential credential = new GoogleCredential.Builder()
            .setTransport(new NetHttpTransport())
            .setJsonFactory(new GsonFactory())
            .setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL)
            .setServiceAccountScopes(ImmutableList.of(DFP_AUTH_SCOPE))
            .setServiceAccountPrivateKeyFromP12File(new File("keys/"+LinMobileVariables.SERVICE_ACCOUNT_KEY))
            .build();
        credential.refreshToken();
        
        log.info(" now going to build dfpSession ...");
        // Construct a DfpSession.
        DfpSession dfpSession = new DfpSession.Builder()
            .withNetworkCode(dfpNetworkCode)
            .withApplicationName(dfpApplicationName)
            .withOAuth2Credential(credential)
            .build();
        
        return dfpSession;
	}
	
	
	public static DfpSession getDFPSession(String networkCode) throws Exception{
		String applicationName=null;
		String email=null;
		String password=null;
		boolean clientLogin=false;
		
		switch(networkCode){
			case "5678":
				applicationName=LinMobileConstants.DFP_APPLICATION_NAME;
				email=LinMobileConstants.EMAIL_ADDRESS;
				password=LinMobileConstants.EMAIL_PASSWORD;
				break;
			case "4206":
				applicationName=LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME;
				email=LinMobileConstants.LIN_DIGITAL_EMAIL_ADDRESS;
				password=LinMobileConstants.LIN_DIGITAL_EMAIL_PASSWORD;
				break;
			case "9331149":
				applicationName=LinMobileConstants.LIN_MOBILE_DFP_APPLICATION_NAME;
				email=LinMobileConstants.LIN_MOBILE_NEW_DFP_EMAIL_ADDRESS;
				password=LinMobileConstants.LIN_MOBILE_NEW_DFP_EMAIL_PASSWORD;
				break;
			case "45604844":
				applicationName=LinMobileConstants.TRIBUNE_DFP_APPLICATION_NAME;
				email=LinMobileConstants.TRIBUNE_DFP_EMAIL_ADDRESS;
				password=LinMobileConstants.TRIBUNE_DFP_EMAIL_PASSWORD;
				/** TODO : Remove this 2014-12-02  Manish Mudgal: */
				//clientLogin=true;
				break;
			case "5578":
				applicationName=LinMobileConstants.EXAMINER_DFP_APPLICATION_NAME;
				email=LinMobileConstants.EXAMINER_DFP_EMAIL_ADDRESS;
				password=LinMobileConstants.EXAMINER_DFP_EMAIL_PASSWORD;
				break;
			case "1007315":
				applicationName=LinMobileConstants.TOPIX_DFP_APPLICATION_NAME;
				email=LinMobileConstants.TOPIX_DFP_EMAIL_ADDRESS;
				password=LinMobileConstants.TOPIX_DFP_EMAIL_PASSWORD;
				clientLogin=true;
				break;
			case "12008447":
				applicationName="MediaAgility";
				email="mediaad2012sense@gmail.com";
				password="maadsense2012";
				break;	
			default:
				throw new Exception("Not a valid DFP account.. networkCode:"+networkCode);
				
		}
		DfpSession dfpSession=null;
		if(clientLogin){
			log.info("Going to build dfpSession using client login ... for dfp : "+networkCode);	
			dfpSession = getDFPSessionUsingClientLogin(networkCode, email, password, applicationName);
		}else{
			log.info("Going to build dfpSession using service account ... for dfp : "+networkCode);	
			dfpSession = getDFPSession(networkCode, applicationName);
		}
		
		return dfpSession;
	}
	
	public static DfpSession getDFPSessionUsingClientLogin(String dfpNetworkCode, String email, 
			String password,String applicationName) throws ValidationException{
		String clientLoginToken = ReportUtil.regenerateClientAuthToken(dfpNetworkCode, email,password);
    	
    	DfpSession	dfpSession = new DfpSession.Builder()
				.withClientLoginToken(clientLoginToken)					
				.withNetworkCode(dfpNetworkCode)
				.withApplicationName(applicationName)
				.build();
    	
    	return dfpSession;
	}
	
}