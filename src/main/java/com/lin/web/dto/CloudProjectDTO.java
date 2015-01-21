package com.lin.web.dto;

import java.io.Serializable;

/*
 * This DTO will keep information for cloud project
 * @author Youdhveer Panwar
 */
@SuppressWarnings("serial")
public class CloudProjectDTO implements Serializable{
	
	private String companyId;
	private String bigQueryProjectId;
	private String bigQueryServiceAccountEmail;
	private String bigQueryServicePrivateKey;	
	private String cloudStorageBucket;
	private String bigQueryDataSet;
	
	public CloudProjectDTO(){
		
	}
	
	public CloudProjectDTO(String companyId, String bigQueryProjectId,
			String bigQueryServiceAccountEmail,
			String bigQueryServicePrivateKey, String cloudStorageBucket,String bigQueryDataSet) {
		this.companyId = companyId;
		this.bigQueryProjectId = bigQueryProjectId;
		this.bigQueryServiceAccountEmail = bigQueryServiceAccountEmail;
		this.bigQueryServicePrivateKey = bigQueryServicePrivateKey;
		this.cloudStorageBucket = cloudStorageBucket;
		this.bigQueryDataSet=bigQueryDataSet;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CloudProjectDTO [companyId=");
		builder.append(companyId);
		builder.append(", bigQueryProjectId=");
		builder.append(bigQueryProjectId);
		builder.append(", bigQueryServiceAccountEmail=");
		builder.append(bigQueryServiceAccountEmail);
		builder.append(", bigQueryServicePrivateKey=");
		builder.append(bigQueryServicePrivateKey);
		builder.append(", cloudStorageBucket=");
		builder.append(cloudStorageBucket);
		builder.append(", bigQueryDataSet=");
		builder.append(bigQueryDataSet);
		builder.append("]");
		return builder.toString();
	}

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getBigQueryProjectId() {
		return bigQueryProjectId;
	}
	public void setBigQueryProjectId(String bigQueryProjectId) {
		this.bigQueryProjectId = bigQueryProjectId;
	}
	public String getBigQueryServiceAccountEmail() {
		return bigQueryServiceAccountEmail;
	}
	public void setBigQueryServiceAccountEmail(String bigQueryServiceAccountEmail) {
		this.bigQueryServiceAccountEmail = bigQueryServiceAccountEmail;
	}
	public String getBigQueryServicePrivateKey() {
		return bigQueryServicePrivateKey;
	}
	public void setBigQueryServicePrivateKey(String bigQueryServicePrivateKey) {
		this.bigQueryServicePrivateKey = bigQueryServicePrivateKey;
	}
	public String getCloudStorageBucket() {
		return cloudStorageBucket;
	}
	public void setCloudStorageBucket(String cloudStorageBucket) {
		this.cloudStorageBucket = cloudStorageBucket;
	}

	public String getBigQueryDataSet() {
		return bigQueryDataSet;
	}

	public void setBigQueryDataSet(String bigQueryDataSet) {
		this.bigQueryDataSet = bigQueryDataSet;
	}
	
	 

}
