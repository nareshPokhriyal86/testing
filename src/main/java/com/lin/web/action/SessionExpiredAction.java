package com.lin.web.action;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;



public class SessionExpiredAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, SessionAware {

	private Map session;
	private HttpServletRequest request;
	HttpServletResponse response;
	private String loginStatus;

	public void setSession(Map session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map getSession() {
		return session;
	}

	public String sessionExpired() {
		setLoginStatus("sessionExpired");
		return "sessionExpired";
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
