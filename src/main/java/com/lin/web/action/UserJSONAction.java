package com.lin.web.action;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.DFPAdvertisersObj;
import com.lin.server.bean.DFPAgencyObj;
import com.lin.server.bean.OrdersObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AccountDTO;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EmailUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;

public class UserJSONAction implements ServletRequestAware, SessionAware{ 

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final Logger log = Logger.getLogger(UserJSONAction.class.getName());
	private HttpServletRequest request;
	private Map session;
	private SessionObjectDTO sessionDTO;

	private List<CommonDTO> publisherDropDownList;
	private List<CommonDTO> propertiesDropDownList;
	private List<CommonDTO> nonAdminTeamList;
	private List<CommonDTO> rolesList;
	private List<CommonDTO> adServerForPublisherList;
	private List<DFPAgencyObj> dfpAgenciesDropDownList;
	private List<DFPAdvertisersObj> dFPAdvertisersDropDownList;
	private List<OrdersObj> ordersDropDownList;
	private List<LineItemDTO> accountList;
	private List<LineItemDTO> agencyList;
	private List<AccountDTO> dfpAccountList;
	private List<LineItemDTO> advertiserList;
	private String publisherIdsForBigQuery;
	private List<LineItemDTO> siteList;
	private List<CommonDTO> appViewsList;
	private List<CommonDTO> teamDataList;
	private List<AdServerCredentialsDTO> serverCredentialsList;
	
	public JSONObject jsonObject;

	private InputStream inputStream;

	public void setSession(Map<String, Object> session) {
		this.session = session;
		}

	public Map<String, Object> getSession() {
		return session;
	}
	
