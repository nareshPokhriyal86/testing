package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Youdhveer Panwar
 * This entity is used to store all orderIds in datastore
 * with respect to DFP Network
 *  
 */
@Index
@SuppressWarnings("serial")
public class DfpOrderIdsObj implements Serializable {
	@Id	private String id;
	private long orderId;
	private String dfpNetworkCode;
	private String dfpNetworkName;
	
	public DfpOrderIdsObj(){
		
	}
	public DfpOrderIdsObj(String id, long orderId, String dfpNetworkCode,
			String dfpNetworkName) {
		this.id = id;
		this.orderId = orderId;
		this.dfpNetworkCode = dfpNetworkCode;
		this.dfpNetworkName = dfpNetworkName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DfpOrderIdsObj [id=").append(id).append(", orderId=")
				.append(orderId).append(", dfpNetworkCode=")
				.append(dfpNetworkCode).append(", dfpNetworkName=")
				.append(dfpNetworkName).append("]");
		return builder.toString();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}
	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}
	public String getDfpNetworkName() {
		return dfpNetworkName;
	}
	public void setDfpNetworkName(String dfpNetworkName) {
		this.dfpNetworkName = dfpNetworkName;
	}
	
	

	
}
