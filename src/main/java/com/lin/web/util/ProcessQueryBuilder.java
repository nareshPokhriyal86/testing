package com.lin.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.google.api.ads.dfp.jaxws.v201403.DeviceCapability;
import com.google.api.ads.dfp.jaxws.v201403.DeviceCapabilityTargeting;
import com.google.api.ads.dfp.jaxws.v201403.TechnologyTargeting;
import com.google.appengine.api.files.LockException;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.CorePerformanceTargetReportObj;
import com.lin.server.bean.DFPTargetReportObj;
import com.lin.server.bean.LinOneRichMediaReportObj;
import com.lin.server.bean.LocationReportObj;
import com.lin.server.bean.ProductPerformanceReportObj;
import com.lin.server.bean.RichMediaCommonReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;
import com.lin.server.bean.RichMediaReportObj;
import com.lin.server.bean.RichMediaReportTraffickingObj;
import com.lin.server.bean.SellThroughReportObj;
import com.lin.web.dto.CeltraDTO;
import com.lin.web.servlet.GCSUtil;
import com.lin.web.servlet.GCStorageUtil;

/*
 * @author Manish Mudgal
 */
public class ProcessQueryBuilder { 
	
	public static final String DEFAULT_STRING = "''";
	public static final String DEFAULT_FLOAT = "0.00";
	public static final String DEFAULT_INTEGER = "0";
	
	public static final String CAST_FLOAT = "FLOAT";
	public static final String CAST_INTEGER = "INTEGER";
	
	
	public static String getRawToProcQueryByLoadType(Map<String, String> columnMap, String loadType, String networkCode, String publisherId, String publisherName, String tableName) throws Exception{
		if(loadType.equals(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE)){
			return getCorePerformanceRawToProcQuery(columnMap, networkCode, publisherId, publisherName, tableName, null);
		}else if(loadType.equals(LinMobileConstants.LOAD_TYPE_LOCATION)){
			return getLocationRawToProcQuery(columnMap, networkCode, publisherId, publisherName, tableName, null);
		}else if(loadType.equals(LinMobileConstants.LOAD_TYPE_TARGET)){
			return getTargetRawToProcQuery(columnMap, networkCode, publisherId, publisherName, tableName, null);
		}else if(loadType.equals(LinMobileConstants.LOAD_TYPE_RICH_MEDIA)){
			return getRichMediaRawToProcQuery(columnMap, networkCode, publisherId, publisherName, tableName, null);
		}else if(loadType.equals(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT)){
			return getCustomEventRawToProcQuery(columnMap, networkCode, publisherId, publisherName, tableName, null);
		}
		
		throw new Exception("No suitable load type found. Parameter loadtype received is ["+loadType+"]");
	}
	
