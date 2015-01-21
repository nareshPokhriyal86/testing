package com.lin.web.dto;

import java.io.Serializable;

/*
 * This DTO is used for storing (key,value) formed data in collections
 * @author Youdhveer Panwar
 */
public class InstrumentDTO implements Serializable, Comparable<InstrumentDTO>{
	private String id;
	private String value;
	
		
	public InstrumentDTO(){
		
	}
	
	public InstrumentDTO(String id,String value){
		this.id=id;
		this.value=value;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public int compareTo(InstrumentDTO commonDTO) {
		return value.compareToIgnoreCase(commonDTO.getValue());
	}

	
}
