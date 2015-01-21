'use strict';

angular.module('lineChartPublisherApp', ['googlechart']).controller("lineChartpublisherCtrl", function ($scope,lineChartDataPublisherService,$rootScope) {
	
	
	$scope.initdata = function(){
		    $("#ecpmChartDiv").hide();
		    $("#fillChartDiv").hide();
		    $("#revenueChartDiv").hide();
		    $("#impressionChartDiv").hide();
		    $("#clickChartDiv").hide();
		    $("#ctrChartDiv").hide();
		    $("#ecpmChartLoaderId").show();
		    $("#ecpmChartLoaderId > div").hide();
		    $("#fillRateChartLoaderId").show();
		    $("#fillRateChartLoaderId > div").hide();
		    $("#revenueChartLoaderId").show();
		    $("#revenueChartLoaderId > div").hide();
		    $("#impressionChartLoaderId").show();
		    $("#impressionChartLoaderId > div").hide();
		    $("#clickChartLoaderId").show();
		    $("#clickChartLoaderId > div").hide();
		    $("#ctrChartLoaderId").show();
		    $("#ctrChartLoaderId > div").hide();
		    var ecpmChartData, fillRateData, revenueChartData,impressionData,clickChartData,ctrChartData;
		    var ecpmColumns = [];
		    var ecpmSeries = {};
		    var fillColumns = [];
		    var fillSeries = {};
		    var fillOptions={};
		    var ecpmOptions={};
		    var chartMapData = {};
		    var ecpmChart = {};
		    var fiilChart = {};
		    var revenueChart = {};
		    var impressionChart = {};
		    var clickChart = {};
		    var ctrChart = {};
   
    
		    try{
		    	$scope.lineChartData = lineChartDataPublisherService.getpublisherLineChartData().then(function(chartData) {     		
		    		if(chartData!=undefined || chartData!=null){
		    			chartMapData = chartData;
		    			var ecpmData=chartData['Ecpm'];
		    			ecpmChartData= eval("(" + ecpmData + ")");
		    			if(!(chartData != null && chartData != undefined && (ecpmData != null && ecpmData.length > 0))) {
		    				 console.log(result.data);
		    				   $("#ecpmChartDiv").hide();
				               $(".ecpmChartLoaderId > img").hide();
				               $(".ecpmChartLoaderId > div").show();
			        	   }
			        	   else {
			        		   $("#ecpmChartDiv").show();
				               ecpmChart.data = ecpmChartData;    
				            	
				    			$("#chartType option[value='? undefined:undefined ?']").remove();
				    			ecpmChart.methods = {};
				    			//fiilChart.methods = {};
				    			if(ecpmChartData !=null){
				    				for (var i = 0; i < ecpmChartData.rows[0].c.length; i++) {
				    					ecpmColumns.push(i);
				    					if (i > 0) {
				    						ecpmSeries[i - 1] = {};
				    					}
				    				}
				    				ecpmOptions = { series: ecpmSeries}
				    			}
				    			ecpmChart.view = {
				    					columns: ecpmColumns
				    			};
		            	
				    			ecpmChart.type = "LineChart";
				    			ecpmChart.displayed = false;
				    			ecpmChart.cssStyle = "height:400px; width:100%;";
				    			ecpmChart.formatters = {};
		              
				    			ecpmChart.options = {
				    					"isStacked": "true",
				    					"fill": 20,
				    					"displayExactValues": true,
				    					"vAxis": {
				    						"title": "ECPM", "gridlines": {"count": 10}, "format":"##.####"
				    					},
				    					"hAxis": {
				    						"title": "Date"	            	
				    					}
				    			};
				    			
				    			 $("#ecpmChartLoaderId").hide();
				    			 $scope.ecpmChart = ecpmChart;
				    			
			        	   }
		    			
		    			$scope.ecpmChart.methods.select = function(selection, event){
		    		    	var sel = selection;	       
		    		        // if selection length is 0, we deselected an element
		    		        if (sel.length > 0) {            
		    		        	console.log('sel[0].row.......'+sel[0].row);
		    		        	var col = sel[0].column;           	
		    		            if (ecpmColumns[col] == col) {
		    		                // hide the data series
		    		            	ecpmColumns[col] = {
		    		                    label: $scope.ecpmChart.data.cols[col].label,
		    		                    type: $scope.ecpmChart.data.cols[col].type,
		    		                    calc: function () {
		    		                        return null;
		    		                    }
		    		                };
		    		                // grey out the legend entry
		    		                ecpmSeries[col-1].color = '#CCCCCC';
		    		            }
		    		            else {
		    		                // show the data series
		    		            	ecpmColumns[col] = col;
		    		            	ecpmSeries[col-1].color = null;
		    		            }
		    		           $scope.ecpmChart.options=ecpmOptions;
		    		           $scope.ecpmChart.view = {columns: ecpmColumns };
		    		        	
		    		            
		    		        }
		    		    }
		    			
		    			$scope.fillRateChartData(); 
		    			$scope.revenueChartData();
		    			$scope.impressionChartData();
		    			$scope.clicksChartData();
		    			$scope.ctrChartData();
		    		};  
		    	});
    	
		    	$scope.fillRateChartData = function() {
		    		var fillData = chartMapData['FillRate'];
		    		fillRateData= eval("(" + fillData + ")");  
		    		if(!(fillData != null && fillData.length > 0)) {
		    			   $("#fillChartDiv").hide();  
			               $("#fillRateChartLoaderId > img").hide();
			               $("#fillRateChartLoaderId > div").show();
		    		}else{
		    			 $("#fillChartDiv").show();
		    			 $("#fillRateChartLoaderId > img").show();
		    		// return fillRateData;
		    		//console.log("data = "+data)
		    		fiilChart.data = fillRateData;
		    		if(fillRateData !=null){
	    				for (var i = 0; i < fillRateData.rows[0].c.length; i++) {
	    					fillColumns.push(i);
	    					if (i > 0) {
	    						fillSeries[i - 1] = {};
	    					}
	    				}
	    				fillOptions = { series: fillSeries}
	    			}
		    		fiilChart.view = {
		    				columns: fillColumns
		    		};
    		
		    		fiilChart.type = "LineChart";
		    		fiilChart.displayed = false;
		    		fiilChart.cssStyle = "height:400px; width:100%;";
		    		fiilChart.formatters = {};
          
		    		
            
		    		fiilChart.options = {
		    				"isStacked": "true",
		    				"fill": 20,
		    				"displayExactValues": true,
		    				"vAxis": {
		    					"title": "FILLRATE", "gridlines": {"count": 10}, "format":"##.####"
		    				},
		    				"hAxis": {
		    					"title": "Date"	            	
		    				}
		    		};
		    		$("#fillRateChartLoaderId").hide();
		    		$scope.fillRateChart = fiilChart;
		    	 }
		    		fiilChart.methods = {};
		    		fiilChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {            
	    		        	console.log('sel[0].row.......'+sel[0].row);
	    		        	var col = sel[0].column;           	
	    		            if (fillColumns[col] == col) {
	    		                // hide the data series
	    		            	fillColumns[col] = {
	    		                    label: $scope.fillRateChart.data.cols[col].label,
	    		                    type: $scope.fillRateChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		                fillSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	fillColumns[col] = col;
	    		            	fillSeries[col-1].color = null;
	    		            }
	    		           $scope.fillRateChart.options=fillOptions;
	    		           $scope.fillRateChart.view = {columns: fillColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
    	
		    	$scope.revenueChartData = function() {
		    		var revenueData = chartMapData['Revenue'];
		    		var revenueColumns = [];
		    		var revenueSeries = {};
		    		var revenueOptions = {};
		    		revenueChartData= eval("(" + revenueData + ")");  
		    		if(!(revenueData != null && revenueData.length > 0)) {
		    			   $("#revenueChartDiv").hide();  
			               $("#revenueChartLoaderId > img").hide();
			               $("#revenueChartLoaderId > div").show();
		    		}else{
		    			 $("#revenueChartDiv").show();
		    			 $("#revenueChartLoaderId > img").show();
		    		revenueChart.data = revenueChartData;
		    		if(revenueChartData !=null){
	    				for (var i = 0; i < revenueChartData.rows[0].c.length; i++) {
	    					revenueColumns.push(i);
	    					if (i > 0) {
	    						revenueSeries[i - 1] = {};
	    					}
	    				}
	    				revenueOptions = { series: revenueSeries}
	    			}
		    		revenueChart.view = {
		    				columns: revenueColumns
		    		};

		    		revenueChart.type = "LineChart";
		    		revenueChart.displayed = false;
		    		revenueChart.cssStyle = "height:400px; width:100%;";
		    		revenueChart.formatters = {};
         
          
           
		    		revenueChart.options = {
		    				"isStacked": "true",
		    				"fill": 20,
		    				"displayExactValues": true,
		    				"vAxis": {
		    					"title": "REVENUE", "gridlines": {"count": 10}, "format":"##.####"
		    				},
		    				"hAxis": {
		    					"title": "Date"	            	
		    				}
		    		};
		    		$("#revenueChartLoaderId").hide();
		    		$scope.revenueChart = revenueChart;
		    	}
		    		revenueChart.methods = {};
		    		revenueChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {            
	    		        	console.log('sel[0].row.......'+sel[0].row);
	    		        	var col = sel[0].column;           	
	    		            if (revenueColumns[col] == col) {
	    		                // hide the data series
	    		            	revenueColumns[col] = {
	    		                    label: $scope.revenueChart.data.cols[col].label,
	    		                    type: $scope.revenueChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		            	revenueSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	revenueColumns[col] = col;
	    		            	revenueSeries[col-1].color = null;
	    		            }
	    		           $scope.revenueChart.options=revenueOptions;
	    		           $scope.revenueChart.view = {columns: revenueColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    	$scope.impressionChartData = function() {
		    		var impData = chartMapData['Impressions'];
		    		var impColumns = [];
		    		var impSeries = {};
		    		var impOptions = {};
		    		impressionData= eval("(" + impData + ")");  
		    		if(!(impData != null && impData.length > 0)) {
		    			   $("#impressionChartDiv").hide();  
			               $("#impressionChartLoaderId > img").hide();
			               $("#impressionChartLoaderId > div").show();
		    		}else{
		    			 $("#impressionChartDiv").show();
		    			 $("#impressionChartLoaderId > img").show();
		    		impressionChart.data = impressionData;
		    		if(impressionData !=null){
	    				for (var i = 0; i < impressionData.rows[0].c.length; i++) {
	    					impColumns.push(i);
	    					if (i > 0) {
	    						impSeries[i - 1] = {};
	    					}
	    				}
	    				impOptions = { series: impSeries}
	    			}
		    		impressionChart.view = {
		    				columns: impColumns
		    		};
   		
		    		impressionChart.type = "LineChart";
		    		impressionChart.displayed = false;
		    		impressionChart.cssStyle = "height:400px; width:100%;";
		    		impressionChart.formatters = {};
         
          
           
		    		impressionChart.options = {
		    				"isStacked": "true",
		    				"fill": 20,
		    				"displayExactValues": true,
		    				"vAxis": {
		    					"title": "IMPRESSION", "gridlines": {"count": 10}, "format":"##.####"
		    				},
		    				"hAxis": {
		    					"title": "Date"	            	
		    				}
		    		};
		    		$("#impressionChartLoaderId").hide();
		    		$scope.impressionChart = impressionChart;
		    	}
		    		impressionChart.methods = {};
		    		impressionChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {            
	    		        	console.log('sel[0].row.......'+sel[0].row);
	    		        	var col = sel[0].column;           	
	    		            if (impColumns[col] == col) {
	    		                // hide the data series
	    		            	impColumns[col] = {
	    		                    label: $scope.impressionChart.data.cols[col].label,
	    		                    type: $scope.impressionChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		            	impSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	impColumns[col] = col;
	    		            	impSeries[col-1].color = null;
	    		            }
	    		           $scope.impressionChart.options=impOptions;
	    		           $scope.impressionChart.view = {columns: impColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    	$scope.clicksChartData = function() {
		    		var clickData = chartMapData['Clicks'];
		    		var clickColumns = [];
		    		var clickSeries = {};
		    		var clickOptions = {};
		    		clickChartData= eval("(" + clickData + ")");  
		    		if(!(clickData != null && clickData.length > 0)) {
		    			   $("#clickChartDiv").hide();  
			               $("#clickChartLoaderId > img").hide();
			               $("#clickChartLoaderId > div").show();
		    		}else{
		    			 $("#clickChartDiv").show();
		    			 $("#clickChartLoaderId > img").show();
		    		clickChart.data = clickChartData;
		    		if(clickChartData !=null){
	    				for (var i = 0; i < clickChartData.rows[0].c.length; i++) {
	    					clickColumns.push(i);
	    					if (i > 0) {
	    						clickSeries[i - 1] = {};
	    					}
	    				}
	    				clickOptions = { series: clickSeries}
	    			}
		    		clickChart.view = {
		    				columns: clickColumns
		    		};
   		
		    		clickChart.type = "LineChart";
		    		clickChart.displayed = false;
		    		clickChart.cssStyle = "height:400px; width:100%;";
		    		clickChart.formatters = {};
         
          
           
		    		clickChart.options = {
		    				"isStacked": "true",
		    				"fill": 20,
		    				"displayExactValues": true,
		    				"vAxis": {
		    					"title": "CLICKS", "gridlines": {"count": 10}, "format":"##.####"
		    				},
		    				"hAxis": {
		    					"title": "Date"	            	
		    				}
		    		};
		    		$("#clickChartLoaderId").hide();
		    		$scope.clickChart = clickChart;
		    	}
		    		clickChart.methods = {};
		    		clickChart.methods.select = function(selection, event){
	    		    	var sel = selection;	       
	    		        // if selection length is 0, we deselected an element
	    		        if (sel.length > 0) {            
	    		        	console.log('sel[0].row.......'+sel[0].row);
	    		        	var col = sel[0].column;           	
	    		            if (clickColumns[col] == col) {
	    		                // hide the data series
	    		            	clickColumns[col] = {
	    		                    label: $scope.clickChart.data.cols[col].label,
	    		                    type: $scope.clickChart.data.cols[col].type,
	    		                    calc: function () {
	    		                        return null;
	    		                    }
	    		                };
	    		                // grey out the legend entry
	    		            	clickSeries[col-1].color = '#CCCCCC';
	    		            }
	    		            else {
	    		                // show the data series
	    		            	clickColumns[col] = col;
	    		            	clickSeries[col-1].color = null;
	    		            }
	    		           $scope.clickChart.options=clickOptions;
	    		           $scope.clickChart.view = {columns: clickColumns };
	    		        	
	    		            
	    		        }
	    		    }
		    	}
		    	
		    		$scope.ctrChartData = function() {
		    			var ctrData = chartMapData['CTR'];
		    			var ctrColumns = [];
			    		var ctrSeries = {};
			    		var ctrOptions = {};
		    			ctrChartData= eval("(" + ctrData + ")");  
		    			if(!(ctrData != null && ctrData.length > 0)) {
			    			   $("#ctrChartDiv").hide();  
				               $("#ctrChartLoaderId > img").hide();
				               $("#ctrChartLoaderId > div").show();
			    		}else{
			    			 $("#ctrChartDiv").show();
			    			 $("#ctrChartLoaderId > img").show();
    	 	   ctrChart.data = ctrChartData;
    	 		if(ctrChartData !=null){
    				for (var i = 0; i < ctrChartData.rows[0].c.length; i++) {
    					ctrColumns.push(i);
    					if (i > 0) {
    						ctrSeries[i - 1] = {};
    					}
    				}
    				ctrOptions = { series: ctrSeries}
    			}
    	 	   ctrChart.view = {
    	 			   columns: ctrColumns
    	 	   };

    	 	   ctrChart.type = "LineChart";
    	 	   ctrChart.displayed = false;
    	 	   ctrChart.cssStyle = "height:400px; width:100%;";
    	 	   ctrChart.formatters = {};
         
          
           
    	 	   ctrChart.options = {
    	 			   "isStacked": "true",
    	 			   "fill": 20,
    	 			   "displayExactValues": true,
    	 			   "vAxis": {
    	 				   "title": "CTR", "gridlines": {"count": 10}, "format":"##.####"
    	 			   },
    	 			   "hAxis": {
    	 				   "title": "Date"	            	
    	 			   }
    	 	   };
    	 	   $("#ctrChartLoaderId").hide();
    	 	   $scope.ctrChart = ctrChart;
		    		}
		    			ctrChart.methods = {};
		    			ctrChart.methods.select = function(selection, event){
		    		    	var sel = selection;	       
		    		        // if selection length is 0, we deselected an element
		    		        if (sel.length > 0) {            
		    		        	console.log('sel[0].row.......'+sel[0].row);
		    		        	var col = sel[0].column;           	
		    		            if (ctrColumns[col] == col) {
		    		                // hide the data series
		    		            	ctrColumns[col] = {
		    		                    label: $scope.ctrChart.data.cols[col].label,
		    		                    type: $scope.ctrChart.data.cols[col].type,
		    		                    calc: function () {
		    		                        return null;
		    		                    }
		    		                };
		    		                // grey out the legend entry
		    		            	ctrSeries[col-1].color = '#CCCCCC';
		    		            }
		    		            else {
		    		                // show the data series
		    		            	ctrColumns[col] = col;
		    		            	ctrSeries[col-1].color = null;
		    		            }
		    		           $scope.ctrChart.options=ctrOptions;
		    		           $scope.ctrChart.view = {columns: ctrColumns };
		    		        	
		    		            
		    		        }
		    		    }
		    		}
    	
		    }catch(err){
		    	console.log("lineChartDataPublisherService: err: "+err);
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
    	};   
    
 
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
	    	$('#linechartdatadiv').hide();
	    	/* 	$scope.chart.data = {"cols": [
                        {id: "date", label: "Date", type: "string"},
                        {id: "lin.khon-id", label: "lin.khon", type: "number"}
                        ], 
                        "rows": [
                                  {c:[{v:2013-10-10},{v:0}]}
                                ]
                      };*/
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
	    
	   	
    
	}).factory('lineChartDataPublisherService', function($http) {
		return { 
			getpublisherLineChartData: function() {
				if(startDate!=null && startDate.length>0 && endDate!=null && endDate.length>0 && allChannelName!=null && allChannelName.length>0 && selectedPublisher!=null && selectedPublisher.length>0){	
					// alert(selectedPublisher)
					return $http.get('/acualLineChartPublisher.lin?startDate='+startDate+'&endDate='+endDate+'&allChannelName='+allChannelName+'&selectedPublisher='+selectedPublisher)
					.then(function(result) {
						/* if(result.data != null && result.data != undefined && ((result.data["CTR"] != null && result.data["CTR"].length > 0) || (result.data["Clicks"] && result.data["Clicks"].length > 0) || (result.data["Impressions"] && result.data["Impressions"].length > 0))) {
			        	   $('#emptyChartdiv').hide();
			               $('#linechartdatadiv').show();
			               $('#linechartdataSelectBox').show();
		        	   }
		        	   else {
		        		   console.log(result.data);
		        		   $('#emptyChartdiv').show();
			               $('#linechartdatadiv').hide();
			               $('#linechartdataSelectBox').hide();
		        	   }*/
						return result.data;
					});
				}else{
					return null;
				}
			}
	
	 }

});
