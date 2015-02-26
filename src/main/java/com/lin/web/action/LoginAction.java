package com.lin.web.action;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.lin.web.dto.LoginDetailsDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.service.IUserService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.LinMobileConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class LoginAction extends ActionSupport implements
		ModelDriven<LoginDetailsDTO>,ServletRequestAware, SessionAware {
	
	private static final Logger log = Logger.getLogger(LoginAction.class.getName());	
	
	private HttpServletRequest request;
	private String loginStatus;
	private Map session;
	private SessionObjectDTO sessionDTO;
	LoginDetailsDTO login = new LoginDetailsDTO();
	private String channelToken;
	public String deploymentVersion = LinMobileConstants.DEPLOYMENT_VERSION;

	
	
	public String loginPage() {
		login.setLoginStatus("UnauthorisedGoogleAccount");
		return "success";
	}
	
	public String alreadyLoggedIn() {
		login.setLoginStatus("alreadyLoggedIn");
		return "success";
	}

	public String login() {
		log.info("Inside login() of loginAction");
		try {
			String googleEmailId = (String)request.getParameter("id");
			String r = (String)request.getParameter("r");
			IUserService service = (IUserService) BusinessServiceLocator.locate(IUserService.class);
			sessionDTO = (SessionObjectDTO) session.get("sessionDTO");
			if(sessionDTO!=null && sessionDTO.getUserId() > 0 && sessionDTO.getEmailId()!=null && sessionDTO.getRoleId()!=null){
				if(r != null && (r.trim().equals("f") || r.trim().equals("in"))) {
					return "alreadyLoggedIn"; // already in session but trying to login with inactive or another account using oauth
				}
				if(googleEmailId != null && !googleEmailId.trim().equalsIgnoreCase("") 
						&& !googleEmailId.trim().equalsIgnoreCase(sessionDTO.getEmailId().trim())) {
					return "alreadyLoggedIn"; // already in session but trying to login with another account using oauth
				}
				if(login.getEmailId() != null && !login.getEmailId().equalsIgnoreCase("") 
						&& !login.getEmailId().trim().equalsIgnoreCase(sessionDTO.getEmailId().trim())) {
					return "alreadyLoggedIn"; // already in session but trying to login with another account using application's own login
				}
				if(sessionDTO.getRoleId() != null && !sessionDTO.getRoleId().equalsIgnoreCase("")) {
					String returnVal = service.getFirstPageForLoginUser(sessionDTO.getRoleId().trim(), sessionDTO.getUserId(), login);
					sessionDTO.setPageName(returnVal);
					return returnVal;
				}
				else {
					login.setLoginStatus("noRole");
					log.warning("No role");
					return "error";
				}
			}else{
				if (sessionDTO == null) {
					sessionDTO = new SessionObjectDTO();
				}				
				Boolean isAuthenticate = false;
				
				if(r != null && r.trim().equals("f")) {
					login.setLoginStatus("UnauthorisedGoogleAccount");
					return "loginPage";
				}
				if(r != null && r.trim().equals("in")) {
					login.setLoginStatus("inactive");
					return "loginPage";
				}
				
				log.info("Going to login");
				if (login.getEmailId() != null && !login.getEmailId().equalsIgnoreCase("") && login.getPassword() != null && !login.getPassword().equalsIgnoreCase("")) {
					isAuthenticate = service.loginAuthentication(login);
				} 
				else if(googleEmailId != null && !googleEmailId.trim().equalsIgnoreCase("")) {
					log.info("Google EmailId : "+googleEmailId);
					isAuthenticate = service.googleLoginAuthentication(login, googleEmailId);
					if (!isAuthenticate) {
						return "loginPage";
					}
				}
				else {
					login.setLoginStatus("");
					return "loginPage";
				}
				
				if (!isAuthenticate) {
					//login.setLoginStatus("invalidUser");
					return "invalidUser";
				}
				
				if (login.getUserId() > 0) {
					sessionDTO.setUserId(login.getUserId());
				}
				if (login.getEmailId() != null) {
					sessionDTO.setEmailId(login.getEmailId().trim());
				}
				if (login.getRoleId() != null) {
					sessionDTO.setRoleId(login.getRoleId().trim());
				}
				if (login.getRoleName() != null) {
					sessionDTO.setRoleName(login.getRoleName().trim());
				}

				if(login.getUserName()!=null){
					sessionDTO.setUserName(login.getUserName().trim());
				}
				if(login.getCompanyLogoURL() != null && login.getCompanyLogoURL().length() > 0) {
					sessionDTO.setCompanyLogo(true);
					sessionDTO.setCompanyLogoURL(login.getCompanyLogoURL());
				}
				sessionDTO.setCompanyName(login.getCompanyName());
				sessionDTO.setAdminUser(login.isAdminUser());
				sessionDTO.setSuperAdmin(login.isSuperAdmin());
				sessionDTO.setClient(login.isClient());
				sessionDTO.setPublisherPoolPartner(login.isPublisherPoolPartner());
				
				session.put("sessionDTO", sessionDTO);
				createChannelToken();
				
				System.out.println(sessionDTO);
				
				if(sessionDTO.getRoleId() != null && !sessionDTO.getRoleId().equalsIgnoreCase("")) {
					String returnVal = service.getFirstPageForLoginUser(sessionDTO.getRoleId().trim(), sessionDTO.getUserId(), login);
					sessionDTO.setPageName(returnVal);
					return returnVal;
				}
				else {
					login.setLoginStatus("noRole");
					log.warning("No role");
					return "error";
				}
			}
		} catch (Exception e) {
		 
		 log.severe("Exception in login() of LoginAction : "+e.getMessage()); 
		 login.setLoginStatus(".....");
		 return "error";
		}
	}

	public String createChannel(){
		log.info("Create channel....");
    	createChannelToken();
		return Action.SUCCESS;
    }
    
    private void  createChannelToken(){
    	log.info("Create channel token for user..");
    	sessionDTO=(SessionObjectDTO) session.get("sessionDTO");		
    	if(sessionDTO !=null){
    		String channelKey=sessionDTO.getUserId()+"";
    		ChannelService channelService = ChannelServiceFactory.getChannelService();
    		channelToken = channelService.createChannel(channelKey);
    		log.info("Add token in sessionDTO: token:"+channelToken+" , for channelKey:"+channelKey);		
    		session.put("sessionDTO",sessionDTO);
    		session.put("token", channelToken);
    		
    		/*if(channelKey !=null && channelKey.trim().length()>0){
    			if(channelKeyList.indexOf(channelKey) < 0 ){
    	    		channelKeyList.add(channelKey);
    	    		log.info("ChannelKey added successfully.");
    	    	}		 
    			channelToken = channelService.createChannel(channelKey);
    			log.info("Add token in sessionDTO: token:"+channelToken);
    			
    			session.put("sessionDTO",sessionDTO);
    			session.put("token", channelToken);
    			session.put("channelKeyList", channelKeyList);
    			
    		}*/
    		log.info("createChannelToken ends...");
    	}else{
    		log.warning("No sessionDTO found..."+sessionDTO);
    	}			
    	  	
    }
    
    public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	
	
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String isLoginStatus() {
		return loginStatus;
	}

	@Override
	public LoginDetailsDTO getModel() {
		return login;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getChannelToken() {
		return channelToken;
	}

	public void setChannelToken(String channelToken) {
		this.channelToken = channelToken;
	}


}
