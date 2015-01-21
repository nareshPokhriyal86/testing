package com.lin.web.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.PlacementObj;
import com.lin.web.dto.ClientIOReportDTO;
import com.lin.web.dto.PlacementDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.PublisherIOReportDTO;

/*
 * @author Youdhveer Panwar
 * IMediaPlanService Interface
 */
public interface IMediaPlanService extends IBusinessService{

	public boolean saveProposal(ProposalDTO proposalDTO, String userId);
	public List<ProposalDTO> loadAllProposals(String userId);
	
	public ProposalDTO loadProposal(long proposalId);
	public Map<String,String> getAllCompaniesByUser(long userId);
	public Map<String,String> getAllAdvertiserFromDataStore();
	public Map<String,String> getAllAgenciesFromDataStore();
	
	public long saveAdvertiser(AdvertiserObj obj);
	public long saveAgency(AgencyObj obj);
	public Map<String,String> loadAllGeoTargets();
	public long saveGeoTarget(GeoTargetsObj obj);
	public long saveIndustry(IndustryObj obj);
	public long saveKPI(KPIObj obj);
	public String savePlacements(PlacementObj obj);
	public String savePlacements(String placementData,String userId);
	public String savePlacements(String placementData,String userId, String proposalId) throws Exception;
	public Map<String,String> loadAllKPIs();
	public Map<String,String> loadAllIndustry();
	public List<String> loadSalesRepresentative(String companyId,String roleName);
	public boolean deleteProposal(long proposalId);
	public boolean deletePlacement(String placementId);
	public boolean deletePlacementSite(String siteId,long userId);
	public JSONObject placementMap(String proposalId);
	
	public long saveAdSize(AdSizeObj obj);
	public long saveAdFormat(AdFormatObj obj);
	public Map<String,String> loadAllAdSize();
	public Map<String,String> loadAllAdFormats();
	
	public Map<String,String> loadForcastingDataBySite(String startDate,String endDate,String site);
	
	public Map<String, String> getAllCampaignTypes(List<DropdownDataObj> allDropdownDataObjList);
	public Map<String, String> getAllCampaignStatus(List<DropdownDataObj> allDropdownDataObjList);
	public PublisherIOReportDTO publisherIOReportObject(String proposalId) throws Exception;
	public List<PlacementDTO> getSelectedPlacementReportList(String selectedRows, StringBuilder totalValue);
	public ClientIOReportDTO clientIOReportObject(String proposalId, long userId) throws Exception;
	//public List<ClientIOReportDTO> clientIOExcelReport(long mediaPlanId, String publisherIdInBQ) throws Exception;
	public Map POExcelReport(long mediaPlanId, String publisherIdInBQ) throws Exception;
	Map clientIOExcelReport(long mediaPlanId, String publisherIdInBQ) throws Exception;
	
	public AdvertiserObj loadAdvertiserFromDatastore(String name,long id, long advertiserId,String dfpNetworkCode);
	public AgencyObj loadAgencyFromDatastore(String name,long id, long agencyId,String dfpNetworkCode);
}
