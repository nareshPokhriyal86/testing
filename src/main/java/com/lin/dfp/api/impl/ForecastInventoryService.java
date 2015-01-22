package com.lin.dfp.api.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import com.google.api.ads.dfp.jaxws.v201403.DeviceCategory;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.utils.v201403.DateTimes;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitTargeting;
import com.google.api.ads.dfp.jaxws.v201403.ApiException;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.CreativePlaceholder;
import com.google.api.ads.dfp.jaxws.v201403.CreativeRotationType;
import com.google.api.ads.dfp.jaxws.v201403.DeviceCapability;
import com.google.api.ads.dfp.jaxws.v201403.DeviceCapabilityTargeting;
import com.google.api.ads.dfp.jaxws.v201403.DeviceCategoryTargeting;
import com.google.api.ads.dfp.jaxws.v201403.Forecast;
import com.google.api.ads.dfp.jaxws.v201403.ForecastServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.GeoTargeting;
import com.google.api.ads.dfp.jaxws.v201403.InventoryTargeting;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.jaxws.v201403.Location;
import com.google.api.ads.dfp.jaxws.v201403.ReservationDetailsErrorReason;
import com.google.api.ads.dfp.jaxws.v201403.RoadblockingType;
import com.google.api.ads.dfp.jaxws.v201403.Size;
import com.google.api.ads.dfp.jaxws.v201403.StartDateTimeType;
import com.google.api.ads.dfp.jaxws.v201403.TargetPlatform;
import com.google.api.ads.dfp.jaxws.v201403.Targeting;
import com.google.api.ads.dfp.jaxws.v201403.TechnologyTargeting;
import com.google.api.ads.dfp.jaxws.v201403.UnitType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.collect.ImmutableList;
import com.lin.dfp.api.IForecastInventoryService;
import com.lin.persistance.dao.IProductDAO;
import com.lin.persistance.dao.impl.InventoryForecastingDAO;
import com.lin.persistance.dao.impl.ProductDAO;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.ProductForecastDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.impl.ProductService;
import com.lin.web.service.impl.SmartCampaignPlannerService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;

/**
 * This example gets a forecast for a prospective line item that targets the
 * entire network. The targeting can be modified to determine forecasts for
 * other criteria such as custom criteria targeting (in addition to targeting
 * the whole network).
 *
 * Tags: ForecastService.getForecast
 * Tags: NetworkService.getCurrentNetwork
 *
 * @author Youdhveer Panwar
 */
public class ForecastInventoryService implements IForecastInventoryService{
	
	  private static final String [] creativeSizeArray={"320x50", "300x50", "300x250", "728x90", "768x1024"};
      
	  public static Map<String,ForecastServiceInterface> forecastServiceMap=new HashMap<String,ForecastServiceInterface>();
	  
	  private static final Logger log = Logger.getLogger(ForecastInventoryService.class.getName());
	  
	   
	  
