

/*=============for Diagnostic Tools========================*/
//Reconciliation functions Starts *******
		
var DFP_requests = 0;
var DFP_delivered = 0;
var DFP_passback = 0;
var demandPartnerRequests = 0;
var demandPartnerDelivered = 0;
var demandPartnerPassbacks = 0;
var varianceRequests = 0;
var varianceDelivered = 0;
var variancePassbacks = 0;

function getAllReconciliationData() {
	 var loader = '<tr class="odd gradeX"><td colspan="10" style="color:red; text-align:center;"><img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
	 $("#reconciliationSummaryTable tbody tr").remove();
	 $("#reconciliationSummaryTable_printView tbody tr").remove();
	 $("#reconciliationSummaryTable tbody").append(loader);
	 
	 $("#reconciliationDetailLoader").css("display", "block");
	 jQuery('#reconciliationDetailTable').dataTable().fnClearTable();
	 $("#reconciliationDetailTable_printView tbody tr").remove();
	 
	 
	 $('div[name="diagnosticToolsHeader"]').remove();
	 $("#diagnosticToolsHeaderLoader").css("display", "block");

	 try{
		 $.ajax({
		       type : "POST",			 		   
		       url : "/loadAllReconciliationData.lin",
		       cache: false,
		      data : {
		    	  startDate:startDate,
		    	  endDate:endDate,
		    	  publisherId : selectedPublisherId
			  		},
		       dataType: 'json',
		       success: function (data) {	    	  
		           $.each(data, function(index, element) {
		        	  if (index == 'reconciliationSummaryDataList') {
		        		 reconciliationSummaryData=element;
					  }
		        	  else if (index == 'reconciliationDetailsDataList') {
		        		 reconciliationDetailsData=element;
					  }
		           });
		          fillReconciliationSummaryTable();
		       },
		       error: function(jqXHR, error) {
		        }
		  });
		}catch(exception){
		}
}

