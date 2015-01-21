<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<jsp:include page="Header.jsp" />

<!DOCTYPE html>
<html lang="en" data-ng-app="mapexplorerApp">
<head>

<script>
	localStorage.clear();
</script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
<script type="text/javascript"
	src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>


<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?libraries=visualization,places&sensor=false"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<title>ONE - Geo Target</title>

<meta name="MobileOptimized" content="320">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<jsp:include page="css.jsp" />
<link rel="stylesheet"
	href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
<link rel="stylesheet"
	href="../css/mapengine.css?v=<s:property value="deploymentVersion"/>">
<link
	href='http://fonts.googleapis.com/css?family=Lato&subset=latin,latin-ext'
	rel='stylesheet' type='text/css'>

</head>
<body>
	<!-- .height-wrapper -->
	<div class="height-wrapper">
		<div id="main" role="main" class="container-fluid">
			<div class="contained">
				<!-- main content -->
				<div id="page-content" class="mlr">
					<jsp:include page="navigationTab.jsp" />
					
					<div id="mapExplorer" class="mapContainer"
						data-ng-controller="mapexplorerController">
						<div id="processingBar" class="processScreen">
							<div class="processingContent">
								<div class="loader" style="display:block"></div>
								<div style="display:block;float:left;">Calculating Rank</div>
							</div>
						</div>
						
						<div id="customInfoWindow">
							<div class="containerRow" >
								<div class="containerColumn columnFirst">{{currentLocation.type}} : <span class="locationTitle">{{currentLocation.name}}</span></div>
							</div>
							<div class="containerRow">
								<div class="containerColumn columnFirst">Rank : <b>{{currentLocation.rank}}</b></div>
							</div>
							
							<div class="containerRow locationProd"  data-ng-show="isCurrentLocData">
								<div class="containerRow">
									<div class="containerColumn columnFirst">Total Products Available : <b>{{currentLocProdCount}}</b></div>
								</div>
								<div class="containerRow">
									<div class="containerColumn columnFirst">Total Impression Available : <b>{{currentLocProdImps|number}}</b></div>
								</div>
							</div>
							<div class="containerRow">
								<div class="containerColumn columnFirst">Total Number of {{currentLocation.genderTxt}} Households: {{currentLocation.totalpop|number}}</div>
							</div>

							<div class="containerRow" data-ng-show="currentLocation.iseducation"></div>
							<div class="containerRow" data-ng-show="currentLocation.iseducation">
								<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households with {{currentLocation.educationtxt}} : {{currentLocation.educationTot|number}}</div>
							</div>
							<div class="containerRow" data-ng-show="currentLocation.iseducation">
								<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households with {{currentLocation.educationtxt}} : {{currentLocation.education|number:2}}%</div>
							</div>
							

							
							<div class="containerRow" data-ng-show="currentLocation.isage"></div>
							<div class="containerRow" data-ng-show="currentLocation.isage">
								<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households with {{currentLocation.agetxt}} : {{currentLocation.ageTot|number}} </div>
							</div>
							<div class="containerRow" data-ng-show="currentLocation.isage">
								<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.agetxt}} : {{currentLocation.age|number:2}}%</div>
							</div>


							
							<div class="containerRow" data-ng-show="currentLocation.isethnicity"></div>
							<div class="containerRow" data-ng-show="currentLocation.isethnicity">
								<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households of {{currentLocation.ethnicitytxt}} : {{currentLocation.ethnicityTot|number}}</div>
							</div>
							<div class="containerRow" data-ng-show="currentLocation.isethnicity">
								<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.ethnicitytxt}} : {{currentLocation.ethnicity|number:2}}%</div>
							</div>
							
							
							
							<div class="containerRow" data-ng-show="currentLocation.isincome"></div>
							<div class="containerRow" data-ng-show="currentLocation.isincome">
								<div class="containerColumn columnFirst">Number of {{currentLocation.genderTxt}} Households of income {{currentLocation.incometxt}} : {{currentLocation.incomeTot|number}}</div>
							</div>
							<div class="containerRow" data-ng-show="currentLocation.isincome">
								<div class="containerColumn columnFirst">Percentage of {{currentLocation.genderTxt}} Households {{currentLocation.incometxt}} : {{currentLocation.income|number:2}}%</div>
							</div>
							
						</div>
						
						<div id="map_canvas" style="width: 100%; height: 100%"></div>
						
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
						
						<div class="mapController">
							<div class="mapRow">
								<div class="headerText">
									<div style="float:left;color:#747476" data-ng-show="!isCalculatingForecast"><img src="/img/explor.png">Estimated Impression : <span id="availImps" class="availImps">{{ productTotal.impression | number }}</span>/month</div>
									<div style="float:left;color:#747476" data-ng-show="isCalculatingForecast"><div class="loader" style="display:block"></div>&nbsp;Calculating product estimated avails </div>
								</div>
							</div>
							<div class="mapRow" id="censusContainer">
								<div class="censusContainer">
									<div class="containerRow" >
										<div class="containerColumn columnFirst">
											Gender
										</div>
										<div class="containerColumn columnlast">
											<select id="selectcensusGender">
												<option value="all">All</option>
												<option value="male">Male</option>
												<option value="female">Female</option>
											</select>
										</div>
									</div>
									
									<s:iterator value="censusMap" var="census">
									<div class="containerRow" >
										<div class="containerColumn columnFirst"><s:property value="#census.key"/></div>
										<div class="containerColumn columnlast">
										<select id="selectcensus<s:property value="#census.key"/>" onChange="applyRule('selectcensus<s:property value="#census.key"/>')">
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
									</s:iterator>
	
									

									<div class="containerRow" >
										<div class="containerColumn columnFirst">Geo Group</div>
										<div class="containerColumn columnlast">
											<select id="geoGroup">
												<option value="dma">DMA</option>
											</select>
										</div>
									</div>
									
									
									<div class="containerRow">
										<div class="containerColumn columnFirst"><button class="btn btn-primary" data-ng-click="calculateRank()">Rank</button></div>
									</div>
									
									
									<!--  
									<div class="containerRow">
										<input id="pac-input" class="controls" type="text" placeholder="Search Box">
										<button onClick="clearMarker()">Clear</button>
									</div>
									-->
								</div>
							</div>
						
							<div class="mapRow">
									<div class="containerRow">
										<div class="containerColumn columnFirst">Rank : <span id="sliderValue"></span></div>
									</div>
									
									<div class="containerRow">
										<div class="sliderHolder">
											<div id="slider-range"></div>
										</div>
									</div>
									
									<div class="containerRow">
										<div class="noteContent">Rank is calculated as the average percentile across all selected demographic brackets</div>
									</div>
									
									<div class="containerRow">
										<div class="lineBreak"></div>
									</div>
									
									<div  class="containerRow">
										<div class="containerColumn columnFirst">
											<button class="btn btn-success" data-ng-click="generateCampaign()">Generate Media Plan</button>
										</div>
									</div>
							</div>
						
						</div>
					</div>
					<form style="display: none" action="initCampaignFromMap.lin" id="campaignForm" method="post">
						<textarea id="formJsonParam" name="jsonParam"></textarea>
					</form>
				</div>


			</div>
			<!-- end main content -->
			<!-- </div> -->
		</div>
		<!--end fluid-container-->
	</div>
	<!-- end of jsp code -->


	<jsp:include page="js.jsp" />
	<script>
		var authToken = '<s:property value="accessToken"/>';
		$(document).ready(function() {
			$('#mapEngineLi').attr('class', 'main-nav-li_selected');
			var height = $(document).height();
			var width = $(document).width();
			$('#mapExplorer').css('height', height - 190);
			$('#mapExplorer').css('width', width - 100);
			
			
			$('#processingBar').css('height', height - 190);
			$('#processingBar').css('width', width - 100);
			
			$("#slider-range" ).slider({
			      range: false,
			      min: 0,
			      max: 100,
			      value: 50,
			      change: sliderChange,
				  slide : onSlide				      
			});
			
			$("#sliderValue").html($("#slider-range").slider("value"));
			$( "#productPerformance" ).tabs();
			$("#processingBar").hide();
		});

		window.onresize = function() {
			var height = $(document).height();
			var width = $(document).width();
			$('#mapExplorer').css('height', height - 190);
			$('#mapExplorer').css('width', width - 100);
			
			$('#processingBar').css('height', height - 190);
			$('#processingBar').css('width', width - 100);
			
		};
	</script>
	<script
		src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/geojs/geo.js?v=<s:property value="deploymentVersion"/>"></script>
	<script type="text/javascript" src="../js/geojs/mapexplorerModule.js?v=<s:property value="deploymentVersion"/>"></script>
	<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

</body>
<jsp:include page="googleAnalytics.jsp" />
</html>

