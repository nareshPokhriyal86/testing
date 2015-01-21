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
	$('#roleLi').attr('class', 'active');
});
	localStorage.clear();
</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Roles</title>
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
		<s:form name="roleSetupForm" action="">
		<div class="contained">
			<!-- aside -->
			<!-- aside end -->
									
			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
			<h1 id="page-header">Roles</h1>
			<div >
				<input class="btn btn-success" onclick="location.href='initCreateRole.lin'" style="margin-bottom: 15px;" type="button" value="New Role">
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
																<th>Name</th>
																<th>Role Type</th>
																<th>Status</th>
																<th style="width:60%;">Description</th>
															</tr>
														</thead>
														<tbody>
														 <s:iterator value="rolesAndAuthorisationDTOList" status="stat">
															<!-- <tr <s:if test="roleType != definedTypeBuiltIn"> onclick="setUpdateRoleId('<s:property value='id'/>', 'hiddenVal');" style="cursor: hand; cursor: pointer;" </s:if>> -->
															<tr onclick="setUpdateRoleId('<s:property value='id'/>', 'hiddenVal');" style="cursor: hand; cursor: pointer;">
																<td><s:property value="roleName"/></td>
																<td><s:property value="roleType"/></td>
																<td><s:property value="roleStatus"/></td>
																<td><s:property value="roleDescription"/></td>
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
				<input type="hidden" id="hiddenVal" name="roleId" value="" >
				
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


<!-- end scripts -->
		<s:if test="%{linStatus == 'failed'}">
			<script type="text/javascript"> errorMessage('Some problem occured, please try again'); </script>
		</s:if>
		<s:elseif test="%{updateRoleStatus == 1}">
			<script type="text/javascript"> successMessage('Role updated successfully'); </script>
		</s:elseif>
		<s:elseif test="%{updateRoleStatus == 0}">
			<script type="text/javascript"> errorMessage('Role update failed'); </script>
		</s:elseif>
		<s:elseif test="%{deleteRoleStatus == 1}">
			<script type="text/javascript"> successMessage('Role deleted successfully'); </script>
		</s:elseif>
		<s:elseif test="%{deleteRoleStatus == 0}">
			<script type="text/javascript"> errorMessage('Role delete failed'); </script>
		</s:elseif>
		<s:elseif test="%{copyPermissionStatus == 0}">
			<script type="text/javascript"> errorMessage('Copy Permission failed'); </script>
		</s:elseif>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
