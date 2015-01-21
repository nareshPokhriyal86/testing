var flightIncrement = 1;
var campaignIdEdit = "";
// New Module

var oneAudianceApp = angular.module('oneAudianceApp', [ 'ngRoute',
		'ui.bootstrap' ]);

// Config + Route
oneAudianceApp.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/campaignList', {
		controller : 'audienceController',
		templateUrl : '/jsp/SmartCampaignList.jsp'
	}).when('/createCampaign', {
		controller : 'audienceController',
		templateUrl : '/jsp/SmartCampaignPlanner.jsp'
	}).when('/editCampaign', {
		controller : 'audienceController',
		templateUrl :'/jsp/SmartCampaignPlanner.jsp'
	}).when('/smartplanner', {
		controller : 'audienceController',
		templateUrl : 'view/audience_step3.html'
	}).otherwise({
		redirectTo : '/campaignList'
	});

} ]);

// Factory
oneAudianceApp.factory('audianceFactory', function($http) {
	var factory = {};

	var status = [ {
		value : 0,
		text : "Proposal"
	}, {
		value : 1,
		text : "Contract"
	}, {
		value : 2,
		text : "Delevering"
	}, {
		value : 3,
		text : "Completed"
	} ];

	var campaignType = [ {
		value : 0,
		text : "CPM"
	}, {
		value : 1,
		text : "CPC"
	} ];

	var stateList = [ {
		value : 'NY',
		text : "New York"
	}, {
		value : "NJ",
		text : "New Jersey"
	} ];

	var targetDevice = [ {} ];

	factory.getCampaignStatus = function() {
		return status;
	};

	factory.getCampaignType = function() {
		return campaignType;
	};

	factory.getStateList = function() {
		return stateList;
	};
	
	factory.saveCampaign = function(campaignJSON){
		 return $http.get('/saveCampaign.lin?campaignJSON='+campaignJSON).then(function(result) {
                    //resolve the promise as the data
                    return result.data;
                });
	};
	
	factory.getPlateformList = function(){
		 return $http.get('/plateFormObj.lin').then(function(result) {
             //resolve the promise as the data
             return result.data;
         });
	};
	
	factory.getDeviceList = function(){
		 return $http.get('/deviceObj.lin').then(function(result) {
            //resolve the promise as the data
            return result.data;
        });
	};
	factory.getCreativeList = function(format){
		 return $http.get('/creativeObj.lin?format='+format).then(function(result) {
           //resolve the promise as the data
           return result.data;
       });
	};
	
	factory.getCampaignList = function(campaignList){
		 return $http.get('/allCampaigns.lin').then(function(result) {
          //resolve the promise as the data
          return result.data;
      });
	};
	return factory;
	
	
});

// Controller

