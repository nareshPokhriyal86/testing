package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.CompanyObj;
import com.lin.web.dto.CampaignStatusAlertDTO;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IAlertEngineService;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


public class AlertEngineAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

	private static final Logger log = Logger.getLogger(AlertEngineAction.class.getName());
	private InputStream inputStream;
	private SessionObjectDTO sessionDTO;
	private Map session;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private JSONArray jsonArray;
	
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

	
	public String alertEngine(){
		log.info("alertEngine starts.. "+new Date());
		IAlertEngineService service = (IAlertEngineService)BusinessServiceLocator.locate(IAlertEngineService.class);
		ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		List<CompanyObj> companyList = new ArrayList<>();

		try{
			String alertDate = request.getParameter("alertDate");
			log.info("alertDate in alertEngine() of AlertEngineAction = "+alertDate);
			companyList = plannerService.getAllCompanyList();
			if(companyList!=null && companyList.size()>0){
				 service.alertEngine(companyList,alertDate);
			}
			//String publisherIdInBQ = getPublisherIdInBQ();
			/*String publisherIdInBQ = "5";
			 if(!publisherIdInBQ.isEmpty()) {
				 service.alertEngine(publisherIdInBQ,alertDate);
			 }*/
		
		}catch(Exception e){
			log.severe("Exception in alertEngine()  in AlertEngineAction : "+e.getMessage());
			 
		}
		return Action.SUCCESS;
	}
	
	public String generateCampaignAlert(){
		log.info("generateCampaignAlert starts.. "+new Date());
		String orderId = request.getParameter("orderId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String alertDate = request.getParameter("alertDate");
		String bqIdentifier = request.getParameter("bqIdentifier");
		if(orderId!=null && orderId.trim().length()>0 && startDate!=null && startDate.trim().length()>0 && endDate!=null && endDate.trim().length()>0){
			IAlertEngineService service = (IAlertEngineService)BusinessServiceLocator.locate(IAlertEngineService.class);
			service.generateCampaignAlert(orderId, startDate, endDate, alertDate, bqIdentifier);
		}else{
			log.warning("Invalid parameters");
		}
		return Action.SUCCESS;
	}

	/*Author : Dheeraj Kumar
	 * Generate excel sheet report of currently generated alerts and sent to all users who have opted for email alert.
	 */
	public void campaignStatusAlertMail(){
		log.info("campaignStatusAlertMail starts.. "+new Date());
		IAlertEngineService service = (IAlertEngineService)BusinessServiceLocator.locate(IAlertEngineService.class);
		Map<Long,Map> campaignAlertMap = null;
		List<PerformanceMonitoringDTO> campaignList = null;
		String publisherIdInBQ = "5";
		
		
		try{
			campaignList = service.loadAllRunningCampaigns(publisherIdInBQ);
			campaignAlertMap = service.campaignStatusAlertMail();
			if(campaignAlertMap != null && campaignAlertMap.size() > 0)
			 {
				for(Long publisherId : campaignAlertMap.keySet())
				{
			     service.sendCampaignStatusAlertMail(publisherId,campaignAlertMap.get(publisherId),campaignList);
				}
			 }
		
		}catch(Exception e){
			log.severe("Exception in campaignStatusAlertMail()  in AlertEngineAction : "+e.toString());
			 
		}
	      
			//return "success";
	}	
	
	public String campaignAlertListing(){
		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		if(!isAuthorised(sessionDTO)) {
			return "unAuthorisedAccess";
		}
		
		return Action.SUCCESS;
	}
	
	public String loadAllCampaignAlert(){
		log.info("loadAllCampaigns starts.. "+new Date());
		IAlertEngineService service = (IAlertEngineService)BusinessServiceLocator.locate(IAlertEngineService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			int campaignPerPage = 25;
			int pageNumber = 1;
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			String dateSearch=request.getParameter("dateSearch");
			String campaignSearch=request.getParameter("campaignSearch");
			String placementSearch=request.getParameter("placementSearch");
			String publisherSearch=request.getParameter("publisherSearch");
			
			log.info("limit : "+limit+", offset : "+offset);
			log.info(" dateSearch :"+dateSearch);
			log.info(" campaignSearch :"+campaignSearch);
			log.info(" placementSearch :"+placementSearch);
			log.info(" publisherSearch :"+publisherSearch);
			
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
			if(dateSearch != null) {
				dateSearch = dateSearch.toLowerCase().trim();
			}
			if(campaignSearch != null) {
				campaignSearch = campaignSearch.toLowerCase().trim();
			}
			if(placementSearch != null) {
				placementSearch = placementSearch.toLowerCase().trim();
			}
			if(publisherSearch != null) {
				publisherSearch = publisherSearch.toLowerCase().trim();
			}
				 jsonArray = service.loadAllCampaignAlert(sessionDTO.getUserId(), sessionDTO.isSuperAdmin(), campaignPerPage, pageNumber, dateSearch, campaignSearch, placementSearch, publisherSearch);
				 if(jsonArray != null){
				 log.info("Records came in search = "+jsonArray.size());
				 }
		}catch(Exception e){
			log.severe("Exception in loadAllCampaigns()  in AlertEngineAction : "+e.getMessage());
			 
		}
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
	
	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
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

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}


	public JSONArray getJsonArray() {
		return jsonArray;
	}


	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
	
}
