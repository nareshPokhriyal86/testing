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
	/*
	 * This method get selected DMA and Product
	 * set as input parameter for SMP integration
	 * */
	$scope.generateCampaign = function() {
		var selectedLocation = [];
		if($scope.productPerformanceList.length == 0)
			return;
		else{
			for(var j = 0; j < $scope.productPerformanceList.length ; j++){
				var temp = {
						location : $scope.productPerformanceList[j].locationID,
						locationName : $scope.productPerformanceList[j].locationName
					};
				
				if(!checkArray(selectedLocation, "location", temp.location))
					selectedLocation.push(temp);
			}
		}
		
		var postVal = {
				age :$("#selectcensusAge option:selected").val(),
				income : $("#selectcensusIncome option:selected").val(),
				gender:$("#selectcensusGender option:selected").val(),
				ethnicity: $("#selectcensusEthnicity option:selected").val(), 
				education: $("#selectcensusEducation option:selected").val(),
				
				dmaList:[],  
				cityList: [],  
				zipList: [],  
				stateList : [], 
				
				rank : $("#slider-range").slider("value"),
				
				products: $scope.selectedProduct.toString()
		};
		
		for(var i=0;i<selectedLocation.length;i++){
			var dma = {
				id : selectedLocation[i].location,
				name : selectedLocation[i].locationName
			};
			postVal.dmaList.push(dma);
		}
		$("#formJsonParam").html(JSON.stringify(postVal));
		$("#campaignForm").submit();
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
		
		
		var geoGroup = $("#geoGroup").val();

		var selectcensusGender = $("#selectcensusGender option:selected").val();
		var selectcensusAge = $("#selectcensusAge option:selected");
		var selectcensusIncome = $("#selectcensusIncome option:selected");
		var selectcensusEducation = $("#selectcensusEducation option:selected");
		var selectcensusEthnicity = $("#selectcensusEthnicity option:selected");
		
		key = "";
		prodKey = geoGroup;

		var censusconfig = {
			ratio : $("#slider-range").slider("value"),
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
						console.log(data);
						currentRankKey = key;
						$("#processingBar").hide();
					}
				});

			} else {
				currentRankKey = key;
				//paintLayer(layerid, censusData,censusconfig.ratio);
				$("#processingBar").hide();
			}
	};
	
	// This method call when user slide rank, and this method calculate values accordingly
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
						
						if($.inArray($scope.mainProductPerformanceList[j].prodID,$scope.selectedProduct) == -1){
								$scope.selectedProduct.push($scope.mainProductPerformanceList[j].prodID);
						}
							$scope.productPerformanceList.push($scope.mainProductPerformanceList[j]);
						try{
							$scope.productTotal.impression += ($scope.mainProductPerformanceList[j].forecast[0].availableImps);
							$scope.productTotal.click = 0;
						}catch(e){
							
						}
					}
				}
			}
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
	initMap();
	$("#customInfoWindow").hide();
});
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