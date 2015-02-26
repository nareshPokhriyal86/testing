package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Accounts) to save advertisers/agencies of company's DFP
 */
@Index
public class DFPTaskEntity implements Serializable{
	 public static final String STATUS_PENDING = "pending";
	 public static final String STATUS_IN_PROGRESS = "in-progress";
	 public static final String STATUS_COMPLETED = "completed";
	 public static final String STATUS_FAILED = "failed";
	 
	 public static final String STATUS_PENDING_REPORT = "pendingReport";
	 public static final String STATUS_FINISHED_REPORT = "finishedReport";
	 public static final String STATUS_PENDING_DOWNLOAD = "pendingDownload";
	 public static final String STATUS_FINISHED_DOWNLOAD = "finishedDownload";
	 public static final String STATUS_PENDING_RAW = "pendingRaw";
	 public static final String STATUS_FINISHED_RAW = "finishedRaw";
	 public static final String STATUS_PENDING_PROC = "pendingProc";
	 public static final String STATUS_FINISHED_PROC = "finishedProc";
	 
	@Id 
	private Long id;
	private String taskName;
	private String networkCode;
	private String startDate;
	private String endDate;
	private String taskGroupKey;
	private String status ;
	private String rawTableId;
	private String csRawFilePath;
	private String procTableId;
	private String dfpFileUrl;
	private String loadType;
	private String errorDesc;
	private String orderId;
	private Date startTime = new Date();
	private Date lastModifiedTime = new Date();
	private String taskType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getNetworkCode() {
		return networkCode;
	}
	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
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
	public String getTaskGroupKey() {
		return taskGroupKey;
	}
	public void setTaskGroupKey(String taskGroupKey) {
		this.taskGroupKey = taskGroupKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRawTableId() {
		return rawTableId;
	}
	public void setRawTableId(String rawTableId) {
		this.rawTableId = rawTableId;
	}
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public static String getStatusPending() {
		return STATUS_PENDING;
	}
	public static String getStatusInProgress() {
		return STATUS_IN_PROGRESS;
	}
	public static String getStatusCompleted() {
		return STATUS_COMPLETED;
	}
	public static String getStatusFailed() {
		return STATUS_FAILED;
	}
	
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getCsRawFilePath() {
		return csRawFilePath;
	}
	public void setCsRawFilePath(String csRawFilePath) {
		this.csRawFilePath = csRawFilePath;
	}
	public String getProcTableId() {
		return procTableId;
	}
	public void setProcTableId(String procTableId) {
		this.procTableId = procTableId;
	}
	public String getDfpFileUrl() {
		return dfpFileUrl;
	}
	public void setDfpFileUrl(String dfpFileUrl) {
		this.dfpFileUrl = dfpFileUrl;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public DFPTaskEntity(String taskName, String networkCode, String startDate, String endDate, String taskGroupKey, String loadType) {
		super();
		this.taskName = taskName;
		this.networkCode = networkCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskGroupKey = taskGroupKey;
		this.loadType = loadType;
	}
	public DFPTaskEntity(){}
	public DFPTaskEntity(String taskName, String networkCode, String startDate,
			String endDate, String taskGroupKey, String status,
			String loadType, Date startTime, Date lastModifiedTime, String orderId) {
		super();
		this.taskName = taskName;
		this.networkCode = networkCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskGroupKey = taskGroupKey;
		this.status = status;
		this.loadType = loadType;
		this.startTime = startTime;
		this.lastModifiedTime = lastModifiedTime;
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "DFPTaskEntity [id=" + id + ", taskName=" + taskName
				+ ", networkCode=" + networkCode + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", taskGroupKey=" + taskGroupKey
				+ ", status=" + status + ", rawTableId=" + rawTableId
				+ ", loadType=" + loadType + ", startTime=" + startTime
				+ ", lastModifiedTime=" + lastModifiedTime 
				+ ",taskType=" + taskType + "]";
	}
	
	
	
}
