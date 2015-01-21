<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
 
<jsp:include page="Header.jsp" /> 

<!DOCTYPE html>
<html lang="en">
<head>
<script>				
localStorage.clear();
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<title>ONE - Campaign Performance</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<style>

#hidefunctionality{

pointer-events: none !important;
background-image: url(img/campaign_icons/disable_edit.png); 
}


.pause_btn{
    background-color:  #fe8d47; /* #e54d40; */
   	margin-right : 10px;
    background-image: url("../img/pause_btn_img.png");
    background-repeat: no-repeat;
    background-position: 2px 3px;
    border: 1px solid #fe8d47;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .cancel_btn{
    background-color:  #BD362F; /* #e54d40; */
   
    background-image: url("../img/close_btn_img.png");
    background-repeat: no-repeat;
    background-position: 2px 3px;
    border: 1px solid #BD362F;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .uncancel_btn{
    background-color:  #729C14; /* #e54d40; */
   
    background-image: url("../img/play_btn_img.png");
    background-repeat: no-repeat;
    background-position: 2px 3px;
    border: 1px solid #729C14;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .launch_btn{
    background-color: #00a020;
    background-image: url("../img/launch_btn_img.png");
    background-position: 2px 3px;
    background-repeat: no-repeat;
    border: 1px solid #00a020;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .setup_btn{
    background-color: #666666;
    background-image: url("../img/setup_btn_img.png");
    background-position: 2px 3px;
    background-repeat: no-repeat;
    border: 1px solid #666666;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .resume_btn{
    background-color: #3875a8;
    background-image: url("../img/resume_btn_img.png");
    background-position: 2px 3px;
    background-repeat: no-repeat;
    border: 1px solid #3875a8;
    border-radius: 2px;
    color: #ffffff;
    cursor: pointer;
    height: 35px;
    padding-left: 25px;
    width: 30px;
 }
 
 .customAlertShowClass{
 	width:40%;margin-left:auto;margin-right:auto;left:8%;right:8%;
 	background: white;overflow-y:hidden !important;bottom:initial !important;
 }
