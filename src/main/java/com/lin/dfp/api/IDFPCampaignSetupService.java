package com.lin.dfp.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.AdUnit;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.Company;
import com.google.api.ads.dfp.jaxws.v201403.CompanyType;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.LineItem;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.DFPCompanyDTO;
import com.lin.web.dto.OrderDTO;

public interface IDFPCampaignSetupService {	
	
	public OrderDTO createNewOrder(AgencyObj agency,AdvertiserObj advertiser, String campaignName,
			String startDate,String endDate) throws ApiException_Exception, DataServiceException;
	
	public boolean approveOrder(long orderId, DfpServices dfpServices,
			DfpSession dfpSession) throws ApiException_Exception;
	
	public String generateOrderName(String agencyName,String advertiserName,String campaignName, String startDate);
	
	public String generateLineItemName(String advertiserName,String campaignName,
			String flightDuration,String placement,String publisherName, String productName);
	
	public String generateCreativeName(String lineItemName, String adSize);
	
	public long createNewLineItem(double rate, CostType costType,LineItemType lineItemType,
			String lineItemName, long orderId,	String startDate,String endDate,
			String adUnitId2,String boughtUnits, String [] adSizes,
			DfpServices dfpServices, DfpSession dfpSession, Integer deviceCapability ) throws ApiException_Exception;
	
	public long createNewImageCreative(DfpServices dfpServices,DfpSession dfpSession,
			String creativeName,long advertiserId);	
	public void createNewCreativeLineItemAssociation(long creativeId, long lineItemId,
			  DfpServices dfpServices,DfpSession dfpSession);	
	public Map<Long,String> getCreativeURLsByLineItemId(DfpServices dfpServices, 
			  DfpSession session, long lineItemId);
	public Map<Long,String> getCreativePreviewURL(DfpServices dfpServices, 
			  DfpSession session,List<Long> creativeIdList);	
	
	public Company createCompany(DfpServices dfpServices, DfpSession session,
			DFPCompanyDTO companyDTO);
	public Set<Long> fetchExistingDFPCompany(DfpServices dfpServices, DfpSession session,
			String companyName,CompanyType companyType);	
		
	public String createTopLevelAdUnitName(String publisherName);
	
	public SmartMediaPlanObj setupCampaignOnDFP(SmartMediaPlanObj smartMediaPlan ) throws Exception;
	public SmartMediaPlanObj setupPlacementsOnDFP(SmartMediaPlanObj smartMediaPlan ) throws Exception;
	
	public List<LineItem> getLineItemsNeedsCreative(DfpServices dfpServices, DfpSession session,long orderId) 
			throws ApiException_Exception;
	
	public List<LineItem> getDeliveringLineItems(DfpServices dfpServices, DfpSession session,long orderId) 
			throws ApiException_Exception;
	
	public Map<String,AdUnit> fetchAdUnits(DfpServices dfpServices, DfpSession session,String adUnitName,
			String parentAdUnitId)	throws ApiException_Exception;
	public AdUnitDTO createTopLevelAdUnit(DfpServices dfpServices, DfpSession session, String adUnitName,
			String [] adSizeArr)  throws ApiException_Exception;
	public AdUnitDTO createAdUnit2(DfpServices dfpServices, DfpSession session, String topLevelAdUnitId,
			String adUnitName2,String [] adSizeArr) throws ApiException_Exception;
	
	
	public String generateGPTPassbackTags(String networkCode,String adUnitCode1,String adUnitCode2,String adSize);
	public List<String> generateBothGPTPassbackTags(String networkCode, AdUnitDTO adUnitObjLevel1,
			AdUnitDTO adUnitObjLevel2, String adSize);
	 public List<String> generateBothGPTPassbackTags(String networkCode, AdUnitDTO adUnitObjLevel1,
			   AdUnitDTO adUnitObjLevel2, String [] creativeAdSizeArr);
	 
	 public List<LineItem> createLineItems(List<LineItem> lineItemList,long orderId,
				DfpServices dfpServices, DfpSession dfpSession ) throws ApiException_Exception;
	 
	 AccountsEntity saveOrUpdateAccountInDFPAndDatastore(DfpServices dfpServices,DfpSession dfpSession, 
				AccountsEntity account);	
}
