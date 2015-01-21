package com.lin.persistance.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AnonymousUserDetailsObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.EmailAuthObj;
import com.lin.server.bean.MaxCountUserDetailsObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;


public class UserDetailsDAO implements IUserDetailsDAO{

	
	private static final Logger log = Logger.getLogger(UserDetailsDAO.class.getName());	
	private Objectify obfy = OfyService.ofy();	
	private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);
	
	public void saveObject(Object obj) throws DataServiceException{	
		//Objectify obfy = OfyService.ofy();
		obfy.save().entity(obj);
	}
	public void deleteObject(Object obj) throws DataServiceException{	
		//Objectify obfy = OfyService.ofy();
		obfy.delete().entity(obj);
        log.info("Object deleted successfully from datastore.");
	}
	
	@Override
	public void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException{
    	strongObfy.save().entity(obj).now();
    }
	
	@Override
	public UserDetailsObj getUserById(long id) throws Exception{
		log.info("Inside getUserById of UserDetailsDAO");
		UserDetailsObj userDetailsObj = null;
		
		if(id > 0){
			List<UserDetailsObj> usersList = MemcacheUtil.getAllUsersList();
			if(usersList != null && usersList.size() > 0) {
				for (UserDetailsObj userDetails : usersList) {
					if(userDetails.getId() == id) {
						userDetailsObj = userDetails;
						break;
					}
				}
			}
		}
		if(userDetailsObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return userDetailsObj;
	}
	
	@Override
	public UserDetailsObj getUserByEmailIdFromDataStore(String emailId) throws Exception{
		log.info("Inside getUserByEmailIdFromDataStore of UserDetailsDAO");
		UserDetailsObj userDetailsObj = null;
		
		if(emailId != null && !emailId.trim().equals("")){
			List<UserDetailsObj> usersList = obfy.load().type(UserDetailsObj.class)
												.filter("emailId = ", emailId.trim().toLowerCase())
												
												.list();
			if(usersList != null && usersList.size() > 0) {
					userDetailsObj = usersList.get(0);
			}
		}
		if(userDetailsObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return userDetailsObj;
	}
	
	@Override
	public UserDetailsObj getUserByEmailId(String emailId) throws Exception{
		log.info("UserDetailsDAO : getUserByEmailId..");
		UserDetailsObj userDetailsObj = null;
		List<UserDetailsObj> resultList = new ArrayList<UserDetailsObj>();
		
		if(emailId != null && !emailId.trim().equalsIgnoreCase("")){
			List<UserDetailsObj> usersList = MemcacheUtil.getAllUsersList();
			if(usersList != null && usersList.size() > 0) {
				for (UserDetailsObj userDetails : usersList) {
					if(userDetails.getEmailId().equalsIgnoreCase(emailId)) {
						resultList.add(userDetails);
					}
				}
			}
		}		
		if(resultList !=null && resultList.size() == 1){
			userDetailsObj = resultList.get(0);
			log.info("resultList:"+resultList.size());
		}
		else if(resultList !=null && resultList.size() > 1){
			log.severe("Duplicate emailId in UserDatailsObj name in Datastore, duplicate count : "+resultList.size());
		}
		else{
			log.info("resultList :Null");
		}
		return userDetailsObj;
	}
	
	@Override
	public AnonymousUserDetailsObj getAnonymousUserByEmailId(String email) throws Exception{
		log.info("UserDetailsDAO : getAnonymousUserByEmailId..");
		AnonymousUserDetailsObj anonymousUserDetailsObj = null;
		List<AnonymousUserDetailsObj> resultList = null;
		if(email != null && !email.trim().equalsIgnoreCase("")){
			resultList = obfy.load().type(AnonymousUserDetailsObj.class)
			                         .filter("email = " , email.toLowerCase())
			                         .list();
		}
		if(resultList !=null && resultList.size() == 1){
			anonymousUserDetailsObj = resultList.get(0);
			log.info("resultList:"+resultList.size());
		}else{
			log.info("resultList :Null");
		}
		return anonymousUserDetailsObj;
	}

	@Override
	public UserDetailsObj getUserDetails(String emailId, String encriptedPassword) throws Exception {
		log.info("UserDetailsDAO : getUserDetails..");
		UserDetailsObj userDetailsObj = null;
		
		List<UserDetailsObj> resultList = new ArrayList<UserDetailsObj>();
		
		if(emailId != null && !emailId.trim().equals("") && encriptedPassword != null && !encriptedPassword.trim().equals("")){
			List<UserDetailsObj> usersList = MemcacheUtil.getAllUsersList();
			if(usersList != null && usersList.size() > 0) {
				for (UserDetailsObj userDetails : usersList) {
					if(userDetails != null && userDetails.getEmailId() != null && userDetails.getEmailId().trim().equalsIgnoreCase(emailId) 
							&& userDetails.getPassword() != null && userDetails.getPassword().equals(encriptedPassword)) {
						resultList.add(userDetails);
					}
				}
			}
		}		
		if(resultList !=null && resultList.size() == 1){
			userDetailsObj = resultList.get(0);
			log.info("resultList : "+resultList.size());
		}
		else if(resultList !=null && resultList.size() > 1){
			log.severe("Duplicate EmailId in UserDetailsObj in Datastore, duplicate count : "+resultList.size());
		}
		else{
			log.info("resultList :Null");
		}
		return userDetailsObj;
	}
	
	@Override
	public long getMaxCount()  throws Exception {
		log.info("Inside getMaxCount of UserDetailsDAO");
		long maxCount = -1;
		try {
			Thread.sleep(2000);
			MaxCountUserDetailsObj maxCountUserDetailsObj = new MaxCountUserDetailsObj();
			List<MaxCountUserDetailsObj> resultList=obfy.load().type(MaxCountUserDetailsObj.class).list();		
			if(resultList != null && resultList.size() == 1){
				maxCountUserDetailsObj = resultList.iterator().next();
					maxCount = maxCountUserDetailsObj.getMaxCount();
					maxCountUserDetailsObj.setMaxCount(maxCount + 1);
					saveObject(maxCountUserDetailsObj);	
				log.info("resultList:"+resultList.size());
			}else{
				maxCount = 100;
				maxCountUserDetailsObj.setMaxCount(maxCount + 1);
				saveObject(maxCountUserDetailsObj);
				log.info("resultList :Null");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Exception in getMaxCount of UserDetailsDAO");
			maxCount = -1;
			throw e;
		}
		return maxCount +1;
	}

	@Override
	public long getEmailAuthObj(String randomNumber, String status) throws Exception {
		log.info("Inside authorizeEmail of UserDetailsDAO : randomNumber="+randomNumber+", status="+status);
		long result = -1;
		try {
		//	obfy = ObjectifyService.beginTransaction();				// begin transaction
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			List<EmailAuthObj> resultList = obfy.load().type(EmailAuthObj.class)
											.filter("randomNumber = ", randomNumber)
											.filter("status = ", status)
											.list();
			if(resultList != null && resultList.size() == 1) {
				emailAuthObj = resultList.iterator().next();
				if(emailAuthObj != null && emailAuthObj.getUserId() > 0) {
					UserDetailsObj userDetailsObj = getUserById(emailAuthObj.getUserId());
					if(userDetailsObj != null && userDetailsObj.getId() > 0) {
						result = userDetailsObj.getId();
					}
				}
			}
	//		obfy.getTxn().commit();							// Commit transaction
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Exception in authorizeEmail of UserDetailsDAO");
			throw e;
		}
		/*finally
		{
		    if (obfy.getTxn().isActive())
		    	obfy.getTxn().rollback();					// Rollback transaction
		}*/
		return result;
	}

	@Override
	public Boolean authorizeUser(UserDetailsObj userDetailsObj, UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("Inside authorizeUser of UserDetailsDAO");
		Boolean result = false;
		try {
	//		obfy = ObjectifyService.beginTransaction();				// begin transaction
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			List<EmailAuthObj> resultList = obfy.load().type(EmailAuthObj.class)
											.filter("userId = ", userDetailsObj.getId())
											.filter("randomNumber = ", userDetailsDTO.getRandomNumber())
											.filter("status = ", "unAuthorised")
											.list();
			if(resultList != null && resultList.size() == 1) {
				emailAuthObj = resultList.get(0);
				if(emailAuthObj != null && emailAuthObj.getUserId() > 0) {
					//emailAuthObj.setStatus("authorised");
					
					if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, false, false)) {
						//saveObject(emailAuthObj);
						deleteObject(emailAuthObj);
						userDetailsObj.setEmailId(userDetailsObj.getEmailId().toLowerCase());
						saveObject(userDetailsObj);
						result = true;
					}
				}
			}
	//		obfy.getTxn().commit();							// Commit transaction
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Exception in authorizeUser of UserDetailsDAO");
			throw e;
		}
		/*finally
		{
		    if (obfy.getTxn().isActive())
		    	obfy.getTxn().rollback();					// Rollback transaction
		}*/
		return result;
	}

	@Override
	public void getAllUsers(UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("UserDetailsDAO : userSetup..");
		List<UserDetailsObj> resultList = MemcacheUtil.getAllUsersList();;		
		if(resultList !=null && resultList.size() > 0){
			userDetailsDTO.setUsersList(resultList);
			log.info("resultList:"+resultList.size());
		}else{
			userDetailsDTO.setUsersList(new ArrayList<UserDetailsObj>());
			log.info("resultList :Null");
		}	
	}

	/*@Override
	public long forgetPasswordEmail(String randomNumber) throws Exception {
		log.info("Inside forgetPasswordEmail of UserDetailsDAO");
		long result = -1;
		try {
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			List<EmailAuthObj> resultList = obfy.load().type(EmailAuthObj.class)
											.filter("randomNumber = ", randomNumber)
											.filter("status = ", "forgetPassword")
											.list();
			if(resultList != null && resultList.size() == 1) {
				emailAuthObj = resultList.iterator().next();
				if(emailAuthObj != null && emailAuthObj.getUserId() > 0) {
					UserDetailsObj userDetailsObj = getUserById(emailAuthObj.getUserId());
					if(userDetailsObj != null && userDetailsObj.getId() > 0) {
						result = userDetailsObj.getId();
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Exception in forgetPasswordEmail of UserDetailsDAO");
			throw e;
		}
		return result;
	}*/

	@Override
	public Boolean resetPassword(UserDetailsObj userDetailsObj, UserDetailsDTO userDetailsDTO) throws Exception {
		log.info("Inside resetPassword of UserDetailsDAO");
		Boolean result = false;
		try {
			EmailAuthObj emailAuthObj = new EmailAuthObj();
			List<EmailAuthObj> resultList = obfy.load().type(EmailAuthObj.class)
											.filter("userId = ", userDetailsObj.getId())
											.filter("randomNumber = ", userDetailsDTO.getRandomNumber())
											.filter("status = ", "forgetPassword")
											.list();
			if(resultList != null && resultList.size() == 1) {
				emailAuthObj = resultList.iterator().next();
				if(emailAuthObj != null && emailAuthObj.getUserId() > 0) {
					emailAuthObj.setStatus("passwordReset");
					userDetailsObj.setEmailId(userDetailsObj.getEmailId().toLowerCase());
					if(MemcacheUtil.selfUpdateMemcacheUserDetails(userDetailsObj, false, false)) {
						saveObject(emailAuthObj);
						saveObject(userDetailsObj);
						result = true;
					}
				}
			}
			log.info("resultList : "+resultList.size());
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Exception in resetPassword of UserDetailsDAO");
			throw e;
		}
		log.info("UserDetailsDAO : resetPassword, result : "+result); 
		return result;
	}
	
	@Override
	public List<UserDetailsObj> getAllUsers()  throws Exception {
		log.info("UserDetailsDAO : userSetup..");
		List<UserDetailsObj> resultList = obfy.load().type(UserDetailsObj.class)
											
											.order("userName")
											.list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("UserDetailsObj resultList:"+resultList.size());
		}else{
			log.info("UserDetailsObj resultList :Null");
		}	
		return resultList;
	}
	
	@Override
	public List<AuthorisationTextObj> getAllAuthorisationText() throws Exception {
		log.info("UserDetailsDAO : getAllAuthorisationText..");
		List<AuthorisationTextObj> resultList = obfy.load().type(AuthorisationTextObj.class)
												.order("authorisationForPage")
												.list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("AuthorisationText resultList:"+resultList.size());
		}else{
			log.info("AuthorisationText resultList :Null");
		}	
		return resultList;	
	}

	@Override
	public List<RolesAndAuthorisation> getAllRolesAndAuthorisation() throws Exception {
		log.info("UserDetailsDAO : getAllRolesAndAuthorisation..");
		List<RolesAndAuthorisation> resultList = obfy.load().type(RolesAndAuthorisation.class)												
												.order("roleName")
												.list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("RolesAndAuthorisation resultList:"+resultList.size());
		}else{
			log.info("RolesAndAuthorisation resultList :Null");
		}	
		return resultList;
	}

	@Override
	public List<TeamPropertiesObj> getAllTeamPropertiesObj() throws Exception {
		log.info("UserDetailsDAO : getAllTeamPropertiesObj..");
		List<TeamPropertiesObj> resultList = obfy.load().type(TeamPropertiesObj.class)
												.order("teamName")
												.list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("TeamPropertiesObj resultList:"+resultList.size());
		}else{
			log.info("TeamPropertiesObj resultList :Null");
		}	
		return resultList;
	}
	
	
	public List<CompanyObj> getAllCompany() throws Exception {
		log.info("UserDetailsDAO : getAllCompany..");
		List<CompanyObj> resultList = obfy.load().type(CompanyObj.class)
										.order("companyName")
										.list();
		if(resultList !=null && resultList.size() > 0){
			log.info("getAllCompany resultList:"+resultList.size());
		}else{
			log.info("getAllCompany resultList :Null");
		}	
		return resultList;
	}
	
	public List<CompanyObj> getAllCompany(String companyType) throws DataServiceException{
		log.info("UserDetailsDAO : getAllCompany..companyType : "+companyType);
		List<CompanyObj> resultList = obfy.load().type(CompanyObj.class)
										.filter("companyType = ", companyType)
										.list();
		
		return resultList;
	}
	
	@Override
	public PropertyObj getPropertyObjById(String propertyId, List<PropertyObj> resultList) throws Exception {
		PropertyObj propertyObj = null;	
		if(resultList !=null && resultList.size() > 0){
			for (PropertyObj obj : resultList) {
				if(obj != null && String.valueOf(obj.getId()).equals(propertyId)) {
					propertyObj = obj;
					break;
				}
			}
		}	
		return propertyObj;
	}
	
	@Override
	public PropertyObj getPropertyObjByDFPPropertyId(String dfpPropertyId, List<PropertyObj> propertyObjList) throws Exception {
		PropertyObj propertyObj = null;	
		if(propertyObjList !=null && propertyObjList.size() > 0){
			for (PropertyObj obj : propertyObjList) {
				if(obj != null && obj.getId() != null && obj.getDFPPropertyId().equalsIgnoreCase(dfpPropertyId)) {
					propertyObj = obj;
					break;
				}
			}
		}	
		return propertyObj;
	}
	
	@Override
	public PropertyObj getPropertyObjById(String propertyId) throws Exception {
		PropertyObj propertyObj = null;	
		List<PropertyObj> propertyObjList = null;
		if(propertyId != null && !(propertyId.trim().equals(""))){
			propertyObjList = obfy.load().type(PropertyObj.class)
												.filter("id = ", propertyId.trim())
												.list();
			if(propertyObjList != null && propertyObjList.size() > 0) {
				propertyObj = propertyObjList.get(0);
			}
		}
		if(propertyObjList != null && propertyObjList.size() > 0) {
			log.info("propertyObjList size : "+propertyObjList.size());
		}
		else {
			log.info("propertyObjList size : 0");
		}
		return propertyObj;
	}
	
	@Override
	public List<PropertyObj> getAllPropertyObjByCompanyId(String companyId) throws Exception {
		log.info("getAllPropertyObjByCompanyId -- companyId:"+companyId);
		List<PropertyObj> resultList = obfy.load().type(PropertyObj.class)
											.filter("companyId = ", companyId.trim())
											.order("propertyName")
											.list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("getAllPropertyObjByCompanyId resultList: "+resultList.size());
		}else{
			log.info("getAllPropertyObjByCompanyId resultList : Null");
		}	
		return resultList;
	}
	
		
	@Override
	public CompanyObj getCompanyObjByIdAndCompanyType(String companyId, String companyType, List<CompanyObj> companyObjList) throws Exception {
		CompanyObj companyObj = null;	
		if(companyObjList !=null && companyObjList.size() > 0 && companyId != null && !companyId.trim().equals("") && companyType != null && !companyType.equals("")){
			for (CompanyObj obj : companyObjList) {
				if(obj != null && String.valueOf(obj.getId()).equals(companyId.trim()) && obj.getCompanyType().equals(companyType)) {
					companyObj = obj;
					break;
				}
			}
		}
		if(companyObj != null) {
			log.info("CompanyObj found");
		}
		else {
			log.info("Found : null");
		}
		return companyObj;
	}
	
	@Override
	public CompanyObj getCompanyObjByNameAndCompanyType(String companyName, String companyType, List<CompanyObj> companyObjList) throws Exception {
		CompanyObj companyObj = null;	
		if(companyObjList !=null && companyObjList.size() > 0 && companyName != null && !companyName.trim().equals("") && companyType != null && !companyType.equals("")){
			for (CompanyObj obj : companyObjList) {
				if(obj != null && obj.getCompanyName().equals(companyName.trim()) && obj.getCompanyType().equals(companyType)) {
					companyObj = obj;
					break;
				}
			}
		}
		if(companyObj != null) {
			log.info("CompanyObj found");
		}
		else {
			log.info("Found : null");
		}
		return companyObj;
	}

	@Override
	public TeamPropertiesObj getTeamPropertiesObjById(String teamId, List<TeamPropertiesObj> resultList) throws Exception {
		TeamPropertiesObj teamPropertiesObj = null;		
		if(resultList !=null && resultList.size() > 0){
			for (TeamPropertiesObj obj : resultList) {
				if(obj != null && obj.getId() != null && obj.getId().toString().equals(teamId)) {
					teamPropertiesObj = obj;
					break;
				}
			}
		}	
		return teamPropertiesObj;
	}
    
	@Override
	public CompanyObj getCompanyById(Long companyId, List<CompanyObj> resultList) throws Exception{
		 CompanyObj companyObj = null;
		 if(resultList !=null && resultList.size() > 0){
			 for (CompanyObj obj : resultList) {
				if(obj != null && Long.valueOf(obj.getId()).equals(companyId)) {
					companyObj = obj;
					break;
				}
			}
		 }
		 if(companyObj ==null){
			 log.info("companyObj : Null, for companyId : "+companyId);
		 }
		 return companyObj;
	 }
	
	@Override
	public CompanyObj getCompanyByName(String companyName, List<CompanyObj> resultList) throws Exception{
		 CompanyObj companyObj = null;
		 if(resultList !=null && resultList.size() > 0){
			 for (CompanyObj obj : resultList) {
				if(obj != null && obj.getCompanyName() != null && obj.getCompanyName().trim().equalsIgnoreCase(companyName.trim())) {
					companyObj = obj;
					break;
				}
			}
		 }
		 if(companyObj ==null) {
			 log.info("companyObj : Null, for companyName : "+companyName);
		 }
		 return companyObj;
	 }
	
	@Override
	public List<CompanyObj> getSelectedCompaniesByUserId(long userId) throws Exception {
		 log.info("UserDetailsDAO : getSelectedCompaniesByUserId..");
		 List<CompanyObj> companiesList = new ArrayList<CompanyObj>();
		 UserDetailsObj userDetailsObj = getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
				 List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
				 if(companyObjList != null && companyObjList.size()  >0) {
					 for (CompanyObj companyObj : companyObjList) {
						if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							companiesList.add(companyObj);
						}
					}
				 }
			 }
			 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 String teamId = teamIdList.get(0).trim();
					 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
					 if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null && !teamPropertiesObj.getCompanyId().trim().equals("")) {
						 CompanyObj companyObj = getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), MemcacheUtil.getAllCompanyList());
						 if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							 	companiesList.add(companyObj);
						 }
					 }
				 }
			 }
		 }
		 return companiesList;
	 }
	
	@Override
	public List<String> getSelectedAppViewsByUserId(long userId) throws Exception {
		 log.info("UserDetailsDAO : getSelectedAppViewsByUserId..");
		 List<String> appViewList = new ArrayList<String>();
		 
		 UserDetailsObj userDetailsObj = getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && userDetailsObj.getRole() != null) {
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					for(int i=0; i<LinMobileConstants.APP_VIEWS.length; i++) {
						appViewList.add((LinMobileConstants.APP_VIEWS[i]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
					}
			 }
			 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					 String[] appViewArray = getAllAvailableAppViewsByUserDetailObj(userDetailsObj);
					 for (String teamId : teamIdList) {
						 if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
							 appViewList.clear();
							 break;
						 }else {
							 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getAppViews() != null) {
								List<String> tempList = teamPropertiesObj.getAppViews();
								if(tempList != null && tempList.size() > 0) {
									if(appViewArray != null && appViewArray.length > 0) {
										/*if(tempList.contains(LinMobileConstants.ALL_APP_VIEWS)) {
											appViewList.clear();
											for (int i = 0; i < appViewArray.length; i++) {
												appViewList.add(appViewArray[i]);
											}
											break;
										}*/
										for(int i=0; i<appViewArray.length; i++) {
											if(tempList.contains(appViewArray[i].split(LinMobileConstants.ARRAY_SPLITTER)[0].trim()) && !appViewList.contains(appViewArray[i].split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
												appViewList.add(appViewArray[i].split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
											}
										}
									}
								}
							 }
						 }
					 }
				 }
			 }
			 if(!roleName.trim().equals("") && roleName.trim().equals(LinMobileConstants.ADMINS[1])) {	// if Admin
				 appViewList.add((LinMobileConstants.APP_VIEWS[4]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
			 }
		 }
		 return appViewList; 
	 }
	
	@Override
	public List<CompanyObj> getSelectedPublishersByUserId(long userId, List<String> publisherIdList, List<CompanyObj> publisherObjList) throws Exception{
		 log.info("UserDetailsDAO : getSelectedPublishersByUserId..");
		 List<CompanyObj> publishersList = new ArrayList<CompanyObj>();
		 
		 UserDetailsObj userDetailsObj = getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) {
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					for (CompanyObj companyObj : publisherObjList) {
						if(companyObj != null && companyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
							publisherIdList.add(String.valueOf(companyObj.getId()));
							publishersList.add(companyObj);
						}
					}
			 }
			 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {		 
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 String team = teamIdList.get(0);
					 if(team.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
						 publisherIdList.clear();
						 publishersList.clear();
					 }
					 else {
						 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
						 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(team.trim(), teamPropertiesObjList);
						 if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							 CompanyObj companyObj = getCompanyObjByIdAndCompanyType(teamPropertiesObj.getCompanyId().trim(), LinMobileConstants.COMPANY_TYPE[0], MemcacheUtil.getAllCompanyList());
							 if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
								 publisherIdList.add(String.valueOf(companyObj.getId()));
								 publishersList.add(companyObj);
							 }
						 }
					 }
				 }
			 }
		 }
		 return publishersList;
	 }
	
	@Override
	public List<CompanyObj> getAllPublishers(List<CompanyObj> companyObjList) throws Exception {
		List<CompanyObj> publisherList = new ArrayList<CompanyObj>();
		if(companyObjList != null && companyObjList.size() > 0) {
			for (CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					publisherList.add(companyObj);
				}
			}
		}
		if(publisherList.size() > 0) {
			log.info("publisherList size : "+publisherList.size());
			return publisherList;
		}
		else {
			log.info("publisherList size : 0");
			return null;
		}
	}
	
	@Override
	public List<CompanyObj> getAllDemandPartners(List<CompanyObj> companyObjList) throws Exception {
		List<CompanyObj> demandPartnerList = new ArrayList<CompanyObj>();
		if(companyObjList != null && companyObjList.size() > 0) {
			for (CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1])) {
					demandPartnerList.add(companyObj);
				}
			}
		}
		if(demandPartnerList.size() > 0) {
			log.info("demandPartnerList size : "+demandPartnerList.size());
			return demandPartnerList;
		}
		else {
			log.info("demandPartnerList size : 0");
			return null;
		}
	}
	
	@Override
	public CompanyObj getCompanyByBqIdentifier(String bqIdentifier, List<CompanyObj> companyObjList) throws Exception {
		CompanyObj companyObj = null;
		if(companyObjList != null && companyObjList.size() > 0 && bqIdentifier != null && bqIdentifier.trim().length() > 0) {
			for (CompanyObj obj : companyObjList) {
				if(obj != null && (obj.getBqIdentifier()+"").equals(bqIdentifier.trim())) {
					companyObj = obj;
					break;
				}
			}
		}
		if(companyObj != null) {
			log.info("companyObj found");
		}
		else {
			log.info("companyObj not found");
		}
		return companyObj;
	}
	
	@Override
	public List<CompanyObj> getAllClients(List<CompanyObj> companyObjList) throws Exception {
		List<CompanyObj> clientList = new ArrayList<CompanyObj>();
		if(companyObjList != null && companyObjList.size() > 0) {
			for (CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[2])) {
					clientList.add(companyObj);
				}
			}
		}
		if(clientList.size() > 0) {
			log.info("clientList size : "+clientList.size());
			return clientList;
		}
		else {
			log.info("clientList size : 0");
			return null;
		}
	}
	
	@Override
	public List<PropertyObj> getSelectedPropertiesByUserIdAndPublisherId(long userId, String publisherId) throws Exception {
		 List<PropertyObj> propertiesList = new ArrayList<PropertyObj>();
		 log.info("check getUserById");
		 UserDetailsObj userDetailsObj = getUserById(userId);
		 log.info("check user");
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) {
			 log.info("userDetailsObj != null");
			 List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
			 CompanyObj publisherCompany = getCompanyObjByIdAndCompanyType(publisherId, LinMobileConstants.COMPANY_TYPE[0], companyObjList);
		 	 if(publisherCompany != null && publisherCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.isAccessToProperties()) {
		 		 log.info("Company has access To Properties");
		 		List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
		 		propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(publisherCompany.getId()+""));
		 		 /*List<CompanyObj> publisherCompanyList = new ArrayList<CompanyObj>();
		 		 publisherCompanyList.add(publisherCompany);
		 		 Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(publisherCompanyList);
		 		 if(map != null && !map.isEmpty()) {
		 			List<PropertyEntityObj> propertyObjList = new ArrayList<PropertyEntityObj>();
					for(String key : map.keySet()) {
						AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
						if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null) {
							propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerUsername()));
						}
					}*/
					 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
					 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
					 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
						 log.info("is super Admin");
						for (PropertyObj propertyObj : propertyObjList) {
							if(propertyObj != null && propertyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
								propertiesList.add(propertyObj);
							}
						}
					 }
					 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {	
						 log.info("Not a super Admin");
						 List<String> teamIdList = userDetailsObj.getTeams();
						 if(teamIdList != null && teamIdList.size() > 0) {
							 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
							 List<String> propertiesIdList = new ArrayList<String>();
							 for (String teamId : teamIdList) {
								 if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
									 propertiesList.clear();
									 break;
								 }
								 else {
									TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
									if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(publisherId)) {
										List<String> tempList = teamPropertiesObj.getPropertyId();
										if(tempList != null && tempList.size() > 0 && tempList.contains(LinMobileConstants.ALL_PROPERTIES)) {
											if(propertyObjList != null && propertyObjList.size() > 0) {
												for (PropertyObj propertyObj : propertyObjList) {
													if(propertyObj != null && !propertiesIdList.contains(String.valueOf(propertyObj.getId())) && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {	
														propertiesList.add(propertyObj);
													}
													propertiesIdList.add(String.valueOf(propertyObj.getId()));
												}
											}
											break;
										}
										else if(tempList != null && tempList.size() > 0) {
											for (String propertyId : tempList) {
												if(!propertiesIdList.contains(propertyId.trim())) {
													propertiesIdList.add(propertyId.trim());
													for (PropertyObj propertyObj : propertyObjList) {
														if(propertyObj != null && (propertyObj.getId()+"").equals(propertyId.trim()) && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
															propertiesList.add(propertyObj);
															break;
														}
													}
												}
											}
										}
									}
								}
							} // For Ends
						}
					}
		 	 	//}
		 	}
		 }
		 log.info("propertiesList size : "+propertiesList.size());
		 return propertiesList;
	 }
	
	@Override
	public List<CompanyObj> getActiveDemandPartnersByPublisherCompanyId(String publisherId) throws Exception {
		List<CompanyObj> selectedDemandPartnersList = new ArrayList<CompanyObj>();
		List <CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
		CompanyObj publisherCompany = getCompanyById(Long.valueOf(publisherId), companyObjList);
		if(publisherCompany != null && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && publisherCompany.getDemandPartnerId() != null && publisherCompany.getDemandPartnerId().size() > 0) {
			List<CompanyObj> demandPartnersList = getAllDemandPartners(companyObjList);
			if(demandPartnersList != null && demandPartnersList.size() > 0) {
				List<String> demandPartnerIdList = publisherCompany.getDemandPartnerId();
				for (String demandPartnerId : demandPartnerIdList) {
					CompanyObj demandPartnerCompany = getCompanyById(Long.valueOf(demandPartnerId), demandPartnersList);
					if(demandPartnerCompany != null && demandPartnerCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && demandPartnerCompany.getCompanyName() != null) {
						selectedDemandPartnersList.add(demandPartnerCompany);
					}
				}
			}
		}
		log.info("selectedDemandPartnersList size : "+selectedDemandPartnersList.size());
		return selectedDemandPartnersList;
	}
	
	@Override
	public List<CommonDTO> getActivePropertiesByPublisherCompanyId(String publisherId) throws Exception {
		List<CommonDTO> selectedPropertiesList = new ArrayList<CommonDTO>();
		CompanyObj publisherCompany = getCompanyById(Long.valueOf(publisherId), MemcacheUtil.getAllCompanyList());
		if(publisherCompany != null && publisherCompany.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && publisherCompany.isAccessToProperties()) {
			 /*List<CompanyObj> publisherCompanyList = new ArrayList<CompanyObj>();
	 		 publisherCompanyList.add(publisherCompany);
	 		 Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(publisherCompanyList);
	 		 if(map != null && !map.isEmpty()) {
	 			List<PropertyEntityObj> propertyObjList = new ArrayList<PropertyEntityObj>();
				for(String key : map.keySet()) {
					AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
					if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null) {
						propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerUsername()));
					}
				}
				if(propertyObjList != null && propertyObjList.size() > 0) {
					for (PropertyEntityObj propertyObj : propertyObjList) {
						if(propertyObj != null && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							selectedPropertiesList.add(new CommonDTO(propertyObj.getId()+"", propertyObj.getPropertyName()));
						}
					}
				}
	 		 }*/
			List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
			propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(publisherCompany.getId()+""));
			if(propertyObjList != null && propertyObjList.size() > 0) {
				for (PropertyObj propertyObj : propertyObjList) {
					if(propertyObj != null && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
						selectedPropertiesList.add(new CommonDTO(propertyObj.getId()+"", propertyObj.getPropertyName()));
					}
				}
			}
			//
		}
		log.info("selectedPropertiesList size : "+selectedPropertiesList.size());
		return selectedPropertiesList;
	}
	
	@Override
	public List<CompanyObj> getSelectedDemandPartnersByUserId(long userId) throws Exception{
		 List<CompanyObj> selectedDemandPartnersList = new ArrayList<CompanyObj>();
		 List<CompanyObj> companyObjList =  MemcacheUtil.getAllCompanyList();
		 List<CompanyObj> demandPartnersList = getAllDemandPartners(companyObjList);
		 if(companyObjList != null && companyObjList.size() > 0) {
			 UserDetailsObj userDetailsObj = getUserById(userId);
			 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
				 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
				 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					 for(CompanyObj companyObj : companyObjList) {
						 if(companyObj != null && companyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1])) {							 
							 selectedDemandPartnersList.add(companyObj);
						 }
					 }
				 }
				 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {		 
					 List<String> teamIdList = userDetailsObj.getTeams();
					 if(teamIdList != null && teamIdList.size() > 0) {
						 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
						 String teamId = teamIdList.get(0);
						 if(!teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
							 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null) {
								 CompanyObj companyObj = getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), companyObjList);
								 if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getDemandPartnerId() != null && companyObj.getDemandPartnerId().size() > 0) {
									 List<String> demandPartnerIdList = companyObj.getDemandPartnerId();
									 for(String demandPartnerId : demandPartnerIdList) {
										 CompanyObj demandPartner = getCompanyById(Long.valueOf(demandPartnerId.trim()), demandPartnersList);
										 if(demandPartner != null && demandPartner.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
											 selectedDemandPartnersList.add(demandPartner);
										 }
									 }
								 }
								 if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1]) && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
									 selectedDemandPartnersList.add(companyObj);
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedDemandPartnersList;
	 }
	
	@Override
	public List<CommonDTO> getSelectedDemandPartnersDropDownByUserId(long userId) throws Exception {
		 List<CommonDTO> selectedDemandPartnersList = new ArrayList<CommonDTO>();
		 List<CompanyObj> companyObjList =  MemcacheUtil.getAllCompanyList();
		 List<CompanyObj> demandPartnersList = getAllDemandPartners(companyObjList);
		 if(companyObjList != null && companyObjList.size() > 0) {
			 UserDetailsObj userDetailsObj = getUserById(userId);
			 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
				 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
				 if(roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					 for(CompanyObj companyObj : companyObjList) {
						 if(companyObj != null && companyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1])) {							 
							 selectedDemandPartnersList.add(new CommonDTO(String.valueOf(companyObj.getId()), companyObj.getCompanyName()));
						 }
					 }
				 }
				 else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {		 
					 List<String> teamIdList = userDetailsObj.getTeams();
					 if(teamIdList != null && teamIdList.size() > 0) {
						 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
						 String teamId = teamIdList.get(0);
						 if(!teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
							 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null) {
								 CompanyObj companyObj = getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), companyObjList);
								 if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]) && companyObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getDemandPartnerId() != null && companyObj.getDemandPartnerId().size() > 0) {
									 List<String> demandPartnerIdList = companyObj.getDemandPartnerId();
									 for(String demandPartnerId : demandPartnerIdList) {
										 CompanyObj demandPartner = getCompanyById(Long.valueOf(demandPartnerId.trim()), demandPartnersList);
										 if(demandPartner != null && demandPartner.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
											 selectedDemandPartnersList.add(new CommonDTO(String.valueOf(demandPartner.getId()), demandPartner.getCompanyName()));
										 }
									 }
								 }
								 if(companyObj != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1]) && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
									 selectedDemandPartnersList.add(new CommonDTO(String.valueOf(companyObj.getId()), companyObj.getCompanyName()));
								 }
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedDemandPartnersList;
	 }
	
	@Override
	public List<PropertyDropDownDTO> loadPropertyDropDownList(String publisherId,long userId,String term) {
		List<PropertyDropDownDTO> siteList = new ArrayList<PropertyDropDownDTO>();
		List<String> propertyIdList = new ArrayList<String>();
		try {
			UserDetailsObj userDetailsObj = getUserById(userId);
			if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherId != null && !publisherId.trim().equals("")) {
				CompanyObj publisherCompany = getCompanyObjByIdAndCompanyType(publisherId, LinMobileConstants.COMPANY_TYPE[0], MemcacheUtil.getAllCompanyList());
				if(publisherCompany != null && publisherCompany.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && publisherCompany.isAccessToProperties()) {
					List<PropertyObj> propertyObjList = new ArrayList<PropertyObj>();
					propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(publisherCompany.getId()+""));
					/*List<CompanyObj> publisherCompanyList = new ArrayList<CompanyObj>();
			 		 publisherCompanyList.add(publisherCompany);
			 		 Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(publisherCompanyList);
			 		 if(map != null && !map.isEmpty()) {
			 			List<PropertyEntityObj> propertyObjList = new ArrayList<PropertyEntityObj>();
						for(String key : map.keySet()) {
							AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
							if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null) {
								propertyObjList.addAll(MemcacheUtil.getAllPropertiesList(adServerCredentialsDTO.getAdServerId(), adServerCredentialsDTO.getAdServerUsername()));
							}
						}*/
						List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
						 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
						if(roleName!= null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
							if(propertyObjList != null && propertyObjList.size() > 0) {
								for (PropertyObj propertyObj : propertyObjList) {
									if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
										PropertyDropDownDTO propertyDropDownDTO = new PropertyDropDownDTO();
										propertyDropDownDTO.setPropertyId(String.valueOf(propertyObj.getId()));
										propertyDropDownDTO.setPropertyName(propertyObj.getPropertyName().trim());
										propertyDropDownDTO.setDFP_propertyName(propertyObj.getDFPPropertyName().trim());
										siteList.add(propertyDropDownDTO);
									}
								}
							}
						}
						else if(!roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {		 
							 List<String> teamIdList = userDetailsObj.getTeams();
							 if(teamIdList != null && teamIdList.size() > 0) {
								 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
								 for (String teamId : teamIdList) {
									 if(teamId.startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
										 siteList.clear();
										 break;
									 }
									 /*else if(teamId.startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
										 siteList.clear();
										 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
										 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(publisherId.trim())) {
											 if(propertyObjList != null && propertyObjList.size() > 0) {
												 for (PropertyObj propertyObj : propertyObjList) {
													 if(propertyObj != null && propertyObj.getPublisherId().trim().equals(publisherId.trim()) && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
														PropertyDropDownDTO propertyDropDownDTO = new PropertyDropDownDTO();
														propertyDropDownDTO.setPropertyId(String.valueOf(propertyObj.getId()));
														propertyDropDownDTO.setPropertyName(propertyObj.getPropertyName().trim());
														propertyDropDownDTO.setDFP_propertyName(propertyObj.getDFPPropertyName().trim());
														siteList.add(propertyDropDownDTO);
													}
												 }
											 }
										 }
										 break;
									 }*/
									 else {
										TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
										if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && teamPropertiesObj.getCompanyId().equals(publisherId)) {
											List<String> tempList = teamPropertiesObj.getPropertyId();
											if(tempList != null && tempList.size() > 0 && tempList.contains(LinMobileConstants.ALL_PROPERTIES)) {
												siteList.clear();
												if(propertyObjList != null && propertyObjList.size() > 0) {
													for (PropertyObj propertyObj : propertyObjList) {
														if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
															PropertyDropDownDTO propertyDropDownDTO = new PropertyDropDownDTO();
															propertyDropDownDTO.setPropertyId(String.valueOf(propertyObj.getId()));
															propertyDropDownDTO.setPropertyName(propertyObj.getPropertyName().trim());
															propertyDropDownDTO.setDFP_propertyName(propertyObj.getDFPPropertyName().trim());
															siteList.add(propertyDropDownDTO);
														}
													}
												}
												break;
											}
											else if(tempList != null && tempList.size() > 0) {
												for (String propertyId : tempList) {
													if(!propertyIdList.contains(propertyId.trim())) {
														propertyIdList.add(propertyId.trim());
														PropertyObj propertyObj = getPropertyObjById(propertyId.trim(), propertyObjList);												
														if(propertyObj != null && propertyObj.getPropertyName() != null && !propertyObj.getPropertyName().trim().equals("") && propertyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && propertyObj.getDFPPropertyName() != null && !propertyObj.getDFPPropertyName().trim().equals("")) {
															PropertyDropDownDTO propertyDropDownDTO = new PropertyDropDownDTO();
															propertyDropDownDTO.setPropertyId(String.valueOf(propertyObj.getId()));
															propertyDropDownDTO.setPropertyName(propertyObj.getPropertyName().trim());
															propertyDropDownDTO.setDFP_propertyName(propertyObj.getDFPPropertyName().trim());
															siteList.add(propertyDropDownDTO);
														}
													}
												}
											}
										}
									}
								 }
							 }							
						}
			 		//}
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadPropertyDropDownList in UserDetailDAO"+e.getMessage());
			e.printStackTrace();
	}
 	return siteList;	
}
	@Override
	public List<String> getAllSuperAdminUserId(String superAdminRoleId) throws Exception {
		List<String> allSuperAdminUserIdList = new ArrayList<String>();
		List<UserDetailsObj> usersList = MemcacheUtil.getAllUsersList();
		if(usersList != null && usersList.size()  >0) {
			for (UserDetailsObj userDetailsObj : usersList) {
				if(userDetailsObj != null && userDetailsObj.getRole() != null && userDetailsObj.getStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && userDetailsObj.getRole().trim().equals(superAdminRoleId)) {
					allSuperAdminUserIdList.add(String.valueOf(userDetailsObj.getId()));
				}
			}
		}
		return allSuperAdminUserIdList;
	}
	
	@Override
	public String getRoleNameByRoleId(String roleId, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception {
		log.info("In getRoleNameByRoleId....");
		String roleName = "";
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
				if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && String.valueOf(rolesAndAuthorisation.getId()).equals(roleId.trim()) && rolesAndAuthorisation.getRoleName() != null) {
					roleName = rolesAndAuthorisation.getRoleName();
					break;
				}
			}
		}
		log.info("roleName : "+roleName);
		return roleName;
	}
	
	@Override
	public RolesAndAuthorisation getRoleByRoleName(String roleName, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception {
		RolesAndAuthorisation obj = null;
		if(roleName != null && !roleName.trim().equals("")) {
			if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
				for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
					if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleName() != null && rolesAndAuthorisation.getRoleName().toString().trim().equalsIgnoreCase(roleName.trim())) {
						obj = rolesAndAuthorisation;
						break;
					}
				}
			}
		}
		return obj;
	}
	
	@Override
	public RolesAndAuthorisation getRolesAndAuthorisationByRoleId(String roleId, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception {
		RolesAndAuthorisation obj = null;
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
				if(rolesAndAuthorisation != null && String.valueOf(rolesAndAuthorisation.getId()).equals(roleId.trim())) {
					obj = rolesAndAuthorisation;
					break;
				}
			}
		}
		return obj;
	}
	
	@Override
	public List<String> getPassbackValues(List<CompanyObj> demandPartnerList) throws Exception {
		List<String> allPassbackValuesList = null;
		if(demandPartnerList != null && demandPartnerList.size() > 0) {
			List<String> tempList = new ArrayList<String>();
			for (CompanyObj demandPartner : demandPartnerList) {
				if(demandPartner != null && !demandPartner.getDataSource().trim().equals(LinMobileConstants.DFP_DATA_SOURCE) && demandPartner.getPassback_Site_type() != null && demandPartner.getPassback_Site_type().size() > 0) {
					tempList.addAll(demandPartner.getPassback_Site_type());
				}
			}
			if(tempList != null && tempList.size() > 0) {
				allPassbackValuesList = tempList;
			}
		}
		return allPassbackValuesList;
	}
	
	@Override
	public List<UserDetailsObj> getUsersByCompanyIdAndRoleName(String selectedCompanyId, String roleName) throws Exception{
		log.info("getUsersByCompanyIdAndRoleName: selectedCompanyId:"+selectedCompanyId+" and roleName:"+roleName);
		List<UserDetailsObj> selectedUserDetailsObjList = new ArrayList<UserDetailsObj>();
		List<UserDetailsObj> userDetailsObjList = MemcacheUtil.getAllUsersList();
		List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		RolesAndAuthorisation rolesAndAuthorisation = getRoleByRoleName(roleName, rolesAndAuthorisationList);
		if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0]) && rolesAndAuthorisation.getRoleName() != null && rolesAndAuthorisation.getRoleName().equals(LinMobileConstants.ADMINS[0])) {
				for (UserDetailsObj userDetailsObj : userDetailsObjList) {
					if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && userDetailsObj.getRole() != null && !userDetailsObj.getRole().trim().equals("") && userDetailsObj.getRole().trim().equals(String.valueOf(rolesAndAuthorisation.getId()))) {
						selectedUserDetailsObjList.add(userDetailsObj);
					}
				}
		}
		else if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleStatus().trim().equals(LinMobileConstants.STATUS_ARRAY[0])) {
			List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
			for (UserDetailsObj userDetailsObj : userDetailsObjList) {
				if(userDetailsObj != null && userDetailsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && userDetailsObj.getRole() != null && !userDetailsObj.getRole().trim().equals("") && userDetailsObj.getRole().trim().equals(String.valueOf(rolesAndAuthorisation.getId())) && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
					String teamId = userDetailsObj.getTeams().get(0);
					String companyId = "";
					 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId, teamPropertiesObjList);
					 if(teamPropertiesObj != null && teamPropertiesObj.getCompanyId() != null) {
						 companyId = teamPropertiesObj.getCompanyId().trim();
					 }
					if(companyId.trim().equals(selectedCompanyId.trim())) {
						selectedUserDetailsObjList.add(userDetailsObj);
					}
				}
			}
		}		
		return selectedUserDetailsObjList;		
	}
	
	@Override
	public TeamPropertiesObj getTeamNameAvailable(String teamName, List<TeamPropertiesObj> resultList) throws Exception {
		TeamPropertiesObj teamPropertiesObj = null;
		 if(resultList !=null && resultList.size() > 0){
			 for (TeamPropertiesObj obj : resultList) {
				if(obj != null && obj.getTeamName() != null && obj.getTeamName().trim().equalsIgnoreCase(teamName)) {
					teamPropertiesObj = obj;
					break;
				}
			}
		 }
		 if(teamPropertiesObj !=null){
			log.info("teamPropertiesObj found");
		 }else{
			 log.info("teamPropertiesObj : Null");
		 }
		 return teamPropertiesObj;
	}
	
	@Override
	public PropertyObj propertyNameAvailable(String propertyName, List<PropertyObj> resultList) throws Exception {
		PropertyObj propertyObj = null;
		 if(resultList !=null && resultList.size() > 0){
			 for (PropertyObj obj : resultList) {
				if(obj != null && obj.getPropertyName() != null && obj.getPropertyName().trim().equalsIgnoreCase(propertyName.trim())) {
					propertyObj = obj;
					break;
				}
			}
		 }
		 if(propertyObj !=null){
			log.info("propertyObj found");
		 }else{
			 log.info("propertyObj : Null");
		 }
		 return propertyObj;
	}
	
	@Override
	public List<LineItemDTO> loadSelectedAgenciesFromBigQuery(String selectedAgencies) throws Exception {
		List<LineItemDTO> agencyList = new ArrayList<LineItemDTO>();
		StringBuilder QUERY = new StringBuilder();
		
		QUERY.append("select Account, Account_id from (select agency + ' (")
		.append(LinMobileConstants.AGENCY_ID_PREFIX)
		.append(")' as Account, '")
		.append(LinMobileConstants.AGENCY_ID_PREFIX)
		.append("' + agency_id as Account_id From [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append(".Core_Performance] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' And agency_id in ('")
		.append(selectedAgencies)
		.append("') group by Account,Account_id ignore case) order by Account ignore case");
		
		log.info("loadSelectedAgenciesFromBigQuery QUERY = "+QUERY);
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					LineItemDTO lineItemDTO = new LineItemDTO();
					lineItemDTO.setAccountId(cellList.get(1).getV().toString());
					lineItemDTO.setAccountName(cellList.get(0).getV().toString());
					agencyList.add(lineItemDTO);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("agencyList size : "+agencyList.size());
		return agencyList;
	}
	@Override
	public List<LineItemDTO> loadSelectedAdvertisersFromBigQuery(String selectedAdvertisers) throws Exception {
		List<LineItemDTO> advertiserList = new ArrayList<LineItemDTO>();
		StringBuilder QUERY = new StringBuilder();
		
		QUERY.append("select Account, Account_id from (select advertiser + ' (")
		.append(LinMobileConstants.ADVERTISER_ID_PREFIX)
		.append(")' as Account, '")
		.append(LinMobileConstants.ADVERTISER_ID_PREFIX)
		.append("' + advertiser_id as Account_id From [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append(".Core_Performance] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' And advertiser_id in ('")
		.append(selectedAdvertisers)
		.append("') group by Account,Account_id ignore case) order by Account ignore case");
		
		log.info("loadSelectedAdvertisersFromBigQuery QUERY = "+QUERY);
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					LineItemDTO lineItemDTO = new LineItemDTO();
					lineItemDTO.setAccountId(cellList.get(1).getV().toString());
					lineItemDTO.setAccountName(cellList.get(0).getV().toString());
					advertiserList.add(lineItemDTO);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("advertiserList size : "+advertiserList.size());
		return advertiserList;
	}
	
	@Override
	public List<String> getSelectedAdvertisersByUserIda(long userId) throws Exception{
		 List<String> selectedAdvertisersList = new ArrayList<String>();
		 
		 UserDetailsObj userDetailsObj = getUserById(userId);
		 if(userDetailsObj != null && userDetailsObj.getRole() != null) { 
			 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
			 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
			 if(roleName != null && (roleName.trim().equals(LinMobileConstants.ADMINS[0]) || roleName.trim().equals(LinMobileConstants.ADMINS[1]))) {
				 selectedAdvertisersList.add(LinMobileConstants.NO_RESTRICTIONS);
			 }
			 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
				 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
				 List<String> teamIdList = userDetailsObj.getTeams();
				 if(teamIdList != null && teamIdList.size() > 0) {
					 for (String teamId : teamIdList) {
						 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
						 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) {
								 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
									 if(advertiserId != null && (advertiserId.split("__")).length == 3) {
										 advertiserId = (advertiserId.split("__"))[2];
										 if(!selectedAdvertisersList.contains(advertiserId.trim()) && !selectedAdvertisersList.contains(LinMobileConstants.NO_RESTRICTIONS)) {
											 selectedAdvertisersList.add(advertiserId.trim());
										 }
									 }
								}
							 }
						 }
					 }
				 }
			 }
		 }
		 return selectedAdvertisersList;
	 }
	
	@Override
	public TeamPropertiesObj getAllEntitiesTeamByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception {
		log.info("UserDetailsDAO : getAllEntitiesTeamByTeamCompanyId..");
		TeamPropertiesObj teamPropertiesObj = null;		
		if(teamPropertiesObjList !=null && teamPropertiesObjList.size() > 0){
			for (TeamPropertiesObj obj : teamPropertiesObjList) {
				if(obj != null && obj.getCompanyId() != null && obj.getCompanyId().equals(companyId) && obj.getTeamName().startsWith(LinMobileConstants.TEAM_ALL_ENTITIE)) {
					teamPropertiesObj = obj;
					break;
				}
			}
		}	
		return teamPropertiesObj;
	}
	
	@Override
	public TeamPropertiesObj getNoEntitiesTeamByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception {
		log.info("UserDetailsDAO : getNoEntitiesTeamByTeamCompanyId..");
		TeamPropertiesObj teamPropertiesObj = null;		
		if(teamPropertiesObjList !=null && teamPropertiesObjList.size() > 0){
			for (TeamPropertiesObj obj : teamPropertiesObjList) {
				if(obj != null && obj.getCompanyId() != null && obj.getCompanyId().equals(companyId) && obj.getTeamName().startsWith(LinMobileConstants.TEAM_NO_ENTITIE)) {
					teamPropertiesObj = obj;
					break;
				}
			}
		}	
		return teamPropertiesObj;
	}
	
	@Override
	public String getSuperAdminRoleId() throws Exception {
		log.info("Inside getSuperAdminRoleId of UserDetailsDAO");
		String roleId = null;
		List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
		if(rolesAndAuthorisationList != null && rolesAndAuthorisationList.size() > 0) {
			for (RolesAndAuthorisation rolesAndAuthorisation : rolesAndAuthorisationList) {
				if(rolesAndAuthorisation != null && rolesAndAuthorisation.getRoleStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && rolesAndAuthorisation.getRoleName() != null && rolesAndAuthorisation.getRoleName().equals(LinMobileConstants.ADMINS[0])) {
					roleId = String.valueOf(rolesAndAuthorisation.getId());
					break;
				}
			}
		}
		return roleId;
	}
	
	@Override
	public TeamPropertiesObj getTeamPropertiesObjByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception {
		log.info("UserDetailsDAO : getTeamPropertiesObjByTeamCompanyId..");
		TeamPropertiesObj teamPropertiesObj = null;		
		if(teamPropertiesObjList !=null && teamPropertiesObjList.size() > 0 && companyId != null && !companyId.trim().equals("")){
			for (TeamPropertiesObj obj : teamPropertiesObjList) {
				if(obj != null && obj.getCompanyId() != null && obj.getCompanyId().trim().equals(companyId.trim())) {
					teamPropertiesObj = obj;
					break;
				}
			}
		}	
		return teamPropertiesObj;
	}
	@Override
	public List<LineItemDTO> getAccountsFromBigQuery(String publisherIdsForBigQuery, String accountPrefix,String agencyIdsNotIn, String advertiserIdsNotIn) {
		List<LineItemDTO> accountList = new ArrayList<LineItemDTO>();
		StringBuilder QUERY = new StringBuilder();
		QUERY.append("select Account, Account_id from (select agency + ' (")
			.append(LinMobileConstants.AGENCY_ID_PREFIX)
			.append(")' as Account, '")
			.append(LinMobileConstants.AGENCY_ID_PREFIX)
			.append("' + agency_id as Account_id From [")
			.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
			.append(".Core_Performance] Where data_source = '")
			.append(LinMobileConstants.DFP_DATA_SOURCE)
			.append("' and Pubisher_ID in ('")
			.append(publisherIdsForBigQuery)
			.append("') And agency contains '")
			.append(accountPrefix+"' ");
			if(agencyIdsNotIn != null && !agencyIdsNotIn.trim().equals("")) {
				QUERY.append(" and agency_id not in (")
				.append(agencyIdsNotIn.trim()+") ");
			}
			QUERY.append(" group by Account,Account_id ignore case)a, (select advertiser + ' (")
			.append(LinMobileConstants.ADVERTISER_ID_PREFIX)
			.append(")' as Account, '")
			.append(LinMobileConstants.ADVERTISER_ID_PREFIX)
			.append("' + advertiser_id as Account_id From [")
			.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
			.append(".Core_Performance] Where data_source = '")
			.append(LinMobileConstants.DFP_DATA_SOURCE)
			.append("' and Pubisher_ID in ('")
			.append(publisherIdsForBigQuery)
			.append("') And advertiser contains '")
			.append(accountPrefix+"' ");
			if(advertiserIdsNotIn != null && !advertiserIdsNotIn.trim().equals("")) {
				QUERY.append(" and advertiser_id not in (")
				.append(advertiserIdsNotIn.trim()+") ");
			}
			QUERY.append(" group by Account,Account_id ignore case) where Account contains '")
			.append(accountPrefix)
			.append("' order by Account limit 20 ignore case");
		
		log.info("getAccountsFromBigQuery QUERY = "+QUERY);
		
		
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					LineItemDTO lineItemDTO = new LineItemDTO();
					lineItemDTO.setAccountId(cellList.get(1).getV().toString());
					lineItemDTO.setAccountName(cellList.get(0).getV().toString());
					accountList.add(lineItemDTO);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("accountList size : "+accountList.size());
		return accountList;
	}
	
	@Override
	public CompanyObj getNonSuperAdminCompany(UserDetailsObj userDetailsObj, List<TeamPropertiesObj> teamPropertiesObjList, List<CompanyObj> companyObjList) throws Exception {
		CompanyObj companyObj = null;
		List<String> teamIdList = userDetailsObj.getTeams();
		 if(teamIdList != null && teamIdList.size() > 0) {
			 for(String teamId : teamIdList) {
				 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
				 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && !teamPropertiesObj.getCompanyId().trim().equals("")) {
					 companyObj = getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), companyObjList);
					 break;
				 }
			 }
		 }
		 return companyObj;
	}
	@Override
	public List<LineItemDTO> loadSitesFromBigQuery(String publisherIdsForBigQuery, String sitePrefix, String siteNameNotIn) {
		List<LineItemDTO> siteList = new ArrayList<LineItemDTO>();
		StringBuilder QUERY = new StringBuilder();
		QUERY.append("Select site_name, site_id From [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append(".Core_Performance] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' and Pubisher_ID in ('")
		.append(publisherIdsForBigQuery)
		.append("') And site_name contains '")
		.append(sitePrefix+"' ");
		if(siteNameNotIn != null && !siteNameNotIn.trim().equals("")) {
			QUERY.append(" and site_name not in (")
			.append(siteNameNotIn+") ");
		}
		QUERY.append(" Group by site_name, site_id Order by site_name limit 20 Ignore case");
		
		log.info("loadSitesFromBigQuery QUERY = "+QUERY);
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					LineItemDTO lineItemDTO = new LineItemDTO();
					lineItemDTO.setSiteId(cellList.get(1).getV().toString());
					lineItemDTO.setSiteName(cellList.get(0).getV().toString());
					siteList.add(lineItemDTO);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("siteList size : "+siteList.size());
		return siteList;
	}
	
	public String[] getAllAvailableAppViewsByUserDetailObj(UserDetailsObj userDetailsObj) throws Exception {
		CompanyObj companyObj = null;
		String companyId = null;
		String[] appViewArray = null;
		IUserDetailsDAO dao = new UserDetailsDAO();
		
		List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
		for(String teamId : userDetailsObj.getTeams()) {
			TeamPropertiesObj teamPropertiesObj = dao.getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
			if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null && !teamPropertiesObj.getCompanyId().trim().equals("")) {
				companyId = teamPropertiesObj.getCompanyId().trim();
				break;
			}
		}
		if(companyId != null) {
			companyObj = dao.getCompanyById(Long.valueOf(companyId), MemcacheUtil.getAllCompanyList());
			if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null) {
				if(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					appViewArray = LinMobileConstants.PUBLISHER_PARTNER_APP_VIEWS;
				}
				else if(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1])) {
					appViewArray = LinMobileConstants.DEMAND_PARTNER_APP_VIEWS;
				}
				else if(companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[2])) {
					appViewArray = LinMobileConstants.CLIENT_APP_VIEWS;
				}
			}
		}
		return appViewArray;
	}
	
	/*
	 * This method will get account data for a user to converts that in form suitable 
	 * for querying on BQ and sets the StringBuilders passed as parameter
	 */
	
	/*@Override
	public void getAccountsInfoForBQa(long userId, StringBuilder publisherIds, StringBuilder advertiserIds, StringBuilder agencyIds){
		 List<String> selectedAgenciesList = new ArrayList<String>();
		 List<String> selectedAdvertisersList = new ArrayList<String>();
		 List<CompanyObj> companyObjList = new ArrayList<CompanyObj>();
		 String getPublisherIds = "YES";
		 try {
			 if(publisherIds.toString().trim().length() > 0) {
				 getPublisherIds = "NO";
			 }
			 
			 UserDetailsObj userDetailsObj = getUserById(userId);
			 if(userDetailsObj != null && userDetailsObj.getRole() != null) {
				 List<CompanyObj> allCompanyList = MemcacheUtil.getAllCompanyList();
				 List<RolesAndAuthorisation> rolesAndAuthorisationList = MemcacheUtil.getAllRolesAndAuthorisationList();
				 String roleName = getRoleNameByRoleId(userDetailsObj.getRole().trim(), rolesAndAuthorisationList);
				 if(roleName != null && roleName.trim().equals(LinMobileConstants.ADMINS[0])) {
					 selectedAgenciesList.add(LinMobileConstants.NO_RESTRICTIONS);
					 selectedAdvertisersList.add(LinMobileConstants.NO_RESTRICTIONS);
					 companyObjList.addAll(allCompanyList);
				 }
				 else if(roleName != null && !roleName.trim().equals("") && userDetailsObj.getTeams() != null && userDetailsObj.getTeams().size() > 0) {
					 List<TeamPropertiesObj> teamPropertiesObjList = MemcacheUtil.getAllTeamPropertiesObjList();
					 List<String> teamIdList = userDetailsObj.getTeams();
					 if(teamIdList != null && teamIdList.size() > 0) {
						 int obtainPublisherId = 1;
						 for (String teamId : teamIdList) {
							 TeamPropertiesObj teamPropertiesObj = getTeamPropertiesObjById(teamId.trim(), teamPropertiesObjList);
							 if(teamPropertiesObj != null && teamPropertiesObj.getTeamStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && teamPropertiesObj.getCompanyId() != null) {
								 if(teamPropertiesObj.getAgencyId() != null && teamPropertiesObj.getAgencyId().size() > 0) {
									 for(String agencyId : teamPropertiesObj.getAgencyId()) {
										 if(agencyId != null && (agencyId.split("__")).length == 3) {
											 agencyId = (agencyId.split("__"))[2];
											 if(!selectedAgenciesList.contains(agencyId.trim()) && !selectedAdvertisersList.contains(LinMobileConstants.NO_RESTRICTIONS)) {
												 selectedAgenciesList.add(agencyId.trim());
											 }
										 }
									 }
								 }
								 if(teamPropertiesObj.getAdvertiserId() != null && teamPropertiesObj.getAdvertiserId().size() > 0) {
									 for(String advertiserId : teamPropertiesObj.getAdvertiserId()) {
										 if(advertiserId != null && (advertiserId.split("__")).length == 3) {
											 advertiserId = (advertiserId.split("__"))[2];
											 if(!selectedAdvertisersList.contains(advertiserId.trim()) && !selectedAdvertisersList.contains(LinMobileConstants.NO_RESTRICTIONS)) {
												 selectedAdvertisersList.add(advertiserId.trim());
											 }
										 }
									}
									 
								 }
								 if(obtainPublisherId == 1 && getPublisherIds.equalsIgnoreCase("YES")) {
									 CompanyObj companyObj = getCompanyById(Long.valueOf(teamPropertiesObj.getCompanyId()), allCompanyList);
									 if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
										 companyObjList.add(companyObj);
										 obtainPublisherId = 0;
									 }
								 }
							 }
						 }
					 }
					 if(roleName.trim().equals(LinMobileConstants.ADMINS[1])) {
						 selectedAgenciesList = new ArrayList<String>();
						 selectedAgenciesList.add(LinMobileConstants.NO_RESTRICTIONS);
						 selectedAdvertisersList = new ArrayList<String>();
						 selectedAdvertisersList.add(LinMobileConstants.NO_RESTRICTIONS);
					 }
				 }
				 if(getPublisherIds.equalsIgnoreCase("YES")) {
					 publisherIds.append(getPublisherIdInBQ(companyObjList));
				 }
				 advertiserIds.append(UserService.createCommaSeperatedStringForBigQuery(selectedAdvertisersList));
				 agencyIds.append(UserService.createCommaSeperatedStringForBigQuery(selectedAgenciesList));
			 }
		 }
		 catch (Exception e) {
			e.printStackTrace();
			log.severe("Exception in getAccountsInfoForBQ : "+e.getMessage());
		}
	 }*/
	
	/*private String getPublisherIdInBQ(List<CompanyObj> companyObjList) throws Exception {
		List<String> tempList = null;
		List<String> publisherIdInBigQueryList = new ArrayList<String>();
		if(companyObjList != null && companyObjList.size() > 0) {
			List<DfpBQMappingObj> adServerToPublisherInBigQueryObjList = MemcacheUtil.getAllAdServerToPublisherInBigQueryObj();
			for(CompanyObj companyObj : companyObjList) {
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
					if(companyObj.getAdServerId() != null && companyObj.getAdServerUsername() != null && companyObj.getAdServerPassword() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerId().size() == companyObj.getAdServerUsername().size() && companyObj.getAdServerUsername().size() == companyObj.getAdServerPassword().size()) {
						int length = companyObj.getAdServerId().size();
						for(int i=0; i<length; i++) {
							if(companyObj.getAdServerId().get(i) != null && companyObj.getAdServerUsername().get(i) != null) {
								String adServerId = companyObj.getAdServerId().get(i).trim();
								String adServerUserName = companyObj.getAdServerUsername().get(i).trim();
								DfpBQMappingObj dfpBQMappingObj = getDfpBQMappingObj(adServerId, adServerUserName, adServerToPublisherInBigQueryObjList);
								if(dfpBQMappingObj != null && dfpBQMappingObj.getPublisherIdInBigQuery() != null && dfpBQMappingObj.getPublisherIdInBigQuery().size() > 0) {
									tempList = dfpBQMappingObj.getPublisherIdInBigQuery();
									for(String publisherId : tempList) {
										if(publisherId != null && !publisherId.trim().equals("") && !publisherIdInBigQueryList.contains(publisherId.trim())) {
											publisherIdInBigQueryList.add(publisherId.trim());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return UserService.createCommaSeperatedStringForBigQuery(publisherIdInBigQueryList);
	}*/
	
	@Override
	public Map<String, String> loadAgenciesFromBigQuery(String publisherIdsForBigQuery, String agencyId) throws Exception {
		Map<String, String> agencyMap = new LinkedHashMap<String, String>();
		StringBuilder QUERY = new StringBuilder();
		
		/*QUERY.append("select agency, agency_id from [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append(".Core_Performance] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' and Pubisher_ID in ('")
		.append(publisherIdsForBigQuery)
		.append("') and agency_id not in ('0','-1') group by agency,agency_id order by agency");*/
		
		QUERY.append("select agency, agency_id from [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append("."+LinMobileConstants.CORE_PERFORMANCE_TABLE_ID+"] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' and Pubisher_ID in ('")
		.append(publisherIdsForBigQuery)
		.append("') and agency_id not in ('0','-1') ");
		if(agencyId !=null && agencyId.trim().length()>0){
			QUERY.append(" and agency_id in ('"+agencyId+"') ");
		}
		QUERY.append(" group by agency,agency_id order by agency");
		
		log.info("loadAgenciesFromBigQuery QUERY = "+QUERY);
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					String key=cellList.get(1).getV().toString();
					String value=cellList.get(0).getV().toString();
					agencyMap.put(key, value);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("agencyMap size : "+agencyMap.size());
		return agencyMap;
	}
	@Override
	public Map<String, String> loadAdvertisersFromBigQuery(String publisherIdsForBigQuery, String advertiserIds) throws Exception {
		Map<String, String> advertiserMap = new LinkedHashMap<String, String>();
		StringBuilder QUERY = new StringBuilder();
		
		/*QUERY.append("select advertiser, advertiser_id from [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append("."+LinMobileConstants.CORE_PERFORMANCE_TABLE_ID+"] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' and Pubisher_ID in ('")
		.append(publisherIdsForBigQuery)
		.append("') and advertiser_id not in ('0','-1') group by advertiser,advertiser_id order by advertiser");*/
		
		QUERY.append("select advertiser, advertiser_id from [")
		.append(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID)
		.append("."+LinMobileConstants.CORE_PERFORMANCE_TABLE_ID+"] Where data_source = '")
		.append(LinMobileConstants.DFP_DATA_SOURCE)
		.append("' and Pubisher_ID in ('")
		.append(publisherIdsForBigQuery)
		.append("') and advertiser_id not in ('0','-1')");
		if(advertiserIds !=null && advertiserIds.trim().length()>0){
			QUERY.append(" and advertiser_id in ('"+advertiserIds+"') ");
		}
		QUERY.append(" group by advertiser,advertiser_id order by advertiser");
		
		log.info("loadAdvertisersFromBigQuery QUERY = "+QUERY);
		
		
		QueryResponse queryResponse = null;
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(QUERY.toString());
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}
			j++;
		}while(!queryResponse.getJobComplete() && j<=3);

		if (queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
			try {
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					String key=cellList.get(1).getV().toString();
					String value=cellList.get(0).getV().toString();
					advertiserMap.put(key, value);
				}
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				e.printStackTrace();
			}

		}
		log.info("advertiserMap size : "+advertiserMap.size());
		return advertiserMap;
	}
	
	@Override
	public List<AccountsEntity> getAccountsObjByAdServerIdUsername(String adServerId, String adServerUsername) throws Exception {
		List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
		if(adServerId != null && !adServerId.trim().equals("") && adServerUsername != null && !adServerUsername.trim().equals("")){
			List<AccountsEntity> accountsObjs = obfy.load().type(AccountsEntity.class)
												.filter("adServerId = ", adServerId.trim())
												.filter("adServerUserName = ", adServerUsername.trim())
												.list();
			if(accountsObjs != null && accountsObjs.size() > 0) {
				accountsObjList.addAll(accountsObjs);
			}
		}
		log.info("accountsObjList size :"+accountsObjList.size());
		return accountsObjList;
	}
	
	@Override
	public List<AccountsEntity> getAccountsObjByCompanyId(String companyId) throws Exception {
		List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
		if(companyId != null && !companyId.trim().equals("")) {
			List<AccountsEntity> accountsObjs = obfy.load().type(AccountsEntity.class)
												.filter("companyId = ", companyId.trim())
												.list();
			if(accountsObjs != null && accountsObjs.size() > 0) {
				accountsObjList.addAll(accountsObjs);
			}
		}
		log.info("accountsObjList size :"+accountsObjList.size());
		return accountsObjList;
	}
	
	@Override
	public List<AccountsEntity> getAccounts(String companyId, String adServerId, String adServerUsername) throws Exception {
		List<AccountsEntity> accountsObjList = new ArrayList<AccountsEntity>();
		if(companyId != null && !companyId.trim().equals("") && adServerId != null && !adServerId.trim().equals("") && adServerUsername != null && !adServerUsername.trim().equals("")){
			List<AccountsEntity> accountsObjs = obfy.load().type(AccountsEntity.class)
												.filter("adServerId = ", adServerId.trim())
												.filter("adServerUserName = ", adServerUsername.trim())
												.filter("companyId = ", companyId.trim())
												.list();
			if(accountsObjs != null && accountsObjs.size() > 0) {
				accountsObjList.addAll(accountsObjs);
			}
		}
		log.info("accountsObjList size :"+accountsObjList.size());
		return accountsObjList;
	}
	
	@Override
	public AccountsEntity getAccountsObjById(String id) throws Exception {
		AccountsEntity accountsObj = null;
		if(id != null && !(id.trim().equals(""))){
			accountsObj = obfy.load().type(AccountsEntity.class)
												.id(id.trim()).now();
		}
		/*if(accountsObjList != null && accountsObjList.size() > 0) {
			log.info("accountsObjList size :"+accountsObjList.size());
		}
		else {
			log.info("accountsObjList : null");
		}*/
		return accountsObj;
	}
	
	@Override
	public AccountsEntity getAccountsObjById(String id, List<AccountsEntity> accountsObjList) throws Exception {
		if(accountsObjList != null && accountsObjList.size() > 0 && id != null && id.trim().length() > 0) {
			id = id.trim();
			for (AccountsEntity accountsObj : accountsObjList) {
				if(accountsObj.getId() != null && accountsObj.getId().equals(id)) {
					return accountsObj;
				}
			}
		}
		log.info("accountsObjList : null");
		return null;
	}
	
	@Override
	public AccountsEntity getAccountsObjByAccountName(String accountName, List<AccountsEntity> accountsObjList) throws Exception {
		AccountsEntity accountsObj = null;
		if(accountsObjList !=null && accountsObjList.size() > 0){
			for (AccountsEntity obj : accountsObjList) {
				if(obj != null && obj.getId() != null && obj.getAccountName().equalsIgnoreCase(accountName)) {
					accountsObj = obj;
					break;
				}
			}
		}	
		return accountsObj;
	}
	
	@Override
	public AccountsEntity getAccountsObjByDfpAccountId(String dfpAccountId, List<AccountsEntity> accountsObjList) throws Exception {
		AccountsEntity accountsObj = null;
		if(accountsObjList !=null && accountsObjList.size() > 0){
			for (AccountsEntity obj : accountsObjList) {
				if(obj != null && obj.getId() != null && obj.getAccountDfpId().equalsIgnoreCase(dfpAccountId)) {
					accountsObj = obj;
					break;
				}
			}
		}	
		return accountsObj;
	}
	
	@Override
	public AccountsEntity getAccountsObjByDfpAccountId(String dfpAccountId) throws Exception {
		AccountsEntity accountsObj = null;
		List<AccountsEntity> accountsObjList = null;
		if(dfpAccountId != null && !(dfpAccountId.trim().equals(""))){
			accountsObjList = obfy.load().type(AccountsEntity.class)
												.filter("accountDfpId = ", dfpAccountId.trim())
												.list();
			if(accountsObjList != null && accountsObjList.size() > 0) {
				accountsObj = accountsObjList.get(0);
			}
		}
		if(accountsObjList != null && accountsObjList.size() > 0) {
			log.info("accountsObjList size :"+accountsObjList.size());
		}
		else {
			log.info("accountsObjList : null");
		}
		return accountsObj;

	}
	
	@Override
	/*
	 * This method will fetch all advertiser and agencies on the basis of their Ids
	 * for a given company Id
	 * @author Naresh Pokhriyal
	 * @param String advertiserIds,String agencyIds, boolean getAdvetisers, boolean getAgencies, QueryDTO queryDTO
	 * gets advetisers from BQ if getAdvetisers is set TRUE, 
	 * gets agencies from BQ if getAgencies is set TRUE,
	 * get selected advertisers and agencies if corresponding variables(advertiserIds, agencyIds) are not empty,
	 * @return Map<String,String>
	 */
	public Map<String,String> loadAdvertiserAndAgencyInBQ(String advertiserIds,String agencyIds, boolean getAdvetisers, boolean getAgencies, QueryDTO queryDTO) throws DataServiceException, IOException{
		Map<String,String> resultMap=new LinkedHashMap<String,String>();
		if(getAdvetisers || getAgencies) {
			String ID = "";
			if(getAdvetisers && getAgencies && advertiserIds !=null && advertiserIds.trim().length()>0 && agencyIds !=null && agencyIds.trim().length()>0) {
				ID = advertiserIds + "','" + agencyIds;
			}
			else if(getAdvetisers && advertiserIds !=null && advertiserIds.trim().length()>0) {
				ID = advertiserIds;
			}
			else if(getAgencies && agencyIds !=null && agencyIds.trim().length()>0) {
				ID = agencyIds;
			}
			StringBuffer query=new StringBuffer();		
			query.append(" Select * From ");
			if(getAdvetisers) {
				query.append(" ( Select Advertiser_id as ID, Advertiser + ' ("+LinMobileConstants.ADVERTISER_ID_PREFIX+")'as Name ")
				.append("From "+queryDTO.getQueryData())
				.append(" Where data_source = '")
				.append(LinMobileConstants.DFP_DATA_SOURCE)
				.append("' and Advertiser_id <> '0' ");
				query.append(" Group By ID, Name  ignore case )a ");
			}
			if(getAdvetisers && getAgencies) {
				query.append(" , ");
			}
			if(getAgencies) {
				query.append(" ( Select Agency_id as ID, Agency + ' ("+LinMobileConstants.AGENCY_ID_PREFIX+")' as Name ")
				.append("From "+queryDTO.getQueryData())
				.append(" Where data_source = '")
				.append(LinMobileConstants.DFP_DATA_SOURCE)
				.append("' and Agency_id <> '0'");
				query.append(" Group By ID, Name ignore case )b ");
			}
			if(ID !=null && ID.trim().length()>0) {
				query.append(" where ID in ('"+ID+"') ");
			}
			query.append(" order by Name LIMIT 2500 ignore case");
			
			log.info("loadAdvertiserAndAgencyInBQ: Query:"+query.toString());
			
			
			QueryResponse queryResponse = null;
			queryDTO.setQueryData(query.toString());
			
			int j = 0;
			do {
				//log.info("Going to fetch advertisers and agencies : query attempts:"+(j+1)+" out of max attempts: 3");
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
				//log.info("queryResponse.getJobComplete() : "+queryResponse.getJobComplete());			
				j++;
			} while (!queryResponse.getJobComplete() && j <= 3);
	
			if (queryResponse != null && queryResponse.getRows() != null) {
				List<TableRow> rowList = queryResponse.getRows();
	            log.info("total row results found :"+rowList.size());
				for (TableRow row : rowList) {
					List<TableCell> cellList = row.getF();
					String id="_"+cellList.get(0).getV().toString();
					String value=cellList.get(1).getV().toString();
					resultMap.put(id, value);				
				}
	
			}
		}
		return resultMap;		
	}
	
	/*
	 * This method will get all active teams of the company from Datastore
	 * @author Naresh Pokhriyal
	 * @param  long companyId
	 * @return List<TeamPropertiesObj>
	 */
	@Override
	public List<TeamPropertiesObj> getAllActiveTeamsOfCompany(String companyId) throws DataServiceException {
		log.info("getAllTeamsOfCompany companyId : "+companyId);
		List<TeamPropertiesObj> resultList = obfy.load().type(TeamPropertiesObj.class)
				.filter("teamStatus = ", LinMobileConstants.STATUS_ARRAY[0])
				.filter("companyId = ", companyId)
				.list();
		return resultList;
	}
	
	/*
	 * This method will get all active users of the team from Datastore
	 * @author Naresh Pokhriyal
	 * @param List<String> teamIdList
	 * @return List<UserDetailsObj>
	 */
	@Override
	public List<UserDetailsObj> getAllActiveUsersOfTeam(List<String> teamIdList) throws DataServiceException {
		log.info("In getAllActiveUsersOfTeam");
		List<UserDetailsObj> resultList = null;
		if(teamIdList != null && teamIdList.size() > 0) {
			log.info("teamIdList size : "+teamIdList.size());
			resultList = obfy.load().type(UserDetailsObj.class)
					.filter("status = ", LinMobileConstants.STATUS_ARRAY[0])
					
					.filter("teams IN ", teamIdList)
					.list();
		}
		else {
			log.info("teamIdList is Empty");
		}
		return resultList;
	}
	
	@Override
	public List<UserDetailsObj> getAllUsersIncludingDeleted() throws Exception {
		log.info("UserDetailsDAO : getAllUsersIncludingDeleted..");
		List<UserDetailsObj> resultList = obfy.load().type(UserDetailsObj.class).list();
		return resultList;
	}
	
	@Override
	public CompanyObj getCompanyByCompanyId(long companyId) throws Exception{
		 CompanyObj companyObj = null;
		 companyObj = obfy.load().type(CompanyObj.class).id(companyId).now();
		 return companyObj;
	 }
	
	
}
