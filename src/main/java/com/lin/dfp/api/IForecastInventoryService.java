package com.lin.dfp.api;

import java.util.List;
import java.util.Map;

import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.jaxws.v201403.ApiException_Exception;
import com.google.api.ads.dfp.jaxws.v201403.CostType;
import com.google.api.ads.dfp.jaxws.v201403.LineItemType;
import com.google.api.ads.dfp.jaxws.v201403.Size;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.server.bean.CreativeObj;
import com.lin.server.bean.DeviceObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.GeoTargetsObj;
import com.lin.server.bean.ProductsObj;
import com.lin.server.bean.StateObj;
import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ForcastDTO;
import com.lin.web.dto.NewAdvertiserViewDTO;
import com.lin.web.dto.ZipDTO;

public interface IForecastInventoryService {

	public ForcastDTO loadForecastDataByAdUnit(DfpServices dfpServices, 
   		  DfpSession session, String adUnitId, String adUnitName,String start,String end) throws Exception;
	
	public ForcastDTO loadForecastInventoryByAdUnit(DfpServices dfpServices, DfpSession session, String adUnitId, 
  		  String adUnitName,String startDate,String endDate,LineItemType lineItemType,CostType lineItemCostType,
  		  String [] creativeSizeArr) throws ApiException_Exception;
	
	public ForcastDTO loadForecastInventoryByAdUnit(String networkCode, List<AdUnitDTO> adUnits,
			String startDate,String endDate,LineItemType lineItemType,CostType lineItemCostType,
	  		String [] creativeSizeArr, Integer deviceCapability, List<DeviceObj> devices, List<StateObj> states, List<GeoTargetsObj> dmas, List<CityDTO> cities, List<ZipDTO> zips) throws ApiException_Exception;
	
    public List<Size> loadCreativeSizes(String [] creativeSizeArr);
    
    public List<Size> loadCreativeSizes();
    
	public List<ForcastInventoryObj> deleteUpdateForcastInventoryObj(List<ForcastDTO> inventoryList,
			   String startDate,String endDate, boolean doEmptyTable);
	
	public Map<String,ForcastDTO> loadForecastInventoryByAdUnit(DfpServices dfpServices, DfpSession session,
			 List<ProductsObj> productList,  String startDate,String endDate,LineItemType lineItemType,
			 CostType lineItemCostType) throws ApiException_Exception;
	
	public List<Size> loadCreativeSizes(List<CreativeObj> creativeList);
	
	public NewAdvertiserViewDTO getLineItemForcast(DfpServices dfpServices,DfpSession session,String lineItemId,String bookedImp,String deliveredImp);

	void addTaskToGetProductForcast();

	boolean setProductFrocast(String productId, String dmaId, String dmaName);
}
