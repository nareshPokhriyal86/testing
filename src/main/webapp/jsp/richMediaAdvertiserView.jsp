<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<jsp:include page="Header.jsp" />
<s:set name="theme" value="'simple'" scope="page" />
<!DOCTYPE html>
<html lang="en">
<head>

<script>
	localStorage.clear();
</script>
<jsp:include page="richMediaWidget.jsp" />
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
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- Le CSS
    ================================================== -->
<jsp:include page="css.jsp" />
<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>
<script type='text/javascript' src='https://www.google.com/jsapi'></script>
<script src="../js/common-graphs.js?v=<s:property value="deploymentVersion"/>"></script>

<script type="text/javascript">
	function hideDonut() {
		$('#' + advertiserDonutGraph).css({
			"display" : "none"
		});
	}
	localStorage.clear();
</script>


</head>

<body>
	<!-- .height-wrapper -->
	<div class="height-wrapper">

		<div id="main" role="main" class="container-fluid">

			<div class="contained">

				<!-- main content -->
				<div id="page-content" class="mlr">

					<jsp:include page="filter.jsp" />

					<!-- tabs view -->
					<div class="row-fluid">
						<article class="span12">
							<!-- new widget -->
							<div class="jarviswidget">
								<!-- widget div-->
								<div>
									<!-- widget edit box -->
									<div class="jarviswidget-editbox">
										<div>
											<label>Title:</label> <input type="text" />
										</div>

										<div>
											<label>Styles:</label> <span data-widget-setstyle="purple"
												class="purple-btn"></span> <span
												data-widget-setstyle="navyblue" class="navyblue-btn"></span>
											<span data-widget-setstyle="green" class="green-btn"></span>
											<span data-widget-setstyle="yellow" class="yellow-btn"></span>
											<span data-widget-setstyle="orange" class="orange-btn"></span>
											<span data-widget-setstyle="pink" class="pink-btn"></span> <span
												data-widget-setstyle="red" class="red-btn"></span> <span
												data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
											<span data-widget-setstyle="black" class="black-btn"></span>
										</div>
									</div>
									<!-- end widget edit box -->

									<div class="inner-spacer widget-content-padding">
										<!-- content -->
										<ul class="nav nav-tabs upper_tabs" id="myTab1">
											<li class="active"><a href="#s1"
												onclick="showDonut();onAdvertiserTabClick(this); getText(this);"
												id="per_sum" data-toggle="tab">Performance Summary</a>
											</li>
											<li><a href="#s2"
												onclick="hideDonut(); getText(this);onAdvertiserTabClick(this);"
												id="tre_ana" data-toggle="tab">Trends and Analysis</a>
											</li>
											<!-- <li  >
											<a href="#s3" onclick="hideDonut(); getText(this);onAdvertiserTabClick(this);" id="rea_pre"
											data-toggle="tab">Reallocation</a></li> -->
											<li><a href="#s4"
												onclick="hideDonut(); getText(this);onAdvertiserTabClick(this);"
												id="indus_new" data-toggle="tab">Industry News and
													Research</a>
											</li>

											<jsp:include page="DatePicker.jsp" />

										</ul>
										<div id="tab-content1" class="tab-content">

											<!-- modal popover stucture -->


											<div id="myModal" class="modal hide fade" tabindex="-1"
												role="dialog" aria-labelledby="myModalLabel"
												aria-hidden="true"
												style="top: 14%; left: 8%; right: 8%; margin-left: 0px; display: none;">
												<div class="modal-header">
													<button type="button" class="close1" data-dismiss="modal"
														aria-hidden="true">×</button>
													<h3 id="myModalLabel"></h3>
												</div>
												<div class="modal-body" id="modalDivId"></div>

											</div>
											<!-- modal popover stucture -->
											<!-- modal popover stucture for rich media -->


											<div id="myModalRichMedia" class="modal1 hide fade"
												tabindex="-1" role="dialog"
												aria-labelledby="myModalRichMediaLabel" aria-hidden="true"
												style="left: 0%; top: 8%; display: none; width: 753px !important;">
												<div class="modal-header" style="width: 730px !important;">
													<button type="button" class="close1" data-dismiss="modal"
														aria-hidden="true">×</button>
													<h3 id="myModalLabel1" style="font-weight: bold;"></h3>
												</div>
												<div class="modal-body" id="modalDivId1"
													style="width: 730px !important;">
													<div id="white"
														style="background: white; margin-left: -6px;">
														<div id="richMediaEventPopupLoader"
															style="width: 730px; height: 30px; text-align: center; display: none;">
															<img src="img/loaders/type4/light/46.gif" alt="loader">
														</div>
														<div id="richMediaPopupOuterDiv"
															style="background: white; min-height: 130px; margin-left: 6px;">
															
														</div>
													</div>
												</div>
											</div>


											<!-- modal popover stucture -->

											<div class="tab-pane active" id="s1">
												<p>
													<!-- Start of the filter for the below table -->
												<div id="dashboard"
													style="clear: both; font-size: 18px; display: none;">
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control1"></div>
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control2"></div>
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control3"></div>
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control4"></div>
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control5"></div>
													<div
														style="display: inline; float: left; margin-right: 15px; margin-top: 15px;"
														id="control6"></div>
												</div>
												<!-- End of the filter -->
												<!-- upper menu -->
												<div class="agency" style="height: 55px;">

													<div class="frl">
														<div id="agency_first" class="f_channel">AGENCY :</div>
														<div id="first_publisher" class="ltv"></div>
														<div
															class='widget alert alert-info-header adjusted agency_option richMediaAgency'>
															<div class="richMediaAgencyName">
																<strong style='text-transform: uppercase;'>All</strong>
															</div>
														</div>
													</div>

													<div class="channel_div"
														style="float: left; width: 50%; clear: none;">
														<div id="advertiser_first" class="f_channel">ADVERTISER
															:</div>
														<div
															class='widget alert alert-info-header adjusted advertiser_option richMediaAgency'>
															<div class="richMediaAgencyName">
																<strong style='text-transform: uppercase;'>All</strong>
															</div>
														</div>
													</div>
												</div>

												<div id="advertiser_performance_summary_header"
													class="mystats indented revenue_bg" style="height: 45px;">
													<div style="width: 98%; float: left;">
														
														<div class="summary_bar clear_summary_bar">
															<div class="rich_summary_Name">IMPRESSIONS</div>
															<div class="summary_value"></div>
														</div>
														<div class="summary_bar">
															<div class="rich_summary_Name">CLICKS</div>
															<div class="summary_value"></div>
														</div>
														<div class="summary_bar">
															<div class="rich_summary_Name">CTR</div>
															<div class="summary_value"></div>
														</div>
														<div class="summary_bar channel_div">
															<div class="rich_summary_Name">BUDGET</div>
															<div class="summary_value"></div>
														</div>
														
														<!-- <div id="RMperfSummaryHeaderLoader" style="display:block;margin-left:50px;">
									        				<img src="img/loaders/type4/light/46.gif" alt="loader">
									        			</div> -->
													</div>

												</div>
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

												<div id="emptyDataTableMsgId" style="display: none;">
													<h2>
														<center>No records found for the selected filters</center>
													</h2>
												</div>

												<!-- end upper menu -->

												<!--  Start of the container for new matrix -->
												<div id="richMediaEventGraphOuterDiv">
												
												</div>

												<div id="filter_main_table" class="row-fluid"
													style="display: none;">
													<article class="span12"
														style="width: 98%; margin-left: 11px;">
														<!-- new widget -->
														<div class="jarviswidget jarviswidget-sortable"
															id="widget-id-1" role="widget" style="">
															<!-- widget div-->
															<div role="content">
																<div class="inner-spacer">
																	<!-- content goes here -->
																	<div id="dashboard">
																		<div style="display: block;" id="chart2"></div>
																	</div>
																</div>

															</div>
															<!-- end widget div -->
														</div>
														<!-- end widget -->
													</article>
												</div>


												<!--  End of the container for new matrix -->



												<div class="fluid-container">

													<!-- widget grid -->
													<section id="widget-grid" class="">
														<!-- row-fluid -->
														<div class="row-fluid" style="margin-top: 10px;">

															<!-- article -->
															<article class="span12" style="margin-top: 20px;">
																<!-- new widget -->
																<div
																	title="Top campaign line items with the highest absolute CTR% values"
																	class="jarviswidget" id="performers">
																	<header>
																		<h2>PERFORMER</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div id="topPerformLineItemDiv" class="inner-spacer">
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Top campaign line items with the highest absolute
																				CTR% values.
																			</div>
																			<!-- content goes here -->
																			<table id="topPerformLineItemTable"
																				class="table text-left">
																				<thead>
																					<tr>

																						<th></th>
																						<th>CAMPAIGN LINE ITEMS</th>

																						<th style="text-align: right;">IMPRESSION
																							DELIVERED</th>
																						<th style="text-align: right;">CLICKS</th>
																						<th style="text-align: right;">CTR(%)</th>

																					</tr>
																				</thead>
																				<tbody>
																					<tr class="odd gradeX">
																						<td colspan="5"
																							style="color: red; text-align: center;"><img
																							src="img/loaders/type4/light/46.gif" alt="loader">
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->

														</div>
														<!-- end row-fluid -->
														<!-- row-fluid -->
														<div class="row-fluid">

															<!-- article -->
															<article class="span12">
																<!-- new widget -->
																<div
																	title="Top under-delivering campaign line items with highest delivery variance"
																	class="jarviswidget" id="topnonperformers">
																	<header>
																		<h2>NON PERFORMER</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Top under-delivering campaign line items with
																				highest delivery variance.
																			</div>
																			<!-- content goes here -->
																			<table id="topNonPerformLineItemTable"
																				class="table text-left">
																				<thead>
																					<tr>
																						<th></th>
																						<!-- <th></th> -->
																						<th>CAMPAIGN LINE ITEMS</th>
																						<!-- <th style="text-align:right;">BOOKED IMPRESSION</th> -->
																						<th style="text-align: right;">IMPRESSION
																							DELIVERED</th>
																						<th style="text-align: right;">CLICKS</th>
																						<th style="text-align: right;">CTR(%)</th>
																						<!-- <th style="text-align:right;">DELIVERY INDICATOR</th> -->

																					</tr>
																				</thead>
																				<tbody>
																					<tr class="odd gradeX">
																						<td colspan="7"
																							style="color: red; text-align: center;"><img
																							src="img/loaders/type4/light/46.gif" alt="loader">
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->

														</div>
														<!-- end row-fluid -->

														<div class="row-fluid">

															<!-- article -->
															<article class="span12">
																<!-- new widget -->
																<div
																	title="Top over-pacing campaign line items with highest delivery variance."
																	class="jarviswidget" id="mostActiveDiv">
																	<header>
																		<h2>MOST ACTIVE</h2>
																	</header>
																	<!-- wrap div -->
																	<div>
																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Top over-pacing campaign line items with highest
																				delivery variance.
																			</div>

																			<!-- content goes here -->
																			<table class="table text-left table-hover"
																				id="mostActiveLineItemTable">
																				<thead>
																					<tr>
																						<th></th>
																						<th>CAMPAIGN LINE ITEMS</th>
																						<th>CTR(%)</th>
																						<th>CHG(This Week)</th>
																						<th>CHG(Life Time)</th>
																						<th>IMPRESSIONS DELIVERED</th>
																						<!-- <th>DELIEVERY IND</th> -->
																					</tr>
																				</thead>
																				<tbody>
																					<tr class="odd gradeX">
																						<td colspan="6"
																							style="color: red; text-align: center;"><img
																							src="img/loaders/type4/light/46.gif" alt="loader">
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->

															<article style='display: none;' class="span6">
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2>BY LOCATION</h2>
																		<div id="by_location_zoom"
																			style="margin-left: 125px; margin-top: 7px;">
																			<i class="icon-zoom-in"></i>
																		</div>
																	</header>

																	<!-- widget div-->
																	<div>
																		<!-- widget edit box -->
																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>
																		<!-- end widget edit box -->

																		<div class="inner-spacer">
																			<!-- content -->
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Google Geochart will only resize during page
																				refresh.
																			</div>

																			<!-- <div id="geomap" class="chart" style="height: auto"></div> -->

																			<div id="advertiserGeoMapByLocation" rel="popover"
																				class="chart geo_map_market"></div>

																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>

														</div>
														<!-- row-fluid -->

														<div class="row-fluid">

															<!-- article -->
															<article id="topGainerArticle" class="span6">
																<!-- new widget -->
																<div
																	title="Top campaign line items with the highest CTR gain from previous period"
																	class="jarviswidget" id="topGainerDiv">
																	<header>
																		<h2>TOP GAINERS</h2>
																	</header>
																	<!-- wrap div -->
																	<div>
																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Top campaign line items with the highest CTR gain
																				from previous period.
																			</div>
																			<!-- content goes here -->
																			<table class="table text-left table-hover"
																				id="topGainersLineItemsTable">
																				<thead>
																					<tr>
																						<th></th>
																						<th>CAMPAIGN LINE ITEMS</th>
																						<th>CTR(%)</th>
																						<th>CHG(This Week)</th>
																						<th>CHG(Life Time)</th>
																						<th>IMPRESSIONS DELIVERED</th>
																					</tr>
																				</thead>
																				<tbody>
																					<tr class="odd gradeX">
																						<td colspan="6"
																							style="color: red; text-align: center;"><img
																							src="img/loaders/type4/light/46.gif" alt="loader">
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->
															<article id="byMarketArticle" class="span6"
																style="margin-left: 0px;">
																<!-- new widget -->
																<div id="byMarket" class="jarviswidget">
																	<header>
																		<h2>BY MARKET</h2>
																		<div id="by_market_zoom"
																			style="margin-left: 110px; margin-top: 7px;">
																			<i class="icon-zoom-in"></i>
																		</div>
																	</header>

																	<!-- widget div-->
																	<div>
																		<!-- widget edit box -->
																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>
																		<!-- end widget edit box -->

																		<div class="inner-spacer">
																			<!-- content -->
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Google Geochart will only resize during page
																				refresh.
																			</div>
																			<!-- geo chart -->
																			<div id="advertiserGeoMapByMarket" rel="popover"
																				class="chart geo_map_market"></div>
																			<!-- end content -->
																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														<!-- row-fluid -->
														<div "id="toploserDiv" class="row-fluid">

															<!-- article -->
															<article class="span12">
																<!-- new widget -->
																<div
																	title="Top campaign line items with the highest CTR loss from previous period"
																	class="jarviswidget" id="toplosers">
																	<header>
																		<h2>TOP LOSERS</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Top campaign line items with the highest CTR loss
																				from previous period.
																			</div>
																			<!-- content goes here -->
																			<table id="topLosersLineItemsTable"
																				class="table text-left">
																				<thead>
																					<tr>
																						<th></th>
																						<th>CAMPAIGN LINE ITEMS..</th>
																						<th>CTR(%)</th>
																						<th>CHG(This Week)</th>
																						<th>CHG(Life Time)</th>
																						<th>IMPRESSIONS DELIVERED</th>
																					</tr>
																				</thead>
																				<tbody>
																					<tr class="odd gradeX">
																						<td colspan="6"
																							style="color: red; text-align: center;"><img
																							src="img/loaders/type4/light/46.gif" alt="loader">
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->

														</div>
														<!-- end row-fluid -->
														<!-- row-fluid -->
														<div class="row-fluid">
															<article class="span12">
																<!-- new widget -->
																<div id="performanceMetricsDiv" class="jarviswidget">
																	<header>
																		<h2>PERFORMANCE METRICS</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<!-- content goes here -->
																			<table class="table dtable"
																				id="performanceMetricsTable">
																				<thead>
																					<tr>
																						<!-- <th>Date</th> -->
																						<th>CAMPAIGN IO</th>
																						<th>CAMPAIGN LINE ITEM</th>
																						<th>BOOKED IMPRESSIONS</th>
																						<th>DELIVERED IMPRESSIONS</th>
																						<th>CLICKS</th>
																						<th>CTR(%)</th>
																						<th>BUDGET</th>
																						<th>BUDGET DELIVERED</th>
																						<th>MARKET</th>
																						<!-- <th>SITE</th> -->
																					</tr>
																				</thead>
																				<tbody>

																				</tbody>
																			</table>
																			<div id="performanceMetricsLoaderId"
																				style="text-align: center;">
																				<img src="img/loaders/type4/light/46.gif"
																					alt="loader">
																			</div>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														<!-- end row-fluid -->
													</section>
												</div>
												</p>
											</div>
											<div class="tab-pane" id="s2">



												<div class="agency">

													<div class="frl">
														<div id="order_trends" class="f_channel">ORDER :</div>
														<div
															class='widget alert alert-info-header adjusted order_option richMediaOrder'>
															<div class="richMediaOrderName">
																<strong style='text-transform: uppercase;'></strong>
															</div>
														</div>
													</div>

													<div class="channel_div"
														style="float: left; width: 50%; clear: none;">
														<div id="lineItem_trends" class="f_channel">LINE
															ITEMS :</div>
														<div
															class='widget alert alert-info-header adjusted lineOrder_option richMediaOrder'>
															<div class="richMediaOrderName">
																<strong style='text-transform: uppercase;'></strong>
															</div>
														</div>
													</div>

													<div id="advertiser_trends_analysis_header"
														class="mystats indented revenue_bg" style="height: 45px;">
														<div style="width: 98%; float: left;">
																														<div class="summary_bar">
																<div class="rich_summary_Name">IMPRESSIONS</div>
																<div class="summary_value"></div>
															</div>
															<div class="summary_bar">
																<div class="rich_summary_Name">CLICKS</div>
																<div class="summary_value"></div>
															</div>
															<div class="summary_bar">
																<div class="rich_summary_Name">CTR</div>
																<div class="summary_value"></div>
															</div>
															<div class="summary_bar">
																<div class="rich_summary_Name">BUDGET</div>
																<div class="summary_value"></div>
															</div>
															
															<!-- <div id="RMtrendAndAnalHeaderLoader" style="display:block;margin-left:50px;">
									        					<img src="img/loaders/type4/light/46.gif" alt="loader">
									        				</div> -->
															
														</div>
														
													</div>
												</div>
												</br>

												<div id="trends_left" class="actual">
													<div id="left_header" style="">
														<h3>ACTUAL</h3>
													</div>
													<div class="chart_box">
														<div id="chart_div_left3_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_left3" rel="popover"></div>
													</div>
													<div class="chart_box">
														<div id="chart_div_left1_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_left1" rel="popover"></div>
													</div>
													<div class="chart_box">
														<div id="chart_div_left2_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_left2" rel="popover"></div>
													</div>
												</div>
												<div id="trends_right" class="actual">
													<div id="right_header" style="">
														<h3>FORECASTED</h3>
													</div>
													<div class="chart_box">
														<div id="chart_div_right3_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_right3" rel="popover"></div>
													</div>
													<div class="chart_box">
														<div id="chart_div_right1_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_right1" rel="popover"></div>
													</div>
													<div class="chart_box">
														<div id="chart_div_right2_icon">
															<i class="icon-zoom-in"></i>
														</div>
														<div id="chart_div_right2" rel="popover"></div>
													</div>
												</div>
												<div class="clear_both_height"></div>
												<!-- div for left table -->
												<!-- row-fluid -->
												<div class="fluid-container">
													<!-- widget grid -->
													<section id="widget-grid" class="">
														<div class="row-fluid">
															<article class="span12">
																<!-- new widget -->
																<div id="actualAdvertiserDiv" class="jarviswidget">
																	<header>
																		<h2>ACTUAL</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<!-- content goes here -->
																			<table id="actualAdvertiserTable"
																				class="table dtable">
																				<thead>
																					<tr>
																						<th>DATE</th>
																						<th>ORDER</th>
																						<th>LINE ORDER</th>
																						<th>DELIVERED IMPRESSIONS</th>
																						<th>CLICKS</th>
																						<th>CTR(%)</th>
																						<th>BUDGET DELIVERED</th>
																						<!-- <th>BUDGET REMAINING</th> -->
																					</tr>
																				</thead>
																				<tbody>


																				</tbody>
																			</table>
																			<div id="actualAdvertiserTableLoaderId"
																				style="text-align: center;">
																				<img src="img/loaders/type4/light/46.gif"
																					alt="loader">
																			</div>
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														<!-- end row-fluid -->
														</br>
														<!--end-->
														<!-- div for right table -->
														<!-- row-fluid -->
														<div class="row-fluid">
															<article class="span12">
																<!-- new widget -->
																<div id="forcastAdvertiserDiv" class="jarviswidget">
																	<header>
																		<h2>Forecasted</h2>
																	</header>
																	<!-- wrap div -->
																	<div>

																		<div class="jarviswidget-editbox">
																			<div>
																				<label>Title:</label> <input type="text" />
																			</div>
																			<div>
																				<label>Styles:</label> <span
																					data-widget-setstyle="purple" class="purple-btn"></span>
																				<span data-widget-setstyle="navyblue"
																					class="navyblue-btn"></span> <span
																					data-widget-setstyle="green" class="green-btn"></span>
																				<span data-widget-setstyle="yellow"
																					class="yellow-btn"></span> <span
																					data-widget-setstyle="orange" class="orange-btn"></span>
																				<span data-widget-setstyle="pink" class="pink-btn"></span>
																				<span data-widget-setstyle="red" class="red-btn"></span>
																				<span data-widget-setstyle="darkgrey"
																					class="darkgrey-btn"></span> <span
																					data-widget-setstyle="black" class="black-btn"></span>
																			</div>
																		</div>

																		<div class="inner-spacer">
																			<!-- content goes here -->
																			<table id="forcastAdvertiserTable"
																				class="table dtable">
																				<thead>
																					<tr>
																						<th>DATE</th>
																						<th>ORDER</th>
																						<th>LINE ORDER</th>
																						<th>DELIVERED IMPRESSIONS</th>
																						<th>CLICKS</th>
																						<th>CTR(%)</th>
																						<th>BUDGET DELIVERED</th>
																						<th>BUDGET REMAINING</th>
																					</tr>
																				</thead>
																				<tbody>


																				</tbody>
																			</table>
																			<!-- <div id="forcastAdvertiserTableLoaderId" style="text-align:center;">
																	   <img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
																		</div>
																		<!-- end content-->
																	</div>
																	<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														<!-- end row-fluid -->
													</section>
												</div>

												</p>
											</div>
											<div class="tab-pane" id="s3">
												<p>
												<div class="agency">

													<div class="frl" style="float: left; width: 48%;">
														<div id="order_reallocation" class="f_channel">ORDER
															:</div>
														<div
															class='widget alert alert-info-header adjusted order_option richMediaOrder'>
															<divclass"richMediaOrderName">  
													     <strong style='text-transform: uppercase;'></strong> 
													     </div>
													     
													
													</div>	
													
															
												</div>	
												
												
											 		
											 <div id = "advertiser_reallocation_header" class="mystats indented revenue_bg" style="width:98%;float:left;border-radius: 0px 0px 4px 4px;height: 45px;">
													<div class="summary_bar clear_summary_bar">
														<div>TOTAL BUDGET</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar clear_summary_bar" >
														<div >START DATE</div>
														<div class="summary_value"></div>	
													</div>
													<div class="summary_bar clear_summary_bar">
														<div>END DATE</div>
														<div class="summary_value"></div>
													</div>
													
													<div class="summary_bar clear_summary_bar">
														<div>DAYS REMAINING</div>
														<div class="summary_value"></div>
													</div>
													
													<div class="summary_bar clear_summary_bar">
														<div>DELIVERED IMPRESSIONS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar clear_summary_bar" >
														<div>CLICKS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar clear_summary_bar">
														<div>CTR(%)</div>
														<div class="summary_value"></div>
													</div>
											</div>
											</br>
											<!-- start :pie chart for reallocation screen -->

											<!--<div id="chart_div1" style="width: 900px; height: 450px;"></div>-->


											<!-- end :pie chart for reallocation screen -->

											<div class="" style="padding: 0px;clear:both;">
												<!--div class="height-wrapper"-->
												<div id="main" role="main" class="container-fluid">
													<div class="contained">
														<!-- main content -->
														<div id="page-content-popup" class="pcpopup">
															<div class="fluid-container">
																<!-- widget grid -->
																<section id="widget-grid">
																<div class="row-fluid">
																	<article class="span12"> <!-- new widget -->
																	<div class="jarviswidget">
																		<header>
																		<div class="natework_partener">
																			BALANCE($): <span id="budget_allocated"
																				class="balance">0</span>
																		</div>
																		</header>
																		<!-- wrap div -->
																		<div>

																			<div class="jarviswidget-editbox">
																				<div>
																					<label>Title:</label> <input type="text" />
																				</div>
																				<div>
																					<label>Styles:</label> <span
																						data-widget-setstyle="purple" class="purple-btn"></span>
																					<span data-widget-setstyle="navyblue"
																						class="navyblue-btn"></span> <span
																						data-widget-setstyle="green" class="green-btn"></span>
																					<span data-widget-setstyle="yellow"
																						class="yellow-btn"></span> <span
																						data-widget-setstyle="orange" class="orange-btn"></span>
																					<span data-widget-setstyle="pink" class="pink-btn"></span>
																					<span data-widget-setstyle="red" class="red-btn"></span>
																					<span data-widget-setstyle="darkgrey"
																						class="darkgrey-btn"></span> <span
																						data-widget-setstyle="black" class="black-btn"></span>
																				</div>
																			</div>

																			<div class="inner-spacer">
																				<!-- content goes here -->
																				<table id="reallocationItemTable" class="table table-bordered"
																					id="s-table-bordered">
																					<thead>
																						<tr>
																							<th>LINE ITEM</th>
																							<th>DELIVERED IMPRESSIONS</th>
																							<th>CLICKS</th>
																							<th>%CTR</th>
																							<th>RATE</th>
																							<th>BOOKED IMPRESSIONS</th>
																							<th>BUDGET</th>
																							<th>DELIVERED ($)</th>
																							<th>REMAINING ($)</th>
																							<th>ADJUSTED ($)</th>
																							<th>REVISED BUDGET</th>
																							<th>REVISED IMPRESSIONS</th>
																							<!-- <th>SIZE</th> -->
																							<!-- <th>ECPM</th>
																							
																							
																							
																							
																							
																							
																							
																							
																							
																							<th>REVENUE TO BE DELIVERED($)</th>

 -->
																						</tr>
																					</thead>
																					<tbody>
																						
																					</tbody>
																				</table>
																				<!-- <div style="padding-top:20px;">If Revised Revenue to be recognized is greater than Budget Left than in  Alert will come as NOT OK.
												<span id="revised_rev" style="padding-left:20px;padding-top:20px;"><b></b></span></div>
												<div style="float:right;"><input type="button" value="Submit" id="sub"/><input type="button" value="Cancel"/>
												
										    </div>--->
																				<!-- end content-->
																			</div>
																			<!-- end wrap div -->
																		</div>
																		<!-- end widget -->
																	</article>
																</div>

																<!-- end row-fluid --> </section>
																<!-- end widget grid -->
															</div>
														</div>
														<!-- end main content -->


													</div>

												</div>
												<!--/.fluid-container-->
												<div class="push"></div>
												<!--/div-->
											</div>
											<div class="modal-footer">

												<button class="btn btn-primary" id="sub" onclick="setAdvertiserReallocationData();">Save
													changes</button>
											</div>
											</p>
										</div>
										
										 <jsp:include page="newsAndResearch.jsp" /> 
										 
										
									</div>

									<!-- end content -->
								</div>

							</div>
							<!-- end widget div -->
						
					</div>
						<!-- end widget --> </article>
					</div>
					<!-- end: tabs view -->

				</div>
				<!-- end main content -->
			</div>

		</div>
		<!--end fluid-container-->
		<div class="push"></div>
	</div>
	<!-- end .height wrapper -->
	<div id="popover_content_wrapper" style="display: none;">

		<%-- <div class="popup">

			<div id="popheading" class="popup_heading_bg">
				<span class="block_impr"><b>Booked Impressions:</b> </span><span
					style="margin-left: 20px;">585,000</span><span
					class="popup_span_cpm"><b>CPM:</b> </span><span
					class="popup_span_cpm_point">$5.78</span>
			</div>
			<div class="popup_line">
				<span class="life_time"><b>Life Time</b> </span><span class="flr"><b>This
						Week</b> </span>
			</div>
			<!-- <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;"><span style="font-weight:bold;color:black;">Booked Impressions:</span><span style="margin-left:150px;">2355</span><span style="float:right;">2755</span></div>-->
			<div class="popup_line">
				<span class="popup_span_bcolor">Impressions Delivered:</span><span
					class="popup_span_delivered">283,200</span><span class="flr">9,258</span>
			</div>
			<div class="popup_line">
				<span class="popup_span_bcolor">Clicks:</span><span
					class="popup_span_click">2,442</span><span class="flr">91</span>
			</div>
			<div class="popup_line">
				<span class="popup_span_bcolor">CTR(%):</span><span
					class="popup_span_ctr">0.80%</span><span class="flr">0.98%</span>
			</div>
			<!--	  <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;"><span style="font-weight:bold;color:black;">CPM:</span><span style="margin-left:252px;">4545</span><span style="float:right;">100</span></div>-->
			<div class="popup_line">
				<span class="popup_span_bcolor">Revenue Delivered:</span><span
					class="popup_span_delivered">$1,636.90</span><span class="flr">$53.31</span>
			</div>
			<div class="popup_line">

				<span class="popup_span_bcolor">Revenue Remeining:</span><span
					class="popup_span_remeining">$1,744.40</span><span class="flr">$1,690.88</span>
			</div>
			<div id="chart_div" class="popup_chart"></div>
			<div class="popup_chart_bg">
				<a class="btn btn-inverse medium view_trends" href="#"
					data-toggle="tab" onclick="viewperformance()">View Trends</a> <a
					class="btn btn-inverse medium location11" href="#"
					data-toggle="tab" onclick="reallcation()">Reallocation</a>

			</div>
		</div> --%>



	</div>
	<!-- footer -->

	<!-- if you like you can insert your footer here -->

	<!-- end footer -->

	<!--================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<!-- Le javascript
    ================================================== -->
	<jsp:include page="js.jsp" />

	<!-- Google Geo Chart -->
	
	<script src="../js/include/adv_charts/geochart.js?v=<s:property value="deploymentVersion"/>"></script>
	<script src="../js/LineGraph.js?v=<s:property value="deploymentVersion"/>"></script>
	<script src="../js/richMediaAdvertiser.js?v=<s:property value="deploymentVersion"/>"></script>
	<script type="text/javascript">
		/* Start: function to show and hide news section*/
		$(document).ready(function() {
			$('#indus_new').click(function() {
				$('#s4').show();
			});
		});
		$(document).ready(function() {
			$('#per_sum').click(function() {
				$('#s4').hide();
			});
		});
		$(document)
				.ready(
						function() {
							$('#tre_ana')
									.click(
											function() {
												$('#s4').hide();
												var oTableTools = TableTools
														.fnGetInstance('actualAdvertiserTable');
												if (oTableTools != null
														&& oTableTools
																.fnResizeRequired()) {
													oTableTools
															.fnResizeButtons();
												}
												oTableTools = TableTools
														.fnGetInstance('forcastAdvertiserTable');
												if (oTableTools != null
														&& oTableTools
																.fnResizeRequired()) {
													oTableTools
															.fnResizeButtons();
												}
											});
						});
		$(document).ready(function() {
			$('#rea_pre').click(function() {
				$('#s4').hide();
			});
		});
		/* End: function to show and hide news section*/

		function showCurrentWeek() {
			var today = new Date();
			var calStartDate = new Date();
			calStartDate.setDate(today.getDate() - 6);
			var calEndDate = new Date();

			$("#startDateId").val(
					$.datepicker.formatDate('yy-mm-dd', calStartDate));
			$("#endDateId")
					.val($.datepicker.formatDate('yy-mm-dd', calEndDate));
		}

		function showDonut() {
			try {
				$('#' + advertiserDonutGraph).css({
					"display" : "block"
				});
			} catch (error) {
			}
		}

		function hideDonut() {
			try {
				$('#' + advertiserDonutGraph).css({
					"display" : "none"
				});
			} catch (error) {

			}
		}

		/* -----script for reallocation table ----- */
		$(document).ready(
				function() {
					$(".col").blur(
							function() {
								//alert("fdsdf");
								var Value1 = parseInt($('#col1').val());
								var Value2 = parseInt($('#col2').val());
								var Value3 = parseInt($('#col3').val());
								var Value4 = parseInt($('#col4').val());
								//alert(Value2);
								var total = Value1 + Value2 + Value3 + Value4
								$('#rev_budget').html(total);
								var ecpm1 = parseInt($('#ecpm1').html()
										.replace('$', ''));
								var ecpm2 = parseInt($('#ecpm2').html()
										.replace('$', ''));
								var ecpm3 = parseInt($('#ecpm3').html()
										.replace('$', ''));
								var ecpm4 = parseInt($('#ecpm4').html()
										.replace('$', ''));

								var rev_book_imp1 = (Value1 * 1000) / ecpm1

								var rev_book_imp2 = (Value2 * 1000) / ecpm2
								var rev_book_imp3 = (Value3 * 1000) / ecpm3
								var rev_book_imp4 = (Value4 * 1000) / ecpm4
								//alert(rev_book_imp1);
								$('#rb1').html(rev_book_imp1);
								$('#rb2').html(rev_book_imp2);
								$('#rb3').html(rev_book_imp3);
								$('#rb4').html(rev_book_imp4);

								var rbvalue1 = parseInt($('#rb1').html());
								var rbvalue2 = parseInt($('#rb2').html());
								var rbvalue3 = parseInt($('#rb3').html());
								var rbvalue4 = parseInt($('#rb4').html());

								var rbtotal = rbvalue1 + rbvalue2 + rbvalue3
										+ rbvalue4
								$('#rb5').html(rbtotal);

								var rev_rec_val1 = parseFloat($('#rev_reco1')
										.html().replace('$', ''));
								var rev_rec_val2 = parseFloat($('#rev_reco2')
										.html().replace('$', ''));
								var rev_rec_val3 = parseFloat($('#rev_reco3')
										.html().replace('$', ''));
								var rev_rec_val4 = parseFloat($('#rev_reco4')
										.html().replace('$', ''));

								var rev_rec_cal1 = Value1 - rev_rec_val1;
								var rev_rec_cal2 = Value2 - rev_rec_val2;
								var rev_rec_cal3 = Value3 - rev_rec_val3;
								var rev_rec_cal4 = Value4 - rev_rec_val4;

								var rev_rec_cal_forma1 = (rev_rec_cal1)
										.toFixed(2);
								var rev_rec_cal_forma2 = (rev_rec_cal2)
										.toFixed(2);
								var rev_rec_cal_forma3 = (rev_rec_cal3)
										.toFixed(2);
								var rev_rec_cal_forma4 = (rev_rec_cal4)
										.toFixed(2);

								var rev_rec_cal_formated1 = "$"
										+ rev_rec_cal_forma1;
								var rev_rec_cal_formated2 = "$"
										+ rev_rec_cal_forma2;
								var rev_rec_cal_formated3 = "$"
										+ rev_rec_cal_forma3;
								var rev_rec_cal_formated4 = "$"
										+ rev_rec_cal_forma4;
								//alert(rev_rec_cal_formated1);
								var rev_rec_col_total = rev_rec_cal1
										+ rev_rec_cal2 + rev_rec_cal3
										+ rev_rec_cal4;

								var rev_rec_col_total1 = (rev_rec_col_total)
										.toFixed(2)
								var rev_rec_col_total_formated = "$"
										+ rev_rec_col_total1;
								$('#rr1').html(rev_rec_cal_formated1);
								$('#rr2').html(rev_rec_cal_formated2);
								$('#rr3').html(rev_rec_cal_formated3);
								$('#rr4').html(rev_rec_cal_formated4);
								$('#rr5').html(rev_rec_col_total_formated);

								var budget_allocated_val = 13524 - total
								$('#budget_allocated').html(
										budget_allocated_val);

							});

				});

		$(document)
				.ready(
						function() {
							$("#sub")
									.click(
											function() {

												var Value1 = parseInt($('#col1')
														.val());
												var Value2 = parseInt($('#col2')
														.val());
												var Value3 = parseInt($('#col3')
														.val());
												var Value4 = parseInt($('#col4')
														.val());
												//alert(Value2);
												var total = Value1 + Value2
														+ Value3 + Value4
												var budget_allocated_val = 13524 - total
												if (budget_allocated_val > 0
														|| budget_allocated_val < 0) {
													bootbox
															.confirm(
																	"The total budget has changed. Are you sure you want to apply the changes?",
																	function(
																			result) {
																		//console.log("Confirm result: "+result);
																		// toastr.info("Confirm result: "+result);
																		if (result == true) {
																			toastr
																					.info("Data saved");
																		} else {
																			toastr
																					.info("Discarded by user");
																		}

																	})
															.css(
																	{
																		'left' : '2%',
																		'margin-left' : '0px'
																	});
												}

											});
						});

		/* end reallocation script */

		/*  start: plugin and code for slider */

		$('.carousel').carousel({
			interval : 2000
		});

		$(document).ready(
				function() {
					if ($('#advertiserDonutGraph').length) {
						Morris.Donut({
							element : 'advertiserDonutGraph',
							colors : [ '#0B62A4', '#3980B5', '#679DC6',
									'#95BBD7', '#B0CCE1', '#095791', '#095085',
									'#083E67', '#052C48', '#042135', ],
							backgroundColor : '#FFFFFF',
							labelColor : '#F0F8FF',
							data : [ {
								value : 93.75,
								label : 'Remaining'
							}, {
								value : 6.25,
								label : 'Complete'
							}, ],
							formatter : function(x) {
								return x + "%"
							}
						});
					}
				});

		google.load("visualization", "1", {
			packages : [ "corechart" ]
		});
		google.setOnLoadCallback(drawAreaChart);
		function drawAreaChart() {
			var data = google.visualization.arrayToDataTable([
					[ 'Days', 'Impressions' ], [ '21', 1039 ], [ '22', 1095 ],
					[ '23', 1287 ], [ '24', 1525 ], [ '25', 1193 ],
					[ '26', 1487 ], [ '27', 1632 ], [ '28', 1259 ] ]);

			var options = {
				title : 'IMPRESSIONS',
				width : 535,
				height : 200,
				hAxis : {
					title : 'Date',
					titleTextStyle : {
						color : 'red'
					}
				},
				legend : {
					position : 'none'
				}
			};

			var chart = new google.visualization.AreaChart(document
					.getElementById('chart_div'));
			chart.draw(data, options);
		}

		google.load("visualization", "1", {
			packages : [ "corechart" ]
		});
		google.setOnLoadCallback(drawPieChart);

		function drawPieChart() {
			var data = google.visualization.arrayToDataTable([
					[ 'Task', 'Hours per Day' ], [ 'Work', 11 ], [ 'Eat', 2 ],
					[ 'Commute', 2 ], [ 'Watch TV', 2 ], [ 'Sleep', 7 ] ]);

			var options = {
				title : 'My Daily Activities'
			};

			// var pieChart = new google.visualization.PieChart(document.getElementById('chart_div1'));
			// pieChart.draw(data, options);

		}

		google.load('visualization', '1.0', {
			'packages' : [ 'controls' ]
		});
		google.setOnLoadCallback(drawDashboard);
		function drawDashboard() {

			try {
				// Create our data table.
				var data = google.visualization.arrayToDataTable([
						[ 'Name', 'Dollar' ], [ 'Michael', 5 ], [ 'Elisa', 7 ],
						[ 'Robert', 3 ]

				]);

				// Create a dashboard.
				var dashboard = new google.visualization.Dashboard(document
						.getElementById('dashboard_div'));

				// Create a range slider, passing some options
				var donutRangeSlider = new google.visualization.ControlWrapper(
						{
							'controlType' : 'NumberRangeFilter',
							'containerId' : 'filter_div',
							'options' : {
								'filterColumnLabel' : 'Dollar'
							}
						});

				// Create a pie chart, passing some options
				var pieChart = new google.visualization.ChartWrapper({
					'chartType' : 'PieChart',
					'containerId' : 'chart_div',
					'options' : {
						'width' : 300,
						'height' : 300,
						'pieSliceText' : 'value',
						'legend' : 'right'
					}
				});

				dashboard.bind(donutRangeSlider, pieChart);

				// Draw the dashboard.
				dashboard.draw(data);
			} catch (error) {

			}
		}

		//start:function selecting the tab

		$(document).ready(function() {
			$('#trendclose').click(function() {
				$('#myTab li:eq(0) a').tab('show');
				$('#myTab li:eq(1)').css({
					'display' : 'none'
				});
			});
		});
		$(document).ready(function() {
			$('#reallocationclose').click(function() {
				$('#myTab li:eq(0) a').tab('show');
				$('#myTab li:eq(2)').css({
					'display' : 'none'
				});
			});
		});
	</script>



</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
