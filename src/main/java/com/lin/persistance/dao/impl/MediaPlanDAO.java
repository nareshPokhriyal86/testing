package com.lin.persistance.dao.impl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.ProposalObj;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.LinMobileVariables;


/*
 * @author Youdhveer Panwar
 * MediaPlanDAO 
 */
public class MediaPlanDAO implements IMediaPlanDAO{

	private static final Logger log=Logger.getLogger(MediaPlanDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	
	public void saveObject(Object obj) throws DataServiceException {
		obfy.save().entity(obj).now();		
	}
	
	public void deleteObject(Object obj) throws DataServiceException {
		obfy.delete().entity(obj);
	}
	
	
	/*
	 * Load all proposals from datastore by userId
	 * @param String 
	 * @return List<ProposalObj>
	 */
	public List<ProposalObj> loadAllProposals(String userId) throws DataServiceException{
		List<ProposalObj> resultList=obfy.load().type(ProposalObj.class) 
								 .filter("createdBy = ",userId)
								 .order("proposalId")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load all proposals from datastore
	 * @return List<ProposalObj>
	 */
	public List<ProposalObj> loadAllProposals() throws DataServiceException{
		List<ProposalObj> resultList=obfy.load().type(ProposalObj.class) 
								 .order("-proposalId")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load proposal by proposalId
	 * @see com.lin.persistance.dao.IMediaPlanDAO#loadProposal(long)
	 * @param long 
	 * @return ProposalObj
	 * 
	 */
	public ProposalObj loadProposal(long proposalId) throws DataServiceException{
		ProposalObj proposal=obfy.load().type(ProposalObj.class) 
									.filter("proposalId = ",proposalId)
									.first().now();
        return proposal;
	}
    
	/*
	 * Count all proposals in datastore
	 * @return long
	 */
	public long maxProposalId() throws DataServiceException{
		long maxProposalId=0;
		ProposalObj proposal=obfy.load().type(ProposalObj.class)   
		                         .order("-proposalId")
		                         .limit(1).first().now();
		if(proposal !=null ){
			maxProposalId=proposal.getProposalId();
		}
		return maxProposalId;
	}
	
		
	/*
	 * Get max GeoId from datastore
	 * @return long
	 */
	public long maxGeoTargetId() throws DataServiceException{
		long maxId=0;
		GeoTargetsObj obj=obfy.load().type(GeoTargetsObj.class)   
		                         .order("-geoTargetId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxId=obj.getGeoTargetId();
		}
		return maxId;
	}
	
	/*
	 * Get maxIndustryId from datastore
	 * @return long
	 */
	public long maxIndustryId() throws DataServiceException{
		long maxId=0;
		IndustryObj obj=obfy.load().type(IndustryObj.class)   
		                         .order("-industryId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxId=obj.getIndustryId();
		}
		return maxId;
	}
	
	/*
	 * Get max kpiId from datastore
	 * @return long
	 */
	public long maxKPIsId() throws DataServiceException{
		long maxId=0;
		KPIObj obj=obfy.load().type(KPIObj.class)   
		                         .order("-kpiId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxId=obj.getKpiId();
		}
		return maxId;
	}
	
	/*
	 * Load all advertisers from datastore
	 * @return List<AdvertisersObj>
	 */
	public List<AdvertiserObj> loadAllAdvertisers() throws DataServiceException{
		List<AdvertiserObj> resultList=obfy.load().type(AdvertiserObj.class) 
								 .order("advertiserName")
		                         .list();
		return resultList;
	}
	
	
	
	public AdvertiserObj loadAdvertiser(Long id) throws DataServiceException{
		AdvertiserObj advertiser=obfy.load().type(AdvertiserObj.class).id(id).now();
		return advertiser;
	}
	
	public AdvertiserObj loadAdvertiser(long advertiserId,String dfpNetworkCode) throws DataServiceException{
		AdvertiserObj advertiser=obfy.load().type(AdvertiserObj.class)
				.filter("advertiserId = ",advertiserId)
				.filter("dfpNetworkCode = ",dfpNetworkCode)
				.first().now();
		return advertiser;
	}
	
	public AdvertiserObj loadAdvertiser(String name,String dfpNetworkCode) throws DataServiceException{
		AdvertiserObj advertiser=obfy.load().type(AdvertiserObj.class)
				.filter("name = ",name)
				.filter("dfpNetworkCode = ",dfpNetworkCode)
				.first().now();
		return advertiser;
	}
	
	public AdvertiserObj loadAdvertiser(String name) throws DataServiceException{
		AdvertiserObj advertiser=obfy.load().type(AdvertiserObj.class)
				.filter("name = ",name)
				.first().now();
		return advertiser;
	}
	
	public AgencyObj loadAgency(Long id) throws DataServiceException{
		AgencyObj agency=obfy.load().type(AgencyObj.class).id(id).now();
		return agency;
	}
	
	public AgencyObj loadAgency(long agencyId,String dfpNetworkCode) throws DataServiceException{
		AgencyObj agency=obfy.load().type(AgencyObj.class)
				.filter("agencyId = ",agencyId)
				.filter("dfpNetworkCode = ",dfpNetworkCode)
				.first().now();
		return agency;
	}
	
	public AgencyObj loadAgency(String name,String dfpNetworkCode) throws DataServiceException{
		AgencyObj agency=obfy.load().type(AgencyObj.class)
				.filter("name = ",name)
				.filter("dfpNetworkCode = ",dfpNetworkCode)
				.first().now();
		return agency;
	}
	
	public AgencyObj loadAgency(String name) throws DataServiceException{
		AgencyObj agency=obfy.load().type(AgencyObj.class)
				.filter("name = ",name)
				.first().now();
		return agency;
	}
	
	/*
	 * Load all agencies from datastore
	 * @return List<AgenciesObj>
	 */
	public List<AgencyObj> loadAllAgencies() throws DataServiceException{
		List<AgencyObj> resultList=obfy.load().type(AgencyObj.class) 
								 .order("agencyName")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load all DMAs(GeoTargets) from datastore
	 * @return List<AgenciesObj>
	 */
	public List<GeoTargetsObj> loadAllGeoTargets() throws DataServiceException{
		List<GeoTargetsObj> resultList=obfy.load().type(GeoTargetsObj.class) 
								 .order("geoTargetsName")
		                         .list();
		return resultList;
	}
	
	
	/*
	 * loadAllIndustries from datastore
	 * @return List<IndustryObj>
	 */
	public List<IndustryObj> loadAllIndustries() throws DataServiceException{
		List<IndustryObj> resultList=obfy.load().type(IndustryObj.class) 
								 .order("industryName")
		                         .list();
		return resultList;
	}
	
	
	/*
	 * Load all KPIs from datastore
	 * @return List<KPIObj>
	 */
	public List<KPIObj> loadAllKPIs() throws DataServiceException{
		List<KPIObj> resultList=obfy.load().type(KPIObj.class) 
								 .order("kpiName")
		                         .list();
		return resultList;
	}
	
	
	/*
	 * Load all placements by proposalId
	 * @param Long 
	 * @return List<PlacementObj>
	 */
	/*public List<PlacementObj> loadAllPlacements(long proposalId) throws DataServiceException{
		List<PlacementObj> resultList=obfy.load().type(PlacementObj.class) 
								 .filter("proposalId = ",proposalId)
								 .order("siteNumberPerPlacement")
		                         .list();
		return resultList;
	}*/
	public List<PlacementObj> loadAllPlacements(long proposalId) throws DataServiceException{
		List<PlacementObj> resultList=obfy.load().type(PlacementObj.class) 
								 .filter("proposalId = ",proposalId)
								 .order("placementName")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load all placements by placementId
	 * @param Long 
	 * @return List<PlacementObj>
	 */
	public List<PlacementObj> loadAllPlacements(String placementId) throws DataServiceException{
		List<PlacementObj> resultList=obfy.load().type(PlacementObj.class) 
								 .filter("placementId = ",placementId)
								 .order("siteNumberPerPlacement")
		                         .list();
		return resultList;
	}
	
	/*
	 * load placement->site  by siteId
	 * @param String	
	 */
	public PlacementObj loadPlacementBySite(String siteId) throws DataServiceException{
		PlacementObj placement=obfy.load().type(PlacementObj.class) 
								 .filter("siteId = ",siteId)
		                         .first().now();
		return placement;
	}
	
	/*
	 * Get maxNumber from datastore
	 * @param long - proposalId
	 * @return long
	 */
	public long maxSiteNumberPerPlacement(long proposalId) throws DataServiceException{
		long maxNumber=0;
		PlacementObj obj=obfy.load().type(PlacementObj.class)   
								 .filter("proposalId = ",proposalId)
		                         .order("-siteNumberPerPlacement")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxNumber=obj.getSiteNumberPerPlacement();
		}
		return maxNumber;
	}
	
	
	/*
	 * Get max placement number from datastore
	 * @param long - proposalId
	 * @return long
	 */
	public long maxPlacementNumberPerProposal(long proposalId) throws DataServiceException{
		long maxNumber=0;
		PlacementObj obj=obfy.load().type(PlacementObj.class)   
								 .filter("proposalId = ",proposalId)
		                         .order("-placementNumber")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxNumber=obj.getPlacementNumber();
		}
		return maxNumber;
	}
	
	
	/*
	 * delete a placement by siteId
	 * @param String	
	 */
	public void deletePlacementBySite(String siteId) throws DataServiceException{
		PlacementObj placement=obfy.load().type(PlacementObj.class) 
								 .filter("siteId = ",siteId)
		                         .first().now();
		obfy.delete().entity(placement);
		log.info("Placement->site deleted successfully  :siteId:"+siteId);
	}
	
	/*
	 * delete a placement by placementId
	 * @param String -placementId
	 */
	public void deletePlacement(String placementId) throws DataServiceException{
		List<PlacementObj> resultList=obfy.load().type(PlacementObj.class) 
								 .filter("placementId = ",placementId)
		                         .list();
		obfy.delete().entity(resultList);
		log.info("Placement deleted successfully  :placementId:"+placementId);
	}
	
	/*
	 * delete a proposal by proposalId
	 * @param String
	 */
	public void deleteProposal(long proposalId) throws DataServiceException{
		ProposalObj proposal=obfy.load().type(ProposalObj.class) 
								 .filter("proposalId = ",proposalId)
		                         .first().now();
		obfy.delete().entity(proposal);
		deleteAllPlacementsByProposalId(proposalId);
		log.info("Proposal deleted successfully :proposalId :"+proposalId);
	}
	
	/*
	 * delete all placements by proposalId
	 * @param String	
	 */
	public void deleteAllPlacementsByProposalId(long proposalId) throws DataServiceException{
		List<PlacementObj> resultList=obfy.load().type(PlacementObj.class) 
								 .filter("proposalId = ",proposalId)
		                         .list();
		obfy.delete().entity(resultList);
		log.info("All placements for proposalId "+proposalId+" deleted successfully");
	}
	
	
	/*
	 * Get max adSizeId from datastore
	 * @return long
	 */
	public long maxAdSizeId() throws DataServiceException{
		long maxId=0;
		AdSizeObj obj=obfy.load().type(AdSizeObj.class)   
		                         .order("-adSizeId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxId=obj.getAdSizeId();
		}
		return maxId;
	}
	
	
	/*
	 * Get max adFormatId from datastore
	 * @return long
	 */
	public long maxAdFormatId() throws DataServiceException{
		long maxId=0;
		AdFormatObj obj=obfy.load().type(AdFormatObj.class)   
		                         .order("-adFormatId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxId=obj.getAdFormatId();
		}
		return maxId;
	}
	
	/*
	 * Load all AdSizeObj from datastore
	 * @return List<AdSizeObj>
	 */
	public List<AdSizeObj> loadAllAdSize() throws DataServiceException{
		List<AdSizeObj> resultList=obfy.load().type(AdSizeObj.class) 
								 .order("adSize")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load all AdFormatObj from datastore
	 * @return List<AdFormatObj>
	 */
	public List<AdFormatObj> loadAllAdFormats() throws DataServiceException{
		List<AdFormatObj> resultList=obfy.load().type(AdFormatObj.class) 
								 .order("adFormatName")
		                         .list();
		return resultList;
	}
	
	/*
	 * Load forecasting data for a site from bigquery
	 * @param String siteName,String startDate,String endDate
	 * @return SellThroughDataObj
	 */
	public SellThroughDataObj loadForecastingDataBySiteName(String startDate,String endDate,String siteName) throws DataServiceException, IOException{
		SellThroughDataObj sellThroughObj=new SellThroughDataObj();
		if(startDate !=null && startDate.indexOf("00:00:00")< 0){
			startDate=startDate+" 00:00:00";
		}
		if(endDate !=null && endDate.indexOf("00:00:00")< 0){
			endDate=endDate+" 00:00:00";
		}
		StringBuffer query=new StringBuffer();
		query.append(" SELECT c.site_name as site_name,SUM(ifnull(c.available_impressions,0)) ");
		query.append(" as available_impressions, SUM(ifnull(c.forecasted_impressions,0)) as forecasted_impressions, ");
		query.append(" SUM(ifnull(c.reserved_impressions,0)) as reserved_impressions ");
		query.append(" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID+".Sell_Through] c JOIN EACH ");
		query.append(" (SELECT  frequency, site_name, creative_size, start_date, end_date,pubisher_id ");
		query.append(" ,data_source, max(load_timestamp) as load_timestamp ");
		query.append(" FROM ["+LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID+".Sell_Through] ");
		query.append(" WHERE Site_Name='"+siteName+"' AND start_date >='"+startDate+"'  AND end_date <='"+endDate+"' ");
		query.append(" GROUP EACH BY frequency, site_name, creative_size, start_date, end_date,pubisher_id,data_source ");
		query.append(" ignore case )d ");
		query.append(" on c.frequency = d.frequency ");
		query.append(" AND c.site_name = d.site_name ");
		query.append(" AND c.load_timestamp = d.load_timestamp ");
		query.append(" AND c.start_date = d.start_date ");
		query.append(" AND c.end_date = d.end_date ");
		query.append(" AND c.creative_size = d.creative_size ");
		query.append(" AND c.data_source = d.data_source ");
		query.append(" GROUP BY site_name ");
		query.append(" ORDER BY site_name ");
		query.append(" ignore case ");
		log.info("Query:"+query.toString());
		
		
		QueryResponse queryResponse = null;
		
		int j = 0;
		do {
			log.info("Going to fetch forecasting date : query attempts:"+(j+1)+" out of max attempts: 3");
			queryResponse = BigQueryUtil.getBigQueryData(query.toString()); //jclServiceAccounts.getBigQueryData(query.toString());
			log.info("queryResponse.getJobComplete() : "+queryResponse.getJobComplete());			
			j++;
		} while (!queryResponse.getJobComplete() && j <= 3);

		if (queryResponse != null && queryResponse.getRows() != null) {
			List<TableRow> rowList = queryResponse.getRows();
            log.info("total row results found :"+rowList.size());
			for (TableRow row : rowList) {
				List<TableCell> cellList = row.getF();
				
				sellThroughObj.setAdUnit(cellList.get(0).getV().toString());
				sellThroughObj.setAvailableImpressions(cellList.get(1).getV().toString());
				sellThroughObj.setForecastedImpressions(cellList.get(2).getV().toString());
				sellThroughObj.setReservedImpressions(cellList.get(3).getV().toString());
			}

		}
		return sellThroughObj;		
	}

	@Override
	public List<DropdownDataObj> getDropdownDataObj() throws Exception {
		List<DropdownDataObj> resultList = obfy.load().type(DropdownDataObj.class).list();		
		if(resultList !=null && resultList.size() > 0){
			log.info("DropdownDataObj resultList:"+resultList.size());
		}else{
			log.info("DropdownDataObj resultList :Null");
		}	
		return resultList;
	}

	@Override
	public SmartMediaPlanObj getSmartMediaPlanObj(long mediaPlanId) throws DataServiceException {
		SmartMediaPlanObj mediaPlanObj = null;	
		if(mediaPlanId > 0){
			mediaPlanObj = obfy.load().type(SmartMediaPlanObj.class)
										.id(mediaPlanId).now();
		}
		return mediaPlanObj;
	}
	
	public SmartMediaPlanObj loadMediaPlan(String campaignId) throws DataServiceException{
		SmartMediaPlanObj smarMediaPlan=obfy.load().type(SmartMediaPlanObj.class) 
									.filter("campaignId = ",campaignId.trim())
									.filter("active =",1)
									.first().now();
        return smarMediaPlan;
	}
	
	public List<SmartMediaPlanObj> loadAllMediaPlans(String campaignId) throws DataServiceException{
		List<SmartMediaPlanObj> resultList=obfy.load().type(SmartMediaPlanObj.class) 
												.filter("campaignId = ",campaignId.trim())
												.list();
		return resultList;
	}
	
	public SmartMediaPlanObj loadMediaPlan(String campaignId,int active) throws DataServiceException{
		SmartMediaPlanObj smarMediaPlan=obfy.load().type(SmartMediaPlanObj.class) 
									.filter("campaignId = ",campaignId.trim())
									.filter("active =",active)
									.first().now();
        return smarMediaPlan;
	}
	
	public SmartMediaPlanObj loadDFPNetworkCode(String orderId) throws DataServiceException{
		SmartMediaPlanObj smarMediaPlan=obfy.load().type(SmartMediaPlanObj.class) 
									.filter("dfpOrderId = ",orderId.trim())
									.filter("active =",1)
									.first().now();
        return smarMediaPlan;
	}

	
}
