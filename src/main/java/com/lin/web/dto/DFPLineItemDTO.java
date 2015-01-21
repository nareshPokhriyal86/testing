package com.lin.web.dto;

/*
 * @author Youdhveer Panwar
 * This object will hold the data for line items after setup on DFP using our platform
 */
public class DFPLineItemDTO {

	private long lineItemId;
	private String lineItemName;
	private String partner;
	private String dfpStatus;
	
	public DFPLineItemDTO(){
		
	}
	
	public DFPLineItemDTO(long lineItemId, String lineItemName, String partner, String dfpStatus) {
		this.lineItemId = lineItemId;
		this.lineItemName = lineItemName;
		this.partner = partner;
		this.dfpStatus = dfpStatus;
	}
	
	
	@Override
	public String toString() {
		return "DFPLineItemDTO [lineItemId=" + lineItemId + 
				", lineItemName=" + lineItemName + 
				", partner=" + partner + 
				", dfpStatus=" + dfpStatus + "]";
	}

	public long getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}
	public String getLineItemName() {
		return lineItemName;
	}
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getDfpStatus() {
		return dfpStatus;
	}

	public void setDfpStatus(String dfpStatus) {
		this.dfpStatus = dfpStatus;
	}
	
}
