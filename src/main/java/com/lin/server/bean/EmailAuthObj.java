package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class EmailAuthObj implements Serializable{

	@Id	private Long id;
	private long userId;
	private String randomNumber;
	private String status;			// authorised OR unAuthorised
	
	public EmailAuthObj(){
		
	}	

	public EmailAuthObj(long userId, String randomNumber, String status) {
		this.userId = userId;
		this.randomNumber = randomNumber;
		this.status = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	
		
}