function fillReconciliationSummaryTable() {
	 var selectedChannels = allChannelName.split(",");
	 var defaultChannel = '';
	 var channelDropDownHTML = '';
	 var count = 0;
	 var headerChannelArray = [];
	 
	 $("#reconciliationDetailLoader").css("display", "block");
	 jQuery('#reconciliationDetailTable').dataTable().fnClearTable();
	 $("#reconciliationDetailTable_printView tbody tr").remove();
	 $("#reconciliationSummaryTable tbody tr").remove();
	 $("#reconciliationSummaryTable_printView tbody tr").remove();
	 $("#selectChannel").html(channelDropDownHTML);
	 
	 clearTotal();
	 
	 if(reconciliationSummaryData != null && reconciliationSummaryData.length > 0 && selectedChannels != null && selectedChannels.length > 0) {
		 for(i=0; i < selectedChannels.length; i++) {
			 $.each(reconciliationSummaryData,function (newIndex,data){
				 if(data.channelName == selectedChannels[i]) {
					 if(count == 0) {
						 defaultChannel = selectedChannels[i];
					 }
					 headerChannelArray[count] = data.channelName;
					 count++;
					 var id = "reconciliationSummaryTr_"+i;
					 if(data.siteType == 'NA') {
						 var tableTr = "<tr id='"+id+"' class='even gradeC'>"
						 	+"<td id='"+id+"_title'>"+data.channelName+"</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"<td style='text-align: right;'>NA</td>"
						 	+"</tr>";
					 }
					 else {
						 channelDropDownHTML = channelDropDownHTML + "<option value='"+data.channelName+"'>"+data.channelName+"</option>";
						 
						 var DFP_requests = formatInt(data.DFP_requests);
						 var DFP_delivered = formatInt(data.DFP_delivered);
						 var DFP_passback = formatInt(data.DFP_passback);
						 var demandPartnerRequests = formatInt(data.demandPartnerRequests);
						 var demandPartnerDelivered = formatInt(data.demandPartnerDelivered);
						 var demandPartnerPassbacks = formatInt(data.demandPartnerPassbacks);
						 var varianceRequests = formatFloat(data.varianceRequests,4)+"%";
						 var varianceDelivered = formatFloat(data.varianceDelivered,4)+"%";
						 var variancePassbacks = formatFloat(data.variancePassbacks,4)+"%";
						 
						 if(DFP_requests == 0) {
						 	DFP_requests = 'NA';
							DFP_delivered = 'NA';
							varianceRequests = 'NA';
							varianceDelivered = 'NA';
						}
						if(demandPartnerRequests == 0) {
							demandPartnerRequests = 'NA';
							demandPartnerPassbacks = 'NA';
							varianceRequests = 'NA';
							variancePassbacks = 'NA';
						}
						 
						 var tableTr = "<tr id='"+id+"' class='even gradeC'>"
						 	+"<td id='"+id+"_title'>"+data.channelName+"</td>"
						 	+"<td style='text-align: right;'>"+DFP_requests+"</td>"
						 	+"<td style='text-align: right;'>"+DFP_delivered+"</td>"
						 	+"<td style='text-align: right;'>"+DFP_passback+"</td>"
						 	+"<td style='text-align: right;'>"+demandPartnerRequests+"</td>"
						 	+"<td style='text-align: right;'>"+demandPartnerDelivered+"</td>"
						 	+"<td style='text-align: right;'>"+demandPartnerPassbacks+"</td>"
						 	+"<td style='text-align: right;'>"+varianceRequests+"</td>"
						 	+"<td style='text-align: right;'>"+varianceDelivered+"</td>"
						 	+"<td style='text-align: right;'>"+variancePassbacks+"</td>"
						 	+"</tr>";
						 
						 countTotal(data);
				 	}
				 	$("#reconciliationSummaryTable tbody").append(tableTr);
				 	$("#reconciliationSummaryTable_printView tbody").append(tableTr);
				 	return false;
				 }
			 });
		 }
		 $("#diagnosticToolsHeaderLoader").css("display", "none");
		 $('div[name="diagnosticToolsHeader"]').remove();
		 for(k=headerChannelArray.length - 1; k>=0; k--) {
			 $("#diagnosticToolsHeader").after("<div name='diagnosticToolsHeader' class='widget alert alert-info-header adjusted' style='clear:none;float:left;margin-left:10px;box-shadow:none;border:none;margin-top: -6px;'>	<div style='float:left;margin-left:-21px;color:white;margin-right: 4px;'>	<strong style='text-transform: uppercase;'>"+ headerChannelArray[k] +"</strong> </div></div>");
		 }
		 $("#selectChannel").html(channelDropDownHTML);
		 if(count > 0) {
		 	fillTotal('reconciliationSummaryTable');
		 	fillReconciliationDetailsTable(defaultChannel);
		 }
		 else {
			 row='<tr class="odd gradeX"><td colspan="10" style="color:red; text-align:center;">'
				        +'<div class="widget alert alert-info adjusted"><i class="cus-exclamation"></i>'
				        +'<strong>No records found for the selected filters</strong></div></td></tr>';
			 
			 $("#reconciliationSummaryTable tbody").append(row);
			 $("#reconciliationSummaryTable_printView tbody").append(row);
			 
			 $("#reconciliationDetailTable tbody").append(row);
			 $("#reconciliationDetailTable_printView tbody").append(row);
			 
			 clearTotal();
		  }
		 
	 }
	 else {
		 row='<tr class="odd gradeX"><td colspan="10" style="color:red; text-align:center;">'
			        +'<div class="widget alert alert-info adjusted"><i class="cus-exclamation"></i>'
			        +'<strong>No records found for the selected filters</strong></div></td></tr>';
		 
		 $("#reconciliationSummaryTable tbody").append(row);
		 $("#reconciliationSummaryTable_printView tbody").append(row);
		 
		 $("#reconciliationDetailTable tbody").append(row);
		 $("#reconciliationDetailTable_printView tbody").append(row);
		 clearTotal();
	  }
	 $("#reconciliationDetailLoader").css("display", "none");
}

