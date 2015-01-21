<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.lin.web.dto.ProposalDTO"%>
    <%@taglib uri="/struts-tags" prefix="s" %>
    <jsp:include page="Header.jsp" />
<s:set name="theme" value="'simple'" scope="page" />
<%@page import="com.lin.web.util.TabsName" %>
<!DOCTYPE html>

<html lang="en">
<head>
		
<script>
	var pageName = '<s:property value="#session.sessionDTO.pageName"/>';
	var campaignTab = '<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%>';
	localStorage.clear();
</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Operational Dashboard</title>
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

%>
<jsp:include page="css.jsp"/>
<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>
<script type='text/javascript' src='https://www.google.com/jsapi'></script>
<script type="text/javascript" src="../js/donutChart.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/common-graphs.js?v=<s:property value="deploymentVersion"/>"></script>
<script type='text/javascript' src='../js/operations.js?v=<s:property value="deploymentVersion"/>'></script>

</head>

<body >
<!-- .height-wrapper -->
<div class="height-wrapper">


	<div id="main" role="main" class="container-fluid">

		<div class="contained">
			<!-- aside -->
			
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			
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

								<div class="inner-spacer widget-content-padding"> 
									<!-- content -->	
									<ul class="nav nav-tabs upper_tabs" id="myTab1">
									
										<!-- <li id="myTab1_1">
											<a href="#s5" onclick = "operationsTabClickValue(this);" id="diagnosticTools" class="upper_tab1">Diagnostic Tools</a>
										</li>
										
										<li id="myTab1_2">
											<a href="#s7" onclick = "operationsTabClickValue(this);" id="trafficking">Trafficking</a>
										</li> -->
										
										<li class="active" id="myTab1_3">
											<a href="#s10" onclick = "operationsTabClickValue(this);" id="mediaPlanner"><%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%></a>
										</li>
										
										<jsp:include page="DatePicker.jsp" />	
									</ul>
					<div id="tab-content1" class="tab-content">

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

<!-- <div class="tab-pane active" id="s1">
</div>
<div class="tab-pane" id="s2">
</div>
<div class="tab-pane" id="s3">
</div> -->
										
						 	<jsp:include page="diagnosticTools.jsp" />
						
							<jsp:include page="trafficking.jsp" />
							
						<div  id="s10" class="tab-pane" style="display:none;">
							<%-- <jsp:include page="CreateProposal.jsp" /> --%>
						</div>						
									</div>
									
									<!-- end content -->	
									
								</div>
			
							</div>
		
							<!-- end widget div -->
						</div>
						<!-- end widget -->
					</article>
				</div>
				<!-- end: tabs view -->
				
			</div>
			<!-- end main content -->
			
		</div>
		<s:iterator value="authorisationKeywordList" var="value">
			<input type="hidden" id="<s:property value="value"/>" value="1">
		</s:iterator>
		
	</div>
	<!--end fluid-container-->
	<div class="push"></div>
</div>
<!-- end .height wrapper -->

<!-- footer -->

<!-- if you like you can insert your footer here -->

<!-- end footer -->

<!--================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

			<!-- Le javascript
    ================================================== --> 
<script src="../js/dataManipulation.js?v=<s:property value="deploymentVersion"/>"></script>
 <jsp:include page="js.jsp"/>
 <script src="../js/diagnosticTools.js?v=<s:property value="deploymentVersion"/>"></script>
 
<script>
function defaultMethods() {
	getAllReconciliationData();
}
$(document).ready(function(){
	$('#s5').addClass("active");
	$('#indus_new').click(function(){
		$('#s4').show();
	});
	$('#inv_rev').click(function(){
		$('#s4').hide();
	});
	$('#tre_ana').click(function(){
		$('#s4').hide();
		var oTableTools = TableTools.fnGetInstance('actualPublisher');
		if ( oTableTools != null && oTableTools.fnResizeRequired()) {
			oTableTools.fnResizeButtons();
		}
	});
	$('#diagnosticTools').click(function(){
		$('#s4').hide();
		var oTableTools = TableTools.fnGetInstance('reconciliationDetailTable');
		if ( oTableTools != null && oTableTools.fnResizeRequired()) {
			oTableTools.fnResizeButtons();
		}
	});
	$('#rea_inv').click(function(){
		$('#s4').hide();
	});
	$('#per_sum_publisher').click(function(){
		$('#s4').hide();
	});
	$('#trafficking').click(function(){
		$('#s4').hide();
		var oTableTools = TableTools.fnGetInstance('col-filter');
		if ( oTableTools != null && oTableTools.fnResizeRequired()) {
			oTableTools.fnResizeButtons();
		}
	});
});
$('.carousel').carousel({
    interval: 2000
    });


</script>
<input type="hidden" id= "tabClick" name = "tabClick"/> 

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
