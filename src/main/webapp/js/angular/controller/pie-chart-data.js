'use strict';

angular.module('pieChartApp', ['googlechart']).controller("pieChartCtrl", function ($scope,pieChartService,$rootScope) {
	var formatter= {
			"number": [
						 {
						   columnNum: 1,
						   pattern:'###,###'			
						}
					]
	};
	
	
	
	
	$scope.initdata = function(){
		
		$("#performanceOSLoaderId").show();
		$("#performanceDeviceLoaderId").show();
		$("#mobileWebVsAppLoaderId").show();
		
		
		try{
			$scope.performanceByOsData = pieChartService.getPerformenceByOSChartData().then(function(chartData) {
				  chart2.data= $scope.$eval($scope.$eval("(" + chartData + ")"));
				  $scope.pieChart1 = chart2;	
				  $("#performanceOSLoaderId").hide();			
			});
			
		   $scope.performanceByDeviceData = pieChartService.getPerformenceByDeviceChartData().then(function(chartData) {
			      chart3.data= $scope.$eval($scope.$eval("(" + chartData + ")"));
			      $scope.pieChart2 = chart3;	      
			  	  $("#performanceDeviceLoaderId").hide();	  	  
			});
		   $scope.mobileWebVsAppData = pieChartService.getMobileWebVsAppChartData().then(function(chartData) {
			   chart4.data = $scope.$eval($scope.$eval("(" + chartData + ")"));
			   $scope.pieChart3 = chart4;
			   $("#mobileWebVsAppLoaderId").hide();
		   });
		   
		
		   
		}catch(err){
			console.log('piechartservice :err:'+err);
		}
		
	}
	$scope.initdata();
	
	var chart2 = {};
    chart2.type = "PieChart";
    chart2.displayed = false;
    chart2.cssStyle = "height:80%; width:80%;";
    chart2.data = {"cols": [
                            {id: "platform", label: "OS Platforms", type: "string"},
                            {id: "impression-id", label: "impressions", type: "number"},
                            {id: "clicks-id", label: "clicks", type: "number"},
                            {id: "ctr-id", label: "ctr", type: "number"}
                        ], "rows": [
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
        }, "tooltip": {
		      "isHtml": true,
		      "trigger": "focus"
	    }
        
    };
    chart2.formatters =formatter;
    $scope.pieChart1 = chart2;
  
	
    var chart3 = {};
    chart3.type = "PieChart";
    chart3.displayed = false;
    chart3.cssStyle = "height:80%; width:80%;";
    chart3.data = {"cols": [
                     {id: "platform", label: "Platform", type: "string"},
                     {id: "impression-id", label: "impressions", type: "number"}
                  ], "rows": [
					
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
    chart3.formatters = formatter;
    $scope.pieChart2 = chart3;
    
    var chart4 = {};
    chart4.type = "PieChart";
    chart4.displayed = false;
    chart4.cssStyle = "height:80%; width:80%;";
    chart4.data = {"cols": [
                     {id: "platform", label: "Platform", type: "string"},
                     {id: "impression-id", label: "impressions", type: "number"}
                  ], "rows": [
					
     ]};

    chart4.options = {
        "title": " Mobile Web vs .App",
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
    chart4.formatters = formatter;
    $scope.pieChart3 = chart4;
    

    
    $scope.chartReady = function() {    	
        fixGoogleChartsBarsBootstrap();
    }

    function fixGoogleChartsBarsBootstrap() {
        $(".google-visualization-table-table img[width]").each(function(index, img) {
            $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
        });
    };
    
    $scope.emptyData = function() {   
    	
    	$scope.pieChart1.data={"cols": [
    	                                {id: "platform", label: "OS Platforms", type: "string"},
    	                                {id: "impression-id", label: "impressions", type: "number"},
    	                                {id: "clicks-id", label: "clicks", type: "number"},
    	                                {id: "ctr-id", label: "ctr", type: "number"}
    	                            ], "rows": [
    	                            ]};
    	$scope.pieChart2.data={"cols": [
    	                                {id: "platform", label: "Platform", type: "string"},
    	                                {id: "impression-id", label: "impressions", type: "number"}
    	                             ], "rows": [
    	           					
    	                ]};
    	$scope.pieChart3.data={"cols": [
    	                                {id: "platform", label: "Platform", type: "string"},
    	                                {id: "impression-id", label: "impressions", type: "number"}
    	                             ], "rows": [
    	           					
    	                ]};
    	
    
    	
    	$scope.pieChart1 = chart2;
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
    
}).factory('pieChartService', function($http) {
	   return {
		   getPerformenceByOSChartData: function() {
	             //return the promise directly.
	             return $http.get('/impressionByOSChartData.lin?orderId='+campaignId)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	        },
	   
	        getPerformenceByDeviceChartData: function() {
            //return the promise directly.
           return $http.get('/impressionByDeviceChartData.lin?orderId='+campaignId)
                     .then(function(result) {
                          //resolve the promise as the data
                          return result.data;
                      });
      },
     
      getMobileWebVsAppChartData: function() {
          //return the promise directly.
         return $http.get('/impressionBymobileWebVsAppChartData.lin?orderId='+campaignId)
                   .then(function(result) {
                        //resolve the promise as the data
                        return result.data;
                    });
    },
      
     
	  
	   }
	  
	});

angular.module('pieChartImpByAdSizeApp', ['googlechart']).controller("pieChartCtrl", function ($scope,pieChartImpByAdSizeService,$rootScope) {
	var formatter= {
			"number": [
						 {
						   columnNum: 1,
						   pattern:'###,###'			
						}
					]
	};
	
	$scope.initdata = function(){
		$("#impByAdSizeLoaderId").show();
		try{
			   $scope.impressionByAdSize = pieChartImpByAdSizeService.getImpressionByAdSizeChartData().then(function(chartData) {
				   chart5.data = $scope.$eval($scope.$eval("(" + chartData + ")"));
				   $scope.pieChart4 = chart5;
				   $("#impByAdSizeLoaderId").hide();
			   });
		}catch(err){
			
		}
	}
	$scope.initdata();
	
	  var chart5 = {};
	    chart5.type = "PieChart";
	    chart5.displayed = false;
	    chart5.cssStyle = "height:80%; width:80%;";
	    chart5.data = {"cols": [
	                     {id: "Ad Size", label: "Ad Size", type: "string"},
	                     {id: "impression-id", label: "impressions", type: "number"}
	                  ], "rows": [
						
	     ]};

	    chart5.options = {
	        "title": " Impressions By Ad Size",
	        "isStacked": "true",
	        "fill": 20,
	        "displayExactValues": true,
	        "vAxis": {
	            "title": "Impressions", "gridlines": {"count": 10}
	        },
	        "hAxis": {
	            "title": "Ad Size"
	        }
	    };
	    chart5.formatters = formatter;
	    $scope.pieChart4 = chart5;
	    
	    $scope.chartReady = function() {    	
	        fixGoogleChartsBarsBootstrap();
	    }

	    function fixGoogleChartsBarsBootstrap() {
	        $(".google-visualization-table-table img[width]").each(function(index, img) {
	            $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
	        });
	    };
	    
	    $scope.emptyData = function() {
	    	$scope.pieChart4.data={"cols": [
	    	                                {id: "Ad Size", label: "Ad Size", type: "string"},
	    	                                {id: "impression-id", label: "impressions", type: "number"}
	    	                             ], "rows": [
	    	           					
	    	                ]};
	    	
	    	$scope.pieChart1 = chart2;
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
}).factory('pieChartImpByAdSizeService', function($http) {
	return{
	      getImpressionByAdSizeChartData: function() {
	          //return the promise directly.
	         return $http.get('/impressionsByAdSizeChartData.lin?orderId='+campaignId)
	                   .then(function(result) {
	                        //resolve the promise as the data
	                        return result.data;
	                    });
	    }
	      
		  
		   
	}
});
