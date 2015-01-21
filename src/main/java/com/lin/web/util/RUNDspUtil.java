package com.lin.web.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RUNDspUtil {
	
	public static String token = "\"5455fc39d4d756764e1c6b33a3eee90b241a14a54eb476d641cf7ab2dd6f19cf\"";
	
	public static String campaignURL = "http://portal.rundsp.com/api/v1/campaigns";
	
	private static final Logger log = Logger.getLogger(RUNDspUtil.class.getName());
	
	public static String getCampaignDetailURL(String campaignID){
		return campaignURL + "/" + campaignID;
	}
	
	public static String getCampaignDetailByDayURL(String campaignID,String startDate,String endDate){
		return campaignURL + "/" + campaignID + "/days/"+ startDate +"/"+endDate ;
	}
	
	/*
	 * author : Anup
	 * description : Method to call 3rd Party URL
	 * */
	
	public static String getResponse(String url){
		String response = null;
		try{
			Map<String ,String> header = new HashMap<String, String>();
			header.put("Authorization", "Token token=" + token);
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//Set Request Method
			con.setRequestMethod("GET");
			
			//Set Header
			for(String key : header.keySet()){
				con.setRequestProperty(key,header.get(key).toString());
			}
			
			int responseCode = con.getResponseCode();
			log.info("\nSending 'GET' request to URL : " + url);
			log.info("Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseBuff = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				responseBuff.append(inputLine);
			}
			in.close();
			response = responseBuff.toString();
			
		}catch(Exception e){
			log.severe("Expetion in getResponse : " + e.getMessage());
		}
		return response;
	}
}
