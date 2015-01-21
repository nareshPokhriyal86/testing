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
	$('#campaignPerformanceViewLi').attr('class', 'main-nav-li_selected');
	$('#per_sum').click(function() {
		$('#s4').hide();
		$('#s2').removeClass("active");
		$('#s3').removeClass("active");
		$('#s1').addClass("active");
		angular.bootstrap($("#lineChartDiv"),['lineChartApp']);
		angular.element(document.getElementById("geoChartDiv")).scope().initdata();
	});
	$('#richMediaSummary').click(function() {
		$('#s4').hide();
		$('#s1').removeClass("active");
		$('#s3').removeClass("active");
		$('#s2').addClass("active");
		drawVisualization('Market', 0);
	});
	$('#videoCampaign').click(function() {
		$('#s4').hide();
		$('#s1').removeClass("active");
		$('#s2').removeClass("active");
		$('#s3').addClass("active");
		var videoCampaignScope=angular.element(document.getElementById("videoCampaignLineChartDiv")).scope();
		videoCampaignScope.fillData();
		videoCampaignScope.safeApply(function(){
			videoCampaignScope.emptyData();
		 });
	});
	$('#indus_new').click(function() {
		$('#s4').show();
	});
	
	$('#isNoise').on('switch-change', function (e, data) {
		isNoise = data.value;
		if(isNoise) {
			$('#thresholdSlider').css("visibility","visible");
		}
		else {
			$('#thresholdSlider').css("visibility","hidden");
		}
	});
	
	$( ".threshold-slider-range" ).slider({
        range: "min",
        step: 0.001,
        value: <s:property value='threshold'/>,
        min: 0.001,
        max: 0.1,
        slide: function( event, ui ) {
            $( ".threshold-slider-value" ).val(  ui.value.toFixed(3) + "%" );
        }
    });
	$(".threshold-slider-value").val( $(".threshold-slider-range" ).slider("value").toFixed(3) + "%");
	
});
var summaryTab = '<%=TabsName.ADVERTIER_VIEW_SUMMARY%>';
var richMediaTab = '<%=TabsName.ADVERTIER_VIEW_RICH_MEDIA%>';
var videoCampaignTab = '<%=TabsName.ADVERTIER_VIEW_VIDEO_CAMPAIGN%>';
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

<jsp:include page="css.jsp"/>
<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
	
<link rel="stylesheet" href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet" href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
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
 <div>

<!-- content -->

<jsp:include page="CampaigenPerformanceSetting.jsp"/>

 <div id="tab-content1" class="tab-content">
    <ul class="nav nav-tabs" id="myTab1" style="font-size:16px;">
    	<s:if test="%{authorisationKeywordList.contains('cpCampaignView')}">
			<li class="active">
				<a href="#s1" onclick="onAdvertiserTabClick(this); getText(this);"	id="per_sum" data-toggle="tab"><%=TabsName.ADVERTIER_VIEW_SUMMARY%></a>
			</li>
		</s:if>
		<s:if test="%{authorisationKeywordList.contains('cpRichMediaView')}">
			<li >
				<a href="#s2" onclick="onAdvertiserTabClick(this); getText(this);"	id="richMediaSummary" data-toggle="tab"><%=TabsName.ADVERTIER_VIEW_RICH_MEDIA%></a>
			</li>
		</s:if>
		<%-- <s:if test="%{authorisationKeywordList.contains('cpVideoCampaignView')}">
			<li >
				<a href="#s3" onclick="onAdvertiserTabClick(this); getText(this);"	id="videoCampaign" data-toggle="tab"><%=TabsName.ADVERTIER_VIEW_VIDEO_CAMPAIGN%></a>
			</li>
		</s:if> --%>
		<!-- <li >
			<a href="#s4" onclick="onAdvertiserTabClick(this); getText(this);"	id="indus_new" data-toggle="tab">Industry News and Research</a>
		</li> -->
	</ul>
	<jsp:include page="newAdvertiserViewFilters.jsp" />
	<br>  
	<br>
	<div class="agencyDropdown">
      <div id="headerDiv" class="mystats indented revenue_bg" style="height: 45px;">
		 <div class="fixme" >
			<jsp:include page="newAdvertiserViewHeader.jsp" />
		 </div>
	  </div>
    </div>
    <s:if test="%{authorisationKeywordList.contains('cpCampaignView')}">
		<div class="tab-pane active" id="s1" >
			<jsp:include page="newAdvertiserViewPerformerSummary.jsp" />
		</div>
	</s:if>

