var Product = {
	// Basic info
	productID : 0,
	productName : "",
	publisherID : 0,
	publisherName : "",
	adUnitID : 0,
	adUnitName : "",
	note : "",

	status : true, // True if Product is available

	contextList : [], // List of IAB Context Objects
	creativeList : [], // list of available creative objects

	// GEO Based Properties
	countries : [], // list of countries objects. As on now only USA
	states : [], // list of states object based on countries. As of now only states of USA
	cities : [], // list of city objects
	zips: [], // list of city objects but unique zip
	dmas : [], // List of DMA
	
	platforms : [], // list of platform objects
	devices : [], // List of device objects
	geoFencing : false, // True if Support Geo Fencing
	demographic : false, // True if Support Any kind
	behaviour : false // True if Support Any kind
};

var IABContext = {
	id : 0,
	group : "", // News ... ref. to http://www.iab.net/QAGInitiative/overview/taxonomy
	subgroup : "" // Local News
};

var Creative = {
	id : 0,
	format : "", // static / video / rich media
	size : "" // 300x50 ... 
};

var Country = {
	id : 0,
	code : "", // USA
	text : "" // The United States of America
};

var State = {
	id : 0,
	countryID : 0,
	code : "", // NJ
	text : "" // New Jersey
};

var City = {
	id: 0,
	stateID : 0,
	zip : "",
	text : ""
};

var DMA = {
	id : 0,
	region : "",
	regionCode : 0,
	
};

var Platform = {
		id : 0,
		text : ""	//iOS , Android , Windows
};

var Device = {
		id : 0,
		text : ""	// Smartphone , TV , Tablet
};

