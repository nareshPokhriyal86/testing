package com.lin.web.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.Duration;
import org.joda.time.Instant;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.utils.v201403.DateTimes;
import com.google.api.ads.dfp.jaxws.utils.v201403.StatementBuilder;
import com.google.api.ads.dfp.jaxws.v201403.CompanyPage;
import com.google.api.ads.dfp.jaxws.v201403.CompanyServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.CreativePage;
import com.google.api.ads.dfp.jaxws.v201403.CreativeServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemPage;
import com.google.api.ads.dfp.jaxws.v201403.LineItemServiceInterface;
import com.google.api.ads.dfp.jaxws.v201403.Order;
import com.google.api.ads.dfp.jaxws.v201403.OrderPage;
import com.google.api.ads.dfp.jaxws.v201403.OrderServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.googlecode.objectify.Key;
import com.ibm.icu.text.DecimalFormat;
import com.lin.dfp.api.IDFPCampaignSetupService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPCampaignSetupService;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.persistance.dao.IMediaPlanDAO;
import com.lin.persistance.dao.IProductDAO;
import com.lin.persistance.dao.ISmartCampaignPlannerDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.MediaPlanDAO;
import com.lin.persistance.dao.impl.ProductDAO;
import com.lin.persistance.dao.impl.SmartCampaignPlannerDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.SmartCampaignFlightObj;
import com.lin.server.bean.SmartCampaignHistObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.CensusDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.CommonDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.LineChartDTO;
import com.lin.web.dto.MigrateCampaignDTO;
import com.lin.web.dto.MigratePlacementDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SmartCampaignFlightDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;
import com.lin.web.dto.UnifiedCampaignDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IPerformanceMonitoringService;
import com.lin.web.service.IProductService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.util.CampaignStatusEnum;
import com.lin.web.util.DateUtil;
import com.lin.web.util.EmailUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.StringUtil;
import com.lin.web.util.TaskQueueUtil;

public class SmartCampaignPlannerService implements ISmartCampaignPlannerService {

	private static final Logger log = Logger.getLogger(SmartCampaignPlannerService.class.getName());
	public StringBuilder placementLevelLog = new StringBuilder();

	private DfpSession dfpSession;
	private DfpServices dfpServices;
	
	public static Object createAllNoneOptionObject(Object objectType,
			boolean isOptionAll) {
		String option = ProductService.allOption;
		String optionId = ProductService.allOptionId;
		if (!isOptionAll) {
			option = ProductService.noneOption;
			optionId = ProductService.noneOptionId;
		}
		if (objectType instanceof IABContextObj) {
			IABContextObj obj = new IABContextObj(
					StringUtil.getLongValue(optionId), option, option);
			return obj;
		} else if (objectType instanceof DeviceObj) {
			DeviceObj obj = new DeviceObj(StringUtil.getLongValue(optionId),
					option);
			return obj;
		} else if (objectType instanceof PlatformObj) {
			PlatformObj obj = new PlatformObj(
					StringUtil.getLongValue(optionId), option);
			return obj;
		} else if (objectType instanceof GeoTargetsObj) {
			GeoTargetsObj obj = new GeoTargetsObj(
					StringUtil.getLongValue(optionId), option);
			return obj;
		} else if (objectType instanceof StateObj) {
			StateObj obj = new StateObj(StringUtil.getLongValue(optionId),
					option, option);
			return obj;
		} else if (objectType instanceof CityDTO) {
			CityDTO obj = new CityDTO(StringUtil.getLongValue(optionId),
					option, StringUtil.getLongValue(optionId), option);
			return obj;
		} else if (objectType instanceof ZipDTO) {
			ZipDTO obj = new ZipDTO(optionId, option, option,
					StringUtil.getLongValue(optionId), option);
			return obj;
		} else {
			log.warning("Object does not match to any type");
		}
		return null;
	}

	public long saveCampaign(UnifiedCampaignDTO unifiedCampaignDTO, long userId, boolean isSuperAdmin) {
		log.info("In saveCampaign method of SmartCampaignPlannerService... ");
		SmartCampaignObj obj = new SmartCampaignObj();
		Date lastUpdateDate = DateUtil.getDateYYYYMMDDHHMMSS();
		SmartCampaignObj campaignPreviousCopy = null;
		try {
			ISmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
			IUserDetailsDAO userDAO = new UserDetailsDAO();
			if (unifiedCampaignDTO.getStatusId() == null || unifiedCampaignDTO.getStatusId().equals("") && !unifiedCampaignDTO.getStatusId().equals("edit")) {
				long maxId = dao.maxCampaignId();
				obj.setId(maxId + 1);
				obj.setCampaignId(maxId + 1);
				List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
				if (companyObjList != null && companyObjList.size() > 0) {
					for (CompanyObj companyObj : companyObjList) {
						if (companyObj != null && companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
							if (companyObj.getAdServerId() != null
									&& companyObj.getAdServerId().size() > 0
									&& companyObj.getAdServerUsername() != null
									&& companyObj.getAdServerUsername().size() > 0
									&& companyObj.getAdServerPassword() != null
									&& companyObj.getAdServerPassword().size() > 0) {
								obj.setCompanyName(companyObj.getCompanyName());
								obj.setCompanyId(companyObj.getId() + "");
								break;
							}
						}
					}
				}
				obj.setAdServerId(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);
				obj.setAdServerUsername(LinMobileConstants.LIN_MOBILE_NEW_DFP_EMAIL_ADDRESS);
				obj.setUserId(userId);
			} else {
				obj = dao.getCampaignById(unifiedCampaignDTO.getId());
				campaignPreviousCopy = new SmartCampaignObj(obj);
			}

			if (unifiedCampaignDTO.getCampaignStatus() != 0) {
				obj.setCampaignStatus(unifiedCampaignDTO.getCampaignStatus()
						+ "");
			} else {
				int campaignStatus = CampaignStatusEnum.Draft.ordinal();
				obj.setCampaignStatus(campaignStatus + "");
			}

			if (unifiedCampaignDTO != null) {

				if (unifiedCampaignDTO.getCampaignName() != null
						&& !unifiedCampaignDTO.getCampaignName().equals("")) {
					obj.setName(unifiedCampaignDTO.getCampaignName());
				} else {
					obj.setName("");
				}
			}

			if (unifiedCampaignDTO.getStartDate() != null
					&& !unifiedCampaignDTO.getStartDate().equals("")) {
				obj.setStartDate(unifiedCampaignDTO.getStartDate());
			}

			if (unifiedCampaignDTO.getEndDate() != null
					&& !unifiedCampaignDTO.getEndDate().equals("")) {
				obj.setEndDate(unifiedCampaignDTO.getEndDate());
			}

			if (unifiedCampaignDTO.getSelectedRateType() != null) {
				String id = unifiedCampaignDTO.getSelectedRateType().trim();
				DropdownDataObj rateType = dao.getRateTypeById(
						Long.parseLong(id), "Campaign Type");
				List<DropdownDataObj> list = new ArrayList<>();
				if (rateType != null && !rateType.equals("")) {
					list.add(rateType);
				}
				if (list != null && list.size() > 0) {
					obj.setRateTypeList(list);
				}
			}

			if (unifiedCampaignDTO.getNotes() != null) {
				obj.setNotes(unifiedCampaignDTO.getNotes());
			} else {
				obj.setNotes("");
			}
			// agency object
			if (unifiedCampaignDTO.getSelectedAgency() != null) {
				obj.setAgencyId(unifiedCampaignDTO.getSelectedAgency().trim());
			}
			// advertiser object
			if (unifiedCampaignDTO.getSelectedAdvertiser() != null) {
				obj.setAdvertiserId(unifiedCampaignDTO.getSelectedAdvertiser().trim());
			}
			obj.setLastUpdatedOn(lastUpdateDate);
			// obj.setDfpOrderId(169504542);
			if (unifiedCampaignDTO.getDfpOrderId() != 0) {
				obj.setDfpOrderId(unifiedCampaignDTO.getDfpOrderId());
			}
			if (unifiedCampaignDTO.getDfpOrderName() != null && unifiedCampaignDTO.getDfpOrderName().length() > 0) {
				obj.setDfpOrderName(unifiedCampaignDTO.getDfpOrderName());
			}

			// save campaign object
			if (obj != null) {
				dao.saveObjectWithStrongConsistancy(obj);
			}

			long placementId = savePlacement(unifiedCampaignDTO, obj.getCampaignId());

			if (campaignPreviousCopy != null) {
				String campaignLevelLog = getCampaignLevelHistory(
						campaignPreviousCopy, obj);
				String campaignHistoryLog = campaignLevelLog + placementLevelLog;
				// long placementId = unifiedCampaignDTO.getPlacementId();
				String placementName = unifiedCampaignDTO.getPName();
				if (placementLevelLog == null || placementLevelLog.toString().trim().length() == 0) {
					// Placement is not in Scope;
					placementId = 0;
					placementName = "";
				}
				if (campaignHistoryLog != null && campaignHistoryLog.length() > 0) {
					log.info("campaignHistoryLog : " + campaignHistoryLog);
					saveCampaignHistory(obj.getCampaignId(), campaignPreviousCopy.getName(), placementId, placementName, userId, campaignHistoryLog);
				} else {
					log.info("campaignHistoryLog is empty");
				}
			} else {
				log.info("campaignPreviousCopy is null...");
			}

		} catch (Exception e) {
			log.severe("Exception in saveCampaign method of SmartCampaignPlannerService = "
					+ e.toString());
			e.printStackTrace();
		}
		return obj.getCampaignId();

	}

	public boolean updateMediaPlanAfterCampaignEdit(UnifiedCampaignDTO unifiedCampaignDTO, SmartCampaignPlacementObj campaignPlacement) {
		boolean hasCampaignChanged = false;
		if (unifiedCampaignDTO != null) {

			hasCampaignChanged = checkTargettingParametersHasChangedInPlacement(
					unifiedCampaignDTO, campaignPlacement);

			long campaignId = unifiedCampaignDTO.getId();
			log.info("Going to check for deactivate mediaplan for campaignId - "
					+ campaignId);
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();

			IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
			try {

				if (!hasCampaignChanged) {
					log.info("Check campaign level changes ....");

					log.info("First load existing smartCampaignObject, campaignId:"
							+ campaignId);
					SmartCampaignObj smartCampaign = smartCampaignPlannerDAO
							.getCampaignById(unifiedCampaignDTO.getId());
					if (smartCampaign != null) {
						// load placements to match
						long editPlacementId = unifiedCampaignDTO
								.getPlacementId();
						String editPlacementRate = unifiedCampaignDTO.getRate();
						String editPlacementGoal = unifiedCampaignDTO.getPGoal();
						String editPlacementBudget = unifiedCampaignDTO
								.getBudget();
						String editPlacementStartDate = unifiedCampaignDTO
								.getPStartDate();
						String editPlacementEndDate = unifiedCampaignDTO
								.getPEndDate();

						if (campaignPlacement != null) {
							long pId = campaignPlacement.getPlacementId();
							if (editPlacementId == pId) {
								log.info("Placement edited--" + pId);
								String rate = campaignPlacement.getRate();
								log.info("editPlacementRate :"
										+ editPlacementRate
										+ " and existing rate:" + rate);
								if (rate != null && editPlacementRate != null
										&& !rate.equals(editPlacementRate)) {

									hasCampaignChanged = true;
								}

								String goal = campaignPlacement.getGoal();
								log.info("editPlacementGoal :"
										+ editPlacementRate
										+ " and existing goal:" + goal);
								if (editPlacementGoal != null && goal != null
										&& !editPlacementGoal.equals(goal)) {
									hasCampaignChanged = true;
								}

								String budget = campaignPlacement.getBudget();
								log.info("editPlacementBudget :"
										+ editPlacementBudget
										+ " and existing budget:" + budget);
								if (editPlacementBudget != null
										&& budget != null
										&& !editPlacementBudget.equals(budget)) {
									hasCampaignChanged = true;
								}
								String startDate = campaignPlacement
										.getStartDate();
								log.info("editPlacementStartDate :"
										+ editPlacementStartDate
										+ " and existing startDate:"
										+ startDate);
								if (editPlacementStartDate != null
										&& startDate != null
										&& !editPlacementStartDate
												.equals(startDate)) {
									hasCampaignChanged = true;
								}
								String endDate = campaignPlacement.getEndDate();
								log.info("editPlacementEndDate :"
										+ editPlacementEndDate
										+ " and existing endDate:" + endDate);
								if (editPlacementEndDate != null
										&& endDate != null
										&& !editPlacementEndDate
												.equals(endDate)) {
									hasCampaignChanged = true;
								}
							} else {
								log.info("Invalid placement matching....editPlacementId:"
										+ editPlacementId + ", new PId:" + pId);
							}
						} else {
							log.info("No placements found for this campaign -"
									+ campaignId);
						}
					} else {
						log.warning("No campaign found for campaignId:"
								+ campaignId);
					}
					log.info("Campaign level change, hasCampaignChanged :"
							+ hasCampaignChanged);

					if (hasCampaignChanged) {
						log.info("Campaign level change found, deactivate/need update mediaPlan for campaignId:"
								+ campaignId + " with active status -2");
						SmartMediaPlanObj mediaPlanObj = mediaPlanDAO
								.loadMediaPlan(campaignId + "");
						if (mediaPlanObj != null) {
							mediaPlanObj.setActive(2);
							mediaPlanDAO.saveObject(mediaPlanObj);
							log.info("MediaPlan deactivated successfully with mediaPlanId -"
									+ mediaPlanObj.getId());
						}
					}

				} else {
					log.info("Geo targetting change found, media plan will be deactivated..");
					log.info("Deactivate mediaPlan for campaignId:"
							+ campaignId + " with active status -0");
					SmartMediaPlanObj mediaPlanObj = mediaPlanDAO
							.loadMediaPlan(campaignId + "");
					if (mediaPlanObj != null) {
						mediaPlanObj.setActive(0);
						mediaPlanDAO.saveObject(mediaPlanObj);
						log.info("MediaPlan deactivated successfully with mediaPlanId -"
								+ mediaPlanObj.getId());
					}
				}

			} catch (DataServiceException e) {
				log.severe("DataServiceException :" + e.getMessage());
			}
		} else {
			log.info("Invalid campaignDTO...null");
		}

		return hasCampaignChanged;

	}

	/*
	 * This method will check whether campaign has been modified on targeting
	 * parameters
	 */
	private boolean checkTargettingParametersHasChangedInPlacement(
			UnifiedCampaignDTO unifiedCampaignDTO,
			SmartCampaignPlacementObj campaignPlacement) {
		boolean hasCampaignChanged = false;
		ISmartCampaignPlannerDAO campaignPlaneerDAO = new SmartCampaignPlannerDAO();
		log.info("Check all targetting parameters..........1) Check platform...");
		String[] platformArray = unifiedCampaignDTO.getpSelectedPlatform();
		List<PlatformObj> oldPlatformList = campaignPlacement.getPlatformObj();
		if (platformArray != null && platformArray.length > 0
				&& oldPlatformList == null) {
			hasCampaignChanged = true;
		} else if (platformArray == null && oldPlatformList != null
				&& oldPlatformList.size() > 0) {
			hasCampaignChanged = true;
		} else if ((platformArray != null && platformArray.length > 0)
				&& (oldPlatformList != null && oldPlatformList.size() > 0)) {
			if (platformArray.length != oldPlatformList.size()) {
				hasCampaignChanged = true;
			} else {
				List<String> oldPlatformArrayList = new ArrayList<String>();

				for (PlatformObj platformObj : oldPlatformList) {
					String oldPlatformId = platformObj.getId() + "";
					oldPlatformArrayList.add(oldPlatformId);
				}
				for (String platformId : platformArray) {
					if (oldPlatformArrayList.indexOf(platformId) == -1) {
						log.info("new platform found..platformId:" + platformId
								+ ", oldPlatformArrayList:"
								+ oldPlatformArrayList);
						hasCampaignChanged = true;
						break;
					}
				}
			}
		}
		log.info("1) hasCampaignChanged:" + hasCampaignChanged);
		if (!hasCampaignChanged) {
			log.info("2) Checking device...");
			String[] deviceArray = unifiedCampaignDTO.getpSelectedDevice();
			List<DeviceObj> oldDeviceList = campaignPlacement.getDeviceObj();

			if (deviceArray != null && deviceArray.length > 0
					&& oldDeviceList == null) {
				hasCampaignChanged = true;
			} else if (deviceArray == null && oldDeviceList != null
					&& oldDeviceList.size() > 0) {
				hasCampaignChanged = true;
			} else if ((deviceArray != null && deviceArray.length > 0)
					&& (oldDeviceList != null && oldDeviceList.size() > 0)) {
				if (deviceArray.length != oldDeviceList.size()) {
					hasCampaignChanged = true;
				} else {
					List<String> oldDataList = new ArrayList<String>();

					for (DeviceObj deviceObj : oldDeviceList) {
						String oldDeviceId = deviceObj.getId() + "";
						oldDataList.add(oldDeviceId);
					}
					for (String deviceId : deviceArray) {

						if (oldDataList.indexOf(deviceId) == -1) {
							log.info("new device found..deviceId:" + deviceId
									+ ", oldDataList:" + oldDataList);
							hasCampaignChanged = true;
							break;
						}
					}
				}
			}
			log.info("2) hasCampaignChanged:" + hasCampaignChanged);

		}

		try {
			if (!hasCampaignChanged) {
				log.info("3) Check geoTargetting...Check DMAs ");
				String[] dmaArray = unifiedCampaignDTO.getpSelectedDMA();
				List<GeoTargetsObj> oldDMAList = campaignPlacement.getGeoObj();
				if (dmaArray == null && oldDMAList != null
						&& oldDMAList.size() > 0) {
					hasCampaignChanged = true;
				} else if (dmaArray != null && dmaArray.length > 0
						&& oldDMAList == null) {
					hasCampaignChanged = true;
				} else if ((dmaArray != null && dmaArray.length > 0)
						&& (oldDMAList != null && oldDMAList.size() > 0)) {
					if (dmaArray.length != oldDMAList.size()) {
						hasCampaignChanged = true;
					} else {
						List<String> geoTargetNameList = new ArrayList<String>();
						for (GeoTargetsObj geo : oldDMAList) {
							geoTargetNameList.add(geo.getGeoTargetsName());
						}
						for (int i = 0; i < dmaArray.length; i++) {
							GeoTargetsObj targetsObj = new GeoTargetsObj();
							targetsObj = campaignPlaneerDAO
									.getGeoTargetById(dmaArray[i].trim());
							if (targetsObj != null) {
								if (!geoTargetNameList.contains(targetsObj
										.getGeoTargetsName())) {
									hasCampaignChanged = true;
									log.info("dma change found...");
									break;
								}

							}
						}
					}
				}
				log.info("3) Check geoTargetting...Check DMAs --hasCampaignChanged :"
						+ hasCampaignChanged);
				if (!hasCampaignChanged) {
					log.info("3) Check geoTargetting...Check State");
					String[] stateArray = unifiedCampaignDTO
							.getpSelectedState();
					List<StateObj> oldStateList = campaignPlacement
							.getStateObj();
					if (stateArray == null && oldStateList != null
							&& oldStateList.size() > 0) {
						hasCampaignChanged = true;
					} else if (stateArray != null && stateArray.length > 0
							&& oldStateList == null) {
						hasCampaignChanged = true;
					} else if ((stateArray != null && stateArray.length > 0)
							&& (oldStateList != null && oldStateList.size() > 0)) {
						if (stateArray.length != oldStateList.size()) {
							hasCampaignChanged = true;
						} else {
							List<String> stateList = new ArrayList<String>();
							for (StateObj obj : oldStateList) {
								stateList.add(obj.getText());
							}
							long countryId = 2840; // US
							for (int i = 0; i < stateArray.length; i++) {
								long stateId = StringUtil
										.getLongValue(stateArray[i].trim());
								StateObj obj = campaignPlaneerDAO.getStateById(
										stateId, countryId);
								if (obj != null) {
									if (!stateList.contains(obj.getText())) {
										hasCampaignChanged = true;
										log.info("state change found...");
										break;
									}
								}
							}
						}
					}
					log.info("3) Check geoTargetting...Check State --hasCampaignChanged :"
							+ hasCampaignChanged);
				}

				if (!hasCampaignChanged) {
					log.info("3) Check geoTargetting...Check city");
					List<CityDTO> oldCityList = campaignPlacement.getCityObj();
					String jsonArrayStr = unifiedCampaignDTO.getSelectedCity();
					log.info("city jsonArrayStr :" + jsonArrayStr);

					JSONArray cityJsonArray = (JSONArray) JSONSerializer
							.toJSON(jsonArrayStr);

					if (cityJsonArray == null && oldCityList != null
							&& oldCityList.size() > 0) {
						log.info("oldCityList :" + oldCityList.size()
								+ " and cityJsonArray: null");
						hasCampaignChanged = true;
					} else if (cityJsonArray != null
							&& cityJsonArray.size() > 0 && oldCityList == null) {
						log.info("cityJsonArray : " + cityJsonArray.size()
								+ " and oldCityList : null");
						hasCampaignChanged = true;
					} else if ((cityJsonArray != null && cityJsonArray.size() > 0)
							&& (oldCityList != null && oldCityList.size() > 0)) {
						log.info("cityJsonArray : " + cityJsonArray.size()
								+ " and oldCityList :" + oldCityList.size());
						if (cityJsonArray.size() != oldCityList.size()) {
							hasCampaignChanged = true;
						} else {
							List<String> dataList = new ArrayList<String>();
							for (CityDTO obj : oldCityList) {
								dataList.add(obj.getText());
							}
							for (int i = 0; i < cityJsonArray.size(); i++) {
								JSONObject jsonObject = cityJsonArray
										.getJSONObject(i);
								String cityName = jsonObject.getString("name");
								/*
								 * CityDTO cityDTO = new CityDTO();
								 * cityDTO.setId(jsonObject.getLong("id"));
								 * cityDTO
								 * .setText(jsonObject.getString("name"));
								 * cityDTO
								 * .setStateId(jsonObject.getLong("stateId"));
								 */
								if (cityName != null
										&& !dataList.contains(cityName)) {
									hasCampaignChanged = true;
									log.info("city change found...");
									break;
								}
							}
						}
					}
					log.info("3) Check geoTargetting...Check city --hasCampaignChanged :"
							+ hasCampaignChanged);
				}

				if (!hasCampaignChanged) {
					log.info("3) Check geoTargetting...Check zip");
					List<ZipDTO> oldZipList = campaignPlacement.getZipObj();
					String jsonArrayStr = unifiedCampaignDTO.getSelectedZip();
					JSONArray zipJsonArray = (JSONArray) JSONSerializer
							.toJSON(jsonArrayStr);

					if (zipJsonArray == null && oldZipList != null
							&& oldZipList.size() > 0) {
						hasCampaignChanged = true;
					} else if (zipJsonArray != null && zipJsonArray.size() > 0
							&& oldZipList == null) {
						hasCampaignChanged = true;
					} else if ((zipJsonArray != null && zipJsonArray.size() > 0)
							&& (oldZipList != null && oldZipList.size() > 0)) {
						if (zipJsonArray.size() != oldZipList.size()) {
							hasCampaignChanged = true;
						} else {
							List<String> dataList = new ArrayList<String>();
							for (ZipDTO obj : oldZipList) {
								dataList.add(obj.getText());
							}
							for (int i = 0; i < zipJsonArray.size(); i++) {
								JSONObject jsonObject = zipJsonArray
										.getJSONObject(i);
								String name = jsonObject.getString("name");
								if (name != null && !dataList.contains(name)) {
									hasCampaignChanged = true;
									log.info("zip change found...");
									break;
								}
							}
						}
					}
					log.info("3) Check geoTargetting...Check zip --hasCampaignChanged :"
							+ hasCampaignChanged);
				}
			}
		} catch (Exception e) {
			log.severe("Exception :" + e.getMessage());
		}
		log.info("End of geo checking---hasCampaignChanged:"
				+ hasCampaignChanged);
		return hasCampaignChanged;
	}

