

var allChannelName = '';
var selectedPublisher = '';
var selectedPublisherId = '';
var previousSelectedPublisherId = '';

var deliveringLineItems = new Array();
var readyLineItems = new Array();
var pausedLineItems = new Array();
var needsCreativesLineItems = new Array();
var inventoryReleasedLineItems = new Array();
var pendingApprovalLineItems = new Array();
var completedLineItems = new Array();
var draftLineItems = new Array();

$(window).load(function(){
	if(pageName == operationalViewPage) {		//  operationalViewPage is taken from UserDetailDTO
		 var url=location.href;
	     if(url.indexOf("#s7")>=0){  // Check if trafficking tab's tag is in URL
	   	   getTraffickingTabData();
	   	   $('#myTab1_2').attr('class', 'active');
	   	   $('#myTab1_3').attr('class', '');
	     }else{
	    	 loadFilterData();
	     }
	    
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
	     
	     $('#s10').hide();
	}
 });

  function operationsTabClickValue(obj){

	   /*if ($(obj).text() == "Diagnostic Tools"){
		   diiagnosticToolTabData();
		 }
	   if ($(obj).text() == "Trafficking"){
		   getTraffickingTabData();
		 }*/
	   if ($(obj).text() == campaignTab){
		   isTrendDefault =false;
		   location.href="/proposals.lin";
		   /*getMediaPlannerTabData();*/	
		 }
	   
 }

 function diiagnosticToolTabData() {
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
		 $('#myTab1 li:eq(1)').css({'display':'inline'});
		 $('#myTab1 li:eq(1) a').tab('show');
		 
		 $('#s10').hide();
		 $('#s5').show();
		 $('#s7').hide();
		 getAllReconciliationData();
 }
 
 function getTraffickingTabData(){
	 isTrendDefault =false;		   
	 $(".well").css({'display':'none'});
	 $(".slide-out-div").css({'display':'none'});
	 $("#agency_dropdown_advertiser").css({'display':'none'});
	 $("#advertiser_dropdown_advertiser").css({'display':'none'});
	 $("#propertyDropDownPublisher").css({'display':'none'});
	 $("#filter_Channel").css({'display':'inline'});
	 $("#publisher_outer").css({'min-height':'139px'});
	 $("#reportrange").css({'display':'none'});
	 $("#order_dropdown_text_publisher").css({'display':'none'});
	 $("#line_dropdown_text_publisher").css({'display':'none'});
	 $("#lineItems_dropdown_single_publisher").css({'display':'none'});
	 $("#order_dropdown_publisher").css({'display':'none'});
	 $("#line_itemName_publisher").css({'display':'none'});
	 setTimeout(function(){$("#btn-today").click()},1);
	 $('#myTab1 li:eq(2)').css({'display':'inline'});
	 $('#myTab1 li:eq(2) a').tab('show');
	 $('#s10').hide();
	 $('#s5').hide();
	 $('#s7').show();
	getCampaignTraffickingData();
 }

 function getMediaPlannerTabData(){	
	 /*isTrendDefault =false;
	 $(".well").css({'display':'inline'});
	 $(".slide-out-div").css({'display':'none'});
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
	 $('#myTab1 li:eq(3)').css({'display':'inline'});
	 $('#myTab1 li:eq(3) a').tab('show');	
	 $('#s10').show();	
	 $('#s5').hide();
	 $('#s7').hide();
	location.href="/proposals.lin";*/
}

/*=============for Trafficking===============================*/
google.load('visualization', '1', {'packages': ['geochart']});		 
google.load('visualization', '1.0', {'packages':['controls']});
google.load("visualization", "1", {packages:["corechart"]});

var campaignTraffickingData;
var traffickingCampaignList;
var popupTitle;
var popupSubtitle;
var startEventList;
var endEventList;
var page;
var totalPage;

