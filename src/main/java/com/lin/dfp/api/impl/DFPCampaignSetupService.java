package com.lin.dfp.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.joda.time.Duration;
import org.joda.time.Instant;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.utils.v201403.DateTimes;
import com.google.api.ads.dfp.jaxws.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.jaxws.v201403.AdUnit;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitPage;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitSize;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitTargetWindow;
import com.google.api.ads.dfp.jaxws.v201403.AdUnitTargeting;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.ApproveAndOverbookOrders;
import com.google.api.ads.dfp.jaxws.v201403.Company;
import com.google.api.ads.dfp.jaxws.v201403.CompanyPage;
import com.google.api.ads.dfp.jaxws.v201403.CompanyServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.CompanyType;
import com.google.api.ads.dfp.jaxws.v201403.ComputedStatus;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.Creative;
import com.google.api.ads.dfp.jaxws.v201403.CreativePage;
import com.google.api.ads.dfp.jaxws.v201403.CreativePlaceholder;
import com.google.api.ads.dfp.jaxws.v201403.CreativeServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.ImageCreative;
import com.google.api.ads.dfp.jaxws.v201403.InventoryServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.InventoryTargeting;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemCreativeAssociation;
import com.google.api.ads.dfp.jaxws.v201403.LineItemCreativeAssociationPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemCreativeAssociationServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.jaxws.v201403.MobilePlatform;
import com.google.api.ads.dfp.jaxws.v201403.Money;
import com.google.api.ads.dfp.jaxws.v201403.Network;
import com.google.api.ads.dfp.jaxws.v201403.NetworkServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Order;
import com.google.api.ads.dfp.jaxws.v201403.OrderPage;
import com.google.api.ads.dfp.jaxws.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Size;
import com.google.api.ads.dfp.jaxws.v201403.StartDateTimeType;
import com.google.api.ads.dfp.jaxws.v201403.Statement;
import com.google.api.ads.dfp.jaxws.v201403.TargetPlatform;
import com.google.api.ads.dfp.jaxws.v201403.Targeting;
import com.google.api.ads.dfp.jaxws.v201403.TechnologyTargeting;
import com.google.api.ads.dfp.jaxws.v201403.UnitType;
import com.google.api.ads.dfp.jaxws.v201403.UpdateResult;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.gdata.data.analytics.AccountEntry;
import com.lin.dfp.api.IDFPCampaignSetupService;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.DFPCompanyDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;

/*
 * Create/manage Order on DFP
 * @author Youdhveer Panwar
 */
public class DFPCampaignSetupService implements IDFPCampaignSetupService {

	private static final Logger log = Logger.getLogger(DFPCampaignSetupService.class.getName());
    private String [] adSizesArray={"216x36", "300x50", "300x250", "320x50", "728x90"};
	

    public Network currentNetwork;
    private DfpServices dfpServices;
	private DfpSession dfpSession;
	public static final String LIN_MOBILE_DFP_NETWORK_CODE="9331149";
    public static long TRAFFICKER_ID=118372949;  //117281789
	
    void initDFPSession(){		
    	log.info(" now going to build dfpSession and dfpServices instances...");
		try {
			if(dfpServices ==null){
				log.info("Create dfp Service service...");
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
			}
			log.info("Creating DFP session...for network --"+LIN_MOBILE_DFP_NETWORK_CODE);
			dfpSession = DFPAuthenticationUtil.getDFPSession(LIN_MOBILE_DFP_NETWORK_CODE);
			log.info("Session created....");			
			
		} catch (ValidationException e) {
			log.severe("ValidationException:"+e.getMessage());			
		} catch (Exception e) {
			log.severe("Exception:"+e.getMessage());	
		}		
	}
    
    
    /*
	 * create an order at DFP
	 * @author Youdhveer Panwar
	 *  
	 */
	public OrderDTO createNewOrder(AgencyObj agency,AdvertiserObj advertiser, String campaignName, 
			String startDate,String endDate)  throws ApiException_Exception, DataServiceException{
		log.info("Creating order on DFP with networkCode :"+dfpSession.getNetworkCode());
		
		initDFPSession();
		
		if(currentNetwork==null){
			NetworkServiceInterface networkService = dfpServices.get(dfpSession,NetworkServiceInterface.class);	
			currentNetwork = networkService.getCurrentNetwork();
		}	
		
       // Get the OrderService.
		OrderServiceInterface orderService = dfpServices.get(dfpSession,
				OrderServiceInterface.class);
		
		OrderDTO orderDTO=null;			
		String agencyName="";
		if(agency !=null){
			agencyName=agency.getName();
		}
		String advertiserName="";
		if(advertiser !=null){
			advertiserName=advertiser.getName();
		}
		String orderName= generateOrderName(agencyName, advertiserName, campaignName,startDate);		
		log.info("Create new order with advertiser:" + advertiser.getName()+" and orderName:"+orderName);
		
		long salesPersonId=0;
		long traffickerId=TRAFFICKER_ID;
		// Create order.
		Order order = new Order();
		order.setName(orderName);
		order.setAdvertiserId(advertiser.getAdvertiserId());
		if(agency !=null && agency.getAgencyId()>0){
			order.setAgencyId(agency.getAgencyId());
		}else{
			log.info("No agaency has been setup for this order.");
		}
		
		//order.setSalespersonId(salesPersonId);
		order.setTraffickerId(traffickerId);
        
		log.info("order startDate : "+startDate+" and endDate : "+endDate);
		if(startDate !=null){
			startDate=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy-MM-dd'T'HH:mm:ss");			
			order.setStartDateTime(DateTimes.toDateTime(startDate,currentNetwork.getTimeZone()));
		}
		if(endDate !=null){
			endDate=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy-MM-dd'T'HH:mm:ss");
			order.setEndDateTime(DateTimes.toDateTime(endDate,currentNetwork.getTimeZone()));
		}		
		
		List<Order> orderList=new ArrayList<Order>();
		orderList.add(order);
		// Create the orders on the server.	
		orderList = orderService.createOrders(orderList);
		long orderId = 0;
		if (orderList != null) {
			for(Order dfpOrder:orderList){
				orderId = dfpOrder.getId();
				log.info("Order has been created with orderId:"+orderId);
				orderDTO=new OrderDTO(orderId, orderName); //Right now we are creating only one order, so there will be only one DTO
			}
			
		}else{
			log.severe("OrderService failed to create order on DFP.");
		}
		
		return orderDTO;
	}
	

	/**
	 * Approve an order on DFP.
	 * 
	 * @throws ApiException_Exception
	 */
	public boolean approveOrder(long orderId, DfpServices dfpServices,
			DfpSession dfpSession) throws ApiException_Exception {
		log.info("Approve order--"+orderId);
		boolean approved=false;
		// Get the OrderService.
		OrderServiceInterface orderService = dfpServices.get(dfpSession,
				OrderServiceInterface.class);

		// Create a statement to select an order.
	    StatementBuilder statementBuilder = new StatementBuilder()
	        .where("WHERE id = :id")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("id", orderId);
	    
	   // Default for total result set size.
	    int totalResultSetSize = 0;

	    do {
	      // Get orders by statement.
	      OrderPage page = orderService.getOrdersByStatement(statementBuilder.toStatement());

	      if (page.getResults() != null) {
	        totalResultSetSize = page.getTotalResultSetSize();
	        int i = page.getStartIndex();
	        for (Order order : page.getResults()) {
	          log.info(i+") Order with ID "+order.getId()+" will be approved.");
	        }
	      }

	      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	    } while (statementBuilder.getOffset() < totalResultSetSize);

	    log.info("Number of orders to be approved:"+ totalResultSetSize);

	    if (totalResultSetSize > 0) {
	      // Remove limit and offset from statement.
	      statementBuilder.removeLimitAndOffset();

    	  // Create action.
	      ApproveAndOverbookOrders overbookAction=new ApproveAndOverbookOrders();
	      
	     /* 
	      ApproveOrders action = new ApproveOrders();
	      action.setSkipInventoryCheck(true);*/

	      // Perform action.
	      UpdateResult result =
	          orderService.performOrderAction(overbookAction, statementBuilder.toStatement());

	      if (result != null && result.getNumChanges() > 0) {
	        log.info("Number of orders approved: "+result.getNumChanges());
	        approved=true;
	      } else {
	    	  log.info("No orders were approved.");
	    	  approved=false;
	      }
	    }
	    
	    return approved;
	}
	
	
	public String generateOrderName(String agencyName,String advertiserName,String campaignName, String startDate){
		String orderName="";
		if(agencyName !=null && agencyName.trim().length()>0){
			orderName=orderName+agencyName;
		}
		if(advertiserName !=null && advertiserName.trim().length()>0){
			orderName=orderName+" | "+advertiserName;
		}
		if(campaignName !=null && campaignName.trim().length()>0){
			orderName=orderName+" | "+campaignName;
		}
		String year="";
		if(startDate !=null && startDate.trim().length()>0){
			year=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy");			
		}else{
			year=DateUtil.getCurrentTimeStamp("yyyy");
		}
		orderName=orderName+" | "+year;		
		
		return orderName;
	}

