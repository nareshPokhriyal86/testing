package com.lin.web.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.CloudProjectDTO;
import com.lin.web.dto.DataUploaderDTO;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;



public class DataLoaderUtil { 
	private static final Logger log = Logger.getLogger(DataLoaderUtil.class.getName());
	private static Map<String, String>dfpBucketMap = null;
	private static Map<String, String>cloudStorageBucketMap = null;
	
	
	
	
	public static CloudProjectDTO getCloudProjectDTO(String publisherIdInBQ){
		Map<String,CloudProjectDTO> cloudProjectMap=LinMobileProperties.getCloudProjectDTOMap();
		log.info("cloudProjectMap : "+cloudProjectMap.size());
		CloudProjectDTO cloudProjectBQDTO=cloudProjectMap.containsKey(publisherIdInBQ)?cloudProjectMap.get(publisherIdInBQ):null;
		return cloudProjectBQDTO;
	}
	
	
	public static String getPublisherName(String publisherIdInBQ){
		String publisherName = null;
		switch (publisherIdInBQ) {
			case LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID:
				publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
			break;
			case LinMobileConstants.LIN_MEDIA_PUBLISHER_ID:
				publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
			break;
			case LinMobileConstants.LIN_MOBILE_PUBLISHER_ID:
				publisherName=LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
			break;
			case LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID:
				publisherName=LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME;
			break;
			case LinMobileConstants.CLIENT_DEMO_COMPANY_ID:
				publisherName=LinMobileConstants.CLIENT_DEMO_COMPANY_NAME;
			break;
			default :
				log.info("No DFP configured for this publisher id  : "+publisherIdInBQ);
				break;
			
			}
		return publisherName;
	}
	
	
	public static String getBucketSubDirectory(String networkCode){
		String bucketSubDir = null;
		switch (networkCode) {
		case LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE:
			bucketSubDir="LinDigital";
		break;
		}
		return bucketSubDir;
	}
	
	
	
	public static String getSchemaNameByLoadType(String loadType){
		String schemaName = null;
		switch (loadType) {
			case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
				schemaName=LinMobileConstants.BQ_CORE_PERFORMANCE;
			break;
			case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
				schemaName=LinMobileConstants.BQ_CUSTOM_EVENT;
			break;
			case LinMobileConstants.LOAD_TYPE_LOCATION:
				schemaName=LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION;
			break;
			case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
				schemaName=LinMobileConstants.BQ_RICH_MEDIA;
			break;
			case LinMobileConstants.LOAD_TYPE_TARGET:
				schemaName=LinMobileConstants.BQ_DFP_TARGET;
			break;
	
			case LinMobileConstants.LOAD_TYPE_PRODUCT_PERFORMANCE:
				schemaName=LinMobileConstants.BQ_PRODUCT_PERFORMANCE;
			break;
			
			case LinMobileConstants.LOAD_TYPE_SELL_THROUGH:
				schemaName=LinMobileConstants.BQ_SELL_THROUGH;
			break;
			default:
				schemaName = null;
		    break;
		
		}
		return schemaName;
	}
	
	
	public static String getSchemaFileByLoadType(String loadType){
		String schemaFileName = null;
		switch (loadType) {
			case LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE:
				schemaFileName=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
			break;
			case LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT:
				schemaFileName=LinMobileConstants.CUSTOM_EVENT_TABLE_SCHEMA;
			break;
			case LinMobileConstants.LOAD_TYPE_LOCATION:
				schemaFileName=LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_SCHEMA;
			break;
			case LinMobileConstants.LOAD_TYPE_RICH_MEDIA:
				schemaFileName=LinMobileConstants.RICH_MEDIA_TABLE_SCHEMA;
			break;
			case LinMobileConstants.LOAD_TYPE_TARGET:
				schemaFileName=LinMobileConstants.DFP_TARGET_PLATFORM_TABLE_SCHEMA;
			break;
	
			case LinMobileConstants.LOAD_TYPE_PRODUCT_PERFORMANCE:
				schemaFileName=LinMobileConstants.PRODUCT_PERFORMANCE_TABLE_SCHEMA;
			break;
			
			case LinMobileConstants.LOAD_TYPE_SELL_THROUGH:
				schemaFileName=LinMobileConstants.SELL_THROUGH_TABLE_SCHEMA;
			break;
			
		}
		return schemaFileName;
	}
	
	
	public static String getDfpBucket(String networkCode){
		if(dfpBucketMap == null){
			initializeDfpBucketMap();
		}
		return dfpBucketMap.get(networkCode);
	}
	
