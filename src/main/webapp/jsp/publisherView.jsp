<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
   <jsp:include page="Header.jsp" /> 
   <s:set name="theme" value="'simple'" scope="page" />
<%@page import="com.lin.web.util.TabsName" %>

<!DOCTYPE html>

<html lang="en">
<head>
		
<script>


var pageName = '<s:property value="#session.sessionDTO.pageName"/>';
var summaryTab = '<%=TabsName.PUBLISHER_VIEW_SUMMARY%>';
var trendsTab = '<%=TabsName.PUBLISHER_VIEW_TRENDS%>';
var diagnosticTab = '<%=TabsName.PUBLISHER_VIEW_DIAGNOSTIC_TOOLS%>';
var traffickingTab = '<%=TabsName.PUBLISHER_VIEW_TRAFFICKING%>';
$(document).ready(function() {
	$('#publisherViewLi').attr('class', 'main-nav-li_selected');
});
localStorage.clear();		
</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Publisher Dashboard</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

	<%
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	%>

		<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>
<link rel="stylesheet" href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
<link rel="alternate" media="print" href="alternativeUrlForPrint.ext" />

<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>
<script type='text/javascript' src='https://www.google.com/jsapi'></script>

<script src="../js/accounting.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/donutChart.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/common-graphs.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/publisher.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/dataManipulation.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/operations.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/diagnosticTools.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>

<script type="text/javascript">

$(document).ready(function(){
    $(document).keydown(function(event) {
        if (event.ctrlKey==true && (event.which == '80')) { //cntrl + p
            event.preventDefault();
            getPrintView();
        	//return false;
        }
    });
 });

</script>


</head>

<body>

<!-- .height-wrapper -->


<div class="height-wrapper">


	<div id="main" role="main" class="container-fluid">

		<!-- <div class="contained"> -->
			<!-- aside -->
			
			<!-- aside end -->
			
			<div id="revenueSummaryActualView" style="display:block;">
			<!-- main content -->
			
			<div id="page-content" class="mlr">
			<jsp:include page="navigationTab.jsp"/>
			<jsp:include page="filter.jsp"/>
           
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
										<label>Title:</label>
										<input type="text" />
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
								<!-- end widget edit box -->
								
                         
								<%-- <jsp:include page="DatePicker.jsp" />     --%>
									<!-- <div style="background: #ddd;width: 100%;height: auto;float: left;"> -->
									<!-- content -->	
									<ul class="nav nav-tabs upper_tabs" id="myTab1">
									<s:if test="%{authorisationKeywordList.contains('publisherInvRevView')}">
										<li class="active">
											<a href="#s1" onclick = "tabClickValue(this);" id="inv_rev" class="upper_tab1"><%=TabsName.PUBLISHER_VIEW_SUMMARY%></a>
										</li>
									</s:if>
									<%-- <s:if test="%{authorisationKeywordList.contains('publisherCampaignView')}">
										<li>
											<a href="#s6" onclick="tabClickValue(this);" id="per_sum_publisher" data-toggle="tab">Campaigns</a>
										</li>
									</s:if> --%>
									<s:if test="%{authorisationKeywordList.contains('publisherTreAnsView')}">
										<li >
											<a href="#s2" onclick = "tabClickValue(this);" id="tre_ana" ><%=TabsName.PUBLISHER_VIEW_TRENDS%></a>
										</li>
										<!-- <li style="display: none;margin-top:10px;" >
											<a href="#s8" onclick = "tabClickValue(this);" id="tre_ana_campaign">Trends and Analysis</a>
										</li> -->
									</s:if>
									<s:if test="%{authorisationKeywordList.contains('publisherDiagToolView')}">
										<li >
											<a href="#s5" onclick = "tabClickValue(this);" id="diagnosticTools" ><%=TabsName.PUBLISHER_VIEW_DIAGNOSTIC_TOOLS%></a>
										</li>
									</s:if>
									<s:if test="%{authorisationKeywordList.contains('publisherTrafView')}">
										<li>
											<a href="#s7" onclick = "tabClickValue(this);getCampaignTraffickingData();" id="trafficking"><%=TabsName.PUBLISHER_VIEW_TRAFFICKING%></a>
										</li>
									</s:if>
									<%-- <s:if test="%{authorisationKeywordList.contains('publisherReallocationView')}">
										<li class="upper_tab3" style="display:none;" >
											<a href="#s3" onclick = "tabClickValue(this);" id="rea_inv" >Reallocation</a>
										</li>
									</s:if> --%>
									<%-- <s:if test="%{authorisationKeywordList.contains('publisherIndNewsResView')}">
										<li>
											<a href="#s4" onclick = "tabClickValue(this);" id="indus_new">Industry News and Research</a>
										</li>
									</s:if> --%>
										
										 <jsp:include page="DatePicker.jsp" /> 
									</ul>
									
										
					<div id="tab-content1" class="tab-content">
					<!-- <div id="ajax_id" class="ajax_loader">
<div class="ajax_tittle">LOADING</div>
<div class="ajax_image">
<img src="img/loaders/type6/light/80.gif" alt="loader">
</div>
</div> -->
<!-- modal popover stucture -->



<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:14%;left:8%;right:8%;margin-left:0px;display:none;">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel"></h3>
	</div>
	<div class="modal-body" id ="modalDivId">

	</div>

