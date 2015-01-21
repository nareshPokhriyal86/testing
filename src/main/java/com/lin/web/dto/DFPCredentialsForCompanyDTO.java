package com.lin.web.dto;

import java.io.Serializable;

public class DFPCredentialsForCompanyDTO implements Serializable{
	
	private String counter;
	private String DFP_Id;
	private String DFP_Username;
	private String DFP_Password;
	
	public DFPCredentialsForCompanyDTO(){
		
	}
	
	public DFPCredentialsForCompanyDTO(String counter, String dFP_Id,
			String dFP_Username, String dFP_Password) {
		this.counter = counter;
		DFP_Id = dFP_Id;
		DFP_Username = dFP_Username;
		DFP_Password = dFP_Password;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getCounter() {
		return counter;
	}
	
	public void setDFP_Id(String dFP_Id) {
		DFP_Id = dFP_Id;
	}

	public String getDFP_Id() {
		return DFP_Id;
	}

	public void setDFP_Username(String dFP_Username) {
		DFP_Username = dFP_Username;
	}

	public String getDFP_Username() {
		return DFP_Username;
	}

	public void setDFP_Password(String dFP_Password) {
		DFP_Password = dFP_Password;
	}

	public String getDFP_Password() {
		return DFP_Password;
	}

}