</style>
<script type='text/javascript' src='../js/accounting.js'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.0-beta.18/angular-sanitize.js"></script>
<!-- Below are css file from js.jsp except bootstrap.min.css and theme.css  -->
<link rel="stylesheet" href="../css/datepicker.css">
<link rel="stylesheet" href="../css/fullcalendar.css">
<link rel="stylesheet" href="../css/TableTools.css">
<link rel="stylesheet" href="../css/bootstrap-wysihtml5.css">
<link rel="stylesheet" href="../css/wysiwyg-color.css">
<link rel="stylesheet" href="../css/toastr.custom.css">
<link rel="stylesheet" href="../css/toastr-responsive.css">
<link rel="stylesheet" href="../css/jquery.jgrowl.css">
<%-- <link rel="stylesheet" href="../css/bootstrap.min.css"> --%>
<link rel="stylesheet" href="../css/bootstrap-responsive.min.css">
<link rel="stylesheet" href="../css/font-awesome.min.css">
<link rel="stylesheet" href="../css/cus-icons.css">
<link rel="stylesheet" href="../css/jarvis-widgets.css">
<link rel="stylesheet" href="../css/DT_bootstrap.css">
<link rel="stylesheet" href="../css/responsive-tables.css">
<link rel="stylesheet" href="../css/uniform.default.css">
<link rel="stylesheet" href="../css/select2.css">
<link rel="stylesheet" href="../css/theme-responsive.css">
<link rel="stylesheet" id="switch-theme-js" href="../css/themes/default.css">
<link rel="stylesheet" id="switch-width" href="../css/full-width.css">
<link rel='stylesheet' href='http://fonts.googleapis.com/css?family=Lato:300,400,700' type='text/css'>
<link rel="shortcut icon" href="../img/favicons/favicon-lin.ico">
<link rel="shortcut icon" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" href="../img/favicons/favicon-lin.ico">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-title" content="Jarvis">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (max-device-width: 320px)">
<link href="../css/style.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet" type="text/css" />
<link href="../css/animate.css" rel="stylesheet">
<link href="../css/stylepopover.css" rel="stylesheet"> 
<link rel="stylesheet" type="text/css" media="all" href="../css/daterangepicker.css" />
<link href="../css/global.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/mediaPlan.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/navigationTabs.css?v=<s:property value='deploymentVersion'/>">
<!-- Above are files from js.jsp except bootstrap.min.css and theme.css -->

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/theme.css">
<link rel="stylesheet" href="../css/custom.css">
<link rel="stylesheet" href="../jquery-notific8-1.1.0/css/jquery.notific8.css" />
<body >
 <!-- .height-wrapper -->
 <div class="height-wrapper">
  <div id="main" role="main" class="container-fluid" >
   
				<!-- main content -->
  <div id="page-content" class="mlr">
    <ul id="theme-links-js" class="main-nav" style="height: 72px">
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(publisherViewPageName)}">
			<li id="publisherViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='publisher.lin'" ><s:property value="publisherViewPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(campaignPerformancePageName)}">
		<li id="campaignPerformanceViewLi" class="main-nav-li">
			<a href="javascript:void(0)" onclick="location.href='newAdvertiserView.lin'" ><s:property value="campaignPerformancePageName"/></a>
		</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
			<li id="advertiserViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='campaignPerformanceListing.lin'" ><s:property value="advertiserViewPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(operationalViewPageName)}">
			<li id="operationalViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='proposals.lin'" ><s:property value="operationalViewPageName"/></a>
			</li>
		</s:if>

		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(unifiedCampaign)}">
			<li id="cmapaignPlanLi" style="float: left !important;
					display: inline !important;
					padding: 24px 20px 22px 20px !important;
					border-left: 1px solid #dda609 !important;
					border-right: 1px solid #dda609 !important;
					font-weight: 600 !important;
					background: #fdc525 url('../img/li_selected_tab_arrow.png') center bottom no-repeat !important;
					margin-top: -7px !important;
					margin-left: -1px !important;">
				<a href="javascript:void(0)" onclick="location.href='smartPlanner.lin'" ><s:property value="unifiedCampaign"/></a>
			</li>
		</s:if>
		
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
			<li id="thePoolLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='newPoolMap.lin'" ><s:property value="poolPageName"/></a>
			</li>
		</s:if>
		
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(newsAndResearchPageName)}">
			<li id="newsAndResearchLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='newsAndResearch.lin'" ><s:property value="newsAndResearchPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(mapEngineName)}">
			<li id="mapEngineLi" class="main-nav-li">
				<a href="mapEngine.lin" onclick="location.href='mapEngine.lin'" ><s:property value="mapEngineName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(report)}">
			<li id="reportLi" class="main-nav-li">
				<a href="reporting.lin" onclick="location.href='reporting.lin'" ><s:property value="report"/></a>
			</li>
		</s:if>
		<s:if test="%{#session.sessionDTO.pageName == mySettingPageName}">
			<li id="mySettingLi" class="main-nav-li_selected">
				<a href="javascript:void(0)" onclick="location.href='initUserOwnProfileUpdate.lin'" ><s:property value="mySettingPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{ (#session.sessionDTO.superAdmin) || (#session.sessionDTO.adminUser)}">
			<li id="adminPageLi" class="main-nav-li-lastChild">
				<a href="javascript:void(0)" onclick="location.href='userSetup.lin'" ><s:property value="adminPageName"/></a>
			</li>
		</s:if>
	</ul>	
		
	<div class="responsiveDiv">
			<ul  id="accordion-menu-js" class="menu " >
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(publisherViewPageName)}">
					<li id="publisherViewLi" class="main-nav-li">
						<a href="publisher.lin" onclick="location.href='publisher.lin'" ><s:property value="publisherViewPageName"/></a>
					</li>
					<s:if test="%{ (#session.sessionDTO.pageName.contains(publisherViewPageName))}">
					
							<s:if test="%{authorisationKeywordList.contains('publisherInvRevView')}">
									<li>
									  <a href="javascript:$('#inv_rev').click();" >-<%=TabsName.PUBLISHER_VIEW_SUMMARY%></a>
									</li>
							</s:if>
							<s:if test="%{authorisationKeywordList.contains('publisherTreAnsView')}">
					        	     <li>
					        	       <a href="javascript:$('#tre_ana').click();" >-<%=TabsName.PUBLISHER_VIEW_TRENDS%></a>
					        	     </li>
					        </s:if>
					        <s:if test="%{authorisationKeywordList.contains('publisherDiagToolView')}">
					        	     <li>
					        	       <a href="javascript:$('#diagnosticTools').click();"  >-<%=TabsName.PUBLISHER_VIEW_DIAGNOSTIC_TOOLS%></a>
					        	     </li>
					        </s:if>
					        <s:if test="%{authorisationKeywordList.contains('publisherTrafView')}">
					                <li>
					        	       <a href="javascript:$('#trafficking').click();" >-<%=TabsName.PUBLISHER_VIEW_TRAFFICKING%></a>
					               </li>
					        </s:if>
					
					</s:if>
				
			</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(campaignPerformancePageName)}">
			<li id="campaignPerformanceViewLi" class="main-nav-li">
				<a href="newAdvertiserView.lin" onclick="location.href='newAdvertiserView.lin'" ><s:property value="campaignPerformancePageName"/></a>
			</li>
					<s:if test="%{ (#session.sessionDTO.pageName.contains(advertiserViewPageName))}">
						 <li>
		                    <a href="javascript:$('#per_sum').click();" >-<%=TabsName.ADVERTIER_VIEW_SUMMARY%></a>
		                 </li>
		                 <li>
		    	            <a href="javascript:$('#richMediaSummary').click();" >-<%=TabsName.ADVERTIER_VIEW_RICH_MEDIA%></a>
		                 </li>
					</s:if>
		  
		</s:if>
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
				<li id="advertiserViewLi" class="main-nav-li_selected">
					<a href="campaignPerformanceListing.lin" onclick="location.href='campaignPerformanceListing.lin'" ><s:property value="advertiserViewPageName"/></a>
				</li>
			  
			</s:if>
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(operationalViewPageName)}">
				<li id="operationalViewLi" class="main-nav-li">
					<a href="proposals.lin" onclick="location.href='proposals.lin'" ><s:property value="operationalViewPageName"/></a>
				</li>
						<s:if test="%{ (#session.sessionDTO.pageName.contains(operationalViewPageName))}">
						       <s:if test="%{proposalDTO.showTabs.contains('yes')}">
							       <li>
							    	    <a href="javascript:$('#campaignBrief').click();" >-<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS_BRIEF%></a>
							       </li>
							       <li>
							    	    <a href="javascript:$('#mediaPlan').click();" >-<%=TabsName.CAMPAIGN_VIEW_MEDIA_PLANNER%></a>
							       </li>
							       <li>
							    	    <a href="javascript:$('#insertionOrder').click();" >-<%=TabsName.CAMPAIGN_VIEW_IO%></a>
							       </li>
						       </s:if>
						       <s:else>
						       	<li>
						    	    <a href="javascript:$('#richMediaSummary').click();" >-<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%></a>
						       </li>
						       </s:else>
			      		</s:if>
			</s:if>
			
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(unifiedCampaign)}">
			<li id="cmapaignPlanLi" class="main-nav-li">
				<a href="smartPlanner.lin" onclick="location.href='newPoolMap.lin'" ><s:property value="unifiedCampaign"/></a>
			</li>
		</s:if>
			
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
				<li id="thePoolLi" class="main-nav-li">
					<a href="newPoolMap.lin" onclick="location.href='newPoolMap.lin'" ><s:property value="poolPageName"/></a>
				</li>
						<s:if test="%{ (#session.sessionDTO.pageName.contains(poolPageName))}">
							<li>
					    	    <a href="javascript:$('#search_inventory').click();" >-<%=TabsName.THE_POOL_SEARCH_INVENTORY%></a>
					       </li>
					       <li>
					    	    <a href="javascript:$('#create_proposal').click();" >-<%=TabsName.THE_POOL_CREATE_PROPOSAL%></a>
					       </li>
				        </s:if>
			</s:if>
			
		
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(newsAndResearchPageName)}">
				<li id="newsAndResearchLi" class="main-nav-li">
					<a href="newsAndResearch.lin" onclick="location.href='newsAndResearch.lin'" ><s:property value="newsAndResearchPageName"/></a>
				</li>
					<s:if test="%{ (#session.sessionDTO.pageName.contains(newsAndResearchPageName))}">
						<li>
				    	    <a href="javascript:$('#indus_new').click();" >-<%=TabsName.NEWS_AND_RESEARCH_NEWS%></a>
				       </li>
			       </s:if>
			</s:if>
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(mapEngineName)}">
				<li id="mapEngineLi" class="main-nav-li">
					<a href="mapEngine.lin" onclick="location.href='mapEngine.lin'" ><s:property value="mapEngineName"/></a>
				</li>
			</s:if>
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(report)}">
				<li id="reportLi" class="main-nav-li">
					<a href="reporting.lin" onclick="location.href='reporting.lin'" ><s:property value="report"/></a>
				</li>
			</s:if>
			<s:if test="%{#session.sessionDTO.pageName == mySettingPageName}">
				<li id="mySettingLi" class="main-nav-li_selected">
					<a href="initUserOwnProfileUpdate.lin" onclick="location.href='initUserOwnProfileUpdate.lin'" ><s:property value="mySettingPageName"/></a>
				</li>
			</s:if>
				<s:if test="%{ (#session.sessionDTO.superAdmin) || (#session.sessionDTO.adminUser)}">
						<li id="adminPageLi" class="main-nav-li-lastChild">
							<a href="userSetup.lin" onclick="location.href='userSetup.lin'" ><s:property value="adminPageName"/></a>
					   </li>
					   <s:if test="%{ (#session.sessionDTO.pageName.contains(adminPageName))}">
						<li>
							 <a href="userSetup.lin" >-Users</a>
						</li>
						<li>
							 <a href="roleSetup.lin" >-Roles</a>
						</li>
						<li>
							 <a href="teamSetup.lin" >-Teams</a>
						</li>
						<s:if test="%{#session.sessionDTO.superAdmin}">
						<li>
							 <a href="companySetup.lin" >-Companies</a>
						</li>
						</s:if>
						<s:if test="%{#session.sessionDTO.superAdmin || #session.sessionDTO.publisherPoolPartner}">
							<li><a href="propertySetup.lin" >-Property</a> </li>
							<li><a href="publisherProduct.lin" >-Products</a></li>
						</s:if>
						<s:if test="%{#session.sessionDTO.superAdmin || #session.sessionDTO.publisherPoolPartner}">
						<li>
							 <a href="companySettings.lin" >-Company Settings</a>
						</li>
						</s:if>
					    </s:if>
				</s:if>
		</ul>
		
	</div>

 <!-- tabs view -->
