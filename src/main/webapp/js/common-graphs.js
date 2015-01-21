google.load('visualization', '1', {'packages': ['geochart']});		 
google.load('visualization', '1.0', {'packages':['controls']});
google.load('visualization', '1', {'packages':['corechart']});

var lastPopUpId = 0;

function  pieChartGeneration(actionName,divName,chartTitle,piechartWidth){
	 var url="/"+actionName+".lin";
	 $.ajax({
	       type : "POST",
	       url : url,
	       cache: false,
	       data : {
		    	   startDate:startDate,
		    	   endDate:endDate,
		    	   compareStartDate:compareStartDate,
			       compareEndDate:compareEndDate,
			       allChannelName : allChannelName,
			       selectedPublisher : selectedPublisher
		       },	       
	       dataType: 'json',
	       success: function (data) {	

	           var mapObj=data['publisherMap'];
	           if(mapObj != null) {
		           var dataStr =mapObj['datastr'];
		           var options = {
		        		      title: chartTitle,
		        		      width : piechartWidth,
		        		      height : 200,
		        		      legend:{alignment: 'center'},
		        		      chartArea:{width:"94%",height:"100%"}
		        		    };
		           google.setOnLoadCallback(drawPieChart(divName,chartTitle,dataStr,options));
		           $("#IrPieChart_div_zoom").attr("onclick","zoomInPieChart('"+chartTitle+"', 'IrPieChart_div_zoom',"+dataStr+","+modalheaderHeight+","+modalheaderWidth+");");
	           }
	           else {
	        	   $('#'+divName).html("No Data Found");
	        	   
	        	   $("#IrPieChart_div_zoom").removeAttr("onclick");
	           }

	       },
	       error: function(jqXHR, exception) {	
	
	       }
	  });
	
 }


