package com.lin.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.mapengine.CsvUpload;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.ForcastInventoryDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IMediaPlanService;
import com.lin.web.service.IPoolMapService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.CSVReaderUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.MemcacheUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

public class PoolMapAction implements ServletRequestAware,SessionAware,ModelDriven<UserDetailsDTO>{

	
	static final Logger log = Logger.getLogger(PoolMapAction.class.getName());
	
	private Map session;
	private HttpServletRequest request;
	
	private SessionObjectDTO sessionDTO;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
    private String accessToken;
    private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
    private String linCSVFileName;
    private String linCSV;	
    
	private ProposalDTO proposalDTO=new ProposalDTO();
	private Map<String,String> companyMap;
	private Map<String,String> advertiserMap;
	private Map<String,String> agencyMap;
	private Map<String,String> geoTargetMap;
	private Map<String,String> industryMap;
	private Map<String,String> campaignTypeMap;
	private Map<String,String> campaignStatusMap;
	private Map<String,String> kpiMap;
	private List<ForcastInventoryDTO> forcastInventoryDTOList = new ArrayList<>();
    public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	private String status;
	private UnifiedCampaignDTO unifiedCampaignDTO = new UnifiedCampaignDTO();
	
	private Map<String,List<CensusDTO>> censusMap;
	
	public boolean isAuthorised(SessionObjectDTO sessionDTO) {
		try {
			IUserService service =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			service.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
			service.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    		return true;
	    	}else if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[8]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[8]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
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
		log.info("PoolMap action executes..");
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    	}
	    	else {
	    		return "unAuthorisedAccess";
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in execution of Poolmap Action : "+e.getMessage());
    		e.printStackTrace();
    		return "unAuthorisedAccess";
		}
		//accessToken=validateAccessToken();
		//request.setAttribute("FusionAccessToken", accessToken);
		