<div class="row-fluid">

 <article class="span12"> <!-- new widget -->
 <div class="jarviswidget">
  <!-- widget div starts -->

	<div id="unifiedCampaignSetupDiv" ng-app="unifiedCampaignSetupApp" class="container-fluid well" ng-controller="unifiedCampaignSetupController" ng-cloak>
		<div class="row-fluid">
			<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
				<div class="input-group">
	  				<span class="input-group-addon glyphicon glyphicon-search"></span>
	  				<input type="text" class="form-control" placeholder="Search Campaign" ng-model="searchCampaign" style=" margin-top: 1px; margin-bottom: -1px; ">
				</div>
			</div>
			<div class="control-group" style="padding:0px;">
			                   <div class="controls">
                                   <select class="span2 with-search" id="campaignFilter" ng-change="campaignFilter()" ng-model="selectedStatus" ng-options="stat.name for stat in status" 
                                   style="font-size:16px;">
            							  
                                   </select>
                               </div>
            <div>
			<div class="col-md-4 col-xs-4 col-sm-4 col-lg-4">
				<div class="btn-group" style="float:right;">
				  <input type="button" class="btn btn-primary" onclick="location.href='initCampaign.lin'" value="New Campaign">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
		</div>	
		<div class="row-fluid" >
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
				<div class="table-responsive">
					<table class="table table-striped table-hover" style="border: 1px solid #D6CBCB;">
					
							<tr>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'name'; reverse=!reverse" title="Campaign Name">Campaign Name</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'advertiser'; reverse=!reverse" title="Advertiser">Advertiser</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'date'; reverse=!reverse" title="Duration">Duration</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'impression'; reverse=!reverse" 
							title="CPM-Impression &#10;CPC-Clicks &#10;CPD-100% Allocation">Goal</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'budget'; reverse=!reverse" title="Budget">Budget</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'status'; reverse=!reverse" title="Status">Status</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'lineItemStatus'; reverse=!reverse" title="Line Items">Line Items</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" title="Media Plan">Media Plan</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;"  title="Edit">Edit</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" title="Archive">Archive</td>
							<td></td>
					     	</tr>
						
							<tr	data-ng-repeat="campaign in campaigns | filter:searchCampaign | orderBy:predicate:reverse">
							<td>
								<div  title="{{ campaign.name }}" data-ng-click="">{{ campaign.name }}</div>
								<a href="https://www.google.com/dfp/{{ campaign.dfpNetworkCode }}#delivery/OrderDetail/orderId={{ campaign.dfpOrderId }}" target="_blank"><span style="font:status-bar">{{ campaign.dfpOrderId }}</span><span style="font:status-bar;margin-left:5px">{{campaign.dfpStatus}}</span></a>
							</td>
							<td   title="{{ campaign.advertiser }}" data-ng-click="">{{ campaign.advertiser }}</td>
							<td   title="{{ campaign.date }}" data-ng-click="">{{ campaign.date }}</td>
						    <td   title="{{ campaign.rateType }}" data-ng-click="">{{ campaign.impression | number}}</td>
							<td   title="{{ campaign.budget | currency  }}" data-ng-click="">{{ campaign.budget | currency }}</td>
							<td   title="{{ campaign.status }}" data-ng-click="">{{ campaign.status }}</td>
							
							<td   title="{{ campaign.lineItemStatusTitle }}" data-ng-click=""><p ng-bind-html='campaign.lineItemStatus'></p></td>
							<td ng-switch on="campaign.status" style="cursor:pointer;cursor:hand;" >
							   <span ng-switch-when="Draft" ng-switch on="campaign.isMigrated">
							   	 <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">
									  
									  <span ng-switch-when="false" ng-switch on="campaign.isProcessing">									  
										  <span ng-switch-when="false" >
										     <img id="create_media_plan_draft_{{campaign.id}}"  src="img/campaign_icons/genrate_media_plan.png" 
										     		style="cursor:pointer;cursor:hand;"  title="Generate Smart Media Plan" 
										     		ng-click="goSmartMediaPlan(campaign.id,campaign.isProcessing,'auto')" />									 
										  </span>
										  <span ng-switch-when="true">
								               <img src="img/loaders/type5/light/24.gif" alt="loader" />
								       	  </span>
								       	    <span ng-switch-when="false" >
										     <img id="create_media_plan_draft_{{campaign.id}}"  src="img/campaign_icons/Generate-mediaplan-manually.png" 
										     		style="cursor:pointer;cursor:hand;"  title="Generate Smart Media Plan Manually" 
										     		ng-click="goSmartMediaPlan(campaign.id,campaign.isProcessing,'manual')" />									 
										  </span>
									  </span>	
									  <span ng-switch-when="true" ng-switch on="campaign.isProcessing">
									  		 <img id="regenrate_media_plan_draft_{{campaign.id}}" style="cursor:pointer;cursor:hand;" 
									          src="img/campaign_icons/regenrate_media_plan.png"	 title="Regenerate Smart Media Plan" 
									          ng-switch-when="false" ng-click="checkPlanType(campaign.id,campaign.statusId,campaign.isProcessing,campaign.planType)" />
										 
								            <img id="view_media_plan_draft_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								              title="View Smart Media Plan"  ng-switch-when="false" ng-click="viewSmartMediaPlan(campaign.id)"  />
									  </span>					        							     
								</span>	
							   </span>

								<span id="smartMediaPlanLoader_{{ campaign.id }}" style="display:none;text-align: center;">
									   <img src="img/loaders/type5/light/24.gif" alt="loader">
							    </span>	
							    
							     <span ng-switch-when="Ready"  ng-switch on="campaign.isMigrated">	
								    <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">
								       <span ng-switch-when="true" ng-switch on="campaign.isProcessing">
								          <img id="view_media_plan_ready_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
									    		title="View Smart Media Plan" ng-switch-when="false" ng-click="viewSmartMediaPlan(campaign.id)">
									    
								           <span ng-switch-when="true">InProgress</span>
								       </span>
								    </span>	
							    </span>
							    
							     <span ng-switch-when="Completed"  ng-switch on="campaign.isMigrated">
								     <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">
								        <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								             title="View Smart Media Plan" ng-switch-when="true" ng-click="viewSmartMediaPlan(campaign.id)">	
								    </span>	
							     </span>
							    				
								
								<span ng-switch-when="Running"  ng-switch on="campaign.isMigrated">
									<span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">							       
								        <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								           title="View Smart Media Plan" ng-switch-when="true" ng-click="viewSmartMediaPlan(campaign.id)">
								    </span>	
								</span>
								
								<span ng-switch-when="Paused"  ng-switch on="campaign.isMigrated">	
								  <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">							       
							        <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								    title="View Smart Media Plan" ng-switch-when="true" ng-click="viewSmartMediaPlan(campaign.id)">
							      </span>
								</span>
							  	<span ng-switch-when="Archived"  ng-switch on="campaign.isMigrated">
							  	   <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">							       
							        <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								        title="View Smart Media Plan" ng-switch-when="true" ng-click="viewSmartMediaPlan(campaign.id)">
							      </span>	
							    </span>
							    
						      	<span ng-switch-when="Canceled"  ng-switch on="campaign.isMigrated">
							  	   <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">							       
							        <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/view_media_plan.png" 
								  title="View Smart Media Plan" ng-switch-when="true" ng-click="viewSmartMediaPlan(campaign.id)">
							      </span>	
							   </span>
							   						
								
							</td>
							 <!-- id = "hidefunctionality" -->
							<td ng-switch on="campaign.status" style="cursor:pointer;cursor:hand;" id="edit_{{campaign.id}}">
								<span ng-switch-when="Draft" ng-switch on="campaign.isProcessing">
							       <img id="edit_draft_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/edit.png" 
							      		ng-switch-when="false" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)"/>
							       <img id="edit_draft_disable_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" 
							      		ng-switch-when="true" data-ng-click=""/>
							    </span>
							    
							    <span ng-switch-when="Ready" ng-switch on="campaign.isProcessing">
							       <img id="edit_ready_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/restricted_edit.png" 
							            ng-switch-when="false" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)" />
							       <img id="edit_ready_disable_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" 
							            ng-switch-when="true" data-ng-click="" />
							    </span>
							    
								
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" ng-switch-when="Archived" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)">
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" ng-switch-when="Running" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)">
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" ng-switch-when="Completed" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)">
								
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/edit.png" ng-switch-when="Paused" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)">
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_edit.png" ng-switch-when="Canceled" data-ng-click="getCampaignDetails(campaign.id,campaign.isProcessing)">
								
							</td>
							<td id="archive_{{campaign.id}}" ng-switch on="campaign.status" style="cursor:pointer;cursor:hand;" >
							    <span ng-switch-when="Ready" ng-switch on="campaign.isProcessing">
							      <img id="archive_ready_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/archive.png" title="Archive" 
							       ng-switch-when="false" data-ng-click="deleteCampaign(campaign.id,campaign.isProcessing)" />
							       <img id="archive_ready_disable_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_archive.png" title="Archive" 
							       ng-switch-when="true" data-ng-click="" />
							    </span>
							    
							     <span ng-switch-when="Draft" ng-switch on="campaign.isProcessing">
							       <img id="archive_draft_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/archive.png" title="Archive" 
							      	ng-switch-when="false" data-ng-click="deleteCampaign(campaign.id,campaign.isProcessing)" />							      
							       <img id="archive_draft_disable_{{campaign.id}}" style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_archive.png" title="Archive" 
							      	ng-switch-when="true" data-ng-click="" />
							    </span>
							    
								
								
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/archive.png" title="Archive" ng-switch-when="Completed" data-ng-click="deleteCampaign(campaign.id,campaign.isProcessing)">
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/archive.png" title="Archive" ng-switch-when="Running" data-ng-click="deleteCampaign(campaign.id,campaign.isProcessing)">
								<!--<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_archive.png" ng-switch-when="Running" data-ng-click="">  -->
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/disable_archive.png" ng-switch-when="Paused" data-ng-click="">
								<img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/unarchive.png" title="Unarchive" ng-switch-when="Archived" data-ng-click="unarchiveCampaign(campaign.id)" >
							    <img style="cursor:pointer;cursor:hand;" src="img/campaign_icons/archive.png" title="Archive" ng-switch-when="Canceled" data-ng-click="deleteCampaign(campaign.id,campaign.isProcessing)">
							</td>
							<td  ng-switch on="campaign.status"> 	
							    <span ng-switch-when="Draft"  ng-switch on="campaign.isMigrated">
							       <span ng-switch-when="false"  ng-switch on="campaign.hasMediaPlan">		
							       
							        <span ng-switch-when="true"  ng-switch on="campaign.isProcessing">							  						  
								     	<input id="setup_{{campaign.id}}" type="button" ng-switch-when="false" 
								     	   	  class="setup_btn" title="Setup" value="" ng-click="setupCampaign(campaign.id)">
									  		<img ng-switch-when="true" src="img/loaders/type5/light/24.gif" alt="loader" />
								    </span>	  						  
								     
								     <span id="setupCampaignLoader_{{ campaign.id }}" style="display:none;text-align: center;">
									  		<img src="img/loaders/type5/light/24.gif" alt="loader" />
							         </span>							     
								   </span>
							    </span>						
								
								<span ng-switch-when="Ready"  ng-switch on="campaign.isMigrated">	
									<span ng-switch-when="false"  ng-switch on="campaign.dfpStatus">							  						  
									     <input id="launch_{{campaign.id}}" type="button" ng-switch-when="PENDING_APPROVAL"  class="launch_btn" title="Launch" value="" ng-click="launchCampaign(campaign.id)">							     
									</span> 	
								</span>
								
								<span ng-switch-when="Running">							  						  
								     <input type="button" class="pause_btn" title="Pause" ng-click="pauseCampaign(campaign.id)">
								      <input type="button" class="cancel_btn" title="Cancel" value="" ng-click="cancelCampaign(campaign.id)">
								      <!-- <i class="icon-remove-circle" ng-click="cancelCampaign(campaign.id)"></i> -->
								</span> 
								<span ng-switch-when="Paused">							  						  
								     <input  type="button" class="resume_btn" title="Resume" value="" ng-click="resumeCampaign(campaign.id)">
								</span> 
								<span ng-switch-when="Completed">							  						  
								     <!-- <input style="cursor:pointer;cursor:hand;" type="button" class="btn btn-default"  value="Archived"> -->
								</span>
								<span ng-switch-when="Canceled">							  						  
								     <!-- <input style="cursor:pointer;cursor:hand;" type="button" class="btn btn-default"  value="Archived"> -->
								</span>
								<span ng-switch-when="Archived">							  						  
								     <!-- <input style="cursor:pointer;cursor:hand;" type="button" class="btn btn-default disabled"  value="Launch"> -->
								</span>
								<span ng-switch-when="Canceled">
								<input type="button" class="uncancel_btn" title="Start" ng-click="unCancelCampaign(campaign.id)">
								</span>
								
							</td>
						
							</tr>
							<tr id="loaderRowId">
								<td colspan="11" style="text-align:center;">
									<img src="img/loaders/type4/light/46.gif" alt="loader">
								</td>
							</tr>
							<tr id="noMatchRowId" style="display: none;">
								<td colspan="11" style="text-align:center;">
									No match found
								</td>
							</tr>
						  
					</table> 

				</div>
			</div>
		</div>	
          <!--  <div id="demo" class="collapse in">dasfaskfhaskdhfkf </div> -->
		<s:form name = "unifiedCampaignSetupForm" id="select-demo-js" cssClass="form-horizontal themed" action="initEditCampaign.lin" >
			<input type="hidden" id="campaignId" name="unifiedCampaignDTO.id" value="" >
		</s:form>
		
			
	</div>

