package com.lin.persistance.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AnonymousUserDetailsObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.PropertyDropDownDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.UserDetailsDTO;


public interface IUserDetailsDAO extends IBaseDao{
	
	public void saveObject(Object obj) throws DataServiceException;
	
	public void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException;
	
	public void deleteObject(Object obj) throws DataServiceException;

	public UserDetailsObj getUserDetails(String emailId, String encriptedPassword) throws Exception;

	public long getMaxCount() throws Exception;

	long getEmailAuthObj(String randomNumber, String status) throws Exception;

	public UserDetailsObj getUserById(long id) throws Exception;

	public UserDetailsObj getUserByEmailId(String emailId) throws Exception;

	public Boolean authorizeUser(UserDetailsObj userDetailsObj, UserDetailsDTO userDetailsDTO) throws Exception;

	public void getAllUsers(UserDetailsDTO userDetailsDTO) throws Exception;

	public AnonymousUserDetailsObj getAnonymousUserByEmailId(String email) throws Exception;

	public Boolean resetPassword(UserDetailsObj userDetailsObj, UserDetailsDTO userDetailsDTO) throws Exception;

	public List<UserDetailsObj> getAllUsers() throws Exception;

	public List<AuthorisationTextObj> getAllAuthorisationText() throws Exception;

	public List<RolesAndAuthorisation> getAllRolesAndAuthorisation() throws Exception;

	public List<TeamPropertiesObj> getAllTeamPropertiesObj() throws Exception;
	
	public List<CompanyObj> getAllCompany() throws Exception;

	public CompanyObj getCompanyById(Long companyId, List<CompanyObj> companyObjList) throws Exception;
	
	public PropertyObj getPropertyObjById(String propertyId, List<PropertyObj> resultList) throws Exception;
	
	public CompanyObj getCompanyObjByIdAndCompanyType(String companyId, String companyType, List<CompanyObj> companyObjList) throws Exception;
	
	public CompanyObj getCompanyObjByNameAndCompanyType(String companyName, String companyType, List<CompanyObj> companyObjList) throws Exception;

	public TeamPropertiesObj getTeamPropertiesObjById(String teamId, List<TeamPropertiesObj> resultList) throws Exception;

	public List<PropertyDropDownDTO> loadPropertyDropDownList(String publisherId, long userId, String term);

	public List<CompanyObj> getSelectedCompaniesByUserId(long userId) throws Exception;

	public List<CompanyObj> getSelectedPublishersByUserId(long userId, List<String> publisherIdList, List<CompanyObj> publisherObjList) throws Exception;
	
	public List<String> getSelectedAppViewsByUserId(long userId) throws Exception;

	public List<PropertyObj> getSelectedPropertiesByUserIdAndPublisherId(long userId, String publisherId) throws Exception;

	public CompanyObj getCompanyByName(String companyName, List<CompanyObj> companyObjList) throws Exception;

	public List<String> getAllSuperAdminUserId(String superAdminRoleId) throws Exception;

