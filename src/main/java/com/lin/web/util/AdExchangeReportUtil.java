package com.lin.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.RootReportObj;

public class AdExchangeReportUtil {
	private static final Logger log = Logger.getLogger(AdExchangeReportUtil.class.getName());
	
	
	public static String getAdExRepors(String startDate,String endDate,String accessToken) {		
		
		String adExWebServiceReportURL="https://www.googleapis.com/adexchangeseller/v1/reports?" +
				"startDate="+startDate +
				"&endDate=" +endDate+
				"&dimension=DATE" +
				"&dimension=AD_CLIENT_ID" +
				"&dimension=AD_UNIT_ID" +
				"&dimension=AD_UNIT_NAME" +
				"&dimension=ADVERTISER_NAME" +				
				//"&dimension=AD_FORMAT_NAME" +
				"&dimension=AD_UNIT_SIZE_NAME" +
				"&metric=AD_REQUESTS_CTR" +
				"&metric=AD_REQUESTS_RPM" +
				"&metric=AD_REQUESTS" +
				"&metric=INDIVIDUAL_AD_IMPRESSIONS" +
				//"&metric=AD_REQUESTS_COVERAGE" +
				"&metric=CLICKS" +
				"&metric=COST_PER_CLICK" +
				"&metric=EARNINGS" +				
				//"&metric=INDIVIDUAL_AD_IMPRESSIONS_CTR" +
				//"&metric=INDIVIDUAL_AD_IMPRESSIONS_RPM" +
				//"&metric=MATCHED_AD_REQUESTS_CTR" +
				//"&metric=MATCHED_AD_REQUESTS_RPM" +
				//"&metric=MATCHED_AD_REQUESTS"+
				"&sort=%2BDATE" +
				//"&alt=csv"+
				"&access_token="+accessToken;
		
		StringBuffer dataBuffer=getWebServiceResponse(adExWebServiceReportURL);		
		return dataBuffer.toString();
	}
	
	
	public static List<CorePerformanceReportObj> createAdExCorePerformanceReportData(String jsonResponse) throws Exception{
		List<CorePerformanceReportObj> rootDataList=new ArrayList<CorePerformanceReportObj>();
		CorePerformanceReportObj rootObj;		
		JSONObject nexageSiteDetails = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		String totalMatchedRows=null;
		String errMsg=null;
		if(nexageSiteDetails.has("totalMatchedRows")){
			totalMatchedRows=nexageSiteDetails.getString("totalMatchedRows");
			
			String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			if(totalMatchedRows !=null && totalMatchedRows.equals("0")){
				log.info("No records found for this date range, totalMatchedRows:"+totalMatchedRows);
			}else{
				log.info("totalMatchedRows:"+totalMatchedRows);
				JSONArray jsonArray=nexageSiteDetails.getJSONArray("rows");
		        log.info("jsonArray.size():"+jsonArray.size());
		        if(jsonArray.size()==0){
		        	log.warning("No data found, our API failed to get data from Adex, please check log.");        	
		        }else{        	
		        	for(int i=0;i<jsonArray.size();i++){
		        		JSONArray subArray=jsonArray.getJSONArray(i);
		    			//log.info("json subArray:"+subArray.size());
		    			if(subArray.size() >=12){
		    				String DATE=subArray.getString(0);
		    				String AD_CLIENT_ID=subArray.getString(1);
		    				String AD_UNIT_ID=subArray.getString(2);
		    				String AD_UNIT_NAME=subArray.getString(3);
		    				//String AD_FORMAT_NAME=subArray.getString(4);
		    				String ADVERTISER_NAME=subArray.getString(4); 
		    				String AD_UNIT_SIZE_NAME=subArray.getString(5);    				   				
		    				String AD_REQUESTS_CTR=subArray.getString(6);
		    				String AD_REQUESTS_RPM=subArray.getString(7);
		    				String AD_REQUESTS=subArray.getString(8);
		    				String INDIVIDUAL_AD_IMPRESSIONS=subArray.getString(9);
		    				String CLICKS=subArray.getString(10);
		    				String COST_PER_CLICK=subArray.getString(11);    				
		    				String EARNINGS=subArray.getString(12);  
		    				
		    				double revenue=StringUtil.getDoubleValue(EARNINGS,6);
		    				long requests=StringUtil.getLongValue(AD_REQUESTS);    					
		    				long clicked=StringUtil.getLongValue(CLICKS);
		    				//double revenueCPC=StringUtil.getDoubleValue(COST_PER_CLICK);
		    				long totalImpressions=StringUtil.getLongValue(INDIVIDUAL_AD_IMPRESSIONS);
		    				double RPM=StringUtil.getDoubleValue(AD_REQUESTS_RPM);
		    				double CTR=StringUtil.getDoubleValue(AD_REQUESTS_CTR,4)*100.0;
		    				
		    				rootObj=new CorePerformanceReportObj();
		    			
		    				if(AD_UNIT_NAME !=null){
		    					AD_UNIT_NAME=AD_UNIT_NAME.replace(",", ":");
		    				}
		    				rootObj.setSiteName(AD_UNIT_NAME);
		    				rootObj.setSiteId(AD_UNIT_ID);    	
		    				if(ADVERTISER_NAME !=null){
		    					ADVERTISER_NAME=ADVERTISER_NAME.replace(",", ":");
		    				}
		    				rootObj.setAdvertiser(ADVERTISER_NAME);    				
		    				rootObj.setDate(DateUtil.getFormatedDate(DATE, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
		    				if(AD_UNIT_NAME.contains("MobileWeb") || AD_UNIT_NAME.contains("Tablet")){
								rootObj.setSiteType("mobilesite");
								rootObj.setSiteTypeId("51933462");
							}else{
								rootObj.setSiteType("mobileapp");
								rootObj.setSiteTypeId("51932502");
							}
		    				
		    				rootObj.setCreativeSize(AD_UNIT_SIZE_NAME);
		    				
		    				rootObj.setTotalClicks(clicked);
		    				rootObj.setClicksCPC(0);
		    				rootObj.setClicksCPM(clicked);
		    					
		    				rootObj.setLoadTimestamp(loadTimestamp);
		    				rootObj.setCTR(CTR);
		    				rootObj.setECPM(ReportUtil.calculateECPM(revenue, totalImpressions));
							rootObj.setRPM(RPM);
							
		    				rootObj.setFillRate(ReportUtil.calculateFillRate(totalImpressions, requests));
		    				rootObj.setTotalImpressions(totalImpressions);
		    				rootObj.setImpressionsCPC(0);
		    				rootObj.setImpressionsCPM(totalImpressions);    				
		    				
		    				rootObj.setRequests(requests);
		    				rootObj.setTotalRevenue(revenue);
		    				//rootObj.setRevenueCPC(revenueCPC);
		    				rootObj.setRevenueCPM(0);
		    				rootObj.setRevenueCPD(0);
		    				
		    				rootObj.setDataSource(LinMobileConstants.AD_EXCHANGE_DATA_SOURCE);
		    				rootObj.setChannelId(Long.parseLong(LinMobileConstants.AD_EXCHANGE_CHANNEL_ID));
		    				rootObj.setChannelName(LinMobileConstants.AD_EXCHANGE_CHANNEL_NAME);
		    				rootObj.setChannelType(LinMobileConstants.AD_EXCHANGE_CHANNEL_TYPE);
		    				
		    				rootObj.setPublisherId(Long.parseLong(LinMobileConstants.AD_EXCHANGE_PUBLISHER_ID));
		    				rootObj.setPublisherName(LinMobileConstants.AD_EXCHANGE_PUBLISHER_NAME);
		    				
		    				rootObj.setSalesType(LinMobileConstants.AD_EXCHANGE_SALES_TYPE);    					
		    				
		    				rootObj.setPassback(ReportUtil.getPassback(rootObj));
							rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
							
		    				rootDataList.add(rootObj);
		    			}
		        	}		
		        }		
			}
	        
		}else if(nexageSiteDetails.has("error")){
			errMsg=nexageSiteDetails.getString("error");
			log.severe("Google ad ex, Error:"+errMsg);
			throw new Exception(errMsg);
		}else{
			log.severe("Google ad ex, invalid response::"+jsonResponse);
			throw new Exception(jsonResponse);
		}		

		return rootDataList;
	}
	
	public static String getAdExReporsInCSV(String startDate,String endDate,String accessToken) {		
	    String adExWebServiceReportURL="https://www.googleapis.com/adexchangeseller/v1/reports?" +
				"startDate="+startDate +
				"&endDate=" +endDate+
				"&dimension=DATE" +
				"&dimension=AD_CLIENT_ID" +
				"&dimension=AD_UNIT_ID" +
				"&dimension=AD_UNIT_NAME" +
				"&dimension=AD_FORMAT_NAME" +
				"&dimension=AD_UNIT_SIZE_NAME" +
				"&dimension=ADVERTISER_NAME" +	
				"&metric=AD_REQUESTS_CTR" +
				"&metric=AD_REQUESTS_RPM" +
				"&metric=AD_REQUESTS" +
				"&metric=INDIVIDUAL_AD_IMPRESSIONS" +
				"&metric=CLICKS" +
				"&metric=COST_PER_CLICK" +
				"&metric=EARNINGS" +	
				"&sort=%2BDATE" +
				"&alt=csv"+
				"&access_token="+accessToken;
	    log.info("adExWebServiceReportURL:"+adExWebServiceReportURL);
		StringBuffer dataBuffer=getWebServiceResponseInCSV(adExWebServiceReportURL);		
		return dataBuffer.toString();
	}
	
	public static String getWebServiceResponseUsingURLFetch(String webServerURL) {
		String response=null;
		OutputStream output = null;
		try {     
			
			//String request = "https://graph.facebook.com/194652357227159?access_token=Aj2h4df...";
			URL url = new URL(webServerURL);
			URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();			
			HTTPResponse httpResponse =  urlFetchService.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotValidateCertificate()));
			response=new String(httpResponse.getContent());
			log.info("resp:"+httpResponse.getResponseCode()+" \n content : "+response);
		} catch (UnsupportedEncodingException e) {
			log.severe("UnsupportedEncodingException:" + e.getMessage());
			
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException logOrIgnore) {
				}
		}
		return response;
	}
	
	public static StringBuffer getWebServiceResponse(String webServerURL) {
		log.info("Going to read webServerURL:"+webServerURL);
		StringBuffer dataBuffer = new StringBuffer();
		try {
			URL url = new URL(webServerURL);
			HttpURLConnection connection =  (HttpURLConnection) new URL(webServerURL).openConnection(); // For AppEngine
		
			connection.setConnectTimeout(50000);  
			connection.setReadTimeout(50000);  //50 Seconds
			connection.setRequestMethod("GET");
		
			InputStream response = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response));
			//BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				dataBuffer.append(line);
				line = reader.readLine();
			}
			log.info("Reading response in buffer done...");
			reader.close();

		} catch (MalformedURLException e) {
			log.severe("MalformedURLException :" + e.getMessage());
			
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}catch(Exception e){
			log.severe("Exception:" + e.getMessage());
			
		}
		return dataBuffer;
	}
	
	public static StringBuffer getWebServiceResponseInCSV(String webServerURL) {
		StringBuffer dataBuffer = new StringBuffer();		
		try {            
			    URL url = new URL(webServerURL);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	dataBuffer.append(line+"\n");	            	
	            }
	            //log.info("dataBuffer:"+dataBuffer.toString());
	            reader.close();

		} catch (MalformedURLException  e) {
			log.severe("MalformedURLException :" + e.getMessage());
			
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		} 
		return dataBuffer;
	}
	public static void main(String arg[]){
		//getAdExClients();
	}

}
