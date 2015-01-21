package com.lin.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.lin.server.bean.AlertEngineObj;
import com.lin.server.bean.CompanyObj;
import com.lin.web.dto.PerformanceMonitoringAlertDTO;
import com.lin.web.dto.PerformanceMonitoringDTO;

public interface IAlertEngineService extends IBusinessService{

	public String alertEngine(List<CompanyObj> companyList, String alertDate);
	public Map<Long,Map> campaignStatusAlertMail() throws Exception;
	public void sendCampaignStatusAlertMail(long publisherId, Map map,List<PerformanceMonitoringDTO> campaignList) throws Exception;
	public JSONArray loadAllCampaignAlert(long userId, boolean isSuperAdmin,
			int campaignPerPage, int pageNumber, String dateSearch, String campaignSearch, String placementSearch, String publisherSearch);
	public String getAlertMessage(AlertEngineObj alertEngineObj) throws Exception;
	public List<PerformanceMonitoringDTO> loadAllRunningCampaigns(String publisherIdInBQ);
	public List<PerformanceMonitoringAlertDTO> loadAllCampaigns(List<PerformanceMonitoringDTO> campaignList, String campaignStatus, long userId, boolean superAdmin);
	String alertEngine1(String publisherIdInBQ, String alertDate);
	String generateCampaignAlert(String orderId, String startDate,
			String endDate, String alertDate, String publisherIdInBQ);
	

}
