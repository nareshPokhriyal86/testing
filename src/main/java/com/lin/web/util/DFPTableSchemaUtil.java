package com.lin.web.util;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

public class DFPTableSchemaUtil {
	
	
	
	public static TableSchema getRawSchema(String loadType){
		String [][] schemaFields = null;
		switch (loadType) {
		case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
			schemaFields = corePerformanceRawSchemaFields;
			break;
		case LinMobileConstants.LOAD_TYPE_COMMON:
			schemaFields = getCommonRawSchema(80);
			break;
		}
		return getSchema(schemaFields);
	}
	
	public static TableSchema getProcSchema(String loadType){
		String [][] schemaFields = null;
		switch (loadType) {
		case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
			schemaFields = corePerformanceProcSchemaFields;
			break;
		case LinMobileConstants.LOAD_TYPE_LOCATION:
			schemaFields = locationProcSchemaFields;
			break;	
		case LinMobileConstants.LOAD_TYPE_TARGET:
			schemaFields = targetProcSchemaFields;
			break;	
		case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
			schemaFields = richMediaProcSchemaFields;
			break;	
		case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
			schemaFields = customEventProcSchemaFields;
			break;		
		case "adSdkBQSchema":
			schemaFields = adSdkDataSchemaFields;
			break;	
		}
		return getSchema(schemaFields);
	}
	
	private static TableSchema getSchema(String[][] schemaFields){
		List<TableFieldSchema> tableFieldSchema = new ArrayList<TableFieldSchema>();
		for (String[] strArr : schemaFields) {

			TableFieldSchema schemaEntry = new TableFieldSchema();
			schemaEntry.setName(strArr[0]);
			schemaEntry.setType(strArr[1]);

			tableFieldSchema.add(schemaEntry);

		}
		TableSchema schema = new TableSchema();
		schema.setFields(tableFieldSchema);
		return schema;
	}
	
