<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:set name="theme" value="'simple'" scope="page" />

<script>
$(document).ready(function() {
	$('#adminPageLi').attr('class', 'main-nav-li_selected_last');
});
</script>
	<div class="row-fluid adminMenu">
		<div >
	        <ul class="nav nav-tabs" id="accordion-menu-js" style="font-size:16px;  background: #ddd !important;">
	        <!-- <ul class="nav nav-tabs upper_tabs" id="myTab1"> -->
		        	<!-- <li class=""><a href="userSetup.lin" style="color:black;">Publisher Partners</a></li>
			        <li class=""><a href="userSetup.lin" style="color:black;">Demand Partners</a></li> -->
					<li id="userLi" class=""><a href="userSetup.lin" style="color:black;">USERS</a></li>
					<li id="roleLi" class=""><a href="roleSetup.lin" style="color:black;">ROLES</a></li>
					<li id="teamLi" class=""><a href="teamSetup.lin" style="color:black;">TEAMS</a></li>
					<li id="accountLi" class=""><a href="accountSetup.lin" style="color:black;">ACCOUNTS</a></li> 
					<s:if test="%{#session.sessionDTO.superAdmin || #session.sessionDTO.publisherPoolPartner}">
						<li id="productLi" class=""><a href="publisherProduct.lin" style="color:black;">PRODUCTS</a></li>
						<li id="propertyLi" class=""><a href="propertySetup.lin" style="color:black;">PROPERTIES</a></li>
					</s:if>
					<s:if test="%{#session.sessionDTO.superAdmin}">
						<li id="companyLi" class=""><a href="companySetup.lin" style="color:black;">COMPANIES</a></li>
					</s:if>
					<s:if test="%{#session.sessionDTO.adminUser}">
						<li id="companySettingsLi" class=""><a href="companySettings.lin" style="color:black;">COMPANY SETTINGS</a></li>
					</s:if>
			</ul>
		</div>
	</div>
	