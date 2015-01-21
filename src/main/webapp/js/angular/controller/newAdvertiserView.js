
var accountId = '';
var campaignId = '';
var isNoise = true;
var accountName = '';
var campaignName = '';
var initialLoad = true;
var threshold = 0.000;

function applyFilters(){
	campaignId = $.trim($('#campaignDropDown').val());
	campaignId=campaignId.replace("_","");
	threshold = $.trim($('#threshold').val());
	if(threshold != undefined && threshold != null) {
		threshold = threshold.replace("%", "");
	}
	if(isNoise && isNaN(threshold)) {
		toastr.error('Threshold should be a number.');
		return;
	}
	campaignName = $('#campaignDropDown option:selected').text();
	accountName = $('#accountDropDown option:selected').text();
	getHeaderData();
	angular.bootstrap($("#lineChartDiv"),['lineChartApp']);	
   // angular.bootstrap($("#barChartDiv1"),['barChartApp']);
    //angular.bootstrap($("#barChartDiv2"),['barChartApp']);
   // angular.bootstrap($("#pieChartDiv"),['pieChartApp']);
    //angular.bootstrap($("#tableDataDiv"),['tableDataApp']);   
    if (initialLoad){
    	angular.bootstrap($("#geoChartDiv"),['geoChartApp']);
    	angular.bootstrap($("#tableDataDiv"),['tableDataApp']);
    	angular.bootstrap($("#pieChartDiv"),['pieChartApp']);
    	angular.bootstrap($("#barChartDiv"),['barChartApp']);
    	angular.bootstrap($("#barChartPerfAdSizeDiv"),['barChartPerfAdSizeApp']);
    	angular.bootstrap($("#pieChartImpByAdSizeDiv"),['pieChartImpByAdSizeApp']);
    	
    	initialLoad=false;
    }else{
    	var scope_geoChartDivScope=angular.element(document.getElementById("geoChartDiv")).scope();
    	console.log("Calling intidata on geChart....");
    	scope_geoChartDivScope.initdata();
    	
    	var scope_tableDataDivScope=angular.element(document.getElementById("tableDataDiv")).scope();
    	console.log("reseting data on Table....");
    	scope_tableDataDivScope.resetTable();
    	console.log("Calling intidata on Table....");
    	scope_tableDataDivScope.initdata();
    	
    	var scope_pieChartDivScope=angular.element(document.getElementById("pieChartDiv")).scope();
    	scope_pieChartDivScope.initdata();
    	scope_pieChartDivScope.safeApply(function(){
    	scope_pieChartDivScope.emptyData();
		 	});
    	
    	var scope_barChartDivScope=angular.element(document.getElementById("barChartDiv")).scope();
    	scope_barChartDivScope.initdata();
    	scope_barChartDivScope.safeApply(function(){
    	scope_barChartDivScope.emptyData();
		 	});
    	
     	var scope_barChartPerfAdSizeDivScope=angular.element(document.getElementById("barChartPerfAdSizeDiv")).scope();
    	scope_barChartPerfAdSizeDivScope.initdata();
    	scope_barChartPerfAdSizeDivScope.safeApply(function(){
    	scope_barChartPerfAdSizeDivScope.emptyData();
		 	});
    	
    	var scope_pieChartImpByAdSizeDivScope=angular.element(document.getElementById("pieChartImpByAdSizeDiv")).scope();
    	scope_pieChartImpByAdSizeDivScope.initdata();
    	scope_pieChartImpByAdSizeDivScope.safeApply(function(){
    	scope_pieChartImpByAdSizeDivScope.emptyData();
		 	});
    	
    	/*var scope_lineChartDiv=angular.element(document.getElementById("lineChartDiv")).scope();
    	scope_lineChartDiv.initdata();
    	scope_lineChartDiv.safeApply(function(){
    		scope_lineChartDiv.emptyData();
		 });*/
    	
    }
    angular.bootstrap($("#videoCampaignLineChartDiv"),['videoCampaignLineChartApp']);
    
    angular.bootstrap($(".popup_class"),['tableDataApp']);
    angular.bootstrap($("#mainId"),['mainApp']);   
    loadRichMediaEventGraph();
}

