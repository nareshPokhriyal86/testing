<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.lin.web.dto.ProposalDTO"%>
<jsp:include page="Header.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>

<script>
	localStorage.clear();
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
<title>ONE - Media Planner</title>

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
				<div id="page-content"
					style="min-height: 1000px; margin-right: 10px !important;">
					<!-- page header -->
					<jsp:include page="OperationViewTabHeader.jsp" />
					<ul class="bwizard-steps">
						<li><span class="label badge-inverse">1</span> Proposal
							Brief</li>
						<li class="active"
							style="font-family: 'Lato', Arial, Helvetica, sans-serif; font-size: 23px;">
							<span class="label badge-inverse">2</span> Media Plan</li>
						<li ><span class="label badge-inverse">3</span> Insertion Order</li>
						<%-- <li><span class="label badge-inverse">4</span> Traffiking</li> --%>

					</ul>
					</br>


					<div class="fluid-container" style="margin-top: 10px;">


						<div class="mystats indented"
							style="background: url('../img/backgrounds/mesh.png') repeat scroll 0 0 #333333; height: 80px; clear: both; border-radius: 4px 4px 4px 4px;">
							<div class="summary_bar">
								<div>PROPOSAL</div>

								<div class="summary_value"><s:property value="proposalDTO.proposalName" /></div>

							</div>
							<div class="summary_bar">
								<div>ADVERTISER</div>

								<div class="summary_value"><s:property value="proposalDTO.advertiser" /></div>

							</div>
							<div class="summary_bar">
								<div>AGENCY</div>

								<div class="summary_value"><s:property value="proposalDTO.agency" /></div>

							</div>
							<div class="summary_bar">
								<div>FLIGHT DATE</div>

								<div class="summary_value"><s:property value="proposalDTO.flightStartDate" /></div>

							</div>
							<div class="summary_bar">
								<div>END DATE</div>

								<div class="summary_value"><s:property value="proposalDTO.flightEndDate" /></div>

							</div>
						</div>
						</br> </br>


						<ul id="myTab" class="nav nav-tabs default-tabs"
							style="font-size: 20px; margin-left: 0px; margin-bottom: 0px; margin-top: 10px; width: 100%;">
							<li style="width: 919px;">&nbsp;</li>
							<li class="active cost"><a href="#" id="internal_cost"
								data-toggle="tab">COSTS</a></li>

							<li class="pricing"><a href="#" id="client_price"
								data-toggle="tab">PRICING</a></li>
							<li class="Revenue"><a href="#" id="Revenue_tab"
								data-toggle="tab">REVENUE</a></li>
							<li class="inventory"><a href="#" id="inv_forecast"
								data-toggle="tab">INVENTORY FORECAST</a></li>
						</ul>
						<form class="form-horizontal themed" action="" method="post">						
							<table id="placementTable" border="1" style="padding:1%;">
								<thead>
									<tr
										style="font-weight: bold; font-family: 'Lato', Arial, Helvetica, sans-serif;">


										<th style="text-align: center; width: 110px; display: none;"></th>
										<th
											style="text-align: center; font-size: 18px; width: 12% ; text-align: center;"><a
											onclick="displayResult(this)" id="add_placement"
											style="cursor: hand; cursor: pointer; float: left;"><i
												class="icon-plus-sign"></i>
										</a>PLACEMENT</th>
										<th style="text-align: center; font-size: 18px; width: 170px;">SITE</th>
										<th style="text-align: center; font-size: 18px; width: 170px;">AD
											SIZE</th>
										<th style="text-align: center; font-size: 18px; width: 170px;">FORMAT</th>
										<th style="text-align: center; font-size: 18px; width: 170px;">AD
											SERVER</th>
										<th class="publisher"
											style="text-align: center; font-size: 18px;">PUBLISHER
											CPM</th>
										<th class="publisherpayout"
											style="display: none; text-align: center; font-size: 18px;">PUBLISHER
											PAYOUT</th>
										<th class="1_party_header"
											style="text-align: center; font-size: 18px;">1ST PARTY AD SERVER COST</th>
										<th class="3_party_header"
											style="text-align: center; font-size: 18px;">3RD PARTY AD SERVER COST</th>
										<th class="totalcost"
											style="text-align: center; font-size: 18px;width:6%;">TOTAL COST</th>
										<th class="margin"
											style="display: none; text-align: center; width: 7%;font-size: 18px;">MARGIN (%)</th>
										<th class="margincpm"
											style="display: none; text-align: center;width: 7%; font-size: 18px;">MARGIN ($)</th>
										<th class="pricequote"
											style="display: none; text-align: center; width: 7%;font-size: 18px;">PRICE QUOTE ($)</th>
										<th class="bujget"
											style="display: none; text-align: center; font-size: 18px;">BUDGET
											ALLOCATION</th>
										<th class="imp"
											style="display: none; text-align: center; font-size: 18px;">PROPOSED
											IMPRESSION</th>
										<th class="grossrev"
											style="display: none; text-align: center; font-size: 18px;">GROSS REVENUE ($)</th>
										<th class="netrev"
											style="display: none; text-align: center; font-size: 18px;">NET REVENUE ($)</th>
										<th class="forecasted"
											style="display: none; text-align: center; font-size: 18px;">Forecasted
											IMPRESSION</th>
										<th class="reserved"
											style="display: none; text-align: center; font-size: 18px;">RESERVED
											IMPRESSION</th>
										<th class="availableimp"
											style="display: none; text-align: center; width: 10% ; font-size: 18px;">AVAILABLE
											IMPRESSION</th>
										<th class="proposed"
											style="display: none; text-align: center;  width: 10% ;font-size: 18px;">PROPOSED
											IMPRESSION</th>


									</tr>
								<tbody>

								</tbody>
							</table>
							<input type="hidden" id="proposalId" name="proposalId" value='<s:property value="proposalDTO.proposalId" />'/>
							<div id="dataAjaxLoader" style="text-align:center;margin-top:10px;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
						</form>
						<div class="form-actions" style="margin-top: 50px;">
							<a href="#" class="btn btn-primary"
								style="float: left;">Insertion Order</a>
							<button onclick="goToPage(1)"
									class="btn medium btn-success" type="button">Back to edit Proposal</button>
							
						     <button onclick="saveAllPlacementsUsingJson()"
									class="btn medium btn-success" type="button">Save all Placements</button>
					   </div>
				</div>
			 </div>
			 <!-- end main content -->

		 </div>
		 <!-- end widget div -->
	  </div>

    </div>
	
	<!--end fluid-container-->
	<div class="push"></div>

	<!-- end .height wrapper -->

	<!-- footer -->

	<!-- if you like you can insert your footer here -->

	<!-- end footer -->

	<!--================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<!-- Le javascript
    ================================================== -->
	<jsp:include page="js.jsp" />
	
<script src="../js/mediaPlan.js?v=<s:property value="deploymentVersion"/>"></script>
	<script type="text/javascript">
	 	var placementData = eval(<s:property escape="false" value="placementMap"/>);
	 	
    	function editProposal(proposalId){
			location.href="proposals.lin?proposalId="+proposalId;		
		}	
    	
    	
    </script>
    

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>
