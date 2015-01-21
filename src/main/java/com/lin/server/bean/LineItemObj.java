package com.lin.server.bean;

import java.io.Serializable;

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Teams) to get LineItems of company's DFP
 */


import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class LineItemObj implements Serializable{

	@Id	private Long id;
	private long lineItemId;
	private String lineItemName;
	private long orderId;
	
	public LineItemObj(){
			
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	
}
