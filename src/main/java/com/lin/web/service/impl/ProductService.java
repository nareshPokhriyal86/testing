package com.lin.web.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.IPerformanceMonitoringDAO;
import com.lin.persistance.dao.IProductDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.PerformanceMonitoringDAO;
import com.lin.persistance.dao.impl.ProductDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.web.documents.TextSearchDocument;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.ProductForecastDTO;
import com.lin.web.dto.LineItemDTO;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IProductService;
import com.lin.web.service.IUserService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DBUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;


public class ProductService implements IProductService {

	private static final Logger log = Logger.getLogger(ProductService.class.getName());
	
	public static final String allOptionId = "1000000";
	public static final String allOption = "ALL";
	public static final String noneOptionId = "1000001";
	public static final String noneOption = "NONE";
	
	public static JSONObject createAllNoneOptionJsonObject(String[] keyArray, boolean isOptionAll) {
		String option = allOption;
		String optionId = allOptionId;
		if(!isOptionAll) {
			option = noneOption;
			optionId = noneOptionId;
		}
		JSONObject jsonObject = new JSONObject();
		if(keyArray != null && keyArray.length > 0) {
			for (int i = 0; i < keyArray.length; i++) {
				String value = option;
				String key = keyArray[i];
				if(key.equals("id") || key.equals("stateId") /*|| key.equals("parentId")*/) {
					value = optionId;
				}
				jsonObject.put(key, value);
			}
		}
		else {
			jsonObject = null;
		}
		return jsonObject;
	}

