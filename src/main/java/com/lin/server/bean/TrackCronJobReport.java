package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Youdhveer Panwar (youdhveer.panwar@mediaagility.com)
 * 
 * This datastore entity will keep track of cron job that runs for given date interval
 */

@SuppressWarnings("serial")
@Index
public class TrackCronJobReport implements Serializable{

	@Id	private String id;
	private String reportId;	
	private String startDate;
	private String endDate;
	private String errorLog;
	private String reportType;
	
	public TrackCronJobReport(){
		
	}

	
	
	public TrackCronJobReport(String id, String reportId, 
			String startDate, String endDate, String errorLog,String reportType) {
		this.id = id;
		this.reportId = reportId;	
		this.startDate = startDate;
		this.endDate = endDate;
		this.errorLog = errorLog;
		this.reportType=reportType;
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
	

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}


	public String getErrorLog() {
		return errorLog;
	}



	public void setReportType(String reportType) {
		this.reportType = reportType;
	}



	public String getReportType() {
		return reportType;
	}
	
	
}
