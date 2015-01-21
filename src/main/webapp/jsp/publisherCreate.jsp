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
				
		</script>
		
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-41865102-1', 'linmobile-backend.appspot.com');
  ga('send', 'pageview', {
		  'page': 'userCreate.jsp',
		  'title': 'Create New User'
		});
  ga('set', 'dimension1', '<s:property value="#session.sessionDTO.userId"/>');
  ga('set', 'dimension2', '<s:property value="#session.sessionDTO.userName"/>');

</script>		
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>SynergyMAP</title>
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
<script src="../js/include/jquery.min.js"></script>
<script src="../js/include/jquery.jgrowl_minimized.js"></script>
<script src="../js/common.js"></script>
<script src="../js/userManagement.js"></script>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->
	
	<!-- end header -->

	<div id="main" role="main" class="container-fluid">

		<div class="contained">
			<!-- aside -->
			<jsp:include page="userManagementLeftMenu.jsp"/>
			<!-- aside end -->

			<!-- main content -->
			<div id="page-content" class="mlr">
			<%-- <jsp:include page="filter.jsp"/> --%>
            <h1 id="page-header">Create New Company</h1>
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
						<s:form name = "createPublisherForm" id="select-demo-js" cssClass="form-horizontal themed" action="createPublisher.lin">
							<fieldset>
							
							<div class="control-group">
								<label class="control-label">Publisher Name<span class="req star"
									>*</span>
								</label>
								<div class="controls">
									<input class="span4" required="required" title="Company Name is required" maxlength="49" type="text" id="publisher" name="publisherName">
								</div>
							</div>
							
							<div class="control-group">
                              <label class="control-label">Demand Partners 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4" id="statusId" name="companyType"
            								list="demandPartnersList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div> 
                             </div>
                             
                             <div class="control-group">
                              <label class="control-label">Companies 
                              	<span  class="help-Helpwidgets-TooltipWidget"></span>
							  </label>
                                    <div class="controls">
                                    <s:select cssClass="span4" id="statusId" name="companyType"
            								list="companyList" listKey="id" listValue="value">
            						</s:select>
                                    <br>
                                   	</div> 
                             </div>

							<div class="control-group">
								<div class="controls" class="shadow">
									<button type="submit" id="submitButton" class="btn btn-success btn-large">Save</button>
									<button type="reset" class="btn btn-danger btn-large">Cancel</button>
								</div>
							</div>

						 </fieldset>
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

</body>
</html>