	@Override
	public JSONArray getContextualConsistencies() throws Exception {
		IProductDAO productDAO = new ProductDAO();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","group","subGroup"}, true));
		String key = "iabContextualConsistencies";
		
		List<IABContextObj> sortedList = (List<IABContextObj>) MemcacheUtil.getObjectFromCache(key);
		if(sortedList == null || sortedList.size() == 0) {
			log.info("Memcache does not exists");
			sortedList = new ArrayList<>();
			List<IABContextObj> iabContextObjList = productDAO.getAllIABContext();
			if(iabContextObjList != null && iabContextObjList.size() > 0) {
				for(IABContextObj iABContextObj : iabContextObjList) {
					if(iABContextObj!=null && iABContextObj.getGroup() != null) {
						String subGroup = "";
						if(iABContextObj.getSubgroup() != null && iABContextObj.getSubgroup().trim().length() > 0){
							subGroup = iABContextObj.getGroup()+" - "+iABContextObj.getSubgroup();
						}else{
							subGroup = iABContextObj.getGroup()+" (Group)";
						}
						iABContextObj.setSubgroup(subGroup);
						sortedList.add(iABContextObj);
					}
				}
				if(sortedList != null && sortedList.size() > 0) {
					Collections.sort(sortedList);
					MemcacheUtil.setObjectInCache(key, sortedList);
					log.info("Memcache created : "+sortedList.size());
				}
			}
		}
		else {
			log.info("Memcache exists size : "+sortedList.size());
		}
			
		if(sortedList != null && sortedList.size() > 0) {
			
			for(IABContextObj contextObj : sortedList) {
				JSONObject jsonObject = new JSONObject();
				String subGroup = contextObj.getSubgroup().replace(contextObj.getGroup()+" - ", "");
				if(subGroup.endsWith(" (Group)")) {
					subGroup = "";
				}
				jsonObject.put("id", contextObj.getId());
				jsonObject.put("text", contextObj.getSubgroup());
				jsonObject.put("group", contextObj.getGroup());
				jsonObject.put("subGroup", subGroup);
				jsonArray.add(jsonObject);
			}
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","group","subGroup"}, false));
		return jsonArray;
	}
	
	@Override
	public JSONArray getProductCreatives() throws Exception {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","format","size"}, true));
		List<CreativeObj> creativeObjList = loadAllCreatives();
		
		if(creativeObjList != null && creativeObjList.size() > 0) {
			for(CreativeObj creativeObj : creativeObjList) {
				String format = creativeObj.getFormat();
				String size = creativeObj.getSize();
				
				if(format != null && !format.equals("null") && size != null && !size.equals("null")){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", creativeObj.getId());
					jsonObject.put("format", format);
					jsonObject.put("size", size);
					jsonObject.put("text", format+" - "+size);
					jsonArray.add(jsonObject);
				}
			}
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","format","size"}, false));
		return jsonArray;
	}

	@Override
	public JSONArray getDevices() throws Exception {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
		List<DeviceObj> deviceObjList = getAllDevices();
		
		if(deviceObjList != null && deviceObjList.size() > 0) {
			for(DeviceObj deviceObj : deviceObjList) {
				String text = deviceObj.getText();
				
				if(text != null && !text.equals("null")){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", deviceObj.getId());
					jsonObject.put("text", text);
					jsonArray.add(jsonObject);
				}
			}
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, false));
		return jsonArray;
	}

	@Override
	public JSONArray getGeoTargets() throws Exception {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
		
		Map<String,String> geoTargetMap=MemcacheUtil.getGeoTargetsFromCache();
    	if(geoTargetMap==null) {
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
						if(value != null && !value.equals("null")){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", key);
							jsonObject.put("text", value);
							jsonArray.add(jsonObject);
						}
					}
					MemcacheUtil.setGeoTargetsInCache(geoTargetMap);
				}else{
					log.info("No geo targets found : resultList : 0");
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : "+e.getMessage());
				e.printStackTrace();
			}
    	}
    	else {
    		for(String regionId : geoTargetMap.keySet()) {
				String regionName = geoTargetMap.get(regionId);
				if(regionName != null && !regionName.equals("null")){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", regionId);
					jsonObject.put("text", regionName);
					jsonArray.add(jsonObject);
				}
			}
    	}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, false));
		return jsonArray;
	}
	
	@Override
	public JSONArray getPlatforms() throws Exception {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
		List<PlatformObj> platformObjList = getAllPlatforms();
		
		if(platformObjList != null && platformObjList.size() > 0) {
			for(PlatformObj platformObj : platformObjList) {
				String text = platformObj.getText();
				
				if(text != null && !text.equals("null")){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", platformObj.getId());
					jsonObject.put("text", text);
					jsonArray.add(jsonObject);
				}
			}
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, false));
		return jsonArray;
	}
	
	

	@Override
	public String initCreateProduct(UserDetailsDTO userDetailsDTO, long userId, boolean isSuperAdmin, long productId) throws Exception {
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		IProductDAO productDAO = new ProductDAO();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		String productStatus ="update";
		if(productId <= 0) {
			productStatus = "create";
			productId = productDAO.maxProductId();
			log.info("Max productId : "+productId);
			productId = productId + 1;
			log.info("incemented by 1 : "+productId);
		}
		log.info("service productId : "+productId+", productStatus : "+productStatus);
		
		if(isSuperAdmin) {
			List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
			if(companyObjList != null && companyObjList.size() > 0) {
				for(CompanyObj companyObj : companyObjList) {
					if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
						if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
							userDetailsDTO.setNetworkAvailabile(true);
							userDetailsDTO.setNetworkId(companyObj.getAdServerId().get(0));
							userDetailsDTO.setNetworkUsername(companyObj.getAdServerUsername().get(0));
							userDetailsDTO.setPublisherName(companyObj.getCompanyName());
							userDetailsDTO.setCompanyId(companyObj.getId()+"");
							break;
						}
						else {
							userDetailsDTO.setNetworkAvailabile(false);
						}
					}
				}
			}
		}
		else {
			CompanyObj companyObj = userService.getCompanyIdByAdminUserId(userId);
			if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
				if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
					userDetailsDTO.setNetworkAvailabile(true);
					userDetailsDTO.setNetworkId(companyObj.getAdServerId().get(0));
					userDetailsDTO.setNetworkUsername(companyObj.getAdServerUsername().get(0));
					userDetailsDTO.setPublisherName(companyObj.getCompanyName());
					userDetailsDTO.setCompanyId(companyObj.getId()+"");
				}
				else {
					userDetailsDTO.setNetworkAvailabile(false);
				}
			}
		}
		return productStatus+":"+productId;
	}

	@Override
	public boolean saveProductData(String productData) throws Exception {
		boolean result = false;
		boolean isGeoAll = true;
		ProductsObj productsObj = new ProductsObj();
		List<IABContextObj> contextObjList = new ArrayList<>();
		List<CreativeObj> creativeObjList = new ArrayList<>();
		List<DeviceObj> deviceObjList = new ArrayList<>();
		List<GeoTargetsObj> geoTargetsObjList = new ArrayList<>();
		List<PlatformObj> platformObjList = new ArrayList<>();
		List<CityDTO> cityDTOList = new ArrayList<>();
		List<ZipDTO> zipList = new ArrayList<>();
		List<StateObj> stateList = new ArrayList<>();
		List<AdUnitDTO> adUnitList = new ArrayList<>();
		IProductDAO productDAO = new ProductDAO();
		try {
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(productData);
			String productId = jsonObject.getString("productID");
			
			if(LinMobileUtil.isNumeric(productId) && Long.valueOf(productId) > 0 && jsonObject.getString("productName").trim().length() > 0) {
				productsObj.setId(jsonObject.getString("productID"));
				productsObj.setProductId(jsonObject.getLong("productID"));
				productsObj.setProductName(jsonObject.getString("productName").trim());
				productsObj.setPublisherId(jsonObject.getString("publisherID"));
				productsObj.setPartnerId(jsonObject.getString("partnerId"));
				productsObj.setPublisherName(jsonObject.getString("publisherName"));
				productsObj.setNote(jsonObject.getString("note"));
				//productsObj.setStatus(jsonObject.getBoolean("status"));
				productsObj.setGeoFencing(jsonObject.getBoolean("geoFencing"));
				productsObj.setDemographic(jsonObject.getBoolean("demographic"));
				productsObj.setBehaviour(jsonObject.getBoolean("behaviour"));
				productsObj.setDeviceCapability(jsonObject.getInt("deviceCapability"));
				productsObj.setLastUpdatedOn(DateUtil.getDateYYYYMMDDHHMMSS());
				isGeoAll = jsonObject.getBoolean("isGeoAll");
				
				String rate = jsonObject.getString("rate").trim();
				if(LinMobileUtil.isNumeric(rate)) {
					productsObj.setRate(Double.valueOf(rate));
				}
				
				// contextList
				JSONArray context = jsonObject.getJSONArray("contextList");
				if(context.size()!=0) {
					for(int i=0;i<context.size();i++){
		    			JSONObject obj=context.getJSONObject(i);
		    			IABContextObj contextObj = new IABContextObj();
		    			contextObj.setId(obj.getLong("id"));
		    			contextObj.setGroup(obj.getString("group"));
		    			contextObj.setSubgroup(obj.getString("subGroup"));
		    			contextObjList.add(contextObj);
					}
		        }
				else {
					IABContextObj contextObj = new IABContextObj();
					contextObj.setId(StringUtil.getLongValue(allOptionId));
	    			contextObj.setGroup(allOption);
	    			contextObj.setSubgroup(allOption);
	    			contextObjList.add(contextObj);
				}
				
				// creativeList
				JSONArray creative = jsonObject.getJSONArray("creativeList");
				if(creative.size()!=0) {
					for(int i=0;i<creative.size();i++){
		    			JSONObject obj=creative.getJSONObject(i);
		    			CreativeObj creativeObj = new CreativeObj();
		    			creativeObj.setId(obj.getLong("id"));
		    			creativeObj.setFormat(obj.getString("format"));
		    			creativeObj.setSize(obj.getString("size"));
		    			creativeObjList.add(creativeObj);
					}
		        }
				else {
					CreativeObj creativeObj = new CreativeObj();
	    			creativeObj.setId(StringUtil.getLongValue(allOptionId));
	    			creativeObj.setFormat(allOption);
	    			creativeObj.setSize(allOption);
	    			creativeObjList.add(creativeObj);
				}
				
				// deviceList
				JSONArray device = jsonObject.getJSONArray("deviceList");
				if(device.size()!=0) {
					for(int i=0;i<device.size();i++){
		    			JSONObject obj=device.getJSONObject(i);
		    			DeviceObj deviceObj = new DeviceObj();
		    			deviceObj.setId(obj.getLong("id"));
		    			deviceObj.setText(obj.getString("text"));
		    			deviceObjList.add(deviceObj);
					}
		        }
				else {
					DeviceObj deviceObj = new DeviceObj();
	    			deviceObj.setId(StringUtil.getLongValue(allOptionId));
	    			deviceObj.setText(allOption);
	    			deviceObjList.add(deviceObj);
				}
				
				// dmaList
				JSONArray dma = jsonObject.getJSONArray("dmaList");
				if(dma.size()!=0) {
					for(int i=0;i<dma.size();i++){
		    			JSONObject obj=dma.getJSONObject(i);
		    			GeoTargetsObj geoTargetsObj = new GeoTargetsObj();
		    			geoTargetsObj.setGeoTargetId(obj.getLong("id"));
		    			geoTargetsObj.setGeoTargetsName(obj.getString("text"));
		    			geoTargetsObjList.add(geoTargetsObj);
					}
		        }
				else {
					GeoTargetsObj geoTargetsObj = null;
					if(isGeoAll) {
						geoTargetsObj = new GeoTargetsObj(StringUtil.getLongValue(allOptionId), allOption);
					}else {
						geoTargetsObj = new GeoTargetsObj(StringUtil.getLongValue(noneOptionId), noneOption);
					}
	    			geoTargetsObjList.add(geoTargetsObj);
				}
				
				// platformList
				JSONArray platform = jsonObject.getJSONArray("platformList");
				if(platform.size()!=0) {
					for(int i=0;i<platform.size();i++){
		    			JSONObject obj=platform.getJSONObject(i);
		    			PlatformObj platformObj = new PlatformObj();
		    			platformObj.setId(obj.getLong("id"));
		    			platformObj.setText(obj.getString("text"));
		    			platformObjList.add(platformObj);
					}
		        }
				else {
					PlatformObj platformObj = new PlatformObj();
	    			platformObj.setId(StringUtil.getLongValue(allOptionId));
	    			platformObj.setText(allOption);
	    			platformObjList.add(platformObj);
				}
				
				// cityList
				JSONArray city = jsonObject.getJSONArray("cityList");
				if(city.size()!=0) {
					for(int i=0;i<city.size();i++){
						JSONObject obj=city.getJSONObject(i);
		    			CityDTO cityDTO = null;
		    			if((obj.getLong("id")+"").equals(noneOptionId)) {
		    				cityDTO = new CityDTO(StringUtil.getLongValue(noneOptionId), noneOption, StringUtil.getLongValue(noneOptionId), noneOption);
		    				cityDTOList.add(cityDTO);
							break;
		    			}else if((obj.getLong("id")+"").equals(allOptionId)) {
		    				cityDTO = new CityDTO(StringUtil.getLongValue(allOptionId), allOption, StringUtil.getLongValue(allOptionId), allOption);
		    				cityDTOList.add(cityDTO);
		    				break;
		    			}else {
			    			cityDTO = new CityDTO();
			    			cityDTO.setId(obj.getLong("id"));
			    			cityDTO.setText(obj.getString("name"));
			    			cityDTO.setStateId(obj.getLong("stateId"));
			    			StateObj stateObj = productDAO.getStateObj(cityDTO.getStateId(), 2840);
			    			if(stateObj != null) {
			    				cityDTO.setStateName(stateObj.getText());
			    			}
			    			cityDTOList.add(cityDTO);
		    			}
					}
		        }
				else {
					CityDTO cityDTO = null;
					if(isGeoAll) {
						cityDTO = new CityDTO(StringUtil.getLongValue(allOptionId), allOption, StringUtil.getLongValue(allOptionId), allOption);
					}else {
						cityDTO = new CityDTO(StringUtil.getLongValue(noneOptionId), noneOption, StringUtil.getLongValue(noneOptionId), noneOption);
					}
	    			cityDTOList.add(cityDTO);
				}
				
				// zipList
				JSONArray zip = jsonObject.getJSONArray("zipList");
				if(zip.size()!=0) {
					for(int i=0;i<zip.size();i++){
						JSONObject obj=zip.getJSONObject(i);
		    			ZipDTO zipDTO = null;
						if((obj.getLong("id")+"").equals(noneOptionId)) {
							zipDTO = new ZipDTO(noneOptionId, noneOption, noneOption, StringUtil.getLongValue(noneOptionId), noneOption);
							zipList.add(zipDTO);
							break;
		    			}else if((obj.getLong("id")+"").equals(allOptionId)) {
		    				zipDTO = new ZipDTO(allOptionId, allOption, allOption, StringUtil.getLongValue(allOptionId), allOption);
		    				zipList.add(zipDTO);
		    				break;
		    			}else {
		    				zipDTO = new ZipDTO();
			    			zipDTO.setText(obj.getString("id"));
			    			zipDTO.setCityId(obj.getString("cityId"));
			    			zipDTO.setCityName(obj.getString("cityName"));
			    			zipDTO.setStateId(obj.getLong("stateId"));
			    			StateObj stateObj = productDAO.getStateObj(zipDTO.getStateId(), 2840);
			    			if(stateObj != null) {
			    				zipDTO.setStateName(stateObj.getText());
			    			}
			    			zipList.add(zipDTO);
		    			}
					}
		        }
				else {
					ZipDTO zipDTO = null;
					if(isGeoAll) {
						zipDTO = new ZipDTO(allOptionId, allOption, allOption, StringUtil.getLongValue(allOptionId), allOption);
					}else {
						zipDTO = new ZipDTO(noneOptionId, noneOption, noneOption, StringUtil.getLongValue(noneOptionId), noneOption);
					}
	    			zipList.add(zipDTO);
				}
				
				// stateList
				JSONArray state = jsonObject.getJSONArray("stateList");
				String stateId = "";
				StateObj stateObj = null;
				if(state != null && state.size()>0) {
					stateId = (state.getJSONObject(0).getLong("id"))+"";
					if(!(stateId.equals(allOptionId)) && !(stateId.equals(noneOptionId))) {
						for(int i=0;i<state.size();i++){
			    			JSONObject obj=state.getJSONObject(i);
			    			stateObj = productDAO.getStateObj(obj.getLong("id"), 2840);
			    			if(stateObj != null) {
			    				stateList.add(stateObj);
			    			}
						}
			        }
					else {
						if(stateId.equals(noneOptionId)) {
							stateObj = new StateObj(StringUtil.getLongValue(noneOptionId), noneOption, noneOption);
						}else {
							stateObj = new StateObj(StringUtil.getLongValue(allOptionId), allOption, allOption);
						}
						stateList.add(stateObj);
					}
				}else {
					if(isGeoAll) {
						stateObj = new StateObj(StringUtil.getLongValue(allOptionId), allOption, allOption);
					}else {
						stateObj = new StateObj(StringUtil.getLongValue(noneOptionId), noneOption, noneOption);
					}
					stateList.add(stateObj);
				}
				
				// adUnitList
				JSONArray adUnit = jsonObject.getJSONArray("adUnitList");
				if(adUnit.size()!=0) {
					for(int i=0;i<adUnit.size();i++){
		    			JSONObject obj=adUnit.getJSONObject(i);
		    			AdUnitDTO adUnitDTO = new AdUnitDTO();
		    			adUnitDTO.setId(obj.getLong("id"));
		    			adUnitDTO.setName(obj.getString("name"));
	    			    adUnitDTO.setCanonicalPath(obj.getString("canonicalPath"));
		    			adUnitDTO.setPid(obj.getLong("parentId"));
		    			adUnitList.add(adUnitDTO);
					}
		        }
				
				productsObj.setContext(contextObjList);
				productsObj.setCreative(creativeObjList);
				productsObj.setDevices(deviceObjList);
				productsObj.setDmas(geoTargetsObjList);
				productsObj.setPlatforms(platformObjList);
				productsObj.setCities(cityDTOList);
				productsObj.setAdUnits(adUnitList);
				productsObj.setStates(stateList);
				productsObj.setZips(zipList);
				
				if(AddProductInMemcache(productsObj)) {		// Add in Memcache.
					productDAO.saveObject(productsObj);		// Add in DataStore
					result = true;
				}
			}
		} catch (JSONException je) {
			log.severe("JSONException in saveProductData of ProductService : "+je.getMessage());
			je.printStackTrace();
			throw je;
		}catch (Exception e) {
			log.severe("Exception in saveProductData of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	StateObj getStateForUS(long stateId) throws DataServiceException {
		IProductDAO productDAO = new ProductDAO();
		List<StateObj> stateObjList = productDAO.getStatesForCountry(2840);
		if(stateObjList != null) {
			for(StateObj stateObj : stateObjList) {
				if(stateObj.getId() == stateId)
					return stateObj;
			}
		}
		return null;
	}

	@Override
	public JSONArray loadAllProducts(long userId, boolean isSuperAdmin, int productsPerPage, int pageNumber, String searchKeyword) throws Exception {
		IProductDAO productDAO = new ProductDAO();
		JSONArray jsonArray = new JSONArray();
		List<ProductsObj> productsObjList = new ArrayList<>();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		try {
			if(isSuperAdmin) {
				List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
				if(companyObjList != null && companyObjList.size() > 0) {
					for(CompanyObj companyObj : companyObjList) {
						if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
							List<ProductsObj> list = MemcacheUtil.getProductObj(companyObj.getId()+"");
							if(list == null || list.size() == 0) {
								list = productDAO.getProductByPartnerId(companyObj.getId()+"");
							}
							if(list != null && list.size() > 0) {
								MemcacheUtil.setProductObj(companyObj.getId()+"", list);
								productsObjList.addAll(list);
							}
						}
					}
				}
			}
			else {
				CompanyObj companyObj = userService.getCompanyIdByAdminUserId(userId);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					productsObjList = MemcacheUtil.getProductObj(companyObj.getId()+"");
					if(productsObjList == null || productsObjList.size() == 0) {
						productsObjList = productDAO.getProductByPartnerId(companyObj.getId()+"");
						MemcacheUtil.setProductObj(companyObj.getId()+"", productsObjList);
					}
				}
			}
			if(productsObjList != null && productsObjList.size() > 0) {
				List<PlatformObj> platformObjList = null;
				List<CreativeObj> creativeObjList = null;
				List<DeviceObj> deviceObjList = null;
				List<GeoTargetsObj> geoTargetsObjList = null;
				
				log.info("loaded productsObjList :"+productsObjList.size());
				int start = 0;
				int end = 0;
				
				if(searchKeyword.length() > 0) {
					end = productsObjList.size()-1;
				}
				else {
					start = (pageNumber - 1)*productsPerPage;
					end = (pageNumber*productsPerPage) - 1;
				}
				log.info("start : "+start+", end : "+end);
				for(int i=start;i<=end;i++) {
					if(productsObjList.size() > i) {
						ProductsObj obj = productsObjList.get(i);
						if(searchKeyword.length() == 0 || (searchKeyword.length() > 0 && obj.getProductName() != null && (obj.getProductName().toLowerCase()).contains(searchKeyword))) {
							String platform = allOption;
							String device = allOption;
							String creative = allOption;
							String dma = allOption;
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", obj.getProductId());
							jsonObject.put("name", obj.getProductName());
							jsonObject.put("pubId", obj.getPublisherId());
							jsonObject.put("partnerId", obj.getPartnerId());
							if(obj.getLastUpdatedOn() != null) {
								jsonObject.put("lastUpdate", DateUtil.getFormatedDate(obj.getLastUpdatedOn(), "yyyyMMddHHmmss"));
							}
							else {
								jsonObject.put("lastUpdate", "");
							}
							//jsonObject.put("pubName", obj.getPublisherName());
							//jsonObject.put("geo", obj.isGeoFencing());
							//jsonObject.put("status", obj.isStatus());
							platformObjList = obj.getPlatforms();
							creativeObjList = obj.getCreative();
							deviceObjList = obj.getDevices();
							geoTargetsObjList = obj.getDmas();
							if(platformObjList != null && platformObjList.size() > 0 && platformObjList.get(0).getText() != null) {
								platform = "";
								for(PlatformObj platformObj : platformObjList) {
									platform = platform + platformObj.getText() + ", ";
								}
								platform = StringUtil.deleteFromLastOccurence(platform, ", ");
							}
							if(deviceObjList != null && deviceObjList.size() > 0 && deviceObjList.get(0).getText() != null) {
								device = "";
								for(DeviceObj deviceObj : deviceObjList) {
									device = device + deviceObj.getText() + ", ";
								}
								device = StringUtil.deleteFromLastOccurence(device, ", ");
							}
							if(creativeObjList != null && creativeObjList.size() > 0 && creativeObjList.get(0).getSize() != null && creativeObjList.get(0).getFormat() != null) {
								creative = "";
								for(CreativeObj creativeObj : creativeObjList) {
									if(creativeObj.getSize().equals(noneOption) || creativeObj.getSize().equals(allOption)) {
										creative = creativeObj.getSize();
										break;
									}else {
										creative = creative + creativeObj.getFormat()+" - "+ creativeObj.getSize() + ", ";
									}
								}
								creative = StringUtil.deleteFromLastOccurence(creative, ", ");
							}
							if(geoTargetsObjList != null && geoTargetsObjList.size() > 0 && geoTargetsObjList.get(0).getGeoTargetsName() != null) {
								dma = "";
								for(GeoTargetsObj geoTargetsObj : geoTargetsObjList) {
									dma = dma + geoTargetsObj.getGeoTargetsName() + ", ";
								}
								dma = StringUtil.deleteFromLastOccurence(dma, ", ");
							}else if((obj.getStates() != null && obj.getStates().size() > 0) ||
									(obj.getCities() != null && obj.getCities().size() > 0) ||
									(obj.getZips() != null && obj.getZips().size() > 0)) {
								dma = noneOption;
							}
							jsonObject.put("platform", platform);
							jsonObject.put("device", device);
							jsonObject.put("creative", creative);
							jsonObject.put("dma", dma);
							if(obj.isDemographic()) {
								jsonObject.put("demo", "Yes");
							}
							else {
								jsonObject.put("demo", "No");
							}
							if(obj.isBehaviour()) {
								jsonObject.put("behaviour", "Yes");
							}
							else {
								jsonObject.put("behaviour", "No");
							}
							jsonArray.add(jsonObject);
						}
					}
				}
			}
		}catch (Exception e) {
			log.severe("Exception in loadAllProducts of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonArray;
	}
	
	/*@Override
	public JSONArray loadAllProducts(long userId, boolean isSuperAdmin) throws Exception {
		IProductDAO productDAO = new ProductDAO();
		JSONArray jsonArray = new JSONArray();
		try {
			IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
			//if(isSuperAdmin) {
			//get all products
			//}
			///else {
				CompanyObj companyObj = userService.getCompanyIdByAdminUserId(userId);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
						List<ProductsObj> productsObjList = MemcacheUtil.getProductObj(companyObj.getAdServerId().get(0));
						if(productsObjList == null || productsObjList.size() == 0) {
							productsObjList = productDAO.getProductByPublisherId(companyObj.getAdServerId().get(0));
							MemcacheUtil.setProductObj(companyObj.getAdServerId().get(0), productsObjList);
						}
						if(productsObjList != null && productsObjList.size() > 0) {
							for(ProductsObj obj : productsObjList) {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("id", obj.getProductId());
								jsonObject.put("name", obj.getProductName());
								jsonObject.put("pubId", obj.getPublisherId());
								jsonObject.put("pubName", obj.getPublisherName());
								//jsonObject.put("status", obj.isStatus());
								jsonObject.put("geo", obj.isGeoFencing());
								jsonObject.put("demo", obj.isDemographic());
								jsonObject.put("behaviour", obj.isBehaviour());
								jsonArray.add(jsonObject);
							}
						}
					}
				}
			//}
		}catch (Exception e) {
			log.severe("Exception in loadAllProducts of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonArray;
	}*/

	@Override
	public JSONObject initEditProduct(long productId, String partnerId) throws Exception {
		JSONObject jsonObject = new JSONObject();
		IProductDAO productDAO = new ProductDAO();
		DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
		try {
			boolean isGeoAll = true;
			ProductsObj productsObj = getProductFromMemcache(productId, partnerId);
			if(productsObj == null) {
				log.info("not in memcache getting from DataStore");
				productsObj = productDAO.getProductById(productId);
			}
			if(productsObj != null) {
				jsonObject.put("productID", productsObj.getProductId());
				jsonObject.put("productName", productsObj.getProductName());
				jsonObject.put("publisherID", productsObj.getPublisherId());
				jsonObject.put("partnerId", productsObj.getPartnerId());
				jsonObject.put("publisherName", productsObj.getPublisherName());
				jsonObject.put("deviceCapability", productsObj.getDeviceCapability());
				jsonObject.put("note", productsObj.getNote());
				if(productsObj.getRate() > 0) {
					jsonObject.put("rate", "$"+df.format(productsObj.getRate()));
				}
				else {
					jsonObject.put("rate", "$0.00");
				}
				//jsonObject.put("status", productsObj.isStatus());
				jsonObject.put("geoFencing", productsObj.isGeoFencing());
				jsonObject.put("demographic", productsObj.isDemographic());
				jsonObject.put("behaviour", productsObj.isBehaviour());
				
				// create context objects array
				JSONArray contextJsonArray = new JSONArray();
				List<IABContextObj> contextObjList = productsObj.getContext();
				if(contextObjList != null && contextObjList.size() > 0) {
					for(IABContextObj obj : contextObjList) {
						JSONObject contextJsonObj = new JSONObject();
						contextJsonObj.put("id", obj.getId());
						contextJsonObj.put("group", obj.getGroup());
						contextJsonObj.put("subGroup", obj.getSubgroup());
						contextJsonArray.add(contextJsonObj);
					}
				}else {
					contextJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","group","subGroup"}, true));
				}
				jsonObject.put("contextList", contextJsonArray);
				
				// create creative objects array
				JSONArray creativeJsonArray = new JSONArray();
				List<CreativeObj> creativeObjList = productsObj.getCreative();
				if(creativeObjList != null && creativeObjList.size() > 0) {
					for(CreativeObj obj : creativeObjList) {
						JSONObject creativeJsonObj = new JSONObject();
						creativeJsonObj.put("id", obj.getId());
						creativeJsonObj.put("format", obj.getFormat());
						creativeJsonObj.put("size", obj.getSize());
						creativeJsonObj.put("text", obj.getFormat()+"-"+obj.getSize());
						creativeJsonArray.add(creativeJsonObj);
					}
				}else {
					creativeJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","format","size"}, true));
				}
				jsonObject.put("creativeList", creativeJsonArray);
	
				// create device objects array
				JSONArray deviceJsonArray = new JSONArray();
				List<DeviceObj> deviceObjList = productsObj.getDevices();
				if(deviceObjList != null && deviceObjList.size() > 0) {
					for(DeviceObj obj : deviceObjList) {
						JSONObject deviceJsonObj = new JSONObject();
						deviceJsonObj.put("id", obj.getId());
						deviceJsonObj.put("text", obj.getText());
						deviceJsonArray.add(deviceJsonObj);
					}
				}else {
					deviceJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
				}
				jsonObject.put("deviceList", deviceJsonArray);
				
				// GEO TARGET
				List<GeoTargetsObj> geoTargetsObjList = productsObj.getDmas();
				List<CityDTO> cityDTOList = productsObj.getCities();
				List<ZipDTO> zipList = productsObj.getZips();
				List<StateObj> stateDTOList = productsObj.getStates();
				if((stateDTOList != null && stateDTOList.size() > 0) ||
						(zipList != null && zipList.size() > 0) ||
						(cityDTOList != null && cityDTOList.size() > 0) ||
						(geoTargetsObjList != null && geoTargetsObjList.size() > 0)) {
					isGeoAll = false;
				}
				
				// create dma objects array
				JSONArray dmaJsonArray = new JSONArray();
				if(geoTargetsObjList != null && geoTargetsObjList.size() > 0) {
					for(GeoTargetsObj obj : geoTargetsObjList) {
						JSONObject dmaJsonObj = new JSONObject();
						dmaJsonObj.put("text", obj.getGeoTargetsName());
						dmaJsonObj.put("id", obj.getGeoTargetId());
						dmaJsonArray.add(dmaJsonObj);
					}
				}
				else if(isGeoAll) {
					dmaJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
				}else {
					dmaJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, false));
				}
				jsonObject.put("dmaList", dmaJsonArray);
				
				// create platform objects array
				JSONArray platformJsonArray = new JSONArray();
				List<PlatformObj> platformObjList = productsObj.getPlatforms();
				if(platformObjList != null && platformObjList.size() > 0) {
					for(PlatformObj obj : platformObjList) {
						JSONObject platformJsonObj = new JSONObject();
						platformJsonObj.put("id", obj.getId());
						platformJsonObj.put("text", obj.getText());
						platformJsonArray.add(platformJsonObj);
					}
				}else {
					platformJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text"}, true));
				}
				jsonObject.put("platformList", platformJsonArray);
				
				// create city objects array
				JSONArray cityJsonArray = new JSONArray();
				if(cityDTOList != null && cityDTOList.size() > 0) {
					for(CityDTO obj : cityDTOList) {
						JSONObject cityJsonObj = new JSONObject();
						cityJsonObj.put("id", obj.getId());
						cityJsonObj.put("name", obj.getText());
						cityJsonObj.put("stateId", obj.getStateId());
						cityJsonArray.add(cityJsonObj);
					}
				}
				else if(isGeoAll) {
					cityJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","stateId"}, true));
				}else {
					cityJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","stateId"}, false));
				}
				jsonObject.put("cityList", cityJsonArray);
				
				// create zip objects array
				JSONArray zipJsonArray = new JSONArray();
				if(zipList != null && zipList.size() > 0) {
					for(ZipDTO obj : zipList) {
						if(obj.getText().equals(allOptionId) || obj.getText().equals(allOption)) {
							zipJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, true));
						}else if(obj.getText().equals(noneOptionId) || obj.getText().equals(noneOption)) {
							zipJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, false));
						}
						else {
							JSONObject zipJsonObj = new JSONObject();
							zipJsonObj.put("id", obj.getText());
							zipJsonObj.put("name", obj.getText()+" - "+obj.getCityName());
							zipJsonObj.put("cityId", obj.getCityId());
							zipJsonObj.put("cityName", obj.getCityName());
							zipJsonObj.put("stateId", obj.getStateId());
							zipJsonArray.add(zipJsonObj);
						}
					}
				}
				else if(isGeoAll) {
					zipJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, true));
				}else {
					zipJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, false));
				}
				jsonObject.put("zipList", zipJsonArray);
				
				// create state objects array
				JSONArray stateJsonArray = new JSONArray();
				if(stateDTOList != null && stateDTOList.size() > 0) {
					for(StateObj obj : stateDTOList) {
						JSONObject stateJsonObj = new JSONObject();
						stateJsonObj.put("id", obj.getId());
						stateJsonObj.put("text", obj.getText());
						stateJsonArray.add(stateJsonObj);
					}
				}
				else if(isGeoAll) {
					stateJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","name"}, true));
				}else {
					stateJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","name"}, false));
				}
				jsonObject.put("stateList", stateJsonArray);
				
				// create AdUnit array
				JSONArray adUnitJsonArray = new JSONArray();
				List<AdUnitDTO> adUnitDTOList = productsObj.getAdUnits();
				if(adUnitDTOList != null && adUnitDTOList.size() > 0) {
					for(AdUnitDTO obj : adUnitDTOList) {
						JSONObject adUnitJsonObj = new JSONObject();
						adUnitJsonObj.put("id", obj.getId());
						adUnitJsonObj.put("name", obj.getName());
						adUnitJsonObj.put("canonicalPath", obj.getCanonicalPath());
						adUnitJsonObj.put("parentId", obj.getPid());
						adUnitJsonObj.put("text", obj.getName()+"   ["+obj.getCanonicalPath()+"]");
						adUnitJsonArray.add(adUnitJsonObj);
					}
				}else {
					//adUnitJsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","canonicalPath","parentId","text"}, true));
				}
				jsonObject.put("adUnitList", adUnitJsonArray);
			}
			else {
				log.info("No product object with productId : "+productId);
			}
		} catch (JSONException je) {
			log.severe("JSONException : initEditProduct: "+je.getMessage());
			je.printStackTrace();
			throw je;
		}catch (DataServiceException de) {
			log.severe("DataServiceException : initEditProduct: "+de.getMessage());
			de.printStackTrace();
			throw de;
		}
		catch (Exception e) {
			log.severe("Exception : initEditProduct: "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonObject;
	}

	@Override
	public boolean deleteProduct(long productId, String partnerId) throws Exception {
		boolean isDeleted = false;
		try {
			IProductDAO productDAO = new ProductDAO();
			ProductsObj productsObj = deleteProductInMemcache(productId, partnerId);	// delete in memcache and get deleted object.
			if(productsObj == null) {													// if not found in Memcache.
				productsObj = productDAO.getProductById(productId);
			}
			if(productsObj != null) {
					productDAO.deleteObject(productsObj);
					isDeleted = true;
			}
		}
		catch(DataServiceException e) {
			log.severe("DataServiceException : deleteProduct: "+e.getMessage());
			e.printStackTrace();
			throw e;
		}catch(Exception e) {
			log.severe("Exception : deleteProduct: "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return isDeleted;
	}

	/*
	 * This method will search all products in datastore based on input parameters
	 * @author Youdhveer Panwar
	 * @see com.lin.web.service.IProductService#searchProducts(java.lang.String, java.lang.String)
	 * 
	 * @param   String adFormat,String size,String contextCategories,String contextSubCategories,String dma,
	 *  		String state,String city,String zip,String device,String platform
	 *        
	 * @return List<ProductsObj>  productList
	 */
	public List<ProductsObj> searchProducts(String adFormat,String size,String contextCategories,String contextSubCategories,
			String dma,String state,String city,String zip,String device,String platform){
		log.info("adFormat:"+adFormat+" and size:"+size+" and dma:"+dma+" and contextCategories:"+contextCategories);
		
		List<ProductsObj> productList=new ArrayList<ProductsObj>();
		try {
			IProductDAO productDAO = new ProductDAO();
			String [] adFormatArray=null;
			List<String> adFormatList=null;
			String [] sizeArray=null;
			List<String> sizeList=null;
			String [] contextArray=null;
			List<String> contextList=null;
			String [] contextSubArray=null;
			List<String> contextSubList=null;
			String [] dmaArray=null;
			List<String> dmaList=null;
			String [] stateArray=null;
			List<String> stateList=null;
			String [] cityArray=null;
			List<String> cityList=null;
			String [] zipArray=null;
			List<String> zipList=null;
			String [] deviceArray=null;
			List<String> deviceList=null;
			String [] platformArray=null;
			List<String> platformList=null;
			
			if(adFormat !=null && adFormat.trim().length()>0 && !adFormat.equals(noneOption)){
				adFormatArray=adFormat.split(",");
				adFormatList=new ArrayList<String>();
				Collections.addAll(adFormatList, adFormatArray);
				adFormatList.add(allOption);
				
			}
			if(size !=null && size.trim().length()>0 && !size.equals(noneOption)){
				sizeArray=size.split(",");
				sizeList=new ArrayList<String>();
				Collections.addAll(sizeList, sizeArray);
				sizeList.add(allOption);
			}
			if(contextCategories !=null && contextCategories.trim().length()>0 && !contextCategories.equals(noneOption)){
				contextArray=contextCategories.split(",");
				contextList=new ArrayList<String>();
				Collections.addAll(contextList, contextArray);
				contextList.add(allOption);
			}
			if(contextSubCategories !=null && contextSubCategories.trim().length()>0 && !contextSubCategories.equals(noneOption)){
				contextSubArray=contextSubCategories.split(",");
				contextSubList=new ArrayList<String>();
				Collections.addAll(contextSubList, contextSubArray);
				contextSubList.add(allOption);
			}
			if(dma !=null && dma.trim().length()>0 && !dma.equals(noneOption)){
				dmaArray=dma.split("\\|");		
				dmaList=new ArrayList<String>();
				Collections.addAll(dmaList, dmaArray);
				dmaList.add(allOption);
			}
			
			if(state !=null && state.trim().length()>0 && !state.equals(noneOption)){
				stateArray=state.split(",");
				stateList=new ArrayList<String>();
				Collections.addAll(stateList, stateArray);
				stateList.add(allOption);
			}
			if(city !=null && city.trim().length()>0 && !city.equals(noneOption)){
				cityArray=city.split(",");
				cityList=new ArrayList<String>();
				Collections.addAll(cityList, cityArray);
				cityList.add(allOption);
			}
			if(zip !=null && zip.trim().length()>0 && !zip.equals(noneOption)){
				zipArray=zip.split(",");
				zipList=new ArrayList<String>();
				Collections.addAll(zipList, zipArray);
				zipList.add(allOption);
			}
			if(device !=null && device.trim().length()>0 && !device.equals(noneOption)){
				deviceArray=device.split(",");				
				deviceList=new ArrayList<String>();
				Collections.addAll(deviceList, deviceArray);
				deviceList.add(allOption);
			}
			if(platform !=null && platform.trim().length()>0 && !platform.equals(noneOption)){
				platformArray=platform.split(",");
				platformList=new ArrayList<String>();
				Collections.addAll(platformList, platformArray);
				platformList.add(allOption);
			}
			
			/*productList=productDAO.searchProductsInDataStore(adFormatArray, sizeArray, 
					contextArray,contextSubArray,dmaArray,stateArray,cityArray,zipArray,deviceArray,platformArray);*/
			productList=productDAO.searchProductsInDataStore(adFormatList, sizeList, 
					contextList,contextSubList,dmaList,stateList,cityList,zipList,deviceList,platformList);
			log.info("productList :"+productList.size());
			
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
		}
		return productList;		
	}
	
	
	/*
	 * This method will search all products in datastore based on input parameters
	 * @author Youdhveer Panwar
	 * @see com.lin.web.service.IProductService#searchProducts(com.lin.server.bean.SmartCampaignPlacementObj)
	 * 
	 * @param   SmartCampaignPlacementObj - campaignPlacementObj
	 *        
	 * @return List<ProductsObj>  productList
	 */
	public List<ProductsObj> searchProducts(SmartCampaignPlacementObj campaignPlacementObj){
		log.info("search product for placement id-"+campaignPlacementObj.getPlacementId()+" : name- "+
					campaignPlacementObj.getPlacementName());
		
		// Check if placement has preselected products(i.e. from map). Then load those products only.
		if(campaignPlacementObj.getSelectedPlacementProducts() != null && campaignPlacementObj.getSelectedPlacementProducts().length() > 0){
			try {
			return	new ProductDAO().getProductByIds(Arrays.asList(campaignPlacementObj.getSelectedPlacementProducts().split(",")));
			} catch (DataServiceException e) {
				log.severe("Error in fetching selected product list of placement.["+campaignPlacementObj.getSelectedPlacementProducts()+"]"+ e.getMessage());
				return null;
			} 
		}
		
		boolean isNone = false;
		List<String> adFormatList=null;
		List<String> sizeList=null;
		List<String> contextList=null;
		List<String> contextSubList=null;
		List<String> dmaList=null;
		List<String> stateList=null;
		List<String> cityList=null;
		List<String> zipList=null;
		List<String> deviceList=null;
		List<String> platformList=null;
		
		List<CreativeObj> creativeList=campaignPlacementObj.getCreativeObj();
		isNone = false;
		if(creativeList !=null) {
			sizeList=new ArrayList<String>();
			adFormatList=new ArrayList<String>();
			for(CreativeObj creative:creativeList){
				String size=creative.getSize();
				if(!size.equals(noneOption) && !sizeList.contains(size)){
					sizeList.add(size);
				}else if(size.equals(noneOption)){
					isNone = true;
					break;
				}			
				String format=creative.getFormat();
				if(!format.equals(noneOption) && !adFormatList.contains(format)){
					adFormatList.add(format);
				}else if(format.equals(noneOption)){
					isNone = true;
					break;
				}
			}	
			if(sizeList.size()>0 && !isNone){
				sizeList.add(allOption);
			}
			if(adFormatList.size()>0 && !isNone){
				adFormatList.add(allOption);
			}
			log.info("sizeArray --"+sizeList+" \nadFormatArray --"+adFormatList);		
		}
		
		List<IABContextObj> contextObjList=campaignPlacementObj.getContextObj();
		isNone = false;
		if(contextObjList !=null){
			contextList=new ArrayList<String>();
			contextSubList=new ArrayList<String>();	
			for(IABContextObj context:contextObjList){
				String group=context.getGroup();
				String subgroup=context.getSubgroup();
				if(group !=null && group.trim().length()>0 && !group.equals(noneOption) && contextList.indexOf(group)==-1){
					contextList.add(group);
				}else if(group.equals(noneOption)){
					isNone = true;
					break;
				}				
				if(subgroup !=null && subgroup.trim().length()>0 && !subgroup.equals(noneOption) && contextSubList.indexOf(subgroup)==-1){
					contextSubList.add(subgroup);
				}else if(subgroup.equals(noneOption)){
					isNone = true;
					break;
				}			
			}
			if(contextList.size()>0 && !isNone){
				contextList.add(allOption);
			}
			if(contextSubList.size()>0 && !isNone){
				contextSubList.add(allOption);
			}
			
			log.info("contextArray : group:"+contextList+" \nsubGroup :"+contextSubList);
		}
		
		List<GeoTargetsObj> geoTargetList=campaignPlacementObj.getGeoObj();
		isNone = false;
		if(geoTargetList !=null){
			dmaList=new ArrayList<String>();			
			for(GeoTargetsObj target:geoTargetList){
				String geoTarget=target.getGeoTargetsName();
				if(!geoTarget.equals(noneOption) && dmaList.indexOf(geoTarget)==-1){
					dmaList.add(geoTarget);	
				}else if(geoTarget.equals(noneOption)){
					isNone = true;
					break;
				}						
			}
			if(!isNone) {
				dmaList.add(allOption);
			}
			log.info("dmaList --"+dmaList);	
		}
		
		List<StateObj> stateObjList=campaignPlacementObj.getStateObj();
		isNone = false;
		if(stateObjList !=null){
			stateList=new ArrayList<String>();			
			for(StateObj state:stateObjList){
				String name=state.getText();
				if(!name.equals(noneOption) && stateList.indexOf(name)==-1){
					stateList.add(name);	
				}else if(name.equals(noneOption)){
					isNone = true;
					break;
				}							
			}if(!isNone) {
				stateList.add(allOption);
			}
			log.info("stateList --"+stateList);			
		}
		
		List<CityDTO> cityObjList=campaignPlacementObj.getCityObj();
		isNone = false;
		if(cityObjList !=null){
			cityList=new ArrayList<String>();			
			for(CityDTO city:cityObjList){
				String name=city.getText();
				if(!name.equals(noneOption) && cityList.indexOf(name) == -1){
					cityList.add(name);	
				}else if(name.equals(noneOption)){
					isNone = true;
					break;
				}
			}
			if(!isNone) {
				cityList.add(allOption);
			}
			log.info("cityList --"+cityList);			
		}
		
		List<String> zipObjList=campaignPlacementObj.getZipList();
		isNone = false;
		if(zipObjList !=null){
			zipList=new ArrayList<String>();			
			for(String zip:zipObjList){				
				if(!zip.equals(noneOption) && zipList.indexOf(zip)==-1){
					zipList.add(zip);				
				}else if(zip.equals(noneOption)){
					isNone = true;
					break;
				}
			}
			if(!isNone) {
				zipList.add(allOption);
			}
			log.info("zipList --"+zipList);
		}
		
		List<DeviceObj> deviceObjList=campaignPlacementObj.getDeviceObj();
		isNone = false;
		if(deviceObjList !=null){
			deviceList=new ArrayList<String>();			
			for(DeviceObj device:deviceObjList){
				String name=device.getText();
				if(!name.equals(noneOption) && deviceList.indexOf(name)==-1){
					deviceList.add(name);				
				}else if(name.equals(noneOption)){
					isNone = true;
					break;
				}
			}
			if(!isNone) {
				deviceList.add(allOption);
			}
			log.info("deviceList --"+deviceList);
		}
		
		List<PlatformObj> platformObjList=campaignPlacementObj.getPlatformObj();
		isNone = false;
		if(platformObjList !=null){
			platformList=new ArrayList<String>();			
			for(PlatformObj platForm:platformObjList){
				String name=platForm.getText();
				if(!name.equals(noneOption) && platformList.indexOf(name)==-1){
					platformList.add(name);				
				}else if(name.equals(noneOption)){
					isNone = true;
					break;
				}
			}
			if(!isNone) {
				platformList.add(allOption);
			}
			log.info("platforms --"+platformList);
		}
		
		
		List<ProductsObj> productList=new ArrayList<ProductsObj>();
		try {
			IProductDAO productDAO = new ProductDAO();
			productList=productDAO.searchProductsInDataStore(adFormatList, sizeList, contextList, 
					contextSubList, dmaList, stateList, cityList, zipList, deviceList, platformList);
			log.info("searched products :"+productList.size()+" for placement-"+campaignPlacementObj.getPlacementId());
			if(productList !=null){
				log.info("Checking if there is any product with blank adUnits..");
				Iterator<ProductsObj> itr=productList.iterator();
				while(itr.hasNext()){
					ProductsObj product=itr.next();
					List<AdUnitDTO> adUnitDTOs=product.getAdUnits();
					if(adUnitDTOs ==null){
						//remove those products from list who don't have adUnits
						itr.remove();
						log.info("removed preduct from search list as it does not have adUnits.. Id:"+product.getId());
					}
				}				
			}
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
		}
		return productList;		
	}
	
	public JSONObject getProductJsonData(List<ProductsObj> productList){
		JSONObject productJson=new JSONObject();
		JSONArray productArray=new JSONArray();
		if(productList !=null){
			for(ProductsObj product:productList) {
				JSONObject prodJson=new JSONObject();
				prodJson.put("name", product.getProductName());
				prodJson.put("publisherId", product.getPublisherId());
				prodJson.put("publisherName", product.getPublisherName());
				prodJson.put("partnerId", product.getPartnerId());
				/*prodJson.put("status", product.isStatus());
				prodJson.put("behaviour", product.isBehaviour());
				prodJson.put("geo_fancing", product.isGeoFencing());
				prodJson.put("demographic", product.isDemographic());*/
				productArray.add(prodJson);
			}
		}		
		productJson.put("products", productArray);
		return productJson;
	}
	
	/*
	 * Allocate impression based on available forecasting inventory to each partner on equal ratio
	 *  and then divide further product wise for each partner
	 */
	public Map<String,Long> allocateImpressionByProduct(List<ProductsObj> productList, 
			Map<String,ForcastDTO> productForecastMap, long goalQty){
		log.info("Going to allocate impression...step 1..");
		long totalAvailableImpression=0;
		long remainingImpressionForOneAmplify=0;
		Map<String,Long> partnerWiseAvailableImpressionMap=new HashMap<String, Long>();
		Map<String,Long> partnerWiseAllocatedImpressionMap=new HashMap<String, Long>();
		
		Map<String,Long> productWiseAvailableImpressionMap=new HashMap<String, Long>();
		Map<String,Long> productWiseAllocatedImpressionMap=new HashMap<String, Long>();
		Map<String, List<ProductsObj>> partnerWiseProductMap=new HashMap<String, List<ProductsObj>>();
		
		if(productList !=null && productList.size()>0 && productForecastMap!=null && productForecastMap.size()>0){			
			for(ProductsObj product:productList){
				String publisherId=product.getPublisherId();
				String productId=product.getProductId()+"";
				log.info("get forecasted product from map --productId:"+productId);
				ForcastDTO forecastProduct=productForecastMap.get(productId);
				if(forecastProduct !=null){
					log.info("forecastProduct : "+forecastProduct.toString());
					totalAvailableImpression=totalAvailableImpression+forecastProduct.getAvailableImpressions();
					productWiseAvailableImpressionMap.put(productId, forecastProduct.getAvailableImpressions());
					
					long publisherWiseImp=0;
					if(partnerWiseAvailableImpressionMap.get(publisherId)==null){
						publisherWiseImp=forecastProduct.getAvailableImpressions();
						List<ProductsObj> products=new ArrayList<ProductsObj>();
						products.add(product);
						partnerWiseProductMap.put(publisherId, products);
					}else{
						publisherWiseImp=partnerWiseAvailableImpressionMap.get(publisherId)+ 
								forecastProduct.getAvailableImpressions();
						List<ProductsObj> products=partnerWiseProductMap.get(publisherId);
						products.add(product);
						partnerWiseProductMap.put(publisherId, products);
					}
					partnerWiseAvailableImpressionMap.put(publisherId, publisherWiseImp);		
				}
						
			}
			log.info("total impression (all choosed products):"+totalAvailableImpression+" and goalQty:"+goalQty);
			log.info("Going to allocate ..step 2");
			if(totalAvailableImpression > 0){
				long partnerWiseAllocatedImpression=0;
				//double ratio=StringUtil.getDoubleValue(  (double) (goalQty*1.0/totalAvailableImpression*1.0)+"",6);
				double ratio= (double) (goalQty*1.0/totalAvailableImpression*1.0);
				log.info("allocation ratio---"+ratio);
				for(String key:partnerWiseAvailableImpressionMap.keySet()){
					long publisherWiseImp=partnerWiseAvailableImpressionMap.get(key);
					
					if(ratio >=1.0){
						ratio=1.0;
					}
					
					//double allocatedImpressionD= (ratio*publisherWiseImp);
					//allocatedImpressionD=Math.round((allocatedImpressionD*100.0))/100.0;
					long allocatedImpression=Math.round((ratio*publisherWiseImp));
					
					
					log.info("publisher:"+key+" , allocatedImpression:"+allocatedImpression 
							+" and available publisherWiseImp:"+publisherWiseImp);
					if(allocatedImpression>goalQty){
						allocatedImpression=goalQty;
					}						
					partnerWiseAllocatedImpressionMap.put(key, allocatedImpression);
					partnerWiseAllocatedImpression=partnerWiseAllocatedImpression + allocatedImpression;
				}
				remainingImpressionForOneAmplify=goalQty-partnerWiseAllocatedImpression;
				log.info("remainingImpressionForOneAmplify:"+remainingImpressionForOneAmplify);
				
				log.info("Step 3-- Allocate impressions product wise for a partner...");
				//now allocated inventory on product wise for each partner
				for(String key:partnerWiseAvailableImpressionMap.keySet()){
					long partnerAvailableImp=partnerWiseAvailableImpressionMap.get(key);
					long partnerGoal=partnerWiseAllocatedImpressionMap.get(key);
					
					
					List<ProductsObj> products=partnerWiseProductMap.get(key);
					if(products.size()==1){
						ProductsObj product=products.get(0);
						productWiseAllocatedImpressionMap.put(product.getProductId()+"", partnerGoal);
						
						log.info("product:"+product.getProductId()+" , allocatedImpression:"+partnerGoal 
								+" and partnerWiseAvailableImp:"+partnerAvailableImp);
					}else if(products.size()>1){
						if(partnerAvailableImp >0){
							//double productWiseRatio=StringUtil.getDoubleValue( (double)(partnerGoal*1.0/partnerAvailableImp*1.0)+"",6);
							double productWiseRatio=(double)(partnerGoal*1.0/partnerAvailableImp*1.0);
							log.info("product wise allocation ratio --"+productWiseRatio);
							for(ProductsObj product:products){
								String productId=product.getProductId()+"";
								long availableImpForProduct=productWiseAvailableImpressionMap.get(productId);
								
								
								if(productWiseRatio >=1.0){
									productWiseRatio=1.0;
								}
								//double allocatedImpForProductD= (productWiseRatio*availableImpForProduct);
								//allocatedImpForProductD=Math.round((allocatedImpForProductD*100.0))/100.0;
								//long allocatedImpForProduct=(long) allocatedImpForProductD;
								long allocatedImpForProduct=Math.round(productWiseRatio*availableImpForProduct);
								if(allocatedImpForProduct>partnerGoal){
									allocatedImpForProduct=partnerGoal;
								}
								productWiseAllocatedImpressionMap.put(product.getProductId()+"", allocatedImpForProduct);
								
								
								log.info("product:"+product.getProductId()+" , allocatedImpression:"+allocatedImpForProduct 
										+" and partnerWiseAvailableImp:"+partnerAvailableImp);
                                    								
							}
						}
						
					}
				}
			}
		
			
		}
		//return partnerWiseAllocatedImpressionMap;
		return productWiseAllocatedImpressionMap;
	}
	
	
	/*
	 * Allocate impression based on available forecasting inventory to each partner on equal ratio
	 */
	public Map<String,Long> allocateImpressionByPublisher(List<ProductsObj> productList, 
			Map<String,ForcastDTO> productForecastMap, long goalQty){
		
		long totalAvailableImpression=0;
		long remainingImpressionForOneAmplify=0;
		Map<String,Long> partnerWiseAvailableImpressionMap=new HashMap<String, Long>();
		Map<String,Long> partnerWiseAllocatedImpressionMap=new HashMap<String, Long>();
		if(productList !=null){			
			for(ProductsObj product:productList){
				String publisherId=product.getPublisherId();
				String productId=product.getProductId()+"";
				ForcastDTO forecastProduct=productForecastMap.get(productId);
				totalAvailableImpression=totalAvailableImpression+forecastProduct.getAvailableImpressions();
				long publisherWiseImp=0;
				if(partnerWiseAvailableImpressionMap.get(publisherId)==null){
					publisherWiseImp=forecastProduct.getAvailableImpressions();
				}else{
					publisherWiseImp=partnerWiseAvailableImpressionMap.get(publisherId)+ 
							forecastProduct.getAvailableImpressions();
				}
				partnerWiseAvailableImpressionMap.put(publisherId, publisherWiseImp);				
			}
			log.info("total impression (all choosed products):"+totalAvailableImpression+" and goalQty:"+goalQty);
			 
			long partnerWiseAllocatedImpression=0;
			double ratio=goalQty/totalAvailableImpression;
			System.out.println("Ratio---"+ratio);
			for(String key:partnerWiseAvailableImpressionMap.keySet()){
				long publisherWiseImp=partnerWiseAvailableImpressionMap.get(key);
				
				if(ratio >=1.0){
					ratio=1.0;
				}
				double allocatedImpressionD= (ratio*publisherWiseImp);
				allocatedImpressionD=Math.round((allocatedImpressionD*100.0))/100.0;
				long allocatedImpression=(long) allocatedImpressionD;
				
				log.info("publisher:"+key+" , allocatedImpression:"+allocatedImpression 
						+" and available publisherWiseImp:"+publisherWiseImp);
				partnerWiseAllocatedImpressionMap.put(key, allocatedImpression);
				partnerWiseAllocatedImpression=partnerWiseAllocatedImpression + allocatedImpression;
			}
			remainingImpressionForOneAmplify=goalQty-partnerWiseAllocatedImpression;
			log.info("remainingImpressionForOneAmplify:"+remainingImpressionForOneAmplify);			
		}
		return partnerWiseAllocatedImpressionMap;
	}
	
	/*
	 * Create placements after allocating impressions using equal sell-through method
	 * 
	 * @author Youdhveer Panwar
	 */
	public List<PlacementObj> createPlacements(Map<String,Long> partnerWiseAllocationMap,SmartCampaignObj smartCampaign){
		PlacementObj placement=null;
		List<PlacementObj> placementList=null;
		if(smartCampaign !=null){
			placementList=new ArrayList<PlacementObj>();
			
			//long goal=StringUtil.getLongValue(smartCampaign.getGoal());
			//double rate=StringUtil.getDoubleValue(smartCampaign.getRate(),2);
			long goal=10000;
			double rate=2.5;
			//double budget=StringUtil.getDoubleValue(smartCampaign.getBudget(),2);
			long totalImpressions=0;
			for(String partnerId:partnerWiseAllocationMap.keySet()){
				placement=new PlacementObj();
				String placementId=partnerId+"_"+smartCampaign.getName();
				long allocatedImpression=partnerWiseAllocationMap.get(partnerId);				
				totalImpressions=totalImpressions+allocatedImpression;
				
				double calculatedBudget=ReportUtil.calculateBudget(allocatedImpression, rate);
								
				placement.setId(placementId);
				placement.setPlacementName(placementId);
				placement.setProposedImpression(allocatedImpression);
				placement.setEffectiveCPM(rate);
				placement.setPublisherCPM(rate);
				placement.setNetCostCPM(rate);
				placement.setBudgetAllocation(calculatedBudget);
				placement.setNetCost(calculatedBudget);
				placement.setPublisherPayout(calculatedBudget);
				placement.setGrossRevenue(calculatedBudget);
				placement.setNetRevenue(0.0);
				placement.setServingFees(0.0);
				placement.setMarginPercent(0);
				placement.setFirstPartyAdServerCost(0.0);
				placement.setThirdPartyAdServerCost(0.0);
				placement.setStatus("1");
				
				placementList.add(placement);
				
			}
			
			if(totalImpressions < goal){
				long impressionLeftOut=goal-totalImpressions;
				placement=new PlacementObj();
				String placementId="OpenRTB"+"_"+smartCampaign.getName();
				double calculatedBudget=ReportUtil.calculateBudget(impressionLeftOut, rate);

				placement.setId(placementId);
				placement.setPlacementName(placementId);
				placement.setProposedImpression(impressionLeftOut);
				placement.setEffectiveCPM(rate);
				placement.setPublisherCPM(rate);
				placement.setNetCostCPM(rate);
				placement.setBudgetAllocation(calculatedBudget);
				placement.setNetCost(calculatedBudget);
				placement.setPublisherPayout(calculatedBudget);
				placement.setGrossRevenue(calculatedBudget);
				placement.setNetRevenue(0.0);
				placement.setServingFees(0.0);
				placement.setMarginPercent(0);
				placement.setFirstPartyAdServerCost(0.0);
				placement.setThirdPartyAdServerCost(0.0);
				placement.setStatus("0");
				
				placementList.add(placement);
			}
		}
		
		return placementList;		
	}
	
	
	public SmartMediaPlanObj createSmartMediaPlan(Map<String,List<ProductsObj>> campaignProducts, 
			Map<String,ForcastDTO> productForecastMap,
			Map<String,Map<String,Long>> campaignProductsAllocationMap, UnifiedCampaignDTO campaignDTO, String planType){
		log.info("Create smart media plan.....");
		IUserService userService=(IUserService) BusinessServiceLocator.locate(IUserService.class);
		Map<String,String> partnerLogoMap=new HashMap<String, String>();
		
		SmartMediaPlanObj smartMediaPlanObj=null;
		//assumption for product rate
		double productRate=2.00;
		double revenueSharePercent=70;
		String overflowPartnerName=LinMobileConstants.OVER_FLOW_PARTNER_NAME;
		String overflowProductName="";
		if(planType!=null && planType.equals("auto")){
			 overflowProductName=LinMobileConstants.OVER_FLOW_PRODUCT_NAME;
		}
		//String overflowProductName=LinMobileConstants.OVER_FLOW_PRODUCT_NAME;
		
		double totalCampaignBudget=0.0;
		long totalImpressionsForCampaign=0;
		double servingFeeAmount=0.04;
		
		//long goal=StringUtil.getLongValue(placement.getGoal());
		//double rate=StringUtil.getDoubleValue(placement.getRate(),2);
		double totalPayout=0.0;
		
		List<SmartCampaignPlacementObj> placementList=null;
		if(campaignDTO !=null){
			placementList=campaignDTO.getPlacementByCampaignList();
			smartMediaPlanObj=new SmartMediaPlanObj();
			//smartMediaPlanObj.setId(campaignDTO.getId());  //keep it auto generated
			smartMediaPlanObj.setCampaignId(campaignDTO.getId()+"");
			smartMediaPlanObj.setCampaignName(campaignDTO.getCampaignName());
			smartMediaPlanObj.setBudget(StringUtil.getDoubleValue(campaignDTO.getBudget()));
			String localAdvertiserId=campaignDTO.getSelectedAdvertiser();
			smartMediaPlanObj.setLocalAdvertiserId(StringUtil.getLongValue(localAdvertiserId));			
			String localAdgencyId=campaignDTO.getSelectedAgency();
			smartMediaPlanObj.setLocalAgencyId(StringUtil.getLongValue(localAdgencyId));
			smartMediaPlanObj.setStartDate(campaignDTO.getStartDate());
			smartMediaPlanObj.setEndDate(campaignDTO.getEndDate());
			smartMediaPlanObj.setActive(1);
			String status=campaignDTO.getStatusId();
			if(status !=null){
				log.info("status from campaign -- "+status);
				if(LinMobileUtil.isNumeric(status)){
					int statusId=Integer.parseInt(status);
					if(statusId <CampaignStatusEnum.values().length){
						status=CampaignStatusEnum.values()[statusId].name();	
					}else{
						status="N/A";
					}
								
				}				
			}else{
				status="";
			}
			smartMediaPlanObj.setCampaignStatus(status);
			
			log.info("Now adding placements...");
			List<SmartCampaignPlacementDTO> placementDTOList=new ArrayList<SmartCampaignPlacementDTO>();
			List<ProductDTO> productDTOList=new ArrayList<ProductDTO>();
			
			
			for(SmartCampaignPlacementObj placement:placementList){
				log.info("placement id:"+placement.getPlacementId()+" , placement.getImpressions():"+placement.getImpressions());
				
				productRate=placement.getRate() ==null?productRate:StringUtil.getDoubleValue(placement.getRate());
				
				String imp=placement.getImpressions();
				if(imp !=null && imp.contains(",")){
					imp=imp.replaceAll(",", "");
				}				
				long placementGoal=StringUtil.getLongValue(imp);
				
				String creatives="";
				
				String geoTargets="";
							
				// add placements 
				List<CreativeObj> creativeList=placement.getCreativeObj();
				
				int count=0;
				if(creativeList !=null){	
					log.info("Creative list--"+creativeList.size());
					for(CreativeObj creative:creativeList){
						String size=creative.getSize();
						if(count==0){
							creatives=creatives+size;
						}else{
							creatives=creatives+", "+size;
						}
						count++;					
					}
				}else{
					log.info("Creative list--null");
				}
				
				//Add targeting data
				List<String> geoTargetList=new ArrayList<String>();
				
				List<StateObj> stateList=placement.getStateObj();
				if(stateList !=null){
					for(StateObj state:stateList){
						String text=state.getText();
						geoTargetList.add(text);											
					}
				}
				
				List<CityDTO> cityList=placement.getCityObj();
				if(cityList !=null){
					for(CityDTO city:cityList){
						String text=city.getText();
						geoTargetList.add(text);											
					}
				}
				
				List<ZipDTO> zipList=placement.getZipObj();
				if(zipList !=null){
					for(ZipDTO zip:zipList){
						String text=zip.getText();
						geoTargetList.add(text);											
					}
				}
				
				List<GeoTargetsObj> dmaTargetList=placement.getGeoObj();
				if(dmaTargetList !=null){
					for(GeoTargetsObj dma:dmaTargetList){
						String text=dma.getGeoTargetsName();
						geoTargetList.add(text);				
					}
				}
				log.info("geoTargetList--"+geoTargetList);
				if(geoTargetList!=null && geoTargetList.size()>0){
					geoTargets=geoTargetList.toString();
				}
				long id=placement.getPlacementId();
				double campaignBudget=StringUtil.getDoubleValue(placement.getBudget());
				totalCampaignBudget=totalCampaignBudget+campaignBudget;
				totalImpressionsForCampaign=totalImpressionsForCampaign+placementGoal;
				
				SmartCampaignPlacementDTO placementDTO=new SmartCampaignPlacementDTO(id,
						placement.getPlacementName(), placementGoal,
						placement.getStartDate(), placement.getEndDate(),
						StringUtil.getDoubleValue(placement.getRate()),
						campaignBudget, creatives, geoTargets, campaignDTO.getNotes(), placement.getItemType(), placement.getDeviceCapability());
				placementDTOList.add(placementDTO);
				log.info("Added placement- "+placementDTO.toString());
				String placementId=placement.getPlacementId()+"";
				
				
				// Add products now
				if(campaignProductsAllocationMap !=null && campaignProductsAllocationMap.size()>0
						&& productForecastMap !=null && productForecastMap.size()>0){
					
					log.info("Going to add products..for placementId:"+placementId);
					long totalproductWiseImpressions=0;
					Map<String,Long> productAllocationMap=campaignProductsAllocationMap.get(placementId);
					log.info("productAllocationMap:"+(productAllocationMap==null?0:productAllocationMap.size()));
					
					List<ProductsObj> productList=campaignProducts.get(placementId);
					log.info("productList:"+(productList==null?0:productList.size()));
					
					//productRate=placement.getRate() ==null?productRate:StringUtil.getDoubleValue(placement.getRate());
					
					if(productList !=null){
						log.info("productList.."+productList.size()+" , get these from allocated /forecast map if found..");
						
						for(ProductsObj product:productList){		
							String productId=product.getProductId()+"";
							ForcastDTO forecastDTO=productForecastMap.get(productId);
							long allocatedImp=productAllocationMap.get(productId);
							log.info("productId : "+productId+" and allocatedImp:"+allocatedImp+" and forecastDTO:"+forecastDTO.toString());
							double allocatedBudget=ReportUtil.calculateBudget(allocatedImp, productRate);
							allocatedBudget=Math.round((allocatedBudget*100.0))/100.0;
							double payout= (double)allocatedBudget*revenueSharePercent/100.0;
							payout=Math.round((payout*100.0))/100.0;
							
							double netRate= (double) (payout/allocatedImp)*1000.0;
							netRate=Math.round((netRate*100.0))/100.0;
							
							totalproductWiseImpressions=totalproductWiseImpressions+allocatedImp;
							ProductDTO productDTO=new ProductDTO(product.getProductId(), product.getProductName(), 
									productRate, forecastDTO.getAvailableImpressions(),forecastDTO.getMatchedImpressions(),  allocatedImp, 
									allocatedBudget, revenueSharePercent, payout, netRate, placementId,
									product.getPublisherName(),product.getPublisherId(), product.getPartnerId());							
							String partnerLogo=null;					
							if(product.getPartnerId() !=null){
								log.info("adding partner logo to product...");
								partnerLogo=partnerLogoMap.get(product.getPartnerId());
								if(partnerLogo==null){
									partnerLogo=userService.getCompanyLogo(product.getPartnerId());
								}
								partnerLogoMap.put(product.getPartnerId(), partnerLogo);								
							}
							productDTO.setPartnerLogo(partnerLogo);
							log.info("adding product..."+productDTO.toString());
							productDTOList.add(productDTO);
							
							totalPayout=totalPayout+payout;
						}
					}else{
						log.info("No products available.... ");
					}
					
					
					//Overflow product
					ProductDTO productDTO=null;
					if(totalproductWiseImpressions < placementGoal){
						long overflowImp=placementGoal-totalproductWiseImpressions;
						log.info("Prodcut with Overflow impression for placement "+placementId +" are left--"+overflowImp);
						double allocatedBudget=ReportUtil.calculateBudget(overflowImp, productRate);
						allocatedBudget=Math.round((allocatedBudget*100.0))/100.0;
						double payout= (double)allocatedBudget*revenueSharePercent/100.0;
						payout=Math.round((payout*100.0))/100.0;
						double netRate= (double) (payout/overflowImp)*1000.0;
						netRate=Math.round((netRate*100.0))/100.0;
						
						productDTO=new ProductDTO(0, overflowProductName, 
								productRate, 0, overflowImp, 
								allocatedBudget, revenueSharePercent, payout, netRate, placementId, overflowPartnerName,"0", "0");
					}else{
						log.info("adding a product with blank overflow...");
						productDTO=new ProductDTO(0, overflowProductName, productRate, 0, 0, 
								0, revenueSharePercent, 0, 0, placementId,overflowPartnerName,"0", "0");
					}				
					productDTOList.add(productDTO);		
					totalPayout=totalPayout+productDTO.getPayout();
				}else{
					log.info("Searched products don't have allocations.");
					long overflowImp=placementGoal;
					log.info("Prodcut with Overflow impression for placement "+placementId +" are left--"+overflowImp);
					double allocatedBudget=ReportUtil.calculateBudget(overflowImp, productRate);
					allocatedBudget=Math.round((allocatedBudget*100.0))/100.0;
					
					double payout= (double)allocatedBudget*revenueSharePercent/100.0;
					payout=Math.round((payout*100.0))/100.0;
					double netRate= (double) (payout/overflowImp)*1000.0;
					netRate=Math.round((netRate*100.0))/100.0;
					
					ProductDTO productDTO=new ProductDTO(0, overflowProductName, 
							productRate, 0, overflowImp, 
							allocatedBudget, revenueSharePercent, payout, netRate, placementId, overflowPartnerName,"0", "0");
					productDTOList.add(productDTO);
					totalPayout=totalPayout+productDTO.getPayout();
				}
				
				
			}
			smartMediaPlanObj.setPlacements(placementDTOList);
			smartMediaPlanObj.setProducts(productDTOList);
			smartMediaPlanObj.setBudget(totalCampaignBudget);
			double eCPM= (double) ((totalCampaignBudget*1000.0)/totalImpressionsForCampaign);
			eCPM=Math.round((eCPM*100.0))/100.0;
			smartMediaPlanObj.seteCPM(eCPM);
			
			double totalServingFee= (double) (totalImpressionsForCampaign *servingFeeAmount) /1000.0 ;
			totalServingFee=Math.round((totalServingFee*100.0))/100.0;
			smartMediaPlanObj.setServingFee(totalServingFee+"");   
			
			smartMediaPlanObj.setCost(totalPayout);
			double netCost=totalPayout+totalServingFee;			
		    double netRevenue= totalCampaignBudget-netCost;
		    smartMediaPlanObj.setNetRevenue(netRevenue+"");
		    
		    log.info("Save this smartMediaPlanObject -- "+smartMediaPlanObj.toString());
		    saveSmartMediaPlan(smartMediaPlanObj);
		    log.info("MediaPlan saved..."+smartMediaPlanObj.getId());
		}else{
			log.warning("You can not create media plan without any placement");
		}
		
		return smartMediaPlanObj;
		
	}
	
	public JSONObject createSmartMediaPlanJSON(SmartMediaPlanObj smartMediaPlanObj){
		log.info("Creating smartMediaPlanJson.....");
		JSONObject smartMediaPlanJson=new JSONObject();		
		long totalImpression=0;
				
		JSONArray jsonArray=new JSONArray();
		List<SmartCampaignPlacementDTO> placementList=smartMediaPlanObj.getPlacements();
		for(SmartCampaignPlacementDTO placement:placementList){
			JSONObject placementJson=new JSONObject();
			placementJson.put("id", placement.getId());
			placementJson.put("name", placement.getPlacementName());
			placementJson.put("impression", placement.getImpressions());
			placementJson.put("rate", placement.getRate());
			placementJson.put("creatives", placement.getCreatives());
			placementJson.put("budget", placement.getBudget());
			placementJson.put("dmas", placement.getDmas());
			if(placement.getItemType()!=null && placement.getItemType().length()>0){
				placementJson.put("itemType", placement.getItemType());
			}else{
				placementJson.put("itemType", "1");
			}
			if(placement.getDeviceCapability()!=null){
				placementJson.put("deviceCapability", placement.getDeviceCapability());
			}else{
				placementJson.put("deviceCapability", 0);
			}
			placementJson.put("notes", placement.getNotes());
			placementJson.put("start_date", placement.getStartDate());
			placementJson.put("end_date", placement.getEndDate());			
			
			totalImpression=totalImpression+placement.getImpressions();
			
			List<ProductDTO> productList=smartMediaPlanObj.getProducts();
			JSONArray productJsonArray=new JSONArray();		
			for(ProductDTO product:productList){
				long pid=Long.parseLong(product.getPlacementId());
				if(pid ==placement.getId()){
					JSONObject productJSON=new JSONObject();
					productJSON.put("id", product.getId());
					productJSON.put("partner", product.getPartnerName());
					productJSON.put("name", product.getName());
					productJSON.put("availableImp", product.getAvailableImpressions());
					productJSON.put("matchedImp", product.getMatchedImpressions());
					productJSON.put("allocatedImp", product.getAllocatedImpressions());
					productJSON.put("rate", product.getOfferedRate());
					productJSON.put("budget", product.getAllocatedBudget());
					productJSON.put("revenueSharingPercent", product.getRevenueSharePercent());
					productJSON.put("payout",product.getPayout());
					productJSON.put("cpm", product.getNetRate());
					productJSON.put("placementId", product.getPlacementId());
					productJSON.put("partner_adserver_id", product.getPartnerAdserverId());
					productJSON.put("partner_id", product.getPartnerId());
					productJSON.put("partner_logo", product.getPartnerLogo());
					productJsonArray.add(productJSON);
				}
			}		
			placementJson.put("products", productJsonArray);
			
			jsonArray.add(placementJson);
		}		
		smartMediaPlanJson.put("placements", jsonArray);
		
		
		
		JSONObject campaignHeader=new JSONObject();
		campaignHeader.put("id", smartMediaPlanObj.getId());
		campaignHeader.put("campaign_id", smartMediaPlanObj.getCampaignId());
		campaignHeader.put("name", smartMediaPlanObj.getCampaignName());
		campaignHeader.put("status", smartMediaPlanObj.getCampaignStatus());
		campaignHeader.put("budget", smartMediaPlanObj.getBudget());
		campaignHeader.put("impression", smartMediaPlanObj.getImpression());
		//campaignHeader.put("impression", smartMediaPlanObj.getImpression());
		campaignHeader.put("impression", totalImpression);
		campaignHeader.put("eCPM", smartMediaPlanObj.geteCPM());
		campaignHeader.put("cost", smartMediaPlanObj.getCost());
		campaignHeader.put("servingFee", smartMediaPlanObj.getServingFee());
		campaignHeader.put("netRevenue", smartMediaPlanObj.getNetRevenue());
		campaignHeader.put("advertiser_id", smartMediaPlanObj.getLocalAdvertiserId());
		campaignHeader.put("agency_id", smartMediaPlanObj.getLocalAgencyId());
		campaignHeader.put("start_date", smartMediaPlanObj.getStartDate());
		campaignHeader.put("end_date", smartMediaPlanObj.getEndDate());
		campaignHeader.put("dfp_order_id", smartMediaPlanObj.getDfpOrderId());
		campaignHeader.put("dfp_order_name", smartMediaPlanObj.getDfpOrderName());
		
		smartMediaPlanJson.put("header", campaignHeader);
		
		// Add all partners also
		log.info("add all partners also...");
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<CompanyObj> companyList=userService.loadAllPartners();
		JSONObject partnerJson=userService.loadPartnersJSON(companyList);	
		if(partnerJson !=null && partnerJson.containsKey("partners")){
			smartMediaPlanJson.put("partners", partnerJson.get("partners"));
		}
		
		log.info("smartMediaPlanJson:"+smartMediaPlanJson.toString());
		return smartMediaPlanJson;		
	}
	
	/*
	 * This method will smart media plan entity
	 * @see com.lin.web.service.IProductService#saveSmartMediaPlanFromJSON(net.sf.json.JSONObject)
	 */
	public SmartMediaPlanObj saveSmartMediaPlanFromJSON(JSONObject smartMediaPlanJson){
		log.info("saving smartMediaPlan.....");
		SmartMediaPlanObj smartMediaPlanObj=null;
		if(smartMediaPlanJson !=null){
			smartMediaPlanObj=new SmartMediaPlanObj();
			
			if(smartMediaPlanJson.containsKey("header")){
				log.info("Step 1-- get value form header...");
				JSONObject campaignHeader=(JSONObject) smartMediaPlanJson.get("header");
				
				String id=null;
				if(campaignHeader.has("id")){
					 id=campaignHeader.getString("id");					 
				}
				String campaignId=campaignHeader.getString("campaign_id");
				String campaignName=campaignHeader.getString("name");
				String campaignStatus=CampaignStatusEnum.Draft.name();
				if(campaignHeader.has("status")){
					campaignStatus=campaignHeader.getString("status");
				}
				String budget=campaignHeader.getString("budget");
				String impression=campaignHeader.getString("impression");
				String eCPM=campaignHeader.getString("eCPM");
				String cost=campaignHeader.getString("cost");
				String servingFee=campaignHeader.getString("servingFee");
				String netRevenue=campaignHeader.getString("netRevenue");
				String localAdvertiserId=campaignHeader.getString("advertiser_id");
				String localAgencyId=campaignHeader.getString("agency_id");
				String startDate=null;
				if(campaignHeader.has("start_date")){
					startDate=campaignHeader.getString("start_date");
				}
				String endDate=null;
				if(campaignHeader.has("end_string")){
					endDate=campaignHeader.getString("end_date");
				}
				
				
				budget=budget.replaceAll("$", "");
				budget=budget.replaceAll(",", "");
				impression=impression.replaceAll(",", "");
				eCPM=eCPM.replaceAll("$", "");
				cost=cost.replaceAll("$", "");
				cost=cost.replaceAll(",", "");
				servingFee=servingFee.replaceAll("$", "");
				servingFee=servingFee.replaceAll(",", "");
				netRevenue=netRevenue.replaceAll("$", "");
				netRevenue=netRevenue.replaceAll(",", "");
				
				if(id !=null ){
					long autoId=StringUtil.getLongValue(id);
					if(autoId >0){
						log.info("This mediaplan already exist. Load it from datastore to update - autoId:"+autoId);
						smartMediaPlanObj=loadSmartMediaPlan(autoId);
						//smartMediaPlanObj.setId(autoId);
					}
				}
				smartMediaPlanObj.setBudget(StringUtil.getDoubleValue(budget));
				smartMediaPlanObj.setImpression(StringUtil.getLongValue(impression));
				smartMediaPlanObj.seteCPM(StringUtil.getDoubleValue(eCPM));
				smartMediaPlanObj.setCost(StringUtil.getDoubleValue(cost));
				smartMediaPlanObj.setServingFee(servingFee);
				smartMediaPlanObj.setNetRevenue(netRevenue);
				smartMediaPlanObj.setCampaignId(campaignId);
				smartMediaPlanObj.setCampaignName(campaignName);
				smartMediaPlanObj.setLocalAdvertiserId(StringUtil.getLongValue(localAdvertiserId));
				smartMediaPlanObj.setLocalAgencyId(StringUtil.getLongValue(localAgencyId));
				smartMediaPlanObj.setStartDate(startDate);
				smartMediaPlanObj.setEndDate(endDate);
				smartMediaPlanObj.setActive(1);
				smartMediaPlanObj.setCampaignStatus(campaignStatus);
			}else{
				log.info("No header...................");
			}
			
			JSONArray jsonArray=null;
			List<SmartCampaignPlacementDTO> placementList=null;
			List<ProductDTO> productList=new ArrayList<ProductDTO>();
			
			if(smartMediaPlanJson.containsKey("placements")){
				log.info("Step -2 : Get all placements from Json");
				placementList=new ArrayList<SmartCampaignPlacementDTO>();
				
				jsonArray=(JSONArray) smartMediaPlanJson.get("placements");
				for(int i=0;i<jsonArray.size();i++){
					JSONObject placementJson=jsonArray.getJSONObject(i);
					
					String id=placementJson.getString("id");
					String name=placementJson.getString("name");
					String impression=placementJson.getString("impression");
					String rate=placementJson.getString("rate");
					String creatives=placementJson.getString("creatives");
					String budget=placementJson.getString("budget");
					String dmas=placementJson.getString("dmas");
					String notes=placementJson.getString("notes");
					String startDate=placementJson.getString("start_date");
					String endDate=placementJson.getString("end_date");
					String itemType =placementJson.getString("itemType");
					int  deviceCapability= ReportUtil.TARGETING_ANY;
					try{
					deviceCapability = Integer.parseInt(placementJson.getString("deviceCapability"));
					}catch(Exception e){}
					
					impression=impression.replaceAll(",", "");
					rate=rate.replaceAll("$", "");
					budget=budget.replaceAll("$", "");
					budget=budget.replaceAll(",", "");
					
					SmartCampaignPlacementDTO placement=new SmartCampaignPlacementDTO(StringUtil.getLongValue(id), 
							name, StringUtil.getLongValue(impression), startDate,
							endDate, StringUtil.getDoubleValue(rate), StringUtil.getDoubleValue(budget),
							creatives, dmas, notes, itemType, deviceCapability);
					placementList.add(placement);

					if(placementJson.containsKey("products")){
						log.info("Step 3 : Get all products from JSON..");
						JSONArray productsJsonArray=(JSONArray) placementJson.get("products");
						for(int j=0;j<productsJsonArray.size();j++){
							JSONObject productJSON=productsJsonArray.getJSONObject(j);
							String pId=productJSON.getString("id");
							String partner=productJSON.getString("partner");
							String productName=productJSON.getString("name");
							String availableImp=productJSON.getString("availableImp");
							String allocatedImp=productJSON.getString("allocatedImp");
							String productRate=productJSON.getString("rate");
							String productBudget=productJSON.getString("budget");
							String revenueSharingPercent=productJSON.getString("revenueSharingPercent");
							String payout=productJSON.getString("payout");
							String cpm=productJSON.getString("cpm");
							String placementId=productJSON.getString("placementId");
							String partnerAdserverId=productJSON.getString("partner_adserver_id");
							String partnerId=productJSON.getString("partner_id");
							String partnerLogo=null;
							if(productJSON.has("partner_logo")){
								partnerLogo=productJSON.getString("partner_logo");
							}
							
							
							productRate=productRate.replaceAll("$", "");
							availableImp=availableImp.replaceAll(",", "");
							allocatedImp=allocatedImp.replaceAll(",", "");
							productBudget=productBudget.replaceAll("$", "");
							productBudget=productBudget.replaceAll(",", "");
							payout=payout.replaceAll("$", "");
							payout=payout.replaceAll(",", "");
							cpm=cpm.replaceAll("$", "");
							
							ProductDTO product=new ProductDTO(StringUtil.getLongValue(pId), productName,
									StringUtil.getDoubleValue(productRate), 
									StringUtil.getLongValue(availableImp), StringUtil.getLongValue(allocatedImp),
									StringUtil.getDoubleValue(productBudget),StringUtil.getDoubleValue(revenueSharingPercent), 
									StringUtil.getDoubleValue(payout), StringUtil.getDoubleValue(cpm), placementId,
									partner, partnerAdserverId,partnerId);
							product.setPartnerLogo(partnerLogo);
							if(StringUtil.getLongValue(allocatedImp) > 0) {
								productList.add(product);
								log.info("product added.....partnerId:"+partnerId+" & pId:"+pId);
							}else {
								log.info("product with ZERO allocatedImpressions cannot be added.....partnerId:"+partnerId+" & pId:"+pId+", allocatedImp : "+allocatedImp);
							}
						}
					}


				}
				smartMediaPlanObj.setPlacements(placementList);
				smartMediaPlanObj.setProducts(productList);
			}else{
				log.info("No placemnets..................");
			}
			
			//Now save this smart media plan in datastore
			IProductDAO productDAO=new ProductDAO();
			try {
				log.info("Save this smart media plan in datastore..");
				String updatedOn=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
				smartMediaPlanObj.setUpdateOn(updatedOn);
				productDAO.saveObjectWithStrongConsistancy(smartMediaPlanObj);
				log.info("Saved successfully in datastore..id--"+smartMediaPlanObj.getId());				
			} catch (DataServiceException e) {
				log.severe("failed to save smart mediaplan in datastore --"+e.getMessage());
			}
		}		
			
		return smartMediaPlanObj;
	}
	
	public SmartMediaPlanObj loadSmartMediaPlan(String campaignId){
		SmartMediaPlanObj smartMediaPlan=null;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			log.info("load smart media plan for campaignId-"+campaignId);
			smartMediaPlan=mediaPlanDAO.loadMediaPlan(campaignId);			
		} catch (DataServiceException e) {
			log.severe("Failed to load media plan.."+e.getMessage());
		}
		return smartMediaPlan;
	}
	
	public int checkMediaPlanStatus(String campaignId){
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		boolean needUpdate=false;
		boolean isActive=false;
		try {
			log.info("load smart media plan for campaignId-"+campaignId);
			
			List<SmartMediaPlanObj> resultList=mediaPlanDAO.loadAllMediaPlans(campaignId);		
			if(resultList !=null){
				for(SmartMediaPlanObj obj:resultList){
					int activeStatus=obj.getActive();
					if(activeStatus==2){
						needUpdate=true;
					}	
					if(activeStatus==1){
						isActive=true;
					}					
				}
			}else{
				log.warning("No media plan found for this campaign.");
			}
			if(isActive){
				
			}
		} catch (DataServiceException e) {
			log.severe("Failed to load media plan.."+e.getMessage());
		}
		
		int status=0;
		log.info("needUpdate : "+needUpdate+" , isActive : "+isActive);
		if(isActive){
			status=1; //Active
		}else if(needUpdate){
			status=2; //InActive - Need Update
		}else{
			status=0; //Inactive
		}
		return status;
	}
	
	public SmartMediaPlanObj loadSmartMediaPlan(String campaignId,int status){
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		SmartMediaPlanObj smartMediaPlan=null;
		try {
			log.info("load smart media plan for campaignId-"+campaignId+", status:"+status);			
		    smartMediaPlan=mediaPlanDAO.loadMediaPlan(campaignId,status);	
		} catch (DataServiceException e) {
			log.severe("Failed to load media plan.."+e.getMessage());
		}
		
		return smartMediaPlan;
	}
	
	public void saveSmartMediaPlan(SmartMediaPlanObj smartMediaPlan){
		if(smartMediaPlan !=null){
			IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
			try {
				log.info("save smart media plan..");
				String updatedOn=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
				smartMediaPlan.setUpdateOn(updatedOn);
				mediaPlanDAO.saveObject(smartMediaPlan);	
				log.info("Saved successfully in datastore..id--"+smartMediaPlan.getId());
			} catch (DataServiceException e) {
				log.severe("Failed to save media plan.."+e.getMessage());
			}
		}	
		
	}
	
	public SmartMediaPlanObj loadSmartMediaPlan(long mediaPlanId){
		SmartMediaPlanObj smartMediaPlan=null;
		IMediaPlanDAO mediaPlanDAO=new MediaPlanDAO();
		try {
			log.info("load smart media plan for mediaPlanId-"+mediaPlanId);
			smartMediaPlan=mediaPlanDAO.getSmartMediaPlanObj(mediaPlanId);	
		} catch (DataServiceException e) {
			log.severe("Failed to load media plan.."+e.getMessage());
		}
		return smartMediaPlan;
	}
	
	public static boolean AddProductInMemcache(ProductsObj productsObj){
		List<ProductsObj> objList = new ArrayList<>();
		IProductDAO productDAO = new ProductDAO();
		boolean result = false;
		try {
			if(productsObj != null) {
				/*String publisherId = productsObj.getPublisherId();
				objList = MemcacheUtil.getProductObj(publisherId);
				if(objList == null || objList.size() == 0) {
					objList = productDAO.getProductByPublisherId(publisherId);	// get from dataStore
				}*/
				String partnerId = productsObj.getPartnerId();
				objList = MemcacheUtil.getProductObj(partnerId);
				if(objList == null || objList.size() == 0) {
					objList = productDAO.getProductByPartnerId(partnerId);	// get from dataStore
				}
				////
				if(objList == null || objList.size() == 0) {	// if Datastore is empty
					objList = new ArrayList<>();
					objList.add(productsObj);
					result = true;
				}
				else if(objList.contains(productsObj)) {		// update existing object
					if(objList != null && objList.size() > 0) {
						int count = 0;
						boolean found = false;
						for (ProductsObj obj : objList) {
							if(obj.getProductId() == productsObj.getProductId()) {
								found = true;
								break;
							}
							count++;
						}
						if(found) {
							objList.add(count, productsObj);
							objList.remove(count + 1);
							result = true;
						}
					}
				}
				else {											// add new object
					objList.add(productsObj);
					result = true;
				}
				if(result) {
					//Collections.sort(objList);
					MemcacheUtil.setProductObj(partnerId, objList);
					log.info("Memcache updated successfully, size : "+objList.size());
				}
			}
		}catch (Exception e) {	
		    log.severe("Exception in updateProductObjMemcache of MemcacheUtil: "+e.getMessage());
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	private ProductsObj getProductFromMemcache(long productId, String partnerId) throws DataServiceException {
		List<ProductsObj> productList = MemcacheUtil.getProductObj(partnerId);
		if(productList != null && productList.size() > 0) {
			for(ProductsObj productsObj : productList) {
				if(productsObj != null && productsObj.getProductId() == productId) {
					return productsObj;
				}
			}
		}
		return null;
	}
	
	private ProductsObj deleteProductInMemcache(long productId,String partnerId) throws DataServiceException {
		List<ProductsObj> productList = MemcacheUtil.getProductObj(partnerId);
		for(int i=0; i<productList.size(); i++) {
			ProductsObj productsObj = productList.get(i);
			if(productsObj != null && productsObj.getProductId() == productId) {
				productList.remove(i);	// if found then remove
				MemcacheUtil.setProductObj(partnerId, productList);
				return productsObj;
			}
		}
		return null;
	}

	@Override
	public JSONArray loadTopLevelAdUnits(String adServerId, String adServerUsername, long userId) throws Exception {
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO dao = new  UserDetailsDAO();
		JSONArray jsonArray= new JSONArray();
		String password = "";
		DfpSession dfpSession = null;
	    DfpServices dfpServices = null;
	    try {
			if(adServerId != null && adServerId.trim().length() > 0 && adServerUsername != null && adServerUsername.trim().length() > 0) {
				adServerId = adServerId.trim();
				List<LineItemDTO> siteList = MemcacheUtil.getDFPPropertiesFromCache(adServerId + adServerUsername);
				if(siteList != null && siteList.size() > 0) {
					log.info("Memcache exists, size : "+siteList.size());
				}
				else {
					log.info("Memcache does not exists........... searching from DFP");
					Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
					if(map != null && map.size() > 0 && map.containsKey(adServerId)) {
						AdServerCredentialsDTO adServerCredentialsDTO = map.get(adServerId);
						password = adServerCredentialsDTO.getAdServerPassword();
						adServerUsername = adServerCredentialsDTO.getAdServerUsername();
									
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();
						
						DFPReportService dfpReportService = new DFPReportService();
						siteList = new ArrayList<>();
						dfpReportService.getTopLevelAdUnitsFromDFP(dfpServices, dfpSession, adServerId, adServerUsername, "", siteList, new StringBuilder());
					}
					else {
						log.severe("No credentials for networkCode : "+adServerId);
					}
				}
				if(siteList != null) {
					log.info("list size : "+siteList.size());
					for (LineItemDTO lineItemDTO : siteList) {
						String siteName=lineItemDTO.getSiteName();
						String pName = lineItemDTO.getParentName();
						if(siteName != null && !siteName.equals("null") &&  LinMobileUtil.isNumeric(lineItemDTO.getSiteId()) 
								&& LinMobileUtil.isNumeric(lineItemDTO.getParentId()) && pName != null && !pName.equals("null")){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", Long.valueOf(lineItemDTO.getSiteId()));
							jsonObject.put("name", siteName);
							jsonObject.put("pid", Long.valueOf(lineItemDTO.getParentId()));
							jsonObject.put("isHasChildren", lineItemDTO.isHasChildren());
							jsonObject.put("fullpath", pName);
							jsonArray.add(jsonObject);
						}
					}
				}
				else {
					log.info("list : null");
				}
			}
	    }catch (Exception e) {
			log.severe("Exception in loadTopLevelAdUnits of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonArray;
	}

	@Override
	public JSONArray loadChildAdUnits(String adServerId, String adServerUsername, String parentId, String parentFullPath, long userId) throws Exception {
		JSONArray jsonArray= new JSONArray();
		String password = "";
		DfpSession dfpSession = null;
	    DfpServices dfpServices = null;
	    try {
			if(adServerId != null && adServerId.trim().length() > 0 && adServerUsername != null && adServerUsername.trim().length() > 0) {
				List<LineItemDTO> siteList = MemcacheUtil.getAdUnitsChildsFromDFP(parentId, parentFullPath, adServerId, adServerUsername);
				if(siteList != null && siteList.size() > 0) {
					log.info("Memcache exists, size : "+siteList.size());
				}
				else {
					log.info("Memcache does not exists........... searching from DFP");
					Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
					if(map != null && map.size() > 0 && map.containsKey(adServerId)) {
						AdServerCredentialsDTO adServerCredentialsDTO = map.get(adServerId);
						password = adServerCredentialsDTO.getAdServerPassword();
						adServerUsername = adServerCredentialsDTO.getAdServerUsername();
									
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(adServerId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();
						
						IDFPReportService dfpReportService = new DFPReportService();
						siteList = dfpReportService.getAdUnitChilds(dfpServices, dfpSession, parentId, parentFullPath);
					}
					else {
						log.severe("No credentials for networkCode : "+adServerId);
					}
				}
				if(siteList != null) {
					log.info("list size : "+siteList.size());
					MemcacheUtil.setAdUnitsChildsFromDFP(siteList, parentId, parentFullPath, adServerId, adServerUsername);
					for (LineItemDTO lineItemDTO : siteList) {
						String siteName=lineItemDTO.getSiteName();
						String pName = lineItemDTO.getParentName();
						if(siteName != null && !siteName.equals("null") &&  LinMobileUtil.isNumeric(lineItemDTO.getSiteId()) 
								&& LinMobileUtil.isNumeric(lineItemDTO.getParentId()) && pName != null && !pName.equals("null")){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", Long.valueOf(lineItemDTO.getSiteId()));
							jsonObject.put("name", siteName);
							jsonObject.put("pid", Long.valueOf(lineItemDTO.getParentId()));
							jsonObject.put("isHasChildren", lineItemDTO.isHasChildren());
							jsonObject.put("fullpath", pName);
							jsonArray.add(jsonObject);
						}
					}
				}
				else {
					log.info("list : null");
				}
			}
	    }catch (Exception e) {
			log.severe("Exception in loadChildAdUnits of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonArray;
	}

	@Override
	public String loadAllActiveAdUnits(String networkId, String networkUsername) throws Exception {
		log.info("loadAllActiveAdUnits service started... adServerId : "+networkId+", adServerUsername : "+networkUsername);
		IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		IProductDAO productDAO = new ProductDAO();
		StringBuilder summary = new StringBuilder();
		/*List<LineItemDTO> siteList = new ArrayList<>();
		String message = "";
		try {
			DfpSession dfpSession = null;
		    DfpServices dfpServices = null;
		    String clientLoginToken = null;
			String adServerId = "";
			String username = "";
			String password = "";
			boolean parameterisedCall = false;
			Map<String, AdServerCredentialsDTO> map = service.getCompanyDFPCredentials(MemcacheUtil.getAllCompanyList());
			for(String key : map.keySet()) {
				if(networkId != null && networkUsername != null && key.equals(networkId.trim() + networkUsername.trim())) {
					parameterisedCall = true;
				}
				else if(networkId != null && networkUsername != null && !(key.equals(networkId.trim() + networkUsername.trim()))) {
					continue;
				}

				AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
				if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null && adServerCredentialsDTO.getAdServerPassword() != null) {
					adServerId = adServerCredentialsDTO.getAdServerId();
					username = adServerCredentialsDTO.getAdServerUsername();
					password = adServerCredentialsDTO.getAdServerPassword();
					summary.append("For ADSERVER_ID : "+adServerId+ ", AD_SERVER_USERNAME : "+username+"<br>");

					clientLoginToken = UserService.regenerateAccountAuthToken(username, password);
					log.info("clientLoginToken : "+clientLoginToken+", for Adserver userName : "+username);

					log.info(" now going to build dfpSession ...");
					dfpSession = new DfpSession.Builder()
					  				.withClientLoginToken(clientLoginToken)					
					  				.withNetworkCode(adServerId)
					  				.withApplicationName(LinMobileConstants.DFP_APPLICATION_NAME)
					  				.build();
					log.info(" getting DfpServices instance from properties...");
					dfpServices = LinMobileProperties.getInstance().getDfpServices();

					IDFPReportService dfpReportService = new DFPReportService();
					boolean result = dfpReportService.getTopLevelAdUnitsFromDFP(dfpServices, dfpSession, adServerId, username, "", siteList, summary);
					if(result) {
						message = "DFP TopLevel AdUnits fetched successfully";
						log.info(message);
						summary.append(message+"<br>");
						if(siteList != null && siteList.size() > 0) {
							message = "TopLevel AdUnits count : "+siteList.size();
							log.info(message);
							summary.append(message+"<br>");
							List<AdUnitHierarchy> adUnitHierarchyList = new ArrayList<>();
							Map<String, String> parentIdMap = new LinkedHashMap<>();
							for(LineItemDTO dto : siteList) {
								AdUnitHierarchy adUnitHierarchy = new AdUnitHierarchy(adServerId+"_"+dto.getSiteId(), adServerId, dto.getSiteId(), dto.getSiteName(), dto.getParentId());
								adUnitHierarchyList.add(adUnitHierarchy);
								parentIdMap.put(dto.getSiteId(), dto.getSiteId());
							}
							List<AdUnitHierarchy> adUnitChildList = dfpReportService.loadAllChildsInHierarchy(dfpServices, dfpSession, adServerId, parentIdMap, summary);
							if(adUnitChildList != null) {
								adUnitHierarchyList.addAll(adUnitChildList);
								List<AdUnitHierarchy> resultList = productDAO.getAllAdUnitHierarchyByServerId(adServerId);
								if(resultList != null) {
									productDAO.deleteAdUnitHierarchyListObject(resultList);
									message = "Old AdUnits deleted, count : "+resultList.size();
									log.info(message);
									summary.append(message+"<br>");
								}
								productDAO.saveAdUnitHierarchyListObject(adUnitHierarchyList);
								message = "New AdUnits added, count : "+adUnitHierarchyList.size();
								log.info(message);
								summary.append(message+"<br>");
							}
						}
					}
					else {
							String errorMessage = "DFP TopLevel AdUnits fetch failed";
							log.severe(errorMessage);
							summary.append(errorMessage+"<br>");
					}
				}
				summary.append("<br><br><br>");

				if(parameterisedCall) {
					break;
				}
			}
			// send mail
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "DFP AdUnits fetch Status - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in DFP AdUnits caching : "+e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage+"<br>");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP AdUnits fetch - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}*/
		return summary.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<StateObj> loadAllStates(String countryCode){
		log.info("Load all states for countryCode:"+countryCode);
		IProductDAO productDAO=new ProductDAO();
		CountryObj country;
		
		String memcacheKey="states_"+countryCode;
		List<StateObj> stateList=(List<StateObj>) MemcacheUtil.getObjectsListFromCache(memcacheKey);;
		if(stateList ==null || stateList.size()==0){
			try {
				country = productDAO.getCountryObj(countryCode);
				if(country !=null){
					stateList=productDAO.getStatesForCountry(country.getId());	
					if(stateList !=null && stateList.size()>0){
						log.info("Set states in memcache with key :"+memcacheKey);
						MemcacheUtil.setObjectsListInCache(stateList, memcacheKey);
					}
				}else{
					log.info("Country not found in datastore with country Code:"+countryCode);
				}
			} catch (DataServiceException e) {
				log.severe(" loadAllStates() :DataServiceException :"+e.getMessage());
			}
		}else{
			log.info("Found states in memcache :"+stateList.size());
		}
		
		
		return stateList;
	}
	
	public JSONObject loadStatesJSON(List<StateObj> stateObjList){
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","name"}, true));
		if(stateObjList !=null && stateObjList.size()>0){			 
			  for(StateObj state:stateObjList){
				  JSONObject json=new JSONObject();					  
				  json.put("id", state.getId());
				  json.put("name", state.getText());
				  json.put("text", state.getText());
				  jsonArray.add(json);
			  }
		}else{
			log.info("No states found in datastore for this country code.");
			//jsonObject.put("states", "No states found in datastore for this country code.");
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","text","name"}, false));
		jsonObject.put("states", jsonArray);
		return jsonObject;
	}	
	
	@SuppressWarnings("unchecked")
	public List<CityObj> loadAllCitiesZips(String stateCode){
		log.info("Load all cities with zip for stateCode:"+stateCode);
		IProductDAO productDAO=new ProductDAO();
		StateObj state;
		String memcacheKey="usa_cities_by_zip_"+stateCode;
		
		List<CityObj> cityWithZipList=(List<CityObj>) MemcacheUtil.getObjectsListFromCache(memcacheKey);
		if(cityWithZipList ==null || cityWithZipList.size()==0){
			try {
				state = productDAO.getSateObj(stateCode);
				if(state !=null){
					cityWithZipList=productDAO.getCityForStates(state.getId());
					if(cityWithZipList !=null && cityWithZipList.size()>0){
						log.info("Set cities in memcache with key :"+memcacheKey);
						MemcacheUtil.setObjectsListInCache(cityWithZipList, memcacheKey);
					}
				}else{
					log.info("State not found in datastore with state Code:"+stateCode);
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
			}
		}else{
			log.info("Found cities in memcache :"+cityWithZipList.size());
		}
		
		return cityWithZipList;
	}
	
	public JSONObject loadCityZipJSON(List<CityObj> cityObjList){
		JSONObject jsonObject=new JSONObject();
		if(cityObjList !=null && cityObjList.size()>0){			 
			  JSONArray jsonArray=new JSONArray();
			  for(CityObj city:cityObjList){
				  JSONObject json=new JSONObject();					  
				  json.put("id", city.getId());
				  json.put("name", city.getText());
				  json.put("zip", city.getZip());
				  jsonArray.add(json);
			  }
			  jsonObject.put("Cities", jsonArray);
		}else{
				log.info("Invalid state code, not found in datastore");
			  //jsonObject.put("error", "Invalid state code, not found in datastore");
		}
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public List<CityDTO> searchCity(String searchText){	
		
		String memcacheKey="search_city_"+searchText;		
		List<CityDTO> searchCityList=(List<CityDTO>) MemcacheUtil.getObjectsListFromCache(memcacheKey);
		
		if(searchCityList ==null || searchCityList.size()==0){
			StringBuffer queryStr=DBUtil.createSearchQuery(searchText);
			log.info("search query:"+queryStr.toString());
			searchCityList=TextSearchDocument.searchCity(queryStr.toString(), 0, 100);
			if(searchCityList !=null && searchCityList.size()>0) {
				
				MemcacheUtil.setObjectsListInCache(searchCityList, memcacheKey);
			}
			
		}else{
			log.info("Search data found in memcache :"+searchCityList.size());
		}
		return searchCityList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ZipDTO> searchZip(String searchText){	
		
		String memcacheKey="search_zip"+searchText;		
		List<ZipDTO> searchZipList=(List<ZipDTO>) MemcacheUtil.getObjectsListFromCache(memcacheKey);
		
		if(searchZipList ==null || searchZipList.size()==0){
			StringBuffer queryStr=DBUtil.createSearchQuery(searchText);
			log.info("search query:"+queryStr.toString());
			searchZipList=TextSearchDocument.searchZip(queryStr.toString(), 0, 50);
			if(searchZipList !=null && searchZipList.size()>0) {
				MemcacheUtil.setObjectsListInCache(searchZipList, memcacheKey);
			}
			
		}else{
			log.info("Search data found in memcache :"+searchZipList.size());
		}
		return searchZipList;
	}
	
	public JSONObject searchCityJSON(List<CityDTO> searchedCityList){
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","stateId"}, true));
		if(searchedCityList !=null && searchedCityList.size()>0){			 
			  for(CityDTO city:searchedCityList){
				  JSONObject json=new JSONObject();					  
				  json.put("id", city.getId());
				  json.put("name", city.getText());
				  //json.put("zip", city.getZip());
				  json.put("stateId", city.getStateId());
				  jsonArray.add(json);
			  }
		}else{
			  log.info("no results found.");
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","stateId"}, false));
		jsonObject.put("cities", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}
	
	public JSONObject searchZipJSON(List<ZipDTO> searchedZipList){
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, true));
		if(searchedZipList !=null && searchedZipList.size()>0){			 
			  for(ZipDTO zip:searchedZipList){
				  JSONObject json=new JSONObject();					  
				  json.put("id", zip.getText());
				  json.put("name", zip.getText()+" - "+zip.getCityName());
				  json.put("cityId", zip.getCityId());
				  json.put("cityName", zip.getCityName());
				  json.put("stateId", zip.getStateId());
				  jsonArray.add(json);
			  }
		}else{
			  log.info("no results found.");
		}
		jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","cityId","cityName","stateId"}, false));
		jsonObject.put("zips", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public List<CreativeObj> loadAllCreatives(){
		String memcacheKey="all_creatives";
		List<CreativeObj> creativeList=(List<CreativeObj>) MemcacheUtil.getObjectsListFromCache(memcacheKey);
		if(creativeList==null || creativeList.size()==0){
			IProductDAO productDAO=new ProductDAO();
			try {
				creativeList=productDAO.getAllCreativeObj();
				if(creativeList !=null && creativeList.size()>0){
					MemcacheUtil.setObjectsListInCache(creativeList, memcacheKey);
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException :"+e.getMessage());
			}
		}else{
			log.info("Found creatives in memcache :"+creativeList.size());
		}
		return creativeList;		
	}
	
	public JSONObject creativeJSON(List<CreativeObj> creativeList){
		JSONObject jsonObject=new JSONObject();
		if(creativeList !=null && creativeList.size()>0){	
			  Map<String,JSONArray> formatMap=new HashMap<String,JSONArray>();
			  JSONArray jsonArray=new JSONArray();
			  
			  for(CreativeObj creative:creativeList){
				  String format=creative.getFormat();
				 // String id=creative.getId()+"";
				  String size=creative.getSize();
				  if(!formatMap.containsKey(format)){
					  //JSONObject sizeJson=new JSONObject();
					  //sizeJson.put("id", id);
					  //sizeJson.put("size", size);
					  JSONArray sizeArray=new JSONArray();
					  sizeArray.add(size);
					  formatMap.put(format, sizeArray);
				  }else{
					  JSONArray sizeArray=formatMap.get(format);
					  //JSONObject sizeJson=new JSONObject();
					  //sizeJson.put("id", id);
					  //sizeJson.put("size", size);
					  sizeArray.add(size);
					  formatMap.put(format, sizeArray);
				  }
			 }
			 for(String format:formatMap.keySet()){
				 JSONArray sizeArray=formatMap.get(format);	
				 JSONObject formatJson=new JSONObject();
				 formatJson.put("format", format);
				 formatJson.put("sizes", sizeArray);
				 jsonArray.add(formatJson);
			 }
			  jsonObject.put("Creatives", jsonArray);
		}else{
			  jsonObject.put("error", "Invalid state code, not found in datastore");
		}
		return jsonObject;
	}
	
	public List<DeviceObj> getAllDevices(){
		log.info("Load all devicese:");
		IProductDAO productDAO=new ProductDAO();
		List<DeviceObj> resultList=null;
		try {
			resultList = productDAO.getAllDevices();
		} catch (DataServiceException e) {
			log.severe("DataServiceException:"+e.getMessage());
		}
		return resultList;
	}
	
	public List<PlatformObj> getAllPlatforms(){
		log.info("Load all platforms:");
		IProductDAO productDAO=new ProductDAO();
		List<PlatformObj> resultList=null;
		try {
			resultList = productDAO.getAllPlatforms();
		} catch (DataServiceException e) {
			log.severe("DataServiceException:"+e.getMessage());
		}
		return resultList;
	}

	@Override
	public List<ProductsObj> productSetup(long userId, boolean isSuperAdmin) throws Exception {
		log.info("IN productSetup ");
		IProductDAO productDAO = new ProductDAO();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<ProductsObj> productsObjList = new ArrayList<>();
		try {
			if(isSuperAdmin) {
				List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
				if(companyObjList != null && companyObjList.size() > 0) {
					/*Map<String, AdServerCredentialsDTO> map = userService.getCompanyDFPCredentials(companyObjList);
					if(map != null && !map.isEmpty()) {
						for(String key : map.keySet()) {
							AdServerCredentialsDTO adServerCredentialsDTO = map.get(key);
							if(adServerCredentialsDTO != null && adServerCredentialsDTO.getAdServerId() != null && adServerCredentialsDTO.getAdServerUsername() != null && adServerCredentialsDTO.getAdServerPassword() != null) {
								List<ProductsObj> list = MemcacheUtil.getProductObj(adServerCredentialsDTO.getAdServerId());
								if(list == null || list.size() == 0) {
									list = productDAO.getProductByPublisherId(adServerCredentialsDTO.getAdServerId());
								}
								if(list != null && list.size() > 0) {
									MemcacheUtil.setProductObj(adServerCredentialsDTO.getAdServerId(), list);
									productsObjList.addAll(list);
								}
							}
						}
					}*/
					for(CompanyObj companyObj : companyObjList) {
						if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
							List<ProductsObj> list = MemcacheUtil.getProductObj(companyObj.getId()+"");
							if(list == null || list.size() == 0) {
								list = productDAO.getProductByPartnerId(companyObj.getId()+"");
							}
							if(list != null && list.size() > 0) {
								MemcacheUtil.setProductObj(companyObj.getId()+"", list);
								productsObjList.addAll(list);
							}
						}
					}
					////////
				}
			}
			else {
				CompanyObj companyObj = userService.getCompanyIdByAdminUserId(userId);
				if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && companyObj.getCompanyType() != null && companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0])) {
					/*if(companyObj.getAdServerId() != null && companyObj.getAdServerId().size() > 0 && companyObj.getAdServerUsername() != null && companyObj.getAdServerUsername().size() > 0 && companyObj.getAdServerPassword() != null && companyObj.getAdServerPassword().size() > 0) {
						productsObjList = MemcacheUtil.getProductObj(companyObj.getAdServerId().get(0));
						if(productsObjList == null || productsObjList.size() == 0) {
							productsObjList = productDAO.getProductByPublisherId(companyObj.getAdServerId().get(0));
							MemcacheUtil.setProductObj(companyObj.getAdServerId().get(0), productsObjList);
						}
					}*/
					productsObjList = MemcacheUtil.getProductObj(companyObj.getId()+"");
					if(productsObjList == null || productsObjList.size() == 0) {
						productsObjList = productDAO.getProductByPartnerId(companyObj.getId()+"");
						MemcacheUtil.setProductObj(companyObj.getId()+"", productsObjList);
					}
					////////
				}
			}
			if(productsObjList != null && productsObjList.size() > 0) {
				for(ProductsObj obj : productsObjList) {
					obj.setId("0");
					obj.setNote("");
					obj.setContext(null);
					obj.setCreative(null);
					obj.setCities(null);
					obj.setStates(null);
					obj.setDmas(null);
					obj.setPlatforms(null);
					obj.setDevices(null);
					obj.setAdUnits(null);
				}
			}
		}catch (Exception e) {
			log.severe("Exception in productSetup of ProductService : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return productsObjList;
	}
	
	@SuppressWarnings("unchecked")
    public List<AdUnitDTO> searchAdUnits(String searchText, String adServerId){	
		log.info("Search adUnits with searchText:"+searchText);
		String memcacheKey="search_adUnits_"+searchText;		
		
		List<AdUnitDTO> searchList=(List<AdUnitDTO>) MemcacheUtil.getObjectsListFromCache(memcacheKey);
		
		if(searchList ==null || searchList.size()==0){
			StringBuffer queryStr=DBUtil.createAdUnitSearchQuery(searchText,adServerId);
			log.info("search query:"+queryStr.toString());
			searchList=TextSearchDocument.searchAdUnits(queryStr.toString(), 0, 200);
			if(searchList !=null && searchList.size()>0){
				MemcacheUtil.setObjectsListInCache(searchList, memcacheKey);
			}
			
		}else{
			log.info("Search data found in memcache :"+searchList.size());
		}
		return searchList;
	}
	
	public JSONObject searchAdUnitsJSON(List<AdUnitDTO> searchList){
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		//jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","canonicalPath","parentId","text"}, true));
		if(searchList !=null && searchList.size()>0){			 
			  for(AdUnitDTO adUnit:searchList){
				  JSONObject json=new JSONObject();					  
				  json.put("id", adUnit.getId());
				  json.put("name", adUnit.getName());
				  json.put("canonicalPath", adUnit.getCanonicalPath());
				  json.put("parentId", adUnit.getPid());
				  json.put("text", adUnit.getName()+"  ["+adUnit.getCanonicalPath()+"]");
				  jsonArray.add(json);
			  }
		}else{
			log.info("no results found.");
		}
		//jsonArray.add(createAllNoneOptionJsonObject(new String[] {"id","name","canonicalPath","parentId","text"}, false));
		jsonObject.put("adUnits", jsonArray);
		return jsonObject;
	}
	
	public List<ProductsObj> aggregateSearchProductsByPartner(List<ProductsObj> searchList){
		List<ProductsObj> partnerWiseProductList=new ArrayList<ProductsObj>();
		Map<String,List<AdUnitDTO>> aggregatedPartnerwiseAdUnits=new HashMap<String,List<AdUnitDTO>>();
		Map<String,List<CreativeObj>> aggregatedPartnerwiseCreatives=new HashMap<String,List<CreativeObj>>();
		List<AdUnitDTO> adUnits=null;
		List<CreativeObj> creativeList=null;
		List<String> partnerList=new ArrayList<String>();
		for(ProductsObj product:searchList){
			String publisherId=product.getPublisherId();
			
			if(!partnerList.contains(publisherId)){
				partnerList.add(publisherId);
				adUnits=product.getAdUnits();
				creativeList=product.getCreative();
				partnerWiseProductList.add(product);
			}else{
				adUnits=aggregatedPartnerwiseAdUnits.get(publisherId);
				if(adUnits !=null && product.getAdUnits() !=null){
					adUnits.addAll(product.getAdUnits());					
				}
				creativeList=aggregatedPartnerwiseCreatives.get(publisherId);
				if(creativeList !=null && product.getCreative() !=null){
					creativeList.addAll(product.getCreative());
				}
			}
			aggregatedPartnerwiseAdUnits.put(publisherId, adUnits);
			aggregatedPartnerwiseCreatives.put(publisherId, creativeList);
		}
		
		for(int i=0;i<partnerWiseProductList.size();i++){
			ProductsObj product=partnerWiseProductList.get(i);
			String publisherId=product.getPublisherId();
			adUnits=aggregatedPartnerwiseAdUnits.get(publisherId);
			creativeList=aggregatedPartnerwiseCreatives.get(publisherId);
			product.setAdUnits(adUnits);
			product.setCreative(creativeList);
			
			partnerWiseProductList.set(i, product);
			
		}
		
		return partnerWiseProductList;
		
	}
	
	public JSONObject getPlacementsJSON(List<PlacementObj> placementList){
		JSONObject placementData=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		for(PlacementObj placement:placementList){
			
			JSONObject jsonObj=new JSONObject();
		    jsonObj.put("placementName",placement.getPlacementName());
		    jsonObj.put("placementId",placement.getPlacementId());
		    jsonObj.put("createdBy",placement.getCreatedBy());
		    jsonObj.put("createdOn",placement.getCreatedOn());
		    jsonObj.put("site",placement.getSiteName());
		    jsonObj.put("publisherCPM",placement.getPublisherCPM());
		    jsonObj.put("budgetAllocation",placement.getBudgetAllocation());
		    jsonObj.put("proposedImpression",placement.getProposedImpression());
		    jsonObj.put("marginPercent",placement.getMarginPercent());
		    jsonObj.put("effectiveCPM",placement.getEffectiveCPM());
		    jsonObj.put("firstPartyAdServerCost",placement.getFirstPartyAdServerCost());
		    jsonObj.put("thirdPartyAdServerCost",placement.getThirdPartyAdServerCost());
		    jsonObj.put("netCostCPM",placement.getNetCostCPM());
		    jsonObj.put("netCost",placement.getNetCost());
		    jsonObj.put("grossRevenue",placement.getGrossRevenue());
		    jsonObj.put("publisherPayout",placement.getPublisherPayout());
		    jsonObj.put("servingFees",placement.getServingFees());
		    jsonObj.put("netRevenue",placement.getNetRevenue());
		    
		    jsonArray.add(jsonObj);
		}
		placementData.put("placement", jsonArray);
		return placementData;
	}

	@Override
	public ProductsObj getProductByName(String productName) throws Exception {
		ProductsObj productsObj = null;
		try {
			IProductDAO productDAO = new ProductDAO();
			productsObj = productDAO.getProductByName(productName);
		}
		catch(DataServiceException de) {
			log.severe("DataServiceException : "+de.getMessage());
			de.printStackTrace();
			throw de;
		}
		catch (Exception e) {
			log.severe("Exception : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return productsObj;
	}
	
	public void saveAdUnitHierarchy(List<AdUnitHierarchy> adUnitHierarchyList){
		IProductDAO dao = new ProductDAO();		
		try {
			for(AdUnitHierarchy adUnitObj:adUnitHierarchyList){
				dao.saveObject(adUnitObj);
				TextSearchDocument.createDocument(adUnitObj);	
			}
			log.info("AdUnit list saved...."+adUnitHierarchyList.size());
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}		
		
	}
	
	public ProductsObj loadProduct(long productId){
		IProductDAO dao = new ProductDAO();
		ProductsObj product=null;
		try {
			product=dao.getProductById(productId);
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}
		return product;
	}
	
	@Override
	public List<ProductsObj> loadProducts(List<String> productIds){
		IProductDAO dao = new ProductDAO();
		List<ProductsObj> products =null;
		try {
			products =dao.getProductByIds(productIds);
		} catch (DataServiceException e) {
			log.severe("DataServiceException : "+e.getMessage());
		}
		return products;
	}
	
	
	/*
	 * @author Youdhveer Panwar
	 * Load all products from datastore
	 */
	public List<ProductsObj> loadAllProducts(){
		log.info("Loading all products from datastore...");
		IProductDAO productDAO= new ProductDAO();
		List<ProductsObj> productList=null;
		try {
			productList=productDAO.getAllProducts();
			if(productList !=null){
				log.info("Total products : "+productList.size());
			}
		} catch (DataServiceException e) {
			log.severe("Failed to load all products : "+e.getMessage());			
		}
		return productList;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * This method will fetch all products and split them by networkCode along with adUnitIds 
	 */
	public Map<String, List<String>> loadProductsAdUnitIdsMapByAdServerId(){
		log.info("Loading all adUnitIds from products along with networkCode..");
		
		Map<String, List<String>> productAdUnitMap=new HashMap<String,List<String>>();
		List<ProductsObj> productList=loadAllProducts();
		
		if(productList!=null && productList.size()>0){
			for(ProductsObj product:productList){
				String networkCode=product.getPublisherId();
				List<AdUnitDTO> adUnitDTOList=product.getAdUnits();
				List<String> topLevelAdUnitList=null;
				if(productAdUnitMap.containsKey(networkCode)){
					topLevelAdUnitList=productAdUnitMap.get(networkCode);
				}else{
					topLevelAdUnitList=new ArrayList<String>();			
				}
				
				if(adUnitDTOList !=null ){
					for(AdUnitDTO adUnitDTO:adUnitDTOList){
						long adUnitId=adUnitDTO.getId();						
						String canonicalPath=adUnitDTO.getCanonicalPath();
						//System.out.println("adUnitId: "+adUnitId+" and name :"+adUnitDTO.getName()+" and canonicalPath: "+canonicalPath);
						String [] adUnitArray=canonicalPath.split(" > ");
						if(adUnitArray.length>0){
							String topLevelAdUnit=adUnitArray[0];
							//System.out.println("add topLevelAdUnit : "+topLevelAdUnit);
							if(!topLevelAdUnitList.contains(topLevelAdUnit)){
								topLevelAdUnitList.add(topLevelAdUnit);
							}
						}						
					}		
				}else{
					log.info("No adUnits for product..."+product.getId());
				}
				//log.info("add in map for networkCode : "+networkCode+", topLevelAdUnitList : "+topLevelAdUnitList);	
				//productAdUnitIdMap.put(networkCode, adUnitIdList);
				productAdUnitMap.put(networkCode, topLevelAdUnitList);
			}
		}else{
			log.info("No prodcuts found in datastore.");
		}
				
		log.info("productAdUnitMap :"+productAdUnitMap.size());
		Map<String, List<String>> productAdUnitIdMap=new HashMap<String,List<String>>();
		
		
		for(String networkCode:productAdUnitMap.keySet()){
			
			List<String> topLevelAdUnitList=productAdUnitMap.get(networkCode);
			if(topLevelAdUnitList !=null){
				//log.info("here : topLevelAdUnitList size : "+topLevelAdUnitList.size());
				for(String adUnitName:topLevelAdUnitList){
					AdUnitHierarchy adUnitObj=loadAdUnitByName(adUnitName, networkCode);
					List<String> adUnitIdList=null;
					if(adUnitObj !=null){
						String adUnitId=adUnitObj.getAdUnitId();
						if(productAdUnitIdMap.containsKey(networkCode)){
							adUnitIdList=productAdUnitIdMap.get(networkCode);
						}else{
							adUnitIdList=new ArrayList<String>();			
						}
						if(adUnitId !=null && adUnitIdList !=null &&
								!adUnitIdList.contains(adUnitId)){
							adUnitIdList.add(adUnitId);
						}
						//log.info("add in map : "+adUnitIdList);
						productAdUnitIdMap.put(networkCode, adUnitIdList);
					}
				}
			}else{
				log.info("topLevelAdUnitList : null");
			}
			
		}
		log.info("productAdUnitIdMap :"+productAdUnitIdMap.size());
		return productAdUnitIdMap;
	}
	
	/*
	 * @author Youdhveer Panwar
	 * Load AdUnitHiearchy object form datastore based on adUnitName and networkCode
	 * 
	 */
	public AdUnitHierarchy loadAdUnitByName(String adUnitName,String adServerCode){
		//log.info("Load adUnit for adUnitName :"+adUnitName+" and adServerCode : "+adServerCode);
		AdUnitHierarchy adUnitObj=null;
		if(adUnitName !=null && adServerCode !=null){
			IProductDAO productDAO=new ProductDAO();
			try {
				List<AdUnitHierarchy> adUnitList=productDAO.loadAdUnitHierachyObj(adUnitName, adServerCode);
				if(adUnitList !=null){
					for(AdUnitHierarchy adUnit:adUnitList){
						if(adUnit !=null && adUnit.getAdServerId()!=null 
								&& adUnit.getAdServerId().equalsIgnoreCase(adServerCode)){
							adUnitObj=adUnit;
							break;
						}
					}
				}else{
					log.warning("adunit not found from datastore :adUnitName :"+adUnitName+" and adServerCode : "+adServerCode);
				}
			} catch (DataServiceException e) {
				log.severe("Failed to loadAdunit : "+adUnitName+" : exception : "+e.getMessage());
			}
		}
		
		return adUnitObj;
	}
	
	/* Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IProductService#getCityObjCount()
	 * returns total count of records of CityObj available in data store
	 */
	public int getCityObjCount()throws DataServiceException {
		
		return TextSearchDocument.getCityObjCount();
	}
	
	/*Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IProductService#getAdUnitHierarchyCount()
	 * returns total count of records of adUnitHiarchy available in data store
	 */
	public int getAdUnitHierarchyCount()throws DataServiceException {
		
		return TextSearchDocument.getAdUnitHierarchyCount();
	}
	
	/* Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IProductService#loadCityDataThroughTaskQueue(java.lang.String, java.lang.String)
	 * getting CityObj from data store on the basis of offset and limit
	 */
	public void loadCityDataThroughTaskQueue(String offset, String limit){
		log.info("Loading all CityObj from datastore to index...");
		IProductDAO productDAO= new ProductDAO();
		List<CityObj> cityList=null;
		try {
			cityList=productDAO.getCities(offset,limit);
			
			if(cityList != null && cityList.size() > 0)
			{
				log.info("cityList.size() = "+cityList.size());
				for(CityObj city : cityList){
					TextSearchDocument.createDocument(city);
				}
			}
			
		} catch (Exception e) {
			log.severe("Failed to load all CityObj : "+e.toString());			
		}
	}
	
	/* Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IProductService#loadAdUnitDataThroughTaskQueue(java.lang.String, java.lang.String)
	 * getting AdUnitHierarchy from data store on the basis of offset and limit
	 */
	public void loadAdUnitDataThroughTaskQueue(String offset, String limit){
		log.info("Loading all AdUnitHierarchy from datastore to index...");
		IProductDAO productDAO= new ProductDAO();
		List<AdUnitHierarchy> adUnitHierarchyList=null;
		try {
			adUnitHierarchyList=productDAO.getAdUnits(offset,limit);
			
			if(adUnitHierarchyList != null && adUnitHierarchyList.size() > 0)
			{
				log.info("adUnitHierarchyList.size() = "+adUnitHierarchyList.size());
				for(AdUnitHierarchy adUnit : adUnitHierarchyList){
					TextSearchDocument.createDocument(adUnit);
				}
			}
			
		} catch (Exception e) {
			log.severe("Failed to load all AdUnitHierarchy Data : "+e.toString());			
		}
	}
	
	/*
	 * @author Youdhveer Panwar
	 * Load all gptTags from SmartMediaPlan for a campaign
	 */
	public JSONObject loadGPTTagsFromSmartMediaPlan(String campaignId){
		SmartMediaPlanObj smartMediaPlan=loadSmartMediaPlan(campaignId);
		JSONObject gptJSONTag=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		if(smartMediaPlan !=null){
			gptJSONTag.put("campaignId", campaignId);
			List<SmartCampaignPlacementDTO> dfpPlacements=smartMediaPlan.getDfpPlacements();			
			if(dfpPlacements !=null){
				log.info("dfpPlacements : "+dfpPlacements.size());
				
				for(SmartCampaignPlacementDTO placementDTO:dfpPlacements){
					String placementName=placementDTO.getPlacementName();
					String gptTags=placementDTO.getGptTag();
					JSONObject gptTag=new JSONObject();
					gptTag.put("id", placementDTO.getLineItemId());
					gptTag.put("placementName", placementName);
					gptTag.put("lineItemName", placementDTO.getLineItemName());
					gptTag.put("productName", placementDTO.getProductName());
					gptTag.put("partner", placementDTO.getPartnerName());
					gptTag.put("gptTags", gptTags);
					jsonArray.add(gptTag);
				}
				gptJSONTag.put("lineItems", jsonArray);
			}else{
				gptJSONTag.put("gptTags", "None");
			}
			
		}
		return gptJSONTag;
	}

	/*
	 * @author Anup Dutta
	 * @description This method takes 
	 * @return JSONArray which contain Impression and click based on location 
	 **/
	@Override
	public JSONArray getLocationProductInfo(String groupBy,JSONObject productProperty) {
		IProductDAO productDAO = new ProductDAO();
		JSONArray jsonArray = new JSONArray();
		
		String[] sizeList = null;
		String[] adFormatList = null;
		String[] contextList = null;
		String[] contextSubList = null;
		String[] dmaList = null;
		String[] stateList = null;
		String[] cityList = null;
		String[] zipList = null;
		String[] deviceList = null;
		String[] platformList = null;
		
		try{
			JSONArray temp = productProperty.getJSONArray("size");
			sizeList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				sizeList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Size array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("adformat");
			adFormatList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				adFormatList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Adformat array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("context");
			contextList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				contextList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Context array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("contextsub");
			contextSubList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				contextSubList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Context Sublist array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("dma");
			dmaList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				dmaList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("DMA array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("state");
			stateList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				stateList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("State array not found");}
		
		
		try{
			JSONArray temp = productProperty.getJSONArray("city");
			cityList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				cityList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("City array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("zip");
			zipList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				zipList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Zip array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("device");
			deviceList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				deviceList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Device array not found");}
		
		try{
			JSONArray temp = productProperty.getJSONArray("platform");
			platformList = new String[temp.size()];
			for(int i= 0;i<temp.size();i++){
				platformList[i]=temp.getString(i);
			}
		}catch(Exception e){log.info("Platform array not found");}
		
		QueryDTO queryDTO=null;
		QueryResponse queryResponse = null;
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		
		List<String> adUnits = new ArrayList<String>();
		try {
			List<ProductsObj> productList = productDAO.searchProductsInDataStore(adFormatList, sizeList, contextList, 
					contextSubList, dmaList, stateList, cityList, zipList, deviceList, platformList);
			for(ProductsObj product : productList){
				if(product.getAdUnits()!=null){
					for(AdUnitDTO adUnit : product.getAdUnits()){
						if(!adUnits.contains(adUnit.getAdUnitCode())){
							adUnits.add(adUnit.getId()+"");
						}
					}
				}
			}
			
			List<String> bqTables = BigQueryUtil.getTables(LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS, LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY, LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID, LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
			
			String productTable = "";
			
			for(String tName : bqTables){
				if(tName.contains("Product_Performance")){
					tName = tName.replace("one-lin-mobile:","");
					if(productTable.equals("")){
						productTable += tName;
					}else{
						productTable += ","+ tName;
					}
				}
			}
			queryDTO = BigQueryUtil.getProductPerformanceDTO();
			queryResponse = dao.getProductPerformance(groupBy, productTable, queryDTO , adUnits);		
			
			List<TableRow> records = queryResponse.getRows();
			for (TableRow row : records) {
				if(row != null && row.getF() != null && row.getF().size() > 0) {
					List<TableCell> cellList = row.getF();
					
					JSONObject jsonrow = new JSONObject();
					jsonrow.put("location",cellList.get(0).getV().toString());
					jsonrow.put("impression",(int)Float.parseFloat(cellList.get(1).getV().toString()));
					jsonrow.put("click",(int)Float.parseFloat(cellList.get(2).getV().toString()));
					jsonArray.add(jsonrow);
				}
			}
			
		} catch (DataServiceException e) {
			log.info("Inside getLocationProductInfo :" + e.getMessage());
		}
		return jsonArray;
	}

	@Override
	public JSONArray getProductForecastPerformance(String groupBy) {
		IProductDAO productDAO = new ProductDAO();
		JSONArray jsonArray = new JSONArray();
		try{
			List<ProductForecastObj> forecastObjs = productDAO.getProductForecast(groupBy);
			log.info("Inside getProductForecastPerformance : Total : " + forecastObjs.size());
			for(ProductForecastObj forcastObj : forecastObjs){
				log.info("inside loop : " + forcastObj.getProductId());
				
				ProductsObj productsObj = productDAO.getProductById(forcastObj.getProductId());
				
				JSONObject jsonrow = new JSONObject();
				jsonrow.put("partnerID", forcastObj.getPartnerId());
				jsonrow.put("prodID", forcastObj.getProductId());
				jsonrow.put("prodName", forcastObj.getProductName());
				jsonrow.put("publisherName", forcastObj.getPublisherName());
				jsonrow.put("locationID", forcastObj.getDmaDetail().getGeoTargetId());
				jsonrow.put("locationName", forcastObj.getDmaDetail().getGeoTargetsName());
				
				jsonrow.put("device", productsObj.getDevices());
				jsonrow.put("creative", productsObj.getCreative());
				jsonrow.put("platform", productsObj.getPlatforms());
				
				JSONArray jsonForecastArray = new JSONArray();
				
				try{
					JSONObject jsonForecastrow = new JSONObject();
					//for(ProductForecastDTO productForecastDTO : forcastObj.getForcastProductList()){
						ProductForecastDTO productForecastDTO = forcastObj.getForcaProductDTO();
						jsonForecastrow.put("dayDiff", DateUtil.getDaysDiff(productForecastDTO.getStartDate(),productForecastDTO.getEndDate()));
						jsonForecastrow.put("availableImps",productForecastDTO.getAvailableImpressions());
						jsonForecastrow.put("deliveredImps",productForecastDTO.getDeliveredImpressions());
						jsonForecastrow.put("matchedImps",productForecastDTO.getMatchedImpressions());
						jsonForecastrow.put("possibleImps",productForecastDTO.getPossibleImpressions());
						jsonForecastrow.put("reserveImps",productForecastDTO.getReservedImpressions());
						log.info("inside sub loop : " + productForecastDTO.getAvailableImpressions());
						jsonForecastArray.add(jsonForecastrow);
					//}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				jsonrow.put("forecast", jsonForecastArray);
				jsonArray.add(jsonrow);
				
			}
		}catch(Exception e){
			log.info("error :" + e.getMessage());
			e.printStackTrace();
		}
		return jsonArray;
	}
}