      public ForcastDTO loadForecastDataByAdUnit(DfpServices dfpServices, 
    		  DfpSession session, String adUnitId, String adUnitName,String start,String end) throws Exception{    	
		  
	    	String [] startDateArray=start.split("-");
	    	String [] endDateArray=end.split("-");
	    	log.info("start:"+start+" and end:"+end+" and adUnitId:"+adUnitId);
	    	ForecastServiceInterface forecastService=null;
	    	if(session!=null ){
	    		String key=session.getNetworkCode();
	    		if(forecastServiceMap.containsKey(key)){
	    			forecastService =forecastServiceMap.get(key);
	    		}else{
	    			forecastService=dfpServices.get(session, ForecastServiceInterface.class);
	    			forecastServiceMap.put(key, forecastService);
	    		}		        
	    		
	    	}else{
	    		throw new Exception("No dfp session found.");
	    	}
	    	
	    	
	    	 
	    	// Create inventory targeting.
	        InventoryTargeting inventoryTargeting = new InventoryTargeting();

	        // Create ad unit targeting for the root ad unit.
	        AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
	        // String adUnitId="32577222"; //"LIN.KASA";
		    adUnitTargeting.setAdUnitId(adUnitId);	 
	        adUnitTargeting.setIncludeDescendants(true);	        
	       
	        inventoryTargeting.getTargetedAdUnits().add(adUnitTargeting);
	        
	        // Create targeting.
	        Targeting targeting = new Targeting();
	        targeting.setInventoryTargeting(inventoryTargeting);

	        // Create a line item.
	        LineItem lineItem = new LineItem();
	        lineItem.setTargeting(targeting);
	        lineItem.setLineItemType(LineItemType.STANDARD);
	        // Set the roadblocking type.
	        lineItem.setRoadblockingType(RoadblockingType.ONE_OR_MORE);
	        // Set the creative rotation type.
	        lineItem.setCreativeRotationType(CreativeRotationType.OPTIMIZED);
	        
	       
	        // Create creative placeholder size.
		    List<Size> sizeList=loadCreativeSizes();
		    
	        // Create the creative placeholder.
		    List<CreativePlaceholder> creativeList=new ArrayList<CreativePlaceholder>();		    
		    for(Size size:sizeList){		    	
			    CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
			    creativePlaceholder.setSize(size);
	            creativeList.add(creativePlaceholder);
		    }
            //creativeList.		    
		    lineItem.getCreativePlaceholders().addAll(creativeList);

	        DateTime startDate=new DateTime(Integer.parseInt(startDateArray[0]),
	        		Integer.parseInt(startDateArray[1]),
	        		Integer.parseInt(startDateArray[2]),0,0);
		    DateTime endDate=new DateTime(Integer.parseInt(endDateArray[0]),
		    		Integer.parseInt(endDateArray[1]),
		    		Integer.parseInt(endDateArray[2]),23,59);		    
		   
		    lineItem.setStartDateTime(DateTimes.toDateTime(startDate));
		    lineItem.setEndDateTime(DateTimes.toDateTime(endDate));
		   
	        // Set the cost type.
	        lineItem.setCostType(CostType.CPM);

	        // Set the line item to use 50% of the impressions.
//	        lineItem.setUnitType(UnitType.IMPRESSIONS);
	        //lineItem.setUnitsBought(50L);

		    // Get forecast for line item.
		    Forecast forecast = forecastService.getForecast(lineItem);
		   
	    	long matched = forecast.getMatchedUnits();

		    
		    long reserved = forecast.getReservedUnits();
		    long delivered = forecast.getDeliveredUnits();
		    long available = forecast.getAvailableUnits();
		    long possible = forecast.getPossibleUnits();
		    
		    ForcastDTO forecastDTO=new ForcastDTO();
		    forecastDTO.setAvailableImpressions(available);
		    forecastDTO.setDeliveredImpressions(delivered);
		    forecastDTO.setPossibleImpressions(possible);
		    forecastDTO.setReservedImpressions(reserved);
		    forecastDTO.setMatchedImpressions(matched);
		    forecastDTO.setAdUnitId(adUnitId);
		    forecastDTO.setAdUnitName(adUnitName);		    
		   
		    
	    	return forecastDTO;
   }
	
      
   /*
    * This method will return forecasted inventory for targeted adUnitId
    * @author Youdhveer Panwar
    *    
    */
   public ForcastDTO loadForecastInventoryByAdUnit(DfpServices dfpServices, DfpSession session, String adUnitId, 
    		  String adUnitName,String startDate,String endDate,LineItemType lineItemType,CostType lineItemCostType,
    		  String [] creativeSizeArr) throws ApiException_Exception{    	
		  
	    	String [] startDateArray=startDate.split("-");
	    	String [] endDateArray=endDate.split("-");
	    	log.info("startDate:"+startDate+" and endDate:"+endDate+" and adUnitId:"+adUnitId);
	    	ForecastServiceInterface forecastService=dfpServices.get(session, ForecastServiceInterface.class);
	    	 
	    	// Create inventory targeting.
	        InventoryTargeting inventoryTargeting = new InventoryTargeting();

	        // Create ad unit targeting for the root ad unit.
	        AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
		    adUnitTargeting.setAdUnitId(adUnitId);	 
	        adUnitTargeting.setIncludeDescendants(true);	        
	       
	        inventoryTargeting.getTargetedAdUnits().add(adUnitTargeting);
	        
	        // Create targeting.
	        Targeting targeting = new Targeting();
	        targeting.setInventoryTargeting(inventoryTargeting);

	        // Create a line item.
	        LineItem lineItem = new LineItem();
	        lineItem.setTargeting(targeting);
	       
	        
	        if(lineItemType ==null || !( lineItemType==LineItemType.SPONSORSHIP || lineItemType==LineItemType.STANDARD ) ){
	        	ApiException apiException=new ApiException();
	        	apiException.setApplicationExceptionType(ReservationDetailsErrorReason.LINE_ITEM_TYPE_NOT_ALLOWED.toString());
	        	throw new ApiException_Exception("LINE_ITEM_TYPE_NOT_ALLOWED",apiException);
	        }
	        
	        lineItem.setLineItemType(lineItemType); // Should be LineItemType.SPONSORSHIP or LineItemType.STANDARD
	        // Set the roadblocking type.
	        //lineItem.setRoadblockingType(RoadblockingType.ONE_OR_MORE);
	        // Set the creative rotation type.
	        lineItem.setCreativeRotationType(CreativeRotationType.OPTIMIZED);
	        
	       
	        // Create creative placeholder size.
		    List<Size> sizeList=loadCreativeSizes(creativeSizeArr);
		    
	        // Create the creative placeholder.
		    List<CreativePlaceholder> creativeList=new ArrayList<CreativePlaceholder>();		    
		    for(Size size:sizeList){		    	
			    CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
			    creativePlaceholder.setSize(size);
	            creativeList.add(creativePlaceholder);
		    }
            //creativeList.		    
		    lineItem.getCreativePlaceholders().addAll(creativeList);

	        DateTime lineItemStartDate=new DateTime(Integer.parseInt(startDateArray[0]),
	        		Integer.parseInt(startDateArray[1]),
	        		Integer.parseInt(startDateArray[2]),0,0);
		    DateTime lineItemEndDate=new DateTime(Integer.parseInt(endDateArray[0]),
		    		Integer.parseInt(endDateArray[1]),
		    		Integer.parseInt(endDateArray[2]),23,59);		    
		   
		    lineItem.setStartDateTime(DateTimes.toDateTime(lineItemStartDate));
		    lineItem.setEndDateTime(DateTimes.toDateTime(lineItemEndDate));
		   
	        // Set the cost type.
	        lineItem.setCostType(lineItemCostType); //CostType.CPM

	        // Set the line item to use 50% of the impressions.
//	        lineItem.setUnitType(UnitType.IMPRESSIONS);
	        //lineItem.setUnitsBought(50L);
	        lineItem.setTargetPlatform(TargetPlatform.ANY);

		    // Get forecast for line item.
		    Forecast forecast = forecastService.getForecast(lineItem);
		   
	    	long matched = forecast.getMatchedUnits();
		    
		    long reserved = forecast.getReservedUnits();
		    long delivered = forecast.getDeliveredUnits();
		    long available = forecast.getAvailableUnits();
		    long possible = forecast.getPossibleUnits();
		    
		    ForcastDTO forecastDTO=new ForcastDTO();
		    forecastDTO.setAvailableImpressions(available);
		    forecastDTO.setDeliveredImpressions(delivered);
		    forecastDTO.setPossibleImpressions(possible);
		    forecastDTO.setReservedImpressions(reserved);
		    forecastDTO.setMatchedImpressions(matched);
		    forecastDTO.setAdUnitId(adUnitId);
		    forecastDTO.setAdUnitName(adUnitName);	
		    
	    	return forecastDTO;
   }
   
