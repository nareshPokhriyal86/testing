package com.lin.web.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.KPIObj;
import com.lin.web.dto.ClientIOReportDTO;
import com.lin.web.dto.PlacementDTO;
import com.lin.web.dto.ProposalDTO;
import com.lin.web.dto.PublisherIOReportDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.dto.UserDetailsDTO;
import com.lin.web.service.IMediaPlanService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

public class ProposalAction implements ServletRequestAware,ServletResponseAware,SessionAware,ModelDriven<ProposalDTO>{

	
	static final Logger log = Logger.getLogger(ProposalAction.class.getName());
	
	private String reportsResponse;	
	private Map session;
	private HttpServletRequest request;
	private HttpServletResponse response; 
	
	private SessionObjectDTO sessionDTO;
	
	private ProposalDTO proposalDTO=new ProposalDTO();
	private UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
	
	private String linCSV;	
    private String linCSVContentType;	
	private String linCSVFileName;
	private List<ProposalDTO> proposalList;
	private Map<String,Integer> headerMap;
	private Map<String,String> companyMap;
	private Map<String,String> advertiserMap;
	private Map<String,String> agencyMap;
	private Map<String,String> geoTargetMap;
	private Map<String,String> industryMap;
	private Map<String,String> campaignTypeMap;
	private Map<String,String> campaignStatusMap;
	private Map<String,String> kpiMap;
	private List<String> salesRepList;
	
	private JSONObject placementMap;
	private String saveStatus;
	private InputStream inputStream;
	
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
	
	private String status;
	
	public String execute(){
		log.info("proposals action starts.. "+new Date());
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		IUserDetailsDAO userDetailsDAO=new UserDetailsDAO();
		String proposalId=request.getParameter("proposalId");
		String saveStatus = request.getParameter("sv");
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && 
	    			userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    	}
	    	else {
	    		return "unAuthorisedAccess";
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in execution of Proposal Action : "+e.getMessage());
    		e.printStackTrace();
    		return "unAuthorisedAccess";
		}
		String userId=sessionDTO.getUserId()+"";
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		if(proposalId == null){
			proposalList=mediaPlanService.loadAllProposals(userId);
			headerMap=new HashMap<String, Integer>();	
			return Action.SUCCESS;
		}else{
			try {
				List<DropdownDataObj> allDropdownDataObjList = MemcacheUtil.getAllDropdownDataObjList();
				proposalDTO=mediaPlanService.loadProposal(StringUtil.getLongValue(proposalId));
				proposalDTO.setShowTabs("yes");
				companyMap=mediaPlanService.getAllCompaniesByUser(sessionDTO.getUserId());
				/*String company=proposalDTO.getCompany();
				if(company !=null && LinMobileUtil.isNumeric(company)){
					proposalDTO.setCompany(companyMap.get(company));
				}
				String companyId="";
				for(String key:companyMap.keySet()){
					String value=companyMap.get(key);
					if(value.equals(company)){
						companyId=key;
						break;
					}
				}*/
				String companyId=proposalDTO.getCompany();
				proposalDTO.setCompany(companyId);
				
				/*String publisherIdsForBigQuery = userService.getPublisherIdsForBigQueryByCompanyId(companyId);
				StringBuilder publisherIds=new StringBuilder(publisherIdsForBigQuery);
				StringBuilder advertiserIds=new StringBuilder();
				StringBuilder agencyIds=new StringBuilder();
				
				userDetailsDAO.getAccountsInfoForBQ(sessionDTO.getUserId(), publisherIds, advertiserIds, agencyIds);
				
				advertiserMap=mediaPlanService.getAllAdvertiserFromDataStore();
				advertiserMap.putAll(userService.loadAdvertisersFromBigQuery(publisherIdsForBigQuery, advertiserIds.toString()));
				agencyMap=mediaPlanService.getAllAgenciesFromDataStore();
				agencyMap.putAll(userService.loadAgenciesFromBigQuery(publisherIdsForBigQuery, agencyIds.toString()));*/
				advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, true, false);
				agencyMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, false, true);
				geoTargetMap=mediaPlanService.loadAllGeoTargets();
				industryMap=mediaPlanService.loadAllIndustry();
				campaignTypeMap = mediaPlanService.getAllCampaignTypes(allDropdownDataObjList);
				campaignStatusMap = mediaPlanService.getAllCampaignStatus(allDropdownDataObjList);
				kpiMap=mediaPlanService.loadAllKPIs();
				
