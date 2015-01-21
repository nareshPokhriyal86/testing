package com.lin.web.dto;

import java.io.Serializable;


public class CompanySetupDTO implements Serializable {

		private String companyName;	
		private String teamDescription;
		
		 CompanySetupDTO(){
			 
		 }
		 
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setTeamDescription(String teamDescription) {
			this.teamDescription = teamDescription;
		}
		public String getTeamDescription() {
			return teamDescription;
		}
		
}
