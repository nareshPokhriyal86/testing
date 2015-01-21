package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class NewAdvertiserViewDTO implements Serializable {

	private String order;
	private String c1lineItem;
	private String c2impressionBooked;
	private String c4impressionDelivered;
	private String pacing;
	private String c5clicks;
	private String c6CTR;
	private String c7budget;
	private String c8spent;
	private String c9balance;
	private String d1startDateTime;
	private String d2endDateTime;
	private String c3site;
	private String b1creativeSize;
	private String d3benchCtr;
	private String z1lineItemId;
	private String z2forcastStatus;
	private String targetValue;
	private String accountName;
	private Long impressionBooked;
	private Long impressionDelivered;
	private Double pacingDouble;
	private Long clicks;
	private Double CTR;
	private Double budget;
	private Double spent;
	private Double balance;
	private Date startDateTime;
	private Date endDateTime;
	
	/* Use for header data */	
	public NewAdvertiserViewDTO(String order, String c4impressionDelivered,
			String c5clicks, String c6CTR, String c7budget, String c8spent,
			String c9balance, String d1startDateTime,String d2endDateTime,String accountName,Double spent, Double CTR,Double balance,
			Long clicks,Long impressionDelivered, Double budget) {		
		this.order = order;
		this.c4impressionDelivered = c4impressionDelivered;
		this.c5clicks = c5clicks;
		this.c6CTR = c6CTR;
		this.c7budget = c7budget;
		this.c8spent = c8spent;
		this.c9balance = c9balance;		
		this.d1startDateTime=d1startDateTime;
		this.d2endDateTime = d2endDateTime;
		this.accountName = accountName;
		this.spent = spent;
		this.CTR = CTR;
		this.balance = balance;
		this.clicks = clicks;
		this.impressionDelivered = impressionDelivered;
		this.budget = budget;
	}

	public NewAdvertiserViewDTO(String c3site,String c4impressionDelivered,
			String c5clicks, String c6CTR) {		
		this.c3site = c3site;
		this.c4impressionDelivered = c4impressionDelivered;
		this.c5clicks = c5clicks;
		this.c6CTR = c6CTR;
		
	}
	
	
	@Override
	public String toString() {
		return "NewAdvertiserViewDTO [order=" + order + ", c1lineItem="
				+ c1lineItem + ", c2impressionBooked=" + c2impressionBooked
				+ ", c4impressionDelivered=" + c4impressionDelivered
				+ ", pacing=" + pacing + ", c5clicks=" + c5clicks + ", c6CTR="
				+ c6CTR + ", c7budget=" + c7budget + ", c8spent=" + c8spent
				+ ", c9balance=" + c9balance + ", d1startDateTime="
				+ d1startDateTime + ", d2endDateTime=" + d2endDateTime
				+ ", c3site=" + c3site + ", b1creativeSize=" + b1creativeSize
				+ ", d3benchCtr=" + d3benchCtr + ", z1lineItemId="
				+ z1lineItemId + ", z2forcastStatus=" + z2forcastStatus
				+ ", targetValue=" + targetValue + ", accountName="
				+ accountName + "]";
	}

	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getC1lineItem() {
		return c1lineItem;
	}
	public void setC1lineItem(String c1lineItem) {
		this.c1lineItem = c1lineItem;
	}
	public String getC2impressionBooked() {
		return c2impressionBooked;
	}
	public void setC2impressionBooked(String c2impressionBooked) {
		this.c2impressionBooked = c2impressionBooked;
	}
	public String getC4impressionDelivered() {
		return c4impressionDelivered;
	}
	public void setC4impressionDelivered(String c4impressionDelivered) {
		this.c4impressionDelivered = c4impressionDelivered;
	}
	public String getPacing() {
		return pacing;
	}
	public void setPacing(String pacing) {
		this.pacing = pacing;
	}
	public String getC5clicks() {
		return c5clicks;
	}
	public void setC5clicks(String c5clicks) {
		this.c5clicks = c5clicks;
	}
	public String getC6CTR() {
		return c6CTR;
	}
	public void setC6CTR(String c6ctr) {
		c6CTR = c6ctr;
	}
	public String getC7budget() {
		return c7budget;
	}
	public void setC7budget(String c7budget) {
		this.c7budget = c7budget;
	}
	public String getC8spent() {
		return c8spent;
	}
	public void setC8spent(String c8spent) {
		this.c8spent = c8spent;
	}
	public String getC9balance() {
		return c9balance;
	}
	public void setC9balance(String c9balance) {
		this.c9balance = c9balance;
	}
	
	public String getC3site() {
		return c3site;
	}
	public void setC3site(String c3site) {
		this.c3site = c3site;
	}
	public void setD1startDateTime(String d1startDateTime) {
		this.d1startDateTime = d1startDateTime;
	}
	public String getD1startDateTime() {
		return d1startDateTime;
	}
	public void setD2endDateTime(String d2endDateTime) {
		this.d2endDateTime = d2endDateTime;
	}
	public String getD2endDateTime() {
		return d2endDateTime;
	}
	public String getB1creativeSize() {
		return b1creativeSize;
	}
	public void setB1creativeSize(String b1creativeSize) {
		this.b1creativeSize = b1creativeSize;
	}
	public String getD3benchCtr() {
		return d3benchCtr;
	}
	public void setD3benchCtr(String d3benchCtr) {
		this.d3benchCtr = d3benchCtr;
	}
	
	public String getLineItemId() {
		return z1lineItemId;
	}
	public void setLineItemId(String lineItemId) {
		this.z1lineItemId = lineItemId;
	}
	public NewAdvertiserViewDTO() {
		
	}

	public void setZ2forcastStatus(String z2forcastStatus) {
		this.z2forcastStatus = z2forcastStatus;
	}

	public String getZ2forcastStatus() {
		return z2forcastStatus;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setImpressionBooked(Long impressionBooked) {
		this.impressionBooked = impressionBooked;
	}

	public Long getImpressionBooked() {
		return impressionBooked;
	}

	public void setImpressionDelivered(Long impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
	}

	public Long getImpressionDelivered() {
		return impressionDelivered;
	}

	public void setPacingDouble(Double pacingDouble) {
		this.pacingDouble = pacingDouble;
	}

	public Double getPacingDouble() {
		return pacingDouble;
	}

	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}

	public Long getClicks() {
		return clicks;
	}

	public void setCTR(Double cTR) {
		CTR = cTR;
	}

	public Double getCTR() {
		return CTR;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Double getBudget() {
		return budget;
	}

	public void setSpent(Double spent) {
		this.spent = spent;
	}

	public Double getSpent() {
		return spent;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getBalance() {
		return balance;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	
	
}
