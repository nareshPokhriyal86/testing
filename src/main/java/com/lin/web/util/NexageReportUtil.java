package com.lin.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.NexageReport;
import com.lin.web.service.impl.GAEConnectionManager;



public class NexageReportUtil {

	private static final Logger log = Logger.getLogger(NexageReportUtil.class.getName());
	
	/*
	 * Get reports from Nexage API
	 * @param startDate, endDate in YYYY-MM-DD format
	 */
	public static String getReportsByURL(String startDate,String endDate,String dim){
		String reportId="sellernetrevenuesummary";
		String reportsAPIURL=null;
		if(dim !=null){
			reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim;
		}else{
			reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate;            
		}
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && response.toString().trim().length()>0){
				break;
			}else{
				response =getWebServiceResponse(reportsAPIURL);
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;
    }  
	
	
	public static String getReportsBySiteId(String startDate,String endDate,String dim,String siteId){
		log.info("get report for site: siteId:"+siteId);
		String reportId="sellernetrevenuesummary";
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim
            +"&site="+siteId;
				
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;
    }
	
	public static String getReportsBySiteIdNewAPI(String startDate,String endDate,String dim,String siteId){
		log.info("get report for site: siteId:"+siteId);
		String reportId="sellerrevenue";
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim
            +"&site="+siteId;
				
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;
    }
	
	
	/*
	 * Get reports from Nexage API
	 * @param startDate, endDate in YYYY-MM-DD format
	 */
	public static String getReportsBySite(String startDate,String endDate){		
		String reportId=LinMobileConstants.NEXAGE_NET_REVENUE_SUMMARY_REPORT_ID; //"sellernetrevenuesummary";
		String dim="site";
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim;
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;	
    }
	
	public static String getReportsBySiteNewAPI(String startDate,String endDate){		
		String reportId="sellerrevenue";
		String dim="site";
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim;
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;	
    }
	
	public static String getNexageReportsByDim(String startDate,String endDate,String reportID,String dimension){		
		String reportId=reportID;
		String dim=dimension;
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim;
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;	
    }
	
	public static String getNexageReportsByDimId(String startDate,String endDate,String reportID,String dimension,String dimensionId){
		log.info("get report for dimensionId:"+dimensionId);
		String reportId=reportID;
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+"day"
            +"&"+dimension+"="+dimensionId;
				
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;
    }
	
	public static String getNexageReportsByDimId(String startDate,String endDate,String reportID,String dim1, String dimId1,String dim2, String dimId2){
		log.info("get report for reportId:"+reportID);
		log.info("get report for dimension1:"+dim1);
		log.info("get report for dimension2:"+dim2);
		log.info("get report for dimensionId1:"+dimId1);
		log.info("get report for dimensionId2:"+dimId2);
		String reportId=reportID;
		
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+"day"
            +"&"+dim1+"="+dimId1
            +"&"+dim2+"="+dimId2;
				
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;
    }
	
	public static List<String> getIdList(String startDate,String endDate,String reportID,String dimension){
		List<String> idList = new ArrayList<String>();
		String reportId=reportID;
		String dim=dimension;
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
            +reportId
            +"?"
            +"start="+startDate
            +"&stop="+endDate
            +"&dim="+dim;
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		

			if(response !=null && (!response.contains("IOException"))){
				JSONObject nexageSiteDetails = (JSONObject) JSONSerializer.toJSON(response);			
		        JSONArray jsonArray=nexageSiteDetails.getJSONArray("detail");
		        if(jsonArray.size() > 0)
		        {
		        	for(int i=0;i<jsonArray.size();i++)
		        	{
		    			JSONObject obj=jsonArray.getJSONObject(i);
		    			String dimId=obj.getString(dim+"Id");
		    			String dimName=obj.getString(dim);
		    			idList.add(dimId+":"+dimName);
		        	}
		        }
			
			}
		
			return idList;
		
    }
	/*
	 * Get reports from Nexage API
	 * @param startDate, endDate in YYYY-MM-DD format
	 */
	public static String getReports(String startDate,String endDate){		
		String reportId="sellernetrevenuesummary";
		String reportsAPIURL=LinMobileConstants.NEXAGE_REPORTS_API_URL
		                     +reportId
		                     +"?"
		                     +"start="+startDate
		                     +"&stop="+endDate;
		
		String response=getWebServiceResponse(reportsAPIURL);
		int count=0;
		while(count <=3){
			if(response !=null && (!response.contains("IOException")) ){
				break;
			}else if(response !=null && (response.contains("IOException")) ){
				response =getWebServiceResponse(reportsAPIURL);
			}else{
				break;
			}
			count ++;
		}
		log.info("Max try to get response from nexage report api:"+count);
		
		return response;		
    }  
	
