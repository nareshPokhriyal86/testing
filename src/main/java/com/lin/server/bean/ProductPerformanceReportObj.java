package com.lin.server.bean;

public class ProductPerformanceReportObj {
	
	private String loadTimestamp;	
	private String adUnit1;
	private long adUnitId1;
	private String adUnit2;
	private long adUnitId2;
	private String adUnit3;
	private long adUnitId3;
	private String adUnit4;
	private long adUnitId4;
	private String adUnit5;
	private long adUnitId5;
	private String countryName;
	private String regionName;
	private String cityName;	
	private long countryId;
	private long regionId;
	private long cityId;
	private long impressions;
	private long clicks;
	private String networkCode;
	
	public ProductPerformanceReportObj(){
		
	}	


	public ProductPerformanceReportObj(String loadTimestamp, String adUnit1,
			long adUnitId1, String adUnit2, long adUnitId2, String adUnit3,
			long adUnitId3, String adUnit4, long adUnitId4, String adUnit5,
			long adUnitId5, String countryName, String regionName,
			String cityName, long countryId, long regionId, long cityId,
			long impressions, long clicks, String networkCode) {
		this.loadTimestamp = loadTimestamp;
		this.adUnit1 = adUnit1;
		this.adUnitId1 = adUnitId1;
		this.adUnit2 = adUnit2;
		this.adUnitId2 = adUnitId2;
		this.adUnit3 = adUnit3;
		this.adUnitId3 = adUnitId3;
		this.adUnit4 = adUnit4;
		this.adUnitId4 = adUnitId4;
		this.adUnit5 = adUnit5;
		this.adUnitId5 = adUnitId5;
		this.countryName = countryName;
		this.regionName = regionName;
		this.cityName = cityName;
		this.countryId = countryId;
		this.regionId = regionId;
		this.cityId = cityId;
		this.impressions = impressions;
		this.clicks = clicks;
		this.networkCode = networkCode;
	}



	public String getAdUnit1() {
		return adUnit1;
	}



	public void setAdUnit1(String adUnit1) {
		this.adUnit1 = adUnit1;
	}



	public long getAdUnitId1() {
		return adUnitId1;
	}



	public void setAdUnitId1(long adUnitId1) {
		this.adUnitId1 = adUnitId1;
	}



	public String getAdUnit2() {
		return adUnit2;
	}



	public void setAdUnit2(String adUnit2) {
		this.adUnit2 = adUnit2;
	}



	public long getAdUnitId2() {
		return adUnitId2;
	}



	public void setAdUnitId2(long adUnitId2) {
		this.adUnitId2 = adUnitId2;
	}



	public String getAdUnit3() {
		return adUnit3;
	}



	public void setAdUnit3(String adUnit3) {
		this.adUnit3 = adUnit3;
	}



	public long getAdUnitId3() {
		return adUnitId3;
	}



	public void setAdUnitId3(long adUnitId3) {
		this.adUnitId3 = adUnitId3;
	}



	public String getAdUnit4() {
		return adUnit4;
	}



	public void setAdUnit4(String adUnit4) {
		this.adUnit4 = adUnit4;
	}



	public long getAdUnitId4() {
		return adUnitId4;
	}



	public void setAdUnitId4(long adUnitId4) {
		this.adUnitId4 = adUnitId4;
	}



	public String getAdUnit5() {
		return adUnit5;
	}



	public void setAdUnit5(String adUnit5) {
		this.adUnit5 = adUnit5;
	}



	public long getAdUnitId5() {
		return adUnitId5;
	}



	public void setAdUnitId5(long adUnitId5) {
		this.adUnitId5 = adUnitId5;
	}



	public String getLoadTimestamp() {
		return loadTimestamp;
	}

	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}

	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getImpressions() {
		return impressions;
	}

	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}

	public String getNetworkCode() {
		return networkCode;
	}

	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}
	
	
}
