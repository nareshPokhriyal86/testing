package com.lin.web.dto;

import java.io.Serializable;

import javax.persistence.Id;

public class TokenResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	private String id;	
	private String accessToken;
	private String refreshToken;
	private String clientId;
	private String clientSecret;
	private Long expiresInSeconds;
	private String scope;
	private String tokenType;
	private String idToken;
	
	
	public TokenResponseDTO(){
		
	}
	
    public TokenResponseDTO(String id,String accessToken,String refreshToken,String clientId,String clientSecret,
    		Long expiresInSeconds,String scope,String tokenType,String idToken){
		this.id=id;
		this.accessToken=accessToken;
		this.refreshToken=refreshToken;
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		this.expiresInSeconds=expiresInSeconds;
		this.scope=scope;
		this.setTokenType(tokenType);
		this.setIdToken(idToken);
		
	}
    
    public TokenResponseDTO(String accessToken,String refreshToken,String clientId,String clientSecret,
    		Long expiresInSeconds,String scope,String tokenType,String idToken){		
		this.accessToken=accessToken;
		this.refreshToken=refreshToken;
		this.clientId=clientId;
		this.clientSecret=clientSecret;
		this.expiresInSeconds=expiresInSeconds;
		this.scope=scope;
		this.setTokenType(tokenType);
		this.setIdToken(idToken);
		
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

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getIdToken() {
		return idToken;
	}

		

}
