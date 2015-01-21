package com.lin.web.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;


public interface IProductService extends IBusinessService {

	JSONArray getContextualConsistencies() throws Exception;

	String initCreateProduct(UserDetailsDTO userDetailsDTO, long userId, boolean isSuperAdmin, long productId) throws Exception;

	boolean saveProductData(String productData) throws Exception;

	JSONArray loadAllProducts(long userId, boolean isSuperAdmin, int productsPerPage, int pageNumber, String searchKeyword) throws Exception;

	JSONObject initEditProduct(long productId, String partnerId) throws Exception;

	boolean deleteProduct(long productId, String partnerId) throws Exception;

	JSONArray getProductCreatives() throws Exception;

	JSONArray getDevices() throws Exception;

	JSONArray getGeoTargets() throws Exception;

	JSONArray getPlatforms() throws Exception;
	
	public JSONObject getProductJsonData(List<ProductsObj> productList);
	
	public List<ProductsObj> searchProducts(String adFormat,String size,String contextCategories,String contextSubCategories,
			String dma,	String state,String city,String zip,String device,String platform);
	
	public List<ProductsObj> searchProducts(SmartCampaignPlacementObj campaignPlacementObj);
	
	public Map<String,Long> allocateImpressionByProduct(List<ProductsObj> productList, 
			Map<String,ForcastDTO> productForecastMap, long goalQty);
	public Map<String,Long> allocateImpressionByPublisher(List<ProductsObj> productList, 
			Map<String,ForcastDTO> productForecastMap, long goalQty);
	public List<PlacementObj> createPlacements(Map<String,Long> partnerWiseAllocationMap,SmartCampaignObj smartCampaign);
	
	JSONArray loadTopLevelAdUnits(String adServerId, String adServerUsername, long userId) throws Exception;

	JSONArray loadChildAdUnits(String adServerId, String adServerUsername, String parentId, String parentFullPath, long userId) throws Exception;

	String loadAllActiveAdUnits(String adServerId, String adServerUsername) throws Exception;

	public List<StateObj> loadAllStates(String countryCode);
	public JSONObject loadStatesJSON(List<StateObj> stateObjList);
	public List<CityObj> loadAllCitiesZips(String stateCode);
	public JSONObject loadCityZipJSON(List<CityObj> cityObjList);
	
	public List<CityDTO> searchCity(String searchText);
	
	public JSONObject searchCityJSON(List<CityDTO> searchedCityList);
	
	public List<CreativeObj> loadAllCreatives();
	public JSONObject creativeJSON(List<CreativeObj> creativeList);
	public List<DeviceObj> getAllDevices();
	public List<PlatformObj> getAllPlatforms();

	List<ProductsObj> productSetup(long userId, boolean isSuperAdmin) throws Exception;
	
	 public List<AdUnitDTO> searchAdUnits(String searchText, String adServerId);
	 public JSONObject searchAdUnitsJSON(List<AdUnitDTO> searchList);
	 
	 public List<ProductsObj> aggregateSearchProductsByPartner(List<ProductsObj> searchList);
	 public JSONObject getPlacementsJSON(List<PlacementObj> placementList);

	 ProductsObj getProductByName(String productName) throws Exception;
	 
	 public SmartMediaPlanObj createSmartMediaPlan(Map<String,List<ProductsObj>> campaignProducts, 
				Map<String,ForcastDTO> productForecastMap,
				Map<String,Map<String,Long>> campaignProductsAllocationMap, UnifiedCampaignDTO campaignDTO, String planType);
	 
	 public JSONObject createSmartMediaPlanJSON(SmartMediaPlanObj smartMediaPlanObj);
	 
	 public SmartMediaPlanObj saveSmartMediaPlanFromJSON(JSONObject smartMediaPlanJson);
	 public SmartMediaPlanObj loadSmartMediaPlan(String campaignId);
	 public int checkMediaPlanStatus(String campaignId);

	 public List<ZipDTO> searchZip(String searchText);

	 public JSONObject searchZipJSON(List<ZipDTO> searchedZipList);
	 public SmartMediaPlanObj loadSmartMediaPlan(long mediaPlanId);
	 public void saveAdUnitHierarchy(List<AdUnitHierarchy> adUnitHierarchyList);
	 public void saveSmartMediaPlan(SmartMediaPlanObj smartMediaPlan);
	 public SmartMediaPlanObj loadSmartMediaPlan(String campaignId,int status);
	 public ProductsObj loadProduct(long id);
	 
	 public List<ProductsObj> loadAllProducts();
	 
	 public Map<String, List<String>> loadProductsAdUnitIdsMapByAdServerId();
	 
	 public int getCityObjCount() throws DataServiceException;
	 public int getAdUnitHierarchyCount()throws DataServiceException;
	 public void loadCityDataThroughTaskQueue(String offset, String limit);
	 public void loadAdUnitDataThroughTaskQueue(String offset, String limit);
	 public JSONObject loadGPTTagsFromSmartMediaPlan(String campaignId);
	 
	 // Added by Anup for Product Performance
	 public JSONArray getLocationProductInfo(String groupBy, JSONObject productProperty);
	 public JSONArray getProductForecastPerformance(String groupBy);

	List<ProductsObj> loadProducts(List<String> productIds);
}