	public String generateLineItemName(String advertiserName,String campaignName,
			String flightDuration,String placement,String publisherName, String productName){
		String lineItemName="";
		if(advertiserName !=null && advertiserName.trim().length()>0){
			lineItemName=lineItemName+advertiserName;
		}
		
		if(campaignName !=null && campaignName.trim().length()>0){
			lineItemName=lineItemName+" | "+campaignName;
		}
		
		if(flightDuration!=null && flightDuration.trim().length()>0){
			lineItemName=lineItemName+" | "+flightDuration;
		}
		if(placement !=null && placement.trim().length()>0){
			lineItemName=lineItemName+" | "+placement;
		}
		
		if(productName !=null && productName.trim().length()>0){
			lineItemName=lineItemName+" | "+productName;
		}
		if(publisherName !=null && publisherName.trim().length()>0){
			lineItemName=lineItemName+" | "+publisherName;
		}
		return lineItemName;
	}	
	
	public String generateCreativeName(String lineItemName, String adSize){
		String creativeName=null;
		if(lineItemName !=null && adSize !=null){		
			lineItemName=   lineItemName+" | "+
							adSize;
		}
		return creativeName;
	}
	
	/**
	 * Create a line item and return the corresponding line item ID.
	 * @param double rate, 
	 *        CostType costType,
	 *        LineItemType lineItemType,
	 *        String lineItemName, 
	 *        long orderId,	
	 *        String startDate,
	 *        String endDate,
	 *        String adUnitId2,
	 *        String boughtUnits, 
	 *        String [] adSizes,
	 *        DfpServices dfpServices,
	 *        DfpSession dfpSession
	 *         
	 * @return long lineItemId
	 */
	public long createNewLineItem(double rate, CostType costType,LineItemType lineItemType,
			String lineItemName, long orderId,	String startDate,String endDate,
			String adUnitId2,String boughtUnits, String [] adSizes,
			DfpServices dfpServices, DfpSession dfpSession , Integer deviceCapability) throws ApiException_Exception{
		
		
		//String lineItemName=generateLineItemName(agencyName, advertiserName, campaignName, flightDuration, placement);
		long lineItemId = 0;
		if(currentNetwork==null){
			NetworkServiceInterface networkService = dfpServices.get(dfpSession,NetworkServiceInterface.class);	
			currentNetwork = networkService.getCurrentNetwork();
		}			      
	    
		    
		// Get the LineItemService.
		LineItemServiceInterface lineItemService = dfpServices.get(dfpSession,
				LineItemServiceInterface.class);
	
		// Create inventory targeting.
		InventoryTargeting inventoryTargeting = new InventoryTargeting();
		List<AdUnitTargeting> adUnitTargetingList = new ArrayList<AdUnitTargeting>();	
		AdUnitTargeting adUnitTargeting = new AdUnitTargeting();
		adUnitTargeting.setAdUnitId(adUnitId2);
		adUnitTargeting.setIncludeDescendants(true);
		adUnitTargetingList.add(adUnitTargeting);
		inventoryTargeting.getTargetedAdUnits().addAll(adUnitTargetingList);
	
		// Create targeting.
		Targeting targeting = new Targeting();
		targeting.setInventoryTargeting(inventoryTargeting);
	
		if(deviceCapability != null && deviceCapability > 0){
			// Technology Targeting
			TechnologyTargeting technologyTargeting = new TechnologyTargeting();
			ReportUtil.setDeviceCapability(technologyTargeting, deviceCapability);
			if(technologyTargeting.getDeviceCapabilityTargeting() != null) {
				log.info("Setting technology targeting.");
				targeting.setTechnologyTargeting(technologyTargeting);
			}
		}
		
		
		// Create line item.
		LineItem lineItem = new LineItem();
		lineItem.setName(lineItemName);
		lineItem.setOrderId(orderId);
		lineItem.setTargeting(targeting);
		lineItem.setLineItemType(lineItemType); //LineItemType.SPONSORSHIP OR STANDARD
		
		 
			
		lineItem.setAllowOverbook(true);
		lineItem.setTargetPlatform(TargetPlatform.ANY);
		
		//lineItem.setRoadblockingType(RoadblockingType.CREATIVE_SET);
	
		// Set the creative rotation type to even.
		//lineItem.setCreativeRotationType(CreativeRotationType.EVEN);
	    
		List<CreativePlaceholder> creativePlaceHoldeList=new ArrayList<CreativePlaceholder>();
		for(String sizeStr:adSizes){
			String [] sizeArr=sizeStr.split("[xX]");
			int width=Integer.parseInt(sizeArr[0].trim());
			int height=Integer.parseInt(sizeArr[1].trim());
			Size size = new Size();
			size.setWidth(width);
			size.setHeight(height);
			size.setIsAspectRatio(false);
			CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
			creativePlaceholder.setSize(size);
			creativePlaceHoldeList.add(creativePlaceholder);
		}
		// Set the size of creatives that can be associated with this line item.
		lineItem.getCreativePlaceholders().addAll(creativePlaceHoldeList);
		
		String currentDate=DateUtil.getCurrentTimeStamp("MM-dd-yyyy");
	    
	    long diff=DateUtil.getDifferneceBetweenTwoDates(currentDate, startDate, "MM-dd-yyyy");
	    log.info("line item startDate :"+startDate+" currentDate :"+currentDate+" and diff : "+diff);
	    if(diff<=0){
	    	startDate=currentDate;
	    	lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
	    	long endDiff=DateUtil.getDifferneceBetweenTwoDates(currentDate, endDate, "MM-dd-yyyy");
	    	log.info("endDiff : "+endDiff);
	    	if(endDiff <=0){
	    		lineItem.setEndDateTime(
	    		        DateTimes.toDateTime(Instant.now().plus(Duration.standardDays(30L)), "America/New_York"));
	    	}else{
	    		/* Edited by Naresh Pokhriyal on December 16, 2014 for issue "Default card lineitem setup fails"*/
				/************************************************************************************************/
				/*Added Code*/
	    		endDate=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy-MM-dd'T'HH:mm:ss");
				/************************************************************************************************/
	    		lineItem.setEndDateTime(DateTimes.toDateTime(endDate, currentNetwork.getTimeZone()));
	    	}
	    }else{
	    	startDate=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy-MM-dd'T'HH:mm:ss");
	    	endDate=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy-MM-dd'T'HH:mm:ss");
	    	
	    	// Set the length of the line item to run.
	    	log.info("Setting the length of the line item to run");
			lineItem.setStartDateTime(DateTimes.toDateTime(startDate, currentNetwork.getTimeZone()));
			lineItem.setEndDateTime(DateTimes.toDateTime(endDate, currentNetwork.getTimeZone()));
	    }		
	    log.info("updated line item startDate :"+startDate+" currentDate :"+currentDate+" and diff : "+diff);
	
		// Set the cost as per rate
	    log.info("Setting the cost as per rate");
		long microMoney=(long) (1000000*(rate));
		lineItem.setCostType(costType);  //CostType.CPM
		Money costPerUnit = new Money();
		costPerUnit.setCurrencyCode("USD");
		costPerUnit.setMicroAmount(microMoney);
		lineItem.setCostPerUnit(costPerUnit);
	
		// Set the number of units bought as per boughtImpression
		log.info("Setting the number of units bought as per boughtImpression");
		long unitsBought=Long.parseLong(boughtUnits);
		if(lineItemType == LineItemType.SPONSORSHIP){
			lineItem.setPriority(4);
			lineItem.setUnitsBought(100L);
		}else{
			lineItem.setPriority(8);
			lineItem.setUnitsBought(unitsBought);
		}
		
		lineItem.setUnitType(UnitType.IMPRESSIONS);
		
		List<LineItem> dfpLineItemList=new ArrayList<>();
		dfpLineItemList.add(lineItem);
		// Create the line items on the server.
		log.info("Creating the line items on the server.");
		dfpLineItemList = lineItemService.createLineItems(dfpLineItemList);
		if(dfpLineItemList !=null){
			for (LineItem dfpLineItem:dfpLineItemList){
				lineItemId = dfpLineItem.getId();	
			}
		}
		
		return lineItemId;
	}
	
