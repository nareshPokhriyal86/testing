package com.lin.web.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.web.dto.ForcastLineItemDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.ReconciliationDataDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

public class OperationalViewAction implements ModelDriven<UserDetailsDTO>, ServletRequestAware, SessionAware{
	private static final Logger log = Logger.getLogger(AdvertiserViewAction.class.getName());
	
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private Map session;
	private SessionObjectDTO sessionDTO;
	
	private HttpServletRequest request;
	public String operationalViewPageName = (LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim();
	
	private String clientLoginToken = null;
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    private List<LineItemDTO> campaignTraffickingDataList;
    private HashMap<String,Integer> startEventMap;
    private HashMap<String,Integer> endEventMap;
	private List<ForcastLineItemDTO> forcastLineItemDTOList;
	private List<ReconciliationDataDTO> reconciliationSummaryDataList;
	private List<ReconciliationDataDTO> reconciliationDetailsDataList;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;

	public String execute(){
		log.info("OperationalViewAction action executes..");
    	IUserService userService = new UserService();
    	try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return Action.SUCCESS;
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in OperationalViewAction : "+e.getMessage());
    		e.printStackTrace();
		}
    	return "unAuthorisedAccess";
	   
    }
    
    public String loadCampaignTraffickingData() throws Exception
    {
    	log.info("Inside loadCampaignTraffickingData() in RichMediaAdvertiserViewAction");
    	
    	/*try{
    		campaignTraffickingDataList = MemcacheUtil.getCampaignTraffickingData();
    	}catch(Exception e){
    		log.info("Exception found in getting CampaignTraffickingData from memcache...");
    		e.printStackTrace();
    	}
    	if(campaignTraffickingDataList == null || campaignTraffickingDataList.size() <= 0)
		{*/
		    	String lowerDate=request.getParameter("startDate");
		    	String upperDate=request.getParameter("endDate");
			try
			{
				sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			    long userId = sessionDTO.getUserId();
				clientLoginToken = regenerateAuthToken();
				log.info(" clientLoginToken in loadCampaignTraffickingData(): "+clientLoginToken);
				log.info(" now going to build dfpSession in loadCampaignTraffickingData()...");
				dfpSession = new DfpSession.Builder()
				      				.withClientLoginToken(clientLoginToken)					
				      				.withNetworkCode(LinMobileConstants.DFP_NETWORK_CODE)
				      				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
				      				.build();
				      
				log.info(" now going to make DfpServices instance in loadCampaignTraffickingData()...");
				dfpServices = LinMobileProperties.getInstance().getDfpServices();
				  
				log.info("dfpServices === "+dfpServices);
				lowerDate = lowerDate+"T00:00:00";
				upperDate = upperDate+"T00:00:00";
				
				log.info("before creating service object in loadCampaignTraffickingData()...");
				ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
				campaignTraffickingDataList = service.loadCampaignTraffickingData(dfpServices,dfpSession,lowerDate, upperDate,userId);
				//MemcacheUtil.setCampaignTraffickingData(campaignTraffickingDataList);
			}catch(Exception e){
				log.info("Exception found in getting CampaignTrafficking Data from DFP...");
				e.printStackTrace();
			}
    //	}

		    if(campaignTraffickingDataList!=null && campaignTraffickingDataList.size()!=0)
		    {
		    	startEventMap = new HashMap<String,Integer>();
		    	endEventMap = new HashMap<String,Integer>();
		    	
		    	for(LineItemDTO lineItemObj : campaignTraffickingDataList)
		    	{
		    		String dateStart = lineItemObj.getStartDateTime();
		    		String dateEnd = lineItemObj.getEndDateTime();
		    		
		    		if(dateStart != null && dateStart.indexOf("2013") >= 0)
		    		{
			    		if(startEventMap.containsKey(dateStart))
			    		{
			    			int value = startEventMap.get(dateStart);
			    			value++;
			    			startEventMap.put(dateStart, value);
			    		}
			    		else
			    		{
			    			startEventMap.put(dateStart, 1);
			    		}
		    		}
		    		
		    		
		    		if(dateEnd != null && dateEnd.indexOf("2013") >= 0)
		    		{
			    		if(endEventMap.containsKey(dateEnd))
			    		{
			    			int value = endEventMap.get(dateEnd);
			    			value++;
			    			endEventMap.put(dateEnd, value);
			    		}
			    		else
			    		{
			    			endEventMap.put(dateEnd, 1);
			    		}
		    		}
		    	
		    	}
		    	return Action.SUCCESS;
		    }

    	return Action.SUCCESS;
    }
	
    
    public String loadForcasts(){
    	log.info("Inside loadForcasts() if PublisherViewAction class...");
		log.info("request.getParameter(IdsOfLineItems) = "+request.getParameter("IdsOfLineItems"));
		String IdsList = request.getParameter("IdsOfLineItems");
		IdsList = IdsList.replaceFirst("~", "");
		String arrayOfLineItemIds[] =  IdsList.split("~");
    	
    	try{
    		forcastLineItemDTOList = MemcacheUtil.getLineItemForcasts(IdsList);
    	}catch(Exception e){
    		log.info("Exception found in getting Trafficking Forecasted Data from memcache...");
    		e.printStackTrace();
    	}
    	if(forcastLineItemDTOList == null || forcastLineItemDTOList.size() <= 0)
    	{
    		try{
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
    		clientLoginToken = regenerateAuthToken();
		    log.info(" clientLoginToken : "+clientLoginToken);
		    log.info(" now going to build dfpSession ...");
		    dfpSession = new DfpSession.Builder()
		      				.withClientLoginToken(clientLoginToken)					
		      				.withNetworkCode(LinMobileConstants.DFP_NETWORK_CODE)
		      				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
		      				.build();
		      
		      log.info(" now going to make DfpServices instance in loadForcasts()...");
			  dfpServices = LinMobileProperties.getInstance().getDfpServices();
			  log.info("dfpServices in loadForcasts()... = "+dfpServices);
			  ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
			  forcastLineItemDTOList = service.getLineItemForcasts(dfpServices, dfpSession, arrayOfLineItemIds);
			  MemcacheUtil.setLineItemForcasts(forcastLineItemDTOList,IdsList);
    		
    	}catch (ValidationException e) {
			log.severe("DFP session exception: ValidationException :"+e.getMessage());				
			e.printStackTrace();
			//setErrorStatus("validationError");
	   }catch(Exception e){
			log.severe("Exception in loadForcasts() in PublisherViewAction "+e.getMessage());
			e.printStackTrace();
			//setErrorStatus("DFPAPIError");
    	}
    }
    	return Action.SUCCESS;
    }
	
