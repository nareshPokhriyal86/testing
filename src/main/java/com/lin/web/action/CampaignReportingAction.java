package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class CampaignReportingAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	private static final Logger log = Logger.getLogger(CampaignReportingAction.class.getName());
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private SessionObjectDTO sessionDTO;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	private String company;
	private JSONObject jsonObject;
	private String publisherBQId;
	private InputStream inputStream;
	
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
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[9]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[9]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			e.printStackTrace();
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
			log.info("Exception in getPublisherIdInBQ in CampaignReportingAction : "+e.getMessage());
			e.printStackTrace();
		}
		log.info("publisherIdInBQ : "+publisherIdInBQ);
		return publisherIdInBQ;
	}
	
	public String execute() {
    	log.info("ReportingAction action executes..");
    	try{
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}else {
				if(sessionDTO.isSuperAdmin()) {
					company = "All";
				} else {
					IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
					CompanyObj companyObj = userService.getCompanyIdByAdminUserId(sessionDTO.getUserId());
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
						company = companyObj.getId()+"";
					}
				}
				publisherBQId=getPublisherIdInBQ();
				return Action.SUCCESS;
			}
    	}
    	catch (Exception e) {
    		log.severe("Exception in ReportingAction : "+e.getMessage());
    		e.printStackTrace();
		}
    	return "unAuthorisedAccess";
    }
	
	public String searchCampaigns() {
		try {
			jsonObject=new JSONObject();
			String company=request.getParameter("company");
			String searchText=request.getParameter("searchText");
			log.info("company : "+company+", searchText : "+searchText);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(searchText !=null && searchText.trim().length()>=1){
					 ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					 List<SmartCampaignObj> smartCampaignObjs = plannerService.loadAllCampaignsFromCache();
					 if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
						 jsonObject=plannerService.searchCampaignsJSON(smartCampaignObjs, company, sessionDTO.isSuperAdmin(), searchText.toLowerCase().trim(), sessionDTO.getUserId());
					 }else {
						 throw new Exception("No campaign in DataStore");
					 }
				}else{
					throw new Exception("Invalid searchText, it must atleast 1 letter long :"+searchText);
				}
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in searchCampaigns : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("campaigns", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String searchPlacements() {
		try {
			jsonObject=new JSONObject();
			String company=request.getParameter("company");
			String searchText=request.getParameter("searchText");
			log.info("company : "+company+", searchText : "+searchText);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(searchText !=null && searchText.trim().length()>=1){
					 ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					 List<SmartCampaignObj> smartCampaignObjs = plannerService.loadAllCampaignsFromCache();
					 if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
						 jsonObject=plannerService.searchPlacementsJSON(smartCampaignObjs, company, sessionDTO.isSuperAdmin(), searchText.toLowerCase().trim(), sessionDTO.getUserId());
					 }else {
						 throw new Exception("No campaign in DataStore");
					 }
				}else{
					throw new Exception("Invalid searchText, it must atleast 1 letter long :"+searchText);
				}
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in searchPlacements : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("placements", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String selectedPlacements() {
		try {
			jsonObject=new JSONObject();
			String commaSeperatedCampaignIds=request.getParameter("campaignIds");
			log.info("commaSeperatedCampaignIds : "+commaSeperatedCampaignIds);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(commaSeperatedCampaignIds !=null && commaSeperatedCampaignIds.trim().length()>0){
					 ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					 List<SmartCampaignObj> smartCampaignObjs = plannerService.loadAllCampaignsByIds(commaSeperatedCampaignIds.replaceAll(" ", ""));
					 if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
						 jsonObject=plannerService.selectedPlacementsJSON(smartCampaignObjs);
					 }else {
						 throw new Exception("No campaign in DataStore");
					 }
				}else{
					throw new Exception("Empty campaign ids :"+commaSeperatedCampaignIds);
				}
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in selectedPlacements : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("placements", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String searchAccounts() {
		try {
			jsonObject=new JSONObject();
			String searchText=request.getParameter("searchText");
			log.info("searchText : "+searchText);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(searchText !=null && searchText.trim().length()>=1){
					IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
					jsonObject=userService.searchAccounts(sessionDTO.getUserId(), true, false, searchText.toLowerCase().trim());
				}else{
					throw new Exception("Invalid searchText, it must atleast 1 letter long :"+searchText);
				}
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in searchPlacements : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("accounts", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String selectedPartners() {
		try {
			jsonObject=new JSONObject();
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
				List<CompanyObj> companyList=userService.loadAllPartners();
				jsonObject = userService.loadPartnersJSON(companyList);
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in selectedPartners : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("partners", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String runCampaignReport() {
		try {
			jsonObject=new JSONObject();
			String campaignIds=request.getParameter("campaignIds");
			String placementIds=request.getParameter("placementIds");
			String partners=request.getParameter("partners");
			String accounts=request.getParameter("accounts");
			String reportType=request.getParameter("reportType");
			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String company=request.getParameter("company");
			
			log.info("campaignIds : "+campaignIds+"; placementIds : "+placementIds+"; partners : "+partners+"; accounts : "+accounts+"; reportType : "+reportType+
									"; startDate : "+startDate+"; endDate : "+endDate+"; company : "+company+", publisherBQId : "+publisherBQId);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				jsonObject = plannerService.runCampaignReport(campaignIds, placementIds, partners, accounts, reportType, startDate, endDate, company, sessionDTO.isSuperAdmin(), publisherBQId, sessionDTO.getUserId());
			}else {
				throw new Exception("session is null");
			}
		} catch(Exception e) {
			log.info("Exception in runCampaignReport : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("campaignReport", new JSONArray());
		}
		return Action.SUCCESS;
    }
	
	public String checkBillingReport() throws Exception {
		try {
			String campaignIds=request.getParameter("campaignIds");
			String partners=request.getParameter("partners");
			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String companyId=request.getParameter("company");
			
			log.info("campaignIds : "+campaignIds+"; partners : "+partners+"; startDate : "+startDate+
							"; endDate : "+endDate+"; companyId : "+companyId+", publisherBQId : "+publisherBQId);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				
				jsonObject = plannerService.billingReport(campaignIds, partners, startDate, endDate, companyId, sessionDTO.isSuperAdmin(), publisherBQId, sessionDTO.getUserId());
				log.info("jsonObject : "+jsonObject);
				if(jsonObject != null && jsonObject.containsKey("billingReport") && ((JSONArray) jsonObject.get("billingReport")).size() > 0) {
					String memcacheKey = campaignIds+partners+startDate+endDate+companyId+publisherBQId;
					String keyPrefix = MemcacheUtil.BILLING_REPORT_KEY;
					memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
					MemcacheUtil.setObjectInCache(memcacheKey, jsonObject.toString(), 20);
					log.info("Memcache created successfully, Key : "+memcacheKey);
					jsonObject = new JSONObject();
					jsonObject.put("success", true);
				}else {
					throw new Exception("No records for selected filters");
				}
			}else {
				throw new Exception("Session Expired");
			}
		} catch(Exception e) {
			log.info("Exception in checkBillingReport : "+e.getMessage());
			e.printStackTrace();
			jsonObject = new JSONObject();
			jsonObject.put("error", e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	public void billingReport() throws Exception {
		try {
			String campaignIds=request.getParameter("campaignIds");
			String partners=request.getParameter("partners");
			String startDate=request.getParameter("startDate");
			String endDate=request.getParameter("endDate");
			String companyId=request.getParameter("company");
			
			log.info("campaignIds : "+campaignIds+"; partners : "+partners+"; startDate : "+startDate+
							"; endDate : "+endDate+"; companyId : "+companyId+", publisherBQId : "+publisherBQId);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String memcacheKey = campaignIds+partners+startDate+endDate+companyId+publisherBQId;
				String keyPrefix = MemcacheUtil.BILLING_REPORT_KEY;
				memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
				String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
				ISmartCampaignPlannerService plannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
				if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
					jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
				}
				if(jsonObject != null && jsonObject.containsKey("billingReport") && ((JSONArray) jsonObject.get("billingReport")).size() > 0) {
					log.info("Found in Memcache : "+memcacheKey);
				}else {
					log.info("Not in Memcache : "+memcacheKey);
					jsonObject = plannerService.billingReport(campaignIds, partners, startDate, endDate, companyId, sessionDTO.isSuperAdmin(), publisherBQId, sessionDTO.getUserId());
				}
				log.info("jsonObject : "+jsonObject);
				if(jsonObject != null && jsonObject.containsKey("billingReport") && ((JSONArray) jsonObject.get("billingReport")).size() > 0) {
					Workbook wb = plannerService.makeBillingExcelReport(jsonObject);
					if(wb != null) {
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
				  	  	wb.write(bao);
				    	response.setHeader("Content-Disposition","attachment; filename=BillingReport_"+startDate+"_"+endDate+".xls");
				    	response.setHeader("contentType","application/vnd.ms-excel");
				    	response.getOutputStream().write(bao.toByteArray());
					}else {
						log.info("Workbook creation failed, Null");
						throw new Exception("Error in creating report");
					}
				}else {
					throw new Exception("No records for selected filters");
				}
			}else {
				throw new Exception("Session Expired");
			}
		} catch(Exception e) {
			log.info("Exception in billingReport : "+e.getMessage());
			e.printStackTrace();
			Workbook wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet(e.getMessage());
			Row row = sheet.createRow(2);
			Cell cell = row.createCell(2);cell.setCellValue(e.getMessage());
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
	  	  	wb.write(bao);
	    	response.setHeader("Content-Disposition","attachment; filename="+e.getMessage()+".xls");
	    	response.setHeader("contentType","application/vnd.ms-excel");
	    	response.getOutputStream().write(bao.toByteArray());
		}
		//return Action.SUCCESS;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getPublisherBQId() {
		return publisherBQId;
	}

	public void setPublisherBQId(String publisherBQId) {
		this.publisherBQId = publisherBQId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	
	
}