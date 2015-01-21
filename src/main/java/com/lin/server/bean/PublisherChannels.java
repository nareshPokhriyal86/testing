package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class PublisherChannels implements Serializable{

@Id	private Long id;
private String publisherName;
private String channelName;
private String channelViewOrder;
private String dataSource;
private String companyName;

public PublisherChannels() {

}

public PublisherChannels(Long id, String publisherName, String channelName, String channelViewOrder,
		String dataSource, String companyName) {
	this.id= id;
	this.publisherName = publisherName;
	this.channelName = channelName;
	this.dataSource = dataSource;
	this.companyName = companyName;
	this.channelViewOrder = channelViewOrder;
}

public PublisherChannels(String publisherName, String channelName, String channelViewOrder,
		String dataSource, String companyName) {
	this.publisherName = publisherName;
	this.channelName = channelName;
	this.dataSource = dataSource;
	this.companyName = companyName;
	this.channelViewOrder = channelViewOrder;
}

public String getPublisherName() {
return publisherName;
}

public void setPublisherName(String publisherName) {
this.publisherName = publisherName;
}

public String getChannelName() {
return channelName;
}

public void setChannelName(String channelName) {
this.channelName = channelName;
}

public void setId(Long id) {
this.id = id;
}

public Long getId() {
return id;
}

public void setDataSource(String dataSource) {
this.dataSource = dataSource;
}

public String getDataSource() {
return dataSource;
}

public void setCompanyName(String companyName) {
	this.companyName = companyName;
}

public String getCompanyName() {
	return companyName;
}

public void setChannelViewOrder(String channelViewOrder) {
	this.channelViewOrder = channelViewOrder;
}

public String getChannelViewOrder() {
	return channelViewOrder;
}	


}