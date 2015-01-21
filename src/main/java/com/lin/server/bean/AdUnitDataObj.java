package com.lin.server.bean;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * This is POJO (for datastore entity) file which will represent ad unit data
 * 
 * @author Youdhveer Panwar
 * 
 */
@Index
public class AdUnitDataObj {

	@Id	private String id;
	private String adUnit1;	
	private String adUnit2;
	private String adUnitId1;	
	private String adUnitId2;
	private String adUnitCode;	
	private String publisher;
	private String publisherId;
	
	public AdUnitDataObj(){
		
	}	
	
	public AdUnitDataObj(String adUnit1, String adUnit2, String adUnitId1,
			String adUnitId2, String adUnitCode, String publisher,
			String publisherId) {
		this.adUnit1 = adUnit1;
		this.adUnit2 = adUnit2;
		this.adUnitId1 = adUnitId1;
		this.adUnitId2 = adUnitId2;
		this.adUnitCode = adUnitCode;
		this.publisher = publisher;
		this.publisherId = publisherId;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdUnit1() {
		return adUnit1;
	}
	public void setAdUnit1(String adUnit1) {
		this.adUnit1 = adUnit1;
	}
	public String getAdUnit2() {
		return adUnit2;
	}
	public void setAdUnit2(String adUnit2) {
		this.adUnit2 = adUnit2;
	}
	public String getAdUnitId1() {
		return adUnitId1;
	}
	public void setAdUnitId1(String adUnitId1) {
		this.adUnitId1 = adUnitId1;
	}
	public String getAdUnitId2() {
		return adUnitId2;
	}
	public void setAdUnitId2(String adUnitId2) {
		this.adUnitId2 = adUnitId2;
	}
	public String getAdUnitCode() {
		return adUnitCode;
	}
	public void setAdUnitCode(String adUnitCode) {
		this.adUnitCode = adUnitCode;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	
	
 
}
