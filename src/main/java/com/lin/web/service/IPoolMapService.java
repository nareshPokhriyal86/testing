package com.lin.web.service;

import java.util.List;
import java.util.Map;

import com.lin.server.bean.ForcastInventoryObj;
import com.lin.web.dto.ForcastInventoryDTO;

/*
 * @author Youdhveer Panwar
 * IMediaPlanService Interface
 */
public interface IPoolMapService extends IBusinessService{

	public Map<String,String> loadAdUnitIda(String publisherId);
	public List<ForcastInventoryDTO> loadAllDMAsWithInventory(String startDate, String endDate);
	public List<ForcastInventoryDTO> loadAllocateInventry(String startDate, String endDate, String codeList);
}
