package com.lin.web.dto;

public class OrderDTO{
	private long orderId;
	private String orderName;
	
	public OrderDTO(){
	}
	
	public OrderDTO(long orderId, String orderName) {		
		this.orderId = orderId;
		this.orderName = orderName;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderDTO [orderId=");
		builder.append(orderId);
		builder.append(", orderName=");
		builder.append(orderName);
		builder.append("]");
		return builder.toString();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	
}
