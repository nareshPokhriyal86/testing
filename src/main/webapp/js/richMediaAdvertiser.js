google.load("visualization", "1", {packages:["corechart"]});
google.load('visualization', '1', {packages: ['table']});
google.load('visualization', '1', {	'packages' : [ 'geochart' ]	});

var countOfEmptyDataTables = 0;
 function countEmptyDataTables() {
	 countOfEmptyDataTables++;
	 if(countOfEmptyDataTables == 8) {
		 $('#emptyDataTableMsgId').css('display','block');
	 }
 }

  
var timePeriod='LAST 7 DAYS';

var creativeTypeRichMedia = 'rich media';
var startDate;
var endDate;
var compareStartDate;
var compareEndDate ;
var richMediaPopupOuterDivContent = '';
var richMediaGraphTable;
var customEvents;

var oldStartDate ;
var oldEndDate ;
var oldPublisher;

var advertisers ='' ;
var agencies = '';
var lineItem = '';
var order = '';
var selectedPublisher = '';
var selectedPublisherId = '';
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
var campaignTotalCurrentDataPerformanceMetrics;
var campaignTotalCompareDataPerformanceMetrics;
var campaignTotalMTDDataPerformanceMetrics;
var deliveryIndicatorData;
var leftFilterStatus;

var performanceSummaryHeaderFilteredData = new Array();

var performerFilteredData = new Array();
//var performerFilteredDeliveryIndicatorData = new Array();//commented by Dheeraj on 22 Oct 2013

var nonPerformerFilteredData = new Array();
var nonPerformerFilteredDeliveryIndicatorData = new Array();

var mostActiveFilteredData = new Array();
//var mostActiveFilteredDeliveryIndicatorData = new Array();//commented by Dheeraj on 22 Oct 2013

var topGainersFilteredData = new Array();
//var topGainersFilteredDeliveryIndicatorData = new Array();//commented by Dheeraj on 22 Oct 2013

var topLosersFilteredData = new Array();
//var topLosersFilteredDeliveryIndicatorData = new Array();//commented by Dheeraj on 22 Oct 2013

var performanceMetricsFilteredData = new Array();
//var performanceMetricsFilteredDeliveryIndicatorData = new Array();//commented by Dheeraj on 22 Oct 2013

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
    // alert(width)
     richMediaMargin =  Math.round(((width - 753)/2)-50);
    // alert(richMediaMargin);
     $(".modal").css({'width':modalHeaderWidthdiv,'left':marginModalLeft,'right':marginModalRight});
     $(".modal-body").css({'max-height':modalheaderHeight});
     $("#myModalRichMedia").css({'margin-left':richMediaMargin});
     
     
 });

function loadAllDataAdvertiser(){
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
	addLoaders();
	loadRichMediaEventGraph();

	if( tabName == "Trends and Analysis") {
		isTrendDefault = false;
		getAdvertiserTrendAnalysisHeaderData();
		actualLineGeneration();
		getActualAdvertiserData();
		//getForcastAdvertiserData();
	}
	else {
		isTrendDefault = true;
		// do nothing
	}
	
	//generateGeoGraph('advertiserGeoMapByLocation',"loadRichMediaAdvertiserBylocationGraphData","By Location","by_location_zoom");
	generateGeoGraph('advertiserGeoMapByMarket',"loadRichMediaAdvertiserBylocationGraphData","By Market","by_market_zoom");
	
	if(!(oldStartDate == startDate && oldEndDate == endDate))
	{
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
		getPerformerFilteredData();
	 	getNonPerformerFilteredData();
	 	getMostActiveFilteredData();
	 	getTopGainersFilteredData();
	 	getPerformanceMetricsFilteredData();
	
	 	loadOptimizedPerformanceSummaryHeaderData();
		loadOptimizedPerformerLineItems();
		loadOptimizedMostActiveLineItems();
		loadOptimizedTopGainers();
		loadOptimizedTopLosers();
		loadOptimizedPerformanceMetrics();
	}
}


