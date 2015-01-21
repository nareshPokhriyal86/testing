package com.lin.web.service.impl;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.AccountsHistObj;
import com.lin.server.bean.ActualAdvertiserObj;
import com.lin.server.bean.ActualPublisherObj;
import com.lin.server.bean.AdFormatObj;
import com.lin.server.bean.AdSizeObj;
import com.lin.server.bean.AdUnitDataObj;
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.server.bean.AdvertiserByLocationObj;
import com.lin.server.bean.AdvertiserByMarketObj;
import com.lin.server.bean.AdvertiserObj;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.AgencyAdvertiserObj;
import com.lin.server.bean.AgencyObj;
import com.lin.server.bean.AlertEngineFlightObj;
import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.AnonymousUserDetailsObj;
import com.lin.server.bean.AuthorisationTextObj;
import com.lin.server.bean.DFPTaskEntity;
import com.lin.server.bean.DailyDataProcessObj;
import com.lin.server.bean.ProcessFileObj;
import com.lin.server.bean.ProductForecastObj;
import com.lin.server.bean.SmartCampaignHistObj;
import com.lin.server.bean.CityObj;
import com.lin.server.bean.CompanyHistObj;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CountryObj;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.CustomLineItemObj;
import com.lin.server.bean.DFPAdvertisersObj;
import com.lin.server.bean.DFPAgencyObj;
import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.DataCollectorReport;
import com.lin.server.bean.DataProcessorReport;
import com.lin.server.bean.DataUploaderReport;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.DfpOrderIdsObj;
import com.lin.server.bean.DropdownDataObj;
import com.lin.server.bean.EmailAuthObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.ForcastedAdvertiserObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.IABContextObj;
import com.lin.server.bean.IndustryObj;
import com.lin.server.bean.InsertionOrderObj;
import com.lin.server.bean.KPIObj;
import com.lin.server.bean.LineItemObj;
import com.lin.server.bean.MaxCountUserDetailsObj;
import com.lin.server.bean.OrdersObj;
import com.lin.server.bean.PerformanceMetricsObj;
import com.lin.server.bean.PlacementHistoryObj;
import com.lin.server.bean.PlacementObj;
import com.lin.server.bean.PlatformObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.PropertyHistObj;
import com.lin.server.bean.PropertyObj;
import com.lin.server.bean.ProposalHistoryObj;
import com.lin.server.bean.ProposalObj;
import com.lin.server.bean.PublisherChannelObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.server.bean.ReallocationDataObj;
import com.lin.server.bean.RolesAndAuthorisation;
import com.lin.server.bean.SellThroughDataObj;
import com.lin.server.bean.SitesObj;
import com.lin.server.bean.SmartCampaignFlightObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.SmartCampaignPlacementObj;
import com.lin.server.bean.SmartMediaPlanObj;
import com.lin.server.bean.StateObj;
import com.lin.server.bean.TeamPropertiesHistObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.TrackCronJobReport;
import com.lin.server.bean.UserDetailsHistObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.AdSdkDTO;
import com.lin.web.dto.CensusDTO;

/*
 * Custom Objectify Service
 */
public class OfyService {

