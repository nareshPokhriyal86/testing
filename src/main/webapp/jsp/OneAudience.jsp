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
<title>ONE -Campaign Summary</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
	
<!-- Below are css file from js.jsp except bootstrap.min.css and theme.css  -->
<link rel="stylesheet" type="text/css" href="../css/ng-grid.min.css?v=<s:property value='deploymentVersion'/>" />
<link rel="stylesheet" href="../css/datepicker.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/fullcalendar.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/TableTools.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/bootstrap-wysihtml5.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/wysiwyg-color.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/toastr.custom.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/toastr-responsive.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/jquery.jgrowl.css?v=<s:property value='deploymentVersion'/>">
<%-- <link rel="stylesheet" href="../css/bootstrap.min.css?v=<s:property value='deploymentVersion'/>"> --%>
<link rel="stylesheet" href="../css/bootstrap-responsive.min.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/font-awesome.min.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/cus-icons.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/jarvis-widgets.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/DT_bootstrap.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/responsive-tables.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/uniform.default.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/select2.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/theme-responsive.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" id="switch-theme-js" href="../css/themes/default.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" id="switch-width" href="../css/full-width.css?v=<s:property value='deploymentVersion'/>">
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
<link href="../css/animate.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet">
<link href="../css/stylepopover.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet"> 
<link rel="stylesheet" type="text/css" media="all" href="../css/daterangepicker.css?v=<s:property value='deploymentVersion'/>" />
<link href="../css/global.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/mediaPlan.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/navigationTabs.css?v=<s:property value='deploymentVersion'/>">
<!-- Above are files from js.jsp except bootstrap.min.css and theme.css -->

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/theme.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/custom.css?v=<s:property value='deploymentVersion'/>">

<link rel="stylesheet" href="../css/classic.css"/>
<link rel="stylesheet" href="../css/redmond/jquery-ui-1.10.4.custom.css"/>
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.css"/>

<!-- CSS for checkbox -->
<link rel="stylesheet" href="../css/bootstrap-checkbox.css">

<body >
 <!-- .height-wrapper -->
 <div class="height-wrapper">
  <div id="main" role="main" class="container-fluid" >
   
				<!-- main content -->
  <div id="page-content" class="mlr">
  
	<!-- Navigation tabs Starts -->   
    <ul id="theme-links-js" class="main-nav" style="height: 72px;">
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(publisherViewPageName)}">
			<li id="publisherViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='publisher.lin'" ><s:property value="publisherViewPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
			<li id="advertiserViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='newAdvertiserView.lin'" ><s:property value="advertiserViewPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(operationalViewPageName)}">
			<li id="operationalViewLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='proposals.lin'" ><s:property value="operationalViewPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(poolPageName)}">
			<li id="thePoolLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='newPoolMap.lin'" ><s:property value="poolPageName"/></a>
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
		<s:if test="%{userDetailsDTO.authorisedPagesList.contains(newsAndResearchPageName)}">
			<li id="newsAndResearchLi" class="main-nav-li">
				<a href="javascript:void(0)" onclick="location.href='newsAndResearch.lin'" ><s:property value="newsAndResearchPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{#session.sessionDTO.pageName == mySettingPageName}">
			<li id="mySettingLi" class="main-nav-li_selected">
				<a href="javascript:void(0)" onclick="location.href='initUserOwnProfileUpdate.lin'" ><s:property value="mySettingPageName"/></a>
			</li>
		</s:if>
		<s:if test="%{ (#session.sessionDTO.superAdmin) || (#session.sessionDTO.adminUser)}">
			<li id="adminPageLi" class="main-nav-li">
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
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(advertiserViewPageName)}">
				<li id="advertiserViewLi" class="main-nav-li">
					<a href="newAdvertiserView.lin" onclick="location.href='newAdvertiserView.lin'" ><s:property value="advertiserViewPageName"/></a>
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
			<s:if test="%{userDetailsDTO.authorisedPagesList.contains(unifiedCampaign)}">
				<li id="cmapaignPlanLi" class="main-nav-li_selected">
					<a href="smartPlanner.lin" onclick="location.href='smartPlanner.lin'" ><s:property value="unifiedCampaign"/></a>
				</li>
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
						<li>
							 <a href="propertySetup.lin" >-Property</a>
						</li>
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
	<!-- Navigation tabs Ends -->

 	<!-- tabs view -->
	<div class="row-fluid">
	
	 <article class="span12"> <!-- new widget -->
	 <div class="jarviswidget">
	  <!-- widget div starts -->
	   <div class="container-fluid" data-ng-app="oneAudianceApp" data-ng-controller="audienceController">
	   
			<div class="row-fluid">
				<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
					<div data-ng-view="">
						<!-- route file -->
					</div>
				</div>
			</div>	
	</div>

<!-- end widget div -->
</div>
<!-- end widget --> 
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
<script src="../js/jQAllRangeSliders-min.js"></script>

<!-- Moment and Picker required for Datepicker -->
<script src="../js/moment.js"></script>
<script src="../js/bootstrap-datetimepicker.js"></script>

<!-- JS for checkbok -->
<script src="../js/bootstrap-checkbox.js"></script>

<!-- Angular Module  -->
<script type="text/javascript" src="../js/dataModel.js"></script>
<script type="text/javascript" src="../js/oneAudienceModule.js"></script>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>	
