package com.lin.web.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.server.bean.CityObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.IProductService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.service.impl.ProductService;
import com.lin.web.util.CSVReaderUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class ProductAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	private static final Logger log = Logger.getLogger(ProductAction.class.getName());
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private SessionObjectDTO sessionDTO;
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private String linCSV;	
	private String linCSVFileName;
	private String entityType;
	private long productId = 0;
	private String adServerId;
	private String partnerId;

	private String productStatus = "update";
	private List<ProductsObj> productsObjList;
	
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private boolean deleted;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	public String allOptionId = ProductService.allOptionId;
	public String allOption = ProductService.allOption;
	public String noneOptionId = ProductService.noneOptionId;
	public String noneOption = ProductService.noneOption;
	
	private String linStatus;
	
	public String publisherViewPageName = (LinMobileConstants.APP_VIEWS[0])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String advertiserViewPageName = (LinMobileConstants.APP_VIEWS[1])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String unifiedCampaign = (LinMobileConstants.APP_VIEWS[2])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String newsAndResearchPageName = (LinMobileConstants.APP_VIEWS[3])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String adminPageName = (LinMobileConstants.APP_VIEWS[4])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mySettingPageName = LinMobileConstants.APP_VIEWS[5]
			.split(LinMobileConstants.ARRAY_SPLITTER)[1].trim();
	public String poolPageName = (LinMobileConstants.APP_VIEWS[6])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String campaignPerformancePageName = (LinMobileConstants.APP_VIEWS[7])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mapEngineName = (LinMobileConstants.APP_VIEWS[8])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String report = (LinMobileConstants.APP_VIEWS[9])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	
	public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[4]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[4]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}
	 	}
		catch (Exception e) {
			
			log.severe("Exception in execution of isAuthorised : " + e.getMessage());
		}
		return false;
	}
	
	public String initProduct() {
		log.info("initProduct.. "+new Date());
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			log.info("action productId : "+productId+", productStatus : "+productStatus+", adServerId : "+adServerId);
			String result = productService.initCreateProduct(userDetailsDTO, sessionDTO.getUserId(), sessionDTO.isSuperAdmin(), productId);
			String[] str = result.split(":");
			productStatus = str[0];
			productId = Long.valueOf(str[1]);
    	}
    	catch (Exception e) {
    		log.severe("Exception in initProduct of ProductAction : "+e.getMessage());
    		
    		return "unAuthorisedAccess";
		}
		return Action.SUCCESS;
	}
	
	public String productSetup() {
		log.info("productSetup.. "+new Date());
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			//productsObjList = productService.productSetup(sessionDTO.getUserId(), sessionDTO.isSuperAdmin());
    	}
    	catch (Exception e) {
    		log.severe("Exception in productSetup of ProductAction : "+e.getMessage());
    		
    		return "unAuthorisedAccess";
		}
		return Action.SUCCESS;
	}
	
	public String loadAllProducts() {
		log.info("loadAllProducts starts.. "+new Date());
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		int productsPerPage = 25;
		int pageNumber = 1;
		try{
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(!isAuthorised(sessionDTO)) {
				return Action.ERROR;
			}
			String limit=request.getParameter("limit");
			String offset=request.getParameter("offset");
			String searchKeyword=request.getParameter("searchKeyword");
			log.info("limit : "+limit+", offset : "+offset+", searchKeyword : "+searchKeyword);
			if(limit != null && (LinMobileUtil.isNumeric(limit))) {
				productsPerPage = Integer.parseInt(limit);
				if(productsPerPage <= 0) {
					productsPerPage = 20;
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
			jsonArray = productService.loadAllProducts(sessionDTO.getUserId(), sessionDTO.isSuperAdmin(), productsPerPage, pageNumber, searchKeyword);
    	}
    	catch (Exception e) {
    		log.severe("Exception in loadAllProducts of ProductAction : "+e.getMessage());
    		
    		return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String initEditProduct() {
		log.info("initEditProduct starts.. "+new Date());
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			jsonObject = productService.initEditProduct(productId, partnerId);
    	}
    	catch (Exception e) {
    		log.severe("Exception in initEditProduct of ProductAction : "+e.getMessage());
    		
    		return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String deleteProduct() {
		log.info("deleteProduct starts.. "+new Date());
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
			/*String productIdToDelete = request.getParameter("productId");
			String adServerId = request.getParameter("adServerId");
			if(LinMobileUtil.isNumeric(productIdToDelete)) {*/
				deleted = productService.deleteProduct(productId, partnerId);
			/*}
			else {
				log.info("productId not numeric : "+productIdToDelete);
				return Action.ERROR;
			}*/
    	}
    	catch (Exception e) {
    		log.severe("Exception in deleteProduct of ProductAction : "+e.getMessage());
    		
    		return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	
	/*
	 * @author Naresh Pokhriyal
	 * uploadProductEntities action which will upload 
	 * data for Product for Publishers in datastore
	 */
	public String uploadProductEntities(){		
		log.info("linCSVFileName:"+linCSVFileName);
		log.info("entityType:"+entityType);
		String status = "";
		if(linCSVFileName !=null){
			if(entityType !=null &&  (!entityType.equals("-1"))){
				if(entityType.equalsIgnoreCase("Contextual Categories")){
					status = CSVReaderUtil.uploadContextualCategories(linCSV);
				}else{
					linStatus="Error: Invalid entity type.";
				}
			}else{
				status= "Failed to upload csv file , please select an entity type also:"+linCSVFileName;
			}
						
		}else{
			status= "Failed to upload csv file :"+linCSVFileName;
		}		
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}
	
	
	public String loadAllActiveAdUnits() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			linStatus = productService.loadAllActiveAdUnits(adServerId, adServerUsername);
		}catch (Exception e) {
			log.severe("Exception in loadAllActiveAdUnits of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
   }
	
	public String loadTopLevelAdUnits(){
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(adServerId != null && adServerUsername != null) {
				jsonArray = productService.loadTopLevelAdUnits(adServerId, adServerUsername, sessionDTO.getUserId());
			}
			
		}catch (Exception e) {
			log.severe("Exception in loadTopLevelAdUnits of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
   }
	
	
	public String loadChildAdUnits() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			String adServerId = request.getParameter("adServerId");
			String adServerUsername = request.getParameter("adServerUsername");
			String parentId = request.getParameter("parentId");
			String parentFullPath = request.getParameter("parentName");
			if(adServerId != null && adServerUsername != null && parentId != null) {
				jsonArray = productService.loadChildAdUnits(adServerId, adServerUsername, parentId, parentFullPath, sessionDTO.getUserId());
			}
		}catch (Exception e) {
			log.severe("Exception in loadChildAdUnits of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
   }
	
	
	public String loadContextualConsistencies() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			jsonArray = productService.getContextualConsistencies();
		}catch (Exception e) {
			log.severe("Exception in loadContextualConsistencies of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
	
	public String loadProductCreatives() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			jsonArray = productService.getProductCreatives();
		}catch (Exception e) {
			log.severe("Exception in loadProductCreatives of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
	
	public String loadProductDevices() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			jsonArray = productService.getDevices();
		}catch (Exception e) {
			log.severe("Exception in loadProductDevices of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
	
	public String loadProductDmas() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			jsonArray = productService.getGeoTargets();
		}catch (Exception e) {
			log.severe("Exception in loadProductDmas of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
	
	public String loadProductPlatforms() {
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			jsonArray = productService.getPlatforms();
		}catch (Exception e) {
			log.severe("Exception in loadProductPlatforms of ProductAction : "+e.getMessage());
			
		}
		return Action.SUCCESS;
	}
	
	
	public String saveProductData(){
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		try {
			 String productdata=request.getParameter("productdata");
			 log.info("productdata : "+productdata);
			 if(productdata != null && productdata.trim().length() > 0) {
				 if(productService.saveProductData(productdata.trim())) {
					 return Action.SUCCESS;
				 }
			 }
		}catch (Exception e) {
			log.severe("Exception in saveProductData of ProductAction : "+e.getMessage());
			
		}
		return Action.ERROR;
   }
	

	public String searchProduct(){
	  	log.info("searchProduct action starts..");
	  	String adFormat=request.getParameter("adFormat");
	  	String size=request.getParameter("size");
	  	String contextCategories=request.getParameter("contextCategories");
	  	String contextSubCategories=request.getParameter("contextSubCategories");
	  	String dma=request.getParameter("dma");
	  	String state=request.getParameter("state");
	  	String city=request.getParameter("city");
	  	String zip=request.getParameter("zip");
	  	String device=request.getParameter("device");
	  	String platform=request.getParameter("platform");
	  	
	  	
	    IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
	    List<ProductsObj> productList=productService.searchProducts(adFormat, size,contextCategories,contextSubCategories,
	    		dma,state,city,zip,device,platform);
	  	jsonObject=productService.getProductJsonData(productList);
		return Action.SUCCESS;
	}
	
	public String loadStates(){
		String countryCode=request.getParameter("countryCode");
		jsonObject=new JSONObject();
		if(countryCode !=null){
			  IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			  List<StateObj> stateList=productService.loadAllStates(countryCode);
			  jsonObject=productService.loadStatesJSON(stateList);
			 
		}else{
			log.info("Invalid country code.");
			//jsonObject.put("error", "Invalid country code");
		}
		return Action.SUCCESS;
	}
	
	public String loadCities() {
		String stateCode=request.getParameter("stateCode");
		jsonObject=new JSONObject();
		if(stateCode !=null){
			  IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
			  List<CityObj> cityWithZipList=productService.loadAllCitiesZips(stateCode);
			  jsonObject=productService.loadCityZipJSON(cityWithZipList);
			 
		}else{
			log.info("Invalid state code.");
			//jsonObject.put("error", "Invalid state code :"+stateCode);
		}
		return Action.SUCCESS;
	}
	
	public String searchCities() {
		try {
			jsonObject=new JSONObject();
			String searchText=request.getParameter("searchText");
			if(searchText !=null && searchText.trim().length()>=2){
				 IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				 List<CityDTO> searchedCityList=productService.searchCity(searchText);
				 jsonObject=productService.searchCityJSON(searchedCityList);
			}else{
				//jsonObject.put("error", "Invalid searchText, it should have atleast 3 characters :"+searchText);
				log.info("Invalid searchText, it should have atleast 2 characters :"+searchText);
				jsonObject.put("cities", new JSONArray());
			}
		} catch(Exception e) {
			log.info("Exception in searchCities : "+e.getMessage());
			
			jsonObject.put("cities", new JSONArray());
		}
		return Action.SUCCESS;
	}
	
	public String searchZips() {
		try {
			jsonObject=new JSONObject();
			String searchText=request.getParameter("searchText");
			if(searchText !=null && searchText.trim().length()>=2 && LinMobileUtil.isNumeric(searchText)){
				 IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				 List<ZipDTO> searchedZipList=productService.searchZip(searchText);
				 jsonObject=productService.searchZipJSON(searchedZipList);
			}else{
				log.info("Invalid searchText, it must atleast be a 2 digit number :"+searchText);
				jsonObject.put("zips", new JSONArray());
			}
		} catch(Exception e) {
			log.info("Exception in searchZips : "+e.getMessage());
			
			jsonObject.put("zips", new JSONArray());
		}
		return Action.SUCCESS;
	}
	
	public String loadCreatives(){
		jsonObject=new JSONObject();
		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
		List<CreativeObj> creativeList=productService.loadAllCreatives();
		jsonObject=productService.creativeJSON(creativeList);		
		return Action.SUCCESS;
	}
	
	public String searchAdUnits() {
		try {
			jsonObject=new JSONObject();
			String searchText=request.getParameter("searchText");
			String adServerId=request.getParameter("adServerId");
			if(searchText !=null && searchText.trim().length()>=2 && adServerId!=null){
				 IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				 List<AdUnitDTO> searchList=productService.searchAdUnits(searchText, adServerId);
				 if(searchList ==null){
					 log.info("searchList...0");
				 }else{
					 log.info("searchList..."+searchList.size());
				 }
				 
				 jsonObject=productService.searchAdUnitsJSON(searchList);
			}else{
				log.info("Invalid searchText, it should have atleast 2 characters :"+searchText+" Or invalid adServerId:"+adServerId);
				//jsonObject.put("error", "Invalid searchText, it should have atleast 2 characters :"+searchText+" Or invalid adServerId:"+adServerId);
			}
			log.info("No exception...search ends..");
		} catch(Exception e) {
			log.severe("Exception in searchAdUnits : "+e.getMessage());			
			jsonObject.put("adUnits", new JSONArray());
		}finally{
			log.info("Search ends...jsonObject :"+jsonObject.toString());	
		}
		return Action.SUCCESS;
	}
	
	public String checkProductNameAvailability() {
		log.info("Inside checkProductNameAvailability in ProductAction");
    	try {
	    	String productName=request.getParameter("productName");
	    	String productId = request.getParameter("productId");
	    	//String productStatus = request.getParameter("productStatus");
	    	IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
	    	if(productName == null || productName.trim().length() == 0) {
	    		log.info("Product name is empty");
	    		return Action.ERROR;
	    	}
	    	ProductsObj productsObj = productService.getProductByName(productName.trim());
	    	if(productsObj == null || (productId != null && productId.trim().length() > 0 && productsObj.getProductId() > 0 && (productsObj.getProductId()+"").equals(productId.trim()))) {
	    		return Action.SUCCESS;
	    	}
	    	else {
	    		return Action.ERROR;
	    	}
    	} catch (Exception e) {
    		log.severe("Exception in checkProductNameAvailability in ProductAction : "+e.getMessage());
			
			return Action.ERROR;
		}
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.setResponse(response);
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.setRequest(request);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String getLinCSV() {
		return linCSV;
	}
	
	public void setLinCSV(String linCSV) {
		this.linCSV = linCSV;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}
	
	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}
 
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public String getLinStatus() {
		return linStatus;
	}
	
	public void setLinStatus(String linStatus) {
		this.linStatus = linStatus;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public List<ProductsObj> getProductsObjList() {
		return productsObjList;
	}

	public void setProductsObjList(List<ProductsObj> productsObjList) {
		this.productsObjList = productsObjList;
	}
	
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	
	
}