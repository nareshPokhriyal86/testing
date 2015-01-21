/**
 * Author : Anup Dutta 
 * Version : 1.0
 * AngularJS Module : MapExplorerApp
 * Description : This module embed within Demo Map Explorer, and show Google Map with various census overlay
 * 				 with heatmap affect based their rank based on parameter. 
 */

// New Module
var mapexplorerApp = angular.module('mapexplorerApp', [ 'ngRoute' ]);

// Factory
mapexplorerApp.factory('mapexplorerFactory', function($http) {
	var factory = {};

	factory.getProductPerformance = function(scope) {
		scope.isCalculatingForecast = true;
	    $http.get('/productForecastPerformance.lin?groupby='+duration).then(function(result) {
			scope.isCalculatingForecast = false;
			scope.mainProductPerformanceList = result.data;
			scope.productPerformanceList = result.data;
			scope.getTotal();
		});
	};
	return factory;
});

// Controller
mapexplorerApp.controller('mapexplorerController',function($scope, $http, mapexplorerFactory) {
	
	$scope.currentLocation = {
			type: "",
			name: "",
			rank : 0,
			age : 0,
			ethnicity : 0,
			income : 0,
			education:0,
			
			agetxt : "",
			ethnicitytxt : "",
			incometxt : "",
			educationtxt:""
	};
	$scope.isCalculatingForecast = false;
	$scope.mainProductPerformanceList = [];
	$scope.productPerformanceList = [];
	$scope.selectedLocation = [];
	$scope.selectedProduct = [];
	
	$scope.productTotal = {
		impression : 0,
		click : 0
	};
	
	$scope.setVal = function(obj,type) {
		$scope.currentLocation.type = type;
		$scope.currentLocation.name = obj.locationName;
		$scope.currentLocation.rank = obj.rank;
		$scope.currentLocation.totalpop = obj.totalPop;
		
		$scope.currentLocation.ageTot = 0;
		$scope.currentLocation.ethnicityTot = 0;
		$scope.currentLocation.incomeTot = 0;
		$scope.currentLocation.educationTot =0;
		
		
		for(var i=0;i<obj.detail.length;i++){
			switch(obj.detail[i].type){
				case "age": 
					$scope.currentLocation.age = obj.detail[i].val;
					$scope.currentLocation.ageTot = obj.detail[i].tot;
					break;
				case "education":
					$scope.currentLocation.education = obj.detail[i].val;
					$scope.currentLocation.educationTot = obj.detail[i].tot;
					break;
				case "income":
					$scope.currentLocation.income = obj.detail[i].val;
					$scope.currentLocation.incomeTot = obj.detail[i].tot;
					break;
				case "ethnicity":
					$scope.currentLocation.ethnicity = obj.detail[i].val;
					$scope.currentLocation.ethnicityTot = obj.detail[i].tot;
					break;
			}
		}
		
		$scope.getProductDetailByLocation(obj.location);
		
		$scope.$apply();
	};

	$scope.getProductDetailByLocation = function(locationID){
		var availProd = [];
		var totAvail = 0;
		
		for(var i = 0;i<$scope.productPerformanceList.length;i++){
			if($scope.productPerformanceList[i].locationID == locationID){
				if($.inArray($scope.productPerformanceList[i].prodID,availProd) == -1){
					availProd.push($scope.productPerformanceList[i].prodID);
				}
				try{
					totAvail += $scope.productPerformanceList[i].forecast[0].availableImps;
				}catch(e){
					
				}
			}
		}
		
		$scope.isCurrentLocData = (availProd.length > 0) ;
		$scope.currentLocProdCount = availProd.length;
		$scope.currentLocProdImps = totAvail;
		
	};

	/* This method get all census parameter,
	 * create a JSON passback value for service in response service send back rank and 
	 * plot it using Geo JSON layer 
	*/
	$scope.calculateRank = function() {
		
		$scope.currentLocation.genderTxt = "";
		
		switch($("#selectcensusGender option:selected").val()){
			case "male":
			case "MALE":
				$scope.currentLocation.genderTxt = "Male";
				break;
			
			case "female":
			case "FEMALE":
				$scope.currentLocation.genderTxt = "Female";
				break;
		}
		
		$scope.currentLocation.agetxt = $("#selectcensusAge option:selected").val();
		$scope.currentLocation.incometxt = $("#selectcensusIncome option:selected").val();
		$scope.currentLocation.educationtxt = $("#selectcensusEducation option:selected").val();
		$scope.currentLocation.ethnicitytxt = $("#selectcensusEthnicity option:selected").val();
		
		$scope.currentLocation.isage = $("#selectcensusAge option:selected").val()==""?false:true;
		$scope.currentLocation.isincome = $("#selectcensusIncome option:selected").val()==""?false:true;
		$scope.currentLocation.iseducation = $("#selectcensusEducation option:selected").val()==""?false:true;
		$scope.currentLocation.isethnicity = $("#selectcensusEthnicity option:selected").val()==""?false:true;
		
		
		var geoGroup = "dma";//$("#geoGroup").val();

		var selectcensusGender = $("#selectcensusGender option:selected").val();
		var selectcensusAge = $("#selectcensusAge option:selected");
		var selectcensusIncome = $("#selectcensusIncome option:selected");
		var selectcensusEducation = $("#selectcensusEducation option:selected");
		var selectcensusEthnicity = $("#selectcensusEthnicity option:selected");
		
		key = "";
		prodKey = geoGroup;
		
		if(isNaN($("#rangeVal").val())){
			$("#rangeVal").val(50);
		}else{
			if($("#rangeVal").val() < 0){
				$("#rangeVal").val(0);
			}
			if($("#rangeVal").val() > 100){
				$("#rangeVal").val(100);
			}
		}
		
		var censusconfig = {
			ratio : $("#rangeVal").val(),
			rankBy : geoGroup,
			gender : selectcensusGender,
			census : [],
		};

		if(selectcensusAge.val() != ""){
			var censusProperty;
			if(selectcensusGender == "male"){
				key += "_male";
				censusProperty = {
						actualColumn : selectcensusAge.attr("bqMaleCol"),
						parentColumn : selectcensusAge.attr("bqParentCol"),
				};
			}else if(selectcensusGender == "female"){
				key += "_female";
				censusProperty = {
						actualColumn : selectcensusAge.attr("bqFemaleCol"),
						parentColumn : selectcensusAge.attr("bqParentCol"),
				};
			}else{
				key += "_all";
				censusProperty = {
						actualColumn:selectcensusAge.attr("bqColumn"),
						parentColumn:selectcensusAge.attr("bqParentCol"),
				};
			}
			key += censusProperty.actualColumn;
			censusProperty.type = "age";
			censusconfig.census.push(censusProperty);
		}
		
		if(selectcensusIncome.val() != ""){
			var censusProperty;
				key += "_all";
				censusProperty = {
						actualColumn:selectcensusIncome.attr("bqColumn"),
						parentColumn:selectcensusIncome.attr("bqParentCol"),
				};
			
			key += censusProperty.actualColumn;
			censusProperty.type = "income";
			censusconfig.census.push(censusProperty);
		}
		
		if(selectcensusEthnicity.val() != ""){
			var censusProperty;
			if(selectcensusGender == "male"){
				key += "_male";
				censusProperty = {
						actualColumn : selectcensusEthnicity.attr("bqMaleCol"),
						parentColumn : selectcensusEthnicity.attr("bqParentCol"),
				};
			}else if(selectcensusGender == "female"){
				key += "_female";
				censusProperty = {
						actualColumn : selectcensusEthnicity.attr("bqFemaleCol"),
						parentColumn : selectcensusEthnicity.attr("bqParentCol"),
				};
			}else{
				key += "_all";
				censusProperty = {
						actualColumn:selectcensusEthnicity.attr("bqColumn"),
						parentColumn:selectcensusEthnicity.attr("bqParentCol"),
				};
			}
			key += censusProperty.actualColumn;
			censusProperty.type = "ethnicity";
			censusconfig.census.push(censusProperty);
		}
		
		if(selectcensusEducation.val() != ""){
			var censusProperty;
			if(selectcensusGender == "male"){
				key += "_male";
				censusProperty = {
						actualColumn : selectcensusEducation.attr("bqMaleCol"),
						parentColumn : selectcensusEducation.attr("bqParentCol"),
				};
			}else if(selectcensusGender == "female"){
				key += "_female";
				censusProperty = {
						actualColumn : selectcensusEducation.attr("bqFemaleCol"),
						parentColumn : selectcensusEducation.attr("bqParentCol"),
				};
			}else{
				key += "_all";
				censusProperty = {
						actualColumn:selectcensusEducation.attr("bqColumn"),
						parentColumn:selectcensusEducation.attr("bqParentCol"),
				};
			}
			key += censusProperty.actualColumn;
			censusProperty.type = "education";
			censusconfig.census.push(censusProperty);
		}
		
			if(censusconfig.census.length == 0){
				toastr.warning("Please select an option from Age, Education, Ethnicity or Income.");
				return;
			}
			
			if (isLayerLoaded == false) {
				isLayerLoaded = true;
				map.data.loadGeoJson('dma.json');
			}
			
			var censusData = getCensusCacheData(key);
			$("#processingBar").show();
			if (censusData == null) {
				$.ajax({
					url : "getLocationRank.lin",
					data : "censusconfig="+ JSON.stringify(censusconfig),
					crossDomain : true,
					success : function(data) {
						//paintLayer(layerid, data,censusconfig.ratio);
						dynamicLayerStyle(data, censusconfig.ratio);
						census_rank.push({"key" : key,"data" : data});
						currentRankKey = key;
						$("#processingBar").hide();
					},
					error : function(data) {
						currentRankKey = key;
						$("#processingBar").hide();
					}
				});

			} else {
				currentRankKey = key;
				$("#processingBar").hide();
			}
	};
	
	// This method call when user set rank, and this method calculate values accordingly
	$scope.updateProductPerformance = function(data,ratio){
		$scope.productTotal = {
				impression : 0,
				click : 0
		};
		$scope.selectedLocation = [];
		$scope.productPerformanceList = [];
		$scope.selectedProduct = [];
		
		for(var i=0;i<data.length;i++){
			if(data[i].rank >= ratio){
				$scope.selectedLocation.push(data[i]);
				
				
				for(var j=0;j<$scope.mainProductPerformanceList.length;j++){

						if(data[i].location == $scope.mainProductPerformanceList[j].locationID){
							//Check for Creatives
							if(prodCreativeArry == null || prodCreativeArry.length == 0 || checkCreativeList(prodCreativeArry,$scope.mainProductPerformanceList[j]))
							{
								//Check for Platform
								if(prodPlatformArry == null || prodPlatformArry.length == 0 || checkPlatformList(prodPlatformArry,$scope.mainProductPerformanceList[j]))
								{
									//Check for Devices
									if(prodDeviceArry == null || prodDeviceArry.length == 0 || checkDeviceList(prodDeviceArry,$scope.mainProductPerformanceList[j]))
									{
										if($.inArray($scope.mainProductPerformanceList[j].prodID,$scope.selectedProduct) == -1){
											$scope.selectedProduct.push($scope.mainProductPerformanceList[j].prodID);
										}
										$scope.productPerformanceList.push($scope.mainProductPerformanceList[j]);
										try{
											$scope.productTotal.impression += ($scope.mainProductPerformanceList[j].forecast[0].availableImps);
											$scope.productTotal.click = 0;
										}catch(e){}
									}
									
								}
							
							}
							
						}
					
				}
				
				
			}
		}
		
		$("#selectedProduct").val($scope.selectedProduct.toString());
		
		var selectedLocation = [];
		if($scope.productPerformanceList.length != 0)
		{
			for(var j = 0; j < $scope.productPerformanceList.length ; j++){
				var locationID = $scope.productPerformanceList[j].locationID;
				
				if($.inArray(locationID,selectedLocation) == -1){
					selectedLocation.push(locationID);
				}
				
			}
		}
		
		//Creative
		if(isUserOptforCreative){
			prodCreativeArry = [];
			for(var j = 0; j < $scope.productPerformanceList.length ; j++){
				for(var k=0;k<$scope.productPerformanceList[j].creative.length;k++){
					var creativeID = $scope.productPerformanceList[j].creative[k].id;
					if($.inArray(creativeID,prodCreativeArry) == -1){
						prodCreativeArry.push(creativeID);
					}
				}
			}
		}
		
		//Platform
		if(isUserOptforPlatform){
			prodPlatformArry = [];
			for(var j = 0; j < $scope.productPerformanceList.length ; j++){
				for(var k=0;k<$scope.productPerformanceList[j].platform.length;k++){
					var platformID = $scope.productPerformanceList[j].platform[k].id;
					if($.inArray(platformID,prodPlatformArry) == -1){
						prodPlatformArry.push(platformID);
					}
				}
			}
			
			if($.inArray(allOptionId,prodPlatformArry) != -1){
				prodPlatformArry = [];
				prodPlatformArry.push(allOptionId);
			}
			
			if( $.inArray(noneOptionId,prodPlatformArry) != -1){
				prodPlatformArry = [];
				prodPlatformArry.push(noneOptionId);
			}
		}
		
		//device
		if(isUserOptforDevice){
			prodDeviceArry = [];
			for(var j = 0; j < $scope.productPerformanceList.length ; j++){
				for(var k=0;k<$scope.productPerformanceList[j].device.length;k++){
					var deviceID = $scope.productPerformanceList[j].device[k].id;
					if($.inArray(deviceID,prodDeviceArry) == -1){
						prodDeviceArry.push(deviceID);
					}
				}
			}
			
			if($.inArray(allOptionId,prodDeviceArry) != -1){
				prodDeviceArry = [];
				prodDeviceArry.push(allOptionId);
			}
			
			if( $.inArray(noneOptionId,prodDeviceArry) != -1){
				prodDeviceArry = [];
				prodDeviceArry.push(noneOptionId);
			}
		}
		
		
		$("#geoSelect").select2("val",selectedLocation);
		$("#selectedDMAList").select2("val",selectedLocation);
		
		
		$("#deviceSelect").select2("val",prodDeviceArry);
		$("#platformSelect").select2("val",prodPlatformArry);
		$("#creativeId").select2("val",prodCreativeArry);
		
		var defaultStartDate = new Date();
		var defaultEndDate = new Date(defaultStartDate);
		defaultEndDate.setDate(defaultStartDate.getDate()+30);
		
		if($("#cStartDate").val() == ""){
			$("#cStartDate").val(formatDate(defaultStartDate));
			$("#cStartDate").change();
		}
		
		if($("#cEndDate").val() == ""){
			if($("#cStartDate").val() != ""){
				defaultEndDate = new Date(parseDate($("#cStartDate").val(),'mm-dd-yyyy'));
				defaultEndDate.setDate(defaultEndDate.getDate()+30);
			}
			
			$("#cEndDate").val(formatDate(defaultEndDate));
			$("#cEndDate").change();
		}

		
		if(prodCreativeArry.length>0){
			$("#creativeError").hide();
		}
		$scope.$apply();
	};
	
	$scope.getTotal = function(){
		for(var j=0;j<$scope.productPerformanceList.length;j++){
			try{
				$scope.productTotal.impression += ($scope.productPerformanceList[j].forecast[0].availableImps);
				$scope.productTotal.click = 0;
			}catch(e){
			}
		}
		$scope.$apply();
	};
	
	mapexplorerFactory.getProductPerformance($scope);
	
	$("#customInfoWindowSMP").hide();
});

// Date Formator
function parseDate(input, format) {
	  format = format || 'yyyy-mm-dd'; // default format
	  var parts = input.match(/(\d+)/g), 
	      i = 0, fmt = {};
	  // extract date-part indexes from the format
	  format.replace(/(yyyy|dd|mm)/g, function(part) { fmt[part] = i++; });
	  return new Date(parts[fmt['yyyy']], parts[fmt['mm']]-1, parts[fmt['dd']]);
}


function formatDate(d){
	  function addZero(n){
	     return n < 10 ? '0' + n : '' + n;
	  }
	  return addZero(d.getMonth()+1)+"-"+ addZero(d.getDate()) + "-" + d.getFullYear();
}
// Method to check data in array
function checkArray(arr, key, value) {
	var isExist = false;
	for (var i = 0; i < arr.length; i++) {
		if (arr[i][key] == value) {
			isExist = true;
			break;
		}
	}
	return isExist;
}