<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
   response.setHeader( "Pragma", "no-cache" );
   response.setHeader( "Cache-Control", "no-cache" );
   response.setDateHeader( "Expires", 0 );
%>
<title>LinMobile</title>

<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js" type="text/javascript"></script> -->

<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
</head>

<body>
   <% 
      response.sendRedirect("/login.lin");
   %>
   <div><h2>Welcome to Synergy Maps...</h2></div>
   <br/>
   <div ><a style="cursor:pointer;color:blue;" onclick="getServerTime()">Click here for Ajax Demo, getServer Time</a></div>
   <br/>
   <div style="color:red;" id="results"></div>
   
</body>
<script type="text/javascript">

 function getServerTime(){
	
	 $.ajax({
	       type : "POST",
	       url : "/serverTime.lin",
	       cache: false,
	       //data : {userId:userId,timestamp:timestamp},
	       dataType: 'json',
	       success: function (data) {	    	  
	           $.each(data, function(index, element) {	        	   
	        	   var time=data['currentTime'];
	        	   $("#results").html(time);

	           });
	          
	       },
	       error: function(jqXHR, exception) {	          
	        }
		
	  });
	 
 }
 
 </script>
</html>