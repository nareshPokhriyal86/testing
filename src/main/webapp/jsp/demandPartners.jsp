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
	<%-- <jsp:include page="filter.jsp"/> --%>
		<s:form name="userSetupForm" action="">
		
		<div class="contained">
			<!-- aside -->
			<jsp:include page="userManagementLeftMenu.jsp"/>
			<!-- aside end -->


			<!-- main content -->
			<div id="page-content" style="margin-right:40px!important; margin-left:40px!important;">
			<div style="text-align: right; float: right;">
				<input class="btn btn-success" onclick="location.href='demandPartnerCreate.lin'" style="margin-top: 15px;" type="button" value="Create New Demand Partner">
			</div>
            <h1 id="page-header">Demand partners</h1>
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
									       <table class="table dtable table-hover table-bordered responsive" id="userSetupTable">
													<thead>
														<tr>
														    <th>Channel/Demand Partners</th>
														    <th>Properties/Site</th>
															<th>Contact Person Name</th>
															<th style="width:150px;"> Contact Person Email</th>
															<th> Contact Person Phone Number</th>
															<th>Website</th>
															<th>Status</th>
															
															<!-- <th style="text-align:center;">Edit</th> -->
															<!-- <th style="text-align:center;">Delete</th> -->
														</tr>
													</thead>
													<tbody>
														<tr class="odd gradeX">
															<td>Mojiva,National Sales Direct,Local Sales Direct,Undertone,Nexage,Google Ad exchange,House</td>
															<td>KHON-TV,KHON-TV,KIMT-TV,KRQE-TV,KSNW-TV,KSNT-TV,KXAN-TV,WALA-TV,WANE-TV,WAVY-TV,WDTN-TV,</td>
															<td>Rajeev Ranjan</td>
															<td>rajeev.ranjan@mediaagility.in</td>
															<td>91+9717040603</td>
															<td>www.mediaagility.com</td>
															<td>Active</td>
														</tr>
														
														<tr class="odd gradeX">
															<td>Mojiva,National Sales Direct,Local Sales Direct,Undertone,Nexage,Google Ad exchange,House</td>
															<td>KHON-TV,KHON-TV,KIMT-TV,KRQE-TV,KSNW-TV,KSNT-TV,KXAN-TV,WALA-TV,WANE-TV,WAVY-TV,WDTN-TV,</td>
															<td>Rajeev Ranjan</td>
															<td>rajeev.ranjan@mediaagility.in</td>
															<td>91+9717040603</td>
															<td>www.mediaagility.com</td>
															<td>Active</td>
														</tr>
														
														<tr class="odd gradeX">
														    <td>Mojiva,National Sales Direct,Local Sales Direct,Undertone,Nexage,Google Ad exchange,House</td>
															<td>KHON-TV,KHON-TV,KIMT-TV,KRQE-TV,KSNW-TV,KSNT-TV,KXAN-TV,WALA-TV,WANE-TV,WAVY-TV,WDTN-TV,</td>
															<td>Rajeev Ranjan</td>
															<td>rajeev.ranjan@mediaagility.in</td>
															<td>91+9717040603</td>
															<td>www.mediaagility.com</td>
															<td>Active</td>
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
			<script type="text/javascript"> toastr.success('User updated Successfully'); </script>
		</s:if>
		<s:elseif test="%{updateUserStatus == 0}">
			<script type="text/javascript"> toastr.error('User update failed'); </script>
		</s:elseif>
		<s:elseif test="%{updateUserStatus == 2}">
			<script type="text/javascript"> toastr.error('Email Id belongs to another user'); </script>
		</s:elseif>
		<s:elseif test="%{updateUserStatus == 3}">
			<script type="text/javascript"> toastr.error('Wrong data'); </script>
		</s:elseif>
		<s:elseif test="%{deleteUserStatus == 1}">
			<script type="text/javascript"> toastr.success('User Deleted Successfully'); </script>
		</s:elseif>
		<s:elseif test="%{deleteUserStatus == 0}">
			<script type="text/javascript"> toastr.error('User delete failed'); </script>
		</s:elseif>
</body>
</html>
