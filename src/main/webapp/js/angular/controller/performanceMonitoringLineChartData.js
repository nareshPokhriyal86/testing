'use strict';

google.load("visualization", "1", {packages:["corechart"]});

var isRichMedia = true;
var isVideoCampaign = true;
var emptyHeaderData = {'rate':'','duration':'','goal':'','delivered':'','clicks':'','pending':'','ctr':'','deliveryPercentage':'','budget':'','spent':'','left':'','pendingLabel':'Pending','rateLabel':'Rate','goalLabel':'Goal','budgetLeftLabel':'Left'};
var flightIndex = 0;
var performanceViewChartApp = angular.module('performanceViewChartApp', ['googlechart']);

//Added by Anup for Dynamic Title of Text while selecting option from Creative/OS/Device CTR dropdown
var kpiValue = "";

//factory
performanceViewChartApp.factory('performanceViewChartDataService', function($http) {
		return {
			loadDeliveryMetricsData: function(scope) {
			   return $http.get('/performanceDeliveryMetricsData.lin?orderId='+scope.orderInfo.orderId+'&placementId='+scope.selectedPlacementIds+
					   '&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
            	  console.log("DeliveryMetricsData");
                   return result.data;
              });
	       },
		   loadHeaderData: function(scope) {
	    	   return $http.get('/performanceMonitoringHeaderData.lin?orderId='+scope.orderInfo.orderId+'&placementId='+scope.selectedPlacementIds+
					   '&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
            	  console.log("headerData");
             	  console.log(result.data);
                  return result.data;
               });
	       },
		   loadCtrData: function(scope) {
			   return $http.get('/performanceCTRLineChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("ctrData");
             	  console.log(result.data);
                  return result.data;
               });
	       },
	       loadClicksData: function(scope) {
	    	   return $http.get('/performanceClicksLineChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("ClicksData");
             	  console.log(result.data);
                  return result.data;
               });
	       },
	       loadImpressionsData: function(scope) {
	    	   return $http.get('/performanceImpressionsLineChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("impressionsData");
             	  console.log(result.data);
                  return result.data;
               });
	       },
	       loadLocationTargetedData: function(scope) {
	    	   return $http.get('/performanceLocationTargetData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("locationTargetData");
             	  console.log(result.data);
             	  return result.data;
               });
	       },
	       loadLocationCompleteData: function(scope) {
	    	   return $http.get('/performanceLocationCompleteData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("locationCompleteData");
             	  console.log(result.data);
             	  return result.data;
               });
	       },
	       loadLocationTopCitiesData: function(scope) {
	    	   return $http.get('/performanceLocationTopCitiesData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
               .then(function(result) {
             	  console.log("locationTopCitiesData");
             	  console.log(result.data);
             	  return result.data;
               });
	       },
		   loadFlightData: function(scope) {
			   return $http.get('/performanceFlightsLineChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("flightData");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadRichMediaData: function(scope) {
	    	   return $http.get('/performanceRichMediaLineChartData.lin?orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("richMedia Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadRichMediaDonutData: function(scope) {
	    	   return $http.get('/performanceRichMediaDonutChartData.lin?orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("richMedia Donut chart Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadCreativeData: function(scope) {
			   return $http.get('/performanceCreativeBarChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("Creative Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadDeviceData: function(scope) {
			   return $http.get('/performanceDeviceBarChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("Device Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadOSData : function(scope){
	    	   return $http.get('/performanceOSChartData.lin?isNoise='+isNoise+'&threshold='+threshold+'&orderId='+scope.orderInfo.orderId+
					   '&placementId='+scope.selectedPlacementIds+'&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("OS Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       },
	       loadVideoData: function(scope) {
	    	   return $http.get('/performanceVideoData.lin?orderId='+scope.orderInfo.orderId+'&placementId='+scope.selectedPlacementIds+
					   '&campaignId='+scope.orderInfo.campaignId+'&publisherIdInBQ='+publisherIdInBQ)
			   .then(function(result) {
				  console.log("Video Data");
            	  console.log(result.data);
            	  return result.data;
              });
	       }
	       
		};
});

// controller
performanceViewChartApp.controller("performanceViewChartCtrl", function ($scope,performanceViewChartDataService,$rootScope, $timeout) {
 	var ctrChartData,impChartData,clickChartData,impressionDonutChartData,clickDonutChartData;   
 	var averageSeries = 1;
 	var myObj = {};
	if($scope.headerData == undefined) {
		$scope.headerData = emptyHeaderData;
	}
	if($scope.finalValue == undefined) {
		$scope.finalValue = {target:'--',current:'--',revised:'--'};
	}
	if($scope.locationCard == undefined) {
		$scope.locationCard = {topCities : [{cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}], withInGeo:{impressionPercentage:'--', impressions:'--'}, outsideGeo:{impressionPercentage:'--', impressions:'--'}};
	}
	if($scope.targetValue == undefined) {
		$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--',campaignType:'--'};
	}
	if($scope.selectedFlight == undefined) {
		$scope.selectedFlight = {goal:0,delivered:0,goalLabel:'Goal'};
	}
	if($scope.videoCardData = undefined) {
		$scope.videoCardData = {averageInteractionRate:'--',averageViewRate:'--',averageCompletionRate:'--',averageViewTime:'--',start:'--',pause:'--',rewind:'--',resume:'--',mute:'--',unMute:'--',fullScreen:'--',firstQuartile:'--',midPoint:'--',thirdQuartile:'--',complete:'--',averageViewTimeUnit:'',rmInteractionTime:'--',rmInteractionCount:'--',rmInteractionRate:'--',rmAvgInteractionTime:'--',rmInteractionImpressions:'--',rmAverageDisplayTime:'--'};
	}
	if($scope.archivedFlightData == undefined) {
		$scope.archivedFlightData = {};
	}
	if($scope.orderInfo == undefined) {
		$scope.orderInfo = {};
	}
	if($scope.partnerInfo == undefined) {
		$scope.partnerInfo = {};
	}
	if($scope.placementInfo == undefined) {
		$scope.placementInfo = {};
	}
	if($scope.lineItemPlacementIds == undefined) {
		$scope.lineItemPlacementIds = {};
	}
	if($scope.lineItemPlacementName == undefined) {
		$scope.lineItemPlacementName = {};
	}
	if($scope.selectedPlacements == undefined) {
		$scope.selectedPlacements = [];
	}
	if($scope.selectedPlacementIds == undefined) {
		$scope.selectedPlacementIds = '';
	}
	if($scope.totalExitButtonJson == undefined) {
		$scope.totalExitButtonJson = '';
	}
	if($scope.totalCallButtonJson == undefined) {
		$scope.totalCallButtonJson = '';
	}
	flightIndex = 0;
	
	$scope.resetDurationButton = function(chart) {
		$('#'+chart+'durationWiseData div').css('background','#F2F2F2');
		$('#'+chart+'durationWiseData a').css('color','black');
		$('#'+chart+'dayDataChart div').css('background','#418BCA');
		$('#'+chart+'dayDataChart').css('color','white');
	};
	
	$scope.initTabClick = function(chartName) {
		kpiValue = "";
		$('#tab_ul div').attr('class','p_m_tabIconInactive');
		$('[name="tabChartData"]').hide();
		if(chartName == ctrChartName || chartName == impressionsChartName || chartName == clicksChartName || chartName == flightChartName) {
			$('#perfChartNoDataInnerDiv').html('<h2>No Data</h2>');
			$('#selectFilterDiv').css('visibility','hidden');
			$('#flightDeliveredInfo').css('visibility','hidden');
			$('#flightGoalInfo').css('visibility','hidden');
			$('#revisedPacing').css('visibility','visible');
			$scope.selectedFlight = {goal:0,delivered:0,goalLabel:'Goal'};
			$scope.finalValue = {target:'--',current:'--',revised:'--'};
			$('#ctrDataDiv').show();
		}
		
		if(chartName == ctrChartName) {
			$('#ctrTabDiv').attr('class','p_m_tabIconActive');
			$('#chartTitleDiv').html('CTR');
			$('#chartInfoDiv').html('CTR Details');
			$('#revisedPacing').css('visibility','hidden');
		}
		else if(chartName == impressionsChartName) {
			$('#impressionTabDiv').attr('class','p_m_tabIconActive');
			$('#chartTitleDiv').html('IMPRESSION');
			$('#chartInfoDiv').html('Impression Details');
		}
		else if(chartName == clicksChartName) {
			$('#clicksTabDiv').attr('class','p_m_tabIconActive');
			$('#chartTitleDiv').html('CLICKS');
			$('#chartInfoDiv').html('Clicks Details');
		}
		else if(chartName == flightChartName) {
			$('#flightTabDiv').attr('class','p_m_tabIconActive');
			$('#chartTitleDiv').html('FLIGHT PACING');
			$('#chartInfoDiv').html('Flight Pacing Details');
			$('#flightDeliveredInfo').css('visibility','visible');
			$('#flightGoalInfo').css('visibility','visible');
			$('#selectFilterDiv').css('visibility','visible');
		}
		else if(chartName == locationChartName) {
			$('#locationTabDiv').attr('class','p_m_tabIconActive');
			$('#locationDataDiv').show();
			$scope.locationCard = {topCities : [{cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}], withInGeo:{impressionPercentage:'--', impressions:'--'}, outsideGeo:{impressionPercentage:'--', impressions:'--'}};
		}
		else if(chartName == creativeChartName) {
			$('#creativeTabDiv').attr('class','p_m_tabIconActive');
			$('#creativeDataDiv').show();
			$("#barChartTitleDiv").html("CREATIVES");
		    $("#barChartInfoDiv").html("Details According to Creatives");
		    $("#selectBoxLabel").html("Performance By Creatives");
		    $("#targetedValueDiv").html("TARGETED SIZE");
		    $("#nontargetedValueDiv").html("NON TARGETED SIZE");
			$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--'};
			kpiValue = "Creative";
			//$("#chartType").select2('val',"CTR");
			$("#chartType").val('CTR');
		}
		else if(chartName == osChartName){
			$('#osTabDiv').attr('class','p_m_tabIconActive');
			$('#creativeDataDiv').show();
			 $("#barChartTitleDiv").html("OPERATING SYSTEM");
			 $("#barChartInfoDiv").html("Details According to Operating System");
			 $("#selectBoxLabel").html("Performance By Operating System");
			 $("#targetedValueDiv").html("TARGETED OS");
			 $("#nontargetedValueDiv").html("NON TARGETED OS");
			$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--'};
			kpiValue = "Operating System";
		}
		else if(chartName == deviceChartName) {
			$('#deviceTabDiv').attr('class','p_m_tabIconActive');
			$('#creativeDataDiv').show();
			$("#barChartTitleDiv").html("DEVICES");
		    $("#barChartInfoDiv").html("Details According to Devices");
		    $("#selectBoxLabel").html("Performance By Devices");
		    $("#targetedValueDiv").html("TARGETED DEVICES");
		    $("#nontargetedValueDiv").html("NON TARGETED DEVICES");
			$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--'};
			kpiValue = "Device";
		}
		else if(chartName == richMediaChartName ) {
			$('#richMediaTabDiv').attr('class','p_m_tabIconActive');
			$('#richMediaDataDiv').show();
		}
		else if(chartName == videoChartName ) {
			$scope.videoCardData = {averageInteractionRate:'--',averageViewRate:'--',averageCompletionRate:'--',averageViewTime:'--',start:'--',pause:'--',rewind:'--',resume:'--',mute:'--',unMute:'--',fullScreen:'--',firstQuartile:'--',midPoint:'--',thirdQuartile:'--',complete:'--',averageViewTimeUnit:'',rmInteractionTime:'--',rmInteractionCount:'--',rmInteractionRate:'--',rmAvgInteractionTime:'--',rmInteractionImpressions:'--',rmAverageDisplayTime:'--'};
			$('#videoTabDiv').attr('class','p_m_tabIconActive');
			$('#videoDataDiv').show();
		}
		
		$scope.resetDurationButton('');
		$scope.resetDurationButton('richMedia');
		selectedTab = chartName;
	};
	
	$scope.startLoading = function(chartName) {
		if(selectedTab == chartName) {
			$("#perfChartDiv").hide();
			$("#perfChartLoaderId").show();
		    $("#perfChartNoDataId").hide();
		}
	};
	
	$scope.stopLoading = function(chartName) {
		console.log("No "+chartName+" Data");
		if(selectedTab == chartName) {
			$("#perfChartDiv").hide();
			$("#perfChartLoaderId").hide();
		    $("#perfChartNoDataId").show();
		}
	};
	
	$scope.startLoadingCreative = function(chartName) {
		if(selectedTab == chartName) {
			$("#lineDiv").hide();
			$("#barChartDiv").hide();
			$("#donutChartDiv").hide();
			$("#donut2ChartDiv").hide();
			$("#barChartLoaderId").show();
		    $("#barChartNoDataId").hide();
		    $("#donutChartLoaderId").show();
		    $("#donutChartNoDataId").hide();
		    $("#donut2ChartLoaderId").show();
		    $("#donut2ChartNoDataId").hide();
		    $("#donutChartlagendDiv").hide();
		    $("#donut2ChartlagendDiv").hide();
		}
	};
	
	$scope.stopLoadingCreative = function(chartName) {
		console.log("No "+chartName+" Data");
		if(selectedTab == chartName) {
			$("#barChartLoaderId").hide();
		    $("#barChartNoDataId").show();
		    $("#donutChartLoaderId").hide();
		    $("#donutChartNoDataId").show();
		    $("#donut2ChartLoaderId").hide();
		    $("#donut2ChartNoDataId").show();
		}
	};
	
	$scope.durationWiseChart = function(duration, chart) {
		$('#'+chart+'durationWiseData div').css('background','#F2F2F2');
		$('#'+chart+'durationWiseData a').css('color','black');
		$('#'+chart+duration+'DataChart div').css('background','#418BCA');
		$('#'+chart+duration+'DataChart').css('color','white');
		if(selectedTab == ctrChartName) {
			$scope.drawPerformanceLineChart("CTR %", $scope.archivedCtrData[duration]);
		}
		else if(selectedTab == impressionsChartName) {
			$scope.drawPerformanceLineChart("Impressions", $scope.archivedImpressionsData[duration]);
		}
		else if(selectedTab == clicksChartName) {
			$scope.drawPerformanceLineChart("Clicks", $scope.archivedClicksData[duration]);
		}
		else if(selectedTab == flightChartName) {
			$scope.drawPerformanceLineChart("Impressions", $scope.selectedFlight[duration]);
		}
		else if(selectedTab == richMediaChartName) {
			$scope.drawPerformanceLineChart($scope.selectedCustomEvent.eventName+" Count", $scope.selectedCustomEvent[duration]);
		}
	};
	
	$scope.setAllLineItems = function() {
		if($scope.placementInfo == undefined || $scope.placementInfo == null || (JSON.stringify($scope.placementInfo)) == '{}' || (JSON.stringify($scope.placementInfo)) == '[]') {
			console.log('No placement info');
		}
		else {
			$scope.selectedPlacements.push($scope.placementInfo[0]);
			$scope.selectedPlacementIds = $scope.placementInfo[0].placementId;
			$scope.loadAllData(true);
		}
	};
	
	/*$scope.placementChanged = function() {
		$scope.selectedPlacement = [];
		var placementIdArr = $('#placementSelect').val();
		if(placementIdArr != null && placementIdArr != undefined && placementIdArr.length > 0) {
			for(var index in placementIdArr) {
				$scope.selectedPlacement.push($scope.placementInfo[index]);
			}
		}
		if(!setFilters()) {
			return;
		}
		$scope.loadAllData(true);
	};*/
	
	$scope.flightChanged = function() {
		$scope.resetDurationButton('');
		$scope.getFlightData(false);
	};
	
	$scope.richMediaEventChanged = function() {
		$scope.resetDurationButton('richMedia');
		$scope.getRichMediaData(false);
	};
	
	$scope.loadAllData = function(isPlacementChanged) {
		$scope.resetDurationButton('');
		$scope.resetDurationButton('richMedia');
		
		$scope.finalValue = {target:'--',current:'--',revised:'--'};
		$scope.locationCard = {topCities : [{cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}, {cityName:'', ctr:''}], withInGeo:{impressionPercentage:'--', impressions:'--'}, outsideGeo:{impressionPercentage:'--', impressions:'--'}};
		$scope.selectedFlight = {goal:0,delivered:0,goalLabel:'Goal'};
		$('#perfChartNoDataInnerDiv').html('<h2>No Data</h2>');
		$scope.archivedCtrData = {};
		$scope.archivedImpressionsData = {};
		$scope.archivedClicksData = {};
		$scope.archivedLocationData = {};
		$scope.archivedFlightData = {};
		$scope.archivedCreativeData = {};
		$scope.archivedDeviceData = {};
		$scope.archivedOSData = {};
		$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--',campaignType:'--'};
		$scope.targetValue.targetCreative = '--';
		ctrChartData="";
		impChartData="";
		clickChartData="";
		impressionDonutChartData = "";
		clickDonutChartData = ""; 
		flightIndex = 0;
		$("#chartType").val('CTR');
		$scope.firstCall(selectedTab);//added for production support bug
		if(isPlacementChanged) {
			$scope.headerData = emptyHeaderData;
			$scope.archivedHeaderData = {};
			$scope.getHeaderData();
			if(selectedTab != ctrChartName && selectedTab != impressionsChartName && selectedTab != clicksChartName && selectedTab != flightChartName) {
				setTimeout(function(){$scope.getDeliveryMetricsData();},1000);
			}
		}
		if(selectedTab != ctrChartName) {setTimeout(function(){$scope.getCtrData(false);},2000);}
		if(selectedTab != impressionsChartName) {setTimeout(function(){$scope.getImpressionsData(false);},3000);}
		if(selectedTab != clicksChartName) {setTimeout(function(){$scope.getClicksData(false);},4000);}
		if(selectedTab != flightChartName) {setTimeout(function(){$scope.getFlightData(false);},5000);}
		if(selectedTab != creativeChartName) {setTimeout(function(){$scope.getCreativeData(false);},6000);}
		if(selectedTab != osChartName) {setTimeout(function(){$scope.getOSData(false);},7000);}
		if(selectedTab != locationChartName) {setTimeout(function(){$scope.getLocationData(false);},8000);}
		if(selectedTab != deviceChartName) {setTimeout(function(){$scope.getDeviceData(false);},9000);}
		if(isPlacementChanged) {
			$scope.archivedRichMediaData = {};
			if(selectedTab != richMediaChartName) {setTimeout(function(){$scope.getRichMediaData(false);},10000);}
			$scope.archivedVideoData = {};
			$scope.videoCardData = {averageInteractionRate:'--',averageViewRate:'--',averageCompletionRate:'--',averageViewTime:'--',start:'--',pause:'--',rewind:'--',resume:'--',mute:'--',unMute:'--',fullScreen:'--',firstQuartile:'--',midPoint:'--',thirdQuartile:'--',complete:'--',averageViewTimeUnit:'',rmInteractionTime:'--',rmInteractionCount:'--',rmInteractionRate:'--',rmAvgInteractionTime:'--',rmInteractionImpressions:'--',rmAverageDisplayTime:'--'};
			if(selectedTab != videoChartName) {setTimeout(function(){$scope.getVideoData(false);},11000);}
		}
	};
	//Added  for production support bug
	$scope.firstCall=function(selectedTab){
		switch(selectedTab){
		case ctrChartName:
			$scope.getCtrData();
			setTimeout(function(){$scope.getDeliveryMetricsData();},1000);
			break;
		case impressionsChartName:
			$scope.getImpressionsData();
			setTimeout(function(){$scope.getDeliveryMetricsData();},1000);
			break;
		case clicksChartName:
			$scope.getClicksData();
			setTimeout(function(){$scope.getDeliveryMetricsData();},1000);
			break;
		case locationChartName:
			$scope.getLocationData();
			break;
		case flightChartName:
			$scope.getFlightData();
			setTimeout(function(){$scope.getDeliveryMetricsData();},1000);
			break;
		case creativeChartName:
			$scope.getCreativeData();
			break;
		case deviceChartName:
			$scope.getDeviceData();
			break;
		case osChartName:
			$scope.getOSData();
			break;
		case richMediaChartName:
			$scope.getRichMediaData();
			break;
		case videoChartName:
			$scope.getVideoData();
			break;
		default:
			console.log("No tab is selected, selectedTab value : "+selectedTab);
			break;
		}
		
	};
	$scope.getDeliveryMetricsData = function() {
		$scope.archivedDeliveryMetricsData = {};
		$('#deliveryMetricsLoader').show();
		jQuery('#deliveryMetrics').dataTable().fnClearTable();
		if($scope.archivedDeliveryMetricsData == undefined || $scope.archivedDeliveryMetricsData == null || (JSON.stringify($scope.archivedDeliveryMetricsData)) == '{}' || (JSON.stringify($scope.archivedDeliveryMetricsData)) == '[]') {
			performanceViewChartDataService.loadDeliveryMetricsData($scope).then(function(data) {
				$scope.archivedDeliveryMetricsData = data.deliveryMetricsData;
				$scope.fillDeliveryMetricsData();
			});
		}
		else {
			$scope.fillDeliveryMetricsData();
		}
	};
	$scope.fillDeliveryMetricsData = function() {
		if($scope.archivedDeliveryMetricsData == undefined || $scope.archivedDeliveryMetricsData == null || (JSON.stringify($scope.archivedDeliveryMetricsData)) == '{}' || (JSON.stringify($scope.archivedDeliveryMetricsData)) == '[]') {
			console.log("No DeliveryMetricsData Data");
		}
		else {
			$('#deliveryMetricsLoader').show();
			jQuery('#deliveryMetrics').dataTable().fnClearTable();
			jQuery('#deliveryMetrics').dataTable().fnSettings()._iDisplayLength = 20;
			jQuery('#deliveryMetrics').dataTable().fnDraw();
			
	   		  var key = 0;
	   		  var dataList = $scope.archivedDeliveryMetricsData;
	 		  for (var i = 0 ; i < dataList.length ; i = i + 1) {
	 			   (function(i) {
	 				   setTimeout( function(i) {
	 					  var dataObject = dataList[key];
	 					  jQuery('#deliveryMetrics').dataTable().fnAddData( [
				   		     dataObject.date,
				   		     dataObject.name,
					   		 dataObject.goal,
					   		dataObject.impressions,
				   			 dataObject.clicks,
				   		     dataObject.ctr,
					   		 dataObject.deliveryPercentage,
					   		 dataObject.target,
					   		 dataObject.current,
				   			 dataObject.revised
				   		]);
	 					key ++;
	 			     }, 10);
	 			  })(i);
	 		  }
	 		 $('#deliveryMetricsLoader').hide();
		}
	};
	
	$scope.getHeaderData = function() {
		if($scope.archivedHeaderData == undefined || $scope.archivedHeaderData == null || (JSON.stringify($scope.archivedHeaderData)) == '{}' || (JSON.stringify($scope.archivedHeaderData)) == '[]') {
			performanceViewChartDataService.loadHeaderData($scope).then(function(data) {
				$scope.archivedHeaderData = data.headerData;
				$scope.fillHeaderData();
			});
		}
		else {
			$scope.fillHeaderData();
		}
	};
	$scope.fillHeaderData = function() {
		if($scope.archivedHeaderData == undefined || $scope.archivedHeaderData == null || (JSON.stringify($scope.archivedHeaderData)) == '{}' || (JSON.stringify($scope.archivedHeaderData)) == '[]') {
			console.log("No header Data");
		}
		else {
			$scope.headerData = $scope.archivedHeaderData;
		}
	};
	
	$scope.getCtrData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(ctrChartName);
		}
		$scope.startLoading(ctrChartName);
		if($scope.archivedCtrData == undefined || $scope.archivedCtrData == null || (JSON.stringify($scope.archivedCtrData)) == '{}' || (JSON.stringify($scope.archivedCtrData)) == '[]') {
			performanceViewChartDataService.loadCtrData($scope).then(function(data) {
				$scope.archivedCtrData = data.ctrData;
				$scope.fillCtrData();
			});
		}
		else {
			$scope.fillCtrData();
		}
	};
	$scope.fillCtrData = function() {
		if($scope.archivedCtrData == undefined || $scope.archivedCtrData == null || (JSON.stringify($scope.archivedCtrData)) == '{}' || (JSON.stringify($scope.archivedCtrData)) == '[]') {
			$scope.stopLoading(ctrChartName);
		}
		else {
			if(selectedTab == ctrChartName) {
				var chartData = $scope.archivedCtrData['day'];
				$scope.finalValue.target = $scope.archivedCtrData['target'];
				$scope.finalValue.current = $scope.archivedCtrData['current'];
				$scope.finalValue.revised = $scope.archivedCtrData['revised'];
				$scope.drawPerformanceLineChart("CTR %", chartData);
			}
		}
	};
	
	$scope.getImpressionsData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(impressionsChartName);
		}
		$scope.startLoading(impressionsChartName);
		if($scope.archivedImpressionsData == undefined || $scope.archivedImpressionsData == null || (JSON.stringify($scope.archivedImpressionsData)) == '{}' || (JSON.stringify($scope.archivedImpressionsData)) == '[]') {
			performanceViewChartDataService.loadImpressionsData($scope).then(function(data) {
				$scope.archivedImpressionsData = data.impressionsData;
				$scope.fillImpressionsData();
			});
		}
		else {
			$scope.fillImpressionsData();
		}
	};
	$scope.fillImpressionsData = function() {
		if($scope.archivedImpressionsData == undefined || $scope.archivedImpressionsData == null || (JSON.stringify($scope.archivedImpressionsData)) == '{}' || (JSON.stringify($scope.archivedImpressionsData)) == '[]') {
			$scope.stopLoading(impressionsChartName);
		}
		else {
			if(selectedTab == impressionsChartName){
				var chartData = $scope.archivedImpressionsData['day'];
				$scope.finalValue.target = $scope.archivedImpressionsData['target'];
				$scope.finalValue.current = $scope.archivedImpressionsData['current'];
				$scope.finalValue.revised = $scope.archivedImpressionsData['revised'];
				$scope.drawPerformanceLineChart("Impressions", chartData);
			}
		}
	};
	
	$scope.getClicksData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(clicksChartName);
		}
		$scope.startLoading(clicksChartName);
		if($scope.archivedClicksData == undefined || $scope.archivedClicksData == null || (JSON.stringify($scope.archivedClicksData)) == '{}' || (JSON.stringify($scope.archivedClicksData)) == '[]') {
			performanceViewChartDataService.loadClicksData($scope).then(function(data) {
				$scope.archivedClicksData = data.clicksData;
				$scope.fillClicksData();
			});
		}
		else {
			$scope.fillClicksData();
		}
	};
	$scope.fillClicksData = function() {
		if($scope.archivedClicksData == undefined || $scope.archivedClicksData == null || (JSON.stringify($scope.archivedClicksData)) == '{}' || (JSON.stringify($scope.archivedClicksData)) == '[]') {
			$scope.stopLoading(clicksChartName);
		}
		else {
			if(selectedTab == clicksChartName) {
				var chartData = $scope.archivedClicksData['day'];
				$scope.finalValue.target = $scope.archivedClicksData['target'];
				$scope.finalValue.current = $scope.archivedClicksData['current'];
				$scope.finalValue.revised = $scope.archivedClicksData['revised'];
				$scope.drawPerformanceLineChart("Clicks", chartData);
			}
		}
	};
	
	$scope.getLocationData = function(isTabClick) {
		$("#locationChartDiv").hide();
		$("#locationChartLoaderId").show();
	    $("#locationChartNoDataId").hide();
	    try {
			if(isTabClick) {
				$scope.initTabClick(locationChartName);
			}
			if($scope.archivedLocationData == undefined || $scope.archivedLocationData == null || (JSON.stringify($scope.archivedLocationData)) == '{}' || (JSON.stringify($scope.archivedLocationData)) == '[]') {
				var responseCount = 0;
				var withInGeoImpressions = 0;
				var geoTargeted = "";
				var completeImpressions = 0;
				var topCities = [];
				var geoChart = {};
				performanceViewChartDataService.loadLocationTopCitiesData($scope).then(function(data) {
					topCities = data.topCities;
					responseCount++;
					if(responseCount == 3) {
						$scope.checkHeader(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart);
					}
				});
				performanceViewChartDataService.loadLocationTargetedData($scope).then(function(data) {	// GEO targeted data 
					withInGeoImpressions = data.targetImpressions;
					geoTargeted = data.geoTargeted;
					responseCount++;
					if(responseCount == 3) {
						$scope.checkHeader(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart);
					}
				});
				performanceViewChartDataService.loadLocationCompleteData($scope).then(function(data) {		// all data
					if(data.locationCompleteData != undefined) {
						geoChart = data.locationCompleteData.geoChart;
						completeImpressions = data.locationCompleteData.impressions;
						console.log(" performance by location completeImpressions : "+completeImpressions);
					}
					responseCount++;
					if(responseCount == 3) {
						$scope.checkHeader(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart);
					}
				});
			}
			else {
				$scope.fillLocationData();
			}
	    }catch(err){
	    	console.log("getLocationData : err: "+err);
	    }
	};
	
	var locationInterval;
	$scope.checkHeader = function(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart) {
		locationInterval = setInterval($scope.makeLocationArchiveData(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart), 500);
	};
	
	$scope.makeLocationArchiveData = function(withInGeoImpressions, completeImpressions, geoTargeted, topCities, geoChart) {
		console.log("makeLocationArchiveData : "+$scope.archivedHeaderData.delivered);
		if($scope.archivedHeaderData != undefined && $scope.archivedHeaderData != null && $scope.archivedHeaderData.delivered != undefined && $scope.archivedHeaderData.delivered != null && !isNaN($scope.archivedHeaderData.delivered)) {
			clearInterval(locationInterval);
			console.log("interval stopped : "+$scope.archivedHeaderData.delivered);
			completeImpressions = $scope.archivedHeaderData.delivered;
			if(withInGeoImpressions == undefined || isNaN(withInGeoImpressions)) {
				withInGeoImpressions = 0;
			}
			if(completeImpressions == undefined || isNaN(completeImpressions)) {
				completeImpressions = 0;
			}
			var outsideGeoImpressions = 0;
			var outsideGeoPercentage = 0;
			var withInGeoPercentage = 0;
			if(geoTargeted == "All") {
				withInGeoImpressions = completeImpressions;
			}
			else {
				outsideGeoImpressions = completeImpressions - withInGeoImpressions;
				if(outsideGeoImpressions < 0) {			// handle negative value for outsideGeoImpressions i.e GeoTarget impression is more than completeImpressions
					withInGeoImpressions = completeImpressions;
					outsideGeoImpressions = 0;
				}
			}
			
			if(completeImpressions > 0) {
				outsideGeoPercentage = (outsideGeoImpressions * 100)/completeImpressions;
				withInGeoPercentage = (withInGeoImpressions * 100)/completeImpressions;
			}
			var locationData = {};
			locationData.geoChart = geoChart;
			locationData.topCities = topCities;
			locationData.withInGeo = {};
			locationData.withInGeo.impressionPercentage = formatFloat(withInGeoPercentage, 2)+'%';
			locationData.withInGeo.impressions = formatInt(withInGeoImpressions)+' IMP';
			locationData.outsideGeo = {};
			locationData.outsideGeo.impressionPercentage = formatFloat(outsideGeoPercentage, 2)+'%';
			locationData.outsideGeo.impressions = formatInt(outsideGeoImpressions)+' IMP';
			
			$scope.archivedLocationData = locationData;
			$scope.fillLocationData();
		}
	};
	$scope.fillLocationData = function() {
		if($scope.archivedLocationData == undefined || $scope.archivedLocationData == null || (JSON.stringify($scope.archivedLocationData)) == '{}' || (JSON.stringify($scope.archivedLocationData)) == '[]') {
			console.log("No Location Data");
			$("#locationChartLoaderId").hide();
		    $("#locationChartNoDataId").show();
		}
		else {
			if(selectedTab == locationChartName) {
				var chartData = $scope.archivedLocationData.geoChart;
				$scope.locationCard.withInGeo = $scope.archivedLocationData.withInGeo;
				$scope.locationCard.outsideGeo = $scope.archivedLocationData.outsideGeo;
				$scope.locationCard.topCities = $scope.archivedLocationData.topCities;
				$scope.drawPerformanceGeoChart(chartData);
			}
		}
	};
	
	$scope.getFlightData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(flightChartName);
		}
		$('#perfChartNoDataInnerDiv').html('<h2>No Data</h2>');
		$scope.startLoading(flightChartName);
		if($scope.archivedFlightData == undefined || $scope.archivedFlightData == null || (JSON.stringify($scope.archivedFlightData)) == '{}' || (JSON.stringify($scope.archivedFlightData)) == '[]') {
			performanceViewChartDataService.loadFlightData($scope).then(function(data) {
				$scope.archivedFlightData = data.flightData;
				flightIndex = data.index;
				$scope.selectedFlight = {};
				$scope.fillFlightData();
			});
		}
		else {
			$scope.fillFlightData();
		}
	};
	$scope.fillFlightData = function() {
		if($scope.archivedFlightData == undefined || $scope.archivedFlightData == null || (JSON.stringify($scope.archivedFlightData)) == '[]' || $scope.archivedFlightData.length == 0) {
			$scope.stopLoading(flightChartName);
		}
		else {
			if(selectedTab == flightChartName) {
				if($scope.selectedFlight == undefined || $scope.selectedFlight == null || (JSON.stringify($scope.selectedFlight)) == '{}' || (JSON.stringify($scope.selectedFlight)) == '[]' || $scope.selectedFlight.target == undefined || $scope.selectedFlight.target == null) {
					$scope.selectedFlight = $scope.archivedFlightData[flightIndex];
				}
				if($scope.selectedFlight != undefined && $scope.selectedFlight != null && $scope.selectedFlight.flightStatus == "notStarted") {
					$('#perfChartNoDataInnerDiv').html('<h2>Flight not yet started</h2>');
				}
				var chartData = $scope.selectedFlight['day'];
				$scope.finalValue.target = $scope.selectedFlight['target'];
				$scope.finalValue.current = $scope.selectedFlight['current'];
				$scope.finalValue.revised = $scope.selectedFlight['revised'];
				$scope.drawPerformanceLineChart("Impressions", chartData);
			}
		}
	};
	
	$scope.getRichMediaData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(richMediaChartName);
		}
		if(!isRichMedia) {
			console.log("isRichMedia : "+isRichMedia);
			return false;
		}
		$("#richMediaChartNoDataId").hide();
		$("#richMediaChartDiv").hide();
		$('#selectRichMediaEventDiv').css('visibility','visible');
		$("#richMediaChartLoaderId").show();
		$("#richMediaChartShowDataId").show();
		$("#richMediaDonutsDivId").show();
		
		if($scope.archivedRichMediaData == undefined || $scope.archivedRichMediaData == null || (JSON.stringify($scope.archivedRichMediaData)) == '{}' || (JSON.stringify($scope.archivedRichMediaData)) == '[]') {
			var responseCount = 0;
			var richMediaLineChartData = null;
			var richMediaDonutData = null;
			performanceViewChartDataService.loadRichMediaData($scope).then(function(data) {
				$scope.selectedCustomEvent = {};
				richMediaLineChartData = data.richMediaData;
				responseCount++;
				if(responseCount == 2) {
					$scope.makeRichMediaData(richMediaLineChartData, richMediaDonutData);
				}
			});
			
			performanceViewChartDataService.loadRichMediaDonutData($scope).then(function(data) {
				richMediaDonutData = data;
				responseCount++;
				if(responseCount == 2) {
					$scope.makeRichMediaData(richMediaLineChartData, richMediaDonutData);
				}
			});
		}
		else {
			$scope.fillRichMediaData();
		}
	};
	
	$scope.makeRichMediaData = function(richMediaLineChartData, richMediaDonutData) {
		var richMediaData = {};
		if(richMediaDonutData != null && richMediaDonutData != undefined && (JSON.stringify(richMediaDonutData)) != '{}' && (JSON.stringify(richMediaDonutData)) != '[]') {
			richMediaData.donutData = richMediaDonutData;
		}
		if(richMediaLineChartData != null && richMediaLineChartData != undefined && (JSON.stringify(richMediaLineChartData)) != '{}' && (JSON.stringify(richMediaLineChartData)) != '[]') {
			richMediaData.lineChartData = richMediaLineChartData;
		}
		$scope.archivedRichMediaData = richMediaData;
		$scope.fillRichMediaData();
	};
	
	$scope.fillRichMediaData = function() {
		if($scope.archivedRichMediaData == undefined || $scope.archivedRichMediaData == null || (JSON.stringify($scope.archivedRichMediaData)) == '{}' || (JSON.stringify($scope.archivedRichMediaData)) == '[]') {
			isRichMedia = false;
			console.log("No Rich Media Data");
			$("#richMediaChartShowDataId").hide();
			$('#selectRichMediaEventDiv').css('visibility','hidden');
		    $("#richMediaChartNoDataId").show();
		    $("#richMediaDonutsDivId").hide();
		}
		else {
			if(selectedTab == richMediaChartName) {
				/*LineChart code Starts*/
				if($scope.selectedCustomEvent == undefined || $scope.selectedCustomEvent == null || (JSON.stringify($scope.selectedCustomEvent)) == '{}' || (JSON.stringify($scope.selectedCustomEvent)) == '[]' || $scope.selectedCustomEvent.day == undefined || $scope.selectedCustomEvent.day == null) {
					if($scope.archivedRichMediaData.lineChartData != null && $scope.archivedRichMediaData.lineChartData != undefined && (JSON.stringify($scope.archivedRichMediaData.lineChartData)) != '{}' && (JSON.stringify($scope.archivedRichMediaData.lineChartData)) != '[]') {
						$scope.selectedCustomEvent = $scope.archivedRichMediaData.lineChartData[0];
					}
				}
				if($scope.selectedCustomEvent.day == null || $scope.selectedCustomEvent.day == undefined) {
					$("#richMediaChartDiv").hide();
					$("#richMediaChartLoaderId").hide();
				}
				else {
					$("#richMediaChartDiv").show();
					$("#richMediaChartLoaderId").hide();
					$("#richMediaChartShowDataId").show();
					var chartData = $scope.selectedCustomEvent['day'];
					$scope.drawPerformanceLineChart($scope.selectedCustomEvent.eventName+" Count", chartData);
				}
				/*LineChart code Ends*/
				
				// make donut
				$scope.makeDynamicDonut();
			}
		}
	};
	
	$scope.makeDynamicDonut = function() {
		if($scope.archivedRichMediaData != undefined && $scope.archivedRichMediaData != {}) {
			$scope.richMediaDonutData = $scope.archivedRichMediaData.donutData;
			var donutCount = $scope.richMediaDonutData.length;
			var dynamicDonutInterval = setInterval(function() {
				var length = $('#richMediaDonutsDivId > article').length;
				console.log("donutCount : "+donutCount+", Donut Div Count : "+length);
				if(donutCount <= length) {
					clearInterval(dynamicDonutInterval);
					for(var i=0;i<donutCount;i++) {
						if($scope['richMediaDonut'+i] == undefined) {
							$scope['richMediaDonut'+i] = {};
						}
						$scope.drawDonutChart('richMediaDonutData'+i, $scope.richMediaDonutData[i].donutStr, $scope.richMediaDonutData[i].donutTitle, 'richMediaHeader'+i);
					}
				}
			}, 500);
		}
	};
	
	$scope.drawDonutChart = function(id, data, chartTitle, headerId) {
		$('#'+headerId).html(chartTitle);
		var options = {
				'chartType': 'PieChart',
                "isStacked": "true",
                "fill": 20,
                "pieHole": 0.5,
                'chartArea':{'width':'100%','height':'350px'},
                "legend": "none",
                slices : {
					0 : {
						color : 'red'
					},
					1 : {
						color : 'green'
					},
					2 : {
						color : 'orange'
					},
					3 : {
						color : 'blue'
					},
					4 : {
						color : 'yellow'
					}
				},
                "displayExactValues": true,
                "vAxis": {
                    "title": "Counts", "gridlines": {"count": 10}
                },
                "hAxis": {
                    "title": "Creative Size"
                }, "tooltip": {
        		      "isHtml": true,
        		      "trigger": "focus"
        	    }
                
            };
			google.setOnLoadCallback($scope.drawPieChart(id,options,data));
	};
	
	$scope.drawPieChart = function (divId,options,formatedData) { 
	    var data = google.visualization.arrayToDataTable(formatedData);
	    var chart = new google.visualization.PieChart(document.getElementById(divId));
	    chart.draw(data, options);
	};
	
	var doubleClickRichMediaInterval;
	$scope.getVideoData = function(isTabClick) {
		if(isTabClick) {
			$scope.initTabClick(videoChartName);
		}
		if(!isVideoCampaign) {
			console.log("isVideoCampaign : "+isVideoCampaign);
			$("#videoChartNoDataId").show();
			return false;
		}
		$("#videoChartNoDataId").hide();
		$("#videoChartLoaderId").show();
		$("#videoCreativesShowDataId").hide();
		$("#doubleClickRichMediaShowDataId").hide();
		if($scope.archivedVideoData == undefined || $scope.archivedVideoData == null || (JSON.stringify($scope.archivedVideoData)) == '{}' || (JSON.stringify($scope.archivedVideoData)) == '[]') {
			performanceViewChartDataService.loadVideoData($scope).then(function(data) {
				if(data.videoData != undefined && data.videoData.doubleClickRichMedia != undefined) {
					doubleClickRichMediaInterval = setInterval($scope.checkHeaderImpressionsForRichMedia(data.videoData), 500);
				}else {
					$scope.archivedVideoData = data.videoData;
					$scope.fillVideoData();
				}
			});
		}
		else {
			$scope.fillVideoData();
		}
	};
	$scope.fillVideoData = function() {
		$("#videoChartLoaderId").hide();
		if($scope.archivedVideoData == undefined || $scope.archivedVideoData == null || (JSON.stringify($scope.archivedVideoData)) == '{}' || (JSON.stringify($scope.archivedVideoData)) == '[]' || ($scope.archivedVideoData.videoCreativeSet == undefined && $scope.archivedVideoData.doubleClickRichMedia == undefined)) {
			isVideoCampaign = false;
			console.log("No Video Data");
		    $("#videoChartNoDataId").show();
		}
		else {
			if(selectedTab == videoChartName) {
				if($scope.archivedVideoData.videoCreativeSet != undefined) {									// if videoCreativeSet
					$("#videoCreativesShowDataId").show();
					$("#byRateChart").show();
					if($scope.archivedVideoData.videoCreativeSet.byRateChart != undefined) {
						var videoChart = {};
						videoChart.data = $scope.archivedVideoData.videoCreativeSet.byRateChart;
						videoChart.type = "ComboChart";
						videoChart.displayed = false;
						videoChart.cssStyle = "height:400px; width:100%;";
						videoChart.formatters = {};
						videoChart.options = {
			        	  		vAxis: {title: "Rate (%)"},
			        	  		hAxis: {title: "Events"},
			        	  		legend : 'none',
			        	  		seriesType: "bars"
			        	   };
			            $scope.videoChart = videoChart;
					}else {
						$("#byRateChart").hide();
						console.log("No Video Chart Data");
					}
					$scope.videoCardData = $scope.archivedVideoData.videoCreativeSet.videoCardJson;
				}
				if($scope.archivedVideoData.doubleClickRichMedia != undefined) {									// if doubleClickRichMedia
					$("#doubleClickRichMediaShowDataId").show();
					var data = $scope.archivedVideoData.doubleClickRichMedia;
					$scope.videoCardData.rmInteractionTime = data.rmInteractionTime;
					$scope.videoCardData.rmInteractionCount = data.rmInteractionCount;
					$scope.videoCardData.rmInteractionRate = data.rmInteractionRate;
					$scope.videoCardData.rmAvgInteractionTime = data.rmAvgInteractionTime;
					$scope.videoCardData.rmInteractionImpressions = data.rmInteractionImpressions;
					$scope.videoCardData.rmAverageDisplayTime = data.rmAverageDisplayTime;
				}
			}
		}
	};
	
	$scope.checkHeaderImpressionsForRichMedia = function(data) {
		if($scope.archivedHeaderData != undefined && $scope.archivedHeaderData != null && $scope.archivedHeaderData.delivered != undefined && $scope.archivedHeaderData.delivered != null && !isNaN($scope.archivedHeaderData.delivered)) {
			clearInterval(doubleClickRichMediaInterval);
			console.log("interval stopped : "+$scope.archivedHeaderData.delivered);
			var rmAverageDisplayTime = 0;
			var rmInteractionRate = 0;
			var totalImpressions = $scope.archivedHeaderData.delivered;
			var rmDisplayTime = data.doubleClickRichMedia.rmDisplayTime;
			var rmInteractionImpressions = data.doubleClickRichMedia.rmInteractionImpressions;
			if(totalImpressions > 0) {
				rmAverageDisplayTime = rmDisplayTime/totalImpressions;
				rmInteractionRate = (rmInteractionImpressions/totalImpressions)*100;
			}
			data.doubleClickRichMedia.rmInteractionRate = formatFloat(rmInteractionRate,2)+'%';
			data.doubleClickRichMedia.rmInteractionImpressions = formatInt(rmInteractionImpressions);
			data.doubleClickRichMedia.rmAverageDisplayTime = formatFloat(rmAverageDisplayTime,2);
			$scope.archivedVideoData = data;
			$scope.fillVideoData();
		}
	};
	
	$scope.dynamicDonut = function(index,donutData) {
		console.log(donutData);
		var donutData = donutData.donutJson;
        var pieChart = {};
        pieChart.type = "PieChart";
        pieChart.displayed = false;
        pieChart.cssStyle = "height:350px; width:100%;";
        var pieChart2 = {};
        pieChart2.type = "PieChart";
        pieChart2.displayed = false;
        pieChart2.cssStyle = "height:350px; width:100%;";
        var donutFormatter= {
    			"number": [
    						 {
    						   columnNum: 1,
    						   pattern:'###,###'			
    						}
    					]
    	};
        
        pieChart.options = {
                "title": "CALL BUTTON",
                "isStacked": "true",
                "fill": 20,
                "pieHole": 0.5,
                "legend": "none",
                slices : {
					0 : {
						color : 'red'
					},
					1 : {
						color : 'green'
					},
					2 : {
						color : 'orange'
					},
					3 : {
						color : 'blue'
					},
					4 : {
						color : 'yellow'
					}
				},
                "displayExactValues": true,
                "vAxis": {
                    "title": "Counts", "gridlines": {"count": 10}
                },
                "hAxis": {
                    "title": "Creative Size"
                }, "tooltip": {
        		      "isHtml": true,
        		      "trigger": "focus"
        	    }
                
            };
            pieChart.formatters =donutFormatter;
            pieChart.data = donutData;
           
            $scope['richMediaDonut'+index] = {};
            $scope['richMediaDonut'+index] = pieChart;
	};
	
	$scope.createRichMediaDonutChartData=function(completeDonutData){
	
			console.log("---createRichMediaDonutChartData-- "+completeDonutData);
	       	var jsonData=completeDonutData;
	       	
	       	$scope.callButtonLegend =  jsonData.CallButtonLegend;
	       	$scope.exitButtonLegend =  jsonData.ExitButtonLegend;
	       	
	       	
	       	$scope.donutCallButtonChartJson= jsonData.DonutCallButton;
	       	$scope.donutExitButtonChartJson= jsonData.DonutExitButton;
	       	$scope.totalCallButtonJson= jsonData.totalCallButton;
	       	$scope.totalExitButtonJson= jsonData.totalExitButton;
	      
	       	console.log("$scope.totalExitButtonJson:"+$scope.totalExitButtonJson);
	       	
	        var pieChart = {};
	        pieChart.type = "PieChart";
	        pieChart.displayed = false;
	        pieChart.cssStyle = "height:350px; width:100%;";
	        var pieChart2 = {};
	        pieChart2.type = "PieChart";
	        pieChart2.displayed = false;
	        pieChart2.cssStyle = "height:350px; width:100%;";
	        var donutFormatter= {
	    			"number": [
	    						 {
	    						   columnNum: 1,
	    						   pattern:'###,###'			
	    						}
	    					]
	    	};
	        
	        pieChart.options = {
	                "title": "CALL BUTTON",
	                "isStacked": "true",
	                "fill": 20,
	                "pieHole": 0.5,
	                "legend": "none",
	                slices : {
						0 : {
							color : 'red'
						},
						1 : {
							color : 'green'
						},
						2 : {
							color : 'orange'
						},
						3 : {
							color : 'blue'
						},
						4 : {
							color : 'yellow'
						}
					},
	                "displayExactValues": true,
	                "vAxis": {
	                    "title": "Counts", "gridlines": {"count": 10}
	                },
	                "hAxis": {
	                    "title": "Creative Size"
	                }, "tooltip": {
	        		      "isHtml": true,
	        		      "trigger": "focus"
	        	    }
	                
	            };
	            pieChart.formatters =donutFormatter;
	            pieChart.data = $scope.donutCallButtonChartJson;
	           
	            $scope.richMediaDonut1 = pieChart;
	            
	        
	            pieChart2.options = {
	                    "title": "MAIN EXIT",
	                    "isStacked": "true",
	                    "fill": 20,
	                    "pieHole": 0.5,
	                    "legend": "none",
	                    slices : {
	    					0 : {
	    						color : 'red'
	    					},
	    					1 : {
	    						color : 'green'
	    					},
	    					2 : {
	    						color : 'orange'
	    					},
	    					3 : {
	    						color : 'blue'
	    					},
	    					4 : {
	    						color : 'yellow'
	    					}
	    				},
	                    "displayExactValues": true,
	                    "vAxis": {
	                        "title": "Counts", "gridlines": {"count": 10}
	                    },
	                    "hAxis": {
	                        "title": "Creative Size"
	                    }, "tooltip": {
	            		      "isHtml": true,
	            		      "trigger": "focus"
	            	    }
	                    
	            };
	            pieChart2.formatters =donutFormatter;
	            pieChart2.data = $scope.donutExitButtonChartJson;
	           
	            $scope.richMediaDonut2 = pieChart2;
	            
		
		
		
	};
	
	
	$scope.getCreativeData = function(isTabClick) {
		try {
			
			if(isTabClick) {
				$scope.initTabClick(creativeChartName);
			}
			$scope.startLoadingCreative(creativeChartName);
		
			if($scope.archivedCreativeData == undefined || $scope.archivedCreativeData == null || (JSON.stringify($scope.archivedCreativeData)) == '{}' || (JSON.stringify($scope.archivedCreativeData)) == '[]') {
				performanceViewChartDataService.loadCreativeData($scope).then(function(data) {
					$scope.archivedCreativeData = data;
					$scope.fillCreativeData();
				});
			}
			else {
				$scope.fillCreativeData();
			}
		}catch(err){
	    	console.log("getCreativeData: err: "+err);
	    }
	};
	
   	var barChart = {};
    barChart.type = "ComboChart";
    barChart.displayed = false;
    barChart.cssStyle = "height:400px; width:100%;";
    barChart.formatters = {};
	$scope.fillCreativeData = function() {
		try {
			if($scope.archivedCreativeData == undefined || $scope.archivedCreativeData == null) {
				$scope.stopLoadingCreative(creativeChartName);
				
			}
			else {
					if(selectedTab == creativeChartName) {
		           	var ctrData=$scope.archivedCreativeData['CTR'];
		           	var impData=$scope.archivedCreativeData['Impressions'];
		           	var clickData=$scope.archivedCreativeData['Clicks'];
		           	var clickDonut = $scope.archivedCreativeData['DonutClick'];
		           	var impresionDonut = $scope.archivedCreativeData['DonutImpression'];
		           	$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--'};
		           
		           	if($scope.archivedCreativeData['TargetCreative']!=null && $scope.archivedCreativeData['TargetCreative']!=undefined && $scope.archivedCreativeData['TargetCreative']!=[]){
		        	   $scope.targetValue.targetCreative = "("+$scope.archivedCreativeData['TargetCreative']+")";
		           	}else{
		        	   $scope.targetValue.targetCreative = "--";
		           	}
		           	if($scope.archivedCreativeData['NonTargetCreative']!=null && $scope.archivedCreativeData['NonTargetCreative']!=undefined && $scope.archivedCreativeData['NonTargetCreative']!=[]){
		        	   $scope.targetValue.nonTargetCreative = "("+$scope.archivedCreativeData['NonTargetCreative']+")";
		           	}else{
		        	   $scope.targetValue.nonTargetCreative = "--";
		           	}
		           	
		          
		           	if($scope.archivedCreativeData['TargetGoal']!=null && $scope.archivedCreativeData['TargetGoal']!=undefined && $scope.archivedCreativeData['TargetGoal']!=[]){
		           		if($scope.orderInfo.campaignType=='CPM'){
		           			$scope.targetValue.targetGoal = formatInt($scope.archivedCreativeData['TargetGoal'])+' IMP';
		           		}else if($scope.orderInfo.campaignType=='CPC'){
		           			$scope.targetValue.targetGoal = formatInt($scope.archivedCreativeData['TargetGoal'])+' CLICK';
		           		}
		           		
		           	}else{
		           		$scope.targetValue.targetGoal="--";
		           	}
		        	if($scope.archivedCreativeData['NonTargetGoal']!=null && $scope.archivedCreativeData['NonTargetGoal']!=undefined && $scope.archivedCreativeData['NonTargetGoal']!=[]){
		        		
		        		if($scope.orderInfo.campaignType=='CPM'){
		        			$scope.targetValue.nonTargetGoal =formatInt($scope.archivedCreativeData['NonTargetGoal'])+' IMP';
		           		}else if($scope.orderInfo.campaignType=='CPC'){
		           			$scope.targetValue.nonTargetGoal =formatInt($scope.archivedCreativeData['NonTargetGoal'])+' CLICK';
		           		}
		        		
		           	}else{
		           		$scope.targetValue.targetGoal="--";
		           	}
		        	if($scope.archivedCreativeData['TargetPercentage']!=null && $scope.archivedCreativeData['TargetPercentage']!=undefined && $scope.archivedCreativeData['TargetPercentage']!=[]){
		        		$scope.targetValue.targetPercentage = formatFloat($scope.archivedCreativeData['TargetPercentage'], 2)+'%';
		           	}else{
		           		$scope.targetValue.targetPercentage="--";
		           	}
		        	if($scope.archivedCreativeData['NonTargetPercentage']!=null && $scope.archivedCreativeData['NonTargetPercentage']!=undefined && $scope.archivedCreativeData['NonTargetPercentage']!=[]){
		        		$scope.targetValue.nonTargetPercentage = formatFloat($scope.archivedCreativeData['NonTargetPercentage'], 2)+'%';
		           	}else{
		           		$scope.targetValue.nonTargetPercentage="--";
		           	}
		        	
		           	$scope.targetValue.campaignType = $scope.orderInfo.campaignType;
		           	$scope.impByCreative =  JSON.parse($scope.archivedCreativeData['ImpByCreative']);
		           	$scope.clicksByCreative =  JSON.parse($scope.archivedCreativeData['ClicksByCreative']);
		           	ctrChartData= eval("(" + ctrData + ")");        	
		           	impChartData= eval("(" + impData + ")");
		           	clickChartData= eval("(" + clickData + ")");
		           	clickDonutChartData= eval("(" + clickDonut + ")");
		           	impressionDonutChartData= eval("(" + impresionDonut + ")");
		           	if($scope.archivedCreativeData['partnerCount']!=null && $scope.archivedCreativeData['partnerCount']!=undefined){
		           		averageSeries = parseInt($scope.archivedCreativeData['partnerCount']);
			           	myObj[averageSeries] = {type : "line"};
		           	}
		        
		            var pieChart = {};
		            pieChart.type = "PieChart";
		            pieChart.displayed = false;
		            pieChart.cssStyle = "height:350px; width:100%;";
		            var pieChart2 = {};
		            pieChart2.type = "PieChart";
		            pieChart2.displayed = false;
		            pieChart2.cssStyle = "height:350px; width:100%;";
		            var donutFormatter= {
		        			"number": [
		        						 {
		        						   columnNum: 1,
		        						   pattern:'###,###'			
		        						}
		        					]
		        	};
		           	
		           	barChart.data =ctrChartData;    
		           	barChart.options = {
		        	  		vAxis: {title: "CTR%"},
		        	  		hAxis: {title: "Creatives"},
		        	  		"title":"CTR% by Creatives",
		        	  		seriesType: "bars",
		        	  		series: {0: {type: "line"}}
		        	   };
		            $("#barChartDiv").show();
		            $scope.barChart = barChart;
		            $("#donutChartlagendDiv").show();
		            $("#barChartLoaderId").hide();
		            
		            pieChart.options = {
		                    "title": "IMPRESSION BY CREATIVE SIZE",
		                    "isStacked": "true",
		                    "fill": 20,
		                    "pieHole": 0.5,
		                    "legend": "none",
		                    slices : {
								0 : {
									color : 'red'
								},
								1 : {
									color : 'green'
								},
								2 : {
									color : 'orange'
								},
								3 : {
									color : 'blue'
								},
								4 : {
									color : 'yellow'
								}
							},
		                    "displayExactValues": true,
		                    "vAxis": {
		                        "title": "Impressions", "gridlines": {"count": 10}
		                    },
		                    "hAxis": {
		                        "title": "OS"
		                    }, "tooltip": {
		            		      "isHtml": true,
		            		      "trigger": "focus"
		            	    }
		                    
		                };
		                pieChart.formatters =donutFormatter;
		                pieChart.data = impressionDonutChartData;
		                $("#donutChartDiv").show();
		                $scope.donutChart = pieChart;
		                $("#donut2ChartlagendDiv").show();
		                $("#donutChartLoaderId").hide();
		                
		                pieChart2.options = {
			                    "title": "CLICKS BY CREATIVE SIZE",
			                    "isStacked": "true",
			                    "fill": 20,
			                    "pieHole": 0.5,
			                    "legend": "none",
			                    slices : {
									0 : {
										color : 'red'
									},
									1 : {
										color : 'green'
									},
									2 : {
										color : 'orange'
									},
									3 : {
										color : 'blue'
									},
									4 : {
										color : 'yellow'
									}
								},
			                    "displayExactValues": true,
			                    "vAxis": {
			                        "title": "Impressions", "gridlines": {"count": 10}
			                    },
			                    "hAxis": {
			                        "title": "OS"
			                    }, "tooltip": {
			            		      "isHtml": true,
			            		      "trigger": "focus"
			            	    }
			                    
			                };
			                pieChart2.formatters =donutFormatter;
			                $("#donut2ChartDiv").show();
			                pieChart2.data = clickDonutChartData;
			                $scope.donut2Chart = pieChart2;
			                $("#donut2ChartLoaderId").hide();
				}
			}
		}catch(err){
	    	console.log("fillCreativeData : err: "+err);
	    }
	};
	
	$scope.setLagendColor = function(color){
		 return { backgroundColor: color };
	};
	
	//Method for Device data || By Dheeraj
	
	$scope.getDeviceData = function(isTabClick) {
		try {
			if(isTabClick) {
				$scope.initTabClick(deviceChartName);
			}
			$scope.startLoadingCreative(deviceChartName);
		
			if($scope.archivedDeviceData == undefined || $scope.archivedDeviceData == null || (JSON.stringify($scope.archivedDeviceData)) == '{}' || (JSON.stringify($scope.archivedDeviceData)) == '[]') {
				performanceViewChartDataService.loadDeviceData($scope).then(function(data) {
					$scope.archivedDeviceData = data;
					$scope.fillDeviceData();
				});
			}
			else {
				$scope.fillDeviceData();
			}
		}catch(err){
	    	console.log("fillDeviceData: err: "+err);
	    }
	};
	
	
	$scope.fillDeviceData = function() {
		try {
			if($scope.archivedDeviceData == null || $scope.archivedDeviceData == undefined || $scope.archivedDeviceData == "" || $scope.archivedDeviceData == "null") {
				$scope.stopLoadingCreative(deviceChartName);
			}
			else {
					if(selectedTab == deviceChartName) {
		           	var ctrData=$scope.archivedDeviceData['CTR'];
		           	var impData=$scope.archivedDeviceData['Impressions'];
		           	var clickData=$scope.archivedDeviceData['Clicks'];
		           	var clickDonut = $scope.archivedDeviceData['DonutClick'];
		           	var impresionDonut = $scope.archivedDeviceData['DonutImpression'];
		           	
		           	if($scope.archivedDeviceData['TargetDevice']!=null && $scope.archivedDeviceData['TargetDevice']!=undefined && $scope.archivedDeviceData['TargetDevice']!=[]){
			        	   $scope.targetValue.targetCreative = "("+$scope.archivedDeviceData['TargetDevice']+")";
			           	}else{
			        	   $scope.targetValue.targetCreative = "";
			           	}
			           	if($scope.archivedDeviceData['NonTargetDevice']!=null && $scope.archivedDeviceData['NonTargetDevice']!=undefined && $scope.archivedDeviceData['NonTargetDevice']!=[]){
			        	   $scope.targetValue.nonTargetCreative = "("+$scope.archivedDeviceData['NonTargetDevice']+")";
			           	}else{
			        	   $scope.targetValue.nonTargetCreative = "";
			           	}
			           	

			           	if($scope.archivedDeviceData['TargetGoal']!=null && $scope.archivedDeviceData['TargetGoal']!=undefined && $scope.archivedDeviceData['TargetGoal']!=[]){
			           		if($scope.orderInfo.campaignType=='CPM'){
			           			$scope.targetValue.targetGoal = formatInt($scope.archivedDeviceData['TargetGoal'])+' IMP';
			           		}else if($scope.orderInfo.campaignType=='CPC'){
			           			$scope.targetValue.targetGoal = formatInt($scope.archivedDeviceData['TargetGoal'])+' CLICK';
			           		}
			           		
			           	}else{
			           		$scope.targetValue.targetGoal="";
			           	}
			           	
			        	if($scope.archivedDeviceData['NonTargetGoal']!=null && $scope.archivedDeviceData['NonTargetGoal']!=undefined && $scope.archivedDeviceData['NonTargetGoal']!=[]){
			        		
			        		if($scope.orderInfo.campaignType=='CPM'){
			        			$scope.targetValue.nonTargetGoal =formatInt($scope.archivedDeviceData['NonTargetGoal'])+' IMP';
			           		}else if($scope.orderInfo.campaignType=='CPC'){
			           			$scope.targetValue.nonTargetGoal =formatInt($scope.archivedDeviceData['NonTargetGoal'])+' CLICK';
			           		}
			           	}else{
			           		$scope.targetValue.nonTargetGoal="";
			           	}
			        	
			        	if($scope.archivedDeviceData['TargetPercentage']!=null && $scope.archivedDeviceData['TargetPercentage']!=undefined && $scope.archivedDeviceData['TargetPercentage']!=[]){
			        		$scope.targetValue.targetPercentage = formatFloat($scope.archivedDeviceData['TargetPercentage'], 2)+'%';
			           	}else{
			           		$scope.targetValue.targetGoal="";
			           	}
			        	
			        	if($scope.archivedDeviceData['NonTargetPercentage']!=null && $scope.archivedDeviceData['NonTargetPercentage']!=undefined && $scope.archivedDeviceData['NonTargetPercentage']!=[]){
			        		$scope.targetValue.nonTargetPercentage = formatFloat($scope.archivedDeviceData['NonTargetPercentage'], 2)+'%';
			           	}else{
			           		$scope.targetValue.targetGoal="";
			           	}

		           	
			        	if($scope.archivedDeviceData['partnerCount']!=null && $scope.archivedDeviceData['partnerCount']!=undefined){
			           		averageSeries = parseInt($scope.archivedDeviceData['partnerCount']);
				           	myObj[averageSeries] = {type : "line"};
			           	}
		        	$scope.targetValue.targetGoal = formatInt($scope.archivedDeviceData['TargetGoal'])+' IMP';
		           	$scope.targetValue.nonTargetGoal =formatInt($scope.archivedDeviceData['NonTargetGoal'])+' IMP';
		           	$scope.targetValue.targetPercentage = formatFloat($scope.archivedDeviceData['TargetPercentage'], 2)+'%';
		           	$scope.targetValue.nonTargetPercentage = formatFloat($scope.archivedDeviceData['NonTargetPercentage'], 2)+'%';
		           	
		           	$scope.targetValue.campaignType = $scope.orderInfo.campaignType;
		           	$scope.impByCreative =  JSON.parse($scope.archivedDeviceData['ImpressionsByDevice']);
		           	$scope.clicksByCreative =  JSON.parse($scope.archivedDeviceData['ClicksByDevice']);
		           	ctrChartData= eval("(" + ctrData + ")");        	
		           	impChartData= eval("(" + impData + ")");
		           	clickChartData= eval("(" + clickData + ")");
		           	clickDonutChartData= eval("(" + clickDonut + ")");
		           	impressionDonutChartData= eval("(" + impresionDonut + ")");

		           	
		            var pieChart = {};
		            pieChart.type = "PieChart";
		            pieChart.displayed = false;
		            pieChart.cssStyle = "height:350px; width:100%;";
		            var pieChart2 = {};
		            pieChart2.type = "PieChart";
		            pieChart2.displayed = false;
		            pieChart2.cssStyle = "height:350px; width:100%;";
		            var donutFormatter= {
		        			"number": [
		        						 {
		        						   columnNum: 1,
		        						   pattern:'###,###'			
		        						}
		        					]
		        	};
		           	
		           	barChart.data =ctrChartData;    
		           	barChart.options = {
		        	  		vAxis: {title: "CTR%"},
		        	  		hAxis: {title: "Devices"},
		        	  		"title":"CTR% by Devices",
		        	  		seriesType: "bars",
		        	  		series: {0: {type: "line"}}
		        	   };
		            $("#barChartDiv").show();
		            $scope.barChart = barChart;
		            $("#donutChartlagendDiv").show();
		            $("#barChartLoaderId").hide();
		            
		            pieChart.options = {
		                    "title": "IMPRESSION BY DEVICES",
		                    "isStacked": "true",
		                    "fill": 20,
		                    "pieHole": 0.5,
		                    "legend": "none",
		                    slices : {
								0 : {
									color : 'red'
								},
								1 : {
									color : 'green'
								},
								2 : {
									color : 'orange'
								},
								3 : {
									color : 'blue'
								},
								4 : {
									color : 'yellow'
								}
							},
		                    "displayExactValues": true,
		                    "vAxis": {
		                        "title": "Impressions", "gridlines": {"count": 10}
		                    },
		                    "hAxis": {
		                        "title": "OS"
		                    }, "tooltip": {
		            		      "isHtml": true,
		            		      "trigger": "focus"
		            	    }
		                    
		                };
		                pieChart.formatters =donutFormatter;
		                pieChart.data = impressionDonutChartData;
		                $("#donutChartDiv").show();
		                $scope.donutChart = pieChart;
		                $("#donut2ChartlagendDiv").show();
		                $("#donutChartLoaderId").hide();
		                
		                pieChart2.options = {
			                    "title": "CLICKS BY DEVICES",
			                    "isStacked": "true",
			                    "fill": 20,
			                    "pieHole": 0.5,
			                    "legend": "none",
			                    slices : {
									0 : {
										color : 'red'
									},
									1 : {
										color : 'green'
									},
									2 : {
										color : 'orange'
									},
									3 : {
										color : 'blue'
									},
									4 : {
										color : 'yellow'
									}
								},
			                    "displayExactValues": true,
			                    "vAxis": {
			                        "title": "Impressions", "gridlines": {"count": 10}
			                    },
			                    "hAxis": {
			                        "title": "OS"
			                    }, "tooltip": {
			            		      "isHtml": true,
			            		      "trigger": "focus"
			            	    }
			                    
			                };
			                pieChart2.formatters =donutFormatter;
			                $("#donut2ChartDiv").show();
			                pieChart2.data = clickDonutChartData;
			                $scope.donut2Chart = pieChart2;
			                $("#donut2ChartLoaderId").hide();
				}
			}
		}catch(err){
	    	console.log("fillDeviceData : err: "+err);
	    }
	};
	
	
	// Anup Dutta | Methods for Performance by OS -- start
	
	$scope.getOSData = function(isTabClick){
		try{
			if(isTabClick) {
				$scope.initTabClick(osChartName);
			}
			$scope.startLoadingCreative(osChartName);
			
			if($scope.archivedOSData == undefined || $scope.archivedOSData == null || (JSON.stringify($scope.archivedOSData)) == '{}' || (JSON.stringify($scope.archivedOSData)) == '[]') {
				performanceViewChartDataService.loadOSData($scope).then(function(data) {
					$scope.archivedOSData = data;
					$scope.fillOSData();
				});
			}
			else {
				$scope.fillOSData();
			}
		}catch(err){
	    	console.log("getCreativeData: err: "+err);
	    }
	};
	
	$scope.fillOSData = function(){
		try{
			if($scope.archivedOSData == null || $scope.archivedOSData == undefined || $scope.archivedOSData == "" || $scope.archivedOSData == "null") {
				$scope.stopLoadingCreative(osChartName);
			}
			else {
				if(selectedTab == osChartName) {
					var ctrData=$scope.archivedOSData['CTR'];
		           	var impData=$scope.archivedOSData['Impressions'];
		           	var clickData=$scope.archivedOSData['Clicks'];
		           	var clickDonut = $scope.archivedOSData['DonutClick'];
		           	var impresionDonut = $scope.archivedOSData['DonutImpression'];
		          // 	$scope.targetValue = {targetCreative:'--',nonTargetCreative:'--',targetGoal:'--',nonTargetGoal:'--',targetPercentage:'--',nonTargetPercentage:'--'};
		           
		           	if($scope.archivedOSData['TargetOS']!=null && $scope.archivedOSData['TargetOS']!=undefined && $scope.archivedOSData['TargetOS']!=[]){
		        	   $scope.targetValue.targetCreative = "("+$scope.archivedOSData['TargetOS']+")";
		           	}else{
		        	   $scope.targetValue.targetCreative = "";
		           	}
		           	if($scope.archivedOSData['NonTargetOS']!=null && $scope.archivedOSData['NonTargetOS']!=undefined && $scope.archivedOSData['NonTargetOS']!=[]){
		        	   $scope.targetValue.nonTargetCreative = "("+$scope.archivedOSData['NonTargetOS']+")";
		           	}else{
		        	   $scope.targetValue.nonTargetCreative = "";
		           	}
					
		           	if($scope.archivedOSData['TargetGoal'] >= $scope.headerData.delivered){
		           		$scope.archivedOSData['TargetGoal'] = $scope.headerData.delivered;
		           	}
		           	
		           	if($scope.archivedOSData['TargetGoal']!=null && $scope.archivedOSData['TargetGoal']!=undefined && $scope.archivedOSData['TargetGoal']!=[]){
		           		if($scope.orderInfo.campaignType=='CPM'){
		           			$scope.targetValue.targetGoal = formatInt($scope.archivedOSData['TargetGoal'])+' IMP';
		           		}else if($scope.orderInfo.campaignType=='CPC'){
		           			$scope.targetValue.targetGoal = formatInt($scope.archivedOSData['TargetGoal'])+' CLICK';
		           		}
		           		
		           	}else{
		           		$scope.targetValue.targetGoal="";
		           	}
		           	
		           	if($scope.orderInfo.campaignType=='CPM'){
	        			$scope.targetValue.nonTargetGoal =formatInt($scope.headerData.delivered - $scope.archivedOSData['TargetGoal'])+' IMP';
	           		}else if($scope.orderInfo.campaignType=='CPC'){
	           			$scope.targetValue.nonTargetGoal =formatInt($scope.headerData.delivered - $scope.archivedOSData['TargetGoal'])+' CLICK';
	           		}
		           	
		           	$scope.targetValue.targetPercentage = formatFloat(($scope.archivedOSData['TargetGoal']/$scope.headerData.delivered)*100, 2)+'%';
		           	$scope.targetValue.nonTargetPercentage = formatFloat((100-($scope.archivedOSData['TargetGoal']/$scope.headerData.delivered)*100), 2)+'%';
		           	

		        	if($scope.archivedOSData['partnerCount']!=null && $scope.archivedOSData['partnerCount']!=undefined){
		           		averageSeries = parseInt($scope.archivedOSData['partnerCount']);
			           	myObj[averageSeries] = {type : "line"};
		           	}
		           	
		           	$scope.targetValue.campaignType = $scope.orderInfo.campaignType;
		           	$scope.impByCreative =  JSON.parse($scope.archivedOSData['ImpByOS']);
		           	$scope.clicksByCreative =  JSON.parse($scope.archivedOSData['ClicksByOS']);
		           	ctrChartData= eval("(" + ctrData + ")");        	
		           	impChartData= eval("(" + impData + ")");
		           	clickChartData= eval("(" + clickData + ")");
		           	clickDonutChartData= eval("(" + clickDonut + ")");
		           	impressionDonutChartData= eval("(" + impresionDonut + ")");
		           	console.log($scope.impByCreative);
		           	
		        
		            var pieChart = {};
		            pieChart.type = "PieChart";
		            pieChart.displayed = false;
		            pieChart.cssStyle = "height:350px; width:100%;";
		            var pieChart2 = {};
		            pieChart2.type = "PieChart";
		            pieChart2.displayed = false;
		            pieChart2.cssStyle = "height:350px; width:100%;";
		            var donutFormatter= {
		        			"number": [
		        						 {
		        						   columnNum: 1,
		        						   pattern:'###,###'			
		        						}
		        					]
		        	};
		           	
		           	barChart.data =ctrChartData;    
		           	barChart.options = {
		        	  		vAxis: {title: "CTR%"},
		        	  		hAxis: {title: "Operating System"},
		        	  		"title":"CTR% by Operating System",
		        	  		seriesType: "bars",
		        	  		/*series: ""*/
		        	  		series: {0: {type: "line"}}
		        	   };
		            $("#barChartDiv").show();
		            $scope.barChart = barChart;
		            $("#donutChartlagendDiv").show();
		            $("#barChartLoaderId").hide();
		            
		            
		            var colorSlices = {
						0 : {
							color : 'red'
						},
						1 : {
							color : 'green'
						},
						2 : {
							color : 'orange'
						},
						3 : {
							color : 'blue'
						},
						4 : {
							color : 'yellow'
						},
						5:{
							color : '#CD7F32'
						},
						6:{
							color : 'aqua'
						},
						7:{
							color : 'gold'
						},
						8:{
							color : 'darkred'
						},
						9:{
							color : 'gray'
						},
						10:{
							color : 'cyan'
						},
						11:{
							color : '#EFDECD'
						},
						12:{
							color : '#79443B'
						},
						13:{
							color : '#F0DC82'
						},
						14:{
							color : '#536872'
						},
						15:{
							color : '#960018'
						},
						16:{
							color : '#92A1CF'
						},
						17:{
							color : '#FF7F50'
						},
						18:{
							color : '#FBEC5D'
						}
						
					};
					
		            pieChart.options = {
		                    "title": "IMPRESSION BY OPERATING SYSTEM",
		                    "isStacked": "true",
		                    "fill": 20,
		                    "pieHole": 0.5,
		                    "legend": "none",
		                    slices : colorSlices,
		                    "displayExactValues": true,
		                    "vAxis": {
		                        "title": "Impressions", "gridlines": {"count": 10}
		                    },
		                    "hAxis": {
		                        "title": "OS"
		                    }, "tooltip": {
		            		      "isHtml": true,
		            		      "trigger": "focus"
		            	    }
		                    
		                };
		                pieChart.formatters =donutFormatter;
		                pieChart.data = impressionDonutChartData;
		                $("#donutChartDiv").show();
		                $scope.donutChart = pieChart;
		                $("#donut2ChartlagendDiv").show();
		                $("#donutChartLoaderId").hide();
		                
		                pieChart2.options = {
			                    "title": "CLICKS BY OPERATING SYSTEM",
			                    "isStacked": "true",
			                    "fill": 20,
			                    "pieHole": 0.5,
			                    "legend": "none",
			                    slices : colorSlices,
			                    "displayExactValues": true,
			                    "vAxis": {
			                        "title": "Impressions", "gridlines": {"count": 10}
			                    },
			                    "hAxis": {
			                        "title": "OS"
			                    }, "tooltip": {
			            		      "isHtml": true,
			            		      "trigger": "focus"
			            	    }
			                    
			                };
			                pieChart2.formatters =donutFormatter;
			                $("#donut2ChartDiv").show();
			                pieChart2.data = clickDonutChartData;
			                $scope.donut2Chart = pieChart2;
			                $("#donut2ChartLoaderId").hide();
				
			}
		}
		}catch(err){
	    	console.log("fillOSData : err: "+err);
	    }
	};
	
	// End Performance by OS
	
	$scope.drawPerformanceLineChart = function(chartYAxisLabel, chartData) {
			$("#perfChartDiv").hide();
            $("#perfChartLoaderId").show();
		    $("#perfChartNoDataId").hide();
		    
		    var perfChartData;
		    var perfChartColumns = [];
		    var perfChartSeries = {};
		    var perfChartOptions={};
		    var perfChart = {};
		    
		    try{
		    	if(chartData == undefined || chartData == null || (JSON.stringify(chartData)) == '{}') {
	    			console.log('No '+chartYAxisLabel+' data');
	    			$("#perfChartLoaderId").hide();
	    		    $("#perfChartNoDataId").show();
	    		}
	    		else {
	    			perfChartData=chartData;
	    			perfChartData= eval("(" + perfChartData + ")");
	    			if(perfChartData == null || perfChartData.length == 0 || perfChartData == '[]') {
	    				$("#perfChartLoaderId").hide();
	    			    $("#perfChartNoDataId").show();
	        	   }
	        	   else {
	        		   $("#perfChartDiv").show();
	        		   perfChart.data = perfChartData;    
		            	
		    			$("#chartType option[value='? undefined:undefined ?']").remove();
		    			perfChart.methods = {};
		    			if(perfChartData !=null){
		    				for (var i = 0; i < perfChartData.rows[0].c.length; i++) {
		    					perfChartColumns.push(i);
		    					if (i > 0) {
		    						perfChartSeries[i - 1] = {};
		    					}
		    				}
		    				perfChartOptions = { series: perfChartSeries, pointSize: 4, colors:['#43C087', '#F26C28', '#8F7FD4', '#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080']};
		    			}
		    			perfChart.view = {
		    					columns: perfChartColumns
		    			};
            	
		    			perfChart.type = "LineChart";
		    			perfChart.displayed = false;
		    			perfChart.cssStyle = "height:400px; width:100%;";
		    			perfChart.formatters = {};
              
		    			perfChart.options = {
		    					"isStacked": "true",
		    					"fill": 20,
		    					"displayExactValues": true,
		    					"pointSize": 4,
		    					"colors":['#43C087', '#F26C28', '#8F7FD4', '#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080'],
		    					"vAxis": {
		    						"title": chartYAxisLabel,
		    						"gridlines": {"count": 10},
		    						"format":"##.####"
		    					},
		    					"hAxis": {
		    						"title": "Date"	            	
		    					},
		    					"tooltip": {
	    					      "isHtml": true
	    					    }
		    			};
		    			
		    			 $("#perfChartLoaderId").hide();
		    			 $scope.perfChart = perfChart;
	        	   }
	    			
	    			$scope.perfChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {
	    		        	var col = sel[0].column;           	
	    		            if (perfChartColumns[col] == col) {
	    		                // hide the data series
	    		            	perfChartColumns[col] = {
	    		                    label: $scope.perfChart.data.cols[col].label,
	    		                    type: $scope.perfChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		                perfChartSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	perfChartColumns[col] = col;
	    		            	perfChartSeries[col-1].color = null;
	    		            }
	    		           $scope.perfChart.options=perfChartOptions;
	    		           $scope.perfChart.view = {columns: perfChartColumns };
	    		        	
	    		            
	    		        }
	    		    };
	    		}
		    	
		    }catch(err){
		    	console.log("performanceViewChartDataService: err: "+err);
		    }
    
    	};   
    	
    	
    	
    
    	var formatCollection = [
    	                        {name: "color",
    	                        	format: [{columnNum: 4,
    	                        		formats: [
    	                        		          {from: 0,to: 3,color: "white", bgcolor: "red" },
    	                        		          {from: 3,to: 5,color: "white",fromBgColor: "red",toBgColor: "blue"},
    	                        		          {from: 6, to: null,color: "black", bgcolor: "#33ff33"}
    	                        		         ]
    	                        	}]
    	                        },
    	                        { 
    	                        	name: "arrow",  checked:false,
    	                        	format: [{columnNum: 1,base: 19}]
    	                        },
    	                        {
    	                        	name: "date",
    	                        	format: [ {columnNum: 5,formatType: 'long'}]
    	                        }, 
    	                        {
    	                        	name: "number",
    	                        	format: [{columnNum: 4,prefix: '$'}]
    	                        },
    	                        {
    	                        	name: "bar",
    	                        	format: [{columnNum: 1,width: 100}]
    	                        }
    	                       ];
    	
    	$scope.hideServer = false;
    	$scope.selectionChange = function () {
    		if($scope.hideServer) {
    			$scope.chart.view = {columns: [0,1,2,4]};
    		} else {
    			$scope.chart.view = {};
    		}
    	};

    	$scope.formatCollection = formatCollection;
    	$scope.toggleFormat= function (format) {
    		$scope.chart.formatters[format.name] = format.format;
    	};
    	/*line chart code Ends*/
    	
    	/* Geo chart code starts*/
    	$scope.drawPerformanceGeoChart = function(chartData) {
    		$("#locationChartDiv").hide();
    		$("#locationChartLoaderId").show();
    	    $("#locationChartNoDataId").hide();
		    
	    	var geoChart = {};
	    	$scope.geoChart = {};
	    	try{
		    	if(chartData == undefined || chartData == null || (JSON.stringify(chartData)) == '{}') {
	    			console.log('No Geo data');
	    			$("#locationChartLoaderId").hide();
	    		    $("#locationChartNoDataId").show();
	    		}
	    		else {
	    			$("#locationChartDiv").show();
					geoChart.data= chartData;
			        geoChart.type = "GeoChart";
			        geoChart.displayed = false;
			        geoChart.cssStyle = "height:600px; width:100%;";
			        geoChart.options = {
			    	    "region": "US",
			            "displayMode": "markers",
			            "resolution" : "provinces",
			    	    "colorAxis": {
			    	            "values": [0, 0.5, 1],
			    	            "colors": ["red", "yellow", "green"]
			    	        },
			    		"title":"Geo Map"
			        };
			        geoChart.formatters = {
			    		"number": [
			    		      {
			    		       	 columnNum: 1,		       
			    		       	 suffix: '%',
			    				 fractionDigits:4			
			    		      },
			    		      {
			    		        columnNum: 2,		       
			    				pattern:'###,###'			
			    		      }
			    		     ]
			    	};
			       	$("#locationChartLoaderId").hide();	
			        $scope.geoChart = geoChart;
	    		}
	    	}catch(err){
		    	console.log("drawPerformanceGeoChart: err: "+err);
		    }
    	};
    	/* Geo chart code ends*/

    	$scope.chartReady = function() {    	
    		fixGoogleChartsBarsBootstrap();
    	};   
    
 
    	function fixGoogleChartsBarsBootstrap() {
    		// Google charts uses <img height="12px">, which is incompatible with Twitter
    		// * bootstrap in responsive mode, which inserts a css rule for: img { height: auto; }.
    		// *
    		// * The fix is to use inline style width attributes, ie <img style="height: 12px;">.
    		// * BUT we can't change the way Google Charts renders its bars. Nor can we change
    		// * the Twitter bootstrap CSS and remain future proof.
    		// *
    		// * Instead, this function can be called after a Google charts render to "fix" the
    		// * issue by setting the style attributes dynamically.

    		$(".google-visualization-table-table img[width]").each(function(index, img) {
    			$(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
    		});
	    };
    
	    $scope.emptyData = function() {   
	    };  
    
	    $rootScope.safeApply = function( fn ) {
	    	var phase = this.$root.$$phase;
	    	if(phase == '$apply' || phase == '$digest') {
	    		if(fn) {
	    			fn();
	    		}
	    	} else {
	    		this.$apply(fn);
	    	}
	    };
	    
	    
	    
	    
	    $scope.chartSelectionChange = function () {    	
	    	 if ($scope.displaydatatype=='CTR') {
	    		 $("#chartType").val("CTR");
	    		
	    		 barChart.data = ctrChartData;
	    		  barChart.options = {
	    			  		vAxis: {title: "CTR%"},
	    			  		hAxis: {title: kpiValue},
	    			  		"title":"CTR% by " + kpiValue,
	    			  		seriesType: "bars",
	    			  		series: {0: {type: "line"}}
	    			   };
	    		  $("#barChartLoaderId").hide();
	    		  
	    		  $scope.barChart = barChart;
		        $scope.chart.view = {
		        		   columns: columns
		        };
				
	           
	    	}else if($scope.displaydatatype=='Impressions') {
	    		 $("#chartType").val("Impressions");            
	    		 if(impChartData !=undefined){
	    			 barChart.data = impChartData;
	    			 
	    			  barChart.options = {
	    				  		vAxis: {title: "Impressions"},
	    				  		hAxis: {title: kpiValue},
	    				  		"title":"Impressions by " + kpiValue,
	    				  		seriesType: "bars",
	    				  		series: {0: {type: "line"}}
	    				   };
	    			  $("#barChartLoaderId").hide();
	        		 $scope.chart.view = {
	                		   columns: columns
	                 }; 
	        		 
	 			}
	             
	    	}else if($scope.displaydatatype=='Clicks') {
	    		 $("#chartType").val("Clicks");
	    		 barChart.data = clickChartData;
	       	 
	       		 barChart.options = {
				  		vAxis: {title: "Clicks"},
				  		hAxis: {title: kpiValue},
				  		"title":"Clicks by " + kpiValue,
				  		seriesType: "bars",
				  		series: {0: {type: "line"}}
				   };
	       		$("#barChartLoaderId").hide();
	       		
	            $scope.chart.view = {
	         		   columns: columns
	         	};
	       	}

	    };
	    
	});

	performanceViewChartApp.directive('multiselectDropdown', [function() {
	    return function(scope, element, attributes) {
	
	        element = $(element[0]); // Get the element as a jQuery element
	
	        // Below setup the dropdown:
	
	        element.multiselect({
	        	includeSelectAllOption: true
	        })
	    }
	}]);


