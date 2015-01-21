package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.lin.web.dto.ProductForecastDTO;

@Entity
@Index

public class ProductForecastObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private long productId;
	private String productName;
	private String partnerId; // publisherCompanyId
	private String publisherName;
	private ProductForecastDTO forcaProductDTO;
	private int daysType;
	private GeoTargetsObj dmaDetail;
	
	public ProductForecastObj() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public ProductForecastDTO getForcaProductDTO() {
		return forcaProductDTO;
	}

	public void setForcaProductDTO(ProductForecastDTO forcaProductDTO) {
		this.forcaProductDTO = forcaProductDTO;
	}

	public int getDaysType() {
		return daysType;
	}

	public void setDaysType(int daysType) {
		this.daysType = daysType;
	}

	public GeoTargetsObj getDmaDetail() {
		return dmaDetail;
	}

	public void setDmaDetail(GeoTargetsObj dmaDetail) {
		this.dmaDetail = dmaDetail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}