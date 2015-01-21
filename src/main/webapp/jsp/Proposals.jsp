<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
	var campaignTab = '<%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%>';
	$(document).ready(function() {
		$('#operationalViewLi').attr('class', 'main-nav-li_selected');
	});
	localStorage.clear();
</script>

<title>ONE - All Proposals</title>

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
							
				<!-- main content -->
				<div id="page-content" class="mlr">
					<jsp:include page="navigationTab.jsp"/>
					<%-- <jsp:include page="OperationViewTabHeader.jsp" /> --%>

					<!-- tabs view -->
					<!-- <h1 id="page-header">Proposal</h1> -->																	
					<div class="row-fluid">
						<%-- <ul class="nav nav-tabs upper_tabs" id="myTab1">
							<li class="active"><a href="#" id=""><%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%></a>
							</li>
						</ul> --%>
						
						<article class="span12">
							<div class="jarviswidget">
								<!-- end widget edit box -->
								<div > 
									<ul class="nav nav-tabs upper_tabs" id="myTab1">
										<li class="active"><a href="#" id=""><%=TabsName.CAMPAIGN_VIEW_CAMPAIGNS%></a>
										</li>
									</ul>
									<!-- widget grid -->
									<section id="widget-grid" class="">
	
										<!-- row-fluid -->
										<div class="row-fluid">
	
											<!-- article -->
											<!-- end article -->
	
										</div>
										
										<!-- row-fluid -->
										<div style="float: left">
											<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpCreateCampaign')}">
												<a class="btn btn-primary" href="/newProposal.lin">NEW CAMPAIGN</a>
											</s:if>
										</div>
										<div class="row-fluid">
											<article class="span12">
												<!-- new widget -->
												<div class="jarviswidget" id="widget-id-0">
													<header>
														<h2>CAMPAIGN</h2>
													</header>
													<!-- wrap div -->
													<div>
	
														<div class="jarviswidget-editbox">
															<div>
																<label>Title:</label> <input type="text" />
															</div>
															<div>
																<label>Styles:</label> <span
																	data-widget-setstyle="purple" class="purple-btn"></span>
																<span data-widget-setstyle="navyblue"
																	class="navyblue-btn"></span> <span
																	data-widget-setstyle="green" class="green-btn"></span> <span
																	data-widget-setstyle="yellow" class="yellow-btn"></span>
																<span data-widget-setstyle="orange" class="orange-btn"></span>
																<span data-widget-setstyle="pink" class="pink-btn"></span>
																<span data-widget-setstyle="red" class="red-btn"></span>
																<span data-widget-setstyle="darkgrey"
																	class="darkgrey-btn"></span> <span
																	data-widget-setstyle="black" class="black-btn"></span>
															</div>
														</div>
	
														<div class="inner-spacer">
															<!-- content goes here -->
															<table
																class="table table-striped table-bordered responsive dtable">
																<thead>
																	<tr>
																		<th>ID</th>
																		<th>NAME</th>
																		<th>ADVERTISER</th>
																		<th>AGENCY</th>
																		<th>SALES REP</th>
																		<th>FLIGHT DATES</th>
																		<!--<th>BUDGET</th>-->
																		<th>INDUSTRY</th>
																		<th>TYPE</th>
																		<th>STATUS</th>
																		<th>LAST MODIFIED ON</th>
																		<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpEditCampaign')}">
																			<th>&nbsp;</th>
																		</s:if>
																	</tr>
																</thead>
	
																<tbody>
	
																	<s:if test="proposalList !=null && proposalList.size()>0" >
																		<s:iterator value="proposalList" status="rowstatus">
																			<s:if test="#rowstatus.odd == true">
																				<tr class="odd gradeX">
																			</s:if>
																			<s:else>
																				<tr class="even gradeC">
																			</s:else>
	
																			<td><s:property value="proposalId"/></td>
																			<td><s:property value="proposalName"/></td>
																			<td><s:property value="advertiser"/></td>
																			<td><s:property value="agency"/></td>
																			<td><s:property value="salesRep"/></td>
																			<td><s:property value="flightStartDate"/> to <s:property value="flightEndDate"/></td>
																			<td><s:property value="industry"/></td>
																			<td><s:property value="proposalType"/></td>
																			<td><s:property value="proposalStatus"/></td>
																			<td><s:property value="updatedOn"/></td>
																			<s:if test="%{userDetailsDTO.authorisationKeywordList.contains('cmpEditCampaign')}">
																				<td><a href='javascript:editProposal(<s:property value="proposalId"/>)'><button type="reset"
																						class="btn medium btn-danger">Edit</button> </a></td>
																			</s:if>
																			</tr>
																		</s:iterator>
	
																	</s:if>
																	<s:else>
																		<tr class="odd gradeX">
																			<td></td>
																			<td></td>
																			<td></td>
																			<td></td>
																			<td></td>
																			<td>No proposals found</td>
																			<td></td>
																			<td></td>
																			<td></td>
																			<td></td>
																			<td></td>
																		</tr>
																	</s:else>
	
	
																</tbody>
															</table>
	
														</div>
														<!-- end content-->
													</div>
													<!-- end wrap div -->
												</div>
												<!-- end widget -->
											</article>
										</div>
										<!-- end row-fluid -->
									</section>
								</div>
							</div>
						</article>							
					</div>
					<div id="s4" style="display: none;">Industry News</div>
				</div>
				<!-- end content -->


			</div>
			<!-- end widget div -->
		</div>
		
	</div>	
	<!--end fluid-container-->
	
	<div class="push"></div>


	<jsp:include page="js.jsp" />

  <script type="text/javascript">
	function editProposal(proposalId){
		location.href="proposals.lin?proposalId="+proposalId;		
	}	
	
  </script>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>