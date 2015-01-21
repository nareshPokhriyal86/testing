package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * @author Naresh Pokhriyal
 */

public class JavaScriptTagDTO implements Serializable{
	
	private String campaignName;
	private String tagForDFP;
	private String tagForNonDFP;
	
	public JavaScriptTagDTO() {
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getTagForDFP() {
		return tagForDFP;
	}

	public void setTagForDFP(String tagForDFP) {
		this.tagForDFP = tagForDFP;
	}

	public String getTagForNonDFP() {
		return tagForNonDFP;
	}

	public void setTagForNonDFP(String tagForNonDFP) {
		this.tagForNonDFP = tagForNonDFP;
	}

	
}
