package com.lin.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.web.dto.MojivaReportDTO;
import com.lin.web.dto.MojiveReportWebServiceResponse;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wappworks.xstream.XStreamGae;

public class MojivaReportUtil {

	private static final Logger log = Logger.getLogger(MojivaReportUtil.class.getName());
	private static final String MOJIVA_SITE_METHOD="getSiteId";
	private static final String MOJIVA_SITE_BY_DATE_METHOD="getSiteInfoByDate";
	private static final String MOJIVA_SITE_BY_RANGE_METHOD="getSiteInfoByRange";
	
	
	public static MojiveReportWebServiceResponse getMojivaSiteReportByDateInterval(String date,String endDate,String siteId) {
		log.info("getMojivaSiteReportByDateInterval....by date:"+date+" and siteId:"+siteId);

		StringBuffer dataBuffer = new StringBuffer();
		String mojivaWebServiceURL = LinMobileConstants.MOJIVA_REST_SERVER_URL
		                              +"?method="+MOJIVA_SITE_BY_RANGE_METHOD
		                              +"&privatekey="+LinMobileConstants.MOJIVA_REST_SERVER_PRIVATE_KEY
		                              +"&date="+date
		                              +"&enddate="+endDate
		                              +"&site_id="+siteId;
		
		dataBuffer = getWebServiceResponse(mojivaWebServiceURL);
		return parseXMLResponse(dataBuffer);
	}
	
	public static MojiveReportWebServiceResponse getMojivaSiteReportByDate(String date,String siteId) {
		log.info("getMojivaSiteReportByDate....by date:"+date+" and siteId:"+siteId);

		StringBuffer dataBuffer = new StringBuffer();
		String mojivaWebServiceURL = LinMobileConstants.MOJIVA_REST_SERVER_URL
		                              +"?method="+MOJIVA_SITE_BY_DATE_METHOD
		                              +"&privatekey="+LinMobileConstants.MOJIVA_REST_SERVER_PRIVATE_KEY
		                              +"&date="+date
		                              +"&site_id="+siteId;
		
		dataBuffer = getWebServiceResponse(mojivaWebServiceURL);
		
		int count=0;
		while(count <=3){
			if(dataBuffer.toString() !=null && dataBuffer.toString().trim().length()>0){
				break;
			}else{
				dataBuffer = getWebServiceResponse(mojivaWebServiceURL);
			}
			count ++;
		}
		log.info("Max try to get response from mojiva report api:"+count);
		
		return parseXMLResponse(dataBuffer,date);
	}
	
	public static MojiveReportWebServiceResponse getAllMojivaSites() {
		log.info("getAllMojivaSites....");

		StringBuffer dataBuffer = new StringBuffer();
		String mojivaWebServiceURL = LinMobileConstants.MOJIVA_REST_SERVER_URL
		                              +"?method="+MOJIVA_SITE_METHOD
		                              +"&privatekey="+LinMobileConstants.MOJIVA_REST_SERVER_PRIVATE_KEY;
		
		dataBuffer = getWebServiceResponse(mojivaWebServiceURL);
		int count=0;
		while(count <=3){
			if(dataBuffer.toString() !=null && dataBuffer.toString().trim().length()>0){
				break;
			}else{
				dataBuffer = getWebServiceResponse(mojivaWebServiceURL);
			}
			count ++;
		}
		log.info("Max try to get response from mojiva report api:"+count);		
		
		return parseXMLResponse(dataBuffer);
	}

