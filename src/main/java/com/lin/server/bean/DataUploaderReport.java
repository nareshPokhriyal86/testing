package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class DataUploaderReport implements Serializable{

	@Id	private String id;
	private String reportId;
	private String reportName;
	private String dataSource;
	private String reportPath;
	private String updatedOn;	
	private String uploadBQStatus;
	private String reportType;
	private String startDate;
	private String endDate;
	private String dirName;
	private String status;
	private String errorLog;
	
	public DataUploaderReport(){
		
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
	
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportName() {
		return reportName;
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


	public void setUploadBQStatus(String uploadBQStatus) {
		this.uploadBQStatus = uploadBQStatus;
	}


	public String getUploadBQStatus() {
		return uploadBQStatus;
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
