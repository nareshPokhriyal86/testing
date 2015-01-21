package com.lin.web.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.SmartCampaignFlightObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;

@SuppressWarnings("serial")
public class UnifiedCampaignDTO implements Serializable {
	public static final String STANDARD_ITEM_TYPE = "1";
	public static final String SPONSORSHIP_ITEM_TYPE = "2";
	private long id;
	private long placementId;
	private long dfpOrderId;
	private String dfpOrderName;
	private int campaignStatus;
	public boolean isPlacementSave;
	private String campaignName;
	private String rate;
	private String budget;
	private String agencyName;
	private String agencyAddress;
	private String agencyPhone;
	private String agencyEmail;
	private String agencyFax;
	private String agencyZip;
	private String advertiserName;
	private String advertiserAddress;
	private String advertiserPhone;
	private String advertiserEmail;
	private String advertiserFax;
	private String advertiserZip;
	private String startDate;
	private String endDate;
	private String itemType = STANDARD_ITEM_TYPE;
	private Integer deviceCapability = 0;
	private String totalGoal;
	private String statusId;
	private String notes;
	private List<SmartCampaignObj> campaignObjList;
	private String selectedAdvertiser;
	private String selectedAgency;
	
	private String selectedDMA;
	private String selectedRateType;
	private String selectedPlatform;
	private String selectedCreative;
	private String selectedState;
	private String selectedCity;
	private String selectedZip;
	private String selectedBehavior;
	private String selectedDevice;
	private String selectedDemographic;
	private String selectedContext;
	private String selectedCountry;
	
	private String[] selectedFlightStartdate;
	private String[] selectedFlightEnddate;
	private String[] selectedFlightGoal;
	
	private String[] flightStartdate;
	private String[] flightEnddate;
	private String[] flightGoal;
	
	private String[] placementName;
	private String[] placementImpression;
	private String[] placementStartDate;
	private String[] placementEndtDate;
	
	private int mediaPlanEditStatus; 
	private int mediaPlanStatus;
	
	private boolean hasProcessing;
	private boolean hasMediaPlan;
	private boolean hasSetupOnDFP;
	private boolean hasMigrated;

	private String selectedPlacementProducts; // products selected from map. Prefilled in placement form.
	
	private List<CommonDTO> plateformList;
	private List<CommonDTO> dmaList;
	private List<CommonDTO> cityList;
	private List<CommonDTO> stateList;
	private List<CommonDTO> zipList;
	private List<CommonDTO> creativeList;
	private List<CommonDTO> deviceList;
	private List<CommonDTO> contextList;
	private List<CommonDTO> rateTypeList;
	private List<CommonDTO> countryList;
	private List<CommonDTO> advertiserList;
	private List<CommonDTO> agencyList;
	
	private List<CommonDTO> selectedDMAList;
	private List<CommonDTO> selectedRateTypeList;
	private List<CommonDTO> selectedPlatformList;
	private List<CommonDTO> selectedCreativeList;
	private List<CommonDTO> selectedStateList;
	private List<CommonDTO> selectedCityList;
	private List<CommonDTO> selectedZipList;
	private List<CommonDTO> selectedBehaviorList;
	private List<CommonDTO> selectedDeviceList;
	private List<CommonDTO> selectedDemographicList;
	private List<CommonDTO> selectedContextList;
	private List<CommonDTO> selectedCountryList;
	private List<CommonDTO> selectedAdvertiserList;
	private List<CommonDTO> selectedAgencyList;
	private String cityJSON;
	private String zipJSON;
	private boolean geoFencing;
	private boolean demoTarget;
	private boolean behaTarget;
	
	
	public boolean pGeoFencing;
	public boolean pDemoTarget;
	public boolean pBehaTarget;
	
	//placement data
	public String isGeographic;
	public int pCount;
	public String pName;
	public String pStartDate;
	public String pEndDate;
	public String pImpression;
	public String pBudget;
	public String pGoal;
	public String pacing;
	private String frequencyCap;
	private String frequencyCapUnit;
	private String placementStatus;
	private List<CommonDTO> frequencyCapList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> selectedPlatformPlacementList;
	private List<CommonDTO> selectedCreativePlateformList;
	private List<CommonDTO> selectedCreativePlacementList;
	private List<CommonDTO> selectedContextPlacementList;
	private List<CommonDTO> selectedDevicePlacementList;
	private List<CommonDTO> selectedCountryPlatcementList;
	private List<CommonDTO> selectedDMAPlacementList;
	private List<CommonDTO> selectedStatePlacementList;
	private List<CommonDTO> selectedFrequencyUnitList;
	
	private List<SmartCampaignPlacementObj> placementByCampaignList;
	private  List<SmartCampaignFlightObj> flightByPlacementList;
	public String[] pSelectedDMA;
	public String[] pSelectedPlatform;
	public String[] pSelectedCreative;
	public String[] pSelectedState;
	public String[] pSelectedCity;
	public String[] pSelectedDevice;
	public String[] pSelectedContext;
	public String[] pSelectedCountry;
	
	//Demographic Targeting
	private String isDemographic;
	private String upperAge;
	private String lowerAge;
	private String upperIncome;
	private String lowerIncome;
	private String age;
	private String income;
	private String[] selectedEducation;
	private String[] selectedEthnicity;
	private String gender;
	private List<CommonDTO> selectedEducationList;
	private List<CommonDTO> selectedEthinicityList;
	private List<CommonDTO> educationList;
	private List<CommonDTO> ethinicityList;
	private List<CommonDTO> selectedGender;
	
