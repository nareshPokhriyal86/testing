<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
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

<%-- <script src="../js/accounting.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/donutChart.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/common-graphs.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/publisher.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/dataManipulation.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/operations.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/diagnosticTools.js?v=<s:property value="deploymentVersion"/>"></script> --%>
<script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.6.0.js"></script>

<script src="../js/revenueSummaryPrintView.js"></script>

<script type="text/javascript">

  $(document).ready(function(){
	  alert("within document.ready() of RevenueSummaryPrintView.jsp");
   getDataForRevenueSummaryPrintView();
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

			<!-- main content -->
			
			<div id="page-content" class="mlr">
			           
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
												
												<div>
												<a title="Cancel" class="btn" style="float: right; font-size: 12px; margin-right: 1%;" href="/publisher.lin"><i class="cus-cross"></i></a>
												<a href="//pdfcrowd.com/url_to_pdf/" style="font-family:arial;color:white;margin-right: 2%;float: right;">Save as PDF</a>
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
											</div>
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
															
											           	<table style="border:1px solid #DDDDDD;" class="table table-striped table-bordered responsive" id="just-table-printView">
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
																			<!-- <div id="sales_Channel_printView" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
													   
													   
													   
												        
										        	</div>
										        	<!-- end span6 -->
										        	
										        	<!-- span6-->
										        	<div class="span3">
										        		<!-- well -->
											
	<!-- <div id="IrPieChart_div_zoom"><i class="icon-zoom-in"></i></div> -->										
	<div id="IrPieChart_div_printView" rel="popover" class="chart" style="width:100%;height: 100%;" ></div>


			
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
							
							<%-- <div class="control-group" style="padding:0px;">
			                   <div class="controls">
                                   <select class="span2 with-search" id="selectChannelPublisher" onchange="changeChannelPublisherDropDown();" style="font-size:16px;">
              
                                   </select>
                               </div>
                            </div> --%>
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
												<table class="table table-striped table-bordered responsive" id="sellThroughTable-printView">
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
												<!-- <div id="sell_through" class="span12" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div> -->
										        	
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
										</div>
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

</html>