	public List<LineItem> createLineItems(List<LineItem> lineItemList,long orderId,
			DfpServices dfpServices, DfpSession dfpSession ) throws ApiException_Exception{

		List<LineItem> dfpLineItemList=new ArrayList<>();
		if(lineItemList !=null && lineItemList.size()>0){
			// Get the LineItemService.
			LineItemServiceInterface lineItemService = dfpServices.get(dfpSession,
							LineItemServiceInterface.class);
			dfpLineItemList=lineItemService.createLineItems(lineItemList);
			if(dfpLineItemList ==null){
				dfpLineItemList=new ArrayList<>();
			}
			log.info("Original request for creating lineItems : "+lineItemList.size()+" and created on DFP :"+dfpLineItemList.size());
		}
		
		
		return dfpLineItemList;
	}
	/*
	 * This method create a mobile adUnit
	 */
	public AdUnitDTO createTopLevelAdUnit(DfpServices dfpServices, DfpSession session, String adUnitName,
			String [] adSizeArr)  throws ApiException_Exception{
		
		   AdUnit adUnit=null;
		   AdUnitDTO adUnitDTO=null;
		   String memcacheKey="adUnits_key:"+session.getNetworkCode();
		   Map<String,AdUnitDTO> adUnitDTOMap=(Map<String, AdUnitDTO>) MemcacheUtil.getObjectsMapFromCache(memcacheKey);
		   if(adUnitDTOMap !=null && adUnitDTOMap.containsKey(adUnitName)){
			   log.info("found in memcache...");
			   adUnitDTO=adUnitDTOMap.get(adUnitName);			   
		   }else{
			   adUnitDTOMap=new HashMap<String, AdUnitDTO>();
			   
			   Map<String,AdUnit> adUnitMap=fetchAdUnits(dfpServices, session, adUnitName, null);
			   if(adUnitMap !=null && adUnitMap.size()>0){
				   //AdUnit is already exist in DFP account, return existing key(adUnitId)
				  log.info("AdUnit is already exist in DFP account, return existing key(adUnitId), adUnitMap size:"+adUnitMap.size());
				  for(String key:adUnitMap.keySet()){
					  adUnit=adUnitMap.get(key);
					  break;
				  }
			   }else{
				   log.info("Going to create this adUnit in dfp...."+adUnitName);
				   // Get the InventoryService.
				    InventoryServiceInterface inventoryService =
				        dfpServices.get(session, InventoryServiceInterface.class);

				    // Get the NetworkService.
				    NetworkServiceInterface networkService =
				        dfpServices.get(session, NetworkServiceInterface.class);

				    // Set the parent ad unit's ID for all ad units to be created under.
				    String parentAdUnitId;
					
					parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
					List<AdUnitSize> adUnitSizes=new ArrayList<AdUnitSize>();
				    
					log.info("adSizeArr : "+adSizeArr);
				    for(String size:adSizeArr){
				      String [] sizeArray=size.split("x");
				      int width=Integer.parseInt(sizeArray[0].trim());
				      int height=Integer.parseInt(sizeArray[1].trim());		      
				      // Create a mobile ad unit size.
					  Size mobileSize = new Size();
					  mobileSize.setWidth(width);
					  mobileSize.setHeight(height);
					  mobileSize.setIsAspectRatio(false);
					  AdUnitSize mobileAdUnitSize = new AdUnitSize();
					  mobileAdUnitSize.setSize(mobileSize);
					  //mobileAdUnitSize.setEnvironmentType(EnvironmentType.BROWSER);			   
					  adUnitSizes.add(mobileAdUnitSize);			    
				    }

				    // Create a mobile ad unit.
				    log.info("parentAdUnitId : "+parentAdUnitId);
				    AdUnit anyAdUnit = new AdUnit();
				    anyAdUnit.setName(adUnitName);
				    anyAdUnit.setDescription(anyAdUnit.getName());
				    anyAdUnit.setTargetPlatform(TargetPlatform.ANY);
				    anyAdUnit.setParentId(parentAdUnitId);
				    anyAdUnit.setTargetWindow(AdUnitTargetWindow.BLANK);
				    anyAdUnit.getAdUnitSizes().addAll(adUnitSizes);
				    
				    List<AdUnit> adUnitList=new ArrayList<AdUnit>();
				    
				   /* Edited by Naresh Pokhriyal on December 16, 2014 for issue "Default card lineitem setup fails"*/
				   /************************************************************************************************/
						    /*Previous Code*/
						    //adUnitList.add(adUnit);
						    
						    /*Edited code*/
						    adUnitList.add(anyAdUnit);
				   /************************************************************************************************/
				    
				   // Create the ad unit on the server.		    
				    adUnitList= inventoryService.createAdUnits(adUnitList);
				    for(AdUnit dfpAdUnit:adUnitList){
				    	System.out.printf("A mobile ad unit with ID \"%s\", name \"%s\" was created.\n", dfpAdUnit.getId(),
				    			dfpAdUnit.getName());
				    	adUnit=dfpAdUnit;
				    }
					
			   }
				
			   if(adUnit !=null){
					adUnitDTO=new AdUnitDTO();
					adUnitDTO.setId(StringUtil.getLongValue(adUnit.getId()));
					adUnitDTO.setName(adUnit.getName());
					adUnitDTO.setAdUnitCode(adUnit.getAdUnitCode());
					adUnitDTOMap.put(adUnitName, adUnitDTO);
				}else {
					log.warning("No top level Ad unit");
				}
				MemcacheUtil.setObjectsMapInCache(adUnitDTOMap, memcacheKey);
					   
		   }
		   return adUnitDTO;		    
	}
	
	
	/*
	 * This method create a adUnit level 2 under an adUnit for each lineItem
	 * @author Youdhveer Panwar
	 */
	public AdUnitDTO createAdUnit2(DfpServices dfpServices, DfpSession session, String topLevelAdUnitId,
			String adUnitName2,String [] adSizeArr) throws ApiException_Exception{
		
		AdUnit adUnit=null;
		AdUnitDTO adUnitDTO=null;
		log.info("topLevelAdUnitId:"+topLevelAdUnitId+" --> adUnitName2:"+adUnitName2);		
		
	    // Get the InventoryService.
	    InventoryServiceInterface inventoryService =
	        dfpServices.get(session, InventoryServiceInterface.class);

	    // Set the parent ad unit's ID for all ad units to be created under.
	    String parentAdUnitId = topLevelAdUnitId; 

	    Map<String,AdUnit> adUnit2Map=fetchAdUnits(dfpServices, session, adUnitName2, topLevelAdUnitId);
	    if(adUnit2Map !=null && adUnit2Map.size()>0){
	    	log.info("Found adUnit2 in dfp already...adUnit2Map:"+adUnit2Map.size());
	    	for(String key:adUnit2Map.keySet()){
	    		AdUnit adUnitAlready=adUnit2Map.get(key);
	    		String value=adUnitAlready.getName();
	    		if(value !=null && value.equalsIgnoreCase(adUnitName2)){
	    			adUnit=adUnitAlready;
		    		break;
	    		}	    		
	    	}	    	
	    }else{
	    	log.info("Going to create this adUnit2 in DFP...");
	    	List<AdUnitSize> adUnitSizes=new ArrayList<AdUnitSize>();
		    
		    for(String size:adSizeArr){
		      String [] sizeArray=size.split("x");
		      int width=Integer.parseInt(sizeArray[0].trim());
		      int height=Integer.parseInt(sizeArray[1].trim());		      
		      // Create a mobile ad unit size.
			  Size mobileSize = new Size();
			  mobileSize.setWidth(width);
			  mobileSize.setHeight(height);
			  mobileSize.setIsAspectRatio(false);
			  AdUnitSize mobileAdUnitSize = new AdUnitSize();
			  mobileAdUnitSize.setSize(mobileSize);
			  //mobileAdUnitSize.setEnvironmentType(EnvironmentType.BROWSER);			   
			  adUnitSizes.add(mobileAdUnitSize);			    
		    }

		    // Create a any ad unit.
		    adUnit = new AdUnit();
		    adUnit.setName(adUnitName2);
		    adUnit.setDescription(adUnit.getName());
		    adUnit.setTargetPlatform(TargetPlatform.ANY);
		    adUnit.setParentId(parentAdUnitId);
		    adUnit.setTargetWindow(AdUnitTargetWindow.BLANK);
		    adUnit.getAdUnitSizes().addAll(adUnitSizes);
		   
		    List<AdUnit> adUnitList=new ArrayList<AdUnit>();
		    adUnitList.add(adUnit);
		    // Create the ad unit on the server.		    
		    List<AdUnit> createdAdUnitList = inventoryService.createAdUnits(adUnitList);
		    if(createdAdUnitList !=null){
		    	for(AdUnit createdAdUnit:createdAdUnitList){
		    		log.info("A mobile ad unit with ID"+ createdAdUnit.getId()+", and name "+ 
		    				createdAdUnit.getName()+" has been created.");
					adUnit=createdAdUnit;
		    	}
		    }
			
	    }	
	    
	    if(adUnit !=null){
			adUnitDTO=new AdUnitDTO();
			adUnitDTO.setId(StringUtil.getLongValue(adUnit.getId()));
			adUnitDTO.setName(adUnit.getName());
			adUnitDTO.setAdUnitCode(adUnit.getAdUnitCode());			
		}
	    
		return adUnitDTO;         
	 }
	
	public List<LineItem> getDeliveringLineItems(DfpServices dfpServices, DfpSession session,long orderId) 
			throws ApiException_Exception{
        
		List<LineItem> lineItemList=new ArrayList<LineItem>();
		
        // Get the LineItemService.
        LineItemServiceInterface lineItemService =
            dfpServices.get(session, LineItemServiceInterface.class);

        // Create a statement to only select line items that need creatives.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("status = :status AND orderId = :orderId")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("status", ComputedStatus.DELIVERING.toString())
            .withBindVariableValue("orderId", orderId);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get line items by statement.
          LineItemPage page =
              lineItemService.getLineItemsByStatement(statementBuilder.toStatement());
          if (page.getResults() != null) {
        	  lineItemList=page.getResults();
              totalResultSetSize = page.getTotalResultSetSize(); 
          }
          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);
       
