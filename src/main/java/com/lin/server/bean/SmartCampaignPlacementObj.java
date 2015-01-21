package com.lin.server.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.DFPLineItemDTO;
import com.lin.web.dto.SmartCampaignFlightDTO;
import com.lin.web.dto.ZipDTO;
import com.lin.web.service.impl.ProductService;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.StringUtil;
@SuppressWarnings("serial")
@Entity
@Index
public class SmartCampaignPlacementObj implements Serializable{

	@Id	private Long id;
	@Parent	private Key<SmartCampaignObj> campaign;
	private Long placementId;
	private String placementName;
	private String impressions;
	private String startDate;  
	private String endDate;
	private String budget;
	private String rate;
	private List<CreativeObj> creativeObj;
	private List<PlatformObj> platformObj;
	private List<IABContextObj> contextObj;
	private List<DeviceObj> deviceObj;
	private String frequencyCap;
	private String frequencyCapUnit;
	private String goal;
	private String pacing;
	private List<SmartCampaignFlightDTO> flightObjList;
	private Integer deviceCapability = 0;
	// Demogarphic Targeting	
	private String upperAge;
	private String lowerAge;
	private String upperIncome;
	private String lowerIncome;
	private List<DropdownDataObj> educationList;
	private List<DropdownDataObj> ethnicityList;
	private String gender;
	private String isDemographic;
	private String itemType;
	private String selectedPlacementProducts;
	//Geogaraphic Targeting	
	private List<StateObj> stateObj;
	private List<CityDTO> cityObj;
	private List<ZipDTO> zipObj;
	private List<GeoTargetsObj> geoObj;
	private List<CountryObj> countryObj;
	private List<String> zipList;
	private String isGeographic;
    
	private List<DFPLineItemDTO> dfpLineItemList;
	
	private boolean copied;
	private long copiedFromPlacementId;
	
	private static final Logger log = Logger.getLogger(SmartCampaignPlacementObj.class.getName());
	
	//Add by Anup
	private String censusGender;
	private String censusAge;
	private String censusIncome;
	private String censusEducation;
	private String censusEthnicity;
	
	// copy constructor
	public SmartCampaignPlacementObj(SmartCampaignPlacementObj obj) {
		this.id = obj.id;
		this.campaign = obj.campaign;
		this.placementId = obj.placementId;
		this.placementName = obj.placementName;
		this.impressions = obj.impressions;
		this.startDate = obj.startDate;
		this.endDate = obj.endDate;
		this.budget = obj.budget;
		this.rate = obj.rate;
		this.creativeObj = obj.creativeObj;
		this.platformObj = obj.platformObj;
		this.contextObj = obj.contextObj;
		this.deviceObj = obj.deviceObj;
		this.frequencyCap = obj.frequencyCap;
		this.frequencyCapUnit = obj.frequencyCapUnit;
		this.goal = obj.goal;
		this.pacing = obj.pacing;
		this.flightObjList = obj.flightObjList;
		this.deviceCapability = obj.deviceCapability;
		this.upperAge = obj.upperAge;
		this.lowerAge = obj.lowerAge;
		this.upperIncome = obj.upperIncome;
		this.lowerIncome = obj.lowerIncome;
		this.educationList = obj.educationList;
		this.ethnicityList = obj.ethnicityList;
		this.gender = obj.gender;
		this.isDemographic = obj.isDemographic;
		this.itemType = obj.itemType;
		this.selectedPlacementProducts = obj.selectedPlacementProducts;
		this.stateObj = obj.stateObj;
		this.cityObj = obj.cityObj;
		this.zipObj = obj.zipObj;
		this.geoObj = obj.geoObj;
		this.countryObj = obj.countryObj;
		this.zipList = obj.zipList;
		this.isGeographic = obj.isGeographic;
		this.dfpLineItemList = obj.dfpLineItemList;
		this.copied = obj.copied;
		this.copiedFromPlacementId = obj.copiedFromPlacementId;
		
		this.censusAge = obj.censusAge;
		this.censusIncome = obj.censusIncome;
		this.censusEducation = obj.censusEducation;
		this.censusEthnicity = obj.censusEthnicity;
		this.censusGender = obj.censusGender;
	}

