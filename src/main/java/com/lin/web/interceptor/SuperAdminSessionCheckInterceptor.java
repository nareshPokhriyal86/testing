package com.lin.web.interceptor;

import java.util.Map;
import java.util.logging.Logger;

import com.lin.web.dto.SessionObjectDTO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SuperAdminSessionCheckInterceptor implements Interceptor {
	
	private static final long serialVersionUID = 1L;
	private String status;
	private SessionObjectDTO sessionDTO;
	private static final Logger log = Logger.getLogger(SuperAdminSessionCheckInterceptor.class.getName());
	
	public void destroy() {
		log.warning(" SuperAdminSessionCheckInterceptor destroy() is called...");
	}

	public void init() {
		log.warning(" SuperAdminSessionCheckInterceptor init() is called...");
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext context = actionInvocation.getInvocationContext();
		Map<String, Object> sessionMap = context.getSession();
		sessionDTO=(SessionObjectDTO) sessionMap.get("sessionDTO");
		if(sessionMap != null && !sessionMap.isEmpty() && (sessionDTO!=null && sessionDTO.getEmailId()!=null && sessionDTO.isSuperAdmin())) {		
			log.warning("User is SuperAdmin");
			
		}else{
			log.warning("Not a SuperAdmin");
			//setStatus("You do not have the required permission.");
			return "unAuthorisedAccess";
		}
		
		String actionResult = actionInvocation.invoke();
		return actionResult;
		}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
