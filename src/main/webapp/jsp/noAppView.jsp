<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:set name="theme" value="'simple'" scope="page" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>ONE - No App View Assigned</title>
</head>
<body>
<h3>
	<s:if test="%{#session.sessionDTO.adminUser}">
		No view is assigned to you. Please contact the Super Administrator
	</s:if>
	<s:else>
		No view is assigned to you. Please contact the Administrator
	</s:else>
</h3>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>