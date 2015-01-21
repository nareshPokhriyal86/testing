package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.DFPAdvertisersObj;
import com.lin.server.bean.DFPAgencyObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.OrdersObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.util.LinMobileConstants;

public class UserDetailsDTO implements Serializable {

	private long id;
	private String userName;
	private String emailId;
	private String emailIdRepeat;
	private String password;
	private String passwordRepeat;
	private String timezone;
	private String role;
	private String status;
	private String team;
	private String note;
	private boolean optEmail; 
	private String returnStatus;

	private String CompanyIDOfSessionAdminUser;
	private String defaultSelectedCompanyType;

	private String hiddenVal;
	private String randomNumber;

	private List<CommonDTO> roleList;
	private List<CommonDTO> statusList;
	private List<CommonDTO> teamList;

	private List<CommonDTO> selectedRoleList;
	private List<CommonDTO> selectedStatusList;
	private List<CommonDTO> selectedTeamList;

	private List<PublisherSetupDTO> publisherSetupDTOList;
	private List<CommonDTO> publishersList;
	private String publishers;
	private List<CommonDTO> selectedPublishersList;

	private List<CommonDTO> demandPartnersList;
	private String demandPartners;
	private List<CommonDTO> selectedDemandPartnersList;

	private List<CommonDTO> propertiesList;
	private String properties;
	private List<CommonDTO> selectedPropertiesList;

	private List<CommonDTO> appViewsList;
	private String appViews;
	private List<CommonDTO> selectedAppViewsList;

	private List<CommonDTO> adminTeamList;
	private List<CommonDTO> nonAdminTeamList;
	private String nonAdminTeam;
	private String adminTeam;
	private List<CommonDTO> selectedAdminTeamList;
	private List<CommonDTO> selectedNonAdminTeamList;

	private String selectedCompanyType;
	private String publisherIdsForBigQuery;

	private String saveDefaultSettings;
	private String defaultSettingsSaveStatus;
	private String adServer;
	private String accounts;
	private String orders;
	private String agencyIdStringForBigQuery;
	private String advertiserIdStringForBigQuery;
	private List<CommonDTO> adServerList;
	private List<CommonDTO> selectedAdServerList;
	private List<OrdersObj> ordersObjList;
	private List<DFPAdvertisersObj> dfpAdvertisersObjList;
	private List<DFPAgencyObj> dfpAgencyObjList;
	private List<OrdersObj> selectedOrdersObjList;
	private List<DFPAdvertisersObj> selectedDfpAdvertisersObjList;
	private List<DFPAgencyObj> selectedDfpAgencyObjList;

	private List<UserDetailsObj> usersList;

	private String teamId;
	private String teamName;
	private String teamDescription;
	private String teamStatus;
	private String teamType;
	private List<TeamPropertiesObj> TeamPropertiesObjList;

	private String accountsFlag;
	private String allPropertiesFlag;

	private String companyTypeToCreate;
	private String companyTypeToUpdate;
	private String formerCompanyType;

	private List<CompanyObj> companyList;
	private List<CommonDTO> selectedCompanyList;
	private String companyLogo;
	private String companyLogoContentType;
	private String companyLogoFileName;
	private String companyName;
	private String companyType;
	private String companyId;
	private String webURL;
	private String companyEmail;
	private String fax;
	private String contactPersonName;
	private String companyAddress;
	private String[] adServerId;
	private String[] adServerUsername;
	private String[] adServerPassword;
	private List<AdServerCredentialsDTO> adServerCredentialsDTOList;
	private String adServerCredentialsCounterValue;
	private String accessToAccounts;
	private String accessToProperties;
	private String companyLogoURL;

	private List<CompanyObj> companySetupList;
	private List<CommonDTO> companyTypeList;
	private List<CommonDTO> selectedCompanyTypeList;

	private String publisherId;
	private String publisherName;
	private String demandPartnerId;
	private String numberOfStations;
	private List<CommonDTO> adServerForPublisherList;
	private String adServerForPublisher;
	private List<CommonDTO> selectedAdServerForPublisherList;

	private String demandPartnerName;
	private String dataSource;
	private String demandPartnerCategory;
	private String demandPartnerType;
	private List<CommonDTO> demandPartnerTypeList;
	private List<CommonDTO> selectedDemandPartnerTypeList;
	private String[] passbackSiteType;
	private List<CommonDTO> passbackSiteTypeList;
	private String passbackSiteTypeCounterValue;
	private String[] serviceURL;
	private List<CommonDTO> serviceURLList;
	private String serviceURLCounterValue;
	private String adServerInfo;

	private List<PropertyObj> propertySetupDTOList;
	private String propertyId;
	private String propertyName;
	private String market;
	private String DMARank;
	private String DFPPropertyName;
	private String DFPPropertyId;
	private String affiliation;
	private String webSite;
	private String mobileWebURL;
	private String tabletWebURL;
	private String generalManager;
	private String address;
	private String zipCode;
	private String state;
	private String phone;
	private List<String> childs;

	private List<AccountsEntity> accountsObjList;
	private List<IndustryObj> industryList;
	private String dfpAccountName;
	private String accountName;
	private String industry;
	private String accountType;
	private String accountDfpId;
	private String contact;
	private String accountId;
	private String networkId;
	private String networkUsername;
	private String networkPassword;
	private boolean networkAvailabile;
	private List<CommonDTO> accountsList;
	private List<CommonDTO> selectedAccountsList;

