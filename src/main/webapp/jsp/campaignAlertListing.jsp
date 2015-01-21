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
		$(document).ready(function() {
			$('#advertiserViewLi').attr('class', 'navigationForNewBootstrap');
			
		});
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
		<title>ONE - Campaign Alert Listing</title>
		
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		
		<jsp:include page="css.jsp"/>
		<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
		<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
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
		
		</head>
			<body >
		<!-- .height-wrapper -->
 		 <div class="height-wrapper">
 		 <div id="main" role="main" class="container-fluid" >
				<!-- main content -->
 		 <div id="page-content" class="mlr">
 		 <jsp:include page="navigationTab.jsp"/>
 		  <!-- tabs view -->
			<div class="row-fluid">

 				<article class="span12"> <!-- new widget -->
				 <div class="jarviswidget">
				  <!-- widget div starts -->

			<div id="campaignAlertListingDiv" ng-app="campaignAlertListingApp" class="container-fluid well" ng-controller="campaignAlertListingController" ng-cloak>
				<div class="row-fluid">
					
			<div class="control-group" style="padding:0px;">
			                   
       <div>
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
		</div>	
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
				<div class="table-responsive">
					<table class="table table-striped table-hover" style="border: 1px solid #D6CBCB;">
					
							<tr>
							<td >	<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12" style="margin-left: -15px;">
										<div class="input-group">
	  										<span class=""></span>
	  										<input type="text" class="form-control" placeholder="Filter By  Date" style="height: 40px;width: 100px;" ng-model="searchDate">
										</div>
									</div>
							</td>
								<td >	<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12" style="margin-left: -15px;">
										<div class="input-group">
	  										<span class=""></span>
	  										<input type="text" class="form-control" placeholder="Filter By Campaign Name" style="height: 40px;width: 200px;" ng-model="searchCampaign">
										</div>
									</div>
							</td>
								<td >	<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12" style="margin-left: -15px;">
										<div class="input-group">
	  										<span class=""></span>
	  										<input type="text" class="form-control" placeholder="Filter By Placement Name" style="height: 40px;width: 250px;" ng-model="searchPlacement">
										</div>
									</div>
							</td>
								<td >	<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12" style="margin-left: -15px;">
										<div class="input-group">
	  										<span class=""></span>
	  										<input type="text" class="form-control" placeholder="Filter By Publisher Name" style="height: 40px;width: 150px;" ng-model="searchPublisher">
										</div>
									</div>
							</td>
							<td></td>
					     	</tr>
					     	
					     	<tr>
					     	<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'date'; reverse=!reverse" title="Date">Date</td>
					     	<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'campaignName'; reverse=!reverse" title="Campaign Name">Campaign Name</td>
					     	<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'placementName'; reverse=!reverse" title="Placement Name">Placement</td>
					     	<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'publisherName'; reverse=!reverse" title="Publisher Name">Publisher</td>
					     	<td style="font-weight:bold;cursor:pointer;cursor:hand;background:url('../img/DT/sort_both.png') no-repeat center right;padding-right: 22px;" data-ng-click="predicate = 'exception'; reverse=!reverse" title="Exceptions">Exceptions</td>
					     	</tr>
						
							<tr	data-ng-repeat="alert in campaignAlerts | filter:alertListCustomFilter | orderBy:predicate:reverse">
							
								<td  title="{{ alert.date }}" >{{ alert.date }}</td>
								<td  title="{{ alert.campaignName }}" >{{ alert.campaignName }}</td>
								<td  title="{{ alert.placementName }}" >{{ alert.placementName }}</td>
								<td  title="{{ alert.publisherName }}" >{{ alert.publisherName }}</td>
								<td  title="{{ alert.exception }}" >{{ alert.exception }}</td>
							
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
					
				</div><!-- <div id="alertLoaderId" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="Loading..."></div> -->
			</div>
		</div>	
          <!--  <div id="demo" class="collapse in">dasfaskfhaskdhfkf </div> -->
		<s:form name = "campaignAlertListingForm" id="select-demo-js" cssClass="form-horizontal themed" action="campaignAlertListing.lin" >
		</s:form>
	</div>

<!-- end widget div -->
</div>
</div>
</div>
<!-- end widget --> 
</div>
</article>

 </div>
 <!-- end main content -->
 		 
 		 </div>
 		 
 		 </div>
 		 
 		 </div>
			
		 	
	 <jsp:include page="js.jsp"/>
 
	 <!-- 3rd Party Libraries Angular , Angular Routes , Jquery , Bootstrap --> 
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
	<script type="text/javascript" src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>

	<!-- Angular Module  -->
 	 <script src="../js/angular/controller/campaignAlert.js?v=<s:property value="deploymentVersion"/>"></script>
		 <script>
 $(document).ready(function() {
	/*  $(function() {
		    $('#tooltip').tooltip();
		}); */
		var divScope=angular.element(document.getElementById("campaignAlertListingDiv")).scope();
		divScope.loadCampaignAlerts();
		//divScope.campaignStatusFilter();
 });
 
 $(window).scroll(function(){
	 // if(ready && $(window).scrollTop() + $(window).height() == $(document).height()) {
	if((ready && $(window).scrollTop() + $(window).height()) >= ($(document).height()-10)) {
		ready=false;
		 var scope=angular.element(document.getElementById("campaignAlertListingDiv")).scope();
		 scope.loadCampaignAlerts();
		// console.log(page)
	 }
 });
 

</script>

	</body>
</html>