<s:if test="%{authorisationKeywordList.contains('cpRichMediaView')}">
 	<div class="tab-pane" id="s2">
	  		
	  		<div style="height:30px;width:100%;"> 					
				<div id="selectRichMediaEventGraphMetricsDiv" class="btn-group hidden-phone " style="display:none;margin-top:8px;font-size:1.25em;float:right;">
					<b>Breakdown by</b>&nbsp; &nbsp; 
					<a id="selectRichMediaEventGraphMetricsLabel" href="javascript:void(0)" class="btn btn-large btn-inverse dropdown-toggle" data-toggle="dropdown">
					Market
					<span class="caret" style="margin-left: 6px;margin-top: 8px;"></span>
					</a>
					<ul id="theme-links-js" class="dropdown-menu toolbar pull-left">
						<li>
							<a href="javascript:void(0)" onclick="drawVisualization('Market',0);" data-rel="default">Market</a>
						</li>
						<li>
							<a href="javascript:void(0)" onclick="drawVisualization('Site',1);" data-rel="default">Site</a>
						</li>
						<li>
							<a href="javascript:void(0)" onclick="drawVisualization('Campaign Category', 2);" data-rel="default">Campaign Category</a>
						</li>
					</ul>
				</div>
			</div>
			
			<div id="richMediaEventGraphLoader" style="text-align: center;">
				<img src="img/loaders/type4/light/46.gif" alt="loader">
			</div>
			<div id="emptyDataTableMsgId" style="display: none;">
				<h2><center>No data found for the selected campaign</center></h2>
			</div>
			
			<div id="richMediaEventGraphOuterDiv">
												
			</div>
	  </div>
</s:if>

