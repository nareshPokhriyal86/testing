package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.lin.dfp.api.IForecastInventoryService;
import com.lin.dfp.api.impl.ForecastInventoryService;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ClientIOReportDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.IMediaPlanService;
import com.lin.web.service.IProductService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.TaskQueueUtil;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;

public class MediaPlanAction implements ServletRequestAware,SessionAware{

	static final Logger log = Logger.getLogger(MediaPlanAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	
	private SessionObjectDTO sessionDTO;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
		
	private ProposalDTO proposalDTO;	
	private Map<String,String> advertiserMap;
	private Map<String,String> agencyMap;
	private String placementId;
	private String siteId;
	private JSONObject placementMap;
	private Map<String,String> adSizeMap;
	private Map<String,String> adFormatMap;
	private Map<String,String> forecastingMap;
	private Map<String,String> geoTargetMap;
	private String status;
	private List<PlacementObj> placementList;
	
	private InputStream inputStream;
	private String fileName;
	private String contentType;
	private String campaignId;
	
	private JSONObject jsonData;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	public String publisherViewPageName = (LinMobileConstants.APP_VIEWS[0])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String advertiserViewPageName = (LinMobileConstants.APP_VIEWS[1])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String unifiedCampaign = (LinMobileConstants.APP_VIEWS[2])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String newsAndResearchPageName = (LinMobileConstants.APP_VIEWS[3])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String adminPageName = (LinMobileConstants.APP_VIEWS[4])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mySettingPageName = LinMobileConstants.APP_VIEWS[5]
			.split(LinMobileConstants.ARRAY_SPLITTER)[1].trim();
	public String poolPageName = (LinMobileConstants.APP_VIEWS[6])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String campaignPerformancePageName = (LinMobileConstants.APP_VIEWS[7])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mapEngineName = (LinMobileConstants.APP_VIEWS[8])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String report = (LinMobileConstants.APP_VIEWS[9])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	
	public String getPublisherIdInBQ() {
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		String publisherIdInBQ = "";
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(sessionDTO.isSuperAdmin()) {
					publisherIdInBQ = "1";
				}
				else if(sessionDTO.isAdminUser()) {
					CompanyObj companyObj = userService.getCompanyIdByAdminUserId(sessionDTO.getUserId());
					if(companyObj != null && companyObj.getCompanyName() != null) {
						log.info("companyObj.getCompanyName() : "+companyObj.getCompanyName());
						//publisherIdInBQ = linMobileBusinessService.getPublisherBQId(companyObj.getCompanyName().trim());
						publisherIdInBQ = userService.getPublisherBQId(companyObj);
					}
				}
				else {
					List<CompanyObj> companyObjList = userDetailsDAO.getSelectedDemandPartnersByUserId(sessionDTO.getUserId());
					if(companyObjList != null && companyObjList.size()==1) {
						log.info("companyObj.getCompanyName() : "+companyObjList.get(0).getCompanyName());
						publisherIdInBQ = userService.getPublisherBQId(companyObjList.get(0));
					}
				}
			}
		} catch(Exception e) {
			log.info("Exception in getPublisherId in NewAdvertiserViewAction : "+e.getMessage());
			
		}
		log.info("publisherIdInBQ : "+publisherIdInBQ);
		return publisherIdInBQ;
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
			
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}
	
	public String execute(){
		
		String proposalId=request.getParameter("proposalId");
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		
		String userId=sessionDTO.getUserId()+"";
		log.info("MediaPlan action..userId:"+userId+" and proposalId:"+proposalId);
		
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		if(proposalId == null){	
			log.info("No proposalId, please provide a proposalId..");
			return "error";
		}else{
			proposalDTO=mediaPlanService.loadProposal(StringUtil.getLongValue(proposalId));
			advertiserMap=mediaPlanService.getAllAdvertiserFromDataStore();
			agencyMap=mediaPlanService.getAllAgenciesFromDataStore();
			proposalDTO.setAdvertiser(advertiserMap.get(proposalDTO.getAdvertiser()));
			proposalDTO.setAgency(agencyMap.get(proposalDTO.getAgency()));
			
			placementMap=mediaPlanService.placementMap(proposalId);
			log.info("placementMap:"+placementMap.toString());
			adSizeMap=mediaPlanService.loadAllAdSize();
			adFormatMap=mediaPlanService.loadAllAdFormats();
			return Action.SUCCESS;
		}					
		
	}
	
	public String savePlacement(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		String userId=sessionDTO.getUserId()+"";
		
		String placementData=request.getParameter("placementData");
		log.info("savePlacement action called....placementData:"+placementData+" \nand loggedIn userId:"+userId);
		
		
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		mediaPlanService.savePlacements(placementData, userId);
		
		
		return Action.SUCCESS;
	}
	
	
	
	public String deletePlacement(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		
		String placementId=request.getParameter("placementId");
		log.info("deletePlacement action called..placementId:"+placementId);
		if(placementId !=null && placementId.trim().length()>0){
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			boolean isDeleted=mediaPlanService.deletePlacement(placementId);
			if(isDeleted){
				status="Success";
			}else{
				log.warning("Falied to delete placements with id :"+placementId+" , from datastore.");
				status="Failed";
			}
		}else{
			log.warning("Invalid placementId :"+placementId);
			status="Failed";
		}		
		return Action.SUCCESS;
	}
	
	public String deleteSite(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		String siteId=request.getParameter("siteId");
		log.info("deleteSite action called..siteId:"+siteId);
		if(siteId !=null && siteId.trim().length()>0){
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			boolean isDeleted=mediaPlanService.deletePlacementSite(siteId,sessionDTO.getUserId());
			if(isDeleted){
				status="Success";
			}else{
				status="Failed";
			}
		}else{
			log.warning("Invalid siteId :"+siteId);
			status="Failed";
		}		
		return Action.SUCCESS;
	}
	
	/*
	 * This action is to load all dropdown required at MediaPlanner page
	 */
	public String fetchMediaPlanDropDowns(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		log.info("Fetch all dropdowns...");
		if(sessionDTO != null && sessionDTO.getUserId()>0){
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			adSizeMap=mediaPlanService.loadAllAdSize();
			adFormatMap=mediaPlanService.loadAllAdFormats();
			status="Success";
			log.info("adSizeMap:"+adSizeMap.size()+" and adFormatMap:"+adFormatMap.size());
		}else{
			log.warning("Invalid user");
			status="Failed";
		}
		return Action.SUCCESS;
	}
	
	/*
	 * Load forecasted data from SellThrough table
	 * using siteName
	 */
	public String loadForcastedData(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		String siteName=request.getParameter("site");
		String proposalId=request.getParameter("proposalId");
		log.info("load forcasting data for site:"+siteName);
		if(siteName !=null && proposalId !=null && siteName.trim().length()>0){
			
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			proposalDTO=mediaPlanService.loadProposal(StringUtil.getLongValue(proposalId));			
			String startDate=DateUtil.getFormatedDate(proposalDTO.getFlightStartDate(),"dd-MM-yyyy","yyyy-MM-dd 00:00:00");
			String endDate=DateUtil.getFormatedDate(proposalDTO.getFlightEndDate(),"dd-MM-yyyy","yyyy-MM-dd 00:00:00");
			forecastingMap=mediaPlanService.loadForcastingDataBySite(startDate, endDate, siteName);
			status="Success";
		}else{
			log.warning("Invalid request, provide valid siteName and proposalId");
			status="Failed";
		}
		return Action.SUCCESS;
	}
	
	public String createInsertionOrder(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		String proposalId=request.getParameter("proposalId");
		if(proposalId !=null && LinMobileUtil.isNumeric(proposalId)){
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			proposalDTO=mediaPlanService.loadProposal(StringUtil.getLongValue(proposalId));
			geoTargetMap=mediaPlanService.loadAllGeoTargets();
		}else{
			log.warning("Invalid proposalId, provide valid proposalId");
			status="Failed";
		}
		
		return Action.SUCCESS;
	}
	
	
	public String newMediaPlanner() {
		return Action.SUCCESS;
	}
	
    
    
	public String createSmartMediaPlan(){
    	campaignId=request.getParameter("campaignId");
    	String campaignStatus=request.getParameter("status");
    	String userId=request.getParameter("userId");
    	String planType=request.getParameter("planType");
		log.info("Create media plan for campaignId:"+campaignId+" , campaignStatus:"+campaignStatus+" and userId:"+userId);
		int planTypeFlag=0;
		Boolean hasMediaPlan=false;
		SmartMediaPlanObj smartMediaPlan=null;
		ISmartCampaignPlannerService campaignService =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		jsonData=new JSONObject();
		if(campaignId != null && userId !=null){
			UnifiedCampaignDTO campaignDTO=campaignService.loadCampaign(campaignId);
			
			if(campaignDTO !=null){
				
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				
				List<SmartCampaignPlacementObj> placementList=campaignDTO.getPlacementByCampaignList();
				
				Map<String,List<ProductsObj>> campaignProducts=new HashMap<String, List<ProductsObj>>();
				Map<String,Map<String,Long>> campaignProductsAllocationMap=new HashMap<String, Map<String,Long>>();
				
				Map<String,ForcastDTO> allProductsForecastMap=new HashMap<String, ForcastDTO>();
				if(placementList !=null && placementList.size()>0){
					if(planType!=null && planType.equals("auto")){
						for(SmartCampaignPlacementObj placement:placementList){
							
							String placementIdStr=placement.getPlacementId().toString();
							
							if(placement.getStartDate()==null || placement.getStartDate().trim().length()==0){
								placement.setStartDate(campaignDTO.getStartDate());
							}
							if(placement.getEndDate()==null || placement.getEndDate().trim().length()==0){
								placement.setEndDate(campaignDTO.getEndDate());
							}
							List<ProductsObj> productListByPlacement=productService.searchProducts(placement);
							campaignProducts.put(placementIdStr, productListByPlacement);
							
							
							Map<String,ForcastDTO> productForecastMap=matchInventoryByPlacement(productListByPlacement, placement);
							
							
							Iterator<ProductsObj> itr = productListByPlacement.iterator();
							while (itr.hasNext()) {
								ProductsObj product = itr.next(); 
								if(!productForecastMap.containsKey(product.getId())){
									log.info("Forecasting map does not conatin this product.. remove this...productId--"+product.getId());
									 itr.remove();
								}
							}	
						
							Map<String,Long> productWiseAllocationMap=allocateImprssionToProducts(placement, productForecastMap,
									productListByPlacement);
							if(productForecastMap !=null && productForecastMap.size()>0){
								allProductsForecastMap.putAll(productForecastMap);
							}
							
							if(productWiseAllocationMap !=null && productWiseAllocationMap.size()>0){
								
								log.info("After allocation for placement :"+placementIdStr+", add in map..");
								campaignProductsAllocationMap.put(placementIdStr, productWiseAllocationMap);
							}
												
						}
						log.info("Last step -- Create media plan  with allProductsForecastMap:"
						  + allProductsForecastMap.size()+" and campaignProductsAllocationMap:"+campaignProductsAllocationMap.size());
					}else{
						log.info("plan generated manually");
						planTypeFlag=1;
						//jsonData.put("isProcessing",isProcessing);
					}
					smartMediaPlan=productService.createSmartMediaPlan(campaignProducts, allProductsForecastMap, 
							campaignProductsAllocationMap, campaignDTO, planType);					
					if(smartMediaPlan !=null){
						hasMediaPlan=true;
						boolean isProcessing=campaignDTO.isHasProcessing();
						log.info("SmartMediaPlan object is created...id:"+smartMediaPlan.getId()+", isProcessing: "+isProcessing);
						
						jsonData=productService.createSmartMediaPlanJSON(smartMediaPlan);
						if(jsonData !=null){
							jsonData.put("isProcessing",isProcessing);
						}
						status="SmartMediaPlan has been generated successfully with id - "+smartMediaPlan.getId();
					}else{
						status="error - No placements available for this campaign to generate SmartMediaPlan for campaign id: "+campaignId;
						jsonData.put("error", status);
					}
						
				}else{
					status="error - No placements available for this campaign to generate SmartMediaPlan for campaign id: "+campaignId;
					jsonData.put("error", status);
				}
	
			}else{
				status="error - No campaign available for this id:"+campaignId;				
				jsonData.put("error", status);
			}	
		}else{
			status="error - Invalid campaignId :"+campaignId+" or userId:"+userId;
		}
		
		log.info(status);
		
		Boolean isProcessing=false;
		Boolean isSetupOnDFP=null;
		boolean updateCampaign=campaignService.updateSmartCampaignFlags(null, campaignId,
				hasMediaPlan, isProcessing, isSetupOnDFP, planTypeFlag);
		log.info("Campaign updated...with isProcessing:"+isProcessing+", updateCampaign:"+updateCampaign);
		
		pushMessageToUserViaChannel(smartMediaPlan, userId, campaignStatus,status);
		log.info("action ends....jsonData :"+jsonData);
		
    	return Action.SUCCESS;
    }
    
    
    /*
     * After creating smart media plan , send notification using Channel API
     */
    /**
     * @param smartMediaPlan
     * @param key
     * @param campaignStatus
     * @param status
     */
    public void pushMessageToUserViaChannel(SmartMediaPlanObj smartMediaPlan,String key,String campaignStatus,String status){	
    	log.info("After media plan creation, send notification to user via channel API..key:"+key);
	    ChannelService channelService = ChannelServiceFactory.getChannelService();
	   
	    JSONObject responseObj=new JSONObject();
	    responseObj.put("campaignId", smartMediaPlan.getCampaignId());
	    responseObj.put("smartMediaPlanId", smartMediaPlan.getId()+"");
	    responseObj.put("dfpOrderId", smartMediaPlan.getDfpOrderId()+"");
	    responseObj.put("requestType", "SMART_MEDIA_PLAN_SETUP");
	    responseObj.put("status", status);
	    log.info("Message sent :" + responseObj.toString());
    	channelService.sendMessage(new ChannelMessage(key,responseObj.toString()));
    	
    	
    }
    
	public String smartMediaPlanner(){
		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		if(sessionDTO !=null){
			log.info("User : "+sessionDTO.getUserName());
		}
		if(!isAuthorised(sessionDTO)) {
			return "unAuthorisedAccess";
		}
		String campaignStatus=request.getParameter("status");
		if(campaignId ==null){
			campaignId=request.getParameter("campaignId");
		}
		if(campaignStatus==null){
			campaignStatus=CampaignStatusEnum.Draft.name();
		}
		log.info("campaignId :"+campaignId+" and campaignStatus:"+campaignStatus);
		jsonData=new JSONObject();
		
		
		
		if(campaignId !=null){
			ISmartCampaignPlannerService campaignService =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			UnifiedCampaignDTO campaignDTO=campaignService.loadCampaign(campaignId);
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(campaignId);
			
			if(smartMediaPlan !=null){
				log.info("Already extsts this media plan for campaignId:"+campaignId);
				jsonData=productService.createSmartMediaPlanJSON(smartMediaPlan);
				boolean isProcessing=false;
				if(campaignDTO !=null){
					isProcessing=campaignDTO.isHasProcessing();
				}
				if(jsonData !=null){
					jsonData.put("isProcessing",isProcessing);
				}
				status=smartMediaPlan.getId()+"";
			}else{
				ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				Boolean hasMediaPlan=false;
				Boolean isProcessing=true;
				Boolean isSetupOnDFP=null;
				service.updateSmartCampaignFlags(null, campaignId, hasMediaPlan, isProcessing, isSetupOnDFP, null);
				log.info("Campaign updated...with isProcessing:"+isProcessing);
				
			    log.info("Create new smart media plan using task queue...");			    
			    String taskURL="/createSmartMediaPlan.lin";
			    TaskQueueUtil.addTaskInDefaultQueue(taskURL, sessionDTO.getUserId()+"", campaignId, campaignStatus,"auto");
			    status="wait";
			}
			
			   
		}else{
			status="error - Invalid campaignId";
			jsonData.put("error", status);
		}
		
		return Action.SUCCESS;
	}
	
	public String checkSmartMediaPlan(){
		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		if(sessionDTO !=null){
			log.info("User : "+sessionDTO.getUserName());
		}
		if(!isAuthorised(sessionDTO)) {
			return "unAuthorisedAccess";
		}
		String campaignStatus=request.getParameter("status");
		if(campaignId ==null){
			campaignId=request.getParameter("campaignId");
		}
		if(campaignStatus==null){
			campaignStatus=CampaignStatusEnum.Draft.name();
		}
		String planType=request.getParameter("planType");
		
		log.info("campaignId :"+campaignId+" and campaignStatus:"+campaignStatus);
		jsonData=new JSONObject();
		
		
		
		if(campaignId !=null){
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(campaignId);
			if(smartMediaPlan !=null){
				log.info("Already extsts this media plan for campaignId:"+campaignId);
				jsonData=productService.createSmartMediaPlanJSON(smartMediaPlan);
				status=smartMediaPlan.getId()+"";
			}else{
				int planTypeFlag=0;
				if(planType!=null && planType.equals("manual")){
					planTypeFlag=1;
				}
				ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				Boolean hasMediaPlan=false;
				Boolean isProcessing=true;
				Boolean isSetupOnDFP=null;
				service.updateSmartCampaignFlags(null, campaignId, hasMediaPlan, isProcessing, isSetupOnDFP, planTypeFlag);
				log.info("Campaign updated...with isProcessing:"+isProcessing);
				
			    log.info("Create new smart media plan using task queue...");			    
			    String taskURL="/createSmartMediaPlan.lin";
			    TaskQueueUtil.addTaskInDefaultQueue(taskURL, sessionDTO.getUserId()+"", campaignId, campaignStatus,planType);
			    status= planTypeFlag ==  1 ? campaignId : "INPROGRESS";
			}
			
			   
		}else{
			status="error - Invalid campaignId";
			jsonData.put("error", status);
		}
		
		return Action.SUCCESS;
	}
	
	
	
	
   
	private Map<String,ForcastDTO> matchInventoryByPlacement(List<ProductsObj> productList, 
			SmartCampaignPlacementObj placement){
		Map<String,ForcastDTO> productForecastMap=null;
				
		String startDate=placement.getStartDate();
		String endDate=placement.getEndDate();
		startDate=DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy-MM-dd");
		endDate=DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy-MM-dd");
		String [] creativeSizeArr=null;
		ForcastDTO forecastDTO=null;
		List<CreativeObj> creativeList=placement.getCreativeObj();
		List<String> creativeSizeList=new ArrayList<String>();
		
		if(creativeList !=null && creativeList.size()>0){				
			for(CreativeObj creative:creativeList){
				String name=creative.getSize();
				creativeSizeList.add(name);					
			}
			creativeSizeArr=creativeSizeList.toArray(new String[creativeSizeList.size()]);
		}
		
		if(productList !=null && productList.size()>0){
	    	log.info("Total search products:"+productList.size());	    	
	    	/*productList=productService.aggregateSearchProductsByPartner(productList);
	    	log.info("aggregated search products by partners :"+productList.size());*/
	    	
	    	productForecastMap=new HashMap<String, ForcastDTO>();
	    	IForecastInventoryService forecastService=new ForecastInventoryService();
	    	
		    for(ProductsObj product : productList){
		    	List<CityDTO> cities =  product.getCities();
		    	List<StateObj> states =  placement.getStateObj();
		    	List<GeoTargetsObj> dmas =  product.getDmas();
		    	List<ZipDTO> zips =  product.getZips();
		    	
		    	List<AdUnitDTO> adUnitDTOList=product.getAdUnits();
		    					
		    	String networkCode=product.getPublisherId();
		    	if(adUnitDTOList!=null && adUnitDTOList.size()>0){
		    		log.info("adUnitDTOList:"+adUnitDTOList.size()+" for product:"+product.getProductId()+
		    				" and networkCode:"+networkCode);
					try {
				   		log.info("Load forecasating data for product..."+product.getProductId());	
				   		LineItemType lineItemType = placement.getItemType() != null && placement.getItemType().equals(UnifiedCampaignDTO.SPONSORSHIP_ITEM_TYPE) ? LineItemType.SPONSORSHIP : LineItemType.STANDARD;
				   		
						forecastDTO=forecastService.loadForecastInventoryByAdUnit(networkCode, adUnitDTOList, startDate, endDate, lineItemType, 
								CostType.CPM, creativeSizeArr, placement.getDeviceCapability(), placement.getDeviceObj(), states, dmas, cities, zips);
						if(forecastDTO !=null && forecastDTO.getAvailableImpressions()>0){
							log.info("add forecasted product in map --"+forecastDTO.toString()+" with key --"+product.getId());
					  		productForecastMap.put(product.getId(), forecastDTO);						  	   
					  	}else{
					  		status="forecastDTO is null or no available impressions..." + forecastDTO;	
					  		log.info(status);
					  	}				
						
					} catch (ApiException_Exception e) {
						log.severe( "ApiException_Exception : "+e.getMessage());
						status="ApiException_Exception :"+e.getMessage();
					} catch (Exception e) {
						log.severe("Exception :"+e.getMessage());
						status="Exception :"+e.getMessage();
					} 
				}else{
					status="adUnitDTOList is null..";
				}
		    }		    
		    log.info("productForecastMap:"+productForecastMap.size());		   
		    
	    }else{
	    	status="productList is null..";
	    }
	    return productForecastMap;
	}
	
	private Map<String,Long> allocateImprssionToProducts(SmartCampaignPlacementObj placement, 
			Map<String,ForcastDTO> productForecastMap,
			List<ProductsObj> productList){
		Map<String,Long> productWiseAllocationMap=null;
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		String goalQty=placement.getImpressions();
		if(goalQty !=null && goalQty.contains(",")){
			goalQty=goalQty.replaceAll(",", "");
		}
		log.info("goalQty:"+goalQty);
		if(goalQty != null && productList !=null && productList.size()>0){
			if(goalQty.contains(",")){
				goalQty=goalQty.replaceAll(",", "");
			}
	    	long goal=Long.parseLong(goalQty);
			productWiseAllocationMap=productService.allocateImpressionByProduct(productList, productForecastMap, goal);
			log.info("Allocation done for placement "+placement.getPlacementId()+" with productWiseAllocationMap:"+productWiseAllocationMap.size());
		    
	    }else{
	    	status="Goal quantity is null for placement :"+placement.getPlacementId();
	    }
		return productWiseAllocationMap;
	}
	
	
	public String saveSmartMediaPlan(){	
		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		if(sessionDTO !=null){
			log.info("User : "+sessionDTO.getUserName());
		}
		if(!isAuthorised(sessionDTO)) {
			return "unAuthorisedAccess";
		}
		String jsonDataStr=request.getParameter("jsonData");
		log.info("Going to save media plan --"+jsonDataStr);
		if(jsonDataStr !=null){
			jsonData = (JSONObject) JSONSerializer.toJSON(jsonDataStr);
		}
		
		if(jsonData !=null){
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);			
			SmartMediaPlanObj smartMediaPlanJson= productService.saveSmartMediaPlanFromJSON(jsonData);
			if(smartMediaPlanJson !=null){
				status=""+smartMediaPlanJson.getId();
				
				ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				Boolean hasMediaPlan=true;
				Boolean isProcessing=false;
				Boolean isSetupOnDFP=null;
				boolean updateCampaign=service.updateSmartCampaignFlags(null, smartMediaPlanJson.getCampaignId(),
						hasMediaPlan, isProcessing, isSetupOnDFP, null);
				log.info("Campaign updated...updateCampaign:"+updateCampaign);
				
			}else{
				status="0";
			}
			
			log.info("Saved Media Plan with id---"+status);
		}
		return Action.SUCCESS;
	}
   
    public String loadSmartMediaPlanStatus(){    	
    	String campaignId=request.getParameter("campaignId");
    	log.info("load smart media plan for campaignId -"+campaignId);
    	if(campaignId !=null && LinMobileUtil.isNumeric(campaignId)){
    		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
    		int activeStatus=productService.checkMediaPlanStatus(campaignId);		
    		status=activeStatus+"";
    	}else{
    		status="error - invalid campaignId "+campaignId;
    	}
    	log.info(status);
    	return Action.SUCCESS;
    }
    
	public String clientIOExcelReport() {
    	log.info("inside clientIOExcelReport in MediaPlanAction");
    	ExcelReportGenerator erGen = new ExcelReportGenerator();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] excelbytes = null;
        contentType = "application/vnd.ms-excel";
        fileName = "Empty.xls";
        inputStream = new ByteArrayInputStream(baos.toByteArray());
    	try {
			String mediaPlanId = request.getParameter("mediaPlanId");
			if(mediaPlanId != null && LinMobileUtil.isNumeric(mediaPlanId)) {
				IMediaPlanService mediaPlanService = (IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
				Map excelData = mediaPlanService.clientIOExcelReport(Long.valueOf(mediaPlanId), getPublisherIdInBQ());
				List<ClientIOReportDTO> clientIOReportList = (List<ClientIOReportDTO>) excelData.get("dataBean");
				String campaignName = (String) excelData.get("campaignName");
				if(clientIOReportList != null && clientIOReportList.size() > 0) {
					fileName = "Client IO for "+campaignName+".xls";
			  		excelbytes = erGen.generateReport("ClientIOTemplate.xls","ClientIOReportDTO", clientIOReportList);
			  		inputStream = new ByteArrayInputStream(excelbytes);
				}
				else {
	        		if(campaignName != null && campaignName.length() != 0) {
	        			fileName = campaignName+".xls";
	        		}
	        	}
			}
    	} catch (Exception e) {
    		log.severe("Exception in clientIOExcelReport in MediaPlanAction");
    		
		}
    	log.info("fileName : "+fileName);
		return Action.SUCCESS;
    }
	
	public String POExcelReport() {
    	log.info("inside POExcelReport in MediaPlanAction");
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(baos);
		ExcelReportGenerator erGen = new ExcelReportGenerator();
        byte[] excelbytes = null;
        ZipEntry zipEntry = null;
        contentType = "application/vnd.ms-excel";
        fileName = "Empty.xls";
        inputStream = new ByteArrayInputStream(baos.toByteArray());
		IMediaPlanService mediaPlanService = (IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
    	try {
			String mediaPlanId = request.getParameter("mediaPlanId");
			if(mediaPlanId != null && LinMobileUtil.isNumeric(mediaPlanId)) {
				Map excelData = mediaPlanService.POExcelReport(Long.valueOf(mediaPlanId), getPublisherIdInBQ());
				/*List<Object> dataBean = (List<Object>) excelData.get("dataBean");*/
				List<ClientIOReportDTO> clientIOReportList = (List<ClientIOReportDTO>) excelData.get("dataBean");
				List<String> sheetName = (List<String>) excelData.get("sheetName");
				String campaignName = (String) excelData.get("campaignName");
				boolean partnerNameFound = (boolean) excelData.get("partnerNameFound");
	        	if(clientIOReportList != null && clientIOReportList.size() > 0 && sheetName != null && sheetName.size() == clientIOReportList.size()) {
	        		if(partnerNameFound) {
	        			fileName = "PO for "+campaignName+".zip";
	        		}else {
	        			fileName = campaignName+".zip";
	        		}
					contentType = "application/octet-stream";
		        	for(int i=0;i<clientIOReportList.size();i++) {
		        		ClientIOReportDTO clientIOReportDTO = clientIOReportList.get(i);
		        		if(clientIOReportDTO != null) {
		        			List<ClientIOReportDTO> list = new ArrayList<>();
		        			list.add(clientIOReportDTO);
		        			excelbytes = erGen.generateReport("POTemplate.xls","ClientIOReportDTO", list);
		        			// excelbytes = erGen.dynamicSheetReport("POTemplate.xls", dataBean, sheetName, "ClientIOReportDTO", 0);
				  			zipEntry = new ZipEntry(sheetName.get(i)+" "+campaignName+".xls");		// Create new zip entry																	 // the constructor parameter is filename of the entry
				  	        zout.putNextEntry(zipEntry);             // add the zip entry to the ZipOutputStream
				  	        zout.write(excelbytes);
				  	        zout.closeEntry();
		        		}
		        	}
		        	zout.flush();
			  	    zout.close();
			  	    inputStream = new ByteArrayInputStream(baos.toByteArray());
	        	}
	        	else {
	        		if(campaignName != null && campaignName.length() != 0) {
	        			fileName = campaignName+".xls";
	        		}
	        	}
			}
    	} catch (Exception e) {
    		log.severe("Exception in POExcelReport in MediaPlanAction");
    		
		}
		return Action.SUCCESS;
    }
	
  public String checkUpdateSmartMediaPlan(){
		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		if(sessionDTO !=null){
			log.info("User : "+sessionDTO.getUserName());
		}
		if(!isAuthorised(sessionDTO)) {
			return "unAuthorisedAccess";
		}
		String campaignStatus=request.getParameter("status");
		if(campaignId ==null){
			campaignId=request.getParameter("campaignId");
		}
		if(campaignStatus==null){
			campaignStatus=CampaignStatusEnum.Draft.name();
		}
		
		String active=request.getParameter("active");
		log.info("campaignId :"+campaignId+" and campaignStatus:"+campaignStatus+",active:"+active);
		jsonData=new JSONObject();
		
		
		
		if(campaignId !=null){
		
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(campaignId,2);
			
			ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			Boolean hasMediaPlan=false;
			Boolean isProcessing=true;
			Boolean isSetupOnDFP=null;
			service.updateSmartCampaignFlags(null, campaignId,
					hasMediaPlan, isProcessing, isSetupOnDFP, null);
			log.info("Campaign updated...with isProcessing:"+isProcessing);
			
			if(active !=null && active.equals("2")){
				log.info("Update existing media plan by refreshing forecasting and do reallocation......");
				if(smartMediaPlan !=null){
					
					log.info("Create new smart media plan using task queue...");			    
					String taskURL="/updateSmartMediaPlan.lin";
					TaskQueueUtil.addForUpdateMediaPlanInDefaultQueue(taskURL, sessionDTO.getUserId()+"", campaignId, 
							campaignStatus,smartMediaPlan.getId()+"");
					status="INPROGRESS";
					
				}else{
					log.warning("No existing media plan with active status 2 found for this campaign.");
					status="error - No smart media plan found for need update.";
				}
			}else{
				if(smartMediaPlan !=null){
					log.info("First set active status from 2 to 0 for need for update... and then regenrate");
					smartMediaPlan.setActive(0);
					productService.saveSmartMediaPlan(smartMediaPlan);
				}
				log.info("Regenerate media plan for this campaign.....");
				
			    log.info("Create new smart media plan using task queue...");			    
			    String taskURL="/createSmartMediaPlan.lin";
			    TaskQueueUtil.addTaskInDefaultQueue(taskURL, sessionDTO.getUserId()+"", campaignId, campaignStatus, "auto");
			    status="INPROGRESS";			    
			}			   
		}else{
			status="error - Invalid campaignId";
			jsonData.put("error", status);
		}		
		return Action.SUCCESS;
	}
	

	public String updateSmartMediaPlan(){
		String campaignId=request.getParameter("campaignId");
		String smartMediaPlanId=request.getParameter("smartMediaPlanId");
		String campaignStatus=request.getParameter("status");
    	String userId=request.getParameter("userId");
		log.info("Update media plan for campaignId:"+campaignId 
				+" , campaignStatus:"+campaignStatus+"\nuserId:"+userId+",smartMediaPlanId:"+smartMediaPlanId);
		
		boolean hasMediaPlan=false;
		
		if(smartMediaPlanId !=null ){	
			
			IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(StringUtil.getLongValue(smartMediaPlanId));
			if(campaignId==null){
				campaignId=smartMediaPlan.getCampaignId();
			}
			Map<String,List<ProductsObj>> placementWiseProducts=new HashMap<String, List<ProductsObj>>();
			if(smartMediaPlan !=null){				
				List<ProductsObj> productObjList=new ArrayList<ProductsObj>();
				List<ProductDTO> productsList=smartMediaPlan.getProducts();
				if(productsList !=null && productsList.size()>0){
					for(ProductDTO productDTO:productsList){
						long id=productDTO.getId();
						String placementId=productDTO.getPlacementId();
						ProductsObj product=productService.loadProduct(id);
						
						if(placementWiseProducts.containsKey(placementId)){
							productObjList=placementWiseProducts.get(placementId);
						}else{
							productObjList=new ArrayList<ProductsObj>();
						}
						if(product !=null){
							productObjList.add(product);
						}
						placementWiseProducts.put(placementId, productObjList);
					}
					log.info("placementWiseProducts:"+placementWiseProducts.size());					
					
				}else{
					log.info("No products already available to update..");
				}
				
			}else{
				log.info("No media plan found for id-"+smartMediaPlanId);
			}
			
			ISmartCampaignPlannerService campaignPlanService =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			if(placementWiseProducts !=null && placementWiseProducts.size()>0){
				log.info(">>>2 : Load campaign and then map placement with products..");
				UnifiedCampaignDTO campaignDTO=campaignPlanService.loadCampaign(campaignId);
				
				List<SmartCampaignPlacementObj> placementList=campaignDTO.getPlacementByCampaignList();
				
				Map<String,List<ProductsObj>> campaignProducts=new HashMap<String, List<ProductsObj>>();
				Map<String,Map<String,Long>> campaignProductsAllocationMap=new HashMap<String, Map<String,Long>>();
				
				Map<String,ForcastDTO> allProductsForecastMap=new HashMap<String, ForcastDTO>();
				if(placementList !=null && placementList.size()>0){				
					for(SmartCampaignPlacementObj placement:placementList){
						
						String placementIdStr=placement.getPlacementId().toString();
						
						if(placement.getStartDate()==null || placement.getStartDate().trim().length()==0){
							placement.setStartDate(campaignDTO.getStartDate());
						}
						if(placement.getEndDate()==null || placement.getEndDate().trim().length()==0){
							placement.setEndDate(campaignDTO.getEndDate());
						}
						List<ProductsObj> productListByPlacement=placementWiseProducts.get(placementIdStr);
						campaignProducts.put(placementIdStr, productListByPlacement);
						
						log.info(">>>3 : Go for forecasting products data..");
						Map<String,ForcastDTO> productForecastMap=matchInventoryByPlacement(productListByPlacement, placement);
						
						
						Iterator<ProductsObj> itr = productListByPlacement.iterator();
						while (itr.hasNext()) {
							ProductsObj product = itr.next(); 
							if(!productForecastMap.containsKey(product.getId())){
								log.info("Forecasting map does not conatin this product.. remove this...productId--"+product.getId());
								 itr.remove();
							}
						}	
						log.info(">>>4 : Go for allocating products data..");
						Map<String,Long> productWiseAllocationMap=allocateImprssionToProducts(placement, productForecastMap,
								productListByPlacement);
						if(productForecastMap !=null && productForecastMap.size()>0){
							allProductsForecastMap.putAll(productForecastMap);
						}
						
						if(productWiseAllocationMap !=null && productWiseAllocationMap.size()>0){							
							log.info("After allocation for placement :"+placementIdStr+", add in map..");
							campaignProductsAllocationMap.put(placementIdStr, productWiseAllocationMap);
						}
											
					}
					log.info(">>5 : Last step -- Create media plan  with allProductsForecastMap:"
					  + allProductsForecastMap.size()+" and campaignProductsAllocationMap:"+campaignProductsAllocationMap.size());
					smartMediaPlan=productService.createSmartMediaPlan(campaignProducts, allProductsForecastMap, 
							campaignProductsAllocationMap, campaignDTO, "auto");					
					if(smartMediaPlan !=null){
						hasMediaPlan=true;
						log.info("SmartMediaPlan object is created...id:"+smartMediaPlan.getId());
						jsonData=productService.createSmartMediaPlanJSON(smartMediaPlan);
						status="SmartMedia has been generated successfully - "+smartMediaPlan.getId();
					}else{
						status="error - No placements available for this campaign to generate SmartMediaPlan for campaign id: "+campaignId;
						jsonData.put("error", status);
					}				
				
			}else{
				status="error - No campaign available for this id:"+campaignId;
				
				jsonData.put("error", status);
			}
			}
			
			Boolean isProcessing=false;
			Boolean isSetupOnDFP=null;
			campaignPlanService.updateSmartCampaignFlags(null, campaignId,
					hasMediaPlan, isProcessing, isSetupOnDFP, null);
			log.info("Campaign updated...with isProcessing:"+isProcessing);
			
			pushMessageToUserViaChannel(smartMediaPlan, userId, campaignStatus,status);
		}		
		
		log.info(status);
		
		return Action.SUCCESS;
	}
	
	public String loadPartners(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		log.info("action starts.. load partners..sessionDTO :"+sessionDTO);
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<CompanyObj> companyList=userService.loadAllPartners();
		jsonData=userService.loadPartnersJSON(companyList);		
		log.info("Action ends...");
		return Action.SUCCESS;
	}
	
	public void testForecastService() throws ApiException_Exception, IOException{
		
		   AdUnitDTO au = new AdUnitDTO();
//		   44662564 and name:weather
		   //au.setId(44662564); au.setName("weather");
		
		   au.setId(32577222); au.setName("lin.kasa");
		   List<AdUnitDTO> list = new ArrayList<AdUnitDTO>(); list.add(au);
		   List<DeviceObj> devices = new ArrayList<DeviceObj>();
		   devices.addAll(Arrays.asList(new DeviceObj[]{new DeviceObj(1, ""),new DeviceObj(2, "")}));
//		 for(int i=0;i<10;i++){
		   ForcastDTO dto =  new ForecastInventoryService().loadForecastInventoryByAdUnit("5678"/*"45604844"*/, list, "2014-12-10", "2015-05-31", 
				   LineItemType.STANDARD, CostType.CPM, new String[]{"300x50","300x250"}, ReportUtil.TARGETING_MOBILE_APP_AND_WEB, 
				   devices, null, null, null ,null);
		   
		   response.getWriter().print("Matched "+ dto.getMatchedImpressions()+" Available: "+ dto.getAvailableImpressions());
	}
	HttpServletResponse response = null;
	
	
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}

		
	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}



	public ProposalDTO getProposalDTO() {
		return proposalDTO;
	}



	public void setProposalDTO(ProposalDTO proposalDTO) {
		this.proposalDTO = proposalDTO;
	}



	public Map<String, String> getAdvertiserMap() {
		return advertiserMap;
	}



	public void setAdvertiserMap(Map<String, String> advertiserMap) {
		this.advertiserMap = advertiserMap;
	}



	public Map<String, String> getAgencyMap() {
		return agencyMap;
	}



	public void setAgencyMap(Map<String, String> agencyMap) {
		this.agencyMap = agencyMap;
	}

	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}

	public String getPlacementId() {
		return placementId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setPlacementMap(JSONObject placementMap) {
		this.placementMap = placementMap;
	}

	public JSONObject getPlacementMap() {
		return placementMap;
	}

	public void setAdSizeMap(Map<String,String> adSizeMap) {
		this.adSizeMap = adSizeMap;
	}

	public Map<String,String> getAdSizeMap() {
		return adSizeMap;
	}

	public void setAdFormatMap(Map<String,String> adFormatMap) {
		this.adFormatMap = adFormatMap;
	}

	public Map<String,String> getAdFormatMap() {
		return adFormatMap;
	}

	public void setForecastingMap(Map<String,String> forecastingMap) {
		this.forecastingMap = forecastingMap;
	}

	public Map<String,String> getForecastingMap() {
		return forecastingMap;
	}

	public void setGeoTargetMap(Map<String,String> geoTargetMap) {
		this.geoTargetMap = geoTargetMap;
	}

	public Map<String,String> getGeoTargetMap() {
		return geoTargetMap;
	}

	public List<PlacementObj> getPlacementList() {
		return placementList;
	}

	public void setPlacementList(List<PlacementObj> placementList) {
		this.placementList = placementList;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public JSONObject getJsonData() {
		return jsonData;
	}

	public void setJsonData(JSONObject jsonData) {
		this.jsonData = jsonData;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	

	
}
