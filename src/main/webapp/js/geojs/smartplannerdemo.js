/**
 * author : Anup Dutta
 * description : This module is integrate Demo Explorer in Smart Media Planner
 */

var map = null;

/// Geo JSON Addons
var isLayerLoaded = false;
var currentData = null;
var currentRatio = null;

var duration = "month";
var census_rank = [];
var currentRankKey ;
var key;

var prodMarkerArry = [];

// Product Properties
var prodCreativeArry = [];
var prodPlatformArry = [];
var prodDeviceArry = [];

// User Preference
var isUserOptforCreative = true;
var isUserOptforDevice = true;
var isUserOptforPlatform = true;

// Default property for polygons
var defaultLayerStyle = {
		fillColor : "white",
		fillOpacity : 0.0,
		strokeColor : "white",
		strokeOpacity : "0.0",
		strokeWeight : 0
};

// Map styling property
var mapStyle = [ {
	"featureType" : "road",
	"stylers" : [ {
		"visibility" : "off"
	} ]
}, {
	"featureType" : "poi",
	"stylers" : [ {
		"visibility" : "off"
	} ]
}, {
	"featureType" : "transit",
	"stylers" : [ {
		"visibility" : "off"
	} ]
}, {
	"featureType" : "administrative.locality",
	"stylers" : [ {
		"visibility" : "off"
	} ]
}, {
	"featureType" : "landscape.natural",
	"stylers" : [ {
		"color" : "#ffffff"
	} ]
}, {
	"featureType" : "water",
	"stylers" : [ {
		"visibility" : "simplified"
	}, {
		"color" : "#cdcdcd"
	} ]
} ];

var imageIcon = {
        url: "img/marker/click-action-Y.png",
        size: new google.maps.Size(24, 24),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(24, 10),
        //scaledSize: new google.maps.Size(32, 32)
};

//First method called after page get loaded
function initMap() {
	var styledMap = new google.maps.StyledMapType(mapStyle, {
		name : "Styled Map"
	});

	// Create a new Google Maps API Map
	var mapOptions = {
		center : new google.maps.LatLng(37.6, -95.665),
		zoom : 5,
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		panControl : false,
		mapTypeControl : false,
		streetViewControl : false,
		zoomControl : false,
	};

	map = new google.maps.Map(document.getElementById('smpDemoMap'), mapOptions);
	map.mapTypes.set('map_style', styledMap);
	map.setMapTypeId('map_style');
	
	// Added default style property of GeoJson Layer
	map.data.setStyle(defaultLayerStyle);
	
	// Bind Click event with GeoJson layer 
	map.data.addListener('click', function(event) {
	});
	
	// Bind Mouseover event with GeoJson layer
	map.data.addListener('mouseover', function(event) {
		var locationId = event.feature.getProperty('dma');
		getInforWindowData(locationId,"DMA");
		showCustomInfoWindow(event.latLng);
	});
	
	
	// Bind MouseOut event with GeoJson layer
	map.data.addListener('mouseout', function(event) {
		hideCustomInfoWindow();
	});
	
	angular.element(document.getElementById('demoModel')).scope().calculateRank();
}