<s:if test="%{authorisationKeywordList.contains('cpVideoCampaignView')}">
	<br/>
   	<br/>
	<div class="tab-pane" id="s3">

	<div class="row-fluid">
		<div id="noVideoDataMsgId" style="display: none;">
			<h2><center>No data found for the selected campaign</center></h2>
		</div>
		<div id="videoCampaignLineChartDiv" ng-app="videoCampaignLineChartApp" ng-controller="videoCampaignLineChartCtrl" class="inner-spacer">
			<div class="row-fluid">
				<article class="span12 sortable-grid ui-sortable">
					<!-- new widget -->
					<div id="videoCampaignLineChartInnerDiv" class="jarviswidget">
						<header>
							<h2>By Rate</h2>
						</header>
						<!-- wrap div -->
						
						<div style="border-bottom-color: white !important">
							
							<div title="AVERAGE INTERACTION RATE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #4cc0bf;">AVERAGE INTERACTION RATE</div>
											<div id="average_Interaction_Rate" class="big-number videoCampaignBoardLowerText" style="color: #4cc0bf;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="AVERAGE VIEW RATE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #acd43c;">AVERAGE VIEW RATE</div>
											<div id="average_View_Rate" class="big-number videoCampaignBoardLowerText" style="color: #acd43c;"></div>
										</div>
									</div>
								</div> 
							</div>
							
							<div title="COMPLETION RATE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #f37f04;">COMPLETION RATE</div>
											<div id="completion_Rate" class="big-number videoCampaignBoardLowerText" style="color: #f37f04;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<!-- <div title="AVERAGE INTERACTION RATE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #c3392d;">AVERAGE INTERACTION RATE</div>
											<div class="big-number videoCampaignBoardLowerText" style="color: #c3392d;">21.65 %</div>
										</div>
									</div>
								</div>
							</div> -->
							
							<!-- <div title="BOARD" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div style="padding: 0px; text-align: center; background-color: grey; min-height: 38px">
											<h2 class="account-color" style="color: white; width: 100%; word-wrap: break-word; background-color: grey; margin: 0px; text-align: center; min-height: 30px;">TITLE</h2>
										</div>
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number" style="margin-left: 0px; margin-top: 20px; color: black; width: 100%; text-align: center; font-size: 60px;">2013</div>
										</div>
									</div>
								</div>
							</div> -->
						</div>
						
						<div>
							<div class="jarviswidget-editbox">
								<div>
									<label>Title:</label> <input type="text" />
								</div>
								<div>
									<label>Styles:</label>
									<span data-widget-setstyle="purple" class="purple-btn"></span>
									<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									<span data-widget-setstyle="green" class="green-btn"></span>
									<span data-widget-setstyle="yellow" class="yellow-btn"></span>
									<span data-widget-setstyle="orange" class="orange-btn"></span>
									<span data-widget-setstyle="pink" class="pink-btn"></span>
									<span data-widget-setstyle="red" class="red-btn"></span>
									<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									<span data-widget-setstyle="black" class="black-btn"></span>
								</div>
							</div>
							
							<!-- <div id="videoCampaignLineChartDiv" ng-app="videoCampaignLineChartApp" ng-controller="videoCampaignLineChartCtrl" class="inner-spacer"> -->	
							    <div id="byRateChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
							     	<img class="chartLoaderImageClass" src="img/loaders/type4/light/46.gif" alt="loader">
							     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
							    </div> 
								
								<div id="byRateChartDiv" class="linechartdatadiv"  google-chart chart="byRateChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- </div> -->

							<!-- end content-->
						</div>
						<!-- end wrap div -->
					</div>
					<!-- end widget -->
				</article>
			</div>
			
			<div class="row-fluid">
				<article class="span12 sortable-grid ui-sortable">
					<!-- new widget -->
					<div class="jarviswidget">
						<header>
							<h2>By Length</h2>
						</header>
						<!-- wrap div -->
						
						<div style="border-bottom-color: white !important">
							
							<div title="FIRST QUARTILE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #4cc0bf;">FIRST QUARTILE</div>
											<div id="firstQuartile" class="big-number videoCampaignBoardLowerText" style="color: #4cc0bf;"></div>
										</div>
									</div>
								</div> 
							</div>
							
							<div title="MIDPOINT" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #acd43c;">MIDPOINT</div>
											<div id="midpoint" class="big-number videoCampaignBoardLowerText" style="color: #acd43c;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="THIRD QUARTILE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #f37f04;">THIRD QUARTILE</div>
											<div id="third_Quartile" class="big-number videoCampaignBoardLowerText" style="color: #f37f04;"></div>
										</div>
									</div>
								</div> 
							</div>
							
							<div title="COMPLETE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #c3392d;">COMPLETE</div>
											<div id="complete" class="big-number videoCampaignBoardLowerText" style="color: #c3392d;"></div>
										</div>
									</div>
								</div>
							</div>
							
						</div>
						
						<div>
							<div class="jarviswidget-editbox">
								<div>
									<label>Title:</label> <input type="text" />
								</div>
								<div>
									<label>Styles:</label>
									<span data-widget-setstyle="purple" class="purple-btn"></span>
									<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									<span data-widget-setstyle="green" class="green-btn"></span>
									<span data-widget-setstyle="yellow" class="yellow-btn"></span>
									<span data-widget-setstyle="orange" class="orange-btn"></span>
									<span data-widget-setstyle="pink" class="pink-btn"></span>
									<span data-widget-setstyle="red" class="red-btn"></span>
									<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									<span data-widget-setstyle="black" class="black-btn"></span>
								</div>
							</div>
							
							<!-- <div id="videoCampaignLineChartDiv" ng-app="videoCampaignLineChartApp" ng-controller="videoCampaignLineChartCtrl" class="inner-spacer"> -->	
							    <div id="byLengthChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
							     	<img class="chartLoaderImageClass" src="img/loaders/type4/light/46.gif" alt="loader">
							     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
							    </div> 
								
								<div id="byLengthChartDiv" class="linechartdatadiv"  google-chart chart="byLengthChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- </div> -->

							<!-- end content-->
						</div>
						<!-- end wrap div -->
					</div>
					<!-- end widget -->
				</article>
			</div>
			
			<div class="row-fluid">
				<article class="span12 sortable-grid ui-sortable">
					<!-- new widget -->
					<div class="jarviswidget">
						<header>
							<h2>By Event</h2>
						</header>
						<!-- wrap div -->
						
						<div style="border-bottom-color: white !important">
							
							<div title="PAUSE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #acd43c;">PAUSE</div>
											<div id="pause" class="big-number videoCampaignBoardLowerText" style="color: #acd43c;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="RESUME" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #f37f04;">RESUME</div>
											<div id="resume" class="big-number videoCampaignBoardLowerText" style="color: #f37f04;"></div>
										</div>
									</div>
								</div> 
							</div>
							
							<div title="REWIND" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #c3392d;">REWIND</div>
											<div id="rewind" class="big-number videoCampaignBoardLowerText" style="color: #c3392d;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="MUTE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #e83e6b;">MUTE</div>
											<div id="mute" class="big-number videoCampaignBoardLowerText" style="color: #e83e6b;"></div>
										</div>
									</div>
								</div> 
							</div>
							
							<div title="UNMUTE" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #FF0000;">UNMUTE</div>
											<div id="unmute" class="big-number videoCampaignBoardLowerText" style="color: #FF0000;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="FULL SCREEN" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #000080;">FULL SCREEN</div>
											<div id="fullScreen" class="big-number videoCampaignBoardLowerText" style="color: #000080;"></div>
										</div>
									</div>
								</div>
							</div>
							
							<div title="START" class="analytics-boards" style="float: left; margin-right: 15px; margin-top: 15px; margin-left: 10px; margin-bottom: 10px; width: 333px;">
								<div class="board-container" style="width: 46%; margin-right: 6%; min-height: 50px;">
									<div class="board has-chart" style="padding: 0px; min-height: 50px;">
										<div class="chart-area" style="min-height: 115px;">
											<div class="big-number videoCampaignBoardUpperText" style="color: #4cc0bf;">START</div>
											<div id="start" class="big-number videoCampaignBoardLowerText" style="color: #4cc0bf;"></div>
										</div>
									</div>
								</div>
							</div>
							
						</div>
						
						<div>
							<div class="jarviswidget-editbox">
								<div>
									<label>Title:</label> <input type="text" />
								</div>
								<div>
									<label>Styles:</label>
									<span data-widget-setstyle="purple" class="purple-btn"></span>
									<span data-widget-setstyle="navyblue" class="navyblue-btn"></span>
									<span data-widget-setstyle="green" class="green-btn"></span>
									<span data-widget-setstyle="yellow" class="yellow-btn"></span>
									<span data-widget-setstyle="orange" class="orange-btn"></span>
									<span data-widget-setstyle="pink" class="pink-btn"></span>
									<span data-widget-setstyle="red" class="red-btn"></span>
									<span data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
									<span data-widget-setstyle="black" class="black-btn"></span>
								</div>
							</div>
							
							<!-- <div id="videoCampaignLineChartDiv" ng-app="videoCampaignLineChartApp" ng-controller="videoCampaignLineChartCtrl" class="inner-spacer"> -->	
							    <div id="byEventChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
							     	<img class="chartLoaderImageClass" src="img/loaders/type4/light/46.gif" alt="loader">
							     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
							    </div> 
								
								<div id="byEventChartDiv" class="linechartdatadiv"  google-chart chart="byEventChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- </div> -->

							<!-- end content-->
						</div>
						<!-- end wrap div -->
					</div>
					<!-- end widget -->
				</article>
			</div>
		</div>
			
	</div>
	</div>
