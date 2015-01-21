google.load("visualization", "1", {packages:["corechart"]});



  function drawChart() {
    var data = google.visualization.arrayToDataTable([
      ['Task', 'Hours per Day'],
      ['Site 1',    43],
      ['Site 2',      40],
      ['Site 3',  29]
    ]);

    var options = {
      title: '',
      width:'360',
      height:'150',
      legend:{alignment: 'center'},
      chartArea:{width:"98%",height:"93%"},
      backgroundColor: 'transparent',
      colors: ['#23adde', '#86af4e', '#1fbbae']

    };

    var chart = new google.visualization.PieChart(document.getElementById('chart_div_rich'));
    chart.draw(data, options);
  }
 

  function drawChartcoupon() {
    var data = google.visualization.arrayToDataTable([
      ['Task', 'Hours per Day'],
      ['Coupon 1',    30],
      ['Coupon 2',      25]
    ]);

    var options = {
      title: '',
      width:'340',
      height:'150',
      legend:{alignment: 'center'},
      chartArea:{width:"98%",height:"99%"},
      backgroundColor: 'transparent',
      colors: ['#23adde', '#86af4e', '#1fbbae']
     
    };

    var chart = new google.visualization.PieChart(document.getElementById('chart_div_coupon'));
    chart.draw(data, options);
  }
 

var timePeriod='LAST 7 DAYS';

var startDate;
var endDate;
var compareStartDate;
var compareEndDate ;

var oldStartDate ;
var oldEndDate ;
var oldPublisher;

var advertisers ='' ;
var agencies = '';
var lineItem = '';
var order = '';
var selectedPublisher = '';
var SelectedProperty ='';
var loadAjaxCounter = 0;
var loadAjaxCounter1 = 0;
var loadAjaxCounter2 = 0;
var arrLoadAjaxCounter=[];
var isTrendDefault = true;
var orderIdReallocation = '';

var optimizingFlag = 0;
var advertiserTotalCurrentData;
var advertiserTotalCompareData;
var advertiserTotalMTDData;
var deliveryIndicatorData;
var leftFilterStatus;

var performanceSummaryHeaderFilteredData = new Array();

var performerFilteredData = new Array();
var performerFilteredDeliveryIndicatorData = new Array();

var nonPerformerFilteredData = new Array();
var nonPerformerFilteredDeliveryIndicatorData = new Array();

var mostActiveFilteredData = new Array();
var mostActiveFilteredDeliveryIndicatorData = new Array();

var topGainersFilteredData = new Array();
var topGainersFilteredDeliveryIndicatorData = new Array();

var topLosersFilteredData = new Array();
var topLosersFilteredDeliveryIndicatorData = new Array();

var performanceMetricsFilteredData = new Array();
var performanceMetricsFilteredDeliveryIndicatorData = new Array();

function updateAjaxCount(val,str) {
	if ( arrLoadAjaxCounter.indexOf(str) == -1 &&  val==1){

		arrLoadAjaxCounter.push(str);
		loadAjaxCounter = loadAjaxCounter + 1;
		loadAjaxCounter1 = loadAjaxCounter1 + 1;
		//alert("oncounter"+loadAjaxCounter1);
		//alert("online")
		
	}else if( arrLoadAjaxCounter.indexOf(str) != -1 &&  val==-1) {
		//alert("offine")
		index = arrLoadAjaxCounter.indexOf($.trim(str));
		loadAjaxCounter2 = loadAjaxCounter2 + 1;
		arrLoadAjaxCounter.splice(index,1);
		loadAjaxCounter = loadAjaxCounter -1;
		//alert("offcounter"+loadAjaxCounter2);
	}

	//alert("counter"+loadAjaxCounter);
	if(loadAjaxCounter >= 1) {
		//alert("on");
		$("#ajax_id").css({'display':'block'});
	}
	else if(loadAjaxCounter <= 0) {
		//alert("off");
		$("#ajax_id").css({'display':'none'});
	}
	
	//alert(arrLoadAjaxCounter);
}

var width;
var graphWidth;
var height;
var modalheaderWidth;
var modalheaderHeight;

$(window).load(function(){	
	 loadFilterData();
	
	 width = $(window).width();
	 h =$(window).height();
	 graphWidth = parseInt((width-100)/3) ;
	 height = parseInt((graphWidth/3)*2) ;
	 modalheaderWidth = parseInt((width*3)/4) ;
     modalheaderHeight = parseInt(h/2) ;
    
     screenWidthDiffrence = width - (modalheaderWidth+100);
     marginModalRight = parseInt(screenWidthDiffrence/2);
     marginModalLeft = parseInt(screenWidthDiffrence/2);
     modalHeaderWidthdiv = modalheaderWidth +100 ;
     $(".modal").css({'width':modalHeaderWidthdiv,'left':marginModalLeft,'right':marginModalRight});
     $(".modal-body").css({'max-height':modalheaderHeight});
 });


function loadAllDataAdvertiser(){
	//alert("Clicked on Apply");
	getLeftFilterStatus();
	
	advertisers = advertisername;
	agencies = agencyname;
	lineItem = lineItemArr;
	properties = SelectedProperty;
	order = ordername;
	orderIdReallocation = orderIdReallocationFilter;
	if(tabName == null || tabName == undefined || tabName.trim() == "") {
		tabName = "Performance Summary";
	}
	
	if( tabName == "Performance Summary" && checkFilterForAdvertiserPerformance30Days(startDate, endDate)) {
		loadAllData();
	}
	else if( tabName == "Trends and Analysis" && checkFilterForAdvertiserTrends30Days(startDate, endDate)) {
		loadAllData();
	}else if(tabName == "Reallocation" && checkFilterForAdvertiserTrends30Days(startDate, endDate)){
		loadAllData();
	}

}

function loadAllData() {
	//updateAjaxCount(1 , 'loadAllData' );
	
    var myVar=setTimeout(function(){myTimer()},1000);
   
}

