

/******************************************currently not in use methods of richMediaAdvertiser.js ************/

function getAdvertiserReallocationData() {
		$("#advertiser_reallocation_header tbody tr").remove();
		$("#reallocationItemTable tbody tr").remove();
		var loader = '<tr class="odd gradeX">'
			   +'<td colspan="13" style="color:red; text-align:center;">'
			   +'<img src="img/loaders/type4/light/46.gif" alt="loader"></td></tr>';
		   $("#reallocationItemTable tbody").append(loader);
		var row = "";
		
		   $.ajax({
		 		       type : "POST",
		 		      url : "/getRichMediaAdvertiserReallocation.lin",
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

function loadPerformanceMetrics(){
	var loadPerformanceMetricsKey =0;
	   var tableTR="";
	   $("#performanceMetricsLoaderId").css("display", "block");
	   jQuery('#performanceMetricsTable').dataTable().fnClearTable();	 
	  // updateAjaxCount(1 , 'loadPerformanceMetrics' );
	   try{	  
	    $.ajax({
	      type : "POST",
	      url : "/loadRichMediaPerformanceMetrics.lin",
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

function loadPerformanceSummaryHeaderData() {
	var divContent = "";
	 var advertiserPerformanceSummaryHeaderDTOList;
	 // updateAjaxCount(1 , 'loadPerformanceSummaryHeaderData' );
	 try{
		 $.ajax({
		       type : "POST",			 		   
		       url : "/loadRichMediaPerformanceSummaryHeaderData.lin",
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
	      url : "/loadRichMediaPerformerLineItems.lin",
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
      		   		/*+'<td onclick="showRichMedia()"><i class="cus-chart-bar"></i></td>'*/
      		   		+'<td onclick=javascript:loadRichMediaEventPopup("'+id+'")><i class="cus-chart-bar"></i></td>'
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
	      url : "/loadRichMediaNonPerformerLineItems.lin",
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
	      url : "/loadRichMediaMostActiveLineItems.lin",
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
	      url : "/loadRichMediaTopGainers.lin",
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
	      url : "/loadRichMediaTopLosers.lin",
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

function forcastLineGeneration() {
	//updateAjaxCount(1 , 'forcastLineGeneration' );
	var divNameImp = "chart_div_right3";
	var divNameClick = "chart_div_right1";
	var divNameCtr = "chart_div_right2";
	lineChart(divNameImp, 'FORECASTED IMPRESSIONS', "[['Date','Impressions'],['31',129.0]]", 'blue',graphWidth,height);
	lineChart(divNameClick, 'FORECASTED CLICKS', "[['Date','Clicks'],['31',3]]", 'green',graphWidth,height);
	lineChart(divNameCtr, 'FORECASTED CTR', "[['Date','CTR'],['31',2.33]]", 'red',graphWidth,height);
	$.ajax({
		type : "POST",
		url : "/forcastRichMediaLineChart.lin",
		cache : false,
		data : {
			  publisherName : selectedPublisher,
			  startDate : startDate,
			  endDate : endDate,
			  order : ordername,
			  lineItem : lineItemArr,
			 properties : SelectedProperty
			 },	
		success : function(data) {		
			var title = "";
			var mapObj = data['headerMap'];
			var impressionStr = mapObj['impression'];
			var clicksStr = mapObj['click'];
			var ctrStr = mapObj['ctr'];
			google.setOnLoadCallback(lineChart(divNameImp, 'FORECASTED IMPRESSIONS', impressionStr, 'blue',graphWidth,height));
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

function getForcastAdvertiserData(){
	$('#forcastAdvertiserDiv').css('display','block');
	$('#forcastAdvertiserTableLoaderId').css('display','block');
	jQuery('#forcastAdvertiserTable').dataTable().fnClearTable();
	var row = "";
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
		 		      	$('#forcastAdvertiserTableLoaderId').css('display','none');
		 		       },

		 		       error: function(jqXHR, exception) {
		 		        }
		 		  });
	$('#forcastAdvertiserDiv').css('display','none');
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

function setAdvertiserReallocationData(){
	if($("#budget_allocated").html()== 0 && budgetCheck){
		//errorMessage('DFP Validation Error ');
	
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
			array = array+idArr[0]+":"+valueArr[0]+";"+rbiValue+",";
		});
		
		   $.ajax({
 		       type : "POST",
 		      url : "/saveRichMediaAdvertiserReallocation.lin",
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
		 		    }
	 		     
	 		  });
	}else if($("#budget_allocated").html()!= 0){
		//alert("Balance Should Be Zero(0)")
		toastr.error('Total Adjusted Budget Should Be Equal To Total Budget ');
	}else if(!budgetCheck){
		toastr.error('Budget should be less than Total Remaining');
	}
	

}

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

var budgetCheck = true;
var noOfLines= 0;

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

$(document).ready(function() {
	$("#performanceMetricsTable tbody").delegate("tr", "click", function() {
		var lineItemName = $("td:eq(2)", this).text();
		$(this).attr("id","a1");
		$(this).attr("rel","popover");
		showPerformanceMetricsPopup(lineItemName, "a1");
		});
});

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

function showRichMedia(){
	 
	 $('#myModalRichMedia').modal('show');
	  setTimeout(drawChartcoupon,500);
	  setTimeout(drawChart,500);
}

function loadRichMediaEventGraph() {
var richMediaEventPopupList;
var table;
var customEvents;
$('#analyticsBoard1').css('display','none');
$('#analyticsBoard2').css('display','none');
$('#analyticsBoard3').css('display','none');
$('#analyticsBoard4').css('display','none');
try{
	 $.ajax({
	       type : "POST",			 		   
	       url : "/loadRichMediaEventGraph.lin",
	       cache: false,
	      data : {
	    	  advertiser:advertisername,
	    	  agency:agencyname,
	    	  startDate:startDate,
	    	  endDate:endDate
	    	  },
	       dataType: 'json',
	       success: function (data) {	    	  
	           $.each(data, function(index, element) {
	        	  if(index == 'richMediaGraphTable') {
	        		 table=element;
				  }
	        	 if(index == 'customEvents') {
	        		customEvents = element.split(',');
	        	 }
	           });
	          if(table != null && table.length  >0 && customEvents != null && customEvents.length  >0) {
       			$('#analyticsBoard1').css('display','block');
       			$('#analyticsBoard2').css('display','block');
       			$('#analyticsBoard3').css('display','block');
       			$('#analyticsBoard4').css('display','block');
       			drawVisualization(table, customEvents); 
   		 }
   		 else {
   			countEmptyDataTables();
   		 }
	       },
	       error: function(jqXHR, error) {
	    	  $('#richMediaEventGraphOuterDiv').html('');
	        }
	  });
	}catch(exception){
	}
}


function drawVisualization(table, customEvents) {
// Prepare the data
//alert(table);
var data = google.visualization.arrayToDataTable(eval("["+table+"]"));
// alert("data"+data)
var formatter = new google.visualization.NumberFormat({pattern:'#,###.##%'});
formatter.format(data, 8); // Apply formatter to second column

var view_coupondownload = new google.visualization.DataView(data);
view_coupondownload.setRows(view_coupondownload.getFilteredRows([{column: 3, value:'Coupon Btn Clicked'}]));


var view_findstore = new google.visualization.DataView(data);
view_findstore.setRows(view_findstore.getFilteredRows([{column: 3, value:'Find Store Btn Clicked'}]));
  
var view_clicktocalls= new google.visualization.DataView(data);
view_clicktocalls.setRows(view_clicktocalls.getFilteredRows([{column: 3, value: 'Click to call' }]));

var view_URL = new google.visualization.DataView(data);
view_URL.setRows(view_URL.getFilteredRows([{column: 3, value: 'http://instapreview.com/GolfsmithCoupon/GolfsmithUSA-Mobile_13-0'}]));



//  var tablen = new google.visualization.Table(document.getElementById('test_dataview'));
// tablen.draw(view); 

// Define a slider control for the Age column.
var CTRslider = new google.visualization.ControlWrapper({
'controlType': 'NumberRangeFilter',
'containerId': 'control1',
'options': {
 'filterColumnLabel': 'CTR%',
'ui': {'labelStacking': 'vertical'}
}
});

// Define a category picker control for the Site column
var SitecategoryPicker = new google.visualization.ControlWrapper({
'controlType': 'CategoryFilter',
'containerId': 'control2',
'options': {
 'filterColumnLabel': 'Site',
 'ui': {
 'labelStacking': 'horizontal',
   'allowTyping': false,
   'allowMultiple': false
 }
}
});

// Define a category picker control for the Format column
var FormatcategoryPicker = new google.visualization.ControlWrapper({
'controlType': 'CategoryFilter',
'containerId': 'control3',
'options': {
 'filterColumnLabel': 'Format',
 'ui': {
 'labelStacking': 'horizontal',
   'allowTyping': false,
   'allowMultiple': false
 }
}
});

// Define a category picker control for the Format column
var MarketcategoryPicker = new google.visualization.ControlWrapper({
'controlType': 'CategoryFilter',
'containerId': 'control4',
'options': {
 'filterColumnLabel': 'Market',
 'ui': {
 'labelStacking': 'horizontal',
   'allowTyping': false,
   'allowMultiple': false
 }
}
});
// Define a category picker control for the Format column
var AdSizecategoryPicker = new google.visualization.ControlWrapper({
'controlType': 'CategoryFilter',
'containerId': 'control5',
'options': {
 'filterColumnLabel': 'Ad Size',
 'ui': {
 'labelStacking': 'horizontal',
   'allowTyping': false,
   'allowMultiple': false
 }
}
});
// Define a category picker control for the Format column
var CouponcategoryPicker = new google.visualization.ControlWrapper({
'controlType': 'CategoryFilter',
'containerId': 'control6',
'options': {
 'filterColumnLabel': 'Coupon',
 'ui': {
 'labelStacking': 'horizontal',
   'allowTyping': false,
   'allowMultiple': false
 }
}
});

// Define a Pie chart
var pie_imp = new google.visualization.ChartWrapper({
'chartType': 'PieChart',
'containerId': 'chart1',
'options': {
 'legend': 'none',
 'title': '',
 'pieSliceText': 'label',
 'pieHole': '0.6',
 'chartArea':{'width':'97%','height':'97%'},
 'colors': ['#23adde', '#86af4e', '#1fbbae'],

},
// Instruct the piechart to use colums 0 (Name) and 3 (Donuts Eaten)
// from the 'data' DataTable.

'dataTable' : google.visualization.data.group(
       view_coupondownload, [0],
         [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])

});
pie_imp.draw();
var pie_click = new google.visualization.ChartWrapper({
 'chartType': 'PieChart',
 'containerId': 'chart_click',
 'options': {
   'legend': 'none',
   'title': '',
   'pieSliceText': 'label',
   'pieHole': '0.6',
   'chartArea':{'width':'97%','height':'97%'},
   'colors': ['#ee3684', '#fedb0a'],
 },
 // Instruct the piechart to use colums 0 (Name) and 3 (Donuts Eaten)
 // from the 'data' DataTable.
 'dataTable' : google.visualization.data.group(
         view_findstore, [1],
         [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
});
pie_click.draw();
var pie_ctr = new google.visualization.ChartWrapper({
 'chartType': 'PieChart',
 'containerId': 'chart_ctr',
 'options': {
   'legend': 'none',
   'title': '',
   'pieSliceText': 'label',
   'pieHole': '0.6',
   'chartArea':{'width':'97%','height':'97%'},
   'colors': ['#02be88', '#024d60', '#1fbbae'],
 },
 // Instruct the piechart to use colums 0 (Name) and 3 (Donuts Eaten)
 // from the 'data' DataTable.
 'dataTable' : google.visualization.data.group(
         view_clicktocalls, [2],
         [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
});
pie_ctr.draw();
var pie_info = new google.visualization.ChartWrapper({
 'chartType': 'PieChart',
 'containerId': 'chart_info',
 'options': {
   'legend': 'none',
   'title': '',
   'pieSliceText': 'label',
   'pieHole': '0.6',
   'chartArea':{'width':'97%','height':'97%'},
   'colors': ['#9dcb08', '#eace01'],
 },
 // Instruct the piechart to use colums 0 (Name) and 3 (Donuts Eaten)
 // from the 'data' DataTable.
 'dataTable' : google.visualization.data.group(
         view_URL, [0],
         [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
});
pie_info.draw();


// Define a Bar chart
//  var bar = new google.visualization.ChartWrapper({
//  'chartType': 'BarChart',
//  'containerId': 'chart3',
//  'options': {
//    'legend': 'none',
//   'title': 'Impressions By Site',
//  },
// Instruct the barchart to use colums 0 (Name) and 3 (Donuts Eaten)
// from the 'data' DataTable.
// 'view': {'columns': [3, 8]}
//});  
// Define a table
var table = new google.visualization.ChartWrapper({
'chartType': 'Table',
'containerId': 'chart2',
'options': {
'allowHtml': true,
'page': 'enable',
'pageSize':'25'
},
view: {
   columns: [0,1,2,3,4,5,6,7,8]
}
});


var chart_core_imp = new google.visualization.ChartWrapper({
 'chartType': 'Table',
 'containerId': 'chart_core_imp',
 'options': {
     'width': '100%'
 },
 'dataTable':google.visualization.data.group(
         view_coupondownload, [0], 
           [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_imp.draw();
var chart_core_click = new google.visualization.ChartWrapper({
 'chartType': 'Table',
 'containerId': 'chart_core_click',
 'options': {
     'width': '100%'
 },
 'dataTable':google.visualization.data.group(
         view_findstore, [1],
           [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_click.draw();
var chart_core_ctr = new google.visualization.ChartWrapper({
 'chartType': 'Table',
 'containerId': 'chart_core_ctr',
 'options': {
     'width': '100%'
 },
 'dataTable':google.visualization.data.group(
         view_clicktocalls, [2],
           [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_ctr.draw();
var chart_core_info = new google.visualization.ChartWrapper({
 'chartType': 'Table',
 'containerId': 'chart_core_info',
 'options': {
     'width': '100%'
 },
 'dataTable':google.visualization.data.group(
         view_URL, [0],
           [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_info.draw();

// Create a dashboard
var chart_core_imp_total = new google.visualization.ChartWrapper({
 'chartType': 'Table',
 'containerId': 'chart_core_imp_total',
 'options': {
     'width': '100%'
 },
 'dataTable':google.visualization.data.group(
         view_coupondownload, [5], 
           [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_imp_total.draw();

var chart_core_click_total = new google.visualization.ChartWrapper({
'chartType': 'Table',
'containerId': 'chart_core_click_total',
'options': {
  'width': '100%'
},
'dataTable':google.visualization.data.group(
      view_findstore, [6], 
        [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_click_total.draw();

var chart_core_ctr_total = new google.visualization.ChartWrapper({
'chartType': 'Table',
'containerId': 'chart_core_ctr_total',
'options': {
  'width': '100%'
},
'dataTable':google.visualization.data.group(
      view_clicktocalls, [7], 
        [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_ctr_total.draw();

var chart_core_info_total = new google.visualization.ChartWrapper({
'chartType': 'Table',
'containerId': 'chart_core_info_total',
'options': {
  'width': '100%'
},
'dataTable':google.visualization.data.group(
      view_URL, [8], 
        [{'column': 4, 'aggregation': google.visualization.data.sum, 'type': 'number'}])
}); 
chart_core_info_total.draw();
new google.visualization.Dashboard(document.getElementById('dashboard')).
 // Establish bindings, declaring the both the slider and the category
 // picker will drive both charts.
 //bind([SitecategoryPicker, FormatcategoryPicker, MarketcategoryPicker, AdSizecategoryPicker, CouponcategoryPicker], [pie, table]).
 bind(SitecategoryPicker, FormatcategoryPicker).
     bind(FormatcategoryPicker, MarketcategoryPicker).
     bind(MarketcategoryPicker, AdSizecategoryPicker).
     bind(AdSizecategoryPicker, CouponcategoryPicker).
     bind(CouponcategoryPicker, [table]).
 // Draw the entire dashboard.

 draw(data);



google.visualization.events.addListener(table, 'ready',
     function(event) {
                     pie_imp.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 8, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_imp.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 8, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_imp_total.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [11],
                             [{'column': 8, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         pie_click.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 9, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_click.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 9, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_click_total.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [12],
                             [{'column': 9, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         pie_ctr.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 10, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_ctr.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [0],
                             [{'column': 10, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                         chart_core_ctr_total.setDataTable( google.visualization.data.group(

                          // get the filtered results
                             table.getDataTable(),
                             [13],
                             [{'column': 10, 'aggregation': google.visualization.data.sum, 'type': 'number'}]
                         ))
                        
                             // redraw the pie chart to reflect changes
                             pie_imp.draw();
                              chart_core_imp.draw();
                              chart_core_imp_total.draw();
                              // pie_click.draw();
                              //chart_core_click.draw();
                              //chart_core_click_total.draw();
                              //pie_ctr.draw();
                              // chart_core_ctr.draw();
                              //chart_core_ctr_total.draw(); 
                              
                         });
                        
//  var table = new google.visualization.Table(document.getElementById('table1'));
// table.draw(data1, null);   
var richMediaBoxEmptyCount = 0;
if ($('#chart1 div div').html().toLowerCase().indexOf("no data") >= 0) {
 richMediaBoxEmptyCount++;
 $('#analyticsBoard1').css('display','none');
}
if ($('#chart_click div div').html().toLowerCase().indexOf("no data") >= 0) {
 richMediaBoxEmptyCount++;
 $('#analyticsBoard2').css('display','none');
}
if ($('#chart_ctr div div').html().toLowerCase().indexOf("no data") >= 0) {
 richMediaBoxEmptyCount++;
 $('#analyticsBoard3').css('display','none');
}
if ($('#chart_info div div').html().toLowerCase().indexOf("no data") >= 0) {
 richMediaBoxEmptyCount++;
 $('#analyticsBoard4').css('display','none');
}

if(richMediaBoxEmptyCount == 4) {
	countEmptyDataTables();
}
richMediaBoxEmptyCount = 0;

}
google.setOnLoadCallback(drawVisualization);



/***************************************currently not in use methods of publisher.js ************************/


function getPublisherInventoryRevenueHeaderData() {
	  try{
	  var title=$('#publisher_allocation_val').html();
	  var count = 0;
	  $.ajax({
		  type : "POST",
		  url : "/loadPublisherInventoryRevenueHeaderDate.lin",
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
		           $.each(data, function(index, element) {
		        	  if (index == 'publisherInventoryRevenurList' && element.length > 0) {
							jsonResponse=element;
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
							
							$('#publisher_inventory_revenue_header').html(headerDiv);
							  updateDonutForInventoryHeader(jsonResponse[0].fillPercentage, 'c1');
							
					  }	
		        	  else if(index == 'publisherInventoryRevenurList' && element.length == 0)
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
		        		  
		        		  $('#publisher_inventory_revenue_header').html(headerDiv);
						  //updateDonutForInventoryHeader(jsonResponse[0].fillPercentage, 'c1');
						  updateDonutForInventoryHeader(0.00, 'c1');
		        	  }
		           });
		  
				  $('#publisher_inventory_revenue_header').html(headerDiv);
				  updateDonutForInventoryHeader(formatFloat(jsonResponse[0].fillPercentage,true), 'c1');
		  },
		      
		       error: function(jqXHR, exception) {	 
		    	   }
		  
		  });
	 		 
	  }catch(exception){
		}
}

function getPublisherTrendAnalysisHeaderData() {
	  var title=$('#publisher_allocation_val').html();
	  //alert(allChannelName);
	  var count = 0;
	  $.ajax({

		  type : "POST",
		  url : "/loadPublisherTrendsAnalysisHeaderDate.lin",
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
		           $.each(data, function(index, element) {
		        	  if (index == 'publisherTrendAnalysisHeaderList' && element.length>0) {
							jsonResponse=element;	
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
						  updateDonutForTrendAnalysisHeader(jsonResponse[0].fillRate, 'c2' );
					  }	
		        	  else if (index == 'publisherTrendAnalysisHeaderList' && element.length == 0) {
							jsonResponse=element;	
							headerDiv= getPublisherTrendsAnalysisHeader(
									"0",
									"0",
									"0.0000",
									"0",
									"0.0000",
									"0.0000",
									"0.0000",
									"0.0000");
						  $('#publisher_trends_analysis_header').html(headerDiv);
						  //updateDonutForTrendAnalysisHeader(jsonResponse[0].fillRate, 'c2' );
						  updateDonutForTrendAnalysisHeader(0.00, 'c2' );
					  }
		           });
		       
		       },
		       error: function(jqXHR, exception) {
		        }
		  
		  });
	 		 
}

function getPublisherReallocationHeaderData() {
	  var title=$('#publisher_allocation_val').html();
	  var count = 0;
	  $.ajax({
		  type : "POST",
		  url : "/loadPublisherReallocationHeaderDate.lin",
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
		        	  if (index == 'publisherReallocationHeaderList') {
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
				  $('#publisher_reallocation_header').html(headerDiv);
		       },
		       error: function(jqXHR, exception) {
		        }
		  
		  });
	 		 
}

function getChannelPerformance(){
	try{
		  $("#just-table tbody tr").remove();
	   	   $("#dtable1 tbody tr").remove();
	 		 $.ajax({
	 		       type : "POST",
	 		       url : "/channelPerformance.lin",
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
	 		    	 var flag=0;
	 		    	 var count=0;
	 		    	 var objForlastRow=data['channelPerformanceTotalObj'];
	 		           $.each(data, function(index, element) {
	 		        	   if(index == 'channelPerformanceList' && element != null && element.length > 0){
	 		        		  var dataList=data['channelPerformanceList'];
	 	 		        	   $("#just-table tbody tr").remove();
	 	 		        	   $("#dtable1 tbody tr").remove();
	 	 		        	   var listIndex=0;
	 	 		        	  
	 	 		        	   for(key in  dataList){
	 	 		        		
	 	 		        		 flag=1;
	 	 		        		  var dtoObject = dataList[key];
	 	 		        		  var id="channelPerformance_"+listIndex;
	 	 		        		  
	 	 		        	//	getChannelPerformanceDataArray[getChannelPerformanceDataArray.length]=new getChannelPerformanceData(newElement.id,newElement.title,newElement.author,newElement.body,newElement.login,newElement.image,newElement.position);	 		        	
	 	 		        		  
	 	 		        		  var row = "<tr id='"+id+"' " +
	 	 		        		  		//	"onclick=javascript:updateSecondAndThird('"+id+"') " +
	 	 		        		  			"style='cursor: hand; cursor: pointer;' class='changecolor'>" +
	 	 		        		  			"<td onclick=javascript:showChannelPerformancePopup('"+id+"') rel='popover'><img src='../img/search.png' /></td>"+
	 	 		        		  			"<td id='"+id+"_title' onclick=javascript:updateSecondAndThird('"+id+"')>"
	 	 		        		  			+dtoObject.channelName+"</td><td id='"+id+"_eCPM' onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'>$"+  formatFloat(dtoObject.eCPM,2)+"</td>";
	 	     			          
				 	 		  if(dtoObject.CHG == 0 || dtoObject.percentageCHG == 0){
	  	 		        			
	 	 		        			
	 	 		        			row = row+"<td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'>$"+  formatFloat(dtoObject.CHG,2)

		        			          +"</td><td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: center;'> --- "
		        			          +"<span id='"+id+"_changePercent' >" 
	           			          +"</span></td>";
			        			        
			        		  }
			        		  else if(dtoObject.CHG < 0 || dtoObject.percentageCHG < 0){
	 	 		        			 row = row+"<td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+  formatFloat(dtoObject.CHG,2)

	 	 		        			          +"</td><td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"
	 	 		        			          +"<span id='"+id+"_changePercent' >"+  formatFloat(dtoObject.percentageCHG,4) 
	 	 	             			          +"%</span></td>";
	 	 		        			        
	 	 		        		  }
	 	 		        	  else{
	 	 		        			 row = row+"<td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+ formatFloat(dtoObject.CHG,2)
	 	        			          +"</td><td onclick=javascript:updateSecondAndThird('"+id+"') style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>" 
	 	        			          +"<span id='"+id+"_changePercent' >"+  formatFloat(dtoObject.percentageCHG,4)
	 	        			          +"%</span></td>"; 		        			  
	 	 		        		  }
				 	 		        		
	 	 		        		   row = row+ "<td id='"+id+"_impressionDelivered' onclick=javascript:updateSecondAndThird('"+id+"') class='' style='text-align: right;'>"+ formatInt(dtoObject.impressionsDelivered)
	  			                    +"</td><td id='"+id+"_clicks' onclick=javascript:updateSecondAndThird('"+id+"') class='' style='text-align: right;'>"+  formatInt(dtoObject.clicks)
	  			                    +"</td><td id='"+id+"_ctr' onclick=javascript:updateSecondAndThird('"+id+"') class='' style='text-align: right;'>"+  formatFloat(dtoObject.CTR,4)+"%"
	  			                    +"</td><td id='"+id+"_payout' onclick=javascript:updateSecondAndThird('"+id+"') class='' style='text-align: right;'>$"+  formatFloat(dtoObject.payout,2) 
	  			                    +"</td></tr>";
	 	 		        		   
	 	 		        		   $("#just-table tbody").append(row);
	 	 		        		  listIndex++;
	 	 		        		  count++;
	 	 		        	   }
	    
	 		        	   }
	 		        	  else if(index == 'channelPerformanceList' && (element == null || element.length == 0)) {
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
	 		          
	 		          
	 		         if(flag == 1){
	 		        	 var noOfRows = count;
	 		        	updateSecondAndThird('channelPerformance_0');
	 		        	
	 		        	if(objForlastRow.CHG < 0 || objForlastRow.percentageCHG < 0){
	 		        		var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((objForlastRow.payout/objForlastRow.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(objForlastRow.CHG,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Down.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(objForlastRow.percentageCHG,4)+"%</td> <td style='text-align: right;'>"+formatInt(objForlastRow.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(objForlastRow.clicks)+"</td><td td style='text-align: right;'>"+formatFloat(objForlastRow.CTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(objForlastRow.payout,2)+"</td> </tr>";
	 		        	}
	 		        	else if(objForlastRow.CHG == 0 || objForlastRow.percentageCHG == 0){
	 		        		//var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((objForlastRow.payout/objForlastRow.impressionsDelivered)*1000,true)+"</td> <td style='text-align: right;'><img src='img/bullet.png'width='13' height='14' style='margin-right:5px;'>$"+formatFloat(objForlastRow.CHG,true)+"</td> <td style='text-align: right;'><img src='img/bullet.png' width='13' height='14' style='margin-right:5px;'>"+formatFloat(objForlastRow.percentageCHG,true)+"%</td> <td style='text-align: right;'>"+formatInt(objForlastRow.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(objForlastRow.clicks)+"</td><td td style='text-align: right;'>"+formatFloat(objForlastRow.CTR,true)+"%</td> <td style='text-align: right;'>$"+formatFloat(objForlastRow.payout,true)+"</td> </tr>";
	 		        		var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((objForlastRow.payout/objForlastRow.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'>$"+formatFloat(objForlastRow.CHG,2)+"</td> <td style='text-align: center;'> --- </td> <td style='text-align: right;'>"+formatInt(objForlastRow.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(objForlastRow.clicks)+"</td><td td style='text-align: right;'>"+formatFloat(objForlastRow.CTR,4)+"%</td> <td style='text-align: right;'>$"+formatFloat(objForlastRow.payout,2)+"</td> </tr>";
	 		        	}
	 		        	else{
	 		        		var lastRow ="<tr> <td></td><td>TOTAL</td> <td style='text-align: right;'>$"+formatFloat((objForlastRow.payout/objForlastRow.impressionsDelivered)*1000,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>$"+formatFloat(objForlastRow.CHG,2)+"</td> <td style='text-align: right;'><img src='img/Arrow2Up.png'width='11' height='12' style='margin-right:5px;'>"+formatFloat(objForlastRow.percentageCHG,4)+"%</td> <td style='text-align: right;'>"+formatInt(objForlastRow.impressionsDelivered)+"</td> <td style='text-align: right;'>"+formatInt(objForlastRow.clicks)+"</td><td td style='text-align: right;'>"+formatFloat(objForlastRow.CTR,4)+"%</td><td style='text-align: right;'>$"+formatFloat(objForlastRow.payout,2)+"</td> </tr>";
	 		        	}
	     	
	         		   
			        	$("#just-table tbody").append(lastRow); 		          
	 		         }
	 		       },
	 		      
	 		       error: function(jqXHR, exception) {
	 		    	   //alert("getChannelPerformance: exception:"+exception);
	 		        }
	 			
	 		  });
		}catch(error){
			//alert("getChannelPerformance: error:"+error);
		}
	 		 
	 }

function loadAllPublisherGraphs(){
	  pieChartGeneration('channelPerformance','IrPieChart_div','Fill Rate',piechartWidth);
	  //geoChartGeneration('performanceByProperty','geomap4','GEO CHART');
}

function loadReallocationData(){

	var publisherName=$("#publisher_allocation_val").text();
	//loadPublisherAllocationHeader(publisherName);
	getReallocationPublisherData();
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
                            + parseFloat(dtoObject.fillRate).toFixed(4)
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
	
}

function getActualPublisherData(dataList) {
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
	     }
	   });   
	  }catch(error){
	}	  
	
}

function getPerformanceByProperty(){
	  try{
		  $("#preloader1").show();
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
	 		          $('#preloader1').hide();
	 		       },
	 		      complete: function(){
	  		         $('#preloader1').hide();
	  		       },
	 		       error: function(jqXHR, exception) {
	 		    	  $("#preloader1").hide();
	 		    	  alert("getPerformanceByProperty: exception: "+exception);
	 		        }
	 			
	 		  });
	  }catch(error){
		  $("#preloader1").hide();
		  alert("getPerformanceByProperty: error: "+error);
	  }	 		 
}



/******************************************currently not in use methods of geoGraph.js ***********************/

google.load('visualization', '1', {'packages': ['geochart']});
google.setOnLoadCallback(drawGeoChart);
function drawGeoChart(divName,dataJsonStr,region,displayMode,colorAxisStr){
       var json_object = eval( '(' + colorAxisStr + ')'); 
       //alert("dataJsonStr: "+dataJsonStr)
       var json_arr = eval( '(' + dataJsonStr + ')');
       //alert("json_arr : "+json_arr);
 	     var data = google.visualization.arrayToDataTable(json_arr);
	     var options = {
	        region: region,
	        displayMode: displayMode,
	        colorAxis: json_object
	      };
	      var chart =null;
	      $("#"+divName).html("");
	      chart = new google.visualization.GeoChart(document.getElementById(divName));
	      try{
	        chart.draw(data, options);
	      }catch(error){
	    	  alert('Error in geo map:'+error);
	      }
 };
 
 
 /******************************************currently not in use methods of barGraph.js ************/
 
 google.load("visualization", "1", {packages:["corechart"]});
 google.setOnLoadCallback(drawBarGraph);
 function drawBarGraph(divName,title,dataStr,vAxisTitle,vAxisColor) {
	  var json_arr = eval( '(' + dataStr + ')');
	  var data = google.visualization.arrayToDataTable(json_arr);
   
   var options = {
     title: title,
     vAxis: {title: vAxisTitle,  titleTextStyle: {color: vAxisColor}}
   };

   var chart = new google.visualization.BarChart(document.getElementById(divName));
   chart.draw(data, options);
 }
 
 
 /******************************************currently not in use methods of areaGraph.js *******************/
 
 google.load("visualization", "1", {packages:["corechart"]});
 google.setOnLoadCallback(drawAreaGraph);
 function drawAreaGraph(dataStr,title,graphWidth,graphheight,hAxisTitle,titleTextStyle,divName) {
	  
	
	var json_arr = eval( '(' + dataStr + ')');

   var data = google.visualization.arrayToDataTable(json_arr);
   
   var options = {
     title: title,
	  width: graphWidth,
	  height: graphheight,
     hAxis: {title: hAxisTitle,  titleTextStyle: {color: 'red'}},
     legend:{position: 'none'}
   };

   var chart = new google.visualization.AreaChart(document.getElementById(divName));
   chart.draw(data, options);
 }
 
 
 /******************************************currently not in use methods of lineChart.js *********************/


 function drawLineChart(divName, title, dataStr) {
 	var json_arr = eval('(' + dataStr + ')');
 	var data = google.visualization.arrayToDataTable(json_arr);

 	var options = {
 		title : title
 	};

 	var chart = new google.visualization.LineChart(document.getElementById(divName));
 	chart.draw(data, options);
 }

 
