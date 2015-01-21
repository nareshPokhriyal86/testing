<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.lin.web.util.LinMobileConstants"  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ONE - Query Test</title>

<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>

<script type="text/javascript">
 function submitForm(){
   	
	 var publisherName= $('#publisherName').val();
	 var schemaType= $('#schemaType :selected').text();
	 var dataSource= $('#dataSource :selected').text();
	 var startDate=$('#startDate').val();
	 var endDate=$('#endDate').val();
	 console.log("publisherName:"+publisherName);
	 $('#dropDownId :selected').text();
	 $('#output').html("Loading....");
	 try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadQueryFromClause.lin",
	      cache: false,
	      data : {
	    	  publisherName :publisherName,
	    	  schemaType :schemaType,
	    	  dataSource : dataSource,
	    	  startDate :startDate,
	    	  endDate :endDate
		   },		    
	      dataType: 'json',
	      success: function (data) {
	    	  console.log(data);
	    	  $('#output').html("Done...."+data);
	     },
	     error: function(jqXHR, exception) {
	    	 alert("exception:"+exception);
	     }	     
	   });   
	}catch(error){
          alert("error:"+error);
	}
 }
</script>
</head>
<body >
 	
	<h4>******************************Query FROM clause generator********************************************************</h4>
	
     <form id="queryForm">
      <select id="publisherName" >
           <option value="-1">Select publisher </option>
           <option value="1">LinMedia</option> 
           <option value="2" >LinDigital</option>
           <option value="4" >Tribune</option>
           <option value="5" >LinMobile</option>
       </select>
       <select id="schemaType" >
           <option value="-1">Select report type</option>
           <option>CorePerformance</option> 
           <option>PerformanceByLocation</option>
           <option>DFPTarget</option>
           <option>CustomEvent</option>
           <option>RichMedia</option>
           <option>Sell_Through</option>
       </select>
       <select id="dataSource">
           <option value="-1">Select data source</option>
           <option>DFP</option> 
           <option>Undertone</option>           
           <option>Mojiva</option>
           <option>Nexage</option>
           <option>GoogleAdExchange</option>           
       </select>
       <br/>
      <div>Start Date(yyyy-MM-dd):<input type="text" id="startDate" /></div>
      <div>End Date(yyyy-MM-dd):<input type="text" id="endDate"  /></div>
      <br/> 
      <div style=" background: none repeat scroll 0 0 #000000;color: #FFFFFF;cursor: pointer;font-size: 18px; height: 30px; padding-left: 0;    padding-top: 7px;    text-align: center;    width: 83px;" 
            onclick="submitForm()" > Submit </div>
     </form>
    <br/>
    <h4>***************************************** OUTPUT **************************************************************</h4>
     <div style="color:red;" id="output"></div>
</body>
</html>