function emptyAllData(){
	var scope_lineChartDivScope=angular.element(document.getElementById("lineChartDiv")).scope();
	scope_lineChartDivScope.emptyData();
	var scope_geoChartDivScope=angular.element(document.getElementById("geoChartDiv")).scope();
	scope_geoChartDivScope.emptyData();
	var scope_pieChartDiv1Scope=angular.element(document.getElementById("pieChartDiv1")).scope();
	scope_pieChartDiv1Scope.emptyData();
	
	var scope_barChartDiv1Scope=angular.element(document.getElementById("barChartDiv1")).scope();
	scope_barChartDiv1Scope.emptyData();
	
	var scope_tableDataDivScope=angular.element(document.getElementById("tableDataDiv")).scope();
	scope_tableDataDivDivScope.resetAllTables();
	
	
}
function getHeaderData() {
	try{
		$('#headerDivInnerId').html(""); 
		$.ajax({
			  type : "POST",
			  url : "/getHeaderData.lin?orderId="+campaignId,
			  cache : false,
			  data : {
				  accountName:accountName,
				  campaignName : campaignName
			  },
				  dataType: 'json',
			      success: function (data) {
			    	   if(data !=null){
			    		   jsonResponse=data[0];	
						   headerDiv= getHeader(
										data[0].c4impressionDelivered,
										data[0].c5clicks,
										data[0].c6CTR,
										data[0].c7budget,
										data[0].c8spent,
										data[0].c9balance,
										data[0].d2endDateTime,
										data[0].d1startDateTime);					
			        	  $('#headerDivInnerId').html(headerDiv); 
			    	   }	
			          
			     },
			     error: function(jqXHR, exception) {
			     }
		});
	}catch(err){
		console.log("getHeaderData:err:"+err);
	}

}

function getHeader(impressionDelivered,clicks,CTR,budget,spent,balance,endDateTime,startDateTime){
	var headerDivData="";
	endDateTime=endDateTime+"";
	if(endDateTime !=null && endDateTime.indexOf("java")>=0){
		endDateTime="None";
	}
	headerDivData=headerDivData
		+'<div class="summary_bar clear_summary_bar">'
		+'	<div style="font-size: 1em;">IMPRESSIONS</div>'
		+'	<div class="summary_value">'+formatInt(impressionDelivered)+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">CLICKS</div>'
		+'	<div class="summary_value">'+formatInt(clicks)+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">CTR</div>'
		+'	<div class="summary_value">'+formatFloat(CTR,4)+'%</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">BUDGET</div>'
		+'	<div class="summary_value">$'+formatFloat(budget,2)+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">SPENT</div>'
		+'	<div class="summary_value">$'+formatFloat(spent,2)+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">BALANCE</div>'
		+'	<div class="summary_value">$'+formatFloat(balance,2)+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">START DATE</div>'
		+'	<div class="summary_value">'+startDateTime+'</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'	<div style="font-size: 1em;">END DATE</div>'
		+'	<div class="summary_value">'+endDateTime+'</div>'
		+'</div>'
		+'</div>';
	
	
return headerDivData;
	
}

 function getUserAccountsDropDownData() {
	try{
		$.ajax({
			  type : "POST",
			  url : "/getUserAccounts.lin",
			  cache : false,
			  data : {},
			  dataType: 'json',
			  success: function (data) {		    	 
				  appendDataToSelectDropDown(data,"accountDropDown");
				  getOrderDropDownData();				  
			  },
			  error: function(jqXHR, exception) {
			     console.log('ajax faild to load data :'+exception); 	   
			  }
		});
		
	}catch(err){
		console.log('getUserAccountsDropDownData:exception:'+err);
	}
 }
 
 function appendDataToSelectDropDown(dataArray,elementId){	
     var account = $('#advertiserId').val();
     var order = $('#orderId').val();
     var setDefault = true;
    	 
	 $('#'+elementId).find('option').remove();
	 $.each(dataArray, function( index, value ) {	
		 $('#'+elementId).append('<option id="'+index+'" value="'+index+'">'+value+'</option>');
	 });
	 if(account != '' && elementId == 'accountDropDown') {
		 $('#'+elementId).select2("val",'_'+account);
		 $('#advertiserId').val('');
		 setDefault = false;
	 }
	 if(order != '' && elementId == 'campaignDropDown') {
		 $('#'+elementId).select2("val",'_'+order);
		 $('#orderId').val('');
		 setDefault = false;
	 }
	 if(setDefault) {
		 for(key in dataArray) {
			 $('#'+elementId).select2("val",key);
			 break;
		 }
	 }
	 
 }
 
  
 function getOrderDropDownData() {	 
	
	 try{
		 var value=$('#accountDropDown').val();
		 var text=$.trim($('#'+value).html());
		 value=value.replace("_","");
		 
		 $.ajax({
				  type : "POST",
				  url : "/getOrdersByAdvertiserOrAgency.lin",
				  cache : false,
				  data : {id:value,advertiserOrAgency:text},
				  dataType: 'json',
				  success: function (data) {		    	 
					  appendDataToSelectDropDown(data,"campaignDropDown");
					  accountId = $('#accountDropDown').val();
					  accountId=accountId.replace("_","");
					  accountName = $('#accountDropDown option:selected').text();
					  campaignId = $.trim($('#campaignDropDown').val());
					  campaignId=campaignId.replace("_","");
					  campaignName = $('#campaignDropDown option:selected').text();
					  while(campaignId !=null && campaignId.length>0){
						  if(initialLoad){
							  applyFilters();
						  }						  
						  break;
					  }
					  					  
				  },
				  error: function(jqXHR, exception) {
				     console.log('ajax faild to load data :'+exception); 	   
				  }
		});
	}catch(err){
			console.log('getOrderDropDownData:error:'+err);
	}
 }
 
 function loadRichMediaEventGraph() {
	 $('#richMediaEventGraphOuterDiv').html('');
	 $('#selectRichMediaEventGraphMetricsDiv').css('display','none');
	 $('#emptyDataTableMsgId').css('display','none');
	 $('#richMediaEventGraphLoader').css('display','block');
	 try{
 		 $.ajax({
 		       type : "POST",
 		       url : "/richMediaEventGraph.lin",
 		       cache: false,
 		      data : {
 		    	 campaignId : campaignId
		    	  },
 		       dataType: 'json',
 		       success: function (data) {	    	  
 		    	  $.each(data, function(index, element) {
 		        	  if(index == 'richMediaGraphTable') {
 		        		 richMediaGraphTable=element;
					  }
 		        	 if(index == 'customEvents') {
 		        		customEvents=element;
 		        	 }
 		           });
 		    	   drawVisualization('Market', 0);
 		       },
 		       error: function(jqXHR, error) {
 		    	  $('#richMediaEventGraphOuterDiv').html('');
 		    	  $('#richMediaEventGraphLoader').css('display','none');
	 		   	  $('#emptyDataTableMsgId').css('display','block');
 		        }
 		  });
		}catch(exception){
		}
 }


