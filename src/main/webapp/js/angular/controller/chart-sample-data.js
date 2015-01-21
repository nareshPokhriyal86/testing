'use strict';

angular.module('advertiser-view-app', ['googlechart']).controller("AdvertiserCtrl", function ($scope) {

    var chart1 = {};
    chart1.type = "LineChart";
    chart1.displayed = false;
    chart1.cssStyle = "height:400px; width:100%;";
    var data1 = {"cols": [
			        {id: "date", label: "Date", type: "string"},
			        {id: "lin.khon-id", label: "lin.khon", type: "number"},
			        {id: "lin.krqe-id", label: "lin.krqe", type: "number"},
			        {id: "lin.ksnt-id", label: "lin.ksnt", type: "number"},
			        {id: "lin.ksnw-id", label: "lin.ksnw", type: "number"},
			        {id: "lin.kxan-id", label: "lin.kxan", type: "number"},
			        {id: "lin.wala-id", label: "lin.wala", type: "number"},
			        {id: "lin.wane-id", label: "lin.wane", type: "number"},
			        {id: "lin.wavy-id", label: "lin.wavy", type: "number"},
			        {id: "lin.wdtn-id", label: "lin.wdtn", type: "number"},
			        {id: "lin.wiat-id", label: "lin.wiat", type: "number"},
			        {id: "lin.wjcl-id", label: "lin.wjcl", type: "number"},
			        {id: "lin.wwlp-id", label: "lin.wwlp", type: "number"}
        		], 
			    "rows": [
					{c:[{v:"2013-10-10"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-11"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-15"},{v:0.1491350169},{v:0.1514386673},{v:0.1941747573},{v:0.0689179876},{v:0.0826873385},{v:0},{v:0.0524952754},{v:0.1054481547},{v:0.1377884947},{v:0},{v:0},{v:0.0824628917}]},
					{c:[{v:"2013-10-16"},{v:0.1526884066},{v:0.1446968601},{v:0.2538071066},{v:0.0555864369},{v:0.0862520408},{v:0},{v:0.0753336203},{v:0.0684150513},{v:0.121901666},{v:0},{v:0.3067484663},{v:0.0862122052}]},
					{c:[{v:"2013-10-17"},{v:0.149750416},{v:0.1172463849},{v:0.1858736059},{v:0.1019021739},{v:0.064951132},{v:0},{v:0.0909936507},{v:0.1474684581},{v:0.1211754014},{v:0.3851091142},{v:0},{v:0.1210090291}]},
					{c:[{v:"2013-10-18"},{v:0.1341531582},{v:0.1112612255},{v:0.4827031376},{v:0.2780094523},{v:0.1165301436},{v:0},{v:0.0764044944},{v:0.1384767557},{v:0.1637073416},{v:0},{v:0},{v:0.1239157373}]},
					{c:[{v:"2013-10-19"},{v:0.094144229},{v:0.1009081736},{v:0.0905797101},{v:0.0710732054},{v:0.0451161741},{v:0},{v:0.0968992248},{v:0.1581920904},{v:0.0559284116},{v:0},{v:0},{v:0.1398444929}]},
					{c:[{v:"2013-10-20"},{v:0.1006947941},{v:0.1469063256},{v:0.3628447025},{v:0.1370801919},{v:0.0923809987},{v:0},{v:0.0643924427},{v:0.1237623762},{v:0.1081431816},{v:0.2590673575},{v:0},{v:0.1220256254}]},
					{c:[{v:"2013-10-21"},{v:0.1709626512},{v:0.0928433269},{v:0.5006257822},{v:0.1156737999},{v:0.1346680433},{v:0},{v:0.0718364564},{v:0.0552181115},{v:0.1081665765},{v:0},{v:0.3717472119},{v:0.0963133394}]},
					{c:[{v:"2013-10-22"},{v:0.0688231246},{v:0.1161440186},{v:0.5384063173},{v:0.2019182231},{v:0.0987209203},{v:0},{v:0.0736105999},{v:0.1929437707},{v:0.1455074573},{v:0},{v:0},{v:0.1235548495}]},
					{c:[{v:"2013-10-23"},{v:0.2060641743},{v:0.0636942675},{v:0},{v:0.406504065},{v:0.2312138728},{v:0},{v:0.1727115717},{v:0.185528757},{v:0.1179245283},{v:0},{v:0},{v:0.1314924392}]},
				]

    
    };
    
    
    var data2 = {"cols": [
						{id: "date", label: "Date", type: "string"},
						{id: "lin.khon-id", label: "lin.khon", type: "number"},
						{id: "lin.krqe-id", label: "lin.krqe", type: "number"},
						{id: "lin.ksnt-id", label: "lin.ksnt", type: "number"},
						{id: "lin.ksnw-id", label: "lin.ksnw", type: "number"},
						{id: "lin.kxan-id", label: "lin.kxan", type: "number"},
						{id: "lin.wala-id", label: "lin.wala", type: "number"},
						{id: "lin.wane-id", label: "lin.wane", type: "number"},
						{id: "lin.wavy-id", label: "lin.wavy", type: "number"},
						{id: "lin.wdtn-id", label: "lin.wdtn", type: "number"},
						{id: "lin.wiat-id", label: "lin.wiat", type: "number"},
						{id: "lin.wjcl-id", label: "lin.wjcl", type: "number"},
						{id: "lin.wwlp-id", label: "lin.wwlp", type: "number"}
                      ], 
             "rows": [
						{c:[{v:"2013-10-10"},{v:3995},{v:4349},{v:502},{v:814},{v:5919},{v:0},{v:15232},{v:2295},{v:2817},{v:70},{v:131},{v:4931}]},
						{c:[{v:"2013-10-11"},{v:6461},{v:5205},{v:551},{v:724},{v:6035},{v:0},{v:18513},{v:1249},{v:3248},{v:73},{v:54},{v:4635}]},
						{c:[{v:"2013-10-15"},{v:10058},{v:3962},{v:1545},{v:1451},{v:19350},{v:0},{v:28574},{v:2845},{v:8709},{v:172},{v:184},{v:10914}]},
						{c:[{v:"2013-10-16"},{v:9169},{v:13822},{v:1182},{v:1799},{v:32463},{v:0},{v:37168},{v:4385},{v:7383},{v:234},{v:326},{v:16239}]},
						{c:[{v:"2013-10-17"},{v:12020},{v:17911},{v:1076},{v:2944},{v:32332},{v:0},{v:49454},{v:6103},{v:6602},{v:779},{v:215},{v:21486}]},
						{c:[{v:"2013-10-18"},{v:8945},{v:12583},{v:1243},{v:3597},{v:29177},{v:0},{v:44500},{v:5055},{v:7941},{v:434},{v:301},{v:23403}]},
						{c:[{v:"2013-10-19"},{v:10622},{v:21802},{v:1104},{v:2814},{v:17732},{v:0},{v:38184},{v:4425},{v:8940},{v:405},{v:473},{v:17877}]},
						{c:[{v:"2013-10-20"},{v:9931},{v:11572},{v:1378},{v:2918},{v:20567},{v:0},{v:68331},{v:4040},{v:9247},{v:386},{v:306},{v:18029}]},
						{c:[{v:"2013-10-21"},{v:7604},{v:12925},{v:799},{v:1729},{v:22277},{v:0},{v:82131},{v:3622},{v:7396},{v:226},{v:269},{v:18689}]},
						{c:[{v:"2013-10-22"},{v:8718},{v:8610},{v:2786},{v:1981},{v:23298},{v:3},{v:40755},{v:7256},{v:8247},{v:279},{v:289},{v:22662}]},
						{c:[{v:"2013-10-23"},{v:9039},{v:15221},{v:3688},{v:2292},{v:26137},{v:23},{v:37627},{v:5519},{v:8362},{v:647},{v:321},{v:23415}]}
					]
	                      
        };
    
    
    var data3 = {"cols": [
						{id: "date", label: "Date", type: "string"},
						{id: "lin.khon-id", label: "lin.khon", type: "number"},
						{id: "lin.krqe-id", label: "lin.krqe", type: "number"},
						{id: "lin.ksnt-id", label: "lin.ksnt", type: "number"},
						{id: "lin.ksnw-id", label: "lin.ksnw", type: "number"},
						{id: "lin.kxan-id", label: "lin.kxan", type: "number"},
						{id: "lin.wala-id", label: "lin.wala", type: "number"},
						{id: "lin.wane-id", label: "lin.wane", type: "number"},
						{id: "lin.wavy-id", label: "lin.wavy", type: "number"},
						{id: "lin.wdtn-id", label: "lin.wdtn", type: "number"},
						{id: "lin.wiat-id", label: "lin.wiat", type: "number"},
						{id: "lin.wjcl-id", label: "lin.wjcl", type: "number"},
						{id: "lin.wwlp-id", label: "lin.wwlp", type: "number"}
                      ], 
			  "rows": [
					{c:[{v:"2013-10-10"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-11"},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0},{v:0}]},
					{c:[{v:"2013-10-15"},{v:15},{v:6},{v:3},{v:1},{v:16},{v:0},{v:15},{v:3},{v:12},{v:0},{v:0},{v:9}]},
					{c:[{v:"2013-10-16"},{v:14},{v:20},{v:3},{v:1},{v:28},{v:0},{v:28},{v:3},{v:9},{v:0},{v:1},{v:14}]},
					{c:[{v:"2013-10-17"},{v:18},{v:21},{v:2},{v:3},{v:21},{v:0},{v:45},{v:9},{v:8},{v:3},{v:0},{v:26}]},
					{c:[{v:"2013-10-18"},{v:12},{v:14},{v:6},{v:10},{v:34},{v:0},{v:34},{v:7},{v:13},{v:0},{v:0},{v:29}]},
					{c:[{v:"2013-10-19"},{v:10},{v:22},{v:1},{v:2},{v:8},{v:0},{v:37},{v:7},{v:5},{v:0},{v:0},{v:25}]},
					{c:[{v:"2013-10-20"},{v:10},{v:17},{v:5},{v:4},{v:19},{v:0},{v:44},{v:5},{v:10},{v:1},{v:0},{v:22}]},
					{c:[{v:"2013-10-21"},{v:13},{v:12},{v:4},{v:2},{v:30},{v:0},{v:59},{v:2},{v:8},{v:0},{v:1},{v:18}]},
					{c:[{v:"2013-10-22"},{v:6},{v:10},{v:15},{v:4},{v:23},{v:0},{v:30},{v:14},{v:12},{v:0},{v:0},{v:28}]},
					{c:[{v:"2013-10-23"},{v:14},{v:25},{v:7},{v:3},{v:34},{v:0},{v:33},{v:3},{v:7},{v:0},{v:0},{v:29}]}
			  ]
                      
      };   
	
    chart1.data = data1;

    chart1.options = {
        "title": "CTR per day",
        "isStacked": "true",
        "fill": 20,
        "displayExactValues": true,
        "vAxis": {
            "title": "CTR", "gridlines": {"count": 10}
        },
        "hAxis": {
            "title": "Date", "format":"##.##"
        }
    };
    
    
    
    //  ({pattern:'#,###'});
    //  formatter.format(data, 1);


    var formatCollection = [
           			     {name: "color",
           			    	 format: [{columnNum: 4,
           				    	 formats: [
           				         {from: 0,to: 3,color: "white", bgcolor: "red" },
           				         {from: 3,to: 5,color: "white",fromBgColor: "red",toBgColor: "blue"},
           				         {from: 6, to: null,color: "black", bgcolor: "#33ff33"}
           			         ]
           			        } 
           			      ]
           			     },
           			    {  name: "arrow",  checked:false,
           				   format: [{columnNum: 1,base: 19}]
           			    },
           			    {
           			       name: "date",
           			       format: [ {columnNum: 5,formatType: 'long'}]
           			    }, 
           			    {
           				   name: "number",
           				   format: [{columnNum: 4,prefix: '$'}]
           			    },
           			    {
           				   name: "bar",
           				   format: [{columnNum: 1,width: 100}]
           			    }
    ]

    //

    $scope.chart = chart1;
   

    $scope.chartSelectionChange = function () {
    	
    	 if ($scope.chart.data=='data1') {
    		 $scope.chart.data = data1;
    		 chart1.options = {
    			        "title": "CTR per day",
    			        "isStacked": "true",
    			        "fill": 20,
    			        "displayExactValues": true,
    			        "vAxis": {
    			            "title": "CTR", "gridlines": {"count": 10}
    			        },
    			        "hAxis": {
    			            "title": "Date"
    			        }
    		};
    	}else if($scope.chart.data=='data2') {
    		 $scope.chart.data = data2;
    		 chart1.options = {
    			        "title": "Impressions per day",
    			        "isStacked": "true",
    			        "fill": 20,
    			        "displayExactValues": true,
    			        "vAxis": {
    			            "title": "Impressions", "gridlines": {"count": 10}
    			        },
    			        "hAxis": {
    			            "title": "Date"
    			        }
    	     };
    		 
    	}else if($scope.chart.data=='data3') {
       		 $scope.chart.data = data3;
       		 chart1.options = {
			        "title": "Clicks per day",
			        "isStacked": "true",
			        "fill": 20,
			        "displayExactValues": true,
			        "vAxis": {
			            "title": "Clicks", "gridlines": {"count": 10}
			        },
			        "hAxis": {
			            "title": "Date"
			        }
	         };
       	}    	
    	
    	
        if (($scope.chart.type==='Table' && $scope.chart.data.cols.length===6 && $scope.chart.options.tooltip.isHtml===true) || 
           ($scope.chart.type!='Table' && $scope.chart.data.cols.length===6 && $scope.chart.options.tooltip.isHtml===false)) {
            $scope.chart.data.cols.pop();
            delete $scope.chart.data.rows[0].c[5];
            delete $scope.chart.data.rows[1].c[5];
            delete $scope.chart.data.rows[2].c[5];
        }


        if ($scope.chart.type==='Table') {

            $scope.chart.options.tooltip.isHtml=false;

            $scope.chart.data.cols.push( {id: "data-id", label: "Date", type: "date"} );
            $scope.chart.data.rows[0].c[5] = {v:"Date(2013,01,05)"};
            $scope.chart.data.rows[1].c[5] = {v:"Date(2013,02,05)"};
            $scope.chart.data.rows[2].c[5] = {v:"Date(2013,03,05)"};
        }

    }


    $scope.htmlTooltip = function() {

        if ($scope.chart.options.tooltip.isHtml) {
            $scope.chart.data.cols.push( {id: "", "role": "tooltip", "type": "string", "p" : { "role" : "tooltip" ,'html': true} }   );
            $scope.chart.data.rows[0].c[5] = {v:" <b>Shipping "+$scope.chart.data.rows[0].c[4].v+"</b><br /><img src=\"http://icons.iconarchive.com/icons/antrepo/container-4-cargo-vans/512/Google-Shipping-Box-icon.png\" style=\"height:85px\" />"};
            $scope.chart.data.rows[1].c[5] = {v:" <b>Shipping "+$scope.chart.data.rows[1].c[4].v+"</b><br /><img src=\"http://icons.iconarchive.com/icons/antrepo/container-4-cargo-vans/512/Google-Shipping-Box-icon.png\" style=\"height:85px\" />"};
            $scope.chart.data.rows[2].c[5] = {v:" <b>Shipping "+$scope.chart.data.rows[2].c[4].v+"</b><br /><img src=\"http://icons.iconarchive.com/icons/antrepo/container-4-cargo-vans/512/Google-Shipping-Box-icon.png\" style=\"height:85px\" />"};
        } else {
            $scope.chart.data.cols.pop();
            delete $scope.chart.data.rows[0].c[5];
            delete $scope.chart.data.rows[1].c[5];
            delete $scope.chart.data.rows[2].c[5];
        }
    }


    $scope.hideServer = false;
    $scope.selectionChange = function () {
        if($scope.hideServer) {
            $scope.chart.view = {columns: [0,1,2,4]};
        } else {
            $scope.chart.view = {};
        }
    }

    $scope.formatCollection = formatCollection;
    $scope.toggleFormat= function (format) {
        $scope.chart.formatters[format.name] = format.format;
    };

    $scope.chartReady = function() {    	
        fixGoogleChartsBarsBootstrap();
    }

    function fixGoogleChartsBarsBootstrap() {
         // Google charts uses <img height="12px">, which is incompatible with Twitter
         // * bootstrap in responsive mode, which inserts a css rule for: img { height: auto; }.
         // *
         // * The fix is to use inline style width attributes, ie <img style="height: 12px;">.
         // * BUT we can't change the way Google Charts renders its bars. Nor can we change
         // * the Twitter bootstrap CSS and remain future proof.
         // *
         // * Instead, this function can be called after a Google charts render to "fix" the
         // * issue by setting the style attributes dynamically.

        $(".google-visualization-table-table img[width]").each(function(index, img) {
            $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
        });
    };

 
    var chart2 = {};
    chart2.type = "PieChart";
    chart2.displayed = false;
    chart2.cssStyle = "height:80%; width:80%;";
    chart2.data = {"cols": [
                            {id: "platform", label: "OS Platforms", type: "string"},
                            {id: "impression-id", label: "impressions", type: "number"}
                        ], "rows": [
                            {c: [{v: "Android"},{v: 327461, f: "327461 impressions"}]},
                            {c: [{v: "Apple iOS"},{v: 951761, f: "951761 impressions"}]},
                            {c: [{v: "BlackBerry"},{v: 2, f: "2 impressions"}]},
                            {c: [{v: "Linux"},{v: 315}]},
                            {c: [{v: "Macintosh"},{v: 229}]},
                            {c: [{v: "Microsoft Windows 7"},{v: 3872}]},
                            {c: [{v: "Microsoft Windows 8"},{v: 271}]},
                            {c: [{v: "Microsoft Windows Vista"},{v: 50}]},
                            {c: [{v: "Microsoft Windows XP"},{v: 50}]},
                            {c: [{v: "Other/Unknown"},{v: 6308}]},
                            {c: [{v: "WindowsPhone"},{v: 4208}]}
                        ]};

    chart2.options = {
        "title": "Impressions breakdown by OS Platform",
        "isStacked": "true",
        "fill": 20,
        "displayExactValues": true,
        "vAxis": {
            "title": "Impressions", "gridlines": {"count": 10}
        },
        "hAxis": {
            "title": "OS"
        }
    };
    $scope.pieChart1 = chart2;
    
    
    var chart3 = {};
    chart3.type = "PieChart";
    chart3.displayed = false;
    chart3.cssStyle = "height:80%; width:80%;";
    chart3.data = {"cols": [
                     {id: "platform", label: "Platform", type: "string"},
                     {id: "impression-id", label: "impressions", type: "number"}
                  ], "rows": [
					{c: [{v: "Desktop"},{v: 11616, f: "11616 impressions"}]},
					{c: [{v: "Smart Phone"},{v: 1262366, f: "1262366 impressions"}]},
					{c: [{v: "MidRangeMobile"},{v: 1355, f: "1355 impressions"}]},
					{c: [{v: "Tablet"},{v: 19190}]}
     ]};

    chart3.options = {
        "title": "Impressions breakdown by Platform",
        "isStacked": "true",
        "fill": 20,
        "displayExactValues": true,
        "vAxis": {
            "title": "Impressions", "gridlines": {"count": 10}
        },
        "hAxis": {
            "title": "Platform"
        }
    };
    $scope.pieChart2 = chart3;
    
  
    var barChart = {};
    barChart.type = "BarChart";
    barChart.displayed = false;
    barChart.cssStyle = "height:80%; width:80%;";
    barChart.data = {"cols": [
                              {id: "creative", label: "Creative", type: "string"},
                              {id: "ctr_id", label: "CTR", type: "number"}
                              
                          ], "rows": [
                              {c: [{v: "USAA Military Millennials Mobile | 320x50"},{v: 0.09}]},
                              {c: [{v: "LIN Mobile | USAA Military Millennials | 300x250 Mobile Web"},{v: 0.00}]},
                              {c: [{v: "USAA Military Millennials Mobile | 320x50 In-App Revised Tag"},{v: 0.10}]},
                              {c: [{v: "USAA Military Millennials Mobile | 320x50 Mobile Web Revised"},{v: 0.18}]},
                              {c: [{v: "USAA Military Millennials | 300x250 Tablet Web"},{v: 0.00}]},
                              {c: [{v: "USAA Military Millennials | 300x250 Tablet In-App"},{v: 0.00}]}
                              
     ]};


    barChart.options = {
        "title": "CTR by Creative Name",
        "isStacked": "true",
        "fill": 20,
        "displayExactValues": true,
        "vAxis": {
            "title": "Creative", "gridlines": {"count": 10}
        },
        "hAxis": {
            "title": "CTR"
        }
    };
    $scope.barChart1 = barChart;
    
    var barChart2 = {};
    barChart2.type = "BarChart";
    barChart2.displayed = false;
    barChart2.cssStyle = "height:80%; width:80%;";
    barChart2.data = {"cols": [
                              {id: "creative", label: "Creative", type: "string"},
                              {id: "ctr_id", label: "CTR", type: "number"}
                              
                          ], "rows": [
                              {c: [{v: "USAA Military Millennials Mobile | Military Geo Targets | 320x50 In-App"},{v: 0.09}]},
                              {c: [{v: "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet Web"},{v: 0.00}]},
                              {c: [{v: "USAA Military Millennials Mobile | Military Geo Targets | 320x50 Mobile Web"},{v: 0.18}]},                              
                              {c: [{v: "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet In-App"},{v: 0.00}]}
                              
                              
     ]};


    barChart2.options = {
        "title": "CTR % by sub campaign",
        "isStacked": "true",
        "fill": 20,
        "displayExactValues": true,
        "vAxis": {
            "title": "Sub campaign", "gridlines": {"count": 10}
        },
        "hAxis": {
            "title": "CTR"
        }
    };
    $scope.barChart2 = barChart2;
    
   
    
    $scope.topGainersData={
    		"rows":[
					{"id1": "lin.ksnt","id2":"15,854","id3":46,"id4":0.29},
					{"id1": "lin.ksnw","id2":"23,063","id3":30,"id4":0.13},
					{"id1": "lin.khon","id2":"96,562","id3":112,"id4":0.12},
					{"id1": "lin.wwlp","id2":"182,280","id3":200,"id4":0.11},
					{"id1": "lin.wdtn","id2":"78,892","id3":84,"id4":0.11}    		           
    		      ]	
    }
    
    $scope.topLoserData={
    		"rows":[                    
					{"id1": "lin.wala","id2":"26","id3":0,"id4":0.00},
					{"id1": "lin.wjcl","id2":"2,869","id3":2,"id4":0.07},
					{"id1": "lin.wane","id2":"460,469","id3":325,"id4":0.07},
					{"id1": "lin.wiat","id2":"3,705","id3":0,"id4":0.08},
					{"id1": "lin.kxan","id2":"235,287","id3":213,"id4":0.09}		           
    		      ]	
    }
    
    $scope.topPerformerData={
    		"rows":[
					{"id1": "lin.ksnt","id2":"15,854","id3":46,"id4":0.29},
					{"id1": "lin.ksnw","id2":"23,063","id3":30,"id4":0.13},
					{"id1": "lin.khon","id2":"96,562","id3":112,"id4":0.12},
					{"id1": "lin.krqe","id2":"127,962","id3":147,"id4":0.11},
					{"id1": "lin.wavy","id2":"46,794","id3":53,"id4":0.11}    		           
    		      ]	
    }
    
    $scope.topNonPerformerData={
    		"rows":[
					{"id1": "lin.wala","id2":"26","id3":0,"id4":0.00},
					{"id1": "lin.wjcl","id2":"2,869","id3":2,"id4":0.07},
					{"id1": "lin.wane","id2":"460,469","id3":325,"id4":0.07},
					{"id1": "lin.kxan","id2":"235,287","id3":213,"id4":0.09}, 
					{"id1": "lin.wdtn","id2":"78,892","id3":84,"id4":0.11}    		           
    		      ]	
    }
    $scope.mostActiveLineItemData={
    		"rows":[
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 320x50 In-App","id2":"1,248,193","id3":"1,171","id4":0.09,"id5":"73.00%"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet Web","id2":"45","id3":0,"id4":0.00,"id5":"6.40%"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 320x50 Mobile Web","id2":"25,361","id3":45,"id4":0.18,"id5":"2.80%"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet In-App","id2":"64","id3":0,"id4":0.00,"id5":"0.52%"} 
					           
    		      ]	
    }
    
    $scope.performanceByPlacementData={
    		"rows":[
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 320x50 In-App","id2":"7,000,000","id3":"1,248,193","id4":"1,171","id5":0.09,"id6":"$14,000.00","id7":"$2,489.03","id8":"$11,510.97"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet Web","id2":"3,500","id3":145,"id4":0,"id5":0.00,"id6":"$7.00","id7":"$0.29","id8":"$6.71"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 320x50 Mobile Web","id2":"1,000,000","id3":"25,361","id4":0,"id5":0.18,"id6":"$2,000.00","id7":"$50.72","id8":"$1,949.28"},
					{"id1": "USAA Military Millennials Mobile | Military Geo Targets | 300x250 Tablet In-App","id2":"4,500","id3":64,"id4":0,"id5":0.00,"id6":"$9.00","id7":"$0.13","id8":"$8.87"} 
					           
    		      ]	
    }
    
});



