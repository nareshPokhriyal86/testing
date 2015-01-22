package com.lin.web.util;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;


/*
 * @author Youdhveer Panwar
 * This is task queue util which handles all task and put into task queue
 */
public class TaskQueueUtil{
	
	private static final Logger log = Logger.getLogger(TaskQueueUtil.class.getName());
	private static Map<String, Queue> queueMap = new HashMap<String, Queue>();
	
	public static void addTaskInDefaultQueue(String taskURL){
		log.info("Add task in taskfaultQueue with taskURL:"+taskURL);
               
        Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }        
        queue.add(withUrl(taskURL));
	}
	
	
	public static void addTaskInForHistoricalData(String taskURL, String start,String  end,
			String orderId, String publisherIdInBQ, String networkCode, String loadTypeElem, boolean merge){
		log.info("Add task in historicalDataUploader with taskURL:"+taskURL+ " | campaignId = "+orderId+" | startDate = "+start+" | endDate = "+end);
        Queue queue = queueMap.get("historicalDataUploader");
        if(queue == null){
        	queue = QueueFactory.getQueue("historicalDataUploader");
        	queueMap.put("historicalDataUploader", queue);
        }
        queue.add(withUrl(taskURL)
	        .param("start",start)
	        .param("end",end)
	        .param("orderId",orderId)
	        .param("publisherIdInBQ",publisherIdInBQ)
	        .param("networkCode",networkCode)
	        .param("loadTypeElem",loadTypeElem)
	        .param("merge",Boolean.toString(merge))
         );
	}
	
	public static String startTaskForDataLoading(String taskURL, String orderId, String publisherBQId, String networkCode, String taskId){
//		TaskQueueUtil.startTaskForDataLoading("/runSmartDataLoader.lin", orderId, dto.getPublisherBQId()+"", dto.getDfpNetworkCode(), null, "common-task-id");
		log.info("Add task in smartDataUploader with taskURL:"+taskURL);
        Queue queue = queueMap.get("smartDFPDataLoader");
        if(queue == null){
        	queue = QueueFactory.getQueue("smartDFPDataLoader");
        	queueMap.put("smartDFPDataLoader", queue);
        }
        TaskHandle handle =  queue.add(withUrl(taskURL)
	        .param("orderId",orderId)
	        .param("publisherIdInBQ",publisherBQId)
	        .param("dfpNetworkCode",networkCode)
	        .param("dfpTaskId",taskId)
         );
        try{
            return handle.getName();
            }catch(Exception e){
            	log.severe("ERROR while returning task name "+ e.getMessage());
            	return null;
            }
	}
	
	public static String addSmartDataLoaderTask(String taskURL, String startDate,String  endDate,
			String orderIds, String publisherIdInBQ, String networkCode, String loadType, boolean historical, String dfpTaskKey, Long entityId){
		log.info("Add task in smartDataUploader with taskURL:"+taskURL);
        Queue queue = queueMap.get("smartDFPDataLoader");
        if(queue == null){
        	queue = QueueFactory.getQueue("smartDFPDataLoader");
        	queueMap.put("smartDFPDataLoader", queue);
        }
        
        TaskHandle handle =  queue.add(withUrl(taskURL)
	        .param("startDate",startDate)
	        .param("endDate",endDate)
	        .param("orderIds",orderIds)
	        .param("publisherIdInBQ",publisherIdInBQ)
	        .param("networkCode",networkCode)
	        .param("loadType",loadType)
	        .param("merge",new Boolean(historical).toString())
	        .param("dfpTaskKey",dfpTaskKey)
	        .param("entityId", entityId.toString())
         );
        try{
            return handle.getName();
            }catch(Exception e){
            	log.severe("ERROR while returning task name "+ e.getMessage());
            	return null;
            }
	}
	
	public static String addFullHistoricalLoad(String taskURL){
		log.info("Add task in addFullHistoricalLoad with taskURL:"+taskURL);
        Queue queue = queueMap.get("smartDFPDataLoader");
        if(queue == null){
        	queue = QueueFactory.getQueue("smartDFPDataLoader");
        	queueMap.put("smartDFPDataLoader", queue);
        }
        TaskHandle handle =  queue.add(withUrl(taskURL));
        try{
            return handle.getName();
            }catch(Exception e){
            	log.severe("ERROR while returning task name "+ e.getMessage());
            	return null;
            }
	}
	
	public static String addBigQueryJobUpdateTask(String taskURL, String taskId, String publisherIdInBQ, String jobId){
		log.info("Add task in smartDataUploader with taskURL:"+taskURL);
        Queue queue = queueMap.get("bigQueryJobUpdater");
        if(queue == null){
        	queue = QueueFactory.getQueue("bigQueryJobUpdater");
        	queueMap.put("bigQueryJobUpdater", queue);
        }
        TaskHandle handle =  queue.add(withUrl(taskURL)
	        .param("taskId",taskId)
	        .param("publisherIdInBQ",publisherIdInBQ)
	        .param("jobId",jobId)
         );
        try{
        return handle.getName();
        }catch(Exception e){
        	log.severe("ERROR while returning task name "+ e.getMessage());
        	return null;
        }
	}

	
	public static void addTaskInDefaultQueue(String taskURL,String userId,String campaignId,String status,String planType){
		log.info("Add task in taskQueue with taskURL:"+taskURL+", userId:"+userId);
        
		Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }  
        
        queue.add(withUrl(taskURL)
    	        .param("userId", userId)
    	        .param("campaignId", campaignId)
    	        .param("status", status)
    	        .param("planType", planType));
	}
	
    public static void addLaunchCampaignTaskInQueue(String taskURL,String userId, String smartMediaPlanId,String orderIdStr){	
		
		String channelKey = userId;      
        log.info("Add task in taskQueue with taskURL:"+taskURL+", userId:"+userId);
        Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }   
        queue.add(withUrl(taskURL)
        		.param("key", channelKey)
    	        .param("userId", userId)
    	        .param("smartMediaPlanId", smartMediaPlanId)
    	        .param("orderId", orderIdStr));
     
	}
    
    public static void addCampaignSetupTaskInQueue(String taskURL,String userId, String smartMediaPlanId,String campaignId,String status){	
  	  String channelKey = userId;
      log.info("Add task in taskQueue with taskURL:"+taskURL+", userId:"+userId);
      Queue queue = queueMap.get("default");
      if(queue == null){
      	queue = QueueFactory.getDefaultQueue();
      	queueMap.put("default", queue);
      }   
      queue.add(withUrl(taskURL)
      		.param("key", channelKey)
  	        .param("userId", userId)
  	        .param("smartMediaPlanId", smartMediaPlanId)
  	        .param("campaignId", campaignId)
  	        .param("status", status));
       
  	}
    
    public static void addForUpdateMediaPlanInDefaultQueue(String taskURL,String userId,String campaignId,String status,String smartMediaPlanId){
		log.info("Add task in taskQueue with taskURL:"+taskURL+", userId:"+userId);
	    Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }   
        queue.add(withUrl(taskURL)
    	        .param("userId", userId)
    	        .param("campaignId", campaignId)
    	        .param("status", status)
    	        .param("smartMediaPlanId", smartMediaPlanId));
	}
    
    /*
	 * @author Naresh Pokhriyal
	 * This method will add task in 'updateCampaignDetailFromDFP' queue to update the campaign details from DFP
	 * @param String taskURL,String adServerId (DFP networkCode),String adServerUsername (DFP networkUsername)
	 */
    public static void updateCampaignDetailFromDFP(String taskURL,String adServerId){
		log.info("Add task in taskQueue with taskURL:"+taskURL+", adServerId:"+adServerId);
        Queue queue = queueMap.get("updateCampaignDetailFromDFP");
        if(queue == null){
        	queue = QueueFactory.getQueue("updateCampaignDetailFromDFP");
        	queueMap.put("updateCampaignDetailFromDFP", queue);
        }  
        queue.add(withUrl(taskURL)
    	        .param("adServerId", adServerId));
	}
    
    /*
     * @author Youdhveer Panwar
     * Add propductAdUnitsPerformance task in taskQueue with dfp networkCode as a parameter
     * task will fetch data from DFP 9based on networkCode) for these products adUnits and upload on BigQuery  
     */
    public static void addProductAdUnitsPerformanceTaskInQueue(String taskURL,String networkCode,String adUnitId,String month){
		log.info("Add task in taskfaultQueue with taskURL:"+taskURL+", networkCode :"
					+networkCode+", adUnitId:"+adUnitId+", month: "+month);
        
        Queue queue = queueMap.get("adUnitQueue");
        if(queue == null){
        	queue = QueueFactory.getQueue("adUnitQueue");
        	queueMap.put("adUnitQueue", queue);
        }  
        queue.add(withUrl(taskURL)
    	        .param("networkCode", networkCode)
    	        .param("adUnitId", adUnitId)
    	        .param("month", month));
	}
    
    /* @author Dheeraj Kumar
     * task will fetch data from data store and create search index for City
     */
    public static void uploadCityDataDefaultQueue(String taskURL,String offset, String limit){
		log.info("Add task in Queue with taskURL:"+taskURL+", offset :"+offset+" , limit : "+limit);
		Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }  
        queue.add(withUrl(taskURL)
    	        .param("offset", offset)
    	        .param("limit", limit));
	}
    
    /* @author Dheeraj Kumar
     * task will fetch data from data store and create search index for adUnit
     */
    public static void uploadAdUnitInQueue(String taskURL,String offset, String limit){
		log.info("Add task in Queue with taskURL:"+taskURL+", offset :"+offset+" , limit : "+limit);
        
        Queue queue = queueMap.get("textSearchQueue");
        if(queue == null){
        	queue = QueueFactory.getQueue("textSearchQueue");
        	queueMap.put("textSearchQueue", queue);
        }  
        queue.add(withUrl(taskURL)
    	        .param("offset", offset)
    	        .param("limit", limit));
	}
	
    
    public static void addDataLoadCronJob(String taskURL,String startDate,String endDate,String nonFinaliseTableId){
		log.info("Add task in Queue with taskURL:"+taskURL+", startDate :"+startDate 
				+",endDate : "+endDate+", nonFinaliseTableId:"+nonFinaliseTableId);
        
        Queue queue = queueMap.get("dailyDataUploader");
        if(queue == null){
        	queue = QueueFactory.getQueue("dailyDataUploader");
        	queueMap.put("dailyDataUploader", queue);
        }  
        queue.add(withUrl(taskURL)
    	        .param("startDate", startDate)
    	        .param("endDate", endDate)
    	        .param("nonFinaliseTableId", nonFinaliseTableId));
	}
    
    
    public static void addInventoryUploadTaskInDefaultQueue(String taskURL){
		log.info("Add task in taskQueue with taskURL:"+taskURL);
		Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getDefaultQueue();
        	queueMap.put("default", queue);
        }  
        queue.add(withUrl(taskURL));
	}
    
    public static void generateCampaignAlert(String taskURL, String orderId, String startDate, String endDate, String alertDate, String bqIdentifier){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	
    	Queue queue = queueMap.get("generateCampaignAlert");
        if(queue == null){
        	queue = QueueFactory.getQueue("generateCampaignAlert");
        	queueMap.put("generateCampaignAlert", queue);
        }      
    	queue.add(withUrl(taskURL)
    			.param("orderId", orderId)
    			.param("startDate", startDate)
    			.param("endDate", endDate)
    			.param("alertDate", alertDate)
    			.param("bqIdentifier", bqIdentifier));
    	
    }
    
    public static void loadHistoricalData(String taskURL, String orderId, String dfpNetworkCode){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	
    	Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getQueue("default");
        	queueMap.put("default", queue);
        }      
    	queue.add(withUrl(taskURL)
    			.param("orderId", orderId)
    			.param("dfpNetworkCode", dfpNetworkCode));
    	
    }
    
    public static void addDailyDataTask(String taskURL, String startDate, String endDate, String loadType, String taskType){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	
    	Queue queue = queueMap.get("default");
        if(queue == null){
        	queue = QueueFactory.getQueue("default");
        	queueMap.put("default", queue);
        }
        queue.add(withUrl(taskURL)
    			.param("startDate", startDate)
    			.param("loadType", loadType)
    			.param("endDate", endDate)
    			.param("taskType", taskType));
    }
    
    /*public static void dailyDataUpload(String taskURL, String startDate, String endDate, String orderIds, String loadTypeElem
    		, String dfpNetworkCode , String publisherIdInBQ , String publisherName , String mapKey){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	String taskQueueType = "";
    	
    	if(loadTypeElem!=null && loadTypeElem.equals(LinMobileConstants.LOAD_TYPE_CORE_PERFORMANCE)){
    		taskQueueType = "dailyDataLoadCorePerformanceQueue";
    	}else if(loadTypeElem!=null && loadTypeElem.equals(LinMobileConstants.LOAD_TYPE_CUSTOM_EVENT)){
    		taskQueueType = "dailyDataLoadCustomEventQueue";
    	}else if(loadTypeElem!=null && loadTypeElem.equals(LinMobileConstants.LOAD_TYPE_LOCATION)){
    		taskQueueType = "dailyDataLoadLocationQueue";
    	}else if(loadTypeElem!=null && loadTypeElem.equals(LinMobileConstants.LOAD_TYPE_RICH_MEDIA)){
    		taskQueueType = "dailyDataLoadRichMediaQueue";
    	}else if(loadTypeElem!=null && loadTypeElem.equals(LinMobileConstants.LOAD_TYPE_TARGET)){
    		taskQueueType = "dailyDataLoadTargetQueue";
    	}
    	
    	Queue queue = queueMap.get(taskQueueType);
        if(queue == null){
        	queue = QueueFactory.getQueue(taskQueueType);
        	queueMap.put(taskQueueType, queue);
        }      
    	queue.add(withUrl(taskURL)
    			.param("startDate", startDate)
    			.param("endDate", endDate)
    			.param("orderIds", orderIds.trim())
    			.param("loadTypeElem", loadTypeElem)
    			.param("dfpNetworkCode", dfpNetworkCode)
    			.param("publisherIdInBQ", publisherIdInBQ)
    			.param("publisherName", publisherName)
    			.param("mapKey", mapKey));
    	
    }*/
    
    public static String dailyDataUpload(String taskURL, String startDate, String endDate, String orderIds, String dfpNetworkCode , String publisherIdInBQ,
    		String loadTypeElem, boolean historical,String dfpTaskKey, Long entityId, String taskType){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	String taskQueueType = "";
    	if(taskType!=null && taskType.equals(LinMobileConstants.DAILY_TASK_TYPE)){
    		taskQueueType = "dailyDataLoadQueue";
    	}else if(taskType!=null && taskType.equals(LinMobileConstants.NON_FINALISE_TASK_TYPE)){
    		taskQueueType = "updateDailyNonFinaliseQueue";
    	}
    	
    	Queue queue = queueMap.get(taskQueueType);
        if(queue == null){
        	queue = QueueFactory.getQueue(taskQueueType);
        	queueMap.put(taskQueueType, queue);
        }  
        
        
        TaskHandle handle = queue.add(withUrl(taskURL)
    			.param("startDate", startDate)
    			.param("endDate", endDate)
    			.param("orderIds", orderIds.trim())
    			.param("loadType", loadTypeElem)
    			.param("networkCode", dfpNetworkCode)
    			.param("publisherIdInBQ", publisherIdInBQ)
    			.param("merge",new Boolean(historical).toString())
    			.param("dfpTaskKey",dfpTaskKey)
    			.param("entityId", entityId.toString()));
        
        try{
            return handle.getName();
            }catch(Exception e){
            	log.severe("ERROR while returning task name "+ e.getMessage());
            	return null;
            }
    	
    }
    
    public static void addProductForcast(String taskURL, String productId, String dmaId, String dmaName){
    	log.info("Add task in taskQueue with taskURL:"+taskURL);
    	
    	Queue queue = queueMap.get("productForcastQueue");
        if(queue == null){
        	queue = QueueFactory.getQueue("productForcastQueue");
        	queueMap.put("productForcastQueue", queue);
        }
        queue.add(withUrl(taskURL)
    			.param("productId", productId)
    			.param("dmaId", dmaId)
    			.param("dmaName", dmaName));
    }
    
}
