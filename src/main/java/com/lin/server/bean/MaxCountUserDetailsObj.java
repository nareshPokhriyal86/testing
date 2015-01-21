package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class MaxCountUserDetailsObj implements Serializable{

	@Id private Long id;
	private long maxCount;
	
	public MaxCountUserDetailsObj(){
		
	}

	

	public MaxCountUserDetailsObj(long maxCount) {
		this.maxCount = maxCount;
	}



	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getId() {
		return id;
	}
	
		
}
