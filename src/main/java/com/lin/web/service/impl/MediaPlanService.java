package com.lin.web.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.PlacementHistoryObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.ProposalHistoryObj;
import com.lin.server.bean.ProposalObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.CampaignDetailDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ClientIOReportDTO;
import com.lin.web.dto.JavaScriptTagDTO;
import com.lin.web.dto.PlacementDTO;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.PublisherIOReportDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.IMediaPlanService;
import com.lin.web.service.IQueryService;
import com.lin.web.service.IUserService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;

/*
 * @author Youdhveer Panwar
 * Implementation of IMediaPlanService interface
 */
public class MediaPlanService implements IMediaPlanService{

	private static final Logger log = Logger.getLogger(MediaPlanService.class.getName()); 
	
	public QueryDTO getQueryDTO(String publisherIdInBQ, String schema) {
		log.info("schema.."+schema);
		IQueryService queryService = (IQueryService) BusinessServiceLocator.locate(IQueryService.class);
		String currentDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		
		QueryDTO queryDTO = queryService.createQueryFromClause(publisherIdInBQ, "2013-01-01", currentDate, schema);
		if(queryDTO != null && !queryDTO.getQueryData().isEmpty()) {
			log.info("queryDTO.getQueryData() : "+queryDTO.getQueryData());
		}
		return queryDTO;
	}

