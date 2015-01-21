var page = 0;
var alertIds = [];
var limit = 25;
var dateSearch = '';
var campaignSearch = '';
var placementSearch = '';
var publisherSearch = '';
var ready = true; //Assign the flag here
var campaignAlertListingApp = angular.module('campaignAlertListingApp', [ 'ui.bootstrap' ]);

// Factory
campaignAlertListingApp.factory('campaignAlertListingFactory', function($http) {
	return { 
		getCampaignAlerts: function() {
			return $http.get('/loadAllCampaignAlert.lin?offset='+page+'&dateSearch='+dateSearch+'&campaignSearch='+campaignSearch+'&placementSearch='+placementSearch+'&publisherSearch='+publisherSearch).then(function(result) {
	    	 //console.log(result.data);
             return result.data;
                       });
       }	

	}
});

// Controller
var selected = 1;
campaignAlertListingApp.controller('campaignAlertListingController', function($scope, $filter, campaignAlertListingFactory) {
	/*if($scope.predicate == undefined) {
		$scope.predicate = 'date';
	}
	if($scope.reverse == undefined) {
		$scope.reverse = true;
	}*/
	
	if($scope.campaignAlerts == undefined) {
		$scope.campaignAlerts = [];
	}
	
	$scope.campaignListData={};
	$scope.loadCampaignAlerts = function(currentMatches) {
		try {
			$('#loaderRowId').css('display','table-row');
			$('#noMatchRowId').css('display','none');
			campaignAlertListingFactory.getCampaignAlerts().then(function(campaignAlertData) {
	    		if(campaignAlertData == undefined || campaignAlertData == null || (JSON.stringify(campaignAlertData)) == '[]') {
	    			console.log("No Data");
	    			if(currentMatches <= 0) {
	    				$('#noMatchRowId').css('display','table-row');
	    			}
	    		//	$('#noMatchRowId').css('display','table-row');
	    			
	    		}
	    		else {
	    			$.each( campaignAlertData, function(index, value) {
						if($.inArray(value.id, alertIds) == -1) {	// if not found
							$scope.campaignAlerts.push(value);
							alertIds.push(value.id);
						}
					});
					//$scope.campaignAlerts=campaignAlertData;
					page = page + 1;
					console.log("offset implemented:"+page);
					ready = true; 
	    			//$("#alertLoaderId").hide();
    				
	    		}
	    		$('#loaderRowId').css('display','none');
			});
		}
		catch(err){
	    	console.log("campaignAlertListingFactory err : "+err);
	    }
	};
	
	

/*	$scope.$watch("searchCampaign","searchDate","searchPlacement","searchPublisher",function(query){
		$scope.filterCampaigns = $filter("filter")($scope.campaignAlerts , query);
	});*/
	
	$scope.$watch("searchCampaign",function(query){
		$scope.filterAlerts = $filter("filter")($scope.campaignAlerts , query);
		var temp = campaignSearch;
		
		if(query != undefined ) {
			campaignSearch = query;
			console.log("campaignSearch Keyword :"+campaignSearch);
		}
		/*if(campaignSearch=='' && dateSearch=='' && placementSearch=='' && publisherSearch==''){
			page = 0;
			alertIds = [];
			$scope.campaignAlerts = [];
			//$scope.loadCampaignAlerts(0);
		}*/
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterAlerts.length < limit) {
			page = 0;
			$scope.loadCampaignAlerts($scope.filterAlerts.length);
		}
	});
	
	$scope.$watch("searchDate",function(query){
		$scope.filterAlerts = $filter("filter")($scope.campaignAlerts , query);
		var temp = dateSearch;
		if(query != undefined ) {
			dateSearch = query;
			console.log("dateSearch keyword :"+dateSearch);
		}
		/*if(campaignSearch=='' && dateSearch=='' && placementSearch=='' && publisherSearch==''){
			page = 0;
			alertIds = [];
			$scope.campaignAlerts = [];
			//$scope.loadCampaignAlerts(0);
		}*/
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterAlerts.length < limit) {
			page = 0;
			$scope.loadCampaignAlerts($scope.filterAlerts.length);
		}
	});
	
	$scope.$watch("searchPlacement",function(query){
		$scope.filterAlerts = $filter("filter")($scope.campaignAlerts , query);
		var temp = placementSearch;
		if(query != undefined ) {
			placementSearch = query;
			console.log("placementSearch Keyword :"+placementSearch);
		}
		/*if(campaignSearch=='' && dateSearch=='' && placementSearch=='' && publisherSearch==''){
			page = 0;
			alertIds = [];
			$scope.campaignAlerts = [];
			//$scope.loadCampaignAlerts(0);
		}*/
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterAlerts.length < limit) {
			page = 0;
			$scope.loadCampaignAlerts($scope.filterAlerts.length);
		}
	});
	
	$scope.$watch("searchPublisher",function(query){
		$scope.filterAlerts = $filter("filter")($scope.campaignAlerts , query);
		var temp = publisherSearch;
		if(query != undefined ) {
			publisherSearch = query;
			console.log("publisherSearch Keyword :"+publisherSearch);
		}
		/*if(campaignSearch=='' && dateSearch=='' && placementSearch=='' && publisherSearch==''){
			page = 0;
			alertIds = [];
			$scope.campaignAlerts = [];
			//$scope.loadCampaignAlerts(0);
		}*/
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterAlerts.length < limit) {
			page = 0;
			$scope.loadCampaignAlerts($scope.filterAlerts.length);
		}
	});
	
	$scope.alertListCustomFilter = function(campaignAlert) {
		try {
			if(campaignAlert != undefined && (dateSearch == undefined || dateSearch == '' || campaignAlert.date.toLowerCase().indexOf(dateSearch.toLowerCase()) >= 0) && 
				(placementSearch == undefined || placementSearch == '' || campaignAlert.placementName.toLowerCase().indexOf(placementSearch.toLowerCase()) >= 0) && 
				(campaignSearch == undefined || campaignSearch == '' || campaignAlert.campaignName.toLowerCase().indexOf(campaignSearch.toLowerCase()) >= 0) && 
				(publisherSearch == undefined || publisherSearch == '' || campaignAlert.publisherName.toLowerCase().indexOf(publisherSearch.toLowerCase()) >= 0)) {
					return true;
				}
		}catch(err){
	    	console.log("alertListCustomFilter err : "+err);
	    }
		return false;
	};
	
});
