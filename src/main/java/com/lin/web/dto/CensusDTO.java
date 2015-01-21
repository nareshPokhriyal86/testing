package com.lin.web.dto;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/*
 * @author Anup Dutta
 * @Description This dto will have all layers detail so that we can add various census dynamically into platforms
 * */
@Index
public class CensusDTO implements Serializable,Comparable<CensusDTO>{
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String group;
	private String groupTxt;
	
	private boolean gender;
	private boolean active;
	
	private String bqColumn;
	private String bqMaleCol;
	private String bqFemaleCol;
	private String bqParentCol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupTxt() {
		return groupTxt;
	}

	public void setGroupTxt(String groupTxt) {
		this.groupTxt = groupTxt;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getBqColumn() {
		return bqColumn;
	}

	public void setBqColumn(String bqColumn) {
		this.bqColumn = bqColumn;
	}

	public String getBqMaleCol() {
		return bqMaleCol;
	}

	public void setBqMaleCol(String bqMaleCol) {
		this.bqMaleCol = bqMaleCol;
	}

	public String getBqFemaleCol() {
		return bqFemaleCol;
	}

	public void setBqFemaleCol(String bqFemaleCol) {
		this.bqFemaleCol = bqFemaleCol;
	}

	public String getBqParentCol() {
		return bqParentCol;
	}

	public void setBqParentCol(String bqParentCol) {
		this.bqParentCol = bqParentCol;
	}

	@Override
	public int compareTo(CensusDTO o) {
		CensusDTO temp = (CensusDTO)o;
		return this.groupTxt.compareTo(temp.groupTxt);
	}
	
}
