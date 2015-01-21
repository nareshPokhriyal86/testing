package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

public class AdServerCredentialsDTO implements Serializable{
	
	private String counter;
	private String adServerId;
	private String adServerUsername;
	private String adServerPassword;
	
	private String adServerIdUsername;
	private String adServerIdUsernamePassword;
	private String customizedValue;
	
	public AdServerCredentialsDTO(){
		
	}
	
	public AdServerCredentialsDTO(String adServerId, String adServerUsername) {
		this.adServerId = adServerId;
		this.adServerUsername = adServerUsername;
	}
	
	public AdServerCredentialsDTO(String adServerId, String adServerUsername, String adServerPassword) {
		this.adServerId = adServerId;
		this.adServerUsername = adServerUsername;
		this.adServerPassword = adServerPassword;
	}

	public AdServerCredentialsDTO(String counter, String adServerId, String adServerUsername, String adServerPassword) {
		this.counter = counter;
		this.adServerId = adServerId;
		this.adServerUsername = adServerUsername;
		this.adServerPassword = adServerPassword;
	}
	
	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public String getAdServerUsername() {
		return adServerUsername;
	}

	public void setAdServerUsername(String adServerUsername) {
		this.adServerUsername = adServerUsername;
	}

	public String getAdServerPassword() {
		return adServerPassword;
	}

	public void setAdServerPassword(String adServerPassword) {
		this.adServerPassword = adServerPassword;
	}

	public void setAdServerIdUsername(String adServerIdUsername) {
		this.adServerIdUsername = adServerIdUsername;
	}

	public String getAdServerIdUsername() {
		return adServerIdUsername;
	}

	public void setCustomizedValue(String customizedValue) {
		this.customizedValue = customizedValue;
	}

	public String getCustomizedValue() {
		return customizedValue;
	}

	public void setAdServerIdUsernamePassword(String adServerIdUsernamePassword) {
		this.adServerIdUsernamePassword = adServerIdUsernamePassword;
	}

	public String getAdServerIdUsernamePassword() {
		return adServerIdUsernamePassword;
	}
	
	

}