	public static StringBuffer getWebServiceResponse(String webServerURL) {
		StringBuffer dataBuffer = new StringBuffer();
		
		try {            
            HttpURLConnection connection;
			if (LinMobileConstants.PROXY_URL != null) {
				connection = (HttpURLConnection) new URL("http", LinMobileConstants.PROXY_URL, 3128, webServerURL).openConnection(); // For MA local machine
			} else {
				connection = (HttpURLConnection) new URL(webServerURL).openConnection(); // For AppEngine
			}
			connection.setConnectTimeout(50000);  //50 Seconds
			connection.setReadTimeout(50000);  //50 Seconds
			connection.setRequestMethod("GET");
			connection.setDoOutput(true); 
			connection.setRequestProperty("Content-Type", "text/xml");
			InputStream response = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response));
			String line;
			while ((line = reader.readLine()) != null) {
				dataBuffer.append(line);
			}
			reader.close();
			
		} catch (UnsupportedEncodingException e) {
			log.severe("UnsupportedEncodingException:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			e.printStackTrace();
		} 
		return dataBuffer;
	}
	
	
	private static MojiveReportWebServiceResponse parseMojivaResponse(String serviceResp){
		//XStream xstream = new XStream(new DomDriver());
		XStreamGae xstream = new XStreamGae(new DomDriver());
		xstream.alias("MojiveReportWebServiceResponse", MojiveReportWebServiceResponse.class);
		xstream.setClassLoader(MojiveReportWebServiceResponse.class.getClassLoader());
		MojiveReportWebServiceResponse mojivaResponse = (MojiveReportWebServiceResponse)xstream.fromXML(serviceResp);
		return mojivaResponse;
    }
	
	public static MojiveReportWebServiceResponse parseXMLResponse(StringBuffer xml) {
		
		List<MojivaReportDTO> resultList=new ArrayList<MojivaReportDTO>();
		MojivaReportDTO mojivaDTO=null;
		MojiveReportWebServiceResponse response = new MojiveReportWebServiceResponse();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new StringBufferInputStream(xml.toString());
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();		
			NodeList nodeLst = doc.getElementsByTagName("site");
			if(nodeLst.getLength()>0){
				//log.info("Mojiva xml response: site -->sub node length:"+nodeLst.getLength());
				for (int s = 0; s < nodeLst.getLength(); s++) {					
					Node nNode = nodeLst.item(s);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {			 
						Element eElement = (Element) nNode;	
						NodeList nodeList=eElement.getChildNodes();
						
						if(nodeList.getLength()==3){
							String domain=eElement.getElementsByTagName("domain").getLength()==0?null:eElement.getElementsByTagName("domain").item(0).getTextContent();
							String networkId=eElement.getElementsByTagName("network_id").getLength()==0?null:eElement.getElementsByTagName("network_id").item(0).getTextContent();
							String id=eElement.getElementsByTagName("id").getLength()==0?null:eElement.getElementsByTagName("id").item(0).getTextContent();
							mojivaDTO=new MojivaReportDTO(id, domain, networkId);
						}else if(nodeList.getLength()>0){
							String siteName=eElement.getElementsByTagName("name").getLength()==0?null:eElement.getElementsByTagName("name").item(0).getTextContent();
							String siteId=eElement.getElementsByTagName("site_id").getLength()==0?null:eElement.getElementsByTagName("site_id").item(0).getTextContent();
							String cpmImpressions=eElement.getElementsByTagName("cpm_impressions").getLength()==0?null:eElement.getElementsByTagName("cpm_impressions").item(0).getTextContent();
							String cpcImpressions=eElement.getElementsByTagName("cpc_impressions").getLength()==0?null:eElement.getElementsByTagName("cpc_impressions").item(0).getTextContent();
							String totalImpressions=eElement.getElementsByTagName("total_impressions").getLength()==0?null:eElement.getElementsByTagName("total_impressions").item(0).getTextContent();
							String requests=eElement.getElementsByTagName("requests").getLength()==0?null:eElement.getElementsByTagName("requests").item(0).getTextContent();
							String clicks=eElement.getElementsByTagName("clicks").getLength()==0?null:eElement.getElementsByTagName("clicks").item(0).getTextContent();
							String cpmRevenue=eElement.getElementsByTagName("cpm_revenue").getLength()==0?null:eElement.getElementsByTagName("cpm_revenue").item(0).getTextContent();
							String cpcRevenue=eElement.getElementsByTagName("cpc_revenue").getLength()==0?null:eElement.getElementsByTagName("cpc_revenue").item(0).getTextContent();
							String ctr=eElement.getElementsByTagName("ctr").getLength()==0?null:eElement.getElementsByTagName("ctr").item(0).getTextContent();
							String eCPM=eElement.getElementsByTagName("eCPM").getLength()==0?null:eElement.getElementsByTagName("eCPM").item(0).getTextContent();
							String totalRevenue=eElement.getElementsByTagName("total_revenue").getLength()==0?null:eElement.getElementsByTagName("total_revenue").item(0).getTextContent();
							if(siteId !=null){
								mojivaDTO=new MojivaReportDTO(
										siteId, siteName, cpmImpressions, cpcImpressions, 
										totalImpressions, requests, clicks, cpmRevenue, cpcRevenue, ctr, 
										eCPM, totalRevenue);
							}
						}else{
							log.severe("Please check, mojiva response has been changed.");							
						}
						if(mojivaDTO !=null){
							if( mojivaDTO.getDomain() !=null && mojivaDTO.getDomain().contains("_deleted_")){
							   log.info("Don't add this site, it is deactivated: siteId(Id): "+mojivaDTO.getId()+" and domain:"+mojivaDTO.getDomain());
						     }else{	
						    	 //log.info("mojivaDTO:"+mojivaDTO.toString());
						    	 resultList.add(mojivaDTO);	 
						     }						     					  
						}
						
					}
				}
				response.setSiteList(resultList);
				
			}else{
				nodeLst = doc.getElementsByTagName("login");				
				for (int s = 0; s < nodeLst.getLength(); s++) {					 
					Node nNode = nodeLst.item(s);			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {			 
						Element eElement = (Element) nNode;	
						String error=eElement.getElementsByTagName("error").getLength()==0?null:eElement.getElementsByTagName("error").item(0).getTextContent();
						response.setError(error);
					}
				}
				
			}			
		} catch (Exception e) {
			log.severe("MojiveReportWebServiceResponse:parseXMLResponse: Exception:"+ e.getMessage()+"\n response:"+xml.toString());
			response=null;
			e.printStackTrace();
		}		
		return response;
	}
	
	public static MojiveReportWebServiceResponse parseXMLResponse(StringBuffer xml,String date) {
		List<MojivaReportDTO> resultList=new ArrayList<MojivaReportDTO>();
		MojivaReportDTO mojivaDTO=null;
		MojiveReportWebServiceResponse response = new MojiveReportWebServiceResponse();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new StringBufferInputStream(xml.toString());
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();		
			NodeList nodeLst = doc.getElementsByTagName("site");
			if(nodeLst.getLength()>0){
				for (int s = 0; s < nodeLst.getLength(); s++) {					
					Node nNode = nodeLst.item(s);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {			 
						Element eElement = (Element) nNode;	
						NodeList nodeList=eElement.getChildNodes();
						//log.info("Mojiva xml response: site -->sub node length:"+nodeLst.getLength());
						if(nodeList.getLength()==3){
							String domain=eElement.getElementsByTagName("domain").getLength()==0?null:eElement.getElementsByTagName("domain").item(0).getTextContent();
							String networkId=eElement.getElementsByTagName("network_id").getLength()==0?null:eElement.getElementsByTagName("network_id").item(0).getTextContent();
							String id=eElement.getElementsByTagName("id").getLength()==0?null:eElement.getElementsByTagName("id").item(0).getTextContent();
							mojivaDTO=new MojivaReportDTO(id, domain, networkId);
						}else if(nodeList.getLength()>0){
							String siteName=eElement.getElementsByTagName("name").getLength()==0?null:eElement.getElementsByTagName("name").item(0).getTextContent();
							String siteId=eElement.getElementsByTagName("site_id").getLength()==0?null:eElement.getElementsByTagName("site_id").item(0).getTextContent();
							
							String cpmImpressions=eElement.getElementsByTagName("cpm_impressions").getLength()==0?null:eElement.getElementsByTagName("cpm_impressions").item(0).getTextContent();
							String cpcImpressions=eElement.getElementsByTagName("cpc_impressions").getLength()==0?null:eElement.getElementsByTagName("cpc_impressions").item(0).getTextContent();
			
							String totalImpressions=eElement.getElementsByTagName("total_impressions").getLength()==0?null:eElement.getElementsByTagName("total_impressions").item(0).getTextContent();
							String requests=eElement.getElementsByTagName("requests").getLength()==0?null:eElement.getElementsByTagName("requests").item(0).getTextContent();
							String clicks=eElement.getElementsByTagName("clicks").getLength()==0?null:eElement.getElementsByTagName("clicks").item(0).getTextContent();
							String cpmRevenue=eElement.getElementsByTagName("cpm_revenue").getLength()==0?null:eElement.getElementsByTagName("cpm_revenue").item(0).getTextContent();
							String cpcRevenue=eElement.getElementsByTagName("cpc_revenue").getLength()==0?null:eElement.getElementsByTagName("cpc_revenue").item(0).getTextContent();
							String ctr=eElement.getElementsByTagName("ctr").getLength()==0?null:eElement.getElementsByTagName("ctr").item(0).getTextContent();
							String eCPM=eElement.getElementsByTagName("eCPM").getLength()==0?null:eElement.getElementsByTagName("eCPM").item(0).getTextContent();
							String totalRevenue=eElement.getElementsByTagName("total_revenue").getLength()==0?null:eElement.getElementsByTagName("total_revenue").item(0).getTextContent();
							if(siteId !=null){
								mojivaDTO=new MojivaReportDTO(
										siteId, siteName, cpmImpressions, cpcImpressions, 
										totalImpressions, requests, clicks, cpmRevenue, cpcRevenue, ctr, 
										eCPM, totalRevenue,date);
							}
							
						}else{
							log.severe("Please check, mojiva response has been changed.");							
						}
						if(mojivaDTO !=null){
							if( mojivaDTO.getDomain() !=null && mojivaDTO.getDomain().contains("_deleted_")){
							   log.info("Don't add this site, it is deactivated: siteId(Id): "+mojivaDTO.getId()+" and domain:"+mojivaDTO.getDomain());
						     }else{
						    	 //log.info("mojivaDTO:"+mojivaDTO.toString());
						    	 resultList.add(mojivaDTO);	 
						     }						     					  
						}
						
					}
				}
				response.setSiteList(resultList);
				
			}else{
				nodeLst = doc.getElementsByTagName("login");				
				for (int s = 0; s < nodeLst.getLength(); s++) {					 
					Node nNode = nodeLst.item(s);			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {			 
						Element eElement = (Element) nNode;	
						String error=eElement.getElementsByTagName("error").getLength()==0?null:eElement.getElementsByTagName("error").item(0).getTextContent();
						response.setError(error);
					}
				}
				
			}			
		} catch (Exception e) {
			log.severe("MojiveReportWebServiceResponse:parseXMLResponse: Exception:"+ e.getMessage()+"\n response:"+xml.toString());
			response=null;
			e.printStackTrace();
		}		
		return response;
	}
	
	public static List<CorePerformanceReportObj> createMojivaCorePerformanceReportData(List<MojivaReportDTO> resultList,String date){
		List<CorePerformanceReportObj> rootDataList=new ArrayList<CorePerformanceReportObj>();
		CorePerformanceReportObj rootObj;
		String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		for(MojivaReportDTO mojivaDTO:resultList){
			//log.info("Add in report :"+mojivaDTO.toString());
			if(mojivaDTO !=null && mojivaDTO.getSiteId() !=null){
				rootObj=new CorePerformanceReportObj();			
				long clicks=StringUtil.getLongValue(mojivaDTO.getClicks());
				double totalRevenue=StringUtil.getDoubleValue(mojivaDTO.getTotalRevenue(),6);
				double cpcRevenue=StringUtil.getDoubleValue(mojivaDTO.getCpcRevenue(),6);
				double cpmRevenue=StringUtil.getDoubleValue(mojivaDTO.getCpmRevenue(),6);
				long requests=StringUtil.getLongValue(mojivaDTO.getRequests());
				long totalImpressions=StringUtil.getLongValue(mojivaDTO.getTotalImpressions());
				long cpmImpressions=StringUtil.getLongValue(mojivaDTO.getCpmImpressions());
				long cpcImpressions=StringUtil.getLongValue(mojivaDTO.getCpcImpressions()); 
				
				String siteId=mojivaDTO.getSiteId();
				String siteName=mojivaDTO.getSiteName();
				if(siteName!=null && siteName.trim().length()>0){
					siteName=siteName.replaceAll("\\+", " ");
				}
				
				double ctr=StringUtil.getDoubleValue(mojivaDTO.getCtr(),6);
				double eCPM=StringUtil.getDoubleValue(mojivaDTO.geteCPM(),6);
				double fillRate=(((double)totalImpressions/requests)*100.0);
				
				
				rootObj.setChannelId(Long.parseLong(LinMobileConstants.MOJIVA_CHANNEL_ID));
				rootObj.setChannelName(LinMobileConstants.MOJIVA_CHANNEL_NAME);
				rootObj.setChannelType(LinMobileConstants.MOJIVA_CHANNEL_TYPE);
				rootObj.setSalesType(LinMobileConstants.MOJIVA_SALES_TYPE);
							
				rootObj.setTotalClicks(clicks);
				rootObj.setClicksCPC(0);
				rootObj.setClicksCPM(clicks);
				
				rootObj.setLoadTimestamp(loadTimestamp);
				
				rootObj.setDate(DateUtil.getFormatedDate(mojivaDTO.getDate(), "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
				
				rootObj.setTotalImpressions(totalImpressions);
				rootObj.setImpressionsCPC(cpcImpressions);
				rootObj.setImpressionsCPM(cpmImpressions);
				
				rootObj.setPublisherId(StringUtil.getLongValue(LinMobileConstants.MOJIVA_PUBLISHER_ID));
				rootObj.setPublisherName(LinMobileConstants.MOJIVA_PUBLISHER_NAME);
				rootObj.setRequests(requests);
				
				rootObj.setTotalRevenue(totalRevenue);				
				rootObj.setRevenueCPC(cpcRevenue);
				rootObj.setRevenueCPM(cpmRevenue);
				rootObj.setAdserverCPMAndCPCRevenue((cpcRevenue+cpmRevenue));
				rootObj.setRevenueCPD(0);
				
				rootObj.setECPM(eCPM);
				rootObj.setFillRate(fillRate);
				rootObj.setCTR(ctr);
				rootObj.setRPM(ReportUtil.calculateRPM(totalRevenue, requests));			
				rootObj.setServed(0);
				rootObj.setSiteId(siteId);
				rootObj.setSiteName(siteName);
				
				rootObj.setDataSource("Mojiva");
				
				if(siteName.contains("Web") || siteName.contains("Tablet")){
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
		}
		return rootDataList;
	}
	
	
	
	public static void main(String[] args){
		MojiveReportWebServiceResponse obj= getAllMojivaSites();
		System.out.println(obj.getSite_id());
	}
}