   public static void main(String[] args) throws ApiException_Exception {
	   AdUnitDTO au = new AdUnitDTO(); au.setId(32577222);
	/*   AdUnitDTO au1 = new AdUnitDTO(); au1.setId(32574702);
	   AdUnitDTO au2 = new AdUnitDTO(); au2.setId(32576862);
	   AdUnitDTO au3 = new AdUnitDTO(); au3.setId(32576742);
	   AdUnitDTO au4 = new AdUnitDTO(); au4.setId(54381822);*/
//	   44662564 and name:weather
	  // au.setId(44662564); au.setName("weather");
	//   au.setId(32577222); au.setName("lin.kasa");
	   List<AdUnitDTO> list = new ArrayList<AdUnitDTO>(); list.add(au); // list.add(au1);list.add(au2);list.add(au3);list.add(au4);
	   List<GeoTargetsObj> dmas = 
			   Arrays.asList(new GeoTargetsObj[]
					   {new GeoTargetsObj(790, "grandrapids"), 
					   new GeoTargetsObj(630, "birm"), 
					   new GeoTargetsObj(544, "portland"),
					   new GeoTargetsObj(563, "portland"),
					   new GeoTargetsObj(566, "portland"),
					   new GeoTargetsObj(635, "portland"),
					   new GeoTargetsObj(533, "portland"),
					   new GeoTargetsObj(527, "portland"),
					   new GeoTargetsObj(820, "portland"),
					   new GeoTargetsObj(819, "portland"),
					   new GeoTargetsObj(618, "portland"),
					   new GeoTargetsObj(511, "portland"),
					   new GeoTargetsObj(602, "portland"),
					   
					   }
					   );
	   List<DeviceObj> devices = new ArrayList<DeviceObj>();
	   devices.addAll(Arrays.asList(new DeviceObj[]{new DeviceObj(1, ""),new DeviceObj(2, ""), new DeviceObj(3, "")}));
//	 for(int i=0;i<10;i++){
	   ForcastDTO dto =  new ForecastInventoryService().loadForecastInventoryByAdUnit(
			   "5678", list, "2014-12-12", "2014-12-31", 
			   LineItemType.STANDARD, 
			   CostType.CPM, new String[]{"300x250","728x90", "320x50"}, 
			   //ForecastInventoryService.TARGETING_MOBILE_APP_ONLY, 
			   null, // in place of above
			   devices, 
			   null, 
			   dmas, 
			   null, 
			   null);
	   System.out.println("Matched "+ dto.getMatchedImpressions()+" Available: "+ dto.getAvailableImpressions());
//	 }
}
   public ForcastDTO loadForecastInventoryByAdUnit(String networkCode, List<AdUnitDTO> adUnits, 
		   String startDate,String endDate,LineItemType lineItemType,CostType lineItemCostType,
			String[] creativeSizeArr, Integer deviceCapability,
			List<DeviceObj> devices, 
			List<StateObj> states, 
			List<GeoTargetsObj> dmas, 
			List<CityDTO> cities, 
 List<ZipDTO> zips)
			throws ApiException_Exception {
	   log.info("============================================================================================================");
	   log.info("Starting forecast for new set of Ad Units");
	   log.info("============================================================================================================");
		String[] startDateArray = startDate.split("-");
		String[] endDateArray = endDate.split("-");
		log.info("startDate:" + startDate + " and endDate:" + endDate
				+ " and adUnits DTO:" + adUnits.size() + ", netWorkCode:"
				+ networkCode);
		ForcastDTO forecastDTO = null;
		String memcacheKey = "";
		for (AdUnitDTO adUnitDTO : adUnits) {
			memcacheKey = memcacheKey + "_" + adUnitDTO.getId();
		}
		memcacheKey = memcacheKey + "_" + startDate + "_" + endDate + "_"
				+ networkCode;
		log.info("Load forecast data from cache ..key:" + memcacheKey);
		forecastDTO = (ForcastDTO) MemcacheUtil.getObjectFromCache(memcacheKey);
		if (forecastDTO == null) {
			DfpSession dfpSession;
			try {
			dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode);
				 
			/*	   GoogleCredential credential = new GoogleCredential.Builder()
				   .setTransport(new NetHttpTransport()) .setJsonFactory(new
				   GsonFactory())
				   .setServiceAccountId(LinMobileVariables.SERVICE_ACCOUNT_EMAIL
				   ) .setServiceAccountScopes(
				   ImmutableList.of("https://www.googleapis.com/auth/dfp"))
				   .setServiceAccountPrivateKeyFromP12File( new File(
				   "C:/Users/user/git/linmobile-dev/src/main/webapp/keys/"
				   + LinMobileVariables.SERVICE_ACCOUNT_KEY)) .build();
				   credential.refreshToken();
				   
				   // Construct a DfpSession. 
				   dfpSession =  
				   new DfpSession.Builder() .withNetworkCode("5678")
				   .withApplicationName(
				  LinMobileConstants.LIN_DIGITAL_DFP_APPLICATION_NAME)
				   .withOAuth2Credential(credential).build();*/
			log.info("Successfully obtained dfpSession");	  	

				DfpServices dfpServices = LinMobileProperties.getInstance().getDfpServices();

				log.info("Not found in cache, load from dfp using forecasting service..");
				ForecastServiceInterface forecastService = dfpServices.get(dfpSession, ForecastServiceInterface.class);
				log.info("Received the DFP's forecastServiceInterface Instance");
				// Create inventory targeting.
				InventoryTargeting inventoryTargeting = new InventoryTargeting();
				String allAdUnitId = "";
				String allAdUnitNames = "";
				int index = 0;
				for (AdUnitDTO adUnitDTO : adUnits) {
					String adUnitId = adUnitDTO.getId() + "";
					String adUnitName = adUnitDTO.getName();

					// Create ad unit targeting for the root ad unit.
					AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
					adUnitTargeting.setAdUnitId(adUnitId);
					adUnitTargeting.setIncludeDescendants(true);
					inventoryTargeting.getTargetedAdUnits().add(adUnitTargeting);
					log.info("Added Ad Unit to inventory targeting. ID=["+adUnitId+"] and Name = ["+adUnitName+"]");
					if (index == 0) {
						allAdUnitNames = allAdUnitNames + adUnitName;
						allAdUnitId = allAdUnitId + adUnitId;
					} else {
						allAdUnitNames = allAdUnitNames + "," + adUnitName;
						allAdUnitId = allAdUnitId + "," + adUnitId;
					}
					index++;
				}
				log.info("Added adunit targeting to inventory. Total size: "
						+ index);

				// Technology Targeting
				TechnologyTargeting technologyTargeting = new TechnologyTargeting();
				ReportUtil.setDeviceCapability(technologyTargeting,deviceCapability);
				setDeviceCategories(technologyTargeting, devices);

				// Create targeting.
				Targeting targeting = new Targeting();
				targeting.setInventoryTargeting(inventoryTargeting);
				setGeoTargeting(targeting, states, dmas, cities, zips);

				if (technologyTargeting.getDeviceCapabilityTargeting() != null || technologyTargeting.getDeviceCategoryTargeting() != null) {
					log.info("Setting technology targeting.");
					targeting.setTechnologyTargeting(technologyTargeting);
				}

				// Create a line item.
				LineItem lineItem = new LineItem();
				lineItem.setTargeting(targeting);

				if (lineItemType == null || !(lineItemType == LineItemType.SPONSORSHIP || lineItemType == LineItemType.STANDARD)) {
					ApiException apiException = new ApiException();
					apiException.setApplicationExceptionType(ReservationDetailsErrorReason.LINE_ITEM_TYPE_NOT_ALLOWED.toString());
					throw new ApiException_Exception("LINE_ITEM_TYPE_NOT_ALLOWED", apiException);
				}

				lineItem.setLineItemType(lineItemType);
				// Set the roadblocking type.
				lineItem.setRoadblockingType(RoadblockingType.ONE_OR_MORE);
				// Set the creative rotation type.
				lineItem.setCreativeRotationType(CreativeRotationType.OPTIMIZED);

				// Create creative placeholder size.
				List<Size> sizeList = loadCreativeSizes(creativeSizeArr);
				log.info("Creative list size:" + sizeList);

				// Create the creative placeholder.
				List<CreativePlaceholder> creativeList = new ArrayList<CreativePlaceholder>();
				for (Size size : sizeList) {
					CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
					creativePlaceholder.setSize(size);
					creativeList.add(creativePlaceholder);
				}

				// creativeList.
				lineItem.getCreativePlaceholders().addAll(creativeList);
				log.info("Added creative placeholders");

				DateTime lineItemStartDate = new DateTime(
						Integer.parseInt(startDateArray[0]),
						Integer.parseInt(startDateArray[1]),
						Integer.parseInt(startDateArray[2]), 0, 0);
				DateTime lineItemEndDate = new DateTime(
						Integer.parseInt(endDateArray[0]),
						Integer.parseInt(endDateArray[1]),
						Integer.parseInt(endDateArray[2]), 23, 59);

				lineItem.setStartDateTime(DateTimes
						.toDateTime(lineItemStartDate));
				lineItem.setEndDateTime(DateTimes.toDateTime(lineItemEndDate));

				log.info("Added start/end time");
				// Set the cost type.
				lineItem.setCostType(lineItemCostType); // CostType.CPM

				lineItem.setUnitType(UnitType.IMPRESSIONS);
				lineItem.setPriority(8);

				// added for sponsership type.
				if (lineItem.getLineItemType().equals(LineItemType.SPONSORSHIP)) {
					log.info("Found sponsorship itemtype. Changing priority and setting goals to 100%.");
					lineItem.setUnitsBought(100L);
					lineItem.setPriority(4);
				}
				// lineItem.setUnitsBought(50L);
				lineItem.setTargetPlatform(TargetPlatform.ANY);
				lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
				log.info("Going to hit forecast service.");
				// Get forecast for line item.
				Forecast forecast = forecastService.getForecast(lineItem);
				log.info("Forecast service result receieved.");

				long matched = forecast.getMatchedUnits();

				long reserved = forecast.getReservedUnits();
				long delivered = forecast.getDeliveredUnits();
				long available = forecast.getAvailableUnits();
				long possible = forecast.getPossibleUnits();

				forecastDTO = new ForcastDTO();
				forecastDTO.setAvailableImpressions(available);
				forecastDTO.setDeliveredImpressions(delivered);
				forecastDTO.setPossibleImpressions(possible);
				forecastDTO.setReservedImpressions(reserved);
				forecastDTO.setMatchedImpressions(matched);
				forecastDTO.setAdUnitId(allAdUnitId);
				forecastDTO.setAdUnitName(allAdUnitNames);
				log.info("Obtained forecast dto. ToString is : \n "
						+ forecastDTO.toString());
				MemcacheUtil.setObjectInCache(memcacheKey, forecastDTO);
			} catch (Exception e) {
				log.severe("dfpSession exception -- " + e.getMessage());
			}

		} else {
			log.info("return forecasting data from cache with key:"
					+ memcacheKey);
		}
		return forecastDTO;
	}
   



