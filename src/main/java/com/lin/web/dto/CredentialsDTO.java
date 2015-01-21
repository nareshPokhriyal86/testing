package com.lin.web.dto;

import java.io.Serializable;
import java.util.Map;
import javax.persistence.Id;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class CredentialsDTO implements Serializable{
	@Id 
	private String id;	
	private String accessToken;
	private String refreshToken;
	private String clientId;
	private String clientSecret;
	private Long expiresInSeconds;
	private String scope;
	private String serviceAccountId;
	private String serviceAccountUser;
	
	
	
	public CredentialsDTO(){
		
	}
	
    public CredentialsDTO(String id,String accessToken,String refreshToken,String clientId,String clientSecret,
    		Long expiresInSeconds,String scope,String serviceAccountId,String serviceAccountUser){
		this.id=id;
		this.accessToken=accessToken;
		this.refreshToken=refreshToken;
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		this.expiresInSeconds=expiresInSeconds;
		this.scope=scope;
		this.serviceAccountId=serviceAccountId;
		this.serviceAccountUser=serviceAccountUser;
		
	}
    
    public CredentialsDTO(String accessToken,String refreshToken,String clientId,String clientSecret,
    		Long expiresInSeconds,String scope,String serviceAccountId,String serviceAccountUser){		
		this.accessToken=accessToken;
		this.refreshToken=refreshToken;
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		this.expiresInSeconds=expiresInSeconds;
		this.scope=scope;
		this.serviceAccountId=serviceAccountId;
		this.serviceAccountUser=serviceAccountUser;
		
	}

	public void setId(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	
	public void setExpiresInSeconds(Long expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}

	public Long getExpiresInSeconds() {
		return expiresInSeconds;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}

	public void setServiceAccountId(String serviceAccountId) {
		this.serviceAccountId = serviceAccountId;
	}

	public String getServiceAccountId() {
		return serviceAccountId;
	}

	public void setServiceAccountUser(String serviceAccountUser) {
		this.serviceAccountUser = serviceAccountUser;
	}

	public String getServiceAccountUser() {
		return serviceAccountUser;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientSecret() {
		return clientSecret;
	}

		

}