	/* 
	 * Save a proposal
	 * @param ProposalDTO
	 * @see com.lin.web.service.IMediaPlanService#saveProposal(com.lin.web.dto.ProposalDTO)
	 */
	public boolean saveProposal(ProposalDTO proposalDTO, String userId){
		
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			long proposalId;
			if(proposalDTO.getProposalId() == null || proposalDTO.getProposalId().trim().length()== 0){
				long maxProposalId=mediaPlanDAO.maxProposalId();				
				proposalId=(maxProposalId+1);
				log.info("maxProposalId exist in datastore: "+maxProposalId+" : so, save new proposal with Id:"+proposalId);
			}else{
				proposalId=StringUtil.getLongValue(proposalDTO.getProposalId());
				log.info("Update proposal with proposalId:"+proposalId);
			}
			proposalDTO.setProposalId(proposalId+"");
			String id=proposalId+"";
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			ProposalObj proposal=new ProposalObj(id,proposalId,proposalDTO.getCompany(),
					proposalDTO.getProposalName(), 
					proposalDTO.getAdvertiser(),proposalDTO.getAgency(), proposalDTO.getSalesRep(), 
					proposalDTO.getIndustry(), proposalDTO.getProposalType(),proposalDTO.getProposalStatus(),
					StringUtil.getUnformattedDoubleValueForMediaPlan(proposalDTO.getBudget()), proposalDTO.getFlightStartDate(), 
					proposalDTO.getFlightEndDate(), proposalDTO.getKpi(), createdOn, createdOn,
					proposalDTO.getUpdatedBy(),proposalDTO.getUpdatedBy(),proposalDTO.getSalesContact(),
					proposalDTO.getSalesEmail(),proposalDTO.getSalesPhone(),proposalDTO.getTrafficContact(),
					proposalDTO.getTrafficEmail(),proposalDTO.getTrafficPhone(), proposalDTO.getGeoTargets());
			mediaPlanDAO.saveObject(proposal);
			
			ProposalHistoryObj proposalHistory=new ProposalHistoryObj(proposalId,proposalDTO.getCompany(),
					proposalDTO.getProposalName(), 
					proposalDTO.getAdvertiser(),proposalDTO.getAgency(), proposalDTO.getSalesRep(), 
					proposalDTO.getIndustry(), proposalDTO.getProposalType(),proposalDTO.getProposalStatus(),
					StringUtil.getUnformattedDoubleValueForMediaPlan(proposalDTO.getBudget()), proposalDTO.getFlightStartDate(), 
					proposalDTO.getFlightEndDate(), proposalDTO.getKpi(), createdOn, createdOn,
					proposalDTO.getUpdatedBy(),proposalDTO.getUpdatedBy(),proposalDTO.getSalesContact(),
					proposalDTO.getSalesEmail(),proposalDTO.getSalesPhone(),proposalDTO.getTrafficContact(),
					proposalDTO.getTrafficEmail(),proposalDTO.getTrafficPhone(), proposalDTO.getGeoTargets());
			proposalHistory.setActiveStatus("1");
			mediaPlanDAO.saveObject(proposalHistory);
			
			String placementData = proposalDTO.getPlacementData();
			if(placementData != null && placementData.trim().length() > 0) {
				placementData = placementData.trim();
				log.info("Going to save placementData : "+proposalDTO.getPlacementData());
				if(placementData.lastIndexOf(",") != -1 && (placementData.lastIndexOf(",") == (placementData.length() - 1))){
					placementData = placementData.substring(0, placementData.lastIndexOf(","));
					log.info("after removing last ',' :"+placementData);
				}
				savePlacements(placementData, userId, String.valueOf(proposalId));
			}
			
			log.info("Also add this proposal in memcache..");
			
			proposalDTO.setProposalId(String.valueOf(proposalId));
			String updateOn=DateUtil.getFormatedDate(createdOn, "yyyy-MM-dd HH:mm:ss", "MM-dd-yyyy");						
			proposalDTO.setUpdatedOn(updateOn);
			MemcacheUtil.setProposalInCache(proposalDTO);
			
			List<ProposalObj> proposalList=MemcacheUtil.getAllProposalsFromCache(proposal.getUpdatedBy());
			if(proposalList !=null && proposalList.size()>0){
				int found=proposalList.indexOf(proposal);				
				if(found>=0){
					proposalList.add(found, proposal);
					proposalList.remove(found+1);					
				}else{
					proposalList.add(proposal);
				}
				
			}else{
				proposalList=new ArrayList<ProposalObj>();
				proposalList.add(proposal);
			}
			log.info("save in cache:proposalList:"+proposalList.size());
			MemcacheUtil.setAllProposalsInCache(proposalList, proposalDTO.getUpdatedBy());
			//log.info("set in cache:"+proposalDTO.toString());
			return true;
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
			return false;
		}
		catch (Exception e) {
		log.severe("Exception : "+e.getMessage());
		
		return false;
	}
	}
	
	/*
	 * Load all proposals
	 * @see com.lin.web.service.IMediaPlanService#loadAllProposals(java.lang.String)
	 * @param String
	 */
	public List<ProposalDTO> loadAllProposals(String userId){
		log.info("Load all proposals for userId:"+userId);
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		List<DropdownDataObj> allDropdownDataObjList = MemcacheUtil.getAllDropdownDataObjList();
		List<ProposalDTO> proposalDTOList=new ArrayList<ProposalDTO>();
		Map<String,String> industryMap=loadAllIndustry();
		/*Map<String,String> advertiserMap=getAllAdvertiserFromDataStore();
		Map<String,String> agencyMap=getAllAgenciesFromDataStore();*/
		Map<String,String> advertiserMap = new LinkedHashMap<String, String>();
		Map<String,String> agencyMap = new LinkedHashMap<String, String>();
		Map<String,String> campaignTypeMap = getAllCampaignTypes(allDropdownDataObjList);
		Map<String,String> campaignStatusMap = getAllCampaignStatus(allDropdownDataObjList);
		Map<String,String> companyMap = getAllCompaniesByUser(StringUtil.getLongValue(userId));
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			List<ProposalObj> resultList=MemcacheUtil.getAllProposalsFromCache(userId);
			if(resultList==null || resultList.size()==0){
				resultList=mediaPlanDAO.loadAllProposals(userId);
				MemcacheUtil.setAllProposalsInCache(resultList, userId);
			}
			log.info("resultList:"+resultList.size());
			for(ProposalObj obj:resultList){
				ProposalDTO dtoObj=new ProposalDTO();
				/*String publisherIdsForBigQuery = userService.getPublisherIdsForBigQueryByCompanyId(obj.getCompanyId());
				
				if(publisherIdsForBigQuery != null && publisherIdsForBigQuery.trim().length() > 0) {
					StringBuilder publisherIds=new StringBuilder(publisherIdsForBigQuery);
					StringBuilder advertiserIds=new StringBuilder();
					StringBuilder agencyIds=new StringBuilder();
					
					userDetailsDAO.getAccountsInfoForBQ(StringUtil.getLongValue(userId), publisherIds, advertiserIds, agencyIds);
					
					advertiserMap.putAll(userService.loadAdvertisersFromBigQuery(publisherIdsForBigQuery, advertiserIds.toString()));
					agencyMap.putAll(userService.loadAgenciesFromBigQuery(publisherIdsForBigQuery, agencyIds.toString()));
				}*/
				advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(Long.valueOf(userId), obj.getCompanyId(), true, false);
				agencyMap = userService.getSelectedAccountsForCampaingnPlanning(Long.valueOf(userId), obj.getCompanyId(), false, true);
				dtoObj.setProposalId(obj.getProposalId()+"");
				dtoObj.setAdvertiser(advertiserMap.get("_"+obj.getAdvertiser()));
				//dtoObj.setAdvertiser(obj.getAdvertiser());
				//
				dtoObj.setAdvertiserId(obj.getAdvertiser());
				dtoObj.setAgency(agencyMap.get("_"+obj.getAgency()));
				//dtoObj.setAgency(obj.getAgency());
				//
				dtoObj.setAgencyId(obj.getAgency());
				dtoObj.setBudget(obj.getBudget()+"");
				dtoObj.setCompany(companyMap.get(obj.getCompanyId()));
				dtoObj.setFlightEndDate(obj.getFlightEndDate());
				dtoObj.setFlightStartDate(obj.getFlightStartDate());
				String CommaSeparatedIndustry = "";
				if(obj.getIndustry() != null && obj.getIndustry().trim().length() > 0) {
					String[] industry = obj.getIndustry().trim().split(",");
					for (int i = 0; i < industry.length; i++) {
						String str = industry[i].trim();
						CommaSeparatedIndustry = CommaSeparatedIndustry + industryMap.get(str) + ", ";
					}
				}
				if(CommaSeparatedIndustry.lastIndexOf(",") != -1) {
					CommaSeparatedIndustry = CommaSeparatedIndustry.substring(0, CommaSeparatedIndustry.lastIndexOf(","));
				}
				dtoObj.setIndustry(CommaSeparatedIndustry);
				dtoObj.setKpi(obj.getKpi());
				dtoObj.setProposalName(obj.getName());
				dtoObj.setProposalStatus(campaignStatusMap.get(obj.getStatus()));
				dtoObj.setProposalType(campaignTypeMap.get(obj.getType()));
				dtoObj.setSalesRep(obj.getSalesRep());
				dtoObj.setUpdatedBy(obj.getUpdatedBy());
				String updateOn=obj.getUpdatedOn();
				if(updateOn !=null){
					updateOn=DateUtil.getFormatedDate(updateOn, "yyyy-MM-dd HH:mm:ss", "MM-dd-yyyy");
				}
				dtoObj.setUpdatedOn(updateOn);
				dtoObj.setSalesContact(obj.getSalesContact());
				dtoObj.setSalesEmail(obj.getSalesEmail());
				dtoObj.setSalesPhone(obj.getSalesPhone());
				dtoObj.setTrafficContact(obj.getTrafficContact());
				dtoObj.setTrafficEmail(obj.getTrafficEmail());
				dtoObj.setTrafficPhone(obj.getTrafficPhone());
				dtoObj.setGeoTargets(obj.getGeoTargets());				
				proposalDTOList.add(dtoObj);
			}
						
			
		} catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
			
		}
		
		return proposalDTOList;
	}
	
	/*
	 * Load all companies by logged in userId
	 * @param long userId
	 * @return List<String> companyList
	 */
	public Map<String,String> getAllCompaniesByUser(long userId){
		log.info("getAllCompaniesByUser :userId :"+userId);
		List<CompanyObj> companyList=new ArrayList<CompanyObj>();
		Map<String,String> companyMap=new HashMap<String,String>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		try {
			companyList = userDetailsDAO.getSelectedCompaniesByUserId(userId);
			if(companyList != null){
				log.info("loaded companyList : "+companyList.size()+"\n Now Loading company names..");
				for(CompanyObj companyObj:companyList){
					companyMap.put(companyObj.getId()+"",companyObj.getCompanyName().trim());
				}
			}else{
				log.info("loaded companyList : 0");
			}
		} catch (Exception e) {
			log.severe("getAllCompaniesByUser : Exception:"+e.getMessage());
			
		}
		log.info("Returning companyMap:"+companyMap.size());
		return companyMap;
	}
	
	/*
	 * Load proposal based on proposalId
	 * @see com.lin.web.service.IMediaPlanService#loadProposal(java.lang.Long)
	 * @param long proposalId
	 * @return ProposalDTO
	 */ 
	public ProposalDTO loadProposal(long proposalId){
		log.info("Load proposal for proposalId:"+proposalId);
		DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
		ProposalDTO proposalDTO=MemcacheUtil.getProposalFromCache(String.valueOf(proposalId));
		if(proposalDTO ==null){
			log.info("Not found in memcache, load from datastore..");
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				ProposalObj proposal=mediaPlanDAO.loadProposal(proposalId);
				
				if(proposal !=null ){
					log.info("proposal loaded..");
					proposalDTO=new ProposalDTO();
					proposalDTO.setCompany(proposal.getCompanyId());
					proposalDTO.setProposalId(proposal.getProposalId()+"");
					proposalDTO.setAdvertiser(proposal.getAdvertiser());
					proposalDTO.setAgency(proposal.getAgency());
					proposalDTO.setAdvertiserId(proposal.getAdvertiser());
					proposalDTO.setAgencyId(proposal.getAgency());
					
					proposalDTO.setBudget("$"+df.format(proposal.getBudget()));				
					proposalDTO.setFlightEndDate(proposal.getFlightEndDate());
					proposalDTO.setFlightStartDate(proposal.getFlightStartDate());
					proposalDTO.setIndustry(proposal.getIndustry());
					proposalDTO.setKpi(proposal.getKpi());
					proposalDTO.setProposalName(proposal.getName());
					proposalDTO.setProposalStatus(proposal.getStatus());
					proposalDTO.setProposalType(proposal.getType());
					proposalDTO.setSalesRep(proposal.getSalesRep());
					proposalDTO.setUpdatedBy(proposal.getUpdatedBy());
					String updateOn=proposal.getUpdatedOn();
					if(updateOn !=null){
						updateOn=DateUtil.getFormatedDate(updateOn, "yyyy-MM-dd HH:mm:ss", "MM-dd-yyyy");
					}				
					proposalDTO.setUpdatedOn(updateOn);
					
					proposalDTO.setSalesContact(proposal.getSalesContact());
					proposalDTO.setSalesEmail(proposal.getSalesEmail());
					proposalDTO.setSalesPhone(proposal.getSalesPhone());
					proposalDTO.setTrafficContact(proposal.getTrafficContact());
					proposalDTO.setTrafficEmail(proposal.getTrafficEmail());
					proposalDTO.setTrafficPhone(proposal.getTrafficPhone());
					proposalDTO.setGeoTargets(proposal.getGeoTargets());
					
					proposalDTO.setUpdatedOn(updateOn);
					log.info("Also set this proposal in memcache..proposalId:"+proposalId);
					MemcacheUtil.setProposalInCache(proposalDTO);
					
				}else{
					log.info("Proposal not found.");
				}
				
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
		}
		
		return proposalDTO;
	}
	
	
	/*
	 * Load all advertisers 
	 * 
	 * @return Map<String,String>
	 */
	public Map<String,String> getAllAdvertiserFromDataStore(){
		log.info("getAllAdvertiserFromDataStore ..");
		List<AdvertiserObj> advertiserList=new ArrayList<AdvertiserObj>();
		Map<String,String> advertiserMap=MemcacheUtil.getAdvertisersFromCache();
		if(advertiserMap == null || advertiserMap.size()==0){
			advertiserMap=new HashMap<String,String>();
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				advertiserList=mediaPlanDAO.loadAllAdvertisers();
				if(advertiserList !=null && advertiserList.size()>0){
					log.info("loaded advertiserList : "+advertiserList.size());	
					for(AdvertiserObj obj:advertiserList){
						String key=obj.getId()+"";
						String value=obj.getName();
						advertiserMap.put(key,value);
					}
					log.info("Also set advertisers in memcache...");
					MemcacheUtil.setAdvertisersInCache(advertiserMap);
					
				}else{
					log.info("loaded advertiserList : 0");
				}
				
			} catch (Exception e) {
				log.severe("getAllAdvertiserFromDataStore : Exception:"+e.getMessage());
				
			}
		}
		
		log.info("Returning advertiserMap:"+advertiserMap.size());
		return advertiserMap;
	}
	
	
	/*
	 * Load all agencies 
	 * 
	 * @return Map<String,String>
	 */
	public Map<String,String> getAllAgenciesFromDataStore(){
		log.info("getAllAgenciesFromDataStore ..");
		List<AgencyObj> agencyList=new ArrayList<AgencyObj>();
		Map<String,String> agencyMap=MemcacheUtil.getAgenciesFromCache();
		if(agencyMap ==null || agencyMap.size()==0){
			agencyMap=new HashMap<String,String>();
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				agencyList=mediaPlanDAO.loadAllAgencies();
				if(agencyList !=null && agencyList.size()>0){
					log.info("loaded agencyList : "+agencyList.size());	
					for(AgencyObj obj:agencyList){
						String key=obj.getId()+"";
						String value=obj.getName();
						agencyMap.put(key,value);
					}	
					log.info("set all agencies in memcache...");
					MemcacheUtil.setAgenciesInCache(agencyMap);
				}else{
					log.info("loaded agencyList : 0");
				}
				
			} catch (Exception e) {
				log.severe("getAllAgenciesFromDataStore : Exception:"+e.getMessage());
				
			}
		}
		
		log.info("Returning agencyMap:"+agencyMap.size());
		return agencyMap;
	}
	
	
	
	/* 
	 * Save an advertiser
	 * @param AdvertiserObj
	 * @return long
	 */
	public long saveAdvertiser(AdvertiserObj obj){
		log.info("Going to save advertiser");
		long id=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);		
			id=obj.getId();
			log.info("add new advertiser in memcache.. Id:"+id);
			Map<String,String> advertiserMap=MemcacheUtil.getAdvertisersFromCache();
			if(advertiserMap ==null){
				advertiserMap=new HashMap<String,String>();
			}
			if(id >0){
				advertiserMap.put(id+"", obj.getName());
				MemcacheUtil.setAdvertisersInCache(advertiserMap);
			}			
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return id;
	}
	
	
	/* 
	 * Save an agency
	 * @param AgenciesObj
	 * @return long
	 */
	public long saveAgency(AgencyObj obj){
		log.info("Going to save agency");
		long id=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);
			id=obj.getId();
			log.info("After saving agency ..datastore generated Id:"+id);
			Map<String,String> agencyMap=MemcacheUtil.getAgenciesFromCache();
			if(agencyMap ==null){
				agencyMap=new HashMap<String,String>();
			}
			if(id != 0){
				agencyMap.put(id+"", obj.getName());
				MemcacheUtil.setAgenciesInCache(agencyMap);
			}
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}
		return id;
	}
	
	public AgencyObj loadAgencyFromDatastore(String name,long id, long agencyId,String dfpNetworkCode){
		log.info("Load agency....name:"+name+", dfpNetworkCode:"+dfpNetworkCode+", id:"+id+" , agencyId:"+agencyId);
		AgencyObj agencyObj=null;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();		
		try {
			if(id >0){
				agencyObj=mediaPlanDAO.loadAgency(id);
			}else if(agencyId >0 && dfpNetworkCode !=null){
				agencyObj=mediaPlanDAO.loadAgency(agencyId, dfpNetworkCode);
			}else if(name !=null && dfpNetworkCode !=null){
				agencyObj=mediaPlanDAO.loadAgency(name,dfpNetworkCode);
			}else if(name !=null){
				agencyObj=mediaPlanDAO.loadAgency(name);
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}
		return agencyObj;
	}
	
	public AdvertiserObj loadAdvertiserFromDatastore(String name,long id, long advertiserId,String dfpNetworkCode){
		log.info("Load advertiser....name:"+name+", dfpNetworkCode:"+dfpNetworkCode+", id:"+id+" , advertiserId:"+advertiserId);
		AdvertiserObj advertiserObj=null;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();		
		try {
			if(id >0){
				advertiserObj=mediaPlanDAO.loadAdvertiser(id);
			}else if(advertiserId >0 && dfpNetworkCode !=null){
				advertiserObj=mediaPlanDAO.loadAdvertiser(advertiserId, dfpNetworkCode);
			}else if(name !=null && dfpNetworkCode !=null){
				advertiserObj=mediaPlanDAO.loadAdvertiser(name,dfpNetworkCode);
			}else if(name !=null){
				advertiserObj=mediaPlanDAO.loadAdvertiser(name);
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}
		return advertiserObj;
	}
	
	/*
     * Get all geo targets(DMA from memcache)
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public Map<String,String> loadAllGeoTargets(){
		log.info("loadAllGeoTargets..");
		
    	Map<String,String> geoTargetMap=MemcacheUtil.getGeoTargetsFromCache();
    	if(geoTargetMap==null){
    		geoTargetMap=new LinkedHashMap<String, String>();
    		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
    		try {
    			List<GeoTargetsObj> resultList=mediaPlanDAO.loadAllGeoTargets();
				if(resultList!=null && resultList.size()>0){
					log.info("resultList :"+resultList.size()+" : Put in memcache also..");
					for(GeoTargetsObj obj:resultList){
						String key=String.valueOf(obj.getGeoTargetId());
						String value=obj.getGeoTargetsName();
						geoTargetMap.put(key, value);
					}
					MemcacheUtil.setGeoTargetsInCache(geoTargetMap);
				}else{
					log.info("No geo targets found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
    	}
    	return geoTargetMap;
	}
    
    
    /* 
	 * Save a GeoTargets(DMA)
	 * @param GeoTargetsObj
	 * @return long
	 */
	public long saveGeoTarget(GeoTargetsObj obj){
		log.info("Going to save agency");
		long geoId=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {			
			if(obj.getGeoTargetId() == 0 ){
				long maxId=mediaPlanDAO.maxGeoTargetId();				
				geoId=(maxId+1);
				log.info("maxId exist in datastore: "+maxId+" : so, save new GeoTargets with Id:"+geoId);
			}else{
				geoId=obj.getGeoTargetId();
				log.info("Update GeoTargets with geoId:"+geoId);
			}				
			//obj.setId(geoId);
			obj.setGeoTargetId(geoId);			
			mediaPlanDAO.saveObject(obj);
			Map<String,String> geoTargetMap=MemcacheUtil.getGeoTargetsFromCache();
			if(geoTargetMap ==null ){
				geoTargetMap=new LinkedHashMap<String, String>();
			}
			geoTargetMap.put(String.valueOf(geoId), obj.getGeoTargetsName());
			MemcacheUtil.setGeoTargetsInCache(geoTargetMap);
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return geoId;
	}
	
	
	/*
     * Get all industryMap
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public Map<String,String> loadAllIndustry(){
		log.info("loadAllIndustry..");
		
    	Map<String,String> industryMap=MemcacheUtil.getIndustriesFromCache();
    	if(industryMap==null){
    		industryMap=new LinkedHashMap<String, String>();
    		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
    		try {
    			List<IndustryObj> resultList=mediaPlanDAO.loadAllIndustries();
				if(resultList!=null && resultList.size()>0){
					log.info("resultList :"+resultList.size()+" : Put in memcache also..");
					for(IndustryObj obj:resultList){
						String key=String.valueOf(obj.getIndustryId());
						String value=obj.getIndustryName();
						industryMap.put(key, value);
					}
					MemcacheUtil.setIndustriesInCache(industryMap);
				}else{
					log.info("No industry found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
    	}
    	return industryMap;
	}
    
    
    /*
     * Get loadAllKPIs
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public Map<String,String> loadAllKPIs(){
		log.info("loadAllKPIs..");
		
    	Map<String,String> kpiMap=MemcacheUtil.getKPIsFromCache();
    	if(kpiMap==null){
    		kpiMap=new LinkedHashMap<String, String>();
    		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
    		try {
    			List<KPIObj> resultList=mediaPlanDAO.loadAllKPIs();
				if(resultList!=null && resultList.size()>0){
					log.info("resultList :"+resultList.size()+" : Put in memcache also..");
					for(KPIObj obj:resultList){
						String key=String.valueOf(obj.getKpiId());
						String value=obj.getKpiName();
						kpiMap.put(key, value);
					}
					MemcacheUtil.setKPIsInCache(kpiMap);
				}else{
					log.info("No KPI found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
    	}
    	return kpiMap;
	}
    
    
	/* 
	 * Save a Industry for a proposal
	 * @param IndustryObj
	 * @return long
	 */
	public long saveIndustry(IndustryObj obj){
		log.info("Going to save industry");
		long industryId=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {			
			if(obj.getIndustryId() == 0 ){
				long maxId=mediaPlanDAO.maxIndustryId();
				industryId=maxId+1;
				log.info("maxId exist in datastore: "+maxId+" : so, save new industry with Id:"+industryId);
			}else{
				industryId=obj.getIndustryId();
				log.info("Update industry with industryId:"+industryId);
			}			
			
			obj.setIndustryId(industryId);			
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);
			Map<String,String> industryMap=MemcacheUtil.getIndustriesFromCache();
			if(industryMap ==null ){
				industryMap=new LinkedHashMap<String, String>();
			}
			industryMap.put(String.valueOf(industryId), obj.getIndustryName());
			MemcacheUtil.setIndustriesInCache(industryMap);
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return industryId;
	}
	
	/* 
	 * Save a KPI
	 * @param KPIObj
	 * @return long
	 */
	public long saveKPI(KPIObj obj){
		log.info("Going to save kpi");
		long kpiId=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {			
			if(obj.getKpiId() == 0 ){
				long maxId=mediaPlanDAO.maxKPIsId();				
				kpiId=(maxId+1);
				log.info("maxId exist in datastore: "+maxId+" : so, save new KPI with Id:"+kpiId);
			}else{
				kpiId=obj.getKpiId();
				log.info("Update KPI with kpiId:"+kpiId);
			}				
			
			obj.setKpiId(kpiId);		
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);
			Map<String,String> kpiMap=MemcacheUtil.getKPIsFromCache();
			if(kpiMap ==null ){
				kpiMap=new LinkedHashMap<String, String>();
			}
			kpiMap.put(String.valueOf(kpiId), obj.getKpiName());
			MemcacheUtil.setKPIsInCache(kpiMap);
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return kpiId;
	}
	
	/* 
	 * Save a placement
	 * @param PlacementObj
	 * @return String siteId
	 */
	public String savePlacements(PlacementObj obj){
		
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {		
			long proposalId=obj.getProposalId();
			String placementId=obj.getPlacementId();
			String siteId = obj.getSiteId();
			log.info("Going to save placement for proposalId :"+proposalId+" and placementId:"+placementId);
			String updatedOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			
			if(placementId==null || (placementId.trim().length()==0) ){
				long maxPlacementNum=mediaPlanDAO.maxPlacementNumberPerProposal(proposalId);
				long maxSiteNum=mediaPlanDAO.maxSiteNumberPerPlacement(proposalId);
				log.info("placementId is null, available maxSiteNum : "+maxSiteNum+" and maxPlacementNum:"+maxPlacementNum);				
				placementId=proposalId+"_"+(maxPlacementNum+1);
				
				siteId=placementId+"_"+(maxSiteNum+1);
				obj.setPlacementId(placementId);
				obj.setSiteId(siteId);
				obj.setCreatedBy(obj.getUpdatedBy());
				obj.setCreatedOn(updatedOn);
				obj.setUpdatedOn(updatedOn);
				obj.setSiteNumberPerPlacement(maxSiteNum+1);
				obj.setPlacementNumber(maxPlacementNum+1);
				log.info("Save new placement data with placementId "+placementId+" with site having siteId "+siteId);
			}else{
				log.info("Placement already exist, add/update site for placementId:"+placementId);
				if(siteId==null || (siteId.trim().length()==0)){
					long maxSiteNum=mediaPlanDAO.maxSiteNumberPerPlacement(proposalId);
					long maxPlacementNum=mediaPlanDAO.maxPlacementNumberPerProposal(proposalId);
					log.info("SiteId is null, so get maxSiteNum from datastore: "+maxSiteNum+" and maxPlacementNum:"+maxPlacementNum);
					siteId=placementId+"_"+(maxSiteNum+1);
					obj.setSiteId(siteId);
					obj.setCreatedBy(obj.getUpdatedBy());
					obj.setCreatedOn(updatedOn);
					obj.setUpdatedOn(updatedOn);
					obj.setSiteNumberPerPlacement(maxSiteNum+1);
					obj.setPlacementNumber(maxPlacementNum);
					log.info("Add new site with siteId:"+siteId);
				}else{	
					PlacementObj existingObj=mediaPlanDAO.loadPlacementBySite(siteId);
					obj.setCreatedBy(existingObj.getCreatedBy());
					obj.setCreatedOn(existingObj.getCreatedOn());
					obj.setUpdatedOn(updatedOn);
					obj.setSiteNumberPerPlacement(existingObj.getSiteNumberPerPlacement());
					log.info("Site already exist, update placement for this siteId:"+siteId);
				}
			}
			obj.setId(siteId);
			log.info("Going to save this placement-->site with siteId:"+siteId);
			mediaPlanDAO.saveObject(obj);	
			PlacementHistoryObj placementHistoryObj=new PlacementHistoryObj(obj.getProposalId(), obj.getPlacementId(), 
					obj.getPlacementName(), obj.getCreatedOn(), obj.getUpdatedOn(), obj.getCreatedBy(), obj.getUpdatedBy(),
					obj.getSiteId(), obj.getSiteName(), obj.getAdSize(), obj.getAdFormat(), obj.getAdServer(), obj.getPublisherCPM(),
					obj.getFirstPartyAdServerCost(), obj.getThirdPartyAdServerCost(), obj.getTotalCost(), obj.getMarginPercent(),
					obj.getMargin(), obj.getPriceQuote(), obj.getBudgetAllocation(), obj.getPublisherPayout(), obj.getGrossRevenue(),
					obj.getNetRevenue(), obj.getForcastedImpression(), obj.getReservedImpression(), obj.getAvailableImpression(),
					obj.getProposedImpression(), obj.getSiteNumberPerPlacement(), obj.getPlacementNumber());
			placementHistoryObj.setActiveStatus("1");
			mediaPlanDAO.saveObject(placementHistoryObj);	
			return siteId;
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
			return null;
		}		
	}
	
	/* 
	 * Save all placements from json string
	 * @param String - placementData (Json format), 
	 *        String - userId
	 * @return String - placementData (Json format)
	 */
	public String savePlacements(String placementData,String userId){
		
		JSONObject placementDetails = (JSONObject) JSONSerializer.toJSON(placementData);			
		
        JSONArray jsonArray=placementDetails.getJSONArray("placement");
        if(jsonArray.size()==0){
        	log.warning("No placement to save..");        	
        }else{        	      	
        	for(int i=0;i<jsonArray.size();i++){
    			JSONObject obj=jsonArray.getJSONObject(i);
    			String proposalId=obj.getString("proposalId");
    			String placementId=obj.getString("placementId");
    			String placementName=obj.getString("placementName");
    			String createdBy=obj.getString("createdBy");
    			String createdOn=obj.getString("createdOn");
    			String updatedBy=obj.getString("updatedBy");
    			String updatedOn=obj.getString("updatedOn");
    			JSONArray siteJsonArray=obj.getJSONArray("site");
    			if(siteJsonArray.size()==0){
    	        	log.warning("No site to save..");        
    			}else{
    				for(int j=0;j<siteJsonArray.size();j++){
    					JSONObject siteObj=siteJsonArray.getJSONObject(j);
    	    			String siteId=siteObj.getString("siteId");
    	    			String siteName=siteObj.getString("siteName");
    	    			String adSize=siteObj.getString("adSize");
    	    			String customAdSize=siteObj.getString("customAdSize");
    	    			String adFormat=siteObj.getString("adFormat");
    	    			String customAdFormat=siteObj.getString("customAdFormat");
    	    			String adServer=siteObj.getString("adServer");
    	    			String publisherCPM=siteObj.getString("publisherCPM");
    	    			String publisherPayOut=siteObj.getString("publisherPayOut");
    	    			String firstPartyAdServerCost=siteObj.getString("firstPartyAdServerCost");
    	    			String thirdPartyAdServerCost=siteObj.getString("thirdPartyAdServerCost");
    	    			String totalCost=siteObj.getString("totalCost");
    	    			String marginPercent=siteObj.getString("marginPercent");
    	    			String margin=siteObj.getString("margin");
    	    			String priceQuote=siteObj.getString("priceQuote");
    	    			String budgetAllocation=siteObj.getString("budgetAllocation");
    	    			String grossRevenue=siteObj.getString("grossRevenue");
    	    			String netRevenue=siteObj.getString("netRevenue");
    	    			String proposedImpression=siteObj.getString("proposedImpression");
    	    			String forcastedImpression=siteObj.getString("forcastedImpression");
    	    			String reservedImpression=siteObj.getString("reservedImpression");
    	    			String availableImpression=siteObj.getString("availableImpression");
    	    			
    	    			String id=proposalId+"_"+siteId;
    	    			PlacementObj placementObj=new PlacementObj(id, StringUtil.getLongValue(proposalId), placementId, 
    	    					placementName,null, null, null, userId, siteId, siteName, adSize, adFormat, adServer,
    	    					StringUtil.getDoubleValue(publisherCPM), StringUtil.getDoubleValue(firstPartyAdServerCost),
    	    					StringUtil.getDoubleValue(thirdPartyAdServerCost),StringUtil.getDoubleValue(totalCost), 
    	    					StringUtil.getDoubleValue(marginPercent), StringUtil.getDoubleValue(margin),StringUtil.getDoubleValue( priceQuote),
    	    					StringUtil.getDoubleValue(budgetAllocation), StringUtil.getDoubleValue(publisherPayOut),
    	    					StringUtil.getDoubleValue(grossRevenue), StringUtil.getDoubleValue(netRevenue), 
    	    					StringUtil.getLongValue(forcastedImpression), StringUtil.getLongValue(reservedImpression), 
    	    					StringUtil.getLongValue(availableImpression), StringUtil.getLongValue(proposedImpression));
    	    			log.info("get placement obj from json :"+placementObj.toString());
    	    			siteId=savePlacements(placementObj);
    	    			placementId=siteId.substring(0, siteId.lastIndexOf("_"));
    	    			log.info("Saved site -- siteId:"+siteId);
    	    			
    	    			if(customAdSize !=null && customAdSize.trim().length()>0){
    	    				AdSizeObj adSizeObj=new AdSizeObj(customAdSize,userId);
    	    				saveAdSize(adSizeObj);
    	    			}
    	    			if(customAdFormat !=null && customAdFormat.trim().length()>0){
    	    				AdFormatObj adFormatObj=new AdFormatObj(customAdFormat,userId);
    	    				saveAdFormat(adFormatObj);
    	    			}
    	    			
    				}
    			}
        	}
        }
        
		return null;
	}
	
public String savePlacements(String placementData,String userId, String proposalId) throws Exception{
	try {
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		Map<String, PlacementObj> placementMap = new LinkedHashMap<String, PlacementObj>();
		String updatedOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
		JSONObject placementDetails = (JSONObject) JSONSerializer.toJSON(placementData);	
		List<PlacementObj> placementObjList = new ArrayList<PlacementObj>();
		
        JSONArray jsonArray=placementDetails.getJSONArray("placement");
        if(jsonArray.size()==0){
        	log.warning("No placement to save..");        	
        }else{
        	List<PlacementObj> placementList = mediaPlanDAO.loadAllPlacements(StringUtil.getLongValue(proposalId));
        	mediaPlanDAO.deleteAllPlacementsByProposalId(StringUtil.getLongValue(proposalId));
        	if(placementList != null && placementList.size() > 0) {
        		for (PlacementObj placementObj : placementList) {
        			placementMap.put(placementObj.getId(), placementObj);
				}
        	}
        	for(int i=0;i<jsonArray.size();i++){
    			JSONObject obj=jsonArray.getJSONObject(i);
    			String placementName=obj.getString("placementName");
    			String site=obj.getString("site");
    			
    			String placementId = obj.getString("placementId");
    			if(placementId == null || placementId.trim().length() == 0) {
    				// new Entry
    				placementId = proposalId+"_"+placementName+"_"+site+"_"+(DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss.SSS").replaceAll(" ", ""));
    			}
    			else {
    				maintainPlacementHistory(placementMap.get(placementId), userId);
    			}
    			String createdBy=obj.getString("createdBy");
    			if(createdBy == null || createdBy.trim().length() == 0) {
    				createdBy = userId;
    			}
    			String createdOn=obj.getString("createdOn");
    			if(createdOn == null || createdOn.trim().length() == 0) {
    				createdOn = updatedOn;
    			}
    			String updatedBy = userId;
    			String publisherCPM=obj.getString("publisherCPM");
    			String budgetAllocation=obj.getString("budgetAllocation");
    			String proposedImpression=obj.getString("proposedImpression");
    			String marginPercent=obj.getString("marginPercent");
    			String effectiveCPM=obj.getString("effectiveCPM");
    			String firstPartyAdServerCost=obj.getString("firstPartyAdServerCost");
    			String thirdPartyAdServerCost=obj.getString("thirdPartyAdServerCost");
    			String netCostCPM=obj.getString("netCostCPM");
    			String netCost=obj.getString("netCost");
    			String grossRevenue=obj.getString("grossRevenue");
    			String publisherPayout=obj.getString("publisherPayout");
    			String servingFees=obj.getString("servingFees");
    			String netRevenue=obj.getString("netRevenue");
    			
    			PlacementObj placementObj=new PlacementObj();
    			placementObj.setId(placementId);
    			placementObj.setProposalId(StringUtil.getLongValue(proposalId));
    			placementObj.setPlacementId(placementId);
    			placementObj.setPlacementName(placementName);
    			placementObj.setSiteName(site);
    			placementObj.setCreatedBy(createdBy);
    			placementObj.setCreatedOn(createdOn);
    			placementObj.setUpdatedBy(updatedBy);
    			placementObj.setUpdatedOn(updatedOn);
    			placementObj.setPublisherCPM(StringUtil.getUnformattedDoubleValueForMediaPlan(publisherCPM));
    			placementObj.setBudgetAllocation(StringUtil.getUnformattedDoubleValueForMediaPlan(budgetAllocation));
    			placementObj.setProposedImpression(StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(proposedImpression)));
    			placementObj.setMarginPercent(StringUtil.getUnformattedDoubleValueForMediaPlan(marginPercent));
    			placementObj.setEffectiveCPM(StringUtil.getUnformattedDoubleValueForMediaPlan(effectiveCPM));
    			placementObj.setFirstPartyAdServerCost(StringUtil.getUnformattedDoubleValueForMediaPlan(firstPartyAdServerCost));
    			placementObj.setThirdPartyAdServerCost(StringUtil.getUnformattedDoubleValueForMediaPlan(thirdPartyAdServerCost));
    			placementObj.setNetCostCPM(StringUtil.getUnformattedDoubleValueForMediaPlan(netCostCPM));
    			placementObj.setNetCost(StringUtil.getUnformattedDoubleValueForMediaPlan(netCost));
    			placementObj.setGrossRevenue(StringUtil.getUnformattedDoubleValueForMediaPlan(grossRevenue));
    			placementObj.setPublisherPayout(StringUtil.getUnformattedDoubleValueForMediaPlan(publisherPayout));
    			placementObj.setServingFees(StringUtil.getUnformattedDoubleValueForMediaPlan(servingFees));
    			placementObj.setNetRevenue(StringUtil.getUnformattedDoubleValueForMediaPlan(netRevenue));
    			
    			mediaPlanDAO.saveObject(placementObj);
    			placementObjList.add(placementObj);
        	}
        }
        MemcacheUtil.setPlacementsInCache(placementObjList, proposalId);
	} catch (Exception e) {
		log.severe("Exception : "+e.getMessage());
		
		throw e;
	}
        
		return null;
	}

	public void maintainPlacementHistory(PlacementObj placementObj, String userId) {
		try {
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			PlacementHistoryObj historyObj = new PlacementHistoryObj();
			historyObj.setId(placementObj.getId());
			historyObj.setProposalId(placementObj.getProposalId());
			historyObj.setPlacementId(placementObj.getPlacementId());
			historyObj.setPlacementName(placementObj.getPlacementName());
			historyObj.setSiteName(placementObj.getSiteName());
			historyObj.setCreatedBy(placementObj.getCreatedBy());
			historyObj.setCreatedOn(placementObj.getCreatedOn());
			historyObj.setUpdatedBy(placementObj.getUpdatedBy());
			historyObj.setUpdatedOn(placementObj.getUpdatedOn());
			historyObj.setPublisherCPM(placementObj.getPublisherCPM());
			historyObj.setBudgetAllocation(placementObj.getBudgetAllocation());
			historyObj.setProposedImpression(placementObj.getProposedImpression());
			historyObj.setMarginPercent(placementObj.getMarginPercent());
			historyObj.setEffectiveCPM(placementObj.getEffectiveCPM());
			historyObj.setFirstPartyAdServerCost(placementObj.getFirstPartyAdServerCost());
			historyObj.setThirdPartyAdServerCost(placementObj.getThirdPartyAdServerCost());
			historyObj.setNetCostCPM(placementObj.getNetCostCPM());
			historyObj.setNetCost(placementObj.getNetCost());
			historyObj.setGrossRevenue(placementObj.getGrossRevenue());
			historyObj.setPublisherPayout(placementObj.getPublisherPayout());
			historyObj.setServingFees(placementObj.getServingFees());
			historyObj.setNetRevenue(placementObj.getNetRevenue());
			historyObj.setHistoryCreationDate(DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
			historyObj.setUpdateDeleteByUserId(userId);
			mediaPlanDAO.saveObject(historyObj);
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
		}	
	}
	
	/*
	 * This method fill fetch all placements along with sites 
	 * for a given proposalId
	 * 
	 * @param String -- proposalId
	 * @return JSONObject
	 */
	
	/*public JSONObject placementMap(String proposalId){
		
		log.info("Load all placements for proposalId:"+proposalId);
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		List<String> placementIdList=new ArrayList<String>();
		List<JSONObject> placementJsonObjectList=new ArrayList<JSONObject>();
		Map<String,JSONArray> siteJsonMap=new HashMap<String, JSONArray>();
		
		JSONArray placementJsonArray= new JSONArray();
		try {
			List<PlacementObj> placementList=mediaPlanDAO.loadAllPlacements(StringUtil.getLongValue(proposalId));
			log.info("loaded placementList :"+placementList.size());
			JSONArray siteJsonArray= new JSONArray();
			for(PlacementObj obj:placementList){
				
				if(!placementIdList.contains(obj.getPlacementId())){
					
					placementIdList.add(obj.getPlacementId());
					JSONObject placementObj = new JSONObject();
					placementObj.put("proposalId",obj.getProposalId());
					placementObj.put("placementId",obj.getPlacementId());
					placementObj.put("placementName",obj.getPlacementName());
					placementObj.put("createdBy",obj.getCreatedBy());
					placementObj.put("createdOn",obj.getCreatedOn());
					placementObj.put("updatedBy",obj.getUpdatedBy());
					placementObj.put("updatedOn",obj.getUpdatedOn());
					
					placementJsonObjectList.add(placementObj);
					
				}
				
				siteJsonArray=siteJsonMap.get(obj.getPlacementId());				
				if(siteJsonArray==null){
					siteJsonArray= new JSONArray();
				}
				
				JSONObject siteObj = new JSONObject();
				siteObj.put("siteId",obj.getSiteId());
				siteObj.put("siteName",obj.getSiteName());
				siteObj.put("adSize",obj.getAdSize());
				siteObj.put("adFormat",obj.getAdFormat());
				siteObj.put("adServer",obj.getAdServer());
				siteObj.put("publisherCPM",obj.getPublisherCPM());
				siteObj.put("publisherPayOut",obj.getPublisherPayout());
				siteObj.put("firstPartyAdServerCost",obj.getFirstPartyAdServerCost());
				siteObj.put("thirdPartyAdServerCost",obj.getThirdPartyAdServerCost());
				siteObj.put("totalCost",obj.getTotalCost());
				siteObj.put("marginPercent",obj.getMarginPercent());
				siteObj.put("margin",obj.getMargin());
				siteObj.put("priceQuote",obj.getPriceQuote());
				siteObj.put("budgetAllocation",obj.getBudgetAllocation());
				siteObj.put("grossRevenue",obj.getGrossRevenue());
				siteObj.put("netRevenue",obj.getNetRevenue());
				siteObj.put("proposedImpression",obj.getProposedImpression());
				siteObj.put("forcastedImpression",obj.getForcastedImpression());
				siteObj.put("reservedImpression",obj.getReservedImpression());
				siteObj.put("availableImpression",obj.getAvailableImpression());
				
				siteJsonArray.add(siteObj);
				siteJsonMap.put(obj.getPlacementId(), siteJsonArray);
			}
			
			for(JSONObject placementObj:placementJsonObjectList){			
				placementObj.put("site", siteJsonMap.get(placementObj.getString("placementId")));
				placementJsonArray.add(placementObj);
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :placementMap: "+e.getMessage());
			
		}
		
		JSONObject placementResult = new JSONObject();
		placementResult.put("placement", placementJsonArray);
		
		return placementResult;
	}*/

	public JSONObject placementMap(String proposalId){
		
		log.info("Load all placements for proposalId:"+proposalId);
		DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
		DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		List<String> placementIdList=new ArrayList<String>();
		
		JSONArray placementJsonArray= new JSONArray();
		try {
			List<PlacementObj> placementList = MemcacheUtil.getPlacementsFromCache(proposalId);
			if(placementList == null || placementList.size() == 0) {
				placementList = mediaPlanDAO.loadAllPlacements(StringUtil.getLongValue(proposalId));
				MemcacheUtil.setPlacementsInCache(placementList, proposalId);
			}
			if(placementList != null && placementList.size() > 0) {
				log.info("loaded placementList :"+placementList.size());
				for(PlacementObj obj:placementList){
					if(!placementIdList.contains(obj.getPlacementId())){
						placementIdList.add(obj.getPlacementId());
						JSONObject placementObj = new JSONObject();
						placementObj.put("proposalId",obj.getProposalId());
						placementObj.put("placementId",obj.getPlacementId());
						placementObj.put("placementName",obj.getPlacementName());
						placementObj.put("createdBy",obj.getCreatedBy());
						placementObj.put("createdOn",obj.getCreatedOn());
						placementObj.put("updatedBy",obj.getUpdatedBy());
						placementObj.put("updatedOn",obj.getUpdatedOn());
						placementObj.put("site",obj.getSiteName());
						placementObj.put("publisherCPM","$"+df.format(obj.getPublisherCPM()));
						placementObj.put("budgetAllocation","$"+df.format(obj.getBudgetAllocation()));
						placementObj.put("proposedImpression",lf.format(obj.getProposedImpression())+"");
						placementObj.put("marginPercent",df.format(obj.getMarginPercent())+"%");
						placementObj.put("effectiveCPM","$"+df.format(obj.getEffectiveCPM()));
						placementObj.put("firstPartyAdServerCost","$"+df.format(obj.getFirstPartyAdServerCost()));
						placementObj.put("thirdPartyAdServerCost","$"+df.format(obj.getThirdPartyAdServerCost()));
						placementObj.put("netCostCPM","$"+df.format(obj.getNetCostCPM()));
						placementObj.put("netCost","$"+df.format(obj.getNetCost()));
						placementObj.put("grossRevenue","$"+df.format(obj.getGrossRevenue()));
						placementObj.put("publisherPayout","$"+df.format(obj.getPublisherPayout()));
						placementObj.put("servingFees","$"+df.format(obj.getServingFees()));
						placementObj.put("netRevenue","$"+df.format(obj.getNetRevenue()));
						placementJsonArray.add(placementObj);
					}
				}
			}
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :placementMap: "+e.getMessage());
			
		}
		
		JSONObject placementResult = new JSONObject();
		placementResult.put("placement", placementJsonArray);
		
		return placementResult;
	}
	
	
	/*
	 * Delete a proposal
	 * @param long - proposalId
	 * @return boolean - true or false
	 */
	public boolean deleteProposal(long proposalId){
		log.info("Going to delete proposal with proposalId:"+proposalId);
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			mediaPlanDAO.deleteProposal(proposalId);
			return true;
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
			return false;
		}		
	}
	
	
	/*
	 * Delete a placement
	 * @param String - placementId
	 * @return boolean - true or false
	 */
	public boolean deletePlacement(String placementId){
		log.info("Going to delete placement :"+placementId);
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			mediaPlanDAO.deletePlacement(placementId);
			return true;
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
			return false;
		}		
	}
	
	
	/*
	 * Delete a placement site
	 * @param String - siteId, long - userId
	 * @return boolean - true or false
	 */
	public boolean deletePlacementSite(String siteId,long userId){
		log.info("Going to delete placement -> site with siteId:"+siteId+" by userId:"+userId);
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			PlacementObj obj=mediaPlanDAO.loadPlacementBySite(siteId);
			
			log.info("First add this data in PlacementHistoryObj with status 0");
			PlacementHistoryObj placementHistoryObj=new PlacementHistoryObj(obj.getProposalId(), obj.getPlacementId(), 
					obj.getPlacementName(), obj.getCreatedOn(), obj.getUpdatedOn(), obj.getCreatedBy(), obj.getUpdatedBy(),
					obj.getSiteId(), obj.getSiteName(), obj.getAdSize(), obj.getAdFormat(), obj.getAdServer(), obj.getPublisherCPM(),
					obj.getFirstPartyAdServerCost(), obj.getThirdPartyAdServerCost(), obj.getTotalCost(), obj.getMarginPercent(),
					obj.getMargin(), obj.getPriceQuote(), obj.getBudgetAllocation(), obj.getPublisherPayout(), obj.getGrossRevenue(),
					obj.getNetRevenue(), obj.getForcastedImpression(), obj.getReservedImpression(), obj.getAvailableImpression(),
					obj.getProposedImpression(), obj.getSiteNumberPerPlacement(), obj.getPlacementNumber());
			placementHistoryObj.setActiveStatus("0");
			placementHistoryObj.setUpdatedBy(userId+"");
			mediaPlanDAO.saveObject(placementHistoryObj);
			
			mediaPlanDAO.deletePlacementBySite(siteId);
			log.info("Site deleted successfully : siteId:"+siteId);
			return true;
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
			
			return false;
		}		
	}
	
	/*
	 * Load all user from a company according to their role
	 * @param String companyId,String roleName
	 * @return List<String> salesRepList
	 */
	public List<String> loadSalesRepresentative(String companyId,String roleName){
		List<String> salesRepList=new ArrayList<String>();
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		try {
			List<UserDetailsObj> userList=userDetailsDAO.getUsersByCompanyIdAndRoleName(companyId, roleName);
			for(UserDetailsObj user:userList){				
				if(user.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])){
					salesRepList.add(user.getUserName());
				}
			}
		} catch (Exception e) {
			log.severe("DataServiceException : "+e.getMessage());
			
		}
		return salesRepList;
	}
	
	
	/* 
	 * Save a adSize
	 * @param AdSizeObj
	 * @return long
	 */
	public long saveAdSize(AdSizeObj obj){
		log.info("Going to save adSize");
		long adSizeId=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {			
			if(obj.getAdSizeId() == 0 ){
				long maxId=mediaPlanDAO.maxAdSizeId();				
				adSizeId=(maxId+1);
				log.info("maxId exist in datastore: "+maxId+" : so, save new adSize with Id:"+adSizeId);
			}else{
				adSizeId=obj.getAdSizeId();
				log.info("Update adSize with adSizeId:"+adSizeId);
			}				
			
			obj.setAdSizeId(adSizeId);		
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);
			Map<String,String> adSizeMap=MemcacheUtil.getAdSizeFromCache();
			if(adSizeMap ==null ){
				adSizeMap=new LinkedHashMap<String, String>();
			}
			adSizeMap.put(String.valueOf(adSizeId), obj.getAdSize());
			MemcacheUtil.setAdSizeInCache(adSizeMap);
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return adSizeId;
	}
	
	
	/* 
	 * Save a adFormat
	 * @param AdFormatObj
	 * @return long
	 */
	public long saveAdFormat(AdFormatObj obj){
		log.info("Going to save adFormat");
		long adFormatId=0;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {			
			if(obj.getAdFormatId() == 0 ){
				long maxId=mediaPlanDAO.maxAdFormatId();				
				adFormatId=(maxId+1);
				log.info("maxId exist in datastore: "+maxId+" : so, save new adFormat with Id:"+adFormatId);
			}else{
				adFormatId=obj.getAdFormatId();
				log.info("Update adFormat with adFormatId:"+adFormatId);
			}				
			
			obj.setAdFormatId(adFormatId);		
			String createdOn=DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
			obj.setCreatedOn(createdOn);
			mediaPlanDAO.saveObject(obj);
			Map<String,String> adFormatMap=MemcacheUtil.getAdFormatFromCache();
			if(adFormatMap ==null ){
				adFormatMap=new LinkedHashMap<String, String>();
			}
			adFormatMap.put(String.valueOf(adFormatId), obj.getAdFormatName());
			MemcacheUtil.setAdFormatInCache(adFormatMap);
			
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
						
		}
		return adFormatId;
	}
	
	
	 /*
     * Get loadAllAdSize
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public Map<String,String> loadAllAdSize(){
		log.info("loading all adsize....");
		
    	Map<String,String> adSizeMap=MemcacheUtil.getAdSizeFromCache();
    	if(adSizeMap==null){
    		adSizeMap=new LinkedHashMap<String, String>();
    		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
    		try {
    			List<AdSizeObj> resultList=mediaPlanDAO.loadAllAdSize();
				if(resultList!=null && resultList.size()>0){
					log.info("resultList :"+resultList.size()+" : Put in memcache also..");
					for(AdSizeObj obj:resultList){
						String key=String.valueOf(obj.getAdSizeId());
						String value=obj.getAdSize();
						adSizeMap.put(key, value);
					}
					MemcacheUtil.setAdSizeInCache(adSizeMap);
				}else{
					log.info("No AdSizeObj found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
    	}
    	return adSizeMap;
	}
    
    /*
     * Get loadAllAdFormats
     * @author Youdhveer Panwar
     * @return Map<String,String>
     */
    public Map<String,String> loadAllAdFormats(){
		log.info("loading all adFormats....");
		
    	Map<String,String> adFormatMap=MemcacheUtil.getAdFormatFromCache();
    	if(adFormatMap==null){
    		adFormatMap=new LinkedHashMap<String, String>();
    		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
    		try {
    			List<AdFormatObj> resultList=mediaPlanDAO.loadAllAdFormats();
				if(resultList!=null && resultList.size()>0){
					log.info("resultList :"+resultList.size()+" : Put in memcache also..");
					for(AdFormatObj obj:resultList){
						String key=String.valueOf(obj.getAdFormatId());
						String value=obj.getAdFormatName();
						adFormatMap.put(key, value);
					}
					MemcacheUtil.setAdFormatInCache(adFormatMap);
				}else{
					log.info("No adFormat found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				
			}
    	}
    	return adFormatMap;
	}
    
    
    /*
	 * loadForcastingDataBySite
	 * @param - String startDate,String endDate,String site
	 * @return -Map<String,String>
	 */
	public Map<String,String> loadForcastingDataBySite(String startDate,String endDate,String site){
		log.info("Loading forecasting data for site :"+site);
		SellThroughDataObj sellThrough=null;
		String siteKey=site+"_"+startDate+"_"+endDate;
		Map<String,String> forecastingMap=MemcacheUtil.getSiteForecastedDataFromCache(siteKey);
		if(forecastingMap==null || forecastingMap.size()==0){
			forecastingMap=new HashMap<String, String>();
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				sellThrough=mediaPlanDAO.loadForecastingDataBySiteName(startDate, endDate, site);
				forecastingMap.put("AdUnit", sellThrough.getAdUnit());
				forecastingMap.put("AvailableImpressions", sellThrough.getAvailableImpressions());
				forecastingMap.put("ForecastedImpressions", sellThrough.getForecastedImpressions());
				forecastingMap.put("ReservedImpressions", sellThrough.getReservedImpressions());			
				MemcacheUtil.setSiteForecastedDataInCache(forecastingMap, siteKey);
			} catch (DataServiceException e) {
				log.severe("Exception in loading site:"+site+" : "+e.getMessage());
				
			} catch (IOException e) {
				log.severe("IOException in loading site:"+site+" : "+e.getMessage());
				
			}
		}
		
		return forecastingMap;
	}
	
	@Override
	public Map<String,String> getAllCampaignTypes(List<DropdownDataObj> allDropdownDataObjList) {
		Map<String,String> campaignTypeMap = new LinkedHashMap<String, String>();
		try {
			if (allDropdownDataObjList != null && allDropdownDataObjList.size() > 0) {
				for (DropdownDataObj dropdownDataObj : allDropdownDataObjList) {
					if(dropdownDataObj != null && dropdownDataObj.getValueType() != null && dropdownDataObj.getValueType().equals(LinMobileConstants.DROP_DOWN_VALUE[0]) && dropdownDataObj.getValue() != null && dropdownDataObj.getObjectId() > 0) {
						campaignTypeMap.put(dropdownDataObj.getObjectId()+"", dropdownDataObj.getValue());
					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
			
		}
		log.info("campaignTypeMap size : "+campaignTypeMap.size());
		return campaignTypeMap;
	}
	
	@Override
	public Map<String, String> getAllCampaignStatus(List<DropdownDataObj> allDropdownDataObjList) {
		Map<String, String> campaignStatusMap = new LinkedHashMap<String, String>();
		try {
			if (allDropdownDataObjList != null && allDropdownDataObjList.size() > 0) {
				for (DropdownDataObj dropdownDataObj : allDropdownDataObjList) {
					if(dropdownDataObj != null && dropdownDataObj.getValueType() != null && dropdownDataObj.getValueType().equals(LinMobileConstants.DROP_DOWN_VALUE[1]) && dropdownDataObj.getValue() != null && dropdownDataObj.getObjectId() > 0) {
						campaignStatusMap.put(dropdownDataObj.getObjectId()+"", dropdownDataObj.getValue());
					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
			
		}
		log.info("campaignStatusMap size : "+campaignStatusMap.size());
		return campaignStatusMap;
	}
	
	@Override
	public PublisherIOReportDTO publisherIOReportObject(String proposalId) throws Exception {
		PublisherIOReportDTO  publisherIOReportDTO = new PublisherIOReportDTO();
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		CompanyObj companyObj = new CompanyObj();
		ProposalDTO proposalDTO = loadProposal(StringUtil.getLongValue(proposalId));
		if(proposalDTO == null) {
			log.severe("No proposal exists for proposalId : "+proposalId);
			throw new Exception();
		}
		String companyId=proposalDTO.getCompany();
		if(companyId == null || companyId.trim().length() == 0){
			log.severe("No company exists for proposalId : "+proposalId+", companyId : "+companyId);
			throw new Exception();
		}
		companyObj = userDetailsDAO.getCompanyById(StringUtil.getLongValue(companyId), MemcacheUtil.getAllCompanyList());
		if(companyObj == null) {
			log.severe("No Company exist for Id : "+companyId);
			throw new Exception();
		}
		publisherIOReportDTO.setPublisherCompanyName(companyObj.getCompanyName());
		if(companyObj.getContactPersonName() != null) {
			publisherIOReportDTO.setPublisherContact(companyObj.getContactPersonName().trim());
		}
		else {
			publisherIOReportDTO.setPublisherContact("");
		}
		if(companyObj.getCompanyAddress() != null) {
			publisherIOReportDTO.setPublisherAddress(companyObj.getCompanyAddress().trim());
		}
		else {
			publisherIOReportDTO.setPublisherAddress("");
		}
		if(companyObj.getCompanyEmail() != null) {
			publisherIOReportDTO.setPublisherEmail(companyObj.getCompanyEmail().trim());
		}
		else {
			publisherIOReportDTO.setPublisherEmail("");
		}
		if(companyObj.getPhone() != null) {
			publisherIOReportDTO.setPublisherPhone(companyObj.getPhone().trim());
		}
		else {
			publisherIOReportDTO.setPublisherPhone("");
		}
		if(companyObj.getFax() != null) {
			publisherIOReportDTO.setPublisherFax(companyObj.getFax().trim());
		}
		else {
			publisherIOReportDTO.setPublisherFax("");
		}
		
		publisherIOReportDTO.setCheckPayableTo("Lin Mobile pvt Ltd");
		publisherIOReportDTO.setTaxID("5778504665656");
		
		publisherIOReportDTO.setCompanyName("Lin Mobile");
		publisherIOReportDTO.setContact("Kevin Wassong");
		publisherIOReportDTO.setAddress("60 East 42nd Street Suite 1460, New York NY 10165");
		publisherIOReportDTO.setEmail("abc@linmobile.com");
		publisherIOReportDTO.setPhone("1234567890");
		publisherIOReportDTO.setFax("12123434");
		
		return publisherIOReportDTO;
	}

	@Override
	public List<PlacementDTO> getSelectedPlacementReportList(String selectedRows, StringBuilder totalValue) {
		List<PlacementDTO> placementDTOList = new ArrayList<PlacementDTO>();
		double totalBudget = 0;
    	long totalImpressions = 0;
    	double totalECPM = 0;
    	DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
		DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
		try {
			JSONObject placementDetails = (JSONObject) JSONSerializer.toJSON(selectedRows);			
	        JSONArray jsonArray=placementDetails.getJSONArray("placement");
	        if(jsonArray.size()==0){
	        	log.warning("No placement to save..");        	
	        }else{
	        	for(int i=0;i<jsonArray.size();i++){
	    			JSONObject obj=jsonArray.getJSONObject(i);
	    			String placementName=obj.getString("placementName");
	    			String site=obj.getString("site");
	    			String effectiveCPM=obj.getString("CPM");
	    			String budgetAllocation=obj.getString("budgetAllocation");
	    			String proposedImpression=obj.getString("proposedImpression");
	    			
	    			totalBudget = totalBudget + StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(budgetAllocation));
	    			totalImpressions = totalImpressions + StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(proposedImpression));
	    			totalECPM = totalECPM + StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(effectiveCPM));
	    			
	    			PlacementDTO placementDTO = new PlacementDTO();
	    			placementDTO.setPlacementName(placementName);
	    			placementDTO.setSiteName(site);
	    			placementDTO.setEffectiveCPM(effectiveCPM);
	    			placementDTO.setBudgetAllocation(budgetAllocation);
	    			placementDTO.setProposedImpression(proposedImpression);
	    			
	    			placementDTOList.add(placementDTO);
	        	}
	        }
		}
		catch (Exception e) {
			
			log.severe("Exception in getSelectedPlacementReportList of mediaPlanService"+e.getMessage());
		}
		totalValue.append("$"+df.format(totalBudget)).append("<SEP>").append(lf.format(totalImpressions)+"")
		.append("<SEP>").append("$"+df.format(totalECPM)+"");
		return placementDTOList;
	}

	@Override
	public ClientIOReportDTO clientIOReportObject(String proposalId, long userId) throws Exception {
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		Map<String,String> advertiserMap = null;
		Map<String,String> agencyMap = null;
		ClientIOReportDTO clientIOReportDTO = new ClientIOReportDTO();
		ProposalDTO proposalDTO = loadProposal(StringUtil.getLongValue(proposalId));
		if(proposalDTO == null) {
			log.severe("No proposal exists for proposalId : "+proposalId);
			throw new Exception();
		}
		String companyId=proposalDTO.getCompany();
		if(companyId == null || companyId.trim().length() == 0){
			log.severe("No company exists for proposalId : "+proposalId+", companyId : "+companyId);
			throw new Exception();
		}
		String advertiserId = proposalDTO.getAdvertiserId();
		String agencyId = proposalDTO.getAgencyId();
		
		if(advertiserId != null && advertiserId.trim().length() > 0) {
			advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(userId, companyId, true, false);
			String advertiser = advertiserMap.get("_"+advertiserId);
			if(advertiser == null || advertiser.trim().length() == 0) {
				advertiser = advertiserId.trim();
			}
			clientIOReportDTO.setAdvertiseCompanyName(advertiser);
		}
		if(agencyId != null && agencyId.trim().length() > 0) {
			agencyMap = userService.getSelectedAccountsForCampaingnPlanning(userId, companyId, false, true);
			String agency = agencyMap.get("_"+agencyId);
			if(agency == null || agency.trim().length() == 0) {
				agency = agencyId.trim();
			}
			clientIOReportDTO.setAgencyCompanyName(agency);
		}
		
		clientIOReportDTO.setAdvertiserBasisForBilling("LIN Mobile Ad Server");
		clientIOReportDTO.setAdvertiserPaymentTerms("NET 30");
		clientIOReportDTO.setAdvertiserCollectionType("NET");
		clientIOReportDTO.setAdvertiserCancellationPolicy("2 business days");
		
		clientIOReportDTO.setInsertionOrderDate(DateUtil.getFormatedDate(new Date(), "MM-dd-yyyy"));
		/*clientIOReportDTO.setInsertionOrderStation("station Name");
		clientIOReportDTO.setAccountExecutive("Kevin Wassong");
		clientIOReportDTO.setAccountExecutiveEmail("kevin.wassong@linmedia.com");*/
		clientIOReportDTO.setAccountExecutivePhone("212-729-4345");
		clientIOReportDTO.setAccountExecutiveFax("1234-1234");
		/*clientIOReportDTO.setAccountManager("Amina Alliyu");
		clientIOReportDTO.setAccountManagerEmail("amina.alliyu@linmedia.com");*/
		clientIOReportDTO.setAccountManagerPhone("917-720-0313");
		clientIOReportDTO.setAccountManagerFax("1235-1235");
		
		clientIOReportDTO.setBillingNotes("billing Notes");
		
		return clientIOReportDTO;
			
	}

	@Override
	public Map clientIOExcelReport(long mediaPlanId, String publisherIdInBQ) throws Exception {
		log.info("inside clientIOExcelReport in MediaPlanService");
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
		/*boolean getAdvertiser = true;
		boolean getAgency = true;*/
		Map<String, String> dmaMap = new HashMap<>();
		Map<String, String> goalMap = new HashMap<>();
		Map<String, String> frequencyCapMap = new HashMap<>();
		String campaignName = "Empty";
		Map excelMap = new HashMap<>();
		List<ClientIOReportDTO> clientIOReportDTOList = new ArrayList<>();
		try {
			SmartMediaPlanObj smartMediaPlanObj = mediaPlanDAO.getSmartMediaPlanObj(mediaPlanId);
			if(smartMediaPlanObj == null || smartMediaPlanObj.getCampaignId() == null) {
				campaignName = "No Media Plan Exists";
				log.severe("No Media Plan Exists");
			}
			else {
				log.info(smartMediaPlanObj.toString());
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				SmartCampaignObj campaignObj = smartCampaignPlannerDAO.getCampaignById(Long.valueOf(smartMediaPlanObj.getCampaignId()));
				if(campaignObj == null) {
					campaignName = "No Campaign Exists";
					log.severe("No Campaign Exists");
				}
				else {
					log.info(campaignObj.toString());
					List<CampaignDetailDTO> campaignDetailDTOList = new ArrayList<>();
					campaignName = campaignObj.getName();
					String dealType = "";
					
					if(campaignObj.getRateTypeList() != null && campaignObj.getRateTypeList().size() > 0) {
						dealType = campaignObj.getRateTypeList().get(0).getValue();
					}
					List<SmartCampaignPlacementObj> placementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(campaignObj.getCampaignId());
					if(placementObjList == null || placementObjList.size() == 0) {
						campaignName = "No Placement Exists";
						log.severe("No Placement Exists");
					}
					else {
						Map<String, SmartCampaignPlacementDTO> mediaPlanPlacementMap = new HashMap<>();
						if(smartMediaPlanObj.getPlacements() != null && smartMediaPlanObj.getPlacements().size() > 0) {
							for(SmartCampaignPlacementDTO smartCampaignPlacementDTO : smartMediaPlanObj.getPlacements()) {
								mediaPlanPlacementMap.put(smartCampaignPlacementDTO.getId()+"", smartCampaignPlacementDTO);
							}
						}
						for(SmartCampaignPlacementObj campaignPlacementObj : placementObjList) {
							if(campaignPlacementObj != null && campaignPlacementObj.getPlacementName() != null) {
								String placementName = campaignPlacementObj.getPlacementName().trim();
								String geoTarget = getFormattedGeoTarget(campaignPlacementObj);
								/*if(campaignPlacementObj.getGeoObj() != null && campaignPlacementObj.getGeoObj().size() > 0) {
									for(GeoTargetsObj geoTargetsObj : campaignPlacementObj.getGeoObj()) {
										geoTarget = geoTarget + "["+geoTargetsObj.getGeoTargetsName()+"], ";
									}
									geoTarget = StringUtil.deleteFromLastOccurence(geoTarget, ",");
								}*/
								if(campaignPlacementObj.getGeoObj() != null && campaignPlacementObj.getGeoObj().size() > 0) {
									for(GeoTargetsObj geoTargetsObj : campaignPlacementObj.getGeoObj()) {
										dmaMap.put(geoTargetsObj.getGeoTargetsName(), null);
									}
								}
								if(campaignPlacementObj.getFrequencyCap() != null && campaignPlacementObj.getFrequencyCap().trim().length() > 0 && LinMobileUtil.isNumeric(campaignPlacementObj.getFrequencyCap().trim())  && campaignPlacementObj.getFrequencyCapUnit() != null) {
									if(campaignPlacementObj.getFrequencyCapUnit().trim().equalsIgnoreCase("Day")) {
										frequencyCapMap.put(campaignPlacementObj.getFrequencyCap() +" x 24", null);
									}else {
										frequencyCapMap.put(campaignPlacementObj.getFrequencyCap() +" per "+campaignPlacementObj.getFrequencyCapUnit(), null);
									}
								}
								if(campaignPlacementObj.getGoal() != null && campaignPlacementObj.getGoal().trim().length() > 0) {
									goalMap.put(campaignPlacementObj.getGoal(), null);
								}
								
								String creatives = "";
								String richMedia = "";
								if(campaignPlacementObj.getCreativeObj() != null && campaignPlacementObj.getCreativeObj().size() > 0) {
									for(CreativeObj creativeObj : campaignPlacementObj.getCreativeObj()) {
										if(creativeObj != null && creativeObj.getFormat() != null && creativeObj.getFormat().contains("Rich")) {
											richMedia = richMedia + creativeObj.getFormat() + ", ";
										}
										else if(creativeObj != null && creativeObj.getSize() != null) {
											creatives = creatives + creativeObj.getSize()+", ";
										}
									}
									creatives = StringUtil.deleteFromLastOccurence(creatives, ",");
									if(richMedia.length() > 0) {
										richMedia = StringUtil.deleteFromLastOccurence(richMedia, ",");
									}
									else {
										richMedia = "No";
									}
								}
								long impressions = 0;
								double rate = 0.0;
								double cost = 0.0;
								SmartCampaignPlacementDTO smartCampaignPlacementDTO = mediaPlanPlacementMap.get(campaignPlacementObj.getPlacementId()+"");
								if(smartCampaignPlacementDTO != null && smartCampaignPlacementDTO.getImpressions() > 0 && smartCampaignPlacementDTO.getRate() > 0) {
									impressions = smartCampaignPlacementDTO.getImpressions();
									rate = smartCampaignPlacementDTO.getRate();
									cost = (double)(impressions*rate)/1000;
								}
								CampaignDetailDTO campaignDetailDTO = new CampaignDetailDTO(campaignName, "", placementName, dealType, geoTarget, creatives, richMedia, 
																		DateUtil.getRequiredFormatDate(campaignPlacementObj.getStartDate(), "MM-dd-yyyy", "MM/dd/yyyy"), 
																		DateUtil.getRequiredFormatDate(campaignPlacementObj.getEndDate(), "MM-dd-yyyy", "MM/dd/yyyy"), 
																		"", impressions, rate, cost);
								campaignDetailDTOList.add(campaignDetailDTO);
							}
						}
					}
					ClientIOReportDTO clientIOReportDTO = new ClientIOReportDTO();
					if(campaignObj.getAdvertiserId() != null && campaignObj.getAdvertiserId().trim().length() > 0 && LinMobileUtil.isNumeric(campaignObj.getAdvertiserId()) && StringUtil.getLongValue(campaignObj.getAdvertiserId()) > 0) {
						//getAdvertiser = false;
						AdvertiserObj advertiserObj = mediaPlanDAO.loadAdvertiser(Long.valueOf(campaignObj.getAdvertiserId()));
						if(advertiserObj != null) {
							log.info(advertiserObj.toString());
							clientIOReportDTO.setAdvertiseCompanyName(advertiserObj.getName());
							clientIOReportDTO.setAdvertiseContact(advertiserObj.getContactPersonName());
							clientIOReportDTO.setAdvertiseAddress(advertiserObj.getAddress());
							clientIOReportDTO.setAdvertiseEmail(advertiserObj.getEmail());
							clientIOReportDTO.setAdvertisePhone(advertiserObj.getPhone());
							clientIOReportDTO.setAdvertiseFax(advertiserObj.getFax());
						}
						else {
							log.severe("AdvertiserObj does not exist for advertiser id : "+campaignObj.getAdvertiserId());
						}
					}
					else {
						log.info("campaignObj.getAdvertiserId() : "+campaignObj.getAdvertiserId());
					}
					if(campaignObj.getAgencyId() != null && campaignObj.getAgencyId().trim().length() > 0 && LinMobileUtil.isNumeric(campaignObj.getAgencyId()) && StringUtil.getLongValue(campaignObj.getAgencyId()) > 0) {
						//getAgency = false;
						AgencyObj agencyObj = mediaPlanDAO.loadAgency(Long.valueOf(campaignObj.getAgencyId()));
						if(agencyObj != null) {
							log.info(agencyObj.toString());
							clientIOReportDTO.setAgencyCompanyName(agencyObj.getName());
							clientIOReportDTO.setAgencyContact(agencyObj.getContactPersonName());
							clientIOReportDTO.setAgencyAddress(agencyObj.getAddress());
							clientIOReportDTO.setAgencyEmail(agencyObj.getEmail());
							clientIOReportDTO.setAgencyPhone(agencyObj.getPhone());
							clientIOReportDTO.setAgencyFax(agencyObj.getFax());
						}
						else {
							log.severe("AgencyObj does not exist for agency id : "+campaignObj.getAgencyId());
						}
					}
					else {
						log.info("campaignObj.getAgencyId() : "+campaignObj.getAgencyId());
					}
					/*AccountsEntity[] accountEntityArr = getAccountsForMediaPlan(campaignObj.getAdvertiserId(), campaignObj.getAgencyId(), publisherIdInBQ, getAdvertiser, getAgency);
					if(getAdvertiser && accountEntityArr != null && accountEntityArr.length>=1) {
						clientIOReportDTO.setAdvertiseCompanyName(accountEntityArr[0].getAccountName().replace(" ("+LinMobileConstants.ADVERTISER_ID_PREFIX+")", ""));
						clientIOReportDTO.setAdvertiseContact(accountEntityArr[0].getContact());
						clientIOReportDTO.setAdvertiseAddress("");
						clientIOReportDTO.setAdvertiseEmail(accountEntityArr[0].getEmail());
						clientIOReportDTO.setAdvertisePhone(accountEntityArr[0].getPhone());
						clientIOReportDTO.setAdvertiseFax(accountEntityArr[0].getFax());
					}*/
					
					/*if(getAgency && accountEntityArr != null && accountEntityArr.length>=2) {
						clientIOReportDTO.setAgencyCompanyName(accountEntityArr[1].getAccountName().replace(" ("+LinMobileConstants.AGENCY_ID_PREFIX+")", ""));
						clientIOReportDTO.setAgencyContact(accountEntityArr[1].getContact());
						clientIOReportDTO.setAgencyAddress("");
						clientIOReportDTO.setAgencyEmail(accountEntityArr[1].getEmail());
						clientIOReportDTO.setAgencyPhone(accountEntityArr[1].getPhone());
						clientIOReportDTO.setAgencyFax(accountEntityArr[1].getFax());
					}*/
				
					clientIOReportDTO.setAdvertiserBasisForBilling("LIN Mobile Ad Server");
					clientIOReportDTO.setAdvertiserPaymentTerms("NET 30");
					//clientIOReportDTO.setAdvertiserCollectionType("NET");
					clientIOReportDTO.setAdvertiserCancellationPolicy("2 business days");
					
					
					clientIOReportDTO.setInsertionOrderDate(DateUtil.getFormatedDate(new Date(), "MM/dd/yyyy"));
					/*clientIOReportDTO.setInsertionOrderStation("station Name");
					clientIOReportDTO.setAccountExecutive("Kevin Wassong");
					clientIOReportDTO.setAccountExecutiveEmail("kevin.wassong@linmedia.com");*/
					clientIOReportDTO.setAccountExecutivePhone("212-729-4345");
					clientIOReportDTO.setAccountExecutiveFax("1234-1234");
					/*clientIOReportDTO.setAccountManager("Amina Alliyu");
					clientIOReportDTO.setAccountManagerEmail("amina.alliyu@linmedia.com");*/
					clientIOReportDTO.setAccountManagerPhone("917-720-0313");
					clientIOReportDTO.setAccountManagerFax("1235-1235");
					 
					clientIOReportDTO.setBillingNotes("");
					clientIOReportDTO.setCampaignDetailDTOList(campaignDetailDTOList);
					
					if(dmaMap != null && dmaMap.size() > 0) {
						String dmas = "";
						for(String dma : dmaMap.keySet()) {
							dmas = dmas + "["+dma+"], ";
						}
						dmas = StringUtil.deleteFromLastOccurence(dmas, ",");
						clientIOReportDTO.setCampaignDMA(dmas);
					}
					
					if(goalMap != null && goalMap.size() > 0) {
						String goals =  "";
						for(String goal : goalMap.keySet()) {
							goals = goals + goal+"%, ";
						}
						goals = StringUtil.deleteFromLastOccurence(goals, ",");
						clientIOReportDTO.setCampaignGoal(goals);
					}
					
					if(frequencyCapMap != null && frequencyCapMap.size() > 0) {
						String frequencyCaps =  "";
						for(String frequencyCap : frequencyCapMap.keySet()) {
							frequencyCaps = frequencyCaps + frequencyCap+", ";
						}
						frequencyCaps = StringUtil.deleteFromLastOccurence(frequencyCaps, ",");
						clientIOReportDTO.setFrequencyCap(frequencyCaps);
					}
					
					clientIOReportDTO.setCampaignNote(campaignObj.getNotes());
					clientIOReportDTOList.add(clientIOReportDTO);
				}
			}
		}catch(Exception e) {
			log.severe("Exception in clientIOExcelReport of MediaPlanService : "+e.getMessage());
			e.printStackTrace();
			//throw e;
		}
		excelMap.put("dataBean", clientIOReportDTOList);
		excelMap.put("campaignName", campaignName);
		log.info("campaignName : "+campaignName);
		return excelMap;
	}

	@Override
	public Map POExcelReport(long mediaPlanId, String publisherIdInBQ) throws Exception {
		log.info("inside POExcelReport in MediaPlanService");
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
		//boolean getAdvertiser = true;
		Map excelMap = new HashMap<>();
		List<ClientIOReportDTO> clientIOReportDTOList = new ArrayList<>();
		List<String> publisherNameList = new ArrayList();
		Map<String, String> dmaMap = new HashMap<>();
		Map<String, String> goalMap = new HashMap<>();
		Map<String, String> frequencyCapMap = new HashMap<>();
		String campaignName = "Empty";
		boolean partnerNameFound = false;
		Map<String, List<CampaignDetailDTO>> campaignDetailDTOListMap = new HashMap<String, List<CampaignDetailDTO>>();
		try {
			SmartMediaPlanObj smartMediaPlanObj = mediaPlanDAO.getSmartMediaPlanObj(mediaPlanId);
			if(smartMediaPlanObj == null || smartMediaPlanObj.getCampaignId() == null) {
				log.severe("No Media Plan Exists");
				campaignName = "No Media Plan Exists";
			}
			else {
				//log.info(smartMediaPlanObj.toString());
				//log.info("smartMediaPlanObj.getPlacements().size() == "+smartMediaPlanObj.getPlacements().size());
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				SmartCampaignObj campaignObj = smartCampaignPlannerDAO.getCampaignById(Long.valueOf(smartMediaPlanObj.getCampaignId()));
				if(campaignObj == null) {
					campaignName = "No Campaign Exists";
					log.severe("No Campaign Exists");
				}
				else {
					log.info(campaignObj.toString());
					// create name wise company
					Map<String, CompanyObj> companyMap = new HashMap<>();
					List<CompanyObj> companyObjList = MemcacheUtil.getAllCompanyList();
					if(companyObjList == null || companyObjList.size() == 0) {
						campaignName = "No Publisher Exists";
						log.severe("No Publisher Exists");
					}
					else {
						for(CompanyObj companyObj : companyObjList) {
							companyMap.put(companyObj.getId()+"", companyObj);
						}
					}
					campaignName = campaignObj.getName();
					String dealType = "";
					
					if(campaignObj.getRateTypeList() != null && campaignObj.getRateTypeList().size() > 0) {
						dealType = campaignObj.getRateTypeList().get(0).getValue();
					}
					
					List<SmartCampaignPlacementObj> placementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(campaignObj.getCampaignId());
					if(placementObjList == null || placementObjList.size() == 0) {
						campaignName = "No Placement Exists";
						log.severe("No Placements Exists in SmartCampaignPlacementObj");
					}
					else {
						//log.info("SmartCampaignPlacementObj list size : "+placementObjList.size());//1
						if(smartMediaPlanObj.getPlacements() == null || smartMediaPlanObj.getPlacements().size() == 0) {
							campaignName = "No Placement Exists";
							log.severe("No Placement exists in SmartMediaPlanObj");
						}
						else {
							//log.info("smartMediaPlanObj placements list size : "+smartMediaPlanObj.getPlacements().size());//1
							// create placement id wise product map
							Map<String, List<ProductDTO>> mediaPlanProductMap = new HashMap<>();
							if(smartMediaPlanObj.getProducts() == null && smartMediaPlanObj.getProducts().size() == 0) {
								campaignName = "No Product Exists";
							}
							else {
								//log.info("smartMediaPlanObj.getProducts().size() = "+smartMediaPlanObj.getProducts().size());//11
								for(ProductDTO productDTO : smartMediaPlanObj.getProducts()) {
									log.info("productDTO.getPlacementId() = "+productDTO.getPlacementId());
									if(mediaPlanProductMap.containsKey(productDTO.getPlacementId()+"")) {
										List<ProductDTO> productDTOList = mediaPlanProductMap.get(productDTO.getPlacementId()+"");
										productDTOList.add(productDTO);
									}
									else {
										List<ProductDTO> productDTOList = new ArrayList<>();
										productDTOList.add(productDTO);
										mediaPlanProductMap.put(productDTO.getPlacementId()+"", productDTOList);
									}
								}
								//log.info("mediaPlanProductMap.size() = "+mediaPlanProductMap.size());
							}
							
							// create placement id wise SmartCampaignPlacementObj map
							Map<String, SmartCampaignPlacementObj> smartCampaignPlacementObjMap = new HashMap<>();
							for(SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
								smartCampaignPlacementObjMap.put(smartCampaignPlacementObj.getPlacementId()+"", smartCampaignPlacementObj);
							}
							
							// main logic
							//log.info("smartMediaPlanObj.getPlacements().size() = "+smartMediaPlanObj.getPlacements().size());
							for(SmartCampaignPlacementDTO smartCampaignPlacementDTO : smartMediaPlanObj.getPlacements()) {
								if(smartCampaignPlacementDTO != null) {
									List<ProductDTO> productDTOList = mediaPlanProductMap.get(smartCampaignPlacementDTO.getId()+"");
									//log.info("productDTOList.size() = = "+productDTOList.size());
									SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlacementObjMap.get(smartCampaignPlacementDTO.getId()+"");
									if(productDTOList != null && productDTOList.size() > 0 && smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlacementName() != null) {
										Map<String, CampaignDetailDTO> calculationMap = new HashMap<>();
										for(ProductDTO productDTO : productDTOList) {
											if(productDTO != null && productDTO.getPartnerId() != null && productDTO.getPartnerId().length() > 0) {
												String partnerId = productDTO.getPartnerId();
												String partnerName = "";
												CampaignDetailDTO campaignDetailDTO = new CampaignDetailDTO();
												if(calculationMap.containsKey(partnerId)) {
													campaignDetailDTO = calculationMap.get(partnerId);
													long imp = campaignDetailDTO.getImpression();
													double cost = campaignDetailDTO.getCampaignCost();
													
													campaignDetailDTO.setImpression(imp + productDTO.getAllocatedImpressions());
													campaignDetailDTO.setCampaignCost(cost + productDTO.getPayout());
												}
												else {
													campaignDetailDTO = new CampaignDetailDTO();
													/*if(partnerId.equals("0")) {
														partnerName = productDTO.getName();
													} else {
														partnerName = productDTO.getPartnerName();
													}*/
													if(partnerId.equals("0")) {
														partnerName = productDTO.getName();
														if(partnerName == null || partnerName.trim().isEmpty()) {
															log.info("Empty partner name.");
															continue;
														}else {
															partnerNameFound = true;
														}
													} else {
														partnerNameFound = true;
														partnerName = productDTO.getPartnerName();
													}
													log.info(">>>>>>>>>>>partnerId : "+partnerId+" and partnerName :"+partnerName);
													campaignDetailDTO.setCampaignName(partnerName);		// temporarily saving partner Name.
													campaignDetailDTO.setImpression(productDTO.getAllocatedImpressions());
													campaignDetailDTO.setCampaignCost(productDTO.getPayout());
												}
												calculationMap.put(partnerId, campaignDetailDTO);
											}
										}     
										//////////////
										if(calculationMap != null && calculationMap.size() > 0) {
											//log.info("calculationMap size : "+calculationMap.size());
											for(String partnerId : calculationMap.keySet()) {
												String placementName = smartCampaignPlacementObj.getPlacementName().trim();
												String partnerName = calculationMap.get(partnerId).getCampaignName(); // // temporarily saving partner Name used here
												//
												String geoTarget = getFormattedGeoTarget(smartCampaignPlacementObj);
												/*if(smartCampaignPlacementDTO.getDmas() != null) {
													geoTarget = smartCampaignPlacementDTO.getDmas();
												}*/
												/*if(smartCampaignPlacementObj.getGeoObj() != null && smartCampaignPlacementObj.getGeoObj().size() > 0) {
													for(GeoTargetsObj geoTargetsObj : smartCampaignPlacementObj.getGeoObj()) {
														geoTarget = geoTarget + "["+geoTargetsObj.getGeoTargetsName()+"], ";
													}
													geoTarget = StringUtil.deleteFromLastOccurence(geoTarget, ",");
												}*/
												if(smartCampaignPlacementObj.getGeoObj() != null && smartCampaignPlacementObj.getGeoObj().size() > 0) {
													for(GeoTargetsObj geoTargetsObj : smartCampaignPlacementObj.getGeoObj()) {
														dmaMap.put(geoTargetsObj.getGeoTargetsName(), null);
													}
												}
												if(smartCampaignPlacementObj.getFrequencyCap() != null && smartCampaignPlacementObj.getFrequencyCap().trim().length() > 0 && LinMobileUtil.isNumeric(smartCampaignPlacementObj.getFrequencyCap().trim())  && smartCampaignPlacementObj.getFrequencyCapUnit() != null) {
													if(smartCampaignPlacementObj.getFrequencyCapUnit().trim().equalsIgnoreCase("Day")) {
														frequencyCapMap.put(smartCampaignPlacementObj.getFrequencyCap() +" x 24", null);
													}else {
														frequencyCapMap.put(smartCampaignPlacementObj.getFrequencyCap() +" per "+smartCampaignPlacementObj.getFrequencyCapUnit(), null);
													}
												}
												if(smartCampaignPlacementObj.getGoal() != null && smartCampaignPlacementObj.getGoal().trim().length() > 0) {
													goalMap.put(smartCampaignPlacementObj.getGoal(), null);
												}
												String creatives = "";
												String richMedia = "";
												if(smartCampaignPlacementObj.getCreativeObj() != null && smartCampaignPlacementObj.getCreativeObj().size() > 0) {
													for(CreativeObj creativeObj : smartCampaignPlacementObj.getCreativeObj()) {
														if(creativeObj.getFormat().contains("Rich")) {
															richMedia = richMedia + creativeObj.getFormat() + ", ";
														}
														else {
															creatives = creatives + creativeObj.getSize()+", ";
														}
													}
													creatives = StringUtil.deleteFromLastOccurence(creatives, ",");
													if(richMedia.length() > 0) {
														richMedia = StringUtil.deleteFromLastOccurence(richMedia, ",");
													}
													else {
														richMedia = "No";
													}
												}
											
												long impressions = calculationMap.get(partnerId).getImpression();
												double rate = 0.0;
												double cost = calculationMap.get(partnerId).getCampaignCost();
												if(impressions > 0 && cost > 0) {
													rate = (double)((cost*1000)/impressions);
												}
											
												CampaignDetailDTO campaignDetailDTO = new CampaignDetailDTO(campaignName, "", placementName, dealType, geoTarget, creatives, richMedia, 
																						DateUtil.getRequiredFormatDate(smartCampaignPlacementObj.getStartDate(), "MM-dd-yyyy", "MM/dd/yyyy"), 
																						DateUtil.getRequiredFormatDate(smartCampaignPlacementObj.getEndDate(), "MM-dd-yyyy", "MM/dd/yyyy"), 
																						"", impressions, rate, cost);
												//campaignDetailDTO.setJavaScriptTag("<SCRIPT language='JavaScript1.1' SRC='http://ad.doubleclick.net/adj/N6103.1784616LINDIGITAL.COM0/B7921957.109156382;sz=300x250;ord=[timestamp]?'></SCRIPT>");
												
												
												
												CampaignDetailDTO updateCampaignDetailDTO=updateGPTTags(smartMediaPlanObj,
														campaignDetailDTO, smartCampaignPlacementDTO, partnerName);
												if(updateCampaignDetailDTO !=null){
													campaignDetailDTO=updateCampaignDetailDTO;
												}
												
												
												log.info("CampaignDetailDTO : "+campaignDetailDTO);
												if(campaignDetailDTOListMap.containsKey(partnerId+"<SEP>"+partnerName)) {
													log.info("campaignDetailDTOListMap Contains key : "+partnerId+"<SEP>"+partnerName);
													 List<CampaignDetailDTO> campaignDetailDTOList = campaignDetailDTOListMap.get(partnerId+"<SEP>"+partnerName);
													 campaignDetailDTOList.add(campaignDetailDTO);
												}
												else {
													log.info("campaignDetailDTOListMap no key : "+partnerId+"<SEP>"+partnerName);
													List<CampaignDetailDTO> campaignDetailDTOList = new ArrayList<CampaignDetailDTO>();
													 campaignDetailDTOList.add(campaignDetailDTO);
													 campaignDetailDTOListMap.put(partnerId+"<SEP>"+partnerName, campaignDetailDTOList);
												}
											}
										}
									}
								}
							}
						}
					}
					
					log.info("creating ClientIOReportDTO");
					if(partnerNameFound) {
						for(String key : campaignDetailDTOListMap.keySet()) {
							String publisherId = key.split("<SEP>")[0];
							String partnerName = key.split("<SEP>")[1];
							log.info("campaignDetailDTOListMap current key : "+publisherId);
							CompanyObj companyObj = null;
							if(!(publisherId.equals("0")) && companyMap.containsKey(publisherId)) {
								companyObj = companyMap.get(publisherId);
								partnerName = companyObj.getCompanyName();
								log.info("companyMap Contains key : "+publisherId);
							}
							else if(publisherId.equals("0") || publisherId.indexOf("0_")>=0) {
								companyObj = new CompanyObj();
								companyObj.setCompanyName(partnerName);
							}
	
							if(companyObj != null) {
								ClientIOReportDTO clientIOReportDTO = new ClientIOReportDTO();
								if(campaignObj.getAdvertiserId() != null && campaignObj.getAdvertiserId().trim().length() > 0 && LinMobileUtil.isNumeric(campaignObj.getAdvertiserId()) && StringUtil.getLongValue(campaignObj.getAdvertiserId()) > 0) {
									//getAdvertiser = false;
									AdvertiserObj advertiserObj = mediaPlanDAO.loadAdvertiser(Long.valueOf(campaignObj.getAdvertiserId()));
									if(advertiserObj != null) {
										log.info(advertiserObj.toString());
										clientIOReportDTO.setAdvertiseCompanyName(advertiserObj.getName());
										clientIOReportDTO.setAdvertiseContact(advertiserObj.getContactPersonName());
										clientIOReportDTO.setAdvertiseAddress(advertiserObj.getAddress());
										clientIOReportDTO.setAdvertiseEmail(advertiserObj.getEmail());
										clientIOReportDTO.setAdvertisePhone(advertiserObj.getPhone());
										clientIOReportDTO.setAdvertiseFax(advertiserObj.getFax());
									}
									else {
										log.severe("AdvertiserObj does not exist for advertiser id : "+campaignObj.getAdvertiserId());
									}
								}
								/*AccountsEntity[] accountEntityArr = getAccountsForMediaPlan(campaignObj.getAdvertiserId(), campaignObj.getAgencyId(), publisherIdInBQ, getAdvertiser, false);
								if(getAdvertiser && accountEntityArr != null && accountEntityArr.length>=1) {
									clientIOReportDTO.setAdvertiseCompanyName(accountEntityArr[0].getAccountName().replace(" ("+LinMobileConstants.ADVERTISER_ID_PREFIX+")", ""));
									clientIOReportDTO.setAdvertiseContact(accountEntityArr[0].getContact());
									clientIOReportDTO.setAdvertiseAddress("");
									clientIOReportDTO.setAdvertiseEmail(accountEntityArr[0].getEmail());
									clientIOReportDTO.setAdvertisePhone(accountEntityArr[0].getPhone());
									clientIOReportDTO.setAdvertiseFax(accountEntityArr[0].getFax());
								}*/
								
								clientIOReportDTO.setAgencyCompanyName(companyObj.getCompanyName());
								clientIOReportDTO.setAgencyContact(companyObj.getContactPersonName());
								clientIOReportDTO.setAgencyAddress(companyObj.getCompanyAddress());
								clientIOReportDTO.setAgencyEmail(companyObj.getCompanyEmail());
								clientIOReportDTO.setAgencyPhone(companyObj.getPhone());
								clientIOReportDTO.setAgencyFax(companyObj.getFax());
							
								clientIOReportDTO.setAdvertiserBasisForBilling("LIN Mobile Ad Server");
								clientIOReportDTO.setAdvertiserPaymentTerms("NET 30");
								//clientIOReportDTO.setAdvertiserCollectionType("NET");
								clientIOReportDTO.setAdvertiserCancellationPolicy("2 business days");
								
								
								clientIOReportDTO.setInsertionOrderDate(DateUtil.getFormatedDate(new Date(), "MM/dd/yyyy"));
								/*clientIOReportDTO.setInsertionOrderStation("station Name");
								clientIOReportDTO.setAccountExecutive("Kevin Wassong");
								clientIOReportDTO.setAccountExecutiveEmail("kevin.wassong@linmedia.com");*/
								clientIOReportDTO.setAccountExecutivePhone("212-729-4345");
								clientIOReportDTO.setAccountExecutiveFax("1234-1234");
								/*clientIOReportDTO.setAccountManager("Amina Alliyu");
								clientIOReportDTO.setAccountManagerEmail("amina.alliyu@linmedia.com");*/
								clientIOReportDTO.setAccountManagerPhone("917-720-0313");
								clientIOReportDTO.setAccountManagerFax("1235-1235");
								 
								clientIOReportDTO.setBillingNotes("");
								clientIOReportDTO.setCampaignDetailDTOList(campaignDetailDTOListMap.get(key));
								
								if(dmaMap != null && dmaMap.size() > 0) {
									String dmas = "";
									for(String dma : dmaMap.keySet()) {
										dmas = dmas + "["+dma+"], ";
									}
									dmas = StringUtil.deleteFromLastOccurence(dmas, ",");
									clientIOReportDTO.setCampaignDMA(dmas);
								}
								
								if(goalMap != null && goalMap.size() > 0) {
									String goals =  "";
									for(String goal : goalMap.keySet()) {
										goals = goals + goal+"%, ";
									}
									goals = StringUtil.deleteFromLastOccurence(goals, ",");
									clientIOReportDTO.setCampaignGoal(goals);
								}
								
								if(frequencyCapMap != null && frequencyCapMap.size() > 0) {
									String frequencyCaps =  "";
									for(String frequencyCap : frequencyCapMap.keySet()) {
										frequencyCaps = frequencyCaps + frequencyCap+", ";
									}
									frequencyCaps = StringUtil.deleteFromLastOccurence(frequencyCaps, ",");
									clientIOReportDTO.setFrequencyCap(frequencyCaps);
								}
								
								clientIOReportDTO.setCampaignNote(campaignObj.getNotes());
								clientIOReportDTOList.add(clientIOReportDTO);
								publisherNameList.add(partnerName);
							}
						}
					}else {
						publisherNameList.add("No partner name in any line item card.");
						campaignName = "No partner name in any line item card.";
						log.warning("No partner name in any product card.");
					}
				}
			}
		}catch(Exception e) {
			log.severe("Exception in POExcelReport of MediaPlanService : "+e.getMessage());
			e.printStackTrace();
			//throw e;
		}
		excelMap.put("partnerNameFound", partnerNameFound);
		excelMap.put("dataBean", clientIOReportDTOList);
		excelMap.put("sheetName", publisherNameList);
		excelMap.put("campaignName", campaignName);
		return excelMap;
	}

	private CampaignDetailDTO updateGPTTags(SmartMediaPlanObj smartMediaPlanObj, CampaignDetailDTO campaignDetailDTO, 
			SmartCampaignPlacementDTO smartCampaignPlacementDTO, String partnerName){
	
		if(smartMediaPlanObj !=null && campaignDetailDTO !=null){
			log.info("Load gpt tags from smartMediaPlanObj......");
			List<JavaScriptTagDTO> tagList1 = new ArrayList<JavaScriptTagDTO>();
			List<JavaScriptTagDTO> tagList2 = new ArrayList<JavaScriptTagDTO>();
			
			log.info("smartMediaPlanObj.getDfpPlacements() = "+smartMediaPlanObj.getDfpPlacements());
			if(smartMediaPlanObj.getDfpPlacements() != null && smartMediaPlanObj.getDfpPlacements().size() > 0){
			for(SmartCampaignPlacementDTO smartCampaignDFPPlacementDTO : smartMediaPlanObj.getDfpPlacements()){
				if(smartCampaignPlacementDTO.getId()==smartCampaignDFPPlacementDTO.getId() &&
						smartCampaignDFPPlacementDTO.getPartnerName() != null && smartCampaignDFPPlacementDTO.getPartnerName().equals(partnerName)){
					if(smartCampaignDFPPlacementDTO.getGptTag() != null) {
						log.info("adding .....");
						String productName=smartCampaignDFPPlacementDTO.getProductName();
						if(productName ==null){
							productName="";
						}
						
						String gptTagsArray[] = smartCampaignDFPPlacementDTO.getGptTag().split("\\|");
						for(String tag : gptTagsArray){
							JavaScriptTagDTO tagDTO = new JavaScriptTagDTO();
							String creativeSize="";
							if(tag.indexOf("[") != -1 && tag.indexOf("]") != -1){
									creativeSize = tag.substring(tag.indexOf("["), tag.indexOf("]")+1);
							}
							log.info("CREATIVE SIZE = "+creativeSize);
							tagDTO.setCampaignName(campaignDetailDTO.getCampaignName()+" | "+campaignDetailDTO.getPlacement()+" | "+productName+" | "+creativeSize);
							
							if(tag!=null && tag.length() > 0 && tag.contains("CLICK_URL_UNESC")){
								tagDTO.setTagForDFP(tag);
								tagList1.add(tagDTO);
								
							}else if(tag!=null && tag.length() > 0 && (!tag.contains("CLICK_URL_UNESC"))){
								tagDTO.setTagForDFP(tag);
								tagList2.add(tagDTO);
							}
							
						}
						
					}else{
						log.info("No placement DTO available..................................");
					}
				}
				else {
					log.info("Do not add tag for this partner : "+smartCampaignDFPPlacementDTO.getPartnerName());
				}
			}
		 }
	    log.info("tagList1(DFPtags) : "+tagList1.size()+" and tagList2(non-dfp tags) : "+tagList2.size());
		campaignDetailDTO.setJavaScriptTagListForDfp(tagList1);
		campaignDetailDTO.setJavaScriptTagListForNonDfp(tagList2);
		log.info("Tag has been added in list...");
		}else{
			log.info("smartMediaPlanObj or campaignDetailDTO is null.....");
		}
		return campaignDetailDTO;
	}
	
	private String getFormattedGeoTarget(SmartCampaignPlacementObj smartCampaignPlacementObj) {
		String geoTarget = "";
		/*private List<GeoTargetsObj> geoObj;*/
		if(smartCampaignPlacementObj != null) {
			if(smartCampaignPlacementObj.getCountryObj() != null && smartCampaignPlacementObj.getCountryObj().size() > 0) {
				for(CountryObj countryObj : smartCampaignPlacementObj.getCountryObj()) {
					if(countryObj != null && countryObj.getText() != null && countryObj.getText().trim().length() > 0)	{
						geoTarget = geoTarget + countryObj.getText().trim()+", ";
					}
				}
			}
			if(smartCampaignPlacementObj.getStateObj() != null && smartCampaignPlacementObj.getStateObj().size() > 0) {
				for(StateObj obj : smartCampaignPlacementObj.getStateObj()) {
					if(obj != null && obj.getText() != null && obj.getText().trim().length() > 0)	{
						geoTarget = geoTarget + obj.getText().trim()+", ";
					}
				}
			}
			if(smartCampaignPlacementObj.getCityObj() != null && smartCampaignPlacementObj.getCityObj().size() > 0) {
				for(CityDTO obj : smartCampaignPlacementObj.getCityObj()) {
					if(obj != null && obj.getText() != null && obj.getText().trim().length() > 0)	{
						geoTarget = geoTarget + obj.getText().trim()+", ";
					}
				}
			}
			if(smartCampaignPlacementObj.getZipObj() != null && smartCampaignPlacementObj.getZipObj().size() > 0) {
				for(ZipDTO obj : smartCampaignPlacementObj.getZipObj()) {
					if(obj != null && obj.getText() != null && obj.getText().trim().length() > 0)	{
						geoTarget = geoTarget + obj.getText().trim()+", ";
					}
				}
			}
			if(smartCampaignPlacementObj.getGeoObj() != null && smartCampaignPlacementObj.getGeoObj().size() > 0) {
				for(GeoTargetsObj obj : smartCampaignPlacementObj.getGeoObj()) {
					if(obj != null && obj.getGeoTargetsName() != null && obj.getGeoTargetsName().trim().length() > 0)	{
						geoTarget = geoTarget + "["+obj.getGeoTargetsName().trim()+"], ";
					}
				}
			}
			geoTarget = StringUtil.deleteFromLastOccurence(geoTarget, ",");
		}
		return geoTarget;
	}

	/*public AccountsEntity[] getAccountsForMediaPlan(String advertiserId, String agencyId, String publisherIdInBQ, boolean getAdvertiser, boolean getAgency) {
		AccountsEntity[] accountsEntity = new AccountsEntity[2];
		AccountsEntity advertiser = null;
		AccountsEntity agency = null;
		try {
			UserDetailsDAO userDetailsDAO = new UserDetailsDAO();
			if(getAdvertiser) {
				advertiser = userDetailsDAO.getAccountsObjByDfpAccountId(advertiserId);	// get from DataStore
				if(advertiser != null) {
					getAdvertiser = false;
					accountsEntity[0] = advertiser;
				}
			}
			if(getAgency) {
				agency = userDetailsDAO.getAccountsObjByDfpAccountId(agencyId);	// get from DataStore
				if(agency != null) {
					getAgency = false;
					accountsEntity[1] = agency;
				}
			}
			if(getAdvertiser || getAgency) {
				// get from Big Query
				QueryDTO queryDTO = getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					Map<String, String> accountMap = userDetailsDAO.loadAdvertiserAndAgencyInBQ(advertiserId, agencyId, getAdvertiser, getAgency, queryDTO);
					if(accountMap != null && accountMap.size() > 0) {
						for(String id : accountMap.keySet()) {
							AccountsEntity accountsObj = new AccountsEntity();
							accountsObj.setAccountName(accountMap.get(id));
							accountsObj.setAccountDfpId(id.replaceAll("_", ""));
							if(accountsObj.getAccountName().contains(LinMobileConstants.ADVERTISER_ID_PREFIX)) {
								accountsEntity[0] = accountsObj;
							}
							else if(accountsObj.getAccountName().contains(LinMobileConstants.AGENCY_ID_PREFIX)) {
								accountsEntity[1] = accountsObj;
							}
						}
					}
				}
			}
		} catch(Exception e) {
			log.severe("Exception in AccountsEntity in MediaPlanService : "+e.getMessage());
			
		}
		return accountsEntity;
	}*/
	
	
}