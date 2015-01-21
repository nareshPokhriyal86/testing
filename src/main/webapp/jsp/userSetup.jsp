<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

<html lang="en">
<head>
		
<script>
$(document).ready(function() {
	$('#userLi').attr('class', 'active');
});
	localStorage.clear();
</script>
		
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>ONE - Users</title>
<meta name="description" content="">
<meta name="author" content="">

<!-- http://davidbcalhoun.com/2010/viewport-metatag -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	
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
	<%-- <jsp:include page="filter.jsp"/> --%>
		<s:form name="userSetupForm" action="">
		
		<div class="contained">
			<!-- aside -->
			
			<!-- aside end -->


			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
            <h1 id="page-header">Users</h1>
            <div>
				<input class="btn btn-success" onclick="location.href='initCreateUser.lin'" style="margin-bottom: 15px;" type="button" value="New User">
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
									        <div class="inner-spacer"> 
									        <!-- content goes here -->
												<!--<div class="widget alert alert-info adjusted">
													<button class="close" data-dismiss="alert">×</button>
													<i class="cus-exclamation"></i>
													<strong>Cool export features:</strong> This dynamtic table can also export <strong>PDF</strong> and <strong>Excel</strong> files, feel free to click on the PDF or Excel button to see the result
												</div>-->
												<table class="table dtable" id="userSetupTable">
													<thead>
														<tr>
															<th>Name</th>
															<th>Email</th>
															<th>Status</th>
															<th>Role</th>
															<th style="text-align:center;">Edit</th>
															<s:if test="%{#session.sessionDTO.superAdmin}">
																<th style="text-align:center;">Remove</th>
															</s:if>
														</tr>
													</thead>
													<tbody>
													<s:iterator value="usersList" status="stat">
														<tr id="userIdToDelete_<s:property value='id'/>" cssClass="odd gradeX">
															<td><s:property value="userName"/></td>
															<td><s:property value="emailId"/></td>
															<td>
																<div id="status_<s:property value='id'/>"><s:property value="status"/></div>
																<s:if test="%{status.equals('Pending')}">
																	<div id="invite_<s:property value='id'/>">
																		<a style="color: blue;" href="javascript:resendAuthorizeEmail('<s:property value='id'/>');">Resend Invite</a>
																	</div>
																</s:if>
															</td>
															<td class="center"><s:property value="role"/></td>
															<td class="center" style="text-align:center;">
																<a href="javascript:setUpdateUserId('<s:property value='id'/>', 'hiddenVal');"><i class="cus-page-white-edit"></i></a>
															</td>
															<s:if test="%{#session.sessionDTO.superAdmin}">
																<td class="center" style="text-align:center;">
																	<a href="javascript:removeUser('<s:property value='id'/>');"><i class="cus-cancel"></i></a>
																</td>
															</s:if>
														</tr>
													</s:iterator>
													</tbody>
												</table>
										        	
										    </div>
										    <!-- end content-->
									    </div>
									    <!-- end wrap div -->
									</div>
									<!-- end widget -->
								</article>
			</div>
									    <!-- end wrap div -->
			</div>
				
				</div>
				<!-- end: tabs view -->
				<input type="hidden" id="hiddenVal" name="id" value="" >
			</s:form>	
	  </div>
			<!-- end main content -->

	<!--end fluid-container-->
	<div class="push"></div>
</div>
<!-- end .height wrapper -->

<!--================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

	<!-- Le javascript
    ================================================== --> 
<jsp:include page="js.jsp"/>

<!-- end scripts -->
		<s:if test="%{updateUserStatus == 1}">
			<script type="text/javascript"> successMessage('User updated Successfully'); </script>
		</s:if>
		<s:elseif test="%{updateUserStatus == 0}">
			<script type="text/javascript"> errorMessage('User update failed'); </script>
		</s:elseif>
		<s:elseif test="%{updateUserStatus == 2}">
			<script type="text/javascript"> errorMessage('Email Id belongs to another user'); </script>
		</s:elseif>
		<s:elseif test="%{updateUserStatus == 3}">
			<script type="text/javascript"> errorMessage('Wrong data'); </script>
		</s:elseif>
		<s:elseif test="%{deleteUserStatus == 1}">
			<script type="text/javascript"> successMessage('User Deleted Successfully'); </script>
		</s:elseif>
		<s:elseif test="%{deleteUserStatus == 0}">
			<script type="text/javascript"> errorMessage('User delete failed'); </script>
		</s:elseif>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
