package com.lin.web.dto;

import com.lin.dfp.api.impl.DFPCompanyEnum;

/*
 * @author Youdhveer Panwar
 * 
 * This DTO is used to setup Accounts(Advertiser/Agency) at DFP
 */
public class DFPCompanyDTO {

	private long id;	
	private String name;	
	private String address;
	private String state;
	private String phone;
	private String email;
	private String fax;
	private String dfpNetworkCode;
	private DFPCompanyEnum type;
	
	public DFPCompanyDTO(){
		
	}

	
	public DFPCompanyDTO(long id, String name, String address, String state,
			String phone, String email, String fax, String dfpNetworkCode,
			DFPCompanyEnum type) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.state = state;
		this.phone = phone;
		this.email = email;
		this.fax = fax;
		this.dfpNetworkCode = dfpNetworkCode;
		this.type = type;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DFPCompanyDTO [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", address=");
		builder.append(address);
		builder.append(", state=");
		builder.append(state);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", email=");
		builder.append(email);
		builder.append(", fax=");
		builder.append(fax);
		builder.append(", dfpNetworkCode=");
		builder.append(dfpNetworkCode);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}

	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}

	public DFPCompanyEnum getType() {
		return type;
	}

	public void setType(DFPCompanyEnum type) {
		this.type = type;
	}


		
	
}
