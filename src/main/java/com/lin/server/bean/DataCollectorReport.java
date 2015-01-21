package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@SuppressWarnings("serial")
@Entity
@Index
public class DataCollectorReport implements Serializable{

	@Id	private String id;
	private String reportId;
	private String reportName;
	private String dataSource;
	private String reportPath;
	private String createdOn;
	private String downloadStatus;
	private String downloadURL;
	private String updatedOn;
	private String processedStatus;
	private String reportType;
	private String startDate;
	private String endDate;
	private String dirName;
	private String status;
	private String errorLog;
	
	public DataCollectorReport(){
		
	}
	
	
	public DataCollectorReport(String id, String reportId, String reportName,
			String dataSource, String reportPath, String createdOn,
			String downloadStatus, String downloadURL, String updatedOn,
			String processedStatus, String reportType, String startDate,
			String endDate, String dirName) {
		this.id = id;
		this.reportId = reportId;
		this.reportName = reportName;
		this.dataSource = dataSource;
		this.reportPath = reportPath;
		this.createdOn = createdOn;
		this.downloadStatus = downloadStatus;
		this.downloadURL = downloadURL;
		this.updatedOn = updatedOn;
		this.processedStatus = processedStatus;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dirName = dirName;
	}

	public DataCollectorReport(String reportId, String reportName,
			String dataSource, String reportPath, String createdOn,
			String downloadStatus, String downloadURL, String updatedOn,
			String processedStatus, String reportType, String startDate,
			String endDate, String dirName) {
		
		this.reportId = reportId;
		this.reportName = reportName;
		this.dataSource = dataSource;
		this.reportPath = reportPath;
		this.createdOn = createdOn;
		this.downloadStatus = downloadStatus;
		this.downloadURL = downloadURL;
		this.updatedOn = updatedOn;
		this.processedStatus = processedStatus;
		this.reportType = reportType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dirName = dirName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getProcessedStatus() {
		return processedStatus;
	}
	public void setProcessedStatus(String processedStatus) {
		this.processedStatus = processedStatus;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportName() {
		return reportName;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReportType() {
		return reportType;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartDate() {
		return startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setDirName(String dirName) {
		this.dirName = dirName;
	}


	public String getDirName() {
		return dirName;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
	}


	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}


	public String getErrorLog() {
		return errorLog;
	}
	
	
}