function applyRule(section){
	
	if(section == "main"){
		$("#selectcensusGenderMap option[value='"+$("#selectcensusGender option:selected").val()+"']").prop('selected', true);
		$("#selectcensusAgeMap option[value='"+$("#selectcensusAge option:selected").val()+"']").prop('selected', true);
		$("#selectcensusIncomeMap option[value='"+$("#selectcensusIncome option:selected").val()+"']").prop('selected', true);
		$("#selectcensusEducationMap option[value='"+$("#selectcensusEducation option:selected").val()+"']").prop('selected', true);
		$("#selectcensusEthnicityMap option[value='"+$("#selectcensusEthnicity option:selected").val()+"']").prop('selected', true);
	}else{
		$("#selectcensusGender option[value='"+$("#selectcensusGenderMap option:selected").val()+"']").prop('selected', true);
		$("#selectcensusAge option[value='"+$("#selectcensusAgeMap option:selected").val()+"']").prop('selected', true);
		$("#selectcensusIncome option[value='"+$("#selectcensusIncomeMap option:selected").val()+"']").prop('selected', true);
		$("#selectcensusEducation option[value='"+$("#selectcensusEducationMap option:selected").val()+"']").prop('selected', true);
		$("#selectcensusEthnicity option[value='"+$("#selectcensusEthnicityMap option:selected").val()+"']").prop('selected', true);
	}
	
	$('#selectcensusGender').prop('disabled', false);
	$('#selectcensusAge').prop('disabled', false);
	
	$('#selectcensusGenderMap').prop('disabled', false);
	$('#selectcensusAgeMap').prop('disabled', false);
	
	//Education Selected
	var selectcensusEducation = $("#selectcensusEducation option:selected").val();
	
	if(selectcensusEducation.indexOf("18+") > -1){
		$('#selectcensusAge  option[value="18+"]').prop('selected', true);
		$('#selectcensusAge').prop('disabled', true);
		
		$('#selectcensusAgeMap  option[value="18+"]').prop('selected', true);
		$('#selectcensusAgeMap').prop('disabled', true);
	}
	
	if(selectcensusEducation.indexOf("25+") > -1){
		$('#selectcensusAge  option[value="25+"]').prop('selected', true);
		$('#selectcensusAge').prop('disabled', true);
		
		$('#selectcensusAgeMap  option[value="25+"]').prop('selected', true);
		$('#selectcensusAgeMap').prop('disabled', true);
	}
	
	var isAgeGenderCheck = $("#selectcensusAge option:selected").attr("gender") == "true"?true:false;
	var isIncomeGenderCheck = $("#selectcensusIncome option:selected").attr("gender") == "true"?true:false;
	var isEducationGenderCheck = $("#selectcensusEducation option:selected").attr("gender") == "true"?true:false;
	var isEthnicityGenderCheck = $("#selectcensusEthnicity option:selected").attr("gender") == "true"?true:false;
	
	if(!isAgeGenderCheck || !isIncomeGenderCheck || !isEducationGenderCheck || !isEthnicityGenderCheck){
		$('#selectcensusGender  option[value="all"]').prop('selected', true);
		$('#selectcensusGender').prop('disabled', true);
		
		$('#selectcensusGenderMap  option[value="all"]').prop('selected', true);
		$('#selectcensusGenderMap').prop('disabled', true);
	}
	
}

//Function to check census option selected
function checkOptionSelected(){
	var isAgeCheck = $("#selectcensusAge option:selected").val() == ""?false:true;
	var isIncomeCheck = $("#selectcensusIncome option:selected").val() == ""?false:true;
	var isEducationCheck = $("#selectcensusEducation option:selected").val() == ""?false:true;
	var isEthnicityCheck = $("#selectcensusEthnicity option:selected").val() == ""?false:true;
	
	return (isAgeCheck || isIncomeCheck || isEducationCheck || isEthnicityCheck);
		
}

//This method get Census Data which already being pulled up and stored on browser cache
function getCensusCacheData(key) {
	for (var i = 0; i < census_rank.length; i++) {
		var rec = census_rank[i];
		if (rec.key == key)
			return rec.data;
	}
	return null;
}

//This method is being called for styling Geo JSON 

function dynamicLayerStyle(data, ratio){
	 clearProdMarker();
	 currentData = data;
	 currentRatio = ratio;
	 angular.element(document.getElementById('demoModel')).scope().updateProductPerformance(data, ratio);
	 
	var dynaStyle = function(feature){
		var fillC = "white";
		var fillO = 0.0;
		var strokeC = "white";
		var strokeO = "0.0";
		var strokeW = 0;
		
		var prodData = angular.element(document.getElementById('demoModel')).scope().productPerformanceList;
		var currentLocation = feature.getProperty('dma');
		var currentLocationData = null;
		var centerLat = feature.getProperty('latitude');
		var centerLong = feature.getProperty('longitude');
		
		
		for(var i=0;i<currentData.length;i++){
			var record = currentData[i];
			if(record.location == currentLocation){
				currentLocationData = record;
				break;
			}
		}
		
		if(currentLocationData != null){
			if(currentLocationData.rank >= currentRatio){
				fillC = currentLocationData.color;
				fillO = 0.5;
				if(checkArray(prodData,"locationID",currentLocationData.location)){
					strokeC = 'black';
					strokeO = 0.8;
					strokeW = 1;
					
					// Create a marker for each place with product.
				      var marker = new google.maps.Marker({
				    	icon: imageIcon,
				        map: map,
				        position: new google.maps.LatLng(centerLat,centerLong)
				      });

				      prodMarkerArry.push(marker);
				}
			}
		}
		
		return ({
			fillColor : fillC,
			fillOpacity : fillO,
			strokeColor : strokeC,
			strokeOpacity : strokeO,
			strokeWeight : strokeW
		});
	};
	
	map.data.setStyle(dynaStyle);
}

