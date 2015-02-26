package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.google.gdata.data.analytics.AccountEntry;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cmd.Query;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
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
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignFlightObj;
import com.lin.server.bean.SmartCampaignHistObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;

public class SmartCampaignPlannerDAO implements ISmartCampaignPlannerDAO {

	private static final Logger log = Logger.getLogger(SmartCampaignPlannerDAO.class.getName());
	private Objectify obfy = OfyService.ofy();	

	private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);

	@Override
	public void saveObject(Object obj) throws DataServiceException{	
		obfy.save().entity(obj).now();
		obfy.clear();
	}

	@Override
	public void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException{
		strongObfy.save().entity(obj).now();
	}
	@Override
	public void savePlacementObjectList(List<SmartCampaignPlacementObj> list) throws DataServiceException{	
		obfy.save().entities(list).now();
		obfy.clear();
	}
	@Override
	public void saveFlightObjectList(List<SmartCampaignFlightObj> list) throws DataServiceException{	
		obfy.save().entities(list).now();
	}

	@Override
	public void deleteObject(Object obj) throws DataServiceException{	
		obfy.delete().entity(obj).now();
		log.info("Object deleted successfully from datastore.");
		obfy.clear();
	}

	@Override
	public List<SmartCampaignFlightObj> getAllFlightsOfPlacement(long placementId) throws DataServiceException {
		log.info("Inside getAllFlightsOfPlacement of SmartCampaignPlannerDAO");
		obfy.clear();
		Key<SmartCampaignPlacementObj> parent = Key.create(SmartCampaignPlacementObj.class, placementId);
		List<SmartCampaignFlightObj> flightList = obfy.load().type(SmartCampaignFlightObj.class)
				.ancestor(parent)
				.list();
		if(flightList != null && flightList.size() > 0) {
			log.info("flightList size : " +flightList.size());
			return flightList;
		}
		else{
			log.info("flightList :Null");
			return null;
		}

	}

	@Override
	public List<SmartCampaignPlacementObj> getAllPlacementOfCampaign(long campaignId) throws DataServiceException {
		obfy.clear();
		Key<SmartCampaignObj> parent = Key.create(SmartCampaignObj.class, campaignId);
		List<SmartCampaignPlacementObj> placementList = obfy.load().type(SmartCampaignPlacementObj.class)
				.ancestor(parent)
				.list();
		return placementList;

	}

	@Override
	public List<SmartCampaignPlacementObj> getPlacementObjList(long placementId) throws DataServiceException {
		log.info("Inside getPlacementObj of SmartCampaignPlannerDAO");
		obfy.clear();
		List<SmartCampaignPlacementObj> objList = obfy.load().type(SmartCampaignPlacementObj.class)
				.filter("placementId = ", placementId).list();
		return objList;

	}

	@Override
	public  List<SmartCampaignObj>  getSmartCampaignListSuperUser(String campaignStatus) throws Exception{
		log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		obfy.clear();
		List<SmartCampaignObj> cmapaignList = new ArrayList<>();
		if(campaignStatus.equals("0")){
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					// .order("lastUpdatedOn")
					.list();
		}else if(campaignStatus.equals("1")){
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					// .filter("adServerId = ",adServerId)
					.filter("campaignStatus != ",	"6")
					//.order("lastUpdatedOn")
					.list();
		} 
		else{
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					//  	.filter("adServerId = ",adServerId)
					.filter("campaignStatus = ",	campaignStatus)
					// .order("lastUpdatedOn")
					.list();
		}
		return cmapaignList;
	}

	@Override
	public  List<SmartCampaignObj>  getSmartCampaignList(String campaignStatus, String companyId) throws Exception{
		log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		obfy.clear();
		List<SmartCampaignObj> cmapaignList = new ArrayList<>();
		if(campaignStatus.equals("0")){
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					// .order("lastUpdatedOn")
					.filter("companyId = ",companyId)
					.list();
		}else if(campaignStatus.equals("1")){
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					// .filter("adServerId = ",adServerId)
					.filter("companyId = ",companyId)
					.filter("campaignStatus != ",	"6")
					//.order("lastUpdatedOn")
					.list();
		} 
		else{
			cmapaignList = obfy.load().type(SmartCampaignObj.class)
					//  	.filter("adServerId = ",adServerId)
					.filter("companyId = ",companyId)
					.filter("campaignStatus = ",	campaignStatus)

					// .order("lastUpdatedOn")
					.list();
		}
		return cmapaignList;
	}

	@Override
	public  List<SmartCampaignObj>  getAllCampaign() throws DataServiceException{
		log.info("Inside getAllCampaign of SmartCampaignPlannerDAO");
		obfy.clear();
		List<SmartCampaignObj> campaignList = obfy.load().type(SmartCampaignObj.class)
				.list();
		return campaignList;
	}

	@Override
	public  List<SmartCampaignObj>  getAllCampaignForDataUpload() throws DataServiceException{
		log.info("Inside getAllCampaignForDataUpload of SmartCampaignPlannerDAO");
		List<SmartCampaignObj> campaignList = obfy.load().type(SmartCampaignObj.class)
				.filter("campaignStatus = ", CampaignStatusEnum.Running.ordinal()+"")
				//.filter("edate >= " , DateUtil.getCurrentTimeStamp("MM-dd-yyyy"))
				.list();
		return campaignList;
	}

	@Override
	public  List<DropdownDataObj>  getDropDownDataList(String valueType) throws Exception{
		log.info("Inside getSmartCampaignList of SmartCampaignPlannerDAO");
		List<DropdownDataObj> dataList = obfy.load().type(DropdownDataObj.class)
				.filter("valueType = ", valueType)
				.order("objectId")
				.list();
		return dataList;

	}

	@Override
	public CreativeObj getCreativeById(String id) throws Exception{
		log.info("Inside getCreativeById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		CreativeObj obj = obfy.load().type(CreativeObj.class)
				.id(lId).now();
		return obj;

	}

	@Override
	public List<CountryObj> getAllCountry() throws DataServiceException{
		log.info("Inside getAllCountry of ProductDAO");

		List<CountryObj> resultList = obfy.load().type(CountryObj.class).list();
		if(resultList != null && resultList.size() > 0) {
			log.info("CountryObj resultList : "+resultList.size());
			return resultList;
		}
		log.info("resultList :Null");
		return null;
	}

	@Override
	public DeviceObj getDeviceById(String id) throws Exception{
		log.info("Inside getDeviceById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		DeviceObj obj = obfy.load().type(DeviceObj.class)
				.id(lId).now();
		return obj;

	}

/*	@Override
	public AgencyObj getAgencyById(String id) throws Exception{
		log.info("Inside getAgencyById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		AgencyObj obj = obfy.load().type(AgencyObj.class)
				.id(lId).now();
		return obj;

	}*/
	@Override
	public AdvertiserObj getAdvertiserById(long id) throws Exception{
		log.info("Inside getAdvertiserById of SmartCampaignPlannerDAO");
		AdvertiserObj obj = obfy.load().type(AdvertiserObj.class)
				.filter("advertiserId = ",id).first().now();
		return obj;

	}

/*
	

	@Override
	public AgencyObj getAgencyById(long id) throws Exception{
		log.info("Inside getAgencyById of SmartCampaignPlannerDAO");
		AgencyObj obj = obfy.load().type(AgencyObj.class)
				.filter("agencyId = ",id).first().now();
		return obj;

	}*/

	@Override
	public GeoTargetsObj getGeoTargetById(String id) throws Exception{
		log.info("Inside getDeviceById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		GeoTargetsObj obj = obfy.load().type(GeoTargetsObj.class)
				.id(lId).now();
		return obj;

	}

	@Override
	public PlatformObj getPlatformById(String id) throws Exception{
		log.info("Inside getDeviceById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		PlatformObj obj = obfy.load().type(PlatformObj.class)
				.id(lId).now();
		return obj;

	}

	@Override
	public DropdownDataObj getRateTypeById(long objectId,String valueType) throws Exception{
		log.info("Inside getRateTypeById of SmartCampaignPlannerDAO");
		//long lId = Long.parseLong(id);
		DropdownDataObj obj = obfy.load().type(DropdownDataObj.class)
				.filter("objectId = ",objectId)
				.filter("valueType = ", valueType)
				.list().get(0);
		return obj;

	}

	@Override
	public DropdownDataObj getEducationById(String id) throws Exception{
		log.info("Inside getEducationById of SmartCampaignPlannerDAO");
		long lId = Long.parseLong(id);
		DropdownDataObj obj = obfy.load().type(DropdownDataObj.class)
				.id(lId).now();
		return obj;

	}

	/*
	 * @author Youdhveer Panwar
	 * Load all value types for rate - CPM/CPC/CPD
	 */
	public List<DropdownDataObj> getRateTypeByValue(String valueType) throws DataServiceException{
		List<DropdownDataObj> dropDownRateTypeList = obfy.load().type(DropdownDataObj.class)
				.filter("valueType = ", valueType)
				.list();
		return dropDownRateTypeList;		
	}

	@Override
	public StateObj getStateById(long id, long parenId) throws DataServiceException{
		if(id > 0 && parenId > 0) {
			Key<CountryObj> parent = Key.create(CountryObj.class, parenId);
			Key<StateObj> key = Key.create(parent,StateObj.class, id);
			return(StateObj) obfy.load().filterKey(key).first().now();
		}
		return null;
	}

	@Override
	public SmartCampaignObj getCampaignById(long id) throws DataServiceException{
		log.info("Inside getCampaignById of SmartCampaignPlannerDAO");
		SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
				.id(id).now();
		return obj;
	}

	/*
	 * Get max campaignId from datastore
	 * @return long
	 */
	@Override
	public long maxCampaignId() throws DataServiceException {
		long maxNumber=0;
		SmartCampaignObj obj=obfy.load().type(SmartCampaignObj.class)
				.order("-campaignId")
				.limit(1).first().now();
		if(obj !=null ){
			maxNumber = obj.getCampaignId();
		}
		return maxNumber;
	}

	/*
	 * Get max placementId from datastore
	 * @return long
	 */
	@Override
	public long maxPlacementId() throws DataServiceException {
		long maxNumber=0;
		SmartCampaignPlacementObj obj=obfy.load().type(SmartCampaignPlacementObj.class)
				.order("-placementId")
				.limit(1).first().now();
		if(obj !=null ){
			maxNumber = obj.getPlacementId();
		}
		return maxNumber;
	}

	/*
	 * Get max flightId from datastore
	 * @return long
	 */
	@Override
	public long maxFlight() throws DataServiceException {
		long maxNumber=0;
		SmartCampaignFlightObj obj=obfy.load().type(SmartCampaignFlightObj.class)
				.order("-flightid")
				.limit(1).first().now();
		if(obj !=null ){
			maxNumber = obj.getFlightid();
		}
		return maxNumber;
	}

	@Override
	public void deletePlacementList(List<SmartCampaignPlacementObj> placementList) throws DataServiceException{	
		obfy.delete().entities(placementList);
		log.info("Object deleted successfully from datastore.");
	}
	@Override
	public void deleteFlightList(List<SmartCampaignFlightObj> flightList) throws DataServiceException{	
		obfy.delete().entities(flightList);
		log.info("Object deleted successfully from datastore.");
	}

	@Override
	public IABContextObj getIABContextById(long id) throws DataServiceException{
		log.info("Inside getIABContext of ProductDAO");
		IABContextObj result = obfy.load().type(IABContextObj.class)
				.id(id).now();
		return result;
	}

	@Override
	public CountryObj getCountryById(long id) throws DataServiceException{
		log.info("Inside getCountryById of ProductDAO");
		CountryObj result = obfy.load().type(CountryObj.class)
				.id(id).now();
		return result;
	}

	@Override
	public SmartCampaignPlacementObj getPlacementById(long placementId) throws DataServiceException{
		log.info("Inside campaignDAO of campaignDAO");
		SmartCampaignPlacementObj result = obfy.load().type(SmartCampaignPlacementObj.class)
				.filter("placementId = ",placementId)
				.list().get(0);
		return result;
	}

	@Override
	public List<AdvertiserObj> getAllAdvertiserByAdServerId(String adServerId)throws Exception{
		log.info("Inside getAllAdvertiserByAdServerId of campaignDAO");
		if(adServerId!=null && !adServerId.equals("0")){
			List<AdvertiserObj> result = obfy.load().type(AdvertiserObj.class)
					.filter("dfpNetworkCode = ",adServerId).list();
			return result;
		}else{
			List<AdvertiserObj> result = obfy.load().type(AdvertiserObj.class)
					.list();
			return result;
		}

	}

	@Override
	public List<AccountsEntity> getAllAccounts()throws Exception{
		List<AccountsEntity> result = obfy.load().type(AccountsEntity.class)
				.list();
		return result;
	}

	@Override
	public List<AccountsEntity> getAllAccountsByName(String name)throws Exception{
		log.info("Inside getAllAccounts of campaignDAO");
		List<AccountsEntity> result = obfy.load().type(AccountsEntity.class)
				.filter("accountName = ",name).list();
		return result;
	}

	@Override
	public AccountsEntity getAccountByID(String accountId)throws Exception{     //shubham
		log.info("Inside getAllAccounts of campaignDAO");
		AccountsEntity accountsEntity = obfy.load().type(AccountsEntity.class).id(accountId).now();
		return accountsEntity;
	}


	@Override
	public List<AccountsEntity> getAllAccounts(String adServerId)throws Exception{
		List<AccountsEntity> result = obfy.load().type(AccountsEntity.class)
				.filter("adServerId = ", adServerId).list();
		return result;
	}

	@Override
	public AccountsEntity loadAccount(String accountDFPId, String adServerId)throws Exception{
		AccountsEntity  accountObj = obfy.load().type(AccountsEntity.class)
				.filter("adServerId = ", adServerId)
				.filter("accountDfpId = ", accountDFPId)
				.first().now();
		return accountObj;
	}

	@Override
	public List<AgencyObj> getAllAgencyByAdServerId(String adServerId){   //shubham need to remove
		log.info("Inside getAllAgencyByAdServerId of campaignDAO");
		if(adServerId!=null && !adServerId.equals("0")){
			List<AgencyObj> result = obfy.load().type(AgencyObj.class)
					.filter("dfpNetworkCode = ",adServerId).list();
			return result;
		}else{
			List<AgencyObj> result = obfy.load().type(AgencyObj.class)
					.list();
			return result;
		}
	}
	
	@Override
	public List<AccountsEntity> getAllAccountByAdServerId(String adServerId , String accountType){   //shubham
		log.info("Inside getAllAccountByAdServerId of smartCampaignplannerDAO");
		if(adServerId == null )
			return null;
		List<AccountsEntity> result = obfy.load().type(AccountsEntity.class)
				.filter("adServerId = ",adServerId).filter("accountType = ", accountType).list();
		return result;

	}
	
	@Override
	public List<AccountsEntity> getAllAccountByCompanyId(String companyId, String accountType){   //shubham
		log.info("Inside getAllAccountByCompanyId of smartCampaignplannerDAO");
		if(companyId == null )
			return null;
		List<AccountsEntity> result = obfy.load().type(AccountsEntity.class)
				.filter("companyId = ",companyId).filter("accountType = ", accountType).list();
		return result;

	}
		
		@Override
		public QueryResponse getAllCampaignsForMigration(QueryDTO queryDTO) throws DataServiceException {
			QueryResponse queryResponse = null;
			StringBuilder query = new StringBuilder();
			query.append(" Select Advertiser_ID, Advertiser, Agency_ID, Agency, Order_id, ")
			.append(" substr(dateorder,27,Length(dateorder)) as Order, ")
			.append(" date(Order_Start_Date) as Order_Start_Date, date(Order_End_date) as Order_End_date, ")
			.append(" Line_item_type, line_item_id, ")
			.append(" substr(dateLine_Item,27,Length(dateLine_Item)) as Line_Item, ")
			.append(" date(Line_Item_start_date) as Line_Item_start_date, date(Line_Item_end_date) as Line_Item_end_date, Creative_ID, Creative, Creative_Size, ")
			.append(" Creative_type, Cost_Type, Rate ")
			.append(" From( ")
			.append(" Select Advertiser_ID, Advertiser, Agency_ID, Agency, Order_id, ")
			.append(" MAX(concat(FORMAT_UTC_USEC(Date), Order)) as dateorder, ")
			.append(" Min(order_start_date) as Order_Start_Date, max(order_end_date) as Order_End_date, ")
			.append(" Line_item_type, line_item_id, MAX(concat(FORMAT_UTC_USEC(Date), line_item)) as dateline_item, ")
			.append(" Min(Line_Item_start_date) as Line_Item_start_date, ")
			.append(" Min(Line_Item_end_date) as Line_Item_end_date, Creative_ID, Creative, Creative_Size, ")
			.append(" Creative_type, Cost_Type, Rate ")
			.append(" from "+queryDTO.getQueryData())
			//.append(" Where Order_id IN ('169504542','172436862') ")
			.append(" Group Each by Advertiser_ID, Advertiser, Agency_ID, Agency, Order_id, Line_item_type, line_item_id, ")
			.append(" Creative_ID, Creative, Creative_Size, ")
			.append(" Creative_type, Cost_Type, Rate ignore case)Z order by Order_id ");
			
			log.info("getAllCampaignsForMigration Query : "+query);
			
			queryDTO.setQueryData(query.toString());
			int j=0;
			do{
	             try {
					queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
				} catch (Exception e) {
					log.severe("getAllCampaignsForMigration Query Exception = " + e.getMessage());
					
				}
	 			j++;
	 		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);
	 		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
	 			log.warning("Invalid getAllCampaignsForMigration query or no data found...");
	 		}
	 		return queryResponse;
	}

	@Override
	public CreativeObj getCreative(String format, String size) throws DataServiceException {
		log.info("Inside getCreative of SmartCampaignPlannerDAO : format : "+format+", size : "+size);
		CreativeObj creativeObj = null;
		List<CreativeObj> creativeObjList = obfy.load().type(CreativeObj.class)
				.filter("format = ",format)
				.filter("size = ",size).list();
		if(creativeObjList != null && creativeObjList.size() > 0) {
			log.info("creativeObjList.size() : "+creativeObjList.size());
			creativeObj = creativeObjList.get(0);
		}
		log.info("CreativeObj : "+creativeObj);
		return creativeObj;

	}
		/*
		 * @author Dheeraj Kumar
		 * This method will return SmartCampaignObj based on input parameters
		 * @param long - unique campaignId 
		 * @return SmartCampaignObj - Campaign object from datastore
		 */
		@Override
		public SmartCampaignObj getCampaignByCampaignId(long campaignId) throws DataServiceException{
			SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
								       .filter("campaignId = ", campaignId)
								       .first().now();							       
			return obj;
		}
		
		
		/*
		 * @author Naresh Pokhriyal
		 * This method will return List<SmartCampaignObj> for given adserverId
		 * @return List<SmartCampaignObj> - Campaign object List from datastore
		 */
		@Override
		public List<SmartCampaignObj> getCampaignByAdserverId(String adServerId) throws DataServiceException{
			List<SmartCampaignObj> smartCampaignObjList = obfy.load().type(SmartCampaignObj.class)
								       .filter("adServerId = ", adServerId)
								       .list();								       
			return smartCampaignObjList;
		}
		
		/**
		 * @author Naresh Pokhriyal<br />
		 * To get all campaigns for the company.
		 * @param companyId : id of the company.
		 * @return {@link List<SmartCampaignObj>} Returns List<SmartCampaignObj>.
		 */
		public List<SmartCampaignObj> getCampaignsByCompanyId(String companyId) throws DataServiceException{
			return obfy.load().type(SmartCampaignObj.class)
								       .filter("companyId = ", companyId)
								       .list();								       
		}
		
		
		/*
		 * @author Youdhveer Panwar
		 * Load all campaigns for a given dfp adServer NetworkCode
		 * (non-Javadoc)
		 * @see com.lin.persistance.dao.ISmartCampaignPlannerDAO#getAllCampaign(java.lang.String)
		 */
		public  List<SmartCampaignObj>  getAllCampaign(String adServerCode) throws DataServiceException{			
			 List<SmartCampaignObj> campaignList = obfy.load().type(SmartCampaignObj.class)
					 								.filter("adServerId = ", adServerCode)
					 								.order("dfpOrderId")
	                     						    .list();
			 return campaignList;
		}
		
		@Override
		public List<AlertEngineFlightObj> getAllAlertEngineFlightObj(long placementId) throws DataServiceException {
	        log.info("Inside getAllFlightsOfPlacement of SmartCampaignPlannerDAO");
	        obfy.clear();
	        List<AlertEngineFlightObj> flightList = obfy.load().type(AlertEngineFlightObj.class)
						.filter("placementId = ", placementId)
					    .list();
	        return flightList;
			
		}
		
		@Override
		public List<SmartCampaignHistObj> getCampaignHistByCampaignId(long campaignId) throws DataServiceException{
			log.info("Inside getCampaignHistByCampaignId of SmartCampaignPlannerDAO, campaignId : "+campaignId);
			List<SmartCampaignHistObj> result = obfy.load().type(SmartCampaignHistObj.class)
												.filter("campaignId = ",campaignId)
												.order("-updatedOn")
												.list();
			return result;
		}


	/*
	 * @author Youdhveer Panwar
	 * This method will return SmartCampaignObj based on input parameters
	 * @param long - unique dfpOrderId for each DFP ad server
	 * 		  String - adServerId (DFP networkCode)
	 * @return SmartCampaignObj - Campaign object from datastore
	 */
	public SmartCampaignObj getCampaignByDFPOrderId(long dfpOrderId,String adServerCode) throws DataServiceException{
		SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
				.filter("dfpOrderId = ", dfpOrderId)
				.filter("adServerId = ", adServerCode)
				.first().now();								       
		return obj;
	}

	/*
	 * @author Naresh Pokhriyal
	 * This method will return SmartCampaignObj based on input parameters
	 * @param long - unique dfpOrderId 
	 * @return SmartCampaignObj - Campaign object from datastore
	 */
	@Override
	public SmartCampaignObj getCampaignByDFPOrderId(long dfpOrderId) throws DataServiceException{
		SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
				.filter("dfpOrderId = ", dfpOrderId)
				.first().now();								       
		return obj;
	}

		/*
	 * @author Naresh Pokhriyal
	 * This method will return List<SmartCampaignObj> based on input parameters
	 * @param String adServerId (DFP networkCode),String adServerUsername (DFP networkUsername)
	 * @return List<SmartCampaignObj> - Campaign object List from datastore
	 */
	@Override
	public List<SmartCampaignObj> getCampaignByDFPOrderId(String adServerId,String adServerUsername) throws DataServiceException{
		List<SmartCampaignObj> smartCampaignObjList = obfy.load().type(SmartCampaignObj.class)
				.filter("adServerId = ", adServerId)
				.filter("adServerUsername = ", adServerUsername)
				.list();								       
		return smartCampaignObjList;
	}

	@Override
	public List<SmartCampaignObj> loadAllCampaignsByIds(List<Long> campaignIdList) {
		log.info("Inside loadAllCampaignsByIds of SmartCampaignPlannerDAO, campaignIdList : "+campaignIdList);
		List<SmartCampaignObj> objs = obfy.load().type(SmartCampaignObj.class)
				.filter("campaignId IN ", campaignIdList).list();
		return objs;
	}

	@Override
	public List<SmartCampaignPlacementObj> loadAllPlacementsByIds(List<Long> placementIdList) throws DataServiceException {
		log.info("Inside loadAllPlacementsByIds of SmartCampaignPlannerDAO, placementIdList : "+placementIdList);
		List<SmartCampaignPlacementObj> objs = obfy.load().type(SmartCampaignPlacementObj.class)
				.filter("placementId IN ", placementIdList).list();
		return objs;
	}

	@Override
	public QueryResponse campaignReportData(String lineItemIds, QueryDTO queryDTO, String startDate, String endDate) throws Exception {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select sum(total_impressions) as Impression_Delivered, ")
		.append(" Sum(total_clicks) as Clicks, line_item_id, date(date) as date ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where line_item_id in ('"+lineItemIds+"')" )
		.append(" and date >= '"+startDate+"' and date <= '"+endDate+"'" )
		.append(" group each By line_item_id, date order by date ignore case "); 
		log.info("campaignReportData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
 			j++;
 		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);

 		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
 			log.warning("Invalid campaignReportData query or no data found...");
 		}
	return queryResponse;
	}

	@Override
	public QueryResponse billingReportData(String lineItemIds, QueryDTO queryDTO, String startDate, String endDate) throws Exception {
		QueryResponse queryResponse = null;
		StringBuilder query = new StringBuilder();
		query.append(" Select sum(total_impressions) as Impression_Delivered, ")
		.append(" Sum(total_clicks) as Clicks, line_item_id, Order_id, date(date) as date ")
		.append(" from "+queryDTO.getQueryData())
		.append(" Where line_item_id in ('"+lineItemIds+"')" )
		.append(" and date >= '"+startDate+"' and date <= '"+endDate+"'" )
		.append(" group each By line_item_id, Order_id, date order by date ignore case "); 
		log.info("billingReportData Query : "+query);
		
		queryDTO.setQueryData(query.toString());
		
		int j=0;
		do{
             try {
				queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
			} catch (Exception e) {
				log.severe("Query Exception = " + e.getMessage());
				
			}
 			j++;
 		}while((queryResponse == null || !queryResponse.getJobComplete()) && j<=3);

 		if (queryResponse==null || queryResponse.getRows() == null || queryResponse.getRows().size() == 0) {
 			log.warning("Invalid billingReportData query or no data found...");
 		}
 		return queryResponse;
	}


	@Override
	public List<CompanyObj> getAllCompanyList() throws DataServiceException{
		log.info("Inside getAllCompanyList of SmartCampaignPlannerDAO");
		List<CompanyObj> result = obfy.load().type(CompanyObj.class)
				.list();
		return result;
	}

	@Override
	public List<CensusDTO> getAllCensus() {
		log.info("Inside getAllCensus of SmartCampaignPlannerDAO");
		List<CensusDTO> result = obfy.load().type(CensusDTO.class).list();
		return result;
	}

	@Override
	public List<SmartCampaignObj> searchCampaignsInDataStore(List<String> accountIdList, List<Long> campaignIdList) throws DataServiceException{
		List<SmartCampaignObj> campaignList = obfy.load().type(SmartCampaignObj.class)
				.filter("advertiserId IN ", accountIdList)
				.filter("campaignId IN ", campaignIdList).list();		
		return campaignList;
	}

	@Override
	public List<SmartCampaignObj> searchCampaignsInDataStore(List<String> accountIdList, String companyId) throws DataServiceException{
		List<SmartCampaignObj> campaignList = obfy.load().type(SmartCampaignObj.class)
				.filter("advertiserId IN ", accountIdList)
				.filter("companyId = ", companyId).list();		
		return campaignList;
	}

	@Override
	public List<Long> getAllLineItemIDByPartner(long campaignID,
			String partnerName) {
		List<Long> lineitemIDList = new ArrayList<Long>();
		obfy.clear();
		Key<SmartCampaignObj> parent = Key.create(SmartCampaignObj.class, campaignID);
		List<SmartCampaignPlacementObj> placementList = obfy.load().type(SmartCampaignPlacementObj.class)
				.ancestor(parent)
				.filter("dfpLineItemList.partner = ",partnerName)
				.list();
		for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementList) {
			List<DFPLineItemDTO> itemDTOs = smartCampaignPlacementObj.getDfpLineItemList();
			for (DFPLineItemDTO dfpLineItemDTO : itemDTOs) {
				lineitemIDList.add(dfpLineItemDTO.getLineItemId());
			}
		}
		return lineitemIDList;
	}
	
	@Override
	public SmartCampaignObj getCampaignByAdvertiserId(String advertiserId) throws DataServiceException{
		SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
							       .filter("advertiserId = ", advertiserId)
							       .first().now();							       
		return obj;
	}
	
	@Override
	public SmartCampaignObj getCampaignByAgencyId(String agencyId) throws DataServiceException{
		SmartCampaignObj obj = obfy.load().type(SmartCampaignObj.class)
							       .filter("agencyId = ", agencyId)
							       .first().now();							       
		return obj;
	}
	
	@Override
	public List<SmartCampaignObj> loadAllCampaignsByDFPId(List<Long> dfpOrderIdList) {
		log.info("Inside loadAllCampaignsByDFPId of SmartCampaignPlannerDAO, campaignIdList : "+dfpOrderIdList);
		List<SmartCampaignObj> objs = obfy.load().type(SmartCampaignObj.class)
				.filter("dfpOrderId IN ", dfpOrderIdList).list();
		return objs;
	}
}
