		<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
		<%@taglib uri="/struts-tags" prefix="s" %>
		<%@page import="com.lin.web.util.TabsName" %>
		 
		<jsp:include page="Header.jsp" /> 
		
		<!DOCTYPE html>
		<html lang="en">
		<head>
		
		<script>				
			localStorage.clear();
			var pageName="Report"; 
			var lastPopUpId = "";
		</script>
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT" />
		<%
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
		%>
		<title>Publisher Billing Report</title>
		
		<meta name="MobileOptimized" content="320">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		
		<jsp:include page="css.jsp"/>
		<script type='text/javascript' src='../js/accounting.js?v=<s:property value="deploymentVersion"/>'></script>
		<script type="text/javascript" src="../js/jquery-1.7.1.js?v=<s:property value="deploymentVersion"/>"></script>
			
		<link rel="stylesheet" href="../css/bootstrap-select.css?v=<s:property value="deploymentVersion"/>">
		<link rel="stylesheet" href="../css/theme.css?v=<s:property value="deploymentVersion"/>">
		<!-- <link rel="stylesheet" href="../css/datepicker_new.css"/> -->
			
		</head>
		
		<body >
		 <!-- .height-wrapper -->
			<div class="height-wrapper">
				<div id="main" role="main" class="container-fluid">
					<div class="contained">
						<!-- main content -->
						<div id="page-content" class="mlr">
							<jsp:include page="navigationTab.jsp" />
							<div id="tab-content1" class="tab-content">
							    <ul class="nav nav-tabs" id="myTab1" style="font-size:16px;">
							    	<%-- <s:if test="%{authorisationKeywordList.contains('cpCampaignView')}"> --%>
										<li class="active">
											<a href="#s1" id="campaignReport" data-toggle="tab"><%=TabsName.CAMPAIGN_REPORTING%></a>
										</li>
									<%-- </s:if> --%>
									<%-- <s:if test="%{authorisationKeywordList.contains('cpRichMediaView')}"> --%>
										<li >
											<a href="#s2" id="billingReport" data-toggle="tab"><%=TabsName.BILLING_REPORTING%></a>
										</li>
									<%-- </s:if> --%>
								</ul>
								<!-- <h1 style="margin: 1% 0 0 2%;" id="page-header">Campaign Reporting</h1> -->
								<div class="fluid-container" style="background: white;">
									<section id="widget-grid" class="">
										<s:form id="select-demo-js" cssClass="themed">
										
											<!-- filter Div Starts -->
											<div class="row-fluid" >
												<article class="span6">
													<div class="control-group" style="border-bottom: none;">
												    	<label class="control-label">Duration<span class="req star">*</span>&nbsp; &nbsp;<span style="color: red;" id="dateError"> </span>
												    	</label>
												    	<div class="well" style="float:left;margin-left: 3px;">	 
															<div id="daterange" class="pull-right" style="background: #fff; cursor: pointer; padding: 0px 3px; border: 1px solid #ccc">
																<i class="icon-calendar icon-large" style="float:left;"></i>
													            <div style="font-size:15px;font-weight:bold; margin-left: 27px;margin-right: 12px;margin-bottom: 6px;"></div>
													            <p style="float:left;font-size:14px;margin:0px;"></p> 
													            <b class="caret" style="margin-top: -15px;float:right;margin-left:5px;"></b>
													        </div>	   
												        </div>
													</div>
												</article>
												<article class="span6" style="margin-left: 5px;">
													<div class="control-group" style="border-bottom: 0px;">
														<label class="control-label">Campaign</label>
														<div class="controls">
							           						<input type="hidden" class="span12" name="campaignSearch" id="campaignSearch"/>
														</div>
													</div>
												</article>
											</div>
											
											<div class="row-fluid" >
												<article class="span6" style="margin-left: 5px;">
													<div class="control-group" style="border-bottom: 0px;">
														<label class="control-label">Partner</label>
														<div class="controls">
							           						<select id="partnerSelect" multiple="true" class="span12 with-search" name="partnerSelect">
			           										</select>
														</div>
													</div>
												</article>
												<article class="span6" style="margin-left: 5px;">
													<div id="placementDiv" class="control-group" style="border-bottom: 0px;">
														<label class="control-label">Placement</label>
														<div class="controls">
							           						<input type="hidden" class="span12" name="placementSearch" id="placementSearch"/>
							           						<div id="placementSelectDiv" style="display:none;">
								           						<select id="placementSelect" multiple="true" class="span12 with-search" name="placementSelect">
				           										</select>
				           									</div>
														</div>
													</div>
												</article>
											</div>
											
											<div id="advertiserAndFilterBy" class="row-fluid" >
												<article class="span6" style="margin-left: 5px;">
													<div class="control-group" style="border-bottom: 0px;">
														<label class="control-label">Advertiser</label>
														<div class="controls">
							           						<input type="hidden" class="span12" name="accountSearch" id="accountSearch"/>
														</div>
													</div>
												</article>
												<article class="span6" style="margin-left: 5px;">
													<div class="control-group" style="border-bottom: 0px;">
														<label class="control-label">Filter By</label>
														<div class="controls">
															<select id="filterBySelect" class="span12 with-search" name="filterBySelect">
																<option value="1" selected="selected">Day</option>
																<option value="2">Week</option>
																<option value="3">Month</option>
				           									</select>
														</div>
													</div>
												</article>
											</div>
											<!-- filter Div Ends -->
											
											<div class="control-group">
												<button type="button" id="runReportBtn" onclick="javascript:runCampaignReport();" class="btn btn-info btn-large">Run Report</button>
												<button type="button" id="billingReportBtn" onclick="javascript:runBillingReport();" class="btn btn-info btn-large" style="display: none;">Billing Report</button>
											</div>
											
											<!-- Delivery Metrics Starts -->
											<div class="row-fluid" style="width: auto; height: auto; display: inline;">
											<div id="reportMetricsLoader" class="span12" style="text-align:center;margin-top:10px;display:none;"><img src="img/loaders/type4/light/46.gif" alt="loader"></div>
												<div id="reportMetricsDiv" style="display: none;">
													<article class="span12" style="margin: 40px 16px;">
														<div class="jarviswidget" style="margin-right: 2%;">
															<header>
																<h2>REPORT METRICS</h2>
															</header>
				            
													        <div class="inner-spacer">
																<table class="table dtable" id="reportMetrics">
																	<thead>
																		<tr>
																			<th style="width:10%;">Date</th>
																			<th style="width:18%;">Publisher</th>
																			<th style="width:9%;">Advertiser</th>
																			<th style="width:9%;">Campaign</th>
																			<th style="width:9%;">Placement</th>
																			<th style="width:9%;">Impressions</th>
																			<th style="width:9%;">Clicks</th>
																			<th style="width:9%;">CTR</th>
																			<th style="width:9%;">eCPM</th>
																			<th style="width:9%;">Cost</th>
																		</tr>
																	</thead>
																	<tbody>
																	
																	</tbody>
																</table>
															</div>
														</div>
													</article>
												</div>
											</div>
											<!-- Delivery Metrics Ends -->
											<div style="margin-bottom:18px;color: white;">.</div>
												
										</s:form>
									</section>
								</div>
							</div>
						</div>
						<!-- end main content -->
					<!-- </div> -->
				</div>
				<!--end fluid-container-->
			</div>
		</div>
		<!-- end of jsp code -->
			
			
		 <jsp:include page="js.jsp"/>
		 <%-- <script type="text/javascript" src="../js/daterangepicker.js?v=<s:property value="deploymentVersion"/>"></script>  
		 <script type="text/javascript" src="../js/poolmap/poolmap-datepicker.js?v=<s:property value="deploymentVersion"/>"></script> --%>
		 <script src="../js/bootstrap-datepicker_new.js"></script>
		 <script type="text/javascript" src="../js/bootstrap-select.js"></script>
		 
		 <script>
		 var campaignReportTab = "CampaignReport";
		 var billingReportTab = "billingReport";
		 var selectedTab = campaignReportTab;
		 
		 var company = '<s:property value="company"/>';
		 var publisherBQId = '<s:property value="publisherBQId"/>';
		 
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
		 
		 if(startDate == undefined) {
			  var startDate ;
			  var endDate;
		 }
		 
		 $('#billingReport').click(function() {
			selectedTab = billingReportTab;
			$('#advertiserAndFilterBy').hide();
			$('#placementDiv').hide();
			$('#runReportBtn').css('display','none');
			$('#billingReportBtn').css('display','block');

			$("#campaignSearch").select2("data",[]);
			$('#partnerSelect').select2('data', []);
			
			$('#reportMetricsDiv').css('display','none');
			$('#reportMetricsLoader').hide();
		});
		 $('#campaignReport').click(function() {
			selectedTab = campaignReportTab;
			$('#advertiserAndFilterBy').show();
			$('#placementDiv').show();
			$('#billingReportBtn').css('display','none');
			$('#runReportBtn').css('display','block');
			
			$("#campaignSearch").select2("data",[]);
			$("#placementSelect").select2("data",[]);
			$('#placementSearch').select2("data",[]);
			$('#partnerSelect').select2('data', []);
			$('#accountSearch').select2('data', []);
			$('#filterBySelect').select2('val', 1);
			
			$('#reportMetricsDiv').css('display','none');
			$('#reportMetricsLoader').hide();
			$('#runReportBtn').removeAttr('disabled');
		});
		 
		 $(document).ready(function() {
			try {
				if(Math.floor(moment().month() / 3) + 1 == '1'){
					var range_start_quater = moment().startOf('year');
					var range_end_quater = moment().startOf('year').add('month', 2).add('days',30);
					var range_start_lastquater = moment().startOf('year').subtract('month',3);
					var range_end_lastquater = moment().endOf('year').subtract('month', 12);
				}else if(Math.floor(moment().month() / 3) + 1 == '2'){
			    	var range_start_quater = moment().startOf('year').add('month', 3);
			    	var range_end_quater = moment().startOf('year').add('month', 5).add('days',29);
			    	var range_start_lastquater = moment().startOf('year');
			    	var range_end_lastquater = moment().startOf('year').add('month', 2).add('days',30);
			    }else if(Math.floor(moment().month() / 3) + 1 == '3'){
			    	var range_start_quater = moment().startOf('year').add('month', 6);
			    	var range_end_quater = moment().startOf('year').add('month', 8).add('days',29);
			    	var range_start_lastquater = moment().startOf('year').add('month', 3);
			    	var range_end_lastquater = moment().startOf('year').add('month', 5).add('days',29);
			    }else if(Math.floor(moment().month() / 3) + 1 == '4'){
			    	var range_start_quater = moment().startOf('year').add('month', 9);
			    	var range_end_quater = moment().endOf('year');
			    	var range_start_lastquater = moment().startOf('year').add('month', 6);
			    	var range_end_lastquater = moment().startOf('year').add('month', 8).add('days',29);
			    }
								
			    $('#daterange').daterangepicker({
			        ranges: {
			           'Today': [new Date(), new Date()],
			           'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
			           'Week To Date': [moment().startOf('week'), new Date()],
			           'Month To Date': [moment().startOf('month'), new Date()],
			           'Quarter To Date': [range_start_quater, new Date()],
			           'Year To Date': [moment().startOf('year'), new Date()],
			           
			           'Previous Week': [moment().subtract('week', 1).startOf('week'), moment().subtract('week', 1).endOf('week')],
			           'Previous Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')],
			 		   'Previous Quater':[range_start_lastquater,range_end_lastquater],
			 		   'Previous Year':[moment().startOf('year').subtract('month', 12), moment().endOf('year').subtract('month', 12)],
			 		   
			           'Last 7 Days': [moment().subtract('days', 6), new Date()],
			           'Last 30 Days': [moment().subtract('days', 29), new Date()],
			           'Last 90 Days': [moment().subtract('days', 89), new Date()],
			           'Last 365 Days': [moment().subtract('days', 364), new Date()],
			           
			           'Last 24 Months': [moment().subtract('month', 23), new Date()],
			        },
			        
			        opens: 'right',
			        format: 'MM/DD/YYYY',
			        separator: ' to ',
			        startDate: moment().startOf('month'), //new Date(),
			        endDate:   moment().add('days', 0), //moment().add('days', 30),
			        //minDate:  new Date(), //'01/01/2012',
			        //maxDate: new Date(),
			        locale: {
			            applyLabel: 'Apply',
			            fromLabel: 'From',
			            toLabel: 'To',
			            customRangeLabel: 'Custom Range',
			            daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
			            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
			            firstDay: 1
			        },
			        showWeekNumbers: false,
			        buttonClasses: [''],
			        dateLimit: false
			     },
			      function(start, end) {
			    	 $('#daterange div').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY') +'</br>');
		 		     $('#daterange p').html('');
		 		     startDate = start.format('YYYY-MM-DD');
		 	         endDate = end.format('YYYY-MM-DD');
		 	         console.log("startDate : "+startDate+", endDate : "+endDate);
			      }
			   ); 				
			    
			    $('#custom_compare_div').hide();
				$('#dateRangeStartDateText').hide();
				$('#dateRangeEndDateText').hide();
			    var momentStartDate = moment().startOf('month');
			    var momentEndDate = moment().add('days', 0);
					
				$('#daterange div').html(momentStartDate.format('MMMM D, YYYY') + ' - ' +  momentEndDate.format('MMMM D, YYYY'));
				
				startDate=momentStartDate.format('YYYY-MM-DD');    	 
				endDate=momentEndDate.format('YYYY-MM-DD');
		        console.log("startDate : "+startDate+", endDate : "+endDate);
		        
			}catch(err) {
				console.log("Error in date picker : "+err);
			}
			 
			try {
				$('#reportLi').attr('class', 'main-nav-li_selected');
				getPartners();
				
				$('#campaignSearch').select2({
	    	        minimumInputLength: 1,
	    	        multiple : true,
	    	        placeholder: 'Search Campaign',
	    	        ajax: {
	    	            url: "/searchCampaigns.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "campaigns",
	    	                    limit: -1,
	    	                    searchText: term,
	    	                    company : company
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	                return { results: data.campaigns }
	    	            }
	    	        },
	    	        formatResult: function(campaigns) {
	    	            return "<div class='select2-user-result'>" + campaigns.name + "</div>";
	    	        },
	    	        formatSelection: function(campaigns) { 
	    	            return campaigns.name; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	    });
	    	    $('#campaignSearch').on("change", function(e) {
	    	    	var val = $('#campaignSearch').val();
	    	    	$("#placementSelect").select2("data",[]);
    				$('#placementSearch').select2("data",[]);
	    			if (val != undefined && val != null && $.trim(val) != '') {
	    				$('#s2id_placementSearch').css('display','none');
	    				$('#placementSelectDiv').css('display','block');
	    				getSelectedPlacements();
	    			}else {
	    				$('#placementSelectDiv').css('display','none');
	    				$('#s2id_placementSearch').css('display','block');
	    			}
   	    		});
	    	    
	    	    $('#placementSearch').select2({
	    	        minimumInputLength: 1,
	    	        multiple : true,
	    	        placeholder: 'Search Placements',
	    	        ajax: {
	    	            url: "/searchPlacements.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "placements",
	    	                    limit: -1,
	    	                    searchText: term,
	    	                    company : company
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	                return { results: data.placements }
	    	            }
	    	        },
	    	        formatResult: function(placements) {
	    	            return "<div class='select2-user-result'>" + placements.name + "</div>";
	    	        },
	    	        formatSelection: function(placements) { 
	    	            return placements.name; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	    });
	    	    
	    	    $('#accountSearch').select2({
	    	        minimumInputLength: 1,
	    	        multiple : true,
	    	        placeholder: 'Search Advertisers',
	    	        ajax: {
	    	            url: "/searchAccountsForCampaignReport.lin",
	    	            dataType: 'json',
	    	            quietMillis: 100,
	    	            data: function(term, page) {
	    	                return {
	    	                    types: "accounts",
	    	                    limit: -1,
	    	                    searchText: term
	    	                };
	    	            },
	    	            results: function(data, page ) {
	    	                return { results: data.accounts }
	    	            }
	    	        },
	    	        formatResult: function(accounts) {
	    	            return "<div class='select2-user-result'>" + accounts.name + "</div>";
	    	        },
	    	        formatSelection: function(accounts) { 
	    	            return accounts.name; 
	    	        },
	    	        initSelection : function (element, callback) {
	    	            var elementText = $(element).attr('data-init-text');
	    	            callback({"searchText":elementText});
	    	        },
	    	    });
	    	    
			}catch(err){
				console.log("error:"+err);
			}
		 });
		 
		 function getSelectedPlacements() {
 	    	var campaignIds = $('#campaignSearch').val();
 	    	$("#placementSelect").select2("data",[]);
 	    	$('#placementSelect').html('');
 	    	if(campaignIds != undefined && campaignIds != '') {
 	    		try{	  
 	    		    $.ajax({
 	    		      type : "POST",
 	    		      url : "/selectedPlacements.lin",
 	    		      cache: false,
 	    		      data : {
 	    		    	  campaignIds : campaignIds
 	    			      },		    
 	    		      dataType: 'json',
 	    		      success: function (data) {
 	    		    	  var selectedPlacements = data.placements;
	    		    		  if (selectedPlacements != null  && selectedPlacements != undefined  && selectedPlacements.length > 0) {
	    		    			  $.each(selectedPlacements,function (newIndex,newElement){		         		  
	    			         		  $('#placementSelect').append('<option value="'+newElement.id+'">'+newElement.name+'</option>');
	    			         	   });
	    		    		  }
 	    		     },
 	    		     error: function(jqXHR, error) {
 	    		    	console.log("error : "+error);
 	    		     }
 	    		   });   
 	    	  }catch(error){
 	    		 console.log("error : "+error);
 	    	  }
 	       }
 	    }
		 
		function getPartners() {
    		try{	  
    		    $.ajax({
    		      type : "POST",
    		      url : "/selectedPartners.lin",
    		      cache: false,		    
    		      dataType: 'json',
    		      success: function (data) {
    		    	  var partners = data.partners;
   		    		  if (partners != null  && partners != undefined  && partners.length > 0) {
   		    			  $.each(partners,function (newIndex,newElement){		         		  
   			         		  $('#partnerSelect').append('<option value="'+newElement.id+'">'+newElement.name+'</option>');
   			         	   });
   		    		  }
    		     },
    		     error: function(jqXHR, error) {
    		    	 console.log("error : "+error);
    		     }
    		   });   
			}catch(error){
    		  	console.log("error : "+error);
    	  	}
 	    }
		
		function isValidDuration() {
			var sDate = new Date($('#startDate').val());
	        var eDate = new Date($('#endDate').val());
	        if(sDate!=null && sDate!=undefined && eDate !=null && eDate !=undefined && sDate>eDate){
	        	$('#dateError').html("End date should be greater");
	        	$("#endDate").focus();
	        	return false;
	        }else if(sDate!=null && sDate!=undefined && endDate !=null && eDate !=undefined) {
	        	$('#dateError').html("");
	        	return true;
	        }else {
	        	$('#dateError').html("Invalid Date");
	        	$("#endDate").focus();
	        	return false;
	        }
		}
		
		function runCampaignReport() {
			$('#reportMetricsDiv').css('display','none');
			var campaignIds = $('#campaignSearch').val();
			var partnerArr = $('#partnerSelect').val();
			var accounts = $('#accountSearch').val();
			/* var startDate = $('#startDate').val();
			var endDate = $('#endDate').val(); */
			var placementIdArr = [];
			var partners = "";
			var placementIds = "";
			if (campaignIds != undefined && campaignIds != null && $.trim(campaignIds) != '') {
				placementIdArr = $('#placementSelect').val();
				if(placementIdArr != null && placementIdArr != undefined && placementIdArr.length > 0) {
					placementIds = placementIdArr.join(',');
				}
			}else {
				placementIds = $('#placementSearch').val();
			}
			
			if(partnerArr != null && partnerArr != undefined && partnerArr.length > 0) {
				partners = partnerArr.join(',');
			}
			
			if(accounts != null && accounts != undefined && accounts.length > 0) {
				accounts = accounts.replaceAll('_', '');
			}
			if(startDate == undefined || startDate == null || startDate == '') {
				toastr.error('Start date is mandatory');
				return false;
			}
			else if(endDate == undefined || endDate == null || endDate == '') {
				toastr.error('End date is mandatory');
				return false;
			}
			if(!isValidDuration()) {return false;}
			$('#runReportBtn').attr('disabled', 'disabled');
			
			$('#reportMetricsLoader').show();
			jQuery('#reportMetrics').dataTable().fnClearTable();
			
			try{	  
    		    $.ajax({
    		      type : "POST",
    		      url : "/runCampaignReport.lin",
    		      cache: false,		    
    		      dataType: 'json',
    		      data : {
	   		    	  campaignIds : campaignIds,
		   		   	  placementIds : placementIds,
		   		      partners : partners,
		   		   	  accounts : accounts,
		   		   	  reportType : $('#filterBySelect option:selected').text(),
		   		      startDate : startDate,
		   		      endDate : endDate,
		   		   	  company:company,
		   		   	  publisherBQId : publisherBQId
	   			      },
    		      success: function (data) {
    		    	  var key = 0;
    		    	  var metrics = data.reportMetrics;
    		    	  var error = data.error;
    		    	  var title = data.title;
    		    	  if(selectedTab == campaignReportTab) {
	   		    		  if (metrics != null  && metrics != undefined  && metrics.length > 0) {
	   		    			  $('#reportMetricsDiv').css('display','block');
	   		    			  var oTableTools = TableTools.fnGetInstance('reportMetrics');
		   		 			  if ( oTableTools != null && oTableTools.fnResizeRequired()) {
		   		 				  oTableTools.fnResizeButtons();
		   		 			  }
	   		    			  $('.DTTT_container').hide();
	   		    			  jQuery('#reportMetrics').dataTable().fnClearTable();
	   		 				  jQuery('#reportMetrics').dataTable().fnSettings()._iDisplayLength = 20;
	   		 				  jQuery('#reportMetrics').dataTable().fnDraw();
		   		 			  for (var i = 0 ; i < metrics.length ; i = i + 1) {
		   			 			   (function(i) {
		   			 				   setTimeout( function(i) {
		   			 					  var dataObject = metrics[key];
		   			 					  jQuery('#reportMetrics').dataTable().fnAddData( [
												 dataObject.date,
											     dataObject.partner,
												 dataObject.advertiserName,
												 dataObject.campaignName,
												 dataObject.placementName,
											     dataObject.impressions,
												 dataObject.clicks,
												 dataObject.ctr,
												 dataObject.rate,
												 dataObject.cost
		   						   		]);
		   			 					key ++;
		   			 					if(key == metrics.length) {
		   			 						$('.DTTT_container').show();
		   			 						$('#runReportBtn').removeAttr('disabled');
		   			 					}
		   			 			     }, 10);
		   			 			  })(i);
		   			 		  }
	   		    		  }else if(error != null  && error != undefined  && error != ''){
	      		    		  toastr.error("Error in fetching report : "+error);
	      		    		  $('#runReportBtn').removeAttr('disabled');
	   		    		  } else {
	   		    			toastr.error("No records for selected filters.");
	   		    			$('#runReportBtn').removeAttr('disabled');
	   		    		  }
    		    	  }else {
    		    		  console.log('Report tab switched, no need to fill table, selected tab : '+selectedTab);
    		    		  $('#runReportBtn').removeAttr('disabled');
    		    	  }
   		    		  $('#reportMetricsLoader').hide();
    		     },
    		     error: function(jqXHR, error) {
    		    	 console.log("error : "+error);
    		    	 toastr.error("Error in fetching report");
    		    	 $('#reportMetricsLoader').hide();
    		    	 $('#runReportBtn').removeAttr('disabled');
    		     }
    		   });   
			}catch(error){
    		  	console.log("error : "+error);
    		  	$('#runReportBtn').removeAttr('disabled');
    	  	}
		}
		
		function runBillingReport() {
			var campaignIds = $('#campaignSearch').val();
			var partnerArr = $('#partnerSelect').val();
			var partners = "";
			
			try {
				if(partnerArr != null && partnerArr != undefined && partnerArr.length > 0) {
					partners = partnerArr.join(',');
				}
				
				if(startDate == undefined || startDate == null || startDate == '') {
					toastr.error('Start date is mandatory');
					return false;
				}
				else if(endDate == undefined || endDate == null || endDate == '') {
					toastr.error('End date is mandatory');
					return false;
				}
				if(!isValidDuration()) {return false;}
				
				var urlParameters = "?campaignIds="+campaignIds+"&partners="+partners+"&startDate="+startDate+
					"&endDate="+endDate+"&publisherBQId="+publisherBQId+"&company="+company;
				toastr.info("Checking if records available for selected filters.. Please wait");
				$.ajax({
    		      type : "POST",
    		      url : "/checkBillingReport.lin"+urlParameters,
    		      cache: false,		    
    		      dataType: 'json',
    		      success: function (data) {
    		    	  if(selectedTab == billingReportTab) {
	    		    	  toastr.clear();
	    		    	  var success = data.success;
	    		    	  var error = data.error;
	    		    	  if(error != null && error != undefined) {
	    		    		  toastr.error(error);
	    		    	  }else if(success != null && success != undefined && success) {
	    		    		  toastr.info('Report will be downloaded shortly.');
	    		    		  location.href = "/billingReport.lin"+urlParameters;
	    		    	  }else {
	    		    		  toastr.error("Error in creating report");
	    		    	  }
    		    	  }else {
    		    		  console.log('Report tab switched, no need to download report, selected tab : '+selectedTab);
    		    	  }
    		     },
    		     error: function(jqXHR, error) {
    		    	 console.log("error : "+error);
    		     }
    		   });
			} catch (exception) {
				console.log("Error in runBillingReport : " + exception);
			}
		}
		
		 </script>
		
		</body>
		<jsp:include page="googleAnalytics.jsp"/>
		</html>