function getCampaignTraffickingData() {
if(campaignTraffickingData == undefined || campaignTraffickingData == null || campaignTraffickingData.length <= 0)
{
	var jsonArray=[];

 	$("#col-filter tbody tr").remove();
	   
	   var loader = '<tr class="odd gradeX">'
		   +'<td colspan="5" style="color:red; text-align:center;">'
		   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
	   $("#col-filter tbody").append(loader);
 	
	   $("#campaignTraffickingLoader").css({'display':'inline'});
	   $("#traffickingCalendarLoader").css({'display':'inline'});
	 try{
		  $.ajax({
			  type : "POST",
			  url : "/loadCampaignTraffickingData.lin",
			  cache : false,
			  data : {
		    	   /*publisherName : selectedPublisher,*/
		    	   startDate:startDate,
		    	   endDate:endDate
			  		},
				  dataType: 'json',
			  success: function (data) {
			           $.each(data, function(index, element) {
			        	  if (index == 'campaignTraffickingDataList' ) {
			        		  campaignTraffickingData = element;
			        		  showCampaignTraffickingData();
			        		  $("#printViewIcon_trafficking").css("display", "block");
						  }
			        	  
			        	  if (index == 'startEventMap' ) 
			        	  {
			        		
			        		  startEventList = element;
			        		  for(key in startEventList)
			        		  {
				        		  var startTotal = startEventList[key];
				        		  var dates = key.split("/");
				        		  var dd = dates[1];
				        		  var mm = dates[0];
				        		  var yyyy = dates[2];
				        		  
				        		  var item={};
				        		  item['title']=startTotal+' Start'+'~'+key;
				        		  item['start']=new Date(yyyy, mm-1, dd);
				        		  jsonArray.push(item);
				        		  
				        		  
				        	  }
			        		 
						  }
			        	  
			        	  if (index == 'endEventMap' ) {
			        		  endEventList = element;
			        		  for(key in endEventList) {
				        		  var endTotal = endEventList[key];
				        		  var dates = key.split("/");
				        		  var dd = dates[1];
				        		  var mm = dates[0];
				        		  var yyyy = dates[2];
				        		  
				        		  
				        		  var item={};
				        		  item['title']=endTotal+' End'+'~'+key;
				        		  item['start']=new Date(yyyy, mm-1, dd);
				        		  jsonArray.push(item);
				        		  
				        	  }
			        		  
			        		 setup_calendar(jsonArray);
						  }
			        	  
			        	 
			        	  

			        	  
			           });
					  
			  },
			      
			  error: function(jqXHR, exception) {
			    	   
			  }
			  
			  });
		 		 
		  }catch(exception){
			  
		  }
	}//end of if
}

		function showCampaignTraffickingData()
		{
			if(campaignTraffickingData!= null && campaignTraffickingData.length > 0)
			{
				deliveringLineItems = new Array();
				readyLineItems = new Array();
				pausedLineItems = new Array();
				needsCreativesLineItems = new Array();
				inventoryReleasedLineItems = new Array();
				pendingApprovalLineItems = new Array();
				completedLineItems = new Array();
				draftLineItems = new Array();
				
				var deliveringTotal = 0;
				var readyTotal = 0;
				var pausedTotal = 0;
				var needsCreativeTotal = 0;
				var inventoryReleasedTotal = 0;
				var pendingApprovalTotal = 0;
				var completedTotal = 0;
				var draftTotal = 0;
				
				campaignStartDates = new Array();
				campaignEndDates = new Array();
				
				for(i = 0; i < campaignTraffickingData.length; i++)
				{
					if(campaignTraffickingData[i].status == "DRAFT")
					{
						draftTotal++;
						draftLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "DELIVERING")
					{
						deliveringTotal ++;
						deliveringLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "READY")
					{
						readyTotal ++;
						readyLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "PAUSED")
					{
						pausedTotal ++;
						pausedLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "COMPLETED")
					{
						completedTotal ++;
						completedLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "NEEDS_CREATIVES")
					{
						needsCreativeTotal ++;
						needsCreativesLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "PAUSED_INVENTORY_RELEASED")
					{
						inventoryReleasedTotal ++;
						inventoryReleasedLineItems.push(campaignTraffickingData[i]);
					}
					
					if(campaignTraffickingData[i].status == "PENDING_APPROVAL")
					{
						pendingApprovalTotal ++;
						pendingApprovalLineItems.push(campaignTraffickingData[i]);
					}
					
				}
				
				var pctDelivering = formatFloat((deliveringTotal / campaignTraffickingData.length)* 100, 2);
				var pctReady = formatFloat((readyTotal / campaignTraffickingData.length)* 100,2);
				var pctPaused = formatFloat((pausedTotal / campaignTraffickingData.length)* 100,2);
				var pctCompleted = formatFloat((completedTotal / campaignTraffickingData.length)* 100,2);
				var pctNeedsCreative = formatFloat((needsCreativeTotal / campaignTraffickingData.length)* 100,2);
				var pctInventoryReleased = formatFloat((inventoryReleasedTotal / campaignTraffickingData.length)* 100,2);
				var pctPendingApproval = formatFloat((pendingApprovalTotal / campaignTraffickingData.length)* 100,2);
				var pctDraft = formatFloat((draftTotal / campaignTraffickingData.length)* 100,2);
				
				$("#totalLineItems").text(campaignTraffickingData.length);
				
				$("#deliveryCount").text(deliveringTotal);
				$("#deliveryPct").css({'width':pctDelivering+"%"});
				//$("#deliveryPct").text(pctDelivering+"%");
				
				$("#readyCount").text(readyTotal);
				$("#readyPct").css({'width':pctReady+"%"});
				//$("#readyPct").text(pctReady+"%");
				
				$("#pausedCount").text(pausedTotal);
				$("#pausedPct").css({'width':pctPaused+"%"});
				//$("#pausedPct").text(pctPaused+"%");
				
				$("#completedCount").text(completedTotal);
				$("#completedPct").css({'width':pctCompleted+"%"});
				//$("#completedPct").text(pctCompleted+"%");
				
				$("#needCreativesCount").text(needsCreativeTotal);
				$("#needCreativesPct").css({'width':pctNeedsCreative+"%"});
				//$("#needCreativesPct").text(pctNeedsCreative+"%");
				
				$("#releasedCount").text(inventoryReleasedTotal);
				$("#releasedPct").css({'width':pctInventoryReleased+"%"});
				//$("#releasedPct").text(pctInventoryReleased+"%");
				
				$("#pendingApprovalCount").text(pendingApprovalTotal);
				$("#pendingApprovalPct").css({'width':pctPendingApproval+"%"});
				//$("#pendingApprovalPct").text(pctPendingApproval+"%");
				
				$("#draftCount").text(draftTotal);
				$("#draftPct").css({'width':pctDraft+"%"});
				//$("#draftPct").text(pctDraft+"%");
				
			}
			$("#campaignTraffickingLoader").css({'display':'none'});
			$("#traffickingCalendarLoader").css({'display':'none'});
			loadAllLineItems();
  
		}
		
		function loadSelectedLineItems(lineItemsType)
		{
			var selectedLineItems;
			var title;
			
			if(lineItemsType == "delivery")
			{
				selectedLineItems =  deliveringLineItems.slice(0);
				title = "DELIVERING";
			}
			else if(lineItemsType == "ready")
			{
				selectedLineItems =  readyLineItems.slice(0);
				title = "READY";
			}
			else if(lineItemsType == "paused")
			{
				selectedLineItems =  pausedLineItems.slice(0);
				title = "PAUSED";
			}
			else if(lineItemsType == "needsCreatives")
			{
				selectedLineItems =  needsCreativesLineItems.slice(0);
				title = "NEEDS CREATIVES";
			}
			else if(lineItemsType == "inventoryReleased")
			{
				selectedLineItems =  inventoryReleasedLineItems.slice(0);
				title = "INVENTORY RELEASED";
			}
			else if(lineItemsType == "pendingApproval")
			{
				selectedLineItems =  pendingApprovalLineItems.slice(0);
				title = "PENDING APPROVAL";
			}
			else if(lineItemsType == "draft")
			{
				selectedLineItems =  draftLineItems.slice(0);
				title = "DRAFT";
			}
			else if(lineItemsType == "completed")
			{
				selectedLineItems =  completedLineItems.slice(0);
				title = "COMPLETED";
			}
			
			title = "LIST OF CAMPAIGN "+title;
			$("#tableTitle h2").html(title);
			$("#campaign_selected").show();
			
			$("#campaign_selected_printView").show();
			
			var oTableTools = TableTools.fnGetInstance('selectedLineItemsTable');
		if ( oTableTools != null && oTableTools.fnResizeRequired()) {
			oTableTools.fnResizeButtons();
		}
			
			loadLineItems(selectedLineItems);
		}
		
		function loadLineItems(lineItemList)
		{
		   var tableTR="";
		   jQuery('#selectedLineItemsTable').dataTable().fnClearTable();
		   $("#selectedLineItemsTable_printView tbody tr").remove();
		   
			if(lineItemList != null && lineItemList.length > 0)
			{ 
				 jQuery('#selectedLineItemsTable').dataTable().fnClearTable();
            	 jQuery('#selectedLineItemsTable').dataTable().fnSettings()._iDisplayLength = 10;
            	 jQuery('#selectedLineItemsTable').dataTable().fnDraw();
				
				$.each(lineItemList,function (newIndex,lineItemObj){
				
	        		   (function(newIndex) {
	        			     setTimeout( function(newIndex) {
 		  

 		   var id="selected_"+lineItemObj.lineItemId;
 		   
 		   var newRowIndex = jQuery('#selectedLineItemsTable').dataTable().fnAddData( [
 		     lineItemObj.name,
 		     lineItemObj.goalQuantity,
 		     lineItemObj.cpm,
 		     lineItemObj.startDateTime,
 		     lineItemObj.endDateTime
 		   ]);
 		   
 		   var tr = jQuery('#selectedLineItemsTable').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
		     	tr.setAttribute('id', id);
		     	
		    var startDateTime = lineItemObj.startDateTime;
		    if(startDateTime == null){startDateTime = "";}
		    
		    var endDateTime = lineItemObj.endDateTime;
		    if(endDateTime == null){endDateTime = "";}
		     	
		   var row = "<tr><td style='text-align: left;'>"+lineItemObj.name+"</td> <td style='text-align: left;'>"+lineItemObj.goalQuantity+"</td> <td style='text-align: left;'>"+lineItemObj.cpm+"</td> "
          	       +"<td style='text-align: left;'>"+startDateTime+"</td> <td style='text-align: left;'>"+endDateTime+"</td> </tr>";
	        		
	        $("#selectedLineItemsTable_printView tbody").append(row);   	
		     	//tr.setAttribute("style","cursor: hand; cursor: pointer;");
   		   	//tr.setAttribute("onclick","showPerformanceMetricsPopup('"+performanceMetricsObj.campaignLineItem.replaceAll("'", "&#apos")+"', '"+id+"');");
   		   	
   		//for loop cont.../////
   		  
	   		       }, 10)
	   		       
	   		   })(newIndex)
					
					
				});
				
				if(lineItemList.length == 0)
				{
					$('#widget-id-0').css('display','none');
					//countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="10" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#selectedLineItemsTable tbody").append(tableTR);
					$("#selectedLineItemsTable_printView tbody").append(tableTR);
				}
				
				 
			}
			else
			{
				$('#widget-id-0').css('display','none');
				//countEmptyDataTables();
				tableTR='<tr class="odd gradeX">'
			        +'<td colspan="10" style="color:red; text-align:center;">'
				        +'<div class="widget alert alert-info adjusted">'
				        +'<i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong>'
				        +'</div>'
			        +'</td>'						      
			        +'</tr>';
				
				$("#selectedLineItemsTable tbody").append(tableTR);
				$("#selectedLineItemsTable_printView tbody").append(tableTR);
				
           }	
			$("#selectedLineItemsLoaderId").css("display", "none");
		  
	
		}
		
	function loadAllLineItems()
	{
		   var tableTR="";
		   //$("#performanceMetricsLoaderId").css("display", "block");
		   jQuery('#col-filter').dataTable().fnClearTable();
		   $("#col-filter-printView tbody tr").remove();
			
			if(campaignTraffickingData != null && campaignTraffickingData.length > 0)
			{ 
				 jQuery('#col-filter').dataTable().fnClearTable();
            	 jQuery('#col-filter').dataTable().fnSettings()._iDisplayLength = 10;
            	 jQuery('#col-filter').dataTable().fnDraw();
				
				$.each(campaignTraffickingData,function (newIndex,traffickingObj){
				
	        		   (function(newIndex) {
	        			     setTimeout( function(newIndex) {
 		  

 		   var id="trafficking_"+traffickingObj.lineItemId;
 		   
 		   var newRowIndex = jQuery('#col-filter').dataTable().fnAddData( [
 		     traffickingObj.name,
 		     traffickingObj.goalQuantity,
 		     traffickingObj.cpm,
 		     traffickingObj.startDateTime,
 		     traffickingObj.endDateTime
 		   ]);
 		   
 		   var tr = jQuery('#col-filter').dataTable().fnSettings().aoData[ newRowIndex[0] ].nTr;
		     	tr.setAttribute('id', id);
		   
		     	var startDateTime = traffickingObj.startDateTime;
			    if(startDateTime == null){startDateTime = "";}
			    
			    var endDateTime = traffickingObj.endDateTime;
			    if(endDateTime == null){endDateTime = "";}	
		     	
		     	
		   var row = "<tr><td style='text-align: left;'>"+traffickingObj.name+"</td> <td style='text-align: left;'>"+traffickingObj.goalQuantity+"</td> <td style='text-align: left;'>"+traffickingObj.cpm+"</td> "
       	   +"<td style='text-align: left;'>"+startDateTime+"</td> <td style='text-align: left;'>"+endDateTime+"</td> </tr>";
	        		
	       $("#col-filter-printView tbody").append(row); 	
		     	
		     	//tr.setAttribute("style","cursor: hand; cursor: pointer;");
   		   	//tr.setAttribute("onclick","showPerformanceMetricsPopup('"+performanceMetricsObj.campaignLineItem.replaceAll("'", "&#apos")+"', '"+id+"');");
   		   	
   		//for loop cont.../////
   		  
	   		       }, 10)
	   		       
	   		   })(newIndex)
					
					
				});
				
				if(campaignTraffickingData.length == 0)
				{
					$('#widget-id-1').css('display','none');
					//countEmptyDataTables();
					tableTR='<tr class="odd gradeX">'
				        +'<td colspan="10" style="color:red; text-align:center;">'
					        +'<div class="widget alert alert-info adjusted">'
					        +'<i class="cus-exclamation"></i>'
					        +'<strong>No records found for the selected filters</strong>'
					        +'</div>'
				        +'</td>'						      
				        +'</tr>';
					
					$("#col-filter tbody").append(tableTR);
					$("#col-filter-printView tbody").append(tableTR);
				}
				
				 
			}
			else
			{
				$('#widget-id-1').css('display','none');
				//countEmptyDataTables();
				tableTR='<tr class="odd gradeX">'
			        +'<td colspan="10" style="color:red; text-align:center;">'
				        +'<div class="widget alert alert-info adjusted">'
				        +'<i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong>'
				        +'</div>'
			        +'</td>'						      
			        +'</tr>';
				
				$("#col-filter tbody").append(tableTR);
				$("#col-filter-printView tbody").append(tableTR);
				
           }	
			$("#traffickingAllDataLoaderId").css("display", "none");
		  
	}
		
		
		function getCalendarDate(obj)
		{
			var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
			contentDiv = contentDiv +"<div id='traffickingPopupLoader' style='width:550px;height:30px;text-align:center'><img src='img/loaders/type4/light/46.gif' alt='loader'></div>";
			contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;'></div>";
			contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'>  </div></div>";
			makeTraffickingPopUP(contentDiv,"");
				
			var typeAndDate = $(obj).text().split("~");
			var selectedID = $(obj).attr("id");
			popupTitle = typeAndDate[0];
			popupSubtitle = typeAndDate[1];
			
			traffickingCampaignList = new Array();
			
			if(popupTitle.indexOf("Start") >= 0)
			{  
				for(i = 0; i < campaignTraffickingData.length; i++)
				{
					var traffickObj = campaignTraffickingData[i];
					
					if(traffickObj.startDateTime == popupSubtitle)
					{
						traffickingCampaignList.push(traffickObj);
					}
				}
			}
			else if(popupTitle.indexOf("End") >= 0)
			{
				for(i = 0; i < campaignTraffickingData.length; i++)
				{
					var traffickObj = campaignTraffickingData[i];
					if(traffickObj.endDateTime == popupSubtitle)
					{
						traffickingCampaignList.push(traffickObj);
					}
				}
			}
			
			if(traffickingCampaignList.length > 0)
			{
				initializePager();
			}
		}

		function initializePager()
		{
			page = 0;
			totalPage = 0;
			var rem = traffickingCampaignList.length % 5;
			
			if(rem != 0)
			{
				totalPage = ((traffickingCampaignList.length - rem) / 5) + 1;
			}
			else
			{
				totalPage = (traffickingCampaignList.length / 5);
			}
			getNext();
		}

		function getNext()
		{
			if(page < totalPage)
			{
				var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
				contentDiv = contentDiv +"<div id='traffickingPopupLoader' style='width:550px;height:30px;text-align:center'><img src='img/loaders/type4/light/46.gif' alt='loader'></div>";
				contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;'></div>";
				contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'>  </div></div>";
				makeTraffickingPopUP(contentDiv,"");
				
				var subList = new Array();
				page++;
				var lineItemIds = "";
				for(a = page*5-5; a < page*5 && a < traffickingCampaignList.length; a++)
				{
					subList.push(traffickingCampaignList[a]);
					lineItemIds = lineItemIds+"~"+traffickingCampaignList[a].lineItemId;
				}
				
				//showTraffickingPopup(subList,lineItemIds);
				showTableOnly(subList,lineItemIds);
				
			}
		}

		function getPrevious()
		{
			if(page > 1)
			{
				var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:10px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
				contentDiv = contentDiv +"<div id='traffickingPopupLoader' style='width:550px;height:30px;text-align:center'><img src='img/loaders/type4/light/46.gif' alt='loader'></div>";
				contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;'></div>";
				contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'>  </div></div>";
				makeTraffickingPopUP(contentDiv,"");
				
				page--;
				var subList = new Array();
				var lineItemIds = "";
				for(a = page*5-5; a < page*5 && a <= traffickingCampaignList.length; a++)
				{
					subList.push(traffickingCampaignList[a]);
					lineItemIds = lineItemIds+"~"+traffickingCampaignList[a].lineItemId;
				}
				//showTraffickingPopup(subList,lineItemIds);
				showTableOnly(subList,lineItemIds);
				
			}
			
		}

		function showTableOnly(traffickCampaignList,lineItemIds)
		{
			var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:4px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
			
			for(i = 0; i < traffickCampaignList.length; i++)
			{
				var objct = traffickCampaignList[i];
				
				var goalQuantity = objct.goalQuantity;
				var CPM = objct.cpm;
				var startDate = objct.startDateTime;
				var endDate = objct.endDateTime;
				var lineItem = objct.name;
				
				goalQuantity = formatInt(goalQuantity);
				
				var campaignName = lineItem.replace("'","");
				contentDiv = contentDiv + "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:4px;margin-right:10px;padding-top: 10px;min-height:80px;width:540px;float:left;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>"+lineItem+"</div><div style='width:80px;float:left;margin-left:5px;'><b>"+goalQuantity+"</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>$"+CPM+"</b></div><div style='width:90px;float:left;margin-left:4px;'><b>"+startDate+"</b></div><div style='width:90px;float:left;margin-left:4px;'><b>"+endDate+"</b></div></div>";
			}
			
			contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;clear:both;'></div>"
			contentDiv = contentDiv +"<div id='traffickingPopupLoader' style='width:550px;height:30px;text-align:center'><img src='img/loaders/type4/light/46.gif' alt='loader'></div>";
			contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'><div style='margin-bottom:8px;margin-left:10px;margin-top:10px;width:100px;float:left;'> <a id='idPrevious' class='btn btn-inverse medium' style='display:none;' href='#' data-toggle='tab'  onclick='getPrevious()'>Previous</a></div><div style='margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;'><a id='idNext' class='btn btn-inverse medium' style='display:none;' href='#' data-toggle='tab'  onclick='getNext()'>Next</a></div>  </div></div>"
			
			makeTraffickingPopUP(contentDiv,"");
			showTraffickingPopup(traffickCampaignList,lineItemIds);
		}


		function showTraffickingPopup(traffickCampaignList,lineItemIds)
		{
			var forcastDTOList;
			try{            	 
				 $.ajax({
				       type : "POST",			 		   
				       url : "/loadForcasts.lin",
				       cache: false,
				      data : {
				    	  		IdsOfLineItems : lineItemIds
					    	 },		
				       dataType: 'json',
				       success: function (data) {	
				    	 
				           $.each(data, function(index, element) {
				        	  if (index == 'forcastLineItemDTOList') {
				        		 forcastDTOList=element;
				        		 
				        		//	var contentDiv = "<div style='border-left:4px solid #333333;border-right:4px solid #333333;border-bottom:4px solid #333333;' ><div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:4px;margin-right:10px;padding-top: 10px;height:43px;width:540px;text-transform: uppercase;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>Campaign Name:</div><div style='width:80px;float:left;margin-left:5px;'><b>Goal Quantity</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>CPM</b></div><div style='width:90px;float:left;margin-left:4px;'><b>Start Date</b></div><div style='width:90px;float:left;margin-left:4px;'><b>End Date</b></div></div>";
				        			var chartData = "[['Line Item', 'Delivered','Forecasted','Forecasted', 'Forecasted']";
				        			
				        			for(i = 0; i < forcastDTOList.length; i++)
				        			{
				        				var objct = traffickCampaignList[i];
				        				var forcastDTO = forcastDTOList[i];
				        				
				        				var forcastDTOStatus = forcastDTO.status;
				        				var archived = forcastDTO.archived;
				        				var bookedImpressions = forcastDTO.bookedImpressions;
				        				var CPM = forcastDTO.ECPM;
				        				var startDate = forcastDTO.startDate;
				        				var endDate = forcastDTO.endDate;
				        				var lineItem = forcastDTO.lineItem;
				        				var delivered = forcastDTO.deliveredUnit;
				 		        		var forcasted;
				 		        		if(bookedImpressions!=null && delivered!=null){
				 		        			forcasted = (bookedImpressions - delivered);
				 		        		}
				 		        		bookedImpressions = formatInt(bookedImpressions);
				        				
				        				var campaignName = forcastDTO.lineItem.replace("'","");
				        			//	contentDiv = contentDiv + "<div style='border-bottom:1px solid #DDDDDD;margin-bottom:8px;margin-left:4px;margin-right:10px;padding-top: 10px;min-height:80px;width:540px;float:left;'> <div style='font-weight:bold;color:black;width:200px;float:left;'>"+lineItem+"</div><div style='width:80px;float:left;margin-left:5px;'><b>"+bookedImpressions+"</b></div> <div style='width:60px;float:left;margin-left:4px;'><b>$"+CPM+"</b></div><div style='width:90px;float:left;margin-left:4px;'><b>"+startDate+"</b></div><div style='width:90px;float:left;margin-left:4px;'><b>"+endDate+"</b></div></div>";
				        				//chartData = chartData +",['"+campaignName+"',"+delivered+","+forcasted+",0]";
				        				if (archived == "no"){
							        			if (forcastDTOStatus){
						        					chartData = chartData +",['"+campaignName+"',"+delivered+",0,"+forcasted+",0]";
						        					
						        				}else{
						        					chartData = chartData +",['"+campaignName+"',"+delivered+","+forcasted+",0,0]";
						        				}
				        				}else{
				        					chartData = chartData +",['"+campaignName+"',"+delivered+",0,0,"+forcasted+"]";
				        				}
				        			
				        			}
				        			
				        			chartData = chartData + "]";
				        		//	contentDiv = contentDiv + "<div id='calendarChart_div' style='width:400px;min-height:100px;clear:both;'></div>"
				        		//	contentDiv = contentDiv + "<div style='background-color:#FDEFBC;height:50px;'><div style='margin-bottom:8px;margin-left:10px;margin-top:10px;width:100px;float:left;'> <a id='idPrevious' class='btn btn-inverse medium' href='#' data-toggle='tab'  onclick='getPrevious()'>Previous</a></div><div style='margin-bottom:8px;float:right;margin-right:10px;margin-top:10px;'><a id='idNext' class='btn btn-inverse medium' href='#' data-toggle='tab'  onclick='getNext()'>Next</a></div>  </div></div>"
				        			traffickingPopupChart(chartData,archived,forcastDTOStatus);
				        			
							  }
				           });
				       },
				       error: function(jqXHR, error) {
				        }
				  });
				
				}catch(exception){
					
				}

		}

		function makeTraffickingPopUP(contentDiv,chartData){
			var id = "pop-up";
			if(lastPopUpId != 0 && lastPopUpId != id) {
				$('#'+lastPopUpId).popover('hide');
			}
			lastPopUpId = id;
			makeTraffickingPopUPDelay(id,contentDiv,chartData);
		}

		function makeTraffickingPopUPDelay(id,contentDiv,chartData) {

		var content='<div id="content">'+contentDiv+'</div>';

		$("#"+id).attr('data-original-title', "<span style='font-size:14px;font-weight:bold;padding-right:20px;word-wrap:break-word;'>"+popupTitle+"</span>"+"<br/>"+popupSubtitle);	
		$("#"+id).attr('data-toggle',"popover");

		var options={
				html: true,
		        trigger: 'manual',
		        content: content,
		        placement:'bottom',
		        title:popupTitle	            
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

		 //traffickingPopupChart(chartData);
		}

		function traffickingPopupChart(chartData,archived,forcastDTOStatus)
		{
			  var data = google.visualization.arrayToDataTable(eval(chartData));
			  var options = {
							  title: 'DELIVERY FORECAST',
						      vAxis: {title: 'LINE ITEM',  titleTextStyle: {color: 'red'}},
							  width:430,
							  height:200,
							  isStacked:true,
							  colors: ['black','red','green','gray'],
							  legend:{position: 'none'}
							  };
			

			  var chart = new google.visualization.BarChart(document.getElementById('calendarChart_div'));
			  chart.draw(data, options);
			  
				if(page < totalPage)
				{
					$("#idNext").css({'display':'block'});
				}
				else
				{
					$("#idNext").css({'display':'none'});
				}
				
				if(page > 1)
				{
					$("#idPrevious").css({'display':'block'});
				}
				else
				{
					$("#idPrevious").css({'display':'none'});
				}
				
				$("#traffickingPopupLoader").css({'display':'none'});
		}
	 