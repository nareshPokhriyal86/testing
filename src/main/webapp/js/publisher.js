google.load('visualization', '1', {'packages': ['geochart']});		 
google.load('visualization', '1.0', {'packages':['controls']});
google.load("visualization", "1", {packages:["corechart"]});

var lastPopUpId = 0;
var lastTrafferPopUpId = 0;
var defaultExecutionFlag = 0;
var allChannelName = '';
var oldChannelName = '';
var creativeTypeRichMedia = 'rich media';
var timePeriod='This Week';
var channelArr = new Array();
var CpPreviousRowId = 'channelPerformance_0' ; 
var selectedPublisher = '';
var selectedPublisherId = '';
var STRPreviousRowId = 'sellThroughRate_1' ;
var tabName = summaryTab;

var sellThroughData = [];
var aggregateSellThroughRate;
var sellThroughDataLowerDate = "01-01-1970";

var optimizingFlag = 0;
var optimizingFlagCampain = 0;
var publisherSummaryCurrentData;
var publisherSummaryCompareData;
var publisherSummaryMTDData;

var performanceByPropertyCurrentData;
var performanceByPropertyCompareData = '';
var performanceByPropertyCurrentDataBySiteName = '';
var performanceByPropertyCompareDataBySiteName = '';

var loadAjaxCounter = 0;
var loadAjaxCounter1 = 0;
var loadAjaxCounter2 = 0;
var arrLoadAjaxCounter=[];
var oldStartDate ;
var oldEndDate ;
var oldPublisher;
var advertisers ='' ;
var agencies = '';
var leftFilterStatus;
var isTrendDefault = false;
var SelectedPropertyPublisher = '';
var SelectedDFP_propertyPublisher = '';
var campaignTotalCurrentData;
var campaignTotalCompareData;
var campaignTotalMTDData;
var campaignTotalCurrentDataPerformanceMetrics;
var campaignTotalCompareDataPerformanceMetrics;
var campaignTotalMTDDataPerformanceMetrics;
var campaigndeliveryIndicatorData;

var invRevOldStartDate;
var invRevOldEndDate;
var performanceByPropertyOldStartDate;
var performanceByPropertyOldEndDate;
var oldperformanceByPropertypublisher;
var performanceSummaryHeaderFilteredData = new Array();

var performerFilteredData = new Array();
//var performerFilteredDeliveryIndicatorData = new Array();

var nonPerformerFilteredData = new Array();
var nonPerformerFilteredDeliveryIndicatorData = new Array();

var mostActiveFilteredData = new Array();
//var mostActiveFilteredDeliveryIndicatorData = new Array();

var topGainersFilteredData = new Array();
//var topGainersFilteredDeliveryIndicatorData = new Array();

var topLosersFilteredData = new Array();
//var topLosersFilteredDeliveryIndicatorData = new Array();

var performanceMetricsFilteredData = new Array();
//var performanceMetricsFilteredDeliveryIndicatorData = new Array();

var countOfEmptyDataTables = 0;
var countOfEmptyGeoGraph = 0;


/*$(document).ready(function(){

	angular.bootstrap($("#lineChartDiv"),['lineChartPublisherApp']);
});*/

function countEmptyDataTables() {
	 countOfEmptyDataTables++;
	 if(countOfEmptyDataTables == 8) {
		 $('#emptyDataTableMsgId').css('display','block');
	 }
}

function countEmptyGeoGraph() {
	countOfEmptyGeoGraph++;
	if(countOfEmptyGeoGraph == 2) {
		$('#topGainerArticle').attr('class','span12');
	 }
}


function tabClickValue(obj){
	try{
		if ($(obj).text() == trendsTab){
			tabName = trendsTab;
			isTrendDefault =false;
			 $(".well").css({'display':'inline'});
			 $(".slide-out-div").css({'display':'inline'});
			 $("#agency_dropdown_advertiser").css({'display':'none'});
			 $("#advertiser_dropdown_advertiser").css({'display':'none'});
			 $("#propertyDropDownPublisher").css({'display':'none'});
			 $("#filter_Channel").css({'display':'inline'});
			 $("#publisher_outer").css({'min-height':'139px'});
			 $(".agency_second_filter_publisher").css({'display':'none'});
			 $(".advertiser_second_filter_publisher").css({'display':'none'});
			 $("#order_dropdown_text_publisher").css({'display':'none'});
			 $("#line_dropdown_text_publisher").css({'display':'none'});
			 $("#order_dropdown_publisher").css({'display':'none'});
			 
			 $("#reportrange").css({'display':'block'});
			 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
			 $("#line_itemName_publisher").css({'display':'none'});
			 $('#myTab1 li:eq(1)').css({'display':'inline'});
			 $('#myTab1 li:eq(1) a').tab('show');
			 
		 }
		else if ($(obj).text() == summaryTab){
			 tabName = summaryTab;
			 isTrendDefault =false;
			 $(".well").css({'display':'inline'});
			 $(".slide-out-div").css({'display':'inline'});
			 $("#agency_dropdown_advertiser").css({'display':'none'});
			 $("#advertiser_dropdown_advertiser").css({'display':'none'});
			 $("#propertyDropDownPublisher").css({'display':'none'});
			 $("#filter_Channel").css({'display':'inline'});
			 $("#publisher_outer").css({'min-height':'139px'});
			 $(".agency_second_filter_publisher").css({'display':'none'});
			 $(".advertiser_second_filter_publisher").css({'display':'none'});
			 $("#order_dropdown_text_publisher").css({'display':'none'});
			 $("#line_dropdown_text_publisher").css({'display':'none'});
			 $("#reportrange").css({'display':'block'});
			 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
			 $("#order_dropdown_publisher").css({'display':'none'});
			 $("#line_itemName_publisher").css({'display':'none'});
			 $('#myTab1 li:eq(0)').css({'display':'inline'});
			 $('#myTab1 li:eq(0) a').tab('show');
		 }
		else if ($(obj).text() == diagnosticTab){
			console.log("diagonstic tab::"+diagnosticTab);
			tabName = diagnosticTab;
			isTrendDefault =false;
			 $(".well").css({'display':'inline'});
			 $(".slide-out-div").css({'display':'inline'});
			 $("#agency_dropdown_advertiser").css({'display':'none'});
			 $("#advertiser_dropdown_advertiser").css({'display':'none'});
			 $("#propertyDropDownPublisher").css({'display':'none'});
			 $("#filter_Channel").css({'display':'inline'});
			 $("#publisher_outer").css({'min-height':'139px'});
			 $(".agency_second_filter_publisher").css({'display':'none'});
			 $(".advertiser_second_filter_publisher").css({'display':'none'});
			 $("#order_dropdown_text_publisher").css({'display':'none'});
			 $("#line_dropdown_text_publisher").css({'display':'none'});
			 $("#reportrange").css({'display':'block'});
			 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
			 $("#order_dropdown_publisher").css({'display':'none'});
			 $("#line_itemName_publisher").css({'display':'none'});
			 $('#myTab1 li:eq(2)').css({'display':'inline'});
			 $('#myTab1 li:eq(2) a').tab('show');
			
		 }
		else if ($(obj).text() == traffickingTab){
			console.log("traffiking tab::"+traffickingTab);
			tabName = traffickingTab;
			isTrendDefault =false;		   
			 $(".well").css({'display':'inline'});
			 $(".slide-out-div").css({'display':'none'});
			 $("#agency_dropdown_advertiser").css({'display':'none'});
			 $("#advertiser_dropdown_advertiser").css({'display':'none'});
			 $("#propertyDropDownPublisher").css({'display':'none'});
			 $("#filter_Channel").css({'display':'inline'});
			 $("#publisher_outer").css({'min-height':'139px'});
			 $("#reportrange").css({'display':'block'});
			 $("#order_dropdown_text_publisher").css({'display':'none'});
			 $("#line_dropdown_text_publisher").css({'display':'none'});
			 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
			 $("#order_dropdown_publisher").css({'display':'none'});
			 $("#line_itemName_publisher").css({'display':'none'});
			 setTimeout(function(){$("#btn-today").click()},1);
			 $('#myTab1 li:eq(3)').css({'display':'inline'});
			 $('#myTab1 li:eq(3) a').tab('show');
			
		 }
	}catch(err){		
		console.log("tabClickValue Error:"+err);
	}
	
   
}

if (startDate == undefined){
	  var startDate ;
	  var endDate;
	  var oldStartDate ;
	  var oldEndDate ;
	  var oldPublisher;
	  var compareStartDate;
	  var compareEndDate ;
	  var channels;
	  var oldChannelName;
	  
}

if (channels == undefined){
	var allChannelName;
	var selectedPublisher;
}

var width;
var graphWidth;
var height;
var modalheaderWidth;
var modalheaderHeight;
var piechartWidth;
var geoChartWidth;
var loadingout=0;
$(window).load(function(){
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
     piechartWidth = parseInt((width-290)/4) ;
     geoChartWidth =  parseInt(width-250) ;
     $(".modal").css({'width':modalHeaderWidthdiv,'left':marginModalLeft,'right':marginModalRight});
     $(".modal-body").css({'max-height':modalheaderHeight});
     loadFilterData();
 });


function loadAllDataPublisher(){
	addLoaders();
 	loadingout++;
 	advertisers = advertisername;
	agencies = agencyname;
	getLeftFilterStatus();
	
	
	if(!(oldStartDate == startDate && oldEndDate == endDate ))
	{ 
		//alert("in if")
	//	$("#geomap4").empty();
		oldStartDate = startDate;
		oldEndDate = endDate;
		oldPublisher = selectedPublisher;
		oldperformanceByPropertypublisher = selectedPublisher;
		oldChannelName = allChannelName;
	//	optimizingFlag = 0;
		optimizingFlagCampain = 0;
	  
	}
	else
	{
	}

////////////////////////////////////////////	
	if(!(invRevOldStartDate == startDate && invRevOldEndDate == endDate && oldPublisher == selectedPublisher && defaultExecutionFlag ==0))
	{ 
		
		$("#geomap4").empty();
		invRevOldStartDate = startDate;
		invRevOldEndDate = endDate;
		oldPublisher = selectedPublisher;
		optimizingFlag = 0;
		
	   $("#just-table tbody tr").remove();
	   $("#dtable1 tbody tr").remove();
	   $("#sales_Channel").css({'display':'block'});
	   $("#perfromace_property").css({'display':'block'});
	   $("#sell_through").css({'display':'block'});
		
		getPublisherSummaryCurrent();
		getPublisherSummaryCompare();
		getPublisherSummaryMTD();
		  
		getPerformanceByPropertyCurrent();
		getPerformanceByPropertyCompare();

	}
	else
	{
		//alert("coming inside else");
		getOptimizedInventoryRevenueHeaderData();
		getOptimizedChannelPerformance();
	}
////////////////////////////////////////////	
	
	
////////////////////////////////////////////
	/*if(!(performanceByPropertyOldStartDate == startDate && performanceByPropertyOldEndDate == endDate && oldperformanceByPropertypublisher == selectedPublisher && oldChannelName == allChannelName))
	{ 
		//alert("coming inside if");
		alert('old Channels '+ oldChannelName);
		alert('all Channel Name'+ allChannelName);
		$("#geomap4").empty();
		oldChannelName = allChannelName;
		performanceByPropertyOldStartDate = startDate;
		performanceByPropertyOldEndDate = endDate;
		oldperformanceByPropertypublisher = selectedPublisher;
		optimizingFlag = 0;
		
	   $("#just-table tbody tr").remove();
	   $("#dtable1 tbody tr").remove();
	   $("#sales_Channel").css({'display':'block'});
	   $("#perfromace_property").css({'display':'block'});
	   $("#sell_through").css({'display':'block'});
			
	    //getOptimizedChannelPerformance();
		getPerformanceByPropertyCurrent();
		getPerformanceByPropertyCompare();
		

	}*/
	
////////////////////////////////////////////	
	
	if(isTrendDefault){
		getAdvertiserTrendAnalysisHeaderData();
		getActualAdvertiserData();
		actualLineGenerationCampaign();
	}
	
	getSellThroughData();
	getActualPublisherData();
	actualLineGeneration();
	var scope_lineChartPublisherDivScope=angular.element(document.getElementById("lineChartPublisherDiv")).scope();
	scope_lineChartPublisherDivScope.initdata();
	
	var scope_lineChartPublisherDivScope_printView=angular.element(document.getElementById("lineChartPublisherDiv_printView")).scope();
	scope_lineChartPublisherDivScope_printView.initdata();

}


function loadTrendsAnalysisData(){
	var publisherName=$("#publisher_allocation_val").text();		
	var channelName=$("#channel_trends_val").text();
	if(channelName != undefined && channelName != 'All'){
		loadChannelByName(channelName);
	}else{
		loadChannelByPublisher(publisherName);
	}
}

function selectPublisher(publisherId){		
	var val=$("#"+publisherId).text();		
	$("#publisher_val").html(val);		
	loadPublisherHeader(val);
}

function selectPublisherTrends(publisherId){		
	var val=$("#"+publisherId).text();		
	$("#publisher_trends_val").html(val);		
	loadChannelByPublisher(val);
}

function selectPublisherAllocation(publisherId){		
	var val=$("#"+publisherId).text();		
	$("#publisher_allocation_val").html(val);		
	//loadPublisherAllocationHeader(val);
}

function selectChannel(channelId){
 var val=$("#"+channelId).text();		
 $("#channel_trends_val").html(val);
}



$(document).ready(function(){
	$(".col").blur(function(){	
	var Value1 =	parseInt($('#col1').val());
	var Value2 =	parseInt($('#col2').val());
	var Value3 =	parseInt($('#col3').val());
	var Value4 =	parseInt($('#col4').val());	
	var total = Value1 + Value2 + Value3 + Value4
	$('#rev_budget').html(total) ;
	var ecpm1= parseInt($('#ecpm1').html().replace('$',''));
	var ecpm2= parseInt($('#ecpm2').html().replace('$',''));
	var ecpm3= parseInt($('#ecpm3').html().replace('$',''));
	var ecpm4= parseInt($('#ecpm4').html().replace('$',''));
	
	var rev_book_imp1 = (Value1 * 1000 )/ecpm1

	var rev_book_imp2 = (Value2 * 1000 )/ecpm2
	var rev_book_imp3 = (Value3 * 1000 )/ecpm3
	var rev_book_imp4 = (Value4 * 1000 )/ecpm4
	//alert(rev_book_imp1);
	$('#rb1').html(rev_book_imp1);
	$('#rb2').html(rev_book_imp2);
	$('#rb3').html(rev_book_imp3);
	$('#rb4').html(rev_book_imp4);
	
	var rbvalue1 =	parseInt($('#rb1').html());
	var rbvalue2 =	parseInt($('#rb2').html());
	var rbvalue3 =	parseInt($('#rb3').html());
	var rbvalue4 =	parseInt($('#rb4').html());
	
	var rbtotal=rbvalue1+rbvalue2+rbvalue3+rbvalue4
	$('#rb5').html(rbtotal) ;
	
	var rev_rec_val1=parseFloat($('#rev_reco1').html().replace('$',''));
	var rev_rec_val2=parseFloat($('#rev_reco2').html().replace('$',''));
	var rev_rec_val3=parseFloat($('#rev_reco3').html().replace('$',''));
	var rev_rec_val4=parseFloat($('#rev_reco4').html().replace('$',''));
	
	var rev_rec_cal1=Value1-rev_rec_val1;
	var rev_rec_cal2=Value2-rev_rec_val2;
	var rev_rec_cal3=Value3-rev_rec_val3;
	var rev_rec_cal4=Value4-rev_rec_val4;
	
	var rev_rec_cal_forma1 = (rev_rec_cal1).toFixed(2) ;
	var rev_rec_cal_forma2 = (rev_rec_cal2).toFixed(2) ;
	var rev_rec_cal_forma3 = (rev_rec_cal3).toFixed(2) ;
	var rev_rec_cal_forma4 = (rev_rec_cal4).toFixed(2) ;
	
	
	var rev_rec_cal_formated1 = "$"+rev_rec_cal_forma1 ;
	var rev_rec_cal_formated2 = "$"+rev_rec_cal_forma2 ;
	var rev_rec_cal_formated3 = "$"+rev_rec_cal_forma3 ;
	var rev_rec_cal_formated4 = "$"+rev_rec_cal_forma4 ;
	var rev_rec_col_total=rev_rec_cal1+rev_rec_cal2+rev_rec_cal3+rev_rec_cal4;
	
	
	
	var rev_rec_col_total1 = (rev_rec_col_total).toFixed(2)
	var rev_rec_col_total_formated = "$"+rev_rec_col_total1;
	$('#rr1').html(rev_rec_cal_formated1);
	$('#rr2').html(rev_rec_cal_formated2);
	$('#rr3').html(rev_rec_cal_formated3);
	$('#rr4').html(rev_rec_cal_formated4);
	$('#rr5').html(rev_rec_col_total_formated);
	
	var budget_allocated_val = 6000 - total
	$('#budget_allocated').html(budget_allocated_val);
	
	if (rev_rec_col_total > 5664.24){
	bootbox.alert("Revised budget exceeds balance remaining.", function() {
				 
			}).css({'left': '2%','margin-left':'0px'});
				
	}
	
	});
	
	
	
	});
	
 $(document).ready(function(){
	$("#sub").click(function(){
	
	var Value1 =	parseInt($('#col1').val());
	var Value2 =	parseInt($('#col2').val());
	var Value3 =	parseInt($('#col3').val());
	var Value4 =	parseInt($('#col4').val());
	var total = Value1 + Value2 + Value3 + Value4
	var budget_allocated_val = 6000 - total
	if(budget_allocated_val>0 || budget_allocated_val<0 )	{
	bootbox.confirm("The total budget has changed. Are you sure you want to apply the changes?", function(result) {
				 if (result == true){
					toastr.info("Data saved");
				 }
				 else{
					toastr.info("Discarded by user");
				 }
				 
			}).css({'left': '2%','margin-left':'0px'});
	}	
	
	});
	});
	


  
  $(document).ready(function(){
	$('#Inventoryclose').click(function(){
		$('#myTab li:eq(0) a').tab('show');
		$('#myTab li:eq(2)').css({'display':'none'});
		});
	});
  $(document).ready(function(){
	$('#reallocationclose').click(function(){
		$('#myTab li:eq(0) a').tab('show');
		$('#myTab li:eq(6)').css({'display':'none'});
		});
	});
  

  function selectTimeInterval(timeId){		
		var val=$("#"+timeId).text();		
		$("#publisher_time_data_div").html(val);
		$("#publisher_time_data_trends_div").html(val);
		$("#publisher_time_data_reallocation_div").html(val);
		timePeriod=val;
 }
 
  function publisherRelocationTable(){
		 $.ajax({
		       type : "POST",
		       url : "/publisherRelocationData.lin",
		       cache: false,
		       dataType: 'json',
		       success: function (data) {	  
	           var dataList=data['list'];
	           var lastRow = dataList.length-1;
	    	   for(key in dataList){  
	    	   var dtoObject = dataList[key];
	    	    var row ="";
	    	    var parameters="'"+dtoObject.callPriority+"','status'";
	    	    row= "<tr>"+
					"<td>" +
					"<span class='badge badge-success'>"+dtoObject.callPriority+"</span>";
	    	        if(key==0){ 
						row+="<img src='img/DT/sort_desc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
					}else if(key==lastRow){
						row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
					}
	    	        else{
						row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')><img src='img/DT/sort_desc.png' width='28' height='38' style='margin-left:-9px;margin-left:-40px; margin-top: 4px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
					}
					row+="</td>"+
					"<td>"+dtoObject.networkOrRTB+"</td>"+
					"<td>"+dtoObject.ecpm+"</td>"+
					"<td>"+dtoObject.fillRate+"</td>"+
					"<td>"+dtoObject.impressions+"</td>"+			
					"<td>"+dtoObject.clicks+"</td>"+
					"<td>"+dtoObject.revenue+"</td>"+
					"<td><input type='text' id ='colI' class='col' value="+dtoObject.floorCPM+" style='width: 50px;'/></td>"+
					"<td><input type='text' id ='colII' class='col' value="+dtoObject.floorCPC+" style='width: 50px;'/></td>"+
					"</tr>";
	    	      $("#ttr tbody").append(row);
	    	   }
		       },
		       error: function(jqXHR, exception) {
		        }
		  });
	 }  
	 
	 function getPublisherHeaderDataById(id,value){
		 $.ajax({
		       type : "POST",
		       url : "/getPublisherHeaderData.lin?id="+id,
		       cache: false,
		       dataType: 'json',
		       success: function (data) {
		           $.each(data, function(index, element) {	 
		        	  var mapObj=data['map'];
		        		 $("#relocationRequest").html(mapObj['relocationRequest']);
		        		 $("#relocationDelivered").html(mapObj['relocationDelivered']);
		        		 $("#relocationFillRate").html(mapObj['relocationFillRate']);
		        		 $("#relocationClicks").html(mapObj['relocationClicks']);
		        		 $("#relocationRevenue").html(mapObj['relocationRevenue']);
		        		 $("#relocationCTRPercent").html(mapObj['relocationCTRPercent']);
		        		 $("#relocationECPM").html(mapObj['relocationECPM']);
		        		 $("#relocationRPM").html(mapObj['relocationRPM']);
		        		 $("#relocationSelectedPublisher").html(value);
		           });
		       },
		       
		       error: function(jqXHR, exception) {
		        }
		  });
	 }
	 
	function getPublisherHeaderData(){
		 $.ajax({
		       type : "POST",
		       url : "/getPublisherHeaderData.lin",
		       cache: false,
		       dataType: 'json',
		       success: function (data) {
		           $.each(data, function(index, element) {	 
		        	  var mapObj=data['map'];
		        		 $("#relocationRequest").html(mapObj['relocationRequest']);
		        		 $("#relocationDelivered").html(mapObj['relocationDelivered']);
		        		 $("#relocationFillRate").html(mapObj['relocationFillRate']);
		        		 $("#relocationClicks").html(mapObj['relocationClicks']);
		        		 $("#relocationRevenue").html(mapObj['relocationRevenue']);
		        		 $("#relocationCTRPercent").html(mapObj['relocationCTRPercent']);
		        		 $("#relocationECPM").html(mapObj['relocationECPM']);
		        		 $("#relocationRPM").html(mapObj['relocationRPM']);
		        		 //$("#relocationSelectedPublisher").html(value);
		           });
		       },
		       
		       error: function(jqXHR, exception) {
		    	   
		       }
		  });
	 }
	 
		 
	 function createRelocationDataTableRow(key,lastRow,callPriority,networkOrRTB,ecpm,fillRate,impressions,clicks,revenue,floorCPM,floorCPC){
		 var row ="";
		    row= "<tr>"+
					"<td>" +
					"<span class='badge badge-success'>"+dtoObject.callPriority+"</span>";
		        if(key==0){ 
						row+="<img src='img/DT/sort_desc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+callPriority+"','status')>";
					}else if(key==lastRow){
						row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+callPriority+"','status')>";
					}
		        else{
						row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;cursor:pointer;' onclick=changeCallPriority("+callPriority+",'status')><img src='img/DT/sort_desc.png' width='28' height='38' style='margin-left:-9px;margin-left:-40px; margin-top: 4px;cursor:pointer;' onclick=changeCallPriority('"+callPriority+"','status')>";
					}
					row+="</td>"+
					"<td>"+networkOrRTB+"</td>"+
					"<td>"+ecpm+"</td>"+
					"<td>"+fillRate+"</td>"+
					"<td>"+impressions+"</td>"+			
					"<td>"+clicks+"</td>"+
					"<td>"+revenue+"</td>"+
					"<td><input type='text' id ='colI' class='col' value="+floorCPM+" style='width: 50px;'/></td>"+
					"<td><input type='text' id ='colII' class='col' value="+floorCPC+" style='width: 50px;'/></td>"+
					"</tr>";
		      $("#ttr tbody").append(row);
	 }
	 
	 function relocationDataTableSaveOrUpdate(){
		 $.ajax({
		       type : "POST",
		       url : "/relocationDataTableSaveOrUpdate.lin",
		       cache: false,
		       dataType: 'json',
		       success: function (data) {
		            $.each(data, function(index, element) {	 
		        	   $('#ttr > tbody').empty();
		               var dataList=data['list'];
		               var lastRow = dataList.length-1;
		        	   for(key in dataList){  
		        	   var dtoObject = dataList[key];
		        	   
		        	   var row ="";
		         	    row= "<tr>"+
		       				"<td>" +
		       				"<span class='badge badge-success'>"+dtoObject.callPriority+"</span>";
		         	        if(key==0){ 
		       					row+="<img src='img/DT/sort_desc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
		       				}else if(key==lastRow){
		       					row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
		       				}
		         	        else{
		       					row+="<img src='img/DT/sort_asc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;cursor:pointer;' onclick=changeCallPriority("+dtoObject.callPriority+",'status')><img src='img/DT/sort_desc.png' width='28' height='38' style='margin-left:-9px;margin-left:-40px; margin-top: 4px;cursor:pointer;' onclick=changeCallPriority('"+dtoObject.callPriority+"','status')>";
		       				}
		       				row+="</td>"+
		       				"<td>"+dtoObject.networkOrRTB+"</td>"+
		       				"<td>"+dtoObject.ecpm+"</td>"+
		       				"<td>"+dtoObject.fillRate+"</td>"+
		       				"<td>"+dtoObject.impressions+"</td>"+			
		       				"<td>"+dtoObject.clicks+"</td>"+
		       				"<td>"+dtoObject.revenue+"</td>"+
		       				"<td><input type='text' id ='colI' class='col' value="+dtoObject.floorCPM+" style='width: 50px;'/></td>"+
		       				"<td><input type='text' id ='colII' class='col' value="+dtoObject.floorCPC+" style='width: 50px;'/></td>"+
		       				"</tr>";
		         	      $("#ttr tbody").append(row);
		        	   }
		           });
		       },
		       
		       error: function(jqXHR, exception) {	      
		        }
		  });
	 }
	 
	 function getAllPublisherName(){
		 
		 $.ajax({
		       type : "POST",
		       url : "/getAllPublisherName.lin",
		       cache: false,
		       dataType: 'json',
		       success: function (data) { 
		    	   var ybj=data['commonDtoList'];
		    	   
		           $.each(data, function(index, element) {	 
		        	  var mapObj=data['commonDtoList'];
		        	  var row ="";
		        	  for(key in mapObj){
		        		  var dataObj = mapObj[key];
		        		  
		        		  
		        		  var as= dataObj.value
		        		  
		        		  row += "<li><a href='#' onclick=javascript:getPublisherHeaderDataById('"+dataObj.id+"','"+as+"') style='color:black;'>"+as+"</a></li>"
		        	  }	        		  
		        	  
		        	  $("#relocationUlDropDown").html(row);
		        	  /*$("#relocationSelectedPublisher").html(value);*/
		           });
		       },
		       error: function(jqXHR, exception) {	          
		        }
		  });
	 }



