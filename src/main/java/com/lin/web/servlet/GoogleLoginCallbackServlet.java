package com.lin.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.bean.AnonymousUserDetailsObj;
import com.lin.server.bean.UserDetailsObj;


public class GoogleLoginCallbackServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());
	
	private String loginStatus;
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {	
		
		String error = request.getParameter("error");
		if(error != null && error.trim().equalsIgnoreCase("access_denied")) {
			response.sendRedirect("/loginPage.lin");
		}
		String code = request.getParameterValues("code")[0];
		log.info("Inside GoogleLoginCallbackServlet, code : "+code);
	    HttpTransport transport = new NetHttpTransport();
	    JacksonFactory jsonFactory = new JacksonFactory();
	    
        GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeFlow(
	        transport, 
	        jsonFactory, 
	        GoogleLoginIndexServlet.CLIENT_ID, 
	        GoogleLoginIndexServlet.CLIENT_SECRET,
	        Arrays.asList(GoogleLoginIndexServlet.SCOPE)
	      )  
	      .newTokenRequest(code)
	      .setRedirectUri(GoogleLoginIndexServlet.REDIRECT_URI);

       
        GoogleTokenResponse token = tokenRequest.execute();
        String tokenValue = token.getAccessToken();
        log.info("Inside GoogleLoginCallbackServlet, tokenValue : "+tokenValue);
        String googleEmailId = "";
        UserDetailsObj userDetailsObj = null;
        try {   
        	log("token : "+tokenValue);
        	JSONObject json = readJsonFromUrl("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+tokenValue.trim());
            googleEmailId = json.get("email").toString();
            System.out.println(json.get("email").toString());
            IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
        	userDetailsObj = userDetailsDAO.getUserByEmailId(googleEmailId);
        	if(userDetailsObj != null && userDetailsObj.getEmailId() != null && (userDetailsObj.getStatus().equalsIgnoreCase("Active") || userDetailsObj.getStatus().equalsIgnoreCase("Pending")) && userDetailsObj.getEmailId().trim().equalsIgnoreCase(googleEmailId.trim())) {
        		if(userDetailsObj.getStatus().equalsIgnoreCase("Pending")) {
        			userDetailsObj.setStatus("Active");
        			userDetailsDAO.saveObject(userDetailsObj);
        		}
        		String emailId = userDetailsObj.getEmailId();
        		response.sendRedirect("/login.lin?id="+emailId);
        	}
        	else {
        		if(json.get("email") != null && !(json.get("email").toString().equalsIgnoreCase(""))) {
	        		String email = json.get("email").toString().trim();
	        		AnonymousUserDetailsObj anonymousUserDetailsObj = userDetailsDAO.getAnonymousUserByEmailId(email);
	        		if(anonymousUserDetailsObj == null) {
	        			anonymousUserDetailsObj = new AnonymousUserDetailsObj();
			        	anonymousUserDetailsObj.setEmail(json.get("email").toString());
			        	try {
			        	if(json.get("id")!= null &&!(json.get("id").toString().equalsIgnoreCase(""))) {
				        		anonymousUserDetailsObj.setGoogle_id(json.get("id").toString());
			        	}}catch (Exception e) {anonymousUserDetailsObj.setGoogle_id("");}
			        	try {
		        		if(json.get("verified_email")!= null &&!(json.get("verified_email").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setVerified_email(json.get("verified_email").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setVerified_email("");}
			        	try {
		        		if(json.get("name")!= null &&!(json.get("name").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setName(json.get("name").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setName("");}
			        	try {
		        		if(json.get("given_name")!= null &&!(json.get("given_name").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setGiven_name(json.get("given_name").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setGiven_name("");}
			        	try {
		        		if(json.get("family_name")!= null &&!(json.get("family_name").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setFamily_name(json.get("family_name").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setFamily_name("");}
			        	try {
		        		if(json.get("link")!= null &&!(json.get("link").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setLink(json.get("link").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setLink("");}
			        	try {
		        		if(json.get("gender")!= null &&!(json.get("gender").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setGender(json.get("gender").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setGender("");}
			        	try {
		        		if(json.get("birthday")!= null &&!(json.get("birthday").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setBirthday(json.get("birthday").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setBirthday("");}
			        	try {
		        		if(json.get("locale")!= null &&!(json.get("locale").toString().equalsIgnoreCase(""))) {
			        		anonymousUserDetailsObj.setLocale(json.get("locale").toString());
		        		}}catch (Exception e) {anonymousUserDetailsObj.setLocale("");}
		        		anonymousUserDetailsObj.setLastAccessTime(new Date().toString());
		        		anonymousUserDetailsObj.setAccessCount(1);
		        		userDetailsDAO.saveObject(anonymousUserDetailsObj);
	        		}
	        		else if(anonymousUserDetailsObj != null && anonymousUserDetailsObj.getObjectId() > 0){
	        			long count = anonymousUserDetailsObj.getAccessCount();
	        			anonymousUserDetailsObj.setAccessCount(count+1);
	        			anonymousUserDetailsObj.setLastAccessTime(new Date().toString());
	        			userDetailsDAO.saveObject(anonymousUserDetailsObj);
	        		}
        		}
        		if(userDetailsObj != null && userDetailsObj.getEmailId() != null && userDetailsObj.getStatus().equalsIgnoreCase("Inactive") && userDetailsObj.getEmailId().trim().equalsIgnoreCase(googleEmailId.trim())) {
        			response.sendRedirect("/login.lin?r=in");
        		}
        		else {
        		response.sendRedirect("/login.lin?r=f");
        		}
            }
        }catch(Exception e) {
        	 
        	log("Exception found in GoogleLoginCallbackServlet.doGet");
        	response.sendRedirect("/loginPage.lin"); 
        }   
	}
 /*
 {
 "id": "114737857096651129752",
 "email": "nareshpokhriyal86@gmail.com",
 "verified_email": true,
 "name": "naresh pokhriyal",
 "given_name": "naresh",
 "family_name": "pokhriyal",
 "link": "https://plus.google.com/114737857096651129752",
 "gender": "male",
 "birthday": "0000-11-27",
 "locale": "en"
}
 */

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getLoginStatus() {
		return loginStatus;
	} 
	
	
}