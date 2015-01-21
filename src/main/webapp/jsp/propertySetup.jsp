<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

<html lang="en">
<head>
		
		<script>
		$(document).ready(function() {
			$('#propertyLi').attr('class', 'active');
		});
			localStorage.clear();
		</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Properties</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
	<!-- Le javascript
    ================================================== -->
<script src="../js/include/jquery.min.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/include/jquery.jgrowl_minimized.js?v=<s:property value="deploymentVersion"/>"></script>
<script src="../js/userManagement.js?v=<s:property value="deploymentVersion"/>"></script>

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
		<s:form name="propertySetupForm" action="">
		<div class="contained">
			<!-- aside -->
			<!-- aside end -->
									
			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
			<h1 id="page-header">Property Setup</h1>
			<div>
				<input class="btn btn-success" onclick="location.href='initCreateProperty.lin'" style="margin-bottom: 15px;" type="button" value="New Property">
			</div>
              <div class="row-fluid" style="border-top:1px solid #ccc;">
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
																<th style="width:20%;">Property Name</th>
																<th style="width:20%;">DFP Property Name</th>
																<th style="width:20%;">DFP Credentials</th>
																<th style="width:12%;">Company</th>
																<th style="width:18%;">Market</th>
																<th style="width:10%;">Status</th>
															</tr>
														</thead>
														<tbody>
														 <s:iterator value="propertySetupDTOList" status="stat">
															<tr onclick="setPropertyId('<s:property value='id'/>', 'hiddenVal');" style="cursor: hand; cursor: pointer;">
																<td><s:property value="propertyName"/></td>
																<td><s:property value="DFPPropertyName"/></td>
																<td><s:property value="adServerId"/></td>
																<td><s:property value="companyId"/></td>
																<td><s:property value="market"/></td>
																<td><s:property value="status"/></td>
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
				<input type="hidden" id="hiddenVal" name="propertyId" value="" >
				
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

		<s:if test="%{linStatus == 'failed'}">
			<script type="text/javascript"> errorMessage('Some problem occured, please try again'); </script>
		</s:if>
		<s:elseif test="%{updatePropertyStatus == 1}">
			<script type="text/javascript"> successMessage('Property updated Successfully'); </script>
		</s:elseif>
		<s:elseif test="%{updatePropertyStatus == 0}">
			<script type="text/javascript"> errorMessage('Property update failed'); </script>
		</s:elseif>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
