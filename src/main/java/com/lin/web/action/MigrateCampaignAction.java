package com.lin.web.action;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.CompanyObj;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.service.IReportService;
import com.lin.web.service.ISmartCampaignPlannerService;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.StringUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class MigrateCampaignAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	private static final Logger log = Logger.getLogger(MigrateCampaignAction.class.getName());
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private SessionObjectDTO sessionDTO;
	private JSONObject jsonObject;
	
	
	public String migrateCampaign() {
		String error=null;
		try {
			IUserService service = (IUserService) BusinessServiceLocator.locate(IUserService.class);
			String dfpNetworkCode = request.getParameter("dfpNetworkCode");
			String dfpNetworkName=request.getParameter("dfpNetworkName");
			String publisherIdInBQ=request.getParameter("publisherIdInBQ");
			String companyId=request.getParameter("companyId");
			log.info("dfpNetworkCode = "+dfpNetworkCode+" dfpNetworkName = "+dfpNetworkName+" publisherIdInBQ = "+publisherIdInBQ+" companyId = "+companyId);
			if(dfpNetworkCode != null && dfpNetworkCode.trim().length() > 0 && dfpNetworkName != null && dfpNetworkName.trim().length() > 0 && publisherIdInBQ != null && publisherIdInBQ.trim().length() > 0 && companyId != null && companyId.trim().length() > 0) {
				CompanyObj companyObj = service.getCompanyById(StringUtil.getLongValue(companyId), MemcacheUtil.getAllCompanyList());
				if(companyObj != null && companyObj.getCompanyName() != null) {
					ISmartCampaignPlannerService campaignPlannerService = (ISmartCampaignPlannerService)BusinessServiceLocator.locate(ISmartCampaignPlannerService.class);
					jsonObject = campaignPlannerService.migrateCampaign(dfpNetworkCode.trim(), dfpNetworkName.trim(), publisherIdInBQ.trim(), companyId.trim(), companyObj.getCompanyName().trim());
				}
				else {
					jsonObject.put("error", "Company for Id "+companyId+" does not exist");
				}
			}else{
				log.warning("Invalid input parameters");
				jsonObject.put("error", "Invalid input parameters.");
			}
		} catch(Exception e) {
			 log.severe("Exception : "+e.getMessage());
			 error=e.getMessage();
		}finally{
			if(error !=null){
				jsonObject.put("error", error);
			}			
		}
		return Action.SUCCESS;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.setResponse(response);
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.setRequest(request);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	
	
	
	
}