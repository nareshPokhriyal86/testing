package com.lin.persistance.dao;

import java.util.List;

import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.web.service.IBusinessService;

public interface IInventoryForecastingDAO extends IBusinessService {
	
	public List<DFPSitesWithDMAObj> getAllDFPSitesWithDMA();
	public void deleteUpdateForcastInventoryObj(List<ForcastInventoryObj> forcastInventoryObjList, boolean doEmptyTable);

}
