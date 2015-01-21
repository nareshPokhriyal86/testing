package com.lin.web.dto;

import java.io.Serializable;

/*
 * @author Naresh Pokhriyal
 */

public class PlacementDTO implements Serializable{

	private String placementName;	
	private String siteName;
	private String budgetAllocation;
	private String effectiveCPM;
	private String proposedImpression;
	
	public PlacementDTO(){
	}

	public PlacementDTO(String placementName, String siteName,
			String budgetAllocation, String effectiveCPM,
			String proposedImpression) {
		this.placementName = placementName;
		this.siteName = siteName;
		this.budgetAllocation = budgetAllocation;
		this.effectiveCPM = effectiveCPM;
		this.proposedImpression = proposedImpression;
	}

	@Override
	public String toString() {
		return "PlacementDTO [placementName=" + placementName + ", siteName="
				+ siteName + ", budgetAllocation=" + budgetAllocation
				+ ", effectiveCPM=" + effectiveCPM + ", proposedImpression="
				+ proposedImpression + "]";
	}

	public String getPlacementName() {
		return placementName;
	}

	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getBudgetAllocation() {
		return budgetAllocation;
	}

	public void setBudgetAllocation(String budgetAllocation) {
		this.budgetAllocation = budgetAllocation;
	}

	public String getEffectiveCPM() {
		return effectiveCPM;
	}

	public void setEffectiveCPM(String effectiveCPM) {
		this.effectiveCPM = effectiveCPM;
	}

	public String getProposedImpression() {
		return proposedImpression;
	}

	public void setProposedImpression(String proposedImpression) {
		this.proposedImpression = proposedImpression;
	}

	
}
