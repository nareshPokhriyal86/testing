
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName"%>

<jsp:include page="Header.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>

<script>
	localStorage.clear();
	var isClient = false;
	<s:if test="%{#session.sessionDTO.client}">
	isClient = true;
	</s:if>
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
<title>ONE - Performance & Monitoring</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<jsp:include page="css.jsp" />
<script type='text/javascript' src='https://www.google.com/jsapi'></script>
<script type='text/javascript'
	src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript"
	src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/angular/angular.min.js"></script>

<link rel="stylesheet"
	href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet"
	href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet"
	href="../css/performanceAndMonitoring.css?v=<s:property value="deploymentVersion"/>">


<style>
.nav-tabs>li {
	margin-bottom: 0px;
}

.tabs-left>.nav-tabs .active>a,.tabs-left>.nav-tabs .active>a:hover,.tabs-left>.nav-tabs .active>a:focus
	{
	border: 0px;
	border-bottom: 1px solid #ECECEC;
}

.tabs-left>.nav-tabs .active>a,.tabs-left>.nav-tabs .active>a:hover,.tabs-left>.nav-tabs .active>a:focus
	{
	margin: 0px !important;
	border-radius: 0px !important;
}

.nav-tabs a,.nav-tabs a:hover,.nav-tabs a:focus {
	padding: 8px 19px !important;
	max-hight: 74px !important;
	border-radius: 0px !important;
}

.nav-tabs .active a,.nav-tabs .active a:hover,.nav-tabs .active a:focus
	{
	padding: 8px 19px !important;
	max-hight: 74px !important;
	border-radius: 0px !important;
}

.color-box {
	width: 10px;
	height: 10px;
	display: inline-block;
	background-color: #ccc;
	left: 5px;
	C: 5px;
}
</style>

</head>

