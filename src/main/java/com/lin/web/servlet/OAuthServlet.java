package com.lin.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;


public class OAuthServlet extends HttpServlet{

	 private static final long serialVersionUID = 1L;
	 private static final Logger log = Logger.getLogger(OAuthServlet.class.getName());
	 
	
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {		   
	    System.out.println("OAuthServlet servlet called....");
	    response.setContentType("text/plain");
	    try {
	      Entity credentials = null;
	      UserService userService = UserServiceFactory.getUserService();
	      User user = userService.getCurrentUser();
	      String user_email = "";
	      if (user != null) {
	        user_email = user.getEmail();
	      }
	      request.setAttribute("user_email", user_email);

	      /* Get saved server credentials from app engine datastore. */
	      DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	      Key creds_key = KeyFactory.createKey("DFPCredentials", "DFPCredentials");
	      credentials = ds.get(creds_key);
	      
	      log.info("found credential in datastore, going to adExReport..");
		  response.sendRedirect("/");
		  
	    } catch (EntityNotFoundException ex) {
	      log.warning("No credentials availble, create new...");      
	    
	      GoogleAuthorizationCodeRequestUrl requestUrl = 
	        new GoogleAuthorizationCodeRequestUrl(
	        		LinMobileVariables.AD_EXCHANGE_CLIENT_ID, 
	        		LinMobileVariables.AD_EXCHANGE_CALLBACK_URL, 
	        		Arrays.asList(LinMobileConstants.DFP_API_SCOPE,LinMobileConstants.AD_EXCHANGE_WEBSERVICE_SCOPE));
	      
	      requestUrl.setAccessType("offline");
	      requestUrl.setApprovalPrompt("force");
	      System.out.println("before sendRedirect:"+requestUrl.build());
	      response.sendRedirect(requestUrl.build());
	    }
	  
	    /*RequestDispatcher view = request.getRequestDispatcher("index.jsp");
	    view.forward(request, response);*/
	      
	 }
	 
		  @Override
		  protected void doPost(HttpServletRequest request, 
		                       HttpServletResponse response) throws 
		                         ServletException, IOException {
		    // Post logic is same as get (posts are used to switch selected model.
		    doGet(request, response);
		  }

		
		
     }