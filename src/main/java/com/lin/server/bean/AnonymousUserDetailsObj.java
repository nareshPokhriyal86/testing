package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class AnonymousUserDetailsObj implements Serializable{

	@Id private Long ObjectId;
	private String  google_id;
	private String email;
	private String verified_email;
	private String name;
	private String given_name;
	private String family_name;
	private String link;
	private String gender;
    private String birthday;
    private String locale;
    private String lastAccessTime;
    private long accessCount;
	
	public AnonymousUserDetailsObj(){
		
	}

	public AnonymousUserDetailsObj(String  google_id, String email,
			String verified_email, String name, String given_name,
			String family_name, String link, String gender, String birthday,
			String locale, String lastAccessTime, long accessCount) {
		super();
		this.setGoogle_id(google_id);
		this.email = email;
		this.verified_email = verified_email;
		this.name = name;
		this.given_name = given_name;
		this.family_name = family_name;
		this.link = link;
		this.gender = gender;
		this.birthday = birthday;
		this.locale = locale;
		this.lastAccessTime = lastAccessTime;
		this.accessCount = accessCount;
	}

	public long getObjectId() {
		return ObjectId;
	}

	public void setObjectId(long objectId) {
		ObjectId = objectId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerified_email() {
		return verified_email;
	}

	public void setVerified_email(String verified_email) {
		this.verified_email = verified_email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setAccessCount(long accessCount) {
		this.accessCount = accessCount;
	}

	public long getAccessCount() {
		return accessCount;
	}

	public void setGoogle_id(String google_id) {
		this.google_id = google_id;
	}

	public String getGoogle_id() {
		return google_id;
	}
	
	
		
}
