package com.lin.web.dto;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;


@Entity

public class LeftMenuDTO {

	@Id
	private Long id;
	private int title_id;
	private String title;
	private int parent_id;
	private int order;
	private String path;
	private int level;
	private String imgsrc;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeftMenuDTO()
	{
		
	}
	
	public LeftMenuDTO(int title_id,String title,int parent_id,int order,String path,int level,String imgsrc){
		this.title_id = title_id;
		this.title = title;
		this.parent_id = parent_id;
		this.order = order;
		this.path = path;
		this.level = level;
		this.imgsrc = imgsrc;
		

	}

	public int getTitle_id() {
		return title_id;
	}

	public void setTitle_id(int title_id) {
		this.title_id = title_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
}
