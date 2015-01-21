
// New Module
var reportingApp = angular.module('reportingApp');

// Factory
reportingApp.factory('productFactory', function($http) {

	return {
		getPlacementsForCampaigns : function() {
			return $http.get('/loadPlacementsForCampaigns.lin').then(
					function(result) {
						return result.data;
					});
		},
	};
});

// Controller
reportingApp.controller('reportingCtrl', function($scope,reportFactory) {
	if($scope.reportBy == undefined) {
		$scope.reportBy = 'day';
	}
	
	function fillSelect(list, id) {
	    for (var i=0; i<list.length; i++) {
	        if (list[i].id == id) {
	            return list[i];
	        }
	    }
	}
	
	$scope.selectPlatform = {
        multiple: true,
        placeholder: "Select Placement",
        query: function (query) {
            var data = {results: []};
            angular.forEach($scope.platformList, function(item, key){
              if (item.text.toUpperCase().indexOf(query.term.toUpperCase()) >= 0) {
                data.results.push(item);
              }
            });
            query.callback(data);
        },
        initSelection: function (element, callback) {
            var val = $(element).select2('val'),
              results = [];
            for (var i=0; i<val.length; i++) {
                results.push(fillSelect($scope.platformList, val[i]));
            }
            callback(results);
        }
    };
		
	// Get Platform List
	productFactory.getPlatforms().then(
			function(platformData) {
				if (platformData == undefined || platformData == null
						|| (JSON.stringify(platformData)) == '[]') {
					console.log('No Platform Data');
				} else {
					$scope.platformList = platformData;
					//if(productStatus != 'update') {$("#platformSelect").select2("val",[allOptionId]);}
				}
			});

	$scope.campaignChanged = function() {
		$scope.checkAllNoneOptions('geoSelect');
		isDmaChanged = true;
		isModified = true;
	};
	
	$scope.modified = function(val) {
		isModified = val;
	};
	
	function getPlatformArray() {
		if(isPlatformChanged) {
			var val = $('#platformSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				platformObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.platformList.length; j++) {
						if($scope.platformList[j].id == arr[i]) {
							platformObject.push($scope.platformList[j]);
						}
					}
				}
			}
			isPlatformChanged = false;
		}
		return platformObject;
	};
	function getCityArray() {
		var cityObject = [];
		var val = $('#citySearch').val();
		if (val != undefined && val != null && $.trim(val) != '') {
			var arr = val.split(',');
			for(i=0; i<arr.length; i++) {
				for(j=0; j<cityJsonObj.length; j++) {
					if(cityJsonObj[j].id == arr[i]) {
						cityObject.push(cityJsonObj[j]);
					}
				}
			}
		}
		return cityObject;
	};
	function getAdUnitArray() {
		var adUnitObject = [];
		var val = $('#adUnitSearch').val();
		if (val != undefined && val != null && $.trim(val) != '') {
			var arr = val.split(',');
			for(i=0; i<arr.length; i++) {
				for(j=0; j<adUnitJsonObj.length; j++) {
					if(adUnitJsonObj[j].id == arr[i]) {
						adUnitObject.push(adUnitJsonObj[j]);
					}
				}
			}
		}
		return adUnitObject;
	};
	function getZipArray() {
		var zipObject = [];
		var val = $('#zipSearch').val();
		if (val != undefined && val != null && $.trim(val) != '') {
			var arr = val.split(',');
			for(i=0; i<arr.length; i++) {
				for(j=0; j<zipJsonObj.length; j++) {
					if(zipJsonObj[j].id == arr[i]) {
						zipObject.push(zipJsonObj[j]);
					}
				}
			}
		}
		return zipObject;
	};

});

