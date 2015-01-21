google.load('visualization', '1', {'packages': ['geochart']});		 
google.load('visualization', '1.0', {'packages':['controls']});
google.load("visualization", "1", {packages:["corechart"]});

var allChannelName = "";
var timePeriod='This Week';
var channelArr = new Array();
var startDate;
var endDate;
var compareStartDate ;
var compareEndDate;

$(window).load(function(){	
	//getPublisherDropDown();
	loadAllData();
 });

function loadAllData(){	 

	//startDate=$("#startDateId").val();
	//endDate=$("#endDateId").val();
	startDate = $('#startDate').val();
	endDate = $('#endDate').val();
	compareStartDate = $('#compareStartDate').val();
	compareEndDate = $('#compareEndDate').val();
	
	getChannelPerformance();
	getPerformanceByProperty();
	getSellThroughData();
	loadAllPublisherGraphs();
	//alert(startDate+endDate);
	getActualPublisherData();
	actualLineGeneration();
	getReallocationPublisherData();
	//loadTrendsAnalysisData(); //  not in used
	getPublisherTrendAnalysisHeaderData(); //-- shubham new function
	getPublisherInventoryRevenueHeaderData();
}

function loadTrendsAnalysisData(){
	//startDate=$("#startDateId").val();
	//endDate=$("#endDateId").val();	
	startDate = $('#startDate').val();
	endDate = $('#endDate').val();
	//alert(startDate+endDate);
	var publisherName=$("#publisher_allocation_val").text();		
	var channelName=$("#channel_trends_val").text();
	if(channelName != undefined && channelName != 'All'){
		loadChannelByName(channelName);
	}else{
		loadChannelByPublisher(publisherName);
	}	
	//getActualPublisherData();
	//actualLineGeneration();
}

function loadReallocationData(){
	//startDate=$("#startDateId").val();
	//endDate=$("#endDateId").val();	
	startDate = $('#startDate').val();
	endDate = $('#endDate').val();
	var publisherName=$("#publisher_allocation_val").text();
	//loadPublisherAllocationHeader(publisherName);
	getReallocationPublisherData();
}

