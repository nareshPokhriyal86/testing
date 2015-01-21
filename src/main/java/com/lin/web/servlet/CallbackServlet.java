package com.lin.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileVariables;

public class CallbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CallbackServlet.class
			.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Callback servlet called....");
		response.setContentType("text/plain");
		String code = request.getParameter("code");
		log.info("Callback servlet: authCode:" + code);
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		// Exchange auth code for access and refresh tokens
		GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeFlow(
				transport, jsonFactory, LinMobileVariables.AD_EXCHANGE_CLIENT_ID,
				LinMobileVariables.AD_EXCHANGE_CLIENT_SECRET,
				Arrays.asList(LinMobileConstants.DFP_API_SCOPE,LinMobileConstants.AD_EXCHANGE_WEBSERVICE_SCOPE)).newTokenRequest(code)
				.setRedirectUri(LinMobileVariables.AD_EXCHANGE_CALLBACK_URL);

		GoogleTokenResponse tokens = tokenRequest.execute();

		// Populate credentials object and store in app engine datastore.
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity credentials = new Entity("DFPCredentials",
				"DFPCredentials");
		credentials.setProperty("accessToken", tokens.getAccessToken());
		credentials.setProperty("expiresIn", tokens.getExpiresInSeconds());
		credentials.setProperty("refreshToken", tokens.getRefreshToken());
		credentials.setProperty("clientId", LinMobileVariables.AD_EXCHANGE_CLIENT_ID);
		credentials.setProperty("clientSecret",
				LinMobileVariables.AD_EXCHANGE_CLIENT_SECRET);
		datastore.put(credentials);
		//response.sendRedirect("/index.jsp");
		 response.sendRedirect("/");
	}
}