function fillReconciliationDetailsTable(channel) {
	 $('#reconciliationDetailsHeader h2').html('RECONCILIATION DETAILS - '+ channel);
	 $('#reconciliationDetailsHeader_printView h2').html('RECONCILIATION DETAILS - '+ channel);
	 $("#reconciliationDetailLoader").css("display", "block");
	 jQuery('#reconciliationDetailTable').dataTable().fnClearTable();
	 $("#reconciliationDetailTable_printView tbody tr").remove();
	 
  jQuery('#reconciliationDetailTable').dataTable().fnSettings()._iDisplayLength = 50;
  jQuery('#reconciliationDetailTable').dataTable().fnDraw();
	 
	 clearTotal();
	 if(reconciliationDetailsData != null && reconciliationDetailsData.length > 0) {
		 $.each(reconciliationDetailsData,function (newIndex,data){
			 if(data.channelName == channel) {
				 countTotal(data);
				 
				 var DFP_requests = formatInt(data.DFP_requests);
				 var DFP_delivered = formatInt(data.DFP_delivered);
				 var DFP_passback = formatInt(data.DFP_passback);
				 var demandPartnerRequests = formatInt(data.demandPartnerRequests);
				 var demandPartnerDelivered = formatInt(data.demandPartnerDelivered);
				 var demandPartnerPassbacks = formatInt(data.demandPartnerPassbacks);
				 var varianceRequests = formatFloat(data.varianceRequests,4)+"%";
				 var varianceDelivered = formatFloat(data.varianceDelivered,4)+"%";
				 var variancePassbacks = formatFloat(data.variancePassbacks,4)+"%";
				 
				 if(DFP_requests == 0) {
				 	DFP_requests = 'NA';
					DFP_delivered = 'NA';
					varianceRequests = 'NA';
					varianceDelivered = 'NA';
				}
				if(demandPartnerRequests == 0) {
					demandPartnerRequests = 'NA';
					demandPartnerPassbacks = 'NA';
					varianceRequests = 'NA';
					variancePassbacks = 'NA';
				}
				 
				 var id = "reconciliationDetailTr_"+i;
		         jQuery('#reconciliationDetailTable').dataTable().fnAddData( [
	        		     data.formattedDate,
	        		     "<div style='text-align: right;'>"+DFP_requests+"</div>",
	        		     "<div style='text-align: right;'>"+DFP_delivered+"</div>",
	        		     "<div style='text-align: right;'>"+DFP_passback+"</div>",
	        		     "<div style='text-align: right;'>"+demandPartnerRequests+"</div>",
	        		     "<div style='text-align: right;'>"+demandPartnerDelivered+"</div>",
	        		     "<div style='text-align: right;'>"+demandPartnerPassbacks+"</div>",
	        		     "<div style='text-align: right;'>"+varianceRequests+"</div>",
	        		     "<div style='text-align: right;'>"+varianceDelivered+"</div>",
	        		     "<div style='text-align: right;'>"+variancePassbacks+"</div>"
	    		   ]);
		         
		         var row = "<tr class='even gradeC'>"
				 	+"<td style='text-align: left;'>"+data.formattedDate+"</td>"
				 	+"<td style='text-align: right;'>"+DFP_requests+"</td>"
				 	+"<td style='text-align: right;'>"+DFP_delivered+"</td>"
				 	+"<td style='text-align: right;'>"+DFP_passback+"</td>"
				 	+"<td style='text-align: right;'>"+demandPartnerRequests+"</td>"
				 	+"<td style='text-align: right;'>"+demandPartnerDelivered+"</td>"
				 	+"<td style='text-align: right;'>"+demandPartnerPassbacks+"</td>"
				 	+"<td style='text-align: right;'>"+varianceRequests+"</td>"
				 	+"<td style='text-align: right;'>"+varianceDelivered+"</td>"
				 	+"<td style='text-align: right;'>"+variancePassbacks+"</td>"
				 	+"</tr>";
		         
		         $("#reconciliationDetailTable_printView tbody").append(row);
			 }
		 });
		 
		 // Fill total
		 /////////////////////////////////////////// Global variables
		 varianceRequests =  ((demandPartnerRequests - DFP_requests)*100)/DFP_requests;
		 varianceDelivered = ((demandPartnerDelivered - DFP_delivered)*100)/DFP_delivered;
		 variancePassbacks = ((demandPartnerPassbacks - DFP_passback)*100)/DFP_passback;
		 
		 DFP_requests = formatInt(DFP_requests);
		 DFP_delivered = formatInt(DFP_delivered);
		 DFP_passback = formatInt(DFP_passback);
		 demandPartnerRequests = formatInt(demandPartnerRequests);
		 demandPartnerDelivered = formatInt(demandPartnerDelivered);
		 demandPartnerPassbacks = formatInt(demandPartnerPassbacks);
		 varianceRequests = formatFloat(varianceRequests,4)+"%";
		 varianceDelivered = formatFloat(varianceDelivered,4)+"%";
		 variancePassbacks = formatFloat(variancePassbacks,4)+"%";
		 
		 if(DFP_requests == 0) {
		 	DFP_requests = 'NA';
			DFP_delivered = 'NA';
			varianceRequests = 'NA';
			varianceDelivered = 'NA';
		}
		if(demandPartnerRequests == 0) {
			demandPartnerRequests = 'NA';
			demandPartnerPassbacks = 'NA';
			varianceRequests = 'NA';
			variancePassbacks = 'NA';
		}
		 
		 jQuery('#reconciliationDetailTable').dataTable().fnAddData( [
			     "<div style='font-weight: bold;'>Total</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+DFP_requests+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+DFP_delivered+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+DFP_passback+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+demandPartnerRequests+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+demandPartnerDelivered+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+demandPartnerPassbacks+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+varianceRequests+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+varianceDelivered+"</div>",
			     "<div style='text-align: right; font-weight: bold;'>"+variancePassbacks+"</div>"
		   ]);
		 
		 var row = "<tr class='even gradeC'>"
			 	+"<td style='text-align: left;'>Total</td>"
			 	+"<td style='text-align: right;'>"+DFP_requests+"</td>"
			 	+"<td style='text-align: right;'>"+DFP_delivered+"</td>"
			 	+"<td style='text-align: right;'>"+DFP_passback+"</td>"
			 	+"<td style='text-align: right;'>"+demandPartnerRequests+"</td>"
			 	+"<td style='text-align: right;'>"+demandPartnerDelivered+"</td>"
			 	+"<td style='text-align: right;'>"+demandPartnerPassbacks+"</td>"
			 	+"<td style='text-align: right;'>"+varianceRequests+"</td>"
			 	+"<td style='text-align: right;'>"+varianceDelivered+"</td>"
			 	+"<td style='text-align: right;'>"+variancePassbacks+"</td>"
			 	+"</tr>";
	         
	         $("#reconciliationDetailTable_printView tbody").append(row);
		 
		 /////////////////////////////////////////// Global variables
	 }
	 else {
		 row='<tr class="odd gradeX"><td colspan="10" style="color:red; text-align:center;">'
			        +'<div class="widget alert alert-info adjusted"><i class="cus-exclamation"></i>'
			        +'<strong>No records found for the selected filters</strong></div></td></tr>';
		 
		 $("#reconciliationDetailTable tbody").append(row);
		 $("#reconciliationDetailTable_printView tbody").append(row);
		 clearTotal();
	 }
	 $("#reconciliationDetailLoader").css("display", "none");
}