    public String loadAllReconciliationData(){
    	log.info("inside loadAllReconciliationData in publisherViewAction");
    	try {
    		String startDate = request.getParameter("startDate");
    		String endDate = request.getParameter("endDate");
    		String publisherId = request.getParameter("publisherId");
    		
    		reconciliationSummaryDataList = new ArrayList<ReconciliationDataDTO>();
    		reconciliationDetailsDataList = new ArrayList<ReconciliationDataDTO>();
    		/*startDate = startDate+" 00:00:00";
    		endDate = endDate+" 00:00:00";*/
			
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
	    	if(startDate != null && endDate != null && publisherId != null && !publisherId.trim().equalsIgnoreCase("")) {
	    		service.loadAllReconciliationData(startDate, endDate, publisherId, sessionDTO.getUserId(), reconciliationSummaryDataList, reconciliationDetailsDataList);
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadAllReconciliationData in publisherViewAction");
    		e.printStackTrace();
		}
    	return Action.SUCCESS;
    }
    
    public String regenerateAuthToken() throws Exception {
		 String clientLoginToken=MemcacheUtil.getAuthToken();
		 if(clientLoginToken ==null){
			 ClientLoginAuth clientLoginAuth = new ClientLoginAuth(LinMobileConstants.EMAIL_ADDRESS, LinMobileConstants.EMAIL_PASSWORD);
			 clientLoginToken =clientLoginAuth.getAuthToken();
			 MemcacheUtil.setAuthToken(clientLoginToken);
		 }	    
	    return clientLoginToken;
	}
    
    @Override
	public UserDetailsDTO getModel() {	
		return userDetailsDTO;
	}
    
	public void setSession(Map<String, Object> session) {
		this.session = session;
		}

	public Map<String, Object> getSession() {
		return session;
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public List<LineItemDTO> getCampaignTraffickingDataList() {
		return campaignTraffickingDataList;
	}

	public void setCampaignTraffickingDataList(
			List<LineItemDTO> campaignTraffickingDataList) {
		this.campaignTraffickingDataList = campaignTraffickingDataList;
	}

	public HashMap<String, Integer> getStartEventMap() {
		return startEventMap;
	}

	public void setStartEventMap(HashMap<String, Integer> startEventMap) {
		this.startEventMap = startEventMap;
	}

	public HashMap<String, Integer> getEndEventMap() {
		return endEventMap;
	}

	public void setEndEventMap(HashMap<String, Integer> endEventMap) {
		this.endEventMap = endEventMap;
	}

	public List<ForcastLineItemDTO> getForcastLineItemDTOList() {
		return forcastLineItemDTOList;
	}

	public void setForcastLineItemDTOList(
			List<ForcastLineItemDTO> forcastLineItemDTOList) {
		this.forcastLineItemDTOList = forcastLineItemDTOList;
	}

	public List<ReconciliationDataDTO> getReconciliationSummaryDataList() {
		return reconciliationSummaryDataList;
	}

	public void setReconciliationSummaryDataList(
			List<ReconciliationDataDTO> reconciliationSummaryDataList) {
		this.reconciliationSummaryDataList = reconciliationSummaryDataList;
	}

	public List<ReconciliationDataDTO> getReconciliationDetailsDataList() {
		return reconciliationDetailsDataList;
	}

	public void setReconciliationDetailsDataList(
			List<ReconciliationDataDTO> reconciliationDetailsDataList) {
		this.reconciliationDetailsDataList = reconciliationDetailsDataList;
	}
		
	
}