<body>
	<!-- .height-wrapper -->
	<div class="height-wrapper">
		<div id="main" role="main" class="container-fluid">
			<div class="contained">
				<!-- main content -->
				<div id="page-content" class="mlr">
					<jsp:include page="navigationTab.jsp" />
					<div id="performanceViewDiv" ng-app="performanceViewChartApp"
						ng-controller="performanceViewChartCtrl"
						class="fluid-container" style="background: white;">
						<section id="widget-grid" class="themed">
							<s:form id="select-demo-js">
								<!-- filter Div Starts -->
								<div class="row-fluid" ng-cloak>
									<article class="span4">
										<h3 style="margin-left: 25px; margin-top: 27px;">{{orderInfo.orderName}}</h3>
									</article>
									
									<!-- Added by Anup - Check for Client  -->
									<article class="span4">
									
										<div class="control-group" style="border-bottom: 0px; background: #FFFFFF;margin-top: 11px;">
											<div class="controls">
				           						<select id="placementSelect" multiple="true" class="span12 with-search" onchange="placementChanged();">
				           						</select>
											</div>
										</div>
									
									</article>
									
									<article class="span4">
										<div class="span11"
											style="background: #F2F2F2; border-radius: 3px; margin: 7px; padding: 5px 10px; border: 1px solid #EBEBEB;">
											<div
												style="float: left; width: auto; padding: 5px; margin-top: 2px;">
												<label class="control-label" style="font-size: 12px;">NOISE
													FILTER :</label>
												<div id="isNoise" class="switch switch-mini"
													data-on="warning" data-off="danger"
													style="vertical-align: middle;">
													<input type="checkbox" value="1" checked />
												</div>
											</div>
											<div id="thresholdSlider" class="summary_bar"
												style="float: left; width: auto; padding: 5px; margin-left: 4%; margin-top: 5px;">
												<div class="controls">
													<input type="text" size="10" id="threshold"
														class="threshold-slider-value ui-display-label-white"
														style="width: 55px; background: transparent;" />
													<div id="" class="threshold-slider-range warning-slider"></div>
												</div>
											</div>
											<div
												style="float: left; width: auto; padding: 17px; margin-left: 4%;">
												<span style="z-index: 10;"> <a href="#"
													onclick="applyFilters()" class="btn medium btn-success">GO</a>
												</span>
											</div>
											
											<s:if test="%{!#session.sessionDTO.client}">
											<div style="float: left; margin-top: 15px; margin-left: 4%">
												<a title="Download Report" class="btn" style="height: 23px;"
													href="javascript:downLoadAdvertiserReport(orderInfo.orderId,orderInfo.orderName);">
													<i class="cus-doc-excel-table"></i>
												</a>
											</div>
											</s:if>
											
										</div>
									</article>
								</div>
								<!-- filter Div Ends -->

								<!-- header Div Starts -->
								<div class="row-fluid p_m_header" ng-cloak ng-show="isObjectAvailable(headerData.startDate)">
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.rate
											| currency:'$'}}</div>
										<div class="p_m_header_cell_lowerText">{{headerData.rateLabel}}</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{ convertStringToDate(headerData.startDate,'mm-dd-yyyy') | date:'mediumDate'}} - {{convertStringToDate(headerData.endDate,'mm-dd-yyyy') | date:'mediumDate'}}</div>
										<div class="p_m_header_cell_lowerText">Duration</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.goal
											| number}}</div>
										<div class="p_m_header_cell_lowerText">{{headerData.goalLabel}}</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.delivered
											| number}}</div>
										<div class="p_m_header_cell_lowerText">Delivered</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.clicks
											| number}}</div>
										<div class="p_m_header_cell_lowerText">Clicks</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.pending
											| number}}</div>
										<div class="p_m_header_cell_lowerText">{{headerData.pendingLabel}}</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.ctr
											| number}}%</div>
										<div class="p_m_header_cell_lowerText">CTR</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.deliveryPercentage
											| number}}%</div>
										<div class="p_m_header_cell_lowerText">Delivery %</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.budget
											| currency:'$'}}</div>
										<div class="p_m_header_cell_lowerText">Budget</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.spent
											| currency:'$'}}</div>
										<div class="p_m_header_cell_lowerText">Spent</div>
									</div>
									<div class="p_m_header_cell">
										<div class="p_m_header_cell_upperText">{{headerData.left
											| currency:'$'}}</div>
										<div class="p_m_header_cell_lowerText">{{headerData.budgetLeftLabel}}</div>
									</div>
								</div>
								<!-- header Div Ends -->

								<!-- Chart View Start -->
								<div class="row-fluid" ng-cloak>
									<article class="span12" id="for_ipad_only">
										<div class="tabbable tabs-left" style="background: #e4e7eb;">
											<ul id="tab_ul" class="nav nav-tabs, span1"
												style="float: left; height: 1800px; text-align: center; background: #ffffff; min-height: 100%; padding: 0px 5px;">
												<li class="active"><a href="#clickTab"
													data-toggle="tab" ng-click="getCtrData(true);"
													data-toggle2="tooltip" data-placement="right" title="CTR">
														<div id="ctrTabDiv" class="p_m_tabIconActive"
															style="background-position: 0 0"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">CTR</span>
												</a></li>
												<li><a href="#clickTab" data-toggle="tab"
													ng-click="getImpressionsData(true);" data-toggle2="tooltip"
													data-placement="right" title="Impression">
														<div id="impressionTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -42px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Impression</span>
												</a></li>
												<li><a href="#clickTab" ng-click="getClicksData(true);"
													data-toggle="tab" data-toggle2="tooltip"
													data-placement="right" title="Clicks">
														<div id="clicksTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -84px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Clicks</span>
												</a></li>
												<li><a href="#locationTab" data-toggle="tab"
													ng-click="getLocationData(true);" data-toggle2="tooltip"
													data-placement="right" title="Location">
														<div id="locationTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -126px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Location</span>
												</a></li>
												
												<s:if test="%{!#session.sessionDTO.client}">
												<li><a href="#clickTab" ng-click="getFlightData(true);"
													data-toggle="tab" data-toggle2="tooltip"
													data-placement="right" title="Flight Pacing">
														<div id="flightTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -168px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Flight
															Pacing</span>
												</a></li>
												</s:if>
												
												<li><a href="#creativeTab"
													ng-click="getCreativeData(true);" data-toggle="tab"
													data-toggle2="tooltip" data-placement="right"
													title="Creative Sizes">
														<div id="creativeTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -210px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Creative
															Sizes</span>
												</a></li>
												<li><a href="#deviceTab"
													ng-click="getDeviceData(true);" data-toggle="tab"
													data-toggle2="tooltip" data-placement="right"
													title="Devices">
														<div id="deviceTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -252px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Devices</span>
												</a></li>
												<li><a href="#osTab" ng-click="getOSData(true);"
													data-toggle="tab" data-toggle2="tooltip"
													data-placement="right" title="Operating System">
														<div id="osTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -294px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Operating
															System</span>
												</a></li>
												<li><a href="#richMediaTab"
													ng-click="getRichMediaData(true);" data-toggle="tab"
													data-toggle2="tooltip" data-placement="right"
													title="Rich Media">
														<div id="richMediaTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -336px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Rich
															Media</span>
												</a></li>
												<li><a href="#videoTab" ng-click="getVideoData(true);"
													data-toggle="tab" data-toggle2="tooltip"
													data-placement="right" title="Video Campaign">
														<div id="videoTabDiv" class="p_m_tabIconInactive"
															style="background-position: 0 -378px;"></div> <span
														style="font-size: 10px; color: #666666; line-height: 18px;">Video
															Campaign</span>
												</a></li>

											</ul>

											<!-- CTR, Impressions, Clicks, Flight Div starts -->
											<div id="ctrDataDiv" name="tabChartData" class="span11"
												style="overflow: hidden; height: auto; min-height: 400px; float: left; background: #e4e7eb;">
												<div id="clickTab"
													style="left: 10px; position: relative; width: 100%; height: auto; display: inline; float: left; top: 0px; left: 0px;">
													<div style="float: left; position: relative;">
														<h2 id="chartTitleDiv"
															style="font-size: 24px; color: #333; font-weight: normal;">CTR</h2>
														<p id="chartInfoDiv"
															style="font-size: 16px; font-weight: lighter; color: #666;">CTR
															Details</p>
													</div>
													<div
														style="float: right; position: relative; padding: 25px 16px;">
														<div id="flightGoalInfo" class="p_m_header_cell"
															style="border-right: 0px; visibility: hidden;">
															<div class="p_m_header_cell_upperText"
																style="color: #0081F0">{{selectedFlight.goal |
																number}}</div>
															<div class="p_m_header_cell_lowerText">{{selectedFlight.goalLabel}}</div>
														</div>
														<div id="flightDeliveredInfo" class="p_m_header_cell"
															style="border-right: 0px; visibility: hidden;">
															<div class="p_m_header_cell_upperText"
																style="color: #149455">{{selectedFlight.delivered
																| number}}</div>
															<div class="p_m_header_cell_lowerText">Delivered</div>
														</div>
														<div id="selectFilterDiv"
															style="visibility: hidden; border-bottom: 0px; background-color: transparent; float: left; position: relative; padding: 4px 24px; border-top: 0px;">
															<select
																ng-options="flight.flightName for flight in archivedFlightData"
																ng-model="selectedFlight" ng-change="flightChanged();"></select>
														</div>
													</div>


												</div>

												<div id="allChartsDiv" class="row-fluid"
													style="display: inline; top: 30px; left: -8px; background: #fff;">
													<article class="span12">
														<div class="jarviswidget" style="margin-right: 2%;">
															<div id="perfChartLoaderId"
																class="lineGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
															</div>
															<div id="perfChartNoDataId"
																class="lineGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<div id="perfChartNoDataInnerDiv"
																	style="text-align: center; margin-top: 100px; border: 0px;">
																	<h4>No Data</h4>
																</div>
															</div>
															<div class="inner-spacer" style="border-style: none;">
																<div id="perfChartDiv" class="linechartdatadiv"
																	google-chart chart="perfChart" style=""
																	on-ready="chartReady()"></div>
															</div>

															<div id="durationWiseData" style="border-style: none;">
																<a id="dayDataChart" style="color: white;"
																	ng-click="durationWiseChart('day', '');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #418BCA; margin-bottom: 30px; margin-left: 5%; font-weight: bold;">
																		DAY</div>
																</a> <a id="weekDataChart"
																	ng-click="durationWiseChart('week', '');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #F2F2F2; margin-left: 15px; font-weight: bold;">
																		WEEK</div>
																</a> <a id="monthDataChart"
																	ng-click="durationWiseChart('month', '');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #F2F2F2; margin-left: 15px; font-weight: bold;">
																		MONTH</div>
																</a>
															</div>
														</div>
													</article>
												</div>
												
												<div id="targetCards">
													<div style="position: relative; width: auto; height: auto; display: inline; top: 30px; left: -8px;" ng-show="(finalValue.target != 'NA')"  >
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: #43C087; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: white; font-size: 6.0em;">
																{{finalValue.target}}</div>
															<div
																style="background: #3CAC79; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																TARGETED VALUE</div>
														</div>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;" ng-show="(finalValue.current != 'NA')" >
															<div
																style="background: #F26C28; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: white; font-size: 6.0em;">
																{{finalValue.current}}</div>
															<div
																style="background: #D96124; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																CURRENT VALUE</div>
														</div>
														<div id="revisedPacing"
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left; visibility: hidden;" ng-show="(finalValue.revised != 'NA')" >
															<div
																style="background: #8F7FD4; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: white; font-size: 6.0em;">
																{{finalValue.revised}}</div>
															<div
																style="background: #8072BE; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																REVISED VALUE</div>
														</div>
													</div>
												</div>
												
												<!-- Delivery Metrics Starts -->
												<div class="row-fluid"
													style="width: auto; height: auto; display: inline; top: 30px; left: -8px;">
													<article class="span12" style="margin-top: 40px;">
														<div class="jarviswidget" style="margin-right: 2%;">
															<header>
																<h2>DAY TO DAY</h2>
															</header>

															<div class="inner-spacer">
																<table class="table dtable" id="deliveryMetrics">
																	<thead>
																		<tr>
																			<th style="width: 10%;">Date</th>
																			<th style="width: 18%;">Name</th>
																			<th style="width: 9%;">Goal</th>
																			<th style="width: 9%;">Impressions</th>
																			<th style="width: 9%;">Clicks</th>
																			<th style="width: 9%;">CTR %</th>
																			<th style="width: 9%;">% Delivered</th>
																			<th style="width: 9%;">Expected Quantity</th>
																			<th style="width: 9%;">Current Pacing</th>
																			<th style="width: 9%;">Revised Pacing</th>
																		</tr>
																	</thead>
																	<tbody>

																	</tbody>
																</table>
															</div>
														</div>
													</article>
													<div id="deliveryMetricsLoader" class="span12"
														style="text-align: center; margin-top: 10px;">
														<img src="img/loaders/type4/light/46.gif" alt="loader">
													</div>
												</div>
												<!-- Delivery Metrics Ends -->
												<div style="margin-bottom: 18px; color: white;">.</div>

											</div>
											<!-- CTR, Impressions, Clicks, Flight Div Ends -->

											<!-- Location Div starts -->
											<div id="locationDataDiv" name="tabChartData" class="span11"
												style="display: none; overflow: hidden; height: auto; min-height: 400px; float: left; background: #e4e7eb;">
												<div id="locationTab"
													style="left: 10px; position: relative; width: 100%; height: auto; display: inline; float: left; top: 0px; left: 0px;">
													<div style="float: left; position: relative;">
														<h2
															style="font-size: 24px; color: #333; font-weight: normal;">Location</h2>
														<p
															style="font-size: 16px; font-weight: lighter; color: #666;">Location
															Based Details</p>
													</div>
												</div>

												<div class="row-fluid"
													style="display: inline; top: 30px; left: -8px; background: #fff;">
													<article class="span12">
														<div class="jarviswidget" style="margin-right: 2%;">
															<div id="locationChartLoaderId"
																class="lineGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
															</div>
															<div id="locationChartNoDataId"
																class="lineGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<div
																	style="text-align: center; margin-top: 100px; border: 0px;">
																	<h4>No Data</h4>
																</div>
															</div>
															<div class="inner-spacer" style="border-style: none;">
																<div id="locationChartDiv" class="linechartdatadiv"
																	google-chart chart="geoChart" style=""
																	on-ready="chartReady()"></div>
															</div>
														</div>
													</article>
												</div>

												<div
													style="position: relative; width: auto; height: auto; display: inline; top: 30px; left: -8px;">
													
													<s:if test="%{!#session.sessionDTO.client}">
														<div style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: #8F7FD4; border-radius: 3px 3px 0px 0px; padding: 41px 80px; text-align: center; vertical-align: middle; color: white;">
																<br>
																<div style="font-size: 4em">{{locationCard.withInGeo.impressionPercentage}}</div>
																<br>
																<div style="font-size: 2em">{{locationCard.withInGeo.impressions}}</div>
															</div>
															<div
																style="background: #8072BE; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																WITH IN GEO</div>
														</div>
														<div style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: #DA534F; border-radius: 3px 3px 0px 0px; padding: 41px 80px; text-align: center; vertical-align: middle; color: white;">
																<br>
																<div style="font-size: 4em">{{locationCard.outsideGeo.impressionPercentage}}</div>
																<br>
																<div style="font-size: 2em">{{locationCard.outsideGeo.impressions}}</div>
															</div>
															<div
																style="background: #C44B47; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																OUTSIDE GEO</div>
															<br>
														</div>
													</s:if>
													
													<div
														style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
														<div
															style="background: #FAFAFA; border-radius: 0 0 3px 3px; padding: 15px; text-align: left; vertical-align: middle; color: black; font-size: 2.0em;">
															Top 5 Cities</div>
														<div
															style="background: #FFFFFF; border-radius: 3px 3px 0px 0px; font-size: 1em; padding: 11px 30px; min-height: 140px; color: font-size: 1.5em;">
															<table>
																<tr data-ng-repeat="topCity in locationCard.topCities">
																	<td
																		style="text-align: left; color: black; width: 50%; padding: 0px 5px; background-color: {{topCity.isOutsideTarget">{{topCity.cityName}}</td>
																	<td
																		style="text-align: right; color: blue; width: 25%; padding: 0px 5px; background-color: {{topCity.isOutsideTarget">{{topCity.impression}}</td>
																	<td
																		style="text-align: right; color: blue; width: 25%; padding: 0px 5px; background-color: {{topCity.isOutsideTarget">{{topCity.ctr}}</td>
																</tr>
															</table>
														</div>
													</div>
												</div>
											</div>
											<!-- Location Div Ends -->

											<!-- Rich Media Div Starts -->

											<div id="richMediaDataDiv" name="tabChartData" class="span11"
												style="display: none; overflow: hidden; height: auto; min-height: 400px; float: left; background: #e4e7eb; margin-left: 20px;">
												<div id="richMediaTab"
													style="left: 10px; position: relative; width: 100%; height: auto; display: inline; float: left; top: 0px; left: 0px;">
													<div style="float: left; position: relative;">
														<h2
															style="font-size: 24px; color: #333; font-weight: normal;">RICH
															MEDIA</h2>
														<p
															style="font-size: 16px; font-weight: lighter; color: #666;">Details
															for Rich Media</p>
													</div>
													<div
														style="float: right; position: relative; padding: 25px 16px;">
														<div id="selectRichMediaEventDiv"
															style="border-bottom: 0px; background-color: transparent; float: left; position: relative; padding: 4px 24px; border-top: 0px;">
															<select
																ng-options="customEvents.eventName for customEvents in archivedRichMediaData.lineChartData"
																ng-model="selectedCustomEvent"
																ng-change="richMediaEventChanged();"></select>
														</div>
													</div>
												</div>

												<div id="richMediaChartNoDataId"
													class="lineGraphloaderArea jarviswidget"
													style="display: none; text-align: center; border-style: none;">
													<img src="img/oops.png" alt="loader">
													<div
														style="text-align: center; background: transparent !important; border: 0px;">
														<h1>No Rich Media Data is Available for this Campaign</h1>
													</div>
												</div>
												<div id="richMediaChartShowDataId" class="row-fluid"
													style="display: inline; top: 30px; left: -8px; background: #fff;">
													<article class="span12" id="donutupper">
														<div class="jarviswidget"
															style="margin-right: 2%; width: 100%;">
															<div id="richMediaChartLoaderId"
																class="lineGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
															</div>
															<div class="inner-spacer" style="border-style: none;">
																<div id="richMediaChartDiv" class="linechartdatadiv"
																	google-chart chart="perfChart" style=""
																	on-ready="chartReady()"></div>
															</div>

															<div id="richMediadurationWiseData"
																style="border-style: none;">
																<a id="richMediadayDataChart" style="color: white;"
																	ng-click="durationWiseChart('day', 'richMedia');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #418BCA; margin-bottom: 30px; margin-left: 5%; font-weight: bold;">
																		DAY</div>
																</a> <a id="richMediaweekDataChart"
																	ng-click="durationWiseChart('week', 'richMedia');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #F2F2F2; margin-left: 15px; font-weight: bold;">
																		WEEK</div>
																</a> <a id="richMediamonthDataChart"
																	ng-click="durationWiseChart('month', 'richMedia');">
																	<div class="p_m_header_cell"
																		style="border-right: 0px; background: #F2F2F2; margin-left: 15px; font-weight: bold;">
																		MONTH</div>
																</a>
															</div>
														</div>
													</article>
												</div>

												<!-- Rich media Donut charts -->
												<div id="richMediaDonutsDivId" class="row-fluid"
													style="float: left; position: relative; margin-top: 4px; width: 102%;">
													<article class="span6" id="richrespo"
														style="margin-left: 0%; margin-right: 1%;"
														data-ng-repeat="donutData in richMediaDonutData">
														<div class="jarviswidget"
															style="margin-right: 2%; background: #fff;">
															<article class="span8" style="">
																<div class="inner-spacer"
																	style="border-style: none; overflow: hidden;">
																	<div id="richMediaHeader{{$index}}"
																		style="margin-top: 25px; margin-bottom: -4px; margin-left: 35%; font-family: Arial; font-size: 1.1em; font-weight: bold; color: rgb(148, 148, 148);"></div>
																	<div id="richMediaDonutData{{$index}}"
																		class="donutchartdatadiv"
																		style="height: 350px; width: 100%;"></div>

																</div>
																<!-- {{dynamicDonut($index, donutData.donutJson)}} -->
															</article>
															<article id="richMediaDonutChartlagendDiv" class="span3"
																style="background: #FAFAFA; height: 406px; float: right; width: 170px; overflow: auto; padding-right: 17px;">

																<div
																	style="padding-top: 40px; margin-bottom: 23px; padding-right: 1px;">
																	<span> <span
																		style="color: #418bca; float: right; font-size: 1.7em; font-weight: 300;">
																			{{ donutData.totalCount }} </span> <br /> <span
																		style="padding-left: 14%; float: right; color: rgb(148, 148, 148); font-size: 1.1em;">For
																			All Sizes</span>
																	</span>
																</div>
																<div
																	data-ng-repeat="lagendData in donutData.jsonLagendArray"
																	style="padding-top: 24px; margin-left: 25px; padding-bottom: 27px;">

																	<span style="padding-bottom: 21px;">
																		<div>
																			<span class="color-box"
																				ng-style="setLagendColor(lagendData.color)"
																				style="margin-right: 18px; margin-bottom: -10px;"></span>
																		</div>
																		<div>
																			<span
																				style="float: right; font-size: 1.7em; font-weight: 300; color: rgb(119, 117, 117);">{{
																				lagendData.count | number }}</span></br> <span
																				style="font-size: 1.1em;color: rgb(148, 148, 148);float: right;<%-- padding-right: 4px; --%>">{{
																				lagendData.event}}</span> </br> <span
																				style="font-size: 13px; font-weight: bold; color: rgb(148, 148, 148); float: right;">{{
																				lagendData.creative }}</span>
																		</div>
																	</span>
																</div>
															</article>


														</div>
													</article>
												</div>
												<!-- Rich media Donut charts ends -->

												<div style="margin-bottom: 18px; color: white;">.</div>

											</div>
											<!-- Rich Media Div Ends -->

											<!-- Creative Div Starts -->
											<div id="creativeDataDiv" name="tabChartData" class="span11"
												style="display: none; overflow: hidden; height: auto; min-height: 400px; float: left; background: #e4e7eb; margin-left: 20px;">
												<div id="creativeTab"
													style="left: 10px; position: relative; width: 100%; height: auto; display: inline; float: left; top: 0px; left: 0px;">
													<div style="float: left; position: relative;">
														<h2 id="barChartTitleDiv"
															style="font-size: 24px; color: #333; font-weight: normal;">CREATIVE
															SIZES</h2>
														<p id="barChartInfoDiv"
															style="font-size: 16px; font-weight: lighter; color: #666;">Details
															According to Creative Sizes</p>
													</div>
													<div
														style="float: right; position: relative; padding: 25px 16px;">
														<div id="filterDiv"
															style="visibility: hidden; border-bottom: 0px; background-color: transparent; float: left; position: relative; padding: 4px 24px; border-top: 0px;">
															<select
																ng-options="flight.flightName for flight in archivedFlightData"
																ng-model="selectedFlight" ng-change="flightChanged();"></select>
														</div>
													</div>


												</div>

												<div id="barChartsDiv" class="row-fluid"
													style="display: inline; top: 30px; left: -8px; background: #fff;">
													<article class="span12">
														<div class="jarviswidget"
															style="margin-right: 2%; margin-bottom: 2px;">

															<div id="barchartdataSelectBox"
																style="margin-top: 5px; font-family: Arial, Helvetica, sans-serif; border: none !important;">
																<label class="control-label" for="chartType"
																	style="width: auto; float: left; margin: 20px;"
																	id="selectBoxLabel">Performance By Creative
																	Size</label>
																<div class="controls">
																	<select id="chartType"
																		style="float: right; margin: 20px; border-radius: 0px; background: #F4F4F4;"
																		ng-init="displaydatatype=CTR"
																		ng-model="displaydatatype"
																		ng-change="chartSelectionChange()">
																		<option value="CTR" style="background: #fff;">CTR</option>
																		<option value="Impressions" style="background: #fff;">Impressions</option>
																		<option value="Clicks" style="background: #fff;">Clicks</option>
																	</select>
																</div>
															</div>
															<div id="barChartLoaderId"
																class="barGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
															</div>
															<div id="barChartNoDataId"
																class="barGraphloaderArea jarviswidget"
																style="display: none; text-align: center; border-style: none;">
																<div
																	style="text-align: center; margin-top: 100px; border: 0px;">
																	<h4>No Data</h4>
																</div>
															</div>
															<div class="inner-spacer" style="border-style: none;">
																<div id="barChartDiv" class="barchartdatadiv"
																	google-chart chart="barChart" style=""
																	on-ready="chartReady()"></div>
															</div>

														</div>
													</article>
												</div>
												
												<s:if test="%{!#session.sessionDTO.client}">
													<div class="row-fluid"
														style="position: relative; height: auto; top: 26px; left: -3px;">
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: #43C087; border-radius: 3px 3px 0px 0px; padding: 41px 80px; text-align: center; vertical-align: middle; color: white;">
																<br>
																<div style="font-size: 3em;">{{targetValue.targetPercentage}}</div>
																<br>
																<div style="font-size: 1.5em;">{{targetValue.targetGoal}}</div>
																<div style="font-size: 1em">{{targetValue.targetCreative}}</div>
															</div>
															<div id="targetedValueDiv"
																style="background: #3CAC79; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																TARGETED DEVICES</div>
														</div>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: #F26C28; border-radius: 3px 3px 0px 0px; padding: 41px 80px; text-align: center; vertical-align: middle; color: white;">
																<br>
																<div style="font-size: 3em;">{{targetValue.nonTargetPercentage}}</div>
																<br>
																<div style="font-size: 1.5em;">{{targetValue.nonTargetGoal}}</div>
																<div style="font-size: 1em">{{targetValue.nonTargetCreative}}</div>
															</div>
															<div id="nontargetedValueDiv"
																style="background: #D96124; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.5em; font-weight: bold;">
																NON TARGETED SIZE</div>
														</div>
													</div>
												</s:if>
												
												<!-- Start of OS Donut Chart -->
												
												
												<div id="donutChartsDiv" class="row-fluid"
													style="float: left; position: relative; margin-top: 20px; width: 102%;">
													
													<!-- Impression -->
													<article class="span6" id="" style="margin-left: 0%; margin-right: 1%;">
														<div id="donutChartLoaderId" class="donutGraphloaderArea jarviswidget" style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
														</div>
														<div class="jarviswidget" style="margin-right: 2%; background: #fff;">
															
															<article class="span8" style="">
																<div class="inner-spacer" style="border-style: none; overflow: hidden;">
																	<div id="donutImpressionHeader" style="margin-top: 25px; margin-bottom: -4px; margin-left: 35%; font-family: Arial; font-size: 1.1em; font-weight: bold; color: rgb(148, 148, 148);">
																	</div>
																	<div id="donutImpressionChart" style="height: 350px; width: 100%;">
																	</div>
																</div>
															</article>
															
															<article id="" class="span3" style="background: #FAFAFA; height: 406px; float: right; width: 170px; overflow: auto; padding-right: 17px;">
																<div data-ng-repeat="impByCre in impByCreative " style="padding-top: 24px; margin-left: 25px; padding-bottom: 27px;">
																	<span style="padding-bottom: 21px;">
																		<div>
																			<span class="color-box" ng-style="setLagendColor(impByCre.color)" style="margin-right: 18px; margin-bottom: -10px;"></span>
																		</div>
																		<div>
																			<span style="float: right; font-size: 1.7em; font-weight: 300; color: rg b(119, 117, 117);">
																			{{impByCre.impressions| number}}
																			</span>
																			</br> 
																			<span style="font-size: 13px; font-weight: bold; color: rgb(148, 148, 148); float: right;">
																			{{impByCre.creative}}
																			</span>
																			
																		</div>
																	</span>
																</div>
															</article>
														</div>
													</article>
													
													
													<!-- Click -->
													
													<article class="span6" id="" style="margin-left: 0%; margin-right: 1%;">
														<div id="donut2ChartLoaderId" class="donutGraphloaderArea jarviswidget" style="display: none; text-align: center; border-style: none;">
																<img src="img/loaders/type4/light/46.gif" alt="loader">
														</div>
														<div class="jarviswidget" style="margin-right: 2%; background: #fff;">
															<article class="span8" style="">
																<div class="inner-spacer" style="border-style: none; overflow: hidden;">
																	<div id="donutClickHeader" style="margin-top: 25px; margin-bottom: -4px; margin-left: 35%; font-family: Arial; font-size: 1.1em; font-weight: bold; color: rgb(148, 148, 148);">
																	</div>
																	<div id="donutClickChart" style="height: 350px; width: 100%;">
																	</div>
																</div>
															</article>
															
															<article id="" class="span3" style="background: #FAFAFA; height: 406px; float: right; width: 170px; overflow: auto; padding-right: 17px;">
																<div data-ng-repeat="clickByCre in clicksByCreative" style="padding-top: 24px; margin-left: 25px; padding-bottom: 27px;">
																	<span style="padding-bottom: 21px;">
																		<div>
																			<span class="color-box" ng-style="setLagendColor(clickByCre.color)" style="margin-right: 18px; margin-bottom: -10px;"></span>
																		</div>
																		<div>
																			<span style="float: right; font-size: 1.7em; font-weight: 300; color: rg b(119, 117, 117);">
																			{{clickByCre.clicks| number}}
																			</span>
																			</br> 
																			<span style="font-size: 13px; font-weight: bold; color: rgb(148, 148, 148); float: right;">
																			{{clickByCre.creative}}
																			</span>
																			
																		</div>
																	</span>
																</div>
															</article>
														</div>
													</article>
													
													
													
												</div>

												<!-- End of OS Donut Chart -->

												<div style="margin-bottom: 18px; color: white;">.</div>

											</div>
											<!-- Creative Div Ends -->

											<!-- Video Metrics Div starts -->
											<div id="videoDataDiv" name="tabChartData" class="span11"
												style="display: none; overflow: hidden; height: auto; min-height: 400px; float: left; background: #e4e7eb;">
												<div id="videoTab"
													style="left: 10px; position: relative; width: 100%; height: auto; display: inline; float: left; top: 0px; left: 0px;">
													<div style="float: left; position: relative;">
														<h2
															style="font-size: 24px; color: #333; font-weight: normal;">VIDEO
															CAMPAIGN</h2>
														<p
															style="font-size: 16px; font-weight: lighter; color: #666;">Details
															for Video Campaign</p>
													</div>
												</div>

												<div id="videoChartNoDataId"
													class="lineGraphloaderArea jarviswidget"
													style="display: none; text-align: center; border-style: none;">
													<img src="img/oops.png" alt="Oops">
													<div
														style="text-align: center; background: transparent !important; border: 0px;">
														<h1>No Video Data is Available for this Campaign</h1>
													</div>
												</div>
												<div id="videoChartLoaderId"
													class="lineGraphloaderArea jarviswidget"
													style="display: none; text-align: center; border-style: none;">
													<img src="img/loaders/type4/light/46.gif" alt="Loading...">
												</div>
												<div id="videoCreativesShowDataId" class="row-fluid"
													style="display: inline; top: -12px; left: -8px; background: #fff;">
													<h2
														style="font-size: 24px; color: #333; font-weight: normal;">By
														Rate</h2>
													<article id="byRateChart" class="span7"
														style="margin-left: 0px;">
														<div class="jarviswidget">
															<div class="inner-spacer" style="border-style: none;">
																<div id="videoChartDiv" class="linechartdatadiv"
																	google-chart chart="videoChart" style=""
																	on-ready="chartReady()"></div>
															</div>
														</div>
													</article>
													<div id="byRateCards"
														style="position: relative; left: -13px; width: 100%; height: auto;">
														<div class="span2"
															style="margin-left: 30px; margin-bottom: 21px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
															<div
																style="background: #43C087; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
																{{videoCardData.averageInteractionRate}}</div>
															<div
																style="background: #3CAC79; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
																<span>AVERAGE</span><br> <span>INTERACTION
																	RATE</span>
															</div>
														</div>
														<div class="span2"
															style="margin-left: 15px; margin-bottom: 21px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
															<div
																style="background: #F26C28; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
																{{videoCardData.averageViewRate}}</div>
															<div
																style="background: #D96124; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
																<span>AVERAGE</span><br> <span>VIEW RATE</span>
															</div>
														</div>
														<div class="span2"
															style="margin-left: 30px; margin-bottom: 21px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
															<div
																style="background: #8F7FD4; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
																{{videoCardData.averageCompletionRate}}</div>
															<div
																style="background: #8072BE; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
																<span>AVERAGE</span><br> <span>COMPLETION
																	RATE</span>
															</div>
														</div>
														<div class="span2"
															style="margin-left: 15px; margin-bottom: 21px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
															<div
																style="background: #DA534F; border-radius: 3px 3px 0px 0px; padding: 36.5px 0px; text-align: center; vertical-align: middle; color: white; font-size: 2.5em;">
																<span>{{videoCardData.averageViewTime}}</span><br>
																<span style="font-size: 0.5em;">{{videoCardData.averageViewTimeUnit}}</span>
															</div>
															<div
																style="background: #C44B47; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
																<span>AVERAGE</span><br> <span>VIEW TIME</span>
															</div>
														</div>
													</div>

													<div id="byEventCards"
														style="position: relative; width: 100%; height: auto; float: left; top: 30px;">
														<h2
															style="font-size: 24px; color: #333; font-weight: normal;">By
															Events</h2>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -600px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.start}}</div>
																<div class="p_m_video_Event_lowerText">Start</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: 0 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.pause}}</div>
																<div class="p_m_video_Event_lowerText">Pause</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -100px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.resume}}</div>
																<div class="p_m_video_Event_lowerText">Resume</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -200px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.rewind}}</div>
																<div class="p_m_video_Event_lowerText">Rewind</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -300px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.mute}}</div>
																<div class="p_m_video_Event_lowerText">Mute</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -400px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.unMute}}</div>
																<div class="p_m_video_Event_lowerText">Unmute</div>
															</div>
														</div>
														<div
															style="background: white; margin-right: 16px; margin-bottom: 16px; position: relative; display: inline; float: left;">
															<div class="p_m_videoIcon"
																style="background-position: -500px 0; position: relative; float: left;">
															</div>
															<div
																style="padding: 30px 14px; text-align: right; position: relative; float: left; min-width: 130px;">
																<div class="p_m_video_Event_upperText">{{videoCardData.fullScreen}}</div>
																<div class="p_m_video_Event_lowerText">Full Screen
																</div>
															</div>
														</div>
													</div>

													<div id="byLengthCards"
														style="position: relative; display: inline; width: 100%; height: auto; float: left; top: 46px;">
														<h2
															style="font-size: 24px; color: #333; font-weight: normal;">By
															Length</h2>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: white; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: #666666; font-size: 3.5em;">
																{{videoCardData.firstQuartile}}</div>
															<div
																style="background: #F2F2F2; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: #AEAEAE; font-size: 1.5em; font-weight: bold;">
																FIRST QUARTILE</div>
														</div>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: white; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: #666666; font-size: 3.5em;">
																{{videoCardData.midPoint}}</div>
															<div
																style="background: #F2F2F2; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: #AEAEAE; font-size: 1.5em; font-weight: bold;">
																MIDPOINT</div>
														</div>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: white; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: #666666; font-size: 3.5em;">
																{{videoCardData.thirdQuartile}}</div>
															<div
																style="background: #F2F2F2; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: #AEAEAE; font-size: 1.5em; font-weight: bold;">
																THIRD QUARTILE</div>
														</div>
														<div
															style="margin-right: 16px; margin-bottom: 16px; position: relative; width: auto; height: auto; display: inline; float: left;">
															<div
																style="background: white; border-radius: 3px 3px 0px 0px; padding: 70px; text-align: center; vertical-align: middle; color: #666666; font-size: 3.5em;">
																{{videoCardData.complete}}</div>
															<div
																style="background: #F2F2F2; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: #AEAEAE; font-size: 1.5em; font-weight: bold;">
																COMPLETE</div>
														</div>
													</div>

												</div>
												<div id="doubleClickRichMediaShowDataId" class="row-fluid"
													style="display: inline; top: -12px; left: -8px; background: #fff;">
													<h2
														style="font-size: 24px; color: #333; font-weight: normal;">Rich
														Media Video</h2>
													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #43C087; border-radius: 3px 3px 0px 0px; padding: 22px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															<div style="margin-top: 13px;">{{videoCardData.rmInteractionTime}}</div>
															<br>
															<div style="font-size: 0.5em;">Seconds</div>
														</div>
														<div
															style="background: #3CAC79; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>INTERACTION TIME</span>
														</div>
													</div>
													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #F26C28; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															{{videoCardData.rmInteractionCount}}</div>
														<div
															style="background: #D96124; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>INTERACTION COUNT</span>
														</div>
													</div>
													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #8F7FD4; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															{{videoCardData.rmInteractionRate}}</div>
														<div
															style="background: #8072BE; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>INTERACTION RATE</span>
														</div>
													</div>

													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #DA534F; border-radius: 3px 3px 0px 0px; padding: 22px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															<div style="margin-top: 13px;">{{videoCardData.rmAvgInteractionTime}}</div>
															<br>
															<div style="font-size: 0.5em;">Seconds</div>
														</div>
														<div
															style="background: #C44B47; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>AVERAGE INTERACTION TIME</span>
														</div>
													</div>
													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #F6A54E; border-radius: 3px 3px 0px 0px; padding: 48px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															{{videoCardData.rmInteractionImpressions}}</div>
														<div
															style="background: #eb7f0d; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>INTERACTION IMPRESSIONS</span>
														</div>
													</div>
													<div class="span4"
														style="margin-left: 0px; margin-right: 10px; margin-bottom: -15px; position: relative; min-width: 19%; min-height: 192px; display: inline;">
														<div
															style="background: #5DC6C5; border-radius: 3px 3px 0px 0px; padding: 22px 0px; text-align: center; vertical-align: middle; color: white; font-size: 3.0em;">
															<div style="margin-top: 13px;">{{videoCardData.rmAverageDisplayTime}}</div>
															<br>
															<div style="font-size: 0.5em;">Seconds</div>
														</div>
														<div
															style="background: #4aaebf; border-radius: 0 0 3px 3px; padding: 15px; text-align: center; vertical-align: middle; color: rgba(225, 225, 225, 0.9); font-size: 1.2em; font-weight: bold;">
															<span>AVERAGE DISPLAY TIME</span>
														</div>
													</div>
												</div>
												<div style="margin-bottom: 18px; color: white;">.</div>
											</div>
											<!-- Video Metrics Div Ends -->

										</div>

									</article>
								</div>
								<!-- Chart View End -->
							</s:form>
						</section>
					</div>
				</div>
				<!-- end main content -->
				<!-- </div> -->
			</div>
			<!--end fluid-container-->
		</div>
	</div>
	<!-- end of jsp code -->


	<jsp:include page="js.jsp" />
	<script src="../js/angular/ng-google-chart.js"></script>
	<script
		src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>
	<script type="text/javascript" src="../js/bootstrap-select.js"></script>

	<script src="../js/angular/controller/performanceMonitoringLineChartData.js?v=<s:property value="deploymentVersion"/>"></script>

	<script>
		var placementSelectChanged = true;
		var isNoise = true;
		var threshold = 0.000;
		var placementJson = eval(<s:property escape="false" value="placementLineItems"/>);
		var orderInfo = eval(<s:property escape="false" value="orderInfo"/>);
		var partnerInfo = eval(<s:property escape="false" value="partnerInfo"/>);
		var lineItemPlacementIds = eval(<s:property escape="false" value="lineItemPlacementIds"/>);
		var lineItemPlacementName = eval(<s:property escape="false" value="lineItemPlacementName"/>);
		var publisherIdInBQ = <s:property value="publisherBQId"/>;

		var ctrChartName = "CTR";
		var impressionsChartName = "Impressions";
		var clicksChartName = "Clicks";
		var locationChartName = "Location";
		var flightChartName = "Flight Pacing";
		var creativeChartName = "Creatives";
		var deviceChartName = "Devices";
		var osChartName = "OS";
		var richMediaChartName = "Rich Media";
		var videoChartName = "Video";

		var selectedTab = ctrChartName;
		
		var redirectURL = '<s:property value="redirectURL"/>';
		var backend = '<s:property value="backend"/>';
		var backendURL = "";
		if(redirectURL.indexOf('http://') == 0) {
			backendURL = redirectURL.replace('http://', 'http://'+backend+'.');
		}else {
			backendURL = backend + '.' + redirectURL;
		}
		console.log("backendURL : "+backendURL);
		
		/* Below function is called by select2 call back function and returns formatted result.
		Here it is used to set the tooltip and text of selected options. */
		function formatSelectedOption(item) {
	        var originalText = item.text;
	        var textToShow = shortenText(originalText, 15);
	        return "<div title ='" + originalText + "'>" + textToShow + "</div>";
		}

		$(document).ready(function() {
			/* select2 constructor to define call-back function parameters - formatSelection and formatResult
			that can be used to render the current selection and the result. */
			$('#placementSelect').select2({
    	        formatSelection: formatSelectedOption
    	    });
			
			try {
				$('#advertiserViewLi').attr('class', 'main-nav-li_selected');
	
				$('#isNoise').on('switch-change',function(e, data) {
					isNoise = data.value;
					if (isNoise) {
						$('#thresholdSlider').css("visibility","visible");
					} else {
						$('#thresholdSlider').css("visibility","hidden");
					}
				});
	
				$(".threshold-slider-range").slider({
					range : "min",
					step : 0.001,
					value : <s:property value='defaultThreshold'/>,
					min : 0.001,
					max : 0.1,
					slide : function(event, ui) {
						$(".threshold-slider-value").val(ui.value.toFixed(3) + "%");
					}
				});
				$(".threshold-slider-value").val($(".threshold-slider-range").slider("value").toFixed(3) + "%");
	
				if (!setFilters()) {
					return;
				}
	
				var performanceViewScope = angular.element(document.getElementById("performanceViewDiv")).scope();
				performanceViewScope.placementInfo = placementJson;
				performanceViewScope.orderInfo = orderInfo;
				performanceViewScope.partnerInfo = partnerInfo;
				performanceViewScope.lineItemPlacementIds = lineItemPlacementIds;
				performanceViewScope.lineItemPlacementName = lineItemPlacementName;
				
				$.each(placementJson, function(index,value) {
					var placementName = value.placementName;
					var textToShow = shortenText(value.placementName, 15);
					if(index == 0) {
						$('#placementSelect').append('<option id="'+index+'" value="'+index+'" selected="selected">'+placementName+'</option>');
						$('#placementSelect').select2('val',[0]);
					}else {
						$('#placementSelect').append('<option id="'+index+'" value="'+index+'">'+placementName+'</option>');
					}
				});
				
				performanceViewScope.setAllLineItems();
				$(window).resize(function() {
					performanceViewScope.makeDynamicDonut();
				});
			} catch (err) {
				console.log("error:" + err);
			}
		});
		
		function shortenText(text, maxLength) {
			var returnVal = ""; 
			if(text != null && text != undefined && $.trim(text) != '') {
				returnVal = $.trim(text);
				if(returnVal.length > 15) {
					return returnVal.substring(0,13)+'..';
				}
			}
			return returnVal;
		}
		
		function setFilters() {
			try {
				threshold = $.trim($('#threshold').val());
				if (threshold != undefined && threshold != null) {
					threshold = threshold.replace("%", "");
				}
				if (isNoise && isNaN(threshold)) {
					toastr.error('Threshold should be a number.');
					return false;
				}
			} catch (err) {
				console.log("error:" + err);
			}
			return true;
		}
		
		function placementChanged() {
			placementSelectChanged = true;
			var val = $('#placementSelect').val();
			if(val != null && val != undefined && $.inArray('0', val) >= 0) {	// if found
				$('#placementSelect').val(0);
				$('#placementSelect').select2('val',[0]);
			}
		}

		function applyFilters() {
			try {
				if (!setFilters()) {
					return;
				}
				var scope = angular.element(document.getElementById("performanceViewDiv")).scope();
				
				scope.selectedPlacements = [];
				scope.selectedPlacementIds = '';
				var placementIdArr = $('#placementSelect').val();
				console.log("Selected placement index : "+placementIdArr);
				if(placementIdArr != null && placementIdArr != undefined && placementIdArr.length > 0) {
					$.each(placementIdArr, function(index, value) {
						  scope.selectedPlacements.push(scope.placementInfo[value]);
						  scope.selectedPlacementIds = scope.selectedPlacementIds + scope.placementInfo[value].placementId + ',';
					});
					if(scope.selectedPlacementIds.lastIndexOf(',') != -1) {
						scope.selectedPlacementIds = scope.selectedPlacementIds.substring(0, scope.selectedPlacementIds.lastIndexOf(','));
					}
					console.log('scope.selectedPlacementIds : '+scope.selectedPlacementIds);
					scope.loadAllData(placementSelectChanged);
					placementSelectChanged = false;
				}else {
					toastr.error("Select atleast one placement");
					return;
				}
			} catch (err) {
				console.log("error:" + err);
			}
		}
		
		function downLoadAdvertiserReport(campaignId, campaignName) {
			try {
				<%-- This request might take more than a minute, we will have to send this to backend --%>
				location.href = backendURL+"/advertiserReportObject.lin?campaignId=" + campaignId
						+ "&campaignName=" + campaignName;
			} catch (exception) {
				console.log("Error in downLoadAdvertiserReport : " + exception);
			}
		}
		
	</script>

</body>
<jsp:include page="googleAnalytics.jsp" />
</html>