    private List<SmartCampaignFlightDTO> flightList;
	private List<AdvertiserObj> advertisers;
	private List<AgencyObj> agencies;
	
	
	//Added any Anup for new Census Category
	private String selectedCensusGender;
	private String selectedCensusAge;
	private String selectedCensusIncome;
	private String selectedCensusEducation;
	private String selectedCensusEthnicity;
	private String selectedCensusRank;
	
	
	public UnifiedCampaignDTO(){
		
	}
	
	


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnifiedCampaignDTO [id=");
		builder.append(id);
		builder.append(", placementId=");
		builder.append(placementId);
		builder.append(", dfpOrderId=");
		builder.append(dfpOrderId);
		builder.append(", dfpOrderName=");
		builder.append(dfpOrderName);
		builder.append(", campaignStatus=");
		builder.append(campaignStatus);
		builder.append(", isPlacementSave=");
		builder.append(isPlacementSave);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", rate=");
		builder.append(rate);
		builder.append(", budget=");
		builder.append(budget);
		builder.append(", agencyName=");
		builder.append(agencyName);
		builder.append(", agencyAddress=");
		builder.append(agencyAddress);
		builder.append(", agencyPhone=");
		builder.append(agencyPhone);
		builder.append(", agencyEmail=");
		builder.append(agencyEmail);
		builder.append(", agencyFax=");
		builder.append(agencyFax);
		builder.append(", agencyZip=");
		builder.append(agencyZip);
		builder.append(", advertiserName=");
		builder.append(advertiserName);
		builder.append(", advertiserAddress=");
		builder.append(advertiserAddress);
		builder.append(", advertiserPhone=");
		builder.append(advertiserPhone);
		builder.append(", advertiserEmail=");
		builder.append(advertiserEmail);
		builder.append(", advertiserFax=");
		builder.append(advertiserFax);
		builder.append(", advertiserZip=");
		builder.append(advertiserZip);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", totalGoal=");
		builder.append(totalGoal);
		builder.append(", statusId=");
		builder.append(statusId);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", campaignObjList=");
		builder.append(campaignObjList);
		builder.append(", selectedAdvertiser=");
		builder.append(selectedAdvertiser);
		builder.append(", selectedAgency=");
		builder.append(selectedAgency);
		builder.append(", selectedDMA=");
		builder.append(selectedDMA);
		builder.append(", selectedRateType=");
		builder.append(selectedRateType);
		builder.append(", selectedPlatform=");
		builder.append(selectedPlatform);
		builder.append(", selectedCreative=");
		builder.append(selectedCreative);
		builder.append(", selectedState=");
		builder.append(selectedState);
		builder.append(", selectedCity=");
		builder.append(selectedCity);
		builder.append(", selectedZip=");
		builder.append(selectedZip);
		builder.append(", selectedBehavior=");
		builder.append(selectedBehavior);
		builder.append(", selectedDevice=");
		builder.append(selectedDevice);
		builder.append(", selectedDemographic=");
		builder.append(selectedDemographic);
		builder.append(", selectedContext=");
		builder.append(selectedContext);
		builder.append(", selectedCountry=");
		builder.append(selectedCountry);
		builder.append(", selectedFlightStartdate=");
		builder.append(Arrays.toString(selectedFlightStartdate));
		builder.append(", selectedFlightEnddate=");
		builder.append(Arrays.toString(selectedFlightEnddate));
		builder.append(", selectedFlightGoal=");
		builder.append(Arrays.toString(selectedFlightGoal));
		builder.append(", flightStartdate=");
		builder.append(Arrays.toString(flightStartdate));
		builder.append(", flightEnddate=");
		builder.append(Arrays.toString(flightEnddate));
		builder.append(", flightGoal=");
		builder.append(Arrays.toString(flightGoal));
		builder.append(", placementName=");
		builder.append(Arrays.toString(placementName));
		builder.append(", placementImpression=");
		builder.append(Arrays.toString(placementImpression));
		builder.append(", placementStartDate=");
		builder.append(Arrays.toString(placementStartDate));
		builder.append(", placementEndtDate=");
		builder.append(Arrays.toString(placementEndtDate));
		builder.append(", mediaPlanEditStatus=");
		builder.append(mediaPlanEditStatus);
		builder.append(", mediaPlanStatus=");
		builder.append(mediaPlanStatus);
		builder.append(", hasProcessing=");
		builder.append(hasProcessing);
		builder.append(", hasMediaPlan=");
		builder.append(hasMediaPlan);
		builder.append(", hasSetupOnDFP=");
		builder.append(hasSetupOnDFP);
		builder.append(", hasMigrated=");
		builder.append(hasMigrated);
		builder.append(", plateformList=");
		builder.append(plateformList);
		builder.append(", dmaList=");
		builder.append(dmaList);
		builder.append(", cityList=");
		builder.append(cityList);
		builder.append(", stateList=");
		builder.append(stateList);
		builder.append(", zipList=");
		builder.append(zipList);
		builder.append(", creativeList=");
		builder.append(creativeList);
		builder.append(", deviceList=");
		builder.append(deviceList);
		builder.append(", contextList=");
		builder.append(contextList);
		builder.append(", rateTypeList=");
		builder.append(rateTypeList);
		builder.append(", countryList=");
		builder.append(countryList);
		builder.append(", advertiserList=");
		builder.append(advertiserList);
		builder.append(", agencyList=");
		builder.append(agencyList);
		builder.append(", selectedDMAList=");
		builder.append(selectedDMAList);
		builder.append(", selectedRateTypeList=");
		builder.append(selectedRateTypeList);
		builder.append(", selectedPlatformList=");
		builder.append(selectedPlatformList);
		builder.append(", selectedCreativeList=");
		builder.append(selectedCreativeList);
		builder.append(", selectedStateList=");
		builder.append(selectedStateList);
		builder.append(", selectedCityList=");
		builder.append(selectedCityList);
		builder.append(", selectedZipList=");
		builder.append(selectedZipList);
		builder.append(", selectedBehaviorList=");
		builder.append(selectedBehaviorList);
		builder.append(", selectedDeviceList=");
		builder.append(selectedDeviceList);
		builder.append(", selectedDemographicList=");
		builder.append(selectedDemographicList);
		builder.append(", selectedContextList=");
		builder.append(selectedContextList);
		builder.append(", selectedCountryList=");
		builder.append(selectedCountryList);
		builder.append(", selectedAdvertiserList=");
		builder.append(selectedAdvertiserList);
		builder.append(", selectedAgencyList=");
		builder.append(selectedAgencyList);
		builder.append(", cityJSON=");
		builder.append(cityJSON);
		builder.append(", zipJSON=");
		builder.append(zipJSON);
		builder.append(", geoFencing=");
		builder.append(geoFencing);
		builder.append(", demoTarget=");
		builder.append(demoTarget);
		builder.append(", behaTarget=");
		builder.append(behaTarget);
		builder.append(", pGeoFencing=");
		builder.append(pGeoFencing);
		builder.append(", pDemoTarget=");
		builder.append(pDemoTarget);
		builder.append(", pBehaTarget=");
		builder.append(pBehaTarget);
		builder.append(", isGeographic=");
		builder.append(isGeographic);
		builder.append(", pCount=");
		builder.append(pCount);
		builder.append(", pName=");
		builder.append(pName);
		builder.append(", pStartDate=");
		builder.append(pStartDate);
		builder.append(", pEndDate=");
		builder.append(pEndDate);
		builder.append(", pImpression=");
		builder.append(pImpression);
		builder.append(", pBudget=");
		builder.append(pBudget);
		builder.append(", pGoal=");
		builder.append(pGoal);
		builder.append(", pacing=");
		builder.append(pacing);
		builder.append(", frequencyCap=");
		builder.append(frequencyCap);
		builder.append(", frequencyCapUnit=");
		builder.append(frequencyCapUnit);
		builder.append(", placementStatus=");
		builder.append(placementStatus);
		builder.append(", frequencyCapList=");
		builder.append(frequencyCapList);
		builder.append(", genderList=");
		builder.append(genderList);
		builder.append(", selectedPlatformPlacementList=");
		builder.append(selectedPlatformPlacementList);
		builder.append(", selectedCreativePlateformList=");
		builder.append(selectedCreativePlateformList);
		builder.append(", selectedCreativePlacementList=");
		builder.append(selectedCreativePlacementList);
		builder.append(", selectedContextPlacementList=");
		builder.append(selectedContextPlacementList);
		builder.append(", selectedDevicePlacementList=");
		builder.append(selectedDevicePlacementList);
		builder.append(", selectedCountryPlatcementList=");
		builder.append(selectedCountryPlatcementList);
		builder.append(", selectedDMAPlacementList=");
		builder.append(selectedDMAPlacementList);
		builder.append(", selectedStatePlacementList=");
		builder.append(selectedStatePlacementList);
		builder.append(", selectedFrequencyUnitList=");
		builder.append(selectedFrequencyUnitList);
		builder.append(", placementByCampaignList=");
		builder.append(placementByCampaignList);
		builder.append(", flightByPlacementList=");
		builder.append(flightByPlacementList);
		builder.append(", pSelectedDMA=");
		builder.append(Arrays.toString(pSelectedDMA));
		builder.append(", pSelectedPlatform=");
		builder.append(Arrays.toString(pSelectedPlatform));
		builder.append(", pSelectedCreative=");
		builder.append(Arrays.toString(pSelectedCreative));
		builder.append(", pSelectedState=");
		builder.append(Arrays.toString(pSelectedState));
		builder.append(", pSelectedCity=");
		builder.append(Arrays.toString(pSelectedCity));
		builder.append(", pSelectedDevice=");
		builder.append(Arrays.toString(pSelectedDevice));
		builder.append(", pSelectedContext=");
		builder.append(Arrays.toString(pSelectedContext));
		builder.append(", pSelectedCountry=");
		builder.append(Arrays.toString(pSelectedCountry));
		builder.append(", isDemographic=");
		builder.append(isDemographic);
		builder.append(", upperAge=");
		builder.append(upperAge);
		builder.append(", lowerAge=");
		builder.append(lowerAge);
		builder.append(", upperIncome=");
		builder.append(upperIncome);
		builder.append(", lowerIncome=");
		builder.append(lowerIncome);
		builder.append(", age=");
		builder.append(age);
		builder.append(", income=");
		builder.append(income);
		builder.append(", selectedEducation=");
		builder.append(Arrays.toString(selectedEducation));
		builder.append(", selectedEthnicity=");
		builder.append(Arrays.toString(selectedEthnicity));
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", selectedEducationList=");
		builder.append(selectedEducationList);
		builder.append(", selectedEthinicityList=");
		builder.append(selectedEthinicityList);
		builder.append(", educationList=");
		builder.append(educationList);
		builder.append(", ethinicityList=");
		builder.append(ethinicityList);
		builder.append(", selectedGender=");
		builder.append(selectedGender);
		builder.append(", flightList=");
		builder.append(flightList);
		builder.append(", advertisers=");
		builder.append(advertisers);
		builder.append(", agencies=");
		builder.append(agencies);
		
		builder.append(", censusAge=").append(selectedCensusAge);
		builder.append(", censusIncome=").append(selectedCensusIncome);
		builder.append(", censusEducation=").append(selectedCensusEducation);
		builder.append(", censusEthnicity=").append(selectedCensusEthnicity);
		builder.append(", censusGender=").append(selectedCensusGender);
		
		builder.append("]");
		return builder.toString();
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

	public String[] getSelectedEducation() {
		return selectedEducation;
	}
	public void setSelectedEducation(String[] selectedEducation) {
		this.selectedEducation = selectedEducation;
	}
	public String[] getSelectedEthnicity() {
		return selectedEthnicity;
	}
	public void setSelectedEthnicity(String[] selectedEthnicity) {
		this.selectedEthnicity = selectedEthnicity;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAgencyAddress() {
		return agencyAddress;
	}
	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}
	public String getAgencyPhone() {
		return agencyPhone;
	}
	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}
	public String getAgencyEmail() {
		return agencyEmail;
	}
	public void setAgencyEmail(String agencyEmail) {
		this.agencyEmail = agencyEmail;
	}
	public String getAgencyFax() {
		return agencyFax;
	}
	public void setAgencyFax(String agencyFax) {
		this.agencyFax = agencyFax;
	}
	public String getAgencyZip() {
		return agencyZip;
	}
	public void setAgencyZip(String agencyZip) {
		this.agencyZip = agencyZip;
	}
	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public String getAdvertiserAddress() {
		return advertiserAddress;
	}
	public void setAdvertiserAddress(String advertiserAddress) {
		this.advertiserAddress = advertiserAddress;
	}
	public String getAdvertiserPhone() {
		return advertiserPhone;
	}
	public void setAdvertiserPhone(String advertiserPhone) {
		this.advertiserPhone = advertiserPhone;
	}
	public String getAdvertiserEmail() {
		return advertiserEmail;
	}
	public void setAdvertiserEmail(String advertiserEmail) {
		this.advertiserEmail = advertiserEmail;
	}
	public String getAdvertiserFax() {
		return advertiserFax;
	}
	public void setAdvertiserFax(String advertiserFax) {
		this.advertiserFax = advertiserFax;
	}
	public String getAdvertiserZip() {
		return advertiserZip;
	}
	public void setAdvertiserZip(String advertiserZip) {
		this.advertiserZip = advertiserZip;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<CommonDTO> getPlateformList() {
		return plateformList;
	}
	public void setPlateformList(List<CommonDTO> plateformList) {
		this.plateformList = plateformList;
	}
	public List<CommonDTO> getDmaList() {
		return dmaList;
	}
	public void setDmaList(List<CommonDTO> dmaList) {
		this.dmaList = dmaList;
	}
	public List<CommonDTO> getCityList() {
		return cityList;
	}
	public void setCityList(List<CommonDTO> cityList) {
		this.cityList = cityList;
	}
	public List<CommonDTO> getStateList() {
		return stateList;
	}
	public void setStateList(List<CommonDTO> stateList) {
		this.stateList = stateList;
	}
	public List<CommonDTO> getZipList() {
		return zipList;
	}
	public void setZipList(List<CommonDTO> zipList) {
		this.zipList = zipList;
	}
	public List<CommonDTO> getCreativeList() {
		return creativeList;
	}
	public void setCreativeList(List<CommonDTO> creativeList) {
		this.creativeList = creativeList;
	}
	public List<CommonDTO> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<CommonDTO> deviceList) {
		this.deviceList = deviceList;
	}
	public List<CommonDTO> getContextList() {
		return contextList;
	}
	public void setContextList(List<CommonDTO> contextList) {
		this.contextList = contextList;
	}
	public List<CommonDTO> getRateTypeList() {
		return rateTypeList;
	}
	public void setRateTypeList(List<CommonDTO> rateTypeList) {
		this.rateTypeList = rateTypeList;
	}
	public String getSelectedDMA() {
		return selectedDMA;
	}
	public void setSelectedDMA(String selectedDMA) {
		this.selectedDMA = selectedDMA;
	}
	public String getFrequencyCap() {
		return frequencyCap;
	}
	public void setFrequencyCap(String frequencyCap) {
		this.frequencyCap = frequencyCap;
	}
	public String getTotalGoal() {
		return totalGoal;
	}
	public void setTotalGoal(String totalGoal) {
		this.totalGoal = totalGoal;
	}
	public String getSelectedRateType() {
		return selectedRateType;
	}
	public void setSelectedRateType(String selectedRateType) {
		this.selectedRateType = selectedRateType;
	}
	public String getSelectedPlatform() {
		return selectedPlatform;
	}
	public void setSelectedPlatform(String selectedPlatform) {
		this.selectedPlatform = selectedPlatform;
	}
	public String getSelectedCreative() {
		return selectedCreative;
	}
	public void setSelectedCreative(String selectedCreative) {
		this.selectedCreative = selectedCreative;
	}
	public String getSelectedState() {
		return selectedState;
	}
	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}
	public String getSelectedCity() {
		return selectedCity;
	}
	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}
	public String getSelectedZip() {
		return selectedZip;
	}
	public void setSelectedZip(String selectedZip) {
		this.selectedZip = selectedZip;
	}
	public String getSelectedBehavior() {
		return selectedBehavior;
	}
	public void setSelectedBehavior(String selectedBehavior) {
		this.selectedBehavior = selectedBehavior;
	}
	public String getSelectedDevice() {
		return selectedDevice;
	}
	public void setSelectedDevice(String selectedDevice) {
		this.selectedDevice = selectedDevice;
	}
	public String getSelectedDemographic() {
		return selectedDemographic;
	}
	public void setSelectedDemographic(String selectedDemographic) {
		this.selectedDemographic = selectedDemographic;
	}
	public String[] getFlightEnddate() {
		return flightEnddate;
	}
	public void setFlightEnddate(String flightEnddate[]) {
		this.flightEnddate = flightEnddate;
	}
	public String[] getFlightGoal() {
		return flightGoal;
	}
	public void setFlightGoal(String flightGoal[]) {
		this.flightGoal = flightGoal;
	}
	public String[] getFlightStartdate() {
		return flightStartdate;
	}
	public void setFlightStartdate(String flightStartdate[]) {
		this.flightStartdate = flightStartdate;
	}
	public String[] getPlacementImpression() {
		return placementImpression;
	}
	public void setPlacementImpression(String[] placementImpression) {
		this.placementImpression = placementImpression;
	}
	public String[] getPlacementStartDate() {
		return placementStartDate;
	}
	public void setPlacementStartDate(String[] placementStartDate) {
		this.placementStartDate = placementStartDate;
	}
	public String[] getPlacementEndtDate() {
		return placementEndtDate;
	}
	public void setPlacementEndtDate(String[] placementEndtDate) {
		this.placementEndtDate = placementEndtDate;
	}
	public List<CommonDTO> getSelectedDMAList() {
		return selectedDMAList;
	}
	public void setSelectedDMAList(List<CommonDTO> selectedDMAList) {
		this.selectedDMAList = selectedDMAList;
	}
	public List<CommonDTO> getSelectedRateTypeList() {
		return selectedRateTypeList;
	}
	public void setSelectedRateTypeList(List<CommonDTO> selectedRateTypeList) {
		this.selectedRateTypeList = selectedRateTypeList;
	}
	public List<CommonDTO> getSelectedPlatformList() {
		return selectedPlatformList;
	}
	public void setSelectedPlatformList(List<CommonDTO> selectedPlatformList) {
		this.selectedPlatformList = selectedPlatformList;
	}
	public List<CommonDTO> getSelectedCreativeList() {
		return selectedCreativeList;
	}
	public void setSelectedCreativeList(List<CommonDTO> selectedCreativeList) {
		this.selectedCreativeList = selectedCreativeList;
	}
	public List<CommonDTO> getSelectedStateList() {
		return selectedStateList;
	}
	public void setSelectedStateList(List<CommonDTO> selectedStateList) {
		this.selectedStateList = selectedStateList;
	}
	public List<CommonDTO> getSelectedCityList() {
		return selectedCityList;
	}
	public void setSelectedCityList(List<CommonDTO> selectedCityList) {
		this.selectedCityList = selectedCityList;
	}
	public List<CommonDTO> getSelectedZipList() {
		return selectedZipList;
	}
	public void setSelectedZipList(List<CommonDTO> selectedZipList) {
		this.selectedZipList = selectedZipList;
	}
	public List<CommonDTO> getSelectedBehaviorList() {
		return selectedBehaviorList;
	}
	public void setSelectedBehaviorList(List<CommonDTO> selectedBehaviorList) {
		this.selectedBehaviorList = selectedBehaviorList;
	}
	public List<CommonDTO> getSelectedDeviceList() {
		return selectedDeviceList;
	}
	public void setSelectedDeviceList(List<CommonDTO> selectedDeviceList) {
		this.selectedDeviceList = selectedDeviceList;
	}
	public List<CommonDTO> getSelectedDemographicList() {
		return selectedDemographicList;
	}
	public void setSelectedDemographicList(List<CommonDTO> selectedDemographicList) {
		this.selectedDemographicList = selectedDemographicList;
	}
	public List<SmartCampaignObj> getCampaignObjList() {
		return campaignObjList;
	}
	public void setCampaignObjList(List<SmartCampaignObj> campaignObjList) {
		this.campaignObjList = campaignObjList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String[] getSelectedFlightStartdate() {
		return selectedFlightStartdate;
	}
	public void setSelectedFlightStartdate(String[] selectedFlightStartdate) {
		this.selectedFlightStartdate = selectedFlightStartdate;
	}
	public String[] getSelectedFlightEnddate() {
		return selectedFlightEnddate;
	}
	public void setSelectedFlightEnddate(String[] selectedFlightEnddate) {
		this.selectedFlightEnddate = selectedFlightEnddate;
	}
	public String[] getSelectedFlightGoal() {
		return selectedFlightGoal;
	}
	public void setSelectedFlightGoal(String[] selectedFlightGoal) {
		this.selectedFlightGoal = selectedFlightGoal;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public List<CommonDTO> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<CommonDTO> countryList) {
		this.countryList = countryList;
	}
	public List<CommonDTO> getSelectedCountryList() {
		return selectedCountryList;
	}
	public void setSelectedCountryList(List<CommonDTO> selectedCountryList) {
		this.selectedCountryList = selectedCountryList;
	}
	public String getSelectedCountry() {
		return selectedCountry;
	}
	public void setSelectedCountry(String selectedCountry) {
		this.selectedCountry = selectedCountry;
	}
	public String getSelectedContext() {
		return selectedContext;
	}
	public void setSelectedContext(String selectedContext) {
		this.selectedContext = selectedContext;
	}
	public List<CommonDTO> getSelectedContextList() {
		return selectedContextList;
	}
	public void setSelectedContextList(List<CommonDTO> selectedContextList) {
		this.selectedContextList = selectedContextList;
	}
	
	public List<CommonDTO> getSelectedPlatformPlacementList() {
		return selectedPlatformPlacementList;
	}
	public void setSelectedPlatformPlacementList(
			List<CommonDTO> selectedPlatformPlacementList) {
		this.selectedPlatformPlacementList = selectedPlatformPlacementList;
	}
	public List<CommonDTO> getSelectedCreativePlacementList() {
		return selectedCreativePlacementList;
	}
	public void setSelectedCreativePlacementList(
			List<CommonDTO> selectedCreativePlacementList) {
		this.selectedCreativePlacementList = selectedCreativePlacementList;
	}
	public List<CommonDTO> getSelectedContextPlacementList() {
		return selectedContextPlacementList;
	}
	public void setSelectedContextPlacementList(
			List<CommonDTO> selectedContextPlacementList) {
		this.selectedContextPlacementList = selectedContextPlacementList;
	}
	public List<CommonDTO> getSelectedCreativePlateformList() {
		return selectedCreativePlateformList;
	}
	public void setSelectedCreativePlateformList(
			List<CommonDTO> selectedCreativePlateformList) {
		this.selectedCreativePlateformList = selectedCreativePlateformList;
	}
	public List<CommonDTO> getSelectedDevicePlacementList() {
		return selectedDevicePlacementList;
	}
	public void setSelectedDevicePlacementList(
			List<CommonDTO> selectedDevicePlacementList) {
		this.selectedDevicePlacementList = selectedDevicePlacementList;
	}

	public String getPBudget() {
		return pBudget;
	}
	public void setPBudget(String pBudget) {
		this.pBudget = pBudget;
	}
	public List<CommonDTO> getSelectedCountryPlatcementList() {
		return selectedCountryPlatcementList;
	}
	public void setSelectedCountryPlatcementList(
			List<CommonDTO> selectedCountryPlatcementList) {
		this.selectedCountryPlatcementList = selectedCountryPlatcementList;
	}
	public List<CommonDTO> getSelectedDMAPlacementList() {
		return selectedDMAPlacementList;
	}
	public void setSelectedDMAPlacementList(List<CommonDTO> selectedDMAPlacementList) {
		this.selectedDMAPlacementList = selectedDMAPlacementList;
	}
	public String[] getpSelectedDMA() {
		return pSelectedDMA;
	}
	public String[] getPSelectedDMA() {
		return pSelectedDMA;
	}
	public void setpSelectedDMA(String[] pSelectedDMA) {
		this.pSelectedDMA = pSelectedDMA;
	}
	public String[] getpSelectedPlatform() {
		return pSelectedPlatform;
	}
	public void setpSelectedPlatform(String[] pSelectedPlatform) {
		this.pSelectedPlatform = pSelectedPlatform;
	}
	public String[] getPSelectedCreative() {
		return pSelectedCreative;
	}
	public void setPSelectedCreative(String[] pSelectedCreative) {
		this.pSelectedCreative = pSelectedCreative;
	}
	public String[] getpSelectedState() {
		return pSelectedState;
	}
	public String[] getPSelectedState() {
		return pSelectedState;
	}
	public void setpSelectedState(String[] pSelectedState) {
		this.pSelectedState = pSelectedState;
	}
	public String[] getpSelectedCity() {
		return pSelectedCity;
	}
	public void setpSelectedCity(String[] pSelectedCity) {
		this.pSelectedCity = pSelectedCity;
	}
	public String[] getpSelectedDevice() {
		return pSelectedDevice;
	}
	public void setpSelectedDevice(String[] pSelectedDevice) {
		this.pSelectedDevice = pSelectedDevice;
	}
	public String[] getpSelectedContext() {
		return pSelectedContext;
	}
	public void setpSelectedContext(String[] pSelectedContext) {
		this.pSelectedContext = pSelectedContext;
	}
	public String[] getpSelectedCountry() {
		return pSelectedCountry;
	}
	public void setpSelectedCountry(String[] pSelectedCountry) {
		this.pSelectedCountry = pSelectedCountry;
	}
	
	public List<CommonDTO> getSelectedStatePlacementList() {
		return selectedStatePlacementList;
	}
	public void setSelectedStatePlacementList(
			List<CommonDTO> selectedStatePlacementList) {
		this.selectedStatePlacementList = selectedStatePlacementList;
	}
	public String[] getPlacementName() {
		return placementName;
	}
	public void setPlacementName(String[] placementName) {
		this.placementName = placementName;
	}
	public boolean isPGeoFencing() {
		return pGeoFencing;
	}
	public void setPGeoFencing(boolean pGeoFencing) {
		this.pGeoFencing = pGeoFencing;
	}
	public boolean isPDemoTarget() {
		return pDemoTarget;
	}
	public void setPDemoTarget(boolean pDemoTarget) {
		this.pDemoTarget = pDemoTarget;
	}
	public boolean isPBehaTarget() {
		return pBehaTarget;
	}
	public void setPBehaTarget(boolean pBehaTarget) {
		this.pBehaTarget = pBehaTarget;
	}
	public boolean isGeoFencing() {
		return geoFencing;
	}
	public void setGeoFencing(boolean geoFencing) {
		this.geoFencing = geoFencing;
	}
	public boolean isDemoTarget() {
		return demoTarget;
	}
	public void setDemoTarget(boolean demoTarget) {
		this.demoTarget = demoTarget;
	}
	public boolean isBehaTarget() {
		return behaTarget;
	}
	public void setBehaTarget(boolean behaTarget) {
		this.behaTarget = behaTarget;
	}
	public int getPCount() {
		return pCount;
	}
	public void setPCount(int pCount) {
		this.pCount = pCount;
	}
	public String getSelectedAdvertiser() {
		return selectedAdvertiser;
	}
	public void setSelectedAdvertiser(String selectedAdvertiser) {
		this.selectedAdvertiser = selectedAdvertiser;
	}
	public String getSelectedAgency() {
		return selectedAgency;
	}
	public void setSelectedAgency(String selectedAgency) {
		this.selectedAgency = selectedAgency;
	}
	public String getPGoal() {
		return pGoal;
	}
	public void setPGoal(String pGoal) {
		this.pGoal = pGoal;
	}
	public String getPImpression() {
		return pImpression;
	}
	public void setPImpression(String pImpression) {
		this.pImpression = pImpression;
	}
	public String getPacing() {
		return pacing;
	}
	public void setPacing(String pacing) {
		this.pacing = pacing;
	}
	public String getFrequencyCapUnit() {
		return frequencyCapUnit;
	}
	public void setFrequencyCapUnit(String frequencyCapUnit) {
		this.frequencyCapUnit = frequencyCapUnit;
	}
	public List<CommonDTO> getAdvertiserList() {
		return advertiserList;
	}
	public void setAdvertiserList(List<CommonDTO> advertiserList) {
		this.advertiserList = advertiserList;
	}
	public List<CommonDTO> getAgencyList() {
		return agencyList;
	}
	public void setAgencyList(List<CommonDTO> agencyList) {
		this.agencyList = agencyList;
	}
	public List<CommonDTO> getSelectedAdvertiserList() {
		return selectedAdvertiserList;
	}
	public void setSelectedAdvertiserList(List<CommonDTO> selectedAdvertiserList) {
		this.selectedAdvertiserList = selectedAdvertiserList;
	}
	public List<CommonDTO> getSelectedAgencyList() {
		return selectedAgencyList;
	}
	public void setSelectedAgencyList(List<CommonDTO> selectedAgencyList) {
		this.selectedAgencyList = selectedAgencyList;
	}
	public List<SmartCampaignPlacementObj> getPlacementByCampaignList() {
		return placementByCampaignList;
	}
	public void setPlacementByCampaignList(List<SmartCampaignPlacementObj> placementByCampaignList) {
		this.placementByCampaignList = placementByCampaignList;
	}

	public List<AdvertiserObj> getAdvertisers() {
		return advertisers;
	}
	public void setAdvertisers(List<AdvertiserObj> advertisers) {
		this.advertisers = advertisers;
	}
	public List<AgencyObj> getAgencies() {
		return agencies;
	}
	public void setAgencies(List<AgencyObj> agencies) {
		this.agencies = agencies;
	}
		

	public List<CommonDTO> getSelectedEducationList() {
		return selectedEducationList;
	}
	public void setSelectedEducationList(List<CommonDTO> selectedEducationList) {
		this.selectedEducationList = selectedEducationList;
	}
	public List<CommonDTO> getSelectedEthinicityList() {
		return selectedEthinicityList;
	}
	public void setSelectedEthinicityList(List<CommonDTO> selectedEthinicityList) {
		this.selectedEthinicityList = selectedEthinicityList;
	}
	public List<CommonDTO> getEthinicityList() {
		return ethinicityList;
	}
	public void setEthinicityList(List<CommonDTO> ethinicityList) {
		this.ethinicityList = ethinicityList;
	}
	public List<CommonDTO> getEducationList() {
		return educationList;
	}
	public void setEducationList(List<CommonDTO> educationList) {
		this.educationList = educationList;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public List<CommonDTO> getSelectedGender() {
		return selectedGender;
	}
	public void setSelectedGender(List<CommonDTO> selectedGender) {
		this.selectedGender = selectedGender;
	}
	public List<SmartCampaignFlightObj> getFlightByPlacementList() {
		return flightByPlacementList;
	}
	public void setFlightByPlacementList(List<SmartCampaignFlightObj> flightByPlacementList) {
		this.flightByPlacementList = flightByPlacementList;
	}
	public List<CommonDTO> getSelectedFrequencyUnitList() {
		return selectedFrequencyUnitList;
	}
	public void setSelectedFrequencyUnitList(
			List<CommonDTO> selectedFrequencyUnitList) {
		this.selectedFrequencyUnitList = selectedFrequencyUnitList;
	}
	public long getPlacementId() {
		return placementId;
	}
	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}
	public String getPlacementStatus() {
		return placementStatus;
	}
	public void setPlacementStatus(String placementStatus) {
		this.placementStatus = placementStatus;
	}
	public String getCityJSON() {
		return cityJSON;
	}
	public void setCityJSON(String cityJSON) {
		this.cityJSON = cityJSON;
	}
	public String getZipJSON() {
		return zipJSON;
	}
	public void setZipJSON(String zipJSON) {
		this.zipJSON = zipJSON;
	}
	public List<SmartCampaignFlightDTO> getFlightList() {
		return flightList;
	}
	public void setFlightList(List<SmartCampaignFlightDTO> flightList) {
		this.flightList = flightList;
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
	public boolean getIsPlacementSave() {
		return isPlacementSave;
	}
	public void setIsPlacementSave(boolean isPlacementSave) {
		this.isPlacementSave = isPlacementSave;
	}
	public int getMediaPlanEditStatus() {
		return mediaPlanEditStatus;
	}
	public void setMediaPlanEditStatus(int mediaPlanEditStatus) {
		this.mediaPlanEditStatus = mediaPlanEditStatus;
	}
	public int getMediaPlanStatus() {
		return mediaPlanStatus;
	}
	public void setMediaPlanStatus(int mediaPlanStatus) {
		this.mediaPlanStatus = mediaPlanStatus;
	}
	public List<CommonDTO> getFrequencyCapList() {
		return frequencyCapList;
	}
	public void setFrequencyCapList(List<CommonDTO> frequencyCapList) {
		this.frequencyCapList = frequencyCapList;
	}
	public List<CommonDTO> getGenderList() {
		return genderList;
	}
	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}
	public long getDfpOrderId() {
		return dfpOrderId;
	}
	public void setDfpOrderId(long dfpOrderId) {
		this.dfpOrderId = dfpOrderId;
	}
	public String getDfpOrderName() {
		return dfpOrderName;
	}
	public void setDfpOrderName(String dfpOrderName) {
		this.dfpOrderName = dfpOrderName;
	}
	public int getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public boolean isHasMediaPlan() {
		return hasMediaPlan;
	}
	public void setHasMediaPlan(boolean hasMediaPlan) {
		this.hasMediaPlan = hasMediaPlan;
	}
	
	public boolean isHasMigrated() {
		return hasMigrated;
	}

	public void setHasMigrated(boolean hasMigrated) {
		this.hasMigrated = hasMigrated;
	}


	public boolean isHasProcessing() {
		return hasProcessing;
	}


	public void setHasProcessing(boolean hasProcessing) {
		this.hasProcessing = hasProcessing;
	}


	public boolean isHasSetupOnDFP() {
		return hasSetupOnDFP;
	}


	public void setHasSetupOnDFP(boolean hasSetupOnDFP) {
		this.hasSetupOnDFP = hasSetupOnDFP;
	}




	public String getPName() {
		return pName;
	}




	public void setPName(String pName) {
		this.pName = pName;
	}




	public String getPStartDate() {
		return pStartDate;
	}




	public void setPStartDate(String pStartDate) {
		this.pStartDate = pStartDate;
	}




	public String getPEndDate() {
		return pEndDate;
	}




	public void setPEndDate(String pEndDate) {
		this.pEndDate = pEndDate;
	}




	public String getItemType() {
		if(itemType == null){
			itemType = STANDARD_ITEM_TYPE;
		}
		return itemType;
	}




	public void setItemType(String itemType) {
		this.itemType = itemType;
	}




	public Integer getDeviceCapability() {
		return deviceCapability;
	}




	public void setDeviceCapability(Integer deviceCapability) {
		this.deviceCapability = deviceCapability;
	}




	public String getSelectedPlacementProducts() {
		return selectedPlacementProducts;
	}




	public void setSelectedPlacementProducts(String selectedPlacementProducts) {
		this.selectedPlacementProducts = selectedPlacementProducts;
	}




	public String getSelectedCensusGender() {
		return selectedCensusGender;
	}




	public void setSelectedCensusGender(String selectedCensusGender) {
		this.selectedCensusGender = selectedCensusGender;
	}




	public String getSelectedCensusAge() {
		return selectedCensusAge;
	}




	public void setSelectedCensusAge(String selectedCensusAge) {
		this.selectedCensusAge = selectedCensusAge;
	}




	public String getSelectedCensusIncome() {
		return selectedCensusIncome;
	}




	public void setSelectedCensusIncome(String selectedCensusIncome) {
		this.selectedCensusIncome = selectedCensusIncome;
	}




	public String getSelectedCensusEducation() {
		return selectedCensusEducation;
	}




	public void setSelectedCensusEducation(String selectedCensusEducation) {
		this.selectedCensusEducation = selectedCensusEducation;
	}




	public String getSelectedCensusEthnicity() {
		return selectedCensusEthnicity;
	}




	public void setSelectedCensusEthnicity(String selectedCensusEthnicity) {
		this.selectedCensusEthnicity = selectedCensusEthnicity;
	}




	public String getSelectedCensusRank() {
		return selectedCensusRank;
	}




	public void setSelectedCensusRank(String selectedCensusRank) {
		this.selectedCensusRank = selectedCensusRank;
	}

	

	
}