function geoChartGeneration(actionName,divName,chartTitle,channelName){
	 var url="/"+actionName+".lin";	
	 var chartOptions = {
				region : "US",
				displayMode : "markers",
				resolution:'provinces',
				colorAxis : {colors: ['red','yellow', 'green'],displayMode: 'auto'}
	 };
  try{
	 $.ajax({
	       type : "POST",
	       url : url,
	       cache: false,
	       data : {
	    	   startDate:startDate,
	    	   endDate:endDate,
	    	   compareStartDate:compareStartDate,
		       compareEndDate:compareEndDate,
		       channel : channelName,
		       selectedPublisher : selectedPublisher
	       },	    	       
	       dataType: 'json',
	       success: function (data) {
	        	  var mapObj=data['publisherMap'];
	        	  var dataStr =mapObj['datastr'];
	        	  google.setOnLoadCallback(drawGeoChart(divName,dataStr,chartOptions));
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
  }catch(error){
		alert("geoChartGeneration:"+error);
  }
}


function drawGeoChart(divName, chartDataStr,chartOptions) {
	var dataArray = eval('(' + chartDataStr + ')');	
	var data = google.visualization.arrayToDataTable(dataArray);
	var chart = null;
	$("#" + divName).html("");
	chart = new google.visualization.GeoChart(document.getElementById(divName));
	chart.draw(data, chartOptions);
};

function zoomInDrawGeoChart(divName, chartDataStr,chartOptions) {
	var data = google.visualization.arrayToDataTable(chartDataStr);
	var chart = null;
	$("#" + divName).html("");
	chart = new google.visualization.GeoChart(document.getElementById(divName));
	chart.draw(data, chartOptions);
};


function drawPieChart(divName,title,dataStr,options,formattedData) { 
	var json_arr = eval( '(' + dataStr + ')');
    var data = google.visualization.arrayToDataTable(json_arr);
    var chart = new google.visualization.PieChart(document.getElementById(divName));
    chart.draw(formattedData, options);
    var item =null;
	google.visualization.events.addListener(chart, 'select', function() {
	  var selection = chart.getSelection();
	  for (var i = 0; i < selection.length; i++) {
		item = selection[i];
		}
	  showOptimizedChannelPerformancePopup(divName, data.getValue(item.row, 0));
    
  });
}


function zoomInDrawPieChart(divName,title,dataStr,options) { 
    var data = google.visualization.arrayToDataTable(dataStr);
    var chart = new google.visualization.PieChart(document.getElementById(divName));
    chart.draw(data, options);
    var item =null;
	google.visualization.events.addListener(chart, 'select', function() {
	  var selection = chart.getSelection();
	  for (var i = 0; i < selection.length; i++) {
		item = selection[i];
		}
  });
}


function lineChart(divName, title, dataStr, color,graphWidth,height) {
if(dataStr != '')
{
	var json_arr = eval('(' + dataStr + ')');
	var data = google.visualization.arrayToDataTable(json_arr);
	var options = {
		title : title,
		width : graphWidth,
		height : height,
		//colors : [ color ],
		//colors : [ 'green','orange','black','blue','red','brown','grey','pink','green','orange','black','blue','red','brown','grey','pink'],
		legend : {position : 'none'},
		hAxis : {
			title : 'Date',
			titleTextStyle : {color : color}
			},
		titleTextStyle : {
			color : 'black',
			fontSize : '16'
			}

	};

	var chart = new google.visualization.LineChart(document.getElementById(divName));
	chart.draw(data, options);
}
else
{
	$('#'+divName).html("No data found for the selected filter");
}
}

function zoomInLineChartGeneration(divName, title, dataStr, color,modalheaderWidth,modalheaderHeight) {
	var data = google.visualization.arrayToDataTable(dataStr);

	var options = {
		title : title,
		width : modalheaderWidth,
		height : modalheaderHeight,
		colors : [ 'green','orange','black','blue','red','brown','grey','pink','light-blue'],
		legend : {position : 'none'},
		hAxis : {
			title : 'Date',
			titleTextStyle : {color : color}
			},
		titleTextStyle : {
			color : 'black',
			fontSize : '16'
			}

	};

	var chart = new google.visualization.LineChart(document.getElementById(divName));
	chart.draw(data, options);
}

function createEmptyPopup(id, title) {
	var content='<div id="content">'
		  +'<div id="popover_content_wrapper" style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
		  +'<div class="popheading_outer" >'              
		  +'<div id="popheading" class="popup_heading" style="background-color:#FDEFBC;height:25px;">'
		  +'<div class="pop_heading_left_name" style="font-weight:bold;color:black;margin-left:1%;width:29%;float:left;"><b> </b></div>'
		  +'<div class="pop_heading_left_value" style="width:29%;float:left;"> </div>'
		  +'<div class="pop_heading_right_name" style="font-weight:bold;color:black;width:29%;float:left;text-align:right;"><b> </b></div>'
		  +'<div class="pop_heading_right_value" style="margin-right:5px;float:right;"> </div>'
		  +'</div>'
		  +'<div class="sub_heading" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;"><span class="sub_heading_left" style="margin-left:55%;"><b>Life Time</b></span>'
		  +'<span class="sub_heading_right" style="float:right;"><b>'+timePeriod+'</b></span></div>'
		  +'<div class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
		  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Impressions Delivered:</div>'
		  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;"> </div>'
		  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;"> </div>'
		  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
		  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Clicks:</div>'
		  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;"> </div>'
		  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;"> </div>'
		  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
		  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">CTR(%):</div>'
		  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;"> </div>'
		  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;"> </div>'
		  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
		  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Delivered:</div>'
		  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;"> </div>'
		  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;"> </div>'
		  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
		  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Remaining:</div>'
		  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;"> </div>'
		  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;"> </div>'
		  +'</div><div id="popup_chart_div" style="width: 100%; height: 50px;"><div id="lineItemPopupLoaderId" style="text-align:center;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div></div>'
	 	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
	 	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
	 	  +'>View Trends</a>'
	 	  //+'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
	 	  //+' onclick=javascript:reAllocation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
	 	  +'</div></div></div>';
	
	if(lastPopUpId != 0 && lastPopUpId != id) {
		$('#'+lastPopUpId).popover('hide');
	}
	lastPopUpId = id;
	
    $("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+title+"</span><br/><span style='margin-left:2px;'></span><span style='margin-left:10px;margin-top:2px;color:red;'></span>");	
	$("#"+id).attr('data-toggle',"popover");

	var options={
			html: true,
            trigger: 'manual',
            content: content,
            placement:'bottom',
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
		
}

function makePopUP(id,title,subTitle,contentDiv,chartData) {
    $("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+title+"</span>"+"<br/>"+subTitle);	
    
	$('#lineItemPopupLoaderId').css('display','none');
	if(lastPopUpId == id) {			// display only if the popup is still open. 
		
		 $('#content').html(contentDiv);
	
		 chartElementId="popup_chart_div";			
		 google.setOnLoadCallback(drawPopupChart(chartElementId,chartData));
	}
}

//Trigger for the hiding


function drawPopupChart(chartElementId,chartData){
	if(chartData != null) {
	   var jsonData = eval('(' + chartData + ')');
	   
		var data=google.visualization.arrayToDataTable(jsonData);
	     
	     var options = {
	       title: 'IMPRESSIONS',
	       width: 535,
	       height: 200,
	       hAxis: {title: 'Date',  titleTextStyle: {color: 'red'}},
	       legend:{position: 'none'}
	     };
	
	     var chart = new google.visualization.AreaChart(document.getElementById(chartElementId));
     chart.draw(data, options);  
	}
	else {
		$('#'+chartElementId).html("<br><br><center><h3>No Data Found For Graph</h3></center>");
	}
}


 google.setOnLoadCallback(drawDashboard);

 function drawDashboard() {

	        // Create our data table.
	        var data = google.visualization.arrayToDataTable([
	          ['Name', 'Dollar'],
	          ['Michael' , 5],
	          ['Elisa', 7],
	          ['Robert', 3]
	          
	        ]);

	        // Create a dashboard.
	        var dashboard = new google.visualization.Dashboard(
	            document.getElementById('dashboard_div'));

	        // Create a range slider, passing some options
	        var donutRangeSlider = new google.visualization.ControlWrapper({
	          'controlType': 'NumberRangeFilter',
	          'containerId': 'filter_div',
	          'options': {
	            'filterColumnLabel': 'Dollar'
	          }
	        });

	        // Create a pie chart, passing some options
	        var pieChart = new google.visualization.ChartWrapper({
	          'chartType': 'PieChart',
	          'containerId': 'chart_div',
	          'options': {
	            'width': 300,
	            'height': 300,
	            'pieSliceText': 'value',
	            'legend': 'right'
	          }
	        });

	    
	        dashboard.bind(donutRangeSlider, pieChart);

	        // Draw the dashboard.
	        dashboard.draw(data);
 }	

 function drawPublisherAllocationChart(chartData,chartDiv,ChartOptions){
	 google.setOnLoadCallback(drawAreaChart(chartData,ChartOptions,"chart_div_realloc1"));
		
 }
 function drawAreaChart(chartData,ChartOptions,chartDiv) {
	 if(chartData == undefined){
		 chartData = google.visualization.arrayToDataTable([
				[ 'Days', 'Mojiva', 'Nexage', 'Google Ad Exchange' ],
				[ '21', 0.25, 0.23, 0.21 ], [ '22', 0.24, 0.22, 0.19 ],
				[ '23', 0.29, 0.24, 0.23 ], [ '24', 0.28, 0.26, 0.27 ],
				[ '25', 0.29, 0.21, 0.24 ], [ '26', 0.28, 0.26, 0.21 ],
				[ '27', 0.31, 0.28, 0.26 ]

		]);
	 }
     if(ChartOptions == undefined){
    	 ChartOptions= {
    	            title: 'eCPM ($)',
    	            width: 1000,
    	            height: 400,
    	            hAxis: {title: 'Date',  titleTextStyle: {color: 'red'}},
    	 	        legend:{position: 'none'}
    	         };
     }   

      var chart = new google.visualization.AreaChart(document.getElementById(chartDiv));
      chart.draw(chartData, ChartOptions);
   }
 
 function deliveryIndicaterGraph(forcastDTOStatus,delivered,lineItem,forcasted) {
	 
	  var data = google.visualization.arrayToDataTable([
       ['Line Item', 'Delivered','Forecasted','w'],
       [lineItem,  delivered,forcasted,0]
      
     ]);
    if(forcastDTOStatus){
    	 var options = {
    		       title: 'DELIVERY INDICATOR',
    		       vAxis: {title: '',  titleTextStyle: {color: 'red'}},
    		 	  width:400,
    		 	  height:200,
    		 	  isStacked:true,
    		 	  colors: ['black','green','red'],
    		 	  legend:{position: 'none'}
    		     };

	  }else{
		  var options = {
			       title: 'DELIVERY INDICATOR',
			       vAxis: {title: '',  titleTextStyle: {color: 'red'}},
			 	  width:400,
			 	  height:200,
			 	  isStacked:true,
			 	  colors: ['black','red','green'],
			 	  legend:{position: 'none'}
			     };

	  }
    
     var chart = new google.visualization.BarChart(document.getElementById('chart_div_traffer'));
     chart.draw(data, options)
 }
 
 