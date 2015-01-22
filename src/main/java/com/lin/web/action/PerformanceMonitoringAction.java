package com.lin.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DataLoaderUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class PerformanceMonitoringAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	private static final Logger log = Logger.getLogger(PerformanceMonitoringAction.class.getName());
	
	public static final String PEFORMANCE_MONITORING_KEY = "performanceMonitoringKey";
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private SessionObjectDTO sessionDTO;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private Map<String,String> dataMap;
	private String placementLineItems;
	private String orderInfo;
	private String partnerInfo;
	private String lineItemPlacementIds;
	private String lineItemPlacementName;
	private String publisherBQId;
	private String barChartDataString;
	
	public double defaultThreshold= LinMobileConstants.THRESHOLD;
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	public String redirectURL = LinMobileVariables.REDIRECT_URL;
	public String backend = LinMobileVariables.BACKEND_NAME;
	
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
	
	public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[1]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}
	
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
				else {
					List<CompanyObj> companyObjList = userDetailsDAO.getSelectedCompaniesByUserId(sessionDTO.getUserId());
					if(companyObjList != null && companyObjList.size()==1) {
						log.info("companyObj.getCompanyName() : "+companyObjList.get(0).getCompanyName());
						publisherIdInBQ = userService.getPublisherBQId(companyObjList.get(0));
					}
				}
			}
		} catch(Exception e) {
			log.info("Exception in getPublisherId in PerformanceMonitoringAction : "+e.getMessage());
			
		}
		log.info("publisherIdInBQ : "+publisherIdInBQ);
		return publisherIdInBQ;
	}
	
	public String execute(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try {
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of PerformanceMonitoringAction : " + e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	public String performanceAndMonitoringView() {
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try {
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			String campaignId = request.getParameter("orderId");
			//publisherBQId = "5";
			publisherBQId=getPublisherIdInBQ();
			
			if(campaignId != null && LinMobileUtil.isNumeric(campaignId) && (Long.valueOf(campaignId)) > 0) {
				IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
				Map<String, String> placementInfo = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
				if(placementInfo != null && placementInfo.size() > 0) {
					if(placementInfo.get("isAuthorised") != null && placementInfo.get("isAuthorised").equals("0")) {
						return "unAuthorisedAccess";
					}
					MemcacheUtil.setObjectInCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId(), placementInfo, 60*30);
					placementLineItems = placementInfo.get("placementInfoArray");
					orderInfo = placementInfo.get("orderInfo");
					partnerInfo = placementInfo.get("partnerInfo");
					lineItemPlacementIds = placementInfo.get("lineItemPlacementIds");
					lineItemPlacementName = placementInfo.get("lineItemPlacementName");
				}
			}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of AdvertiserViewAction : " + e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String headerData() {
		log.info("In headerData of PerformanceMonitoringAction");	   
	    try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				String placementInfo = "";
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonObject = monitoringService.headerData(orderId, campaignId, placementIds, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				 }else{
					 log.warning("Invalid parameters"); 
				 }			
	    	}else {
				log.warning("User not in session");
			}
	     }catch(Exception e){
	    	 log.severe("Exception in headerData in PerformanceMonitoringAction : "+e.getMessage());
				
	     }
	   
	     return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String deliveryMetricsData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				String placementInfo = "";
				String lineItemPlacementName = "";
				String  lineItemPlacementIds = "";
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						orderInfo = placementInfoMap.get("orderInfo");
						lineItemPlacementName = placementInfoMap.get("lineItemPlacementName");
						lineItemPlacementIds = placementInfoMap.get("lineItemPlacementIds");
					}
					if(placementInfo != null && orderInfo != null) {
						jsonObject = monitoringService.deliveryMetricsData(orderId, campaignId, placementIds, publisherIdInBQ, orderInfo, placementInfo, lineItemPlacementName, lineItemPlacementIds);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in deliveryMetricsData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String ctrLineChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						jsonObject = monitoringService.ctrLineChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in ctrLineChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String impressionsLineChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						jsonObject = monitoringService.impressionsLineChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in impressionsLineChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String clicksLineChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						jsonObject = monitoringService.clicksLineChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in clicksLineChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String locationCompleteData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		    if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonObject = monitoringService.locationCompleteData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in locationCompleteData() in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String locationTopCitiesData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonObject = monitoringService.locationTopCitiesData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in locationTopCitiesData() in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String locationTargetData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonObject = monitoringService.locationTargetData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in locationTargetData() in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String flightLineChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
						lineItemPlacementIds = placementInfoMap.get("lineItemPlacementIds");
						lineItemPlacementName = placementInfoMap.get("lineItemPlacementName");
					}
					if(placementInfo != null && partnerInfo != null && lineItemPlacementIds != null && lineItemPlacementName != null) {
						jsonObject = monitoringService.flightLineChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo, lineItemPlacementIds, lineItemPlacementName);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid data.....");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in flightLineChartData() in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	public String loadAllRunningCampaigns(){
		log.info("loadAllCampaigns starts.. "+new Date());
		IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			int campaignPerPage = 25;
			int pageNumber = 1;
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			String campaignStatus = request.getParameter("campaignStatus");
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			String searchKeyword=request.getParameter("searchKeyword");
			log.info("limit : "+limit+", offset : "+offset+", searchKeyword : "+searchKeyword);
			if(limit != null && (LinMobileUtil.isNumeric(limit))) {
				campaignPerPage = Integer.parseInt(limit);
				if(campaignPerPage <= 0) {
					campaignPerPage = 20;
				}
			}
			if(offset != null && (LinMobileUtil.isNumeric(offset.trim()))) {
				pageNumber = Integer.parseInt(offset);
				if(pageNumber <= 0) {
					pageNumber = 1;
				}
			}
			if(searchKeyword != null) {
				searchKeyword = searchKeyword.toLowerCase().trim();
			}
			
			String publisherIdInBQ = getPublisherIdInBQ();
			log.info("publisherIdInBQ : "+publisherIdInBQ);
			//publisherIdInBQ = "5";
			 if(!publisherIdInBQ.isEmpty()) {
				// dataMap=monitoringService.performanceClicksLineChartData(orderId, isNoise, threshold, publisherIdInBQ);
				 jsonArray = monitoringService.loadAllCampaigns(campaignStatus, sessionDTO.getUserId(),sessionDTO.isSuperAdmin(), campaignPerPage, pageNumber, searchKeyword, publisherIdInBQ);
			 }
		
		}catch(Exception e){
			log.severe("Exception in loadAllCampaigns()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	public String updateCampaignData(){
		log.info("called from inside updateCampaignData");
		IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		monitoringService.updateCampaignData();
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String creativeBarChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						dataMap = monitoringService.creativeBarChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in creativeBarChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String deviceBarChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						dataMap = monitoringService.deviceBarChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in deviceBarChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
		
	@SuppressWarnings("unchecked")
	public String osChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				boolean isNoise = false;
				double threshold = 0.0;
				String placementInfo = "";
				String isNoiseValue=request.getParameter("isNoise");
				String thresholdValue=request.getParameter("threshold");
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds+", isNoiseValue : "+isNoiseValue+", thresholdValue : "+thresholdValue);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true") && thresholdValue != null && LinMobileUtil.isNumeric(thresholdValue)) {
						isNoise = true;
						threshold = Double.valueOf(thresholdValue);
					}
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						dataMap = monitoringService.osChartData(orderId, campaignId, placementIds, isNoise, threshold, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in creativeBarChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String richMediaLineChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				String placementInfo = "";
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
						partnerInfo = placementInfoMap.get("partnerInfo");
					}
					if(placementInfo != null && partnerInfo != null) {
						jsonObject = monitoringService.richMediaLineChartData(orderId, campaignId, placementIds, publisherIdInBQ, placementInfo, partnerInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in richMediaLineChartData()  in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String richMediaDonutChartData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				String placementInfo = "";
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonArray = monitoringService.richMediaDonutChartData(orderId, campaignId, placementIds, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");
					jsonObject.put("error", "Invalid request parameters");
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception : "+e.getMessage());
			 jsonObject.put("error", "Exception - "+e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String videoData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	if(sessionDTO != null && sessionDTO.getUserId() > 0) {
				String placementInfo = "";
				String orderId=request.getParameter("orderId");
				String publisherIdInBQ = request.getParameter("publisherIdInBQ");
				String campaignId = request.getParameter("campaignId");
				String placementIds = request.getParameter("placementId");
				log.info("orderId : "+orderId+", publisherIdInBQ : "+publisherIdInBQ+", campaignId : "+campaignId+", placementIds : "+placementIds);
				if(orderId != null && (LinMobileUtil.isNumeric(orderId)) && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ)) && 
						campaignId != null && LinMobileUtil.isNumeric(campaignId) && placementIds != null) {
					IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
					Map<String, String> placementInfoMap = null;
					placementInfoMap = (Map<String, String>) MemcacheUtil.getObjectFromCache(PEFORMANCE_MONITORING_KEY+campaignId+"_"+sessionDTO.getUserId());
					if(placementInfoMap == null || placementInfoMap.size() == 0) {
						placementInfoMap = monitoringService.getPlacementInformation(Long.valueOf(campaignId), sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
					}
					if(placementInfoMap != null && placementInfoMap.size() > 0) {
						placementInfo = placementInfoMap.get("placementInfo");
					}
					if(placementInfo != null) {
						jsonObject = monitoringService.videoData(orderId, campaignId, placementIds, publisherIdInBQ, placementInfo);
					}
					else {
						log.info("No sufficient info for campaignId : "+campaignId+" --> "+placementInfoMap);
					}
				}else{
					log.warning("Invalid parameters");			
				}
	    	}else {
				log.warning("User not in session");
			}
		} catch(Exception e) {
			 log.severe("Exception in videoData in PerformanceMonitoringAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	public String addLineItemInfo() {
		String status = "";
		try {
			Exception e = new Exception();
			String campaignId=request.getParameter("campaignId");		// Data Store Id
			String orderId=request.getParameter("orderId");				// DFP ID
			String lineItemId = request.getParameter("lineItemId");
			String lineItemName = request.getParameter("lineItemName");
			String placementId = request.getParameter("placementId");
			String partnerName = request.getParameter("partnerName");
			if(campaignId == null || !(LinMobileUtil.isNumeric(campaignId.trim()))) {
				status = "Campaign Id should be numeric";
				throw e;
			}
			if(orderId == null || !(LinMobileUtil.isNumeric(orderId.trim()))) {
				status = "Order DFP Id should be numeric";
				throw e;
			}
			if(placementId == null || !(LinMobileUtil.isNumeric(placementId.trim()))) {
				status = "Placement Id should be numeric";
				throw e;
			}
			if(lineItemId == null || lineItemId.trim().length() == 0) {
				status = "lineItem Id should not be empty";
				throw e;
			}
			if(lineItemName == null || lineItemName.trim().length() == 0) {
				status = "lineItem Name should not be empty";
				throw e;
			}
			if(partnerName == null || partnerName.trim().length() == 0) {
				status = "Partner Name should not be empty";
				throw e;
			}
			String[] lineArr = lineItemId.split(",");
			String[] partnerArr = partnerName.split(",");
			String[] lineItemNameArr = lineItemName.split(",");
			
			if(lineArr == null || partnerArr == null || lineArr.length != partnerArr.length) {
				status = "Count of lineItem Id and Partner Name do not match";
				throw e;
			}
			if(lineItemNameArr != null && lineItemNameArr.length != lineArr.length) {
				status = "Count of lineItem Name and lineItem Id do not match";
				throw e;
			}
			
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			SmartCampaignObj smartCampaignObj = smartCampaignPlannerDAO.getCampaignById(StringUtil.getLongValue(campaignId.trim()));
			if(smartCampaignObj != null ) {
				smartCampaignObj.setDfpOrderId(StringUtil.getLongValue(orderId.trim()));
				smartCampaignPlannerDAO.saveObjectWithStrongConsistancy(smartCampaignObj);
			}
			else  {
				status = "No campaign exists for Campaign Id : "+campaignId;
				throw e;
			}
			
			SmartCampaignPlacementObj placementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId.trim()));
			if(placementObj != null) {
				List<DFPLineItemDTO> dfpLineItemList = new ArrayList<>();
				for(int i=0; i<lineArr.length; i++) {
					lineItemId = lineArr[i].trim();
					String partner = partnerArr[i].trim();
					lineItemName = lineItemNameArr[i].trim();
					if(LinMobileUtil.isNumeric(lineItemId)) {
						DFPLineItemDTO dfpLineItemDTO = new DFPLineItemDTO(StringUtil.getLongValue(lineItemId), lineItemName, partner, "");
						dfpLineItemList.add(dfpLineItemDTO);
					}
					else {
						status = status + "Non numeric LineItem Id : "+lineItemId+ "\n";
					}
				}
				placementObj.setDfpLineItemList(dfpLineItemList);
				smartCampaignPlannerDAO.saveObjectWithStrongConsistancy(placementObj);
			}
			else  {
				status = "No Placement exists for Placement Id : "+placementId;
				throw e;
			}
			if(status.length() == 0) {
				status = "Campaign information added sucessfully";
			}
		} catch(Exception e) {
			 log.severe("Exception in addLineItemInfo()  in PerformanceMonitoringAction : "+e.getMessage());
			 log.warning(status);
			 
		}
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}
	
	
	
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.setResponse(response);
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.setRequest(request);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}
	

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Map<String,String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String,String> dataMap) {
		this.dataMap = dataMap;
	}
	public String getPlacementLineItems() {
		return placementLineItems;
	}

	public void setPlacementLineItems(String placementLineItems) {
		this.placementLineItems = placementLineItems;
	}

	public String getPublisherBQId() {
		return publisherBQId;
	}

	public void setPublisherBQId(String publisherBQId) {
		this.publisherBQId = publisherBQId;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getPartnerInfo() {
		return partnerInfo;
	}

	public void setPartnerInfo(String partnerInfo) {
		this.partnerInfo = partnerInfo;
	}

	public String getBarChartDataString() {
		return barChartDataString;
	}

	public void setBarChartDataString(String barChartDataString) {
		this.barChartDataString = barChartDataString;
	}

	public String getLineItemPlacementIds() {
		return lineItemPlacementIds;
	}

	public void setLineItemPlacementIds(String lineItemPlacementIds) {
		this.lineItemPlacementIds = lineItemPlacementIds;
	}

	public String getLineItemPlacementName() {
		return lineItemPlacementName;
	}

	public void setLineItemPlacementName(String lineItemPlacementName) {
		this.lineItemPlacementName = lineItemPlacementName;
	}
	
	
	public void runUrlbyTQ(){
		log.info("Inside Task Queue Caller");
		System.out.println(request.getParameter("url"));
		TaskQueueUtil.addTaskInDefaultQueue(request.getParameter("url"));
	}
	
	//Added By Anup : To get LineItemId by Partner
	public String getPartner(){
		String campaignID = request.getParameter("campaignid");
		String partnerName = request.getParameter("partner");
		String orderID	=	request.getParameter("orderId");
		String publisherIdInBQ = request.getParameter("publisherIdInBQ");
		
		IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		jsonArray = monitoringService.getCampaignDetailByPartner(orderID, campaignID, publisherIdInBQ, partnerName);
		
		return Action.SUCCESS;
	}
	
	public String getRUNDspCampaignList(){
		IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		jsonArray = monitoringService.getRUNDspCampaignList();
		return Action.SUCCESS;
	}
	
	public String getRUNDspCampaignDetail(){
		IPerformanceMonitoringService monitoringService = (IPerformanceMonitoringService)BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		String end =  request.getParameter("end");
		String campaingID =  request.getParameter("campaignid");
		String groupBy =  request.getParameter("groupby");
		String start = request.getParameter("start");
		jsonArray = monitoringService.getRUNDspCampaignDetail(campaingID, groupBy, start, end);
		return Action.SUCCESS;
	}
}