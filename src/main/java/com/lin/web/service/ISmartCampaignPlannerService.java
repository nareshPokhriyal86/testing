package com.lin.web.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.MigrateCampaignDTO;
import com.lin.web.dto.MigratePlacementDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.util.CampaignStatusEnum;

public interface ISmartCampaignPlannerService extends IBusinessService {

	public long saveCampaign(UnifiedCampaignDTO unifiedCampaignDTO,long userId,boolean isSuperAdmin);

	UnifiedCampaignDTO initCampaign(UnifiedCampaignDTO campaignDTO, long userId);

	UnifiedCampaignDTO getAllCampaignList(UnifiedCampaignDTO campaignDTO)
			throws Exception;

	public UnifiedCampaignDTO initEditCampaign(UnifiedCampaignDTO campaignDTO,long campaignId, long userId);

	/*	boolean deleteCampaign(String campaignId) throws Exception;

	boolean unarchiveCampaign(String campaignId) throws Exception;*/

	/*boolean savePlacement(UnifiedCampaignDTO campaignDTO, long campaignId)
			throws Exception;*/

	JSONArray loadAllCampaigns(String campaignStatus, long userId,
			boolean isSuperAdmin, int campaignPerPage, int pageNumber, String searchKeyword) throws Exception;

	UnifiedCampaignDTO initEditPlacement(String campaignId, String placementId, long userId);

	UnifiedCampaignDTO initEditFlight(UnifiedCampaignDTO campaignDTO,
			long placementId);

	public UnifiedCampaignDTO loadCampaign(String campaignId);

	boolean deletePlacement(String campaignId, String placementId, long userId) throws Exception;


	AdvertiserObj addAdvertiser(String name, String address, String phone,           //shubham
			String fax, String email, String zip, long userId);

	AgencyObj addAgency(String name, String address, String phone,                      //shubham
			String fax, String email, String zip, long userId);


	public boolean updateSmartCampaignAndPlacementsAfterAdServerSetup(SmartMediaPlanObj smartMediaPlan);
	public boolean updateSmartCampaignStatus(CampaignStatusEnum campaignStatus,String campaignId);
	public SmartCampaignObj loadSmartCampaign(String campaignId);

	public boolean updateSmartCampaignFlags(CampaignStatusEnum campaignStatus,String campaignId,
			Boolean hasMediaPlan,Boolean isProcessing,Boolean isSetupOnDFP, Integer planTypeFlag);

	public void migrateCampaignFromBigQueryToDatastore(Map<String,MigrateCampaignDTO> campaignMap, String dfpNetworkName);
	public SmartCampaignPlacementObj createSmartPlacementFromMigratePlacement(SmartCampaignObj smartCampaign,
			MigratePlacementDTO migratedPlacement,long placemetMaxId);
	public List<DropdownDataObj> loadRateTypeDropDown(String campaignTypeValue);
	public AdvertiserObj saveOrUpdateAdvertiser(AdvertiserObj advertiser);
	public AgencyObj saveOrUpdateAgency(AgencyObj agency);


	public JSONObject migrateCampaign(String dfpNetworkCode, String dfpNetworkName, String publisherIdInBQ, String companyId, String companyName);
	public String getPublisherName(String publisherId);

	public String updateCampaignDetailFromDFP(String networkId);

	public String updatePlacementDetailFromDFP(String networkId);

	public String updateAllCampaignFromDFP();

	public List<SmartCampaignObj> loadAllCampaignsFromCache();
	public List<String> loadAllOrderIdsFromDatastore(String adServerNetworkCode);


	boolean changeCampaignStatus(String campaignId, String status, long userId) throws Exception;

	public JSONObject loadCampaignHistory(long campaignId, long limit, long offset) throws Exception;
	public List<SmartCampaignObj>  loadCampaignsForHistoryLoading(String adServerNetworkCode);

	SmartCampaignObj loadSmartCampaignByOrderId(String campaignId, String networkCode);

	List<CompanyObj> getAllCompanyList();

	Map<String,CompanyObj> getUniqueDFPNetworkCode(List<CompanyObj> companyList);

	boolean createOrUpdateCampaignObjFromDFP(
			Map<String, CompanyObj> uniqueDFPNetworkMap, String hours);

	CompanyObj getCompanyByDFPNetworkCode(String dfpNetworkCode);

	public JSONObject searchCampaignsJSON(List<SmartCampaignObj> smartCampaignObjs, String companyId, boolean superAdmin, String searchText, long userId) throws Exception;

	public JSONObject searchPlacementsJSON(List<SmartCampaignObj> smartCampaignObjs, String companyId, boolean superAdmin, String searchText, long userId) throws Exception;

	public List<SmartCampaignObj> loadAllCampaignsByIds(String commaSeperatedCampaignIds) throws Exception;

	List<SmartCampaignPlacementObj> loadAllPlacementsByIds(String commaSeperatedPlacementIds) throws Exception;

	JSONObject selectedPlacementsJSON(List<SmartCampaignObj> smartCampaignObjs) throws Exception;

	public JSONObject runCampaignReport(String campaignIds, String placementIds, String partners, String accounts,
			String reportType, String startDate, String endDate, String company, boolean superAdmin, String publisherBQId, long userId) throws Exception;

	CompanyObj getCompanyObjForLinMobile(String dfpNetworkCode);

	public AccountsEntity addAccount(String name, String address,String accountType,
			String phone, String fax, String email, String zip, long userId,String dfpNetworkCode,CompanyObj userCompany);
	


	boolean loadHistoricalDataForDataStoreCampaigns(String dfpNetworkCode,
			String startCount, String endCount);

	AccountsEntity addAccount(String name, String address, String phone, String fax, String email, String zip, long userId, String dfpNetworkCode , String type);

	public JSONObject billingReport(String campaignIds, String partners, String startDate, String endDate, String company, boolean isSuperAdmin, String publisherBQId, long userId);

	Map<String, List<SmartCampaignObj>> getCampaignByCompanyId(List<SmartCampaignObj> campaignObjList);

	/*
	 * Added by Anup to add Census Category
	 * */
	public String addCensusCategory(String group,String groupTxt,String gender,String bqColumn,String bqParentColumn,String bqMaleColumn,String bqFemaleColumn);

	public Workbook makeBillingExcelReport(JSONObject jsonObject);

	public List<CensusDTO> getCensusCategory();

	public boolean isPlacementNameAvailable(String campaignId, String placementNameToCheck) throws Exception;

	public boolean createCopyOfPlacement(String campaignId, long idOfPlacementToCopy, String placementName) throws Exception;
	

}
