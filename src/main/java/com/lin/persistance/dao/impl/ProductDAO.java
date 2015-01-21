package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cmd.Query;
import com.lin.persistance.dao.IProductDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.StateObj;
import com.lin.web.service.impl.OfyService;


public class ProductDAO implements IProductDAO{
	
	private static final Logger log = Logger.getLogger(ProductDAO.class.getName());	
	private Objectify obfy = OfyService.ofy();	
	private Objectify strongObfy = OfyService.ofy().consistency(Consistency.STRONG);
	
	public void saveObject(Object obj) throws DataServiceException{	
		obfy.save().entity(obj).now();
		//log.info("Object saved successfully in datastore.");
	}
	
	@Override
    public void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException{
    	strongObfy.save().entity(obj).now();
    }
	
	public void deleteObject(Object obj) throws DataServiceException{	
		obfy.delete().entity(obj).now();
        log.info("Object deleted successfully from datastore.");
	}
	
	/*
	 * Get all product by publisherId id from datastore
	 * @param String publisherId
	 * @return List<ProductsObj>
	 */
	@Override
	public List<ProductsObj> getProductByPublisherId(String publisherId) throws DataServiceException {
		if(publisherId != null && publisherId.trim().length() > 0 && !publisherId.trim().equals("0")) {
			List<ProductsObj> resultList = obfy.load().type(ProductsObj.class)
									 .filter("publisherId = ", publisherId)
			                         .order("publisherId")
			                         .list();
			if(resultList !=null  && resultList.size() > 0){
				log.info("resultList size : "+resultList.size());
				return resultList;
			}
		}
		log.info("resultList : null");
		return null;
	}
	
	/*
	 * Get all product by companyId id from datastore
	 * @param String companyId
	 * @return List<ProductsObj>
	 */
	@Override
	public List<ProductsObj> getProductByPartnerId(String partnerId) throws DataServiceException {
		if(partnerId != null && partnerId.trim().length() > 0 && !partnerId.trim().equals("0")) {
			List<ProductsObj> resultList = obfy.load().type(ProductsObj.class)
									 .filter("partnerId = ", partnerId)
			                         .order("partnerId")
			                         .list();
			if(resultList !=null  && resultList.size() > 0){
				log.info("resultList size : "+resultList.size());
				return resultList;
			}
		}
		log.info("resultList : null");
		return null;
	}
	
	/*
	 * Get all product by publisherId id from datastore
	 * @param String productId
	 * @return ProductsObj
	 */
	@Override
	public ProductsObj getProductById(long productId) throws DataServiceException {
		if(productId > 0) {
			ProductsObj productsObj = obfy.load().type(ProductsObj.class)
									 .filter("productId = ", productId)
			                          .limit(1).first().now();
			if(productsObj !=null){
				log.info("productsObj found for productId : "+productId);
				return productsObj;
			}
		}
		log.info("productsObj not found for productId : "+productId);
		return null;
	}
	@Override
	public List<ProductsObj> getProductByIds(List<String> productIds) throws DataServiceException {
		List<Long> productIdList = new ArrayList<>();
		for(String pid: productIds){
			productIdList.add(Long.parseLong(pid));
		}
		if(productIds.size()  > 0) {
			List<ProductsObj> products = obfy.load().type(ProductsObj.class)
									 .filter("productId IN  ", productIdList)
			                          .list();
			if(products !=null){
				log.info("productsObj found for productId : "+productIds);
				return products;
			}
		}
		log.info("productsObj not found for productIds : "+productIds);
		return null;
	}
	@Override
	public ProductsObj getProductByName(String productName) throws DataServiceException {
		if(productName != null && productName.length() > 0) {
			ProductsObj productsObj = obfy.load().type(ProductsObj.class)
									 .filter("productName = ", productName)
			                          .limit(1).first().now();
			if(productsObj !=null){
				log.info("productsObj found for productName : "+productName);
				return productsObj;
			}
		}
		log.info("productsObj not found for productName : "+productName);
		return null;
	}
	
	@Override
	public List<AdUnitHierarchy> getAllAdUnitHierarchyByServerId(String adServerId) throws DataServiceException {
		if(adServerId != null && adServerId.trim().length() > 0) {
			List<AdUnitHierarchy> adUnitHierarchyList = obfy.load().type(AdUnitHierarchy.class)
									.filter("adServerId = ", adServerId)
				                    .list();
			if(adUnitHierarchyList !=null  && adUnitHierarchyList.size() > 0){
				log.info("adUnitHierarchyList size : "+adUnitHierarchyList.size());
				return adUnitHierarchyList;
			}
		}
		log.info("adUnitHierarchyList : null");
		return null;
	}
	
