package com.lin.persistance.dao;

import java.util.Date;
import java.util.List;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.SmartCampaignObj;
import com.lin.server.bean.TeamPropertiesObj;
import com.lin.server.bean.UserDetailsObj;
import com.lin.web.dto.PerformanceMonitoringDTO;
import com.lin.web.dto.QueryDTO;

public interface IAlertEngineDAO {

	List<PerformanceMonitoringDTO> loadAllCampaignData(QueryDTO queryDTO,
			String orderIds);

	List<SmartCampaignObj> getRunningCampaignList(String companyId) throws Exception;

	List<PerformanceMonitoringDTO> loadAllLineItemsForOrderId(String orderId,
			QueryDTO queryDTO, String alertDate);

	void saveObject(Object obj) throws DataServiceException;

	List<PerformanceMonitoringDTO> loadAllLineItemsForFlights(
			String lineItemId, String startDate, String endDate,
			QueryDTO queryDTO);
	
	public  List<AlertEngineObj>  getAllCampaigns() throws Exception;
	public  List<AlertEngineObj>  getAllCampaigns(Date date) throws Exception;

/*	public List<AlertEngineObj> getAllCampaignAlert(List<Long> campaignIdList)
			throws Exception;*/

	List<AlertEngineObj> getAllCampaignAlert(String companyId) throws Exception;

	List<AlertEngineObj> getAllCampaignAlertSuperUser() throws Exception;

	

	
}
