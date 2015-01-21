package com.lin.web.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.google.api.client.util.Lists;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.NumberValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.lin.persistance.dao.IPerformanceMonitoringDAO;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.PerformanceMonitoringDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.LineChartDTO;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.RichMediaDataDTO;
import com.lin.web.dto.SmartCampaignFlightDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.GoogleVisulizationUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.RUNDspUtil;
import com.lin.web.util.StringUtil;


public class PerformanceMonitoringService implements IPerformanceMonitoringService{
	 enum lagendColor {
			Red, green, orange, blue, yellow , bronze , aqua , gold , darkred ,gray,cyan,amaranth,bole,buff,cadet,carmine,ceil,coral,corn
		}
	private static final Logger log = Logger.getLogger(PerformanceMonitoringService.class.getName());
	private static final int EXPIRATION_TIME = 60*15;
	private static final String VIDEO_CREATIVE_SETS = "Video creative sets";
	private static final String DOUBLECLICK_RICH_MEDIA = "DoubleClick Rich Media";
	
	@Override
	/*
	 * This method will calculate number of days from startDate(if startDate is not null and lies in flight Dates) 
	 * to the endDate(if endDate is not null and lies in flight Dates)
	 * @author Naresh Pokhriyal
	 * 
	 * @param   List<SmartCampaignFlightDTO> flightObjList, String startDate, String endDate, String dateFormat
	 *        
	 * @return long numberOfDays
	 */
	public long dayDurationInFlights(List<SmartCampaignFlightDTO> flightObjList, String startDate, String endDate, String dateFormat) {
		long numberOfDays = 0;
		try {
			if(flightObjList != null && flightObjList.size() > 0) {
				flightObjList = copyFlights(flightObjList);
				if(startDate != null && startDate.trim().length() > 0) {
					flightObjList = flightsFromStartDate(flightObjList, startDate, dateFormat);
				}
				if(endDate != null && endDate.trim().length() > 0) {
					flightObjList = flightsToEndDate(flightObjList, endDate, dateFormat);
				}
				List<SmartCampaignFlightDTO> clashLessFlights = new ArrayList<>();
				if(flightObjList != null && flightObjList.size() > 0) {
					for (SmartCampaignFlightDTO smartCampaignFlightDTO : flightObjList) {
						if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
							if(clashLessFlights.size() > 0) {
								int clashIndex = flightClashIndex(clashLessFlights, smartCampaignFlightDTO, dateFormat);
								if(clashIndex >= 0) {			// if clash
									manageFlightClash(clashLessFlights, smartCampaignFlightDTO, clashIndex, dateFormat);
									if(clashLessFlights.size() > 1) {
										for(int i=0; i<clashLessFlights.size(); i++) {
											clashIndex = flightClashIndex(clashLessFlights, clashLessFlights.get(i), dateFormat);
											if(clashIndex >= 0) {
												manageFlightClash(clashLessFlights, clashLessFlights.get(i), clashIndex, dateFormat);
												clashLessFlights.remove(i);
												i=-1;
											}
											if(clashLessFlights.size() == 1) {
												break;
											}
										}
									}
								}
								else if(clashIndex == -100) {	// duplicate Campaign
									continue;
								}else {
									clashLessFlights.add(smartCampaignFlightDTO);
								}
							}else {
								clashLessFlights.add(smartCampaignFlightDTO);
							}
						}
					}
					if(clashLessFlights != null && clashLessFlights.size() > 0) {
						for (SmartCampaignFlightDTO smartCampaignFlightDTO : clashLessFlights) {
							if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
								numberOfDays = numberOfDays + (DateUtil.getDifferneceBetweenTwoDates(smartCampaignFlightDTO.getStartDate(), smartCampaignFlightDTO.getEndDate(), dateFormat))+1;
							}
						}
					}
				}
			}else {
				log.info("Flights are empty");
			}
		} catch (ParseException e) {
			log.severe("ParseException in dayDurationInFlights in PerformanceMonitoringService"+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in dayDurationInFlights in PerformanceMonitoringService"+e.getMessage());
			e.printStackTrace();
		}
		log.info("numberOfDays for flights : "+numberOfDays);
		return numberOfDays;
	}
	
	private List<SmartCampaignFlightDTO> copyFlights(List<SmartCampaignFlightDTO> flightObjList) throws Exception {
		List<SmartCampaignFlightDTO> copyList = new ArrayList<>();
		if(flightObjList != null && flightObjList.size() > 0) {
			for (SmartCampaignFlightDTO flight : flightObjList) {
				SmartCampaignFlightDTO smartCampaignFlightDTO = new SmartCampaignFlightDTO(flight.getFlightName(), flight.getStartDate(), flight.getEndDate(), flight.getGoal());
				copyList.add(smartCampaignFlightDTO);
			}
		}
		return copyList;
	}

	private List<SmartCampaignFlightDTO> flightsToEndDate(List<SmartCampaignFlightDTO> flightObjList, String endDate, String dateFormat) throws ParseException, Exception {
		List<SmartCampaignFlightDTO> flightList = new ArrayList<>();
		try {
			if(flightObjList != null && flightObjList.size() > 0 && endDate != null && endDate.trim().length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        	Date deadLine = sdf.parse(endDate);
	        	
				for (SmartCampaignFlightDTO smartCampaignFlightDTO : flightObjList) {
					if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
						Date flightStartDate = sdf.parse(smartCampaignFlightDTO.getStartDate());
						Date flightEndDate = sdf.parse(smartCampaignFlightDTO.getEndDate());
						if(flightStartDate.compareTo(deadLine)<=0 && flightEndDate.compareTo(deadLine)<=0) {
							flightList.add(smartCampaignFlightDTO);
						}
						else if(flightStartDate.compareTo(deadLine)<=0) {
							smartCampaignFlightDTO.setEndDate(endDate);
							flightList.add(smartCampaignFlightDTO);
						}
					}
				}
			}
		} catch (ParseException e) {
			log.severe("ParseException in flightsToEndDate in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.severe("Exception in flightsToEndDate in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		return flightList;
	}
	
	private List<SmartCampaignFlightDTO> flightsFromStartDate(List<SmartCampaignFlightDTO> flightObjList, String startDate, String dateFormat) throws ParseException, Exception {
		List<SmartCampaignFlightDTO> flightList = new ArrayList<>();
		try {
			if(flightObjList != null && flightObjList.size() > 0 && startDate != null && startDate.trim().length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        	Date startLine = sdf.parse(startDate);
	        	
				for (SmartCampaignFlightDTO smartCampaignFlightDTO : flightObjList) {
					if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
						Date flightStartDate = sdf.parse(smartCampaignFlightDTO.getStartDate());
						Date flightEndDate = sdf.parse(smartCampaignFlightDTO.getEndDate());
						if(flightStartDate.compareTo(startLine)>=0 && flightEndDate.compareTo(startLine)>=0) {
							flightList.add(smartCampaignFlightDTO);
						}
						else if(flightStartDate.compareTo(startLine)<=0 && flightEndDate.compareTo(startLine)>=0) {
							smartCampaignFlightDTO.setStartDate(startDate);
							flightList.add(smartCampaignFlightDTO);
						}
					}
				}
			}
		} catch (ParseException e) {
			log.severe("ParseException in flightsFromStartDate in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.severe("Exception in flightsFromStartDate in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		return flightList;
	}

	private int flightClashIndex(List<SmartCampaignFlightDTO> clashLessFlights, SmartCampaignFlightDTO smartCampaignFlightDTO, String dateFormat) throws ParseException, Exception {
		try {
			if(clashLessFlights != null && clashLessFlights.size() > 0 && smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
				for (int i = 0; i < clashLessFlights.size(); i++) {
					SmartCampaignFlightDTO clashLessFlight = clashLessFlights.get(i);
					if(clashLessFlight != null && clashLessFlight.getStartDate() != null && clashLessFlight.getEndDate() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
						
			        	Date clashStart = sdf.parse(clashLessFlight.getStartDate());
			        	Date otherStart = sdf.parse(smartCampaignFlightDTO.getStartDate());
			        	Date clashEnd = sdf.parse(clashLessFlight.getEndDate());
			        	Date otherEnd = sdf.parse(smartCampaignFlightDTO.getEndDate());
						if(clashStart.compareTo(otherStart)<0 && clashEnd.compareTo(otherStart)<0) {
							// No clash
							continue;
			        	}
						else if(otherStart.compareTo(clashStart)<0 && otherEnd.compareTo(clashStart)<0) {
							// No clash
							continue;
			        	}
						else if(otherStart.compareTo(clashStart)==0 && otherEnd.compareTo(clashEnd)==0) {
							// duplicate
							return -100;
			        	}
						else {
							// clash
							return i;
						}
					}
				}
			}
		} catch (ParseException e) {
			log.severe("ParseException in flightClashIndex in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.severe("Exception in flightClashIndex in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		return -1;
	}
	
	private void manageFlightClash(List<SmartCampaignFlightDTO> clashLessFlights, SmartCampaignFlightDTO smartCampaignFlightDTO, int clashIndex, String dateFormat) throws ParseException, Exception {
		try {	
			if(clashLessFlights != null && clashLessFlights.size() >= (clashIndex + 1) && smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
				SmartCampaignFlightDTO clashFlight = clashLessFlights.get(clashIndex);
				if(clashFlight != null && clashFlight.getStartDate() != null && clashFlight.getEndDate() != null) {
					SmartCampaignFlightDTO newFlight = new SmartCampaignFlightDTO(clashFlight.getFlightName(), clashFlight.getStartDate(), clashFlight.getEndDate(), clashFlight.getGoal());
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					
		        	Date clashStart = sdf.parse(newFlight.getStartDate());
		        	Date otherStart = sdf.parse(smartCampaignFlightDTO.getStartDate());
					if(clashStart.compareTo(otherStart)>0){
						newFlight.setStartDate(smartCampaignFlightDTO.getStartDate());
		        	}
					
					Date clashEnd = sdf.parse(newFlight.getEndDate());
					Date otherEnd = sdf.parse(smartCampaignFlightDTO.getEndDate());
					if(clashEnd.compareTo(otherEnd)<0) {
						newFlight.setEndDate(smartCampaignFlightDTO.getEndDate());
		        	}
					clashLessFlights.add(clashIndex, newFlight);
					clashLessFlights.remove(clashIndex+1);
				}
			}
		} catch (ParseException e) {
			log.severe("ParseException in manageFlightClash in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
		catch (Exception e) {
			log.severe("Exception in manageFlightClash in PerformanceMonitoringService"+e.getMessage());
			throw e;
		}
	}

	private String createJsonResponseForLineChart(Map<String, Object> dateMap,
			Map<String, Object> siteMap,Map<String, Object> perfDataMap,String chartType) throws TypeMismatchException{
		
		DataTable linechartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription("date", ValueType.DATE, "Date");
	    linechartTable.addColumn(col0);

	    for (String site : siteMap.keySet()) {
			  ColumnDescription sitecol = new ColumnDescription(site, ValueType.NUMBER, site);
			  linechartTable.addColumn(sitecol);
		}
	    rows = Lists.newArrayList();	
	    int year, month, day = 0;	
	    for (String date : dateMap.keySet()) {
			  String [] dtArray=date.split("-");
			  com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
			  
			  year = Integer.parseInt(dtArray[0]);
			  month = Integer.parseInt(dtArray[1])-1;
			  day = Integer.parseInt(dtArray[2]);
			  DateValue dateValue= new DateValue(year,month,day);
			  
			  row.addCell(new com.google.visualization.datasource.datatable.TableCell(dateValue));
			  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(date));
			  
			  for (String site : siteMap.keySet()) {
				  if(siteMap.get(site) != null) {
					  if(chartType.equalsIgnoreCase("CTR")) {
						  Double ctr=StringUtil.getDoubleValue(siteMap.get(site)+"", 4);
						  row.addCell(new NumberValue(new Double(ctr)));
					  }
					  else if(chartType.equalsIgnoreCase("Clicks")) {
						  row.addCell(new NumberValue(new Long(StringUtil.getLongValue(siteMap.get(site)+""))));
					  }
					  else if(chartType.equalsIgnoreCase("Impressions")) {
						  row.addCell(new NumberValue(new Long(StringUtil.getLongValue(siteMap.get(site)+""))));
					  }
					  else{
						  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
					  }
					  continue;
				  }
				  String key=date+"_"+site;
				  LineChartDTO perData = (LineChartDTO) perfDataMap.get(key);					 
				  if ( perData  != null){
					  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(perData.getCtr()).);						  
					  if(chartType.equalsIgnoreCase("CTR")){
						  Double ctr=StringUtil.getDoubleValue(perData.getCtr(), 4);
						  row.addCell(new NumberValue(new Double(ctr)));  
					  }else if(chartType.equalsIgnoreCase("Clicks")){
						  row.addCell(new NumberValue(new Long(perData.getClicks()))); 
					  }else if(chartType.equalsIgnoreCase("Impressions")){
						  row.addCell(new NumberValue(new Long(perData.getImpressions()))); 
					  }
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  }
				  
			  }
			  rows.add(row);				  
	  }
	  linechartTable.addRows(rows);
	  
	  java.lang.CharSequence jsonStr =	 JsonRenderer.renderDataTable(linechartTable, true, false);
	  //System.out.println("LineChart Table JSON Data::" + jsonStr.toString());
	  return jsonStr.toString();
	}
	
	@Override
	public Map<String, String> getPlacementInformation(Long campaignId, long userId, boolean isSuperAdmin) {
		Map<String, String> placementInfo = new HashMap<>();
		try {
			String companyName = "";
			boolean isAuthorised = false;
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			SmartCampaignObj smartCampaignObj = smartCampaignPlannerDAO.getCampaignById(campaignId);
			String campaignType = "CPM";
			
			if(smartCampaignObj != null && smartCampaignObj.getDfpOrderId()  > 0) {
				IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
				CompanyObj companyObj = userService.getCompanyById(StringUtil.getLongValue(smartCampaignObj.getCompanyId()), MemcacheUtil.getAllCompanyList());
				if(companyObj != null) {
					companyName = companyObj.getCompanyName();
				}
				if(isSuperAdmin) {
					isAuthorised = true;
				}
				else {
					// check if campaign is authorised by account to user
					isAuthorised = true;			// remove this line when account check code below is uncommented.
					/*Map<String,String> accountDataMap = userService.getSelectedAccountsByUserId(userId, true, true);
					String advertiserId = smartCampaignObj.getAdvertiserId();
					String agencyId = smartCampaignObj.getAgencyId();
					if((advertiserId != null && UserService.isAuthorisedAccountId(advertiserId, accountDataMap)) || 
							(agencyId != null && UserService.isAuthorisedAccountId(agencyId, accountDataMap))) {
						IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
						List<CompanyObj> companyObjList = userDetailsDAO.getSelectedCompaniesByUserId(userId);
						if(companyObjList != null && companyObjList.size() > 0) {
							companyObj = companyObjList.get(0);
							if(companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && smartCampaignObj.getCompanyId().equals(companyObj.getId()+"")) {
								isAuthorised = true;
							}
						}
					}else {
						//log.info("Campaign Account is not authorised, campaignId : "+smartCampaignObj.getCampaignId()+
							//	", advertiserId : "+advertiserId+", agencyId : "+agencyId);
					}*/
				}
			}
			
			if(isAuthorised) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("campaignId", campaignId);
				jsonObject.put("orderId", smartCampaignObj.getDfpOrderId()+"");
				jsonObject.put("orderName", smartCampaignObj.getName());
				if(smartCampaignObj.getRateTypeList() != null && smartCampaignObj.getRateTypeList().size() > 0) {
					if(smartCampaignObj.getRateTypeList().get(0).getValue().contains("CPC")) {
						campaignType = "CPC";
					}
					else if(smartCampaignObj.getRateTypeList().get(0).getValue().contains("CPD")) {
						campaignType = "CPD";
					}
				}
				jsonObject.put("campaignType", campaignType);
				jsonObject.put("startDate", smartCampaignObj.getStartDate());
				jsonObject.put("endDate", smartCampaignObj.getEndDate());
				placementInfo.put("orderInfo", jsonObject.toString());
				
				List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(campaignId);
				if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
					//log.info(smartCampaignPlacementObjList.toString());
					String campaignPacing = "All";
					if(smartCampaignPlacementObjList.size()==1 && smartCampaignPlacementObjList.get(0).getPacing() != null && LinMobileUtil.isNumeric(smartCampaignPlacementObjList.get(0).getPacing())) {
						campaignPacing = smartCampaignPlacementObjList.get(0).getPacing();
					}
					List<SmartCampaignFlightDTO> allFlightObjList = new ArrayList<>();
					JSONArray tempPlacementInfoJsonArray = new JSONArray();
					JSONObject placementInfoJSONObject = new JSONObject();
					long impressionOrClicks = 0;
					JSONObject lineItemPartner = new JSONObject();
					JSONObject lineItemPlacementName = new JSONObject();
					JSONObject lineItemPlacementIds = new JSONObject();
					String campaignGoalCTR = "NA";
					String allLineItemIds = "";
					double budget = 0.0;
					for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
						if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlacementId() != null && smartCampaignPlacementObj.getPlacementName() != null && smartCampaignPlacementObj.getDfpLineItemList() != null && smartCampaignPlacementObj.getDfpLineItemList().size() > 0) {
							jsonObject = new JSONObject();
							jsonObject.put("campaignId", campaignId);
							jsonObject.put("placementId", smartCampaignPlacementObj.getPlacementId());
							jsonObject.put("placementName", smartCampaignPlacementObj.getPlacementName());
							jsonObject.put("campaignType", campaignType);
							String imp = "0";
							if(smartCampaignPlacementObj.getImpressions() != null) {
								if(LinMobileUtil.isNumeric(smartCampaignPlacementObj.getImpressions().replaceAll(",", ""))) {
									imp = smartCampaignPlacementObj.getImpressions().replaceAll(",", "");
									impressionOrClicks = impressionOrClicks + StringUtil.getLongValue(imp);
								}
							}
							jsonObject.put("impressionOrClicks", imp);		// goal
							double bgt =0;
							if(smartCampaignPlacementObj.getBudget() != null && LinMobileUtil.isNumeric(smartCampaignPlacementObj.getBudget())) {
								bgt = StringUtil.getDoubleValue(smartCampaignPlacementObj.getBudget());
								budget = budget + bgt;
							}
							jsonObject.put("budget", bgt);
							double rate = 0.0;
							if(smartCampaignPlacementObj.getRate() != null && LinMobileUtil.isNumeric(smartCampaignPlacementObj.getRate())) {
								rate = StringUtil.getDoubleValue(smartCampaignPlacementObj.getRate());
							}
							jsonObject.put("rate", rate);
							
							String goal = "NA";
							if(smartCampaignPlacementObj.getGoal() != null && LinMobileUtil.isNumeric(smartCampaignPlacementObj.getGoal())) {
								goal = smartCampaignPlacementObj.getGoal();
								if(!LinMobileUtil.isNumeric(campaignGoalCTR)) {
									campaignGoalCTR = goal;
								}
							}
							jsonObject.put("goalCTR", goal);
							
							String pacing = "NA";
							if(smartCampaignPlacementObj.getPacing() != null && LinMobileUtil.isNumeric(smartCampaignPlacementObj.getPacing())) {
								pacing = smartCampaignPlacementObj.getPacing();
							}
							jsonObject.put("pacing", pacing);
							
							String lineItemIds = "";
							List<DFPLineItemDTO> dfpLineItemList = smartCampaignPlacementObj.getDfpLineItemList();
							if(dfpLineItemList != null && dfpLineItemList.size() > 0) {
								for(DFPLineItemDTO dfpLineItemDTO : dfpLineItemList) {
									if(dfpLineItemDTO != null && dfpLineItemDTO.getLineItemId() >0) {
										if(dfpLineItemDTO.getPartner() == null || dfpLineItemDTO.getPartner().trim().length() == 0) {
											lineItemPartner.put(dfpLineItemDTO.getLineItemId(), companyName);
										}else {
											lineItemPartner.put(dfpLineItemDTO.getLineItemId(), dfpLineItemDTO.getPartner());
										}
										lineItemIds = lineItemIds + dfpLineItemDTO.getLineItemId()+"','";
										lineItemPlacementName.put(dfpLineItemDTO.getLineItemId(), smartCampaignPlacementObj.getPlacementName());
										lineItemPlacementIds.put(dfpLineItemDTO.getLineItemId(), smartCampaignPlacementObj.getPlacementId());
									}
								}
							}
							allLineItemIds = allLineItemIds + lineItemIds;
							lineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds, "','");
							jsonObject.put("lineItemIds", lineItemIds);
							
							jsonObject.put("startDate", smartCampaignPlacementObj.getStartDate());
							jsonObject.put("endDate", smartCampaignPlacementObj.getEndDate());
							
							long durationInDays = 0;
							if(smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
								allFlightObjList.addAll(smartCampaignPlacementObj.getFlightObjList());
								durationInDays = dayDurationInFlights(smartCampaignPlacementObj.getFlightObjList(), null, null, "MM-dd-yyyy");
								
							}
							jsonObject.put("durationInDays", durationInDays);
							
							tempPlacementInfoJsonArray.add(jsonObject);
							placementInfoJSONObject.put(smartCampaignPlacementObj.getPlacementId(), jsonObject);
						}
					}
					
					JSONArray placementInfoArray = new JSONArray();
					if(tempPlacementInfoJsonArray.size() > 0) {
						JSONObject tempJson = new JSONObject();
						double rate = 0.0;
						if(impressionOrClicks > 0 && campaignType.equals("CPM")) {
							rate = (double)((budget*1000)/impressionOrClicks);
						}
						else if(impressionOrClicks > 0 && campaignType.equals("CPC")) {
							rate = (double)(budget/impressionOrClicks);
						}
						
						allLineItemIds = StringUtil.deleteFromLastOccurence(allLineItemIds, "','");
						tempJson.put("campaignId", campaignId);
						tempJson.put("placementId", "All");
						tempJson.put("placementName", "All");
						tempJson.put("campaignType", campaignType);
						tempJson.put("impressionOrClicks", impressionOrClicks);
						tempJson.put("budget", budget);
						tempJson.put("rate", rate);
						tempJson.put("goalCTR", campaignGoalCTR);
						tempJson.put("pacing", campaignPacing);
						tempJson.put("lineItemIds", allLineItemIds);
						tempJson.put("startDate", smartCampaignObj.getStartDate());
						tempJson.put("endDate", smartCampaignObj.getEndDate());
						long durationInDays = 0;
						if(allFlightObjList != null && allFlightObjList.size() > 0) {
							durationInDays = dayDurationInFlights(allFlightObjList, null, null, "MM-dd-yyyy");
							
						}
						tempJson.put("durationInDays", durationInDays);
						
						placementInfoArray.add(tempJson);
						placementInfoArray.addAll(tempPlacementInfoJsonArray);
						placementInfoJSONObject.put("All", tempJson);
					}
					else {
						log.info("tempPlacementInfoJsonArray is Empty");
					}
					placementInfo.put("placementInfo", placementInfoJSONObject.toString());
					placementInfo.put("placementInfoArray", placementInfoArray.toString());
					placementInfo.put("partnerInfo", lineItemPartner.toString());
					placementInfo.put("lineItemPlacementIds", lineItemPlacementIds.toString());
					placementInfo.put("lineItemPlacementName", lineItemPlacementName.toString());
				}
				else {
					log.info("smartCampaignPlacementObjList is Empty");
				}
			}
			else {
				log.info("Unauthorised User");
				placementInfo.put("isAuthorised","0");
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in getPlacementInformation of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in getPlacementInformation of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		log.info("placementInfo size : "+placementInfo.size());
		return placementInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray loadAllCampaigns(String campaignStatus, long userId,boolean superAdmin, int campaignPerPage, int pageNumber,String searchKeyword, String publisherIdInBQ) {
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		List<PerformanceMonitoringDTO> campaignList = new ArrayList<>();
		Map<String,String> companyNameMap = new HashMap<>();
		List<SmartCampaignObj> dataStoreCampaignList = new ArrayList<>();
		Map<String,SmartCampaignObj> campaignStatusMap = new HashMap<>();
		JSONArray jsonArray = new JSONArray();
		SmartCampaignPlannerDAO campaignDao = new SmartCampaignPlannerDAO();
		double goal = 0.0;
		double goalProgress = 0.0;
		int expirationTime = 0;
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		try{
			QueryDTO queryDTO = BigQueryUtil.getQueryDTO(publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			Date currentDate = DateUtil.getDateYYYYMMDDHHMMSS();
			String currentDateStr = DateUtil.getFormatedDate(currentDate, "MM-dd-yyyy");
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				String key = "CAMPAIGN_OBJ_LIST_KEY_BY_PUBLISHERID"+publisherIdInBQ;
				campaignList = (List<PerformanceMonitoringDTO>) MemcacheUtil.getObjectsListFromCache(key);
				if(campaignList==null || campaignList.size()<=0 ){
					campaignList = dao.loadAllCampaigns(queryDTO);
					String memcacheKey = "CAMPAIGN_OBJ_LIST_KEY_BY_PUBLISHERID"+publisherIdInBQ;
					expirationTime = 900;
					if(!MemcacheUtil.setObjectsListInCacheWithExpirationErrorFree(campaignList, memcacheKey, expirationTime)){
						log.severe("Memcache Failed with Key : "+ memcacheKey);
					}
				}
					
			}
			
			if(campaignList!=null && campaignList.size()>0){
				log.info("campaign list : " + campaignList.size());
				if(superAdmin) {
					dataStoreCampaignList = dao.getRunningCampaignListSuperUser(campaignStatus);
				}
				else {
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
					log.info("campaign count for user : " + dataStoreCampaignList.size());
					// check if campaign is authorised by account to user
					/*IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
					Map<String,String> accountDataMap = userService.getSelectedAccountsByUserId(userId, true, true);
					for (SmartCampaignObj smartCampaignObj : dataStoreCampaignList) {
						if(smartCampaignObj!=null && !smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Archived.ordinal()+"")  && !smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Draft.ordinal()+"")){
							String advertiserId = smartCampaignObj.getAdvertiserId();
							String agencyId = smartCampaignObj.getAgencyId();
							if((advertiserId != null && UserService.isAuthorisedAccountId(advertiserId, accountDataMap)) || 
									(agencyId != null && UserService.isAuthorisedAccountId(agencyId, accountDataMap))) {
								campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
							}else {
								//log.info("Campaign Account is not authorised, campaignId : "+smartCampaignObj.getCampaignId()+
									//	", advertiserId : "+advertiserId+", agencyId : "+agencyId);
							}
						}
					}
					log.info("After account authorisation check, authorised campaigns count : " + campaignStatusMap.size());*/
					
					IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
					for (SmartCampaignObj smartCampaignObj : dataStoreCampaignList) {
						if(smartCampaignObj!=null && !smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Archived.ordinal()+"")  /*&& !smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Draft.ordinal()+"")*/){
							campaignStatusMap.put(smartCampaignObj.getDfpOrderId()+"", smartCampaignObj);
						}
					}
					///////////////////////////////////////
					
					for (PerformanceMonitoringDTO campaignObj : campaignList) {
						if(campaignObj!=null && campaignObj.getOrderId()!=null && campaignStatusMap.containsKey(campaignObj.getOrderId()) && campaignStatusMap.get(campaignObj.getOrderId()) != null ){
							double progressBarMargin = 0.0;
							Map<String,String> partnerMap = new HashMap<>();
							JSONObject jsonObject = new JSONObject();
							SmartCampaignObj obj = new SmartCampaignObj();
							obj = campaignStatusMap.get(campaignObj.getOrderId());
							if(obj.getCampaignStatus()!=null){
								String cStatus = obj.getCampaignStatus();
								String status=CampaignStatusEnum.values()[Integer.parseInt(cStatus)].name();
								jsonObject.put("status", status);
							}
							
							if(obj.getCampaignId()!=null){
								jsonObject.put("campaignId", obj.getCampaignId());
								if(obj.getsDate() != null) {
									jsonObject.put("sDate", obj.getsDate().getTime());
								}else {
									jsonObject.put("sDate", "");
								}
							}
						
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
							} else {
								log.info("No Placement for the campaign id : "+obj.getCampaignId());
							}
							if(partner == null || partner.length() == 0) {
								if(companyNameMap.containsKey(obj.getCompanyId())) {
									partner = companyNameMap.get(obj.getCompanyId());
								}else {
									CompanyObj companyObj = userService.getCompanyById(StringUtil.getLongValue(obj.getCompanyId()), MemcacheUtil.getAllCompanyList());
									if(companyObj != null && companyObj.getCompanyName() != null) {
										partner = companyObj.getCompanyName();
										companyNameMap.put(obj.getCompanyId(), partner);
									}
								}
							}
							jsonObject.put("partner", partner);
							Date campaignEndDate = null;  
							double dateProgress = 0.0;
							
							if(obj.getStartDate()!=null && obj.getStartDate().length()>0 && obj.getEndDate()!=null && obj.getEndDate().length()>0){
								campaignEndDate = DateUtil.getRequiredFormatDate(obj.getEndDate(), "MM-dd-yyyy", "yyyy-MM-dd HH:mm:ss");
								int dateFlag = campaignEndDate.compareTo(DateUtil.getDateYYYYMMDDHHMMSS());
								log.info("DateFlag = "+dateFlag);
								if(dateFlag>=0){
									 dateProgress = durationInPercentage(obj.getStartDate(), obj.getEndDate());
									 long totalDays = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
									 jsonObject.put("totalDays", totalDays+1);
									 long daysTillDate = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), currentDateStr, "MM-dd-yyyy");
									 jsonObject.put("daysTillDate", daysTillDate);
								}else{
									 long totalDays = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
									 jsonObject.put("totalDays", totalDays+1);
									 dateProgress = durationInPercentage(obj.getStartDate(), obj.getEndDate());
									 if(dateProgress>100){
										 dateProgress = 100; 
									 }
									 long daysTillDate = DateUtil.getDifferneceBetweenTwoDates(obj.getStartDate(), obj.getEndDate(), "MM-dd-yyyy");
									 jsonObject.put("daysTillDate", daysTillDate+1);
								}
							}
							
							if(obj.getRateTypeList()!=null && obj.getRateTypeList().size()>0){
								if(obj.getRateTypeList().get(0).getObjectId()!=0 && obj.getRateTypeList().get(0).getObjectId()==1){
									log.info("Campaign type is CPM");
									jsonObject.put("rateType", "CPM");
									 goalProgress = goalInPercentage(goal, Double.parseDouble(campaignObj.getImpressionDelivered()));
									 log.info("goalProgress for Campaign type  CPM"+goalProgress);
								}else if(obj.getRateTypeList().get(0).getObjectId()!=0 && obj.getRateTypeList().get(0).getObjectId()==2){
									log.info("Campaign type is CPC");
									jsonObject.put("rateType", "CPC");
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
							
							if(dateProgress>=100 && goalProgress>=100){
								jsonObject.put("barStatus", 1);
							}else if(dateProgress>=100 && goalProgress<100){
								jsonObject.put("barStatus", 3);
							}else if(progressBarMargin<=5){
								jsonObject.put("barStatus", 1);
							}else if(progressBarMargin>5 && progressBarMargin<=20){
								jsonObject.put("barStatus", 2);
							}else if(progressBarMargin>20){
								jsonObject.put("barStatus", 3);
							}else{
								jsonObject.put("barStatus", 1);
							}
							
							jsonObject.put("dateProgress", dateProgress);
							jsonObject.put("goalProgress", goalProgress);
							jsonObject.put("goal", goal);
							String orderId="";
							if(StringUtil.getLongValue(campaignObj.getOrderId()) > 0) {
								orderId = campaignObj.getOrderId();
								jsonObject.put("dfpStatus", obj.getDfpStatus());
							}
							jsonObject.put("id", orderId);
							if(obj.getName()!=null && obj.getName().length()>0){
								jsonObject.put("name", obj.getName());
							}
							
							jsonObject.put("delivered", StringUtil.getLongValue(campaignObj.getImpressionDelivered()));
							jsonObject.put("clicks",  StringUtil.getLongValue(campaignObj.getClicks()));
							jsonObject.put("ctr", StringUtil.getDoubleValue(campaignObj.getCTR()));
							if(obj.getStartDate()!=null && obj.getStartDate().length()>0 && obj.getEndDate()!=null && obj.getEndDate().length()>0){
								jsonObject.put("date", obj.getStartDate()+" - "+obj.getEndDate());
							}
							//jsonObject.put("dfpNetworkCode", mediaPlanDAO.loadMediaPlan(campaignObj.getOrderId().toString()).getDfpPlacements().get(0).getPartnerDFPNetworkCode());
							jsonObject.put("dfpNetworkCode", obj.getAdServerId());
							jsonArray.add(jsonObject);
						}
					}
				}else {
					log.info("No campaigns in datastore");
				}
			}else {
				log.info("No campaign data in BQ");
			}
			
		}catch(Exception e){
			log.severe("Exception in loadAllCampaigns of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		return jsonArray;
	}
	
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
	
	public double goalInPercentage(double totalImpression, double deliveredImpression){
		double percentageImpression = 0.0;
		if(totalImpression!=0 && deliveredImpression!=0){
			percentageImpression = (deliveredImpression/totalImpression)*100;
		}
		return percentageImpression;
	}
	
	@Override
	public JSONObject headerData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+placementInfo;
		String keyPrefix = "PM_HeaderData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonObject != null && jsonObject.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonObject;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			jsonObject = new JSONObject();
		}
		JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
		try {
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) placementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.headerData(orderId, lineItemIds, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() == 1) {
				TableRow row = queryResponse.getRows().get(0);
			     if(row != null && row.getF() != null && row.getF().size() > 0) {
			    	 List<TableCell> cellList = row.getF();
			    	 long impressions = StringUtil.getLongValue(cellList.get(0).getV().toString());
			    	 long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
			    	 double ctr = StringUtil.getDoubleValue(cellList.get(2).getV().toString());
			    	 double rate = StringUtil.getDoubleValue(placementJson.get("rate")+"");
			    	 long goal = StringUtil.getLongValue(placementJson.get("impressionOrClicks")+"");
			    	 double budget = StringUtil.getDoubleValue(placementJson.get("budget")+"");
			    	 double spent = 0.0;
			    	 double left = 0.0;
			    	 long pending = 0;
			    	 double deliveryPercentage = 0.0;
			    	 String rateLabel = "Rate";
			    	 String goalLabel = "Goal";
			    	 String pendingLabel = "Pending";
			    	 String budgetLeftLabel = "Left";
			    	 String campaignType = (String) placementJson.get("campaignType");
			    	 if(campaignType.equals("CPM")) {
			    		 pending = goal - impressions;
			    		 deliveryPercentage = (double)(impressions*100)/goal;
			    		 spent = (double)(rate * impressions)/1000;
			    		 
			    		 rateLabel = rateLabel + " CPM";
			    		 goalLabel = goalLabel + " Impressions";
					 }
			    	 else if(campaignType.equals("CPC")) {
			    		 pending = goal - clicks;
			    		 deliveryPercentage = (double)(clicks*100)/goal;
			    		 spent = (double)(rate*clicks);
			    		 
			    		 rateLabel = rateLabel + " CPC";
			    		 goalLabel = goalLabel + " Clicks";
					 }
			    	 left = budget - spent;
			    	 if(pending < 0) {
			    		 pendingLabel = "Over Delivered";
			    		 pending = pending*(-1);
			    	 }
			    	 if(left < 0) {
			    		 budgetLeftLabel = "Over Budget";
			    		 left = left*(-1);
			    	 }
			    	 deliveryPercentage = StringUtil.getDoubleValue(df.format(deliveryPercentage));
			    	 
			    	 jsonObject.put("rateLabel", rateLabel);
		    	  	 jsonObject.put("goalLabel", goalLabel);
		    	  	 jsonObject.put("pendingLabel", pendingLabel);
		    	  	 jsonObject.put("budgetLeftLabel", budgetLeftLabel);
		    	  	 
			    	 jsonObject.put("rate", rate);
		    	  	 jsonObject.put("duration", placementJson.get("startDate")+" - "+placementJson.get("endDate"));
		    	  	 jsonObject.put("duarationTitle", (placementJson.get("duarationTitle")!=null)?(placementJson.get("duarationTitle")):"");
		    	  	 jsonObject.put("goal", goal);
		    	  	 jsonObject.put("delivered", impressions);
		    	  	 jsonObject.put("clicks", clicks);
		    	  	 jsonObject.put("pending", pending);
		    	  	 jsonObject.put("ctr", ctr);
		    	  	 jsonObject.put("deliveryPercentage", deliveryPercentage);
		    	  	 jsonObject.put("budget", budget);
		    	  	 jsonObject.put("spent", spent);
		    	  	 jsonObject.put("left", left);
				}
			}
		}
		catch (NumberFormatException e) {
			log.severe("NumberFormatException in headerData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (JSONException e) {
			log.severe("JSONException in headerData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in headerData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("headerData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	private List<SmartCampaignFlightDTO> getFlights(String campaignId, String placementIds) throws Exception {
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignFlightDTO> flightList = new ArrayList<>();
		if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all"))) {
			log.info("campaignId : "+campaignId);
			List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
			if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
				for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
					if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
						flightList.addAll(smartCampaignPlacementObj.getFlightObjList());
					}
				}
			}
		}
		else if(placementIds != null) {
			String[] tempArr = placementIds.split(",");
			for (String placementId : tempArr) {
				if(placementId != null && LinMobileUtil.isNumeric(placementId)) {
					SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
					if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
						flightList.addAll(smartCampaignPlacementObj.getFlightObjList());
					}
				}
			}
		}
		return flightList;
	}
	
	private List<SmartCampaignPlacementObj> getCampaignPlacementObj(String campaignId, String placementIds) throws Exception {
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = new ArrayList<>();;
		if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all"))) {
			log.info("campaignId : "+campaignId);
			List<SmartCampaignPlacementObj> tempList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
			if(tempList != null && tempList.size() > 0) {
				smartCampaignPlacementObjList.addAll(tempList);
			}
		}
		else if(placementIds != null) {
			String[] tempArr = placementIds.split(",");
			for (String placementId : tempArr) {
				if(placementId != null && LinMobileUtil.isNumeric(placementId)) {
					SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
					if(smartCampaignPlacementObj != null) {
						smartCampaignPlacementObjList.add(smartCampaignPlacementObj);
					}
				}
			}
		}
		return smartCampaignPlacementObjList;
	}

	@Override
	public JSONObject deliveryMetricsData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String orderInfo, String placementInfo, String lineItemPlacementName, String lineItemPlacementIds) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		String memcacheKey = "";
		JSONObject thisPlacementJson = null;
		try {
			List<SmartCampaignFlightDTO> flightList = getFlights(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+orderInfo+placementInfo+lineItemPlacementName+lineItemPlacementIds+flightList.toString();
			String keyPrefix = "PM_DeliveryMetricsData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			DecimalFormat df3 = new DecimalFormat( "###,###,###,##0.000" );
			
			JSONObject orderInfoJson = (JSONObject) JSONSerializer.toJSON(orderInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0  && allPlacementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				if(allPlacementJson != null) {
					thisPlacementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", flightList);
					String lineItemIds = thisPlacementJson.get("lineItemIds")+"','";
					queryResponse = monitoringDAO.deliveryMetricsData(orderId, lineItemIds, queryDTO, false);
				}
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && orderInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				
				String campaignType = (String) orderInfoJson.get("campaignType");
				String name = (String) thisPlacementJson.get("placementName");
				if(name.equalsIgnoreCase("All")) {
					name = (String) orderInfoJson.get("orderName");
				}
				long currentImpressionOrClicks = 0;
				long totalDuration = thisPlacementJson.getInt("durationInDays");
				long impressionOrClicks = thisPlacementJson.getInt("impressionOrClicks");
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date = cellList.get(0).getV().toString();
						long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
						long impressions = StringUtil.getLongValue(cellList.get(2).getV().toString());
						double ctr = 0;
						long dayPassed = 0;
						long dayRemaining = 0;
						double deliveryPercentage = 0.0;
						
						if(impressions > 0) {
							ctr = (double)(clicks*100)/impressions;
						}
						
						if(campaignType.equals("CPM")) {
							currentImpressionOrClicks = currentImpressionOrClicks + impressions;
						}
				    	else if(campaignType.equals("CPC")) {
				    		currentImpressionOrClicks = currentImpressionOrClicks + clicks;
						}
						
						String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
						dayPassed = dayDurationInFlights(flightList, null, currentDate, "MM-dd-yyyy");
						dayRemaining = totalDuration - dayPassed;
						
						if(impressionOrClicks > 0) {
							deliveryPercentage = (double)(currentImpressionOrClicks*100)/impressionOrClicks;
						}
						
						String targetPacing = "NA";
						String currentPacing = "NA";
						String revisedPacing = "NA";
						if(totalDuration > 0) {
							long tp = (impressionOrClicks/totalDuration);
							if(tp >= 0) {
								targetPacing = lf.format(tp);
							}
						}
						if(dayPassed > 0) {
							long cp = (currentImpressionOrClicks/dayPassed);
							if(cp >= 0) {
								currentPacing = lf.format(cp);
							}
						}
						if(dayRemaining > 0) {
							long rp = ((impressionOrClicks - currentImpressionOrClicks)/dayRemaining);
							if(rp >= 0) {
								revisedPacing = lf.format(rp);
							}
						}
						
						jsonObject = new JSONObject();
						jsonObject.put("date", date);
						jsonObject.put("name", name);
						jsonObject.put("goal", lf.format(impressionOrClicks));
						jsonObject.put("clicks", lf.format(clicks));
						jsonObject.put("impressions", lf.format(impressions));
						jsonObject.put("ctr", df3.format(ctr));
						jsonObject.put("deliveryPercentage", df3.format(deliveryPercentage));
						jsonObject.put("target", targetPacing);
						jsonObject.put("current", currentPacing);
						jsonObject.put("revised", revisedPacing);
						jsonArray.add(jsonObject);
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in deliveryMetricsData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in deliveryMetricsData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonArray != null && jsonArray.size() > 0) {
			retunJson.put("deliveryMetricsData", jsonArray);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	/*
	 public JSONObject deliveryMetricsData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String orderInfo, String placementInfo, String lineItemPlacementName, String lineItemPlacementIds) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		String memcacheKey = "";
		boolean multipleOptionSelected = false;
		JSONObject thisPlacementJson = null;
		try {
			String[] placementIdArr = placementIds.split(",");
			List<SmartCampaignFlightDTO> flightList = getFlights(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+orderInfo+placementInfo+lineItemPlacementName+lineItemPlacementIds+flightList.toString();
			String keyPrefix = "PM_DeliveryMetricsData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			DecimalFormat df3 = new DecimalFormat( "###,###,###,##0.000" );
			
			JSONObject orderInfoJson = (JSONObject) JSONSerializer.toJSON(orderInfo);
			JSONObject placementsJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject lineItemPlacementNameJson = (JSONObject) JSONSerializer.toJSON(lineItemPlacementName);
			JSONObject lineItemPlacementIdJson = (JSONObject) JSONSerializer.toJSON(lineItemPlacementIds);
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0  && placementsJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				if(placementsJson != null) {
					String lineItemIds = "";
					if(placementIdArr != null && placementIdArr.length > 0) {
						if(placementIdArr.length > 1) {
							multipleOptionSelected = true;
						}
						for (String placementId : placementIdArr) {
							thisPlacementJson = (JSONObject) placementsJson.get(placementId);
							if(LinMobileUtil.isNumeric(placementId)) {
								lineItemIds = lineItemIds + thisPlacementJson.get("lineItemIds")+"','";
							}else if(placementId.equalsIgnoreCase("All")) {
								lineItemIds = thisPlacementJson.get("lineItemIds")+"','";
								break;
							}
						}
						lineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds, "','");
						queryResponse = monitoringDAO.deliveryMetricsData(orderId, lineItemIds, queryDTO, multipleOptionSelected);
					}
				}
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && orderInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				
				String campaignType = (String) orderInfoJson.get("campaignType");
				if(multipleOptionSelected) {
					jsonArray = multiplePlacementDeliveryMetricsData(rowList, campaignType, lineItemPlacementNameJson, 
							lineItemPlacementIdJson, placementsJson, lf, df3);
				}else {
					String name = (String) thisPlacementJson.get("placementName");
					if(name.equalsIgnoreCase("All")) {
						name = (String) orderInfoJson.get("orderName");
					}
					long currentImpressionOrClicks = 0;
					long totalDuration = thisPlacementJson.getInt("durationInDays");
					long impressionOrClicks = thisPlacementJson.getInt("impressionOrClicks");
					for (TableRow row : rowList) {
						if(row != null && row.getF() != null && row.getF().size() > 0) {
							List<TableCell> cellList = row.getF();
							String date = cellList.get(0).getV().toString();
							long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
							long impressions = StringUtil.getLongValue(cellList.get(2).getV().toString());
							double ctr = 0;
							long dayPassed = 0;
							long dayRemaining = 0;
							double deliveryPercentage = 0.0;
							
							if(impressions > 0) {
								ctr = (double)(clicks*100)/impressions;
							}
							
							if(campaignType.equals("CPM")) {
								currentImpressionOrClicks = currentImpressionOrClicks + impressions;
							}
					    	else if(campaignType.equals("CPC")) {
					    		currentImpressionOrClicks = currentImpressionOrClicks + clicks;
							}
							
							String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
							dayPassed = dayDurationInFlights(flightList, null, currentDate, "MM-dd-yyyy");
							dayRemaining = totalDuration - dayPassed;
							
							if(impressionOrClicks > 0) {
								deliveryPercentage = (double)(currentImpressionOrClicks*100)/impressionOrClicks;
							}
							
							String targetPacing = "NA";
							String currentPacing = "NA";
							String revisedPacing = "NA";
							if(totalDuration > 0) {
								long tp = (impressionOrClicks/totalDuration);
								if(tp >= 0) {
									targetPacing = lf.format(tp);
								}
							}
							if(dayPassed > 0) {
								long cp = (currentImpressionOrClicks/dayPassed);
								if(cp >= 0) {
									currentPacing = lf.format(cp);
								}
							}
							if(dayRemaining > 0) {
								long rp = ((impressionOrClicks - currentImpressionOrClicks)/dayRemaining);
								if(rp >= 0) {
									revisedPacing = lf.format(rp);
								}
							}
							
							jsonObject = new JSONObject();
							jsonObject.put("date", date);
							jsonObject.put("name", name);
							jsonObject.put("goal", lf.format(impressionOrClicks));
							jsonObject.put("clicks", lf.format(clicks));
							jsonObject.put("impressions", lf.format(impressions));
							jsonObject.put("ctr", df3.format(ctr));
							jsonObject.put("deliveryPercentage", df3.format(deliveryPercentage));
							jsonObject.put("target", targetPacing);
							jsonObject.put("current", currentPacing);
							jsonObject.put("revised", revisedPacing);
							jsonArray.add(jsonObject);
						}
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in deliveryMetricsData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in deliveryMetricsData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonArray != null && jsonArray.size() > 0) {
			retunJson.put("deliveryMetricsData", jsonArray);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	*/
	
	public JSONArray multiplePlacementDeliveryMetricsData(List<TableRow> rowList, String campaignType, JSONObject lineItemPlacementNameJson, 
							JSONObject lineItemPlacementIdJson, JSONObject placementsJson, DecimalFormat lf, DecimalFormat df3) throws DataServiceException {
		JSONArray jsonArray = new JSONArray();
		Map<String, Object> placementsByDate = new LinkedHashMap<String, Object>();
		for (TableRow row : rowList) {
			if(row != null && row.getF() != null && row.getF().size() > 0) {
				List<TableCell> cellList = row.getF();
				String date = cellList.get(0).getV().toString();
				long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
				long impressions = StringUtil.getLongValue(cellList.get(2).getV().toString());
				String placementName = lineItemPlacementNameJson.get(cellList.get(3).getV().toString())+"";
				String placementId = lineItemPlacementIdJson.get(cellList.get(3).getV().toString())+"";
				String key = date+"_"+placementName;
				LineChartDTO chartDTO = null;
				if(placementsByDate.get(key) != null) {
					chartDTO = (LineChartDTO) placementsByDate.get(key);
				}
				else {
					chartDTO = new LineChartDTO();
				}
				if(chartDTO != null) {
					long clk = 0;
					long imp = 0;
					chartDTO.setDate(date);
					chartDTO.setSiteName(placementName);
					chartDTO.setId(placementId);
					if(chartDTO.getClicks() != null && LinMobileUtil.isNumeric(chartDTO.getClicks())) {
						clk = StringUtil.getLongValue(chartDTO.getClicks());
					}
					chartDTO.setClicks((clk + clicks)+"");
					if(chartDTO.getImpressions() != null && LinMobileUtil.isNumeric(chartDTO.getImpressions())) {
						imp = StringUtil.getLongValue(chartDTO.getImpressions());
					}
					chartDTO.setImpressions((imp + impressions)+"");
					placementsByDate.put(key, chartDTO);
				}
			}
		}
		
		if(placementsByDate != null && placementsByDate.size() > 0) {
			JSONObject placementJson = new JSONObject();
			Map<String,List<SmartCampaignFlightDTO>> placementFlightMap = new HashMap<>();
			List<SmartCampaignFlightDTO> flightList = new ArrayList<>();
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			
			for(String key : placementsByDate.keySet()) {
				LineChartDTO chartDTO = (LineChartDTO) placementsByDate.get(key);
				if(chartDTO != null) {
					long impressionOrClicks = 0;
					long currentImpressionOrClicks = 0;
					long impressions = StringUtil.getLongValue(chartDTO.getImpressions());
					long clicks = StringUtil.getLongValue(chartDTO.getClicks());
					String date = chartDTO.getDate();
					double ctr = (double)(clicks*100)/impressions;
					double deliveryPercentage = 0.0;
					
					long totalDuration = 0;
					long dayPassed = 0;
					long dayRemaining = 0;
					
					if(campaignType.equals("CPM")) {
						currentImpressionOrClicks = impressions;
					}
			    	else if(campaignType.equals("CPC")) {
			    		currentImpressionOrClicks = clicks;
					}
					
					String placementId = chartDTO.getId();
					if(placementJson.get(placementId) != null) {
						JSONObject placement = (JSONObject) placementJson.get(placementId);
						totalDuration = placement.getInt("totalDuration");
						impressionOrClicks = placement.getInt("goal");
						currentImpressionOrClicks = currentImpressionOrClicks + placement.getInt("currentImpressionOrClicks");
						placement.put("currentImpressionOrClicks", currentImpressionOrClicks);
						flightList = placementFlightMap.get(placementId);
					}
					else {
						JSONObject placement = new JSONObject();
						SmartCampaignPlacementObj campaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
						if(campaignPlacementObj != null && campaignPlacementObj.getFlightObjList() != null && campaignPlacementObj.getFlightObjList().size() > 0) {
							flightList = campaignPlacementObj.getFlightObjList();
							JSONObject thisPlacementJson = (JSONObject) placementsJson.get(placementId);
							totalDuration = totalDuration + thisPlacementJson.getInt("durationInDays");
							impressionOrClicks = thisPlacementJson.getInt("impressionOrClicks");
							log.info("Total duration for "+campaignPlacementObj.getPlacementName()+" is : "+totalDuration+" and goal : "+impressionOrClicks);
							placement.put("totalDuration", totalDuration);
							placement.put("goal", impressionOrClicks);		// goal
							placement.put("currentImpressionOrClicks", currentImpressionOrClicks);
							placementJson.put(placementId, placement);
							placementFlightMap.put(placementId, flightList);
						}
					}
					
					String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
					dayPassed = dayDurationInFlights(flightList, null, currentDate, "MM-dd-yyyy");
					dayRemaining = totalDuration - dayPassed;
					log.info("Placement : "+chartDTO.getSiteName()+", currentImpressionOrClicks : "+currentImpressionOrClicks+", currentDate : "+currentDate+", totalDuration : "+totalDuration+", dayPassed : "+dayPassed+", dayRemaining : "+dayRemaining);
					
					if(impressionOrClicks > 0) {
						deliveryPercentage = (double)(currentImpressionOrClicks*100)/impressionOrClicks;
					}
					
					String targetPacing = "NA";
					String currentPacing = "NA";
					String revisedPacing = "NA";
					if(totalDuration > 0) {
						long tp = (impressionOrClicks/totalDuration);
						if(tp >= 0) {
							targetPacing = lf.format(tp);
						}
					}
					if(dayPassed > 0) {
						long cp = (currentImpressionOrClicks/dayPassed);
						if(cp >= 0) {
							currentPacing = lf.format(cp);
						}
					}
					if(dayRemaining > 0) {
						long rp = ((impressionOrClicks - currentImpressionOrClicks)/dayRemaining);
						if(rp >= 0) {
							revisedPacing = lf.format(rp);
						}
					}
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("date", chartDTO.getDate());
					jsonObject.put("name", chartDTO.getSiteName());
					jsonObject.put("goal", lf.format(impressionOrClicks));
					jsonObject.put("clicks", lf.format(clicks));
					jsonObject.put("impressions", lf.format(impressions));
					jsonObject.put("ctr", df3.format(ctr));
					jsonObject.put("deliveryPercentage", df3.format(deliveryPercentage));
					jsonObject.put("target", targetPacing);
					jsonObject.put("current", currentPacing);
					jsonObject.put("revised", revisedPacing);
					jsonArray.add(jsonObject);
				}
			}
		}
		return jsonArray;
	}
	
	public JSONObject getConsolidatedJsonForMultiplePlacements(String[] placementIdArr, JSONObject allPlacementJson, String dateFormat, List<SmartCampaignFlightDTO> allFlightObjList) throws ParseException {
		JSONObject placementJson = new JSONObject();
		log.info("In getConsolidatedJsonForMultiplePlacements");
		if(placementIdArr != null && placementIdArr.length > 0) {
			if(placementIdArr.length == 1) {					// No need for further calculations, we already have calculated data for single placement
				log.info("Single placement id : "+placementIdArr[0]);
				return (JSONObject) allPlacementJson.get(placementIdArr[0]);
			}
			String lineItemIds = "";
			String campaignId = "";
			String campaignType = "";
			long impressionOrClicks = 0;
			long durationInDays = 0;
			double budget = 0.0;
			String startDate = "";
			String endDate = "";
			String goalCTR = "NA";
			String duarationTitle = "";
			JSONObject thisPlacementJson = null;
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			for (String placementId : placementIdArr) {
				thisPlacementJson = (JSONObject) allPlacementJson.get(placementId);
				if(LinMobileUtil.isNumeric(placementId) && thisPlacementJson != null) {
					log.info("for placement Id : "+placementId);
					lineItemIds = lineItemIds + thisPlacementJson.get("lineItemIds")+"','";
					campaignId = thisPlacementJson.get("campaignId")+"";
					campaignType = (String) thisPlacementJson.get("campaignType");
					impressionOrClicks = impressionOrClicks + thisPlacementJson.getInt("impressionOrClicks");
					//durationInDays = durationInDays + thisPlacementJson.getInt("durationInDays");
					budget = budget + thisPlacementJson.getInt("budget");
					String goal = (String) thisPlacementJson.get("goalCTR");
					if(!LinMobileUtil.isNumeric(goalCTR)) {
						goalCTR = goal;
					}
					String sDate = (String) thisPlacementJson.get("startDate");
					String eDate = (String) thisPlacementJson.get("endDate");
					if(startDate.isEmpty()) {
						startDate = sDate;
						endDate = eDate;
					}else {
						Date lowerStartDate = sdf.parse(startDate);
						Date lowerEndDate = sdf.parse(endDate);
						Date thisPlacementStartDate = sdf.parse(sDate);
						Date thisPlacementEndDate = sdf.parse(eDate);
						if(lowerStartDate.compareTo(thisPlacementStartDate)>0) {
							startDate = sDate;
						}
						if(lowerEndDate.compareTo(thisPlacementEndDate)<0) {
							startDate = eDate;
						}
					}
					String placementName = (String) thisPlacementJson.get("placementName");
					duarationTitle = duarationTitle + placementName + " : " + sDate + " to " + eDate + "<br>";
				}else if(placementId.equalsIgnoreCase("All")) {		// No need for further calculations, we already have calculated data for whole campaign.
					log.info("'All' selected");
					return (JSONObject) allPlacementJson.get(placementId);
				}
			}
			if(allFlightObjList != null && allFlightObjList.size() > 0) {
				durationInDays = dayDurationInFlights(allFlightObjList, null, null, dateFormat);
			}
			duarationTitle = StringUtil.deleteFromLastOccurence(duarationTitle, "<br>");
			lineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds, "','");
			
			double rate = 0.0;
			if(impressionOrClicks > 0 && campaignType.equals("CPM")) {
				rate = (double)((budget*1000)/impressionOrClicks);
			}
			else if(impressionOrClicks > 0 && campaignType.equals("CPC")) {
				rate = (double)(budget/impressionOrClicks);
			}
			
			placementJson.put("campaignId", campaignId);
			placementJson.put("placementId", "All");
			placementJson.put("placementName", "All");
			placementJson.put("campaignType", campaignType);
			placementJson.put("impressionOrClicks", impressionOrClicks);
			placementJson.put("budget", budget);
			placementJson.put("rate", rate);
			placementJson.put("goalCTR", goalCTR);
			placementJson.put("pacing", "All");
			placementJson.put("lineItemIds", lineItemIds);
			placementJson.put("startDate", startDate);
			placementJson.put("endDate", endDate);
			placementJson.put("duarationTitle", duarationTitle);
			placementJson.put("durationInDays", durationInDays);
		}
		log.info("Out of getConsolidatedJsonForMultiplePlacements"+placementJson);
		return placementJson;
	}
	
	@Override
	public JSONObject clicksLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = "";
		try {
			List<SmartCampaignFlightDTO> flightObjList = getFlights(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+flightObjList.toString();
			String keyPrefix = "PM_ClicksData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonObject = new JSONObject();
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
		
			long totalClicks = 0;
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) placementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", flightObjList);
			}
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			
		    if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.clicksLineChartData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && placementJson != null && partnerInfoJson != null) {
				boolean isShowPacing  = false;
				String targetPacing = "NA";
				String currentPacing = "NA";
				String revisedPacing = "NA";
				long impressionOrClicks = StringUtil.getLongValue(placementJson.get("impressionOrClicks")+"");
				long totalDuration = StringUtil.getLongValue(placementJson.get("durationInDays")+"");
				long dayPassed = 0;
				long dayRemaining = 0;
				if(placementJson.get("campaignType").toString().equalsIgnoreCase("CPC") && placementJson.get("pacing") != null && !(placementJson.get("pacing")).toString().equalsIgnoreCase("NA")) {
					isShowPacing = true;
					if((placementJson.get("pacing")).toString().equalsIgnoreCase("All")) {
						targetPacing = (totalDuration > 0)?(impressionOrClicks/totalDuration)+"":"NA";
					}
					else {
						targetPacing = placementJson.get("pacing")+"";
					}
				}
				
				List<TableRow> rowList = queryResponse.getRows();
				int thisWeekEndDateValue = 0;
				String weekStartDate = "";
				int thisMonthValue = 0;
				String monthStartDate = "";
				String previousDate = "";
				Map<String, Boolean> pacingMap = new HashMap<>();
				Map<String, Object> dayDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> weekDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> monthDateMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> partnerMap = new LinkedHashMap<String, Object>();
				Map<String, Object> tempPartnerMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> perfDayDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfWeekDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfMonthDataMap = new LinkedHashMap<String, Object>();
				
				Map<String, Long> weekPartnerClicksMap = new LinkedHashMap<String, Long>();
				Map<String, Long> monthPartnerClicksMap = new LinkedHashMap<String, Long>();
				
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date=cellList.get(0).getV().toString();
						int dateValue=Integer.parseInt((cellList.get(0).getV().toString()).replaceAll("-", ""));
						int monthValue=Integer.parseInt((cellList.get(0).getV().toString()).split("-")[1]);
						
						String partnerName=partnerInfoJson.get(cellList.get(1).getV().toString())+"";
						long clicks=StringUtil.getLongValue(cellList.get(2).getV().toString());
						totalClicks = totalClicks + clicks;
						
						if(LinMobileUtil.isNumeric(targetPacing)) {					// Day wise Pacing
							pacingMap.put("Target", true);
							String key=date+"_Target";
							LineChartDTO lineChartDTO=new LineChartDTO();
							lineChartDTO.setDate(date);
							lineChartDTO.setSiteName("Target");
							lineChartDTO.setClicks(targetPacing);
							perfDayDataMap.put(key, lineChartDTO);
							
							String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
							dayPassed = dayDurationInFlights(flightObjList, null, currentDate, "MM-dd-yyyy");
							dayRemaining = totalDuration - dayPassed;
							
							currentPacing = (dayPassed > 0)?(totalClicks/dayPassed)+"":"NA";
							if(LinMobileUtil.isNumeric(currentPacing)) {
								pacingMap.put("Current", true);
								key=date+"_Current";
								lineChartDTO=new LineChartDTO();
								lineChartDTO.setDate(date);
								lineChartDTO.setSiteName("Current");
								lineChartDTO.setClicks(currentPacing);
								perfDayDataMap.put(key, lineChartDTO);
							}
							
							if(dayRemaining > 0) {
								long rp = ((impressionOrClicks - totalClicks)/dayRemaining);
								if(rp >= 0) {
									revisedPacing = rp+"";
									pacingMap.put("Revised", true);
									key=date+"_Revised";
									lineChartDTO=new LineChartDTO();
									lineChartDTO.setDate(date);
									lineChartDTO.setSiteName("Revised");
									lineChartDTO.setClicks(revisedPacing);
									perfDayDataMap.put(key, lineChartDTO);
								}
							}
						}
						
						String key=date+"_"+partnerName;
						LineChartDTO lineChartDTO=new LineChartDTO();		// Day wise Data
						lineChartDTO.setDate(date);
						lineChartDTO.setSiteName(partnerName);
						lineChartDTO.setClicks(clicks+"");
						
						if(perfDayDataMap.get(key) != null) {		
							LineChartDTO chartDTO = (LineChartDTO) perfDayDataMap.get(key);
							long clk = StringUtil.getLongValue(chartDTO.getClicks()) + clicks;
							chartDTO.setClicks(clk+"");
							perfDayDataMap.put(key, chartDTO);
						}
						else {
							perfDayDataMap.put(key, lineChartDTO);
						}
						dayDateMap.put(date, null);
						tempPartnerMap.put(partnerName, null);
						
						if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {				// Week wise Data
							if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
								long thisWeekClicks = 0;
								for(String weekSiteKey : weekPartnerClicksMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(weekSiteKey);
									chartDTO.setClicks(weekPartnerClicksMap.get(weekSiteKey)+"");
									thisWeekClicks = thisWeekClicks + weekPartnerClicksMap.get(weekSiteKey);
									perfWeekDataMap.put(previousDate+"_"+weekSiteKey, chartDTO);
									weekPartnerClicksMap.put(weekSiteKey, 0L);
								}
								weekDateMap.put(previousDate, null);
								// week wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Clicks", thisWeekClicks, 0);
							}
							weekStartDate = date;
							int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
							thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
						}
						if(weekPartnerClicksMap.get(partnerName) != null) {
							long weekSiteClicks = weekPartnerClicksMap.get(partnerName);
							weekPartnerClicksMap.put(partnerName, weekSiteClicks+clicks);
						}
						else {
							weekPartnerClicksMap.put(partnerName, clicks);
						}
						
						if(thisMonthValue == 0 || thisMonthValue != monthValue) {				// Month wise Data
							if(thisMonthValue != 0 && thisMonthValue != monthValue) {
								long thisMonthClicks = 0;
								for(String monthSiteKey : monthPartnerClicksMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(monthSiteKey);
									chartDTO.setClicks(monthPartnerClicksMap.get(monthSiteKey)+"");
									thisMonthClicks = thisMonthClicks + monthPartnerClicksMap.get(monthSiteKey); 
									perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
									monthPartnerClicksMap.put(monthSiteKey, 0L);
								}
								monthDateMap.put(previousDate, null);
								
								// month wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Clicks", thisMonthClicks, 0);
								
							}
							monthStartDate = date;
							thisMonthValue = monthValue;
						}
						if(monthPartnerClicksMap.get(partnerName) != null) {
							long momthSiteClicks = monthPartnerClicksMap.get(partnerName);
							monthPartnerClicksMap.put(partnerName, momthSiteClicks+clicks);
						}
						else {
							monthPartnerClicksMap.put(partnerName, clicks);
						}
						
						previousDate = date;
					}
				}
				// add week, month data which was left to add.
				long thisWeekClicks = 0;
				for(String weekSiteKey : weekPartnerClicksMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(weekSiteKey);
					chartDTO.setClicks(weekPartnerClicksMap.get(weekSiteKey)+"");
					thisWeekClicks = thisWeekClicks + weekPartnerClicksMap.get(weekSiteKey);
					perfWeekDataMap.put(previousDate+"_"+weekSiteKey, chartDTO);
				}
				weekDateMap.put(previousDate, null);
				// week wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Clicks", thisWeekClicks, 0);
				
				long thisMonthClicks = 0;
				for(String monthSiteKey : monthPartnerClicksMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(monthSiteKey);
					chartDTO.setClicks(monthPartnerClicksMap.get(monthSiteKey)+"");
					thisMonthClicks = thisMonthClicks + monthPartnerClicksMap.get(monthSiteKey);
					perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
				}
				monthDateMap.put(previousDate, null);
				// month wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Clicks", thisMonthClicks, 0);
				
				currentPacing = "NA";
				revisedPacing = "NA";
				if(tempPartnerMap != null && tempPartnerMap.size() > 0) {
					if(isShowPacing) {
						String lastDate = DateUtil.getFormatedDate(previousDate, "yyyy-MM-dd", "MM-dd-yyyy");
						dayPassed = dayDurationInFlights(flightObjList, null, lastDate, "MM-dd-yyyy");
						dayRemaining = totalDuration - dayPassed;
						log.info("totalDuration  : "+totalDuration+",dayPassed"+dayPassed+", dayRemaining : "+dayRemaining+", lastDate"+lastDate+", flightObjList"+flightObjList);
						
						currentPacing = (dayPassed > 0)?(totalClicks/dayPassed)+"":"NA";
						if(dayRemaining > 0) {
							long rp = ((impressionOrClicks - totalClicks)/dayRemaining);
							if(rp >= 0) {
								revisedPacing = rp+"";
							}
						}
						if(pacingMap!= null) {
							if(pacingMap.get("Target") != null) {
								partnerMap.put("Target", null);
							}
							if(pacingMap.get("Current") != null) {
								partnerMap.put("Current", null);
							}
							if(pacingMap.get("Revised") != null) {
								partnerMap.put("Revised", null);
							}
						}
					}
					for(String site : tempPartnerMap.keySet()) {
						partnerMap.put(site, null);
					}
				}
				if(LinMobileUtil.isNumeric(targetPacing)) {
					targetPacing = lf.format(Math.round(StringUtil.getDoubleValue(targetPacing)))+"";
				}
				if(LinMobileUtil.isNumeric(currentPacing)) {
					currentPacing = lf.format(Math.round(StringUtil.getDoubleValue(currentPacing)))+"";
				}
				if(LinMobileUtil.isNumeric(revisedPacing)) {
					revisedPacing = lf.format(Math.round(StringUtil.getDoubleValue(revisedPacing)))+"";
				}
				jsonObject.put("target", targetPacing);
				jsonObject.put("current", currentPacing);
				jsonObject.put("revised", revisedPacing);

				String dayLineChartJson=createJsonResponseForLineChart(dayDateMap, partnerMap, perfDayDataMap, "Clicks");
				jsonObject.put("day", dayLineChartJson);
				String weekLineChartJson=createJsonResponseForLineChart(weekDateMap, partnerMap, perfWeekDataMap, "Clicks");
				jsonObject.put("week", weekLineChartJson);
				log.info("weekLineChartJson : "+weekLineChartJson);
				String monthChartJson=createJsonResponseForLineChart(monthDateMap, partnerMap, perfMonthDataMap, "Clicks");
				jsonObject.put("month", monthChartJson);
				log.info("monthChartJson : "+monthChartJson);
				
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in clicksLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in clicksLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("clicksData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	public void weekOrMonthPacing(String targetPacing, Map<String, Boolean> pacingMap, Map<String, Object> performanceMap,
							List<SmartCampaignFlightDTO> flightObjList, String durationStartDate, String durationLastDate, 
							String chartType, long durationClicks, long durationImpressions) {
		log.info("pacing -> targetPacing:"+targetPacing+", durationStartDate"+durationStartDate+", durationLastDate"+durationLastDate
				+"chartType"+chartType+", durationClicks:"+durationClicks+", durationImpressions:"+durationImpressions);
		String startDate = "";
		String endDate = "";
		long totalDuration = 0;
		long target = 0;
		if(LinMobileUtil.isNumeric(targetPacing)) {
			if(!(chartType.equalsIgnoreCase("CTR"))) {
				startDate = DateUtil.getFormatedDate(durationStartDate, "yyyy-MM-dd", "MM-dd-yyyy");
				endDate = DateUtil.getFormatedDate(durationLastDate, "yyyy-MM-dd", "MM-dd-yyyy");
				totalDuration = dayDurationInFlights(flightObjList, startDate, endDate, "MM-dd-yyyy");
				log.info("totalDuration:"+totalDuration+", startDate:"+startDate+", endDate:"+endDate);
				target = (StringUtil.getLongValue(targetPacing))*totalDuration;
			}
			if(totalDuration > 0 || chartType.equalsIgnoreCase("CTR")) {
				pacingMap.put("Target", true);
				String key=durationLastDate+"_Target";
				LineChartDTO lineChartDTO=new LineChartDTO();
				lineChartDTO.setDate(durationLastDate);
				lineChartDTO.setSiteName("Target");
				lineChartDTO.setClicks(target+"");
				lineChartDTO.setImpressions(target+"");
				lineChartDTO.setCtr(targetPacing+"");
				performanceMap.put(key, lineChartDTO);
				
				pacingMap.put("Current", true);
				key=durationLastDate+"_Current";
				lineChartDTO=new LineChartDTO();
				lineChartDTO.setDate(durationLastDate);
				lineChartDTO.setSiteName("Current");
				lineChartDTO.setClicks(durationClicks+"");
				lineChartDTO.setImpressions(durationImpressions+"");
				lineChartDTO.setCtr((durationImpressions > 0)?((double)(durationClicks*100)/durationImpressions)+"":"0.00");
				performanceMap.put(key, lineChartDTO);
				
				if(!(chartType.equalsIgnoreCase("CTR"))) {
					long revised = 0;
					if(chartType.equalsIgnoreCase("Clicks")) {
						revised = target - durationClicks;
					}
					else if(chartType.equalsIgnoreCase("Impressions")) {
						revised = target - durationImpressions;
					}
					if(revised > 0) {
						pacingMap.put("Revised", true);
						key=durationLastDate+"_Revised";
						lineChartDTO=new LineChartDTO();
						lineChartDTO.setDate(durationLastDate);
						lineChartDTO.setSiteName("Revised");
						lineChartDTO.setClicks(revised+"");
						lineChartDTO.setImpressions(revised+"");
						performanceMap.put(key, lineChartDTO);
					}
				}
			}
		}
	}
	
	@Override
	public JSONObject impressionsLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = "";
		try {
			List<SmartCampaignFlightDTO> flightObjList = getFlights(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+flightObjList.toString();
			String keyPrefix = "PM_ImpressionsData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonObject = new JSONObject();
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			
			long totalImpressions = 0;
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", flightObjList);
			}
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.impressionsLineChartData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && placementJson != null && partnerInfoJson != null) {
				boolean isShowPacing  = false;
				String targetPacing = "NA";
				String currentPacing = "NA";
				String revisedPacing = "NA";
				long impressionOrClicks = StringUtil.getLongValue(placementJson.get("impressionOrClicks")+"");
				long totalDuration = StringUtil.getLongValue(placementJson.get("durationInDays")+"");
				long dayPassed = 0;
				long dayRemaining = 0;
				if(placementJson.get("campaignType").toString().equalsIgnoreCase("CPM") && placementJson.get("pacing") != null && !(placementJson.get("pacing")).toString().equalsIgnoreCase("NA")) {
					isShowPacing = true;
					if((placementJson.get("pacing")).toString().equalsIgnoreCase("All")) {
						targetPacing = (totalDuration > 0)?(impressionOrClicks/totalDuration)+"":"NA";
					}
					else {
						targetPacing = placementJson.get("pacing")+"";
					}
				}
				
				/*ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				if(placementId != null && placementId.equalsIgnoreCase("All")) {
					log.info("campaignId : "+campaignId);
					List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
					if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
						for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
							if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
								flightObjList.addAll(smartCampaignPlacementObj.getFlightObjList());
							}
						}
					}
				}
				else if(placementId != null && LinMobileUtil.isNumeric(placementId)) {
					SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
					if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
						flightObjList.addAll(smartCampaignPlacementObj.getFlightObjList());
					}
				}*/
				
				List<TableRow> rowList = queryResponse.getRows();
				int thisWeekEndDateValue = 0;
				String weekStartDate = "";
				int thisMonthValue = 0;
				String monthStartDate = "";
				String previousDate = "";
				Map<String, Boolean> pacingMap = new HashMap<>();
				Map<String, Object> dayDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> weekDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> monthDateMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> partnerMap = new LinkedHashMap<String, Object>();
				Map<String, Object> tempPartnerMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> perfDayDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfWeekDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfMonthDataMap = new LinkedHashMap<String, Object>();
				
				Map<String, Long> weekPartnerImpressionsMap = new LinkedHashMap<String, Long>();
				Map<String, Long> monthPartnerImpressionsMap = new LinkedHashMap<String, Long>();
				
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date=cellList.get(0).getV().toString();
						int dateValue=Integer.parseInt((cellList.get(0).getV().toString()).replaceAll("-", ""));
						int monthValue=Integer.parseInt((cellList.get(0).getV().toString()).split("-")[1]);
						
						String partnerName=partnerInfoJson.get(cellList.get(1).getV().toString())+"";
						long impressions=StringUtil.getLongValue(cellList.get(2).getV().toString());
						totalImpressions = totalImpressions + impressions;
						
						if(LinMobileUtil.isNumeric(targetPacing)) {					// Day wise Pacing
							pacingMap.put("Target", true);
							String key=date+"_Target";
							LineChartDTO lineChartDTO=new LineChartDTO();
							lineChartDTO.setDate(date);
							lineChartDTO.setSiteName("Target");
							lineChartDTO.setImpressions(targetPacing);
							perfDayDataMap.put(key, lineChartDTO);
							
							String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
							//String yesterdayDate = DateUtil.getModifiedDateStringByDays(currentDate, -1, "MM-dd-yyyy");
							dayPassed = dayDurationInFlights(flightObjList, null, currentDate, "MM-dd-yyyy");
							dayRemaining = totalDuration - dayPassed;
							
							currentPacing = (dayPassed > 0)?(totalImpressions/dayPassed)+"":"NA";
							if(LinMobileUtil.isNumeric(currentPacing)) {
								pacingMap.put("Current", true);
								key=date+"_Current";
								lineChartDTO=new LineChartDTO();
								lineChartDTO.setDate(date);
								lineChartDTO.setSiteName("Current");
								lineChartDTO.setImpressions(currentPacing);
								perfDayDataMap.put(key, lineChartDTO);
							}
							
							if(dayRemaining > 0) {
								long rp = ((impressionOrClicks - totalImpressions)/dayRemaining);
								if(rp >= 0) {
									revisedPacing = rp+"";
									pacingMap.put("Revised", true);
									key=date+"_Revised";
									lineChartDTO=new LineChartDTO();
									lineChartDTO.setDate(date);
									lineChartDTO.setSiteName("Revised");
									lineChartDTO.setImpressions(revisedPacing);
									perfDayDataMap.put(key, lineChartDTO);
								}
							}
						}
						
						String key=date+"_"+partnerName;
						LineChartDTO lineChartDTO=new LineChartDTO();
						lineChartDTO.setDate(date);
						lineChartDTO.setSiteName(partnerName);
						lineChartDTO.setImpressions(impressions+"");
						
						if(perfDayDataMap.get(key) != null) {		// Day wise Data
							LineChartDTO chartDTO = (LineChartDTO) perfDayDataMap.get(key);
							long imp = StringUtil.getLongValue(chartDTO.getImpressions()) + impressions;
							chartDTO.setImpressions(imp+"");
							perfDayDataMap.put(key, chartDTO);
						}
						else {
							perfDayDataMap.put(key, lineChartDTO);
						}
						dayDateMap.put(date, null);
						tempPartnerMap.put(partnerName, null);
						
						if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {				// Week wise Data
							log.info("thisWeekEndDateValue : "+thisWeekEndDateValue);
							log.info("dateValue : "+dateValue);
							if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
								long thisWeekImpressions = 0;
								for(String weekPartnerKey : weekPartnerImpressionsMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(weekPartnerKey);
									chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekPartnerKey)+"");
									thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekPartnerKey); 
									perfWeekDataMap.put(previousDate+"_"+weekPartnerKey, chartDTO);
									weekPartnerImpressionsMap.put(weekPartnerKey, 0L);
								}
								weekDateMap.put(previousDate, null);
								// week wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Impressions", 0, thisWeekImpressions);
							}
							weekStartDate = date;
							int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
							thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
						}
						log.info("weekPartnerImpressionsMap.get(partnerName) : "+weekPartnerImpressionsMap.get(partnerName));
						if(weekPartnerImpressionsMap.get(partnerName) != null) {
							long weekSiteImpressions = weekPartnerImpressionsMap.get(partnerName);
							weekPartnerImpressionsMap.put(partnerName, weekSiteImpressions+impressions);
						}
						else {
							weekPartnerImpressionsMap.put(partnerName, impressions);
						}
						
						if(thisMonthValue == 0 || thisMonthValue != monthValue) {				// Month wise Data
							log.info("monthValue : "+monthValue);
							log.info("thisMonthValue : "+thisMonthValue);
							if(thisMonthValue != 0 && thisMonthValue != monthValue) {
								long thisMonthImpressions = 0;
								for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(monthSiteKey);
									chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
									thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
									perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
									monthPartnerImpressionsMap.put(monthSiteKey, 0L);
								}
								monthDateMap.put(previousDate, null);
								// month wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Impressions", 0, thisMonthImpressions);
							}
							monthStartDate = date;
							thisMonthValue = monthValue;
						}
						if(monthPartnerImpressionsMap.get(partnerName) != null) {
							long momthSiteClicks = monthPartnerImpressionsMap.get(partnerName);
							monthPartnerImpressionsMap.put(partnerName, momthSiteClicks+impressions);
						}
						else {
							monthPartnerImpressionsMap.put(partnerName, impressions);
						}
						
						previousDate = date;
					}
				}
				log.info("adding week, month data which was left to add.");
				long thisWeekImpressions = 0;
				for(String weekSiteKey : weekPartnerImpressionsMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(weekSiteKey);
					chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekSiteKey)+"");
					thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekSiteKey); 
					perfWeekDataMap.put(previousDate+"_"+weekSiteKey, chartDTO);
				}
				weekDateMap.put(previousDate, null);
				// week wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Impressions", 0, thisWeekImpressions);
				
				long thisMonthImpressions = 0;
				for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(monthSiteKey);
					chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
					thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
					perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
				}
				monthDateMap.put(previousDate, null);
				// month wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Impressions", 0, thisMonthImpressions);
				
				log.info("Added week, month data");
				
				currentPacing = "NA";
				revisedPacing = "NA";
				if(tempPartnerMap != null && tempPartnerMap.size() > 0) {
					if(isShowPacing) {
						String lastDate = DateUtil.getFormatedDate(previousDate, "yyyy-MM-dd", "MM-dd-yyyy");
						dayPassed = dayDurationInFlights(flightObjList, null, lastDate, "MM-dd-yyyy");
						dayRemaining = totalDuration - dayPassed;
						log.info("totalDuration  : "+totalDuration+",dayPassed"+dayPassed+", dayRemaining : "+dayRemaining+", lastDate"+lastDate+", flightObjList"+flightObjList);
						
						currentPacing = (dayPassed > 0)?(totalImpressions/dayPassed)+"":"NA";
						if(dayRemaining > 0) {
							long rp = ((impressionOrClicks - totalImpressions)/dayRemaining);
							if(rp >= 0) {
								revisedPacing = rp+"";
							}
						}
						if(pacingMap!= null) {
							if(pacingMap.get("Target") != null) {
								partnerMap.put("Target", null);
							}
							if(pacingMap.get("Current") != null) {
								partnerMap.put("Current", null);
							}
							if(pacingMap.get("Revised") != null) {
								partnerMap.put("Revised", null);
							}
						}
					}
					for(String site : tempPartnerMap.keySet()) {
						partnerMap.put(site, null);
					}
				}
				if(LinMobileUtil.isNumeric(targetPacing)) {
					targetPacing = lf.format(Math.round(StringUtil.getDoubleValue(targetPacing)))+"";
				}
				if(LinMobileUtil.isNumeric(currentPacing)) {
					currentPacing = lf.format(Math.round(StringUtil.getDoubleValue(currentPacing)))+"";
				}
				if(LinMobileUtil.isNumeric(revisedPacing)) {
					revisedPacing = lf.format(Math.round(StringUtil.getDoubleValue(revisedPacing)))+"";
				}
				jsonObject.put("target", targetPacing);
				jsonObject.put("current", currentPacing);
				jsonObject.put("revised", revisedPacing);

				String dayLineChartJson=createJsonResponseForLineChart(dayDateMap, partnerMap, perfDayDataMap, "Impressions");
				jsonObject.put("day", dayLineChartJson);
				String weekLineChartJson=createJsonResponseForLineChart(weekDateMap, partnerMap, perfWeekDataMap, "Impressions");
				jsonObject.put("week", weekLineChartJson);
				log.info("weekLineChartJson : "+weekLineChartJson);
				String monthChartJson=createJsonResponseForLineChart(monthDateMap, partnerMap, perfMonthDataMap, "Impressions");
				jsonObject.put("month", monthChartJson);
				log.info("monthChartJson : "+monthChartJson);
				
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in impressionsLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in impressionsLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("impressionsData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}

	@Override
	public JSONObject ctrLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo;
		String keyPrefix = "PM_CtrData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonObject != null && jsonObject.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonObject;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			jsonObject = new JSONObject();
		}
		DecimalFormat df3 = new DecimalFormat( "###,###,###,##0.000" );
		JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
		try {
			long totalImpressions = 0;
			long totalClicks = 0;
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) placementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.ctrLineChartData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && partnerInfoJson != null) {
				boolean isShowPacing  = false;
				String targetPacing = "NA";
				String currentPacing = "NA";
				if(placementJson.get("goalCTR") != null) {
					String goalCTR = (placementJson.get("goalCTR")).toString();
					if(goalCTR != null && LinMobileUtil.isNumeric(goalCTR)) {
						isShowPacing = true;
						targetPacing = goalCTR;
					}
				}
				
				List<TableRow> rowList = queryResponse.getRows();
				int thisWeekEndDateValue = 0;
				String weekStartDate = "";
				int thisMonthValue = 0;
				String monthStartDate = "";
				String previousDate = "";
				Map<String, Boolean> pacingMap = new HashMap<>();
				Map<String, Object> dayDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> weekDateMap = new LinkedHashMap<String, Object>();
				Map<String, Object> monthDateMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> partnerMap = new LinkedHashMap<String, Object>();
				Map<String, Object> tempPartnerMap = new LinkedHashMap<String, Object>();
				
				Map<String, Object> perfDayDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfWeekDataMap = new LinkedHashMap<String, Object>();
				Map<String, Object> perfMonthDataMap = new LinkedHashMap<String, Object>();
				
				Map<String, LineChartDTO> weekPartnerCTRMap = new LinkedHashMap<String, LineChartDTO>();
				Map<String, LineChartDTO> monthPartnerCTRMap = new LinkedHashMap<String, LineChartDTO>();
				
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date=cellList.get(0).getV().toString();
						int dateValue=Integer.parseInt((cellList.get(0).getV().toString()).replaceAll("-", ""));
						int monthValue=Integer.parseInt((cellList.get(0).getV().toString()).split("-")[1]);
						
						String partnerName=partnerInfoJson.get(cellList.get(1).getV().toString())+"";
						long clicks=StringUtil.getLongValue(cellList.get(2).getV().toString());
						long impressions=StringUtil.getLongValue(cellList.get(3).getV().toString());
						totalImpressions = totalImpressions + impressions;
						totalClicks = totalClicks + clicks;
						
						if(LinMobileUtil.isNumeric(targetPacing)) {					// Day wise Pacing
							pacingMap.put("Target", true);
							String key=date+"_Target";
							LineChartDTO lineChartDTO=new LineChartDTO();
							lineChartDTO.setDate(date);
							lineChartDTO.setSiteName("Target");
							lineChartDTO.setCtr(targetPacing);
							perfDayDataMap.put(key, lineChartDTO);
							
							currentPacing = (totalImpressions > 0)?((double)(totalClicks*100)/totalImpressions)+"":"NA";
							if(LinMobileUtil.isNumeric(currentPacing)) {
								pacingMap.put("Current", true);
								key=date+"_Current";
								lineChartDTO=new LineChartDTO();
								lineChartDTO.setDate(date);
								lineChartDTO.setSiteName("Current");
								lineChartDTO.setCtr(currentPacing);
								perfDayDataMap.put(key, lineChartDTO);
							}
						}
						
						String key=date+"_"+partnerName;
						LineChartDTO lineChartDTO=new LineChartDTO();				// Day wise Data
						lineChartDTO.setDate(date);
						lineChartDTO.setSiteName(partnerName);
						lineChartDTO.setClicks(clicks+"");
						lineChartDTO.setImpressions(impressions+"");
						lineChartDTO.setCtr((impressions > 0)?((double)(clicks*100)/impressions)+"":"0.00");
						
						if(perfDayDataMap.get(key) != null) {
							LineChartDTO chartDTO = (LineChartDTO) perfDayDataMap.get(key);
							long imp = StringUtil.getLongValue(chartDTO.getImpressions()) + impressions;
							long clk = StringUtil.getLongValue(chartDTO.getClicks()) + clicks;
							chartDTO.setClicks(clk+"");
							chartDTO.setImpressions(imp+"");
							chartDTO.setCtr((impressions > 0)?((double)(clk*100)/imp)+"":"0.00");
							perfDayDataMap.put(key, chartDTO);
						}
						else {
							perfDayDataMap.put(key, lineChartDTO);
						}
						dayDateMap.put(date, null);
						tempPartnerMap.put(partnerName, null);
						
						if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {				// Week wise Data
							log.info("thisWeekEndDateValue : "+thisWeekEndDateValue);
							log.info("dateValue : "+dateValue);
							if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
								long thisWeekClicks = 0;
								long thisWeekImpressions = 0;
								for(String weekPartnerKey : weekPartnerCTRMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(weekPartnerKey);
									long imp = StringUtil.getLongValue(weekPartnerCTRMap.get(weekPartnerKey).getImpressions());
									long clk = StringUtil.getLongValue(weekPartnerCTRMap.get(weekPartnerKey).getClicks());
									thisWeekClicks = thisWeekClicks + clk;
									thisWeekImpressions = thisWeekImpressions + imp;
									chartDTO.setCtr((imp > 0)?((double)(clk*100)/imp)+"":"0.00");
									perfWeekDataMap.put(previousDate+"_"+weekPartnerKey, chartDTO);
									weekPartnerCTRMap.put(weekPartnerKey, new LineChartDTO());
								}
								weekDateMap.put(previousDate, null);
								// week wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, null, weekStartDate, previousDate, "CTR", thisWeekClicks, thisWeekImpressions);
							}
							weekStartDate = date;
							int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
							thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
						}
						log.info("weekPartnerCTRMap.get(partnerName) : "+weekPartnerCTRMap.get(partnerName));
						if(weekPartnerCTRMap.get(partnerName) != null) {
							LineChartDTO chartDTO = weekPartnerCTRMap.get(partnerName);
							chartDTO.setImpressions((StringUtil.getLongValue(chartDTO.getImpressions())+impressions)+"");
							chartDTO.setClicks((StringUtil.getLongValue(chartDTO.getClicks())+clicks)+"");
							weekPartnerCTRMap.put(partnerName, chartDTO);
						}
						else {
							LineChartDTO chartDTO = new LineChartDTO();
							chartDTO.setImpressions(impressions+"");
							chartDTO.setClicks(clicks+"");
							weekPartnerCTRMap.put(partnerName, chartDTO);
						}
						
						if(thisMonthValue == 0 || thisMonthValue != monthValue) {				// Month wise Data
							log.info("monthValue : "+monthValue);
							log.info("thisMonthValue : "+thisMonthValue);
							if(thisMonthValue != 0 && thisMonthValue != monthValue) {
								long thisMonthClicks = 0;
								long thisMonthImpressions = 0;
								for(String monthPartnerKey : monthPartnerCTRMap.keySet()) {
									LineChartDTO chartDTO = new LineChartDTO();
									chartDTO.setDate(previousDate);
									chartDTO.setSiteName(monthPartnerKey);
									long imp = StringUtil.getLongValue(monthPartnerCTRMap.get(monthPartnerKey).getImpressions());
									long clk = StringUtil.getLongValue(monthPartnerCTRMap.get(monthPartnerKey).getClicks());
									thisMonthClicks = thisMonthClicks + clk;
									thisMonthImpressions = thisMonthImpressions + imp;
									chartDTO.setCtr((imp > 0)?((double)(clk*100)/imp)+"":"0.00");
									perfMonthDataMap.put(previousDate+"_"+monthPartnerKey, chartDTO);
									monthPartnerCTRMap.put(monthPartnerKey, new LineChartDTO());
								}
								monthDateMap.put(previousDate, null);
								// month wise pacing
								weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, null, monthStartDate, previousDate, "CTR", thisMonthClicks, thisMonthImpressions);
							}
							monthStartDate = date;
							thisMonthValue = monthValue;
						}
						if(monthPartnerCTRMap.get(partnerName) != null) {
							LineChartDTO chartDTO = monthPartnerCTRMap.get(partnerName);
							chartDTO.setImpressions((StringUtil.getLongValue(chartDTO.getImpressions())+impressions)+"");
							chartDTO.setClicks((StringUtil.getLongValue(chartDTO.getClicks())+clicks)+"");
							monthPartnerCTRMap.put(partnerName, chartDTO);
						}
						else {
							LineChartDTO chartDTO = new LineChartDTO();
							chartDTO.setImpressions(impressions+"");
							chartDTO.setClicks(clicks+"");
							monthPartnerCTRMap.put(partnerName, chartDTO);
						}
						
						previousDate = date;
					}
				}
				// add week, month data which was left to add.
				long thisWeekClicks = 0;
				long thisWeekImpressions = 0;
				for(String weekPartnerKey : weekPartnerCTRMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(weekPartnerKey);
					long imp = StringUtil.getLongValue(weekPartnerCTRMap.get(weekPartnerKey).getImpressions());
					long clk = StringUtil.getLongValue(weekPartnerCTRMap.get(weekPartnerKey).getClicks());
					thisWeekClicks = thisWeekClicks + clk;
					thisWeekImpressions = thisWeekImpressions + imp;
					chartDTO.setCtr((imp > 0)?((double)(clk*100)/imp)+"":"0.00");
					perfWeekDataMap.put(previousDate+"_"+weekPartnerKey, chartDTO);
				}
				weekDateMap.put(previousDate, null);
				// week wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, null, weekStartDate, previousDate, "CTR", thisWeekClicks, thisWeekImpressions);
				
				long thisMonthClicks = 0;
				long thisMonthImpressions = 0;
				for(String monthPartnerKey : monthPartnerCTRMap.keySet()) {
					LineChartDTO chartDTO = new LineChartDTO();
					chartDTO.setDate(previousDate);
					chartDTO.setSiteName(monthPartnerKey);
					long imp = StringUtil.getLongValue(monthPartnerCTRMap.get(monthPartnerKey).getImpressions());
					long clk = StringUtil.getLongValue(monthPartnerCTRMap.get(monthPartnerKey).getClicks());
					thisMonthClicks = thisMonthClicks + clk;
					thisMonthImpressions = thisMonthImpressions + imp;
					chartDTO.setCtr((imp > 0)?((double)(clk*100)/imp)+"":"0.00");
					perfMonthDataMap.put(previousDate+"_"+monthPartnerKey, chartDTO);
				}
				monthDateMap.put(previousDate, null);
				// month wise pacing
				weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, null, monthStartDate, previousDate, "CTR", thisMonthClicks, thisMonthImpressions);
				
				currentPacing = "NA";
				if(tempPartnerMap != null && tempPartnerMap.size() > 0) {
					if(isShowPacing) {
						currentPacing = (totalImpressions > 0)?((double)(totalClicks*100)/totalImpressions)+"":"NA";
						if(pacingMap!= null) {
							if(pacingMap.get("Target") != null) {
								partnerMap.put("Target", null);
							}
							if(pacingMap.get("Current") != null) {
								partnerMap.put("Current", null);
							}
						}
					}
					for(String site : tempPartnerMap.keySet()) {
						partnerMap.put(site, null);
					}
				}
				if(LinMobileUtil.isNumeric(targetPacing)) {
					targetPacing = df3.format(StringUtil.getDoubleValue(targetPacing))+"%";
				}
				if(LinMobileUtil.isNumeric(currentPacing)) {
					currentPacing = df3.format(StringUtil.getDoubleValue(currentPacing))+"%";
				}
				jsonObject.put("target", targetPacing);
				jsonObject.put("current", currentPacing);

				String dayLineChartJson=createJsonResponseForLineChart(dayDateMap, partnerMap, perfDayDataMap, "CTR");
				jsonObject.put("day", dayLineChartJson);
				String weekLineChartJson=createJsonResponseForLineChart(weekDateMap, partnerMap, perfWeekDataMap, "CTR");
				jsonObject.put("week", weekLineChartJson);
				log.info("weekLineChartJson : "+weekLineChartJson);
				String monthChartJson=createJsonResponseForLineChart(monthDateMap, partnerMap, perfMonthDataMap, "CTR");
				jsonObject.put("month", monthChartJson);
				log.info("monthChartJson : "+monthChartJson);
				
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in ctrLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in ctrLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("ctrData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}

	@Override
	public JSONObject flightLineChartData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo, String lineItemPlacementIds, String lineItemPlacementName) {
		int index = 0;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		String memcacheKey = "";
		try {
			List<SmartCampaignFlightDTO> flightObjList = getFlights(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+lineItemPlacementIds+lineItemPlacementName+flightObjList.toString();
			String keyPrefix = "PM_FlightData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject lineItemPlacementNameJson = (JSONObject) JSONSerializer.toJSON(lineItemPlacementName);
			JSONObject lineItemPlacementIdsJson = (JSONObject) JSONSerializer.toJSON(lineItemPlacementIds);
			
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", flightObjList);
			}
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.flightLineChartData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && partnerInfoJson != null && lineItemPlacementIdsJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				long impressionOrClicks = StringUtil.getLongValue(placementJson.get("impressionOrClicks")+"");
				String campaignType = (String) placementJson.get("campaignType");
				String placementName = (String) placementJson.get("placementName");
				
				Map<BigDecimal, List<LineChartDTO>> dateSortedFlightDataMap = new TreeMap<>();
				
				Map<String, List<SmartCampaignFlightDTO>> placementFlightsMap = new HashMap<>();
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all"))) {
					log.info("campaignId : "+campaignId);
					List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
					if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
						for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
							if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlacementName() != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
								placementFlightsMap.put(smartCampaignPlacementObj.getPlacementId()+"<SEP>"+smartCampaignPlacementObj.getPlacementName().trim(), smartCampaignPlacementObj.getFlightObjList());
							}
						}
					}
				}
				else if(placementIds != null) {
					String[] tempArr = placementIds.split(",");
					for (String placementId : tempArr) {
						if(placementId != null && LinMobileUtil.isNumeric(placementId)) {
							SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
							if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlacementName() != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
								placementFlightsMap.put(smartCampaignPlacementObj.getPlacementId()+"<SEP>"+smartCampaignPlacementObj.getPlacementName().trim(), smartCampaignPlacementObj.getFlightObjList());
							}
						}
					}
				}
				
				index = getDateSortedFlightDataMap(rowList, lineItemPlacementIdsJson, lineItemPlacementNameJson, partnerInfoJson, placementFlightsMap, dateSortedFlightDataMap);
				log.info("dateSortedFlightDataMap : "+dateSortedFlightDataMap.size());
				if(dateSortedFlightDataMap != null && dateSortedFlightDataMap.size() > 0) {
					String targetPacing = "NA";
					String currentPacing = "NA";
					String revisedPacing = "NA";
					for(BigDecimal flightDataKey : dateSortedFlightDataMap.keySet()) {
						log.info("flightDataKey : "+flightDataKey.toPlainString());
						List<LineChartDTO> lineChartDTOList = dateSortedFlightDataMap.get(flightDataKey);
						if(lineChartDTOList != null && lineChartDTOList.size() > 0) {
							log.info("lineChartDTOList : "+lineChartDTOList);
							jsonObject = new JSONObject();
							long totalImpressions = 0;
							long totalClicks = 0;
							long totalDuration = 0;
							long dayPassed = 0;
							long dayRemaining = 0;
							
							int thisWeekEndDateValue = 0;
							String weekStartDate = "";
							int thisMonthValue = 0;
							String monthStartDate = "";
							String previousDate = "";
							Map<String, Boolean> pacingMap = new HashMap<>();
							Map<String, Object> dayDateMap = new LinkedHashMap<String, Object>();
							Map<String, Object> weekDateMap = new LinkedHashMap<String, Object>();
							Map<String, Object> monthDateMap = new LinkedHashMap<String, Object>();

							Map<String, Object> partnerMap = new LinkedHashMap<String, Object>();
							Map<String, Object> tempPartnerMap = new LinkedHashMap<String, Object>();

							Map<String, Object> perfDayDataMap = new LinkedHashMap<String, Object>();
							Map<String, Object> perfWeekDataMap = new LinkedHashMap<String, Object>();
							Map<String, Object> perfMonthDataMap = new LinkedHashMap<String, Object>();

							Map<String, Long> weekPartnerImpressionsMap = new LinkedHashMap<String, Long>();
							Map<String, Long> monthPartnerImpressionsMap = new LinkedHashMap<String, Long>();
							
							long flightGoal = 0;
							String flightPlacementName = "";
							String flightGoalString = "NA";
							String flightStartDate = "";
							String flightEndDate = "";
							flightObjList = new ArrayList<>();
							if(lineChartDTOList != null && lineChartDTOList.get(0) != null) {
								flightPlacementName = lineChartDTOList.get(0).getPlacementName();
								flightStartDate = lineChartDTOList.get(0).getStartDate();
								flightEndDate = lineChartDTOList.get(0).getEndDate();
								flightObjList.add(new SmartCampaignFlightDTO("", flightStartDate, flightEndDate, ""));
								totalDuration = (DateUtil.getDifferneceBetweenTwoDates(flightStartDate, lineChartDTOList.get(0).getEndDate(), "MM-dd-yyyy"))+1;
								flightGoalString = lineChartDTOList.get(0).getGoal();
								if(LinMobileUtil.isNumeric(flightGoalString)) {
									flightGoal = StringUtil.getLongValue(flightGoalString);
								}
								if(campaignType.equalsIgnoreCase("CPM") && LinMobileUtil.isNumeric(flightGoalString)) {
									if(placementName.equalsIgnoreCase("All") || (placementJson.get("pacing") != null && !(placementJson.get("pacing")).toString().equalsIgnoreCase("NA"))) {
										targetPacing = (totalDuration > 0)?(flightGoal/totalDuration)+"":"NA";
									}
									else if(placementJson.get("pacing") != null && !(placementJson.get("pacing")).toString().equalsIgnoreCase("NA") &&
											impressionOrClicks != 0 && impressionOrClicks == flightGoal){
										targetPacing = placementJson.get("pacing")+"";
									}
								}
								
								if(lineChartDTOList.get(0).getSiteName() != null && lineChartDTOList.get(0).getSiteName().equals("FLIGHT_NOT_YET_STARTED") || lineChartDTOList.get(0).getSiteName().equals("NO_DATA")) {
									log.info("lineChartDTOList.get(0).getSiteName() : "+lineChartDTOList.get(0).getSiteName());
									jsonObject.put("flightName", flightStartDate+" - "+flightEndDate +" ("+flightPlacementName+")");
									jsonObject.put("goal", flightGoalString);
									jsonObject.put("target", "--");
									jsonObject.put("current", "--");
									jsonObject.put("revised", "--");
									if(lineChartDTOList.get(0).getSiteName().equals("FLIGHT_NOT_YET_STARTED")) {
										jsonObject.put("flightStatus", "notStarted");
									}
									else {
										jsonObject.put("flightStatus", "noData");
									}
									if(campaignType.equalsIgnoreCase("CPM")) {
										jsonObject.put("delivered", 0);
										jsonObject.put("goalLabel", "Goal Impressions");
									}
									else if(campaignType.equalsIgnoreCase("CPC")) {
										jsonObject.put("delivered", 0);
										jsonObject.put("goalLabel", "Goal Clicks");
									}
									jsonArray.add(jsonObject);
									continue;
								}
								
							}
							
							for (LineChartDTO flightLineChartDTO : lineChartDTOList) {
								if(flightLineChartDTO != null) {
									String date=flightLineChartDTO.getDate();
									int dateValue=Integer.parseInt(date.replaceAll("-", ""));
									int monthValue=Integer.parseInt(date.split("-")[1]);
									String partnerName=flightLineChartDTO.getSiteName();
									long impressions=StringUtil.getLongValue(flightLineChartDTO.getImpressions());
									totalImpressions = totalImpressions + impressions;
									long clicks=StringUtil.getLongValue(flightLineChartDTO.getClicks());
									totalClicks = totalClicks + clicks;
									
									if(LinMobileUtil.isNumeric(targetPacing)) {					// Day wise Pacing
										pacingMap.put("Target", true);
										String key=date+"_Target";
										LineChartDTO lineChartDTO=new LineChartDTO();
										lineChartDTO.setDate(date);
										lineChartDTO.setSiteName("Target");
										lineChartDTO.setImpressions(targetPacing);
										perfDayDataMap.put(key, lineChartDTO);
										
										String currentDate = DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy");
										dayPassed = (DateUtil.getDifferneceBetweenTwoDates(flightStartDate, currentDate, "MM-dd-yyyy"))+1;
										dayRemaining = totalDuration - dayPassed;
										
										currentPacing = (dayPassed > 0)?(totalImpressions/dayPassed)+"":"NA";
										if(LinMobileUtil.isNumeric(currentPacing)) {
											pacingMap.put("Current", true);
											key=date+"_Current";
											lineChartDTO=new LineChartDTO();
											lineChartDTO.setDate(date);
											lineChartDTO.setSiteName("Current");
											lineChartDTO.setImpressions(currentPacing);
											perfDayDataMap.put(key, lineChartDTO);
										}
										
										if(dayRemaining > 0) {
											long rp = ((flightGoal - totalImpressions)/dayRemaining);
											if(rp >= 0) {
												revisedPacing = rp+"";
												pacingMap.put("Revised", true);
												key=date+"_Revised";
												lineChartDTO=new LineChartDTO();
												lineChartDTO.setDate(date);
												lineChartDTO.setSiteName("Revised");
												lineChartDTO.setImpressions(revisedPacing);
												perfDayDataMap.put(key, lineChartDTO);
											}
										}
									}
									
									String key=date+"_"+partnerName;
									LineChartDTO lineChartDTO=new LineChartDTO();
									lineChartDTO.setDate(date);
									lineChartDTO.setSiteName(partnerName);
									lineChartDTO.setImpressions(impressions+"");
									
									if(perfDayDataMap.get(key) != null) {		// Day wise Data
										LineChartDTO chartDTO = (LineChartDTO) perfDayDataMap.get(key);
										long imp = StringUtil.getLongValue(chartDTO.getImpressions()) + impressions;
										chartDTO.setImpressions(imp+"");
										perfDayDataMap.put(key, chartDTO);
									}
									else {
										perfDayDataMap.put(key, lineChartDTO);
									}
									dayDateMap.put(date, null);
									tempPartnerMap.put(partnerName, null);
									
									if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {				// Week wise Data
										//log.info("thisWeekEndDateValue : "+thisWeekEndDateValue);
										//log.info("dateValue : "+dateValue);
										if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
											long thisWeekImpressions = 0;
											for(String weekPartnerKey : weekPartnerImpressionsMap.keySet()) {
												LineChartDTO chartDTO = new LineChartDTO();
												chartDTO.setDate(previousDate);
												chartDTO.setSiteName(weekPartnerKey);
												chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekPartnerKey)+"");
												thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekPartnerKey); 
												perfWeekDataMap.put(previousDate+"_"+weekPartnerKey, chartDTO);
												weekPartnerImpressionsMap.put(weekPartnerKey, 0L);
											}
											weekDateMap.put(previousDate, null);
											// week wise pacing
											weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Impressions", 0, thisWeekImpressions);
										}
										weekStartDate = date;
										int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
										thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
									}
									//log.info("weekPartnerImpressionsMap.get(partnerName) : "+weekPartnerImpressionsMap.get(partnerName));
									if(weekPartnerImpressionsMap.get(partnerName) != null) {
										long weekSiteImpressions = weekPartnerImpressionsMap.get(partnerName);
										weekPartnerImpressionsMap.put(partnerName, weekSiteImpressions+impressions);
									}
									else {
										weekPartnerImpressionsMap.put(partnerName, impressions);
									}
									
									if(thisMonthValue == 0 || thisMonthValue != monthValue) {				// Month wise Data
										//log.info("monthValue : "+monthValue);
										//log.info("thisMonthValue : "+thisMonthValue);
										if(thisMonthValue != 0 && thisMonthValue != monthValue) {
											long thisMonthImpressions = 0;
											for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
												LineChartDTO chartDTO = new LineChartDTO();
												chartDTO.setDate(previousDate);
												chartDTO.setSiteName(monthSiteKey);
												chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
												thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
												perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
												monthPartnerImpressionsMap.put(monthSiteKey, 0L);
											}
											monthDateMap.put(previousDate, null);
											// month wise pacing
											weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Impressions", 0, thisMonthImpressions);
										}
										monthStartDate = date;
										thisMonthValue = monthValue;
									}
									if(monthPartnerImpressionsMap.get(partnerName) != null) {
										long momthSiteClicks = monthPartnerImpressionsMap.get(partnerName);
										monthPartnerImpressionsMap.put(partnerName, momthSiteClicks+impressions);
									}
									else {
										monthPartnerImpressionsMap.put(partnerName, impressions);
									}
									
									previousDate = date;
								}
							}
							log.info("adding week, month data which was left to add.");
							long thisWeekImpressions = 0;
							for(String weekSiteKey : weekPartnerImpressionsMap.keySet()) {
								LineChartDTO chartDTO = new LineChartDTO();
								chartDTO.setDate(previousDate);
								chartDTO.setSiteName(weekSiteKey);
								chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekSiteKey)+"");
								thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekSiteKey); 
								perfWeekDataMap.put(previousDate+"_"+weekSiteKey, chartDTO);
							}
							weekDateMap.put(previousDate, null);
							// week wise pacing
							weekOrMonthPacing(targetPacing, pacingMap, perfWeekDataMap, flightObjList, weekStartDate, previousDate, "Impressions", 0, thisWeekImpressions);

							long thisMonthImpressions = 0;
							for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
								LineChartDTO chartDTO = new LineChartDTO();
								chartDTO.setDate(previousDate);
								chartDTO.setSiteName(monthSiteKey);
								chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
								thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
								perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
							}
							monthDateMap.put(previousDate, null);
							// month wise pacing
							weekOrMonthPacing(targetPacing, pacingMap, perfMonthDataMap, flightObjList, monthStartDate, previousDate, "Impressions", 0, thisMonthImpressions);
							log.info("Added week, month data");
							
							currentPacing = "NA";
							revisedPacing = "NA";
							if(tempPartnerMap != null && tempPartnerMap.size() > 0) {
								log.info("tempPartnerMap.size : "+tempPartnerMap.size());
								if(LinMobileUtil.isNumeric(targetPacing)) {
									String lastDate = DateUtil.getFormatedDate(previousDate, "yyyy-MM-dd", "MM-dd-yyyy");
									dayPassed = dayDurationInFlights(flightObjList, null, lastDate, "MM-dd-yyyy");
									dayRemaining = totalDuration - dayPassed;
									log.info("totalDuration  : "+totalDuration+",dayPassed"+dayPassed+", dayRemaining : "+dayRemaining+", flightObjList"+flightObjList+", lastDate : "+lastDate);
									
									currentPacing = (dayPassed > 0)?(totalImpressions/dayPassed)+"":"NA";
									if(dayRemaining > 0) {
										long rp = ((flightGoal - totalImpressions)/dayRemaining);
										if(rp >= 0) {
											revisedPacing = rp+"";
										}
									}
									if(pacingMap != null && pacingMap.size() > 0) {
										log.info("pacingMap : "+pacingMap);
										if(pacingMap.get("Target") != null) {
											partnerMap.put("Target", null);
										}
										if(pacingMap.get("Current") != null) {
											partnerMap.put("Current", null);
										}
										if(pacingMap.get("Revised") != null) {
											partnerMap.put("Revised", null);
										}
									}
									targetPacing = lf.format(Math.round(StringUtil.getDoubleValue(targetPacing)))+"";
									log.info("targetPacing : "+targetPacing);
								}
								for(String site : tempPartnerMap.keySet()) {
									partnerMap.put(site, null);
								}
								log.info("2. partnerMap.size : "+partnerMap.size());
							}
							if(LinMobileUtil.isNumeric(currentPacing)) {
								currentPacing = lf.format(Math.round(StringUtil.getDoubleValue(currentPacing)))+"";
							}
							if(LinMobileUtil.isNumeric(revisedPacing)) {
								revisedPacing = lf.format(Math.round(StringUtil.getDoubleValue(revisedPacing)))+"";
							}
							jsonObject.put("flightName", flightStartDate+" - "+flightEndDate +" ("+flightPlacementName+")");
							
							String dayLineChartJson=createJsonResponseForLineChart(dayDateMap, partnerMap, perfDayDataMap, "Impressions");
							jsonObject.put("day", dayLineChartJson);
							String weekLineChartJson=createJsonResponseForLineChart(weekDateMap, partnerMap, perfWeekDataMap, "Impressions");
							jsonObject.put("week", weekLineChartJson);
							log.info("weekLineChartJson : "+weekLineChartJson);
							String monthChartJson=createJsonResponseForLineChart(monthDateMap, partnerMap, perfMonthDataMap, "Impressions");
							jsonObject.put("month", monthChartJson);
							log.info("monthChartJson : "+monthChartJson);
							
							jsonObject.put("target", targetPacing);
							jsonObject.put("current", currentPacing);
							jsonObject.put("revised", revisedPacing);
							jsonObject.put("goal", flightGoalString);
							if(campaignType.equalsIgnoreCase("CPM")) {
								jsonObject.put("delivered", totalImpressions);
								jsonObject.put("goalLabel", "Goal Impressions");
							}
							else if(campaignType.equalsIgnoreCase("CPC")) {
								jsonObject.put("delivered", totalClicks);
								jsonObject.put("goalLabel", "Goal Clicks");
							}
							jsonArray.add(jsonObject);
							log.info("jsonArray.size : "+jsonArray.size());
						}
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in flightLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in flightLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		log.info("jsonArray.size() : "+jsonArray.size());
		if(jsonArray != null && jsonArray.size() > 0) {
			retunJson.put("flightData", jsonArray);
			retunJson.put("index", index);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	private int getDateSortedFlightDataMap(List<TableRow> rowList, JSONObject lineItemPlacementIdsJson, JSONObject lineItemPlacementNameJson, JSONObject partnerInfoJson, Map<String, List<SmartCampaignFlightDTO>> placementFlightsMap, Map<BigDecimal, List<LineChartDTO>> flightDataMap) throws Exception {
		log.info("flightDataMap : "+flightDataMap);
		int index = 0;
		try {
			if(rowList != null && rowList.size() > 0 && lineItemPlacementIdsJson != null && lineItemPlacementNameJson != null) {
				ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
				List<SmartCampaignFlightDTO> flightObjList = new ArrayList<>();
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date = cellList.get(0).getV().toString();
						String placementId = lineItemPlacementIdsJson.get(cellList.get(1).getV().toString())+"";
						String placementName = lineItemPlacementNameJson.get(cellList.get(1).getV().toString())+"";
						String partnerName=partnerInfoJson.get(cellList.get(1).getV().toString())+"";
						long impressions=StringUtil.getLongValue(cellList.get(2).getV().toString());
						long clicks=StringUtil.getLongValue(cellList.get(3).getV().toString());
						String mapKey = placementId+"<SEP>"+placementName;
						if(placementFlightsMap.containsKey(mapKey)) {
							flightObjList = placementFlightsMap.get(mapKey);
						}
						else {
							SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
							if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getFlightObjList() != null && smartCampaignPlacementObj.getFlightObjList().size() > 0) {
								flightObjList = smartCampaignPlacementObj.getFlightObjList();
								placementFlightsMap.put(mapKey, flightObjList);
							}
						}
						if(flightObjList != null && flightObjList.size() > 0) {
							SmartCampaignFlightDTO smartCampaignFlightDTO = getRunningFlight(DateUtil.getFormatedDate(date, "yyyy-MM-dd", "MM-dd-yyyy"), flightObjList, "MM-dd-yyyy");
							if(smartCampaignFlightDTO != null) {
								String startDate = smartCampaignFlightDTO.getStartDate();
								String endDate = smartCampaignFlightDTO.getEndDate();
								String goal = smartCampaignFlightDTO.getGoal();
								LineChartDTO lineChartDTO = new LineChartDTO();
								lineChartDTO.setDate(date);
								lineChartDTO.setStartDate(startDate);
								lineChartDTO.setEndDate(endDate);
								lineChartDTO.setGoal(goal);
								lineChartDTO.setSiteName(partnerName);
								lineChartDTO.setPlacementName(placementName);
								lineChartDTO.setImpressions(impressions+"");
								lineChartDTO.setClicks(clicks+"");
								BigDecimal flightDataKey = new BigDecimal(DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyyMMdd")+DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyyMMdd")+placementId);
								log.info("startDate : "+startDate+", endDate : "+endDate+", flightDataKey : "+flightDataKey+", placementId : "+placementId);
								if(flightDataMap.containsKey(flightDataKey)) {
									List<LineChartDTO> lineChartDTOList = flightDataMap.get(flightDataKey);
									lineChartDTOList.add(lineChartDTO);
								}
								else {
									List<LineChartDTO> lineChartDTOList = new ArrayList<>();
									lineChartDTOList.add(lineChartDTO);
									flightDataMap.put(flightDataKey, lineChartDTOList);
								}
							}
						}
					}
				}
			}
			
			log.info("flightDataMap.size: "+flightDataMap.size());
			log.info("flightDataMap : "+flightDataMap);
			log.info("placementFlightsMap.size: "+placementFlightsMap.size()); 
			log.info("placementFlightsMap : "+placementFlightsMap);
			long thisDayValue = StringUtil.getLongValue(DateUtil.getCurrentTimeStamp("yyyyMMdd"));
			for(String key : placementFlightsMap.keySet()) {		// Add flights not yet started
				String[] temp  = key.split("<SEP>");
				if(temp.length == 2) {
					String placementId = temp[0];
					String placementName = temp[1];
					List<SmartCampaignFlightDTO> flightDTOs = placementFlightsMap.get(key);
					if(flightDTOs != null && flightDTOs.size() > 0){
						for (SmartCampaignFlightDTO smartCampaignFlightDTO : flightDTOs) {
							if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
								String startDate = smartCampaignFlightDTO.getStartDate();
								String endDate = smartCampaignFlightDTO.getEndDate();
								String goal = smartCampaignFlightDTO.getGoal();
								BigDecimal flightDataKey = new BigDecimal(DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyyMMdd")+DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyyMMdd")+placementId);
								if(!(flightDataMap.containsKey(flightDataKey))) {
									List<LineChartDTO> lineChartDTOList = new ArrayList<>();
									LineChartDTO lineChartDTO = new LineChartDTO();
									lineChartDTO.setStartDate(startDate);
									lineChartDTO.setEndDate(endDate);
									lineChartDTO.setGoal(goal);
									lineChartDTO.setPlacementName(placementName);
									long startValue = StringUtil.getLongValue((flightDataKey.toPlainString()).substring(0, 8));
									if(startValue > thisDayValue) {
										lineChartDTO.setSiteName("FLIGHT_NOT_YET_STARTED");
									}
									else {
										lineChartDTO.setSiteName("NO_DATA");
									}
									lineChartDTOList.add(lineChartDTO);
									flightDataMap.put(flightDataKey, lineChartDTOList);
								}
							}
						}
					}
				}
			}
			
			log.info("flightDataMap.size: "+flightDataMap.size());
			log.info("flightDataMap : "+flightDataMap);
			if(flightDataMap != null && flightDataMap.size() > 0) {		// find default selected flight index
				long latestByDays = 1000000;
				int count = 0;
				boolean found = false;
				for(BigDecimal key : flightDataMap.keySet()) {
					log.info("flightDataMap key :"+key.toPlainString()+", "+flightDataMap.get(key));
					String keyStr = key.toPlainString();
					long startValue = StringUtil.getLongValue(keyStr.substring(0, 8));
					long endValue = StringUtil.getLongValue(keyStr.substring(8, 16));
					if(startValue <= thisDayValue && endValue >= thisDayValue) {		// if running flight
						found = true;
						break;
					}
					else if(startValue < thisDayValue && (thisDayValue - endValue) >= 0 && (thisDayValue - endValue) < latestByDays) {	   // if latest flight but completed flight
						index = count;
						latestByDays = thisDayValue - startValue;
					}
					count++;
				}
				if(found) {
					index = count;
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getDateSortedFlightDataMap of PerformanceMonitoringService : "+e.getMessage());
			throw e;
		}
		return index;
	}
	
	private SmartCampaignFlightDTO getRunningFlight(String date, List<SmartCampaignFlightDTO> flightObjList, String dateFormat) {
		try {
			if(date != null && flightObjList != null && flightObjList.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        	Date currentDate = sdf.parse(date);
				for (SmartCampaignFlightDTO smartCampaignFlightDTO : flightObjList) {
					if(smartCampaignFlightDTO != null && smartCampaignFlightDTO.getStartDate() != null && smartCampaignFlightDTO.getEndDate() != null) {
						Date startDate = sdf.parse(smartCampaignFlightDTO.getStartDate());
						Date endDate = sdf.parse(smartCampaignFlightDTO.getEndDate());
						if(startDate.compareTo(currentDate)<=0 && endDate.compareTo(currentDate)>=0) {
							if(smartCampaignFlightDTO.getGoal() == null || !(LinMobileUtil.isNumeric(smartCampaignFlightDTO.getGoal()))) {
								smartCampaignFlightDTO.setGoal("NA");
							}
							return smartCampaignFlightDTO;
						}
					}
				}
			}
		}catch (ParseException e) {
			log.severe("ParseException in getRunningFlight in PerformanceMonitoringService"+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in getRunningFlight in PerformanceMonitoringService"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public JSONObject locationCompleteData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo;
		String keyPrefix = "PM_LocationCompleteData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonObject != null && jsonObject.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonObject;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			jsonObject = new JSONObject();
		}
		try {
		    log.info("Going to create placement Json : placementInfo : "+placementInfo);
		    long totalImpressions = 0;
		    JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) placementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
		    log.info("Going for query DTO.......");
			QueryResponse withInGeoQueryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				withInGeoQueryResponse = monitoringDAO.locationCompleteData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}else{
				throw new Exception("Failed to load queryDTO (BigQuery tables)...");
			}
			if (withInGeoQueryResponse!=null && withInGeoQueryResponse.getRows() != null 
					&& withInGeoQueryResponse.getRows().size() > 0) {
				List<TableRow> rowList = withInGeoQueryResponse.getRows();
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						totalImpressions = totalImpressions + StringUtil.getLongValue(StringUtil.deleteFromLastOccurence(cellList.get(2).getV().toString(), "."));
						log.info("totalImpressions : "+totalImpressions + ", cellList : "+cellList.get(2).getV().toString());
					}
				}
				
				DataTable datatable = new DataTable();
				List<String> list = new ArrayList<String>();
				datatable = GoogleVisulizationUtil.buildColumns(withInGeoQueryResponse,list);
				datatable = GoogleVisulizationUtil.buildRows(datatable, withInGeoQueryResponse);
				java.lang.CharSequence jsonStr = JsonRenderer.renderDataTable(datatable, true, false);
				jsonObject.put("geoChart", jsonStr.toString());
				jsonObject.put("impressions", totalImpressions);
				
			}else{
				throw new Exception("No query response found .."+withInGeoQueryResponse);
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in locationCompleteData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in locationCompleteData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("locationCompleteData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	@Override
	public JSONObject locationTopCitiesData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		String memcacheKey = "";
		try {
			Map geoTargetMap = getGeoTargeting(campaignId, placementIds);
			List<String> dmaCodeList = (List<String>) geoTargetMap.get("dmaCodeList");
			List<String> cityNameList = (List<String>) geoTargetMap.get("cityNameList");
			List<String> stateNameList = (List<String>) geoTargetMap.get("stateNameList");
			memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+dmaCodeList+cityNameList+stateNameList;
			String keyPrefix = "PM_LocationTopCitiesData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
			}
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			QueryResponse topCityQueryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				topCityQueryResponse = monitoringDAO.locationTopCitiesData(orderId, lineItemIds, isNoise, threshold, queryDTO);
			}
			if (topCityQueryResponse!=null && topCityQueryResponse.getRows() != null && topCityQueryResponse.getRows().size() > 0) {
				if(dmaCodeList == null) {
					dmaCodeList = new ArrayList<>();
				}
				List<TableRow> rowList = topCityQueryResponse.getRows();
				int count = 0;
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						jsonObject = new JSONObject();
						List<TableCell> cellList = row.getF();
						if(cellList.get(0).getV().toString() != null && !(cellList.get(0).getV().toString().equals("null")) && !(cellList.get(0).getV().toString().equals("NA")) && !(cellList.get(0).getV().toString().equals("N/A"))) {
							String city = cellList.get(0).getV().toString();
							String state = cellList.get(3).getV().toString();
							String dma = cellList.get(4).getV().toString();
							jsonObject.put("cityName", (++count)+". " + city+", "+state);
							jsonObject.put("impression", lf.format(StringUtil.getLongValue(StringUtil.deleteFromLastOccurence(cellList.get(1).getV().toString(), ".")))+" IMP");
							jsonObject.put("ctr", cellList.get(2).getV().toString()+"% CTR");
							if(cityNameList == null || cityNameList.size() == 0 || (cityNameList.contains(ProductService.allOption)) || (city != null && cityNameList.contains(city)) 
									|| stateNameList == null || stateNameList.size() == 0 || (stateNameList.contains(ProductService.allOption)) || (state != null && stateNameList.contains(state)) 
									|| dmaCodeList == null || dmaCodeList.size() == 0 || (dmaCodeList.contains(ProductService.allOptionId)) || (dma != null && dmaCodeList.contains(dma))) {
								jsonObject.put("isOutsideTarget", "white");
							}else {
								jsonObject.put("isOutsideTarget", "yellow");
							}
							
							jsonArray.add(jsonObject);
						}
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in locationTopCitiesData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in locationTopCitiesData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		boolean isMemcacheRequired = false;
		if(jsonArray.size() == 0) {
			jsonObject = new JSONObject();
			jsonObject.put("cityName", "No Data");
			jsonObject.put("impression", "");
			jsonObject.put("ctr", "");
			jsonObject.put("isOutsideTarget", "white");
			jsonArray.add(jsonObject);
		}
		else {
			isMemcacheRequired = true;
		}
		retunJson.put("topCities", jsonArray);
		if(isMemcacheRequired) {
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	@Override
	public JSONObject locationTargetData(String orderId, String campaignId, String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = "";
		long totalImpressions = 0;
		String geoTargeted = "";
		try {
			Map geoTargetMap = getGeoTargeting(campaignId, placementIds);
			memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+geoTargetMap.toString();
			String keyPrefix = "PM_LocationTargetData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
				jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
			}
			if(jsonObject != null && jsonObject.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonObject;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonObject = new JSONObject();
			}
		
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) placementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_PERFORMANCE_BY_LOCATION);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				
				String stateStr = geoTargetMap.get("state").toString();
				String cityStr = geoTargetMap.get("city").toString();
				String dmaStr = geoTargetMap.get("dma").toString();
				List<String> cityNameList = (List<String>) geoTargetMap.get("cityNameList");
				List<String> stateNameList = (List<String>) geoTargetMap.get("stateNameList");
				List<String> dmaCodeList = (List<String>) geoTargetMap.get("dmaCodeList");
				if((cityNameList == null || cityNameList.size() == 0 || cityNameList.contains(ProductService.allOption))
						|| (stateNameList == null || stateNameList.size() == 0 || stateNameList.contains(ProductService.allOption)) 
						|| (dmaCodeList == null || dmaCodeList.size() == 0 || dmaCodeList.contains(ProductService.allOptionId))) {
					// Not Targeted
					geoTargeted = "All";
				}
				else {
					queryResponse = monitoringDAO.locationTargetData(orderId, lineItemIds, isNoise, threshold, queryDTO, stateStr, cityStr, dmaStr);
					if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() == 1) {
						TableRow row = queryResponse.getRows().get(0);
						if(row != null && row.getF() != null && row.getF().size() == 1) {
							log.info("row.getF().get(0).toString() : "+row.getF().get(0).toString());
							totalImpressions = StringUtil.getLongValue(StringUtil.deleteFromLastOccurence(row.getF().get(0).getV().toString(), "."));
						}
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in locationTargetData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in locationTargetData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		jsonObject.put("geoTargeted", geoTargeted);
		jsonObject.put("targetImpressions", totalImpressions);
		if(totalImpressions > 0) {
			MemcacheUtil.setObjectInCache(memcacheKey, jsonObject.toString(), EXPIRATION_TIME);
		}
		return jsonObject;
	}
	
	private Map getGeoTargeting(String campaignId, String placementIds) throws Exception {
		long allOptionId = StringUtil.getLongValue(ProductService.allOptionId);
		Map geoTargetMap = new HashMap<>();
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = new ArrayList<>();
		if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all"))) {
			log.info("campaignId : "+campaignId);
			smartCampaignPlacementObjList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
		}
		else if(placementIds != null && LinMobileUtil.isNumeric(placementIds)) {
			String[] tempArr = placementIds.split(",");
			for (String placementId : tempArr) {
				if(placementId != null && LinMobileUtil.isNumeric(placementId)) {
					SmartCampaignPlacementObj smartCampaignPlacementObj = smartCampaignPlannerDAO.getPlacementById(StringUtil.getLongValue(placementId));
					if(smartCampaignPlacementObj != null) {
						smartCampaignPlacementObjList.add(smartCampaignPlacementObj);
					}
				}
			}
		}
		
		List<String> stateList = new ArrayList<>();
		List<String> cityList = new ArrayList<>();
		List<String> dmaList = new ArrayList<>();
		String stateStr = "";
		String cityStr = "";
		String dmaStr = "";
		if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
			for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
				if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getStateObj() != null && smartCampaignPlacementObj.getStateObj().size() > 0) {
					for(StateObj stateObj : smartCampaignPlacementObj.getStateObj()) {
						if(stateObj != null && stateObj.getText() != null && !(stateList.contains(stateObj.getText()))) {
							if(!stateObj.getText().equals(ProductService.allOption)) {
								stateStr = stateStr + stateObj.getText() + "','";
							}
							stateList.add(stateObj.getText());
						}
					}
				}
				if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getCityObj() != null && smartCampaignPlacementObj.getCityObj().size() > 0) {
					for(CityDTO cityDTO : smartCampaignPlacementObj.getCityObj()) {
						if(cityDTO != null && cityDTO.getText() != null && !(cityList.contains(cityDTO.getText()))) {
							if(!cityDTO.getText().equals(ProductService.allOption)) {
								cityStr = cityStr + cityDTO.getText() + "','";
							}
							cityList.add(cityDTO.getText());
						}
					}
				}
				if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getGeoObj() != null && smartCampaignPlacementObj.getGeoObj().size() > 0) {
					for(GeoTargetsObj geoTargetsObj : smartCampaignPlacementObj.getGeoObj()) {
						if(geoTargetsObj != null && geoTargetsObj.getGeoTargetId() > 0 && !(dmaList.contains(geoTargetsObj.getGeoTargetId()+""))) {
							if(geoTargetsObj.getGeoTargetId() != allOptionId) {
								dmaStr = dmaStr + geoTargetsObj.getGeoTargetId() + "','";
							}
							dmaList.add(geoTargetsObj.getGeoTargetId()+"");
						}
					}
				}
			}
			stateStr = StringUtil.deleteFromLastOccurence(stateStr, "','");
			cityStr = StringUtil.deleteFromLastOccurence(cityStr, "','");
			dmaStr = StringUtil.deleteFromLastOccurence(dmaStr, "','");
		}
		geoTargetMap.put("state", stateStr);
		geoTargetMap.put("city", cityStr);
		geoTargetMap.put("dma", dmaStr);
		geoTargetMap.put("dmaCodeList", dmaList);
		geoTargetMap.put("cityNameList", cityList);
		geoTargetMap.put("stateNameList", stateList);
		log.info("stateStr : "+stateStr+", cityStr : "+cityStr+", dmaStr : "+dmaStr);
		log.info("dmaCodeList : "+dmaList+", cityNameList : "+cityList+", stateNameList : "+stateList);
		
		return geoTargetMap;
	}
	
	public JSONObject richMediaLineChartData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo, String partnerInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+placementInfo+partnerInfo;
		String keyPrefix = "PM_RMLineChartData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonObject != null && jsonObject.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonObject;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
		try {
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CUSTOM_EVENT);
			
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.richMediaLineChartData(orderId, lineItemIds, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && partnerInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				
				Map<String, List<LineChartDTO>> eventWiseDataMap = getEventWiseDataMap(rowList, partnerInfoJson);
				log.info("eventWiseDataMap : "+eventWiseDataMap);
				if(eventWiseDataMap != null && eventWiseDataMap.size() > 0) {
					log.info("eventWiseDataMap.size() : "+eventWiseDataMap.size());
					for(String eventKey : eventWiseDataMap.keySet()) {
						log.info("eventKey : "+eventKey);
						List<LineChartDTO> lineChartDTOList = eventWiseDataMap.get(eventKey);
						if(lineChartDTOList != null && lineChartDTOList.size() > 0) {
							log.info("lineChartDTOList.size() : "+lineChartDTOList.size());
							log.info("lineChartDTOList : "+lineChartDTOList);
							jsonObject = new JSONObject();
							long dayImpressions = 0;
							
							int thisWeekEndDateValue = 0;
							String weekStartDate = "";
							int thisMonthValue = 0;
							String monthStartDate = "";
							String previousDate = "";
							Map<String, Object> dayDateMap = new LinkedHashMap<String, Object>();
							Map<String, Object> weekDateMap = new LinkedHashMap<String, Object>();
							Map<String, Object> monthDateMap = new LinkedHashMap<String, Object>();

							Map<String, Object> partnerMap = new LinkedHashMap<String, Object>();

							Map<String, Object> perfDayDataMap = new LinkedHashMap<String, Object>();
							Map<String, Object> perfWeekDataMap = new LinkedHashMap<String, Object>();
							Map<String, Object> perfMonthDataMap = new LinkedHashMap<String, Object>();

							Map<String, Long> weekPartnerImpressionsMap = new LinkedHashMap<String, Long>();
							Map<String, Long> monthPartnerImpressionsMap = new LinkedHashMap<String, Long>();
							
							for (LineChartDTO eventLineChartDTO : lineChartDTOList) {
								if(eventLineChartDTO != null) {
									Map<String, String> partnerCountForDay = new HashMap<>();
									String date=eventLineChartDTO.getDate();
									int dateValue=Integer.parseInt(date.replaceAll("-", ""));
									int monthValue=Integer.parseInt(date.split("-")[1]);
									String partnerName=eventLineChartDTO.getSiteName();
									long impressions=StringUtil.getLongValue(eventLineChartDTO.getImpressions());
									
									String key=date+"_"+partnerName;
									
									LineChartDTO lineChartDTO=new LineChartDTO();									// Day wise Data
									lineChartDTO.setDate(date);
									lineChartDTO.setSiteName(partnerName);
									lineChartDTO.setImpressions(impressions+"");
									dayImpressions = impressions;
									
									if(perfDayDataMap.containsKey(key)) {
										LineChartDTO chartDTO = (LineChartDTO) perfDayDataMap.get(key);
										long imp = StringUtil.getLongValue(chartDTO.getImpressions()) + impressions;
										chartDTO.setImpressions(imp+"");
										dayImpressions = imp;
										perfDayDataMap.put(key, chartDTO);
									}
									else {
										perfDayDataMap.put(key, lineChartDTO);
									}
									
									if(!(date.equals(previousDate))) {												// Day wise Average
										partnerCountForDay.clear();
									}
									if(!(partnerCountForDay.containsKey(partnerName))) {
										partnerCountForDay.put(partnerName, partnerName);
									}
									long dayPartnerCount = partnerCountForDay.size();
									String dayAveragekey=date+"_Average";
									lineChartDTO=new LineChartDTO();
									lineChartDTO.setDate(date);
									lineChartDTO.setSiteName("Average");
									lineChartDTO.setImpressions((dayImpressions/dayPartnerCount)+"");
									perfDayDataMap.put(dayAveragekey, lineChartDTO);
									partnerMap.put("Average", null);
									
									dayDateMap.put(date, null);
									partnerMap.put(partnerName, null);
									
									if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {				// Week wise Data
										//log.info("thisWeekEndDateValue : "+thisWeekEndDateValue);
										//log.info("dateValue : "+dateValue);
										if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
											long thisWeekImpressions = 0;
											int countOfPartner = weekPartnerImpressionsMap.size();
											for(String weekPartnerKey : weekPartnerImpressionsMap.keySet()) {
												LineChartDTO chartDTO = new LineChartDTO();
												chartDTO.setDate(previousDate);
												chartDTO.setSiteName(weekPartnerKey);
												chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekPartnerKey)+"");
												thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekPartnerKey); 
												perfWeekDataMap.put(previousDate+"_"+weekPartnerKey, chartDTO);
												weekPartnerImpressionsMap.put(weekPartnerKey, 0L);
											}
											weekDateMap.put(previousDate, null);
											// week wise Average
											weekOrMonthAverage(countOfPartner, previousDate, thisWeekImpressions, perfWeekDataMap);
										}
										weekStartDate = date;
										int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
										thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
									}
									//log.info("weekPartnerImpressionsMap.get(partnerName) : "+weekPartnerImpressionsMap.get(partnerName));
									if(weekPartnerImpressionsMap.get(partnerName) != null) {
										long weekSiteImpressions = weekPartnerImpressionsMap.get(partnerName);
										weekPartnerImpressionsMap.put(partnerName, weekSiteImpressions+impressions);
									}
									else {
										weekPartnerImpressionsMap.put(partnerName, impressions);
									}
									
									if(thisMonthValue == 0 || thisMonthValue != monthValue) {				// Month wise Data
										//log.info("monthValue : "+monthValue);
										//log.info("thisMonthValue : "+thisMonthValue);
										if(thisMonthValue != 0 && thisMonthValue != monthValue) {
											long thisMonthImpressions = 0;
											int countOfPartner = monthPartnerImpressionsMap.size();
											for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
												LineChartDTO chartDTO = new LineChartDTO();
												chartDTO.setDate(previousDate);
												chartDTO.setSiteName(monthSiteKey);
												chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
												thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
												perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
												monthPartnerImpressionsMap.put(monthSiteKey, 0L);
											}
											monthDateMap.put(previousDate, null);
											// month wise average
											weekOrMonthAverage(countOfPartner, previousDate, thisMonthImpressions, perfMonthDataMap);
										}
										monthStartDate = date;
										thisMonthValue = monthValue;
									}
									if(monthPartnerImpressionsMap.get(partnerName) != null) {
										long momthSiteClicks = monthPartnerImpressionsMap.get(partnerName);
										monthPartnerImpressionsMap.put(partnerName, momthSiteClicks+impressions);
									}
									else {
										monthPartnerImpressionsMap.put(partnerName, impressions);
									}
									
									previousDate = date;
								}
							}
							log.info("adding week, month data which was left to add.");
							long thisWeekImpressions = 0;
							int countOfPartner = weekPartnerImpressionsMap.size();
							for(String weekSiteKey : weekPartnerImpressionsMap.keySet()) {
								LineChartDTO chartDTO = new LineChartDTO();
								chartDTO.setDate(previousDate);
								chartDTO.setSiteName(weekSiteKey);
								chartDTO.setImpressions(weekPartnerImpressionsMap.get(weekSiteKey)+"");
								thisWeekImpressions = thisWeekImpressions + weekPartnerImpressionsMap.get(weekSiteKey); 
								perfWeekDataMap.put(previousDate+"_"+weekSiteKey, chartDTO);
							}
							weekDateMap.put(previousDate, null);
							// week wise average
							weekOrMonthAverage(countOfPartner, previousDate, thisWeekImpressions, perfWeekDataMap);

							long thisMonthImpressions = 0;
							countOfPartner = monthPartnerImpressionsMap.size();
							for(String monthSiteKey : monthPartnerImpressionsMap.keySet()) {
								LineChartDTO chartDTO = new LineChartDTO();
								chartDTO.setDate(previousDate);
								chartDTO.setSiteName(monthSiteKey);
								chartDTO.setImpressions(monthPartnerImpressionsMap.get(monthSiteKey)+"");
								thisMonthImpressions = thisMonthImpressions + monthPartnerImpressionsMap.get(monthSiteKey); 
								perfMonthDataMap.put(previousDate+"_"+monthSiteKey, chartDTO);
							}
							monthDateMap.put(previousDate, null);
							// month wise average
							weekOrMonthAverage(countOfPartner, previousDate, thisMonthImpressions, perfMonthDataMap);
							log.info("Added week, month data");
							
							jsonObject.put("eventName", eventKey);
							String dayLineChartJson=createJsonResponseForLineChart(dayDateMap, partnerMap, perfDayDataMap, "Impressions");
							jsonObject.put("day", dayLineChartJson);
							String weekLineChartJson=createJsonResponseForLineChart(weekDateMap, partnerMap, perfWeekDataMap, "Impressions");
							jsonObject.put("week", weekLineChartJson);
							log.info("weekLineChartJson : "+weekLineChartJson);
							String monthChartJson=createJsonResponseForLineChart(monthDateMap, partnerMap, perfMonthDataMap, "Impressions");
							jsonObject.put("month", monthChartJson);
							log.info("monthChartJson : "+monthChartJson);
							
							jsonArray.add(jsonObject);
							log.info("jsonArray.size : "+jsonArray.size());
						}
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in richMediaLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in richMediaLineChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		log.info("jsonArray.size() : "+jsonArray.size());
		JSONObject retunJson = new JSONObject();
		if(jsonArray != null && jsonArray.size() > 0) {
			retunJson.put("richMediaData", jsonArray);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	
	private void weekOrMonthAverage(int countOfPartner, String lastDate, long totalCount, Map<String, Object> performanceMap) {
		try {
			if(countOfPartner > 0) {
				String datekey=lastDate+"_Average";
				LineChartDTO lineChartDTO=new LineChartDTO();
				lineChartDTO.setDate(lastDate);
				lineChartDTO.setSiteName("Average");
				lineChartDTO.setImpressions((totalCount/countOfPartner)+"");
				performanceMap.put(datekey, lineChartDTO);
			}
		}
		catch (Exception e) {
			log.severe("Exception in weekOrMonthAverage of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private Map<String, List<LineChartDTO>> getEventWiseDataMap(List<TableRow> rowList, JSONObject partnerInfoJson) throws Exception {
		Map<String, List<LineChartDTO>> eventWiseDataMap = new TreeMap<>();
		try {
			if(rowList != null && rowList.size() > 0 && partnerInfoJson != null) {
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						String date = cellList.get(0).getV().toString();
						String partnerName=partnerInfoJson.get(cellList.get(1).getV().toString())+"";
						String customEvent = cellList.get(2).getV().toString();
						long count = StringUtil.getLongValue(cellList.get(3).getV().toString());
						LineChartDTO lineChartDTO = new LineChartDTO();
						lineChartDTO.setSiteName(partnerName);
						lineChartDTO.setDate(date);
						lineChartDTO.setImpressions(count+"");
						if(eventWiseDataMap.containsKey(customEvent)) {
							List<LineChartDTO> lineChartDTOList =  eventWiseDataMap.get(customEvent);
							lineChartDTOList.add(lineChartDTO);
						}
						else {
							List<LineChartDTO> lineChartDTOList =  new ArrayList<>();
							lineChartDTOList.add(lineChartDTO);
							eventWiseDataMap.put(customEvent, lineChartDTOList);
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.severe("Exception in getEventWiseDataMap of PerformanceMonitoringService : "+e.getMessage());
			throw e;
		}
		return eventWiseDataMap;
	}
	
	public JSONObject videoData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo) {
		JSONObject jsonObject = null;
		String memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+placementInfo;
		String keyPrefix = "PM_VideoData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonObject = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonObject != null && jsonObject.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonObject;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			jsonObject = new JSONObject();
		}
		try {
			QueryResponse queryResponse = null;
			DecimalFormat df2 = new DecimalFormat( "###,###,###,##0.00" );
			DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_RICH_MEDIA);
			
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.videoData(orderId, lineItemIds, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0) {
				List<TableRow> rowList = queryResponse.getRows();
				if(rowList != null && rowList.size() > 0) {
					int videoCreativeSetsCount = 0;
					int richMediaCount = 0;
					double averageInteractionRate = 0.0;
					double averageViewRate = 0.0;
					double averageCompletionRate = 0.0;
					double averageViewTime = 0.0;
					double videoLength = 0.0;
					
					long start = 0;
					long pause = 0;
					long resume = 0;
					long rewind = 0;
					long mute = 0;
					long unMute = 0;
					long fullScreen = 0;
					
					long firstquartile = 0;
					long midPoint = 0;
					long thirdQuartile = 0;
					long complete = 0;
					
					double rmInteractionTime = 0.0;
					long rmInteractionCount = 0;
					double rmInteractionRate = 0.0;
					double rmAvgInteractionTime = 0.0;
					long rmInteractionImpressions = 0;
					double rmAverageDisplayTime = 0.0;
					double rmDisplayTime = 0.0;
					
					for (TableRow row : rowList) {
						if(row != null && row.getF() != null && row.getF().size() > 0) {
							List<TableCell> cellList = row.getF();
							String creativeType = cellList.get(3).getV().toString();
							if(creativeType.equalsIgnoreCase(VIDEO_CREATIVE_SETS)) {								//	if Video creative sets
								++videoCreativeSetsCount;
								averageViewTime = averageViewTime + StringUtil.getDoubleValue(cellList.get(4).getV().toString());
								videoLength = videoLength + StringUtil.getDoubleValue(cellList.get(5).getV().toString());
								
								start = start + StringUtil.getLongValue(cellList.get(6).getV().toString());
								pause = pause + StringUtil.getLongValue(cellList.get(7).getV().toString());
								resume = resume + StringUtil.getLongValue(cellList.get(8).getV().toString());
								rewind = rewind + StringUtil.getLongValue(cellList.get(9).getV().toString());
								mute = mute + StringUtil.getLongValue(cellList.get(10).getV().toString());
								unMute = unMute + StringUtil.getLongValue(cellList.get(11).getV().toString());
								fullScreen = fullScreen + StringUtil.getLongValue(cellList.get(12).getV().toString());
								
								firstquartile = firstquartile + StringUtil.getLongValue(cellList.get(13).getV().toString());
								midPoint = midPoint + StringUtil.getLongValue(cellList.get(14).getV().toString());
								thirdQuartile = thirdQuartile + StringUtil.getLongValue(cellList.get(15).getV().toString());
								complete = complete + StringUtil.getLongValue(cellList.get(16).getV().toString());
							}else if(creativeType.equalsIgnoreCase(DOUBLECLICK_RICH_MEDIA)) {						//	if DoubleClick Rich Media
								++richMediaCount;
								rmInteractionTime = rmInteractionTime + StringUtil.getDoubleValue(cellList.get(17).getV().toString());
								rmInteractionCount = rmInteractionCount + StringUtil.getLongValue(cellList.get(18).getV().toString());
								rmInteractionImpressions = rmInteractionImpressions + StringUtil.getLongValue(cellList.get(19).getV().toString());
								rmDisplayTime = rmDisplayTime + StringUtil.getDoubleValue(cellList.get(20).getV().toString());
								//rmInteractionRate = rmInteractionRate + StringUtil.getDoubleValue(cellList.get(19).getV().toString());			// rmInteractionImpressions / AdserverImpressions
								//rmAverageDisplayTime = rmAverageDisplayTime + StringUtil.getDoubleValue(cellList.get(21).getV().toString());	// totalDisplayTime / AdserverImpressions
								//rmAvgInteractionTime = rmAvgInteractionTime + StringUtil.getDoubleValue(cellList.get(22).getV().toString());	// rmInteractionTime / rmInteractionImpressions
							}
						}
					}
					if(videoCreativeSetsCount == 0) {
						log.info("No Records found for videoCreativeSets : "+videoCreativeSetsCount);
					}
					else if(videoCreativeSetsCount > 0) {
						JSONObject videoCreativeSetJsonObject = new JSONObject();
						log.info("videoCreativeSetsCount : "+videoCreativeSetsCount);
						if(videoLength > 0) {
							averageViewRate = (double)(averageViewTime*100)/videoLength;
						}
						if(start > 0) {
							averageInteractionRate = (double)((pause + resume + rewind + mute + unMute + fullScreen)*100)/start;
							averageCompletionRate = (double)(complete*100)/start;
						}
						averageViewTime = (double)averageViewTime/videoCreativeSetsCount;
						
						if(averageViewRate > 0 || averageInteractionRate > 0 || averageCompletionRate > 0) {
							DataTable barChartTable = new DataTable();
						 	List<com.google.visualization.datasource.datatable.TableRow> rows;
						 	
						 	ColumnDescription rateCol = new ColumnDescription("rateType", ValueType.TEXT, "Rate Type");
						    barChartTable.addColumn(rateCol);
						    rateCol = new ColumnDescription("rate", ValueType.NUMBER, "");
						    barChartTable.addColumn(rateCol);
						    
						    rateCol = new ColumnDescription("", ValueType.TEXT, "");
						    rateCol.setCustomProperty("role", "style");
						    barChartTable.addColumn(rateCol);
						    
						    com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
						    
						    rows = Lists.newArrayList();
						    row.addCell(new com.google.visualization.datasource.datatable.TableCell("Average Interaction Rate"));
							row.addCell(new NumberValue(new Double(averageInteractionRate)));
							row.addCell("#43C087");
							rows.add(row);
							log.info("averageInteractionRate : "+averageInteractionRate);
							
							row = new com.google.visualization.datasource.datatable.TableRow();
							row.addCell(new com.google.visualization.datasource.datatable.TableCell("Average View Rate"));
							row.addCell(new NumberValue(new Double(averageViewRate)));
							row.addCell("#F26C28");
							rows.add(row);
							log.info("averageViewRate : "+averageViewRate);
							
							row = new com.google.visualization.datasource.datatable.TableRow();
							row.addCell(new com.google.visualization.datasource.datatable.TableCell("Average Completion Rate"));
							row.addCell(new NumberValue(new Double(averageCompletionRate)));
							row.addCell("#8F7FD4");
							rows.add(row);
							log.info("averageCompletionRate : "+averageCompletionRate);
							
							barChartTable.addRows(rows);
							
							java.lang.CharSequence jsonStr = JsonRenderer.renderDataTable(barChartTable, true, false);
							videoCreativeSetJsonObject.put("byRateChart", jsonStr.toString());
							log.info("byRateChart : "+jsonStr.toString());
						}else {
							log.info("No need to show chart --> videoCreativeSetsCount : "+videoCreativeSetsCount+", videoLength : "+videoLength+", averageViewTime : "+averageViewTime+", averageInteractionRate : "+averageInteractionRate+", averageViewRate : "+averageViewRate+", averageCompletionRate : "+averageCompletionRate);
						}
						JSONObject videoCardJson = new JSONObject();
						videoCardJson.put("averageInteractionRate", df2.format(averageInteractionRate)+"%");
						videoCardJson.put("averageViewRate", df2.format(averageViewRate)+"%");
						videoCardJson.put("averageCompletionRate", df2.format(averageCompletionRate)+"%");
						videoCardJson.put("averageViewTime", df2.format(averageViewTime));
						
						videoCardJson.put("start", lf.format(start));
						videoCardJson.put("pause", lf.format(pause));
						videoCardJson.put("resume", lf.format(resume));
						videoCardJson.put("rewind", lf.format(rewind));
						videoCardJson.put("mute", lf.format(mute));
						videoCardJson.put("unMute", lf.format(unMute));
						videoCardJson.put("fullScreen", lf.format(fullScreen));
						
						videoCardJson.put("firstQuartile", lf.format(firstquartile));
						videoCardJson.put("midPoint", lf.format(midPoint));
						videoCardJson.put("thirdQuartile", lf.format(thirdQuartile));
						videoCardJson.put("complete", lf.format(complete));
						videoCardJson.put("averageViewTimeUnit", "Seconds");
						videoCreativeSetJsonObject.put("videoCardJson", videoCardJson);
						jsonObject.put("videoCreativeSet", videoCreativeSetJsonObject);
					}
					
					if(richMediaCount == 0) {
						log.info("No Records found for DoubleClick Rich Media : "+richMediaCount);
					}
					else if(richMediaCount > 0) {
						log.info("richMediaCount : "+richMediaCount);
						if(rmInteractionImpressions > 0) {
							rmAvgInteractionTime = (double)rmInteractionTime/rmInteractionImpressions;
						}
						rmInteractionRate = (double)rmInteractionRate/richMediaCount;
						
						JSONObject richMediaCardJson = new JSONObject();
						richMediaCardJson.put("rmInteractionTime", df2.format(rmInteractionTime));
						richMediaCardJson.put("rmInteractionCount", lf.format(rmInteractionCount));
						richMediaCardJson.put("rmInteractionRate", "0%");
						richMediaCardJson.put("rmAvgInteractionTime", df2.format(rmAvgInteractionTime));
						richMediaCardJson.put("rmInteractionImpressions", rmInteractionImpressions);
						richMediaCardJson.put("rmAverageDisplayTime", rmAverageDisplayTime);
						richMediaCardJson.put("rmDisplayTime", rmDisplayTime);
						jsonObject.put("doubleClickRichMedia", richMediaCardJson);
					}
				}
			}
		}
		catch (JSONException e) {
			log.severe("JSONException in videoData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in videoData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		JSONObject retunJson = new JSONObject();
		if(jsonObject != null && jsonObject.size() > 0) {
			retunJson.put("videoData", jsonObject);
			MemcacheUtil.setObjectInCache(memcacheKey, retunJson.toString(), EXPIRATION_TIME);
		}
		return retunJson;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> creativeBarChartData(String orderId, String campaignId,
			String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo){
		List<PerformanceMonitoringDTO> monitoringDTOList = new ArrayList<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> creativeDataMap  = new LinkedHashMap<>();
		Map<String,String> jsonChartMap = null;
		LinkedHashMap<String,Object> partnerMap = new LinkedHashMap<>();
		LinkedHashMap<String,Object> creativeMap = new LinkedHashMap<>();
		Map<String,Object> campaignCreativeMap = new HashMap<>();
		Map<String,Long> nonTargetCreativeMap = new HashMap<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> averageMap = new LinkedHashMap<>();
		List<String> list = new ArrayList<String>();
		DataTable dataTable = new DataTable();
		IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
		String queryData = "";
		
		try {
			List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = getCampaignPlacementObj(campaignId, placementIds);
			String memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+smartCampaignPlacementObjList.toString();
			String keyPrefix = "PM_CreativeData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			jsonChartMap = (Map<String,String>) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(jsonChartMap != null && jsonChartMap.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonChartMap;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonChartMap = new HashMap<>();
			}
			String lineItemId = "";
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			long targetGoal = 0;
			long nonTargetGoal = 0;
			long totaltarget = 0;
			String targetCreative = "";
			String nonTargetCreative = "";
			String campaignType = (String) placementJson.get("campaignType");
			log.info("placementIds : "+placementIds);
			if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all") || placementIds.split(",").length > 1)) {
				log.info("campaignId : "+campaignId);
				if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
					for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
						if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getCreativeObj()!=null  
								&& smartCampaignPlacementObj.getCreativeObj().size()>0) {
							for(CreativeObj obj : smartCampaignPlacementObj.getCreativeObj()){
								if(obj!=null ){
									campaignCreativeMap.put(obj.getSize(), null);
								}
							}
						}
					}
				}
			}
			
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				lineItemId = placementJson.get("lineItemIds")+"";
				queryData = queryDTO.getQueryData();
				queryResponse = monitoringDAO.loadCreativeChartData(orderId, lineItemId, isNoise, threshold, queryDTO);
			}
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && placementJson != null && partnerInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						PerformanceMonitoringDTO monitoringDTO = new PerformanceMonitoringDTO();
						List<TableCell> cellList = row.getF();
						String partnerName = partnerInfoJson.get(cellList.get(3).getV().toString())+"";
						if(partnerName!=null && partnerName.length()>0){
							monitoringDTO.setPublisherName(partnerName);
						}
						monitoringDTO.setImpressionDelivered(cellList.get(0).getV().toString());
						monitoringDTO.setClicks(cellList.get(1).getV().toString());
						monitoringDTO.setCTR(cellList.get(2).getV().toString());
						monitoringDTO.setCreativeSize(cellList.get(4).getV().toString());
						monitoringDTOList.add(monitoringDTO);
					}
				}
			}
			if(monitoringDTOList!=null && monitoringDTOList.size()>0){
				for (PerformanceMonitoringDTO performanceMonitoringDTO : monitoringDTOList) {
					if(performanceMonitoringDTO!=null && performanceMonitoringDTO.getPublisherName()!=null && performanceMonitoringDTO.getCreativeSize()!=null){
						PerformanceMonitoringDTO dto = new PerformanceMonitoringDTO();
						PerformanceMonitoringDTO aveDto = new PerformanceMonitoringDTO();
						long impressions = 0L;
						long clicks = 0L;
						double ctr = 0.0;
						int averageCount = 0;
						double averageImpression=0.0;
						double averageClick=0.0;
						double averageCTR=0.0;
						
						if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
							totaltarget = totaltarget + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
						}
						if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
							totaltarget = totaltarget +  StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
						}
						
						if(performanceMonitoringDTO.getCreativeSize()!=null && campaignCreativeMap.containsKey(performanceMonitoringDTO.getCreativeSize().replaceAll(" ", ""))){
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								targetGoal = targetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								targetGoal = targetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
							
						}else if(performanceMonitoringDTO.getCreativeSize()!=null && nonTargetCreativeMap.containsKey(performanceMonitoringDTO.getCreativeSize().replaceAll(" ", ""))){
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
						}else{
							if(performanceMonitoringDTO.getCreativeSize()!=null){
								nonTargetCreativeMap.put(performanceMonitoringDTO.getCreativeSize().replaceAll(" ", ""), StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered()));
							}
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
						}
						
						String key ="";
						if(performanceMonitoringDTO.getCreativeSize()!=null){
							 key = performanceMonitoringDTO.getPublisherName()+"_"+performanceMonitoringDTO.getCreativeSize().replaceAll(" ", "");

						}
						
						if(creativeDataMap!=null && !creativeDataMap.containsKey(key)){
							creativeDataMap.put(key, performanceMonitoringDTO);
							partnerMap.put(performanceMonitoringDTO.getPublisherName(), null);
							if(performanceMonitoringDTO!=null){
								aveDto.setImpAverage(performanceMonitoringDTO.getImpressionDelivered());
								aveDto.setClickAverage(performanceMonitoringDTO.getClicks());
								aveDto.setCtrAverage(performanceMonitoringDTO.getCTR());
								if(performanceMonitoringDTO.getCreativeSize()!=null){
									averageMap.put(performanceMonitoringDTO.getCreativeSize().replaceAll(" ", ""), aveDto);
									creativeMap.put(performanceMonitoringDTO.getCreativeSize().replaceAll(" ", ""), null);
								}
							
							}
							
						}else{
							dto = creativeDataMap.get(key);
							String[] averageKeyArr = key.split("_");
							String averageKey = averageKeyArr[1];
							aveDto = averageMap.get(averageKey);
						    averageCount = aveDto.getAverageCount()+1;
							if(dto!=null){
								if(dto.getImpressionDelivered()!=null && dto.getImpressionDelivered().length()>0 && performanceMonitoringDTO.getImpressionDelivered()!=null 
																					&& performanceMonitoringDTO.getImpressionDelivered().length()>0 ){
									 impressions = StringUtil.getLongValue(dto.getImpressionDelivered())+ StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
									 averageImpression = (double) impressions/averageCount;
									 
								}
								if(dto.getClicks()!=null && dto.getClicks().length()>0 && performanceMonitoringDTO.getClicks()!=null 
										&& performanceMonitoringDTO.getClicks().length()>0 ){
									clicks = StringUtil.getLongValue(dto.getClicks())+ StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
									averageClick = (double) clicks/averageCount;
								}
								if(dto.getCTR()!=null && dto.getCTR().length()>0 && performanceMonitoringDTO.getCTR()!=null 
										&& performanceMonitoringDTO.getCTR().length()>0 ){
									log.info("clicks  :"+clicks+"impressions :"+impressions);
									//ctr = StringUtil.getDoubleValue(dto.getCTR())+ StringUtil.getDoubleValue(performanceMonitoringDTO.getCTR());
									ctr = (double)(clicks*100)/impressions;
									log.info("creative CTR :"+ctr);
									averageCTR =  ctr/averageCount;
								}
								dto.setImpressionDelivered(impressions+"");
								dto.setClicks(clicks+"");
								dto.setCTR(ctr+"");
								creativeDataMap.put(key, dto);
								aveDto.setImpAverage(averageImpression+"");
								aveDto.setClickAverage(averageClick+"");
								aveDto.setCtrAverage(averageCTR+"");
								averageMap.put(averageKey, aveDto);
								
							}
						}
						
					}
				}
			}
			String ctrLineChartJson=createJsonResponseForBarChart("creative","Creative",creativeDataMap,partnerMap,creativeMap,averageMap , "CTR");
			jsonChartMap.put("CTR", ctrLineChartJson);
			String clicksLineChartJson=createJsonResponseForBarChart("creative","Creative",creativeDataMap,partnerMap,creativeMap,averageMap, "Clicks");
			jsonChartMap.put("Clicks", clicksLineChartJson);
			String impressionsLineChartJson=createJsonResponseForBarChart("creative","Creative",creativeDataMap,partnerMap,creativeMap,averageMap, "Impressions");
			jsonChartMap.put("Impressions", impressionsLineChartJson);
			 	
		 	queryResponse = null;
			dataTable = new DataTable();
			list = new ArrayList<String>();
			queryDTO.setQueryData(queryData);
		 	if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
		 		queryResponse = monitoringDAO.loadCreativeImpressionClicksChartDate(orderId, lineItemId, isNoise,threshold,queryDTO);
		 		ArrayList<String> donutImpressionList = new ArrayList<>();
		 		donutImpressionList.add("creative_size");
		 		donutImpressionList.add("Impression_Delivered");
		 		
		 		ArrayList<String> donutClickList = new ArrayList<>();
		 		donutClickList.add("creative_size");
		 		donutClickList.add("Clicks");
		 		
		 		DataTable impressionDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutImpressionList);
		 		DataTable clickDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutClickList);
		 		
				if(queryResponse !=null && queryResponse.getRows()!=null){
					impressionDataTable = GoogleVisulizationUtil.buildCustomRows(impressionDataTable, queryResponse,2);
					clickDataTable = GoogleVisulizationUtil.buildCustomRows(clickDataTable, queryResponse,1);
				}else{
					log.warning("No data found for this campaign -"+orderId);
				}
				
				String creativeImpDonutJson =JsonRenderer.renderDataTable(impressionDataTable, true, false).toString();
				jsonChartMap.put("DonutImpression", creativeImpDonutJson);
				String creativeClickDonutJson =JsonRenderer.renderDataTable(clickDataTable, true, false).toString();
				jsonChartMap.put("DonutClick", creativeClickDonutJson);
				
				List<TableRow> rowList = queryResponse.getRows();
				JSONArray jsonImpressionArray = new JSONArray();
				JSONArray jsonClickArray = new JSONArray();
				int i=0;
				
				for (TableRow row : rowList) {
					
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						JSONObject  jsonImpressionObject = new JSONObject();
						JSONObject  jsonClickObject = new JSONObject();
						
						List<TableCell> cellList = row.getF();
							jsonImpressionObject.put("creative", cellList.get(0).getV().toString());
							jsonImpressionObject.put("impressions", cellList.get(1).getV().toString());
							jsonImpressionObject.put("color",lagendColor.values()[i%10].name());
							jsonImpressionArray.add(jsonImpressionObject);
						
							jsonClickObject.put("creative", cellList.get(0).getV().toString());
							jsonClickObject.put("clicks", cellList.get(2).getV().toString());
							jsonClickObject.put("color",lagendColor.values()[i%10].name());
							jsonClickArray.add(jsonClickObject);
						
					}
					i++;
				}
				jsonChartMap.put("ImpByCreative", jsonImpressionArray.toString());
				jsonChartMap.put("ClicksByCreative", jsonClickArray.toString());
		 	}
			if(totaltarget!=0){
				log.info("total Target:"+totaltarget+"target goal"+targetGoal);
				double targetPercentage = (double)(targetGoal*100)/totaltarget;
				log.info("targetPercentage :"+targetPercentage);
				jsonChartMap.put("TargetPercentage", targetPercentage+"");
			}
			if(totaltarget!=0){
				double nonTargetPercentage = (double)(nonTargetGoal*100)/totaltarget;
				jsonChartMap.put("NonTargetPercentage", nonTargetPercentage+"");
			}
		
			for(Entry<String, Object> targetMap : campaignCreativeMap.entrySet()) {
				 String key = targetMap.getKey();
				 targetCreative = targetCreative+key+",";
			 }
			targetCreative = StringUtil.deleteFromLastOccurence(targetCreative, ",");
			jsonChartMap.put("TargetCreative",targetCreative);
			
			for(Entry<String, Long> nonTargetMap : nonTargetCreativeMap.entrySet()) {
				 String key = nonTargetMap.getKey();
				 nonTargetCreative = nonTargetCreative+key+",";
			 }
			nonTargetCreative = StringUtil.deleteFromLastOccurence(nonTargetCreative, ",");
			jsonChartMap.put("NonTargetCreative",nonTargetCreative);
			jsonChartMap.put("TargetGoal",targetGoal+"");
			jsonChartMap.put("NonTargetGoal",nonTargetGoal+"");
			jsonChartMap.put("partnerCount",partnerMap.size()+"");
			MemcacheUtil.setObjectInCache(memcacheKey, jsonChartMap, EXPIRATION_TIME);
			
		}catch(Exception e){
			log.severe("Exception in creativeBarChartData of PerformanceMonitoringService : "+e.getMessage());
			e.printStackTrace();
		}
		return jsonChartMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> deviceBarChartData(String orderId,
			String campaignId, String placementIds, boolean isNoise,
			double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo){
		
		List<PerformanceMonitoringDTO> monitoringDTOList = new ArrayList<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> deviceDataMap  = new LinkedHashMap<>();
		Map<String,String> jsonChartMap = null;
		LinkedHashMap<String,Object> partnerMap = new LinkedHashMap<>();
		LinkedHashMap<String,Object> deviceMap = new LinkedHashMap<>();
		Map<String,Object> campaignDeviceMap = new HashMap<>();
		Map<String,Long> nonTargetDeviceMap = new HashMap<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> averageMap = new LinkedHashMap<>();
		List<String> list = new ArrayList<String>();
		DataTable dataTable = new DataTable();
		IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
		
		try{
			List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = getCampaignPlacementObj(campaignId, placementIds);
			String memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+smartCampaignPlacementObjList.toString();
			String keyPrefix = "PM_DeviceData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			jsonChartMap = (Map<String,String>) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(jsonChartMap != null && jsonChartMap.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonChartMap;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonChartMap = new HashMap<>();
			}
			String lineItemId = "";
			String queryData = "";
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			long targetGoal = 0;
			long nonTargetGoal = 0;
			long totaltarget = 0;
			String targetDevice = "";
			String nonTargetDevice = "";
			String campaignType = (String) placementJson.get("campaignType");
			log.info("placementIds : "+placementIds);
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			
			// Get List of all Devices used for a Campaign
			if(placementIds != null && (placementIds.equalsIgnoreCase("All") || placementIds.toLowerCase().contains("all") || placementIds.split(",").length > 1)) {
				log.info("campaignId : "+campaignId);
				if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
					for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
						if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getDeviceObj()!=null  && smartCampaignPlacementObj.getDeviceObj().size()>0) {
							for(DeviceObj obj : smartCampaignPlacementObj.getDeviceObj()){
								if(obj != null){
									campaignDeviceMap.put(obj.getText(), null);
									
								}
							}
						}
					}
				}
			}
			
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_DFP_TARGET);
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				lineItemId = placementJson.get("lineItemIds")+"";
				queryData = queryDTO.getQueryData();
				queryResponse = monitoringDAO.loadDeviceChartData(orderId, lineItemId, isNoise, threshold, queryDTO);
			}
			
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && placementJson != null && partnerInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						PerformanceMonitoringDTO monitoringDTO = new PerformanceMonitoringDTO();
						List<TableCell> cellList = row.getF();
						String partnerName = partnerInfoJson.get(cellList.get(3).getV().toString())+"";
						if(partnerName!=null && partnerName.length()>0){
							monitoringDTO.setPublisherName(partnerName);
						}
						monitoringDTO.setImpressionDelivered(cellList.get(0).getV().toString());
						monitoringDTO.setClicks(cellList.get(1).getV().toString());
						monitoringDTO.setCTR(cellList.get(2).getV().toString());
						monitoringDTO.setDevice(cellList.get(4).getV().toString());
						monitoringDTOList.add(monitoringDTO);
					}
				}
			}
			
			// Manipulate Data 
			if(monitoringDTOList!=null && monitoringDTOList.size()>0){
				for (PerformanceMonitoringDTO performanceMonitoringDTO : monitoringDTOList) {
					if(performanceMonitoringDTO!=null && performanceMonitoringDTO.getPublisherName()!=null && performanceMonitoringDTO.getDevice()!=null){
						PerformanceMonitoringDTO dto = new PerformanceMonitoringDTO();
						PerformanceMonitoringDTO aveDto = new PerformanceMonitoringDTO();
						long impressions = 0L;
						long clicks = 0L;
						double ctr = 0.0;
						int averageCount = 0;
						double averageImpression=0.0;
						double averageClick=0.0;
						double averageCTR=0.0;
						
						if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
							totaltarget = totaltarget + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
						}
						if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
							totaltarget = totaltarget +  StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
						}
						
						String monitoringDeviceTxt = performanceMonitoringDTO.getDevice();
						if(performanceMonitoringDTO.getDevice()!=null && isKeyAvailable(campaignDeviceMap.keySet(),monitoringDeviceTxt)){
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								targetGoal = targetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								targetGoal = targetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
							
						}else if(performanceMonitoringDTO.getDevice()!=null &&  isKeyAvailable(nonTargetDeviceMap.keySet(),monitoringDeviceTxt)){
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
						}else{
							if(performanceMonitoringDTO.getDevice()!=null){
								nonTargetDeviceMap.put(performanceMonitoringDTO.getDevice(), StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered()));
							}
							if(campaignType!=null && campaignType.equalsIgnoreCase("CPM")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
							}else if(campaignType!=null && campaignType.equalsIgnoreCase("CPC")){
								nonTargetGoal = nonTargetGoal + StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
							}
						}
						
						String key = performanceMonitoringDTO.getPublisherName()+"_"+performanceMonitoringDTO.getDevice();
						if(deviceDataMap!=null && !deviceDataMap.containsKey(key)){
							deviceDataMap.put(key, performanceMonitoringDTO);
							partnerMap.put(performanceMonitoringDTO.getPublisherName(), null);
							if(performanceMonitoringDTO!=null){
								aveDto.setImpAverage(performanceMonitoringDTO.getImpressionDelivered());
								aveDto.setClickAverage(performanceMonitoringDTO.getClicks());
								aveDto.setCtrAverage(performanceMonitoringDTO.getCTR());
								averageMap.put(performanceMonitoringDTO.getDevice(), aveDto);
								deviceMap.put(performanceMonitoringDTO.getDevice(), null);
								
								if(campaignDeviceMap.containsKey(performanceMonitoringDTO.getDevice())){
								
								}
							}
						}else{
							dto = deviceDataMap.get(key);
							String[] averageKeyArr = key.split("_");
							String averageKey = averageKeyArr[1];
							aveDto = averageMap.get(averageKey);
						    averageCount = aveDto.getAverageCount()+1;
						    
						    if(dto!=null){
						    	if(dto.getImpressionDelivered()!=null && dto.getImpressionDelivered().length()>0 && performanceMonitoringDTO.getImpressionDelivered()!=null && performanceMonitoringDTO.getImpressionDelivered().length()>0 ){
						    		impressions = StringUtil.getLongValue(dto.getImpressionDelivered()) + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
									averageImpression = (double) impressions/averageCount;
						    	}
						    	if(dto.getClicks()!=null && dto.getClicks().length()>0 && performanceMonitoringDTO.getClicks()!=null 
										&& performanceMonitoringDTO.getClicks().length()>0 ){
									clicks = StringUtil.getLongValue(dto.getClicks())+ StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
									averageClick = (double) clicks/averageCount;
								}
								if(dto.getCTR()!=null && dto.getCTR().length()>0 && performanceMonitoringDTO.getCTR()!=null 
										&& performanceMonitoringDTO.getCTR().length()>0 ){
									ctr = StringUtil.getDoubleValue(dto.getCTR())+ StringUtil.getDoubleValue(performanceMonitoringDTO.getCTR());
									averageCTR =  ctr/averageCount;
								}
								
								dto.setImpressionDelivered(impressions+"");
								dto.setClicks(clicks+"");
								dto.setCTR(ctr+"");
								deviceDataMap.put(key, dto);
								aveDto.setImpAverage(averageImpression+"");
								aveDto.setClickAverage(averageClick+"");
								aveDto.setCtrAverage(averageCTR+"");
								averageMap.put(averageKey, aveDto);
								
						    }
						}
						
					}
				}
			}
			
			
			// Conversion of data to JSON
			String ctrLineChartJson=createJsonResponseForBarChart("device","Device",deviceDataMap,partnerMap,deviceMap,averageMap , "CTR");
			jsonChartMap.put("CTR", ctrLineChartJson);
			String clicksLineChartJson=createJsonResponseForBarChart("device","Device",deviceDataMap,partnerMap,deviceMap,averageMap, "Clicks");
			jsonChartMap.put("Clicks", clicksLineChartJson);
			String impressionsLineChartJson=createJsonResponseForBarChart("device","Device",deviceDataMap,partnerMap,deviceMap,averageMap, "Impressions");
			jsonChartMap.put("Impressions", impressionsLineChartJson);
			 	
			 	 queryResponse = null;
				 dataTable = new DataTable();
				 list = new ArrayList<String>();
				 queryDTO.setQueryData(queryData);
				 	if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				 		
				 		queryResponse = monitoringDAO.loadDeviceImpressionClicksChartDate(orderId, lineItemId, isNoise, threshold, queryDTO);
				 		
				 		ArrayList<String> donutImpressionList = new ArrayList<>();
				 		donutImpressionList.add("Target_Value");
				 		donutImpressionList.add("Impression_Delivered");
				 		
				 		
				 		ArrayList<String> donutClickList = new ArrayList<>();
				 		donutClickList.add("Target_Value");
				 		donutClickList.add("Clicks");
				 		
				 		DataTable impressionDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutImpressionList);
				 		DataTable clickDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutClickList);
				 		
				 		
						if(queryResponse !=null && queryResponse.getRows()!=null){
							impressionDataTable = GoogleVisulizationUtil.buildCustomRows(impressionDataTable, queryResponse,2);
							clickDataTable = GoogleVisulizationUtil.buildCustomRows(clickDataTable, queryResponse,1);
						}else{
							log.warning("No data found for this campaign -"+orderId);
						}
						
						String deviceImpDonutJson =JsonRenderer.renderDataTable(impressionDataTable, true, false).toString();
						jsonChartMap.put("DonutImpression", deviceImpDonutJson);
						String deviceClickDonutJson =JsonRenderer.renderDataTable(clickDataTable, true, false).toString();
						jsonChartMap.put("DonutClick", deviceClickDonutJson);
						
						
						List<TableRow> rowList = queryResponse.getRows();
						JSONArray jsonImpressionArray = new JSONArray();
						JSONArray jsonClickArray = new JSONArray();
						int i=0;
						
						for (TableRow row : rowList) {
							
							if(row != null && row.getF() != null && row.getF().size() > 0) {
								JSONObject  jsonImpressionObject = new JSONObject();
								JSONObject  jsonClickObject = new JSONObject();
								
								List<TableCell> cellList = row.getF();
									jsonImpressionObject.put("creative", cellList.get(0).getV().toString());
									jsonImpressionObject.put("impressions", cellList.get(1).getV().toString());
									jsonImpressionObject.put("color",lagendColor.values()[i%10].name());
									jsonImpressionArray.add(jsonImpressionObject);
									
									jsonClickObject.put("creative", cellList.get(0).getV().toString());
									jsonClickObject.put("clicks", cellList.get(2).getV().toString());
									jsonClickObject.put("color",lagendColor.values()[i%10].name());
									jsonClickArray.add(jsonClickObject);
								
								
							}
							i++;
						}
						jsonChartMap.put("ImpressionsByDevice", jsonImpressionArray.toString());
						jsonChartMap.put("ClicksByDevice", jsonClickArray.toString());
				 	}
					for(Entry<String, Object> targetMap : campaignDeviceMap.entrySet()) {
						 String key = targetMap.getKey();
						 targetDevice = targetDevice+key+",";
					 }
					targetDevice = StringUtil.deleteFromLastOccurence(targetDevice, ",");
					jsonChartMap.put("TargetDevice",targetDevice);
					
					for(Entry<String, Long> nonTargetMap : nonTargetDeviceMap.entrySet()) {
						 String key = nonTargetMap.getKey();
						 nonTargetDevice = nonTargetDevice+key+",";
					 }
					nonTargetDevice = StringUtil.deleteFromLastOccurence(nonTargetDevice, ",");
					
			if(totaltarget!=0){
				double targetPercentage = (double)(targetGoal*100)/totaltarget;
				jsonChartMap.put("TargetPercentage", targetPercentage+"");
			}
			if(totaltarget!=0){
				double nonTargetPercentage = (double)(nonTargetGoal*100)/totaltarget;
				if(nonTargetPercentage==100){
					double targetPercentage = nonTargetPercentage;
					nonTargetPercentage = 0;
					jsonChartMap.put("TargetPercentage", targetPercentage+"");
					jsonChartMap.put("NonTargetPercentage", nonTargetPercentage+"");
					targetGoal = nonTargetGoal;
					jsonChartMap.put("TargetGoal",targetGoal+"");
					nonTargetGoal = 0;
					jsonChartMap.put("NonTargetGoal",nonTargetGoal+"");
					targetDevice = nonTargetDevice;
					jsonChartMap.put("TargetDevice",targetDevice);
					nonTargetDevice = "--";
					jsonChartMap.put("NonTargetGoal",nonTargetGoal+"");
				}else{
					jsonChartMap.put("NonTargetPercentage", nonTargetPercentage+"");
				}
				
			}
		
		
			jsonChartMap.put("NonTargetDevice",nonTargetDevice);
			jsonChartMap.put("TargetGoal",targetGoal+"");
			jsonChartMap.put("NonTargetGoal",nonTargetGoal+"");
			jsonChartMap.put("partnerCount",partnerMap.size()+"");
			MemcacheUtil.setObjectInCache(memcacheKey, jsonChartMap, EXPIRATION_TIME);
			
		}catch(Exception e){
			log.severe("Exception in deviceBarChartData in PerformanceMonitoringService"+e.toString());
			e.printStackTrace();
		}
		
		
		return jsonChartMap;
	}
	
	/*@SuppressWarnings("unused")
	private String createJsonResponseForBarChart(String kpiKey, String kpiValue, LinkedHashMap<String,PerformanceMonitoringDTO> creativeDataMap,LinkedHashMap<String,Object> partnerMap,LinkedHashMap<String,Object> creativeMap,LinkedHashMap<String,PerformanceMonitoringDTO> averageMap,String chartType) throws TypeMismatchException{
		try{
		DataTable barChartTable = new DataTable();	 	
	 	List<com.google.visualization.datasource.datatable.TableRow> rows;

	    ColumnDescription col0 = new ColumnDescription(kpiKey, ValueType.TEXT, kpiValue);
	    barChartTable.addColumn(col0);
	    
	    ColumnDescription averagecol = new ColumnDescription("average", ValueType.NUMBER, "Average");
	    barChartTable.addColumn(averagecol);
	    
	    for (Entry<String, Object> partners : partnerMap.entrySet()) {
			  String partner = partners.getKey();
			  ColumnDescription partnercol = new ColumnDescription(partner, ValueType.NUMBER, partner);
			  barChartTable.addColumn(partnercol);
		}
	    
	   
	    rows = Lists.newArrayList();	
	   // int year, month, day = 0;	
	    String creative = "";
	    for (Entry<String, Object> creatives : creativeMap.entrySet()) {
			  String key = creatives.getKey();
			 // String [] creativeArray=key.split("_");
			  com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
			  creative = key;
			  row.addCell(new com.google.visualization.datasource.datatable.TableCell(creative));
			  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(date));
			  PerformanceMonitoringDTO aveDTO = averageMap.get(creative);
			  if(aveDTO!=null){
				  if(chartType.equalsIgnoreCase("CTR")){
					  Double avCtr=StringUtil.getDoubleValue(aveDTO.getCtrAverage(), 4);
					  row.addCell(new NumberValue(new Double(avCtr)));  
				  }else if(chartType.equalsIgnoreCase("Clicks")){
					  Double avClick=StringUtil.getDoubleValue(aveDTO.getClickAverage(), 4);
					  row.addCell(new NumberValue(new Double(avClick))); 
				  }else if(chartType.equalsIgnoreCase("Impressions")){
					  Double avImp=StringUtil.getDoubleValue(aveDTO.getImpAverage(), 4);
					  row.addCell(new NumberValue(new Double(avImp))); 
			  }else{
				  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
			  	}
		  }
			  
			  for (Entry<String, PerformanceMonitoringDTO> creativeData : creativeDataMap.entrySet()) {
				  String dataKey = creativeData.getKey();
				  String [] dataKeyArr = dataKey.split("_");
				  if(creative!=null && dataKeyArr[1]!=null && creative.trim().equals(dataKeyArr[1].trim())){
					  PerformanceMonitoringDTO perDTO = (PerformanceMonitoringDTO) creativeDataMap.get(dataKey);
					  //LineChartDTO perData = (LineChartDTO) perfDataMap.get(key);					 
					  if ( perDTO  != null){
						  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(perData.getCtr()).);						  
						  if(chartType.equalsIgnoreCase("CTR")){
							  Double ctr=StringUtil.getDoubleValue(perDTO.getCTR(), 4);
							  row.addCell(new NumberValue(new Double(ctr)));  
						  }else if(chartType.equalsIgnoreCase("Clicks")){
							  row.addCell(new NumberValue(new Long(perDTO.getClicks()))); 
						  }else if(chartType.equalsIgnoreCase("Impressions")){
							  row.addCell(new NumberValue(new Long(perDTO.getImpressionDelivered()))); 
					  }else{
						  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
					  }
					  
				  }
			  }else{
				  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
			  }
		  }
			
				 
			  rows.add(row);				  
	  }
	    barChartTable.addRows(rows);
	  
	  java.lang.CharSequence jsonStr =	 JsonRenderer.renderDataTable(barChartTable, true, false);
	  log.info("bar chart JSON String : "+jsonStr);
	  return jsonStr.toString();
	}catch(Exception e){
		log.severe("Exception in createJsonResponseForBarChart in PerformanceMonitoringService"+e.getMessage());
		e.printStackTrace();
		}
	  //System.out.println("LineChart Table JSON Data::" + jsonStr.toString());
		return chartType;
	 
	}*/
	
	@SuppressWarnings("unused")
	private String createJsonResponseForBarChart(String kpiKey, String kpiValue, LinkedHashMap<String,PerformanceMonitoringDTO> creativeDataMap,LinkedHashMap<String,Object> partnerMap,LinkedHashMap<String,Object> creativeMap,LinkedHashMap<String,PerformanceMonitoringDTO> averageMap,String chartType) throws TypeMismatchException{
		try{
			DataTable barChartTable = new DataTable();	 	
		 	List<com.google.visualization.datasource.datatable.TableRow> rows;

		    ColumnDescription col0 = new ColumnDescription(kpiKey, ValueType.TEXT, kpiValue);
		    barChartTable.addColumn(col0);
		    
		    ColumnDescription averagecol = new ColumnDescription("average", ValueType.NUMBER, "Average");
		    barChartTable.addColumn(averagecol);
		    for (Entry<String, Object> partners : partnerMap.entrySet()) {
				  String partner = partners.getKey();
				  ColumnDescription partnercol = new ColumnDescription(partner, ValueType.NUMBER, partner);
				  barChartTable.addColumn(partnercol);
			}
		    
		    rows = Lists.newArrayList();
		    for (Entry<String, Object> creatives : creativeMap.entrySet()) {
				  String creativeKey = creatives.getKey();
				  com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
				  row.addCell(new com.google.visualization.datasource.datatable.TableCell(creativeKey));
				  PerformanceMonitoringDTO aveDTO = averageMap.get(creativeKey);
				  if(aveDTO!=null){
					  if(chartType.equalsIgnoreCase("CTR")){
						  Double avCtr=StringUtil.getDoubleValue(aveDTO.getCtrAverage(), 4);
						  row.addCell(new NumberValue(new Double(avCtr)));  
					  }else if(chartType.equalsIgnoreCase("Clicks")){
						  Double avClick=StringUtil.getDoubleValue(aveDTO.getClickAverage(), 4);
						  row.addCell(new NumberValue(new Double(avClick))); 
					  }else if(chartType.equalsIgnoreCase("Impressions")){
						  Double avImp=StringUtil.getDoubleValue(aveDTO.getImpAverage(), 4);
						  row.addCell(new NumberValue(new Double(avImp))); 
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  	}
				  }
				  for (Entry<String, Object> partners : partnerMap.entrySet()) {
					  String partner = partners.getKey();
					  String patnerCreativeKey = partner+"_"+creativeKey;
					  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(date));
		
					  if(patnerCreativeKey!=null && creativeDataMap.containsKey(patnerCreativeKey)){
						  PerformanceMonitoringDTO perDTO = creativeDataMap.get(patnerCreativeKey);
						  if ( perDTO  != null){
							  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(perData.getCtr()).);						  
							  if(chartType.equalsIgnoreCase("CTR")){
								  Double ctr=StringUtil.getDoubleValue(perDTO.getCTR(), 4);
								  row.addCell(new NumberValue(new Double(ctr)));  
							  }else if(chartType.equalsIgnoreCase("Clicks")){
								  row.addCell(new NumberValue(new Long(perDTO.getClicks()))); 
							  }else if(chartType.equalsIgnoreCase("Impressions")){
								  row.addCell(new NumberValue(new Long(perDTO.getImpressionDelivered()))); 
						  }else{
							  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
						  }
						  
					  }
					  }/*else{
						  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
					  }*/
				  }
				  rows.add(row);		  
			}
		    barChartTable.addRows(rows);
		    