// Get Data for current Location
function getInforWindowData(locationId,type){
	var censusData = getCensusCacheData(currentRankKey);
	for(var i = 0; i < censusData.length; i++){
		var record = censusData[i];
		if(record.location == locationId){
			angular.element(document.getElementById('demoModel')).scope().setVal(record,type);
			break;
		}
	}
}

// Hide InfoWindow
function hideCustomInfoWindow(){
	$("#customInfoWindowSMP").hide();
}

// Position Custom Info 
function showCustomInfoWindow(position){
	try{
		var overlay = new google.maps.OverlayView();
		overlay.draw = function() {};
		overlay.setMap(map);
	
		var proj = overlay.getProjection();
		var pos = position;
		var p = proj.fromLatLngToContainerPixel(pos);
		
		var boxW = $("#customInfoWindowSMP").width();
		var boxH = $("#customInfoWindowSMP").height();
		
		var modalW = $("#demoModel").width();
		var modalH = $("#demoModel").height();
		
		if((boxW + p.x + 15) > modalW){
			$("#customInfoWindowSMP").css('left', (p.x - boxW - 15));
		}else{
			$("#customInfoWindowSMP").css('left', p.x+15);
		}
		
		
		if((boxH + p.y + 15) > modalH){
			$("#customInfoWindowSMP").css('top', p.y - boxH);
		}else{
			$("#customInfoWindowSMP").css('top', p.y);
		}
		
		$("#customInfoWindowSMP").show();
		
	}
	catch(e){}
}

// On value change of rank 
function rankValueChange(){
	
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
	
	var censusData = getCensusCacheData(key);
	dynamicLayerStyle(censusData, $("#rangeVal").val());
}

//Clear all markers
function clearProdMarker(){
	var i = 0;
	var prodMarker;
	for (; prodMarker = prodMarkerArry[i]; i++) {
		prodMarker.setMap(null);
	}
	prodMarkerArry = [];
}


// Update Additional Properties 
function updateProductProperties(){
	 prodCreativeArry = [];
	 prodPlatformArry = [];
	 prodDeviceArry = [];
	 
	 var device =  $('#deviceSelect').val();
	 if(device != null){
		 for(var i=0; i<device.length;i++)
		 {
			 prodDeviceArry.push(parseInt(device[i]));
		 }
	 }
	
	 
	 var platform =  $('#platformSelect').val();
	 if(platform != null){
		 for(var i=0; i<platform.length;i++)
		 {
			 prodPlatformArry.push(parseInt(platform[i]));
		 }
	 }
	 
	 
	 var creative =  $('#creativeId').val();
	 if(creative != null){
		 for(var i=0; i<creative.length;i++)
		 {
			 prodCreativeArry.push(parseInt(creative[i]));
		 }
	 }
	 
	 try{
		 rankValueChange();
	 }catch(err){
		 
	 }
	 
	 
}

//function to check creative size within product
function checkCreativeList(formField, Product){
	if($.inArray(allOptionId,formField) != -1){
		return true;
	}
	
	var isAvail = false;
	for(var j=0;j<Product.creative.length;j++){
		if($.inArray(Product.creative[j].id,formField) != -1){
			isAvail = true;
			break;
		}
	}
	return isAvail;
}

//function to check Platform size within product
function checkPlatformList(formField, Product){
	if($.inArray(allOptionId,formField) != -1){
		return true;
	}
	
	var isAvail = false;
	for(var j=0;j<Product.platform.length;j++){
		if($.inArray(Product.platform[j].id,formField) != -1){
			isAvail = true;
			break;
		}
	}
	return isAvail;
}

//function to check Device size within product
function checkDeviceList(formField, Product){
	if($.inArray(allOptionId,formField) != -1){
		return true;
	}
	
	var isAvail = false;
	for(var j=0;j<Product.device.length;j++){
		if($.inArray(Product.device[j].id,formField) != -1){
			isAvail = true;
			break;
		}
	}
	return isAvail;
}

// method to set user preference
function setUserPref(id){
	
	var selectVal =  $('#'+id).val();
	
	if(selectVal != null && selectVal.length !=0){
		switch(id){
			case "creativeId":isUserOptforCreative = false;break;
			case "deviceSelect":isUserOptforDevice = false;break;
			case "platformSelect":isUserOptforPlatform = false;break;
		}
	}else{
		switch(id){
			case "creativeId":isUserOptforCreative = true;break;
			case "deviceSelect":isUserOptforDevice = true;break;
			case "platformSelect":isUserOptforPlatform = true;break;
		}
	}
}