function myTimer(){

    //getAdvertiserReallocationData(); 
	//forcastLineGeneration(); required for phase 2

loadPerformanceMetrics();
loadPerformanceSummaryHeaderData();
loadPerformerLineItems();
loadNonPerformerLineItems();
loadMostActiveLineItems();
loadTopGainers();
loadTopLosers();
getAdvertiserReallocationData();
if( tabName == "Trends and Analysis") {
	isTrendDefault = false;
	getAdvertiserTrendAnalysisHeaderData();
	actualLineGeneration();
	getActualAdvertiserData();
	getForcastAdvertiserData();
	//forcastLineGeneration();
}
else {
	isTrendDefault = true;
	// do nothing
}

generateGeoGraph('advertiserGeoMapByLocation',"loadAdvertiserBylocationGraphData","By Location","by_location_zoom");
generateGeoGraph('advertiserGeoMapByMarket',"loadAdvertiserByMarketGraphData","By Market","by_market_zoom");
// updateAjaxCount(-1 , 'loadAllData' );

/*if(!(oldStartDate == startDate && oldEndDate == endDate && oldPublisher == selectedPublisher))
{
	//alert("1");
  oldStartDate = startDate;
  oldEndDate = endDate;
  oldPublisher = selectedPublisher;
  optimizingFlag = 0;

  getAdvertiserTotalCurrent();
  getAdvertiserTotalCompare();
  getAdvertiserTotalMTD();
  getDeliveryIndicatorData();
  
}
else
{
	getPerformanceSummaryHeaderFilteredData();
	getTrendAnalysisHeaderFilteredData();
	getPerformerFilteredData();
 	getNonPerformerFilteredData();
 	getMostActiveFilteredData();
 	getTopGainersFilteredData();
 //	getTopLosersFilteredData();
 	getPerformanceMetricsFilteredData();
	
	loadOptimizedPerformanceSummaryHeaderData();
	loadOptimizedTrendAnalysisHeaderData();
	loadOptimizedPerformerLineItems();
	loadOptimizedNonPerformerLineItems();
	loadOptimizedMostActiveLineItems();
	loadOptimizedTopGainers();
	loadOptimizedTopLosers();
	loadOptimizedPerformanceMetrics();
}*/
}
function actualLineGeneration() {
	
	//updateAjaxCount(1 , 'actualLineGeneration' );
	$.ajax({
		type : "POST",
		url : "/acualLineChart.lin",
		cache : false,
		data : {
			  publisherName : selectedPublisher,
			  startDate : startDate,
			  endDate : endDate,
			  order : ordername,
			  lineItem : lineItemArr,
			  properties : SelectedProperty
		},	
		dataType : 'json',
		success : function(data) {
			var mapObj = data['headerMap'];
			var title = "";
			var impressionStr = mapObj['impression'];
			
			var clicksStr = mapObj['click'];
			var ctrStr = mapObj['ctr'];
			var divNameImp = "chart_div_left3";
			var divNameClick = "chart_div_left1";
			var divNameCtr = "chart_div_left2";			
			lineChart(divNameImp, 'ACTUAL IMPRESSIONS', impressionStr, 'blue',graphWidth,height);
			lineChart(divNameClick, 'ACTUAL CLICKS', clicksStr, 'green',graphWidth,height);
			lineChart(divNameCtr, 'ACTUAL CTR', ctrStr, 'red',graphWidth,height);
			
			$("#chart_div_left3_icon").attr("onclick","zoomInLineChart('ACTUAL IMPRESSIONS','right',"+impressionStr+",'chart_div_left3_icon','blue',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_left1_icon').attr("onclick","zoomInLineChart('ACTUAL CLICKS','left',"+clicksStr+",'chart_div_left1_icon','green',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_left2_icon').attr("onclick","zoomInLineChart('ACTUAL CTR','left',"+ctrStr+",'chart_div_left2_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
		
			//updateAjaxCount(-1 , 'actualLineGeneration' );
		},
		error : function(jqXHR, exception) {
			//updateAjaxCount(-1 , 'actualLineGeneration' );
		}
	});

}

function forcastLineGeneration() {
	//updateAjaxCount(1 , 'forcastLineGeneration' );
	$.ajax({
		type : "POST",
		url : "/forcastLineChart.lin",
		cache : false,
		data : {
			  publisherName : selectedPublisher,
			  startDate : startDate,
			  endDate : endDate,
			  order : ordername,
			  lineItem : lineItemArr,
			 properties : SelectedProperty
			 },	
		dataType : 'json',
		success : function(data) {			      	   
			var mapObj = data['headerMap'];
			var title = "";
			var impressionStr = mapObj['impression'];
			var clicksStr = mapObj['click'];
			var ctrStr = mapObj['ctr'];
			var divNameImp = "chart_div_right3";
			var divNameClick = "chart_div_right1";
			var divNameCtr = "chart_div_right2";
			google.setOnLoadCallback(lineChart(divNameImp, 'FORECASTED IMPRESSIONs', impressionStr, 'blue',graphWidth,height));
			google.setOnLoadCallback(lineChart(divNameClick, 'FORECASTED CLICKS', clicksStr, 'green',graphWidth,height));
			google.setOnLoadCallback(lineChart(divNameCtr, 'FORECASTED CTR', ctrStr, 'red',graphWidth,height));
			
			$('#chart_div_right3_icon').attr("onclick","zoomInLineChart('FORECASTED IMPRESSION','right',"+impressionStr+",'chart_div_right3_icon','blue',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_right1_icon').attr("onclick","zoomInLineChart('FORECASTED CLICKS','left',"+clicksStr+",'chart_div_right1_icon','green',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_right2_icon').attr("onclick","zoomInLineChart('FORECASTED CTR','left',"+ctrStr+",'chart_div_right2_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
			//updateAjaxCount(-1 , 'forcastLineGeneration' );
		},
		error : function(jqXHR, exception) {
			//updateAjaxCount(-1 , 'forcastLineGeneration' );
		}
	});

}

function getAdvertiserTrendAnalysisHeaderData() {
	//updateAjaxCount(1 , 'getAdvertiserTrendAnalysisHeaderData' );
	var order = '';
	var lineItem ='';
	var headerDiv = '';
	
	order = ordername;
	lineItem = lineItemArr;
		
	  var count = 0;
	  
	  $.ajax({
		  type : "POST",
		  url : "/loadAdvertiserTrendsAnalysisHeaderDate.lin",
		  cache : false,
		  data : {
			  publisherName : selectedPublisher,
			  startDate : startDate,
			  endDate : endDate,
			  order : order,
			  lineItem : lineItem,
			  properties : SelectedProperty
		  		},
			  dataType: 'json',
		       success: function (data) {	
				//	updateAjaxCount(-1 , 'getAdvertiserTrendAnalysisHeaderData' );
		           $.each(data, function(index, element) {
		        	  if (index == 'advertiserTrendAnalysisHeaderList' && element != null  && element != undefined  && element.length > 0) {
							jsonResponse=element;	
							headerDiv= getAdvetiserTrendsAnalysisHeader(
			 		        		 jsonResponse[0].bookedImpressions,
			 		        		 jsonResponse[0].impressions,
			 		        		 jsonResponse[0].lifeTimeImpresions,
			 		        		 jsonResponse[0].clicks,
			 		        		 jsonResponse[0].lifeTimeClicks,
			 		        		 jsonResponse[0].budget,
			 		        		 jsonResponse[0].CTR,
			 		        		 jsonResponse[0].totalCTR,
			 		        		 jsonResponse[0].revenue,
			 		        		 jsonResponse[0].revenueDelivered,
			 		        		 jsonResponse[0].revenueRemaining);
							
							
					  }	
		        	  else if (index == 'advertiserTrendAnalysisHeaderList' && element.length == 0) {
							headerDiv= getAdvetiserTrendsAnalysisHeader(
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0",
			 		        		"0");
							
							
					  }
		        	  $('#advertiser_trends_analysis_header').html(headerDiv);
		           });
		       },
		       error: function(jqXHR, exception) {
		    	//	updateAjaxCount(-1 , 'getAdvertiserTrendAnalysisHeaderData' );
		    	  //alert("getAdvertiserTrendAnalysisHeaderData: exception:"+exception);
		        }
		  });

}

function getAdvetiserTrendsAnalysisHeader(bookedImpressions,impressions,lifeTimeImpresions,clicks,lifeTimeClicks,budget,CTR,totalCTR,revenue,revenueDelivered,revenueRemaining){
	var headerDivData="";
	
	var bookedImp = "NA";
	   if(bookedImpressions > 0) {
		   bookedImp = formatInt(bookedImpressions);
	   }
	
	headerDivData=headerDivData
	+'<div style="width:98%;float:left;">'
	/*+'<div class="summary_bar">'
	+'<div style="">TOTAL BUDGET</div>'
	+'<div class="summary_value">$'+formatFloat(budget,2)+'</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">TOTAL REVENUE DELIVERED</div>'
	+'<div class="summary_value">$'+formatFloat(revenueDelivered,2)+'</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">TOTAL REVENUE REMAINING</div>'
	+'<div class="summary_value">$'+formatFloat(revenueRemaining,2)+'</div>'
	+'</div>'
	+'<div class="summary_bar" >'
	+'<div style="">BOOKED IMPRESSIONS</div>'
	+'<div class="summary_value">'+bookedImp+'</div>'		
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">LIFETIME IMPRESSIONS</div>'
	+'<div class="summary_value">'+formatInt(lifeTimeImpresions)+'</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">LIFETIME CLICKS</div>'
	+'<div class="summary_value">'+formatInt(lifeTimeClicks)+'</div>'	
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">LIFETIME CTR</div>'
	+'<div class="summary_value">'+formatFloat(totalCTR,4)+'%</div>'
	+'</div>'
	+'</div>'
	
	+'<div style="width:98%;float:left;clear:both;margin-top:10px">'*/
	+'<div class="summary_bar">'
	+'<div style="">IMPRESSIONS</div>'
	+'<div class="summary_value">'+formatInt(impressions)+'</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">CLICKS</div>'
	+'<div class="summary_value">'+formatInt(clicks)+'</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">CTR</div>'
	+'<div class="summary_value">'+formatFloat(CTR,4)+'%</div>'
	+'</div>'
	+'<div class="summary_bar">'
	+'<div style="">BUDGET</div>'
	+'<div class="summary_value">$'+formatFloat(budget,2)+'</div>'
	+'</div>'
	+'</div>';

	
return headerDivData;
	
}


function getActualAdvertiserData(){	
	  
	//updateAjaxCount(1 , 'getActualAdvertiserData' );
	jQuery('#actualAdvertiserTable').dataTable().fnClearTable();
	var row = "";
	   $.ajax({
	 		       type : "POST",
	 		      url : "/actualAdvertiser.lin",
	 		       cache: false,
	 		       data : {
	 		    	    publisherName : selectedPublisher,
	 					startDate : startDate,
	 					endDate : endDate,
	 					order : ordername,
	 					lineItem : lineItemArr,
	 					properties : SelectedProperty
	 				},	
	 		        dataType: 'json',
	 		        success: function (data) {
	 		           jQuery('#actualAdvertiserTable').dataTable().fnClearTable();
	 		          jQuery('#actualAdvertiserTable').dataTable().fnSettings()._iDisplayLength = 10;
	 		          jQuery('#actualAdvertiserTable').dataTable().fnDraw();
	 		           $.each(data, function(index, element) {	
	 		        	 
	 		        	   if(index == 'advertiserTrendAnalysisActualDatarList' && element != null  && element != undefined  && element.length > 0) {
	 		        		  var dataList=data['advertiserTrendAnalysisActualDatarList'];
	 		        		  
	 		        	//// for loop start/////	 		        	   
	 		        		  var key = 0;
	 		        		  for (var i = 0 ; i < dataList.length ; i = i + 1) {
	 		        			   (function(i) {
	 		        				     setTimeout( function(i) {
	 		        			    	   
	 		        			           // code-here
	  
	 		        		  
	 		        		  
			 		        	   		        		   
			 		        		  var dtoObject = dataList[key];
			 		        		  /*row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.date+"</td><td>"+dtoObject.order+"</td><td style='text-align:right;'>"+dtoObject.lineItem+"</td><td style='text-align:right;'>"+formatInt(dtoObject.deliveredImpression)+"</td><td style='text-align:right;'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;'>"+formatFloat(dtoObject.CTR,true)+"%</td><td style='text-align:right;'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td></tr>";*/
			 		        		 
					        		   jQuery('#actualAdvertiserTable').dataTable().fnAddData( [
						        		     dtoObject.date,
						        		     dtoObject.order,
						        		     dtoObject.lineItem,
						        		     formatInt(dtoObject.deliveredImpression),
						        		     formatInt(dtoObject.clicks),
						        		     formatFloat(dtoObject.CTR,4)+"%",
						        		     "$"+formatFloat(dtoObject.revenueDelivered,2),
						        		     "$"+formatFloat(dtoObject.revenueRemaining,2)
					        		   ]);
			 		        	   
					        		 //for loop cont.../////	 		        		  
		      	 		        		  key ++;
		       }, 10)
		   })(i)

		}	        	   
		//for loop end .../////   
					        		   
					        		   
					        		   
	 		        	   }
	 		        	   else if(index == 'advertiserTrendAnalysisActualDatarList' && element != null  && element != undefined  && element.length == 0) {		
	 		        		  row='<tr class="odd gradeX">'
	 						        /*+'<td colspan="8" style="color:red; text-align:center;">'*/
	 		        			  +'<td colspan="7" style="color:red; text-align:center;">'
	 							        +'<div class="widget alert alert-info adjusted">'
	 							        +'<i class="cus-exclamation"></i>'
	 							        +'<strong>No records found for the selected filters</strong>'
	 							        +'</div>'
	 						        +'</td>'						      
	 						        +'</tr>';
	 		        		 $("#actualAdvertiserTable tbody").append(row);
	 						}
		 		       });
	 		         
	 		         //updateAjaxCount(-1 , 'getActualAdvertiserData' );
	 		       },
	 		        error: function(jqXHR, exception) {
		 		    	//updateAjaxCount(-1 , 'getActualAdvertiserData' );
		 		    }
		 		  });

}

function getDaysDate(days) {
	  var today = new Date();
	  var dd = today.getDate()+days;
	  var mm = today.getMonth()+1; //January is 0!
	  var yyyy = today.getFullYear();
	  if(dd<10){
		  dd='0'+dd;
	  } 
	  if(mm<10){
		  mm='0'+mm;
	  }
	  today = yyyy+'-'+mm+'-'+dd;
	  return today;
}

	
	function getForcastAdvertiserData(){
		//updateAjaxCount(1 , 'getForcastAdvertiserData' );
		jQuery('#forcastAdvertiserTable').dataTable().fnClearTable();
		var row = "";
		/*
		   $.ajax({
		 		       type : "POST",
		 		       url : "/forcastAdvertiser.lin",
		 		       cache: false,
		 		       data : {
		 		    	  publisherName : selectedPublisher,
		 					startDate : startDate,
		 					endDate : endDate,
		 					order : ordername,
		 					lineItem : lineItemArr,
		 					properties : SelectedProperty
		 				},
		 		        dataType: 'json',
		 		        success: function (data) {
		 		         	jQuery('#forcastAdvertiserTable').dataTable().fnClearTable();
		 		         	jQuery('#forcastAdvertiserTable').dataTable().fnSettings()._iDisplayLength = 10;
		 		           jQuery('#forcastAdvertiserTable').dataTable().fnDraw();
		 		           $.each(data, function(index, element) {	
		 		        	   if(index == 'advertiserTrendAnalysisForcastlDatarList' && element.length>0){
		 		        		  var dataList=data['advertiserTrendAnalysisForcastlDatarList'];
			 		        	   
		 		        	//// for loop start/////	 		        	   
		 		        		  var key = 0;
		 		        		  for (var i = 0 ; i < dataList.length ; i = i + 1) {
		 		        			   (function(i) {
		 		        				     setTimeout( function(i) {
		 		        			    	   
		 		        			           // code-here		  
		 		        		  
		 		        		  
			 		        		  var dtoObject = dataList[key];
			 		        			 row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.date+"</td><td>"+dtoObject.order+"</td><td style='text-align:right;'>"+dtoObject.lineOrder+"</td><td style='text-align:right;'>"+formatInt(dtoObject.deliveredImpression)+"</td><td style='text-align:right;'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;'>"+formatFloat(dtoObject.CTR,true)+"%</td><td style='text-align:right;'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td></tr>";
			 		        			jQuery('#forcastAdvertiserTable').dataTable().fnAddData( [
											   getDaysDate(key+1),
											   dtoObject.order,
											   dtoObject.lineItem,
											   formatInt(dtoObject.deliveredImpression * ((dtoObject.clicks*.2)+1)),
											   formatInt(dtoObject.clicks * (dtoObject.deliveredImpression*.0015+1)),
											   formatFloat(formatInt(dtoObject.clicks * (dtoObject.deliveredImpression*.0015+1))/formatInt(dtoObject.deliveredImpression * ((dtoObject.clicks*.2)+1))*100,true)+"%",
											   "$"+formatFloat(dtoObject.revenueDelivered,true),
											   "$"+formatFloat(dtoObject.revenueRemaining,true)
	   					        		    //dtoObject.date,
	   					        		    // dtoObject.order,
	   					        		    // dtoObject.lineItem,
	   					        		    // formatInt(dtoObject.deliveredImpression),
	   					        		   //  formatInt(dtoObject.clicks),
	   					        		   //  formatFloat(dtoObject.CTR,true)+"%",
	   					        		    // "$"+formatFloat(dtoObject.revenueDelivered,true),
	   					        		   //  "$"+formatFloat(dtoObject.revenueRemaining,true)
	   					        		   ]);
			 		        			
			 		        			//for loop cont.../////	 		        		  
		      	 		        		  key ++;
		       }, 10)
		   })(i)

		}	        	   
		//for loop end .../////   
			 		        			
			 		        			
		 		        	   }
		 		        		 else if(index == 'advertiserTrendAnalysisForcastlDatarList' && element.length == 0){		
		 		        			row='<tr class="odd gradeX">'
		 						        +'<td colspan="8" style="color:red; text-align:center;">'
		 							        +'<div class="widget alert alert-info adjusted">'
		 							        +'<i class="cus-exclamation"></i>'
		 							        +'<strong>No records found for the selected filters</strong>'
		 							        +'</div>'
		 						        +'</td>'						      
		 						        +'</tr>';
		 		        			$("#forcastAdvertiserTable tbody").append(row);
		 						}
		 		           });
			 		      
		 		         // updateAjaxCount(-1 , 'getForcastAdvertiserData' );
			 		       },

			 		       error: function(jqXHR, exception) {
			 		    	 // updateAjaxCount(-1 , 'getForcastAdvertiserData' );
			 		        }
			 		  });	
			 		  */
			row='<tr class="odd gradeX">'
		        +'<td colspan="8" style="color:red; text-align:center;">'
			        +'<div class="widget alert alert-info adjusted">'
			        +'<i class="cus-exclamation"></i>'
			        +'<strong>No records found for the selected filters</strong>'
			        +'</div>'
		        +'</td>'						      
		        +'</tr>';
			$("#forcastAdvertiserTable tbody").append(row);
	}
	
	
	function loadPerformanceSummaryHeaderData() {
		var divContent = "";
		 var advertiserPerformanceSummaryHeaderDTOList;
		 // updateAjaxCount(1 , 'loadPerformanceSummaryHeaderData' );
		 try{
			 $.ajax({
			       type : "POST",			 		   
			       url : "/loadPerformanceSummaryHeaderData.lin",
			       data : {
			    	   publisherName : selectedPublisher,
			    	   startDate:startDate,
			    	   endDate:endDate,
			    	   advertiser:advertisername,
			    	   agency:agencyname,
			    	   properties : SelectedProperty
			    	},
			       cache: false,
			       dataType: 'json',
			       success: function (data) {	    	  
			           $.each(data, function(index, element) {
			        	  if (index == 'performanceSummaryHeaderDataList' && element != null && element != undefined && element.length>0) {
			        		  advertiserPerformanceSummaryHeaderDTOList=element;	
			        		  for(key in advertiserPerformanceSummaryHeaderDTOList){
			        			  var dtoObj = advertiserPerformanceSummaryHeaderDTOList[key];
			        			  
			        			  var bookedImpression = "NA";
				        		   if(dtoObj.bookedImpressions > 0) {
				        			   bookedImpression = formatInt(dtoObj.bookedImpressions);
				        		   }
			        			  
			    		          divContent = divContent 
			    		          	+'<div style="width:98%;float:left;">'
			    		          	/*+'<div class="summary_bar" >'
			    					+'<div style="">TOTAL BUDGET</div>'
			    					+'<div class="summary_value">$'+formatInt(dtoObj.budget)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">TOTAL REVENUE DELIVERED</div>'
			    					+'<div class="summary_value">$'+formatInt(dtoObj.totalRevenueDelivered)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">TOTAL REVENUE REMAINING</div>'
			    					+'<div class="summary_value">$'+formatInt(dtoObj.revenueRemaining)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">BOOKED IMPRESSIONS</div>'
			    					+'<div class="summary_value">'+bookedImpression+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME IMPRESSIONS</div>'
			    					+'<div class="summary_value">'+formatInt(dtoObj.totalImpressionDelivered)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME CLICKS</div>'
			    					+'<div class="summary_value">'+formatInt(dtoObj.totalClicks)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME CTR</div>'
			    					+'<div class="summary_value">'+formatFloat(dtoObj.totalCtr,4)+'</div>'
			    					+'</div>'
			    					+'</div>'
			    					
			    					+'<div style="width:98%;float:left;clear:both;margin-top:10px">'*/
			    					+'<div class="summary_bar">'
			    					+'<div style="">IMPRESSIONS</div>'
			    					+'<div class="summary_value">'+formatInt(dtoObj.impressionDelivered)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">CLICKS</div>'
			    					+'<div class="summary_value">'+formatInt(dtoObj.clicks)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">CTR</div>'
			    					+'<div class="summary_value">'+formatFloat(dtoObj.ctr,4)+'</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">BUDGET</div>'
			    					+'<div class="summary_value">$'+formatInt(dtoObj.revenueDelivered)+'</div>'
			    					+'</div>'
			    					+'</div>';
			        		  }
						  }
			        	  
			        	  else if (index == 'performanceSummaryHeaderDataList' && element.length == 0) {
			        			  
			    		          divContent = divContent 
			    		          	+'<div style="width:98%;float:left;">'
			    		          	/*+'<div class="summary_bar" >'
			    					+'<div style="">TOTAL BUDGET</div>'
			    					+'<div class="summary_value">$0.00</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">TOTAL REVENUE DELIVERED</div>'
			    					+'<div class="summary_value">$0.00</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">TOTAL REVENUE REMAINING</div>'
			    					+'<div class="summary_value">$0.00</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">BOOKED IMPRESSIONS</div>'
			    					+'<div class="summary_value">0</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME IMPRESSIONS</div>'
			    					+'<div class="summary_value">0</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME CLICKS</div>'
			    					+'<div class="summary_value">0</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">LIFETIME CTR</div>'
			    					+'<div class="summary_value">0.0000</div>'
			    					+'</div>'
			    					+'</div>'
			    					
			    					+'<div style="width:98%;float:left;clear:both;margin-top:10px">'*/
			    					+'<div class="summary_bar">'
			    					+'<div style="">IMPRESSIONS</div>'
			    					+'<div class="summary_value">0</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">CLICKS</div>'
			    					+'<div class="summary_value">0</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">CTR</div>'
			    					+'<div class="summary_value">0.0000</div>'
			    					+'</div>'
			    					+'<div class="summary_bar">'
			    					+'<div style="">BUDGET</div>'
			    					+'<div class="summary_value">$0.00</div>'
			    					+'</div>'
			    					+'</div>';
			        		  }
						  });
			        // updateAjaxCount(-1 , 'loadPerformanceSummaryHeaderData' );
			           $('#advertiser_performance_summary_header').html(divContent);
			       },
			       error: function(jqXHR, error) {
			    	  //alert("loadPerformanceSummaryHeaderData: error:"+error);
			    	// updateAjaxCount(-1 , 'loadPerformanceSummaryHeaderData' );
			        }
				
			  });
			}catch(exception){
				// updateAjaxCount(-1 , 'loadPerformanceSummaryHeaderData' );
			 // alert("loadPerformanceSummaryHeaderData: exception:"+exception);
			}
			
	}
	
	function loadPerformerLineItems(){
		   var tableTR="";
		   $("#topPerformLineItemTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="5" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topPerformLineItemTable tbody").append(loader);
		// updateAjaxCount(1 , 'loadPerformerLineItems' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadPerformerLineItems.lin",
		      cache: false,
		      data : {
		    	   publisherName : selectedPublisher,
		    	   startDate:startDate,
		    	   endDate:endDate,
		    	   advertiser:advertisername,
		    	   agency:agencyname,
		    	   properties : SelectedProperty
		    	},
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='topPerformerLineItemList' && element != null  && element != undefined  && element.length > 0) {
		        	   $.each(element,function (newIndex,newElement){	         		  
		         		   var lineItemName=newElement.campaignLineItem;
		         		   var id="topPerformer_"+newIndex;
		         		   if(newIndex == 2)
		         			{
		         			  
		         			  tableTR=tableTR
	         		   			+'<tr style="cursor:pointer;" id='+id+' >'
	         		   		+'<td onclick="showRichMedia()"><i class="cus-chart-bar"></i></td>'
	         			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
	         		   			+id+'",false) rel="popover" class="odd gradeX">'+newElement.campaignLineItem+'</td>'
	         			        
						        +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
	         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(newElement.impressionDelivered)+'</td>'						      
						        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(newElement.clicks)+'</td>'
						        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(newElement.CTR,4)+'%</td></tr>';
		         			}
		         		   else
		         			   {
		         			   
		         			  tableTR=tableTR
	         		   			+'<tr style="cursor:pointer;" id='+id+' onclick=javascript:showPerformerNonPerformerPopup("'
	         		   			+id+'",false) rel="popover" class="odd gradeX">'
	         		   		+'<td></td>'
	         		   			+'<td id="'+id+'_title" >'+newElement.campaignLineItem+'</td>'
	         			        
						        +'<td style="text-align:right;" class="">'+formatInt(newElement.impressionDelivered)+'</td>'						      
						        +'<td style="text-align:right;">'+formatInt(newElement.clicks)+'</td>'
						        +'<td style="text-align:right;">'+formatFloat(newElement.CTR,4)+'%</td></tr>';
		         			   }   
		         	   });
		        	   
		           }else if(index =='topPerformerLineItemList' && element.length==0){
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="5" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		           }
		         });
		         $("#topPerformLineItemTable tbody tr").remove();
		         $("#topPerformLineItemTable tbody").append(tableTR);
		         
		         
		         /*$("#topPerformLineItemDiv div").html('<button class="close" data-dismiss="alert">×</button>'
					+'<i class="cus-exclamation-octagon-fram"></i> <strong>NOTE : </strong>'
					+'No records found for the selected filters');*/
		      // updateAjaxCount(-1 , 'loadPerformerLineItems' );
		     },
		     error: function(jqXHR, exception) {
		    	// updateAjaxCount(-1 , 'loadPerformerLineItems' );
		   	  // alert('loadPerformerLineItems Exception:'+exception);
		     }
		   });   
		  }catch(error){
			// updateAjaxCount(-1 , 'loadPerformerLineItems' );
			  //	alert('loadPerformerLineItems error:'+error);
		  }	  
		  
	}

	function loadNonPerformerLineItems(){
		var tableTR="";
		   $("#topNonPerformLineItemTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="7" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topNonPerformLineItemTable tbody").append(loader);
		// updateAjaxCount(1 , 'loadNonPerformerLineItems' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadNonPerformerLineItems.lin",
		      cache: false,
		      data : {
		    	   publisherName : selectedPublisher,
		    	   startDate:startDate,
		    	   endDate:endDate,
		    	   advertiser:advertisername,
		    	   agency:agencyname,
		    	   properties : SelectedProperty
		    	},
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='topNonPerformerLineItemList' && element.length>0){
		        	   $.each(element,function (newIndex,newElement){
		        		   var lineItemName=newElement.campaignLineItem;
		         		   var id="topNonPerformer_"+newIndex;
		         		   
		         		   var bookedImpression = "NA";
		        		   if(newElement.bookedImpressions > 0) {
		        			   bookedImpression = formatInt(newElement.bookedImpressions);
		        		   }
		         		   
		         		   tableTR=tableTR
		         		   			+'<tr style="cursor:pointer;" id='+id+' >'
		         		   			+'<td onclick=showTraffer("'+id+'","'+newElement.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
		         			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+newElement.campaignLineItem+'</td>'
							        +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+bookedImpression+'</td>'
							        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(newElement.impressionDelivered)+'</td>'
							        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(newElement.clicks)+'</td>'
							        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(newElement.CTR,4)+'%</td>'
							        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
		         		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(newElement.deliveryIndicator,4)+'%</td></tr>';				
		         		   
		         	   });
		           }else if(index =='topNonPerformerLineItemList' && element.length==0){
		        	  
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		           }	           
		             
		         });
		         $("#topNonPerformLineItemTable tbody tr").remove();
		         $("#topNonPerformLineItemTable tbody").append(tableTR); 
		      // updateAjaxCount(-1 , 'loadNonPerformerLineItems' );
		     },
		     error: function(jqXHR, exception) {
		   	  //alert('loadNonPerformerLineItems Exception:'+exception);
		    	// updateAjaxCount(-1 , 'loadNonPerformerLineItems' );
		     }
		   });   
		  }catch(error){
			  	//alert('loadNonPerformerLineItems error:'+error);
			// updateAjaxCount(-1 , 'loadNonPerformerLineItems' );
		  }	
		  
	}

	function loadMostActiveLineItems(){	
		
		   var tableTR="";
		   $("#mostActiveLineItemTable thead tr").remove();	 
		   $("#mostActiveLineItemTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="6" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#mostActiveLineItemTable tbody").append(loader);
		   
		   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
			                   +'<th style="text-align:right;">CTR(%)</th>'
			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th>'
			                   +'<th style="text-align:right;">DELIEVERY IND</th></tr>';
		   $("#mostActiveLineItemTable thead").append(tableHeadTR); 
		   
		   //updateAjaxCount(1 , 'loadMostActiveLineItems' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadMostActiveLineItems.lin",
		      cache: false,
		      data : {
		    	  publisherName : selectedPublisher,
		    	  startDate:startDate,
		    	  endDate:endDate,
		    	  compareStartDate:compareStartDate,
		    	  compareEndDate:compareEndDate,
		    	  advertiser:advertisername,
		    	  agency:agencyname,
		    	  properties : SelectedProperty
		    	  },
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='mostActiveLineItemList' && element.length>0){
		        	   $.each(element,function (newIndex,newElement){	         		  
		         		   var ctrChange=newElement.changeInTimePeriod+"";
		         		   var lineItemName=newElement.campaignLineItem;
		         		   var id="mostActive_"+newIndex;
		         		   
		         		   var deliveryIndicator = formatFloat(newElement.deliveryIndicator,4)+"%";
		        		   if(newElement.deliveryIndicator == 10000) {
		        			   deliveryIndicator = "NA";
		        		   }
		         		   
		         		   tableTR=tableTR
		         		    +'<tr style="cursor:pointer;" id='
		         		    +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA"><td id="'
		         		    +id+'_title" rel="popover" >'
		         		    +lineItemName+'</td><td style="text-align:right;" id="'
		         		    +id+'_ctr">'
		         		    +formatFloat(newElement.CTR,4)+'%</td><td style="text-align:right;" width="56px">';
		         		   if(ctrChange.indexOf('-')>=0){
		         			  tableTR=tableTR+'<img src="img/Arrow2Down.png" width="11" height="12" style="margin-right: 5px;">';		         			      
		         		   }else{
		         			  tableTR=tableTR+'<img src="img/Arrow2Up.png" width="11" height="12" style="margin-right: 5px;">';
		         		   }
		         		  tableTR=tableTR
		         		       + formatFloat(ctrChange,4)+'%</td><td style="text-align:right;">'
		         		       +formatFloat(newElement.changeInLifeTime,4)+'%</td><td style="text-align:right;" class="">'
		         		       +formatInt(newElement.impressionDelivered)+'</td><td style="text-align:right;">'
		         		       +deliveryIndicator+'</td></tr>';
		         	   });
		           }else if(index =='mostActiveLineItemList' && element.length==0){
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		           }	           
		             
		         });
		         $("#mostActiveLineItemTable tbody tr").remove();
		         $("#mostActiveLineItemTable tbody").append(tableTR); 
		        // updateAjaxCount(-1 , 'loadMostActiveLineItems' );
		     },
		     error: function(jqXHR, exception) {
		    	 //alert('loadMostActiveLineItems Exception:'+exception);
		    	 //updateAjaxCount(-1 , 'loadMostActiveLineItems' );
		     }
		   });   
		  }catch(error){
			  //alert('loadMostActiveLineItems error:'+error);
			 // updateAjaxCount(-1 , 'loadMostActiveLineItems' );
		  }	  
		 
	}

	function loadTopGainers(){
		   var tableTR="";
		   $("#topGainersLineItemsTable thead tr").remove();
		   $("#topGainersLineItemsTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="5" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topGainersLineItemsTable tbody").append(loader);
		   
		   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
			                   +'<th style="text-align:right;">CTR(%)</th>'
			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
		   $("#topGainersLineItemsTable thead").append(tableHeadTR); 
		   	
		   //updateAjaxCount(1 , 'loadTopGainers' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadTopGainers.lin",
		      cache: false,
		      data : {
		    	  publisherName : selectedPublisher,
		    	  startDate:startDate,
		    	  endDate:endDate,
		    	  compareStartDate:compareStartDate,
		    	  compareEndDate:compareEndDate,
		    	  advertiser:advertisername,
		    	  agency:agencyname,
		    	  properties : SelectedProperty
		    	  },
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='topGainersLineItemList' && element.length>0){
		        	   $.each(element,function (newIndex,newElement){	         		  
		         		   var dataPercent=newElement.changeInLifeTime+"";		         		
		         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
		         		   
		         		   var id="topGainers_"+newIndex;		         		   
		         		   tableTR=tableTR
		         		   		   +'<tr style="cursor:pointer;" id='
		         		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA"><td id="'
		         		   		   +id+'_title" rel="popover" >'
		         		   		   +newElement.campaignLineItem+'</td><td style="text-align:right;" id="'
		         		   		   +id+'_ctr">'
		         		           +formatFloat(newElement.CTR,4)+'%</td><td style="text-align:right;" width="90px">'
		         		           +formatFloat(newElement.changeInTimePeriod,4)+'%</td><td style="text-align:right;">'
		         		           +formatFloat(newElement.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
								   +'<div class="bar" data-percentage="'
								   +dataPercent+'" style="background: green;"></div></div></td><td style="text-align:right;" class="">'
								   +formatInt(newElement.impressionDelivered)+'</td></tr>';
		         	   });
		           }else if(index =='topGainersLineItemList' && element.length==0){
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="5" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		           }	           
		             
		         });
		         $("#topGainersLineItemsTable tbody tr").remove();
		         $("#topGainersLineItemsTable tbody").append(tableTR); 
		         //updateAjaxCount(-1 , 'loadTopGainers' );
		     },
		     error: function(jqXHR, exception) {
		    	 //alert('loadTopGainers Exception:'+exception);
		    	 //updateAjaxCount(-1 , 'loadTopGainers' );
		     }
		   });   
		  }catch(error){
			  //alert('loadTopGainers error:'+error);
			  //updateAjaxCount(-1 , 'loadTopGainers' );
		  }	  
	
	}
	 
	function loadTopLosers(){
		   var tableTR="";
		   $("#topLosersLineItemsTable thead tr").remove();
		   $("#topLosersLineItemsTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="5" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topLosersLineItemsTable tbody").append(loader);
		   
		   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
			                   +'<th style="text-align:right;">CTR(%)</th>'
			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
		   $("#topLosersLineItemsTable thead").append(tableHeadTR); 
		   	 
		   //updateAjaxCount(1 , 'loadTopLosers' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadTopLosers.lin",
		      cache: false,
		      data : {
		    	  publisherName : selectedPublisher,
		    	  startDate:startDate,
		    	  endDate:endDate,
		    	  compareStartDate:compareStartDate,
		    	  compareEndDate:compareEndDate,
		    	  advertiser:advertisername,
		    	  agency:agencyname,
		    	  properties : SelectedProperty
		    	  },
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='topLosersLineItemList' && element.length>0){
		        	   $.each(element,function (newIndex,newElement){
		        		   var dataPercent=newElement.changeLifeTime+"";		        		   
		         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
		         		 
		         		   var id="topLosers_"+newIndex;		         		   
		         		   tableTR=tableTR
		         		   		   +'<tr style="cursor:pointer;" id='
		         		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA">'
		         		   		   +'<td id="'+id+'_title" rel="popover" >'
		         		   		   +newElement.campaignLineItem+'</td><td style="text-align:right;" id="'
		         		   		   +id+'_ctr">'
		         		   		   +formatFloat(newElement.CTR,4)+'%</td><td style="text-align:right;">'
		         		   		   +formatFloat(newElement.changeInTimePeriod,4)+'%</td><td style="text-align:right;">'
		         		   		   +formatFloat(newElement.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
		         		   		   +'<div class="bar" data-percentage="'
		         		   		   +dataPercent+'" ></div></div></td><td style="text-align:right;" >'
		         		   		   +formatInt(newElement.impressionDelivered)+'</td></tr>';
		         	   });
		           }else if(index =='topLosersLineItemList' && element.length==0){
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="5" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		           }	           
		             
		         });
		         $("#topLosersLineItemsTable tbody tr").remove();
		         $("#topLosersLineItemsTable tbody").append(tableTR); 
		        // updateAjaxCount(-1 , 'loadTopLosers' );
		     },
		     error: function(jqXHR, exception) {
		    	 //alert('loadTopLosers Exception:'+exception);
		    	 //updateAjaxCount(-1 , 'loadTopLosers' );
		     }
		   });   
		  }catch(error){
			  //alert('loadTopLosers error:'+error);
			 // updateAjaxCount(-1 , 'loadTopLosers' );
		  }	  
		  
	}
	
	function loadPerformanceMetrics(){
		var loadPerformanceMetricsKey =0;
		   var tableTR="";
		   $("#performanceMetricsLoaderId").css("display", "block");
		   jQuery('#performanceMetricsTable').dataTable().fnClearTable();	 
		  // updateAjaxCount(1 , 'loadPerformanceMetrics' );
		   try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadPerformanceMetrics.lin",
		      cache: false,
		      data : {
		    	  publisherName : selectedPublisher,
		    	  startDate:startDate,
		    	  endDate:endDate,
		    	  advertiser:advertisername,
		    	  agency:agencyname,
		    	 properties : SelectedProperty
		    	  },
		      dataType: 'json',
		      success: function (data) {
		    	  
		         $.each(data, function(index, element) {
		             if(index =='performanceMetricsList' && element.length>0){
		            	 jQuery('#performanceMetricsTable').dataTable().fnClearTable();
		            	 jQuery('#performanceMetricsTable').dataTable().fnSettings()._iDisplayLength = 10;
		            	 jQuery('#performanceMetricsTable').dataTable().fnDraw();
		        	   $.each(element,function (newIndex,newElement){
		        		 
		 		        	//// for loop start/////
		 		        		   (function(newIndex) {
		 		        			     setTimeout( function(newIndex) {
		 		        
		 		        		           // code-here
		        		  
		        		   if(loadPerformanceMetricsKey == 0 && isTrendDefault){
		        			   loadPerformanceMetricsKey++;
		        				ordername = newElement.campaignIO;
		        			   lineItemArr = newElement.campaignLineItem;
		        			   //alert("ordername :"+ordername+"lineItemArr : "+lineItemArr );
		        			   getAdvertiserTrendAnalysisHeaderData();
		        			   actualLineGeneration();
		        			   //forcastLineGeneration();
		        			   getActualAdvertiserData();
		        			   getForcastAdvertiserData();
		        			   $(".order_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+ordername+"</strong></div>");
		        			   $(".lineOrder_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+lineItemArr+"</strong></div>");
		        			   $("#order_dropdown_name").text("");
		        			   $("#line_dropdown_name").text("");
		        			   $("#order_dropdown_name").text(ordername);
		        			   $("#line_dropdown_name").text(lineItemArr);
		        		   }
		        		   var id="performanceMetrics_"+newIndex;
		        		   
		        		   var bookedImpression = "NA";
		        		   if(newElement.bookedImpressions > 0) {
		        			   bookedImpression = formatInt(newElement.bookedImpressions);
		        		   }
		        		   
		        		   var newRowIndex = jQuery('#performanceMetricsTable').dataTable().fnAddData( [
		        		     newElement.campaignIO,
		        		     newElement.campaignLineItem,
		        		     //formatInt(newElement.bookedImpressions),
		        		     bookedImpression,
		        		     formatInt(newElement.impressionDelivered),
		        		     formatInt(newElement.clicks),
		        		     formatFloat(newElement.CTR,4),
		        		     "$"+formatFloat(newElement.budget,2),
		        		     "$"+formatFloat(newElement.spent,2)
		        		   ]);
		        		   
		        		   var tr = jQuery('#performanceMetricsTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
	        		     	tr.setAttribute('id', id);
	        		     	tr.setAttribute("style","cursor: hand; cursor: pointer;");
  		        		   	tr.setAttribute("onclick","showPerformanceMetricsPopup('"+newElement.campaignLineItem.replaceAll("'", "&#apos")+"', '"+id+"');");
  		        		   	
  		        		//for loop cont.../////
  		        		  
				   		       }, 10)
				   		       
				   		   })(newIndex)
				   		
				   	  //end code
  		        		   	
		        	 });
		           }else if(index =='performanceMetricsList' && element.length==0){
		        	   tableTR='<tr class="odd gradeX">'
					        +'<td colspan="8" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
		        	   $("#performanceMetricsTable tbody").append(tableTR);
		           }	           
		             
		         });
		         $("#performanceMetricsLoaderId").css("display", "none");
		        // updateAjaxCount(-1 , 'loadPerformanceMetrics' );
		     },
		     error: function(jqXHR, exception) {
		    	 //alert('loadPerformanceMetrics Exception:'+exception);
		    	// updateAjaxCount(-1 , 'loadPerformanceMetrics' );
		     }
		   });   
		  }catch(error){
			  //alert('loadPerformanceMetrics error:'+error);
			 // updateAjaxCount(-1 , 'loadPerformanceMetrics' );
		  }	  
		   
		  
	}


	google.load('visualization', '1', {	'packages' : [ 'geochart' ]	});

	function generateGeoGraph(divName,actionName,graphTitle,zoom_Divid){
		var actionUrl  = "/"+actionName+".lin";
		$('#'+divName).html('');
		var chartOptions = {
				region : "US",
				displayMode : "markers",
				resolution:'provinces',
				colorAxis : {colors: ['red', 'yellow', 'green'],displayMode: 'auto'}
		};
		var mapObj;
		var chartDataStr;
		var isEmpty;
		// updateAjaxCount(1 , 'generateGeoGraph' );
		$.ajax({
		       type : "POST",
		       url : actionUrl,
		       cache: false,
		       data : {
		    	   	  publisherName : selectedPublisher,
			    	  startDate:startDate,
			    	  endDate:endDate,
			    	  advertiser:advertisername,
			    	  agency:agencyname,
			    	  properties : SelectedProperty
			    	  
			    	  },
		       dataType: 'json',
		       success: function (data) {	  
		           $.each(data, function(index, element) {	  
		        	  mapObj=data['advertiserByLocationDataMap'];		      		    
		           });
		           
		           isEmpty = mapObj['isEmpty'];
		           if(isEmpty == 'No') {
			           chartDataStr =mapObj['JsonData'];
			           google.setOnLoadCallback(drawGeoChart(divName,chartDataStr,chartOptions));
			           $('#'+zoom_Divid).attr("onclick","zoomInGeoChart('"+graphTitle+"','left',"+chartDataStr+",'"+zoom_Divid+"',"+modalheaderWidth+","+modalheaderHeight+");");
		           }
		           else {
		        	   $('#'+divName).html('<div style="text-align:center;" class="widget alert alert-info adjusted">'
				        +'<i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong>'
				        +'</div>');
		           }
		         //  updateAjaxCount(-1 , 'generateGeoGraph' );
		       },
		       error: function(jqXHR, exception) {	
		    	  // updateAjaxCount(-1 , 'generateGeoGraph' );
		        }
		  });
		
	}
	
	 function viewPerformance(lineItemName, orderName){
		 $('#myTab1 li:eq(1)').css({'display':'inline'});
		 $('#myTab1 li:eq(1) a').tab('show');
		
		 //alert($('#myTab1 li:eq(1) a').text());
		 
		 if ($('#myTab1 li:eq(1) a').text() == "Trends and Analysis"){
			 $("#agency_dropdown").css({'display':'none'});
			 $("#advertiser_dropdown").css({'display':'none'});
			 //$("#order_dropdown").show();
			 //$("#lineItems_dropdown_single").show();
			 $(".agency_second_filter").show();
			 $(".advertiser_second_filter").show();
			 $("#order_dropdown_text").css({'display':'inline'});
			 $("#line_dropdown_text").css({'display':'inline'});
			 
		 }
		 
		 lineItem = lineItemName;
		 lineItemArr = lineItemName;
		 order = orderName;
		 ordername = orderName;
		 
		 $(".order_option").remove();
		 $("#order_trends").after("<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>"+orderName+"</strong> </div></div>");
		 
		$(".lineOrder_option").remove();
     	$("#lineItem_trends").after("<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>"+lineItemName+"</strong> </div></div>");
     	$("#order_dropdown_name").text("");
     	$("#line_dropdown_name").text("");
     	$("#order_dropdown_name").text(orderName);
     	$("#line_dropdown_name").text(lineItemName);
     	
		 	getAdvertiserTrendAnalysisHeaderData();
			getActualAdvertiserData();
			actualLineGeneration();
			getForcastAdvertiserData();
			//forcastLineGeneration();
	 }
	 
	 function reAllocation(){
		 $('#myTab1 li:eq(2)').css({'display':'inline'});
		 $('#myTab1 li:eq(2) a').tab('show');
	 }

	function showPerformerNonPerformerPopup(id, showChange){
		var contentDiv="";
		var title=$('#'+id+'_title').html();
		var chgPercent = $('#'+id+'_ctr').html();
		var subTitle="";
		 updateAjaxCount(1 , 'showPerformerNonPerformerPopup' );
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadPopUpData.lin",
		      cache: false,
		      data : {
		    	     publisherName : selectedPublisher,
			    	 startDate:startDate,
			    	 endDate:endDate,
			    	 lineItemName:title
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='popUpDTOObj' && element !=null){
		            	 
		            	 var bookedImpression = "NA";
		        		 if(element.bookedImpressions > 0) {
		        			 bookedImpression = formatInt(getNumericValue(element.bookedImpression));
		        		 }
		            	 
		            	 contentDiv=contentDiv
			              +'<div id="popover_content_wrapper" style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
	                      +'<div class="popheading_outer" >';
	                     
	                      +'<div id="popheading" class="popup_heading" style="background-color:#FDEFBC;height:25px;">';
		            	 if(element.costType != null && element.costType.trim() != "CPD") {
	                      	  contentDiv=contentDiv
							  +'<div class="pop_heading_left_name" style="font-weight:bold;color:black;margin-left:1%;width:29%;float:left;"><b>Booked Impressions:</b></div>'
							  +'<div class="pop_heading_left_value" style="width:29%;float:left;">'+bookedImpression+'</div>'
							  +'<div class="pop_heading_right_name" style="font-weight:bold;color:black;width:29%;float:left;text-align:right;"><b>CPM:</b></div>'
							  +'<div class="pop_heading_right_value" style="margin-right:5px;float:right;">$'+formatFloat(element.eCPM,2)+'</div>';
		             	  }
						  contentDiv=contentDiv
						  +'</div>'
						  +'<div class="sub_heading" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;"><span class="sub_heading_left" style="margin-left:55%;"><b>Life Time</b></span>'
						  +'<span class="sub_heading_right" style="float:right;"><b>'+timePeriod+'</b></span></div>'
						  +'<div class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Impressions Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatInt(getNumericValue(element.impressionDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatInt(getNumericValue(element.impressionDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Clicks:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatInt(getNumericValue(element.clicksLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatInt(getNumericValue(element.clicksInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">CTR(%):</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatFloat(element.ctrLifeTime,4)+'%</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatFloat(element.ctrInSelectedTime,4)+'%</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Remaining:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueRemainingLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueRemainingInSelectedTime))+'</div>'
						  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
			           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
			           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
			           	  +' onclick=javascript:viewPerformance("'+title.replace(/ /g, "&#32;")+'","'+element.order.replace(/ /g, "&#32;")+'")>View Trends</a>'
			           	  //+'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
			           	  //+' onclick=javascript:reAllocation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
			           	  +'</div></div>';
		            	 if(showChange) {
		            		if(chgPercent.indexOf('-')< 0){
		            			subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Up.png'width='15'></span>";
		            			subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:#52ad00;'>"+chgPercent+"</span></span>";
	            			}else{
	            				subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span>";
	            				subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'>"+chgPercent+"</span></span>";
	            			}
		            	 }
		            	 else {
		            		 subTitle="<span style='margin-left:2px;'></span>";
	            			 subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'></span></span>";
		            	 }
		            	 var chartData=element.chartData;
		            	 makePopUP(id,title,subTitle,contentDiv,chartData);
		            	 
		           }           
		             
		         }); 
		         updateAjaxCount(-1 , 'showPerformerNonPerformerPopup' );
		     },
		     error: function(jqXHR, exception) {
		    	 updateAjaxCount(-1 , 'showPerformerNonPerformerPopup' );
		     }
		   });   
		  }catch(error){
			  updateAjaxCount(-1 , 'showPerformerNonPerformerPopup' );
		  }	  
		
	}		
	
	
	var noOfLines= 0;
	function getAdvertiserReallocationData(){	
		  //alert(orderIdReallocation);
		// updateAjaxCount(1 , 'getAdvertiserReallocationData' );
		$("#advertiser_reallocation_header tbody tr").remove();
		$("#reallocationItemTable tbody tr").remove();
		var loader = '<tr class="odd gradeX">'
			   +'<td colspan="13" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#reallocationItemTable tbody").append(loader);
		var row = "";
		
		   $.ajax({
		 		       type : "POST",
		 		      url : "/getAdvertiserReallocation.lin",
		 		       cache: false,
		 		       data : {
		 		    	   	publisherName : selectedPublisher,
		 					startDate : startDate,
		 					endDate : endDate,
		 					orderIdReallocation : orderIdReallocation,
		 					lineItem : lineItemArr,
		 					properties : SelectedProperty
		 				},	
		 		        dataType: 'json',
		 		        success: function (data) {
		 		         //updateAjaxCount(-1 , 'getAdvertiserReallocationData' );
		 		           $.each(data, function(index, element) {	
		 		        	   
		 		        	  if (index == 'advertiserReallocationHeaderObj'&& element != null) {
									jsonResponse=element;	
									 headerDiv= getAdvetiserReallocationHeader(
					 		        		 jsonResponse.totalBudget,
					 		        		 jsonResponse.startDate,
					 		        		 jsonResponse.endDate,
					 		        		 jsonResponse.impressions,
					 		        		 jsonResponse.clicks,
					 		        		 jsonResponse.CTR,
					 		        		 jsonResponse.date);
								  $('#advertiser_reallocation_header').html(headerDiv);
									////alert(element);
							  }	
		 		        	
		 		        	   if(index == 'advertiserReallocationDataList' && element !=null && element.length>0){
		 		        		   var count = 1;
		 		        		  var dataList=data['advertiserReallocationDataList'];
				 		        	   for(key in  dataList){	 		        		   
				 		        		  var dtoObject = dataList[key];
				 		        		
				 		        		 // row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.lineItem+"</td><td id = 'ecpm_"+count+"'>"+dtoObject.ECPM+"</td><td style='text-align:right;' id = 'budget_"+count+"'>"+formatFloat(dtoObject.budget,true)+"</td><td style='text-align:right;' id = 'bookedImp_"+count+"'>"+formatInt(dtoObject.bookedImpressions)+"</td><td style='text-align:right;' id = 'delivery_"+count+"'>"+formatInt(dtoObject.deliveredImpressions)+"</td><td style='text-align:right;' id ='clicks_"+count+"'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;' id ='overUnder_"+count+"'>$"+formatFloat(dtoObject.overUnder,true)+"</td><td style='text-align:right;' id ='CTR_"+count+"'>"+formatFloat(dtoObject.CTR,true)+"%</td><td style='text-align:right;' id ='revenueDelivered_"+count+"'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td id='revRecognized_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td><td><input type='text' name= 'reallocation_text' onkeyup='calculation(this)' id = '"+dtoObject.lineItemId+"_"+count+"' value = '"+formatFloat(dtoObject.revenueRemaining,true)+"' class='col budget_"+count+"'  style='width: 50px;'/></td><td id = 'rbi_"+count+"' style='text-align:right;'>"+formatInt(dtoObject.bookedImpressions)+"</td><td id = 'rrd_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueToBeDelivered,true)+"</td></tr>";
				 		        		  row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.lineItem+"</td><td style='text-align:right;' id = 'delivery_"+count+"'>"+formatInt(dtoObject.deliveredImpressions)+"</td><td style='text-align:right;' id ='clicks_"+count+"'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;' id ='CTR_"+count+"'>"+formatFloat(dtoObject.CTR,true)+"%</td><td id = 'ecpm_"+count+"'>$"+dtoObject.ECPM+"</td><td style='text-align:right;' id = 'bookedImp_"+count+"'>"+formatInt(dtoObject.bookedImpressions)+"</td><td style='text-align:right;' id = 'budget_"+count+"'>"+formatFloat(dtoObject.budget,true)+"</td><td style='text-align:right;' id ='revenueDelivered_"+count+"'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td id='revRecognized_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td><td><input type='text' name= 'reallocation_text' onkeyup='calculation(this)' id = '"+dtoObject.lineItemId+"_"+count+"' value = '"+formatFloat(dtoObject.revenueRemaining,true)+"' class='col budget_"+count+"'  style='width: 50px;'/></td><td id = 'rb_"+count+"' style='text-align:right;'>"+formatFloat(dtoObject.budget,true)+"</td><td id = 'rbi_"+count+"' style='text-align:right;'>"+formatInt(dtoObject.bookedImpressions)+"</td></tr>";
				 		        		  count++;
				 		        	     noOfLines++;
				 		        	   }
				 		        	   //row = row +"<tr class='even gradeC'><td style='text-align:right;'>Total</td><td id = 'total_ecpm'>"+dtoObject.totalECPM+"</td><td style='text-align:right;' id = 'total_budget'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td style='text-align:right;' id = 'total_bookedImp'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td style='text-align:right;' id = 'total_delivery'>"+formatFloat(dtoObject.totalDeliveredImpressions)+"</td><td style='text-align:right;' id = 'total_clicks'>"+formatFloat(dtoObject.totalClicks)+"</td><td style='text-align:right;' id = 'total_overUnder'>"+formatFloat(dtoObject.totalOverUnder)+"</td><td style='text-align:right;' id = 'total_CTR'>"+formatFloat(dtoObject.totalCTR)+"</td><td style='text-align:right;' id = 'total_revDeliverd'>"+formatFloat(dtoObject.totalRevenueDelivered)+"</td><td id='total_revRecognized' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_col' >"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_rbi' style='text-align:right;'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td id = 'total_rrd' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueToBeDelivered)+"</td></tr>"
				 		        	   row = row +"<tr class='even gradeC'><td style='text-align:right;'>Total</td><td style='text-align:right;' id = 'total_delivery'>"+formatFloat(dtoObject.totalDeliveredImpressions)+"</td><td style='text-align:right;' id = 'total_clicks'>"+formatFloat(dtoObject.totalClicks)+"</td><td style='text-align:right;' id = 'total_CTR'>"+formatFloat(dtoObject.totalCTR)+"%</td><td id = 'total_ecpm'></td><td style='text-align:right;' id = 'total_bookedImp'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td style='text-align:right;' id = 'total_budget'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td style='text-align:right;' id = 'total_revDeliverd'>"+formatFloat(dtoObject.totalRevenueDelivered)+"</td><td id='total_revRecognized' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_col' >"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_rb' style='text-align:right;'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td id = 'total_rbi' style='text-align:right;'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td></tr>";
		 		        	   }else if(index == 'advertiserReallocationDataList' && element !=null && element.length==0) {		
			 		        		  row='<tr class="odd gradeX">'
			 						        +'<td colspan="13" style="color:red; text-align:center;">'
			 							        +'<div class="widget alert alert-info adjusted">'
			 							        +'<i class="cus-exclamation"></i>'
			 							        +'<strong>No records found for the selected filters</strong>'
			 							        +'</div>'
			 						        +'</td>'						      
			 						        +'</tr>';
			 						} else if(index == 'errorStatus' && element == 'DFPAPIError'){
			 							
			 							row='<tr class="odd gradeX">'
			 						        +'<td colspan="13" style="color:red; text-align:center;">'
			 							        +'<div class="widget alert alert-info adjusted">'
			 							        +'<i class="cus-exclamation"></i>'
			 							        +'<strong>No records found for the selected filters</strong>'
			 							        +'</div>'
			 						        +'</td>'						      
			 						        +'</tr>';
					 		        	$("#reallocationItemTable tbody tr").remove();
						 		         $("#reallocationItemTable tbody").append(row);
						 		        toastr.error('Some Error In Accessing DFP ');
			 						}else if(index == 'errorStatus' && element == 'validationError'){
			 							
			 							row='<tr class="odd gradeX">'
			 						        +'<td colspan="13" style="color:red; text-align:center;">'
			 							        +'<div class="widget alert alert-info adjusted">'
			 							        +'<i class="cus-exclamation"></i>'
			 							        +'<strong>No records found for the selected filters</strong>'
			 							        +'</div>'
			 						        +'</td>'						      
			 						        +'</tr>';
					 		        	$("#reallocationItemTable tbody tr").remove();
						 		         $("#reallocationItemTable tbody").append(row);
						 		        toastr.error('DFP Validation Error ');
			 						}
		 		        	   
			 		       });
		 		          $("#reallocationItemTable tbody tr").remove();
		 		         $("#reallocationItemTable tbody").append(row);
		 		         
		 		       },
		 		        error: function(jqXHR, exception) {
		 		       //  updateAjaxCount(-1 , 'getAdvertiserReallocationData' );
		 		        	
			 		    }
			 		  });

	}
	
	
	/*function setAdvertiserReallocationData(){
		if($("#budget_allocated").html()== 0 && budgetCheck){
			//errorMessage('DFP Validation Error ');
		
			//updateAjaxCount(1 , 'setAdvertiserReallocationData' );
			var loader = '<tr class="odd gradeX">'
				   +'<td colspan="13" style="color:red; text-align:center;">'
				   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
			   $("#reallocationItemTable tbody").append(loader);
			var array ='';
			var row = "";
			$('input[name="reallocation_text"]').each(function() {
				var idArr =[];
				var valueArr = [];
				var id = $('#'+this.id).attr("id");
				idArr = id.split("_");
				var value = $('#'+this.id).val().replaceAll(",","");
				valueArr = value.split(".");
				var rbiValue = $('#rbi_'+idArr[1]).html().replaceAll(",","");
				//alert(rbiValue);
				array = array+idArr[0]+":"+valueArr[0]+";"+rbiValue+",";
			});
			//alert("array : "+array);
			
			   $.ajax({
	 		       type : "POST",
	 		      url : "/saveAdvertiserReallocation.lin",
	 		       cache: false,
	 		       data : {
	 		    	   	publisherName : selectedPublisher,
	 					startDate : startDate,
	 					endDate : endDate,
	 					orderIdReallocation : orderIdReallocation,
	 					lineItem : lineItemArr,
	 					properties : SelectedProperty,
	 					array : array
	 				},	
	 		        dataType: 'json',
	 		       success: function (data) {
	 		    	      // updateAjaxCount(-1 , 'setAdvertiserReallocationData' );
		 		           $.each(data, function(index, element) {	
		 		        	  if(index == 'advertiserReallocationDataList' && element.length>0){
		 		        		   var count = 1;
		 		        		  var dataList=data['advertiserReallocationDataList'];
				 		        	   for(key in  dataList){	 		        		   
				 		        		  var dtoObject = dataList[key];
				 		        		
				 		        		  //row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.lineItem+"</td><td id = 'ecpm_"+count+"'>"+dtoObject.ECPM+"</td><td style='text-align:right;' id = 'budget_"+count+"'>"+formatFloat(dtoObject.budget,true)+"</td><td style='text-align:right;' id = 'bookedImp_"+count+"'>"+formatInt(dtoObject.bookedImpressions)+"</td><td style='text-align:right;' id = 'delivery_"+count+"'>"+formatInt(dtoObject.deliveredImpressions)+"</td><td style='text-align:right;' id ='clicks_"+count+"'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;' id ='overUnder_"+count+"'>$"+formatFloat(dtoObject.overUnder,true)+"</td><td style='text-align:right;' id ='CTR_"+count+"'>"+formatFloat(dtoObject.CTR,true)+"%</td><td style='text-align:right;' id ='revenueDelivered_"+count+"'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td id='revRecognized_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td><td><input type='text' name= 'reallocation_text' onkeyup='calculation(this)' id = '"+dtoObject.lineItemId+"_"+count+"' value = '"+formatFloat(dtoObject.revenueRemaining,true)+"' class='col budget_"+count+"'  style='width: 50px;'/></td><td id = 'rbi_"+count+"' style='text-align:right;'>"+formatInt(dtoObject.bookedImpressions)+"</td><td id = 'rrd_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueToBeDelivered,true)+"</td></tr>";
				 		        		 row = row + "<tr class='even gradeC'><td style='text-align:right;'>"+dtoObject.lineItem+"</td><td style='text-align:right;' id = 'delivery_"+count+"'>"+formatInt(dtoObject.deliveredImpressions)+"</td><td style='text-align:right;' id ='clicks_"+count+"'>"+formatInt(dtoObject.clicks)+"</td><td style='text-align:right;' id ='CTR_"+count+"'>"+formatFloat(dtoObject.CTR,true)+"%</td><td id = 'ecpm_"+count+"'>$"+dtoObject.ECPM+"</td><td style='text-align:right;' id = 'bookedImp_"+count+"'>"+formatInt(dtoObject.bookedImpressions)+"</td><td style='text-align:right;' id = 'budget_"+count+"'>"+formatFloat(dtoObject.budget,true)+"</td><td style='text-align:right;' id ='revenueDelivered_"+count+"'>$"+formatFloat(dtoObject.revenueDelivered,true)+"</td><td id='revRecognized_"+count+"' style='text-align:right;'>$"+formatFloat(dtoObject.revenueRemaining,true)+"</td><td><input type='text' name= 'reallocation_text' onkeyup='calculation(this)' id = '"+dtoObject.lineItemId+"_"+count+"' value = '"+formatFloat(dtoObject.revenueRemaining,true)+"' class='col budget_"+count+"'  style='width: 50px;'/></td><td id = 'rb_"+count+"' style='text-align:right;'>"+formatFloat(dtoObject.budget,true)+"</td><td id = 'rbi_"+count+"' style='text-align:right;'>"+formatInt(dtoObject.bookedImpressions)+"</td></tr>";
				 		        		  count++;
				 		        	     noOfLines++;
				 		        	   }
				 		        	  //row = row +"<tr class='even gradeC'><td style='text-align:right;'>Total</td><td id = 'total_ecpm'>"+formatFloat(dtoObject.totalECPM)+"</td><td style='text-align:right;' id = 'total_budget'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td style='text-align:right;' id = 'total_bookedImp'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td style='text-align:right;' id = 'total_delivery'>"+formatFloat(dtoObject.totalDeliveredImpressions)+"</td><td style='text-align:right;' id = 'total_clicks'>"+formatFloat(dtoObject.totalClicks)+"</td><td style='text-align:right;' id = 'total_overUnder'>"+formatFloat(dtoObject.totalOverUnder)+"</td><td style='text-align:right;' id = 'total_CTR'>"+formatFloat(dtoObject.totalCTR)+"</td><td style='text-align:right;' id = 'total_revDeliverd'>"+formatFloat(dtoObject.totalRevenueDelivered)+"</td><td id='total_revRecognized' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_col' >"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_rbi' style='text-align:right;'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td id = 'total_rrd' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueToBeDelivered)+"</td></tr>"
				 		        	  row = row +"<tr class='even gradeC'><td style='text-align:right;'>Total</td><td style='text-align:right;' id = 'total_delivery'>"+formatFloat(dtoObject.totalDeliveredImpressions)+"</td><td style='text-align:right;' id = 'total_clicks'>"+formatFloat(dtoObject.totalClicks)+"</td><td style='text-align:right;' id = 'total_CTR'>"+formatFloat(dtoObject.totalCTR)+"%</td><td id = 'total_ecpm'></td><td style='text-align:right;' id = 'total_bookedImp'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td><td style='text-align:right;' id = 'total_budget'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td style='text-align:right;' id = 'total_revDeliverd'>"+formatFloat(dtoObject.totalRevenueDelivered)+"</td><td id='total_revRecognized' style='text-align:right;'>"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_col' >"+formatFloat(dtoObject.totalRevenueRemaining)+"</td><td id = 'total_rb' style='text-align:right;'>"+formatFloat(dtoObject.totalBudget,2)+"</td><td id = 'total_rbi' style='text-align:right;'>"+formatFloat(dtoObject.totalBookedImpressions)+"</td></tr>"; 
		 		        	  }else if(index == 'advertiserReallocationDataList' && element.length==0) {		
			 		        		  row='<tr class="odd gradeX">'
			 						        +'<td colspan="13" style="color:red; text-align:center;">'
			 							        +'<div class="widget alert alert-info adjusted">'
			 							        +'<i class="cus-exclamation"></i>'
			 							        +'<strong>No records found for the selected filters</strong>'
			 							        +'</div>'
			 						        +'</td>'						      
			 						        +'</tr>';
			 						}else if(index == 'errorStatus' && element == 'DFPAPIError'){
			 							
			 							toastr.error('Some Error In Accessing DFP ');
			 						}else if(index == 'errorStatus' && element == 'validationError'){
			 							toastr.error('DFP Validation Error');
			 							
			 						}
			 		       });
		 		          $("#reallocationItemTable tbody tr").remove();
		 		         $("#reallocationItemTable tbody").append(row);
		 		        toastr.success('Data Saved Successfully');
		 		         
		 		       },
		 		        error: function(jqXHR, exception) {
		 		        	// updateAjaxCount(-1 , 'setAdvertiserReallocationData' );
			 		    }
		 		     
		 		  });
		}else if($("#budget_allocated").html()!= 0){
			//alert("Balance Should Be Zero(0)")
			toastr.error('Total Adjusted Budget Should Be Equal To Total Budget ');
		}else if(!budgetCheck){
			toastr.error('Budget should be less than Total Remaining');
		}
		

	}*/
	
	function getAdvetiserReallocationHeader(totalBudget,startDate,endDate,impressions,clicks,CTR,date){
		var headerDivData="";
		var daysRemaining = getDaysRemaining(date, endDate);
		headerDivData=headerDivData
		//alert(daysRemaining);
		//+'<div id = "advertiser_trends_analysis_header" class="mystats indented revenue_bg" >'
		//+'<div class="revenue_sites">'
		+'<div class="summary_bar clear_summary_bar">'
		+'<div style="">TOTAL BUDGET</div>'
		+'<div class="summary_value">$'+formatFloat(totalBudget,2)+'</div>'
		+'</div>'
		+'<div class="summary_bar clear_summary_bar" >'
		+'<div style="">START DATE</div>'
		+'<div class="summary_value">'+startDate+'</div>'	
		+'</div>'
		+'<div class="summary_bar clear_summary_bar">'
		+'<div style="">END DATE</div>'
		+'<div class="summary_value">'+endDate+'</div>'
		+'</div>'
		+'<div class="summary_bar clear_summary_bar">'
		+'<div style="">DAYS REMAINING</div>'
		+'<div class="summary_value">'+daysRemaining+'</div>'
		+'</div>'
		//+'</div>'
		//+'<div class="click">'
		+'<div class="summary_bar clear_summary_bar">'
		+'<div style="">DELIVERED IMPRESSIONS</div>'
		+'<div class="summary_value">'+formatInt(impressions)+'</div>'
		+'</div>'
		+'<div class="summary_bar clear_summary_bar" >'
		+'<div style="">CLICKS</div>'
		+'<div class="summary_value">'+formatInt(clicks)+'</div>'
		+'</div>'
		+'<div class="summary_bar clear_summary_bar">'
		+'<div style="">CTR(%)</div>'
		+'<div class="summary_value">'+formatFloat(CTR,2)+'%</div>'
		+'</div>'
		//+'</div>'
		//+'</div>'
		return headerDivData;
		
	}
	
	function showPerformanceMetricsPopup(title, id){
		var contentDiv="";
		title = title.replaceAll("&#apos","'");
		var subTitle="";
		 updateAjaxCount(1 , 'showPerformanceMetricsPopup' );
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadPopUpData.lin",
		      cache: false,
		      data : {
		    	  	 publisherName : selectedPublisher,
			    	 startDate:startDate,
			    	 endDate:endDate,
			    	 lineItemName:title
			    	 },		    
		      dataType: 'json',
		      success: function (data) {
		         $.each(data, function(index, element) {
		             if(index =='popUpDTOObj' && element !=null){
		            	 
		            	 var bookedImpression = "NA";
		        		 if(element.bookedImpressions > 0) {
		        			 bookedImpression = formatInt(getNumericValue(element.bookedImpression));
		        		 }
		        		 
		            	 contentDiv=contentDiv
			              +'<div id="popover_content_wrapper">'
	                      +'<div class="popheading_outer" style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">';
	                      if(element.costType != null && element.costType.trim() != "CPD") {
	                      	  contentDiv=contentDiv
						      +'<div id="popheading" class="popup_heading" style="background-color:#FDEFBC;height:25px;">'
							  +'<div class="pop_heading_left_name" style="font-weight:bold;color:black;margin-left:1%;width:29%;float:left;"><b>Booked Impressions:</b></div>'
							  +'<div class="pop_heading_left_value" style="width:29%;float:left;">'+bookedImpression+'</div>'
							  +'<div class="pop_heading_right_name" style="font-weight:bold;color:black;width:29%;float:left;text-align:right;"><b>CPM:</b></div>'
							  +'<div class="pop_heading_right_value" style="margin-right:5px;float:right;">$'+formatFloat(element.eCPM,2)+'</div></div>';
		             	  }
						  contentDiv=contentDiv
						  +'<div class="sub_heading" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;"><span class="sub_heading_left" style="margin-left:55%;"><b>Life Time</b></span>'
						  +'<span class="sub_heading_right" style="float:right;"><b>'+timePeriod+'</b></span></div>'
						  +'<div class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Impressions Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatInt(getNumericValue(element.impressionDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatInt(getNumericValue(element.impressionDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Clicks:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatInt(getNumericValue(element.clicksLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatInt(getNumericValue(element.clicksInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">CTR(%):</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">'+formatFloat(element.ctrLifeTime,4)+'%</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">'+formatFloat(element.ctrInSelectedTime,4)+'%</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Revenue Remaining:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueRemainingLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueRemainingInSelectedTime))+'</div>'
						  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
			           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
			           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
			           	  +' onclick=javascript:viewPerformance("'+title.replace(/ /g, "&#32;")+'","'+element.order.replace(/ /g, "&#32;")+'")>View Trends</a>'
			           	 // +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
			           	  //+' onclick=javascript:reAllocation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
			           	  +'</div></div>';
		            	 var chartData=element.chartData;
		            	 makePopUP(id,title,subTitle,contentDiv,chartData);
		            	 
		           }           
		             
		         }); 
		         updateAjaxCount(-1 , 'showPerformanceMetricsPopup' );
		     },
		     error: function(jqXHR, exception) {
		    	 updateAjaxCount(-1 , 'showPerformanceMetricsPopup' );
		     }
		   });   
		  }catch(error){
			  updateAjaxCount(-1 , 'showPerformanceMetricsPopup' );
		  }	  
		  
		
	}
	
	/*$(document).ready(function() {
		$("#performanceMetricsTable tbody").delegate("tr", "click", function() {
			var lineItemName = $("td:eq(2)", this).text();
			$(this).attr("id","a1");
			$(this).attr("rel","popover");
			showPerformanceMetricsPopup(lineItemName, "a1");
			});
	});*/

	var budgetCheck = true;
	function calculation(obj){
		//alert(noOfLines);
		//rI = obj.parentNode.parentNode.rowIndex
		//cI = obj.parentNode.cellIndex
		var id = obj.getAttribute('id');
		//alert(id)
		var counterSplit = id.split("_");
		var actualCounter = counterSplit[1];
		//alert(actualCounter)
		
		var Value = document.getElementById(id).value;
		var formatedValue = Value.replaceAll(",","");
		//alert(formatedValue)
		//alert($("#ecpm_"+actualCounter).html())
		var ecpm= $('#ecpm_'+actualCounter).html().replace('$','');
		var formatedEcpm =ecpm.replaceAll(",","");
		//alert("Value"+Value.replaceAll(",",""));
		//alert("ecpm"+formatedEcpm)
		var rev_book_imp = parseFloat((formatedValue * 1000 )/formatedEcpm);
		//var inter_rev_book_imp = rev_book_imp.split(".");
		var formatedRev_book_imp=rev_book_imp.toFixed(0);
		//alert("rev_book_imp"+formatedRev_book_imp)
		$('#rbi_'+actualCounter).html(formatedRev_book_imp);
		var revenueRecog = $("#revRecognized_"+actualCounter).html().replaceAll('$','');
		var revenueRecogCheck = parseFloat($("#revRecognized_"+actualCounter).html().replaceAll('$','').replaceAll(",",""));
		var revBudgetCheck = parseFloat($(".budget_"+actualCounter).val().replaceAll(",","")); 
		
	
		var budget = parseFloat($("#budget_"+actualCounter).html().replaceAll('$','').replaceAll(",",""));
		//alert("budget :"+budget);
		var delivered = parseFloat($("#revenueDelivered_"+actualCounter).html().replaceAll('$','').replaceAll(",",""));
		//alert("delivered :"+delivered);
		//alert("revBudgetCheck : "+revBudgetCheck);
		var revicedBudget = revBudgetCheck + delivered ;
		//alert("revicedBudget :"+revicedBudget);
		var formatedRevicedBudget = formatFloat(revicedBudget,true);
		//alert("formatedRevicedBudget :"+formatedRevicedBudget);
		$('#rb_'+actualCounter).html(formatedRevicedBudget);
		var formatedrevenueRecog = revenueRecog.replaceAll(",","");
		//alert("revenueRecog"+formatedrevenueRecog)
		
		
		var revenueToDelivered = formatedValue - formatedrevenueRecog
		var formatedrevenueToDelivered = revenueToDelivered;
		var a = "$"+formatedrevenueToDelivered
		//alert("revenueToDelivered"+formatedrevenueToDelivered)
		$('#rrd_'+actualCounter).html(a);
		
		
		
		var totalecpm=0;
		var totalbudget=0;
		var totalbookedImp=0;
		var totaldelivery=0;
		var totalclicks=0;
		var totaloverUnder=0;
		var totalCTR=0;
		var totalrevenueDelivered=0;
		var totalrevRecognized=0;
		var totalrbi=0;
		var totalrrd=0;
		var totalrevBudget=0;
		var totalRevicedBudget = 0;
		for (i=1;i<=noOfLines;i++){
			//alert($("#ecpm_"+i).html())
			//alert($(".budget_"+i).val())
			totalrevBudget=totalrevBudget+parseFloat($(".budget_"+i).val().replaceAll(",",""));
			//revised_budget_a = $("#ecpm_"+i).html()counterSplit[0]
			//alert($(".budget_"+i).val())
			//alert(totalrevBudget)
			totalecpm=totalecpm+parseFloat($("#ecpm_"+i).html());
			//alert(parseFloat($("#ecpm_"+i).html()))
			//alert($("#ecpm_"+i).html())
			totalbudget=totalbudget+parseFloat($("#budget_"+i).html().replaceAll(",",""));
			totalbookedImp=totalbookedImp+parseFloat($("#bookedImp_"+i).html().replaceAll(",",""));
			totaldelivery=totaldelivery+parseFloat($("#delivery_"+i).html().replaceAll(",",""));
			totalclicks=totalclicks+parseFloat($("#clicks_"+i).html().replaceAll(",",""));
			totaloverUnder=totaloverUnder+parseFloat($("#clicks_"+i).html().replaceAll(",",""));
			
			totalRevicedBudget = totalRevicedBudget+parseFloat($("#rb_"+i).html().replaceAll(",",""));
			//alert(totalRevicedBudget);
			
			var interrevenue = parseFloat($("#revenueDelivered_"+i).html().replaceAll("$","").replaceAll(",",""));
			//alert(interrevenue)
			//var formatinterrevenue =interrevenue.replaceAll(",","");
			totalrevenueDelivered=totalrevenueDelivered+interrevenue;
			
			var interrevenueRec = parseFloat($("#revRecognized_"+i).html().replaceAll("$","").replaceAll(",",""));
			//alert(interrevenueRec)
			//var formatinterrevenueRec =interrevenueRec.replaceAll(",","");
			totalrevRecognized=totalrevRecognized+interrevenueRec;
			
			totalrbi=totalrbi + parseFloat($("#rbi_"+i).html());
			//alert($("#rrd_"+i).html())
			//alert("parseFloat " +parseFloat($("#rrd_"+i).html().replaceAll("$","")))
			//totalrrd = totalrrd + parseFloat($("#rrd_"+i).html().replaceAll("$","").replaceAll(",",""));
			//alert(totalrrd)
			//alert(totalecpm)
			
			
			
			$('#total_ecpm').html(totalecpm);
			$('#total_budget').html(totalbudget);
			$('#total_bookedImp').html(totalbookedImp);
			$('#total_delivery').html(totaldelivery);
			$('#total_clicks').html(totalclicks);
			$('#total_overUnder').html(totaloverUnder);
			
			$('#total_revDeliverd').html(totalrevenueDelivered);
			$('#total_revRecognized').html(totalrevRecognized);
			$('#total_rbi').html(totalrbi);
			//$('#total_rrd').html(totalrrd);
			$('#total_col').html(totalrevBudget);
			$('#total_rb').html(totalRevicedBudget);
			//alert(total)
			
			var budget_left = totalbudget - totalrevBudget;
			$("#budget_allocated").html(budget_left);
		}
		
		if(revBudgetCheck>totalrevRecognized){
			//alert('revBudget'+revBudget);
			//alert('revenueRecog'+revenueRecog);
			budgetCheck = false;
			toastr.error('Budget should be less than Total Remaining');
		}else{
			budgetCheck = true;
		}
		
		totalCTR=((parseFloat($("#total_clicks").html().replaceAll("%","").replaceAll(",","")) / parseFloat($("#total_delivery").html().replaceAll(",","")))*100);
		$('#total_CTR').html(totalCTR);
		/*alert(id)
		
		alert(rI);
		alert(cI);
		alert(obj.innerHTML);
		var budget_val = document.getElementById('reallocationItemTable').rows[rI].cells[cI].innerHTML;
		alert(document.getElementById('reallocationItemTable').rows[rI].cells[cI].value);
		alert(budget_val);*/
}
	
	function loadFilterData() {
		 var allPublishersList;
		 try{
	 		 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadInvAndRevFilterData.lin",
	 		       cache: false,
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {
	 		        	  if (index == 'allPublishersList') {
	 		        		 allPublishersList=element;																			
						  }
	 		           });
	 		          getchannelTrendsDropDown(allPublishersList);
	 		         loadAllDataAdvertiser();
	 		        loadAdvertiserPropertyList();
	 		       },
	 		       error: function(jqXHR, error) {
	 		        }
	 		  });
			}catch(exception){
			}
	 }
	
	function loadFilterDataByPublisherName(publisherId) {
		var allPublishersList;
		var channelsNameList;
			 try{
		 		 $.ajax({
		 		       type : "POST",			 		   
		 		       url : "/loadInvAndRevFilterDataByPublisherName.lin",
		 		      data : {
		 		    	 publisherId:publisherId
		 		       },
		 		       cache: false,
		 		       dataType: 'json',
		 		       success: function (data) {	    	  
		 		           $.each(data, function(index, element) {
		 		        	  if (index == 'allPublishersList') {
		 		        		 allPublishersList=element;																			
							  }
		 		           });
		 		          getchannelTrendsDropDown(allPublishersList);
		 		         loadAdvertiserPropertyList();
		 		       },
		 		       error: function(jqXHR, error) {
		 		        }
		 		  });
				}catch(exception){
				}
		 }
		
	
	 function getchannelTrendsDropDown(allPublishersList) {
		 var ulContents = "";
		 $("#advertiserViewPublishersDropDown ul").empty();
		 if(allPublishersList != null && allPublishersList.length > 0){
			 $('#advertiserViewPubTitle').html(allPublishersList[0].value);
			 selectedPublisher = allPublishersList[0].value;
		 }
		 if(allPublishersList != null && allPublishersList.length > 1){
			 for(j=1;j<allPublishersList.length;j++) {
				// ulContents = ulContents + "<li><a href=\"javascript:changeAdvertiseViewPublisherDropdown('"+allPublishersList[j]+"');\" style=\"color:black;\">" + allPublishersList[j] + "</a></li>";
				 ulContents = ulContents + "<li><a href=\"javascript:loadFilterDataByPublisherName('"+allPublishersList[j].id.trim()+"');\" onclick=\"changeAdvertiseViewPublisherDropdown('"+allPublishersList[j].value+"');\" style=\"color:black;\">" + allPublishersList[j].value + "</a></li>";
			 }
		 }
		 $("#advertiserViewPublishersDropDown ul").append(ulContents);
	 }
	 
	 function loadAdvertiserPropertyList() {
		 var propertyList;
		// alert(selectedPublisher);
		 try{
	 		 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadAdvertiserPropertyList.lin",
	 		       cache: false,
	 		      data : {
			    	     publisherName : selectedPublisher
				    	 
				    	 },		
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {
	 		        	  if (index == 'siteDropDownList') {
	 		        		 propertyList=element;	
	 		        		
						  }
	 		           });
	 		          getPropertyDropDown(propertyList)
	 		         
	 		       },
	 		       error: function(jqXHR, error) {
	 		        }
	 		  });
			}catch(exception){
			}
	 }
	 
	 function getPropertyDropDown(propertyList) {
		 var ulContents = "";
		 $("#advertiserViewPropertyDropDown ul").empty();
		/* if(propertyList != null && propertyList.length > 0){
			 $('#advertiserViewPropertyTitle').html(propertyList[0]);
			 selectedPublisher = propertyList[0];
		 }*/
		 $('#advertiserViewPropertyTitle').html("All Properties");
		 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changeAdvertiseViewPropertyDropdown('All Properties');\" style=\"color:black;\">All Properties</a></li>";
		 if(propertyList != null && propertyList.length > 0){
			 for(j=0;j<propertyList.length;j++) {
				 
				 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changeAdvertiseViewPropertyDropdown('"+propertyList[j].trim()+"');\" style=\"color:black;\">" + propertyList[j] + "</a></li>";
			 }
		 }
		 $("#advertiserViewPropertyDropDown ul").append(ulContents);
	 }
	 function showRichMedia(){
		
		 $('#myModalRichMedia').modal('show');
		  setTimeout(drawChartcoupon,500);
		  setTimeout(drawChart,500);
		 
		 
	 }
	 
	// $('#myModalRichMedia').modal('show')
	 
	 function showTraffer(id,lineItemId){
		// alert("id = "+id);
		  // alert(lineItemId);
		   var forcastDTOStatus = false;
		    var forcastDTO;
		    var bookedImpressions;
		    var delivered;
		    var startDate;
		    var forcasted;
		    var CPM;
		    var lineItem;
			var contentDiv="";
			var subTitle="";
			var title = $('#'+id+'_title').html();
			 contentDiv=contentDiv
        	 +'<div id="popover_content_wrapper" style="width:550px;">'
				+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
				  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:25px;width:527px;text-transform: uppercase;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Campaign Name:</div><div style="width:109px;float:left;margin-left:10px;text-align:center;"><b>Goal Quantity</b></div> <div style="width:50px;float:left;margin-left:10px;text-align:center;"><b>CPM</b></div><div style="float:left; margin-left:10px;text-align:center;"><b>Start Date</b></div></div>'
				  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:527px;"> <div style="font-weight:bold;color:black;width:200px;float:left;" id ="line_name"></div><div style="width:109px;float:left;margin-left:10px;text-align:right;" id = "booked_imp"><b></b></div> <div style="width:62px;float:left;margin-left:10px;text-align:right;" id="ecpm"><b></b></div><div style="float:left;text-align:right;margin-left:10px;" id = "start_date"><b></b></div></div>'
				 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
				  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
				  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
				  +'<div id="trafficPopup_loader" style="width:550px;height:30px;text-align:center"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>'
				  +' <div id="chart_div_traffer" style="width: 500px; height: 220px;margin-left:10px;"></div>'
				 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
				 
				 +'</div>'
				 +'</div>';
			 makeTrafferPopUP(id,title,subTitle,contentDiv);
			
			try{            	 
			 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadLineItemForcast.lin",
	 		       cache: false,
	 		      data : {
	 		    	 lineItemId : lineItemId
				    	 },		
	 		       dataType: 'json',
	 		       success: function (data) {	
	 		    	 
	 		           $.each(data, function(index, element) {
	 		        	  if (index == 'forcastLineItemDTO') {
	 		        		 forcastDTO=element;
	 		        		forcastDTOStatus = forcastDTO.status;
	 		        		bookedImpressions = forcastDTO.bookedImpressions;
	 		        		CPM = '$'+forcastDTO.ECPM;
	 		        		startDate = forcastDTO.startDate;
	 		        		lineItem = forcastDTO.lineItem;
	 		        		delivered = forcastDTO.deliveredUnit;
	 		        		if(bookedImpressions!=null && delivered!=null){
	 		        			forcasted = (bookedImpressions - delivered);
	 		        		}
	 		        		bookedImpressions = formatInt(bookedImpressions);
	 		        		//alert("forcastDTOStatus :"+forcastDTOStatus);
	 		        		//alert("bookedImpressions :"+bookedImpressions);
	 		        		//if(forcastDTOStatus){
	 		        			/* contentDiv=contentDiv
				            	 +'<div id="popover_content_wrapper" style="width:550px;">'
									+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
									  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:25px;width:527px;text-transform: uppercase;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Campaign Name:</div><div style="width:109px;float:left;margin-left:10px;"><b>Goal Quantity</b></div> <div style="width:50px;float:left;margin-left:10px;"><b>CPM</b></div><div style="float:left; margin"><b>Start Date</b></div></div>'
									  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:527px;"> <div style="font-weight:bold;color:black;width:200px;float:left;" id ="line_name"></div><div style="width:109px;float:left;margin-left:10px;" id = "booked_imp"><b></b></div> <div style="width:62px;float:left;margin-left:10px;" id="ecpm"><b></b></div><div style="float:left;" id = "start_date"><b></b></div></div>'
									 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
									  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
									  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
									 +' <div id="chart_div_traffer" style="width: 500px; height: 220px;margin-left:10px;"></div>'
									 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
									 
									 +'</div>'
									 +'</div>';*/
	 		        		/*}else{
	 		        			 contentDiv=contentDiv
				            	 +'<div id="popover_content_wrapper" style="width:550px;">'
									+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
									  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:25px;width:460px;text-transform: uppercase;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Campaign Name:</div><div style="width:109px;float:left;margin-left:10px;"><b>Goal Quantity</b></div> <div style="width:50px;float:left;margin-left:10px;"><b>CPM</b></div><div style="float:left; margin"><b>Start Date</b></div></div>'
									  +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">'+forcastDTO.lineItem+'</div><div style="width:109px;float:left;margin-left:10px;"><b>'+forcastDTO.bookedImpressions+'</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>'+forcastDTO.ECPM+'</b></div><div style="float:left;"><b>'+forcastDTO.startDate+'</b></div></div>'
									 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
									  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
									  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
									 +' <div id="chart_div_traffer1" style="width: 500px; height: 220px;margin-left:10px;"></div>'
									 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
									 
									 +'</div>'
									 +'</div>';
	 		        		}*/
	 		        		$("#trafficPopup_loader").css({'display':'none'});	 
	 		        		deliveryIndicaterGraph(forcastDTOStatus,delivered,lineItem,forcasted);
	 		        			$('#line_name').html(lineItem);
	 		        			$('#booked_imp').html(bookedImpressions);
	 		        			$('#ecpm').html(CPM);
	 		        			$('#start_date').html(startDate);
	 		        			 
						  }
	 		           });
	 		       },
	 		       error: function(jqXHR, error) {
	 		        }
	 		  });
			
			}catch(exception){
				
			}          	
			
		/*	if(id == "topNonPerformer_3" || id == "topNonPerformer_4"){
			            		 contentDiv=contentDiv
				            	 +'<div id="popover_content_wrapper" style="width:550px;">'
									+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
									  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:25px;width:460px;text-transform: uppercase;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Campaign Name:</div><div style="width:109px;float:left;margin-left:10px;"><b>Goal Quantity</b></div> <div style="width:50px;float:left;margin-left:10px;"><b>CPM</b></div><div style="float:left; margin"><b>Start Date</b></div></div>'
									  +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>50,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$5.00</b></div><div style="float:left;"><b>05/31/2013</b></div></div>'
									 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
									  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
									  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
									 +' <div id="chart_div_traffer1" style="width: 500px; height: 220px;margin-left:10px;"></div>'
									 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
									 
									 +'</div>'
									 +'</div>';
			            	 }
			            	 else{
			            		 contentDiv=contentDiv
				            	 +'<div id="popover_content_wrapper" style="width:550px;">'
									+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
									  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:25px;width:460px;text-transform: uppercase;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Campaign Name:</div><div style="width:109px;float:left;margin-left:10px;"><b>Goal Quantity</b></div> <div style="width:50px;float:left;margin-left:10px;"><b>CPM</b></div><div style="float:left; margin"><b>Start Date</b></div></div>'
									  +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>50,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$5.00</b></div><div style="float:left;"><b>05/31/2013</b></div></div>'
									 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
									  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
									  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
									 +' <div id="chart_div_traffer" style="width: 500px; height: 220px;margin-left:10px;"></div>'
									 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
									 
									 +'</div>'
									 +'</div>';
			            		 
			            	 }*/
			            	// var chartData=element.chartData;
			            	 
			            	
			
		}
	 
	 function makeTrafferPopUP(id,title,subTitle,contentDiv){
		  //  alert("hi");
			if(lastPopUpId != 0 && lastPopUpId != id) {
				$('#'+lastPopUpId).popover('hide');
			}
			lastPopUpId = id;
			setTimeout(function(){makeTrafferPopUPDelay(id,title,subTitle,contentDiv)},300);
	}

	function makeTrafferPopUPDelay(id,title,subTitle,contentDiv) {
		var content='<div id="content">'+contentDiv+'</div>';
		
	    $("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+title+"</span>"+"<br/>"+subTitle);	
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
		
		 $('#content').html(contentDiv);
		
		 /*if(forcastDTOStatus){
			 drawChart3();
		 }
		 else {
			 drawChart4(); 
		 }*/
	}
	
	 function getAdvertiserTotalCurrent() {
	//	 alert("3");
	//	 alert(selectedPublisher+" "+startDate+" "+endDate+" "+advertisers+" "+agencies+" "+SelectedProperty);
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadAdvertiserTotalDataListCurrent.lin",
			  cache : false,
			  data : {
				  //publisherName : selectedPublisher,
		    	  //startDate:startDate,
		    	  //endDate:endDate,
		    	   publisherName : selectedPublisher,
		    	   startDate:startDate,
		    	   endDate:endDate,
		    	   advertiser:advertisers,
		    	   agency:agencies,
		    	   properties : SelectedProperty
			  		},
				  dataType: 'json',
			  success: function (data) {
				  		optimizingFlag++;
			           $.each(data, function(index, element) {
			        	  if (index == 'advertiserTotalDataListCurrent' ) {
			        		  advertiserTotalCurrentData = element;
			        		  
			        		  /*advertiserTotalCurrentData.sort(function(a, b){
			        			  return b.CTR-a.CTR
			        			 })*/
			        			 
			        //		  alert("advertiserTotalCurrentData.length = "+advertiserTotalCurrentData.length);
			        		  checkFlag();
						  }

			        	  
			           });
					  
			  },
			      
			       error: function(jqXHR, exception) {
			    	   
			    	   }
			  
			  });
		 		 
		  }catch(exception){
			  
			}

	}

	 function getAdvertiserTotalCompare() {
	//	 alert("4");
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadAdvertiserTotalDataListCompare.lin",
			  cache : false,
			  data : {
				   publisherName : selectedPublisher,
		    	   startDate:compareStartDate,
		    	   endDate:compareEndDate,
		    	   advertiser:advertisers,
		    	   agency:agencies,
		    	   properties : SelectedProperty
			  		},
				  dataType: 'json',
			  success: function (data) {
				  		optimizingFlag++;
			           $.each(data, function(index, element) {
			        	  if (index == 'advertiserTotalDataListCompare') {
			        		  advertiserTotalCompareData = element;
			        		  
			        		  /*advertiserTotalCurrentData.sort(function(a, b){
			        			  return b.CTR-a.CTR
			        			 })*/
			        		  
			   //     		  alert("advertiserTotalCompareData.length = "+advertiserTotalCompareData.length);
			        		  checkFlag();
						  }
		
			           });
					  
			  },
			      
			       error: function(jqXHR, exception) {
			    	   
			    	   }
			  
			  });
		 		 
		  }catch(exception){
			  
			}

	}
	 
	 function getAdvertiserTotalMTD() {
	//	 alert("5");
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadAdvertiserTotalDataListMTD.lin",
			  cache : false,
			  data : {
				  	publisherName : selectedPublisher,
				  	advertiser:advertisers,
				  	agency:agencies,
				  	properties : SelectedProperty
			  		},
				  dataType: 'json',
			  success: function (data) {
				  		optimizingFlag++;
			           $.each(data, function(index, element) {
			        	  if (index == 'advertiserTotalDataListMTD') {
			        		  advertiserTotalMTDData = element;
			 //       		  alert("advertiserTotalMTDData.length = "+advertiserTotalMTDData.length);
			        		  checkFlag();
						  }
		
			           });
					  
			  },
			      
			       error: function(jqXHR, exception) {
			    	   
			    	   }
			  
			  });
		 		 
		  }catch(exception){
			  
			}

	 }
	 
	 function getDeliveryIndicatorData()
	 {

	//	 alert("6");
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadDeliveryIndicatorData.lin",
			  cache : false,
			  data : {
				   publisherName : selectedPublisher,
		    	   startDate:startDate,
		    	   endDate:endDate,
		    	   advertiser:advertisers,
		    	   agency:agencies,
		    	   properties : SelectedProperty
			  		},
				  dataType: 'json',
				  success: function (data) {
				  		optimizingFlag++;
			           $.each(data, function(index, element) {
			        	  if (index == 'deliveryIndicatorDataList') {
			        		  deliveryIndicatorData = element;
		//	        		  alert("deliveryIndicatorData.length = "+deliveryIndicatorData.length);
			        		  checkFlag();
						  }
		
			           });
					  
			  },
			      
			       error: function(jqXHR, exception) {
			    	   
			    	   }
			  
			  });
		 		 
		  }catch(exception){
			  
			}

	 
	 }
	 
	 function checkFlag()
	 {
		 if(optimizingFlag == 4 && advertiserTotalMTDData.length == 0 && 
				 advertiserTotalCurrentData.length == 0 && advertiserTotalCompareData.length == 0) {
			 
			// alert("optimizingFlag inside if = "+optimizingFlag);
			 
		}
		 else if(optimizingFlag == 4)
		 {
			// alert("optimizingFlag inside else if = "+optimizingFlag);
			 	
			 	getPerformanceSummaryHeaderFilteredData();
			 	getTrendAnalysisHeaderFilteredData();
			 	getPerformerFilteredData();
			 	getNonPerformerFilteredData();
			 	getMostActiveFilteredData();
			 	getTopGainersFilteredData();
			 //	getTopLosersFilteredData();
			 	getPerformanceMetricsFilteredData();
			 	
			 	loadOptimizedPerformanceSummaryHeaderData();
			 	loadOptimizedTrendAnalysisHeaderData();
			 	loadOptimizedPerformerLineItems();
			 	loadOptimizedNonPerformerLineItems();
			 	loadOptimizedMostActiveLineItems();
				loadOptimizedTopGainers();
				loadOptimizedTopLosers();
				loadOptimizedPerformanceMetrics();
				
				
				
		 }
	 }
	 