        return lineItemList;
	}
	
	public List<LineItem> getLineItemsNeedsCreative(DfpServices dfpServices, DfpSession session,long orderId) 
			throws ApiException_Exception {
		log.info("Check lineitems need creatives :"+orderId);
		List<LineItem> lineItemList=new ArrayList<LineItem>();
		
        // Get the LineItemService.
        LineItemServiceInterface lineItemService =
            dfpServices.get(session, LineItemServiceInterface.class);

        // Create a statement to only select line items that need creatives.
        StatementBuilder statementBuilder = new StatementBuilder()
            .where("status = :status AND orderId = :orderId")
            .orderBy("id ASC")
            .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
            .withBindVariableValue("status", ComputedStatus.NEEDS_CREATIVES.toString())
            .withBindVariableValue("orderId", orderId);

        // Default for total result set size.
        int totalResultSetSize = 0;

        do {
          // Get line items by statement.
          LineItemPage page =
              lineItemService.getLineItemsByStatement(statementBuilder.toStatement());

          if (page.getResults() != null) {
        	  lineItemList=page.getResults();
              totalResultSetSize = page.getTotalResultSetSize();    
              log.info("totalResultSetSize:"+totalResultSetSize);
		        /*for (LineItem lineItem : page.getResults()) {
		          System.out.printf(
		              "%d) Line item with ID \"%d\" and name \"%s\" was found.\n", i++,
		              lineItem.getId(), lineItem.getName());
		        }*/
          }

          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
        } while (statementBuilder.getOffset() < totalResultSetSize);

       
        return lineItemList;
      }
	
	
	public long createNewImageCreative(DfpServices dfpServices,DfpSession dfpSession,
			String creativeName,long advertiserId) {

	    // Get the CreativeService.
	    CreativeServiceInterface creativeService = dfpServices.get(dfpSession,
	        CreativeServiceInterface.class);
	    
	    // Create image creative.
	    ImageCreative imageCreative = new ImageCreative();
	    imageCreative.setName(creativeName);
	    imageCreative.setAdvertiserId(advertiserId);
	    imageCreative.setDestinationUrl("http://google.com");
	  
	    Size size=new Size();
	    size.setWidth(300);
	    size.setHeight(250);
	    size.setIsAspectRatio(false);	    
	    imageCreative.setSize(size);
	    // Create the creatives on the server.
	    long creativeId=0;
	   /* List<ImageCreative> imageCreativeList=new ArrayList<ImageCreative>();
	    try {
	    	 List<ImageCreative> createdImageCreativeList = creativeService.createCreatives(imageCreativeList);		
		    if(imageCreative !=null){
		    	creativeId=imageCreative.getId();
		    }
		} catch (ApiException_Exception e) {
			log.severe("ApiException_Exception:"+e.getMessage());
		}	*/   
	    return creativeId;
	 }

	  /**
	   * Create a new creative line item association.	   * 
	   */
	 public void createNewCreativeLineItemAssociation(long creativeId, long lineItemId,
			  DfpServices dfpServices,DfpSession dfpSession){

	    // Get the LineItemCreativeAssociationService.
	    LineItemCreativeAssociationServiceInterface licaService = dfpServices.get(dfpSession,
	        LineItemCreativeAssociationServiceInterface.class);

	    // Set the line item ID and creative IDs to associate.
	    LineItemCreativeAssociation lica = new LineItemCreativeAssociation();
	    lica.setCreativeId(creativeId);
	    lica.setLineItemId(lineItemId);

	    // Create an array to store LICA objects.
	    List<LineItemCreativeAssociation> licas = new ArrayList<LineItemCreativeAssociation>();
	    licas.add(lica);

	    // Create the LICAs on the server.
	    try {
			licaService.createLineItemCreativeAssociations(licas);
		} catch (ApiException_Exception e) {
			log.severe("ApiException_Exception:"+e.getMessage());
		}
	}
		  
	public Map<Long,String> getCreativeURLsByLineItemId(DfpServices dfpServices, 
			  DfpSession session, long lineItemId){		  
		    
		    List<Long> creativeIdList=new ArrayList<Long>();
		    // Get the LineItemCreativeAssociationService.
		    LineItemCreativeAssociationServiceInterface licaService =
			        dfpServices.get(session, LineItemCreativeAssociationServiceInterface.class);

		    // Create a statement to all LICAs for a line item.
		    StatementBuilder statementBuilder = new StatementBuilder()
		        .where("WHERE lineItemId = :lineItemId ")
		        .orderBy("lineItemId ASC, creativeId ASC")
		        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
		        .withBindVariableValue("lineItemId", lineItemId);

		    // Default for total result set size.
		    int totalResultSetSize = 0;

		    Map<Long,String> creativeURLMap=null;
			try {
				 do {
			        // Get LICAs by statement.
			        LineItemCreativeAssociationPage page;
					page = licaService.getLineItemCreativeAssociationsByStatement(statementBuilder.toStatement());
					if (page.getResults() != null) {
				        totalResultSetSize = page.getTotalResultSetSize();
				        int i = page.getStartIndex();
				        for (LineItemCreativeAssociation lica : page.getResults()) {
				          /*if (lica.getCreativeSetId() != null) {
				            System.out.printf(
				                "%d) LICA with line item ID \"%d\" and creative set ID \"%d\" was found.\n", i++,
				                lica.getLineItemId(), lica.getCreativeSetId());
				          } else {
				            System.out.printf("%d) LICA with line item ID \"%d\" and creative ID \"%d\" and destination URL \"%d\" was found.\n", i++,
				                lica.getLineItemId(), lica.getCreativeId(),lica.getDestinationUrl());
				           		            
				          }*/
				          creativeIdList.add(lica.getCreativeId());
				          
				        }
				      }	
				      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
			    } while (statementBuilder.getOffset() < totalResultSetSize);

			    log.info("Number of creatives found:"+ totalResultSetSize);			    
			    creativeURLMap=getCreativePreviewURL(dfpServices,session,creativeIdList);
			    
			} catch (ApiException_Exception e) {
				log.severe("ApiException_Exception:"+e.getMessage());
			}  
		    return creativeURLMap;    
	}
	
	public Map<Long,String> getCreativePreviewURL(DfpServices dfpServices, 
				  DfpSession session,List<Long> creativeIdList) {
				
		  Map<Long,String> creativeURLMap=new HashMap<Long,String>();
		 
	      CreativeServiceInterface creativeService = dfpServices.get(session, CreativeServiceInterface.class);
	       
	      Statement statement=new Statement();	    
		  StringBuffer query=new StringBuffer();
		  if(creativeIdList !=null && creativeIdList.size()>0){
			  query.append(" WHERE id IN ( ");
			  for(int i=0;i<creativeIdList.size();i++){
			    	 if(i==0){
			    		 query.append(creativeIdList.get(i));	 
			    	 }else{
			    		 query.append(" , "+ creativeIdList.get(i));
			    	 }	    	 
			  }
			  query.append(" )");	
			  
		  }
		  query.append(" LIMIT 100");
		  log.info("Query:"+query.toString());
		  statement.setQuery(query.toString());
	    	 
		  CreativePage creativePage;
		try {
			creativePage = creativeService.getCreativesByStatement(statement);
			for(Creative creative:creativePage.getResults()){
				  String previewURL = creative.getPreviewUrl();
				  long creativeId = creative.getId();
				  creativeURLMap.put(creativeId, previewURL);
			}				 
		} catch (ApiException_Exception e) {
			log.severe("ApiException_Exception:"+e.getMessage());
		}
		 
		return creativeURLMap;
			    
	}
		  
	
	/*
	 * This method is creating company (advertiser/agency on dfp account)
	 * @see com.lin.dfp.api.IDFPCampaignSetupService#createCompany(com.google.api.ads.dfp.jaxws.factory.DfpServices, com.google.api.ads.dfp.lib.client.DfpSession, com.lin.dfp.api.impl.DFPCompanyEnum, java.lang.String)
	 */
	public Company createCompany(DfpServices dfpServices, DfpSession session,
			DFPCompanyDTO companyDTO){
		Company dfpCompany =null;
		log.info("Create company...");
		if(companyDTO !=null){
			List<Company> companyList=null;
			
			try{
				DFPCompanyEnum companyEnum=companyDTO.getType();
				String companyName=companyDTO.getName();
				CompanyServiceInterface companyService =
				        dfpServices.get(session, CompanyServiceInterface.class);
				dfpCompany = new Company();
				dfpCompany.setName(companyName);
				dfpCompany.setAddress(companyDTO.getAddress());
				dfpCompany.setEmail(companyDTO.getEmail());
				dfpCompany.setPrimaryPhone(companyDTO.getPhone());
				dfpCompany.setFaxPhone(companyDTO.getFax());
				
				log.info("CompanyType.ADVERTISER:"+CompanyType.ADVERTISER+" and companyDTO:"+companyDTO.toString());
				
				if(companyEnum==DFPCompanyEnum.ADVERTISER){
					log.info("Going to create advertiser in DFP...companyName:"+companyName);			
					dfpCompany.setType(CompanyType.ADVERTISER);			
					companyList=fetchExistingDFPCompany(companyService, companyName, CompanyType.ADVERTISER);
					
				}else if(companyEnum==DFPCompanyEnum.AGENCY){
					log.info("Going to create agency in DFP....companyName:"+companyName);
					dfpCompany.setType(CompanyType.AGENCY);
					companyList=fetchExistingDFPCompany(companyService, companyName, CompanyType.AGENCY);
				}else{
					log.warning("Invalid company type, please choose advertiser or agency type company...");				
				}
				
				if(companyList!=null && companyList.size()==0){
					List<Company> dfpCompanyList=new ArrayList<>();
					dfpCompanyList.add(dfpCompany);
					List<Company> createdCompanyList=companyService.createCompanies(dfpCompanyList);
					for(Company createdCompany:createdCompanyList){
						System.out.printf("A company with ID \"%d\", name \"%s\", and type \"%s\" was created.\n",
						          createdCompany.getId(), createdCompany.getName(), createdCompany.getType());				
						dfpCompany=createdCompany;
					}
					
					
				}else{
					log.info("Returning only first company...");
					dfpCompany=companyList.get(0);
				}			
				
			}catch (ApiException_Exception e) {
				log.severe("ApiException_Exception:"+e.getMessage());			
			} 
		}
		
		return dfpCompany;
	}
	
	
	
	
	private List<Company> fetchExistingDFPCompany(CompanyServiceInterface companyService, 
			String companyName,CompanyType companyType){
		List<Company> companyList=new ArrayList<Company>();
		try{
			StatementBuilder statementBuilder = new StatementBuilder()
		        .where("type = :type AND name = :name")	       
		        .withBindVariableValue("name", companyName)
		        .withBindVariableValue("type", companyType.toString()).limit(100);
			
			int totalResultSetSize = 0;
			
		    do {
		      // Get companies by statement.
		      CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());		
		      if (page.getResults() != null) {
		        totalResultSetSize = page.getTotalResultSetSize();
		        log.info("Found companies...totalResultSetSize:"+totalResultSetSize);
		        companyList=page.getResults();		
		      }
		      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		    } while (statementBuilder.getOffset() < totalResultSetSize);
		    
			
		}catch (ApiException_Exception e) {
			log.severe("ApiException_Exception:"+e.getMessage());			
		} 
		log.info("found companies in DFP :"+companyList.size());
		return companyList;
	}
	
	
	/*
	 * This is a method to fetch all companies form DFP with matching companyName
	 * 
	 * @param DfpServices dfpServices, DfpSession session,
			String companyName,
			CompanyType companyType (ADVERTISR | AGENCY ...)
     * @return Set<Long> companyIdSet 	
	 */
	public Set<Long> fetchExistingDFPCompany(DfpServices dfpServices, DfpSession session,
			String companyName,CompanyType companyType){
		Set<Long> companySet=new HashSet<Long>();
		try{
			CompanyServiceInterface companyService =
			        dfpServices.get(session, CompanyServiceInterface.class);
			StatementBuilder statementBuilder = new StatementBuilder()
		        .where("type = :type AND name = :name")	       
		        .withBindVariableValue("name", companyName)
		        .withBindVariableValue("type", companyType.toString());
			
			int totalResultSetSize = 0;
			
		    do {
		      // Get companies by statement.
		      CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());		
		      if (page.getResults() != null) {
		        totalResultSetSize = page.getTotalResultSetSize();
		        log.info("Found companies...totalResultSetSize:"+totalResultSetSize);
		        for (Company company : page.getResults()) {
		        	Long companyId=company.getId();
		        	companySet.add(companyId);
		        }
		      }		
		      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		    } while (statementBuilder.getOffset() < totalResultSetSize);			
			
		}catch (ApiException_Exception e) {
			log.severe("ApiException_Exception:"+e.getMessage());			
		} 
		log.info("found companies in DFP :"+companySet.size());
		return companySet;
	}
	
	/*
	 * Fetch adUnit based on parentAdUnit and adUnitName
	 * 
	 * @param DfpServices dfpServices, 
	 *        DfpSession session,
	 *        String adUnitName,
	 *        String parentAdUnitId
	 *        			  
	 * @return Map<String,String> : adUnitMap, key-- adUnitId and value-- adUnitName
	 */
	public Map<String,AdUnit> fetchAdUnits(DfpServices dfpServices, DfpSession session,String adUnitName,
			String parentAdUnitId)	throws ApiException_Exception {
		
		Map<String,AdUnit> adUnitMap=new HashMap<String, AdUnit>();
	
		InventoryServiceInterface inventoryService =
	            dfpServices.get(session, InventoryServiceInterface.class);
		if(parentAdUnitId ==null){
			 NetworkServiceInterface networkService =
		            dfpServices.get(session, NetworkServiceInterface.class);
			 parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
		}
		
		StatementBuilder statementBuilder=new StatementBuilder()
		        .where("name = :name AND parentId = :parentId")	       
		        .withBindVariableValue("name", adUnitName)
		        .withBindVariableValue("parentId", parentAdUnitId);
		
		int totalResultSetSize = 0;
		do {
	          // Get ad units by statement.
	          AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());
	          if (page.getResults() != null) {
	            totalResultSetSize = page.getTotalResultSetSize();
	            for (AdUnit adUnit : page.getResults()) {
	            	String name=adUnit.getName();
	            	TargetPlatform targetPlatform=adUnit.getTargetPlatform();
	            	String target=targetPlatform.toString();
	            	MobilePlatform mobile=adUnit.getMobilePlatform();
	            	String mobileStr=mobile.toString();
	            	
	            	System.out.println(adUnit.getId()+","+name+","+target+","+mobileStr);
	            	adUnitMap.put(adUnit.getId(), adUnit);	             
	            }
	          }
	          statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
	        } while (statementBuilder.getOffset() < totalResultSetSize);
	
	 return adUnitMap;
  }
	
	
     
   /*
    * @author Youdhveer Panwar
    * This method will generateGPTPassbackTags based on dfp network and adUnits
    * 
    * @param String networkCode,String adUnit1,String adUnit2,String adSize
    * @return String (tagScript)
    */
   public String generateGPTPassbackTags(String networkCode,String adUnitCode1,String adUnitCode2,String adSize){
	   log.info("gneerate gpt tag for adSize:"+adSize+" , networkCode:"+networkCode+", adUnitCode1:"+adUnitCode1+", adUnitCode2:"+adUnitCode2);
	   String [] adSizeArray=null;
	   if(adSize !=null){
		   adSizeArray=adSize.split("[xX]");
		   if(adSizeArray.length <2){
			   return null;
		   }else{
			   StringBuffer tagScriptBuffer=new StringBuffer();
			   tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
			   tagScriptBuffer.append("\n");
			   tagScriptBuffer.append("googletag.pubads().definePassback(");
			   tagScriptBuffer.append("\n");
			   
			   tagScriptBuffer.append("\"/"+networkCode+"/"+adUnitCode1+"/"+adUnitCode2+"\", ["+adSizeArray[0]+", "+adSizeArray[1]+"])");
			   tagScriptBuffer.append("\n");
			   tagScriptBuffer.append(".setClickUrl(\"%%CLICK_URL_UNESC%%\")");
			   tagScriptBuffer.append("\n");
			   tagScriptBuffer.append(".display();");
			   tagScriptBuffer.append("\n");
			   tagScriptBuffer.append("</script>");
			   
			   return tagScriptBuffer.toString();
		   }
	   }else{
		   return null;
	   }	   
   }
   
   /*
    * @author Youdhveer Panwar
    * Create GPT tags for adUnitLevel1 and adUnitLevel2
    * @param String networkCode, AdUnitDTO adUnitObjLevel1,
    *    AdUnitDTO adUnitObjLevel2, String adSize
    * @return List<String> gptTagsList 
    */
   public List<String> generateBothGPTPassbackTags(String networkCode, AdUnitDTO adUnitObjLevel1,
		   AdUnitDTO adUnitObjLevel2, String adSize){
	   List<String> gptTagsList=new ArrayList<String>();
	   if(adUnitObjLevel1 !=null && adUnitObjLevel2 !=null){
		   
		   String adUnitCodeLevel1=adUnitObjLevel1.getAdUnitCode();
		   String adUnitCodeLevel2=adUnitObjLevel2.getAdUnitCode();
		   
		   log.info("gneerate gpt tag for adSize:"+adSize+" , networkCode:"+networkCode 
				   +", adUnit1 code:"+adUnitCodeLevel1+", adUnit2Code :"+adUnitCodeLevel2);
		   String [] adSizeArray=null;
		 
		   if(adSize !=null){
			   adSizeArray=adSize.split("[xX]");
			   if(adSizeArray.length <2){
				   return null;
			   }else{
				   StringBuffer tagScriptBuffer=new StringBuffer();
				   tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append("googletag.pubads().definePassback(");
				   tagScriptBuffer.append("\n");
				   
				   tagScriptBuffer.append("\"/"+networkCode+"/"+adUnitCodeLevel1+"/"+adUnitCodeLevel2+"\", ["+adSizeArray[0]+", "+adSizeArray[1]+"])");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append(".setClickUrl(\"%%CLICK_URL_UNESC%%\")");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append(".display();");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append("</script>");
				   
				   gptTagsList.add(tagScriptBuffer.toString());	
				   
				   tagScriptBuffer=new StringBuffer();
				   tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append("googletag.pubads().definePassback(");
				   tagScriptBuffer.append("\n");
				   
				   tagScriptBuffer.append("\"/"+networkCode+"/"+adUnitCodeLevel1+"/"+adUnitCodeLevel2+"\", ["+adSizeArray[0]+", "+adSizeArray[1]+"])");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append(".setClickUrl(\"\")");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append(".display();");
				   tagScriptBuffer.append("\n");
				   tagScriptBuffer.append("</script>");
				   
				   gptTagsList.add(tagScriptBuffer.toString());	
				   
			   }
		   }
	   }
	   
	   return gptTagsList;
   }
   
   
   /*
    * @author Youdhveer Panwar
    * Create GPT tags for adUnitLevel1 and adUnitLevel2
    * @param String networkCode, AdUnitDTO adUnitObjLevel1,
    *    AdUnitDTO adUnitObjLevel2, String [] creativeAdSizeArr
    * @return List<String> gptTagsList 
    */
   public List<String> generateBothGPTPassbackTags(String networkCode, AdUnitDTO adUnitObjLevel1,
		   AdUnitDTO adUnitObjLevel2, String [] creativeAdSizeArr){
	   List<String> gptTagsList=new ArrayList<String>();
	   
	   if(adUnitObjLevel1 !=null && adUnitObjLevel2 !=null){
		   
		   String adUnitCodeLevel1=adUnitObjLevel1.getAdUnitCode();
		   String adUnitCodeLevel2=adUnitObjLevel2.getAdUnitCode();
		   
		   if(creativeAdSizeArr !=null && creativeAdSizeArr.length>0){
			   
			   for(String creativeSize:creativeAdSizeArr){	
				   log.info("gneerate gpt tag for adSize:"+creativeSize+" , networkCode:"+networkCode 
						   +", adUnit1 code:"+adUnitCodeLevel1+", adUnit2Code :"+adUnitCodeLevel2);
				   String [] adSizeArray=null;
				 
				   if(creativeSize !=null){
					   adSizeArray=creativeSize.split("[xX]");
					   if(adSizeArray.length <2){
						   return null;
					   }else{
						   StringBuffer tagScriptBuffer=new StringBuffer();
						   tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append("googletag.pubads().definePassback(");
						   tagScriptBuffer.append("\n");
						   
						   tagScriptBuffer.append("\"/"+networkCode+"/"+adUnitCodeLevel1+"/"+adUnitCodeLevel2+"\", ["+adSizeArray[0]+", "+adSizeArray[1]+"])");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append(".setClickUrl(\"%%CLICK_URL_UNESC%%\")");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append(".display();");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append("</script>");
						   
						   gptTagsList.add(tagScriptBuffer.toString());	
						   
						   tagScriptBuffer=new StringBuffer();
						   tagScriptBuffer.append("<script src=\"//www.googletagservices.com/tag/js/gpt.js\">");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append("googletag.pubads().definePassback(");
						   tagScriptBuffer.append("\n");
						   
						   tagScriptBuffer.append("\"/"+networkCode+"/"+adUnitCodeLevel1+"/"+adUnitCodeLevel2+"\", ["+adSizeArray[0]+", "+adSizeArray[1]+"])");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append(".setClickUrl(\"\")");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append(".display();");
						   tagScriptBuffer.append("\n");
						   tagScriptBuffer.append("</script>");
						   
						   gptTagsList.add(tagScriptBuffer.toString());	
						   
					   }
				   }else{
					   log.warning("Invalid creativeSize : "+creativeSize);
				   }					   
				  
			   }
			   
			   log.info("Total tags -- "+gptTagsList.size());			   		  
			  
		   }else{
			   log.warning("Can not create GPT tags as there are not creatives for this placement.");
		   }
	   }
	   return gptTagsList;
   }
   
   public String convertGPTtagListToString(List<String> gptTagsList){
	   StringBuffer gptTag=new StringBuffer();
	   if(gptTagsList !=null && gptTagsList.size()>0){
		   int count=0;			  
		   for(String tag:gptTagsList){
			   if(count==0){
				   gptTag.append(tag);
			   }else{
				   gptTag.append(" || ");
				   gptTag.append(tag);
			   }
			   count++;
		   }
		   //log.info("Tags : "+gptTag.toString());	
	   }else{
		   log.info("No tags as gptTagsList.size() : 0");
	   }
	   return gptTag.toString();
   }
   
   public String createTopLevelAdUnitName(String publisherName){
	    String topLevelAdUnit=publisherName.replace(" ", ".");
		topLevelAdUnit=topLevelAdUnit.toLowerCase();
		return topLevelAdUnit;
   }
   
   
	/*
	 * Create a campaign on network code: 
	 */
	public SmartMediaPlanObj setupCampaignOnDFP(SmartMediaPlanObj smartMediaPlan )
			throws Exception{
		
		if(smartMediaPlan !=null){
			initDFPSession();
			long localAdvertiserId=smartMediaPlan.getLocalAdvertiserId();
			long localAgencyId=smartMediaPlan.getLocalAgencyId();
			log.info("Step -1: Setup advertiser and agency in DFP if not exist....localAdvertiserId:"+
					localAdvertiserId+" and localAgencyId:"+localAgencyId);
			AdvertiserObj advertiser=null;
			AgencyObj agency=null;
			 
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				if(localAdvertiserId >0){
					advertiser=mediaPlanDAO.loadAdvertiser(localAdvertiserId);
					advertiser=saveOrUpdateAdvertiserInDFPAndDatastore(dfpServices, dfpSession, advertiser);
					log.info("Advertiser updated..");
				}else{
					log.warning("This advertiser does not exist in Datastore..");
					throw new Exception("Campaign does not have an advertsier.");
				}
				
				if(localAgencyId >0){
					agency=mediaPlanDAO.loadAgency(localAgencyId);
					
					agency=saveOrUpdateAgencyInDFPAndDatastore(dfpServices, dfpSession, agency);
				 
					log.info("Agency updated..");
				}
				
				log.info("Step-2 : Create order on DFP..");
				String orderStartDate=smartMediaPlan.getStartDate();
				String orderEndDate=smartMediaPlan.getEndDate();
                if(smartMediaPlan.getDfpOrderName() ==null){
                	OrderDTO orderDTO=createNewOrder(agency, advertiser, smartMediaPlan.getCampaignName(),orderStartDate,orderEndDate);
                	if(orderDTO !=null){
    					log.info("Step-3 : Update media plan with this dfp Order :"+orderDTO.toString());
    					smartMediaPlan.setDfpOrderId(orderDTO.getOrderId());
    					smartMediaPlan.setDfpOrderName(orderDTO.getOrderName());
    					mediaPlanDAO.saveObject(smartMediaPlan);    					
    				}else{
    					log.warning("Failed to setup Order at DFP for this campaign.");
    				}
                }else{
                	log.info("DFP order has already been setup for this campaign.. orderName:"+smartMediaPlan.getDfpOrderName());
                	
                }
				
			} catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
			}
		}		
		return smartMediaPlan;
	}
	
	/*
	 * Create a campaign on network code: 
	 */
	public SmartMediaPlanObj setupPlacementsOnDFP(SmartMediaPlanObj smartMediaPlan ) throws Exception{
		
		if(smartMediaPlan !=null){
			initDFPSession();
			long localAdvertiserId=smartMediaPlan.getLocalAdvertiserId();
			long localAgencyId=smartMediaPlan.getLocalAgencyId();
			log.info("Step -1: Get advertiser and agency in DFP..localAdvertiserId:"+
					localAdvertiserId+" and localAgencyId:"+localAgencyId);
			AdvertiserObj advertiser=null;
			AgencyObj agency=null;
			String agencyName="";
			 
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				if(localAdvertiserId >0){
					advertiser=mediaPlanDAO.loadAdvertiser(localAdvertiserId);
				}else{
					log.warning("This advertiser does not exist in Datastore..");
					throw new Exception("Campaign does not have an advertsier.");
				}
				
				if(localAgencyId >0){
					agency=mediaPlanDAO.loadAgency(localAgencyId);
					if(agency !=null){
						  agencyName=agency.getName();
					}
				}
				
                if(smartMediaPlan.getDfpOrderName() ==null){
                	throw new Exception("Please setup orders first before creating lineitems.");
                }else{                	
                	if(smartMediaPlan.getDfpPlacements() !=null && smartMediaPlan.getDfpPlacements().size()>0){
                		log.info("LineItems has already been setup for this campaign.."+smartMediaPlan.getDfpPlacements().size());
                	}else{
                		log.info("Step-3 : Now creating lineItems for this order :"+smartMediaPlan.getDfpOrderName());
                		smartMediaPlan=setUpLineItems(smartMediaPlan, agencyName, advertiser.getName());
    					mediaPlanDAO.saveObject(smartMediaPlan);
    					log.info("Updated media plan in datastore successfully..");
    					log.info("Step 6 - Send an email to trafficker...");
    					sendEmailNotoficationToTrafficker(smartMediaPlan.getDfpOrderId(), smartMediaPlan.getDfpOrderName(),
    							dfpSession.getNetworkCode());
                	}
                }
				
			} catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
			}
		}		
		return smartMediaPlan;
	}
	
	private SmartMediaPlanObj setUpLineItems(SmartMediaPlanObj smartMediaPlan, String agencyName,String advertiserName) throws Exception{
		
		if(dfpSession ==null){
			initDFPSession();
		}
		log.info("Going to create lineitem for each placement and each partner..");
		if(smartMediaPlan !=null && smartMediaPlan.getDfpOrderName() !=null){
			
			long orderId=smartMediaPlan.getDfpOrderId();
			String orderName=smartMediaPlan.getDfpOrderName();
			
			List<SmartCampaignPlacementDTO> placementDTOList=smartMediaPlan.getPlacements();
			if(placementDTOList ==null){
				log.info("There is no placement assosiated with this smart media plan. You can not create a lineitem.");
				throw new Exception("No placement has been created for this smart media plan. You can not create line item.");
			}else{
				List<ProductDTO> productsDTOList=smartMediaPlan.getProducts();
				Map<String,List<ProductDTO>> placementWiseProducts=new HashMap<String,List<ProductDTO>>();
				Map<String,String> partnerAdserverMap=new HashMap<String,String>();
				if(productsDTOList ==null){
					log.info("There is no product assosiated with this smart media plan. You can not create a lineitem.");
					throw new Exception("No partner product assosiated with this smart media plan. You can not create line item.");
				}else{
					List<ProductDTO> productsList=new ArrayList<ProductDTO>();
					List<SmartCampaignPlacementDTO> lineItemList=new ArrayList<SmartCampaignPlacementDTO>();
					
					for(ProductDTO product:productsDTOList){
						String placementId=product.getPlacementId();
						String partnerName=product.getPartnerName();
						String partnerDFPCode=product.getPartnerAdserverId();
						
						if(placementWiseProducts.containsKey(placementId)){
							productsList=placementWiseProducts.get(placementId);
							productsList.add(product);
							
						}else{
							productsList=new ArrayList<ProductDTO>();
							productsList.add(product);									
						}								
						placementWiseProducts.put(placementId, productsList);
						partnerAdserverMap.put(partnerName, partnerDFPCode);
					}
					log.info("Going to create lineitem for each placement and each partner..");
					
					for(SmartCampaignPlacementDTO placement:placementDTOList){
						
					   String placementId=placement.getId()+"";
					   String adSize=placement.getCreatives();					  
					   List<ProductDTO> placemnetWiseProducts=placementWiseProducts.get(placementId);
					   for(ProductDTO product:placemnetWiseProducts){
						   String partner=product.getPartnerName();
						   String productName=product.getName()==null?"":product.getName();
						   String productId=product.getId()+"";
						   long allocatedImp=product.getAllocatedImpressions();
						   if(partner.equalsIgnoreCase(LinMobileConstants.OVER_FLOW_PARTNER_NAME)){
							   partner=LinMobileConstants.OVER_FLOW_PRODUCT_NAME;
							   log.info(" allocatedImp for partner "+partner+" are "+allocatedImp);
						   }
						   if(allocatedImp >0){
							   String topLevelAdUnit=createTopLevelAdUnitName(partner);
							   log.info("Creating topLevelAdUnit:"+topLevelAdUnit+" and partner:"+partner);
							   String [] adSizeArr=adSize.split(",");
							   AdUnitDTO topLevelAdUnitObj=createTopLevelAdUnit(dfpServices, dfpSession, topLevelAdUnit,
									  adSizeArr);
							   
							   String adUnitName2="";
							   if(agencyName !=null && agencyName.trim().length()>0){
								   adUnitName2=agencyName.toLowerCase().trim();
							   }
							   if(smartMediaPlan.getCampaignName() !=null){
								   if(adUnitName2.trim().length()>0){
									   adUnitName2=adUnitName2+"_"+smartMediaPlan.getCampaignName().toLowerCase().trim();
								   }else{
									   adUnitName2=adUnitName2+smartMediaPlan.getCampaignName().toLowerCase().trim();
								   }								   
							   }
							   if(placement.getPlacementName() !=null){
								   if(adUnitName2.trim().length()>0){
									   adUnitName2=adUnitName2+"_"+placement.getPlacementName().toLowerCase().trim();
								   }else{
									   adUnitName2=adUnitName2+placement.getPlacementName().toLowerCase().trim();
								   }
								   
							   }
							   if(productName !=null){
								   if(adUnitName2.trim().length()>0){
									   adUnitName2=adUnitName2+"_"+productName.toLowerCase();
								   }else{
									   adUnitName2=adUnitName2+productName.toLowerCase();
								   }								   
							   }
							   adUnitName2=adUnitName2.replaceAll(" ", "_");
							   //adUnitName2=adUnitName2.replaceAll("_", "").trim();
							   String topLevelAdUnitId=topLevelAdUnitObj==null?"0":topLevelAdUnitObj.getId()+"";
							   log.info("Creating adUnitName2:"+adUnitName2+" and partner:"+partner+", for topLevelAdUnitId :"+topLevelAdUnitId);
							 
							   AdUnitDTO adUnitObjLevel2=createAdUnit2(dfpServices, dfpSession, topLevelAdUnitId, adUnitName2, adSizeArr);
							   
							   String flightDuration=formattedFlightDuration(placement.getStartDate(), placement.getEndDate());
							   
							   String lineItemName=generateLineItemName(advertiserName, 
									   smartMediaPlan.getCampaignName(), flightDuration, placement.getPlacementName(),partner,productName);
							   log.info("Going to generate lineItem with name -"+lineItemName);
							
							   //String unitsBoughtStr=allocatedImp+"";
							   String unitsBoughtStr= placement.getImpressions()+"" ; //100% since lineItem type is SPONSORSHIP
							   String adUnitId2=adUnitObjLevel2==null?"0":adUnitObjLevel2.getId()+"";
							   LineItemType lineItemType = 
									   placement.getItemType() != null &&  placement.getItemType().equals(UnifiedCampaignDTO.STANDARD_ITEM_TYPE)
									   ? LineItemType.STANDARD : 
										 LineItemType.SPONSORSHIP;
							   
							   long lineItemId=createNewLineItem(placement.getRate(), CostType.CPM, lineItemType, lineItemName, 
									   orderId, placement.getStartDate(), 
										placement.getEndDate(), adUnitId2, unitsBoughtStr, adSizeArr, dfpServices, dfpSession, placement.getDeviceCapability());
							   
							   log.info("Successfully generated lineItem with id:"+lineItemId+" for partner:"+partner+", with adUnitId2 : "+adUnitId2);
							   placement.setPartnerName(partner);
							   placement.setLineItemId(lineItemId);
							   placement.setLineItemName(lineItemName);
							   placement.setPartnerDFPNetworkCode(partnerAdserverMap.get(partner));
							  	
							   log.info("Step 3-2 : Generate tags for this lineItem");
							   if(adSizeArr !=null && adSizeArr.length>0){
								   
								   List<String> gptTagsList=generateBothGPTPassbackTags(dfpSession.getNetworkCode(), 
										   topLevelAdUnitObj, adUnitObjLevel2, adSizeArr);
								   String gptTag=convertGPTtagListToString(gptTagsList);
								  
								   placement.setGptTag(gptTag);								  
								   placement.setProductId(productId);
								   placement.setProductName(productName);
							   }else{
								   log.warning("Can not create GPT tags as there are not creatives for this placement.");
							   }		
							   SmartCampaignPlacementDTO dfpPlacement=new SmartCampaignPlacementDTO(placement.getId(),
									   placement.getPlacementName(),  placement.getImpressions(), placement.getStartDate(),
									   placement.getEndDate(), placement.getRate(), placement.getBudget(), 
									   placement.getCreatives(), placement.getDmas(), placement.getNotes(),
									   partnerAdserverMap.get(partner),partner, lineItemName, lineItemId);
							   dfpPlacement.setProductId(productId);
							   dfpPlacement.setProductName(productName);
							   dfpPlacement.setGptTag(placement.getGptTag());
							   dfpPlacement.setCities(placement.getCities());
							   							   
							   lineItemList.add(dfpPlacement);	
						   }
						   						   
					   }							   
					}
					log.info("Total lineItems created for this order :"+lineItemList.size());
					log.info("Step 4 - Update smart media plan with lineitmes in datastore..");
					smartMediaPlan.setDfpPlacements(lineItemList);				
		      }
			}
	    }else{
	    	log.info("No media plan is available or no order has been setup.");;
	    }
		return smartMediaPlan;
	}

	private void sendEmailNotoficationToTrafficker(long orderId,String orderName,String dfpCode){
		log.info("Going to sent email.....");
		String subject="Campaign has bee setup successfully with order Id "+orderId;
		String message="Hi,"+"\n"+subject+"\n"+"Please setup creatives for all lineitems. See the following details."+"\n"
				+"Details : \n"+"DFP Network Code -"+dfpCode
				+"\n"+"Order Name-"+orderName;
		LinMobileUtil.sendMailOnGAE(
				LinMobileVariables.SENDER_EMAIL_ADDRESS, 
				LinMobileConstants.TRAFFICKER_EMAIL,
				LinMobileConstants.CC_EMAIL_ADDRESS, 
				subject,message);
		log.info("Email sent successfully.");
	}
	private String formattedFlightDuration(String startDate,String endDate){
	   log.info("get flight duration --startDate:"+startDate+" and endDate:"+endDate);
	   String flightDuration="";
	   String flightStart=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "MMM");
	   String flightEnd=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "MMM");
	   String startYear=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy");
	   String endYear=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy");
	   
	   if(startYear!=null && endYear !=null && !startYear.equals(endYear)){
		   System.out.println("Here....");
		   flightStart=flightStart+" "+startYear;
		   flightEnd=flightEnd+" "+endYear;
		   flightDuration=flightStart+" - "+flightEnd;			 
	   }else if(startYear!=null && endYear !=null && startYear.equals(endYear)){
		   
		   if(flightStart!=null && flightEnd!=null && flightStart.equals(flightEnd)){				  
			   flightStart=flightStart+" "+endYear;
			   flightDuration=flightStart;		
		   }else{
			   flightEnd=flightEnd+" "+endYear;
			   flightDuration=flightStart+" - "+flightEnd;	
		   }
	   }		   
	   return flightDuration;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * 
	 * This method will check whether account(Advertiser/Agency) created by user exist on DFP or not,
	 * if not exist, it will setup it on DFP and updates new accountId at datastore
	 */
	public AccountsEntity saveOrUpdateAccountInDFPAndDatastore(DfpServices dfpServices,DfpSession dfpSession, 
			AccountsEntity account){
		if(account !=null){
			String accountId=account.getAccountDfpId();
			ISmartCampaignPlannerDAO campaignPlannerDAO=new SmartCampaignPlannerDAO();
			try{
				boolean setup=false;
				
				if(accountId ==null || accountId.equals("0")){
					log.info("setup this account on DFP..");
					setup=true;	
					
				}else if(account.getAdServerId()!=null){
					if(account.getAdServerId().equalsIgnoreCase(dfpSession.getNetworkCode())){
						log.info("Account is already setup in this adServer.");
					}else{
						setup=true;
						log.info("Also setup in adServer -- "+dfpSession.getNetworkCode());
						AccountsEntity accountExist=campaignPlannerDAO.loadAccount(accountId, dfpSession.getNetworkCode());
						if(accountExist !=null){
							log.info("account exist already in adServer --"+dfpSession.getNetworkCode());
							setup=false;
							account=accountExist;
						}
					}
				}else{
					log.warning("Invalid account for update ...account.getAdServerId() :"+account.getAdServerId());
				}
				
				if(setup){
					log.info("Going to setup account..account:"+account.getAccountName()+", type: "+account.getAccountType());
					
					DFPCompanyEnum companyType=null;
					if(account.getAccountType() !=null && account.getAccountType().equalsIgnoreCase(LinMobileConstants.ADVERTISER_ID_PREFIX)){
						companyType=DFPCompanyEnum.ADVERTISER;
					}else if(account.getAccountType() !=null && account.getAccountType().equalsIgnoreCase(LinMobileConstants.AGENCY_ID_PREFIX)){
						companyType=DFPCompanyEnum.AGENCY;
					}else{
						companyType=DFPCompanyEnum.NONE;
					}
					DFPCompanyDTO companyDTO=new DFPCompanyDTO(0, account.getAccountName(), account.getAddress(), 
							account.getState(), account.getPhone(), account.getEmail(),
							account.getFax(), dfpSession.getNetworkCode(), companyType);
					
					Company dfpCompany=createCompany(dfpServices, dfpSession, companyDTO);
					if(dfpCompany !=null){
						Date createdOn=new Date();
						String id=dfpSession.getNetworkCode()+"_"+dfpCompany.getId()+"_"+account.getCompanyId();
						account.setId(id);
						account.setAccountDfpId(dfpCompany.getId()+"");
						account.setLastModifiedByUserId(account.getCreatedByUserId());
						account.setLastModifiedDate(createdOn);
						account.setCreationDate(createdOn);
						
						log.info("account found/created in dfp :"+dfpCompany.getId()+", now going to update in datastore also..");
						campaignPlannerDAO.saveObject(account);
						log.info("account saved/updated in datastore :"+account.toString());
					}else{
						log.severe("Failed to setup account (company) at DFP : "+companyDTO.toString());
						throw new DataServiceException("Failed to setup account (company) at DFP : "+companyDTO.toString());
					}
				}
			} catch (DataServiceException e) {
				log.severe("Failed to save account-accountId:"+accountId+": "+e.getMessage());
			} catch (Exception e) {
				log.severe("Failed to load account from datastore accountId:"+accountId+": "+e.getMessage());
				
			}				
		}else{
			log.warning("Invalid account for update ..");
		}
		return account;
	}
	
	public AdvertiserObj saveOrUpdateAdvertiserInDFPAndDatastore(DfpServices dfpServices,DfpSession dfpSession, 
			AdvertiserObj advertiser){
		if(advertiser !=null){
			long advertiserId=advertiser.getAdvertiserId();
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try{
				boolean setup=false;
				
				if(advertiserId <=0){
					log.info("setup this advertiser on DFP");
					setup=true;	
					
				}else if(advertiser.getDfpNetworkCode()!=null){
					if(advertiser.getDfpNetworkCode().equalsIgnoreCase(dfpSession.getNetworkCode())){
						log.info("Already setup in this adServer..");
					}else{
						setup=true;
						log.info("Also setup in adServer -- "+dfpSession.getNetworkCode());
						AdvertiserObj advertiserExist=mediaPlanDAO.loadAdvertiser(advertiserId, dfpSession.getNetworkCode());
						if(advertiserExist !=null){
							log.info("Already in adServer --"+dfpSession.getNetworkCode());
							setup=false;
							advertiser=advertiserExist;
						}
					}
				}
				
				if(setup){
					log.info("Going to setup advertiser..advertiser:"+advertiser);
					DFPCompanyDTO companyDTO=new DFPCompanyDTO(0, advertiser.getName(), advertiser.getAddress(), 
							advertiser.getState(), advertiser.getPhone(), advertiser.getEmail(),
							advertiser.getFax(), dfpSession.getNetworkCode(), DFPCompanyEnum.ADVERTISER);
					
					Company dfpCompany=createCompany(dfpServices, dfpSession, companyDTO);
					if(dfpCompany !=null){
						advertiser.setAdvertiserId(dfpCompany.getId());
						advertiser.setAddress(dfpCompany.getAddress());
						advertiser.setEmail(dfpCompany.getEmail());
						advertiser.setFax(dfpCompany.getFaxPhone());
						advertiser.setPhone(dfpCompany.getPrimaryPhone());
						advertiser.setDfpNetworkCode(dfpSession.getNetworkCode());
						log.info("advertiser found in dfp :"+advertiserId);
						mediaPlanDAO.saveObject(advertiser);
						log.info("advertiser saved in dfp :"+advertiser.toString());
					}
				}
			} catch (DataServiceException e) {
				log.severe("Not saved advertiser-advertiserId:"+advertiserId+": "+e.getMessage());
			}				
		}
		return advertiser;
	}
	
	
	public AgencyObj saveOrUpdateAgencyInDFPAndDatastore(DfpServices dfpServices,DfpSession dfpSession, 
			AgencyObj agency){
		if(agency !=null){
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try{
				boolean setup=false;
				long agencyId=agency.getAgencyId();
				if(agencyId <=0){
					log.info("setup this agency on DFP");
					setup=true;					
				}else if(agency.getDfpNetworkCode()!=null){
					if(agency.getDfpNetworkCode().equalsIgnoreCase(dfpSession.getNetworkCode())){
						log.info("Already setup in this adServer..");
					}else{
						setup=true;
						log.info("Also setup in adServer -- "+dfpSession.getNetworkCode());
						AgencyObj agencyExist=mediaPlanDAO.loadAgency(agencyId, dfpSession.getNetworkCode());
						if(agencyExist !=null){
							log.info("Already in adServer --"+dfpSession.getNetworkCode());
							setup=false;
							agency=agencyExist;
						}
					}
				}
				if(setup){
					DFPCompanyDTO companyDTO=new DFPCompanyDTO(0, agency.getName(), agency.getAddress(), 
							agency.getState(), agency.getPhone(), agency.getEmail(),
							agency.getFax(), dfpSession.getNetworkCode(), DFPCompanyEnum.AGENCY);
					Company dfpCompany=createCompany(dfpServices, dfpSession, companyDTO);
					if(dfpCompany !=null){
						agency.setAgencyId(dfpCompany.getId());
						agency.setAddress(dfpCompany.getAddress());
						agency.setEmail(dfpCompany.getEmail());
						agency.setFax(dfpCompany.getFaxPhone());
						agency.setPhone(dfpCompany.getPrimaryPhone());
						agency.setDfpNetworkCode(dfpSession.getNetworkCode());
						log.info("agency found in dfp :"+agencyId);
						mediaPlanDAO.saveObject(agency);
						log.info("agency saved in dfp :"+agency.toString());
					}
				}
						
					
				
			} catch (DataServiceException e) {
				log.severe("Not saved agency-agencyId:"+agency.getId());
			}				
		}
		return agency;
	}


	

}