/*
    * This method will return forecasted inventory for targeted adUnitId
    * @author Youdhveer Panwar
    *    
    */
   public Map<String,ForcastDTO> loadForecastInventoryByAdUnit(DfpServices dfpServices, DfpSession session,
		   List<ProductsObj> productList, String startDate,String endDate,
		   LineItemType lineItemType,CostType lineItemCostType) throws ApiException_Exception{    	
		  
	    	
	    	Map<String,ForcastDTO> forecastDTOMap=new HashMap<String, ForcastDTO>();
	    	return forecastDTOMap;
   }
   
   
   public List<Size> loadCreativeSizes(List<CreativeObj> creativeList){
		  List<Size> sizeList=new ArrayList<Size>();
		  if(creativeList==null || creativeList.size()==0){
			  log.info("No creative size list available, load default...");
			  sizeList=loadCreativeSizes();
		  }else{
			  for(CreativeObj creative:creativeList){
				  String creativeSize=creative.getSize();
				  String [] dim=creativeSize.split("[xX]");
				  Size size=new Size();
				  size.setWidth(Integer.parseInt(dim[0]));
				  size.setHeight(Integer.parseInt(dim[1]));
				  size.setIsAspectRatio(false);
				  sizeList.add(size);
			  }	
		  }		  	 
		  return sizeList;
   }
   
   public List<Size> loadCreativeSizes(String [] creativeSizeArr){
		  List<Size> sizeList=new ArrayList<Size>();	
		  if(creativeSizeArr ==null || creativeSizeArr.length==0){
			  creativeSizeArr=creativeSizeArray;
		  }
		  for(int i=0;i<creativeSizeArr.length;i++){
			  try{
			  String [] dim=creativeSizeArr[i].split("[xX]");
			  Size size=new Size();
			  size.setWidth(Integer.parseInt(dim[0]));
			  size.setHeight(Integer.parseInt(dim[1]));
			  size.setIsAspectRatio(false);
			  sizeList.add(size);
			  }catch(NumberFormatException nfe){
				  log.severe("Invalid creative size found. Skipping ["+creativeSizeArr[i]+"]");
			  }
		  }		  
		  return sizeList;
   }
   
   public List<Size> loadCreativeSizes(){
		  List<Size> sizeList=new ArrayList<Size>();	
		  
		  for(int i=0;i<creativeSizeArray.length;i++){
			  String [] dim=creativeSizeArray[i].split("x");
			  Size size=new Size();
			  size.setWidth(Integer.parseInt(dim[0]));
			  size.setHeight(Integer.parseInt(dim[1]));
			  size.setIsAspectRatio(false);
			  sizeList.add(size);
		  }
		  
		  return sizeList;
  }
   
   public List<ForcastInventoryObj> deleteUpdateForcastInventoryObj(List<ForcastDTO> inventoryList,
		   String startDate,String endDate, boolean doEmptyTable){
	   List<DFPSitesWithDMAObj> dfpSitesWithDMAObjsList = new ArrayList<>();
	   List<ForcastInventoryObj> forcastInventoryObjsList = new ArrayList<>();
	   Map<String,DFPSitesWithDMAObj> DFPSitesWithDMAObjMap  = new HashMap<>();
	   InventoryForecastingDAO dao = new InventoryForecastingDAO();
	   try{
		   dfpSitesWithDMAObjsList = dao.getAllDFPSitesWithDMA();
		   for (DFPSitesWithDMAObj dfpSitesWithDMAObj : dfpSitesWithDMAObjsList) {
			   if(dfpSitesWithDMAObj!=null && dfpSitesWithDMAObj.getDFPPropertyName()!=null && !dfpSitesWithDMAObj.getDFPPropertyName().equals("") ){
				   DFPSitesWithDMAObjMap.put(dfpSitesWithDMAObj.getDFPPropertyName(), dfpSitesWithDMAObj);
			   }
		    }
		   for (ForcastDTO forcastDTOObj : inventoryList) {
			   if(forcastDTOObj!=null && DFPSitesWithDMAObjMap!=null){
				   if(DFPSitesWithDMAObjMap.containsKey(forcastDTOObj.getAdUnitName())){
					   DFPSitesWithDMAObj dmaObj = new DFPSitesWithDMAObj();
					   ForcastInventoryObj forcastInventoryObj = new ForcastInventoryObj();
					   dmaObj = DFPSitesWithDMAObjMap.get(forcastDTOObj.getAdUnitName());
					   if(dmaObj!=null){
						   forcastInventoryObj.setCode(dmaObj.getCode()+"");
						   if(dmaObj.getAddress()!=null){
							   forcastInventoryObj.setAddress(dmaObj.getAddress());
						   }
						   if(dmaObj.getState()!=null){
							   forcastInventoryObj.setState(dmaObj.getState());
						   }
						   if(dmaObj.getZipCode()!=null){
							   forcastInventoryObj.setZipCode(dmaObj.getZipCode());
						   }
						   if(dmaObj.getName()!=null){
							   forcastInventoryObj.setName(dmaObj.getName());
						   }
						   if(dmaObj.getPropertyName()!=null){
							   forcastInventoryObj.setPropertyName(dmaObj.getPropertyName());
						   }
						   if(dmaObj.getPublisherId()!=null){
							   forcastInventoryObj.setPublisherId(dmaObj.getPublisherId());
						   }
						   if(dmaObj.getPublisherName()!=null){
							   forcastInventoryObj.setPublisherName(dmaObj.getPublisherName());
						   }
						  if(forcastDTOObj.getAdUnitName()!=null){
							  forcastInventoryObj.setDFPPropertyName(forcastDTOObj.getAdUnitName());  
						  }
						  if(forcastDTOObj.getAdUnitId()!=null){
							  forcastInventoryObj.setAdUnitId(forcastDTOObj.getAdUnitId());
						  }
						  				  
						  forcastInventoryObj.setAvailableImpressions(forcastDTOObj.getAvailableImpressions());
						  forcastInventoryObj.setReservedImpressions(forcastDTOObj.getReservedImpressions());
						  forcastInventoryObj.setForcastedImpressions(forcastDTOObj.getPossibleImpressions());
						  forcastInventoryObj.setId(dmaObj.getDFPPropertyName()+":"+dmaObj.getCode()+""+":"+dmaObj.getPublisherId()+":"+forcastDTOObj.getAdUnitId()+":"+startDate+":"+endDate);
					  if(startDate!=null){
						  forcastInventoryObj.setStartDate(startDate);
					  }
					  if(endDate!=null){
						  forcastInventoryObj.setEndDate(endDate);
					  }
					  forcastInventoryObjsList.add(forcastInventoryObj);
				   }
			   }
				  
			   }
			   
		}
		   dao.deleteUpdateForcastInventoryObj(forcastInventoryObjsList, doEmptyTable); 
	   }catch(Exception e){
		   log.severe("Exception in deleteUpdateDFPSitesWithDMA "+e.getMessage());
		   
	   }
	return forcastInventoryObjsList;
   }

   
	public NewAdvertiserViewDTO getLineItemForcast(DfpServices dfpServices,DfpSession session,String lineItemId,String bookedImp,String deliveredImp){
    	log.info("inside getLineItemForcast of NewAdvertiserViewService service");
    	NewAdvertiserViewDTO newAdvertiserViewDTO = new NewAdvertiserViewDTO();
    	Forecast forecast = null;
    	 long deliveredImpressions = 0;
    	 long bookedImpressions = 0;
    	 long impressionsToBeDelivered = 0;
    	 ForecastServiceInterface forecastService = null;
    	 try{
    	    forecastService = dfpServices.get(session, ForecastServiceInterface.class);
            log.info("before getting lineitem forcast data");
    	    forecast =  forecastService.getForecastById(Long.valueOf(lineItemId));
    	    log.info("after forcast api "+forecast.getId());
    	   // forecast =  forecastService.getForecastById((long) 39152982);
              if(forecast!=null){
        	     if(forecast.getId()!=null){
        	    	 newAdvertiserViewDTO.setLineItemId(forecast.getId().toString());
        		   log.info(forecast.getId().toString());
            	}
        	  
            if(bookedImp==null){
            	bookedImpressions = 0;
            }else{
            	bookedImpressions = Long.parseLong(bookedImp);
            }
            
            if(deliveredImp==null){
            	deliveredImpressions = 0;
            }else{
            	deliveredImpressions = Long.parseLong(deliveredImp);
            }
        	  impressionsToBeDelivered = bookedImpressions - deliveredImpressions;
        	System.out.println("impressionsToBeDelivered "+ impressionsToBeDelivered);
        	newAdvertiserViewDTO.setZ2forcastStatus("false");
        	if(forecast.getAvailableUnits()!=null && impressionsToBeDelivered>=0 ){
        		System.out.println("in first if "+forecast.getAvailableUnits());
        		if(forecast.getAvailableUnits()>=impressionsToBeDelivered){
        			System.out.println("in second if");
        			newAdvertiserViewDTO.setZ2forcastStatus("true");
        		}
        	}
        	
        }
		    forecastService = null;
        
    	}catch(Exception e){
	    	 //lineItemService = null;
	    	 forecastService = null;
    		log.severe("Exception in getLineItemForcast of NewAdvertiserViewService service"+e.getMessage());
    		
    	}
		return newAdvertiserViewDTO;
        
   }
	
   private void setDeviceCategories(TechnologyTargeting targeting, List<DeviceObj> devices){
	  
	  if(devices == null || devices.size() == 0){ return; }
	  log.info("Setting device targeting categories. Total list size is: "+ devices.size());
	  DeviceCategoryTargeting deviceCatTar = new DeviceCategoryTargeting();
	   DeviceCategory desktopCategory = new DeviceCategory();
	   DeviceCategory mobileHighCategory = new DeviceCategory();
	   DeviceCategory mobileMidCategory = new DeviceCategory();
	   DeviceCategory tabletCategory = new DeviceCategory();
	   desktopCategory.setId(30000L);
	   mobileHighCategory.setId(30001L);
	   mobileMidCategory.setId(30003L);
	   tabletCategory.setId(30002L);
	   boolean deviceAdded = false;
	  if(devices.size() == 1 && devices.get(0).getId() == Long.parseLong(ProductService.allOptionId)){
		  deviceCatTar.getTargetedDeviceCategories().addAll(Arrays.asList(new DeviceCategory[]{desktopCategory, mobileHighCategory, mobileMidCategory, tabletCategory}));
		  deviceAdded = true;
		  log.info("setting device targeting for ALL option ");
	  }else if(devices.size() == 1 && devices.get(0).getId() == Long.parseLong(ProductService.noneOptionId)){
		  log.warning("None option found while setting device categories in targeting for dfp forecasting");
		 // deviceCatTar.getExcludedDeviceCategories().addAll(Arrays.asList(new DeviceCategory[]{desktopCategory, mobileHighCategory, mobileMidCategory, tabletCategory}));
		  return;
	  }else{
		  for(DeviceObj device: devices){
			  if(device.getId() == 1){
				  deviceCatTar.getTargetedDeviceCategories().addAll(Arrays.asList(new DeviceCategory[]{mobileHighCategory, mobileMidCategory}));
				  deviceAdded = true;
				  log.info("Added mobile device targeting for forecasting");
			  }else if(device.getId() == 2){
				  deviceCatTar.getTargetedDeviceCategories().add(tabletCategory);
				  deviceAdded = true;
				  log.info("Added tablet device targeting for forecasting ");
			  }else if(device.getId() == 3){
				  deviceCatTar.getTargetedDeviceCategories().add(desktopCategory);
				  deviceAdded = true;
				  log.info("Added desktop targeting for forecasting");
			  }
		  }
	  }
	  if(deviceAdded){
		  targeting.setDeviceCategoryTargeting(deviceCatTar);
		  log.info("Added devicetargeting to technology targeting.");
	  }
   }	
 
   
   private void setGeoTargeting(Targeting targeting, List<StateObj> states, List<GeoTargetsObj> dmas, List<CityDTO> cities, List<ZipDTO> zips) {
 	   try{
	   GeoTargeting geoTargeting = new GeoTargeting();
 	   
	   List<Location> locations = new ArrayList<Location>();
	   if(states != null && states.size() > 0){
		   for(StateObj state: states){
			   if(state.getId() ==  Long.parseLong(ProductService.allOptionId) || state.getId() == Long.parseLong(ProductService.noneOptionId)){
				   continue;
			   }
			   Location location = new Location();
			   location.setId(state.getId());
			   locations.add(location);
			   log.info("Added state to the geoTargeting with ID=["+state.getId()+"]");
		   }
	   }
	   /*
	   if(cities != null && cities.size() > 0){
		   for(CityDTO city : cities){
			   Location location = new Location();
			   location.setId(city.getId());
			   locations.add(location);
		   }
	   }*/
	   
	   if(dmas != null && dmas.size() > 0){
		   for(GeoTargetsObj dma: dmas){
			   try{
				   if(dma.getGeoTargetId() ==  Long.parseLong(ProductService.allOptionId) || dma.getGeoTargetId()  == Long.parseLong(ProductService.noneOptionId)){
					   continue;
				   }
				   Location location = new Location();
				   // We have 405 as DMA id in datastore. Corresponding to that DFP has 200405 as criteria id of that dma. it adds 200
				   location.setId(Long.parseLong("200"+dma.getGeoTargetId()));
				   locations.add(location);
				   log.info("Added DMA to the geoTargeting with ID = ["+dma.getGeoTargetId()+"]");
			   }catch(NumberFormatException nfe){
				   log.severe("Error while setting dma with id: ["+dma.getGeoTargetId()+"]");
			   }
		   }
	   }
	   
	   if(locations.size() > 0){
		   geoTargeting.getTargetedLocations().addAll(locations);
		   targeting.setGeoTargeting(geoTargeting);
	   }
 	   }catch(Exception e){
 		   log.severe("Error while setting geo targetin in forecast. "+ e.getMessage());
 	   }
   }    
   
   @Override
   /**
    * 
    * 
    */
   public void addTaskToGetProductForcast(){
	   log.info("inside addTaskToGetProductForcast ...");
	   List<ProductsObj> allProductList = new ArrayList<>();
	   try{
		   allProductList = new ProductDAO().getAllProducts();
		   if(allProductList!=null && allProductList.size()>0){
			   log.info("No of products found = "+allProductList.size());
			   for (ProductsObj productsObj : allProductList) {
				   if(productsObj!=null && productsObj.getDmas()!=null && productsObj.getDmas().size()>0){
					   if(productsObj.getDmas().size()==1 
							   && (productsObj.getDmas().get(0).getGeoTargetId()==StringUtil.getLongValue(ProductService.allOptionId)
							   || productsObj.getDmas().get(0).getGeoTargetId()==StringUtil.getLongValue(ProductService.noneOptionId)) ){
						   GeoTargetsObj geoTargetsObj = productsObj.getDmas().get(0);
						   if(geoTargetsObj.getGeoTargetId()== StringUtil.getLongValue(ProductService.noneOptionId)){
							   continue;
						   }else if(geoTargetsObj.getGeoTargetId()== StringUtil.getLongValue(ProductService.allOptionId)){
							   List<GeoTargetsObj> geoTargetsObjList = new ProductDAO().getAllGeoTargetsObj();
							   if(geoTargetsObjList!=null && geoTargetsObjList.size()>0){
								   for (GeoTargetsObj geoObj : geoTargetsObjList) {
									if(geoObj!=null){
										TaskQueueUtil.addProductForcast("/getProductForcast.lin", String.valueOf(productsObj.getProductId()), 
												String.valueOf(geoObj.getGeoTargetId()), geoObj.getGeoTargetsName());
									}
								}
							   }
						   }
					   }else{
						   for (GeoTargetsObj geoObj : productsObj.getDmas()) {
							   if(geoObj!=null){
								   TaskQueueUtil.addProductForcast("/getProductForcast.lin", String.valueOf(productsObj.getProductId()), 
											String.valueOf(geoObj.getGeoTargetId()), geoObj.getGeoTargetsName());
							   }
						   }
					   }
				   }
			   }
		   }
	   }catch(Exception e){
		   log.severe("Exception in addTaskToGetProductForcast ::"+e.getMessage());
		   
	   }
	   
   }
   
   @Override
   public boolean setProductFrocast(String productId, String dmaId, String dmaName){
	   log.info("inside setProductFrocast ...");
	   	
	   IProductDAO productDAO = new ProductDAO();
	   ProductsObj productsObj = new ProductsObj();
	   GeoTargetsObj geoTargetsObj = null;
	   List<AdUnitDTO> adUnitDTOList = new ArrayList<>();
	   try{
		   
		   productsObj = productDAO.getProductById(StringUtil.getLongValue(productId.trim()));
		   if(dmaId!=null && dmaName!=null){
			    geoTargetsObj = new GeoTargetsObj(StringUtil.getLongValue(dmaId), dmaName);
			    log.info("GeoTargetsObj created with dmaId = "+dmaId+" and dmaName = "+dmaName);
		   }
		   
		   if(productsObj!=null){
			   
			   Date startDate = DateUtil.getDateYYYYMMDD();
			   Date endDate = null;
			   
			   LineItemType lineItemType = LineItemType.SPONSORSHIP;
			   CostType lineItemCostType = CostType.CPM;
			   int[] productForcastDays = LinMobileConstants.PRODUCT_FORCAST_DAY;
			   ProductForecastObj productForcastObj = new ProductForecastObj();
			   String [] creativeSizeArr= getCreativeArr(productsObj.getCreative());
			   
			   if(productForcastDays!=null && productForcastDays.length>0){
				   log.info("productForcastDays length = "+productForcastDays.length);
				   for(int i=0;i<productForcastDays.length;i++){

					   endDate = DateUtil.getDateBeforeAfterDays(productForcastDays[i]);
					   ProductForecastDTO forcastProductDTO = new ProductForecastDTO();
					   List<GeoTargetsObj> geoTargetsObjList = new ArrayList<>();
				
					   if(geoTargetsObj!=null){
						   geoTargetsObjList.add(geoTargetsObj);
					   }
					   
					   if(productsObj.getAdUnits()!=null && productsObj.getAdUnits().size()>0){
						   adUnitDTOList = productsObj.getAdUnits();
					   }
					   
					   forcastProductDTO = loadForecastInventoryByProduct(productsObj.getPublisherId(), adUnitDTOList, startDate, endDate,
							   lineItemType, lineItemCostType, creativeSizeArr, productsObj.getDeviceCapability(), 
							   productsObj.getDevices(), productsObj.getStates(),geoTargetsObjList, productsObj.getCities(), productsObj.getZips());
					   
					   if(forcastProductDTO!=null){
						   productForcastObj.setId(productsObj.getProductId()+"_"+geoTargetsObj.getGeoTargetId()+"_"+productForcastDays[i]); 
						   productForcastObj.setProductId(productsObj.getProductId());
						   productForcastObj.setProductName(productsObj.getProductName());
						   productForcastObj.setPartnerId(productsObj.getPartnerId());
						   productForcastObj.setPublisherName(productsObj.getPublisherName());
						   productForcastObj.setForcaProductDTO(forcastProductDTO);
						   productForcastObj.setDaysType(productForcastDays[i]);
						   // Added by Anup
						   productForcastObj.setDmaDetail(geoTargetsObj);
						   productDAO.saveObject(productForcastObj);
					   }
				   }
			   }
		   }else{
			   log.info("no product found for id "+productId);
		   }
		  
	   }catch(Exception e){
		   log.severe("Exception in setProductFrocast ::"+e.getMessage());
		   
		   return false;
	   }
	   return true;
	   
		}
   private String[] getCreativeArr(List<CreativeObj> creativeObjList) throws Exception{
	   String [] creativeSizeArr=null;
	   List<String> creativeSizeList=new ArrayList<String>();
	   if(creativeObjList !=null && creativeObjList.size()>0){				
		   for(CreativeObj creative:creativeObjList){
			   String name=creative.getSize();
			   creativeSizeList.add(name);					
		   }
		   creativeSizeArr=creativeSizeList.toArray(new String[creativeSizeList.size()]);
		   log.info("creativeSizeArr length = "+creativeSizeArr.length);
	   }
	return creativeSizeArr;
   }
   
   private ProductForecastDTO loadForecastInventoryByProduct(String networkCode, List<AdUnitDTO> adUnits, 
		   Date startDate,Date endDate,LineItemType lineItemType,CostType lineItemCostType,
			String[] creativeSizeArr, Integer deviceCapability,
			List<DeviceObj> devices, List<StateObj> states, List<GeoTargetsObj> dmas, List<CityDTO> cities, List<ZipDTO> zips) throws ApiException_Exception {
	  
	    String start = DateUtil.getDateAsString(startDate, "yyyy-MM-dd");
	    String end= DateUtil.getDateAsString(endDate, "yyyy-MM-dd");
		String[] startDateArray = start.split("-");
		String[] endDateArray = end.split("-");
		log.info("startDate:" + startDate + " and endDate:" + endDate + " and adUnits DTO:" + adUnits.size() + ", netWorkCode:" + networkCode);
		ProductForecastDTO forcastProductDTO = null;
		if (forcastProductDTO == null) {
			DfpSession dfpSession;
			try {
 				dfpSession = DFPAuthenticationUtil.getDFPSession(networkCode);
				DfpServices dfpServices = LinMobileProperties.getInstance().getDfpServices();

				log.info("Not found in cache, load from dfp using forecasting service..");
				ForecastServiceInterface forecastService = dfpServices.get(dfpSession, ForecastServiceInterface.class);
				// Create inventory targeting.
				InventoryTargeting inventoryTargeting = new InventoryTargeting();
				String allAdUnitId = "";
				String allAdUnitNames = "";
				int index = 0;
				if(adUnits!=null && adUnits.size()>0){
					for (AdUnitDTO adUnitDTO : adUnits) {
						String adUnitId = adUnitDTO.getId() + "";
						String adUnitName = adUnitDTO.getName();
						
						// Create ad unit targeting for the root ad unit.
						AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
						adUnitTargeting.setAdUnitId(adUnitId);
						adUnitTargeting.setIncludeDescendants(true);
						inventoryTargeting.getTargetedAdUnits().add(adUnitTargeting);

						if (index == 0) {
							allAdUnitNames = allAdUnitNames + adUnitName;
							allAdUnitId = allAdUnitId + adUnitId;
						} else {
							allAdUnitNames = allAdUnitNames + "," + adUnitName;
							allAdUnitId = allAdUnitId + "," + adUnitId;
						}
						index++;
					}
					log.info("Added adunit targeting to inventory. Total size: "+ index);
				}
	

				// Technology Targeting
				TechnologyTargeting technologyTargeting = new TechnologyTargeting();
				ReportUtil.setDeviceCapability(technologyTargeting, deviceCapability);
				setDeviceCategories(technologyTargeting, devices);
				
				// Create targeting.
				Targeting targeting = new Targeting();
				targeting.setInventoryTargeting(inventoryTargeting);
				
				setGeoTargeting(targeting, states, dmas, cities, zips);
				
				if(technologyTargeting.getDeviceCapabilityTargeting() != null || technologyTargeting.getDeviceCategoryTargeting() != null) {
					log.info("Setting technology targeting.");
					targeting.setTechnologyTargeting(technologyTargeting);
				}

				// Create a line item.
				LineItem lineItem = new LineItem();
				lineItem.setTargeting(targeting);

				if (lineItemType == null || !(lineItemType == LineItemType.SPONSORSHIP || lineItemType == LineItemType.STANDARD)) {
					ApiException apiException = new ApiException();
					apiException.setApplicationExceptionType(ReservationDetailsErrorReason.LINE_ITEM_TYPE_NOT_ALLOWED.toString());
					throw new ApiException_Exception("LINE_ITEM_TYPE_NOT_ALLOWED", apiException);
				}

				lineItem.setLineItemType(lineItemType);  
				// Set the roadblocking type.
				lineItem.setRoadblockingType(RoadblockingType.ONE_OR_MORE);
				// Set the creative rotation type.
				lineItem.setCreativeRotationType(CreativeRotationType.OPTIMIZED);

				// Create creative placeholder size.
				List<Size> sizeList = loadCreativeSizes(creativeSizeArr);
				log.info("Creative list size:" + sizeList);

				// Create the creative placeholder.
				List<CreativePlaceholder> creativeList = new ArrayList<CreativePlaceholder>();
				for (Size size : sizeList) {
					CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
					creativePlaceholder.setSize(size);
					creativeList.add(creativePlaceholder);
				}
				
				// creativeList.
				lineItem.getCreativePlaceholders().addAll(creativeList);
				log.info("Added creative placeholders");
				
				DateTime lineItemStartDate = new DateTime(
						Integer.parseInt(startDateArray[0]),
						Integer.parseInt(startDateArray[1]),
						Integer.parseInt(startDateArray[2]), 0, 0);
				DateTime lineItemEndDate = new DateTime(
						Integer.parseInt(endDateArray[0]),
						Integer.parseInt(endDateArray[1]),
						Integer.parseInt(endDateArray[2]), 23, 59);
				
			    lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
				lineItem.setStartDateTime(DateTimes.toDateTime(lineItemStartDate));
				lineItem.setEndDateTime(DateTimes.toDateTime(lineItemEndDate));
				
				log.info("Added start/end time");
				// Set the cost type.
				lineItem.setCostType(lineItemCostType); // CostType.CPM

				// Set the line item to use 50% of the impressions.
				lineItem.setUnitType(UnitType.IMPRESSIONS);
				lineItem.setPriority(8);
				
				// added for sponsership type.
				if (lineItem.getLineItemType().equals(LineItemType.SPONSORSHIP)) {
					log.info("Found sponsorship itemtype. Changing priority and setting goals to 100%.");
					lineItem.setUnitsBought(100L);
					lineItem.setPriority(4);
				}
				// lineItem.setUnitsBought(50L);
				lineItem.setTargetPlatform(TargetPlatform.ANY);
				
				log.info("Going to hit forecast service.");
				// Get forecast for line item.
				Forecast forecast = forecastService.getForecast(lineItem);
				log.info("Forecast service result receieved.");
				if(forecast!=null && forecast.getAvailableUnits()>0){
					long matched = forecast.getMatchedUnits();

					long reserved = forecast.getReservedUnits();
					long delivered = forecast.getDeliveredUnits();
					long available = forecast.getAvailableUnits();
					long possible = forecast.getPossibleUnits();

					forcastProductDTO = new ProductForecastDTO();
					forcastProductDTO.setAvailableImpressions(available);
					forcastProductDTO.setDeliveredImpressions(delivered);
					forcastProductDTO.setPossibleImpressions(possible);
					forcastProductDTO.setReservedImpressions(reserved);
					forcastProductDTO.setMatchedImpressions(matched);
					forcastProductDTO.setStartDate(startDate);
					forcastProductDTO.setEndDate(endDate);
					forcastProductDTO.setAdUnitId(allAdUnitId);
					forcastProductDTO.setAdUnitName(allAdUnitNames);
					log.info("Obtained forecast dto. ToString is : \n "+ forcastProductDTO.toString());
				}else{
					log.info("obtained forecast has Zero(0) available impressions"); 
				}
				
			} catch (Exception e) {
				log.severe("dfpSession exception -- " + e.getMessage());
			}

		} 
		return forcastProductDTO;
	 }
}