	public static String getCustomEventRawToProcQuery(Map<String, String> columnMap, String networkCode, String publisherId, String publisherName, String tableName, Integer counter){

		String loadType = LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT;
		 String poCol = columnMap.get("DimensionAttribute.ORDER_PO_NUMBER");
			String  advertiserNameCol = columnMap.get("Dimension.ADVERTISER_NAME"); 
			String lineItemTypeCol = columnMap.get("Dimension.LINE_ITEM_TYPE");
		int fieldIndex = 0;
		StringBuffer query = new StringBuffer();
				query.append("select "); 
		appendToQuery(getAsString(fieldIndex++, loadType), query," CURRENT_TIMESTAMP()  "); // loadTimeStamp);
				
					
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelIdSubQueryForRichMedia(networkCode, publisherId, publisherName, poCol, advertiserNameCol, lineItemTypeCol));
			
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelNameSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelTypeSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getSalesTypeSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+ProcessQueryBuilder.getPublisherIdSubQuery(networkCode, publisherId)+"'");
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherNameSubQuery(networkCode, publisherName));
		//8
		appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+LinMobileConstants.DFP_DATA_SOURCE+"'");
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("Dimension.DATE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// siteid
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// sitename
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// siteType
		appendToQuery(getAsString(fieldIndex++, loadType), query,"0");// zoneId
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// zonetype
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// adformat
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// servingPlatform
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_NAME", ",", " ",  columnMap,ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_NAME", ",", " ", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));				
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_TYPE",  columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_SIZE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_TYPE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING ));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.ORDER_AGENCY_ID", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY", columnMap, ProcessQueryBuilder.DEFAULT_STRING));					
		String goalQty = columnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY");
		appendToQuery(getAsString(fieldIndex++, loadType), query,"if(FLOAT("+goalQty+") IS NULL, -1, FLOAT("+goalQty+")) ");
		String costPerUnit = columnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT");
		appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(INTEGER(if("+costPerUnit+" IS NULL OR "+costPerUnit+" == '-0', '0', "+costPerUnit+"))/1000000) ");
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''"); // market
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''"); // campaignCategory
	
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CUSTOM_EVENT_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));	
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CUSTOM_EVENT_TYPE", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CUSTOM_EVENT_ID", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_CUSTOM_EVENT_COUNT", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));		
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_CUSTOM_EVENT_TIME", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));		

		
 
				query.append(" FROM "+ tableName +" where col_0_0 <> 'Dimension.DATE'");
				System.out.println("Last field index: "+ fieldIndex);
				System.out.println(query.toString());
				return query.toString();
	

}
	
	public static String getRichMediaRawToProcQuery(Map<String, String> columnMap, String networkCode, String publisherId, String publisherName, String tableName, Integer counter){
		String loadType = LinMobileConstants.LOAD_TYPE_RICH_MEDIA;
		 String poCol = columnMap.get("DimensionAttribute.ORDER_PO_NUMBER");
			String  advertiserNameCol = columnMap.get("Dimension.ADVERTISER_NAME"); 
			String lineItemTypeCol = columnMap.get("Dimension.LINE_ITEM_TYPE");
 		int fieldIndex = 0;
		StringBuffer query = new StringBuffer();
				query.append("select "); 
			/*1*/	appendToQuery(getAsString(fieldIndex++, loadType), query," CURRENT_TIMESTAMP()  "); // loadTimeStamp);
				
				
			/*2*/ appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelIdSubQueryForRichMedia(networkCode, publisherId, publisherName, poCol, advertiserNameCol, lineItemTypeCol));
			
			/*3*/	appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelNameSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		
			/*4*/ appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelTypeSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getSalesTypeSubQueryForRichMedia(networkCode, publisherId, publisherName,  poCol, advertiserNameCol, lineItemTypeCol));
		appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+ProcessQueryBuilder.getPublisherIdSubQuery(networkCode, publisherId)+"'");
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherNameSubQuery(networkCode, publisherName));
		//8
		appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+LinMobileConstants.DFP_DATA_SOURCE+"'");
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("Dimension.DATE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_NAME", ",", " ",  columnMap,ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_NAME", ",", " ", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));				
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_TYPE",  columnMap, ProcessQueryBuilder.DEFAULT_STRING));

		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_SIZE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_TYPE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
			
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING ));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY", columnMap, ProcessQueryBuilder.DEFAULT_STRING));					
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY_ID", columnMap,  ProcessQueryBuilder.DEFAULT_STRING));
		String goalQty = columnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY");
		appendToQuery(getAsString(fieldIndex++, loadType), query,"if(FLOAT("+goalQty+") IS NULL, -1, FLOAT("+goalQty+")) ");
		String costPerUnit = columnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT");
		appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(INTEGER(if("+costPerUnit+" IS NULL OR "+costPerUnit+" == '-0', '0', "+costPerUnit+"))/1000000) ");

		appendToQuery(getAsString(fieldIndex++, loadType), query,"''"); // market
		appendToQuery(getAsString(fieldIndex++, loadType), query,"''"); // campaignCategory
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_BACKUP_IMAGES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_DISPLAY_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_AVERAGE_DISPLAY_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_EXPANSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_EXPANDING_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_INTERACTION_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_INTERACTION_COUNT", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_INTERACTION_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_AVERAGE_INTERACTION_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_INTERACTION_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_MANUAL_CLOSES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_FULL_SCREEN_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_INTERACTIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_INTERACTION_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_MUTES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_PAUSES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_PLAYES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_MIDPOINTS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_COMPLETES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("RICH_MEDIA_VIDEO_REPLAYS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_STOPS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_UNMUTES", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_VIEW_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.RICH_MEDIA_VIDEO_VIEW_TIME", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_START", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_FIRST_QUARTILE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_MIDPOINT", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_THIRD_QUARTILE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_COMPLETE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_AVERAGE_VIEW_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_COMPLETION_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_ERROR_COUNT", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_VIDEO_LENGTH", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_VIDEO_SKIP_SHOWN", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_ENGAGED_VIEW", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_VIEW_THROUGH_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_PAUSE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_RESUME", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_REWIND", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_MUTE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_UNMUTE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_COLLAPSE", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_EXPAND", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_FULL_SCREEN", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_VIDEO_SKIPS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_AVERAGE_INTERACTION_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
		appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.VIDEO_INTERACTION_VIEW_RATE", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));

		
		
				query.append(" FROM "+ tableName +" where col_0_0 <> 'Dimension.DATE'");
				System.out.println("Last field index: "+ fieldIndex);
				System.out.println(query.toString());
				return query.toString();
	

}
	public static String getTargetRawToProcQuery(Map<String, String> columnMap, String networkCode, String publisherId, String publisherName, String tableName, Integer counter){

		String loadType = LinMobileConstants.LOAD_TYPE_TARGET;
		int fieldIndex = 0;
		StringBuffer query = new StringBuffer();
				query.append("select "); 
			/*1*/	appendToQuery(getAsString(fieldIndex++, loadType), query," CURRENT_TIMESTAMP()  "); // loadTimeStamp);
				
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherIdSubQuery(networkCode, publisherId));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherNameSubQuery(networkCode, publisherName));
			
			
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_NAME", ",", " ",  columnMap,ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_NAME", ",", " ", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_TYPE",  columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));				
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("Dimension.DATE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getRequestSubQuery(columnMap));
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0");// served
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalImpressions(columnMap));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalClicks(columnMap));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalRevenue(columnMap));
					String adServerCpcCpmRevenue = columnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(FLOAT(if("+adServerCpcCpmRevenue+" IS NULL , '0', "+adServerCpcCpmRevenue+"))/1000000) ");
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getECPM(columnMap));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getFillRateSubQuery(columnMap));// TODO: Round off implementations in fill rate
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCTR(columnMap));// TODO: Round off implementations in CTR
					String deliveryIndicator = columnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"if(FLOAT("+deliveryIndicator+") IS NULL, 0.00, FLOAT("+deliveryIndicator+")) "); // TODO: ROUND
					appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+LinMobileConstants.DFP_DATA_SOURCE+"'");
					String targetCriteria = columnMap.get("Dimension.GENERIC_CRITERION_NAME");
					appendToQuery(getAsString(fieldIndex++, loadType), query," NTH(1, SPLIT("+targetCriteria+", '='))");
					appendToQuery(getAsString(fieldIndex++, loadType), query," NTH(2, SPLIT("+targetCriteria+", '='))");  

				query.append(" FROM "+ tableName +" where col_0_0 <> 'Dimension.DATE'");
				System.out.println("Last field index: "+ fieldIndex);
				System.out.println(query.toString());
				return query.toString();
	

}
	
	public static String getLocationRawToProcQuery(Map<String, String> columnMap, String networkCode, String publisherId, String publisherName, String tableName, Integer counter){

			String loadType = LinMobileConstants.LOAD_TYPE_LOCATION;
		 String poCol = columnMap.get("DimensionAttribute.ORDER_PO_NUMBER");
			String  advertiserNameCol = columnMap.get("Dimension.ADVERTISER_NAME"); 
			String lineItemTypeCol = columnMap.get("Dimension.LINE_ITEM_TYPE");
			int count = 0;
			int fieldIndex = 0;
			StringBuffer query = new StringBuffer();
					query.append("select "); 
				/*1*/	appendToQuery(getAsString(fieldIndex++, loadType), query," CURRENT_TIMESTAMP()  "); // loadTimeStamp);
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_NAME", ",", " ",  columnMap,ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_NAME", ",", " ", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_TYPE",  columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.COUNTRY_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.REGION_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CITY_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));				

						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.COUNTRY_CRITERIA_ID", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.REGION_CRITERIA_ID", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CITY_CRITERIA_ID", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));
						
						
						appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING ));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							
							//30	
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_PO_NUMBER", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY", columnMap, ProcessQueryBuilder.DEFAULT_STRING));					 
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY_ID", columnMap,  ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("Dimension.DATE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.LINE_ITEM_COST_TYPE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
								
							String costPerUnit = columnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT");
							appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(INTEGER(if("+costPerUnit+" IS NULL OR "+costPerUnit+" == '-0', '0', "+costPerUnit+"))/1000000) ");

							String goalQty = columnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY");
							appendToQuery(getAsString(fieldIndex++, loadType), query,"if(INTEGER("+goalQty+") IS NULL, -1, INTEGER("+goalQty+")) ");
							
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));		
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.ORDER_LIFETIME_CLICKS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));				
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));				
							
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalImpressionsFloat(columnMap));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalClicks(columnMap));
								
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getECPM(columnMap));
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCTR(columnMap));// TODO: Round off implementations in CTR
							appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+ProcessQueryBuilder.getPublisherIdSubQuery(networkCode, publisherId)+"'");
							appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherNameSubQuery(networkCode, publisherName));
							appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+LinMobileConstants.DFP_DATA_SOURCE+"'");
 
					query.append(" FROM "+ tableName +" where col_0_0 <> 'Dimension.DATE'");
					System.out.println("Last field index: "+ fieldIndex);
					System.out.println(query.toString());
					return query.toString();
		

	}
	public static String getCorePerformanceRawToProcQuery(Map<String, String> columnMap, String networkCode, String publisherId, String publisherName, String tableName, Integer counter)
	{
			String loadType = LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE;
		 String poCol = columnMap.get("DimensionAttribute.ORDER_PO_NUMBER");
			String  advertiserNameCol = columnMap.get("Dimension.ADVERTISER_NAME"); 
			String lineItemTypeCol = columnMap.get("Dimension.LINE_ITEM_TYPE");
			int count = 0;
			int fieldIndex = 0;
			StringBuffer query = new StringBuffer();
					query.append("select "); 
					count++; if(counter == null || counter == count){
				/*1*/	appendToQuery(getAsString(fieldIndex++, loadType), query," CURRENT_TIMESTAMP()  "); // loadTimeStamp);
					}count++; if(counter == null || counter == count){
				/*2*/ appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelIdSubQuery(networkCode, poCol, advertiserNameCol, lineItemTypeCol));
					}count++; if(counter == null || counter == count){
						/*3*/	appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelNameSubQuery(networkCode, poCol, advertiserNameCol, lineItemTypeCol));
					}count++; if(counter == null || counter == count){
						/*4*/ appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getChannelTypeSubQuery(networkCode, poCol, advertiserNameCol, lineItemTypeCol));
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getSalesTypeSubQuery(networkCode, poCol, advertiserNameCol, lineItemTypeCol));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherIdSubQuery(networkCode, publisherId));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getPublisherNameSubQuery(networkCode, publisherName));}count++; if(counter == null || counter == count){
					//8
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_NAME", ",", " ",  columnMap,ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_NAME", ",", " ", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_TYPE",  columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_NAME", ",", " ",columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_SIZE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_TYPE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit ID 1", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit 1", ","," " ,columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit ID 2", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit 2", ","," " ,columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit ID 3", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					//20
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit 3", ","," " ,columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ADVERTISER_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.ORDER_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.LINE_ITEM_ID", columnMap,   ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.CREATIVE_ID", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){

					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING ));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					
					//30	
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_START_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.LINE_ITEM_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					//appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("DimensionAttribute.ORDER_END_DATE_TIME", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_PO_NUMBER", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY", columnMap, ProcessQueryBuilder.DEFAULT_STRING));					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_AGENCY_ID", columnMap,  ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.ORDER_TRAFFICKER", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Dimension.SALESPERSON_NAME",","," " , columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTimeStampColumn("Dimension.DATE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}count++; if(counter == null || counter == count){
					//40
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("DimensionAttribute.LINE_ITEM_COST_TYPE", columnMap, ProcessQueryBuilder.DEFAULT_STRING));}
					String costPerUnit = columnMap.get("DimensionAttribute.LINE_ITEM_COST_PER_UNIT");
					count++; if(counter == null || counter == count){
					
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(INTEGER(if("+costPerUnit+" IS NULL OR "+costPerUnit+" == '-0', '0', "+costPerUnit+"))/1000000) ");}
					String goalQty = columnMap.get("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY");
					count++; if(counter == null || counter == count){
					
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,"if(INTEGER("+goalQty+") IS NULL, -1, INTEGER("+goalQty+")) ");}count++; if(counter == null || counter == count){
						
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0 ");// lineItemPriority
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));}count++; if(counter == null || counter == count){		
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.ORDER_LIFETIME_CLICKS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));				}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));				}
					count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS", columnMap, ProcessQueryBuilder.CAST_INTEGER,  ProcessQueryBuilder.DEFAULT_INTEGER));				}
					count++; if(counter == null || counter == count){
					// 47s	
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getRequestSubQuery(columnMap));}count++; if(counter == null || counter == count){
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalImpressions(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getImpressionsCPM(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0");// CPC
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getImpressionsCPD(columnMap));}count++; if(counter == null || counter == count){
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalClicks(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getClicksCPM(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0");//CPC
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getClicksCPD(columnMap)); }count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getTotalRevenue(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// revenuecpm
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// revenuecpc
					}count++; if(counter == null || counter == count){
					String adServerCpcCpmRevenue = columnMap.get("Column.AD_SERVER_CPM_AND_CPC_REVENUE");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(FLOAT(if("+adServerCpcCpmRevenue+" IS NULL , '0', "+adServerCpcCpmRevenue+"))/1000000) ");
					}count++; if(counter == null || counter == count){
					String adServerCpdRevenue = columnMap.get("Column.AD_SERVER_CPD_REVENUE");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(FLOAT(if("+adServerCpdRevenue+" IS NULL ,  '0', "+adServerCpdRevenue+"))/1000000) ");}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getECPM(columnMap));}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// rpm
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getFillRateSubQuery(columnMap));// TODO: Round off implementations in fill rate
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCTR(columnMap));// TODO: Round off implementations in CTR
					}count++; if(counter == null || counter == count){
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getCastColumn("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY", columnMap, ProcessQueryBuilder.CAST_FLOAT,  ProcessQueryBuilder.DEFAULT_FLOAT));
					}count++; if(counter == null || counter == count){
					String deliveryIndicator = columnMap.get("Column.AD_SERVER_DELIVERY_INDICATOR");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"if(FLOAT("+deliveryIndicator+") IS NULL, 0.00, FLOAT("+deliveryIndicator+")) "); // TODO: ROUND
					}count++; if(counter == null || counter == count){
					 
					appendToQuery(getAsString(fieldIndex++, loadType), query,"FLOAT(if(INTEGER("+goalQty+") IS NULL , -1, INTEGER("+goalQty+")) * FLOAT(INTEGER(if("+costPerUnit+" IS NULL OR "+costPerUnit+" == '-0', '0', "+costPerUnit+"))/1000000) )/1000.0");
					
					// ABOVE IS BUDGET
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// spent lifetime
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// balance lifetime
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// spent lifetime
					appendToQuery(getAsString(fieldIndex++, loadType), query,"'"+LinMobileConstants.DFP_DATA_SOURCE+"'");
					appendToQuery(getAsString(fieldIndex++, loadType), query,"0.00");// order budget
					}
					String channelName = ProcessQueryBuilder.getChannelNameSubQuery(networkCode, poCol, advertiserNameCol, lineItemTypeCol);
					count++; if(counter == null || counter == count){
					String siteName = "REGEXP_REPLACE("+columnMap.get("Ad unit 1")+", ',',' ')";
					
					appendToQuery(getAsString(fieldIndex++, loadType), query,"if(("+siteName+") IS NOT  null AND (("+siteName+") == 'LIN Passbacks (lin.pb)' "
							+ " OR ("+siteName+") == 'lin.wlin') AND "+
					channelName+" IS NOT null AND ("+channelName+") <> 'House', 0, 1)"); // passback
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,"if(("+	channelName+") IS NOT null AND "
							+ "(("+channelName+") == 'National Sales Direct' OR ("+channelName+") == 'Local Sales Direct' OR "+channelName+" == 'House'), 0, 1)");// direct delivered
					 
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column1
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column2
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column3
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column4
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit ID 4", columnMap, ProcessQueryBuilder.DEFAULT_STRING)); // column5
					}count++; if(counter == null || counter == count){
					appendToQuery(getAsString(fieldIndex++, loadType), query,ProcessQueryBuilder.getStringColumn("Ad unit 4",","," ", columnMap, ProcessQueryBuilder.DEFAULT_STRING)); // column6
					}
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column7
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column8
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column9
					appendToQuery(getAsString(fieldIndex++, loadType), query,"''");// column10
					query.append(" FROM "+ tableName +" where col_0_0 <> 'Dimension.DATE'");
					System.out.println("Last field index: "+ fieldIndex);
					System.out.println(query.toString());
					return query.toString();
		
 }
 

 private static void appendToQuery(String asString, StringBuffer query, String string) {
	 query.append(string+" AS "+ asString+" , ");
}