	public String getRoleNameByRoleId(String roleId, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception;

	public RolesAndAuthorisation getRolesAndAuthorisationByRoleId(String roleId, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception;

	public List<String> getPassbackValues(List<CompanyObj> demandPartnersList) throws Exception;

	public List<CompanyObj> getSelectedDemandPartnersByUserId(long userId) throws Exception;
	
	public List<CommonDTO> getSelectedDemandPartnersDropDownByUserId(long userId) throws Exception;

	public RolesAndAuthorisation getRoleByRoleName(String roleName, List<RolesAndAuthorisation> rolesAndAuthorisationList) throws Exception;

	public List<UserDetailsObj> getUsersByCompanyIdAndRoleName(String selectedCompanyId, String roleName) throws Exception;

	public TeamPropertiesObj getTeamNameAvailable(String teamName, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception;

	public PropertyObj propertyNameAvailable(String propertyName, List<PropertyObj> propertyObjList) throws Exception;

	public UserDetailsObj getUserByEmailIdFromDataStore(String emailId) throws Exception;

	public List<LineItemDTO> getAccountsFromBigQuery(String publisherIdsForBigQuery, String accountPrefix,String agencyIdsNotIn, String advertiserIdsNotIn);

	public List<LineItemDTO> loadSelectedAgenciesFromBigQuery(String selectedAgencies) throws Exception;
	
	public List<LineItemDTO> loadSelectedAdvertisersFromBigQuery(String selectedAdvertisers) throws Exception;

	public List<String> getSelectedAdvertisersByUserIda(long userId) throws Exception;

	public TeamPropertiesObj getAllEntitiesTeamByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception;

	public TeamPropertiesObj getNoEntitiesTeamByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception;

	public List<CompanyObj> getAllPublishers(List<CompanyObj> companyObjList) throws Exception;

	public List<CompanyObj> getAllDemandPartners(List<CompanyObj> companyObjList) throws Exception;

	public List<CompanyObj> getAllClients(List<CompanyObj> companyObjList) throws Exception;

	public String getSuperAdminRoleId() throws Exception;

	public TeamPropertiesObj getTeamPropertiesObjByTeamCompanyId(String companyId, List<TeamPropertiesObj> teamPropertiesObjList) throws Exception;

	public List<CompanyObj> getActiveDemandPartnersByPublisherCompanyId(String publisherId) throws Exception;

	public List<CommonDTO> getActivePropertiesByPublisherCompanyId(String publisherId) throws Exception;

	public CompanyObj getNonSuperAdminCompany(UserDetailsObj userDetailsObj, List<TeamPropertiesObj> teamPropertiesObjList, List<CompanyObj> companyObjList) throws Exception;

	public List<LineItemDTO> loadSitesFromBigQuery(String publisherIdsForBigQuery, String sitePrefix, String siteNameNotIn);

	//public void getAccountsInfoForBQa(long userId, StringBuilder publisherIds, StringBuilder advertiserIds, StringBuilder agencyIds);

	public Map<String, String> loadAdvertisersFromBigQuery(String publisherIdsForBigQuery, String advertiserIds) throws Exception;

	public Map<String, String> loadAgenciesFromBigQuery(String publisherIdsForBigQuery, String agencyId) throws Exception;

	public List<AccountsEntity> getAccountsObjByAdServerIdUsername(String adServerId, String adServerUsername) throws Exception;

	public AccountsEntity getAccountsObjById(String id) throws Exception;

	public AccountsEntity getAccountsObjById(String id, List<AccountsEntity> accountsObjList) throws Exception;

	public AccountsEntity getAccountsObjByAccountName(String accountName, List<AccountsEntity> accountsObjList) throws Exception;

	public AccountsEntity getAccountsObjByDfpAccountId(String dfpAccountId, List<AccountsEntity> accountsObjList) throws Exception;

	public Map<String, String> loadAdvertiserAndAgencyInBQ(String advertiserIds, String agencyIds, boolean getAdvetisers, boolean getAgencies, QueryDTO queryDTO) throws DataServiceException, IOException;

	public PropertyObj getPropertyObjById(String propertyId) throws Exception;

	public PropertyObj getPropertyObjByDFPPropertyId(String dfpPropertyId, List<PropertyObj> propertyObjList) throws Exception;

	public List<AccountsEntity> getAccountsObjByCompanyId(String companyId) throws Exception;

	public List<AccountsEntity> getAccounts(String companyId, String adServerId, String adServerUsername) throws Exception;

	public List<PropertyObj> getAllPropertyObjByCompanyId(String companyId) throws Exception;

	public CompanyObj getCompanyByBqIdentifier(String bqIdentifier, List<CompanyObj> companyObjList) throws Exception;

	public AccountsEntity getAccountsObjByDfpAccountId(String dfpAccountId) throws Exception;

	public List<TeamPropertiesObj> getAllActiveTeamsOfCompany(String companyId) throws DataServiceException;

	public List<UserDetailsObj> getAllActiveUsersOfTeam(List<String> teamIdList) throws DataServiceException;

	List<UserDetailsObj> getAllUsersIncludingDeleted() throws Exception;

	public List<CompanyObj> getAllCompany(String companyType) throws DataServiceException;

	CompanyObj getCompanyByCompanyId(long companyId) throws Exception;

	public Map<String, AccountsEntity> loadAllAccountsByIds(String[] accountIds) throws Exception;
  
}
