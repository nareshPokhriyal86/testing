<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
 
<jsp:include page="Header.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>

<link rel="stylesheet" href="../css/poolMap/poolmap.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet" type="text/css" href="../css/angular/ng-grid.min.css?v=<s:property value="deploymentVersion"/>" />

<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<title>ONE - News And Research</title>

<script>
var newsTab = '<%=TabsName.NEWS_AND_RESEARCH_NEWS%>';
$(document).ready(function() {
	$('#newsAndResearchLi').attr('class', 'main-nav-li_selected');
	$("#indus_new").click(getNews());
});
</script>
 
<jsp:include page="css.jsp"/>

</head>

<body  ng-app="poolApp" ng-controller="poolController">
 
 <div id="mainDiv" class="height-wrapper">
  <div id="page-content" class="mlr" >
  	<jsp:include page="navigationTab.jsp"/>
  	<div class="row-fluid">
		<div > 
			<ul class="nav nav-tabs upper_tabs" id="myTab1">
				<li class="active"><a href="#s4" id="indus_new" data-toggle="tab"><%=TabsName.NEWS_AND_RESEARCH_NEWS%></a></li>
			</ul>
  			
			<jsp:include page="newsAndResearch.jsp"/>
			</div>
		</div>
   </div>
  
</div>    
</body>
 <jsp:include page="js.jsp"/>
<jsp:include page="googleAnalytics.jsp"/>
</html>