	@Override
	public String toString() {
		return "SmartCampaignPlacementObj [id=" + id + ", campaign=" + campaign
				+ ", placementId=" + placementId + ", placementName="
				+ placementName + ", impressions=" + impressions
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", budget=" + budget + ", rate=" + rate + ", creativeObj="
				+ creativeObj + ", platformObj=" + platformObj
				+ ", contextObj=" + contextObj + ", deviceObj=" + deviceObj
				+ ", frequencyCap=" + frequencyCap + ", frequencyCapUnit="
				+ frequencyCapUnit + ", goal=" + goal + ", pacing=" + pacing
				+ ", flightObjList=" + flightObjList + ", deviceCapability="
				+ deviceCapability + ", upperAge=" + upperAge + ", lowerAge="
				+ lowerAge + ", upperIncome=" + upperIncome + ", lowerIncome="
				+ lowerIncome + ", educationList=" + educationList
				+ ", ethnicityList=" + ethnicityList + ", gender=" + gender
				+ ", isDemographic=" + isDemographic + ", itemType=" + itemType
				+ ", selectedPlacementProducts=" + selectedPlacementProducts
				+ ", stateObj=" + stateObj + ", cityObj=" + cityObj
				+ ", zipObj=" + zipObj + ", geoObj=" + geoObj + ", countryObj="
				+ countryObj + ", zipList=" + zipList + ", isGeographic="
				+ isGeographic + ", dfpLineItemList=" + dfpLineItemList
				+ ", copied=" + copied + ", copiedFromPlacementId="
				+ copiedFromPlacementId + "]";
	}

	public SmartCampaignPlacementObj() {
	
	 }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Key<SmartCampaignObj> getCampaign() {
		return campaign;
	}

	public void setCampaign(Key<SmartCampaignObj> campaign) {
		this.campaign = campaign;
	}

	public String getPlacementName() {
		return placementName;
	}

	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public List<PlatformObj> getPlatformObj() {
		return platformObj;
	}

	public void setPlatformObj(List<PlatformObj> platformObj) {
		this.platformObj = platformObj;
	}

	public List<IABContextObj> getContextObj() {
		return contextObj;
	}

	public void setContextObj(List<IABContextObj> contextObj) {
		this.contextObj = contextObj;
	}

	public List<DeviceObj> getDeviceObj() {
		return deviceObj;
	}

	public void setDeviceObj(List<DeviceObj> deviceObj) {
		this.deviceObj = deviceObj;
	}

	public List<CreativeObj> getCreativeObj() {
		return creativeObj;
	}

	public void setCreativeObj(List<CreativeObj> creativeObj) {
		this.creativeObj = creativeObj;
	}

	public List<StateObj> getStateObj() {
		return stateObj;
	}

	public void setStateObj(List<StateObj> stateObj) {
		this.stateObj = stateObj;
	}


	public List<GeoTargetsObj> getGeoObj() {
		return geoObj;
	}

	public void setGeoObj(List<GeoTargetsObj> geoObj) {
		this.geoObj = geoObj;
	}

