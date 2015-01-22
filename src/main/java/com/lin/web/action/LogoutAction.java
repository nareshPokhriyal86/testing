package com.lin.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.LinMobileConstants;
import com.opensymphony.xwork2.ActionSupport;



public class LogoutAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware{
	private String loginStatus;
	private Map session;
    String userId;
    private HttpServletRequest request;
    HttpServletResponse response;
    private SessionObjectDTO sessionDTO;
    
    public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;
    
	public void setSession(Map session) {
		this.session = session;
		}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}	
		public Map getSession() {
		return session;
	}
		
	public String logout() {
		try {
			if(session!=null){
				sessionDTO = (SessionObjectDTO) session.get("sessionDTO");	
			}else{
				setLoginStatus("sessionExpired");
				return "sessionexpired";
			}
			
			if (sessionDTO == null) {
				sessionDTO = new SessionObjectDTO();
			}
			
			session.remove("sessionDTO");
			session.clear();
			
			if (session instanceof org.apache.struts2.dispatcher.SessionMap) {
				try {
					((org.apache.struts2.dispatcher.SessionMap) session)
							.invalidate();
				} catch (IllegalStateException exp) {

				}
			}

			request.setAttribute("LoginStatus", "logout");
			
		} catch (Exception e) { 
			
			setLoginStatus("......");
            return "error";
		}
			setLoginStatus("logout");
			return "success";
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String isLoginStatus() {
		return loginStatus;
	}
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}
}