	/*
	 * Get max productId from datastore
	 * @return long
	 */
	@Override
	public long maxProductId() throws DataServiceException {
		long maxNumber=0;
		ProductsObj obj=obfy.load().type(ProductsObj.class)
		                         .order("-productId")
		                         .limit(1).first().now();
		if(obj !=null ){
			maxNumber = obj.getProductId();
		}
		return maxNumber;
	}
	
	@Override
	public IABContextObj getIABContext(long id) throws DataServiceException{
		log.info("Inside getIABContext of ProductDAO");
		IABContextObj contextObj = null;
		
		if(id > 0) {
			List<IABContextObj> resultList = obfy.load().type(IABContextObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				contextObj = resultList.get(0);
		}
		}
		if(contextObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return contextObj;
	}
	
	@Override
	public List<IABContextObj> getAllIABContext() throws DataServiceException{
		log.info("Inside getAllIABContext of ProductDAO");
		
			List<IABContextObj> resultList = obfy.load().type(IABContextObj.class)
											.order("subgroup")
											.list();
			if(resultList != null && resultList.size() > 0) {
				log.info("IABContext resultList : "+resultList.size());
				return resultList;
			}
			log.info("resultList :Null");
			return null;
	}
	
	@Override
	public CreativeObj getCreativeObj(long id) throws DataServiceException{
		log.info("Inside getCreativeObj of ProductDAO");
		CreativeObj creativeObj = null;
		
		if(id > 0) {
			List<CreativeObj> resultList = obfy.load().type(CreativeObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				creativeObj = resultList.get(0);
		}
		}
		if(creativeObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return creativeObj;
	}
	
	@Override
	public List<CreativeObj> getAllCreativeObj() throws DataServiceException{
		log.info("Inside getAllCreativeObj of ProductDAO");
		
			List<CreativeObj> resultList = obfy.load().type(CreativeObj.class).list();
			if(resultList != null && resultList.size() > 0) {
				log.info("CreativeObj resultList : "+resultList.size());
				return resultList;
			}
			log.info("resultList :Null");
			return null;
	}
	
	@Override
	public CountryObj getCountryObj(long id) throws DataServiceException{
		log.info("Inside getCountryObj of ProductDAO");
		CountryObj countryObj = null;
		
		if(id > 0) {
			List<CountryObj> resultList = obfy.load().type(CountryObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				countryObj = resultList.get(0);
		}
		}
		if(countryObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return countryObj;
	}
	
	
	public CountryObj getCountryObj(String code) throws DataServiceException{		
		CountryObj countryObj= obfy.load().type(CountryObj.class) 
									.filter("code = ",code)
									.first().now();
		return countryObj;
	}
	
	public StateObj getSateObj(String code) throws DataServiceException{		
		StateObj stateObj= obfy.load().type(StateObj.class) 
									.filter("code = ",code)
									.first().now();
		return stateObj;
	}
	
	@Override
	public StateObj getStateObj(long id, long parenId) throws DataServiceException{
		if(id > 0 && parenId > 0) {
			Key<CountryObj> parent = Key.create(CountryObj.class, parenId);
			Key<StateObj> key = Key.create(parent,StateObj.class, id);
			return(StateObj) obfy.load().filterKey(key).first().now();
		}
		return null;
	}
	
	@Override
	public CityObj getCityObj(long id) throws DataServiceException{
		log.info("Inside getCityObj of ProductDAO");
		CityObj cityObj = null;
		
		if(id > 0) {
			List<CityObj> resultList = obfy.load().type(CityObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				cityObj = resultList.get(0);
		}
		}
		if(cityObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return cityObj;
	}
	
	@Override
	public GeoTargetsObj getGeoTargetsObj(long id) throws DataServiceException{
		log.info("Inside getGeoTargetsObj of ProductDAO");
		GeoTargetsObj geoTargetsObj = null;
		
		if(id > 0) {
			List<GeoTargetsObj> resultList = obfy.load().type(GeoTargetsObj.class)
											.filter("geoTargetId = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				geoTargetsObj = resultList.get(0);
		}
		}
		if(geoTargetsObj !=null){
			log.info("geoTargetsObj resultList : 1");
		}else{
			log.info("geoTargetsObj resultList :Null");
		}
		return geoTargetsObj;
	}
	
	@Override
	public List<GeoTargetsObj> getAllGeoTargetsObj() throws DataServiceException{
		log.info("Inside getAllGeoTargetsObj of ProductDAO");
		
			List<GeoTargetsObj> resultList = obfy.load().type(GeoTargetsObj.class)
														.order("geoTargetsName")
														.list();
			if(resultList != null && resultList.size() > 0) {
				log.info("GeoTargetsObj resultList : "+resultList.size());
				return resultList;
			}
			log.info("resultList :Null");
			return null;
	}
	
	@Override
	public PlatformObj getPlatformObj(long id) throws DataServiceException{
		log.info("Inside getPlatformObj of ProductDAO");
		PlatformObj platformObj = null;
		
		if(id > 0) {
			List<PlatformObj> resultList = obfy.load().type(PlatformObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				platformObj = resultList.get(0);
		}
		}
		if(platformObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return platformObj;
	}
	
	@Override
	public DeviceObj getDeviceObj(long id) throws DataServiceException{
		log.info("Inside getDeviceObj of ProductDAO");
		DeviceObj deviceObj = null;
		
		if(id > 0) {
			List<DeviceObj> resultList = obfy.load().type(DeviceObj.class)
											.filter("id = ", id)
											.list();
			if(resultList != null && resultList.size() > 0) {
				deviceObj = resultList.get(0);
		}
		}
		if(deviceObj !=null){
			log.info("resultList : 1");
		}else{
			log.info("resultList :Null");
		}
		return deviceObj;
	}
	
	@Override
	public List<StateObj> getStatesForCountry(long id) throws DataServiceException {
		log.info("Inside getStatesForCountry of ProductDAO");
		
		Key<CountryObj> parent = Key.create(CountryObj.class, id);
		List<StateObj> stateObjList = obfy.load().type(StateObj.class)
											.ancestor(parent)
											.list();
		return stateObjList;
	}
	
	@Override
	public List<CityObj> getCityForStates(long id) throws DataServiceException {
		log.info("Inside getCityForStates of ProductDAO");
		
		Key<StateObj> parent = Key.create(StateObj.class, id);
		List<CityObj> cityObjList = obfy.load().type(CityObj.class)
											.ancestor(parent)
											.list();
		if(cityObjList != null && cityObjList.size() > 0) {
			log.info("cityObjList size : " +cityObjList.size());
				return cityObjList;
		}
		else{
			log.info("cityObjList :Null");
			return null;
		}
	}
	
	public List<PlatformObj> getAllPlatforms() throws DataServiceException{
		List<PlatformObj> resultList = obfy.load().type(PlatformObj.class).list();		
		return resultList;
	}
	
	public List<DeviceObj> getAllDevices() throws DataServiceException{
		List<DeviceObj> resultList = obfy.load().type(DeviceObj.class).list();		
		return resultList;
	}
	public List<CreativeObj> getAllCreativeObjs() throws DataServiceException{
		List<CreativeObj> resultList = obfy.load().type(CreativeObj.class).list();		
		return resultList;
	}
	
	
	/*
	 * @author Youdhveer Panwar
	 * Load all products from datastore
	 */
	public List<ProductsObj> getAllProducts() throws DataServiceException{
		List<ProductsObj> resultList = obfy.load().type(ProductsObj.class).list();		
		return resultList;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * Load all adUnits by name
	 */
	public List<AdUnitHierarchy> loadAdUnitHierachyObj(String adUnitName,String adServerId) throws DataServiceException{
		List<AdUnitHierarchy> resultList = obfy.load().type(AdUnitHierarchy.class)
											   .filter("adUnitName = ",adUnitName)
											   //.filter("adServerId = ",adServerId)
											   .list();		
		return resultList;
	}
	
	
	public List<ProductsObj> searchProductsInDataStore(String [] adFormatArray, String [] sizeArray,
			String [] contextArray,String [] contextSubArray, String [] dmaArray,String [] stateArray,String [] cityArray, 
			String [] zipArray,	String [] deviceArray, String [] platformArray )	throws DataServiceException{
		
		List<ProductsObj> productList=null;
		
		Query<ProductsObj> query=obfy.load().type(ProductsObj.class);
		
		if(adFormatArray !=null){
			query=query.filter("creative.format IN ", adFormatArray);
		}
		if(sizeArray !=null){
			query=query.filter("creative.size IN ", sizeArray);
		}
		
		if(contextArray !=null){
			query=query.filter("context.group IN", contextArray);
		}
		
		if(contextSubArray !=null){
			query=query.filter("context.subgroup IN", contextSubArray);
		}
		
		if(dmaArray !=null){
			query=query.filter("dmas.geoTargetsName IN ", dmaArray);
		}
		
		if(stateArray !=null){		
			query=query.filter("states.text IN ", stateArray);	
		}
		
		if(cityArray !=null){		
			query=query.filter("cities.text IN ", cityArray);	
		}
		
		if(zipArray !=null){		
			query=query.filter("cities.zip IN ", zipArray);	
		}
		if(deviceArray !=null){		
			query=query.filter("devices.text IN ", deviceArray);	
		}
		if(platformArray !=null){		
			query=query.filter("platforms.text IN ", platformArray);		
		}
		log.info("Search Product Query :: "+query.toString());
		productList=query.list();
		log.info("Searched Product List :: "+productList);
		return productList;
		
	}
	
	public List<ProductsObj> searchProductsInDataStore(List<String> adFormatList, List<String> sizeList,
			List<String> contextList, List<String> contextSubList, List<String> dmaList,List<String> stateList,List<String> cityList, 
			List<String> zipList,	List<String> deviceList, List<String> platformList )	throws DataServiceException{
		
		List<ProductsObj> productList=null;
		
		Query<ProductsObj> query=obfy.load().type(ProductsObj.class);
		
		if(adFormatList !=null && adFormatList.size()>0){
			query=query.filter("creative.format IN ", adFormatList);
		}
		if(sizeList !=null && sizeList.size()>0){
			query=query.filter("creative.size IN ", sizeList);
		}
		
		if(contextList !=null && contextList.size()>0){
			query=query.filter("context.group IN", contextList);
		}
		
		if(contextSubList !=null && contextSubList.size()>0){
			query=query.filter("context.subgroup IN", contextSubList);
		}
		
		if(dmaList !=null && dmaList.size()>0){
			query=query.filter("dmas.geoTargetsName IN ", dmaList);
		}
		
		if(stateList !=null && stateList.size()>0){		
			query=query.filter("states.text IN ", stateList);	
		}
		
		if(cityList !=null && cityList.size()>0){		
			query=query.filter("cities.text IN ", cityList);	
		}
		
		if(zipList !=null && zipList.size()>0){		
			query=query.filter("cities.zip IN ", zipList);	
		}
		if(deviceList !=null && deviceList.size()>0){
			query=query.filter("devices.text IN ", deviceList);	
		}
		if(platformList !=null && platformList.size()>0){
			query=query.filter("platforms.text IN ", platformList);	
		}
		log.info("Search Product Query :: "+query.toString());
		productList=query.list();	
		log.info("Searched Product List :: "+productList);
		return productList;
	}
	
	public List<CityObj> getCities(String offset,String limit)
	{
	    List<CityObj> cityList = obfy.load().type(CityObj.class).
	    		offset(Integer.parseInt(offset)).
	    		limit(Integer.parseInt(limit)).list();
	    return cityList;
	}
	
	public List<AdUnitHierarchy> getAdUnits(String offset,String limit)
	{
	    List<AdUnitHierarchy> adUnitList = obfy.load().type(AdUnitHierarchy.class).
	    		offset(Integer.parseInt(offset)).
	    		limit(Integer.parseInt(limit)).list();
	    return adUnitList;
	}

	@Override
	public List<ProductForecastObj> getProductForecast(String groupBy) throws DataServiceException {
		List<ProductForecastObj> forcastObjs = obfy.load().type(ProductForecastObj.class).list();	
		log.info("Inside getProductForecast : Total :" + forcastObjs.size());
		for(int i=0;i<forcastObjs.size();i++){
			ProductsObj productsObj =  getProductById(forcastObjs.get(i).getProductId());
			try{
				long geoID = forcastObjs.get(i).getDmaDetail().getGeoTargetId();
				if( geoID == 0 ){
					forcastObjs.get(i).setDmaDetail(productsObj.getDmas().get(0));
				}
			}catch(Exception e){
				forcastObjs.get(i).setDmaDetail(productsObj.getDmas().get(0));
				log.info(e.getMessage());
			}
		}
		return forcastObjs;
	}

}