	public long savePlacement(UnifiedCampaignDTO campaignDTO, long campaignId) throws Exception {
		log.info("In savePlacement method of SmartCampaignPlannerService... ");
		SmartCampaignPlacementObj earlierPlacementCopy = null;

		long placementId = 0;
		boolean hasCampaignChanged = false;

		try {
			ISmartCampaignPlannerDAO campaignPlaneerDAO = new SmartCampaignPlannerDAO();
			IProductDAO productDAO = new ProductDAO();
			List<SmartCampaignFlightDTO> flightObjList = new ArrayList<>();
			SmartCampaignPlacementObj placementObj = new SmartCampaignPlacementObj();
			List<SmartCampaignPlacementObj> placementObjList = new ArrayList<>();

			boolean isDuplecatePlacement = false;
			long placemetMaxId = 0L;
			placementObj.setItemType(campaignDTO.getItemType());
			placementObj.setSelectedPlacementProducts(campaignDTO.getSelectedPlacementProducts());
			placementObj.setDeviceCapability(campaignDTO.getDeviceCapability());
			if (campaignDTO.getStatusId() == null
					|| campaignDTO.getStatusId().equals("")
					&& !campaignDTO.getStatusId().equals("edit")) {

				placemetMaxId = campaignPlaneerDAO.maxPlacementId();

				placemetMaxId = placemetMaxId + 1;
				placementObjList = campaignPlaneerDAO
						.getAllPlacementOfCampaign(campaignId);
				if (placementObjList != null && placementObjList.size() > 0) {
					if (campaignDTO.getPName() != null
							&& campaignDTO.getPName().length() > 0) {

						for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {

							if (smartCampaignPlacementObj != null && smartCampaignPlacementObj.getPlacementName() != null && smartCampaignPlacementObj.getPlacementName().length() > 0 && smartCampaignPlacementObj.getPlacementName().trim().equalsIgnoreCase(campaignDTO.getPName().trim())) {
								isDuplecatePlacement = true;
								return 0;
							}
						}
					}
				}

			} else if (campaignDTO.getStatusId() != null
					&& !campaignDTO.getStatusId().equals("")
					&& campaignDTO.getPlacementStatus() != null
					&& campaignDTO.getPlacementStatus().equals("create")) {

				placemetMaxId = campaignPlaneerDAO.maxPlacementId();
				placemetMaxId = placemetMaxId + 1;

				placementObjList = campaignPlaneerDAO
						.getAllPlacementOfCampaign(campaignId);
				if (placementObjList != null && placementObjList.size() > 0) {
					if (campaignDTO.getPName() != null
							&& campaignDTO.getPName().length() > 0) {
						for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
							if (smartCampaignPlacementObj != null
									&& smartCampaignPlacementObj
											.getPlacementName() != null
									&& smartCampaignPlacementObj
											.getPlacementName().length() > 0
									&& smartCampaignPlacementObj
											.getPlacementName()
											.trim()
											.equalsIgnoreCase(
													campaignDTO.getPName()
															.trim())) {
								isDuplecatePlacement = true;
								return 0;
							}
						}
					}
				}
				if (campaignDTO.isHasMediaPlan()
						&& (!campaignDTO.isHasMigrated())) {
					log.info("You are adding new placement during edit, deactivate old smartmedia plan..");
					hasCampaignChanged = true;
				}
			} else {
				placemetMaxId = campaignDTO.getPlacementId();
				placementObj = campaignPlaneerDAO.getPlacementById(campaignDTO
						.getPlacementId());

				earlierPlacementCopy = new SmartCampaignPlacementObj(
						placementObj);

				if (!hasCampaignChanged && (!campaignDTO.isHasMigrated())) {
					log.info("Edit Existing Placements -- Check media plan need to deactivate or not..");
					hasCampaignChanged = updateMediaPlanAfterCampaignEdit(
							campaignDTO, placementObj);
				}
			}

			if (hasCampaignChanged) {
				log.info("Update SmartCampaign with hasMediaPlan flag as false..");
				SmartCampaignObj campaignObj = campaignPlaneerDAO
						.getCampaignById(campaignDTO.getId());
				campaignObj.setHasMediaPlan(false);
				campaignPlaneerDAO.saveObjectWithStrongConsistancy(campaignObj);
				log.info("Updated campaignObj successfully.");
				campaignDTO.setHasMediaPlan(false);
			}

			if (!isDuplecatePlacement) {

				Key<SmartCampaignObj> campaignKey = Key.create(
						SmartCampaignObj.class, campaignId);

				if (campaignDTO != null) {
					if (campaignKey != null) {
						placementObj.setCampaign(campaignKey);
					}
					placementObj.setId(placemetMaxId);

					placementObj.setPlacementId(placemetMaxId);


					if (campaignDTO.getPName() != null) {
						placementObj.setPlacementName(campaignDTO.getPName());
					}

					if (campaignDTO.getPStartDate() != null) {
						placementObj.setStartDate(campaignDTO.getPStartDate());
					}

					if (campaignDTO.getPEndDate() != null) {
						placementObj.setEndDate(campaignDTO.getPEndDate());

					}
					if (campaignDTO.getPBudget() != null) {
						placementObj.setBudget(campaignDTO.getPBudget().replaceAll(",", ""));
					}
					String imp = "0";
					if (campaignDTO.getPImpression() != null) {
						imp = campaignDTO.getPImpression();
						imp = imp.replaceAll(",", "");
						if (imp.contains(".")) {
							imp = imp.substring(0, imp.indexOf("."));
						}
						placementObj.setImpressions(imp);
					}
					if (campaignDTO.getPGoal() != null) {
						placementObj.setGoal(campaignDTO.getPGoal().replaceAll(
								",", ""));
					}
					if (campaignDTO.getPacing() != null) {
						placementObj.setPacing(campaignDTO.getPacing()
								.replaceAll(",", ""));
					}

					if (campaignDTO.getFrequencyCap() != null) {
						placementObj.setFrequencyCap(campaignDTO
								.getFrequencyCap().replaceAll(",", ""));
					}

					if (campaignDTO.getFrequencyCapUnit() != null) {
						placementObj.setFrequencyCapUnit(campaignDTO
								.getFrequencyCapUnit());
					}
					if (campaignDTO.getRate() != null) {
						placementObj.setRate(campaignDTO.getRate());
					}

					if (campaignDTO.getPSelectedCreative() != null) {
						String[] creativeArr = campaignDTO
								.getPSelectedCreative();
						List<CreativeObj> creativeList = new ArrayList<>();
						for (int i = 0; i < creativeArr.length; i++) {
							// long creativeId =Long.parseLong(creativeArr[i]);
							CreativeObj creativeObj = new CreativeObj();
							creativeObj = campaignPlaneerDAO
									.getCreativeById(creativeArr[i].trim());
							if (creativeObj != null) {
								creativeList.add(creativeObj);
							}
							if (creativeList != null && creativeList.size() > 0) {
								placementObj.setCreativeObj(creativeList);
							}
						}
					} else {
						placementObj
								.setCreativeObj(new ArrayList<CreativeObj>());
					}

					List<DeviceObj> deviceList = new ArrayList<>();
					if (campaignDTO.getpSelectedDevice() != null && campaignDTO.getpSelectedDevice().length > 0
							//&& !(campaignDTO.getpSelectedDevice()[0].equals(ProductService.noneOptionId))						// NONE option not needed
							&& !(campaignDTO.getpSelectedDevice()[0].equals(ProductService.allOptionId))) {
						String[] deviceArr = campaignDTO.getpSelectedDevice();
						for (int i = 0; i < deviceArr.length; i++) {
							DeviceObj deviceObj = new DeviceObj();
							deviceObj = campaignPlaneerDAO
									.getDeviceById(deviceArr[i].trim());
							if (deviceObj != null) {
								deviceList.add(deviceObj);
							}
							if (deviceList != null && deviceList.size() > 0) {
								placementObj.setDeviceObj(deviceList);
							}
						}
					} else {																									// NONE option not needed
						/*if (campaignDTO.getpSelectedDevice() != null && campaignDTO.getpSelectedDevice()[0].equals(ProductService.noneOptionId)) {
							deviceList.add((DeviceObj) createAllNoneOptionObject(new DeviceObj(), false));
						} else {*/
							deviceList.add((DeviceObj) createAllNoneOptionObject(new DeviceObj(), true));
						//}
						placementObj.setDeviceObj(deviceList);
					}

					List<PlatformObj> plateformList = new ArrayList<>();
					if (campaignDTO.getpSelectedPlatform() != null && campaignDTO.getpSelectedPlatform().length > 0
							//&& !(campaignDTO.getpSelectedPlatform()[0].equals(ProductService.noneOptionId))					// NONE option not needed
							&& !(campaignDTO.getpSelectedPlatform()[0].equals(ProductService.allOptionId))) {
						String[] plateformArr = campaignDTO
								.getpSelectedPlatform();
						for (int i = 0; i < plateformArr.length; i++) {
							PlatformObj platformObj = new PlatformObj();
							platformObj = campaignPlaneerDAO
									.getPlatformById(plateformArr[i].trim());
							if (platformObj != null) {
								plateformList.add(platformObj);
							}
							if (plateformList != null
									&& plateformList.size() > 0) {
								placementObj.setPlatformObj(plateformList);
							}
						}
					} else {																									// NONE option not needed
						/*if (campaignDTO.getpSelectedPlatform() != null && campaignDTO.getpSelectedPlatform()[0].equals(ProductService.noneOptionId)) {
							plateformList.add((PlatformObj) createAllNoneOptionObject(new PlatformObj(), false));
						} else {*/
							plateformList.add((PlatformObj) createAllNoneOptionObject(new PlatformObj(), true));
						//}
						placementObj.setPlatformObj(plateformList);
					}

					List<IABContextObj> contextList = new ArrayList<>();
					if (campaignDTO.getpSelectedContext() != null
							&& campaignDTO.getpSelectedContext().length > 0
							&& !(campaignDTO.getpSelectedContext()[0]
									.equals(ProductService.noneOptionId))
							&& !(campaignDTO.getpSelectedContext()[0]
									.equals(ProductService.allOptionId))) {
						String[] contextArr = campaignDTO.getpSelectedContext();
						for (int i = 0; i < contextArr.length; i++) {
							long contextId = Long.parseLong(contextArr[i]
									.trim());
							IABContextObj contextObj = new IABContextObj();
							contextObj = campaignPlaneerDAO
									.getIABContextById(contextId);
							if (contextObj != null) {
								contextList.add(contextObj);
							}
							if (contextList != null && contextList.size() > 0) {
								placementObj.setContextObj(contextList);
							}
						}
					} else {
						if (campaignDTO.getpSelectedContext() != null
								&& campaignDTO.getpSelectedContext()[0]
										.equals(ProductService.noneOptionId)) {
							contextList
									.add((IABContextObj) createAllNoneOptionObject(
											new IABContextObj(), false));
						} else {
							contextList
									.add((IABContextObj) createAllNoneOptionObject(
											new IABContextObj(), true));
						}
						placementObj.setContextObj(contextList);
					}

					// Geographic Targeting Starts
					List<CountryObj> countryList = new ArrayList<>();
					List<GeoTargetsObj> dmaList = new ArrayList<>();
					List<StateObj> stateList = new ArrayList<>();
					List<CityDTO> cityDTOList = new ArrayList<>();
					List<ZipDTO> zipDTOList = new ArrayList<>();
					if (campaignDTO.getIsGeographic() != null
							&& campaignDTO.getIsGeographic().equals("true")) {
						if (campaignDTO.getpSelectedCountry() != null) {
							String[] countryArr = campaignDTO
									.getpSelectedCountry();
							for (int i = 0; i < countryArr.length; i++) {
								long countryId = Long.parseLong(countryArr[i]
										.trim());
								CountryObj countryObj = new CountryObj();
								countryObj = campaignPlaneerDAO
										.getCountryById(countryId);
								if (countryObj != null) {
									countryList.add(countryObj);
								}
							}
						}

						boolean isGeoAll = true;
						if ((campaignDTO.getpSelectedDMA() != null && campaignDTO
								.getpSelectedDMA().length > 0)
								|| (campaignDTO.getpSelectedState() != null && campaignDTO
										.getpSelectedState().length > 0)
								|| (campaignDTO.getSelectedCity() != null
										&& !(campaignDTO.getSelectedCity()
												.equals("")) && !(campaignDTO
											.getSelectedCity().equals("[]")))
								|| (campaignDTO.getSelectedZip() != null
										&& !(campaignDTO.getSelectedZip()
												.equals("")) && !(campaignDTO
											.getSelectedZip().equals("[]")))) {
							isGeoAll = false;
						}

						if (campaignDTO.getpSelectedDMA() != null
								&& campaignDTO.getpSelectedDMA().length > 0
								&& !(campaignDTO.getpSelectedDMA()[0]
										.equals(ProductService.noneOptionId))
								&& !(campaignDTO.getpSelectedDMA()[0]
										.equals(ProductService.allOptionId))) {
							String[] dmaArr = campaignDTO.getpSelectedDMA();
							for (int i = 0; i < dmaArr.length; i++) {
								GeoTargetsObj targetsObj = new GeoTargetsObj();
								targetsObj = campaignPlaneerDAO
										.getGeoTargetById(dmaArr[i].trim());
								if (targetsObj != null) {
									dmaList.add(targetsObj);
								}
							}
						} else if ((campaignDTO.getpSelectedDMA() != null && campaignDTO
								.getpSelectedDMA()[0]
								.equals(ProductService.noneOptionId))
								|| (campaignDTO.getpSelectedDMA() == null && !isGeoAll)) {
							dmaList.add((GeoTargetsObj) createAllNoneOptionObject(
									new GeoTargetsObj(), false));
						} else {
							dmaList.add((GeoTargetsObj) createAllNoneOptionObject(
									new GeoTargetsObj(), true));
						}

						if (campaignDTO.getpSelectedState() != null
								&& campaignDTO.getpSelectedState().length > 0
								&& !(campaignDTO.getpSelectedState()[0]
										.equals(ProductService.noneOptionId))
								&& !(campaignDTO.getpSelectedState()[0]
										.equals(ProductService.allOptionId))) {
							String[] stateArr = campaignDTO.getpSelectedState();
							long countryId = 2840;
							for (int i = 0; i < stateArr.length; i++) {
								StateObj stateObj = new StateObj();
								long stateId = Long.parseLong(stateArr[i]
										.trim());
								stateObj = campaignPlaneerDAO.getStateById(
										stateId, countryId);
								if (stateObj != null) {
									stateList.add(stateObj);
								}
							}
						} else if ((campaignDTO.getpSelectedState() != null && campaignDTO
								.getpSelectedState()[0]
								.equals(ProductService.noneOptionId))
								|| (campaignDTO.getpSelectedState() == null && !isGeoAll)) {
							stateList.add((StateObj) createAllNoneOptionObject(
									new StateObj(), false));
						} else {
							stateList.add((StateObj) createAllNoneOptionObject(
									new StateObj(), true));
						}

						boolean isCityEmpty = true;
						if (campaignDTO.getSelectedCity() != null) {
							String jsonArrayStr = campaignDTO.getSelectedCity();
							log.info("jsonArrayStr" + jsonArrayStr);
							if (jsonArrayStr != null
									&& !(jsonArrayStr.equals(""))
									&& !(jsonArrayStr.equals("[]"))) {
								log.info("inside if block" + jsonArrayStr);
								JSONArray city = (JSONArray) JSONSerializer
										.toJSON(jsonArrayStr);
								log.info("city :" + city);
								if (city.size() != 0) {
									for (int i = 0; i < city.size(); i++) {
										JSONObject jsonObject = city
												.getJSONObject(i);
										CityDTO cityDTO = null;
										if ((jsonObject.getLong("id") + "")
												.equals(ProductService.noneOptionId)) {
											cityDTOList
													.add((CityDTO) createAllNoneOptionObject(
															new CityDTO(),
															false));
											break;
										} else if ((jsonObject.getLong("id") + "")
												.equals(ProductService.allOptionId)) {
											cityDTOList
													.add((CityDTO) createAllNoneOptionObject(
															new CityDTO(), true));
											break;
										} else {
											cityDTO = new CityDTO();
											cityDTO.setId(jsonObject
													.getLong("id"));
											cityDTO.setText(jsonObject
													.getString("name"));
											cityDTO.setStateId(jsonObject
													.getLong("stateId"));
											StateObj stateObj = productDAO
													.getStateObj(cityDTO
															.getStateId(), 2840);
											if (stateObj != null) {
												cityDTO.setStateName(stateObj
														.getText());
											}
											cityDTOList.add(cityDTO);
										}
									}
									isCityEmpty = false;
								}
							} else {
								log.info("inside else block" + jsonArrayStr);
							}
						}
						if (isCityEmpty) {
							if (isGeoAll) {
								cityDTOList
										.add((CityDTO) createAllNoneOptionObject(
												new CityDTO(), true));
							} else {
								cityDTOList
										.add((CityDTO) createAllNoneOptionObject(
												new CityDTO(), false));
							}
						}

						boolean isZipEmpty = true;
						if (campaignDTO.getSelectedZip() != null) {
							String jsonArrayStr = campaignDTO.getSelectedZip();
							if (jsonArrayStr != null
									&& !(jsonArrayStr.equals(""))
									&& !(jsonArrayStr.equals("[]"))) {
								JSONArray city = (JSONArray) JSONSerializer
										.toJSON(jsonArrayStr);
								if (city.size() != 0) {
									for (int i = 0; i < city.size(); i++) {
										JSONObject obj = city.getJSONObject(i);
										ZipDTO zipDTO = null;
										if ((obj.getLong("id") + "")
												.equals(ProductService.noneOptionId)) {
											zipDTOList
													.add((ZipDTO) createAllNoneOptionObject(
															new ZipDTO(), false));
										} else if ((obj.getLong("id") + "")
												.equals(ProductService.allOptionId)) {
											zipDTOList
													.add((ZipDTO) createAllNoneOptionObject(
															new ZipDTO(), true));
										} else {
											zipDTO = new ZipDTO();
											zipDTO.setText(obj.getString("id"));
											zipDTO.setCityId(obj
													.getString("cityId"));
											zipDTO.setCityName(obj
													.getString("cityName"));
											zipDTO.setStateId(obj
													.getLong("stateId"));
											StateObj stateObj = productDAO
													.getStateObj(
															zipDTO.getStateId(),
															2840);
											if (stateObj != null) {
												zipDTO.setStateName(stateObj
														.getText());
											}
											zipDTOList.add(zipDTO);
										}
									}
									isZipEmpty = false;
								}
							} else {
								log.info("inside else block" + jsonArrayStr);
							}
						}
						if (isZipEmpty) {
							if (isGeoAll) {
								zipDTOList
										.add((ZipDTO) createAllNoneOptionObject(
												new ZipDTO(), true));
							} else {
								zipDTOList
										.add((ZipDTO) createAllNoneOptionObject(
												new ZipDTO(), false));
							}
						}
					} else {
						dmaList.add((GeoTargetsObj) createAllNoneOptionObject(
								new GeoTargetsObj(), false));
						stateList.add((StateObj) createAllNoneOptionObject(
								new StateObj(), false));
						cityDTOList.add((CityDTO) createAllNoneOptionObject(
								new CityDTO(), false));
						zipDTOList.add((ZipDTO) createAllNoneOptionObject(
								new ZipDTO(), false));
					}
					placementObj.setCountryObj(countryList);
					placementObj.setGeoObj(dmaList);
					placementObj.setStateObj(stateList);
					placementObj.setCityObj(cityDTOList);
					placementObj.setZipObj(zipDTOList);

					// DemoGraphic Targeting
					List<DropdownDataObj> educationList = new ArrayList<>();
					List<DropdownDataObj> ethnicityList = new ArrayList<>();
					if (campaignDTO.getIsDemographic() != null
							&& campaignDTO.getIsDemographic().equals("true")) {
						if (campaignDTO.getAge() != null
								&& campaignDTO.getAge().length() > 0) {
							String str = campaignDTO.getAge().trim();
							String str1 = str.replaceAll("Year", "");
							String[] strArr = str1.split("-");
							placementObj.setLowerAge(strArr[0].trim());
							placementObj.setUpperAge(strArr[1].trim());
						}
						if (campaignDTO.getIncome() != null
								&& campaignDTO.getIncome().length() > 0) {
							String str = campaignDTO.getIncome().trim();
							String str1 = str.replaceAll("\\$", "");
							String[] strArr = str1.split("-");
							placementObj.setLowerIncome(strArr[0].trim());
							placementObj.setUpperIncome(strArr[1].trim());
						}
						if (campaignDTO.getGender() != null) {
							placementObj.setGender(campaignDTO.getGender());
						}
						if (campaignDTO.getSelectedEducation() != null) {
							String[] id = campaignDTO.getSelectedEducation();
							for (int i = 0; i < id.length; i++) {
								DropdownDataObj educationObj = campaignPlaneerDAO
										.getEducationById(id[i].trim());
								if (educationObj != null) {
									educationList.add(educationObj);
								}
							}
						}

						if (campaignDTO.getSelectedEthnicity() != null) {
							String[] id = campaignDTO.getSelectedEthnicity();
							for (int i = 0; i < id.length; i++) {
								DropdownDataObj ethinicityObj = campaignPlaneerDAO
										.getEducationById(id[i].trim());
								if (ethinicityObj != null) {
									ethnicityList.add(ethinicityObj);
								}
							}
						}
						
						placementObj.setCensusAge(campaignDTO.getSelectedCensusAge());
						placementObj.setCensusEducation(campaignDTO.getSelectedCensusEducation());
						placementObj.setCensusEthnicity(campaignDTO.getSelectedCensusEthnicity());
						placementObj.setCensusIncome(campaignDTO.getSelectedCensusIncome());
						placementObj.setCensusGender(campaignDTO.getSelectedCensusGender());
						
					} else { // if false
						placementObj.setLowerAge("");
						placementObj.setUpperAge("");
						placementObj.setLowerIncome("");
						placementObj.setUpperIncome("");
						placementObj.setGender("");
						
						placementObj.setCensusAge("");
						placementObj.setCensusEducation("");
						placementObj.setCensusEthnicity("");
						placementObj.setCensusIncome("");
						placementObj.setCensusGender("");
					}
					placementObj.setEthnicityList(ethnicityList);
					placementObj.setEducationList(educationList);
					placementObj.setItemType(campaignDTO.getItemType());
					placementObj.setDeviceCapability(campaignDTO.getDeviceCapability());
					
					placementObj.setSelectedPlacementProducts(campaignDTO.getSelectedPlacementProducts());
					// flight object
					if (campaignDTO.getFlightStartdate() != null
							&& campaignDTO.getFlightEnddate() != null
							&& campaignDTO.getFlightGoal() != null) {

						String[] fStartArr = campaignDTO.getFlightStartdate();
						String[] fEndArr = campaignDTO.getFlightEnddate();
						String[] fGoalArr = campaignDTO.getFlightGoal();
						for (int i = 0; i < campaignDTO.getFlightStartdate().length; i++) {
							if (fStartArr[i] != null
									&& !(fStartArr[i].trim().equals(""))
									&& fEndArr[i] != null
									&& !(fEndArr[i].trim().equals(""))
									&& fGoalArr[i] != null
									&& !(fGoalArr[i].trim().equals(""))) {
								SmartCampaignFlightDTO flightObj = new SmartCampaignFlightDTO();
								flightObj.setFlightId(i + 1);
								if (fStartArr[i] != null
										&& !(fStartArr[i].trim().equals(""))) {
									flightObj.setStartDate(fStartArr[i].trim());
								} else {
									flightObj.setStartDate(campaignDTO
											.getPStartDate());
								}
								if (fEndArr[i] != null
										&& !(fEndArr[i].trim().equals(""))) {
									flightObj.setEndDate(fEndArr[i].trim());

								} else {
									flightObj.setEndDate(campaignDTO
											.getPEndDate());
								}
								if (fGoalArr[i] != null
										&& !(fStartArr[i].trim().equals(""))) {
									flightObj.setGoal(fGoalArr[i].trim()
											.replaceAll(",", ""));
								} else {
									flightObj.setGoal(campaignDTO
											.getPImpression());
								}
								if (fStartArr[i] != null && fEndArr[i] != null
										&& !(fStartArr[i].trim().equals(""))
										&& !(fEndArr[i].trim().equals(""))) {
									flightObj.setFlightName(fStartArr[i].trim()
											+ ":" + fEndArr[i].trim());
								} else {
									flightObj.setFlightName(campaignDTO
											.getPStartDate()
											+ ":"
											+ campaignDTO.getPEndDate());
								}
								if (flightObj != null) {
									flightObjList.add(flightObj);
								}
							}
						}
					}
					if (flightObjList.size() == 0) {
						SmartCampaignFlightDTO flightObj = new SmartCampaignFlightDTO();
						flightObj.setFlightId(1);
						if (campaignDTO.getPStartDate() != null) {
							flightObj.setStartDate(campaignDTO.getPStartDate());
						}
						if (campaignDTO.getPEndDate() != null
								&& !campaignDTO.getPEndDate().equals("")) {
							flightObj.setEndDate(campaignDTO.getPEndDate());
						}
						if (imp != null) {
							flightObj.setGoal(imp);
						}
						if (campaignDTO.getStartDate() != null
								&& campaignDTO.getEndDate() != null) {
							flightObj.setFlightName(campaignDTO.getPStartDate()
									+ ":" + campaignDTO.getPEndDate());
						}
						flightObjList.add(flightObj);
					}
					placementObj.setFlightObjList(flightObjList);

					campaignPlaneerDAO
							.saveObjectWithStrongConsistancy(placementObj);
					placementId = placementObj.getId();
					placementLevelLog.setLength(0);
					if (earlierPlacementCopy != null) {
						placementLevelLog.append(earlierPlacementCopy
								.getCompareString(placementObj));
					} else {
						placementLevelLog.append("New placement added : "
								+ placementObj.getPlacementName());
						log.info("placementLevelLog : "
								+ placementLevelLog.toString());
					}

					// boolean isFlightAdd = saveFlight(campaignDTO,
					// placementObj.getId());
				}
			}

		} catch (Exception e) {
			log.severe("DataServiceException : savePlacement : "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return placementId;
	}

	@Override
	public JSONArray loadAllCampaigns(String campaignStatus, long userId,
			boolean isSuperAdmin, int campaignPerPage, int pageNumber,
			String searchKeyword) throws Exception {
		log.info("In loadAllCampaigns method of SmartCampaignPlannerService... ");
		JSONArray jsonArray = new JSONArray();
		try {
			SmartCampaignPlannerDAO dao = new SmartCampaignPlannerDAO();
			IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
			IUserDetailsDAO userDAO = new UserDetailsDAO();
			List<SmartCampaignObj> campaignList = null;
			List<SmartCampaignObj> tempList = null;
			if (campaignStatus != null) {
				if (isSuperAdmin) {
					campaignList = dao.getSmartCampaignListSuperUser(campaignStatus);
				} else {
					List<CompanyObj> companyObjList = userDAO.getSelectedCompaniesByUserId(userId);
					if (companyObjList != null && companyObjList.size() > 0) {
							if (companyObjList.get(0) != null && companyObjList.get(0).getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])) {
								tempList = dao.getSmartCampaignList(campaignStatus,companyObjList.get(0).getId() + "");
							}else {
								log.info("Inactive company, id : "+companyObjList.get(0).getId());
							}
							campaignList = tempList;			// remove this line when account check code below is uncommented.
						/*if(tempList != null && tempList.size() > 0) {
							// check if campaign is authorised by account to user
							log.info("Campaigns count before account check : "+tempList.size());
							IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
							Map<String,String> accountDataMap = userService.getSelectedAccountsByUserId(userId, true, true);
							if(accountDataMap != null && accountDataMap.size() > 0) {
								campaignList = new ArrayList<>();
								for(SmartCampaignObj smartCampaignObj : tempList) {
									if(campaignStatus.equals("1") && !smartCampaignObj.getCampaignStatus().equals(CampaignStatusEnum.Paused.ordinal()+"")){
										String advertiserId = smartCampaignObj.getAdvertiserId();
										String agencyId = smartCampaignObj.getAgencyId();
										if((advertiserId != null && UserService.isAuthorisedAccountId(advertiserId, accountDataMap)) || 
												(agencyId != null && UserService.isAuthorisedAccountId(agencyId, accountDataMap))) {
											campaignList.add(smartCampaignObj);
										}else {
											//log.info("Campaign Account is not authorised, campaignId : "+smartCampaignObj.getCampaignId()+
												//	", advertiserId : "+advertiserId+", agencyId : "+agencyId);
										}
									}else if(!campaignStatus.equals("1")){
										String advertiserId = smartCampaignObj.getAdvertiserId();
										String agencyId = smartCampaignObj.getAgencyId();
										if((advertiserId != null && UserService.isAuthorisedAccountId(advertiserId, accountDataMap)) || 
												(agencyId != null && UserService.isAuthorisedAccountId(agencyId, accountDataMap))) {
											campaignList.add(smartCampaignObj);
										}else {
											//log.info("Campaign Account is not authorised, campaignId : "+smartCampaignObj.getCampaignId()+
												//	", advertiserId : "+advertiserId+", agencyId : "+agencyId);
										}
									}
								}
								log.info("Campaigns count after account check : "+campaignList.size());
							}else {
								log.info("No accounts for user");
							}
						}else {
							log.info("No campaign for user's company in datastore, id : "+companyObjList.get(0).getId());
						}*/
					}
				}

				if (campaignList != null && campaignList.size() > 0) {
					log.info("loaded campaignList :" + campaignList.size());
					int start = 0;
					int end = 0;

					if (searchKeyword.length() > 0) {
						end = campaignList.size() - 1;
					} else {
						start = (pageNumber - 1) * campaignPerPage;
						end = (pageNumber * campaignPerPage) - 1;
					}
					log.info("start : " + start + ", end : " + end);
					for (int i = start; i <= end; i++) {
						if (campaignList.size() > i) {
							SmartCampaignObj obj = campaignList.get(i);
							if (searchKeyword.length() == 0 || (searchKeyword.length() > 0 && obj.getName() != null && 
									(obj.getName().toLowerCase()).contains(searchKeyword))) {
								double budget = 0.0;
								long goal = 0L;
								List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();

								placementByIdList = dao.getAllPlacementOfCampaign(obj.getCampaignId());
								if (placementByIdList != null && placementByIdList.size() > 0) {

									for (SmartCampaignPlacementObj placementObj : placementByIdList) {
										if (placementObj != null && placementObj.getBudget() != null
												&& !placementObj.getBudget().equals("")
												&& placementObj.getImpressions() != null
												&& !placementObj.getImpressions().equals("")) {
											budget = budget + StringUtil.getDoubleValue(placementObj.getBudget());
											String imp = placementObj.getImpressions().trim().replaceAll(",", "");
											goal = goal + StringUtil.getLongValue(imp);
										}
									}
								}
								JSONObject jsonObject = new JSONObject();
								if (obj.getCampaignStatus() != null) {
									String cStatus = obj.getCampaignStatus();
									/*
									 * String status =
									 * LinMobileConstants.CAMPAIGN_STATUS
									 * [Integer.parseInt(cStatus)]; String[]
									 * strArr = status.split(":");
									 * if(strArr[1]!=null){
									 * jsonObject.put("status",
									 * strArr[1].trim()); }
									 */
									String status = CampaignStatusEnum.values()[Integer.parseInt(cStatus)].name();
									jsonObject.put("status", status);
								}
								if (obj.getRateTypeList() != null && obj.getRateTypeList().size() > 0) {
									List<DropdownDataObj> rateTypeList = new ArrayList<>();
									rateTypeList = obj.getRateTypeList();
									String rateType = rateTypeList.get(0).getValue();
									jsonObject.put("rateType", rateType);
								}

								if (obj.getAdvertiserId() != null) {
									long advertiserId = Long.parseLong(obj.getAdvertiserId().trim());
									if (advertiserId != 0) {
										AdvertiserObj advertisersObj = mediaPlanDAO
												.loadAdvertiser(advertiserId);
										if (advertisersObj != null
												&& advertisersObj.getName() != null) {
											jsonObject.put("advertiser",
													advertisersObj.getName());
										}
									}

								}

								jsonObject.put("id", obj.getCampaignId());
								jsonObject.put("name", obj.getName());
								String dfpOrderId = "";
								if (obj.getDfpOrderId() > 0) {
									dfpOrderId = obj.getDfpOrderId() + "";
								}
								jsonObject.put("dfpOrderId", dfpOrderId);
								jsonObject.put("dfpNetworkCode",
										obj.getAdServerId());
								jsonObject.put("impression", goal);
								jsonObject.put("date", obj.getStartDate()
										+ " - " + obj.getEndDate());

								jsonObject.put("budget", budget);
								jsonObject.put("startDate", obj.getStartDate());
								jsonObject.put("endDate", obj.getEndDate());
								if (obj.getLastUpdatedOn() != null) {
									log.info("LastUpdatedOn : "
											+ obj.getLastUpdatedOn());
									String lastUpdateOn = DateUtil
											.getFormatedDate(
													obj.getLastUpdatedOn(),
													"yyyyMMddHHmmss");
									jsonObject.put("lastUpdate", lastUpdateOn);
									log.info("time stamp : " + lastUpdateOn);
								} else {
									jsonObject.put("lastUpdate", "");
								}
								boolean hasMediaPlan = obj.isHasMediaPlan();
								boolean isProcessing = obj.isProcessing();
								boolean isSetupOnDFP = obj.isSetupOnDFP();
								boolean isMigrated = obj.isMigrated();
								jsonObject.put("hasMediaPlan", hasMediaPlan);
								jsonObject.put("isProcessing", isProcessing);
								jsonObject.put("isSetupOnDFP", isSetupOnDFP);
								jsonObject.put("orderId", obj.getDfpOrderId());
								jsonObject.put("orderName",
										obj.getDfpOrderName());
								jsonObject.put("isMigrated", isMigrated);
								String lineItemStatus = getLineItemStatus(obj
										.getCampaignId());
								// if(obj.getCampaignStatus() != null &&
								// !(obj.getCampaignStatus().equals((CampaignStatusEnum.Archive.ordinal())+""))
								// &&
								// !(obj.getCampaignStatus().equals((CampaignStatusEnum.Completed.ordinal())+"")))
								// {
								if (lineItemStatus != null) {
									jsonObject.put("lineItemStatus",
											lineItemStatus);
									jsonObject.put("lineItemStatusTitle",
											lineItemStatus.replaceAll("<br>",
													", "));
								}
								jsonObject.put("dfpStatus", obj.getDfpStatus());
								if(obj.getMediaPlanType()!=null){
									jsonObject.put("planType", obj.getMediaPlanType());
								}else{
									jsonObject.put("planType", 0);
								}
								// }
								jsonArray.add(jsonObject);
							}
						}
					}

				} else {
					log.info("There is no campaign found...");
				}
			}
		} catch (Exception e) {
			log.severe("Exception in loadAllCampaigns of SmartCampaignPlannerService : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return jsonArray;
	}

	private String getLineItemStatus(long campaignId) {
		String lineItemStatus = "";
		if (campaignId > 0) {
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			Map<String, Long> lineItemStatusCountMap = new TreeMap<>();
			try {
				List<SmartCampaignPlacementObj> smartCampaignPlacementList = smartCampaignPlannerDAO
						.getAllPlacementOfCampaign(campaignId);
				if (smartCampaignPlacementList != null
						&& smartCampaignPlacementList.size() > 0) {
					for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementList) {
						if (smartCampaignPlacementObj != null
								&& smartCampaignPlacementObj
										.getDfpLineItemList() != null
								&& smartCampaignPlacementObj
										.getDfpLineItemList().size() > 0) {
							for (DFPLineItemDTO dfpLineItemDTO : smartCampaignPlacementObj
									.getDfpLineItemList()) {
								if (dfpLineItemDTO != null
										&& dfpLineItemDTO.getLineItemId() > 0
										&& dfpLineItemDTO.getDfpStatus() != null
										&& dfpLineItemDTO.getDfpStatus().trim()
												.length() > 0) {
									String status = dfpLineItemDTO
											.getDfpStatus().trim();
									if (lineItemStatusCountMap
											.containsKey(status)) {
										long count = lineItemStatusCountMap
												.get(status);
										lineItemStatusCountMap.put(status,
												(count + 1));
									} else {
										lineItemStatusCountMap.put(status, 1L);
									}
								}
							}
						}
					}
					if (lineItemStatusCountMap.size() > 0) {
						StringBuilder stringBuilder = new StringBuilder();
						for (String status : lineItemStatusCountMap.keySet()) {
							long count = lineItemStatusCountMap.get(status);
							stringBuilder.append(status + "(" + count + ")"
									+ "<br>");
						}
						lineItemStatus = StringUtil.deleteFromLastOccurence(stringBuilder.toString(), "<br>");
					}
				}
			} catch (Exception e) {
				log.severe("Exception in getLineItemStatus of SmartCampaignPlannerService : "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
		return lineItemStatus;
	}

	@Override
	public UnifiedCampaignDTO initCampaign(UnifiedCampaignDTO campaignDTO,
			long userId) {
		log.info("In initCampaign method of SmartCampaignPlannerService... ");
		List<PlatformObj> platformObjList = new ArrayList<>();
		List<DeviceObj> deviceObjList = new ArrayList<>();
		List<GeoTargetsObj> geoTargetsObjList = new ArrayList<>();
		List<CreativeObj> creativeObjList = new ArrayList<>();
		List<StateObj> stateObjList = new ArrayList<>();
		List<IABContextObj> contextObjList = new ArrayList<>();
		List<DropdownDataObj> dataObjList = new ArrayList<>();
		List<CountryObj> countryobjList = new ArrayList<>();
		List<AdvertiserObj> advertiserList = new ArrayList<>();
		List<AgencyObj> agencyList = new ArrayList<>();
		List<CommonDTO> list = new ArrayList<>();
		IProductDAO productDAO = new ProductDAO();
		IProductService service = new ProductService();
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		try {
			// platform list
			platformObjList.add((PlatformObj) createAllNoneOptionObject(new PlatformObj(), true));
			List<PlatformObj> tempPlatformList = service.getAllPlatforms();
			if (tempPlatformList != null && tempPlatformList.size() > 0) {
				platformObjList.addAll(tempPlatformList);
			}
			//platformObjList.add((PlatformObj) createAllNoneOptionObject(new PlatformObj(), false));			// NONE option not needed
			for (PlatformObj platformObj : platformObjList) {
				if (platformObj != null) {
					list.add(new CommonDTO(String.valueOf(platformObj.getId()),
							platformObj.getText()));
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setPlateformList(list);
			} else {
				campaignDTO.setPlateformList(new ArrayList<CommonDTO>());
			}
			// device list
			deviceObjList.add((DeviceObj) createAllNoneOptionObject(new DeviceObj(), true));
			List<DeviceObj> tempDeviceList = service.getAllDevices();
			if (tempDeviceList != null && tempDeviceList.size() > 0) {
				deviceObjList.addAll(tempDeviceList);
			}
			//deviceObjList.add((DeviceObj) createAllNoneOptionObject(new DeviceObj(), false));					// NONE option not needed
			list = new ArrayList<>();
			for (DeviceObj deviceObj : deviceObjList) {
				if (deviceObj != null) {
					list.add(new CommonDTO(String.valueOf(deviceObj.getId()),
							deviceObj.getText()));
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setDeviceList(list);
			} else {
				campaignDTO.setDeviceList(new ArrayList<CommonDTO>());
			}
			// DMA list
			geoTargetsObjList.add((GeoTargetsObj) createAllNoneOptionObject(
					new GeoTargetsObj(), true));
			List<GeoTargetsObj> tempGeoList = productDAO.getAllGeoTargetsObj();
			if (tempGeoList != null && tempGeoList.size() > 0) {
				geoTargetsObjList.addAll(tempGeoList);
			}
			geoTargetsObjList.add((GeoTargetsObj) createAllNoneOptionObject(
					new GeoTargetsObj(), false));
			list = new ArrayList<>();
			for (GeoTargetsObj geoTargetObj : geoTargetsObjList) {
				if (geoTargetObj != null) {
					list.add(new CommonDTO(String.valueOf(geoTargetObj
							.getGeoTargetId()), geoTargetObj
							.getGeoTargetsName()));
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setDmaList(list);
			} else {
				campaignDTO.setDmaList(new ArrayList<CommonDTO>());
			}
			// Creative List
			creativeObjList = service.loadAllCreatives();
			list = new ArrayList<>();
			if (creativeObjList != null && creativeObjList.size() > 0) {
				for (CreativeObj creativeObj : creativeObjList) {
					if (creativeObj != null) {
						String str = "";
						str = creativeObj.getFormat() + "-"
								+ creativeObj.getSize();
						list.add(new CommonDTO(String.valueOf(creativeObj
								.getId()), str));
					}
				}
				if (list != null && list.size() > 0) {
					campaignDTO.setCreativeList(list);
				}

			}

			// state list
			stateObjList.add((StateObj) createAllNoneOptionObject(
					new StateObj(), true));
			List<StateObj> tempStateList = service.loadAllStates("US");
			if (tempStateList != null && tempStateList.size() > 0) {
				stateObjList.addAll(tempStateList);
			}
			stateObjList.add((StateObj) createAllNoneOptionObject(
					new StateObj(), false));
			list = new ArrayList<>();
			if (stateObjList != null && stateObjList.size() > 0) {
				for (StateObj stateObj : stateObjList) {
					if (stateObj != null) {
						list.add(new CommonDTO(
								String.valueOf(stateObj.getId()), stateObj
										.getText()));
					}
				}
				if (list != null && list.size() > 0) {
					campaignDTO.setStateList(list);
				}
			}
			// context list
			contextObjList = new ArrayList<>();
			contextObjList.add((IABContextObj) createAllNoneOptionObject(
					new IABContextObj(), true));
			List<IABContextObj> tempContextList = productDAO.getAllIABContext();
			if (tempContextList != null && tempContextList.size() > 0) {
				contextObjList.addAll(tempContextList);
			}
			contextObjList.add((IABContextObj) createAllNoneOptionObject(
					new IABContextObj(), false));
			list = new ArrayList<>();
			if (contextObjList != null && contextObjList.size() > 0) {
				for (IABContextObj iABContextObj : contextObjList) {
					if (iABContextObj != null) {
						if (!iABContextObj.getSubgroup().equals("")
								&& !(iABContextObj.getSubgroup()
										.equals(ProductService.allOption))
								&& !(iABContextObj.getSubgroup()
										.equals(ProductService.noneOption))) {
							list.add(new CommonDTO(String.valueOf(iABContextObj
									.getId()), iABContextObj.getGroup() + "-"
									+ iABContextObj.getSubgroup()));
						} else {
							list.add(new CommonDTO(String.valueOf(iABContextObj
									.getId()), iABContextObj.getGroup()));
						}

					}
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setContextList(list);
			}
			// country List
			countryobjList = campaignDAO.getAllCountry();
			list = new ArrayList<>();
			if (countryobjList != null && countryobjList.size() > 0) {
				for (CountryObj dataObj : countryobjList) {
					if (dataObj != null) {
						list.add(new CommonDTO(String.valueOf(dataObj.getId()),
								dataObj.getText()));
					}
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setCountryList(list);
			}
			// rate list
			dataObjList = campaignDAO.getDropDownDataList("Campaign Type");
			list = new ArrayList<>();
			if (dataObjList != null && dataObjList.size() > 0) {
				for (DropdownDataObj dataObj : dataObjList) {
					if (dataObj != null) {
						list.add(new CommonDTO(String.valueOf(dataObj
								.getObjectId()), dataObj.getValue()));
					}
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setRateTypeList(list);
			}

			// Advertiser List
			advertiserList = campaignDAO.getAllAdvertiserByAdServerId("0");
			// advertiserList =
			// campaignDAO.getAllAdvertiserByAdServerId(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);

			list = new ArrayList<>();
			list.add(new CommonDTO("0", "Add New Advertiser"));
			List<String> advertiser = new ArrayList<String>();
			if (advertiserList != null && advertiserList.size() > 0) {

				for (AdvertiserObj advertisersObj : advertiserList) {
					if (advertisersObj != null) {
						if (advertiser.contains(advertisersObj.getName())) {
							list.add(new CommonDTO(advertisersObj.getId() + "",
									advertisersObj.getName()
											+ " ("
											+ advertisersObj
													.getDfpNetworkCode() + " )"));
						} else {
							advertiser.add(advertisersObj.getName());
							list.add(new CommonDTO(advertisersObj.getId() + "",
									advertisersObj.getName()));
						}

					}
				}

			}
			if (list != null && list.size() > 0) {
				campaignDTO.setAdvertiserList(list);
			} else {
				campaignDTO.setAdvertiserList(new ArrayList<CommonDTO>());
			}

			agencyList = campaignDAO.getAllAgencyByAdServerId("0"); 
			// agencyList =
			// campaignDAO.getAllAgencyByAdServerId(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);
			list = new ArrayList<>();
			list.add(new CommonDTO("0", "Add New Agency"));
			List<String> agency = new ArrayList<String>();
			if (agencyList != null && agencyList.size() > 0) {
				for (AgencyObj agenciesObj : agencyList) {
					if (agenciesObj != null) {
						if (agency.contains(agenciesObj.getName())) {
							list.add(new CommonDTO(agenciesObj.getId() + "",
									agenciesObj.getName() + " ("
											+ agenciesObj.getDfpNetworkCode()
											+ " )"));
						} else {
							agency.add(agenciesObj.getName());
							list.add(new CommonDTO(agenciesObj.getId() + "",
									agenciesObj.getName()));
						}

					}
				}

			}
			if (list != null && list.size() > 0) {
				campaignDTO.setAgencyList(list);
			} else {
				campaignDTO.setAgencyList(new ArrayList<CommonDTO>());
			}

			// Education list
			dataObjList = campaignDAO.getDropDownDataList("Education");
			list = new ArrayList<>();
			if (dataObjList != null && dataObjList.size() > 0) {
				for (DropdownDataObj dataObj : dataObjList) {
					if (dataObj != null) {
						list.add(new CommonDTO(String.valueOf(dataObj.getId()),
								dataObj.getValue()));
					}
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setEducationList(list);
			}
			// Ethinicity list
			dataObjList = campaignDAO.getDropDownDataList("Ethinicity");
			list = new ArrayList<>();
			if (dataObjList != null && dataObjList.size() > 0) {
				for (DropdownDataObj dataObj : dataObjList) {
					if (dataObj != null) {
						list.add(new CommonDTO(String.valueOf(dataObj.getId()),
								dataObj.getValue()));
					}
				}
			}
			if (list != null && list.size() > 0) {
				campaignDTO.setEthinicityList(list);
			}
			// frequency Cap unit list
			list = new ArrayList<>();
			list.add(new CommonDTO("Hour", "Hour"));
			list.add(new CommonDTO("Day", "Day"));
			list.add(new CommonDTO("Week", "Week"));
			list.add(new CommonDTO("Month", "Month"));
			campaignDTO.setFrequencyCapList(list);
			// gender list
			list = new ArrayList<>();
			list.add(new CommonDTO("Both", ""));
			list.add(new CommonDTO("Male", "Male"));
			list.add(new CommonDTO("Female", "Female"));
			list.add(new CommonDTO("Other", "Other"));
			campaignDTO.setGenderList(list);

		} catch (Exception e) {
			log.severe("Exception in initCampaign method of SmartCampaignPlannerService"
					+ e.getMessage());
			e.printStackTrace();
		}
		return campaignDTO;
	}

	public UnifiedCampaignDTO initEditCampaign(UnifiedCampaignDTO campaignDTO,
			long campaignId, long userId) {
		log.info("In initEditCampaign method of SmartCampaignPlannerService... ");
		DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
		SmartCampaignObj campaignObj = new SmartCampaignObj();
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		NumberFormat nf = NumberFormat.getInstance();
		// long CampaignId = 0L;

		try {

			if (campaignId != 0) {
				campaignObj = campaignDAO.getCampaignById(campaignId);
				placementByIdList = campaignDAO
						.getAllPlacementOfCampaign(campaignId);

			} else {
				campaignId = campaignDTO.getId();
				if (campaignId != 0) {
					campaignObj = campaignDAO.getCampaignById(campaignId);
					placementByIdList = campaignDAO
							.getAllPlacementOfCampaign(campaignId);
				}

			}

			if (placementByIdList != null && placementByIdList.size() > 0) {
				List<SmartCampaignPlacementObj> tempList = new ArrayList<>();
				for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementByIdList) {
					if (smartCampaignPlacementObj != null) {
						String imp = smartCampaignPlacementObj.getImpressions();
						if (imp != null && imp.length() > 0) {
							imp = imp.replaceAll(",", "");
							long impression = Long.parseLong(imp.trim());
							smartCampaignPlacementObj.setImpressions(nf
									.format(impression));
						}
						String budget = smartCampaignPlacementObj.getBudget();
						if (budget != null) {
							double bud = StringUtil.getDoubleValue(budget
									.trim());
							smartCampaignPlacementObj.setBudget(bud + "");
						}
						if (smartCampaignPlacementObj.getRate() != null) {
							smartCampaignPlacementObj.setRate(df.format(Double
									.valueOf(smartCampaignPlacementObj
											.getRate().trim())));
						}
						tempList.add(smartCampaignPlacementObj);
					}

				}
				campaignDTO.setPlacementByCampaignList(tempList);
			}

			if (campaignObj != null) {
				if (campaignObj.getCampaignId() != null) {
					campaignDTO.setId(campaignObj.getCampaignId());
				}
				if (campaignObj.getName() != null) {
					campaignDTO.setCampaignName(campaignObj.getName());
				}
				if (campaignObj.getCampaignStatus() != null) {
					campaignDTO.setStatusId(campaignObj.getCampaignStatus()
							.trim());
				}
				if (campaignObj.getRateTypeList() != null
						&& campaignObj.getRateTypeList().size() > 0) {
					List<CommonDTO> list = new ArrayList<>();
					CommonDTO commonDTO = new CommonDTO(
							String.valueOf(campaignObj.getRateTypeList().get(0)
									.getObjectId()), campaignObj
									.getRateTypeList().get(0).getValue());
					if (commonDTO != null) {
						list.add(commonDTO);
						campaignDTO.setSelectedRateTypeList(list);
					}
				}

				if (campaignObj.getStartDate() != null) {
					campaignDTO.setStartDate(campaignObj.getStartDate());
				}
				if (campaignObj.getEndDate() != null) {
					campaignDTO.setEndDate(campaignObj.getEndDate());
				}
				if (campaignObj.getNotes() != null) {
					campaignDTO.setNotes(campaignObj.getNotes());
				}

				if (campaignObj.getDfpOrderId() != 0) {
					campaignDTO.setDfpOrderId(campaignObj.getDfpOrderId());
				}

				if (campaignObj.getDfpOrderName() != null
						&& campaignObj.getDfpOrderName().length() > 0) {
					campaignDTO.setDfpOrderName(campaignObj.getDfpOrderName());
				}

				if (campaignObj.getCampaignStatus() != null
						&& campaignObj.getCampaignStatus().length() > 0) {
					campaignDTO.setCampaignStatus(StringUtil.getIntegerValue(
							campaignObj.getCampaignStatus(), 0));
				}

				// campaignDTO.setMediaPlanEditStatus(campaignObj.getMediaPlanEditStatus());
				// campaignDTO.setMediaPlanStatus(campaignObj.getMediaPlanStatus());

				if (campaignObj.getAdvertiserId() != null) {
					List<CommonDTO> list = new ArrayList<>();
					long advertiserId = Long.parseLong(campaignObj
							.getAdvertiserId().trim());
					if (advertiserId != 0) {
						AdvertiserObj advertisersObj = mediaPlanDAO
								.loadAdvertiser(advertiserId);
						if (advertisersObj != null) {
							CommonDTO commonDTO = new CommonDTO(
									campaignObj.getAdvertiserId(),
									advertisersObj.getName());
							if (commonDTO != null) {
								list.add(commonDTO);
								campaignDTO.setSelectedAdvertiserList(list);
							}
						}
					}

				}

				if (campaignObj.getAgencyId() != null) {
					List<CommonDTO> list = new ArrayList<>();
					long agencyId = Long.parseLong(campaignObj.getAgencyId()
							.trim());
					if (agencyId != 0) {
						AgencyObj agenciesObj = mediaPlanDAO
								.loadAgency(agencyId);
						if (agenciesObj != null) {
							CommonDTO commonDTO = new CommonDTO(
									campaignObj.getAgencyId(),
									agenciesObj.getName());
							if (commonDTO != null) {
								list.add(commonDTO);
								campaignDTO.setSelectedAgencyList(list);
							}
						}
					}

				}
				campaignDTO.setHasMediaPlan(campaignObj.isHasMediaPlan());
				campaignDTO.setHasProcessing(campaignObj.isProcessing());
				campaignDTO.setHasSetupOnDFP(campaignObj.isSetupOnDFP());
				campaignDTO.setHasMigrated(campaignObj.isMigrated());
			}
			campaignDTO = initCampaign(campaignDTO, userId);
		} catch (Exception e) {
			log.severe("Exception in initEditCampaign method of SmartCampaignPlannerService"
					+ e.getMessage());
			e.printStackTrace();
		}
		return campaignDTO;

	}

	@Override
	public UnifiedCampaignDTO initEditPlacement(String campaignId, String placementId, long userId) {
		log.info("In initEditPlacement method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		SmartCampaignPlacementObj placementObj = new SmartCampaignPlacementObj();
		List<SmartCampaignPlacementObj> placementByIdList = new ArrayList<>();
		UnifiedCampaignDTO campaignDTO = new UnifiedCampaignDTO();
		NumberFormat nf = NumberFormat.getInstance();
		try {
			if (placementId != null && !placementId.equals("")) {
				placementByIdList = campaignDAO.getPlacementObjList(Long
						.parseLong(placementId));
				campaignDTO.setPlacementId(Long.parseLong(placementId));
				campaignDTO.setStatusId("edit");
			}

			if (placementByIdList != null && placementByIdList.size() > 0) {
				placementObj = placementByIdList.get(0);
			}
			if (placementObj != null) {
				campaignDTO.setItemType(placementObj.getItemType());
				campaignDTO.setDeviceCapability(placementObj.getDeviceCapability());
				campaignDTO.setSelectedPlacementProducts(placementObj.getSelectedPlacementProducts());
				if (placementObj.getPlacementName() != null) {
					campaignDTO.setPName(placementObj.getPlacementName());
				}
				if (placementObj.getBudget() != null) {
					double budget = com.lin.web.util.StringUtil
							.getDoubleValue(placementObj.getBudget());
					campaignDTO.setPBudget(budget + "");
				}
				if (placementObj.getStartDate() != null) {
					campaignDTO.setPStartDate(placementObj.getStartDate());
				}
				if (placementObj.getEndDate() != null) {
					campaignDTO.setPEndDate(placementObj.getEndDate());
				}
				if (placementObj.getImpressions() != null
						&& placementObj.getImpressions().length() > 0) {
					String imp = placementObj.getImpressions().trim()
							.replace(",", "");
					long impression = Long.parseLong(imp);
					campaignDTO.setPImpression(nf.format(impression));
				}
				if (placementObj.getRate() != null) {
					campaignDTO.setRate(placementObj.getRate());
				}
				if (placementObj.getGoal() != null
						&& placementObj.getGoal().length() > 0) {
					double goal = StringUtil.getDoubleValue(placementObj
							.getGoal().trim());
					campaignDTO.setPGoal(goal + "");
				}
				if (placementObj.getFrequencyCap() != null
						&& placementObj.getFrequencyCap().length() > 0) {
					long freCap = Long.parseLong(placementObj.getFrequencyCap()
							.trim());
					campaignDTO.setFrequencyCap(nf.format(freCap));
				}
				if (placementObj.getFrequencyCapUnit() != null
						&& placementObj.getFrequencyCapUnit().length() > 0) {
					campaignDTO.setFrequencyCapUnit(placementObj
							.getFrequencyCapUnit());
				}

				if (placementObj.getPacing() != null
						&& placementObj.getPacing().length() > 0) {
					long pacing = Long.parseLong(placementObj.getPacing()
							.trim());
					campaignDTO.setPacing(nf.format(pacing));
				}
				if (placementObj.getCreativeObj() != null && placementObj.getCreativeObj().size() > 0) {
					List<CommonDTO> list = new ArrayList<>();
					for (CreativeObj obj : placementObj.getCreativeObj()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getFormat() + "-"
									+ obj.getSize());
							list.add(dto);
						}
					}
					campaignDTO.setSelectedCreativePlacementList(list);
				}

				// platform
				List<CommonDTO> platformList = new ArrayList<>();
				if (placementObj.getPlatformObj() != null && placementObj.getPlatformObj().size() > 0) {
					for (PlatformObj obj : placementObj.getPlatformObj()) {
						if (obj != null && (obj.getId()+"").equals(ProductService.noneOptionId)) {
							platformList = new ArrayList<>();
							platformList.add(new CommonDTO(ProductService.allOptionId,ProductService.allOption));					// NONE option not needed
							break;
						}else if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj.getId()), obj.getText());
							platformList.add(dto);
						}
					}
				} else {
					platformList.add(new CommonDTO(ProductService.allOptionId,ProductService.allOption));
				}
				campaignDTO.setSelectedPlatformPlacementList(platformList);

				// device
				List<CommonDTO> deviceList = new ArrayList<>();
				if (placementObj.getDeviceObj() != null && placementObj.getDeviceObj().size() > 0) {
					for (DeviceObj obj : placementObj.getDeviceObj()) {
						if (obj != null && (obj.getId()+"").equals(ProductService.noneOptionId)) {
							deviceList = new ArrayList<>();
							deviceList.add(new CommonDTO(ProductService.allOptionId,ProductService.allOption));						// NONE option not needed
							break;
						}else if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj.getId()), obj.getText());
							deviceList.add(dto);
						}
					}
				}else {
					deviceList.add(new CommonDTO(ProductService.allOptionId,ProductService.allOption));
				}
				campaignDTO.setSelectedDevicePlacementList(deviceList);

				// context
				List<CommonDTO> contextList = new ArrayList<>();
				if (placementObj.getContextObj() != null
						&& placementObj.getContextObj().size() > 0) {
					for (IABContextObj obj : placementObj.getContextObj()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getGroup() + "-"
									+ obj.getSubgroup());
							contextList.add(dto);
						}
					}
				} else {
					contextList.add(new CommonDTO(ProductService.allOptionId,
							ProductService.allOption));
				}
				campaignDTO.setSelectedContextPlacementList(contextList);

				if (placementObj.getFrequencyCapUnit() != null) {
					List<CommonDTO> list = new ArrayList<>();
					CommonDTO dto = new CommonDTO(
							placementObj.getFrequencyCapUnit(),
							placementObj.getFrequencyCapUnit());
					list.add(dto);
					campaignDTO.setSelectedFrequencyUnitList(list);
				}

				// Demographic Targeting
				if (placementObj.getUpperAge() != null
						&& placementObj.getUpperAge().length() > 0) {
					campaignDTO.setUpperAge(placementObj.getUpperAge().trim());
				}
				if (placementObj.getLowerAge() != null
						&& placementObj.getLowerAge().length() > 0) {
					campaignDTO.setLowerAge(placementObj.getLowerAge().trim());
				}
				if (placementObj.getUpperIncome() != null
						&& placementObj.getUpperIncome().length() > 0) {
					campaignDTO.setUpperIncome(placementObj.getUpperIncome()
							.trim());
				}
				if (placementObj.getLowerIncome() != null
						&& placementObj.getLowerIncome().length() > 0) {
					campaignDTO.setLowerIncome(placementObj.getLowerIncome()
							.trim());
				}
/*
				if ((placementObj.getEducationList() == null || placementObj
						.getEducationList().size() == 0)
						&& (placementObj.getEthnicityList() == null || placementObj
								.getEthnicityList().size() == 0)
						&& (placementObj.getGender() == null || placementObj
								.getGender().trim().length() == 0)
						&& (placementObj.getUpperAge() == null || placementObj
								.getUpperAge().trim().length() == 0)
						&& (placementObj.getLowerAge() == null || placementObj
								.getLowerAge().trim().length() == 0)
						&& (placementObj.getUpperIncome() == null || placementObj
								.getUpperIncome().trim().length() == 0)
						&& (placementObj.getLowerIncome() == null || placementObj
								.getLowerIncome().trim().length() == 0)) {
					campaignDTO.setIsDemographic("false");
				} else {
					campaignDTO.setIsDemographic("true");
				}
*/
				if (placementObj.getEducationList() != null
						&& placementObj.getEducationList().size() > 0) {
					List<CommonDTO> list = new ArrayList<>();
					for (DropdownDataObj obj : placementObj.getEducationList()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getValue());
							list.add(dto);
						}
					}
					campaignDTO.setSelectedEducationList(list);
				}
				if (placementObj.getEthnicityList() != null
						&& placementObj.getEthnicityList().size() > 0) {
					List<CommonDTO> list = new ArrayList<>();
					for (DropdownDataObj obj : placementObj.getEthnicityList()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getValue());
							list.add(dto);
						}
					}
					campaignDTO.setSelectedEthinicityList(list);
				}

				if (placementObj.getGender() != null) {
					List<CommonDTO> list = new ArrayList<>();
					CommonDTO dto = new CommonDTO(placementObj.getGender(),
							placementObj.getGender());
					list.add(dto);
					campaignDTO.setSelectedGender(list);
				}
				// geographic targeting

				boolean isGeoAll = true;
				if ((placementObj.getGeoObj() != null && placementObj
						.getGeoObj().size() > 0)
						|| (placementObj.getStateObj() != null && placementObj
								.getStateObj().size() > 0)
						|| (placementObj.getCityObj() != null && placementObj
								.getCityObj().size() > 0)
						|| (placementObj.getZipObj() != null && placementObj
								.getZipObj().size() > 0)) {
					isGeoAll = false;
				}

				if ((placementObj.getGeoObj() == null
						|| placementObj.getGeoObj().size() == 0 || (placementObj
						.getGeoObj().get(0).getGeoTargetId() + "")
						.equals(ProductService.noneOptionId))
						&& (placementObj.getStateObj() == null
								|| placementObj.getStateObj().size() == 0 || (placementObj
								.getStateObj().get(0).getId() + "")
								.equals(ProductService.noneOptionId))
						&& (placementObj.getCityObj() == null
								|| placementObj.getCityObj().size() == 0 || (placementObj
								.getCityObj().get(0).getId() + "")
								.equals(ProductService.noneOptionId))
						&& (placementObj.getZipObj() == null
								|| placementObj.getZipObj().size() == 0 || (placementObj
								.getZipObj().get(0).getText() + "")
								.equals(ProductService.noneOptionId))
						&& (placementObj.getCountryObj() == null || placementObj
								.getCountryObj().size() == 0)) {
					campaignDTO.setIsGeographic("false");
				} else {
					campaignDTO.setIsGeographic("true");
				}

				if (placementObj.getCountryObj() != null
						&& placementObj.getCountryObj().size() > 0) {
					List<CommonDTO> list = new ArrayList<>();
					for (CountryObj obj : placementObj.getCountryObj()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getText());
							list.add(dto);
						}
					}
					campaignDTO.setSelectedCountryPlatcementList(list);
				}

				// dma
				List<CommonDTO> dmaList = new ArrayList<>();
				if (placementObj.getGeoObj() != null
						&& placementObj.getGeoObj().size() > 0) {
					for (GeoTargetsObj obj : placementObj.getGeoObj()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getGeoTargetId()), obj.getGeoTargetsName());
							dmaList.add(dto);
						}
					}
				} else if (isGeoAll) {
					dmaList.add(new CommonDTO(ProductService.allOptionId,
							ProductService.allOption));
				} else {
					dmaList.add(new CommonDTO(ProductService.noneOptionId,
							ProductService.noneOption));
				}
				campaignDTO.setSelectedDMAPlacementList(dmaList);

				// state
				List<CommonDTO> stateList = new ArrayList<>();
				if (placementObj.getStateObj() != null
						&& placementObj.getStateObj().size() > 0) {
					for (StateObj obj : placementObj.getStateObj()) {
						if (obj != null) {
							CommonDTO dto = new CommonDTO(String.valueOf(obj
									.getId()), obj.getText());
							stateList.add(dto);
						}
					}
				} else if (isGeoAll) {
					stateList.add(new CommonDTO(ProductService.allOptionId,
							ProductService.allOption));
				} else {
					stateList.add(new CommonDTO(ProductService.noneOptionId,
							ProductService.noneOption));
				}
				campaignDTO.setSelectedStatePlacementList(stateList);

				// create city objects array
				JSONArray cityJsonArray = new JSONArray();
				List<CityDTO> cityDTOList = placementObj.getCityObj();
				if (cityDTOList != null && cityDTOList.size() > 0) {
					for (CityDTO obj : cityDTOList) {
						JSONObject cityJsonObj = new JSONObject();
						cityJsonObj.put("id", obj.getId());
						cityJsonObj.put("name", obj.getText());
						cityJsonObj.put("stateId", obj.getStateId());
						cityJsonArray.add(cityJsonObj);
					}
				} else if (isGeoAll) {
					cityJsonArray.add(ProductService
							.createAllNoneOptionJsonObject(new String[] { "id",
									"name", "stateId" }, true));
				} else {
					cityJsonArray.add(ProductService
							.createAllNoneOptionJsonObject(new String[] { "id",
									"name", "stateId" }, false));
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("cityList", cityJsonArray);
				campaignDTO.setCityJSON(jsonObject.toString());

				// create zip objects array
				JSONArray zipJsonArray = new JSONArray();
				List<ZipDTO> zipDTOList = placementObj.getZipObj();
				if (zipDTOList != null && zipDTOList.size() > 0) {
					for (ZipDTO obj : zipDTOList) {
						JSONObject zipJsonObj = new JSONObject();
						zipJsonObj.put("id", obj.getText());
						zipJsonObj.put("name",
								obj.getText() + " - " + obj.getCityName());
						if (obj.getText().equals(ProductService.allOptionId)
								|| obj.getText().equals(
										ProductService.noneOptionId)) {
							zipJsonObj.put("name", obj.getCityName());
						}
						zipJsonObj.put("cityId", obj.getCityId());
						zipJsonObj.put("cityName", obj.getCityName());
						zipJsonObj.put("stateId", obj.getStateId());
						zipJsonArray.add(zipJsonObj);
					}
				} else if (isGeoAll) {
					zipJsonArray.add(ProductService
							.createAllNoneOptionJsonObject(new String[] { "id",
									"name", "cityId", "cityName", "stateId" },
									true));
				} else {
					zipJsonArray.add(ProductService
							.createAllNoneOptionJsonObject(new String[] { "id",
									"name", "cityId", "cityName", "stateId" },
									false));
				}
				jsonObject = new JSONObject();
				jsonObject.put("zipList", zipJsonArray);
				campaignDTO.setZipJSON(jsonObject.toString());

				campaignDTO = initEditCampaign(campaignDTO,
						Long.parseLong(campaignId), userId);

				List<SmartCampaignFlightDTO> flightObjList = placementObj
						.getFlightObjList();
				if (flightObjList == null || flightObjList.size() == 0) {
					flightObjList = new ArrayList<>();
					SmartCampaignFlightDTO flightObj = new SmartCampaignFlightDTO();
					flightObj.setFlightId(1);

					if (campaignDTO.getPStartDate() != null) {
						flightObj.setStartDate(campaignDTO.getPStartDate());
					}

					if (campaignDTO.getPEndDate() != null
							&& !campaignDTO.getPEndDate().equals("")) {
						flightObj.setEndDate(campaignDTO.getPEndDate());
					}
					if (campaignDTO.getPImpression() != null) {
						flightObj.setGoal(campaignDTO.getPImpression());
					}

					if (campaignDTO.getStartDate() != null
							&& campaignDTO.getEndDate() != null) {
						flightObj.setFlightName(campaignDTO.getPStartDate()
								+ ":" + campaignDTO.getPEndDate());
					}
					flightObjList.add(flightObj);
				}
				campaignDTO.setFlightList(flightObjList);
				
				
				//Added by Anup : Set Census Property
				campaignDTO.setSelectedCensusAge(placementObj.getCensusAge());
				campaignDTO.setSelectedCensusEducation(placementObj.getCensusEducation());
				campaignDTO.setSelectedCensusEthnicity(placementObj.getCensusEthnicity());
				campaignDTO.setSelectedCensusGender(placementObj.getCensusGender());
				campaignDTO.setSelectedCensusIncome(placementObj.getCensusIncome());
				
				
				if ((placementObj.getCensusAge() == null || placementObj
						.getCensusAge().length() == 0)
						&& (placementObj.getCensusEducation() == null || placementObj
								.getCensusEducation().trim().length() == 0)
						&& (placementObj.getCensusEthnicity() == null || placementObj
								.getCensusEthnicity().trim().length() == 0)
						&& (placementObj.getCensusGender() == null || placementObj
								.getCensusGender().trim().length() == 0)
						&& (placementObj.getCensusIncome() == null || placementObj
								.getCensusIncome().trim().length() == 0)) {
					campaignDTO.setIsDemographic("false");
				} else {
					campaignDTO.setIsDemographic("true");
				}
				
				campaignDTO.setSelectedPlacementProducts(placementObj.getSelectedPlacementProducts());
				
			}
		} catch (Exception e) {
			log.severe("Exception in initEditPlacement method of SmartCampaignPlannerService"
					+ e.getMessage());
			e.printStackTrace();
		}
		return campaignDTO;

	}

	@Override
	public UnifiedCampaignDTO initEditFlight(UnifiedCampaignDTO campaignDTO,
			long placementId) {
		log.info("In initEditFlight method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignFlightObj> flightList = new ArrayList<>();
		try {
			flightList = campaignDAO.getAllFlightsOfPlacement(placementId);
			if (flightList != null && flightList.size() > 0) {
				campaignDTO.setFlightByPlacementList(flightList);
				int i = 0;
				String[] startArr = new String[flightList.size()];
				String[] endArr = new String[flightList.size()];
				String[] goalArr = new String[flightList.size()];
				for (SmartCampaignFlightObj smartCampaignFlightObj : flightList) {
					if (smartCampaignFlightObj != null) {
						if (smartCampaignFlightObj.getStartDate() != null) {
							startArr[i] = smartCampaignFlightObj.getStartDate();
						}
						if (smartCampaignFlightObj.getEndDate() != null) {
							endArr[i] = smartCampaignFlightObj.getEndDate();
						}
						if (smartCampaignFlightObj.getGoal() != null) {
							goalArr[i] = smartCampaignFlightObj.getGoal();
						}
						campaignDTO.setFlightStartdate(startArr);
						campaignDTO.setFlightEnddate(endArr);
						campaignDTO.setFlightGoal(goalArr);
					}
					i++;
				}
			}
		} catch (Exception e) {
			log.severe("Exception in initEditFlight method of SmartCampaignPlannerService"
					+ e.getMessage());
			e.printStackTrace();
		}

		return campaignDTO;

	}

	public List<SmartCampaignPlacementObj> getAllPlacementBycampaignId(
			Long campaignId) throws Exception {
		log.info("In getAllPlacementBycampaignId method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignPlacementObj> placementObjList = new ArrayList<>();
		placementObjList = campaignDAO.getAllPlacementOfCampaign(campaignId);
		return placementObjList;
	}

	public void deleteAllChildPlacement(
			List<SmartCampaignPlacementObj> placementList)
			throws DataServiceException {
		log.info("In deletePlacementById method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		campaignDAO.deletePlacementList(placementList);
	}

	public void deleteAllChildFlights(List<SmartCampaignFlightObj> flightList)
			throws DataServiceException {
		log.info("In deletePlacementById method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		if (flightList != null && flightList.size() > 0) {
			campaignDAO.deleteFlightList(flightList);
		}
	}

	/*
	 * @Override public boolean deleteCampaign(String campaignId) throws
	 * Exception {
	 * log.info("In deleteCampaign method of SmartCampaignPlannerService... ");
	 * boolean isArchived = false; try { ISmartCampaignPlannerDAO
	 * campaignPlaneerDAO = new SmartCampaignPlannerDAO(); if(campaignId!=null){
	 * long campId = Long.parseLong(campaignId); SmartCampaignObj campaignObj =
	 * campaignPlaneerDAO.getCampaignById(campId); if(campaignObj!=null){
	 * 
	 * campaignObj.setCampaignStatus(CampaignStatusEnum.Archived.ordinal()+"");
	 * 
	 * String campaignStatus= LinMobileConstants.CAMPAIGN_STATUS[6].toString();
	 * 
	 * if(campaignStatus!=null){ String strArr[] = campaignStatus.split(":");
	 * if(strArr[1]!=null){ campaignObj.setCampaignStatus(strArr[0].trim()); } }
	 * campaignPlaneerDAO.saveObjectWithStrongConsistancy(campaignObj);
	 * List<SmartCampaignObj> campaignList = new ArrayList<>(); campaignList =
	 * campaignPlaneerDAO.getSmartCampaignList(campaignObj.getCampaignStatus(),
	 * campaignObj.getPublisherId()); //String memcacheKey =
	 * "CAMPAIGN_OBJ_LIST_KEY_BY_PUBLISHERID"+campaignObj.getAdServerId()+""; //
	 * MemcacheUtil.setObjectsListInCache(campaignList, memcacheKey); isArchived
	 * = true; } }
	 * 
	 * 
	 * } catch(DataServiceException e) {
	 * log.severe("DataServiceException : deleteCampaign: "+e.getMessage());
	 * e.printStackTrace(); throw e; }catch(Exception e) {
	 * log.severe("Exception : deleteCampaign: "+e.getMessage());
	 * e.printStackTrace(); throw e; } return isArchived; }
	 * 
	 * 
	 * @Override public boolean unarchiveCampaign(String campaignId) throws
	 * Exception {
	 * log.info("In unarchiveCampaign method of SmartCampaignPlannerService... "
	 * ); boolean isUnarchived = false; try { ISmartCampaignPlannerDAO
	 * campaignPlaneerDAO = new SmartCampaignPlannerDAO(); if(campaignId!=null){
	 * long campId = Long.parseLong(campaignId); SmartCampaignObj campaignObj =
	 * campaignPlaneerDAO.getCampaignById(campId); if(campaignObj!=null){
	 * 
	 * campaignObj.setCampaignStatus(CampaignStatusEnum.Draft.ordinal()+"");
	 * String campaignStatus= LinMobileConstants.CAMPAIGN_STATUS[4].toString();
	 * if(campaignStatus!=null){ String strArr[] = campaignStatus.split(":");
	 * if(strArr[1]!=null){ campaignObj.setCampaignStatus(strArr[0].trim()); } }
	 * campaignPlaneerDAO.saveObjectWithStrongConsistancy(campaignObj);
	 * List<SmartCampaignObj> campaignList = new ArrayList<>(); campaignList =
	 * campaignPlaneerDAO.getSmartCampaignList(campaignObj.getCampaignStatus(),
	 * campaignObj.getPublisherId()); //String memcacheKey =
	 * "CAMPAIGN_OBJ_LIST_KEY_BY_PUBLISHERID"+campaignObj.getAdServerId()+""; //
	 * MemcacheUtil.setObjectsListInCache(campaignList, memcacheKey);
	 * isUnarchived = true; } }
	 * 
	 * 
	 * } catch(DataServiceException e) {
	 * log.severe("DataServiceException : unarchiveCampaign: "+e.getMessage());
	 * e.printStackTrace(); throw e; }catch(Exception e) {
	 * log.severe("Exception : unarchiveCampaign: "+e.getMessage());
	 * e.printStackTrace(); throw e; } return isUnarchived; }
	 */

	@Override
	public boolean deletePlacement(String campaignId, String placementId,
			long userId) throws Exception {
		log.info("In deleteCampaign method of SmartCampaignPlannerService... ");
		boolean isDelete = false;
		try {
			ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
			if (placementId != null) {
				long placeId = Long.parseLong(placementId);
				SmartCampaignPlacementObj placementObj = campaignPlannerDAO
						.getPlacementById(placeId);
				if (placementObj != null) {
					campaignPlannerDAO.deleteObject(placementObj);
					isDelete = true;

					if (campaignId != null) {
						SmartCampaignObj campaignObj = campaignPlannerDAO
								.getCampaignByCampaignId(StringUtil
										.getLongValue(campaignId));
						if (campaignObj != null) {
							String campaignHistoryLog = "Placement Deleted : "
									+ placementObj.getPlacementName();
							saveCampaignHistory(campaignObj.getCampaignId(),
									campaignObj.getName(),
									placementObj.getPlacementId(),
									placementObj.getPlacementName(), userId,
									campaignHistoryLog);
						}
					}
				}
			}

		} catch (DataServiceException e) {
			log.severe("DataServiceException : deletePlacement: "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			log.severe("Exception : deletePlacement: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return isDelete;
	}

	@Override
	public UnifiedCampaignDTO getAllCampaignList(UnifiedCampaignDTO campaignDTO)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public UnifiedCampaignDTO loadCampaign(String campaignId) {
		log.info("In loadCampaign method of SmartCampaignPlannerService... campaignId:"
				+ campaignId);
		long id = Long.parseLong(campaignId);
		UnifiedCampaignDTO unifiedCampaignDTO = null;
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		try {
			SmartCampaignObj campaign = campaignDAO.getCampaignById(id);
			if (campaign != null) {
				unifiedCampaignDTO = new UnifiedCampaignDTO();
				unifiedCampaignDTO.setCampaignName(campaign.getName());
				unifiedCampaignDTO.setId(campaign.getCampaignId());
				unifiedCampaignDTO.setNotes(campaign.getNotes());
				unifiedCampaignDTO.setStartDate(campaign.getStartDate());
				unifiedCampaignDTO.setEndDate(campaign.getEndDate());
				unifiedCampaignDTO.setSelectedAdvertiser(campaign
						.getAdvertiserId());
				unifiedCampaignDTO.setSelectedAgency(campaign.getAgencyId());
				unifiedCampaignDTO.setStartDate(campaign.getStartDate());
				unifiedCampaignDTO.setEndDate(campaign.getEndDate());
				unifiedCampaignDTO.setStatusId(campaign.getCampaignStatus());
				unifiedCampaignDTO.setHasMediaPlan(campaign.isHasMediaPlan());
				unifiedCampaignDTO.setHasProcessing(campaign.isProcessing());
				unifiedCampaignDTO.setHasSetupOnDFP(campaign.isSetupOnDFP());
				unifiedCampaignDTO.setHasMigrated(campaign.isMigrated());

				List<SmartCampaignPlacementObj> placementList = campaignDAO
						.getAllPlacementOfCampaign(id);
				if (placementList != null && placementList.size() > 0) {
					unifiedCampaignDTO.setPlacementByCampaignList(placementList);
				} else {
					log.warning("No placements founds for this campaign --"
							+ campaignId);
				}
			}
		} catch (DataServiceException e) {
			log.severe("Failed to load placement--DataServiceException:"
					+ e.getMessage());
		}
        log.info("Return unified campaignDTO...");
		return unifiedCampaignDTO;
	}

	
	public AccountsEntity addAccount(String name, String address, String accountType, String phone, String fax, 
			String email, String zip, long userId,String dfpNetworkCode,CompanyObj userCompany) {
		log.info("Add account in dfp if not available, also add in datastore, name: "+name+" and accountType : "+accountType+ 
					" and dfpNetworkCode :"+dfpNetworkCode);
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();

		AccountsEntity accountObj=null;
		List<AccountsEntity> accountList = new ArrayList<>();
		boolean isDuplicateName = false;
		CompanyObj companyObj = userCompany;
		try {

			if (userId != 0) {
				
				log.info("Load all accounts..");
				accountList = campaignDAO.getAllAccounts(dfpNetworkCode);
				if (accountList != null && accountList.size() > 0) {
					for (AccountsEntity account : accountList) {
						if (account.getAccountName() != null
								&& account.getAccountName().length() > 0
								&& account.getAccountName().trim().equalsIgnoreCase(name.trim())) {
							isDuplicateName = true;
							break;
						}
					}
				}
				if (!isDuplicateName) {
					//account.setDfpNetworkCode(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);
								
					log.info("Save this account on DFP if not exist...");
					IDFPCampaignSetupService dfpCampaignSetupService=new DFPCampaignSetupService();
					
					accountObj=new AccountsEntity(name, accountType, LinMobileConstants.STATUS_ARRAY[0], 
							name, null, null, phone, email, fax, dfpNetworkCode, companyObj.getId()+"", userId, null,zip,address);
					accountObj=dfpCampaignSetupService.saveOrUpdateAccountInDFPAndDatastore(dfpServices, dfpSession, accountObj);
					
				}
					
						 
				

			}else{
				log.warning("Invalid userId : "+userId);
			}

		} catch (Exception e) {
			log.severe("Exception : adding account: " + e.getMessage());
			e.printStackTrace();
			
		}
		

		return accountObj;
	}
	
	
	@Override
	public AdvertiserObj addAdvertiser(String name, String address,
			String phone, String fax, String email, String zip, long userId) {
		log.info("In addAdvertiser method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();

		AdvertiserObj advertisersObj = new AdvertiserObj();
		List<AdvertiserObj> advertiserList = new ArrayList<>();
		boolean isDuplicateName = false;
		// CompanyObj companyObj = new CompanyObj();
		try {

			if (userId != 0) {
				/*
				 * if(companyObj.getAdServerId()!=null &&
				 * companyObj.getAdServerId().get(0)!=null &&
				 * companyObj.getAdServerId().get(0).length()>0){ advertiserList
				 * =
				 * campaignDAO.getAllAdvertiserByAdServerId(companyObj.getAdServerId
				 * ().get(0)); }else{ advertiserList =
				 * campaignDAO.getAllAdvertiserByAdServerId("0"); }
				 */
				// advertiserList =
				// campaignDAO.getAllAdvertiserByAdServerId(companyObj.getAdServerId().get(0));
				advertiserList = campaignDAO.getAllAdvertiserByAdServerId("0");
				if (advertiserList != null && advertiserList.size() > 0) {
					for (AdvertiserObj advObj : advertiserList) {
						if (advObj != null
								&& advObj.getName() != null
								&& advObj.getName().length() > 0
								&& advObj.getName().trim()
										.equalsIgnoreCase(name.trim())) {
							isDuplicateName = true;
							break;
						}
					}
				}

				// if(!isDuplicateName && companyObj != null &&
				// companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])
				// && companyObj.getCompanyType() != null &&
				// companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]))
				// {
				// if(companyObj.getAdServerId() != null &&
				// companyObj.getAdServerId().size() > 0 &&
				// companyObj.getAdServerUsername() != null &&
				// companyObj.getAdServerUsername().size() > 0 &&
				// companyObj.getAdServerPassword() != null &&
				// companyObj.getAdServerPassword().size() > 0) {
				/*
				 * if(!isDuplicateName && companyObj.getAdServerId()!=null &&
				 * companyObj.getAdServerId().get(0)!=null &&
				 * companyObj.getAdServerId().get(0).length()>0){ advertiserList
				 * =
				 * campaignDAO.getAllAdvertiserByAdServerId(companyObj.getAdServerId
				 * ().get(0)); }else{ advertiserList =
				 * campaignDAO.getAllAdvertiserByAdServerId("0"); }
				 */
				if (!isDuplicateName) {
					advertisersObj
							.setDfpNetworkCode(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);

					if (name != null && name.length() > 0) {
						advertisersObj.setName(name);
					}
					if (address != null && address.length() > 0) {
						advertisersObj.setAddress(address);
					}
					if (phone != null && phone.length() > 0) {
						advertisersObj.setPhone(phone);
					}
					if (fax != null && fax.length() > 0) {
						advertisersObj.setFax(fax);
					}
					if (email != null && email.length() > 0) {
						advertisersObj.setEmail(email);
					}
					if (zip != null && zip.length() > 0) {
						advertisersObj.setZipCode(zip);
					}
					if (advertisersObj != null) {
						campaignDAO
								.saveObjectWithStrongConsistancy(advertisersObj);
						System.out.println("after saving....."
								+ advertisersObj.getId());
						return advertisersObj;
					}
				}

				// }
				// }
			}

		} catch (Exception e) {
			log.severe("Exception : addAdvertiser: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return null;

		// return null;
	}

	@Override
	public AccountsEntity addAccount(String name, String address, String phone, String fax, String email, String zip, long userId, String dfpNetworkCode, String type) {
		log.info("In addAdvertiser method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();

		AccountsEntity accountsObj = new AccountsEntity();
		List<AccountsEntity> accountsList = new ArrayList<AccountsEntity>();
		try {

			if (userId != 0) {
				accountsList = campaignDAO.getAllAccountsByName(name);
				if (accountsList != null && accountsList.size() > 0) {
					return null;
					}
				}
 					accountsObj.setAdServerId(dfpNetworkCode);

					if (name != null && name.length() > 0) {
						accountsObj.setAccountName(name);
					}
					if (address != null && address.length() > 0) {
						accountsObj.setAddress(address);
					}
					if (phone != null && phone.length() > 0) {
						accountsObj.setPhone(phone);
					}
					if (fax != null && fax.length() > 0) {
						accountsObj.setFax(fax);
					}
					if (email != null && email.length() > 0) {
						accountsObj.setEmail(email);
					}
					if (zip != null && zip.length() > 0) {
						accountsObj.setZip(zip);
					}
					accountsObj.setAccountType(type);
					
					 	campaignDAO
								.saveObjectWithStrongConsistancy(accountsObj);
						System.out.println("after saving....."
								+ accountsObj.getId());
						return accountsObj;
		} catch (Exception e) {
			log.severe("Exception : addAdvertiser: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public AgencyObj addAgency(String name, String address, String phone,
			String fax, String email, String zip, long userId) {
		log.info("In addAgency method of SmartCampaignPlannerService... ");
		ISmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();

		AgencyObj agenciesObj = new AgencyObj();
		List<AgencyObj> agencyList = new ArrayList<>();
		boolean isDuplicateName = false;
		// CompanyObj companyObj = new CompanyObj();
		try {

			if (userId != 0) {
				/*
				 * if(companyObj.getAdServerId()!=null &&
				 * companyObj.getAdServerId().get(0)!=null &&
				 * companyObj.getAdServerId().get(0).length()>0){ agencyList =
				 * campaignDAO
				 * .getAllAgencyByAdServerId(companyObj.getAdServerId().get(0));
				 * }else{ agencyList =
				 * campaignDAO.getAllAgencyByAdServerId("0"); }
				 */
				// agencyList =
				// campaignDAO.getAllAgencyByAdServerId(companyObj.getAdServerId().get(0));
				agencyList = campaignDAO.getAllAgencyByAdServerId("0");
				if (agencyList != null && agencyList.size() > 0) {
					for (AgencyObj agencyObj : agencyList) {
						if (agencyObj != null
								&& agencyObj.getName() != null
								&& agencyObj.getName().length() > 0
								&& agencyObj.getName().trim()
										.equalsIgnoreCase(name.trim())) {
							isDuplicateName = true;
							break;
						}
					}
				}
				// if(!isDuplicateName && companyObj != null &&
				// companyObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0])
				// && companyObj.getCompanyType() != null &&
				// companyObj.getCompanyType().equals(LinMobileConstants.COMPANY_TYPE[0]))
				// {
				// if(companyObj.getAdServerId() != null &&
				// companyObj.getAdServerId().size() > 0 &&
				// companyObj.getAdServerUsername() != null &&
				// companyObj.getAdServerUsername().size() > 0 &&
				// companyObj.getAdServerPassword() != null &&
				// companyObj.getAdServerPassword().size() > 0) {
				/*
				 * if(!isDuplicateName && companyObj.getAdServerId()!=null &&
				 * companyObj.getAdServerId().get(0)!=null &&
				 * companyObj.getAdServerId().get(0).length()>0){ agencyList =
				 * campaignDAO
				 * .getAllAgencyByAdServerId(companyObj.getAdServerId().get(0));
				 * }else{ agencyList =
				 * campaignDAO.getAllAgencyByAdServerId("0"); }
				 */
				if (!isDuplicateName) {
					agenciesObj
							.setDfpNetworkCode(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);

					if (name != null && name.length() > 0) {
						agenciesObj.setName(name);
					}
					if (address != null && address.length() > 0) {
						agenciesObj.setAddress(address);
					}
					if (phone != null && phone.length() > 0) {
						agenciesObj.setPhone(phone);
					}
					if (fax != null && fax.length() > 0) {
						agenciesObj.setFax(fax);
					}
					if (email != null && email.length() > 0) {
						agenciesObj.setEmail(email);
					}
					if (zip != null && zip.length() > 0) {
						agenciesObj.setZipCode(zip);
					}
					if (agenciesObj != null) {
						campaignDAO
								.saveObjectWithStrongConsistancy(agenciesObj);
						return agenciesObj;
					}
				}

				// }
				// }
			}

		} catch (Exception e) {
			log.severe("Exception : addAgency: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/*
	 * @author Youdhveer Panwar This method will update smart campaign and all
	 * placements after setup on DFP with their order and lineItmes details
	 */
	public boolean updateSmartCampaignAndPlacementsAfterAdServerSetup(
			SmartMediaPlanObj smartMediaPlan) {
		boolean updated = true;
		String campaignId = smartMediaPlan.getCampaignId();
		ISmartCampaignPlannerDAO campaignPlanerDAO = new SmartCampaignPlannerDAO();

		try {
			long id = StringUtil.getLongValue(campaignId);
			if (id > 0) {
				SmartCampaignObj smartCampaignObj = campaignPlanerDAO.getCampaignById(id);
				if (smartCampaignObj != null) {
					log.info("Step-1 : Update smartCampaignObj with dfp orderId and dfpOrder name..");
					smartCampaignObj.setDfpOrderId(smartMediaPlan.getDfpOrderId());
					smartCampaignObj.setDfpOrderName(smartMediaPlan.getDfpOrderName());
					smartCampaignObj.setAdServerId(LinMobileConstants.LIN_MOBILE_NEW_DFP_NETWORK_CODE);
					/*
					 * int readyIndex=CampaignStatusEnum.READY.ordinal();
					 * if(readyIndex <
					 * LinMobileConstants.CAMPAIGN_STATUS.length){ String
					 * campaignStatus=
					 * LinMobileConstants.CAMPAIGN_STATUS[readyIndex
					 * ].toString(); if(campaignStatus!=null){ String strArr[] =
					 * campaignStatus.split(":"); if(strArr[1]!=null){
					 * smartCampaignObj.setCampaignStatus(strArr[0].trim()); } }
					 * }
					 */

					campaignPlanerDAO.saveObjectWithStrongConsistancy(smartCampaignObj);

					List<SmartCampaignPlacementDTO> dfpPlacementsDTOList = smartMediaPlan
							.getDfpPlacements();
					log.info("Step-2 : Update all smartCampaignPlacementObj for this campaign with their lineItemIds and lineItemNames..");
					if (dfpPlacementsDTOList != null) {
						Map<String, List<DFPLineItemDTO>> lineItemMapByPlacement = new HashMap<String, List<DFPLineItemDTO>>();
						for (SmartCampaignPlacementDTO dfpPlacementDTO : dfpPlacementsDTOList) {
							String placementId = dfpPlacementDTO.getId() + "";
							String partner = dfpPlacementDTO.getPartnerName();
							List<DFPLineItemDTO> dfpLineItemList = null;
							if (lineItemMapByPlacement.containsKey(placementId)) {
								dfpLineItemList = lineItemMapByPlacement
										.get(placementId);
								DFPLineItemDTO lineItemDTO = new DFPLineItemDTO(
										dfpPlacementDTO.getLineItemId(),
										dfpPlacementDTO.getLineItemName(),
										partner, "");
								dfpLineItemList.add(lineItemDTO);
							} else {
								dfpLineItemList = new ArrayList<DFPLineItemDTO>();
							}
							DFPLineItemDTO lineItemDTO = new DFPLineItemDTO(
									dfpPlacementDTO.getLineItemId(),
									dfpPlacementDTO.getLineItemName(), partner,
									"");
							dfpLineItemList.add(lineItemDTO);
							lineItemMapByPlacement.put(placementId,dfpLineItemList);
						}

						List<SmartCampaignPlacementObj> smartCampaignPlacementsList = campaignPlanerDAO
								.getAllPlacementOfCampaign(id);
						if (smartCampaignPlacementsList != null) {
							for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementsList) {
								String placementId = smartCampaignPlacementObj.getPlacementId() + "";
								List<DFPLineItemDTO> dfpLineItemList = lineItemMapByPlacement.get(placementId);
								smartCampaignPlacementObj.setDfpLineItemList(dfpLineItemList);

								campaignPlanerDAO.saveObjectWithStrongConsistancy(smartCampaignPlacementObj);
								log.info("smartCampaignPlacementObj updated successfully.. placementId:"
										+ smartCampaignPlacementObj.getPlacementId());
							}
						}
					} else {
						log.info("There is no lineitem for this order right now :"+ smartMediaPlan.getDfpOrderId());
					}

				} else {
					log.info("No smartCampaignObj loaded for id:" + id);
				}

			} else {
				log.info("Invalid campaign id-" + campaignId);
			}

		} catch (DataServiceException e) {
			log.severe("Failed to load campaign/placements with id:"
					+ campaignId);
			updated = false;
		}
		return updated;
	}

	/*
	 * @author Youdhveer Panwar Update campaign status as per DFP
	 * 
	 * @see
	 * com.lin.web.service.ISmartCampaignPlannerService#updateSmartCampaignStatus
	 * (com.lin.web.util.CampaignStatusEnum, java.lang.String)
	 */
	public boolean updateSmartCampaignStatus(CampaignStatusEnum campaignStatus,
			String campaignId) {
		log.info("update campaign status : " + campaignStatus
				+ " for campaignId -" + campaignId);
		ISmartCampaignPlannerDAO campaignPlanerDAO = new SmartCampaignPlannerDAO();
		try {
			SmartCampaignObj smartCampaignObj = loadSmartCampaign(campaignId);
			int campaignStatusValue = campaignStatus.ordinal();
			if (smartCampaignObj != null) {
				String oldStatus = smartCampaignObj.getCampaignStatus();
				smartCampaignObj.setCampaignStatus(campaignStatusValue + "");
				campaignPlanerDAO
						.saveObjectWithStrongConsistancy(smartCampaignObj);
				log.info("oldStatus :" + oldStatus + " has been changed with "
						+ campaignStatusValue + " (" + campaignStatus + ")");
			} else {
				log.info("No campaign found for this id -" + campaignId);
			}
			return true;
		} catch (DataServiceException e) {
			log.severe("Failed to load campaign/placements with id:"
					+ campaignId);
			return false;
		}
	}

	public boolean updateSmartCampaignFlags(CampaignStatusEnum campaignStatus,
			String campaignId, Boolean hasMediaPlan, Boolean isProcessing,
			Boolean isSetupOnDFP, Integer planTypeFlag) {
		log.info("update campaign status : " + campaignStatus
				+ " for campaignId -" + campaignId + ", hasMediaPlan : "
				+ hasMediaPlan + " , isProcessing : " + isProcessing
				+ ", isSetupOnDFP : " + isSetupOnDFP);
		ISmartCampaignPlannerDAO campaignPlanerDAO = new SmartCampaignPlannerDAO();
		try {
			SmartCampaignObj smartCampaignObj = loadSmartCampaign(campaignId);

			if (smartCampaignObj != null) {
				if (campaignStatus != null) {
					int campaignStatusValue = campaignStatus.ordinal();
					String oldStatus = smartCampaignObj.getCampaignStatus();
					smartCampaignObj
							.setCampaignStatus(campaignStatusValue + "");
					log.info("oldStatus :" + oldStatus
							+ " , new campaign status : " + campaignStatusValue
							+ " (" + campaignStatus + ")");
				}

				if (hasMediaPlan != null) {
					smartCampaignObj.setHasMediaPlan(hasMediaPlan);
				}
				if (isProcessing != null) {
					smartCampaignObj.setProcessing(isProcessing);
				}
				if (isSetupOnDFP != null) {
					smartCampaignObj.setSetupOnDFP(isSetupOnDFP);
				}
				if(planTypeFlag!=null){
					smartCampaignObj.setMediaPlanType(planTypeFlag);
				}
				campaignPlanerDAO
						.saveObjectWithStrongConsistancy(smartCampaignObj);
				log.info("Campaign object updated...");
			} else {
				log.info("No campaign found for this id -" + campaignId);
			}
			return true;
		} catch (DataServiceException e) {
			log.severe("Failed to load campaign/placements with id:"
					+ campaignId);
			return false;
		}
	}

	public SmartCampaignObj loadSmartCampaign(String campaignId) {
		log.info("load campaign campaignId -" + campaignId);
		SmartCampaignObj smartCampaignObj = null;
		ISmartCampaignPlannerDAO campaignPlanerDAO = new SmartCampaignPlannerDAO();
		try {
			long id = StringUtil.getLongValue(campaignId);
			if (id > 0) {
				smartCampaignObj = campaignPlanerDAO.getCampaignById(id);
			} else {
				log.info("Invalid id -" + id);
			}
		} catch (DataServiceException e) {
			log.severe("Failed to load campaign/placements with id:"
					+ campaignId);
		}
		return smartCampaignObj;
	}

	/*
	 * This method will migrate all campaigns from BigQuery to Datastore
	 * 
	 * @author Youdhveer Panwar
	 * 
	 * @param Map<String,MigrateCampaignDTO>
	 */
	public void migrateCampaignFromBigQueryToDatastore(
			Map<String, MigrateCampaignDTO> campaignMap, String dfpNetworkName) {
		if (campaignMap != null && campaignMap.size() > 0) {
			log.info("Going to migrate campaigns..." + campaignMap.size());
			ISmartCampaignPlannerDAO smartPlannerDAO = new SmartCampaignPlannerDAO();
			int count = 0;
			long maxId = 0;
			try {
				maxId = smartPlannerDAO.maxCampaignId();
				long placementMaxId = smartPlannerDAO.maxPlacementId();
				for (String orderId : campaignMap.keySet()) {
					MigrateCampaignDTO campaignDTO = campaignMap.get(orderId);
					String dfpNetworkCode = campaignDTO.getDfpNetworkCode();

					log.info("Update advertiser ..");
					long advertiserId = campaignDTO.getAdvertiserId();
					String advertiserName = campaignDTO.getAdvertiserName();
					AdvertiserObj advertiser = new AdvertiserObj(advertiserId,
							advertiserName, dfpNetworkCode);
					advertiser.setId(advertiserId);
					advertiser = saveOrUpdateAdvertiser(advertiser);

					log.info("Update agency if available..");
					long agencyId = campaignDTO.getAgencyId();
					AgencyObj agency = null;
					if (agencyId > 0) {
						String agencyName = campaignDTO.getAgencyName();
						agency = new AgencyObj(agencyId, agencyName,
								dfpNetworkCode);
						agency.setId(agencyId);
						agency = saveOrUpdateAgency(agency);
					}

					log.info("Create SmartCampaignObj ..");
					maxId++;
					log.info("Id for smartCampaign by using maxId : " + maxId);
					SmartCampaignObj smartCampaign = new SmartCampaignObj();
					smartCampaign.setAdServerId(dfpNetworkCode);
					smartCampaign.setAdServerUsername(dfpNetworkName);
					smartCampaign.setAdvertiserId(advertiser.getId() + "");
					if (agency != null) {
						smartCampaign.setAgencyId(agency.getId() + "");
					}
					smartCampaign.setName(campaignDTO.getOrderName()
							.replaceAll("'", ""));
					// smartCampaign.setId(campaignDTO.getOrderId());
					// smartCampaign.setCampaignId(campaignDTO.getOrderId());
					smartCampaign.setId(maxId);
					smartCampaign.setCampaignId(maxId);
					smartCampaign.setDfpOrderId(campaignDTO.getOrderId());
					smartCampaign.setDfpOrderName(campaignDTO.getOrderName());
					smartCampaign.setStartDate(campaignDTO.getOrderStartDate());
					smartCampaign.setEndDate(campaignDTO.getOrderEndDate());
					smartCampaign.setMigrated(true);
					smartCampaign.setSetupOnDFP(true);

					String companyId = campaignDTO.getCompanyId();
					smartCampaign.setCompanyId(companyId);
					String companyName = campaignDTO.getCompanyName();
					smartCampaign.setCompanyName(companyName);
					String campaignType = campaignDTO.getCampaignType();
					List<DropdownDataObj> rateTypeList = loadRateTypeDropDown(campaignType);
					smartCampaign.setRateTypeList(rateTypeList);
					String currentDate = DateUtil
							.getCurrentTimeStamp("MM-dd-yyyy");
					long days = DateUtil.getDifferneceBetweenTwoDates(
							currentDate, campaignDTO.getOrderEndDate(),
							"MM-dd-yyyy");
					if (days > 0) {
						smartCampaign
								.setCampaignStatus(CampaignStatusEnum.Draft
										.ordinal() + "");
					} else {
						smartCampaign
								.setCampaignStatus(CampaignStatusEnum.Completed
										.ordinal() + "");
					}

					smartPlannerDAO
							.saveObjectWithStrongConsistancy(smartCampaign);
					log.info("Save smart campaign object with auto id--"
							+ smartCampaign.getId());
					List<MigratePlacementDTO> dfpPlacementList = campaignDTO
							.getPlacementList();
					// long placementMaxId = smartPlannerDAO.maxPlacementId();

					for (MigratePlacementDTO migratedPlacement : dfpPlacementList) {
						placementMaxId = placementMaxId + 1;
						SmartCampaignPlacementObj smartCampaignPlacement = createSmartPlacementFromMigratePlacement(
								smartCampaign, migratedPlacement,
								placementMaxId);
						smartPlannerDAO
								.saveObjectWithStrongConsistancy(smartCampaignPlacement);
						log.info("smartCampaignPlacement has been saved successfully with id - "
								+ smartCampaignPlacement.getId());
					}
					log.info("Campaign migration completed for orderId :"
							+ orderId);

					count++;
				}
				log.info("Total campaign migrated --" + count);

			} catch (DataServiceException e1) {
				log.severe("Exception in loading max id for campaign.."
						+ e1.getMessage());
			}

		} else {
			log.info("No campaign to migrate");
		}
	}

	/*
	 * @author Youdhveer Panwar This method will create smart placement from
	 * migrated placement
	 * 
	 * @see com.lin.web.service.ISmartCampaignPlannerService#
	 * createSmartPlacementFromMigratePlacement
	 * (com.lin.server.bean.SmartCampaignObj,
	 * com.lin.web.dto.MigratePlacementDTO, long)
	 */
	public SmartCampaignPlacementObj createSmartPlacementFromMigratePlacement(
			SmartCampaignObj smartCampaign,
			MigratePlacementDTO migratedPlacement, long placemetMaxId) {

		long lineItemId = migratedPlacement.getLineItemId();
		log.info("Create smart campaign placement from migrated placement..lineItemId : "
				+ lineItemId + ", with id:" + placemetMaxId);
		String lineItemName = migratedPlacement.getLineItemName();
		String lineItemStartDate = migratedPlacement.getLineItemStartDate();
		String lineItemEndDate = migratedPlacement.getLineItemEndDate();
		String rate = migratedPlacement.getRate();
		List<CreativeObj> creativeList = migratedPlacement.getCreativeList();

		Key<SmartCampaignObj> campaignKey = Key.create(SmartCampaignObj.class,
				smartCampaign.getCampaignId());
		SmartCampaignPlacementObj smartCampaignPlacement = new SmartCampaignPlacementObj();
		smartCampaignPlacement.setCampaign(campaignKey);
		smartCampaignPlacement.setCreativeObj(creativeList);
		smartCampaignPlacement.setRate(rate);
		smartCampaignPlacement.setStartDate(lineItemStartDate);
		smartCampaignPlacement.setEndDate(lineItemEndDate);
		smartCampaignPlacement.setPlacementName(lineItemName);

		List<DFPLineItemDTO> dfpLineItemList = new ArrayList<DFPLineItemDTO>();
		DFPLineItemDTO lineItemDTO = new DFPLineItemDTO(lineItemId,
				lineItemName, smartCampaign.getCompanyName(), "");
		dfpLineItemList.add(lineItemDTO);
		smartCampaignPlacement.setDfpLineItemList(dfpLineItemList);
		if (placemetMaxId <= 0) {
			smartCampaignPlacement.setId(lineItemId);
			smartCampaignPlacement.setPlacementId(lineItemId);
		} else {
			smartCampaignPlacement.setId(placemetMaxId);
			smartCampaignPlacement.setPlacementId(placemetMaxId);
		}

		log.info("Create a default flight for this placement with id-1");
		List<SmartCampaignFlightDTO> flightObjList = new ArrayList<SmartCampaignFlightDTO>();
		SmartCampaignFlightDTO flightDTO = new SmartCampaignFlightDTO();
		String flightName = lineItemStartDate + ":" + lineItemEndDate;
		flightDTO.setFlightName(flightName);
		flightDTO.setStartDate(lineItemStartDate);
		flightDTO.setEndDate(lineItemEndDate);
		flightDTO.setFlightId(1);
		flightObjList.add(flightDTO);
		smartCampaignPlacement.setFlightObjList(flightObjList);

		return smartCampaignPlacement;
	}

	/*
	 * @author Youdhveer Panwar
	 * 
	 * Load dropdown list for Campaign Type (Like CPM, CPC, CPD)
	 */
	public List<DropdownDataObj> loadRateTypeDropDown(String campaignTypeValue) {
		String campaignValueType = "Campaign Type";
		log.info("Load dropdown object for value type : " + campaignValueType
				+ " with value :" + campaignTypeValue);
		ISmartCampaignPlannerDAO smartPlannerDAO = new SmartCampaignPlannerDAO();
		List<DropdownDataObj> dropDownList = new ArrayList<>();

		List<DropdownDataObj> rateTypeList;
		try {
			rateTypeList = smartPlannerDAO
					.getRateTypeByValue(campaignValueType);
			if (rateTypeList != null && rateTypeList.size() > 0) {
				for (DropdownDataObj rateType : rateTypeList) {
					String value = rateType.getValue();
					if (value != null && value.contains(campaignTypeValue)) {
						dropDownList.add(rateType);
						break;
					}
				}
			}
		} catch (DataServiceException e) {
			log.severe("DataServiceException : " + e.getMessage());
		}
		log.info("Return droip down list --" + dropDownList.size());
		return dropDownList;
	}

	/*
	 * @author Youdhveer Panwar Save AdvertiserObj from DFP to Datastore
	 * 
	 * @see
	 * com.lin.web.service.ISmartCampaignPlannerService#saveOrUpdateAdvertiser
	 * (com.lin.server.bean.AdvertiserObj)
	 */

	public AdvertiserObj saveOrUpdateAdvertiser(AdvertiserObj advertiser) {
		AdvertiserObj advertiserObj = advertiser;
		if (advertiser != null && advertiser.getName() != null) {
			String advertiserName = advertiser.getName();
			int index = advertiserName.indexOf("|");
			if (index != -1) {
				advertiser
						.setName((advertiserName.substring(index + 1)).trim());
			}
			IMediaPlanDAO mediaplanDAO = new MediaPlanDAO();

			try {
				log.info("update advertiserObj with dfp advertiserId-"
						+ advertiser.getAdvertiserId());
				boolean update = false;
				advertiserObj = mediaplanDAO.loadAdvertiser(
						advertiser.getName(), advertiser.getDfpNetworkCode());
				if (advertiserObj != null) {
					if (advertiserObj.getAdvertiserId() == 0
							&& advertiser.getAdvertiserId() > 0) {
						advertiserObj.setAdvertiserId(advertiser
								.getAdvertiserId());
						update = true;
					}
				} else {
					advertiserObj = advertiser;
					update = true;
				}
				if (update) {
					mediaplanDAO.saveObject(advertiserObj);
					log.info("Advertiser saved successfully..datastore id- :"
							+ advertiserObj.getId());
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : " + e.getMessage());
			}
		}
		return advertiserObj;
	}

	/*
	 * @author Youdhveer Panwar Save Agency from DFP to Datastore
	 * 
	 * @see
	 * com.lin.web.service.ISmartCampaignPlannerService#saveOrUpdateAgency(com
	 * .lin.server.bean.AgencyObj)
	 */

	public AgencyObj saveOrUpdateAgency(AgencyObj agency) {
		AgencyObj agencyObj = agency;
		if (agency != null) {
			IMediaPlanDAO mediaplanDAO = new MediaPlanDAO();

			try {
				log.info("update agencyObj with dfp agencyId-"
						+ agency.getAgencyId());
				boolean update = false;
				agencyObj = mediaplanDAO.loadAgency(agency.getName(),
						agency.getDfpNetworkCode());
				if (agencyObj != null) {
					if (agencyObj.getAgencyId() == 0
							&& agencyObj.getAgencyId() > 0) {
						agencyObj.setAgencyId(agencyObj.getAgencyId());
						update = true;
					}
				} else {
					agencyObj = agency;
					update = true;
				}
				if (update) {
					mediaplanDAO.saveObject(agencyObj);
					log.info("Agency saved successfully..datastore id- :"
							+ agencyObj.getId());
				}
			} catch (DataServiceException e) {
				log.severe("DataServiceException : " + e.getMessage());
			}
		}
		return agencyObj;
	}

	@Override
	public JSONObject migrateCampaign(String dfpNetworkCode,
			String dfpNetworkName, String publisherIdInBQ, String companyId,
			String companyName) {
		JSONObject jsonObject = new JSONObject();
		String queryStartDate = "2014-01-01";
		if (publisherIdInBQ != null
				&& publisherIdInBQ
						.equalsIgnoreCase(LinMobileConstants.LIN_MOBILE_PUBLISHER_ID)) {
			queryStartDate = "2013-01-01";
		}
		try {
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			QueryResponse queryResponse = null;
			QueryDTO queryDTO = BigQueryUtil.getQueryDTO(queryStartDate,
					publisherIdInBQ, LinMobileConstants.BQ_CORE_PERFORMANCE);
			if (queryDTO != null && queryDTO.getQueryData() != null
					&& queryDTO.getQueryData().length() > 0) {
				queryResponse = smartCampaignPlannerDAO
						.getAllCampaignsForMigration(queryDTO);
				if (queryResponse != null && queryResponse.getRows() != null
						&& queryResponse.getRows().size() > 0) {
					Map<String, String> existingOrderIdMap = new HashMap<>();
					Map<String, MigrateCampaignDTO> campaignMap = new HashMap<>();
					Map<String, MigratePlacementDTO> placementMap = new HashMap<>();
					Map<String, CreativeObj> creativeMap = new HashMap<>();
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO
							.getAllCampaign();
					if (smartCampaignObjList != null
							&& smartCampaignObjList.size() > 0) {
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							if (smartCampaignObj != null
									&& smartCampaignObj.getDfpOrderId() > 0) {
								existingOrderIdMap.put(
										smartCampaignObj.getDfpOrderId() + "",
										smartCampaignObj.getDfpOrderId() + "");
							}
						}
						log.info("existingOrderIdMap size : "
								+ existingOrderIdMap.size());
					}
					List<TableRow> rowList = queryResponse.getRows();
					for (TableRow row : rowList) {
						if (row != null && row.getF() != null
								&& row.getF().size() > 0) {
							List<TableCell> cellList = row.getF();
							String orderId = cellList.get(4).getV().toString();
							if (orderId != null
									&& LinMobileUtil.isNumeric(orderId)
									&& !(existingOrderIdMap
											.containsKey(orderId))) {
								if (campaignMap.containsKey(orderId)) {
									createPlacement(cellList, orderId,
											campaignMap, placementMap,
											creativeMap);
								} else {
									MigrateCampaignDTO migrateCampaignDTO = new MigrateCampaignDTO();
									migrateCampaignDTO.setOrderId(StringUtil
											.getLongValue(orderId));
									migrateCampaignDTO.setOrderName(cellList
											.get(5).getV().toString());
									migrateCampaignDTO
											.setOrderStartDate(DateUtil
													.getFormatedDate(cellList
															.get(6).getV()
															.toString(),
															"yyyy-MM-dd",
															"MM-dd-yyyy"));
									migrateCampaignDTO
											.setOrderEndDate(DateUtil
													.getFormatedDate(cellList
															.get(7).getV()
															.toString(),
															"yyyy-MM-dd",
															"MM-dd-yyyy"));
									migrateCampaignDTO
											.setDfpNetworkCode(dfpNetworkCode);
									migrateCampaignDTO
											.setAdvertiserId(StringUtil
													.getLongValue(cellList
															.get(0).getV()
															.toString()));
									migrateCampaignDTO
											.setAdvertiserName(cellList.get(1)
													.getV().toString());
									long agencyId = StringUtil
											.getLongValue(cellList.get(2)
													.getV().toString());
									migrateCampaignDTO.setAgencyId(agencyId);
									if (agencyId > 0) {
										migrateCampaignDTO
												.setAgencyName(cellList.get(3)
														.getV().toString());
									}
									migrateCampaignDTO.setCampaignType(cellList
											.get(17).getV().toString());
									migrateCampaignDTO.setCompanyId(companyId);
									migrateCampaignDTO
											.setCompanyName(companyName);
									campaignMap
											.put(orderId, migrateCampaignDTO);
									createPlacement(cellList, orderId,
											campaignMap, placementMap,
											creativeMap);
								}
							}
						}
					}
					log.info("created campaignMap size :" + campaignMap.size());
					jsonObject.put("success", "created campaignMap size :"
							+ campaignMap.size());
					log.info(campaignMap.toString());
					migrateCampaignFromBigQueryToDatastore(campaignMap,
							dfpNetworkName);
				}
			}
		} catch (Exception e) {
			log.severe("Exception in migrateCampaign of SmartCampaignPlannerService : "
					+ e.getMessage());
			jsonObject.put("error", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	private void createPlacement(List<TableCell> cellList, String orderId,
			Map<String, MigrateCampaignDTO> campaignMap,
			Map<String, MigratePlacementDTO> placementMap,
			Map<String, CreativeObj> creativeMap) throws Exception {
		String lineItemId = cellList.get(9).getV().toString();
		String creativeId = cellList.get(13).getV().toString();
		String placementKey = orderId + "_" + lineItemId;
		String creativeKey = placementKey + "_" + creativeId;
		if (placementMap.containsKey(placementKey)) {
			if (!(creativeMap.containsKey(creativeKey))) { // Placement exists
															// but creative does
															// not
				List<MigratePlacementDTO> migratePlacementDTOList = campaignMap
						.get(orderId).getPlacementList();
				if (migratePlacementDTOList != null
						&& migratePlacementDTOList.size() > 0) {
					for (MigratePlacementDTO migratePlacementDTO : migratePlacementDTOList) {
						if ((migratePlacementDTO.getLineItemId() + "")
								.equals(lineItemId)) {
							List<CreativeObj> creativeList = migratePlacementDTO
									.getCreativeList();
							CreativeObj creativeObj = getCreative(cellList);
							creativeList.add(creativeObj);
							creativeMap.put(creativeKey, creativeObj);
							break;
						}
					}
				}
			}
		} else { // Neither Placement nor creative exists
			MigratePlacementDTO migratePlacementDTO = new MigratePlacementDTO();
			migratePlacementDTO.setLineItemId(StringUtil
					.getLongValue(lineItemId));
			migratePlacementDTO.setLineItemName(cellList.get(10).getV()
					.toString());
			migratePlacementDTO.setLineItemStartDate(DateUtil.getFormatedDate(
					cellList.get(11).getV().toString(), "yyyy-MM-dd",
					"MM-dd-yyyy"));
			migratePlacementDTO.setLineItemEndDate(DateUtil.getFormatedDate(
					cellList.get(12).getV().toString(), "yyyy-MM-dd",
					"MM-dd-yyyy"));
			migratePlacementDTO.setOrderId(StringUtil.getLongValue(orderId));
			migratePlacementDTO.setLineItemType(cellList.get(8).getV()
					.toString());
			migratePlacementDTO.setRate(cellList.get(18).getV().toString());
			CreativeObj creativeObj = getCreative(cellList);
			List<CreativeObj> creativeList = new ArrayList<>();
			creativeList.add(creativeObj);
			creativeMap.put(creativeKey, creativeObj);
			migratePlacementDTO.setCreativeList(creativeList);
			placementMap.put(placementKey, migratePlacementDTO);

			MigrateCampaignDTO migrateCampaignDTO = campaignMap.get(orderId);
			List<MigratePlacementDTO> migratePlacementDTOList = migrateCampaignDTO
					.getPlacementList();
			if (migratePlacementDTOList == null) {
				migratePlacementDTOList = new ArrayList<>();
			}
			migratePlacementDTOList.add(migratePlacementDTO);
			migrateCampaignDTO.setPlacementList(migratePlacementDTOList);
		}
	}

	public CreativeObj getCreative(List<TableCell> cellList) throws Exception {
		String size = "";
		if (cellList.get(15).getV().toString() != null) {
			size = cellList.get(15).getV().toString();
			log.info("Size from DB before modifications -- size:" + size);
			size = size.replaceAll(" ", "");
		}
		log.info("size---:" + size);
		String format = cellList.get(16).getV().toString();
		// String creativeId = cellList.get(13).getV().toString();
		if ((format.toLowerCase()).contains("rich media")) {
			format = "Rich Media";
		} else if ((format.toLowerCase()).contains("video")) {
			format = "Video";
			size = size + "v";
		} else {
			format = "Standard";
		}
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		CreativeObj creativeObj = smartCampaignPlannerDAO.getCreative(format,
				size);
		// CreativeObj creativeObj = new
		// CreativeObj(StringUtil.getLongValue(creativeId), format, size);
		return creativeObj;
	}

	public String getPublisherName(String publisherId) {
		String publisherName = null;
		if (publisherId != null) {
			if (publisherId.equals(LinMobileConstants.LIN_MEDIA_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.LIN_MEDIA_PUBLISHER_NAME;
			} else if (publisherId
					.equals(LinMobileConstants.LIN_MOBILE_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.LIN_MOBILE_PUBLISHER_NAME;
			} else if (publisherId
					.equals(LinMobileConstants.TRIBUNE_DFP_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.TRIBUNE_DFP_PUBLISHER_NAME;
			} else if (publisherId
					.equals(LinMobileConstants.LIN_DIGITAL_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.LIN_DIGITAL_PUBLISHER_NAME;
			} else if (publisherId.equals(LinMobileConstants.XAD_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.XAD_PUBLISHER_NAME;
			} else if (publisherId
					.equals(LinMobileConstants.EXAMINER_DFP_PUBLISHER_ID)) {
				publisherName = LinMobileConstants.EXAMINER_DFP_PUBLISHER_NAME;
			} else {
				log.warning("No publisher exist in constant file for publisherId:"
						+ publisherId);
			}
		}
		return publisherName;
	}

	/*
	 * @author Naresh Pokhriyal Pre and Post procesing for update of Order
	 * status in SmartCampaignObj. Also creates a report and email in case of
	 * any Exception.
	 * 
	 * @param String networkId
	 * 
	 * @return String status
	 */

	@Override
	public String updateCampaignDetailFromDFP(String networkId) {
		String status = "";
		StringBuilder summary = new StringBuilder();
		try {
			DfpSession dfpSession = null;
			DfpServices dfpServices = null;
			String networkUsername = "";
			String commaSeperatedOrderIds = "";

			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			String credentialKey = networkId.trim();
			if (map != null && map.size() > 0 && map.containsKey(credentialKey)) {
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(credentialKey);
				if (adServerCredentialsDTO != null
						&& adServerCredentialsDTO.getAdServerId() != null
						&& adServerCredentialsDTO.getAdServerUsername() != null
						&& adServerCredentialsDTO.getAdServerPassword() != null) {
					ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
					networkUsername = adServerCredentialsDTO.getAdServerUsername();

					summary.append("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername + "<br>");
					log.info("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername);
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getAllCampaign(networkId);
					if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
						int totalCampaigns = smartCampaignObjList.size();
						int allOrderIdscount = 0;
						int thisBatchOrderIdsCount = 0;
						boolean hasDFPOrderIds = false;
						boolean hasFailed = false;
						StringBuilder stringBuilder = new StringBuilder();
						List<String> orderIdList = new ArrayList<>();
						DFPReportService dfpReportService = new DFPReportService();
						
						log.info("totalCampaigns : "+totalCampaigns);
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(networkId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();
						
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							allOrderIdscount++;
							if (smartCampaignObj != null && smartCampaignObj.getDfpOrderId() > 0
									&& !(orderIdList.contains(smartCampaignObj.getDfpOrderId() + ""))
									&& !(smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Archived.ordinal()) + ""))) {
								hasDFPOrderIds = true;
								thisBatchOrderIdsCount++;
								stringBuilder.append(smartCampaignObj.getDfpOrderId() + ",");
								orderIdList.add(smartCampaignObj.getDfpOrderId() + "");
								if(thisBatchOrderIdsCount >= 1000 || allOrderIdscount == totalCampaigns) {
									thisBatchOrderIdsCount = 0;
									commaSeperatedOrderIds = StringUtil.deleteFromLastOccurence(stringBuilder.toString(), ",");
									stringBuilder.setLength(0);
									if(!isCampaignStatusUpdated(dfpReportService, dfpServices, dfpSession, commaSeperatedOrderIds, summary, networkId)) {
										hasFailed = true;
									}
								}
							}
						}
						commaSeperatedOrderIds = StringUtil.deleteFromLastOccurence(stringBuilder.toString(), ",");
						if(commaSeperatedOrderIds != null && !(commaSeperatedOrderIds.trim().isEmpty())) {
							if(!isCampaignStatusUpdated(dfpReportService, dfpServices, dfpSession, commaSeperatedOrderIds, summary, networkId)) {
								hasFailed = true;
							}
						}
						if(hasFailed) {
							throw new Exception();
						}
						if(!hasDFPOrderIds) {
							log.warning("No DFP Order Ids for networkCode : " + networkId);
						}
					} else {
						log.warning("No campaigns for networkCode : " + networkId);
					}
				}
				summary.append("<br><br><br>");
			} else {
				log.severe("No credentials for networkCode : " + credentialKey);
				summary.append("No credentials for networkCode : " + credentialKey + "<br>");
			}
			// send mail
			// EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com",
			// LinMobileVariables.SENDER_EMAIL_ADDRESS,
			// "DFP Campaign Update Status - " +
			// LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
			status = "Success : " + summary.toString().replaceAll("<br>", " ");
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in DFP Campaign Status Update : " + e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
			status = "Error : " + summary.toString().replaceAll("<br>", " ");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP Campaign Status Update - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return status;
	}
	
	private boolean isCampaignStatusUpdated(DFPReportService dfpReportService, DfpServices dfpServices, DfpSession dfpSession, 
								String commaSeperatedOrderIds, StringBuilder summary, String networkId) throws Exception {
		boolean result = dfpReportService.updateCampaignDetailFromDFP(dfpServices, dfpSession, commaSeperatedOrderIds, summary, networkId);
		if (result) {
			String successMessage = "DFP Campaign Status Updated Successfull for DFP OrderIds : "+commaSeperatedOrderIds;;
			log.info(successMessage);
			summary.append(successMessage + "<br>");
		} else {
			String errorMessage = "DFP Campaign Status Update Failed for DFP OrderIds : "+commaSeperatedOrderIds;
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
		}
		return result;
	}
	
	/*@Override
	public String updateCampaignDetailFromDFP(String networkId) {
		String status = "";
		StringBuilder summary = new StringBuilder();
		try {
			DfpSession dfpSession = null;
			DfpServices dfpServices = null;
			String networkUsername = "";
			String commaSeperatedOrderIds = "";

			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			String credentialKey = networkId.trim();
			if (map != null && map.size() > 0 && map.containsKey(credentialKey)) {
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(credentialKey);
				if (adServerCredentialsDTO != null
						&& adServerCredentialsDTO.getAdServerId() != null
						&& adServerCredentialsDTO.getAdServerUsername() != null
						&& adServerCredentialsDTO.getAdServerPassword() != null) {
					ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
					networkUsername = adServerCredentialsDTO.getAdServerUsername();

					summary.append("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername + "<br>");
					log.info("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername);
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getAllCampaign(networkId);
					if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
						StringBuilder stringBuilder = new StringBuilder();
						List<String> orderIdList = new ArrayList<>();
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							if (smartCampaignObj != null
									&& smartCampaignObj.getDfpOrderId() > 0
									&& !(orderIdList.contains(smartCampaignObj.getDfpOrderId() + ""))
									&& !(smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Archived.ordinal()) + ""))) {
								stringBuilder.append(smartCampaignObj.getDfpOrderId() + ",");
								orderIdList.add(smartCampaignObj.getDfpOrderId() + "");
							}
						}
						commaSeperatedOrderIds = StringUtil.deleteFromLastOccurence(stringBuilder.toString(), ",");
					}

					if (commaSeperatedOrderIds != null && commaSeperatedOrderIds.trim().length() > 0) {
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(networkId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();

						DFPReportService dfpReportService = new DFPReportService();
						int i = 1;
						while (i < 3) {
							summary.append("Iteration " + i + "<br>");
							boolean result = dfpReportService.updateCampaignDetailFromDFP(dfpServices, dfpSession, commaSeperatedOrderIds, summary, networkId);
							if (result) {
								String successMessage = "DFP Campaign Status Updated Successfully";
								log.info(successMessage);
								summary.append(successMessage + "<br>");
								break;
							} else {
								if (i == 2) {
									String errorMessage = "DFP Campaign Status Update Failed";
									log.severe(errorMessage);
									summary.append(errorMessage + "<br>");
								}
							}
							i++;
						}
					} else {
						log.warning("No DFP Order Ids for networkCode : " + networkId);
					}
				}
				summary.append("<br><br><br>");
			} else {
				log.severe("No credentials for networkCode : " + credentialKey);
				summary.append("No credentials for networkCode : " + credentialKey + "<br>");
			}
			// send mail
			// EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com",
			// LinMobileVariables.SENDER_EMAIL_ADDRESS,
			// "DFP Campaign Update Status - " +
			// LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
			status = "Success : " + summary.toString().replaceAll("<br>", " ");
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Exception generated in DFP Campaign Status Update : " + e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
			status = "Error : " + summary.toString().replaceAll("<br>", " ");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP Campaign Status Update - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return status;
	}*/

	/*
	 * @author Naresh Pokhriyal Pre and Post procesing for update of LineItem
	 * status in SmartCampaignPlacementObj. Also creates a report and email in
	 * case of any Exception.
	 * 
	 * @param String networkId
	 * 
	 * @return String status
	 */
	
	public String updatePlacementDetailFromDFP(String networkId) {
		Map<Long, SmartCampaignPlacementObj> lineItemPlacementMap = new HashMap<>();
		String status = "";
		StringBuilder summary = new StringBuilder();
		try {
			DfpSession dfpSession = null;
			DfpServices dfpServices = null;
			String networkUsername = "";
			String commaSeperatedLineItemIds = "";
			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			String credentialKey = networkId.trim();
			if (map != null && map.size() > 0 && map.containsKey(credentialKey)) {
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(credentialKey);
				if (adServerCredentialsDTO != null
						&& adServerCredentialsDTO.getAdServerId() != null
						&& adServerCredentialsDTO.getAdServerUsername() != null
						&& adServerCredentialsDTO.getAdServerPassword() != null) {
					ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
					networkUsername = adServerCredentialsDTO.getAdServerUsername();

					summary.append("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername + "<br>");
					log.info("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername);
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getAllCampaign(networkId);
					if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
						StringBuilder lineItemIds = new StringBuilder();
						List<String> lineItemIdList = new ArrayList<>();
						DFPReportService dfpReportService = new DFPReportService();
						boolean hasLineItemIds = false;
						boolean hasFailed = false;
						int totalCampaigns = smartCampaignObjList.size();
						int thisBatchLineItemIdsCount = 0;
						
						log.info("totalCampaigns : "+totalCampaigns);
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(networkId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();
						
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							if (smartCampaignObj != null && smartCampaignObj.getDfpOrderId() > 0 && !(smartCampaignObj.getCampaignStatus()
											.equals((CampaignStatusEnum.Archived.ordinal()) + ""))) {
								List<SmartCampaignPlacementObj> smartCampaignPlacementList = smartCampaignPlannerDAO
											.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
								if (smartCampaignPlacementList != null && smartCampaignPlacementList.size() > 0) {
									for(SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementList) {
										if (smartCampaignPlacementObj != null && smartCampaignPlacementObj.getDfpLineItemList() != null
												&& smartCampaignPlacementObj.getDfpLineItemList().size() > 0) {
											for(DFPLineItemDTO dfpLineItemDTO : smartCampaignPlacementObj.getDfpLineItemList()) {
												if (dfpLineItemDTO != null && dfpLineItemDTO.getLineItemId() > 0
														&& !(lineItemIdList.contains(dfpLineItemDTO.getLineItemId() + ""))) {
													lineItemIdList.add(dfpLineItemDTO.getLineItemId() + "");
													lineItemIds.append(dfpLineItemDTO.getLineItemId() + ",");
													lineItemPlacementMap.put(dfpLineItemDTO.getLineItemId(), smartCampaignPlacementObj);
													hasLineItemIds = true;
													thisBatchLineItemIdsCount++;
													if(thisBatchLineItemIdsCount >= 1000) {
														thisBatchLineItemIdsCount = 0;
														commaSeperatedLineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds.toString(), ",");
														lineItemIds.setLength(0);

														if(!isLineItemStatusUpdated(dfpReportService, dfpServices, dfpSession, lineItemPlacementMap,
																	commaSeperatedLineItemIds, summary, networkId)) {
															hasFailed = true;
														}
														lineItemPlacementMap.clear();
													}
												}
											}
										}
									}
								}
							}
						}
						commaSeperatedLineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds.toString(), ",");
						if(commaSeperatedLineItemIds != null && !(commaSeperatedLineItemIds.trim().isEmpty())) {
							if(!isLineItemStatusUpdated(dfpReportService, dfpServices, dfpSession, lineItemPlacementMap, 
										commaSeperatedLineItemIds, summary, networkId)) {
								hasFailed = true;
							}
						}
						log.info("total LineItems : "+lineItemIdList.size());
						if(hasFailed) {
							throw new Exception();
						}
						if(!hasLineItemIds) {
							log.warning("No DFP LineItem Ids for networkCode : " + networkId);
						}
					}else {
						log.warning("No campaigns for networkCode : " + networkId);
					}
				}
				summary.append("<br><br><br>");
			} else {
				log.severe("No credentials for networkCode : " + credentialKey);
				summary.append("No credentials for networkCode : " + credentialKey + "<br>");
			}
			// send mail
			// EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com",
			// LinMobileVariables.SENDER_EMAIL_ADDRESS,
			// "DFP LineItem Update Status - " +
			// LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
			status = "Success : " + summary.toString().replaceAll("<br>", " ");
		} catch (Exception e) {
			String errorMessage = "Exception generated in DFP LineItem Status Update : " + e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
			status = "Error : " + summary.toString().replaceAll("<br>", " ");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP LineItem Status Update - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return status;
	}
	
	private boolean isLineItemStatusUpdated(DFPReportService dfpReportService, DfpServices dfpServices, DfpSession dfpSession, 
				Map<Long, SmartCampaignPlacementObj> lineItemPlacementMap, String commaSeperatedLineItemIds, StringBuilder summary, String networkId) throws Exception {
		boolean result = dfpReportService.updatePlacementDetailFromDFP(dfpServices,dfpSession,commaSeperatedLineItemIds,summary, lineItemPlacementMap, networkId);
		if (result) {
			String successMessage = "DFP LineItem Status Updated Successfully for lineItemIds : "+commaSeperatedLineItemIds;
			log.info(successMessage);
			summary.append(successMessage + "<br>");
		} else {
			String errorMessage = "DFP LineItem Status Update Failed for lineItemIds : "+commaSeperatedLineItemIds;
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
		}
		return result;
	}

	/*public String updatePlacementDetailFromDFP(String networkId) {
		Map<Long, SmartCampaignPlacementObj> lineItemPlacementMap = new HashMap<>();
		String status = "";
		StringBuilder summary = new StringBuilder();
		try {
			DfpSession dfpSession = null;
			DfpServices dfpServices = null;
			String networkUsername = "";
			String commaSeperatedLineItemIds = "";
			Map<String, AdServerCredentialsDTO> map = DFPReportService.getNetWorkCredentials();
			String credentialKey = networkId.trim();
			if (map != null && map.size() > 0 && map.containsKey(credentialKey)) {
				AdServerCredentialsDTO adServerCredentialsDTO = map.get(credentialKey);
				if (adServerCredentialsDTO != null
						&& adServerCredentialsDTO.getAdServerId() != null
						&& adServerCredentialsDTO.getAdServerUsername() != null
						&& adServerCredentialsDTO.getAdServerPassword() != null) {
					ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
					networkUsername = adServerCredentialsDTO.getAdServerUsername();

					summary.append("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername + "<br>");
					log.info("For ADSERVER_ID : " + networkId + ", AD_SERVER_USERNAME : " + networkUsername);
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getAllCampaign(networkId);
					if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
						StringBuilder lineItemIds = new StringBuilder();
						List<String> lineItemIdList = new ArrayList<>();
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							if (smartCampaignObj != null && smartCampaignObj.getDfpOrderId() > 0 && !(smartCampaignObj.getCampaignStatus()
											.equals((CampaignStatusEnum.Archived.ordinal()) + ""))) {
								List<SmartCampaignPlacementObj> smartCampaignPlacementList = smartCampaignPlannerDAO
											.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
								if (smartCampaignPlacementList != null && smartCampaignPlacementList.size() > 0) {
									for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementList) {
										if (smartCampaignPlacementObj != null
												&& smartCampaignPlacementObj.getDfpLineItemList() != null
												&& smartCampaignPlacementObj.getDfpLineItemList().size() > 0) {
											for (DFPLineItemDTO dfpLineItemDTO : smartCampaignPlacementObj.getDfpLineItemList()) {
												if (dfpLineItemDTO != null
														&& dfpLineItemDTO.getLineItemId() > 0
														&& !(lineItemIdList.contains(dfpLineItemDTO.getLineItemId() + ""))) {
													lineItemIdList.add(dfpLineItemDTO.getLineItemId() + "");
													lineItemIds.append(dfpLineItemDTO.getLineItemId() + ",");
													lineItemPlacementMap.put(dfpLineItemDTO.getLineItemId(), smartCampaignPlacementObj);
												}
											}
										}
									}
								}
							}
						}
						commaSeperatedLineItemIds = StringUtil.deleteFromLastOccurence(lineItemIds.toString(), ",");
					}

					if (commaSeperatedLineItemIds != null && commaSeperatedLineItemIds.trim().length() > 0) {
						log.info(" now going to build dfpSession ...");
						dfpSession = DFPAuthenticationUtil.getDFPSession(networkId,LinMobileConstants.DFP_APPLICATION_NAME);
						log.info(" getting DfpServices instance from properties...");
						dfpServices = LinMobileProperties.getInstance().getDfpServices();

						DFPReportService dfpReportService = new DFPReportService();
						int i = 1;
						while (i < 3) {
							summary.append("Iteration " + i + "<br>");
							boolean result = dfpReportService.updatePlacementDetailFromDFP(dfpServices,dfpSession,commaSeperatedLineItemIds,summary, lineItemPlacementMap, networkId);
							if (result) {
								String successMessage = "DFP LineItem Status Updated Successfully";
								log.info(successMessage);
								summary.append(successMessage + "<br>");
								break;
							} else {
								if (i == 2) {
									String errorMessage = "DFP LineItem Status Update Failed";
									log.severe(errorMessage);
									summary.append(errorMessage + "<br>");
								}
							}
							i++;
						}
					} else {
						log.warning("No DFP LineItem Ids for networkCode : " + networkId);
					}
				}
				summary.append("<br><br><br>");
			} else {
				log.severe("No credentials for networkCode : " + credentialKey);
				summary.append("No credentials for networkCode : " + credentialKey + "<br>");
			}
			// send mail
			// EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com",
			// LinMobileVariables.SENDER_EMAIL_ADDRESS,
			// "DFP LineItem Update Status - " +
			// LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
			status = "Success : " + summary.toString().replaceAll("<br>", " ");
		} catch (Exception e) {
			String errorMessage = "Exception generated in DFP LineItem Status Update : " + e.getMessage();
			log.severe(errorMessage);
			summary.append(errorMessage + "<br>");
			status = "Error : " + summary.toString().replaceAll("<br>", " ");
			EmailUtil.sendAuthMail("naresh.pokhriyal@mediaagility.com", LinMobileVariables.SENDER_EMAIL_ADDRESS, "Exception in DFP LineItem Status Update - " + LinMobileVariables.APPLICATION_TYPE, summary.toString(), "");
		}
		return status;
	}*/

	@Override
	public String updateAllCampaignFromDFP() {
		String status = "";
		String campaignUpdateURL = "/updateCampaignDetailFromDFP.lin";
		String placementUpdateURL = "/updatePlacementDetailFromDFP.lin";
		try {
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getAllCampaign();
			if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
				Map adserverMap = DFPReportService.getNetWorkCredentials();
				if (adserverMap.size() > 0) {
					StringBuilder stringBuilder = new StringBuilder();
					for (Object obj : adserverMap.keySet()) {
						String adServerId = (String) obj;
						TaskQueueUtil.updateCampaignDetailFromDFP(campaignUpdateURL, adServerId);
						TaskQueueUtil.updateCampaignDetailFromDFP(placementUpdateURL, adServerId);
						String str = "Campaign and Placement update TaskQueue started for ADSERVER_ID : " + adServerId + ", Campaigns found : "+smartCampaignObjList.size()+" ";
						log.info(str);
						stringBuilder.append(str);
					}
					status = stringBuilder.toString();
				} else {
					status = "No Network Credentials for Campaigns in DataStore";
				}
			} else {
				status = "No campaign in DataStore";
			}
		} catch (Exception e) {
			status = "Exception generated in updateAllCampaignFromDFP : " + e.getMessage();
			log.severe(status);
		}
		return status;
	}
	
	/*@Override
	public String updateAllCampaignFromDFP() {
		String status = "";
		String campaignUpdateURL = "/updateCampaignDetailFromDFP.lin";
		String placementUpdateURL = "/updatePlacementDetailFromDFP.lin";
		try {
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			Map adserverMap = DFPReportService.getNetWorkCredentials();
			if (adserverMap.size() > 0) {
				StringBuilder stringBuilder = new StringBuilder();
				for (Object obj : adserverMap.keySet()) {
					String adServerId = (String) obj;
					String str = "Campaign and Placement update process started for ADSERVER_ID : " + adServerId + "  ";
					log.info(str);
					stringBuilder.append(str);
					List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO.getCampaignByAdserverId(adServerId);
					if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
						str = "Campaign found : "+smartCampaignObjList.size();
						log.info(str);
						stringBuilder.append(str);
						Map<Long, SmartCampaignPlacementObj> lineItemPlacementMap = new HashMap<>();
						StringBuilder lineItemIds = new StringBuilder();
						List<String> lineItemIdList = new ArrayList<>();
						for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
							if (smartCampaignObj != null && smartCampaignObj.getDfpOrderId() > 0 
										&& !(smartCampaignObj.getCampaignStatus().equals((CampaignStatusEnum.Archived.ordinal()) + ""))) {
								List<SmartCampaignPlacementObj> smartCampaignPlacementList = smartCampaignPlannerDAO.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
								if (smartCampaignPlacementList != null && smartCampaignPlacementList.size() > 0) {
									for (SmartCampaignPlacementObj smartCampaignPlacementObj : smartCampaignPlacementList) {
										if (smartCampaignPlacementObj != null && smartCampaignPlacementObj.getDfpLineItemList() != null
												&& smartCampaignPlacementObj.getDfpLineItemList().size() > 0) {
											for (DFPLineItemDTO dfpLineItemDTO : smartCampaignPlacementObj.getDfpLineItemList()) {
												if (dfpLineItemDTO != null && dfpLineItemDTO.getLineItemId() > 0
														&& !(lineItemIdList.contains(dfpLineItemDTO.getLineItemId() + ""))) {
													lineItemIdList.add(dfpLineItemDTO.getLineItemId() + "");
													lineItemIds.append(dfpLineItemDTO.getLineItemId() + ",");
													lineItemPlacementMap.put(dfpLineItemDTO.getLineItemId(), smartCampaignPlacementObj);
													if(lineItemIdList.size() >= 100) {
														// call Placement TaskQueue
														lineItemIdList
													}
												}
											}
										}
									}
								}
								
							}
						}
					} else {
						str = "No campaign in DataStore for ADSERVER_ID : " + adServerId;
						log.info(str);
						stringBuilder.append(str);
					}
					TaskQueueUtil.updateCampaignDetailFromDFP(campaignUpdateURL, adServerId);
					TaskQueueUtil.updateCampaignDetailFromDFP(placementUpdateURL, adServerId);
				}
				status = stringBuilder.toString();
			} else {
				status = "No Network Credentials for Campaigns in DataStore";
			}
		} catch (Exception e) {
			status = "Exception generated in updateAllCampaignFromDFP : " + e.getMessage();
			log.severe(status);
		}
		return status;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<SmartCampaignObj> loadAllCampaignsFromCache() {
		SmartCampaignPlannerDAO campaignDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignObj> campaignList = new ArrayList<SmartCampaignObj>();
		try {
			String AllCampaignKey = "ALL_CAMPAIGN_LIST_KEY";
			campaignList = (List<SmartCampaignObj>) MemcacheUtil
					.getObjectsListFromCache(AllCampaignKey);
			if (campaignList == null || campaignList.size() <= 0) {
				campaignList = campaignDAO.getAllCampaign();
				MemcacheUtil
						.setObjectsListInCache(campaignList, AllCampaignKey);
			}

		} catch (Exception e) {
			log.severe("Exception in loadAllCampaignsFromCache of SmartCampaignPlannerService : "
					+ e.getMessage());
			// e.printStackTrace();
		}
		return campaignList;
	}

	/*
	 * @author Youdhveer Panwar Load all dfp Order ids for a given dfp adserver
	 * (Network code) (non-Javadoc)
	 * 
	 * @see
	 * com.lin.web.service.ISmartCampaignPlannerService#loadAllOrderIdsFromDatastore
	 * (java.lang.String)
	 * 
	 * @param String networkCode
	 * 
	 * @return List<String> orderList
	 */
	public List<String> loadAllOrderIdsFromDatastore(String adServerNetworkCode) {
		log.info("Load dfp orderIds for adServerNetworkCode : "
				+ adServerNetworkCode);
		ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
		List<String> orderList = new ArrayList<String>();
		try {
			List<SmartCampaignObj> campaignList = smartCampaignPlannerDAO
					.getAllCampaign(adServerNetworkCode);
			if (campaignList != null && campaignList.size() > 0) {
				log.info("campaignList :" + campaignList.size());
				for (SmartCampaignObj campaign : campaignList) {
					long dfpOrderId = campaign.getDfpOrderId();
					if (dfpOrderId > 0) {
						String orderId = dfpOrderId + "";
						if (!orderList.contains(orderId)) {
							orderList.add(orderId);
						}
					} else {
						log.info("This campaign is not setup on dfp , campaignId : "
								+ campaign.getCampaignId());
					}
				}
				log.info("orderList : " + orderList);
			}
		} catch (DataServiceException e) {
			log.severe("Failed to load campaigns for adServerNetworkCode :"
					+ adServerNetworkCode + ", exception :" + e.getMessage());
		}
		return orderList;
	}

	/*
	 * @author Shubham Goel Update campaign status in Datastore
	 * 
	 * @see
	 * com.lin.web.service.ISmartCampaignPlannerService#changeCampaignStatus
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changeCampaignStatus(String campaignId, String status,
			long userId) throws Exception {
		log.info("In changeCampaignState method of SmartCampaignPlannerService... ");
		boolean isStatusChange = false;
		try {
			ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
			if (campaignId != null) {
				long campId = Long.parseLong(campaignId);
				SmartCampaignObj campaignObj = campaignPlannerDAO
						.getCampaignById(campId);
				if (campaignObj != null && status != null
						&& status.length() > 0) {
					Thread.sleep(1000);
					String previousStatus = campaignObj.getCampaignStatus();
					campaignObj.setCampaignStatus(status);
					campaignPlannerDAO
							.saveObjectWithStrongConsistancy(campaignObj);
					isStatusChange = true;
					log.info("*******************************************************************");
					log.info("Status to be changed from " + previousStatus
							+ " to " + status);
					log.info("Status to be changed from "
							+ LinMobileUtil.getCampaignStatusValue(StringUtil
									.getIntegerValue(previousStatus))
							+ " to "
							+ LinMobileUtil.getCampaignStatusValue(StringUtil
									.getIntegerValue(status)));

					String campaignHistoryLog = "Campaign status changed from "
							+ LinMobileUtil.getCampaignStatusValue(StringUtil
									.getIntegerValue(previousStatus))
							+ " to  "
							+ LinMobileUtil.getCampaignStatusValue(StringUtil
									.getIntegerValue(status));
					saveCampaignHistory(campaignObj.getCampaignId(),
							campaignObj.getName(), 0, "", userId,
							campaignHistoryLog);
				}
			}

		} catch (DataServiceException e) {
			log.severe("DataServiceException : changeCampaignState: "
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			log.severe("Exception : changeCampaignState: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return isStatusChange;
	}

	public static String updateSmartCampaignObj() {
		String msg = "";
		try {
			ISmartCampaignPlannerDAO smartCampaignPlannerDAO = new SmartCampaignPlannerDAO();
			List<SmartCampaignObj> smartCampaignObjList = smartCampaignPlannerDAO
					.getAllCampaign();
			if (smartCampaignObjList != null && smartCampaignObjList.size() > 0) {
				for (SmartCampaignObj smartCampaignObj : smartCampaignObjList) {
					// String companyId = smartCampaignObj.getPublisherId();
					// String companyName = smartCampaignObj.getPublisherName();
					// smartCampaignObj.setCompanyId(companyId);
					// smartCampaignObj.setCompanyName(companyName);
					smartCampaignPlannerDAO
							.saveObjectWithStrongConsistancy(smartCampaignObj);
				}
				msg = "SmartCampaignObj updated, count : "
						+ smartCampaignObjList.size();
			} else {
				msg = "SmartCampaignObj is Empty";
			}
		} catch (Exception e) {
			log.severe("Exception:" + e.getMessage());
			msg = "Exception:" + e.getMessage();
			e.printStackTrace();
		}
		log.info(msg);
		return msg;
	}

	public String getCampaignLevelHistory(SmartCampaignObj earlierObj,
			SmartCampaignObj updatedObj) {
		StringBuilder campaignLevelLog = new StringBuilder();
		log.info("In getCampaignLevelHistory...");
		try {
			// Campaign History logs
			if (!earlierObj.getName().equals(updatedObj.getName())) {
				campaignLevelLog.append(LinMobileUtil.changeFromToLog(
						"Campaign Name", earlierObj.getName(),
						updatedObj.getName()));
			}
			if (!earlierObj.getRateTypeList().equals(
					updatedObj.getRateTypeList())) {
				campaignLevelLog.append(LinMobileUtil.changeFromToLog(
						"Rate Type", earlierObj.getRateTypeList().get(0)
								.getValue(), updatedObj.getRateTypeList()
								.get(0).getValue()));
			}
			if (!earlierObj.getStartDate().equals(updatedObj.getStartDate())
					|| !earlierObj.getEndDate().equals(updatedObj.getEndDate())) {
				campaignLevelLog.append(LinMobileUtil.changeFromToLog(
						"Campaign Duration",
						earlierObj.getStartDate() + " / "
								+ earlierObj.getEndDate(),
						updatedObj.getStartDate() + " / "
								+ updatedObj.getEndDate()));
			}
			if (!earlierObj.getNotes().equals(updatedObj.getNotes())) {
				campaignLevelLog.append(LinMobileUtil.changeFromToLog("Notes",
						earlierObj.getNotes(), updatedObj.getNotes()));
			}
			if (!earlierObj.getAdvertiserId().equals(
					updatedObj.getAdvertiserId())) {
				IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
				AdvertiserObj earlierAdvertiser = mediaPlanDAO
						.loadAdvertiser(StringUtil.getLongValue(earlierObj
								.getAdvertiserId()));
				AdvertiserObj updatedAdvertiser = mediaPlanDAO
						.loadAdvertiser(StringUtil.getLongValue(updatedObj
								.getAdvertiserId()));
				if (earlierAdvertiser == null) {
					earlierAdvertiser = new AdvertiserObj();
				}
				if (updatedAdvertiser == null) {
					updatedAdvertiser = new AdvertiserObj();
				}
				campaignLevelLog.append(LinMobileUtil.changeFromToLog(
						"Advertiser", earlierAdvertiser.getName(),
						updatedAdvertiser.getName()));
			}
			if (!earlierObj.getAgencyId().equals(updatedObj.getAgencyId())) {
				IMediaPlanDAO mediaPlanDAO = new MediaPlanDAO();
				AgencyObj earlierAgency = mediaPlanDAO.loadAgency(StringUtil
						.getLongValue(earlierObj.getAgencyId()));
				AgencyObj updatedAgency = mediaPlanDAO.loadAgency(StringUtil
						.getLongValue(updatedObj.getAgencyId()));
				if (earlierAgency == null) {
					earlierAgency = new AgencyObj();
				}
				if (updatedAgency == null) {
					updatedAgency = new AgencyObj();
				}
				campaignLevelLog.append(LinMobileUtil.changeFromToLog("Agency",
						earlierAgency.getName(), updatedAgency.getName()));
			}

		}catch(Exception e){
			log.info("Exception found in generateCampaignHistory()..."+e.toString());

		}
		return campaignLevelLog.toString();
	}

	public static void saveCampaignHistory(long campaignId,
			String campaignName, long placementId, String placementName,
			long userId, String campaignHistoryLog) {
		log.info("campaignHistoryLog : " + campaignHistoryLog);
		IUserDetailsDAO userDAO = new UserDetailsDAO();
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		try {
			UserDetailsObj userDetailObj = userDAO.getUserById(userId);
			SmartCampaignHistObj history = new SmartCampaignHistObj();
			history.setCampaignId(campaignId);
			history.setCampaignName(campaignName);
			history.setPlacementId(placementId);
			history.setPlacementName(placementName);
			history.setUpdatedByUserId(userId);
			if (userDetailObj != null) {
				history.setUpdatedByUserName(userDetailObj.getUserName());
			}
			history.setChangeLog(campaignHistoryLog);
			history.setUpdatedOn(DateUtil.getDate(new Date(),
					"yyyy-MM-dd HH:mm:ss"));

			campaignPlannerDAO.saveObjectWithStrongConsistancy(history);
			log.info("History made successfully for campaignId : " + campaignId);
			MemcacheUtil.flushCache(MemcacheUtil.CAMPAIGN_HISTORY_KEY
					+ campaignId);
			log.info("Cache removed......");
		} catch (Exception e) {
			log.severe("Exception in saveCampaignHistory : " + e.getMessage());
		}
	}

	@Override
	public JSONObject loadCampaignHistory(long campaignId, long limit,
			long offset) throws Exception {
		boolean isStart = true;
		boolean isEnd = true;
		if (limit <= 0) {
			limit = 10;
		}
		if (offset <= 0) {
			offset = 1;
		}
		log.info("In loadCampaignHistory, campaignId : " + campaignId
				+ ", limit : " + limit + ", offset : " + offset);
		JSONObject historyJsonObject = new JSONObject();
		String key = MemcacheUtil.CAMPAIGN_HISTORY_KEY + campaignId;
		JSONArray jsonArray = (JSONArray) MemcacheUtil.getObjectFromCache(key);
		if (jsonArray != null && jsonArray.size() > 0) {
			log.info("Found in memcache.");
		} else {
			if (offset > 1) {
				offset = 1;
				log.info("Offset reset to " + offset);
			}
			jsonArray = new JSONArray();
			ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
			List<SmartCampaignHistObj> histObjList = plannerDAO
					.getCampaignHistByCampaignId(campaignId);
			if (histObjList != null && histObjList.size() > 0) {
				log.info("history List size : " + histObjList.size());
				for (SmartCampaignHistObj smartCampaignHistObj : histObjList) {
					JSONObject jsonObject = new JSONObject();
					String scope = "Campaign:<br>"
							+ smartCampaignHistObj.getCampaignName();
					if (smartCampaignHistObj.getPlacementName() != null
							&& smartCampaignHistObj.getPlacementName().length() > 0) {
						scope = scope + "<br>Placement:<br>"
								+ smartCampaignHistObj.getPlacementName();
					}
					jsonObject.put("changeDate", DateUtil.getFormatedDate(
							smartCampaignHistObj.getUpdatedOn(),
							"yyyy-MM-dd hh:mm a"));
					jsonObject.put("user",
							smartCampaignHistObj.getUpdatedByUserName());
					jsonObject.put("scope", scope);
					jsonObject.put("change",
							smartCampaignHistObj.getChangeLog());
					jsonArray.add(jsonObject);
				}
				if (jsonArray.size() > 0) {
					MemcacheUtil.setObjectInCache(key, jsonArray, 60 * 60);
					log.info("Memcache created of size : " + jsonArray.size()
							+ ", for key : " + key);
				}
			} else {
				log.info("No history found for campaignId : " + campaignId);
			}
		}

		JSONArray returnJsonArray = new JSONArray();
		int start = (int) ((offset - 1) * limit);
		int end = (int) ((offset * limit) - 1);
		log.info("start : " + start + ", end : " + end);
		if (jsonArray.size() > 0) {
			for (int i = start; i <= end; i++) {
				if (jsonArray.size() > i) {
					returnJsonArray.add(jsonArray.get(i));
				}
			}
			log.info("Return JSON size : " + returnJsonArray.size());
			start++;
			if (start > 1) {
				isStart = false;
			}
			end++;
			if (end >= jsonArray.size()) {
				end = jsonArray.size();
			} else {
				isEnd = false;
			}
		}
		historyJsonObject.put("campaignHistoryData", returnJsonArray);
		historyJsonObject.put("campaignHistoryCountLabel", start + " - " + end
				+ " of " + jsonArray.size());
		historyJsonObject.put("isStart", isStart);
		historyJsonObject.put("isEnd", isEnd);
		return historyJsonObject;
	}

	@Override
	public JSONObject searchCampaignsJSON(List<SmartCampaignObj> smartCampaignObjs, String companyId, boolean superAdmin, String searchText, long userId) throws Exception {
		List<SmartCampaignObj> campaignList = null;
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		int count = 0;
		if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
			if(superAdmin) {
				campaignList = smartCampaignObjs;
			}else {
				ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
				/*log.info("Fetching accessible Accounts");
				List<String> accountIdList = new ArrayList<>();
				Map<String,String> accountDataMap = null;
				IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
				accountDataMap = userService.getSelectedAccountsByUserId(userId, true, false);
				if(accountDataMap != null && accountDataMap.size() > 0) {
					for(String accountId : accountDataMap.keySet()) {
						accountIdList.add(accountId.replace("_", ""));
					}
				}
				log.info("Accessible Accounts count : "+accountIdList.size());
				campaignList = campaignPlannerDAO.searchCampaignsInDataStore(accountIdList, companyId);*/
				campaignList = campaignPlannerDAO.getCampaignsByCompanyId(companyId);
			}
			if(campaignList != null && campaignList.size() > 0) {
				for (SmartCampaignObj smartCampaignObj : campaignList) {
					if(smartCampaignObj.getName().toLowerCase().contains(searchText)) {
						JSONObject json=new JSONObject();					  
						json.put("id", smartCampaignObj.getCampaignId());
						json.put("name", smartCampaignObj.getName());
						jsonArray.add(json);
						count++;
					}
					if(count==20) break;
				}
			}else {
				throw new Exception("No campaign belongs to user, company : "+companyId);
			}
		}else {
			throw new Exception("No campaign in DataStore");
		}
		jsonObject.put("campaigns", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}
	
	@Override
	public JSONObject searchPlacementsJSON(List<SmartCampaignObj> smartCampaignObjs, String companyId, boolean superAdmin, String searchText, long userId) throws Exception {
		List<SmartCampaignObj> campaignList = null;
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
			if(superAdmin) {
				campaignList = smartCampaignObjs;
			}else {
				ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
				/*log.info("Fetching accessible Accounts");
				List<String> accountIdList = new ArrayList<>();
				Map<String,String> accountDataMap = null;
				IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
				accountDataMap = userService.getSelectedAccountsByUserId(userId, true, false);
				if(accountDataMap != null && accountDataMap.size() > 0) {
					for(String accountId : accountDataMap.keySet()) {
						accountIdList.add(accountId.replace("_", ""));
					}
				}
				log.info("Accessible Accounts count : "+accountIdList.size());
				campaignList = campaignPlannerDAO.searchCampaignsInDataStore(accountIdList, companyId);*/
				campaignList = campaignPlannerDAO.getCampaignsByCompanyId(companyId);
			}
			if(campaignList != null && campaignList.size() > 0) {
				log.info("Campaigns for user : "+campaignList.size());
				ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
				int count = 0;
				for (SmartCampaignObj smartCampaignObj : campaignList) {
					List<SmartCampaignPlacementObj> placementObjs = plannerDAO.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
					if(placementObjs != null && placementObjs.size() > 0) {
						for(SmartCampaignPlacementObj campaignPlacementObj : placementObjs) {
							if(campaignPlacementObj != null && campaignPlacementObj.getPlacementName() != null && campaignPlacementObj.getPlacementName().toLowerCase().contains(searchText)) {
								JSONObject json=new JSONObject();					  
								json.put("id", campaignPlacementObj.getPlacementId());
								json.put("name", campaignPlacementObj.getPlacementName());
								jsonArray.add(json);
								count++;
							}
							if(count==20) break;
						}
					}
					if(count==20) break;
				}
				log.info("Filtered placements : "+jsonArray.size());
			}else {
				log.info("No campaign belongs to user, company : "+companyId);
			}
		}else {
			log.info("No campaign in DataStore");
		}
		jsonObject.put("placements", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}
	
	@Override
	public JSONObject selectedPlacementsJSON(List<SmartCampaignObj> smartCampaignObjs) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(smartCampaignObjs != null && smartCampaignObjs.size() > 0) {
			ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
			for (SmartCampaignObj smartCampaignObj : smartCampaignObjs) {
				List<SmartCampaignPlacementObj> placementObjs = plannerDAO.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
				if(placementObjs != null && placementObjs.size() > 0) {
					for(SmartCampaignPlacementObj campaignPlacementObj : placementObjs) {
						JSONObject json=new JSONObject();					  
						json.put("id", campaignPlacementObj.getPlacementId());
						json.put("name", campaignPlacementObj.getPlacementName());
						jsonArray.add(json);
					}
				}
			}
		}else {
			log.info("No campaign in DataStore");
		}
		jsonObject.put("placements", jsonArray);
		log.info("jsonObject : "+jsonObject);
		return jsonObject;
	}

	@Override
	public List<SmartCampaignObj> loadAllCampaignsByIds(String commaSeperatedCampaignIds) throws Exception {
		log.info("commaSeperatedCampaignIds : "+commaSeperatedCampaignIds);
		List<Long> campaignIdList = StringUtil.commaSeperatedToNumericList(commaSeperatedCampaignIds);
		ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignObj> list = plannerDAO.loadAllCampaignsByIds(campaignIdList);
		if(list == null || list.size() == 0) {
			return new ArrayList<SmartCampaignObj>();
		}
		return list;
	}
	
	@Override
	public List<SmartCampaignPlacementObj> loadAllPlacementsByIds(String commaSeperatedPlacementIds) throws Exception {
		log.info("commaSeperatedPlacementIds : "+commaSeperatedPlacementIds);
		List<Long> placementIdList = StringUtil.commaSeperatedToNumericList(commaSeperatedPlacementIds);
		ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignPlacementObj> list = plannerDAO.loadAllPlacementsByIds(placementIdList);
		if(list == null || list.size() == 0) {
			return new ArrayList<SmartCampaignPlacementObj>();
		}
		return list;
	}

	@Override
	/**
	 * @author Naresh Pokhriyal<br />
	 * @param campaignIds : comma seperated campaignIds. Null or empty if no campaign is selected
	 * @param placementIds : comma seperated placementIds. Null or empty if no placement is selected.
	 * @param partners : comma seperated partner ids. Null or empty if no partner is selected.
	 * @param accounts : comma seperated accounts= ids. Null or empty if no accounts is selected.
	 * @param reportType : Day, week or month.
	 * @param startDate : startDate for report.
	 * @param endDate : endDate for report.
	 * @param company : company of the user. Null or empty if user is superAdmin.
	 * @param superAdmin : true if user is super admin.
	 * @param publisherBQId : Big Query project Id.
	 * @param userId : user's id.
	 * @return {@link JSONObject} Returns JSONObject.
	 */
	public JSONObject runCampaignReport(String campaignIds, String placementIds, String partners, String accounts,
			String reportType, String startDate, String endDate, String company, boolean superAdmin, String publisherBQId, long userId) throws Exception {
		log.info("In runCampaignReport...");
		/*startDate = DateUtil.getFormatedDate(startDate, "MM-dd-yyyy", "yyyy-MM-dd");
		endDate = DateUtil.getFormatedDate(endDate, "MM-dd-yyyy", "yyyy-MM-dd");*/
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = null;
		Map<String,String> placementIdCampaignName = new HashMap<>();
		
		Map<String,String> placementIdPlacementName = new HashMap<>();
		Map<String,String> placementIdAdvertiserId = new HashMap<>();
		Map<String,String> placementIdRate = new HashMap<>();
		Map<String,String> lineItemIdPlacementId = new HashMap<>();
		Map<String,String> placementIdPartnerName = new HashMap<>();
		List<SmartCampaignPlacementObj> placementObjs = new ArrayList<>();
		List<Long> campaignIdList = StringUtil.commaSeperatedToNumericList(campaignIds);
		List<Long> placementIdList = StringUtil.commaSeperatedToNumericList(placementIds);
		StringBuilder lineItemsForReport = new StringBuilder();
		
		ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		
		List<String> partnerNameList = getPartnerNameList(partners, true);
		//Map<String,String> accountDataMap = null;
		List<String> advertiserList = StringUtil.commaSeperatedToList(accounts);
		if(advertiserList == null || advertiserList.size() ==0) {
			/*IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
			accountDataMap = userService.getSelectedAccountsByUserId(userId, true, false);*/
			log.info("No account was selected");
		}
		
		try {
			List<SmartCampaignObj> campaignList = null;
			if(campaignIds != null && campaignIds.trim().length() > 0) {
				log.info("Campaigns were selected");
				campaignList = plannerDAO.loadAllCampaignsByIds(campaignIdList);
			} else {
				log.info("No campaign were selected");
				campaignList = new ArrayList<>();
				List<SmartCampaignObj> smartCampaignObjs = loadAllCampaignsFromCache();
				if(superAdmin) {
					campaignList = smartCampaignObjs;
				}else {
					campaignList = new ArrayList<>();
					for (SmartCampaignObj smartCampaignObj : smartCampaignObjs) {
						if(smartCampaignObj.getCompanyId().equals(company)) {
							campaignList.add(smartCampaignObj);
						}
					}
				}
			}
			
			if(campaignList != null && campaignList.size() > 0) {
				for (SmartCampaignObj smartCampaignObj : campaignList) {
					if(advertiserList == null || advertiserList.size() == 0 || advertiserList.contains(smartCampaignObj.getAdvertiserId())) {
					/*if((advertiserList != null && advertiserList.size() > 0 && advertiserList.contains(smartCampaignObj.getAdvertiserId())) || 
							(accountDataMap != null && accountDataMap.containsKey("_"+smartCampaignObj.getAdvertiserId()))) {*/
						List<SmartCampaignPlacementObj> placementList = plannerDAO.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
						if(placementList != null && placementList.size() > 0) {
							if(placementIdList != null && placementIdList.size() > 0) {
								for(SmartCampaignPlacementObj campaignPlacementObj : placementList) {
									if(placementIdList.contains(campaignPlacementObj.getPlacementId())) {
										placementObjs.add(campaignPlacementObj);
										placementIdCampaignName.put(campaignPlacementObj.getPlacementId()+"", smartCampaignObj.getName());
										placementIdAdvertiserId.put(campaignPlacementObj.getPlacementId()+"", smartCampaignObj.getAdvertiserId());
									}
								}
							}else {
								for(SmartCampaignPlacementObj campaignPlacementObj : placementList) {
									placementObjs.add(campaignPlacementObj);
									placementIdCampaignName.put(campaignPlacementObj.getPlacementId()+"", smartCampaignObj.getName());
									placementIdAdvertiserId.put(campaignPlacementObj.getPlacementId()+"", smartCampaignObj.getAdvertiserId());
								}
							}
						}
					}
				}
			}else {
				log.info("No campaign belongs to user, company : "+company);
			}
			
			if(placementObjs != null && placementObjs.size() > 0) {
				log.info("After account check placementObjs size : "+placementObjs.size());
				boolean partnerMatched = false;
				for (SmartCampaignPlacementObj campaignPlacementObj : placementObjs) {
					partnerMatched = false;
					List<DFPLineItemDTO> dfpLineItemDTOs = campaignPlacementObj.getDfpLineItemList();
					String lineItems = "";
					if(dfpLineItemDTOs != null && dfpLineItemDTOs.size() > 0) {
						for(DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOs) {
							lineItems = lineItems + dfpLineItemDTO.getLineItemId()+"','";
							if(partnerNameList != null && partnerNameList.size() > 0 && dfpLineItemDTO.getPartner() != null && partnerNameList.contains(dfpLineItemDTO.getPartner().toLowerCase())) {
								partnerMatched = true;
							}
							lineItemIdPlacementId.put(dfpLineItemDTO.getLineItemId()+"", campaignPlacementObj.getPlacementId()+"");
							placementIdPartnerName.put(campaignPlacementObj.getPlacementId()+"", dfpLineItemDTO.getPartner());
						}
					}
					if(partnerMatched || partnerNameList == null || partnerNameList.size() == 0) {
						lineItemsForReport.append(lineItems);
					}
					placementIdPlacementName.put(campaignPlacementObj.getPlacementId()+"", campaignPlacementObj.getPlacementName());
					placementIdRate.put(campaignPlacementObj.getPlacementId()+"", campaignPlacementObj.getRate());
				}
			} else {
				log.info("No placement left after account check. Report wiil be empty");
			}
			
			if(lineItemsForReport != null && lineItemsForReport.length() > 0) {
				QueryResponse queryResponse = null;
				QueryDTO queryDTO=BigQueryUtil.getQueryDTO(startDate, endDate, publisherBQId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					queryResponse = plannerDAO.campaignReportData(StringUtil.deleteFromLastOccurence(lineItemsForReport.toString(), "','"), queryDTO, startDate+" 00:00:00", endDate+" 00:00:00");
				}else {
					log.warning("Empty queryDTO : "+queryDTO);
				}
				jsonArray = createCampaignReportData(queryResponse, lineItemIdPlacementId, userId, reportType, placementIdCampaignName, placementIdPlacementName,
						placementIdPartnerName, placementIdAdvertiserId, placementIdRate);								//  createCampaignReportData
				String title = "Publisher Billing Report";
				if(jsonArray != null && jsonArray.size() > 0) {
					title = title +"_"+ startDate +"_"+ endDate+"_"+ StringUtil.getSeperatedValues(partnerNameList, "_");
				}
				jsonObject.put("title", title.replaceAll(" ", "_"));
			} else {
				log.info("No lineItems left. Report wiil be empty");
			}
		}catch (JSONException e) {
			log.severe("JSONException in runCampaignReport of SmartCampaignPlannerService : "+e.getMessage());
			jsonObject.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in runCampaignReport of SmartCampaignPlannerService : "+e.getMessage());
			jsonObject.put("error", e.getMessage());
			e.printStackTrace();
		}
		jsonObject.put("reportMetrics", jsonArray);
		return jsonObject;
		
	}
	
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * support method for runCampaignReport. formats and groups data according to report type after fetching data from Big Query.
	 * @return {@link JSONArray} Returns JSONArray.
	 */
	private JSONArray createCampaignReportData(QueryResponse queryResponse, Map<String,String> lineItemIdPlacementId, long userId, String reportType,
			Map<String, String> placementIdCampaignName, Map<String, String> placementIdPlacementName, Map<String, String> placementIdPartnerName,
			Map<String, String> placementIdAdvertiserId, Map<String, String> placementIdRate) throws Exception {
		JSONArray jsonArray = new JSONArray();
		log.info("In createCampaignReportData....");
		if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0) {
			String weekStartDate = "";
			Map<String, LineChartDTO> placementMap = new HashMap<>();
			List<TableRow> rowList = queryResponse.getRows();
			IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
			Map<String,String> advertiserMap = userService.getSelectedAccountsByUserId(userId, true, false);
			if(advertiserMap == null) { advertiserMap = new HashMap<>();}
			int thisWeekEndDateValue = 0;
			int thisMonthValue = 0;
			String previousDate = "";
			for (TableRow row : rowList) {
			     if(row != null && row.getF() != null && row.getF().size() > 0) {
			    	 List<TableCell> cellList = row.getF();
			    	 long impressions = StringUtil.getLongValue(cellList.get(0).getV().toString());
			    	 long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
			    	 String lineItemId = cellList.get(2).getV().toString();
			    	 String date=cellList.get(3).getV().toString();
			    	 String placementId = lineItemIdPlacementId.get(lineItemId);
			    	 
			    	 int dateValue=Integer.parseInt(date.replaceAll("-", ""));
			    	 int monthValue=Integer.parseInt(date.split("-")[1]);
			    	 
			    	 if(reportType.equalsIgnoreCase("week")) {													// Week wise Data
			    		 if(thisWeekEndDateValue == 0 || dateValue > thisWeekEndDateValue) {
							if(thisWeekEndDateValue != 0 && dateValue > thisWeekEndDateValue) {
								for(String weekPlacementId : placementMap.keySet()) {
									LineChartDTO chartDTO = placementMap.get(weekPlacementId);
									if(chartDTO != null) {
										long imp = chartDTO.getImp();
										long clk = chartDTO.getClk();
										JSONObject json = createJsonObjectForCampaignReport(imp, clk, weekStartDate+" to "+previousDate, weekPlacementId, placementIdPlacementName, placementIdPartnerName,
								    			 placementIdCampaignName, placementIdAdvertiserId, advertiserMap, placementIdRate);
								    	 jsonArray.add(json);
									}
								}
								placementMap = new HashMap<>();
							}
							weekStartDate = date;
							int thisDayNumber = DateUtil.getDayOfDate(date, "yyyy-MM-dd");
							thisWeekEndDateValue = Integer.parseInt((DateUtil.getModifiedDateStringByDays(date, (8-thisDayNumber), "yyyy-MM-dd")).replaceAll("-", ""));
						}
						if(placementMap.containsKey(placementId) && placementMap.get(placementId) != null) {
							LineChartDTO chartDTO = placementMap.get(placementId);
							long imp = chartDTO.getImp();
							long clk = chartDTO.getClk();
							chartDTO.setImp(imp + impressions);
							chartDTO.setClk(clk + clicks);
							placementMap.put(placementId, chartDTO);
						}else {
							LineChartDTO chartDTO = new LineChartDTO();
							chartDTO.setClk(clicks);
							chartDTO.setImp(impressions);
							placementMap.put(placementId, chartDTO);
						}
			    	 }
			    	 else if(reportType.equalsIgnoreCase("month")) {												// month wise data
			    		 if(thisMonthValue == 0 || thisMonthValue != monthValue) {				
								if(thisMonthValue != 0 && thisMonthValue != monthValue) {
									for(String monthPlacementId : placementMap.keySet()) {
										LineChartDTO chartDTO = placementMap.get(monthPlacementId);
										if(chartDTO != null) {
											long imp = chartDTO.getImp();
											long clk = chartDTO.getClk();
											JSONObject json = createJsonObjectForCampaignReport(imp, clk, DateUtil.getMonthName(previousDate, "yyyy-MM-dd", true), monthPlacementId, 
													placementIdPlacementName, placementIdPartnerName, placementIdCampaignName, placementIdAdvertiserId, advertiserMap, placementIdRate);
									    	 jsonArray.add(json);
										}
									}
									placementMap = new HashMap<>();
								}
								thisMonthValue = monthValue;
							}
							if(placementMap.containsKey(placementId) && placementMap.get(placementId) != null) {
								LineChartDTO chartDTO = placementMap.get(placementId);
								long imp = chartDTO.getImp();
								long clk = chartDTO.getClk();
								chartDTO.setImp(imp + impressions);
								chartDTO.setClk(clk + clicks);
								placementMap.put(placementId, chartDTO);
							}else {
								LineChartDTO chartDTO = new LineChartDTO();
								chartDTO.setClk(clicks);
								chartDTO.setImp(impressions);
								placementMap.put(placementId, chartDTO);
							}
			    	 }else {																			// day wise data
			    		 JSONObject json = createJsonObjectForCampaignReport(impressions, clicks, date, placementId, placementIdPlacementName, placementIdPartnerName,
				    			 placementIdCampaignName, placementIdAdvertiserId, advertiserMap, placementIdRate);
				    	 jsonArray.add(json);
			    	 }
			    	 
			    	 previousDate = date;
			     }
			}
			// add week, month data which was left to add.
			log.info("Adding week, month data which was left to add, jsonArray size : "+jsonArray.size());
			if(reportType.equalsIgnoreCase("week") || reportType.equalsIgnoreCase("month")) {
				String date = ""; 
				for(String weekPlacementId : placementMap.keySet()) {
					LineChartDTO chartDTO = placementMap.get(weekPlacementId);
					if(chartDTO != null) {
						long imp = chartDTO.getImp();
						long clk = chartDTO.getClk();
						if(reportType.equalsIgnoreCase("month")) {
							date = DateUtil.getMonthName(previousDate, "yyyy-MM-dd", true);
						} else {
							date = weekStartDate+" to "+previousDate;
						}
						JSONObject json = createJsonObjectForCampaignReport(imp, clk, date, weekPlacementId, placementIdPlacementName, placementIdPartnerName,
				    			 placementIdCampaignName, placementIdAdvertiserId, advertiserMap, placementIdRate);
						jsonArray.add(json);
					}
				}
			}
		} else {
			log.info("Query response is empty or null.");
		}
		log.info("jsonArray size : "+jsonArray.size());
		return jsonArray;
	}

	/**
	 * @author Naresh Pokhriyal<br />
	 * support method for createCampaignReportData. Creates a formatted row by the data passed as parameters.
	 * @return {@link JSONObject} Returns JSONObject.
	 */
	private JSONObject createJsonObjectForCampaignReport(long impressions, long clicks, String date, String placementId,
			Map<String, String> placementIdPlacementName, Map<String, String> placementIdPartnerName, Map<String, String> placementIdCampaignName,
			Map<String, String> placementIdAdvertiserId, Map<String, String> advertiserMap, Map<String, String> placementIdRate) {
		DecimalFormat df2 = new DecimalFormat( "###,###,###,##0.00" );
		DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
			
		String placementName = placementIdPlacementName.get(placementId);
	   	String partner = placementIdPartnerName.get(placementId);
	   	String campaignName = placementIdCampaignName.get(placementId);
	   	String advertiserName = advertiserMap.get("_"+(placementIdAdvertiserId.get(placementId)));
	   	double rate = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(placementIdRate.get(placementId)));
	   	double ctr = (double) (clicks*100)/impressions;
	   	double cost = (double) (impressions*rate)/1000;
	   	
	   	JSONObject jsonObject = new JSONObject();
	   	jsonObject.put("date", date);
	   	jsonObject.put("partner", partner);
	   	jsonObject.put("advertiserName", advertiserName);
	   	jsonObject.put("campaignName", campaignName);
	   	jsonObject.put("placementName", placementName);
	   	jsonObject.put("impressions", lf.format(impressions));
	   	jsonObject.put("clicks", lf.format(clicks));
	   	jsonObject.put("ctr", df2.format(ctr)+"%");
	   	jsonObject.put("rate", "$"+df2.format(rate));
	   	jsonObject.put("cost", "$"+df2.format(cost));
	   	
	   	return jsonObject;
	}
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * @param commaSeperatedPartnerIds : comma seperated Partner ids.
	 * @param toLowerCase : if true then the names wiil be returned in lower case. 
	 * @return Returns List<String> as partner names of the partner ids passed.
	 */
	private List<String> getPartnerNameList(String commaSeperatedPartnerIds, boolean toLowerCase) throws Exception {
		List<String> partnerNameList = new ArrayList<>();
		if(commaSeperatedPartnerIds != null && commaSeperatedPartnerIds.length() > 0) {
			IUserDetailsDAO userDAO = new UserDetailsDAO();
			List<Long> PartnerIdList = StringUtil.commaSeperatedToNumericList(commaSeperatedPartnerIds);
			List<CompanyObj> partnerList=userDAO.getAllPublishers(MemcacheUtil.getAllCompanyList());
			if(partnerList != null && partnerList.size() > 0 && PartnerIdList != null && PartnerIdList.size() > 0)
				for(long partnerId : PartnerIdList) {
					for(CompanyObj companyObj : partnerList) {
						if(companyObj.getId() == partnerId) {
								if(toLowerCase) {
									partnerNameList.add(companyObj.getCompanyName().toLowerCase());
								}else {
									partnerNameList.add(companyObj.getCompanyName());
								}
						}
					}
				}
		}
		return partnerNameList;
	}
	
	@Override
	/**
	 * @author Naresh Pokhriyal<br />
	 * Creates billing report formatted data 
	 * @param campaignIds : comma seperated campaignIds. Null or empty if no campaign is selected
	 * @param partners : comma seperated partner ids. Null or empty if no partner is selected.
	 * @param startDate : startDate for report.
	 * @param endDate : endDate for report.
	 * @param company : company of the user. Null or empty if user is superAdmin.
	 * @param superAdmin : true if user is super admin.
	 * @param publisherBQId : Big Query project Id.
	 * @param userId : user's id.
	 * @return {@link JSONObject} Returns JSONObject.
	 */
	public JSONObject billingReport(String campaignIds, String partners, String startDate, String endDate, String companyId, boolean isSuperAdmin, String publisherBQId, long userId) {
		log.info("In billingReport...");
		ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		IPerformanceMonitoringService performanceService = (IPerformanceMonitoringService) BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
		
		JSONObject jsonObject = new JSONObject();
		JSONArray partnerInfoJsArr = null;
		
		List<Long> campaignIdList = StringUtil.commaSeperatedToNumericList(campaignIds);
		StringBuilder lineItemsForReport = new StringBuilder();
		/*IUserService userService = (IUserService) BusinessServiceLocator.locate(IUserService.class);
		Map<String,String> accountDataMap = null;*/
		Map<String,String> lineItemIdPartnerNameMap = new HashMap<>();
		Map<String,List<SmartCampaignFlightDTO>> campaignFlightMap = new HashMap<>();		// key : orderId
		Map<String,JSONObject> campaignInfoMap = new HashMap<>();		// key : orderId
		Map<String,JSONObject> orderDataMap = new HashMap<>();		// key : orderId, value : JSONObject(impressions and clicks)
		//List<String> accountIdList = new ArrayList<>();
		try {
			List<String> partnerNameList = getPartnerNameList(partners, true);
			/*log.info("Fetching accessible Accounts");
			accountDataMap = userService.getSelectedAccountsByUserId(userId, true, false);
			if(accountDataMap != null && accountDataMap.size() > 0) {
				for(String accountId : accountDataMap.keySet()) {
					accountIdList.add(accountId.replace("_", ""));
				}
			}
			log.info("Accessible Accounts count : "+accountIdList.size());*/
			
			List<SmartCampaignObj> campaignList = null;
			if(campaignIdList != null && campaignIdList.size() > 0) {
				log.info("Campaigns were selected");
				//campaignList = plannerDAO.searchCampaignsInDataStore(accountIdList, campaignIdList);
				campaignList = plannerDAO.loadAllCampaignsByIds(campaignIdList);
			} else {
				log.info("No campaign were selected");
				if(isSuperAdmin) {
					campaignList = loadAllCampaignsFromCache();
				}else {
					//campaignList = plannerDAO.searchCampaignsInDataStore(accountIdList, companyId);
					campaignList = plannerDAO.getCampaignsByCompanyId(companyId);
				}
			}
			if(campaignList != null && campaignList.size() > 0) {
				boolean partnerMatched = false;
				for (SmartCampaignObj smartCampaignObj : campaignList) {
					if(smartCampaignObj != null && smartCampaignObj.getDfpOrderId() > 0) {
						String campaignType = "";
						if(smartCampaignObj.getRateTypeList() != null && smartCampaignObj.getRateTypeList().size() > 0) {
							if(smartCampaignObj.getRateTypeList().get(0).getValue().contains("CPC")) {
								campaignType = "CPC";
							}else if(smartCampaignObj.getRateTypeList().get(0).getValue().contains("CPD")) {
								campaignType = "CPD";
							}else if(smartCampaignObj.getRateTypeList().get(0).getValue().contains("CPM")) {
								campaignType = "CPM";
							}
						}
						List<SmartCampaignPlacementObj> placementList = plannerDAO.getAllPlacementOfCampaign(smartCampaignObj.getCampaignId());
						if(placementList != null && placementList.size() > 0) {
							long goal = 0;
							double budget = 0.0;
							List<SmartCampaignFlightDTO> flightList = null;
							for(SmartCampaignPlacementObj campaignPlacementObj : placementList) {
								flightList = new ArrayList<>();
								partnerMatched = false;
								if(campaignPlacementObj.getFlightObjList() != null && campaignPlacementObj.getFlightObjList().size() > 0) {
									flightList.addAll(campaignPlacementObj.getFlightObjList());
								}
								if(campaignPlacementObj.getImpressions() != null) {
									String imp = campaignPlacementObj.getImpressions().replaceAll(",", "");
									if(LinMobileUtil.isNumeric(imp)) {
										goal = goal + StringUtil.getLongValue(imp);
									}
								}
								if(campaignPlacementObj.getBudget() != null && LinMobileUtil.isNumeric(campaignPlacementObj.getBudget())) {
									budget = budget + StringUtil.getDoubleValue(campaignPlacementObj.getBudget());
								}
								List<DFPLineItemDTO> dfpLineItemDTOs = campaignPlacementObj.getDfpLineItemList();
								String lineItems = "";
								if(dfpLineItemDTOs != null && dfpLineItemDTOs.size() > 0) {
									for(DFPLineItemDTO dfpLineItemDTO : dfpLineItemDTOs) {
										lineItems = lineItems + dfpLineItemDTO.getLineItemId()+"','";
										if(partnerNameList != null && partnerNameList.size() > 0 && dfpLineItemDTO.getPartner() != null && partnerNameList.contains(dfpLineItemDTO.getPartner().toLowerCase())) {
											partnerMatched = true;
										}
										lineItemIdPartnerNameMap.put(dfpLineItemDTO.getLineItemId()+"", dfpLineItemDTO.getPartner());
									}
								}
								if(partnerMatched || partnerNameList == null || partnerNameList.size() == 0) {
									lineItemsForReport.append(lineItems);
								}
							}
							campaignFlightMap.put(smartCampaignObj.getDfpOrderId()+"", flightList);
							long durationInDays = performanceService.dayDurationInFlights(flightList, null, null, "MM-dd-yyyy");
							double perDayGoal = 0;
							if(durationInDays > 0) {
								perDayGoal = (double) goal/durationInDays;
							}
							JSONObject campaignInfoJson = new JSONObject();
							double rate = 0.0;
							if(goal > 0 && campaignType.equals("CPM")) {
								rate = (double) (budget*1000)/goal;
							}
							else if(goal > 0 && campaignType.equals("CPC")) {
								rate = (double) budget/goal;
							}
							
							campaignInfoJson.put("rate", rate);
							campaignInfoJson.put("campaignType", campaignType);
							campaignInfoJson.put("perDayGoal", perDayGoal);
							campaignInfoJson.put("name", smartCampaignObj.getName());
							campaignInfoJson.put("startDate", smartCampaignObj.getStartDate());
							campaignInfoJson.put("goal", goal);
							campaignInfoJson.put("budget", budget);
							campaignInfoMap.put(smartCampaignObj.getDfpOrderId()+"", campaignInfoJson);
						}
					}
				}
			}else {
				log.info("No campaign belongs to user, company : "+companyId);
			}
			
			if(lineItemsForReport != null && lineItemsForReport.length() > 0) {
				QueryResponse queryResponse = null;
				QueryDTO queryDTO=BigQueryUtil.getQueryDTO(startDate, endDate, publisherBQId, LinMobileConstants.BQ_CORE_PERFORMANCE);
				
				if(queryDTO != null && queryDTO.getQueryData() != null && queryDTO.getQueryData().length() > 0) {
					queryResponse = plannerDAO.billingReportData(StringUtil.deleteFromLastOccurence(lineItemsForReport.toString(), "','"), queryDTO, startDate+" 00:00:00", endDate+" 00:00:00");
				}else {
					log.warning("Empty queryDTO : "+queryDTO);
				}

				/* Biling Report Data Calculation Starts*/
				if (queryResponse!=null && queryResponse.getRows() != null && queryResponse.getRows().size() > 0) {
					partnerInfoJsArr = createBillingReportData(queryResponse.getRows(), lineItemIdPartnerNameMap, orderDataMap, campaignInfoMap,
							startDate, endDate, campaignFlightMap);
				} else {
					log.info("Query response is empty or null.");
				}
				/* Biling Report Data Calculation Ends*/
				
			} else {
				log.info("No lineItems left. Report wiil be empty");
			}
			
		}catch (JSONException e) {
			log.severe("JSONException in billingReport of SmartCampaignPlannerService : "+e.getMessage());
			jsonObject.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			log.severe("Exception in billingReport of SmartCampaignPlannerService : "+e.getMessage());
			jsonObject.put("error", e.getMessage());
			e.printStackTrace();
		}
		jsonObject.put("billingReport", partnerInfoJsArr);
		return jsonObject;
	}
	
	
	/**
	 * @author Naresh Pokhriyal<br />
	 * support method for billingReport. formats and groups data by publisher and month after fetching data from Big Query.
	 * @return {@link JSONArray} Returns JSONArray.
	 */
	public JSONArray createBillingReportData(List<TableRow> rowList, Map<String,String> lineItemIdPartnerNameMap, Map<String, JSONObject> orderDataMap, 
			Map<String, JSONObject> campaignInfoMap, String startDate, String endDate, Map<String, List<SmartCampaignFlightDTO>> campaignFlightMap) throws Exception {
		log.info("Biling Report Data Calculation Starts");
		JSONArray partnerInfoJsArr = new JSONArray();
		Map<String,Map> dataMap = new LinkedHashMap<>();		// key : partnerName + <SEP> + yyyy-MM, value : orderDataMap
		JSONObject orderDataJson = null;
		
		for (TableRow row : rowList) {
		     if(row != null && row.getF() != null && row.getF().size() > 0) {
		    	 List<TableCell> cellList = row.getF();
		    	 long impressions = StringUtil.getLongValue(cellList.get(0).getV().toString());
		    	 long clicks = StringUtil.getLongValue(cellList.get(1).getV().toString());
		    	 String lineItemId = cellList.get(2).getV().toString();
		    	 String orderId = cellList.get(3).getV().toString();
		    	 String date=cellList.get(4).getV().toString();
		    	 date = date.substring(0, 7);
		    	 String partnerName = lineItemIdPartnerNameMap.get(lineItemId);
		    	 String dataMapKey = date + "<SEP>" + partnerName;			// year-month + partnerName
		    	 String orderDataKey = dataMapKey + "<SEP>" + orderId;		// year-month + partnerName + orderId
		    	 if(orderDataMap.containsKey(orderDataKey)) {
		    		 orderDataJson = orderDataMap.get(orderDataKey);
		    		 orderDataJson.put("imp", impressions + orderDataJson.getLong("imp"));
		    		 orderDataJson.put("clk", clicks + orderDataJson.getLong("clk"));
		    	 }else {
		    		 orderDataJson = new JSONObject();
		    		 orderDataJson.put("imp", impressions);
		    		 orderDataJson.put("clk", clicks);
		    		 orderDataMap.put(orderDataKey, orderDataJson);
		    	 }
		    	 
		    	 Map<String,JSONObject> tempMap = null;
		    	 if(dataMap.containsKey(dataMapKey)) {
		    		 tempMap = dataMap.get(dataMapKey);
		    	 }else {
		    		 tempMap = new HashMap<>();
		    	 }
		    	 tempMap.put(orderDataKey, orderDataJson);
		    	 dataMap.put(dataMapKey, tempMap);
		     }
		}
		
		if(dataMap != null && dataMap.size() > 0) {
			log.info("dataMap : "+dataMap);
			log.info("dataMap size : "+dataMap.size());
			log.info("campaignInfoMap : " + campaignInfoMap);
			IPerformanceMonitoringService performanceService = (IPerformanceMonitoringService) BusinessServiceLocator.locate(IPerformanceMonitoringService.class);
			Map<String, Long> cumulativeDeliveredMap = new HashMap<>();
			Map<String, Double> cumulativeAmountPaidMap = new HashMap<>();
			String formattedEndDate = endDate.substring(5, 10)+"-"+endDate.substring(0, 4);
			Date reportEndDate = DateUtil.getFormatedDate(endDate, "yyyy-MM-dd");
			Map<String, JSONObject> partnerMap = new HashMap<>();
			
			String startMonth = DateUtil.getMonthName(startDate, "yyyy-MM-dd", true);
			String endMonth = DateUtil.getMonthName(endDate, "yyyy-MM-dd", true);
			
			for(String dataMapKey : dataMap.keySet()) {						// all partners
				String thisMonthStartDate = dataMapKey.split("<SEP>")[0] + "-01";
				String partnerName = dataMapKey.split("<SEP>")[1];
				thisMonthStartDate = thisMonthStartDate.substring(5, 10)+"-"+thisMonthStartDate.substring(0, 4);
				String dateStr = DateUtil.getMonthName(thisMonthStartDate, "MM-dd-yyyy", true);
				String maxDayForReport = formattedEndDate;
				
				// get dayPassedEndDate
				Date thisMonthEndDate = DateUtil.getLastDateOfMonth(thisMonthStartDate, "MM-dd-yyyy");
				if(thisMonthEndDate.compareTo(reportEndDate) < 1) {
					maxDayForReport = DateUtil.getFormatedDate(thisMonthEndDate, "MM-dd-yyyy");
				}
				
				orderDataMap = dataMap.get(dataMapKey);
				if(orderDataMap != null && orderDataMap.size() > 0) {
					log.info("orderDataMap size : "+orderDataMap.size());
					JSONArray orderInfoJsArr = new JSONArray();
					double monthAmount = 0.0;
					for(String orderDataKey : orderDataMap.keySet()) {		// campaignWise data for partner for a month
						String orderId = orderDataKey.split("<SEP>")[2];
						orderDataJson = orderDataMap.get(orderDataKey);
						JSONObject campaignInfoJson = campaignInfoMap.get(orderId);
						List<SmartCampaignFlightDTO> campaignFlights = campaignFlightMap.get(orderId);
						if(campaignInfoJson != null && campaignFlights != null) {
							String cumulativeDeliveredMapKey = orderId+partnerName;
							long imp = orderDataJson.getLong("imp");
							long clk = orderDataJson.getLong("clk");
							String campaignType = campaignInfoJson.get("campaignType")+"";
							double perDayGoal = campaignInfoJson.getDouble("perDayGoal");
							String campaignStartDate = campaignInfoJson.get("startDate")+"";
							
							long monthDelivered = 0;
							double monthPaymentForCampaign = 0.0;
							String campaignName = campaignInfoJson.get("name")+"";
							double rate = campaignInfoJson.getDouble("rate");
							
							long daysPassedTillDate = performanceService.dayDurationInFlights(campaignFlights, campaignStartDate, maxDayForReport, "MM-dd-yyyy");
							long daysPassedThisMonth = performanceService.dayDurationInFlights(campaignFlights, thisMonthStartDate, maxDayForReport, "MM-dd-yyyy");
							long cumulativeGoal = Math.round((double)daysPassedTillDate * perDayGoal);
							long monthGoal = Math.round((double)daysPassedThisMonth * perDayGoal);
							long cumulativeDelivered = StringUtil.getLongValue(cumulativeDeliveredMap.get(cumulativeDeliveredMapKey)+"");
							double cumulativeAmountPaid = StringUtil.getDoubleValue(cumulativeAmountPaidMap.get(cumulativeDeliveredMapKey)+"");
							
							double totalPaid = 0.0;
							if(campaignType.equals("CPM")) {
								cumulativeDelivered = cumulativeDelivered+imp;
								monthDelivered = imp;
								// No target
								if(cumulativeGoal <= cumulativeDelivered) {
									totalPaid =  ((double) rate*cumulativeGoal)/1000;
								}else {
									totalPaid =  ((double) rate*cumulativeDelivered)/1000;
								}
							}else if(campaignType.equals("CPC")) {
								cumulativeDelivered = cumulativeDelivered+clk;
								monthDelivered = clk;
								// No target
								if(cumulativeGoal <= cumulativeDelivered) {
									totalPaid =  (double) rate*cumulativeGoal;
								}else {
									totalPaid =  (double) rate*cumulativeDelivered;
								}
							}
							log.info("cumulativeDelivered : "+cumulativeDelivered+", cumulativeGoal : "+cumulativeGoal+", rate: "+rate+", totalPaid : "+totalPaid);
							
							cumulativeDeliveredMap.put(cumulativeDeliveredMapKey, cumulativeDelivered);
							monthPaymentForCampaign = totalPaid - cumulativeAmountPaid;
							cumulativeAmountPaidMap.put(cumulativeDeliveredMapKey, totalPaid);
							monthAmount = monthAmount + monthPaymentForCampaign;
							
							JSONObject orderInfoJsObj = new JSONObject();
							orderInfoJsObj.put("campaignName", campaignName);
							orderInfoJsObj.put("cumulativeGoal", cumulativeGoal);
							orderInfoJsObj.put("monthGoal", monthGoal);
							orderInfoJsObj.put("cumulativeDelivered", cumulativeDelivered);
							orderInfoJsObj.put("monthDelivered", monthDelivered);
							orderInfoJsObj.put("rate", rate);
							orderInfoJsObj.put("monthPaymentForCampaign", monthPaymentForCampaign);
							orderInfoJsObj.put("totalPaid", totalPaid);
							orderInfoJsObj.put("campaignType", campaignType);
							orderInfoJsArr.add(orderInfoJsObj);
							//log.info("orderInfoJsObj : "+orderInfoJsObj);
						}
					}
					
					JSONObject partnerInfoJsObj = null;
					JSONArray monthInfoJsArr = null;
					JSONObject monthInfoJsObj = new JSONObject();
					monthInfoJsObj.put("month", dateStr);
					monthInfoJsObj.put("data",orderInfoJsArr);
					monthInfoJsObj.put("total", monthAmount);
					
					if(partnerMap.containsKey(partnerName) && partnerMap.get(partnerName) != null) {
						partnerInfoJsObj = partnerMap.get(partnerName);
						monthInfoJsArr = (JSONArray) partnerInfoJsObj.get("data");
						monthInfoJsArr.add(monthInfoJsObj);
						partnerInfoJsObj.put("data", monthInfoJsArr);
						partnerInfoJsObj.put("total", partnerInfoJsObj.getDouble("total") + monthAmount);
					}else {
						log.info("No partnerName contained, partnerName : "+partnerName+", partnerMap : "+partnerMap);
						partnerInfoJsObj = new JSONObject();
						monthInfoJsArr = new JSONArray();
						monthInfoJsArr.add(monthInfoJsObj);
						partnerInfoJsObj.put("name", partnerName);
						partnerInfoJsObj.put("data", monthInfoJsArr);
						partnerInfoJsObj.put("total", monthAmount);
						partnerInfoJsObj.put("duration", startMonth + " - " + endMonth);
					}
					partnerMap.put(partnerName, partnerInfoJsObj);
					//log.info("partnerMap : "+partnerMap);
				}
			}
			log.info("partnerMap.size()");
			if(partnerMap.size() > 0) {
				for(String partnerName : partnerMap.keySet()) {
					partnerInfoJsArr.add(partnerMap.get(partnerName));
				}
			}
		}else {
			log.warning("No data for any partner");
		}
		log.info("Biling Report Data Calculation Ends");
		return partnerInfoJsArr;
	}
	
	
	@Override
	/**
	 * @author Naresh Pokhriyal<br />
	 * @param billingReportJson : JSONObject with formatted data for billing report
	 * @return {@link Workbook} Returns Workbook.
	 */
	public Workbook makeBillingExcelReport(JSONObject billingReportJson) {
		Workbook wb = null;
		DecimalFormat df2 = new DecimalFormat( "###,###,###,##0.00" );
		DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
		try {
			if(billingReportJson != null && billingReportJson.containsKey("billingReport")) {
				wb = new HSSFWorkbook();
				int topMargin = 2;
				int leftMargin = 1;
				int gapBetweenTables = 2;
				
				DataFormat format = wb.createDataFormat();
				
				CellStyle borderedCell = wb.createCellStyle();
			    borderedCell.setBorderBottom(CellStyle.BORDER_THIN);
			    borderedCell.setBorderLeft(CellStyle.BORDER_THIN);
			    borderedCell.setBorderRight(CellStyle.BORDER_THIN);
			    borderedCell.setBorderTop(CellStyle.BORDER_THIN);
			    
			    CellStyle borderedCurrencyValueCell = wb.createCellStyle();
			    borderedCurrencyValueCell.setDataFormat(format.getFormat("[$$-409]#,##0.00;[RED]-[$$-409]#,##0.00"));
			    //borderedCurrencyValueCell.setDataFormat((short)8); //8 for currency
			    borderedCurrencyValueCell.setBorderBottom(CellStyle.BORDER_THIN);
			    borderedCurrencyValueCell.setBorderLeft(CellStyle.BORDER_THIN);
			    borderedCurrencyValueCell.setBorderRight(CellStyle.BORDER_THIN);
			    borderedCurrencyValueCell.setBorderTop(CellStyle.BORDER_THIN);
			    
			    CellStyle blackBGCurrencyValueCell = wb.createCellStyle();
			    blackBGCurrencyValueCell.setDataFormat((short)8); //8 for currency
			    blackBGCurrencyValueCell.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			    blackBGCurrencyValueCell.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    
			    CellStyle borderedLongValueCell = wb.createCellStyle();
			    borderedLongValueCell.setDataFormat(format.getFormat("###,###,###,###"));
			    borderedLongValueCell.setBorderBottom(CellStyle.BORDER_THIN);
			    borderedLongValueCell.setBorderLeft(CellStyle.BORDER_THIN);
			    borderedLongValueCell.setBorderRight(CellStyle.BORDER_THIN);
			    borderedLongValueCell.setBorderTop(CellStyle.BORDER_THIN);
			    
			    CellStyle blackBG = wb.createCellStyle();
			    blackBG.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			    blackBG.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    
			    CellStyle greyBGBorderedCell = wb.createCellStyle();
			    greyBGBorderedCell.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			    greyBGBorderedCell.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    greyBGBorderedCell.setBorderLeft(CellStyle.BORDER_THIN);
			    greyBGBorderedCell.setBorderTop(CellStyle.BORDER_THIN);
			    greyBGBorderedCell.setBorderRight(CellStyle.BORDER_THIN);
			    greyBGBorderedCell.setBorderBottom(CellStyle.BORDER_THIN);
			    
				JSONArray partnerInfoJsArr = (JSONArray) billingReportJson.get("billingReport");
				if(partnerInfoJsArr != null && partnerInfoJsArr.size() > 0) {
					for(int i = 0; i<partnerInfoJsArr.size(); i++) {
						JSONObject partnerInfoJsObj = (JSONObject) partnerInfoJsArr.get(i);
						String partnerName = partnerInfoJsObj.get("name")+"";
						double partnerAmount = StringUtil.getDoubleValue(partnerInfoJsObj.get("total")+"");
						String duration = partnerInfoJsObj.get("duration")+"";
						int rowCounter = topMargin;
						
						JSONArray monthInfoJsArr = (JSONArray) partnerInfoJsObj.get("data");
						if(monthInfoJsArr != null && monthInfoJsArr.size() > 0) {
							Sheet sheet = wb.createSheet(partnerName);
							sheet.setColumnWidth(leftMargin, 30*256);sheet.setColumnWidth(leftMargin+1, 18*256);sheet.setColumnWidth(leftMargin+2, 23*256);
							sheet.setColumnWidth(leftMargin+3, 17*256);sheet.setColumnWidth(leftMargin+4, 20*256);sheet.setColumnWidth(leftMargin+5, 17*256);
							sheet.setColumnWidth(leftMargin+6, 10*256);sheet.setColumnWidth(leftMargin+7, 15*256);sheet.setColumnWidth(leftMargin+8, 18*256);
							
							Row row = sheet.createRow(rowCounter);									// Partner Summary
							Cell cell = row.createCell(leftMargin);cell.setCellValue("Publisher");cell.setCellStyle(greyBGBorderedCell);
						    cell = row.createCell(leftMargin+1);cell.setCellValue(partnerName);cell.setCellStyle(borderedCell);
						    row = sheet.createRow(++rowCounter);
						    cell = row.createCell(leftMargin);cell.setCellValue("Duration");cell.setCellStyle(greyBGBorderedCell);
						    cell = row.createCell(leftMargin+1);cell.setCellValue(duration);cell.setCellStyle(borderedCell);
						    row = sheet.createRow(++rowCounter);
						    cell = row.createCell(leftMargin);cell.setCellValue("Billing Amount");cell.setCellStyle(greyBGBorderedCell);
						    cell = row.createCell(leftMargin+1);cell.setCellValue(partnerAmount);cell.setCellStyle(borderedCurrencyValueCell);
						    
						    rowCounter = rowCounter + gapBetweenTables;								// gapBetweenTables
							
						    for(int j = 0; j<monthInfoJsArr.size(); j++) {							// month Tables
						    	JSONObject monthInfoJsObj = (JSONObject) monthInfoJsArr.get(j);
						    	String month = monthInfoJsObj.get("month")+"";
								double monthAmount = StringUtil.getDoubleValue(monthInfoJsObj.get("total")+"");
								JSONArray orderInfoJsArr = (JSONArray) monthInfoJsObj.get("data");
								if(orderInfoJsArr != null && orderInfoJsArr.size() > 0) {
									row = sheet.createRow(++rowCounter);							// Month Table Summary
								    cell = row.createCell(leftMargin);cell.setCellValue(month);cell.setCellStyle(blackBG);
								    cell = row.createCell(leftMargin+1);cell.setCellValue(monthAmount);cell.setCellStyle(blackBGCurrencyValueCell);
								    
								    row = sheet.createRow(++rowCounter);							// month Table Header
								    cell = row.createCell(leftMargin);cell.setCellValue("Campaign Name");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+1);cell.setCellValue("Rate Type");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+2);cell.setCellValue("Cumulative Booked Goal");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+3);cell.setCellValue("Booked Goal");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+4);cell.setCellValue("Cumulative Delivered");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+5);cell.setCellValue("Delivered");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+6);cell.setCellValue("Rate");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+7);cell.setCellValue("Payout");cell.setCellStyle(greyBGBorderedCell);
								    cell = row.createCell(leftMargin+8);cell.setCellValue("Billing Amount");cell.setCellStyle(greyBGBorderedCell);
								    //sheet.autoSizeColumn(rowCounter); //adjust width of the first column
								    
								    for(int k = 0; k<orderInfoJsArr.size(); k++) {					// month Table Data
								    	JSONObject orderInfoJsObj = (JSONObject) orderInfoJsArr.get(k);
								    	row = sheet.createRow(++rowCounter);
								    	cell = row.createCell(leftMargin);cell.setCellValue(orderInfoJsObj.get("campaignName")+"");cell.setCellStyle(borderedCell);
									    cell = row.createCell(leftMargin+1);cell.setCellValue(orderInfoJsObj.get("campaignType")+"");cell.setCellStyle(borderedCell);
									    cell = row.createCell(leftMargin+2);cell.setCellValue(StringUtil.getLongValue(orderInfoJsObj.get("cumulativeGoal")+""));cell.setCellStyle(borderedLongValueCell);
									    cell = row.createCell(leftMargin+3);cell.setCellValue(StringUtil.getLongValue(orderInfoJsObj.get("monthGoal")+""));cell.setCellStyle(borderedLongValueCell);
									    cell = row.createCell(leftMargin+4);cell.setCellValue(StringUtil.getLongValue(orderInfoJsObj.get("cumulativeDelivered")+""));cell.setCellStyle(borderedLongValueCell);
									    cell = row.createCell(leftMargin+5);cell.setCellValue(StringUtil.getLongValue(orderInfoJsObj.get("monthDelivered")+""));cell.setCellStyle(borderedLongValueCell);
									    cell = row.createCell(leftMargin+6);cell.setCellValue(StringUtil.getDoubleValue(orderInfoJsObj.get("rate")+""));cell.setCellStyle(borderedCurrencyValueCell);
									    cell = row.createCell(leftMargin+7);cell.setCellValue(StringUtil.getDoubleValue(orderInfoJsObj.get("totalPaid")+""));cell.setCellStyle(borderedCurrencyValueCell);
									    cell = row.createCell(leftMargin+8);cell.setCellValue(StringUtil.getDoubleValue(orderInfoJsObj.get("monthPaymentForCampaign")+""));cell.setCellStyle(borderedCurrencyValueCell);
								    }
								    rowCounter = rowCounter + gapBetweenTables;						// gapBetweenTables
								}
						    }
						}
					}
				}else {
					log.info("partnerInfoJsArr is null or empty");
				}
			}else {
				log.info("billingReportJson is null or empty");
			}
		}catch (JSONException e) {
			log.severe("JSONException in makeBillingExcelReport of SmartCampaignPlannerService : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			log.severe("Exception in makeBillingExcelReport of SmartCampaignPlannerService : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		return wb;
	}
	
	
	/**
	 * @author Shubham Goel <br />
	 * @return Returns List<CompanyObj> This method returns list of all company object from datastore
	 */
	@Override
	public List<CompanyObj> getAllCompanyList(){
		log.info("Inside getAllCompanyList of SmartCampaignPlannerService...");
		List<CompanyObj> companyList = new ArrayList<>();
		ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		try{
			companyList = plannerDAO.getAllCompanyList();
		}catch(Exception e){
			log.severe("Exception in getAllCompanyList : " + e.getMessage());
		}
		return companyList;
		
	}
	
	/**
	 * @author Shubham Goel <br />
	 * @param List<CompanyObj> : List of company object
	 * @return Returns Map<String,CompanyObj> Map of unique DFP network code and their company object.
	 */
	@Override
	public Map<String,CompanyObj> getUniqueDFPNetworkCode(List<CompanyObj> companyList){
		log.info("Inside getUniqueDFPNetworkCode of SmartCampaignPlannerService...");
		Map<String,CompanyObj> uniqueDFPNetworkMap = new HashMap<String, CompanyObj>();
		try{
			if(companyList!=null && companyList.size()>0){
				for (CompanyObj companyObj : companyList) {
					if(companyObj!=null){
						if(companyObj.getAdServerId()!=null && companyObj.getAdServerId().size()>1){
							for(int i=0;i<companyObj.getAdServerId().size();i++){
								if(!uniqueDFPNetworkMap.containsKey(companyObj.getAdServerId().get(i).toString())){
									uniqueDFPNetworkMap.put(companyObj.getAdServerId().get(i).toString(), companyObj);
								}
							}
						}else if(companyObj.getAdServerId()!=null && companyObj.getAdServerId().size()==1){
							if(!uniqueDFPNetworkMap.containsKey(companyObj.getAdServerId().get(0).toString())){
								uniqueDFPNetworkMap.put(companyObj.getAdServerId().get(0).toString(), companyObj);
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			log.severe("Exception in getUniqueDFPNetworkCode : " + e.getMessage());
		}
		return uniqueDFPNetworkMap;
	}
	
	/**
	 * @author Shubham Goel 
	 * @param Map<String,CompanyObj> : Map of unique DFP network code and their company object.
	 * @param String : Hours for which last updated or created order are fetch from DFP
	 * @return Returns Boolean 
	 */
	
	@Override
	public boolean createOrUpdateCampaignObjFromDFP(Map<String,CompanyObj> uniqueDFPNetworkMap, String hours){
		log.info("Inside createOrUpdateCampaignObjFromDFP of SmartCampaignPlannerService...");
		List<SmartCampaignObj> campaignObjList = new ArrayList<>();
		List<SmartCampaignPlacementObj> placementObjList = new ArrayList<>();
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		Map<Long,SmartCampaignPlacementObj> lineItemMap = new HashMap<>();
		String loadHistoricalDataURL = "/loadHistoricalData.lin";
		boolean newCampaign = false;
		try{
			long pastHours = 0L;
			if(hours!=null){
				 pastHours = StringUtil.getLongValue(hours);
			}else{
				pastHours = 6L;
			}
			OrderPage orderPage = new OrderPage();
			if(uniqueDFPNetworkMap!=null && uniqueDFPNetworkMap.size()>0){
				 Iterator iterator = uniqueDFPNetworkMap.entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry mapEntry = (Map.Entry) iterator.next();
						if(mapEntry!=null && mapEntry.getKey()!=null){
							CompanyObj companyObj = new CompanyObj();
							companyObj = (CompanyObj) mapEntry.getValue();
							if(companyObj.getAdServerId().size()>0){
								for(int i=0;i<companyObj.getAdServerId().size();i++){
									//createDFPSessionService(companyObj.getAdServerId().get(i),companyObj.getAdServerUsername().get(i),companyObj.getAdServerPassword().get(i));
									dfpSession = DFPAuthenticationUtil.getDFPSession(companyObj.getAdServerId().get(i));
									log.info(" getting DfpServices instance from properties...");
									dfpServices = LinMobileProperties.getInstance().getDfpServices();
									log.info("Going to fetch Last updated orders for DFP Network Code : "+companyObj.getAdServerId().get(i)); 
									orderPage = getLastUpdatedOredersFromDFP(dfpServices, dfpSession, pastHours);
									if(orderPage.getResults()!=null){
										log.info("No of orders found..."+orderPage.getResults().size());
									}
									campaignObjList = createCampaignObjForLastUpdatedOrders(orderPage, companyObj.getId(), companyObj.getAdServerId().get(i), companyObj.getAdServerUsername().get(i),companyObj.getCompanyName());
									if(campaignObjList!=null && campaignObjList.size()>0){
										log.info("campaignObjList size : "+campaignObjList.size());
										for (SmartCampaignObj smartCampaignObj : campaignObjList) {
											if(smartCampaignObj!=null ){
												SmartCampaignObj campaignObj = campaignPlannerDAO.getCampaignByCampaignId(smartCampaignObj.getDfpOrderId());
												if(campaignObj==null && smartCampaignObj.getAdServerId()!=null){
													newCampaign = true;
												}else{
													newCampaign = false;
												}
												if(smartCampaignObj.getAdServerId()!=null && smartCampaignObj.getAdvertiserId()!=null){
													AdvertiserObj advertiserObj = new AdvertiserObj();
													AccountsEntity accountsEntity = new AccountsEntity();
													advertiserObj = getAdvertiserObj(smartCampaignObj.getAdServerId(), StringUtil.getLongValue(smartCampaignObj.getAdvertiserId()));
													if(advertiserObj!=null){
														advertiserObj = saveOrUpdateAdvertiser(advertiserObj);
													}
													log.info("going to Save Account");
													accountsEntity = saveOrUpdateAccountEntity(advertiserObj.getId()+"", advertiserObj.getName(), 
															advertiserObj.getDfpNetworkCode(), smartCampaignObj.getAdServerUsername(), smartCampaignObj.getCompanyId(), "Advertiser");
													log.info("Account updated : "+accountsEntity.getAccountDfpId());
												}
												if(smartCampaignObj.getAdServerId()!=null && smartCampaignObj.getAgencyId()!=null){
													AgencyObj agencyObj = new AgencyObj();
													AccountsEntity accountsEntity = new AccountsEntity();
													agencyObj = getAgencyObj(smartCampaignObj.getAdServerId(), StringUtil.getLongValue(smartCampaignObj.getAgencyId()));
													if(agencyObj!=null){
														agencyObj = saveOrUpdateAgency(agencyObj);
													}
													log.info("going to Save Account");
													accountsEntity = saveOrUpdateAccountEntity(agencyObj.getId()+"", agencyObj.getName(), 
															agencyObj.getDfpNetworkCode(), smartCampaignObj.getAdServerUsername(), smartCampaignObj.getCompanyId(), "Agency");
													log.info("Account updated : "+accountsEntity.getAccountDfpId());
												}
												LineItemPage lineItemPage = new LineItemPage();
												lineItemPage = getAllLineItemForOrderFromDFP(dfpServices, dfpSession, smartCampaignObj.getDfpOrderId());
												Key<SmartCampaignObj> campaignKey = Key.create(SmartCampaignObj.class, smartCampaignObj.getCampaignId());
												lineItemMap = getLineItemMap(smartCampaignObj.getCampaignId());
												placementObjList = createPlacementObjsForOrder(lineItemPage, campaignKey, companyObj.getCompanyName() ,lineItemMap);
												campaignPlannerDAO.saveObjectWithStrongConsistancy(smartCampaignObj);
												if(placementObjList!=null && placementObjList.size()>0){
													for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
														campaignPlannerDAO.saveObjectWithStrongConsistancy(smartCampaignPlacementObj);
													}
												}
												if(newCampaign && smartCampaignObj.getDfpOrderId() >0
														&&  smartCampaignObj.getAdServerId()!=null){
													TaskQueueUtil.loadHistoricalData(loadHistoricalDataURL, smartCampaignObj.getDfpOrderId()+"", smartCampaignObj.getAdServerId()); // Load Historical data for a particular order ID.
												}
											}	
										}
									}
								}
							}
						}
						iterator.remove();
				}
			}
		}catch(Exception e){
			log.severe("Exception in createOrUpdateCampaignObjFromDFP : " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public Map<Long,SmartCampaignPlacementObj> getLineItemMap(long campaignId){
		List<SmartCampaignPlacementObj> placementObjList = new ArrayList<>();
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		Map<Long,SmartCampaignPlacementObj> lineItemMap = new HashMap<>();
		try{
			placementObjList = campaignPlannerDAO.getAllPlacementOfCampaign(campaignId);
			if(placementObjList!=null && placementObjList.size()>0){
				for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
					if(smartCampaignPlacementObj!=null && smartCampaignPlacementObj.getDfpLineItemList()!=null && smartCampaignPlacementObj.getDfpLineItemList().size()>0)
						for (DFPLineItemDTO dfpLineDTO : smartCampaignPlacementObj.getDfpLineItemList()) {
							lineItemMap.put(dfpLineDTO.getLineItemId(), smartCampaignPlacementObj);
						}
				}
			}
		}catch(Exception e){
			log.severe("Exception in getLineItemMap : " + e.getMessage());
		}
		
		return lineItemMap;
		
	}
	  
	/**
	 * @author Shubham Goel 
	 * @param long advertiserId : Advertiser ID for which company object is loaded from DFP.
	 * @param String dfpNetworkCode : DFP Network Code  
	 * @return AdvertiserObj : This method fetch  company object from DFP for a particular advertiser and create a advertiser Object for datastore
	 */
	public AdvertiserObj getAdvertiserObj(String dfpNetworkCode, long advertiserId) throws Exception{
		AdvertiserObj advertiserObj = new AdvertiserObj();
		CompanyPage advertiserCompanyPage = new CompanyPage();
		 advertiserCompanyPage = getCompanyObjFromDFP(dfpServices, dfpSession, advertiserId);
		 if(advertiserCompanyPage.getResults()!=null && advertiserCompanyPage.getResults().size()>0){
			if(advertiserCompanyPage.getResults().get(0).getName()!=null){
				advertiserObj.setName(advertiserCompanyPage.getResults().get(0).getName());
			}
		 }
		if(dfpNetworkCode!=null){
			advertiserObj.setDfpNetworkCode(dfpNetworkCode);
		}
		advertiserObj.setAdvertiserId(advertiserId);
		advertiserObj.setId(advertiserId);
		return advertiserObj;
		
	}
	
	/**
	 * @author Shubham Goel 
	 * @param long agencyId : Agency ID for which company object is loaded from DFP.
	 * @param String dfpNetworkCode : DFP Network Code  
	 * @return AgencyObj : This method fetch  company object from DFP for a particular agency and create a agency Object for datastore
	 */
	public AgencyObj getAgencyObj(String dfpNetworkCode, long agencyId) throws Exception{
		AgencyObj agencyObj = new AgencyObj();
		CompanyPage companyPage = new CompanyPage();
		companyPage = getCompanyObjFromDFP(dfpServices, dfpSession, agencyId);
		 if(companyPage.getResults()!=null && companyPage.getResults().size()>0){
			if(companyPage.getResults().get(0).getName()!=null){
				agencyObj.setName(companyPage.getResults().get(0).getName());
			}
		 }
		if(dfpNetworkCode!=null){
			agencyObj.setDfpNetworkCode(dfpNetworkCode);
		}
		agencyObj.setAgencyId(agencyId);
		agencyObj.setId(agencyId);
		return agencyObj;
		
	}
	 
	/**
	 * @author Shubham Goel 
	 * @param DfpServices dfpServices : DFP service object
	 * @param DfpSession session : DFP session object  
	 * @param long hours : Hours for which orders are fetch
	 * @return OrderPage : This method fetch  last update or created orders on DFP for given hours
	 */
	 public OrderPage getLastUpdatedOredersFromDFP(DfpServices dfpServices, DfpSession session, long hours) throws Exception{
		 log.info("Inside getLastUpdatedOredersFromDFP of SmartCampaignPlannerService...");
		    // Get the OrderService.
		    OrderServiceInterface orderService =
		        dfpServices.get(session, OrderServiceInterface.class);
		    if(hours==0){
    			hours = 3L;
    		}
		    // Create a statement to only select Orders updated or created 
		   
		    StatementBuilder statementBuilder = new StatementBuilder()
		        .where("lastModifiedDateTime >= :lastModifiedDateTime")
		        .orderBy("id ASC")
		        .withBindVariableValue("lastModifiedDateTime",
		            DateTimes.toDateTime(Instant.now().minus(Duration.standardHours(hours)),
		                "America/New_York"));

		    // Default for total result set size.
		    int totalResultSetSize = 0;

		      // Get line items by statement.
		      OrderPage page =
		    		  orderService.getOrdersByStatement(statementBuilder.toStatement());
		      System.out.printf("Number of results found: %d\n", totalResultSetSize);
		return  page;
		 
	 }
	 
	 /**
		 * @author Shubham Goel 
		 * @param DfpServices dfpServices : DFP service object
		 * @param DfpSession session : DFP session object  
		 * @param long orderId : order id for which all the lineitems to be fetched.
		 * @return LineItemPage : This method fetch  all the List of lineitem for a given order id.
	 */
	 public LineItemPage getAllLineItemForOrderFromDFP(DfpServices dfpServices, DfpSession session, long orderId) throws Exception{
		 log.info("Inside getAllLineItemForOrderFromDFP of SmartCampaignPlannerService...");
		  LineItemServiceInterface lineItemService =
			        dfpServices.get(session, LineItemServiceInterface.class);
		 StatementBuilder statementBuilder = new StatementBuilder()
	        .where("WHERE orderId = :id")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("id", orderId);
		 
		 LineItemPage page = lineItemService.getLineItemsByStatement(statementBuilder.toStatement());
		 return page;
	 }
	 
	 /**
		 * @author Shubham Goel 
		 * @param OrderPage page : DFP order object
		 * @param long companyId : Datastore company ID  
		 * @param String adServerId : Datastore Adsever ID (DFP network Code)
		 * @param String adServerName : Datastore adserver name(DFP user name)
		 * @param String companyName : Datastore company name
		 * @return List<SmartCampaignObj>: returns list of datastore campaign objects of last updated or created order objects on DFP.
	 */
	 public List<SmartCampaignObj> createCampaignObjForLastUpdatedOrders(OrderPage page, long companyId, String adServerId, String adServerName, String companyName){
		 log.info("Inside createCampaignObjForLastUpdatedOrders of SmartCampaignPlannerService...");
		 List<SmartCampaignObj> campaignObjList = new ArrayList<>();
		 ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		 try{
			 if(page!=null && page.getResults()!=null && page.getResults().size()>0){
				 for (Order order : page.getResults()) {
					 if(order!=null && (order.getStatus().ordinal()==order.getStatus().PENDING_APPROVAL.ordinal() || order.getStatus().ordinal()==order.getStatus().PAUSED.ordinal()
							 || order.getStatus().ordinal()==order.getStatus().APPROVED.ordinal())){
						 SmartCampaignObj campaignObj = new SmartCampaignObj();
						 if(order.getId()!=null){
							 log.info("order Id = "+order.getId());
							 campaignObj = plannerDAO.getCampaignByDFPOrderId(order.getId());
							 if(campaignObj==null){
								 campaignObj = new SmartCampaignObj();
							 }
							 campaignObj.setDfpOrderId(order.getId());
							 campaignObj.setId(order.getId());
							 campaignObj.setCampaignId(order.getId());
						 }
						 if(order.getName()!=null){
							 if(adServerId.equals("5678") && order.getName().contains("WLIN")){
								 CompanyObj companyObj = new CompanyObj();
								 companyObj = getCompanyObjForLinMobile("5678");
								 if(companyObj!=null){
									 campaignObj.setCompanyId(companyObj.getId()+"");
									 campaignObj.setCompanyName(companyObj.getCompanyName());
									 campaignObj.setAdServerId("5678");
									 if(companyObj.getAdServerUsername()!=null && companyObj.getAdServerUsername().get(0)!=null){
										 campaignObj.setAdServerUsername(companyObj.getAdServerUsername().get(0));
									 }
								 }
								 
							 }else{
								 campaignObj.setCompanyId(companyId+"");
								 campaignObj.setCompanyName(companyName);
								 campaignObj.setAdServerId(adServerId);
								 if(adServerName!=null){
									 campaignObj.setAdServerUsername(adServerName);
								 }
							 }
							 campaignObj.setDfpOrderName(order.getName());
							 campaignObj.setName(order.getName());
						 }
						 if(order.getStartDateTime()!=null){
							 String startDate = order.getStartDateTime().getDate().getMonth()+"-"+order.getStartDateTime().getDate().getDay()+"-"+order.getStartDateTime().getDate().getYear();
							 campaignObj.setStartDate(startDate);
						 }else{
							 campaignObj.setStartDate("01-01-2015");
						 }
						 if(order.getEndDateTime()!=null){
							 String endDate = order.getEndDateTime().getDate().getMonth()+"-"+order.getEndDateTime().getDate().getDay()+"-"+order.getEndDateTime().getDate().getYear();
							 campaignObj.setEndDate(endDate);
						 }else{
							 campaignObj.setEndDate("12-31-2099");
						 }
						 if(order.getAdvertiserId()!=null){
							 campaignObj.setAdvertiserId(order.getAdvertiserId()+"");
						 }
						 if(order.getAgencyId()!=null){
							 campaignObj.setAgencyId(order.getAgencyId()+"");
						 }
						 if(order.getNotes()!=null){
							 campaignObj.setNotes(order.getNotes());
						 }
						  if(order.getStatus()!=null){
							 if(order.getStatus().equals(order.getStatus().PENDING_APPROVAL)){
								 campaignObj.setCampaignStatus(CampaignStatusEnum.Ready.ordinal()+"");
								 campaignObj.setDfpStatus(order.getStatus().value());
							 }else if(order.getStatus().equals(order.getStatus().APPROVED)){
								 campaignObj.setCampaignStatus(CampaignStatusEnum.Running.ordinal()+"");
								 campaignObj.setDfpStatus(order.getStatus().value());
							 }else if(order.getStatus().equals(order.getStatus().PAUSED)){
								 campaignObj.setCampaignStatus(CampaignStatusEnum.Paused.ordinal()+""); 
							 }
						 }
						
						 campaignObj.setMigrated(true);
						 campaignObjList.add(campaignObj);
					 }
				 }
			 }
		 }catch(Exception e){
				log.severe("Exception in createCampaignObjForLastUpdatedOrders : " + e.getMessage());

		 }
		 return campaignObjList;
	 }
	 
	 /**
		 * @author Shubham Goel 
		 * @param LineItemPage lineItemPage : DFP Lineitem object
		 * @param Key<SmartCampaignObj> campaignKey : Datastore key of SmartCampaignObj for SmartCampaignPlacementObj.
		 * @return List<SmartCampaignPlacementObj>: returns list of datastore Palcement  objects of last updated or created order objects on DFP.
	 */
	 public List<SmartCampaignPlacementObj> createPlacementObjsForOrder(LineItemPage lineItemPage, Key<SmartCampaignObj> campaignKey, String companyName,
			 																Map<Long,SmartCampaignPlacementObj> lineItemMap){
		 log.info("Inside createPlacementObjsForOrder of SmartCampaignPlannerService...");
		 List<SmartCampaignPlacementObj> campaignPlacementObjList = new ArrayList<>();
		 SmartCampaignPlacementObj campaignPlacementObj = null;
		 try{
			 if(lineItemPage!=null && lineItemPage.getResults()!=null && lineItemPage.getResults().size()>0){
				 for (LineItem lineItem : lineItemPage.getResults()) {
					 if(lineItem!=null){
						 if(lineItemMap!=null && lineItemMap.containsKey(lineItem.getId())){
							  campaignPlacementObj = lineItemMap.get(lineItem.getId());
						 }else{
							  campaignPlacementObj = new SmartCampaignPlacementObj();
						 }
						 List<DFPLineItemDTO> dfpLineItemDTOList = new ArrayList<>();
						 DFPLineItemDTO lineItemDTO = new DFPLineItemDTO();
						 if(campaignKey!=null){
							 campaignPlacementObj.setCampaign(campaignKey);
						 }
						 if(lineItem.getId()!=null){
							 log.info("lineItem Id = "+lineItem.getId());
							 campaignPlacementObj.setId(lineItem.getId());
							 campaignPlacementObj.setPlacementId(lineItem.getId());
							 lineItemDTO.setLineItemId(lineItem.getId());
						 }
						 if(lineItem.getName()!=null){
							 campaignPlacementObj.setPlacementName(lineItem.getName());
							 lineItemDTO.setLineItemName(lineItem.getName());
						 }
						 if(lineItem.getStatus()!=null){
							 lineItemDTO.setDfpStatus(lineItem.getStatus().name());
						 }
						 if(lineItemDTO!=null){
							 dfpLineItemDTOList.add(lineItemDTO);
							 campaignPlacementObj.setDfpLineItemList(dfpLineItemDTOList);
						 }
						 if(companyName!=null){
							 lineItemDTO.setPartner(companyName);
						 }else{
							 lineItemDTO.setPartner("");
						 }
						 if(lineItem.getStartDateTime()!=null){
							 String startDate = lineItem.getStartDateTime().getDate().getMonth()+"-"+lineItem.getStartDateTime().getDate().getDay()+"-"+lineItem.getStartDateTime().getDate().getYear();
							 campaignPlacementObj.setStartDate(startDate);
						 }else{
							 campaignPlacementObj.setStartDate("01-01-2015");
						 }
						 if(lineItem.getEndDateTime()!=null){
							 String endDate = lineItem.getEndDateTime().getDate().getMonth()+"-"+lineItem.getEndDateTime().getDate().getDay()+"-"+lineItem.getEndDateTime().getDate().getYear();
							 campaignPlacementObj.setEndDate(endDate);
						 }else{
							 campaignPlacementObj.setEndDate("12-31-2099");
						 }
						 if(lineItem.getBudget()!=null){
							 campaignPlacementObj.setBudget(ReportUtil.convertMoney(lineItem.getBudget().getMicroAmount().toString())+"");
						 }
						 if(lineItem.getCreativePlaceholders().size()>0){
							 for(int i=0;i<lineItem.getCreativePlaceholders().size();i++){
								 String creativeWidth = lineItem.getCreativePlaceholders().get(i).getSize().getWidth()+"";
								 String creativeHeight = lineItem.getCreativePlaceholders().get(i).getSize().getHeight()+"";
								 String creativeSize = creativeWidth+"X"+creativeHeight;

							 }
						 }
						 log.info("Create a default flight for this placement with id-1");
						 List<SmartCampaignFlightDTO> flightObjList=new ArrayList<SmartCampaignFlightDTO>();
						 SmartCampaignFlightDTO flightDTO=new SmartCampaignFlightDTO();
						 String flightName=campaignPlacementObj.getStartDate()+":"+campaignPlacementObj.getEndDate();
						 flightDTO.setFlightName(flightName);
						 flightDTO.setStartDate(campaignPlacementObj.getStartDate());
						 flightDTO.setEndDate(campaignPlacementObj.getEndDate());
						 flightDTO.setFlightId(1);
						 flightObjList.add(flightDTO);
						 campaignPlacementObj.setFlightObjList(flightObjList);
							
						 campaignPlacementObjList.add(campaignPlacementObj);
					 }
				 }
			 }
		 }catch(Exception e){
				log.severe("Exception in createPlacementObjsForOrder : " + e.getMessage());
		 }
		return campaignPlacementObjList;
		 
	 }
	 
	 public CreativePage getCreativeObjFromDFP(DfpServices dfpServices, DfpSession session, long creativeId) throws Exception{
		 log.info("Inside getCreativeObjFromDFP of SmartCampaignPlannerService...");
		 CreativeServiceInterface creativeService =
			        dfpServices.get(session, CreativeServiceInterface.class);
		 StatementBuilder statementBuilder = new StatementBuilder()
	        .where("WHERE id = :id")
	        .orderBy("id ASC")
	        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
	        .withBindVariableValue("id", creativeId);
		 
		 CreativePage page = creativeService.getCreativesByStatement(statementBuilder.toStatement());
		 return page;
	 }
	 
	 public CreativeObj getCreativeObj(String creativeSize, String creativeType){
		 
		return null;
		 
	 }
	
	 /**
		 * @author Shubham Goel 
		 * @param DfpServices dfpServices : DFP service object
		 * @param DfpSession session : DFP session object  
		 * @param long comapanyId : Company ID i,e, Advertiser or Agency ID 
		 * @return CompanyPage: returns DFP company object for a given company ID.
	 */
	 public CompanyPage getCompanyObjFromDFP(DfpServices dfpServices, DfpSession session,long comapanyId) throws Exception{
		 log.info("Inside getCompanyObjFromDFP of SmartCampaignPlannerService...");
			 CompanyServiceInterface companyService = dfpServices.get(session, CompanyServiceInterface.class);
			 StatementBuilder statementBuilder = new StatementBuilder()
		        .where("WHERE id = :id")
		        .orderBy("id ASC")
		        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
		        .withBindVariableValue("id", comapanyId);
			 
			 CompanyPage page = companyService.getCompaniesByStatement(statementBuilder.toStatement());
			 return page;
	 }
	 
	 /**
		 * @author Shubham Goel 
		 * @param String dfpNetworkCode : DFP Network Code
		 * @return CompanyObj: returns CompanyObj for a given DFP network code from datastore.
	 */
	 @Override
	 public CompanyObj getCompanyByDFPNetworkCode(String dfpNetworkCode){
		 log.info("Inside getCompanyByDFPNetworkCode of SmartCampaignPlannerService...");
		 ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		 List<CompanyObj> companyObjList = new ArrayList<>();
		 CompanyObj companyObj = new CompanyObj();
		 
		 try{
			// companyObjList = plannerDAO.getCompanyByDFPNetworkCode(StringUtil.getLongValue(dfpNetworkCode));
			 companyObjList = plannerDAO.getAllCompanyList();
			 if(companyObjList!=null && companyObjList.size()>0){
				  //companyObj = companyObjList.get(0);
				 for (CompanyObj obj : companyObjList) {
					if(obj!=null && obj.getAdServerId()!=null && obj.getAdServerId().size()>0){
						for (String adServrId : obj.getAdServerId()) {
							if(dfpNetworkCode.equals(adServrId)){
								companyObj = obj;
								break;
							}
						}
					}
				}
			 }
		 }catch(Exception e){
				log.severe("Exception in getCompanyByDFPNetworkCode : " + e.getMessage());
		 }
		return companyObj;
	 }
	
	 /**
		 * @author Shubham Goel 
		 * @param String dfpNetworkCode : DFP Network Code
		 * @return CompanyObj: returns CompanyObj for 5678 network code and Lin Mobile company from datastore.
	 */
	 @Override
	public CompanyObj getCompanyObjForLinMobile(String dfpNetworkCode){
		 log.info("Inside getCompanyByDFPNetworkCode of SmartCampaignPlannerService...");
		 ISmartCampaignPlannerDAO plannerDAO = new SmartCampaignPlannerDAO();
		 List<CompanyObj> companyObjList = new ArrayList<>();
		 CompanyObj companyObj = new CompanyObj();
		 
		 try{
			// companyObjList = plannerDAO.getCompanyByDFPNetworkCode(StringUtil.getLongValue(dfpNetworkCode));
			 companyObjList = plannerDAO.getAllCompanyList();
			 if(companyObjList!=null && companyObjList.size()>0){
				  //companyObj = companyObjList.get(0);
				 for (CompanyObj obj : companyObjList) {
					if(obj!=null && obj.getAdServerId()!=null && obj.getAdServerId().size()>0){
						for (String adServrId : obj.getAdServerId()) {
							if(dfpNetworkCode.equals(adServrId) && obj.getCompanyName()!=null && obj.getCompanyName().trim().equalsIgnoreCase("Lin Mobile")){
								companyObj = obj;
								break;
							}
						}
					}
				}
			 }
		 }catch(Exception e){
				log.severe("Exception in getCompanyObjForLinMobile : " + e.getMessage());
		 }
		return companyObj;
	 }
	 
	 /**
		 * @author Shubham Goel 
		 * @param String dfpNetworkCode : DFP Network Code
		 * @param String accountName : Advertiser or Agency name
		 * @paramString accountDFPId : Advertiser or Agency ID
		 * @return AccountsEntity : returns AccountsEntity, this method update or create a AccountsEntity for give account id i,e, Advertiser or Agency. 
	 */
	 public AccountsEntity saveOrUpdateAccountEntity(String accountDFPId, String accountName, String dfpNetworkCode,
			 String adServerUserName, String companyId, String accountType) throws Exception{
		 ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		 AccountsEntity accountsEntity = new AccountsEntity();
		 if(accountName!=null && accountName.length()>0){
			 accountsEntity.setDfpAccountName(accountName);
			 if(accountType!=null && accountType.equalsIgnoreCase(LinMobileConstants.ADVERTISER_ID_PREFIX)){
				 accountsEntity.setAccountType(accountType);
				 accountsEntity.setAccountName(accountName+"("+LinMobileConstants.ADVERTISER_ID_PREFIX+")");
			 }else if(accountType!=null && accountType.equalsIgnoreCase(LinMobileConstants.AGENCY_ID_PREFIX)){
				 accountsEntity.setAccountType(accountType);
				 accountsEntity.setAccountName(accountName+"("+LinMobileConstants.AGENCY_ID_PREFIX+")");
			 }
		 }
		 accountsEntity.setStatus(LinMobileConstants.STATUS_ARRAY[0]);
		 if(accountDFPId!=null){
			 accountsEntity.setAccountDfpId(accountDFPId);
		 }
		 if(dfpNetworkCode!=null){
			 accountsEntity.setAdServerId(dfpNetworkCode);
		 }
		 if(companyId!=null){
			 accountsEntity.setCompanyId(companyId);
		 }
		 if(adServerUserName!=null){
			 accountsEntity.setAdServerUserName(adServerUserName);
		 }
		 log.info("dfpNetworkCode :"+dfpNetworkCode+"accountDFPId :"+accountDFPId+"companyId :"+companyId);
		 if(dfpNetworkCode!=null && accountDFPId!=null && companyId!=null){
				accountsEntity.setId(dfpNetworkCode+"_"+accountDFPId+"_"+companyId);
				campaignPlannerDAO.saveObject(accountsEntity);
		 }
		 
		return accountsEntity;
		 
	 }

	 
	@Override
	public List<SmartCampaignObj> loadCampaignsForHistoryLoading(String adServerNetworkCode) {
		List<SmartCampaignObj> smartCampaignList=null;
		ISmartCampaignPlannerDAO smartCampaignDAO=new SmartCampaignPlannerDAO();
		try {
			smartCampaignList=smartCampaignDAO.getAllCampaign(adServerNetworkCode);
		} catch (NumberFormatException | DataServiceException e) {
			log.severe("Exception :: "+e.getMessage());
		}
		return smartCampaignList;
	}

	@Override
	public SmartCampaignObj loadSmartCampaignByOrderId(String dfpOrderId, String networkCode) {
		
		SmartCampaignObj smartCampaignObj=null;
		ISmartCampaignPlannerDAO smartCampaignDAO=new SmartCampaignPlannerDAO();
		try {
			smartCampaignObj=smartCampaignDAO.getCampaignByDFPOrderId(Long.parseLong(dfpOrderId), networkCode);
		} catch (NumberFormatException | DataServiceException e) {
			log.severe("Exception :: "+e.getMessage());
		}
		return smartCampaignObj;
	}
	
	@Override
	public boolean loadHistoricalDataForDataStoreCampaigns(String dfpNetworkCode, String startCount, String endCount){
		List<SmartCampaignObj> campaignObjList = new ArrayList<>();
		String loadHistoricalDataURL = "/loadHistoricalData.lin";
		try{
			if(dfpNetworkCode!=null){
				campaignObjList = loadCampaignsForHistoryLoading(dfpNetworkCode);
				int start = StringUtil.getIntegerValue(startCount);
				int end = StringUtil.getIntegerValue(endCount);
				if(campaignObjList!=null && campaignObjList.size()>0){
					for(int i=start;i<=end;i++){
						SmartCampaignObj smartCampaignObj = new SmartCampaignObj();
						smartCampaignObj = campaignObjList.get(i);
						if(smartCampaignObj!=null && smartCampaignObj.getAdServerId()!=null ){
							TaskQueueUtil.loadHistoricalData(loadHistoricalDataURL, smartCampaignObj.getDfpOrderId()+"", smartCampaignObj.getAdServerId()); // Load Historical data for a particular order ID.
						}
					}
				}
			}
		}catch(Exception e){
			log.severe("Exception :: "+e.getMessage());
			return false;
		}
		return true;
		
	}
	
	 /**
	 * @author Shubham Goel 
	 * @param List<SmartCampaignObj> : List of smartCampaignObj
	 * @return Map<String,List<SmartCampaignObj>> : returns Map, companyId as key and list of smartCampaignObj as value.
 */
	@Override
	public Map<String,List<SmartCampaignObj>> getCampaignByCompanyId(List<SmartCampaignObj> campaignObjList){
		Map<String,List<SmartCampaignObj>> campaignByCompanyIdMap = new HashMap<>();
		try{
			if(campaignObjList!=null && campaignObjList.size()>0){
				for (SmartCampaignObj smartCampaignObj : campaignObjList) {
					List<SmartCampaignObj> campaignList = new ArrayList<>();
					if(smartCampaignObj!=null && smartCampaignObj.getDfpOrderId()!=0 && smartCampaignObj.getCompanyId()!=null && campaignByCompanyIdMap.containsKey(smartCampaignObj.getCompanyId())){
						campaignList = campaignByCompanyIdMap.get(smartCampaignObj.getCompanyId());
						campaignList.add(smartCampaignObj);
						campaignByCompanyIdMap.put(smartCampaignObj.getCompanyId(), campaignList);
					}else if(smartCampaignObj!=null && smartCampaignObj.getDfpOrderId()!=0 && smartCampaignObj.getCompanyId()!=null && !campaignByCompanyIdMap.containsKey(smartCampaignObj.getCompanyId())){
						campaignList.add(smartCampaignObj);
						campaignByCompanyIdMap.put(smartCampaignObj.getCompanyId(), campaignList);
					}
				}
			}
		}catch(Exception e){
			log.severe("Exception ::"+e.getMessage());
		}
		return campaignByCompanyIdMap;
		
	}


	@Override
	public String addCensusCategory(String group, String groupTxt,
			String gender, String bqColumn, String bqParentColumn,
			String bqMaleColumn, String bqFemaleColumn) {
		 ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		 CensusDTO censusDTO = new CensusDTO();
		 String message = "Updated Successfully";
		 
		 censusDTO.setGroup(group);
		 censusDTO.setGroupTxt(groupTxt);
		 censusDTO.setBqColumn(bqColumn);
		 censusDTO.setBqParentCol(bqParentColumn);
		 censusDTO.setActive(true);
		 
		 if(gender == "0" || gender.equalsIgnoreCase("0")){
			 censusDTO.setGender(false);
		 }else{
			 censusDTO.setGender(true);
		 }
		 
		 if(censusDTO.isGender() && (bqMaleColumn.length() == 0 || bqFemaleColumn.length() == 0)){
			 message = "Invalid Input";
		 }else{
			 censusDTO.setBqMaleCol(bqMaleColumn);
			 censusDTO.setBqFemaleCol(bqFemaleColumn);
			 try {
				campaignPlannerDAO.saveObject(censusDTO);
			} catch (DataServiceException e) {
				message = e.getMessage();
			}
		 }
		 
		 
		return message;
	}

	@Override
	public List<CensusDTO> getCensusCategory() {
		 ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		return campaignPlannerDAO.getAllCensus();
	}
	
	
	
	@Override
	/**
	 * @author Naresh Pokhriyal<br />
	 * Checks if placement name already exists and returns true if not else false. 
	 * @param campaignId : id of the campaign whose placement need to be checked.
	 * @param placementNameToCheck : placement name to be checked.
	 * returns boolean.
	 */
	public boolean isPlacementNameAvailable(String campaignId, String placementNameToCheck) throws Exception{
		boolean isPlacementNameAvailable = false;
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		List<SmartCampaignPlacementObj> placementObjList = campaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
		if(placementObjList != null && placementObjList.size() > 0) {
			isPlacementNameAvailable = true;
			for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
				if(smartCampaignPlacementObj.getPlacementName().equalsIgnoreCase(placementNameToCheck)) {
					isPlacementNameAvailable = false;				// placement name already exists, name not available.
					break;
				}
			}
		}
		return isPlacementNameAvailable;
	}

	@Override
	/**
	 * @author Naresh Pokhriyal<br />
	 * Creates copy of the placement and returns true if not else false. 
	 * @param campaignId : id of the campaign whose placement need to be copied.
	 * @param idOfPlacementToCopy : id of the placement which need to be copied.
	 * @param placementName : name of the copy of the placement.
	 * returns boolean.
	 */
	public boolean createCopyOfPlacement(String campaignId, long idOfPlacementToCopy, String placementName) throws Exception {
		boolean isPlacementCopied = false;
		ISmartCampaignPlannerDAO campaignPlannerDAO = new SmartCampaignPlannerDAO();
		SmartCampaignPlacementObj placementObjToCopy = null;
		List<SmartCampaignPlacementObj> placementObjList = campaignPlannerDAO.getAllPlacementOfCampaign(StringUtil.getLongValue(campaignId));
		if(placementObjList != null && placementObjList.size() > 0) {
			for (SmartCampaignPlacementObj smartCampaignPlacementObj : placementObjList) {
				if(smartCampaignPlacementObj.getPlacementName().equalsIgnoreCase(placementName)) {
					// placement name already exists, copy should not be made.
					log.warning("Placement name already exists, copy should not be made, placement matched id : "+
								smartCampaignPlacementObj.getPlacementId()+", placement name : "+smartCampaignPlacementObj.getPlacementName());
					throw new Exception("Placement name already exists.");
				}else if(smartCampaignPlacementObj.getPlacementId() == idOfPlacementToCopy) {
					placementObjToCopy = new SmartCampaignPlacementObj(smartCampaignPlacementObj);		// create copy of placement
				}
			}
			if(placementObjToCopy != null) {
				log.info("Placement found to copy");
				placementObjToCopy.setPlacementName(placementName);
				long placemetMaxId = campaignPlannerDAO.maxPlacementId() + 1;
				placementObjToCopy.setId(placemetMaxId);
				placementObjToCopy.setPlacementId(placemetMaxId);
				placementObjToCopy.setCopied(true);
				placementObjToCopy.setCopiedFromPlacementId(idOfPlacementToCopy);
				campaignPlannerDAO.saveObjectWithStrongConsistancy(placementObjToCopy);
				isPlacementCopied = true;
				log.info("Placement copy created successfully, placementId : "+placemetMaxId);
			}else {
				throw new Exception("Placement does not exist.");
			}
		}
		return isPlacementCopied;
	}

}
