package com.lin.persistance.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.api.services.bigquery.model.QueryResponse;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.ProposalObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.SitesObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.QueryDTO;


public interface IMediaPlanDAO extends IBaseDao{

	public void saveObject(Object obj) throws DataServiceException;
	public void deleteObject(Object obj) throws DataServiceException;	
	public List<ProposalObj> loadAllProposals() throws DataServiceException;
	public List<ProposalObj> loadAllProposals(String userId) throws DataServiceException;
	public ProposalObj loadProposal(long proposalId) throws DataServiceException;
	public long maxProposalId() throws DataServiceException;
	
	public List<AdvertiserObj> loadAllAdvertisers() throws DataServiceException;
	public List<AgencyObj> loadAllAgencies() throws DataServiceException;
	public List<GeoTargetsObj> loadAllGeoTargets() throws DataServiceException;
	public long maxGeoTargetId() throws DataServiceException;
	public long maxIndustryId() throws DataServiceException;
	public long maxKPIsId() throws DataServiceException;
	
	public List<PlacementObj> loadAllPlacements(long proposalId) throws DataServiceException;
	public List<PlacementObj> loadAllPlacements(String placementId) throws DataServiceException;
	public long maxSiteNumberPerPlacement(long proposalId) throws DataServiceException;
	public long maxPlacementNumberPerProposal(long proposalId) throws DataServiceException;
	public void deletePlacement(String placementId) throws DataServiceException;
	public void deletePlacementBySite(String siteId) throws DataServiceException;
	public void deleteProposal(long proposalId) throws DataServiceException;
	public void deleteAllPlacementsByProposalId(long proposalId) throws DataServiceException;
	public PlacementObj loadPlacementBySite(String siteId) throws DataServiceException;
	
	public List<IndustryObj> loadAllIndustries() throws DataServiceException;
	public List<KPIObj> loadAllKPIs() throws DataServiceException;
	
	public long maxAdSizeId() throws DataServiceException;
	public long maxAdFormatId() throws DataServiceException;
	public List<AdSizeObj> loadAllAdSize() throws DataServiceException;
	public List<AdFormatObj> loadAllAdFormats() throws DataServiceException;
	
	public SellThroughDataObj loadForecastingDataBySiteName(String startDate,String endDate,String siteName) throws DataServiceException, IOException;
	public List<DropdownDataObj> getDropdownDataObj() throws Exception;
	public SmartMediaPlanObj getSmartMediaPlanObj(long mediaPlanId) throws DataServiceException;
	
	public SmartMediaPlanObj loadMediaPlan(String campaignId) throws DataServiceException;
	public List<SmartMediaPlanObj> loadAllMediaPlans(String campaignId) throws DataServiceException;
	
	public AgencyObj loadAgency(long agencyId,String dfpNetworkCode) throws DataServiceException;
	public AgencyObj loadAgency(Long id) throws DataServiceException;
	public AgencyObj loadAgency(String name) throws DataServiceException;
	public AgencyObj loadAgency(String name,String dfpNetworkCode) throws DataServiceException;
	
	public AdvertiserObj loadAdvertiser(long advertiserId,String dfpNetworkCode) throws DataServiceException;
	public AdvertiserObj loadAdvertiser(Long id) throws DataServiceException;
	public AdvertiserObj loadAdvertiser(String name) throws DataServiceException;   
	public AccountsEntity loadAccount(String accountId) throws DataServiceException;
	public AdvertiserObj loadAdvertiser(String name,String dfpNetworkCode) throws DataServiceException;
	public SmartMediaPlanObj loadMediaPlan(String campaignId,int active) throws DataServiceException;
}
