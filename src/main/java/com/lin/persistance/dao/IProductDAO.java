package com.lin.persistance.dao;

import java.util.List;

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

public interface IProductDAO extends IBaseDao {
	
	void saveObject(Object obj) throws DataServiceException;
	
	void saveObjectWithStrongConsistancy(Object obj) throws DataServiceException;
	
	void deleteObject(Object obj) throws DataServiceException;

	IABContextObj getIABContext(long id) throws DataServiceException;
	
	List<IABContextObj> getAllIABContext() throws DataServiceException;

	CreativeObj getCreativeObj(long id) throws DataServiceException;

	CountryObj getCountryObj(long id) throws DataServiceException;

	CityObj getCityObj(long id) throws DataServiceException;

	GeoTargetsObj getGeoTargetsObj(long id) throws DataServiceException;

	PlatformObj getPlatformObj(long id) throws DataServiceException;

	DeviceObj getDeviceObj(long id) throws DataServiceException;

	List<StateObj> getStatesForCountry(long id) throws DataServiceException;

	List<CityObj> getCityForStates(long id) throws DataServiceException;

	long maxProductId() throws DataServiceException;

	List<ProductsObj> getProductByPublisherId(String publisherId) throws DataServiceException;

	ProductsObj getProductById(long productId) throws DataServiceException;
	public List<ProductsObj> searchProductsInDataStore(String [] adFormatArray, String [] sizeArray,
			String [] contextArray, String [] contextSubArray, String [] dmaArray,String [] stateArray,String [] cityArray, 
			String [] zipArray,	String [] deviceArray, String [] platformArray )	throws DataServiceException;

	public List<ProductsObj> searchProductsInDataStore(List<String> adFormatList, List<String> sizeList,
			List<String> contextList, List<String> contextSubList, List<String> dmaList,List<String> stateList,List<String> cityList, 
			List<String> zipList,	List<String> deviceList, List<String> platformList )	throws DataServiceException;

	List<GeoTargetsObj> getAllGeoTargetsObj() throws DataServiceException;

	public CountryObj getCountryObj(String code) throws DataServiceException;
	
	public StateObj getSateObj(String code) throws DataServiceException;
	
	public List<CreativeObj> getAllCreativeObjs() throws DataServiceException;
	
	public List<PlatformObj> getAllPlatforms() throws DataServiceException;
	
	public List<DeviceObj> getAllDevices() throws DataServiceException;

	List<AdUnitHierarchy> getAllAdUnitHierarchyByServerId(String adServerId) throws DataServiceException;

	List<CreativeObj> getAllCreativeObj() throws DataServiceException;

	StateObj getStateObj(long stateId, long parentId) throws DataServiceException;

	ProductsObj getProductByName(String productName) throws DataServiceException;

	List<ProductsObj> getProductByPartnerId(String partnerId) throws DataServiceException;
	public List<ProductsObj> getAllProducts() throws DataServiceException;
	public List<CityObj> getCities(String offset,String limit) throws DataServiceException;
	public List<AdUnitHierarchy> getAdUnits(String offset,String limit) throws DataServiceException;
	public List<AdUnitHierarchy> loadAdUnitHierachyObj(String adUnitName,String adServerId) throws DataServiceException;

	List<ProductsObj> getProductByIds(List<String> productId)
			throws DataServiceException;

	public List<ProductForecastObj> getProductForecast(String groupBy) throws DataServiceException;
}