	private static String[][] commonRawSchema = null;
	private static String[][] getCommonRawSchema(int columns){
		if(commonRawSchema == null){
			commonRawSchema = new String[columns][2];
			for(int i=0;i<columns;i++){
				commonRawSchema[i][0] = "COL_"+i+"_"+0;
				commonRawSchema[i][1] = "STRING";
			}
		}
		return commonRawSchema;
	} 
	private static String[][] corePerformanceRawSchemaFields = new String[][] {
			{ "Dimension_DATE", "STRING" },
			{ "Dimension_ADVERTISER_ID", "STRING" },
			{ "Dimension_ADVERTISER_NAME", "STRING" },
			{ "Ad_unit_ID_1", "STRING" },
			{ "Ad_unit_ID_2", "STRING" },
			{ "Ad_unit_1", "STRING" },
			{ "Ad_unit_2", "STRING" },
			{ "Dimension_ORDER_ID", "STRING" },
			{ "Dimension_ORDER_NAME", "STRING" },
			{ "Dimension_LINE_ITEM_ID", "STRING" },
			{ "Dimension_LINE_ITEM_NAME", "STRING" },
			{ "Dimension_LINE_ITEM_TYPE", "STRING" },
			{ "Dimension_CREATIVE_ID", "STRING" },
			{ "Dimension_CREATIVE_NAME", "STRING" },
			{ "Dimension_CREATIVE_SIZE", "STRING" },
			{ "Dimension_CREATIVE_TYPE", "STRING" },
			{ "Dimension_SALESPERSON_NAME", "STRING" },
			{ "Dimension_SALESPERSON_ID", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_GOAL_QUANTITY", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_CONTRACTED_QUANTITY", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_COST_PER_UNIT", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_COST_TYPE", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_LIFETIME_CLICKS", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_LIFETIME_IMPRESSIONS", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_START_DATE_TIME", "STRING" },
			{ "DimensionAttribute_LINE_ITEM_END_DATE_TIME", "STRING" },
			{ "DimensionAttribute_ORDER_AGENCY", "STRING" },
			{ "DimensionAttribute_ORDER_AGENCY_ID", "STRING" },
			{ "DimensionAttribute_ORDER_LIFETIME_CLICKS", "STRING" },
			{ "DimensionAttribute_ORDER_LIFETIME_IMPRESSIONS", "STRING" },
			{ "DimensionAttribute_ORDER_PO_NUMBER", "STRING" },
			{ "DimensionAttribute_ORDER_START_DATE_TIME", "STRING" },
			{ "DimensionAttribute_ORDER_END_DATE_TIME", "STRING" },
			{ "DimensionAttribute_ORDER_TRAFFICKER", "STRING" },
			{ "Column_AD_SERVER_IMPRESSIONS", "STRING" },
			{ "Column_AD_SERVER_CLICKS", "STRING" },
			{ "Column_AD_SERVER_WITH_CPD_AVERAGE_ECPM", "STRING" },
			{ "Column_AD_SERVER_CTR", "STRING" },
			{ "Column_AD_SERVER_CPM_AND_CPC_REVENUE", "STRING" },
			{ "Column_AD_SERVER_CPD_REVENUE", "STRING" },
			{ "Column_AD_SERVER_ALL_REVENUE", "STRING" },
			{ "Column_AD_SERVER_DELIVERY_INDICATOR", "STRING" },
			{ "Column_AD_SERVER_WITHOUT_CPD_AVERAGE_ECPM", "STRING" },
			{ "Column_AD_SERVER_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_AVERAGE_ECPM", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_CLICKS", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_CTR", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_IMPRESSIONS", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_PERCENT_IMPRESSIONS", "STRING" },
			{ "Column_AD_EXCHANGE_LINE_ITEM_LEVEL_REVENUE", "STRING" }

	};
	
	public static String[][] corePerformanceProcSchemaFields = new String[][]{{
		"Load_Timestamp","TIMESTAMP"},
		{"Channel_ID","INTEGER"},
		{"Channel_Name","STRING"},
		{"Channel_Type","STRING"},
		{"Sales_Type","STRING"},
		{"Pubisher_ID","INTEGER"},
		{"Publisher_Name","STRING"},
		{"Advertiser","STRING"},
		{"Order","STRING"},
		{"Line_item_type","STRING"},
		{"Line_Item","STRING"},
		{"Creative","STRING"},
		{"Creative_Size","STRING"},
		{"Creative_type","STRING"},
		{"Site_ID","STRING"},
		{"Site_Name","STRING"},
		{"Site_type_ID","STRING"},
		{"Site_type","STRING"},
		{"Zone_ID","STRING"},
		{"Zone","STRING"},
		{"Advertiser_ID","STRING"},
		{"Order_ID","STRING"},
		{"Line_item_ID","STRING"},
		{"Creative_ID","STRING"},
		{"Order_start_date","TIMESTAMP"},
		{"Line_Item_start_date","TIMESTAMP"},
		{"Order_end_date","TIMESTAMP"},
		{"Line_Item_end_date","TIMESTAMP"},
		{"Order_start_time","TIMESTAMP"},
		{"Line_item_start_time","TIMESTAMP"},
		{"Order_end_time","TIMESTAMP"},
		{"Line_item_end_time","TIMESTAMP"},
		{"Order_PO_Number","STRING"},
		{"Agency","STRING"},
		{"Agency_ID","STRING"},
		{"Trafficker","STRING"},
		{"Salesperson","STRING"},
		{"Date","TIMESTAMP"},
		{"Cost_Type","STRING"},
		{"Rate","FLOAT"},
		{"Goal_qty","INTEGER"},
		{"Line_Item_priority","INTEGER"},
		{"Order_lifetime_impressions","INTEGER"},
		{"Line_item_lifetime_impressions","INTEGER"},
		{"Orde_lifetime_clicks","INTEGER"},
		{"Line_item_lifetime_clicks","INTEGER"},
		{"Requests","INTEGER"},
		{"Served","INTEGER"},
		{"Total_Impressions","INTEGER"},
		{"CPM_Impressions","INTEGER"},
		{"CPC_Impressions","INTEGER"},
		{"CPD_Impressions","INTEGER"},
		{"Total_Clicks","INTEGER"},
		{"CPM_Clicks","INTEGER"},
		{"CPC_Clicks","INTEGER"},
		{"CPD_Clicks","INTEGER"},
		{"Total_Revenue","FLOAT"},
		{"CPM_Revenue","FLOAT"},
		{"CPC_Revenue","FLOAT"},
		{"Ad_server_CPM_and_CPC_revenue","FLOAT"},
		{"Ad_server_CPD_revenue","FLOAT"},
		{"eCPM","FLOAT"},
		{"RPM","FLOAT"},
		{"Fill_Rate","FLOAT"},
		{"CTR","FLOAT"},
		{"Contracted_QTY","FLOAT"},
		{"Delivery_Indicator","FLOAT"},
		{"Budget","FLOAT"},
		{"Spent_Lifetime","FLOAT"},
		{"Balance_Lifetime","FLOAT"},
		{"Ad_Source","STRING"},
		{"Data_Source","STRING"},
		{"Order_Budget","FLOAT"},
		{"Passback","INTEGER"},
		{"Direct_Delivered","INTEGER"},
		{"Column1","STRING"},
		{"Column2","STRING"},
		{"Column3","STRING"},
		{"Column4","STRING"},
		{"Column5","STRING"},
		{"Column6","STRING"},
		{"Column7","STRING"},
		{"Column8","STRING"},
		{"Column9","STRING"},
		{"Column10","STRING"}};
	
	private static String[][] adSdkDataSchemaFields = new String[][]{
		{"id","STRING"},
		{"key","STRING"},
		{"timestamp","STRING"},
		{"make","STRING"},
		{"model","STRING"},
		{"idfa","STRING"},
		{"idfv","STRING"},
		{"androidId","STRING"},
		{"googleAdId","STRING"},
		{"deviceId","STRING"},
		{"odin","STRING"},
		{"pushNotificationId","STRING"},
			//Device parameters
		{"osDeviceParam","STRING"},
		{"osVersion","STRING"},
		{"imei","STRING"},
		{"meid","STRING"},
		{"serialId","STRING"},
		{"multisim","STRING"},
			// Network parameters
		{"wifiTimeStamp","STRING"},
		{"wifiMacAddress","STRING"},
		{"wifiName","STRING"},
		{"wifiIpAddress","STRING"},
		{"networkType","STRING"},
			// connection provider
		{"connectionProviderTimestamp","STRING"},
		{"sim","STRING"},
		{"mcc","STRING"},
		{"mnc","STRING"},
		{"timezone","STRING"},
			// network connection
		{"networkConnectionTimestamp","STRING"},
		{"macAddress","STRING"},
		{"ipAddress","STRING"},
		{"networkConnectionType","STRING"},
			// geo Params
		{"geoTimestamp","STRING"},
		{"latitude","STRING"},
		{"longitude","STRING"},
		{"cellId","STRING"},
		{"lac","STRING"},
			// user params
		{"language","STRING"},
		{"appstoreId","STRING"},
		{"country","STRING"},
		{"firstName","STRING"},
		{"lastName","STRING"},
		{"insertDate","STRING"},
		{"insertTime","STRING"}
	};
	public static String[][] locationProcSchemaFields = new String[][]{
		{"Load_Timestamp","TIMESTAMP"},
		{"Advertiser","STRING"},
		{"Order","STRING"},
		{"Line_item_type","STRING"},
		{"Line_Item","STRING"},
		{"Country","STRING"},
		{"Region","STRING"},
		{"City","STRING"},
		{"Advertiser_ID","STRING"},
		{"Order_ID","STRING"},
		{"Line_item_ID","STRING"},
		{"Country_ID","STRING"},
		{"Region_ID","STRING"},
		{"City_ID","STRING"},
		{"Order_start_date","TIMESTAMP"},
		{"Line_Item_start_date","TIMESTAMP"},
		{"Order_end_date","TIMESTAMP"},
		{"Line_Item_end_date","TIMESTAMP"},
		{"Order_start_time","TIMESTAMP"},
		{"Line_item_start_time","TIMESTAMP"},
		{"Order_end_time","TIMESTAMP"},
		{"Line_item_end_time","TIMESTAMP"},
		{"Order_PO_Number","STRING"},
		{"Agency","STRING"},
		{"Agency_ID","STRING"},
		{"Date","TIMESTAMP"},
		{"Cost_Type","STRING"},
		{"Rate","FLOAT"},
		{"Goal_qty","INTEGER"},
		{"Order_lifetime_impressions","INTEGER"},
		{"Line_item_lifetime_impressions","INTEGER"},
		{"Orde_lifetime_clicks","INTEGER"},
		{"Line_item_lifetime_clicks","INTEGER"},
		{"Ad_server_impressions","FLOAT"},
		{"Ad_server_clicks","INTEGER"},
		{"Ad_server_average_eCPM","FLOAT"},
		{"Ad_server_CTR","FLOAT"},
		{"Pubisher_ID","STRING"},
		{"Publisher_Name","STRING"},
		{"Data_Source","STRING"}
		};
	
	public static String[][] targetProcSchemaFields = new String[][]{
		{"Load_Timestamp","TIMESTAMP"},
		{"Pubisher_ID","INTEGER"},
		{"Publisher_Name","STRING"},
		{"Advertiser","STRING"},
		{"Order","STRING"},
		{"Line_Item_type","STRING"},
		{"Line_Item","STRING"},
		{"Advertiser_ID","STRING"},
		{"Order_ID","STRING"},
		{"Line_Item_ID","STRING"},
		{"Date","TIMESTAMP"},
		{"Requests","INTEGER"},
		{"Served","INTEGER"},
		{"Total_Impressions","INTEGER"},
		{"Total_Clicks","INTEGER"},
		{"Total_Revenue","FLOAT"},
		{"Ad_server_CPM_and_CPC_revenue","FLOAT"},
		{"ECPM","FLOAT"},
		{"Fill_Rate","FLOAT"},
		{"CTR","FLOAT"},
		{"Delivery_Indicator","FLOAT"},
		{"Data_Source","STRING"},
		{"Target_Category","STRING"},
		{"Target_Value","STRING"}
	};
	public static String[][] richMediaProcSchemaFields = new String[][]{
		{"Load_TIMESTAMP","TIMESTAMP"},
		{"Channel_Id","STRING"},
		{"Channel_Name","STRING"},
		{"Channel_Type","STRING"},
		{"Sales_Type","STRING"},
		{"Pubisher_Id","STRING"},
		{"Publisher_Name","STRING"},
		{"Data_Source","STRING"},
		{"Date","TIMESTAMP"},
		{"Advertiser_Id","STRING"},
		{"Advertiser","STRING"},
		{"Order_Id","STRING"},
		{"Order","STRING"},
		{"Line_Item_Id","STRING"},
		{"Line_Item","STRING"},
		{"Line_Item_type","STRING"},
		{"Creative_Id","STRING"},
		{"Creative","STRING"},
		{"Creative_Size","STRING"},
		{"Creative_Type","STRING"},
		{"Order_Start_Date","TIMESTAMP"},
		{"Line_Item_Start_Date","TIMESTAMP"},
		{"Order_End_Date","TIMESTAMP"},
		{"Line_Item_End_Date","TIMESTAMP"},
		{"Agency","STRING"},
		{"Agency_Id","STRING"},
		{"Goal_Quantity","FLOAT"},
		{"Rate","FLOAT"},
		{"Market","STRING"},
		{"Campaign_Category","STRING"},
		{"Rich_Media_Backup_Images","INTEGER"},
		{"Rich_Media_Display_Time","FLOAT"},
		{"Rich_Media_Average_Display_Time","FLOAT"},
		{"Rich_Media_Expansions","INTEGER"},
		{"Rich_Media_Expanding_Time","FLOAT"},
		{"Rich_Media_Interaction_Time","FLOAT"},
		{"Rich_Media_Interaction_Count","INTEGER"},
		{"Rich_Media_Interaction_Rate","FLOAT"},
		{"Rich_Media_Average_Interaction_Time","FLOAT"},
		{"Rich_Media_Interaction_Impressions","INTEGER"},
		{"Rich_Media_Manual_Closes","INTEGER"},
		{"Rich_Media_FullScreen_Impressions","INTEGER"},
		{"Rich_Media_Video_Interactions","INTEGER"},
		{"Rich_Media_Video_Interaction_Rate","FLOAT"},
		{"Rich_Media_Video_Mutes","INTEGER"},
		{"Rich_Media_Video_Pauses","INTEGER"},
		{"Rich_Media_Video_Playes","INTEGER"},
		{"Rich_Media_Video_Midpoints","INTEGER"},
		{"Rich_Media_Video_Completes","INTEGER"},
		{"Rich_Media_Video_Replays","INTEGER"},
		{"Rich_Media_Video_Stops","INTEGER"},
		{"Rich_Media_Video_Unmutes","INTEGER"},
		{"Rich_Media_Video_View_Rate","FLOAT"},
		{"Rich_Media_Video_View_Time","FLOAT"},
		{"Start","INTEGER"},
		{"First_Quartile","INTEGER"},
		{"Midpoint","INTEGER"},
		{"Third_Quartile","INTEGER"},
		{"Complete","INTEGER"},
		{"Average_View_Time","FLOAT"},
		{"Completion_Rate","FLOAT"},
		{"Error_Count","INTEGER"},
		{"Video_Length","FLOAT"},
		{"Video_Skip_Shown","INTEGER"},
		{"Engage_View","INTEGER"},
		{"View_Through_Rate","FLOAT"},
		{"Pause","INTEGER"},
		{"Resume","INTEGER"},
		{"Rewind","INTEGER"},
		{"Mute","INTEGER"},
		{"Unmute","INTEGER"},
		{"Collapse","INTEGER"},
		{"Expand","INTEGER"},
		{"FullScreen","INTEGER"},
		{"Video_Skips","INTEGER"},
		{"Average_Interaction_Rate","FLOAT"},
		{"Average_View_Rate","FLOAT"},
	};
	
	public static String[][] customEventProcSchemaFields = new String[][]{
		{"Load_Timestamp","TIMESTAMP"},
		{"Channel_Id","STRING"},
		{"Channel_Name","STRING"},
		{"Channel_Type","STRING"},
		{"Sales_Type","STRING"},
		{"Pubisher_Id","STRING"},
		{"Publisher_Name","STRING"},
		{"Data_Source","STRING"},
		{"Date","TIMESTAMP"},
		{"Site_Id","STRING"},
		{"Site_Name","STRING"},
		{"Site_Type","STRING"},
		{"Zone_Id","INTEGER"},
		{"Zone_Type","STRING"},
		{"Ad_Format","STRING"},
		{"Serving_Platform","STRING"},
		{"Advertiser_Id","STRING"},
		{"Advertiser","STRING"},
		{"Order_Id","STRING"},
		{"Order","STRING"},
		{"Line_Item_Id","STRING"},
		{"Line_Item","STRING"},
		{"Line_Item_type","STRING"},
		{"Creative_Id","STRING"},
		{"Creative","STRING"},
		{"Creative_Size","STRING"},
		{"Creative_type","STRING"},
		{"Order_Start_Date","TIMESTAMP"},
		{"Line_Item_Start_Date","TIMESTAMP"},
		{"Order_End_Date","TIMESTAMP"},
		{"Line_Item_End_Date","TIMESTAMP"},
		{"Agency_Id","INTEGER"},
		{"Agency","STRING"},
		{"Goal_Quantity","FLOAT"},
		{"Rate","FLOAT"},
		{"Market","STRING"},
		{"Campaign_Category","STRING"},
		{"Custom_Event","STRING"},
		{"Custom_Event_Type","STRING"},
		{"Custom_Event_Id","STRING"},
		{"Custom_Count_Value","INTEGER"},
		{"Custom_Time_Value","INTEGER"}

	};
}