///////////////////////////////////////////////////////////////////////
// Start: Channel Performance Table data pupulation 
// Description :
// Author: Dheeraj Kumar

 function getOptimizedChannelPerformance() {
			
	var channelPublisherDropDownHTML = '';
	
	   $("#just-table tbody tr").remove();
	   $("#dtable1 tbody tr").remove();
	   $("#sales_Channel").css({'display':'block'});
	   $("#perfromace_property").css({'display':'block'});
	   $("#sell_through").css({'display':'block'});
	   $("#selectChannelPublisher").html(channelPublisherDropDownHTML);
	  
	   sbStr = "[['Sales Channel',   'Impressions Delivered']";  //sbStr started for creation of Pie Chart;
	   
	   var data = new google.visualization.DataTable();
	   data.addColumn('string', 'Sales Channel');
	   data.addColumn('number', 'Impressions Delivered');
	   var rows="[";
	   
	if(publisherSummaryCurrentData != undefined && publisherSummaryCurrentData.length > 0){
		
	  var selectedChannels = allChannelName.split(",");
	  var arrayLength = selectedChannels.length;
	  
	  var totalECPMLast = 0.0;
	  var totalPayoutsLast = 0.0;
	  var totalEmpDeliveredLast = 0;
	  
	  var totalECPM = 0.0;
	  var totalEmpDelivered = 0;
	  var totalClicks = 0;
	  var totalCTR = 0.0;
	  var totalPayouts = 0.0;	  
	  var totalChange = 0.0;
	  var totalChangePercentage = 0.0;
	  
	  var flg = 0;
	  
	  for(var i = 0; i < arrayLength; i++){
		  //flg ++;
		  for (var j=0;  j < publisherSummaryCurrentData.length;j++) {
			var matchflag = 0; 			  
			var dtoObjectCurrent = publisherSummaryCurrentData[j];

			if (publisherSummaryCompareData.length !=0){				
			
			  for (var k=0; k < publisherSummaryCompareData.length ; k++){	
				  var dtoObjectCompare = publisherSummaryCompareData[k];
				  
				  if(selectedChannels[i] == dtoObjectCurrent.channelName && 
						  dtoObjectCurrent.channelName == dtoObjectCompare.channelName){ 
					  
					  channelPublisherDropDownHTML = channelPublisherDropDownHTML + "<option value='"+dtoObjectCurrent.channelName+"'>"+dtoObjectCurrent.channelName+"</option>";
				     
				        flg ++;
				  		var CHG = 0;
						var percentageCHG = 0;
						matchflag = 1;
					    //data adding for creation of Pie Chart to sbStr ;
						sbStr = sbStr+",[";
						sbStr = sbStr +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
						sbStr = sbStr+"]";
						
						rows = rows+",[";
						rows = rows +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
						rows = rows+"]";
		
					    CHG = dtoObjectCurrent.eCPM - dtoObjectCompare.eCPM;
					   
					    if(dtoObjectCompare.eCPM == 0){
					 	   percentageCHG = 0.0;
					    }else{
						   percentageCHG = (CHG / dtoObjectCompare.eCPM) * 100;
					    }
						  
                        var id="channelPerformance_"+i;
                        var empty ='';
						var row = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
						"<td onclick=javascript:showOptimizedChannelPerformancePopup('"+id+"','"+empty+"') rel='popover'><img src='../img/search.png' /></td>"+
						"<td id='"+id+"_title' >"
						+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
						
						var row_printView = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
						"<td id='"+id+"_title' >"
						+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
		  			          
			 	 		 if(CHG == 0 || percentageCHG == 0){

							row = row+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
							  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
							  +"<span id='"+id+"_changePercent' >" 
							  +"</span></td>";
							
							row_printView = row_printView+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
							  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
							  +"<span id='"+id+"_changePercent' >" 
							  +"</span></td>";
							
		        			        
		        		 }else if(CHG < 0 || percentageCHG < 0){
							  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
							  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
							  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
							  +"%</span></td>";
							  
							  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
							  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
							  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
							  +"%</span></td>";
							  
				        			        
				         }else{
							  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
							  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
							  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
							  +"%</span></td>";
							  
							  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
							  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
							  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
							  +"%</span></td>";
				         }
			 	 		        		
						row = row+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
						+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
						+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
						+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
						+"</td></tr>";
						
						row_printView = row_printView+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
						+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
						+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
						+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
						+"</td></tr>";
				        		   
				        $("#just-table tbody").append(row);
				        $("#just-table-printView tbody").append(row_printView);
							  
							  
						  totalPayoutsLast = totalPayoutsLast + dtoObjectCompare.payOuts;
						  totalEmpDeliveredLast = totalEmpDeliveredLast + dtoObjectCompare.impressionsDelivered;
						  totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.impressionsDelivered;
						  totalClicks = totalClicks + dtoObjectCurrent.clicks;
						  totalPayouts = totalPayouts + dtoObjectCurrent.payOuts;

					  }else if(selectedChannels[i] == dtoObjectCurrent.channelName && publisherSummaryCompareData.length-1 == k && matchflag == 0){
						  channelPublisherDropDownHTML = channelPublisherDropDownHTML + "<option value='"+dtoObjectCurrent.channelName+"'>"+dtoObjectCurrent.channelName+"</option>";
						 // $("#selectChannelPublisher").html(channelPublisherDropDownHTML);
						  flg ++;
					  		var CHG = 0;
							var percentageCHG = 0.0;
							matchflag = 1
						    //data adding for creation of Pie Chart to sbStr ;
							sbStr = sbStr+",[";
							sbStr = sbStr +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
							sbStr = sbStr+"]";
							
							rows = rows+",[";
							rows = rows +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
							rows = rows+"]";

							CHG = dtoObjectCurrent.eCPM;
							percentageCHG = 0.0;
								  
	                        var id="channelPerformance_"+i;
							var row = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
							"<td onclick=javascript:showChannelOptimizedPerformancePopup('"+id+"') rel='popover'><img src='../img/search.png' /></td>"+
							"<td id='"+id+"_title' >"
							+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
			  			    
							var row_printView = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
							"<td id='"+id+"_title' >"
							+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
							
							
				 	 		 if(CHG == 0 || percentageCHG == 0){

								row = row+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
								  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
								  +"<span id='"+id+"_changePercent' >" 
								  +"</span></td>";
								
								row_printView = row_printView+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
								  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
								  +"<span id='"+id+"_changePercent' >" 
								  +"</span></td>";
								
			        		 }else if(CHG < 0 || percentageCHG < 0){
								  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
								  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
								  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
								  +"%</span></td>";
								  
								  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
								  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
								  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
								  +"%</span></td>";
					        			        
					         }else{
								  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
								  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
								  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
								  +"%</span></td>";
								  
								  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
								  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
								  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
								  +"%</span></td>";
					         }
				 	 		        		
							row = row+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
							+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
							+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
							+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
							+"</td></tr>";
							
							row_printView = row_printView+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
							+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
							+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
							+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
							+"</td></tr>";
							
					        		   
					        $("#just-table tbody").append(row);
					        $("#just-table-printView tbody").append(row_printView);
								  
							totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.impressionsDelivered;
							totalClicks = totalClicks + dtoObjectCurrent.clicks;
							totalPayouts = totalPayouts + dtoObjectCurrent.payOuts;
 
					  }
			  	}
			  
			  }else{
			  
				  if(selectedChannels[i] == dtoObjectCurrent.channelName){
					  flg ++;				  
				  channelPublisherDropDownHTML = channelPublisherDropDownHTML + "<option value='"+dtoObjectCurrent.channelName+"'>"+dtoObjectCurrent.channelName+"</option>";
				 // $("#selectChannelPublisher").html(channelPublisherDropDownHTML);
			  		var CHG = 0;
					var percentageCHG = 0.0;
					matchflag = 1;
				    //data adding for creation of Pie Chart to sbStr ;
					sbStr = sbStr+",[";
					sbStr = sbStr +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
					sbStr = sbStr+"]";
					
					rows = rows+",[";
					rows = rows +"'"+dtoObjectCurrent.channelName+"',"+dtoObjectCurrent.impressionsDelivered;
					rows = rows+"]";

					CHG = dtoObjectCurrent.eCPM;
					percentageCHG = 0.0;
						  
                    var id="channelPerformance_"+i;
                    var empty = '';
					var row = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
					"<td onclick=javascript:showOptimizedChannelPerformancePopup('"+id+"','"+empty+"') rel='popover'><img src='../img/search.png' /></td>"+
					"<td id='"+id+"_title' >"
					+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
	  			    
					var row_printView = "<tr id='"+id+"' style='cursor: hand; cursor: pointer;' class='changecolor'>" +
					"<td id='"+id+"_title' >"
					+dtoObjectCurrent.channelName+"</td><td id='"+id+"_eCPM'  style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.eCPM,2)+"</td>";
					
					
		 	 		 if(CHG == 0 || percentageCHG == 0){

						row = row+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
						  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
						  +"<span id='"+id+"_changePercent' >" 
						  +"</span></td>";
						
						row_printView = row_printView+"<td  style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"+  formatFloat(CHG,2)
						  +"</td><td  style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
						  +"<span id='"+id+"_changePercent' >" 
						  +"</span></td>";
	        			        
	        		 }else if(CHG < 0 || percentageCHG < 0){
						  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
						  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
						  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
						  +"%</span></td>";
						  
						  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(CHG,2)
						  +"</td><td  style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
						  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4) 
						  +"%</span></td>";
			        			        
			         }else{
						  row = row+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
						  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
						  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
						  +"%</span></td>";
						  
						  row_printView = row_printView+"<td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(CHG,2)
						  +"</td><td  style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
						  +"<span id='"+id+"_changePercent' >"+  formatFloat(percentageCHG,4)
						  +"%</span></td>";
			         }
		 	 		        		
					row = row+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
					+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
					+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
					+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
					+"</td></tr>";
					
					row_printView = row_printView+ "<td id='"+id+"_impressionDelivered'  class='' style='text-align: right;'>"+ formatInt(dtoObjectCurrent.impressionsDelivered)
					+"</td><td id='"+id+"_clicks'  class='' style='text-align: right;'>"+  formatInt(dtoObjectCurrent.clicks)
					+"</td><td id='"+id+"_ctr'  class='' style='text-align: right;'>"+  formatFloat(dtoObjectCurrent.CTR,4)+"%"
					+"</td><td id='"+id+"_payout'  class='' style='text-align: right;'>$"+  formatFloat(dtoObjectCurrent.payOuts,2) 
					+"</td></tr>";
			        		   
			        $("#just-table tbody").append(row);
			        $("#just-table-printView tbody").append(row_printView);
						  
						  

					  totalEmpDelivered = totalEmpDelivered + dtoObjectCurrent.impressionsDelivered;
					  totalClicks = totalClicks + dtoObjectCurrent.clicks;
					  totalPayouts = totalPayouts + dtoObjectCurrent.payOuts;				  
				  }
			  }
			  
			  
			  
			  
		  }
	  }

	  totalCTR =  (totalClicks / totalEmpDelivered) * 100;
	  totalECPM = (totalPayouts / totalEmpDelivered) * 1000;
	  totalECPMLast = (totalPayoutsLast / totalEmpDeliveredLast) * 1000;
	  
	  totalChange = totalECPM - totalECPMLast;
	  totalChangePercentage = (totalChange / totalECPMLast) * 100;

	  channelPublisherDropDownHTML = "<option value='All'>All</option>" + channelPublisherDropDownHTML;
	  $("#selectChannelPublisher").html(channelPublisherDropDownHTML);
	 if(flg > 0){
		 var firstRowId = $('#just-table tr').eq(1).attr('id');
		 updateSecondAndThird('-1');
		
		if(totalChange < 0 || totalChangePercentage < 0){
			var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(totalChangePercentage,4)+"%</td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
			var lastRow_printView ="<tr> <td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(totalChangePercentage,4)+"%</td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
		}
		else if(totalChange == 0 || totalChangePercentage == 0){
			var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: center;'> --- </td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td> <td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
			var lastRow_printView ="<tr> <td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: center;'> --- </td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td> <td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
		}
		else{
			var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(totalChangePercentage,4)+"%</td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
			var lastRow_printView ="<tr> <td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(totalECPM,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(totalChange,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(totalChangePercentage,4)+"%</td> <td style='text-align: right;'>"+formatInt(totalEmpDelivered)+"</td> <td style='text-align: right;'>"+formatInt(totalClicks)+"</td><td td style='text-align: right;'>"+formatFloat(totalCTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(totalPayouts,2)+"</td> </tr>";
		}
	
	   
		$("#just-table tbody").append(lastRow);
		$("#just-table-printView tbody").append(lastRow_printView);
	 }
	 /////////////
	 else{
		 row='<tr class="odd gradeX">'
		        +'<td colspan="9" style="color:red; text-align:center;">'
			        +'<div class="widget alert alert-info adjusted">'
			        +'<i class="cus-exclamation"></i>'
			        +'<strong>No records found for the selected filters</strong>'
			        +'</div>'
		        +'</td>'						      
		        +'</tr>';
		  
		$("#just-table tbody tr").remove();
       	$("#just-table tbody").append(row);
       	
		$("#sell_throughdata").css({'display':'none'});
		$("#performance_geomap").css({'display':'none'});
		
		
		$("#just-table-printView tbody tr").remove();
       	$("#just-table-printView tbody").append(row);
		$("#sell_throughdata_printView").css({'display':'none'});
		$("#sell_throughdata_donut_printView").css({'display':'none'});
		$("#performance_geomap_printView").css({'display':'none'});
		
	  row='<tr class="odd gradeX">'
	        +'<td colspan="8" style="color:red; text-align:center;">'
		        +'<div class="widget alert alert-info adjusted">'
		        +'<i class="cus-exclamation"></i>'
		        +'<strong>No records found for the selected filters</strong>'
		        +'</div>'
	        +'</td>'						      
	        +'</tr>';
	  
	 	$("#dtable1 tbody tr").remove();
		$("#dtable1 tbody").append(row);
  
	 }
	 ///////////
	 	$("#sales_Channel").css({'display':'none'});
		$("#perfromace_property").css({'display':'none'});
		$("#sell_through").css({'display':'none'});
	}
	else{
				 row='<tr class="odd gradeX">'
				        +'<td colspan="9" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
				  
				 $("#just-table tbody tr").remove();
		       $("#just-table tbody").append(row);
				$("#sell_throughdata").css({'display':'none'});
				$("#performance_geomap").css({'display':'none'});
				
				$("#sell_throughdata_printView").css({'display':'none'});
				$("#sell_throughdata_donut_printView").css({'display':'none'});
				$("#performance_geomap_printView").css({'display':'none'});
				
			  row='<tr class="odd gradeX">'
			        +'<td colspan="8" style="color:red; text-align:center;">'
				        +'<div class="widget alert alert-info adjusted">'
				        +'<i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong>'
				        +'</div>'
			        +'</td>'						      
			        +'</tr>';
			  
			 $("#dtable1 tbody tr").remove();
		  $("#dtable1 tbody").append(row);
		  
		  $("#sales_Channel").css({'display':'none'});
			$("#perfromace_property").css({'display':'none'});
			$("#sell_through").css({'display':'none'});
	
	}
	
	sbStr = sbStr+"]"; //sbStr completed for creation of Pie Chart
	
	rows = rows+"]";
	if(rows.charAt(1) == ",");
	rows = "[" + rows.substring(2);
	data.addRows(eval(rows));
	
	var formatter = new google.visualization.NumberFormat({pattern:'#,###'});
	formatter.format(data, 1);
	
	   ///////////////////////////////////////////////////////
	   // Start: creating pie chart for Channel Performance///
	   // Description :
	   // Author: Dheeraj Kumar
	   //
	   // dataStr : String containg data values to create Pie Chart
	   // divName : 
	   // chartTitle : 
	   // piechartWidth : This is a  
	   
	   channelPerformancePieChart(sbStr,'IrPieChart_div','Fill Rate',piechartWidth,data);
	   channelPerformancePieChart(sbStr,'IrPieChart_div_printView','Fill Rate',piechartWidth,data);
	
	   ///End : Creating pie chart for Channel Performance///
	   //////////////////////////////////////////////////////
 }	 

function showChannelPerformancePopup(id){
	var title=$('#'+id+'_title').html();
	var chgPercent=$('#'+id+'_changePercent').html();

	if(chgPercent.indexOf('-')< 0){
		subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Up.png'width='15'></span>";
		subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:#52ad00;'>"+chgPercent+"</span></span>";
	}else{
		subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span>";
		subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'>"+chgPercent+"</span></span>";
	}
	
	var contentDiv="";
	
	createEmptyPopup(id, title);
	
	try{	 
	    $.ajax({
	      type : "POST",
	      url : "/loadChannelPerformancePopup.lin",
	      cache: false,
	      data : {
	    	  channelName:title,
	    	  startDate:startDate,
	    	  endDate:endDate,
	    	  selectedPublisher : selectedPublisher
	    	  },		    
	      dataType: 'json',
	      success: function (data) {
	    	  $("#ajax_id").hide();
	         $.each(data, function(index, element) {
	             if(index =='popUpDTO' && element !=null){	            	
	            	
	            	 var contentDiv='<div id="popover_content_wrapper" >'
	                 +'<div class="popheading_outer" style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
	                 +'<div id="popheading" class="popup_heading" style="background-color:#FDEFBC;height:25px;">'
	                 
	                 +'<div class="pop_heading_left_name" style="font-weight:bold;color:black;margin-left:1%;width:29%;float:left;"><b></b></div>'
					 +'<div class="pop_heading_left_value" style="width:29%;float:left;"></div>'
					 +'<div class="pop_heading_right_name" style="font-weight:bold;color:black;width:29%;float:left;text-align:right;"><b></b></div>'
					 +'<div class="pop_heading_right_value" style="margin-right:5px;float:right;"></div></div>'
					  
	           	  +'<div class="sub_heading" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;"><span style="margin-left: 45%;margin-right:15px;"><b>'+'Current'+'</b></span>'
	           	  +'<span style="margin-left:62px;"><b>'+'Last'+'</b></span>'
	           	  +'<span class="sub_heading_right" style="float:right;"><b>'+'MTD'+'</b></span></div>'
	           	  +'<div class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Impressions Delivered:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(element['impressionDeliveredInSelectedTime']))+'</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(element['impressionsDeliveredLastTime']))+'</div>'
	           	  +'<div style="float:right;">'+formatInt(getNumericValue(element['impressionsDeliveredMTD']))+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Clicks:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(element['clicksInSelectedTime']))+'</div>'
	           	  +'<div style="float: left;text-align: right; margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(element['clicksLastTime']))+'</div>'
	           	  +'<div style="float:right;">'+formatInt(getNumericValue(element['clicksMTD']))+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">CTR(%):</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatFloat(element['ctrInSelectedTime'],4)+'%</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatFloat(element['ctrLastTime'],4)+'%</div>'
	           	  +'<div style="float:right;">'+formatFloat(element['ctrMTD'],4)+'%</div>'
	           	  
	           	  
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">eCPM</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(element['eCPMInSelectedTime'],2)+'</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(element['eCPMLastTime'],2)+'</div>'
	           	  +'<div style="float:right;">$'+formatFloat(element['eCPMMTD'],2)+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Payout:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(getFloatValue(element['payoutInSelectedTime'],2),2)+'</div>'
	           	  +'<div style=" float: left;text-align: right; margin-left: 10px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(getFloatValue(element['payoutsLastTime'],2),2)+'</div>'
	           	  +'<div style="float:right;">$'
	           	  +formatFloat(getFloatValue(element['payoutsMTD'],2),2)+'</div>'
	           	  
	           	  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
	           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
	           	  +' onclick=viewPerformance("'+title.replace(/ /g, "&#32;")+'")>View Trends</a>'
//	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
//	           	  +' onclick=javascript:reallcation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
	           	  +'</div></div>';
	           	 
	           	 var chartData=element['chartData'];
	           	
	           	 makePopUP(id,title,subTitle,contentDiv,chartData);
	           	 
	           }           
	             
	         });
	         
	     },
	     error: function(jqXHR, exception) {
	     }
	   });   
	  }catch(error){
	}	  
	
}

