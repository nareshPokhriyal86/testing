<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.lin.web.util.LinMobileConstants"  %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ONE - Bidder Test</title>

<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>

<script type="text/javascript">
 var AWS_BIDDER_SERVER_URL='http://54.91.115.220:8888/'; //getstat/all
 var configJson = {
		  "account": ["hello","world"],
		  "bidProbability": 1.0,
		  "creatives": [ 
		   {
		      "format":{"width":720, "height":90},
		      "id":0,
		      "name":"LeaderBoard",
		      "tagId":0
		    },
		    {
		      "format":{"width":160,"height":600},
		      "id":1,
		      "name":"LeaderBoard",
		      "tagId":1
		    },
		    {
		      "format":{"width":300,"height":250},
		      "id":2,
		      "name":"BigBox",
		      "tagId":2
		    }
		  ],
		  "augmentations":{
		    "frequency-cap-ex":{
		      "required":true,
		      "config":42,
		      "filters":{"include":["pass-frequency-cap-ex"]}    
		    }  
		  },
		  "maxInFlight": 50
 } 
 
 $(window).load(function() {	
	 $('#campaignJson').val(JSON.stringify(configJson));	
 });
 
 
 
 function submitForm(){	 
	 var campaignAction= $('#campaignAction :selected').text();
	 
	 $('#output').html("wait...");
	 //$('#campaignJson').val(configJson);
	 var campaignId=$('#campaignId').val();
	 
	 var request = $.ajax({

         url: AWS_BIDDER_SERVER_URL+campaignAction,
         //async: false,
         crossDomain:true,
         type: "POST",
         data: {
        	 campaignid:campaignId,
             campaignConfigObj: configJson
         },
         contentType: "application/x-www-form-urlencoded", //This is what made the difference.
         dataType: "json"

     });


     request.success(function(result) {
    	 $('#output').val(result);
         console.log(result);

     });

     request.error(function(jqXHR, textStatus) {
    	 console.log(jqXHR);
    	 if(jqXHR.status==200){
    		 $('#output').val("" + jqXHR.responseText);
    	 }else{
    		 $('#output').val("Request failed: " + textStatus);
    	 }
        
     });

     request.done(function(result) {
    	 //$('#output').html(result);
         console.log("done--- "+result);

     });
 }
 
 function showStatus(){
	 var request = $.ajax({

         url: AWS_BIDDER_SERVER_URL+'getstat/all',
         //async: false,
         crossDomain:true,
         type: "GET",
         data: {
         },
         contentType: "application/x-www-form-urlencoded", //This is what made the difference.
         dataType: "json"

     });


     request.success(function(result) {
    	 $('#output').val(result);
         console.log(result);

     });

     request.error(function(jqXHR, textStatus) {
    	 console.log(jqXHR);
    	 if(jqXHR.status==200){
    		 $('#output').val(jqXHR.responseText);
    	 }else{
    		 $('#output').val("Request failed: " + textStatus);
    	 }
        
     });

     request.done(function(result) {
    	 //$('#output').html(result);
         console.log("done--- "+result);

     });
 }
</script>
</head>
<body >
 	
	<h4>******************************Bidder Launcher********************************************************</h4>
	
     <form id="testForm">
	     <fieldset>
	        <legend>Launcher data:</legend>
	        <div>CampaignId :<input type="text" id="campaignId" value="12345" /></div>
	        <div>CampaignJson:<textarea id="campaignJson" readonly="true" rows="10" cols="70"></textarea>  </div>
	        <div>Select an action:
	        <select id="campaignAction">
			  <option value="start">start</option>
			  <option value="stop">stop</option>
			  <option value="reconfig">reconfig</option>
			 <!--  <option value="getstat">getstat</option>	 -->			  
			</select>  
			</div>
	        <br/> 
	       <div> <button type="button" onclick="submitForm()">Submit</button></div>     
	       <div> <button type="button" onclick="showStatus()">Show All Status</button></div>  
	      </fieldset>
     </form>
    <br/>
    <h4>***************************************** OUTPUT ******************************************************</h4>
	      
    <div>
       <textarea id="output" readonly="true" rows="10" cols="100"></textarea> 
    </div>
</body>
</html>