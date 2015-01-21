<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
 
<jsp:include page="Header.jsp" /> 

<!DOCTYPE html>
<html lang="en" data-ng-app="mapexplorerApp">
<head>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
	<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>

 <script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?libraries=visualization,places&sensor=false"></script>
		
<script type="text/javascript" src="../js/unifiedCampaign.js?v=<s:property value="deploymentVersion"/>"></script>
<script type="text/javascript">
var flightIncrement = parseInt('<s:property value="unifiedCampaignDTO.flightList.size()"/>') - 1;
var allOptionId = <s:property value="allOptionId"/>;
var allOption = '<s:property value="allOption"/>';
var noneOptionId = <s:property value="noneOptionId"/>;
var noneOption = '<s:property value="noneOption"/>';
var campaignStatus = '<s:property value="campaignStatus"/>';

var campaignStartDateOnLoad = '<s:property value="unifiedCampaignDTO.startDate"/>';
var campaignEndDateOnLoad = '<s:property value="unifiedCampaignDTO.endDate"/>';

var flightCount = parseInt('<s:property value="unifiedCampaignDTO.flightList.size()"/>');
		$(document).ready(function() {
			$('#cmapaignPlanLi').attr('class', 'main-nav-li_selected');
			
			if($("#isDemographic").val() == "true") {
				$('#demobuttonId').click();
			}
			if($("#isGeographic").val() == "true") {
				$('#geobuttonId').click();
			}
			
			var cityStr = '<s:property escape="false" value="unifiedCampaignDTO.cityJSON"/>';
			if(cityStr!=null && cityStr!=undefined && cityStr!="" ){
				var cityJson = $.parseJSON(cityStr);
				cityJsonObj = cityJson.cityList;
				var jsonObj = [];
				if(cityJsonObj != undefined) {
					for(i=0; i<cityJsonObj.length; i++) {
						cityIds.push(cityJsonObj[i].id);
						var item = {};
						item ["id"] = cityJsonObj[i].id;
				        item ["name"] = cityJsonObj[i].name;
				        jsonObj.push(item);
					}
				}
		        $('#citySearchPlacement').select2("data",jsonObj); 
		       // cityJsonObj = jsonObj;
			}
			
	        
	    	 var zipStr = '<s:property escape="false" value="unifiedCampaignDTO.zipJSON"/>';
	    	 if(zipStr!=null && zipStr!=undefined && zipStr!="" ){
			var zipJson = $.parseJSON(zipStr);
			zipJsonObj = zipJson.zipList;
			var jsonObjZip = [];
			if(zipJsonObj != undefined) {
				for(i=0; i<zipJsonObj.length; i++) {
					zipIds.push(zipJsonObj[i].id);
					var item = {};
					item ["id"] = zipJsonObj[i].id;
			        item ["name"] = zipJsonObj[i].name;
			        jsonObjZip.push(item);
				}
			}
	        $('#zipSearchPlacement').select2("data",jsonObjZip); 
	     }
	    	 
		
		
		var selectedCensusAge = '<s:property value="unifiedCampaignDTO.selectedCensusAge"/>';
		var selectedCensusGender = '<s:property value="unifiedCampaignDTO.selectedCensusGender"/>';
		var selectedCensusIncome = '<s:property value="unifiedCampaignDTO.selectedCensusIncome"/>';
		var selectedCensusEducation = '<s:property value="unifiedCampaignDTO.selectedCensusEducation"/>';
		var selectedCensusEthnicity = '<s:property value="unifiedCampaignDTO.selectedCensusEthnicity"/>';
		
		
		$("#selectcensusGender option[value='"+selectedCensusGender+"']").prop('selected', true);
		applyRule('main');
		$("#selectcensusAge option[value='"+selectedCensusAge+"']").prop('selected', true);
		applyRule('main');
		$("#selectcensusIncome option[value='"+selectedCensusIncome+"']").prop('selected', true);
		applyRule('main');
		$("#selectcensusEducation option[value='"+selectedCensusEducation+"']").prop('selected', true);
		applyRule('main');
		$("#selectcensusEthnicity option[value='"+selectedCensusEthnicity+"']").prop('selected', true);
		applyRule('main');
		
		
		dropDownChanged("deviceSelect");
		dropDownChanged("platformSelect");
		dropDownChanged("creativeId");
		
		});
		
		
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
   
   <title>ONE - Campaign Performance</title>
   <jsp:include page="css.jsp"/>
	<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
	<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
	
	<link rel="stylesheet" href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
	<link rel="stylesheet" href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
	<link rel="stylesheet" href="../css/datepicker_new.css"/>
	
	<link rel="stylesheet" href="../css/smpDemo.css?v=<s:property value="deploymentVersion"/>">		