	public Long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(Long placementId) {
		this.placementId = placementId;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public List<CountryObj> getCountryObj() {
		return countryObj;
	}

	public void setCountryObj(List<CountryObj> countryObj) {
		this.countryObj = countryObj;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}


	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getFrequencyCap() {
		return frequencyCap;
	}

	public void setFrequencyCap(String frequencyCap) {
		this.frequencyCap = frequencyCap;
	}

	public String getFrequencyCapUnit() {
		return frequencyCapUnit;
	}

	public void setFrequencyCapUnit(String frequencyCapUnit) {
		this.frequencyCapUnit = frequencyCapUnit;
	}

	public String getUpperAge() {
		return upperAge;
	}

	public void setUpperAge(String upperAge) {
		this.upperAge = upperAge;
	}

	public String getLowerAge() {
		return lowerAge;
	}

	public void setLowerAge(String lowerAge) {
		this.lowerAge = lowerAge;
	}

	public List<DropdownDataObj> getEducationList() {
		return educationList;
	}

	public void setEducationList(List<DropdownDataObj> educationList) {
		this.educationList = educationList;
	}

	public List<DropdownDataObj> getEthnicityList() {
		return ethnicityList;
	}

	public void setEthnicityList(List<DropdownDataObj> ethnicityList) {
		this.ethnicityList = ethnicityList;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getZipList() {
		return zipList;
	}

	public void setZipList(List<String> zipList) {
		this.zipList = zipList;
	}

	public String getPacing() {
		return pacing;
	}

	public void setPacing(String pacing) {
		this.pacing = pacing;
	}

	public List<CityDTO> getCityObj() {
		return cityObj;
	}

	public void setCityObj(List<CityDTO> cityObj) {
		this.cityObj = cityObj;
	}

	public String getUpperIncome() {
		return upperIncome;
	}

	public void setUpperIncome(String upperIncome) {
		this.upperIncome = upperIncome;
	}

	public String getLowerIncome() {
		return lowerIncome;
	}

	public void setLowerIncome(String lowerIncome) {
		this.lowerIncome = lowerIncome;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<ZipDTO> getZipObj() {
		return zipObj;
	}

	public void setZipObj(List<ZipDTO> zipObj) {
		this.zipObj = zipObj;
	}

	public List<SmartCampaignFlightDTO> getFlightObjList() {
		return flightObjList;
	}

	public void setFlightObjList(List<SmartCampaignFlightDTO> flightObjList) {
		this.flightObjList = flightObjList;
	}

	public String getIsDemographic() {
		return isDemographic;
	}

	public void setIsDemographic(String isDemographic) {
		this.isDemographic = isDemographic;
	}

	public String getIsGeographic() {
		return isGeographic;
	}

	public void setIsGeographic(String isGeographic) {
		this.isGeographic = isGeographic;
	}

	public List<DFPLineItemDTO> getDfpLineItemList() {
		return dfpLineItemList;
	}

	public void setDfpLineItemList(List<DFPLineItemDTO> dfpLineItemList) {
		this.dfpLineItemList = dfpLineItemList;
	}
	
	
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getCompareString(SmartCampaignPlacementObj updatedPlacementObject) {
		log.info("In SmartCampaignPlacementObj's getCompareString...");
		StringBuilder placementChangesLog = new StringBuilder();
		DecimalFormat df = new DecimalFormat( "###,###,###,##0.00" );
		DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
		
		if(!this.getPlacementName().equals(updatedPlacementObject.getPlacementName())) {
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Placement Name", this.getPlacementName(), updatedPlacementObject.getPlacementName()));
		}
		
		if(!this.getStartDate().equals(updatedPlacementObject.getStartDate()) || !this.getEndDate().equals(updatedPlacementObject.getEndDate())) {
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Placement Duration", this.getStartDate()+" / "+this.getEndDate(), updatedPlacementObject.getStartDate()+" / "+updatedPlacementObject.getEndDate()));
		}
		
		long previousImpressions = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getImpressions()));
		long updatedImpressions = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getImpressions()));
		if(previousImpressions != updatedImpressions){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Goal", lf.format(previousImpressions), lf.format(updatedImpressions)));
		}
		
