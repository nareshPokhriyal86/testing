var page = 0;
var campaignIds = [];
var limit = 25;
var searchKeyword = '';
var campaignPerformanceSetupApp = angular.module('campaignPerformanceSetupApp', [ 'ui.bootstrap' ]);

// Factory
campaignPerformanceSetupApp.factory('campaignPerformanceSetupFactory', function($http) {
	return { 
		getCampaigns: function(campaignStatus) {
			return $http.get('/loadAllRunningCampaigns.lin?campaignStatus='+campaignStatus+'&offset='+page+'&searchKeyword='+searchKeyword).then(function(result) {
	    	 //console.log(result.data);
             return result.data;
                       });
       }
	
	};
});

// Controller
var selected = 1;
campaignPerformanceSetupApp.controller('campaignPerformanceSetupController', function($scope, $filter, campaignPerformanceSetupFactory) {
	
	if($scope.predicate == undefined) {
		$scope.predicate = 'sDate';
	}
	if($scope.reverse == undefined) {
		$scope.reverse = true;
	}
	
	if($scope.campaigns == undefined) {
		$scope.campaigns = [];
	}
	
	$scope.loadCampaignData = function(selected) {
		try {
			$scope.campaignStatus = 1;
			if(selected!=undefined && selected!=null){
				$scope.campaignStatus = selected;
			}
			$('#loaderRowId').css('display','table-row');
			$('#noMatchRowId').css('display','none');
			campaignPerformanceSetupFactory.getCampaigns($scope.campaignStatus).then(function(campaignData) {
	    		if(campaignData == undefined || campaignData == null || (JSON.stringify(campaignData)) == '[]') {
	    			console.log("No Data");
	    			$('#noMatchRowId').css('display','table-row');
	    			//$scope.campaigns = campaignData;
	    		}
	    		else {
	    			$.each( campaignData, function(index, value) {
						if($.inArray(value.campaignId, campaignIds) == -1) {	// if not found
    						$scope.campaigns.push(value);
    						campaignIds.push(value.campaignId);
						}else {
							console.log('Duplicate campaignId : '+value.campaignId);
						}
					});
    				console.log('campaignData : ');
					console.log(campaignData);
					page = page + 1;
	    		}
	    		$('#loaderRowId').css('display','none');
			});
		}
		catch(err){
	    	console.log("campaignPerformanceSetupFactory err : "+err);
	    }
	};
	
	$scope.showPerforemanceMonitoring = function(campaignId) {
		 location.href="/performanceAndMonitoring.lin?orderId="+campaignId;
	 }
	
	$scope.getCampaignDetails = function(campaignId) {
		$('#campaignId').val(campaignId);
		document.unifiedCampaignSetupForm.submit();
	};
	
	
	
	/*$scope.$watch("searchCampaign",function(query){
		$scope.filterCampaigns = $filter("filter")($scope.campaigns , query);
	});*/
	
	$scope.$watch("searchCampaign",function(query){
		$scope.filterCampaigns = $filter("filter")($scope.campaigns , query);
		var temp = searchKeyword;
		if(query != undefined ) {
			searchKeyword = query;
		}
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterCampaigns.length < limit) {
			$scope.loadCampaignData();
		}
	});
	

	$scope.status = [
              {id:'1', name:'All campaign'},
              {id:'2', name:'Running'},
              {id:'3', name:'Paused'},
              {id:'5', name:'Completed'},
	];
    $scope.selectedStatus = $scope.status[0]; // Active
	
	$scope.campaignPerformanceFilter = function(){
		selected =   $scope.selectedStatus;
		page = 1;
		campaignIds = [];
		$scope.campaigns = [];
		$scope.loadCampaignData(selected.id);
	};
	
	$scope.setGoalWidth = function(campaign){
		var goalValue = campaign.goalProgress;
		 return { width: goalValue+"%" };
	};
	
	$scope.setDateWidth = function(campaign){
		//var goalValue = campaign.goalProgress;
		var dateValue = campaign.dateProgress;
		if(dateValue>100){
			dateValue = 100;
		}
		//var lastValue = 100-(goalValue+dateValue);
		//var dateWidth = dateValue-goalValue;
		 return { width: dateValue+"%"   };
	};
	
	$scope.setLastWidth = function(campaign){
		var goalValue = campaign.goalProgress;
		var dateValue = campaign.dateProgress;
		var dateWidth = dateValue-goalValue;
		var lastValue = 100-(goalValue+dateWidth);
		 return { width: lastValue+"%"};
	};
	
});
