
var retrievedContext = [];
var retrievedCreatives = [];
var retrievedDevices = [];
var retrievedDMAs = [];
var retrievedPlatforms = [];
var retrievedStates = [];
var isModified = false;
var isDmaChanged = false;
var isDeviceChanged = false;
var isPlatformChanged = false;
var isContextChanged = false;
var isCreativeChanged = false;
var isStateChanged = false;
var productName = '';
var dmaObject = [];
var deviceObject = [];
var platformObject = [];
var contextObject = [];
var creativeObject = [];
var stateObject = [];
var isProductNameAvailable = true;
//var initSave = false;

// New Module
var productApp = angular.module('productApp', [ 'ui' ]);

// Factory
productApp.factory('productFactory', function($http) {

	return {
		getIABContext : function() {
			return $http.get('/loadContextualConsistencies.lin').then(
					function(result) {
						return result.data;
					});
		},
		
		getCreatives : function() {
			return $http.get('/loadProductCreatives.lin').then(
					function(result) {
						return result.data;
					});
		},
		
		getDevices : function() {
			return $http.get('/loadProductDevices.lin')
					.then(function(result) {
						//console.log("device data : " + result.data);
						return result.data;
					});
		},
		
		getDmas : function() {
			return $http.get('/loadProductDmas.lin')
					.then(function(result) {
						//console.log("device data : " + result.data);
						return result.data;
					});
		},
		
		getPlatforms : function() {
			return $http.get('/loadProductPlatforms.lin')
					.then(function(result) {
						//console.log("device data : " + result.data);
						return result.data;
					});
		},
		
		getStates : function() {
			return $http.get('/loadStates.lin?countryCode=US')
					.then(function(result) {
						//console.log("State data : " + result.data);
						return result.data.states;
					});
		},

		retrieve : function(productId, partnerId) {
			return $http.get('/initEditProduct.lin?productId=' + productId+'&partnerId='+partnerId)
					.then(function(result) {
						//console.log("retrieve data : " + result.data);
						return result.data;
					});
		},

		save : function(productdata, isAutoSave) {
			$.ajax({
				url : "/saveProductData.lin",
				type : "POST",
				data : {
					productdata : JSON.stringify(productdata)
				},
				dataType : 'json',
				error : function(err) {
					console.log("save error : " + err);
					toastr.error('Save Failed');
				},
				success : function(data) {
					//console.log("save : success");
					toastr.success('Product saved Successfully');
					if(!isAutoSave) {
						document.submitForm.action='/publisherProduct.lin';
						document.submitForm.submit();
					}
				}
			});
			return;
		},
		
		isNameAvailable : function(name, productId, $scope) {
			//$('textValueAvailableImage').hide();
			$.ajax({
				url : "/checkProductNameAvailability.lin",
				type : "POST",
				data : {
					productName : $.trim(name),
					productId : productId
				},
				dataType : 'json',
				error : function(err) {
					console.log("isNameAvailable error : " + err);
					isProductNameAvailable = false;
					$('#productNameId').focus();
					$('#productAvailableMsg').html('Product Name already in Use');
					//toastr.error('Product Name not available');
				},
				success : function(data) {
					console.log("isNameAvailable : success");
					//$('textValueAvailableImage').show();
					$('#productAvailableMsg').html('');
					productName = name;
					$scope.productNameModel = name;
					isProductNameAvailable = true;
					isModified = true;
				}
			});
			return;
		},
	};
});