<!-- end widget div -->
</div>
<!-- end widget --> 
				<!-- Custom Prompt Starts -->
								<div id="customAlertId" class="modal hide fade customAlertShowClass" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display:none;">
									<div class="modal-body">
										<div class="row-fluid">
											<div class="control-group">
												<h5 id="customAlertMessageId"></h5>
											</div>
										</div>
										<div class="row-fluid">
											<div class="control-group">
												<div class="controls" style="float: right;">
													<button type="button" id="customAlertOkBtn" onclick="javascript:regenerateSmartMediaPlanForManual()" style="margin-right: 10px;" class="btn btn-success btn-large">Ok</button>
													<button type="button" id="customAlertCancelBtn" onclick="javascript:hideCustomPromptRegenerate();" class="btn btn-danger btn-large" >Cancel</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							<!-- Custom Prompt Ends -->
</div>

</div>
	
</article>

 </div>
 <!-- end main content -->
 </div>
 </div>
  <!--end fluid-container-->
		
</div>
	
	
 <jsp:include page="js.jsp"/>
 
 <!-- 3rd Party Libraries Angular , Angular Routes , Jquery , Bootstrap --> 
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
<script type="text/javascript" src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>


 <script type="text/javascript" src="../jquery-notific8-1.1.0/js/jquery.notific8.js"></script>
 <script type="text/javascript" src="/_ah/channel/jsapi"></script>
 
 <script>
 $(document).ready(function() {
	    createChannel();
		var divScope=angular.element(document.getElementById("unifiedCampaignSetupDiv")).scope();
		divScope.loadUnifiedCampaignData();
		//divScope.campaignStatusFilter();
	 	$('#customAlertId').on('hidden', function () {
	 		$('#customAlertId').attr('style', 'display: none !important');
	 	    
	 	});
 });
 
 $(window).scroll(function(){
	 if(($(window).scrollTop() + $(window).height()) >= ($(document).height()-10)) {
		 var scope=angular.element(document.getElementById("unifiedCampaignSetupDiv")).scope();
		 scope.loadUnifiedCampaignData();
	 }
 });
 
		
