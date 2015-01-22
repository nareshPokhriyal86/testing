package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.gdata.data.introspection.IServiceDocument;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.IForecastInventoryService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.InstrumentDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.MostActiveReportDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.NonPerformerReportDTO;
import com.lin.web.dto.PerformanceByAdSizeReportDTO;
import com.lin.web.dto.PerformanceByDeviceReportDTO;
import com.lin.web.dto.PerformanceByLocationReportDTO;
import com.lin.web.dto.PerformanceByOSReportDTO;
import com.lin.web.dto.PerformanceByPlacementReportDTO;
import com.lin.web.dto.PerformanceBySiteReportDTO;
import com.lin.web.dto.PerformerReportDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.SummaryReportDTO;
import com.lin.web.service.IAdvertiserService;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.INewAdvertiserViewService;
import com.lin.web.service.IRichMediaAdvertiserService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.DateUtil;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;

public class NewAdvertiserViewAction implements  ServletRequestAware,ServletResponseAware, SessionAware{

	private static final Logger log = Logger.getLogger(AdvertiserViewAction.class.getName());
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private SessionObjectDTO sessionDTO;
	private String errorStatus;
	private List<NewAdvertiserViewDTO> headerDataList;
	private java.lang.CharSequence jsonStr;
	private java.lang.CharSequence performerJsonStr;
	private java.lang.CharSequence performanceByLocationChartDatajsonStr;
	private java.lang.CharSequence performanceByAdSizeChartDataJsonStr;
	private java.lang.CharSequence performanceByOSeChartDataJsonStr;
	private java.lang.CharSequence performanceByDeviceChartDataJsonStr;
	private java.lang.CharSequence mobileWebVsAppChartDataJsonStr;
	private java.lang.CharSequence impressionByOSeChartDataJsonStr;
	private java.lang.CharSequence impressionByDeviceChartDataJsonStr;
	private java.lang.CharSequence impressionByAdSizeChartDataJsonStr;
	private java.lang.CharSequence impressionBymobileWebVsAppChartDataJsonStr;
	private String performerDataStr;
	private String mostActiveDataStr;
	private String deliveryMetricsStr;
	private List<CommonDTO> customEvents;
	private String richMediaGraphTable;
	private String clientLoginToken = null;
	private DfpSession dfpSession;
	private String fileName;
    private DfpServices dfpServices;
    private NewAdvertiserViewDTO newAdvertiserViewDTO;
	private Map<String,String> dataMap;
	private Map<String,Object> videoCampaignDataMap;
	private InputStream inputStream;
	private JSONArray campaignGridArray;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	public String getPublisherIdInBQ() {
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		ILinMobileBusinessService linMobileBusinessService = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		String publisherIdInBQ = "";
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(sessionDTO.isSuperAdmin()) {
					publisherIdInBQ = "1";
				}
				/*else if(sessionDTO.isAdminUser()) {
					CompanyObj companyObj = userService.getCompanyIdByAdminUserId(sessionDTO.getUserId());
					if(companyObj != null && companyObj.getCompanyName() != null) {
						log.info("companyObj.getCompanyName() : "+companyObj.getCompanyName());
						//publisherIdInBQ = linMobileBusinessService.getPublisherBQId(companyObj.getCompanyName().trim());
						publisherIdInBQ = userService.getPublisherBQId(companyObj);
					}
				}
				*/else {
					List<CompanyObj> companyObjList = userDetailsDAO.getSelectedCompaniesByUserId(sessionDTO.getUserId());
					if(companyObjList != null && companyObjList.size()==1) {
						log.info("companyObj.getCompanyName() : "+companyObjList.get(0).getCompanyName());
						publisherIdInBQ = userService.getPublisherBQId(companyObjList.get(0));
					}else{
						log.severe("failed to load company for this user.."+sessionDTO.getUserId());
					}
				}
			}
		} catch(Exception e) {
			log.info("Exception in getPublisherId in NewAdvertiserViewAction : "+e.getMessage());
			
		}
		log.info("publisherIdInBQ : "+publisherIdInBQ);
		return publisherIdInBQ;
	}
	
	public String headerData(){
		log.info("In HeaderData() of NewAdvertiserViewAction");	   
	    try{
	    	 String orderId=request.getParameter("orderId");	
	    	 String campaignName = request.getParameter("campaignName");
	    	 String accountName = request.getParameter("accountName");
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
				 	headerDataList = service.headerDate(orderId,campaignName,accountName, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }			
	    	 
	     }catch(Exception e){
	    	 log.severe("Exception in HeaderData()  in NewAdvertiserViewAction : "+e.getMessage());
				
	     }
	   
	     return Action.SUCCESS;
	}
	
	public String performarData(){
		log.info("In performarData() of NewAdvertiserViewAction");	   
	    try{
	    	 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 performerDataStr = service.performarData(orderId, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }			 
	     }catch(Exception e){
	    	 log.severe("Exception in performarData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
		return Action.SUCCESS;
	}

	public String mostActiveData(){
		log.info("In mostActiveData() of NewAdvertiserViewAction");	    
	    try{
	    	 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 mostActiveDataStr = service.mostActiveData(orderId, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }			 
	     }catch(Exception e){
	    	 log.severe("Exception in mostActiveData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
		return Action.SUCCESS;
	}
	
	public String deliveryMetrics(){
		log.info("In deliveryMetrics() of NewAdvertiserViewAction");	    	   
	    try{
	    	 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 deliveryMetricsStr = service.deliveryMetricsData(orderId, publisherIdInBQ);
				 }
		    	
			 }else{
				 log.warning("Invalid data....."); 
			 }
	     }catch(Exception e){
	    	 log.severe("Exception in deliveryMetrics()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
		return Action.SUCCESS;
	}
	
	public String performanceByLocationChartData() {
		 log.info("In performanceByLocationChartData() of NewAdvertiserViewAction");
		 try{
			 boolean isNoise = false;
			 double threshold = 0.0;
			 String orderId=request.getParameter("orderId");
			 String isNoiseValue=request.getParameter("isNoise");
			 String thresholdValue=request.getParameter("threshold");
			 log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
			 if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
				 isNoise = true;
				 threshold = Double.valueOf(thresholdValue);
			 }	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 DataTable dataTable = service.performanceByLocationChartData(orderId, isNoise, threshold, publisherIdInBQ);
					 performanceByLocationChartDatajsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }			 
		 }catch(Exception e){
			 log.severe("Exception in performanceByLocationChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String performanceByAdSizeChartData(){
		 log.info("In performanceByAdSizeChartData() of NewAdvertiserViewAction");
		 try{
			 boolean isNoise = false;
			 double threshold = 0.0;
			 String orderId=request.getParameter("orderId");
			 String isNoiseValue=request.getParameter("isNoise");
			 String thresholdValue=request.getParameter("threshold");
			 log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
			 if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
				 isNoise = true;
				 threshold = Double.valueOf(thresholdValue);
			 }	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 performanceByAdSizeChartDataJsonStr = service.performanceByAdSizeChartData(orderId, isNoise, threshold, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }
			 
			 
		 }catch(Exception e){
			 log.severe("Exception in performanceByAdSizeChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String impressionByOSChartData(){
		 log.info("In performanceByOSChartData() of NewAdvertiserViewAction");
		 try{
			 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
			   INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
			   String publisherIdInBQ = getPublisherIdInBQ();
			   if(!publisherIdInBQ.isEmpty()) {
				   DataTable dataTable = service.impressionByOSChartData(orderId, publisherIdInBQ);
				   impressionByOSeChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
			   }
			 }else{
				 log.warning("Invalid data....."); 
			 }
		 }catch(Exception e){
			 log.severe("Exception in performanceByOSChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String impressionByDeviceChartData(){
		 log.info("In performanceByDeviceChartData() of NewAdvertiserViewAction");
		 try{
			 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
			   INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
			   String publisherIdInBQ = getPublisherIdInBQ();
			   if(!publisherIdInBQ.isEmpty()) {
				   DataTable dataTable = service.impressionByDeviceChartData(orderId, publisherIdInBQ);
				   impressionByDeviceChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
			   }
			 }else{
				log.warning("Invalid data.....");			
			 } 
		 }catch(Exception e){
			 log.severe("Exception in performanceByDeviceChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String impressionBymobileWebVsAppChartData(){
		 log.info("In mobileWebVsAppChartData() of NewAdvertiserViewAction");
		 try{
			 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 DataTable dataTable = service.impressionBymobileWebVsAppChartData(orderId, publisherIdInBQ);
					 impressionBymobileWebVsAppChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
				 }
			}else{
				log.warning("Invalid data.....");
			} 
		 }catch(Exception e){
			 log.severe("Exception in mobileWebVsAppChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String loadUserAccounts(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		long userId=sessionDTO.getUserId();
		log.info("UserId:"+userId);
		IUserService service = (IUserService)BusinessServiceLocator.locate(IUserService.class);
		try {
			dataMap=service.getSelectedAccountsByUserId(userId, true, true);
		} catch(Exception e) {
			log.severe("Exception in loadUserAccounts : "+e.getMessage());
			
		}
		log.info("dataMap size:"+dataMap.size());		
		return Action.SUCCESS;
	}
	
	public String loadOrders(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		long userId=sessionDTO==null?0:sessionDTO.getUserId();
		log.info("UserId:"+userId);
		String id=request.getParameter("id");
		String advertiserOrAgency=request.getParameter("advertiserOrAgency");
		log.info("id :"+id+" | advertiserOrAgency:"+advertiserOrAgency);	
		if(id == null ){
			log.warning("Invalid data..");
		}else{
			String advertiserId="";
			String agencyId="";
			if(advertiserOrAgency !=null && advertiserOrAgency.trim().endsWith("("+LinMobileConstants.AGENCY_ID_PREFIX+")")){
				agencyId=id;
			}else if(advertiserOrAgency !=null && advertiserOrAgency.trim().endsWith("("+LinMobileConstants.ADVERTISER_ID_PREFIX+")")){
				advertiserId=id;
			}
			INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator
					.locate(INewAdvertiserViewService.class);
			 String publisherIdInBQ = getPublisherIdInBQ();
			 if(!publisherIdInBQ.isEmpty()) {
				 dataMap=service.loadOrdersForAdvertiserOrAgency(advertiserId, agencyId, publisherIdInBQ,userId);
			 }
			 if(dataMap !=null){
				 log.info("dataMap size:"+dataMap.size());	
			 }
		}			
		return Action.SUCCESS;
	}
	
	public String loadLineCharts(){
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		boolean isNoise = false;
		double threshold = 0.0;
		String orderId=request.getParameter("orderId");
		String isNoiseValue=request.getParameter("isNoise");
		String thresholdValue=request.getParameter("threshold");
		log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
		if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
			isNoise = true;
			threshold = Double.valueOf(thresholdValue);
		}
		if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
			INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
			String publisherIdInBQ = getPublisherIdInBQ();
			 if(!publisherIdInBQ.isEmpty()) {
				 dataMap=service.processLineChartData(orderId, isNoise, threshold, publisherIdInBQ);
			 }
		}else{
			log.warning("Invalid data.....");			
		}			
		return Action.SUCCESS;
	}
	
	public String richMediaEventGraph(){
    	try{
    		String campaignId=request.getParameter("campaignId");
			customEvents = new ArrayList<CommonDTO>();
			//campaignId = "141903979";
			INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
			String publisherIdInBQ = getPublisherIdInBQ();
			 if(!publisherIdInBQ.isEmpty()) {
				 richMediaGraphTable = service.richMediaEventGraph(customEvents, campaignId, publisherIdInBQ);
			 }
    	}catch(Exception e){
    		log.severe("Exception in richMediaEventGraph in NewAdvertiserViewAction"+e.getMessage());
			
			return Action.ERROR;
    	}
    	return Action.SUCCESS;
    }
	
	
	public String loadLineItemForcastPopupData(){    	
    	try{
    		String lineItemId = request.getParameter("lineItemId");
    		String bookedImp = request.getParameter("bookedImp");
    		String deliveredImp = request.getParameter("deliveredImp");
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
    		log.info("Load lineItemForcast data for LineItemId:"+lineItemId);
    		
		    dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.DFP_NETWORK_CODE, LinMobileConstants.DFP_APPLICATION_NAME);
		      				
		    dfpServices = LinMobileProperties.getInstance().getDfpServices();
			IForecastInventoryService forecastDFPService = (IForecastInventoryService) BusinessServiceLocator.locate(IForecastInventoryService.class);
			newAdvertiserViewDTO = forecastDFPService.getLineItemForcast(dfpServices, dfpSession, lineItemId,bookedImp,deliveredImp);
    		
    	}catch (ValidationException e) {
			log.severe("DFP session exception: ValidationException :"+e.getMessage());				
			
			setErrorStatus("validationError");
			 
	   }catch(Exception e){
			log.severe("Exception in updateReallocationLineItem in AdvertiserViewAction"+e.getMessage());
			
			setErrorStatus("DFPAPIError");
		  
    	}
    	return Action.SUCCESS;
    }
	
	
	 
	 public String performanceByOSBarChartData(){
		 log.info("In performanceByOSChartData() of NewAdvertiserViewAction");
		 try{
			 boolean isNoise = false;
			 double threshold = 0.0;
			 String orderId=request.getParameter("orderId");
			 String isNoiseValue=request.getParameter("isNoise");
			 String thresholdValue=request.getParameter("threshold");
			 log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
			 if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
				 isNoise = true;
				 threshold = Double.valueOf(thresholdValue);
			 }	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 performanceByOSeChartDataJsonStr = service.performanceByOSBarChartData(orderId, isNoise, threshold, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }
			 
			 
		 }catch(Exception e){
			 log.severe("Exception in performanceByAdSizeChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String performanceByDeviceBarChartData(){
		 log.info("In performanceByDeviceChartData() of NewAdvertiserViewAction");
		 try{
			 boolean isNoise = false;
			 double threshold = 0.0;
			 String orderId=request.getParameter("orderId");
			 String isNoiseValue=request.getParameter("isNoise");
			 String thresholdValue=request.getParameter("threshold");
			 log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
			 if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
				 isNoise = true;
				 threshold = Double.valueOf(thresholdValue);
			 }	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 performanceByDeviceChartDataJsonStr = service.performanceByDeviceBarChartData(orderId, isNoise, threshold, publisherIdInBQ);
				 }
				 //performanceByAdSizeChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
			 }else{
				 log.warning("Invalid data....."); 
			 }
			 
			 
		 }catch(Exception e){
			 log.severe("Exception in performanceByAdSizeChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String mobileWebVsAppBarChartData(){
		 log.info("In mobileWebVsAppChartData() of NewAdvertiserViewAction");
		 try{
			 boolean isNoise = false;
			 double threshold = 0.0;
			 String orderId=request.getParameter("orderId");
			 String isNoiseValue=request.getParameter("isNoise");
			 String thresholdValue=request.getParameter("threshold");
			 log.info("orderId : "+orderId+", isNoiseValue : "+isNoiseValue+", threshold : "+threshold);	
			 if(isNoiseValue != null && isNoiseValue.equalsIgnoreCase("true")) {
				 isNoise = true;
				 threshold = Double.valueOf(thresholdValue);
			 }
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 mobileWebVsAppChartDataJsonStr = service.mobileWebVsAppBarChartData(orderId, isNoise, threshold, publisherIdInBQ);
				 }
				 //performanceByAdSizeChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
			 }else{
				 log.warning("Invalid data....."); 
			 }
		 }catch(Exception e){
			 log.severe("Exception in performanceByAdSizeChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String deliveryMetricsReport(){
		log.info("In deliveryMetricsReport() of NewAdvertiserViewAction");
		
		 List<NewAdvertiserViewDTO> dataBean = null;
	    try{
	    	 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 dataBean = service.deliveryMetricsReportObject(orderId, publisherIdInBQ);
				 }
			 }else{
				 log.warning("Invalid data....."); 
			 }
	     }catch(Exception e){
	    	 log.severe("Exception in deliveryMetricsReport()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
        ExcelReportGenerator erGen = new ExcelReportGenerator();
        
        byte[] excelbytes = null;
        try {
			excelbytes = erGen.generateReport("DeliveryMetrics.xls","NewAdvertiserViewDTO", dataBean);
			 inputStream = new ByteArrayInputStream(excelbytes);
			 
			 /*  
	    	   response.setContentType("application/vnd.ms-excel");
	    	   response.getOutputStream().write(excelbytes);
	    	   response.getOutputStream().flush();*/
		} catch (Exception e) {
			
		}       
     
		return "success";
	}
	
	public String headerReportObject(){
		 log.info("In headerReportObject() of NewAdvertiserViewAction");
		 List<SummaryReportDTO> summaryReportObjectList = null;
	    try{
	    	 String orderId=request.getParameter("orderId");	
	    	 String campaignName = request.getParameter("campaignName");
	    	 String accountName = request.getParameter("accountName");
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 summaryReportObjectList = service.summaryReportObject(orderId,campaignName,accountName, publisherIdInBQ);
				 }
		    	 
			 }else{
				 log.warning("Invalid data....."); 
			 }
	     }catch(Exception e){
	    	 log.severe("Exception in headerReportObject()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
       ExcelReportGenerator erGen = new ExcelReportGenerator();
       byte[] excelbytes = null;
       try {
			excelbytes = erGen.generateReport("SummaryReport.xls","SummaryReportDTO", summaryReportObjectList);
			inputStream = new ByteArrayInputStream(excelbytes);
		} catch (Exception e) {
			
		}     
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	public String advertiserReportObject(){
		 log.info("In advertiserReportObject() of NewAdvertiserViewAction");
		 List<PerformanceBySiteReportDTO> performanceBySiteReportList = null;
		 List<SummaryReportDTO> summaryReportObjectList = null;
		 List<NewAdvertiserViewDTO> deliveryMetricObjectList = null;
		 List<PerformanceByOSReportDTO> performancebyOSList = null;
		 List<PerformanceByDeviceReportDTO> performancebyDeviceList = null;
		 List<PerformanceByPlacementReportDTO> performancebyPlacementList = null;
		 List<PerformanceByAdSizeReportDTO> performancebyAdSizeList = null;
		 List<MostActiveReportDTO> mostActiveReportDataList = null;
		 List<PerformanceByLocationReportDTO> performanceByLocationList = null;
		 List<PerformerReportDTO> performerReportDataList = null;
		 List<NonPerformerReportDTO> nonPerformerReportDataList = null;
		 Map map = new HashMap();
	    try{
	    	 String orderId=request.getParameter("campaignId");	
	    	 String campaignName = request.getParameter("campaignName");
	    	 String accountName = request.getParameter("accountName");
	         Date date = new Date();
	         String formatedate = DateUtil.getFormatedStringDateYYYYMMDD(date);
	         fileName = campaignName+formatedate;
	         fileName = fileName.replaceAll("[|<>\"?*:\\/]", "-");
	         fileName = fileName+".xls";
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
		    	 // if request comes from backend instance, session would be null and publisherIdInBQ need to be obtained from campaigns company
		    	 if(publisherIdInBQ.isEmpty()) {
		    		 SmartCampaignObj campaignObj = (new SmartCampaignPlannerDAO()).getCampaignByDFPOrderId(StringUtil.getLongValue(orderId));
		    		 if(campaignObj != null) {
		    			 String companyId = campaignObj.getCompanyId();
		    			 CompanyObj companyObj = (new UserDetailsDAO()).getCompanyById(StringUtil.getLongValue(companyId), MemcacheUtil.getAllCompanyList());
		    			 if(companyObj != null) {
		    				 publisherIdInBQ = companyObj.getBqIdentifier()+"";
		    			 }
		    		 }
		    	 }
				 if(publisherIdInBQ != null && publisherIdInBQ.length() > 0) {
			    	 performanceBySiteReportList = service. performanceBySiteReportObject(orderId, publisherIdInBQ);
			    	 summaryReportObjectList = service.summaryReportObject(orderId, campaignName, accountName, publisherIdInBQ);
			    	 deliveryMetricObjectList = service.deliveryMetricsReportObject(orderId, publisherIdInBQ);
			    	 performancebyOSList = service.performanceByOSReportObject(orderId, publisherIdInBQ);
			    	 performancebyDeviceList = service.performanceByDeviceReportObject(orderId, publisherIdInBQ);
			    	 performancebyPlacementList = service.performanceByPlacementReportObject(orderId, publisherIdInBQ);
			    	 performancebyAdSizeList = service.performanceByAdSizeReportObject(orderId, publisherIdInBQ);
			    	 mostActiveReportDataList = service.mostActiveReportObject(orderId, publisherIdInBQ);
			    	 performanceByLocationList = service.performanceByLocationReportobject(orderId, publisherIdInBQ);
			    	 performerReportDataList = service.PerformerReportObject(orderId, publisherIdInBQ);
			    	 nonPerformerReportDataList = service.NonPerformerReportObject(orderId, publisherIdInBQ);
				 } else {
					log.info("publisherIdInBQ is Empty"); 
				 }
		    	 if(performanceBySiteReportList!=null && performanceBySiteReportList.size()>0){
		    		 map.put("PerformanceBySiteReportDTO",performanceBySiteReportList);
		    	 }
		    	 if(summaryReportObjectList!=null && summaryReportObjectList.size()>0){
		    		 map.put("SummaryReportDTO",summaryReportObjectList);
		    	 }
		    	 if(deliveryMetricObjectList!=null && deliveryMetricObjectList.size()>0){
		    		 map.put("NewAdvertiserViewDTO",deliveryMetricObjectList);
		    	 }
		    	 if(performancebyOSList!=null && performancebyOSList.size()>0){
		    		 map.put("PerformanceByOSReportDTO",performancebyOSList);
		    	 }
		    	 if(performancebyDeviceList!=null && performancebyDeviceList.size()>0){
		    		 map.put("PerformanceByDeviceReportDTO",performancebyDeviceList);
		    	 }
		    	 if(performancebyPlacementList!=null && performancebyPlacementList.size()>0){
		    		 map.put("PerformanceByPlacementReportDTO",performancebyPlacementList);
		    	 }
		    	 if(performancebyAdSizeList!=null &&  performancebyAdSizeList.size()>0){
		    		 map.put("PerformanceByAdSizeReportDTO",performancebyAdSizeList);
		    	 }
		    	 if(mostActiveReportDataList!=null && mostActiveReportDataList.size()>0){
		    		 map.put("MostActiveReportDTO",mostActiveReportDataList);
		    	 }
		    	 if(performanceByLocationList!=null && performanceByLocationList.size()>0){
		    		 map.put("PerformanceByLocationReportDTO", performanceByLocationList); 
		    	 }
		    	 if(performerReportDataList!=null && performerReportDataList.size()>0){
		    		 map.put("PerformerReportDTO", performerReportDataList);
		    	 }
		    	 if(nonPerformerReportDataList!=null && nonPerformerReportDataList.size()>0){
		    		 map.put("NonPerformerReportDTO", nonPerformerReportDataList);
		    	 }
		    	
			 }else{
				 log.warning("Invalid data....."); 
			 }
	     }catch(Exception e){
	    	 log.severe("Exception in advertiserReportObject()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
      ExcelReportGenerator erGen = new ExcelReportGenerator();
      byte[] excelbytes = null;
      try {
			excelbytes = erGen.advertiserReportGenerate("AdvertiserReport.xls",map);
			inputStream = new ByteArrayInputStream(excelbytes);
		} catch (Exception e) {
			
		}     
		return "success";
	}
	
	public String performanceBySiteReportObject(){
		 log.info("In performanceBySiteReportObject() of NewAdvertiserViewAction");
		 List<PerformanceBySiteReportDTO> performanceBySiteReportList = null;
	    try{
	    	 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
		    	 INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
		    	 String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 performanceBySiteReportList = service. performanceBySiteReportObject(orderId, publisherIdInBQ);
				 }
		    	 
			 }else{
				 log.warning("Invalid data....."); 
			 }
	     }catch(Exception e){
	    	 log.severe("Exception in performanceBySiteReportObject()  in NewAdvertiserViewAction : "+e.getMessage());
			 
	     }
     ExcelReportGenerator erGen = new ExcelReportGenerator();
     byte[] excelbytes = null;
     try {
			excelbytes = erGen.generateReport("PerformanceBySite.xls","NewAdvertiserViewDTO", performanceBySiteReportList);
			inputStream = new ByteArrayInputStream(excelbytes);
		} catch (Exception e) {
			
		}     
		return "success";
	}
	
	public String impressionsByAdSizeChartData(){
		 log.info("In impressionsByAdSizeChartData() of NewAdvertiserViewAction");
		 try{
			 String orderId=request.getParameter("orderId");	
			 log.info("orderId : "+orderId);	
			 if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
			   INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
			   String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 DataTable dataTable = service.impressionsByAdSizeChartData(orderId, publisherIdInBQ);
					 impressionByAdSizeChartDataJsonStr =JsonRenderer.renderDataTable(dataTable, true, false).toString();
				 }
			 }else{
				log.warning("Invalid data.....");			
			 } 
		 }catch(Exception e){
			 log.severe("Exception in impressionsByAdSizeChartData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		 }
		 
		 return Action.SUCCESS;
	}
	
	public String videoCampaignData() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			String orderId=request.getParameter("orderId");
			log.info("orderId : "+orderId);
			if(orderId != null && (LinMobileUtil.isNumeric(orderId))){
				INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				String publisherIdInBQ = getPublisherIdInBQ();
				 if(!publisherIdInBQ.isEmpty()) {
					 videoCampaignDataMap=service.videoCampaignData(orderId, publisherIdInBQ);
				 }
			}else{
				log.warning("Invalid data.....");			
			}
		} catch(Exception e) {
			 log.severe("Exception in videoCampaignData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	public String campaignGridData() {
		try {
			IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			int campaignsPerPage = 20;
			int pageNumber = 1;
			boolean activeCampaign = false;
			String dataType=request.getParameter("dataType");
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			String publisherIdInBQ=request.getParameter("publisherIdInBQ");
			String searchKeyword=request.getParameter("searchKeyword");
			log.info("dataType : "+dataType+", limit : "+limit+", offset : "+offset+", publisherIdInBQ : "+publisherIdInBQ+", searchKeyword : "+searchKeyword);
			if(dataType != null && publisherIdInBQ != null && (LinMobileUtil.isNumeric(publisherIdInBQ))) {
				if(limit != null && (LinMobileUtil.isNumeric(limit))) {
					campaignsPerPage = Integer.parseInt(limit);
					if(campaignsPerPage <= 0) {
						campaignsPerPage = 20;
					}
				}
				if(offset != null && (LinMobileUtil.isNumeric(offset.trim()))) {
					pageNumber = Integer.parseInt(offset);
					if(pageNumber <= 0) {
						pageNumber = 1;
					}
				}
				if(dataType.equalsIgnoreCase("active")) {
					activeCampaign = true;
				}
				if(searchKeyword != null) {
					searchKeyword = searchKeyword.toLowerCase().trim();
				}
				String commaSeperatedAccountIds = userService.getSelectedAccountsInfo(sessionDTO.getUserId(), true, true);
				INewAdvertiserViewService service = (INewAdvertiserViewService)BusinessServiceLocator.locate(INewAdvertiserViewService.class);
				campaignGridArray = service.campaignGridData(activeCampaign, campaignsPerPage, pageNumber, publisherIdInBQ, searchKeyword, commaSeperatedAccountIds);
			}else{
				log.warning("Invalid parameters.....");			
			}
		} catch(Exception e) {
			 log.severe("Exception in campaignGridData()  in NewAdvertiserViewAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	/*public String loadTopLevelAdUnits(){
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO dao = new  UserDetailsDAO();
		System.out.println("===========");
		try {
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			//String prefixStr = "";
			String password = "";
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
		    String clientLoginToken = null;
			//sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(adServerId != null && adServerId.trim().length() > 0 && adServerUsername != null && adServerUsername.trim().length() > 0) {
				List<LineItemDTO> siteList = MemcacheUtil.getDFPPropertiesFromCache(adServerId + adServerUsername);
				if(siteList != null && siteList.size() > 0) {
					log.info("Memcache exists, size : "+siteList.size());
				}
				else {
					log.info("Memcache does not exists........... searching from DFP");
					//List<CompanyObj> companyObjList = dao.getSelectedCompaniesByUserId(sessionDTO.getUserId());
					List<CompanyObj> companyObjList = dao.getSelectedCompaniesByUserId(2);
					if(companyObjList != null && companyObjList.size() > 0) {
						Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(companyObjList);
						if(map != null && !map.isEmpty()) {
							for(String key : map.keySet()) {
								AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
								if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerId().equals(adServerId.trim()) && adServerCredentialsDTO.getAdServerUsername() != null && adServerCredentialsDTO.getAdServerUsername().equals(adServerUsername.trim()) && adServerCredentialsDTO.getAdServerPassword() != null) {
									password = adServerCredentialsDTO.getAdServerPassword();
									
									clientLoginToken = UserService.regenerateAccountAuthToken(adServerUsername.trim(), password.trim());
									log.info("clientLoginToken : "+clientLoginToken+", for Adserver userName : "+adServerUsername);
									
									dfpSession = new DfpSession.Builder()
									  				.withClientLoginToken(clientLoginToken)					
									  				.withNetworkCode(adServerId)
									  				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
									  				.build();
									log.info(" getting DfpServices instance from properties...");
									dfpServices = LinMobileProperties.getInstance().getDfpServices();
									
									IDFPReportService dfpReportService = new DFPReportService();
									siteList = new ArrayList<>();
									dfpReportService.getTopLevelAdUnitsFromDFP(dfpServices, dfpSession, adServerId, adServerUsername, "", siteList, new StringBuilder());
									break;
								}
							}
						}
					}
				}
				if(siteList != null) {
					log.info("list size : "+siteList.size());
					JSONArray jsonArray = new JSONArray();
					for (LineItemDTO lineItemDTO : siteList) {
						String siteName=lineItemDTO.getSiteName();
						
						if(siteName != null && !siteName.equals("null")){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", lineItemDTO.getSiteId());
							jsonObject.put("name", siteName);
							jsonArray.add(jsonObject);
						}
					}
					campaignGridArray = jsonArray;
				}
				else {
					log.info("list : null");
				}
			}
		}catch (Exception e) {
			log.severe("Exception in loadTopLevelAdUnits of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
   }*/
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}

	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}

	
	public void setHeaderDataList(List<NewAdvertiserViewDTO> headerDataList) {
		this.headerDataList = headerDataList;
	}

	public List<NewAdvertiserViewDTO> getHeaderDataList() {
		return headerDataList;
	}

	
	public void setJsonStr(java.lang.CharSequence jsonStr) {
		this.jsonStr = jsonStr;
	}

	public java.lang.CharSequence getJsonStr() {
		return jsonStr;
	}

	public void setPerformerJsonStr(java.lang.CharSequence performerJsonStr) {
		this.performerJsonStr = performerJsonStr;
	}

	public java.lang.CharSequence getPerformerJsonStr() {
		return performerJsonStr;
	}

	public void setPerformerDataStr(String performerDataStr) {
		this.performerDataStr = performerDataStr;
	}

	public String getPerformerDataStr() {
		return performerDataStr;
	}

	public void setMostActiveDataStr(String mostActiveDataStr) {
		this.mostActiveDataStr = mostActiveDataStr;
	}

	public String getMostActiveDataStr() {
		return mostActiveDataStr;
	}

	public void setDeliveryMetricsStr(String deliveryMetricsStr) {
		this.deliveryMetricsStr = deliveryMetricsStr;
	}

	public String getDeliveryMetricsStr() {
		return deliveryMetricsStr;
	}

	public void setPerformanceByLocationChartDatajsonStr(
			java.lang.CharSequence performanceByLocationChartDatajsonStr) {
		this.performanceByLocationChartDatajsonStr = performanceByLocationChartDatajsonStr;
	}

	public java.lang.CharSequence getPerformanceByLocationChartDatajsonStr() {
		return performanceByLocationChartDatajsonStr;
	}

	public void setPerformanceByAdSizeChartDataJsonStr(
			java.lang.CharSequence performanceByAdSizeChartDataJsonStr) {
		this.performanceByAdSizeChartDataJsonStr = performanceByAdSizeChartDataJsonStr;
	}

	public java.lang.CharSequence getPerformanceByAdSizeChartDataJsonStr() {
		return performanceByAdSizeChartDataJsonStr;
	}

	public void setPerformanceByOSeChartDataJsonStr(
			java.lang.CharSequence performanceByOSeChartDataJsonStr) {
		this.performanceByOSeChartDataJsonStr = performanceByOSeChartDataJsonStr;
	}

	public java.lang.CharSequence getPerformanceByOSeChartDataJsonStr() {
		return performanceByOSeChartDataJsonStr;
	}

	public void setPerformanceByDeviceChartDataJsonStr(
			java.lang.CharSequence performanceByDeviceChartDataJsonStr) {
		this.performanceByDeviceChartDataJsonStr = performanceByDeviceChartDataJsonStr;
	}

	public java.lang.CharSequence getPerformanceByDeviceChartDataJsonStr() {
		return performanceByDeviceChartDataJsonStr;
	}

	public void setMobileWebVsAppChartDataJsonStr(
			java.lang.CharSequence mobileWebVsAppChartDataJsonStr) {
		this.mobileWebVsAppChartDataJsonStr = mobileWebVsAppChartDataJsonStr;
	}

	public java.lang.CharSequence getMobileWebVsAppChartDataJsonStr() {
		return mobileWebVsAppChartDataJsonStr;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	
	public Map<String,String> getDataMap() {
		return dataMap;
	}

	public void setDateMap(Map<String,String> dataMap) {
		this.dataMap = dataMap;
	}
	
	public void setCustomEvents(List<CommonDTO> customEvents) {
		this.customEvents = customEvents;
	}

	public List<CommonDTO> getCustomEvents() {
		return customEvents;
	}

	public void setRichMediaGraphTable(String richMediaGraphTable) {
		this.richMediaGraphTable = richMediaGraphTable;
	}

	public String getRichMediaGraphTable() {
		return richMediaGraphTable;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setNewAdvertiserViewDTO(NewAdvertiserViewDTO newAdvertiserViewDTO) {
		this.newAdvertiserViewDTO = newAdvertiserViewDTO;
	}

	public NewAdvertiserViewDTO getNewAdvertiserViewDTO() {
		return newAdvertiserViewDTO;
	}

	public void setImpressionByOSeChartDataJsonStr(
			java.lang.CharSequence impressionByOSeChartDataJsonStr) {
		this.impressionByOSeChartDataJsonStr = impressionByOSeChartDataJsonStr;
	}

	public java.lang.CharSequence getImpressionByOSeChartDataJsonStr() {
		return impressionByOSeChartDataJsonStr;
	}

	public void setImpressionByDeviceChartDataJsonStr(
			java.lang.CharSequence impressionByDeviceChartDataJsonStr) {
		this.impressionByDeviceChartDataJsonStr = impressionByDeviceChartDataJsonStr;
	}

	public java.lang.CharSequence getImpressionByDeviceChartDataJsonStr() {
		return impressionByDeviceChartDataJsonStr;
	}


	public void setImpressionBymobileWebVsAppChartDataJsonStr(
			java.lang.CharSequence impressionBymobileWebVsAppChartDataJsonStr) {
		this.impressionBymobileWebVsAppChartDataJsonStr = impressionBymobileWebVsAppChartDataJsonStr;
	}

	public java.lang.CharSequence getImpressionBymobileWebVsAppChartDataJsonStr() {
		return impressionBymobileWebVsAppChartDataJsonStr;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setImpressionByAdSizeChartDataJsonStr(
			java.lang.CharSequence impressionByAdSizeChartDataJsonStr) {
		this.impressionByAdSizeChartDataJsonStr = impressionByAdSizeChartDataJsonStr;
	}

	public java.lang.CharSequence getImpressionByAdSizeChartDataJsonStr() {
		return impressionByAdSizeChartDataJsonStr;
	}

	public Map<String,Object> getVideoCampaignDataMap() {
		return videoCampaignDataMap;
	}

	public void setVideoCampaignDataMap(Map<String,Object> videoCampaignDataMap) {
		this.videoCampaignDataMap = videoCampaignDataMap;
	}

	public JSONArray getCampaignGridArray() {
		return campaignGridArray;
	}

	public void setCampaignGridArray(JSONArray campaignGridArray) {
		this.campaignGridArray = campaignGridArray;
	}
	
}
