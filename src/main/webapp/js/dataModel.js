var Campaign = {
	name : '',
	publisher : {
		name : "",
		address : "",
		state : "",
		state_short : "",
		zipcode : "",
		email : "",
		phone : "",
		fax : ""
	},
	advertiser : {
		name : "",
		address : "",
		state : "",
		state_short : "",
		zipcode : "",
		email : "",
		phone : "",
		fax : ""
	},
	gauranteed : true,
	status : 0, // 0: proposal , 1: contract , 2:delivering ,3: completed
	budget : 0.0,
	rate : 0.0,
	rate_type : 0,// 0 : CPM 1 : CPC etc.
	total_impression : 0,
	frequency_cap :"",
	ctr_goal : 0.0,
	notes : "",
	duration : {
		start_date : "",
		end_date : ""
	},
	
	ads : [], // will have creative Object . Max length of 3
	
	target : {
		device : [],// will have device object
		plateform:[],
		geographic : false, // will have geographic object
		contextual : false, // will have contextual object based upon IAB
		behavior : false,						// standard
		demographic : false
	},

	creative :[],
	flights : [] // will have flight object
};

var standardCreative = {
	format : "", // 0 : static , 1: rich media , 2: video
	size : [] // 300x250 , 300x50 ...
};

var richCreative = {
		format : "", // 0 : static , 1: rich media , 2: video
		size : [] // 300x250 , 300x50 ...
	};

var videoCreative = {
		format : "", // 0 : static , 1: rich media , 2: video
		size : [] // 300x250 , 300x50 ...
	};

var Audiance = {
		gender : "",
		age : {
			start : 0,
			end : 0
		}
};

var Flight = {
		date : {
			start : new Date(),
			end : new Date()
		},
		time : {
			start : "",
			end :""
		},
		impression : 0
};