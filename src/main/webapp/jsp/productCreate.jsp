<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:set name="theme" value="'simple'" scope="page" />
<jsp:include page="Header.jsp" />

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
		
<!-- <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> -->

<title>ONE - Create Product</title>
<!-- <meta name="description" content="">
<meta name="author" content="">

<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0"> -->

	<!-- Le CSS
    ================================================== --> 
<jsp:include page="css.jsp"/>
<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>

</head>

<body>
<div class="height-wrapper">
	<div id="main" role="main" class="container-fluid">
		<div class="contained">
			<!-- main content -->
			<div id="page-content" class="mlr">
			 <jsp:include page="navigationTab.jsp"/>
			 <jsp:include page="userManagementLeftMenu.jsp"/>
            <h1 style="margin: 1% 0 0 2%;" id="page-header">Create Product</h1>
            	<!-- start content-->
            	<div class="fluid-container" style="background: white;" id="publisherProductDiv" ng-app="productApp" ng-controller="productController">
					<section id="widget-grid" class="">
						<s:form id="select-demo-js" cssClass="themed">
							<div class="row-fluid" >
								<article class="span6" style="margin-left: 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Product Name<span class="req star">*</span>
											&nbsp;&nbsp;<span id="productAvailableMsg" class="req star"></span>
										</label>
										<div class="controls">
											<div><input type="text" id="productNameId" required="required" autocomplete="on" class="span12" ng-model="productNameModel"
													ng-change="isNameAvailable();"></div>
										</div>
									</div>
								</article>
								<article class="span6" style="margin-left: 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Rate</label>
										<div class="controls">
											<div><input type="text" autocomplete="on" class="span12" ng-model="rateModel"
													ng-change="modified(true);" ng-blur="formatRate();"></div>
										</div>
									</div>
								</article>
							</div>
							<div class="row-fluid" >
								<article class="span12 articleWidth" style="margin: -2% 0 0 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Note</label>
										<div class="controls">
											<textarea class="span12" name="note" rows="3" 
												ng-model="noteModel" ng-change="modified(true);">
											</textarea>
										</div>
									</div>
								</article>
							</div>
							<div class="row-fluid" >
								<!-- <article class="span12 articleWidth" style="margin: -2% 0 2% 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Ad Units</label>
										<div class="controls">
											<input type="hidden" class="span12"
			           								name="adUnitSearch" id="adUnitSearch"/>
										</div>
									</div>
								</article> -->
								<article class="span6" style="margin: -2% 0 2% 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Ad Units</label>
										<div class="controls">
											<input type="hidden" class="span12"
			           								name="adUnitSearch" id="adUnitSearch"/>
										</div>
									</div>
								</article>
								<article class="span6" style="margin: -2% 0 2% 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Platform</label>
										<div class="controls">
											<input type="text" id="platformSelect" class="span12" ui-select2="selectPlatform" 
			           							ng-model="selectedPlatforms" ng-change="platformChanged();"/>
										</div>
									</div>
								</article>
							</div>
							<div class="row-fluid" >
								<article class="span6" style="margin: -2% 0 0 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Context Category Type</label>
										<div class="controls">
											<input type="text" id="contextSelect" class="span12" ui-select2="selectContext" 
			           							ng-model="selectedContexts" ng-change="contextChanged();"/>
										</div>
									</div>
								</article>
								<article class="span6" style="margin: -2% 0 0 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Creative</label>
										<div class="controls">
											<input type="text" id="creativeSelect" class="span12" ui-select2="selectCreative" 
			           							ng-model="selectedCreatives" ng-change="creativeChanged();"/>
										</div>
									</div>
								</article>
							</div>
							<div class="row-fluid" >
								<!-- <article class="span6" style="margin-left: 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Platform</label>
										<div class="controls">
											<input type="text" id="platformSelect" class="span12" ui-select2="selectPlatform" 
			           							ng-model="selectedPlatforms" ng-change="platformChanged();"/>
										</div>
									</div>
								</article> -->
								<article class="span6" style="margin-left: 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Device</label>
										<div class="controls">
											<input type="text" id="deviceSelect" class="span12" ui-select2="selectDevice" 
			           							ng-model="selectedDevices" ng-change="deviceChanged();"/>
										</div>
									</div>
								</article>
								<article class="span6" style="margin-left: 5px;">
									<div class="control-group" style="border-bottom: 0px;">
										<label class="control-label">Device Capabilities</label>
										<div class="controls">
											<select id="deviceCapabilitySelect" class="span12 with-search" ng-options="dev.name for dev in allDeviceCapabilities"
												ng-model="deviceCapability" ng-change="modified(true);">
					           				</select>
										</div>
									</div>
								</article>
							</div>
							
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title" style="margin: 3% 0% -1% 2%;">Geo Targeting</h3>
								</div>
								<div class="panel-body">
									<div class="row-fluid" >
										<article class="span6" style="margin-left: 5px;">
											<div class="control-group" style="border-bottom: 0px;">
												<label class="control-label">Country</label>
												<div class="controls">
					           						<select class="span12 with-search" >
					           							<option id="US" selected="selected">United States</option>
					           						</select>
												</div>
											</div>
										</article>
										<article class="span6" style="margin-left: 5px;">
											<div class="control-group" style="border-bottom: 0px;">
												<label class="control-label">DMA</label>
												<div class="controls">
					           						<input type="text" id="geoSelect" class="span12" ui-select2="selectDma" 
					           							ng-model="selectedGeoTargets" ng-change="dmaChanged();"/>
												</div>
											</div>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span6" style="margin-left: 5px;">
											<div class="control-group" style="border-bottom: 0px;">
												<label class="control-label">State</label>
												<div class="controls">
													<input type="text" id="stateSelect" class="span12" ui-select2="selectState" 
					           							ng-model="selectedStates" ng-change="stateChanged();"/>
												</div>
											</div>
										</article>
										<article class="span6" style="margin-left: 5px;">
											<div class="control-group" style="border-bottom: 0px;">
												<label class="control-label">City</label>
												<div class="controls">
					           						<input type="hidden" class="span12"
					           								name="citySearch" id="citySearch"/>
												</div>
											</div>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span6" style="margin-left: 5px;">
											<div class="control-group" style="border-bottom: 0px;">
												<label class="control-label">Zip</label>
												<div class="controls">
					           						<input type="hidden" class="span12"
					           								name="zipSearch" id="zipSearch"/>
												</div>
											</div>
										</article>
									</div>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title" style="margin: 3% 0% -1% 2%;">Advance Targeting</h3>
								</div>
								<div class="panel-body">
									<div class="row-fluid" >
										<article class="span4" style="margin: 2% 0 0 22px;">
											<label style="font-weight: bold;"><input type="checkbox" id="geoFencingId"
												ng-change="modified(true);" ng-model="geoFencingModel"
												ng-true-value="true" ng-false-value="false">Geo Fencing</label>
										</article>
									</div>
									<div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="demographicId"
												ng-change="modified(true);" ng-model="demographicModel"
												ng-true-value="true" ng-false-value="false">Demographic Targeting</label>
										</article>
									</div>
									<!-- <div class="row-fluid" >
										<article class="span4" style="margin-left: 22px;">
			           						<label style="font-weight: bold;"><input type="checkbox" id="behaviourId"
												ng-change="modified(true);" ng-model="behaviourModel"
												ng-true-value="true" ng-false-value="false">Behavioral Targeting</label>
										</article>
									</div> -->
								</div>
							</div>
							<div class="row-fluid" >
								<article class="span12" style="margin-left: 5px;">
									<div class="control-group" >
										<label class="control-label"></label>
										<div class="controls">
											
										</div>
									</div>
								</article>
							</div>
							<div class="row-fluid" >
								<article class="span12" style="margin-left: 5px;">
									<div class="control-group">
										<button type="button" id="saveButtonId" ng-click="saveData(false)" class="btn btn-success btn-large">Finish</button>
										<button type="button" onclick="location.href='publisherProduct.lin'" class="btn btn-danger btn-large">Cancel</button>
										<div style="visibility: hidden;">
											<button type="button" id="autoSaveButtonId" ng-click="saveData(true)" class="btn btn-success btn-large"></button>
										</div>
									</div>
								</article>
							</div>
							<input type="hidden" class="span12" ng-model="networkIdModel"
											 		id="publisherId" ng-change="modified(true);">
					 		<input type="hidden" class="span12" ng-model="publisherNameModel" 
							ng-change="modified(true);">
						</s:form>
					</section>
					<!-- <h4>Product data</h4>
					<pre>{{productdata|json}}</pre> -->
				</div>
            	<!-- end content-->
			</div>
		</div>
	</div>
	<form name="submitForm"></form>
	<div class="push"></div>

	</div>
	<input type="hidden" id="networkUsername" value="<s:property value='userDetailsDTO.networkUsername'/>">
	<input type="hidden" id="networkId" value="<s:property value='userDetailsDTO.networkId'/>">
	<input type="hidden" id="publisherName" value="<s:property value='userDetailsDTO.publisherName'/>">
	<input type="hidden" id="productId" value="<s:property value='productId'/>">
	<input type="hidden" id="adServerId" value="<s:property value='adServerId'/>">
	<input type="hidden" id="partnerId" value="<s:property value='userDetailsDTO.companyId'/>">
	<input type="hidden" id="productStatus" value="<s:property value='productStatus'/>">
	
	<jsp:include page="js.jsp"/>
	
	<!-- 3rd Party Libraries Angular , Angular Routes , Jquery , Bootstrap --> 
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js"></script>
	<script src="../js/angular/angular-ui.js"></script>
	<script type="text/javascript" src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>
	<script src="http://ivaynberg.github.com/select2/select2-3.2/select2.js"></script>
	
	<!-- Angular Module  -->
	<script type="text/javascript" src="../js/angular/controller/productModule.js?v=<s:property value="deploymentVersion"/>"></script>
	
	<script>
		var cityJsonObj = [];
		var cityIds = [];
		var adUnitJsonObj = [];
		var adUnitIds = [];
		var zipJsonObj = [];
		var zipIds = [];
		
		var allOptionId = <s:property value="allOptionId"/>;
		var allOption = '<s:property value="allOption"/>';
		var noneOptionId = <s:property value="noneOptionId"/>;
		var noneOption = '<s:property value="noneOption"/>';
		
		var productStatus = '<s:property value="productStatus"/>';
	
		$(document).ready(function() {
			try{
				$('#productLi').attr('class', 'active');
				var adUnitLength = 0;
				toastr.options.timeOut = 1000;
				//$('#selectedlist').tagsinput();
				var count = 6;
				var contextUpdate = false;
				var creativeUpdate = false;
				var deviceUpdate = false;
				var dmaUpdate = false;
				var platformUpdate = false;
				var stateUpdate = false;
				var publisherProductScope = angular.element(document.getElementById("publisherProductDiv")).scope();
				publisherProductScope.loadPublisherProductData();
				
				if(productStatus == 'update') {
					publisherProductScope.safeApply(function(){
						  publisherProductScope.retrieveData();
						});
					
					
			    	function check() {
		    		  //console.log('check');
		    		  if(publisherProductScope.isDataRetrieved) {
			    		  if(!contextUpdate && publisherProductScope.IABContextList != undefined && publisherProductScope.IABContextList != null && publisherProductScope.IABContextList.length > 0) {
			    			  --count;
			    			  contextUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updateContext();
			    				});
			    			  //console.log('context Updated');
			    		  }
			    		  if(!creativeUpdate && publisherProductScope.creativeList != undefined && publisherProductScope.creativeList != null && publisherProductScope.creativeList.length > 0) {
			    			  --count;
			    			  creativeUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updateCreative();
			    				});
			    			  //console.log('creative Updated');
			    		  }
			    		  if(!deviceUpdate && publisherProductScope.deviceList != undefined && publisherProductScope.deviceList != null && publisherProductScope.deviceList.length > 0) {
			    			  --count;
			    			  deviceUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updateDevice();
			    				});
			    			  //console.log('device Updated');
			    		  }
			    		  if(!dmaUpdate && publisherProductScope.dmaList != undefined && publisherProductScope.dmaList != null && publisherProductScope.dmaList.length > 0) {
			    			  --count;
			    			  dmaUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updateDma();
			    				});
			    			  //console.log('DMA Updated');
			    		  }
			    		  if(!platformUpdate && publisherProductScope.platformList != undefined && publisherProductScope.platformList != null && publisherProductScope.platformList.length > 0) {
			    			  --count;
			    			  platformUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updatePlatform();
			    				});
			    			  //console.log('Platform Updated');
			    		  }
			    		  if(!stateUpdate && publisherProductScope.stateList != undefined && publisherProductScope.stateList != null && publisherProductScope.stateList.length > 0) {
			    			  --count;
			    			  stateUpdate = true;
			    			  publisherProductScope.safeApply(function(){
			    			  		publisherProductScope.updateState();
			    				});
			    			  //console.log('State Updated');
			    		  }
			    		  if(count == 0) {
			    			  publisherProductScope.isDataRetrieved = true;
			    			  isModified = false;
				    		  clearInterval(interval);
							  //console.log('clearInterval');
			    		  } 
		    		  }
		    		  else {
		    			  console.log('Waiting for data to be retrieved');
		    		  }
			    	}
			    	
			    	var interval = setInterval(check, 1000);
				}
				else {		// create new
					publisherProductScope.isDataRetrieved = true;
					$('#deviceCapabilitySelect').select2("val",0);
					publisherProductScope.deviceCapability = publisherProductScope.allDeviceCapabilities[0];
					isModified = false;
				}
				
		    	function save() {
		    		if(publisherProductScope.selectedAdUnits != undefined && adUnitLength != publisherProductScope.selectedAdUnits.length) {
		    			isModified = true;
		    		}
		    		adUnitLength = publisherProductScope.selectedAdUnits.length;
		    		$('#autoSaveButtonId').click();
		    	}
		    	
		    	/*********** ALL NONE Options Handling *************/
		    	function setSearchBoxDefaultValue(idArray, searchBoxJsonArray, defaultJson, searchBoxId) {
		    		if(productStatus != 'update') {
			    		searchBoxJsonArray.push(defaultJson);
			    		idArray.push(allOptionId);
		        		$('#'+searchBoxId).select2("data",defaultJson);
		    		}
		    	}
		    	
		    	function checkAllNoneOptionsForSearchBox(searchBoxId, allOptionJson, noneOptionJson) {
					var val = $('#'+searchBoxId).val();
					if (val != undefined && val != null && (val.indexOf(allOptionId) >= 0 || val.indexOf(noneOptionId) >= 0) && val.indexOf(',') >= 0) {
						var arr = [];
						if(val.indexOf(allOptionId) >= 0) {
							arr.push(allOptionJson);
						}else {
							arr.push(noneOptionJson);
						}
						$("#"+searchBoxId).select2("data",arr);
					}
				};
				/*********** ALL NONE Options Handling *************/
		    	
		    	setInterval(save,15000);
		    	
	    	    $('#citySearch').select2({
	    	        minimumInputLength: 2,
	    	        multiple : true,
	    	        placeholder: 'Search City',
	    	        ajax: {
	    	            url: "/searchCities.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "cities",
	    	                    limit: -1,
	    	                    searchText: term
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	            	//isModified = true;
	    	                return { results: data.cities }
	    	            }
	    	        },
	    	        formatResult: function(cities) {
	    	        	if($.inArray(cities.id, cityIds) == -1) {	// if not found
	    					cityJsonObj.push(cities);
	    	        		cityIds.push(cities.id);
	    				}
	    	            return "<div class='select2-user-result'>" + cities.name + "</div>";
	    	        },
	    	        formatSelection: function(cities) { 
	    	            return cities.name; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	        //a:setTimeout(function() {setSearchBoxDefaultValue(cityIds, cityJsonObj, {id:allOptionId,name:allOption,stateId:allOptionId}, 'citySearch');},500)
	    	    });
   	    		$('#citySearch').on("change", function(e) {
   	    			checkAllNoneOptionsForSearchBox('citySearch', {id:allOptionId,name:allOption,stateId:allOptionId}, {id:noneOptionId,name:noneOption,stateId:noneOptionId});
   	    			isModified = true;
   	    		});
	    	    
	    	    $('#adUnitSearch').select2({
	    	        minimumInputLength: 2,
	    	        multiple : true,
	    	        placeholder: 'Search Ad Units',
	    	        ajax: {
	    	            url: "/searchAdUnits.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "adUnits",
	    	                    adServerId : $('#networkId').val(),
	    	                    limit: -1,
	    	                    searchText: term
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	            	//isModified = true;
	    	                return { results: data.adUnits }
	    	            }
	    	        },
	    	        formatResult: function(adUnits) {
	    	        	if($.inArray(adUnits.id, adUnitIds) == -1) {	// if not found
	    	        		adUnitJsonObj.push(adUnits);
	    	        		adUnitIds.push(adUnits.id);
	    				}
	    	            return "<div class='select2-user-result'>" + adUnits.text + "</div>";
	    	        },
	    	        formatSelection: function(adUnits) { 
	    	            return adUnits.text; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	        //a:setTimeout(function() {setSearchBoxDefaultValue(adUnitIds, adUnitJsonObj, {id:allOptionId,name:allOption,canonicalPath:allOption,parentId:allOption,text:allOption}, 'adUnitSearch');},500)
	    	    });
	    	    $('#adUnitSearch').on("change", function(e) { 
   	    			isModified = true;
   	    			//checkAllNoneOptionsForSearchBox('adUnitSearch', {id:allOptionId,name:allOption,canonicalPath:allOption,parentId:allOption,text:allOption}, {id:noneOptionId,name:noneOption,canonicalPath:noneOption,parentId:noneOption,text:noneOption});
   	    		});
	    	    
	    	    $('#zipSearch').select2({
	    	        minimumInputLength: 2,
	    	        multiple : true,
	    	        placeholder: 'Search Zip',
	    	        ajax: {
	    	            url: "/searchZips.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "zips",
	    	                    limit: -1,
	    	                    searchText: term
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	                return { results: data.zips }
	    	            }
	    	        },
	    	        formatResult: function(zips) {
	    	        	if($.inArray(zips.id, zipIds) == -1) {	// if not found
	    	        		zipJsonObj.push(zips);
	    	        		zipIds.push(zips.id);
	    				}
	    	            return "<div class='select2-user-result'>" + zips.name + "</div>";
	    	        },
	    	        formatSelection: function(zips) { 
	    	            return zips.name; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	        //a:setTimeout(function() {setSearchBoxDefaultValue(zipIds, zipJsonObj, {id:allOptionId,name:allOption,cityId:allOption,cityName:allOption,stateId:allOptionId}, 'zipSearch');},500)
	    	    });
	    	    $('#zipSearch').on("change", function(e) {
	    	    	checkAllNoneOptionsForSearchBox('zipSearch', {id:allOptionId,name:allOption,cityId:allOption,cityName:allOption,stateId:allOptionId}, {id:noneOptionId,name:noneOption,cityId:noneOption,cityName:noneOption,stateId:noneOptionId});
   	    			isModified = true;
   	    		});
			}catch(err){
				console.log("error:"+err);
			}
			
		});
		
	</script>

</body>
<jsp:include page="googleAnalytics.jsp"/>
</html>