function showOptimizedChannelPerformancePopup(id, title){
	
	var subTitle = '';
	var chgPercent = '';
	if($('#'+id+'_title').html() != undefined){
		title=$('#'+id+'_title').html();
		chgPercent=$('#'+id+'_changePercent').html();
		if(chgPercent.indexOf('-')< 0){
			subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Up.png'width='15'></span>";
			subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:#52ad00;'>"+chgPercent+"</span></span>";
		}
		else{
			subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span>";
			subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'>"+chgPercent+"</span></span>";
		}
	}
	
	createEmptyPopup(id, title);
	
	var contentDiv = "";
	//var chartData = "";
	
		var dtoObjectCurrent = {}; 
		var dtoObjectCompare = {};
		var dtoObjectMTD = {};
		
			for(key in publisherSummaryCurrentData)
			{
				dtoObjectCurrent = publisherSummaryCurrentData[key];
				if(title == dtoObjectCurrent.channelName)
				{
					break;
				}
			}
			
			for(key in publisherSummaryCompareData)
			{
				dtoObjectCompare = publisherSummaryCompareData[key];
				if(title == dtoObjectCompare.channelName)
				{
					break;
				}
			}
			
			for(key in publisherSummaryMTDData)
			{
				dtoObjectMTD = publisherSummaryMTDData[key];
				if(title == dtoObjectMTD.channelName)
				{
					break;
				}
			}
			
            	 var contentDiv='<div id="popover_content_wrapper" >'
                 +'<div class="popheading_outer" style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
                 +'<div id="popheading" class="popup_heading" style="background-color:#FDEFBC;height:25px;">'
                 
                 +'<div class="pop_heading_left_name" style="font-weight:bold;color:black;margin-left:1%;width:29%;float:left;"><b></b></div>'
				 +'<div class="pop_heading_left_value" style="width:29%;float:left;"></div>'
				 +'<div class="pop_heading_right_name" style="font-weight:bold;color:black;width:29%;float:left;text-align:right;"><b></b></div>'
				 +'<div class="pop_heading_right_value" style="margin-right:5px;float:right;"></div></div>'
					  
	           	  +'<div class="sub_heading" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;"><span style="margin-left: 45%;margin-right:15px;"><b>'+'Current'+'</b></span>'
	           	  +'<span style="margin-left:62px;"><b>'+'Last'+'</b></span>'
	           	  +'<span class="sub_heading_right" style="float:right;"><b>'+'MTD'+'</b></span></div>'
	           	  +'<div class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Impressions Delivered:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(dtoObjectCurrent.impressionsDelivered))+'</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(dtoObjectCompare.impressionsDelivered))+'</div>'
	           	  +'<div style="float:right;">'+formatInt(getNumericValue(dtoObjectMTD.impressionsDelivered))+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Clicks:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(dtoObjectCurrent.clicks))+'</div>'
	           	  +'<div style="float: left;text-align: right; margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatInt(getNumericValue(dtoObjectCompare.clicks))+'</div>'
	           	  +'<div style="float:right;">'+formatInt(getNumericValue(dtoObjectMTD.clicks))+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">CTR(%):</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">'
	           	  +formatFloat(dtoObjectCurrent.CTR,4)+'%</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">'
	           	  +formatFloat(dtoObjectCompare.CTR,4)+'%</div>'
	           	  +'<div style="float:right;">'+formatFloat(dtoObjectMTD.CTR,4)+'%</div>'
	           	  
	           	  
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">eCPM</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(dtoObjectCurrent.eCPM,2)+'</div>'
	           	  +'<div style=" float: left; text-align: right;margin-left: 10px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(dtoObjectCompare.eCPM,2)+'</div>'
	           	  +'<div style="float:right;">$'+formatFloat(dtoObjectMTD.eCPM,2)+'</div>'
	           	  +'</div><div  class="popup_content_outer" style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:25px;">'
	           	  +'<div class="popup_content_name" style="font-weight:bold;color:black;width:33%;float:left;">Payout:</div>'
	           	  +'<div style="float:left;text-align: right;margin-left: 20px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(dtoObjectCurrent.payOuts,2)+'</div>'
	           	  +'<div style=" float: left;text-align: right; margin-left: 10px;margin-right: 10px;width: 90px;">$'
	           	  +formatFloat(dtoObjectCompare.payOuts,2)+'</div>'
	           	  +'<div style="float:right;">$'
	           	  +formatFloat(dtoObjectMTD.payOuts,2)+'</div>'
	           	  
	           	  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
	           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
	           	  +' onclick=viewPerformance("'+title.replace(/ /g, "&#32;")+'")>View Trends</a>'
//	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
//	           	  +' onclick=javascript:reallcation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
	           	  +'</div></div>';		            	

//	           	 var chartData=element['chartData'];
//            	 var chartData="";
            	 console.log("calling loadChannelPerformancePopup");
            try{
         	    $.ajax({
      		      type : "POST",
      		      url : "/loadChannelPerformancePopup.lin",
      		      cache: false,
      		      data : {
      		    	  channelName:title,
      		    	  startDate:startDate,
      		    	  endDate:endDate,
      		    	  selectedPublisher : selectedPublisher
      		    	  },		    
      		      dataType: 'json',
      		      success: function (data) {
      		    	 console.log("loadChannelPerformancePopup : success");
      		    	  $("#ajax_id").hide();
      		         $.each(data, function(index, element) {
      		             if(index =='popUpDTO' && element !=null){	            	
      		            	
      		           	var chartData=element['chartData'];
      		           	
      		           	 makePopUP(id,title,subTitle,contentDiv,chartData);
      		           	 
      		           }           
      		             
      		         });
      		         
      		     },
      		     error: function(jqXHR, error) {
      		    	 console.log("Error in loadChannelPerformancePopup : "+error);
      		     }
      		   });
	   }catch(exception){
		   console.log("Exception in loadChannelPerformancePopup : "+exception);
	}	  
	
}

function updateSecondAndThird(id) {
	if(id!='-1')
	{
		var channelName=$('#'+id+'_title').html();
		/// Start calling performanceByProperty  ///
		getOptimizedPerformanceByPropertyByChannel(channelName);
		/// End calling performanceByProperty  ///
		 if(selectedPublisher != 'Lin Digital'){
		if(channelName == 'Local Sales Direct' || channelName == 'National Sales Direct' || channelName == 'House'){
			$("#sell_throughdata").css({'display':'block'});
			$("#performance_geomap").css({'display':'block'});
			$("#performance_geomap_printView").css({'display':'block'});
			$("#sell_throughdata_printView").css({'display':'block'});
			$("#sell_throughdata_donut_printView").css({'display':'block'});
		}
		else{
			$("#sell_throughdata").css({'display':'none'});
			$("#performance_geomap").css({'display':'block'});
			$("#performance_geomap_printView").css({'display':'block'});
			$("#sell_throughdata_printView").css({'display':'none'});
			$("#sell_throughdata_donut_printView").css({'display':'none'});
		}
		 }else{
			 $("#sell_throughdata").css({'display':'none'});
			 $("#performance_geomap").css({'display':'none'});
			 $("#performance_geomap_printView").css({'display':'none'});
			 $("#sell_throughdata_printView").css({'display':'none'});
			 $("#sell_throughdata_donut_printView").css({'display':'none'});
		 }

	}else{
		var channelName=$('#1_title').html();
		/// Start calling performanceByProperty  ///
				getOptimizedPerformanceByPropertyAllChannels();
			      if(selectedPublisher != 'Lin Digital'){
			        	$("#sell_throughdata").css({'display':'block'});
			        	$("#performance_geomap").css({'display':'block'});
			        	$("#performance_geomap_printView").css({'display':'block'});
			        	$("#sell_throughdata_printView").css({'display':'block'});
						$("#sell_throughdata_donut_printView").css({'display':'block'});
				    }else{
				    	$("#sell_throughdata").css({'display':'none'});
						$("#performance_geomap").css({'display':'none'});
						$("#performance_geomap_printView").css({'display':'none'});
						$("#sell_throughdata_printView").css({'display':'none'});
						$("#sell_throughdata_donut_printView").css({'display':'none'});
				    }
		/// End calling performanceByProperty  ///
	}
	
	
	
	
	
	$("#"+CpPreviousRowId).css({'background':'#ffffff'});
	$("#"+id).css({'background':'#d9edf7'});
	CpPreviousRowId= id;

}



function changeRowColor(id)
{
	$("#"+STRPreviousRowId).css({'background':'#ffffff'});
	$("#"+id).css({'background':'#d9edf7'});
	STRPreviousRowId= id;
}


function updateDonutChart(strVal, containerDiv ) {
//alert(strVal);
	remaining = 100 - strVal;
	strVal = formatFloat(strVal,4);
	remaining = formatFloat(remaining,4);
	
	    var chart = new Highcharts.Chart({
	        chart: {
	            renderTo: containerDiv,
	            type: 'pie'
	        },
	        plotOptions: {
	            pie: {
	                innerSize: '40%',
	                size: '50%'
	            }
	        },
	        series: [{
	   	            data: [
	                ['Sell Through Rate', eval(strVal)],
	                ['Remnant',  eval(remaining)]
	                ]}]
	    },
	                                     
	    function(chart) { // on complete
	        
	        var xpos = '50%';
	        var ypos = '55%';
	        var circleradius = 100;
	    
	    // Render the circle
	    chart.renderer.circle(xpos, ypos, circleradius).attr({
	        fill: 'white',
	    }).add();

	    // Render the text 
	    chart.renderer.text('', xpos, ypos ).css({
	            width: circleradius*2,
	            color: 'red',
	            textAlign: 'center',
	            text: 'STR'
	      }).attr({
	            zIndex: 999
	        }).add();
	    });
	}


function updateDonutForInventoryHeader(strVal, containerDiv ) {
	remaining = 100 - strVal;
	strVal = formatFloat(strVal,4);
	remaining = formatFloat(remaining,4);
	
		    var chart = new Highcharts.Chart({
		        chart: {
		            renderTo: containerDiv,
		            type: 'pie'
		        },
		        plotOptions: {
		            pie: {
		                innerSize: '40%'
		            }
		        },
		        series: [{
		   	            data: [
		                ['Fill %', eval(strVal)],
		                ['Remnant',  eval(remaining)]
		                ]}]
		    },
		                                     
		    function(chart) { // on complete
		        
		    var xpos = '50%';
		        var ypos = '53%';
		        var circleradius = 30;
		    
		    // Render the circle
		    chart.renderer.circle(xpos, ypos, circleradius).attr({
		        fill: '#333333',
		    }).add();

		    // Render the text 
		    chart.renderer.text('', xpos, ypos ).css({
		            width: circleradius*2,
		            color: 'red',
		            textAlign: 'center',
		            text: 'STR'
		      }).attr({
		            zIndex: 999
		        }).add();
		    });
		}


function updateDonutForTrendAnalysisHeader(strVal, containerDiv ) {
	//alert(strVal);
	remaining = 100 - strVal;
	strVal = formatFloat(strVal,4);
	remaining = formatFloat(remaining,4);
	
		    var chart = new Highcharts.Chart({
		        chart: {
		            renderTo: containerDiv,
		            type: 'pie'
		        },
		        plotOptions: {
		            pie: {
		                innerSize: '40%'
		            }
		        },
		        series: [{
		   	            data: [
		                ['Fill Rate', eval(strVal)],
		                ['Remnant',  eval(remaining)]
		                ]}]
		    },
		                                     
		    function(chart) { // on complete
		        
		    var xpos = '50%';
		        var ypos = '53%';
		        var circleradius = 30;
		    
		    // Render the circle
		    chart.renderer.circle(xpos, ypos, circleradius).attr({
		        fill: '#333333',
		    }).add();

		    // Render the text 
		    chart.renderer.text('', xpos, ypos ).css({
		            width: circleradius*2,
		            color: 'red',
		            textAlign: 'center',
		            text: 'STR'
		      }).attr({
		            zIndex: 999
		        }).add();
		    });
		}
  
