package com.lin.web.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lin.persistance.dao.IAlertEngineDAO;
import com.lin.persistance.dao.IPerformanceMonitoringDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.AlertEngineDAO;
import com.lin.persistance.dao.impl.PerformanceMonitoringDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AlertEngineFlightObj;
import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.CampaignStatusAlertDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.PerformanceMonitoringAlertDTO;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignFlightDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IAlertEngineService;
import com.lin.web.service.IUserService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EmailUtil;
import com.lin.web.util.ExcelReportGenerator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;

public class AlertEngineService implements IAlertEngineService{
	
	private static final Logger log = Logger.getLogger(AlertEngineService.class.getName());
	String rowColor="";
	
	@Override
	public String alertEngine(List<CompanyObj> companyList, String alertDate){
		List<SmartCampaignObj> dataStoreCampaignList = new ArrayList<>();
	//	Map<String,SmartCampaignObj> campaignStatusMap = new HashMap<>();
		
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<>();
		String generateAlertURL = "/generateCampaignAlert.lin";
		IAlertEngineDAO dao = new AlertEngineDAO();
		
		try{
			if(alertDate==null){
				alertDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			}
			if(companyList!=null && companyList.size()>0){
				for (CompanyObj companyObj : companyList) {
					if(companyObj!=null && companyObj.getCompanyType()!=null && !companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[1])){
						long companyId = companyObj.getId();
						int bqIdentifier = companyObj.getBqIdentifier();
						log.info("going to fetch campaign for companyid = :"+companyId+" bqIdentifier = "+bqIdentifier);
						dataStoreCampaignList = dao.getRunningCampaignList(companyId+"");
						if(dataStoreCampaignList!=null && dataStoreCampaignList.size()>0){
							log.info("dataStoreCampaignList size = "+dataStoreCampaignList.size());
							String commaSepratedOrderIds = "";
							for (SmartCampaignObj smartCampaignObj : dataStoreCampaignList) {
								if(smartCampaignObj!=null && !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Archived.ordinal())+"") 
										&& !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Draft.ordinal())+"") 
										&& !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Inactive.ordinal())+"")){
									if(smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Canceled.ordinal()+"")&& smartCampaignObj.getLastUpdatedOn()!=null){
										Date lastUpdated = smartCampaignObj.getLastUpdatedOn();
										String lastUpdatedStr = DateUtil.getFormatedDate(lastUpdated,"yyyy-MM-dd");
										long fromLastCanceled = 0;
										if(alertDate != null){
											log.info("alertDate in alertEngine() of AlertEngineService = "+alertDate);
											 fromLastCanceled = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getTimeStamp(alertDate, "yyyy-MM-dd"), lastUpdatedStr, "yyyy-MM-dd");
										}
										else
										{
											 fromLastCanceled = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("yyyy-MM-dd"), lastUpdatedStr, "yyyy-MM-dd");
										}
										log.info("lastUpdatedStr = "+lastUpdatedStr);
										
										log.info("fromLastCanceled = "+fromLastCanceled);
										if(fromLastCanceled==0 || fromLastCanceled==-1){
											//campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
											commaSepratedOrderIds = commaSepratedOrderIds+"'"+smartCampaignObj.getDfpOrderId()+"',";
										}
									}else{
										//campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
										commaSepratedOrderIds = commaSepratedOrderIds+"'"+smartCampaignObj.getDfpOrderId()+"',";
									}
									
								}
							}
							if(commaSepratedOrderIds!=null){
								commaSepratedOrderIds = StringUtil.deleteFromLastOccurence(commaSepratedOrderIds, ",");
							}
							QueryDTO queryDTO = new QueryDTO();
							queryDTO = BigQueryUtil.getQueryDTO(bqIdentifier+"", LinMobileConstants.BQ_CORE_PERFORMANCE);
							if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
								campaignList = dao.loadAllCampaignData(queryDTO,commaSepratedOrderIds);
								if(campaignList!=null && campaignList.size()>0){
									for (PerformanceMonitoringDTO performanceMonitoringDTOObj : campaignList) {
										if(performanceMonitoringDTOObj!=null && performanceMonitoringDTOObj.getOrderId()!=null){
											long dayRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getTimeStamp(alertDate, "yyyy-MM-dd"), performanceMonitoringDTOObj.getEndDateTime(), "yyyy-MM-dd");
											if(dayRemaining+1>=0){
												TaskQueueUtil.generateCampaignAlert(generateAlertURL, performanceMonitoringDTOObj.getOrderId(),
														performanceMonitoringDTOObj.getStartDateTime(), performanceMonitoringDTOObj.getEndDateTime(), alertDate, bqIdentifier+"");
											}
										
										}
									
									}
								}
							}
								
						}
					}
				}
			}

		}catch(Exception e){
			log.severe("Exception in AlertEngine of AlertEngineService : "+e.getMessage());
			
		}
		return alertDate;
		
	}
	
	@Override
	public String generateCampaignAlert(String orderId, String startDate, String endDate, String alertDate, String bqIdentifier){
		log.info("inside generateCampaignAlert of alertEngineService");
		SmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		List<AlertEngineObj> engineObjList = new ArrayList<>();
		long dayRemaining = 0;
		try{
			if(alertDate==null){
				alertDate=DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
			}
			dayRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getTimeStamp(alertDate, "yyyy-MM-dd"), endDate, "yyyy-MM-dd");
			dayRemaining = dayRemaining+1;
			log.info("days remaining......................"+dayRemaining);
			if(dayRemaining>=0){
				long dayPassed = 0;
					dayPassed = DateUtil.getDifferneceBetweenTwoDates(startDate, DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd"), "yyyy-MM-dd");
					log.info("dayPassed...."+dayPassed);
					long totalDays = DateUtil.getDifferneceBetweenTwoDates(startDate, endDate, "yyyy-MM-dd");
					dayPassed = dayPassed+1;
					totalDays = totalDays+1;
					log.info("totalDays....."+totalDays);
					SmartCampaignObj campaignObj = campaignDAO.getCampaignByDFPOrderId(StringUtil.getLongValue(orderId));
					if(campaignObj!=null && campaignObj.getCampaignId()!=null){
						engineObjList = getPlacementLevelAlert(campaignObj, alertDate, totalDays, dayPassed, dayRemaining, bqIdentifier );
					}
					if(engineObjList!=null && engineObjList.size()>0){
						for (AlertEngineObj alertEngineObj : engineObjList) {
							if(alertEngineObj!=null){
								campaignDAO.saveObject(alertEngineObj);
							}
						}
					}
			}
			
		}catch(Exception e){
			log.severe("Exception in generateCampaignAlert of AlertEngineService : "+e.getMessage());
			
		}
		
		return alertDate;
		
	}
	
	public List<AlertEngineObj> getPlacementLevelAlert(SmartCampaignObj campaignObj,String alertDate,long totalDays,long dayPassed, long dayRemaining, String bqIdentifier) throws Exception{
		log.info("inside getPlacementLavelAlert of alertEngineService");
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		List<PerformanceMonitoringDTO> lineItemList = new ArrayList<>();
		String justPreviousDate = null;
		IAlertEngineDAO alertDAO = new AlertEngineDAO();
		SmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		placementByIdList = campaignDAO.getAllPlacementOfCampaign(campaignObj.getCampaignId());
		Map<String,PerformanceMonitoringDTO> lineItemMap = new HashMap<>();
		long campaignGoal = 0;
		List<DFPLineItemDTO> dfpLineItemDTOList = new ArrayList<DFPLineItemDTO>();
		Map<String,String> partnerMap = new HashMap<>();
		List<AlertEngineObj> engineObjList = new ArrayList<>();
		PerformanceMonitoringService performanceMonitoringServiceObj  = new PerformanceMonitoringService();
		QueryDTO queryDTO = BigQueryUtil.getQueryDTO(bqIdentifier, LinMobileConstants.BQ_CORE_PERFORMANCE);
		Date date = DateUtil.getRequiredFormatDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd");
		justPreviousDate = DateUtil.getJustPreviousDate(date, "yyyy-MM-dd");
		lineItemList = alertDAO.loadAllLineItemsForOrderId(campaignObj.getDfpOrderId()+"", queryDTO, justPreviousDate);
		if(lineItemList!=null && lineItemList.size()>0){
			log.info("lineItemList size = "+lineItemList.size());
			for (PerformanceMonitoringDTO performanceMonitoringDTO : lineItemList) {
				if(performanceMonitoringDTO!=null && performanceMonitoringDTO.getLineItemId()!=null){
					lineItemMap.put(performanceMonitoringDTO.getLineItemId(), performanceMonitoringDTO);
				}
			}
		}
		if(placementByIdList!=null && placementByIdList.size()>0){
			campaignGoal = 0;
			for (SmartCampaignPlacementObj placementObj : placementByIdList) {
				 
				if(placementObj!=null && placementObj.getBudget()!=null && !placementObj.getBudget().equals("") && placementObj.getImpressions()!=null && !placementObj.getImpressions().equals("")){
					String imp = placementObj.getImpressions().trim().replaceAll(",","");
					campaignGoal = campaignGoal+Long.parseLong(imp);
				}
				
				if(placementObj!=null && placementObj.getDfpLineItemList()!=null && placementObj.getDfpLineItemList().size()>0 && placementObj.getBudget()!=null && !placementObj.getBudget().equals("") && placementObj.getImpressions()!=null && !placementObj.getImpressions().equals("")){
					long placementGoal = 0;
					long impressionDelivered = 0;
					long clicksDelivered = 0;
					int pacingAlert = 0;
					int deliveryAlert = 0;
					int ctrAlert = 0;
					long targetPacing = 0;
					long currentPacing = 0;
					long revisedPacing = 0;
					double pacingMargin =  0.0;
					double deliveryMargin =  0.0;
					double ctrMargin =  0.0;
					long targetDelivery = 0;
					float currentCTR = 0;
					float targetCTR = 0;
					AlertEngineObj engineObj = new AlertEngineObj();
					long totalFlightDays = performanceMonitoringServiceObj.dayDurationInFlights(placementObj.getFlightObjList(),null,null,"MM-dd-yyyy");
					totalFlightDays = totalFlightDays+1;
					log.info("Total days = "+totalDays);
					log.info("Total Flight days = "+totalFlightDays);
					totalDays = totalFlightDays;
					
					String partner = "";
					String imp = placementObj.getImpressions().trim().replaceAll(",","");
					placementGoal = StringUtil.getLongValue(imp);
					dfpLineItemDTOList = placementObj.getDfpLineItemList();
					for (DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOList) {
						if(dfpLineItemDTO!=null && dfpLineItemDTO.getLineItemId()!=0){
							PerformanceMonitoringDTO lineItemObj = lineItemMap.get(dfpLineItemDTO.getLineItemId()+"");
							if(lineItemObj!=null){
								impressionDelivered = impressionDelivered + StringUtil.getLongValue(lineItemObj.getImpressionDelivered());
								clicksDelivered = clicksDelivered + StringUtil.getLongValue(lineItemObj.getClicks());
							}
							
						}
						
						if(dfpLineItemDTO!=null && dfpLineItemDTO.getPartner()!=null && dfpLineItemDTO.getPartner().length()>0){
							partnerMap.put(dfpLineItemDTO.getPartner(),dfpLineItemDTO.getPartner());
						}
					}
					
					
					if(partnerMap!=null && partnerMap.size()>0){
						 Iterator iterator = partnerMap.entrySet().iterator();
							while (iterator.hasNext()) {
								Map.Entry mapEntry = (Map.Entry) iterator.next();
								partner = partner+mapEntry.getValue()+",";
								iterator.remove();
								//partner
						}
							partner = StringUtil.deleteFromLastOccurence(partner, ",");
					}
					
					// Pacing and Delivery calculations
					
					if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0){
						if(campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1 && totalDays!=0){
							log.info("Campaign type is CPM");
							targetPacing = (placementGoal/totalDays);
							log.info("targetPacing = "+targetPacing);
							if(dayPassed!=0){
								log.info("currentPacing if");
								currentPacing = (impressionDelivered/dayPassed);
								log.info("targetPacing = "+currentPacing);
							}
							if(dayRemaining!=0){
								log.info("revisedPacing if");
								revisedPacing = ((placementGoal-impressionDelivered)/dayRemaining);
								log.info("revisedPacing = "+revisedPacing);
							}
							
							if(targetPacing!=0 && dayPassed!=0){
								log.info("targetDelivery if");
								targetDelivery = targetPacing*dayPassed;
								log.info("targetDelivery = "+targetDelivery);
							}
							if(targetDelivery>impressionDelivered && targetDelivery!=0){
								deliveryMargin = (double)((impressionDelivered*100)/targetDelivery);
							}else if(impressionDelivered>targetDelivery && impressionDelivered!=0){
								deliveryMargin = (double)((targetDelivery*100)/impressionDelivered);
							}else{
								deliveryMargin = 0.0;
							}
							
						}else if(campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
							log.info("Campaign type is CPC");
							targetPacing = (placementGoal/totalDays);
							if(dayPassed!=0){
								currentPacing = (clicksDelivered/dayPassed);
							}
							if(dayRemaining!=0){
								revisedPacing = ((placementGoal-clicksDelivered)/dayRemaining);
							}

							if(targetPacing!=0 && dayPassed!=0){
								targetDelivery = targetPacing*dayPassed;
							}
							if(targetDelivery>clicksDelivered && targetDelivery!=0){
								deliveryMargin = (double)((clicksDelivered*100)/targetDelivery);
							}else if(clicksDelivered>targetDelivery && clicksDelivered!=0){
								deliveryMargin = (double)((targetDelivery*100)/clicksDelivered);
							}else{
								deliveryMargin = 0.0;
							}

						}
					}
					
					if(targetPacing>currentPacing && targetPacing!=0){
						pacingMargin = (double)((currentPacing*100)/targetPacing);
					}else if(targetPacing<currentPacing && currentPacing!=0){
						pacingMargin = (double)((targetPacing*100)/currentPacing);
					}else{
						pacingMargin = 0;
					}
					
					if(pacingMargin>2.0){
						pacingAlert = 1;
					}else{
						pacingAlert = 0;
					}
					
					if(deliveryMargin>2.0){
						deliveryAlert = 1;
					}else{
						deliveryAlert = 0;
					}
					
			
					
					
					//Goal CTR Calculations
					 log.info("Goal CTR Calculations");
					if(placementObj!=null && placementObj.getGoal()!=null && placementObj.getGoal().length()>0){
						targetCTR = Float.parseFloat(placementObj.getGoal());
						if(impressionDelivered!=0){
							currentCTR = (float)(clicksDelivered*100)/impressionDelivered;
						}
						
						if(targetCTR>=currentCTR){
							ctrMargin = (double)((currentCTR*100)/targetCTR);
						}else if(currentCTR>=targetCTR){
							ctrMargin = (double)((targetCTR*100)/currentCTR);
						}else{
							ctrMargin = 0.0;
						}
						
						if(ctrMargin>1.0){
							ctrAlert = 1;
						}else{
							ctrAlert= 0;
						}
						
					}
					
					// Alert Engine object Creation......
					 log.info("Alert Engine object Creation....");
					 engineObj.setPublisherName(partner);
					 
					if(campaignObj.getCampaignId()!=0){
						engineObj.setCampaignId(campaignObj.getCampaignId());
					}
					if(placementObj.getPlacementId()!=0){
						engineObj.setPlacementId(placementObj.getPlacementId());
					}
					if(campaignObj.getName()!=null && campaignObj.getName().trim().length()>0){
						engineObj.setCampaignName(campaignObj.getName().trim());
					}
					if(campaignObj.getCompanyId()!=null && campaignObj.getCompanyId().length()>0){
						engineObj.setPublisherId(StringUtil.getLongValue(campaignObj.getCompanyId()));
					}
					if(placementObj.getPlacementName()!=null && placementObj.getPlacementName().trim().length()>0){
						engineObj.setPlacementName(placementObj.getPlacementName().trim());
					}
					if(targetPacing!=0){
						engineObj.setDailyPacingExpected(targetPacing);
					}
					if(currentPacing!=0){
						engineObj.setDailyPacingCurrent(currentPacing);
					}
					if(revisedPacing!=0){
						engineObj.setDailyPacingReviced(revisedPacing);
					}
					engineObj.setDailyPacingAlert(pacingAlert);
					
					if(targetCTR!=0.0){
						engineObj.setGoalCTRExpected(targetCTR);
					}
					if(currentCTR!=0.0){
						engineObj.setGoalCTRCurrent(currentCTR);
					}
					engineObj.setGoalCTRAlert(ctrAlert);
					
					if(targetDelivery!=0){
						engineObj.setDeliveryExpected(targetDelivery);
					}
					if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1){
						engineObj.setDeliveryCurent(impressionDelivered);
					}else if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
						engineObj.setDeliveryCurent(clicksDelivered);
					}
					engineObj.setDeliveyAlert(deliveryAlert);
					engineObj.setAlertDate(DateUtil.getDateYYYYMMDD());
					if(alertDate != null){
						engineObj.setAlertDate(DateUtil.getRequiredFormatDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd"));
					}
					if(alertDate != null && placementObj.getPlacementId()!=null ){
						log.info("alertDate = "+alertDate);
						engineObj.setId(alertDate+"_"+placementObj.getPlacementId());
					}
					engineObj.setStartDate(DateUtil.getFormatedDate(campaignObj.getStartDate(), "MM-dd-yyyy", "yyyy-MM-dd"));
					engineObj.setEndDate(DateUtil.getFormatedDate(campaignObj.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd"));
					engineObj.setRateType(campaignObj.getRateTypeList().get(0).getObjectId());
					engineObj.setCampaignStatus(StringUtil.getIntegerValue(campaignObj.getCampaignStatus(), 0));
					engineObj.setPlacementGoal(placementGoal);
					if(campaignObj.getCompanyId()!=null && campaignObj.getCompanyId().length()>0){
						engineObj.setCompanyId(campaignObj.getCompanyId());
					}
					engineObj = getFlightLevelAlert(engineObj, placementObj, alertDate, justPreviousDate, bqIdentifier, lineItemMap, campaignObj);
					engineObjList.add(engineObj);
				}
			}
		}
		return engineObjList;
		
	}
	
	public AlertEngineObj getFlightLevelAlert(AlertEngineObj engineObj, SmartCampaignPlacementObj placementObj, String alertDate,
			String justPreviousDate, String bqIdentifier, Map<String,PerformanceMonitoringDTO> lineItemMap, SmartCampaignObj campaignObj)throws Exception{
		IAlertEngineDAO dao = new AlertEngineDAO();
		if(placementObj.getFlightObjList()!=null && placementObj.getFlightObjList().size()>0){
			log.info("flight level Calculations starts"+placementObj.getFlightObjList().size());
			List<DFPLineItemDTO> dfpLineItemDTOList = new ArrayList<DFPLineItemDTO>();
			dfpLineItemDTOList = placementObj.getDfpLineItemList();
						for (SmartCampaignFlightDTO flightDTO : placementObj.getFlightObjList()) {
							long flightDaysRemaining = 0;
							long flightFutureDays = 0;
							
							
								log.info(flightDTO.getStartDate()+"====="+flightDTO.getEndDate());
								flightDaysRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getFormatedDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), flightDTO.getEndDate(), "MM-dd-yyyy");
								flightFutureDays =  DateUtil.getDifferneceBetweenTwoDates(DateUtil.getFormatedDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), flightDTO.getStartDate(), "MM-dd-yyyy");
								log.info("inside if......flightDaysRemaining..>>="+flightDaysRemaining+" flightFutureDays.."+flightFutureDays);
														
							
							if(flightDaysRemaining>=-1 && flightFutureDays<=1){
								log.info("flightDaysRemaining>=0 && flightFutureDays<=0");
							long flightTotalDays = DateUtil.getDifferneceBetweenTwoDates(flightDTO.getStartDate(), flightDTO.getEndDate(), "MM-dd-yyyy");
							long flightDaysPassed = 0;
						
								flightDaysPassed = DateUtil.getDifferneceBetweenTwoDates(flightDTO.getStartDate(), DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), "MM-dd-yyyy");
								log.info("inside if......"+flightDaysPassed);
								log.info("DateUtil.getJustPreviousDate = "+DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"));
								log.info("getYesterdayDateString = "+DateUtil.getYesterdayDateString("MM-dd-yyyy"));
														
							
							flightTotalDays = flightTotalDays+1;
							flightDaysPassed = flightDaysPassed+1;
							flightDaysRemaining = flightDaysRemaining+1;
							
							log.info("flight total days ......................"+flightTotalDays);
							log.info("flight passed days ......................"+flightDaysPassed);
							log.info("flight days remaining......................"+flightDaysRemaining);
							
							long flightGoal = StringUtil.getLongValue(flightDTO.getGoal());
							long flightTargetDelivery = 0;
							long flightImpressionsDelivered = 0;
							long flightClicksDelivered = 0;
							long flightPacingCurrent = 0;
							long flightPacingExpected = 0;
							long flightPacingRevised = 0;
							int flightDeliveryAlert = 0;
							int flightPacingAlert = 0;
							int flightId = 0;
							String flightEndDate = "";
							
							if(alertDate != null){
								flightEndDate = justPreviousDate;
								log.info("justPreviousDate : "+justPreviousDate);
							}else{
								flightEndDate = DateUtil.getFormatedDate(flightDTO.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd");
							}
							
							String flightStartDate = DateUtil.getFormatedDate(flightDTO.getStartDate(), "MM-dd-yyyy", "yyyy-MM-dd");
							if(flightDTO!=null && flightDTO.getFlightId()!=0){
								flightId = flightDTO.getFlightId();
							}
							double flightDeliveryMargin = 0.0;
							double flightPacingMargin = 0.0;
								for (DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOList) {
									log.info("dfpLineItemDTOList size = "+dfpLineItemDTOList.size());
									if(dfpLineItemDTO!=null){
										PerformanceMonitoringDTO lineItemObj = lineItemMap.get(dfpLineItemDTO.getLineItemId()+"");
										List<PerformanceMonitoringDTO> lineItemDataList = new ArrayList<>();
										QueryDTO queryDTO = BigQueryUtil.getQueryDTO(bqIdentifier, LinMobileConstants.BQ_CORE_PERFORMANCE);
										if(lineItemObj !=null){
											
											log.info("lineItemObj : "+lineItemObj.toString());
											if(lineItemObj.getLineItemId() !=null){
												lineItemDataList = dao.loadAllLineItemsForFlights(lineItemObj.getLineItemId(),
														flightStartDate, flightEndDate, queryDTO);
											 }
											
											if(lineItemDataList!=null && lineItemDataList.size()>0){
												
													for (PerformanceMonitoringDTO performanceMonitoringDTO : lineItemDataList) {
														if(performanceMonitoringDTO!=null){
															if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1){
																log.info("Campaign type is CPM");
																flightImpressionsDelivered = flightImpressionsDelivered+StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
																log.info("flightImpressionsDelivered = "+flightImpressionsDelivered);

																flightPacingExpected = (flightGoal/flightTotalDays);
																log.info("flightPacingExpected = "+flightPacingExpected);
																if(flightDaysPassed!=0){
																	log.info("flightCurrentPacing if");
																	flightPacingCurrent = (flightImpressionsDelivered/flightDaysPassed);
																	log.info("flightPacingCurrent = "+flightPacingCurrent);
																}
																if(flightDaysRemaining!=0){
																	log.info("flightPacingRevised if");
																	flightPacingRevised = ((flightGoal-flightImpressionsDelivered)/flightDaysRemaining);
																	log.info("flightPacingRevised = "+flightPacingRevised);
																}
																
																if(flightPacingExpected!=0 && flightDaysPassed!=0){
																	log.info("flight target Delivery if");
																	flightTargetDelivery = flightPacingExpected*flightDaysPassed;
																	log.info("flightTargetDelivery = "+flightTargetDelivery);
																}
																if(flightTargetDelivery>flightImpressionsDelivered && flightTargetDelivery!=0){
																	flightDeliveryMargin = (double)((flightImpressionsDelivered*100)/flightTargetDelivery);
																}else if(flightImpressionsDelivered>flightTargetDelivery && flightImpressionsDelivered!=0){
																	flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightImpressionsDelivered);
																}else{
																	flightDeliveryMargin = 0.0;
																}
																
																
																
															}else if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
																log.info("Campaign type is CPC");
																flightClicksDelivered = flightClicksDelivered+StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
																log.info("flightClicksDelivered = "+flightClicksDelivered);
																
																flightPacingExpected = (flightGoal/flightTotalDays);
																if(flightDaysPassed!=0){
																	flightPacingCurrent = (flightClicksDelivered/flightDaysPassed);
																}
																if(flightDaysRemaining!=0){
																	flightPacingRevised = ((flightGoal-flightClicksDelivered)/flightDaysRemaining);
																}

																if(flightPacingExpected!=0 && flightDaysPassed!=0){
																	flightTargetDelivery = flightPacingExpected*flightDaysPassed;
																}
																if(flightTargetDelivery>flightClicksDelivered && flightTargetDelivery!=0){
																	flightDeliveryMargin = (double)((flightClicksDelivered*100)/flightTargetDelivery);
																}else if(flightClicksDelivered>flightTargetDelivery && flightClicksDelivered!=0){
																	flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightClicksDelivered);
																}else{
																	flightDeliveryMargin = 0.0;
																}
															}
														}
													}
											}
										}else{
											log.info("lineItemObj is null");
										}																
										log.info("inside flight calculation date if block");																			
									}else{
										log.info(">>>>>dfpLineItemDTO is null");
									}
									
									log.info("Now setting flightDeliveryAlert.....");
									if(flightTargetDelivery>flightImpressionsDelivered && flightTargetDelivery!=0){
										flightDeliveryMargin = (double)((flightImpressionsDelivered*100)/flightTargetDelivery);
									}else if(flightImpressionsDelivered>flightTargetDelivery && flightImpressionsDelivered!=0){
										flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightImpressionsDelivered);
									}else{
										flightDeliveryMargin = 0.0;
									}
						
									if(flightDeliveryMargin>2.0){
										flightDeliveryAlert = 1;
									}else{
										flightDeliveryAlert = 0;
									}
									
									
									if(flightPacingExpected>flightPacingCurrent && flightPacingExpected!=0){
										flightPacingMargin = (double)((flightPacingCurrent*100)/flightPacingExpected);
									}else if(flightPacingExpected<flightPacingCurrent && flightPacingCurrent!=0){
										flightPacingMargin = (double)((flightPacingExpected*100)/flightPacingCurrent);
									}else{
										flightPacingMargin = 0;
									}
									
									if(flightPacingMargin>2.0){
										flightPacingAlert = 1;
									}else{
										flightPacingAlert = 0;
									}
									
						
									if(flightImpressionsDelivered!=0){
										engineObj.setFlightDeliveryCurent(flightImpressionsDelivered);
									}
									if(flightTargetDelivery!=0){
										engineObj.setFlightDeliveryExpected(flightTargetDelivery);
									}
									if(flightDeliveryAlert!=0){
										engineObj.setFlightDeliveyAlert(flightDeliveryAlert);
									}
									if(flightId!=0){
										engineObj.setFlightId(flightId);
									}
									if(flightStartDate!=null){
										engineObj.setFlightStartDate(flightStartDate);
									}
									
									if(flightDTO.getEndDate()!=null){
										String endData = DateUtil.getFormatedDate(flightDTO.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd");

										engineObj.setFlightEndDate(endData);
									}
									
									if(flightPacingExpected!=0){
										engineObj.setFlightDailyPacingExpected(flightPacingExpected);
									}
									if(flightPacingCurrent!=0){
										engineObj.setFlightDailyPacingCurrent(flightPacingCurrent);
									}
									if(flightPacingRevised!=0){
										engineObj.setFlightDailyPacingRevised(flightPacingRevised);
									}
									if(flightGoal!=0){
										engineObj.setFlightGoal(flightGoal);
									}
									
								/*	if(engineObj!=null){
										dao.saveObject(engineObj);
									}*/
									
								/*	if(engineFlightObj!=null){
										dao.saveObject(engineFlightObj);
										log.info(" save AlertEngineFlightObj success");
									}*/
						
								}
							break;	
				
							}
						}
		}
		return engineObj;
	}
	
	@Override
	public String alertEngine1(String publisherIdInBQ, String alertDate){
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<>();
		IAlertEngineDAO dao = new AlertEngineDAO();
		List<SmartCampaignObj> dataStoreCampaignList = new ArrayList<>();
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		Map<String,SmartCampaignObj> campaignStatusMap = new HashMap<>();
		QueryDTO queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
		SmartCampaignPlannerDAO campaignDao = new SmartCampaignPlannerDAO();
		long campaignGoal = 0;
		String justPreviousDate = null;
		String currentDate = DateUtil.getCurrentTimeStamp("yyyy-MM-dd");
		PerformanceMonitoringService performanceMonitoringServiceObj  = new PerformanceMonitoringService();
		try{
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				campaignList = dao.loadAllCampaignData(queryDTO,"");
				if(campaignList!=null && campaignList.size()>0){
					//dataStoreCampaignList = dao.getRunningCampaignList();
					if(dataStoreCampaignList!=null && dataStoreCampaignList.size()>0){
						for (SmartCampaignObj smartCampaignObj : dataStoreCampaignList) {
							if(smartCampaignObj!=null && !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Archived.ordinal())+"") 
									&& !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Draft.ordinal())+"") 
									&& !smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Inactive.ordinal())+"")){
								if(smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Canceled.ordinal()+"")&& smartCampaignObj.getLastUpdatedOn()!=null){
									Date lastUpdated = smartCampaignObj.getLastUpdatedOn();
									String lastUpdatedStr = DateUtil.getFormatedDate(lastUpdated,"yyyy-MM-dd");
									long fromLastCanceled = 0;
									if(alertDate != null){
										log.info("alertDate in alertEngine() of AlertEngineService = "+alertDate);
										 fromLastCanceled = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getTimeStamp(alertDate, "yyyy-MM-dd"), lastUpdatedStr, "yyyy-MM-dd");
									}
									else
									{
										 fromLastCanceled = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("yyyy-MM-dd"), lastUpdatedStr, "yyyy-MM-dd");
									}
									log.info("lastUpdatedStr = "+lastUpdatedStr);
									
									log.info("fromLastCanceled = "+fromLastCanceled);
									if(fromLastCanceled==0 || fromLastCanceled==-1){
										campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
									}
								}else{
									campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
								}
								
							}
						}
					}
					
					for (PerformanceMonitoringDTO performanceMonitoringDTOObj : campaignList) {
						if(performanceMonitoringDTOObj!=null && performanceMonitoringDTOObj.getOrderId()!=null && campaignStatusMap.containsKey(performanceMonitoringDTOObj.getOrderId())){
							long dayRemaining = 0;
							if(alertDate != null){
								log.info("alertDate in alertEngine() of AlertEngineService = "+alertDate);
								dayRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getTimeStamp(alertDate, "yyyy-MM-dd"), performanceMonitoringDTOObj.getEndDateTime(), "yyyy-MM-dd");
							}
							else
							{
								dayRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("yyyy-MM-dd"), performanceMonitoringDTOObj.getEndDateTime(), "yyyy-MM-dd");
							}
							dayRemaining = dayRemaining+1;
							log.info("days remaining......................"+dayRemaining);
							if(dayRemaining>=0){
							List<PerformanceMonitoringDTO> lineItemList = new ArrayList<>();
							Map<String,PerformanceMonitoringDTO> lineItemMap = new HashMap<>();
							List<DFPLineItemDTO> dfpLineItemDTOList = new ArrayList<DFPLineItemDTO>();
							SmartCampaignObj campaignObj = campaignStatusMap.get(performanceMonitoringDTOObj.getOrderId());
							long dayPassed = 0;
							if(alertDate != null){
								dayPassed = DateUtil.getDifferneceBetweenTwoDates(performanceMonitoringDTOObj.getStartDateTime(), DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd"), "yyyy-MM-dd");
							}else
							{
								dayPassed = DateUtil.getDifferneceBetweenTwoDates(performanceMonitoringDTOObj.getStartDateTime(), DateUtil.getYesterdayDateString("yyyy-MM-dd"), "yyyy-MM-dd");
							}
							log.info("dayPassed...."+dayPassed);
							Map<String,String> partnerMap = new HashMap<>();
							
							long totalDays = DateUtil.getDifferneceBetweenTwoDates(performanceMonitoringDTOObj.getStartDateTime(), performanceMonitoringDTOObj.getEndDateTime(), "yyyy-MM-dd");
							//long impressionDelivered = StringUtil.getLongValue(performanceMonitoringDTOObj.getImpressionDelivered());
							//long clicksDelivered = StringUtil.getLongValue(performanceMonitoringDTOObj.getClicks());
							dayPassed = dayPassed+1;
							totalDays = totalDays+1;
							log.info("totalDays....."+totalDays);
							placementByIdList = campaignDao.getAllPlacementOfCampaign(campaignObj.getCampaignId());
							
							queryDTO = new QueryDTO();
							queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
							if(performanceMonitoringDTOObj.getOrderId() !=null && performanceMonitoringDTOObj.getOrderId().length()>0){
								if(alertDate != null)
								{
									alertDate = DateUtil.getFormatedDate(alertDate,"yyyy-MM-dd", "yyyy-MM-dd");
									
									Date date = DateUtil.getRequiredFormatDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd");
									justPreviousDate = DateUtil.getJustPreviousDate(date, "yyyy-MM-dd");
									lineItemList = dao.loadAllLineItemsForOrderId(performanceMonitoringDTOObj.getOrderId(), queryDTO, justPreviousDate);
								}
								else
								{	Date date = DateUtil.getRequiredFormatDate(currentDate, "yyyy-MM-dd", "yyyy-MM-dd");
									justPreviousDate = DateUtil.getJustPreviousDate(date, "yyyy-MM-dd");
									lineItemList = dao.loadAllLineItemsForOrderId(performanceMonitoringDTOObj.getOrderId(), queryDTO, justPreviousDate);
								}
									
							}
							if(lineItemList!=null && lineItemList.size()>0){
								log.info("lineItemList size = "+lineItemList.size());
								for (PerformanceMonitoringDTO performanceMonitoringDTO : lineItemList) {
									if(performanceMonitoringDTO!=null && performanceMonitoringDTO.getLineItemId()!=null){
										lineItemMap.put(performanceMonitoringDTO.getLineItemId(), performanceMonitoringDTO);
									}
								}
							}
							
							if(placementByIdList!=null && placementByIdList.size()>0){
								campaignGoal = 0;
								for (SmartCampaignPlacementObj placementObj : placementByIdList) {
									 
									if(placementObj!=null && placementObj.getBudget()!=null && !placementObj.getBudget().equals("") && placementObj.getImpressions()!=null && !placementObj.getImpressions().equals("")){
										String imp = placementObj.getImpressions().trim().replaceAll(",","");
										campaignGoal = campaignGoal+Long.parseLong(imp);
									}
									
									if(placementObj!=null && placementObj.getDfpLineItemList()!=null && placementObj.getDfpLineItemList().size()>0 && placementObj.getBudget()!=null && !placementObj.getBudget().equals("") && placementObj.getImpressions()!=null && !placementObj.getImpressions().equals("")){
										long placementGoal = 0;
										long impressionDelivered = 0;
										long clicksDelivered = 0;
										int pacingAlert = 0;
										int deliveryAlert = 0;
										int ctrAlert = 0;
										long targetPacing = 0;
										long currentPacing = 0;
										long revisedPacing = 0;
										double pacingMargin =  0.0;
										double deliveryMargin =  0.0;
										double ctrMargin =  0.0;
										long targetDelivery = 0;
										float currentCTR = 0;
										float targetCTR = 0;
										AlertEngineObj engineObj = new AlertEngineObj();
										
										long totalFlightDays = performanceMonitoringServiceObj.dayDurationInFlights(placementObj.getFlightObjList(),null,null,"MM-dd-yyyy");
										totalFlightDays = totalFlightDays+1;
										log.info("Total days = "+totalDays);
										log.info("Total Flight days = "+totalFlightDays);
										totalDays = totalFlightDays;
										
										String partner = "";
										String imp = placementObj.getImpressions().trim().replaceAll(",","");
										placementGoal = StringUtil.getLongValue(imp);
										dfpLineItemDTOList = placementObj.getDfpLineItemList();
										for (DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOList) {
											if(dfpLineItemDTO!=null && dfpLineItemDTO.getLineItemId()!=0){
												PerformanceMonitoringDTO lineItemObj = lineItemMap.get(dfpLineItemDTO.getLineItemId()+"");
												if(lineItemObj!=null){
													impressionDelivered = impressionDelivered + StringUtil.getLongValue(lineItemObj.getImpressionDelivered());
													clicksDelivered = clicksDelivered + StringUtil.getLongValue(lineItemObj.getClicks());
												}
												
											}
											
											if(dfpLineItemDTO!=null && dfpLineItemDTO.getPartner()!=null && dfpLineItemDTO.getPartner().length()>0){
												partnerMap.put(dfpLineItemDTO.getPartner(),dfpLineItemDTO.getPartner());
											}
										}
										
										
										if(partnerMap!=null && partnerMap.size()>0){
											 Iterator iterator = partnerMap.entrySet().iterator();
												while (iterator.hasNext()) {
													Map.Entry mapEntry = (Map.Entry) iterator.next();
													partner = partner+mapEntry.getValue()+",";
													iterator.remove();
													//partner
											}
												partner = StringUtil.deleteFromLastOccurence(partner, ",");
										}
										
										// Pacing and Delivery calculations
										
										if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0){
											if(campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1 && totalDays!=0){
												log.info("Campaign type is CPM");
												targetPacing = (placementGoal/totalDays);
												log.info("targetPacing = "+targetPacing);
												if(dayPassed!=0){
													log.info("currentPacing if");
													currentPacing = (impressionDelivered/dayPassed);
													log.info("targetPacing = "+currentPacing);
												}
												if(dayRemaining!=0){
													log.info("revisedPacing if");
													revisedPacing = ((placementGoal-impressionDelivered)/dayRemaining);
													log.info("revisedPacing = "+revisedPacing);
												}
												
												if(targetPacing!=0 && dayPassed!=0){
													log.info("targetDelivery if");
													targetDelivery = targetPacing*dayPassed;
													log.info("targetDelivery = "+targetDelivery);
												}
												if(targetDelivery>impressionDelivered && targetDelivery!=0){
													deliveryMargin = (double)((impressionDelivered*100)/targetDelivery);
												}else if(impressionDelivered>targetDelivery && impressionDelivered!=0){
													deliveryMargin = (double)((targetDelivery*100)/impressionDelivered);
												}else{
													deliveryMargin = 0.0;
												}
												
											}else if(campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
												log.info("Campaign type is CPC");
												targetPacing = (placementGoal/totalDays);
												if(dayPassed!=0){
													currentPacing = (clicksDelivered/dayPassed);
												}
												if(dayRemaining!=0){
													revisedPacing = ((placementGoal-clicksDelivered)/dayRemaining);
												}

												if(targetPacing!=0 && dayPassed!=0){
													targetDelivery = targetPacing*dayPassed;
												}
												if(targetDelivery>clicksDelivered && targetDelivery!=0){
													deliveryMargin = (double)((clicksDelivered*100)/targetDelivery);
												}else if(clicksDelivered>targetDelivery && clicksDelivered!=0){
													deliveryMargin = (double)((targetDelivery*100)/clicksDelivered);
												}else{
													deliveryMargin = 0.0;
												}

											}
										}
										
										if(targetPacing>currentPacing && targetPacing!=0){
											pacingMargin = (double)((currentPacing*100)/targetPacing);
										}else if(targetPacing<currentPacing && currentPacing!=0){
											pacingMargin = (double)((targetPacing*100)/currentPacing);
										}else{
											pacingMargin = 0;
										}
										
										if(pacingMargin>2.0){
											pacingAlert = 1;
										}else{
											pacingAlert = 0;
										}
										
										if(deliveryMargin>2.0){
											deliveryAlert = 1;
										}else{
											deliveryAlert = 0;
										}
										
								
										
										
										//Goal CTR Calculations
										 log.info("Goal CTR Calculations");
										if(placementObj!=null && placementObj.getGoal()!=null && placementObj.getGoal().length()>0){
											targetCTR = Float.parseFloat(placementObj.getGoal());
											if(impressionDelivered!=0){
												currentCTR = (float)(clicksDelivered*100)/impressionDelivered;
											}
											
											if(targetCTR>=currentCTR){
												ctrMargin = (double)((currentCTR*100)/targetCTR);
											}else if(currentCTR>=targetCTR){
												ctrMargin = (double)((targetCTR*100)/currentCTR);
											}else{
												ctrMargin = 0.0;
											}
											
											if(ctrMargin>1.0){
												ctrAlert = 1;
											}else{
												ctrAlert= 0;
											}
											
										}
										
										// Alert Engine object Creation......
										 log.info("Alert Engine object Creation....");
										 engineObj.setPublisherName(partner);
										 
										if(campaignObj.getCampaignId()!=0){
											engineObj.setCampaignId(campaignObj.getCampaignId());
										}
										if(placementObj.getPlacementId()!=0){
											engineObj.setPlacementId(placementObj.getPlacementId());
										}
										if(campaignObj.getName()!=null && campaignObj.getName().trim().length()>0){
											engineObj.setCampaignName(campaignObj.getName().trim());
										}
										if(campaignObj.getCompanyId()!=null && campaignObj.getCompanyId().length()>0){
											engineObj.setPublisherId(StringUtil.getLongValue(campaignObj.getCompanyId()));
										}
										if(placementObj.getPlacementName()!=null && placementObj.getPlacementName().trim().length()>0){
											engineObj.setPlacementName(placementObj.getPlacementName().trim());
										}
										if(targetPacing!=0){
											engineObj.setDailyPacingExpected(targetPacing);
										}
										if(currentPacing!=0){
											engineObj.setDailyPacingCurrent(currentPacing);
										}
										if(revisedPacing!=0){
											engineObj.setDailyPacingReviced(revisedPacing);
										}
										engineObj.setDailyPacingAlert(pacingAlert);
										
										if(targetCTR!=0.0){
											engineObj.setGoalCTRExpected(targetCTR);
										}
										if(currentCTR!=0.0){
											engineObj.setGoalCTRCurrent(currentCTR);
										}
										engineObj.setGoalCTRAlert(ctrAlert);
										
										if(targetDelivery!=0){
											engineObj.setDeliveryExpected(targetDelivery);
										}
										if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1){
											engineObj.setDeliveryCurent(impressionDelivered);
										}else if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
											engineObj.setDeliveryCurent(clicksDelivered);
										}
										engineObj.setDeliveyAlert(deliveryAlert);
										engineObj.setAlertDate(DateUtil.getDateYYYYMMDD());
										if(alertDate != null){
											engineObj.setAlertDate(DateUtil.getRequiredFormatDate(alertDate, "yyyy-MM-dd", "yyyy-MM-dd"));
										}
										if(alertDate != null && placementObj.getPlacementId()!=null ){
											log.info("alertDate = "+alertDate);
											engineObj.setId(alertDate+"_"+placementObj.getPlacementId());
										}else{
											engineObj.setId(currentDate+"_"+placementObj.getPlacementId());
										}
										engineObj.setStartDate(DateUtil.getFormatedDate(campaignObj.getStartDate(), "MM-dd-yyyy", "yyyy-MM-dd"));
										engineObj.setEndDate(DateUtil.getFormatedDate(campaignObj.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd"));
										engineObj.setRateType(campaignObj.getRateTypeList().get(0).getObjectId());
										engineObj.setCampaignStatus(StringUtil.getIntegerValue(campaignObj.getCampaignStatus(), 0));
										engineObj.setPlacementGoal(placementGoal);
										if(campaignObj.getCompanyId()!=null && campaignObj.getCompanyId().length()>0){
											engineObj.setCompanyId(campaignObj.getCompanyId());
										}
										if(placementObj.getFlightObjList()!=null && placementObj.getFlightObjList().size()>0){
												log.info("flight level Calculations starts"+placementObj.getFlightObjList().size());
												
															for (SmartCampaignFlightDTO flightDTO : placementObj.getFlightObjList()) {
																long flightDaysRemaining = 0;
																long flightFutureDays = 0;
																
																if(alertDate != null){
																	log.info(flightDTO.getStartDate()+"====="+flightDTO.getEndDate());
																	flightDaysRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getFormatedDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), flightDTO.getEndDate(), "MM-dd-yyyy");
																	flightFutureDays =  DateUtil.getDifferneceBetweenTwoDates(DateUtil.getFormatedDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), flightDTO.getStartDate(), "MM-dd-yyyy");
																	log.info("inside if......flightDaysRemaining..>>="+flightDaysRemaining+" flightFutureDays.."+flightFutureDays);
																}
																else
																{
																	flightDaysRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("MM-dd-yyyy"), flightDTO.getEndDate(), "MM-dd-yyyy");
																	flightFutureDays =  DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("MM-dd-yyyy"), flightDTO.getStartDate(), "MM-dd-yyyy");
																	log.info("inside else......flightDaysRemaining..>>="+flightDaysRemaining+" flightFutureDays.."+flightFutureDays);
																}
																
																if(flightDaysRemaining>=-1 && flightFutureDays<=1){
																	log.info("flightDaysRemaining>=0 && flightFutureDays<=0");
																long flightTotalDays = DateUtil.getDifferneceBetweenTwoDates(flightDTO.getStartDate(), flightDTO.getEndDate(), "MM-dd-yyyy");
																long flightDaysPassed = 0;
																if(alertDate != null)
																{
																	flightDaysPassed = DateUtil.getDifferneceBetweenTwoDates(flightDTO.getStartDate(), DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"), "MM-dd-yyyy");
																	log.info("inside if......"+flightDaysPassed);
																	log.info("DateUtil.getJustPreviousDate = "+DateUtil.getJustPreviousDate(alertDate, "yyyy-MM-dd", "MM-dd-yyyy"));
																	log.info("getYesterdayDateString = "+DateUtil.getYesterdayDateString("MM-dd-yyyy"));
																}else
																{
																	flightDaysPassed = DateUtil.getDifferneceBetweenTwoDates(flightDTO.getStartDate(), DateUtil.getYesterdayDateString("MM-dd-yyyy"), "MM-dd-yyyy");
																	log.info("inside else......"+flightDaysPassed);
																}
																
																
																flightTotalDays = flightTotalDays+1;
																flightDaysPassed = flightDaysPassed+1;
																flightDaysRemaining = flightDaysRemaining+1;
																
																log.info("flight total days ......................"+flightTotalDays);
																log.info("flight passed days ......................"+flightDaysPassed);
																log.info("flight days remaining......................"+flightDaysRemaining);
																
																AlertEngineFlightObj engineFlightObj = new AlertEngineFlightObj();
																long flightGoal = StringUtil.getLongValue(flightDTO.getGoal());
																long flightTargetDelivery = 0;
																long flightImpressionsDelivered = 0;
																long flightClicksDelivered = 0;
																long flightPacingCurrent = 0;
																long flightPacingExpected = 0;
																long flightPacingRevised = 0;
																int flightDeliveryAlert = 0;
																int flightPacingAlert = 0;
																int flightId = 0;
																String flightEndDate = "";
																
																if(alertDate != null){
																	flightEndDate = justPreviousDate;
																	log.info("justPreviousDate : "+justPreviousDate);
																}else{
																	flightEndDate = DateUtil.getFormatedDate(flightDTO.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd");
																}
																
																String flightStartDate = DateUtil.getFormatedDate(flightDTO.getStartDate(), "MM-dd-yyyy", "yyyy-MM-dd");
																if(flightDTO!=null && flightDTO.getFlightId()!=0){
																	flightId = flightDTO.getFlightId();
																}
																double flightDeliveryMargin = 0.0;
																double flightPacingMargin = 0.0;
																	for (DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOList) {
																		log.info("dfpLineItemDTOList size = "+dfpLineItemDTOList.size());
																		if(dfpLineItemDTO!=null){
																			PerformanceMonitoringDTO lineItemObj = lineItemMap.get(dfpLineItemDTO.getLineItemId()+"");
																			List<PerformanceMonitoringDTO> lineItemDataList = new ArrayList<>();
																
																			queryDTO = new QueryDTO();
																			queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
																			if(lineItemObj !=null){
																				
																				log.info("lineItemObj : "+lineItemObj.toString());
																				if(lineItemObj.getLineItemId() !=null){
																					lineItemDataList = dao.loadAllLineItemsForFlights(lineItemObj.getLineItemId(),
																							flightStartDate, flightEndDate, queryDTO);
																				 }
																				
																				if(lineItemDataList!=null && lineItemDataList.size()>0){
																					
																						for (PerformanceMonitoringDTO performanceMonitoringDTO : lineItemDataList) {
																							if(performanceMonitoringDTO!=null){
																								if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==1){
																									log.info("Campaign type is CPM");
																									flightImpressionsDelivered = flightImpressionsDelivered+StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
																									log.info("flightImpressionsDelivered = "+flightImpressionsDelivered);

																									flightPacingExpected = (flightGoal/flightTotalDays);
																									log.info("flightPacingExpected = "+flightPacingExpected);
																									if(flightDaysPassed!=0){
																										log.info("flightCurrentPacing if");
																										flightPacingCurrent = (flightImpressionsDelivered/flightDaysPassed);
																										log.info("flightPacingCurrent = "+flightPacingCurrent);
																									}
																									if(flightDaysRemaining!=0){
																										log.info("flightPacingRevised if");
																										flightPacingRevised = ((flightGoal-flightImpressionsDelivered)/flightDaysRemaining);
																										log.info("flightPacingRevised = "+flightPacingRevised);
																									}
																									
																									if(flightPacingExpected!=0 && flightDaysPassed!=0){
																										log.info("flight target Delivery if");
																										flightTargetDelivery = flightPacingExpected*flightDaysPassed;
																										log.info("flightTargetDelivery = "+flightTargetDelivery);
																									}
																									if(flightTargetDelivery>flightImpressionsDelivered && flightTargetDelivery!=0){
																										flightDeliveryMargin = (double)((flightImpressionsDelivered*100)/flightTargetDelivery);
																									}else if(flightImpressionsDelivered>flightTargetDelivery && flightImpressionsDelivered!=0){
																										flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightImpressionsDelivered);
																									}else{
																										flightDeliveryMargin = 0.0;
																									}
																									
																									
																									
																								}else if(campaignObj.getRateTypeList()!=null && campaignObj.getRateTypeList().size()>0 && campaignObj.getRateTypeList().get(0).getObjectId()!=0 && campaignObj.getRateTypeList().get(0).getObjectId()==2){
																									log.info("Campaign type is CPC");
																									flightClicksDelivered = flightClicksDelivered+StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
																									log.info("flightClicksDelivered = "+flightClicksDelivered);
																									
																									flightPacingExpected = (flightGoal/flightTotalDays);
																									if(flightDaysPassed!=0){
																										flightPacingCurrent = (flightClicksDelivered/flightDaysPassed);
																									}
																									if(flightDaysRemaining!=0){
																										flightPacingRevised = ((flightGoal-flightClicksDelivered)/flightDaysRemaining);
																									}

																									if(flightPacingExpected!=0 && flightDaysPassed!=0){
																										flightTargetDelivery = flightPacingExpected*flightDaysPassed;
																									}
																									if(flightTargetDelivery>flightClicksDelivered && flightTargetDelivery!=0){
																										flightDeliveryMargin = (double)((flightClicksDelivered*100)/flightTargetDelivery);
																									}else if(flightClicksDelivered>flightTargetDelivery && flightClicksDelivered!=0){
																										flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightClicksDelivered);
																									}else{
																										flightDeliveryMargin = 0.0;
																									}
																								}
																							}
																						}
																				}
																			}else{
																				log.info("lineItemObj is null");
																			}																
																			log.info("inside flight calculation date if block");																			
																		}else{
																			log.info(">>>>>dfpLineItemDTO is null");
																		}
																		
																		log.info("Now setting flightDeliveryAlert.....");
																		if(flightTargetDelivery>flightImpressionsDelivered && flightTargetDelivery!=0){
																			flightDeliveryMargin = (double)((flightImpressionsDelivered*100)/flightTargetDelivery);
																		}else if(flightImpressionsDelivered>flightTargetDelivery && flightImpressionsDelivered!=0){
																			flightDeliveryMargin = (double)((flightTargetDelivery*100)/flightImpressionsDelivered);
																		}else{
																			flightDeliveryMargin = 0.0;
																		}
															
																		if(flightDeliveryMargin>2.0){
																			flightDeliveryAlert = 1;
																		}else{
																			flightDeliveryAlert = 0;
																		}
																		
																		
																		if(flightPacingExpected>flightPacingCurrent && flightPacingExpected!=0){
																			flightPacingMargin = (double)((flightPacingCurrent*100)/flightPacingExpected);
																		}else if(flightPacingExpected<flightPacingCurrent && flightPacingCurrent!=0){
																			flightPacingMargin = (double)((flightPacingExpected*100)/flightPacingCurrent);
																		}else{
																			flightPacingMargin = 0;
																		}
																		
																		if(flightPacingMargin>2.0){
																			flightPacingAlert = 1;
																		}else{
																			flightPacingAlert = 0;
																		}
																		
															
																		if(flightImpressionsDelivered!=0){
																			engineFlightObj.setDeliveryCurent(flightImpressionsDelivered);
																			engineObj.setFlightDeliveryCurent(flightImpressionsDelivered);
																		}
																		if(flightTargetDelivery!=0){
																			engineFlightObj.setDeliveryExpected(flightTargetDelivery);
																			engineObj.setFlightDeliveryExpected(flightTargetDelivery);
																		}
																		if(flightDeliveryAlert!=0){
																			engineFlightObj.setDeliveyAlert(flightDeliveryAlert);
																			engineObj.setFlightDeliveyAlert(flightDeliveryAlert);
																		}
																		if(flightPacingAlert!=0){
																			engineFlightObj.setPacingAlert(flightPacingAlert);
																		}
																		if(placementObj.getPlacementId()!=null){
																			engineFlightObj.setPlacementId(placementObj.getPlacementId());
																		}
																		if(flightId!=0){
																			engineFlightObj.setFlightId(flightId);
																			engineObj.setFlightId(flightId);
																		}
																		if(flightStartDate!=null){
																			engineFlightObj.setStartDate(flightStartDate);
																			engineObj.setFlightStartDate(flightStartDate);
																		}
																		
																		if(flightDTO.getEndDate()!=null){
																			String endData = DateUtil.getFormatedDate(flightDTO.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd");

																			engineFlightObj.setEndDate(endData);
																			engineObj.setFlightEndDate(endData);
																		}
																		
																		if(flightPacingExpected!=0){
																			engineFlightObj.setFlightDailyPacingExpected(flightPacingExpected);
																			engineObj.setFlightDailyPacingExpected(flightPacingExpected);
																		}
																		if(flightPacingCurrent!=0){
																			engineFlightObj.setFlightDailyPacingCurrent(flightPacingCurrent);
																			engineObj.setFlightDailyPacingCurrent(flightPacingCurrent);
																		}
																		if(flightPacingRevised!=0){
																			engineFlightObj.setFlightDailyPacingRevised(flightPacingRevised);
																			engineObj.setFlightDailyPacingRevised(flightPacingRevised);
																		}
																		if(flightGoal!=0){
																			engineFlightObj.setFlightGoal(flightGoal);
																			engineObj.setFlightGoal(flightGoal);
																		}
																		engineFlightObj.setAlertDate(DateUtil.getDateYYYYMMDD());
																		
																		log.info("going to save AlertEngineFlightObj");
																	/*	if(engineObj!=null){
																			dao.saveObject(engineObj);
																		}*/
																		
																	/*	if(engineFlightObj!=null){
																			dao.saveObject(engineFlightObj);
																			log.info(" save AlertEngineFlightObj success");
																		}*/
															
																	}
																break;	
													
															}
										}
												
										}
										
										if(engineObj!=null){
											dao.saveObject(engineObj);
										}
										
									}
								}
							}
							
						}
						}
					}
				}
			}else{
				log.info("Invalid QueryDTO...."+queryDTO);
			}
		}catch(Exception e){
			log.severe("Exception in AlertEngine of AlertEngineService : "+e.getMessage());
			
		}
	
		return publisherIdInBQ;
		
	}
	
	
	/*Author : Dheeraj Kumar
	 * get list of alerts generated today.
	 * (non-Javadoc)
	 * @see com.lin.web.service.IAlertEngineService#campaignStatusAlertMail(long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Map<Long,Map> campaignStatusAlertMail() throws Exception{
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		IAlertEngineDAO alertEngineDao = new AlertEngineDAO();
		
		List<AlertEngineObj> alertEngineObjList = new ArrayList<AlertEngineObj>();
		Map<Long,List<AlertEngineObj>> campaignMap = new HashMap<Long,List<AlertEngineObj>>();
		//Map<Long,byte[]> campaignAlertMap = new HashMap<Long,byte[]>();
		Map<Long,Map> campaignAlertMap = new HashMap<Long,Map>();
		
	try{
		alertEngineObjList  = alertEngineDao.getAllCampaigns(DateUtil.getDateYYYYMMDD());
		
		if(alertEngineObjList != null && alertEngineObjList.size() > 0)
		{
			for(AlertEngineObj alertEngineObj : alertEngineObjList)
			{
				if(campaignMap.containsKey(alertEngineObj.getPublisherId()))
				{
					List<AlertEngineObj> alertEngineList = campaignMap.get(alertEngineObj.getPublisherId());
					alertEngineList.add(alertEngineObj);
					campaignMap.put(alertEngineObj.getPublisherId(), alertEngineList);
				}
				else
				{
					List<AlertEngineObj> alertEngineList = new ArrayList<AlertEngineObj>();
					alertEngineList.add(alertEngineObj);
					campaignMap.put(alertEngineObj.getPublisherId(), alertEngineList);
				}
				
			}
		}else{
			log.info("No alert found.");
		}
		
		log.info("campaignMap = "+campaignMap);
		if(campaignMap != null && campaignMap.size() > 0)
		{
			for(Long publisherId : campaignMap.keySet())
			{	
				List<CampaignStatusAlertDTO> campaignStatusAlertDTOList = new ArrayList<CampaignStatusAlertDTO>();
				List<AlertEngineObj> alertEngineList = campaignMap.get(publisherId);
				for(AlertEngineObj alertEngineObj : alertEngineList)
				{
					CampaignStatusAlertDTO campaignStatusAlertDTO = new CampaignStatusAlertDTO();
					campaignStatusAlertDTO.setAlertDate(DateUtil.getFormatedDate(alertEngineObj.getAlertDate(), "MM-dd-yyyy"));
					campaignStatusAlertDTO.setCampaignName(alertEngineObj.getCampaignName());
					campaignStatusAlertDTO.setPlacement(alertEngineObj.getPlacementName());
					campaignStatusAlertDTO.setPublisherName(alertEngineObj.getPublisherName());
					/*CompanyObj companyObj = userDAO.getCompanyById(alertEngineObj.getPublisherId(), MemcacheUtil.getAllCompanyList());
					if(companyObj != null) {
						campaignStatusAlertDTO.setPublisherName(companyObj.getCompanyName());
					}*/
					
					String alertMessage = getAlertMessage(alertEngineObj);
					
					log.info("alertMessage = "+alertMessage.toString());
					campaignStatusAlertDTO.setExceptions(alertMessage.toString());
					campaignStatusAlertDTO.setRowColor(rowColor);
					if(alertMessage != null && !alertMessage.equals("")){
					campaignStatusAlertDTOList.add(campaignStatusAlertDTO);
					}
				
				}
				Map map = new HashMap();
				map.put("CampaignStatusAlertDTO", campaignStatusAlertDTOList);
				/*ExcelReportGenerator erGen = new ExcelReportGenerator();
			    byte[] excelbytes = null;
			    excelbytes = erGen.advertiserReportGenerate("CampaignStatusAlertReport.xls",map);
				campaignAlertMap.put(publisherId, excelbytes);*/
			    campaignAlertMap.put(publisherId, map);
			}
	}
				
		}catch(Exception e){
			log.severe("Exception in campaignStatusAlertMail of AlertEngineService : "+e.toString());
			
		}
	
		//return campaignStatusAlertDTOList;
		return campaignAlertMap;
		
	}
	
	/*Author : Dheeraj Kumar
	 * send mail to all users who have opted for email alert
	 * (non-Javadoc)
	 * @see com.lin.web.service.IAlertEngineService#sendCampaignStatusAlertMail(byte[])
	 */
	@Override
	public void sendCampaignStatusAlertMail(long publisherId, Map map, List<PerformanceMonitoringDTO> campaignList) throws Exception{
		
		IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		List<UserDetailsObj> userDetailsObjList = new ArrayList<UserDetailsObj>();
		
		try{
				UserDetailsDAO dao = new UserDetailsDAO();
				List<RolesAndAuthorisation>  rolesAndAuthorisationList = dao.getAllRolesAndAuthorisation();
				userDetailsObjList = userService.getAllActiveUsersOfCompany(publisherId);
				
				if(userDetailsObjList != null && userDetailsObjList.size() > 0)
				{
					log.info("userDetailsObjList.size() = "+userDetailsObjList.size());
					for(UserDetailsObj userDetailsObj : userDetailsObjList)
					{
						log.info(userDetailsObj.getEmailId()+"====="+userDetailsObj.isOptEmail());
						if(userDetailsObj.isOptEmail()){
						String toEmail = userDetailsObj.getEmailId();
						String fromEmail = LinMobileVariables.SENDER_EMAIL_ADDRESS;
						
						log.info("toEmail = "+toEmail);
						log.info("fromEmail = "+fromEmail);
						
						boolean superAdmin = false;
						String roleName = dao.getRoleNameByRoleId(userDetailsObj.getRole(), rolesAndAuthorisationList);
						log.info("user role is :"+roleName);
						if(roleName.trim().equalsIgnoreCase(LinMobileConstants.ADMINS[0])) {
							superAdmin = true;
						}

						byte[] excelbytes = null;
						List<PerformanceMonitoringAlertDTO> alertDTOList = loadAllCampaigns(campaignList, "1", userDetailsObj.getId(), superAdmin);
						if(alertDTOList != null && alertDTOList.size() > 0){
							log.info("alertDTOList.size() in sendCampaignStatusAlertMail = "+alertDTOList.size());
							map.put("PerformanceMonitoringAlertDTO", alertDTOList);
						}
						
						ExcelReportGenerator erGen = new ExcelReportGenerator();
					    excelbytes = erGen.advertiserReportGenerate("CampaignStatusAlertReport.xls",map);
						
						String reportDate = DateUtil.getCurrentTimeStamp("MM-dd-YYYY");
						EmailUtil.sendMailWithAttachment(toEmail, fromEmail, "LIN Mobile Daily Pacing Report - "+reportDate, "Campaign Status Report file is attached with this mail.", excelbytes, "DailyCampaignStatus_"+reportDate, " Please find the attachment.");
						}
					}
					
					/*String toEmail = "dheeraj.it.java@gmail.com";
					String fromEmail = LinMobileVariables.SENDER_EMAIL_ADDRESS;
					String content = excelbytes.toString();
					
					log.info("toEmail = "+toEmail);
					log.info("fromEmail = "+fromEmail);
					log.info("content = "+content);
					
					String reportDate = DateUtil.getCurrentTimeStamp("MM-dd-YYYY");
					EmailUtil.sendMailWithAttachment(toEmail, fromEmail, "LIN Mobile Daily Pacing Report - ", "Campaign Status Report file is attached with this mail.", excelbytes, "DailyCampaignStatus_"+reportDate, " Please find the attachment.");*/
				}
				
				
		}catch(Exception e){
			log.severe("Exception in sendCampaignStatusAlertMail of AlertEngineService : "+e.toString());
			
		}
	
		
	}
	
	/*
	 * @author Shubham Goel 
	 * This method will get alerts related to the user and show on alert listing screen....
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray loadAllCampaignAlert(long userId, boolean isSuperAdmin, int campaignPerPage, int pageNumber, String dateSearch, String campaignSearch, String placementSearch, String publisherSearch){
		AlertEngineDAO alertDAO = new AlertEngineDAO();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		JSONArray jsonArray = new JSONArray();		
		List<AlertEngineObj> alertList = new ArrayList<>();
	    try{
			String campaignStatus = CampaignStatusEnum.All.ordinal()+"";
			String alertKey = "ALL_ALERT_LIST_KEY_"+userId;
			
			
			if(campaignStatus!=null) {
				if(isSuperAdmin) {
					//campaignList = campaignDAO.getSmartCampaignListSuperUser(campaignStatus);
					//alertList = (List<AlertEngineObj>) MemcacheUtil.getObjectsListFromCache(alertKey);
				//	if(alertList==null || alertList.size()<=0){
						alertList = alertDAO.getAllCampaignAlertSuperUser();
						//Collections.sort(alertList, Collections.reverseOrder());
						//MemcacheUtil.setObjectsListInCache(alertList, alertKey);
					//}
				}
				else {
				 List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
					if(companyObjList != null && companyObjList.size() > 0) {
						for(CompanyObj companyObj : companyObjList) {
							if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
									//campaignList = campaignDAO.getSmartCampaignList(campaignStatus, companyObj.getId()+"");
								//alertList = (List<AlertEngineObj>) MemcacheUtil.getObjectsListFromCache(alertKey);
							//	if(alertList==null || alertList.size()<=0){
									alertList = alertDAO.getAllCampaignAlert(companyObj.getId()+"");
									//Collections.sort(alertList, Collections.reverseOrder());
								//	MemcacheUtil.setObjectsListInCache(alertList, alertKey);
								//}
								break;
							}
						}
					}
				}
			}
				
		/*	for (SmartCampaignObj smartCampaignObj : campaignList) {
				if(smartCampaignObj!=null && smartCampaignObj.getCampaignStatus()!=null && !smartCampaignObj.getCampaignStatus().equals("4")&& !smartCampaignObj.getCampaignStatus().equals("6")) {
					campaignIdList.add(smartCampaignObj.getCampaignId());
				}
			}
			
			if(alertList==null || alertList.size()<=0){
				alertList = alertDAO.getAllCampaignAlert(campaignIdList);
				Collections.sort(alertList, Collections.reverseOrder());
				MemcacheUtil.setObjectsListInCache(alertList, alertKey);
			}*/
			if(alertList!=null && alertList.size()>0){
				log.info("loaded alertList :"+alertList.size());
				int start = 0;
				int end = 0;
				
				if(dateSearch.length() > 0 || campaignSearch.length() > 0 || placementSearch.length() > 0 || publisherSearch.length() > 0) {
					end = alertList.size()-1;
					log.info("dateSearch :"+dateSearch+" campaignSearch : "+campaignSearch+" placementSearch : "+placementSearch+"publisherSearch : "+publisherSearch);
					log.info("start : "+start+", end : "+end);
					for(int i=start;i<=end;i++) {
						if(alertList.size() > i) {
							AlertEngineObj alertEngineObj = alertList.get(i);
							log.info("loaded alertDate   :"+DateUtil.getFormatedStringDateMMDDYYYY(alertEngineObj.getAlertDate()));
							String alertDateStr = DateUtil.getFormatedStringDateMMDDYYYY(alertEngineObj.getAlertDate());
							if((dateSearch.length() > 0 && alertEngineObj.getAlertDate() != null && (alertDateStr).contains(dateSearch))
									||(campaignSearch.length() > 0 && alertEngineObj.getCampaignName() != null && (alertEngineObj.getCampaignName().toLowerCase()).contains(campaignSearch))
									||(placementSearch.length() > 0 && alertEngineObj.getPlacementName() != null && (alertEngineObj.getPlacementName().toLowerCase()).contains(placementSearch))
									||(publisherSearch.length() > 0 && alertEngineObj.getPublisherName() != null && (alertEngineObj.getPublisherName().toLowerCase()).contains(publisherSearch))){
									
								JSONObject jsonObject = getAletJSONObject(alertEngineObj);
								if(jsonObject!=null){
									jsonArray.add(jsonObject);
								}
									
							}
						}
					}
					
				}
				else{
					start = (pageNumber - 1)*campaignPerPage;
					end = (pageNumber*campaignPerPage) - 1;
					log.info("start : "+start+", end : "+end);
					for(int i=start;i<=end;i++) {
						if(alertList.size() > i) {
							AlertEngineObj alertEngineObj = alertList.get(i);
							log.info("loaded alertDate   :"+alertEngineObj.getAlertDate());
							JSONObject jsonObject = getAletJSONObject(alertEngineObj);
								if(jsonObject!=null){
									jsonArray.add(jsonObject);
								}
						}
					}
				}
				
			}else{
				log.info("Alert List iS empty for this user........... : "+userId);
			}
			
		}catch(Exception e){
			log.severe("Exception in loadAllCampaignAlert of AlertEngineService : "+e.getMessage());
			
		}
		return jsonArray;
		
	}
	
	public JSONObject getAletJSONObject(AlertEngineObj alertEngineObj){
		JSONObject jsonObject = new JSONObject();
		try{
			if(alertEngineObj!=null){
				String date =DateUtil.getFormatedDate(alertEngineObj.getAlertDate(), "MM-dd-yyyy");
				String alertMessage = getAlertMessage(alertEngineObj);
				if(alertMessage!=null && alertMessage.length()>0 ){
					jsonObject.put("id", alertEngineObj.getId());
					jsonObject.put("date",date);
					jsonObject.put("campaignName",alertEngineObj.getCampaignName());
					jsonObject.put("placementName",alertEngineObj.getPlacementName());
					jsonObject.put("publisherName",alertEngineObj.getPublisherName());
					jsonObject.put("exception",alertMessage.toString());
					
					//jsonArray.add(jsonObject);
				}
				 
			}
		}catch(Exception e){
			log.severe("Exception in getAletJSONObject of AlertEngineService : "+e.getMessage());
			
		}
		
		return jsonObject;
		
	}
	
	/* Author : Shubham Goel
	 * (non-Javadoc)
	 * @see com.lin.web.service.IAlertEngineService#getAlertMessage(com.lin.server.bean.AlertEngineObj)
	 */
	 @Override
	 public String getAlertMessage(AlertEngineObj alertEngineObj) throws Exception
	 {
	 String alertMessage = "";
	 //long dayRemaining = DateUtil.getDifferneceBetweenTwoDates(DateUtil.getCurrentTimeStamp("yyyy-MM-dd"), alertEngineObj.getEndDate(), "yyyy-MM-dd");
	 String alertDateStr = DateUtil.getFormatedStringDateYYYYMMDD(alertEngineObj.getAlertDate());
	 log.info("alertDateStr :"+alertDateStr+"campaignId = "+alertEngineObj.getCampaignId());
	 long dayRemaining = DateUtil.getDifferneceBetweenTwoDates(alertDateStr, alertEngineObj.getFlightEndDate(), "yyyy-MM-dd");
	 dayRemaining = dayRemaining+1;
	 log.info("dayRemaining : "+dayRemaining);
	 log.info("campaign End Date : "+alertEngineObj.getEndDate());
	 if(dayRemaining>=0){

/*		 SmartCampaignPlannerDAO campaignDao = new SmartCampaignPlannerDAO();
		 long placementId = alertEngineObj.getPlacementId();
		 List<AlertEngineFlightObj> flightList = campaignDao.getAllAlertEngineFlightObj(placementId);
		 for(AlertEngineFlightObj flightObj : flightList){
			 
			 Date flightStartDate = DateUtil.getFormatedDate(flightObj.getStartDate(), "yyyy-MM-dd");
			 Date flightEndDate = DateUtil.getFormatedDate(flightObj.getEndDate(), "yyyy-MM-dd");
			 Date flightAlertDate = flightObj.getAlertDate();
			 
			 if(flightAlertDate.getTime() >= flightStartDate.getTime() && flightAlertDate.getTime() <= flightEndDate.getTime())
			 {*/
				 if(/*alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Completed.ordinal() ||*/ dayRemaining==0 ){
				 alertMessage = completedCampaignMessage(alertEngineObj);
				 }else if(alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Canceled.ordinal()){
					 alertMessage = canceledCampaignMessage(alertEngineObj);
				 }else if((alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Running.ordinal() || 
				 alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Active.ordinal()) && dayRemaining>0 
				 && alertEngineObj.getFlightDeliveryCurent()>alertEngineObj.getFlightGoal()){
				 alertMessage = completedGoalCampaignMessage(alertEngineObj);	
				 }else if(alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Running.ordinal() || alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Completed.ordinal() ||
				 alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Active.ordinal()){
				 alertMessage = runningCampaignMessage(alertEngineObj);
				 }else if(alertEngineObj.getCampaignStatus()==CampaignStatusEnum.Paused.ordinal()){
				 alertMessage = pausedCampaignMessage(alertEngineObj);
				 }
		/*	}
		 }*/
	 }

	 return alertMessage;
	 }
	
	 
		/*
		 * @author Shubham Goel 
		 * This method will generate alerts for paused campaigns...
		 */
		
		public String pausedCampaignMessage(AlertEngineObj alertEngineObj){
			
			log.info("In pausedCampaignMessage() of AlertMessageService");
			
			StringBuilder alertMessage = new StringBuilder();
			alertMessage.append("Campaign is Paused ");
			rowColor = "red";
			return alertMessage.toString();
			
		}
		
		/*
		 * @author Shubham Goel 
		 * This method will generate alerts for canceled  campaigns...
		 */
		
		public String canceledCampaignMessage(AlertEngineObj alertEngineObj){
			
			log.info("In canceledCampaignMessage() of AlertMessageService");
			
			StringBuilder alertMessage = new StringBuilder();
			alertMessage.append("Campaign is Canceled ");
			rowColor = "red";
			return alertMessage.toString();
			
		}
		
		/*
		 * @author Shubham Goel 
		 * This method will generate alerts for completed campaigns...
		 */
		public String completedCampaignMessage(AlertEngineObj alertEngineObj){
			
			log.info("In completedCampaignMessage() of AlertMessageService");
			
			StringBuilder alertMessage = new StringBuilder();
			 NumberFormat nf = NumberFormat.getInstance();
			if(alertEngineObj!=null){
				if(alertEngineObj.getFlightDeliveryCurent() >= alertEngineObj.getFlightGoal()){
					alertMessage.append("Campaign Ends, Delivered Full ");
					rowColor="green";
				}else{
					//if(alertEngineObj.getFlightGoal()!=0){
						alertMessage.append("Campaign ends , Under delivered by "+nf.format((alertEngineObj.getPlacementGoal()-alertEngineObj.getDeliveryCurent())));
					/*}else{
						alertMessage.append("Campaign ends , Under delivered by "+nf.format((alertEngineObj.getDeliveryExpected()-alertEngineObj.getFlightDeliveryCurent())));
					}*/
					
					if(alertEngineObj.getRateType()==1){
						alertMessage.append(" Imps");
					}else if(alertEngineObj.getRateType()==2){
						alertMessage.append(" Clicks");
					}
					rowColor="red";
				}
			}
			
			return alertMessage.toString();
			
		}
		
		/*
		 * @author Shubham Goel 
		 * This method will generate alerts for running campaigns...
		 */
		String runningCampaignMessage(AlertEngineObj alertEngineObj){
			
			log.info("In runningCampaignMessage() of AlertMessageService");
			
			StringBuilder alertMessage = new StringBuilder();
			long dailyPacingCurrent = alertEngineObj.getFlightDailyPacingCurrent();
			long dailyPacingExpected = alertEngineObj.getFlightDailyPacingExpected();
			long dailyPacingRevised = alertEngineObj.getFlightDailyPacingRevised();
			float goalCTRCurrent = alertEngineObj.getGoalCTRCurrent();
			float goalCTRExpected = alertEngineObj.getGoalCTRExpected();
			 NumberFormat nf = NumberFormat.getInstance();
			 DecimalFormat df_one = new DecimalFormat("##.##");
			
			double percentagePacing = 0;
			if(dailyPacingExpected >0){
				percentagePacing = (double)(((double)dailyPacingCurrent - (double)dailyPacingExpected) * 100 / (double)dailyPacingExpected);
			}
			log.info("percentagePacing in runningCampaignMessage for "+alertEngineObj.getPlacementName()+" = "+percentagePacing);
			if(percentagePacing <= 5.0 && percentagePacing >= -5.0)
			{
				alertMessage.append(" Pacing on Schedule.");
				rowColor="green";
			}else if(percentagePacing > 5.0 && percentagePacing > -5.0){
				alertMessage.append("Over Pacing.");
				alertMessage.append(" Need to deliver at "+df_one.format((double)dailyPacingRevised/1000.0)+" K");
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day ");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day ");
				}
				
				alertMessage.append("(revised pacing "+nf.format(dailyPacingRevised));
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day");
				}
				alertMessage.append(").");
				
				alertMessage.append(" Currently pacing at "+nf.format(dailyPacingCurrent));
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day.");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day.");
				}
				
				alertMessage.append(" Initial Daily pacing goal was about "+nf.format(dailyPacingExpected));
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day.");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day.");
				}
				
				rowColor = "red";
			}else if(percentagePacing < -5.0){
				alertMessage.append("Pacing Low.");
				alertMessage.append(" Currently at "+nf.format(dailyPacingCurrent));
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day.");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day.");
				}
				alertMessage.append(" Need to ramp up "+nf.format(dailyPacingRevised));
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps/Day.");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks/Day.");
				}
				alertMessage.append("");
				rowColor = "red";
			}
			
			if(goalCTRCurrent < goalCTRExpected)
			{
				alertMessage.append("CTR should be "+nf.format(goalCTRExpected)+"% , Currently "+nf.format(goalCTRCurrent)+"%.");
				rowColor = "red";
			}
			
			return alertMessage.toString();
		}
			
		/*
		 * @author Shubham Goel 
		 * This method will generate alerts for completed campaigns...
		 */
		public String completedGoalCampaignMessage(AlertEngineObj alertEngineObj){
			
			log.info("In completedCampaignMessage() of AlertMessageService");
			
			StringBuilder alertMessage = new StringBuilder();
			alertMessage.append("Campaign Delivered Full, End Date "+DateUtil.getFormatedDate(alertEngineObj.getFlightEndDate(), "yyyy-MM-dd", "MM-dd-yyyy"));
			rowColor="green";
			return alertMessage.toString();
			
		}
	/* 
	
	 * @author Shubham Goel 
	 * This method will generate alerts for paused campaigns...
	 
	
	public String pausedCampaignMessage(AlertEngineObj alertEngineObj){
		
		log.info("In pausedCampaignMessage() of AlertMessageService");
		
		StringBuilder alertMessage = new StringBuilder();
		alertMessage.append("Campaign is Paused ");
		rowColor = "red";
		return alertMessage.toString();
		
	}
	
	
	 * @author Shubham Goel 
	 * This method will generate alerts for completed campaigns...
	 
	public String completedCampaignMessage(AlertEngineObj alertEngineObj){
		
		log.info("In completedCampaignMessage() of AlertMessageService");
		
		StringBuilder alertMessage = new StringBuilder();
		 NumberFormat nf = NumberFormat.getInstance();
		if(alertEngineObj!=null){
			if(alertEngineObj.getDeliveryCurent() >= alertEngineObj.getDeliveryExpected()){
				alertMessage.append("Campaign Ends, Delivered Full ");
				rowColor="green";
			}else{
				if(alertEngineObj.getPlacementGoal()!=0){
					alertMessage.append("Campaign ends , Under delivered by "+nf.format((alertEngineObj.getPlacementGoal()-alertEngineObj.getDeliveryCurent())));
				}else{
					alertMessage.append("Campaign ends , Under delivered by "+nf.format((alertEngineObj.getDeliveryExpected()-alertEngineObj.getDeliveryCurent())));
				}
				
				if(alertEngineObj.getRateType()==1){
					alertMessage.append(" Imps");
				}else if(alertEngineObj.getRateType()==2){
					alertMessage.append(" Clicks");
				}
				rowColor="red";
			}
		}
		
		return alertMessage.toString();
		
	}
	
	
	 * @author Shubham Goel 
	 * This method will generate alerts for running campaigns...
	 
	String runningCampaignMessage(AlertEngineObj alertEngineObj){
		
		log.info("In runningCampaignMessage() of AlertMessageService");
		
		StringBuilder alertMessage = new StringBuilder();
		long dailyPacingCurrent = alertEngineObj.getDailyPacingCurrent();
		long dailyPacingExpected = alertEngineObj.getDailyPacingExpected();
		long dailyPacingRevised = alertEngineObj.getDailyPacingReviced();
		float goalCTRCurrent = alertEngineObj.getGoalCTRCurrent();
		float goalCTRExpected = alertEngineObj.getGoalCTRExpected();
		 NumberFormat nf = NumberFormat.getInstance();
		 DecimalFormat df_one = new DecimalFormat("##.##");
		
		double percentagePacing = 0;
		if(dailyPacingExpected >0){
			percentagePacing = (double)(((double)dailyPacingCurrent - (double)dailyPacingExpected) * 100 / (double)dailyPacingExpected);
		}
		log.info("percentagePacing in runningCampaignMessage for "+alertEngineObj.getPlacementName()+" = "+percentagePacing);
		if(percentagePacing <= 5.0 && percentagePacing >= -5.0)
		{
			alertMessage.append(" Pacing on Schedule.");
			rowColor="green";
		}else if(percentagePacing > 5.0 && percentagePacing > -5.0){
			alertMessage.append("Over Pacing.");
			alertMessage.append(" Need to deliver at "+df_one.format((double)dailyPacingRevised/1000.0)+" K");
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day ");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day ");
			}
			
			alertMessage.append("(revised pacing "+nf.format(dailyPacingRevised));
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day");
			}
			alertMessage.append(").");
			
			alertMessage.append(" Currently pacing at "+nf.format(dailyPacingCurrent));
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day.");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day.");
			}
			
			alertMessage.append(" Initial Daily pacing goal was about "+nf.format(dailyPacingExpected));
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day.");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day.");
			}
			
			rowColor = "red";
		}else if(percentagePacing < -5.0){
			alertMessage.append("Pacing Low.");
			alertMessage.append(" Currently at "+nf.format(dailyPacingCurrent));
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day.");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day.");
			}
			alertMessage.append(" Need to ramp up "+nf.format(dailyPacingRevised));
			if(alertEngineObj.getRateType()==1){
				alertMessage.append(" Imps/Day.");
			}else if(alertEngineObj.getRateType()==2){
				alertMessage.append(" Clicks/Day.");
			}
			alertMessage.append("");
			rowColor = "red";
		}
		
		if(goalCTRCurrent < goalCTRExpected)
		{
			alertMessage.append("CTR should be "+nf.format(goalCTRExpected)+"% , Currently "+nf.format(goalCTRCurrent)+"%.");
			rowColor = "red";
		}
		
		return alertMessage.toString();
	}
		
	
	 * @author Shubham Goel 
	 * This method will generate alerts for completed campaigns...
	 
	public String completedGoalCampaignMessage(AlertEngineObj alertEngineObj){
		
		log.info("In completedCampaignMessage() of AlertMessageService");
		
		StringBuilder alertMessage = new StringBuilder();
		alertMessage.append("Campaign Delivered Full, End Date "+DateUtil.getFormatedDate(alertEngineObj.getEndDate(), "yyyy-MM-dd", "MM-dd-yyyy"));
		rowColor="green";
		return alertMessage.toString();
		
	}*/
	
	/*Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IAlertEngineService#loadAllRunningCampaigns(java.lang.String)
	 * getting all the campaigns from BQ
	 */
	public List<PerformanceMonitoringDTO> loadAllRunningCampaigns(String publisherIdInBQ) {
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<>();
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		QueryDTO queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
		if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) 
		{
			campaignList = dao.loadAllCampaigns(queryDTO);
		}
		return campaignList;
	}
	
	
	/*Author : Dheeraj Kumar
	 * (non-Javadoc)
	 * @see com.lin.web.service.IAlertEngineService#loadAllCampaigns(java.util.List, java.lang.String, long, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceMonitoringAlertDTO> loadAllCampaigns(List<PerformanceMonitoringDTO> campaignList, String campaignStatus, long userId, boolean superAdmin) {
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		List<SmartCampaignObj> dataStoreCampaignList = new ArrayList<>();
		Map<String,SmartCampaignObj> campaignStatusMap = new HashMap<>();
		List<PerformanceMonitoringAlertDTO>  alertDTOList = new ArrayList<PerformanceMonitoringAlertDTO>();
		SmartCampaignPlannerDAO campaignDao = new SmartCampaignPlannerDAO();
		double goal = 0.0;
		double goalProgress = 0.0;
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		Date currentDate = DateUtil.getDateYYYYMMDDHHMMSS();
		String currentDateStr = DateUtil.getFormatedDate(currentDate, "MM-dd-yyyy");
		DecimalFormat df_two = new DecimalFormat("###.##");
		DecimalFormat df_one = new DecimalFormat("###.#");
		
		try{
				if(campaignList!=null && campaignList.size()>0)
				{
					log.info("campaignList.size() in loadAllCampaigns of AlertEngineService..."+campaignList.size());
					if(superAdmin) 
					{
						dataStoreCampaignList = dao.getRunningCampaignListSuperUser(campaignStatus);
					}
					else 
					{
						List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
						if(companyObjList != null && companyObjList.size() > 0) {
							for(CompanyObj companyObj : companyObjList) {
								if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
									dataStoreCampaignList = dao.getRunningCampaignList(campaignStatus, companyObj.getId()+"");
									break;
								}
							}
						}
					}
					
					if(dataStoreCampaignList!=null && dataStoreCampaignList.size()>0 ){
						for (SmartCampaignObj smartCampaignObj : dataStoreCampaignList) {
							if(smartCampaignObj!=null && smartCampaignObj.getCampaignStatus().equals("2") /*&& !smartCampaignObj.getCampaignStatus().equals("4")*/){
								campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
							}
							
						}
						
						
						
						for (PerformanceMonitoringDTO campaignObj : campaignList) {
							if(campaignObj!=null && campaignObj.getOrderId()!=null && campaignStatusMap.containsKey(campaignObj.getOrderId()) ){
								double progressBarMargin = 0.0;
								Map<String,String> partnerMap = new HashMap<>();
								PerformanceMonitoringAlertDTO alertDTO = new PerformanceMonitoringAlertDTO();
								SmartCampaignObj obj = new SmartCampaignObj();
								obj = campaignStatusMap.get(campaignObj.getOrderId());
								if(obj.getCampaignStatus()!=null){
									String cStatus = obj.getCampaignStatus();
									String status=CampaignStatusEnum.values()[Integer.parseInt(cStatus)].name();
									alertDTO.setStatus(status);
								}
								
								/*if(obj.getCampaignId()!=null){
									alertDTO.setCampaignId(obj.getCampaignId()+"");
								}*/
							
								placementByIdList = campaignDao.getAllPlacementOfCampaign(obj.getCampaignId());
								String partner = "";
								if(placementByIdList!=null && placementByIdList.size()>0){
									goal = 0.0;
									
									for (SmartCampaignPlacementObj placementObj : placementByIdList) {
										 
										if(placementObj!=null && placementObj.getBudget()!=null && !placementObj.getBudget().equals("") && placementObj.getImpressions()!=null && !placementObj.getImpressions().equals("")){
											String imp = placementObj.getImpressions().trim().replaceAll(",","");
											goal = goal+Long.parseLong(imp);
										}
										
										if(placementObj!=null && placementObj.getDfpLineItemList()!=null && placementObj.getDfpLineItemList().size()>0){
											for (DFPLineItemDTO lineItemDTOObj : placementObj.getDfpLineItemList()) {
												if(lineItemDTOObj!=null && lineItemDTOObj.getPartner()!=null && lineItemDTOObj.getPartner().length()>0){
													partnerMap.put(lineItemDTOObj.getPartner(),lineItemDTOObj.getPartner());
												}
											}
										}
										
									}
									if(partnerMap!=null && partnerMap.size()>0){
										 Iterator iterator = partnerMap.entrySet().iterator();
											while (iterator.hasNext()) {
												Map.Entry mapEntry = (Map.Entry) iterator.next();
												partner = partner+mapEntry.getValue()+",";
												iterator.remove();
												//partner
										}
											partner = StringUtil.deleteFromLastOccurence(partner, ",");
									}
								}
								alertDTO.setPartner(partner);
								Date campaignEndDate = null;  
								double dateProgress = 0.0;
								
								if(obj.getStartDate()!=null && obj.getStartDate().length()>0 && obj.getEndDate()!=null && obj.getEndDate().length()>0){
									campaignEndDate = DateUtil.getRequiredFormatDate(obj.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd HH:mm:ss");
									int dateFlag = campaignEndDate.compareTo(DateUtil.getDateYYYYMMDDHHMMSS());
									log.info("DateFlag = "+dateFlag);
									if(dateFlag>=0){
										 dateProgress = durationInPercentage(obj.getStartDate(), obj.getEndDate());
										 long totalDays = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
										 alertDTO.setTotalDays(totalDays+1+"");
										 long daysTillDate = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), currentDateStr, "MM-dd-yyyy");
										 alertDTO.setDaysTillDate(daysTillDate+"");
									}else{
										 long totalDays = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
										 alertDTO.setTotalDays(totalDays+1+"");
										 dateProgress = durationInPercentage(obj.getStartDate(), obj.getEndDate());
										 if(dateProgress>100){
											 dateProgress = 100; 
										 }
										 long daysTillDate = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
										 alertDTO.setDaysTillDate(daysTillDate+1+"");
									}
								}
								
								if(obj.getRateTypeList()!=null && obj.getRateTypeList().size()>0){
									if(obj.getRateTypeList().get(0).getObjectId()!=0 && obj.getRateTypeList().get(0).getObjectId()==1){
										log.info("Campaign type is CPM");
										alertDTO.setRateType("CPM");
										 goalProgress = goalInPercentage(goal, Double.parseDouble(campaignObj.getImpressionDelivered()));
										 log.info("goalProgress for Campaign type  CPM"+goalProgress);
									}else if(obj.getRateTypeList().get(0).getObjectId()!=0 && obj.getRateTypeList().get(0).getObjectId()==2){
										log.info("Campaign type is CPC");
										alertDTO.setRateType("CPC");
										 goalProgress = goalInPercentage(goal, Double.parseDouble(campaignObj.getClicks()));
										 log.info("goalProgress for Campaign type  CPC"+goalProgress);
									}
								}
								
								if(dateProgress>goalProgress){
									progressBarMargin = dateProgress-goalProgress;
									log.info("progressBarMargin = "+ progressBarMargin);
								}else{
									progressBarMargin = goalProgress-dateProgress;
									log.info("progressBarMargin = "+ progressBarMargin);
								}
								
								alertDTO.setDateProgress(Math.round(dateProgress)+"");
								alertDTO.setGoalProgress(df_two.format(goalProgress)+"");
								alertDTO.setGoal(df_two.format(goal)+"");
								alertDTO.setId(campaignObj.getOrderId());
								
								if(obj.getName()!=null && obj.getName().length()>0){
									alertDTO.setName(obj.getName());
								}
								
								alertDTO.setDelivered(campaignObj.getImpressionDelivered());
								alertDTO.setClicks(campaignObj.getClicks());
								alertDTO.setCtr(df_two.format(Double.parseDouble(campaignObj.getCTR()))+"%");
								if(obj.getStartDate()!=null && obj.getStartDate().length()>0 && obj.getEndDate()!=null && obj.getEndDate().length()>0){
									alertDTO.setDate(obj.getStartDate()+" - "+obj.getEndDate());
								}
								//jsonObject.put("dfpNetworkCode", mediaPlanDAO.loadMediaPlan(campaignObj.getOrderId().toString()).getDfpPlacements().get(0).getPartnerDFPNetworkCode());
								
								alertDTO.setDfpNetworkCode(obj.getAdServerId());
								alertDTO.setDfpStatus(obj.getDfpStatus());
								StringBuilder progressMessage = new StringBuilder("");
								double delivered = Double.parseDouble(alertDTO.getDelivered());
								double clicks = Double.parseDouble(alertDTO.getClicks());
								double gol = Double.parseDouble(alertDTO.getGoal());
								
								if(alertDTO.getRateType()!= null && alertDTO.getRateType().equalsIgnoreCase("CPM")){
									progressMessage.append(alertDTO.getDateProgress()+"% Time Completed ");
									progressMessage.append("("+alertDTO.getDaysTillDate()+" out of "+alertDTO.getTotalDays()+" Days) ");
									progressMessage.append(alertDTO.getGoalProgress()+"% Goal Completed ");
									progressMessage.append("("+df_two.format(delivered/1000000)+"M out of "+df_two.format(gol/1000000)+"M impressions)");
									
								}else if(alertDTO.getRateType()!= null && alertDTO.getRateType().equalsIgnoreCase("CPC")){
									progressMessage.append(alertDTO.getDateProgress()+"% Time Completed ");
									progressMessage.append("("+alertDTO.getDaysTillDate()+" out of "+alertDTO.getTotalDays()+" Days) ");
									progressMessage.append(alertDTO.getGoalProgress()+"% Goal Completed ");
									progressMessage.append("("+df_one.format(clicks/1000)+"K out of "+df_one.format(gol/1000)+"K Clicks)");
								}else{
									progressMessage.append(alertDTO.getDateProgress()+"% Time Completed ");
									progressMessage.append("("+alertDTO.getDaysTillDate()+" out of "+alertDTO.getTotalDays()+" Days) ");
									progressMessage.append(alertDTO.getGoalProgress()+"% Goal Completed ");
									progressMessage.append("("+df_one.format(clicks/1000)+"K out of "+df_one.format(gol/1000)+"K Clicks)");
								}
								alertDTO.setProgressMessage(progressMessage.toString());
								
								alertDTOList.add(alertDTO);
								
							}
						}
					}
				}
			
		}catch(Exception e){
			log.severe("Exception in loadAllCampaigns of AlertEngineService : "+e.toString());
			
		}
		//return jsonArray;
		log.info("alertDTOList.size() in loadAllCampaigns() of AlertEngineService..."+alertDTOList.size());
		return alertDTOList;
	}
	
	/*Author : Dheeraj Kumar
	 * getting duration in Percentage
	 */
	public double durationInPercentage(String startDate, String endDate) {
		double totalDays = 0;
		double daysTillDate = 0;
		double percentDays = 0.0;
		if(startDate!=null && startDate.length()>0 && endDate!=null && endDate.length()>0){
			 totalDays = DateUtil.getDifferneceBetweenTwoDates(startDate, endDate, "MM-dd-yyyy");
			 totalDays = totalDays+1;
		}
		Date currentDate = DateUtil.getDateYYYYMMDDHHMMSS();
		String currentDateStr = DateUtil.getFormatedDate(currentDate, "MM-dd-yyyy");
		if(startDate!=null && startDate.length()>0 && currentDateStr!=null && currentDateStr.length()>0){
			daysTillDate = DateUtil.getDifferneceBetweenTwoDates(startDate, currentDateStr, "MM-dd-yyyy");
		}
		if(totalDays!=0 && daysTillDate!=0){
			percentDays = (daysTillDate/totalDays)*100;
		}
		return percentDays; 
		
	}
	
	/*Author : Dheeraj kumar
	 * getting goal in percentage
	 */
	public double goalInPercentage(double totalImpression, double deliveredImpression){
		double percentageImpression = 0.0;
		if(totalImpression!=0 && deliveredImpression!=0){
			percentageImpression = (deliveredImpression/totalImpression)*100;
		}
		return percentageImpression;
	}
	

}
