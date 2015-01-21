
var page = 0;
var limit = 25;
var dataType = 'active';
var campaignIds = [];
var searchKeyword = '';

// New Module
var campaignApp = angular.module('campaignApp', [ 'ngRoute', 'ui.bootstrap' ]);

// Config + Route
campaignApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/campaignGrid', {
		controller : 'simpleController',
		templateUrl : 'campaignGrid.html'
	}).when('/campaignList', {
		controller : 'simpleController',
		templateUrl : 'campaignList.html'
	}).otherwise({
		redirectTo : '/campaignList'
	});

} ]);

// Factory
campaignApp.factory('simpleFactory', function($http) {
	return { 
		getCampaigns: function() {
			return $http.get('/campaignGridData.lin?dataType='+dataType+'&limit='+limit+'&offset='+page+'&publisherIdInBQ=5&searchKeyword='+searchKeyword)
                      .then(function(result) {
	    	 console.log(result.data);
             return result.data;
                       });
       }
	}

});

// Controller
campaignApp.controller('simpleController', function($scope, $filter, $location, $modal, simpleFactory) {
	if(page == 0) {
		// this is done because the controller is called every time a function is called
		// and re initialises $scope.campaigns 
		$scope.campaigns = [];
		campaignIds = [];
		page = page + 1;
	}
	
	/*$scope.orderProp='name';
	$scope.orderType = 'asc';*/
	$scope.predicate = 'pacing';
	
	$scope.loadCampaignSummaryData = function(buttonClick) {
		if(buttonClick) {
			page = 1;
		}
		if($('#activeRadio').is(':checked')) {
			dataType = 'active';
		}
		else {
			dataType = 'all';
		}
		try {
			simpleFactory.getCampaigns().then(function(campaignData) {
	    		if(campaignData == undefined || campaignData == null || (JSON.stringify(campaignData)) == '[]') {
	    			console.log("No Data");
	    			if(buttonClick) {
	    				$scope.campaigns = [];
	    				campaignIds = [];
	    			}
	    		}
	    		else {
    				if(buttonClick) {
    					$scope.campaigns = campaignData;
    					campaignIds = [];
    					$.each( campaignData, function(index, value){
    						campaignIds.push(value.id);
    					});
    				}
    				else {
    					$.each( campaignData, function(index, value) {
    						if($.inArray(value.id, campaignIds) == -1) {	// if not found
	    						$scope.campaigns.push(value);
	    						campaignIds.push(value.id);
    						}
    					});
    				}
    				console.log('campaignIds : ');
					console.log(campaignIds);
	    		}
			});
			page = page + 1;
		}
		catch(err){
	    	console.log("simpleFactory err : "+err);
	    }
	};
	
	$scope.go = function(path) {
		$location.path(path);
	};
	
	$scope.getCampaignDetails = function(advertiserId, orderId) {
		$('#advertiserId').val(advertiserId);
		$('#orderId').val(orderId);
		document.campaignSummaryForm.submit();
	}

	$scope.showPopUp = function(obj) {
		var modalInstance = $modal.open({
			templateUrl : '../campaignAlert.html',
			controller : function($scope, $modalInstance, currentCampaign) {
				$scope.currentCampaign = currentCampaign;
			},
			resolve : {
				currentCampaign : function() {
					return obj;
				}
			}
		});

	};
	
	/*$scope.sortBy = function(order) {
		if($scope.orderProp == order) {
			if($scope.orderType == 'asc') {
				$scope.orderType = 'desc';
			}
			else {
				$scope.orderType = 'asc';
			}
		}
		else {
			$scope.orderType = 'asc';
		}
		$scope.orderProp = order;
	};
	
	$scope.sort = function(campaign) {
		var returnval = '';
       if($scope.orderProp == 'name') {
    	   returnval = campaign.name;
        }
       else if($scope.orderProp == 'impression') {
    	   returnval = campaign.impression;
        }
       else if($scope.orderProp == 'click') {
    	   returnval = campaign.click;
        }
       else if($scope.orderProp == 'ctr') {
    	   returnval = campaign.ctr;
        }
       else if($scope.orderProp == 'spend') {
    	   returnval = campaign.spend;
        }
       else if($scope.orderProp == 'pacing') {
    	   returnval = campaign.pacing;
        }
       else {
    	   returnval = campaign.name;
       }
       if($scope.orderType == 'desc') {
    	   return -returnval;
       }
       return returnval;
    };*/
	
	$scope.$watch("searchCampaign",function(query){
		$scope.filtercampaigns = $filter("filter")($scope.campaigns , query);
		var temp = searchKeyword;
		if(query != undefined ) {
			searchKeyword = query;
		}
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filtercampaigns.length < limit) {
			$scope.loadCampaignSummaryData(false);
		}
	});

});

function getCampaignPerformancePrintView(str)
{
	if(str == 'true')
	{
		$("#campaignSummaryDiv_actualView").css("display", "none");
		$("#tabsDiv").css("display", "none");
		$("#header-toolbar").css("display", "none");
		$("#campaignSummaryDiv_printView").css("display", "block");
		
	}
	else
	{
		$("#campaignSummaryDiv_actualView").css("display", "block");
		$("#tabsDiv").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#campaignSummaryDiv_printView").css("display", "none");
	}
	
	window.print();
	
	$("#campaignSummaryDiv_actualView").css("display", "block");
	$("#tabsDiv").css("display", "block");
	$("#header-toolbar").css("display", "block");
	$("#campaignSummaryDiv_printView").css("display", "none");
	
}