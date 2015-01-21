package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/*
 * It is 
 * 
 * @author Shubham Goel
 */
@Index
public class DailyDataProcessObj implements Serializable{
	
	@Id
	private String id;
	private String  processKey;
	private int taskCount;
	
	
	public DailyDataProcessObj() {
	}
	
	
	public DailyDataProcessObj(String id, String processKey, int taskCount) {
		super();
		this.id = id;
		this.processKey = processKey;
		this.taskCount = taskCount;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	
}
