package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class SummaryReportDTO implements Serializable {
	
	private String accountName;
	private String order;
	private Date startDateTime;
	private Date endDateTime;
	private Double budget;
	private Double spent;
	private Long impressionDelivered;
	private Long clicks;
	private Double CTR;
	private Double balance;
	private Double benchCtr;
	public SummaryReportDTO(){
		
	}
	public SummaryReportDTO(String accountName, String order,
			Date startDateTime, Date endDateTime, Double budget, Double spent,
			Long impressionDelivered, Long clicks, Double cTR,
			Double balance, Double benchCtr) {
		super();
		this.accountName = accountName;
		this.order = order;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.budget = budget;
		this.spent = spent;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
		this.balance = balance;
		this.benchCtr = benchCtr;
	}
	@Override
	public String toString() {
		return "SummaryReportDTO [accountName=" + accountName + ", order="
				+ order + ", startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", budget=" + budget + ", spent=" + spent
				+ ", impressionDelivered=" + impressionDelivered + ", clicks="
				+ clicks + ", CTR=" + CTR + ", balance=" + balance
				+ ", benchCtr=" + benchCtr + "]";
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Date getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	public Double getBudget() {
		return budget;
	}
	public void setBudget(Double budget) {
		this.budget = budget;
	}
	public Double getSpent() {
		return spent;
	}
	public void setSpent(Double spent) {
		this.spent = spent;
	}
	public Long getImpressionDelivered() {
		return impressionDelivered;
	}
	public void setImpressionDelivered(Long impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
	}
	public Long getClicks() {
		return clicks;
	}
	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}
	public Double getCTR() {
		return CTR;
	}
	public void setCTR(Double cTR) {
		CTR = cTR;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getBenchCtr() {
		return benchCtr;
	}
	public void setBenchCtr(Double benchCtr) {
		this.benchCtr = benchCtr;
	}
	

	
}
