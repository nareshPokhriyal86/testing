package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Youdhveer Panwar
 * This entity will store all finalised and non-finalised table information
 */

@SuppressWarnings("serial")
@Index
public class FinalisedTableDetailsObj implements Serializable {
	
	@Id	private String id;		
	private String tableId;
	private int tableType;  //0 for non finalised and 1 for finalised
	private String startDate;
	private String endDate; 
	private String lastUpdatedOn;
	private int publisherId;
	private String dataSource;
	private String processedFilePath;
	private String bigQueryProjectId;
	private String bigQueryDataSet;
	private int mergeStatus;
	
	public FinalisedTableDetailsObj(){
	}	

	

	public FinalisedTableDetailsObj(String id, String tableId, int tableType,
			String startDate, String endDate, String lastUpdatedOn,
			int publisherId, String dataSource, String processedFilePath,
			String bigQueryProjectId, String bigQueryDataSet, int mergeStatus) {
		this.id = id;
		this.tableId = tableId;
		this.tableType = tableType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lastUpdatedOn = lastUpdatedOn;
		this.publisherId = publisherId;
		this.dataSource = dataSource;
		this.processedFilePath = processedFilePath;
		this.bigQueryProjectId = bigQueryProjectId;
		this.bigQueryDataSet = bigQueryDataSet;
		this.mergeStatus = mergeStatus;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinalisedTableDetailsObj [id=");
		builder.append(id);
		builder.append(", tableId=");
		builder.append(tableId);
		builder.append(", tableType=");
		builder.append(tableType);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", lastUpdatedOn=");
		builder.append(lastUpdatedOn);
		builder.append(", publisherId=");
		builder.append(publisherId);
		builder.append(", dataSource=");
		builder.append(dataSource);
		builder.append(", processedFilePath=");
		builder.append(processedFilePath);
		builder.append(", bigQueryProjectId=");
		builder.append(bigQueryProjectId);
		builder.append(", bigQueryDataSet=");
		builder.append(bigQueryDataSet);
		builder.append(", mergeStatus=");
		builder.append(mergeStatus);
		builder.append("]");
		return builder.toString();
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public int getTableType() {
		return tableType;
	}

	public void setTableType(int tableType) {
		this.tableType = tableType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}


	public int getPublisherId() {
		return publisherId;
	}


	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public String getProcessedFilePath() {
		return processedFilePath;
	}


	public void setProcessedFilePath(String processedFilePath) {
		this.processedFilePath = processedFilePath;
	}


	public String getBigQueryProjectId() {
		return bigQueryProjectId;
	}


	public void setBigQueryProjectId(String bigQueryProjectId) {
		this.bigQueryProjectId = bigQueryProjectId;
	}


	public String getBigQueryDataSet() {
		return bigQueryDataSet;
	}


	public void setBigQueryDataSet(String bigQueryDataSet) {
		this.bigQueryDataSet = bigQueryDataSet;
	}

	public int getMergeStatus() {
		return mergeStatus;
	}

	public void setMergeStatus(int mergeStatus) {
		this.mergeStatus = mergeStatus;
	}

		
	
	
}
