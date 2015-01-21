<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.dto.ProposalDTO" %>
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
<script>
	var campaignbriefTab = '<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS_BRIEF%>';
	var mediaPlanTab = '<%=TabsName.CAMPAIGN_VIEW_MEDIA_PLANNER%>';
	var insertionOrderTab = '<%=TabsName.CAMPAIGN_VIEW_IO%>';
	$(document).ready(function() {
		$('#operationalViewLi').attr('class', 'main-nav-li_selected');
	});
	localStorage.clear();
</script>

<title>ONE - Proposal</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- Le CSS
    ================================================== -->
<jsp:include page="css.jsp" />
<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>


</head>

<body>
	<!-- .height-wrapper -->
	<div class="height-wrapper">

		<div id="main" role="main" class="container-fluid">

			<div class="contained">

				<div id="page-content" style="width: 94%;margin-left:40px;margin-right:40px;">
					<!-- page header -->
				<jsp:include page="navigationTab.jsp"/>
				<%-- <jsp:include page="OperationViewTabHeader.jsp" /> --%>
				<div class="row-fluid">
						
					
					<div > 
						<ul class="nav nav-tabs upper_tabs" id="myTab1">
							<li class="active"><a href="#s1" id="campaignBrief" class="upper_tab1"><%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS_BRIEF%></a></li>
							<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpMediaPlan')}">
								<li><a href="#s2" id="mediaPlan" data-toggle="tab"><%=TabsName.CAMPAIGN_VIEW_MEDIA_PLANNER%></a></li>
							</s:if>
							<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpInsertionOrder')}">
								<li><a href="#s3" id="insertionOrder" data-toggle="tab"><%=TabsName.CAMPAIGN_VIEW_IO%></a></li>
							</s:if>
							<a href="javascript:void(0);" onclick="cancel()" style="float: right; margin-top: 1%; margin-right: 1%;" class="btn btn-danger btn-large floatRight">Close</a>
							<a href="javascript:void(0);" onclick="saveProposalAndShowList(0)" style="float: right; margin-top: 1%; margin-right: 1%;" class="btn btn-success btn-large">Save</a>
						</ul>
						<div class="responsiveShowDiv">
						<ul  >
							<a href="javascript:void(0);" onclick="cancel()" style="float: right; margin-top: 1%; margin-right: 1%;" class="btn btn-danger btn-large floatRight">Close</a>
							<a href="javascript:void(0);" onclick="saveProposalAndShowList(0)" style="float: right; margin-top: 1%; margin-right: 1%;" class="btn btn-success btn-large">Save</a>
						</ul>
						</div>
						<div ng-app="mediaPlannerApp" ng-controller="gridDataCtrl" id="tab-content1" class="tab-content">
							<div class="fluid-container tab-pane active" id="s1">
								<form id="proposalForm" class="form-horizontal themed"  action="newProposal.lin" method="post">
									<div style="margin-left: 1%;">
										<s:if test="companyMap !=null && companyMap.size()==1 && !(#session.sessionDTO.superAdmin)">			<!-- not a super admin -->													   
										    <s:iterator value="companyMap">
										      <input type="hidden" name="company" id="company" value='<s:property value="key"/>'/>																      
										    </s:iterator>																   
										</s:if>
										<s:else>
											<label class="control-label">Company</label>
												<s:if test="companyMap !=null && companyMap.size()>1">
												  <Select name="company"  class="span4" id="company" onchange="getAdvertiserAgencyByCompany();">
												    <s:iterator value="companyMap">
												      <option value='<s:property value="key"/>'><s:property value="value"/></option>
												    </s:iterator>
												  </Select>
												</s:if>
												<s:elseif test="companyMap !=null && companyMap.size()==1 && #session.sessionDTO.superAdmin">																   
												    <s:iterator value="companyMap">
												      <input type="text" value='<s:property value="value"/>' readonly="readonly"  class="span4"/>
												      <input type="hidden" name="company" id="company" value='<s:property value="key"/>'/>																      
												    </s:iterator>																   
												</s:elseif>
												<s:else>
												  <Select name="company"  class="span4" id="company">
												    <option value=""></option>
												  </Select>
												</s:else>
										</s:else>
									</div>
									<br>
			
									<!-- row-fluid -->
			
									<div style="margin-left: 1%;" class="row-fluid Profile">
										
										<article class="span6">
											<!-- new widget -->
											<div class="jarviswidget" id="widget-id-0">
												<!-- wrap div -->
												<div>
			
													<div class="jarviswidget-editbox">
														<div>
															<label>Title:</label> <input type="text" />
														</div>
														<div>
															<label>Styles:</label> <span data-widget-setstyle="purple"
																class="purple-btn"></span> <span
																data-widget-setstyle="navyblue" class="navyblue-btn"></span>
															<span data-widget-setstyle="green" class="green-btn"></span>
															<span data-widget-setstyle="yellow" class="yellow-btn"></span>
															<span data-widget-setstyle="orange" class="orange-btn"></span>
															<span data-widget-setstyle="pink" class="pink-btn"></span>
															<span data-widget-setstyle="red" class="red-btn"></span> <span
																data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
															<span data-widget-setstyle="black" class="black-btn"></span>
														</div>
													</div>
			
													  <fieldset>
													   <div class="inner-spacer">
														 <!-- content goes here -->
																
																<div class="control-group">
																	<label class="control-label" for="multiSelect">Campaign Name<span class="req star">*</span></label>
																	<div class="controls">
																		<input name="proposalName" type="text" class="span12" id="proposalName" />
																	</div>
																</div>
																
																<div class="control-group">
																	<label class="control-label" for="multiSelect">Advertiser/Client Name<span class="req star">*</span></label>
																	<div class="controls">
																		<s:if test="advertiserMap !=null && advertiserMap.size()>0">
																		  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
																			  <s:iterator value="advertiserMap">
																			     <option value='<s:property value="key"/>'><s:property value="value"/></option>
																			  </s:iterator>
																		  </select>
																		</s:if>
																		<s:else>
																		  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
																		  </select>
																		</s:else>
																	</div>
																</div>
																
																<div class="control-group">
																	<label class="control-label" for="input01">Agency</label>
																	<div class="controls">
																		 <s:if test="agencyMap !=null && agencyMap.size()>0">
																		    <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
																		      <option value="-1">Select Agency</option>
																		      <s:iterator value="agencyMap">																       
																		       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																		      </s:iterator>
																		     </select>
																		    </s:if>
																		    <s:else>
																		       <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
																		         <option value="-1">Select Agency</option>
																		       </select>
																		    </s:else>																
																	</div>
																</div>
																
																<div class="control-group">
																	<label class="control-label" for="input01">Campaign Type<span class="req star">*</span></label>
																	<div class="controls">
																		<Select name="proposalType" class="span12" id="proposalType">
																		    <option value="-1"></option>
																		    <s:if test="campaignTypeMap !=null && campaignTypeMap.size()>0">
																		    	<s:iterator value="campaignTypeMap">																       
																		       		<option value='<s:property value="key"/>'><s:property value="value"/></option>
																		      	</s:iterator>
																		    </s:if>
																		</Select>
																	</div>
																</div>													
			
																<div class="control-group">
																	<label class="control-label" for="input01">Industry<span class="req star">*</span></label>
																	<div class="controls">
																	  <s:if test="industryMap !=null && industryMap.size()>0">
																	    <select name="industry" id="industry" onchange="chooseIndustry()" class="span12 with-search">
																	      <option value="-1"></option>
																	      <s:iterator value="industryMap">					       
																	       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																	      </s:iterator>
																	       <option value="0" >New</option>
																	     </select>
																	     <div id="customIndustryDiv" style="display:none">
																	        <input name="customIndustry" type="text" class="span12" id="customIndustry" />
																	     </div>
																      </s:if>
									  								  <s:else>
																	     <select  multiple="multiple" name="industry" id="industry" onchange="chooseIndustry()" class="span12 with-search">
																	       <option value="0" >New</option>
																	      </select>
																	      <div id="customIndustryDiv">
																	         <input name="customIndustry" type="text" class="span12" id="customIndustry" />
																	      </div>
																	  </s:else>
																	
																	</div>
																</div>
			
																<div id="flightStartDateDiv" class="control-group">
																	<label class="control-label">Flight Start Date<span class="req star">*</span></label>
																	<div class="controls">
																		<div class="input-append date" id="datepicker-js"
																			data-date="" data-date-format="mm-dd-yyyy">
																				<input class="datepicker-input" size="16"  type="text" onchange="$('#flightStartDateDiv').mousedown();"
																				 name="flightStartDate" id="flightStartDate" value="" placeholder="Select a date" /> <span
																				class="add-on"><i class="cus-calendar-2"></i>
																			</span>
																		</div>
																	</div>
																</div>
																
																<div id="flightEndDateDiv" class="control-group">
																	<label class="control-label">Flight End Date<span class="req star">*</span></label>
																	<div class="controls">
																		<div class="input-append date" id="datepicker-js"
																				data-date="" data-date-format="mm-dd-yyyy">
																				<input class="datepicker-input" size="16" type="text" onchange="$('#flightEndDateDiv').mousedown();"
																					name="flightEndDate" id="flightEndDate" value="" placeholder="Select a date" /> <span
																					class="add-on"><i class="cus-calendar-2"></i>
																				</span>
																		</div>
																	</div>
																</div>														
			
																<div class="control-group">
																	<label class="control-label">KPIs<span class="req star">*</span></label>
																	<div class="controls">
																	<s:if test="kpiMap !=null && kpiMap.size()>0">
																	    <select  multiple="multiple" name="kpi" id="kpi" onchange="chooseKpi()" class="span12 with-search">
																	      <option value="-1"></option>
																	      <s:iterator value="kpiMap">															       
																	       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																	      </s:iterator>
																	       <option value="0" >New</option>
																	     </select>
																	     <div id="customKpiDiv" style="display:none">
																	        <input name="customKpi" type="text" class="span12" id="customKpi" />
																	     </div>
																    </s:if>
																	<s:else>
																	    <select  multiple="multiple" name="kpi" id="kpi" onchange="chooseKpi()" class="span12 with-search">
																	      <option value="0" >New</option>
																	     </select>
																	     <div id="customKpiDiv">
																	        <input name="customKpi" type="text" class="span12" id="customKpi" />
																	     </div>
																	</s:else>
																	
																		
																	</div>
																</div>
													</div>
													</fieldset>
													<input type="hidden" id="proposalId" name="proposalId" value='<s:property value="proposalDTO.proposalId" />'/>
													<input type="hidden" id="nextPageControl" name="nextPageControl" value="0"/>
													<input type="hidden" id="placementData" name="placementData" value='<s:property value="proposalDTO.placementData" />'/>
												  
												</div>
												<!-- end content-->
											</div>
											<!-- end wrap div -->
										</article>
										
										<article style="margin-left: 0%;" class="span6">
											<!-- new widget -->
											<div class="jarviswidget" id="widget-id-0">
												<!-- wrap div -->
												<div>
			
													<div class="jarviswidget-editbox">
														<div>
															<label>Title:</label> <input type="text" />
														</div>
														<div>
															<label>Styles:</label> <span data-widget-setstyle="purple"
																class="purple-btn"></span> <span
																data-widget-setstyle="navyblue" class="navyblue-btn"></span>
															<span data-widget-setstyle="green" class="green-btn"></span>
															<span data-widget-setstyle="yellow" class="yellow-btn"></span>
															<span data-widget-setstyle="orange" class="orange-btn"></span>
															<span data-widget-setstyle="pink" class="pink-btn"></span>
															<span data-widget-setstyle="red" class="red-btn"></span> <span
																data-widget-setstyle="darkgrey" class="darkgrey-btn"></span>
															<span data-widget-setstyle="black" class="black-btn"></span>
														</div>
													</div>
			
													  <fieldset>
													   <div class="inner-spacer">
														 <!-- content goes here -->
			
																<!-- <div class="control-group">
																	<label class="control-label" for="multiSelect">Budget</label>
			
																	<div class="controls">
																		<input name="budget" type="text" onblur="validateBudget();" class="span12" id="budget" />
																	</div>
																</div> -->
																
																<div class="control-group">
																<label class="control-label" for="input01">Markets (DMAs)<span class="req star">*</span></label>
																<div class="controls">
																    <select  multiple="multiple" name="geoTargets" id="geoTargets" onchange="chooseGeoTargets()" class="span12 with-search">
																      <option value="-1"></option>
																      <s:iterator value="geoTargetMap">															       
																       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																      </s:iterator>
																    </select>
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="input01">Sales Contact Name<span class="req star">*</span></label>
																	<div class="controls">
																		<input name="salesRep" type="text" class="span12" id="salesRep" />
																	</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">Sales Email</label>
																<div class="controls">
																	<input  type="email" name="salesEmail" id="salesEmail" class="span12" 
																	value='<s:property value="proposalDTO.salesEmail" />' />
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">Sales Phone</label>
																<div class="controls">
																	<input  type="text" name="salesPhone" id="salesPhone" class="span12" 
																	value='<s:property value="proposalDTO.salesPhone" />' />
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">Trafficking Contact Name</label>
																<div class="controls">
																	<input  type="text" name="trafficContact" id="trafficContact" class="span12" 
																	value='<s:property value="proposalDTO.trafficContact" />' />
																</div>
															</div>
																													
															<div class="control-group">
																<label class="control-label" for="multiSelect">Trafficking Email</label>
																<div class="controls">
																	<input  type="email" name="trafficEmail" id="trafficEmail" class="span12" 
																	value='<s:property value="proposalDTO.trafficEmail" />' />
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="multiSelect">Trafficking Phone</label>
																<div class="controls">
																	<input  type="text" name="trafficPhone" id="trafficPhone" class="span12" 
																	value='<s:property value="proposalDTO.trafficPhone" />' />
																</div>
															</div>
															
															<div class="control-group">
																<label class="control-label" for="input01">Campaign Status<span class="req star">*</span></label>
																<div class="controls">
																	<Select name="proposalStatus" type="text" class="span12" id="proposalStatus">
																	    <!-- <option value="-1"></option> -->
																	    <s:if test="campaignStatusMap !=null && campaignStatusMap.size()>0">
																	    	<s:iterator value="campaignStatusMap">																       
																	       		<option value='<s:property value="key"/>'><s:property value="value"/></option>
																	      	</s:iterator>
																	    </s:if>
																	</Select>
																</div>
															</div>
															
													</div>
													</fieldset>
													<input type="hidden" name="budget" id="budget" value='<s:property value="proposalDTO.budget" />' />
													<input type="hidden" id="proposalId" name="proposalId" value='<s:property value="proposalDTO.proposalId" />'/>
													<input type="hidden" id="nextPageControl" name="nextPageControl" value="0"/>
													<input type="hidden" id="placementData" name="placementData" value='<s:property value="proposalDTO.placementData" />'/>
												</div>
												<!-- end content-->
											</div>
											<!-- end wrap div -->
										</article>
									</div>
									<!-- end widget -->
								</form>
							</div>
								
							<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpMediaPlan')}">
								<div class="fluid-container tab-pane" id="s2">
									<!-- <button ng-click="addRow()">New Placement</button> -->
									<div style="float: left">
										<a class="btn btn-primary" ng-click="addRow()">NEW PLACEMENT</a>
									</div>
									<br><br>
									<button ng-click="costDefs()">Cost</button>
							        <button ng-click="revenueDefs()">Revenue</button>
							        <div style="width: 1500px; height: 400px;"  ng-grid="gridOptions"></div>
								</div>
							</s:if>
							
							<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpInsertionOrder')}">	
								<div class="tab-pane fluid-container" id="s3">
									<button ng-click="IOcostDefs()">Cost</button>
							        <button ng-click="IOrevenueDefs()">Revenue</button>
							        <div style="width: 1500px; height: 400px; margin-top: 28px;"  ng-grid="IOgridOptions"></div>
							        <br>
							        <button onclick="generateIO('publisherIO');" class="btn medium btn-info" type="button">Publisher IO</button>
							        <button onclick="generateIO('clientIO');" class="btn medium btn-info" type="button">Client IO</button>
								</div>
							</s:if>
							<br>
							<br>
						</div>
					</div>
				</div>
			   </div>

			</div>
			<!-- end widget div -->
		</div>
		<!-- end widget -->

	</div>
	<!-- end: tabs view -->


	<!--end fluid-container-->
	<div class="push"></div>

	<!-- end .height wrapper -->
	
 <jsp:include page="js.jsp" />
 <script type="text/javascript" src="../js/angular/angular.min.js?v=<s:property value="deploymentVersion"/>"></script>
 <script type="text/javascript" src="../js/angular/ng-grid.min.js?v=<s:property value="deploymentVersion"/>"></script>
 <script type="text/javascript" src="../js/angular/mediaPlans.js?v=<s:property value="deploymentVersion"/>"></script>
 <script type="text/javascript" src="../js/proposals.js?v=<s:property value="deploymentVersion"/>"></script>
 
 <script type="text/javascript">   
	var proposalId= '<s:property value="proposalDTO.proposalId" />';
	var company= "<s:property value="proposalDTO.company" />";
	var proposalName= "<s:property value="proposalDTO.proposalName" />";
	var advertiser= "<s:property value="proposalDTO.advertiser" />";
	var agency= "<s:property value="proposalDTO.agency" />";
	var advertiserId= '<s:property value="proposalDTO.advertiserId" />';
	var agencyId= '<s:property value="proposalDTO.agencyId" />';
	var salesRep= "<s:property value="proposalDTO.salesRep" />";
	var industry= "<s:property value="proposalDTO.industry" />";
	var proposalType= '<s:property value="proposalDTO.proposalType" />';
	var proposalStatus= '<s:property value="proposalDTO.proposalStatus" />';
	var budget= '<s:property value="proposalDTO.budget" />';
	var flightStartDate= '<s:property value="proposalDTO.flightStartDate" />';
	var flightEndDate= '<s:property value="proposalDTO.flightEndDate" />';
	var kpi= '<s:property value="proposalDTO.kpi" />';
	var geoTargets="<s:property value="proposalDTO.geoTargets" />";
	
	if(proposalId !=null && proposalId !=''){
		showProposalDataToEdit();
	}
	var placementData = eval(<s:property escape="false" value="placementMap"/>);
	function editProposal(proposalId){
		location.href="proposals.lin?proposalId="+proposalId;		
	}
 </script>
 
<s:if test="%{saveStatus == 'completed'}">
	<script type="text/javascript"> successMessage('Data Saved Succesfully.'); </script>
</s:if>
<s:elseif test="%{saveStatus == 'failed'}">
	<script type="text/javascript"> errorMessage('Data Saved Failed. Please try again'); </script>
</s:elseif>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>

