package com.lin.web.action;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.Action;

public class HomeAction  implements ServletRequestAware{

	
	private static final Logger log = Logger.getLogger(HomeAction.class.getName());
	
	private String status;
	private String currentTime;
	private HttpServletRequest request;
	
	public String execute(){		
		log.info("Home action executes..");
		//ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		status="Success";
		return Action.SUCCESS;
	}	
	
	public String getLatestTime(){		
		log.info("serverTime action executes..");
		//ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		status="";
		Date date=new Date();
		currentTime=date+"";
		return Action.SUCCESS;
	}	

	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getStatus() {
		return status;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getCurrentTime() {
		return currentTime;
	}

}
