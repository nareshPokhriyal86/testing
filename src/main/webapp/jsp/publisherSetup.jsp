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
		  'page': 'userSetup.jsp',
		  'title': 'User Settings',
		  'dimension1': '<s:property value="#session.sessionDTO.userId"/>',
		  'dimension2': '<s:property value="#session.sessionDTO.userName"/>'
		});
  /* ga('set', 'dimension1', '<s:property value="#session.sessionDTO.userId"/>');
  ga('set', 'dimension2', '<s:property value="#session.sessionDTO.userName"/>'); */
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
	
	<!-- Le javascript
    ================================================== --> 
<script src="../js/include/jquery.min.js"></script>
<script src="../js/include/jquery.jgrowl_minimized.js"></script>
<script src="../js/common.js"></script>
<script src="../js/userManagement.js"></script>

<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>

</head>

<body>
<!-- .height-wrapper -->
<div class="height-wrapper">
	<!-- header -->

	<!-- end header -->

	<div id="main" role="main" class="container-fluid">
		<s:form name="companySetupForm" action="">
		<div class="contained">
			<!-- aside -->
			<jsp:include page="userManagementLeftMenu.jsp"/>
			<!-- aside end -->
									
			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			<div style="text-align: right; float: right;">
				<input class="btn btn-success" onclick="location.href='initCreatePublisher.lin'" style="margin-top: 15px;" type="button" value="Create New Publisher">
			</div>
			 <h1 id="page-header" style="position:absolute;">Publisher Setup</h1>
              <div class="row-fluid" style="border-top:1px solid #ccc; margin-top:110px;">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget">
									  
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
												
												
									<div class="row-fluid">
	            
										        <div class="inner-spacer"> 
										        <!-- content goes here -->
													<table class="table dtable checked-in responsive has-checkbox" id="sc-table">
														<thead>
															<tr>
																<th>publisher Name</th>
																<th width="50%">Demand Partners</th>
																
															</tr>
														</thead>
														<tbody>
														 <s:iterator value="publisherSetupList" status="stat">
															<tr onclick="setCompanyName('<s:property value='id'/>', 'hiddenVal');" style="cursor: hand; cursor: pointer;">
																<td><s:property value="publisherName"/></td>
																<td><s:property value="channelObject"/></td>
																
															</tr>
														 </s:iterator>									
														</tbody>
													</table>
													
											    </div>
										    <!-- end wrap div -->
								
									</div>
				
								</div>
								</div>
							</article>
					</div>
				</div>
			</div>
				
				<!-- end: tabs view -->
				<input type="hidden" id="hiddenVal" name="companyId" value="" >
				
			</s:form>
	  </div>
			<!-- end main content -->



			<!-- aside right on high res -->
						

			<!-- end aside right -->
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