function changeChannelDropDown() {
	 var selectedChannel = $('#selectChannel').val();
	 fillReconciliationDetailsTable(selectedChannel);
}

function countTotal(data) {
	 DFP_requests = DFP_requests + data.DFP_requests;
	 DFP_delivered = DFP_delivered + data.DFP_delivered;
	 DFP_passback = DFP_passback + data.DFP_passback;
	 demandPartnerRequests = demandPartnerRequests + data.demandPartnerRequests;
	 demandPartnerDelivered = demandPartnerDelivered + data.demandPartnerDelivered;
	 demandPartnerPassbacks = demandPartnerPassbacks + data.demandPartnerPassbacks;
}

function clearTotal() {
	 DFP_requests = 0;
	 DFP_delivered = 0;
	 DFP_passback = 0;
	 demandPartnerRequests = 0;
	 demandPartnerDelivered = 0;
	 demandPartnerPassbacks = 0;
	 varianceRequests = 0;
	 varianceDelivered = 0;
	 variancePassbacks = 0;
}

function fillTotal(tableId) {
	 
	 varianceRequests =  ((demandPartnerRequests - DFP_requests)*100)/DFP_requests;
	 varianceDelivered = ((demandPartnerDelivered - DFP_delivered)*100)/DFP_delivered;
	 variancePassbacks = ((demandPartnerPassbacks - DFP_passback)*100)/DFP_passback;
	 
	 DFP_requests = formatInt(DFP_requests);
	 DFP_delivered = formatInt(DFP_delivered);
	 DFP_passback = formatInt(DFP_passback);
	 demandPartnerRequests = formatInt(demandPartnerRequests);
	 demandPartnerDelivered = formatInt(demandPartnerDelivered);
	 demandPartnerPassbacks = formatInt(demandPartnerPassbacks);
	 varianceRequests = formatFloat(varianceRequests,4)+"%";
	 varianceDelivered = formatFloat(varianceDelivered,4)+"%";
	 variancePassbacks = formatFloat(variancePassbacks,4)+"%";
	 
	 if(DFP_requests == 0) {
	 	DFP_requests = 'NA';
		DFP_delivered = 'NA';
		varianceRequests = 'NA';
		varianceDelivered = 'NA';
	}
	if(demandPartnerRequests == 0) {
		demandPartnerRequests = 'NA';
		demandPartnerPassbacks = 'NA';
		varianceRequests = 'NA';
		variancePassbacks = 'NA';
	}
	 
		var tableTr = "<tr>"
	 	+"<td style='font-weight: bold;'>Total</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+DFP_requests+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+DFP_delivered+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+DFP_passback+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+demandPartnerRequests+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+demandPartnerDelivered+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+demandPartnerPassbacks+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+varianceRequests+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+varianceDelivered+"</td>"
	 	+"<td style='text-align: right; font-weight: bold;'>"+variancePassbacks+"</td>"
	 	+"</tr>";
	 	$('#'+tableId+' tbody').append(tableTr);
	 	$('#'+tableId+'_printView tbody').append(tableTr);
}

/****** Reconciliation functions Ends****/