		double previousRate = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(this.getRate()));
		double updatedRate = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getRate()));
		if(previousRate != updatedRate){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Rate", "$"+df.format(previousRate), "$"+df.format(updatedRate)));
		}
		
		double previousBudget = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(this.getBudget()));
		double updatedBudget = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getBudget()));
		if(previousBudget != updatedBudget){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Budget", "$"+df.format(previousBudget), "$"+df.format(updatedBudget)));
		}
		
		// Creative
		if((this.getCreativeObj() == null || this.getCreativeObj().size() == 0) && updatedPlacementObject.getCreativeObj() != null && updatedPlacementObject.getCreativeObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Creative", updatedPlacementObject.getCreativeObj(), null));		// earlier empty, added now
		}else if((updatedPlacementObject.getCreativeObj() == null || updatedPlacementObject.getCreativeObj().size() == 0) && this.getCreativeObj() != null && this.getCreativeObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Creative", null, this.getCreativeObj()));							// removed all
		}else if(this.getCreativeObj() != null && this.getCreativeObj().size() > 0 && updatedPlacementObject.getCreativeObj() != null && updatedPlacementObject.getCreativeObj().size() > 0) {
			// added or removed
			List<CreativeObj> updatedList = updatedPlacementObject.getCreativeObj();
			List<CreativeObj> earlierList = this.getCreativeObj();
			List<CreativeObj> addedList = new ArrayList<>();
			for(CreativeObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Creative", addedList, earlierList));
		}
		
		double previousGoalCtr = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(this.getGoal()));
		double updatedGoalCtr = StringUtil.getDoubleValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getGoal()));
		if(previousGoalCtr != updatedGoalCtr){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Goal(CTR%)", df.format(previousGoalCtr), df.format(updatedGoalCtr)));
		}
		
		// Platform
		if((this.getPlatformObj() == null || this.getPlatformObj().size() == 0) && updatedPlacementObject.getPlatformObj() != null && updatedPlacementObject.getPlatformObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Platform", updatedPlacementObject.getPlatformObj(), null));		// earlier empty, added now
		}else if((updatedPlacementObject.getPlatformObj() == null || updatedPlacementObject.getPlatformObj().size() == 0) && this.getPlatformObj() != null && this.getPlatformObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Platform", null, this.getPlatformObj()));							// removed all
		}else if(this.getPlatformObj() != null && this.getPlatformObj().size() > 0 && updatedPlacementObject.getPlatformObj() != null && updatedPlacementObject.getPlatformObj().size() > 0) {
			// added or removed
			List<PlatformObj> updatedList = updatedPlacementObject.getPlatformObj();
			List<PlatformObj> earlierList = this.getPlatformObj();
			List<PlatformObj> addedList = new ArrayList<>();
			for(PlatformObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Platform", addedList, earlierList));
		}
		
		// Device
		if((this.getDeviceObj() == null || this.getDeviceObj().size() == 0) && updatedPlacementObject.getDeviceObj() != null && updatedPlacementObject.getDeviceObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Device", updatedPlacementObject.getDeviceObj(), null));		// earlier empty, added now
		}else if((updatedPlacementObject.getDeviceObj() == null || updatedPlacementObject.getDeviceObj().size() == 0) && this.getDeviceObj() != null && this.getDeviceObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Device", null, this.getDeviceObj()));							// removed all
		}else if(this.getDeviceObj() != null && this.getDeviceObj().size() > 0 && updatedPlacementObject.getDeviceObj() != null && updatedPlacementObject.getDeviceObj().size() > 0) {
			// added or removed
			List<DeviceObj> updatedList = updatedPlacementObject.getDeviceObj();
			List<DeviceObj> earlierList = this.getDeviceObj();
			List<DeviceObj> addedList = new ArrayList<>();
			for(DeviceObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Device", addedList, earlierList));
		}
		
		long previousFrequency = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getFrequencyCap()));
		long updatedFrequency = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getFrequencyCap()));
		if((previousFrequency != updatedFrequency) || !this.getFrequencyCapUnit().equals(updatedPlacementObject.getFrequencyCapUnit())) {
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Frequency", lf.format(previousFrequency)+" per "+this.getFrequencyCapUnit(), lf.format(updatedFrequency)+" per "+updatedPlacementObject.getFrequencyCapUnit()));
		}
		
		// Context Category Type
		if((this.getContextObj() == null || this.getContextObj().size() == 0) && updatedPlacementObject.getContextObj() != null && updatedPlacementObject.getContextObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Context Category Type", updatedPlacementObject.getContextObj(), null));		// earlier empty, added now
		}else if((updatedPlacementObject.getContextObj() == null || updatedPlacementObject.getContextObj().size() == 0) && this.getContextObj() != null && this.getContextObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Context Category Type", null, this.getContextObj()));							// removed all
		}else if(this.getContextObj() != null && this.getContextObj().size() > 0 && updatedPlacementObject.getContextObj() != null && updatedPlacementObject.getContextObj().size() > 0) {
			// added or removed
			List<IABContextObj> updatedList = updatedPlacementObject.getContextObj();
			List<IABContextObj> earlierList = this.getContextObj();
			List<IABContextObj> addedList = new ArrayList<>();
			for(IABContextObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Context Category Type", addedList, earlierList));
		}
		
		long previousPacing = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getPacing()));
		long updatedPacing = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getPacing()));
		if(previousPacing != updatedPacing){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Daily Pacing", lf.format(previousPacing), lf.format(updatedPacing)));
		}
		
		long previousLowerAge = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getLowerAge()));
		long updatedLowerAge = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getLowerAge()));
		long previousUpperAge = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getUpperAge()));
		long updatedUpperAge = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getUpperAge()));
		if((previousLowerAge != updatedLowerAge) || (previousUpperAge != updatedUpperAge)){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Age", lf.format(previousLowerAge)+" - "+lf.format(previousUpperAge), lf.format(updatedLowerAge)+" - "+lf.format(updatedUpperAge)));
		}
		
		long previousLowerIncome = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getLowerIncome()));
		long updatedLowerIncome = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getLowerIncome()));
		long previousUpperIncome = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(this.getUpperIncome()));
		long updatedUpperIncome = StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(updatedPlacementObject.getUpperIncome()));
		if((previousLowerIncome != updatedLowerIncome) || (previousUpperIncome != updatedUpperIncome)){
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Income", "$"+lf.format(previousLowerIncome)+" - "+lf.format(previousUpperIncome), "$"+lf.format(updatedLowerIncome)+" - "+lf.format(updatedUpperIncome)));
		}
		
		// Education
		if((this.getEducationList() == null || this.getEducationList().size() == 0) && updatedPlacementObject.getEducationList() != null && updatedPlacementObject.getEducationList().size() > 0) {
			placementChangesLog.append(listChangeLog("Education", updatedPlacementObject.getEducationList(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getEducationList() == null || updatedPlacementObject.getEducationList().size() == 0) && this.getEducationList() != null && this.getEducationList().size() > 0) {
			placementChangesLog.append(listChangeLog("Education", null, this.getEducationList()));									// removed all
		}else if(this.getEducationList() != null && this.getEducationList().size() > 0 && updatedPlacementObject.getEducationList() != null && updatedPlacementObject.getEducationList().size() > 0) {
			// added or removed
			List<DropdownDataObj> updatedList = updatedPlacementObject.getEducationList();
			List<DropdownDataObj> earlierList = this.getEducationList();
			List<DropdownDataObj> addedList = new ArrayList<>();
			for(DropdownDataObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Education", addedList, earlierList));
		}
		
		// Ethinicity
		if((this.getEthnicityList() == null || this.getEthnicityList().size() == 0) && updatedPlacementObject.getEthnicityList() != null && updatedPlacementObject.getEthnicityList().size() > 0) {
			placementChangesLog.append(listChangeLog("Ethinicity", updatedPlacementObject.getEthnicityList(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getEthnicityList() == null || updatedPlacementObject.getEthnicityList().size() == 0) && this.getEthnicityList() != null && this.getEthnicityList().size() > 0) {
			placementChangesLog.append(listChangeLog("Ethinicity", null, this.getEthnicityList()));									// removed all
		}else if(this.getEthnicityList() != null && this.getEthnicityList().size() > 0 && updatedPlacementObject.getEthnicityList() != null && updatedPlacementObject.getEthnicityList().size() > 0) {
			// added or removed
			List<DropdownDataObj> updatedList = updatedPlacementObject.getEthnicityList();
			List<DropdownDataObj> earlierList = this.getEthnicityList();
			List<DropdownDataObj> addedList = new ArrayList<>();
			for(DropdownDataObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Ethinicity", addedList, earlierList));
		}
		
		if(!this.getGender().equals(updatedPlacementObject.getGender())) {
			placementChangesLog.append(LinMobileUtil.changeFromToLog("Gender", this.getGender(), updatedPlacementObject.getGender()));
		} 
		
		// Country
		if((this.getCountryObj() == null || this.getCountryObj().size() == 0) && updatedPlacementObject.getCountryObj() != null && updatedPlacementObject.getCountryObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Country", updatedPlacementObject.getCountryObj(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getCountryObj() == null || updatedPlacementObject.getCountryObj().size() == 0) && this.getCountryObj() != null && this.getCountryObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Country", null, this.getCountryObj()));									// removed all
		}else if(this.getCountryObj() != null && this.getCountryObj().size() > 0 && updatedPlacementObject.getCountryObj() != null && updatedPlacementObject.getCountryObj().size() > 0) {
			// added or removed
			List<CountryObj> updatedList = updatedPlacementObject.getCountryObj();
			List<CountryObj> earlierList = this.getCountryObj();
			List<CountryObj> addedList = new ArrayList<>();
			for(CountryObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Country", addedList, earlierList));
		}
		
		// DMA
		if((this.getGeoObj() == null || this.getGeoObj().size() == 0) && updatedPlacementObject.getGeoObj() != null && updatedPlacementObject.getGeoObj().size() > 0) {
			placementChangesLog.append(listChangeLog("DMA", updatedPlacementObject.getGeoObj(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getGeoObj() == null || updatedPlacementObject.getGeoObj().size() == 0) && this.getGeoObj() != null && this.getGeoObj().size() > 0) {
			placementChangesLog.append(listChangeLog("DMA", null, this.getGeoObj()));									// removed all
		}else if(this.getGeoObj() != null && this.getGeoObj().size() > 0 && updatedPlacementObject.getGeoObj() != null && updatedPlacementObject.getGeoObj().size() > 0) {
			// added or removed
			List<GeoTargetsObj> updatedList = updatedPlacementObject.getGeoObj();
			List<GeoTargetsObj> earlierList = this.getGeoObj();
			List<GeoTargetsObj> addedList = new ArrayList<>();
			for(GeoTargetsObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("DMA", addedList, earlierList));
		}
		
		// State
		if((this.getStateObj() == null || this.getStateObj().size() == 0) && updatedPlacementObject.getStateObj() != null && updatedPlacementObject.getStateObj().size() > 0) {
			placementChangesLog.append(listChangeLog("State", updatedPlacementObject.getStateObj(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getStateObj() == null || updatedPlacementObject.getStateObj().size() == 0) && this.getStateObj() != null && this.getStateObj().size() > 0) {
			placementChangesLog.append(listChangeLog("State", null, this.getStateObj()));									// removed all
		}else if(this.getStateObj() != null && this.getStateObj().size() > 0 && updatedPlacementObject.getStateObj() != null && updatedPlacementObject.getStateObj().size() > 0) {
			// added or removed
			List<StateObj> updatedList = updatedPlacementObject.getStateObj();
			List<StateObj> earlierList = this.getStateObj();
			List<StateObj> addedList = new ArrayList<>();
			for(StateObj updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("State", addedList, earlierList));
		}
		
		// City
		if((this.getCityObj() == null || this.getCityObj().size() == 0) && updatedPlacementObject.getCityObj() != null && updatedPlacementObject.getCityObj().size() > 0) {
			placementChangesLog.append(listChangeLog("City", updatedPlacementObject.getCityObj(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getCityObj() == null || updatedPlacementObject.getCityObj().size() == 0) && this.getCityObj() != null && this.getCityObj().size() > 0) {
			placementChangesLog.append(listChangeLog("City", null, this.getCityObj()));									// removed all
		}else if(this.getCityObj() != null && this.getCityObj().size() > 0 && updatedPlacementObject.getCityObj() != null && updatedPlacementObject.getCityObj().size() > 0) {
			// added or removed
			List<CityDTO> updatedList = updatedPlacementObject.getCityObj();
			List<CityDTO> earlierList = this.getCityObj();
			List<CityDTO> addedList = new ArrayList<>();
			for(CityDTO updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("City", addedList, earlierList));
		}
		
		// Zip
		if((this.getZipObj() == null || this.getZipObj().size() == 0) && updatedPlacementObject.getZipObj() != null && updatedPlacementObject.getZipObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Zip", updatedPlacementObject.getZipObj(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getZipObj() == null || updatedPlacementObject.getZipObj().size() == 0) && this.getZipObj() != null && this.getZipObj().size() > 0) {
			placementChangesLog.append(listChangeLog("Zip", null, this.getZipObj()));									// removed all
		}else if(this.getZipObj() != null && this.getZipObj().size() > 0 && updatedPlacementObject.getZipObj() != null && updatedPlacementObject.getZipObj().size() > 0) {
			// added or removed
			List<ZipDTO> updatedList = updatedPlacementObject.getZipObj();
			List<ZipDTO> earlierList = this.getZipObj();
			List<ZipDTO> addedList = new ArrayList<>();
			for(ZipDTO updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Zip", addedList, earlierList));
		}
		
		// Flight Duration	
		if((this.getFlightObjList() == null || this.getFlightObjList().size() == 0) && updatedPlacementObject.getFlightObjList() != null && updatedPlacementObject.getFlightObjList().size() > 0) {
			placementChangesLog.append(listChangeLog("Flight Duration", updatedPlacementObject.getFlightObjList(), null));				// earlier empty, added now
		}else if((updatedPlacementObject.getFlightObjList() == null || updatedPlacementObject.getFlightObjList().size() == 0) && this.getFlightObjList() != null && this.getFlightObjList().size() > 0) {
			placementChangesLog.append(listChangeLog("Flight Duration", null, this.getFlightObjList()));									// removed all
		}else if(this.getFlightObjList() != null && this.getFlightObjList().size() > 0 && updatedPlacementObject.getFlightObjList() != null && updatedPlacementObject.getFlightObjList().size() > 0) {
			// added or removed
			List<SmartCampaignFlightDTO> updatedList = updatedPlacementObject.getFlightObjList();
			List<SmartCampaignFlightDTO> earlierList = this.getFlightObjList();
			List<SmartCampaignFlightDTO> addedList = new ArrayList<>();
			for(SmartCampaignFlightDTO updatedObj : updatedList) {
				if(earlierList.contains(updatedObj)) {
					earlierList.remove(updatedObj);				// remove, once matched item shouldn't come back for match.
				}else {
					addedList.add(updatedObj);
				}
			}
			placementChangesLog.append(listChangeLog("Flight Duration", addedList, earlierList));
		}

		return placementChangesLog.toString();
	}
	
	
	public static String listChangeLog(String updatedListName, List<? extends Object> addedList, List<? extends Object> deletedList) {
		String returnStr = "";
		if((addedList == null || addedList.size() == 0) && (deletedList == null || deletedList.size() == 0)) {
			return "";
		}else {
			returnStr = updatedListName + " updated : <br>";
			if(addedList != null && addedList.size() > 0) {
				returnStr = returnStr + changeString(addedList, "+ ");
			}if(deletedList != null && deletedList.size() > 0) {
				returnStr = returnStr + changeString(deletedList, "- ");
			}
		}
		return returnStr;
	}
	
	public static String changeString(List<? extends Object> list, String prefixString) {
		StringBuilder changeString = new StringBuilder(); 
		if(list != null && list.size() > 0) {
			if(list.get(0) instanceof DropdownDataObj) {
				for(Object obj : list) {
					DropdownDataObj dropdownDataObj = (DropdownDataObj) obj;
					changeString.append(prefixString + dropdownDataObj.getValue()+"<br>");
				}
			}else if(list.get(0) instanceof CreativeObj) {
				for(Object obj : list) {
					CreativeObj dataObj = (CreativeObj) obj;
					changeString.append(prefixString + dataObj.getFormat() +"-"+ dataObj.getSize() +"<br>");
				}
			}else if(list.get(0) instanceof PlatformObj) {
				for(Object obj : list) {
					PlatformObj dataObj = (PlatformObj) obj;
					changeString.append(prefixString + dataObj.getText() +"<br>");
				}
			}else if(list.get(0) instanceof DeviceObj) {
				for(Object obj : list) {
					DeviceObj dataObj = (DeviceObj) obj;
					changeString.append(prefixString + dataObj.getText() +"<br>");
				}
			}else if(list.get(0) instanceof CountryObj) {
				for(Object obj : list) {
					CountryObj dataObj = (CountryObj) obj;
					changeString.append(prefixString + dataObj.getText() +"<br>");
				}
			}else if(list.get(0) instanceof GeoTargetsObj) {
				for(Object obj : list) {
					GeoTargetsObj dataObj = (GeoTargetsObj) obj;
					changeString.append(prefixString + dataObj.getGeoTargetsName() +"<br>");
				}
			}else if(list.get(0) instanceof StateObj) {
				for(Object obj : list) {
					StateObj dataObj = (StateObj) obj;
					changeString.append(prefixString + dataObj.getText() +"<br>");
				}
			}else if(list.get(0) instanceof CityDTO) {
				for(Object obj : list) {
					CityDTO dataObj = (CityDTO) obj;
					changeString.append(prefixString + dataObj.getText() +"<br>");
				}
			}else if(list.get(0) instanceof ZipDTO) {
				for(Object obj : list) {
					ZipDTO dataObj = (ZipDTO) obj;
					changeString.append(prefixString + dataObj.getCityName() +"<br>");
				}
			}else if(list.get(0) instanceof SmartCampaignFlightDTO) {
				DecimalFormat lf = new DecimalFormat( "###,###,###,###" );
				for(Object obj : list) {
					SmartCampaignFlightDTO dataObj = (SmartCampaignFlightDTO) obj;
					changeString.append(prefixString + "Duration : " + dataObj.getStartDate() + " / " + dataObj.getEndDate() 
							+ ", Goal : "+ lf.format(StringUtil.getLongValue(StringUtil.removeMediaPlanFormatters(dataObj.getGoal()))) +"<br>");
				}
			}else if(list.get(0) instanceof IABContextObj) {
				for(Object object : list) {
					IABContextObj dataObj = (IABContextObj) object;
					if(dataObj.getGroup() != null && dataObj.getGroup().length() > 0) {
						changeString.append(prefixString + dataObj.getGroup());
					}
					if(dataObj.getSubgroup() != null && dataObj.getSubgroup().length() > 0 && !dataObj.getSubgroup().equals(ProductService.allOption)) {
						changeString.append(" - ");
						if(dataObj.getGroup() == null || dataObj.getGroup().length() == 0) {
							changeString.append(prefixString);
						}
						changeString.append(dataObj.getSubgroup());
					}
					changeString.append("<br>");
				}
			}
		}
		return changeString.toString();
	}

	public Integer getDeviceCapability() {
		return deviceCapability;
	}

	public void setDeviceCapability(Integer deviceCapability) {
		this.deviceCapability = deviceCapability;
	}

	public boolean isCopied() {
		return copied;
	}

	public void setCopied(boolean copied) {
		this.copied = copied;
	}

	public long getCopiedFromPlacementId() {
		return copiedFromPlacementId;
	}

	public void setCopiedFromPlacementId(long copiedFromPlacementId) {
		this.copiedFromPlacementId = copiedFromPlacementId;
	}
	
	public String getSelectedPlacementProducts() {
		return selectedPlacementProducts;
	}

	public void setSelectedPlacementProducts(String selectedPlacementProducts) {
		this.selectedPlacementProducts = selectedPlacementProducts;
	}

	public String getCensusGender() {
		return censusGender;
	}

	public void setCensusGender(String censusGender) {
		this.censusGender = censusGender;
	}

	public String getCensusAge() {
		return censusAge;
	}

	public void setCensusAge(String censusAge) {
		this.censusAge = censusAge;
	}

	public String getCensusIncome() {
		return censusIncome;
	}

	public void setCensusIncome(String censusIncome) {
		this.censusIncome = censusIncome;
	}

	public String getCensusEducation() {
		return censusEducation;
	}

	public void setCensusEducation(String censusEducation) {
		this.censusEducation = censusEducation;
	}

	public String getCensusEthnicity() {
		return censusEthnicity;
	}

	public void setCensusEthnicity(String censusEthnicity) {
		this.censusEthnicity = censusEthnicity;
	}
	
	
	
}