	static {
		ObjectifyService.register(DataCollectorReport.class);
		ObjectifyService.register(DataProcessorReport.class);
		ObjectifyService.register(DataUploaderReport.class);
		ObjectifyService.register(TrackCronJobReport.class);
		ObjectifyService.register(LineItemObj.class);
		ObjectifyService.register(AdvertiserReportObj.class);
		//ObjectifyService.register(PublisherViewDTO.class);
		//ObjectifyService.register(LeftMenuDTO.class);
		ObjectifyService.register(AgencyAdvertiserObj.class);
		//ObjectifyService.register(OrderLineItemObj.class);
		ObjectifyService.register(ReallocationDataObj.class);
		ObjectifyService.register(ActualAdvertiserObj.class);
		ObjectifyService.register(ForcastedAdvertiserObj.class);		
		ObjectifyService.register(CustomLineItemObj.class);
		ObjectifyService.register(PerformanceMetricsObj.class);
		ObjectifyService.register(AdvertiserByLocationObj.class);
		ObjectifyService.register(AdvertiserByMarketObj.class);
		ObjectifyService.register(PublisherChannelObj.class);
		ObjectifyService.register(PublisherPropertiesObj.class);
		ObjectifyService.register(SellThroughDataObj.class);
		ObjectifyService.register(UserDetailsObj.class);
		ObjectifyService.register(ActualPublisherObj.class);
		ObjectifyService.register(MaxCountUserDetailsObj.class);
		ObjectifyService.register(EmailAuthObj.class);
		ObjectifyService.register(UserDetailsHistObj.class);
		ObjectifyService.register(AnonymousUserDetailsObj.class);
		ObjectifyService.register(TeamPropertiesObj.class);
		ObjectifyService.register(TeamPropertiesHistObj.class);
		ObjectifyService.register(RolesAndAuthorisation.class);
		ObjectifyService.register(AuthorisationTextObj.class);
		ObjectifyService.register(CompanyObj.class);
		ObjectifyService.register(CompanyHistObj.class);
		ObjectifyService.register(PropertyObj.class);
		ObjectifyService.register(PropertyHistObj.class);
		ObjectifyService.register(ProposalObj.class);
		ObjectifyService.register(AdvertiserObj.class);
		ObjectifyService.register(AgencyObj.class);
		ObjectifyService.register(GeoTargetsObj.class);
		ObjectifyService.register(PlacementObj.class);
		ObjectifyService.register(SitesObj.class);
		ObjectifyService.register(DfpOrderIdsObj.class);
		ObjectifyService.register(IndustryObj.class);
		ObjectifyService.register(KPIObj.class);
		ObjectifyService.register(AdSizeObj.class);
		ObjectifyService.register(AdFormatObj.class);
		ObjectifyService.register(InsertionOrderObj.class);
		ObjectifyService.register(PlacementHistoryObj.class);
		ObjectifyService.register(ProposalHistoryObj.class);
		ObjectifyService.register(DFPAgencyObj.class);
		ObjectifyService.register(DFPAdvertisersObj.class);
		ObjectifyService.register(OrdersObj.class);
		ObjectifyService.register(DropdownDataObj.class);
		ObjectifyService.register(AdUnitDataObj.class);
		ObjectifyService.register(AccountsEntity.class);
		ObjectifyService.register(AccountsHistObj.class);
		ObjectifyService.register(FinalisedTableDetailsObj.class);
		ObjectifyService.register(DFPSitesWithDMAObj.class);
		ObjectifyService.register(ForcastInventoryObj.class);
		ObjectifyService.register(ProductsObj.class);
		ObjectifyService.register(IABContextObj.class);
		ObjectifyService.register(SmartCampaignFlightObj.class);
		ObjectifyService.register(SmartCampaignObj.class);
		ObjectifyService.register(SmartCampaignPlacementObj.class);
		ObjectifyService.register(DeviceObj.class);
		ObjectifyService.register(PlatformObj.class);
		ObjectifyService.register(CityObj.class);
		ObjectifyService.register(StateObj.class);
		ObjectifyService.register(CountryObj.class);
		ObjectifyService.register(CreativeObj.class);
		ObjectifyService.register(AdUnitHierarchy.class);
		ObjectifyService.register(SmartMediaPlanObj.class);
		ObjectifyService.register(AlertEngineObj.class);
		ObjectifyService.register(AlertEngineFlightObj.class);
		ObjectifyService.register(SmartCampaignHistObj.class);
		ObjectifyService.register(ProcessFileObj.class);
		ObjectifyService.register(CensusDTO.class);
		ObjectifyService.register(DailyDataProcessObj.class);
		ObjectifyService.register(ProductForecastObj.class);
		ObjectifyService.register(AdSdkDTO.class);
		ObjectifyService.register(DFPTaskEntity.class);
		
    }

    public static Objectify ofy() {       
        //return ObjectifyService.begin();  //For objectifiy 3
        return ObjectifyService.ofy();

    }

    public static ObjectifyFactory factory() {    	
        return ObjectifyService.factory();
    }
}

