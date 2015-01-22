package com.lin.web.service.impl;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AccountsHistObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyHistObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.EmailAuthObj;
import com.lin.server.bean.PropertyChildObj;
import com.lin.server.bean.PropertyHistObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesHistObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsHistObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.LoginDetailsDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.RolesAndAuthorisationDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IMediaPlanService;
import com.lin.web.service.IUserService;
import com.lin.web.servlet.GCStorageUtil;
import com.lin.web.util.ClientLoginAuth;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EmailUtil;
import com.lin.web.util.EncriptionUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;

public class UserService implements IUserService{
	private static final Logger log = Logger.getLogger(LinMobileBusinessService.class.getName());

	@Override
	public Boolean loginAuthentication(LoginDetailsDTO login) throws Exception {
		log.info("inside loginAuthentication of UserService");
		Boolean isAuthenticate = false;
		String emailId=login.getEmailId();
		String password=login.getPassword();
		String encriptedPassword = EncriptionUtil.getEncriptedStrMD5(password);
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		UserDetailsObj userDetails = userDAO.getUserDetails(emailId, encriptedPassword);
		if(userDetails!=null && userDetails.getEmailId()!=null && userDetails.getStatus() != null && userDetails.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])){ 
			if(userDetails.getRole()!=null){
				login.setRoleId(String.valueOf(userDetails.getRole()));
				List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				RolesAndAuthorisation rolesAndAuthorisation = userDAO.getRolesAndAuthorisationByRoleId(userDetails.getRole(), rolesAndAuthorisationList);
				if(rolesAndAuthorisation != null) {
					login.setRoleName(rolesAndAuthorisation.getRoleName().trim());
					if(rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0])) {
						login.setSuperAdmin(true);
					}else {
						login.setSuperAdmin(false);
					}
					if(rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[1])) {
						login.setAdminUser(true);
					}else { 
						login.setAdminUser(false);
					}
				}else {
					return false;
				}
			}else {
				return false;
			}
			login.setIsAuthenticated("true");
			login.setEmailId(userDetails.getEmailId().trim());
			if(userDetails.getId() > 0){
				login.setUserId(userDetails.getId());	
			}
			String userName ="";
			if(userDetails.getUserName()!=null){
				userName = userDetails.getUserName();
			}
			login.setUserName(userName);
			isAuthenticate = true;
			login.setLoginStatus("active");
			if(!login.isSuperAdmin()) {			// if not a superAdmin
				CompanyObj companyObj = userDAO.getNonSuperAdminCompany(userDetails, MemcacheUtil.getAllTeamPropertiesObjList(), MemcacheUtil.getAllCompanyList());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					if(login.isAdminUser() && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) { 	// if Admin user
						login.setPublisherPoolPartner(true);
					}
					if(companyObj.getCompanyLogoURL() != null && companyObj.getCompanyLogoURL().length() > 0) {
						login.setCompanyLogoURL(companyObj.getCompanyLogoURL());
					}
				}
			}
		}else if(userDetails!=null && userDetails.getEmailId()!=null && userDetails.getStatus() != null && userDetails.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[1])){ 
			login.setLoginStatus("inactive");
			login.setEmailId("");
			login.setIsAuthenticated("false");
		}else{
			login.setLoginStatus("invalidUser");
			login.setEmailId("");
			login.setIsAuthenticated("false");
		}
		return isAuthenticate;
	}
	
	@Override
	public Boolean googleLoginAuthentication(LoginDetailsDTO login, String emailId) throws Exception{
		log.info("inside googleLoginAuthentication of UserService");
		Boolean isAuthenticate = false;
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		UserDetailsObj userDetails = userDAO.getUserByEmailId(emailId);
		if(userDetails!=null && userDetails.getEmailId()!=null && userDetails.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])){ 
			if(userDetails.getRole()!=null){
				login.setRoleId(String.valueOf(userDetails.getRole()));
				List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				RolesAndAuthorisation rolesAndAuthorisation = userDAO.getRolesAndAuthorisationByRoleId(userDetails.getRole(), rolesAndAuthorisationList);
				if(rolesAndAuthorisation != null) {
					login.setRoleName(rolesAndAuthorisation.getRoleName().trim());
					if(rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0])) {
						login.setSuperAdmin(true);
					}else {
						login.setSuperAdmin(false);
					}
					if(rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[1])) {
						login.setAdminUser(true);
					}else { 
						login.setAdminUser(false);
					}
				}else {
					return false;
				}
			}else {
				return false;
			}
			login.setIsAuthenticated("true");
			login.setEmailId(userDetails.getEmailId().trim());
			if(userDetails.getId() > 0){
				login.setUserId(userDetails.getId());	
			}
			String userName ="";
			if(userDetails.getUserName()!=null){
				userName = userDetails.getUserName();
			}
			login.setUserName(userName);
			isAuthenticate = true;
			login.setLoginStatus("active");
			if(!login.isSuperAdmin()) {			// if not a superAdmin
				CompanyObj companyObj = userDAO.getNonSuperAdminCompany(userDetails, MemcacheUtil.getAllTeamPropertiesObjList(), MemcacheUtil.getAllCompanyList());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					if(login.isAdminUser() && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) { 	// if Admin user
						login.setPublisherPoolPartner(true);
					}
					if(companyObj.getCompanyLogoURL() != null && companyObj.getCompanyLogoURL().length() > 0) {
						login.setCompanyLogoURL(companyObj.getCompanyLogoURL());
					}
				}
			}
		}
		else if(userDetails!=null && userDetails.getEmailId()!=null && userDetails.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[1])){ 
			login.setLoginStatus("inactive");
			login.setEmailId("");
			login.setIsAuthenticated("false");
		}
		else{
			login.setLoginStatus("UnauthorisedGoogleAccount");
			login.setEmailId("");
			login.setIsAuthenticated("false");
		}
		return isAuthenticate;
	}

	@Override
	public List<CommonDTO> fetchAllActiveRoles(long userId, boolean superAdmin) throws Exception {
		log.info("inside fetchAllActiveRoles of UserService");
		List<CommonDTO> list = new ArrayList<CommonDTO>();
		String companyId = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		if(!superAdmin) {
			List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
			if(companyList != null && companyList.size() > 0) {
				CompanyObj obj = companyList.get(0);
				if(obj != null) {
					companyId = obj.getId()+"";
				}
			}
		}
		List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
				 if(superAdmin && rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
				else if(!superAdmin && rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleType().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[1]) && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && companyId != null && rolesAndAuthorisation.getCompanyId() != null && rolesAndAuthorisation.getCompanyId().trim().equals(companyId.trim())) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
				else if(!superAdmin && rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleType().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[0]) && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && !rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0])) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
			}
		}
		return list;
	}
	
	@Override
	public List<CommonDTO> fetchActiveRolesByCompanyId(String companyId, boolean superAdmin) throws Exception {
		log.info("inside fetchActiveRolesByCompanyId of UserService");
		List<CommonDTO> list = new ArrayList<CommonDTO>();
		List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(companyId != null && rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			companyId = companyId.trim();
			for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
				if(!superAdmin && rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleType().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[0]) && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && !rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0])) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
				else if(superAdmin && rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleType().equals(LinMobileConstants.DEFINED_TYPES[0]) && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
				else if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleType().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[1]) && rolesAndAuthorisation.getRoleStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && rolesAndAuthorisation.getCompanyId() != null && rolesAndAuthorisation.getCompanyId().trim().equals(companyId)) {
					list.add(new CommonDTO(String.valueOf(rolesAndAuthorisation.getId()), rolesAndAuthorisation.getRoleName()));
				}
			}
		}
		return list;
	}
	
	@Override
	public List<AuthorisationTextObj> fetchAllActiveAuthorisationText() throws Exception {
		log.info("inside fetchAllActiveAuthorisationText of UserService");
		List<AuthorisationTextObj> list = new ArrayList<AuthorisationTextObj>();
		IUserDetailsDAO dao = new UserDetailsDAO();		
		List<AuthorisationTextObj> resultList = MemcacheUtil.getAllAuthorisationTextList();	
		if(resultList != null && resultList.size() > 0) {
			for (AuthorisationTextObj authorisationText : resultList) {
				if(authorisationText != null && authorisationText.getAuthorisationTextStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])) {
					list.add(authorisationText);
				}
			}
		}
		return list;
	}

	@Override
	public Boolean createUser(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside createUser of UserService");
		Boolean result = false;
		userDetailsDTO.setReturnStatus("failed");
		IUserDetailsDAO dao = new UserDetailsDAO();
		if (dao.getUserByEmailId(userDetailsDTO.getEmailId()) != null) {
			userDetailsDTO.setReturnStatus("exists"); // email Id exists
			return result;
		}
		UserDetailsObj userDetailsObj = new UserDetailsObj();
				
		userDetailsObj.setEmailId(userDetailsDTO.getEmailId().trim().toLowerCase());
		userDetailsObj.setUserName(userDetailsDTO.getUserName());
		userDetailsObj.setRole(userDetailsDTO.getRole());
		userDetailsObj.setStatus("Pending");
		userDetailsObj.setTimezone(userDetailsDTO.getTimezone());
		userDetailsObj.setReservedStatus(userDetailsDTO.getStatus());
		userDetailsObj.setUserCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
		userDetailsObj.setUserModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
		userDetailsObj.setUserModifiedUserId(userId);
		userDetailsObj.setCreatedByUserId(userId);
		userDetailsObj.setOptEmail(userDetailsDTO.isOptEmail());
		
		if(userDetailsDTO.getSelectedRoleType().trim().equals(LinMobileConstants.ADMINS[1])) {					// if admin
			List<String> teamIdList = new ArrayList<String>();
			if(userDetailsDTO.getAdminTeam() != null && !userDetailsDTO.getAdminTeam().trim().equalsIgnoreCase("")) {
				teamIdList.add(userDetailsDTO.getAdminTeam().trim());
			}
			userDetailsObj.setTeams(teamIdList);
		}
		else if(!userDetailsDTO.getSelectedRoleType().trim().equals(LinMobileConstants.ADMINS[0])) {			// if neither admin nor superAdmin
			List<String> teamIdList = new ArrayList<String>();
			if(userDetailsDTO.getNonAdminTeam() != null && !userDetailsDTO.getNonAdminTeam().trim().equalsIgnoreCase("")) {
				String[] teamIdArray = userDetailsDTO.getNonAdminTeam().split(",");
				for (String teamId : teamIdArray) {
					if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
						teamIdList.clear();
						teamIdList.add(teamId.trim());
						break;
					}
					else if(teamId.startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
						teamIdList.clear();
						teamIdList.add(teamId.trim());
						break;
					}
					else {
						teamIdList.add(teamId.trim());
					}
				}
			}
			userDetailsObj.setTeams(teamIdList);
		}
		userDetailsObj.setId(dao.getMaxCount());
		if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, true, false)) {
			dao.saveObject(userDetailsObj);
			userDetailsDTO.setReturnStatus("mailSentFailed");
			boolean emailStatus = sendActivationMail(userDetailsDTO.getUserName(), userDetailsObj.getId(), userDetailsDTO.getEmailId(), "");
			if(emailStatus) {
				userDetailsDTO.setReturnStatus("success");
			}
			result = true;
		}
		return result;		
	}
	
	public boolean sendActivationMail(String toUserName, long userId, String emailId, String note) {
		log.info("inside sendActivationMail of UserService");
		boolean result = false;
		try {
			/*String uniqueNumber = EncriptionUtil.getEncriptedStrMD5(new Date().toString().replaceAll(" ", ""));
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			emailAuthObj.setRandomNumber(uniqueNumber);
			emailAuthObj.setUserId(userId);
			emailAuthObj.setStatus("unAuthorised");
			IUserDetailsDAO dao = new UserDetailsDAO();
			dao.saveObject(emailAuthObj);*/
			String uniqueNumber = createEmailAuth(userId, "unAuthorised");
			String subject = LinMobileConstants.APPLICATION_NAME+" Account Activation";
			String authenticate_url = LinMobileVariables.AUTHORIZATION_URL+uniqueNumber;
			String messageText = welcomeEmailMsg(toUserName, note, authenticate_url);
			EmailUtil.sendAuthMail(emailId.trim(), LinMobileVariables.SENDER_EMAIL_ADDRESS, subject, messageText, "");
			result = true;
		}
		catch (Exception e) {
			
			log.severe("Exception in sendActivationMail of UserService : "+e.getMessage());
		}
		return result;
	}
	
	public static String createEmailAuth(long userId, String emailStatus) throws DataServiceException {
		String uniqueNumber = EncriptionUtil.getEncriptedStrMD5(new Date().toString().replaceAll(" ", ""));
		EmailAuthObj emailAuthObj = new EmailAuthObj();
		emailAuthObj.setRandomNumber(uniqueNumber);
		emailAuthObj.setUserId(userId);
		emailAuthObj.setStatus(emailStatus);
		IUserDetailsDAO dao = new UserDetailsDAO();
		dao.saveObject(emailAuthObj);
		return uniqueNumber;
	}

	@Override
	public long authorizeEmail(String randomNumber) throws Exception {
		log.info("inside authorizeEmail of UserService");
		long result = -1;
		IUserDetailsDAO dao = new UserDetailsDAO();
		result = dao.getEmailAuthObj(randomNumber, "unAuthorised");
		return result;
	}

	@Override
	public Boolean authorizeUser(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside authorizeUser of UserService");
		Boolean result = false;
		UserDetailsObj userDetailsObj = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		userDetailsObj = dao.getUserById(userDetailsDTO.getId());
		if(userDetailsObj != null && userDetailsObj.getId() > 0) {
			userDetailsDTO.setEmailId(userDetailsObj.getEmailId());
			String status = userDetailsObj.getReservedStatus();
			if(status == null){
				status = LinMobileConstants.STATUS_ARRAY[0];
			}
			userDetailsObj.setPassword(EncriptionUtil.getEncriptedStrMD5(userDetailsDTO.getPassword()));
			userDetailsObj.setStatus(status);
			userDetailsObj.setUserModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			userDetailsObj.setUserModifiedUserId(userDetailsDTO.getId());
			
			result = dao.authorizeUser(userDetailsObj, userDetailsDTO);
		}
		return result;
	}

	@Override
	public void userSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		log.info("inside userSetup of UserService");
		List<UserDetailsObj> selectedUserDetailsObjList = new ArrayList<UserDetailsObj>();
		IUserDetailsDAO dao = new UserDetailsDAO();
		if(superAdmin) {
			List<UserDetailsObj> userDetailsObjList = MemcacheUtil.getAllUsersList();
			if(userDetailsObjList != null && userDetailsObjList.size() > 0) {
				List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				for (UserDetailsObj userDetailsObj : userDetailsObjList) {
					if(userDetailsObj != null && userDetailsObj.getRole() != null) {
						 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
						if(!roleName.trim().equals("")) {
							userDetailsObj.setRole(roleName);
							selectedUserDetailsObjList.add(userDetailsObj);
						}
					}
				}
			}
		}
		else {
			List<String> selectedTeamIdList = new ArrayList<String>();
			List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
			String superAdminRoleId = dao.getSuperAdminRoleId();
			if(superAdminRoleId != null && !superAdminRoleId.trim().equalsIgnoreCase("")) {
				List<String> allSuperAdminUserIdList = dao.getAllSuperAdminUserId(superAdminRoleId);
				if(companyList != null && companyList.size() > 0 && companyList.get(0).getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					String companyId = String.valueOf(companyList.get(0).getId());
					List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					if(teamPropertiesObjList != null && teamPropertiesObjList.size() > 0) {
						for (TeamPropertiesObj teamPropertiesObj : teamPropertiesObjList) {
							if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().trim().equals(companyId)) {
								selectedTeamIdList.add(teamPropertiesObj.getId().trim());
							}
						}
					}
					
					List<UserDetailsObj> userDetailsObjList = MemcacheUtil.getAllUsersList();
					if(userDetailsObjList != null && userDetailsObjList.size() > 0 && selectedTeamIdList != null && selectedTeamIdList.size() > 0) {
						List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
						for (UserDetailsObj userDetailsObj : userDetailsObjList) {
							if(userDetailsObj != null && !allSuperAdminUserIdList.contains(String.valueOf(userDetailsObj.getId())) && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size()  > 0 && selectedTeamIdList.contains(userDetailsObj.getTeams().get(0))) {
								String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole(), rolesAndAuthorisationList);
								if(!roleName.trim().equals("")) {
									userDetailsObj.setRole(roleName);
									selectedUserDetailsObjList.add(userDetailsObj);
								}
							}
						}
					}
				}
			}
		}
		userDetailsDTO.setUsersList(selectedUserDetailsObjList);
	}

	@Override
	public Boolean initEditUser(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside initEditUser of UserService");
		UserDetailsObj userDetailsObj = null;
		Boolean result = false;
		List<CommonDTO> selectedRoleList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedTeamList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedAdminTeamList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedNonAdminTeamList = new ArrayList<CommonDTO>();
		IUserDetailsDAO dao = new UserDetailsDAO();
		userDetailsObj = dao.getUserById(Long.valueOf(userDetailsDTO.getId()));
		if(userDetailsObj != null) {
			List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			userDetailsDTO.setId(userDetailsObj.getId());
			userDetailsDTO.setUserName(userDetailsObj.getUserName());
			userDetailsDTO.setEmailId(userDetailsObj.getEmailId());
			userDetailsDTO.setEmailIdRepeat(userDetailsObj.getEmailId());
			
			if(userDetailsObj.getStatus().equalsIgnoreCase("Pending")) {
				selectedStatusList.add(new CommonDTO(userDetailsObj.getReservedStatus(), ""));
			}
			else {
				selectedStatusList.add(new CommonDTO(userDetailsObj.getStatus(), ""));
			}
			
			String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);						// used to check if superAdmin
			if(!roleName.trim().equals("")) {
				userDetailsDTO.setSelectedRoleType(roleName.trim());
				selectedRoleList.add(new CommonDTO(userDetailsObj.getRole(), ""));
				
				if(userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
					if(roleName.trim().equals(LinMobileConstants.ADMINS[1])) {						// if Admin
						selectedAdminTeamList.add( new CommonDTO(userDetailsObj.getTeams().get(0).trim(), ""));
					}
					else if(!roleName.trim().equals(LinMobileConstants.ADMINS[0])) {				// if neither Admin nor superAdmin
						List<String> teamIdList = userDetailsObj.getTeams();
						for(String teamId : teamIdList) {
							selectedNonAdminTeamList.add( new CommonDTO(teamId.trim(), ""));
						}
					}
					List<String> teamList = userDetailsObj.getTeams();
					if(teamList != null && teamList.size() > 0) {
						List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
						for(String teamId : teamList) {
							TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId, teamPropertiesObjList);
							if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && !teamPropertiesObj.getCompanyId().equals("")) {
								List<CommonDTO> selectedCompanyList = new ArrayList<CommonDTO>();
								selectedCompanyList.add(new CommonDTO(teamPropertiesObj.getCompanyId(), ""));
								userDetailsDTO.setSelectedCompanyList(selectedCompanyList);
								break;
							}
						}
					}
				}
				userDetailsDTO.setSelectedNonAdminTeamList(selectedNonAdminTeamList);
				userDetailsDTO.setSelectedAdminTeamList(selectedAdminTeamList);
				userDetailsDTO.setTimezone(userDetailsObj.getTimezone());
				userDetailsDTO.setSelectedRoleList(selectedRoleList);
				userDetailsDTO.setSelectedStatusList(selectedStatusList);
				userDetailsDTO.setSelectedTeamList(selectedTeamList);
				userDetailsDTO.setOptEmail(userDetailsObj.isOptEmail());
				result = true;
			}
		}
		return result;
	}

	@Override
	public int userUpdate(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside userUpdate of UserService");
		int result = 0;
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> teamIdList = new ArrayList<String>();
		UserDetailsObj userDetailsObj = dao.getUserByEmailId(userDetailsDTO.getEmailId());
		if (userDetailsObj != null && userDetailsDTO.getId() != userDetailsObj.getId()) {
			result = -1;					// emailId belongs to another user
			return result;
		}
		
		userDetailsObj = dao.getUserById(userDetailsDTO.getId());
		if(userDetailsObj != null) {
			maintainUserHistory(userDetailsObj, userId, "Updated");
			
			userDetailsObj.setEmailId(userDetailsDTO.getEmailId().toLowerCase());
			userDetailsObj.setUserName(userDetailsDTO.getUserName());
			userDetailsObj.setOptEmail(userDetailsDTO.isOptEmail());
			if(userDetailsObj.getStatus().equalsIgnoreCase("Pending")) {
				userDetailsObj.setReservedStatus(userDetailsDTO.getStatus());
			}
			else {
				userDetailsObj.setStatus(userDetailsDTO.getStatus());
			}
			userDetailsObj.setTimezone(userDetailsDTO.getTimezone());
			userDetailsObj.setRole(userDetailsDTO.getRole());
			userDetailsObj.setUserModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			userDetailsObj.setUserModifiedUserId(userId);
			
			if(userDetailsDTO.getSelectedRoleType().trim().equals(LinMobileConstants.ADMINS[1])) {					// if admin
				if(userDetailsDTO.getAdminTeam() != null && !userDetailsDTO.getAdminTeam().trim().equalsIgnoreCase("")) {
					teamIdList.add(userDetailsDTO.getAdminTeam().trim());
				}
				userDetailsObj.setTeams(teamIdList);
			}
			else if(!userDetailsDTO.getSelectedRoleType().trim().equals(LinMobileConstants.ADMINS[0])) {			// if neither admin nor superAdmin
				if(userDetailsDTO.getNonAdminTeam() != null && !userDetailsDTO.getNonAdminTeam().trim().equalsIgnoreCase("")) {
					String[] teamIdArray = userDetailsDTO.getNonAdminTeam().split(",");
					for (String teamId : teamIdArray) {
						if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
							teamIdList.clear();
							teamIdList.add(teamId.trim());
							break;
						}
						else if(teamId.startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
							teamIdList.clear();
							teamIdList.add(teamId.trim());
							break;
						}
						else {
							teamIdList.add(teamId.trim());
						}
					}
				}
				userDetailsObj.setTeams(teamIdList);
			}
			
			if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, false, false)) {
				dao.saveObject(userDetailsObj);
				result = 1;
				if(userId == userDetailsObj.getId()) {
					result = -2;
				}
			}
		}			
		return result;
	}
	
	public void maintainUserHistory(UserDetailsObj userDetailsObj,  long updateDeleteByUserId, String historyStatus) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		UserDetailsHistObj userDetailsHistObj = new UserDetailsHistObj();
		
		userDetailsHistObj.setId(userDetailsObj.getId());
		userDetailsHistObj.setEmailId(userDetailsObj.getEmailId());
		userDetailsHistObj.setPassword(userDetailsObj.getPassword());
		userDetailsHistObj.setUserName(userDetailsObj.getUserName());
		userDetailsHistObj.setRole(userDetailsObj.getRole());
		userDetailsHistObj.setStatus(userDetailsObj.getStatus());
		userDetailsHistObj.setTeams(userDetailsObj.getTeams());
		userDetailsHistObj.setUserCreationDate(userDetailsObj.getUserCreationDate());
		userDetailsHistObj.setUserModifiedDate(userDetailsObj.getUserModifiedDate());
		userDetailsHistObj.setUserModifiedUserId(userDetailsObj.getUserModifiedUserId());
		userDetailsHistObj.setHistoryDate(DateUtil.getDateYYYYMMDDHHMMSS());
		userDetailsHistObj.setUpdateDeleteByUserId(updateDeleteByUserId);
		userDetailsHistObj.setHistoryStatus(historyStatus);
		
		dao.saveObject(userDetailsHistObj);		
	}

	@Override
	public int forgetPassword(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside forgetPassword of UserService");
		int result = -1;
		IUserDetailsDAO dao = new UserDetailsDAO();
		
		UserDetailsObj userDetailsObj = dao.getUserByEmailId(userDetailsDTO.getEmailId());
		if(userDetailsObj == null) {
			result = 2;		// user not in system
		}
		else if(userDetailsObj != null && userDetailsObj.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0])) {
			sendForgetPasswordMail(userDetailsObj.getId(), userDetailsObj.getEmailId());
			result = 1;		// active User
		}
		else if(userDetailsObj != null && userDetailsObj.getStatus().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[1])) {
			result = 3;		// Inactive User
		}
		return result;
	}
	
	public String welcomeEmailMsg(String toUserName, String note,String authenticate_url) throws Exception {
		StringBuilder msg = new StringBuilder();
		msg.append("<html lang='en'>")
		.append("<head>")
		.append("</head>")
		.append("<body>")
		.append("<div style='margin:0; margin-top:10px;' >")
		.append("<div style='margin:0;padding:0;' dir='ltr'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;' border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-family:Arial, Helvetica, sans-serif;font-size:12px'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:620px;'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:16px;font-family:Arial, Helvetica, sans-serif; background:#333333;color:#ffffff;font-weight:bold;text-align:left;padding:5px 20px ; border-top-left-radius:10px; border-top-right-radius:10px; -moz-border-radius-topleft:10px; -webkit-border-radius-topleft:10px; -o-border-radius:10px; -ms-border-radius:10px;'>")
		.append("<a style='text-decoration:none' href='#' target='_blank'>")
		.append("<span style='background:#333333;color:#ffffff;font-weight:bold;font-family:Arial, Helvetica, sans-serif;font-size:16px;letter-spacing:-0.03em;text-align:left;vertical-align:baseline'>")
		.append("<img src='"+LinMobileVariables.APPLICATION_URL+LinMobileConstants.APPLICATION_IMAGE_URL+"' width='100'></span></a><span style='vertical-align: 20px; font-size:12px;'><sup style='font-size:8px;'>TM</sup></span><span style='float:right; margin-top:11px; margin-right:56%; font-size:14px;'>Account Activation</span>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("<table cellspacing='0' cellpadding='0' width='620px' style='border-collapse:collapse;width:620px' border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:11px;font-family:Arial, Helvetica, sans-serif;padding:0px; border-left:none;border-right:none;border-top:none;border-bottom:none'>")
		.append("<table cellspacing='0' cellpadding='0' width='620px' style='border-collapse:collapse'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:11px;font-family:Arial, Helvetica, sans-serif;padding:0px;width:620px'>")
		.append("<table cellspacing='0' cellpadding='0' border='0' style='border-collapse:collapse;width:100%; border:1px solid #333;'>")
		.append("<tbody><tr><td style='font-size:11px;font-family:Arial, Helvetica, sans-serif;padding:20px;background-color:#fff;border-left:none;border-right:none;border-top:none;border-bottom:none'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:100%; '><tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-bottom:6px'>Hi "+toUserName+",<br>")
		.append("<br>Welcome to Lin Mobile "+LinMobileConstants.APPLICATION_NAME+"<sup style='font-size:8px;'>TM</sup>, our mobile advertising platform.")
		.append("</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-bottom:15px'>All you need to do is choose a password. It only takes a few seconds and you'll be ready to use "+LinMobileConstants.APPLICATION_NAME+"<sup style='font-size:8px;'>")
		.append("TM</sup>. ")
		.append("</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-top:10px; padding-bottom:10px;border-top:1px solid #e8e8e8'><b>")
		.append("</b>Click here or copy this link into your browser to get started:<br>")
		.append("<br><a href='"+authenticate_url+"' style='color:#3b5998;text-decoration:none' target='_blank'>"+authenticate_url+"</a></td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-top:20px;'><b>")
		.append("</b>Thank you,")
		.append("<br>"+LinMobileConstants.APPLICATION_NAME+"<sup style='font-size:8px;'>TM</sup> Team</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:11px;font-family:Arial, Helvetica, sans-serif;padding:0px;width:620px'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:100%' border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding:7px 20px;background-color:#333333; height:17px; color:#fff;  border-bottom-left-radius:10px; border-bottom-right-radius:10px; -moz-border-radius-bottomleft:10px; -moz-border-radius-bottomright:10px;-webkit-border-radius-bottomleft:10px; -webkit-border-radius-bottomright:10px;'>")
		.append("<table cellspacing='0' cellpadding='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-left:0px'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse'>")
		.append("<tbody>")
		.append("<tr>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</td></tr></tbody></table></div>")
		.append("</div>")
		.append("</div>")
		.append("</div>")
		.append("</body>")
		.append("</html>")
		
        .append(note);
		return msg.toString();
	}

	public String forgetPasswordEmailMsg(String reset_password_url, long userId) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		UserDetailsObj userDetailsObj = new UserDetailsObj();
		try {
			userDetailsObj = dao.getUserById(userId);
		} catch (Exception e) {
			
		}
		String toUserName  = userDetailsObj.getUserName();
		
		StringBuilder msg = new StringBuilder();
		msg.append("<html lang='en'>")
		.append("<head>")
		.append("</head>")
		.append("<body>")
		.append("<div style='margin:0; margin-top:10px;' >")
		.append("<div style='margin:0;padding:0' dir='ltr'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:98%' border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-family:Arial, Helvetica, sans-serif;font-size:12px'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:620px; '>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:16px;font-family:Arial, Helvetica, sans-serif;background:#333333;color:#ffffff;font-weight:bold;vertical-align:baseline;letter-spacing:-0.03em;text-align:left;padding:5px 20px ; border-top-left-radius:10px; border-top-right-radius:10px; -moz-border-radius-topleft:10px; -webkit-border-radius-topleft:10px; -o-border-radius:10px; -ms-border-radius:10px;''>")
		.append("<a style='text-decoration:none' href='#' target='_blank'>")
		.append("<span style='background:#333333;color:#ffffff;font-weight:bold;font-family:Arial, Helvetica, sans-serif;vertical-align:middle;font-size:16px;letter-spacing:-0.03em;text-align:left;vertical-align:baseline'>")
		.append("<img src='"+LinMobileVariables.APPLICATION_URL+LinMobileConstants.APPLICATION_IMAGE_URL+"' width='100'></span></a><span style='vertical-align: 20px; font-size:9px;'><sup>TM</sup></span><span style='float:right; margin-top:11px; margin-right:59%; font-size:14px;'>Password Reset</span>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("<table cellspacing='0' cellpadding='0' width='620px' style='border-collapse:collapse;width:620px'  border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif; padding:0px;background-color:#f2f2f2;border-left:none;border-right:none;border-top:none;border-bottom:none'>")
		.append("<table cellspacing='0' cellpadding='0' width='620px' style='border-collapse:collapse;  '>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding:0px;width:620px'>")
		.append("<table cellspacing='0' cellpadding='0' border='0' style='border-collapse:collapse;width:100%;border:solid 1px #333; '>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding:20px;background-color:#fff;'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:100%;'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-bottom:10px'>Hi "+toUserName+",<br><br>You recently asked to reset your "+LinMobileConstants.APPLICATION_NAME+"<span style=' font-size:10px;'><sup style='font-size:8px;'>TM</sup></span> password.</td></tr><tr>")
		.append("</td></tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-top:6px;border-top:1px solid #e8e8e8'>Click here or copy this link into your browser to get started:")
		.append("<br>")
		.append("<br><a href='"+reset_password_url+"' style='color:#3b5998;text-decoration:none' target='_blank'>"+reset_password_url+"</a>")
		.append("<br><br>If you did not request a password reset, please ignore this message.")
		.append("</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding-top:20px;'><b>")
		.append("</b>Thank you,")
		.append("<br>"+LinMobileConstants.APPLICATION_NAME+"<sup style='font-size:8px;'>TM</sup> Team</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding:0px;width:620px'>")
		.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;width:100%' border='0'>")
		.append("<tbody>")
		.append("<tr>")
		.append("<td style='font-size:12px;font-family:Arial, Helvetica, sans-serif;padding:7px 20px;background-color:#333333; height:17px; color:#fff;  border-bottom-left-radius:10px; border-bottom-right-radius:10px; -moz-border-radius-bottomleft:10px; -moz-border-radius-bottomright:10px;-webkit-border-radius-bottomleft:10px; -webkit-border-radius-bottomright:10px;''>")
		.append("<table cellspacing='0' cellpadding='0'>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</td>")
		.append("</tr>")
		.append("</tbody>")
		.append("</table>")
		.append("</div>")
		.append("</div>")
		.append("</body>")
		.append("</html>");
		return msg.toString();
	}

	public void sendForgetPasswordMail(long userId, String emailId) {
		log.info("inside sendForgetPasswordMail of UserService");
		try {
			/*String uniqueNumber = EncriptionUtil.getEncriptedStrMD5(new Date().toString().replaceAll(" ", ""));
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			emailAuthObj.setRandomNumber(uniqueNumber);
			emailAuthObj.setUserId(userId);
			emailAuthObj.setStatus("forgetPassword");
			IUserDetailsDAO dao = new UserDetailsDAO();
			dao.saveObject(emailAuthObj);*/
			String uniqueNumber = createEmailAuth(userId, "forgetPassword");
			String subject = "Password reset on "+LinMobileConstants.APPLICATION_NAME;
			String reset_password_url = LinMobileVariables.FORGET_PASSWORD_URL+uniqueNumber;
			String messageText = forgetPasswordEmailMsg(reset_password_url, userId);
			
			EmailUtil.sendAuthMail(emailId.trim(), LinMobileVariables.SENDER_EMAIL_ADDRESS, subject, messageText, "");
		}
		catch (Exception e) {
			
			log.info("Exception in sendForgetPasswordMail of UserService");
		}
	}
	
	@Override
	public long forgetPasswordEmail(String randomNumber) throws Exception {
		log.info("inside forgetPasswordEmail of UserService");
		long result = -1;
		IUserDetailsDAO dao = new UserDetailsDAO();
		result = dao.getEmailAuthObj(randomNumber, "forgetPassword");
		return result;
	}

	@Override
	public Boolean resetPassword(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside resetPassword of UserService");
		Boolean result = false;
		UserDetailsObj userDetailsObj = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		userDetailsObj = dao.getUserById(userDetailsDTO.getId());
		if(userDetailsObj != null && userDetailsObj.getId() > 0) {
			maintainUserHistory(userDetailsObj, userDetailsObj.getId(), "forgetPassword");
			userDetailsDTO.setEmailId(userDetailsObj.getEmailId());
			userDetailsObj.setPassword(EncriptionUtil.getEncriptedStrMD5(userDetailsDTO.getPassword()));
			userDetailsObj.setUserModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			userDetailsObj.setUserModifiedUserId(userDetailsDTO.getId());
			
			result = dao.resetPassword(userDetailsObj, userDetailsDTO);
		}
		log.info("UserDetailsDAO : resetPassword, result : "+result); 
		return result;
	}

	@Override
	public Boolean initUserOwnProfileUpdate(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside initUserOwnProfileUpdate of UserService");
		UserDetailsObj userDetailsObj = null;
		Boolean result = false;
		IUserDetailsDAO dao = new UserDetailsDAO();
		userDetailsObj = dao.getUserById(userId);
		if(userDetailsObj != null) {
			userDetailsDTO.setId(userDetailsObj.getId());
			userDetailsDTO.setUserName(userDetailsObj.getUserName());
			userDetailsDTO.setEmailId(userDetailsObj.getEmailId());
			userDetailsDTO.setEmailIdRepeat(userDetailsObj.getEmailId());
			userDetailsDTO.setPassword(LinMobileConstants.DEFAULT_PASSWORD);
			userDetailsDTO.setPasswordRepeat(LinMobileConstants.DEFAULT_PASSWORD);
			userDetailsDTO.setTimezone(userDetailsObj.getTimezone());
			result = true;
		}
		return result;
	}

	@Override
	public Boolean userOwnProfileUpdate(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside userOwnProfileUpdate of UserService");
		Boolean result = false;
		IUserDetailsDAO dao = new UserDetailsDAO();
		UserDetailsObj userDetailsObj = dao.getUserByEmailId(userDetailsDTO.getEmailId());
		if (userDetailsObj != null && userDetailsDTO.getId() != userDetailsObj.getId()) {
			// emailId belongs to an existing user
			return result;
		}
		
		userDetailsObj = dao.getUserById(userDetailsDTO.getId());
		if(userDetailsObj != null) {
			maintainUserHistory(userDetailsObj, userDetailsDTO.getId(), "Updated");
			
			userDetailsObj.setEmailId(userDetailsDTO.getEmailId().toLowerCase());
			userDetailsObj.setUserName(userDetailsDTO.getUserName());
			if(!(userDetailsDTO.getPassword().equals(LinMobileConstants.DEFAULT_PASSWORD))) {
				userDetailsObj.setPassword(EncriptionUtil.getEncriptedStrMD5(userDetailsDTO.getPassword().trim()));
			}
			userDetailsObj.setUserModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			userDetailsObj.setUserModifiedUserId(userDetailsDTO.getId());
			userDetailsObj.setTimezone(userDetailsDTO.getTimezone());
			
			if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, false, false)) {
				dao.saveObject(userDetailsObj);
				result = true;
			}
		}			
		return result;
	}
	
	@Override
	public void roleSetup(UserDetailsDTO userDetailsDTO, long userId, boolean isSuperAdmin) throws Exception {
		log.info("inside roleSetup of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<RolesAndAuthorisationDTO> rolesAndAuthorisationDTOList = new ArrayList<RolesAndAuthorisationDTO>();
		List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			RolesAndAuthorisationDTO rolesAndAuthorisationDTO = null;
			if(isSuperAdmin) {
				for(RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
					rolesAndAuthorisationDTO = new RolesAndAuthorisationDTO();
					rolesAndAuthorisationDTO.setId(rolesAndAuthorisation.getId());
					rolesAndAuthorisationDTO.setRoleName(rolesAndAuthorisation.getRoleName());
					rolesAndAuthorisationDTO.setRoleDescription(rolesAndAuthorisation.getRoleDescription());
					rolesAndAuthorisationDTO.setRoleStatus(rolesAndAuthorisation.getRoleStatus());
					rolesAndAuthorisationDTO.setRoleType(rolesAndAuthorisation.getRoleType());
					rolesAndAuthorisationDTOList.add(rolesAndAuthorisationDTO);
				}
			}
			else {
				
				List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
				if(companyList != null && companyList.size() > 0 && companyList.get(0).getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					String companyId = String.valueOf(companyList.get(0).getId());
					for(RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
						if(rolesAndAuthorisation != null 
								&& ((rolesAndAuthorisation.getRoleType().trim().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[0]) && !rolesAndAuthorisation.getRoleName().trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0]))
								|| (rolesAndAuthorisation.getRoleType().trim().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[1]) && rolesAndAuthorisation.getCompanyId() != null && rolesAndAuthorisation.getCompanyId().trim().equals(companyId)))) {							
							rolesAndAuthorisationDTO = new RolesAndAuthorisationDTO();
							rolesAndAuthorisationDTO.setId(rolesAndAuthorisation.getId());
							rolesAndAuthorisationDTO.setRoleName(rolesAndAuthorisation.getRoleName());
							rolesAndAuthorisationDTO.setRoleDescription(rolesAndAuthorisation.getRoleDescription());
							rolesAndAuthorisationDTO.setRoleStatus(rolesAndAuthorisation.getRoleStatus());
							rolesAndAuthorisationDTO.setRoleType(rolesAndAuthorisation.getRoleType());
							rolesAndAuthorisationDTOList.add(rolesAndAuthorisationDTO);
						}
					}
				}
			}
		}
		userDetailsDTO.setRolesAndAuthorisationDTOList(rolesAndAuthorisationDTOList);
		
	}

	@Override
	public Boolean createRole(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		log.info("inside createRole of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false; 
		
		RolesAndAuthorisation rolesAndAuthorisation = new RolesAndAuthorisation();
		
		rolesAndAuthorisation.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
		rolesAndAuthorisation.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
		rolesAndAuthorisation.setLastModifiedById(userId);
		rolesAndAuthorisation.setCreatedById(userId);
		rolesAndAuthorisation.setRoleDescription(userDetailsDTO.getRoleDescription());
		rolesAndAuthorisation.setRoleStatus(LinMobileConstants.STATUS_ARRAY[0]);
		rolesAndAuthorisation.setRoleName(userDetailsDTO.getRoleName());
		rolesAndAuthorisation.setRoleType(LinMobileConstants.DEFINED_TYPES[1]);
		if(superAdmin) {
			rolesAndAuthorisation.setCompanyId(userDetailsDTO.getCompanyId());
		}
		else {
			List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
			if(companyList != null && companyList.size() > 0 && companyList.get(0).getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
				rolesAndAuthorisation.setCompanyId(String.valueOf(companyList.get(0).getId()));
			}
		}		
		rolesAndAuthorisation.setA1(userDetailsDTO.getA1());
		rolesAndAuthorisation.setA2(userDetailsDTO.getA2());
		rolesAndAuthorisation.setA3(userDetailsDTO.getA3());
		rolesAndAuthorisation.setA4(userDetailsDTO.getA4());
		rolesAndAuthorisation.setA5(userDetailsDTO.getA5());
		rolesAndAuthorisation.setA6(userDetailsDTO.getA6());
		rolesAndAuthorisation.setA7(userDetailsDTO.getA7());
		rolesAndAuthorisation.setA8(userDetailsDTO.getA8());
		rolesAndAuthorisation.setA9(userDetailsDTO.getA9());
		rolesAndAuthorisation.setA10(userDetailsDTO.getA10());
		rolesAndAuthorisation.setA11(userDetailsDTO.getA11());
		rolesAndAuthorisation.setA12(userDetailsDTO.getA12());
		rolesAndAuthorisation.setA13(userDetailsDTO.getA13());
		rolesAndAuthorisation.setA14(userDetailsDTO.getA14());
		rolesAndAuthorisation.setA15(userDetailsDTO.getA15());
		rolesAndAuthorisation.setA16(userDetailsDTO.getA16());
		rolesAndAuthorisation.setA17(userDetailsDTO.getA17());
		rolesAndAuthorisation.setA18(userDetailsDTO.getA18());
		rolesAndAuthorisation.setA19(userDetailsDTO.getA19());
		rolesAndAuthorisation.setA20(userDetailsDTO.getA20());
		rolesAndAuthorisation.setA21(userDetailsDTO.getA21());
		rolesAndAuthorisation.setA22(userDetailsDTO.getA22());
		rolesAndAuthorisation.setA23(userDetailsDTO.getA23());
		rolesAndAuthorisation.setA24(userDetailsDTO.getA24());
		rolesAndAuthorisation.setA25(userDetailsDTO.getA25());
		rolesAndAuthorisation.setA26(userDetailsDTO.getA26());
		rolesAndAuthorisation.setA27(userDetailsDTO.getA27());
		rolesAndAuthorisation.setA28(userDetailsDTO.getA28());
		rolesAndAuthorisation.setA29(userDetailsDTO.getA29());
		rolesAndAuthorisation.setA30(userDetailsDTO.getA30());
		rolesAndAuthorisation.setA31(userDetailsDTO.getA31());
		rolesAndAuthorisation.setA32(userDetailsDTO.getA32());
		rolesAndAuthorisation.setA33(userDetailsDTO.getA33());
		rolesAndAuthorisation.setA34(userDetailsDTO.getA34());
		rolesAndAuthorisation.setA35(userDetailsDTO.getA35());
		rolesAndAuthorisation.setA36(userDetailsDTO.getA36());
		rolesAndAuthorisation.setA37(userDetailsDTO.getA37());
		rolesAndAuthorisation.setA38(userDetailsDTO.getA38());
		rolesAndAuthorisation.setA39(userDetailsDTO.getA39());
		rolesAndAuthorisation.setA40(userDetailsDTO.getA40());
		rolesAndAuthorisation.setA41(userDetailsDTO.getA41());
		rolesAndAuthorisation.setA42(userDetailsDTO.getA42());
		rolesAndAuthorisation.setA43(userDetailsDTO.getA43());
		rolesAndAuthorisation.setA44(userDetailsDTO.getA44());
		rolesAndAuthorisation.setA45(userDetailsDTO.getA45());
		rolesAndAuthorisation.setA46(userDetailsDTO.getA46());
		rolesAndAuthorisation.setA47(userDetailsDTO.getA47());
		rolesAndAuthorisation.setA48(userDetailsDTO.getA48());
		rolesAndAuthorisation.setA49(userDetailsDTO.getA49());
		rolesAndAuthorisation.setA50(userDetailsDTO.getA50());
		rolesAndAuthorisation.setA51(userDetailsDTO.getA51());
		rolesAndAuthorisation.setA52(userDetailsDTO.getA52());
		rolesAndAuthorisation.setA53(userDetailsDTO.getA53());
		rolesAndAuthorisation.setA54(userDetailsDTO.getA54());
		rolesAndAuthorisation.setA55(userDetailsDTO.getA55());
		rolesAndAuthorisation.setA56(userDetailsDTO.getA56());
		rolesAndAuthorisation.setA57(userDetailsDTO.getA57());
		rolesAndAuthorisation.setA58(userDetailsDTO.getA58());
		rolesAndAuthorisation.setA59(userDetailsDTO.getA59());
		rolesAndAuthorisation.setA60(userDetailsDTO.getA60());
		rolesAndAuthorisation.setA61(userDetailsDTO.getA61());
		rolesAndAuthorisation.setA62(userDetailsDTO.getA62());
		rolesAndAuthorisation.setA63(userDetailsDTO.getA63());
		rolesAndAuthorisation.setA64(userDetailsDTO.getA64());
		rolesAndAuthorisation.setA65(userDetailsDTO.getA65());
		rolesAndAuthorisation.setA66(userDetailsDTO.getA66());
		rolesAndAuthorisation.setA67(userDetailsDTO.getA67());
		rolesAndAuthorisation.setA68(userDetailsDTO.getA68());
		rolesAndAuthorisation.setA69(userDetailsDTO.getA69());
		rolesAndAuthorisation.setA70(userDetailsDTO.getA70());
		rolesAndAuthorisation.setA71(userDetailsDTO.getA71());
		rolesAndAuthorisation.setA72(userDetailsDTO.getA72());
		rolesAndAuthorisation.setA73(userDetailsDTO.getA73());
		rolesAndAuthorisation.setA74(userDetailsDTO.getA74());
		rolesAndAuthorisation.setA75(userDetailsDTO.getA75());
		rolesAndAuthorisation.setA76(userDetailsDTO.getA76());
		rolesAndAuthorisation.setA77(userDetailsDTO.getA77());
		rolesAndAuthorisation.setA78(userDetailsDTO.getA78());
		rolesAndAuthorisation.setA79(userDetailsDTO.getA79());
		rolesAndAuthorisation.setA80(userDetailsDTO.getA80());
		rolesAndAuthorisation.setA81(userDetailsDTO.getA81());
		rolesAndAuthorisation.setA82(userDetailsDTO.getA82());
		rolesAndAuthorisation.setA83(userDetailsDTO.getA83());
		rolesAndAuthorisation.setA84(userDetailsDTO.getA84());
		rolesAndAuthorisation.setA85(userDetailsDTO.getA85());
		rolesAndAuthorisation.setA86(userDetailsDTO.getA86());
		rolesAndAuthorisation.setA87(userDetailsDTO.getA87());
		rolesAndAuthorisation.setA88(userDetailsDTO.getA88());
		rolesAndAuthorisation.setA89(userDetailsDTO.getA89());
		rolesAndAuthorisation.setA90(userDetailsDTO.getA90());
		rolesAndAuthorisation.setA91(userDetailsDTO.getA91());
		rolesAndAuthorisation.setA92(userDetailsDTO.getA92());
		rolesAndAuthorisation.setA93(userDetailsDTO.getA93());
		rolesAndAuthorisation.setA94(userDetailsDTO.getA94());
		rolesAndAuthorisation.setA95(userDetailsDTO.getA95());
		rolesAndAuthorisation.setA96(userDetailsDTO.getA96());
		rolesAndAuthorisation.setA97(userDetailsDTO.getA97());
		rolesAndAuthorisation.setA98(userDetailsDTO.getA98());
		rolesAndAuthorisation.setA99(userDetailsDTO.getA99());
		rolesAndAuthorisation.setA100(userDetailsDTO.getA100());
		
		rolesAndAuthorisation.setId(dao.getMaxCount());
		if(MemcacheUtil.selfUpdateMemcacheRolesAndAuthorisation(rolesAndAuthorisation, true)) {
			dao.saveObject(rolesAndAuthorisation);
			result = true;
		}
		userDetailsDTO.setRoleName("");
		userDetailsDTO.setRoleDescription("");
					
		return result;
	}

	@Override
	public Boolean initEditRole(UserDetailsDTO userDetailsDTO, boolean superAdmin) throws Exception {
		log.info("inside initEditRole of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		List<AuthorisationTextObj> authorisationTextList = null;
		List<RolesAndAuthorisation> rolesAndAuthorisationList = null;
		RolesAndAuthorisation rolesAndAuthorisation = null;
		rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() >0) {
			for (RolesAndAuthorisation obj : rolesAndAuthorisationList) {
				if(String.valueOf(obj.getId()).equals(userDetailsDTO.getRoleId().trim()) && obj.getRoleStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					rolesAndAuthorisation = obj;
					break;
				}
			}
		}
		if(rolesAndAuthorisation != null) {
			userDetailsDTO.setRoleDescription(rolesAndAuthorisation.getRoleDescription());
			authorisationTextList = MemcacheUtil.getAllAuthorisationTextList();
			if(rolesAndAuthorisation != null && authorisationTextList != null && authorisationTextList.size() >0) {
				userDetailsDTO.setRoleId(String.valueOf(rolesAndAuthorisation.getId()));
				userDetailsDTO.setRoleName(rolesAndAuthorisation.getRoleName().trim());
				userDetailsDTO.setRoleType(rolesAndAuthorisation.getRoleType());
				List<CommonDTO> selectedCompanyList = new ArrayList<CommonDTO>();
				if(rolesAndAuthorisation.getCompanyId() != null) {
					selectedCompanyList.add(new CommonDTO(rolesAndAuthorisation.getCompanyId().trim(), ""));
				}
				userDetailsDTO.setSelectedCompanyList(selectedCompanyList);
				List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
				selectedStatusList.add(new CommonDTO(rolesAndAuthorisation.getRoleStatus(), rolesAndAuthorisation.getRoleStatus()));
				userDetailsDTO.setSelectedStatusList(selectedStatusList);
				
				setAuthorisations(authorisationTextList, rolesAndAuthorisation);
				userDetailsDTO.setAuthorisationTextList(authorisationTextList);
				result = true;
			}
		}
		return result;
	}
	
	public void setAuthorisations(List<AuthorisationTextObj> authorisationTextList, RolesAndAuthorisation rolesAndAuthorisation) {
		for (AuthorisationTextObj authorisationText : authorisationTextList) {
			if(authorisationText.getAuthorisationTextStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
				if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A1")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA1());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A2")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA2());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A3")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA3());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A4")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA4());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A5")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA5());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A6")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA6());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A7")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA7());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A8")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA8());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A9")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA9());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A10")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA10());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A11")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA11());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A12")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA12());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A13")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA13());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A14")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA14());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A15")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA15());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A16")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA16());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A17")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA17());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A18")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA18());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A19")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA19());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A20")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA20());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A21")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA21());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A22")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA22());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A23")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA23());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A24")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA24());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A25")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA25());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A26")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA26());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A27")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA27());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A28")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA28());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A29")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA29());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A30")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA30());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A31")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA31());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A32")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA32());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A33")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA33());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A34")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA34());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A35")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA35());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A36")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA36());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A37")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA37());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A38")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA38());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A39")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA39());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A40")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA40());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A41")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA41());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A42")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA42());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A43")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA43());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A44")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA44());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A45")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA45());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A46")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA46());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A47")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA47());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A48")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA48());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A49")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA49());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A50")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA50());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A51")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA51());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A52")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA52());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A53")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA53());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A54")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA54());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A55")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA55());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A56")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA56());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A57")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA57());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A58")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA58());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A59")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA59());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A60")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA60());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A61")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA61());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A62")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA62());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A63")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA63());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A64")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA64());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A65")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA65());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A66")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA66());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A67")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA67());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A68")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA68());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A69")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA69());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A70")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA70());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A71")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA71());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A72")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA72());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A73")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA73());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A74")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA74());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A75")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA75());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A76")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA76());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A77")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA77());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A78")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA78());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A79")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA79());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A80")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA80());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A81")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA81());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A82")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA82());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A83")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA83());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A84")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA84());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A85")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA85());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A86")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA86());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A87")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA87());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A88")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA88());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A89")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA89());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A90")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA90());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A91")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA91());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A92")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA92());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A93")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA93());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A94")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA94());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A95")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA95());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A96")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA96());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A97")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA97());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A98")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA98());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A99")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA99());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A100")) {
					authorisationText.setPermission(rolesAndAuthorisation.getA100());
				}
			}
		}
	}

	@Override
	public Boolean updateRole(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside updateRole of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		
		List<RolesAndAuthorisation> rolesAndAuthorisationList = null;
		RolesAndAuthorisation rolesAndAuthorisation = null;
		rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() >0) {
			for (RolesAndAuthorisation obj : rolesAndAuthorisationList) {
				if(String.valueOf(obj.getId()).equals(userDetailsDTO.getRoleId().trim()) && obj.getRoleStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					rolesAndAuthorisation = obj;
					break;
				}
			}
		}
		if(rolesAndAuthorisation != null) {
			rolesAndAuthorisation.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			rolesAndAuthorisation.setLastModifiedById(userId);
			rolesAndAuthorisation.setRoleDescription(userDetailsDTO.getRoleDescription());
			//rolesAndAuthorisation.setRoleStatus("Active");
			rolesAndAuthorisation.setRoleName(userDetailsDTO.getRoleName());
			if(rolesAndAuthorisation.getRoleType().trim().equalsIgnoreCase(LinMobileConstants.DEFINED_TYPES[1])) {
				rolesAndAuthorisation.setRoleStatus(userDetailsDTO.getRoleStatus());
			}
			if(userDetailsDTO.getCompanyId() != null) {
				rolesAndAuthorisation.setCompanyId(userDetailsDTO.getCompanyId());
			}
			else {
				List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
				if(companyList != null && companyList.size() > 0 && companyList.get(0).getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					rolesAndAuthorisation.setCompanyId(String.valueOf(companyList.get(0).getId()));
				}
			}
			rolesAndAuthorisation.setA1(userDetailsDTO.getA1());
			rolesAndAuthorisation.setA2(userDetailsDTO.getA2());
			rolesAndAuthorisation.setA3(userDetailsDTO.getA3());
			rolesAndAuthorisation.setA4(userDetailsDTO.getA4());
			rolesAndAuthorisation.setA5(userDetailsDTO.getA5());
			rolesAndAuthorisation.setA6(userDetailsDTO.getA6());
			rolesAndAuthorisation.setA7(userDetailsDTO.getA7());
			rolesAndAuthorisation.setA8(userDetailsDTO.getA8());
			rolesAndAuthorisation.setA9(userDetailsDTO.getA9());
			rolesAndAuthorisation.setA10(userDetailsDTO.getA10());
			rolesAndAuthorisation.setA11(userDetailsDTO.getA11());
			rolesAndAuthorisation.setA12(userDetailsDTO.getA12());
			rolesAndAuthorisation.setA13(userDetailsDTO.getA13());
			rolesAndAuthorisation.setA14(userDetailsDTO.getA14());
			rolesAndAuthorisation.setA15(userDetailsDTO.getA15());
			rolesAndAuthorisation.setA16(userDetailsDTO.getA16());
			rolesAndAuthorisation.setA17(userDetailsDTO.getA17());
			rolesAndAuthorisation.setA18(userDetailsDTO.getA18());
			rolesAndAuthorisation.setA19(userDetailsDTO.getA19());
			rolesAndAuthorisation.setA20(userDetailsDTO.getA20());
			rolesAndAuthorisation.setA21(userDetailsDTO.getA21());
			rolesAndAuthorisation.setA22(userDetailsDTO.getA22());
			rolesAndAuthorisation.setA23(userDetailsDTO.getA23());
			rolesAndAuthorisation.setA24(userDetailsDTO.getA24());
			rolesAndAuthorisation.setA25(userDetailsDTO.getA25());
			rolesAndAuthorisation.setA26(userDetailsDTO.getA26());
			rolesAndAuthorisation.setA27(userDetailsDTO.getA27());
			rolesAndAuthorisation.setA28(userDetailsDTO.getA28());
			rolesAndAuthorisation.setA29(userDetailsDTO.getA29());
			rolesAndAuthorisation.setA30(userDetailsDTO.getA30());
			rolesAndAuthorisation.setA31(userDetailsDTO.getA31());
			rolesAndAuthorisation.setA32(userDetailsDTO.getA32());
			rolesAndAuthorisation.setA33(userDetailsDTO.getA33());
			rolesAndAuthorisation.setA34(userDetailsDTO.getA34());
			rolesAndAuthorisation.setA35(userDetailsDTO.getA35());
			rolesAndAuthorisation.setA36(userDetailsDTO.getA36());
			rolesAndAuthorisation.setA37(userDetailsDTO.getA37());
			rolesAndAuthorisation.setA38(userDetailsDTO.getA38());
			rolesAndAuthorisation.setA39(userDetailsDTO.getA39());
			rolesAndAuthorisation.setA40(userDetailsDTO.getA40());
			rolesAndAuthorisation.setA41(userDetailsDTO.getA41());
			rolesAndAuthorisation.setA42(userDetailsDTO.getA42());
			rolesAndAuthorisation.setA43(userDetailsDTO.getA43());
			rolesAndAuthorisation.setA44(userDetailsDTO.getA44());
			rolesAndAuthorisation.setA45(userDetailsDTO.getA45());
			rolesAndAuthorisation.setA46(userDetailsDTO.getA46());
			rolesAndAuthorisation.setA47(userDetailsDTO.getA47());
			rolesAndAuthorisation.setA48(userDetailsDTO.getA48());
			rolesAndAuthorisation.setA49(userDetailsDTO.getA49());
			rolesAndAuthorisation.setA50(userDetailsDTO.getA50());
			rolesAndAuthorisation.setA51(userDetailsDTO.getA51());
			rolesAndAuthorisation.setA52(userDetailsDTO.getA52());
			rolesAndAuthorisation.setA53(userDetailsDTO.getA53());
			rolesAndAuthorisation.setA54(userDetailsDTO.getA54());
			rolesAndAuthorisation.setA55(userDetailsDTO.getA55());
			rolesAndAuthorisation.setA56(userDetailsDTO.getA56());
			rolesAndAuthorisation.setA57(userDetailsDTO.getA57());
			rolesAndAuthorisation.setA58(userDetailsDTO.getA58());
			rolesAndAuthorisation.setA59(userDetailsDTO.getA59());
			rolesAndAuthorisation.setA60(userDetailsDTO.getA60());
			rolesAndAuthorisation.setA61(userDetailsDTO.getA61());
			rolesAndAuthorisation.setA62(userDetailsDTO.getA62());
			rolesAndAuthorisation.setA63(userDetailsDTO.getA63());
			rolesAndAuthorisation.setA64(userDetailsDTO.getA64());
			rolesAndAuthorisation.setA65(userDetailsDTO.getA65());
			rolesAndAuthorisation.setA66(userDetailsDTO.getA66());
			rolesAndAuthorisation.setA67(userDetailsDTO.getA67());
			rolesAndAuthorisation.setA68(userDetailsDTO.getA68());
			rolesAndAuthorisation.setA69(userDetailsDTO.getA69());
			rolesAndAuthorisation.setA70(userDetailsDTO.getA70());
			rolesAndAuthorisation.setA71(userDetailsDTO.getA71());
			rolesAndAuthorisation.setA72(userDetailsDTO.getA72());
			rolesAndAuthorisation.setA73(userDetailsDTO.getA73());
			rolesAndAuthorisation.setA74(userDetailsDTO.getA74());
			rolesAndAuthorisation.setA75(userDetailsDTO.getA75());
			rolesAndAuthorisation.setA76(userDetailsDTO.getA76());
			rolesAndAuthorisation.setA77(userDetailsDTO.getA77());
			rolesAndAuthorisation.setA78(userDetailsDTO.getA78());
			rolesAndAuthorisation.setA79(userDetailsDTO.getA79());
			rolesAndAuthorisation.setA80(userDetailsDTO.getA80());
			rolesAndAuthorisation.setA81(userDetailsDTO.getA81());
			rolesAndAuthorisation.setA82(userDetailsDTO.getA82());
			rolesAndAuthorisation.setA83(userDetailsDTO.getA83());
			rolesAndAuthorisation.setA84(userDetailsDTO.getA84());
			rolesAndAuthorisation.setA85(userDetailsDTO.getA85());
			rolesAndAuthorisation.setA86(userDetailsDTO.getA86());
			rolesAndAuthorisation.setA87(userDetailsDTO.getA87());
			rolesAndAuthorisation.setA88(userDetailsDTO.getA88());
			rolesAndAuthorisation.setA89(userDetailsDTO.getA89());
			rolesAndAuthorisation.setA90(userDetailsDTO.getA90());
			rolesAndAuthorisation.setA91(userDetailsDTO.getA91());
			rolesAndAuthorisation.setA92(userDetailsDTO.getA92());
			rolesAndAuthorisation.setA93(userDetailsDTO.getA93());
			rolesAndAuthorisation.setA94(userDetailsDTO.getA94());
			rolesAndAuthorisation.setA95(userDetailsDTO.getA95());
			rolesAndAuthorisation.setA96(userDetailsDTO.getA96());
			rolesAndAuthorisation.setA97(userDetailsDTO.getA97());
			rolesAndAuthorisation.setA98(userDetailsDTO.getA98());
			rolesAndAuthorisation.setA99(userDetailsDTO.getA99());
			rolesAndAuthorisation.setA100(userDetailsDTO.getA100());
			
			if(MemcacheUtil.selfUpdateMemcacheRolesAndAuthorisation(rolesAndAuthorisation, false)) {
				dao.saveObject(rolesAndAuthorisation);
				result = true;
			}
		}	
					
		return result;
	}

	@Override
	public Boolean copyRole(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside copyRole of UserService");
		Boolean result = false;
		List<AuthorisationTextObj> authorisationTextList = null;
		List<RolesAndAuthorisation> rolesAndAuthorisationList = null;
		RolesAndAuthorisation rolesAndAuthorisation = null;
		List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedCompanyList = new ArrayList<CommonDTO>();
		
		if(userDetailsDTO.getRoleStatus() != null) {
			selectedStatusList.add(new CommonDTO(userDetailsDTO.getRoleStatus(), userDetailsDTO.getRoleStatus()));
			userDetailsDTO.setSelectedStatusList(selectedStatusList);
		}
		if(userDetailsDTO.getCompanyId() != null) {
			selectedCompanyList.add(new CommonDTO(userDetailsDTO.getCompanyId(), ""));
			userDetailsDTO.setSelectedCompanyList(selectedCompanyList);
		}
		rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() >0) {
			for (RolesAndAuthorisation obj : rolesAndAuthorisationList) {
				if(String.valueOf(obj.getId()).equals(userDetailsDTO.getRole().trim()) && obj.getRoleStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					rolesAndAuthorisation = obj;
					break;
				}
			}
		}
		if(rolesAndAuthorisation != null) {
			authorisationTextList = MemcacheUtil.getAllAuthorisationTextList();
			if(authorisationTextList != null && authorisationTextList.size() >0) {
				setAuthorisations(authorisationTextList, rolesAndAuthorisation);
				userDetailsDTO.setAuthorisationTextList(authorisationTextList);
				result = true;
			}
		}
		return result;
	}
	
	@Override
	public void teamSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		log.info("inside teamSetup of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<TeamPropertiesObj> teamSetupTeamPropertiesObjList = new ArrayList<TeamPropertiesObj>();
		List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
		if(superAdmin) {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			if(teamPropertiesObjList!= null && teamPropertiesObjList.size() > 0) {
				for (TeamPropertiesObj teamPropertiesObj : teamPropertiesObjList) {
					if(teamPropertiesObj != null) {
						teamSetupTeamPropertiesObjList.add(teamPropertiesObj);
					}
				}
			}
		}
		else {
			List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
			if(teamPropertiesObjList!= null && teamPropertiesObjList.size() > 0 && companyList != null && companyList.size() > 0) {
				CompanyObj companyObj = companyList.get(0);
				String status = companyObj.getStatus();
				String companyId = companyObj.getId()+"";
				if(status.equals(LinMobileConstants.STATUS_ARRAY[0]) && !companyId.equals("")) {
					for (TeamPropertiesObj teamPropertiesObj : teamPropertiesObjList) {
						if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().trim().equals(companyId)) {
							teamSetupTeamPropertiesObjList.add(teamPropertiesObj);
						}
					}
				}
			}
		}
		userDetailsDTO.setTeamPropertiesObjList(teamSetupTeamPropertiesObjList);
	}
	
	public String getCommaSeperatedIdValues(List<CommonDTO> list) {
		log.info("inside getCommaSeperatedIdValues of UserService");
		String commaSeperatedIds ="";
		if(list != null && list.size() > 0) {
			for (CommonDTO commonDTO : list) {
				commaSeperatedIds = commaSeperatedIds + commonDTO.getId().trim() + ",";
			}
		}
		if(commaSeperatedIds != null && commaSeperatedIds.lastIndexOf(",") != -1) {
			commaSeperatedIds = commaSeperatedIds.substring(0, commaSeperatedIds.lastIndexOf(","));
		}
		return commaSeperatedIds;
	}
	
	@Override
	public List<String> CommaSeperatedStringToList(String commaSeperatedString) {
		log.info("inside CommaSeperatedStringToList of UserService");
		List<String> tempList = new ArrayList<String>();
		if(commaSeperatedString != null && !commaSeperatedString.trim().equals("")) {
			String[] stringArray = commaSeperatedString.trim().split(",");
			int length = stringArray.length;
			if(length > 0) {
				for (int i = 0; i < stringArray.length; i++) {
					String tempString = stringArray[i];
					if(tempString != null) {
						tempList.add(tempString.trim());
					}
				}
			}
		}
		log.info("tempList size :  "+tempList.size());
		return tempList;
	}
	
	@Override
	public Boolean createTeam(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false; 
		List<String> propertyIdList =  new ArrayList<String>();
		List<String> agencyList = new ArrayList<String>();
		List<String> advertiserList = new ArrayList<String>();
		List<String> appViewList = new ArrayList<String>();
		TeamPropertiesObj teamPropertiesObj = new TeamPropertiesObj();
		
		teamPropertiesObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
		teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
		teamPropertiesObj.setLastModifiedByUserId(userId);
		teamPropertiesObj.setCreatedByUserId(userId);
		teamPropertiesObj.setTeamName(userDetailsDTO.getTeamName().trim());
		teamPropertiesObj.setTeamType(LinMobileConstants.DEFINED_TYPES[1]);
		teamPropertiesObj.setTeamDescription(userDetailsDTO.getTeamDescription().trim());
		teamPropertiesObj.setTeamStatus(LinMobileConstants.STATUS_ARRAY[0]);
		
		if(userDetailsDTO.getCompanyId() != null) {
			teamPropertiesObj.setCompanyId(userDetailsDTO.getCompanyId().trim());
		}
		else {
			teamPropertiesObj.setCompanyId(userDetailsDTO.getCompanyIDOfSessionAdminUser().trim());
		}
		
		if(userDetailsDTO.getAppViews() != null) {
			appViewList.addAll(CommaSeperatedStringToList(userDetailsDTO.getAppViews()));
		}
		
		//Publisher Pool Partner Company
		if(userDetailsDTO.getSelectedCompanyType().trim().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccessToProperties() != null && userDetailsDTO.getAccessToProperties().equals("1")) {
			if(userDetailsDTO.getAllPropertiesFlag() != null && userDetailsDTO.getAllPropertiesFlag().equals("1")) {
				propertyIdList.add(LinMobileConstants.ALL_PROPERTIES);
			}
			else if(userDetailsDTO.getProperties() != null) {
				propertyIdList.addAll(CommaSeperatedStringToList(userDetailsDTO.getProperties()));
			}
		}
		
		if(userDetailsDTO.getAccessToAccounts() != null && userDetailsDTO.getAccessToAccounts().equals("1")) {
			if(userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("1")) {
				createAdvertiserAndAgencyList(userDetailsDTO.getAccounts(), agencyList, advertiserList);
			}
			if(userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("2")) {
				agencyList.add(LinMobileConstants.ALL_AGENCIES);
				advertiserList.add(LinMobileConstants.ALL_ADVERTISERS);
			}
			if(userDetailsDTO.getSelectedCompanyType().trim().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("3")) {
				agencyList.add(LinMobileConstants.NO_RESTRICTIONS);
				advertiserList.add(LinMobileConstants.NO_RESTRICTIONS);
			}
		}
		
		teamPropertiesObj.setAppViews(appViewList);
		teamPropertiesObj.setPropertyId(propertyIdList);
		teamPropertiesObj.setAgencyId(agencyList);
		teamPropertiesObj.setAdvertiserId(advertiserList);
		
		String uniqueNumber = String.valueOf(dao.getMaxCount());
		teamPropertiesObj.setId(uniqueNumber);
		if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, true)) {
			dao.saveObject(teamPropertiesObj);
			result = true;
		}
		return result;
	}

	public void createAdvertiserAndAgencyList(String CommaSeperatedAccountString, List<String> agencyList, List<String> advertiserList) {
		if(CommaSeperatedAccountString != null && !CommaSeperatedAccountString.trim().equals("")) {
			CommaSeperatedAccountString = CommaSeperatedAccountString.trim();
			String[] stringArray = CommaSeperatedAccountString.split(",");
			for (int i = 0; i < stringArray.length; i++) {
				String tempString = stringArray[i].trim();
				if(tempString.startsWith(LinMobileConstants.AGENCY_ID_PREFIX)) {
					agencyList.add(tempString.substring(LinMobileConstants.AGENCY_ID_PREFIX.length()));
				}
				else if(tempString.startsWith(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
					advertiserList.add(tempString.substring(LinMobileConstants.ADVERTISER_ID_PREFIX.length()));
				}
			}
		}
	}

	@Override
	public void initEditTeam(TeamPropertiesObj teamProperties, UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("In initEditTeam");
		List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedAppViewsList = new ArrayList<CommonDTO>();
		List<CommonDTO> propertiesList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedPropertiesList = new ArrayList<CommonDTO>();
		List<CommonDTO> accountsList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedAccountsList = new ArrayList<CommonDTO>();
		/*String agencyIdStringForBigQuery = "";
		String advertiserIdStringForBigQuery = "";*/
		IUserDetailsDAO dao = new UserDetailsDAO();
		
		if(teamProperties != null && userDetailsDTO.getDefaultSelectedCompanyType() != null) {
			String companyId = teamProperties.getCompanyId();
			userDetailsDTO.setTeamName(teamProperties.getTeamName());
			userDetailsDTO.setTeamDescription(teamProperties.getTeamDescription());
			userDetailsDTO.setTeamType(teamProperties.getTeamType());
			selectedStatusList.add(new CommonDTO(teamProperties.getTeamStatus(),""));
			userDetailsDTO.setTeamStatus(teamProperties.getTeamStatus());
			
			List<String> tempList = teamProperties.getAppViews();
			if(tempList != null && tempList.size() > 0) {
				for (String appview : tempList) {
					selectedAppViewsList.add(new CommonDTO(appview.trim(), ""));
				}
			}
			
			if(userDetailsDTO.getAccessToProperties() != null && userDetailsDTO.getAccessToProperties().equals("1") && userDetailsDTO.getDefaultSelectedCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
				// Publisher Pool Partner
				propertiesList = dao.getActivePropertiesByPublisherCompanyId(companyId);
				tempList = teamProperties.getPropertyId();
				if(tempList != null && tempList.size() > 0) {
					if(tempList.contains(LinMobileConstants.ALL_PROPERTIES)) {
						userDetailsDTO.setAllPropertiesFlag("1");
					}
					else {
						for (String propertyId : tempList) {
							selectedPropertiesList.add(new CommonDTO(propertyId.trim(), ""));
						}
					}
				}
			}
			
			if(userDetailsDTO.getAccessToAccounts() != null && userDetailsDTO.getAccessToAccounts().equals("1")) {
				accountsList = getActiveAccountsByCompanyId(companyId, true);
				if(accountsList != null) {
					log.info("accountsList : "+accountsList.size());
				}else {
					log.info("accountsList is null");
				}
				if(teamProperties.getAgencyId() != null && teamProperties.getAdvertiserId() != null && teamProperties.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS) && teamProperties.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS)) {
					log.info("NO RESTRICTIONS on accounts");
					userDetailsDTO.setAccountsFlag("3");
				}
				else if(teamProperties.getAgencyId() != null && teamProperties.getAdvertiserId() != null && teamProperties.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES) && teamProperties.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS)) {
					log.info("Access to ALL ACCOUNT in datastore");
					userDetailsDTO.setAccountsFlag("2");
				}
				else {
					log.info("selected Accounts");
					userDetailsDTO.setAccountsFlag("1");
					if(teamProperties.getAgencyId() != null && teamProperties.getAgencyId().size() > 0 && !(teamProperties.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES)) && !(teamProperties.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS))) {
						selectedAccountsList.addAll(createUpdateTeamAccounts(LinMobileConstants.AGENCY_ID_PREFIX, teamProperties.getAgencyId()));
					}
					if(teamProperties.getAdvertiserId() != null && teamProperties.getAdvertiserId().size() > 0 && !(teamProperties.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS)) && !(teamProperties.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS))) {
						selectedAccountsList.addAll(createUpdateTeamAccounts(LinMobileConstants.ADVERTISER_ID_PREFIX, teamProperties.getAdvertiserId()));
					}
				}
			}
		}
		userDetailsDTO.setPropertiesList(propertiesList);
		userDetailsDTO.setSelectedPropertiesList(selectedPropertiesList);
		userDetailsDTO.setAccountsList(accountsList);
		userDetailsDTO.setSelectedAccountsList(selectedAccountsList);
		userDetailsDTO.setSelectedAppViewsList(selectedAppViewsList);
		userDetailsDTO.setSelectedStatusList(selectedStatusList);
	}
	
	public List<CommonDTO> createUpdateTeamAccounts(String prefixString, List<String> list) {
		log.info("inside createUpdateTeamAccounts");
		List<CommonDTO> commonDTOList = new ArrayList<CommonDTO>();
		if(list != null) {
			log.info("list size : "+list.size());
			for (String str : list) {
				commonDTOList.add(new CommonDTO(prefixString+str, ""));
			}
		}
		return commonDTOList;
	}

	@Override
	public Boolean updateTeam(UserDetailsDTO userDetailsDTO, long updateByUserId) throws Exception {
		log.info("inside updateTeam of UserService");
		List<String> propertyIdList =  new ArrayList<String>();
		List<String> appViewList = new ArrayList<String>();
		List<String> agencyList = new ArrayList<String>();
		List<String> advertiserList = new ArrayList<String>();
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		
		List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
		TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(userDetailsDTO.getTeamId(), teamPropertiesObjList);
		if(teamPropertiesObj != null) {
			maintainTeamPropertiesObjHistory(teamPropertiesObj, updateByUserId);
			
			teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			teamPropertiesObj.setLastModifiedByUserId(updateByUserId);
			teamPropertiesObj.setTeamName(userDetailsDTO.getTeamName().trim());
			teamPropertiesObj.setTeamDescription(userDetailsDTO.getTeamDescription().trim());
			teamPropertiesObj.setTeamStatus(userDetailsDTO.getTeamStatus());
			
			if(userDetailsDTO.getAppViews() != null) {
				appViewList.addAll(CommaSeperatedStringToList(userDetailsDTO.getAppViews()));
			}
			
			//Publisher Pool Partner Company
			if(userDetailsDTO.getSelectedCompanyType().trim().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccessToProperties() != null && userDetailsDTO.getAccessToProperties().equals("1")) {
				if(userDetailsDTO.getAllPropertiesFlag() != null && userDetailsDTO.getAllPropertiesFlag().equals("1")) {
					propertyIdList.add(LinMobileConstants.ALL_PROPERTIES);
				}
				else if(userDetailsDTO.getProperties() != null) {
					propertyIdList.addAll(CommaSeperatedStringToList(userDetailsDTO.getProperties()));
				}
			}
			
			log.info("userDetailsDTO.getAccessToAccounts() + "+userDetailsDTO.getAccessToAccounts());
			if(userDetailsDTO.getAccessToAccounts() != null && userDetailsDTO.getAccessToAccounts().equals("1")) {
				if(userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("1")) {
					createAdvertiserAndAgencyList(userDetailsDTO.getAccounts(), agencyList, advertiserList);
				}
				if(userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("2")) {
					agencyList.add(LinMobileConstants.ALL_AGENCIES);
					advertiserList.add(LinMobileConstants.ALL_ADVERTISERS);
				}
				if(userDetailsDTO.getSelectedCompanyType().trim().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccountsFlag() != null && userDetailsDTO.getAccountsFlag().equals("3")) {
					agencyList.add(LinMobileConstants.NO_RESTRICTIONS);
					advertiserList.add(LinMobileConstants.NO_RESTRICTIONS);
				}
			}
			
			teamPropertiesObj.setAppViews(appViewList);
			teamPropertiesObj.setPropertyId(propertyIdList);
			teamPropertiesObj.setAgencyId(agencyList);
			teamPropertiesObj.setAdvertiserId(advertiserList);
			
			if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, false)) {
				dao.saveObject(teamPropertiesObj);
				result = true;
			}
		}
		return result;
	}

	private void maintainTeamPropertiesObjHistory(TeamPropertiesObj teamPropertiesObj, long updateByUserId) throws Exception{
		log.info("inside maintainTeamPropertiesObjHistory of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		TeamPropertiesHistObj teamPropertiesHistObj = new TeamPropertiesHistObj();
		
		teamPropertiesHistObj.setId(teamPropertiesObj.getId());
		teamPropertiesHistObj.setTeamStatus(teamPropertiesObj.getTeamStatus());
		teamPropertiesHistObj.setTeamName(teamPropertiesObj.getTeamName());
		teamPropertiesHistObj.setTeamDescription(teamPropertiesObj.getTeamDescription());
		teamPropertiesHistObj.setCompanyId(teamPropertiesObj.getCompanyId());
		teamPropertiesHistObj.setAppViews(teamPropertiesObj.getAppViews());
		teamPropertiesHistObj.setTeamType(teamPropertiesObj.getTeamType());
		teamPropertiesHistObj.setAgencyId(teamPropertiesObj.getAgencyId());
		teamPropertiesHistObj.setAdvertiserId(teamPropertiesObj.getAdvertiserId());
		teamPropertiesHistObj.setPropertyId(teamPropertiesObj.getPropertyId());
		teamPropertiesHistObj.setCreationDate(teamPropertiesObj.getCreationDate());
		teamPropertiesHistObj.setCreatedByUserId(teamPropertiesObj.getCreatedByUserId());
		teamPropertiesHistObj.setLastModifiedDate(teamPropertiesObj.getLastModifiedDate());
		teamPropertiesHistObj.setLastModifiedByUserId(teamPropertiesObj.getLastModifiedByUserId());
		teamPropertiesHistObj.setHistoryDate(DateUtil.getDateYYYYMMDDHHMMSS());
		teamPropertiesHistObj.setModifiedByUserId(updateByUserId);
		
		dao.saveObject(teamPropertiesHistObj);
		log.info("Successfully created history for TeamPropertiesObj");		
	}

	@Override
	public Boolean createNewCompany(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside createNewCompany method of userservice");
		Boolean result = false;
		List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		CompanyObj companyObj = new CompanyObj();
		List<String> adServerId = new ArrayList<String>();
		List<String> adServerUsername = new ArrayList<String>();
		List<String> adServerPassword = new ArrayList<String>();
		List<String> appViewList = new ArrayList<String>();
		companyObj.setCompanyName(userDetailsDTO.getCompanyName());
		companyObj.setCompanyType(userDetailsDTO.getCompanyTypeToCreate());
		companyObj.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
		companyObj.setWebURL(userDetailsDTO.getWebURL());
		companyObj.setCompanyEmail(userDetailsDTO.getCompanyEmail());
		companyObj.setPhone(userDetailsDTO.getPhone());
		companyObj.setFax(userDetailsDTO.getFax());
		companyObj.setContactPersonName(userDetailsDTO.getContactPersonName());
		companyObj.setCompanyAddress(userDetailsDTO.getCompanyAddress());
		if(userDetailsDTO.getAccessToAccounts() != null && userDetailsDTO.getAccessToAccounts().equals("1")) {
			companyObj.setAccessToAccounts(true);
		}
		
		if(userDetailsDTO.getCompanyTypeToCreate().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccessToProperties() != null && userDetailsDTO.getAccessToProperties().equals("1")) {	// if publisher pool partner
			companyObj.setAccessToProperties(true);
		}
		
		if(userDetailsDTO.getAdServerInfo() != null) {
			if(userDetailsDTO.getAdServerInfo().equals(LinMobileConstants.DFP_DATA_SOURCE) && userDetailsDTO.getAdServerId() != null && userDetailsDTO.getAdServerUsername() != null && userDetailsDTO.getAdServerPassword() != null && (userDetailsDTO.getAdServerId().length == userDetailsDTO.getAdServerUsername().length) && (userDetailsDTO.getAdServerUsername().length == userDetailsDTO.getAdServerPassword().length)) {
				int length = userDetailsDTO.getAdServerId().length;
				for(int i=0; i<length; i++) {
					if(userDetailsDTO.getAdServerId()[i] != null && userDetailsDTO.getAdServerUsername()[i] != null && userDetailsDTO.getAdServerPassword()[i] != null 
							&& !userDetailsDTO.getAdServerId()[i].trim().equals("") && !userDetailsDTO.getAdServerUsername()[i].trim().equals("") && !userDetailsDTO.getAdServerPassword()[i].trim().equals("")) {
						adServerId.add(userDetailsDTO.getAdServerId()[i].trim());
						adServerUsername.add(userDetailsDTO.getAdServerUsername()[i].trim());
						adServerPassword.add(userDetailsDTO.getAdServerPassword()[i].trim());
					}
				}
				
				companyObj.setAdServerId(adServerId);
				companyObj.setAdServerUsername(adServerUsername);
				companyObj.setAdServerPassword(adServerPassword);
			}
			
			if(userDetailsDTO.getAdServerInfo().equalsIgnoreCase("Other") && userDetailsDTO.getServiceURL()!= null && userDetailsDTO.getServiceURL().length > 0) {
				List<String> serviceURLList = new ArrayList<String>();
				for(String serviceURL : userDetailsDTO.getServiceURL()) {
					if(serviceURL != null && !serviceURL.trim().equals("")) {
						serviceURLList.add(serviceURL.trim());
					}
				}
				companyObj.setServiceURL(serviceURLList);
			}
			companyObj.setAdServerInfo(userDetailsDTO.getAdServerInfo());
		}
		
		if(userDetailsDTO.getAppViews() != null) {
			appViewList.addAll(CommaSeperatedStringToList(userDetailsDTO.getAppViews()));
			companyObj.setAppViews(appViewList);
		}
		
		if(userDetailsDTO.getCompanyTypeToCreate().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getDemandPartnerId() != null) {		// if Publisher Pool Partner
			List<String> DemandPartnerIdsList = CommaSeperatedStringToList(userDetailsDTO.getDemandPartnerId());
			companyObj.setDemandPartnerId(DemandPartnerIdsList);
		}
		if(userDetailsDTO.getCompanyTypeToCreate().equals(LinMobileConstants.COMPANY_TYPE[1])) {		// if Demand Partner
			companyObj.setDataSource(userDetailsDTO.getDataSource().trim());
			if(userDetailsDTO.getDemandPartnerCategory() != null && !userDetailsDTO.getDemandPartnerCategory().trim().equals("")) {
				companyObj.setDemandPartnerCategory(userDetailsDTO.getDemandPartnerCategory().trim());
			}
			if(userDetailsDTO.getDemandPartnerType() != null && !userDetailsDTO.getDemandPartnerType().trim().equals("")) {
				companyObj.setDemandPartnerType(userDetailsDTO.getDemandPartnerType().trim());
			}
			if(userDetailsDTO.getPassbackSiteType()!= null && userDetailsDTO.getPassbackSiteType().length > 0) {
				List<String> passBackSiteTypeList = new ArrayList<String>();
				for(String passBackSiteType : userDetailsDTO.getPassbackSiteType()) {
					if(passBackSiteType != null && !passBackSiteType.trim().equals("")) {
						passBackSiteTypeList.add(passBackSiteType.trim());
					}
				}
				companyObj.setPassback_Site_type(passBackSiteTypeList);
			}
		}
		
		companyObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
		companyObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
		companyObj.setLastModifiedByUserId(userId);
		companyObj.setCreatedByUserId(userId);
		IUserDetailsDAO dao = new UserDetailsDAO();
		CompanyObj obj = dao.getCompanyByName(userDetailsDTO.getCompanyName(), companyObjList);
		if(obj == null) {
			companyObj.setId(dao.getMaxCount());
			log.info("logo file : "+userDetailsDTO.getCompanyLogo());
			log.info("logo file content type : "+userDetailsDTO.getCompanyLogoContentType());
			log.info("logo file name : "+userDetailsDTO.getCompanyLogoFileName());
			if(userDetailsDTO.getCompanyLogoFileName() != null) {
				String uploadedFileURL = uploadCompanyLogo(userDetailsDTO, companyObj);
				log.info("uploadedFileURL : "+uploadedFileURL);
				if(uploadedFileURL != null && uploadedFileURL.length() > 0) {
					companyObj.setCompanyLogoURL(uploadedFileURL);
					companyObj.setCompanyLogoName(userDetailsDTO.getCompanyLogoFileName());
				}
			}
			if(MemcacheUtil.selfUpdateMemcacheCompany(companyObj, true)) {
				dao.saveObject(companyObj);
				result = true;
				createDefaultTeamAllEntity(companyObj, userId);
				createDefaultTeamNoEntity(userDetailsDTO, companyObj.getId()+"", userId);
			}
		}
		return result;
	}
	
	public Boolean initEditCompany(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside initEditCompany method of userservice");
		Boolean result = false;
		CompanyObj companyObj = null;
		int count = 1;
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CommonDTO> selectedDemandPartnersList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedAppViewsList = new ArrayList<CommonDTO>();
		List<AdServerCredentialsDTO> adServerCredentialsDTOList = new ArrayList<AdServerCredentialsDTO>();
		List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedDemandPartnerTypeList = new ArrayList<CommonDTO>();
		List<String> tempPassbackSiteTypeList = null;
		List<String> tempServiceURLList = null;
		List<CommonDTO> passbackSiteTypeList = new ArrayList<CommonDTO>();
		List<CommonDTO> serviceURLList = new ArrayList<CommonDTO>();
		if(userDetailsDTO.getCompanyTypeToUpdate() != null) {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			companyObj = dao.getCompanyById(Long.valueOf(userDetailsDTO.getCompanyId()), companyObjList);
			if(companyObj != null) {
				if(companyObj.getCompanyLogoURL() != null && companyObj.getCompanyLogoURL().length() > 0) {
					userDetailsDTO.setCompanyLogoURL(companyObj.getCompanyLogoURL());
					if(companyObj.getCompanyLogoName() != null && companyObj.getCompanyLogoName().length() > 0) {
						userDetailsDTO.setCompanyLogoFileName(companyObj.getCompanyLogoName());
					}
				}
				if(companyObj.getCompanyName()!=null && !companyObj.getCompanyName().equals("")){
					userDetailsDTO.setCompanyName(companyObj.getCompanyName());
				}
				if(companyObj.getStatus()!=null && !companyObj.getStatus().equals("")){
					selectedStatusList.add(new CommonDTO(companyObj.getStatus(), companyObj.getStatus()));
					userDetailsDTO.setSelectedStatusList(selectedStatusList);
					userDetailsDTO.setStatus(companyObj.getStatus());
				}
				if(companyObj.getWebURL()!=null){
					userDetailsDTO.setWebURL(companyObj.getWebURL());
				}
				if(companyObj.getCompanyEmail()!=null){
					userDetailsDTO.setCompanyEmail(companyObj.getCompanyEmail());
				}
				if(companyObj.getPhone()!=null){
					userDetailsDTO.setPhone(companyObj.getPhone());
				}
				if(companyObj.getFax()!=null){
					userDetailsDTO.setFax(companyObj.getFax());
				}
				if(companyObj.getContactPersonName()!=null){
					userDetailsDTO.setContactPersonName(companyObj.getContactPersonName());
				}
				if(companyObj.getCompanyAddress()!=null){
					userDetailsDTO.setCompanyAddress(companyObj.getCompanyAddress());
				}
				
				userDetailsDTO.setAdServerCredentialsCounterValue(String.valueOf(count));
				userDetailsDTO.setServiceURLCounterValue(String.valueOf(count));
				if(companyObj.getAdServerInfo() !=null){
					userDetailsDTO.setAdServerInfo(companyObj.getAdServerInfo());
					if(companyObj.getAdServerInfo().equals(LinMobileConstants.DFP_DATA_SOURCE) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null && companyObj.getAdServerPassword().get(i) != null) {
								adServerCredentialsDTOList.add(new AdServerCredentialsDTO(String.valueOf(count), companyObj.getAdServerId().get(i).trim(), companyObj.getAdServerUsername().get(i).trim(), companyObj.getAdServerPassword().get(i).trim()));
								count++;
							}
						}
						userDetailsDTO.setAdServerCredentialsDTOList(adServerCredentialsDTOList);
						userDetailsDTO.setAdServerCredentialsCounterValue(String.valueOf(count));
					}					
					if(companyObj.getAdServerInfo().equalsIgnoreCase("Other")) {
						count = 1;
						tempServiceURLList = companyObj.getServiceURL();
						if(tempServiceURLList != null && tempServiceURLList.size() > 0) {
							for (String serviceURL : tempServiceURLList) {
								serviceURLList.add(new CommonDTO(String.valueOf(count), serviceURL.trim()));
								count++;
							}
							userDetailsDTO.setServiceURLList(serviceURLList);
							userDetailsDTO.setServiceURLCounterValue(String.valueOf(count));
						}
					}
				}
				
				List<String> tempList = companyObj.getAppViews();
				if(tempList != null && tempList.size() > 0) {
					for (String appview : tempList) {
						selectedAppViewsList.add(new CommonDTO(appview.trim(), ""));
					}
				}
				
				if(companyObj.isAccessToAccounts()) {
					userDetailsDTO.setAccessToAccounts("1");
				}
				if(companyObj.isAccessToProperties()) {
					userDetailsDTO.setAccessToProperties("1");
				}
				
				// Publisher Pool Partner
				if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.getDemandPartnerId() != null && companyObj.getDemandPartnerId().size() > 0) {
					for (String demandPartnerId : companyObj.getDemandPartnerId()) {
						selectedDemandPartnersList.add(new CommonDTO(demandPartnerId.trim(), ""));
					}
					userDetailsDTO.setSelectedDemandPartnersList(selectedDemandPartnersList);
				}
				
				// Demand Partner
				if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[1])) {
					count = 1;
					if(companyObj.getDataSource() != null) {
						userDetailsDTO.setDataSource(companyObj.getDataSource().trim());
					}
					if(companyObj.getDemandPartnerCategory() != null) {
						userDetailsDTO.setDemandPartnerCategory(companyObj.getDemandPartnerCategory().trim());
					}
					
					if(companyObj.getDemandPartnerType() != null) {
						selectedDemandPartnerTypeList.add(new CommonDTO(companyObj.getDemandPartnerType().trim(), ""));
						userDetailsDTO.setSelectedDemandPartnerTypeList(selectedDemandPartnerTypeList);
					}
					
					tempPassbackSiteTypeList = companyObj.getPassback_Site_type();
					if(tempPassbackSiteTypeList != null && tempPassbackSiteTypeList.size() > 0) {
						for (String passbackSiteType : tempPassbackSiteTypeList) {
							passbackSiteTypeList.add(new CommonDTO(String.valueOf(count), passbackSiteType.trim()));
							count++;
						}
						userDetailsDTO.setPassbackSiteTypeList(passbackSiteTypeList);
					}
					userDetailsDTO.setPassbackSiteTypeCounterValue(String.valueOf(count));
				}
			}
			result = true;
		}
		userDetailsDTO.setSelectedAppViewsList(selectedAppViewsList);
		return result;		
	}
	
	public Boolean updateCompany(UserDetailsDTO userDetailsDTO,long updateDeleteByUserId) throws Exception{
		log.info("inside updateCompany method of userservice");
		Boolean result = false;
		IUserDetailsDAO dao = new UserDetailsDAO();
		CompanyObj companyObj = null;
		List<String> adServerId = new ArrayList<String>();
		Map<String, AdServerCredentialsDTO> updatedAdServerInfo = new HashMap<>();
		Map<String, AdServerCredentialsDTO> existingAdServerInfo = new HashMap<>();
		List<String> adServerUsername = new ArrayList<String>();
		List<String> adServerPassword = new ArrayList<String>();
		List<String> serviceURLList = new ArrayList<String>();
		List<String> appViewList = new ArrayList<String>();
		List<String> passBackSiteTypeList = new ArrayList<String>();
		List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		companyObj = dao.getCompanyById(Long.valueOf(userDetailsDTO.getCompanyId()), companyObjList);
		if(companyObj != null) {
			maintainCompanyHistory(companyObj,updateDeleteByUserId);
			
			if(companyObj.getAdServerInfo() !=null && companyObj.getAdServerInfo().equals(LinMobileConstants.DFP_DATA_SOURCE) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
				int length = companyObj.getAdServerId().size();
				for(int i=0; i<length; i++) {
					if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null && companyObj.getAdServerPassword().get(i) != null) {
						existingAdServerInfo.put(companyObj.getAdServerId().get(i).trim()+companyObj.getAdServerUsername().get(i).trim(), new AdServerCredentialsDTO(companyObj.getAdServerId().get(i).trim(), companyObj.getAdServerUsername().get(i).trim()));
					}
				}
			}
			
			if(companyObj.getCompanyName()!=null && !companyObj.getCompanyName().equals("")){
				companyObj.setCompanyName(userDetailsDTO.getCompanyName());
			}
			if(companyObj.getStatus()!=null && !companyObj.getStatus().equals("")){
				companyObj.setStatus(userDetailsDTO.getStatus());
			}
			companyObj.setCompanyType(userDetailsDTO.getCompanyTypeToUpdate());
			companyObj.setWebURL(userDetailsDTO.getWebURL());
			companyObj.setCompanyEmail(userDetailsDTO.getCompanyEmail());
			companyObj.setPhone(userDetailsDTO.getPhone());
			companyObj.setFax(userDetailsDTO.getFax());
			companyObj.setContactPersonName(userDetailsDTO.getContactPersonName());
			companyObj.setCompanyAddress(userDetailsDTO.getCompanyAddress());
			if(userDetailsDTO.getAccessToAccounts() != null && userDetailsDTO.getAccessToAccounts().equals("1")) {
				companyObj.setAccessToAccounts(true);
			}
			else {
				companyObj.setAccessToAccounts(false);
			}
			
			if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getAccessToProperties() != null && userDetailsDTO.getAccessToProperties().equals("1")) {		// if publisher pool partner
				companyObj.setAccessToProperties(true);
			}
			else {
				companyObj.setAccessToProperties(false);
			}
			
			if(userDetailsDTO.getAdServerInfo() != null) {
				if(userDetailsDTO.getAdServerInfo().equals(LinMobileConstants.DFP_DATA_SOURCE) && userDetailsDTO.getAdServerId() != null && userDetailsDTO.getAdServerUsername() != null && userDetailsDTO.getAdServerPassword() != null && (userDetailsDTO.getAdServerId().length == userDetailsDTO.getAdServerUsername().length) && (userDetailsDTO.getAdServerUsername().length == userDetailsDTO.getAdServerPassword().length)) {
					int length = userDetailsDTO.getAdServerId().length;
					for(int i=0; i<length; i++) {
						if(userDetailsDTO.getAdServerId()[i] != null && userDetailsDTO.getAdServerUsername()[i] != null && userDetailsDTO.getAdServerPassword()[i] != null 
								&& !userDetailsDTO.getAdServerId()[i].trim().equals("") && !userDetailsDTO.getAdServerUsername()[i].trim().equals("") && !userDetailsDTO.getAdServerPassword()[i].trim().equals("")) {
							adServerId.add(userDetailsDTO.getAdServerId()[i].trim());
							adServerUsername.add(userDetailsDTO.getAdServerUsername()[i].trim());
							adServerPassword.add(userDetailsDTO.getAdServerPassword()[i].trim());
							updatedAdServerInfo.put(userDetailsDTO.getAdServerId()[i].trim()+userDetailsDTO.getAdServerUsername()[i].trim(), new AdServerCredentialsDTO(userDetailsDTO.getAdServerId()[i].trim(), userDetailsDTO.getAdServerUsername()[i].trim()));
						}
					}
				}
				
				if(userDetailsDTO.getAdServerInfo().equalsIgnoreCase("Other") && userDetailsDTO.getServiceURL()!= null && userDetailsDTO.getServiceURL().length > 0) {
					for(String serviceURL : userDetailsDTO.getServiceURL()) {
						if(serviceURL != null && !serviceURL.trim().equals("")) {
							serviceURLList.add(serviceURL.trim());
						}
					}
				}
				companyObj.setAdServerInfo(userDetailsDTO.getAdServerInfo());
			}
			
			if(userDetailsDTO.getAppViews() != null) {
				appViewList.addAll(CommaSeperatedStringToList(userDetailsDTO.getAppViews()));
			}
			
			if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[0]) && userDetailsDTO.getDemandPartnerId() != null) {		// if Publisher Pool Partner
				List<String> DemandPartnerIdsList = CommaSeperatedStringToList(userDetailsDTO.getDemandPartnerId());
				companyObj.setDemandPartnerId(DemandPartnerIdsList);
			}
			if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[1])) {		// if Demand Partner
				companyObj.setDataSource(userDetailsDTO.getDataSource().trim());
				if(userDetailsDTO.getDemandPartnerCategory() != null && !userDetailsDTO.getDemandPartnerCategory().trim().equals("")) {
					companyObj.setDemandPartnerCategory(userDetailsDTO.getDemandPartnerCategory().trim());
				}
				if(userDetailsDTO.getDemandPartnerType() != null && !userDetailsDTO.getDemandPartnerType().trim().equals("")) {
					companyObj.setDemandPartnerType(userDetailsDTO.getDemandPartnerType().trim());
				}
				if(userDetailsDTO.getPassbackSiteType()!= null && userDetailsDTO.getPassbackSiteType().length > 0) {
					for(String passBackSiteType : userDetailsDTO.getPassbackSiteType()) {
						if(passBackSiteType != null && !passBackSiteType.trim().equals("")) {
							passBackSiteTypeList.add(passBackSiteType.trim());
						}
					}
				}
			}
			
			// remove Accounts for DFP if no longer DFP exists for this company
			List<AccountsEntity> accountsObjToDelete = new ArrayList<>();
			for(String key : existingAdServerInfo.keySet()) {
				if(!updatedAdServerInfo.containsKey(key)) {
					AdServerCredentialsDTO adServerCredentialsDTO = existingAdServerInfo.get(key);
					log.info("No longer DFP("+adServerCredentialsDTO.getAdServerId()+", "+adServerCredentialsDTO.getAdServerIdUsername()+") exists for companyId : "+companyObj.getId());
					accountsObjToDelete.addAll(dao.getAccounts(companyObj.getId()+"", adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerIdUsername()));
				}
			}
			if(accountsObjToDelete.size() > 0) {
				List<AccountsEntity> existingAccountList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
				List<AccountsEntity> copyList = new ArrayList<>();
				copyList.addAll(existingAccountList);
				
				for(int i=0; i<copyList.size(); i++) {
					AccountsEntity accountsObj = copyList.get(i);
					if(accountsObjToDelete.contains(accountsObj)) {
						existingAccountList.remove(i);
					}
				}
				MemcacheUtil.setAccountsObj(existingAccountList, companyObj.getId()+"");
				dao.deleteObject(accountsObjToDelete);
			}
			//
			
			companyObj.setAdServerId(adServerId);
			companyObj.setAdServerUsername(adServerUsername);
			companyObj.setAdServerPassword(adServerPassword);
			companyObj.setServiceURL(serviceURLList);
			companyObj.setAppViews(appViewList);
			companyObj.setPassback_Site_type(passBackSiteTypeList);
			companyObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			companyObj.setLastModifiedByUserId(updateDeleteByUserId);
			
			log.info("logo file : "+userDetailsDTO.getCompanyLogo());
			log.info("logo file content type : "+userDetailsDTO.getCompanyLogoContentType());
			log.info("logo file name : "+userDetailsDTO.getCompanyLogoFileName());
			if(userDetailsDTO.getCompanyLogoFileName() != null) {
				String uploadedFileURL = uploadCompanyLogo(userDetailsDTO, companyObj);
				log.info("uploadedFileURL : "+uploadedFileURL);
				if(uploadedFileURL != null && uploadedFileURL.length() > 0) {
					companyObj.setCompanyLogoURL(uploadedFileURL);
					companyObj.setCompanyLogoName(userDetailsDTO.getCompanyLogoFileName());
					userDetailsDTO.setCompanyLogoURL(uploadedFileURL);
				}
			}
			
			if(MemcacheUtil.selfUpdateMemcacheCompany(companyObj, false)) {
				dao.saveObject(companyObj);
				result = true;
				updateDefaultTeamAllEntity(companyObj, updateDeleteByUserId);
				updateDefaultTeamNoEntity(userDetailsDTO, companyObj.getId()+"", updateDeleteByUserId);
			}
		}
		return result;
		
	}
	
	public void maintainCompanyHistory(CompanyObj companyObj,long updateDeleteByUserId) throws Exception{
		log.info("inside maintainCompanyHistory method of userservice");
		IUserDetailsDAO dao = new UserDetailsDAO();
		CompanyHistObj companyHistObj = new CompanyHistObj();
		
		if(companyObj!=null){
			companyHistObj.setCompanyName(companyObj.getCompanyName());
			companyHistObj.setStatus(companyObj.getStatus());
			companyHistObj.setId(companyObj.getId());
			companyHistObj.setCompanyType(companyObj.getCompanyType());
			companyHistObj.setCompanyLogoURL(companyObj.getCompanyLogoURL());
			companyHistObj.setCompanyLogoName(companyObj.getCompanyLogoName());
			companyHistObj.setWebURL(companyObj.getWebURL());
			companyHistObj.setCompanyEmail(companyObj.getCompanyEmail());
			companyHistObj.setPhone(companyObj.getPhone());
			companyHistObj.setFax(companyObj.getFax());
			companyHistObj.setContactPersonName(companyObj.getContactPersonName());
			companyHistObj.setCompanyAddress(companyObj.getCompanyAddress());
			companyHistObj.setAdServerInfo(companyObj.getAdServerInfo());
			companyHistObj.setAdServerId(companyObj.getAdServerId());
			companyHistObj.setAdServerUsername(companyObj.getAdServerUsername());
			companyHistObj.setAdServerPassword(companyObj.getAdServerPassword());
			companyHistObj.setServiceURL(companyObj.getServiceURL());
			companyHistObj.setAppViews(companyObj.getAppViews());
			companyHistObj.setAccessToAccounts(companyObj.isAccessToAccounts());
			companyHistObj.setAccessToProperties(companyObj.isAccessToProperties());
			companyHistObj.setDemandPartnerId(companyObj.getDemandPartnerId());
			companyHistObj.setDataSource(companyObj.getDataSource());
			companyHistObj.setDemandPartnerCategory(companyObj.getDemandPartnerCategory());
			companyHistObj.setPassback_Site_type(companyObj.getPassback_Site_type());
			companyHistObj.setDemandPartnerType(companyObj.getDemandPartnerType());
			companyHistObj.setCreationDate(companyObj.getCreationDate());
			companyHistObj.setCreatedByUserId(companyObj.getCreatedByUserId());
			companyHistObj.setLastModifiedDate(companyObj.getLastModifiedDate());
			companyHistObj.setLastModifiedByUserId(companyObj.getLastModifiedByUserId());
			companyHistObj.setHistoryDate(DateUtil.getDateYYYYMMDDHHMMSS());
			companyHistObj.setUpdateDeleteByUserId(updateDeleteByUserId);
			
			dao.saveObject(companyHistObj);
		}
	}
	
	public void createDefaultTeamAllEntity(CompanyObj companyObj, long userId) {
		try {
			log.info("creating default AllEntity team for id : "+companyObj.getId() +" companyType : "+companyObj.getCompanyType());
			TeamPropertiesObj teamPropertiesObj = new TeamPropertiesObj();
			IUserDetailsDAO dao = new UserDetailsDAO();
			
			// create All Entity
			teamPropertiesObj.setCompanyId(companyObj.getId()+"");
			teamPropertiesObj.setTeamType(LinMobileConstants.DEFINED_TYPES[0]);
			teamPropertiesObj.setTeamStatus(LinMobileConstants.STATUS_ARRAY[0]);
			teamPropertiesObj.setTeamName(LinMobileConstants.TEAM_ALL_ENTITIE+" ("+companyObj.getCompanyName().trim()+")");		
			teamPropertiesObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
			teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			teamPropertiesObj.setLastModifiedByUserId(userId);
			teamPropertiesObj.setCreatedByUserId(userId);
			
			List<String> tempList = new ArrayList<String>();
			if(companyObj.getAppViews() != null && companyObj.getAppViews().size() > 0) {
				tempList.addAll(companyObj.getAppViews());
			}
			teamPropertiesObj.setAppViews(tempList);
			
			tempList = new ArrayList<String>();
			if(companyObj.isAccessToProperties()) {
				tempList.add(LinMobileConstants.ALL_PROPERTIES);
			}
			teamPropertiesObj.setPropertyId(tempList);
			
			tempList = new ArrayList<String>();
			if(companyObj.isAccessToAccounts()) {
				tempList.add(LinMobileConstants.NO_RESTRICTIONS);
				teamPropertiesObj.setAgencyId(tempList);
				tempList = new ArrayList<String>();
				tempList.add(LinMobileConstants.NO_RESTRICTIONS);
				teamPropertiesObj.setAdvertiserId(tempList);
			}
			else {
				teamPropertiesObj.setAgencyId(tempList);
				teamPropertiesObj.setAdvertiserId(tempList);
			}
			
			String uniqueNumber = LinMobileConstants.TEAM_ALL_ENTITIE + dao.getMaxCount();
			teamPropertiesObj.setId(uniqueNumber);
			if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, true)) {
				dao.saveObject(teamPropertiesObj);
			}
		}
		catch (Exception e) {
			log.severe("Exception in createDefaultTeamAllEntity of userAction : "+e.getMessage());
			
		}
		
	}
	
	public void createDefaultTeamNoEntity(UserDetailsDTO userDetailsDTO, String companyId, long userId) {
		try {
			log.info("creating default NoEntity team for id : "+companyId +" companyType : "+userDetailsDTO.getCompanyTypeToCreate());
			TeamPropertiesObj teamPropertiesObj = new TeamPropertiesObj();
			IUserDetailsDAO dao = new UserDetailsDAO();
			
			// create No Entity
			teamPropertiesObj.setCompanyId(companyId);
			teamPropertiesObj.setTeamType(LinMobileConstants.DEFINED_TYPES[0]);
			teamPropertiesObj.setTeamStatus(LinMobileConstants.STATUS_ARRAY[0]);
			teamPropertiesObj.setTeamName(LinMobileConstants.TEAM_NO_ENTITIE+" ("+userDetailsDTO.getCompanyName().trim()+")");		
			teamPropertiesObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
			teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			teamPropertiesObj.setLastModifiedByUserId(userId);
			teamPropertiesObj.setCreatedByUserId(userId);
			
			teamPropertiesObj.setAppViews(new ArrayList<String>());
			
			String uniqueNumber = LinMobileConstants.TEAM_NO_ENTITIE + dao.getMaxCount();
			teamPropertiesObj.setId(uniqueNumber);
			if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, true)) {
				dao.saveObject(teamPropertiesObj);
			}
		}
		catch (Exception e) {
			log.severe("Exception in createDefaultTeamNoEntity of userAction : "+e.getMessage());
			
		}
		
	}
	
	public void updateDefaultTeamAllEntity(CompanyObj companyObj, long userId) {
		try {
			log.info("updating default team AllEntity for id : "+companyObj.getId() +" companyType : "+companyObj.getCompanyType());
			IUserDetailsDAO dao = new UserDetailsDAO();
			TeamPropertiesObj teamPropertiesObj =  dao.getAllEntitiesTeamByTeamCompanyId(companyObj.getId()+"", MemcacheUtil.getAllTeamPropertiesObjList());
			
			if(teamPropertiesObj != null) {
				maintainTeamPropertiesObjHistory(teamPropertiesObj, userId);
				teamPropertiesObj.setTeamStatus(companyObj.getStatus());
				teamPropertiesObj.setTeamName(LinMobileConstants.TEAM_ALL_ENTITIE+" ("+companyObj.getCompanyName().trim()+")");		
				teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj.setLastModifiedByUserId(userId);
				
				/*List<String> tempList = new ArrayList<String>();
				if(companyObj.getAppViews() != null && companyObj.getAppViews().size() > 0) {
					tempList.addAll(companyObj.getAppViews());
				}
				teamPropertiesObj.setAppViews(tempList);
				
				tempList = new ArrayList<String>();
				if(companyObj.isAccessToProperties()) {
					tempList.add(LinMobileConstants.ALL_PROPERTIES);
				}
				teamPropertiesObj.setPropertyId(tempList);
				
				tempList = new ArrayList<String>();
				if(companyObj.isAccessToAccounts()) {
					tempList.add(LinMobileConstants.NO_RESTRICTIONS);
					teamPropertiesObj.setAgencyId(tempList);
					tempList = new ArrayList<String>();
					tempList.add(LinMobileConstants.NO_RESTRICTIONS);
					teamPropertiesObj.setAdvertiserId(tempList);
				}
				else {
					teamPropertiesObj.setAgencyId(tempList);
					teamPropertiesObj.setAdvertiserId(tempList);
				}*/
				
				if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, false)) {
					dao.saveObject(teamPropertiesObj);
				}
			}
			else {
				log.severe("ALL ENTITIES for company id : '"+companyObj.getId()+"' does not exist at the time of update company. Creating it now");
				createDefaultTeamAllEntity(companyObj, userId);
			}
		}
		catch (Exception e) {
			log.severe("Exception in updateDefaultTeamAllEntity of userAction : "+e.getMessage());
			
		}
		
	}
	
	public void updateDefaultTeamNoEntity(UserDetailsDTO userDetailsDTO, String companyId, long userId) {
		try {
			log.info("updating default team NoEntity for id : "+companyId +" companyType : "+userDetailsDTO.getCompanyTypeToUpdate());
			IUserDetailsDAO dao = new UserDetailsDAO();
			TeamPropertiesObj teamPropertiesObj =  dao.getNoEntitiesTeamByTeamCompanyId(companyId, MemcacheUtil.getAllTeamPropertiesObjList());
			
			if(teamPropertiesObj != null) {
				maintainTeamPropertiesObjHistory(teamPropertiesObj, userId);
				teamPropertiesObj.setTeamStatus(userDetailsDTO.getStatus());
				teamPropertiesObj.setTeamName(LinMobileConstants.TEAM_NO_ENTITIE+" ("+userDetailsDTO.getCompanyName().trim()+")");		
				teamPropertiesObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				teamPropertiesObj.setLastModifiedByUserId(userId);
				
				teamPropertiesObj.setAppViews(new ArrayList<String>());
				if(MemcacheUtil.selfUpdateMemcacheTeamPropertiesObj(teamPropertiesObj, false)) {
					dao.saveObject(teamPropertiesObj);
				}
			}
			else {
				createDefaultTeamNoEntity(userDetailsDTO, companyId, userId);
			}
		}
		catch (Exception e) {
			log.severe("Exception in updateDefaultTeamNoEntity of userAction : "+e.getMessage());
			
		}
		
	}

	@Override
	public void fetchTeamsByCompanyId(String companyId, UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("inside fetchTeamsByCompanyId method of userservice");
		List<CommonDTO> adminTeamList = new ArrayList<CommonDTO>();
		List<CommonDTO> nonAdminTeamList = new ArrayList<CommonDTO>();
		List<TeamPropertiesObj> teamPropertiesObjList =  MemcacheUtil.getAllTeamPropertiesObjList();
		if(teamPropertiesObjList != null && teamPropertiesObjList.size() > 0) {
			for (TeamPropertiesObj teamPropertiesObj : teamPropertiesObjList) {
				if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(companyId.trim()) && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					if(teamPropertiesObj.getTeamName().startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
						adminTeamList.add(new CommonDTO(teamPropertiesObj.getId(), teamPropertiesObj.getTeamName()));
					}
					nonAdminTeamList.add(new CommonDTO(teamPropertiesObj.getId(), teamPropertiesObj.getTeamName()));
				}
			}
		}
		userDetailsDTO.setAdminTeamList(adminTeamList);
		userDetailsDTO.setNonAdminTeamList(nonAdminTeamList);
	}
	
	@Override
	public CompanyObj getCompanyIdByAdminUserId(long userId) throws Exception {
		log.info("inside getCompanyIdByAdminUserId method of userservice");
		CompanyObj  companyObj = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CompanyObj> companyList = dao.getSelectedCompaniesByUserId(userId);
		if(companyList != null && companyList.size() > 0) {
			companyObj = companyList.get(0);
		}
		return companyObj;
	}
	
	@Override
	public void getAuthorisations(UserDetailsDTO userDetailsDTO, String roleId) {
		log.info("inside getAuthorisations of UserService");
		List<AuthorisationTextObj> authorisationTextList = new ArrayList<AuthorisationTextObj>();
		List<String> authorisationKeywordList = new ArrayList<String>();
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		try {
			List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			RolesAndAuthorisation rolesAndAuthorisation = userDetailsDAO.getRolesAndAuthorisationByRoleId(roleId, rolesAndAuthorisationList);
			if(rolesAndAuthorisation != null) {
				authorisationTextList = MemcacheUtil.getAllAuthorisationTextList();
				if(authorisationTextList != null && authorisationTextList.size() > 0) {
					setAuthorisationKeywords(authorisationTextList, authorisationKeywordList, rolesAndAuthorisation);
					userDetailsDTO.setAuthorisationTextList(authorisationTextList);
				}
			}
		} catch (Exception e) {
			log.severe("Exception in getAuthorisations of UserService"+e.getMessage());
			
		}
		//userDetailsDTO.setAuthorisationTextList(authorisationTextList);
		userDetailsDTO.setAuthorisationKeywordList(authorisationKeywordList);
	}
	
	public void setAuthorisationKeywords(List<AuthorisationTextObj> authorisationTextList, List<String> authorisationKeywordList, RolesAndAuthorisation rolesAndAuthorisation) {
		List<String> tempAuthorisationKeywordList = new ArrayList<String>();
		for (AuthorisationTextObj authorisationText : authorisationTextList) {
			if(authorisationText.getAuthorisationTextStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
				if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A1") && rolesAndAuthorisation.getA1() != null && rolesAndAuthorisation.getA1().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A2") && rolesAndAuthorisation.getA2() != null && rolesAndAuthorisation.getA2().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A3") && rolesAndAuthorisation.getA3() != null && rolesAndAuthorisation.getA3().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A4") && rolesAndAuthorisation.getA4() != null && rolesAndAuthorisation.getA4().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A5") && rolesAndAuthorisation.getA5() != null && rolesAndAuthorisation.getA5().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A6") && rolesAndAuthorisation.getA6() != null && rolesAndAuthorisation.getA6().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A7") && rolesAndAuthorisation.getA7() != null && rolesAndAuthorisation.getA7().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A8") && rolesAndAuthorisation.getA8() != null && rolesAndAuthorisation.getA8().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A9") && rolesAndAuthorisation.getA9() != null && rolesAndAuthorisation.getA9().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A10") && rolesAndAuthorisation.getA10() != null && rolesAndAuthorisation.getA10().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A11") && rolesAndAuthorisation.getA11() != null && rolesAndAuthorisation.getA11().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A12") && rolesAndAuthorisation.getA12() != null && rolesAndAuthorisation.getA12().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A13") && rolesAndAuthorisation.getA13() != null && rolesAndAuthorisation.getA13().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A14") && rolesAndAuthorisation.getA14() != null && rolesAndAuthorisation.getA14().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A15") && rolesAndAuthorisation.getA15() != null && rolesAndAuthorisation.getA15().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A16") && rolesAndAuthorisation.getA16() != null && rolesAndAuthorisation.getA16().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A17") && rolesAndAuthorisation.getA17() != null && rolesAndAuthorisation.getA17().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A18") && rolesAndAuthorisation.getA18() != null && rolesAndAuthorisation.getA18().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A19") && rolesAndAuthorisation.getA19() != null && rolesAndAuthorisation.getA19().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A20") && rolesAndAuthorisation.getA20() != null && rolesAndAuthorisation.getA20().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A21") && rolesAndAuthorisation.getA21() != null && rolesAndAuthorisation.getA21().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A22") && rolesAndAuthorisation.getA22() != null && rolesAndAuthorisation.getA22().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A23") && rolesAndAuthorisation.getA23() != null && rolesAndAuthorisation.getA23().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A24") && rolesAndAuthorisation.getA24() != null && rolesAndAuthorisation.getA24().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A25") && rolesAndAuthorisation.getA25() != null && rolesAndAuthorisation.getA25().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A26") && rolesAndAuthorisation.getA26() != null && rolesAndAuthorisation.getA26().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A27") && rolesAndAuthorisation.getA27() != null && rolesAndAuthorisation.getA27().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A28") && rolesAndAuthorisation.getA28() != null && rolesAndAuthorisation.getA28().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A29") && rolesAndAuthorisation.getA29() != null && rolesAndAuthorisation.getA29().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A30") && rolesAndAuthorisation.getA30() != null && rolesAndAuthorisation.getA30().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A31") && rolesAndAuthorisation.getA31() != null && rolesAndAuthorisation.getA31().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A32") && rolesAndAuthorisation.getA32() != null && rolesAndAuthorisation.getA32().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A33") && rolesAndAuthorisation.getA33() != null && rolesAndAuthorisation.getA33().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A34") && rolesAndAuthorisation.getA34() != null && rolesAndAuthorisation.getA34().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A35") && rolesAndAuthorisation.getA35() != null && rolesAndAuthorisation.getA35().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A36") && rolesAndAuthorisation.getA36() != null && rolesAndAuthorisation.getA36().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A37") && rolesAndAuthorisation.getA37() != null && rolesAndAuthorisation.getA37().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A38") && rolesAndAuthorisation.getA38() != null && rolesAndAuthorisation.getA38().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A39") && rolesAndAuthorisation.getA39() != null && rolesAndAuthorisation.getA39().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A40") && rolesAndAuthorisation.getA40() != null && rolesAndAuthorisation.getA40().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A41") && rolesAndAuthorisation.getA41() != null && rolesAndAuthorisation.getA41().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A42") && rolesAndAuthorisation.getA42() != null && rolesAndAuthorisation.getA42().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A43") && rolesAndAuthorisation.getA43() != null && rolesAndAuthorisation.getA43().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A44") && rolesAndAuthorisation.getA44() != null && rolesAndAuthorisation.getA44().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A45") && rolesAndAuthorisation.getA45() != null && rolesAndAuthorisation.getA45().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A46") && rolesAndAuthorisation.getA46() != null && rolesAndAuthorisation.getA46().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A47") && rolesAndAuthorisation.getA47() != null && rolesAndAuthorisation.getA47().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A48") && rolesAndAuthorisation.getA48() != null && rolesAndAuthorisation.getA48().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A49") && rolesAndAuthorisation.getA49() != null && rolesAndAuthorisation.getA49().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A50") && rolesAndAuthorisation.getA50() != null && rolesAndAuthorisation.getA50().trim().equals("1")) {

					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A51") && rolesAndAuthorisation.getA51() != null && rolesAndAuthorisation.getA51().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A52") && rolesAndAuthorisation.getA52() != null && rolesAndAuthorisation.getA52().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A53") && rolesAndAuthorisation.getA53() != null && rolesAndAuthorisation.getA53().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A54") && rolesAndAuthorisation.getA54() != null && rolesAndAuthorisation.getA54().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A55") && rolesAndAuthorisation.getA55() != null && rolesAndAuthorisation.getA55().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A56") && rolesAndAuthorisation.getA56() != null && rolesAndAuthorisation.getA56().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A57") && rolesAndAuthorisation.getA57() != null && rolesAndAuthorisation.getA57().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A58") && rolesAndAuthorisation.getA58() != null && rolesAndAuthorisation.getA58().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A59") && rolesAndAuthorisation.getA59() != null && rolesAndAuthorisation.getA59().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A60") && rolesAndAuthorisation.getA60() != null && rolesAndAuthorisation.getA60().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A61") && rolesAndAuthorisation.getA61() != null && rolesAndAuthorisation.getA61().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A62") && rolesAndAuthorisation.getA62() != null && rolesAndAuthorisation.getA62().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A63") && rolesAndAuthorisation.getA63() != null && rolesAndAuthorisation.getA63().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A64") && rolesAndAuthorisation.getA64() != null && rolesAndAuthorisation.getA64().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A65") && rolesAndAuthorisation.getA65() != null && rolesAndAuthorisation.getA65().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A66") && rolesAndAuthorisation.getA66() != null && rolesAndAuthorisation.getA2() != null && rolesAndAuthorisation.getA66().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A67") && rolesAndAuthorisation.getA67() != null && rolesAndAuthorisation.getA67().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A68") && rolesAndAuthorisation.getA68() != null && rolesAndAuthorisation.getA68().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A69") && rolesAndAuthorisation.getA69() != null && rolesAndAuthorisation.getA69().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A70") && rolesAndAuthorisation.getA70() != null && rolesAndAuthorisation.getA70().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A71") && rolesAndAuthorisation.getA71() != null && rolesAndAuthorisation.getA71().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A72") && rolesAndAuthorisation.getA72() != null && rolesAndAuthorisation.getA72().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A73") && rolesAndAuthorisation.getA73() != null && rolesAndAuthorisation.getA73().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A74") && rolesAndAuthorisation.getA74() != null && rolesAndAuthorisation.getA74().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A75") && rolesAndAuthorisation.getA75() != null && rolesAndAuthorisation.getA75().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A76") && rolesAndAuthorisation.getA76() != null && rolesAndAuthorisation.getA76().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A77") && rolesAndAuthorisation.getA77() != null && rolesAndAuthorisation.getA77().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A78") && rolesAndAuthorisation.getA78() != null && rolesAndAuthorisation.getA78().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A79") && rolesAndAuthorisation.getA79() != null && rolesAndAuthorisation.getA79().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A80") && rolesAndAuthorisation.getA80() != null && rolesAndAuthorisation.getA80().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A81") && rolesAndAuthorisation.getA81() != null && rolesAndAuthorisation.getA81().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A82") && rolesAndAuthorisation.getA82() != null && rolesAndAuthorisation.getA82().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A83") && rolesAndAuthorisation.getA83() != null && rolesAndAuthorisation.getA83().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A84") && rolesAndAuthorisation.getA84() != null && rolesAndAuthorisation.getA84().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A85") && rolesAndAuthorisation.getA85() != null && rolesAndAuthorisation.getA85().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A86") && rolesAndAuthorisation.getA86() != null && rolesAndAuthorisation.getA86().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A87") && rolesAndAuthorisation.getA87() != null && rolesAndAuthorisation.getA87().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A88") && rolesAndAuthorisation.getA88() != null && rolesAndAuthorisation.getA88().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A89") && rolesAndAuthorisation.getA89() != null && rolesAndAuthorisation.getA89().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A90") && rolesAndAuthorisation.getA90() != null && rolesAndAuthorisation.getA90().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A91") && rolesAndAuthorisation.getA91() != null && rolesAndAuthorisation.getA91().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A92") && rolesAndAuthorisation.getA92() != null && rolesAndAuthorisation.getA92().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A93") && rolesAndAuthorisation.getA93() != null && rolesAndAuthorisation.getA93().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A94") && rolesAndAuthorisation.getA94() != null && rolesAndAuthorisation.getA94().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A95") && rolesAndAuthorisation.getA95() != null && rolesAndAuthorisation.getA95().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A96") && rolesAndAuthorisation.getA96() != null && rolesAndAuthorisation.getA96().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A97") && rolesAndAuthorisation.getA97() != null && rolesAndAuthorisation.getA97().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A98") && rolesAndAuthorisation.getA98() != null && rolesAndAuthorisation.getA98().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A99") && rolesAndAuthorisation.getA99() != null && rolesAndAuthorisation.getA99().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
				else if(authorisationText.getRolesAndAuthorisationColumnName().trim().equals("A100") && rolesAndAuthorisation.getA100() != null && rolesAndAuthorisation.getA100().trim().equals("1")) {
					tempAuthorisationKeywordList.add(authorisationText.getAuthorisationTextKeyword().trim());
				}
			}
		}
		authorisationKeywordList.addAll(tempAuthorisationKeywordList);
	}
	
	@Override
	public String getFirstPageForLoginUser(String roleId, long userId, LoginDetailsDTO login) {
		log.info("Inside getFirstPageForLoginUser of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		
		try {
			List<String> appViewList = dao.getSelectedAppViewsByUserId(userId);
			if(appViewList != null && appViewList.size() > 0) {
				/*return appViewList.get(0).replaceAll(" ", "");*/
				return appViewList.get(0).replaceAll(" ", "");
			}
			else {					// if no appView is assigned
				return "noAppView";
			}
		}
		catch (Exception e) {
			log.info("Exception in getFirstPageForLoginUser of UserService : "+e.getMessage());
			
		}
		return "error";
	}
	
	@Override
	public void getAuthorisedPagesForUser(String roleId, long userId, UserDetailsDTO userDetailsDTO) throws Exception{
		log.info("Inside getAuthorisedPagesForUser of UserService");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> authorisedPagesList = new ArrayList<String>();
		
		List<String> appViewList = dao.getSelectedAppViewsByUserId(userId);
		log.info("appViewList : "+appViewList);
		if(appViewList != null && appViewList.size() > 0) {
			for (String appView : appViewList) {
				/*authorisedPagesList.add(appView.replaceAll(" ", ""));*/
				authorisedPagesList.add(appView.trim());
			}
		}
		userDetailsDTO.setAuthorisedPagesList(authorisedPagesList);
	}

	@Override
	public void propertySetup(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CompanyObj> companyObjList = null;
		List<PropertyObj> propertySetupDTOList = new ArrayList<PropertyObj>();
		if(superAdmin) {
			companyObjList = MemcacheUtil.getAllCompanyList();
		}
		else {
			companyObjList = dao.getSelectedCompaniesByUserId(userId);
		}
		
		if(companyObjList != null) {
			for(CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					propertySetupDTOList.addAll(preparePropertiesListForSetup(MemcacheUtil.getAllPropertiesList(companyObj.getId()+""), companyObj.getCompanyName()));
				}
			}
		}
		
		userDetailsDTO.setPropertySetupDTOList(propertySetupDTOList);
	}

	@Override
	public List<CommonDTO> getPublishersForPropertySetup(long userId, boolean superAdmin) throws Exception {
		log.info("Inside getPublishersForPropertySetup of UserService");
		List<CommonDTO> publishersList = new ArrayList<CommonDTO>();
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CompanyObj> allPublishersList = dao.getAllPublishers(MemcacheUtil.getAllCompanyList());
		
		if(allPublishersList != null) {
			if(superAdmin) {
				for(CompanyObj publisherCompany : allPublishersList) {
					if(publisherCompany != null && publisherCompany.getStatus().trim().equalsIgnoreCase(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.getCompanyName() != null) {
						publishersList.add(new CommonDTO(publisherCompany.getId()+"", publisherCompany.getCompanyName().trim()));
					}
				}
			}
			else {
				CompanyObj companyObj = getCompanyIdByAdminUserId(userId);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.getCompanyName() != null) {
					publishersList.add(new CommonDTO(companyObj.getId()+"", companyObj.getCompanyName().trim()));
				}
			}
		}
		return publishersList;
	}

	@Override
	public Boolean createProperty(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside createProperty method of userservice");
		PropertyObj propertyObj = new PropertyObj();
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<PropertyChildObj> childList = new ArrayList<PropertyChildObj>();
		String networkId = "";
		String networkUsername = "";
		Boolean result = false;
		
		networkId = userDetailsDTO.getNetworkId().trim();
		propertyObj.setAdServerId(networkId);
		networkUsername = userDetailsDTO.getNetworkUsername().trim();
		propertyObj.setAdServerUserName(networkUsername);
		propertyObj.setCompanyId(userDetailsDTO.getPublisherId().trim());
		
		propertyObj.setPropertyName(userDetailsDTO.getPropertyName().trim());
		
		if(userDetailsDTO.getChilds() != null && userDetailsDTO.getChilds().size() > 0) {
			for(String str : userDetailsDTO.getChilds()) {
				String[] childInfo = str.split("<SEP>");
				if(childInfo.length == 2 && childInfo[0] != null &&  childInfo[1] != null && childInfo[0].trim().length() > 0) {
					PropertyChildObj childObj = new PropertyChildObj(childInfo[0].trim(), childInfo[1].trim());
					if(!childList.contains(childObj))
					childList.add(childObj);
				}
			}
		}
		propertyObj.setChilds(childList);
		
		if(userDetailsDTO.getMarket() != null) {
			propertyObj.setMarket(userDetailsDTO.getMarket().trim());
		}
		if(userDetailsDTO.getDMARank() != null) {
			propertyObj.setDMARank(userDetailsDTO.getDMARank().trim());
		}
		if(userDetailsDTO.getDFPPropertyName() != null) {
			propertyObj.setDFPPropertyName(userDetailsDTO.getDFPPropertyName().trim());
		}
		if(userDetailsDTO.getDFPPropertyId() != null) {
			propertyObj.setDFPPropertyId(userDetailsDTO.getDFPPropertyId().trim());
		}
		if(userDetailsDTO.getAffiliation() != null) {
			propertyObj.setAffiliation(userDetailsDTO.getAffiliation().trim());
		}
		if(userDetailsDTO.getWebSite() != null) {
			propertyObj.setWebSite(userDetailsDTO.getWebSite().trim());
		}
		if(userDetailsDTO.getMobileWebURL() != null) {
			propertyObj.setMobileWebURL(userDetailsDTO.getMobileWebURL().trim());
		}
		if(userDetailsDTO.getTabletWebURL() != null) {
			propertyObj.setTabletWebURL(userDetailsDTO.getTabletWebURL().trim());
		}
		if(userDetailsDTO.getGeneralManager() != null) {
			propertyObj.setGeneralManager(userDetailsDTO.getGeneralManager().trim());
		}
		if(userDetailsDTO.getAddress() != null) {
			propertyObj.setAddress(userDetailsDTO.getAddress().trim());
		}
		if(userDetailsDTO.getZipCode() != null) {
			propertyObj.setZipCode(userDetailsDTO.getZipCode().trim());
		}
		if(userDetailsDTO.getState() != null) {
			propertyObj.setState(userDetailsDTO.getState().trim());
		}
		if(userDetailsDTO.getPhone() != null) {
			propertyObj.setPhone(userDetailsDTO.getPhone().trim());
		}
		propertyObj.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
		propertyObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
		propertyObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
		propertyObj.setCreatedByUserId(userId);
		propertyObj.setLastModifiedByUserId(userId);
		
		String uniqueNumber = networkId + "__" + networkUsername + "__" + userDetailsDTO.getDFPPropertyId().trim()+ "__" + userDetailsDTO.getPublisherId().trim();
		propertyObj.setId(uniqueNumber);
		if(MemcacheUtil.selfUpdateMemcachePropertyObj(propertyObj, true)) {
			dao.saveObject(propertyObj);
			result = true;
		}
		
		return result;
	}

	@Override
	public Boolean initEditProperty(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		CompanyObj companyObj = null;
		PropertyObj propertyObj = dao.getPropertyObjById(userDetailsDTO.getPropertyId().trim());
		if(propertyObj != null) {
			if(!superAdmin) {
				companyObj = getCompanyIdByAdminUserId(userId);
				if(companyObj == null || !(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) || companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[1]) || companyObj.getAdServerId()== null || !(companyObj.getAdServerId().contains(propertyObj.getAdServerId())) || !(propertyObj.getCompanyId().equals(companyObj.getId()+""))) {
					return false;
				}
			}
			else {
				companyObj = dao.getCompanyById(Long.valueOf(propertyObj.getCompanyId()), MemcacheUtil.getAllCompanyList());
				if(companyObj == null || !(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) || companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[1])) {
					return false;
				}
			}
			if(propertyObj.getStatus() != null) {
				List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
				selectedStatusList.add(new CommonDTO(propertyObj.getStatus().trim(), ""));
				userDetailsDTO.setSelectedStatusList(selectedStatusList);
			}
			userDetailsDTO.setCompanyName(companyObj.getCompanyName());
			userDetailsDTO.setPublisherId(companyObj.getId()+"");
			userDetailsDTO.setPropertyName(propertyObj.getPropertyName());
			userDetailsDTO.setMarket(propertyObj.getMarket());
			userDetailsDTO.setDMARank(propertyObj.getDMARank());
			userDetailsDTO.setNetworkId(propertyObj.getAdServerId());
			userDetailsDTO.setNetworkUsername(propertyObj.getAdServerUserName());
			userDetailsDTO.setDFPPropertyName(propertyObj.getDFPPropertyName());
			userDetailsDTO.setDFPPropertyId(propertyObj.getDFPPropertyId());
			
			List<PropertyChildObj> childObjList = propertyObj.getChilds();
			if(childObjList != null && childObjList.size() > 0) {
				List<String> childList = new ArrayList<String>();
				for(PropertyChildObj childObj : childObjList) {
					if(childObj != null && childObj.getChildId() != null && childObj.getChildName() != null && childObj.getChildId().trim().length() > 0 && !childList.contains(childObj.getChildId().trim()+"<SEP>"+childObj.getChildName().trim())) {
						childList.add(childObj.getChildId().trim()+"<SEP>"+childObj.getChildName().trim());
					}
				}
				userDetailsDTO.setChilds(childList);
			}
			
			userDetailsDTO.setAffiliation(propertyObj.getAffiliation());
			userDetailsDTO.setWebSite(propertyObj.getWebSite());
			userDetailsDTO.setMobileWebURL(propertyObj.getMobileWebURL());
			userDetailsDTO.setTabletWebURL(propertyObj.getTabletWebURL());
			userDetailsDTO.setGeneralManager(propertyObj.getGeneralManager());
			userDetailsDTO.setAddress(propertyObj.getAddress());
			userDetailsDTO.setZipCode(propertyObj.getZipCode());
			userDetailsDTO.setState(propertyObj.getState());
			userDetailsDTO.setPhone(propertyObj.getPhone());
			/*userDetailsDTO.setPublisherIdsForBigQuery(publisherIdsForBigQuery);*/
			
			result = true;
		}
		return result;
	}
	
	public void maintainPropertyHistory(PropertyObj propertyObj, long updateDeleteByUserId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		PropertyHistObj propertyHistObj = new PropertyHistObj();
		
		if(propertyObj != null) {
			propertyHistObj.setId(propertyObj.getId());
			propertyHistObj.setPropertyName(propertyObj.getPropertyName());
			propertyHistObj.setMarket(propertyObj.getMarket());
			propertyHistObj.setDMARank(propertyObj.getDMARank());
			propertyHistObj.setDFPPropertyName(propertyObj.getDFPPropertyName());
			propertyHistObj.setDFPPropertyId(propertyObj.getDFPPropertyId());
			propertyHistObj.setChilds(propertyObj.getChilds());
			propertyHistObj.setAdserverId(propertyObj.getAdServerId());
			propertyHistObj.setAdServerUserName(propertyObj.getAdServerUserName());
			propertyHistObj.setCompanyId(propertyObj.getCompanyId());
			propertyHistObj.setAffiliation(propertyObj.getAffiliation());
			propertyHistObj.setWebSite(propertyObj.getWebSite());
			propertyHistObj.setMobileWebURL(propertyObj.getMobileWebURL());
			propertyHistObj.setTabletWebURL(propertyObj.getTabletWebURL());
			propertyHistObj.setGeneralManager(propertyObj.getGeneralManager());
			propertyHistObj.setAddress(propertyObj.getAddress());
			propertyHistObj.setZipCode(propertyObj.getZipCode());
			propertyHistObj.setState(propertyObj.getState());
			propertyHistObj.setStatus(propertyObj.getStatus());
			propertyHistObj.setPhone(propertyObj.getPhone());
			propertyHistObj.setCreationDate(propertyObj.getCreationDate());
			propertyHistObj.setCreatedByUserId(propertyObj.getCreatedByUserId());
			propertyHistObj.setLastModifiedByUserId(propertyObj.getLastModifiedByUserId());
			propertyHistObj.setLastModifiedDate(propertyObj.getLastModifiedDate());
			propertyHistObj.setHistoryDate(DateUtil.getDateYYYYMMDDHHMMSS());
			propertyHistObj.setUpdateDeleteByUserId(updateDeleteByUserId);
			
			dao.saveObject(propertyHistObj);
		}
	}

	@Override
	public Boolean updateProperty(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside createProperty method of userservice");
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<PropertyChildObj> childList = new ArrayList<PropertyChildObj>();
		Boolean result = false;
		
		PropertyObj propertyObj = dao.getPropertyObjById(userDetailsDTO.getPropertyId().trim());
		if(propertyObj != null) {
			maintainPropertyHistory(propertyObj, userId);
			
			propertyObj.setPropertyName(userDetailsDTO.getPropertyName().trim());
			if(userDetailsDTO.getChilds() != null && userDetailsDTO.getChilds().size() > 0) {
				for(String str : userDetailsDTO.getChilds()) {
					String[] childInfo = str.split("<SEP>");
					if(childInfo.length == 2 && childInfo[0] != null &&  childInfo[1] != null && childInfo[0].trim().length() > 0) {
						PropertyChildObj childObj = new PropertyChildObj(childInfo[0].trim(), childInfo[1].trim());
						if(!childList.contains(childObj))
						childList.add(childObj);
					}
				}
			}
			propertyObj.setChilds(childList);
			
			if(userDetailsDTO.getMarket() != null) {
				propertyObj.setMarket(userDetailsDTO.getMarket().trim());
			}
			if(userDetailsDTO.getDMARank() != null) {
				propertyObj.setDMARank(userDetailsDTO.getDMARank().trim());
			}
			if(userDetailsDTO.getAffiliation() != null) {
				propertyObj.setAffiliation(userDetailsDTO.getAffiliation().trim());
			}
			if(userDetailsDTO.getWebSite() != null) {
				propertyObj.setWebSite(userDetailsDTO.getWebSite().trim());
			}
			if(userDetailsDTO.getMobileWebURL() != null) {
				propertyObj.setMobileWebURL(userDetailsDTO.getMobileWebURL().trim());
			}
			if(userDetailsDTO.getTabletWebURL() != null) {
				propertyObj.setTabletWebURL(userDetailsDTO.getTabletWebURL().trim());
			}
			if(userDetailsDTO.getGeneralManager() != null) {
				propertyObj.setGeneralManager(userDetailsDTO.getGeneralManager().trim());
			}
			if(userDetailsDTO.getAddress() != null) {
				propertyObj.setAddress(userDetailsDTO.getAddress().trim());
			}
			if(userDetailsDTO.getZipCode() != null) {
				propertyObj.setZipCode(userDetailsDTO.getZipCode().trim());
			}
			if(userDetailsDTO.getState() != null) {
				propertyObj.setState(userDetailsDTO.getState().trim());
			}
			if(userDetailsDTO.getPhone() != null) {
				propertyObj.setPhone(userDetailsDTO.getPhone().trim());
			}
			propertyObj.setStatus(userDetailsDTO.getStatus());
			propertyObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			propertyObj.setLastModifiedByUserId(userId);
			if(MemcacheUtil.selfUpdateMemcachePropertyObj(propertyObj, false)) {
				dao.saveObject(propertyObj);
				result = true;
			}
		}	
		return result;
	}
	
	/*public static String regenerateAccountAuthToken(String username, String password) {
		log.info("credentials : "+username+" , "+password);
		String clientLoginToken = null;
		ClientLoginAuth clientLoginAuth = new ClientLoginAuth(username, password);
		try {
			clientLoginToken =clientLoginAuth.getAuthToken();
			MemcacheUtil.setAccountAuthToken(username, clientLoginToken);
		} catch (Exception e) {
			log.severe(" Exception in getting authToken: "+e.getMessage());
			
		}	    
	    return clientLoginToken;
	}*/

	/*@Override
	public Map<String, AdServerCredentialsDTO> getCompanyDFPCredentials(List<CompanyObj> companyObIjList) {
		Map<String, AdServerCredentialsDTO> map = new LinkedHashMap<String, AdServerCredentialsDTO>();
		try {
			if(companyObjList != null && companyObjList.size() > 0) {
				for (CompanyObj companyObj : companyObjList) {
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && !companyObj.getAdServerId().get(i).trim().equals("") && companyObj.getAdServerUsername().get(i) != null && !companyObj.getAdServerUsername().get(i).trim().equals("") && companyObj.getAdServerPassword().get(i) != null && !companyObj.getAdServerPassword().get(i).trim().equals("")) {
								String key = companyObj.getAdServerId().get(i).trim() + companyObj.getAdServerUsername().get(i).trim();
								if(!(map.containsKey(key))) {
									AdServerCredentialsDTO obj =  new AdServerCredentialsDTO();
									obj.setAdServerId(companyObj.getAdServerId().get(i).trim());
									obj.setAdServerUsername(companyObj.getAdServerUsername().get(i).trim());
									obj.setAdServerPassword(companyObj.getAdServerPassword().get(i).trim());
									map.put(key, obj);
									log.info("AdServerId : "+obj.getAdServerId()+", AdServerUsername : "+obj.getAdServerUsername());
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getCompanyDFPCredentials of UserService : "+e.getMessage());
			
		}
		log.info("AdServerCredentialsDTO map size : "+map.size());
		return map;
	}*/
	
	/*@Override
	public List<AdServerCredentialsDTO> getCompanyDFPCredentials(List<CompanyObj> companyObjList, String seperator) {
		List<AdServerCredentialsDTO> adServerCredentialsDTOList = new ArrayList<AdServerCredentialsDTO>();
		List<String> tempList = new ArrayList<String>();
		try {
			if(companyObjList != null && companyObjList.size() > 0) {
				for (CompanyObj companyObj : companyObjList) {
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && !companyObj.getAdServerId().get(i).trim().equals("") && companyObj.getAdServerUsername().get(i) != null && !companyObj.getAdServerUsername().get(i).trim().equals("") && companyObj.getAdServerPassword().get(i) != null && !companyObj.getAdServerPassword().get(i).trim().equals("")) {
								String adServerIdUsername = companyObj.getAdServerId().get(i).trim() + companyObj.getAdServerUsername().get(i).trim();
								if(!(tempList.contains(adServerIdUsername))) {
									String adServerCustomizedUsername = companyObj.getAdServerId().get(i).trim() + " ("+companyObj.getAdServerUsername().get(i).trim()+")";
									tempList.add(adServerIdUsername);
									AdServerCredentialsDTO obj =  new AdServerCredentialsDTO();
									obj.setAdServerId(companyObj.getAdServerId().get(i).trim());
									obj.setAdServerUsername(companyObj.getAdServerUsername().get(i).trim());
									obj.setAdServerPassword(companyObj.getAdServerPassword().get(i).trim());
									obj.setAdServerIdUsername(adServerIdUsername);
									obj.setAdServerCustomizedUsername(adServerCustomizedUsername)
									map.put(key, obj);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getCompanyDFPCredentials of UserService : "+e.getMessage());
			
		}
		log.info("AdServerCredentialsDTO map size : "+map.size());
		return map;
	}*/
	
	public static String createCommaSeperatedStringForBigQuery(List<String> list) {
		// output would be in form --> a','b','c
		StringBuilder commaSeperatedString = new StringBuilder();
		if(list != null && list.size() > 0) {
			for(String str : list) {
				if(str != null && !str.trim().equals("")) {
					commaSeperatedString.append(str+"','");
				}
			}
		}
		String tempString = commaSeperatedString.toString();
		if(tempString.lastIndexOf("','") != -1) {
			tempString = tempString.substring(0, tempString.lastIndexOf("','"));
		}
		return tempString;
	}
	
	@Override
	public String getPublisherBQId(String companyId) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		String publisherBQId = "";
		try {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			if(companyId != null && !companyId.equals("") && companyObjList != null && companyObjList.size() > 0) {
				CompanyObj companyObj = dao.getCompanyById(Long.valueOf(companyId), companyObjList);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getBqIdentifier() >= 0) {
					publisherBQId = companyObj.getBqIdentifier()+"";
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherBQId in UserService : "+e.getMessage());
			
		}
		return publisherBQId;
	}
	
	@Override
	public String getPublisherBQId(CompanyObj companyObj) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		String publisherBQId = "";
		try {
			if(companyObj != null){
				log.info("companyObj : "+companyObj);
			}
			if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getBqIdentifier() >= 0) {
				publisherBQId = companyObj.getBqIdentifier()+"";
				log.info("publisherBQId : "+publisherBQId);
			}
			else {
				log.info("publisherBQId could not be obtained");
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherBQId in UserService : "+e.getMessage());
			
		}
		return publisherBQId;
	}
	
	/*@Override
	public String getPublisherIdsForBigQueryByCompanyId(String companyId) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> publisherIdInBigQueryList = new ArrayList<String>();
		StringBuilder publisherIdsForBigQuery = new StringBuilder();
		try {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList = MemcacheUtil.getAllAdServerToPublisherInBigQueryObj();
			if(companyId != null && !companyId.equals("") && companyObjList != null && companyObjList.size() > 0 && adServerToPublisherInBigQueryObjList != null && adServerToPublisherInBigQueryObjList.size() > 0) {
				CompanyObj companyObj = dao.getCompanyById(Long.valueOf(companyId), companyObjList);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size() && companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size()) {
					int length = companyObj.getAdServerId().size();
					for(int i=0; i<length; i++) {
						if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null) {
							String adServerId = companyObj.getAdServerId().get(i).trim();
							String adServerUserName = companyObj.getAdServerUsername().get(i).trim();
							DfpBQMappingObj dfpBQMappingObj = dao.getDfpBQMappingObj(adServerId, adServerUserName, adServerToPublisherInBigQueryObjList);
							if(dfpBQMappingObj != null && dfpBQMappingObj.getPublisherIdInBigQuery() != null && dfpBQMappingObj.getPublisherIdInBigQuery().size() > 0) {
								List<String> tempList = dfpBQMappingObj.getPublisherIdInBigQuery();
								for(String publisherId : tempList) {
									if(publisherId != null && !publisherId.trim().equals("") && !publisherIdInBigQueryList.contains(publisherId.trim())) {
										publisherIdInBigQueryList.add(publisherId.trim());
									}
								}
							}
						}
					}
					publisherIdsForBigQuery.append(createCommaSeperatedStringForBigQuery(publisherIdInBigQueryList));
				}
				
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherIdsForBigQueryByCompanyId in UserService : "+e.getMessage());
			
		}
		log.info("publisherIdsForBigQuery = " + publisherIdsForBigQuery.toString());
		return publisherIdsForBigQuery.toString();
	}*/
	
	/*@Override
	public String getPublisherIdsForBigQueryByCompanyName(String companyName) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> publisherIdInBigQueryList = new ArrayList<String>();
		StringBuilder publisherIdsForBigQuery = new StringBuilder();
		try {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList = MemcacheUtil.getAllAdServerToPublisherInBigQueryObj();
			if(companyName != null && !companyName.equals("") && companyObjList != null && companyObjList.size() > 0 && adServerToPublisherInBigQueryObjList != null && adServerToPublisherInBigQueryObjList.size() > 0) {
				CompanyObj companyObj = dao.getCompanyByName(companyName, companyObjList);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size() && companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size()) {
					int length = companyObj.getAdServerId().size();
					for(int i=0; i<length; i++) {
						if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null) {
							String adServerId = companyObj.getAdServerId().get(i).trim();
							String adServerUserName = companyObj.getAdServerUsername().get(i).trim();
							DfpBQMappingObj dfpBQMappingObj = dao.getDfpBQMappingObj(adServerId, adServerUserName, adServerToPublisherInBigQueryObjList);
							if(dfpBQMappingObj != null && dfpBQMappingObj.getPublisherIdInBigQuery() != null && dfpBQMappingObj.getPublisherIdInBigQuery().size() > 0) {
								List<String> tempList = dfpBQMappingObj.getPublisherIdInBigQuery();
								for(String publisherId : tempList) {
									if(publisherId != null && !publisherId.trim().equals("") && !publisherIdInBigQueryList.contains(publisherId.trim())) {
										publisherIdInBigQueryList.add(publisherId.trim());
									}
								}
							}
						}
					}
					publisherIdsForBigQuery.append(createCommaSeperatedStringForBigQuery(publisherIdInBigQueryList));
				}
				
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherIdsForBigQueryByCompanyName in UserService : "+e.getMessage());
			
		}
		log.info("publisherIdsForBigQuery = " + publisherIdsForBigQuery.toString());
		return publisherIdsForBigQuery.toString();
	}*/
	
	/*@Override
	public String getPublisherIdsForBigQuery(List<CompanyObj> companyObjList) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<String> publisherIdInBigQueryList = new ArrayList<String>();
		StringBuilder publisherIdsForBigQuery = new StringBuilder();
		try {
			List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList = MemcacheUtil.getAllAdServerToPublisherInBigQueryObj();
			if(companyObjList != null && companyObjList.size() > 0 && adServerToPublisherInBigQueryObjList != null && adServerToPublisherInBigQueryObjList.size() > 0) {
				Map<String, AdServerCredentialsDTO> credentialsMap = getCompanyDFPCredentials(companyObjList);
				if(credentialsMap != null && credentialsMap.size() > 0) {
					for (String key : credentialsMap.keySet()) {
						AdServerCredentialsDTO serverCredentialsDTO = credentialsMap.get(key);
						DfpBQMappingObj dfpBQMappingObj = dao.getDfpBQMappingObj(serverCredentialsDTO.getAdServerId(), serverCredentialsDTO.getAdServerUsername(), adServerToPublisherInBigQueryObjList);
						if(dfpBQMappingObj != null && dfpBQMappingObj.getPublisherIdInBigQuery() != null && dfpBQMappingObj.getPublisherIdInBigQuery().size() > 0) {
							List<String> tempList = dfpBQMappingObj.getPublisherIdInBigQuery();
							for(String publisherId : tempList) {
								if(publisherId != null && !publisherId.trim().equals("") && !publisherIdInBigQueryList.contains(publisherId.trim())) {
									publisherIdInBigQueryList.add(publisherId.trim());
								}
							}
						}
					}
					publisherIdsForBigQuery.append(createCommaSeperatedStringForBigQuery(publisherIdInBigQueryList));
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherIdsForBigQuery in UserService : "+e.getMessage());
			
		}
		log.info("publisherIdsForBigQuery = " + publisherIdsForBigQuery.toString());
		return publisherIdsForBigQuery.toString();
	}*/

	@Override
	public List<LineItemDTO> loadAccountsForTeam(String publisherIdsForBigQuery, String accountPrefix, String accountIdsNotIn) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<LineItemDTO> accountList = new ArrayList<LineItemDTO>();
		StringBuilder agencyIdsNotIn = new StringBuilder();
		StringBuilder advertiserIdsNotIn = new StringBuilder();
		getAgencyAndAdvertisersFromAccounts(accountIdsNotIn, agencyIdsNotIn, advertiserIdsNotIn);
		List<LineItemDTO> tempList = dao.getAccountsFromBigQuery(publisherIdsForBigQuery, accountPrefix, agencyIdsNotIn.toString(), advertiserIdsNotIn.toString());
		if(tempList != null) {
			accountList = tempList;
		}
		log.info("accountList size : "+accountList.size());
		return accountList;
	}

	private void getAgencyAndAdvertisersFromAccounts(String accountIdsNotIn, StringBuilder agencyIdsNotIn, StringBuilder advertiserIdsNotIn) {
		if(accountIdsNotIn != null && !accountIdsNotIn.trim().equals("")) {
			accountIdsNotIn = accountIdsNotIn.trim();
			String[] accountIdsNotInArr = accountIdsNotIn.split(",");
			for (int i = 0; i < accountIdsNotInArr.length; i++) {
				String accountId = accountIdsNotInArr[i].trim();
				if(accountId.startsWith(LinMobileConstants.AGENCY_ID_PREFIX)) {
					agencyIdsNotIn.append("'"+accountId.substring(LinMobileConstants.AGENCY_ID_PREFIX.length())+"',");
				}
				else if(accountId.startsWith(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
					advertiserIdsNotIn.append("'"+accountId.substring(LinMobileConstants.ADVERTISER_ID_PREFIX.length())+"',");
				}
			}
			String tempStr = agencyIdsNotIn.toString();
			if(tempStr.lastIndexOf(",") != -1) {
				tempStr = tempStr.substring(0, tempStr.lastIndexOf(","));
				agencyIdsNotIn.delete(0, agencyIdsNotIn.length());
				agencyIdsNotIn.append(tempStr);
			}
			tempStr = advertiserIdsNotIn.toString();
			if(tempStr.lastIndexOf(",") != -1) {
				tempStr = tempStr.substring(0, tempStr.lastIndexOf(","));
				advertiserIdsNotIn.delete(0, advertiserIdsNotIn.length());
				advertiserIdsNotIn.append(tempStr);
			}
		}
	}
	
/*	@Override
	public List<CommonDTO> getAllAppViewsByCompanyType(String companyType) throws Exception {
		List<CommonDTO> appViewList = new ArrayList<CommonDTO>();
		String[] appViewsArray = null;
		if(companyType.equals(LinMobileConstants.COMPANY_TYPE[0])) {		// Publisher Pool Partner
			appViewsArray = LinMobileConstants.PUBLISHER_PARTNER_APP_VIEWS;
		}
		else if(companyType.equals(LinMobileConstants.COMPANY_TYPE[1])) {	// Demand Partner
			appViewsArray = LinMobileConstants.DEMAND_PARTNER_APP_VIEWS;
		}
		else if(companyType.equals(LinMobileConstants.COMPANY_TYPE[2])) {	// Client
			appViewsArray = LinMobileConstants.CLIENT_APP_VIEWS;
		}
		int length = appViewsArray.length;
		for(int i=0; i<length; i++) {
			appViewList.add(new CommonDTO(appViewsArray[i], appViewsArray[i]));
		}
		
		return appViewList;
	}*/
	
	@Override
	public List<CommonDTO> getAllAppViewsforCompany() {
		List<CommonDTO> appViewList = new ArrayList<CommonDTO>();
		String[] appViewsArray = LinMobileConstants.COMPANY_APP_VIEWS;
		int length = appViewsArray.length;
			for(int i=0; i<length; i++) {
				appViewList.add(new CommonDTO(appViewsArray[i].split(LinMobileConstants.ARRAY_SPLITTER)[0].trim(), appViewsArray[i].split(LinMobileConstants.ARRAY_SPLITTER)[1].trim()));
			}
		return appViewList;
	}

	@Override
	public Boolean companySettings(UserDetailsDTO userDetailsDTO, long userId) throws Exception {
		log.info("inside companySettings method of userservice");
		Boolean result = false;
		int count = 1;
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CommonDTO> selectedDemandPartnersList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedAppViewsList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedStatusList = new ArrayList<CommonDTO>();
		List<CommonDTO> selectedDemandPartnerTypeList = new ArrayList<CommonDTO>();
		List<String> tempServiceURLList = null;
		List<CommonDTO> serviceURLList = new ArrayList<CommonDTO>();
		List<AdServerCredentialsDTO> adServerCredentialsDTOList = new ArrayList<AdServerCredentialsDTO>();
		List<String> tempPassbackSiteTypeList = null;
		List<CommonDTO> passbackSiteTypeList = new ArrayList<CommonDTO>();
		List<CompanyObj> allCompanyObjList = MemcacheUtil.getAllCompanyList();
		List<CompanyObj> companyObjList = dao.getSelectedCompaniesByUserId(userId);
		if(allCompanyObjList != null && allCompanyObjList.size() > 0 && companyObjList != null && companyObjList.size() > 0) {
			CompanyObj companyObj = companyObjList.get(0);
			if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
				userDetailsDTO.setCompanyId(companyObj.getId()+"");
				if(companyObj.getCompanyLogoURL() != null && companyObj.getCompanyLogoURL().length() > 0) {
					userDetailsDTO.setCompanyLogoURL(companyObj.getCompanyLogoURL());
					if(companyObj.getCompanyLogoName() != null && companyObj.getCompanyLogoName().length() > 0) {
						userDetailsDTO.setCompanyLogoFileName(companyObj.getCompanyLogoName());
					}
				}
				if(companyObj.getCompanyType() != null) {
					userDetailsDTO.setCompanyTypeToUpdate(companyObj.getCompanyType());
				}
				if(companyObj.getCompanyName()!=null && !companyObj.getCompanyName().equals("")){
					userDetailsDTO.setCompanyName(companyObj.getCompanyName());
				}
				if(companyObj.getStatus()!=null && !companyObj.getStatus().equals("")){
					selectedStatusList.add(new CommonDTO(companyObj.getStatus(), companyObj.getStatus()));
					userDetailsDTO.setSelectedStatusList(selectedStatusList);
					userDetailsDTO.setStatus(companyObj.getStatus());
				}
				if(companyObj.getWebURL()!=null){
					userDetailsDTO.setWebURL(companyObj.getWebURL());
				}
				if(companyObj.getCompanyEmail()!=null){
					userDetailsDTO.setCompanyEmail(companyObj.getCompanyEmail());
				}
				if(companyObj.getPhone()!=null){
					userDetailsDTO.setPhone(companyObj.getPhone());
				}
				if(companyObj.getFax()!=null){
					userDetailsDTO.setFax(companyObj.getFax());
				}
				if(companyObj.getContactPersonName()!=null){
					userDetailsDTO.setContactPersonName(companyObj.getContactPersonName());
				}
				if(companyObj.getCompanyAddress()!=null){
					userDetailsDTO.setCompanyAddress(companyObj.getCompanyAddress());
				}
				
				userDetailsDTO.setAdServerCredentialsCounterValue(String.valueOf(count));
				userDetailsDTO.setServiceURLCounterValue(String.valueOf(count));
				if(companyObj.getAdServerInfo() !=null){
					userDetailsDTO.setAdServerInfo(companyObj.getAdServerInfo());
					if(companyObj.getAdServerInfo().equals(LinMobileConstants.DFP_DATA_SOURCE) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null && companyObj.getAdServerPassword().get(i) != null) {
								adServerCredentialsDTOList.add(new AdServerCredentialsDTO(String.valueOf(count), companyObj.getAdServerId().get(i).trim(), companyObj.getAdServerUsername().get(i).trim(), companyObj.getAdServerPassword().get(i).trim()));
								count++;
							}
						}
						userDetailsDTO.setAdServerCredentialsDTOList(adServerCredentialsDTOList);
						userDetailsDTO.setAdServerCredentialsCounterValue(String.valueOf(count));
					}					
					if(companyObj.getAdServerInfo().equalsIgnoreCase("Other")) {
						count = 1;
						tempServiceURLList = companyObj.getServiceURL();
						if(tempServiceURLList != null && tempServiceURLList.size() > 0) {
							for (String serviceURL : tempServiceURLList) {
								serviceURLList.add(new CommonDTO(String.valueOf(count), serviceURL.trim()));
								count++;
							}
							userDetailsDTO.setServiceURLList(serviceURLList);
							userDetailsDTO.setServiceURLCounterValue(String.valueOf(count));
						}
					}
				}
				
				List<String> tempList = companyObj.getAppViews();
				if(tempList != null && tempList.size() > 0) {
					for (String appview : tempList) {
						selectedAppViewsList.add(new CommonDTO(appview.trim(), ""));
					}
				}
				
				if(companyObj.isAccessToAccounts()) {
					userDetailsDTO.setAccessToAccounts("1");
				}
				if(companyObj.isAccessToProperties()) {
					userDetailsDTO.setAccessToProperties("1");
				}
				
				// Publisher Pool Partner
				if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.getDemandPartnerId() != null && companyObj.getDemandPartnerId().size() > 0) {
					for (String demandPartnerId : companyObj.getDemandPartnerId()) {
						selectedDemandPartnersList.add(new CommonDTO(demandPartnerId.trim(), ""));
					}
					userDetailsDTO.setSelectedDemandPartnersList(selectedDemandPartnersList);
				}
				
				// Demand Partner
				if(userDetailsDTO.getCompanyTypeToUpdate().equals(LinMobileConstants.COMPANY_TYPE[1])) {
					count = 1;
					if(companyObj.getDataSource() != null) {
						userDetailsDTO.setDataSource(companyObj.getDataSource().trim());
					}
					if(companyObj.getDemandPartnerCategory() != null) {
						userDetailsDTO.setDemandPartnerCategory(companyObj.getDemandPartnerCategory().trim());
					}
					
					if(companyObj.getDemandPartnerType() != null) {
						selectedDemandPartnerTypeList.add(new CommonDTO(companyObj.getDemandPartnerType().trim(), ""));
						userDetailsDTO.setSelectedDemandPartnerTypeList(selectedDemandPartnerTypeList);
					}
					
					tempPassbackSiteTypeList = companyObj.getPassback_Site_type();
					if(tempPassbackSiteTypeList != null && tempPassbackSiteTypeList.size() > 0) {
						for (String passbackSiteType : tempPassbackSiteTypeList) {
							passbackSiteTypeList.add(new CommonDTO(String.valueOf(count), passbackSiteType.trim()));
							count++;
						}
						userDetailsDTO.setPassbackSiteTypeList(passbackSiteTypeList);
					}
					userDetailsDTO.setPassbackSiteTypeCounterValue(String.valueOf(count));
				}
				result = true;
			}
		}
		userDetailsDTO.setSelectedAppViewsList(selectedAppViewsList);
		return result;
	}
	
	public String uploadCompanyLogo(UserDetailsDTO userDetailsDTO, CompanyObj companyObj){
		String uploadedFileURL = "";
		String companyLogoFileName = userDetailsDTO.getCompanyLogoFileName(); 
		log.info("companyLogo FileName : "+companyLogoFileName);
		try {
			if(companyLogoFileName !=null){
				String file = userDetailsDTO.getCompanyLogo();
				String mimeType = userDetailsDTO.getCompanyLogoContentType();
				log.info("mimeType : "+mimeType);
				if(mimeType.contains("image")) {
					String companyLogoFileNameForBucket = LinMobileConstants.COMPANY_LOGO_NAME_PREFIX+"_"+(DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS").replaceAll(" ", ""));
		    	    String dirName=LinMobileConstants.COMPANY_IMAGES_FOLDER+"/"+companyObj.getId();
		    	    if(file !=null && file.length()>0){
		    	    	uploadedFileURL = uploadImageOnCloudStorage(file, companyLogoFileNameForBucket, mimeType, dirName);
					}else{
						log.warning("Image file is empty");
					}
		    	    log.info("Image file path : "+uploadedFileURL);
		    		log.info("companyLogoFileNameForBucket : "+companyLogoFileNameForBucket);
				}
				else {
					log.warning("Not an image file... ");
				}
			}
			else{
				log.warning("No file found to upload.");
			}
		} catch (FileNotFoundException e) {
			log.severe("FileNotFoundException:" + e.getMessage());
			

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		}
		return uploadedFileURL;
	}
	
	public static String uploadImageOnCloudStorage(String file, String fileName, String mimeType, String dirName) throws IOException {
		InputStream inputStream=IOUtils.toInputStream(file,"ISO-8859-1");
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		GCStorageUtil storage = new GCStorageUtil();
		try {
			log.info(":Going to upload file on cloud storage: fileName : " + fileName +" in directory : "+dirName);
			storage.init(dirName, fileName, mimeType);

			String uploadedFileURL =null;
			if(dirName !=null){
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream,dirName);
			}else{
				uploadedFileURL = storage.writeFile(fileName,bufferedInputStream);
			}
			
			if(uploadedFileURL != null && uploadedFileURL.length() > 0 && uploadedFileURL.startsWith("gs://")) {
				uploadedFileURL = uploadedFileURL.replaceFirst("gs://", "http://storage.googleapis.com/");
			}
			return uploadedFileURL;
		} catch (FileNotFoundException e) {
			log.severe(":FileNotFoundException::" + e.getMessage());
			
			return null;
		} catch (Exception e) {
			log.severe(":Exception::" + e.getMessage());
			
			return null;
		}
   }
	
	@Override
	public List<CommonDTO> getTeamDataByCompanyId(String companyId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CommonDTO> teamDataList = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		String companyType = "";
		List<CommonDTO> appViewsList = new ArrayList<CommonDTO>();
		List<CommonDTO> propertyList = new ArrayList<CommonDTO>();
		List<CommonDTO> accountsList = new ArrayList<CommonDTO>();
		boolean accessToAccounts = false;
		boolean accessToProperties = false;
		try {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			if(companyId != null && !companyId.equals("") && companyObjList != null && companyObjList.size() > 0) {
				CompanyObj companyObj = dao.getCompanyById(Long.valueOf(companyId), companyObjList);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
					companyType = companyObj.getCompanyType();
					if(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.isAccessToProperties()) {
						accessToProperties = true;
						propertyList.addAll(dao.getActivePropertiesByPublisherCompanyId(companyId));
					}
					if(companyObj.isAccessToAccounts()) {
						accessToAccounts = true;
						accountsList.addAll(getActiveAccountsByCompanyId(companyId, true));
					}
					if(companyObj.getAppViews() != null && companyObj.getAppViews().size() > 0) {
						appViewsList.addAll(getAppViewsByappViewId(companyObj.getAppViews()));
					}
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getPublisherIdsForBigQueryByCompanyId in UserService : "+e.getMessage());
			
		}
		commonDTO.setCompanyType(companyType);
		commonDTO.setAccessToAccounts(accessToAccounts);
		commonDTO.setAccessToProperties(accessToProperties);
		commonDTO.setAppViews(appViewsList);
		commonDTO.setPropertyList(propertyList);
		commonDTO.setAccountsList(accountsList);
		teamDataList.add(commonDTO);
		
		log.info("appViewsList size : "+appViewsList.size());
		log.info("propertyList size : "+propertyList.size());
		log.info("accountsList size : "+accountsList.size());
		log.info("accessTOAccounts : "+accessToAccounts);
		log.info("accessToProperties : "+accessToProperties);

		return teamDataList;
	}
	
	public List<CommonDTO> getAppViewsByappViewId(List<String> appViewId) {
		List<CommonDTO> appViewList = new ArrayList<CommonDTO>();
		String[] allAppviews = LinMobileConstants.APP_VIEWS;
		for (String appView : allAppviews) {
			String[] appViewArray = appView.split(LinMobileConstants.ARRAY_SPLITTER);
			if(appViewId.contains(appViewArray[0].trim())) {
				CommonDTO commonDTO = new CommonDTO(appViewArray[0], appViewArray[1]);
				appViewList.add(commonDTO);
			}
		}
		return appViewList;
	}
	
	@Override
	public Map<String, String> loadAdvertisersFromBigQuery(String publisherIdsForBigQuery, String advertiserIds) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Map<String, String> advertiserMap = new LinkedHashMap<String, String>();
		try {
			
			if(publisherIdsForBigQuery !=null && publisherIdsForBigQuery.trim().length() ==0){
				log.warning("User has no publisherIdsForBigQuery....");
			}else{
				String advertiserId=advertiserIds.toString();
				if(advertiserId !=null && advertiserId.contains(LinMobileConstants.NO_RESTRICTIONS)){
					advertiserId="";
				}
				
				advertiserMap = MemcacheUtil.getBQAdvertisersFromCache(publisherIdsForBigQuery, advertiserId);
				if(advertiserMap == null) {
					advertiserMap = dao.loadAdvertisersFromBigQuery(publisherIdsForBigQuery, advertiserId);
					MemcacheUtil.setBQAdvertisersInCache(advertiserMap, publisherIdsForBigQuery, advertiserId);
				}
				
			}
		}
		catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
		}
		return advertiserMap;
	}
	
	@Override
	public Map<String, String> loadAgenciesFromBigQuery(String publisherIdsForBigQuery, String agencyIds) {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Map<String, String> agencyMap = new LinkedHashMap<String, String>();
		try {
			
			if(publisherIdsForBigQuery !=null && publisherIdsForBigQuery.trim().length() ==0){
				log.warning("User has no publisherIdsForBigQuery....");
			}else{
				String agencyId=agencyIds.toString();
				if(agencyId !=null && agencyId.contains(LinMobileConstants.NO_RESTRICTIONS)){
					agencyId="";
				}
				
				agencyMap = MemcacheUtil.getBQAgenciesFromCache(publisherIdsForBigQuery, agencyId);
				if(agencyMap == null) {
					agencyMap = dao.loadAgenciesFromBigQuery(publisherIdsForBigQuery, agencyId);
					MemcacheUtil.setBQAgenciesInCache(agencyMap, publisherIdsForBigQuery, agencyId);
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
		}
		return agencyMap;
	}
	
	public static Map<String, String> getAllAppViewsMap() {
		Map<String, String> appViewMap = new LinkedHashMap<String, String>();
		if(LinMobileConstants.APP_VIEWS != null && LinMobileConstants.APP_VIEWS.length > 0) {
			for(String appView : LinMobileConstants.APP_VIEWS) {
				appViewMap.put(appView.split(LinMobileConstants.ARRAY_SPLITTER)[0].trim(), appView.split(LinMobileConstants.ARRAY_SPLITTER)[1].trim()); 
			}
		}
		return appViewMap;
	}

	@Override
	public void accountSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
			IUserDetailsDAO dao = new UserDetailsDAO();
			IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
			List<CompanyObj> companyObjList = null;
			List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
			Map<String, String> industryMap = mediaPlanService.loadAllIndustry();
			if(superAdmin) {
				companyObjList = MemcacheUtil.getAllCompanyList();
			}
			else {
				companyObjList = dao.getSelectedCompaniesByUserId(userId);
			}
			/*Map<String, AdServerCredentialsDTO> dFPCredentialsMap = getCompanyDFPCredentials(companyObjList);
			if(dFPCredentialsMap != null && !dFPCredentialsMap.isEmpty()) {
				for(String key : dFPCredentialsMap.keySet()) {
					AdServerCredentialsDTO adServerCredentialsDTO = dFPCredentialsMap.get(key);
					if(adServerCredentialsDTO != null) {
						accountsObjList.addAll(prepareAccountsListForSetup(MemcacheUtil.getAllAccountsObjList(adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerUsername()), industryMap));
					}
				}
			}*/
			if(companyObjList != null) {
				for(CompanyObj companyObj : companyObjList) {
					accountsObjList.addAll(prepareAccountsListForSetup(MemcacheUtil.getAllAccountsObjList(companyObj.getId()+""), industryMap, companyObj.getCompanyName()));
				}
			}
			//
			userDetailsDTO.setAccountsObjList(accountsObjList);
		}

	private List<AccountsEntity> prepareAccountsListForSetup(List<AccountsEntity> allAccountsObjList, Map<String, String> industryMap, String companyName) {
		List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
		if(allAccountsObjList != null && allAccountsObjList.size()> 0) {
			for (AccountsEntity accountsObj : allAccountsObjList) {
				if(accountsObj != null) {
					accountsObj.setAdServerId(accountsObj.getAdServerId().trim() + " ("+accountsObj.getAdServerUserName().trim()+")");
					accountsObj.setCompanyId(companyName);
					accountsObj.setIndustry(industryMap.get(accountsObj.getIndustry()));
					accountsObjList.add(accountsObj);
				}
			}
		}
		return accountsObjList;
	}
	
	private List<PropertyObj> preparePropertiesListForSetup(List<PropertyObj> allPropertyObjList, String companyName) {
		List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
		if(allPropertyObjList != null && allPropertyObjList.size()> 0) {
			for (PropertyObj propertyObj : allPropertyObjList) {
				if(propertyObj != null) {
					propertyObj.setAdServerId(propertyObj.getAdServerId().trim() + " ("+propertyObj.getAdServerUserName().trim()+")");
					propertyObj.setCompanyId(companyName);
					propertyObjList.add(propertyObj);
				}
			}
		}
		return propertyObjList;
	}

	@Override
	public Boolean createAccount(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false; 
		String networkId = "";
		String networkUsername = "";
		AccountsEntity accountsObj = new AccountsEntity();
		try {
			accountsObj.setCreationDate(DateUtil.getDateYYYYMMDDHHMMSS());
			accountsObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
			accountsObj.setLastModifiedByUserId(userId);
			accountsObj.setCreatedByUserId(userId);
			accountsObj.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
			
			networkId = userDetailsDTO.getNetworkId().trim();
			accountsObj.setAdServerId(userDetailsDTO.getNetworkId().trim());
			networkUsername = userDetailsDTO.getNetworkUsername().trim();
			accountsObj.setAdServerUserName(userDetailsDTO.getNetworkUsername().trim());
			accountsObj.setAccountDfpId(userDetailsDTO.getAccountDfpId().trim());
			
			accountsObj.setCompanyId(userDetailsDTO.getCompanyId().trim());
			
			accountsObj.setAccountType(userDetailsDTO.getAccountType().trim());
			
			if(userDetailsDTO.getAccountType().trim().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
				accountsObj.setAccountName(userDetailsDTO.getAccountName().trim() + " ("+LinMobileConstants.ADVERTISER_ID_PREFIX+")");
			}
			else if(userDetailsDTO.getAccountType().trim().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
				accountsObj.setAccountName(userDetailsDTO.getAccountName().trim() + " ("+LinMobileConstants.AGENCY_ID_PREFIX+")");
			}
				
			if(userDetailsDTO.getDfpAccountName() != null) {
				accountsObj.setDfpAccountName(userDetailsDTO.getDfpAccountName().trim());
			}
			if(userDetailsDTO.getIndustry() != null && !userDetailsDTO.getIndustry().trim().equalsIgnoreCase("") && !userDetailsDTO.getIndustry().trim().equalsIgnoreCase("-1")) {
				accountsObj.setIndustry(userDetailsDTO.getIndustry().trim());
			}
			if(userDetailsDTO.getWebURL() != null) {
				accountsObj.setWebURL(userDetailsDTO.getWebURL().trim());
			}
			if(userDetailsDTO.getEmailId() != null) {
				accountsObj.setEmail(userDetailsDTO.getEmailId().trim());
			}
			if(userDetailsDTO.getPhone() != null) {
				accountsObj.setPhone(userDetailsDTO.getPhone().trim());
			}
			if(userDetailsDTO.getFax() != null) {
				accountsObj.setFax(userDetailsDTO.getFax().trim());
			}
			if(userDetailsDTO.getContact() != null) {
				accountsObj.setContact(userDetailsDTO.getContact().trim());
			}
			
			String uniqueNumber = networkId + "__" + networkUsername + "__" + userDetailsDTO.getAccountDfpId().trim() + "__" + accountsObj.getCompanyId();
			accountsObj.setId(uniqueNumber);
			/*if(MemcacheUtil.selfUpdateMemcacheAccountsObj(accountsObj, true, networkId, networkUsername, "", "")) {*/
			if(MemcacheUtil.selfUpdateMemcacheAccountsObj(accountsObj, true)) {
			//
				dao.saveObject(accountsObj);
				result = true;
			}
		}catch (Exception e) {
			log.severe("Exception in createAccount of UserService : "+e.getMessage());
			
			throw e;
		}
		return result;
	}

	@Override
	public boolean initEditAccount(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		String accountName = "";
		CompanyObj companyObj = null;
		try {
			AccountsEntity accountsObj = dao.getAccountsObjById(userDetailsDTO.getAccountId());
			if(accountsObj != null) {
				if(!superAdmin) {
					companyObj = getCompanyIdByAdminUserId(userId);
					if(companyObj == null || companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[1]) || companyObj.getAdServerId()== null || !(companyObj.getAdServerId().contains(accountsObj.getAdServerId())) || !(accountsObj.getCompanyId().equals(companyObj.getId()+""))) {
						return false;
					}
				}
				else {
					companyObj = dao.getCompanyById(Long.valueOf(accountsObj.getCompanyId()), MemcacheUtil.getAllCompanyList());
				}
				userDetailsDTO.setNetworkId(accountsObj.getAdServerId());
				userDetailsDTO.setNetworkUsername(accountsObj.getAdServerUserName());
				userDetailsDTO.setAccountId(accountsObj.getId());
				userDetailsDTO.setAccountDfpId(accountsObj.getAccountDfpId());
				accountName = accountsObj.getAccountName();
				if(accountName != null && accountName.endsWith("("+LinMobileConstants.AGENCY_ID_PREFIX+")")) {
					accountName = accountName.substring(0, accountName.lastIndexOf("("+LinMobileConstants.AGENCY_ID_PREFIX+")"));
				}
				else if(accountName != null && accountName.endsWith("("+LinMobileConstants.ADVERTISER_ID_PREFIX+")")) {
					accountName = accountName.substring(0, accountName.lastIndexOf("("+LinMobileConstants.ADVERTISER_ID_PREFIX+")"));
				}
				userDetailsDTO.setDefaultSelectedCompanyType(companyObj.getCompanyType().trim());
				userDetailsDTO.setCompanyId(companyObj.getId()+"");
				userDetailsDTO.setAccountName(accountName.trim());
				userDetailsDTO.setCompanyName(companyObj.getCompanyName());
				userDetailsDTO.setAccountType(accountsObj.getAccountType());
				userDetailsDTO.setStatus(accountsObj.getStatus());
				userDetailsDTO.setDfpAccountName(accountsObj.getDfpAccountName());
				userDetailsDTO.setIndustry(accountsObj.getIndustry());
				userDetailsDTO.setWebURL(accountsObj.getWebURL());
				userDetailsDTO.setContact(accountsObj.getContact());
				userDetailsDTO.setPhone(accountsObj.getPhone());
				userDetailsDTO.setEmailId(accountsObj.getEmail());
				userDetailsDTO.setFax(accountsObj.getFax());
				
				result = true;
			}
		} catch (Exception e) {
			log.severe("Exception in initEditAccount of UserService : "+e.getMessage());
			
			throw e;
		}
		return result;
	}

	@Override
	public Boolean updateAccount(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		Boolean result = false;
		try {
			AccountsEntity accountsObj = dao.getAccountsObjById(userDetailsDTO.getAccountId());
			if(accountsObj != null) {
				maintainAccountObjHistory(accountsObj, userId);
				accountsObj.setLastModifiedDate(DateUtil.getDateYYYYMMDDHHMMSS());
				accountsObj.setLastModifiedByUserId(userId);
				accountsObj.setStatus(userDetailsDTO.getStatus());
				if(userDetailsDTO.getAccountType().trim().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
					accountsObj.setAccountName(userDetailsDTO.getAccountName().trim() + " ("+LinMobileConstants.ADVERTISER_ID_PREFIX+")");
				}
				else if(userDetailsDTO.getAccountType().trim().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
					accountsObj.setAccountName(userDetailsDTO.getAccountName().trim() + " ("+LinMobileConstants.AGENCY_ID_PREFIX+")");
				}
				if(userDetailsDTO.getIndustry() != null && !userDetailsDTO.getIndustry().trim().equalsIgnoreCase("") && !userDetailsDTO.getIndustry().trim().equalsIgnoreCase("-1")) {
					accountsObj.setIndustry(userDetailsDTO.getIndustry().trim());
				}
				if(userDetailsDTO.getWebURL() != null) {
					accountsObj.setWebURL(userDetailsDTO.getWebURL().trim());
				}
				if(userDetailsDTO.getEmailId() != null) {
					accountsObj.setEmail(userDetailsDTO.getEmailId().trim());
				}
				if(userDetailsDTO.getPhone() != null) {
					accountsObj.setPhone(userDetailsDTO.getPhone().trim());
				}
				if(userDetailsDTO.getFax() != null) {
					accountsObj.setFax(userDetailsDTO.getFax().trim());
				}
				if(userDetailsDTO.getContact() != null) {
					accountsObj.setContact(userDetailsDTO.getContact().trim());
				}
				
				if(MemcacheUtil.selfUpdateMemcacheAccountsObj(accountsObj, false)) {
					dao.saveObject(accountsObj);
					result = true;
				}
			}
		}catch (Exception e) {
			log.severe("Exception in updateAccount of UserService : "+e.getMessage());
			
			throw e;
		}
		return result;
	}

	private void maintainAccountObjHistory(AccountsEntity accountsObj, long userId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		
		AccountsHistObj accountsHistObj = new AccountsHistObj();
		accountsHistObj.setId(accountsObj.getId());
		accountsHistObj.setAccountDfpId(accountsObj.getAccountDfpId());
		accountsHistObj.setAccountName(accountsObj.getAccountName());
		accountsHistObj.setAccountType(accountsObj.getAccountType());
		accountsHistObj.setStatus(accountsObj.getStatus());
		accountsHistObj.setDfpAccountName(accountsObj.getDfpAccountName());
		accountsHistObj.setIndustry(accountsObj.getIndustry());
		accountsHistObj.setWebURL(accountsObj.getWebURL());
		accountsHistObj.setContact(accountsObj.getContact());
		accountsHistObj.setPhone(accountsObj.getPhone());
		accountsHistObj.setEmail(accountsObj.getEmail());
		accountsHistObj.setFax(accountsObj.getFax());
		accountsHistObj.setAdServerId(accountsObj.getAdServerId());
		accountsHistObj.setAdServerUserName(accountsObj.getAdServerUserName());
		accountsHistObj.setCompanyId(accountsObj.getCompanyId());
		accountsHistObj.setCreationDate(accountsObj.getCreationDate());
		accountsHistObj.setCreatedByUserId(accountsObj.getCreatedByUserId());
		accountsHistObj.setLastModifiedDate(accountsObj.getLastModifiedDate());
		accountsHistObj.setLastModifiedByUserId(accountsObj.getLastModifiedByUserId());
		accountsHistObj.setHistoryDate(DateUtil.getDateYYYYMMDDHHMMSS());
		accountsHistObj.setUpdateDeleteByUserId(userId);
		
		dao.saveObject(accountsHistObj);
		
	}
	
	@Override
	public String convertListToSeperatedString(List<String> list, String seperator) {
		String returnStr ="";
		try {
			if(list != null && list.size() > 0){
				for (String str : list) {
					if(str != null && str.trim().length() > 0) {
						returnStr = returnStr + str + seperator;
					}
				}
				if(returnStr.lastIndexOf(seperator) != -1) {
					returnStr = returnStr.substring(0, returnStr.lastIndexOf(seperator));
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in convertListToSeperatedString : "+e.getMessage());
			
		}
		return returnStr;
	}

	@Override
	public List<AdServerCredentialsDTO> loadAdserverCredentialsForDropDown(List<CompanyObj> companyObjList) throws Exception {
		List<AdServerCredentialsDTO> adServerCredentialsDTOList = new ArrayList<AdServerCredentialsDTO>();
		List<String> tempList = new ArrayList<String>();
		if(companyObjList != null && companyObjList.size() > 0) {
			try {
				for(CompanyObj companyObj : companyObjList) {
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && (companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size()) && (companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size())) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && !companyObj.getAdServerId().get(i).trim().equals("") && companyObj.getAdServerUsername().get(i) != null && !companyObj.getAdServerUsername().get(i).trim().equals("") && companyObj.getAdServerPassword().get(i) != null && !companyObj.getAdServerPassword().get(i).trim().equals("")) {
								String adServerIdUsername = companyObj.getAdServerId().get(i).trim() +"<SEP>"+ companyObj.getAdServerUsername().get(i).trim();
								if(!(tempList.contains(adServerIdUsername))) {
									AdServerCredentialsDTO obj =  new AdServerCredentialsDTO();
									obj.setAdServerId(companyObj.getAdServerId().get(i).trim());
									obj.setAdServerUsername(companyObj.getAdServerUsername().get(i).trim());
									obj.setAdServerPassword(companyObj.getAdServerPassword().get(i).trim());
									obj.setAdServerIdUsername(adServerIdUsername);
									obj.setCustomizedValue(companyObj.getAdServerId().get(i).trim() + " ("+companyObj.getAdServerUsername().get(i).trim()+")");
									tempList.add(adServerIdUsername);
									adServerCredentialsDTOList.add(obj);
								}
							}
						}
					}
				}
			}
			catch (Exception e) {
				log.severe("Exception in getCompanyDFPCredentials of UserService : "+e.getMessage());
				
			}
		}
		log.info("adServerCredentialsDTOList size : "+adServerCredentialsDTOList.size());
		return adServerCredentialsDTOList;
	}
	
	
	/* author = Naresh Pokhriyal 
	  gets id(prefixed with accountType if addPrefix is set TRUE) and AccountName 
	   for all active accounts for given companyId */
	@Override
	public List<CommonDTO> getActiveAccountsByCompanyId(String companyId, boolean addPrefix) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<CommonDTO> selectedAccountsList = new ArrayList<CommonDTO>();
		List<AccountsEntity> accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
		if(accountsObjList != null && accountsObjList.size() > 0) {
			for (AccountsEntity accountsObj : accountsObjList) {
				if(accountsObj != null && accountsObj.getId() != null && accountsObj.getAccountName() != null && accountsObj.getStatus() != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getDfpAccountName() != null && accountsObj.getAccountDfpId() != null && accountsObj.getAccountType() != null) {
					if(addPrefix) {
						selectedAccountsList.add(new CommonDTO(accountsObj.getAccountType()+accountsObj.getId(), accountsObj.getAccountName()));
					}
					else {
						selectedAccountsList.add(new CommonDTO(accountsObj.getId(), accountsObj.getAccountName()));
					}
				}
			}
		}
		//
		log.info("selectedAccountsList size : "+selectedAccountsList.size());
		return selectedAccountsList;
	}
	
	/* author = Naresh Pokhriyal 
	   gets AccountDfpId prefixed with '_' and AccountName for all active Accounts based
	   on DFP Credential for the given companyId. 
	   gets advetisers if getAdvetisers is set TRUE, 
	   gets agencies if getAgencies is set TRUE,*/
	@Override
	public Map<String, String> getActiveAccountsForDropDown(String companyId, boolean getAdvertiser, boolean getAgencies) throws Exception {
		log.info("In getActiveAccountsForDropDown, companyId : "+companyId+", getAdvertiser : "+getAdvertiser+", getAgencies : "+getAgencies);
		IUserDetailsDAO dao = new UserDetailsDAO();
		Map<String, String> selectedAdvertisersMap = new LinkedHashMap<String, String>();
		List<AccountsEntity> accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
		if(accountsObjList != null && accountsObjList.size() > 0) {
			for (AccountsEntity accountsObj : accountsObjList) {
				if(accountsObj != null && accountsObj.getId() != null && accountsObj.getAccountName() != null && accountsObj.getStatus() != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getDfpAccountName() != null && accountsObj.getAccountDfpId() != null && accountsObj.getAccountType() != null) {
					if(getAdvertiser && getAgencies) {
						selectedAdvertisersMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
					}
					else if(getAdvertiser && accountsObj.getAccountType() != null && accountsObj.getAccountType().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
						selectedAdvertisersMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
					}
					else if(getAgencies && accountsObj.getAccountType() != null && accountsObj.getAccountType().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
						selectedAdvertisersMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
					}
				}
			}
		}
		//
		log.info("selectedAdvertisersMap size : "+selectedAdvertisersMap.size());
		return selectedAdvertisersMap;
	}

	@Override
	public List<AccountsEntity> getActiveAccountsObjByCompanyId(String companyId) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<AccountsEntity> selectedAccountsList = new ArrayList<AccountsEntity>();
		List<AccountsEntity> accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
		if(accountsObjList != null && accountsObjList.size() > 0) {
			for (AccountsEntity accountsObj : accountsObjList) {
				if(accountsObj != null && accountsObj.getId() != null && accountsObj.getAccountName() != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getDfpAccountName() != null && accountsObj.getAccountDfpId() != null) {
					selectedAccountsList.add(accountsObj);
				}
			}
		}
		log.info("selectedAccountsList size : "+selectedAccountsList.size());
		return selectedAccountsList;
	}
	
	
	/*
	 * This method will return logged in user's accounts
	 * 
	 * @author Naresh Pokhriyal 
	 * @param long userId 
	 * @return Map<String,String>
	 */
	@Override
	public Map<String, String> getSelectedAccountsByUserId(long userId, boolean getAdvertiser, boolean getAgency) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		 Map<String, String> selectedAccountsMap = new LinkedHashMap<String, String>();
		 Map<String, List<AccountsEntity>> accountsObjMap = new LinkedHashMap<String, List<AccountsEntity>>();
		 List<CompanyObj> allCompanyObjList = MemcacheUtil.getAllCompanyList();
		 CompanyObj companyObj = null;
		 
		 UserDetailsObj userDetailsObj = dao.getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
				// NO RESTRICTIONS
				 CompanyObj companyForSuperUser = dao.getCompanyById(Long.valueOf("1"), allCompanyObjList);
				 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(companyForSuperUser), "", "", getAdvertiser, getAgency));
			 }
			 else if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[1])) {
				// NO RESTRICTIONS
				 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(getCompanyIdByAdminUserId(userId)), "", "", getAdvertiser, getAgency));
			 }
			 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
				 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 for (String teamId : teamIdList) {
						 TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
						 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && ((teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) || (teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().size() > 0))) {
							 if(companyObj == null) {
								 companyObj = dao.getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), allCompanyObjList);
							 }
							 if(companyObj != null && companyObj.isAccessToAccounts()) {
								 // NO RESTRICTIONS
								 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS)) {
									 selectedAccountsMap.clear();
									 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(companyObj), "", "", getAdvertiser, getAgency));
									 break;
								 }
								 else if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES)) {
									 // ALL ADVERTISERS and AGENCIES
									 selectedAccountsMap.clear();
									 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
									 break;
								 }
								 else {
									 // SELECTED ADVERTISERS and AGENCIES
									 if(getAdvertiser && teamPropertiesObj.getAdvertiserId() != null) {
										 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
											 /*if(advertiserId != null && (advertiserId.split("__")).length == 3) {*/
											 if(advertiserId != null && (advertiserId.split("__")).length == 4) {
											 //
												 AccountsEntity accountsObj = null;
												 List<AccountsEntity> accountsObjList = null;
												 String[] advertiser = advertiserId.split("__");
												 if(!selectedAccountsMap.containsKey(advertiser[2])) {
													 if(accountsObjMap.containsKey(advertiser[0]+advertiser[1])) {
														 accountsObjList = accountsObjMap.get(advertiser[0]+advertiser[1]);
													 }
													 else {
														 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
														 accountsObjMap.put(advertiser[0]+advertiser[1], accountsObjList);
													 }
													 accountsObj = dao.getAccountsObjById(advertiserId, accountsObjList);
													 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
														 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
													 }
												 }
											 }
										 }
									 }
									 if(getAgency && teamPropertiesObj.getAgencyId() != null) {
										 for(String agencyId : teamPropertiesObj.getAgencyId()) {
											 if(agencyId != null && (agencyId.split("__")).length == 4) {
												 AccountsEntity accountsObj = null;
												 List<AccountsEntity> accountsObjList = null;
												 String[] agency = agencyId.split("__");
												 if(!selectedAccountsMap.containsKey(agency[2])) {
													 if(accountsObjMap.containsKey(agency[0]+agency[1])) {
														 accountsObjList = accountsObjMap.get(agency[0]+agency[1]);
													 }
													 else {
														 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
														 accountsObjMap.put(agency[0]+agency[1], accountsObjList);
													 }
													 accountsObj = dao.getAccountsObjById(agencyId, accountsObjList);
													 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
														 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
													 }
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedAccountsMap;
	 }
	
	public Map<String,String> loadAdvertiserAndAgencyInBQ(String publisherIdInBQ,String advertiserIds,String agencyIds, boolean getAdvetisers, boolean getAgencies) throws DataServiceException, IOException {
		Map<String,String> map = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		//ILinMobileBusinessService linMobileBusinessService = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		try {
			if(!publisherIdInBQ.isEmpty()) {
				String key = publisherIdInBQ + advertiserIds + agencyIds + getAdvetisers + getAgencies;
				map = MemcacheUtil.getBQAccountsFromCache(key);
				if(map == null || map.size() == 0) {
					QueryDTO queryDTO = (new NewAdvertiserViewService()).getQueryDTOForCampaignPerformance(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
					map = dao.loadAdvertiserAndAgencyInBQ(advertiserIds, agencyIds, getAdvetisers, getAgencies, queryDTO);
					if(map != null && map.size() > 0) {
						MemcacheUtil.setBQAccountsInCache(map, key);
					}
				}
			}
		} catch(Exception e) {
			log.severe("Esception in loadAdvertiserAndAgencyInBQ : "+e.getMessage());
			
		}
		return map;
	}
	
	@Override
	public Map<String, String> getSelectedAccountsByUserIdAndCompanyId(long userId, String companyId, boolean getAdvertiser, boolean getAgency) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		 Map<String, String> selectedAccountsMap = new LinkedHashMap<String, String>();
		 Map<String, List<AccountsEntity>> accountsObjMap = new LinkedHashMap<String, List<AccountsEntity>>();
		 List<CompanyObj> allCompanyObjList = MemcacheUtil.getAllCompanyList();
		 CompanyObj companyObj = null;
		 
		 UserDetailsObj userDetailsObj = dao.getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null && companyId != null) {
			 companyObj = dao.getCompanyById(Long.valueOf(companyId), allCompanyObjList);
			 if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
				 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
				 if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					// NO RESTRICTIONS
					 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(companyObj), "", "", getAdvertiser, getAgency));
				 }
				 else if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[1])) {
					// NO RESTRICTIONS
					 CompanyObj obj = getCompanyIdByAdminUserId(userId);
					 if(obj != null && (companyObj.getId()+"").equals(obj.getId()+"")) {
						 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(companyObj), "", "", getAdvertiser, getAgency));
					 }
				 }
				 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
					 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					 List<String> teamIdList = userDetailsObj.getTeams();
					 CompanyObj obj = null;
					 if(teamIdList != null && teamIdList.size() > 0) {
						 for (String teamId : teamIdList) {
							 TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && ((teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) || (teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().size() > 0))) {
								 if(obj == null) {
									 obj = dao.getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), allCompanyObjList);
								 }
								 if(obj != null && companyObj != null && (companyObj.getId()+"").equals(obj.getId()+"") && companyObj.isAccessToAccounts()) {
									 // NO RESTRICTIONS
									 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS)) {
										 selectedAccountsMap.clear();
										 selectedAccountsMap.putAll(loadAdvertiserAndAgencyInBQ(getPublisherBQId(companyObj), "", "", getAdvertiser, getAgency));
										 break;
									 }
									 else if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES)) {
										 // ALL ADVERTISERS and AGENCIES
										 selectedAccountsMap.clear();
										 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
										 break;
									 }
									 else {
										 // SELECTED ADVERTISERS and AGENCIES
										 if(getAdvertiser && teamPropertiesObj.getAdvertiserId() != null) {
											 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
												 /*if(advertiserId != null && (advertiserId.split("__")).length == 3) {*/
												 if(advertiserId != null && (advertiserId.split("__")).length == 4) {
												 //
													 AccountsEntity accountsObj = null;
													 List<AccountsEntity> accountsObjList = null;
													 String[] advertiser = advertiserId.split("__");
													 if(!selectedAccountsMap.containsKey(advertiser[2])) {
														 if(accountsObjMap.containsKey(advertiser[0]+advertiser[1])) {
															 accountsObjList = accountsObjMap.get(advertiser[0]+advertiser[1]);
														 }
														 else {
															 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
															 accountsObjMap.put(advertiser[0]+advertiser[1], accountsObjList);
														 }
														 accountsObj = dao.getAccountsObjById(advertiserId, accountsObjList);
														 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
															 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
														 }
													 }
												 }
											 }
										 }
										 if(getAgency && teamPropertiesObj.getAgencyId() != null) {
											 for(String agencyId : teamPropertiesObj.getAgencyId()) {
												 if(agencyId != null && (agencyId.split("__")).length == 4) {
													 AccountsEntity accountsObj = null;
													 List<AccountsEntity> accountsObjList = null;
													 String[] agency = agencyId.split("__");
													 if(!selectedAccountsMap.containsKey(agency[2])) {
														 if(accountsObjMap.containsKey(agency[0]+agency[1])) {
															 accountsObjList = accountsObjMap.get(agency[0]+agency[1]);
														 }
														 else {
															 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
															 accountsObjMap.put(agency[0]+agency[1], accountsObjList);
														 }
														 accountsObj = dao.getAccountsObjById(agencyId, accountsObjList);
														 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
															 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
														 }
													 }
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedAccountsMap;
	 }
	
	@Override
	public Map<String, String> getSelectedAccountsForCampaingnPlanning(long userId, String companyId, boolean getAdvertiser, boolean getAgency) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		 Map<String, String> selectedAccountsMap = new LinkedHashMap<String, String>();
		 Map<String, List<AccountsEntity>> accountsObjMap = new LinkedHashMap<String, List<AccountsEntity>>();
		 List<CompanyObj> companyObjList = new ArrayList<CompanyObj>();
		 List<CompanyObj> allCompanyObjList = MemcacheUtil.getAllCompanyList();
		 CompanyObj companyObj = null;
		 
		 UserDetailsObj userDetailsObj = dao.getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null && companyId != null) {
			 companyObj = dao.getCompanyById(Long.valueOf(companyId), allCompanyObjList);
			 if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
				 companyObjList.add(companyObj);
				 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
				 if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					 // ALL ADVERTISERS and AGENCIES
					 log.info("getting all advertisers or agencies for SUPER ADMIN");
					 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
				 }
				 else if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[1])) {
					 // ALL ADVERTISERS and AGENCIES
					 log.info("getting all advertisers or agencies for ADMIN");
					 CompanyObj obj = getCompanyIdByAdminUserId(userId);
					 if(obj != null && (companyObj.getId()+"").equals(obj.getId()+"")) {
						 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
					 }
				 }
				 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
					 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					 List<String> teamIdList = userDetailsObj.getTeams();
					 CompanyObj obj = null;
					 if(teamIdList != null && teamIdList.size() > 0) {
						 for (String teamId : teamIdList) {
							 TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && ((teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) || (teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().size() > 0))) {
								 if(obj == null) {
									 obj = dao.getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), allCompanyObjList);
								 }
								 if(obj != null && companyObj != null && (companyObj.getId()+"").equals(obj.getId()+"") && companyObj.isAccessToAccounts()) {
									 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS)) {
										// ALL ADVERTISERS and AGENCIES
										 selectedAccountsMap.clear();
										 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
										 break;
									 }
									 else if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES)) {
										 // ALL ADVERTISERS and AGENCIES
										 selectedAccountsMap.clear();
										 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
										 break;
									 }
									 else {
										 // SELECTED ADVERTISERS and AGENCIES
										 if(getAdvertiser && teamPropertiesObj.getAdvertiserId() != null) {
											 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
												 if(advertiserId != null && (advertiserId.split("__")).length == 4) {
													 AccountsEntity accountsObj = null;
													 List<AccountsEntity> accountsObjList = null;
													 String[] advertiser = advertiserId.split("__");
													 if(!selectedAccountsMap.containsKey(advertiser[2])) {
														 if(accountsObjMap.containsKey(advertiser[0]+advertiser[1])) {
															 accountsObjList = accountsObjMap.get(advertiser[0]+advertiser[1]);
														 }
														 else {
															 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
															 accountsObjMap.put(advertiser[0]+advertiser[1], accountsObjList);
														 }
														 accountsObj = dao.getAccountsObjById(advertiserId, accountsObjList);
														 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
															 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
														 }
													 }
												 }
											 }
										 }
										 if(getAgency && teamPropertiesObj.getAgencyId() != null) {
											 for(String agencyId : teamPropertiesObj.getAgencyId()) {
												 if(agencyId != null && (agencyId.split("__")).length == 4) {
													 AccountsEntity accountsObj = null;
													 List<AccountsEntity> accountsObjList = null;
													 String[] agency = agencyId.split("__");
													 if(!selectedAccountsMap.containsKey(agency[2])) {
														 if(accountsObjMap.containsKey(agency[0]+agency[1])) {
															 accountsObjList = accountsObjMap.get(agency[0]+agency[1]);
														 }
														 else {
															 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
															 accountsObjMap.put(agency[0]+agency[1], accountsObjList);
														 }
														 accountsObj = dao.getAccountsObjById(agencyId, accountsObjList);
														 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
															 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
														 }
													 }
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedAccountsMap;
	 }
	
	@Override
	public List<PropertyObj> getSiteIdsByBqIdentifier(String bqIdentifier) {
		List<PropertyObj> siteIdlist = new ArrayList<PropertyObj>();
		try {
			if(bqIdentifier != null && bqIdentifier.trim().length() > 0) {
				bqIdentifier = bqIdentifier.trim();
				IUserDetailsDAO dao = new UserDetailsDAO();
				CompanyObj companyObj = dao.getCompanyByBqIdentifier(bqIdentifier, MemcacheUtil.getAllCompanyList());
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])){
					List<PropertyObj> propertyObjList = MemcacheUtil.getAllPropertiesList(companyObj.getId()+"");
					if(propertyObjList != null && propertyObjList.size() > 0) {
						for (PropertyObj propertyObj : propertyObjList) {
							if(propertyObj != null && propertyObj.getAdServerId() != null && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
								siteIdlist.add(propertyObj);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			log.severe("Exception in getSiteIds in UserService : "+e.getMessage()); 
			
		}
		log.info("siteIdlist size : "+siteIdlist.size());
		return siteIdlist;
	}
	
	@Override
	public Map<String,String> getAdunit1(List<PropertyObj> propertyObjList) {
		Map<String,String> siteMap = new LinkedHashMap<String, String>();
		if(propertyObjList != null && propertyObjList.size() > 0){
			for (PropertyObj propertyObj : propertyObjList) {
				if(propertyObj != null && propertyObj.getDFPPropertyId() != null && propertyObj.getDFPPropertyId().trim().length() > 0) {
					siteMap.put(propertyObj.getDFPPropertyId().trim(), propertyObj.getDFPPropertyName());
				}
			}
		}
		return siteMap;
	}
	
	/*
	 * This method will return logged in user's accounts
	 * only if 
	 * @author Naresh Pokhriyal 
	 * @param long userId 
	 * @return Map<String,String>
	 */
	@Override
	public String getSelectedAccountsInfo(long userId, boolean getAdvertiser, boolean getAgency) throws Exception {
		IUserDetailsDAO dao = new UserDetailsDAO();
		String returnStr = "";
		 Map<String, String> selectedAccountsMap = new LinkedHashMap<String, String>();
		 Map<String, List<AccountsEntity>> accountsObjMap = new LinkedHashMap<String, List<AccountsEntity>>();
		 List<CompanyObj> allCompanyObjList = MemcacheUtil.getAllCompanyList();
		 CompanyObj companyObj = null;
		 
		 UserDetailsObj userDetailsObj = dao.getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
				// NO RESTRICTIONS
				 return "NO RESTRICTIONS";
			 }
			 else if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[1])) {
				// NO RESTRICTIONS
				 return "NO RESTRICTIONS";
			 }
			 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
				 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 for (String teamId : teamIdList) {
						 TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
						 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && ((teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) || (teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().size() > 0))) {
							 if(companyObj == null) {
								 companyObj = dao.getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), allCompanyObjList);
							 }
							 if(companyObj != null && companyObj.isAccessToAccounts()) {
								 // NO RESTRICTIONS
								 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.NO_RESTRICTIONS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.NO_RESTRICTIONS)) {
									 return "NO RESTRICTIONS";
								 }
								 else if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().contains(LinMobileConstants.ALL_ADVERTISERS) && teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().contains(LinMobileConstants.ALL_AGENCIES)) {
									 // ALL ADVERTISERS and AGENCIES
									 selectedAccountsMap.clear();
									 selectedAccountsMap.putAll(getActiveAccountsForDropDown(companyObj.getId()+"", getAdvertiser, getAgency));
									 break;
								 }
								 else {
									 // SELECTED ADVERTISERS and AGENCIES
									 if(getAdvertiser && teamPropertiesObj.getAdvertiserId() != null) {
										 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
											 /*if(advertiserId != null && (advertiserId.split("__")).length == 3) {*/
											 if(advertiserId != null && (advertiserId.split("__")).length == 4) {
											 //
												 AccountsEntity accountsObj = null;
												 List<AccountsEntity> accountsObjList = null;
												 String[] advertiser = advertiserId.split("__");
												 if(!selectedAccountsMap.containsKey(advertiser[2])) {
													 if(accountsObjMap.containsKey(advertiser[0]+advertiser[1])) {
														 accountsObjList = accountsObjMap.get(advertiser[0]+advertiser[1]);
													 }
													 else {
														 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
														 accountsObjMap.put(advertiser[0]+advertiser[1], accountsObjList);
													 }
													 accountsObj = dao.getAccountsObjById(advertiserId, accountsObjList);
													 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
														 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
													 }
												 }
											 }
										 }
									 }
									 if(getAgency && teamPropertiesObj.getAgencyId() != null) {
										 for(String agencyId : teamPropertiesObj.getAgencyId()) {
											 if(agencyId != null && (agencyId.split("__")).length == 4) {
												 AccountsEntity accountsObj = null;
												 List<AccountsEntity> accountsObjList = null;
												 String[] agency = agencyId.split("__");
												 if(!selectedAccountsMap.containsKey(agency[2])) {
													 if(accountsObjMap.containsKey(agency[0]+agency[1])) {
														 accountsObjList = accountsObjMap.get(agency[0]+agency[1]);
													 }
													 else {
														 accountsObjList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
														 accountsObjMap.put(agency[0]+agency[1], accountsObjList);
													 }
													 accountsObj = dao.getAccountsObjById(agencyId, accountsObjList);
													 if(accountsObj != null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountType().equals(LinMobileConstants.AGENCY_ID_PREFIX)) {
														 selectedAccountsMap.put("_"+accountsObj.getAccountDfpId(), accountsObj.getAccountName());
													 }
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 if(selectedAccountsMap != null && selectedAccountsMap.size() > 0) {
			 for(String key : selectedAccountsMap.keySet()) {
				 returnStr = returnStr + key.replaceAll("_", "") + ",";
			 }
		 }
		 if(returnStr.lastIndexOf(",") != -1) {
			 returnStr = returnStr.substring(0, returnStr.lastIndexOf(","));
		 }
		 return returnStr.trim();
	 }
	
	@Override
	public String getCompanyLogo(String companyId) {
		if(companyId != null && companyId.length() > 0) {
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			for(CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyLogoURL() != null && companyId.equals(companyObj.getId()+"")) {
					return companyObj.getCompanyLogoURL();
				}
			}
		}
		return "";
	}
	
	@Override
	public List<UserDetailsObj> getAllEmailOptedUsersList() {
		List<UserDetailsObj> emailOptUsers = new ArrayList<>();
		List<UserDetailsObj> userDetailsObjList = MemcacheUtil.getAllUsersList();
		if(userDetailsObjList != null && userDetailsObjList.size() > 0) {
			for(UserDetailsObj userDetailsObj : userDetailsObjList) {
				if(userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && userDetailsObj.isOptEmail()) {
					emailOptUsers.add(userDetailsObj);
				}
			}
		}
		return emailOptUsers;
	}
	
	/*
	 * This method will get all active users of active teams of the company
	 * @author Naresh Pokhriyal
	 * @param  long companyId
	 * @return List<UserDetailsObj>
	 */
	@Override
	public List<UserDetailsObj> getAllActiveUsersOfCompany(long companyId){
		log.info("getAllActiveUsersOfCompany companyId : "+companyId);
		IUserDetailsDAO dao = new UserDetailsDAO();
		List<UserDetailsObj> selectedUserDetailsObjList = null;
		try {
			List<TeamPropertiesObj> teamPropertiesObjList = dao.getAllActiveTeamsOfCompany(companyId+"");
			if(teamPropertiesObjList != null && teamPropertiesObjList.size() > 0) {
				List<String> teamIdList = new ArrayList<>();
				for (TeamPropertiesObj teamPropertiesObj : teamPropertiesObjList) {
					if(teamPropertiesObj != null && !(teamIdList.contains(teamPropertiesObj.getId()))) {
						teamIdList.add(teamPropertiesObj.getId());
					}
				}
				selectedUserDetailsObjList = dao.getAllActiveUsersOfTeam(teamIdList);
			}
			else {
				log.warning("No Active teams for companyId : "+companyId);
			}
		}
		catch(DataServiceException e) {
			log.severe("Exception in getUsersOfCompany : "+e.getMessage());
		}
		return selectedUserDetailsObjList;		
	}

	@Override
	public JSONObject resendAuthorizeEmail(String userId) throws Exception {
		log.info("In resendAuthorizeEmail, userId : "+userId);
		JSONObject jsonObject = new JSONObject();
		IUserDetailsDAO dao = new UserDetailsDAO();
		UserDetailsObj userDetailsObj = dao.getUserById(StringUtil.getLongValue(userId));
		if(userDetailsObj != null && userDetailsObj.getEmailId() != null) {
			if(userDetailsObj.getStatus().equals("Pending")) {
				boolean isEmailSendSuccessfully = sendActivationMail(userDetailsObj.getUserName(), StringUtil.getLongValue(userId), userDetailsObj.getEmailId(), "");
				if(!isEmailSendSuccessfully) {
					log.warning("Resending Invite failed");
					jsonObject.put("message", "Resending Invite failed");
					jsonObject.put("error", "yes");
				}else {
					log.warning("Invite Sent");
					jsonObject.put("message", "Invite Sent");
				}
			}else {
				log.warning("Invitation already accepted");
				jsonObject.put("message", "Invitation already accepted");
				jsonObject.put("userStatus", userDetailsObj.getStatus());
			}
		}else {
			log.warning("User not found");
			jsonObject.put("message", "User not found");
			jsonObject.put("error", "yes");
		}
		return jsonObject;
	}

	@Override
	public boolean deleteUser(long userIdToDelete, long deletedByUserId) throws Exception {
		log.info("In deleteUser, userIdToDelete : "+userIdToDelete+", deletedByUserId : "+deletedByUserId);
		boolean isUserDeleted = false;
		IUserDetailsDAO dao = new UserDetailsDAO();
		UserDetailsObj userDetailsObj = dao.getUserById(userIdToDelete);
		if(userDetailsObj != null) {
			maintainUserHistory(userDetailsObj, deletedByUserId, "Deleted");
			if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, false, true)) {
				userDetailsObj.setDeleted(true);
				dao.saveObject(userDetailsObj);
				isUserDeleted = true;
				log.info("User Archived successfully");
			}
		}else {
			log.warning("User not found");
		}
		if(!isUserDeleted){
			log.warning("User Archived failed");
		}
		return isUserDeleted;
	}

	public static Map updateAllUsers() {
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		Map map = new HashMap<>();
		map.put("status", false);
		map.put("message", "Failed");
		try {
			List<UserDetailsObj> userDetailsObjList = userDetailsDAO.getAllUsersIncludingDeleted();
			if(userDetailsObjList != null && userDetailsObjList.size() > 0) {
				for (UserDetailsObj userDetailsObj : userDetailsObjList) {
					userDetailsDAO.saveObjectWithStrongConsistancy(userDetailsObj);
				}
				log.info("UserDetailsObj updated, count : "+userDetailsObjList.size());
				MemcacheUtil.getAllUsersFromDB();
				log.info("Memcache Refreshed");
				map.put("status", true);
				map.put("message", "UserDetailsObj updated, count : "+userDetailsObjList.size());
			}else {
				log.info("UserDetailsObj is Empty");
				map.put("message", "UserDetailsObj is Empty");
			}
		} catch (Exception e) {
			log.severe("Exception:" + e.getMessage());
			map.put("message", "Exception:" + e.getMessage());
			
		}
		return map;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * Load all companies for a given company type
	 */
	@SuppressWarnings("unchecked")
	public List<CompanyObj> loadAllPartners() {
		log.info("Load all partners..");
		List<CompanyObj> companyList=null;
		List<CompanyObj> partnerList=null;
		IUserDetailsDAO userDAO=new UserDetailsDAO();
		try {
		
			companyList=(List<CompanyObj>) MemcacheUtil.getAllCompanyList();
			partnerList=userDAO.getAllPublishers(companyList);
			
			if(companyList==null || companyList.size()==0){
				log.warning("No company found in memcache/datastore ");
			}else{
				partnerList=userDAO.getAllPublishers(companyList);
			}
		} catch (Exception e) {
			log.severe("Exception in loading companies, DataServiceException: "+e.getMessage());
		}
		if(partnerList!=null){
			log.info(" returing partnerList : "+partnerList.size());
		}
		return partnerList;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * Load partner json (id, name) from company list
	 */
	public JSONObject loadPartnersJSON(List<CompanyObj> partnerList) throws JSONException {
		JSONObject partnerJSON=new JSONObject();
		if(partnerList!=null && partnerList.size()>0){
			JSONArray partnerArray=new JSONArray();
			for(CompanyObj companyObj:partnerList){
				JSONObject companyJson=new JSONObject();
				companyJson.put("id", companyObj.getId());
				companyJson.put("name", companyObj.getCompanyName());
				partnerArray.add(companyJson);
			}
			partnerJSON.put("partners", partnerArray);
		}else{
			partnerJSON.put("error", "No partner found");
		}
		return partnerJSON;
	}

	@Override
	public JSONObject searchAccounts(long userId, boolean getAdvertiser,boolean getAgency, String searchText) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		int count = 0;
		Map<String,String> dataMap = getSelectedAccountsByUserId(userId, getAdvertiser, getAgency);
		if(dataMap != null && dataMap.size() > 0) {
			for (String key : dataMap.keySet()) {
				String value = dataMap.get(key);
				if(value.toLowerCase().contains(searchText)) {
					JSONObject json = new JSONObject();
					json.put("id", key);
					json.put("name", value);
					jsonArray.add(json);
					count++;
					if(count==20) break;
				}
			}
		}else {
			log.info("No accounts for the user");
		}
		jsonObject.put("accounts", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}
	
	public static boolean isAuthorisedAccountId(String accountId, Map<String,String> accountMap) {
		boolean isAuthorised = false;
		if(accountMap != null && accountMap.size() > 0 && accountMap.containsKey("_"+accountId)) {
			isAuthorised = true;
		}
		//log.info("accountId : "+accountId+", isAuthorised : "+isAuthorised);
		return isAuthorised;
	}
	
	public CompanyObj getCompanyById(Long companyId, List<CompanyObj> companyObjList){
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		CompanyObj companyObj=null;
		try {
			companyObj = userDetailsDAO.getCompanyById(companyId, MemcacheUtil.getAllCompanyList());			
		} catch (Exception e) {
			log.severe("Exception in loading company : "+e.getMessage());
		}
		
		return companyObj;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lin.web.service.IUserService#getSelectedCompaniesByUserId(long)
	 */
	public List<CompanyObj> getSelectedCompaniesByUserId(long userId){
		List<CompanyObj> companyList=null;
		IUserDetailsDAO userDAO=new UserDetailsDAO();
		try {
			log.info("Load all companies for a user: "+userId);
			companyList=userDAO.getSelectedCompaniesByUserId(userId);
		} catch (Exception e) {
			log.severe("Failed to load companies for user:"+userId+", exception :"+e.getMessage());
			
		}
		return companyList;
	}
	
	@Override
	public CompanyObj getCompanyObjById(long companyId){
		CompanyObj companyObj = new CompanyObj();
		IUserDetailsDAO userDAO=new UserDetailsDAO();
		try{
			log.info("Load company by ID : "+companyId);
			companyObj=userDAO.getCompanyByCompanyId(companyId);
		}catch(Exception e){
			log.severe("Exception ::"+e.getMessage());
		}
		return companyObj;
		
	}
	
	public static String updateAllAccounts() {
		String msg = "";
		try {
			IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
			
			List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
			List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			if(companyObjList != null && companyObjList.size() > 0) {
				for(CompanyObj companyObj : companyObjList) {
					accountsObjList.addAll(MemcacheUtil.getAllAccountsObjList(companyObj.getId()+""));
				}
				if (accountsObjList != null && accountsObjList.size() > 0) {
					for (AccountsEntity obj : accountsObjList) {
						if(obj.getStatus() == null) {
							obj.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
						}
						userDetailsDAO.saveObjectWithStrongConsistancy(obj);
					}
					msg = "AccountsEntity updated, count : "+ accountsObjList.size()+", Please flush the memcache.";
				} else {
					msg = "AccountsEntity is Empty";
				}
			}else {
				msg = "CompanyObj is Empty";
			}
		} catch (Exception e) {
			log.severe("Exception:" + e.getMessage());
			msg = "Exception:" + e.getMessage();
			
		}
		log.info(msg);
		return msg;
	}
	
}