function loadAllPublisherGraphs(){
	  pieChartGeneration('channelPerformance','IrPieChart_div','Fill Rate');
	  geoChartGeneration('performanceByProperty','geomap4','GEO CHART');
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
 //loadChannelByName(val);
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
	//alert(rev_rec_cal_formated1);
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
	//alert(Value2);
	var total = Value1 + Value2 + Value3 + Value4
	var budget_allocated_val = 6000 - total
	if(budget_allocated_val>0 || budget_allocated_val<0 )	{
	bootbox.confirm("The total budget has changed. Are you sure you want to apply the changes?", function(result) {
				 //console.log("Confirm result: "+result);
				 // toastr.info("Confirm result: "+result);
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
		$('#myTab li:eq(1)').css({'display':'none'});
		});
	});
  $(document).ready(function(){
	$('#reallocationclose').click(function(){
		$('#myTab li:eq(0) a').tab('show');
		$('#myTab li:eq(2)').css({'display':'none'});
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
	
function getChannelPerformance(){ 	
	try{
 		 $.ajax({
 		       type : "POST",
 		       url : "/channelPerformance.lin",
 		       cache: false,
 		       data : {startDate:startDate,endDate:endDate},
 		       dataType: 'json',
 		       success: function (data) {	    	  
 		           $.each(data, function(index, element) {
 		        	   if(index == 'channelPerformanceList'){
 		        		  var dataList=data['channelPerformanceList'];
 	 		        	   $("#just-table tbody tr").remove();
 	 		        	   var listIndex=0;
 	 		        	   for(key in  dataList){
 	 		        		  var dtoObject = dataList[key];
 	 		        		  var id="channelPerformance_"+listIndex;
 	 		        		   
 	 		        		  var row = "<tr id='"+id
 	 		        		  			+"' onclick=javascript:showChannelPerformancePopup('"+id
 	 		        		  			+"') class='even gradeC' ><td id='"+id+"_title' rel='popover'>"
 	 		        		  			+dtoObject.channelName+"</td><td id='"+id+"_eCPM'>$"+dtoObject.eCPM+"</td>";
 	     			          
 	 		        		  if(dtoObject.CHG < 0 || dtoObject.percentageCHG < 0){
 	 		        			 row = row+"<td width='56px'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+dtoObject.CHG
 	 		        			          +"</td><td width='56px'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
 	 		        			          +"<span id='"+id+"_changePercent' >"+dtoObject.percentageCHG
 	 	             			          +"%</span></td>";
 	 		        			        
 	 		        		  }else{
 	 		        			 row = row+"<td width='56px'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+dtoObject.CHG
 	        			          +"</td><td width='56px'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
 	        			          +"<span id='"+id+"_changePercent' >"+dtoObject.percentageCHG
 	        			          +"%</span></td>"; 		        			  
 	 		        		  }
 	 		        		   row=row+ "<td id='"+id+"_impressionDelivered' class=''>"+dtoObject.impressionsDelivered
 	      			                    +"</td><td id='"+id+"_clicks' class=''>"+dtoObject.clicks
 	      			                    +"</td><td id='"+id+"_payout' class=''>$"+dtoObject.payout
 	      			                    +"</td></tr>";
 	 		        		   
 	 		        		   $("#just-table tbody").append(row);
 	 		        		  listIndex++;
 	 		        	   } 
 		        	   }
 		        	  

 		           });
 		          
 		       },
 		       error: function(jqXHR, exception) {
 		    	 //  alert("getChannelPerformance: exception:"+exception);
 		        }
 			
 		  });
	}catch(error){
		//alert("getChannelPerformance: error:"+error);
	}
 		 
 }
 
function showChannelPerformancePopup(id){
	var title=$('#'+id+'_title').html();
	var chgPercent=$('#'+id+'_changePercent').html();
	/*var chgPercent=$('#'+id+'_changePercent').html();
    var eCPM=$('#'+id+'_eCPM').html();
    var impressionDelivered=$('#'+id+'_impressionDelivered').html();
    var clicks=$('#'+id+'_clicks').html();
    var payout=$('#'+id+'_payout').html();*/
    	
	if(chgPercent.indexOf('-')< 0){
		subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Up.png'width='15'></span>";
		subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:#52ad00;'>"+chgPercent+"</span></span>";
	}else{
		subTitle="<span style='margin-left:2px;'><img src='img/Arrow2Down.png'width='15'></span>";
		subTitle=subTitle+"<span style='margin-left:10px;margin-top:2px;color:red;'>"+chgPercent+"</span></span>";
	}
	
	var contentDiv="";
	
	try{	  
	    $.ajax({
		  async: false,
	      type : "POST",
	      url : "/loadChannelPerformancePopup.lin",
	      cache: false,
	      data : {channelName:title,startDate:startDate,endDate:endDate},		    
	      dataType: 'json',
	      success: function (data) {
	         $.each(data, function(index, element) {
	             if(index =='popUpDTO' && element !=null){	            	
	            	 var eCPM=element['CPM'];
	            	 var impressionDelivered=element['impressionDeliveredInSelectedTime'];
	            	 var clicks=element['clicksInSelectedTime'];
	            	 var payout=element['payout'];
	            	 var CTR=element['ctrInSelectedTime'];
	            	    
	            	 var contentDiv='<div id="popover_content_wrapper">'
	                 +'<div class="popheading_outer">'
	                 +'<div id="popheading" class="popup_heading">'
	                 
	                 +'<div class="pop_heading_left_name"><b></b></div>'
					 +'<div class="pop_heading_left_value"></div>'
					 +'<div class="pop_heading_right_name"><b></b></div>'
					 +'<div class="pop_heading_right_value"></div></div>'
					  
	           	  +'<div class="sub_heading"><span class="sub_heading_left"><b>'+timePeriod+'</b></span>'
	           	  +'<span class="sub_heading_right"><b>MTD</b></span></div>'
	           	  +'<div class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Impressions Delivered:</div>'
	           	  +'<div class="popup_content_left_value">'+impressionDelivered+'</div>'
	           	  +'<div class="popup_content_right_value">'+impressionDelivered*5+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Clicks:</div>'
	           	  +'<div class="popup_content_left_value">'+clicks+'</div>'
	           	  +'<div class="popup_content_right_value">'+clicks*2+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">CTR(%):</div>'
	           	  +'<div class="popup_content_left_value">'+CTR+'%</div>'
	           	  +'<div class="popup_content_right_value">'+parseFloat(CTR*2/3).toFixed(2)+'%</div>'
	           	  
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">eCPM</div>'
	           	  +'<div class="popup_content_left_value">$'+eCPM+'</div>'
	           	  +'<div class="popup_content_right_value">$'+parseFloat(eCPM*2).toFixed(2)+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Payout:</div>'
	           	  +'<div class="popup_content_left_value">$'+payout+'</div>'
	           	  +'<div class="popup_content_right_value">$'+parseFloat(payout*3/2).toFixed(2)+'</div>'
	           	  
	           	  +'</div><div id="popup_chart_div" style="width: 50px; height: 50px;"></div>'
	           	  +'<div style="margin-top:157px;background-color:#FDEFBC;">'
	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;margin-left:10px;margin-top:10px;" ' 
	           	  +' onclick=viewPerformance("'+title.replace(/ /g, "&#32;")+'")>View Trends</a>'
	           	  +'<a class="btn btn-inverse medium" href="#" data-toggle="tab" style="margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;" ' 
	           	  +' onclick=javascript:reallcation("'+title.replace(/ /g, "&#32;")+'")>Reallocation</a>'
	           	  +'</div></div>';		            	
	           	 
	           	 var chartData=element['chartData'];
	           	
	           	 makePopUP(id,title,subTitle,contentDiv,chartData);
	           }           
	             
	         });
	         
	         
	     },
	     error: function(jqXHR, exception) {
	   	   alert('Exception:'+exception);
	     }
	   });   
	  }catch(error){
		 alert('error:'+error);
	}	  
	
}

function showSellThroughPopup(id){
	var title=$('#'+id+'_title').html();
	var subTitle="<span style='margin-left:2px;'></span>"
		         +"<span style='margin-left:10px;margin-top:2px;color:red;'></span></span>";
	
	var contentDiv="";
	
	try{	  
	    $.ajax({
	    	url : "/sellThroughData.lin",
	 	    cache: false,
	 	    data : {property:title,startDate:startDate,endDate:endDate},		    
	        dataType: 'json',
	        success: function (data) {
	          $.each(data, function(index, element) {
	             if(index =='popUpDTO' && element !=null){	            	
	            	
	            	 var impressionReserved=element['impressionReserved'];
	            	 var impressionAvailable=element['impressionAvailable'];
	            	 var impressionForcasted=element['impressionForcasted'];
	            	 var sellThroughRate=element['sellThroughRate'];
	            	    
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
	           	  +'<div class="popup_content_name">Impressions available:</div>'
	           	  +'<div class="popup_content_left_value">'+impressionAvailable+'</div>'
	           	  +'<div class="popup_content_right_value">'+impressionAvailable*5+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Impressions reserved:</div>'
	           	  +'<div class="popup_content_left_value">'+impressionReserved+'</div>'
	           	  +'<div class="popup_content_right_value">'+impressionReserved*4+'</div>'
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">Impression forcasted:</div>'
	           	  +'<div class="popup_content_left_value">'+impressionForcasted+'</div>'
	           	  +'<div class="popup_content_right_value">'+impressionForcasted*3+'</div>'
	           	  
	           	  +'</div><div  class="popup_content_outer">'
	           	  +'<div class="popup_content_name">SellThroughRate</div>'
	           	  +'<div class="popup_content_left_value">'+sellThroughRate+'%</div>'
	           	  +'<div class="popup_content_right_value">'+parseFloat(sellThroughRate*3/2).toFixed(2)+'%</div>'
	           	
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
	   	   alert('showSellThroughPopup: Exception:'+exception);
	     }
	   });   
	  }catch(error){
		 alert('showSellThroughPopup: error:'+error);
	}	  
	
}

function getPerformanceByProperty(){
	  try{
	 		 $.ajax({
	 		       type : "POST",
	 		       url : "/performanceByProperty.lin",
	 		       cache: false,
	 		       data : {startDate:startDate,endDate:endDate},
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {	        	   
	 		        	   if(index== 'performanceByPropertyList'){
	 		        		  var dataList=data['performanceByPropertyList'];
		 		        	   $("#dtable1 tbody tr").remove();
		 		        	   var idIndex=0;
		 		        	   for(key in  dataList){
		 		        		  var id="performanceByProperty_"+idIndex;
		 		        		  var dtoObject = dataList[key];
		 		        		  var row = "<tr onclick=javascript:showPerformanceByPropertyPopup('"+id+"') class='even gradeC'>" +
		 		        		  		"<td id='"+id+"_title' rel='popover'>"
		 		        			    +dtoObject.name+"</td><td>$"
		 		        			    +dtoObject.eCPM+"</td>";
		 		        		  
		 		        		  if(dtoObject.CHG < 0 || dtoObject.percentageCHG < 0){
		 		        			 row=row + "<td width='56px'><img src='img/Arrow2Down.png' width='11' height='12' style='margin-right:5px;'>$"
		 		        			         +dtoObject.CHG+"</td><td width='56px'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
		 		        			         +dtoObject.percentageCHG+"%</td>";
		 		        			}else{
		 		        			   row=row + "<td width='56px'><img src='img/Arrow2Up.png' width='11' height='12' style='margin-right:5px;'>$"
		 		        				       +dtoObject.CHG+"</td><td width='56px'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"
		 		        				       +dtoObject.percentageCHG+"%</td>";
		 		        		  }
		 		        		  
		 		        		  row=row+"<td class=''>"+dtoObject.impressionsDelivered+"</td><td class=''>"+dtoObject.clicks+"</td><td class=''>$"+dtoObject.payout+"</td></tr>";
		 		        		  
		 		        		  $("#dtable1 tbody").append(row);
		 		        		  idIndex++;
		 		        	   }
	 		        	   } 

	 		           });
	 		          
	 		       },
	 		       error: function(jqXHR, exception) {
	 		    	  //alert("getPerformanceByProperty: exception: "+exception);
	 		        }
	 			
	 		  });
	  }catch(error){
		  //alert("getPerformanceByProperty: error: "+error);
	  }	 		 
  }
  

function showPerformanceByPropertyPopup(id){
	var title=$('#'+id+'_title').html();
	var subTitle="<span style='margin-left:2px;'></span>"
		         +"<span style='margin-left:10px;margin-top:2px;color:red;'></span></span>";
	
	var contentDiv="";
	
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
	           	  +'<div class="popup_content_right_value">'+parseFloat(CPM*3).toFixed(2)+'%</div>'
	           	  
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
	   	   alert('showPerformanceByPropertyPopup: Exception:'+exception);
	     }
	   });   
	  }catch(error){
		 alert('showPerformanceByPropertyPopup: error:'+error);
	}	  
	
}


   function getActualPublisherData(){	
	 		 $.ajax({
	 		       type : "POST",
	 		       url : "/actualPublisherData.lin",
	 		       cache: false,
	 		       data : {
	 					startDate : startDate,
	 					endDate : endDate
	 				},	
	 		        dataType: 'json',
	 		        success: function (data) {
	 		           $.each(data, function(index, element) {	        	   
	 		        	   var dataList=data['actualPublisherDataList'];
	 		        	   $("#actualPublisher tbody tr").remove();
	 		        	   for(key in  dataList){	 		        		   
	 		        		  var dtoObject = dataList[key];
	 		        		  if(dataList.length>0)
	 		        			{
	 		        			 var row = "<tr class='even gradeC'><td>"+dtoObject.date+"</td><td>"+dtoObject.requests+"</td><td width='56px'>"+dtoObject.served+"</td><td width='56px'>"+dtoObject.impressionsDelivered+"</td><td class=''>"+dtoObject.fillRate+"%</td><td class=''>"+dtoObject.clicks+"</td><td class=''>"+parseFloat(dtoObject.CTR).toFixed(2)+"%</td><td class=''>$"+parseFloat(dtoObject.revenue).toFixed(2)+"</td><td class=''>$"+parseFloat(dtoObject.eCPM).toFixed(2)+"</td><td class=''>$"+parseFloat(dtoObject.RPM).toFixed(2)+"</td></tr>";
	 		        			}
	 		        		 else {							
	 							tableTR = '<tr class="odd gradeX"><td></td>'
	 									+ '<td class="">No data found</td>'
	 									+ '<td></td><td></td></tr>';
	 						}
	 		        		   $("#actualPublisher tbody").append(row);
	 		        	   }
	 		        	
	 		           });
	 		          
	 		       },
	 		       error: function(jqXHR, exception) {
	 		        }
	 			
	 		  });
	 		 
	}
  
  function actualLineGeneration() {
	 
		$.ajax({
			type : "POST",
			url : "/acualLineChartPublisher.lin",
			cache : false,
			data : {
				startDate : startDate,
				endDate : endDate
			},	
			dataType : 'json',
			success : function(data) {
				var mapObj = data['headerMap'];
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
				
			},
			error : function(jqXHR, exception) {
			}
		});
	}
	
  
	var total = 0;
	var pages = 0;
	var counter = 0;
	 
	function getSellThroughData(){
	 	 $.ajax({
	 	       type : "POST",
	 	       url : "/sellThroughData.lin",
	 	       cache: false,
	 	       data : {
					startDate : startDate,
					endDate : endDate
				},	
	 	       dataType: 'json',
	 	       success: function (data) {	    	  
	 	           $.each(data, function(index, element) {
	 	        	   if(index== 'sellThroughDataTotal'){
	 	        		   var totalData=data['sellThroughDataTotal'];
		 	        	   pages = totalData / 10;
		 	        	   var r = totalData % 10;
		 	        	   if(r!=0){
		 	        			pages = parseInt(pages+1); 
		 	        		}
		 	        	
		 	        	  $("#STR ul li").remove();

		 	        	   if(totalData>0){
		 	        		   getNext(1);   
		 	        		}
		 	        	   
		 	        	   if(pages > 1){
		 	        		   var row1 = "<li><a href='javascript:void(0);' onclick='decrease();'>Previous</a></li>";
		 		        		 $("#STR ul").append(row1);
		 	        		}
		 	        	   
		 	        	   for(i=1; i<=pages;i++){
		 	        		 var row1 = "<li><a href='javascript:void(0)'; onclick='getNext("+i+")'>"+i+"</a></li>";
		 	        		 $("#STR ul").append(row1);
		 	        		}
		 	        	   
		 	        	   if(pages > 1){
		 	        		   var row1 = "<li><a href='javascript:void(0);' onclick='increase();'>Next</a></li>";
		 		        		 $("#STR ul").append(row1);
		 	        		}
	 	        	   }
	 	        	  
	 	        		   
	 	        	 	  

	 	           });
	 	          
	 	       },
	 	       error: function(jqXHR, exception) {
	 	        }
	 		
	 	  });
	 	 
	  }
	  
	  
	  function getNext(pageNo){	
	 		counter=pageNo;
	 		var dataList;
	 		 $.ajax({
	 		       type : "POST",
	 		       url : "/sellThroughData.lin?counter="+pageNo,
	 		       data : {
						startDate : startDate,
						endDate : endDate
					},	
	 		       cache: false,
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {
	 		        	   if(index=='sellThroughDataList'){
	 		        		  dataList=data['sellThroughDataList'];
	 		        		  //alert(dataList.length);
	 		        		  $("#dtable tbody tr").remove();
	 			        	  var a=1;
	 			        	  for(key in  dataList){
	 			        		   var id="sellThroughRate_"+a;	 		        		  
	 			        		   if(a<=10){		        		   
	 			        		     var dtoObject = dataList[key];
	 			        		     var row ="<tr id='"+id
	 		 		        		  			+"' onclick=javascript:showSellThroughPopup('"+id
	 		 		        		  			+"')  class='odd gradeX'><td id='"+id+"_title' rel='popover'>"
	 		 		        		  			+dtoObject.property+"</td><td>"+dtoObject.adUnit+"</td><td>"
	 		 		        		  			+dtoObject.forecastedImpressions+"</td><td>"
	 		 		        		  			+dtoObject.availableImpressions+"</td><td>"
	 		 		        		  			+dtoObject.reservedImpressions+"</td><td>"
	 		 		        		  			+dtoObject.sellThroughRate+"%</td></tr>";
	 			        		     $("#dtable tbody").append(row);
	 			        		     a++;
	 			        		   }
	 			        	  }
	 		        	   }
	 		        	   
	 		           });
	 		           
	 		         
		        	  
	 		       },
	 		       error: function(jqXHR, exception) {
	 		    	   
	 		        }
	 			
	 		  });
	 		 
	 	 }
	  
	  
	  function increase()
	  {
	 	 if(counter<pages)
	 	{
	 		 counter++;
	 	}
	 	 getNext(counter);
	 	 
	  }
	  
	  function decrease()
	  {
	 	 if(counter>1)
	 	{
	 		counter--;
	 	}
	 	 getNext(counter);
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
			 		    	  alert("getPublisherDropDown: exception:"+exception);
			 		        }
			 			
			 		  });
		  }catch(error){
			  alert("getPublisherDropDown: error:"+error);
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
			 		    	  alert("loadPublisherHeader: exception:"+exception);
			 		        }
			 			
			 		  });
		  }catch(error){
			  alert("loadPublisherHeader: error:"+error);
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
	    +'<div class="summary_bar"><div  style="">PAYOUTS($)(Estimated)</div><br>'
	    +'<div class="summary_value">$'+payout+'</div></div>';
		
		return headerDivData;
	}
	
	/*function loadChannelByPublisher(publisherName){		
		$("#channelTrendsDropDown ul").empty();
		var listData="";
		var jsonResponse;
		
		try{
			
			$.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadChannels.lin",
	 		       cache: false,
	 		       data : {publisherName:publisherName},
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {	 		        	  
	 		        	   if (index == 'channelPerformanceList') {
								jsonResponse=element;
								$.each(element,function(newIndex,newElement) {	
          			         	   var id='channel_trends_val_'+newIndex;
       			        		   listData=listData+"<li><a href=javascript:selectChannel('"+
       			        		   id+"')><span id='"+id+"'>"+
       		         		             newElement.channelName+"</span></a></li>";
          			         		   
								 });	
								 $("#channelTrendsDropDown ul").append(listData);
								 var firstName=jsonResponse[0].channelName;
								 if(firstName != undefined && firstName !=null){
								        $("#channel_trends_val").html(firstName);
								  }else{
								        $("#channel_trends_val").html('All');							        	
								  }	
						  }	

	 		           });
	 		          
	 		          headerDiv= getPublisherTrendsAnalysisHeader(
		 		        		// jsonResponse[0].sites,			 		        		
		 		        		 'ALL', jsonResponse[0].requests,
		 		        		 jsonResponse[0].impressionsDelivered,
		 		        		 jsonResponse[0].fillRate,
		 		        		 jsonResponse[0].clicks,
		 		        		 jsonResponse[0].CTR,
		 		        		 jsonResponse[0].revenue,
		 		        		 jsonResponse[0].eCPM,
		 		        		 jsonResponse[0].RPM);
		 			  $('#publisher_trends_analysis_header').html(headerDiv);
	 		       },
	 		       error: function(jqXHR, exception) {
	 		    	  alert("loadChannelByPublisher: exception:"+exception);
	 		        }
	 			
	 		  });
			
		}catch(error){
			 alert("loadChannelByPublisher: error:"+error);
		}
	}
	*/

	function getPublisherInventoryRevenueHeader(site,delivered,mobileApp,mobileWeb,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
		+'<div class="" style="width:10%;float:left;border-radius: 4px 4px 4px 4px;margin-left: 1%;">'
		+'<div class="summary_bar" >'
		+'<div style="">SITES</div>'
		+'<div class="summary_value">'+ parseFloat(site).toFixed(0) + ' </div>'
			
		+'		</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'<div style="">MOBILE WEB</div>'
		+'<div class="summary_value">'+ parseFloat(mobileWeb).toFixed(0) + ' </div>'
			
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'<div style="">MOBILE APP</div>'
		+'<div class="summary_value">'+ parseFloat(mobileApp).toFixed(0) + ' </div>'

		+'			</div>'
		+'</div>'
		+'<div class="" style=" margin-left: 3%;width: 18%;float:left;border-radius: 4px 4px 4px 4px;">'
		+'<div class="summary_bar">'
		+'<div style="">IMPRESSIONS DELIVERED</div>'
		+'<div class="summary_value">'+ parseFloat(delivered).toFixed(0) + ' </div>'
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'<div style="">CLICKS</div>'
		+'<div class="summary_value">'+ parseFloat(clicks).toFixed(0) + ' </div>'
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'<div style="">CTR(%)</div>'
		+'<div class="summary_value">'+ parseFloat(CTR).toFixed(2) + ' %</div>'
		+'			</div>'
		+'</div>'
		+'<div class="" style=" margin-left: 3%;width: 20%;float:left;border-radius: 4px 4px 4px 4px;">'
		+'<div class="summary_bar">'
		+'<div style="">eCPM(Estimated)</div>'
		+'<div class="summary_value">$'+ parseFloat(eCPM).toFixed(2) + ' </div>'
		+'</div>'
		+'<div class="mychart" id="balance" style="width:35px;float:left;margin-left:15px;margin-top:16px;"></div>'
		+'<div class="summary_bar" style="float:left;clear:both;">'
		+'<div style="">RPM(Estimated)</div>'
		+'<div class="summary_value">$'+ parseFloat(RPM).toFixed(2) + ' </div>'
			
		+'</div>'
		+'<div class="mychart" id="clicks" style="width:35px;float:left;margin-left:18px;margin-top:16px;"></div>'
		 
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'<div style="">PAYOUTS($)(Estimated)</div>'
		+'<div class="summary_value">$'+ parseFloat(payOuts).toFixed(0) + ' </div>'
		+'			</div>'
		+'</div>'
		
		+'<div class="easypie" style="float: right;">'
		+'<div class="percentage easyPieChart" data-percent="68"><span>68</span>%</div>'
		+'<div class="easypie-text">FILL RATE</div>	</div>';

		return headerDivData;
		
	}
	
	
	function getPublisherTrendsAnalysisHeader(requests,delivered,fillRate,clicks,CTR,eCPM,RPM,payOuts){
		var headerDivData="";
		headerDivData=headerDivData
	   /* +'<div class="summary_bar"><div  style="">SITES</div><br>'
		+'<div class="summary_value">'+sites+'</div></div>'
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
	    +'<div class="summary_value">$'+RPM+'</div></div>';*/

		
		+'<div class="" style="width:15%;float:left;border-radius: 4px 4px 4px 4px;margin-left:1%;">'
		+'<div class="summary_bar" >'
		+'	<div style="">REQUESTS</div>'
		+'	<div class="summary_value"> '+ parseFloat(requests).toFixed(0) + ' </div>'
			
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'	<div style="">DELIVERED</div>'
		+'	<div class="summary_value">'+parseFloat(delivered).toFixed(0)+ '</div>'
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'	<div style="">FILL RATE</div>'
		+'	<div class="summary_value">'+parseFloat(fillRate).toFixed(2)+ '</div>'
		+'</div>'
		+'</div>'
		+'<div class="" style=" margin-left: 3%;width: 15%;float:left;border-radius: 4px 4px 4px 4px;">'
		+'<div class="summary_bar">'
		+'	<div style="">IMPRESSIONS</div>'
		+'	<div class="summary_value">'+parseFloat(delivered).toFixed(0)+ '</div>'
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'	<div style="">CLICKS</div>'
		+'	<div class="summary_value">'+parseFloat(clicks).toFixed(0)+ '</div>'
		+'</div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'	<div style="">CTR(%)</div>'
		+'	<div class="summary_value">'+parseFloat(CTR).toFixed(2)+ '%</div>'
		+'</div>'
		+'</div>'
		+'<div class="" style=" margin-left: 3%;width: 21%;float:left;border-radius: 4px 4px 4px 4px;">'
		+'<div class="summary_bar">'
		+'	<div style="">eCPM($)</div>'
		+'	<div class="summary_value">$'+parseFloat(eCPM).toFixed(2)+ '</div>'
		+'</div>'
		+'<div class="mychart" id="subscribe" style="width:35px;float:left;margin-left:15px;margin-top:16px;"></div>'
		+'<div class="summary_bar" style="float:left;clear:both;">'
		+'	<div style="">RPM($)</div>'
		+'	<div class="summary_value">$'+parseFloat(RPM).toFixed(2)+ '</div>'
		+'</div>'
		+'<div class="mychart" id="support" style="width:35px;float:left;margin-left:18px;margin-top:16px;"></div>'
		+'<div class="summary_bar" style="float:none;clear:both;">'
		+'	<div style="">PAYOUTS($)</div>'
		+'	<div class="summary_value">$'+parseFloat(payOuts).toFixed(2)+ '</div>'
		+'</div>'
		+'</div>'
		+'		<div class="easypie" style="float: right;">'
		+'		<div class="percentage2 easyPieChart" data-percent="68">'
		+'			<span>68</span>%'
		+'			<canvas width="150" height="150"></canvas>'
		+'		</div>'
		+'		<div class="easypie-text">FILL RATE</div>'
		+'	</div>';

		
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
			 		    	//  alert("loadPublisherAllocationHeader: exception:"+exception);
			 		        }
			 			
			 		  });
		  }catch(error){
			  //alert("loadPublisherAllocationHeader: error:"+error);
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
	
/*	function loadChannelByName(channelName){
		//$("#channelTrendsDropDown ul").empty();
		var listData="";
		var jsonResponse;
		var publisherName=$("#publisher_trends_val").text();
		try{
			
			$.ajax({
	 		       type : "POST",			 		   
	 		       url : "/loadChannels.lin",
	 		       cache: false,
	 		       data : {channelName:channelName},
	 		       dataType: 'json',
	 		       success: function (data) {	    	  
	 		           $.each(data, function(index, element) {	 		        	  
	 		        	   if (index == 'channelPerformanceList') {
								jsonResponse=element;
								$.each(element,function(newIndex,newElement) {	
          			         	   var id='channel_trends_val_'+newIndex;
       			        		   listData=listData+"<li><a href=javascript:selectChannel('"+
       			        		   id+"')><span id='"+id+"'>"+
       		         		             newElement.channelName+"</span></a></li>";
          			         		   
								 });	
								 //$("#channelTrendsDropDown ul").append(listData);
								 var firstName=jsonResponse[0].channelName;
								 publisherName=jsonResponse[0].publisherName;
								 if(firstName != undefined && firstName !=null){
								        $("#channel_trends_val").html(firstName);								        
								 }else{
								        $("#channel_trends_val").html('All');							        	
								 }	
								 $("#publisher_trends_val").html(publisherName);
								 
								
								 
						  }else if(index=='headerMap'){
							  var mapObj = data['headerMap'];
							  getAllLineGraphsForPublisherTrendsAnalysis(mapObj);
						  }else if(index=='actualPublisherDataList'){
							  var dataList=data['actualPublisherDataList'];
							  getActualPublisherData(dataList);
						  }	

	 		           });
	 		          
	 		          headerDiv= getPublisherTrendsAnalysisHeader(
		 		        		// jsonResponse[0].sites,			 		        		
		 		        		 'ALL', jsonResponse[0].requests,
		 		        		 jsonResponse[0].impressionsDelivered,
		 		        		 jsonResponse[0].fillRate,
		 		        		 jsonResponse[0].clicks,
		 		        		 jsonResponse[0].CTR,
		 		        		 jsonResponse[0].revenue,
		 		        		 jsonResponse[0].eCPM,
		 		        		 jsonResponse[0].RPM);
		 			  $('#publisher_trends_analysis_header').html(headerDiv);
	 		       },
	 		       error: function(jqXHR, exception) {
	 		    	  alert("loadChannelByName: exception:"+exception);
	 		        }
	 			
	 		  });
			
		}catch(error){
			 alert("loadChannelByName: error:"+error);
		}
	}
	*/
	
function viewPerformance(channelName){
		$('#myTab li:eq(1)').css({'display':'inline'});
		$('#myTab li:eq(1) a').tab('show');	
		loadChannelByName(channelName);
}

function reallcation(channelName){
		$('#myTab li:eq(2)').css({'display':'inline'});
		$('#myTab li:eq(2) a').tab('show');
		//loadChannelByName(channelName);
}

/*function getActualPublisherData(dataList) {
	$("#actualPublisher tbody tr").remove();
	for (key in dataList) {
		var dtoObject = dataList[key];
		if (dataList.length > 0) {
			var row = "<tr class='even gradeC'><td>" + dtoObject.date
					+ "</td><td>" + dtoObject.requests
					+ "</td><td width='56px'>" + dtoObject.served
					+ "</td><td width='56px'>" + dtoObject.impressionsDelivered
					+ "</td><td class=''>" + dtoObject.fillRate
					+ "%</td><td class=''>" + dtoObject.clicks
					+ "</td><td class=''>" + dtoObject.CTR
					+ "%</td><td class=''>$" + dtoObject.revenue
					+ "</td><td class=''>$" + dtoObject.eCPM
					+ "</td><td class=''>$" + dtoObject.RPM + "</td></tr>";
		} else {
			tableTR = '<tr class="odd gradeX"><td></td>'
					+ '<td class="">No data found</td>'
					+ '<td></td><td></td></tr>';
		}
		$("#actualPublisher tbody").append(row);
	}
}*/

function getAllLineGraphsForPublisherTrendsAnalysis(mapObj) {

	//var mapObj = data['headerMap'];
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
		  

  function getReallocationPublisherData() {
	  
	  var title=$('#publisher_allocation_val').html();
	  var count = 0;
	  $.ajax({
		  type : "POST",
		  url : "/reallocationPublisherData.lin",
		  cache : false,
		  data : {
			  startDate : startDate,
			  endDate : endDate,
			  publisherName : title
		  },
		  dataType : 'json',
		  success : function(data) {
			  $.each(data, function(index, element) {
				  if(index=='reallocationDataList'){
					  var dataList = data['reallocationDataList'];					 
					  $("#reallocationPublisher tbody tr").remove();
					  for (key in dataList) {
				
						  var dtoObject = dataList[key];
						  if (dataList.length > 0) {
							  count++;
							  var row = "<tr class='even gradeC'><td><span class='badge badge-success'>"+count+"</span>"+
                              "<img src='img/DT/sort_desc.png' width='28' height='38' style='margin-right:9px;margin-left:15px;margin-top:-5px;'>" +
                              "</td><td>"  	
                              + dtoObject.channelName + "</td><td width='56px'>$"
                              + dtoObject.eCPM + "</td><td width='56px'>"
                              + parseFloat(dtoObject.fillRate).toFixed(2)
                              + "%</td><td class=''>" + dtoObject.impressionsDelivered
                              + "</td><td class=''>" + dtoObject.clicks
                              + "</td><td class=''>" + dtoObject.revenue
                              + "</td><td class=''>$" + parseFloat(dtoObject.floorCPM).toFixed(2)
                              + "</td><td class=''>$" + parseFloat(dtoObject.CPC).toFixed(2)
                              + "</td></tr>";
						  } else {
							  tableTR = '<tr class="odd gradeX"><td></td>'
								  + '<td class="">No data found</td>'
								  + '<td></td><td></td></tr>';
						  }
						  $("#reallocationPublisher tbody").append(row);
					  }
					  
				  }

			  });
	

		  },
		  error : function(jqXHR, exception) {
		  }

	  });
	  
   /* var title=$('#publisher_allocation_val').html();
		 
	 $.ajax({
		type : "POST",
		url : "/reallocationPublisherData.lin",
		cache : false,
		data : {
			startDate : startDate,
			endDate : endDate,
			publisherName : title
		},
		dataType : 'json',
		success : function(data) {
			$.each(data, function(index, element) {
				var dataList = data['reallocationDataList'];
				$("#publisherAllocationDropDown tbody tr").remove();
				for (key in dataList) {
					var dtoObject = dataList[key];
					
					if (dataList.length > 0) {
						var row = "<tr class='even gradeC'><td>"
								+ dtoObject.date + "</td><td>"
								+ dtoObject.channelName + "</td><td width='56px'>$"
								+ dtoObject.eCPM + "</td><td width='56px'>"
								+ parseFloat(dtoObject.fillRate).toFixed(2)
								+ "%</td><td class=''>" + dtoObject.impressionsDelivered
								+ "</td><td class=''>" + dtoObject.clicks
								+ "</td><td class=''>" + dtoObject.revenue
								+ "</td><td class=''>$" + parseFloat(dtoObject.floorCPM).toFixed(2)
								+ "</td><td class=''>$" + parseFloat(dtoObject.CPC).toFixed(2)
								+ "</td></tr>";
					} else {
						tableTR = '<tr class="odd gradeX"><td></td>'
								+ '<td class="">No data found</td>'
								+ '<td></td><td></td></tr>';
					}
					$("#publisherAllocationDropDown tbody").append(row);
					reallocationChart();
				}

			});

		},
		error : function(jqXHR, exception) {
			//alert("getReallocationPublisherData:error:"+error);
		}

	});*/
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
  
  function tabClickValue(tabId){
  if(tabId == 'inv_rev'){	  
	 	  $('#submit_calender').attr('onclick','loadAllData()');
  }else if(tabId == 'tre_ana'){
	  	  $('#submit_calender').attr('onclick','loadAllData()');
  }else if(tabId == 'rea_inv'){
	 	  $('#submit_calender').attr('onclick','loadAllData()');
  }else if(tabId == 'indus_new'){
	  $('#submit_calender').attr('onclick','loadAllData()');
  }
  }
  
  
  function getPublisherInventoryRevenueHeaderData() {
	  var title=$('#publisher_allocation_val').html();
	  //alert(allChannelName);
	  var count = 0;
	  //alert("hi");
	
	  $.ajax({
		  type : "POST",
		  url : "/loadPublisherInventoryRevenueHeaderDate.lin",
		  cache : false,
		  data : {
			  startDate : startDate,
			  endDate : endDate,
			  publisherName : title			 
		  		},
			  dataType: 'json',
		       success: function (data) {	    	
		    	  // alert(data);
		           $.each(data, function(index, element) {
		        	  // alert(index);
		        	  if (index == 'publisherInventoryRevenurList') {
							jsonResponse=element;	
							//alert(element);
					  }	
		           });
//alert("dfgdfgd");
		          headerDiv= getPublisherInventoryRevenueHeader(
		        		  	 jsonResponse[0].site,
		        		  	 jsonResponse[0].delivered,
	 		        		 jsonResponse[0].mobileApp,
	 		        		 jsonResponse[0].mobileWeb,
	 		        		 jsonResponse[0].clicks,
	 		        		 jsonResponse[0].CTR,
	 		        		 jsonResponse[0].eCPM,
	 		        		 jsonResponse[0].RPM,
	 		        		 jsonResponse[0].payOuts);
		        //  alert("dfgf");
				  $('#publisher_inventory_revenue_header').html(headerDiv);
		       },
		       error: function(jqXHR, exception) {
		    	 // alert("getPublisherInventoryRevenueHeaderData: exception:"+exception);
		        }
		  
		  });
	 		 
 }  
 function getPublisherTrendAnalysisHeaderData() {
	  var title=$('#publisher_allocation_val').html();
	  //alert(allChannelName);
	  var count = 0;
	  //alert("hi");
	
	  $.ajax({
		  type : "POST",
		  url : "/loadPublisherTrendsAnalysisHeaderDate.lin",
		  cache : false,
		  data : {
			  startDate : startDate,
			  endDate : endDate,
			  publisherName : title,
			  channelName : allChannelName
		  		},
			  dataType: 'json',
		       success: function (data) {	    	
		    	   //alert(data);
		           $.each(data, function(index, element) {
		        	   //alert(index);
		        	  if (index == 'publisherTrendAnalysisHeaderList') {
							jsonResponse=element;	
							//alert(element);
					  }	
		           });
		           
		           
		           
		                    
		           
		          // alert(jsonResponse[0].requests)
		          headerDiv= getPublisherTrendsAnalysisHeader(
	 		        		 jsonResponse[0].requests,
	 		        		 jsonResponse[0].delivered,
	 		        		 jsonResponse[0].fillRate,
	 		        		 jsonResponse[0].clicks,
	 		        		 jsonResponse[0].CTR,
	 		        		 jsonResponse[0].eCPM,
	 		        		 jsonResponse[0].RPM,
	 		        		 jsonResponse[0].payOuts);
				  $('#publisher_trends_analysis_header').html(headerDiv);
		       },
		       error: function(jqXHR, exception) {
		    	  //alert("getPublisherTrendAnalysisHeaderData: exception:"+exception);
		        }
		  
		  });
	 		 
 }
