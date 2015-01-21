<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

<html lang="en">
<head>
		
<script>
localStorage.clear();
$(document).ready(function() {
	$('#companySettingsLi').attr('class', 'active');
	if($('#radioAdserverOther').attr("checked") == "checked") {
   		removeCssClassOnDFPCredentials();
   	}
});
var adServerCredentialsCounter = '<s:property value="adServerCredentialsCounterValue"/>';
var passbackSiteTypeCounter = '<s:property value="passbackSiteTypeCounterValue"/>';
var serviceURLCounter = '<s:property value="serviceURLCounterValue"/>';
</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Company Settings Saved</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
	<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>
	
	<!-- Le javascript
    ================================================== -->
<script src="../js/include/jquery.min.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/include/jquery.jgrowl_minimized.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/common.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/userManagement.js?v=<s:property value="deploymentVersion"/>"></script>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->
	
	<!-- end header -->

	<div id="main" role="main" class="container-fluid">

		<div class="contained">
			<!-- aside -->
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			<jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
			<s:if test="%{linStatus == 'updateFailed'}">
				<h1 id="page-header">Company Settings Save Failed</h1>
			</s:if>
			<s:elseif test="%{linStatus == 'updateSuccess'}">
				<h1 id="page-header">Company Settings Saved</h1>
			</s:elseif>
            <div class="row-fluid Profile">
								<!-- article -->	
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-1">
									    
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
									      <%--   <div class="row-fluid dt-header"><div class="span6"><div class="dataTables_filter" id="userSetupTable_filter"><label><input type="text" aria-controls="userSetupTable" placeholder="Search Filter"></label></div></div><div class="span6 hidden-phone"><div class="DTTT_container"><a class="DTTT_button DTTT_button_print" id="ToolTables_userSetupTable_0" title="View print view"><span><i class="cus-printer oTable-adjust"></i> Print View</span></a><a class="DTTT_button DTTT_button_pdf" id="ToolTables_userSetupTable_1"><span><i class="cus-doc-pdf oTable-adjust"></i> Save to PDF</span><div style="position: absolute; left: 0px; top: 0px; width: 97px; height: 26px; z-index: 99;"><embed id="ZeroClipboard_TableToolsMovie_1" src="js/include/assets/DT/swf/copy_csv_xls_pdf.swf" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="97" height="26" name="ZeroClipboard_TableToolsMovie_1" align="middle" allowscriptaccess="always" allowfullscreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="id=1&amp;width=97&amp;height=26" wmode="transparent"></div></a><a class="DTTT_button DTTT_button_xls" id="ToolTables_userSetupTable_2"><span><i class="cus-doc-excel-table oTable-adjust"></i> Save for Excel</span><div style="position: absolute; left: 0px; top: 0px; width: 109px; height: 26px; z-index: 99;"><embed id="ZeroClipboard_TableToolsMovie_2" src="js/include/assets/DT/swf/copy_csv_xls_pdf.swf" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="109" height="26" name="ZeroClipboard_TableToolsMovie_2" align="middle" allowscriptaccess="always" allowfullscreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="id=2&amp;width=109&amp;height=26" wmode="transparent"></div></a></div></div></div> --%>
									        <!-- content goes here -->
		<!-- -----------------   Form starts   --------------------------- -->
						<s:form name = "companySettingsSavedForm" id="select-demo-js" cssClass="form-horizontal themed" >
						</s:form>

		<!-- -----------------   Form Ends   --------------------------- -->
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
								<!-- end article-->
			</div>
            <!-- end content-->
			</div>
									    <!-- end wrap div -->
		</div>
									<!-- end widget -->
	</div>                                                                         

			<!-- end main content -->



			<!-- aside right on high res -->
			
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
<jsp:include page="js.jsp"/>

	<s:if test="%{linStatus == 'updateFailed'}">
		<script type="text/javascript"> errorMessage('Some problem occured, please try again'); </script>
	</s:if>
	<s:elseif test="%{linStatus == 'updateSuccess'}">
		<script type="text/javascript"> successMessage('Company updated Successfully'); </script>
	</s:elseif>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