	private List<AuthorisationTextObj> authorisationTextList;
	private List<String> authorisationKeywordList;
	private List<String> authorisedPagesList;
	private List<RolesAndAuthorisationDTO> rolesAndAuthorisationDTOList;
	private String roleDescription;
	private String roleName;
	private String roleId;
	private String roleType;
	private String roleStatus;
	private String A1;
	private String A2;
	private String A3;
	private String A4;
	private String A5;
	private String A6;
	private String A7;
	private String A8;
	private String A9;
	private String A10;
	private String A11;
	private String A12;
	private String A13;
	private String A14;
	private String A15;
	private String A16;
	private String A17;
	private String A18;
	private String A19;
	private String A20;
	private String A21;
	private String A22;
	private String A23;
	private String A24;
	private String A25;
	private String A26;
	private String A27;
	private String A28;
	private String A29;
	private String A30;
	private String A31;
	private String A32;
	private String A33;
	private String A34;
	private String A35;
	private String A36;
	private String A37;
	private String A38;
	private String A39;
	private String A40;
	private String A41;
	private String A42;
	private String A43;
	private String A44;
	private String A45;
	private String A46;
	private String A47;
	private String A48;
	private String A49;
	private String A50;
	private String A51;
	private String A52;
	private String A53;
	private String A54;
	private String A55;
	private String A56;
	private String A57;
	private String A58;
	private String A59;
	private String A60;
	private String A61;
	private String A62;
	private String A63;
	private String A64;
	private String A65;
	private String A66;
	private String A67;
	private String A68;
	private String A69;
	private String A70;
	private String A71;
	private String A72;
	private String A73;
	private String A74;
	private String A75;
	private String A76;
	private String A77;
	private String A78;
	private String A79;
	private String A80;
	private String A81;
	private String A82;
	private String A83;
	private String A84;
	private String A85;
	private String A86;
	private String A87;
	private String A88;
	private String A89;
	private String A90;
	private String A91;
	private String A92;
	private String A93;
	private String A94;
	private String A95;
	private String A96;
	private String A97;
	private String A98;
	private String A99;
	private String A100;

	/*
	 * public String companyTypePublisher = LinMobileConstants.COMPANY_TYPES[0];
	 * public String companyTypeAdvertiser =
	 * LinMobileConstants.COMPANY_TYPES[1]; public String companyTypeAgency =
	 * LinMobileConstants.COMPANY_TYPES[2];
	 */

	public String companyTypePublisherPartner = LinMobileConstants.COMPANY_TYPE[0];
	public String companyTypeDemandPartner = LinMobileConstants.COMPANY_TYPE[1];
	public String companyTypeClient = LinMobileConstants.COMPANY_TYPE[2];

	public String definedTypeBuiltIn = LinMobileConstants.DEFINED_TYPES[0];

	public String administratorRole = LinMobileConstants.ADMINS[1];
	public String superAdminRole = LinMobileConstants.ADMINS[0];

	public String allOrders = LinMobileConstants.ALL_ORDERS;
	public String allAdvertisers = LinMobileConstants.ALL_ADVERTISERS;
	public String allAgencies = LinMobileConstants.ALL_AGENCIES;

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

	public String teamAllEntity = LinMobileConstants.TEAM_ALL_ENTITIE;
	public String teamNoEntity = LinMobileConstants.TEAM_NO_ENTITIE;

	public String dfpDataSource = LinMobileConstants.DFP_DATA_SOURCE;
	// public String allAppView = LinMobileConstants.ALL_APP_VIEWS;
	public String allProperties = LinMobileConstants.ALL_PROPERTIES;
	public String activeStatus = LinMobileConstants.STATUS_ARRAY[0];

	private String selectedRoleType;

	public UserDetailsDTO() {

	}

