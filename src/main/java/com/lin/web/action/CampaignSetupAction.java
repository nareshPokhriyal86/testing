package com.lin.web.action;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.Company;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.lin.dfp.api.IDFPCampaignSetupService;
import com.lin.dfp.api.IForecastInventoryService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPCampaignSetupService;
import com.lin.dfp.api.impl.DFPCompanyEnum;
import com.lin.dfp.api.impl.ForecastInventoryService;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.DFPCompanyDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.OrderDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IProductService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;

public class CampaignSetupAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(CampaignSetupAction.class.getName());
	
	private String status;	
	private HttpServletRequest request;
	private DfpServices dfpServices;
	private DfpSession dfpSession;
	private JSONObject jsonObject;
	
	private Map session;
	private SessionObjectDTO sessionDTO;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	
	public static final String LIN_MOBILE_DFP_NETWORK_CODE="9331149";
	
	@SuppressWarnings({ "static-access", "deprecation" })
	private void init(){		
    	log.info(" now going to build dfpSession and dfpServices instances...");
		try {
			if(dfpSession ==null){
				log.info("Create DFP session...");
				dfpSession = DFPAuthenticationUtil.getDFPSession(LIN_MOBILE_DFP_NETWORK_CODE);
			}
			if(dfpServices ==null){
				log.info("Create dfp Service session...");
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
			}
			
		} catch (ValidationException e) {
			log.severe("ValidationException:"+e.getMessage());			
		} catch (Exception e) {
			log.severe("Exception:"+e.getMessage());	
		}		
	}
	
	public String execute(){		
		log.info("campaignSetup executes..");
	
		return Action.SUCCESS;
	}	
	
	
    public String addCampaignSetupInQueue(){	
		log.info("addCampaignSetup in task queue...");
		
		String jsonDataStr=request.getParameter("jsonData");
		String mediaPlanId=request.getParameter("smartMediaPlanId");
		String userId=request.getParameter("userId");
		String campaignStatus=request.getParameter("campaignStatus");
		String campaignId=request.getParameter("campaignId");
		
		log.info("Going to setup campaign from smart media plan --mediaPlanId:"+mediaPlanId+",campaignId:"+campaignId+
				"\nuserId:"+userId+", campaignStatus:"+campaignStatus+"\n jsonDataStr : "+jsonDataStr);
		if(jsonDataStr !=null){
			jsonObject = (JSONObject) JSONSerializer.toJSON(jsonDataStr);
		}
		
		
		if(campaignId !=null && userId !=null){
			
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=null;
			if(mediaPlanId !=null){
				log.info("From smart media plan screen - mediaPlanId:"+mediaPlanId);
				if(jsonObject !=null){
					log.info("Save/Update media plan id:"+mediaPlanId+"\nSave existing first and then setup campaign..");
					smartMediaPlan= productService.saveSmartMediaPlanFromJSON(jsonObject);
				}else{
					smartMediaPlan=productService.loadSmartMediaPlan(StringUtil.getLongValue(mediaPlanId));
				}				
				
			}else{
				log.info("From campaign listing screen, load smart media plan first-- campaignId:"+campaignId);
				smartMediaPlan=productService.loadSmartMediaPlan(campaignId);
			}			
			if(smartMediaPlan ==null){
				status="error- Please generate a media plan for this campaign first.";
			}else{
				mediaPlanId=smartMediaPlan.getId()+"";
				
		    	/*	if(smartMediaPlan ==null){
					log.info("No media plan found in datastore for id:"+mediaPlanId+"\nSave existing first and then setup campaign..");
					smartMediaPlan= productService.saveSmartMediaPlanFromJSON(jsonObject);
					mediaPlanId=smartMediaPlan.getId()+"";
				}*/			
				CampaignStatusEnum campaignEnumStatus=CampaignStatusEnum.Draft;	    
				ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				Boolean hasMediaPlan=true;
				Boolean isProcessing=true;
				Boolean isSetupOnDFP=false;
				service.updateSmartCampaignFlags(campaignEnumStatus, campaignId,
						hasMediaPlan, isProcessing, isSetupOnDFP, null);
				
				String taskURL="/setupCampaignOnDFP.lin";
				TaskQueueUtil.addCampaignSetupTaskInQueue(taskURL,userId, mediaPlanId,campaignId,campaignStatus);
				status="INPROGRESS";
			}
			
			
		}else{
			status="error : Invalid mediaPlanId :"+mediaPlanId+" or campaignId:"+campaignId+" or userId:"+userId;
			jsonObject.put("error", status);
		}
		log.info("Action ends - status:"+status);
		return Action.SUCCESS;
	}
    
 
    public String setupCampaign(){
    	
    	String mediaPlanId=request.getParameter("smartMediaPlanId");
    	String campaignId=request.getParameter("campaignId");
    	String userId=request.getParameter("userId");
    	String key=request.getParameter("key");
    	log.info("Setup campaign using task queue....userId:"+userId+", mediaPlanId:"+mediaPlanId+", campaignId:"+campaignId);
    	
    	SmartMediaPlanObj smartMediaPlan=null;
    	ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
    	
    	//Campaign flags, don't change without any impact analysis
    	CampaignStatusEnum campaignEnumStatus=null;
    	Boolean hasMediaPlan=true;
    	Boolean isProcessing=false;
		Boolean isSetupOnDFP=null;
		
    	if(mediaPlanId !=null && campaignId !=null && userId !=null ){
    	  	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    		smartMediaPlan=productService.loadSmartMediaPlan(StringUtil.getLongValue(mediaPlanId));   		
    		
        	IDFPCampaignSetupService campaignService=new DFPCampaignSetupService();
    		
    		try {    			
    			if(smartMediaPlan !=null){   
    				smartMediaPlan=campaignService.setupCampaignOnDFP(smartMediaPlan);
    				log.info("Campaign has been setup successfully with orderId:"+smartMediaPlan.getDfpOrderId());
    				status="Campaign has been setup successfully with dfp order id -- "+smartMediaPlan.getDfpOrderId();
    				isSetupOnDFP=true;
    				campaignEnumStatus=CampaignStatusEnum.Ready;
    				log.info("Going to update campaign and placements in datastore after setup in DFP...");
    				
    				boolean updateCampaign=service.updateSmartCampaignAndPlacementsAfterAdServerSetup(smartMediaPlan);
    				log.info("update status : "+updateCampaign);
    				
    				smartMediaPlan=campaignService.setupPlacementsOnDFP(smartMediaPlan);
					log.info("Last step -- going to update campaign and placements in datastore after setup in DFP...");
					service.updateSmartCampaignAndPlacementsAfterAdServerSetup(smartMediaPlan);
					log.info("Update status : "+updateCampaign);	
					campaignEnumStatus=CampaignStatusEnum.Ready;
    			}
    			
    		} catch (Exception e) {
    			log.severe("Exception :"+e.getMessage());
    			status="error : "+e.getMessage();
    			jsonObject.put("error", status);
    		}finally{
    			
    			log.info("Inside Finally...");
    			service.updateSmartCampaignFlags(campaignEnumStatus, campaignId,
    					hasMediaPlan, isProcessing, isSetupOnDFP, null);
    			
    			pushMessageToChannel(smartMediaPlan, key, status);
    		}
    	}else{
    		status="error - Invalid smartMediaPlanId : "+mediaPlanId+", or campaignId : "+campaignId+" or userId : "+userId;
    		service.updateSmartCampaignFlags(campaignEnumStatus, campaignId,
					hasMediaPlan, isProcessing, isSetupOnDFP, null);
    	}  
    	
		log.info(status);
    	return Action.SUCCESS;
    }
    
    /*
     * After creating campaign on DFP, send notification using Channel API
     */
    public void pushMessageToChannel(SmartMediaPlanObj smartMediaPlan,String key,String status){	
    	log.info("After campaign setup, send notification to channel API..key:"+key);
	    ChannelService channelService = ChannelServiceFactory.getChannelService();
	  
	    JSONObject responseObj=new JSONObject();
	    responseObj.put("campaignId", smartMediaPlan.getCampaignId());
	    responseObj.put("smartMediaPlanId", smartMediaPlan.getId()+"");
	    responseObj.put("dfpOrderId", smartMediaPlan.getDfpOrderId()+"");
	    responseObj.put("requestType", "CAMPAIGN_SETUP");
	    responseObj.put("status", status);
	    log.info("Message sent :" + responseObj.toString());
    	channelService.sendMessage(new ChannelMessage(key,responseObj.toString()));
			
    }
    
    public String setupLineItems(){
    	String mediaPlanId=request.getParameter("smartMediaPlanId");
		log.info("Going to setup lineItems for campaign with mediaPlanId"+mediaPlanId);
		if(mediaPlanId !=null){			
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(StringUtil.getLongValue(mediaPlanId));
			if(smartMediaPlan !=null){
				long orderId=smartMediaPlan.getDfpOrderId();
				if(orderId >0){
					log.info("This campaign has already setup in DFP with orderId -"+orderId);
				}
				IDFPCampaignSetupService campaignService=new DFPCampaignSetupService();
				try {
					smartMediaPlan=campaignService.setupPlacementsOnDFP(smartMediaPlan);
					if(smartMediaPlan !=null){						
						status=""+smartMediaPlan.getDfpOrderId();						
						log.info("Last step -- going to update campaign and placements in datastore after setup in DFP...");
						ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
						boolean updateCampaign=service.updateSmartCampaignAndPlacementsAfterAdServerSetup(smartMediaPlan);
						log.info("updateCampaign in datastore after setup in DFP : "+updateCampaign);
					}					
				} catch (Exception e) {
					log.severe("Exception :"+e.getMessage());
					status="error : "+e.getMessage();
				}
			}else{
				log.info("No smart media plan found for this mediaPlanId -"+mediaPlanId);
			}
		}
		
    	return Action.SUCCESS;
    }
   
    public String checkCampaignSetupStatus(){
    	String campaignId=request.getParameter("campaignId");
    	log.info("Check campaign setup status ... campaignId :"+campaignId);
    	if(campaignId !=null){
    		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
        	SmartCampaignObj campaignObj=service.loadSmartCampaign(campaignId);
        	if(campaignObj !=null){
        		status=campaignObj.getDfpOrderId()+"";
        	}else{
        		status="Campaign not found for campaignId : "+campaignId;
        	}
        	log.info("status : "+status);
    	}else {
    		log.warning("campaignId is null.");
    	}
    	return Action.SUCCESS;
    }
    
    public String approveOrderViaTaskQueue(){
    	
    	String orderIdStr=request.getParameter("orderId");
    	String smartMediaPlanId=request.getParameter("smartMediaPlanId");
    	String campaignId=request.getParameter("campaignId");
    	
		String userId=request.getParameter("userId");
		log.info("campaignId : "+campaignId+"smartMediaPlanId : "+smartMediaPlanId
				+", orderId:"+orderIdStr+" userId:"+userId);
		
		if(smartMediaPlanId !=null && orderIdStr !=null && campaignId !=null && userId !=null){			
			
			ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			UnifiedCampaignDTO campaignDTO=service.loadCampaign(campaignId);
			if(campaignDTO!=null ){
				if(!campaignDTO.isHasSetupOnDFP()){
					boolean hasMediaPlan=true;
					boolean isProcessing=true;
					boolean isSetupOnDFP=false;
					boolean updateCampaign=service.updateSmartCampaignFlags(null, campaignId,
							hasMediaPlan, isProcessing, isSetupOnDFP, null);
					log.info("Campaign updated...with isProcessing:"+isProcessing+" and updateCampaign:"+updateCampaign);
					String taskURL="/launchCampaignOnDFP.lin";			        
					TaskQueueUtil.addLaunchCampaignTaskInQueue(taskURL,userId, smartMediaPlanId,orderIdStr);
			        
			        status="INPROCESS";
				}else{
					status="Campaign is already setup on DFP";
				}
			}
			
		}else{
			log.warning("Invalid paramerters : (orderId , smartMediaPlanId, campaignId, userId can not be null)");
			status="error - required parameters are missing.";
		}
		
		
    	
    	return Action.SUCCESS;
    }
    
    public String approveOrder(){
    	String orderIdStr=request.getParameter("orderId");
    	String smartMediaPlanId=request.getParameter("smartMediaPlanId");
    	String userId=request.getParameter("userId");    	
    	//String key=request.getParameter("key"); //For channel API
    	String campaignId=request.getParameter("campaignId");
    	
    	log.info("campaignId : "+campaignId+"smartMediaPlanId : "+smartMediaPlanId
				+", orderId:"+orderIdStr+" userId:"+userId);
    	ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
    	CampaignStatusEnum campaignEnumStatus=null;
    	Boolean hasMediaPlan=true;
    	Boolean isProcessing=false;
		Boolean isSetupOnDFP=true;
		
    	if(orderIdStr !=null && campaignId !=null){			
		
			long orderId=StringUtil.getLongValue(orderIdStr);
			SmartMediaPlanObj smartMediaPlan=null;
			IDFPCampaignSetupService campaignService=new DFPCampaignSetupService();
			
			try {
				init();
				hasMediaPlan=true;
				isProcessing=true;
				isSetupOnDFP=true;
				boolean updateCampaign=service.updateSmartCampaignFlags(null, campaignId,
						hasMediaPlan, isProcessing, isSetupOnDFP, null);
				log.info("Campaign updated...with isProcessing:"+isProcessing+", updateCampaign:"+updateCampaign);
				
				List<LineItem> lineItemList=campaignService.getLineItemsNeedsCreative(dfpServices, dfpSession, orderId);
				log.info("lineItemList need creatives:"+lineItemList.size());
				if(lineItemList !=null && lineItemList.size()>0){
					//campaignEnumStatus=CampaignStatusEnum.INACTIVE;
					status="error : There are some line items still need creatives.";
				}else{
					boolean approve=campaignService.approveOrder(orderId, dfpServices, dfpSession);
					log.info("Approve order--"+approve);
					if(approve){
						isSetupOnDFP=true;
						log.info("Now update smart media plan and campaign status..."+CampaignStatusEnum.Running);
						IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
						if(smartMediaPlanId !=null){
							smartMediaPlan=productService.loadSmartMediaPlan(StringUtil.getLongValue(smartMediaPlanId));
						}else{
							smartMediaPlan=productService.loadSmartMediaPlan(campaignId);
						}
						 
						
						if(smartMediaPlan !=null){
							String campaignStatus=CampaignStatusEnum.Running.name();
							smartMediaPlan.setCampaignStatus(campaignStatus);
							productService.saveSmartMediaPlan(smartMediaPlan);
							campaignId=smartMediaPlan.getCampaignId();
							log.info("Now update campaign - "+campaignId+" with status..."+campaignStatus);
							campaignEnumStatus=CampaignStatusEnum.Running;
							//boolean updateCampaign=service.updateSmartCampaignStatus(CampaignStatusEnum.ACTIVE, campaignId);
							
							status=campaignStatus;
						}else{				
							status="No smart media plan found for update : smartMediaPlanId  :: "+smartMediaPlanId;
						}
					}
				}
				
				
			} catch (ApiException_Exception e) {
				log.severe("Failed to approve order -"+e.getMessage());
				status="error : Failed to approve order ::"+e.getMessage();
			} catch (Exception e) {
				log.severe("Exception -"+e.getMessage());
				status="error :: "+e.getMessage();
			}finally{
				
				log.info("Inside Finally...status:"+status);
				isProcessing=false;
				boolean updateCampaign=service.updateSmartCampaignFlags(campaignEnumStatus, 
						campaignId,hasMediaPlan,isProcessing,isSetupOnDFP, null);
				
				log.info("updateCampaign in datastore after setup in DFP : "+updateCampaign+", isProcessing:"+isProcessing);
				
    			//pushMessageToChannel(smartMediaPlan, key, status);	
			}
			
		}else{
			log.info("Invalid paramerters : (orderId , campaignId can not be null)");
			status="error - required parameters are missing.";
			isProcessing=false;
			boolean updateCampaign=service.updateSmartCampaignFlags(campaignEnumStatus, 
					campaignId,hasMediaPlan,isProcessing,isSetupOnDFP, null);			
			log.info("updateCampaign in datastore after setup in DFP : "+updateCampaign+" , isProcessing:"+isProcessing);
		}    	
		
    	log.info(status);
    	return Action.SUCCESS;
    }
    
    public String getAllLineItemsNeedsCreative(){
    	String orderIdStr=request.getParameter("orderId");
    	if(orderIdStr !=null){
    		long orderId=StringUtil.getLongValue(orderIdStr);
    		init();
    		IDFPCampaignSetupService campaignService=new DFPCampaignSetupService();
    		try {
				List<LineItem> lineItemList=campaignService.getLineItemsNeedsCreative(dfpServices, dfpSession, orderId);
				log.info("lineItemList need creatives:"+lineItemList.size());
			} catch (ApiException_Exception e) {				
				status="error : Failed to getLineItems ::"+e.getMessage();
				log.severe(status);
			}
    		
    	}
    	return Action.SUCCESS;
    }
    public String setUpOrder(){		
		log.info("setUpOrder executes..");
		
		String dfpAccount=request.getParameter("dfpAccount");
		String publisherName=request.getParameter("publisherName");
		String orderName=request.getParameter("orderName");
		String agencyName=request.getParameter("agencyName");
		String advertiserName=request.getParameter("advertiserName");
		String campaignName=request.getParameter("campaignName");
		String placement=request.getParameter("placement");
		String adSizes=request.getParameter("adSizes");
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String rateStr=request.getParameter("rate");
		String unitsBoughtStr=request.getParameter("unitsBought");
		String flightDuration=startDate+":"+endDate;
		String [] adSizeArr={"300x250","300x50"};
		if(adSizes!=null){
			adSizeArr=adSizes.split(",");
			//adSizeArr=adSizes.split("x");
		}
		double rate=4;
		if(rateStr!=null){
			rate=Double.parseDouble(rateStr);
		}
		if(unitsBoughtStr==null){
			unitsBoughtStr="0";//default....
		}
		if(advertiserName ==null || agencyName==null || campaignName==null ||
				( (publisherName!=null && publisherName.equals("-1")) || publisherName ==null)  ){
			
			log.info("publisherName, advertiserName,agencyName and campaignName  can not be blank or null");
			status="Failed: publisherName, advertiserName,agencyName and campaignName can not be blank or null";
		}else{
			init();
			IDFPCampaignSetupService dfpService=new DFPCampaignSetupService();
			
			DFPCompanyDTO companyDTO=new DFPCompanyDTO(0, advertiserName, null,null, null, null,null,
					dfpSession.getNetworkCode(), DFPCompanyEnum.ADVERTISER);
			
			Company advertiserCompany=dfpService.createCompany(dfpServices, dfpSession,companyDTO);
			
			companyDTO=new DFPCompanyDTO(0, agencyName, null,null, null, null,null,
					dfpSession.getNetworkCode(), DFPCompanyEnum.AGENCY);
			
			Company agencyCompany=dfpService.createCompany(dfpServices, dfpSession,companyDTO);
			String advertiserId=advertiserCompany.getId()+"";
			String agencyId=agencyCompany.getId()+"";
			log.info("advertiserId:"+advertiserId+" and agencyId:"+agencyId);
			
			AdvertiserObj advertiser=new AdvertiserObj(Long.parseLong(advertiserId), advertiserName,dfpSession.getNetworkCode());
			AgencyObj agency=new AgencyObj(Long.parseLong(agencyId), agencyName,dfpSession.getNetworkCode());
			
			long orderId;
			try {
				OrderDTO orderDTO = dfpService.createNewOrder(agency, advertiser, campaignName,startDate,endDate);
				orderId=orderDTO.getOrderId();
				String topLevelAdUnit=dfpService.createTopLevelAdUnitName(publisherName);
				log.info("topLevelAdUnit:"+topLevelAdUnit+" and publisherName:"+publisherName);
			
				AdUnitDTO adUnitTopLevel=dfpService.createTopLevelAdUnit(dfpServices, dfpSession, topLevelAdUnit,adSizeArr);
				String topLevelAdUnitId=adUnitTopLevel==null?"0":adUnitTopLevel.getId()+"";
				orderName=dfpService.generateOrderName(agencyName, advertiserName, campaignName,startDate);
				
				String adUnitName2=agencyName.toLowerCase()+"_"+campaignName.toLowerCase()+"_"+placement.toLowerCase();
			    adUnitName2=adUnitName2.replaceAll("_", "").trim();
				   
			    AdUnitDTO adUnitTopLevel2=dfpService.createAdUnit2(dfpServices, dfpSession, topLevelAdUnitId,adUnitName2, adSizeArr);
			    String adUnitId2=adUnitTopLevel2==null?"0":adUnitTopLevel2.getId()+"";
				String lineItemName=dfpService.generateLineItemName(advertiserName, campaignName,
						flightDuration, placement,publisherName,null);
				long lineItemId=dfpService.createNewLineItem(rate, CostType.CPM, LineItemType.SPONSORSHIP, lineItemName, 
						orderId, startDate, endDate, adUnitId2, unitsBoughtStr, adSizeArr, dfpServices, dfpSession, null);
				status="orderId-"+orderId+" and lineItemId:"+lineItemId;
			} catch (ApiException_Exception | DataServiceException e) {
				status=e.getMessage();				
			}			
		}
		log.info("done.."+status);
		return Action.SUCCESS;
	}	
	
	public String setUpAdUnit(){		
		log.info("setUpAdUnit executes..");		
		String adUnitName=request.getParameter("adUnitName");
		String level=request.getParameter("adUnitLevel");
		String orderName=request.getParameter("orderName");
		String agencyName=request.getParameter("agencyName");		
		String placement=request.getParameter("placement");
		String adSizes=request.getParameter("adSizes");
		try{
			if(level !=null && level.equals("2")){
				if(orderName ==null || agencyName==null || placement==null || adUnitName==null || adSizes==null){
					log.info("orderName,agencyName,adUnitName,adSizes and placement can not be blank or null");
					status="Failed: orderName,agencyName,adUnitName,adSizes and placement can not be blank or null";
				}else{
					init();
					IDFPCampaignSetupService dfpService=new DFPCampaignSetupService();
					String [] adSizeArr=adSizes.split(",");
					AdUnitDTO adUnitTopLevel=dfpService.createTopLevelAdUnit(dfpServices, dfpSession, adUnitName,adSizeArr);
					String topLevelAdUnitId=adUnitTopLevel==null?"0":adUnitTopLevel.getId()+"";
					String adUnitName2=agencyName.toLowerCase()+"_"+orderName.toLowerCase()+"_"+placement.toLowerCase();
					adUnitName2=adUnitName2.replaceAll(" ", "_");				    
				    AdUnitDTO adUnitLevel2=dfpService.createAdUnit2(dfpServices, dfpSession, topLevelAdUnitId,adUnitName2,adSizeArr);
					String adUnitId=adUnitLevel2==null?"0":adUnitLevel2.getId()+"";
					//orderId = dfpService.createNewOrder(agencyName, advertiserName, campaignName, dfpServices, dfpSession);
					status=adUnitId;
					log.info("adUnitTopLevelCode : "+adUnitTopLevel.getAdUnitCode()+" and adUnitCodeLevel2 : "+adUnitLevel2.getAdUnitCode());
				}
			}else if(level !=null && level.equals("1")){
				if(adUnitName ==null || adUnitName.trim().length()==0 || adSizes==null){
					log.info("adUnitName and adSizes can not be blank or null");
					status="Failed: adUnitName and adSizes can not be blank or null";
				}else{
					init();
					IDFPCampaignSetupService dfpService=new DFPCampaignSetupService();
					String [] adSizeArr=adSizes.split(",");
					AdUnitDTO adUnitTopLevel=dfpService.createTopLevelAdUnit(dfpServices, dfpSession, adUnitName,adSizeArr);
					String adUnitId=adUnitTopLevel==null?"0":adUnitTopLevel.getId()+"";
					status=adUnitId;
				}
			}else{
				status="Invalid adUnit level, please enter 1 or 2";
			}
		}catch(ApiException_Exception e){
			status=e.getMessage();
		}		

		log.info("done.."+status);
		return Action.SUCCESS;
	}	
	public String setUpCompany(){
		String companyName=request.getParameter("companyName");
		String companyType=request.getParameter("companyType");
		String address=request.getParameter("address");
		String state=request.getParameter("state");
		String phone=request.getParameter("phone");
		String email=request.getParameter("email");
		String fax=request.getParameter("fax");
		
		log.info("setUpCompany action...companyName:"+companyName+" and companyType:"+companyType);
		if(companyName==null || companyName.trim().length()==0){
			status="Company name can not be blank or null.";
		}else{
			init();
		
			DFPCompanyDTO companyDTO=null;
			
			boolean createCompany=false;
			if(companyType !=null && companyType.equalsIgnoreCase("AGENCY") ){				
				companyDTO=new DFPCompanyDTO(0, companyName, address,state, phone, email,fax,
						dfpSession.getNetworkCode(), DFPCompanyEnum.AGENCY);
				createCompany=true;			
				
			}else if(companyType !=null && companyType.equalsIgnoreCase("ADVERTISER")){
				companyDTO=new DFPCompanyDTO(0, companyName, address,state, phone, email,fax,
						dfpSession.getNetworkCode(), DFPCompanyEnum.ADVERTISER);				
				createCompany=true;				
			}else{
				createCompany=false;
				status="Invaild company type:"+companyType+". It should be ADVERTISER or AGENCY";
			}
			if(createCompany){				
				IDFPCampaignSetupService dfpService=new DFPCampaignSetupService();
				Company company=dfpService.createCompany(dfpServices, dfpSession,companyDTO);			
				if(company !=null){
					status=company.getId()+"";	
				}				
			}
		}
		
		log.info("done.."+status);
		return Action.SUCCESS;
	}

		
	public String gptTagGenerator(){
		
		String networkCode=request.getParameter("networkCode");
		if(networkCode==null){
			init();
			networkCode=dfpSession.getNetworkCode();
		}
		String adUnitCode1=request.getParameter("adUnitCode1");
		String adUnitCode2=request.getParameter("adUnitCode2");
		String adSize=request.getParameter("adSize");
		log.info("networkCode:"+networkCode+" and adUnitCode1:"+adUnitCode1+" and adUnitCode2:"+adUnitCode2+" and adSize:"+adSize);
		if(adUnitCode1 ==null && adUnitCode2 ==null && adSize ==null){
			status="adUnitCode1, adUnitCode2 and adSize can not be blank..";
		}else{
			IDFPCampaignSetupService dfpService=new DFPCampaignSetupService();
			status=dfpService.generateGPTPassbackTags(networkCode, adUnitCode1, adUnitCode2, adSize);
			//log.info("tag script :"+status);
		}
		
		return Action.SUCCESS;
	}

	@SuppressWarnings("static-access")
	public String forecastInventory(){
		String adUnitId=request.getParameter("adUnitId");
		String adUnitName=request.getParameter("adUnitName");
		String adSize=request.getParameter("adSize");
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String networkCode=request.getParameter("networkCode");
		
		String [] creativeSizeArr=null;
		if(adSize !=null){
			creativeSizeArr=adSize.split(",");
		}
		
		jsonObject=new JSONObject();
		
		if(adUnitId !=null && adSize !=null && startDate !=null && endDate !=null && networkCode !=null){
			
			IForecastInventoryService forecastService=new ForecastInventoryService();
			try {			
				dfpSession=DFPAuthenticationUtil.getDFPSession(networkCode);
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
				ForcastDTO forecastDTO=forecastService.loadForecastInventoryByAdUnit(dfpServices, dfpSession, adUnitId, adUnitName,
						startDate, endDate,  LineItemType.STANDARD, CostType.CPM, creativeSizeArr);
				if(forecastDTO !=null){
					JSONArray jsonArray=new JSONArray();
					JSONObject forecastObj=new JSONObject();
					forecastObj.put("id", forecastDTO.getAdUnitId());
					forecastObj.put("name", forecastDTO.getAdUnitName());
					forecastObj.put("available_impression", forecastDTO.getAvailableImpressions());
					forecastObj.put("delivered_impression", forecastDTO.getDeliveredImpressions());
					forecastObj.put("reserved_impression", forecastDTO.getReservedImpressions());
					forecastObj.put("matched_impression", forecastDTO.getMatchedImpressions());
					forecastObj.put("publisher_id", forecastDTO.getPublisherId());
					forecastObj.put("publisher", forecastDTO.getPublisherName());
					jsonArray.add(forecastObj);
					jsonObject.put("forecasted_ad_unit_data", jsonArray);
				}
			} catch (ApiException_Exception e) {
				jsonObject.put("error", "ApiException_Exception : "+e.getMessage());
				log.severe("ApiException_Exception : "+e.getMessage());
			} catch (Exception e) {
				jsonObject.put("error", "Exception : "+e.getMessage());
				log.severe("Exception : "+e.getMessage());
			}
		}else{
			jsonObject.put("error", "Please provide all mandatory fileds ( adUnitId, adSize, networkCode, startDate and endDate)");
		}
		
		return Action.SUCCESS;
	}

	public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			e.printStackTrace();
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}
	
	public String loadGPTTags(){
		String campaignId=request.getParameter("campaignId");
		log.info("campaignId : "+campaignId);
		jsonObject=new JSONObject();
		if(campaignId !=null){
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			jsonObject=productService.loadGPTTagsFromSmartMediaPlan(campaignId);
		}		
		return Action.SUCCESS;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	public void setSession(Map session) {
		this.session=session;
		
	}
	
	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}
	
	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}



	public String getStatus() {
		return status;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
}
