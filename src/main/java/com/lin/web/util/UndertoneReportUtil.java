package com.lin.web.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.files.LockException;
import com.lin.server.bean.CorePerformanceReportObj;

public class UndertoneReportUtil {

	private static final Logger log = Logger.getLogger(UndertoneReportUtil.class.getName());

	public static String uploadUndertoneRawCSVReportOnCloudStorage(String file,
			String fileName, String dirName) {
		
        String reportPath=null;
		InputStream inputStream;
		StringBuffer strBuffer = new StringBuffer();
		try {
			inputStream = IOUtils.toInputStream(file, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			int count=0;
			while ((line = reader.readLine()) != null) {
				if(count !=0){
					strBuffer.append('\n');
				}
				strBuffer.append(line);
				count++;
			}			
			reader.close();
			if(strBuffer.toString() !=null && strBuffer.toString().length()>0){
				reportPath=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
			}else{
				log.warning("No data found in csv file:");
			}
			
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		
		return reportPath;
	}
	
	public static String uploadUndertoneRawCSVReportOnCloudStorage(String file,
			String fileName, String dirName,String bucketName) {
		
        String reportPath=null;
		InputStream inputStream;
		StringBuffer strBuffer = new StringBuffer();
		try {
			inputStream = IOUtils.toInputStream(file, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			int count=0;
			while ((line = reader.readLine()) != null) {
				if(count !=0){
					strBuffer.append('\n');
				}
				strBuffer.append(line);
				count++;
			}			
			reader.close();
			if(strBuffer.toString() !=null && strBuffer.toString().length()>0){
				reportPath=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName,bucketName);
			}else{
				log.warning("No data found in csv file:");
			}
			
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		
		return reportPath;
	}
	public static List<CorePerformanceReportObj> createUndertoneCorePerformanceCSVReport(String fileName) {
		List<CorePerformanceReportObj> reportList = new ArrayList<CorePerformanceReportObj>();

		InputStream inputStream;
		String loadTimestamp=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		try {
			inputStream = IOUtils.toInputStream(fileName, "ISO-8859-1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			CSVReader csvReader = new CSVReader(reader);

			List<String[]> allElements = csvReader.readAll();
			
			int count = 0;
			for (String[] line : allElements) {
				if (count == 0) {
					log.info("Skip first row...");
				} else {
					if (line.length >= 6) {
						String siteName = line[0];							
						String costType = line[1];
						String rate = line[2];
						String date = line[3];
						date = DateUtil.getFormatedDate(date, "MM/dd/yyyy",
								"yyyy-MM-dd 00:00:00");
						String impressions = line[4];
						String revenue = line[5];

						CorePerformanceReportObj rootObj = new CorePerformanceReportObj();
						rootObj.setLoadTimestamp(loadTimestamp);
						rootObj.setCostType(costType);
						rootObj.setDate(date);

						rootObj.setTotalImpressions(StringUtil.getLongValue(impressions));
						rootObj.setTotalRevenue(StringUtil.getDoubleValue(revenue));
						
						if (costType != null && costType.equalsIgnoreCase("CPD")) {
							rootObj.setImpressionsCPD(StringUtil.getLongValue(impressions));
							rootObj.setRevenueCPD(StringUtil.getDoubleValue(revenue));
						} else {
							rootObj.setImpressionsCPM(StringUtil.getLongValue(impressions));
							rootObj.setRevenueCPM(StringUtil.getDoubleValue(revenue));
						}
						rootObj.setRate(StringUtil.getDoubleValue(rate));
						rootObj.setECPM(StringUtil.getDoubleValue(rate));
						rootObj.setSiteName(siteName);

						rootObj.setPublisherId(0);
						rootObj.setPublisherName("Publisher A");
						
						rootObj.setChannelId(Integer
								.parseInt(LinMobileConstants.UNDERTONE_CHANNEL_ID));
						rootObj.setChannelName(LinMobileConstants.UNDERTONE_CHANNEL_NAME);
						rootObj.setChannelType(LinMobileConstants.UNDERTONE_CHANNEL_TYPE);
						rootObj.setSalesType(LinMobileConstants.UNDERTONE_SALES_TYPE);
						rootObj.setDataSource("Undertone");
						
						rootObj.setPassback(ReportUtil.getPassback(rootObj));
						rootObj.setDirectDelivered(ReportUtil.getDirectDelivered(rootObj));
						
						reportList.add(rootObj);
					}
				}
				count++;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (LockException e) {
			log.severe("LockException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			

		}
		return reportList;
	}


	public static void main(String[] args) {

	}
}