///////////////////Optimized Functions///////////////////////////////////
	 
		function loadOptimizedPerformanceSummaryHeaderData() {
			
		//	alert("performanceSummaryHeaderFilteredData.length = "+performanceSummaryHeaderFilteredData.length);
			var impressions = 0;
			var clicks = 0;
			var ctr = 0;
			var budget = 0;
			var divContent = "";
			
			for(i= 0; i < performanceSummaryHeaderFilteredData.length; i++)
			{
				var headerObj = performanceSummaryHeaderFilteredData[i];
				impressions = impressions + headerObj.impressionDelivered;
				clicks = clicks + headerObj.clicks;
				budget = budget + headerObj.revenueDeliverd;
			}
			ctr = (clicks / impressions) * 100;
			
			if(i > 0)
			{
				
				divContent = divContent 
	          	+'<div style="width:98%;float:left;">'
				+'<div class="summary_bar">'
				+'<div style="">IMPRESSIONS</div>'
				+'<div class="summary_value">'+formatInt(impressions)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">CLICKS</div>'
				+'<div class="summary_value">'+formatInt(clicks)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">CTR</div>'
				+'<div class="summary_value">'+formatFloat(ctr,4)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">BUDGET</div>'
				+'<div class="summary_value">$'+formatInt(budget)+'</div>'
				+'</div>'
				+'</div>';
			}
			else
			{
		          divContent = divContent 
		          	+'<div style="width:98%;float:left;">'
					+'<div class="summary_bar">'
					+'<div style="">IMPRESSIONS</div>'
					+'<div class="summary_value">0</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">CLICKS</div>'
					+'<div class="summary_value">0</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">CTR</div>'
					+'<div class="summary_value">0.0000</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">BUDGET</div>'
					+'<div class="summary_value">$0.00</div>'
					+'</div>'
					+'</div>';
			}
			
			$('#advertiser_performance_summary_header').html(divContent);
             
		}
		
		
				
		function loadOptimizedPerformerLineItems(){
			   var tableTR="";
			   $("#topPerformLineItemTable tbody tr").remove();
				
				if(performerFilteredData != null && performerFilteredData.length > 0)
				{ 
					//alert("agencyname = "+agencyname+" advertisername = "+advertisername);
					for (counter=0; counter < 5 && counter < performerFilteredData.length ; counter++)
					{
						var topPerformerObj  = performerFilteredData[counter];
						var id = "topPerformer_"+topPerformerObj.lineItemId;
						
						if(counter == 2)
						{
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			+'<td onclick="showRichMedia()"><i class="cus-chart-bar"></i></td>'
	     			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+topPerformerObj.campaignLineItem+'</td>'
	     			        
					        +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.impressionDelivered)+'</td>'						      
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.clicks)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topPerformerObj.CTR,4)+'%</td></tr>';
						
						}
						else
	         			{
	         			    tableTR=tableTR
         		   			+'<tr style="cursor:pointer;" id='+id+' onclick=javascript:showPerformerNonPerformerPopup("'
         		   			+id+'",false) rel="popover" class="odd gradeX">'
         		   			+'<td></td>'
         		   			+'<td id="'+id+'_title" >'+topPerformerObj.campaignLineItem+'</td>'
         			        
					        +'<td style="text-align:right;" class="">'+formatInt(topPerformerObj.impressionDelivered)+'</td>'						      
					        +'<td style="text-align:right;">'+formatInt(topPerformerObj.clicks)+'</td>'
					        +'<td style="text-align:right;">'+formatFloat(topPerformerObj.CTR,4)+'%</td></tr>';
	         			  
	         			}
					}
					
					if(counter > 0)
					{
						$("#topPerformLineItemTable tbody").append(tableTR);
					}
					else
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
						$("#topPerformLineItemTable tbody").append(tableTR);
					}
					 
				}
				else
				{
					
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#topPerformLineItemTable tbody").append(tableTR);
					
	           }	
				
			  
		}

		function loadOptimizedNonPerformerLineItems(){
			//alert("8");
			var tableTR="";
			   $("#topNonPerformLineItemTable tbody tr").remove();
				
				if(nonPerformerFilteredData != null && nonPerformerFilteredData.length > 0)
				{
					//alert("agencyname = "+agencyname+" advertisername = "+advertisername);
					for (counter=0, i=nonPerformerFilteredData.length-1; counter < 5 && i >= 0; counter++,i--)
					{
						var topNonPerformerObj = nonPerformerFilteredData[i];
						var topNonPerformerIndicatorObj = nonPerformerFilteredDeliveryIndicatorData[i];
						
						var bookedImpression = "NA";
						if(topNonPerformerIndicatorObj.bookedImpressions > 0) {
	        			   bookedImpression = formatInt(topNonPerformerIndicatorObj.bookedImpressions);
	        		   }
						
						var id = "topNonPerformer_"+topNonPerformerObj.lineItemId;
						tableTR=tableTR
     		   			+'<tr style="cursor:pointer;" id='+id+' >'
     		   			+'<td onclick=showTraffer("'+id+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
     			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+topNonPerformerObj.campaignLineItem+'</td>'
				        +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+bookedImpression+'</td>'
				        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.impressionDelivered)+'</td>'
				        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.clicks)+'</td>'
				        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerObj.CTR,4)+'%</td>'
				        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerIndicatorObj.deliveryIndicator,4)+'%</td></tr>';
					}
					if(counter > 0)
					{
						$("#topNonPerformLineItemTable tbody").append(tableTR);
					}
					else
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
						$("#topNonPerformLineItemTable tbody").append(tableTR);
					}
					 
				}
				else
				{
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="7" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#topNonPerformLineItemTable tbody").append(tableTR);
					
	           }	

		}
		
		
		function loadOptimizedMostActiveLineItems(){
			//alert("8");
			   var tableTR="";
			   $("#mostActiveLineItemTable thead tr").remove();	 
			   $("#mostActiveLineItemTable tbody tr").remove();
			   
			   var loader = '<tr class="odd gradeX">'
				   +'<td colspan="6" style="color:red; text-align:center;">'
				   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
			   $("#mostActiveLineItemTable tbody").append(loader);
			   
			   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
				                   +'<th style="text-align:right;">CTR(%)</th>'
				                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
				                   +'<th style="text-align:right;">CHG(Life Time)</th>'
				                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th>'
				                   +'<th style="text-align:right;">DELIEVERY IND</th></tr>';
			   $("#mostActiveLineItemTable thead").append(tableHeadTR);
			   
				if(mostActiveFilteredData != null && mostActiveFilteredData.length > 0)
				{ 
					for (counter=0 ; counter < 5 && counter < mostActiveFilteredData.length; counter++)
					{
					   var mostActiveObj  = mostActiveFilteredData[counter];
					   //alert("aaaaaaaaaaa="+mostActiveObj)
					   var mostActiveIndicatorObj = mostActiveFilteredDeliveryIndicatorData[counter];
					   
					   var ctrChange=mostActiveObj.changeInTimePeriod+"";
	         		   var lineItemName=mostActiveObj.campaignLineItem;
	         		   var id="mostActive_"+mostActiveObj.lineItemId;
	         		   
	         		   var deliveryIndicator = formatFloat(mostActiveIndicatorObj.deliveryIndicator,4)+"%";
	        		   if(mostActiveIndicatorObj.deliveryIndicator == 10000) {
	        			   deliveryIndicator = "NA";
	        		   }
	         		   
	         		   tableTR=tableTR
	         		    +'<tr style="cursor:pointer;" id='
	         		    +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA"><td id="'
	         		    +id+'_title" rel="popover" >'
	         		    +lineItemName+'</td><td style="text-align:right;" id="'
	         		    +id+'_ctr">'
	         		    +formatFloat(mostActiveObj.CTR,4)+'%</td><td style="text-align:right;" width="56px">';
	         		   if(ctrChange.indexOf('-')>=0){
	         			  tableTR=tableTR+'<img src="img/Arrow2Down.png" width="11" height="12" style="margin-right: 5px;">';		         			      
	         		   }else{
	         			  tableTR=tableTR+'<img src="img/Arrow2Up.png" width="11" height="12" style="margin-right: 5px;">';
	         		   }
	         		  tableTR=tableTR
	         		       + formatFloat(ctrChange,4)+'%</td><td style="text-align:right;">'
	         		       +formatFloat(mostActiveObj.changeInLifeTime,4)+'%</td><td style="text-align:right;" class="">'
	         		       +formatInt(mostActiveObj.impressionDelivered)+'</td><td style="text-align:right;">'
	         		       +deliveryIndicator+'</td></tr>';
	         		  }
					if(counter > 0)
					{
						$("#mostActiveLineItemTable tbody").append(tableTR);
					}
					else
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
						//$("#mostActiveLineItemTable tbody").append(tableTR);
					}
					 
				}
				else
				{
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
				//	$("#mostActiveLineItemTable tbody").append(tableTR);
					
	           }
				
				$("#mostActiveLineItemTable tbody tr").remove();
		        $("#mostActiveLineItemTable tbody").append(tableTR); 
		}
		
		
		function loadOptimizedTopGainers(){
			var tableTR="";
			   $("#topGainersLineItemsTable thead tr").remove();
			   $("#topGainersLineItemsTable tbody tr").remove();
			   
			   var loader = '<tr class="odd gradeX">'
				   +'<td colspan="5" style="color:red; text-align:center;">'
				   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
			   $("#topGainersLineItemsTable tbody").append(loader);
			   
			   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
				                   +'<th style="text-align:right;">CTR(%)</th>'
				                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
				                   +'<th style="text-align:right;">CHG(Life Time)</th>'
				                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
			   $("#topGainersLineItemsTable thead").append(tableHeadTR); 
				
				if(topGainersFilteredData != null && topGainersFilteredData.length > 0)
				{ 
					for (counter=0, i = 0; counter < 5 && i < topGainersFilteredData.length; i++)
					{
					   var topGainersObj  = topGainersFilteredData[i];
					   //alert(" AAA topGainersObj.changeInTimePeriod "+topGainersObj.changeInTimePeriod);
					   
					   if(!(topGainersObj.changeInTimePeriod == undefined) && topGainersObj.changeInTimePeriod > 0)
					   {
						   var id="topGainers_"+topGainersObj.lineItemId;
						   
						   var dataPercent=topGainersObj.changeInLifeTime+"";	         		
		         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
						   
						   tableTR=tableTR
         		   		   +'<tr style="cursor:pointer;" id='
         		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA"><td id="'
         		   		   +id+'_title" rel="popover" >'
         		   		   +topGainersObj.campaignLineItem+'</td><td style="text-align:right;" id="'
         		   		   +id+'_ctr">'
         		           +formatFloat(topGainersObj.CTR,4)+'%</td><td style="text-align:right;" width="90px">'
         		           +formatFloat(topGainersObj.changeInTimePeriod,4)+'%</td><td style="text-align:right;">'
         		           +formatFloat(topGainersObj.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
						   +'<div class="bar" data-percentage="'
						   +dataPercent+'" style="background: green;"></div></div></td><td style="text-align:right;" class="">'
						   +formatInt(topGainersObj.impressionDelivered)+'</td></tr>';
						   
						   counter ++;
					   	}
	         		  }
					
					if(counter > 0)
					{
						//$("#topGainersLineItemsTable tbody").append(tableTR);
					}
					else
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
						//$("#topGainersLineItemsTable tbody").append(tableTR);
					}
					 
				}
				else
				{
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
				//	$("#mostActiveLineItemTable tbody").append(tableTR);
					
	           }	

				$("#topGainersLineItemsTable tbody tr").remove();
		        $("#topGainersLineItemsTable tbody").append(tableTR);
		}
		
		
		function loadOptimizedTopLosers(){
			var tableTR="";
			   $("#topLosersLineItemsTable thead tr").remove();
			   $("#topLosersLineItemsTable tbody tr").remove();
			   
			   var loader = '<tr class="odd gradeX">'
				   +'<td colspan="5" style="color:red; text-align:center;">'
				   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
			   $("#topLosersLineItemsTable tbody").append(loader);
			   
			   var tableHeadTR='<tr><th>CAMPAIGN LINE ITEMS</th>'
				                   +'<th style="text-align:right;">CTR(%)</th>'
				                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
				                   +'<th style="text-align:right;">CHG(Life Time)</th>'
				                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
			   $("#topLosersLineItemsTable thead").append(tableHeadTR); 
				
				if(topLosersFilteredData != null && topLosersFilteredData.length > 0)
				{ 
					for (counter=0, i = 0; counter < 5 && i < topLosersFilteredData.length; i++)
					{
						var topLosersObj = topLosersFilteredData[i];
						
						if(!(topLosersObj.changeInTimePeriod == undefined) && topLosersObj.changeInTimePeriod < 0)
						{
							var dataPercent=topLosersObj.changeInLifeTime+"";		         		
			         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
							
						   var id="topLosers_"+topLosersObj.lineItemId;        		   
		         		   tableTR=tableTR
	     		   		   +'<tr style="cursor:pointer;" id='
	     		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA">'
	     		   		   +'<td id="'+id+'_title" rel="popover" >'
	     		   		   +topLosersObj.campaignLineItem+'</td><td style="text-align:right;" id="'
	     		   		   +id+'_ctr">'
	     		   		   +formatFloat(topLosersObj.CTR,4)+'%</td><td style="text-align:right;">'
	     		   		   +formatFloat(topLosersObj.changeInTimePeriod,4)+'%</td><td style="text-align:right;">'
	     		   		   +formatFloat(topLosersObj.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
	     		   		   +'<div class="bar" data-percentage="'
	     		   		   +dataPercent+'" ></div></div></td><td style="text-align:right;" >'
	     		   		   +formatInt(topLosersObj.impressionDelivered)+'</td></tr>';
		         		   counter ++;
						}
	         		   
	         		 }
					
					if(counter > 0)
					{
						//$("#topLosersLineItemsTable tbody").append(tableTR);
					}
					else
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
					//	$("#topLosersLineItemsTable tbody").append(tableTR);
					}
					 
				}
				else
				{
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
				//	$("#topLosersLineItemsTable tbody").append(tableTR);
					
	           }
				
				$("#topLosersLineItemsTable tbody tr").remove();
		        $("#topLosersLineItemsTable tbody").append(tableTR);

		}
		
		function loadOptimizedPerformanceMetrics()
		{
			//alert("SIZE = "+performanceMetricsFilteredData.length);
			
			   var loadPerformanceMetricsKey =0;
			   var tableTR="";
			   $("#performanceMetricsLoaderId").css("display", "block");
			   jQuery('#performanceMetricsTable').dataTable().fnClearTable();
				
				if(performanceMetricsFilteredData != null && performanceMetricsFilteredData.length > 0)
				{ 
					 jQuery('#performanceMetricsTable').dataTable().fnClearTable();
	            	 jQuery('#performanceMetricsTable').dataTable().fnSettings()._iDisplayLength = 10;
	            	 jQuery('#performanceMetricsTable').dataTable().fnDraw();
					
					for (i = 0; i < performanceMetricsFilteredData.length ; i++)
					{
						var performanceMetricsObj  = performanceMetricsFilteredData[i];
						var performanceMetricsDeliveryIndicatorObj  = performanceMetricsFilteredDeliveryIndicatorData[i];
					//// for loop start/////
		        		   /*(function(i) {
		        			     setTimeout( function(i) {*/
		        
		        		           // code-here
     		  
     		   if(loadPerformanceMetricsKey == 0 && isTrendDefault){
     			   loadPerformanceMetricsKey++;
     			   ordername = performanceMetricsObj.campaignIO;
     			   lineItemArr = performanceMetricsObj.campaignLineItem;
     			   //alert(ordername+"  "+lineItemArr);
     			   getAdvertiserTrendAnalysisHeaderData();
     			   actualLineGeneration();
     			   getActualAdvertiserData();
     			   getForcastAdvertiserData();
     			   
     			   $(".order_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+ordername+"</strong></div>");
     			   $(".lineOrder_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+lineItemArr+"</strong></div>");
     			   $("#order_dropdown_name").text("");
     			   $("#line_dropdown_name").text("");
     			   $("#order_dropdown_name").text(ordername);
     			   $("#line_dropdown_name").text(lineItemArr);
     		   }
     		   var id="performanceMetrics_"+performanceMetricsObj.lineItemId;
     		   
     		   var bookedImpression = "NA";
     		   if(performanceMetricsDeliveryIndicatorObj.bookedImpressions > 0) {
     			   bookedImpression = formatInt(performanceMetricsDeliveryIndicatorObj.bookedImpressions);
     		   }
     		   
     		   var newRowIndex = jQuery('#performanceMetricsTable').dataTable().fnAddData( [
     		     performanceMetricsObj.campaignIO,
     		    performanceMetricsObj.campaignLineItem,
     		     bookedImpression,
     		     formatInt(performanceMetricsObj.impressionDelivered),
     		     formatInt(performanceMetricsObj.clicks),
     		     formatFloat(performanceMetricsObj.CTR,4),
     		     "$"+formatFloat(performanceMetricsDeliveryIndicatorObj.budget,2),
     		     "$"+formatFloat(performanceMetricsObj.revenueDeliverd,2)
     		   ]);
     		   
     		   var tr = jQuery('#performanceMetricsTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
 		     	tr.setAttribute('id', id);
 		     	tr.setAttribute("style","cursor: hand; cursor: pointer;");
       		   	tr.setAttribute("onclick","showPerformanceMetricsPopup('"+performanceMetricsObj.campaignLineItem.replaceAll("'", "&#apos")+"', '"+id+"');");
       		   	
       		//for loop cont.../////
       		  
		   		/*       }, 10)
		   		       
		   		   })(i)*/
						
						
					}
					
					if(i == 0)
					{
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="8" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
						
						$("#performanceMetricsTable tbody").append(tableTR);
					}
					
					 
				}
				else
				{
					
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="8" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#performanceMetricsTable tbody").append(tableTR);
					
	           }	
				$("#performanceMetricsLoaderId").css("display", "none");
			  
		
		}
		
		
		function loadOptimizedTrendAnalysisHeaderData() {/*
			
			alert("performanceSummaryHeaderFilteredData.length = "+performanceSummaryHeaderFilteredData.length);
			var impressions = 0;
			var clicks = 0;
			var ctr = 0;
			var budget = 0;
			var divContent = "";
			
			for(i= 0; i < performanceSummaryHeaderFilteredData.length; i++)
			{
				var headerObj = performanceSummaryHeaderFilteredData[i];
				impressions = impressions + headerObj.impressionDelivered;
				clicks = clicks + headerObj.clicks;
				budget = budget + headerObj.revenueDeliverd;
			}
			ctr = (clicks / impressions) * 100;
			
			if(i > 0)
			{
				
				divContent = divContent 
	          	+'<div style="width:98%;float:left;">'
				+'<div class="summary_bar">'
				+'<div style="">IMPRESSIONS</div>'
				+'<div class="summary_value">'+formatInt(impressions)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">CLICKS</div>'
				+'<div class="summary_value">'+formatInt(clicks)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">CTR</div>'
				+'<div class="summary_value">'+formatFloat(ctr,4)+'</div>'
				+'</div>'
				+'<div class="summary_bar">'
				+'<div style="">BUDGET</div>'
				+'<div class="summary_value">$'+formatInt(budget)+'</div>'
				+'</div>'
				+'</div>';
			}
			else
			{
		          divContent = divContent 
		          	+'<div style="width:98%;float:left;">'
					+'<div class="summary_bar">'
					+'<div style="">IMPRESSIONS</div>'
					+'<div class="summary_value">0</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">CLICKS</div>'
					+'<div class="summary_value">0</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">CTR</div>'
					+'<div class="summary_value">0.0000</div>'
					+'</div>'
					+'<div class="summary_bar">'
					+'<div style="">BUDGET</div>'
					+'<div class="summary_value">$0.00</div>'
					+'</div>'
					+'</div>';
			}
			
			$('#advertiser_performance_summary_header').html(divContent);
             
		*/}
		
		
		
		
		
		function getPerformanceSummaryHeaderFilteredData()
		{
			performanceSummaryHeaderFilteredData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					
					if(leftFilterStatus == 1)
					{
						performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.agency == agencyname)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
				
		}
	}
		
		
		function getPerformerFilteredData()
		{
			performerFilteredData = new Array();
			performerFilteredDeliveryIndicatorData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
				for(j = 0; j < deliveryIndicatorData.length ; j++)
				{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator >= 100)
						{
							performerFilteredData.push(dtoObjectCurrent);
							performerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator >= 100 && dtoObjectCurrent.agency == agencyname)
						{
							performerFilteredData.push(dtoObjectCurrent);
							performerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator >= 100 && dtoObjectCurrent.advertiser == advertisername)
						{
							performerFilteredData.push(dtoObjectCurrent);
							performerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator >= 100 && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							performerFilteredData.push(dtoObjectCurrent);
							performerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}
		}
	}
	
		function getNonPerformerFilteredData()
		{
			nonPerformerFilteredData = new Array();
			nonPerformerFilteredDeliveryIndicatorData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
				for(j = 0; j < deliveryIndicatorData.length ; j++)
				{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator < 100)
						{
							//alert("leftFilterStatus inside getNonPerformerFilteredData= "+leftFilterStatus);
							nonPerformerFilteredData.push(dtoObjectCurrent);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator < 100 && dtoObjectCurrent.agency == agencyname)
						{
					//		alert("leftFilterStatus inside getNonPerformerFilteredData= "+leftFilterStatus);
							nonPerformerFilteredData.push(dtoObjectCurrent);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator < 100 && dtoObjectCurrent.advertiser == advertisername)
						{
					//		alert("leftFilterStatus inside getNonPerformerFilteredData= "+leftFilterStatus);
							nonPerformerFilteredData.push(dtoObjectCurrent);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectIndicator.deliveryIndicator < 100 && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
					//		alert("leftFilterStatus inside getNonPerformerFilteredData= "+leftFilterStatus);
							nonPerformerFilteredData.push(dtoObjectCurrent);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}
		}
	}
	
		function getMostActiveFilteredData()
		{
			mostActiveFilteredData = new Array();
			mostActiveFilteredDeliveryIndicatorData = new Array();
			
		//	alert("inside getMostActiveFilteredData()...");
			var performerFilteredDeliveryIndicatorData1 = performerFilteredDeliveryIndicatorData.slice(0);
			
			performerFilteredDeliveryIndicatorData1.sort(function(a, b){
				 return a.deliveryIndicator-b.deliveryIndicator
			})
			
			for(i = 0; i < performerFilteredDeliveryIndicatorData1.length ; i++)
			{
				var performerFilteredIndicatorObj = performerFilteredDeliveryIndicatorData1[i];
				
				for(j = 0; j < performerFilteredData.length ; j++)
				{
					var performerFilteredObj = performerFilteredData[j];
					
					if(performerFilteredIndicatorObj.lineItemId == performerFilteredObj.lineItemId)
					{
						mostActiveFilteredData.push(performerFilteredObj);
						mostActiveFilteredDeliveryIndicatorData.push(performerFilteredIndicatorObj);
					}
				}
			}
			
			
			for(i = 0; i < mostActiveFilteredData.length ;i++)
			{
				var mostActiveObj = mostActiveFilteredData[i];
				var lastPctChange = 0;
				
				for(j = 0; j < advertiserTotalCompareData; j++)
				{
					compareObj = advertiserTotalCompareData[j];
					if(mostActiveObj.lineItemId == compareObj.lineItemId)
					{
						if(compareObj.CTR == 0 || compareObj.CTR == undefined)
						{
							lastPctChange = 0;
						}
						else
						{
							lastPctChange = ((mostActiveObj.CTR - compareObj.CTR) / compareObj.CTR) * 100;
						}
						
						
					}	
				}
				mostActiveObj.changeInTimePeriod = lastPctChange;
				
				
				
				var lifeTimePctChange = 0;
				for(k = 0; k < advertiserTotalMTDData; k++)
				{
					mtdObj = advertiserTotalMTDData[k];
					if(mostActiveObj.lineItemId == mtdObj.lineItemId)
					{
						if(mtdObj.CTR == 0 || mtdObj.CTR == undefined)
						{
							lifeTimePctChange = 0;
						}
						else
						{
							lifeTimePctChange = ((mostActiveObj.CTR - mtdObj.CTR) / mtdObj.CTR) * 100;
						}
						
					}
				}
				mostActiveObj.changeInLifeTime = lifeTimePctChange;
			}
		}
		
		
		function getTopGainersFilteredData()
		{
			topGainersFilteredData = new Array();
			topGainersFilteredDeliveryIndicatorData = new Array();
			topLosersFilteredData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
				for(j = 0; j < deliveryIndicatorData.length ; j++)
				{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							//topGainersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
							
							topLosersFilteredData.push(dtoObjectCurrent);
							//topLosersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							//topGainersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
							
							topLosersFilteredData.push(dtoObjectCurrent);
							//topLosersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.advertiser == advertisername)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							//topGainersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
							
							topLosersFilteredData.push(dtoObjectCurrent);
							//topLosersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							//topGainersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
							
							topLosersFilteredData.push(dtoObjectCurrent);
							//topLosersFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}
		}
			
			
			
			for(i = 0; i < topGainersFilteredData.length ;i++)
			{
				//alert("before aaaaaaa -1 = ");
				//var topGainersObj = topGainersFilteredData[i];
				var lastPctChange = 0;
				
				for(j = 0; j < advertiserTotalCompareData.length; j++)
				{
					//alert("before aaaaaaa 0 = ");
					compareObj = advertiserTotalCompareData[j];
					if(topGainersFilteredData[i].lineItemId == compareObj.lineItemId)
					{
						//alert("before aaaaaaa = "+compareObj.CTR);
						if(compareObj.CTR == 0 || compareObj.CTR == undefined)
						{
							lastPctChange = 0;
							//alert("aaaaaaaa");
						}
						else
						{
							lastPctChange = ((topGainersFilteredData[i].CTR - compareObj.CTR) / compareObj.CTR) * 100;
							//alert("bbbbbbbb");
						}
						
						topGainersFilteredData[i].changeInTimePeriod = lastPctChange;
					}	
				}
				
				
				
				
				var lifeTimePctChange = 0;
				for(k = 0; k < advertiserTotalMTDData.length; k++)
				{
					mtdObj = advertiserTotalMTDData[k];
					if(topGainersFilteredData[i].lineItemId == mtdObj.lineItemId)
					{
						//alert("before ccccccc");
						if(mtdObj.CTR == 0 || mtdObj.CTR == undefined)
						{
							lifeTimePctChange = 0;
							//alert("ccccccccc");
						}
						else
						{
							lifeTimePctChange = ((topGainersFilteredData[i].CTR - mtdObj.CTR) / mtdObj.CTR) * 100;
							//alert("dddddddddd");
						}
						
						topGainersFilteredData[i].changeInLifeTime = lifeTimePctChange;
					}
				}
				
			}
		
			
			topGainersFilteredData.sort(function(a, b){
			  return b.changeInTimePeriod-a.changeInTimePeriod
			 })
			 
			 topLosersFilteredData = topGainersFilteredData.slice(0);
			
			 topLosersFilteredData.sort(function(a, b){
				  return a.changeInTimePeriod-b.changeInTimePeriod
				 })
				 
				 
			//	alert("TOTAL TOP GAINERS = "+topGainersFilteredData.length);
			// 	alert("TOTAL TOP LOSERS = "+topLosersFilteredData.length);
		}
		
		
		
		function getPerformanceMetricsFilteredData()
		{
		//	alert("inside getPerformanceMetricsFilteredData...");
			performanceMetricsFilteredData = new Array();
			performanceMetricsFilteredDeliveryIndicatorData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
				for(j = 0; j < deliveryIndicatorData.length ; j++)
				{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId)
						{
							//alert("leftFilterStatus in getPerformanceMetricsFilteredData = "+leftFilterStatus);
							performanceMetricsFilteredData.push(dtoObjectCurrent);
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname)
						{
						//	alert("leftFilterStatus in getPerformanceMetricsFilteredData = "+leftFilterStatus);
							performanceMetricsFilteredData.push(dtoObjectCurrent);
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.advertiser == advertisername)
						{
						//	alert("leftFilterStatus in getPerformanceMetricsFilteredData = "+leftFilterStatus);
							performanceMetricsFilteredData.push(dtoObjectCurrent);
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
					//		alert("leftFilterStatus in getPerformanceMetricsFilteredData = "+leftFilterStatus);
							performanceMetricsFilteredData.push(dtoObjectCurrent);
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}
		}
		//	alert("After For Loop "+performanceMetricsFilteredData.length);
	}
		
		
		function getTrendAnalysisHeaderFilteredData()
		{/*
			performanceSummaryHeaderFilteredData = new Array();
			
			for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
					var dtoObjectCurrent = advertiserTotalCurrentData[i];
					
					if(leftFilterStatus == 1)
					{
						performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.agency == agencyname)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
				
		}
	*/}
		
		
		
	function getLeftFilterStatus()
	{
		if((agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 1;
		//	alert("status "+leftFilterStatus);
		}
		else if(!(agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 2;
		//	alert("status "+leftFilterStatus);
		}
		else if((agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 3;
		//	alert("status "+leftFilterStatus);
		}
		else if(!(agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 4;
		//	alert("status "+leftFilterStatus);
		}
	}
//////////////////////////////////////////////////////	 
	 
	 