private static String getAsString(int fieldIndex, String loadType) {
	//System.out.println("got fieldIndex = ["+fieldIndex+"]");
	String asString = null;
	switch (loadType) {
	case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
		asString = DFPTableSchemaUtil.corePerformanceProcSchemaFields[fieldIndex][0];
		break;
	case LinMobileConstants.LOAD_TYPE_LOCATION:
		asString = DFPTableSchemaUtil.locationProcSchemaFields[fieldIndex][0];
		break;
	case LinMobileConstants.LOAD_TYPE_TARGET:
		asString = DFPTableSchemaUtil.targetProcSchemaFields[fieldIndex][0];
		break;	
	case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
		asString = DFPTableSchemaUtil.richMediaProcSchemaFields[fieldIndex][0];
		break;	
	case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
		asString = DFPTableSchemaUtil.customEventProcSchemaFields[fieldIndex][0];
		break;		
	}
	
	 return asString;
}

private static void initMap(Map<String, String> columnMap){
	 columnMap.put("Dimension.DATE","col_0_0");
	 columnMap.put("Dimension.ADVERTISER_ID","col_1_0");
	 columnMap.put("Dimension.ADVERTISER_NAME","col_2_0");
	 columnMap.put("Ad unit ID 1","col_3_0");
	 columnMap.put("Ad unit ID 2","col_4_0");
	 columnMap.put("Ad unit 1","col_5_0");
	 columnMap.put("Ad unit 2","col_6_0");
	 columnMap.put("Dimension.ORDER_ID","col_7_0");
	 columnMap.put("Dimension.ORDER_NAME","col_8_0");
	 columnMap.put("Dimension.LINE_ITEM_ID","col_9_0");
	 columnMap.put("Dimension.LINE_ITEM_NAME","col_10_0");
	 columnMap.put("Dimension.LINE_ITEM_TYPE","col_11_0");
	 columnMap.put("Dimension.CREATIVE_ID","col_12_0");
	 columnMap.put("Dimension.CREATIVE_NAME","col_13_0");
	 columnMap.put("Dimension.CREATIVE_SIZE","col_14_0");
	 columnMap.put("Dimension.CREATIVE_TYPE","col_15_0");
	 columnMap.put("Dimension.SALESPERSON_NAME","col_16_0");
	 columnMap.put("Dimension.SALESPERSON_ID","col_17_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_GOAL_QUANTITY","col_18_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY","col_19_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_COST_PER_UNIT","col_20_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_COST_TYPE","col_21_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS","col_22_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS","col_23_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_START_DATE_TIME","col_24_0");
	 columnMap.put("DimensionAttribute.LINE_ITEM_END_DATE_TIME","col_25_0");
	 columnMap.put("DimensionAttribute.ORDER_AGENCY","col_26_0");
	 columnMap.put("DimensionAttribute.ORDER_AGENCY_ID","col_27_0");
	 columnMap.put("DimensionAttribute.ORDER_LIFETIME_CLICKS","col_28_0");
	 columnMap.put("DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS","col_29_0");
	 columnMap.put("DimensionAttribute.ORDER_PO_NUMBER","col_30_0");
	 columnMap.put("DimensionAttribute.ORDER_START_DATE_TIME","col_31_0");
	 columnMap.put("DimensionAttribute.ORDER_END_DATE_TIME","col_32_0");
	 columnMap.put("DimensionAttribute.ORDER_TRAFFICKER","col_33_0");
	 columnMap.put("Column.AD_SERVER_IMPRESSIONS","col_34_0");
	 columnMap.put("Column.AD_SERVER_CLICKS","col_35_0");
	 columnMap.put("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM","col_36_0");
	 columnMap.put("Column.AD_SERVER_CTR","col_37_0");
	 columnMap.put("Column.AD_SERVER_CPM_AND_CPC_REVENUE","col_38_0");
	 columnMap.put("Column.AD_SERVER_CPD_REVENUE","col_39_0");
	 columnMap.put("Column.AD_SERVER_ALL_REVENUE","col_40_0");
	 columnMap.put("Column.AD_SERVER_DELIVERY_INDICATOR","col_41_0");
	 columnMap.put("Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM","col_42_0");
	 columnMap.put("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","col_43_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM","col_44_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS","col_45_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR","col_46_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS","col_47_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS","col_48_0");
	 columnMap.put("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE","col_49_0");

 }

