
 try{
	google.load('visualization', '1', {	'packages' : [ 'geochart' ]	}); 
 }catch(err){
	
 }



String.prototype.replaceAll = function(stringToFind, stringToReplace) {
     if (stringToFind == stringToReplace) return this;
     var temp = this;
     var index = temp.indexOf(stringToFind);
     while (index != -1) {
         temp = temp.replace(stringToFind, stringToReplace);
         index = temp.indexOf(stringToFind);
     }
     return temp;
 }


$(window).load(function(){	
	showCurrentWeek();	
 });




function  barGraphGeneration(){
	 $.ajax({
	       type : "POST",
	       url : "/barGraphData.lin",
	       cache: false,
	       dataType: 'json',
	       success: function (data) {	  
	           $.each(data, function(index, element) {	        	   
	        	  var mapObj=data['map'];
	        	  var title =mapObj['title'];
	        	  var dataStr =mapObj['datastr'];
	        	  var divName = "bar_chart_div";
	        	  var vAxisTitle ="Year";
	      		  var vAxisColor ="green";
	        	  
	        	  drawBarGraph(divName,title,dataStr,vAxisTitle,vAxisColor);
	           });
	          
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
}
 
 function  pieGraphGeneration(){
	 $.ajax({
	       type : "POST",
	       url : "/pieGraphData.lin",
	       cache: false,
	       dataType: 'json',
	       success: function (data) {	  
	           $.each(data, function(index, element) {	        	   
	        	  var mapObj=data['map'];
	        	  var title =mapObj['title'];
	        	  var dataStr =mapObj['datastr'];
	        	  var divName = "pie_chart_div";
	      		  drawPieChart(divName,title, dataStr);
	           });
	          
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
}
 
function  geoGraphGeneration(){
	 $.ajax({
	       type : "POST",
	       url : "/geoGraphData.lin",
	       cache: false,
	       dataType: 'json',
	       success: function (data) {	  
	           $.each(data, function(index, element) {	        	   
	        	  var mapObj=data['map'];
	        	  var dataStr =mapObj['datastr'];
	        	  var divName = "geo_chart_div";
		      		var displayMode="markers";
		    		var region ="US";
		    		var colorAxisStr ="{colors: ['red', 'blue']}";
	      		    drawGeoChart(divName,dataStr,region,displayMode,colorAxisStr);
	           });
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
}


 
 function  lineGraphGeneration(){
	 $.ajax({
	       type : "POST",
	       url : "/lineGraphData.lin",
	       cache: false,
	       dataType: 'json',
	       success: function (data) {	  
	           $.each(data, function(index, element) {	        	   
	        	  var mapObj=data['map'];
	        	  var title =mapObj['title'];
	        	  var dataStr =mapObj['datastr'];
	        	  var divName = "line_chart_div";
 	      		  drawLineChart(divName,title,dataStr);
	           });
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
}
 
 function  areaGraphGeneration(divName,title){
	 $.ajax({
	       type : "POST",
	       url : "/areaGraphData.lin",
	       cache: false,
	       dataType: 'json',
	       success: function (data) {	  
	           $.each(data, function(index, element) {	        	   
	        	  var mapObj=data['map'];
	        	  //var title =mapObj['title'];
	        	  var dataStr =mapObj['datastr'];
	        	  //var divName = "chart_div_realloc1";
	          	  var graphWidth ="1000";
	          	  var graphheight ="400";
	          	  var hAxisTitle =mapObj['hAxisTitle'];
	          	  var titleTextStyle = "color: 'red'";
	          	  
	          	
	          	  drawAreaGraph(dataStr,title,graphWidth,graphheight,hAxisTitle,titleTextStyle,divName);
	           });
	       },
	       error: function(jqXHR, exception) {	          
	        }
	  });
} 

 
 function showCurrentWeek(){
		var today = new Date();
		var calStartDate = new Date();
		calStartDate.setDate(today.getDate() - 6);
		var calEndDate = new Date();

		$("#startDateId").val($.datepicker.formatDate('yy-mm-dd', calStartDate));
		$("#endDateId").val($.datepicker.formatDate('yy-mm-dd', calEndDate));
 }
 
 function dateSelection(dateId){
	    var today = new Date();
	    var startDate ="";
	    var endDate ="";
	    if(dateId=="ThisWeek" ){
			 startDate = new Date();
			 startDate.setDate(today.getDate() - 6);
			 endDate = new Date();
		}

	    if(dateId=="LastWeek"){
			 startDate = new Date();
			 startDate.setDate(today.getDate() - 13);
			 endDate = new Date();
			 endDate.setDate(today.getDate() - 7);
		} 
		   
		if(dateId=="ThirdWeek"){
			 startDate = new Date();
			 startDate.setDate(today.getDate() - 20);
			 endDate = new Date();
			 endDate.setDate(today.getDate() - 14);
		}   
		if(dateId=="ForthWeek"){
			 startDate = new Date();
			 startDate.setDate(today.getDate() - 27);
			 endDate = new Date();
			 endDate.setDate(today.getDate() - 21);
		}   
		if(dateId=="ThisMonth"){
			 var currentDate = $.datepicker.formatDate('yy-mm-dd', today);
			 var arr = currentDate.split("-");
			 var year = arr[0];
			 var month = arr[1];
			 var day = arr[2];
			 startDate = new Date(year+'/'+month+'/'+'01');
			 endDate = new Date();
		}   
		if(dateId=="LastMonth"){ 
			var c_month = today.getMonth();
			var c_year = today.getFullYear();
			if (c_month == 0) {  
				c_month = 11;  
				c_year = c_year - 1;  
		    } else {  
		    	c_month = c_month - 1;  
		    }  
			c_month =c_month+1;

			 var sd = c_year+'/'+c_month+'/'+'01';
			 var totalDays = getNumberOfDays(2013, 3);
			 var sdEnd = c_year+'/'+c_month+'/'+totalDays;
 			 startDate = new Date(sd);
             endDate = new Date(sdEnd);
		}
		
		 $("#startDateId").val($.datepicker.formatDate('yy-mm-dd', startDate));
		 $("#endDateId").val($.datepicker.formatDate('yy-mm-dd', endDate));
		 customCal($.datepicker.formatDate('yy-mm-dd', startDate),$.datepicker.formatDate('yy-mm-dd', endDate));

		 selectTimeInterval(dateId);
}
 
 function getNumberOfDays(year, month) {
	    var isLeap = ((year % 4) == 0 && ((year % 100) != 0 || (year % 400) == 0));
	    return [31, (isLeap ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
	}
 
 function callCalc(){
	 var startD =$("#startDateId").val();
	 var endDat = $("#endDateId").val();
	 customCal(startD,endDat);
 }
 
   $(window).load(function(){	
		
	 });
   
 function showRedMessage(msg) {
	$.jGrowl(msg, { header : '', theme : 'jGrowl_red' });
 }

 function showGreenMessage(msg) {
	$.jGrowl(msg, { header : '', theme : 'jGrowl_green' });
 }
 
 function zoomInPieChart(chartTitle,chartTypediv,dataStr,modalheaderHeight,modalheaderWidth) {			 
	 var options = {
		      title: chartTitle,
		      height : modalheaderHeight,
		      width : modalheaderWidth
		    };
	 
	 var $modal = $('.modal').modal({
		    show: false
		});
		$('#'+chartTypediv).on('click', function() {
			//alert("hi");
			$("#myModalLabel").html(chartTitle);
			 zoomInDrawPieChart("modalDivId",chartTitle,dataStr,options);
		    $modal.modal('show');
		});
 }
 
 function zoomInLineChart(chartTitle,align,dataStr,chartTypediv,color,modalheaderWidth,modalheaderHeight) {
	 /*var id = 'popupDiv'+chartTitle.trim().replaceAll(" ", "");
	 var contents = '<div id="'+id+'" style="margin: 0auto;"></div>';
	 $('#'+chartTypediv).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;'>"+chartTitle+"</span>");	
	 $('#'+chartTypediv).attr('data-toggle',"popover");
	 
	 var options={ 
				html: true,
	            trigger: 'manual',
	            content: contents,
	            placement:align,
	            title:chartTitle	            
		};
	
	 $('#'+chartTypediv).popover(options);
	 $('#'+chartTypediv).popover('show');
	 $('.popover-title').append('<br><button type="button" class="close1" style="opacity: 0.84;margin-top:-18px;">&times;</button>');
	 zoomInLineChartGeneration(id, chartTitle, dataStr, color);
	 $('.close1').click(function(e){
		$(".popover").hide();
	 });*/
	 
	 var $modal = $('.modal').modal({
		    show: false
		});
		$('#'+chartTypediv).on('click', function() {
			//alert("hi");
			$("#myModalLabel").html(chartTitle);
			zoomInLineChartGeneration("modalDivId", chartTitle, dataStr, color,modalheaderWidth,modalheaderHeight);
		    $modal.modal('show');
		});
 }

 function zoomInGeoChart(graphTitle,align,dataStr,chartTypediv,modalheaderWidth,modalheaderHeight) {
	 var id = 'popupDiv'+graphTitle.trim().replaceAll(" ", "");
	 var chartOptions = {
				region : "US",
				displayMode : "markers",
				resolution:'provinces',
				width : modalheaderWidth,
				height : modalheaderHeight,
				colorAxis : {colors: ['red', 'yellow', 'green'],displayMode: 'auto'}
				};
	 var contents = '<div id="'+id+'" style="margin: 0auto;"></div>';
	 $('#'+chartTypediv).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;'>"+graphTitle+"</span>");	
	 $('#'+chartTypediv).attr('data-toggle',"popover");
	 
	 var options={ 
				html: true,
	            trigger: 'manual',
	            content: contents,
	            placement:align,
	            title:graphTitle	            
		};
	
	 /*$('#'+chartTypediv).popover(options);
	 $('#'+chartTypediv).popover('show');
	 $('.popover-title').append('<br><button type="button" class="close1" style="opacity: 0.84;margin-top:-18px;">&times;</button>');
	 google.setOnLoadCallback(zoomInDrawGeoChart(id,dataStr,chartOptions));
	 $('.close1').click(function(e){
		$(".popover").hide();*/
	 var $modal = $('.modal').modal({
		    show: false
		});
		$('#'+chartTypediv).on('click', function() {
			//alert("hi");
			$("#myModalLabel").html(graphTitle);
			google.setOnLoadCallback(zoomInDrawGeoChart("modalDivId",dataStr,chartOptions));
		    $modal.modal('show');
		});
	 
 }
 
 function formatFloat(val,usa){
	 return accounting.formatNumber(val,usa,",");
 }
 
 function formatInt(val)
 {
	 val = parseFloat(val).toFixed(0);
	 val = val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	 return val;
	 	
 }
 
 function getFloatValue(value,digits){
		if(value== null || value =='' || value ==undefined){
			value="0.0";
		}else{
			value=parseFloat(value).toFixed(digits);
		}
		return value;
	}

	function getNumericValue(value){
		if(value== null || value =='' || value ==undefined){
			value="0";
		}
		return value;
	}
 
 function checkFilterForAdvertiserPerformance30Days(date1, date2) {
		var yr1 = parseInt(date1.substring(0, 4), 10);
		var mon1 = parseInt(date1.substring(5, 7), 10);
		var dt1 = parseInt(date1.substring(8, 10), 10);
		
		var yr2 = parseInt(date2.substring(0, 4), 10);
		var mon2 = parseInt(date2.substring(5, 7), 10);
		var dt2 = parseInt(date2.substring(8, 10), 10);
		
		var Date1 = new Date(yr1, mon1, dt1);
		var Date2 = new Date(yr2, mon2, dt2);
			
		var date_diff = Math.abs((Date2.getTime() - Date1.getTime())/(24*3600*1000));
        /*if (date_diff > 30){
        	if((advertisername != null && advertisername != undefined && advertisername.trim() != "") 
        		|| (agencyname != null && agencyname != undefined && agencyname.trim() != "")) {
        		return true;
        	}
        	else {
        		alert("Please select filters for date range not more than 30 days");
        		return false;
        	}
        }*/
        return true;
	}
 
 function checkFilterForAdvertiserTrends30Days(date1, date2) {
		var yr1 = parseInt(date1.substring(0, 4), 10);
		var mon1 = parseInt(date1.substring(5, 7), 10);
		var dt1 = parseInt(date1.substring(8, 10), 10);
		
		var yr2 = parseInt(date2.substring(0, 4), 10);
		var mon2 = parseInt(date2.substring(5, 7), 10);
		var dt2 = parseInt(date2.substring(8, 10), 10);
		
		var Date1 = new Date(yr1, mon1, dt1);
		var Date2 = new Date(yr2, mon2, dt2);
			
		var date_diff = Math.abs((Date2.getTime() - Date1.getTime())/(24*3600*1000));
		/*if (date_diff > 30){
		 	if((lineItemArr != null && lineItemArr != undefined && lineItemArr.trim() != "") 
		 		|| (ordername != null && ordername != undefined && ordername.trim() != "")) {
		 		return true;
		 	}
		 	else {
		 		alert("Please select filters for date range not more than 30 days");
		 		return false;
		 	}
		}*/
		return true;
	}
 
 function getDaysRemaining(date1, date2) {
		var yr1 = parseInt(date1.substring(0, 4), 10);
		var mon1 = parseInt(date1.substring(5, 7), 10);
		var dt1 = parseInt(date1.substring(8, 10), 10);
		
		var yr2 = parseInt(date2.substring(0, 4), 10);
		var mon2 = parseInt(date2.substring(5, 7), 10);
		var dt2 = parseInt(date2.substring(8, 10), 10);
		
		var Date1 = new Date(yr1, mon1, dt1);
		var Date2 = new Date(yr2, mon2, dt2);
			
		var date_diff = Math.abs((Date2.getTime() - Date1.getTime())/(24*3600*1000));
		return date_diff;
	}
 
 function getCurrentDate() {
	  var today = new Date();
	  var dd = today.getDate();
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

 function errorMessage(msg) {
	jQuery(window).load(function () {
		toastr.error(msg);
	});
}

function successMessage(msg) {
	jQuery(window).load(function () {
		toastr.success(msg);
	});
}

function selectAllFilterCheckbox() {
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
		 //$('#diagnosticToolsPublisher').html(allPublishersList[0].value);
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
	//var publisherViewHeaderChannelDivs = '';
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
  		//publisherViewHeaderChannelDivs = publisherViewHeaderChannelDivs + "<div class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: -6px;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"+ channelNameArray[j] +"</strong> </div></div>";
  	}
  //	$("#second_channel").after(publisherViewHeaderChannelDivs);
	//$("#first_channel").after(publisherViewHeaderChannelDivs);
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
		        			previousSelectedPublisherId = allPublishersList[0].id;
		        			$('#first_publisher').html(allPublishersList[0].value);
		        			$('#diagnosticToolsPublisher').html(allPublishersList[0].value);
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
		         defaultMethods();
		       },
		       error: function(jqXHR, error) {
		        }
		  });
		}catch(exception){
		}
}


function loadFilterDataByPublisher(publisherId) {
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
		       },
		       error: function(jqXHR, error) {
		        }
		  });
		}catch(exception){
		}
}

 
  