/*		   
		    rows = Lists.newArrayList();	
		   // int year, month, day = 0;	
		    String creative = "";
		    for (Entry<String, Object> creatives : creativeMap.entrySet()) {
				  String key = creatives.getKey();
				 // String [] creativeArray=key.split("_");
				  com.google.visualization.datasource.datatable.TableRow row = new com.google.visualization.datasource.datatable.TableRow();
				  creative = key;
				  row.addCell(new com.google.visualization.datasource.datatable.TableCell(creative));
				  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(date));
				  PerformanceMonitoringDTO aveDTO = averageMap.get(creative);
				  if(aveDTO!=null){
					  if(chartType.equalsIgnoreCase("CTR")){
						  Double avCtr=StringUtil.getDoubleValue(aveDTO.getCtrAverage(), 4);
						  row.addCell(new NumberValue(new Double(avCtr)));  
					  }else if(chartType.equalsIgnoreCase("Clicks")){
						  Double avClick=StringUtil.getDoubleValue(aveDTO.getClickAverage(), 4);
						  row.addCell(new NumberValue(new Double(avClick))); 
					  }else if(chartType.equalsIgnoreCase("Impressions")){
						  Double avImp=StringUtil.getDoubleValue(aveDTO.getImpAverage(), 4);
						  row.addCell(new NumberValue(new Double(avImp))); 
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  	}
			  }
				  
				  for (Entry<String, PerformanceMonitoringDTO> creativeData : creativeDataMap.entrySet()) {
					  String dataKey = creativeData.getKey();
					  String [] dataKeyArr = dataKey.split("_");
					  if(creative!=null && dataKeyArr[1]!=null && creative.trim().equals(dataKeyArr[1].trim())){
						  PerformanceMonitoringDTO perDTO = (PerformanceMonitoringDTO) creativeDataMap.get(dataKey);
						  //LineChartDTO perData = (LineChartDTO) perfDataMap.get(key);					 
						  if ( perDTO  != null){
							  //row.addCell(new com.google.visualization.datasource.datatable.TableCell(perData.getCtr()).);						  
							  if(chartType.equalsIgnoreCase("CTR")){
								  Double ctr=StringUtil.getDoubleValue(perDTO.getCTR(), 4);
								  row.addCell(new NumberValue(new Double(ctr)));  
							  }else if(chartType.equalsIgnoreCase("Clicks")){
								  row.addCell(new NumberValue(new Long(perDTO.getClicks()))); 
							  }else if(chartType.equalsIgnoreCase("Impressions")){
								  row.addCell(new NumberValue(new Long(perDTO.getImpressionDelivered()))); 
						  }else{
							  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
						  }
						  
					  }
				  }else{
					  row.addCell(new com.google.visualization.datasource.datatable.TableCell(0));
				  }
			  }
				
					 
				  rows.add(row);				  
		  }*/
		    
		  
		  java.lang.CharSequence jsonStr =	 JsonRenderer.renderDataTable(barChartTable, true, false);
		  log.info("bar chart JSON String : "+jsonStr);
		  return jsonStr.toString();
		}catch(Exception e){
		log.severe("Exception in createJsonResponseForBarChart in PerformanceMonitoringService"+e.getMessage());
		e.printStackTrace();
		}
	  //System.out.println("LineChart Table JSON Data::" + jsonStr.toString());
		return chartType;
	 
	}


	
	
	

	/* (non-Javadoc)
	 * @see com.lin.web.service.IPerformanceMonitoringService#osChartData(java.lang.String, java.lang.String, boolean, double, java.lang.String, java.lang.String, java.lang.String)
	 * @author : Anup Dutta
	 * @description : 	This method pull all data from DFP_TARGET (Finalized + Non-Finalized) table based on Target Category = Platform and Target Value = Operating System
	 * 					After pulling data, Data are divided into various groups and sent back to caller.
	 */
	@Override
	public Map<String, String> osChartData(String orderId, String campaignId,
			String placementIds, boolean isNoise, double threshold, String publisherIdInBQ, String placementInfo, String partnerInfo) {
		log.info("In method osChartData");
		List<PerformanceMonitoringDTO> monitoringDTOList = new ArrayList<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> osDataMap  = new LinkedHashMap<>();
		Map<String,String> jsonChartMap = null;
		LinkedHashMap<String,Object> partnerMap = new LinkedHashMap<>();
		LinkedHashMap<String,Object> osMap = new LinkedHashMap<>();
		Map<String,Object> campaignOSMap = new HashMap<>();
		Map<String,Long> nonTargetOSMap = new HashMap<>();
		LinkedHashMap<String,PerformanceMonitoringDTO> averageMap = new LinkedHashMap<>();
		
		IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
		
		try{
			List<SmartCampaignPlacementObj> smartCampaignPlacementObjList = getCampaignPlacementObj(campaignId, placementIds);
			String memcacheKey = orderId+campaignId+placementIds+isNoise+threshold+publisherIdInBQ+placementInfo+partnerInfo+smartCampaignPlacementObjList.toString();
			String keyPrefix = "PM_OSData";
			memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
			jsonChartMap = (Map<String,String>) MemcacheUtil.getObjectFromCache(memcacheKey);
			if(jsonChartMap != null && jsonChartMap.size() > 0) {
				log.info("Found in Memcache : "+memcacheKey);
				return jsonChartMap;
			}
			else {
				log.info("Not in Memcache : "+memcacheKey);
				jsonChartMap = new HashMap<>();
			}
			String lineItemId = "";
			String queryData = "";
			JSONObject partnerInfoJson = (JSONObject) JSONSerializer.toJSON(partnerInfo);
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			String targetOS = "";
			String nonTargetOS = "";
			log.info("placementId : "+placementIds);
			
			if(smartCampaignPlacementObjList != null && smartCampaignPlacementObjList.size() > 0) {
				for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementObjList) {
					if(smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlatformObj()!=null  && smartCampaignPlacementObj.getPlatformObj().size()>0) {
						//totaltarget = totaltarget + StringUtil.getLongValue(smartCampaignPlacementObj.getImpressions());
						for(PlatformObj obj : smartCampaignPlacementObj.getPlatformObj()){
							if(obj != null){
								campaignOSMap.put(obj.getText(), null);
							}
						}
					}
				}
			}
		
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_DFP_TARGET);
			queryData = queryDTO.getQueryData();
			
			if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				lineItemId = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.loadOSChartData(orderId, lineItemId, isNoise, threshold, queryDTO);
			}
			
			if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0 && placementJson != null && partnerInfoJson != null) {
				List<TableRow> rowList = queryResponse.getRows();
				for (TableRow row : rowList) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						PerformanceMonitoringDTO monitoringDTO = new PerformanceMonitoringDTO();
						List<TableCell> cellList = row.getF();
						String partnerName = partnerInfoJson.get(cellList.get(3).getV().toString())+"";
						if(partnerName!=null && partnerName.length()>0){
							monitoringDTO.setPublisherName(partnerName);
						}
						monitoringDTO.setImpressionDelivered(cellList.get(0).getV().toString());
						monitoringDTO.setClicks(cellList.get(1).getV().toString());
						monitoringDTO.setCTR(cellList.get(2).getV().toString());
						monitoringDTO.setOperatingSystem(cellList.get(4).getV().toString());
						monitoringDTOList.add(monitoringDTO);
					}
				}
			}
			
			System.out.println("Execute upto here : " + queryResponse.getRows().size());
			// Dividing Data into various categories 
			if(monitoringDTOList!=null && monitoringDTOList.size()>0){
				for (PerformanceMonitoringDTO performanceMonitoringDTO : monitoringDTOList) {
					if(performanceMonitoringDTO!=null && performanceMonitoringDTO.getPublisherName()!=null && performanceMonitoringDTO.getOperatingSystem()!=null){
						PerformanceMonitoringDTO dto = new PerformanceMonitoringDTO();
						PerformanceMonitoringDTO aveDto = new PerformanceMonitoringDTO();
						long impressions = 0L;
						long clicks = 0L;
						double ctr = 0.0;
						int averageCount = 0; 
						double averageImpression=0.0;
						double averageClick=0.0;
						double averageCTR=0.0;
						
						String key = performanceMonitoringDTO.getPublisherName()+"_"+performanceMonitoringDTO.getOperatingSystem();
						if(osDataMap!=null && !isKeyAvailable(osDataMap.keySet(),key)){
							osDataMap.put(key, performanceMonitoringDTO);
							partnerMap.put(performanceMonitoringDTO.getPublisherName(), null);
							if(performanceMonitoringDTO!=null){
								aveDto.setImpAverage(performanceMonitoringDTO.getImpressionDelivered());
								aveDto.setClickAverage(performanceMonitoringDTO.getClicks());
								aveDto.setCtrAverage(performanceMonitoringDTO.getCTR());
								averageMap.put(performanceMonitoringDTO.getOperatingSystem(), aveDto);
								osMap.put(performanceMonitoringDTO.getOperatingSystem(), null);
							
							}
						}else{
							dto = osDataMap.get(key);
							String[] averageKeyArr = key.split("_");
							String averageKey = averageKeyArr[1];
							aveDto = averageMap.get(averageKey);
						    averageCount = aveDto.getAverageCount()+1;
						    
						    if(dto!=null){
						    	if(dto.getImpressionDelivered()!=null && dto.getImpressionDelivered().length()>0 && performanceMonitoringDTO.getImpressionDelivered()!=null && performanceMonitoringDTO.getImpressionDelivered().length()>0 ){
						    		impressions = StringUtil.getLongValue(dto.getImpressionDelivered()) + StringUtil.getLongValue(performanceMonitoringDTO.getImpressionDelivered());
									averageImpression = (double) impressions/averageCount;
						    	}
						    	if(dto.getClicks()!=null && dto.getClicks().length()>0 && performanceMonitoringDTO.getClicks()!=null 
										&& performanceMonitoringDTO.getClicks().length()>0 ){
									clicks = StringUtil.getLongValue(dto.getClicks())+ StringUtil.getLongValue(performanceMonitoringDTO.getClicks());
									averageClick = (double) clicks/averageCount;
								}
								if(dto.getCTR()!=null && dto.getCTR().length()>0 && performanceMonitoringDTO.getCTR()!=null 
										&& performanceMonitoringDTO.getCTR().length()>0 ){
									ctr = StringUtil.getDoubleValue(dto.getCTR())+ StringUtil.getDoubleValue(performanceMonitoringDTO.getCTR());
									averageCTR =  ctr/averageCount;
								}
								
								dto.setImpressionDelivered(impressions+"");
								dto.setClicks(clicks+"");
								dto.setCTR(ctr+"");
								osDataMap.put(key, dto);
								aveDto.setImpAverage(averageImpression+"");
								aveDto.setClickAverage(averageClick+"");
								aveDto.setCtrAverage(averageCTR+"");
								averageMap.put(averageKey, aveDto);
								
						    }
						}
						
					}
				}
			}
			
			for(Entry<String, Object> targetMap : campaignOSMap.entrySet()) {
				 String key = targetMap.getKey();
				 targetOS = targetOS+key+",";
			 }
			targetOS = StringUtil.deleteFromLastOccurence(targetOS, ",");
			
			System.out.println("Target OS : " + targetOS);
			
			for(Entry<String, Long> nonTargetMap : nonTargetOSMap.entrySet()) {
				 String key = nonTargetMap.getKey();
				 nonTargetOS = nonTargetOS+key+",";
			 }
			nonTargetOS = StringUtil.deleteFromLastOccurence(nonTargetOS, ",");
			
			// Conversion of data to JSON
			String ctrLineChartJson=createJsonResponseForBarChart("os","Operating System",osDataMap,partnerMap,osMap,averageMap , "CTR");
			jsonChartMap.put("CTR", ctrLineChartJson);
			String clicksLineChartJson=createJsonResponseForBarChart("os","Operating System",osDataMap,partnerMap,osMap,averageMap, "Clicks");
			jsonChartMap.put("Clicks", clicksLineChartJson);
			String impressionsLineChartJson=createJsonResponseForBarChart("os","Operating System",osDataMap,partnerMap,osMap,averageMap, "Impressions");
			jsonChartMap.put("Impressions", impressionsLineChartJson);
			
			 queryResponse = null;
			 queryDTO.setQueryData(queryData);
			 	
			 if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
			 		
			 		queryResponse = monitoringDAO.loadOperatingSystemImpressionClicksChartDate(orderId, lineItemId, isNoise, threshold, queryDTO);
			 		
			 		ArrayList<String> donutImpressionList = new ArrayList<>();
			 		donutImpressionList.add("Target_Value");
			 		donutImpressionList.add("Impression_Delivered");
			 		
			 		
			 		ArrayList<String> donutClickList = new ArrayList<>();
			 		donutClickList.add("Target_Value");
			 		donutClickList.add("Clicks");
			 		
			 		DataTable impressionDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutImpressionList);
			 		DataTable clickDataTable = GoogleVisulizationUtil.buildCustomColumns(queryResponse,donutClickList);
			 		
			 		
					if(queryResponse !=null && queryResponse.getRows()!=null){
						impressionDataTable = GoogleVisulizationUtil.buildCustomRows(impressionDataTable, queryResponse,2);
						clickDataTable = GoogleVisulizationUtil.buildCustomRows(clickDataTable, queryResponse,1);
					}else{
						log.warning("No data found for this campaign -"+orderId);
					}
					
					String osImpDonutJson =JsonRenderer.renderDataTable(impressionDataTable, true, false).toString();
					jsonChartMap.put("DonutImpression", osImpDonutJson);
					String osClickDonutJson =JsonRenderer.renderDataTable(clickDataTable, true, false).toString();
					jsonChartMap.put("DonutClick", osClickDonutJson);
					
					
					List<TableRow> rowList = queryResponse.getRows();
					JSONArray jsonImpressionArray = new JSONArray();
					JSONArray jsonClickArray = new JSONArray();
					int i=0;
					
					for (TableRow row : rowList) {
						
						if(row != null && row.getF() != null && row.getF().size() > 0) {
							JSONObject  jsonImpressionObject = new JSONObject();
							JSONObject  jsonClickObject = new JSONObject();
							
							List<TableCell> cellList = row.getF();
							
								jsonImpressionObject.put("creative", cellList.get(0).getV().toString());
								jsonImpressionObject.put("impressions", cellList.get(1).getV().toString());
								jsonImpressionObject.put("color",lagendColor.values()[i%10].name());
								jsonImpressionArray.add(jsonImpressionObject);
							
								jsonClickObject.put("creative", cellList.get(0).getV().toString());
								jsonClickObject.put("clicks", cellList.get(2).getV().toString());
								jsonClickObject.put("color",lagendColor.values()[i%10].name());
								jsonClickArray.add(jsonClickObject);
			
							
						}
						i++;
					}
					jsonChartMap.put("ImpByOS", jsonImpressionArray.toString());
					jsonChartMap.put("ClicksByOS", jsonClickArray.toString());
			 	}
			 	
			 queryResponse = null;
			 queryDTO.setQueryData(queryData);
			 String targetGoal = "";
			 if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
				 queryResponse = monitoringDAO.loadOperatingTotalGoal(orderId, lineItemId,targetOS, queryDTO);
					List<TableRow> totalRowList = queryResponse.getRows();
					if(totalRowList.size() > 0){
						List<TableCell> cellList = totalRowList.get(0).getF();
						targetGoal = cellList.get(0).getV().toString();
					}
			 }
			 
			if(targetOS == "")
				targetOS = "All OS";
				
			if(nonTargetOS == "")
				nonTargetOS = "Other OS";
			 
			jsonChartMap.put("TargetOS",targetOS);
			jsonChartMap.put("NonTargetOS",nonTargetOS);
			jsonChartMap.put("TargetGoal",targetGoal);
			jsonChartMap.put("NonTargetGoal","");
			jsonChartMap.put("NonTargetPercentage", "");
			jsonChartMap.put("TargetPercentage", "");
			jsonChartMap.put("partnerCount",partnerMap.size()+"");
			MemcacheUtil.setObjectInCache(memcacheKey, jsonChartMap, EXPIRATION_TIME);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return jsonChartMap;
	}


	/**
	 * @param keys
	 * @param KeyVal
	 * @return boolean
	 * @description 	This method check whether search string contains within set of keys.
	 * @author  Anup Dutta 
	 */
	public boolean isKeyAvailable(Set<String> keys , String KeyVal){
		for (String key : keys) {
			if(KeyVal.toLowerCase().contains(key.toLowerCase()))
				{
				return true;
				}
		}
		return false;
	}

	
	
	/*
	 * @author Youdhveer Panwar
	 * 
	 * This method will create donut data for rich media campaign
	 * 
	 * 
	 */
	/*
	public JSONObject richMediaDonutChartData(String orderId, String campaignId, String placementId, String publisherIdInBQ, String placementInfo) {
		JSONObject richMediaDonutChartJson = null;
		String memcacheKey = orderId+campaignId+placementId+publisherIdInBQ+placementInfo;
		String keyPrefix = "PM_RMDonutData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			richMediaDonutChartJson = (JSONObject) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(richMediaDonutChartJson != null && richMediaDonutChartJson.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return richMediaDonutChartJson;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			richMediaDonutChartJson = new JSONObject();
		}
		IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
		try{
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CUSTOM_EVENT);
			
			JSONObject placementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			if(placementJson != null) {
				placementJson = (JSONObject) placementJson.get(placementId);
			}
			 Set<String> customEventSet=new HashSet<String>();
			 if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				String lineItemIds = placementJson.get("lineItemIds")+"";
				ArrayList<String> callButtonDonut = new ArrayList<>();
				callButtonDonut.add("Creative_Size");
				callButtonDonut.add("CountOrTimeValue");
				DataTable callButtonDataTable=null;
				
				ArrayList<String> exitEventDonut = new ArrayList<>();
				exitEventDonut.add("Creative_Size");
				exitEventDonut.add("CountOrTimeValue");
				DataTable exitEventDataTable=null;
				
				String exitEventDonutJson=null;
				String callButtonDonutJson=null;
				
				queryResponse = monitoringDAO.loadRMCustomEventCards(orderId, lineItemIds,  queryDTO);
				
				if(queryResponse !=null && queryResponse.getRows()!=null && queryResponse.getRows().size()>0){
					List<TableRow> rowList = queryResponse.getRows();
					List<String> creativeSizeList=new ArrayList<String>();
							
					JSONArray jsonCallButtonArray = new JSONArray();
					JSONArray jsonExitButtonArray = new JSONArray();
					int i=0;
					int totalCallButtonCount=0;
					int totalExitButtonCount=0;
					for (TableRow row : rowList) {
						if(row != null && row.getF() != null && row.getF().size() > 0) {
							
							JSONObject  jsonCallButtonObject = new JSONObject();
							JSONObject  jsonExitButtonObject = new JSONObject();
							
							List<TableCell> cellList = row.getF();
							String cutomEvent=cellList.get(0).getV().toString();
							String customEventType=cellList.get(1).getV().toString();
							String creativeSize=cellList.get(2).getV().toString();
							String counter=cellList.get(3).getV().toString();
							
							if(!creativeSizeList.contains(creativeSize)){
								creativeSizeList.add(creativeSize);
							}
							
							if(!customEventSet.contains(cutomEvent)){
								log.info("Create column header datatable for customEventType :"+customEventType);
								 TableSchema tabSchema = queryResponse.getSchema();
								if(customEventType !=null && customEventType.equalsIgnoreCase("Exit")){
									log.info("Create columns for exitEventDataTable...");
									exitEventDataTable = GoogleVisulizationUtil.buildCustomColumns(exitEventDonut,tabSchema);								
								}else{
									log.info("Create columns for callButtonDataTable...");
									callButtonDataTable = GoogleVisulizationUtil.buildCustomColumns(callButtonDonut,tabSchema);								
								}
							}
							customEventSet.add(cutomEvent);
							
							int creativeIndex=creativeSizeList.indexOf(creativeSize);
							//Creating legends data
							if(customEventType !=null && customEventType.equalsIgnoreCase("Exit")){								
								
								jsonExitButtonObject.put("event", cutomEvent);
								jsonExitButtonObject.put("creative", creativeSize);
								jsonExitButtonObject.put("count", counter);
								jsonExitButtonObject.put("color",lagendColor.values()[creativeIndex%10].name());
								jsonExitButtonArray.add(jsonExitButtonObject);
								totalExitButtonCount=totalExitButtonCount+Integer.parseInt(counter);
							}else{
								jsonCallButtonObject.put("event", cutomEvent);
								jsonCallButtonObject.put("creative", creativeSize);
								jsonCallButtonObject.put("count", counter);
								jsonCallButtonObject.put("color",lagendColor.values()[creativeIndex%10].name());								
								jsonCallButtonArray.add(jsonCallButtonObject);
								totalCallButtonCount=totalCallButtonCount+Integer.parseInt(counter);
							}						
						}
						i++;
						
				    }
					log.info("customEventSet : "+customEventSet.size());
					
					log.info("Going add rows for each donut.....");
					List<Integer> skipCol=new ArrayList<Integer>();
					skipCol.add(0);
					skipCol.add(1);
					callButtonDataTable = GoogleVisulizationUtil.buildCustomRows(callButtonDataTable, queryResponse,skipCol,"Call Button");
					exitEventDataTable = GoogleVisulizationUtil.buildCustomRows(exitEventDataTable, queryResponse,skipCol,"Main Exit");
										
					log.info("Donut json data created...");
					callButtonDonutJson =JsonRenderer.renderDataTable(callButtonDataTable, true, false).toString();
					log.info("callButtonDonutJson :"+callButtonDonutJson);
					exitEventDonutJson =JsonRenderer.renderDataTable(exitEventDataTable, true, false).toString();
					log.info("exitEventDonutJson :"+exitEventDonutJson);
					
					richMediaDonutChartJson.put("DonutCallButton", callButtonDonutJson);
					richMediaDonutChartJson.put("DonutExitButton", exitEventDonutJson);
					
					
					richMediaDonutChartJson.put("CallButtonLegend", jsonCallButtonArray.toString());
					//JSONObject totalCallobj=new JSONObject();				
					//totalCallobj.put("total", totalCallButtonCount);					
					richMediaDonutChartJson.put("totalCallButton", totalCallButtonCount);
					
					JSONObject totalExitobj=new JSONObject();
					totalExitobj.put("total", totalExitButtonCount);
					totalExitobj.put("event","N/A");
					jsonExitButtonArray.add(totalExitobj);
					richMediaDonutChartJson.put("ExitButtonLegend", jsonExitButtonArray.toString());
					richMediaDonutChartJson.put("totalExitButton", totalExitButtonCount);
					MemcacheUtil.setObjectInCache(memcacheKey, richMediaDonutChartJson.toString(), EXPIRATION_TIME);
				}else{
					log.warning("No data found for this campaign -"+orderId+" and lineItemIds --"+lineItemIds);
				}	
		  }			 
		}catch(Exception e){
			log.severe("Exception in rich media donut data -- "+e.getMessage());
			richMediaDonutChartJson.put("error", "Failed to load RichMedia Donut Chart data");
		}finally{			
			log.info("richMediaDonutChartJson : "+richMediaDonutChartJson.toString());
		}
		return richMediaDonutChartJson;
	}*/
	
   public JSONArray richMediaDonutChartData(String orderId, String campaignId, String placementIds, String publisherIdInBQ, String placementInfo) {
		JSONObject donutChartJson = new JSONObject();	
		JSONArray jsonArray = null;
		
		String memcacheKey = orderId+campaignId+placementIds+publisherIdInBQ+placementInfo;
		String keyPrefix = "PM_RMDonutData";
		memcacheKey =  keyPrefix + StringUtil.hashToMaxLength(memcacheKey, (MemcacheUtil.MEMCACHE_KEY_LENGTH - keyPrefix.length()));
		String cachedDataJsonString = (String) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(cachedDataJsonString != null && cachedDataJsonString.length() > 0) {
			jsonArray = (JSONArray) JSONSerializer.toJSON(cachedDataJsonString);
		}
		if(jsonArray != null && jsonArray.size() > 0) {
			log.info("Found in Memcache : "+memcacheKey);
			return jsonArray;
		}
		else {
			log.info("Not in Memcache : "+memcacheKey);
			jsonArray = new JSONArray();
		}
		
		Map<String,List<RichMediaDataDTO>> dataMap = new HashMap<>();
		List<RichMediaDataDTO> richMediaDataDTOList = new ArrayList<>();
		IPerformanceMonitoringDAO monitoringDAO = new PerformanceMonitoringDAO();
		RichMediaDataDTO dataDTO = new RichMediaDataDTO();
		try{
			 //Pull List of custom Events for a campaign
			
			//Fetch only those tables which are under start date and end dates
			
			QueryResponse queryResponse = null;
			QueryDTO queryDTO=getCampaignQueryDTO(campaignId, publisherIdInBQ, LinMobileConstants.BQ_CUSTOM_EVENT);
			// fetching tables ends
			
			JSONObject allPlacementJson = (JSONObject) JSONSerializer.toJSON(placementInfo);
			JSONObject placementJson = null;
			if(allPlacementJson != null) {
				//placementJson = (JSONObject) allPlacementJson.get(placementId);
				placementJson = getConsolidatedJsonForMultiplePlacements(placementIds.split(","), allPlacementJson, "MM-dd-yyyy", null);
			}
			
			 if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0 && placementJson != null) {
				String lineItemIds = placementJson.get("lineItemIds")+"";
				queryResponse = monitoringDAO.loadRMCustomEventCards(orderId, lineItemIds,  queryDTO);
				if(queryResponse !=null && queryResponse.getRows()!=null && queryResponse.getRows().size()>0){
					List<String> creativeSizeList=new ArrayList<String>();
					List<TableRow> rowList = queryResponse.getRows();
					for (TableRow row : rowList) {
						if(row != null && row.getF() != null && row.getF().size() > 0) {
							List<TableCell> cellList = row.getF();
							dataDTO = new RichMediaDataDTO(
								(cellList.get(0).getV().toString()),
								(cellList.get(1).getV().toString()),
								(cellList.get(2).getV().toString()),
								(cellList.get(3).getV().toString()));
							
							richMediaDataDTOList.add(dataDTO);
						}
					}
						
						if(richMediaDataDTOList!=null && richMediaDataDTOList.size()>0){
							for (RichMediaDataDTO dto : richMediaDataDTOList) {
								if(dto!=null){
									if(!dataMap.containsKey(dto.getCutomEvent())){
										List<RichMediaDataDTO> dataList = new ArrayList<>();
										if(!creativeSizeList.contains(dto.getCreativeSize())){
											creativeSizeList.add(dto.getCreativeSize());
										}
										dataList.add(dto);
										dataMap.put(dto.getCutomEvent(),dataList);
									}else{
										List<RichMediaDataDTO> dataList = new ArrayList<>();
										dataList = dataMap.get(dto.getCutomEvent());
										if(!creativeSizeList.contains(dto.getCreativeSize())){
											creativeSizeList.add(dto.getCreativeSize());
										}
										dataList.add(dto);
										dataMap.put(dto.getCutomEvent(), dataList);
									}
								}
								
							}
						}
						
						ArrayList<String> donutColumnList = new ArrayList<>();
						donutColumnList.add("Creative_Size");
						donutColumnList.add("CountOrTimeValue");
						
						for(String customEventType : dataMap.keySet()) {
							
							log.info("dataMap size :"+dataMap.size());
							List<RichMediaDataDTO> dataList = new ArrayList<>();
							String donutJson=null;
							int totalCount=0;
							JSONArray jsonLagendArray = new JSONArray();
							JSONObject  jsonObject = new JSONObject();
							StringBuilder sbStr = new StringBuilder();
							dataList = dataMap.get(customEventType);
							String customEvent = "";
							if(dataList!=null && dataList.size()>0){
								 customEvent = dataList.get(0).getCutomEvent();
								
								 sbStr.append("[['Creative Size',   'Count']");
								 
								 for (RichMediaDataDTO richMediaDataDTO : dataList) {
									if(richMediaDataDTO!=null){
										int creativeIndex=creativeSizeList.indexOf(richMediaDataDTO.getCreativeSize());
										jsonObject.put("event",richMediaDataDTO.getCutomEvent() );
										jsonObject.put("creative", richMediaDataDTO.getCreativeSize());
										jsonObject.put("count", richMediaDataDTO.getCounter());
										jsonObject.put("color",lagendColor.values()[creativeIndex%10].name());
										jsonLagendArray.add(jsonObject);
										totalCount=totalCount+Integer.parseInt(richMediaDataDTO.getCounter());
										
										sbStr.append(",[");
										sbStr.append("'"+richMediaDataDTO.getCreativeSize()+"',"+richMediaDataDTO.getCounter());
										sbStr.append("]");
									}
								}
								 sbStr.append("]");
							}
							DataTable donutDataTable=null;
							TableSchema tabSchema = queryResponse.getSchema();
							donutDataTable = GoogleVisulizationUtil.buildCustomColumns(donutColumnList,tabSchema);	
							log.info("Going add rows for each donut.....");
							List<Integer> skipCol=new ArrayList<Integer>();
							skipCol.add(0);
							skipCol.add(1);
							donutDataTable = GoogleVisulizationUtil.buildCustomRows(donutDataTable, queryResponse,skipCol,customEvent);
							donutJson =JsonRenderer.renderDataTable(donutDataTable, true, false).toString();
							donutChartJson.put("donutJson", donutJson);
							donutChartJson.put("totalCount", totalCount);
							donutChartJson.put("jsonLagendArray", jsonLagendArray.toString());
							donutChartJson.put("donutStr", sbStr.toString());
							donutChartJson.put("donutTitle", customEvent.toUpperCase());
							
							jsonArray.add(donutChartJson);
							MemcacheUtil.setObjectInCache(memcacheKey, jsonArray.toString(), EXPIRATION_TIME);
						}
					}
				}
			 
			 
			
	}catch(Exception e){
		log.severe("Exception in rich media donut data -- "+e.getMessage());
	}finally{			
	}
	return jsonArray;

}
	/*
	 * @author YoudhveePanwar
	 * This method will create queryDTO(finalise and non-finalise tables) based on parameter
	 * @param String campaignId,String publisherIdInBQ,String bqSchema
	 */
	public QueryDTO getCampaignQueryDTO(String campaignId,String publisherIdInBQ,String bqSchema){
		//Fetch only those tables which are under start date and end dates
		log.info("Generate query DTO for ....campaignId : "+campaignId+" : publisherIdInBQ:"+publisherIdInBQ+" and bqSchema :"+bqSchema);
		String startDate=null;
		String endDate=null;
		QueryDTO queryDTO=null;
		if(campaignId !=null){
			String memcacheKey="QueryDTO_Order_"+campaignId+"_"+bqSchema;
			queryDTO=(QueryDTO) MemcacheUtil.getObjectFromCache(memcacheKey);
			
			if(queryDTO ==null){
				log.info("queryDTO not found in cache, for memcacheKey : "+memcacheKey+", re-generate it..");
				ISmartCampaignPlannerService smartCampaignPlannerService=(ISmartCampaignPlannerService)BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);

				SmartCampaignObj campaignObj=smartCampaignPlannerService.loadSmartCampaign(campaignId);
				if(campaignObj !=null){
					startDate=campaignObj.getStartDate();
					endDate=campaignObj.getEndDate();
					if(startDate !=null){
						startDate=DateUtil.getFormatedDate(startDate, "MM-dd-yyy", "yyyy-MM-dd");
					}
					if(endDate !=null){
						endDate=DateUtil.getFormatedDate(endDate, "MM-dd-yyy", "yyyy-MM-dd");
					}
					
					queryDTO=BigQueryUtil.getQueryDTO(startDate,endDate,publisherIdInBQ, bqSchema); //bqSchema  will be like LinMobileConstants.BQ_CORE_PERFORMANCE
					if(queryDTO ==null || queryDTO.getQueryData() ==null || queryDTO.getQueryData().trim().length()==0){
						log.severe("Failed to load big quey tables to make query...");
					}else{
						MemcacheUtil.setObjectInCache(memcacheKey, queryDTO, 1*60*60);
					}
					
				}else{
					log.warning("No campaign found with this id :  "+campaignId);
				}
				
			}else{
				log.info("queryDTO  found in cache, memcacheKey : "+memcacheKey+", queryDTO :"+queryDTO.toString());
			}
		}else{
			log.info("Invalid campaign id : "+campaignId);
		}
		return queryDTO;
	}

	
	/*
	 * @author : Anup Dutta
	 * @description : This method will convert String startdate/enddate to Date type sDate/eDate 
	 * */
	@Override
	public void updateCampaignData() {
		log.info("inside updateCampaignData of PerformanceMonitoringService");
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		dao.updateSmartCampaignData();
	}

	
	/*
	 * @author Anup Dutta
	 * @Description This method will give Rank to a location based on the Census and Group By zip, dma, state
	 **/
	@Override
	public JSONArray getLocationRank(int rankRatio, String rankBy,
			JSONArray censusArry,String gender) {
		
		JSONArray jsonArray = null;
		IPerformanceMonitoringDAO dao = new PerformanceMonitoringDAO();
		StringBuffer rankStmt = new StringBuffer();
		StringBuffer categoryStmt = new StringBuffer();
		StringBuffer subQueryCategoryStmt = new StringBuffer();
		
		StringBuffer subQuery2 = new StringBuffer();
		StringBuffer subQuery3 = new StringBuffer();
		
		
		QueryDTO queryDTO=null;
		QueryResponse queryResponse = null;
		StringBuffer keyFormat = new StringBuffer();
		String keyPrefix = "getLocation_rank_";
		String mainPopulationColumn = "";
		
		switch(gender){
			case "male":
			case "MALE":
				mainPopulationColumn = "hh_male_population";
				break;
			
			case "female":
			case "FEMALE":
				mainPopulationColumn = "hh_female_population";
				break;
				
			case "all":
			case "ALL":
				mainPopulationColumn = "hh_population";
				break;
		}
		rankStmt.append("ceil((");
		
		for(int i=0;i<censusArry.size();i++){
			JSONObject censusObj = censusArry.getJSONObject(i);
			String actualColumn = censusObj.getString("actualColumn");
			String parentColumn = censusObj.getString("parentColumn");
			if(i>0){
				rankStmt.append(" + ");
				keyFormat.append("_");
			}
			rankStmt.append("("+actualColumn+"*"+ parentColumn +"/100)");
			//categoryStmt.append(", avg("+ actualColumn+") ");
			categoryStmt.append(", ((sum(a"+i+")/sum( "+mainPopulationColumn+" ))*100) ");
			subQueryCategoryStmt.append(", "+ actualColumn+" ");
			
			subQuery2.append(", sum(a"+i+") ");
			subQuery3.append(", ("+actualColumn+"*"+ parentColumn +"/100) as a"+i + " ");
			
			keyFormat.append(actualColumn);
		}
		
		categoryStmt.append(subQuery2);
		subQueryCategoryStmt.append(subQuery3);
		
		rankStmt.append(")/"+censusArry.size()+") as avg_pop");
		
		String memcacheKey = keyPrefix + StringUtil.getHashedValue(keyFormat.toString());
		//jsonArray = (JSONArray) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(jsonArray == null || jsonArray.size() == 0)
		{
			jsonArray = new JSONArray();
			queryDTO = BigQueryUtil.getGeoQueryDTO();
			queryResponse = dao.getLocationRank(mainPopulationColumn,rankRatio,rankBy,rankStmt,categoryStmt,subQueryCategoryStmt,queryDTO);
			List<TableRow> records = queryResponse.getRows();
			for (TableRow row : records) {
				if(row != null && row.getF() != null && row.getF().size() > 0) {
					List<TableCell> cellList = row.getF();
					JSONObject jsonrow = new JSONObject();
					int addonCol = 0;
					switch(rankBy){
						case "DMA":
						case "dma":
							jsonrow.put("location",cellList.get(0).getV().toString());
							jsonrow.put("locationName",cellList.get(1).getV().toString());
							jsonrow.put("totalPop",(int)(Float.parseFloat(cellList.get(2).getV().toString())));
							
							jsonrow.put("rank",(int)(Float.parseFloat(cellList.get(4).getV().toString())*100));
							jsonrow.put("color",getHexColorCode(Float.parseFloat(cellList.get(4).getV().toString())));
							addonCol = 5;
							break;
							
						case "state":
						case "STATE":
							jsonrow.put("location",cellList.get(0).getV().toString());
							jsonrow.put("locationName",cellList.get(1).getV().toString());
							jsonrow.put("totalPop",(int)(Float.parseFloat(cellList.get(2).getV().toString())));
							
							jsonrow.put("rank",(int)(Float.parseFloat(cellList.get(4).getV().toString())*100));
							jsonrow.put("color",getHexColorCode(Float.parseFloat(cellList.get(4).getV().toString())));
							addonCol = 5;
							break;
							
						case "zip":
						case "ZIP":
							jsonrow.put("location",cellList.get(0).getV().toString());
							jsonrow.put("locationName",cellList.get(0).getV().toString());
							jsonrow.put("totalPop",(int)(Float.parseFloat(cellList.get(1).getV().toString())));
							
							jsonrow.put("rank",(int)(Float.parseFloat(cellList.get(3).getV().toString())*100));
							jsonrow.put("color",getHexColorCode(Float.parseFloat(cellList.get(3).getV().toString())));
							addonCol = 4;
							break;	
							
					}
					
					JSONArray jsonDetailArray = new JSONArray();
					for(int i=0;i<censusArry.size();i++){
						JSONObject censusObj = censusArry.getJSONObject(i);
						String actualColumn = censusObj.getString("actualColumn");
						String type = censusObj.getString("type");
						JSONObject detail = new JSONObject();
						
						float val = Float.parseFloat(cellList.get(i+addonCol).getV().toString());
						int tot = (int)Float.parseFloat(cellList.get(i+addonCol+censusArry.size()).getV().toString());
						
						detail.put("col", actualColumn);
						detail.put("type", type);
						
						detail.put("val", val);
						detail.put("tot", tot);
						
						jsonDetailArray.add(detail);
					}
					
					jsonrow.put("detail", jsonDetailArray);
					
					jsonArray.add(jsonrow);
				}
			}
			
			//MemcacheUtil.setObjectInCache(memcacheKey, jsonArray);
		}
		return jsonArray;
		
	}
	/*
	 * @author Anup Dutta
	 * @Description 
	 * This Method will return hexadeecimal color code base on range passed
	 * Range start from 0 to 100
	 * Output will vary from Red to Green
	 * */
	public String getHexColorCode(double rank){
		int n = (int)(rank * 100);
		String color = "";
		if(n > 0 && n <=20){
			color = "#74c7e3";
		}else if(n > 20 && n <=40){
			color = "#84afe3";
		}else if(n > 40 && n <=60){
			color = "#f7d183";
		}else if(n > 60 && n <=80){
			color = "#60cc9a";
		}else{
			color = "#e67253";
		}
			
		return color;	
	}
	
	public String getHexColorCodeYellowtoRed(double rank){
		int n = (int)(rank * 100);
		int	r = 255 ;
		int g = (255 * (((100-n)/10) * 10 )) / 100;
		int	b = 0;//(255 * n) / 100;
		
		String hex = String.format("#%02x%02x%02x", r, g, b);
		return hex;
	}
	
	public String getHexColorCode10shade(double rank){
		int n = (int)(rank * 100);
		String color = "#e9f5e9";
		if(n >= 10 && n < 20){
			color = "#c5e1a5";
		}else if(n >= 20 && n < 30){
			color = "#9fda34";
		}else if(n >= 30 && n < 40){
			color = "#8bc34a";
		}else if(n >= 40 && n < 50){
			color = "#81c784";
		}else if(n >= 50 && n < 60){
			color = "#ffdd00";
		}else if(n >= 60 && n < 70){
			color = "#f8c80d";
		}else if(n >= 70 && n < 80){
			color = "#ffa700";
		}else if(n >= 80 && n < 90){
			color = "#ff7800";
		}else if(n >= 90){
			color = "#ff5500";
		}
		 return color;
	}
	
	
	public String getHexColorCode5Shade(double rank){
		int n = (int)(rank * 100);
		String color = "#ffeac0";
		if(n >= 10 && n < 30){
			color = "#f7d591";
		}else if(n >= 30 && n < 40){
			color = "#ebbb56";
		}else if(n >= 40 && n < 60){
			color = "#e4a82e";
		}else if(n >= 60){
			color = "#dd9505";
		}
		 return color;
	}
	
	public String getHexColorGradientCode(double rank){
		int n = (int)(rank * 100);
		int	r = (255 * (100 - n)) / 100; 
		int g = (255 * n) / 100;
		int	b = 0;
		
		String hex = String.format("#%02x%02x%02x", r, g, b);
		return hex;
	}

	@Override
	public JSONArray getCampaignDetailByPartner(String orderID, String campaignID,String publisherIdInBQ, String partnerName) {
		
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		IPerformanceMonitoringDAO performanceMonitoringDAO = new PerformanceMonitoringDAO();
		QueryResponse queryResponse = null;
		JSONArray jsonArray = null;
		
		StringBuffer keyFormat = new StringBuffer();
		String keyPrefix = "getCampaignDetailByPartner_";
		
		keyFormat.append(orderID).append(campaignID).append(publisherIdInBQ).append(partnerName.replaceAll(" ", "_"));
		
		String memcacheKey = keyPrefix + StringUtil.getHashedValue(keyFormat.toString());
		jsonArray = (JSONArray) MemcacheUtil.getObjectFromCache(memcacheKey);
		if(jsonArray == null || jsonArray.size() == 0)
		{
			List<Long> lineItemList = campaignPlannerDAO.getAllLineItemIDByPartner(Long.parseLong(campaignID), partnerName);
			
			if(lineItemList.size()>0){
				QueryDTO queryDTO = getCampaignQueryDTO(campaignID, publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
				queryResponse = performanceMonitoringDAO.getCampaignDetailByPartner(orderID, lineItemList, queryDTO);
				
				List<TableRow> records = queryResponse.getRows();
				
				jsonArray = new JSONArray();
				
				for (TableRow row : records) {
					if(row != null && row.getF() != null && row.getF().size() > 0) {
						List<TableCell> cellList = row.getF();
						JSONObject jsonrow = new JSONObject();
						
						jsonrow.put("date",cellList.get(1).getV());
						jsonrow.put("impression",(int)(Float.parseFloat(cellList.get(2).getV().toString())));
						jsonrow.put("click",(int)(Float.parseFloat(cellList.get(3).getV().toString())));
						jsonArray.add(jsonrow);
					}
				}
			}
			MemcacheUtil.setObjectInCache(memcacheKey, jsonArray);
		}
		
		return jsonArray;
	}

	@Override
	public JSONArray getRUNDspCampaignList() {
		String response = RUNDspUtil.getResponse(RUNDspUtil.campaignURL);
		JSONArray resArray = JSONArray.fromObject(response);
	
		JSONArray result = new JSONArray();
		
		for(int i=0;i<resArray.size();i++){
			JSONObject jsonObject = new JSONObject();
			JSONObject row = (JSONObject)resArray.get(i);
			
			jsonObject.put("id", row.get("_id"));
			jsonObject.put("name", row.get("name"));
			
			result.add(jsonObject);
		}
		return result;
	}

	@Override
	public JSONArray getRUNDspCampaignDetail(String campaingID, String groupBy,String start, String end) {
		String response = "";
		String url = "";
		JSONArray result = null;
		switch(groupBy){
			case "day":
			case "DAY": url = RUNDspUtil.getCampaignDetailByDayURL(campaingID, start, end); break;
			
			default:
				url = RUNDspUtil.getCampaignDetailByDayURL(campaingID, start, end); break;
		}
		
		StringBuffer keyFormat = new StringBuffer();
		String keyPrefix = "getRUNDspCampaignDetail_";
		
		keyFormat.append(campaingID).append(groupBy).append(start).append(end);
		
		String memcacheKey = keyPrefix + StringUtil.getHashedValue(keyFormat.toString());
		result = (JSONArray) MemcacheUtil.getObjectFromCache(memcacheKey);
		
		if(result == null || result.size() == 0)
		{
			response = RUNDspUtil.getResponse(url);
			JSONArray resArray = JSONArray.fromObject(response);
			
			result = new JSONArray();
			
			for(int i=0;i<resArray.size();i++){
				JSONObject jsonObject = new JSONObject();
				JSONObject row = (JSONObject)resArray.get(i);
				
				long timeStamp = Long.parseLong(row.get("unix_time").toString());
				Date date = new Date(timeStamp*1000);
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				jsonObject.put("date",format.format(date));
				jsonObject.put("impression", row.get("imp"));
				jsonObject.put("click", row.get("cli"));
				
				result.add(jsonObject);
			}
			MemcacheUtil.setObjectInCache(memcacheKey, result);
		}
		
		return result;
	}
	
	
	
	
}