oneAudianceApp.controller('audienceController', function($scope, $location,
		$modal, audianceFactory) {

	$scope.campaignStatus = audianceFactory.getCampaignStatus();
	$scope.campaignType = audianceFactory.getCampaignType();
	$scope.stateList = audianceFactory.getStateList();
	$scope.flightRowCount = 1;
	$scope.isFirstTime = true;
	$scope.plateFormList = audianceFactory.getPlateformList().then(function(plateformList) {
		$scope.plateFormList = plateformList;
	});
	
	$scope.deviceList = audianceFactory.getDeviceList().then(function(deviceList) {
		$scope.deviceList = deviceList;
	});
	
	$scope.standardCreativeList = audianceFactory.getCreativeList("standard").then(function(creativeList) {
		$scope.standardCreativeList = creativeList;
	});
	
	$scope.richCreativeList = audianceFactory.getCreativeList("rich").then(function(creativeList) {
		$scope.richCreativeList = creativeList;
	});
	
	$scope.videoCreativeList = audianceFactory.getCreativeList("video").then(function(creativeList) {
		$scope.videoCreativeList = creativeList;
	});
	
	$scope.show = function() {
		console.log($scope.campaign);
	};
	
	$scope.saveCampaign = function() {
		try{
			console.log(flightIncrement);
		
			$scope.campaign.flights = [];
			$scope.campaign.duration.start_date = $("#flightDateSlider_" + 1).dateRangeSlider("max");
			$scope.campaign.duration.end_date = $("#flightDateSlider_" + 1).dateRangeSlider("min");
			for(var i = 1;i<=flightIncrement;i++){
				var dateSliderMax = "";
			    var dateSliderMin = "";
			    var goalImp = "";
				var innerFlight = {
						date : {
							start : new Date(),
							end : new Date()
						},
						time : {
							start : "",
							end :""
						},
					
						impression : 0
				};
				 goalImp = $("#goalImp_"+i).val();
				 dateSliderMax = $("#flightDateSlider_" + i).dateRangeSlider("max");
				 dateSliderMin = $("#flightDateSlider_" + i).dateRangeSlider("min");
				 innerFlight.date.start = dateSliderMin;
				 innerFlight.date.end = dateSliderMax;
				 innerFlight.impression = goalImp;
				 $scope.campaign.flights.push(innerFlight);
			}
			$scope.campaign.creative=[];
			$scope.campaign.creative.push(standardCreative);
			$scope.campaign.creative.push(richCreative);
			$scope.campaign.creative.push(videoCreative);
			console.log($scope.campaign);
			console.log(JSON.stringify($scope.campaign));
			
			$scope.isSave=audianceFactory.saveCampaign(JSON.stringify($scope.campaign)).then(function(status) {
				
			});
		}catch(error){
			console.log(error);
		}
		
	};

	$scope.go = function(path) {
		$location.path(path);
	};

	$scope.reset = function() {
		$scope.campaign = null;
		$scope.campaign = angular.copy(Campaign);
	};

	$scope.addFlight = function() {
		var row = $("#dynaFlightRow").clone();

		//$scope.flightRowCount++;
		flightIncrement++;
		row.attr("id", "dynaFlightRow_" + flightIncrement);
		row.show();
		row.find(".fRow").html(
				'<div id="flightDateSlider_' + flightIncrement
						+ '"></div>');
		row.find(".gRow").attr("id","goalImp_"+flightIncrement);
		var delbutton = row.find("button");
		delbutton.attr("onclick", "deleteDynaRow(" + flightIncrement
				+ ")");

		$("#flightContainer").append(row);
		$("#flightDateSlider_" + flightIncrement).dateRangeSlider({
		    bounds: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)},
		    defaultValues: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)}});

	};
	
	$scope.loadCampaignData = function() {
		try {
			$scope.campaignList = audianceFactory.getCampaignList().then(function(campaignData) {
	    		if(campaignData == undefined || campaignData == null || (JSON.stringify(campaignData)) == '[]') {
	    			console.log("No Data");
	    		}
	    		else {
	    			$scope.campaignList = campaignData;
	    		}
			});
		}
		catch(err){
	    	console.log("audianceFactory err : "+err);
	    }
	};
	
	$scope.updateCampaign = function(campaignId){
		//campaignIdEdit = campaignId;
		//$scope.go('/editCampaign');
		for(var i=0;i<$scope.campaignList.length;i++){
			var tempCampaign = "";
			tempCampaign = $scope.campaignList[i];
			if(tempCampaign.id==campaignId){
		    	campaignIdEdit = tempCampaign;
		    	$scope.go('/editCampaign');
		    	
			}
		}
	};
	
	// Initialize Method
	$scope.init = function() {
		$scope.loadCampaignData();
		
		if(campaignIdEdit!=undefined && campaignIdEdit!=""){
			$scope.campaign = campaignIdEdit;
		}
		if ($scope.campaign) {
			// Already Created
		} else {
			$scope.campaign = angular.copy(Campaign);
		}
		
		$scope.setDateRange();
		$scope.tabInit();
		
		$('.splchk').checkbox();
	};

	$scope.setDateRange = function() {
		//$("#dateSlider").dateRangeSlider();
		$('#campaignStartDate').datetimepicker({
            pickTime: false
        });
		$('#campaignEndDate').datetimepicker({
            pickTime: false
        });
	};

	/*$scope.tabInit = function() {
		$("#tabs").tabs();
		$("#addresstabs").tabs();
		
		$("#tabs").on("tabsactivate", function(event, ui) {
			if (ui.newPanel.selector == "#tabs-3") {
				$("#flightDateInitSlider").dateRangeSlider({
					enabled : false
				});
				$("#flightDateSlider_1").dateRangeSlider();
                //$("#flightDateSlider_1").dateRangeSlider("bounds", new Date(2014,11,31), new Date().getMonth()+12);
                $("#flightDateSlider_1").dateRangeSlider("bounds", new Date(2012, 0, 1), new Date(2012, 3, 30));
				$("#flightDateSlider_1").dateRangeSlider({
					  range:{
					    min: {days: 1},
					    max: {years: 1}
					  }});
				$("#flightDateSlider_1").dateRangeSlider(
						  "option",
						  "bounds",
						  {
						    min: new Date(),
						    max: new Date().getMonth()+12  
						});
				//$("#flightDateSlider_1").dateRangeSlider("values", new Date(), new Date().getMonth()+1);
				//$("#flightDateSlider_1").dateRangeSlider("min", new Date());
				//$("#flightDateSlider_1").dateRangeSlider("max", new Date().getMonth()+12);
			}
		});
	};*/
	
	$scope.tabInit = function() {
		$("#tabs").tabs();
		$("#addresstabs").tabs();
      
		$("#tabs").on("tabsactivate",function(event, ui) {
					if (ui.newPanel.selector == "#tabs-3") {
						$scope.startdateValue = $("#campaignStartDate").data('date');
						$scope.enddateValue = $("#campaignEndDate").data('date');

						if($scope.isFirstTime){
							$("#flightDateInitSlider").dateRangeSlider({
							    bounds: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)},
							    defaultValues: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)},
							    range: { min: {days: 1}, max: {years: 1}},
							    arrows : false
							});
							
							$("#flightDateInitSlider").dateRangeSlider("disable");
							
							
							$("#flightDateSlider_1").dateRangeSlider({
							    bounds: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)},
							    range: { min: {days: 1}, max: {years: 1}},
							    defaultValues: {min: new Date($scope.startdateValue), max: new Date($scope.enddateValue)}});
							    $scope.isFirstTime = false;
						}else{
							
							$scope.resetCampaignDates();
						}
						
					}
				});
	};

	$scope.init();
	

	$scope.$watch('deviceList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
		$scope.campaign.target.device = nv.map(function(device) {
	return device;
	});
		}
	}, true);
	
	$scope.$watch('plateFormList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
		$scope.campaign.target.plateform = nv.map(function(plateform) {
	return plateform;
	});
		}
	}, true);
	
	$scope.$watch('standardCreativeList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
		standardCreative = nv.map(function(standardCreative) {
	return standardCreative;
	});
		}
	}, true);
	
	$scope.$watch('richCreativeList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
		richCreative = nv.map(function(richCreative) {
	return richCreative;
	});
		}
	}, true);
	
	$scope.$watch('videoCreativeList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
		videoCreative = nv.map(function(videoCreative) {
	return videoCreative;
	});
		}
	}, true);
	
	$scope.resetCampaignDates = function(){
		$("#flightDateInitSlider").dateRangeSlider("bounds", new Date($scope.startdateValue), new Date($scope.enddateValue));
	    $("#flightDateInitSlider").dateRangeSlider("values", new Date($scope.startdateValue), new Date($scope.enddateValue));
	    
	    $("#flightDateSlider_1").dateRangeSlider("bounds", new Date($scope.startdateValue), new Date($scope.enddateValue));
	    $("#flightDateSlider_1").dateRangeSlider("values", new Date($scope.startdateValue), new Date($scope.enddateValue));
	    
	    // also add code to delete all newly created rows
	
	};
});

function deleteDynaRow(rowID) {
	//var scope_deletedRow = angular.element(document.getElementById("#flightContainer")).scope();
	//console.log(scope_deletedRow);
	//scope_deletedRow.decrement();
	$("#dynaFlightRow_" + rowID).remove();
	flightIncrement--;
}