	public static String getCloudStorageBucket(String networkCode){
		if(cloudStorageBucketMap == null){
			initializeCloudStorageBucketMap();
		}
		return cloudStorageBucketMap.get(networkCode);
	}
	
	
	private static void initializeDfpBucketMap(){
		dfpBucketMap = new HashMap<String, String>();
		
		dfpBucketMap.put(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE, LinMobileConstants.LIN_DIGITAL_DFP_REPORT_BUCKET);
		//TODO: Add more network codes
		
	}
	private static void initializeCloudStorageBucketMap(){
		cloudStorageBucketMap = new HashMap<String, String>();
		
		cloudStorageBucketMap.put(LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE, LinMobileVariables.LIN_DIGITAL_CLOUD_STORAGE_BUCKET);
		//TODO: Add more network codes
		
	}
	
	
	/**
	 * @author Shubham Goel 
	 * @param int orderListSize : order id count 
	 * @return List<DataUploaderDTO> : returns dataUploaderDTOList, this method returns list of DataUploaderDTO for data upload crons..
    */
	
	
	
	
	public static List<DataUploaderDTO> getDataUploaderDTO(String orderId, String taskType){
		ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		List<SmartCampaignObj> campaignObjList = new ArrayList<>();
		Map<String,List<SmartCampaignObj>> campaignByCompanyIdMap = new HashMap<>();
		List<DataUploaderDTO> dataUploaderDTOList = new ArrayList<>();
		try{
			if(orderId!=null && !orderId.equals("")){
				List<Long> orderIdList = StringUtil.commaSeperatedToNumericList(orderId);
				campaignObjList = dao.loadAllCampaignsByDFPId(orderIdList);
			}else{
				campaignObjList = dao.getAllCampaignForDataUpload();
			}
			
			if(campaignObjList!=null && campaignObjList.size()>0){
				log.info("campaignObjList size = "+campaignObjList.size());
				campaignByCompanyIdMap = service.getCampaignByCompanyId(campaignObjList);
				dataUploaderDTOList = getOrderByBQID(campaignByCompanyIdMap, taskType);
			}else{
				log.info("no order found for data uplaod");
			}
		}catch(Exception e){
			log.severe("Exception ::"+e.getMessage());
		}
		return dataUploaderDTOList;
	}
	