</div>									
<!-- modal popover stucture -->	
<s:if test="%{authorisationKeywordList.contains('publisherInvRevView')}">
										<div class="tab-pane active" id="s1">
											<!-- upper menu -->
											
											<div class="publis_er" >
												<div style="float:left;">
														<div class="publisher_outer">PUBLISHER :</div>
														<div id="first_publisher" class="publisher_outer_name"></div>
														<div id="first_channel" class="channel_outer">CHANNELS :</div>
												
												</div>
												
												<div  style="float:right; margin-right: 1%">
													<s:if test="%{authorisationKeywordList.contains('publisherDownloadReport')}">
														<a title="Print View" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getRevenueSummaryPrintView('true');"><i class="cus-printer"></i></a>
													</s:if>
												</div>
												<div  style="float:right; margin-right: 1%">
													<s:if test="%{authorisationKeywordList.contains('publisherDownloadReport')}">
														<a title="Download Report" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:downLoadPublisherReport();"><i class="cus-doc-excel-table"></i></a>
													</s:if>
												</div>
											<div id="publisher_inventory_revenue_header" class="mystats indented revenue_bg">
												<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 0%;">
													<div class="summary_bar" >
														<div class="summary_Name">SITES</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">IMPRESSIONS DELIVERED</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CLICKS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CTR</div>
													<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">eCPM&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
													</div>
													
													<div class="summary_bar">
														<div class="summary_Name">RPM&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
														
													</div>
													<div class="summary_bar">
														<div class="summary_Name">PAYOUTS&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
													</div>
												</div>
												<div id="c3">
												</div>			
											</div></br>
											<!-- end upper menu -->
											
																		        		
												<div class="row-fluid">
								
								<article class="span12">
									<!-- new widget -->
									
							
									
									<div class="jarviswidget">
									    <header>
									        <h2><div style="float:left;">Channel Performance</div><div id="preloader" style="display:none;margin-top:5px;width:100%;position:absolute;float:left;margin-left:225px;">
									        	<img src="img/loaders/type1/light/24.gif" alt="loader">
									        	
									    </div></h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer widget-content-padding"> 
									        <!-- content goes here -->
									        
									        	<!-- row-fluid -->
										        <div class="row-fluid">
										        	
										        	<!-- span6 -->
										        	<div class="span9">
															
											           	<table style="border:1px solid #DDDDDD;" class="table table-hover table-bordered responsive" id="just-table">
																				<thead>
																					<tr>
																					    <th></th>
																						<th>Sales Channel</th>
																						<th>eCPM</th>
																						<th>CHG</th>
																						<th>CHG%</th>
																						<th>IMPRESSIONS DELIVERED</th>
																						<th>Clicks</th>
																						<th>CTR%</th>
																						<th>Payout</th>
																					</tr>
																				</thead>
																				<tbody>
																					
																				</tbody>
																			</table>
																			<div id="sales_Channel" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
													   
													   
													   
												        
										        	</div>
										        	<!-- end span6 -->
										        	
										        	<!-- span6-->
										        	<div class="span3">
										        		<!-- well -->
											
	<div id="IrPieChart_div_zoom"><i class="icon-zoom-in"></i></div>												
	<div id="IrPieChart_div" rel="popover" class="chart" style="width:100%;height: 100%;" ></div>


			
														<!-- end .well -->
														
										        	</div>
										        	<!-- end span6-->
										        	
										        </div>
									        	<!-- end row-fluid -->

										    <!-- end content-->
										    </div>
									    
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
							
							</div>
							
							<div class="control-group" style="padding:0px;">
			                   <div class="controls">
                                   <select class="span2 with-search" id="selectChannelPublisher" onchange="changeChannelPublisherDropDown();" style="font-size:16px;">
              
                                   </select>
                               </div>
                            </div>
														<!-- row-fluid -->

														<div class="row-fluid">
															<article class="span12">
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2><div style="float:left; font-size-adjust: 0.58;">Performance By Property</div>
																		<!-- <div id="preloader1" style="float:left;margin-left: 150px;margin-top:5px;width:100%;position:absolute; text-align:center;"> -->
																		
																		</h2>
																	</header>
																		<!-- wrap div -->
																			<div>
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

																				<div class="inner-spacer">
																			<!-- content goes here -->
																			<table class="table table-striped table-bordered responsive" id="dtable1">
																				<thead>
																					
																					<tr>
																						<th>Property</th>
																						<th>eCPM</th>
																						<th>CHG</th>
																						<th>CHG%</th>
																						<!-- <th>REQUEST SUBMITTED</th>
																						<th>REQUESTS RECEIVED</th>
																						<th>VARIANCE</th> -->
																						<th>IMPRESSIONS DELIVERED</th>
																						<th>Clicks</th>
																						<th>Payout</th>
																					</tr>
																					
																					
																				</thead>
																				<tbody>
																					

																				</tbody>
																			</table>
																			<div id="perfromace_property" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
																		</div>
																				<!-- end content-->
																		</div>
																		<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															<!-- end article -->			
															<article id="performance_geomap" class="span12" style="margin-left:0px;display:none;">
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2>Performance By Property</h2>
																	</header>

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

																		<div class="inner-spacer">
																			<!-- content -->
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Google Geochart will only resize during page refresh.
																			</div>
																			<!-- geo chart -->
																			<div id="geomap4" class="chart" style="height: auto"></div>
																			<!-- end content -->
																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														
														
					<div class="row-fluid" id="sell_throughdata">
								<article class="span7">
									<!-- new widget -->
									<div class="jarviswidget">
									  	<header>
											<h2><div style="float:left">Sell Through Data For Next Week</div>
											<div id="preloader2" style="display:none;float:left;margin-top:1px;margin-left:195px;width:100%;position:absolute; text-align:center;">
		        								<img src="img/loaders/type1/light/24.gif" alt="loader">
		        								
		    								</div>
											</h2>
										</header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												<!--<div class="widget alert alert-info adjusted">
													<button class="close" data-dismiss="alert">Ã—</button>
													<i class="cus-exclamation"></i>
													<strong>Cool export features:</strong> This dynamtic table can also export <strong>PDF</strong> and <strong>Excel</strong> files, feel free to click on the PDF or Excel button to see the result
												</div>-->
												<table class="table dtable" id="sellThroughTable">
													<thead>
														<tr>
																<th>Property/Site</th>
																<th>Ad Unit</th>
																<th>Forecasted Impressions</th>
																<th>Available Impressions</th>
																<th>Reserved Impressions</th>
																<th>Sell Through Rate</th>
															</tr>
													</thead>
													<tbody>
													
													</tbody>
												</table>
												<div id="sell_through" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
										        	
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
								
								<article class="span5" >
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2>Sell Through Rate</h2>
																	</header>

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

																		<div class="inner-spacer" id="container_printView">
																			
																			<!-- geo chart -->
																			<!--<div id="geomap2" class="chart" style="height: auto"></div>-->
																			<%-- <div class="easypie" style="align:center">
																				<div class="percentage1" data-percent="68">
																					<span>0.31</span>%
																				</div>
																				<div class="easypie-text">
																					Sell through rate
																				</div>
																			</div> --%>
																			<!-- end content -->
																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>
			</div>
														
														
														
														
														
														<!-- row-fluid -->
														
														<!-- end row-fluid -->
														
													</section>
												</div>
											</p>
										</div>
</s:if>
									
