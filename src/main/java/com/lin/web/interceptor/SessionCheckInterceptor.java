package com.lin.web.interceptor;

import java.util.Map;
import java.util.logging.Logger;

import com.lin.web.dto.SessionObjectDTO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SessionCheckInterceptor implements Interceptor {
	
	private static final long serialVersionUID = 1L;
	private String status;
	private SessionObjectDTO sessionDTO;
	private static final Logger log = Logger.getLogger(SessionCheckInterceptor.class.getName());
	
	public void destroy() {
		log.warning(" SessionCheckInterceptor destroy() is called...");
	}

	public void init() {
		log.warning(" SessionCheckInterceptor init() is called...");
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext context = actionInvocation.getInvocationContext();
		Map<String, Object> sessionMap = context.getSession();
		sessionDTO=(SessionObjectDTO) sessionMap.get("sessionDTO");		
		log.warning(" retrived session..."+ sessionMap);		
		if(sessionDTO !=null){
			log.warning("sessionDTO:"+sessionDTO.toString());
		}else{
			log.warning("sessionDTO:null");
		}
		if(sessionMap == null || sessionMap.isEmpty() || (sessionDTO==null || sessionDTO.getEmailId()==null )) {		
			log.warning(" session expired...sessionMap:"+sessionMap);
			
			setStatus("sessionExpired");
			return "sessionExpired";
		}else{
			log.warning(" session exist...");	
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
