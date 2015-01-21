package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/*
 * Processed  file object for datastore
 * It will keep the track of all the uploaded Data files on Cloud Storage
 * @author Shubham Goel
 */
@Index
public class ProcessFileObj implements Serializable {
	
	@Id
	private Long id;
	private List<String> cloudStoragFilePathList;
	private String bqProjectId;
	private int uploadState;
	private Date creationTime;
	private Date uploadTime;
	private String mapKey;
	
	public ProcessFileObj() {
	}

	public ProcessFileObj(List<String> cloudStoragFilePathList, String bqProjectId,
			int uploadState, Date creationTime, Date uploadTime, String mapKey) {
		super();
		this.setCloudStoragFilePathList(cloudStoragFilePathList);
		this.bqProjectId = bqProjectId;
		this.uploadState = uploadState;
		this.creationTime = creationTime;
		this.uploadTime = uploadTime;
		this.mapKey = mapKey;
	}

	

	public String getBqProjectId() {
		return bqProjectId;
	}

	public void setBqProjectId(String bqProjectId) {
		this.bqProjectId = bqProjectId;
	}

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getMapKey() {
		return mapKey;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}

	public List<String> getCloudStoragFilePathList() {
		return cloudStoragFilePathList;
	}

	public void setCloudStoragFilePathList(List<String> cloudStoragFilePathList) {
		this.cloudStoragFilePathList = cloudStoragFilePathList;
	}
	
	
	
	
	

}
