package com.lin.web.dto;

import java.io.Serializable;

import javax.persistence.Id;

public class QueryDTO implements Serializable{
	@Id 
	private String id;	
	private String serviceAccountEmail;
	private String servicePrivateKey;
	private String bigQueryProjectId;
	private String bigQuerySchemaFileName;
	private String bigQueryTableId;
	private String bigQueryDataSetId;
	private String bigQuerySchemaName;
	private String queryData;	
	
	
	public QueryDTO(){
		
	}
	
   
	public QueryDTO(String serviceAccountEmail,
			String servicePrivateKey, String bigQueryProjectId,
			String bigQuerySchemaFileName, String bigQueryTableId,
			String bigQueryDataSetId, String bigQuerySchemaName) {		
		this.serviceAccountEmail = serviceAccountEmail;
		this.servicePrivateKey = servicePrivateKey;
		this.bigQueryProjectId = bigQueryProjectId;
		this.bigQuerySchemaFileName = bigQuerySchemaFileName;
		this.bigQueryTableId = bigQueryTableId;
		this.bigQueryDataSetId = bigQueryDataSetId;
		this.bigQuerySchemaName = bigQuerySchemaName;
	}


	public QueryDTO(String id,String serviceAccountEmail, String servicePrivateKey,
			String bigQueryProjectId, String bigQuerySchemaFileName,
			String bigQueryTableId, String bigQueryDataSetId,
			String bigQuerySchemaName, String queryData) {
		this.id=id;
		this.serviceAccountEmail = serviceAccountEmail;
		this.servicePrivateKey = servicePrivateKey;
		this.bigQueryProjectId = bigQueryProjectId;
		this.bigQuerySchemaFileName = bigQuerySchemaFileName;
		this.bigQueryTableId = bigQueryTableId;
		this.bigQueryDataSetId = bigQueryDataSetId;
		this.bigQuerySchemaName = bigQuerySchemaName;
		this.queryData = queryData;
	}


	public QueryDTO(String serviceAccountEmail, String servicePrivateKey,
			String bigQueryProjectId,String bigQueryDataSetId,String queryData) {
	
		this.serviceAccountEmail = serviceAccountEmail;
		this.servicePrivateKey = servicePrivateKey;
		this.bigQueryProjectId = bigQueryProjectId;	
		this.bigQueryDataSetId = bigQueryDataSetId;
		this.queryData = queryData;
	}
	
	
	
	@Override
	public String toString() {
		return "QueryDTO [id=" + id + ", serviceAccountEmail="
				+ serviceAccountEmail + ", servicePrivateKey="
				+ servicePrivateKey + ", bigQueryProjectId="
				+ bigQueryProjectId + ", bigQuerySchemaFileName="
				+ bigQuerySchemaFileName + ", bigQueryTableId="
				+ bigQueryTableId + ", bigQueryDataSetId=" + bigQueryDataSetId
				+ ", bigQuerySchemaName=" + bigQuerySchemaName + ", queryData="
				+ queryData + "]";
	}


	public void setId(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}


	public String getServiceAccountEmail() {
		return serviceAccountEmail;
	}


	public void setServiceAccountEmail(String serviceAccountEmail) {
		this.serviceAccountEmail = serviceAccountEmail;
	}


	public String getServicePrivateKey() {
		return servicePrivateKey;
	}


	public void setServicePrivateKey(String servicePrivateKey) {
		this.servicePrivateKey = servicePrivateKey;
	}


	public String getBigQueryProjectId() {
		return bigQueryProjectId;
	}


	public void setBigQueryProjectId(String bigQueryProjectId) {
		this.bigQueryProjectId = bigQueryProjectId;
	}


	public String getBigQuerySchemaFileName() {
		return bigQuerySchemaFileName;
	}


	public void setBigQuerySchemaFileName(String bigQuerySchemaFileName) {
		this.bigQuerySchemaFileName = bigQuerySchemaFileName;
	}


	public String getBigQueryTableId() {
		return bigQueryTableId;
	}


	public void setBigQueryTableId(String bigQueryTableId) {
		this.bigQueryTableId = bigQueryTableId;
	}


	public String getBigQueryDataSetId() {
		return bigQueryDataSetId;
	}


	public void setBigQueryDataSetId(String bigQueryDataSetId) {
		this.bigQueryDataSetId = bigQueryDataSetId;
	}


	public String getBigQuerySchemaName() {
		return bigQuerySchemaName;
	}


	public void setBigQuerySchemaName(String bigQuerySchemaName) {
		this.bigQuerySchemaName = bigQuerySchemaName;
	}


	public String getQueryData() {
		return queryData;
	}


	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

		

}