</s:if>
	  
	  <%-- <jsp:include page="newsAndResearch.jsp" /> --%>	  							
										
</div>

<!-- end content -->

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
  <div class="push"></div>
		
		
		
		
		  <!--   var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
			contentDiv = contentDiv +"<div id='traffickingPopupLoader' style='width:550px;height:30px;text-align:center'><img src='img/loaders/type4/light/46.gif' alt='loader'></div>";
			contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;'></div>";
			contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'>  </div></div>";
		 -->
		
		
		
		
	
		<!-- <div style="display:block;" class="popup_class"  ng-app="tableDataApp" ng-controller="tableDataCtrl" >
           <div  style="width:550px;">
		       <div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;">
		           <div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:43px;width:540px;'> <div style='font-weight:bold;color:black;width:160px;float:left;'>Campaign Name</div><div style='width:105px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>
		           <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:527px;"><div style="width:160px;float:left;text-align:right;" id = "campaign_name"></div><div style="width:105px;float:left;text-align:right;" id = "booked_imp"></div><div style="width:50px;float:left;margin-left:10px;text-align:right;" id="ecpm"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:90px;" id = "start_date"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:90px;" id = "end_date"><b></b></div></div>
			       <div id="trafficPopup_loader" style="width:550px;height:30px;text-align:center"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
		           <div style="width: 500px; height: 220px; margin-left:10px;" google-chart chart="progressBarChart" style="{{chart.cssStyle}}" on-ready="chartReady()" > </div> 
		           <div style="height:10px;background-color:#FDEFBC;"></div>
			   </div>
		  </div>
	  </div>
		
		</div>  -->
			
				
	<div style="display:none;" class="fluid-container" id="geoChartPopUp" ng-app="geoChartApp" ng-controller="geoChartCtrl" >
                <div id="popover_content_wrapper" style="width:550px;">
		            <div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;"></div>
		            <div>
		            <table class="table text-left">
		            	<thead>
		            	<tr>
		            	    <th style="text-align: left;">State</th>		
							<th style="text-align: left;">Site</th>
							<th style="text-align: left;">Impressions</th>
							<th style="text-align: left;">CTR</th>
		            	
		            	</tr>
		            	</thead>
		            
		            </table>
		            </div>
		            
		            <div style="height:10px;background-color:#FDEFBC;"></div>
			   </div>
	</div>
		
		
		
