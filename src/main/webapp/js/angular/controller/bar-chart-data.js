'use strict';

angular.module('barChartApp', ['googlechart']).controller("barChartCtrl", function ($scope,$rootScope,barChartService) {
	var barChart1 = {};
	
	
	
	$scope.initdata = function(){
		
		//$("#performanceAdSizeLoaderId").show();
		$("#barChartByOSLoaderId").show();
		$("#barChartByDeviceLoaderId").show();
		$("#barChartByMobileLoaderId").show();
		try{
			/* $scope.performanceByAdSizeChartData = barChartService.getPerformanceByAdSizeChartData().then(function(chartData) {
	         	if(chartData !=undefined && chartData !=null){
	         		$scope.performanceByAdSizeChartData= $scope.$eval($scope.$eval("(" + chartData + ")"));
	 		        $scope.chartData = [];
	 		        //console.log($scope.performanceByAdSizeChartData);
	 		        angular.forEach( $scope.performanceByAdSizeChartData, function(value, key){ 		        	
	 		        	$scope.chartData.push({ c: [ { v: value.creativeSize}, {v:value.CTR},{v:value.benchCtr}]});
	 	            }); 
	 		        //console.log($scope.chartData);
	 		        barChart1.data = {"cols": [
	 		      	                            {id: "creative", label: "Creative", type: "string"},
	 		      	                            {id: "ctr_id", label: "CTR", type: "number"},
	 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
	 		      	                            
	 		      	                        ], "rows": $scope.chartData
	 		      	 };    
	 	            
	 	      	    $scope.barChart1=barChart1;
	 	      	    $("#performanceAdSizeLoaderId").hide(); 	    	
	         	}
	     	
	    
	    });*/
			
			 
			 $scope.performanceByOSBarChartData = barChartService.getPerformanceByOSBarChartData().then(function(chartData) {
		         	if(chartData !=undefined && chartData !=null){
		         		$scope.performanceByOSBarChartData= $scope.$eval($scope.$eval("(" + chartData + ")"));
		 		        $scope.chartData = [];
		 		       // console.log($scope.performanceByAdSizeChartData);
		 		        angular.forEach( $scope.performanceByOSBarChartData, function(value, key){ 		        	
		 		        	$scope.chartData.push({ c: [ { v: value.targetValue}, {v:value.CTR},{v:value.benchCtr}]});
		 	            }); 
		 		        //console.log($scope.chartData);
		 		       barChartByOS.data = {"cols": [
		 		      	                            {id: "creative", label: "Creative", type: "string"},
		 		      	                            {id: "ctr_id", label: "CTR", type: "number" },
		 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		 		      	                            
		 		      	                        ], "rows": $scope.chartData
		 		      	 };    
		 	            
		 	      	    $scope.barChartByOS=barChartByOS;
		 	      	    $("#barChartByOSLoaderId").hide(); 	    	
		         	}
		     	
		    
		    });
			 
			 $scope.performanceByDeviceBarChartData = barChartService.getPerformanceByDeviceBarChartData().then(function(chartData) {
		         	if(chartData !=undefined && chartData !=null){
		         		$scope.performanceByDeviceBarChartData= $scope.$eval($scope.$eval("(" + chartData + ")"));
		 		        $scope.chartData = [];
		 		        //console.log($scope.performanceByAdSizeChartData);
		 		        angular.forEach( $scope.performanceByDeviceBarChartData, function(value, key){ 		        	
		 		        	$scope.chartData.push({ c: [ { v: value.targetValue}, {v:value.CTR},{v:value.d3benchCtr}]});
		 	            }); 
		 		        //console.log($scope.chartData);
		 		       barChartByDevice.data = {"cols": [
		 		      	                            {id: "creative", label: "Creative", type: "string"},
		 		      	                            {id: "ctr_id", label: "CTR", type: "number"},
		 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		 		      	                            
		 		      	                        ], "rows": $scope.chartData
		 		      	 };    
		 	            
		 	      	    $scope.barChartByDevice=barChartByDevice;
		 	      	    $("#barChartByDeviceLoaderId").hide(); 	    	
		         	}
		     	
		    
		    });
			 
			 $scope.performanceBymobileWebVsAppBarChartData = barChartService.getPerformanceBymobileWebVsAppBarChartData().then(function(chartData) {
		         	if(chartData !=undefined && chartData !=null){
		         		$scope.performanceBymobileWebVsAppBarChartData= $scope.$eval($scope.$eval("(" + chartData + ")"));
		 		        $scope.chartData = [];
		 		        //console.log($scope.performanceByAdSizeChartData);
		 		        angular.forEach( $scope.performanceBymobileWebVsAppBarChartData, function(value, key){ 		        	
		 		        	$scope.chartData.push({ c: [ { v: value.site}, {v:value.CTR},{v:value.d3benchCtr}]});
		 	            }); 
		 		        //console.log($scope.chartData);
		 		       barChartByMobile.data = {"cols": [
		 		      	                            {id: "creative", label: "Creative", type: "string"},
		 		      	                            {id: "ctr_id", label: "CTR", type: "number"},
		 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		 		      	                            
		 		      	                        ], "rows": $scope.chartData
		 		      	 };    
		 	            
		 	      	    $scope.barChartByMobile=barChartByMobile;
		 	      	    $("#barChartByMobileLoaderId").hide(); 	    	
		         	}
		     	
		    
		    });
		}catch(err){
			console.log("performanceByAdSizeChartData:err:"+err);
		}   
		
	}
	
	$scope.initdata();
	   
	/* $scope.response= $('#amount2').val();
	 barChart1.type = "ComboChart";
	    barChart1.displayed = false;
	    barChart1.cssStyle = "height:80%; width:80%;";
	    barChart1.data = {"cols": [
	                            {id: "creative", label: "Creative", type: "string"},
	                            {id: "ctr_id", label: "CTR", type: "number"},
	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
	                            
	                        ], "rows": [
	   ]};

	   barChart1.options = {
	  		vAxis: {title: "CTR%"},
	  		hAxis: {title: "Ad Size"},
	  		"title":"CTR% by Ad Size",
	  		seriesType: "bars",
	  		series: {1: {type: "line"}}
	   };
	   barChart1.formatters = {
			   "number": [
			              {
			                columnNum: 1,
			                suffix: '%',
			       			fractionDigits:4
			       			//pattern:'#,###%'			
			             }
			            ]
	    };
	   $scope.barChart1 = barChart1; */	    
      	    
      	   
	    
	    /*var barChart2 = {};
	    barChart2.type = "ComboChart";
	    barChart2.displayed = false;
	    barChart2.cssStyle = "height:80%; width:80%;";
	    barChart2.data = {"cols": [
	                              {id: "creative", label: "Creative", type: "string"},
	                              {id: "ctr_id", label: "CTR", type: "number"},
	                              {id: "ctr_id2", label: "BENCHMARK", type: "number"}
	                              
	                          ], "rows": [
	                              {c: [{v: "Online Tools to plan for Today�s latte and tomorrow�s home"},{v: 0.20},{v: $scope.response}]},
	                              {c: [{v: "Test Message"},{v: 0.30},{v: $scope.response}]}
	                             
	                              
	                              
	     ]};

	    barChart2.options = {
	      		vAxis: {title: "CTR%"},
	      		hAxis: {title: "Message"},
	      		"title":"CTR% by Message",
	      		seriesType: "bars",
	      		series: {1: {type: "line"}}
	    };
	    
	    $scope.barChart2 = barChart2;	    
		$("#performanceMsgLoaderId").hide();*/
	    
		var barChartByOS = {};
		 barChartByOS.type = "ComboChart";
		    barChartByOS.displayed = false;
		    barChartByOS.cssStyle = "height:80%; width:80%;";
		    barChartByOS.data = {"cols": [
		                            {id: "creative", label: "Creative", type: "string"},
		                            {id: "ctr_id", label: "CTR", type: "number"},
		                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		                            
		                        ], "rows": [
		   ]};

		   barChartByOS.options = {
		  		vAxis: {title: "CTR%"},
		  		hAxis: {title: "OS Plateform"},
		  		"title":"CTR% by OS",
		  		seriesType: "bars",
		  		series: {1: {type: "line"}}
		   };
		   barChartByOS.formatters = {
				   "number": [
				              {
				                columnNum: 1,
				                suffix: '%',
				       			fractionDigits:4
				       			//pattern:'#,###%'			
				             }
				            ]
		    };
		   $scope.barChartByOS = barChartByOS;
		   
		   var barChartByDevice = {};
			 barChartByDevice.type = "ComboChart";
			    barChartByDevice.displayed = false;
			    barChartByDevice.cssStyle = "height:80%; width:80%;";
			    barChartByDevice.data = {"cols": [
			                            {id: "creative", label: "Creative", type: "string"},
			                            {id: "ctr_id", label: "CTR", type: "number"},
			                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
			                            
			                        ], "rows": [
			   ]};

			   barChartByDevice.options = {
			  		vAxis: {title: "CTR%"},
			  		hAxis: {title: "Devices"},
			  		"title":"CTR% by Device",
			  		seriesType: "bars",
			  		series: {1: {type: "line"}}
			   };
			   barChartByDevice.formatters = {
					   "number": [
					              {
					                columnNum: 1,
					                suffix: '%',
					       			fractionDigits:4
					       			//pattern:'#,###%'			
					             }
					            ]
			    };
			   $scope.barChartByDevice = barChartByDevice; 
			   
			   var barChartByMobile = {};
				 barChartByMobile.type = "ComboChart";
				    barChartByMobile.displayed = false;
				    barChartByMobile.cssStyle = "height:80%; width:80%;";
				    barChartByMobile.data = {"cols": [
				                            {id: "creative", label: "Creative", type: "string"},
				                            {id: "ctr_id", label: "CTR", type: "number"},
				                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
				                            
				                        ], "rows": [
				   ]};

				   barChartByMobile.options = {
				  		vAxis: {title: "CTR%"},
				  		hAxis: {title: "Web/App"},
				  		"title":"CTR% by Mobile Web VS App",
				  		seriesType: "bars",
				  		series: {1: {type: "line"}}
				   };
				   barChartByMobile.formatters = {
						   "number": [
						              {
						                columnNum: 1,
						                suffix: '%',
						       			fractionDigits:4
						       			//pattern:'#,###%'			
						             }
						            ]
				    };
				   $scope.barChartByMobile = barChartByMobile; 
	    // Added for external updates
	    
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
		 

		 /* $scope.changeCombo1=function(){	
			  $scope.chartData = [];
			  angular.forEach( $scope.performanceByAdSizeChartData, function(value, key){
		        	$scope.chartData.push({ c: [ { v: value.creativeSize}, {v:value.CTR},{v:parseFloat($('#amount2').val())}]});
	            }); 
		        barChart1.data = {"cols": [
		      	                            {id: "creative", label: "Creative", type: "string"},
		      	                            {id: "ctr_id", label: "CTR", type: "number"},
		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		      	                            
		      	                        ], "rows": $scope.chartData
		      	   };  
	            
	      	    $scope.barChart1=barChart1;
			 	  
			  };*/  
			  
			 /* $scope.changeCombo2=function(){	
				   
				  barChart2.data = {"cols": [
				                              {id: "creative", label: "Creative", type: "string"},
				                              {id: "ctr_id", label: "CTR", type: "number"},
				                              {id: "ctr_id2", label: "BENCHMARK", type: "number"}
				                              
				                          ], "rows": [
				                              {c: [{v: "Online Tools to plan for Today�s latte and tomorrow�s home"},{v: 0.20},{v:  parseFloat($('#amount2').val())}]},
				                              {c: [{v: "Test Message"},{v: 0.30},{v: parseFloat($('#amount2').val())}]}
				                            
				                              
				     ]};
				  	
				  	 $scope.barChart2=barChart2;				  	
					 $("#performanceMsgLoaderId").hide();
				  };*/
				  
				  $scope.changeBarChartByOS=function(){	
					  $scope.chartData = [];
					  angular.forEach( $scope.performanceByOSBarChartData, function(value, key){
				        	$scope.chartData.push({ c: [ { v: value.targetValue}, {v:value.CTR},{v:parseFloat($('#amount2').val())}]});
			            }); 
					  barChartByOS.data = {"cols": [
				      	                            {id: "creative", label: "Creative", type: "string"},
				      	                            {id: "ctr_id", label: "CTR", type: "number"},
				      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
				      	                            
				      	                        ], "rows": $scope.chartData
				      	   };    
			            
			      	    $scope.barChartByOS=barChartByOS;
				  }
				  
				  $scope.changeBarChartByDevice=function(){
					  $scope.chartData = [];
		 		        angular.forEach( $scope.performanceByDeviceBarChartData, function(value, key){ 		        	
		 		        	$scope.chartData.push({ c: [ { v: value.targetValue}, {v:value.CTR},{v:parseFloat($('#amount2').val())}]});
		 	            }); 
		 		       barChartByDevice.data = {"cols": [
		 		      	                            {id: "creative", label: "Creative", type: "string"},
		 		      	                            {id: "ctr_id", label: "CTR", type: "number"},
		 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		 		      	                            
		 		      	                        ], "rows": $scope.chartData
		 		      	 };    
		 	            
		 	      	    $scope.barChartByDevice=barChartByDevice;
				  }
				  
				  $scope.changeBarChartMobile=function(){
					  $scope.chartData = [];
		 		        //console.log($scope.performanceByAdSizeChartData);
		 		        angular.forEach( $scope.performanceBymobileWebVsAppBarChartData, function(value, key){ 		        	
		 		        	$scope.chartData.push({ c: [ { v: value.site}, {v:value.CTR},{v:parseFloat($('#amount2').val())}]});
		 	            }); 
		 		        //console.log($scope.chartData);
		 		       barChartByMobile.data = {"cols": [
		 		      	                            {id: "creative", label: "Creative", type: "string"},
		 		      	                            {id: "ctr_id", label: "CTR", type: "number"},
		 		      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		 		      	                            
		 		      	                        ], "rows": $scope.chartData
		 		      	 };    
		 	            
		 	      	    $scope.barChartByMobile=barChartByMobile;
				  }
	
    $scope.chartReady = function() {    	
        fixGoogleChartsBarsBootstrap();
    };

    function fixGoogleChartsBarsBootstrap() {
        $(".google-visualization-table-table img[width]").each(function(index, img) {
            $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
        });
    };
    
   /* $scope.barChartByOS.methods = {};
    
    $scope.barChartByOS.methods.select = function(selection, event){
    	var rows = [];
    	var data1= {};
    	 var series = {};
    	 var options={};
    	data1 = barChartByOS.data;
    	console.log(data1.rows.length)
    	if(data1 !=null){
        	for (var i = 0; i < data1.rows.length; i++) {
        		rows.push(i);
                if (i > 0) {
                    series[i - 1] = {};
                }
            }
            options = { series: series}
        }
    	var sel = selection;
    	console.log(data1);
    	// if selection length is 0, we deselected an element
        if (sel.length > 0) {            
        	console.log('sel[0].row.......'+sel[0].row);
        	var roww = sel[0].row;           	
            if (rows[roww] == roww) {
                // hide the data series
            	rows[roww] = {
                    label:  $scope.barChartByOS.data.rows[roww].label,
                    type:  $scope.barChartByOS.data.rows[roww].type,
                    calc: function () {
                        return null;
                    }
                };
                // grey out the legend entry
               // series[roww-1].color = '#CCCCCC';
            }
            else {
                // show the data series
                rows[roww] = roww;
                //series[roww-1].color = null;
            }
            $scope.barChartByOS.options=options;
            $scope.barChartByOS.view = {row: rows };
        	
            
        }
    };*/
   
    $scope.emptyData = function() {    	
    	/*$scope.barChart1.data = {"cols": [
    	                            {id: "creative", label: "Creative", type: "string"},
    	                            {id: "ctr_id", label: "CTR", type: "number"},
    	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
    	                            
    	                        ], "rows": [
    	   ]};*/
    	
    	$scope.barChartByMobile.data = {"cols": [
    	    	                            {id: "creative", label: "Creative", type: "string"},
    	    	                            {id: "ctr_id", label: "CTR", type: "number"},
    	    	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
    	    	                            
    	    	                        ], "rows": [
    	    	   ]};
    	
    	$scope.barChartByDevice.data = {"cols": [
    	    	                            {id: "creative", label: "Creative", type: "string"},
    	    	                            {id: "ctr_id", label: "CTR", type: "number"},
    	    	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
    	    	                            
    	    	                        ], "rows": [
    	    	   ]};
    	
    	$scope.barChartByOS.data = {"cols": [
    	    	                            {id: "creative", label: "Creative", type: "string"},
    	    	                            {id: "ctr_id", label: "CTR", type: "number"},
    	    	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
    	    	                            
    	    	                        ], "rows": [
    	    	   ]};
    };
    
    
    
    
}).factory('barChartService', function($http) {
	 
	   return {
		   
	       
	        
	        getPerformanceByOSBarChartData: function() {
	        	
	             return $http.get('/performanceByOSBarChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	        },
	        
	        getPerformanceByDeviceBarChartData: function() {
	        	
	             return $http.get('/performanceByDeviceBarChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	        },
	        
	        getPerformanceBymobileWebVsAppBarChartData: function() {
	        	
	             return $http.get('/mobileWebVsAppBarChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
	                       .then(function(result) {
	                            //resolve the promise as the data
	                            return result.data;
	                        });
	        }
	        
	   }
	});

 angular.module('barChartPerfAdSizeApp', ['googlechart']).controller("barChartCtrl", function ($scope,$rootScope,barChartPerfAdSizeService) {
	var barChart1 = {};
	$scope.initdata = function(){
		$("#performanceAdSizeLoaderId").show();
		try{
		
			$scope.performanceByAdSizeChartData = barChartPerfAdSizeService.getPerformanceByAdSizeChartData().then(function(chartData) {
		     	if(chartData !=undefined && chartData !=null){
		     		$scope.performanceByAdSizeChartData= $scope.$eval($scope.$eval("(" + chartData + ")"));
				        $scope.chartData = [];
				        //console.log($scope.performanceByAdSizeChartData);
				        angular.forEach( $scope.performanceByAdSizeChartData, function(value, key){ 		        	
				        	$scope.chartData.push({ c: [ { v: value.creativeSize}, {v:value.CTR},{v:value.benchCtr}]});
			            }); 
				        //console.log($scope.chartData);
				        barChart1.data = {"cols": [
				      	                            {id: "creative", label: "Creative", type: "string"},
				      	                            {id: "ctr_id", label: "CTR", type: "number"},
				      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
				      	                            
				      	                        ], "rows": $scope.chartData
				      	 };    
			            
			      	    $scope.barChart1=barChart1;
			      	    $("#performanceAdSizeLoaderId").hide(); 	    	
		     	}
 	

			});
}catch(err){
	console.log("getPerformanceByAdSizeChartData:err:"+err);	
}
}
	$scope.initdata();
	
	$scope.response= $('#amount2').val();
	barChart1.type = "ComboChart";
	   barChart1.displayed = false;
	   barChart1.cssStyle = "height:80%; width:80%;";
	   barChart1.data = {"cols": [
	                           {id: "creative", label: "Creative", type: "string"},
	                           {id: "ctr_id", label: "CTR", type: "number"},
	                           {id: "ctr_id2", label: "BENCHMARK", type: "number"}
	                           
	                       ], "rows": [
	  ]};
	
	  barChart1.options = {
	 		vAxis: {title: "CTR%"},
	 		hAxis: {title: "Ad Size"},
	 		"title":"CTR% by Ad Size",
	 		seriesType: "bars",
	 		series: {1: {type: "line"}}
	  };
	  barChart1.formatters = {
			   "number": [
			              {
			                columnNum: 1,
			                suffix: '%',
			       			fractionDigits:4
			       			//pattern:'#,###%'			
			             }
			            ]
	   };
	  $scope.barChart1 = barChart1;
	  
	  
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


	$scope.changeCombo1=function(){	
		  $scope.chartData = [];
		  angular.forEach( $scope.performanceByAdSizeChartData, function(value, key){
	        	$scope.chartData.push({ c: [ { v: value.creativeSize}, {v:value.CTR},{v:parseFloat($('#amount2').val())}]});
          }); 
	        barChart1.data = {"cols": [
	      	                            {id: "creative", label: "Creative", type: "string"},
	      	                            {id: "ctr_id", label: "CTR", type: "number"},
	      	                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
	      	                            
	      	                        ], "rows": $scope.chartData
	      	   };  
          
    	    $scope.barChart1=barChart1;
		 	  
		  };   
       
	$scope.emptyData = function() {    	
		$scope.barChart1.data = {"cols": [
		                            {id: "creative", label: "Creative", type: "string"},
		                            {id: "ctr_id", label: "CTR", type: "number"},
		                            {id: "ctr_id2", label: "BENCHMARK", type: "number"}
		                            
		                        ], "rows": [
		   ]};
};

$scope.chartReady = function() {    	
    fixGoogleChartsBarsBootstrap();
};

function fixGoogleChartsBarsBootstrap() {
    $(".google-visualization-table-table img[width]").each(function(index, img) {
        $(img).css("width", $(img).attr("width")).css("height", $(img).attr("height"));
    });
};

}).factory('barChartPerfAdSizeService', function($http) {
	return {
		
		 getPerformanceByAdSizeChartData: function() {
             return $http.get('/performanceByAdSizeChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
                       .then(function(result) {
                            //resolve the promise as the data
                            return result.data;
                        });
        }
	}
});