		return Action.SUCCESS;
		
	}
	
	
	public String newPoolMap(){		
		log.info("newPoolMap action executes..");
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	
	    	String userId=sessionDTO.getUserId()+"";
	    	log.info("Load proposal data...userId:"+userId);
			
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[6]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    	}
	    	else {
	    		return "unAuthorisedAccess";
	    	}
	    	loadProposalMap();
    	}
    	catch (Exception e) {
    		log.severe("Exception in execution of Poolmap Action : "+e.getMessage());
    		e.printStackTrace();
    		return "unAuthorisedAccess";
		}
		
		return Action.SUCCESS;
		
	}
	
	public void loadProposalMap() throws Exception {
		System.out.println("Load proposal data.........");
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);		
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		
		List<DropdownDataObj> allDropdownDataObjList = MemcacheUtil.getAllDropdownDataObjList();
		companyMap=mediaPlanService.getAllCompaniesByUser(sessionDTO.getUserId());
		String companyId = "";
		for(String key : companyMap.keySet()) {
			companyId = key;
			log.info("selected companyId : "+companyId);
			break;
		}
		advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, true, false);
		log.info("getting agencies");
		agencyMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, false, true);
		log.info("getting gep targets");
		geoTargetMap=mediaPlanService.loadAllGeoTargets();
		industryMap=mediaPlanService.loadAllIndustry();
		campaignTypeMap = mediaPlanService.getAllCampaignTypes(allDropdownDataObjList);
		campaignStatusMap = mediaPlanService.getAllCampaignStatus(allDropdownDataObjList);
		kpiMap=mediaPlanService.loadAllKPIs();
	}
	
	public String forecastInventory(){
		log.info("forecastInventory action starts");		
		String lineItemIdStr=request.getParameter("lineItemId");
		try {
			dfpSession = DFPAuthenticationUtil.getDFPSession(LinMobileConstants.DFP_NETWORK_CODE, LinMobileConstants.DFP_APPLICATION_NAME);
			dfpServices = LinMobileProperties.getInstance().getDfpServices();			
			
			//InventoryForecastingService.getForecastingData(dfpServices, dfpSession);
			
			log.info("action ends .....");
			
		} catch (Exception e) {
			log.severe("forecastInventory:Exception:"+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
   private String validateAccessToken(){
		
		Entity credentials = null;
		try {
	    	log.info("Going to fetch credentials...");
	        DatastoreService datastore =DatastoreServiceFactory.getDatastoreService();
	        Key credsKey = KeyFactory.createKey("Credentials", "FusionTable");
	        credentials = datastore.get(credsKey);
	      
	        GoogleTokenResponse tokens = new GoogleTokenResponse();
		    tokens.setAccessToken((String) credentials.getProperty("accessToken"));
		    tokens.setExpiresInSeconds((Long) credentials.getProperty("expiresIn"));
		    tokens.setRefreshToken((String) credentials.getProperty("refreshToken"));
		    String clientId = (String) credentials.getProperty("clientId");
		    String clientSecret = (String) credentials.getProperty("clientSecret");
		    tokens.setScope(LinMobileConstants.FUSION_TABLE_SCOPE);
		    
		    HttpTransport httpTransport = new NetHttpTransport();
		    JsonFactory jsonFactory = new JacksonFactory();
		    
		    GoogleCredential credential=new GoogleCredential.Builder()			       
					.setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setClientSecrets(clientId,clientSecret)
					.addRefreshListener(new CredentialRefreshListener() {
				public void onTokenResponse(Credential credential,
						TokenResponse tokenResponse) {
					log.info("Credential was refreshed successfully: "+ tokenResponse.getAccessToken());
					accessToken=tokenResponse.getAccessToken();
					
				}
				public void onTokenErrorResponse(Credential credential,
						TokenErrorResponse tokenErrorResponse) {
					log.severe("Credential was not refreshed successfully. Redirect to error page or login screen.");
				}
			}).build();
		    credential.setFromTokenResponse(tokens);
			credential.refreshToken();
			
			if(accessToken !=null){
				tokens.setAccessToken(accessToken);
			}
			
	    } catch (EntityNotFoundException ex) {
	    	 log.severe("Please authorize using oauth2 first, EntityNotFoundException :"+ex.getMessage());
	    	 accessToken=null;
	         ex.printStackTrace();
	        
	    } catch (IOException e) {
			log.severe("IOException :"+e.getMessage());		
			accessToken=null;
			e.printStackTrace();				
		} 
		
		return accessToken;
	    
	}
	
   /*
	 * @author Shubham Goel
	 * uploadDFPSitesWithDMA action which will upload 
	 * DFP sites with DMA in datastore that are required for reporting
	 */
	public String uploadDFPSitesWithDMA(){		
		log.info("uploadDfpOrderIds action executes..csvFileName: "+linCSVFileName);
		if(linCSVFileName !=null){
			status=CSVReaderUtil.readCSVAndUploadDFPSitesWithDMA(linCSV);			
		}else{
			status= "Failed to upload csv file :"+linCSVFileName;
		}		
		request.setAttribute("status", status);
		return Action.SUCCESS;
	}
	
	public String loadAllDMAsWithInventory(){
		log.info("in loadAllDMAsWithInventory of PoolMapAction");
		String startDate = request.getParameter("startDate");
		String endtDate = request.getParameter("endtDate");
		try{
			IPoolMapService poolMapService = (IPoolMapService) BusinessServiceLocator.locate(IPoolMapService.class);
			forcastInventoryDTOList = poolMapService.loadAllDMAsWithInventory(startDate, endtDate);
		}catch(Exception e){
			log.severe("Exception in loadAllDMAsWithInventory() of PoolMapAction "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String loadAllocateInventry(){
		log.info("in loadAllocateInventry of PoolMapAction");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endtDate");
		String dmaCode = request.getParameter("dmaCode");
		try{
			IPoolMapService poolMapService = (IPoolMapService) BusinessServiceLocator.locate(IPoolMapService.class);
			forcastInventoryDTOList = poolMapService.loadAllocateInventry(startDate, endDate, dmaCode);
		}catch(Exception e){
			log.severe("Exception in loadAllocateInventry  of PoolMapAction "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String authenticateMap(){		
		log.info("authenticateMap action executes..");
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		ISmartCampaignPlannerService service =  (ISmartCampaignPlannerService) BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
		
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	
	    	unifiedCampaignDTO.setStatusId("create");
			unifiedCampaignDTO = service.initCampaign(unifiedCampaignDTO, sessionDTO.getUserId());
	    	System.out.println("context list :" + unifiedCampaignDTO.getContextList().size());
			if(!isAuthorised(sessionDTO)) {
				return "unAuthorisedAccess";
			}
			Credential credential = CsvUpload.authorize();
			if(credential != null && credential.getAccessToken() != null) {
				accessToken = credential.getAccessToken();
				if(accessToken != null) {
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
				}
			}
    	}
    	catch (Exception e) {
    		log.severe("Exception in execution of Poolmap Action : "+e.getMessage());
    		e.printStackTrace();
		}
		//accessToken=validateAccessToken();
		//request.setAttribute("FusionAccessToken", accessToken);
		return "unAuthorisedAccess";
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	@Override
	public UserDetailsDTO getModel() {		
		return userDetailsDTO;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}

		
	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}

	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}


	public Map<String,String> getKpiMap() {
		return kpiMap;
	}


	public void setKpiMap(Map<String,String> kpiMap) {
		this.kpiMap = kpiMap;
	}


	public Map<String,String> getCampaignStatusMap() {
		return campaignStatusMap;
	}


	public void setCampaignStatusMap(Map<String,String> campaignStatusMap) {
		this.campaignStatusMap = campaignStatusMap;
	}


	public Map<String,String> getCampaignTypeMap() {
		return campaignTypeMap;
	}


	public void setCampaignTypeMap(Map<String,String> campaignTypeMap) {
		this.campaignTypeMap = campaignTypeMap;
	}


	public Map<String,String> getIndustryMap() {
		return industryMap;
	}


	public void setIndustryMap(Map<String,String> industryMap) {
		this.industryMap = industryMap;
	}


	public Map<String,String> getGeoTargetMap() {
		return geoTargetMap;
	}


	public void setGeoTargetMap(Map<String,String> geoTargetMap) {
		this.geoTargetMap = geoTargetMap;
	}


	public Map<String,String> getAgencyMap() {
		return agencyMap;
	}


	public void setAgencyMap(Map<String,String> agencyMap) {
		this.agencyMap = agencyMap;
	}


	public Map<String,String> getAdvertiserMap() {
		return advertiserMap;
	}


	public void setAdvertiserMap(Map<String,String> advertiserMap) {
		this.advertiserMap = advertiserMap;
	}


	public Map<String,String> getCompanyMap() {
		return companyMap;
	}


	public void setCompanyMap(Map<String,String> companyMap) {
		this.companyMap = companyMap;
	}


	public ProposalDTO getProposalDTO() {
		return proposalDTO;
	}


	public void setProposalDTO(ProposalDTO proposalDTO) {
		this.proposalDTO = proposalDTO;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}

	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}
	
    public String getLinCSV() {
			return linCSV;
	}

	public void setLinCSV(String linCSV) {
        	this.linCSV = linCSV;
	}


	public List<ForcastInventoryDTO> getForcastInventoryDTOList() {
		return forcastInventoryDTOList;
	}


	public void setForcastInventoryDTOList(List<ForcastInventoryDTO> forcastInventoryDTOList) {
		this.forcastInventoryDTOList = forcastInventoryDTOList;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public UnifiedCampaignDTO getUnifiedCampaignDTO() {
		return unifiedCampaignDTO;
	}

	public void setUnifiedCampaignDTO(UnifiedCampaignDTO unifiedCampaignDTO) {
		this.unifiedCampaignDTO = unifiedCampaignDTO;
	}

	public Map<String, List<CensusDTO>> getCensusMap() {
		return censusMap;
	}

	public void setCensusMap(Map<String, List<CensusDTO>> censusMap) {
		this.censusMap = censusMap;
	}
	
	
}