	public UserDetailsDTO(long id, String emailId, String password,
			String userName, String role, String status, String team) {
		this.id = id;
		this.emailId = emailId;
		this.password = password;
		this.userName = userName;
		this.role = role;
		this.status = status;
		this.team = team;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setRoleList(List<CommonDTO> roleList) {
		this.roleList = roleList;
	}

	public List<CommonDTO> getRoleList() {
		return roleList;
	}

	public void setStatusList(List<CommonDTO> statusList) {
		this.statusList = statusList;
	}

	public List<CommonDTO> getStatusList() {
		return statusList;
	}

	public void setTeamList(List<CommonDTO> teamList) {
		this.teamList = teamList;
	}

	public List<CommonDTO> getTeamList() {
		return teamList;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getTeam() {
		return team;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setEmailIdRepeat(String emailIdRepeat) {
		this.emailIdRepeat = emailIdRepeat;
	}

	public String getEmailIdRepeat() {
		return emailIdRepeat;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setUsersList(List<UserDetailsObj> usersList) {
		this.usersList = usersList;
	}

	public List<UserDetailsObj> getUsersList() {
		return usersList;
	}

	public void setHiddenVal(String hiddenVal) {
		this.hiddenVal = hiddenVal;
	}

	public String getHiddenVal() {
		return hiddenVal;
	}

	public void setSelectedRoleList(List<CommonDTO> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	public List<CommonDTO> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedStatusList(List<CommonDTO> selectedStatusList) {
		this.selectedStatusList = selectedStatusList;
	}

	public List<CommonDTO> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedTeamList(List<CommonDTO> selectedTeamList) {
		this.selectedTeamList = selectedTeamList;
	}

	public List<CommonDTO> getSelectedTeamList() {
		return selectedTeamList;
	}

	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}

	public String getRandomNumber() {
		return randomNumber;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setAuthorisationTextList(
			List<AuthorisationTextObj> authorisationTextList) {
		this.authorisationTextList = authorisationTextList;
	}

	public List<AuthorisationTextObj> getAuthorisationTextList() {
		return authorisationTextList;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getA1() {
		return A1;
	}

	public void setA1(String a1) {
		A1 = a1;
	}

	public String getA2() {
		return A2;
	}

	public void setA2(String a2) {
		A2 = a2;
	}

	public String getA3() {
		return A3;
	}

	public void setA3(String a3) {
		A3 = a3;
	}

	public String getA4() {
		return A4;
	}

	public void setA4(String a4) {
		A4 = a4;
	}

	public String getA5() {
		return A5;
	}

	public void setA5(String a5) {
		A5 = a5;
	}

	public String getA6() {
		return A6;
	}

	public void setA6(String a6) {
		A6 = a6;
	}

	public String getA7() {
		return A7;
	}

	public void setA7(String a7) {
		A7 = a7;
	}

	public String getA8() {
		return A8;
	}

	public void setA8(String a8) {
		A8 = a8;
	}

	public String getA9() {
		return A9;
	}

	public void setA9(String a9) {
		A9 = a9;
	}

	public String getA10() {
		return A10;
	}

	public void setA10(String a10) {
		A10 = a10;
	}

	public String getA11() {
		return A11;
	}

	public void setA11(String a11) {
		A11 = a11;
	}

	public String getA12() {
		return A12;
	}

	public void setA12(String a12) {
		A12 = a12;
	}

	public String getA13() {
		return A13;
	}

	public void setA13(String a13) {
		A13 = a13;
	}

	public String getA14() {
		return A14;
	}

	public void setA14(String a14) {
		A14 = a14;
	}

	public String getA15() {
		return A15;
	}

	public void setA15(String a15) {
		A15 = a15;
	}

	public String getA16() {
		return A16;
	}

	public void setA16(String a16) {
		A16 = a16;
	}

	public String getA17() {
		return A17;
	}

	public void setA17(String a17) {
		A17 = a17;
	}

	public String getA18() {
		return A18;
	}

	public void setA18(String a18) {
		A18 = a18;
	}

	public String getA19() {
		return A19;
	}

	public void setA19(String a19) {
		A19 = a19;
	}

	public String getA20() {
		return A20;
	}

	public void setA20(String a20) {
		A20 = a20;
	}

	public String getA21() {
		return A21;
	}

	public void setA21(String a21) {
		A21 = a21;
	}

	public String getA22() {
		return A22;
	}

	public void setA22(String a22) {
		A22 = a22;
	}

	public String getA23() {
		return A23;
	}

	public void setA23(String a23) {
		A23 = a23;
	}

	public String getA24() {
		return A24;
	}

	public void setA24(String a24) {
		A24 = a24;
	}

	public String getA25() {
		return A25;
	}

	public void setA25(String a25) {
		A25 = a25;
	}

	public String getA26() {
		return A26;
	}

	public void setA26(String a26) {
		A26 = a26;
	}

	public String getA27() {
		return A27;
	}

	public void setA27(String a27) {
		A27 = a27;
	}

	public String getA28() {
		return A28;
	}

	public void setA28(String a28) {
		A28 = a28;
	}

	public String getA29() {
		return A29;
	}

	public void setA29(String a29) {
		A29 = a29;
	}

	public String getA30() {
		return A30;
	}

	public void setA30(String a30) {
		A30 = a30;
	}

	public String getA31() {
		return A31;
	}

	public void setA31(String a31) {
		A31 = a31;
	}

	public String getA32() {
		return A32;
	}

	public void setA32(String a32) {
		A32 = a32;
	}

	public String getA33() {
		return A33;
	}

	public void setA33(String a33) {
		A33 = a33;
	}

	public String getA34() {
		return A34;
	}

	public void setA34(String a34) {
		A34 = a34;
	}

	public String getA35() {
		return A35;
	}

	public void setA35(String a35) {
		A35 = a35;
	}

	public String getA36() {
		return A36;
	}

	public void setA36(String a36) {
		A36 = a36;
	}

	public String getA37() {
		return A37;
	}

	public void setA37(String a37) {
		A37 = a37;
	}

	public String getA38() {
		return A38;
	}

	public void setA38(String a38) {
		A38 = a38;
	}

	public String getA39() {
		return A39;
	}

	public void setA39(String a39) {
		A39 = a39;
	}

	public String getA40() {
		return A40;
	}

	public void setA40(String a40) {
		A40 = a40;
	}

	public String getA41() {
		return A41;
	}

	public void setA41(String a41) {
		A41 = a41;
	}

	public String getA42() {
		return A42;
	}

	public void setA42(String a42) {
		A42 = a42;
	}

	public String getA43() {
		return A43;
	}

	public void setA43(String a43) {
		A43 = a43;
	}

	public String getA44() {
		return A44;
	}

	public void setA44(String a44) {
		A44 = a44;
	}

	public String getA45() {
		return A45;
	}

	public void setA45(String a45) {
		A45 = a45;
	}

	public String getA46() {
		return A46;
	}

	public void setA46(String a46) {
		A46 = a46;
	}

	public String getA47() {
		return A47;
	}

	public void setA47(String a47) {
		A47 = a47;
	}

	public String getA48() {
		return A48;
	}

	public void setA48(String a48) {
		A48 = a48;
	}

	public String getA49() {
		return A49;
	}

	public void setA49(String a49) {
		A49 = a49;
	}

	public String getA50() {
		return A50;
	}

	public void setA50(String a50) {
		A50 = a50;
	}

	public String getA51() {
		return A51;
	}

	public void setA51(String a51) {
		A51 = a51;
	}

	public String getA52() {
		return A52;
	}

	public void setA52(String a52) {
		A52 = a52;
	}

	public String getA53() {
		return A53;
	}

	public void setA53(String a53) {
		A53 = a53;
	}

	public String getA54() {
		return A54;
	}

	public void setA54(String a54) {
		A54 = a54;
	}

	public String getA55() {
		return A55;
	}

	public void setA55(String a55) {
		A55 = a55;
	}

	public String getA56() {
		return A56;
	}

	public void setA56(String a56) {
		A56 = a56;
	}

	public String getA57() {
		return A57;
	}

	public void setA57(String a57) {
		A57 = a57;
	}

	public String getA58() {
		return A58;
	}

	public void setA58(String a58) {
		A58 = a58;
	}

	public String getA59() {
		return A59;
	}

	public void setA59(String a59) {
		A59 = a59;
	}

	public String getA60() {
		return A60;
	}

	public void setA60(String a60) {
		A60 = a60;
	}

	public String getA61() {
		return A61;
	}

	public void setA61(String a61) {
		A61 = a61;
	}

	public String getA62() {
		return A62;
	}

	public void setA62(String a62) {
		A62 = a62;
	}

	public String getA63() {
		return A63;
	}

	public void setA63(String a63) {
		A63 = a63;
	}

	public String getA64() {
		return A64;
	}

	public void setA64(String a64) {
		A64 = a64;
	}

	public String getA65() {
		return A65;
	}

	public void setA65(String a65) {
		A65 = a65;
	}

	public String getA66() {
		return A66;
	}

	public void setA66(String a66) {
		A66 = a66;
	}

	public String getA67() {
		return A67;
	}

	public void setA67(String a67) {
		A67 = a67;
	}

	public String getA68() {
		return A68;
	}

	public void setA68(String a68) {
		A68 = a68;
	}

	public String getA69() {
		return A69;
	}

	public void setA69(String a69) {
		A69 = a69;
	}

	public String getA70() {
		return A70;
	}

	public void setA70(String a70) {
		A70 = a70;
	}

	public String getA71() {
		return A71;
	}

	public void setA71(String a71) {
		A71 = a71;
	}

	public String getA72() {
		return A72;
	}

	public void setA72(String a72) {
		A72 = a72;
	}

	public String getA73() {
		return A73;
	}

	public void setA73(String a73) {
		A73 = a73;
	}

	public String getA74() {
		return A74;
	}

	public void setA74(String a74) {
		A74 = a74;
	}

	public String getA75() {
		return A75;
	}

	public void setA75(String a75) {
		A75 = a75;
	}

	public String getA76() {
		return A76;
	}

	public void setA76(String a76) {
		A76 = a76;
	}

	public String getA77() {
		return A77;
	}

	public void setA77(String a77) {
		A77 = a77;
	}

	public String getA78() {
		return A78;
	}

	public void setA78(String a78) {
		A78 = a78;
	}

	public String getA79() {
		return A79;
	}

	public void setA79(String a79) {
		A79 = a79;
	}

	public String getA80() {
		return A80;
	}

	public void setA80(String a80) {
		A80 = a80;
	}

	public String getA81() {
		return A81;
	}

	public void setA81(String a81) {
		A81 = a81;
	}

	public String getA82() {
		return A82;
	}

	public void setA82(String a82) {
		A82 = a82;
	}

	public String getA83() {
		return A83;
	}

	public void setA83(String a83) {
		A83 = a83;
	}

	public String getA84() {
		return A84;
	}

	public void setA84(String a84) {
		A84 = a84;
	}

	public String getA85() {
		return A85;
	}

	public void setA85(String a85) {
		A85 = a85;
	}

	public String getA86() {
		return A86;
	}

	public void setA86(String a86) {
		A86 = a86;
	}

	public String getA87() {
		return A87;
	}

	public void setA87(String a87) {
		A87 = a87;
	}

	public String getA88() {
		return A88;
	}

	public void setA88(String a88) {
		A88 = a88;
	}

	public String getA89() {
		return A89;
	}

	public void setA89(String a89) {
		A89 = a89;
	}

	public String getA90() {
		return A90;
	}

	public void setA90(String a90) {
		A90 = a90;
	}

	public String getA91() {
		return A91;
	}

	public void setA91(String a91) {
		A91 = a91;
	}

	public String getA92() {
		return A92;
	}

	public void setA92(String a92) {
		A92 = a92;
	}

	public String getA93() {
		return A93;
	}

	public void setA93(String a93) {
		A93 = a93;
	}

	public String getA94() {
		return A94;
	}

	public void setA94(String a94) {
		A94 = a94;
	}

	public String getA95() {
		return A95;
	}

	public void setA95(String a95) {
		A95 = a95;
	}

	public String getA96() {
		return A96;
	}

	public void setA96(String a96) {
		A96 = a96;
	}

	public String getA97() {
		return A97;
	}

	public void setA97(String a97) {
		A97 = a97;
	}

	public String getA98() {
		return A98;
	}

	public void setA98(String a98) {
		A98 = a98;
	}

	public String getA99() {
		return A99;
	}

	public void setA99(String a99) {
		A99 = a99;
	}

	public String getA100() {
		return A100;
	}

	public void setA100(String a100) {
		A100 = a100;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setCompanyList(List<CompanyObj> companyList) {
		this.companyList = companyList;
	}

	public List<CompanyObj> getCompanyList() {
		return companyList;
	}

	public void setSelectedCompanyList(List<CommonDTO> selectedCompanyList) {
		this.selectedCompanyList = selectedCompanyList;
	}

	public List<CommonDTO> getSelectedCompanyList() {
		return selectedCompanyList;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanySetupList(List<CompanyObj> companySetupList) {
		this.companySetupList = companySetupList;
	}

	public List<CompanyObj> getCompanySetupList() {
		return companySetupList;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setSelectedCompanyTypeList(
			List<CommonDTO> selectedCompanyTypeList) {
		this.selectedCompanyTypeList = selectedCompanyTypeList;
	}

	public List<CommonDTO> getSelectedCompanyTypeList() {
		return selectedCompanyTypeList;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyTypeList(List<CommonDTO> companyTypeList) {
		this.companyTypeList = companyTypeList;
	}

	public List<CommonDTO> getCompanyTypeList() {
		return companyTypeList;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setSelectedPublishersList(List<CommonDTO> selectedPublishersList) {
		this.selectedPublishersList = selectedPublishersList;
	}

	public List<CommonDTO> getSelectedPublishersList() {
		return selectedPublishersList;
	}

	public void setPublishers(String publishers) {
		this.publishers = publishers;
	}

	public String getPublishers() {
		return publishers;
	}

	public void setPublishersList(List<CommonDTO> publishersList) {
		this.publishersList = publishersList;
	}

	public List<CommonDTO> getPublishersList() {
		return publishersList;
	}

	public void setSelectedDemandPartnersList(
			List<CommonDTO> selectedDemandPartnersList) {
		this.selectedDemandPartnersList = selectedDemandPartnersList;
	}

	public List<CommonDTO> getSelectedDemandPartnersList() {
		return selectedDemandPartnersList;
	}

	public void setDemandPartners(String demandPartners) {
		this.demandPartners = demandPartners;
	}

	public String getDemandPartners() {
		return demandPartners;
	}

	public void setDemandPartnersList(List<CommonDTO> demandPartnersList) {
		this.demandPartnersList = demandPartnersList;
	}

	public List<CommonDTO> getDemandPartnersList() {
		return demandPartnersList;
	}

	public void setSelectedPropertiesList(List<CommonDTO> selectedPropertiesList) {
		this.selectedPropertiesList = selectedPropertiesList;
	}

	public List<CommonDTO> getSelectedPropertiesList() {
		return selectedPropertiesList;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getProperties() {
		return properties;
	}

	public void setPropertiesList(List<CommonDTO> propertiesList) {
		this.propertiesList = propertiesList;
	}

	public List<CommonDTO> getPropertiesList() {
		return propertiesList;
	}

	public void setSelectedAppViewsList(List<CommonDTO> selectedAppViewsList) {
		this.selectedAppViewsList = selectedAppViewsList;
	}

	public List<CommonDTO> getSelectedAppViewsList() {
		return selectedAppViewsList;
	}

	public void setAppViews(String appViews) {
		this.appViews = appViews;
	}

	public String getAppViews() {
		return appViews;
	}

	public void setAppViewsList(List<CommonDTO> appViewsList) {
		this.appViewsList = appViewsList;
	}

	public List<CommonDTO> getAppViewsList() {
		return appViewsList;
	}

	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}

	public String getTeamDescription() {
		return teamDescription;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getTeamStatus() {
		return teamStatus;
	}

	public void setTeamStatus(String teamStatus) {
		this.teamStatus = teamStatus;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setDemandPartnerId(String demandPartnerId) {
		this.demandPartnerId = demandPartnerId;
	}

	public String getDemandPartnerId() {
		return demandPartnerId;
	}

	public void setPublisherSetupDTOList(
			List<PublisherSetupDTO> publisherSetupDTOList) {
		this.publisherSetupDTOList = publisherSetupDTOList;
	}

	public List<PublisherSetupDTO> getPublisherSetupDTOList() {
		return publisherSetupDTOList;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setRolesAndAuthorisationDTOList(
			List<RolesAndAuthorisationDTO> rolesAndAuthorisationDTOList) {
		this.rolesAndAuthorisationDTOList = rolesAndAuthorisationDTOList;
	}

	public List<RolesAndAuthorisationDTO> getRolesAndAuthorisationDTOList() {
		return rolesAndAuthorisationDTOList;
	}

	public void setCompanyIDOfSessionAdminUser(
			String companyIDOfSessionAdminUser) {
		CompanyIDOfSessionAdminUser = companyIDOfSessionAdminUser;
	}

	public String getCompanyIDOfSessionAdminUser() {
		return CompanyIDOfSessionAdminUser;
	}

	public void setTeamPropertiesObjList(
			List<TeamPropertiesObj> teamPropertiesObjList) {
		TeamPropertiesObjList = teamPropertiesObjList;
	}

	public List<TeamPropertiesObj> getTeamPropertiesObjList() {
		return TeamPropertiesObjList;
	}

	public void setAdminTeamList(List<CommonDTO> adminTeamList) {
		this.adminTeamList = adminTeamList;
	}

	public List<CommonDTO> getAdminTeamList() {
		return adminTeamList;
	}

	public void setNonAdminTeamList(List<CommonDTO> nonAdminTeamList) {
		this.nonAdminTeamList = nonAdminTeamList;
	}

	public List<CommonDTO> getNonAdminTeamList() {
		return nonAdminTeamList;
	}

	public void setSelectedRoleType(String selectedRoleType) {
		this.selectedRoleType = selectedRoleType;
	}

	public String getSelectedRoleType() {
		return selectedRoleType;
	}

	public void setNonAdminTeam(String nonAdminTeam) {
		this.nonAdminTeam = nonAdminTeam;
	}

	public String getNonAdminTeam() {
		return nonAdminTeam;
	}

	public void setAdminTeam(String adminTeam) {
		this.adminTeam = adminTeam;
	}

	public String getAdminTeam() {
		return adminTeam;
	}

	public void setSelectedAdminTeamList(List<CommonDTO> selectedAdminTeamList) {
		this.selectedAdminTeamList = selectedAdminTeamList;
	}

	public List<CommonDTO> getSelectedAdminTeamList() {
		return selectedAdminTeamList;
	}

	public void setSelectedNonAdminTeamList(
			List<CommonDTO> selectedNonAdminTeamList) {
		this.selectedNonAdminTeamList = selectedNonAdminTeamList;
	}

	public List<CommonDTO> getSelectedNonAdminTeamList() {
		return selectedNonAdminTeamList;
	}

	public String getDemandPartnerName() {
		return demandPartnerName;
	}

	public void setDemandPartnerName(String demandPartnerName) {
		this.demandPartnerName = demandPartnerName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDemandPartnerCategory() {
		return demandPartnerCategory;
	}

	public void setDemandPartnerCategory(String demandPartnerCategory) {
		this.demandPartnerCategory = demandPartnerCategory;
	}

	public String getDemandPartnerType() {
		return demandPartnerType;
	}

	public void setDemandPartnerType(String demandPartnerType) {
		this.demandPartnerType = demandPartnerType;
	}

	public List<CommonDTO> getDemandPartnerTypeList() {
		return demandPartnerTypeList;
	}

	public void setDemandPartnerTypeList(List<CommonDTO> demandPartnerTypeList) {
		this.demandPartnerTypeList = demandPartnerTypeList;
	}

	public List<CommonDTO> getSelectedDemandPartnerTypeList() {
		return selectedDemandPartnerTypeList;
	}

	public void setSelectedDemandPartnerTypeList(
			List<CommonDTO> selectedDemandPartnerTypeList) {
		this.selectedDemandPartnerTypeList = selectedDemandPartnerTypeList;
	}

	public String[] getPassbackSiteType() {
		return passbackSiteType;
	}

	public void setPassbackSiteType(String[] passbackSiteType) {
		this.passbackSiteType = passbackSiteType;
	}

	public void setPassbackSiteTypeList(List<CommonDTO> passbackSiteTypeList) {
		this.passbackSiteTypeList = passbackSiteTypeList;
	}

	public List<CommonDTO> getPassbackSiteTypeList() {
		return passbackSiteTypeList;
	}

	public void setPassbackSiteTypeCounterValue(
			String passbackSiteTypeCounterValue) {
		this.passbackSiteTypeCounterValue = passbackSiteTypeCounterValue;
	}

	public String getPassbackSiteTypeCounterValue() {
		return passbackSiteTypeCounterValue;
	}

	public void setAuthorisationKeywordList(
			List<String> authorisationKeywordList) {
		this.authorisationKeywordList = authorisationKeywordList;
	}

	public List<String> getAuthorisationKeywordList() {
		return authorisationKeywordList;
	}

	public void setAuthorisedPagesList(List<String> authorisedPagesList) {
		this.authorisedPagesList = authorisedPagesList;
	}

	public List<String> getAuthorisedPagesList() {
		return authorisedPagesList;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getMarket() {
		return market;
	}

	public void setDMARank(String dMARank) {
		DMARank = dMARank;
	}

	public String getDMARank() {
		return DMARank;
	}

	public String getDFPPropertyName() {
		return DFPPropertyName;
	}

	public void setDFPPropertyName(String dFPPropertyName) {
		DFPPropertyName = dFPPropertyName;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getMobileWebURL() {
		return mobileWebURL;
	}

	public void setMobileWebURL(String mobileWebURL) {
		this.mobileWebURL = mobileWebURL;
	}

	public String getTabletWebURL() {
		return tabletWebURL;
	}

	public void setTabletWebURL(String tabletWebURL) {
		this.tabletWebURL = tabletWebURL;
	}

	public String getGeneralManager() {
		return generalManager;
	}

	public void setGeneralManager(String generalManager) {
		this.generalManager = generalManager;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertySetupDTOList(List<PropertyObj> propertySetupDTOList) {
		this.propertySetupDTOList = propertySetupDTOList;
	}

	public List<PropertyObj> getPropertySetupDTOList() {
		return propertySetupDTOList;
	}

	/*
	 * public void setDFP_CredentialsCounterValue( String
	 * dFP_CredentialsCounterValue) { DFP_CredentialsCounterValue =
	 * dFP_CredentialsCounterValue; }
	 * 
	 * public String getDFP_CredentialsCounterValue() { return
	 * DFP_CredentialsCounterValue; }
	 * 
	 * public void setDFP_Id(String[] dFP_Id) { DFP_Id = dFP_Id; }
	 * 
	 * public String[] getDFP_Id() { return DFP_Id; }
	 * 
	 * public void setDFP_Username(String[] dFP_Username) { DFP_Username =
	 * dFP_Username; }
	 * 
	 * public String[] getDFP_Username() { return DFP_Username; }
	 * 
	 * public void setDFP_Password(String[] dFP_Password) { DFP_Password =
	 * dFP_Password; }
	 * 
	 * public String[] getDFP_Password() { return DFP_Password; }
	 * 
	 * public void setCredentialsForCompanyDTOList(
	 * List<DFPCredentialsForCompanyDTO> credentialsForCompanyDTOList) {
	 * this.credentialsForCompanyDTOList = credentialsForCompanyDTOList; }
	 * 
	 * public List<DFPCredentialsForCompanyDTO>
	 * getCredentialsForCompanyDTOList() { return credentialsForCompanyDTOList;
	 * }
	 */

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setDefaultSelectedCompanyType(String defaultSelectedCompanyType) {
		this.defaultSelectedCompanyType = defaultSelectedCompanyType;
	}

	public String getDefaultSelectedCompanyType() {
		return defaultSelectedCompanyType;
	}

	public void setOrdersObjList(List<OrdersObj> ordersObjList) {
		this.ordersObjList = ordersObjList;
	}

	public List<OrdersObj> getOrdersObjList() {
		return ordersObjList;
	}

	public void setDfpAdvertisersObjList(
			List<DFPAdvertisersObj> dfpAdvertisersObjList) {
		this.dfpAdvertisersObjList = dfpAdvertisersObjList;
	}

	public List<DFPAdvertisersObj> getDfpAdvertisersObjList() {
		return dfpAdvertisersObjList;
	}

	public void setSelectedOrdersObjList(List<OrdersObj> selectedOrdersObjList) {
		this.selectedOrdersObjList = selectedOrdersObjList;
	}

	public List<OrdersObj> getSelectedOrdersObjList() {
		return selectedOrdersObjList;
	}

	public void setSelectedDfpAdvertisersObjList(
			List<DFPAdvertisersObj> selectedDfpAdvertisersObjList) {
		this.selectedDfpAdvertisersObjList = selectedDfpAdvertisersObjList;
	}

	public List<DFPAdvertisersObj> getSelectedDfpAdvertisersObjList() {
		return selectedDfpAdvertisersObjList;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public String getOrders() {
		return orders;
	}

	public void setSelectedCompanyType(String selectedCompanyType) {
		this.selectedCompanyType = selectedCompanyType;
	}

	public String getSelectedCompanyType() {
		return selectedCompanyType;
	}

	public void setAdServerCredentialsCounterValue(
			String adServerCredentialsCounterValue) {
		this.adServerCredentialsCounterValue = adServerCredentialsCounterValue;
	}

	public String getAdServerCredentialsCounterValue() {
		return adServerCredentialsCounterValue;
	}

	public void setAdServerPassword(String[] adServerPassword) {
		this.adServerPassword = adServerPassword;
	}

	public String[] getAdServerPassword() {
		return adServerPassword;
	}

	public void setAdServerUsername(String[] adServerUsername) {
		this.adServerUsername = adServerUsername;
	}

	public String[] getAdServerUsername() {
		return adServerUsername;
	}

	public void setAdServerId(String[] adServerId) {
		this.adServerId = adServerId;
	}

	public String[] getAdServerId() {
		return adServerId;
	}

	public void setNumberOfStations(String numberOfStations) {
		this.numberOfStations = numberOfStations;
	}

	public String getNumberOfStations() {
		return numberOfStations;
	}

	public void setAdServerCredentialsDTOList(
			List<AdServerCredentialsDTO> adServerCredentialsDTOList) {
		this.adServerCredentialsDTOList = adServerCredentialsDTOList;
	}

	public List<AdServerCredentialsDTO> getAdServerCredentialsDTOList() {
		return adServerCredentialsDTOList;
	}

	public void setAdServerForPublisherList(
			List<CommonDTO> adServerForPublisherList) {
		this.adServerForPublisherList = adServerForPublisherList;
	}

	public List<CommonDTO> getAdServerForPublisherList() {
		return adServerForPublisherList;
	}

	public void setAdServerForPublisher(String adServerForPublisher) {
		this.adServerForPublisher = adServerForPublisher;
	}

	public String getAdServerForPublisher() {
		return adServerForPublisher;
	}

	public void setSelectedAdServerForPublisherList(
			List<CommonDTO> selectedAdServerForPublisherList) {
		this.selectedAdServerForPublisherList = selectedAdServerForPublisherList;
	}

	public List<CommonDTO> getSelectedAdServerForPublisherList() {
		return selectedAdServerForPublisherList;
	}

	public void setDfpAgencyObjList(List<DFPAgencyObj> dfpAgencyObjList) {
		this.dfpAgencyObjList = dfpAgencyObjList;
	}

	public List<DFPAgencyObj> getDfpAgencyObjList() {
		return dfpAgencyObjList;
	}

	public void setSelectedDfpAgencyObjList(
			List<DFPAgencyObj> selectedDfpAgencyObjList) {
		this.selectedDfpAgencyObjList = selectedDfpAgencyObjList;
	}

	public List<DFPAgencyObj> getSelectedDfpAgencyObjList() {
		return selectedDfpAgencyObjList;
	}

	public void setAdServerList(List<CommonDTO> adServerList) {
		this.adServerList = adServerList;
	}

	public List<CommonDTO> getAdServerList() {
		return adServerList;
	}

	public void setSelectedAdServerList(List<CommonDTO> selectedAdServerList) {
		this.selectedAdServerList = selectedAdServerList;
	}

	public List<CommonDTO> getSelectedAdServerList() {
		return selectedAdServerList;
	}

	public void setAdServer(String adServer) {
		this.adServer = adServer;
	}

	public String getAdServer() {
		return adServer;
	}

	public void setAgencyIdStringForBigQuery(String agencyIdStringForBigQuery) {
		this.agencyIdStringForBigQuery = agencyIdStringForBigQuery;
	}

	public String getAgencyIdStringForBigQuery() {
		return agencyIdStringForBigQuery;
	}

	public void setAdvertiserIdStringForBigQuery(
			String advertiserIdStringForBigQuery) {
		this.advertiserIdStringForBigQuery = advertiserIdStringForBigQuery;
	}

	public String getAdvertiserIdStringForBigQuery() {
		return advertiserIdStringForBigQuery;
	}

	public void setCompanyTypeToCreate(String companyTypeToCreate) {
		this.companyTypeToCreate = companyTypeToCreate;
	}

	public String getCompanyTypeToCreate() {
		return companyTypeToCreate;
	}

	public void setCompanyTypeToUpdate(String companyTypeToUpdate) {
		this.companyTypeToUpdate = companyTypeToUpdate;
	}

	public String getCompanyTypeToUpdate() {
		return companyTypeToUpdate;
	}

	public void setFormerCompanyType(String formerCompanyType) {
		this.formerCompanyType = formerCompanyType;
	}

	public String getFormerCompanyType() {
		return formerCompanyType;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	public String getAccounts() {
		return accounts;
	}

	public void setPublisherIdsForBigQuery(String publisherIdsForBigQuery) {
		this.publisherIdsForBigQuery = publisherIdsForBigQuery;
	}

	public String getPublisherIdsForBigQuery() {
		return publisherIdsForBigQuery;
	}

	public void setSaveDefaultSettings(String saveDefaultSettings) {
		this.saveDefaultSettings = saveDefaultSettings;
	}

	public String getSaveDefaultSettings() {
		return saveDefaultSettings;
	}

	public void setDefaultSettingsSaveStatus(String defaultSettingsSaveStatus) {
		this.defaultSettingsSaveStatus = defaultSettingsSaveStatus;
	}

	public String getDefaultSettingsSaveStatus() {
		return defaultSettingsSaveStatus;
	}

	public void setServiceURLCounterValue(String serviceURLCounterValue) {
		this.serviceURLCounterValue = serviceURLCounterValue;
	}

	public String getServiceURLCounterValue() {
		return serviceURLCounterValue;
	}

	public void setServiceURL(String[] serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String[] getServiceURL() {
		return serviceURL;
	}

	public void setServiceURLList(List<CommonDTO> serviceURLList) {
		this.serviceURLList = serviceURLList;
	}

	public List<CommonDTO> getServiceURLList() {
		return serviceURLList;
	}

	public void setAdServerInfo(String adServerInfo) {
		this.adServerInfo = adServerInfo;
	}

	public String getAdServerInfo() {
		return adServerInfo;
	}

	public String getCompanyLogoContentType() {
		return companyLogoContentType;
	}

	public void setCompanyLogoContentType(String companyLogoContentType) {
		this.companyLogoContentType = companyLogoContentType;
	}

	public String getCompanyLogoFileName() {
		return companyLogoFileName;
	}

	public void setCompanyLogoFileName(String companyLogoFileName) {
		this.companyLogoFileName = companyLogoFileName;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeamType() {
		return teamType;
	}

	public void setAccessToAccounts(String accessToAccounts) {
		this.accessToAccounts = accessToAccounts;
	}

	public String getAccessToAccounts() {
		return accessToAccounts;
	}

	public void setAccessToProperties(String accessToProperties) {
		this.accessToProperties = accessToProperties;
	}

	public String getAccessToProperties() {
		return accessToProperties;
	}

	public void setAllPropertiesFlag(String allPropertiesFlag) {
		this.allPropertiesFlag = allPropertiesFlag;
	}

	public String getAllPropertiesFlag() {
		return allPropertiesFlag;
	}

	public void setCompanyLogoURL(String companyLogoURL) {
		this.companyLogoURL = companyLogoURL;
	}

	public String getCompanyLogoURL() {
		return companyLogoURL;
	}

	public void setIndustryList(List<IndustryObj> industryList) {
		this.industryList = industryList;
	}

	public List<IndustryObj> getIndustryList() {
		return industryList;
	}

	public void setDfpAccountName(String dfpAccountName) {
		this.dfpAccountName = dfpAccountName;
	}

	public String getDfpAccountName() {
		return dfpAccountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIndustry() {
		return industry;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountDfpId(String accountDfpId) {
		this.accountDfpId = accountDfpId;
	}

	public String getAccountDfpId() {
		return accountDfpId;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return contact;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkUsername(String networkUsername) {
		this.networkUsername = networkUsername;
	}

	public String getNetworkUsername() {
		return networkUsername;
	}

	public void setNetworkAvailabile(boolean networkAvailabile) {
		this.networkAvailabile = networkAvailabile;
	}

	public boolean isNetworkAvailabile() {
		return networkAvailabile;
	}

	public void setNetworkPassword(String networkPassword) {
		this.networkPassword = networkPassword;
	}

	public String getNetworkPassword() {
		return networkPassword;
	}

	public void setDFPPropertyId(String dFPPropertyId) {
		DFPPropertyId = dFPPropertyId;
	}

	public String getDFPPropertyId() {
		return DFPPropertyId;
	}

	public void setAccountsFlag(String accountsFlag) {
		this.accountsFlag = accountsFlag;
	}

	public String getAccountsFlag() {
		return accountsFlag;
	}

	public void setAccountsObjList(List<AccountsEntity> accountsObjList) {
		this.accountsObjList = accountsObjList;
	}

	public List<AccountsEntity> getAccountsObjList() {
		return accountsObjList;
	}

	public void setAccountsList(List<CommonDTO> accountsList) {
		this.accountsList = accountsList;
	}

	public List<CommonDTO> getAccountsList() {
		return accountsList;
	}

	public void setSelectedAccountsList(List<CommonDTO> selectedAccountsList) {
		this.selectedAccountsList = selectedAccountsList;
	}

	public List<CommonDTO> getSelectedAccountsList() {
		return selectedAccountsList;
	}

	public List<String> getChilds() {
		return childs;
	}

	public void setChilds(List<String> childs) {
		this.childs = childs;
	}

	public boolean isOptEmail() {
		return optEmail;
	}

	public void setOptEmail(boolean optEmail) {
		this.optEmail = optEmail;
	}

}