<s:if test="%{authorisationKeywordList.contains('publisherTreAnsView')}">									
<div class="tab-pane" id="s2">
			<p>
			 <div class="jarviswidget-editbox">
	            <div>
	                <label>Title:</label>
	                <input type="text" />
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
	        
	        
		
			<div class="trands_analysis" >
												<div style="float:left;">
														<div class="publisher_outer">PUBLISHER :</div>
														<div id="second_publisher" class="publisher_outer_name"></div>
														<div id="second_channel" class="channel_outer">CHANNELS :</div>
													
												</div>
												
												<div  style="float:right; margin-right: 1%">
													<s:if test="%{authorisationKeywordList.contains('publisherDownloadReport')}">
														<a title="Print View" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getTrendsAnalysisPrintView('true');"><i class="cus-printer"></i></a>
													</s:if>
												</div>
												
												<div  style="float:right; margin-right: 1%">
													<s:if test="%{authorisationKeywordList.contains('publisherDownloadReport')}">
														<a title="Download Report" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:downLoadPublisherReport();"><i class="cus-doc-excel-table"></i></a>
													</s:if>
												</div>
											</div>
											<div id = "publisher_trends_analysis_header" class="mystats indented" style="background:url('../img/backgrounds/mesh.png') repeat scroll 0 0 #333333;clear:both;height: 100px;">
												<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left:0%;">
													<div class="summary_bar" >
														<div class="summary_Name">REQUESTS</div>
														<div class="summary_value"></div>
														
													</div>
													
													<div class="summary_bar">
														<div class="summary_Name">DELIVERED</div>
														<div class="summary_value"></div>
														
													</div>
													<div class="summary_bar">
														<div class="summary_Name">FILL RATE(%)</div>
														<div class="summary_value"></div>
														
													</div>
												<!-- </div>
												<div class="" style=" margin-left: 3%;width: 15%;float:left;border-radius: 4px 4px 4px 4px;"> -->
													<div class="summary_bar">
														<div class="summary_Name">IMPRESSIONS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CLICKS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CTR</div>
														<div class="summary_value"></div>
													</div>
												</div>
												
												<!--<div class="indented" style=" margin-left: 1%;width: 27%;float:left;border-radius: 4px 4px 4px 4px;">
														<div id="upperheader_chart_trends" style="width:100%;height:70%;"></div>
												
												</div>-->
												
															<div class="easypie" style="float: right;">
																<div class="percentage2 easyPieChart" data-percent="68">
																	<span>68</span>%
																	<canvas width="150" height="150"></canvas>
																</div>
																<div class="easypie-text">FILL RATE</div>
															</div>
											<!-- </div>
											
											<div class="" style=" margin-left: 0%;width: 98%;float:left;border-radius: 4px 4px 4px 4px;"> -->
													<div class="summary_bar">
														<div class="summary_Name">eCPM</div>
														<div class="summary_value"></div>
													</div>
													<!-- <div class="mychart" id="subscribe" style="width:35px;float:left;margin-left:15px;margin-top:16px;"></div> -->
													<div class="summary_bar">
														<div class="summary_Name">RPM</div>
														<div class="summary_value"></div>
													</div>
													<!-- <div class="mychart" id="support" style="width:35px;float:left;margin-left:18px;margin-top:16px;"></div> -->
													<div class="summary_bar">
														<div class="summary_Name">PAYOUTS</div>
														<div class="summary_value"></div>
													</div>
													
													<!-- <div id="trendAndAnalHeaderLoader" style="display:block;margin-left:50px;">
									        			<img src="img/loaders/type4/light/46.gif" alt="loader">
									        		</div> -->
											</div>
											</br>
		
 <!-- Line chart starts -->
 
 
 
    <div class="fluid-container" id="lineChartPublisherDiv" ng-app="lineChartPublisherApp" ng-controller="lineChartpublisherCtrl" >
    <!-- widget grid -->
    <section id="widget-grid" class="">
	    <div class="row-fluid" >	    
		  
			<article class="span6 ">
				<!-- new widget -->
				<div id= "ecpmlLneChartDiv" class="jarviswidget">
					<header ><h2>ECPM Per Day</h2></header>
				
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
						<div>`
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
				
						<div class="inner-spacer">	
						    <div id="ecpmChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img class="chartLoaderImageClass" src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> 
							
							<div id="ecpmChartDiv" class="linechartdatadiv"  google-chart chart="ecpmChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		
				<article class="span6">
				<!-- new widget -->
				<div title="Actual Charts"  id= "filllLneChartDiv" class="jarviswidget" >
					<header>
						<h2>FILLRATE Per Day</h2>
						<!-- <div class="jarviswidget-ctrls" role="menu"> 
						     <a href="#" class="button-icon jarviswidget-delete-btn" style="display: block;"><i class="trashcan-10"></i></a>  
						     <a href="#" class="button-icon jarviswidget-edit-btn" style="display: block;"><i class="pencil-10 "></i></a> 
						     <a href="#" class="button-icon jarviswidget-fullscreen-btn"><i class="fullscreen-10"></i></a> 
						     <a href="#" class="button-icon jarviswidget-toggle-btn" style="display: block;"><i class="min-10 "></i></a>
						     </div> -->
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer">	
						      <div id="fillRateChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img  src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div>
							
							<div id="fillChartDiv" class="linechartdatadiv" google-chart chart="fillRateChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		</div>
			<div class="row-fluid">
			
			<article class="span6 sortable-grid ui-sortable">
				<!-- new widget -->
				<div   id= "revenuelLneChartDiv" class="jarviswidget" >
					<header>
						<h2>REVENUE Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer">	
						       <div id="revenueChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> 
							
							<div id="revenueChartDiv" class="linechartdatadiv"  google-chart chart="revenueChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
				<article class="span6 sortable-grid ui-sortable">
				<!-- new widget -->
				<div title="Actual Charts"  id= "impressionlLneChartDiv" class="jarviswidget" >
					<header>
						<h2>IMPRESSION Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer">	
						       <div id="impressionChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div>
							
							<div id="impressionChartDiv" class="linechartdatadiv"  google-chart chart="impressionChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
			</div>
				<div class="row-fluid">
			
			<article class="span6 sortable-grid ui-sortable">
				<!-- new widget -->
				<div   id= "clicklLneChartDiv" class="jarviswidget" >
					<header>
						<h2>CLICKS Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer">	
						       <div id="clickChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div>
							
							<div  id="clickChartDiv" class="linechartdatadiv"  google-chart chart="clickChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
				<article class="span6 sortable-grid ui-sortable">
				<!-- new widget -->
				<div title="Actual Charts"  id= "ctrlLneChartDiv" class="jarviswidget" >
					<header>
						<h2>CTR Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div  class="inner-spacer">	
						       <div  id="ctrChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div>
							
							<div  id="ctrChartDiv" class="linechartdatadiv" google-chart chart="ctrChart" style="{{chart.cssStyle}}" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
		</div>	
	 </section>
</div>

			

		<div style="height:15px;clear:both;"></div>
		<!-- div for left table -->
		<!-- row-fluid -->
		
		
		<div class="row-fluid" style="border-top:1px solid #ccc;">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget">
									  	<header>
											<h2>ACTUAL</h2>
										</header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer">
												<table class="table dtable" id="actualPublisher">
													<thead>
														<tr>
															<th>DAYS</th>
															<th>CHANNELS</th>
															<th>REQUESTS</th>
															<!-- <th>SERVED</th> -->
															<th>IMPRESSION DELIVERED</th>
															<th>FILL RATE</th>
															<th>CLICKS</th>
															<th>CTR(%)</th>
															<th>REVENUE</th>
															<th>eCPM</th>
															<th>RPM</th>
															
														</tr>
													</thead>
													<tbody>
												
													</tbody>
												</table>
										        <div id="trends_publisher" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>	
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
			</div>
		<!--end-->
		
	</p>
</div>
</s:if>									
<s:if test="%{authorisationKeywordList.contains('publisherReallocationView')}">
										<div class="tab-pane" id="s3">
											
													<div style="background:url('../img/backgrounds/mesh.png') repeat scroll 0 0 #333333; height:32px;font-family: 'Lato',Arial,Helvetica,sans-serif;border-radius: 4px 4px 0px 0px;box-shadow: 0 1px 1px #606060, 0 1px 1px #2A2A2A inset;padding-top: 10px;">
												<div style="float:left;">
														<div style="color:#D1D1D1;font-size: 16px;font-weight: normal;margin-bottom: 7px;margin-left:20px;float:left;">PUBLISHER :</div>
														<div style="font-size:16px;font-weight:bold;color:#F0F0F0;margin-left:20px;float:left;margin-left:12px;">LIN TV</div>
												</div>
												
											</div>	
											
											<div id = "publisher_reallocation_header" class="mystats indented" style="background:url('../img/backgrounds/mesh.png') repeat scroll 0 0 #333333;clear:both;height: 165px;">
												<div style="width:14%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 1%;">
													<div class="summary_bar" >
														<div>REQUESTS</div>
														<div class="summary_value">11,166,155</div>
														
													</div>
													<div class="summary_bar publisher_reallo">
														<div>DELIVERD</div>
														<div class="summary_value">10,374,057</div>
														
													</div>
													<div class="summary_bar publisher_reallo">
														<div>FILL RATE</div>
														<div class="summary_value">92.90%</div>
														
													</div>
												</div>
												<div style=" margin-left: 3%;width: 15%;float:left;border-radius: 4px 4px 4px 4px;">
													<div class="summary_bar">
														<div>REVENUE</div>
														<div class="summary_value">$2,826.95</div>
													</div>
													<div class="summary_bar publisher_reallo">
														<div>CLICKS</div>
														<div class="summary_value">4,956</div>
													</div>
													<div class="summary_bar publisher_reallo">
														<div>CTR(%)</div>
													<div class="summary_value">0.04%</div>
													</div>
												</div>
												<div class="" style=" margin-left: 3%;width: 20%;float:left;border-radius: 4px 4px 4px 4px;">
													<div class="summary_bar">
														<div>eCPM(Estimated)</div>
														<div class="summary_value">$0.27%</div>
													</div>
													<div class="mychart" id="reall_ecpm" style="width:35px;float:left;margin-left:15px;margin-top:16px;"></div>
													<div class="summary_bar publisher_reallo">
														<div>RPM(Estimated)</div>
														<div class="summary_value">$0.25%</div>
														
													</div>
													<div class="mychart" id="reall_rpm" style="width:35px;float:left;margin-left:18px;margin-top:16px;"></div>
													
												</div>
												<!--<div class="indented" style=" margin-left: 1%;width: 27%;float:left;border-radius: 4px 4px 4px 4px;">
														<div id="upperheader_chart" style="width:100%;height:70%;"></div>
												
												</div>-->
												<div class="easypie" style="float: right;">
																<div class="percentage4 easyPieChart" data-percent="68"
																	>
																	<span>68</span>%
																	
																</div>
																<div class="easypie-text">FILL RATE</div>
															</div>
											</div></br>
												
										
<!-- start :pie chart for reallocation screen -->
<div style="width:100%;clear:both;margin-top:15px;">
<div id="chart_div_realloc1" class="char_realloc_left" ></div>
<div id="chart_div_realloc2" class="char_realloc_right" ></div>
</div><br>		
<!-- end :pie chart for reallocation screen -->

<!--STERT TABLE-->


<!-- END TABLE -->

<div class="" style="padding:0px;clear:both;">
	<!--div class="height-wrapper"-->
	    <div id="main" role="main" class="container-fluid">
			<div class="contained">
				<!-- main content -->
				<div id="page-content-popup" style="margin-top:0px;">
					<div class="fluid-container">
						<!-- widget grid -->
						<section id="widget-grid" class="">
							<div class="row-fluid">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget">
									    <header>
									      <div style="color:red;">NETWORK PARTNERS DAISY CHAIN PRIORITY
						<span id="budget_allocated" style="padding-left:20px;padding-top:20px;"></span></div>                         
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												<table class="table table-bordered"  id="reallocationPublisher">
													<thead>
														<tr>
															<th>CALL PRIORITY</th>
															<th>NETWORK/RTB</th>
															<th>eCPM($)</th>
															<th>FILL RATE(%)</th>
															<th>IMPRESSIONS</th>
															<th>CLICKS</th>
															<th>REVENUE</th>
															<th>FLOOR CPM($)</th>
															<th>FLOOR CPC($)</th>
																												
														</tr>
													</thead>
													<tbody>
														

														
													</tbody>
												</table>
												
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									</div>
									<!-- end widget -->
								</article>
							</div>
							
							<!-- end row-fluid -->

							

						</section>
						<!-- end widget grid -->
					</div>		
				</div>
				<!-- end main content -->
			
				
			</div>
			
	    </div><!--/.fluid-container-->
		<div class="push"></div>
	<!--/div-->
</div>

										
<div class="modal-footer">

<button class="btn btn-primary" id="sub" onClick="relocationDataTableSaveOrUpdate();">Save changes</button>
</div>
											</p>
										</div>
</s:if>
						<%-- <s:if test="%{authorisationKeywordList.contains('publisherIndNewsResView')}">				
						 	<jsp:include page="newsAndResearch.jsp" /> 
						 </s:if> --%>
						
						<!--news-->
						
						<%-- <s:if test="%{authorisationKeywordList.contains('publisherCampaignView')}">						
							<jsp:include page="campainPerformance.jsp" />
							<div class="tab-pane " id="s6">
								<p>
								<jsp:include page="newAdvertiserViewFilters.jsp" />
								<br>
								<div class="agency">
									<div id="headerDiv" class="mystats indented revenue_bg" style="height: 45px;">
										<div style="width: 98%; float: left;">
											<jsp:include page="newAdvertiserViewHeader.jsp" />
										</div>
									</div>
								</div>
								<jsp:include page="newAdvertiserViewPerformerSummary.jsp" />
							</div>
						</s:if> --%>
						<%-- <s:if test="%{authorisationKeywordList.contains('publisherTreAnsView')}">
							<jsp:include page="trendsAnalysisCampaignPerformance.jsp" />
						</s:if> --%>
<s:if test="%{authorisationKeywordList.contains('publisherDiagToolView')}">
 	<jsp:include page="diagnosticTools.jsp" />
</s:if>
<s:if test="%{authorisationKeywordList.contains('publisherTrafView')}">
	<jsp:include page="trafficking.jsp" />
</s:if>
												
									</div>
									
									<!-- end content -->	
									
								</div>
			
							</div>
		              <!-- end widget -->
					    </article>
							<!-- end widget div -->
					</div>
						
				</div>
				<!-- end: tabs view -->
				
				</div> 
				<!-- end of Actual View -->
				
				
				<!-- start of revenueSummaryPrintView -->
				<div id="revenueSummaryPrintView" style="display:none;">
				
				<div id="page-content" class="">
			           
				<!-- tabs view -->
				<!-- <div class="row-fluid"> -->
					<!-- <article class="span12"> -->
						<!-- new widget -->
						<!-- <div class="jarviswidget"> -->
							<!-- widget div-->
							<!-- <div> -->
								<!-- widget edit box -->
								<%-- <div class="jarviswidget-editbox">
									<div>
										<label>Title:</label>
										<input type="text" />
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
								</div> --%>
								<!-- end widget edit box -->
								
					<!-- <div id="tab-content1" class="tab-content"> -->
					<!-- <div id="ajax_id" class="ajax_loader">
<div class="ajax_tittle">LOADING</div>
<div class="ajax_image">
<img src="img/loaders/type6/light/80.gif" alt="loader">
</div>
</div> -->
<!-- modal popover stucture -->



<!-- <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="top:14%;left:8%;right:8%;margin-left:0px;display:none;">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel"></h3>
	</div>
	<div class="modal-body" id ="modalDivId">

	</div>

</div> -->									
<!-- modal popover stucture -->	
<%-- <s:if test="%{authorisationKeywordList.contains('publisherInvRevView')}"> --%>
										<!-- <div class="tab-pane active" id="s1"> -->
											<!-- upper menu -->
											
											<div class="publis_er1" >
											
												<!-- <div style="float:left;">
														<div class="publisher_outer">PUBLISHER :</div>
														<div id="first_publisher" class="publisher_outer_name"></div>
														<div id="first_channel" class="channel_outer">CHANNELS :</div>
												
												</div> -->
												
												<!-- <div>
												<a title="Cancel" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getRevenueSummaryPrintView('false');"><i class="cus-cross"></i></a>
												<a href="//pdfcrowd.com/url_to_pdf/" style="font-family:arial;color:white;margin-right: 2%;float: right;">Save as PDF</a>
												</div> -->
												
											<div id="publisher_inventory_revenue_header_printView" class="mystats indented revenue_bg">
												<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 0%;">
													<div class="summary_bar" >
														<div class="summary_Name">SITES</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">IMPRESSIONS DELIVERED</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CLICKS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CTR</div>
													<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">eCPM&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
													</div>
													
													<div class="summary_bar">
														<div class="summary_Name">RPM&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
														
													</div>
													<div class="summary_bar">
														<div class="summary_Name">PAYOUTS&nbsp;&nbsp;&nbsp;(Estimated)</div>
														<div class="summary_value"></div>
													</div>
												</div>
												<div id="c3">
												</div>			
											</div>
											<!-- end upper menu -->
											
																		        		
							<div class="row-fluid">
								
								<article class="span12">
									<!-- new widget -->
									
									<div class="jarviswidget">
									    <header>
									        <h2><div style="float:left;">Channel Performance</div><!-- <div id="preloader" style="display:none;margin-top:5px;width:100%;position:absolute;float:left;margin-left:225px;">
									        	<img src="img/loaders/type1/light/24.gif" alt="loader">
									        	
									    		</div> -->
									    </h2>                           
									    </header>
									    <!-- wrap div -->
									    <div style="zoom: 90%;">
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer "> 
									        <!-- content goes here -->
									        
									        	<!-- row-fluid -->
										        	
										        	<!-- span6 -->
															
											           	<table style="border:1px solid #DDDDDD;" class="table table-striped table-bordered responsive" id="just-table-printView">
																				<thead>
																					<tr>
																					    <!-- <th></th> -->
																						<th>Sales Channel</th>
																						<th>eCPM</th>
																						<th>CHG</th>
																						<th>CHG%</th>
																						<th>IMPRESSIONS DELIVERED</th>
																						<th>Clicks</th>
																						<th>CTR%</th>
																						<th>Payout</th>
																					</tr>
																				</thead>
																				<tbody>
																					
																				</tbody>
																			</table>
																			<!-- <div id="sales_Channel_printView" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
													   
													   
													   
												        
										        	<!-- end span6 -->
										        	
									        	<!-- end row-fluid -->

										    <!-- end content-->
										    </div>
									    
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
							
							</div>
							<!-- row-fluid -->
							
							
							<div class="row-fluid" style="">
								
								<article class="span12">
									<!-- new widget -->
									
									<div class="jarviswidget">
									    <header>
									        <h2>
									        <div style="float:left;">Channel Performance</div>
									        
									        <!-- <div id="preloader" style="display:none;margin-top:5px;width:100%;position:absolute;float:left;margin-left:225px;">
									        	<img src="img/loaders/type1/light/24.gif" alt="loader">
									        </div> -->
									    </h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer">
									        <!-- content goes here -->
									        
											
	<!-- <div id="IrPieChart_div_zoom"><i class="icon-zoom-in"></i></div> -->										
	<div id="IrPieChart_div_printView" rel="popover" class="chart" style="width:100%;height: 100%;" ></div>

										    <!-- end content-->
										    </div>
									    
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
							
							</div>
							<!-- row-fluid -->
							
							<%-- <div class="control-group" style="padding:0px;">
			                   <div class="controls">
                                   <select class="span2 with-search" id="selectChannelPublisher" onchange="changeChannelPublisherDropDown();" style="font-size:16px;">
              
                                   </select>
                               </div>
                            </div> --%>
														<!-- row-fluid -->

														<div class="row-fluid" style="page-break-before:always;">
															<article class="span12">
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2><div style="float:left; font-size-adjust: 0.58;">Performance By Property</div>
																		<!-- <div id="preloader1" style="float:left;margin-left: 150px;margin-top:5px;width:100%;position:absolute; text-align:center;"> -->
																		
																		</h2>
																	</header>
																		<!-- wrap div -->
																			<div>
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

																				<div class="inner-spacer">
																			<!-- content goes here -->
																			<table class="table table-striped table-bordered responsive" id="dtable1-printView">
																				<thead>
																					
																					<tr>
																						<th>Property</th>
																						<th>eCPM</th>
																						<th>CHG</th>
																						<th>CHG%</th>
																						<!-- <th>REQUEST SUBMITTED</th>
																						<th>REQUESTS RECEIVED</th>
																						<th>VARIANCE</th> -->
																						<th>IMPRESSIONS DELIVERED</th>
																						<th>Clicks</th>
																						<th>Payout</th>
																					</tr>
																					
																					
																				</thead>
																				<tbody>
																					

																				</tbody>
																			</table>
																			<!-- <div id="perfromace_property" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
																		</div>
																				<!-- end content-->
																		</div>
																		<!-- end wrap div -->
																</div>
																<!-- end widget -->
															</article>
															</div>
															
															
															<div class="row-fluid" style="page-break-before:always;">			
															<article id="performance_geomap_printView" class="span12" style="margin-left:0px;display:none;">
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2>Performance By Property</h2>
																	</header>

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

																		<div class="inner-spacer">
																			<!-- content -->
																			<div class="widget alert alert-warning adjusted">
																				<button class="close" data-dismiss="alert">×</button>
																				<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE:</strong>
																				Google Geochart will only resize during page refresh.
																			</div>
																			<!-- geo chart -->
																			<div id="geomap4_printView" class="chart" style="height: auto;"></div>
																			<!-- end content -->
																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>
														</div>
														
														
					<div class="row-fluid" id="sell_throughdata_printView" style="page-break-before:always;">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget">
									  	<header>
											<h2><div style="float:left">Sell Through Data For Next Week</div>
											<div id="preloader2" style="display:none;float:left;margin-top:1px;margin-left:195px;width:100%;position:absolute; text-align:center;">
		        								<img src="img/loaders/type1/light/24.gif" alt="loader">
		        								
		    								</div>
											</h2>
										</header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												<!--<div class="widget alert alert-info adjusted">
													<button class="close" data-dismiss="alert">Ã—</button>
													<i class="cus-exclamation"></i>
													<strong>Cool export features:</strong> This dynamtic table can also export <strong>PDF</strong> and <strong>Excel</strong> files, feel free to click on the PDF or Excel button to see the result
												</div>-->
												<table class="table table-striped table-bordered responsive" id="sellThroughTable-printView">
													<thead>
														<tr>
																<th>Property / Site</th>
																<th>Ad Unit</th>
																<th>Forecasted Impressions</th>
																<th>Available Impressions</th>
																<th>Reserved Impressions</th>
																<th>STR</th>
															</tr>
													</thead>
													<tbody>
													
													</tbody>
												</table>
												<!-- <div id="sell_through" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
										        	
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
								</div>
								
								
							<div class="row-fluid" id="sell_throughdata_donut_printView" style="">	
								
								<article class="span12" >
																<!-- new widget -->
																<div class="jarviswidget">
																	<header>
																		<h2>Sell Through Rate</h2>
																	</header>

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

																		<div class="inner-spacer" id="container">
																			
																			<!-- geo chart -->
																			<!--<div id="geomap2" class="chart" style="height: auto"></div>-->
																			<%-- <div class="easypie" style="align:center">
																				<div class="percentage1" data-percent="68">
																					<span>0.31</span>%
																				</div>
																				<div class="easypie-text">
																					Sell through rate
																				</div>
																			</div> --%>
																			<!-- end content -->
																		</div>

																	</div>
																	<!-- end widget div -->
																</div>
																<!-- end widget -->
															</article>
			</div>
														
														
														
														
														
														<!-- row-fluid -->
														
														<!-- end row-fluid -->
														
												</div>
										<!-- </div> -->
<%-- </s:if> --%>



												
									<!-- </div> -->
									
									<!-- end content -->	
									
								<!-- </div> -->
			
							<!-- </div> -->
		              <!-- end widget -->
					    <!-- </article> -->
							<!-- end widget div -->
					<!-- </div> -->
						
				</div>
				<!-- End of page content -->
				
				</div>
				<!-- End of revenueSummaryPrintView -->
				
				
				
				<!-- Start of  trendsAnalysisPrintView-->
				<div id="trendsAnalysisPrintView" class="trendsPrintView">
				
			 <div class="jarviswidget-editbox">
	            <div>
	                <label>Title:</label>
	                <input type="text" />
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
	        
	        <!-- <div>
			<a title="Cancel" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getTrendsAnalysisPrintView('false');"><i class="cus-cross"></i></a>
			<a href="//pdfcrowd.com/url_to_pdf/" style="font-family:arial;color:white;margin-right: 2%;float: right;">Save as PDF</a>
			</div> -->
		
											<div class="trands_analysis" >
											
												<!-- <div style="float:left;">
														<div class="publisher_outer">PUBLISHER :</div>
														<div id="second_publisher" class="publisher_outer_name"></div>
														<div id="second_channel" class="channel_outer">CHANNELS :</div>
													
												</div> -->
												
												
											</div>
											
											<div id = "publisher_trends_analysis_header_printView" class="mystats indented" style="background:url('../img/backgrounds/mesh.png') repeat scroll 0 0 #333333;clear:both;height: 100px;">
												<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left:0%;">
													<div class="summary_bar" >
														<div class="summary_Name">REQUESTS</div>
														<div class="summary_value"></div>
														
													</div>
													
													<div class="summary_bar">
														<div class="summary_Name">DELIVERED</div>
														<div class="summary_value"></div>
														
													</div>
													<div class="summary_bar">
														<div class="summary_Name">FILL RATE(%)</div>
														<div class="summary_value"></div>
														
													</div>
												<!-- </div>
												<div class="" style=" margin-left: 3%;width: 15%;float:left;border-radius: 4px 4px 4px 4px;"> -->
													<div class="summary_bar">
														<div class="summary_Name">IMPRESSIONS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CLICKS</div>
														<div class="summary_value"></div>
													</div>
													<div class="summary_bar">
														<div class="summary_Name">CTR</div>
														<div class="summary_value"></div>
													</div>
												
												<!--<div class="indented" style=" margin-left: 1%;width: 27%;float:left;border-radius: 4px 4px 4px 4px;">
														<div id="upperheader_chart_trends" style="width:100%;height:70%;"></div>
												
												</div>-->
												
															<div class="easypie" style="float: right;">
																<div class="percentage2 easyPieChart" data-percent="68">
																	<span>68</span>%
																	<canvas width="150" height="150"></canvas>
																</div>
																<div class="easypie-text">FILL RATE</div>
															</div>
											<!-- </div>
											<div class="" style=" margin-left: 0%;width: 98%;float:left;border-radius: 4px 4px 4px 4px;"> -->
											
													<div class="summary_bar">
														<div class="summary_Name">eCPM</div>
														<div class="summary_value"></div>
													</div>
													<!-- <div class="mychart" id="subscribe" style="width:35px;float:left;margin-left:15px;margin-top:16px;"></div> -->
													<div class="summary_bar">
														<div class="summary_Name">RPM</div>
														<div class="summary_value"></div>
													</div>
													<!-- <div class="mychart" id="support" style="width:35px;float:left;margin-left:18px;margin-top:16px;"></div> -->
													<div class="summary_bar">
														<div class="summary_Name">PAYOUTS</div>
														<div class="summary_value"></div>
													</div>
													
													<!-- <div id="trendAndAnalHeaderLoader" style="display:block;margin-left:50px;">
									        			<img src="img/loaders/type4/light/46.gif" alt="loader">
									        		</div> -->
											</div>
										</div>
										<br>
		
 <!-- Line chart starts -->
 
 
 
    <div class="fluid-container" id="lineChartPublisherDiv_printView" ng-app="lineChartPublisherApp" ng-controller="lineChartpublisherCtrl" >
    <!-- widget grid -->
    
    <section id="widget-grid" class="">
	    <div class="row-fluid" id="ecpmChartId">
			<article class="span6">
				<!-- new widget -->
				<div id= "ecpmlLneChartDiv_printView" class="jarviswidget">
					<header ><h2>ECPM Per Day</h2></header>
				
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
						<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer" style="margin-left:20px;margin-top: 20px;">
						    <!-- <div id="ecpmChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img class="chartLoaderImageClass" src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> --> 
							
							<div id="ecpmChartDiv_printView" class="linechartdatadiv"  google-chart chart="ecpmChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		</div>
		
		 <div class="row-fluid" id="fillChartId">
				<article class="span6">
				<!-- new widget -->
				<div title="Actual Charts"  id= "filllLneChartDiv_printView" class="jarviswidget" >
					<header>
						<h2>FILLRATE Per Day</h2>
						<!-- <div class="jarviswidget-ctrls" role="menu"> 
						     <a href="#" class="button-icon jarviswidget-delete-btn" style="display: block;"><i class="trashcan-10"></i></a>  
						     <a href="#" class="button-icon jarviswidget-edit-btn" style="display: block;"><i class="pencil-10 "></i></a> 
						     <a href="#" class="button-icon jarviswidget-fullscreen-btn"><i class="fullscreen-10"></i></a> 
						     <a href="#" class="button-icon jarviswidget-toggle-btn" style="display: block;"><i class="min-10 "></i></a>
						     </div> -->
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer" style="margin-left:20px;margin-top: 20px;">
						
						      <!-- <div id="fillRateChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img  src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> -->
							
							<div id="fillChartDiv_printView" class="linechartdatadiv" google-chart chart="fillRateChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		</div>
		
		<div class="row-fluid" id="revenueChartId">
			<article class="span6">
				<!-- new widget -->
				<div   id= "revenuelLneChartDiv_printView" class="jarviswidget" >
					<header>
						<h2>REVENUE Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer" style="margin-left:20px;margin-top: 20px;">	
						       <!-- <div id="revenueChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> --> 
							
							<div id="revenueChartDiv_printView" class="linechartdatadiv"  google-chart chart="revenueChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
		</div>
		
		 <div class="row-fluid" id="impressionsChartId">
			
				<article class="span6">
				<!-- new widget -->
				<div title="Actual Charts"  id= "impressionlLneChartDiv_printView" class="jarviswidget" >
					<header>
						<h2>IMPRESSION Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer" style="margin-left:20px;margin-top: 20px;">	
						       <!-- <div id="impressionChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> -->
							
							<div id="impressionChartDiv_printView" class="linechartdatadiv"  google-chart chart="impressionChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
			</div>
			
			<div class="row-fluid" id="clicksChartId">
			<article class="span6">
				<!-- new widget -->
				<div   id= "clicklLneChartDiv_printView" class="jarviswidget" >
					<header>
						<h2>CLICKS Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div class="inner-spacer" style="margin-left:20px;margin-top: 20px;">	
						       <!-- <div id="clickChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> -->
							
							<div  id="clickChartDiv_printView" class="linechartdatadiv"  google-chart chart="clickChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
		</div>
		
		 <div class="row-fluid" id="ctrChartId">
				<article class="span6">
				<!-- new widget -->
				<div title="Actual Charts"  id= "ctrlLneChartDiv_printView" class="jarviswidget" >
					<header>
						<h2>CTR Per Day</h2>
					</header>
					<!-- wrap div -->
					<div>
						<div class="jarviswidget-editbox">
							<div><label>Title:</label> <input type="text" /></div>
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
				
						<div  class="inner-spacer" style="margin-left:20px;margin-top: 20px;">
						       <!-- <div  id="ctrChartLoaderId" class=" lineGraphloaderArea jarviswidget" style="height:170px;text-align:center;">
						     	<img src="img/loaders/type4/light/46.gif" alt="loader">
						     	<div style="text-align: center; margin-top: 100px; display: none; border: 0px;">No Data</div>
						    </div> -->
							
							<div  id="ctrChartDiv_printView" class="linechartdatadiv" google-chart chart="ctrChart" style="width:700px !important;height:200px" on-ready="chartReady()" ></div>
							<!-- <div id="emptyChartdiv" style="height: 200px; display:none">
								<div style="text-align: center; margin-top: 100px;">No Data</div>
							</div> -->
						</div>
						
						<!-- end content-->
					</div>
					<!-- end wrap div -->
				</div>
				<!-- end widget -->
			</article>
			
		</div>	
	 </section>
</div>

			

		<div style="height:15px;clear:both;"></div>
		<!-- div for left table -->
		<!-- row-fluid -->		
		
		<div class="row-fluid" style="border-top:1px solid #ccc;page-break-before:always;">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget">
									  	<header>
											<h2>ACTUAL</h2>
										</header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer">
												<table class="table table-striped table-bordered responsive" id="actualPublisher_printView">
													<thead>
														<tr>
															<th>DAYS</th>
															<th>CHANNELS</th>
															<th>REQUESTS</th>
															<!-- <th>SERVED</th> -->
															<th>IMPRESSION DELIVERED</th>
															<th>FILL RATE</th>
															<th>CLICKS</th>
															<th>CTR(%)</th>
															<th>REVENUE</th>
															<th>eCPM</th>
															<th>RPM</th>
															
														</tr>
													</thead>
													<tbody>
												
													</tbody>
												</table>
										        <!-- <div id="trends_publisher" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->	
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
			</div>
		<!--end-->

				
				
				
				</div>
				<!-- End of  trendsAnalysisPrintView-->
				
				
			<!--Start of diagnosticToolsPrintView  -->

<div id="diagnosticToolsPrintView" style="display:none; zoom: 90%;">

		<!-- row-fluid -->

			<!-- <div>
			<a title="Cancel" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getDiagnosticToolsPrintView('false');"><i class="cus-cross"></i></a>
			<a href="//pdfcrowd.com/url_to_pdf/" style="font-family:arial;color:white;margin-right: 2%;float: right;">Save as PDF</a>
			</div> -->

		<div class="row-fluid">
			<article class="span12">
				<!-- new widget -->
				<div class="jarviswidget">
					<header>
						<h2>
						RECONCILIATION SUMMARY
						</h2>
					</header>
					
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

								<div class="inner-spacer">
							<!-- content goes here -->
							<table class="table table-striped table-bordered responsive" id="reconciliationSummaryTable_printView">
								<thead>
									<tr>
										<th class="reconcilation_table_style"  colspan="1">Demand Partners</th>
										<th class="reconcilation_table_style" colspan="3">DFP Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Demand Partner Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Variance</th>
									</tr>
									<tr>
										<th class="reconcilation_table_style"></th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
							<!-- <div id="reconciliationSummaryLoader" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
						</div>
								<!-- end content-->
				</div>
				<!-- end widget -->
			</article>
		</div>
		
		
	        <!-- row-fluid -->
		<div class="row-fluid" style="border-top:1px solid #ccc;page-break-before: always;">
			<article class="span12">
				<!-- new widget -->
				<div class="jarviswidget">
				  	<header id="reconciliationDetailsHeader_printView">
						<h2>RECONCILIATION DETAILS</h2>
					</header>
				    
				        <div class="jarviswidget-editbox">
				            <div>
				                <label>Title:</label>
				                <input type="text" />
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
       
				        <div class="inner-spacer">
							<table class="table table-striped table-bordered responsive" id="reconciliationDetailTable_printView">
								<thead>
									<tr>
										<th class="reconcilation_table_style" colspan="1">Date</th>
										<th class="reconcilation_table_style" colspan="3">DFP Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Demand Partner Reporting</th>
										<th class="reconcilation_table_style" colspan="3">Variance</th>
									</tr>
									<tr>
										<th class="reconcilation_table_style"></th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
										<th class="reconcilation_table_style">Requests</th>
										<th class="reconcilation_table_style">Delivered</th>
										<th class="reconcilation_table_style">Passbacks</th>
									</tr>
								</thead>
								<tbody>
							
								</tbody>
							</table>
					        <!-- <div id="reconciliationDetailLoader" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->	
					    </div>
					    <!-- end content-->
				</div>
				<!-- end widget -->
			</article>
		</div>	

</div>
<!-- End of diagnosticToolsPrintView -->


<!-- Start of traffickingSummaryPrintView -->

<div id="traffickingSummaryPrintView" style="display:none">

		<div class="inner-spacer widget-content-padding" style="min-height:1300px"> 
			<!-- content -->
			<div id="pop-up" style="width:20px;height:10px;border:0px solid black;margin-left:40%;"></div>
				<div id="myTabContent" class="tab-content">
					<div class="tab-pane fade in active">

						<div class="fluid-container">

						<!-- widget grid -->
							<section id="widget-grid" class="">
							
							<!-- <div>
							<a title="Cancel" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="javascript:getTraffickingSummaryPrintView('false');"><i class="cus-cross"></i></a>
							<a href="//pdfcrowd.com/url_to_pdf/" style="font-family:arial;color:white;margin-right: 2%;float: right;">Save as PDF</a>
							</div> -->

								<div class="row-fluid">

									<article class="span12" id="campaign_selected_printView" style="margin-left: 0;display:none;">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-0 " style="">
									<!-- <button type="button" class="close2" style="opacity: 0.84; margin-top: -18px">&times;</button> -->
									    <header id="tableTitle">
									        <h2>List of Campaign Delivering</h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												
												<!-- <table class="table table-striped table-bordered responsive" id="dtable"> -->
												<table class="table table-striped table-bordered responsive" id="selectedLineItemsTable_printView">
													<thead>
														<tr>
															<th>Campaign Name</th>
															<th>Goal Quality</th>
															<th>CPM</th>
															<th>Start Date</th>
															<th>End Date</th>
														</tr>
													</thead>
													<tbody>
														
													</tbody>
												</table>
										        <!-- <div id="selectedLineItemsLoaderId" style="text-align:center;">
												<img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
							</div>				
									
									<!-- new widget -->
							<div class="row-fluid" id="totalCampaignsId">
									<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-1">
									    <header>
									        <h2>TOTAL CAMPAIGNS</h2>                           
									    </header>
									    <!-- wrap div -->
									    <div>
									    
									        <div class="jarviswidget-editbox">
									            <div>
									                <label>Title:</label>
									                <input type="text" />
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
            
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
									        
												<!-- <table class="table table-striped table-bordered responsive" id="col-filter"> -->
												<table class="table table-striped table-bordered responsive" id="col-filter-printView">
													<thead>
														<tr>
															<th>Campaign Name</th>
															<th>Goal Quality</th>
															<th>CPM</th>
															<th>Start Date</th>
															<th>End Date</th>
														</tr>
													</thead>
													<tbody>
														
													</tbody>
												</table>
												<!-- <div id="traffickingAllDataLoaderId" style="text-align:center;">
												<img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
											</div>
										</div>
									</div>
								</article>
														
							</div>
						<!-- row-fluid -->

														
							</section>
					</div>
											
										</div>
										<div  id="popoverData" class="" href="#" data-html="" rel="popover" data-placement="center" data-original-title="" >	</div>
										
										<div id="popover_content_wrapper" style="display: none;width:480px;">
										
										 
										</div>
										
									</div>
										
									
						
									</div>
									<!-- end content -->



</div>				
			
		<!-- End of traffickingSummaryPrintView -->
				
			<!-- </div> -->
			<!-- end main content -->



			<!-- aside right on high res -->
			<%-- <aside class="right">
			  <jsp:include page="RightPanel.jsp"/> 
			</aside> --%>
			<!-- end aside right -->
		</div>
		<s:iterator value="authorisationKeywordList" var="value">
			<input type="hidden" id="<s:property value="value"/>" value="1">
		</s:iterator>
		
		<%-- <s:iterator value="authorisationTextList" status="stat">
			<s:if test="permission == 1">
				<input type="hidden" id="<s:property value="authorisationTextKeyword"/>" value="1">
			</s:if>
		</s:iterator> --%>

	</div>
	<!--end fluid-container-->
	<div class="push"></div>

<!-- end .height wrapper -->

<!-- footer -->

<!-- if you like you can insert your footer here -->

<!-- end footer -->

<!--================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

			<!-- Le javascript
    ================================================== --> 
 <jsp:include page="js.jsp"/>
	
 <script src="../js/angular/angular.min.js"></script>
 <script src="../js/angular/ng-google-chart.js"></script>
 <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.js"></script>
 <script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>
 <script type="text/javascript" src="../js/bootstrap-select.js"></script>
  <script src="../js/angular/controller/publisher-line-chart-data.js"></script>
 
 <script>  
 function defaultMethods() {
	 angular.bootstrap($("#lineChartPublisherDiv"),['lineChartPublisherApp']);
	 angular.bootstrap($("#lineChartPublisherDiv_printView"),['lineChartPublisherApp']);
	/*  loadAdvertiserPropertyList(); */
     loadAllDataPublisher();
     getAllReconciliationData();
 }
 $('.carousel').carousel({
	    interval: 2000
	    });
$(document).ready(function(){
	$('#indus_new').click(function(){
		$('#s4').show();
	});
});

		
		$(document).ready(function(){
			$('#inv_rev').click(function(){
				$('#s4').hide();
			});
		});
		$(document).ready(function(){
			$('#tre_ana').click(function(){
				$('#s4').hide();
				
				angular.bootstrap($("#lineChartPublisherDiv"),['lineChartPublisherApp']);
				angular.bootstrap($("#lineChartPublisherDiv_printView"),['lineChartPublisherApp']);
				//angular.element(document.getElementById("geoChartDiv")).scope().initdata();
				
				var oTableTools = TableTools.fnGetInstance('actualPublisher');
				if ( oTableTools != null && oTableTools.fnResizeRequired()) {
					oTableTools.fnResizeButtons();
				}
			});
		});
	
		$(document).ready(function(){
			$('#rea_inv').click(function(){
				$('#s4').hide();
			});
		});
		
		$(document).ready(function(){
			$('#per_sum_publisher').click(function(){
				$('#s4').hide();
				if(initialLoad){
					getUserAccountsDropDownData();
				}
			});
		});
		

</script>
<input type="hidden" id= "tabClick" name = "tabClick"/> 



</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>