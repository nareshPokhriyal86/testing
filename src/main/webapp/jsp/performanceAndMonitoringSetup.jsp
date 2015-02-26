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
		<title>ONE - Performance And Monitoring</title>
		
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
		
		<style>
		.arrowDefault{
			background: url('../img/DT/sort_both.png') no-repeat center right;
		}
		.arrowAsc{
			background: url('../img/DT/sort_asc.png') no-repeat center right;
		}
		.arrowDesc{
			background: url('../img/DT/sort_desc.png') no-repeat center right;
		}
		</style>
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

			<div id="campaignPerformanceSetupDiv" ng-app="campaignPerformanceSetupApp" class="container-fluid well" ng-controller="campaignPerformanceSetupController" ng-cloak>
				<div class="row-fluid">
					<div class="col-md-6 col-xs-6 col-sm-6 col-lg-6">
						<div class="input-group">
	  						<span class="input-group-addon glyphicon glyphicon-search"></span>
	  						<input type="text" class="form-control" placeholder="Search Campaign" style="height: 33px;" ng-model="searchCampaign">
						</div>
					</div>
					
			<div class="control-group" style="padding:0px;">
			                   <div class="controls">
                                   <select class="span2 with-search" id="campaignPerformanceFilter" ng-change="campaignPerformanceFilter()" ng-model="selectedStatus" ng-options="stat.name for stat in status" 
                                   style="font-size:16px;height: 33px;">
                                   </select>
                                   <div ng-show="isFilterResetRequired()" data-ng-click="resetFilter()" class="btn btn-small btn-primary" style="float:right" title="This will reset campaign list to default view"><i style="color: #FFF;" class="icon-refresh"  title="This will reset campaign list to default view"></i></div>
                               </div>
       <div>
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
		</div>	
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
				<div class="table-responsive">
					<table class="table table-striped table-hover" style="border: 1px solid #D6CBCB;">
					
							<tr>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'name'; reverse=!reverse" ng-class="(predicate == 'name')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Campaign Name</td>
							
							<s:if test="%{#session.sessionDTO.roleId !=4}">
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'Partner'; reverse=!reverse"  ng-class="(predicate == 'Partner')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Partner</td>
							</s:if>
							
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'sDate'; reverse=!reverse"  ng-class="(predicate == 'sDate')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Start Date</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'eDate'; reverse=!reverse"  ng-class="(predicate == 'eDate')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">End Date</td>
							
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'goal'; reverse=!reverse"  ng-class="(predicate == 'goal')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Goal</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'delivered'; reverse=!reverse"  ng-class="(predicate == 'delivered')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Delivered</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'clicks'; reverse=!reverse" ng-class="(predicate == 'clicks')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Clicks</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'ctr'; reverse=!reverse" ng-class="(predicate == 'ctr')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">CTR</td>
							<td style="font-weight:bold;" title="Progress">Progress</td>
							<td style="font-weight:bold;cursor:pointer;cursor:hand;padding-right: 22px;" data-ng-click="predicate = 'status'; reverse=!reverse" ng-class="(predicate == 'status')?(reverse?'arrowAsc':'arrowDesc'):'arrowDefault' ">Status</td>
							
							<s:if test="%{#session.sessionDTO.roleId !=4}">
							<td></td>
							</s:if>
							
					     	</tr>
						
							<tr	data-ng-repeat="campaign in campaigns | filter:searchCampaign | orderBy : predicate : reverse">

							<td>
								<div style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.name }}</div>
								<s:if test="%{#session.sessionDTO.roleId !=4}">
								<a href="https://www.google.com/dfp/{{ campaign.dfpNetworkCode }}#delivery/OrderDetail/orderId={{ campaign.id }}" target="_blank"><span><img src="img/dfp_id_logo.png" alt="dfp" style="height: 20px;"></span><span style="font-style: italic;font-size: 12px;border: 1px #adc8ea solid;padding-right: 2px;padding-left: 3px;padding-bottom: 0px;background-color: #e0e4ec;color: #2a5da4;font-weight: bold;"><span style="margin-right: -4px;">#</span> {{ campaign.id }}</span><span style="font:status-bar;margin-left:5px">{{campaign.dfpStatus}}</span></a>
								</s:if>
							</td>
							
							<s:if test="%{#session.sessionDTO.roleId !=4}">
							<td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.partner }}</td>
							</s:if>
							
							<td  style="cursor:pointer;cursor:hand;width: 116px;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ convertStringToDate(campaign.startDate,'mm-dd-yyyy') | date:'mediumDate' }}</td>
							<td  style="cursor:pointer;cursor:hand;width: 116px;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ convertStringToDate(campaign.endDate,'mm-dd-yyyy') | date:'mediumDate' }}</td>
							
						    <td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)" ng-switch on="(campaign.goal != 0)">
						    <div ng-switch-when="true" ng-switch on="campaign.rateType" style="font-size: 16px;">{{ campaign.goal | number}}<br/>
						    	<span ng-switch-when="CPM">Impressions</span>
						    	<span ng-switch-when="CPC">Clicks</span>
						    	<span ng-switch-default>Impressions</span>
						    </div>
						    <div ng-switch-default style="font-size: 16px;">Not Set</div>
						    </td>
						    <td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.delivered | number}}</td>
							<td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.clicks | number }}</td>
							<td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.ctr | number }}%</td>
							<td  style="cursor:pointer;cursor:hand;" ng-switch on="campaign.rateType">
								
								<div class="progress" ng-switch-when="CPM" ng-switch on="campaign.barStatus" data-html="true" popover="{{ campaign.dateProgress |number:0}}% Time Completed ({{ campaign.daysTillDate | number}} out of {{ campaign.totalDays | number}} Days)
								{{ campaign.goalProgress |number:0}}% Goal Completed({{ campaign.delivered/1000000 | number:2}}M out of {{  campaign.goal/1000000 | number:2}}M impressions)" 
								popover-trigger="mouseenter" class="btn btn-default" >
 									 <div class="progress-bar progress-bar-success " ng-switch-when="1" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;">
 									 </div>
 									   <div class="progress-bar progress-bar-warning " ng-switch-when="2" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
 									  <div class="progress-bar progress-bar-danger " ng-switch-when="3" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
									  <div class="progress-bar progress-bar-warning " ng-style="setDateWidth(campaign)" style="background-color: transparent; position: absolute; top: 0px; left: 0px;border-right: 1px solid #000 !important; z-index: 2; ">
 									 </div>
							  			<!-- <div class="progress-bar progress-bar-danger" ng-style="setLastWidth(campaign)" style="background-color: #fff;">
 									 </div>
 --> 								
 	  
								</div>
								
								<div class="progress" ng-switch-when="CPC" ng-switch on="campaign.barStatus" data-html="true" popover="{{ campaign.dateProgress |number:0}}% Time Completed ({{ campaign.daysTillDate | number}} out of {{ campaign.totalDays | number}} Days)
								{{ campaign.goalProgress |number:0}}% Goal Completed({{ campaign.clicks/1000 | number:1}}K out of {{  campaign.goal/1000 | number:1}}K Clicks)" 
								popover-trigger="mouseenter" class="btn btn-default" >
 									 <div class="progress-bar progress-bar-success " ng-switch-when="1" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
 									  <div class="progress-bar progress-bar-warning " ng-switch-when="2" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
 									  <div class="progress-bar progress-bar-danger " ng-switch-when="3" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
									  <div class="progress-bar progress-bar-warning " ng-style="setDateWidth(campaign)" style="background-color: transparent; position: absolute; top: 0px; left: 0px;border-right: 1px solid #000 !important; z-index: 2; ">
 									 </div>
							  			<!-- <div class="progress-bar progress-bar-danger" ng-style="setLastWidth(campaign)" style="background-color: #fff;">
 									 </div>
 --> 								
 	  
								</div>
								
									<div class="progress" ng-switch-default ng-switch on="campaign.barStatus" data-html="true" popover="{{ campaign.dateProgress |number:0}}% Time Completed ({{ campaign.daysTillDate | number}} out of {{ campaign.totalDays | number}} Days)
								{{ campaign.goalProgress |number:0}}% Goal Completed({{ campaign.clicks/1000 | number:1}}K out of {{  campaign.goal/1000 | number:1}}K Clicks)" 
								popover-trigger="mouseenter" class="btn btn-default" >
 									 <div class="progress-bar progress-bar-success " ng-switch-when="1" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;">
 									 </div>
 									   <div class="progress-bar progress-bar-warning " ng-switch-when="2" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
 									  <div class="progress-bar progress-bar-danger " ng-switch-when="3" ng-style="setGoalWidth(campaign)" style="position: absolute;top: 0px;left: 0px;z-index: 1;" >
 									 </div>
									  <div class="progress-bar progress-bar-warning " ng-style="setDateWidth(campaign)" style="background-color: transparent; position: absolute; top: 0px; left: 0px;border-right: 1px solid #000 !important; z-index: 2; ">
 									 </div>
							  			<!-- <div class="progress-bar progress-bar-danger" ng-style="setLastWidth(campaign)" style="background-color: #fff;">
 									 </div>
 --> 								
 	  
								</div>
								
							</td>
							<td  style="cursor:pointer;cursor:hand;" data-ng-click="showPerforemanceMonitoring(campaign.campaignId)">{{ campaign.status}}</td>
						
						<s:if test="%{#session.sessionDTO.roleId !=4}">
							<td><span class="glyphicon glyphicon-bell " data-ng-click="showPopUp(campaign)"  ></span></td>
						</s:if>
						
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
			<!-- 		  <table>
        <tbody ng-repeat='campaign in campaigns'>
            <tr >
                <td>
                    <input type=checkbox ng-model=show ng-class='{open:show}'></input>
                </td>
                <td>{{campaign.name}}</td>
            </tr>
            <tr ng-repeat='campaign in campaigns' ng-show=show>
                <td></td>
                <td>{{campaign.placement.name }}</td>
            </tr>
        </tbody>
    </table> -->
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
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
	<script type="text/javascript" src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>

	<!-- Angular Module  -->
 	<script type="text/javascript" src="../js/performanceMonitoring.js?v=<s:property value="deploymentVersion"/>"></script>
		 <script>
 $(document).ready(function() {
	/*  $(function() {
		    $('#tooltip').tooltip();
		}); */
		var divScope=angular.element(document.getElementById("campaignPerformanceSetupDiv")).scope();
		divScope.loadCampaignData();
		//divScope.campaignStatusFilter();
 });
 
/*  $(window).scroll(function(){
	 if(($(window).scrollTop() + $(window).height()) >= ($(document).height()-10)) {
		 var scope=angular.element(document.getElementById("campaignPerformanceSetupDiv")).scope();
		 scope.loadCampaignData();
	 }
 }); */
 

</script>
<script type="text/javascript">
$('.mypopover').popover();
</script>
	</body>
	<jsp:include page="googleAnalytics.jsp"/>
	</html>