/*********************************** Generate Media Plan ********************************/
		var status='<s:property value="unifiedCampaignDTO.statusId"/>';
		var token='<s:property value="#session.token"/>';
		var userId=<s:property value="#session.sessionDTO.userId"/>;
		var id=0;
        var selectedCampaignId=0;
        var inProgress=false;
        var smartMediaPlanMsg="Smart Media plan generated successfully.";
		String.prototype.replaceAll = function(stringToFind, stringToReplace) {
			if (stringToFind == stringToReplace) return this;
			var temp = this;
			var index = temp.indexOf(stringToFind);
			while (index != -1) {
			temp = temp.replace(stringToFind, stringToReplace);
			index = temp.indexOf(stringToFind);
			}
			return temp;
		}

/***************************************** Channel API implementation***************************************/
		 //Use channel API to notify after a task completion in task queue
		 function createChannel(){
			 token='<s:property value="#session.token"/>';
			
			  try{
					var channel = new goog.appengine.Channel(token);
					var socket = channel.open();
					socket.onopen = onOpened;
					socket.onmessage = onMessage;
					socket.onerror = onError;
					socket.onclose = onClose;
			  }catch(error){		  
				 console.log('Error: Channel not created after expires...'+error);		  	  
			  }
		 }

		 onOpened = function() {
			console.log("channel opened...");
		 } 

		 onMessage = function(message) {			
			console.log("======= status via channel API ======message:");
			var response=$.parseJSON(message.data);
			
			//$('#draft_campaign_'+response.campaignId).show();
			$('#smartMediaPlanLoader_'+response.campaignId).hide();
			inProgress=false;
			var requestType=response.requestType;
			
			if(requestType == 'SMART_MEDIA_PLAN_SETUP' && response.smartMediaPlanId =='0'){	
				toastr.error(response.status);
				toastr.options.timeOut = 400;	
	    	}else if(requestType == 'CAMPAIGN_SETUP' && response.dfpOrderId =='0'){
	    		toastr.error(response.status);
				toastr.options.timeOut = 400;	
	    	}else{	
	    		
	    		toastr.success(response.status);
		        toastr.options.timeOut = 100;	
		        location.reload();
	    	}	
						
		 } 
				

		 onError=function(){
			try{
				var child = $wnd.parent.document.getElementById("wcs-iframe");
				if ( child != null ) {
					child.parentNode.removeChild(child);	
				} 
			}catch(error){
				console.log("error -- "+error);
			}
			reCreateChannel();
		 }

		 onClose = function() {
			try{
				var child = $wnd.parent.document.getElementById("wcs-iframe");
				if ( child != null ) {
					child.parentNode.removeChild(child);	
				} 
			}catch(error){
				console.log('error : '+error);
			}	
			reCreateChannel();
		 } 

		 var counter=0;
		 function reCreateChannel(){
		 	try{
		 		if(counter <3){			
		 			counter++;
		 			$.ajax({
		 			      url: "/createChannelAfterLogin.lin",
		 			      dataType: 'json',	     
		 			      contentType: 'application/JSON',
		 			      type: "GET",
		 			      success: function(data) {
		 			    	  console.log('channelToken -- '+data);
		 			    	  token=data;
		 		 			  createChannel();
		 			      },
		 			      error: function(jqXHR, exception) {
		 			       console.log("ajax exception -- "+exception);
		 			      }
		 			});
		 		}else{
		 			console.log("Please check channel api -- not able to get channel after relogin attempts :"+counter);
		 		}
		 	}catch(err){
		 		console.log("error in recreate channel- "+err);
		 	}
		 	
		 }

/***************************************** Channel API implementation ends ***************************************/
		

</script>		

 <!-- Angular Module  -->
 <script type="text/javascript" src="../js/unifiedCampaign.js?v=<s:property value="deploymentVersion"/>"></script>
		
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
