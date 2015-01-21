package com.lin.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.dfp.api.IForecastInventoryService;
import com.lin.dfp.api.impl.ForecastInventoryService;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.IProductService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.ProductService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

public class SmartCampaignPlannerAction implements ModelDriven<UserDetailsDTO>, ServletRequestAware,ServletResponseAware, SessionAware {
	private static final Logger log = Logger.getLogger(SmartCampaignPlannerAction.class.getName());
	private SessionObjectDTO sessionDTO;
	private Map session;
	private HttpServletRequest request;
	private List<PlatformObj> platformObjList;
	private List<DeviceObj> deviceObjList;
	private List<CreativeObj> creativeObjList;
	private List<SmartCampaignObj> cmapaignObjList;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	private UnifiedCampaignDTO unifiedCampaignDTO = new UnifiedCampaignDTO();
	private AdvertiserObj advertisersObj = new AdvertiserObj();
	private AccountsEntity accountObj = new AccountsEntity();
	private AgencyObj agenciesObj = new AgencyObj();
	private boolean deleted;
	private boolean unarchived;
	private boolean paused;
	private boolean resumed;
	private boolean canceled;
	private boolean unCanceled;
	private boolean isSave = false;
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private String status;
	    
	public String campaignStatus = "update";
	public String allOptionId = ProductService.allOptionId;
	public String allOption = ProductService.allOption;
	public String noneOptionId = ProductService.noneOptionId;
	public String noneOption = ProductService.noneOption;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	private Map<String,List<CensusDTO>> censusMap;
	
	
	/*public String mySettingPageName = LinMobileConstants.APP_VIEWS[6]
			.split(LinMobileConstants.ARRAY_SPLITTER)[1].trim();

	public String publisherViewPageName = (LinMobileConstants.APP_VIEWS[0])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String advertiserViewPageName = (LinMobileConstants.APP_VIEWS[1])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mediaPlannerPageName = (LinMobileConstants.APP_VIEWS[2])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String adminPageName = (LinMobileConstants.APP_VIEWS[5])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String operationalViewPageName = (LinMobileConstants.APP_VIEWS[2])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String poolPageName = (LinMobileConstants.APP_VIEWS[3])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String newsAndResearchPageName = (LinMobileConstants.APP_VIEWS[4])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
    public String unifiedCampaign = (LinMobileConstants.APP_VIEWS[7])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];*/
    
    
    public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			e.printStackTrace();
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}

	public String execute(){
    	log.info("SmartCampaignPlannerAction action executes..");
    	try{
    		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			
			//unifiedCampaignDTO = service.getAllCampaignList(unifiedCampaignDTO);
	    	return Action.SUCCESS;
    	}
    	catch (Exception e) {
    		log.severe("Exception in SmartCampaignPlannerAction : "+e.getMessage());
    		e.printStackTrace();
		}
    	return "unAuthorisedAccess";
    }
	
	public String loadAllCampaigns() {
		log.info("loadAllCampaigns starts.. "+new Date());
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			int campaignPerPage = 25;
			int pageNumber = 1;
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			String campaignStatus = request.getParameter("campaignStatus");
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			String searchKeyword=request.getParameter("searchKeyword");
			log.info("limit : "+limit+", offset : "+offset+", searchKeyword : "+searchKeyword);
			if(limit != null && (LinMobileUtil.isNumeric(limit))) {
				campaignPerPage = Integer.parseInt(limit);
				if(campaignPerPage <= 0) {
					campaignPerPage = 20;
				}
			}
			if(offset != null && (LinMobileUtil.isNumeric(offset.trim()))) {
				pageNumber = Integer.parseInt(offset);
				if(pageNumber <= 0) {
					pageNumber = 1;
				}
			}
			if(searchKeyword != null) {
				searchKeyword = searchKeyword.toLowerCase().trim();
			}
			jsonArray = service.loadAllCampaigns(campaignStatus, sessionDTO.getUserId(),sessionDTO.isSuperAdmin(), campaignPerPage, pageNumber, searchKeyword);
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadAllCampaigns of ProductAction : "+e.getMessage());
    		e.printStackTrace();
    		return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String initCampaignFromMap(){
		String responseStr = initCampaign();
		setJsonDataInUnifiedCampaign(unifiedCampaignDTO, true); 
		return responseStr;
	}
	
	/**
	 * 
	 * Data comes to this method in form of json. The source of data(at present) is the map.
	 * This method uses the products and other data to prefill the unifiedCampaignDTO values.
	 * 
	 */
	private void setJsonDataInUnifiedCampaign(UnifiedCampaignDTO unifiedCampaignDTO, boolean checkMaster){
		try{
			JSONObject jsonObject  = (JSONObject) JSONSerializer.toJSON(request.getParameter("jsonParam").trim());

		 	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			String products = jsonObject.getString("products");
			
			Date startDate = new Date();
			Date endDate = DateUtil.getDateBeforeAfterDays(30);
			unifiedCampaignDTO.setStartDate(DateUtil.getDateAsString(startDate, "MM-dd-yyyy"));
			unifiedCampaignDTO.setEndDate(DateUtil.getDateAsString(endDate, "MM-dd-yyyy"));
			unifiedCampaignDTO.setPStartDate(DateUtil.getDateAsString(startDate, "MM-dd-yyyy"));
			unifiedCampaignDTO.setPEndDate(DateUtil.getDateAsString(endDate, "MM-dd-yyyy"));
			
			if(products != null && products.trim().length() > 0){
				List<ProductsObj> productsList = productService.loadProducts(Arrays.asList(products.split(",")));
				if(productsList != null){
					Set<CommonDTO> creatives = new HashSet<CommonDTO>();
					Set<CommonDTO> platforms = new HashSet<CommonDTO>();
					Set<CommonDTO> devices = new HashSet<CommonDTO>();
					for(ProductsObj product: productsList){
						if(product.getCreative() != null){
							for(CreativeObj  creative: product.getCreative()){
								creatives.add(new CommonDTO(String.valueOf(creative.getId()), creative.getSize()));
							}
						}
					 
						if(product.getPlatforms() != null && product.getPlatforms().size() > 0){
							for(PlatformObj platform : product.getPlatforms()){
								if(String.valueOf(platform.getId()).equals(ProductService.allOptionId)){
									platforms.clear();
									platforms.add(new CommonDTO(ProductService.allOptionId, ProductService.allOption));
									break;
								}
								platforms.add(new CommonDTO(String.valueOf(platform.getId()), platform.getText()));
								
							}
						}else {
							platforms.add(new CommonDTO(ProductService.allOptionId, ProductService.allOption));
						}
						
						if(product.getDevices() != null && product.getDevices().size() > 0){
						
							for(DeviceObj device : product.getDevices()){
								if(String.valueOf(device.getId()).equals(ProductService.allOptionId)){
									devices.clear();
									devices.add(new CommonDTO(ProductService.allOptionId, ProductService.allOption));
									break;
								}

								devices.add(new CommonDTO(String.valueOf(device.getId()), device.getText()));
							}
						}else {
							devices.add(new CommonDTO(ProductService.allOptionId, ProductService.allOption));
						}
						unifiedCampaignDTO.setDeviceCapability(product.getDeviceCapability());	
						unifiedCampaignDTO.setSelectedDevicePlacementList(new ArrayList<CommonDTO>(devices));
						unifiedCampaignDTO.setSelectedPlatformPlacementList(new ArrayList<CommonDTO>(platforms));
						unifiedCampaignDTO.setSelectedCreativePlacementList(new ArrayList<CommonDTO>(creatives));
						 
					}
					
				}
				int deviceCapability = ReportUtil.TARGETING_ANY;
				for(ProductsObj product: productsList){
					if((deviceCapability == ReportUtil.TARGETING_MOBILE_WEB_ONLY && product.getDeviceCapability() == ReportUtil.TARGETING_MOBILE_APP_ONLY)||
						(deviceCapability == ReportUtil.TARGETING_MOBILE_APP_ONLY && product.getDeviceCapability() == ReportUtil.TARGETING_MOBILE_WEB_ONLY)){
							deviceCapability = ReportUtil.TARGETING_MOBILE_APP_AND_WEB;   
							continue;
					   } 
					
					if(product.getDeviceCapability() == ReportUtil.TARGETING_ANY){
						deviceCapability = ReportUtil.TARGETING_ANY;
						break;
					}
					deviceCapability = product.getDeviceCapability();
				}
				unifiedCampaignDTO.setDeviceCapability(deviceCapability);
 			}
			try{unifiedCampaignDTO.setSelectedPlacementProducts(jsonObject.getString("products"));}catch(Exception e){}
			try{unifiedCampaignDTO.setUpperAge(jsonObject.getString("upperAge"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setLowerAge(jsonObject.getString("lowerAge"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setUpperIncome(jsonObject.getString("upperIncome"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setLowerIncome(jsonObject.getString("lowerIncome"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setGender(jsonObject.getString("gender"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedEducationList(getDTOListFromJSONObject(jsonObject.getString("educationList"), unifiedCampaignDTO.getEducationList(), checkMaster));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedEthinicityList(getDTOListFromJSONObject(jsonObject.getString("ethnicityList"), unifiedCampaignDTO.getEthinicityList(), checkMaster));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}

			try{unifiedCampaignDTO.setSelectedCountryList(getDTOListFromJSON(jsonObject.getString("countryList"), unifiedCampaignDTO.getCountryList(), checkMaster));unifiedCampaignDTO.setIsGeographic(Boolean.TRUE.toString());}catch(Exception e){}
		 	try{unifiedCampaignDTO.setSelectedDMAPlacementList(getDTOListFromJSONObject(jsonObject.getString("dmaList"), unifiedCampaignDTO.getDmaList(), checkMaster));unifiedCampaignDTO.setIsGeographic(Boolean.TRUE.toString());}catch(Exception e){}
		 	try{unifiedCampaignDTO.setSelectedStateList(getDTOListFromJSONObject(jsonObject.getString("stateList"), unifiedCampaignDTO.getStateList(), checkMaster));unifiedCampaignDTO.setIsGeographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{
				String cityList = jsonObject.getString("cityList");
				if(cityList != null && cityList.trim().length()>0){
					unifiedCampaignDTO.setCityJSON("{\"cityList\":"+cityList+"}");				
				}
				unifiedCampaignDTO.setIsGeographic(Boolean.TRUE.toString());
			}catch(Exception e){}
			try{
				String zipList = jsonObject.getString("zipList");
				if(zipList != null && zipList.trim().length()>0){
					unifiedCampaignDTO.setZipJSON("{\"zipList\":"+zipList+"}");				
				}
				unifiedCampaignDTO.setIsGeographic(Boolean.TRUE.toString());
			}catch(Exception e){}
			
			try{unifiedCampaignDTO.setSelectedCensusAge(jsonObject.getString("age"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedCensusIncome(jsonObject.getString("income"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedCensusEducation(jsonObject.getString("education"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedCensusEthnicity(jsonObject.getString("ethnicity"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			try{unifiedCampaignDTO.setSelectedCensusGender(jsonObject.getString("gender"));unifiedCampaignDTO.setIsDemographic(Boolean.TRUE.toString());}catch(Exception e){}
			
			try{unifiedCampaignDTO.setSelectedCensusRank(jsonObject.getString("rank"));}catch(Exception e){}
			
			
			}catch(Exception e){
				log.severe("ERROR while parsing main JSON ["+ request.getParameter("jsonParam") +"]");
			}
	}
	private List<CommonDTO> getDTOListFromJSON(String JSON, List<CommonDTO> masterList, boolean checkMaster){
		List<CommonDTO> list = new ArrayList<>();		 
		try{
			JSONObject jsonObject  = (JSONObject) JSONSerializer.toJSON(JSON);

			Set<?> set = jsonObject.keySet();
			
		if(!checkMaster){
			for(Object obj: set){
				list.add(new CommonDTO(obj.toString(),""));
			}
			return list;
		}

		if(masterList == null || masterList.size() == 0){
			return list;
		}
		
		for(Object obj: set){
			for(CommonDTO dto: masterList){
				if(dto.getId().equals(obj.toString())){
					list.add(dto);
				}
			}
		}}catch(Exception e){ 
			
			log.severe("Error while parsing JSON. "+ JSON);
		}
 		return list;
	}
	
	/**
	 * 
	 * @param JSON  
	 * @param masterList
	 * JSON should be a composition of json objects. Each json object must have id and name keys.
	 */
	private List<CommonDTO> getDTOListFromJSONObject(String JSON, List<CommonDTO> masterList, boolean checkMaster){
		
		List<CommonDTO> list = new ArrayList<>();	
		try{
		JSONArray jsonArray  = JSONArray.fromObject(JSON);
		
		if(jsonArray != null && jsonArray.size() > 0){
			if(!checkMaster){
			for(int i = 0 ; i < jsonArray.size() ; i++){
				JSONObject jsonObject  = (JSONObject) JSONSerializer.toJSON(jsonArray.get(i));
				list.add(new CommonDTO(jsonObject.get("id").toString(),""));
			}
			return list;
			}
		
		if(masterList == null || masterList.size() == 0){
			return list;
		}
		
			for(int i = 0 ; i < jsonArray.size() ; i++){
				JSONObject jsonObject  = (JSONObject) JSONSerializer.toJSON(jsonArray.get(i));
				for(CommonDTO dto: masterList){
					if(dto.getId().equals(jsonObject.get("id").toString())){
						list.add(dto);
					}
				}
			}
		 
		}
		
		}catch(Exception e){ 
			
			log.severe("Error while parsing JSON. "+ JSON);
		}
 		return list;
	}
	
	
	public static void main(String[] args) { 
		new SmartCampaignPlannerAction().getDTOListFromJSONObject(
				"[{\"id\":662,\"name\":\"Isabela\",\"stateId\":21193},{\"id\":637,\"name\":\"Sabana Grande\",\"stateId\":21193}]"
				,null, false
				);
		
		
	}
	public String initCampaign(){
		log.info("initCampaign ofSmartCampaignPlannerAction executes..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			campaignStatus = "create";
			unifiedCampaignDTO.setStatusId("create");
			unifiedCampaignDTO = service.initCampaign(unifiedCampaignDTO, sessionDTO.getUserId());
			
			censusMap = new HashMap<String, List<CensusDTO>>();
			List<CensusDTO> temp =  service.getCensusCategory();
			Collections.sort(temp);
			for(CensusDTO cTemp : temp){
				if(censusMap.containsKey(cTemp.getGroup())){
					censusMap.get(cTemp.getGroup()).add(cTemp);
				}else{
					List<CensusDTO> value = new ArrayList<CensusDTO>();
					value.add(cTemp);
					censusMap.put(cTemp.getGroup(), value);
				}
			}
			
		}catch(Exception e){
			log.severe("Exception in initCampaign of SmartCampaignPlannerAction : "+e.getMessage());
    		e.printStackTrace();	
		}
		return Action.SUCCESS;
		
	}
	
	/*public String AllCampaignsList(){
		log.info("SmartCampaignPlannerAction action getAllCampaigns..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			cmapaignObjList = service.getAllCampaignList();
		}catch(Exception e){
			log.severe("Exception in execution of getAllCampaigns() in  SmartCampaignPlannerAction: " + e.getMessage());
			e.printStackTrace();
		}
		return "success";
		
	}*/
	public String saveCampaign(){
		log.info("In saveCampaign method of SmartCampaignPlannerAction .."+unifiedCampaignDTO);
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			long saveCampaignId = service.saveCampaign(unifiedCampaignDTO,sessionDTO.getUserId(),sessionDTO.isSuperAdmin());
			
			unifiedCampaignDTO.setStatusId("edit");
			unifiedCampaignDTO = service.initEditCampaign(unifiedCampaignDTO,saveCampaignId, sessionDTO.getUserId());
			
			censusMap = new HashMap<String, List<CensusDTO>>();
			List<CensusDTO> temp =  service.getCensusCategory();
			Collections.sort(temp);
			for(CensusDTO cTemp : temp){
				if(censusMap.containsKey(cTemp.getGroup())){
					censusMap.get(cTemp.getGroup()).add(cTemp);
				}else{
					List<CensusDTO> value = new ArrayList<CensusDTO>();
					value.add(cTemp);
					censusMap.put(cTemp.getGroup(), value);
				}
			}
			
			return Action.SUCCESS;
		
		}catch(Exception e){
			log.severe("Exception in saveCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		
	}
	
/*	public String getPlatformObjs(){
		log.info("In getPlatformObjs method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			platformObjList = service.getPlatformObjs();
		}catch(Exception e){
			log.severe("Exception in getPlatformObjs method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
		}
		return "success";
		
	}
	
	public String getDeviceObjs(){
		log.info("In getDeviceObjs method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			deviceObjList = service.getDeviceObjs();
		}catch(Exception e){
			log.severe("Exception in getDeviceObjs method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
		}
		return "success";
		
	}
	
	public String getCreativeObjs(){
		log.info("In getCreativeObjs method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			String format = request.getParameter("format");
			creativeObjList = service.getCreativeSizeList(format);
		}catch(Exception e){
			log.severe("Exception in getCreativeObjs method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
		}
		return "success";
		
	}*/
	
	public String initEditCampaign(){
		log.info("In initEditCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			unifiedCampaignDTO.setStatusId("edit");
			unifiedCampaignDTO = service.initEditCampaign(unifiedCampaignDTO,0, sessionDTO.getUserId());
			
			censusMap = new HashMap<String, List<CensusDTO>>();
			List<CensusDTO> temp =  service.getCensusCategory();
			Collections.sort(temp);
			for(CensusDTO cTemp : temp){
				if(censusMap.containsKey(cTemp.getGroup())){
					censusMap.get(cTemp.getGroup()).add(cTemp);
				}else{
					List<CensusDTO> value = new ArrayList<CensusDTO>();
					value.add(cTemp);
					censusMap.put(cTemp.getGroup(), value);
				}
			}
			
			System.out.println("unifiedCampaignDTO----"+unifiedCampaignDTO.isHasMigrated());
		}catch(Exception e){
			log.severe("Exception in initEditCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
		
	}
	
	public String initEditPlacement(){
		log.info("In initEditPlacement method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			String campaignId = request.getParameter("campaignId");
			String placementId = request.getParameter("placementId");
			//unifiedCampaignDTO.setStatusId("edit");
			unifiedCampaignDTO = service.initEditPlacement(campaignId, placementId, sessionDTO.getUserId());
			
			censusMap = new HashMap<String, List<CensusDTO>>();
			List<CensusDTO> temp =  service.getCensusCategory();
			Collections.sort(temp);
			for(CensusDTO cTemp : temp){
				if(censusMap.containsKey(cTemp.getGroup())){
					censusMap.get(cTemp.getGroup()).add(cTemp);
				}else{
					List<CensusDTO> value = new ArrayList<CensusDTO>();
					value.add(cTemp);
					censusMap.put(cTemp.getGroup(), value);
				}
			}
			
			System.out.println("StartDate :"+unifiedCampaignDTO.getPEndDate()+" and name :"+unifiedCampaignDTO.getPName());
		
			
		}catch(Exception e){
			log.severe("Exception in initEditCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
		
	}
	
	
	public String deleteCampaign(){
		log.info("In deleteCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				deleted = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Archived.ordinal()+"", sessionDTO.getUserId());
			}
		}catch(Exception e){
			log.severe("Exception in deleteCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	/*public String savePlacement(){
		log.info("In savePlacement method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			long campaignId =unifiedCampaignDTO.getId();
			unifiedCampaignDTO.setStatusId("edit");
			isSave = service.savePlacement( unifiedCampaignDTO,campaignId);
	}catch(Exception e){
		log.severe("Exception in savePlacement method of SmartCampaignPlannerAction"+e.getMessage());
		e.printStackTrace();
		return Action.ERROR;
	}
		return Action.SUCCESS;
  }*/
	
	public String deletePlacement(){
		log.info("In deletePlacement method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			String placementId = request.getParameter("placementId").trim();
			String campaignId = request.getParameter("campaignId").trim();
			deleted = service.deletePlacement(campaignId, placementId, sessionDTO.getUserId());
			if(deleted){
				if(campaignId!=null){
					unifiedCampaignDTO.setId(Long.parseLong(campaignId));
					unifiedCampaignDTO.setStatusId("edit");
					unifiedCampaignDTO = service.initEditCampaign(unifiedCampaignDTO,0, sessionDTO.getUserId());
				}
				//TODO :deactivate smart media plan
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				SmartMediaPlanObj smartMediaPlan=productService.loadSmartMediaPlan(campaignId);
				if(smartMediaPlan !=null){
					smartMediaPlan.setActive(0);
					productService.saveSmartMediaPlan(smartMediaPlan);
					log.info("Smart Media Plan has been deactivated for this campaign with campaignId : "+campaignId);
				}
				
				
			}else{
				return Action.ERROR;
			}
		}catch(Exception e){
			log.severe("Exception in deletePlacement method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * Creates copy of the placement.
	 */
	public String copyPlacement(){
		log.info("In copyPlacement method of SmartCampaignPlannerAction ..");
		try{
			boolean isPlacementCopied = false;
			jsonObject = new JSONObject();
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO == null) {
				jsonObject.put("isInSession", false);
			}
			else if(!isAuthorised(sessionDTO)) {
				log.info("UnAuthorised access");
				jsonObject.put("isAuthorised", false);
			}else {
				String idOfPlacementToCopy = request.getParameter("idOfPlacementToCopy").trim();
				String campaignId = request.getParameter("campaignId").trim();
				String placementName = request.getParameter("placementName").trim();
				log.info("campaignId : "+campaignId+", idOfPlacementToCopy : "+idOfPlacementToCopy+", placementName : "+placementName);
				if(campaignId != null && campaignId.trim().length() > 0 && placementName != null && placementName.trim().length() > 0 && idOfPlacementToCopy != null && idOfPlacementToCopy.trim().length() > 0) {
					ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					isPlacementCopied = service.createCopyOfPlacement(campaignId.trim(), StringUtil.getLongValue(idOfPlacementToCopy.trim()), placementName.trim());
					jsonObject.put("isPlacementCopied", isPlacementCopied);
				}else {
					jsonObject.put("error", "Missing Information");
				}
			}
		}catch(Exception e){
			log.severe("Exception in copyPlacement method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			jsonObject.put("error", e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * Checks if placement name already exists. 
	 */
	public String checkPlacementNameAvailability(){
		log.info("In checkPlacementNameAvailability method of SmartCampaignPlannerAction ..");
		try{
			boolean isPlacementNameAvailable = false;
			jsonObject = new JSONObject();
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO == null) {
				jsonObject.put("isInSession", false);
			}
			else if(!isAuthorised(sessionDTO)) {
				log.info("UnAuthorised access");
				jsonObject.put("isAuthorised", false);
			}else {
				String campaignId = request.getParameter("campaignId").trim();
				String placementNameToCheck = request.getParameter("placementNameToCheck").trim();
				log.info("campaignId : "+campaignId+", placementNameToCheck : "+placementNameToCheck);
				if(campaignId != null && campaignId.trim().length() > 0 && placementNameToCheck != null && placementNameToCheck.trim().length() > 0) {
					ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					isPlacementNameAvailable = service.isPlacementNameAvailable(campaignId.trim(), placementNameToCheck.trim());
					jsonObject.put("isPlacementNameAvailable", isPlacementNameAvailable);
				}else {
					jsonObject.put("error", "Missing Information");
				}
			}
		}catch(Exception e){
			log.severe("Exception in checkPlacementNameAvailability method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			jsonObject.put("error", e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	public String unarchiveCampaign(){
		log.info("In unarchivePlacement method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				unarchived = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Draft.ordinal()+"", sessionDTO.getUserId());
			}
		}catch(Exception e){
			log.severe("Exception in unarchivePlacement method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	
	public String addAdvertiser(){
		log.info("In addAdvertiser method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			
			if(sessionDTO !=null ){
				String name = request.getParameter("name").trim();
				String address = request.getParameter("address");
				String phone = request.getParameter("phone").trim();
				String fax = request.getParameter("fax").trim();
				String email = request.getParameter("email").trim();
				String zip = request.getParameter("zip").trim();
				String accountType=LinMobileConstants.ADVERTISER_ID_PREFIX;
				String dfpNetworkCode=LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE;
				
				advertisersObj = service.addAdvertiser(name, address, phone, fax, email, zip,sessionDTO.getUserId());
			    if(advertisersObj!=null){
					return Action.SUCCESS;
				}else{
					return Action.ERROR;
				}
				
			}else{
				throw new Exception("Invalid user or session not exist..");
				
			}
			
		
		}catch(Exception e){
			log.severe("Exception in addAdvertiser method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		
		
	}
	
	/*
	 * Not used right now
	 * Under development, need for query
	 */
	public String addAdvertiserAccount(){
		log.info("In addAdvertiserAccount method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			
			if(sessionDTO !=null ){
				String name = request.getParameter("name").trim();
				String address = request.getParameter("address");
				String phone = request.getParameter("phone").trim();
				String fax = request.getParameter("fax").trim();
				String email = request.getParameter("email").trim();
				String zip = request.getParameter("zip").trim();
				String accountType=LinMobileConstants.ADVERTISER_ID_PREFIX;
				String dfpNetworkCode=LinMobileConstants.LIN_MOBILE_DFP_NETWORK_CODE;
				
				IUserService userService=(IUserService) BusinessServiceLocator.locate(IUserService.class);
				List<CompanyObj> userCompanyList=userService.getSelectedCompaniesByUserId(sessionDTO.getUserId());
				if(userCompanyList!=null && userCompanyList.size()>0){
					CompanyObj companyObj=null;
					if(userCompanyList.size()>1){
						for(CompanyObj company:userCompanyList){
							if(company.getAdServerId().contains(dfpNetworkCode)){
								companyObj=company;
								break;
							}
						}
					}
					
					if(companyObj==null){
						//Assign first company if , there is only one company to this user 
						// or all the companies assigned to this user don't have company with LIN_MOBILE_DFP_NETWORK_CODE
						companyObj=userCompanyList.get(0);
					}
						
					
					// Right now we are setting up on LIN Mobile new DFP....
					
					
					
					if(companyObj==null || dfpNetworkCode==null){
						companyObj=userCompanyList.get(0);						
					}
					
					//advertisersObj = service.addAdvertiser(name, address, phone, fax, email, zip,sessionDTO.getUserId(),accountType);
					
					accountObj = service.addAccount(name, address,accountType, phone, fax, email, zip,sessionDTO.getUserId(),dfpNetworkCode,companyObj);
					if(accountObj!=null){
						return Action.SUCCESS;
					}else{
						return Action.ERROR;
					}
				}else{
					throw new Exception("No company exist for this user, please assign a company first to this user.");
				}
				
			}else{
				throw new Exception("Invalid user or session not exist..");
				
			}
			
			
			}catch(Exception e){
				log.severe("Exception in addAdvertiser method of SmartCampaignPlannerAction"+e.getMessage());
				e.printStackTrace();
				return Action.ERROR;
			}		
	}
	
	public String addAgency(){
		log.info("In addAdvertiser method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			String name = request.getParameter("name").trim();
			String address = request.getParameter("address");
			String phone = request.getParameter("phone").trim();
			String fax = request.getParameter("fax").trim();
			String email = request.getParameter("email").trim();
			String zip = request.getParameter("zip").trim();
			agenciesObj = service.addAgency(name, address, phone, fax, email, zip,sessionDTO.getUserId());
			if(agenciesObj!=null){
				return Action.SUCCESS;
			}else{
				return Action.ERROR;
			}
			
			}catch(Exception e){
				log.severe("Exception in addAgency method of SmartCampaignPlannerAction"+e.getMessage());
				e.printStackTrace();
				return Action.ERROR;
			}
	}
	
	public String checkCampaignInProgress(){
		String campaignId=request.getParameter("campaignId");
		log.info("loadCampaignProgress action starts....campaignId : "+campaignId);
		
		if(campaignId !=null && campaignId.trim().length()>0 && LinMobileUtil.isNumeric(campaignId)){
			ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			SmartCampaignObj campaign=service.loadSmartCampaign(campaignId);
			if(campaign!=null){
				status=""+campaign.isHasMediaPlan();
			}else{
				status="error - No campaign found for campaignId :"+campaignId;
			}
		}else{
			status="error - Invalid campaignId:"+campaignId;
		}
		
		log.info("status --"+status);
		return Action.SUCCESS;
	}
	
	public String updateCampaignDetailFromDFP() {
		String adServerId=request.getParameter("adServerId");
		log.info("adServerId : "+adServerId);
		if(adServerId != null && adServerId.trim().length()>0) {
			ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			status = service.updateCampaignDetailFromDFP(adServerId);
		}else{
			log.warning("Invalid parameters");			
		}
		return Action.SUCCESS;
	}
	
	public String updatePlacementDetailFromDFP() {
		String adServerId=request.getParameter("adServerId");
		log.info("adServerId : "+adServerId);
		if(adServerId != null && adServerId.trim().length()>0) {
			ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			status = service.updatePlacementDetailFromDFP(adServerId);
		}else{
			log.warning("Invalid parameters");			
		}
		return Action.SUCCESS;
	}
	
	public String updateAllCampaignFromDFP() {
		log.info("In updateAllCampaignFromDFP");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		status = service.updateAllCampaignFromDFP();
		return Action.SUCCESS;
	}
	
	public String pauseCampaign(){
		log.info("In pauseCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				paused = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Paused.ordinal()+"", sessionDTO.getUserId());
			}
		}catch(Exception e){
			log.severe("Exception in pauseCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String resumeCampaign(){
		log.info("In resumeCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				resumed = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Running.ordinal()+"", sessionDTO.getUserId());
			}
		}catch(Exception e){
			log.severe("Exception in resumeCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String cancelCampaign(){
		log.info("In cancelCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				canceled = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Canceled.ordinal()+"", sessionDTO.getUserId());
			}
		}catch(Exception e){
			log.severe("Exception in cancelCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String unCancelCampaign(){
		log.info("In unCancelCampaign method of SmartCampaignPlannerAction ..");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				String campaignId = request.getParameter("campaignId").trim();
				unCanceled = service.changeCampaignStatus(campaignId,CampaignStatusEnum.Running.ordinal()+"", sessionDTO.getUserId());
			} else {
				log.warning("Session is null");
			}
		}catch(Exception e){
			log.severe("Exception in unCancelCampaign method of SmartCampaignPlannerAction"+e.getMessage());
			e.printStackTrace();
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String loadCampaignHistory() {
		try {
			jsonObject=new JSONObject();
			String campId=request.getParameter("campaignId");
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			log.info("In loadCampaignHistory... campaignId :"+campId+", limit :"+limit+", offset :"+offset);
			long campaignId = StringUtil.getLongValue(campId);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO != null) {
				if(campaignId>0){
					 ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					 jsonObject = campaignPlannerService.loadCampaignHistory(campaignId, StringUtil.getLongValue(limit), StringUtil.getLongValue(offset));
				}else{
					log.warning("Invalid campaignId :"+campId);
					jsonObject.put("campaignHistoryData", new JSONArray());
				}
			} else {
				log.warning("Session is null");
			}
		} catch(Exception e) {
			log.severe("Exception in loadCampaignHistory : "+e.getMessage());
			e.printStackTrace();
			jsonObject.put("campaignHistoryData", new JSONArray());
		}
		return Action.SUCCESS;
	}
	
	public String createOrUpdateCampaignObjFromDFP(){
		log.info("In createOrUpdateCampaignObjFromDFP method of SmartCampaignPlannerAction ..");
		Map<String,CompanyObj> uniqueDFPNetworkMap = new HashMap<String, CompanyObj>();
		 CompanyObj companyObj = new CompanyObj();
		 List<CompanyObj> companyList = new ArrayList<>();
		try{
			String dfpNetworkCode = request.getParameter("dfpNetworkCode");
			String hours = request.getParameter("hours");
			ISmartCampaignPlannerService service = (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
			if(dfpNetworkCode!=null && dfpNetworkCode.length()>0){
				companyObj = service.getCompanyByDFPNetworkCode(dfpNetworkCode);
				if(companyObj!=null){
					companyList.add(companyObj);
					uniqueDFPNetworkMap = service.getUniqueDFPNetworkCode(companyList);
					boolean updated=service.createOrUpdateCampaignObjFromDFP(uniqueDFPNetworkMap, hours);
					status="Campaigns updated : "+updated;
				}
			}else{
				companyList = service.getAllCompanyList();
				uniqueDFPNetworkMap = service.getUniqueDFPNetworkCode(companyList);
				boolean updated=service.createOrUpdateCampaignObjFromDFP(uniqueDFPNetworkMap, hours);
				status="Campaigns updated : "+updated;
			}
				
		}catch(Exception e){
			log.severe("Exception in createOrUpdateCampaignObjFromDFP : "+e.getMessage());
			status="Campaigns updated : exception : "+e.getMessage();
			e.printStackTrace();
			
		}
		return Action.SUCCESS;
	}
	
	public String getAllProductsForcast(){
		log.info("inside getAlProductsForcast...");
		IForecastInventoryService forecastService=new ForecastInventoryService();
		try{
			forecastService.addTaskToGetProductForcast();
		}catch(Exception e){
			log.severe("Exception in getAlProductsForcast" + e.getMessage());
		}
		return Action.SUCCESS;
	}
	
	public String getProductForcast(){
		log.info("inside getProductForcast...");
		String productId = request.getParameter("productId");
		String dmaId = request.getParameter("dmaId");
		String dmaName = request.getParameter("dmaName");
		IForecastInventoryService forecastDFPService = new ForecastInventoryService();
		try{
			boolean status =  forecastDFPService.setProductFrocast(productId, dmaId, dmaName);
			if(status){
				log.info("forcast added for product with Id :"+productId+" and DMA Id = "+dmaId);
			}else{
				log.warning("fail to add forcast for product with Id :"+productId+" and DMA Id = "+dmaId);
			}
		}catch(Exception e){
			log.severe("Exception in getProductForcast" + e.getMessage());
			return Action.ERROR;
		}
		return Action.SUCCESS;
		
	}
	
	@Override
	public UserDetailsDTO getModel() {
		return userDetailsDTO;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
		}
	
	public Map<String, Object> getSession() {
		return session;
	}
	

	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}
	
	public void setServletResponse(HttpServletResponse response) {
		
	}
	
	public List<PlatformObj> getPlatformObjList() {
		return platformObjList;
	}

	public List<DeviceObj> getDeviceObjList() {
		return deviceObjList;
	}

	public void setDeviceObjList(List<DeviceObj> deviceObjList) {
		this.deviceObjList = deviceObjList;
	}

	public List<CreativeObj> getCreativeObjList() {
		return creativeObjList;
	}

	public void setCreativeObjList(List<CreativeObj> creativeObjList) {
		this.creativeObjList = creativeObjList;
	}
	public void setPlatformObjList(List<PlatformObj> platformObjList) {
		this.platformObjList = platformObjList;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public List<SmartCampaignObj> getCmapaignObjList() {
		return cmapaignObjList;
	}

	public void setCmapaignObjList(List<SmartCampaignObj> cmapaignObjList) {
		this.cmapaignObjList = cmapaignObjList;
	}

	public UnifiedCampaignDTO getUnifiedCampaignDTO() {
		return unifiedCampaignDTO;
	}

	public void setUnifiedCampaignDTO(UnifiedCampaignDTO unifiedCampaignDTO) {
		this.unifiedCampaignDTO = unifiedCampaignDTO;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public AdvertiserObj getAdvertisersObj() {
		return advertisersObj;
	}

	public void setAdvertisersObj(AdvertiserObj advertisersObj) {
		this.advertisersObj = advertisersObj;
	}

	public AgencyObj getAgenciesObj() {
		return agenciesObj;
	}

	public void setAgenciesObj(AgencyObj agenciesObj) {
		this.agenciesObj = agenciesObj;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public boolean isUnarchived() {
		return unarchived;
	}

	public void setUnarchived(boolean unarchived) {
		this.unarchived = unarchived;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isResumed() {
		return resumed;
	}

	public void setResumed(boolean resumed) {
		this.resumed = resumed;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isUnCanceled() {
		return unCanceled;
	}

	public void setUnCanceled(boolean unCanceled) {
		this.unCanceled = unCanceled;
	}

	
	public void syncAccountAdertiserAgencies() {
		 ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
		 ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		 try {
			List<AdvertiserObj> advertisers = dao.getAllAdvertiserByAdServerId(null);
			List<AgencyObj> agencies = dao.getAllAgencyByAdServerId(null);
			List<AccountsEntity> accounts = dao.getAllAccounts();
			
			for(AdvertiserObj advertiser: advertisers){
				boolean found = false;
				for(AccountsEntity accountsEntity: accounts){
					
					if(accountsEntity.getAccountDfpId().equals(advertiser.getAdvertiserId())){
						accountsEntity.setContactPersonName(advertiser.getContactPersonName());
						accountsEntity.setAddress(advertiser.getAddress());
						accountsEntity.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
						dao.saveObject(accountsEntity);
						found = true;
						break;
					}
				}
				if(!found){
					
					CompanyObj company = service.getCompanyByDFPNetworkCode(advertiser.getDfpNetworkCode());
					AccountsEntity newAccount = getAccount(advertiser, null);
					newAccount.setId(advertiser.getDfpNetworkCode()+"_"+company.getAdServerUsername()+"_"+advertiser.getAdvertiserId()+"_"+company.getId());
					dao.saveObject(newAccount);
				}
			}
			
			for(AgencyObj agency: agencies){
				boolean found = false;
				for(AccountsEntity accountsEntity: accounts){
					if(accountsEntity.getAccountDfpId().equals(agency.getAgencyId())){
						accountsEntity.setContactPersonName(agency.getContactPersonName());
						accountsEntity.setAddress(agency.getAddress());
						accountsEntity.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
						dao.saveObject(accountsEntity);
						found = true;
						break;
					}
				}
				if(!found){
					
					CompanyObj company = service.getCompanyByDFPNetworkCode(agency.getDfpNetworkCode());
					AccountsEntity newAccount = getAccount(null, agency);
					newAccount.setId(agency.getDfpNetworkCode()+"_"+company.getAdServerUsername()+"_"+agency.getAgencyId()+"_"+company.getId());
					dao.saveObject(newAccount);
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		}
		private AccountsEntity getAccount(AdvertiserObj advertiser, AgencyObj agency){
			if(advertiser != null){
			AccountsEntity accountsEntity = new AccountsEntity();
			accountsEntity.setAccountDfpId(advertiser.getAdvertiserId()+"");
			accountsEntity.setAccountName(advertiser.getName());
			accountsEntity.setAccountType(LinMobileConstants.ADVERTISER_ID_PREFIX);
			accountsEntity.setAddress(advertiser.getAddress());
			accountsEntity.setAdServerId(advertiser.getDfpNetworkCode());
			//accountsEntity.setAdServerUserName(advertiser.);
			//accountsEntity.setCompanyId(advertiser.get);
			accountsEntity.setContactPersonName(advertiser.getContactPersonName());
		//	accountsEntity.setCreatedByUserId(advertiser.getCreatedBy());
			accountsEntity.setCreationDate(new Date());
		//	accountsEntity.setDfpAccountName(advertiser.get);
			accountsEntity.setEmail(advertiser.getEmail());
			accountsEntity.setPhone(advertiser.getPhone());
			accountsEntity.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
			return accountsEntity;
			}else if(agency != null){
				AccountsEntity accountsEntity = new AccountsEntity();
				accountsEntity.setAccountDfpId(agency.getAgencyId()+"");
				accountsEntity.setAccountName(agency.getName());
				accountsEntity.setAccountType(LinMobileConstants.AGENCY_ID_PREFIX);
				accountsEntity.setAddress(agency.getAddress());
				accountsEntity.setAdServerId(agency.getDfpNetworkCode());
				//accountsEntity.setAdServerUserName(advertiser.);
				//accountsEntity.setCompanyId(advertiser.get);
				accountsEntity.setContactPersonName(agency.getContactPersonName());
			//	accountsEntity.setCreatedByUserId(advertiser.getCreatedBy());
				accountsEntity.setCreationDate(new Date());
			//	accountsEntity.setDfpAccountName(advertiser.get);
				accountsEntity.setEmail(agency.getEmail());
				accountsEntity.setPhone(agency.getPhone());
				accountsEntity.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
				return accountsEntity;
				
			}
			return null;
		}
		
		
		/*
		 * 
		 * @author Anup Dutta
		 * @description This method except request in GET with 
		 * param as censusconfig as JSON value
		 * @return get rank of location based on census option
		 * 
		 **/
		public String getLocationRank(){
			try{
				jsonObject=new JSONObject();
				String jsonString = request.getParameter("censusconfig");
				JSONObject mapData = (JSONObject)JSONSerializer.toJSON(jsonString);

				int rankRatio = mapData.getInt("ratio");
				String rankBy = mapData.getString("rankBy");
				String gender = mapData.getString("gender");
				JSONArray censusArry = mapData.getJSONArray("census");
				
				IPerformanceMonitoringService plannerService = (IPerformanceMonitoringService) BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
				jsonArray = plannerService.getLocationRank(rankRatio,rankBy,censusArry,gender);
				
			}catch(Exception e){
				log.info("inside getLocationRank" + e.getMessage());
			}
			return Action.SUCCESS;
		}
		
		/*
		 * 
		 * @author Anup Dutta
		 * @description This method except request in GET with 
		 * param as productConfig as JSON value
		 * @return performance of product based on product and group
		 * 
		 **/
		public String productPerfomance(){
			try{
				jsonObject=new JSONObject();
				String jsonString = request.getParameter("productconfig");
				JSONObject mapData = (JSONObject)JSONSerializer.toJSON(jsonString);
				
				String groupBy = mapData.getString("groupBy");
				JSONObject productProperty = mapData.getJSONObject("productProperty");
				
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				jsonArray = productService.getLocationProductInfo(groupBy, productProperty);
				
			}catch(Exception e){
				log.info("inside getLocationRank" + e.getMessage());
			}
			return Action.SUCCESS;
		}
		
		public String productForecastPerfomance(){
			try{
				jsonObject=new JSONObject();
				String groupBy = request.getParameter("groupby");
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				jsonArray = productService.getProductForecastPerformance(groupBy);
			}catch(Exception e){
				log.info("Inside Product Forecast : " + e.getMessage());
			}
			return Action.SUCCESS;
		}

		public Map<String, List<CensusDTO>> getCensusMap() {
			return censusMap;
		}

		public void setCensusMap(Map<String, List<CensusDTO>> censusMap) {
			this.censusMap = censusMap;
		}
		
}