	/**
	 * @author Shubham Goel 
	 * @param int orderListSize : order id count 
	 * @param Map<String,List<SmartCampaignObj>> : HashMap of company Id as key and list of smartCampaignObj as value
	 * @return List<DataUploaderDTO> : returns dataUploaderDTOList, this method returns list of DataUploaderDTO for data upload crons..
    */
	private  static List<DataUploaderDTO> getOrderByBQID(Map<String,List<SmartCampaignObj>> campaignByCompanyIdMap, String taskType){
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IHistoricalReportService historicalService = (IHistoricalReportService) BusinessServiceLocator.locate(IHistoricalReportService.class);
		List<DataUploaderDTO> dataUploaderDTOList = new ArrayList<>();
		int count = 0;
		int orderListSize = 0;
		HashSet<String> nonFinaliseOrdersSet = new HashSet<>();
		try{
			if(campaignByCompanyIdMap!=null && campaignByCompanyIdMap.size()>0){
				 Iterator iterator = campaignByCompanyIdMap.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry mapEntry = (Map.Entry) iterator.next();
						if(mapEntry!=null && mapEntry.getKey()!=null){
							CompanyObj companyObj = new CompanyObj();
							companyObj = service.getCompanyObjById(StringUtil.getLongValue(mapEntry.getKey()+""));
							DataUploaderDTO dataUploaderDTO = new DataUploaderDTO();
							List<SmartCampaignObj> campaignObjList = new ArrayList<>();
							campaignObjList = (List<SmartCampaignObj>) mapEntry.getValue();
							String commaSepratedOrderId = "";
							List<String> orderList = new ArrayList<>();
							if(campaignObjList!=null && campaignObjList.size()>0){
								if(campaignObjList!=null && campaignObjList.size()>0 &&  campaignObjList.get(0).getAdServerId()!=null ){
									orderListSize = getOrderListSizeByNetworkCode(campaignObjList.get(0).getAdServerId());
									log.info("orderListSize by networkCode = "+orderListSize);
								}
								dataUploaderDTO.setPublisherBQId(companyObj.getBqIdentifier());
								dataUploaderDTO.setPublisherName(companyObj.getCompanyName());
								log.info("taskType = "+taskType);
								if(taskType!=null && taskType.equals(LinMobileConstants.NON_FINALISE_TASK_TYPE)){
									nonFinaliseOrdersSet = historicalService.getAllOrderIdsForNonFinaliseData(companyObj.getBqIdentifier()+"");
									for (SmartCampaignObj smartCampaignObj : campaignObjList) {
										if(smartCampaignObj!=null){
											nonFinaliseOrdersSet.add(smartCampaignObj.getDfpOrderId()+"");
											dataUploaderDTO.setDfpNetworkCode(smartCampaignObj.getAdServerId());
										}
									}
									log.info("nonFinaliseOrdersSet size after adding campaign order : "+nonFinaliseOrdersSet.size());
									for (String orderId : nonFinaliseOrdersSet) {
										if(count>orderListSize ){
											commaSepratedOrderId = StringUtil.deleteFromLastOccurence(commaSepratedOrderId, ",");
											if(!commaSepratedOrderId.equals("")){
												orderList.add(commaSepratedOrderId);
											}
											count = 0;
											commaSepratedOrderId = "";
										}else if(orderId!=null && !commaSepratedOrderId.contains(orderId)){
											commaSepratedOrderId = commaSepratedOrderId+orderId+",";
										}
										count++;
									}
								}else{
									for (SmartCampaignObj smartCampaignObj : campaignObjList) {
										if(count>orderListSize ){
											commaSepratedOrderId = StringUtil.deleteFromLastOccurence(commaSepratedOrderId, ",");
											if(!commaSepratedOrderId.equals("")){
												orderList.add(commaSepratedOrderId);
											}
											count = 0;
											commaSepratedOrderId = "";
										}else if(smartCampaignObj!=null && commaSepratedOrderId!=null && !commaSepratedOrderId.contains(String.valueOf(smartCampaignObj.getDfpOrderId()))){
											/*String endDate = smartCampaignObj.getEndDate();
											
											Date eDate = DateUtil.getDateMMDDYYYY(endDate);
											if(dataType.equals(LinMobileConstants.DAILY_TASK_TYPE) && !eDate.before(DateUtil.getAbsoluteDate(new Date()))){
												commaSepratedOrderId = commaSepratedOrderId+smartCampaignObj.getDfpOrderId()+",";
											}else if(dataType.equals(LinMobileConstants.HISTORICAL_TASK_TYPE)){
												commaSepratedOrderId = commaSepratedOrderId+smartCampaignObj.getDfpOrderId()+",";
											}*/
											commaSepratedOrderId = commaSepratedOrderId+smartCampaignObj.getDfpOrderId()+",";
										}
										count++;
										dataUploaderDTO.setDfpNetworkCode(smartCampaignObj.getAdServerId());
									}
								}
								
								commaSepratedOrderId = StringUtil.deleteFromLastOccurence(commaSepratedOrderId, ",");
								orderList.add(commaSepratedOrderId);
								dataUploaderDTO.setOrderIdList(orderList);
								dataUploaderDTOList.add(dataUploaderDTO);
							}
						}
					}
				}
		}catch(Exception e){
			log.severe("Exception ::"+e.getMessage());
		}
		return dataUploaderDTOList;
		
	}
	
	private static int getOrderListSizeByNetworkCode(String networkCode){
		int orderListSize = 0;
		switch (networkCode) {
		case LinMobileConstants.LIN_DIGITAL_DFP_NETWORK_CODE:
			orderListSize = LinMobileConstants.DAILY_REPORT_LIN_DIGITAL_ORDER_COUNT;
			break;
		case LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE:
			orderListSize = LinMobileConstants.DAILY_REPORT_LIN_MOBILE_ORDER_COUNT;
			break;
		case LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE:
			orderListSize = LinMobileConstants.DAILY_REPORT_LIN_MOBILE_NEW_ORDER_COUNT;
			break;
		default:
			orderListSize = LinMobileConstants.DAILY_REPORT_ORDER_COUNT;
			break;
		}
		return orderListSize;
	}
	
}