// Controller
productApp.controller('productController', function($scope,productFactory, $rootScope) {
	$scope.allDeviceCapabilities = [
	     {id:'0', name:'Any'},
	     {id:'3', name:'Mobile App & Web'},
	     {id:'1', name:'Mobile App Only'},
	     {id:'2', name:'Mobile Web Only'}
	     
	   ];
	//$scope.deviceCapability = $scope.allDeviceCapabilities[selectedDeviceCapability];
	if($scope.selectedAdUnits == undefined) {
		$scope.selectedAdUnits = [];
	}
	if($scope.isDataRetrieved == undefined) {
		$scope.isDataRetrieved = false;
	}
	if ($scope.productNameModel == undefined) {
		$scope.productNameModel = '';
	}
	if ($scope.siteNameModel == undefined) {
		$scope.siteNameModel = '';
	}
	if ($scope.noteModel == undefined) {
		$scope.noteModel = '';
	}
	if ($scope.networkIdModel == undefined) {
		$scope.networkIdModel = $('#networkId').val();
	}
	if ($scope.publisherNameModel == undefined) {
		$scope.publisherNameModel = $('#publisherName').val();
	}
	if ($scope.rateModel == undefined) {
		$scope.rateModel = '$0.00';
	}
	if ($scope.statusModel == undefined) {
		$scope.statusModel = 'false';
	}
	if ($scope.geoFencingModel == undefined) {
		$scope.geoFencingModel = 'false';
	}
	if ($scope.demographicModel == undefined) {
		$scope.demographicModel = 'false';
	}
	if ($scope.behaviourModel == undefined) {
		$scope.behaviourModel = 'false';
	}
	if ($scope.selectedIABList == undefined) {
		$scope.selectedIABList = [];
	}
	if ($scope.selectedCreativeList == undefined) {
		$scope.selectedCreativeList = [];
	}
	if ($scope.selectedDeviceList == undefined) {
		$scope.selectedDeviceList = [];
	}
	if ($scope.selectedPlatformList == undefined) {
		$scope.selectedPlatformList = [];
	}
	if ($scope.selectedDmaList == undefined) {
		$scope.selectedDmaList = [];
	}
	if ($scope.selectedCityList == undefined) {
		$scope.selectedCityList = [];
	}
	if ($scope.deviceCapability == undefined) {
		$scope.deviceCapability = $scope.allDeviceCapabilities[0];
	}
	
	function fillSelect(list, id) {
	    for (var i=0; i<list.length; i++) {
	        if (list[i].id == id) {
	            return list[i];
	        }
	    }
	}
	
	function fillGroupSelect(list, id) {
		for (var i=0; i<list.length; i++) {
            for (var j=0; j<list[i].children.length; j++) {
                if (list[i].children[j].id == id) {
                    return list[i].children[j];
                }
            }
        }
	}
	
	$scope.getSelectedDeviceCapabilityIndex = function(id){
		var index = 0;
		for(var i=0; i<$scope.allDeviceCapabilities.length; i++) {
			if ($scope.allDeviceCapabilities[i].id == id) {
          	  index = i;
          	  break;
            }
		}
		return index;
	}
	
	$scope.selectDma = {
        multiple: true,
        placeholder: "Select DMA",
        query: function (query) {
            var data = {results: []};
            angular.forEach($scope.dmaList, function(item, key){
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
                results.push(fillSelect($scope.dmaList, val[i]));
            }
            callback(results);
        }
    };
	
	$scope.selectPlatform = {
        multiple: true,
        placeholder: "Select Platform",
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
	
	$scope.selectDevice = {
        multiple: true,
        placeholder: "Select Device",
        query: function (query) {
            var data = {results: []};
            angular.forEach($scope.deviceList, function(item, key){
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
                results.push(fillSelect($scope.deviceList, val[i]));
            }
            callback(results);
        }
    };
	
	$scope.selectContext = {
        multiple: true,
        placeholder: "Select Context",
        query: function (query) {
            var data = {results: []};
            angular.forEach($scope.IABContextList, function(item, key){
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
                results.push(fillSelect($scope.IABContextList, val[i]));
            }
            callback(results);
        }
    };
	
	$scope.selectCreative = {
        multiple: true,
        placeholder: "Select Creative",
        query: function (query) {
            var data = {results: []};
            angular.forEach($scope.creativeList, function(item, key){
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
                results.push(fillSelect($scope.creativeList, val[i]));
            }
            callback(results);
        }
    };
	
	$scope.selectState = {
	        multiple: true,
	        placeholder: "Select State",
	        query: function (query) {
	            var data = {results: []};
	            angular.forEach($scope.stateList, function(item, key){
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
	                results.push(fillSelect($scope.stateList, val[i]));
	            }
	            callback(results);
	        }
	    };

	$scope.loadPublisherProductData = function() {
		// Get IABContext List
		productFactory.getIABContext().then(
				function(iabContextData) {
					if (iabContextData == undefined || iabContextData == null
							|| (JSON.stringify(iabContextData)) == '[]') {
						console.log('No IABContext Data');
					} else {
						$scope.IABContextList = iabContextData;
						//if(productStatus != 'update') {$("#contextSelect").select2("val",[allOptionId]);}
					}
				});
		
		// Get Creatives List
		productFactory.getCreatives().then(
				function(creativeData) {
					if (creativeData == undefined || creativeData == null
							|| (JSON.stringify(creativeData)) == '[]') {
						console.log('No Creative Data');
					} else {
						$scope.creativeList = creativeData;
						//if(productStatus != 'update') {$("#creativeSelect").select2("val",[allOptionId]);}
					}
				});
		
		// Get Device List
		productFactory.getDevices().then(
				function(deviceData) {
					if (deviceData == undefined || deviceData == null
							|| (JSON.stringify(deviceData)) == '[]') {
						console.log('No Device Data');
					} else {
						$scope.deviceList = deviceData;
						//if(productStatus != 'update') {$("#deviceSelect").select2("val",[allOptionId]);}
					}
				});
		
		// Get DMA List
		productFactory.getDmas().then(
				function(dmaData) {
					if (dmaData == undefined || dmaData == null
							|| (JSON.stringify(dmaData)) == '[]') {
						console.log('No DMA Data');
					} else {
						$scope.dmaList = dmaData;
						//if(productStatus != 'update') {$("#geoSelect").select2("val",[allOptionId]);}
					}
				});
		
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
		
		// Get States
		productFactory.getStates().then(
				function(stateData) {
					if (stateData == undefined || stateData == null
							|| (JSON.stringify(stateData)) == '[]') {
						console.log('No State Data');
					} else {
						$scope.stateList = stateData;
						//if(productStatus != 'update') {$("#stateSelect").select2("val",[allOptionId]);}
					}
				});
	};
	
	// Retrieve data
	$scope.retrieveData = function() {
		var productId = $('#productId').val();
		var partnerId = $('#partnerId').val();
		productFactory.retrieve(productId, partnerId).then(
				function(productData) {
					if (productData == undefined || productData == null || (JSON.stringify(productData)) == '{}') {
						console.log('Product Data Retrieval failed');
					} else {
						//console.log(productData);
						$scope.productNameModel = productData.productName;
						productName = productData.productName;
						$scope.rateModel = productData.rate;
						$scope.noteModel = productData.note;
						$scope.networkIdModel = productData.publisherID;
						$scope.publisherNameModel = productData.publisherName;
						parentId = productData.adUnitID;
						retrievedContext = productData.contextList;
						retrievedCreatives = productData.creativeList;
						retrievedDevices = productData.deviceList;
						retrievedDMAs = productData.dmaList;
						retrievedPlatforms = productData.platformList;
						retrievedStates = productData.stateList;
						var selectedDeviceCapabilityIndex = $scope.getSelectedDeviceCapabilityIndex(productData.deviceCapability)
						$scope.deviceCapability = $scope.allDeviceCapabilities[selectedDeviceCapabilityIndex];
						$('#deviceCapabilitySelect').select2("val",selectedDeviceCapabilityIndex);
						/*if(productData.status || productData.status == 'true') {
							$scope.statusModel = 'true';
							$('#statusId').prop('checked', true);
							$('#statusId').attr('checked','checked');
							$('#uniform-statusId span').attr('class','checked');
						}*/
						if(productData.geoFencing || productData.geoFencing == 'true') {
							$scope.geoFencingModel = 'true';
							$('#geoFencingId').prop('checked', true);
							$('#geoFencingId').attr('checked','checked');
							$('#uniform-geoFencingId span').attr('class','checked');
						}
						if(productData.demographic || productData.demographic == 'true') {
							$scope.demographicModel = 'true';
							$('#demographicId').prop('checked', true);
							$('#demographicId').attr('checked','checked');
							$('#uniform-demographicId span').attr('class','checked');
						}
						if(productData.behaviour || productData.behaviour == 'true') {
							$scope.behaviourModel = 'true';
							$('#behaviourId').prop('checked', true);
							$('#behaviourId').attr('checked','checked');
							$('#uniform-behaviourId span').attr('class','checked');
						}
						cityJsonObj = productData.cityList;
						var jsonObj = [];
						if(cityJsonObj != undefined) {
							for(i=0; i<cityJsonObj.length; i++) {
								cityIds.push(cityJsonObj[i].id);
								var item = {};
								item ["id"] = cityJsonObj[i].id;
						        item ["name"] = cityJsonObj[i].name;
						        jsonObj.push(item);
							}
						}
				        $('#citySearch').select2("data",jsonObj);
				        
				        adUnitJsonObj = productData.adUnitList;
						var jsonObj = [];
						if(adUnitJsonObj != undefined) {
							for(i=0; i<adUnitJsonObj.length; i++) {
								adUnitIds.push(adUnitJsonObj[i].id);
								var item = {};
								item ["id"] = adUnitJsonObj[i].id;
						        item ["text"] = adUnitJsonObj[i].text;
						        jsonObj.push(item);
							}
						}
				        $('#adUnitSearch').select2("data",jsonObj);
				        
				        zipJsonObj = productData.zipList;
						var jsonObj = [];
						if(zipJsonObj != undefined) {
							for(i=0; i<zipJsonObj.length; i++) {
								zipIds.push(zipJsonObj[i].id);
								var item = {};
								item ["id"] = zipJsonObj[i].id;
						        item ["name"] = zipJsonObj[i].name;
						        jsonObj.push(item);
							}
						}
				        $('#zipSearch').select2("data",jsonObj);
				        
						$scope.isDataRetrieved = true;
					}
				});
	};
	
	// update context
	$scope.updateContext = function() {
		if (retrievedContext != undefined && retrievedContext != null && retrievedContext.length > 0) {
			var selectedIds = [];
			$.each(retrievedContext, function(index, value){
				selectedIds.push(value.id);
			});
			$("#contextSelect").select2("val",selectedIds);
		}
	};
	
	// update creatives
	$scope.updateCreative = function() {
		if (retrievedCreatives != undefined && retrievedCreatives != null && retrievedCreatives.length > 0) {
			var selectedIds = [];
			$.each(retrievedCreatives, function(index, value){
				selectedIds.push(value.id);
			});
			$("#creativeSelect").select2("val",selectedIds);
		}
	};
	
	// update device
	$scope.updateDevice = function() {
		if (retrievedDevices != undefined && retrievedDevices != null && retrievedDevices.length > 0) {
			var selectedIds = [];
			$.each(retrievedDevices, function(index, value){
				selectedIds.push(value.id);
			});
			$("#deviceSelect").select2("val",selectedIds);
		}
	};
	
	// update dma
	$scope.updateDma = function() {
		if (retrievedDMAs != undefined && retrievedDMAs != null && retrievedDMAs.length > 0) {
			var selectedIds = [];
			$.each(retrievedDMAs, function(index, value){
				selectedIds.push(value.id);
			});
			$("#geoSelect").select2("val",selectedIds);
			
		}
	};
	
	// update platform
	$scope.updatePlatform = function() {
		if (retrievedPlatforms != undefined && retrievedPlatforms != null && retrievedPlatforms.length > 0) {
			var selectedIds = [];
			$.each(retrievedPlatforms, function(index, value){
				selectedIds.push(value.id);
			});
			$("#platformSelect").select2("val",selectedIds);
		}
	};
	
	// update state
	$scope.updateState = function() {
		if (retrievedStates != undefined && retrievedStates != null && retrievedStates.length > 0) {
			var selectedIds = [];
			$.each(retrievedStates, function(index, value){
				selectedIds.push(value.id);
			});
			$("#stateSelect").select2("val",selectedIds);
		}
	};

	$scope.dmaChanged = function() {
		$scope.checkAllNoneOptions('geoSelect');
		isDmaChanged = true;
		isModified = true;
	};
	$scope.deviceChanged = function() {
		$scope.checkAllNoneOptions('deviceSelect');
		isDeviceChanged = true;
		isModified = true;
	};
	$scope.platformChanged = function() {
		$scope.checkAllNoneOptions('platformSelect');
		isPlatformChanged = true;
		isModified = true;
	};
	$scope.contextChanged = function() {
		$scope.checkAllNoneOptions('contextSelect');
		isContextChanged = true;
		isModified = true;
	};
	$scope.creativeChanged = function() {
		$scope.checkAllNoneOptions('creativeSelect');
		isCreativeChanged = true;
		isModified = true;
	};
	$scope.stateChanged = function() {
		$scope.checkAllNoneOptions('stateSelect');
		isStateChanged = true;
		isModified = true;
	};
	
	$scope.formatRate = function() {
		if($scope.rateModel != undefined) {
			var cellValue = $scope.rateModel;
	    	cellValue = cellValue.replaceAll('$','');
	    	cellValue = cellValue.replaceAll(' ','');
	    	if(cellValue != '' && !(isNaN(cellValue))) {
	    		cellValue = '$'+formatFloat(cellValue,2);
	    		$scope.rateModel = cellValue;
	    	}
		}
	};
	
	$scope.modified = function(val) {
		isModified = val;
	};
	
	$scope.isNameAvailable = function() {
		var val = $scope.productNameModel;
		$('textValueAvailableImage').hide();
		if(val != undefined && val != null && $.trim(val) != '') {
			productFactory.isNameAvailable($.trim(val), $('#productId').val(), $scope);
		}
	};
	
	function getDmaArray() {
		if(isDmaChanged) {
			var val = $('#geoSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				dmaObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.dmaList.length; j++) {
						if($scope.dmaList[j].id == arr[i]) {
							dmaObject.push($scope.dmaList[j]);
						}
					}
				}
			}
			isDmaChanged = false;
		}
		return dmaObject;
	};
	function getDeviceArray() {
		if(isDeviceChanged) {
			var val = $('#deviceSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				deviceObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.deviceList.length; j++) {
						if($scope.deviceList[j].id == arr[i]) {
							deviceObject.push($scope.deviceList[j]);
						}
					}
				}
			}
			isDeviceChanged = false;
		}
		return deviceObject;
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
	function getContextArray() {
		if(isContextChanged) {
			var val = $('#contextSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				contextObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.IABContextList.length; j++) {
						if($scope.IABContextList[j].id == arr[i]) {
							contextObject.push($scope.IABContextList[j]);
						}
					}
				}
			}
			isContextChanged = false;
		}
		return contextObject;
	};
	function getCreativeArray() {
		if(isCreativeChanged) {
			var val = $('#creativeSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				creativeObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.creativeList.length; j++) {
						if($scope.creativeList[j].id == arr[i]) {
							creativeObject.push($scope.creativeList[j]);
						}
					}
				}
			}
			isCreativeChanged = false;
		}
		return creativeObject;
	};
	function getStateArray() {
		if(isStateChanged) {
			var val = $('#stateSelect').val();
			if (val != undefined && val != null && $.trim(val) != '') {
				stateObject = [];
				var arr = val.split(',');
				for(i=0; i<arr.length; i++) {
					for(j=0; j<$scope.stateList.length; j++) {
						if($scope.stateList[j].id == arr[i]) {
							stateObject.push($scope.stateList[j]);
						}
					}
				}
			}
			isStateChanged = false;
		}
		return stateObject;
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
	
	$scope.saveData = function(isAutoSave) {
		if(!isProductNameAvailable || $scope.productNameModel == undefined || $.trim($scope.productNameModel) == '') {
			if(!isAutoSave) {
				$('#productNameId').focus();
				if(!isProductNameAvailable) {
					//toastr.error('Product name not available');
					$('#productAvailableMsg').html('Product Name not Available');
				}
				else {
					//toastr.error('Product name is mandatory');
					$('#productAvailableMsg').html('Product name is mandatory');
				}
			}
			return false;
		}
		if(!$scope.isDataRetrieved) {
			console.log('Data not retrieved yet, save not reqiured');
			return false;
		}
		if(isAutoSave && !isModified) {
			console.log('No changes made, save not reqiured');
			return false;
		}
		/*if(initSave) {		// to prevent initial save at creation time. 
			console.log('Initial save, not reqiured');
			initSave = false;
			isModified = false;
			return false;
		}*/
		isModified = false;
		$scope.productdata = {};
		$scope.productdata["productID"] = $('#productId').val();
		$scope.productdata["publisherID"] = $scope.networkIdModel;
		$scope.productdata["publisherName"] = $scope.publisherNameModel;
		$scope.productdata["partnerId"] = $('#partnerId').val();
		$scope.productdata["note"] = $scope.noteModel;
		$scope.productdata["productName"] = productName;
		var rate = $scope.rateModel.replaceAll('$','');
		rate - rate.replaceAll('.','');
		rate = rate.replaceAll(',','');
		$scope.productdata["rate"] = rate;
		//$scope.productdata["status"] = $scope.statusModel;
		$scope.productdata["geoFencing"] = $scope.geoFencingModel;
		$scope.productdata["demographic"] = $scope.demographicModel;
		$scope.productdata["behaviour"] = $scope.behaviourModel;
		$scope.productdata["contextList"] = getContextArray();
		$scope.productdata["creativeList"] = getCreativeArray();
		$scope.productdata["deviceList"] = getDeviceArray();
		$scope.productdata["dmaList"] = getDmaArray();
		$scope.productdata["platformList"] = getPlatformArray();
		$scope.productdata["cityList"] = getCityArray();
		$scope.productdata["stateList"] = getStateArray();
		$scope.productdata["adUnitList"] = getAdUnitArray();
		$scope.productdata["zipList"] = getZipArray();
		$scope.productdata["deviceCapability"] = $scope.deviceCapability.id;
		console.log("Saving...");
		console.log($scope.productdata);
		if(($scope.productdata.zipList != undefined && $scope.productdata.zipList.length > 0) ||
				($scope.productdata.stateList != undefined && $scope.productdata.stateList.length > 0) || 
				($scope.productdata.cityList != undefined && $scope.productdata.cityList.length > 0) ||
				($scope.productdata.dmaList != undefined && $scope.productdata.dmaList.length > 0)) {
			$scope.productdata["isGeoAll"] = false;
		}else {
			$scope.productdata["isGeoAll"] = true;
		}
		productFactory.save($scope.productdata, isAutoSave);
	};
	
	$scope.checkAllNoneOptions = function(dropDownId) {
		var val = $('#'+dropDownId).val();
		if (val != undefined && val != null && (val.indexOf(allOptionId) >= 0 || val.indexOf(noneOptionId) >= 0) && val.indexOf(',') >= 0) {
			var arr = [];
			if(val.indexOf(allOptionId) >= 0) {
				arr.push(allOptionId);
			}else {
				arr.push(noneOptionId);
			}
			$("#"+dropDownId).select2("val",arr);
		}
	};

	/*// watch IAB for changes
	$scope.$watch('IABContextList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedIABList = nv.map(function(iabContextGrp) {
				return iabContextGrp;
			});
			$scope.modified(true);
		}
	}, true);
	// watch Creatives for changes
	$scope.$watch('creativeList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedCreativeList = nv.map(function(creativeFormatGrp) {
				return creativeFormatGrp;
			});
			$scope.modified(true);
		}
	}, true);
	// watch Devices for changes
	$scope.$watch('deviceList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedDeviceList = nv.map(function(device) {
				return device;
			});
			$scope.modified(true);
		}
	}, true);
	// watch DMAs for changes
	$scope.$watch('dmaList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedDmaList = nv.map(function(dma) {
				return dma;
			});
			$scope.modified(true);
		}
	}, true);
	// watch Platforms for changes
	$scope.$watch('platformList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedPlatformList = nv.map(function(platform) {
				return platform;
			});
			$scope.modified(true);
		}
	}, true);
	// watch cities for changes
	$scope.$watch('cityList|filter:{selected:true}', function(nv) {
		if(nv != undefined) {
			$scope.selectedCityList = nv.map(function(city) {
				return city;
			});
			$scope.modified(true);
		}
	}, true);*/

	// refresh div data
	$rootScope.safeApply = function(fn) {
		var phase = this.$root.$$phase;
		if (phase == '$apply' || phase == '$digest') {
			if (fn) {
				fn();
			}
		} else {
			this.$apply(fn);
		}
	};

});

