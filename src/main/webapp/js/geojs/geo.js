var map;

var census_rank = [];
var productPerformance = [];
var key;
var prodKey;
var currentRankKey ;
var duration = "month";


var markers = [];
var prodMarkerArry = [];

/// Geo JSON Addons
var isLayerLoaded = false;
var currentData = null;
var currentRatio = null;

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

// First method called after page get loaded
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

	map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
	map.mapTypes.set('map_style', styledMap);
	map.setMapTypeId('map_style');
	
	// Added default style property of GeoJson Layer
	map.data.setStyle(defaultLayerStyle);
	
	// Bind Click event with GeoJson layer 
	map.data.addListener('click', function(event) {
		console.log(event);
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
	
	/*
	var input = (document.getElementById('pac-input'));
	var searchBox = new google.maps.places.SearchBox((input));
	
	var bounds = map.getBounds();
    searchBox.setBounds(bounds);
    
	google.maps.event.addListener(searchBox, 'places_changed', function() {
	    var places = searchBox.getPlaces();

	    if (places.length == 0) {
	      return;
	    }
	    for (var i = 0, marker; marker = markers[i]; i++) {
	      marker.setMap(null);
	    }

	    // For each place, get the icon, place name, and location.
	    markers = [];
	    var bounds = new google.maps.LatLngBounds();
	    for (var i = 0, place; place = places[i]; i++) {
	      var image = {
	        url: place.icon,
	        size: new google.maps.Size(71, 71),
	        origin: new google.maps.Point(0, 0),
	        anchor: new google.maps.Point(17, 34),
	        scaledSize: new google.maps.Size(25, 25)
	      };

	      // Create a marker for each place.
	      var marker = new google.maps.Marker({
	        map: map,
	        icon: image,
	        title: place.name,
	        position: place.geometry.location
	      });

	      markers.push(marker);

	      //bounds.extend(place.geometry.location);
	    }

	    //map.fitBounds(bounds);
	  });
	  */
}

// This method remove all the markers which was generated by place API
function clearMarker(){
	for (var i = 0, marker; marker = markers[i]; i++) {
	      marker.setMap(null);
	    }
	 markers = [];
	 $("#pac-input").val("");
}

function clearProdMarker(){
	var i = 0;
	var prodMarker;
	for (; prodMarker = prodMarkerArry[i]; i++) {
		prodMarker.setMap(null);
	}
	prodMarkerArry = [];
}

// This method get Census Data which already being pulled up and stored on browser cache
function getCensusCacheData(key) {
	for (var i = 0; i < census_rank.length; i++) {
		var rec = census_rank[i];
		if (rec.key == key)
			return rec.data;
	}
	return null;
}

// This method get product data which pull
function getProductCacheData(key) {
	for (var i = 0; i < productPerformance.length; i++) {
		var rec = productPerformance[i];
		if (rec.key == key)
			return rec.data;
	}
	return null;
}

function getProductLocationData(location, data) {
	for (var i = 0; i < data.length; i++) {
		var rec = data[i];
		if (rec.location == location)
			return rec;
	}
	return null;
}

//This method is being called for styling Geo JSON 

function dynamicLayerStyle(data, ratio){
	 clearProdMarker();
	 currentData = data;
	 currentRatio = ratio;
	 angular.element(document.getElementById('mapExplorer')).scope().updateProductPerformance(data, ratio);
	 
	var dynaStyle = function(feature){
		var fillC = "white";
		var fillO = 0.0;
		var strokeC = "white";
		var strokeO = "0.0";
		var strokeW = 0;
		
		var prodData = angular.element(document.getElementById('mapExplorer')).scope().productPerformanceList;
		var currentLocation = feature.getProperty('dma');
		
		var centerLat = feature.getProperty('latitude');
		var centerLong = feature.getProperty('longitude');
		
		var currentLocationData = null;
		
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

// Handle Slider
function sliderChange() {
	var censusData = getCensusCacheData(key);
	$("#sliderValue").html($("#slider-range").slider("value"));
	//paintLayer(1002, censusData, $("#slider-range").slider("value"));
	dynamicLayerStyle(censusData, $("#slider-range").slider("value"));
}

function onSlide() {
	$("#sliderValue").html($("#slider-range").slider("value"));
}

function applyRule(id){
	$('#selectcensusGender').prop('disabled', false);
	$('#selectcensusAge').prop('disabled', false);
	
	//Education Selected
	var selectcensusEducation = $("#selectcensusEducation option:selected").val();
	if(selectcensusEducation.indexOf("18+") > -1){
		$('#selectcensusAge  option[value="18+"]').prop('selected', true);
		$('#selectcensusAge').attr('disabled', true);
	}
	
	if(selectcensusEducation.indexOf("25+") > -1){
		$('#selectcensusAge  option[value="25+"]').prop('selected', true);
		$('#selectcensusAge').attr('disabled', true);
	}
	
	var isAgeGenderCheck = $("#selectcensusAge option:selected").attr("gender") == "true"?true:false;
	var isIncomeGenderCheck = $("#selectcensusIncome option:selected").attr("gender") == "true"?true:false;
	var isEducationGenderCheck = $("#selectcensusEducation option:selected").attr("gender") == "true"?true:false;
	var isEthnicityGenderCheck = $("#selectcensusEthnicity option:selected").attr("gender") == "true"?true:false;
	
	if(!isAgeGenderCheck || !isIncomeGenderCheck || !isEducationGenderCheck || !isEthnicityGenderCheck){
		$('#selectcensusGender  option[value="all"]').prop('selected', true);
		$('#selectcensusGender').prop('disabled', true);
	}
	
}

function getInforWindowData(locationId,type){
	var censusData = getCensusCacheData(currentRankKey);
	for(var i = 0; i < censusData.length; i++){
		var record = censusData[i];
		if(record.location == locationId){
			angular.element(document.getElementById('mapExplorer')).scope().setVal(record,type);
			break;
		}
	}
}

function showCustomInfoWindow(position){
	try{
		var overlay = new google.maps.OverlayView();
		overlay.draw = function() {};
		overlay.setMap(map);
	
		var proj = overlay.getProjection();
		var pos = position;
		var p = proj.fromLatLngToContainerPixel(pos);
		
		$("#customInfoWindow").css('top', p.y);
		$("#customInfoWindow").css('left', p.x+15);
		
		$("#customInfoWindow").show();
	}
	catch(e){}
}
function hideCustomInfoWindow(){
	$("#customInfoWindow").hide();
}