public static String getChannelIdSubQueryForRichMedia(String networkCode, String publisherId, String publisherName, String poCol, String advertiserNameCol, String lineItemTypeCol){
	StringBuffer query = new StringBuffer();
	  if(publisherId !=null && publisherName !=null){
		  query.append("'5'");
      }else{
      	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
      		query.append("'5'");	
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
        	  query.append("'5'");
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
        	  query.append("'4'");
          }
      }
	return query.toString();
}
public static String getChannelNameSubQueryForRichMedia(String networkCode, String publisherId, String publisherName, String poCol, String advertiserNameCol, String lineItemTypeCol){
	StringBuffer query = new StringBuffer();
	  if(publisherId !=null && publisherName !=null){
		  query.append("'National Sales Direct'");
      }else{
      	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
      		query.append("'National Sales Direct'");	
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
        	  query.append("'National Sales Direct'");
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
        	  query.append("'Local Sales Direct'");
          }
      }
	return query.toString();
}

public static String getChannelTypeSubQueryForRichMedia(String networkCode, String publisherId, String publisherName, String poCol, String advertiserNameCol, String lineItemTypeCol){
	StringBuffer query = new StringBuffer();
	  if(publisherId !=null && publisherName !=null){
		  query.append("'National Sales'");
      }else{
      	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
      		query.append("'National Sales'");	
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
        	  query.append("'National Sales'");
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
        	  query.append("'Local Sales'");
          }
      }
	return query.toString();
}