function actualLineGeneration() {
	var divNameImp = "chart_div_left3";
	var divNameClick = "chart_div_left1";
	var divNameCtr = "chart_div_left2";
	lineChart(divNameImp, 'ACTUAL IMPRESSIONS', "[['Date','Impressions'],['31',129.0]]", 'blue',graphWidth,height);
	lineChart(divNameClick, 'ACTUAL CLICKS', "[['Date','Clicks'],['31',3]]", 'green',graphWidth,height);
	lineChart(divNameCtr, 'ACTUAL CTR', "[['Date','CTR'],['31',2.33]]", 'red',graphWidth,height);
	$.ajax({
		type : "POST",
		url : "/acualRichMediaLineChart.lin",
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
			var title = "";
			var mapObj = data['headerMap'];			
			var impressionStr = mapObj['impression'];
			var clicksStr = mapObj['click'];
			var ctrStr = mapObj['ctr'];
			
			lineChart(divNameImp, 'ACTUAL IMPRESSIONS', impressionStr, 'blue',graphWidth,height);
			lineChart(divNameClick, 'ACTUAL CLICKS', clicksStr, 'green',graphWidth,height);
			lineChart(divNameCtr, 'ACTUAL CTR', ctrStr, 'red',graphWidth,height);
			
			$("#chart_div_left3_icon").attr("onclick","zoomInLineChart('ACTUAL IMPRESSIONS','right',"+impressionStr+",'chart_div_left3_icon','blue',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_left1_icon').attr("onclick","zoomInLineChart('ACTUAL CLICKS','left',"+clicksStr+",'chart_div_left1_icon','green',"+modalheaderWidth+","+modalheaderHeight+");");
			$('#chart_div_left2_icon').attr("onclick","zoomInLineChart('ACTUAL CTR','left',"+ctrStr+",'chart_div_left2_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
		
		},
		error : function(jqXHR, exception) {
		}
	});

}

function getAdvertiserTrendAnalysisHeaderData() {
	var order = '';
	var lineItem ='';
	var headerDiv = '';
	
	order = ordername;
	lineItem = lineItemArr;
		
	  var count = 0;
	  
	  $.ajax({
		  type : "POST",
		  url : "/loadRichMediaAdvertiserTrendsAnalysisHeaderDate.lin",
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


function getActualAdvertiserData() {
	$("#actualAdvertiserDiv").css("display", "block");
	$("#actualAdvertiserTableLoaderId").css("display", "block");
	jQuery('#actualAdvertiserTable').dataTable().fnClearTable();
	var row = "";
	   $.ajax({
	 		       type : "POST",
	 		      url : "/actualRichMediaAdvertiser.lin",
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
			 		        	   		        		   
			 		        		  var dtoObject = dataList[key];
			 		        		  
					        		   jQuery('#actualAdvertiserTable').dataTable().fnAddData( [
						        		     dtoObject.date,
						        		     dtoObject.order,
						        		     dtoObject.lineItem,
						        		     formatInt(dtoObject.deliveredImpression),
						        		     formatInt(dtoObject.clicks),
						        		     formatFloat(dtoObject.CTR,4)+"%",
						        		     "$"+formatFloat(dtoObject.revenueDelivered,2)/*,
						        		     "$"+formatFloat(dtoObject.revenueRemaining,2)*/
					        		   ]);
			 		        	   
					        		 //for loop cont.../////	 		        		  
		      	 		        		  key ++;
		       }, 10)
		   })(i)

		}	        	   
		//for loop end .../////  		   
	 		        	   }
	 		        	   else if(index == 'advertiserTrendAnalysisActualDatarList' && (element == null  || element == undefined  || element.length == 0)) {
	 		        		  $("#actualAdvertiserDiv").css("display", "none");
	 		        		  row='<tr class="odd gradeX">'
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
	 		          $("#actualAdvertiserTableLoaderId").css("display", "none");
	 		       },
	 		        error: function(jqXHR, exception) {
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

	function generateGeoGraph(divName,actionName,graphTitle,zoom_Divid){
		$('#'+divName).html('');
		$('#byMarket').css('display','block');
		$('#topGainerArticle').attr('class','span6');
		$('#byMarketArticle').attr('class','span6');
		$('#byMarketArticle').css('margin-left','2.56%');
		var actionUrl  = "/"+actionName+".lin";
		$('#'+divName).html('');
		var chartOptions = {
				region : "US",
				displayMode : "markers",
				resolution:'provinces',
				colorAxis : {minValue:0, colors: ['red', 'yellow', 'green'],displayMode: 'auto'},
				sizeAxis:{minSize:8, maxSize:12}
		
		};
		var mapObj;
		var chartDataStr;
		var isEmpty;
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
		        	   $('#byMarket').css('display','none');
		        	   countEmptyDataTables();
		        	   $('#topGainerArticle').attr('class','span12');
		        	   $('#'+divName).html('<div style="text-align:center;" class="widget alert alert-info adjusted">'
				        +'<i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong>'
				        +'</div>');
		           }
		       },
		       error: function(jqXHR, exception) {
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
			//getForcastAdvertiserData();
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
		
		createEmptyPopup(id, title);
		
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadRichMediaPopUpData.lin",
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
	                      +'<div class="popheading_outer" >'              
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
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Budget Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Budget Remaining:</div>'
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
		            			subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:#52ad00;'>"+chgPercent+"</span>";
	            			}else{
	            				subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span>";
	            				subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'>"+chgPercent+"</span>";
	            			}
		            	 }
		            	 else {
		            		 subTitle="<span style='margin-left:2px;'></span>";
	            			 subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'></span>";
		            	 }
		            	 var chartData=element.chartData;
		            	 setTimeout(function(){makePopUP(id,title,subTitle,contentDiv,chartData)},200);
		            	 
		           }           
		             
		         }); 
		     },
		     error: function(jqXHR, exception) {
		     }
		   });   
		  }catch(error){
		  }	  
		
	}		
	
	function showPerformanceMetricsPopup(title, id){
		var contentDiv="";
		title = title.replaceAll("&#apos","'");
		var subTitle="";
		
		createEmptyPopup(id, title);
		
		try{	  
		    $.ajax({
		      type : "POST",
		      url : "/loadRichMediaPopUpData.lin",
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
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Budget Delivered:</div>'
						  +'<div class="popup_content_left_value" style="width:33%;float:left;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredLifeTime))+'</div>'
						  +'<div class="popup_content_right_value" style="float:right;width:33%;text-align:right;">$'+formatInt(getNumericValue(element.revenueDeliveredInSelectedTime))+'</div>'
						  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
						  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Budget Remaining:</div>'
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
		            	 setTimeout(function(){makePopUP(id,title,subTitle,contentDiv,chartData)},200);
		            	 
		           }           
		             
		         }); 
		     },
		     error: function(jqXHR, exception) {
		     }
		   });   
		  }catch(error){
		  }	  
		  
		
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
			 selectedPublisherId = allPublishersList[0].id;
		 }
		 if(allPublishersList != null && allPublishersList.length > 1){
			 for(j=1;j<allPublishersList.length;j++) {
				ulContents = ulContents + "<li><a href=\"javascript:loadFilterDataByPublisherName('"+allPublishersList[j].id.trim()+"');\" onclick=\"changeAdvertiseViewPublisherDropdown('"+allPublishersList[j].value+"');\" style=\"color:black;\">" + allPublishersList[j].value + "</a></li>";
			 }
		 }
		 $("#advertiserViewPublishersDropDown ul").append(ulContents);
	 }
	 
	 function loadAdvertiserPropertyList() {
		 var propertyList;
		 try{
	 		 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadRichMediaAdvertiserPropertyList.lin",
	 		       cache: false,
	 		      data : {
	 		    	 selectedPublisherId : selectedPublisherId
				    	 
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
		 $('#advertiserViewPropertyTitle').html("All Properties");
		 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changeAdvertiseViewPropertyDropdown('All Properties');\" style=\"color:black;\">All Properties</a></li>";
		 if(propertyList != null && propertyList.length > 0){
			 for(j=0;j<propertyList.length;j++) {
				 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changeAdvertiseViewPropertyDropdown('"+propertyList[j].trim()+"');\" style=\"color:black;\">" + propertyList[j] + "</a></li>";
			 }
		 }
		 $("#advertiserViewPropertyDropDown ul").append(ulContents);
	 }
	
	function showTraffer(id,lineItemId){
		/* Currently not in use as we are not displaying the trafficking icons. */
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
			}
		 
		 function makeTrafferPopUP(id,title,subTitle,contentDiv){
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
		}
	
	 function getAdvertiserTotalCurrent() {
			   
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadRichMediaAdvertiserTotalDataListCurrent.lin",
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
			        	  if (index == 'advertiserTotalDataListCurrentMap' ) {
			        		  mapData  = element;
			        		  campaignTotalCurrentDataPerformanceMetrics = mapData['campainTotal'];
			        		  advertiserTotalCurrentData =  mapData['lineItemCalculated'];
			        		  //alert("current "+ advertiserTotalCurrentData.length)
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
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadRichMediaAdvertiserTotalDataListCompare.lin",
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
			        	  if (index == 'advertiserTotalDataListCompareMap') {
			        		  mapData  = element;
			        		  campaignTotalCompareDataPerformanceMetrics = mapData['campainTotal'];
			        		  advertiserTotalCompareData =  mapData['lineItemCalculated'];
			        		 // alert("compare "+ advertiserTotalCompareData.length)
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
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadRichMediaAdvertiserTotalDataListMTD.lin",
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
			        	  if (index == 'advertiserTotalDataListMTDMap') {
			        		  mapData  = element;
			        		  campaignTotalMTDDataPerformanceMetrics = mapData['campainTotal'];
			        		  advertiserTotalMTDData =  mapData['lineItemCalculated'];
			        		 // alert("MTD "+ advertiserTotalMTDData.length)
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
	 
	 function getDeliveryIndicatorData() {
		  try{
		  $.ajax({
			  type : "POST",
			  url : "/loadRichMediaDeliveryIndicatorData.lin",
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
		 if(optimizingFlag == 4
			&& advertiserTotalCurrentData != null 
			&& advertiserTotalCompareData != null
			&& advertiserTotalMTDData != null
			&& advertiserTotalCurrentData.length == 0 
			&& advertiserTotalCompareData.length == 0
			&& advertiserTotalMTDData.length == 0) {
			 // do nothing
			}
		 else if(optimizingFlag == 4)
		 {		getPerformanceSummaryHeaderFilteredData();
			 	getPerformerFilteredData();
			 	getNonPerformerFilteredData();
			 	getMostActiveFilteredData();
			 	getTopGainersFilteredData();
			 	getPerformanceMetricsFilteredData();
			 	
			 	loadOptimizedPerformanceSummaryHeaderData();
			 	loadOptimizedPerformerLineItems();
			 	loadOptimizedMostActiveLineItems();
				loadOptimizedTopGainers();
				loadOptimizedTopLosers();
				loadOptimizedPerformanceMetrics();
				
				
				
		 }
	 }
	 
///////////////////Optimized Functions///////////////////////////////////
	 
		function loadOptimizedPerformanceSummaryHeaderData() {
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
				/*for(j = 0; j < deliveryIndicatorData.length; j++)
				{
					var indicatorObj = deliveryIndicatorData[j];
					if(headerObj.lineItemId == indicatorObj.lineItemId)
					{
						impressions = impressions + headerObj.impressionDelivered;
						clicks = clicks + headerObj.clicks;
						budget = budget + indicatorObj.budget;
					}
				}*/
				
				
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
				+'<div class="summary_value">'+formatFloat(ctr,4)+'%</div>'
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
					+'<div class="summary_value">0.0000%</div>'
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
			   
			   //following two lines added by Dheeraj on 22 Oct 2013
			   performerFilteredData.sort(function(a, b){
					 return b.CTR-a.CTR
				})
				
				if(performerFilteredData != null && performerFilteredData.length > 0)
				{ 
					for (counter=0; counter < 10 && counter < performerFilteredData.length ; counter++)
					{
						var topPerformerObj  = performerFilteredData[counter];
						var id = "topPerformer_"+topPerformerObj.lineItemId;
						
						if(topPerformerObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0)
						{
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			+'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
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
						// do nothing
					}
					else
					{
						$('#performers').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="5" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
					}
					 
				}
				else
				{
					$('#performers').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="5" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }	
				$("#topPerformLineItemTable tbody tr").remove();
		        $("#topPerformLineItemTable tbody").append(tableTR);
			  
		}

		function loadOptimizedNonPerformerLineItems(){
			
			var tableTR="";
				
				if(nonPerformerFilteredData != null && nonPerformerFilteredData.length > 0)
				{
					for (counter=0, i=nonPerformerFilteredData.length-1; counter < 10 && i >= 0; counter++,i--)
					{
						
						var topNonPerformerObj = nonPerformerFilteredData[i];
					
						var topNonPerformerIndicatorObj = nonPerformerFilteredDeliveryIndicatorData[i];
						
						var bookedImpression = "NA";
						
						for (j=0; j < nonPerformerFilteredDeliveryIndicatorData.length; j++){
							
							if (nonPerformerFilteredDeliveryIndicatorData[j].lineItemId == topNonPerformerObj.lineItemId){
								
								if(topNonPerformerIndicatorObj.bookedImpressions > 0) {
									
									bookedImpression = formatInt(topNonPerformerIndicatorObj.bookedImpressions);
									
								}
		        			   
							}
		        			   
						}
						
						var id = "topNonPerformer_"+topNonPerformerObj.lineItemId;
						
						if(topNonPerformerObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0) {
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			+'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
	     		   			//+'<td onclick=showTraffer("'+id+'","'+topNonPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
	     			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+topNonPerformerObj.campaignLineItem+'</td>'
					       /* +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+bookedImpression+'</td>'*/
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.impressionDelivered)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.clicks)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerObj.CTR,4)+'%</td>'
					        //+'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			//+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerIndicatorObj.deliveryIndicator,4)+'%</td>
	     		   			+'</tr>';
						}
						else {
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			+'<td></td>'
	     		   			//+'<td onclick=showTraffer("'+id+'","'+topNonPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
	     			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+topNonPerformerObj.campaignLineItem+'</td>'
					        /*+'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+bookedImpression+'</td>'*/
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.impressionDelivered)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topNonPerformerObj.clicks)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerObj.CTR,4)+'%</td>'
					        //+'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			//+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerIndicatorObj.deliveryIndicator,4)+'%</td>
	     		   			+'</tr>';
						}
					}
					if(counter == 0)
					{	
						$('#topnonperformers').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
					}
					 
				}
				else
				{
					$('#topnonperformers').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="7" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }	
				
				$("#topNonPerformLineItemTable tbody tr").remove();
		        $("#topNonPerformLineItemTable tbody").append(tableTR);

		}
		
		
		function loadOptimizedMostActiveLineItems(){
			   var tableTR="";
			   
				if(mostActiveFilteredData != null && mostActiveFilteredData.length > 0)
				{ 
					for (counter=0 ; counter < 10 && counter < mostActiveFilteredData.length; counter++)
					{
					   var mostActiveObj  = mostActiveFilteredData[counter];
					   
					   var ctrChange=mostActiveObj.changeInTimePeriod+"";
	         		   var lineItemName=mostActiveObj.campaignLineItem;
	         		   var id="mostActive_"+mostActiveObj.lineItemId;
	         		   
	         		   var deliveryIndicator = formatFloat(mostActiveObj.deliveryIndicator,4)+"%";
	        		   if(mostActiveObj.deliveryIndicator == 10000) {
	        			   deliveryIndicator = "NA";
	        		   }
	         		   
	        		   if(mostActiveObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0) {
		         		   tableTR=tableTR
		         		    +'<tr style="cursor:pointer;" id='
		         		    +id+' class="even gradeA">'
		         		   +'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
		         		    +'<td id="'
		         		    +id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover" >'
		         		    +lineItemName+'</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover" id="'
		         		    +id+'_ctr">'
		         		    +formatFloat(mostActiveObj.CTR,4)+'%</td><td style="text-align:right;" width="56px" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover" >';
		         		   if(ctrChange.indexOf('-')>=0){
		         			  tableTR=tableTR+'<img src="img/Arrow2Down.png" width="11" height="12" style="margin-right: 5px;">';		         			      
		         		   }else{
		         			  tableTR=tableTR+'<img src="img/Arrow2Up.png" width="11" height="12" style="margin-right: 5px;">';
		         		   }
		         		  tableTR=tableTR
		         		       + formatFloat(ctrChange,4)+'%</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		         		       +formatFloat(mostActiveObj.changeInLifeTime,4)+'%</td><td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		         		       +formatInt(mostActiveObj.impressionDelivered)+'</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		         		       +deliveryIndicator+'</td></tr>';
	        		   }
	        		   else {
	        			   tableTR=tableTR
		         		    +'<tr style="cursor:pointer;" id='
		         		    +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA">'
		         		    +'<td></td>'
		         		    +'<td id="'
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
	         		  
	         		  }
					if(counter > 0)
					{
						$("#mostActiveLineItemTable tbody").append(tableTR);
					}
					else
					{
						$('#mostActiveDiv').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
					}
					 
				}
				else
				{
					$('#mostActiveDiv').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="7" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }
				
				$("#mostActiveLineItemTable tbody tr").remove();
		        $("#mostActiveLineItemTable tbody").append(tableTR); 
		}
		
		
		function loadOptimizedTopGainers(){
			var tableTR="";
				
				if(topGainersFilteredData != null && topGainersFilteredData.length > 0)
				{ 
					for (counter=0, i = 0; counter < 10 && i < topGainersFilteredData.length; i++)
					{
					   var topGainersObj  = topGainersFilteredData[i];
					   
					   if(!(topGainersObj.changeInTimePeriod == undefined) && topGainersObj.changeInTimePeriod > 0)
					   {
						   var id="topGainers_"+topGainersObj.lineItemId;
						   
						   var dataPercent=topGainersObj.changeInLifeTime+"";	         		
		         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
						   
		         		  if(topGainersObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0) {
							   tableTR=tableTR
	         		   		   +'<tr style="cursor:pointer;" id='
	         		   		   +id+' class="even gradeA">'
	         		   		   +'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
	         		   		   +'<td id="'
	         		   		   +id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover" >'
	         		   		   +topGainersObj.campaignLineItem+'</td><td style="text-align:right;" id="'
	         		   		   +id+'_ctr" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
	         		           +formatFloat(topGainersObj.CTR,4)+'%</td><td style="text-align:right;" width="90px" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
	         		           +formatFloat(topGainersObj.changeInTimePeriod,4)+'%</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
	         		           +formatFloat(topGainersObj.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
							   +'<div class="bar" data-percentage="'
							   +dataPercent+'" style="background: green;"></div></div></td><td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
							   +formatInt(topGainersObj.impressionDelivered)+'</td></tr>';
		         		  }
		         		  else {
		         			   tableTR=tableTR
	         		   		   +'<tr style="cursor:pointer;" id='
	         		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA">'
	         		   		   +'<td></td>'
	         		   		   +'<td id="'
	         		   		   +id+'_title" rel="popover" >'
	         		   		   +topGainersObj.campaignLineItem+'</td><td style="text-align:right;" id="'
	         		   		   +id+'_ctr">'
	         		           +formatFloat(topGainersObj.CTR,4)+'%</td><td style="text-align:right;" width="90px">'
	         		           +formatFloat(topGainersObj.changeInTimePeriod,4)+'%</td><td style="text-align:right;">'
	         		           +formatFloat(topGainersObj.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
							   +'<div class="bar" data-percentage="'
							   +dataPercent+'" style="background: green;"></div></div></td><td style="text-align:right;" class="">'
							   +formatInt(topGainersObj.impressionDelivered)+'</td></tr>';
		         		  }
						   
						   counter ++;
					   	}
	         		  }
					
					if(counter > 0)
					{
						// do nothing
					}
					else
					{
						$('#topGainerDiv').css('display','none');
						countEmptyDataTables();
				    	$('#byMarketArticle').attr('class','span12');
				    	$('#byMarketArticle').css({'margin-left':'0px'});
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
					}
					 
				}
				else
				{
					$('#topGainerDiv').css('display','none');
					countEmptyDataTables();
					$('#byMarketArticle').attr('class','span12');
					$('#byMarketArticle').css({'margin-left':'0px'});
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }	

				$("#topGainersLineItemsTable tbody tr").remove();
		        $("#topGainersLineItemsTable tbody").append(tableTR);
		}
		
		
		function loadOptimizedTopLosers(){
			var tableTR="";
				
				if(topLosersFilteredData != null && topLosersFilteredData.length > 0)
				{ 
					for (counter=0, i = 0; counter < 10 && i < topLosersFilteredData.length; i++)
					{
						var topLosersObj = topLosersFilteredData[i];
						
						if(!(topLosersObj.changeInTimePeriod == undefined) && topLosersObj.changeInTimePeriod < 0)
						{
							var dataPercent=topLosersObj.changeInLifeTime+"";		         		
			         		   dataPercent=dataPercent.substring(dataPercent.indexOf(".")+1,dataPercent.length);
							
						   var id="topLosers_"+topLosersObj.lineItemId;
						   
						   if(topLosersObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0) {
			         		   tableTR=tableTR
		     		   		   +'<tr style="cursor:pointer;" id='
		     		   		   +id+' class="even gradeA">'
		     		   		   +'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
		     		   		   +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover" >'
		     		   		   +topLosersObj.campaignLineItem+'</td><td style="text-align:right;" id="'
		     		   		   +id+'_ctr" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		     		   		   +formatFloat(topLosersObj.CTR,4)+'%</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		     		   		   +formatFloat(topLosersObj.changeInTimePeriod,4)+'%</td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		     		   		   +formatFloat(topLosersObj.changeInLifeTime,4)+'%<div class="progress progress-danger slim" style="border: none; background-color: white;">'
		     		   		   +'<div class="bar" data-percentage="'
		     		   		   +dataPercent+'" ></div></div></td><td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) rel="popover">'
		     		   		   +formatInt(topLosersObj.impressionDelivered)+'</td></tr>';
			         		   counter ++;
						   }
						   else {
							   tableTR=tableTR
		     		   		   +'<tr style="cursor:pointer;" id='
		     		   		   +id+' onclick=javascript:showPerformerNonPerformerPopup("'+id+'",true) class="even gradeA">'
		     		   		   +'<td></td>'
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
	         		   
	         		 }
					
					if(counter > 0)
					{
						// do nothing
					}
					else
					{
						$('#toplosers').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="6" style="color:red; text-align:center;">'
						        +'<div class="widget alert alert-info adjusted">'
						        +'<i class="cus-exclamation"></i>'
						        +'<strong>No records found for the selected filters</strong>'
						        +'</div>'
					        +'</td>'						      
					        +'</tr>';
					}
					 
				}
				else
				{
					$('#toplosers').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }
				
				$("#topLosersLineItemsTable tbody tr").remove();
		        $("#topLosersLineItemsTable tbody").append(tableTR);

		}
		
		function loadOptimizedPerformanceMetrics()
		{
			   var loadPerformanceMetricsKey =0;
			   var tableTR="";
			   jQuery('#performanceMetricsTable').dataTable().fnClearTable();
				
				if(performanceMetricsFilteredData != null && performanceMetricsFilteredData.length > 0)
				{ 
					 jQuery('#performanceMetricsTable').dataTable().fnClearTable();
	            	 jQuery('#performanceMetricsTable').dataTable().fnSettings()._iDisplayLength = 10;
	            	 jQuery('#performanceMetricsTable').dataTable().fnDraw();
					$.each(performanceMetricsFilteredData,function (newIndex,performanceMetricsObj){
					
		        		   (function(newIndex) {
		        			     setTimeout( function(newIndex) {
		        
		        		          
     		  
     		   if(loadPerformanceMetricsKey == 0 && isTrendDefault){
     			   loadPerformanceMetricsKey++;
     			   ordername = performanceMetricsObj.campaignIO;
     			   lineItemArr = performanceMetricsObj.campaignLineItem;
     			   getAdvertiserTrendAnalysisHeaderData();
     			   actualLineGeneration();
     			   getActualAdvertiserData();
     			   //getForcastAdvertiserData();
     			   
     			   $(".order_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+ordername+"</strong></div>");
     			   $(".lineOrder_option").html("<div style='float:left;margin-left:-21px;color:white;'><strong style='text-transform: uppercase;'>"+lineItemArr+"</strong></div>");
     			   $("#order_dropdown_name").text("");
     			   $("#line_dropdown_name").text("");
     			   $("#order_dropdown_name").text(ordername);
     			   $("#line_dropdown_name").text(lineItemArr);
     		   }
     		   var id="performanceMetrics_"+performanceMetricsObj.lineItemId;
     		   
     		   var bookedImpression = "NA";
     		   var budget = "NA"; 
     			 
     		   if(performanceMetricsObj.budget != 0)
     		   {
     			  budget = "$"+formatFloat(performanceMetricsObj.budget,2);  
     		   }
     		  

     		   
     		   if (performanceMetricsObj.market == 'null' || performanceMetricsObj.market == null || performanceMetricsObj.market == undefined || performanceMetricsObj.market == ''){
     			   
     			  market = "NA";
     			   
     		   }else{
     			   
     			  market = performanceMetricsObj.market; 
     		   }
     		   
     		   
     		  if(performanceMetricsObj.bookedImpressions > 0) {
				   bookedImpression = formatInt(performanceMetricsObj.bookedImpressions);
			   }
     		   
     		   var newRowIndex = jQuery('#performanceMetricsTable').dataTable().fnAddData( [
     		     performanceMetricsObj.campaignIO,
     		    performanceMetricsObj.campaignLineItem,
     		     bookedImpression,
     		     formatInt(performanceMetricsObj.impressionDelivered),
     		     formatInt(performanceMetricsObj.clicks),
     		     formatFloat(performanceMetricsObj.CTR,4),
     		     budget,
     		     "$"+formatFloat(performanceMetricsObj.revenueDeliverd,2),
     		     market,
     		     performanceMetricsObj.site,
     		   ]);
     		   
     		   var tr = jQuery('#performanceMetricsTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
 		     	tr.setAttribute('id', id);
 		     	tr.setAttribute("style","cursor: hand; cursor: pointer;");
       		   	tr.setAttribute("onclick","showPerformanceMetricsPopup('"+performanceMetricsObj.campaignLineItem.replaceAll("'", "&#apos")+"', '"+id+"');");
       		   	
       		//for loop cont.../////
       		  
		   		       }, 10)
		   		       
		   		   })(newIndex)
						
						
					});
					
					if(performanceMetricsFilteredData.length == 0)
					{
						$('#performanceMetricsDiv').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="10" style="color:red; text-align:center;">'
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
					$('#performanceMetricsDiv').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="10" style="color:red; text-align:center;">'
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
			//performerFilteredDeliveryIndicatorData = new Array();
			performerFilteredData = performanceSummaryHeaderFilteredData.slice(0);//This line has been added by Dheeraj on 22 Oct 2013
			
			//Following lines have been commented by Dheeraj on 22 Oct 2013
			
			/*for(i = 0; i < advertiserTotalCurrentData.length ; i++)
			{
				var dtoObjectCurrent = advertiserTotalCurrentData[i];
					
					if(leftFilterStatus == 1)
					{
							performerFilteredData.push(dtoObjectCurrent);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.agency == agencyname)
						{
							performerFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername)
						{
							performerFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							performerFilteredData.push(dtoObjectCurrent);
						}
					}
			}*/
		}
	
		function getNonPerformerFilteredData()
		{
			nonPerformerFilteredData = new Array();
			nonPerformerFilteredDeliveryIndicatorData = new Array();
			
			nonPerformerFilteredDataTemp = performerFilteredData.slice(0);
			
			nonPerformerFilteredDataTemp.sort(function(a, b){
				 return b.CTR-a.CTR
			})
			
			for(i = 0; i < nonPerformerFilteredDataTemp.length ; i++)
			{
				var nonPerformerObj = nonPerformerFilteredDataTemp[i];
				
				for(j = 0; j < deliveryIndicatorData.length ; j++)
				{
					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.advertiser == advertisername)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname && nonPerformerObj.advertiser == advertisername)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}
		}
			
			loadOptimizedNonPerformerLineItems();
	}
		
		function getMostActiveFilteredData()
		{
			mostActiveFilteredData = new Array();
			//mostActiveFilteredDeliveryIndicatorData = new Array();
			
			var mostActiveFilteredDataTemp = advertiserTotalCurrentData.slice(0);
			
			for(i = 0; i < mostActiveFilteredDataTemp.length ; i++)
			{
				var mostActiveObj = mostActiveFilteredDataTemp[i];
				
					if(leftFilterStatus == 1)
					{
						mostActiveFilteredData.push(mostActiveObj);
					}
					else if(leftFilterStatus == 2)
					{
						if(mostActiveObj.agency == agencyname)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(mostActiveObj.advertiser == advertisername)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(mostActiveObj.agency == agencyname && mostActiveObj.advertiser == advertisername)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
				
			}
			
			mostActiveFilteredData.sort(function(a, b){
				 return b.deliveryIndicator-a.deliveryIndicator
			})
			
			
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
					else
					{
						lastPctChange = 0;
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
					else
					{
						lifeTimePctChange = 0;
					}
				}
				mostActiveObj.changeInLifeTime = lifeTimePctChange;
			}
			
		}
		
		
		
		function getTopGainersFilteredData()
		{
			
			topGainersFilteredData = new Array();
			//topGainersFilteredDeliveryIndicatorData = new Array();
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
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.advertiser == advertisername)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
				
			}
		}
			
			
			
			for(i = 0; i < topGainersFilteredData.length ;i++)
			{
				var lastPctChange = 0;
				
				for(j = 0; j < advertiserTotalCompareData.length; j++)
				{
					compareObj = advertiserTotalCompareData[j];
					if(topGainersFilteredData[i].lineItemId == compareObj.lineItemId)
					{
						if(compareObj.CTR == 0 || compareObj.CTR == undefined)
						{
							lastPctChange = 0;
						}
						else
						{
							lastPctChange = ((topGainersFilteredData[i].CTR - compareObj.CTR) / compareObj.CTR) * 100;
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
						if(mtdObj.CTR == 0 || mtdObj.CTR == undefined)
						{
							lifeTimePctChange = 0;
						}
						else
						{
							lifeTimePctChange = ((topGainersFilteredData[i].CTR - mtdObj.CTR) / mtdObj.CTR) * 100;
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
		}
		
		
		
		function getPerformanceMetricsFilteredData()
		{
			performanceMetricsFilteredData = new Array();
			//performanceMetricsFilteredDeliveryIndicatorData = new Array();
			
			for(i = 0; i < campaignTotalCurrentDataPerformanceMetrics.length ; i++)
			{

					var dtoObjectCurrent = campaignTotalCurrentDataPerformanceMetrics[i];
				
					if(leftFilterStatus == 1)
					{
							performanceMetricsFilteredData.push(dtoObjectCurrent);

					}
					else if(leftFilterStatus == 2)
					{
						if( dtoObjectCurrent.agency == agencyname)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);

						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);

						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
								performanceMetricsFilteredData.push(dtoObjectCurrent);

						}
					}
				
			}
	
			
			
			// Following lines have been commented by Dheeraj on 22 Oct 2013
				/*for(j = 0; j <deliveryIndicatorData.length ; j++)
				{

					var dtoObjectIndicator = deliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.agency == agencyname)
						{
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername)
						{
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername)
						{
							performanceMetricsFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
				
			}*/
	}
		
		
	function getLeftFilterStatus()
	{
		if((agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 1;
		}
		else if(!(agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 2;
		}
		else if((agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 3;
		}
		else if(!(agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == ""))
		{
			leftFilterStatus = 4;
		}
	}
//////////////////////////////////////////////////////	 
	 
	 
	function loadRichMediaEventPopup(id) {
		 var richMediaEventPopupList;
		 var lineItemName = $('#'+id+'_title').html();
		 richMediaPopupOuterDivContent = '';
		 
		 $('#richMediaPopupOuterDiv').html('');
		 $('#myModalLabel1').html(lineItemName);
		 $('#richMediaEventPopupLoader').css('display','block');
		 $('#myModalRichMedia').modal('show');
		 
		 try{
	 		 $.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadRichMediaEventPopup.lin",
	 		       cache: false,
	 		      data : {
	 		    	  lineItem : lineItemName,
			    	  startDate:startDate,
			    	  endDate:endDate
			    	  },
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {
	 		        	  if(index == 'richMediaEventPopupList') {
	 		        		 if(element != null && element.length  > 0) {
	 		        			var length = element.length;
	 		        			var whiteBackGroundheight = (Math.round(length/2))*190;
	 		        			$('#white').css("min-height",whiteBackGroundheight+"px");
	 		        			 richMediaEventPopupList=element;
	 		        			for(i=0; i<length; i++) {
	 		        				var event = richMediaEventPopupList[i];
	 		        				createRichMediaPopupEventBox(event.customizedCustomEvent, event.value, event.customEvent);
	 		        			}
	 		        		 }
	 		        		 else {
	 		        			richMediaPopupOuterDivContent = '<center><h3>No Events</h3></center>';
	 		        		 }
						  }
	 		           });
	 		          $('#richMediaEventPopupLoader').css('display','none');
	 		          $('#richMediaPopupOuterDiv').append(richMediaPopupOuterDivContent);
	 		       },
	 		       error: function(jqXHR, error) {
	 		    	  $('#richMediaEventPopupLoader').css('display','none');
	 		    	  $('#richMediaPopupOuterDiv').append('<center><h3>No Events</h3></center>');
	 		        }
	 		  });
			}catch(exception){
			}
	 } 
    
    function createRichMediaPopupEventBox(eventTitle, eventValue, tooltip) {
    	richMediaPopupOuterDivContent = richMediaPopupOuterDivContent + '<div title="'+tooltip+'" class="analytics-boards" style="float:left;margin-right:15px;margin-top:15px;margin-left:10px;margin-bottom:10px;width:333px;">'
        +'<div class="board-container" style="width:46%;margin-right:6%;min-height:50px;">'
        +'<div class="board has-chart" style="padding:0px;min-height:50px;">'
        +'<div style="padding:0px;text-align:center;background-color:grey;min-height:38px">'
        +'<h2 class="account-color"  style="color:white;width:100%;word-wrap:break-word;background-color:grey;margin:0px;text-align:center;min-height:30px;">'+eventTitle+'</h2>'
        +'</div>'
        +'<div class="chart-area" style="min-height:115px;">' 
        +'<div class="big-number" style="margin-left: 0px;margin-top:20px;color:black;width:100%;text-align:center;font-size:60px;">'+eventValue+'</div>'
        +'</div></div></div></div>';
    }
    
    function addLoaders()
    {
    	countOfEmptyDataTables = 0;
    	$('#emptyDataTableMsgId').css('display','none');
    	$('#topGainerArticle').attr('class','span6');
    	$('#byMarketArticle').attr('class','span6');
    	$('#performers').css('display','block');
    	$('#topnonperformers').css('display','block');
    	$('#mostActiveDiv').css('display','block');
    	$('#topGainerDiv').css('display','block');
    	$('#toplosers').css('display','block');
    	$('#performanceMetricsDiv').css('display','block');
    	 ////Loader for Top Performer//////////
    	  $("#topPerformLineItemTable tbody tr").remove();
    		var loader = '<tr class="odd gradeX">'
    			   +'<td colspan="5" style="color:red; text-align:center;">'
    			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
    		   $("#topPerformLineItemTable tbody").append(loader);
    		   
    	////Loader for TopNonPerformer//////////
    	 $("#topNonPerformLineItemTable tbody tr").remove();
    	   var loader = '<tr class="odd gradeX">'
    		   +'<td colspan="5" style="color:red; text-align:center;">'
    		   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
    	   $("#topNonPerformLineItemTable tbody").append(loader);   

    	   
    	////Loader for Most Active//////////
    	   $("#mostActiveLineItemTable thead tr").remove();	 
    	   $("#mostActiveLineItemTable tbody tr").remove();
    	   var loader = '<tr class="odd gradeX">'
    		   +'<td colspan="6" style="color:red; text-align:center;">'
    		   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
    	   $("#mostActiveLineItemTable tbody").append(loader);
    	   
    	   var tableHeadTR='<tr><th></th><th>CAMPAIGN LINE ITEMS</th>'
    		                   +'<th style="text-align:right;">CTR(%)</th>'
    		                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
    		                   +'<th style="text-align:right;">CHG(Life Time)</th>'
    		                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th>'
    		                   +'<th style="text-align:right;">DELIEVERY IND</th></tr>';
    	   $("#mostActiveLineItemTable thead").append(tableHeadTR);

    	   
    	/////////Loader for Top Gainer////////
    		 $("#topGainersLineItemsTable thead tr").remove();
    		 $("#topGainersLineItemsTable tbody tr").remove();
    		   
    		   var loader = '<tr class="odd gradeX">'
    			   +'<td colspan="5" style="color:red; text-align:center;">'
    			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
    		   $("#topGainersLineItemsTable tbody").append(loader);
    		   
    		   var tableHeadTR='<tr><th></th><th>CAMPAIGN LINE ITEMS</th>'
    			                   +'<th style="text-align:right;">CTR(%)</th>'
    			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
    			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
    			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
    		   $("#topGainersLineItemsTable thead").append(tableHeadTR);
    		   

    	/////////Loader for Top Loser////////
    		   $("#topLosersLineItemsTable thead tr").remove();
    		   $("#topLosersLineItemsTable tbody tr").remove();
    		   
    		   var loader = '<tr class="odd gradeX">'
    			   +'<td colspan="6" style="color:red; text-align:center;">'
    			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
    		   $("#topLosersLineItemsTable tbody").append(loader);
    		   
    		   var tableHeadTR='<tr><th></th><th>CAMPAIGN LINE ITEMS</th>'
    			                   +'<th style="text-align:right;">CTR(%)</th>'
    			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
    			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
    			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
    		   $("#topLosersLineItemsTable thead").append(tableHeadTR);
    		   
    		   
    	//////////Loader for Performance Metrics///////////////	 
    		   $("#performanceMetricsLoaderId").css("display", "block");
    		   jQuery('#performanceMetricsTable').dataTable().fnClearTable();
    	
    	//////////Loader for Rich Media Advertiser Header ///////////////
    		   var loader = '<div style="display:block;margin-left:45%;margin-top:20px;">'
						+'<img src="img/loaders/type6/light/80.gif" alt="loader">'
						+'</div>';
    		   
    		   $("#advertiser_performance_summary_header").empty();
    		   $("#advertiser_trends_analysis_header").empty();
    		   
    		   $("#advertiser_performance_summary_header").append(loader);
    		   $("#advertiser_trends_analysis_header").append(loader);
    		   
    }
    