function getPerformanceByPropertyByChannel(channel){
	$("#dtable1 tbody tr").remove();
	  console.log("getPerformanceByPropertyByChannel:startDate:"+startDate+" and endDate:"+endDate
			  +" : compareStartDate:"+compareStartDate+" and compareEndDate:"+compareEndDate);
	  try{
		  $("#perfromace_property_ajax").css({'display':'inline'});
	 		 $.ajax({
	 		       type : "POST",
	 		       url : "/performanceByProperty.lin",
	 		       cache: false,
	 		       data : {
	 		    	   		startDate:startDate,
	 		    	   		endDate:endDate,
	 		    	   		compareStartDate:compareStartDate,
	 		    	   		compareEndDate:compareEndDate,
	 		    	   		channel:channel,
	 		    	   	    selectedPublisher : selectedPublisher
	 		       		},
	 		       dataType: 'json',
	 		       success: function (data) {	
	 		    	  $("#perfromace_property_ajax").css({'display':'none'});
	 		    	  $.each(data, function(index, element) {	        	   
	 		        	   if(index== 'performanceByPropertyList' && element.length > 0){
	 		        		  var dataList=data['performanceByPropertyList'];
	 		        		  var lastRowObj=data['performanceByPropertyTotalObj'];
		 		        	   $("#dtable1 tbody tr").remove();
		 		        	   var idIndex=0;
		 		        	   var jj = 0;
		 		        	//// for loop start/////	 		        	   
		 		        	  var key = 0;
		 		        	  for (var i = 0 ; i < dataList.length ; i = i + 1) {
		 		        		   (function(i) {
		 		        			     setTimeout( function(i) {
		 		        		    	   
		 		        		           // code-here
 
		 		        	  jj++;
		 		        	   
		 		        	   
		 		        		  var id="performanceByProperty_"+idIndex;
		 		        		  var dtoObject = dataList[key];
		 		        		  var row = "<tr class='even gradeC'>" +
		 		        		  		"<td id='"+id+"_title' rel='popover'>"
		 		        			    +dtoObject.name+"</td><td style='text-align: right;'>$"
		 		        			    +formatFloat(dtoObject.eCPM,2)+"</td>";
		 		        		  
		 		        		 if(dtoObject.CHG == 0 || dtoObject.percentageCHG == 0){
		 		        			 /*row = row + "<td style='text-align: right;'><img src='img/bullet.png'width='13' height='14' style='margin-right:5px;'>$"
		 		        			         +formatFloat(dtoObject.CHG,true)+"</td><td style='text-align: right;'><img src='img/bullet.png'width='13' height='14' style='margin-right:5px;'>"
		 		        			         +formatFloat(dtoObject.percentageCHG,true)+"%</td>";*/
		 		        			 
		 		        			row = row + "<td style='text-align: right;'>$"
		        			         +formatFloat(dtoObject.CHG,2)+"</td><td style='text-align: center;'> --- "
		        			         +"</td>";
		 		        			
		 		        			 
		 		        			}
		 		        		 else if(dtoObject.CHG < 0 || dtoObject.percentageCHG < 0){
		 		        			 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
		 		        			         +formatFloat(dtoObject.CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
		 		        			         +formatFloat(dtoObject.percentageCHG,4)+"%</td>";
		 		        			}
		 		        		  
		 		        		  else{
		 		        			   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
		 		        				       +formatFloat(dtoObject.CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
		 		        				       +formatFloat(dtoObject.percentageCHG,4)+"%</td>";
		 		        		  }
		 		        		  
		 		        		  
		 		        		  row = row+"<td class='' style='text-align: right;'>"+formatInt(dtoObject.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(dtoObject.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(dtoObject.payout,2)+"</td></tr>";
		 		        		  
		 		        		  $("#dtable1 tbody").append(row);
		 		        		  idIndex++;
		 		        		  
				 		        	 if(dataList.length == jj){
				 		        		 var noOfRows = idIndex;
				 		        		 if(lastRowObj.CHG < 0 || lastRowObj.percentageCHG < 0)
				 		        		{
				 		        			var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((lastRowObj.payout/lastRowObj.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"+formatFloat(lastRowObj.CHG,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>"+formatFloat(lastRowObj.percentageCHG,4)+"%</td> "
			 	 		          		       +"<td style='text-align: right;'>"+formatInt(lastRowObj.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(lastRowObj.clicks)+"</td> <td style='text-align: right;'>$"+formatFloat(lastRowObj.payout,2)+"</td> </tr>";
				 		        		}
				 		        		else if(lastRowObj.CHG == 0 || lastRowObj.percentageCHG == 0)
				 		        		{
					 		        			
				 		        			var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((lastRowObj.payout/lastRowObj.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'>$"+formatFloat(lastRowObj.CHG,2)+"</td> <td style='text-align: center;'> --- </td> "
				 		        			+"<td style='text-align: right;'>"+formatInt(lastRowObj.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(lastRowObj.clicks)+"</td> <td style='text-align: right;'>$"+formatFloat(lastRowObj.payout,2)+"</td> </tr>";
				 		        		}
				 		        		else
				 		        		{
				 		        			var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((lastRowObj.payout/lastRowObj.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(lastRowObj.CHG,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(lastRowObj.percentageCHG,4)+"%</td> "
			 	 		          		       +"<td style='text-align: right;'>"+formatInt(lastRowObj.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(lastRowObj.clicks)+"</td> <td style='text-align: right;'>$"+formatFloat(lastRowObj.payout,2)+"</td> </tr>";
				 		        		}
				 	 		          
				 	 		          
				 	 		          		          
				 	 		         }
				 		        	 $("#dtable1 tbody").append(lastRow);
				 		        		//for loop end .../////  		 		        		  
		 		        		  


		 		        		  
		 		        	   
		 		        	  
		 		        	  

		 		        		//for loop cont.../////	 		        		  
		 		        		      	 		  key ++;
		 		        		       }, 10)
		 		        		   })(i)

		 		        		}
		 		        	  //end code

		 		        		  
	 		        	   }
	 		        	  else if(index == 'performanceByPropertyList' && element.length == 0) {
	 		        		  row='<tr class="odd gradeX">'
	 						        +'<td colspan="8" style="color:red; text-align:center;">'
	 							        +'<div class="widget alert alert-info adjusted">'
	 							        +'<i class="cus-exclamation"></i>'
	 							        +'<strong>No records found for the selected filters</strong>'
	 							        +'</div>'
	 						        +'</td>'						      
	 						        +'</tr>';
	 		        		  
	 		        		 $("#dtable1 tbody tr").remove();
	 		 		         $("#dtable1 tbody").append(row);
	 						}

	 		           });
	 		          
	 		       },
	 		      
	 		       error: function(jqXHR, exception) {
	 		    	  $("#perfromace_property_ajax").css({'display':'none'});
	 		        }
	 			
	 		  });
	  }catch(error){
		  $("#perfromace_property_ajax").css({'display':'none'});
	  }	 		 
}

function getOptimizedPerformanceByPropertyByChannel(param_channel){
	var channel = param_channel;
	$("#dtable1 tbody tr").remove();
	$("#dtable1-printView tbody tr").remove();
	
	var sbStr = "[['latitude', 'longitude', 'Property', 'CTR(%)']";
	
	if(performanceByPropertyCurrentData != undefined && performanceByPropertyCurrentData.length > 0)
	{
	
	var payoutsTotalCompare = 0.0;
	var impDeliveredTotalCompare = 0;
	
	var clicksTotalCurrent = 0;
	var payoutsTotalCurrent = 0.0;
	var impDeliveredTotalCurrent = 0;
	
	var eCPMTotalCompare = 0.0;
	var eCPMTotalCurrent = 0.0;

	var changeTotal = 0.0;
	var percentageCHGTotal = 0.0;
	var imprs =0;
	var payout = 0;
	var flg = 0;
	  
	
	
	for(i=0 ; i<performanceByPropertyCurrentData.length; i++)
	{
	var matchflag = 0; 	

	var objectCurrent  = performanceByPropertyCurrentData[i];

	if (performanceByPropertyCompareData != undefined && performanceByPropertyCompareData.length !=0){	

	for(j=0 ; j< performanceByPropertyCompareData.length; j++)	
	{
	
	var objectCompare  = performanceByPropertyCompareData[j];
	
	if(objectCurrent.site == objectCompare.site && channel == objectCurrent.channelName && objectCurrent.channelName == objectCompare.channelName && objectCurrent.dataSource == 'DFP')
	{	
	
	imprs = 0;
	var clks = objectCurrent.clicks;
	    imprs = objectCurrent.impressionsDelivered;
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;	
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;
	
	
	if(objectCompare.eCPM == 0)
	{
	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;
	}
	else
	{
	CHG = objectCurrent.eCPM - objectCompare.eCPM;
	percentageCHG = (CHG / objectCompare.eCPM) * 100;
	}
	
	
	  var id="performanceByProperty_"+i;
	       	  var row = "<tr class='even gradeC'>" +
	       	  	"<td id='"+id+"_title' rel='popover'>"
	       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
	       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
	       	  
	       	 if(CHG == 0 || percentageCHG == 0){
	       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
	       	
	       	 
	       	}
	       	 else if(CHG < 0 || percentageCHG < 0){
	       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
	       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	       	         +formatFloat(percentageCHG,4)+"%</td>";
	       	}
	       	  
	       	  else{
	       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
	       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
	       	       +formatFloat(percentageCHG,4)+"%</td>";
	       	  }
	       	  
	       	  
	       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.DFPPayout,2)+"</td></tr>";
	
	       	  $("#dtable1 tbody").append(row);
	       	  $("#dtable1-printView tbody").append(row);
	
	
	payoutsTotalCompare = payoutsTotalCompare + objectCompare.payout;
	impDeliveredTotalCompare = impDeliveredTotalCompare + objectCompare.impressionsDelivered;
	
	clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.DFPPayout;
	impDeliveredTotalCurrent = impDeliveredTotalCurrent + objectCurrent.impressionsDelivered;
	
	}else if(objectCurrent.site == objectCompare.site && channel == objectCurrent.channelName && objectCurrent.channelName == objectCompare.channelName && objectCurrent.dataSource != 'DFP' ){
	imprs =0;
	payout = 0;
	var requestweightage=0.0;
	if(objectCurrent.totalImpressionsDeliveredByChannelName!=null && objectCurrent.totalImpressionsDeliveredByChannelName!=0 && objectCurrent.impressionsDelivered!=null){
	 requestweightage = objectCurrent.impressionsDelivered / objectCurrent.totalImpressionsDeliveredByChannelName;
	}
	
	var clks = objectCurrent.clicks;
	if(objectCurrent.totalImpressionsDeliveredBySiteName!=null && requestweightage!=null && requestweightage!= undefined){
	imprs = (parseFloat(requestweightage) * parseInt(objectCurrent.totalImpressionsDeliveredBySiteName));
	}
	if(objectCurrent.payout!=null && objectCurrent.payout!=undefined && requestweightage!=null && requestweightage!= undefined){
	payout = requestweightage * objectCurrent.payout;
	}
	
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;	
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;
	
	
	if(objectCompare.eCPM == 0)
	{
	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;
	}
	else
	{
	CHG = objectCurrent.eCPM - objectCompare.eCPM;
	percentageCHG = (CHG / objectCompare.eCPM) * 100;
	}
	
	
	  var id="performanceByProperty_"+i;
	       	  var row = "<tr class='even gradeC'>" +
	       	  	"<td id='"+id+"_title' rel='popover'>"
	       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
	       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
	       	  
	       	 if(CHG == 0 || percentageCHG == 0){
	       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
	       	
	       	 
	       	}
	       	 else if(CHG < 0 || percentageCHG < 0){
	       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
	       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	       	         +formatFloat(percentageCHG,4)+"%</td>";
	       	}
	       	  
	       	  else{
	       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
	       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
	       	       +formatFloat(percentageCHG,4)+"%</td>";
	       	  }
	       	  
	       	  
	       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(imprs)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(payout,2)+"</td></tr>";
	
	       	  $("#dtable1 tbody").append(row);
	       	  $("#dtable1-printView tbody").append(row);
	
	payoutsTotalCompare = payoutsTotalCompare + objectCompare.payout;
	impDeliveredTotalCompare = impDeliveredTotalCompare + objectCompare.impressionsDelivered;
	
	clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	payoutsTotalCurrent = payoutsTotalCurrent + payout;
	impDeliveredTotalCurrent = impDeliveredTotalCurrent + imprs;	
	}else if (channel == objectCurrent.channelName && performanceByPropertyCompareData.length-1 == j && matchflag == 0 && objectCurrent.dataSource == 'DFP'){

	//alert("3")
	imprs = 0;
	var clks = objectCurrent.clicks;
	imprs = objectCurrent.impressionsDelivered;
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;

	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;

	var id="performanceByProperty_"+i;
	       	    var row = "<tr class='even gradeC'>" +
	       	  	"<td id='"+id+"_title' rel='popover'>"
	       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
	       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
	       	  
	       	 if(CHG == 0 || percentageCHG == 0){
	       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
	       	
	       	 
	       	}
	       	 else if(CHG < 0 || percentageCHG < 0){
	       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
	       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	       	         +formatFloat(percentageCHG,4)+"%</td>";
	       	}
	       	  
	       	  else{
	       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
	       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
	       	       +formatFloat(percentageCHG,4)+"%</td>";
	       	  }
	       	  
	       	  
	       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.DFPPayout,2)+"</td></tr>";
	
	       	  $("#dtable1 tbody").append(row);
	       	  $("#dtable1-printView tbody").append(row);

	       	  clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	  payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.DFPPayout;
	  impDeliveredTotalCurrent = impDeliveredTotalCurrent + objectCurrent.impressionsDelivered;	
	
	
	
	}else if (channel == objectCurrent.channelName && performanceByPropertyCompareData.length-1 == j && matchflag == 0 && objectCurrent.dataSource != 'DFP'){
	var imprs = 0;
	var payout = 0;
	if(objectCurrent.totalImpressionsDeliveredByChannelName!=null && objectCurrent.impressionsDelivered!=null){
	var requestweightage = objectCurrent.impressionsDelivered / objectCurrent.totalImpressionsDeliveredByChannelName;
	}
	
	var clks = objectCurrent.clicks;
	if(objectCurrent.totalImpressionsDeliveredBySiteName!=null && objectCurrent.totalImpressionsDeliveredByChannelName!=0 && requestweightage!=null && requestweightage!= undefined){
	imprs = requestweightage * objectCurrent.totalImpressionsDeliveredBySiteName;
	}
	if(objectCurrent.payout!=null && objectCurrent.payout!=undefined && requestweightage!=null && requestweightage!= undefined){
	payout = requestweightage * objectCurrent.payout;
	}
	
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;

	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;

	var id="performanceByProperty_"+i;
       	    var row = "<tr class='even gradeC'>" +
       	  	"<td id='"+id+"_title' rel='popover'>"
       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
       	  
       	 if(CHG == 0 || percentageCHG == 0){
       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
       	
       	 
       	}
       	 else if(CHG < 0 || percentageCHG < 0){
       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
       	         +formatFloat(percentageCHG,4)+"%</td>";
       	}
       	  
       	  else{
       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
       	       +formatFloat(percentageCHG,4)+"%</td>";
       	  }
       	  
       	  
       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(imprs)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(payout,2)+"</td></tr>";
	
       	  $("#dtable1 tbody").append(row);
       	  $("#dtable1-printView tbody").append(row);

       	  clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	  payoutsTotalCurrent = payoutsTotalCurrent + payout;
	  impDeliveredTotalCurrent = impDeliveredTotalCurrent + imprs;	
	
	}
	
	  }
	
	}
	else{
	
	if (channel == objectCurrent.channelName){
	imprs = 0;
	var clks = objectCurrent.clicks;
	 imprs = objectCurrent.impressionsDelivered;
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;

	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;

	var id="performanceByProperty_"+i;
       	    var row = "<tr class='even gradeC'>" +
       	  	"<td id='"+id+"_title' rel='popover'>"
       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
       	  
       	 if(CHG == 0 || percentageCHG == 0){
       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
       	
       	 
       	}
       	 else if(CHG < 0 || percentageCHG < 0){
       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
       	         +formatFloat(percentageCHG,4)+"%</td>";
       	}
       	  
       	  else{
       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
       	       +formatFloat(percentageCHG,4)+"%</td>";
       	  }
       	  
       	  
       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.DFPPayout,2)+"</td></tr>";
	
       	  $("#dtable1 tbody").append(row);
       	  $("#dtable1-printView tbody").append(row);

       	  clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	  payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.DFPPayout;
	  impDeliveredTotalCurrent = impDeliveredTotalCurrent + objectCurrent.impressionsDelivered;	

	  }	
	}
	

	}	
	
	if(flg > 0)
	{
	eCPMTotalCurrent = (payoutsTotalCurrent / impDeliveredTotalCurrent) * 1000;
	eCPMTotalCompare = (payoutsTotalCompare / impDeliveredTotalCompare) * 1000;
	
	changeTotal = (eCPMTotalCurrent - eCPMTotalCompare);
	percentageCHGTotal = (changeTotal / eCPMTotalCompare) * 100;
	
	
	 if(changeTotal < 0 || percentageCHGTotal < 0)
        	{
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>"+formatFloat(percentageCHGTotal,4)+"%</td> "
          	       +"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
        	else if(changeTotal == 0 || percentageCHGTotal == 0)
        	{
	        	
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> </td> "
        	+"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
        	else
        	{
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(percentageCHGTotal,4)+"%</td> "
          	       +"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
	         
        	 $("#dtable1 tbody").append(lastRow);
        	 $("#dtable1-printView tbody").append(lastRow);
	
	}else{
		row='<tr class="odd gradeX">'
		        +'<td colspan="8" style="color:red; text-align:center;">'
		        +'<div class="widget alert alert-info adjusted">'
		        +'<i class="cus-exclamation"></i>'
		        +'<strong>No records found for the selected filters</strong>'
		        +'</div>'
		        +'</td>'	      
		        +'</tr>';
		  
		 	$("#dtable1 tbody tr").remove();
	         $("#dtable1 tbody").append(row);
	         
	         $("#dtable1-printView tbody tr").remove();
	         $("#dtable1-printView tbody").append(row);
		}
	

	}
	else
	{
	row='<tr class="odd gradeX">'
	        +'<td colspan="8" style="color:red; text-align:center;">'
	        +'<div class="widget alert alert-info adjusted">'
	        +'<i class="cus-exclamation"></i>'
	        +'<strong>No records found for the selected filters</strong>'
	        +'</div>'
	        +'</td>'	      
	        +'</tr>';
	  
	 $("#dtable1 tbody tr").remove();
         $("#dtable1 tbody").append(row);
         
         $("#dtable1-printView tbody tr").remove();
         $("#dtable1-printView tbody").append(row);
	}
	
	sbStr = sbStr+"]";
	performanceByPropertyGeoChart(sbStr,'geomap4','GEO CHART',geoChartWidth);
	performanceByPropertyGeoChart_printView(sbStr,'geomap4_printView','GEO CHART','768');
	
}

function getOptimizedPerformanceByPropertyAllChannels(){
	$("#dtable1 tbody tr").remove();
	$("#dtable1-printView tbody tr").remove();
	
	var sbStr = "[['latitude', 'longitude', 'Property', 'CTR(%)']";
	/*sbStr = sbStr+",[33.896366, -80.9102195, 'lin.kasa', 30],[41.615065, -87.8455645, 'lin.kxon', 1]";*/
	
	if(performanceByPropertyCurrentDataBySiteName != undefined && performanceByPropertyCurrentDataBySiteName.length > 0)
	{
	
	var payoutsTotalCompare = 0.0;
	var impDeliveredTotalCompare = 0;
	
	var clicksTotalCurrent = 0;
	var payoutsTotalCurrent = 0.0;
	var impDeliveredTotalCurrent = 0;
	
	var eCPMTotalCompare = 0.0;
	var eCPMTotalCurrent = 0.0;

	var changeTotal = 0.0;
	var percentageCHGTotal = 0.0;
	var imprs =0;
	var payout = 0;
	var flg = 0;
	//var oldPropertyName = '';
	//var newPropertyName = '';
	
	for(i=0 ; i<performanceByPropertyCurrentDataBySiteName.length; i++)
	{
	var matchflag = 0; 	
	
	var objectCurrent  = performanceByPropertyCurrentDataBySiteName[i];
	//newPropertyName = objectCurrent.site;
	if (performanceByPropertyCompareDataBySiteName != undefined && performanceByPropertyCompareDataBySiteName.length !=0){	

	for(j=0 ; j< performanceByPropertyCompareDataBySiteName.length; j++)	
	{
	
	var objectCompare  = performanceByPropertyCompareDataBySiteName[j];
	if(objectCurrent.site == objectCompare.site && objectCurrent.channelName == objectCompare.channelName )
	{	
	//alert("1")
	imprs = 0;
	var clks = objectCurrent.clicks;
	    imprs = objectCurrent.impressionsDelivered;
	    //alert(objectCurrent.)
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;	
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;
	
	
	if(objectCompare.eCPM == 0)
	{
	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;
	}
	else
	{
	CHG = objectCurrent.eCPM - objectCompare.eCPM;
	percentageCHG = (CHG / objectCompare.eCPM) * 100;
	}
	
	
	  var id="performanceByProperty_"+i;
	       	  var row = "<tr class='even gradeC'>" +
	       	  	"<td id='"+id+"_title' rel='popover'>"
	       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
	       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
	       	  
	       	 if(CHG == 0 || percentageCHG == 0){
	       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'> $"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
	       	
	       	 
	       	}
	       	 else if(CHG < 0 || percentageCHG < 0){
	       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
	       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	       	         +formatFloat(percentageCHG,4)+"%</td>";
	       	}
	       	  
	       	  else{
	       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
	       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
	       	       +formatFloat(percentageCHG,4)+"%</td>";
	       	  }
	       	  
	       	  
	       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.payout,2)+"</td></tr>";
	
	       	  $("#dtable1 tbody").append(row);
	       	  $("#dtable1-printView tbody").append(row);
	
	
	payoutsTotalCompare = payoutsTotalCompare + objectCompare.payout;
	impDeliveredTotalCompare = impDeliveredTotalCompare + objectCompare.impressionsDelivered;
	clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.payout;
	impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.impressionsDelivered);
	
	}else if ( performanceByPropertyCompareDataBySiteName.length-1 == j && matchflag == 0 ){
	imprs = 0;
	var clks = objectCurrent.clicks;
	imprs = objectCurrent.impressionsDelivered;
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;

	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;

	var id="performanceByProperty_"+i;
	       	    var row = "<tr class='even gradeC'>" +
	       	  	"<td id='"+id+"_title' rel='popover'>"
	       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
	       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
	       	  
	       	 if(CHG == 0 || percentageCHG == 0){
	       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
	       	
	       	 
	       	}
	       	 else if(CHG < 0 || percentageCHG < 0){
	       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
	       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	       	         +formatFloat(percentageCHG,4)+"%</td>";
	       	}
	       	  
	       	  else{
	       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
	       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
	       	       +formatFloat(percentageCHG,4)+"%</td>";
	       	  }
	       	  
	       	  
	       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.payout,2)+"</td></tr>";
	
	       	  $("#dtable1 tbody").append(row);
	       	  $("#dtable1-printView tbody").append(row);
	       	  
	       	  clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	  payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.payout;
	  impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.impressionsDelivered);	
	
	
	
	}
	
	  }
	
	}
	else{
	imprs = 0;
	var clks = objectCurrent.clicks;
	 imprs = objectCurrent.impressionsDelivered;
	var ctr = (clks / imprs) * 100;
	ctr = ctr.toFixed(4);
	sbStr = sbStr+",["+objectCurrent.latitude+","+objectCurrent.longitude+",'"+ objectCurrent.name + "'," + ctr+"]";
	
	flg++;
	matchflag = 1;
	var eCPM = objectCurrent.eCPM;
	var CHG = 0.0;
	var percentageCHG = 0.0;

	CHG = objectCurrent.eCPM
	percentageCHG = 0.0;

	var id="performanceByProperty_"+i;
       	    var row = "<tr class='even gradeC'>" +
       	  	"<td id='"+id+"_title' rel='popover'>"
       	    +objectCurrent.name+"</td><td style='text-align: right;'>$"
       	    +formatFloat(objectCurrent.eCPM,2)+"</td>";
       	  
       	 if(CHG == 0 || percentageCHG == 0){
       	row = row + "<td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"
	         +formatFloat(CHG,2)+"</td><td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> "
	         +"</td>";
       	
       	 
       	}
       	 else if(CHG < 0 || percentageCHG < 0){
       	 row = row + "<td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
       	         +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
       	         +formatFloat(percentageCHG,4)+"%</td>";
       	}
       	  
       	  else{
       	   row = row + "<td style='text-align: right;'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
       	       +formatFloat(CHG,2)+"</td><td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
       	       +formatFloat(percentageCHG,4)+"%</td>";
       	  }
       	  
       	  
       	  row = row+"<td class='' style='text-align: right;'>"+formatInt(objectCurrent.impressionsDelivered)+"</td><td class='' style='text-align: right;'>"+formatInt(objectCurrent.clicks)+"</td><td class='' style='text-align: right;'>$"+formatFloat(objectCurrent.payout,2)+"</td></tr>";
	
       	  $("#dtable1 tbody").append(row);
       	  $("#dtable1-printView tbody").append(row);
       	  clicksTotalCurrent = clicksTotalCurrent + objectCurrent.clicks;
	  payoutsTotalCurrent = payoutsTotalCurrent + objectCurrent.payout;
	  impDeliveredTotalCurrent = impDeliveredTotalCurrent + Math.round(objectCurrent.impressionsDelivered);	
	 
	}
	

	}	
	
	if(flg > 0)
	{
	eCPMTotalCurrent = (payoutsTotalCurrent / impDeliveredTotalCurrent) * 1000;
	eCPMTotalCompare = (payoutsTotalCompare / impDeliveredTotalCompare) * 1000;
	
	changeTotal = (eCPMTotalCurrent - eCPMTotalCompare);
	percentageCHGTotal = (changeTotal / eCPMTotalCompare) * 100;
	
	
	 if(changeTotal < 0 || percentageCHGTotal < 0)
        	{
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>"+formatFloat(percentageCHGTotal,4)+"%</td> "
          	       +"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
        	else if(changeTotal == 0 || percentageCHGTotal == 0)
        	{
	        	
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/noChange.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: center;'> <img src='img/minus.png'width='11' height='12' style='margin-right:5px;'> </td> "
        	+"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
        	else
        	{
        	var lastRow ="<tr><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat(eCPMTotalCurrent,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(changeTotal,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(percentageCHGTotal,4)+"%</td> "
          	       +"<td style='text-align: right;'>"+formatInt(impDeliveredTotalCurrent)+"</td> <td style='text-align: right;'>"+formatInt(clicksTotalCurrent)+"</td> <td style='text-align: right;'>$"+formatFloat(payoutsTotalCurrent,2)+"</td> </tr>";
        	}
	         
        	 $("#dtable1 tbody").append(lastRow);
        	 $("#dtable1-printView tbody").append(lastRow);
	
	}
	

	}
	else
	{
	row='<tr class="odd gradeX">'
	        +'<td colspan="8" style="color:red; text-align:center;">'
	        +'<div class="widget alert alert-info adjusted">'
	        +'<i class="cus-exclamation"></i>'
	        +'<strong>No records found for the selected filters</strong>'
	        +'</div>'
	        +'</td>'	      
	        +'</tr>';
	  
	 $("#dtable1 tbody tr").remove();
     $("#dtable1 tbody").append(row);
     
     $("#dtable1-printView tbody tr").remove();
     $("#dtable1-printView tbody").append(row);
	}
	
	sbStr = sbStr+"]";
	performanceByPropertyGeoChart(sbStr,'geomap4','GEO CHART',geoChartWidth);
	performanceByPropertyGeoChart_printView(sbStr,'geomap4_printView','GEO CHART','768');
	
}



function showPerformanceByPropertyPopup(id){
	var title=$('#'+id+'_title').html();
	var subTitle="<span style='margin-left:2px;'></span>"
		         +"<span style='margin-left:10px;margin-top:2px;color:red;'></span></span>";
	
	var contentDiv="";
	console.log("showPerformanceByPropertyPopup:startDate:"+startDate+" and endDate:"+endDate);
	try{
	    $.ajax({
	    	url : "/performanceByProperty.lin",
	 	    cache: false,
	 	    data : {property:title,startDate:startDate,endDate:endDate},		    
	        dataType: 'json',
	        success: function (data) {
	          $.each(data, function(index, element) {
	             if(index =='popUpDTO' && element !=null){
	            	 var CPM=element['CPM'];
	            	 var impressionDeliveredInSelectedTime=element['impressionDeliveredInSelectedTime'];
	            	 var clicksInSelectedTime=element['clicksInSelectedTime'];
	            	 var changeCTRInSelectedTime=element['changeCTRInSelectedTime'];
	            	 var payout=element['payout'];
	            	    
	            	 var contentDiv='<div id="popover_content_wrapper">'
	                 +'<div class="popheading_outer">'
	                 +'<div id="popheading" class="popup_heading">'
	                 
	                 +'<div class="pop_heading_left_name"><b></b></div>'
					 +'<div class="pop_heading_left_value"></div>'
					 +'<div class="pop_heading_right_name"><b></b></div>'
					 +'<div class="pop_heading_right_value"></div></div>'
					  
	           	  +'<div class="sub_heading"><span class="sub_heading_left"><b>'+timePeriod+'</b></span>'
	           	  +'<span class="sub_heading_right"><b>LifeTime</b></span></div>'
	           	  +'<div class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Impressions delivered:</div>'
	           	  +'<div class="popup_content_left_value">'+impressionDeliveredInSelectedTime+'</div>'
	           	  +'<div class="popup_content_right_value">'+impressionDeliveredInSelectedTime*5+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Clicks:</div>'
	           	  +'<div class="popup_content_left_value">'+clicksInSelectedTime+'</div>'
	           	  +'<div class="popup_content_right_value">'+clicksInSelectedTime*4+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">CPM :</div>'
	           	  +'<div class="popup_content_left_value">'+CPM+'</div>'
	           	  +'<div class="popup_content_right_value">'+parseFloat(CPM*3).toFixed(4)+'%</div>'
	           	  
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Payout :</div>'
	           	  +'<div class="popup_content_left_value">$'+payout+'</div>'
	           	  +'<div class="popup_content_right_value">$'+parseFloat(payout*5/2).toFixed(2)+'</div>'
	           	
	           	  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
	           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
	           	  //+'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
	           	 // +' onclick=viewPerformance("'+title.replace(/ /g, "&#32;")+'")>View Trends</a>'
	           	  //+'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
	           	 // +' onclick=javascript:reallcation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
	           	  +'</div></div>';		            	
	           	 
	           	 var chartData=element['chartData'];	           	
	           	 makePopUP(id,title,subTitle,contentDiv,chartData);
	           }           
	             
	         });
	         
	     },
	    
	     error: function(jqXHR, exception) {
	     }
	   });   
	  }catch(error){
	}	  
	
}


   function getActualPublisherData(){
	   var title=$('#publisher_allocation_val').html();	 
	   var row = '';
	   jQuery('#actualPublisher').dataTable().fnClearTable();
	   $("#actualPublisher_printView tbody tr").remove();
	   
	   $('#trends_publisher').css("display","block");
	   $.ajax({
	 		       type : "POST",
	 		       url : "/actualPublisherData.lin",
	 		       cache: false,
	 		       data : {
	 					startDate : startDate,
	 					endDate : endDate,
	 					publisherName : title,
	 					channelName : allChannelName,
	 					selectedPublisher : selectedPublisher
	 				},	
	 		        dataType: 'json',
	 		        success: function (data) {
	 		        	jQuery('#actualPublisher').dataTable().fnClearTable();
	 		        	jQuery('#actualPublisher').dataTable().fnSettings()._iDisplayLength = 10;
		            	 jQuery('#actualPublisher').dataTable().fnDraw();

 		        	   
	 		           $.each(data, function(index, element) {	
	 		        	  if(index== 'actualPublisherDataList' && element.length > 0){
	 		        	   var dataList=data['actualPublisherDataList'];


//// for loop start/////	 		        	   
  var key = 0;
  if(dataList.length>0)
   {
	  for (var i = 0 ; i < dataList.length ; i = i + 1) {
		   (function(i) {
			   
		       setTimeout(function(i) {
		    	   
		           // code-here
	
 		        		  var dtoObject = dataList[key];

 		        			 jQuery('#actualPublisher').dataTable().fnAddData( [
      		        		     dtoObject.date,
      		        		     dtoObject.channelName,
      		        		     dtoObject.requests,
      		        		     dtoObject.impressionsDelivered,
      		        		     parseFloat(dtoObject.fillRate).toFixed(4)+"%",
      		        		     dtoObject.clicks,
      		        		     parseFloat(dtoObject.CTR).toFixed(4)+"%",
      		        		     "$"+parseFloat(dtoObject.revenue).toFixed(2),
      		        		     "$"+parseFloat(dtoObject.eCPM).toFixed(2),
      		        		     "$"+parseFloat(dtoObject.RPM).toFixed(2)
      		        		   ]);
 		        			 
 		        			 
 		        			var row = "<tr><td style='text-align: left;'>"+dtoObject.date+"</td> <td style='text-align: left;'>"+dtoObject.channelName+"</td> <td style='text-align: left;'>"+dtoObject.requests+"</td> "
        	          	       +"<td style='text-align: left;'>"+dtoObject.impressionsDelivered+"</td> <td style='text-align: left;'>"+parseFloat(dtoObject.fillRate).toFixed(4)+"%</td> <td style='text-align: left;'>"+dtoObject.clicks+"</td>"
        	          	       +"<td style='text-align: left;'>"+parseFloat(dtoObject.CTR).toFixed(4)+"</td> <td style='text-align: left;'>$"+parseFloat(dtoObject.revenue).toFixed(2)+"</td>"
        	          	       +"<td style='text-align: left;'>$"+parseFloat(dtoObject.eCPM).toFixed(2)+"</td> <td style='text-align: left;'>$"+parseFloat(dtoObject.RPM).toFixed(2)+"</td></tr>";
        		        		
        		        		$("#actualPublisher_printView tbody").append(row);
 		        			 
 		        			 
 		        			 key ++;
 		        			 if(key == dataList.length) {
 		        				$('#trends_publisher').css("display","none");
 		        			 }
	
	//for loop cont.../////
		       }, 10)
		   })(i)
		}	        	   
  //for loop end ...///// 
	 }
	 else {
		row = '<tr class="odd gradeX"><td></td>'
				+ '<td class="">No data found</td>'
				+ '<td></td><td></td></tr>';
		$("#actualPublisher tbody").append(row);
		$("#actualPublisher_printView tbody").append(row);
		$('#trends_publisher').css("display","none");
	}
	 		        	   
	 		        	   
	 		        	   
	 		        	   
	 		        	   
	 		        	  }
	 		          
	 		           });
	 		       },
	 		      complete: function(){
	 		    	 
		  		       },
                	  
	 		       error: function(jqXHR, exception) {
	 		    	 
	 		        }
	 			
	 		  });
	 		 
	}
  
  function actualLineGeneration() {
	 // angular.bootstrap($("#lineChartDiv"),['lineChartPublisherApp']);
	/*  	var divNameEcpm = "chart_div_left1";
	  	var divNameFillRate = "chart_div_left3";
		var divNameRevenue = "chart_div_left2";
		var divNameImp = "chart_div_right1";
		var divNameClick = "chart_div_right2";
		var divNameCtr = "chart_div_right3";
	  	lineChart(divNameEcpm, 'eCPM', "[['Date','eCPM'],['31',131.0]]", 'red',graphWidth,height);
		lineChart(divNameFillRate, 'FILL RATE(%)', "[['Date','FILL RATE(%)'],['31',131.0]]", 'red',graphWidth,height);
		lineChart(divNameRevenue, 'REVENUE', "[['Date','REVENUE'],['31',131.0]]", 'red',graphWidth,height);
		lineChart(divNameImp, 'IMPRESSION', "[['Date','Impressions'],['31',131.0]]", 'red',graphWidth,height);
		lineChart(divNameClick, 'CLICKS', "[['Date','Clicks'],['31',5]]", 'red',graphWidth,height);
		lineChart(divNameCtr, 'CTR', "[['Date','CTR'],['31',3.10]]", 'red',graphWidth,height);
	  try{
	 	$.ajax({
			type : "POST",
			url : "/acualLineChartPublisher.lin",
			cache : false,
			data : {
				startDate : startDate,
				endDate : endDate,
				channelName : allChannelName,
				selectedPublisher : selectedPublisher
			},	
			dataType : 'json',
			success : function(data) {
				
				var mapObj = data['headerMap'];
				if(mapObj !=null){
					var title = "";
					var ecpmStr = mapObj['eCPM'];
					var revenueStr = mapObj['revenue'];
					var fillRateStr = mapObj['fillRate'];
					var impressionStr = mapObj['impression'];
					var clicksStr = mapObj['click'];
					var ctrStr = mapObj['ctr'];
					
					lineChart(divNameEcpm, 'eCPM', ecpmStr, 'red',graphWidth,height);
					lineChart(divNameFillRate, 'FILL RATE(%)', fillRateStr, 'red',graphWidth,height);
					lineChart(divNameRevenue, 'REVENUE', revenueStr, 'red',graphWidth,height);
					lineChart(divNameImp, 'IMPRESSION', impressionStr, 'red',graphWidth,height);
					lineChart(divNameClick, 'CLICKS', clicksStr, 'red',graphWidth,height);
					lineChart(divNameCtr, 'CTR', ctrStr, 'red',graphWidth,height);
					$('#chart_div_left1_icon').attr("onclick","zoomInLineChart('eCPM','right',"+ecpmStr+",'chart_div_left1_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					$('#chart_div_left3_icon').attr("onclick","zoomInLineChart('FILL RATE(%)','left',"+fillRateStr+",'chart_div_left3_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					$('#chart_div_left2_icon').attr("onclick","zoomInLineChart('REVENUE','left',"+revenueStr+",'chart_div_left2_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					$('#chart_div_right1_icon').attr("onclick","zoomInLineChart('IMPRESSION','right',"+impressionStr+",'chart_div_right1_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					$('#chart_div_right2_icon').attr("onclick","zoomInLineChart('CLICKS','left',"+clicksStr+",'chart_div_right2_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					$('#chart_div_right3_icon').attr("onclick","zoomInLineChart('CTR','left',"+ctrStr+",'chart_div_right3_icon','red',"+modalheaderWidth+","+modalheaderHeight+");");
					
				}else{
					console.log("headerMap is null, check data or query");
				}
				
			},
			 
			error : function(jqXHR, exception) {
			}
		});
	 	
	  }catch(error){
	  }*/
	}
  
  function getSellThroughData(){
		var dataList;
		jQuery('#sellThroughTable').dataTable().fnClearTable();
	     $("#sellThroughTable-printView tbody tr").remove();
		
		try{
			var todayDate = getCurrentDate();
			if(sellThroughDataLowerDate != todayDate) {
		 $.ajax({
		       type : "POST",
		       url : "/sellThroughData.lin",
		       data : {
					startDate : startDate,
					endDate : endDate,
					selectedPublisherId : selectedPublisherId
				},	
		       cache: false,
		       dataType: 'json',
		       success: function (data) {
		    	   jQuery('#sellThroughTable').dataTable().fnClearTable();
		    	   jQuery('#sellThroughTable').dataTable().fnSettings()._iDisplayLength = 10;
		    	   jQuery('#sellThroughTable').dataTable().fnDraw();
		           $.each(data, function(index, element) {
		        	   if(index=='sellThroughDataList'){
		        		  dataList=data['sellThroughDataList'];
		        		  sellThroughDataLowerDate = todayDate;
				    	  sellThroughData = dataList;
		        		  aggrSellThroughRate = data['aggrSellThroughRate'];
		        		  aggregateSellThroughRate = aggrSellThroughRate;
		        		  updateDonutChart(aggrSellThroughRate ,'container' );
		        		  updateDonutChart(aggrSellThroughRate ,'container_printView' );
		        		  
//// for loop start/////	 		        	   
  var key = 0;
  if(dataList != null && dataList.length > 0){
  for (var i = 0 ; i < dataList.length ; i = i + 1) {
	   (function(i) {
		     setTimeout( function(i) {
	    	   
	           // code-here
			        		  
			        		     var id="sellThroughRate_"+key;	 		        		  
			        		     var dtoObject = dataList[key];
					        		     
			        		     var newRowIndex = jQuery('#sellThroughTable').dataTable().fnAddData( [
           		        		     dtoObject.property,
           		        		     dtoObject.adUnit,
           		        		     formatInt(dtoObject.forecastedImpressions),
           		        		     formatInt(dtoObject.availableImpressions),
           		        		     formatInt(dtoObject.reservedImpressions),
           		        		     formatFloat(dtoObject.sellThroughRate,4)+"%"
           		        		   ]);
			        		     var tr = jQuery('#sellThroughTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
			        		     	tr.setAttribute('id', id);
			        		     	tr.setAttribute("style","cursor: hand; cursor: pointer;");
           		        		   	tr.setAttribute("onclick","updateDonutChart('"+dtoObject.sellThroughRate+"','container'); changeRowColor('"+id+"');");           		        		   
			        	  
           		        		var row = "<tr><td style='text-align: left;'>"+dtoObject.property+"</td> <td style='text-align: left;'>"+dtoObject.adUnit+"</td> <td style='text-align: left;'>"+formatInt(dtoObject.forecastedImpressions)+"</td> "
           	          	       +"<td style='text-align: left;'>"+formatInt(dtoObject.availableImpressions)+"</td> <td style='text-align: left;'>"+formatInt(dtoObject.reservedImpressions)+"</td> <td style='text-align: left;'>"+formatFloat(dtoObject.sellThroughRate,4)+"%</td> </tr>";
           		        		
           		        		$("#sellThroughTable-printView tbody").append(row);	
//for loop cont.../////	 		        		  
      	 		        		  key ++;
       }, 10)
   })(i)

}	        	   
//for loop end .../////   	
  
	        	  
  }        	  
			        	  
			        	  
			        	  
		        	   }
		        	   
		           });
		       },
		     
		       error: function(jqXHR, exception) {
		        }
			
		  });
		}
			else {
				jQuery('#sellThroughTable').dataTable().fnClearTable();
				jQuery('#sellThroughTable').dataTable().fnSettings()._iDisplayLength = 10;
		    	jQuery('#sellThroughTable').dataTable().fnDraw();
				dataList = sellThroughData;
				updateDonutChart(aggregateSellThroughRate ,'container' );
				updateDonutChart(aggrSellThroughRate ,'container_printView' );
				//// for loop start/////	 		        	   
				  var key = 0;
				  if(dataList != null && dataList.length > 0){
				  for (var i = 0 ; i < dataList.length ; i = i + 1) {
					   (function(i) {
						     setTimeout( function(i) {
					    	 var id="sellThroughRate_"+key;	 		        		  
		        		     var dtoObject = dataList[key];
				        		     
		        		     var newRowIndex = jQuery('#sellThroughTable').dataTable().fnAddData( [
	   		        		     dtoObject.property,
	   		        		     dtoObject.adUnit,
	   		        		     formatInt(dtoObject.forecastedImpressions),
	   		        		     formatInt(dtoObject.availableImpressions),
	   		        		     formatInt(dtoObject.reservedImpressions),
	   		        		     formatFloat(dtoObject.sellThroughRate,4)+"%"
	   		        		   ]);
		        		     var tr = jQuery('#sellThroughTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
		        		     	tr.setAttribute('id', id);
		        		     	tr.setAttribute("style","cursor: hand; cursor: pointer;");
	   		        		   	tr.setAttribute("onclick","updateDonutChart('"+dtoObject.sellThroughRate+"','container'); changeRowColor('"+id+"');");           		        		   
		        	  		  
	   		        		 var row = "<tr><td style='text-align: left;'>"+dtoObject.property+"</td> <td style='text-align: left;'>"+dtoObject.adUnit+"</td> <td style='text-align: left;'>"+formatInt(dtoObject.forecastedImpressions)+"</td> "
         	          	       +"<td style='text-align: left;'>"+formatInt(dtoObject.availableImpressions)+"</td> <td style='text-align: left;'>"+formatInt(dtoObject.reservedImpressions)+"</td> <td style='text-align: left;'>"+formatFloat(dtoObject.sellThroughRate,4)+"%</td> </tr>";
         		        		
	   		        		 $("#sellThroughTable-printView tbody").append(row);
	   		        		   	
	 		        		  key ++;
				       }, 10)
				   })(i)

				}
			
				//for loop end .../////
				  
				  }
			}
		 
		}catch(error){
		}	
		 
	 }

  
	  function getPublisherDropDown(){
		  
		  $("#publisherDropDown ul").empty();
		  $("#publisherTrendsDropDown ul").empty();
		  $("#publisherAllocationDropDown ul").empty();
		  
		  var jsonResponse;
		  var listData = "";
		  var listDataTrends="";
		  var listDataAllocation="";
		  try{
			 		 $.ajax({
			 		       type : "POST",			 		   
			 		       url : "/loadPublisher.lin",
			 		       cache: false,
			 		       dataType: 'json',
			 		       success: function (data) {	    	  
			 		           $.each(data, function(index, element) {	
			 		        	   
			 		        	  if (index == 'publisherList') {			 		        		  
										jsonResponse=element;
										$.each(element,function(newIndex,newElement) {	
	             			         	   var id='publisher_val_'+newIndex;
	          			        		   listData=listData+"<li><a href=javascript:selectPublisher('"+
	          			        		   id+"')><span id='"+id+"'>"+
	          		         		             newElement+"</span></a></li>";
	             			         		
	          			        		   var id2='publisher_trends_val_'+newIndex;
	          			        		   listDataTrends=listDataTrends+"<li><a href=javascript:selectPublisherTrends('"+
	          			        		   id2+"')><span id='"+id2+"'>"+
	          		         		             newElement+"</span></a></li>";
	          			        		  
	          			        		  var id3='publisher_allocation_val_'+newIndex;	   
	          			        		  listDataAllocation=listDataAllocation+"<li><a href=javascript:selectPublisherAllocation('"+
	          			        		   id3+"')><span id='"+id3+"'>"+
	          		         		             newElement+"</span></a></li>";	             			         		   
	          			        		 
										  });	
										 $("#publisherDropDown ul").append(listData);
										 $("#publisherTrendsDropDown ul").append(listDataTrends);
										 $("#publisherAllocationDropDown ul").append(listDataAllocation);
										 var firstName=jsonResponse[0];
									     if(firstName != undefined && firstName !=null){
									        $("#publisher_val").html(firstName);
									        $("#publisher_trends_val").html(firstName);
									        $("#publisher_allocation_val").html(firstName);
									     }else{
									        $("#publisher_val").html('All');
									        $("#publisher_trends_val").html('All');
									        $("#publisher_allocation_val").html('All');
									        
									     }	
								   }
			 		        			

			 		           });
			 		          
			 		       },
			 		       error: function(jqXHR, exception) {
			 		        }
			 			
			 		  });
		  }catch(error){
		  }		 		 
	}
	  
	function loadPublisherHeader(publisherName){
		  var headerDiv="";
		  try{
			 		 $.ajax({
			 		       type : "POST",			 		   
			 		       url : "/loadPublisher.lin",
			 		       cache: false,
			 		       data : {publisherName:publisherName},
			 		       dataType: 'json',
			 		       success: function (data) {	    	  
			 		           $.each(data, function(index, element) {
			 		        	  if (index == 'channelPerformanceList') {
										jsonResponse=element;																			
								  }	

			 		           });
			 		          
			 		          headerDiv= getPublisherInventrySummaryHeader(
			 		        		// jsonResponse[0].sites,
			 		        		// jsonResponse[0].mobileWeb,
			 		        		// jsonResponse[0].mobileApp,
			 		        		 21, 23,15,
			 		        		 jsonResponse[0].impressionsDelivered,
			 		        		 jsonResponse[0].clicks,
			 		        		 jsonResponse[0].CTR,
			 		        		 jsonResponse[0].eCPM,
			 		        		 jsonResponse[0].payout);
			 				  $('#publisher_inventry_tab_header').html(headerDiv);
			 		       },
			 		      
			 		       error: function(jqXHR, exception) {
			 		        }
			 			
			 		  });
		  }catch(error){
		  }		 		 
	}
	
	function getPublisherInventrySummaryHeader(sites,mobileWeb,mobileApp,impressionsDelivered,clicks,CTR,eCPM,payout){
		var headerDivData="";
		headerDivData=headerDivData
	    +'<div class="summary_bar"><div  style="">SITES</div><br>'
		  +'<div class="summary_value">'+sites+'</div></div>'
	    +'<div class="summary_bar"><div >MOBILE WEB</div><br>'
		  +'<div class="summary_value">'+mobileWeb+'</div></div>'
	    +'<div class="summary_bar"><div  style="">MOBILE APP</div><br>'
		  +'<div class="summary_value">'+mobileApp+'</div></div>'
	    +'<div class="summary_bar"><div  style="">IMPRESSIONS DELIVERED</div><br>'
	    +'<div class="summary_value">'+impressionsDelivered+'</div></div>'
	    +'<div class="summary_bar"><div  style="">CLICKS</div><br>'
	    +'<div class="summary_value">'+clicks+'</div></div>'
	    +'<div class="summary_bar"><div  style="">CTR(%)</div><br>'
	    +'<div class="summary_value">'+CTR+'%</div></div>'
	    +'<div class="summary_bar"><div  style="">eCPM(Estimated)</div><br>'
	    +'<div class="summary_value">$'+eCPM+'</div></div>'
	    +'<div class="summary_bar"><div  style="">PAYOUTS(Estimated)</div><br>'
	    +'<div class="summary_value">$'+payout+'</div></div>';
		
		return headerDivData;
	}
		
	function getPublisherInventoryRevenueHeader(site,delivered,mobileApp,mobileWeb,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
		+'<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 0%;">'
		
		+'<div class="summary_bar" >'
		+'<div style="">SITES</div>'
		+'<div class="summary_value">'+ formatInt(site) + ' </div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'<div style="">IMPRESSIONS DELIVERED</div>'
		+'<div class="summary_value">'+ formatInt(delivered) + ' </div>'
		+'</div>'
		
		+'<div class="summary_bar" >'
		+'<div style="">CLICKS</div>'
		+'<div class="summary_value">'+ formatInt(clicks) + ' </div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'<div style="">CTR</div>'
		+'<div class="summary_value">'+ formatFloat(CTR,4) + ' %</div>'
		+'</div>'
		+'<div class="summary_bar">'
		+'<div style="">eCPM &nbsp;&nbsp;(Estimated)</div>'
		+'<div class="summary_value">$'+ formatFloat(eCPM,2) + ' </div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'<div style="">RPM &nbsp;&nbsp;(Estimated)</div>'
		+'<div class="summary_value">$'+ formatFloat(RPM,2) + ' </div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'<div style="">PAYOUTS&nbsp;&nbsp;(Estimated)</div>'
		+'<div class="summary_value">$'+ formatFloat(payOuts,2) + ' </div>'
		+'</div>'
		+'</div>'
		
		+'<div class="inner-spacer" id="c1" style="float:right;width:200px;height:105px; z-index:1000;margin-top:-50px; ">'
		+'</div>';


		return headerDivData;
		
	}
	
/*	function getPublisherInventoryRevenueHeader_printView1(site,delivered,mobileApp,mobileWeb,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
		+'<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 0%;">'
		+'<table>'
		
		+'<tr>'
		+'<td style="color:white;">SITES</td>'
		
		+'<td style="margin-left:100px;color:white;">IMPRESSIONS DELIVERED</td>'
		
		+'<td style="margin-left:100px;color:white;">CLICKS</td>'
		
		+'<td style="margin-left:100px;color:white;">CTR</td>'
		
		+'<td style="margin-left:100px;color:white;">eCPM &nbsp;&nbsp;(Estimated)</td>'
		
		+'<td style="margin-left:100px;color:white;">RPM &nbsp;&nbsp;(Estimated)</td>'
		
		+'<td style="margin-left:100px;color:white;">PAYOUTS&nbsp;&nbsp;(Estimated)</td>'
		+'</tr>'
		
		
		+'<tr>'
		+'<td style="color:white;">'+ formatInt(site) + '</td>'
		
		+'<td style="margin-left:100px;color:white;">'+ formatInt(delivered) + '</td>'
		
		+'<td style="margin-left:100px;color:white;">'+ formatInt(clicks) + '</td>'
		
		+'<td style="margin-left:100px;color:white;">'+ formatFloat(CTR,4) + '%</td>'
		
		+'<td style="margin-left:100px;color:white;">$'+ formatFloat(eCPM,2) + '</td>'
		
		+'<td style="margin-left:100px;color:white;">$'+ formatFloat(RPM,2) + '</td>'
		
		+'<td style="margin-left:100px;color:white;">$'+ formatFloat(payOuts,2) + '</td>'
		+'</tr>'
		
		
		+'</table>'
		+'</div>'
		
		+'<div class="inner-spacer" id="c1_printView" style="float:right;width:200px;height:105px; z-index:1000;margin-top:-50px; ">'
		+'</div>';


		return headerDivData;
		
	}*/
	
	function getPublisherInventoryRevenueHeader_printView(site,delivered,mobileApp,mobileWeb,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
		+'<div class="" style="width:80%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 0%;">'
		+'<table>'
		+'<tr>'
		
		+'<td style="width:100px">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">SITES</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">'+ formatInt(site) + '</td>'
				+'</tr>'
			+'</table>'
		+'</td>'
		
		+'<td style="width:200px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">IMPRESSIONS DELIVERED</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">'+ formatInt(delivered) + '</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'<td style="width:100px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">CLICKS</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">'+ formatInt(clicks) + '</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'<td style="width:100px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">CTR</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">'+ formatFloat(CTR,4) + '%</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'<td style="width:150px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">eCPM</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">$'+ formatFloat(eCPM,2) + '</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'<td style="width:150px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">RPM</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">$'+ formatFloat(RPM,2) + '</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'<td style="200px">'
		+'<table>'
			+'<tr>'
				+'<td style="color:white;">PAYOUTS</td>'
				+'</tr>'
				+'<tr>'
				+'<td style="color:white;">$'+ formatFloat(payOuts,2) + '</td>'
			+'</tr>'
		+'</table>'
		+'</td>'
		
		+'</tr>'
		+'</table>'
		+'</div>'
		
		+'<div class="inner-spacer" id="c1_printView" style="float:right;width:100px;height:105px; z-index:1000;margin-top:-10px; ">'
		+'</div>';


		return headerDivData;
		
	}
	
	
	
	function getPublisherTrendsAnalysisHeader(requests,delivered,fillRate,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
	  	+'<div class="" style="width:98%;float:left;border-radius: 4px 4px 4px 4px;margin-left:0%;">'
		+'<div class="summary_bar" >'
		+'	<div style="font-size:1em;">REQUESTS</div>'
		+'	<div class="summary_value"> '+ formatInt(requests) + ' </div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">DELIVERED</div>'
		+'	<div class="summary_value">'+formatInt(delivered)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">FILL RATE(%)</div>'
		+'	<div class="summary_value">'+formatFloat(fillRate,4)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">IMPRESSIONS</div>'
		+'	<div class="summary_value">'+formatInt(delivered)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">CLICKS</div>'
		+'	<div class="summary_value">'+formatInt(clicks)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">CTR</div>'
		+'	<div class="summary_value">'+formatFloat(CTR,4)+ '%</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">eCPM</div>'
		+'	<div class="summary_value">$'+formatFloat(eCPM,2)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">RPM</div>'
		+'	<div class="summary_value">$'+formatFloat(RPM,2)+ '</div>'
		+'</div>'
		
		+'<div class="summary_bar">'
		+'	<div style="font-size:1em;">PAYOUTS</div>'
		+'	<div class="summary_value">$'+formatFloat(payOuts,2)+ '</div>'
		+'</div>'
		+'</div>'
		+'<div class="inner-spacer" id="c2" style="float:right;width:300px;height:105px;z-index:1000;margin-top: -42px;">'
		+'</div>';

		
	return headerDivData;
		
	}
	
	
	function getPublisherTrendsAnalysisHeader_printView(requests,delivered,fillRate,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
	  	+'<div class="" style="width:100%;float:left;border-radius: 4px 4px 4px 4px;margin-left:0%;">'
	  	+'<table>'
		+'<tr>'
		
			+'<td style="width:10%">'
				+'<table>'
					+'<tr>'
						+'<td style="color:white;">REQUESTS</td>'
					+'</tr>'
					+'<tr>'
						+'<td style="color:white;">'+formatInt(requests)+'</td>'
					+'</tr>'
				+'</table>'
			+'</td>'
			
				+'<td style="width:10%">'
					+'<table>'
						+'<tr>'
							+'<td style="color:white;">DELIVERED</td>'
						+'</tr>'
						+'<tr>'
							+'<td style="color:white;">'+formatInt(delivered)+ '</td>'
						+'</tr>'
					+'</table>'
				+'</td>'
					
		
			+'<td style="width:10%">'
				+'<table>'
					+'<tr>'
						+'<td style="color:white;">FILL RATE(%)</td>'
					+'</tr>'
					+'<tr>'
						+'<td style="color:white;">'+formatFloat(fillRate,4)+'</td>'
					+'</tr>'
				+'</table>'
			+'</td>'
	
			+'<td style="width:10%">'
				+'<table>'
					+'<tr>'
						+'<td style="color:white;">IMPRESSIONS</td>'
					+'</tr>'
					+'<tr>'
						+'<td style="color:white;">'+formatInt(delivered)+'</td>'
					+'</tr>'
				+'</table>'
			+'</td>'
			
			+'<td style="width:8%">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">CLICKS</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">'+formatInt(clicks)+'</td>'
				+'</tr>'
			+'</table>'
			+'</td>'	
						
			+'<td style="width:8%">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">CTR</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">'+formatFloat(CTR,4)+ '% </td>'
				+'</tr>'
			+'</table>'
			+'</td>'
			
			+'<td style="width:8%">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">eCPM</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">$'+formatFloat(eCPM,2)+'</td>'
				+'</tr>'
			+'</table>'
			+'</td>'
			
			+'<td style="width:8%">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">RPM</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">$'+formatFloat(RPM,2)+'</td>'
				+'</tr>'
			+'</table>'
			+'</td>'
			
			+'<td style="width:8%">'
			+'<table>'
				+'<tr>'
					+'<td style="color:white;">PAYOUTS</td>'
				+'</tr>'
				+'<tr>'
					+'<td style="color:white;">$'+formatFloat(payOuts,2)+'</td>'
				+'</tr>'
			+'</table>'
			+'</td>'

		+'</tr>'
		+'</table>'
		
		+'<div class="inner-spacer" id="c2_printView" style="float:right;width:200px;height:100px;z-index:1000;margin-top: -25px;">'
		+'</div>'
		+'</div>';

		
	return headerDivData;
		
	}
	
		
	function loadPublisherAllocationHeader(publisherName){
		  var headerDiv="";		 
		  try{
			 		 $.ajax({
			 		       type : "POST",			 		   
			 		       url : "/loadPublisher.lin",
			 		       cache: false,
			 		       data : {publisherName:publisherName},
			 		       dataType: 'json',
			 		       success: function (data) {	    	  
			 		           $.each(data, function(index, element) {
			 		        	  if (index == 'channelPerformanceList') {
										jsonResponse=element;																			
								  }	

			 		           });
			 		          
			 		          headerDiv= getPublisherAllocationHeader(			 		        			 		        		
			 		        		 jsonResponse[0].requests,
			 		        		 jsonResponse[0].impressionsDelivered,
			 		        		 jsonResponse[0].fillRate,
			 		        		 jsonResponse[0].clicks,
			 		        		 jsonResponse[0].CTR,
			 		        		 jsonResponse[0].revenue,
			 		        		 jsonResponse[0].eCPM,
			 		        		 jsonResponse[0].RPM);
			 				  $('#publisher_allocation_header').html(headerDiv);
			 		       },
			 		      
			 		       error: function(jqXHR, exception) {
			 		        }
			 			
			 		  });
		  }catch(error){
		  }		 		 
	}
	
	
	
	function getPublisherAllocationHeader(requests,impressionsDelivered,fillRate,clicks,CTR,revenue,eCPM,RPM){
		var headerDivData="";
		headerDivData=headerDivData
	   
	    +'<div class="summary_bar"><div >REQUESTS</div><br>'
		+'<div class="summary_value">'+requests+'</div></div>'
	    +'<div class="summary_bar"><div  style="">DELIVERED</div><br>'
	    +'<div class="summary_value">'+impressionsDelivered+'</div></div>'
	    +'<div class="summary_bar"><div  style="">FILL RATE</div><br>'
	    +'<div class="summary_value">'+fillRate+'</div></div>'
	    +'<div class="summary_bar"><div  style="">CLICKS</div><br>'
	    +'<div class="summary_value">'+clicks+'</div></div>'
	    +'<div class="summary_bar"><div  style="">REVENUE($)</div><br>'
	    +'<div class="summary_value">$'+revenue+'</div></div>'
	    +'<div class="summary_bar"><div  style="">CTR(%)</div><br>'
	    +'<div class="summary_value">'+CTR+'%</div></div>'
	    +'<div class="summary_bar"><div  style="">eCPM(Estimated)</div><br>'
	    +'<div class="summary_value">$'+eCPM+'</div></div>'
	    +'<div class="summary_bar"><div  style="">RPM</div><br>'
	    +'<div class="summary_value">$'+RPM+'</div></div>';
		return headerDivData;
	}
	
function viewPerformance(channelName){	
		$("#tre_ana_campaign").css({'display':'none'});
		$('#myTab1 li:eq(2)').css({'display':'inline'});
		$('#myTab1 li:eq(2) a').tab('show');
		$("#tre_ana").css({'display':'inline'});
		/*allChannelName=channelName;
		$(".alert-info-header").remove();
		$("#second_channel").after("<div class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"+ channelName+"</strong> </div></div>");
    	   	
		getActualPublisherData();
		actualLineGeneration();*/
}

function reallcation(channelName){
		$('#myTab1 li:eq(6)').css({'display':'inline'});
		$('#myTab1 li:eq(6) a').tab('show');
		//loadChannelByName(channelName);
}

function getAllLineGraphsForPublisherTrendsAnalysis(mapObj) {

	var title = "";
	var ecpmStr = mapObj['eCPM'];
	var revenueStr = mapObj['revenue'];
	var fillRateStr = mapObj['fillRate'];
	var impressionStr = mapObj['impression'];
	var clicksStr = mapObj['click'];
	var ctrStr = mapObj['ctr'];
	var divNameEcpm = "chart_div_left1";
	var divNameFillRate = "chart_div_left3";
	var divNameRevenue = "chart_div_left2";
	var divNameImp = "chart_div_right1";
	var divNameClick = "chart_div_right2";
	var divNameCtr = "chart_div_right3";	
	
	lineChart(divNameEcpm, 'eCPM', ecpmStr, 'green');
	lineChart(divNameFillRate, 'FILL RATE(%)', fillRateStr, 'red');
	lineChart(divNameRevenue, 'REVENUE', revenueStr, 'red');
	lineChart(divNameImp, 'IMPRESSION', impressionStr, 'blue');
	lineChart(divNameClick, 'CLICKS', clicksStr, 'red');
	lineChart(divNameCtr, 'CTR', ctrStr, 'blue');
	
}

	  
	  function showPopup(title) {																							
		  $("#popoverData").attr('data-original-title', "<span style='font-size:14px;font-weight:bold;'>"+title+"</span></br><span style='color:#9ac050;font-size:14px;'><span style='margin-left:2px;'><img src='img/Arrow2Up.png'width='15'></span><span style='margin-left:10px;margin-top:2px;color:#52ad00;'>6.12%</span></span>");
		  $("#popoverData").click();
		  }

		  function changePopOverHtml() {
		  	var popOverHtml = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; '>"+
		  	"<div id='popheading' style='background-color:#FDEFBC;'><span style='font-weight:bold;color:black;margin-left:12px;'><b>Booked Impressions:</b></span><span style='margin-left:20px;'>585,000</span><span style='font-weight:bold;color:black;margin-left:135px'><b>CPM:</b></span><span style='margin-right:5px;float:right;'>$5.78</span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;'><span style='margin-left:255px;'><b>Life Time</b></span><span style='float:right;'><b>This Week</b></span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;'><span style='font-weight:bold;color:black;'>Impressions Delivered:</span><span style='margin-left:112px;'>283,200</span><span style='float:right;'>9,258</span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;'><span style='font-weight:bold;color:black;'>Clicks:</span><span style='margin-left:232px;'>2,442</span><span style='float:right;'>91</span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;'><span style='font-weight:bold;color:black;'>CTR(%):</span><span style='margin-left:220px;'>0.80%</span><span style='float:right;'>0.98%</span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;'><span style='font-weight:bold;color:black;'>Revenue Recognised:</span><span style='margin-left:112px;'>$1,636.90</span><span style='float:right;'>$53.31</span></div>"+
		  	  "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;'><span style='font-weight:bold;color:black;'>Revenue Left:</span><span style='margin-left:164px;'>$1,744.40</span><span style='float:right;'>$1,690.88</span></div>"+
		  	  "<div id='chart_div' style='width: 50px; height: 50px;'></div>"+
		  	  "<div style='margin-top:157px;background-color:#FDEFBC;'>"+
		  		"<a class='btn btn-inverse medium' href='#' data-toggle='tab' style='margin-bottom:8px;margin-left:10px;margin-top:10px;' onclick='viewperformance()'>View Trends</a>"+
		  		"<a class='btn btn-inverse medium' href='#' data-toggle='tab' style='margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;' onclick='reallcation()'>Reallocation</a>"+
		  	  "</div>"+
		      "</div>";
		  	$('#popover_content_wrapper').html(popOverHtml);
		  }
  
  function reallocationChart(){
		try{
		 var chartData = google.visualization.arrayToDataTable([
				[ 'Days', 'Mojiva', 'Nexage', 'Google Ad Exchange' ],
				[ '21', 0.25, 0.23, 0.21 ], [ '22', 0.24, 0.22, 0.19 ],
				[ '23', 0.29, 0.24, 0.23 ], [ '24', 0.28, 0.26, 0.27 ],
				[ '25', 0.29, 0.21, 0.24 ], [ '26', 0.28, 0.26, 0.21 ],
				[ '27', 0.31, 0.28, 0.26 ]

		]);
		var ChartOptions= {
	            title: 'eCPM ($)',
	            width: 1000,
	            height: 400,
	            hAxis: {title: 'Date',  titleTextStyle: {color: 'red'}},
	 	        legend:{position: 'none'}
	         };
		
		


		chartData = google.visualization.arrayToDataTable([
				     [ 'Days', 'Mojiva', 'Nexage', 'Google Ad Exchange' ],
				     [ '21', 92.85, 91.80, 92.75 ], [ '22', 93.86, 92.89, 96.26 ],
				     [ '23', 93.06, 93.00, 93.86 ], [ '24', 93.17, 93.57, 93.10 ],
				     [ '25', 91.44, 93.24, 95.24 ], [ '26', 93.19, 93.01, 93.09 ],
				     [ '27', 92.75, 92.25, 92.15 ]
				   ]);
		options = { title: 'FILL RATE (%)', width: 1000, height: 400, 
				    hAxis:{title: 'Date', titleTextStyle: {color: 'red'}}, 
				    legend:{position: 'none'} 
				   };
		google.setOnLoadCallback(drawAreaChart(chartData,ChartOptions,"chart_div_realloc2"));
		
	}catch(error){
		//alert("reallocationChart: error:"+error);
	}
 }

  
  
  function checkFlag_campain()
	 {
		 if(optimizingFlagCampain == 4
			&& campaignTotalCurrentData != null 
			&& campaignTotalCompareData != null
			&& campaignTotalMTDData != null
			&& campaignTotalCurrentData.length == 0 
			&& campaignTotalCompareData.length == 0
			&& campaignTotalMTDData.length == 0) {
			 
		}
		 else if(optimizingFlagCampain == 4)
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
  
  function getOptimizedInventoryRevenueHeaderData() {
	  
	  if(publisherSummaryCurrentData != null && publisherSummaryCompareData != null && publisherSummaryMTDData != null)
	  {
		  var selectedChannels = allChannelName.split(",");
		  var arrayLength = selectedChannels.length;
		  
		  var maxSite = 0;
		  var totalECPM = 0.0;
		  var totalEmpDelivered = 0;
		  var totalClicks = 0;
		  var totalCTR = 0.0;
		  var totalRPM = 0.0;
		  var totalPayouts = 0.0;
		  var totalRequests = 0;
		  var totalFillRate = 0;
		  
		  var fillPercentage = 0.0;
		  var houseImpression = 0;
		  var allChannelsTotalEmpDelivered = 0;
		  var newFlag = 0;
		  
		  for(i = 0; i < arrayLength; i++)
		  {
			  for (key in publisherSummaryCurrentData)
			  {
				  newFlag++;
					  var dtoObject = publisherSummaryCurrentData[key];
					  if(selectedChannels[i] == dtoObject.channelName)
					  {
						  if(dtoObject.channelName == "House")
						  {
							  houseImpression =  dtoObject.impressionsDelivered;
						  }
						  
						  if(dtoObject.site > maxSite)
						  {
							  maxSite = dtoObject.site;
						  }
							  
						  totalEmpDelivered = totalEmpDelivered + dtoObject.impressionsDelivered;
						  totalClicks = totalClicks + dtoObject.clicks;
						  totalPayouts = totalPayouts + dtoObject.payOuts;
						  totalRequests = totalRequests + dtoObject.requests; 
					  }
					  
					  if(i == 0){
						  allChannelsTotalEmpDelivered = allChannelsTotalEmpDelivered + dtoObject.impressionsDelivered;  
					  }
			  }
			  
		  }
		  

		  	  totalCTR =  (totalClicks / totalEmpDelivered) * 100;
		  	  totalECPM = (totalPayouts / totalEmpDelivered) * 1000;
		  	  
				if(totalRequests == 0)
				{
					totalRPM = 0;
				}
				else
				{
					totalRPM = (totalPayouts / totalRequests) * 1000;
				}
				
				if(totalRequests != 0) {
					totalFillRate = (totalEmpDelivered / totalRequests) * 100;
				}
	
				fillPercentage = ((totalEmpDelivered - houseImpression) / allChannelsTotalEmpDelivered) * 100;					

				if(newFlag > 0)
				{
					maxSite = 23;
				}				
		  
		  headerDiv = getPublisherInventoryRevenueHeader(
				  maxSite,
				  totalEmpDelivered,
				  "",
				  "",
				  totalClicks,
				  totalCTR,
				  totalECPM,
				  totalRPM,
	        	  totalPayouts);
		  
		  headerDiv_printView = getPublisherInventoryRevenueHeader_printView(
				  maxSite,
				  totalEmpDelivered,
				  "",
				  "",
				  totalClicks,
				  totalCTR,
				  totalECPM,
				  totalRPM,
	        	  totalPayouts);
			
			$('#publisher_inventory_revenue_header').html(headerDiv);
			updateDonutForInventoryHeader(fillPercentage, 'c1');
			
			$('#publisher_inventory_revenue_header_printView').html(headerDiv_printView);
			updateDonutForInventoryHeader(fillPercentage, 'c1_printView');
			  
			  
		  headerDiv = getPublisherTrendsAnalysisHeader(
				  totalRequests,
				  totalEmpDelivered,
				  totalFillRate,
				  totalClicks,
				  totalCTR,
				  totalECPM,
				  totalRPM,
	        	  totalPayouts);
		  
		  
		  headerDiv_printView = getPublisherTrendsAnalysisHeader_printView(
				  totalRequests,
				  totalEmpDelivered,
				  totalFillRate,
				  totalClicks,
				  totalCTR,
				  totalECPM,
				  totalRPM,
	        	  totalPayouts);
			
		  $('#publisher_trends_analysis_header').html(headerDiv);
		  updateDonutForTrendAnalysisHeader(totalFillRate, 'c2' );
		  
		  $('#publisher_trends_analysis_header_printView').html(headerDiv_printView);
		  updateDonutForTrendAnalysisHeader(totalFillRate, 'c2_printView' );
		  
	  }
	  else
	  {
		  headerDiv= getPublisherInventoryRevenueHeader(
     		  	 		"0",
     		  	 		"0",
     		  	 		"0",
     		  	 		"0",
     		  	 		"0",
     		  	 		"0.0000",
     		  	 		"0.0000",
     		  	 		"0.0000",
	        			"0.0000");
		  
		  
		  headerDiv_printView= getPublisherInventoryRevenueHeader_printView(
		  	 		"0",
		  	 		"0",
		  	 		"0",
		  	 		"0",
		  	 		"0",
		  	 		"0.0000",
		  	 		"0.0000",
		  	 		"0.0000",
      				"0.0000");
			
			$('#publisher_inventory_revenue_header').html(headerDiv);
			  updateDonutForInventoryHeader(0.0, 'c1');
			  
			  $('#publisher_inventory_revenue_header_printView').html(headerDiv_printView);
			  updateDonutForInventoryHeader(0.0, 'c1_printView');
			  
			  
		  headerDiv= getPublisherTrendsAnalysisHeader(
					"0",
					"0",
					"0.0000",
					"0",
					"0.0000",
					"0.0000",
					"0.0000",
					"0.0000");
		  
		  headerDiv_printView= getPublisherTrendsAnalysisHeader_printView(
					"0",
					"0",
					"0.0000",
					"0",
					"0.0000",
					"0.0000",
					"0.0000",
					"0.0000");
		  
			
		  	$('#publisher_trends_analysis_header').html(headerDiv);
		  	updateDonutForTrendAnalysisHeader(0.0, 'c2');
		  	
		  	$('#publisher_trends_analysis_header_printView').html(headerDiv_printView);
		  	updateDonutForTrendAnalysisHeader(0.0, 'c2_printView');
		  	
	  }
  }
 
 /*function selectAllFilterCheckbox() {
		 $(':checkbox[name="checkMr"]').each (function () {
	    	$('#'+this.id+'-span').attr("class","checked");
			$('#'+this.id).attr("checked","checked");
			
	     });
 }
 
 function unselectAllFilterCheckbox() {
		 $(':checkbox[name="checkMr"]').each (function () {
		 	$('#'+this.id+'-span').removeAttr("class");
			$('#'+this.id).removeAttr("checked");
		 });
 }
 
 function getchannelTrendsDropDown(allPublishersList) {
	 var ulContents = "";
	 $("#channelsDropDown ul").empty();
	 if(allPublishersList != null && allPublishersList.length > 0){
		 $('#pub_title').html(allPublishersList[0].value);
		 selectedPublisher = allPublishersList[0].value;
		 selectedPublisherId = allPublishersList[0].id;
	 }
	 if(allPublishersList != null && allPublishersList.length > 1){
		 for(j=1;j<allPublishersList.length;j++) {
			 ulContents = ulContents + "<li><a href=\"javascript:loadFilterDataByPublisher('"+allPublishersList[j].id.trim()+"');\" onclick=\"changeDropdown(this)\" style=\"color:black;\">" + allPublishersList[j].value + "</a></li>";
		 }
	 }
	 $("#channelsDropDown ul").append(ulContents);
 }
	 
function getChannels(channelsNameList) {
	var divContents = "";
    var channelNameArray = channelsNameList[0].split(",");
    allChannelName = channelsNameList[0];
   	for(j=0; j<channelNameArray.length; j++) {
   		divContents = divContents 
   		+'<label class="checkbox" onclick="toggleFilterCheckbox(\''+j+'\');" style="color:#fff; margin-left:5px;">'
        +'<div class="checker" id="">'
        +'<span id="optionsCheckbox-'+j+'-span" class="checked" name="one">'
        +'<input type="checkbox" name="checkMr" id="optionsCheckbox-'+j+'" value="option6" checked="checked">'
        +'</span>'
        +'</div>'
        +channelNameArray[j].trim()
   		+'</label>';
   	}
  return divContents;
}

function getDefaultChannels(channelsNameList) {
	var divContents = "";
	var publisherViewHeaderChannelDivs = '';
    var channelNameArray = channelsNameList[0].split(",");
    allChannelName = channelsNameList[0];
   	for(j=0; j<channelNameArray.length; j++) {
   		divContents = divContents 
   		+'<label class="checkbox" onclick="toggleFilterCheckbox(\''+j+'\');" style="color:#fff; margin-left:5px;">'
        +'<div class="checker" id="">'
        +'<span id="optionsCheckbox-'+j+'-span" class="checked" name="one">'
        +'<input type="checkbox" name="checkMr" id="optionsCheckbox-'+j+'" value="option6" checked="checked">'
        +'</span>'
        +'</div>'
        +channelNameArray[j].trim()
   		+'</label>';
   		publisherViewHeaderChannelDivs = publisherViewHeaderChannelDivs + "<div class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: -6px;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"+ channelNameArray[j] +"</strong> </div></div>";
   	}
   	$("#second_channel").after(publisherViewHeaderChannelDivs);
	$("#first_channel").after(publisherViewHeaderChannelDivs);
  return divContents;
}

function toggleFilterCheckbox(id) {
	if($('#optionsCheckbox-'+id+'-span').attr("name") == "two") {
		if($('#optionsCheckbox-'+id+'-span').attr("class") == "checked" && $('#optionsCheckbox-'+id).attr("checked") == "checked") {
			$('#optionsCheckbox-'+id+'-span').removeAttr("class");
			$('#optionsCheckbox-'+id).removeAttr("checked");
		}
		else {
			$('#optionsCheckbox-'+id+'-span').attr("class","checked");
			$('#optionsCheckbox-'+id).attr("checked","checked");
		}
		$('#optionsCheckbox-'+id+'-span').attr("name","one");
	}
	else if($('#optionsCheckbox-'+id+'-span').attr("name") == "one") {
		$('#optionsCheckbox-'+id+'-span').attr("name","two");
	}
}
 
 function loadFilterData() {
	 alert("loadFilterData");
	 var allPublishersList;
	 var channelsNameList;
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
 		        		 if(allPublishersList != null && allPublishersList.length > 0){
 		        			$('#first_publisher').html(allPublishersList[0].value);
 		        			$('#second_publisher').html(allPublishersList[0].value);
 		        		 }
					  }	
 		        	 if (index == 'channelsNameList') {
 		        		channelsNameList=element;																			
					  }
 		           });
 		          getchannelTrendsDropDown(allPublishersList);
 		          if(channelsNameList != null && channelsNameList.length > 0) {
 		          	$('#channelsDiv').html(getDefaultChannels(channelsNameList));
 		          }
 		          maintainFilterState();
 		       },
 		       error: function(jqXHR, error) {
 		        }
 		  });
		}catch(exception){
		}
 }
 
 
 function loadFilterDataByPublisher(publisherId) {
	 $('#channelsDiv').html('');
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
	 		        	  if (index == 'channelsNameList') {
	 		        		channelsNameList=element;																			
						  }
	 		           });
	 		          getchannelTrendsDropDown(allPublishersList);
	 		          if(channelsNameList != null && channelsNameList.length > 0) {
	 		          	$('#channelsDiv').html(getChannels(channelsNameList));
	 		          }
	 		         maintainFilterState();
	 		        loadAdvertiserPropertyList();
	 		       },
	 		       error: function(jqXHR, error) {
	 		        }
	 		  });
			}catch(exception){
			}
	 }*/
 
 

 
 function formatInt(val)
 {
	 val = parseFloat(val).toFixed(0);
	 val = val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	 return val;
	
 }
     var popover_animate_title = '<span style="font-size:14px;font-weight:bold;">Local Sales Direct </span></br><span style="color:#9ac050;font-size:14px;"><span style="margin-left:2px;"><img src="img/Arrow2Down.png"width="15"></span><span style="margin-left:10px;margin-top:2px;color:red;">-5.88%</span></span><button type="button" class="close1" style="opacity: 0.84;margin-top:-18px;float:right;" onclick="close_animate()">&times;</button>'
	 var popover_animate_content = '<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;font-size:15px; "><div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:30px;"><span style="margin-left:175px;"><b>This Week</b></span><span style="margin-left:82px;"><b>Last Week</b></span><span style="float:right;"><b>MTD</b></span></div><div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:30px;"><span style="font-weight:bold;color:black;">Impressions Delivered:</span><span style="margin-left:25px;">2,882,543</span><span style="margin-left:87px;"><b>3,801,666</b></span><span style="float:right;">11,672,296</span></div><div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:30px;"><span style="font-weight:bold;color:black;">Clicks:</span><span style="margin-left:168px;">4,955</span><span style="margin-left:115px;"><b>8,424</b></span><span style="float:right;">21,161</span></div><div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:30px;"><span style="font-weight:bold;color:black;">CTR(%):</span><span style="margin-left:157px;">0.17%</span><span style="margin-left:110px;"><b>0.22%</b></span><span style="float:right;">0.18%</span></div>      <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:30px;"><span style="font-weight:bold;color:black;">eCPM:</span><span style="margin-left:168px;">$0.98</span><span style="margin-left:110px;"><b>$0.92</b></span><span style="float:right;">$0.96</span></div>      <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;height:30px;"><span style="font-weight:bold;color:black;">Payout:</span><span style="margin-left:134px;">$2,826,94</span><span style="margin-left:80px;"><b>$3,509.70</b></span><span style="float:right;">$11,251.55</span></div><div id="chart_div_animate" style="width: 50px; height: 200px;"></div>      <div style="background-color:#FDEFBC;height:50px;"><a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" onclick="viewperformance()">View Trends</a>        <a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" onclick="reallcation()">Reallocation</a></div></div>';

	/* $(".popover_animate").popover({  
		 title: popover_animate_title ,content: popover_animate_content  , html:"true" 
	    }
	 );*/
	 function close_animate(){
	         $('.popover').hide(1000);
	 }
	 
	 function loadPopUp(){
		 try{
			 $(".popover_animate").popover({  
				 title: popover_animate_title ,content: popover_animate_content  , html:"true" 
			 	}
			 );
		 }catch(error){
		 }
	 }
 function numberWithCommas(x) {
	 return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	 }



 function getPublisherSummaryCurrent() {

	  try{
		  $("#selectChannelPublisher").html('');
		  var title=$('#publisher_allocation_val').html();
		 
		  $.ajax({
			  type : "POST",
			  url : "/loadPublisherSummaryList.lin",
			  cache : false,
			  data : {
				  startDate : startDate,
				  endDate : endDate,
				  publisherName : title,
				  channelName : allChannelName,
				  selectedPublisher : selectedPublisher
			  		},
				  dataType: 'json',
				  success: function (data) {
					  		optimizingFlag++;
				           $.each(data, function(index, element) {
				        	  if (index == 'publisherSummaryList' ) {
				        		  publisherSummaryCurrentData = element;
				        		  checkFlag();
							  }				        	  
				           });
						  
				  },			      
			 	  error: function(jqXHR, exception) {
					 console.log("getPublisherSummaryCurrent: jqXHR:"+exception);	   
				  }
			  
		 });
		 		 
	}catch(exception){
		 console.log("getPublisherSummaryCurrent: exception:"+exception);
	}

}

 function getPublisherSummaryCompare() {
	  try{
		  var title=$('#publisher_allocation_val').html();
		
		  $.ajax({
			  type : "POST",
			  url : "/loadPublisherSummaryList.lin",
			  cache : false,
			  data : {
				  startDate:compareStartDate,
				  endDate:compareEndDate,
				  publisherName : title,
				  channelName : allChannelName,
				  selectedPublisher : selectedPublisher
			  },
			  dataType: 'json',
			  success: function (data) {
			  		optimizingFlag++;
		            $.each(data, function(index, element) {
		        	  if (index == 'publisherSummaryList') {
		        		  publisherSummaryCompareData = element;
		        		  checkFlag();
					  }
	
		            });
					  
			  },			      
	          error: function(jqXHR, exception) {
	    	     console.log("getPublisherSummaryCompare: jqXHR:"+exception);
	    	  }
			  
		});
	 		 
	 }catch(exception){
		 console.log("getPublisherSummaryCompare: exception:"+exception);
	 }

}
 
 function getPublisherSummaryMTD() {
	  try{
	  var title=$('#publisher_allocation_val').html();
	 
	  $.ajax({
		  type : "POST",
		  url : "/loadPublisherSummaryList.lin",
		  cache : false,
		  data : {
			  publisherName : title,
			  channelName : allChannelName,
			  selectedPublisher : selectedPublisher
		  },
	      dataType: 'json',
		  success: function (data) {
			  		optimizingFlag++;
		           $.each(data, function(index, element) {
		        	  if (index == 'publisherSummaryList') {
		        		  publisherSummaryMTDData = element;
		        		  checkFlag();
					  }
	
		           });
				  
		    },
		    error: function(jqXHR, exception) {
		    	console.log("getPublisherSummaryMTD: jqXHR:"+exception);
		    }
		  
		  });
	 		 
	  }catch(exception){
		  console.log("getPublisherSummaryMTD: exception:"+exception);
		}

 }

 function getPerformanceByPropertyCurrent() {
 
	  try{
		  //alert("allChannelName : "+allChannelName)
	  var title=$('#publisher_allocation_val').html();
	  var mapData = '';
	  $.ajax({
		  type : "POST",
		  url : "/loadAllPerformanceByPropertyCurrent.lin",
		  cache : false,
		  data : {
			  startDate : startDate,
			  endDate : endDate,
			  selectedPublisher : selectedPublisher,
			  allChannelName : allChannelName,
			  defaultExecutionFlag : defaultExecutionFlag
		  		},
			  dataType: 'json',
		  success: function (data) {
			  		optimizingFlag++;
		           $.each(data, function(index, element) {
		        	  if (index == 'allPerformanceByPropertyListCurrentMap' ) {
		        		  mapData  = element;
		        		  performanceByPropertyCurrentData = mapData['allData'];
		        		  //console.log(performanceByPropertyCurrentData)
		        		  performanceByPropertyCurrentDataBySiteName =  mapData['allDataBySiteName'];
		        		  //alert("current = "+performanceByPropertyCurrentData.length);
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
 
 function getPerformanceByPropertyCompare() {
 
	  try{
	  var title=$('#publisher_allocation_val').html();
	  var mapData ='';
	  $.ajax({
		  type : "POST",
		  url : "/loadAllPerformanceByPropertyCompare.lin",
		  cache : false,
		  data : {
			  startDate:compareStartDate,
			  endDate:compareEndDate,
			  selectedPublisher : selectedPublisher,
			  allChannelName : allChannelName,
			  defaultExecutionFlag : defaultExecutionFlag
		  		},
			  dataType: 'json',
		  success: function (data) {
			  		optimizingFlag++;
		           $.each(data, function(index, element) {
		        	  if (index == 'allPerformanceByPropertyListCompareMap') {
		        		  mapData  = element;
		        		  performanceByPropertyCompareData = mapData['allData'];
		        		  performanceByPropertyCompareDataBySiteName =  mapData['allDataBySiteName'];
		        		 
		        		  //alert("compare = "+performanceByPropertyCompareData.length);
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
	 if(optimizingFlag == 5 
			 && performanceByPropertyCompareData != undefined 
			 && performanceByPropertyCurrentData != undefined 
			 && publisherSummaryMTDData != undefined 
			 && publisherSummaryCurrentData != undefined 
			 && publisherSummaryCompareData != undefined
			 && performanceByPropertyCompareData.length == 0 
			 && performanceByPropertyCurrentData.length == 0 
			 && publisherSummaryMTDData.length == 0 
			 && publisherSummaryCurrentData.length == 0 
			 && publisherSummaryCompareData.length == 0) {
		 //alert("optimizingFlag inside else= "+optimizingFlag);
		 row='<tr class="odd gradeX">'
		        +'<td colspan="9" style="color:red; text-align:center;">'
			        +'<div class="widget alert alert-info adjusted">'
			        +'<i class="cus-exclamation"></i>'
			        +'<strong>No records found for the selected filters</strong>'
			        +'</div>'
		        +'</td>'						      
		        +'</tr>';
		  
		 $("#just-table tbody tr").remove();
	    $("#just-table tbody").append(row);
		$("#sell_throughdata").css({'display':'none'});
		$("#performance_geomap").css({'display':'none'});
		$("#sell_throughdata_printView").css({'display':'none'});
		$("#sell_throughdata_donut_printView").css({'display':'none'});
		
		row='<tr class="odd gradeX">'
	        +'<td colspan="8" style="color:red; text-align:center;">'
		        +'<div class="widget alert alert-info adjusted">'
		        +'<i class="cus-exclamation"></i>'
		        +'<strong>No records found for the selected filters</strong>'
		        +'</div>'
	        +'</td>'						      
	        +'</tr>';
	  
	 $("#dtable1 tbody tr").remove();
	 $("#dtable1 tbody").append(row);
	}
	 else if(optimizingFlag == 5)
	 {
		 
		getOptimizedInventoryRevenueHeaderData(); //populating data on Header on Inventory Revenue
		getOptimizedChannelPerformance(); //populating data on  channel Performance table
		
		$("#sales_Channel").css({'display':'none'});
		$("#perfromace_property").css({'display':'none'});
		$("#sell_through").css({'display':'none'});
	 }
 }
 
 function  channelPerformancePieChart(dataStr,divName,chartTitle,piechartWidth,formatedData){


           var options = {
        		      title: chartTitle,
        		      width : piechartWidth,
        		      height : 200,
        		      legend:{alignment: 'center'},
        		      chartArea:{width:"94%",height:"100%"}
        		    };
           google.setOnLoadCallback(drawPieChart(divName,chartTitle,dataStr,options,formatedData));
           $("#IrPieChart_div_zoom").attr("onclick","zoomInPieChart('"+chartTitle+"', 'IrPieChart_div_zoom',"+dataStr+","+modalheaderHeight+","+modalheaderWidth+");");
     
/*    	   $('#'+divName).html("No Data Found");
    	   
    	   $("#IrPieChart_div_zoom").removeAttr("onclick");*/

 }
 
 function performanceByPropertyGeoChart(dataStr,divName,chartTitle,geoChartWidth){
	 var chartOptions = {
			 
				region : "US",
				width:geoChartWidth,
				displayMode : "markers",
				resolution:'provinces',
				colorAxis : {colors: ['red','yellow', 'green'],displayMode: 'auto'}
	 };
	 google.setOnLoadCallback(drawGeoChart(divName,dataStr,chartOptions));
	 
}
 
 function performanceByPropertyGeoChart_printView(dataStr,divName,chartTitle,geoChartWidth){
	 var chartOptions = {
			 
				region : "US",
				width:geoChartWidth,
				displayMode : "markers",
				resolution:'provinces',
				colorAxis : {colors: ['red','yellow', 'green'],displayMode: 'auto'}
	 };
	 google.setOnLoadCallback(drawGeoChart(divName,dataStr,chartOptions));
	 
}
 
 
 function loadOneYrData(){
	  $.ajax({
		  type : "POST",
		  url : "/loadOneYrData.lin",
		  cache : false,
		 success: function (data) {
				  
		  },
		       error: function(jqXHR, exception) {
		    	   
		    	   }
		  });
}

 function loadOptimizedPerformanceSummaryHeaderData() {
			var impressions = 0;
			var clicks = 0;
			var ctr = 0;
			var budget = 0;
			var divContent = "";
			try{
			for(i= 0; i < performanceSummaryHeaderFilteredData.length; i++)
			{
				var headerObj = performanceSummaryHeaderFilteredData[i];
				impressions = impressions + headerObj.impressionDelivered;
				clicks = clicks + headerObj.clicks;
				budget = budget + headerObj.revenueDeliverd;
				/*for(j = 0; j < campaigndeliveryIndicatorData.length; j++)
				{
					var indicatorObj = campaigndeliveryIndicatorData[j];
					if(headerObj.lineItemId == indicatorObj.lineItemId)
					{
						budget = budget + indicatorObj.budget;
					}
				}
				*/
				
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
				+'<div style="">REVENUE</div>'
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
					+'<div style="">REVENUE</div>'
					+'<div class="summary_value">$0.00</div>'
					+'</div>'
					+'</div>';
			}
			
			$('#publisher_performance_summary_header').html(divContent);
          
		}catch(exception){
		 }
 }
		
		
				
		function loadOptimizedPerformerLineItems(){
			
			   var tableTR="";
				
				if(performerFilteredData != null && performerFilteredData.length > 0)
				{ 
					for (counter=0; counter < 10 && counter < performerFilteredData.length ; counter++)
					{
						var topPerformerObj  = performerFilteredData[counter];
						var id = "topPerformer_"+topPerformerObj.lineItemId;
						
						if(topPerformerObj.creativeType.toLowerCase().indexOf(creativeTypeRichMedia) >= 0)
							//if(counter == 0)
						{
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			/*+'<td onclick="showRichMedia()"><i class="cus-chart-bar"></i></td>'*/
	     		   			+'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
	     		   		    +'<td onclick=showTraffer("'+id+'","'+topPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
	     			        +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+topPerformerObj.campaignLineItem+'</td>'
					        +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.impressionDelivered)+'</td>'						      
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.clicks)+'</td>'
					        +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topPerformerObj.CTR,4)+'%</td>'
	     		   			+'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topPerformerObj.deliveryIndicator,4)+'</td></tr>';
						
						}
						else
	         			{
	         			    tableTR=tableTR
      		   			+'<tr style="cursor:pointer;" id='+id+'>' 
      		   			+'<td></td>'
      		   		    +'<td onclick=showTraffer("'+id+'","'+topPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
      		   		    +'<td id="'+id+'_title" onclick=javascript:showPerformerNonPerformerPopup("'
      		   		    +id+'",false) rel="popover" class="odd gradeX">'+topPerformerObj.campaignLineItem+'</td>'
      		   		    +'<td style="text-align:right;" class="" onclick=javascript:showPerformerNonPerformerPopup("'
      		   		    +id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.impressionDelivered)+'</td>'						      
      		   		    +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
      		   		    +id+'",false) rel="popover" class="odd gradeX">'+formatInt(topPerformerObj.clicks)+'</td>'
      		   		    +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
      		   		    +id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topPerformerObj.CTR,4)+'</td>'
      		   		    +'<td style="text-align:right;" onclick=javascript:showPerformerNonPerformerPopup("'
      		   		    +id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topPerformerObj.deliveryIndicator,4)+'</td></tr>';
	         			  
	         			}
					}
					
					if(counter > 0)
					{
					}
					else
					{
						$('#performers').css('display','none');
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
					$('#performers').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="6 style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
	           }	
				$("#topPerformLineItemPublisherTable tbody tr").remove();
		        $("#topPerformLineItemPublisherTable tbody").append(tableTR);
			  
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
	     		   			+'<td onclick=showTraffer("'+id+'","'+topNonPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerObj.deliveryIndicator,4)+'%</td>'
	     		   			+'</tr>';
						}
						else {
							tableTR=tableTR
	     		   			+'<tr style="cursor:pointer;" id='+id+' >'
	     		   			+'<td></td>'
	     		   			+'<td onclick=showTraffer("'+id+'","'+topNonPerformerObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
	     		   			+id+'",false) rel="popover" class="odd gradeX">'+formatFloat(topNonPerformerObj.deliveryIndicator,4)+'%</td>'
	     		   			+'</tr>';
						}
					}
					if(counter == 0)
					{	
						$('#topnonperformers').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="8" style="color:red; text-align:center;">'
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
				        +'<td colspan="8" style="color:red; text-align:center;">'
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
					   //var mostActiveIndicatorObj = mostActiveFilteredDeliveryIndicatorData[counter];
					   
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
		         		    +'<td onclick=showTraffer("'+id+'","'+mostActiveObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
	        			    +'<tr style="cursor:pointer;" id='+id+' >'
		         		    +'<td></td>'
		         		    +'<td onclick=showTraffer("'+id+'","'+mostActiveObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
					        +'<td colspan="8" style="color:red; text-align:center;">'
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
					$('#mostActiveDiv').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="8" style="color:red; text-align:center;">'
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
	         		   		   +'<td onclick=showTraffer("'+id+'","'+topGainersObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
		         			   +'<tr style="cursor:pointer;" id='+id+' >'
	         		   		   +'<td></td>'
	         		   		   +'<td onclick=showTraffer("'+id+'","'+topGainersObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
						   
						   counter ++;
					   	}
	         		  }
					
					if(counter > 0)
					{
						//$("#topGainersLineItemsTable tbody").append(tableTR);
					}
					else
					{
						$('#topGainerDiv').css('display','none');
						countEmptyDataTables();
				    	$('#advertiserGeoMapByMarketArticle').attr('class','span12');
				    	$('#advertiserGeoMapByMarketArticle').css({'margin-left':'0px'});
				    	$('#advertiserGeoMapByLocationArticle').attr('class','span12');
				    	$('#advertiserGeoMapByLocationArticle').css({'margin-left':'0px'});
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
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
					$('#topGainerDiv').css('display','none');
					countEmptyDataTables();
					$('#advertiserGeoMapByMarketArticle').attr('class','span12');
			    	$('#advertiserGeoMapByMarketArticle').css({'margin-left':'0px'});
			    	$('#advertiserGeoMapByLocationArticle').attr('class','span12');
			    	$('#advertiserGeoMapByLocationArticle').css({'margin-left':'0px'});
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="7" style="color:red; text-align:center;">'
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
		     		   		   +'<td onclick=showTraffer("'+id+'","'+topLosersObj                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               .lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
							   +'<tr style="cursor:pointer;" id='+id+' >'
		     		   		   +'<td></td>'
		     		   		   +'<td onclick=showTraffer("'+id+'","'+topLosersObj.lineItemId+'")  href="#" data-html="" rel="popover" data-placement="right" data-original-title="" data-trigger="hover"><i class="cus-traffic"></i></td>'
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
						}
	         		   
	         		 }
					
					if(counter > 0)
					{
						//$("#topLosersLineItemsTable tbody").append(tableTR);
					}
					else
					{
						$('#toplosers').css('display','none');
						countEmptyDataTables();
						tableTR='<tr class="odd gradeX">'
					        +'<td colspan="7" style="color:red; text-align:center;">'
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
					$('#toplosers').css('display','none');
					countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="7" style="color:red; text-align:center;">'
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
			var loadTime1 = new Date().getSeconds();
			   var loadPerformanceMetricsKey =0;
			   var tableTR="";
			   //$("#performanceMetricsLoaderId").css("display", "block");
			   jQuery('#performanceMetricsTable').dataTable().fnClearTable();
				
				if(performanceMetricsFilteredData != null && performanceMetricsFilteredData.length > 0)
				{ 
					 jQuery('#performanceMetricsTable').dataTable().fnClearTable();
	            	 jQuery('#performanceMetricsTable').dataTable().fnSettings()._iDisplayLength = 10;
	            	 jQuery('#performanceMetricsTable').dataTable().fnDraw();
					/*for (i = 0; i < performanceMetricsFilteredData.length ; i++)
					{*/
					$.each(performanceMetricsFilteredData,function (newIndex,performanceMetricsObj){
						/*var performanceMetricsObj  = performanceMetricsFilteredData[i];*/
						/*var performanceMetricsDeliveryIndicatorObj  = performanceMetricsFilteredDeliveryIndicatorData[i];*/
					
		        		   (function(newIndex) {
		        			     setTimeout( function(newIndex) {
		        
		        		          
  		  
  		 /*  if(loadPerformanceMetricsKey == 0 && isTrendDefault){
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
  		   }*/
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
  		     market/*,
  		     performanceMetricsObj.site,*/
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
					        +'<td colspan="9" style="color:red; text-align:center;">'
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
				        +'<td colspan="9" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#performanceMetricsTable tbody").append(tableTR);
					
	           }	
				$("#performanceMetricsLoaderId").css("display", "none");
			  
				var loadTime2 = new Date().getSeconds();
				//alert("Loading Time = "+ (parseInt(loadTime2) - parseInt(loadTime1)));
				//alert("loadTime1 = "+loadTime1+"   loadTime2 = "+loadTime2);
		}		
	
	
		
		
		
/***************************************************************/	
/*Start :creating filter data for publisher view campaign tab */	
	
		
		function getPerformanceSummaryHeaderFilteredData() {
			
			performanceSummaryHeaderFilteredData = new Array();
			try{
			if(campaignTotalCurrentData != null && campaignTotalCurrentData != undefined)
			{
				for(i = 0; i < campaignTotalCurrentData.length ; i++)
				{
					var dtoObjectCurrent = campaignTotalCurrentData[i];

					if(leftFilterStatus == 1 && dtoObjectCurrent.publisherName == selectedPublisher)
					{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher  && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 5)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 6)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 7)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 8)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceSummaryHeaderFilteredData.push(dtoObjectCurrent);
						}
					}
				
			}
			
		}//end of if
	}catch(exception){
		// alert("excption in header data"+exception);
	 }
			}
		
		
		
		function getPerformerFilteredData()
		{
			performerFilteredData = new Array();
			//performerFilteredDeliveryIndicatorData = new Array();
			
			/*performerFilteredData = performanceSummaryHeaderFilteredData.slice(0);//Added by Dheeraj on 23 Oct 2013
			performerFilteredData = performerFilteredData.sort(function(a, b){
				 return b.CTR-a.CTR
			})*/
			
			for(i = 0; i < campaignTotalCurrentData.length ; i++)
			{
				var dtoObjectCurrent = campaignTotalCurrentData[i];

				if(leftFilterStatus == 1 && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
				{
					performerFilteredData.push(dtoObjectCurrent);
				}
				else if(leftFilterStatus == 2)
				{
					if(dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 3)
				{
					if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 4)
				{
					if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 5)
				{
					if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 6)
				{
					if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 7)
				{
					if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
				else if(leftFilterStatus == 8)
				{
					if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.deliveryIndicator >= 100)
					{
						performerFilteredData.push(dtoObjectCurrent);
					}
				}
		}
			
			performerFilteredData = performerFilteredData.sort(function(a, b){
				 return b.CTR-a.CTR
			})
	}

	
		function getNonPerformerFilteredData()
		{
			nonPerformerFilteredData = new Array();
			nonPerformerFilteredDeliveryIndicatorData = new Array();
			
			nonPerformerFilteredDataTemp = campaignTotalCurrentData.slice(0);
			
			nonPerformerFilteredDataTemp.sort(function(a, b){
				 return b.deliveryIndicator-a.deliveryIndicator
			})
			
			for(i = 0; i < nonPerformerFilteredDataTemp.length ; i++)
			{
				var nonPerformerObj = nonPerformerFilteredDataTemp[i];
				
				for(j = 0; j < campaigndeliveryIndicatorData.length ; j++)
				{
					var dtoObjectIndicator = campaigndeliveryIndicatorData[j];
					
					
					if( nonPerformerObj.deliveryIndicator < 100 && dtoObjectIndicator.bookedImpressions > 0 && nonPerformerObj.endDateTime != "" && dtoObjectIndicator.bookedImpressions != 100){
			
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
						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.siteName == SelectedDFP_propertyPublisher)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.advertiser == advertisername )
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.advertiser == advertisername && nonPerformerObj.siteName == SelectedDFP_propertyPublisher)
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}else if(leftFilterStatus == 5)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname )
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}else if(leftFilterStatus == 6)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname  && nonPerformerObj.siteName == SelectedDFP_propertyPublisher )
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}else if(leftFilterStatus == 7)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname && nonPerformerObj.advertiser == advertisername )
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
					}else if(leftFilterStatus == 8)
					{

						if(nonPerformerObj.lineItemId == dtoObjectIndicator.lineItemId && nonPerformerObj.agency == agencyname && nonPerformerObj.advertiser == advertisername && nonPerformerObj.siteName == SelectedDFP_propertyPublisher )
						{
							nonPerformerFilteredData.push(nonPerformerObj);
							nonPerformerFilteredDeliveryIndicatorData.push(dtoObjectIndicator);
						}
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
			
			var mostActiveFilteredDataTemp = campaignTotalCurrentData.slice(0);
			
			for(i = 0; i < mostActiveFilteredDataTemp.length ; i++)
			{
				var mostActiveObj = mostActiveFilteredDataTemp[i];
				
					if(leftFilterStatus == 1 && mostActiveObj.publisherName == selectedPublisher)
					{
						//if(mostActiveObj.deliveryIndicator >= 100)
						//{
							mostActiveFilteredData.push(mostActiveObj);
						//}
					}
					else if(leftFilterStatus == 2)
					{
						if(mostActiveObj.siteName == SelectedDFP_propertyPublisher && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(mostActiveObj.advertiser == advertisername && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(mostActiveObj.advertiser == advertisername && mostActiveObj.siteName == SelectedDFP_propertyPublisher && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 5)
					{

						if(mostActiveObj.agency == agencyname && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 6)
					{

						if(mostActiveObj.agency == agencyname && mostActiveObj.siteName == SelectedDFP_propertyPublisher && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 7)
					{

						if(mostActiveObj.agency == agencyname && mostActiveObj.advertiser == advertisername && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
					else if(leftFilterStatus == 8)
					{

						if(mostActiveObj.advertiser == advertisername && mostActiveObj.agency == agencyname && mostActiveObj.siteName == SelectedDFP_propertyPublisher && mostActiveObj.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(mostActiveObj);
						}
					}
				
			}
			
			mostActiveFilteredData.sort(function(a, b){
				 return b.impressionDelivered-a.impressionDelivered
			})
			
			
			for(i = 0; i < mostActiveFilteredData.length ;i++)
			{
				var mostActiveObj = mostActiveFilteredData[i];

				var lastPctChange = 0;
				for(j = 0; j < campaignTotalCompareData; j++)
				{
					compareObj = campaignTotalCompareData[j];
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
				for(k = 0; k < campaignTotalMTDData; k++)
				{
					mtdObj = campaignTotalMTDData[k];
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
			
			for(i = 0; i < campaignTotalCurrentData.length ; i++)
			{
				for(j = 0; j < campaigndeliveryIndicatorData.length ; j++)
				{
					var dtoObjectCurrent = campaignTotalCurrentData[i];
					var dtoObjectIndicator = campaigndeliveryIndicatorData[j];
					
					if(leftFilterStatus == 1)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.advertiser == advertisername  && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 5)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.publisherName == selectedPublisher )
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 6)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname  && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 7)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname  && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.advertiser == advertisername)
						{
							topGainersFilteredData.push(dtoObjectCurrent);
							topLosersFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 8)
					{

						if(dtoObjectCurrent.lineItemId == dtoObjectIndicator.lineItemId && dtoObjectCurrent.agency == agencyname  && dtoObjectCurrent.publisherName == selectedPublisher && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.advertiser == advertisername)
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
				
				for(j = 0; j < campaignTotalCompareData.length; j++)
				{
					compareObj = campaignTotalCompareData[j];
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
				for(k = 0; k < campaignTotalMTDData.length; k++)
				{
					mtdObj = campaignTotalMTDData[k];
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
			
		}
		
		
		
		function getPerformanceMetricsFilteredData()
		{
			var getTime1 = new Date().getSeconds();
			performanceMetricsFilteredData = new Array();
			//performanceMetricsFilteredDeliveryIndicatorData = new Array();
			
			for(i = 0; i < campaignTotalCurrentData.length ; i++)
			{

					var dtoObjectCurrent = campaignTotalCurrentDataPerformanceMetrics[i];
					
					if(leftFilterStatus == 1 && dtoObjectCurrent.publisherName == selectedPublisher)
					{
						performanceMetricsFilteredData.push(dtoObjectCurrent);
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 4)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 5)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 6)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 7)
					{
						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
					else if(leftFilterStatus == 8)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							performanceMetricsFilteredData.push(dtoObjectCurrent);
						}
					}
				
					
				
			}
			//Commented following for...loop by Dheeraj on 23 Oct 2013
			/*for(j = 0; j <campaigndeliveryIndicatorData.length ; j++)
				{
					var dtoObjectIndicator = campaigndeliveryIndicatorData[j];
					
					if(leftFilterStatus == 1 && dtoObjectCurrent.publisherName == selectedPublisher)
					{
						//if(dtoObjectCurrent.deliveryIndicator >= 100)
						//{
							mostActiveFilteredData.push(dtoObjectIndicator);
						//}
					}
					else if(leftFilterStatus == 2)
					{
						if(dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 3)
					{
						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 4)
					{

						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 5)
					{

						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 6)
					{

						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 7)
					{

						if(dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
					else if(leftFilterStatus == 8)
					{

						if(dtoObjectCurrent.advertiser == advertisername && dtoObjectCurrent.agency == agencyname && dtoObjectCurrent.siteName == SelectedDFP_propertyPublisher && dtoObjectCurrent.publisherName == selectedPublisher)
						{
							mostActiveFilteredData.push(dtoObjectIndicator);
						}
					}
				
			}*/
			var getTime2 = new Date().getSeconds();
			//alert("Filtering Time = "+(parseInt(getTime2) - parseInt(getTime1)));
			//alert("getTime1 = "+getTime1+"   getTime2 = "+getTime2);
	}
		
	
function getLeftFilterStatus()
{
	if(SelectedDFP_propertyPublisher == "All Properties"  ){
		SelectedDFP_propertyPublisher = '';
	}
	if((agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == "") && (SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 1;
		//alert(1)
	}
	else if((agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == "") && !(SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 2;
		//alert(2)
	}
	else if((agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == "") && (SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 3;
		//alert(3)
	}
	else if((agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == "") && !(SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 4;
		//alert(4)
	}
	else if(!(agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == "") && (SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 5;
		//alert(5)
	}
	else if(!(agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == "") && !(SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 6;
		//alert(6)
	}
	else if(!(agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == "") && (SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 7;
		//alert(7)
	}
	else if(!(agencyname == undefined || agencyname == "") && !(advertisername == undefined || advertisername == "") && !(SelectedDFP_propertyPublisher == undefined || SelectedDFP_propertyPublisher == ""))
	{
		leftFilterStatus = 8;
		//alert(8)
	}
	/*else if(!(agencyname == undefined || agencyname == "") && (advertisername == undefined || advertisername == ""))
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
	}*/
}


function getAdvertiserTotalCurrent() {
	 console.log("getAdvertiserTotalCurrent:startDate:"+startDate+" and endDate:"+endDate);
	  try{
	  $.ajax({
		  type : "POST",
		  url : "/loadCampainTotalDataPublisherViewListCurrent.lin",
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
	    	   properties : SelectedDFP_propertyPublisher
		  		},
			  dataType: 'json',
		  success: function (data) {
			  optimizingFlagCampain++;
		           $.each(data, function(index, element) {
		        	   if (index == 'publisherCampainTotalDataListCurrentMap' ) {
			        		  mapData  = element;
			        		  campaignTotalCurrentDataPerformanceMetrics = mapData['campainTotal'];
			        		  campaignTotalCurrentData =  mapData['lineItemCalculated'];
			        		  campaignTotalCurrentData.sort(function(a, b){
			        			  return b.CTR-a.CTR
			        			 })
			        		  //alert("current = "+performanceByPropertyCurrentData.length);
			        		  checkFlag_campain();
						  }
		        	 /* if (index == 'publisherCampainTotalDataListCurrent' ) {
		        		  campaignTotalCurrentData = element;
		        		  
		        		  campaignTotalCurrentData.sort(function(a, b){
		        			  return b.CTR-a.CTR
		        			 })
		        		  checkFlag_campain();
					  }*/

		        	  
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
		  url : "/loadCampainTotalDataPublisherViewListCompare.lin",
		  cache : false,
		  data : {
			   publisherName : selectedPublisher,
	    	   startDate:compareStartDate,
	    	   endDate:compareEndDate,
	    	   advertiser:advertisers,
	    	   agency:agencies,
	    	   properties : SelectedDFP_propertyPublisher
		  		},
			  dataType: 'json',
		  success: function (data) {
			  optimizingFlagCampain++;
		           $.each(data, function(index, element) {
		        	   if (index == 'publisherCampainTotalDataListCompareMap' ) {
			        		  mapData  = element;
			        		  campaignTotalCompareDataPerformanceMetrics = mapData['campainTotal'];
			        		  campaignTotalCompareData =  mapData['lineItemCalculated'];
			        		  campaignTotalCurrentData.sort(function(a, b){
			        			  return b.CTR-a.CTR
			        			 })
			        		  //alert("current = "+performanceByPropertyCurrentData.length);
			        		  checkFlag_campain();
						  }
		        	  /*if (index == 'publisherCampainTotalDataListCompare') {
		        		  campaignTotalCompareData = element;
		        		  
		        		  campaignTotalCurrentData.sort(function(a, b){
		        			  return b.CTR-a.CTR
		        			 })
		        		  checkFlag_campain();
					  }*/
	
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
		  url : "/loadCampainTotalDataPublisherViewListMTD.lin",
		  cache : false,
		  data : {
			  	publisherName : selectedPublisher,
			  	advertiser:advertisers,
			  	agency:agencies,
			  	properties : SelectedDFP_propertyPublisher
		  		},
			  dataType: 'json',
		  success: function (data) {
			  optimizingFlagCampain++;
		           $.each(data, function(index, element) {
		        	   if (index == 'publisherCampainTotalDataListMTDMap' ) {
			        		  mapData  = element;
			        		  campaignTotalMTDDataPerformanceMetrics = mapData['campainTotal'];
			        		  campaignTotalMTDData =  mapData['lineItemCalculated'];
			        		  //alert("current = "+performanceByPropertyCurrentData.length);
			        		  checkFlag_campain();
						  }
		        	 /* if (index == 'publisherCampainTotalDataListMTD') {
		        		  campaignTotalMTDData = element;
		        		  checkFlag_campain();
					  }*/
	
		           });
				  
		  },
		      
		       error: function(jqXHR, exception) {
		    	   
		    	   }
		  
		  });
	 		 
	  }catch(exception){
		  
		}

}

function getDeliveryIndicatorData(){
	  console.log("getDeliveryIndicatorData:startDate:"+startDate+" and endDate:"+endDate);
	  try{
	  $.ajax({
		  type : "POST",
		  url : "/loadCampainPerformanceDeliveryIndicatorData.lin",
		  cache : false,
		  data : {
			   publisherName : selectedPublisher,
	    	   startDate:startDate,
	    	   endDate:endDate,
	    	   advertiser:advertisers,
	    	   agency:agencies,
	    	   properties : SelectedDFP_propertyPublisher
		  		},
			  dataType: 'json',
			  success: function (data) {
				  optimizingFlagCampain++;
		           $.each(data, function(index, element) {
		        	  if (index == 'publisherCampainDeliveryIndicatorDataList') {
		        		  campaigndeliveryIndicatorData = element;
		        		  checkFlag_campain();
					  }
	
		           });
				  
		  },
		      
		       error: function(jqXHR, exception) {
		    	   
		    	   }
		  
		  });
	 		 
	  }catch(exception){
		  
		}


}

function generateGeoGraph(divName,actionName,graphTitle,zoom_Divid){
	var actionUrl  = "/"+actionName+".lin";
	$('#'+divName).css('display','block');
	$('#topGainerArticle').attr('class','span6');
	
	$('#advertiserGeoMapByMarketArticle').attr('class','span6');
	$('#advertiserGeoMapByMarketArticle').css('margin-left','2.56%');
	$('#advertiserGeoMapByLocationArticle').attr('class','span6');
	$('#advertiserGeoMapByLocationArticle').css('margin-left','2.56%');
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
		    	  properties : SelectedDFP_propertyPublisher
		    	  
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
	        	   countEmptyGeoGraph();
	        	   $('#'+divName+'Article').css('display','none');
					countEmptyDataTables();
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
	        		 if(element.bookedImpression > 0) {
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
		           	  +' onclick=javascript:viewPerformanceCampaign("'+title.replace(/ /g, "&#32;")+'","'+element.order.replace(/ /g, "&#32;")+'")>View Trends</a>'
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
		           	  +' onclick=javascript:viewPerformanceCampaign("'+title.replace(/ /g, "&#32;")+'","'+element.order.replace(/ /g, "&#32;")+'")>View Trends</a>'
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
function showTraffer(id,lineItemId){
	   var forcastDTOStatus = false;
	    var forcastDTO;
	    var bookedImpressions;
	    var delivered;
	    var startDate;
	    var endDate;
	    var forcasted;
	    var CPM;
	    var lineItem;
		var contentDiv="";
		var subTitle="";
		var title = $('#'+id+'_title').html();
		 contentDiv=contentDiv
    	 +'<div id="popover_content_wrapper" style="width:550px;">'
			+'<div style="border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333; ">'
			  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:40px;width:527px;text-transform: uppercase;"> <div style="width:75px;float:left;text-align:center;"><b>Goal Quantity</b></div><div style="width:80px;float:left;margin-left:10px;text-align:center;"><b>Delivered</b></div><div style="width:86px;float:left;margin-left:10px;text-align:center;"><b>Remaining</b></div> <div style="width:50px;float:left;margin-left:10px;text-align:center;"><b>CPM</b></div><div style="float:left; margin-left:10px;width:90px;text-align:center;"><b>Start Date</b></div><div style="width:90px;float:left;margin-left:10px;text-align:center;"><b>End Date</b></div></div>'
			  +'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:527px;"><div style="width:80px;float:left;text-align:right;" id = "booked_imp"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:80px;" id = "deliverd"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:86px;" id = "remaining"><b></b></div> <div style="width:50px;float:left;margin-left:10px;text-align:right;" id="ecpm"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:90px;" id = "start_date"><b></b></div><div style="float:left;text-align:right;margin-left:10px;width:90px;" id = "end_date"><b></b></div></div>'
			 // +' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>585,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$8.00</b></div><div style="float:left;"><b>03/30/2013</b></div></div>'
			  //+'<div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>310,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$10.00</b></div><div style="float:left;"><b>02/15/2013</b></div></div>'
			  //+' <div style="border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:70px;width:460px;"> <div style="font-weight:bold;color:black;width:200px;float:left;">Line Item 1</div><div style="width:109px;float:left;margin-left:10px;"><b>250,000</b></div> <div style="width:62px;float:left;margin-left:10px;"><b>$12</b></div><div style="float:left;"><b>01/15/2013</b></div></div>'
			  +'<div id="trafficPopup_loader" style="width:550px;height:30px;text-align:center"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>'
			  +' <div id="chart_div_traffer" style="width: 500px; height: 220px;margin-left:10px;"></div>'
			 +'<div style="height:10px;background-color:#FDEFBC;"></div>'
			 
			 +'</div>'
			 +'</div>';
		 lastTrafferPopUpId = id;
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
 		        		endDate = forcastDTO.endDate;
 		        		if(bookedImpressions!=null && delivered!=null){
 		        			forcasted = (bookedImpressions - delivered);
 		        		}
 		        		bookedImpressions = formatInt(bookedImpressions);
 		        		
 		        		if(lastTrafferPopUpId == id) {
 		        			if(forcastDTOStatus){ 
		 		        		deliveryIndicaterGraph(forcastDTOStatus,delivered,lineItem,forcasted);
	 		        			//$('#line_name').html(lineItem);
	 		        			$('#booked_imp').html(bookedImpressions);
	 		        			$('#ecpm').html(CPM);
	 		        			$('#start_date').html(startDate);
	 		        			$('#end_date').html(endDate);
	 		        			$('#deliverd').html(delivered);
	 		        			$('#remaining').html(forcasted);
 		        			}
 		        			else { 
		 		        		$('#chart_div_traffer').html("<center><h4>Bar Graph Not Available</h4></center>")
	 		        			//$('#line_name').html(lineItem);
	 		        			$('#booked_imp').html('');
	 		        			$('#ecpm').html('');
	 		        			$('#start_date').html('');
	 		        			$('#end_date').html('');
	 		        			$('#deliverd').html('');
	 		        			$('#remaining').html('');
 		        			}
		 		        		
 		        		}	 
					  }
 		           });
 		          $("#trafficPopup_loader").css({'display':'none'});
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

function viewPerformanceCampaign(lineItemName, orderName){
	 $("#tre_ana").css({'display':'none'});
	 $('#myTab1 li:eq(3)').css({'display':'inline'});
	 $('#myTab1 li:eq(3) a').tab('show');
	 $(".agency_second_filter_publisher").css({'display':'block'});
	 $(".advertiser_second_filter_publisher").css({'display':'block'});
	 $("#lineItems_dropdown_publisher").css({'display':'none'});
	 
	 if ($('#myTab1 li:eq(3) a').text() == "Trends and Analysis"){
		 $("#tre_ana_campaign").css({'display':'inline'});
		 $("#agency_dropdown_advertiser").css({'display':'none'});
		 $("#advertiser_dropdown_advertiser").css({'display':'none'});
		 //$("#order_dropdown").show();
		 //$("#lineItems_dropdown_single").show();
		 /*$(".agency_second_filter").show();
		 $(".advertiser_second_filter").show();*/
		 $("#order_dropdown_text_publisher").css({'display':'inline'});
		 $("#line_dropdown_text_publisher").css({'display':'inline'});
		 
	 }
	 
	 lineItem = lineItemName;
	 lineItemArr = lineItemName;
	 order = orderName;
	 ordername = orderName;
	 $(".order_option").remove();
	 $("#order_trends_publisher").after("<div class='widget alert alert-info-header adjusted order_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>"+orderName+"</strong> </div></div>");
	 
	$(".lineOrder_option").remove();
	$("#lineItem_trends_publisher").after("<div class='widget alert alert-info-header adjusted lineOrder_option' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: 7px;'>    <div style='float:left;margin-left:-21px;color:white;'>    <strong style='text-transform: uppercase;'>"+lineItemName+"</strong> </div></div>");
	$("#order_dropdown_name_publisher").text("");
	$("#line_dropdown_name_publisher").text("");
	$("#order_dropdown_name_publisher").text(orderName);
	$("#line_dropdown_name_publisher").text(lineItemName);
	
	 	getAdvertiserTrendAnalysisHeaderData();
		getActualAdvertiserData();
		actualLineGenerationCampaign();
		//getForcastAdvertiserData();
}

function actualLineGenerationCampaign() {
	var divNameImp = "chart_div_left3_campaign";
	var divNameClick = "chart_div_left1_campaign";
	var divNameCtr = "chart_div_left2_campaign";	
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
			  properties : SelectedDFP_propertyPublisher
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
	var headerDiv_printView = '';
	
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
			  properties : SelectedDFP_propertyPublisher
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
		        	  $('#trends_analysis_header_campaign').html(headerDiv);
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
	+'<div style="">REVENUE</div>'
	+'<div class="summary_value">$'+formatFloat(budget,2)+'</div>'
	+'</div>'
	+'</div>';

	
return headerDivData;
	
}


function getActualAdvertiserData(){	
	$("#actualPublisherCampaignDiv").css("display", "block");
	$("#actualPublisherTableLoaderIdCampaign").css("display", "block");
	jQuery('#actualPublisherTableCampaign').dataTable().fnClearTable();
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
	 					properties : SelectedDFP_propertyPublisher
	 				},	
	 		        dataType: 'json',
	 		        success: function (data) {
	 		           jQuery('#actualPublisherTableCampaign').dataTable().fnClearTable();
	 		          jQuery('#actualPublisherTableCampaign').dataTable().fnSettings()._iDisplayLength = 10;
	 		          jQuery('#actualPublisherTableCampaign').dataTable().fnDraw();
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
			 		        		  jQuery('#actualPublisherTableCampaign').dataTable().fnAddData( [
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
	 		        	   else if(index == 'advertiserTrendAnalysisActualDatarList' && (element != null  || element != undefined  || element.length == 0)) {
	 		        		  $("#actualPublisherCampaignDiv").css("display", "none");
	 		        		  row='<tr class="odd gradeX">'
	 		        			  +'<td colspan="7" style="color:red; text-align:center;">'
	 							        +'<div class="widget alert alert-info adjusted">'
	 							        +'<i class="cus-exclamation"></i>'
	 							        +'<strong>No records found for the selected filters</strong>'
	 							        +'</div>'
	 						        +'</td>'						      
	 						        +'</tr>';
	 		        		 $("#actualPublisherTableCampaign tbody").append(row);
	 						}
		 		       });
	 		          $("#actualPublisherTableLoaderIdCampaign").css("display", "none");
	 		       },
	 		        error: function(jqXHR, exception) {
		 		    }
		 		  });

}

/*function loadAdvertiserPropertyList() {
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
}*/

/*function getPropertyDropDown(propertyList) {
	 var ulContents = "";
	 $("#publisherViewPropertyDropDown ul").empty();
	 if(propertyList != null && propertyList.length > 0){
		 $('#advertiserViewPropertyTitle').html(propertyList[0]);
		 selectedPublisher = propertyList[0];
	 }
	 $("#publisherViewPropertyTitle").html("All Properties");
	 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changePublisherViewPropertyDropdown('All Properties');\" style=\"color:black;\">All Properties</a></li>";
	 if(propertyList != null && propertyList.length > 0){
		 for(j=0;j<propertyList.length;j++) {
			 
			 ulContents = ulContents + "<li><a href=\"javascript:void(0);\" onclick=\"changePublisherViewPropertyDropdown('"+propertyList[j].propertyName+"','"+propertyList[j].DFP_propertyName+"');\" style=\"color:black;\">" + propertyList[j].propertyName + "</a></li>";
		 }
	 }
	 $("#publisherViewPropertyDropDown ul").append(ulContents);
}*/

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
	/*$('#richMediaPopupOuterDiv').append(divContent);*/
}




function addLoaders() {
	countOfEmptyDataTables = 0;
	countOfEmptyGeoGraph = 0;
	$('#emptyDataTableMsgId').css('display','none');
	/*   // for earlier campaign view
	$('#topGainerArticle').attr('class','span6');
	$('#advertiserGeoMapByLocationArticle').attr('class','span6');
	$('#advertiserGeoMapByMarketArticle').attr('class','span6');
	$('#advertiserGeoMapByLocationArticle').css('display','block');
	$('#advertiserGeoMapByMarketArticle').css('display','block');
	$('#performers').css('display','block');
	$('#topnonperformers').css('display','block');
	$('#mostActiveDiv').css('display','block');
	$('#topGainerDiv').css('display','block');
	$('#toplosers').css('display','block');
	$('#performanceMetricsDiv').css('display','block');
	 ////Loader for Top Performer//////////
	  $("#topPerformLineItemPublisherTable tbody tr").remove();
		var loader = '<tr class="odd gradeX">'
			   +'<td colspan="6" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topPerformLineItemPublisherTable tbody").append(loader);
		   
	////Loader for TopNonPerformer//////////
	 $("#topNonPerformLineItemTable tbody tr").remove();
	   var loader = '<tr class="odd gradeX">'
		   +'<td colspan="8" style="color:red; text-align:center;">'
		   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
	   $("#topNonPerformLineItemTable tbody").append(loader);   

	   
	////Loader for Most Active//////////
	   $("#mostActiveLineItemTable thead tr").remove();	 
	   $("#mostActiveLineItemTable tbody tr").remove();
	   var loader = '<tr class="odd gradeX">'
		   +'<td colspan="8" style="color:red; text-align:center;">'
		   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
	   $("#mostActiveLineItemTable tbody").append(loader);
	   
	   var tableHeadTR='<tr><th></th>'
		   				   +'<th></th>'
		   				   +'<th>CAMPAIGN LINE ITEMS</th>'
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
			   +'<td colspan="7" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topGainersLineItemsTable tbody").append(loader);
		   
		   var tableHeadTR='<tr><th></th>'
							   +'<th></th>'
							   +'<th>CAMPAIGN LINE ITEMS</th>'
			                   +'<th style="text-align:right;">CTR(%)</th>'
			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
		   $("#topGainersLineItemsTable thead").append(tableHeadTR);
		   

	/////////Loader for Top Loser////////
		   $("#topLosersLineItemsTable thead tr").remove();
		   $("#topLosersLineItemsTable tbody tr").remove();
		   
		   var loader = '<tr class="odd gradeX">'
			   +'<td colspan="7" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#topLosersLineItemsTable tbody").append(loader);
		   
		   var tableHeadTR='<tr><th></th>'
							   +'<th></th>'
							   +'<th>CAMPAIGN LINE ITEMS</th>'
			                   +'<th style="text-align:right;">CTR(%)</th>'
			                   +'<th style="text-align:right;">CHG('+timePeriod+')</th>'
			                   +'<th style="text-align:right;">CHG(Life Time)</th>'
			                   +'<th style="text-align:right;">IMPRESSIONS DELIVERED</th></tr>';
		   $("#topLosersLineItemsTable thead").append(tableHeadTR);
		   
		   
	//////////Loader for Performance Metrics///////////////	 
		   $("#performanceMetricsLoaderId").css("display", "block");
		   jQuery('#performanceMetricsTable').dataTable().fnClearTable();
		   */
		   
	//////////Loader for Inventory & Revenue///////////////	
		   var loader = '<div style="display:block;margin-left:45%;margin-top:20px;">'
   						+'<img src="img/loaders/type6/light/80.gif" alt="loader">'
   						+'</div>';
		   
		   $("#publisher_inventory_revenue_header").empty();
		   $("#publisher_trends_analysis_header").empty();
		   $("#publisher_performance_summary_header").empty();
		   
		   
		   $("#publisher_inventory_revenue_header").append(loader);
		   $("#publisher_trends_analysis_header").append(loader);
		   $("#publisher_performance_summary_header").append(loader);
		   
		   
		   loader = '<div style="display:block;margin-left:45%;margin-top:20px;">'
					+'<img src="img/loaders/type4/light/46.gif" alt="loader">'
					+'</div>';
		   $("#IrPieChart_div").empty();
		   $("#IrPieChart_div").append(loader);
}

function changeChannelPublisherDropDown() {
	 var selectedChannel = $('#selectChannelPublisher').val();
	 if(selectedChannel == 'All'){
		 getOptimizedPerformanceByPropertyAllChannels();
		 //alert(selectedPublisher)
		 if(selectedPublisher != 'Lin Digital'){
			 $("#sell_throughdata").css({'display':'block'});
			 $("#performance_geomap").css({'display':'block'});
			 
			 $("#sell_throughdata_printView").css({'display':'block'});
			 $("#sell_throughdata_donut_printView").css({'display':'block'});
			 $("#performance_geomap_printView").css({'display':'block'});
		 }else{
			 $("#sell_throughdata").css({'display':'none'});
			 $("#performance_geomap").css({'display':'none'});
			 
			 $("#sell_throughdata_printView").css({'display':'none'});
			 $("#sell_throughdata_donut_printView").css({'display':'none'});
			 $("#performance_geomap_printView").css({'display':'none'});
		 }
		 
	 }else{
		 getOptimizedPerformanceByPropertyByChannel(selectedChannel);
		 if(selectedPublisher != 'Lin Digital'){
		 if(selectedChannel == 'Local Sales Direct' || selectedChannel == 'National Sales Direct' || selectedChannel == 'House'){
				$("#sell_throughdata").css({'display':'block'});
				$("#performance_geomap").css({'display':'block'});
				
				$("#sell_throughdata_printView").css({'display':'block'});
				$("#sell_throughdata_donut_printView").css({'display':'block'});
				$("#performance_geomap_printView").css({'display':'block'});
			}
			else{
				$("#sell_throughdata").css({'display':'none'});
				$("#performance_geomap").css({'display':'block'});
				
				$("#sell_throughdata_printView").css({'display':'none'});
				$("#sell_throughdata_donut_printView").css({'display':'none'});
				$("#performance_geomap_printView").css({'display':'block'});
			}
		 }else{
			 	$("#sell_throughdata").css({'display':'none'});
				$("#performance_geomap").css({'display':'none'});
				
				$("#sell_throughdata_printView").css({'display':'none'});
				$("#sell_throughdata_donut_printView").css({'display':'none'});
				$("#performance_geomap_printView").css({'display':'none'});
		 }
	 }
}

function downLoadPublisherReport() {
	 var title=$('#publisher_allocation_val').html();
	 try{	  
			location.href="/downloadPublisherReport.lin?startDate="+startDate+"&endDate="+endDate+
							"&compareStartDate="+compareStartDate+"&compareEndDate="+compareEndDate+
							"&publisherName="+title+"&channelName="+allChannelName+
							"&selectedPublisher="+selectedPublisher+"&defaultExecutionFlag="+defaultExecutionFlag;   
		}catch(error){
		}
}

function getRevenueSummaryPrintView(str){
	
	if(str == 'true')
	{
		$("#revenueSummaryActualView").css("display", "none");
		$("#header-toolbar").css("display", "none");
		$("#revenueSummaryPrintView").css("display", "block");
		$(document).attr('title', 'RevenueAndSummary');
	}
	else
	{
		$("#revenueSummaryActualView").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#revenueSummaryPrintView").css("display", "none");
	}
	 window.print();
	 
		$("#revenueSummaryActualView").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#revenueSummaryPrintView").css("display", "none");
		$(document).attr('title', 'ONE - Publisher Dashboard');

}

function getTrendsAnalysisPrintView(str)
{
	if(str == 'true')
	{
		$("#revenueSummaryActualView").css("display", "none");
		$("#header-toolbar").css("display", "none");
		$("#trendsAnalysisPrintView").css("display", "block");
		$(document).attr('title', 'TrendsAndAnalysis');
	}
	else
	{
		$("#revenueSummaryActualView").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#trendsAnalysisPrintView").css("display", "none");
	}
	
	$("#trendsAnalysisPrintView").removeClass("trendsPrintView");
	window.print();
	
	$("#revenueSummaryActualView").css("display", "block");
	$("#header-toolbar").css("display", "block");
	$("#trendsAnalysisPrintView").css("display", "none");
	$(document).attr('title', 'ONE - Publisher Dashboard');
	
}

function getDiagnosticToolsPrintView(str)
{
	if(str == 'true')
	{
		$("#revenueSummaryActualView").css("display", "none");
		$("#header-toolbar").css("display", "none");
		$("#diagnosticToolsPrintView").css("display", "block");
		$(document).attr('title', 'DiagnosticTools');
	}
	else
	{
		$("#revenueSummaryActualView").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#diagnosticToolsPrintView").css("display", "none");
	}

	window.print();
	
	$("#revenueSummaryActualView").css("display", "block");
	$("#header-toolbar").css("display", "block");
	$("#diagnosticToolsPrintView").css("display", "none");
	$(document).attr('title', 'ONE - Publisher Dashboard');
}

function getTraffickingSummaryPrintView(str)
{
	if(str == 'true')
	{
		$("#revenueSummaryActualView").css("display", "none");
		$("#header-toolbar").css("display", "none");
		$("#traffickingSummaryPrintView").css("display", "block");
		$(document).attr('title', 'TraffickingSummary');
	}
	else
	{
		$("#revenueSummaryActualView").css("display", "block");
		$("#header-toolbar").css("display", "block");
		$("#traffickingSummaryPrintView").css("display", "none");
	}
	
	window.print();
	
	$("#revenueSummaryActualView").css("display", "block");
	$("#header-toolbar").css("display", "block");
	$("#traffickingSummaryPrintView").css("display", "none");
	$(document).attr('title', 'ONE - Publisher Dashboard');
}

function getPrintView()
{
	//alert(tabName+" == "+summaryTab);
	if(tabName == summaryTab || tabName == '')
	{
		getRevenueSummaryPrintView('true');
	}
	else if(tabName == trendsTab)
	{
		getTrendsAnalysisPrintView('true');
	}
	else if(tabName == diagnosticTab)
	{
		getDiagnosticToolsPrintView('true');
	}
	else if(tabName == traffickingTab)
	{
		getTraffickingSummaryPrintView('true');
	}
}