public static String getSalesTypeSubQueryForRichMedia(String networkCode, String publisherId, String publisherName, String poCol, String advertiserNameCol, String lineItemTypeCol){
	StringBuffer query = new StringBuffer();
	  if(publisherId !=null && publisherName !=null){
		  query.append("'Direct'");
      }else{
      	if(networkCode.equalsIgnoreCase(LinMobileConstants.DFP_NETWORK_CODE)){
      		query.append("'Direct'");	
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
        	  query.append("'Direct'");
          }else if(networkCode.equalsIgnoreCase(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
        	  query.append("'Direct'");
          }
      }
	return query.toString();
}


	public static String getChannelIdSubQuery(String networkCode, String poCol, String advertiserNameCol, String lineItemTypeCol){
		StringBuffer query = new StringBuffer();
		
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append("5 ") ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			query.append(
							" if("+poCol+" is null, 0,"
						    + "		if("+poCol+" == 'Lin Mobile Partnership' OR "+poCol+" == 'LIN Mobile Partnership' OR "+poCol+" == 'Partnership'," 
							+ "			if("+advertiserNameCol+" == 'WLIN | Nexage',   "+StringUtil.getLongValue(LinMobileConstants.NEXAGE_CHANNEL_ID)+","
							+ "				if("+advertiserNameCol+" == 'WLIN | Mojiva', "+StringUtil.getLongValue(LinMobileConstants.MOJIVA_CHANNEL_ID)+","
							+ "					if("+advertiserNameCol+" == 'LIN | Undertone' OR "+advertiserNameCol+" == 'LIN | Undertone', 6,"
							+ "						if("+advertiserNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)', "+StringUtil.getLongValue(LinMobileConstants.AD_EXCHANGE_CHANNEL_ID)+","
							+ "							if("+advertiserNameCol+" == 'WLIN | LSN', "+StringUtil.getLongValue(LinMobileConstants.LSN_CHANNEL_ID)+",-1	"
							+ "		 ))))) ,"
							+ "		if("+poCol+" == 'Lin Mobile Direct', 5,"
							+ "			if("+poCol+" == 'TEST Ads', 9, "
							+ "				if("+lineItemTypeCol+" == 'HOUSE' OR "+poCol+" == 'HOUSE', 8,"
							+ "					if("+lineItemTypeCol+" == 'STANDARD' OR "+lineItemTypeCol+" == 'SPONSORSHIP', 4,"
							+ "						-1"
							+ "			))"
							+ "			"
							+ "			))))"
					);
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append("4 ");
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)){
			query.append("4 ");
		}
		return query.toString();
	}
	public static String getChannelNameSubQuery(String networkCode, String poCol, String advertiserNameCol, String lineItemTypeCol){
		StringBuffer query = new StringBuffer();
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append("'National Sales Direct'") ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			query.append(
							" if("+poCol+" is null, 'N/A',"
						    + "		if("+poCol+" == 'Lin Mobile Partnership' OR "+poCol+" == 'LIN Mobile Partnership' OR "+poCol+" == 'Partnership'," 
							+ "			if("+advertiserNameCol+" == 'WLIN | Nexage',  '"+LinMobileConstants.NEXAGE_CHANNEL_NAME+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Mojiva', '"+LinMobileConstants.MOJIVA_CHANNEL_NAME+"',"
							+ "			if("+advertiserNameCol+" == 'LIN | Undertone' OR "+advertiserNameCol+" == 'LIN | Undertone', 'Undertone',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)', '"+LinMobileConstants.AD_EXCHANGE_CHANNEL_NAME+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | LSN', '"+LinMobileConstants.LSN_CHANNEL_NAME+"','N/A'	"
							+ "		 ))))),"
							+ "		if("+poCol+" == 'Lin Mobile Direct', 'National Sales Direct',"
							+ "		if("+poCol+" == 'TEST Ads', 'TEST Ads', "
							+ "			if("+lineItemTypeCol+" == 'HOUSE' OR "+poCol+" == 'HOUSE', 'House',"
							+ "			if("+lineItemTypeCol+" == 'STANDARD' OR "+lineItemTypeCol+" == 'SPONSORSHIP', 'Local Sales Direct',"
							+ "			'N/A'"
							+ "			))"
							+ "			"
							+ "			))))"
					);
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append("'Local Sales Direct'");
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)){
			query.append("'Local Sales Direct' ");
		}else{
			query.append("''");
		}
		
		return query.toString();
	}

	public static String getChannelTypeSubQuery(String networkCode, String poCol, String advertiserNameCol, String lineItemTypeCol){
		StringBuffer query = new StringBuffer();
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append("'National Sales' ") ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			query.append(
							" if("+poCol+" is null, 'N/A',"
						    + "		if("+poCol+" == 'Lin Mobile Partnership' OR "+poCol+" == 'LIN Mobile Partnership' OR "+poCol+" == 'Partnership'," 
							+ "			if("+advertiserNameCol+" == 'WLIN | Nexage',  '"+LinMobileConstants.NEXAGE_CHANNEL_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Mojiva', '"+LinMobileConstants.MOJIVA_CHANNEL_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'LIN | Undertone' OR "+advertiserNameCol+" == 'LIN | Undertone', 'Ad Network',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)', '"+LinMobileConstants.AD_EXCHANGE_CHANNEL_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | LSN', '"+LinMobileConstants.LSN_CHANNEL_TYPE+"', 'N/A'"
							+ "		 ))))),"
							+ "		if("+poCol+" == 'Lin Mobile Direct', 'National Sales',"
							+ "		if("+poCol+" == 'TEST Ads', 'TEST Ads', "
							+ "			if("+lineItemTypeCol+" == 'HOUSE' OR "+poCol+" == 'HOUSE', 'House Ads',"
							+ "			if("+lineItemTypeCol+" == 'STANDARD' OR "+lineItemTypeCol+" == 'SPONSORSHIP', 'Local Sales',"
							+ "			'N/A'"
							+ "			))"
							+ "			"
							+ "			))))"
					);
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append("'Local Sales'");
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)){
			query.append("'Local Sales'");
		}
		
		return query.toString();
	}
	public static String getSalesTypeSubQuery(String networkCode, String poCol, String advertiserNameCol, String lineItemTypeCol){
		StringBuffer query = new StringBuffer();
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append("'Diret' ") ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			query.append(
							" if("+poCol+" is null, 'N/A',"
						    + "		if("+poCol+" == 'Lin Mobile Partnership' OR "+poCol+" == 'LIN Mobile Partnership' OR "+poCol+" == 'Partnership'," 
							+ "			if("+advertiserNameCol+" == 'WLIN | Nexage',  '"+LinMobileConstants.NEXAGE_SALES_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Mojiva', '"+LinMobileConstants.MOJIVA_SALES_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'LIN | Undertone' OR "+advertiserNameCol+" == 'LIN | Undertone', 'Non-direct',"
							+ "			if("+advertiserNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)', '"+LinMobileConstants.AD_EXCHANGE_SALES_TYPE+"',"
							+ "			if("+advertiserNameCol+" == 'WLIN | LSN', '"+LinMobileConstants.LSN_SALES_TYPE+"', 'N/A'	"
							+ "		 ))))),"
							+ "		if("+poCol+" == 'Lin Mobile Direct', 'Direct',"
							+ "		if("+poCol+" == 'TEST Ads', 'N/A', "
							+ "			if("+lineItemTypeCol+" == 'HOUSE' OR "+poCol+" == 'HOUSE', 'In House',"
							+ "			if("+lineItemTypeCol+" == 'STANDARD' OR "+lineItemTypeCol+" == 'SPONSORSHIP', 'Direct',"
							+ "			'N/A'"
							+ "			))"
							+ "			"
							+ "			))))"
					);
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append("'Direct'");
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE)){
			query.append("'Direct'");
		}
		
		return query.toString();
	}
	public static String getPublisherIdSubQuery(String networkCode, String publisherId){
		StringBuffer query = new StringBuffer();
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append(StringUtil.getLongValue(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)+"") ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			
			 if(publisherId !=null ){
				 query.append(StringUtil.getLongValue(publisherId));
			 }else{
				 query.append(StringUtil.getLongValue(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID));
			 }
			query.append(" ");
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append(StringUtil.getLongValue(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)+"");
		}else if(publisherId !=null ){
			 query.append(StringUtil.getLongValue(publisherId));
		}
		
		return query.toString();
	}
	
	
	public static String getPublisherNameSubQuery(String networkCode, String publisherName){
		StringBuffer query = new StringBuffer();
		if(networkCode!=null && networkCode.equals(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE)){
			query.append(LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME) ;
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.DFP_NETWORK_CODE)){
			 if(publisherName!=null){
				 query.append(publisherName);
			 }else{
				 query.append(LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME);
			 }
			
		}else if(networkCode!=null && networkCode.equals(LinMobileConstants.TRIBUNE_DFP_NETWORK_CODE)){
			query.append(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME+" ");
		}else if(publisherName!=null){
			 query.append(publisherName);
		}
		
		return "'"+query.toString()+"' ";
	}
	
	public static String getStringColumn(String columnName, String replace1, String replaceWith1 , Map<String, String> columnMap, String nullDefault){
		return getStringColumn(columnName, replace1, replaceWith1, null, null, columnMap, nullDefault,"");
	}
	
	public static String getStringColumn(String columnName,  Map<String, String> columnMap, String nullDefault){
		return getStringColumn(columnName, null, null, null, null, columnMap, nullDefault,"");
	}
	public static String getStringColumn(String columnName,  Map<String, String> columnMap, String nullDefault, String asField){
		return getStringColumn(columnName, null, null, null, null, columnMap, nullDefault, asField);
	}
	public static String getStringColumn(String columnName, String replace1, String replaceWith1, String replace2, String replaceWith2, Map<String, String> columnMap, String nullDefault, String asField){
		StringBuilder querySnippet = new StringBuilder("");
		columnName = columnMap.get(columnName);
		if(columnName == null){
			return nullDefault+"  ";
		}
		if(replace1 != null && replaceWith1 != null){
			querySnippet.append("ifNull(REGEXP_REPLACE("+columnName+", '"+replace1+"', '"+replaceWith1+"'), "+nullDefault+") ");
			if(replace2 != null && replaceWith2 != null){
				String tempQ = querySnippet.toString();
				querySnippet = new StringBuilder("");
				querySnippet.append("ifNull(REGEXP_REPLACE("+tempQ+", '"+replace2+"', '"+replaceWith2+"'), "+nullDefault+") ");
			} 
			 
		}else{
			querySnippet.append(columnName);
		}
		
		return querySnippet.toString();
	}
	public static String getTimeStampColumn(String columnName, Map<String, String> columnMap, String nullDefault){
		columnName = columnMap.get(columnName);
		if(columnName == null){
			return "CURRENT_TIMESTAMP()";
		}
		 return "TIMESTAMP(substr(REGEXP_REPLACE("+columnName+", 'T',' '), 0, 19))";
	 }
	public static String getCastColumn(String columnName, Map<String, String> columnMap, String castType, String nullDefault){
		StringBuilder querySnippet = new StringBuilder("");
		
		columnName = columnMap.get(columnName);
		if(columnName == null){
			return nullDefault+"  ";
		}
		querySnippet.append("ifNull("+castType+"("+columnName+"), "+nullDefault+")");
 		return querySnippet.toString();
	}
	public static String getRequestSubQuery(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		// Normal columns
		String levelImpCol = "INTEGER("+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+")"; // long
		String levelImpPercentCol = columnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"); // long
		String finalLevelImpPercent= "if(INTEGER("+levelImpPercentCol+") IS NULL ,0,FLOAT("+levelImpPercentCol+") * 100)";
		
		
		// Ad exchange column
		String adExLevelImpCol = "INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+")"; // long
		String adExLevelImpPercentCol = columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"); // long
		String finalAdExLevelImpPercent= "if(FLOAT("+adExLevelImpPercentCol+") IS NULL,0,FLOAT("+adExLevelImpPercentCol+" ) * 100)";
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
							+ "		if("+finalAdExLevelImpPercent+" == 0 , 0 , "
									+ "	INTEGER(("+adExLevelImpCol+" * 100) / "+finalAdExLevelImpPercent+"))"
							+ ","
							+ "if("+finalLevelImpPercent+" == 0 , 0 , INTEGER(("+levelImpCol+" * 100) / "+finalLevelImpPercent+"))) ");
				
		return querySnippet.toString();
	}
	public static String getFillRateSubQuery(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		// Normal columns
		String levelImpCol = "INTEGER("+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+")"; // long
		String levelImpPercentCol = columnMap.get("Column.AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"); // long
		String finalLevelImpPercent= "if(FLOAT("+levelImpPercentCol+") IS NULL,0,FLOAT("+levelImpPercentCol+") * 100)";
		
		/**
		  		public static double calculateFillRate(long impressions,long requests){
		double fillRate=(((double)impressions/requests)*100.0);
		fillRate=Math.round((fillRate*100.0))/100.0;
		return fillRate;
		 */
		// Ad exchange column
		String adExLevelImpCol = "INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+")"; // long
		String adExLevelImpPercentCol = columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS"); // long
		String finalAdExLevelImpPercent= "if(FLOAT("+adExLevelImpPercentCol+")IS NULL,0,FLOAT("+adExLevelImpPercentCol+") * 100 )";
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ "FLOAT("+adExLevelImpCol+"/(if("+finalAdExLevelImpPercent+" == 0 , 0 , INTEGER(("+adExLevelImpCol+" * 100) / "+finalAdExLevelImpPercent+"))))"
				+ ","
				+ "FLOAT("+levelImpCol+"/(if("+finalLevelImpPercent+" == 0 , 0 , INTEGER(("+levelImpCol+" * 100) / "+finalLevelImpPercent+"))))) ");
				
		return querySnippet.toString();
	}
	public static String getTotalImpressions(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		// Normal columns
		String levelImpCol = "INTEGER("+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+")"; // long
		
		
		// Ad exchange column
		String adExLevelImpCol = "INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+")"; // long
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExLevelImpCol
				+ ","
				+ levelImpCol+") ");
				
		return querySnippet.toString();
	}
	
	public static String getTotalImpressionsFloat(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		// Normal columns
		String levelImpCol = "FLOAT("+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+")"; // long
		
		
		// Ad exchange column
		String adExLevelImpCol = "FLOAT("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+")"; // long
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExLevelImpCol
				+ ","
				+ levelImpCol+") ");
				
		return querySnippet.toString();
	}
	public static String getImpressionsCPM(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");

		String costType = columnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE");
	String levelImpCol = ""+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+""; // long
		
		
		// Ad exchange column
		String adExLevelImpCol = ""+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+""; // long
		
		querySnippet.append("INTEGER(if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', '0', "+adExLevelImpCol+") "
				+ ","
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', '0', "+levelImpCol+") "+"))");
				
		return querySnippet.toString();
				
	}
	
	public static String getImpressionsCPD(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");

		String costType = columnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE");
	String levelImpCol = ""+columnMap.get("Column.AD_SERVER_IMPRESSIONS")+""; // long
		
		
		// Ad exchange column
		String adExLevelImpCol = ""+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS")+""; // long
		
		querySnippet.append("INTEGER(if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', "+adExLevelImpCol+" , '0') "
				+ ","
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', "+levelImpCol+" , '0') "+")) ");
				
		return querySnippet.toString();
				
	}
	
	public static String getTotalClicks(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		String adServerClicks = "ifNull(INTEGER("+columnMap.get("Column.AD_SERVER_CLICKS")+"), 0)"; // ;
		String adExServerClicks = "ifNull(INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")+"), 0)"; // ;
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExServerClicks
				+ ","
				+ adServerClicks +") ");
				
		return querySnippet.toString();
				
	}
	
	public static String getClicksCPM(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");

		String costType = columnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE");
	String levelClickCol = ""+columnMap.get("Column.AD_SERVER_CLICKS")+""; // long
		
		
		// Ad exchange column
		String adExLevelClickCol = ""+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")+""; // long
		
		querySnippet.append("INTEGER(if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', '0', "+adExLevelClickCol+") "
				+ ","
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', '0', "+levelClickCol+") "+"))");
				
		return querySnippet.toString();
				
	}
	
	public static String getClicksCPD(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");

		String costType = columnMap.get("DimensionAttribute.LINE_ITEM_COST_TYPE");
	String levelClickCol = ""+columnMap.get("Column.AD_SERVER_CLICKS")+""; // long
		
		
		// Ad exchange column
		String adExLevelClickCol = ""+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS")+""; // long
		
		querySnippet.append("INTEGER(if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', "+adExLevelClickCol+" , '0') "
				+ ","
				+ "if("+costType+" IS NOT NULL AND "+costType+"  == 'CPD', "+levelClickCol+" , '0' ) "+"))");
				
		return querySnippet.toString();
				
	}
	
	
	
	
	public static String getTotalRevenue(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		String adServerTotalRevenue = "FLOAT(ifNull(INTEGER("+columnMap.get("Column.AD_SERVER_ALL_REVENUE")+"), 0)/1000000)"; // ;
		String adExServerTotalRevenue = "FLOAT(ifNull(INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE")+"), 0)/1000000)"; // ;
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExServerTotalRevenue
				+ ","
				+ adServerTotalRevenue +") ");
				
		return querySnippet.toString();
				
	}
	public static String getECPM(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		String adServerECPM = "FLOAT(ifNull(INTEGER("+columnMap.get("Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM")+"), 0)/1000000)"; // ;
		String adExServerECPM = "FLOAT(ifNull(INTEGER("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM")+"),  0)/1000000)"; // ;
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExServerECPM
				+ ","
				+ adServerECPM +") ");
				
		return querySnippet.toString();
				
	}
	
	public static String getCTR(Map<String, String> columnMap) {
		StringBuilder querySnippet = new StringBuilder("");
		String advNameCol = columnMap.get("Dimension.ADVERTISER_NAME");
		String adServerClicks = "ifNull(FLOAT("+columnMap.get("Column.AD_SERVER_CTR")+"), 0.00)"; // ;
		String adExServerClicks = "ifNull(FLOAT("+columnMap.get("Column.AD_EXCHANGE_LINE_ITEM_LEVEL_CTR")+"), 0.00)"; // ;
		
		querySnippet.append("if("+advNameCol+" == 'WLIN | Google Ad Exchange - AdEx (Remnant)' , "
				+ adExServerClicks
				+ ","
				+ adServerClicks +") ");
				
		return querySnippet.toString();
				
	}
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
		int index = 0;
		map.put("col_"+(index++)+"_0","Dimension.DATE"); 
		 map.put("col_"+(index++)+"_0","Dimension.ADVERTISER_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.ADVERTISER_NAME"); 
		 map.put("col_"+(index++)+"_0","Dimension.ORDER_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.ORDER_NAME"); 
		 map.put("col_"+(index++)+"_0","Dimension.LINE_ITEM_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.LINE_ITEM_NAME"); 
		 map.put("col_"+(index++)+"_0","Dimension.LINE_ITEM_TYPE"); 
		 map.put("col_"+(index++)+"_0","Dimension.COUNTRY_CRITERIA_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.REGION_CRITERIA_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.CITY_CRITERIA_ID"); 
		 map.put("col_"+(index++)+"_0","Dimension.COUNTRY_NAME"); 
		 map.put("col_"+(index++)+"_0","Dimension.REGION_NAME"); 
		 map.put("col_"+(index++)+"_0","Dimension.CITY_NAME"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_GOAL_QUANTITY"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_CONTRACTED_QUANTITY"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_COST_PER_UNIT"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_COST_TYPE"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_LIFETIME_CLICKS"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_LIFETIME_IMPRESSIONS"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_START_DATE_TIME"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.LINE_ITEM_END_DATE_TIME"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_AGENCY"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_AGENCY_ID"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_LIFETIME_CLICKS"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_LIFETIME_IMPRESSIONS"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_PO_NUMBER"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_START_DATE_TIME"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_END_DATE_TIME"); 
		 map.put("col_"+(index++)+"_0","DimensionAttribute.ORDER_TRAFFICKER"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_IMPRESSIONS"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_CLICKS"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_WITH_CPD_AVERAGE_ECPM"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_CTR"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_CPM_AND_CPC_REVENUE"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_CPD_REVENUE"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_ALL_REVENUE"); 
		 map.put("col_"+(index++)+"_0","Column.AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM");
		 getLocationRawToProcQuery(map, "5678", "1", "LinMedia", "[LIN_RAW._raw_order_178037502__location_2015_01_04_2015_01_04_1420541720045]", null);
		 
	}
}