	public static String getWebServiceResponse(String reportsAPIURL){
		System.out.println("reportsAPIURL:"+reportsAPIURL);
		String responseBody=null;
		HttpParams httpParams = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
		//HttpConnectionParams.setSoTimeout(httpParams, 50000);
		ClientConnectionManager connectionManager = new GAEConnectionManager();
		HttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
	    String request=null;   
		 try {        	
	        	((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(
	                    new AuthScope("reports.nexage.com", 443,"api.nexage.com"),
	                    new UsernamePasswordCredentials(LinMobileConstants.NEXAGE_COMPNAY_ACCESS_KEY, 
	                    		LinMobileConstants.NEXAGE_COMPNAY_SECRET_KEY));

	            HttpGet httpget = new HttpGet(reportsAPIURL);
	            request= httpget.getRequestLine()+"";        
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseBody = httpClient.execute(httpget, responseHandler);            
	            //log.info(responseBody);                      
	        } catch (IOException e) {
	        	responseBody="IOException:"+e.getMessage()+" with responseBody:"+responseBody;
				log.severe("IOException :"+e.getMessage()+" and request:"+request);
				//e.printStackTrace();
			} 
			return responseBody;
	}
	
	
	public static List<CorePerformanceReportObj> createNexageCorePerformanceReportData(String jsonResponse,String siteId,String siteName){
		log.info("creating core performance report for siteId:"+siteId);
		List<CorePerformanceReportObj> rootDataList=new ArrayList<CorePerformanceReportObj>();
		CorePerformanceReportObj rootObj;
		String timestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		JSONObject nexageReportResponse = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray jsonArray=nexageReportResponse.getJSONArray("detail");
		
		for(int i=0;i<jsonArray.size();i++){
			    rootObj=new CorePerformanceReportObj();
				JSONObject obj=jsonArray.getJSONObject(i);
				String day=obj.getString("day");
				double revenue=StringUtil.getDoubleValue(obj.getString("revenue"),8);
				long requests=StringUtil.getLongValue(obj.getString("requests"));
				long served=StringUtil.getLongValue(obj.getString("served"));
				long delivered=StringUtil.getLongValue(obj.getString("delivered"));
				long clicked=StringUtil.getLongValue(obj.getString("clicked"));
				
				rootObj.setChannelId(Long.parseLong(LinMobileConstants.NEXAGE_CHANNEL_ID));
				rootObj.setChannelName(LinMobileConstants.NEXAGE_CHANNEL_NAME);
				//rootObj.setAdSource(null);
				rootObj.setChannelType(LinMobileConstants.NEXAGE_CHANNEL_TYPE);
				
				rootObj.setTotalClicks(clicked);
				rootObj.setClicksCPC(0);
				rootObj.setClicksCPM(clicked);
				
				rootObj.setLoadTimestamp(timestamp);
				rootObj.setCTR(ReportUtil.calculateCTR(clicked, served));
				rootObj.setDate(DateUtil.getFormatedDate(day, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
				rootObj.setECPM(ReportUtil.calculateECPM(revenue, served));
				rootObj.setFillRate(ReportUtil.calculateFillRate(served, requests));
				rootObj.setTotalImpressions(served);
				rootObj.setImpressionsCPC(0);
				rootObj.setImpressionsCPM(served);
				
				rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.NEXAGE_PUBLISHER_ID));
				rootObj.setPublisherName(LinMobileConstants.NEXAGE_PUBLISHER_NAME);
				rootObj.setRequests(requests);
				
				rootObj.setTotalRevenue(revenue);				
				rootObj.setRevenueCPC(0);
				rootObj.setRevenueCPM(revenue);
				rootObj.setRevenueCPD(0);
				
				
				rootObj.setRPM(ReportUtil.calculateRPM(revenue, requests));
				
				rootObj.setSalesType(LinMobileConstants.NEXAGE_SALES_TYPE);
				rootObj.setServed(served);
				rootObj.setSiteId(siteId);
				rootObj.setSiteName(siteName);
				rootObj.setDataSource("Nexage");
				if(siteName.contains("MobileWeb") || siteName.contains("Tablet")){
					rootObj.setSiteType("mobilesite");
					rootObj.setSiteTypeId("51933462");
				}else{
					rootObj.setSiteType("mobileapp");
					rootObj.setSiteTypeId("51932502");
				}
				rootObj.setPassback(ReportUtil.getPassback(rootObj));
				rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
				rootDataList.add(rootObj);
		}
		return rootDataList;
	}
	
	
	public static List<CorePerformanceReportObj> createNexageCorePerformanceReportDataNewAPI(String jsonResponse,String siteId,String siteName,String sourceId,String sourceName){
		log.info("creating core performance report for siteId:"+siteId);
		List<CorePerformanceReportObj> rootDataList=new ArrayList<CorePerformanceReportObj>();
		CorePerformanceReportObj rootObj;
		String timestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		JSONObject nexageReportResponse = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray jsonArray=nexageReportResponse.getJSONArray("detail");
		
		for(int i=0;i<jsonArray.size();i++){
			    rootObj=new CorePerformanceReportObj();
				JSONObject obj=jsonArray.getJSONObject(i);
				String day=obj.getString("day");
				double revenue=StringUtil.getDoubleValue(obj.getString("revenue"),8);
				long requests=StringUtil.getLongValue(obj.getString("requests"));
				long served=StringUtil.getLongValue(obj.getString("served"));
				long delivered=StringUtil.getLongValue(obj.getString("delivered"));
				long clicked=StringUtil.getLongValue(obj.getString("clicked"));
				
				rootObj.setChannelId(Long.parseLong(LinMobileConstants.NEXAGE_CHANNEL_ID));
				rootObj.setChannelName(LinMobileConstants.NEXAGE_CHANNEL_NAME);
				//rootObj.setAdSource(null);
				rootObj.setChannelType(LinMobileConstants.NEXAGE_CHANNEL_TYPE);
				
				rootObj.setTotalClicks(clicked);
				rootObj.setClicksCPC(0);
				rootObj.setClicksCPM(clicked);
				
				rootObj.setLoadTimestamp(timestamp);
				rootObj.setCTR(ReportUtil.calculateCTR(clicked, served));
				rootObj.setDate(DateUtil.getFormatedDate(day, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
				rootObj.setECPM(ReportUtil.calculateECPM(revenue, served));
				rootObj.setFillRate(ReportUtil.calculateFillRate(served, requests));
				rootObj.setTotalImpressions(served);
				rootObj.setImpressionsCPC(0);
				rootObj.setImpressionsCPM(served);
				
				rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.NEXAGE_PUBLISHER_ID));
				rootObj.setPublisherName(LinMobileConstants.NEXAGE_PUBLISHER_NAME);
				rootObj.setRequests(requests);
				
				rootObj.setTotalRevenue(revenue);				
				rootObj.setRevenueCPC(0);
				rootObj.setRevenueCPM(revenue);
				rootObj.setRevenueCPD(0);
				
				
				rootObj.setRPM(ReportUtil.calculateRPM(revenue, requests));
				
				rootObj.setSalesType(LinMobileConstants.NEXAGE_SALES_TYPE);
				rootObj.setServed(served);
				rootObj.setSiteId(siteId);
				rootObj.setSiteName(siteName);
				rootObj.setAdSource(sourceName);
				rootObj.setDataSource("Nexage");
				if(siteName.contains("MobileWeb") || siteName.contains("Tablet")){
					rootObj.setSiteType("mobilesite");
					rootObj.setSiteTypeId("51933462");
				}else{
					rootObj.setSiteType("mobileapp");
					rootObj.setSiteTypeId("51932502");
				}
				rootObj.setPassback(ReportUtil.getPassback(rootObj));
				rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
				rootDataList.add(rootObj);
		}
		return rootDataList;
	}
	
	public static StringBuffer createNexageCSVReport (List<NexageReport> dataList) {		
    	
		  StringBuffer strBuffer = new StringBuffer();
		  strBuffer.append("Load_Timestamp");
		  strBuffer.append(",");
		  strBuffer.append("Channel_ID");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Name");
		  strBuffer.append(",");
		  strBuffer.append("Channel_Type");
		  strBuffer.append(",");
		  strBuffer.append("Sales_Type");
		  strBuffer.append(",");
		  strBuffer.append("Pubisher_ID");
		  strBuffer.append(",");
		  strBuffer.append("Publisher_Name");
		  strBuffer.append(",");
		  strBuffer.append("Site_ID");
		  strBuffer.append(",");
		  strBuffer.append("Site_Name");
		  strBuffer.append(",");
		  strBuffer.append("Ad_Source");
		  strBuffer.append(",");
		  strBuffer.append("Date");
		  strBuffer.append(",");
		  strBuffer.append("Requests");
		  strBuffer.append(",");
		  strBuffer.append("Served");
		  strBuffer.append(",");
		  strBuffer.append("Total_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("CPM_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("CPC_Impressions");
		  strBuffer.append(",");
		  strBuffer.append("Total_Clicks");
		  strBuffer.append(",");
		  strBuffer.append("CPM_Clicks");
		  strBuffer.append(",");
		  strBuffer.append("CPC_Clicks");
		  strBuffer.append(",");
		  strBuffer.append("Total_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("CPM_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("CPC_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("CPD_Revenue");
		  strBuffer.append(",");
		  strBuffer.append("Fill_Rate");
		  strBuffer.append(",");
		  strBuffer.append("CTR");
		  strBuffer.append(",");
		  strBuffer.append("eCPM");
		  strBuffer.append(",");
		  strBuffer.append("RPM");
		  
		  strBuffer.append('\n');
			
	      for(NexageReport obj:dataList){
	    	  strBuffer.append(obj.getCreatedOn());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getChannelType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSalesType());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getPublisherName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteId());
			  strBuffer.append(",");
			  strBuffer.append(obj.getSiteName());
			  strBuffer.append(",");
			  strBuffer.append(obj.getAdSource());
			  strBuffer.append(",");
			  strBuffer.append(obj.getDate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRequests());
			  strBuffer.append(",");
			  strBuffer.append(obj.getServed());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalImpressions());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getImpressionsCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalClicks());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getClicksCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getTotalRevenue());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRevenueCPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRevenueCPC());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRevenueCPD());
			  strBuffer.append(",");
			  strBuffer.append(obj.getFillRate());
			  strBuffer.append(",");
			  strBuffer.append(obj.getCTR());
			  strBuffer.append(",");
			  strBuffer.append(obj.getECPM());
			  strBuffer.append(",");
			  strBuffer.append(obj.getRPM());
			  strBuffer.append('\n');
	      }
	      return strBuffer;  
	}
	
	public static List<NexageReport> parseJSONResponseFromNexage(String jsonResponse,String siteId,String siteName){
		JSONObject nexageReportResponse = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray jsonArray=nexageReportResponse.getJSONArray("detail");
		List<NexageReport> reportObjList=new ArrayList<NexageReport>();
		NexageReport reportObj;
		String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		for(int i=0;i<jsonArray.size();i++){
			    reportObj=new NexageReport();
				JSONObject obj=jsonArray.getJSONObject(i);
				String day=obj.getString("day");
				double revenue=StringUtil.getDoubleValue(obj.getString("revenue"),8);
				long requests=StringUtil.getLongValue(obj.getString("requests"));
				long served=StringUtil.getLongValue(obj.getString("served"));
				long delivered=StringUtil.getLongValue(obj.getString("delivered"));
				long clicked=StringUtil.getLongValue(obj.getString("clicked"));
				
				reportObj.setChannelId(Long.parseLong(LinMobileConstants.NEXAGE_CHANNEL_ID));
				reportObj.setChannelName(LinMobileConstants.NEXAGE_CHANNEL_NAME);
				//reportObj.setAdSource(null);
				reportObj.setChannelType(LinMobileConstants.NEXAGE_CHANNEL_TYPE);
				
				reportObj.setTotalClicks(clicked);
				reportObj.setClicksCPC(0);
				reportObj.setClicksCPM(clicked);
				
				reportObj.setCreatedOn(createdOn);
				reportObj.setCTR(ReportUtil.calculateCTR(clicked, served));
				reportObj.setDate(DateUtil.getFormatedDate(day, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
				//reportObj.setDate(day);
				reportObj.setECPM(ReportUtil.calculateECPM(revenue, served));
				reportObj.setFillRate(ReportUtil.calculateFillRate(served, requests));
				reportObj.setTotalImpressions(served);
				reportObj.setImpressionsCPC(0);
				reportObj.setImpressionsCPM(served);
				
				reportObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.NEXAGE_PUBLISHER_ID));
				reportObj.setPublisherName(LinMobileConstants.NEXAGE_PUBLISHER_NAME);
				reportObj.setRequests(requests);
				
				reportObj.setTotalRevenue(revenue);				
				reportObj.setRevenueCPC(0);
				reportObj.setRevenueCPM(revenue);
				reportObj.setRevenueCPD(0);
				
				
				reportObj.setRPM(ReportUtil.calculateRPM(revenue, requests));
				
				reportObj.setSalesType(LinMobileConstants.NEXAGE_SALES_TYPE);
				reportObj.setServed(served);
				reportObj.setSiteId(siteId);
				reportObj.setSiteName(siteName);
				reportObjList.add(reportObj);
		}
		return reportObjList;
	}
    public static void main(String[] args)  {    	
    	//String response=getReports("2013-05-13","2013-05-14");
    	String response=getReportsByURL("2013-05-13","2013-05-14",null);
    	System.out.println("response: "+response);
    }
}