function drawVisualization(selectedMetricsText,selectedMetrics) {
	$('#richMediaEventGraphOuterDiv').html('');
	if(richMediaGraphTable != null && richMediaGraphTable.length  >0 && customEvents != null && customEvents.length  >0) {
		$('#selectRichMediaEventGraphMetricsLabel').html(selectedMetricsText+'<span class="caret" style="margin-left: 6px;margin-top: 8px;"></span>');
		$('#selectRichMediaEventGraphMetricsDiv').css('display','block');
		$('#richMediaEventGraphLoader').css('display','none');
		$('#emptyDataTableMsgId').css('display','none');
		
		for(i=0; i<customEvents.length; i++) {
			var customEventId = customEvents[i].id;
			var customEvent = customEvents[i].value;
	    	var richMediaEventGraphAnalyticBoardId = customEventId+'AnalyticsBoard';
	    	var richMediaEventGraphTotalId = customEventId+'Total';
	    	var richMediaEventGraphPieChartId = customEventId+'PieChart';
	    	var richMediaEventGraphDetailsId = customEventId+'Details';
	    	
	    	var divContent = '<div name="richMediaEventGraphAnalyticsBoard" class="analytics-boards" id="'+richMediaEventGraphAnalyticBoardId+'" style="display: block;">'
			+'<div class="board-container" style="min-height:335px;"><div class="board has-chart" style="padding: 0px;"><div class="board-outer">'
			+'<h2 class="account-color board-inner"><div style="display: block;" class="richMediaEventGraphTotal" id="'+richMediaEventGraphTotalId+'"></div></h2>'
			+'</div><div class="chart-area"><div class="rich-chart-area" id="'+richMediaEventGraphPieChartId+'"></div>'
			+'</div><div class="filter_data board-filter"><article><div><div style="display: block;" class="richMediaEventGraphDetail" id="'+richMediaEventGraphDetailsId+'"></div>'
			+'</div></article></div></div></div></div>';
			
	    	$('#richMediaEventGraphOuterDiv').append(divContent);
	      // Prepare the data
	      var data = google.visualization.arrayToDataTable(eval("["+richMediaGraphTable+"]"));
	      var formatter = new google.visualization.NumberFormat({pattern:'#,###.##%'});
	      formatter.format(data, 5); 
	      
	      var customEventView = new google.visualization.DataView(data);
	      customEventView.setRows(customEventView.getFilteredRows([{column: 3, value:customEvent}]));
	
	      var richMediaEventGraphDetails = new google.visualization.ChartWrapper({
	          'chartType': 'Table',
	          'containerId': richMediaEventGraphDetailsId,
	          'options': {
	              'width': '100%'
	          },
	          'dataTable':google.visualization.data.group(customEventView, [selectedMetrics], 
	                    [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
	      }); 
	      richMediaEventGraphDetails.draw();
	
	      // Create a dashboard
	   var richMediaEventGraphTotal = new google.visualization.ChartWrapper({
	          'chartType': 'Table',
	          'containerId': richMediaEventGraphTotalId,
	          'options': {
	              'width': '100%'
	          },
	          'dataTable':google.visualization.data.group(customEventView, [i+5], 
	                    [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
	      }); 
	      richMediaEventGraphTotal.draw();
	      
	   // Define a Pie chart
	   var richMediaEventGraphPieChart = new google.visualization.ChartWrapper({
	        'chartType': 'PieChart',
	        'containerId': richMediaEventGraphPieChartId,
	        'options': {
	          'legend': 'none',
	          'title': '',
	          'pieSliceText': 'label',
	          'pieHole': '0.6',
	          'chartArea':{'width':'97%','height':'97%'},
	          'colors': ['#23adde', '#86af4e', '#1fbbae'],
	        },
	        
	        'dataTable' : google.visualization.data.group(customEventView, [selectedMetrics],
	                  [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
	        
	      });
	      richMediaEventGraphPieChart.draw();
		}
	}
	else {
		$('#selectRichMediaEventGraphMetricsDiv').css('display','none');
		$('#richMediaEventGraphLoader').css('display','none');
		$('#emptyDataTableMsgId').css('display','block');
	}
  
}
 
//scroll header div 
var fixmeTop = $('.fixme').offset().top;
$(window).scroll(function() {
    var currentScroll = $(window).scrollTop();
    if (currentScroll >= fixmeTop) {
        $('.fixme').css({
            position: 'fixed',
            top: '0',
            left: '0'
        });
    } else {
        $('.fixme').css({
            position: 'static'
        });
    }
}); 

function downLoadAdvertiserReport() {
	 try{
		 location.href="/advertiserReportObject.lin?campaignId="+campaignId+"&accountName="+accountName+"&campaignName="+campaignName;
	 }catch(exception){
		 
	 }
}



//$( document ).ready( handler )
/*$(document).ready(function() {
	alert("hi")
	$("#myModal").modal({
	    show: false
	});
});*/

function openSettingModel() {
	var $modal = $('#myModal').modal({
		show:false
	}).css({
	    'width': '85%'
	});
	$("#myModalLabel").html("chartTitle");
    $modal.modal('show');
//	 $( "#dialog-form" ).dialog( "open" );
}

function printSummary() {
	
	var dataHTML = $('#headerDiv').html();
	var printWindow = window.open('', 'Print - Summary', 'height=400,width=600');
	printWindow.document.write('<html><head><title>Summary</title>');
    /*//Stylesheet
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/bootstrap-select.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/theme.css" />');
	
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/fullcalendar.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/TableTools.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/bootstrap-wysihtml5.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/wysiwyg-color.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/bootstrap.min.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/bootstrap-responsive.min.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/font-awesome.min.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/cus-icons.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/jarvis-widgets.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/DT_bootstrap.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/responsive-tables.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/uniform.default.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/theme.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/theme-responsive.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/themes/default.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/full-width.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://fonts.googleapis.com/css?family=Lato:300,400,700" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/style.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/animate.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/daterangepicker.css" />');
	printWindow.document.write('<link type="text/css" rel="stylesheet" href="http://localhost:8888/css/global.css" />');
	 //Stylesheet
*/	printWindow.document.write('<link rel="stylesheet" href="http://localhost:8888/css/abc.css" />');
	printWindow.document.write('</head><body >');
	printWindow.document.write('<h2><center>No data found for the selected campaign</center></h2>');
	printWindow.document.write('<div class="abc">mdjjjjjjjjjjj</div>');
	printWindow.document.write(dataHTML);
	printWindow.document.write('</body></html>');
	printWindow.document.write('</body></html>');
	printWindow.print();
	printWindow.close();
    return true;
}

/*$(document).ready(function(){
	$('#benchmark').tagsinput({
		tagClass: function(item) {
		switch (item.kpi) {
		  case '1'   : return 'label label-info';
		  case '2'  : return 'badge badge-inverse';
		  case '3': return 'label label-success';
		  default : return 'label label-info';
		}
	  },
		itemValue: 'value',
		itemText: 'text'
	});
	
	//$(".bootstrap-tagsinput > input").attr('disabled',true);
	});*/

/*function addValue(){
	var obj = { value: null,text:null,kpi:null,kpival:null};
	obj.value = $("#kpival").val() + "_" + $("#kpi").val();
	obj.text = $("#kpival").val() + " " + $("#kpi option:selected").text();
	obj.kpi = $("#kpi").val();
	obj.kpival = $("#kpival").val();
	$('#benchmark').tagsinput('add',obj );
}

function showValue(){
		console.log($('#benchmark').tagsinput('items'));
}*/
