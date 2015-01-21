'use strict';

angular.module('videoCampaignLineChartApp', ['googlechart']).controller("videoCampaignLineChartCtrl", function ($scope,videoCampaignLineChartDataService,$rootScope) {
	
	var chartMapData = {};
    var totalData;
	var byRateData;
	var byLengthData;
	var byEventData;
	
	var dataReady = 0;
	
	$scope.initdata = function() {
			$('#noVideoDataMsgId').css('display','none');
			$('#videoCampaignLineChartDiv').css('display','block');
		
		    $("#byRateChartDiv").hide();
		    $("#byRateChartLoaderId").show();
		    $("#byRateChartLoaderId > div").hide();
		    
		    $("#byLengthChartDiv").hide();
		    $("#byLengthChartLoaderId").show();
		    $("#byLengthChartLoaderId > div").hide();
		    
		    $("#byEventChartDiv").hide();
		    $("#byEventChartLoaderId").show();
		    $("#byEventChartLoaderId > div").hide();
		    
		    $('#average_Interaction_Rate').html("");
			$('#average_View_Rate').html("");
			$('#completion_Rate').html("");
			$('#firstQuartile').html("");
			$('#midpoint').html("");
			$('#third_Quartile').html("");
			$('#complete').html("");
			$('#start').html("");
			$('#pause').html("");
			$('#resume').html("");
			$('#rewind').html("");
			$('#mute').html("");
			$('#unmute').html("");
			$('#fullScreen').html("");
		    
		    var byRateChartData;
		    var byRateColumns = [];
		    var byRateSeries = {};
		    var byRateOptions={};
		    var byRateChart = {};
		    
		    var byLengthChartData;
		    var byLengthColumns = [];
		    var byLengthSeries = {};
		    var byLengthOptions={};
		    var byLengthChart = {};
		    
		    var byEventChartData;
		    var byEventColumns = [];
		    var byEventSeries = {};
		    var byEventOptions={};
		    var byEventChart = {};
    
		    try{
		    	$scope.lineChartData = videoCampaignLineChartDataService.getLineChartData().then(function(chartData) {  
		    		if(chartData == undefined || chartData == null || (JSON.stringify(chartData)) == '{}') {
		    			dataReady = 0;
		    			$('#videoCampaignLineChartDiv').css('display','none');
		    			$('#noVideoDataMsgId').css('display','block');
		    		}
		    		else {
		    			dataReady = 1;
		    			chartMapData = chartData;
		    			byRateData=chartMapData['byRate'];
		    			byLengthData=chartMapData['byLength'];
		    			byEventData=chartMapData['byEvent'];
		    			totalData=chartMapData['videoCampaignDataSum'];
		    			
		    			/*$scope.drawByRateLineChart();
		    			$scope.drawByLengthLineChart();
		    			$scope.drawByEventLineChart();
		    			showTotalData();*/
		    			$scope.fillData();
		    		}
		    	});
		    	
		    	$scope.fillData = function() {
		    		if(dataReady == 1) {
		    			
		    			byRateChartData = {};
		    		    byRateColumns = [];
		    		    byRateSeries = {};
		    		    byRateOptions={};
		    		    byRateChart = {};
		    		    
		    		    byLengthChartData = {};
		    		    byLengthColumns = [];
		    		    byLengthSeries = {};
		    		    byLengthOptions={};
		    		    byLengthChart = {};
		    		    
		    		    byEventChartData = {};
		    		    byEventColumns = [];
		    		    byEventSeries = {};
		    		    byEventOptions={};
		    		    byEventChart = {};
		    			
		    			$scope.drawByRateLineChart();
		    			$scope.drawByLengthLineChart();
		    			$scope.drawByEventLineChart();
		    			
		    	    	$('#average_Interaction_Rate').html(totalData.average_Interaction_Rate);
		    			$('#average_View_Rate').html(totalData.average_View_Rate);
		    			$('#completion_Rate').html(totalData.completion_Rate);
		    			
		    			$('#firstQuartile').html(totalData.firstQuartile);
		    			$('#midpoint').html(totalData.midpoint);
		    			$('#third_Quartile').html(totalData.third_Quartile);
		    			$('#complete').html(totalData.complete);
		    			
		    			$('#start').html(totalData.start);
		    			$('#pause').html(totalData.pause);
		    			$('#resume').html(totalData.resume);
		    			$('#rewind').html(totalData.rewind);
		    			$('#mute').html(totalData.mute);
		    			$('#unmute').html(totalData.unmute);
		    			$('#fullScreen').html(totalData.fullScreen);
		    		}
		    	}
    	
		    	$scope.drawByRateLineChart = function() {
		    		byRateChartData= eval("(" + byRateData + ")");
	    			if(!(byRateData != null && byRateData.length > 0)) {
	    				   $("#byRateChartDiv").hide();
			               $(".byRateChartLoaderId > img").hide();
			               $(".byRateChartLoaderId > div").show();
		        	   }
		        	   else {
		        		   $("#byRateChartDiv").show();
		        		   byRateChart.data = byRateChartData;    
			            	
			    			$("#chartType option[value='? undefined:undefined ?']").remove();
			    			byRateChart.methods = {};
			    			if(byRateChartData !=null){
			    				for (var i = 0; i < byRateChartData.rows[0].c.length; i++) {
			    					byRateColumns.push(i);
			    					if (i > 0) {
			    						byRateSeries[i - 1] = {};
			    					}
			    				}
			    				byRateOptions = { series: byRateSeries, pointSize: 4, colors:['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080']}
			    			}
			    			byRateChart.view = {
			    					columns: byRateColumns
			    			};
	            	
			    			byRateChart.type = "LineChart";
			    			byRateChart.displayed = false;
			    			byRateChart.cssStyle = "height:400px; width:100%;";
			    			byRateChart.formatters = {};
	              
			    			byRateChart.options = {
			    					"isStacked": "true",
			    					"fill": 20,
			    					"displayExactValues": true,
			    					"pointSize": 4,
			    					"colors":['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080'],
			    					"vAxis": {
			    						"title": "By Rate", "gridlines": {"count": 10}, "format":"##.####"
			    					},
			    					"hAxis": {
			    						"title": "Date"	            	
			    					}
			    			};
			    			
			    			 $("#byRateChartLoaderId").hide();
			    			 $scope.byRateChart = byRateChart;
		        	   }
	    			
	    			$scope.byRateChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {
	    		        	var col = sel[0].column;           	
	    		            if (byRateColumns[col] == col) {
	    		                // hide the data series
	    		            	byRateColumns[col] = {
	    		                    label: $scope.byRateChart.data.cols[col].label,
	    		                    type: $scope.byRateChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		                byRateSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	byRateColumns[col] = col;
	    		            	byRateSeries[col-1].color = null;
	    		            }
	    		           $scope.byRateChart.options=byRateOptions;
	    		           $scope.byRateChart.view = {columns: byRateColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    	$scope.drawByLengthLineChart = function() {
		    		byLengthChartData= eval("(" + byLengthData + ")");
	    			if(!(byLengthData != null && byLengthData.length > 0)) {
	    				   $("#byLengthChartDiv").hide();
			               $(".byLengthChartLoaderId > img").hide();
			               $(".byLengthChartLoaderId > div").show();
		        	   }
		        	   else {
		        		   $("#byLengthChartDiv").show();
		        		   byLengthChart.data = byLengthChartData;    
			            	
			    			$("#chartType option[value='? undefined:undefined ?']").remove();
			    			byLengthChart.methods = {};
			    			if(byLengthChartData !=null){
			    				for (var i = 0; i < byLengthChartData.rows[0].c.length; i++) {
			    					byLengthColumns.push(i);
			    					if (i > 0) {
			    						byLengthSeries[i - 1] = {};
			    					}
			    				}
			    				byLengthOptions = { series: byLengthSeries, pointSize: 4, colors:['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080']}
			    			}
			    			byLengthChart.view = {
			    					columns: byLengthColumns
			    			};
	            	
			    			byLengthChart.type = "LineChart";
			    			byLengthChart.displayed = false;
			    			byLengthChart.cssStyle = "height:400px; width:100%;";
			    			byLengthChart.formatters = {};
	              
			    			byLengthChart.options = {
			    					"isStacked": "true",
			    					"fill": 20,
			    					"displayExactValues": true,
			    					"pointSize": 4,
			    					"colors":['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080'],
			    					"vAxis": {
			    						"title": "By Length", "gridlines": {"count": 10}, "format":"##.####"
			    					},
			    					"hAxis": {
			    						"title": "Date"	            	
			    					}
			    			};
			    			
			    			 $("#byLengthChartLoaderId").hide();
			    			 $scope.byLengthChart = byLengthChart;
		        	   }
	    			
	    			$scope.byLengthChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {
	    		        	var col = sel[0].column;           	
	    		            if (byLengthColumns[col] == col) {
	    		                // hide the data series
	    		            	byLengthColumns[col] = {
	    		                    label: $scope.byLengthChart.data.cols[col].label,
	    		                    type: $scope.byLengthChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		                byLengthSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	byLengthColumns[col] = col;
	    		            	byLengthSeries[col-1].color = null;
	    		            }
	    		           $scope.byLengthChart.options=byLengthOptions;
	    		           $scope.byLengthChart.view = {columns: byLengthColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    	$scope.drawByEventLineChart = function() {
		    		byEventChartData= eval("(" + byEventData + ")");
	    			if(!(byEventData != null && byEventData.length > 0)) {
	    				   $("#byEventChartDiv").hide();
			               $(".byEventChartLoaderId > img").hide();
			               $(".byEventChartLoaderId > div").show();
		        	   }
		        	   else {
		        		   $("#byEventChartDiv").show();
		        		   byEventChart.data = byEventChartData;    
			            	
			    			$("#chartType option[value='? undefined:undefined ?']").remove();
			    			byEventChart.methods = {};
			    			if(byEventChartData !=null){
			    				for (var i = 0; i < byEventChartData.rows[0].c.length; i++) {
			    					byEventColumns.push(i);
			    					if (i > 0) {
			    						byEventSeries[i - 1] = {};
			    					}
			    				}
			    				byEventOptions = { series: byEventSeries, pointSize: 4, colors:['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080']}
			    			}
			    			byEventChart.view = {
			    					columns: byEventColumns
			    			};
	            	
			    			byEventChart.type = "LineChart";
			    			byEventChart.displayed = false;
			    			byEventChart.cssStyle = "height:400px; width:100%;";
			    			byEventChart.formatters = {};
	              
			    			byEventChart.options = {
			    					"isStacked": "true",
			    					"fill": 20,
			    					"displayExactValues": true,
			    					"pointSize": 4,
			    					"colors":['#4cc0bf', '#acd43c','#f37f04', '#c3392d', '#e83e6b', '#FF0000', '#000080'],
			    					"vAxis": {
			    						"title": "By Event", "gridlines": {"count": 10}, "format":"##.####"
			    					},
			    					"hAxis": {
			    						"title": "Date"	            	
			    					}
			    			};
			    			
			    			 $("#byEventChartLoaderId").hide();
			    			 $scope.byEventChart = byEventChart;
		        	   }
	    			
	    			$scope.byEventChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {
	    		        	var col = sel[0].column;           	
	    		            if (byEventColumns[col] == col) {
	    		                // hide the data series
	    		            	byEventColumns[col] = {
	    		                    label: $scope.byEventChart.data.cols[col].label,
	    		                    type: $scope.byEventChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		                byEventSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	byEventColumns[col] = col;
	    		            	byEventSeries[col-1].color = null;
	    		            }
	    		           $scope.byEventChart.options=byEventOptions;
	    		           $scope.byEventChart.view = {columns: byEventColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    }catch(err){
		    	console.log("videoCampaignLineChartDataService: err: "+err);
		    }
    
    	}   
    
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
    	                        { 
    	                        	name: "arrow",  checked:false,
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



    	$scope.initdata(); 
    	
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
    
	    $scope.emptyData = function() {   
	    	//$('#linechartdatadiv').hide();
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
	    
	}).factory('videoCampaignLineChartDataService', function($http) {
		return { 
			getLineChartData: function() {
				return $http.get('/videoCampaignData.lin?orderId='+campaignId)
	                      .then(function(result) {
	                    	  console.log("result.data");
	                    	  console.log(result.data);
	                           return result.data;
	                       });
	       }
	
	 }

});

