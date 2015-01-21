<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.dto.ProposalDTO" %>

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
	localStorage.clear();
</script>

<title>ONE - Insertion Order</title>

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
				<jsp:include page="OperationViewTabHeader.jsp" />
					<ul class="bwizard-steps">
						<li ><span class="label badge-inverse">1</span> Proposal Brief</li>
						<li><span class="label badge-inverse">2</span> Media Plan</li>
						<li class="active" style="cursor:pointer;font-family: 'Lato', Arial, Helvetica, sans-serif; font-size: 23px;">
							<span class="label badge-inverse">3</span> Insertion Order
						</li>
						<%-- <li><span class="label badge-inverse">4</span> Traffiking</li> --%>

					</ul>
					</br>

					<div class="fluid-container" style="margin-top: 10px;">

						<!-- widget grid -->
						<section id="widget-grid" class="">

							<!-- row-fluid -->

							<div class="row-fluid">
								<article class="span12">
									<!-- new widget -->
									<div class="jarviswidget" id="widget-id-0">
										<header>
											<h2>Insertion Order</h2>
										</header>
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

                                          <form id="proposalForm" class="form-horizontal themed" 
												 action="newProposal.lin" method="post">
											  <fieldset>
											   <div class="inner-spacer" style="min-height:900px;">
												 <!-- content goes here -->
												        
												        <div class="control-group">
															<label class="control-label">Date</label>
															<div class="controls"
																style="float: left; margin-left: 1%;">
																<div class="input-append date" id="datepicker-js"
																	data-date="" data-date-format="dd-mm-yyyy">																	
																		<input class="datepicker-input" size="16"  type="text"
																		 name="date" id="date" value="" placeholder="Select a date" /> <span
																		class="add-on"><i class="cus-calendar-2"></i>
																	</span>
																</div>
															</div>
														</div>	
														
														<div class="control-group">
															<label class="control-label" for="input01">Station</label>
															<div class="controls">
															 <s:if test="stationMap !=null && stationMap.size()>0">
																    <select name="station" id="station" class="span12 with-search" onchange="chooseStation()">
																      <option value="-1"></option>
																      <s:iterator value="stationMap">																       
																       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																      </s:iterator>
																      <option value="0" >Choose Custom</option>
																     </select>
																     <div id="customStationDiv" style="display:none">
																        <input name="customStation" type="text" class="span12" id="customStation" />
																     </div>
														     </s:if>
															  <s:else>
															       <select name="station" id="station" class="span12 with-search" onchange="chooseStation()">
															         <option value="0" >Choose Custom</option>
															       </select>
															       <div id="customStationDiv">
															        <input name="customStation" type="text" class="span12" id="customStation" />
															       </div>
															  </s:else>	
																    
																<!-- <input type="text" name="station" id="station" value="" class="span12" /> -->
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Acct Exec Name</label>
															<div class="controls">
																<input  type="text" name="accExecName" id="accExecName" class="span12" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Acct Exec Email</label>
															<div class="controls">
															  <input  type="email" name="accExecEmail" id="accExecEmail" class="span12"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Acct Exec Phone</label>
															<div class="controls">
																<input  type="text" name="accExecPhone" id="accExecPhone" class="span12" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Acct Exec Fax</label>
															<div class="controls">
															  <input  type="text" name="accExecFax" id="accExecFax" class="span12"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Acct Mgr Name</label>
															<div class="controls">
																<input  type="text" name="accMgrName" id="accMgrName" class="span12" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Acct Mgr Email</label>
															<div class="controls">
															  <input  type="email" name="accMgrEmail" id="accMgrEmail" class="span12"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Acct Mgr Phone</label>
															<div class="controls">
																<input  type="text" name="accMgrPhone" id="accMgrPhone" class="span12" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Acct Mgr Fax</label>
															<div class="controls">
															  <input  type="text" name="accMgrFax" id="accMgrFax" class="span12"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Agency Name</label>
															<div class="controls">
																 <s:if test="agencyMap !=null && agencyMap.size()>0">
																    <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
																      <option value="-1"></option>
																      <s:iterator value="agencyMap">																       
																       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																      </s:iterator>
																      <option value="0" >Choose Custom</option>
																     </select>
																     <div id="customAgencyDiv" style="display:none">
																        <input name="customAgency" type="text" class="span12" id="customAgency" />
																     </div>
																    </s:if>
																    <s:else>
																       <select name="agency" id="agency" class="span12 with-search" onchange="chooseAgency()">
																         <option value="0" >Choose Custom</option>
																       </select>
																       <div id="customAgencyDiv">
																        <input name="customAgency" type="text" class="span12" id="customAgency" />
																       </div>
																    </s:else>																
															</div>
														</div>
													
														<div class="control-group">
															<label class="control-label" for="multiSelect">Agency Address</label>
															<div class="controls">
																<input name="agencyAddress" type="text" class="span12" id="agencyAddress" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Agency Contact</label>
															<div class="controls">
																<input name="agencyContact" type="text" class="span12" id="agencyContact" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Agency Phone</label>
															<div class="controls">
																<input name="agencyPhone" type="text" class="span12" id="agencyPhone" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Agency Fax</label>
															<div class="controls">
																<input name="agencyFax" type="text" class="span12" id="agencyFax" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Agency Email</label>
															<div class="controls">
																<input name="agencyEmail" type="email" class="span12" id="agencyEmail" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Name</label>
															<div class="controls">
																
																<s:if test="advertiserMap !=null && advertiserMap.size()>0">
																  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
																  <option value="-1"></option>
																  <s:iterator value="advertiserMap">
																     
																     <option value='<s:property value="key"/>'><s:property value="value"/></option>
																  </s:iterator>
																  <option value="0" >Choose Custom</option>
																  </select>
																  <div id="customAdvertiserDiv" style="display:none">
																     <input name="customAdvertiser" type="text" class="span12" id="customAdvertiser" />
																   </div>
																</s:if>
																<s:else>
																  <select name="advertiser" id="advertiser"  class="span12 with-search" onchange="chooseAdvertiser()">
																      <option value="0" >Choose Custom</option>
																  </select>
																  <div id="customAdvertiserDiv" >
																      <input name="customAdvertiser" type="text" class="span12" id="customAdvertiser" />
																   </div>
																</s:else>
																
															</div>
														</div>														
														
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Address</label>
															<div class="controls">
																<input name="advertiserAddress" type="text" class="span12" id="advertiserAddress" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Contact</label>
															<div class="controls">
																<input name="advertiserContact" type="text" class="span12" id="advertiserContact" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Phone</label>
															<div class="controls">
																<input name="advertiserPhone" type="text" class="span12" id="advertiserPhone" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Fax</label>
															<div class="controls">
																<input name="advertiserFax" type="text" class="span12" id="advertiserFax" />
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="multiSelect">Advertiser Email</label>
															<div class="controls">
																<input name="advertiserEmail" type="email" class="span12" id="advertiserEmail" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">DBA/Billing Company</label>
															<div class="controls">
															 <s:if test="billingCompanyMap !=null && billingCompanyMap.size()>0">
																    <select name="billingCompName" id="billingCompName" class="span12 with-search" onchange="chooseBillingCompany()">
																      <option value="-1"></option>
																      <s:iterator value="billingCompanyMap">																       
																       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																      </s:iterator>
																      <option value="0" >Choose Custom</option>
																     </select>
																     <div id="customBillingCompanyDiv" style="display:none">
																        <input name="customBillingCompany" type="text" class="span12" id="customBillingCompany" />
																     </div>
														     </s:if>
															 <s:else>
															       <select name="billingCompName" id="billingCompName" class="span12 with-search" onchange="chooseBillingCompany()">
															         <option value="0" >Choose Custom</option>
															       </select>
															       <div id="customBillingCompanyDiv">
															        <input name="customBillingCompany" type="text" class="span12" id="customBillingCompany" />
															       </div>
															  </s:else>																
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="multiSelect">Billing Address</label>
															<div class="controls">
																<input name="billingAddress" type="text" class="span12" id="billingAddress" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Contact Person</label>
															<div class="controls">
															 <s:if test="billingContactMap !=null && billingContactMap.size()>0">
																    <select name="billingContact" id="billingContact" class="span12 with-search" onchange="chooseBillingContact()">
																      <option value="-1"></option>
																      <s:iterator value="billingContactMap">																       
																       <option value='<s:property value="key"/>'><s:property value="value"/></option>
																      </s:iterator>
																      <option value="0" >Choose Custom</option>
																     </select>
																     <div id="customBillingContactDiv" style="display:none">
																        <input name="customBillingContact" type="text" class="span12" id="customBillingContact" />
																     </div>
														     </s:if>
															 <s:else>
															       <select name="billingContact" id="billingContact" class="span12 with-search" onchange="chooseBillingContact()">
															         <option value="0" >Choose Custom</option>
															       </select>
															       <div id="customBillingContactDiv">
															        <input name="customBillingContact" type="text" class="span12" id="customBillingContact" />
															       </div>
															  </s:else>																
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Address</label>
															<div class="controls">
																<input name="billingAddress" type="text" class="span12" id="billingAddress" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Phone</label>
															<div class="controls">
																<input name="billingPhone" type="text" class="span12" id="billingPhone" />
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Email</label>
															<div class="controls">
																<input name="billingEmail" type="email" class="span12" id="billingEmail" />
															</div>
														</div>
														
														
														<div class="control-group">
															<label class="control-label" for="input01">Basis of Billing</label>
															<div class="controls">
																<input name="billingBasis" type="text" class="span12" id="billingBasis" value="LIN Mobile Ad Server"/>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Billing Payment Terms</label>
															<div class="controls">
																<input name="paymentTerms" type="text" class="span12" id="paymentTerms" value="NET 30"/>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Billing Rate Type</label>
															<div class="controls">
																<input name="rateType" type="text" class="span12" id="rateType" value="NET"/>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Billing Type</label>
															<div class="controls">
																<input name="billingType" type="text" class="span12" id="billingType" value="NET"/>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="input01">Billing Collection Type</label>
															<div class="controls">
																<input name="collectionType" type="text" class="span12" id="collectionType" value="NET"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Cancellation Policy</label>
															<div class="controls">
																<input name="cancellationPolicy" type="text" class="span12" id="cancellationPolicy" value="2 business days"/>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Billing Notes</label>
															<div class="controls">
																<textarea name="billingNote"  class="span12" id="billingNote" ></textarea>
															</div>
														</div>
														
														<div class="control-group">
															<label class="control-label" for="input01">Geo Targets</label>
															<div class="controls">
															
															<s:if test="geoTargetMap !=null && geoTargetMap.size()>0">
															    <select  multiple="multiple" name="geoTargets" id="geoTargets" onchange="chooseGeoTargets()" class="span12 with-search">
															      <option value="-1"></option>
															      <s:iterator value="geoTargetMap">															       
															       <option value='<s:property value="value"/>'><s:property value="value"/></option>
															      </s:iterator>
															       <option value="0" >Choose Custom</option>
															     </select>
															     <div id="customGeoTargetsDiv" style="display:none">
															        <input name="customGeoTargets" type="text" class="span12" id="customGeoTargets" />
															     </div>
														    </s:if>
															<s:else>
															    <select  multiple="multiple" name="geoTargets" id="geoTargets" onchange="chooseGeoTargets()" class="span12 with-search">
															      <option value="0" >Choose Custom</option>
															     </select>
															     <div id="customGeoTargetsDiv">
															        <input name="customGeoTargets" type="text" class="span12" id="customGeoTargets" />
															     </div>
															</s:else>																
															</div>
														</div>
													<div class="control-group">
															<label class="control-label" for="input01">Pixel/Conversion Window</label>
															<div class="controls">
																<input name="pixelConversionWindow" type="text" class="span12" id="pixelConversionWindow" />
															</div>
													</div>
													<div class="control-group">
															<label class="control-label" for="input01">Dayparting</label>
															<div class="controls">
																<input name="dayParting" type="text" class="span12" id="dayParting" />
															</div>
													</div>
													<div class="control-group">
															<label class="control-label" for="input01">Freq Cap</label>
															<div class="controls">
																<input name="freqCap" type="text" class="span12" id="freqCap" />
															</div>
												    </div>
													<div class="control-group">
															<label class="control-label" for="input01">Goals</label>
															<div class="controls">
																<input name="goals" type="text" class="span12" id="goals" />
															</div>
												    </div>
													<div class="control-group">
															<label class="control-label" for="input01">Reporting Instructions</label>
															<div class="controls">
																<textarea name="reportingInstructions"  class="span12" id="reportingInstructions" ></textarea>
															</div>
												    </div>
												    
												    
												    
											</div>
											<div class="form-actions">
												<a href="#" class="btn btn-primary" style="float: left;">Media
													Plan</a>
												<button onclick="saveProposalAndShowList(0)"
													class="btn medium btn-success" type="button">Save & Move to Proposal List</button>
												<button onclick="saveProposalAndShowList(1)"
													class="btn medium btn-success" type="button">Save & Move to Media Plan</button>
												<button type="reset" class="btn medium btn-danger"
													onclick="cancel()">Cancel</button>
											</div>
											</fieldset>
											<input type="hidden" id="proposalId" name="proposalId" value='<s:property value="proposalDTO.proposalId" />'/>
											<input type="hidden" id="nextPageControl" name="nextPageControl" value="0"/>
										  </form>
										</div>
										<!-- end content-->
									</div>
									<!-- end wrap div -->
							</div>
							<!-- end widget -->
							
					</div>

					<!-- end row-fluid -->

					</section>
					<!-- end widget grid -->
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
<script src="../js/insertionOrder.js?v=<s:property value="deploymentVersion"/>"></script>
 <script type="text/javascript">   
	var proposalId= '<s:property value="proposalDTO.proposalId" />';	
	var advertiser= "<s:property value="proposalDTO.advertiser" />";
	var agency= "<s:property value="proposalDTO.agency" />";
	var advertiserId= '<s:property value="proposalDTO.advertiserId" />';
	var agencyId= '<s:property value="proposalDTO.agencyId" />';	
	var geoTargets="<s:property value="proposalDTO.geoTargets" />";
	
	if(proposalId !=null && proposalId !=''){
		showInsertionOrderToEdit();
	}
  </script>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>

