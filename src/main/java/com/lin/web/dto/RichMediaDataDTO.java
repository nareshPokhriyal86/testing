package com.lin.web.dto;

import java.io.Serializable;

public class RichMediaDataDTO implements Serializable{
	
	private String cutomEvent;
	private String customEventType;
	private String creativeSize;
	private String counter;
	
	public RichMediaDataDTO() {
	}

	public RichMediaDataDTO(String cutomEvent, String customEventType,
			String creativeSize, String counter) {
		super();
		this.cutomEvent = cutomEvent;
		this.customEventType = customEventType;
		this.creativeSize = creativeSize;
		this.counter = counter;
	}

	public String getCutomEvent() {
		return cutomEvent;
	}

	public void setCutomEvent(String cutomEvent) {
		this.cutomEvent = cutomEvent;
	}

	public String getCustomEventType() {
		return customEventType;
	}

	public void setCustomEventType(String customEventType) {
		this.customEventType = customEventType;
	}

	public String getCreativeSize() {
		return creativeSize;
	}

	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	
	
	
	
}
