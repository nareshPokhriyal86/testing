package com.lin.web.servlet;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;



public class GoogleLoginIndexServlet extends HttpServlet{
	
	/*private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";*/
	public static final String CLIENT_ID = LinMobileVariables.GOOGLE_LOGIN_CLIENT_ID;
	public static final String CLIENT_SECRET = LinMobileVariables.GOOGLE_LOGIN_CLIENT_SECRET;
	public static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
	public static final String REDIRECT_URI = LinMobileVariables.GOOGLE_LOGIN_REDIRECT_URI;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
	
	GoogleAuthorizationCodeRequestUrl requestUrl = 
        new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, REDIRECT_URI, Arrays.asList(SCOPE));
	requestUrl.setAccessType("offline");
    requestUrl.setApprovalPrompt("force");
    response.sendRedirect(requestUrl.build());	     
	}
}