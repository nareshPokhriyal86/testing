<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
 
<jsp:include page="Header.jsp" /> 

<!DOCTYPE html>
<html lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
	<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
   %>
<title>Insert title here</title>
 <jsp:include page="css.jsp"/>
	<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
	<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
	
	<link rel="stylesheet" href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
	<link rel="stylesheet" href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
</head>
<body>
<div class="height-wrapper">
  		<div id="main" role="main" class="container-fluid"  >
  			<div class="contained">
			<!-- main content -->
  		 <div id="page-content" class="mlr">
   			 <jsp:include page="navigationTab.jsp"/>
   			  <h1 id="page-header" style="margin-left: 1%;">Unified Campaign Details</h1>
   			 
   				 <div class="fluid-container" id ="removeBorderForm" style="background-color: white;" >
   					 <section id="widget-grid" class="">
   					 <s:form name = "unifiedCampaignListForm" id="select-demo-js" cssClass="form-horizontal themed" action="" >
   					 	 	   <div class="row-fluid" >
							<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
										<button type="button" id="nextButtonid" onclick="createCampaign()" class="btn btn-primary btn-large">New Campaign</button>
									</div>
								</article>
							</div>
   					 <div class="row-fluid" >
   					 
  <div class="table-responsive">
	  <table class="table table-striped table-hover">
		<tr>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" >Campaign Name</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" >Impression</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" >Start Date</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" >End Date</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" >Budget</td>
			<td></td>
		</tr>
		 <s:iterator value="unifiedCampaignDTO.campaignObjList">
		<tr>
			
			<td style="cursor:pointer;cursor:hand;" onclick="updateCampaign('<s:property value='id'/>');"><s:property value="name"/></td>
			<td style="cursor:pointer;cursor:hand;" onclick="updateCampaign('<s:property value='id'/>');"><s:property value="goal"/></td>
			<td style="cursor:pointer;cursor:hand;" onclick="updateCampaign('<s:property value='id'/>');"><s:property value="startDate"/></td>
			<td style="cursor:pointer;cursor:hand;" onclick="updateCampaign('<s:property value='id'/>');"><s:property value="endDate"/></td>
			<td style="cursor:pointer;cursor:hand;" onclick="updateCampaign('<s:property value='id'/>');"><s:property value="budget"/></td>
			
		</tr>
		</s:iterator>
		</table>
	</div>
	</div>
	<input type="hidden" id="campaignId" name="unifiedCampaignDTO.id" value="" >
	
	</s:form>
   </section>
  	 </div>
   </div>
   </div>
  	 </div>
  	 </div>
<%--   <div class="col-md-4 col-xs-4 col-sm-4 col-lg-4" style="float:right;">
				<div class="btn-group" style="float:right;">
				  <input type="button" class="btn btn-default" ng-click="go('/createCampaign')" value="New Campaign">
				</div>
</div>
<div class="table-responsive">
	<table class="table table-striped table-hover">
		<tr>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" data-ng-click="predicate = 'name'; reverse=!reverse">Campaign Name</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" data-ng-click="predicate = 'pubId'; reverse=!reverse">Impression</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" data-ng-click="predicate = 'pubName'; reverse=!reverse">Start Date</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" data-ng-click="predicate = 'pubName'; reverse=!reverse">End Date</td>
			<td style="font-weight:bold;cursor:pointer;cursor:hand;" data-ng-click="predicate = 'pubName'; reverse=!reverse">Budget</td>
			<td></td>
		</tr>
		<tr
			data-ng-repeat="campaign in campaignList ">
			<td style="cursor:pointer;cursor:hand;" data-ng-click="updateCampaign(campaign.id)">{{ campaign.name }}</td>
			<td style="cursor:pointer;cursor:hand;" data-ng-click="updateCampaign(campaign.id)">{{ campaign.goal | number}}</td>
			<td style="cursor:pointer;cursor:hand;" data-ng-click="updateCampaign(campaign.id)">{{ campaign.click | number }}</td>
			<td style="cursor:pointer;cursor:hand;" data-ng-click="updateCampaign(campaign.id)">{{ campaign.budget | currency }}</td>
			<td style="cursor:pointer;cursor:hand;" data-ng-click="updateCampaign(campaign.id)">{{ campaign.pacing }}</td>
			<td><span class="glyphicon glyphicon-bell" data-ng-click="showPopUp(campaign)"></span></td>
		</tr>
	</table>
</div> --%>
		<jsp:include page="js.jsp"/>
		<script type="text/javascript" src="../js/dataModel.js"></script>
		<script type="text/javascript" src="../js/unifiedCampaign.js"></script>
</body>
</html>