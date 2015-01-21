package com.lin.web.dto;

public class ReadBigQuerySchemaDTO {
	private String columnName;
	private String columnType;
	
	
	public ReadBigQuerySchemaDTO(String columnName, String columnType) {
		super();
		this.columnName = columnName;
		this.columnType = columnType;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getColumnType() {
		return columnType;
	}

}
