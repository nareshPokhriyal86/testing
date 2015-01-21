<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.util.TabsName" %>
 
<jsp:include page="Header.jsp" /> 

<!DOCTYPE html>
<html lang="en">
<head>
	
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
	<script type='text/javascript' src='../js/accounting.js'></script>
	<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
	
	<link rel="stylesheet" href="../css/bootstrap-select.css">
	<link rel="stylesheet" href="../css/theme.css">
	<link rel="stylesheet" href="../css/bootstrap-datetimepicker.css"/>	
	</head>
	<body>
	<div class="height-wrapper">
  		<div id="main" role="main" class="container-fluid"  >
  			<div class="contained">
			<!-- main content -->
  		 <div id="page-content" class="mlr">
   			 <jsp:include page="navigationTab.jsp"/>
   			 <h1 id="page-header" style="margin-left: 1%;">Unified Campaign</h1>
   			 
   				 <div class="fluid-container" id ="removeBorderForm" style="background-color: white;" >
   					 <section id="widget-grid" class="">
   					  <s:form name = "unifiedCampaignForm" id="select-demo-js" cssClass=" themed" onsubmit="return getJsonData();" action="saveCampaign.lin" >
   					  <div id="step1Id">
   					 	   <div class="row-fluid" >
								  <article class="span6">
								      	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Campaign Name<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Campaign Name" 
													                    id="campaignName" name="unifiedCampaignDTO.campaignName" value="<s:property value="unifiedCampaignDTO.campaignName"/>">
												</div>
											</div>
									</article>
									
									<article class="span6">
										<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Rate<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required" title="Name is required" placeholder="Rate$"
														type="text" id="rate" name="unifiedCampaignDTO.rate" value="<s:property value="unifiedCampaignDTO.rate"/>">
												</div>
											</div>
											</article>
											
							
							</div>
							<div class="row-fluid" >
												<article class="span6">
												 <div class="control-group" style="border-bottom: none;">
                              						<label class="control-label">Rate Type 
                              							<span  class="help-Helpwidgets-TooltipWidget"></span>
							  						</label>
                                    				<div class="controls">
                                   				 		<s:select Class="span12" id="rateType" name="unifiedCampaignDTO.selectedRateType"
            												list="unifiedCampaignDTO.rateTypeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedRateTypeList.{id}}">
            											</s:select>
                                    					<br>
                                   					</div>
                             					</div>	
                             					</article>
                             					
                             						<article class="span6">
											<div class="control-group" style="border-bottom: none;">
													<label class="control-label">Budget<span class="req star">*</span>
													</label>
													<div class="controls">
														<input class="span12" required="required" title="Name is required" placeholder="Budget"
														type="number" id="budget" name="unifiedCampaignDTO.budget" value="<s:property value="unifiedCampaignDTO.budget"/>">
													</div>
												</div>
												</article>
												
										
										</div>	
										<div class="row-fluid" >
													<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label">Duration</label>
										    	<div class="controls">
																<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
																	<input class="datepicker-input" size="16" type="text" id="cStartDate" placeholder="Select a date" 
																	name="unifiedCampaignDTO.startDate" value="<s:property value="unifiedCampaignDTO.startDate"/>"/>
																	<span class="add-on"><i class="cus-calendar-2"></i></span>
																</div>
																to
																<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
																	<input class="datepicker-input" size="16" type="text"  id="cEndtDate" placeholder="Select a date" 
																	name="unifiedCampaignDTO.endDate" value="<s:property value="unifiedCampaignDTO.endDate"/>"/>
																	<span class="add-on"><i class="cus-calendar-2"></i></span>
																</div>
										    	
										    	
											</div>
											</div>
											</article>
											
											   <article class="span6">
   					 							   <div class="control-group" style="border-bottom: none;">
   					 	 									  <label class="control-label" for="textarea">Notes</label>
															<div class="controls">
																<textarea class="span12" id="address" rows="2" name="unifiedCampaignDTO.notes" placeholder="Notes" 
																><s:property value="unifiedCampaignDTO.notes"/></textarea>
															</div>
   					 							   </div>
   					 							   </article>
									
                             			   
											</div>
											       
									       <div class="row-fluid" >
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Platform</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.selectedPlatform" list="unifiedCampaignDTO.plateformList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedPlatformList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
												<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Creative</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="myId" onchange="chooseAdvertiser()"
				           						 		name="unifiedCampaignDTO.selectedCreative" list="unifiedCampaignDTO.creativeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCreativeList.{id}}" >
				           							<%-- <ul>
														<s:iterator value="unifiedCampaignDTO.selectedCreativeList">
       															 <li><s:property value="%{unifiedCampaignDTO.selectedCreativeList.{id}}"/>
														</s:iterator>
													</ul> --%>
				           							</s:select>
												</div>
											</div>
											</article>
									       </div>
									       
									       		       
									       <div class="row-fluid" >
													<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Context Category Type</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.selectedContext" list="unifiedCampaignDTO.contextList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedContextList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Device</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.selectedDevice" list="unifiedCampaignDTO.deviceList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedDeviceList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
									       </div>
									       
									        <div class="row-fluid" >
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Total Goal<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Total Goal" 
													                    id="totalGoal" name="unifiedCampaignDTO.totalGoal" value="<s:property value="unifiedCampaignDTO.totalGoal"/>">
												</div>
											</div>
											</article>
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Frequency Cap
											</label>
												<div class="controls">
													<input class="span12"  maxlength="49" type="text" placeholder="Frequency Cap" 
													                    id="frequencyCap" name="unifiedCampaignDTO.frequencyCap" value="<s:property value="unifiedCampaignDTO.frequencyCap"/>">
												</div>
											</div>
											</article>
									       </div>
											 <div class="row-fluid" >
												 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Geo Targeting</h1>
											 </div>
											<div class="row-fluid" >
												<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">Country</label>
																<div class="controls">
																	<s:select cssClass="span12 with-search"	multiple="true" id="selectedCountryList"
				           										 		name="unifiedCampaignDTO.selectedCountry" list="unifiedCampaignDTO.countryList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCountryList.{id}}">
				           										</s:select>
														</div>
													</div>
												  </article>
											
													<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">DMA</label>
																<div class="controls">
																	<s:select cssClass="span12 with-search"	multiple="true" id="selectedDMAList"
				           										 		name="unifiedCampaignDTO.selectedDMA" list="unifiedCampaignDTO.dmaList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedDMAList.{id}}">
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
															<s:select cssClass="span12 with-search"	multiple="true" id="selectAppViews"
				           								 		name="unifiedCampaignDTO.selectedState" list="unifiedCampaignDTO.stateList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedStateList.{id}}">
				           									</s:select>
													</div>
												</div>
											</article>	
											
											 	<article class="span6">
                             					<div class="control-group" style="border-bottom: none;">
										    		<label class="control-label" for="multiSelect">City</label>
														<div class="controls">
														<input type="hidden" class="span12"
			           								name="unifiedCampaignDTO.selectedCity" id="citySearch" />
													</div>
												</div>
                             					</article>
									       </div>
									
									<div class="panel panel-default">
							 <div class="row-fluid" style=" margin-bottom: -2%">
							 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Advance Targeting</h1>
							 </div>
								<div class="panel-body">
									<div class="row-fluid" >
										<article class="span4" style="margin: 2% 0 0 22px;">
											<label style="font-weight: bold;"><input type="checkbox" id="geoFencingId"
												name="unifiedCampaignDTO.geoFencing"
												value="true" <s:if test="%{unifiedCampaignDTO.geoFencing == true}" > checked="checked" </s:if>>Geo Fencing</label>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="demographicId"
												name="unifiedCampaignDTO.demoTarget"
												value="true" <s:if test="%{unifiedCampaignDTO.demoTarget == true}" > checked="checked" </s:if>>Demographic Targeting</label>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="behaviourId"
												name="unifiedCampaignDTO.behaTarget"
												value="true" <s:if test="%{unifiedCampaignDTO.behaTarget == true}" > checked="checked" </s:if> >Behavioral Targeting</label>
										</article>
									</div>
								</div>
							</div>
									    <%--    
											<div class="row-fluid" >
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Context Category Type</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="context" list="unifiedCampaignDTO.contextList" listKey="id" listValue="value" value="%{selectedContextList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
											</div> --%>
											
												<div class="row-fluid" >
									<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
										<button type="button" id="nextButtonid" onclick="nextTab('<s:property value='unifiedCampaignDTO.statusId'/>','<s:property value='unifiedCampaignDTO.id'/>');" class="btn btn-primary btn-large">Next</button>
										<button type="button" onclick="cancleTab()" class="btn btn-danger btn-large">Cancel</button>
									</div>
								</article>
							</div>
							</div>
							
							<div id="step2Id">
							 	 <div class="row-fluid" >
							 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Agency</h1>
							 </div>
   					 	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Name<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Agency Name" 
													                    id="agencyName" name="unifiedCampaignDTO.agencyName" value="<s:property value="unifiedCampaignDTO.agencyName"/>">
												</div>
								</div>
   					 	   </article>
   					 	   
   					 	   <article class="span6">
   					 	   
   					 	   <label class="control-label" for="textarea">Address</label>
															<div class="controls">
																<textarea class="span12" id="address" rows="2" name="unifiedCampaignDTO.agencyAddress" placeholder="Address" 
																><s:property value="unifiedCampaignDTO.agencyAddress"/></textarea>
															</div>
   					 	   
   					 	   </article>
   					 	   </div>
   					 	   	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Phone
											</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Phone" 
													                    id="phone" name="unifiedCampaignDTO.AgencyPhone" value="<s:property value="unifiedCampaignDTO.agencyPhone"/>" >
												</div>
								</div>
   					 	   </article>
   					 	   <article class="span6">
   					 	   <label class="control-label" >Email<span class="req star">*</span></label>
															<div class="controls">
																<div><input type="email" class="span12" maxlength="49"  
																title="Email is required" id="agencyEmailId" name="unifiedCampaignDTO.agencyEmail" value="<s:property value="unifiedCampaignDTO.agencyEmail"/>"
																></div>
															</div>
   					 	   
   					 	   </article>
   					 	   </div>
   					 	   	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Fax</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Fax" 
													                    id="fax" name="unifiedCampaignDTO.Agencyfax" value="<s:property value="unifiedCampaignDTO.agencyFax"/>">
												</div>
								</div>
   					 	   </article>
   					 	   <article class="span6">
   					 	   <label class="control-label" >Zip</label>
															<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="zip" 
													                    id="zip" name="unifiedCampaignDTO.Agencyzip" value="<s:property value="unifiedCampaignDTO.agencyZip"/>" >
												</div>
   					 	   
   					 	   </article>
   					 	   </div>
   					 	   
							 	 <div class="row-fluid" >
							 <h1 id="page-header" style="margin-left: 1%; font-size: 25px;">Advertiser</h1>
							 </div>
   					 	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Name<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Advertiser Name" 
													                    id="advertiserName" name="unifiedCampaignDTO.advertiserName" value="<s:property value="unifiedCampaignDTO.advertiserName"/>">
												</div>
								</div>
   					 	   </article>
   					 	   <article class="span6">
   					 	   
   					 	   <label class="control-label" for="textarea">Address</label>
															<div class="controls">
																<textarea class="span12" id="advertiserAddress" rows="2" name="unifiedCampaignDTO.advertiserAddress"
																 placeholder="Address"><s:property value="unifiedCampaignDTO.advertiserAddress"/></textarea>
															</div>
							
   					 	   </article>
   					 	   </div>
   					 	   	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Phone
											</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Phone" 
													                    id="advertiserPhone" name="unifiedCampaignDTO.advertiserPhone" value="<s:property value="unifiedCampaignDTO.advertiserPhone"/>" >
												</div>
								</div>
   					 	   </article>
   					 	   <article class="span6">
   					 	   <label class="control-label" >Email<span class="req star">*</span></label>
															<div class="controls">
																<div><input type="email" class="span12" maxlength="49"   
																title="Email is required" id="advertiserEmailId" name="unifiedCampaignDTO.advertiserEmail" 
																 value="<s:property value="unifiedCampaignDTO.advertiserEmail"/>"></div>
															</div>
   					 	   
   					 	   </article>
   					 	   </div>
   					 	   	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Fax</label>
												<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="Fax" 
													                    id="advertiserFax" name="unifiedCampaignDTO.advertiserFax" value="<s:property value="unifiedCampaignDTO.advertiserFax"/>">
												</div>
								</div>
   					 	   </article>
   					 	   <article class="span6">
   					 	   <label class="control-label" >Zip</label>
															<div class="controls">
													<input class="span12"   maxlength="49" type="text" placeholder="zip" 
													                    id="advertiserZip" name="unifiedCampaignDTO.advertiserZip" value="<s:property value="unifiedCampaignDTO.advertiserZip"/>" >
												</div>
   					 	   
   					 	   </article>
   					 	   </div>
   					 	   
   					<%--  	   			 <div class="row-fluid" >
							 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Flights</h1>
							 </div>
							  <div id= "flightContainer">
							 <div class="row-fluid" id="dynamicFlight_1">
							 <article class="span6">
													<div class="control-group" style="border-bottom: none;">
												    	<label class="control-label">Duration</label>
											    	<div class="controls">
																		<div class="input-append date " id="datepicker-js"  data-date-format="mm-dd-yyyy">
																		<input class="datepicker-input startdateclass" id="startdateId_1" size="16" type="text" 
																		name="unifiedCampaignDTO.flightStartdate"  placeholder="Select a date"/>
																		<span class="add-on"><i class="cus-calendar-2"></i></span>
																	</div>
 																to
															<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
														<input class="datepicker-input enddateclass" id="enddateId_1"  size="16" type="text"  name="unifiedCampaignDTO.flightEnddate" placeholder="Select a date"  />
													<span class="add-on"><i class="cus-calendar-2"></i></span>
												</div>
							    	</div>
								</div>
							</article>
											
											<article class="span3">
												<div class="control-group" style="margin-left:-5%;border-bottom: none;">
													<label class="control-label">Goal<span class="req star">*</span>
												</label>
													<div class="controls">
														<input class="span12 gclass" required="required" title="Goal is required" placeholder="Goal"
														type="number" id="goalId_1" name="unifiedCampaignDTO.flightGoal" value="<s:property value="unifiedCampaignDTO.agencyAddress"/>">
													</div>
												</div>
											</article>
											
											 <!-- <article class="span4" >
												<div class="control-group removebutton"></div>
											  </article> -->
											  
											   <article class="span3" >
									<div class="control-group buttonId">
									<button type="button" id="addFlightButtonId" onclick="addFlight()" class="btn disabled btn-link"><i class="cus-add"></i> Add Flight</button>	
									</div>
								</article>
							 </div>
							 
							
   					 	   </div> --%>
   					 	   
   					 	   				<div class="row-fluid" >
									<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
									<button type="button" id="nextButtonid" onclick="backTab()" class="btn btn-primary btn-large">Back</button>
										<s:submit type="button" id="nextButtonid" cssClass="btn btn-success btn-large" value="Add Campaign" key="Add Campaign">Save Campaign</s:submit> 
										<button type="button" onclick="cancleTab()" class="btn btn-danger btn-large">Cancel</button>
										<!--  <button type="button" id="nextButtonid" onclick="placementTab()" class="btn btn-primary btn-large">Add Placement</button>  -->
									</div>
								</article>
							</div>
					
   					 	   </div>
   					 	    
   					 	   <input type="hidden" id="statusId" name="unifiedCampaignDTO.statusId" value="" >
   					 	   <input type="hidden" id="campaignId" name="unifiedCampaignDTO.id" value="" >
									</s:form>
									
			<%-- 						<!-- placement form -->
									<s:form name="placementForm" id="placementForm" cssClass="form-horizontal themed" action="savePlacement.lin">
									<div id="step3Id">
   					 	   <div id="dynamicPlacement_1">
   					 	    <div class="row-fluid" >
							 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Placements</h1>
							 </div>
							  	   <div class="row-fluid" >
   					 	   <article class="span6">
   					 	    	<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Name<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Placement Name" 
													                    id="placementNameId" name="unifiedCampaignDTO.placementName" value="<s:property value="unifiedCampaignDTO.placementName"/>">
												</div>
								</div>
   					 	   </article>
   					 	   
   					 					<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label">Duration</label>
										    	<div class="controls">
																<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
																	<input class="datepicker-input" size="16" type="text" id="pStartDate" placeholder="Select a date" 
																	name="unifiedCampaignDTO.pStartDate" value="<s:property value="unifiedCampaignDTO.pStartDate"/>"/>
																	<span class="add-on"><i class="cus-calendar-2"></i></span>
																</div>
																to
																<div class="input-append date" id="datepicker-js"  data-date-format="mm-dd-yyyy">
																	<input class="datepicker-input" size="16" type="text"  id="pEndtDate" placeholder="Select a date" 
																	name="unifiedCampaignDTO.pEndDate" value="<s:property value="unifiedCampaignDTO.pEndDate"/>"/>
																	<span class="add-on"><i class="cus-calendar-2"></i></span>
																</div>
										    	
										    	
											</div>
											</div>
											</article>
   					 	   </div>
   					 	   
   					 	         <div class="row-fluid" >
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Platform</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.pSelectedPlatform" list="unifiedCampaignDTO.plateformList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedPlatformPlacementList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
												<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Creative</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.pSelectedCreative" list="unifiedCampaignDTO.creativeList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCreativePlacementList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
									       </div>
									       
									              <div class="row-fluid" >
													<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Context Category Type</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.pSelectedContext" list="unifiedCampaignDTO.contextList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedContextPlacementList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
											<article class="span6">
											<div class="control-group" style="border-bottom: none;">
										    	<label class="control-label" for="multiSelect">Device</label>
												<div class="controls">
													<s:select Class="span12 with-search"	multiple="true" id="selectAppViews"
				           						 		name="unifiedCampaignDTO.pSelectedDevice" list="unifiedCampaignDTO.deviceList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedDevicePlacementList.{id}}">
				           							</s:select>
												</div>
											</div>
											</article>
									       </div>
									       
									       		<div class="row-fluid" >
											
                             						<article class="span6">
											<div class="control-group" style="border-bottom: none;">
											<label class="control-label">Goal<span class="req star">*</span>
											</label>
												<div class="controls">
													<input class="span12" required="required"  maxlength="49" type="text" placeholder="Total Goal" 
													                    id="totalGoal" name="unifiedCampaignDTO.pTotalGoal" value="<s:property value="unifiedCampaignDTO.pTotalGoal"/>">
												</div>
											</div>
											</article>
                             						<article class="span6">
											<div class="control-group" style="border-bottom: none;">
													<label class="control-label">Budget<span class="req star">*</span>
													</label>
													<div class="controls">
														<input class="span12" required="required" title="Name is required" placeholder="Budget"
														type="number" id="budget" name="unifiedCampaignDTO.pBudget" value="<s:property value="unifiedCampaignDTO.pBudget"/>">
													</div>
												</div>
												</article>
												
										
										</div>	
										
									       				 <div class="row-fluid" >
												 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Geo Targeting</h1>
											 </div>
											<div class="row-fluid" >
												<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">Country</label>
																<div class="controls">
																	<s:select cssClass="span12 with-search"	multiple="true" id="selectedCountryList"
				           										 		name="unifiedCampaignDTO.pSelectedCountry" list="unifiedCampaignDTO.countryList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedCountryPlacementList.{id}}">
				           										</s:select>
														</div>
													</div>
												  </article>
											
													<article class="span6">
														<div class="control-group" style="border-bottom: none;">
										 			   		<label class="control-label" for="multiSelect">DMA</label>
																<div class="controls">
																	<s:select cssClass="span12 with-search"	multiple="true" id="selectedDMAList"
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
															<s:select cssClass="span12 with-search"	multiple="true" id="selectAppViews"
				           								 		name="unifiedCampaignDTO.pSelectedState" list="unifiedCampaignDTO.stateList" listKey="id" listValue="value" value="%{unifiedCampaignDTO.selectedStatePlacementList.{id}}">
				           									</s:select>
													</div>
												</div>
											</article>	
											
											 	<article class="span6">
                             					<div class="control-group" style="border-bottom: none;">
										    		<label class="control-label" for="multiSelect">City</label>
														<div class="controls">
														<input type="hidden" class="span12"
			           								name="unifiedCampaignDTO.selectedplacementCity" id="citySearchPlacement" />
													</div>
												</div>
                             					</article>
									       </div>
									       
									       				
									<div class="panel panel-default">
							 <div class="row-fluid" style=" margin-bottom: -2%">
							 <h1 id="page-header" style="margin-left: 1%;font-size: 25px;">Advance Targeting</h1>
							 </div>
								<div class="panel-body">
									<div class="row-fluid" >
										<article class="span4" style="margin: 2% 0 0 22px;">
											<label style="font-weight: bold;"><input type="checkbox" id="geoFencingId"
												name="unifiedCampaignDTO.geoFencing"
												value="true" <s:if test="%{unifiedCampaignDTO.pGeoFencing == true}" > checked="checked" </s:if>>Geo Fencing</label>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="demographicId"
												name="unifiedCampaignDTO.demoTarget"
												value="true" <s:if test="%{unifiedCampaignDTO.pDemoTarget == true}" > checked="checked" </s:if>>Demographic Targeting</label>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="behaviourId"
												name="unifiedCampaignDTO.behaTarget"
												value="true" <s:if test="%{unifiedCampaignDTO.pBehaTarget == true}" > checked="checked" </s:if> >Behavioral Targeting</label>
										</article>
									</div>
								</div>
							</div>
							
   					 	   				<div class="row-fluid" >
									<article class="span12"  style="margin-left: 1%;">
									<div class="control-group">
									<button type="button" id="" onclick="backPlacementTab()" class="btn btn-primary btn-large">Back</button>
										<s:submit type="button" id="nextButtonid" cssClass="btn btn-success btn-large" value="Add Campaign" key="Add Campaign">Save Placement</s:submit> 
										<button type="button" onclick="cancleTab()" class="btn btn-danger btn-large">Cancel</button>
										<button type="button" id="nextButtonid" onclick="addMorePlacementTab()" class="btn btn-primary btn-large">Add More</button>
										<button type="button" id="preId" onclick="prePlacementTab('<s:property value="unifiedCampaignDTO.id"/>')" style="float: right;" class="btn btn-primary btn-large">Pre</button>
										<button type="button" id="nextId" onclick="nextPlacementTab()" style="float: right;" class="btn btn-primary btn-large">Next</button>
									</div>
								</article>
							</div>
							</div>
   					 	   </div> 
									
									</s:form> --%>
								</section>
							 </div>
						</div>
   					 </div>
  				</div>
 			 </div>
		</body>
		<jsp:include page="js.jsp"/>
		<script src="../js/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="../js/dataModel.js"></script>
		<script type="text/javascript" src="../js/unifiedCampaign.js"></script>
		<script type="text/javascript">
		$( document ).ready(function() {
			   $('input').change(function(e)  {
			        var goal = parseFloat($('#totalGoal').val());
			        var rate = parseFloat($('#rate').val());
			        var budget = 0.0;
			        if(goal!=undefined && goal!=null && rate!=undefined && rate!=null){
			        	budget = (goal*rate)/1000;
				        $('#budget').val( budget);
			        }
			        
			    });
		});
		</script>
	</html>