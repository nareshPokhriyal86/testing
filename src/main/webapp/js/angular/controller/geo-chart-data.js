'use strict';

angular.module('geoChartApp', ['googlechart']).controller("geoChartCtrl", function ($scope, $filter,$rootScope,geoChartService) {
	var geoChart = {};
	
	
	try{
		$scope.initdata = function(){ // initData begin
			$("#performanceLocationLoaderId").show();
			
			$scope.geoChartData = geoChartService.getPerformanceByLocationChartData().then(function(chartData) {
				  $scope.geoChart.data=[];
				
				  geoChart.data= $scope.$eval($scope.$eval("(" + chartData + ")"));
				  $scope.geoChart = geoChart;
				  $("#performanceLocationLoaderId").hide();
		     });
		}		 
	}catch(err){
		console.log("geoChartService : err:"+err);
	}//initdata ends
	
	$scope.initdata();
	
	
    geoChart.type = "GeoChart";
    geoChart.displayed = false;
    geoChart.cssStyle = "height:400px; width:100%;";
    var geoData = {"cols": [
			        {id: "state", label: "State", type: "string"},
			        {id: "ctr", label: "ctr(%)", type: "number"},
			        {id: "impression", label: "impressions", type: "number"}
        		], 
			    "rows": [
					
				 ]
    };
   
       
    
   geoChart.data=geoData;
   geoChart.options = {
	    "region": "US",
        "displayMode": "markers",
        "resolution" : "provinces",
	    "colorAxis": {
	            "values": [0,parseFloat($('#amount2').val()),1],
	            "colors": ["red","yellow", "green"]
	        },
		"title":"Geo Map"
		
        
    };
   geoChart.formatters = {
		"number": [
		      {
		       	 columnNum: 1,		       
		       	 suffix: '%',
				 fractionDigits:4			
		      },
		      {
		        columnNum: 2,		       
				pattern:'###,###'			
		      }
		     ]
	};
			
    $scope.geoChart = geoChart;
    
    $scope.chartReady = function() {    	
        fixGoogleChartsBarsBootstrap();
    }

    function fixGoogleChartsBarsBootstrap() {
        $(".google-visualization-table-table img[width]").each(function(index, img) {
            $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
        });
    };
    
    $scope.emptyData = function() {    	
    	$scope.geoChart.data=[];
    }
    

	$rootScope.safeApply = function( fn ) {
	      var phase = this.$root.$$phase;
	      if(phase == '$apply' || phase == '$digest') {
	          if(fn) {
	              fn();
	          }
	      } else {
	          this.$apply(fn);
	      }
	  };
	  
	  $scope.changeGeoChart = function(){
		   geoChart.options = {
				    "region": "US",
			        "displayMode": "markers",
			        "resolution" : "provinces",
				    "colorAxis": {
				            "values": [0,parseFloat($('#amount2').val()), 1],
				            "colors": ["red","yellow", "green"]
				        },
					"title":"Geo Map"
					
			        
			    };
		   $scope.geoChart = geoChart;
	  }
	  
	  $scope.onClickEvent = function(){
		  $scope.contentDiv="";
			var subTitle="";
			var title = "";
			$scope.contentDiv= $('#geoChartPopUp').html();
			//makeTrafferPopUP("geoChartId",title,subTitle,$scope.contentDiv);
	  }
	  function makeTrafferPopUP(id,title,subTitle,contentDiv){
			setTimeout(function(){makeTrafferPopUPDelay(id,title,subTitle,contentDiv)},0);
	  }

	    
	    
	    
	function makeTrafferPopUPDelay(id,title,subTitle,contentDiv) {
		var content='<div id="content">'+contentDiv+'</div>';
		//alert("hi2");
	    $("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+title+"</span>"+"<br/>"+subTitle);	
		$("#"+id).attr('data-toggle',"popover");

		var options={
				html: true,
	            trigger: 'manual',
	            content: content,
	            placement:'top',
	            title:title	            
		};
		 
		$('#'+id).popover(options);
		$('#'+id).popover('show');
		$('.popover-title').append('<button type="button" class="close1" style="opacity: 0.84;margin-top:-18px;">&times;</button>');
		
		$('.close1').click(function(e){
			$('#'+id).popover('hide');
		});	
		 $('.popover').css({'cursor':'move'});
		 $('.popover').click(function(e){
			 $('.popover').draggable();
		 });
		
		 $('#content').html(contentDiv);
		
		 $("#trafficPopup_loader").css({'display':'none'});
		// $scope.$digest();
		 
		
		 //setTimeout(function(){ progressChart($scope)},5000);
	}
}).factory('geoChartService', function($http) {
	   return {
	        getPerformanceByLocationChartData: function() {
	             //return the promise directly.
	             return $http.get('/performanceByLocationChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	        }
	   }
	});

;

