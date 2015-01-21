<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ONE - Report Uploader</title>
<script type="text/javascript">

</script>
</head>
<body >
 	
 	<div  style="color:red;">
	  <%
	  String status=(String)request.getAttribute("status");
	  if(status !=null){
		  out.println("reportsResponse: "+status);
	  }
	%>
	</div>
	
	
  
 <h4>*******************************Undertone data uploader********************************************************</h4>
	
  <h4>Upload Undertone finalise/non-finalise CSV Report on cloud storage and bigquery</h4>
	
     <form action="undertoneDailyLinMediaReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />    
       <input type="text" id="undertone_startDate" name="startDate" value="yyyy-MM-dd"/>
       <input type="text" id="undertone_endDate" name="endDate" value="yyyy-MM-dd"/>    
       <select name="finalise">
           <option value="-1">Select staus</option>
           <option value="0">NonFinalise</option> 
           <option value="1">Finalise</option>           
       </select> 
       <input type="submit" value="Upload"/>
     </form>
    <br/>
    
   <h4>*******************************SellThrough Finalise-NonFinalise data uploader***************************</h4>
	
	<h4>Upload SellThrough CSV Report on cloud storage and bigquery</h4>
	
     <form action="sellThroughDailyReport.lin" method="post" enctype="multipart/form-data">
       <select name="publisherId">
           <option value="-1">Select publisher</option>
           <option value="1">LinMedia</option> 
          <!--  <option value="2">LinDigital</option> -->
           <option value="4">Tribune</option>
       </select>
       <input type="file" name="linCSV"  /> 
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  
    <h4>******************************* Data Uploader *****************************</h4>	
	<h4>Upload Entity data from csv file to datastore</h4>	
    <form action="entityDataUploader.lin" method="post" enctype="multipart/form-data">
       <select name="entityType" >
           <option value="-1">Select data store entity</option>
           <option>CountryObj</option>
           <option>StateObj</option>
           <option>CityObj</option>    
           <option>CreativeObj</option>
           <option>DeviceObj</option> 
           <option>PlatformObj</option> 
           <option>AdUnitHierarchy</option>
           <option>Industry</option>
           <option>Campaign Type</option>
           <option>Campaign Status</option>
           <option>GeoTargets</option> 
           <option>KPI</option>
           <option>DfpOrderIdsObj</option>
           <option>Education Type</option>
           <option>Ethinicity Type</option>
           <option>UpdateMigratedCampaign</option>
       </select>
        <select name ="adServerNetwork" id="adServerNetwork" >
	           <option value="-1">Select adServer network</option>
	           <option value="5678">Lin Media</option> 
	           <option value="4206">Lin Digital</option>
	           <option value="9331149">Lin Mobile</option>   
	           <option value="45604844">Tribune</option>
	           <option value="5578">Examiner</option> 
	           <!-- Added by Lavakush for topix -->   
	           <option value="1007315">Topix</option>          
	   </select>
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
    </form>
    <br/>
  
    <h4>*******************************Admin Functions********************************************************</h4>
	<h3>Upload Admin module data from csv file to datastore</h3>
    <form action="uploadAdminEntities.lin" method="post" enctype="multipart/form-data">
       <select name="entityType">
           <option value="-1">Select Entity type</option>
           <option>Companies</option>
           <option>Properties</option>
           <option>Accounts</option>
           <option>Roles</option>
           <option>Authorisation Label</option> 
           <option>Users</option>
       </select>
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
    </form>
    
    <h4>******************************* Product for Publisher ********************************************************</h4>
	<h3>Upload Product for Publisher data from csv file to datastore</h3>
    <form action="uploadProductEntities.lin" method="post" enctype="multipart/form-data">
       <select name="entityType">
           <option value="-1">Select Entity type</option>
           <option>Contextual Categories</option>
       </select>
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
    </form>
     
      <h4>*******************************DFP Sites With DMA Data Uploader *****************************</h4>	
	<h4>Upload DFP sites with DMA data from csv file to datastore</h4>	
    <form action="uploadDfpSitesWithDMA.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
    </form>
    
    
    <h4>******************************* Campaign LineItem Data *****************************</h4>
	
     <form action="addLineItemInfo.lin" method="post">
       <input type="text" placeholder="Campaign Id" name="campaignId"  />
       <input type="text" placeholder="Order DFP Id" name="orderId"  />
       <input type="text" placeholder="Placement Id" name="placementId"  />       
       <input type="text" placeholder="LineItem Id" name="lineItemId"  />
       <input type="text" placeholder="LineItem Name" name="lineItemName"  />
       <input type="text" placeholder="Partner Name" name="partnerName"  />
       <input type="submit" value="Submit"/>
     </form>
  <br/>
  
  <h4>******************************* Company Id replacement in Account Entity *****************************</h4>
	
     <form action="replaceAccountCompanyId.lin" method="post">
       <input type="text" placeholder="Previous Company Id" name="prevoiusId"  />
       <input type="text" placeholder="New Company Id" name="newId"  />
       <input type="submit" value="Submit"/>
     </form>
    <br/>
    
    <h4>******************************* UpdateEntities *****************************</h4>
    <form action="updateEntities.lin" method="post">
    	<select name="entityType">
           <option value="-1">Select Entity type</option>
           <option>UserDetailsObj</option>
           <option>SmartCampaignObj</option>
           <option>AccountsEntity</option>
       </select>
       <input type="submit" value="Update"/>
      </form>
    
    <h4>***************************** Add Census Group **********************</h4>
    <form action="updateCensus.lin" method="post">
     	<input type="text" placeholder="Census Group Text" name="grouptxt"  />
    	<select name="group">
    		<option value="">Select Group</option>
    		<option value="Age">Age</option>
    		<option value="Education">Education</option>
    		<option value="Ethnicity">Ethnicity</option>
    		<option value="Income">Income</option>
    	</select>
    	
    	<select name="gender">
    		<option value="">Select Gender available</option>
    		<option value="1">True</option>
    		<option value="0">False</option>
    	</select>
    	
    	<input type="text" placeholder="BQ main column" name="bqColumn"  />
    	<input type="text" placeholder="BQ male column" name="bqMaleColumn"  />
    	<input type="text" placeholder="BQ female column" name="bqFemaleColumn"  />
    	<input type="text" placeholder="BQ parent column" name="bqParentColumn"  />
      
       <input type="submit" value="Submit"/>
    </form>
  <%--   <h4>*******************************Data Corrector********************************************************</h4>
	<h3>Upload raw/processed csv reports on cloud storage for Data Collection Framework</h3>
     <form action="dataCorrector.lin" method="post" enctype="multipart/form-data">
       <select name="reportType" onchange="showDataSource()">
           <option value="-1">Select report type</option>
           <option>CorePerformance</option> 
           <option>PerformanceByLocation</option>
          <!--  <option>SellThrough</option> -->
       </select>
       <select name="dataSource">
           <option value="-1">Select data source</option>
           <option>DFP</option> 
           <option>Undertone</option>           
           <option>Mojiva</option>
           <option>Nexage</option>
           <option>Google AdExchange</option>
           <option>LSN</option>
           <option>Celtra</option>
           <option>XAd</option>
           <option>Tribune</option>
       </select>
       <select name="level">
           <option value="-1">Select data level</option>
           <option>Process</option> 
           <option>Upload</option>           
       </select>
      <div>Start Date(yyyy-MM-dd):<input type="text" name="startDate" /></div>
      <div>End Date(yyyy-MM-dd):<input type="text" name="endDate"  /></div>
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
    <br/>
    
     <h4>*******************************LSN data uploader********************************************************</h4>
	
	<h4>Upload LSN CSV Report directly on cloud storage and bigquery</h4>
	
     <form action="lsnReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  <h4>*******************************Celtra - [CorePerformance] - Rich Media trafficking data uploader*******************************</h4>
	
	<h4>Upload trafficking Report directly on cloud storage and bigquery</h4>
	
     <form action="celtraReportCorePerformanceRM.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  <h4>*******************************Celtra Rich Media trafficking data uploader*******************************</h4>
	
	<h4>Upload trafficking Report directly on cloud storage and bigquery</h4>
	
     <form action="richMediaCeltraTraffickingReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  <h4>*******************************Celtra Rich Media custom event data uploader*****************************</h4>
	
	<h4>Upload custom event Report directly on cloud storage and bigquery</h4>
	
     <form action="richMediaCeltraCustomEventReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  
  <h4>******************************* XAd Rich Media trafficking data uploader*******************************</h4>
	
	<h4>Upload trafficking Report directly on cloud storage and bigquery</h4>
	
     <form action="richMediaXAdTraffickingReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
  <h4>******************************* XAd Rich Media custom event data uploader*****************************</h4>
	
	<h4>Upload custom event Report directly on cloud storage and bigquery</h4>
	
     <form action="richMediaXAdCustomEventReport.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/>
  
   
   
    <h4>******************************* LinOne Rich Media custom event data uploader*****************************</h4>
	
	<h4>Upload custom event Report directly on cloud storage and bigquery</h4>
	
     <form action="uploadLinOneDataFromDFPDashboard.lin" method="post" enctype="multipart/form-data">
       <input type="file" name="linCSV"  />       
       <input type="submit" value="Upload"/>
     </form>
  <br/> --%>
  
</body>
</html>