				proposalDTO.setAdvertiserId("_"+proposalDTO.getAdvertiser());
				proposalDTO.setAgencyId("_"+proposalDTO.getAgency());
				/*proposalDTO.setAdvertiser(advertiserMap.get("_"+proposalDTO.getAdvertiser()));
				proposalDTO.setAgency(agencyMap.get("_"+proposalDTO.getAgency()));*/
				log.info(proposalDTO.toString());
				
				placementMap = mediaPlanService.placementMap(proposalId);
				if(saveStatus != null && saveStatus.equals("c")) {
					setSaveStatus("completed");
				}
				else if(saveStatus != null && saveStatus.equals("f")) {
					setSaveStatus("failed");
				}
				log.info("proposals action ends.. "+new Date());
			}
			catch (Exception e) {
	    		log.severe("Exception in execute of Proposal Action : "+e.getMessage());
	    		e.printStackTrace();
			}
			return "edit";
		}
	}
	
	
	public String createProposal(){
		log.info("newProposal action..");
		IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		String proposalId=request.getParameter("proposalId");		
		log.info("proposalId :"+proposalId);	
		IUserService userService =(IUserService) BusinessServiceLocator.locate(IUserService.class);
		try{
	    	sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	userService.getAuthorisations(userDetailsDTO, sessionDTO.getRoleId());
	    	userService.getAuthorisedPagesForUser(sessionDTO.getRoleId(), sessionDTO.getUserId(), userDetailsDTO);
	    	if(userDetailsDTO.getAuthorisedPagesList() != null && userDetailsDTO.getAuthorisedPagesList().contains((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim())) {
	    		sessionDTO.setPageName((LinMobileConstants.APP_VIEWS[2]).split(LinMobileConstants.ARRAY_SPLITTER)[1].trim());
	    	}
	    	else {
	    		return "unAuthorisedAccess";
	    	}
    	}
    	catch (Exception e) {
    		log.severe("Exception in execution of Proposal Action : "+e.getMessage());
    		e.printStackTrace();
    		return "unAuthorisedAccess";
		}
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		if(sessionDTO !=null){
			if(proposalDTO !=null && proposalDTO.getProposalName()!=null){
				proposalDTO.setShowTabs("yes");
				/*String nextPageControl=proposalDTO.getNextPageControl();*/
				
				proposalDTO.setUpdatedBy(sessionDTO.getUserId()+"");
				proposalDTO.setProposalId(proposalId);
				/*if(proposalDTO.getCustomAdvertiser() !=null && (proposalDTO.getAdvertiser() !=null && proposalDTO.getAdvertiser().equals("0"))){
					AdvertisersObj obj=new AdvertisersObj();
					obj.setAdvertiserName(proposalDTO.getCustomAdvertiser());
					obj.setCreatedBy(String.valueOf(sessionDTO.getUserId()));
					long advertiserId=mediaPlanService.saveAdvertiser(obj);
					if(advertiserId >0){
						proposalDTO.setAdvertiser(String.valueOf(advertiserId));
					}else{
						proposalDTO.setAdvertiser(proposalDTO.getCustomAdvertiser());
					}
					proposalDTO.setAdvertiserId(String.valueOf(advertiserId));					
				}else{
					proposalDTO.setAdvertiserId(proposalDTO.getAdvertiser());
				}*/
				if(proposalDTO.getAdvertiser() != null && !proposalDTO.getAdvertiser().trim().equals("-1")) {
					proposalDTO.setAdvertiser(proposalDTO.getAdvertiser().replaceAll("_", "").trim());
				}
				
				/*if(proposalDTO.getCustomAgency() !=null && (proposalDTO.getAgency() !=null && proposalDTO.getAgency().equals("0"))){
					AgenciesObj obj=new AgenciesObj();
					obj.setAgencyName(proposalDTO.getCustomAgency());
					obj.setCreatedBy(String.valueOf(sessionDTO.getUserId()));
					long agencyId=mediaPlanService.saveAgency(obj);
					if(agencyId >0){
						proposalDTO.setAgency(String.valueOf(agencyId));
					}else{
						proposalDTO.setAgency(proposalDTO.getCustomAgency());
					}
					proposalDTO.setAgencyId(String.valueOf(agencyId));
				}else{
					proposalDTO.setAgencyId(proposalDTO.getAgency());
				}*/
				if(proposalDTO.getAgency() != null && !proposalDTO.getAgency().trim().equals("-1")) {
					proposalDTO.setAgency(proposalDTO.getAgency().replaceAll("_", "").trim());
				}
				else {
					proposalDTO.setAgency("");
				}
				
				String customIndustry=proposalDTO.getCustomIndustry();
				String industry=proposalDTO.getIndustry();		
				industry =removeText(industry,"0");
				if(customIndustry !=null && (customIndustry.trim().length()>0)){					
					
					IndustryObj obj=new IndustryObj();
					obj.setIndustryName(customIndustry);
					obj.setCreatedBy(String.valueOf(sessionDTO.getUserId()));
					long industryId=mediaPlanService.saveIndustry(obj);
								
					if(industry !=null && industry.trim().length()>0){
						industry=industry+","+customIndustry;
					}else{
						industry=customIndustry;
					}
					proposalDTO.setIndustry(industry);					
				}
				
				String customKpi=proposalDTO.getCustomKpi();
				String kpi=proposalDTO.getKpi();
				kpi =removeText(kpi,"0");
				if(customKpi !=null && 
						(customKpi.trim().length()>0)){					
					KPIObj obj=new KPIObj();
					obj.setKpiName(customKpi);	
					obj.setCreatedBy(String.valueOf(sessionDTO.getUserId()));
					long kpiId=mediaPlanService.saveKPI(obj);
					
					if(kpi !=null && kpi.trim().length()>0){					
						kpi=kpi+","+customKpi;
					}else{
						kpi=customKpi;
					}
					proposalDTO.setKpi(kpi);					
				}
				
				String customGeoTarget=proposalDTO.getCustomGeoTargets();
				String geoTargets=proposalDTO.getGeoTargets();
				geoTargets =removeText(geoTargets,"0");
				
				if(customGeoTarget !=null && 
						(customGeoTarget.trim().length()>0)){
					
					GeoTargetsObj obj=new GeoTargetsObj();
					obj.setGeoTargetsName(customGeoTarget);					
					long geoId=mediaPlanService.saveGeoTarget(obj);
					
					if(geoTargets !=null && geoTargets.trim().length()>0){						
						geoTargets=geoTargets+","+customGeoTarget;
					}else{
						geoTargets=customGeoTarget;
					}
					proposalDTO.setGeoTargets(geoTargets);					
				}
				
				log.info("Going to save : "+proposalDTO.toString());	
				
				boolean status=mediaPlanService.saveProposal(proposalDTO, sessionDTO.getUserId()+"");
				log.info("Proposal saved: "+status);
				try {
					log.info("Redirecting to proposals.lin..........................");
					if(proposalDTO.getFromPool()!=null && proposalDTO.getFromPool().equals("1")){
						response.sendRedirect("/proposals.lin");
					}else if(status) {
						response.sendRedirect("/proposals.lin?sv=c&proposalId="+proposalDTO.getProposalId());
					}
					else {
						response.sendRedirect("/proposals.lin?sv=f&proposalId="+proposalDTO.getProposalId());
					}
					return null;
					/*if(nextPageControl !=null && nextPageControl.equals("0")){
						log.info("Redirecting to proposals.lin..........................");						 
						response.sendRedirect("/proposals.lin");
						return null;
					}else if(nextPageControl !=null && nextPageControl.equals("1")){
						log.info("Redirecting to mediaPlanner.lin........................");
					    response.sendRedirect("/mediaPlanner.lin?proposalId="+proposalId);
					    return null;
					}*/
				}catch (IOException e) {
					log.severe(" IOException : "+e.getMessage());
					e.printStackTrace();
				}
			}else{
				log.info("no proposal data found....please fill form...");				
			}
			try {
				proposalDTO.setShowTabs("yes");
				List<DropdownDataObj> allDropdownDataObjList = MemcacheUtil.getAllDropdownDataObjList();
				companyMap=mediaPlanService.getAllCompaniesByUser(sessionDTO.getUserId());
				String companyId = "";
				for(String key : companyMap.keySet()) {
					companyId = key;
					log.info("selected companyId : "+companyId);
					break;
				}
				/*String publisherIdsForBigQuery = userService.getPublisherIdsForBigQueryByCompanyId(companyId);
				StringBuilder publisherIds=new StringBuilder(publisherIdsForBigQuery);
				StringBuilder advertiserIds=new StringBuilder();
				StringBuilder agencyIds=new StringBuilder();
				
				userDetailsDAO.getAccountsInfoForBQ(sessionDTO.getUserId(), publisherIds, advertiserIds, agencyIds);
				
				advertiserMap=mediaPlanService.getAllAdvertiserFromDataStore();
				advertiserMap.putAll(userService.loadAdvertisersFromBigQuery(publisherIdsForBigQuery, advertiserIds.toString()));
				agencyMap=mediaPlanService.getAllAgenciesFromDataStore();
				agencyMap.putAll(userService.loadAgenciesFromBigQuery(publisherIdsForBigQuery, agencyIds.toString()));*/
				advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, true, false);
				agencyMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), companyId, false, true);
				geoTargetMap=mediaPlanService.loadAllGeoTargets();
				industryMap=mediaPlanService.loadAllIndustry();
				campaignTypeMap = mediaPlanService.getAllCampaignTypes(allDropdownDataObjList);
				campaignStatusMap = mediaPlanService.getAllCampaignStatus(allDropdownDataObjList);
				kpiMap=mediaPlanService.loadAllKPIs();
				//salesRepList=mediaPlanService.loadSalesRepresentative(companyId, LinMobileConstants.USERS_ARRAY[2]);
				//log.info("salesRepList:"+salesRepList);
			}
	    	catch (Exception e) {
	    		log.severe("Exception in createProposal of Proposal Action : "+e.getMessage());
	    		e.printStackTrace();
			}
		}
		log.info("end of action...");
		return Action.SUCCESS;
	}
	
	
	/*
	 * Remove a text from a String containing comma separated texts
	 */
	private String removeText(String sourceStr,String textToRemove){
		String correctedStr="";
		if(sourceStr !=null){
			String [] sourceStrArray= sourceStr.split(",");			
			for(int i=0;i<sourceStrArray.length;i++){
				String element=sourceStrArray[i].trim();
				if(!element.equals(textToRemove)){
					if(i==0){
						correctedStr=element;
					}else{
						correctedStr=correctedStr+","+element;
					}							
				}
			}
		}else{
			correctedStr=sourceStr;
		}
		return correctedStr;
	}
	
	public String updateProposals(){
		String proposalId=request.getParameter("proposalId");
		log.info("update proposal :"+proposalId);
		if(proposalId !=null){
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");		
			if(sessionDTO !=null){			
				proposalDTO.setUpdatedBy(sessionDTO.getUserId()+"");
				proposalDTO.setProposalId(proposalId);
				log.info(proposalDTO.toString());
				IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
				boolean status=mediaPlanService.saveProposal(proposalDTO, sessionDTO.getUserId()+"");
				log.info("Proposal saved: "+status);
			}
		}else{
			log.info("Invalid proposalId..");
		}		
		return Action.SUCCESS;
	}
	
	public String uploadProposals(){
		log.info("upload proposal csv file name:"+linCSVFileName);
		if(linCSVFileName !=null){
			
    		request.setAttribute("status", reportsResponse);
		}else{
			reportsResponse="No csv file found to upload.";
			request.setAttribute("status", reportsResponse);
		}
		return Action.SUCCESS;
	}
	
	/*
	 * Auto save proposal action
	 * Incomplete........Need to work later.....
	 */
	public String autoSave(){		
		sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
		String proposalData=request.getParameter("proposalData");
		log.info("autoSaveProposal action..proposalData:"+proposalData);
		
		if(sessionDTO !=null){
			if(proposalData !=null){
				JSONObject proposalDetails = (JSONObject) JSONSerializer.toJSON(proposalData);		
				
		        JSONArray jsonArray=proposalDetails.getJSONArray("proposal");
		        if(jsonArray.size()==0){
		        	log.warning("No proposal to save..");        	
		        }else{        	      	
		        	for(int i=0;i<jsonArray.size();i++){
		    			JSONObject obj=jsonArray.getJSONObject(i);
		    			String proposalId=obj.getString("proposalId");		    			
		    			String proposalName=obj.getString("proposalName");		    			
		    			String updatedBy=obj.getString("updatedBy");
		    			String updatedOn=obj.getString("updatedOn");		    			
		    	    }
		        }
		        
				status="Success";
				
			}else{
				log.warning("Invalid proposalData");
				status="Failed";
			}
			
		}else{
			log.warning("Invalid user");
			status="Failed";
		}        
		return Action.SUCCESS;
	}
	
	public String loadAdvertisersByCompany() {
		log.info("Inside loadAdvertisersByCompany in ProposalAction");
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		/*IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		String publisherIdInBQ = "";*/
		try {
			String company=request.getParameter("company");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			/*if(company != null && company.trim().length() > 0) {
				publisherIdInBQ = userService.getPublisherIdsForBigQueryByCompanyId(company);
			}
			advertiserMap = mediaPlanService.getAllAdvertiserFromDataStore();
			if(advertiserMap == null) {
				advertiserMap = new LinkedHashMap<String, String>();
			}
			StringBuilder publisherIds=new StringBuilder(publisherIdInBQ);
			StringBuilder advertiserIds=new StringBuilder();
			StringBuilder agencyIds=new StringBuilder();
			
			userDetailsDAO.getAccountsInfoForBQ(sessionDTO.getUserId(), publisherIds, advertiserIds, agencyIds);
			advertiserMap.putAll(userService.loadAdvertisersFromBigQuery(publisherIdInBQ, advertiserIds.toString()));*/
			if(company != null && company.trim().length() > 0) {
				advertiserMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), company, true, false);
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadAdvertisersByCompany in ProposalAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String loadAgenciesByCompany() {
		log.info("Inside loadAgenciesByCompany in ProposalAction");
		IUserService userService =  (IUserService) BusinessServiceLocator.locate(IUserService.class);
		/*IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
		IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
		String publisherIdInBQ = "";*/
		try {
			String company=request.getParameter("company");
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			/*if(company != null && company.trim().length() > 0) {
				publisherIdInBQ = userService.getPublisherIdsForBigQueryByCompanyId(company);
			}
			agencyMap = mediaPlanService.getAllAgenciesFromDataStore();
			if(agencyMap == null) {
				agencyMap = new LinkedHashMap<String, String>();
			}
			StringBuilder publisherIds=new StringBuilder(publisherIdInBQ);
			StringBuilder advertiserIds=new StringBuilder();
			StringBuilder agencyIds=new StringBuilder();
			
			userDetailsDAO.getAccountsInfoForBQ(sessionDTO.getUserId(), publisherIds, advertiserIds, agencyIds);
			agencyMap.putAll(userService.loadAgenciesFromBigQuery(publisherIdInBQ, agencyIds.toString()));*/
			if(company != null && company.trim().length() > 0) {
				agencyMap = userService.getSelectedAccountsForCampaingnPlanning(sessionDTO.getUserId(), company, false, true);
			}
		}
		catch (Exception e) {
			log.severe("Exception in loadAgenciesByCompany in ProposalAction : "+e.getMessage());
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String clientIOReport(){
		 log.info("In clientIOReport() of ProposalAction");
		 List<ClientIOReportDTO> clientIOReportDTOList = new ArrayList<ClientIOReportDTO>();
		 ClientIOReportDTO clientIOReportDTO = null;
		 List<PlacementDTO> placementDTOList = new ArrayList<PlacementDTO>();
	    try{
	    	 sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
	    	 if(sessionDTO != null && sessionDTO.getUserId() > 0) {
		    	 String selectedRows=request.getParameter("selectedRows");
		    	 String proposalId=request.getParameter("proposalId");
				 log.info("selectedRows : "+selectedRows+",  proposalId : "+proposalId);	
				 if(proposalId != null && proposalId.trim().length() > 0 && selectedRows != null && selectedRows.trim().length() > 0) {
					 IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
					 clientIOReportDTO = mediaPlanService.clientIOReportObject(proposalId, sessionDTO.getUserId());
					 if(clientIOReportDTO != null) {
						 StringBuilder totalValue = new StringBuilder();
						 placementDTOList = mediaPlanService.getSelectedPlacementReportList(selectedRows, totalValue);
						 String[] tempStr = (totalValue.toString()).split("<SEP>");
						 clientIOReportDTO.setTotalImpressions(tempStr[1]);
						 clientIOReportDTO.setTotalECPM(tempStr[2]);
						 clientIOReportDTO.setPlacementDTOList(placementDTOList);
						 clientIOReportDTOList.add(clientIOReportDTO);
					 }
					 else {
						 log.warning("ClientIOReportDTO is null");
					 }
				 }else{
					 log.warning("Invalid data.....");
				 }
			     ExcelReportGenerator erGen = new ExcelReportGenerator();
			     byte[] excelbytes = null;
				 excelbytes = erGen.generateReport("ClientIOReport.xls","ClientIOReportDTO", clientIOReportDTOList);
				 inputStream = new ByteArrayInputStream(excelbytes);
	    	}
	    }catch(Exception e){
	    	 log.severe("Exception in ClientIOReportDTO() in ProposalAction : "+e.getMessage());
			 e.printStackTrace();
	    }
		return Action.SUCCESS;
	}
	
	public String publisherIOReport(){
		 log.info("In publisherIOReport() of ProposalAction");
		 List<PublisherIOReportDTO> publisherIOReportDTOList = new ArrayList<PublisherIOReportDTO>();
		 PublisherIOReportDTO publisherIOReportDTO = null;
		 List<PlacementDTO> placementDTOList = new ArrayList<PlacementDTO>();
	    try{
	    	 String selectedRows=request.getParameter("selectedRows");
	    	 String proposalId=request.getParameter("proposalId");
			 log.info("selectedRows : "+selectedRows+",  proposalId : "+proposalId);	
			 if(proposalId != null && proposalId.trim().length() > 0 && selectedRows != null && selectedRows.trim().length() > 0){
				 IMediaPlanService mediaPlanService=(IMediaPlanService) BusinessServiceLocator.locate(IMediaPlanService.class);
				 publisherIOReportDTO = mediaPlanService.publisherIOReportObject(proposalId);
				 if(publisherIOReportDTO != null) {
					 StringBuilder totalValue = new StringBuilder();
					 placementDTOList = mediaPlanService.getSelectedPlacementReportList(selectedRows, totalValue);
					 String[] tempStr = (totalValue.toString()).split("<SEP>");
					 publisherIOReportDTO.setTotalBudget(tempStr[0]);
					 publisherIOReportDTO.setTotalImpressions(tempStr[1]);
					 publisherIOReportDTO.setPlacementDTOList(placementDTOList);
					 publisherIOReportDTOList.add(publisherIOReportDTO);
				 }
				 else {
					 log.warning("publisherIOReportDTO is null");
				 }
			 }else{
				 log.warning("Invalid data.....");
			 }
		     ExcelReportGenerator erGen = new ExcelReportGenerator();
		     byte[] excelbytes = null;
			 excelbytes = erGen.generateReport("PublisherIOReport.xls","PublisherIOReportDTO", publisherIOReportDTOList);
			 inputStream = new ByteArrayInputStream(excelbytes);
	    }catch(Exception e){
	    	 log.severe("Exception in publisherIOReport()  in ProposalAction : "+e.getMessage());
			 e.printStackTrace();
	    }
		return Action.SUCCESS;
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	@Override
	public ProposalDTO getModel() {		
		return proposalDTO;
	}

	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	
	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setLinCSV(String linCSV) {
		this.linCSV = linCSV;
	}

	public String getLinCSV() {
		return linCSV;
	}

	public void setLinCSVContentType(String linCSVContentType) {
		this.linCSVContentType = linCSVContentType;
	}

	public String getLinCSVContentType() {
		return linCSVContentType;
	}

	public void setLinCSVFileName(String linCSVFileName) {
		this.linCSVFileName = linCSVFileName;
	}

	public String getLinCSVFileName() {
		return linCSVFileName;
	}
	
	
	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setProposalList(List<ProposalDTO> proposalList) {
		this.proposalList = proposalList;
	}

	public List<ProposalDTO> getProposalList() {
		return proposalList;
	}


	public ProposalDTO getProposalDTO() {
		return proposalDTO;
	}


	public void setProposalDTO(ProposalDTO proposalDTO) {
		this.proposalDTO = proposalDTO;
	}


	public void setHeaderMap(Map<String,Integer> headerMap) {
		this.headerMap = headerMap;
	}


	public Map<String,Integer> getHeaderMap() {
		return headerMap;
	}

	public void setCompanyMap(Map<String,String> companyMap) {
		this.companyMap = companyMap;
	}


	public Map<String,String> getCompanyMap() {
		return companyMap;
	}


	public void setAdvertiserMap(Map<String,String> advertiserMap) {
		this.advertiserMap = advertiserMap;
	}


	public Map<String,String> getAdvertiserMap() {
		return advertiserMap;
	}


	public void setAgencyMap(Map<String,String> agencyMap) {
		this.agencyMap = agencyMap;
	}


	public Map<String,String> getAgencyMap() {
		return agencyMap;
	}


	public void setGeoTargetMap(Map<String,String> geoTargetMap) {
		this.geoTargetMap = geoTargetMap;
	}


	public Map<String,String> getGeoTargetMap() {
		return geoTargetMap;
	}


	public void setIndustryMap(Map<String,String> industryMap) {
		this.industryMap = industryMap;
	}


	public Map<String,String> getIndustryMap() {
		return industryMap;
	}


	public void setKpiMap(Map<String,String> kpiMap) {
		this.kpiMap = kpiMap;
	}


	public Map<String,String> getKpiMap() {
		return kpiMap;
	}


	public void setSalesRepList(List<String> salesRepList) {
		this.salesRepList = salesRepList;
	}


	public List<String> getSalesRepList() {
		return salesRepList;
	}


	public void setCampaignTypeMap(Map<String,String> campaignTypeMap) {
		this.campaignTypeMap = campaignTypeMap;
	}


	public Map<String,String> getCampaignTypeMap() {
		return campaignTypeMap;
	}


	public void setCampaignStatusMap(Map<String,String> campaignStatusMap) {
		this.campaignStatusMap = campaignStatusMap;
	}


	public Map<String,String> getCampaignStatusMap() {
		return campaignStatusMap;
	}


	public void setPlacementMap(JSONObject placementMap) {
		this.placementMap = placementMap;
	}


	public JSONObject getPlacementMap() {
		return placementMap;
	}


	public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
		this.userDetailsDTO = userDetailsDTO;
	}


	public UserDetailsDTO getUserDetailsDTO() {
		return userDetailsDTO;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public void setSaveStatus(String saveStatus) {
		this.saveStatus = saveStatus;
	}


	public String getSaveStatus() {
		return saveStatus;
	}

	
	
}