</div>
<!-- end of jsp code -->

<input type="hidden" name="accounts" id="advertiserId" value="<s:property value='accounts'/>"/>
<input type="hidden" name="orders" id="orderId" value="<s:property value='orders'/>"/>
	
	
 <jsp:include page="js.jsp"/>
 <script src="../js/angular/angular.min.js"></script>
 <script src="../js/angular/controller/newAdvertiserView.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="../js/angular/ng-google-chart.js"></script>
 <script src="../js/angular/controller/bar-chart-data.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="../js/angular/controller/line-chart-data.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="../js/angular/controller/geo-chart-data.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="../js/angular/controller/pie-chart-data.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="../js/angular/controller/table-data.js?v=<s:property value="deploymentVersion"/>"></script>
  <script src="../js/angular/controller/video-chart-data.js?v=<s:property value="deploymentVersion"/>"></script>
 <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.js"></script>
 <script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>
 <script type="text/javascript" src="../js/bootstrap-select.js"></script>
 <%-- <script src="../js/angular/angular.min.js"></script>
  <script src="../js/angular/controller/settingTab.js"/></script>
 <script src="../js/bootstrap-tagsinput-angular.js"></script> --%>
 
 <script src="../js/printThis.js"></script>
 
 <script> 
 
 $(document).ready(function(){		
		getUserAccountsDropDownData();
 });
 
 var app = angular.module("mainApp", []);
 app.run(function ($rootScope,$window) {
	 $window.response = $('#amount2').val(); 
     $window.barChart1 = "test";     
 }); 
 
 	  
 /* angular.element(document).ready(function() {		
	    angular.bootstrap($("#tableDataDiv"),['tableDataApp']);
 });  */

	 
	$( "#slider-range-min-ctr" ).slider({
    	change: function( event, ui ) {
    		try{
    			var scope_barChartDiv1=angular.element(document.getElementById("barChartPerfAdSizeDiv")).scope();
    			//var scope_barChartDiv2=angular.element(document.getElementById("barChartDiv2")).scope();
    			var scope_barChartByOSDiv=angular.element(document.getElementById("barChartByOSDiv")).scope();
    			var scope_barChartByDeviceDiv=angular.element(document.getElementById("barChartByDeviceDiv")).scope();
    			var scope_barChartMobileDiv=angular.element(document.getElementById("barChartMobileDiv")).scope();
    			var scope_tableDataDiv=angular.element(document.getElementById("tableDataDiv")).scope();
    			var scope_geoChartDiv=angular.element(document.getElementById("geoChartDiv")).scope();
    		 	scope_barChartDiv1.safeApply(function(){
    		 	//scope_barChartDiv1.$apply(function(){
    		 	//Either change/set the data directly using this next line
    		 	//scope_barChartDiv1.barChart1.data=data;
    		 	//OR call a function on the scope that internally adjusts the data. This might be better approach.
    		 	scope_barChartDiv1.changeCombo1();
    		 	});
    		 	
    		 /* 	scope_barChartDiv2.safeApply(function(){
    		 		scope_barChartDiv2.changeCombo2();
        		 	}); */
    		 	
    		 	scope_barChartByOSDiv.safeApply(function(){
    		 		scope_barChartByOSDiv.changeBarChartByOS();
        		 	});
    		 	
    		 	scope_barChartByDeviceDiv.safeApply(function(){
    		 		scope_barChartByDeviceDiv.changeBarChartByDevice();
        		 	});
    		 	
    		 	scope_barChartMobileDiv.safeApply(function(){
    		 		scope_barChartMobileDiv.changeBarChartMobile();
        		 	});
    		 	
    		 	scope_tableDataDiv.safeApply(function(){
    		 		scope_tableDataDiv.changeTable();
        		 	});
    		 	
    		 	scope_geoChartDiv.safeApply(function(){
    		 		scope_geoChartDiv.changeGeoChart();
        		 	});

    			console.log("Slider Changed ");

    		}
    		 	catch(err){
    			//alert('error:'+err);
    		}
   		
       	}
    }); 
	

</script>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
