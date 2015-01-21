'use strict';

angular.module('lineChartApp', ['googlechart']).controller("lineChartCtrl", function ($scope,lineChartDataService,$rootScope) {
	
	var data1, data2, data3;
	var columns = [];
    var series = {};
    var options={};
    
    $("#performanceSiteLoaderId").show();
    try{
    	$scope.lineChartData = lineChartDataService.getLineChartData().then(function(chartData) {     		
        	if(chartData!=undefined || chartData!=null){    		
            	var ctrData=chartData['CTR'];
            	var impData=chartData['Impressions'];
            	var clickData=chartData['Clicks'];            	 
            	data1= eval("(" + ctrData + ")");        	
            	data2= eval("(" + impData + ")");
            	data3= eval("(" + clickData + ")");
            	//$('#linechartdatadiv').show();
            	$scope.chart.data =data1;    
            	$("#chartType").val("CTR");
            	$("#performanceSiteLoaderId").hide();
            	$("#chartType option[value='? undefined:undefined ?']").remove();
            	
            	
            	
            	if(data1 !=null){
                	for (var i = 0; i < data1.rows[0].c.length; i++) {
                    	columns.push(i);
                        if (i > 0) {
                            series[i - 1] = {};
                        }
                    }
                	options = { series: series}
                }
            	$scope.chart.view = {
               		   columns: columns
               	};
            	
            	  
             };  
        });
    }catch(err){
    	console.log("lineChartDataService: err: "+err);
    }
    
    $scope.initdata = function(){
    	
    	 
    }   
    
    
    var chart1 = {};
    chart1.type = "LineChart";
    chart1.displayed = false;
    chart1.cssStyle = "height:400px; width:100%;";
    chart1.formatters = {};
  
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

    
    chart1.options = {
	        "title": "CTR per day",
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
    
    $scope.chart = chart1;
    
    $scope.chartSelectionChange = function () {    	
   	 
    	 if ($scope.displaydatatype=='CTR') {
    		 $scope.chart.options=options;
    		 $("#chartType").val("CTR");
    		
    		 $scope.chart.data = data1;
    		 chart1.options = {
    			        "title": "CTR per day",
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
			/*chart1.formatters = {
   			     "number": [
   			       {
   			            columnNum: 2,
   			            suffix: '%',
						fractionDigits:4
   			      }
   			     ]
   	        }*/
   		    
	        $scope.chart.view = {
	        		   columns: columns
	        };
			
           
    	}else if($scope.displaydatatype=='Impressions') {
    		 $scope.chart.options=options;
    		 $("#chartType").val("Impressions");            
    		 if(data2 !=undefined){
    			 $scope.chart.data = data2;
    			/* var formatAray=[];
    			 for(var i=2;i<=data2['cols'].length;i++){
    				 var format={ columnNum: i, pattern: '###,####' };
    				 formatAray.push(format);    				 
    			 }
    			 chart1.formatters = {
        			     "number": formatAray
        	     }*/
    			 
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
        		 $scope.chart.view = {
                		   columns: columns
                 }; 
        		 //console.log(chart1);
        		 
 			}
             
    	}else if($scope.displaydatatype=='Clicks') {
    		 $scope.chart.options=options;
    		 $("#chartType").val("Clicks");
       		 $scope.chart.data = data3;
    		
       		 chart1.options = {
			        "title": "Clicks per day",
			        "isStacked": "true",
			        "fill": 20,
			        "displayExactValues": true,
			        "vAxis": {
			            "title": "Clicks", "gridlines": {"count": 10},"format":"###,####"
			        },
			        "hAxis": {
			            "title": "Date"
			        }
	         };
       		/*chart1.formatters = {
   			     "number": [
   			       {
   			            columnNum: 2,
   						pattern: '###,####'
   			      }
   			     ]
   	        };*/
       		
            $scope.chart.view = {
         		   columns: columns
         	};
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
    
    $scope.chart.methods = {};
    
    $scope.chart.methods.select = function(selection, event){
    	var sel = selection;	       
        // if selection length is 0, we deselected an element
        if (sel.length > 0) {            
        	console.log('sel[0].row.......'+sel[0].row);
        	var col = sel[0].column;           	
            if (columns[col] == col) {
                // hide the data series
                columns[col] = {
                    label: $scope.chart.data.cols[col].label,
                    type: $scope.chart.data.cols[col].type,
                    calc: function () {
                        return null;
                    }
                };
                // grey out the legend entry
                series[col-1].color = '#CCCCCC';
            }
            else {
                // show the data series
                columns[col] = col;
                series[col-1].color = null;
            }
           $scope.chart.options=options;
           $scope.chart.view = {columns: columns };
        	
            
        }
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
    
    $scope.initdata();
    
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
    
}).factory('lineChartDataService', function($http) {
	return { 
		   getLineChartData: function() {
			   if(campaignId !=null && campaignId.length>0){				   
				   return $http.get('/lineChartData.lin?orderId='+campaignId+'&isNoise='+isNoise+'&threshold='+threshold)
		           .then(function(result) {
		        	   if(result.data != null && result.data != undefined && ((result.data["CTR"] != null && result.data["CTR"].length > 0) || (result.data["Clicks"] && result.data["Clicks"].length > 0) || (result.data["Impressions"] && result.data["Impressions"].length > 0))) {
			        	   $('#emptyChartdiv').hide();
			               $('#linechartdatadiv').show();
			               $('#linechartdataSelectBox').show();
		        	   }
		        	   else {
		        		   console.log(result.data);
		        		   $('#emptyChartdiv').show();
			               $('#linechartdatadiv').hide();
			               $('#linechartdataSelectBox').hide();
		        	   }
		                return result.data;
		            });
			   }else{
				   return null;
			   }
		    }
	 }

});