</head>
	<body>
		<div class="height-wrapper">
  		<div id="main" role="main" class="container-fluid"  >
  			<div class="contained">
			<!-- main content -->
  		 <div id="page-content" class="mlr">
   			 <jsp:include page="navigationTab.jsp"/>
   			  <div >
   			 <h1 id="page-header" style="margin: 2% 1% 1% 1%;">Update Campaign</h1>
   			 </div>
   			 
   			 
   				 <div class="fluid-container" id ="removeBorderForm" style="background-color: white;" >
   					 <section id="widget-grid" class="">
   					  <s:form name = "campaignPlacementEditForm" id="select-demo-js" cssClass="  themed" onsubmit="return getJsonData();" action="saveCampaign.lin" >
   					  	 <div id="addCampaignId">
   					 	   <div class="row-fluid" >
								  <article class="span6">
								      	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Campaign Name<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="campaignNameError"></span>
											</label>
												<div class="controls">
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
													<input class="span12" required="required"  maxlength="200" type="text" placeholder="Campaign Name" onblur="campaignNameChanged();"
													id="campaignName" name="unifiedCampaignDTO.campaignName" value="<s:property value="unifiedCampaignDTO.campaignName"/>">
												</s:if>
													<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
    													<input class="span12 " required="required"  maxlength="200" type="text" placeholder="Campaign Name" onblur="campaignNameChanged();" 
													id="campaignName" name="unifiedCampaignDTO.campaignName" disabled value="<s:property value="unifiedCampaignDTO.campaignName"/>">
													</s:elseif>
													                 
												</div>
											</div>
									</article>
									<article class="span6">
											 <div class="control-group" style="border-bottom: none;">
                              					<label class="control-label">Rate Type 
                              						<span  class="help-Helpwidgets-TooltipWidget"></span>
							  					</label>
                                    				<div class="controls">
                                    				<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
                                    					<s:select Class="span12" id="rateType" name="unifiedCampaignDTO.selectedRateType"
            												list="unifiedCampaignDTO.rateTypeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedRateTypeList.{id}}">
            											</s:select>
                                    				</s:if>
                                    				 <s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
                                    				<s:select Class="span12" id="rateType" readonly="true"  name="unifiedCampaignDTO.selectedRateType"
            												list="unifiedCampaignDTO.rateTypeList"    listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedRateTypeList.{id}}">
            											</s:select>
                                    				</s:elseif> 
                                   				 		
                                    					<br>
                                   					</div>
                             				</div>	
                             		</article>
								</div>
								<div class="row-fluid" >
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label">Duration<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="campaignDateError"></span>
									    	</label>
										     	<div class="controls">
													
													<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
													<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
													<input class="datepicker-input" size="16" type="text" id="cStartDate" placeholder="Select a date" 
																required="required" title="Start date is required" name="unifiedCampaignDTO.startDate" value="<s:property value="unifiedCampaignDTO.startDate"/>"/>
															<span class="add-on"><i class="cus-calendar-2"></i></span>
													</div>
													</s:if>
													<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
													<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy" >
													<input  class="datepicker-input" size="16" type="text" id="cStartDate" readonly="true" placeholder="Select a date" 
																required="required" title="Start date is required" name="unifiedCampaignDTO.startDate" value="<s:property value="unifiedCampaignDTO.startDate"/>"/>
															<span class="add-on"><i  class="cus-calendar-2"></i></span>  
													</div>
													</s:elseif>
													to
													
													<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
													<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
													<input class="datepicker-input" size="16" type="text"  id="cEndDate" placeholder="Select a date" 
															required="required" title="End date is required"	name="unifiedCampaignDTO.endDate" value="<s:property value="unifiedCampaignDTO.endDate"/>"/>
															<span class="add-on"><i class="cus-calendar-2"></i></span>
													</div>
													</s:if>
													<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
														<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
														<input class="datepicker-input" size="16" type="text"  id="cEndDate" readonly="true" placeholder="Select a date" 
															required="required" title="End date is required"	name="unifieCampaignDTO.endDate" value="<s:property value="unifiedCampaignDTO.endDate"/>"/>
															 <span class="add-on"><i class="cus-calendar-2"></i></span> 
													</div>
													</s:elseif>
												</div>
											</div>
									</article>
									<article class="span6">
   					 				   <div class="control-group" style="border-bottom: none;">
   					 	 				  <label class="control-label" for="textarea">Notes</label>
											<div class="controls">
											<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
												<textarea class="span12" id="address" rows="2" name="unifiedCampaignDTO.notes" placeholder="Notes" 
													><s:property value="unifiedCampaignDTO.notes"/></textarea>
											</s:if>
											<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
											<textarea class="span12" id="address" rows="2" name="unifiedCampaignDTO.notes" disabled placeholder="Notes" 
													><s:property value="unifiedCampaignDTO.notes"/></textarea>
											</s:elseif>
											</div>
   					 					</div>
   					 			   </article>
								</div>
							    <div class="row-fluid" >
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Advertiser <span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="advertiserError" tabindex="-1"></span></label>
												<div class="controls">
												<s:if test="unifiedCampaignDTO.selectedAdvertiserList!=null && unifiedCampaignDTO.selectedAdvertiserList.size()>0">
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
												<s:select Class="span12 with-search"	 id="advertiserListId" required="required" name="unifiedCampaignDTO.selectedAdvertiser"
				           						 		 onchange="addNewAdvertiserPopup()"  list="unifiedCampaignDTO.advertiserList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedAdvertiserList.{id}}">
				           							</s:select>
												</s:if>
												<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
													<s:select Class="span12 with-search"	 id="advertiserListId" required="required" name="unifiedCampaignDTO.selectedAdvertiser"
				           						 		  onchange="addNewAdvertiserPopup()" list="unifiedCampaignDTO.advertiserList" disabled="true"  listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedAdvertiserList.{id}}">
				           							</s:select>
				           						</s:elseif>
												</s:if>
												<s:else>
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
													<s:select Class="span12 with-search"	id="advertiserListId" required="required" name="unifiedCampaignDTO.selectedAdvertiser"
				           						 		  onchange="addNewAdvertiserPopup()"  list="unifiedCampaignDTO.advertiserList" listKey="id" listValue="value" value="">
				           							</s:select>
												</s:if>
												<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2'>
												<s:select Class="span12 with-search" id="advertiserListId" disabled="true"  required="required" name="unifiedCampaignDTO.selectedAdvertiser"
				           						 		  onchange="addNewAdvertiserPopup()" list="unifiedCampaignDTO.advertiserList" listKey="id"  listValue="value" value="">
				           							</s:select>
												</s:elseif>
												</s:else>
												</div>
										</div>
									</article>
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Agency</label>
												<div class="controls">
												<s:if test="unifiedCampaignDTO.selectedAdvertiserList!=null && unifiedCampaignDTO.selectedAdvertiserList.size()>0">
														<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
													<s:select Class="span12 with-search"	 id="agencyListId" required="required" name="unifiedCampaignDTO.selectedAgency"
				           						 		  onchange="addNewAgencyPopup()" list="unifiedCampaignDTO.agencyList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedAgencyList.{id}}">
				           							</s:select>
				           							</s:if>
				           							<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
				           							<s:select Class="span12 with-search"	 id="agencyListId" required="required" name="unifiedCampaignDTO.selectedAgency"
				           						 		  onchange="addNewAgencyPopup()" list="unifiedCampaignDTO.agencyList" listKey="id" disabled="true" listValue="value" value="%{unifiedCampaignDTO.selectedAgencyList.{id}}">
				           							</s:select>
				           							</s:elseif>
												</s:if>
												<s:else>
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==3'>
													<s:select Class="span12 with-search"	id="agencyListId" required="required" name="unifiedCampaignDTO.selectedAgency"
				           						 		 onchange="addNewAgencyPopup()"  list="unifiedCampaignDTO.agencyList" listKey="id" listValue="value" value="">
				           							</s:select>
				           							</s:if><s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 '>
				           							<s:select Class="span12 with-search"	id="agencyListId" required="required" name="unifiedCampaignDTO.selectedAgency"
				           						 		 onchange="addNewAgencyPopup()"  list="unifiedCampaignDTO.agencyList" listKey="id"  disabled="true" listValue="value" value="">
				           							</s:select>
				           							</s:elseif>
												</s:else>
									
											</div>
										</div>
									</article>
								   </div>
								</div>
						<div id="editplacementId" >
							<div class="row-fluid"  >
							<h1 id="page-header" style="margin: 3% 1% 1% 1%;padding: 1%; background-color: #F5F5F5;">Edit Placement</h1>
							</div>
								<input id="selectedProduct" type="hidden" name="unifiedCampaignDTO.selectedPlacementProducts" value="<s:property value="unifiedCampaignDTO.selectedPlacementProducts"/>">
								<div class="row-fluid" >
						  			<article class="span6">
   					 	    			<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Placement Name<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="placementNameIdError"></span>
											</label>
										<div class="controls">
										<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
											<input class="span12" required="required"  maxlength="200" type="text" placeholder="Placement Name" onblur="placementNameChanged();"
										                    id="placementNameId" name="unifiedCampaignDTO.pName" value="<s:property value="unifiedCampaignDTO.pName"/>">
										</s:if>  
										<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
										<input class="span12" required="required"  maxlength="200" type="text" placeholder="Placement Name" disabled  onblur="placementNameChanged();"
										                    id="placementNameId" name="unifiedCampaignDTO.pName" value="<s:property value="unifiedCampaignDTO.pName"/>">
										</s:elseif>                  
										</div>
										</div>
   					 	   			</article>
   					 				<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label">Duration<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="placementDateError"></span></label>
										    	<div class="controls">
													<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
													<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
														<input class="datepicker-input" size="16" type="text" id="pStartDate" placeholder="Select a date" 
															required="required" title="Start date is required" type="text" name="unifiedCampaignDTO.pStartDate" value="<s:property value="unifiedCampaignDTO.pStartDate"/>"/>
																<span class="add-on"><i class="cus-calendar-2"></i></span>
													</s:if>
													<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
													<input class="datepicker-input" size="16" type="text" disabled id="pStartDate" placeholder="Select a date" 
															required="required" title="Start date is required" type="text" name="unifiedCampaignDTO.pStartDate" value="<s:property value="unifiedCampaignDTO.pStartDate"/>"/>
																<span class="add-on"><i class="cus-calendar-2"></i></span>
													</s:elseif>
													</div>
													to
												<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
														<input class="datepicker-input" size="16" type="text"  id="pEndDate" placeholder="Select a date" 
															required="required" title="End date is required" type="text" name="unifiedCampaignDTO.pEndDate" value="<s:property value="unifiedCampaignDTO.pEndDate"/>"/>
																<span class="add-on"><i class="cus-calendar-2"></i></span>
												</s:if>
												<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
												<input class="datepicker-input" size="16" type="text" disabled id="pEndDate" placeholder="Select a date" 
															required="required" title="End date is required" type="text" name="unifiedCampaignDTO.pEndDate" value="<s:property value="unifiedCampaignDTO.pEndDate"/>"/>
																<span class="add-on"><i class="cus-calendar-2"></i></span>
												</s:elseif>
												</div>
											</div>
										</div>
									</article>
							   </div>
							   <div class="row-fluid" >
							   		<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Goal<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="totalGoalError"></span>
											</label>
												<div class="controls">
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Impression" 
									                    id="totalGoal" name="unifiedCampaignDTO.pImpression" value="<s:property value="unifiedCampaignDTO.pImpression"/>">
									            </s:if>
									            <s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
									            <input class="span12" required="required" disabled maxlength="49" type="text" placeholder="Impression" 
									                    id="totalGoal" name="unifiedCampaignDTO.pImpression" value="<s:property value="unifiedCampaignDTO.pImpression"/>">
									            </s:elseif>
												</div>
												
										</div>
									</article>
							  	 	<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">$ Rate<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="rateError"></span>
											</label>
												<div class="controls">
												<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
													<input class="span12" required="required" title="Rate is required" placeholder="$ Rate"
														type="text" id="rate" name="unifiedCampaignDTO.rate" value="<s:property value="unifiedCampaignDTO.rate"/>">
												</s:if>	
												<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>	
												<input class="span12" required="required" disabled title="Rate is required" placeholder="$ Rate"
														type="text" id="rate" name="unifiedCampaignDTO.rate" value="<s:property value="unifiedCampaignDTO.rate"/>">	
												</s:elseif>			
												</div>
										</div>
									</article>
							   </div>
							   <div class="row-fluid">
							   		<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">$ Budget<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="budgetError"></span>
											</label>
										<div class="controls">
										<s:if test='unifiedCampaignDTO.statusId==4 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
											<input class="span12" required="required" title="Budget is required" placeholder="$ Budget"
												type="text" id="budget" name="unifiedCampaignDTO.pBudget" value="<s:property value="unifiedCampaignDTO.pBudget"/>">
										</s:if>
										<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6'>
										<input class="span12" required="required" disabled title="Budget is required" placeholder="$ Budget"
												type="text" id="budget" name="unifiedCampaignDTO.pBudget" value="<s:property value="unifiedCampaignDTO.pBudget"/>">
										</s:elseif>
										</div>
										</div>
									</article>
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
										   	<label class="control-label" for="multiSelect">Creative Size<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="creativeError" tabindex="-1"></span></label>
												<div class="controls">
												<s:if test='unifiedCampaignDTO.statusId==4'>
													<s:select cssClass="span12 with-search"	multiple="true" id="creativeId" onchange="creativeChanged();setUserPref('creativeId');"
				           						 		name="unifiedCampaignDTO.pSelectedCreative" list="unifiedCampaignDTO.creativeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCreativePlacementList.{id}}" >
				           							</s:select>
				           						</s:if>		
				           						<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
				           							<s:select cssClass="span12 with-search"	multiple="true" id="creativeId" onchange="creativeChanged();setUserPref('creativeId');"
				           						 		name="unifiedCampaignDTO.pSelectedCreative" disable="true" list="unifiedCampaignDTO.creativeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCreativePlacementList.{id}}" >
				           							</s:select>
				           						</s:elseif>
												</div>
										</div>
									</article>
							  </div>
							  <div class="row-fluid">
								  <article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Goal(CTR%)&nbsp;&nbsp;<span type="hidden" style="color: red;" id="pGoalError"></span>	</label>
										<div class="controls">
										<s:if test='unifiedCampaignDTO.statusId==4'>
											<input class="span12" title="Goal is required" placeholder="Goal(CTR%)"
												type="text" id="pGoal" name="unifiedCampaignDTO.pGoal" value="<s:property value="unifiedCampaignDTO.pGoal"/>">
										</s:if>
										<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
										<input class="span12" title="Goal is required" placeholder="Goal(CTR%)"
												type="text" id="pGoal"   name="unifiedCampaignDTO.pGoal" value="<s:property value="unifiedCampaignDTO.pGoal"/>">
										</s:elseif>
										</div>
										</div>
									</article>
							  		
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Platform</label>
												<div class="controls">
												<s:if test='unifiedCampaignDTO.statusId==4'>
													<s:select Class="span12 with-search"	multiple="true" id="platformSelect" onchange="dropDownChanged('platformSelect');setUserPref('platformSelect');"
				           						 		name="unifiedCampaignDTO.pSelectedPlatform" list="unifiedCampaignDTO.plateformList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedPlatformPlacementList.{id}}">
				           							</s:select>
				           						</s:if>	
				           						<s:elseif test='unifiedCampaignDTO.statusId==5 || unifiedCampaignDTO.statusId==6 || unifiedCampaignDTO.statusId==2 || unifiedCampaignDTO.statusId==3'>
				           							<s:select Class="span12 with-search"	multiple="true" id="platformSelect" onchange="dropDownChanged('platformSelect');setUserPref('platformSelect');"
				           						 		name="unifiedCampaignDTO.pSelectedPlatform"   list="unifiedCampaignDTO.plateformList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedPlatformPlacementList.{id}}">
				           							</s:select>
				           						</s:elseif>
												</div>
										</div>
									</article>
							  </div>
							  	  <div class="row-fluid">
							  		<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Device</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="deviceSelect" onchange="dropDownChanged('deviceSelect');setUserPref('deviceSelect');"
				           						 		name="unifiedCampaignDTO.pSelectedDevice" list="unifiedCampaignDTO.deviceList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedDevicePlacementList.{id}}">
				           							</s:select>
												</div>
										</div>
									</article>
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Frequency Cap&nbsp;&nbsp;<span type="hidden" style="color: red;" id="frequencyCapError"></span>
											</label>
												<div class="controls">
													<input class="span6"  maxlength="49" type="text" placeholder="Frequency Cap" 
											               id="frequencyCap" name="unifiedCampaignDTO.frequencyCap" value="<s:property value="unifiedCampaignDTO.frequencyCap"/>">
												
													per
											<s:select 	 id="selectAppViews"
				           						 		name="unifiedCampaignDTO.frequencyCapUnit" list="unifiedCampaignDTO.frequencyCapList" listKey="id" 
				           						 		listValue="value" value="%{unifiedCampaignDTO.selectedFrequencyUnitList.{id}}">
				           							</s:select>
											</div>
										</div>
									</article>
							  </div>
							  <div class="row-fluid">
							  		<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Context Category Type</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="contextSelect" onchange="dropDownChanged('contextSelect');"
				           						 		name="unifiedCampaignDTO.pSelectedContext" list="unifiedCampaignDTO.contextList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedContextPlacementList.{id}}">
				           							</s:select>
												</div>
										</div>
									</article>
										 <article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Daily Pacing&nbsp;&nbsp;<span type="hidden" style="color: red;" id="pacingError"></span></label>
										<div class="controls">
											<input class="span12" placeholder="Daily Pacing"
												type="text" id="pacing" name="unifiedCampaignDTO.pacing" value="<s:property value="unifiedCampaignDTO.pacing"/>">
										</div>
										</div>
									</article>
								
							  </div>
							<div class="row-fluid">
							  		<article class="span6">
										<div class="control-group" style="border-bottom: none;">
									    	<label class="control-label" for="multiSelect">Item Type</label>
												<div class="controls">
													<s:select  cssClass="span12 with-search"	
													multiple="false" 
													id="forecastItemTypeSelect" 
				           						 	name="unifiedCampaignDTO.itemType" 
				           						 	value="%{unifiedCampaignDTO.itemType}"
				           						 	list="#{'1':'Standard', '2':'Sponsorship'}"
				           						 		 
				           						 		>
				           							</s:select>
												</div>
										</div>
									</article>
										 <article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Device Capabilities&nbsp;&nbsp;<span type="hidden" style="color: red;" id="pacingError"></span></label>
										<div class="controls">
										<s:select 
				           								cssClass="span12 with-search"	
													multiple="false" 
													id="deviceCapabilitySelect" 
												 
				           						 	name="unifiedCampaignDTO.deviceCapability" 
				           						 	value="%{unifiedCampaignDTO.deviceCapability}"
				           						 	list="#{'0':'Any', '3':'Mobile App & Web', '1':'Mobile App Only', '2':'Mobile Web Only'}"
				           						 		>
				           							</s:select>
										</div>
										</div>
									</article>
								
							  </div>
	<!-- 	<div class="panel-group" id="accordion">
    First Panel
    <div class="panel panel-default">
        <div class="panel-heading">
     	   <h1 id="page-header" data-toggle="collapse"  data-target="#demographicId" style="margin-left: 1%;">Demographic</h1>
             <h1 class="page-header" data-toggle="collapse"  data-target="#demographicId">
                 Collapsible Group Item #1
             </h1>
        </div>
        <div id="collapseOne" class="panel-collapse collapse">
            <div class="panel-body">
                Anim pariatur cliche reprehenderit, 
                enim eiusmod high life accusamus terry richardson ad squid.
            </div>
        </div>
    </div>
    
    </div> -->
						<div id="demoHeaderId" style="background-color: #F5F5F5;">
								<div class="row-fluid" style="background: #f5f5f5;padding: 1%;margin: 3% 1% 1% 1%;width: 96%;" >
										<h1 id="page-header" style="margin:0; width:auto;float: left;padding: 0px;border: 0px;">Demographic</h1>
										<button type="button" id="demobuttonId"  class="btn" 
										data-toggle="collapse" data-target="#demographicEditId" style="float:right;background: none;border: 0px;box-shadow: none;">
										<img src="img/btn_plus.png"/></button>
								</div>
							</div> 
							<div id="demographicEditId" class="panel-collapse collapse">
								
								<div class="row-fluid" >
									
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Gender</label>
											<div class="controls">
											<select class="span12" name="unifiedCampaignDTO.selectedCensusGender" id="selectcensusGender" class="selectcensusGender" onChange="applyRule('main')">
												<option value="all">All</option>
												<option value="male">Male</option>
												<option value="female">Female</option>
											</select>
											</div>
										</div>
										</article>
									
									<s:set var="counter" value="1"/>
									
									<s:iterator value="censusMap" var="census">
									
									
										<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label"><s:property value="#census.key"/></label>
											<div class="controls">
											<select class="span12" name="unifiedCampaignDTO.selectedCensus<s:property value="#census.key"/>" id="selectcensus<s:property value="#census.key"/>" class="selectcensus<s:property value="#census.key"/>" onChange="applyRule('main')" >
												<option gender="true" value="">Select <s:property value="#census.key"/></option>
												<s:iterator value="#census.value" var="censusVal">
													<option 
													gender="<s:property value="#censusVal.gender"/>"
													bqColumn="<s:property value="#censusVal.bqColumn"/>"
													bqFemaleCol="<s:property value="#censusVal.bqFemaleCol"/>"
													bqMaleCol="<s:property value="#censusVal.bqMaleCol"/>"
													bqParentCol="<s:property value="#censusVal.bqParentCol"/>"
													value="<s:property value="#censusVal.groupTxt"/>"
													><s:property value="#censusVal.groupTxt"/></option>
												</s:iterator>
											</select>
											</div>
										</div>
										</article>
									
									
										<s:if test="#counter % 2 != 0">
        									</div><div class="row-fluid" >
    									</s:if>
									
										<s:set var="counter" value="%{#counter+1}"/>
									
									
									</s:iterator>
									
									<article class="span6">
									<label class="control-label">&nbsp;</label>
									<div class="controls"  style="margin-top: 26px;margin-left: 21px;">
										<div class="btn btn-primary" onClick="showDemo()">Show Demo</div>
									</div>
									</article>
								</div>
								
								
								
						</div>
						<br>
							
							<div id="geoHeaderId" >
								<div class="row-fluid" style="background: #f5f5f5;padding: 1%;margin: 3% 1% 1% 1%;width: 96%;" >
										<h1 id="page-header" style="margin:0; width:auto;float: left;padding: 0px;border: 0px;">Geographic</h1>
										<button type="button" id="geobuttonId"  class="btn" 
										data-toggle="collapse" data-target="#geographicEditId" style="float:right;background: none;border: 0px;box-shadow: none;">
										<img src="img/btn_plus.png"/></button>
								</div>
							</div>
							
							
							<div id="geographicEditId" class="panel-collapse collapse">
							<div class="row-fluid" >
												<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">Country</label>
																<div class="controls">
																	<s:select cssClass="span12" id="selectedCountryList"
				           										 		name="unifiedCampaignDTO.pSelectedCountry" list="unifiedCampaignDTO.countryList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCountryPlatcementList.{id}}">
				           										</s:select>
														</div>
													</div>
												  </article>
											
													<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">DMA</label>
																<div class="controls">
																	<s:select cssClass="span12 with-search"	multiple="true" id="geoSelect" onchange="dropDownChanged('geoSelect');"
				           										 		name="unifiedCampaignDTO.pSelectedDMA" list="unifiedCampaignDTO.dmaList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedDMAPlacementList.{id}}">
				           										</s:select>
														</div>
													</div>
												  </article>
												</div>
												
										<div class="row-fluid" >
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    		<label class="control-label" for="multiSelect">State</label>
														<div class="controls">
															<s:select cssClass="span12 with-search"	multiple="true" id="stateSelect" onchange="dropDownChanged('stateSelect');"
				           								 		name="unifiedCampaignDTO.pSelectedState" list="unifiedCampaignDTO.stateList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedStatePlacementList.{id}}">
				           									</s:select>
													</div>
												</div>
											</article>	
											
											 	<article class="span6">
                             					<div class="control-group" style="border-bottom: none;">
										    		<label class="control-label" for="multiSelect">City</label>
														<div class="controls">
														<input type="hidden"  class="span12" id="citySearchPlacement" />
													</div>
													<input type="hidden" name="unifiedCampaignDTO.selectedCity" id="cityJsonStrId" />
												</div>
                             					</article>
									       </div>
									       <div class="row-fluid" >
									      	 <article class="span6">
									      	 <div class="control-group" style="border-bottom: none;">
										    		<label class="control-label" for="multiSelect">Zip</label>
														<div class="controls">
														<input type="hidden"  class="span12" id="zipSearchPlacement" />
													</div>
													<input type="hidden" name="unifiedCampaignDTO.selectedZip" id="zipJsonStrId" />
												</div>
									      	 </article>
									       </div>
							</div>
							<br>
							<div id="addflightId">
							<div class="row-fluid"  >
							<h1 id="page-header" style="margin: 3% 1% 1% 1%;padding: 1%; background-color: #F5F5F5;" >Flight</h1>
							</div>
								<div id= "flightContainer">
								<s:iterator value="unifiedCampaignDTO.flightList" status="stat">
							 <div class="row-fluid" id="dynamicFlight_<s:property value='%{#stat.index}'/>">
							 <article class="span6">
													<div class="control-group" style="border-bottom: none;">
												    	<label class="control-label">Duration&nbsp;&nbsp;<span type="hidden" style="color: red;" id="flightDateError"> </span></label>
											    	<div class="controls">
																		<div class="input-append date startdatedivclass" id="datepicker-js"  data-date-format="mm-dd-yyyy">
																		<input class="datepicker-input startdateclass" id="startdateId_<s:property value='%{#stat.index}'/>" size="16" type="text" 
																	 title="Start date is required"	name="unifiedCampaignDTO.flightStartdate"  placeholder="Select a date" value="<s:property value="startDate"/>"/>
																		<span class="add-on"><i class="cus-calendar-2"></i></span>
																	</div>
 																to
															<div class="input-append date enddatedivclass" id="datepicker-js"  data-date-format="mm-dd-yyyy">
														<input class="datepicker-input enddateclass" id="enddateId_<s:property value='%{#stat.index}'/>"  size="16" type="text"  
														 title="End date is required" name="unifiedCampaignDTO.flightEnddate" 
														placeholder="Select a date"  value="<s:property value="endDate"/>"/>
													<span class="add-on"><i class="cus-calendar-2"></i></span>
												</div>
							    	</div>
								</div>
							</article>
											
											<article class="span3">
												<div class="control-group" style="margin-left:-5%;border-bottom: none;">
													<label class="control-label">Goal&nbsp;&nbsp;<span type="hidden" style="color: red;" id="flightGoalError" tabindex="-1"> </span> </label>
													<div class="controls">
														<input class="span12 gclass"   title="Goal is required" placeholder="Goal" onblur="validateFlightGoal();"
														id="goalId_<s:property value='%{#stat.index}'/>" name="unifiedCampaignDTO.flightGoal" 
														value="<s:property value="goal"/>">
													</div>
												</div>
											</article>
											
											 <!-- <article class="span4" >
												<div class="control-group removebutton"></div>
											  </article> -->
											  
											   <article class="span3" >
									<div class="control-group buttonId" style="border-bottom: none;">
									<label class="control-label">&nbsp;</label>
									<s:if test="%{#stat.index == 0}"> 
									<button type="button" id="addFlightButtonId" onclick="addFlight()" class="btn disabled"><i class="cus-add"></i>
										 Add Flight</button></s:if>
										<s:else>
										<button type="button" id="removeflightId_<s:property value='%{#stat.index}'/>" onclick="removeFlight('<s:property value='%{#stat.index}'/>')" class="btn disabled"><i class="cus-cross"></i>
										Remove Flight</button>	</s:else>
									
									</div>
								</article>
							 </div>
							 <s:if test="%{#stat.index == 0}">
								<div id="removeFlightDiv">						<!-- Start div for removeFlightDiv Group -->
							 </s:if>
							 </s:iterator>
							 
							</div>												<!-- End div for removeFlightDiv Group -->
   					 	   </div>
							</div>
							</div>
									<div id="buttonRowId" >
									<div class="row-fluid" >
								<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
										<%-- <s:submit type="button" id="nextButtonid" cssClass="btn btn-success btn-large" value="Add Campaign" key="Add Campaign">Save Placement</s:submit> --%>
									    <s:if test='unifiedCampaignDTO.statusId==4|| unifiedCampaignDTO.statusId==3'>
									    <button type="button" id="addPlacement" style="padding: 12px 22px;background: #8BB226;border: 0px;" class="btn btn-success btn-large" onclick="editPlacement('<s:property value="unifiedCampaignDTO.id"/>','<s:property value="unifiedCampaignDTO.placementId"/>','<s:property value="unifiedCampaignDTO.statusId"/>','<s:property value="unifiedCampaignDTO.campaignStatus"/>')" >
									    Save Placement</button> 
									    </s:if>
										<!-- <button type="button" id="generateMediaPlan" onclick="" class="btn btn-success btn-large">Generate Media Plan</button>  -->
										<button type="button" onclick="cancleTab()" style="padding: 12px 22px;background: #bd362f;border: 0px;" class="btn btn-danger btn-large">Cancel</button>
										<!--  <button type="button" id="nextButtonid" onclick="placementTab()" class="btn btn-primary btn-large">Add Placement</button>  -->
									</div>
								</article>
									</div>
								</div>
							
								
								<input type="hidden" id="campaignId" name="unifiedCampaignDTO.id" value="" >
								<input type="hidden" id="statusId" name="unifiedCampaignDTO.statusId" value="" >
								<input type="hidden" id="placementId" name="unifiedCampaignDTO.placementId" value="" >
								<input type="hidden" id="isDemographic" name="unifiedCampaignDTO.isDemographic" value="<s:property value="unifiedCampaignDTO.isDemographic"/>" >
								<input type="hidden" id="isGeographic" name="unifiedCampaignDTO.isGeographic" value="<s:property value="unifiedCampaignDTO.isGeographic"/>" >
								<input type="hidden" id="campaignStatus" name="unifiedCampaignDTO.campaignStatus" value="" >
					</s:form>
							  <div id="advertiserModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="left:8%;right:8%;margin-left:0px;display:none;">
									<div class="modal-body" id ="modalDivId">
									<div id=advertiserPopup >
								<div class="row-fluid"  >
								<h1 id="page-header" style="margin-left: 1%;background-color: #F5F5F5;">Add Advertiser</h1>
											<div class="row-fluid" >
												<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Advertiser Name<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="advertiserNameError"></span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Advertiser Name" 
													                    id="advertiserName" >
												</div>
											</div>
												  </article>
											
													<article class="span6">
												  <div class="control-group" style="border-bottom: none;">
   					 	 				  <label class="control-label" for="textarea">Address</label>
   					 	 				  <div class="controls">
													<input class="span12"  maxlength="49" type="text" placeholder="Address" 
													                    id="advertiserAddress">
												</div>
   					 					</div>
												  </article>
												</div>
														<div class="row-fluid" >
												<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Phone
											</label>
												<div class="controls">
													<input class="span12 "   maxlength="49" type="text" placeholder="Phone" 
													                    id="advertiserPhone" >
												</div>
											</div>
												  </article>
												  
												  	<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Fax
											</label>
												<div class="controls">
													<input class="span12 "   maxlength="49" type="text" placeholder="Fax" 
													                    id="advertiserFax">
												</div>
											</div>
												  </article>
												  
												  
												   
												</div>
														<div class="row-fluid" >
											<article class="span6">
												<div class="control-group" style="border-bottom: none;">
   					 	   											<label class="control-label" >Email</label>
															<div class="controls">
																<div><input type="email" class="span12" maxlength="49"   
																title="Email is required" placeholder="Email"  id="advertiserEmail" ></div>
															</div>
   					 	  									 </div>
   					 	   							</article>
														<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Zip
											</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Zip" 
													                    id="advertiserZip" >
												</div>
											</div>
												  </article>
												</div>
												
													<div class="row-fluid" >
									<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
									    <button type="button" id="addAdvertiserId" style="padding: 12px 22px;background: #8BB226;border: 0px;"  class="btn btn-success btn-large" onclick="addAdvertiser()" >
									    Save Advertiser</button> 
										<button type="button"  style="padding: 12px 22px;background: #bd362f;border: 0px;" onclick="cancleAdvertiser()" class="btn btn-danger btn-large">Cancel</button>
									</div>
									</article>
									</div>
							
							</div>
								</div>
								</div>
								</div>
								
									 <div id="agencyModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="left:8%;right:8%;margin-left:0px;display:none;">
									<div class="modal-body" id ="modalDivId">
									<div id=agencyPopup >
								<div class="row-fluid"  >
								<h1 id="page-header" style="margin-left: 1%;background-color: #F5F5F5;">Add Agency</h1>
											<div class="row-fluid" >
												<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Agency Name<span class="req star">*</span>&nbsp;&nbsp;<span type="hidden" style="color: red;" id="agencyNameError"></span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Agency Name" 
													                    id="agencyName" >
												</div>
											</div>
												  </article>
											
													<article class="span6">
												  <div class="control-group" style="border-bottom: none;">
   					 	 				  <label class="control-label" for="textarea">Address</label>
   					 	 				  <div class="controls">
													<input class="span12"  maxlength="49" type="text" placeholder="Address" 
													                    id="agencyAddress">
												</div>
   					 					</div>
												  </article>
												</div>
														<div class="row-fluid" >
												<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Phone
											</label>
												<div class="controls">
													<input class="span12 "   maxlength="49" type="text" placeholder="Phone" 
													                    id="agencyPhone" >
												</div>
											</div>
												  </article>
												  
												  	<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Fax
											</label>
												<div class="controls">
													<input class="span12 "   maxlength="49" type="text" placeholder="Fax" 
													                    id="agencyFax" >
												</div>
											</div>
												  </article>
												  
												  
												   
												</div>
														<div class="row-fluid" >
											<article class="span6">
												<div class="control-group" style="border-bottom: none;">
   					 	   											<label class="control-label" >Email</label>
															<div class="controls">
																<div><input type="email" class="span12" maxlength="49"   
																title="Email is required"  placeholder="Email"  id="agencyEmail"></div>
															</div>
   					 	  									 </div>
   					 	   							</article>
														<article class="span6">
												<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Zip
											</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Zip"  id="agencyZip">
												</div>
											</div>
												  </article>
												</div>
												
													<div class="row-fluid" >
									<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
									    <button type="button" id="addAgencyId" style="padding: 12px 22px;background: #8BB226;border: 0px;"  class="btn btn-success btn-large" onclick="addAgency()" >
									    Save Agency</button> 
										<button type="button" onclick="cancleAgency()" style="padding: 12px 22px;background: #bd362f;border: 0px;" class="btn btn-danger btn-large">Cancel</button>
									</div>
									</article>
									</div>
							
							</div>
								</div>
								</div>
								</div>
			
							<!-- Map Modal Window -->
								<div id="demoModel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="left:8%;right:8%;margin-left:0px;display:none;" data-ng-controller="mapexplorerController">
										<div id="smpDemoMap" class="demoPopup" >
										</div>
										
										<!-- Map Color Legend -->
										<div class="mapLegend">
											<div class="legendGroup">
												<div class="legendColorGroup">
													<span class="legendText">0 - 20</span>
													<div class="legendColorBox range1"></div>
												</div>
												
												<div class="legendColorGroup">
													<span class="legendText">20 - 40</span>
													<div class="legendColorBox range2"></div>
												</div>
												
												<div class="legendColorGroup">
													<span class="legendText">40 - 60</span>
													<div class="legendColorBox range3"></div>
												</div>
												
												<div class="legendColorGroup">
													<span class="legendText">60 - 80</span>
													<div class="legendColorBox range4"></div>
												</div>
												
												<div class="legendColorGroup">
													<span class="legendText">80 - 100</span>
													<div class="legendColorBox range5"></div>
												</div>
											</div>
										</div>
										
										<!-- Map Processing Bar -->			
										<div id="processingBar" class="processScreenSMP">
											<div class="processingContentSMP">
												<div class="loader" style="display:block"></div>
												<div style="display:block;float:left;">Calculating Rank</div>
											</div>
										</div>
										
										<!-- Map Custom Infowindow -->
											
											<div id="customInfoWindowSMP">
													<div class="containerRow" >
														<div class="containerColumn columnFirst">{{currentLocation.type}} : <span class="locationTitle">{{currentLocation.name}}</span></div>
													</div>
													<div class="containerRow">
														<div class="containerColumn columnFirst">Rank : <span class="censusFig">{{currentLocation.rank}}</span></div>
													</div>
													
													<div class="containerRow locationProd"  data-ng-show="isCurrentLocData">
														<div class="containerRow">
															<div class="containerColumn columnFirst">Total Products Available : <span class="censusFig">{{currentLocProdCount}}</span></div>
														</div>
														<div class="containerRow">
															<div class="containerColumn columnFirst">Total Impression Available : <span class="censusFig">{{currentLocProdImps|number}}</span></div>
														</div>
													</div>
													<div class="containerRow">
														<div class="containerColumn columnFirst">Total Number of {{currentLocation.genderTxt}} Households: <span class="censusFig">{{currentLocation.totalpop|number}}</span></div>
													</div>
						
													<div class="containerRow" data-ng-show="currentLocation.iseducation"></div>
													<div class="containerRow" data-ng-show="currentLocation.iseducation">
														<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households with {{currentLocation.educationtxt}} : <span class="censusFig">{{currentLocation.educationTot|number}}</span></div>
													</div>
													<div class="containerRow" data-ng-show="currentLocation.iseducation">
														<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households with {{currentLocation.educationtxt}} : <span class="censusFig">{{currentLocation.education|number:2}}%</span></div>
													</div>
													
						
													
													<div class="containerRow" data-ng-show="currentLocation.isage"></div>
													<div class="containerRow" data-ng-show="currentLocation.isage">
														<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households with {{currentLocation.agetxt}} : <span class="censusFig">{{currentLocation.ageTot|number}}</span> </div>
													</div>
													<div class="containerRow" data-ng-show="currentLocation.isage">
														<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.agetxt}} : <span class="censusFig">{{currentLocation.age|number:2}}%</span></div>
													</div>
						
						
													
													<div class="containerRow" data-ng-show="currentLocation.isethnicity"></div>
													<div class="containerRow" data-ng-show="currentLocation.isethnicity">
														<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households of {{currentLocation.ethnicitytxt}} : <span class="censusFig">{{currentLocation.ethnicityTot|number}}</span></div>
													</div>
													<div class="containerRow" data-ng-show="currentLocation.isethnicity">
														<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.ethnicitytxt}} : <span class="censusFig">{{currentLocation.ethnicity|number:2}}%</span></div>
													</div>
													
													
													
													<div class="containerRow" data-ng-show="currentLocation.isincome"></div>
													<div class="containerRow" data-ng-show="currentLocation.isincome">
														<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households of income {{currentLocation.incometxt}} : <span class="censusFig">{{currentLocation.incomeTot|number}}</span></div>
													</div>
													<div class="containerRow" data-ng-show="currentLocation.isincome">
														<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.incometxt}} : <span class="censusFig">{{currentLocation.income|number:2}}%</span></div>
													</div>
											</div>
											
											
											<!-- Product Impression Counts -->
											<div class="mapRowSMP">
												<div class="headerText">
													<div style="float:left;color:#747476" data-ng-show="!isCalculatingForecast"><img src="/img/explor.png">Estimated Impression : <span id="availImps" class="censusFig">{{ productTotal.impression | number }}</span>/month</div>
													<div style="float:left;color:#747476" data-ng-show="isCalculatingForecast"><div class="loader" style="display:block"></div>&nbsp;Calculating product estimated avails </div>
												</div>
											</div>
											
										<!-- Map Census Option and Rank controller -->
										<div class="demoMapOption"  id="demoMapController">
											<div class="demoMapOptionContainer">
													<div>
														<label class="control-label">Gender</label>
														<div class="controls">
														<select id="selectcensusGenderMap" class="selectcensusGender" onChange="applyRule('mapcontrol')">
															<option value="all">All</option>
															<option value="male">Male</option>
															<option value="female">Female</option>
														</select>
														</div>
													</div>
												</div>
											
											<s:iterator value="censusMap" var="census">
												<div class="demoMapOptionContainer">
												<div>
													<label class="control-label"><s:property value="#census.key"/></label>
													<div class="controls">
													<select id="selectcensus<s:property value="#census.key"/>Map" class="selectcensus<s:property value="#census.key"/>" onChange="applyRule('mapcontrol')">
														<option gender="true" value="">Select <s:property value="#census.key"/></option>
														<s:iterator value="#census.value" var="censusVal">
															<option 
															gender="<s:property value="#censusVal.gender"/>"
															bqColumn="<s:property value="#censusVal.bqColumn"/>"
															bqFemaleCol="<s:property value="#censusVal.bqFemaleCol"/>"
															bqMaleCol="<s:property value="#censusVal.bqMaleCol"/>"
															bqParentCol="<s:property value="#censusVal.bqParentCol"/>"
															value="<s:property value="#censusVal.groupTxt"/>"
															><s:property value="#censusVal.groupTxt"/></option>
														</s:iterator>
													</select>
													</div>
												</div>
												</div>
											
											</s:iterator>
										
											<div class="demoMapOptionContainer">
													<div>
														<label class="control-label">Rank</label>
														<div class="controls">
														<input id="rangeVal" type="text" maxlength=3 value=50 min=0 max=100 style="width: 32px;margin: 0px 2px;" onChange="rankValueChange()">
														<input  class="btn btn-primary" type="button" value="Rank"  data-ng-click="calculateRank()">
														</div>
													</div>
											</div>
											
										</div>
								</div>
			<!-- End of map modal -->
			
			
				</section>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="js.jsp"/>
		<script src="../js/bootstrap-datepicker_new.js"></script>
		<script type="text/javascript" src="../js/dataModel.js"></script>
		<script type="text/javascript" src="../js/bootstrap-select.js"></script>
		
		<script type="text/javascript" src="../js/geojs/smartplannerdemo.js?v=<s:property value="deploymentVersion"/>"></script>
		<script type="text/javascript" src="../js/geojs/smpModule.js?v=<s:property value="deploymentVersion"/>"></script>
	
	
		<script type="text/javascript">
		
		$('#demographicEditId').on('show', function() {
			$("#demobuttonId img").attr('src','img/btn_minus.png');
			$("#isDemographic").val("true");
		})
		$('#demographicEditId').on('hide', function() {
			$("#demobuttonId img").attr('src','img/btn_plus.png');
			$("#isDemographic").val("false");
		})
		$('#geographicEditId').on('show', function() {
		    $("#geobuttonId img").attr('src','img/btn_minus.png');
			$("#isGeographic").val("true");
		})
		$('#geographicEditId').on('hide', function() {
			$("#geobuttonId img").attr('src','img/btn_plus.png');
			$("#isGeographic").val("false");
		})
		
		</script>
		
		<script>
			function showDemo(){
				
				if(!checkOptionSelected()){
					toastr.warning("Please select an option from Age, Education, Ethnicity or Income.");
					return;
				}
				
				updateProductProperties();
				var $modal = $('#demoModel').modal({
					show:false
					}).css({
					    'width': '80%',
					    'height' : '80%'
					});
				$modal.modal('show');
				
				$('#demoModel').on('shown', function(){
					centerDemoController();
				});
				
				if(map == null){
					window.setTimeout(initMap,1000);
				}else{
					angular.element(document.getElementById('demoModel')).scope().calculateRank();
				}
				
				
			}
			
			$(window).resize(function(){
				centerDemoController();
			});
			
			function centerDemoController(){
				var modalW = $("#demoModel").width();
				var controlW = $("#demoMapController").width();
				
				var leftCod = modalW/2 - controlW/2
				$("#demoMapController").css("left",leftCod);

			}
		</script>
</body>
</html>