productApp.directive('ngBlur', function () {
    return function (scope, elem, attrs) {
        elem.bind('blur', function () {
            scope.$apply(attrs.ngBlur);
        });
    };
});



// New Module
var page = 0;
var productIds = [];
var limit = 25;
var searchKeyword = '';

var productSetupApp = angular.module('productSetupApp', [ 'ngRoute', 'ui.bootstrap' ]);

// Config + Route
productSetupApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/productList', {
		controller : 'productSetupController',
		templateUrl : 'productList.html'
	}).otherwise({
		redirectTo : '/productList'
	});

} ]);

// Factory
productSetupApp.factory('productSetupFactory', function($http) {
	return { 
		getProducts: function() {
			return $http.get('/loadAllProducts.lin?limit='+limit+'&offset='+page+'&searchKeyword='+searchKeyword).then(function(result) {
	    	 //console.log(result.data);
             return result.data;
                       });
       },
	
       deleteProduct: function(productId, partnerId) {
			return $http.get('/deleteProduct.lin?productId='+productId+'&partnerId='+partnerId).then(function(result) {
	    	 //console.log(result.data);
	         return result.data;
	                   });
	   }
	}
});

// Controller
productSetupApp.controller('productSetupController', function($scope, $filter, productSetupFactory) {
	if(page == 0) {
		$scope.products = [];
		productIds = [];
		page = page + 1;
	}
	if($scope.products == undefined) {
		$scope.products = [];
	}
	if($scope.predicate == undefined) {
		$scope.predicate = 'lastUpdate';
	}
	if($scope.reverse == undefined) {
		$scope.reverse = true;
	}
	
	$scope.loadProductSetupData = function() {
		try {
			productSetupFactory.getProducts().then(function(productData) {
	    		if(productData == undefined || productData == null || (JSON.stringify(productData)) == '[]') {
	    			console.log("No Data");
	    		}
	    		else {
    				$.each( productData, function(index, value) {
						if($.inArray(value.id, productIds) == -1) {	// if not found
    						$scope.products.push(value);
    						productIds.push(value.id);
						}
					});
    				console.log('productData : ');
					console.log(productData);
					page = page + 1;
	    		}
			});
			console.log('$scope.products : ');
			console.log($scope.products);
		}
		catch(err){
	    	console.log("productSetupFactory err : "+err);
	    }
	};
	
	$scope.getProductDetails = function(productId, partnerId) {
		$('#productId').val(productId);
		$('#partnerId').val(partnerId);
		document.productSetupForm.submit();
	};
	
	$scope.deleteProduct = function(productId, partnerId) {
		try {
			if(!confirm("Are you sure you want to delete this product ?")) {
				return false;
			}
			productSetupFactory.deleteProduct(productId, partnerId).then(function(isDeleted) {
	    		if(isDeleted == undefined || isDeleted == null || isDeleted == 'false') {
	    			console.log("Delete Failed");
	    			toastr.error('Delete Failed');
	    		}
	    		else if(isDeleted == 'true') {
	    			var index = -1;
	    			for(i=0; i<$scope.products.length; i++) {
	    				var obj = $scope.products[i];
	    				if(obj.id == productId) {
	    					//console.log("object to delete is at index : "+i);
	    					index = i;
	    					break;
	    				}
	    			}
	    			if(index >= 0) {
	    				$scope.products.splice(index, 1);		// remove 1 element at index
						//console.log("Delete Successful");
	    				toastr.success('Product deleted Successfully');
	    			}
	    		}
			});
		}
		catch(err){
	    	console.log("productSetupFactory err : "+err);
	    }
	};
	
	$scope.$watch("searchProduct",function(query){
		$scope.filterProducts = $filter("filter")($scope.products , query);
		var temp = searchKeyword;
		if(query != undefined ) {
			searchKeyword = query;
		}
		if(query != undefined && $.trim(query) != '' && query != temp && $scope.filterProducts.length < limit) {
			$scope.loadProductSetupData();
		}
	});
});
