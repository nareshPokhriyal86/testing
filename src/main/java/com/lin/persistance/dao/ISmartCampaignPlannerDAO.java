package com.lin.persistance.dao;

import java.util.List;
import java.util.Map;

import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.AlertEngineFlightObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.SmartCampaignFlightObj;
import com.lin.server.bean.SmartCampaignHistObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.QueryDTO;

public interface ISmartCampaignPlannerDAO {

	public List<SmartCampaignFlightObj> getAllFlightsOfPlacement(long id) throws DataServiceException;


	public List<DropdownDataObj> getDropDownDataList(String valueType)
			throws Exception;


	public void saveObject(Object obj) throws DataServiceException;
	public void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException;


	public void deleteObject(Object obj) throws DataServiceException;

	public CreativeObj getCreativeById(String id) throws Exception;

	public DeviceObj getDeviceById(String id) throws Exception;

	public GeoTargetsObj getGeoTargetById(String id) throws Exception;

	public PlatformObj getPlatformById(String id) throws Exception;



	public SmartCampaignObj getCampaignById(long id) throws DataServiceException;

	public long maxCampaignId() throws DataServiceException;

	public long maxPlacementId() throws DataServiceException;

	public long maxFlight() throws DataServiceException;

	public void savePlacementObjectList(List<SmartCampaignPlacementObj> list)
			throws DataServiceException;

	public void saveFlightObjectList(List<SmartCampaignFlightObj> list)
			throws DataServiceException;

	public List<SmartCampaignPlacementObj> getAllPlacementOfCampaign(long campaignId)
			throws DataServiceException;

	void deletePlacementList(List<SmartCampaignPlacementObj> placementList)
			throws DataServiceException;

	void deleteFlightList(List<SmartCampaignFlightObj> flightList)
			throws DataServiceException;

	public List<CountryObj> getAllCountry() throws DataServiceException;

	public IABContextObj getIABContextById(long id) throws DataServiceException;

	public CountryObj getCountryById(long id) throws DataServiceException;



/*	public AgencyObj getAgencyById(String id) throws Exception;

*/

	public AdvertiserObj getAdvertiserById(long id) throws Exception;

	public DropdownDataObj getEducationById(String id) throws Exception;


	public List<SmartCampaignPlacementObj> getPlacementObjList(long placementId)
			throws DataServiceException;


	public StateObj getStateById(long id, long parenId) throws DataServiceException;


	public DropdownDataObj getRateTypeById(long objectId, String valueType)
			throws Exception;

	public List<DropdownDataObj> getRateTypeByValue(String valueType) throws DataServiceException;

	public SmartCampaignPlacementObj getPlacementById(long placementId) throws DataServiceException;


	/*public AgencyObj getAgencyById(long id) throws Exception;*/

	public List<SmartCampaignObj> getSmartCampaignList(String campaignStatus,
			String companyId) throws Exception;
	/*	List<SmartCampaignObj> getSmartCampaignList(String campaignStatus,
			String adServerId) throws Exception;*/


	public List<AdvertiserObj> getAllAdvertiserByAdServerId(String adServerId) throws Exception;


	public List<AgencyObj> getAllAgencyByAdServerId(String adServerId);


	public List<SmartCampaignObj> getSmartCampaignListSuperUser(String campaignStatus)
			throws Exception;


	public List<SmartCampaignObj> getAllCampaign() throws DataServiceException;


	QueryResponse getAllCampaignsForMigration(QueryDTO queryDTO) throws DataServiceException;


	public CreativeObj getCreative(String format, String size) throws DataServiceException;
	public SmartCampaignObj getCampaignByDFPOrderId(long dfpOrderId,String adServerCode) throws DataServiceException;


	public List<SmartCampaignObj> getCampaignByDFPOrderId(String adServerId, String adServerUsername) throws DataServiceException;

	public List<SmartCampaignObj> getCampaignByAdserverId(String adServerId) throws DataServiceException;
	
	public List<SmartCampaignObj> getCampaignsByCompanyId(String companyId) throws DataServiceException;
	public SmartCampaignObj getCampaignByDFPOrderId(long dfpOrderId) throws DataServiceException;

	public List<SmartCampaignObj> getAllCampaign(String adServerCode) throws DataServiceException;

	public SmartCampaignObj getCampaignByCampaignId(long campaignId) throws DataServiceException;

	public List<AlertEngineFlightObj> getAllAlertEngineFlightObj(long placementId) throws DataServiceException;

	List<SmartCampaignHistObj> getCampaignHistByCampaignId(long campaignId) throws DataServiceException;


	public List<SmartCampaignObj> loadAllCampaignsByIds(List<Long> campaignIdList) throws DataServiceException;


	public List<SmartCampaignPlacementObj> loadAllPlacementsByIds(List<Long> placementIdList) throws DataServiceException;


	public QueryResponse campaignReportData(String lineItemIds, QueryDTO queryDTO, String startDate, String endDate)  throws Exception;

	List<CompanyObj> getAllCompanyList() throws DataServiceException;


	List<AccountsEntity> getAllAccounts() throws Exception;
	public List<AccountsEntity> getAllAccounts(String adServerId)throws Exception;

	public AccountsEntity loadAccount(String accountDFPId, String adServerId)throws Exception;


	List<AccountsEntity> getAllAccountsByName(String name) throws Exception;

	public AccountsEntity getAccountByID(String accountId)throws Exception;   //shubham
	

	public List<AccountsEntity> getAllAccountByAdServerId(String adServerId , String accountType);   //shubham
	
	public List<AccountsEntity> getAllAccountByCompanyId(String companyId, String accountType);


	List<SmartCampaignObj> searchCampaignsInDataStore(List<String> accountIdList, List<Long> campaignIdList) throws DataServiceException;


	List<SmartCampaignObj> searchCampaignsInDataStore(List<String> accountIdList, String companyId) throws DataServiceException;

	QueryResponse billingReportData(String lineItemIds, QueryDTO queryDTO, String startDate, String endDate) throws Exception;

	List<SmartCampaignObj> getAllCampaignForDataUpload()
			throws DataServiceException;

	List<CensusDTO> getAllCensus();

	//Added by Anup
	List<Long> getAllLineItemIDByPartner(long campaignID, String partnerName);
	
	public SmartCampaignObj getCampaignByAdvertiserId(String advertiserId) throws DataServiceException;
	public SmartCampaignObj getCampaignByAgencyId(String agencyId) throws DataServiceException;

	List<SmartCampaignObj> loadAllCampaignsByDFPId(List<Long> dfpOrderIdList);


	
}
