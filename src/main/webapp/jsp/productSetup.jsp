<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
<%@page import="com.lin.web.dto.UserDetailsDTO" %>
 
<jsp:include page="Header.jsp" /> 

<!DOCTYPE html>
<html lang="en">
<head>

<script>			
$(document).ready(function() {
	$('#productLi').attr('class', 'active');
	
});
localStorage.clear();
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<title>ONE - Products</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
	
<!-- Below are css file from js.jsp except bootstrap.min.css and theme.css  -->
<link rel="stylesheet" type="text/css" href="../css/ng-grid.min.css?v=<s:property value='deploymentVersion'/>" />
<link rel="stylesheet" href="../css/datepicker.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/fullcalendar.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/TableTools.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/bootstrap-wysihtml5.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/wysiwyg-color.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/toastr.custom.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/toastr-responsive.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/jquery.jgrowl.css?v=<s:property value='deploymentVersion'/>">
<%-- <link rel="stylesheet" href="../css/bootstrap.min.css?v=<s:property value='deploymentVersion'/>"> --%>
<link rel="stylesheet" href="../css/bootstrap-responsive.min.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/font-awesome.min.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/cus-icons.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/jarvis-widgets.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/DT_bootstrap.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/responsive-tables.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/uniform.default.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/select2.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/theme-responsive.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" id="switch-theme-js" href="../css/themes/default.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" id="switch-width" href="../css/full-width.css?v=<s:property value='deploymentVersion'/>">
<link rel='stylesheet' href='http://fonts.googleapis.com/css?family=Lato:300,400,700' type='text/css'>
<link rel="shortcut icon" href="../img/favicons/favicon-lin.ico">
<link rel="shortcut icon" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="../img/favicons/favicon-lin.ico">
<link rel="apple-touch-icon-precomposed" href="../img/favicons/favicon-lin.ico">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-title" content="Jarvis">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
<link rel="apple-touch-startup-image" href="../img/favicons/favicon-lin.ico" media="screen and (max-device-width: 320px)">
<link href="../css/style.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet" type="text/css" />
<link href="../css/animate.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet">
<link href="../css/stylepopover.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet"> 
<link rel="stylesheet" type="text/css" media="all" href="../css/daterangepicker.css?v=<s:property value='deploymentVersion'/>" />
<link href="../css/global.css?v=<s:property value='deploymentVersion'/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/mediaPlan.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/navigationTabs.css?v=<s:property value='deploymentVersion'/>">
<!-- Above are files from js.jsp except bootstrap.min.css and theme.css -->

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="../css/theme.css?v=<s:property value='deploymentVersion'/>">
<link rel="stylesheet" href="../css/custom.css?v=<s:property value='deploymentVersion'/>">

<body >
 <!-- .height-wrapper -->
 <div class="height-wrapper">
  <div id="main" role="main" class="container-fluid" >
   
				<!-- main content -->
  <div id="page-content" class="mlr">
	<jsp:include page="navigationTab.jsp"/>
	<jsp:include page="userManagementLeftMenu.jsp"/>
	<h1 style="margin: 1% 0 0 2%;" id="page-header">Products</h1>

 <!-- tabs view -->
<div class="row-fluid">

 <article class="span12"> <!-- new widget -->
 <div class="jarviswidget">
  <!-- widget div starts -->

	<div id="productSetupDiv" ng-app="productSetupApp" class="container-fluid well" ng-controller="productSetupController" ng-cloak>
		<div class="row-fluid">
			<div class="col-md-8 col-xs-8 col-sm-8 col-lg-8">
				<div class="input-group">
	  				<span class="input-group-addon glyphicon glyphicon-search"></span>
	  				<input style="margin-top: 3px;" type="text" class="form-control" placeholder="Search Product" ng-model="searchProduct">
				</div>
			</div>
			<div class="col-md-4 col-xs-4 col-sm-4 col-lg-4">
				<div class="btn-group" style="float:right;">
				  <input type="button" class="btn btn-default" onclick="location.href='createProduct.lin'" value="New Product" 
				  		style="color: #FFF; background-color: #729C14;">
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">&nbsp;</div>
		</div>	
		<div class="row-fluid">
			<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12">
				<div ng-view="">
					<!-- productList.html -->
				</div>
			</div>
		</div>

		<s:form name = "productSetupForm" id="select-demo-js" cssClass="form-horizontal themed" action="createProduct.lin" >
			<input type="hidden" id="productId" name="productId" value="" >
			<input type="hidden" id="partnerId" name="partnerId" value="" >
		</s:form>
	</div>

<!-- end widget div -->
</div>
<!-- end widget --> 
</article>

 </div>
 <!-- end main content -->
 </div>
 </div>
  <!--end fluid-container-->
		
</div>
	
	
 <jsp:include page="js.jsp"/>
 
 <!-- 3rd Party Libraries Angular , Angular Routes , Jquery , Bootstrap --> 
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
<script type="text/javascript" src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>

<!-- Angular Module  -->
<%--  <script type="text/javascript" src="../js/angular/controller/productModule.js?v=<s:property value="deploymentVersion"/>"></script> --%>
 <script type="text/javascript" src="../js/angular/controller/productModule.js?v=<s:property value="deploymentVersion"/>"></script>
 
  <script> 
 $(document).ready(function() {
	 $('#adminPageLi').attr('class', 'navigationForNewBootstrapLastChild');
		var productSetupScope=angular.element(document.getElementById("productSetupDiv")).scope();
		productSetupScope.loadProductSetupData();
 });
 
 $(window).scroll(function(){
	 if(($(window).scrollTop() + $(window).height()) >= ($(document).height()-10)) {
		 var scope=angular.element(document.getElementById("productSetupDiv")).scope();
		 scope.loadProductSetupData();
	 }
 });

</script>
</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>

