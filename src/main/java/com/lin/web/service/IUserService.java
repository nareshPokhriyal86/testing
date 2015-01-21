package com.lin.web.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.LoginDetailsDTO;
import com.lin.web.dto.UserDetailsDTO;


public interface IUserService extends IBusinessService{

	public Boolean loginAuthentication(LoginDetailsDTO login) throws Exception;

	public Boolean googleLoginAuthentication(LoginDetailsDTO login, String emailId) throws Exception;

	public List<CommonDTO> fetchAllActiveRoles(long userId, boolean superAdmin) throws Exception;
	
	public List<CommonDTO> fetchActiveRolesByCompanyId(String companyId, boolean superAdmin) throws Exception;

	public Boolean createUser(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public long authorizeEmail(String randomNumber) throws Exception;

	public Boolean authorizeUser(UserDetailsDTO userDetailsDTO) throws Exception;

	public void userSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public Boolean initEditUser(UserDetailsDTO userDetailsDTO) throws Exception;

	public int userUpdate(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public int forgetPassword(UserDetailsDTO userDetailsDTO) throws Exception;

	public long forgetPasswordEmail(String randomNumber) throws Exception;

	public Boolean resetPassword(UserDetailsDTO userDetailsDTO) throws Exception;

	public Boolean initUserOwnProfileUpdate(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public Boolean userOwnProfileUpdate(UserDetailsDTO userDetailsDTO) throws Exception;

	public List<AuthorisationTextObj> fetchAllActiveAuthorisationText() throws Exception;

	public Boolean createRole(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public Boolean initEditRole(UserDetailsDTO userDetailsDTO, boolean superAdmin) throws Exception;

	public Boolean updateRole(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public Boolean copyRole(UserDetailsDTO userDetailsDTO) throws Exception;
	
	public Boolean createTeam(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public Boolean updateTeam(UserDetailsDTO userDetailsDTO, long updateByUserId) throws Exception;
	
	public Boolean createNewCompany(UserDetailsDTO userDetailsDTO, long userId)  throws Exception;
	
	public Boolean initEditCompany(UserDetailsDTO userDetailsDTO) throws Exception;
	
	public Boolean updateCompany(UserDetailsDTO userDetailsDTO,long updateDeleteByUserId) throws Exception;

	public void roleSetup(UserDetailsDTO userDetailsDTO, long userId, boolean isSuperAdmin) throws Exception;

	public void fetchTeamsByCompanyId(String companyId, UserDetailsDTO userDetailsDTO) throws Exception;

	public void teamSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public CompanyObj getCompanyIdByAdminUserId(long userId) throws Exception;

	public void getAuthorisations(UserDetailsDTO userDetailsDTO, String roleId);

	public String getFirstPageForLoginUser(String roleId, long userId, LoginDetailsDTO login) throws Exception;

	public void getAuthorisedPagesForUser(String roleId, long userId, UserDetailsDTO userDetailsDTO) throws Exception;

	public void propertySetup(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception;

	public List<CommonDTO> getPublishersForPropertySetup(long userId, boolean superAdmin) throws Exception;

	public Boolean createProperty(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public Boolean initEditProperty(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception;

	public Boolean updateProperty(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public List<String> CommaSeperatedStringToList(String commaSeperatedString);

	public String getPublisherBQId(String publisherCompanyId);

	public List<LineItemDTO> loadAccountsForTeam(String publisherIdsForBigQuery, String accountPrefix, String accountIdsNotIn) throws Exception;

	public void initEditTeam(TeamPropertiesObj teamProperties, UserDetailsDTO userDetailsDTO) throws Exception;

	//public List<CommonDTO> getAllAppViewsByCompanyType(String companyType) throws Exception;

	public Boolean companySettings(UserDetailsDTO userDetailsDTO, long userId) throws Exception;

	public List<CommonDTO> getAllAppViewsforCompany();

	public List<CommonDTO> getTeamDataByCompanyId(String companyId) throws Exception;

	//public String getPublisherIdsForBigQueryByCompanyName(String companyName);

	public Map<String, String> loadAdvertisersFromBigQuery(String publisherIdsForBigQuery, String advertiserIds);

	public Map<String, String> loadAgenciesFromBigQuery(String publisherIdsForBigQuery, String agencyIds);

	public void accountSetup(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public Boolean createAccount(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;

	public boolean initEditAccount(UserDetailsDTO userDetailsDTO, boolean superAdmin, long userId) throws Exception;

	public Boolean updateAccount(UserDetailsDTO userDetailsDTO, long userId, boolean superAdmin) throws Exception;
	
	public String convertListToSeperatedString(List<String> list, String seperator);

	//public Map<String, AdServerCredentialsDTO> getCompanyDFPCredentials(List<CompanyObj> companyObjList);

	//public List<AdServerCredentialsDTO> getCompanyDFPCredentials(List<CompanyObj> companyObjList, String seperator);

	public List<AdServerCredentialsDTO> loadAdserverCredentialsForDropDown(List<CompanyObj> companyObjList) throws Exception;

	public List<AccountsEntity> getActiveAccountsObjByCompanyId(String companyId) throws Exception;

	public List<CommonDTO> getActiveAccountsByCompanyId(String companyId, boolean addPrefix) throws Exception;

	/*public Map<String, String> getActiveAgenciesByCompanyId(String companyId) throws Exception;
	
	public Map<String, String> getActiveAdvertisersByCompanyId(String companyId, boolean getAdvertiser, boolean getAgencies) throws Exception;*/

	//public String getPublisherIdsForBigQuery(List<CompanyObj> companyObjList);

	public Map<String, String> getActiveAccountsForDropDown(String companyId, boolean getAdvertiser, boolean getAgencies) throws Exception;

	public Map<String, String> getSelectedAccountsByUserIdAndCompanyId(long userId, String companyId, boolean getAdvertiser, boolean getAgency) throws Exception;

	public Map<String, String> getSelectedAccountsForCampaingnPlanning(long userId, String companyId, boolean getAdvertiser, boolean getAgency) throws Exception;

	public List<PropertyObj> getSiteIdsByBqIdentifier(String companyId);

	public Map<String, String> getAdunit1(List<PropertyObj> propertyObjList);

	public String getPublisherBQId(CompanyObj companyObj);

	Map<String, String> getSelectedAccountsByUserId(long userId, boolean getAdvertiser, boolean getAgency) throws Exception;

	String getSelectedAccountsInfo(long userId, boolean getAdvertiser, boolean getAgency) throws Exception;
	
	String getCompanyLogo(String companyId);

	public List<UserDetailsObj> getAllEmailOptedUsersList();
	
	List<UserDetailsObj> getAllActiveUsersOfCompany(long companyId);

	JSONObject resendAuthorizeEmail(String userId) throws Exception;

	boolean deleteUser(long userIdToDelete, long deletedByUserId) throws Exception;
	
	public List<CompanyObj> loadAllPartners();
	public JSONObject loadPartnersJSON(List<CompanyObj> partnerList) throws JSONException;

	public JSONObject searchAccounts(long userId, boolean getAdvertiser, boolean getAgency, String searchText) throws Exception;
	
	public CompanyObj getCompanyById(Long companyId, List<CompanyObj> companyObjList);
	public List<CompanyObj> getSelectedCompaniesByUserId(long userId);

	CompanyObj getCompanyObjById(long comapanyId);
	
}
