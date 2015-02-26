package com.lin.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.SmartCampaignPlannerService;
import com.lin.web.service.impl.UserService;
import com.lin.web.util.CSVReaderUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.MemcacheUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends ActionSupport implements
ModelDriven<UserDetailsDTO>,ServletRequestAware, SessionAware, ServletResponseAware{ 
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	Logger log = Logger.getLogger("UserAction");
	
	private Map session;

	private SessionObjectDTO sessionDTO;
	
	private String linStatus;
	private int updateRoleStatus = -1;
	private int deleteRoleStatus = -1;
	private int updateUserStatus = -1;
	private int deleteUserStatus = -1;
	private int updateTeamStatus = -1;
	private int deleteTeamStatus = -1;
	private int copyPermissionStatus = -1;
	private int updateCompanyStatus = -1;
	private int updatePublisherStatus = -1;
	private int updateDemandPartnerStatus = -1;
	private int updatePropertyStatus = -1;
	private int updateAccountStatus = -1;
	
	public String seperator = "<SEP>";
	
	private String forgetPasswordStatus = "";
	
	private String linCSV;	
	private String linCSVFileName;
	private String entityType;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
		}

	public Map<String, Object> getSession() {
		return session;
	}
	
	public String execute() {
		return "success";
	}
	
	public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[4]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[4]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}
	
	public void fetchAllActiveRoles(long userId, boolean superAdmin) throws Exception {
		List<CommonDTO> list = null;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		list = service.fetchAllActiveRoles(userId, superAdmin);
		userDetailsDTO.setRoleList(list);
	}
	
	public void fetchTeamsByCompanyId(String companyId) throws Exception {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		service.fetchTeamsByCompanyId(companyId, userDetailsDTO);
	}
	
	public void fetchAllStatus() throws Exception {
		List<CommonDTO> list = new ArrayList<CommonDTO>();
		for(int i=0; i<LinMobileConstants.STATUS_ARRAY.length; i++) {
			list.add(new CommonDTO(LinMobileConstants.STATUS_ARRAY[i], LinMobileConstants.STATUS_ARRAY[i]));
		}
		userDetailsDTO.setStatusList(list);
	}
	
	public void fetchAllActiveAuthorisationText() throws Exception {
		List<AuthorisationTextObj> list = null;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		list = service.fetchAllActiveAuthorisationText();
		userDetailsDTO.setAuthorisationTextList(list);
	}
	
	public void fetchAllActiveCompanies() throws Exception {
		List<CompanyObj> list =  new ArrayList<CompanyObj>();
		List<CompanyObj> companyObjList =  MemcacheUtil.getAllCompanyList();
		if(companyObjList != null && companyObjList.size() > 0) {
			for (CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyName() != null && !companyObj.getCompanyName().trim().equalsIgnoreCase("") && companyObj.getStatus() != null && companyObj.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					list.add(companyObj);
				}
			}
		}
		userDetailsDTO.setCompanyList(list);
	}
	
	/*public void fetchCompaniesByUserId(long userId) throws Exception {
		List<CommonDTO> list = new ArrayList<CommonDTO>();
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CompanyObj> selectedCompanyObjList = userDetailsDAO.getSelectedCompaniesByUserId(userId); 
		if(selectedCompanyObjList != null && selectedCompanyObjList.size() > 0) {
			for (CompanyObj companyObj : selectedCompanyObjList) {
				if(companyObj != null && companyObj.getCompanyName() != null && !companyObj.getCompanyName().trim().equalsIgnoreCase("") && companyObj.getStatus() != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					list.add(new CommonDTO(String.valueOf(companyObj.getId()), companyObj.getCompanyName()));
				}
			}
		}
		userDetailsDTO.setCompanyList(list);
	}*/
	
	public void fetchAllActiveDemandPartners(long userId) throws Exception {
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<CommonDTO> list = userDetailsDAO.getSelectedDemandPartnersDropDownByUserId(userId);
		userDetailsDTO.setDemandPartnersList(list);
	}
	
	public void fetchAllDemandPartnerTypes() throws Exception {
		List<CommonDTO> list = new ArrayList<CommonDTO>();
		for(int i=0; i<LinMobileConstants.DEMAND_PARTNER_TYPES.length; i++) {
			list.add(new CommonDTO(LinMobileConstants.DEMAND_PARTNER_TYPES[i], LinMobileConstants.DEMAND_PARTNER_TYPES[i]));
		}
		userDetailsDTO.setDemandPartnerTypeList(list);
	}
	
	public String initCreateUser() {
		log.info("Inside initCreateUser of UserAction");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO dao = new UserDetailsDAO();
		try {
			String companyId = "";
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
				if(userDetailsDTO.getCompanyList() != null && userDetailsDTO.getCompanyList().size() > 0) {
					companyId = userDetailsDTO.getCompanyList().get(0).getId()+"";
				}
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyId = String.valueOf(companyObj.getId());
				}
				else {
					throw new Exception();
				}
			}
			fetchTeamsByCompanyId(companyId);
			userDetailsDTO.setRoleList(service.fetchActiveRolesByCompanyId(companyId, sessionDTO.isSuperAdmin()));
			fetchAllStatus();
			userDetailsDTO.setSelectedRoleType(userDetailsDTO.getRoleList().get(0).getValue().trim());
		}
		catch (Exception e) {
			
			log.severe("Exception in initCreateUser of UserAction" + e.getMessage());
		}
		return "success";
	}
	
	public String createUser() {		
		log.info("inside createUser of UserAction");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			String companyId = "";
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
				if(userDetailsDTO.getCompanyList() != null && userDetailsDTO.getCompanyList().size() > 0) {
					companyId = userDetailsDTO.getCompanyList().get(0).getId()+"";
				}
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyId = String.valueOf(companyObj.getId());
				}
				else {
					throw new Exception();
				}
			}
			fetchTeamsByCompanyId(companyId);
			userDetailsDTO.setRoleList(service.fetchActiveRolesByCompanyId(companyId, sessionDTO.isSuperAdmin()));
			fetchAllStatus();
			
			if(userDetailsDTO.getEmailId() != null && !userDetailsDTO.getEmailId().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getUserName() != null && !userDetailsDTO.getUserName().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat() != null && !userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase(userDetailsDTO.getEmailId().trim()) ){
				
				service.createUser(userDetailsDTO, sessionDTO.getUserId());
				setLinStatus(userDetailsDTO.getReturnStatus());
			}
			else {
				setLinStatus("inappropriate");
			}
			userDetailsDTO.setCompanyId("");
			userDetailsDTO.setRole("");
			userDetailsDTO.setAdminTeam("");
			userDetailsDTO.setNonAdminTeam("");
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in createUser of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String authorizeEmail() {
		log.info("inside authorizeEmail of UserAction");
		try {
			String randomNumber = request.getParameter("authorize");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			long id = service.authorizeEmail(randomNumber);
			if(id > 0) {
				userDetailsDTO.setId(id);
				userDetailsDTO.setRandomNumber(randomNumber);
			}
			else {
				return "loginPage";
			}
		}catch (Exception e) {
			
			log.severe("Exception in authorizeEmail of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String authorizeUser() {
		log.info("inside authorizeUser of UserAction");
		Boolean result = false;
		try {
			if(userDetailsDTO.getId() > 0
					&& userDetailsDTO.getPasswordRepeat() != null && !userDetailsDTO.getPasswordRepeat().trim().equalsIgnoreCase("")
					&& userDetailsDTO.getPassword() != null && !userDetailsDTO.getPassword().trim().equalsIgnoreCase("")
					&& userDetailsDTO.getPassword().trim().equals(userDetailsDTO.getPasswordRepeat().trim())){
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				result = service.authorizeUser(userDetailsDTO);
				if(result) {
					setLinStatus("success");
					response.sendRedirect("/login.lin?id="+userDetailsDTO.getEmailId());
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("inappropriate");
			}
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in authorizeUser of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	
	public String userSetup() {		
		log.info("inside userSetup of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			service.userSetup(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in userSetup of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String initEditUser() {
		log.info("inside initEditUser of UserAction");
		IUserDetailsDAO dao = new UserDetailsDAO();
		String companyId = "";
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			result = service.initEditUser(userDetailsDTO);
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
				if(userDetailsDTO.getCompanyList() != null && userDetailsDTO.getCompanyList().size() > 0 && userDetailsDTO.getSelectedCompanyList() != null && userDetailsDTO.getSelectedCompanyList().size() > 0) {
					companyId = userDetailsDTO.getSelectedCompanyList().get(0).getId();
				}
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyId = String.valueOf(companyObj.getId());
				}
				else {
					throw new Exception();
				}
			}
			fetchTeamsByCompanyId(companyId);
			if(sessionDTO.isSuperAdmin() && sessionDTO.getUserId() == userDetailsDTO.getId()) {			// SuperAdmin cannot inactive itself also cannot change its role
				List<CommonDTO> statusList = new ArrayList<CommonDTO>();
				statusList.add(new CommonDTO(LinMobileConstants.STATUS_ARRAY[0], LinMobileConstants.STATUS_ARRAY[0]));
				userDetailsDTO.setStatusList(statusList);
				userDetailsDTO.setSelectedStatusList(statusList);
				List<CommonDTO> roleList = new ArrayList<CommonDTO>();
				roleList.add(new CommonDTO(sessionDTO.getRoleId(),sessionDTO.getRoleName()));
				userDetailsDTO.setRoleList(roleList);
			}
			else {
				fetchAllStatus();
				userDetailsDTO.setRoleList(service.fetchActiveRolesByCompanyId(companyId, sessionDTO.isSuperAdmin()));
			}
			
			if(result) {
				return "success";
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in initEditUser of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String userUpdate() {		
		log.info("inside userUpdate of UserAction");
		int result = 0;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			sessionDTO.setSuperAdmin(false);
			sessionDTO.setAdminUser(false);
			sessionDTO.setClient(false);
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			log.info("sessionDTO initialised.");
			
			if(userDetailsDTO.getEmailId() != null && !userDetailsDTO.getEmailId().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getUserName() != null && !userDetailsDTO.getUserName().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat() != null && !userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase(userDetailsDTO.getEmailId().trim()) ){
				
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				IUserDetailsDAO dao = new UserDetailsDAO();
				result = service.userUpdate(userDetailsDTO, sessionDTO.getUserId());
				if(result > 0) {
					setUpdateUserStatus(1);
					return "success";
				}
				else if(result == -1) {			// emailId belongs to another user
					setUpdateUserStatus(2);
					return "exists";
				}
				else if(result == -2) {
					setUpdateUserStatus(1);
					sessionDTO.setRoleId(userDetailsDTO.getRole());
					
					List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
					RolesAndAuthorisation rolesAndAuthorisation = dao.getRolesAndAuthorisationByRoleId(userDetailsDTO.getRole(), rolesAndAuthorisationList);
					if(rolesAndAuthorisation != null) {
						sessionDTO.setRoleName(rolesAndAuthorisation.getRoleName().trim());
					}
					if(rolesAndAuthorisation.getRoleName().trim().equals(LinMobileConstants.ADMINS[0])) {
						sessionDTO.setSuperAdmin(true);
					}else if(rolesAndAuthorisation.getRoleName().trim().equals(LinMobileConstants.ADMINS[1])) {
						sessionDTO.setAdminUser(true);
					}else if(rolesAndAuthorisation.getRoleName().trim().equals(LinMobileConstants.USERS_ARRAY[2])) {
						sessionDTO.setClient(true);
					}
					
					sessionDTO.setEmailId(userDetailsDTO.getEmailId());
					sessionDTO.setUserName(userDetailsDTO.getUserName());
					session.put("sessionDTO", sessionDTO);
					return "success";
				}
				else {
					setUpdateUserStatus(0);
					return "failed";
				}
				
			}
			else {
				setUpdateUserStatus(3);
				return "inappropriate";
			}
		}catch (Exception e) {
			
			setUpdateUserStatus(0);
			log.severe("Exception in userUpdate of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String initUserOwnProfileUpdate() {
		log.info("inside initUserOwnProfileUpdate of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			//service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
			sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[5]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
			result = service.initUserOwnProfileUpdate(userDetailsDTO, sessionDTO.getUserId());
			if(result) {
				return "success";
			}
			else {
				setLinStatus("failed");
				return "failed";
			}
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in initUserOwnProfileUpdate of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String userOwnProfileUpdate() {		
		log.info("inside userOwnProfileUpdate of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			//service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
			sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[5]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
			if(userDetailsDTO.getEmailId() != null && !userDetailsDTO.getEmailId().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getUserName() != null && !userDetailsDTO.getUserName().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat() != null && !userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getPassword() != null && !userDetailsDTO.getPassword().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getPasswordRepeat() != null && !userDetailsDTO.getPasswordRepeat().trim().equalsIgnoreCase("")
				&& userDetailsDTO.getEmailIdRepeat().trim().equalsIgnoreCase(userDetailsDTO.getEmailId().trim())
				&& userDetailsDTO.getPasswordRepeat().trim().equals(userDetailsDTO.getPassword().trim()) ){
				
				result = service.userOwnProfileUpdate(userDetailsDTO);
				if(result) {
					setUpdateUserStatus(1);
					sessionDTO.setEmailId(userDetailsDTO.getEmailId());
					sessionDTO.setUserName(userDetailsDTO.getUserName());
					return "success";
				}
				else {
					setUpdateUserStatus(0);
					return "failed";
				}
			}
			else {
				setLinStatus("inappropriate");
			}
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in userOwnProfileUpdate of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String forgetPassword() {
		log.info("inside forgetPassword of UserAction");
		int result = -1;
		try {	
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			result = service.forgetPassword(userDetailsDTO);
			if(result == 1) {		// mail send successfully
				setForgetPasswordStatus("mailSend");
			}
			else if(result == 2) {	// user not in system
				setForgetPasswordStatus("emailNotFound");
			}
			else if(result == 3) {	// Inactive User
				setForgetPasswordStatus("inactiveUser");
			}
			else {
				setForgetPasswordStatus("tryAgain");
			}
		}catch (Exception e) {
			setForgetPasswordStatus("tryAgain");
			
			log.severe("Exception in forgetPassword of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String forgetPasswordEmail() {
		log.info("inside forgetPasswordEmail of UserAction");
		try {
			String randomNumber = request.getParameter("reset");
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			long id = service.forgetPasswordEmail(randomNumber);
			if(id > 0) {
				userDetailsDTO.setId(id);
				userDetailsDTO.setRandomNumber(randomNumber);
			}
			else {
				return "loginPage";
			}
		}catch (Exception e) {
			
			log.severe("Exception in forgetPasswordEmail of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String resetPassword() {
		log.info("inside resetPassword of UserAction");
		Boolean result = false;
		try {
			if(userDetailsDTO.getId() > 0
					&& userDetailsDTO.getPasswordRepeat() != null && !userDetailsDTO.getPasswordRepeat().trim().equalsIgnoreCase("")
					&& userDetailsDTO.getPassword() != null && !userDetailsDTO.getPassword().trim().equalsIgnoreCase("")
					&& userDetailsDTO.getPassword().trim().equals(userDetailsDTO.getPasswordRepeat().trim())){
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				result = service.resetPassword(userDetailsDTO);
				if(result) {
					setLinStatus("success");
					response.sendRedirect("/login.lin?id="+userDetailsDTO.getEmailId());
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("inappropriate");
			}
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in resetPassword of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String roleSetup() {
		log.info("inside roleSetup of UserAction");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			service.roleSetup(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in roleSetup of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String createRole() {		
		log.info("inside createRole of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllActiveRoles(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			userDetailsDTO.getRoleList().add(0, new CommonDTO("-1", "------"));
			fetchAllActiveAuthorisationText();
			
			if(userDetailsDTO.getRoleName() != null && !userDetailsDTO.getRoleName().trim().equalsIgnoreCase("")){
				
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				result = service.createRole(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
				if(result) {
					setLinStatus("success");
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("failed");
			}
			userDetailsDTO.setRole("");
			userDetailsDTO.setCompanyId("");
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in createRole of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String initCreateRole() {
		log.info("Inside initCreateRole of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllActiveRoles(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			userDetailsDTO.getRoleList().add(0, new CommonDTO("-1", "------"));
			List <CommonDTO> selectedRoleList = new ArrayList<CommonDTO>();
			selectedRoleList.add(new CommonDTO("-1", ""));
			userDetailsDTO.setSelectedRoleList(selectedRoleList);
			fetchAllActiveAuthorisationText();
			fetchAllStatus();
		}
		catch (Exception e) {
			
			log.severe("Exception in initCreateRole of UserAction" + e.getMessage());
		}
		return "success";
	}
	
	public String initEditRole() {
		log.info("inside initEditRole of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllActiveRoles(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			userDetailsDTO.getRoleList().add(0, new CommonDTO("-1", "------"));
			List <CommonDTO> selectedRoleList = new ArrayList<CommonDTO>();
			selectedRoleList.add(new CommonDTO("-1", ""));
			userDetailsDTO.setSelectedRoleList(selectedRoleList);
			fetchAllActiveAuthorisationText();
			fetchAllStatus();
			
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			result = service.initEditRole(userDetailsDTO, sessionDTO.isSuperAdmin());
			if(result) {
				return "success";
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in initEditRole of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String updateRole() {		
		log.info("inside updateRole of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			
			if(userDetailsDTO.getRoleName() != null && !userDetailsDTO.getRoleName().trim().equalsIgnoreCase("")){
				
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				result = service.updateRole(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					return "success";
				}
				else {
					return "failed";
				}
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in updateRole of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String copyRole() {		
		log.info("inside copyRole of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllActiveRoles(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			userDetailsDTO.getRoleList().add(0, new CommonDTO("-1", "------"));
			List <CommonDTO> selectedRoleList = new ArrayList<CommonDTO>();
			selectedRoleList.add(new CommonDTO(userDetailsDTO.getRole(), ""));
			userDetailsDTO.setSelectedRoleList(selectedRoleList);
			fetchAllActiveAuthorisationText();
			fetchAllStatus();
			
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			result = service.copyRole(userDetailsDTO);
			if(result) {
				return "success";
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in copyRole of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String copyRoleUpdate() {		
		log.info("inside copyRoleUpdate of UserAction");
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllActiveRoles(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			userDetailsDTO.getRoleList().add(0, new CommonDTO("-1", "------"));
			List <CommonDTO> selectedRoleList = new ArrayList<CommonDTO>();
			selectedRoleList.add(new CommonDTO(userDetailsDTO.getRole(), ""));
			userDetailsDTO.setSelectedRoleList(selectedRoleList);
			fetchAllActiveAuthorisationText();
			fetchAllStatus();
			
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			result = service.copyRole(userDetailsDTO);
			if(result) {
				return "success";
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in copyRoleUpdate of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String teamSetup() {
		log.info("inside teamSetup of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			service.teamSetup(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in teamSetup of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String initCreateTeam() {
		log.info("inside initCreateTeam of UserAction");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> tempAppViewList = null;
		List<CommonDTO> appViewsList = new ArrayList<CommonDTO>();
		boolean accessToAccounts = false;
		boolean accessToProperties = false;
		List<CommonDTO> propertiesList = new ArrayList<CommonDTO>();
		List<CommonDTO> accountsList = new ArrayList<CommonDTO>();
		/*String publisherIdsForBigQuery = "";*/
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			String companyId = "";
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
				if(userDetailsDTO.getCompanyList() != null && userDetailsDTO.getCompanyList().size() > 0) {
					tempAppViewList = userDetailsDTO.getCompanyList().get(0).getAppViews();
					accessToAccounts = userDetailsDTO.getCompanyList().get(0).isAccessToAccounts();
					accessToProperties = userDetailsDTO.getCompanyList().get(0).isAccessToProperties();
					companyId = userDetailsDTO.getCompanyList().get(0).getId()+"";
					userDetailsDTO.setDefaultSelectedCompanyType(userDetailsDTO.getCompanyList().get(0).getCompanyType().trim());
				}
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					tempAppViewList = companyObj.getAppViews();
					accessToAccounts = companyObj.isAccessToAccounts();
					accessToProperties = companyObj.isAccessToProperties();
					companyId = String.valueOf(companyObj.getId());
					userDetailsDTO.setCompanyIDOfSessionAdminUser(companyId);
					userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
				}
				else {
					throw new Exception("No company found for user");
				}
			}
			
			if(accessToAccounts) {
				userDetailsDTO.setAccessToAccounts("1");
			}
			if(accessToProperties) {
				userDetailsDTO.setAccessToProperties("1");
			}
			if(tempAppViewList != null && tempAppViewList.size() > 0) {
				Map<String, String> allAllViewMap = UserService.getAllAppViewsMap();
				for (String appView : tempAppViewList) {
					appViewsList.add(new CommonDTO(appView.trim(), allAllViewMap.get(appView.trim())));
				}
			}
			
			/*publisherIdsForBigQuery = service.getPublisherIdsForBigQueryByCompanyId(companyId);*/
			
			if(accessToProperties && companyId != null && userDetailsDTO.getDefaultSelectedCompanyType() != null && userDetailsDTO.getDefaultSelectedCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
				// if publisher Pool partner and have access to properties
				propertiesList = dao.getActivePropertiesByPublisherCompanyId(companyId);
			}
			
			if(accessToAccounts && companyId != null) {
				accountsList = service.getActiveAccountsByCompanyId(companyId, true);
			}
			
			userDetailsDTO.setAppViewsList(appViewsList);
			userDetailsDTO.setPropertiesList(propertiesList);
			userDetailsDTO.setAccountsList(accountsList);
			/*userDetailsDTO.setPublisherIdsForBigQuery(publisherIdsForBigQuery);*/
			
		}catch (Exception e) {
			log.severe("Exception in initCreateTeam of UserAction "+e.getMessage());
			
			setLinStatus("failed");
		}	
		return "success";
	}
	
	public String createTeam() {
		log.info("inside createTeam of UserAction");
		Boolean result = false;
		/*String publisherIdsForBigQuery = "";*/
		List<String> tempAppViewList = null;
		List<CommonDTO> appViewsList = new ArrayList<CommonDTO>();
		boolean accessToAccounts = false;
		boolean accessToProperties = false;
		List<CommonDTO> propertiesList = new ArrayList<CommonDTO>();
		List<CommonDTO> accountsList = new ArrayList<CommonDTO>();
		IUserDetailsDAO dao = new UserDetailsDAO();
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			
			if(userDetailsDTO.getTeamName() != null && !userDetailsDTO.getTeamName().trim().equalsIgnoreCase("") 
				&& userDetailsDTO.getSelectedCompanyType() != null && !userDetailsDTO.getSelectedCompanyType().trim().equalsIgnoreCase("")){				
				result = service.createTeam(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
				if(result) {
					setLinStatus("success");
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("failed");
			}
			
								/****  init create  ****/
			String companyId = "";
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
				if(userDetailsDTO.getCompanyList() != null && userDetailsDTO.getCompanyList().size() > 0) {
					tempAppViewList = userDetailsDTO.getCompanyList().get(0).getAppViews();
					accessToAccounts = userDetailsDTO.getCompanyList().get(0).isAccessToAccounts();
					accessToProperties = userDetailsDTO.getCompanyList().get(0).isAccessToProperties();
					companyId = userDetailsDTO.getCompanyList().get(0).getId()+"";
					userDetailsDTO.setDefaultSelectedCompanyType(userDetailsDTO.getCompanyList().get(0).getCompanyType().trim());
				}
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					tempAppViewList = companyObj.getAppViews();
					accessToAccounts = companyObj.isAccessToAccounts();
					accessToProperties = companyObj.isAccessToProperties();
					companyId = String.valueOf(companyObj.getId());
					userDetailsDTO.setCompanyIDOfSessionAdminUser(companyId);
					userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
				}
				else {
					throw new Exception();
				}
			}
			
			if(accessToAccounts) {
				userDetailsDTO.setAccessToAccounts("1");
			}
			if(accessToProperties) {
				userDetailsDTO.setAccessToProperties("1");
			}
			if(tempAppViewList != null && tempAppViewList.size() > 0) {
				Map<String, String> allAllViewMap = UserService.getAllAppViewsMap();
				for (String appView : tempAppViewList) {
					appViewsList.add(new CommonDTO(appView.trim(), allAllViewMap.get(appView.trim())));
				}
			}
			
			/*publisherIdsForBigQuery = service.getPublisherIdsForBigQueryByCompanyId(companyId);*/
			
			if(accessToProperties && companyId != null && userDetailsDTO.getDefaultSelectedCompanyType() != null && userDetailsDTO.getDefaultSelectedCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
				// if publisher Pool partner and have access to properties
				propertiesList = dao.getActivePropertiesByPublisherCompanyId(companyId);
			}
			
			if(accessToAccounts && companyId != null) {
				accountsList = service.getActiveAccountsByCompanyId(companyId, true);
			}
			
			userDetailsDTO.setAppViewsList(appViewsList);
			userDetailsDTO.setPropertiesList(propertiesList);
			userDetailsDTO.setAccountsList(accountsList);
			/*userDetailsDTO.setPublisherIdsForBigQuery(publisherIdsForBigQuery);*/
			
								/***  init create  ****/
			userDetailsDTO.setCompanyId("");
			userDetailsDTO.setProperties("");
			userDetailsDTO.setAccounts("");
			userDetailsDTO.setAppViews("");
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in createTeam of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	
	public String initEditTeam() {
		log.info("inside initEditTeam of UserAction");
		String publisherIdsForBigQuery = "";
		TeamPropertiesObj teamProperties = null;
		List<String> tempAppViewList = null;
		List<CommonDTO> appViewsList = new ArrayList<CommonDTO>();
		boolean accessToAccounts = false;
		boolean accessToProperties = false;
		
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			IUserDetailsDAO dao = new UserDetailsDAO();
			
			List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
			teamProperties = dao.getTeamPropertiesObjById(userDetailsDTO.getTeamId(), teamPropertiesObjList);
			if(teamProperties != null && teamProperties.getCompanyId() != null) {
 				sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
 				if(!isAuthorised(sessionDTO)) {
 					return "unAuthorisedAccess";
 				}
 				
 				String companyId = teamProperties.getCompanyId().trim();
 				CompanyObj companyObj = dao.getCompanyById(Long.valueOf(companyId), MemcacheUtil.getAllCompanyList());
 				if(companyObj != null && companyObj.getCompanyType() != null) {
 					tempAppViewList = companyObj.getAppViews();
					accessToAccounts = companyObj.isAccessToAccounts();
					accessToProperties = companyObj.isAccessToProperties();
					userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
					userDetailsDTO.setCompanyName(companyObj.getCompanyName());
					
					if(accessToAccounts) {
						userDetailsDTO.setAccessToAccounts("1");
					}
					if(accessToProperties) {
						userDetailsDTO.setAccessToProperties("1");
					}
					if(tempAppViewList != null && tempAppViewList.size() > 0) {
						Map<String, String> allAppViewMap = UserService.getAllAppViewsMap();
						for (String appView : tempAppViewList) {
							appViewsList.add(new CommonDTO(appView.trim(), allAppViewMap.get(appView.trim())));
						}
					}
 					
	 				//publisherIdsForBigQuery = service.getPublisherIdsForBigQueryByCompanyId(companyId);
	 				service.initEditTeam(teamProperties, userDetailsDTO);
	 				
	 				userDetailsDTO.setAppViewsList(appViewsList);
	 				//userDetailsDTO.getAppViewsList().add(0, new CommonDTO(LinMobileConstants.ALL_APP_VIEWS, LinMobileConstants.ALL_APP_VIEWS));
	 				//userDetailsDTO.setPublisherIdsForBigQuery(publisherIdsForBigQuery);
	 				fetchAllStatus();
					
					return "success";
 				}
			}
		}catch (Exception e) {
			
			log.severe("Exception in initEditTeam of UserAction : "+e.getMessage());
		}
		return "failed";
	}
	
	public String updateTeam() {
		log.info("inside updateTeam of UserAction");
		Boolean result = false;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(userDetailsDTO.getTeamName() != null && !userDetailsDTO.getTeamName().trim().equalsIgnoreCase("")
					&& userDetailsDTO.getSelectedCompanyType() != null && !userDetailsDTO.getSelectedCompanyType().trim().equalsIgnoreCase("") 
					&& userDetailsDTO.getTeamId() != null && !userDetailsDTO.getTeamId().trim().equalsIgnoreCase("")){				
				result = service.updateTeam(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					return "success";
				}
				else {
					return "failed";
				}
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in updateTeam of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String deleteTeam() {
		return "success";
	}
	
	public String companySetup() {
		log.info("inside companySetup of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			userDetailsDTO.setCompanySetupList(MemcacheUtil.getAllCompanyList());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in companySetup of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String initCreateNewCompany() {
		log.info("Inside initCreateNewCompany of UserAction");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllDemandPartnerTypes();
			fetchAllActiveDemandPartners(sessionDTO.getUserId());
			userDetailsDTO.setAdServerCredentialsCounterValue("2");
			userDetailsDTO.setPassbackSiteTypeCounterValue("2");
			userDetailsDTO.setServiceURLCounterValue("2");
			
			// get views
			userDetailsDTO.setAppViewsList(service.getAllAppViewsforCompany());
		}
		catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in initCreateNewCompany of UserAction" + e.getMessage());
		}
		return "success";
	}
	
	public String createNewCompany(){
		log.info("inside createNewCompany of UserAction");
		Boolean result = false;
		try{
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllDemandPartnerTypes();
			fetchAllActiveDemandPartners(sessionDTO.getUserId());
			userDetailsDTO.setAdServerCredentialsCounterValue("2");
			userDetailsDTO.setPassbackSiteTypeCounterValue("2");
			userDetailsDTO.setServiceURLCounterValue("2");
			
			// get views
			userDetailsDTO.setAppViewsList(service.getAllAppViewsforCompany());
			
			if(userDetailsDTO.getCompanyName()!=null && !userDetailsDTO.getCompanyName().equals("")){
				result = service.createNewCompany(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					setLinStatus("success");
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("failed");
			}
			userDetailsDTO.setDemandPartnerId("");
			userDetailsDTO.setDemandPartnerType("");
			userDetailsDTO.setAppViews("");
		}catch(Exception e){
			
			setLinStatus("failed");
			log.severe("Exception in createNewCompany of UserAction"+ e.getMessage());
		}
		return "success";
		
	}
	
	public String initEditCompany(){
		log.info("inside initEditCompany of UserAction");
		Boolean result = false;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllDemandPartnerTypes();
			fetchAllActiveDemandPartners(sessionDTO.getUserId());
			fetchAllStatus();
			
			// get views
			userDetailsDTO.setAppViewsList(service.getAllAppViewsforCompany());
			result = service.initEditCompany(userDetailsDTO);
			if(result){
				return "success";
			}else{
				return "failed";
			}
		}catch(Exception e){
			log.severe("Exception in initEditCompany userAction "+ e.getMessage());
			
			return "failed";
		}
	}
	
	public String updateCompany(){
		log.info("inside updateCompany of UserAction");
		Boolean result = false;
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			
			if(userDetailsDTO.getCompanyName()!=null && !userDetailsDTO.getCompanyName().equals("")){
				IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
				result = service.updateCompany(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					return "success";
				}
				else {
					return "failed";
				}
			}
			else {
				return "failed";
			}
		}catch(Exception e){
			log.severe("Exception in updateCompany userAction "+ e.getMessage());
			
			return "failed";
		}
	}
	
	public String propertySetup() {
		log.info("inside propertySetup of UserAction");
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!sessionDTO.isSuperAdmin() && !sessionDTO.isPublisherPoolPartner()) {
				return "unAuthorisedAccess";
			}
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			service.propertySetup(userDetailsDTO, sessionDTO.isSuperAdmin(), sessionDTO.getUserId());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in propertySetup of UserAction"+e.getMessage());
		}
		return "success";
	}
	
	public String initCreateProperty() {
		log.info("Inside initCreateProperty of UserAction");
		String publisherId = "";
		List<CommonDTO> publishersList = new ArrayList<CommonDTO>();
		userDetailsDTO.setNetworkAvailabile(true);
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!sessionDTO.isSuperAdmin() && !sessionDTO.isPublisherPoolPartner()) {
				return "unAuthorisedAccess";
			}
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(sessionDTO.isSuperAdmin()) {
				publishersList = service.getPublishersForPropertySetup(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					publisherId = String.valueOf(companyObj.getId());
					if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
						userDetailsDTO.setNetworkAvailabile(true);
					}
					else {
						userDetailsDTO.setNetworkAvailabile(false);
					}
				}
			}
			userDetailsDTO.setPublisherId(publisherId);
			userDetailsDTO.setPublishersList(publishersList);
		}
		catch (Exception e) {
			
			log.severe("Exception in initCreateProperty of UserAction" + e.getMessage());
		}
		return "success";
	}
	
	public String createProperty(){
		log.info("inside createProperty of UserAction");
		Boolean result = false;
		String publisherIdsForBigQuery = "";
		try{
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!sessionDTO.isSuperAdmin() && !sessionDTO.isPublisherPoolPartner()) {
				return "unAuthorisedAccess";
			}
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			List<CommonDTO> publishersList = service.getPublishersForPropertySetup(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			userDetailsDTO.setPublishersList(publishersList);
			userDetailsDTO.setPublisherIdsForBigQuery(publisherIdsForBigQuery);
			
			if(userDetailsDTO.getPropertyName()!=null && !userDetailsDTO.getPropertyName().equals("") &&
				userDetailsDTO.getNetworkId() != null && !userDetailsDTO.getNetworkId().trim().equals("") &&
				userDetailsDTO.getNetworkUsername() != null && !userDetailsDTO.getNetworkUsername().trim().equals("") &&
				userDetailsDTO.getDFPPropertyName()!=null && !userDetailsDTO.getDFPPropertyName().trim().equals("") &&
				userDetailsDTO.getPublisherId() != null && !userDetailsDTO.getPublisherId().trim().equals("") &&
				userDetailsDTO.getDFPPropertyId()!=null && !userDetailsDTO.getDFPPropertyId().trim().equals("")){
				result = service.createProperty(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					setLinStatus("success");
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("failed");
			}
			userDetailsDTO.setPublisherId("");
		}catch(Exception e){
			
			setLinStatus("failed");
			log.severe("Exception in createProperty of UserAction"+ e.getMessage());
		}
		return "success";
		
	}
	
	public String initEditProperty() {
		log.info("inside initEditProperty of UserAction");
		Boolean result = false;
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!sessionDTO.isSuperAdmin() && !sessionDTO.isPublisherPoolPartner()) {
				return "unAuthorisedAccess";
			}
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			List<CommonDTO> publishersList = service.getPublishersForPropertySetup(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
			userDetailsDTO.setPublishersList(publishersList);
			fetchAllStatus();
			
			result = service.initEditProperty(userDetailsDTO, sessionDTO.isSuperAdmin(), sessionDTO.getUserId());
			if(result) {
				 return "success";
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in initEditProperty of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String updateProperty() {
		log.info("inside updateProperty of UserAction");
		Boolean result = false;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!sessionDTO.isSuperAdmin() && !sessionDTO.isPublisherPoolPartner()) {
				return "unAuthorisedAccess";
			}
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(userDetailsDTO.getPropertyName()!=null && !userDetailsDTO.getPropertyName().equals("") &&
				userDetailsDTO.getNetworkId() != null && !userDetailsDTO.getNetworkId().trim().equals("") &&
				userDetailsDTO.getDFPPropertyName()!=null && !userDetailsDTO.getDFPPropertyName().trim().equals("") &&
				userDetailsDTO.getPublisherId() != null && !userDetailsDTO.getPublisherId().trim().equals("") &&
				userDetailsDTO.getDFPPropertyId()!=null && !userDetailsDTO.getDFPPropertyId().trim().equals("")){				
				result = service.updateProperty(userDetailsDTO, sessionDTO.getUserId());
				if(result) {
					return "success";
				}
				else {
					return "failed";
				}
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in updateProperty of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String companySettings(){
		log.info("inside companySettings of UserAction");
		Boolean result = false;
		setLinStatus("failed");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllDemandPartnerTypes();
			fetchAllActiveDemandPartners(sessionDTO.getUserId());
			fetchAllStatus();
			
			// get views
			userDetailsDTO.setAppViewsList(service.getAllAppViewsforCompany());
			result = service.companySettings(userDetailsDTO, sessionDTO.getUserId());
			if(result){
				setLinStatus("success");
			}
		}catch(Exception e){
			log.severe("Exception in companySettings userAction "+ e.getMessage());
			
		}
		return "success";
	}
	
	public String companySettingsUpdate(){
		log.info("inside companySettingsUpdate of UserAction");
		Boolean result = false;
		setLinStatus("updateFailed");
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			fetchAllDemandPartnerTypes();
			fetchAllActiveDemandPartners(sessionDTO.getUserId());
			fetchAllStatus();
			
			// get views
			userDetailsDTO.setAppViewsList(service.getAllAppViewsforCompany());
			
			if(userDetailsDTO.getCompanyName()!=null && !userDetailsDTO.getCompanyName().equals("")){
				userDetailsDTO.setCompanyLogoURL("");
				result = service.updateCompany(userDetailsDTO, sessionDTO.getUserId());
				if(result){
					String companyLogoURL = userDetailsDTO.getCompanyLogoURL();
					if(companyLogoURL != null && companyLogoURL.length() > 0) {
						sessionDTO.setCompanyLogo(true);
						sessionDTO.setCompanyLogoURL(companyLogoURL);
						sessionDTO.setCompanyName(userDetailsDTO.getCompanyName());
						session.put("sessionDTO", sessionDTO);
					}
					setLinStatus("updateSuccess");
				}
			}
		}catch(Exception e){
			log.severe("Exception in companySettingsUpdate userAction "+ e.getMessage());
			
		}
		return "success";
	}
	
	public String accountSetup() {
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			service.accountSetup(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in accountSetup of UserAction"+e.getMessage());
		}	
		return "success";
	}
	
	public String initCreateAccount() {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		String companyId = "";
		List<AdServerCredentialsDTO> adServerCredentialsDTOList = new ArrayList<AdServerCredentialsDTO>();
		userDetailsDTO.setNetworkAvailabile(false);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyId = String.valueOf(companyObj.getId());
					if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
						userDetailsDTO.setNetworkAvailabile(true);
					}
					else {
						userDetailsDTO.setNetworkAvailabile(false);
					}
					userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
				}
			}
		}catch (Exception e) {
			log.severe("Exception in initCreateAccount of UserAction "+e.getMessage());
			
			setLinStatus("failed");
		}
		userDetailsDTO.setCompanyId(companyId);
		userDetailsDTO.setAdServerCredentialsDTOList(adServerCredentialsDTOList);
		userDetailsDTO.setIndustryList(MemcacheUtil.getAllIndustryObjList());
		return "success";
	}
	
	public String createAccount() {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		String companyId = "";
		Boolean result = false;
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
							
			if(userDetailsDTO.getDfpAccountName() != null && !userDetailsDTO.getDfpAccountName().trim().equals("") &&
				userDetailsDTO.getNetworkId() != null && !userDetailsDTO.getNetworkId().trim().equals("") &&
				userDetailsDTO.getNetworkUsername() != null && !userDetailsDTO.getNetworkUsername().trim().equals("") &&
				userDetailsDTO.getAccountName() != null && !userDetailsDTO.getAccountName().trim().equals("") &&
				userDetailsDTO.getAccountDfpId() != null && !userDetailsDTO.getAccountDfpId().trim().equals("") && 
				userDetailsDTO.getAccountType() != null && !userDetailsDTO.getAccountType().trim().equals("") && 
				userDetailsDTO.getCompanyId() != null && !userDetailsDTO.getCompanyId().trim().equals("")) {				
				result = service.createAccount(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
				if(result) {
					setLinStatus("success");
				}
				else {
					setLinStatus("failed");
				}
			}
			else {
				setLinStatus("failed");
			}
								/****  init create  ****/
			if(sessionDTO.isSuperAdmin()) {
				fetchAllActiveCompanies();
			}
			else {
				CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyId = String.valueOf(companyObj.getId());
					if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
						userDetailsDTO.setNetworkAvailabile(true);
					}
					else {
						userDetailsDTO.setNetworkAvailabile(false);
					}
					userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
				}
			}
							    /***  init create  ****/
		}catch (Exception e) {
			
			setLinStatus("failed");
			log.severe("Exception in createAccount of UserAction"+e.getMessage());
		}
		userDetailsDTO.setCompanyId(companyId);
		userDetailsDTO.setIndustryList(MemcacheUtil.getAllIndustryObjList());
		userDetailsDTO.setIndustry("");
		return "success";
	}
	
	public String initEditAccount() {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(service.initEditAccount(userDetailsDTO, sessionDTO.isSuperAdmin(), sessionDTO.getUserId())) {
				/*if(sessionDTO.isSuperAdmin()) {
					fetchAllActiveCompanies();
				}
				else {
					CompanyObj companyObj = service.getCompanyIdByAdminUserId(sessionDTO.getUserId());
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
						companyId = String.valueOf(companyObj.getId());
						if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
							userDetailsDTO.setNetworkAvailabile(true);
						}
						else {
							userDetailsDTO.setNetworkAvailabile(false);
						}
						userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
						userDetailsDTO.setCompanyId(companyId);
					}
				}*/
				userDetailsDTO.setIndustryList(MemcacheUtil.getAllIndustryObjList());
				fetchAllStatus();
				return "success";
			}
		}catch (Exception e) {
			
			log.severe("Exception in initEditTeam of UserAction : "+e.getMessage());
		}
		return "failed";
	}
	
	public String updateAccount() {
		log.info("inside updateTeam of UserAction");
		Boolean result = false;
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			if(userDetailsDTO.getDfpAccountName() != null && !userDetailsDTO.getDfpAccountName().trim().equals("") &&
					userDetailsDTO.getNetworkId() != null && !userDetailsDTO.getNetworkId().trim().equals("") &&
					userDetailsDTO.getNetworkUsername() != null && !userDetailsDTO.getNetworkUsername().trim().equals("") &&
					userDetailsDTO.getAccountName() != null && !userDetailsDTO.getAccountName().trim().equals("") &&
					userDetailsDTO.getAccountDfpId() != null && !userDetailsDTO.getAccountDfpId().trim().equals("") && 
					userDetailsDTO.getAccountType() != null && !userDetailsDTO.getAccountType().trim().equals("") && 
					userDetailsDTO.getCompanyId() != null && !userDetailsDTO.getCompanyId().trim().equals("")) {				
				result = service.updateAccount(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
				if(result) {
					return "success";
				}
				else {
					return "failed";
				}
			}
			else {
				return "failed";
			}
		}catch (Exception e) {
			
			log.severe("Exception in updateTeam of UserAction"+e.getMessage());
			return "failed";
		}
	}
	
	public String uploadAdminEntities(){
		log.info("linCSVFileName:"+linCSVFileName);
		log.info("entityType:"+entityType);
		boolean status = false;
		linStatus = "";
		
		if(entityType!=null && !entityType.equals("-1")){
			if(entityType.equalsIgnoreCase("companies")) {
				status = CSVReaderUtil.uploadCompany(linCSV);
			}else if(entityType.equalsIgnoreCase("Properties")) {
				status = CSVReaderUtil.uploadProperties(linCSV);
			}else if(entityType.equalsIgnoreCase("Roles")) {
				status = CSVReaderUtil.readCSVForRolesAndAuthorisation(linCSV, 0, 10000);
			}else if(entityType.equalsIgnoreCase("Authorisation Label")) {
				status = CSVReaderUtil.readCSVForAuthorisationText(linCSV, 0, 10000);
			}else if(entityType.equalsIgnoreCase("Users")) {
				status = CSVReaderUtil.readCSVForUserDetails(linCSV);
			}else if(entityType.equalsIgnoreCase("Accounts")) {
				status = CSVReaderUtil.uploadAccounts(linCSV);
			}else{
				linStatus="Error: Invalid entity type.";
			}
			if(linStatus.equals("")) {
				if(status){
					linStatus = "Uploaded successfully";
				}else{
					linStatus = "Failed to upload file, please check log";
				}
			}
			
		}else{
			linStatus="Error: entity type is required.";				
		}
		request.setAttribute("status", linStatus);
		return Action.SUCCESS;
	}
	
	public String updateEntities(){
		log.info("entityType:"+entityType);
		linStatus = "";
		
		if(entityType!=null && !entityType.equals("-1")){
			if(entityType.equalsIgnoreCase("UserDetailsObj")) {
				Map map = UserService.updateAllUsers();
				linStatus = (String) map.get("message");
			}else if(entityType.equalsIgnoreCase("SmartCampaignObj")) {
				linStatus = SmartCampaignPlannerService.updateSmartCampaignObj();
			}else if(entityType.equalsIgnoreCase("AccountsEntity")) {
				linStatus = UserService.updateAllAccounts();
			}else{
				linStatus="Error: Invalid entity type.";
			}
		}else{
			linStatus="Error: entity type is required.";				
		}
		request.setAttribute("status", linStatus);
		return Action.SUCCESS;
	}
	
	
	/*
	 * Added by Anup Dutta to update Census Data Object
	 * */
	public String updateCensus(){
		log.info("Inside Census Update");
		linStatus = "";
		
		String grouptxt = request.getParameter("grouptxt");
		String group = request.getParameter("group");
		String gender = request.getParameter("gender");
		String bqColumn = request.getParameter("bqColumn");
		String bqMaleColumn = request.getParameter("bqMaleColumn");
		String bqFemaleColumn = request.getParameter("bqFemaleColumn");
		String bqParentColumn = request.getParameter("bqParentColumn");
		
		
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		
		if(grouptxt == "" || group == "" || bqColumn == "" || bqParentColumn == "" || gender == ""){
			linStatus="Error: Group text, Gender Available , Group, BQ main column , BQ parent column are mandatory";
		}else{
			System.out.println(gender + " " + bqMaleColumn + " " + bqFemaleColumn);
			if(gender == "1"){
				if(bqMaleColumn == "" || bqFemaleColumn == "" || bqMaleColumn == null || bqFemaleColumn == null)
				{
					linStatus="Error: if Gender is true BQ Male column , BQ Female column is must";
				}else{
					linStatus = service.addCensusCategory(group, grouptxt, gender, bqColumn, bqParentColumn, bqMaleColumn, bqFemaleColumn);
				}
			}else{
				linStatus = service.addCensusCategory(group, grouptxt, gender, bqColumn, bqParentColumn, bqMaleColumn, bqFemaleColumn);
			}
		}
		
		request.setAttribute("status", linStatus);
		return Action.SUCCESS;
	}
	
	public String newsAndResearch() {
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[3]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[3]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    	}
	    	else {
	    		return "unAuthorisedAccess";
	    	}
    	}
		catch (Exception e) {
    		log.severe("Exception in execution of newsAndResearch Action : "+e.getMessage());
    		
    		return "unAuthorisedAccess";
		}
		return Action.SUCCESS;
	}
	
	public String replaceAccountCompanyId() {
		String status = "";
		try {
			Exception e = new Exception();
			String prevoiusId=request.getParameter("prevoiusId");
			String newId=request.getParameter("newId");
			if(prevoiusId == null || !(LinMobileUtil.isNumeric(prevoiusId.trim()))) {
				status = "Prevoius Company Id should be numeric";
				throw e;
			}
			if(newId == null || !(LinMobileUtil.isNumeric(newId.trim()))) {
				status = "New Company Id should be numeric";
				throw e;
			}
			
			IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
			List<AccountsEntity> accountsEntities = userDetailsDAO.getAccountsObjByCompanyId(prevoiusId.trim());
			if(accountsEntities != null && accountsEntities.size() > 0) {
				for (AccountsEntity accountsEntity : accountsEntities) {
					if(accountsEntity != null && accountsEntity.getCompanyId() != null) {
						userDetailsDAO.deleteObject(accountsEntity);
						accountsEntity.setCompanyId(newId.trim());
						String id = accountsEntity.getId();
						String replaceId = id.substring(0, (id.length() - prevoiusId.trim().length() + 1)) + newId.trim();
						accountsEntity.setId(replaceId);
						userDetailsDAO.saveObject(accountsEntity);
						log.info("Account updated for prevoius id : "+id+" by new Id :"+replaceId);
					}
				}
			}
			else  {
				status = "No Account exists for company Id : "+prevoiusId;
				throw e;
			}
			if(status.length() == 0) {
				status = "Account company Id updated sucessfully";
			}
		} catch(Exception e) {
			 log.severe("Exception in replaceAccountCompanyId()  in PerformanceMonitoringAction : "+e.getMessage());
			 log.warning(status);
			 
		}
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}

	
	@Override
	public UserDetailsDTO getModel() {	
		return userDetailsDTO;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setLinStatus(String linStatus) {
		this.linStatus = linStatus;
	}

	public String getLinStatus() {
		return linStatus;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setUpdateUserStatus(int updateUserStatus) {
		this.updateUserStatus = updateUserStatus;
	}

	public int getUpdateUserStatus() {
		return updateUserStatus;
	}

	public void setDeleteUserStatus(int deleteUserStatus) {
		this.deleteUserStatus = deleteUserStatus;
	}

	public int getDeleteUserStatus() {
		return deleteUserStatus;
	}

	public void setForgetPasswordStatus(String forgetPasswordStatus) {
		this.forgetPasswordStatus = forgetPasswordStatus;
	}

	public String getForgetPasswordStatus() {
		return forgetPasswordStatus;
	}

	public void setUpdateRoleStatus(int updateRoleStatus) {
		this.updateRoleStatus = updateRoleStatus;
	}

	public int getUpdateRoleStatus() {
		return updateRoleStatus;
	}

	public void setDeleteRoleStatus(int deleteRoleStatus) {
		this.deleteRoleStatus = deleteRoleStatus;
	}

	public int getDeleteRoleStatus() {
		return deleteRoleStatus;
	}

	public void setCopyPermissionStatus(int copyPermissionStatus) {
		this.copyPermissionStatus = copyPermissionStatus;
	}

	public int getCopyPermissionStatus() {
		return copyPermissionStatus;
	}

	public void setUpdateTeamStatus(int updateTeamStatus) {
		this.updateTeamStatus = updateTeamStatus;
	}

	public int getUpdateTeamStatus() {
		return updateTeamStatus;
	}

	public void setDeleteTeamStatus(int deleteTeamStatus) {
		this.deleteTeamStatus = deleteTeamStatus;
	}

	public int getDeleteTeamStatus() {
		return deleteTeamStatus;
	}

	public void setUpdateCompanyStatus(int updateCompanyStatus) {
		this.updateCompanyStatus = updateCompanyStatus;
	}

	public int getUpdateCompanyStatus() {
		return updateCompanyStatus;
	}

	public void setUpdatePublisherStatus(int updatePublisherStatus) {
		this.updatePublisherStatus = updatePublisherStatus;
	}

	public int getUpdatePublisherStatus() {
		return updatePublisherStatus;
	}

	public void setUpdateDemandPartnerStatus(int updateDemandPartnerStatus) {
		this.updateDemandPartnerStatus = updateDemandPartnerStatus;
	}

	public int getUpdateDemandPartnerStatus() {
		return updateDemandPartnerStatus;
	}

	public void setUpdatePropertyStatus(int updatePropertyStatus) {
		this.updatePropertyStatus = updatePropertyStatus;
	}

	public int getUpdatePropertyStatus() {
		return updatePropertyStatus;
	}

	public void setLinCSV(String linCSV) {
		this.linCSV = linCSV;
	}

	public String getLinCSV() {
		return linCSV;
	}

	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityType() {
		return entityType;
	}


	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}


	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUpdateAccountStatus(int updateAccountStatus) {
		this.updateAccountStatus = updateAccountStatus;
	}

	public int getUpdateAccountStatus() {
		return updateAccountStatus;
	}



}