	public String resendAuthorizeEmail() {
		log.info("inside resendAuthorizeEmail of UserAction");
		try {
			String userId = request.getParameter("userId");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			if(userId != null && userId.length() > 0) {
				jsonObject = service.resendAuthorizeEmail(userId);
				return Action.SUCCESS;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.severe("Exception in resendAuthorizeEmail of UserJSONAction : "+e.getMessage());
		}
		return Action.ERROR;
	}
	
	public String deleteUser() {
		log.info("inside deleteUser of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null && sessionDTO.isSuperAdmin()) {
				String userIdToDelete = request.getParameter("userIdToDelete");
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				if(userIdToDelete != null && userIdToDelete.length() > 0 && LinMobileUtil.isNumeric(userIdToDelete)) {
					if(service.deleteUser(StringUtil.getLongValue(userIdToDelete), sessionDTO.getUserId())) {
						return Action.SUCCESS;
					}
				}
			}else {
				log.warning("Either session is null or user is not a Super Admin");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.severe("Exception in deleteUser of UserJSONAction : "+e.getMessage());
		}
		return Action.ERROR;
	}

	public String emailIdAvailable() {
		log.info("Inside emailIdAvailable in UserJSONAction");
    	try {
	    	String emailId=request.getParameter("checkVal");
	    	String entityId = request.getParameter("id");
	    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	UserDetailsObj userDetailsObj = userDetailsDAO.getUserByEmailId(emailId.trim());
	    	if(userDetailsObj == null || (entityId != null && entityId.trim().length() > 0 && userDetailsObj.getId() > 0 && (userDetailsObj.getId()+"").equals(entityId.trim()))) {
	    		return Action.SUCCESS;
	    	}
	    	else {
	    		return Action.ERROR;
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in emailIdAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
	}

	public String roleNameAvailable() {
		log.info("Inside roleNameAvailable in UserJSONAction");
    	try {
	    	String roleName=request.getParameter("checkVal");
	    	String entityId = request.getParameter("id");
	    	if(roleName != null) {
		    	roleName = roleName.trim();
		    	if(roleName.toLowerCase().replaceAll(" ", "").contains(LinMobileConstants.ADMINS[0].toLowerCase().replaceAll(" ", ""))) {
	    			return Action.ERROR;
	    		}
		    	for(int i=0; i<LinMobileConstants.USERS_ARRAY.length; i++) {
		    		if(roleName.toLowerCase().replaceAll(" ", "").contains(LinMobileConstants.USERS_ARRAY[i].toLowerCase().replaceAll(" ", ""))) {
		    			return Action.ERROR;
		    		}
		    	}

		    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		    	List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		    	RolesAndAuthorisation rolesAndAuthorisation = userDetailsDAO.getRoleByRoleName(roleName, rolesAndAuthorisationList);
		    	if(rolesAndAuthorisation == null || (entityId != null && entityId.trim().length() > 0 && rolesAndAuthorisation.getId() > 0 && (rolesAndAuthorisation.getId()+"").equals(entityId.trim()))) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in roleNameAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String teamNameAvailable() {
		log.info("Inside teamNameAvailable in UserJSONAction");
    	try {
	    	String teamName=request.getParameter("checkVal");
	    	String entityId = request.getParameter("id");
	    	if(teamName != null && teamName.trim().length() > 0) {
		    	teamName = teamName.trim();
		    	if(teamName.toLowerCase().replaceAll(" ", "").contains(LinMobileConstants.TEAM_ALL_ENTITIE.toLowerCase().replaceAll(" ", "")) || teamName.toLowerCase().replaceAll(" ", "").contains(LinMobileConstants.TEAM_NO_ENTITIE.toLowerCase().replaceAll(" ", ""))) {
	    			return Action.ERROR;
	    		}

		    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		    	List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
		    	TeamPropertiesObj teamPropertiesObj = userDetailsDAO.getTeamNameAvailable(teamName, teamPropertiesObjList);
		    	if(teamPropertiesObj == null || (entityId != null && entityId.trim().length() > 0 && teamPropertiesObj.getId() != null && teamPropertiesObj.getId().equals(entityId.trim()))) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in teamNameAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String companyNameAvailable() {
		log.info("Inside companyNameAvailable in UserJSONAction");
    	try {
	    	String companyName=request.getParameter("checkVal");
	    	String entityId = request.getParameter("id");
	    	if(companyName != null && companyName.trim().length() > 0) {
	    		companyName = companyName.trim();
		    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		    	List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		    	CompanyObj companyObj = userDetailsDAO.getCompanyByName(companyName.trim(), companyObjList);
		    	if(companyObj == null || (entityId != null && entityId.trim().length() > 0 && companyObj.getId() > 0 && (companyObj.getId()+"").equals(entityId.trim()))) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in companyNameAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String propertyNameAvailable() {
		log.info("Inside propertyNameAvailable in UserJSONAction");
    	try {
    		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	String propertyName = request.getParameter("name");
	    	String entityId = request.getParameter("id");
	    	String companyId = request.getParameter("companyId");
	    	if(propertyName != null && propertyName.trim().length() > 0) {
		    	propertyName = propertyName.trim();
		    	if(propertyName.toLowerCase().replaceAll(" ", "").contains(LinMobileConstants.ALL_PROPERTIES.toLowerCase().replaceAll(" ", ""))) {
	    			return Action.ERROR;
	    		}
		    	if(companyId != null && companyId.trim().length() > 0) {
		    		PropertyObj propertyObj = userDetailsDAO.propertyNameAvailable(propertyName, MemcacheUtil.getAllPropertiesList(companyId.trim()));
			    	if(propertyObj == null || (entityId != null && entityId.trim().length() > 0 && propertyObj.getId() != null && propertyObj.getId().equals(entityId))) {
			    		return Action.SUCCESS;
			    	}
			    	else {
			    		return Action.ERROR;
			    	}
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in propertyNameAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String dfpPropertyAvailable() {
		log.info("Inside dfpPropertyAvailable in UserJSONAction");
    	try {
	    	String dfpPropertyId = request.getParameter("dfpId");
	    	String companyId = request.getParameter("companyId");
	    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	if(dfpPropertyId != null && dfpPropertyId.trim().length() > 0 && companyId != null && companyId.trim().length() > 0) {
		    	PropertyObj propertyObj = userDetailsDAO.getPropertyObjByDFPPropertyId(dfpPropertyId.trim(), MemcacheUtil.getAllPropertiesList(companyId.trim()));
		    	if(propertyObj == null) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in dfpPropertyAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String accountNameAvailable() {
		log.info("Inside accountNameAvailable in UserJSONAction");
    	try {
	    	String accountName = request.getParameter("name");
	    	String entityId = request.getParameter("id");
	    	String companyId = request.getParameter("companyId");
	    	String accountType = request.getParameter("accountType");
	    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	if(accountName != null && accountName.trim().length() > 0 && companyId != null && companyId.trim().length() > 0 && accountType != null && accountType.trim().length() > 0) {
		    	AccountsEntity accountsObj = userDetailsDAO.getAccountsObjByAccountName(accountName.trim() + " ("+accountType.trim()+")", MemcacheUtil.getAllAccountsObjList(companyId.trim()));
		    	if(accountsObj == null || (entityId != null && entityId.trim().length() > 0 && accountsObj.getId() != null && accountsObj.getId().equals(entityId))) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in dfpAccountAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

	public String dfpAccountAvailable() {
		log.info("Inside dfpAccountAvailable in UserJSONAction");
    	try {
	    	String dfpAccountId = request.getParameter("dfpId");
	    	String companyId = request.getParameter("companyId");
	    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
	    	if(dfpAccountId != null && dfpAccountId.trim().length() > 0 && companyId != null && companyId.trim().length() > 0) {
		    	AccountsEntity accountsObj = userDetailsDAO.getAccountsObjByDfpAccountId(dfpAccountId.trim(), MemcacheUtil.getAllAccountsObjList(companyId.trim()));
		    	if(accountsObj == null) {
		    		return Action.SUCCESS;
		    	}
		    	else {
		    		return Action.ERROR;
		    	}
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in dfpAccountAvailable in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
    	return Action.ERROR;
	}

   public String loadPublisherPartnerDataForTeam(){
		log.info("Inside loadPublisherPartnerDataForTeam in UserJSONAction");
		try {
			String publisherCompanyId=request.getParameter("companyId");
			IUserDetailsDAO dao = new UserDetailsDAO();

			if(publisherCompanyId != null && !publisherCompanyId.equalsIgnoreCase("")) {
				publisherCompanyId = publisherCompanyId.trim();
				propertiesDropDownList = dao.getActivePropertiesByPublisherCompanyId(publisherCompanyId);
				//propertiesDropDownList.add(0, new CommonDTO(LinMobileConstants.ALL_PROPERTIES, LinMobileConstants.ALL_PROPERTIES));
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadPublisherPartnerDataForTeam in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
   }
   
   /*public String loadPublisherIdsForBigQueryByCompanyId(){
		log.info("Inside loadPublisherIdsForBigQueryByCompanyId in UserJSONAction");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			String publisherCompanyId=request.getParameter("companyId");
			if(publisherCompanyId != null && !publisherCompanyId.equalsIgnoreCase("")) {
				publisherCompanyId = publisherCompanyId.trim();
				publisherIdsForBigQuery = service.getPublisherIdsForBigQueryByCompanyId(publisherCompanyId);
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadPublisherIdsForBigQueryByCompanyId in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
  }*/

	public String loadAccountsForTeam(){
		log.info("Inside loadAccountsForTeam in UserJSONAction");
		try {
			String accountIdsNotIn=request.getParameter("accountIdsNotIn");
			String accountPrefix=request.getParameter("term");
			String publisherIdsForBigQuery=request.getParameter("publisherIdsForBigQuery");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			if(accountPrefix != null && !accountPrefix.trim().equals("") && publisherIdsForBigQuery != null && !publisherIdsForBigQuery.trim().equals("")) {
				if(accountIdsNotIn == null) {
					accountIdsNotIn = "";
				}
				accountList = service.loadAccountsForTeam(publisherIdsForBigQuery.trim(), accountPrefix.trim(), accountIdsNotIn.trim());
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadAccountsForTeam in UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		if(accountList == null) {
			accountList = new ArrayList<LineItemDTO>();
		}
		return Action.SUCCESS;
   }

	public String loadSelectedAgenciesFromBigQuery(){
		log.info("Inside loadSelectedAgenciesFromBigQuery in UserJSONAction");
		try {
			String selectedAgencies=request.getParameter("selectedAgencies");
			IUserDetailsDAO dao = new UserDetailsDAO();
			if(selectedAgencies != null && !selectedAgencies.trim().equals("")) {
				agencyList = dao.loadSelectedAgenciesFromBigQuery(selectedAgencies.trim());
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadSelectedAgenciesFromBigQuery in UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
   }

	public String loadSelectedAccountsFromBigQuery(){
		log.info("Inside loadSelectedAccountsFromBigQuery in UserJSONAction");
		try {
			String selectedAdvertisers=request.getParameter("selectedAdvertisers");
			String selectedAgencies=request.getParameter("selectedAgencies");
			IUserDetailsDAO dao = new UserDetailsDAO();
			accountList = new ArrayList<LineItemDTO>();
			if(selectedAdvertisers != null && !selectedAdvertisers.trim().equals("")) {
				advertiserList = dao.loadSelectedAdvertisersFromBigQuery(selectedAdvertisers.trim());
				accountList.addAll(advertiserList);
			}
			if(selectedAgencies != null && !selectedAgencies.trim().equals("")) {
				agencyList = dao.loadSelectedAgenciesFromBigQuery(selectedAgencies.trim());
				accountList.addAll(agencyList);
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadSelectedAccountsFromBigQuery in UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
   }

	public String fetchTeamsByCompanyId() {
		log.info("inside fetchTeamsByCompanyId of UserJSONAction");
		try {
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			String companyId=request.getParameter("companyId");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);

			if(companyId != null && !companyId.trim().equals("") && !companyId.trim().equals("-1")) {
				companyId = companyId.trim();
				service.fetchTeamsByCompanyId(companyId, userDetailsDTO);
				nonAdminTeamList = userDetailsDTO.getNonAdminTeamList();
			}
		}
		catch (Exception e) {
			log.severe("Exception in fetchTeamsByCompanyId in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	public String fetchActiveRolesByCompanyId() {
		log.info("inside fetchActiveRolesByCompanyId of UserJSONAction");
		try {
			String companyId=request.getParameter("companyId");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);

			if(companyId != null && !companyId.trim().equals("") && !companyId.trim().equals("-1")) {
				companyId = companyId.trim();
				rolesList = service.fetchActiveRolesByCompanyId(companyId, sessionDTO.isSuperAdmin());
			}
		}
		catch (Exception e) {
			log.severe("Exception in fetchActiveRolesByCompanyId in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	/*public String loadSitesForProperty(){
		try {
			//String siteIdNotIn=request.getParameter("siteIdNotIn");
			String siteNameNotIn=request.getParameter("siteNameNotIn");
			String sitePrefix=request.getParameter("term");
			String publisherIdsForBigQuery=request.getParameter("publisherIdsForBigQuery");
			IUserDetailsDAO dao = new UserDetailsDAO();
			if(sitePrefix != null && !sitePrefix.trim().equals("") && publisherIdsForBigQuery != null && !publisherIdsForBigQuery.trim().equals("")) {
				if(siteNameNotIn == null) {
					siteNameNotIn = "";
				}
				siteList = dao.loadSitesFromBigQuery(publisherIdsForBigQuery.trim(), sitePrefix.trim(), siteNameNotIn.trim());
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadSitesForProperty in UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		if(siteList == null) {
			siteList = new ArrayList<LineItemDTO>();
		}
		return Action.SUCCESS;
   }*/

	public String loadSitesForProperty(){
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO dao = new  UserDetailsDAO();
		try {
			siteList = new ArrayList<LineItemDTO>();
			String prefixStr = request.getParameter("term");
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			String password = "";
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(prefixStr != null && prefixStr.trim().length() > 0 && adServerId != null && adServerId.trim().length() > 0 && adServerUsername != null && adServerUsername.trim().length() > 0) {
				prefixStr = prefixStr.trim().toLowerCase();
				adServerId = adServerId.trim();
				List<LineItemDTO> propertyList = MemcacheUtil.getDFPPropertiesFromCache(adServerId + adServerUsername);
				if(propertyList != null && propertyList.size() > 0) {
					log.info("Memcache exists, size : "+propertyList.size());
					for (LineItemDTO lineItemDTO : propertyList) {
						if(lineItemDTO.getSiteName().toLowerCase().contains(prefixStr)) {
							siteList.add(lineItemDTO);
						}
						if(siteList.size() >= 20) {
							break;
						}
					}
					log.info("siteList size : "+siteList.size());
				}
				else {
					log.info("Memcache does not exists........... searching from DFP");
					Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
					if(map != null && map.size() > 0 && map.containsKey(adServerId)) {
						AdServerCredentialsDTO adServerCredentialsDTO = map.get(adServerId);
						password = adServerCredentialsDTO.getAdServerPassword();
						adServerUsername = adServerCredentialsDTO.getAdServerUsername();
						
						dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();

						DFPReportService dfpReportService = new DFPReportService();
						//siteList = dfpReportService.searchProperties(dfpServices, dfpSession, prefixStr.trim());
						dfpReportService.getTopLevelAdUnitsFromDFP(dfpServices, dfpSession, adServerId, adServerUsername, prefixStr.trim(), siteList, new StringBuilder());
					}
					else {
						log.severe("No credentials for networkCode : "+adServerId);
					}
				}
			}
		}catch (Exception e) {
			log.severe("Exception in loadSitesForProperty of UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
   }

	public String loadTeamDataByCompany(){
		log.info("Inside loadTeamDataByCompany in UserJSONAction");
		try {
			String companyId=request.getParameter("companyId");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);

			if(companyId != null && !companyId.equalsIgnoreCase("")) {
				companyId = companyId.trim();
				teamDataList = service.getTeamDataByCompanyId(companyId);
			}
			else{
				log.info("Company id is either null or empty string");
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadTeamDataByCompany in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
   }

	 public String loadAdserverCredentialsByCompanyId(){
			log.info("Inside loadAdserverCredentialsByCompanyId in UserJSONAction");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			IUserDetailsDAO dao = new UserDetailsDAO();
			List<CompanyObj> companyObjList = new ArrayList<CompanyObj>();
			try {
				String companyId = request.getParameter("companyId");
				if(companyId != null && !companyId.equalsIgnoreCase("")) {
					CompanyObj companyObj = dao.getCompanyById(Long.valueOf(companyId.trim()), MemcacheUtil.getAllCompanyList());
					if(companyObj != null) {
						companyObjList.add(companyObj);
						serverCredentialsList = service.loadAdserverCredentialsForDropDown(companyObjList);
					}
				}
			}
			catch (Exception e) {
				log.severe("Exception in loadAdserverCredentialsByCompanyId in UserJSONAction"+e.getMessage());
				e.printStackTrace();
			}
			return Action.SUCCESS;
	  }

	 public String searchAccounts() {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO dao = new  UserDetailsDAO();
		List<AccountDTO> accountDTOList = new ArrayList<AccountDTO>();
		try {
			dfpAccountList = new ArrayList<AccountDTO>();
			String prefixStr = request.getParameter("term");
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			String password = "";
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			log.info("adServerId : "+adServerId+", adServerUsername : "+adServerUsername);
			if(prefixStr != null && prefixStr.trim().length() > 0 && adServerId != null && adServerId.trim().length() > 0 && adServerUsername != null && adServerUsername.trim().length() > 0) {
				adServerId = adServerId.trim();
				prefixStr = prefixStr.trim().toLowerCase();
				accountDTOList = MemcacheUtil.getDFPAccountsFromCache(adServerId + adServerUsername);
				if(accountDTOList != null && accountDTOList.size() > 0) {
					log.info("Memcache exists, size : "+accountDTOList.size());
					for (AccountDTO accountDTO : accountDTOList) {
						if(accountDTO.getName().toLowerCase().contains(prefixStr)) {
							dfpAccountList.add(accountDTO);
						}
						if(dfpAccountList.size() >= 20) {
							break;
						}
					}
					log.info("dfpAccountList size : "+dfpAccountList.size());
				}
				else {
					log.info("Memcache does not exists........... searching from DFP");
					Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
					if(map != null && map.size() > 0 && map.containsKey(adServerId)) {
						AdServerCredentialsDTO adServerCredentialsDTO = map.get(adServerId);
						password = adServerCredentialsDTO.getAdServerPassword();
						adServerUsername = adServerCredentialsDTO.getAdServerUsername();

						dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();

						DFPReportService dfpReportService = new DFPReportService();
						dfpReportService.getAccountsFromDFP(dfpServices, dfpSession, adServerId, adServerUsername, prefixStr.trim(), dfpAccountList, new StringBuilder());
					}
					else {
						log.severe("No credentials for networkCode : "+adServerId);
					}
				}
			}
		}catch (Exception e) {
			log.severe("Exception in searchAccounts of UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	 public String loadAccountsFromDFP() {
		log.info("loadAccountsFromDFP started at : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		StringBuilder summary = new StringBuilder();
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			String adServerId = "";
			String username = "";
			String password = "";
			boolean parameterisedCall = false;
			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			for(String key : map.keySet()) {
				if(networkId != null && networkUsername != null && key.equals(networkId.trim())) {
					parameterisedCall = true;
				}
				else if(networkId != null && networkUsername != null && !(key.equals(networkId.trim()))) {
					continue;
				}

				log.info("new key, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
				if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null && adServerCredentialsDTO.getAdServerPassword() != null) {
					adServerId = adServerCredentialsDTO.getAdServerId();
					username = adServerCredentialsDTO.getAdServerUsername();
					password = adServerCredentialsDTO.getAdServerPassword();
					summary.append("For ADSERVER_ID : "+adServerId+ ", AD_SERVER_USERNAME : "+username+"<br>");

					log.info(" now going to build dfpSession ...");
					dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
					log.info(" getting DfpServices instance from properties...");
					dfpServices = LinMobileProperties.getInstance().getDfpServices();

					log.info("Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					DFPReportService dfpReportService = new DFPReportService();
					int i=1;
					while(i<3) {
						summary.append("Iteration "+i+"<br>");
						boolean result = dfpReportService.getAccountsFromDFP(dfpServices, dfpSession, adServerId, username, "", new ArrayList<AccountDTO>(), summary);
						if(result) {
							String successMessage = "DFP Accounts cached successfully";
							log.info(successMessage);
							summary.append(successMessage+"<br>");
							break;
						}
						else {
							if(i==2) {
								String errorMessage = "DFP Accounts caching failed";
								log.severe(errorMessage);
								summary.append(errorMessage+"<br>");
							}
						}
						i++;
					}
				}
				log.info("end of IF, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
				summary.append("<br><br><br>");

				if(parameterisedCall) {
					break;
				}
			}
			// send mail
			//EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "DFP Accounts caching Status - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in DFP Accounts caching : "+e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage+"<br>");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP Accounts caching - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return Action.SUCCESS;
	}

	public String updateAccountsFromDFP() {
		String dateDiffMessageString = "";
		StringBuilder summary = new StringBuilder();
		try {
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			Date startDate = null;
			Date endDate = null;
			String startDateString = request.getParameter("startDate");
			String endDateString = request.getParameter("endDate");
			if(startDateString != null && !startDateString.trim().equals("") && endDateString != null && !endDateString.trim().equals("")) {
				startDateString = startDateString.trim();
				endDateString = endDateString.trim();
				startDate = DateUtil.getFormatedDate(startDateString, "yyyy-MM-dd'T'HH:mm:ss");
				endDate = DateUtil.getFormatedDate(endDateString, "yyyy-MM-dd'T'HH:mm:ss");
				if(startDate != null && endDate != null) {
					dateDiffMessageString = " For startDate = " + DATE_TIME_FORMAT.format(startDate)+" and endDate = " + DATE_TIME_FORMAT.format(endDate) + " ";
					log.info("dateDiffMessageString : "+dateDiffMessageString);
					summary.append(dateDiffMessageString+"<br><br>");
				}
			}
			String adServerId = "";
			String username = "";
			String password = "";
			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			for(String key : map.keySet()) {
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
				adServerId = adServerCredentialsDTO.getAdServerId();
				username = adServerCredentialsDTO.getAdServerUsername();
				password = adServerCredentialsDTO.getAdServerPassword();
				summary.append("For ADSERVER_ID : "+adServerId+ ", AD_SERVER_USERNAME : "+username+"<br>");

				log.info(" now going to build dfpSession ...");
				dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
				log.info(" getting DfpServices instance from properties...");
				dfpServices = LinMobileProperties.getInstance().getDfpServices();

				DFPReportService dfpReportService = new DFPReportService();
				int i=1;
				while(i<3) {
					summary.append("Iteration "+i+"<br>");
					boolean result = dfpReportService.updateAccounts(dfpServices, dfpSession, adServerId, username, startDate, endDate, summary);
					if(result) {
						String successMessage = "Accounts updated successfully";
						log.info(successMessage);
						summary.append(successMessage+"<br>");
						break;
					}
					else {
						log.info("updateAccountsFromDFP failed for iteration : "+ i +" result : false");
						if(i==2) {
							String errorMessage = "Accounts update failed";
							log.severe(errorMessage);
							summary.append(errorMessage+"<br>");
						}
					}
					i++;
				}
				summary.append("<br><br><br>");
			}
			// send mail
			//EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Accounts update Status - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in Accounts update : "+e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage+"<br>");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in Accounts update - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return Action.SUCCESS;
	}

	public String refreshDFPAccountMemcache() {
		log.info("inside refreshDFPAccountMemcache of UserJSONAction");
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			if(networkId != null && !networkId.trim().equals("") && networkUsername != null && !networkUsername.trim().equals("")) {
				String backendUrl = "";
				backendUrl = LinMobileVariables.REDIRECT_URL;
				if(backendUrl.startsWith("http://")) {
					backendUrl = backendUrl.replace("http://", "http://"+LinMobileVariables.BACKEND_NAME+".");
				}
				else {
					backendUrl = LinMobileVariables.BACKEND_NAME+"." + backendUrl;
				}
				//backendUrl= "http://localhost:8888";
				backendUrl = backendUrl + "/loadAccountsFromDFP.lin?networkId="+networkId+"&networkUsername="+networkUsername;
				backendUrl = backendUrl.replace(" ", "%20");
				try {
					URL url = new URL(backendUrl);
				    URLConnection urlConnection = url.openConnection();
				    urlConnection.getInputStream();
				}
				catch (SocketTimeoutException ste) { }

				return Action.SUCCESS;
			}
		}
		catch (Exception e) {
			log.severe("Exception in refreshDFPAccountMemcache in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.ERROR;
	}

	public String checkDFPAccountMemcache() {
		log.info("inside checkDFPAccountMemcache of UserJSONAction");
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			if(networkId != null && !networkId.trim().equals("") && networkUsername != null && !networkUsername.trim().equals("")) {
				List<AccountDTO> accountDTOList = MemcacheUtil.getDFPAccountsFromCache(networkId.trim() + networkUsername.trim());
				if(accountDTOList == null|| accountDTOList.size() == 0) {
					log.info("No or empty memcache, "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					return Action.ERROR;
				}
				else {
					log.info("Memcache found of size : "+accountDTOList.size());
					return Action.SUCCESS;
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in checkDFPAccountMemcache in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.ERROR;
	}

	public String loadPropertiesFromDFP() {
		log.info("loadPropertiesFromDFP started at : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		StringBuilder summary = new StringBuilder();
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			String adServerId = "";
			String username = "";
			String password = "";
			boolean parameterisedCall = false;
			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			for(String key : map.keySet()) {
				if(networkId != null && networkUsername != null && key.equals(networkId.trim())) {
					parameterisedCall = true;
				}
				else if(networkId != null && networkUsername != null && !(key.equals(networkId.trim()))) {
					continue;
				}

				log.info("new key, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
				if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerId().equals("4206")) {
					// 4206 does not have any property
					continue;
				}
				if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null && adServerCredentialsDTO.getAdServerPassword() != null) {
					adServerId = adServerCredentialsDTO.getAdServerId();
					username = adServerCredentialsDTO.getAdServerUsername();
					password = adServerCredentialsDTO.getAdServerPassword();
					summary.append("For ADSERVER_ID : "+adServerId+ ", AD_SERVER_USERNAME : "+username+"<br>");

					log.info(" now going to build dfpSession ...");
					dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
					log.info(" getting DfpServices instance from properties...");
					dfpServices = LinMobileProperties.getInstance().getDfpServices();

					log.info("Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					DFPReportService dfpReportService = new DFPReportService();
					int i=1;
					while(i<3) {
						summary.append("Iteration "+i+"<br>");
						boolean result = dfpReportService.getTopLevelAdUnitsFromDFP(dfpServices, dfpSession, adServerId, username, "", new ArrayList<LineItemDTO>(), summary);
						if(result) {
							String successMessage = "DFP Properties cached successfully";
							log.info(successMessage);
							summary.append(successMessage+"<br>");
							break;
						}
						else {
							if(i==2) {
								String errorMessage = "DFP Properties caching failed";
								log.severe(errorMessage);
								summary.append(errorMessage+"<br>");
							}
						}
						i++;
					}
				}
				log.info("end of IF, Time : "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
				summary.append("<br><br><br>");

				if(parameterisedCall) {
					break;
				}
			}
			// send mail
			//EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "DFP Properties caching Status - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in DFP Properties caching : "+e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage+"<br>");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP Properties caching - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return Action.SUCCESS;
	}

	public String refreshDFPPropertyMemcache() {
		log.info("inside refreshDFPPropertyMemcache of UserJSONAction");
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			if(networkId != null && !networkId.trim().equals("") && networkUsername != null && !networkUsername.trim().equals("")) {
				String backendUrl = LinMobileVariables.REDIRECT_URL;
				if(backendUrl.startsWith("http://")) {
					backendUrl = backendUrl.replace("http://", "http://"+LinMobileVariables.BACKEND_NAME+".");
				}
				else {
					backendUrl = LinMobileVariables.BACKEND_NAME+"." + backendUrl;
				}
				//backendUrl = "http://localhost:8888";
				backendUrl = backendUrl + "/loadPropertiesFromDFP.lin?networkId="+networkId+"&networkUsername="+networkUsername;
				backendUrl = backendUrl.replace(" ", "%20");
				try {
					URL url = new URL(backendUrl);
				    URLConnection urlConnection = url.openConnection();
				    urlConnection.getInputStream();
				}
				catch (SocketTimeoutException ste) { }

				return Action.SUCCESS;
			}
		}
		catch (Exception e) {
			log.severe("Exception in refreshDFPPropertyMemcache in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.ERROR;
	}

	public String checkDFPPropertyMemcache() {
		log.info("inside checkDFPPropertyMemcache of UserJSONAction");
		try {
			String networkId=request.getParameter("networkId");
			String networkUsername=request.getParameter("networkUsername");

			if(networkId != null && !networkId.trim().equals("") && networkUsername != null && !networkUsername.trim().equals("")) {
				List<LineItemDTO> propertiesList = MemcacheUtil.getDFPPropertiesFromCache(networkId.trim() + networkUsername.trim());
				if(propertiesList == null|| propertiesList.size() == 0) {
					log.info("No or empty memcache, "+DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS"));
					return Action.ERROR;
				}
				else {
					log.info("Memcache found of size : "+propertiesList.size());
					return Action.SUCCESS;
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in checkDFPPropertyMemcache in UserJSONAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.ERROR;
	}

	 public String loadAdUnitChilds() {
		IUserDetailsDAO dao = new  UserDetailsDAO();
		try {
			String adServerId = request.getParameter("adServerId");
			String username = request.getParameter("adServerUsername");
			String adUnitId = request.getParameter("adUnitId");
			String password = "";
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(adUnitId != null && adUnitId.trim().length() > 0 && adServerId != null && adServerId.trim().length() > 0 && username != null && username.trim().length() > 0) {
				adServerId = adServerId.trim();
				Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
				if(map != null && map.size() > 0 && map.containsKey(adServerId)) {
					AdServerCredentialsDTO adServerCredentialsDTO = map.get(adServerId);
					password = adServerCredentialsDTO.getAdServerPassword();
					username = adServerCredentialsDTO.getAdServerUsername();

					dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
					log.info(" getting DfpServices instance from properties...");
					dfpServices = LinMobileProperties.getInstance().getDfpServices();

					DFPReportService dfpReportService = new DFPReportService();
					siteList = dfpReportService.getAdUnitChilds(dfpServices, dfpSession, adUnitId.trim(), "");
				}
				else {
					log.severe("No credentials for network code : "+adServerId);
				}
			}
		}catch (Exception e) {
			log.severe("Exception in loadSitesForProperty of UserJSONAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

public void setPublisherDropDownList(List<CommonDTO> publisherDropDownList) {
	this.publisherDropDownList = publisherDropDownList;
}

public List<CommonDTO> getPublisherDropDownList() {
	return publisherDropDownList;
}

public void setPropertiesDropDownList(List<CommonDTO> propertiesDropDownList) {
	this.propertiesDropDownList = propertiesDropDownList;
}

public List<CommonDTO> getPropertiesDropDownList() {
	return propertiesDropDownList;
}

public void setNonAdminTeamList(List<CommonDTO> nonAdminTeamList) {
	this.nonAdminTeamList = nonAdminTeamList;
}

public List<CommonDTO> getNonAdminTeamList() {
	return nonAdminTeamList;
}

public void setdFPAdvertisersDropDownList(List<DFPAdvertisersObj> dFPAdvertisersDropDownList) {
	this.dFPAdvertisersDropDownList = dFPAdvertisersDropDownList;
}

public List<DFPAdvertisersObj> getdFPAdvertisersDropDownList() {
	return dFPAdvertisersDropDownList;
}

public void setOrdersDropDownList(List<OrdersObj> ordersDropDownList) {
	this.ordersDropDownList = ordersDropDownList;
}

public List<OrdersObj> getOrdersDropDownList() {
	return ordersDropDownList;
}

public void setAdServerForPublisherList(List<CommonDTO> adServerForPublisherList) {
	this.adServerForPublisherList = adServerForPublisherList;
}

public List<CommonDTO> getAdServerForPublisherList() {
	return adServerForPublisherList;
}

public void setDfpAgenciesDropDownList(List<DFPAgencyObj> dfpAgenciesDropDownList) {
	this.dfpAgenciesDropDownList = dfpAgenciesDropDownList;
}

public List<DFPAgencyObj> getDfpAgenciesDropDownList() {
	return dfpAgenciesDropDownList;
}

public void setAgencyList(List<LineItemDTO> agencyList) {
	this.agencyList = agencyList;
}

public List<LineItemDTO> getAgencyList() {
	return agencyList;
}

public void setAdvertiserList(List<LineItemDTO> advertiserList) {
	this.advertiserList = advertiserList;
}

public List<LineItemDTO> getAdvertiserList() {
	return advertiserList;
}

public void setPublisherIdsForBigQuery(String publisherIdsForBigQuery) {
	this.publisherIdsForBigQuery = publisherIdsForBigQuery;
}

public String getPublisherIdsForBigQuery() {
	return publisherIdsForBigQuery;
}

public void setAccountList(List<LineItemDTO> accountList) {
	this.accountList = accountList;
}

public List<LineItemDTO> getAccountList() {
	return accountList;
}

public void setRolesList(List<CommonDTO> rolesList) {
	this.rolesList = rolesList;
}

public List<CommonDTO> getRolesList() {
	return rolesList;
}

public void setSiteList(List<LineItemDTO> siteList) {
	this.siteList = siteList;
}

public List<LineItemDTO> getSiteList() {
	return siteList;
}

public void setAppViewsList(List<CommonDTO> appViewsList) {
	this.appViewsList = appViewsList;
}

public List<CommonDTO> getAppViewsList() {
	return appViewsList;
}

public void setTeamDataList(List<CommonDTO> teamDataList) {
	this.teamDataList = teamDataList;
}

public List<CommonDTO> getTeamDataList() {
	return teamDataList;
}

public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
}

public InputStream getInputStream() {
	return inputStream;
}

public void setServerCredentialsList(List<AdServerCredentialsDTO> serverCredentialsList) {
	this.serverCredentialsList = serverCredentialsList;
}

public List<AdServerCredentialsDTO> getServerCredentialsList() {
	return serverCredentialsList;
}

public void setDfpAccountList(List<AccountDTO> dfpAccountList) {
	this.dfpAccountList = dfpAccountList;
}

public List<AccountDTO> getDfpAccountList() {
	return dfpAccountList;
}


}
